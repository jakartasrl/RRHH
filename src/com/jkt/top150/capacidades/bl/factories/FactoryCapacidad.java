package com.jkt.top150.capacidades.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.Capacidad;

public class FactoryCapacidad extends Factory { 
   
   private static final String OID_CAP = "OID_CAP";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ORDEN = "ORDEN";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Capacidad persis = (Capacidad) server.getObjectForView(db.getInteger(OID_CAP));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setOrden(db.getSimpleInt(ORDEN));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}