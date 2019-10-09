package com.jkt.top150.objetivos.bl.factories;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bl.LegajoEjerEtapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.EjercicioEtapas;

public class FactoryLegajoEjerEtapa extends Factory{
   
   private static final String OID_LEG_EJE_ETAP = "OID_LEG_EJE_ETAP";
   
   private static final String EST_EVALUADO_C_OBJ = "EST_EVALUADO_C_OBJ";
   private static final String EST_EVALUADOR_C_OBJ = "EST_EVALUADOR_C_OBJ";
   private static final String EST_PLANEAMIENTO_C_OBJ = "EST_PLANEAMIENTO_C_OBJ";
   private static final String EST_EVALUADO_CUMPL = "EST_EVALUADO_CUMPL";
   private static final String EST_EVALUADOR_CUMPL = "EST_EVALUADOR_CUMPL";
   private static final String EST_PLANEAMIENTO_CUMPL = "EST_PLANEAMIENTO_CUMPL";
   private static final String EST_EVALUADO_CAPAC = "EST_EVALUADO_CAPAC";
   private static final String EST_EVALUADOR_CAPAC = "EST_EVALUADOR_CAPAC";
   private static final String EST_PLANEAMIENTO_CAPAC = "EST_PLANEAMIENTO_CAPAC";
   
   private static final String OID_LEG_EJE = "OID_LEG_EJE";
   private static final String OID_EJE_ETA = "OID_EJE_ETAPA";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      LegajoEjerEtapa persis = (LegajoEjerEtapa) server.getObjectForView(db.getInteger(OID_LEG_EJE_ETAP));
      
      persis.setEstadoEvaluadoCargaObj(db.getSimpleInt(EST_EVALUADO_C_OBJ));
      persis.setEstadoEvaluadorCargaObj(db.getSimpleInt(EST_EVALUADOR_C_OBJ));
      persis.setEstadoPlaneamientoCargaObj(db.getSimpleInt(EST_PLANEAMIENTO_C_OBJ));
      
      persis.setEstadoEvaluadoCapacidades(db.getSimpleInt(EST_EVALUADO_CAPAC));
      persis.setEstadoEvaluadorCapacidades(db.getSimpleInt(EST_EVALUADOR_CAPAC));
      persis.setEstadoPlaneamientoCapacidades(db.getSimpleInt(EST_PLANEAMIENTO_CAPAC));
      
      persis.setEstadoEvaluadoCumplimientos(db.getSimpleInt(EST_EVALUADO_CUMPL));
      persis.setEstadoEvaluadorCumplimientos(db.getSimpleInt(EST_EVALUADOR_CUMPL));
      persis.setEstadoPlaneamientoCumplimientos(db.getSimpleInt(EST_PLANEAMIENTO_CUMPL));
      
      IObjectServer sLeg = sesion.getObjectServer(LegajoEjer.class);
      persis.setLegajoEjer((LegajoEjer) sLeg.getObjectByOID(db.getInteger(OID_LEG_EJE)));
      
      IObjectServer sEjerEta = sesion.getObjectServer(EjercicioEtapas.class);
      persis.setEjerEtapa((EjercicioEtapas) sEjerEta.getObjectByOID(db.getInteger(OID_EJE_ETA)));
      
      this.notificar(persis);
   }
}