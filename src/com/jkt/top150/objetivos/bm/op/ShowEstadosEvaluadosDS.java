package com.jkt.top150.objetivos.bm.op;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EvaluadorHandler;
import com.jkt.top150.varios.bl.ListOrderer;
import com.jkt.top150.varios.bm.Ejercicio;

public class ShowEstadosEvaluadosDS extends Operation {

	public static final String OBJETIVOS     = "Objetivos";
	public static final String CUMPLIMIENTOS = "Cumplimientos";
	public static final String CAPACIDADES   = "Competencias";

	private XMLTableMaker detalle;
	private Etapa etapa;

	public Integer execute(MapDS arg0) throws Exception {
		etapa = Etapa.getEtapaActual(sesion);

		arg0.put("Ejercicio", Ejercicio.getEjercicioActual(sesion));

		writer.addTabla("Evaluados");

		this.borrarClavesInvalidas(arg0, "nombres");
		this.borrarClavesInvalidas(arg0, "apellido");
		this.borrarClavesInvalidas(arg0, "legajo");

		if(arg0.containsKey("oid_evaluador")){
			Integer oid = arg0.getInteger("oid_evaluador"); 
			if(oid.intValue() < 0)
				arg0.remove("oid_evaluador");
		}

		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
		List lista = (List) server.getObjects(LegajoEjer.SELECT_EVALUADOS_POR_ANIO, arg0, new ListObserver());
		Collections.sort(lista, new ListOrderer());
		
		Iterator it = lista.iterator();
		while(it.hasNext())
			this.writeLegajo((LegajoEjer) it.next());

		EvaluadorHandler handler = new EvaluadorHandler();
		handler.writeEvaluadores(writer, (UsuarioRRHH) sesion.getLogin().getUsuario());

		return OK;
	}

	private void borrarClavesInvalidas(MapDS aParams, String aKey) throws ExceptionDS{
		if(!aParams.containsKey(aKey))
			return;

		String valor = aParams.getString(aKey).trim(); 
		if(valor.length() == 0)
			  aParams.remove(aKey);
		else aParams.put(aKey, "%" + valor.toUpperCase() + "%");
	}

	private void writeLegajo(LegajoEjer ejer) throws ExceptionDS {
		int oidLeg = ejer.getLegajo().getOID();

		writer.addFila();
		writer.addColumna("oid_leg",     oidLeg);
		writer.addColumna("oid_leg_eje", ejer.getOID());
		writer.addColumna("legajo",    ejer.getLegajo().getLegajo());
		writer.addColumna("nombre",    ejer.getLegajo().getApNom());

		if(ejer.tieneAsignadoUnEvaluador())
			  writer.addColumna("evaluador", ejer.getEvaluador().getLegajo().getApNom());
		else writer.addColumna("evaluador", "");

		detalle = new XMLTableMaker("Estados_" + oidLeg, this);
		detalle.addFila();
		detalle.addColumna("titulo", OBJETIVOS);

		if(etapa.isCargaObjetivo())
			detalle.addColumna("valor", ejer.getEstadoCargaObjStr());
		else{
			if(ejer.finCargaCapacEvaluado())
				  detalle.addColumna("valor", LegajoEjer.CERRADO);
			else detalle.addColumna("valor", LegajoEjer.FIN_PLANEAMIENTO);
		}

		detalle.addFila();
		detalle.addColumna("titulo", CUMPLIMIENTOS);

		if(etapa.isCargaCumplimiento())
			  detalle.addColumna("valor", ejer.getEstadoCumplimientosStr());
		else detalle.addColumna("valor",  LegajoEjer.INACTIVO);

		detalle.addFila();
		detalle.addColumna("titulo", CAPACIDADES);

		if(etapa.isEvaluaCapacidades())
			  detalle.addColumna("valor", ejer.getEstadoCapacidadesStr());
		else detalle.addColumna("valor", LegajoEjer.INACTIVO);
	}
}