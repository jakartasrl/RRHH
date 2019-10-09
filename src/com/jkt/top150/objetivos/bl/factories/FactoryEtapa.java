package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bm.Etapa;

public class FactoryEtapa extends Factory { 
   
   private static final String OID_ETAPA = "OID_ETAPA";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String PERIODO = "PERIODO";
   private static final String CARGA_OBJ = "CARGA_OBJ";
   private static final String CALCULA_RES = "CALCULA_RES";
   private static final String EVALUA_CAPA = "EVALUA_CAPA";
   private static final String CARGA_CUMPL = "CARGA_CUMPL";
   private static final String CARGA_RESUMEN = "CARGA_RESUMEN";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Etapa persis = (Etapa) server.getObjectForView(db.getInteger(OID_ETAPA));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setPeriodo(db.getString(PERIODO));
      
      persis.setCargaObjetivo(db.getSimpleBoolean(CARGA_OBJ));
      persis.setCargaCumplimiento(db.getSimpleBoolean(CARGA_CUMPL));
      persis.setEvaluaCapacidades(db.getSimpleBoolean(EVALUA_CAPA));
      persis.setCargaResumen(db.getSimpleBoolean(CARGA_RESUMEN));
      
      persis.setCalculaResultado(db.getSimpleBoolean(CALCULA_RES));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }  
}