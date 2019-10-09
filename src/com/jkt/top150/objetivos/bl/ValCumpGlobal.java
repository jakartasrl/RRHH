package com.jkt.top150.objetivos.bl; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.util.ExceptionDS;

public class ValCumpGlobal extends Descriptible { 
   
   public static final int SELECT_MAXIMO_VALOR = 20;
   
   private int orden;
   private double valorNumerico;
   private double ponderador;
   private String descExtendida;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public double getValorNumerico() throws ExceptionDS{
      this.supportRefresh();
      return valorNumerico;
   }
   
   public void setValorNumerico(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,valorNumerico, aObj, "Valor Numerico");
      valorNumerico = aObj;
   }
   
   public double getPonderador() throws ExceptionDS{
      this.supportRefresh();
      return ponderador;
   }
   
   public void setPonderador(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,ponderador, aObj, "Ponderador");
      ponderador = aObj;
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