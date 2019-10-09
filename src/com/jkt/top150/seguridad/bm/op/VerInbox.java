package com.jkt.top150.seguridad.bm.op;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.EvaluadorHandler;
import com.jkt.top150.varios.bl.HeaderWriter;
import com.jkt.top150.varios.bl.ListOrderer;

public class VerInbox extends Operation implements IObserver{
	private static final String OBJETIVOS     = "<span style='color: black' onmouseover='showTipos();'>C. Objetivos</span>";
	private static final String CUMPLIMIENTOS = "<span style='color: black' onmouseover='showTipos();'>Cumplimientos</span>";
	private static final String CAPACIDADES   = "<span style='color: black' onmouseover='showTipos();'>Competencias</span>";

	private XMLTableMaker detalle;
	private Etapa etapa;
	
	//private List visto = new ArrayList();
	private Map visto = new HashMap();

	public Integer execute(MapDS aParams) throws Exception {
		etapa = Etapa.getEtapaActual(sesion);

		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
		Legajo leg = null;

		writer.addTabla("Evaluados");

		try{
			leg = user.getLegajo();
			user.getEvaluados(this, aParams);
		}
		catch(ExceptionValidacion e){
			//POR SI EL LEGAJO NO ESTA CONFIGURADO EN EL ETAPA
		}
		
		this.writeEvaluados();

		EvaluadorHandler handler = new EvaluadorHandler();
		handler.writeEvaluadores(writer, user);

		HeaderWriter hw = new HeaderWriter(sesion);
		hw.write(writer, leg, EstadosHandler.INBOX);

		return OK;
	}

	public Object getResult(){
		return null;
	}

	public void notify(Object aObj) throws ExceptionDS{
		LegajoEjer ejer = (LegajoEjer) aObj;

		visto.put(ejer.getOIDInteger(), ejer);
		//if(!visto.contains(ejer))
			//visto.add(ejer);
	}
	
	private void writeEvaluados() throws ExceptionDS{
		List lista = new ArrayList(visto.values());
		Collections.sort(lista, new ListOrderer());
		
		Iterator it = lista.iterator();
		while(it.hasNext()){
			LegajoEjer ejer = (LegajoEjer) it.next();
			
			int oidLeg = ejer.getLegajo().getOID();

			writer.addFila();
			writer.addColumna("oid_leg", oidLeg);
			writer.addColumna("legajo",    ejer.getLegajo().getLegajo());
			writer.addColumna("nombre",    ejer.getLegajo().getApNom());

			if(ejer.tieneAsignadoUnEvaluador())
				  writer.addColumna("evaluador", ejer.getEvaluador().getLegajo().getApNom());
			else writer.addColumna("evaluador", "");

			writer.addColumna("periodo",   ejer.getEjercicio().getAnio());

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

	protected void tomarParams(MapDS aParams) throws ExceptionDS {
		super.tomarParams(aParams);

		try{
			sesion.putAttribute("colNumber", aParams.getInteger("col"));
		}
		catch(Exception e){}

		try{
			sesion.putAttribute("order",    aParams.getString("order"));
		}
		catch(Exception e){}

		this.borrarClavesInvalidas(aParams, "nombres");
		this.borrarClavesInvalidas(aParams, "apellido");
		this.borrarClavesInvalidas(aParams, "legajo");

		if(aParams.containsKey("oid_evaluador")){
			Integer oid = aParams.getInteger("oid_evaluador"); 
			if(oid.intValue() < 0){
				aParams.remove("oid_evaluador");
				sesion.remove("oid_evaluador");
			}
			else sesion.putAttribute("oid_evaluador", oid);
		}
		else{
			Integer value = (Integer) sesion.getAttribute("oid_evaluador");
			if(value != null)
				aParams.put("oid_evaluador", value);
		}
	}

	private void borrarClavesInvalidas(MapDS aParams, String aKey) throws ExceptionDS{
		if(!aParams.containsKey(aKey)){//SI NO LAS ENCUENTRA LAS SACA DE LA SESION
			String value = (String) sesion.getAttribute(aKey);
			if(value != null)
				aParams.put(aKey, "%" + value.toUpperCase() + "%");

			return;
		}
		
		String valor = aParams.getString(aKey).trim(); 
		if(valor.length() == 0){
			aParams.remove(aKey);
			sesion.remove(aKey);
		}
		else {
			aParams.put(aKey, "%" + valor.toUpperCase() + "%");
			sesion.putAttribute(aKey, valor); //EL VALOR SI LOS %%, SI PONEN leo, QUE NO GUARDE %LEO%
		}
	}
}