package com.jkt.top150.configuracion.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.configuracion.bm.Entidad;

public class FactoryEntidad extends Factory { 
   
   private static final String OID_ENT = "OID_ENT";
   private static final String CODIGO = "CODIGO";
   private static final String RAZON = "RAZON";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Entidad persis = (Entidad) server.getObjectForView(db.getInteger(OID_ENT));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(RAZON));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}