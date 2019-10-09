package com.jkt.top150.varios.bl; 

import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.varios.bm.ConcVarios;

public class ValConcVarios extends Persistente { 
   
   private ConcVarios concVarios;
   private String codigo;
   private String descripcion;
   private boolean activo;
   
   public ConcVarios getConcVarios() throws ExceptionDS{
      this.supportRefresh();
      return concVarios;
   }
   
   public void setConcVarios(ConcVarios aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,concVarios, aObj, "ConcVarios");
      concVarios = aObj;
   }
   
   public String getCodigo() throws ExceptionDS{
      this.supportRefresh();
      return codigo;
   }
   
   public void setCodigo(String aObj) throws ExceptionDS{
      this.changePropertyValue(CODIGO_EXISTENTE,codigo, aObj, "Codigo");
      codigo = aObj;
   }
   
   public String getDescripcion() throws ExceptionDS{
      this.supportRefresh();
      return descripcion;
   }
   
   public void setDescripcion(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,descripcion, aObj, "Descripcion");
      descripcion = aObj;
   }
   
   public boolean getActivo() throws ExceptionDS{
      this.supportRefresh();
      return activo;
   }
   
   public void setActivo(boolean aObj) throws ExceptionDS{
      activo = aObj;
   }
   
}