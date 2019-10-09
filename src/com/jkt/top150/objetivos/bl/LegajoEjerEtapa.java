package com.jkt.top150.objetivos.bl;

import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.EjercicioEtapas;

public class LegajoEjerEtapa extends Persistente {
   
   private int estadoEvaluadoCargaObj;
   private int estadoEvaluadorCargaObj;
   private int estadoPlaneamientoCargaObj;
   
   private int estadoEvaluadoCumplimientos;
   private int estadoEvaluadorCumplimientos;
   private int estadoPlaneamientoCumplimientos;
   
   private int estadoEvaluadoCapacidades;
   private int estadoEvaluadorCapacidades;
   private int estadoPlaneamientoCapacidades;
   
   private LegajoEjer legajoEjer;
   private EjercicioEtapas ejerEtapa;
   
   public int getEstadoEvaluadoCapacidades() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadoCapacidades;
   }
   
   public void setEstadoEvaluadoCapacidades(int estadoEvaluadoCapacidades) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadoCapacidades, estadoEvaluadoCapacidades, null);
      this.estadoEvaluadoCapacidades = estadoEvaluadoCapacidades;
   }
   
   public int getEstadoEvaluadoCargaObj() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadoCargaObj;
   }
   
   public void setEstadoEvaluadoCargaObj(int estadoEvaluadoCargaObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadoCargaObj, estadoEvaluadoCargaObj, null);
      this.estadoEvaluadoCargaObj = estadoEvaluadoCargaObj;
   }
   
   public int getEstadoEvaluadoCumplimientos() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadoCumplimientos;
   }
   
   public void setEstadoEvaluadoCumplimientos(int aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadoCumplimientos, aObj, null);
      this.estadoEvaluadoCumplimientos = aObj;
   }
   
   public int getEstadoEvaluadorCapacidades() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadorCapacidades;
   }
   
   public void setEstadoEvaluadorCapacidades(int estadoEvaluadorCapacidades) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadorCapacidades, estadoEvaluadorCapacidades, null);
      this.estadoEvaluadorCapacidades = estadoEvaluadorCapacidades;
   }
   
   public int getEstadoEvaluadorCargaObj() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadorCargaObj;
   }
   
   public void setEstadoEvaluadorCargaObj(int estadoEvaluadorCargaObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadorCargaObj, estadoEvaluadorCargaObj, null);
      this.estadoEvaluadorCargaObj = estadoEvaluadorCargaObj;
   }
   
   public int getEstadoEvaluadorCumplimientos() throws ExceptionDS{
      this.supportRefresh();
      return estadoEvaluadorCumplimientos;
   }
   
   public void setEstadoEvaluadorCumplimientos(int aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoEvaluadorCumplimientos, aObj, null);
      this.estadoEvaluadorCumplimientos = aObj;
   }
   
   public int getEstadoPlaneamientoCapacidades() throws ExceptionDS{
      this.supportRefresh();
      return estadoPlaneamientoCapacidades;
   }
   
   public void setEstadoPlaneamientoCapacidades(int aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoPlaneamientoCapacidades, aObj, null);
      this.estadoPlaneamientoCapacidades = aObj;
   }
   
   public int getEstadoPlaneamientoCargaObj() throws ExceptionDS{
      this.supportRefresh();
      return estadoPlaneamientoCargaObj;
   }
   
   public void setEstadoPlaneamientoCargaObj(int aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoPlaneamientoCargaObj, aObj, null);
      this.estadoPlaneamientoCargaObj = aObj;
   }
   
   public int getEstadoPlaneamientoCumplimientos() throws ExceptionDS{
      this.supportRefresh();
      return estadoPlaneamientoCumplimientos;
   }
   
   public void setEstadoPlaneamientoCumplimientos(int aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, this.estadoPlaneamientoCumplimientos, aObj, null);
      this.estadoPlaneamientoCumplimientos = aObj;
   }
   
   public EjercicioEtapas getEjerEtapa() {
      return ejerEtapa;
   }
   
   public void setEjerEtapa(EjercicioEtapas ejerEtapa) {
      this.ejerEtapa = ejerEtapa;
   }
   
   public LegajoEjer getLegajoEjer() {
      return legajoEjer;
   }
   
   public void setLegajoEjer(LegajoEjer legajoEjer) {
      this.legajoEjer = legajoEjer;
   }
}