package com.jkt.top150.objetivos.bm; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.ListObserver;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class GrupoObjetivo extends Descriptible { 
   
   private int orden;
   private boolean esReto;
   private List cuantificadores;
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public boolean isReto() throws ExceptionDS{
      this.supportRefresh();
      return esReto;
   }
   
   public void setEsReto(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, esReto, aObj, null);
      esReto = aObj;
   }
   
   public void getObjetivos(IObserver aObs, Legajo aLeg) throws ExceptionDS{
      Map condi = new HashMap();
      condi.put("Legajo", aLeg);
      condi.put("Grupo",  this);
      condi.put("Ejercicio",  ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio());
      
      IObjectServer server = sesion.getObjectServer(Objetivo.class);
      server.getObjects(IDB.SELECT_ALL, condi, aObs);
   }
   
   public List getCuantificadores() throws ExceptionDS{
      if(cuantificadores == null){
         IObjectServer server = sesion.getObjectServer(Cuantificador.class);
         Map condi = new HashMap();
         condi.put("oid_dimen", this.getOIDInteger());
         
         cuantificadores = (List) server.getObjects(IDB.SELECT_ALL, condi, new ListObserver());
      }
      return cuantificadores;
   }
}