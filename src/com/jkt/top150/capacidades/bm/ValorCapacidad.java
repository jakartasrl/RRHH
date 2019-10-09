package com.jkt.top150.capacidades.bm; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.util.ExceptionDS;

public class ValorCapacidad extends Descriptible {
   
   public static final int SELECT_MAXIMO_VALOR = 20;
   public static final int SELECT_MINIMO_VALOR = 21;
   
   private int orden;
   private boolean valoracionGlobal;
   private double valorNumerico;
   private String descExtendida;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public boolean isValoracionGlobal() throws ExceptionDS{
      this.supportRefresh();
      return valoracionGlobal;
   }
   
   public void setValoracionGlobal(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,valoracionGlobal, aObj, null);
      valoracionGlobal = aObj;
   }

   public double getValorNumerico() throws ExceptionDS{
      this.supportRefresh();
      return valorNumerico;
   }
   
   public void setValorNumerico(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,valorNumerico, aObj, "ValorNumerico");
      valorNumerico = aObj;
   }
   
   public String getDescExtendida() throws ExceptionDS{
      this.supportRefresh();
      return descExtendida;
   }
   
   public void setDescExtendida(String aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,descExtendida, aObj, null);
      descExtendida = aObj;
   }
}