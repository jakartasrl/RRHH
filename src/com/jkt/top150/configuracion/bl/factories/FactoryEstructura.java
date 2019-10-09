package com.jkt.top150.configuracion.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.configuracion.bm.Entidad;
import com.jkt.top150.configuracion.bm.Estructura;

public class FactoryEstructura extends Factory { 
   
   private static final String OID_EST = "OID_EST";
   private static final String OID_ENT = "OID_ENT";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String NIVEL = "NIVEL";
   private static final String ACTIVO = "ACTIVO";
   private static final String OID_EST_PADRE = "OID_EST_PADRE";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Estructura persis = (Estructura) server.getObjectForView(db.getInteger(OID_EST));
      
      Integer oidentidad= db.getInteger(OID_ENT);
      if(oidentidad.intValue() != 0){
         IObjectServer entidadServer = sesion.getObjectServer(Entidad.class);
         Entidad entidad = (Entidad) entidadServer.getObjectProxy(oidentidad);
         persis.setEntidad(entidad);
      }
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcon(db.getString(DESCRIPCION));
      persis.setNivel(db.getSimpleInt(NIVEL));
      persis.setActivo(db.getSimpleBoolean(ACTIVO));
      
      Integer oidpadre= db.getInteger(OID_EST_PADRE);
      if(oidpadre.intValue() != 0){
         IObjectServer padreServer = sesion.getObjectServer(Estructura.class);
         Estructura padre = (Estructura) padreServer.getObjectProxy(oidpadre);
         persis.setPadre(padre);
      }
      
      this.notificar(persis);
   }
   
}