package com.jkt.top150.legajos.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bm.Ejercicio;

public class VerRolLegajos extends Operation implements IObserver{
	private Ejercicio ejercicio;

	public Integer execute(MapDS aParams) throws Exception {
		IObjectServer sEjer = sesion.getObjectServer(Ejercicio.class);
		ejercicio = (Ejercicio) sEjer.getObjectByOID(aParams.getInteger("oid_ejercicio"));

		writer.addTabla("Roles");

		IObjectServer server = sesion.getObjectServer(Legajo.class);
		server.getObjects(IDB.SELECT_ALL, null, this);

		return new Integer(0);
	}

	public Object getResult(){
		return null;
	}

	public void notify(Object aObj) throws ExceptionDS{
		Legajo leg = (Legajo) aObj;
		writer.addFila();

		writer.addColumna("oid_legajo",       leg.getOID());
		writer.addColumna("legajo",           leg.getLegajo());
		writer.addColumna("nombres",          leg.getNombres());
		writer.addColumna("apellido",         leg.getApellidoPaterno());

		LegajoEjer legE = leg.getLegajoEjer(ejercicio);

		writer.addColumna("oid_leg_ejer",     legE.getOID());
		writer.addColumna("es_evaluador",     legE.isEvaluador());
		writer.addColumna("es_evaluado",      legE.isEvaluado());
		writer.addColumna("es_administrador", legE.isAdministradorGerencia());
	}
}