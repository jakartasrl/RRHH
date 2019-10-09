package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.varios.bm.NivelIdioma;

public class FactoryNivelIdioma extends Factory { 
   
   private static final String OID_NIVEL = "OID_NIVEL";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      NivelIdioma persis = (NivelIdioma) server.getObjectForView(db.getInteger(OID_NIVEL));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setActivo(db.getSimpleBoolean(ACTIVO));
      this.notificar(persis);
   }
}