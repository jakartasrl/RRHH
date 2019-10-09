package com.jkt.top150.objetivos.bm; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.util.ExceptionDS;

public class Cuantificador extends Descriptible { 
   
   private GrupoObjetivo grupoObjetivo;
   private int orden;
   
   public GrupoObjetivo getGrupoObjetivo() throws ExceptionDS{
      this.supportRefresh();
      return grupoObjetivo;
   }
   
   public void setGrupoObjetivo(GrupoObjetivo aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,grupoObjetivo, aObj, "GrupoObjetivo");
      grupoObjetivo = aObj;
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