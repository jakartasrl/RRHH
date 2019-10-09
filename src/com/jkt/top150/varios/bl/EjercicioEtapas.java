package com.jkt.top150.varios.bl; 

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class EjercicioEtapas extends Persistente {
   
   public static final String INICIADA = "I";
   public static final String CERRADA  = "C";
   
   private Ejercicio ejercicio;
   private Etapa etapa;
   private String estado;
   private IUsuario usuario;
   
   public Ejercicio getEjercicio() throws ExceptionDS{
      this.supportRefresh();
      return ejercicio;
   }
   
   public void setEjercicio(Ejercicio aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,ejercicio, aObj, "Ejercicio");
      ejercicio = aObj;
   }
   
   public Etapa getEtapa() throws ExceptionDS{
      this.supportRefresh();
      return etapa;
   }
   
   public void setEtapa(Etapa aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,etapa, aObj, "Etapa");
      etapa = aObj;
   }
   
   public String getEstado() throws ExceptionDS{
      this.supportRefresh();
      return estado;
   }
   
   public void setEstado(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,estado, aObj, "Estado");
      estado = aObj;
   }
   
   public boolean isIniciada() throws ExceptionDS{
      return this.getEstado().equalsIgnoreCase(INICIADA);
   }
   
   public boolean isCerrada() throws ExceptionDS{
      return this.getEstado().equalsIgnoreCase(CERRADA);
   }
   
   public Date getFecProceso() throws ExceptionDS{
      return new Date(System.currentTimeMillis());
   }
   
   public IUsuario getUsuario() throws ExceptionDS{
      this.supportRefresh();
      return usuario;
   }
   
   public void setUsuario(IUsuario aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,usuario, aObj, "Usuario");
      usuario = aObj;
   }
   
   public static EjercicioEtapas getEjercicioEtapasActual(ISesion sesion) throws ExceptionDS{
      return EjercicioEtapas.getEjercicioEtapas(sesion, Etapa.getEtapaActual(sesion));
   }
   
   public static EjercicioEtapas getEjercicioEtapas(ISesion sesion, Etapa aEtapa) throws ExceptionDS{
      Ejercicio ejer = ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio();
      
      IObjectServer server = sesion.getObjectServer(EjercicioEtapas.class); 
      
      Map condi = new HashMap();
      condi.put("Etapa", aEtapa);
      condi.put("Ejercicio", ejer);
      
      EjercicioEtapas ee = (EjercicioEtapas) server.getObjectByCodigo(condi);
      if(ee == null)
      	throw new ExceptionValidacion("No se pudo determinar el estado del proceso.Etapa: " + aEtapa.getDescripcion() + ", Ejercicio:" + ejer.getAnio());
      
      return ee;
   }
}