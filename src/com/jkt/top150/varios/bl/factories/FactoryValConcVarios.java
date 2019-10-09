package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.varios.bl.ValConcVarios;
import com.jkt.top150.varios.bm.ConcVarios;

public class FactoryValConcVarios extends Factory { 
   
   private static final String OID_VAL_CONC = "OID_VAL_CONC";
   private static final String OID_CONC = "OID_CONC";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      ValConcVarios persis = (ValConcVarios) server.getObjectForView(db.getInteger(OID_VAL_CONC));
      
      Integer oidconcVarios= db.getInteger(OID_CONC);
      if(oidconcVarios.intValue() != 0){
         IObjectServer concVariosServer = sesion.getObjectServer(ConcVarios.class);
         ConcVarios concVarios = (ConcVarios) concVariosServer.getObjectProxy(oidconcVarios);
         persis.setConcVarios(concVarios);
      }
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setActivo(db.getSimpleBoolean(ACTIVO));
      this.notificar(persis);
   }
   
}