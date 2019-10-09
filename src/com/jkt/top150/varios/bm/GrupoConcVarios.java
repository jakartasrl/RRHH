package com.jkt.top150.varios.bm; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.util.ExceptionDS;

public class GrupoConcVarios extends Descriptible { 
   
   private int orden;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }   
}