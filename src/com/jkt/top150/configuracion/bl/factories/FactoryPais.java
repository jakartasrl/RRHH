package com.jkt.top150.configuracion.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.configuracion.bm.Pais;

public class FactoryPais extends Factory { 
   
   private static final String OID_PAIS = "OID_PAIS";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Pais persis = (Pais) server.getObjectForView(db.getInteger(OID_PAIS));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}