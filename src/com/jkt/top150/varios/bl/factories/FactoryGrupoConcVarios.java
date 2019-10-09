package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.varios.bm.GrupoConcVarios;

public class FactoryGrupoConcVarios extends Factory { 
   
   private static final String OID_GRUPO = "OID_GRUPO";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ORDEN = "ORDEN";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      GrupoConcVarios persis = (GrupoConcVarios) server.getObjectForView(db.getInteger(OID_GRUPO));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setOrden(db.getSimpleInt(ORDEN));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}