package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.LegajoIdioma;
import com.jkt.top150.varios.bm.NivelIdioma;

public class FactoryLegajoIdioma extends Factory { 
   
   private static final String OID_LEG_IDIO = "OID_LEG_IDIO";
   private static final String OID_LEG_EJE = "OID_LEG_EJE";
   private static final String ORDEN = "ORDEN";
   private static final String IDIOMA = "IDIOMA";
   private static final String OID_NIVEL = "OID_NIVEL";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      LegajoIdioma persis = (LegajoIdioma) server.getObjectForView(db.getInteger(OID_LEG_IDIO));
      
      Integer oidlegajoEjer= db.getInteger(OID_LEG_EJE);
      if(oidlegajoEjer.intValue() != 0){
         IObjectServer legajoEjerServer = sesion.getObjectServer(LegajoEjer.class);
         LegajoEjer legajoEjer = (LegajoEjer) legajoEjerServer.getObjectProxy(oidlegajoEjer);
         persis.setLegajoEjer(legajoEjer);
      }
      
      persis.setOrden(db.getSimpleInt(ORDEN));
      persis.setIdioma(db.getString(IDIOMA));
      Integer oidnivelIdioma= db.getInteger(OID_NIVEL);
      if(oidnivelIdioma.intValue() != 0){
         IObjectServer nivelIdiomaServer = sesion.getObjectServer(NivelIdioma.class);
         NivelIdioma nivelIdioma = (NivelIdioma) nivelIdiomaServer.getObjectProxy(oidnivelIdioma);
         persis.setNivelIdioma(nivelIdioma);
      }
      
      this.notificar(persis);
   }
   
}