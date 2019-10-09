package com.jkt.top150.varios.bm; 

import java.util.HashMap;
import java.util.Map;

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.varios.bl.LegajoVarios;

public class ConcVarios extends Descriptible { 
   
   private static String EVALUADOR = "R";
   private static String EVALUADO  = "O";
   
   private int orden;
   private String tipo;
   private boolean repetitivo;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public String getTipo() throws ExceptionDS{
      this.supportRefresh();
      return tipo;
   }
   
   public void setTipo(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,tipo, aObj, "Tipo");
      tipo = aObj;
   }
   
   public boolean isTipoEvaluador() throws ExceptionDS{
      return this.getTipo().equals(ConcVarios.EVALUADOR);
   }
   
   public boolean getRepetitivo() throws ExceptionDS{
      this.supportRefresh();
      return repetitivo;
   }
   
   public void setRepetitivo(boolean aObj) throws ExceptionDS{
      repetitivo = aObj;
   }
   
   public void getComentarios(IObserver aObs, Legajo legajo) throws ExceptionDS{
      Map condi = new HashMap();
      condi.put("LegajoEjer", legajo.getLegajoEjer());
      condi.put("ConcVarios", this);
      
      IObjectServer server = sesion.getObjectServer(LegajoVarios.class);
      server.getObjects(IDB.SELECT_ALL, condi, aObs);
   }
}