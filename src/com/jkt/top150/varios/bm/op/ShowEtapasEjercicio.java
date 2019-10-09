package com.jkt.top150.varios.bm.op;

import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bm.Ejercicio;

public class ShowEtapasEjercicio extends Operation implements IObserver{
	private XMLTableMaker makerE = new XMLTableMaker("Etapas", this);
	private List etapas;

	public Integer execute(MapDS aParams) throws Exception {
		writer.addTabla("Ejercicios");

		IObjectServer server = sesion.getObjectServer(Etapa.class);
		etapas = (List) server.getObjects(IDB.SELECT_ACTIVOS, null, new ListObserver());

		this.writeEtapas();

		server = sesion.getObjectServer(Ejercicio.class);
		server.getObjects(IDB.SELECT_ALL, null, this);

		return new Integer(0);
	}

	public Object getResult(){
		return null;
	}

	public void notify(Object aObj) throws ExceptionDS{
		Ejercicio ejer = (Ejercicio) aObj;
		writer.addFila();
		writer.addColumna("oid_ejercicio", ejer.getOID());
		writer.addColumna("anio",          ejer.getAnio());
		writer.addColumna("actual",        ejer.isActual());

		XMLTableMaker maker  = new XMLTableMaker("EtapasEjercicio_" + ejer.getOID(), this);

		Iterator it = etapas.iterator();
		while(it.hasNext()){
			Etapa etapa = (Etapa) it.next();

			EjercicioEtapas next = ejer.getEjercicioEtapas(etapa);

			maker.addFila();
			maker.addColumna("descripcion",    etapa.getDescripcion());
			maker.addColumna("oid_etapa",      etapa.getOID());

			if(!next.isNew()){
				maker.addColumna("oid_etapa_ejer", next.getOID());
				maker.addColumna("iniciada",       next.isIniciada());
				maker.addColumna("cerrada",        next.isCerrada());
			}
			else{
				maker.addColumna("oid_etapa_ejer", 0);
				maker.addColumna("iniciada",       false);
				maker.addColumna("cerrada",        true);
			}
		}
	}

	private void writeEtapas() throws ExceptionDS{
		Iterator it = etapas.iterator();

		while(it.hasNext()){
			Etapa next = (Etapa) it.next();

			makerE.addFila();
			makerE.addColumna("oid_etapa",   next.getOID());
			makerE.addColumna("descripcion", next.getDescripcion());
		}
	}
}