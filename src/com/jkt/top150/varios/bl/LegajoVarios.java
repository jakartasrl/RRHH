package com.jkt.top150.varios.bl; 

import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bm.ConcVarios;

public class LegajoVarios extends Persistente { 
   
   private LegajoEjer legajoEjer;
   private ConcVarios concVarios;
   private ValConcVarios valConcVarios;
   private int secuencia;
   private String texto;
   private double monto;
   
   public LegajoEjer getLegajoEjer() throws ExceptionDS{
      this.supportRefresh();
      return legajoEjer;
   }
   
   public void setLegajoEjer(LegajoEjer aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,legajoEjer, aObj, "LegajoEjer");
      legajoEjer = aObj;
   }
   
   public ConcVarios getConcVarios() throws ExceptionDS{
      this.supportRefresh();
      return concVarios;
   }
   
   public void setConcVarios(ConcVarios aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,concVarios, aObj, "ConcVarios");
      concVarios = aObj;
   }
   
   public ValConcVarios getValConcVarios() throws ExceptionDS{
      this.supportRefresh();
      return valConcVarios;
   }
   
   public void setValConcVarios(ValConcVarios aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,valConcVarios, aObj, "ValConcVarios");
      valConcVarios = aObj;
   }
   
   public int getSecuencia() throws ExceptionDS{
      this.supportRefresh();
      return secuencia;
   }
   
   public void setSecuencia(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,secuencia, aObj, "Secuencia");
      secuencia = aObj;
   }
   
   public String getTexto() throws ExceptionDS{
      this.supportRefresh();
      return texto;
   }
   
   public void setTexto(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,texto, aObj, "Texto");
      texto = aObj;
   }
   
   public double getMonto() throws ExceptionDS{
      this.supportRefresh();
      return monto;
   }
   
   public void setMonto(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,monto, aObj, "Monto");
      monto = aObj;
   }
}