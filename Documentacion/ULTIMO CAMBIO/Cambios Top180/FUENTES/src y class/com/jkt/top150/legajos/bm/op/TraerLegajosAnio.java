package com.jkt.top150.legajos.bm.op;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.ListOrderer;
import com.jkt.top150.varios.bm.Ejercicio;

public class TraerLegajosAnio extends Operation {

	public Integer execute(MapDS arg0) throws Exception {
		writer.addTabla("Legajos");

		Map cond = new HashMap();
		cond.put("Ejercicio", Ejercicio.getEjercicioActual(sesion));

		if(arg0.containsKey("administrador"))
			cond.put("Administrador", new Integer(1));

		if(arg0.containsKey("evaluador"))
			cond.put("Evaluador", new Integer(1));

		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
		List list = (List) server.getObjects(LegajoEjer.SELECT_ALL_BY_ANIO, cond, new ListObserver());

		Collections.sort(list, new ListOrderer());

		Iterator it = list.iterator();
		while(it.hasNext())
			this.writeLegajo((LegajoEjer) it.next());	

		if(arg0.containsKey("ninguno")){
			writer.addFila();
			writer.addColumna("oid_leg",  0);
			writer.addColumna("oid_eval", 0);
			writer.addColumna("legajo",  "");
			writer.addColumna("ape",     "NINGUNO");
			writer.addColumna("nom",     "NINGUNO");
		}

		return OK;
	}

	private void writeLegajo(LegajoEjer lEjer) throws ExceptionDS {
		writer.addFila();
		writer.addColumna("oid_leg",  lEjer.getOID());
		writer.addColumna("oid_eval", lEjer.getEvaluador() != null ? lEjer.getEvaluador().getOID() : 0);
		writer.addColumna("legajo",   lEjer.getLegajo().getLegajo());
		writer.addColumna("ape",      lEjer.getLegajo().getApellidos());
		writer.addColumna("nom",      lEjer.getLegajo().getNombres());

		String descripcion = "";

		if(lEjer.getEvaluador() == null){//ES GERENTE
			descripcion = "- Administrador: (No asignado)";

			LegajoEjer administrador = lEjer.getAdminGerencia();
			if(administrador != null)
				descripcion = "- Administrador: " + administrador.getDescripcion();
		}

		writer.addColumna("administrador", descripcion);
	}
}