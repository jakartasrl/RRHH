package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.da.ObjectNotFoundException;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.LegajoGetter;

public class SaveAvanceObjetivos extends Operation {

	private Legajo legajo;

	public Integer execute(MapDS aParams) throws Exception {
		LegajoGetter getter = new LegajoGetter(sesion);
		legajo = getter.execute(aParams);

		Transaccion tran = new Transaccion(this.getConnection());

		List etapas  = Etapa.getEtapasEvaluadoras(sesion);

		this.cargarResultados((Tabla) aParams.get("Consecucion"), etapas, tran);
		this.cargarValorCumplimientoGlobal(aParams, tran);

		Integer aprobado = aParams.getInteger("aprobado");

		EstadosHandler handler = new EstadosHandler(sesion);
		handler.setLegajoEjer(legajo.getLegajoEjer());
		handler.setCargaActual(EstadosHandler.AVANCEOBJETIVOS);

		boolean finCarga = aprobado.intValue() == 1;

		handler.setFinalizoCarga(finCarga);

		handler.actualizar(tran);
		tran.save();

		Legajo legSesion           = ((UsuarioRRHH) sesion.getLogin().getUsuario()).getLegajo();
		boolean esUsuarioEvaluador = legSesion != null && legSesion.getLegajoEjer().isEvaluador();
		boolean finalizacion       = ((UsuarioRRHH) sesion.getLogin().getUsuario()).tienePermisosFinalizacion();

		//EL EVALUADOR DEBE PASAR A LAS COMPETENCIAS
		if(!legajo.esMismoLegajoSession() && finCarga && esUsuarioEvaluador && !finalizacion)
			return new Integer(1);

		return new Integer(0);
	}

	private void cargarResultados(Tabla aTabla, List etapas, Transaccion tran) throws ExceptionDS{
		if(aTabla == null) return;

		Iterator it = aTabla.getRegitros().iterator();
		while(it.hasNext()){
			Registro next = (Registro) it.next();

			Objetivo obj = (Objetivo) next.getObject(sesion, "oid_objetivo", Objetivo.class);
			String str = next.getString("cump_real");
			if(str != null && str.length() > 3700)
				str = str.substring(0, 3700);

			obj.setCumplimientoReal(str);

			tran.addObject(obj);

			Iterator it2 = etapas.iterator();
			while(it2.hasNext()){
				Etapa etapa  = (Etapa) it2.next();

				Cumplimiento cumpl = (Cumplimiento) next.getObject(sesion, "oid_cumpl_" + etapa.getOID(), Cumplimiento.class);
				try{
					cumpl.setObjetivo(obj);
					cumpl.setEtapa(etapa);

					str = next.getString("comentario_" + etapa.getOID());
					if(str != null && str.length() > 3700)
						str = str.substring(0, 3700);

					cumpl.setComentario(str);

					String porcentaje = next.getString("porcentaje_" + etapa.getOID());
					if(porcentaje.equalsIgnoreCase("na")){// NO APLICA
						cumpl.setPorcentajeSinValidar(-1);
						cumpl.setResultado(0);
					}
					else{
						cumpl.setPorcentaje(next.getDouble("porcentaje_" + etapa.getOID()).doubleValue());
						cumpl.setResultado(next.getDouble("resultado").doubleValue());
					}
				}
				catch(ExceptionValidacion e){}

				tran.addObject(cumpl);            
			}
		}
	}

	private void cargarValorCumplimientoGlobal(MapDS aParam, Transaccion tran) throws ExceptionDS{
		CumplimientoGlobal cumplimiento = CumplimientoGlobal.getCumplimientoGlobal(legajo.getLegajoEjer(), sesion);
		try{
			String str = aParam.getString("comentario_global");
			if(str != null && str.length() > 3700)
				str = str.substring(0, 3700);

			cumplimiento.setComentario(str);

			IObjectServer server = sesion.getObjectServer(ValCumpGlobal.class);
			ValCumpGlobal valor = (ValCumpGlobal) server.getObjectByOID(aParam.getInteger("valor_global"));
			cumplimiento.setValorCumplimiento(valor);
		}
		catch(ExceptionValidacion e){}
		catch(ObjectNotFoundException e){}

		tran.addObject(cumplimiento);
	}
}