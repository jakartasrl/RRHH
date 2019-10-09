package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bl.LegajoEjerEtapa;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bl.EstadosHandler;

public class SaveEstadosEvaluados extends Operation {

	public Integer execute(MapDS arg0) throws Exception {
		Transaccion tran = new Transaccion(this.getConnection());

		Etapa etapa = Etapa.getEtapaActual(sesion);

		Iterator it = ((Tabla) arg0.get("Estados")).getRegitros().iterator();

		while(it.hasNext()){
			Registro next = (Registro) it.next();
			
			if(etapa.isCargaObjetivo() && next.getInteger(ShowEstadosEvaluadosDS.OBJETIVOS).intValue() != 0){
				LegajoEjer legEje   = (LegajoEjer) next.getObject(sesion, "oid_leg_ejer", LegajoEjer.class);
				LegajoEjerEtapa lee = legEje.getEjercicioEtapas();

				this.actualizarEstados(lee, next.getInteger(ShowEstadosEvaluadosDS.OBJETIVOS).intValue(), ShowEstadosEvaluadosDS.OBJETIVOS);
				
				tran.addObject(lee);
			}

			if(etapa.isCargaCumplimiento() && next.getInteger(ShowEstadosEvaluadosDS.CUMPLIMIENTOS).intValue() != 0){
				LegajoEjer legEje   = (LegajoEjer) next.getObject(sesion, "oid_leg_ejer", LegajoEjer.class);
				LegajoEjerEtapa lee = legEje.getEjercicioEtapas();
				
				this.actualizarEstados(lee, next.getInteger(ShowEstadosEvaluadosDS.CUMPLIMIENTOS).intValue(), ShowEstadosEvaluadosDS.CUMPLIMIENTOS);
				
				tran.addObject(lee);
			}
			
			if(etapa.isEvaluaCapacidades() && next.getInteger(ShowEstadosEvaluadosDS.CAPACIDADES).intValue() != 0){
				LegajoEjer legEje   = (LegajoEjer) next.getObject(sesion, "oid_leg_ejer", LegajoEjer.class);
				
				EjercicioEtapas ee = EjercicioEtapas.getEjercicioEtapas(sesion, Etapa.getEtapaActual(sesion));
				LegajoEjerEtapa lee = legEje.getEjercicioEtapas(ee);
				
				this.actualizarEstados(lee, next.getInteger(ShowEstadosEvaluadosDS.CAPACIDADES).intValue(), ShowEstadosEvaluadosDS.CAPACIDADES);
				
				tran.addObject(lee);
			}
		}
		
		tran.save();

		return OK;
	}
	
	private void actualizarEstados(LegajoEjerEtapa lee, int aNum, String aEtapa) throws ExceptionDS{
		/*
       <option value="0">------------------</option>
		 <option value="1">ACTIVO</option>
		 <option value="2">FIN EVALUADO</option>
		 <option value="3">FIN EVALUADOR</option>
		 <option value="4">FIN PLANEAMIENTO</option>
		 <option value="5">CERRADO</option>
		 
       public static final int ESTADO_CARGANDO    = 0;
       public static final int ESTADO_FIN_CARGA   = 1;
       public static final int ESTADO_CERRADO     = -1;
		 */
		
		if(aNum == 0)
			return;
		
		if(aNum == 5){
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.OBJETIVOS)){
				lee.setEstadoEvaluadoCargaObj(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoEvaluadorCargaObj(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoPlaneamientoCargaObj(EstadosHandler.ESTADO_CERRADO);
			}

			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CAPACIDADES)){
				lee.setEstadoEvaluadoCapacidades(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoEvaluadorCapacidades(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoPlaneamientoCapacidades(EstadosHandler.ESTADO_CERRADO);
			}

			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CUMPLIMIENTOS)){
				lee.setEstadoEvaluadoCumplimientos(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoEvaluadorCumplimientos(EstadosHandler.ESTADO_CERRADO);
				lee.setEstadoPlaneamientoCumplimientos(EstadosHandler.ESTADO_CERRADO);
			}
			
			return;
		}

		//SE INICIALIZAN TODOS LOS ESTADOS
		if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.OBJETIVOS)){
			lee.setEstadoEvaluadoCargaObj(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoEvaluadorCargaObj(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoPlaneamientoCargaObj(EstadosHandler.ESTADO_CARGANDO);
		}

		if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CAPACIDADES)){
			lee.setEstadoEvaluadoCapacidades(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoEvaluadorCapacidades(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoPlaneamientoCapacidades(EstadosHandler.ESTADO_CARGANDO);
		}

		if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CUMPLIMIENTOS)){
			lee.setEstadoEvaluadoCumplimientos(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoEvaluadorCumplimientos(EstadosHandler.ESTADO_CARGANDO);
			lee.setEstadoPlaneamientoCumplimientos(EstadosHandler.ESTADO_CARGANDO);
		}

		//SI SOO HAY QUE MARCA ACTIVO, YA ESTA
		if(aNum == 1)
   		return;
		
		//FINALIZACION DE EVALUADO
		if(aNum == 2){
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.OBJETIVOS))
				lee.setEstadoEvaluadoCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
			
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CAPACIDADES))
				lee.setEstadoEvaluadoCapacidades(EstadosHandler.ESTADO_FIN_CARGA);

			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CUMPLIMIENTOS))
				lee.setEstadoEvaluadoCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
		}
		
		//FINALIZACION DE EVALUADOR
		if(aNum == 3){
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.OBJETIVOS)){
				lee.setEstadoEvaluadoCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoEvaluadorCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
			}
			
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CAPACIDADES)){
				//lee.setEstadoEvaluadoCapacidades(EstadosHandler.ESTADO_FIN_CARGA);EL EVALUADOR TERMINA ANTES
				lee.setEstadoEvaluadorCapacidades(EstadosHandler.ESTADO_FIN_CARGA);
			}

			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CUMPLIMIENTOS)){
				lee.setEstadoEvaluadoCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoEvaluadorCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
			}
		}

		//FINALIZACION DE PLANEMIAENTO
		if(aNum == 4){
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.OBJETIVOS)){
				lee.setEstadoEvaluadoCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoEvaluadorCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoPlaneamientoCargaObj(EstadosHandler.ESTADO_FIN_CARGA);
			}
			
			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CAPACIDADES)){
				lee.setEstadoEvaluadoCapacidades(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoEvaluadorCapacidades(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoPlaneamientoCapacidades(EstadosHandler.ESTADO_FIN_CARGA);
			}

			if(aEtapa.equalsIgnoreCase(ShowEstadosEvaluadosDS.CUMPLIMIENTOS)){
				lee.setEstadoEvaluadoCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoEvaluadorCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
				lee.setEstadoPlaneamientoCumplimientos(EstadosHandler.ESTADO_FIN_CARGA);
			}
		}
	}
}