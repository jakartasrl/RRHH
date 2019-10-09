package com.jkt.top150.objetivos.bm; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bm.Ejercicio;

public class Etapa extends Descriptible {
	public static final int SELECT_ETAPA_ACTIVA             = 10;
	public static final int SELECT_ETAPA_DICIEMBRE          = 11;
   public static final int SELECT_EVALUADORAS_CUMPLIMIENTO = 20;
   
   private String periodo;
   private boolean cargaObjetivo;     //CARGA DE OBJETIVOS     PANTALLA NRO 1
   private boolean cargaCumplimientos;//CARGA DE CUMPLIMIENTOS PANTALLA NRO 2
   private boolean evaluaCapacidades; //EVALUA CAPACIDADES     PANTALLA NRO 3
   private boolean cargaResumen;      //CARGA RESUMEN          PANTALLA NRO 4
   
   private boolean calculaResultado;
   private Boolean iniciada;
   
   public static List getEtapasEvaluadoras(ISesion aSes) throws ExceptionDS{
      IObjectServer server = aSes.getObjectServer(Etapa.class);
      return (List) server.getObjects(Etapa.SELECT_EVALUADORAS_CUMPLIMIENTO, null, new ListObserver());
   }
   
   public String getPeriodo() throws ExceptionDS{
      this.supportRefresh();
      return periodo;
   }
   
   public void setPeriodo(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,periodo, aObj, "Periodo");
      periodo = aObj;
   }
   
   public boolean isCargaObjetivo() throws ExceptionDS{
      this.supportRefresh();
      return cargaObjetivo;
   }
   
   public void setCargaObjetivo(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, cargaObjetivo, aObj, null);
      cargaObjetivo = aObj;
   }
   
   public boolean isCalculaResultado() throws ExceptionDS{
      this.supportRefresh();
      return calculaResultado;
   }
   
   public void setCalculaResultado(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, calculaResultado, aObj, null);
      calculaResultado = aObj;
   }
   
   public boolean isEvaluaCapacidades() throws ExceptionDS{
      this.supportRefresh();
      return evaluaCapacidades;
   }
   
   public void setEvaluaCapacidades(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, evaluaCapacidades, aObj, null);
      evaluaCapacidades = aObj;
   }
   
   public boolean isCargaCumplimiento() throws ExceptionDS{
      this.supportRefresh();
      return cargaCumplimientos;
   }
   
   public void setCargaCumplimiento(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, cargaCumplimientos, aObj, null);
      cargaCumplimientos = aObj;
   }
   
   public boolean isCargaResumen() throws ExceptionDS{
      this.supportRefresh();
      return cargaResumen;
   }
   
   public void setCargaResumen(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, cargaResumen, aObj, null);
      cargaResumen = aObj;
   }
   
   public boolean isIniciada() throws ExceptionDS{
      if(iniciada != null)
         return iniciada.booleanValue();
      
      IObjectServer server = sesion.getObjectServer(EjercicioEtapas.class);
      Map condi = new HashMap();
      condi.put("Etapa", this);
      condi.put("Ejercicio", ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio());
      EjercicioEtapas et = (EjercicioEtapas) server.getObjectByCodigo(condi);
      
      if(et == null)
           iniciada = new Boolean(false);
      else iniciada = new Boolean(et.isIniciada());
      
      return iniciada.booleanValue();
   }
   
   public static Etapa getEtapaActual(ISesion sesion) throws ExceptionDS{
      Ejercicio ejer = ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio();

      IObjectServer server = sesion.getObjectServer(Etapa.class);
      Etapa etapa = (Etapa) server.getObjects(SELECT_ETAPA_ACTIVA, ejer, new ObjectObserver());
      
      if(etapa == null)
         throw new ExceptionValidacion(null, "No se pudo encontrar la etapa activa");

      return etapa;
   }
      
   public static Etapa getEtapaDiciembre(ISesion sesion) throws ExceptionDS{
   	IObjectServer server = sesion.getObjectServer(Etapa.class);
   	Etapa etapa = (Etapa) server.getObjects(SELECT_ETAPA_DICIEMBRE, null, new ObjectObserver());
   	
   	if(etapa == null)
   		throw new ExceptionValidacion("No se pudo determinar la etapa DICIEMBRE.");
   	
   	return etapa;
   }
}