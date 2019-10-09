package com.jkt.top150.capacidades.bl.factories;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidad;
import com.jkt.top150.capacidades.bm.ValorCapacidad;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class FactoryEvalCapacidad extends Factory {
   
   private static final String OID           = "OID_EVAL_CAP";
   private static final String OID_CAPACIDAD = "OID_CAP";
   private static final String OID_VALOR     = "OID_VAL_CAP";
   private static final String OID_ETAPA     = "OID_ETAPA";
   private static final String OID_LEG_EJER  = "OID_LEG_EJE";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      EvalCapacidad persis = (EvalCapacidad) server.getObjectForView(db.getInteger(OID));
      
      IObjectServer sCapacidad = sesion.getObjectServer(Capacidad.class);
      persis.setCapacidad((Capacidad) sCapacidad.getObjectProxy(db.getInteger(OID_CAPACIDAD)));
      
      IObjectServer sValor = sesion.getObjectServer(ValorCapacidad.class);
      persis.setValor((ValorCapacidad) sValor.getObjectProxy(db.getInteger(OID_VALOR)));
      
      IObjectServer sEtapa = sesion.getObjectServer(Etapa.class);
      persis.setEtapa((Etapa) sEtapa.getObjectProxy(db.getInteger(OID_ETAPA)));
      
      IObjectServer sLeg = sesion.getObjectServer(LegajoEjer.class);
      persis.setLegajo((LegajoEjer) sLeg.getObjectProxy(db.getInteger(OID_LEG_EJER)));
      
      this.notificar(persis);
   }
}
