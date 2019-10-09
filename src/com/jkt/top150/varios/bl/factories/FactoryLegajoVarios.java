package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.LegajoVarios;
import com.jkt.top150.varios.bl.ValConcVarios;
import com.jkt.top150.varios.bm.ConcVarios;

public class FactoryLegajoVarios extends Factory { 
   
   private static final String OID_LEG_POT = "OID_LEG_POT";
   private static final String OID_LEG_EJE = "OID_LEG_EJE";
   private static final String OID_CONC = "OID_CONC";
   private static final String OID_VAL_CONC = "OID_VAL_CONC";
   private static final String SECU = "SECU";
   private static final String TEXTO = "TEXTO";
   private static final String MONTO = "MONTO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      LegajoVarios persis = (LegajoVarios) server.getObjectForView(db.getInteger(OID_LEG_POT));
      
      Integer oidlegajoEjer= db.getInteger(OID_LEG_EJE);
      if(oidlegajoEjer.intValue() != 0){
         IObjectServer legajoEjerServer = sesion.getObjectServer(LegajoEjer.class);
         LegajoEjer legajoEjer = (LegajoEjer) legajoEjerServer.getObjectProxy(oidlegajoEjer);
         persis.setLegajoEjer(legajoEjer);
      }
      
      Integer oidconcVarios= db.getInteger(OID_CONC);
      if(oidconcVarios.intValue() != 0){
         IObjectServer concVariosServer = sesion.getObjectServer(ConcVarios.class);
         ConcVarios concVarios = (ConcVarios) concVariosServer.getObjectProxy(oidconcVarios);
         persis.setConcVarios(concVarios);
      }
      
      Integer oidvalConcVarios= db.getInteger(OID_VAL_CONC);
      if(oidvalConcVarios.intValue() != 0){
         IObjectServer valConcVariosServer = sesion.getObjectServer(ValConcVarios.class);
         ValConcVarios valConcVarios = (ValConcVarios) valConcVariosServer.getObjectProxy(oidvalConcVarios);
         persis.setValConcVarios(valConcVarios);
      }
      
      persis.setSecuencia(db.getSimpleInt(SECU));
      persis.setTexto(db.getString(TEXTO));
      persis.setMonto(db.getSimpleInt(MONTO));
      this.notificar(persis);
   }
}