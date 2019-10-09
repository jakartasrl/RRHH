package com.jkt.top150.configuracion.bm; 

import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.util.ExceptionDS;

public class Estructura extends Persistente { 
   
   private Entidad entidad;
   private String codigo;
   private String descripcon;
   private int nivel;
   private boolean activo;
   private Estructura padre;
   
   public Entidad getEntidad() throws ExceptionDS{
      this.supportRefresh();
      return entidad;
   }
   
   public void setEntidad(Entidad aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,entidad, aObj, "Entidad");
      entidad = aObj;
   }
   
   public String getCodigo() throws ExceptionDS{
      this.supportRefresh();
      return codigo;
   }
   
   public void setCodigo(String aObj) throws ExceptionDS{
      this.changePropertyValue(CODIGO_EXISTENTE,codigo, aObj, "Codigo");
      codigo = aObj;
   }
   
   public String getDescripcon() throws ExceptionDS{
      this.supportRefresh();
      return descripcon;
   }
   
   public void setDescripcon(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,descripcon, aObj, "Descripcon");
      descripcon = aObj;
   }
   
   public int getNivel() throws ExceptionDS{
      this.supportRefresh();
      return nivel;
   }
   
   public void setNivel(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,nivel, aObj, "Nivel");
      nivel = aObj;
   }
   
   public boolean getActivo() throws ExceptionDS{
      this.supportRefresh();
      return activo;
   }
   
   public void setActivo(boolean aObj) throws ExceptionDS{
      activo = aObj;
   }
   
   public Estructura getPadre() throws ExceptionDS{
      this.supportRefresh();
      return padre;
   }
   
   public void setPadre(Estructura aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,padre, aObj, "Padre");
      padre = aObj;
   }
   
}