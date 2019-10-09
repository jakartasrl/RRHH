package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.varios.bm.ConcVarios;

public class FactoryConcVarios extends Factory { 
   
   private static final String OID_CONC = "OID_CONC";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ORDEN = "ORDEN";
   private static final String TIPO = "TIPO";
   private static final String REPETITIVO = "REPETITIVO";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      ConcVarios persis = (ConcVarios) server.getObjectForView(db.getInteger(OID_CONC));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setOrden(db.getSimpleInt(ORDEN));
      persis.setTipo(db.getString(TIPO));
      persis.setRepetitivo(db.getSimpleBoolean(REPETITIVO));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}