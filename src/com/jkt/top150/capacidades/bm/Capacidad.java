package com.jkt.top150.capacidades.bm; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.ListObserver;

public class Capacidad extends Descriptible { 
   
   private int orden;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public List getFactores() throws ExceptionDS {
      IObjectServer server = sesion.getObjectServer(Factor.class);
      Map condi = new HashMap();
      condi.put("oid_capacidad", this.getOIDInteger());
      
      return (List) server.getObjects(IDB.SELECT_ACTIVOS, condi, new ListObserver());
   }
   
   public void getFactores(IObserver aObs) throws ExceptionDS {
      IObjectServer server = sesion.getObjectServer(Factor.class);
      Map condi = new HashMap();
      condi.put("oid_capacidad", this.getOIDInteger());
      
      server.getObjects(IDB.SELECT_ALL, condi, aObs);
   }
}