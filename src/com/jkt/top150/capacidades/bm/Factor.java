package com.jkt.top150.capacidades.bm; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.util.ExceptionDS;

public class Factor extends Descriptible { 
   
   private Capacidad capacidad;
   private int orden;
   
   public Capacidad getCapacidad() throws ExceptionDS{
      this.supportRefresh();
      return capacidad;
   }
   
   public void setCapacidad(Capacidad aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,capacidad, aObj, "Capacidad");
      capacidad = aObj;
   }
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }   
}