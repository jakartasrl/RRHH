package com.jkt.top150.capacidades.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.Factor;

public class FactoryFactor extends Factory { 
   
   private static final String OID_FAC = "OID_FAC";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String OID_CAP = "OID_CAP";
   private static final String ORDEN = "ORDEN";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Factor persis = (Factor) server.getObjectForView(db.getInteger(OID_FAC));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      Integer oidCapacidad= db.getInteger(OID_CAP);
      if(oidCapacidad.intValue() != 0){
         IObjectServer CapacidadServer = sesion.getObjectServer(Capacidad.class);
         Capacidad capacidad = (Capacidad) CapacidadServer.getObjectProxy(oidCapacidad);
         persis.setCapacidad(capacidad);
      }
      
      persis.setOrden(db.getSimpleInt(ORDEN));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}