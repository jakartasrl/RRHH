package com.jkt.top150.capacidades.bm.op;

import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidadGlobal;
import com.jkt.top150.capacidades.bm.EvalFactor;
import com.jkt.top150.capacidades.bm.Factor;
import com.jkt.top150.capacidades.bm.ValorCapacidad;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.HeaderWriter;
import com.jkt.top150.varios.bl.LegajoGetter;

public class CargarEvaluaciones extends Operation implements IObserver{
   
   private String  codValDef;
   
   private XMLTableMaker factorMaker = null; 
   private XMLTableMaker vCapMaker  = new XMLTableMaker("ValoresCapacidad", this);
   
   private Etapa etapa;
   private Legajo legajo;
   private EvalCapacidadGlobal evalGlobal;
   
   public Integer execute(MapDS aParams) throws Exception {
      LegajoGetter getter = new LegajoGetter(sesion);
      legajo = getter.execute(aParams);
      
      UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario(); 
      if(user.isPlanificacion() && user.getLegajo().getOID() != legajo.getOID())
         throw new ExceptionValidacion("Planeamiento no puede acceder a las competencias ni al resumen de ningun evaluado");

      etapa  = Etapa.getEtapaActual(sesion);
      
      this.obtenerEvaluacionCapacidadGlobal(aParams);
      
      IObjectServer server = sesion.getObjectServer(ValorCapacidad.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
      
      writer.addTabla("Capacidades");
      server = sesion.getObjectServer(Capacidad.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
      
      this.writeHeader();
      
      return new Integer(0);
   }
   
   protected void writeHeader() throws ExceptionDS{
      HeaderWriter hw = new HeaderWriter(sesion);
      hw.write(writer, legajo, EstadosHandler.CARGAEVALUACIONES);
   }
   
   private void obtenerEvaluacionCapacidadGlobal(MapDS aParams) throws ExceptionDS{
      Map condi = new HashMap();
      condi.put("Etapa", etapa);
      condi.put("Legajo", legajo.getLegajoEjer());
      condi.put("Ejercicio", ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio());
      
      IObjectServer server     = sesion.getObjectServer(EvalCapacidadGlobal.class);
      evalGlobal = (EvalCapacidadGlobal) server.getObjectByCodigo(condi);
   }
   
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      if(aObj instanceof Capacidad){
         this.tratarCapacidad((Capacidad) aObj);
         return;
      }
      
      if(aObj instanceof Factor){
         this.tratarFactor((Factor) aObj);
         return;
      }
      
      if(aObj instanceof ValorCapacidad){
         this.tratarValorCapacidad((ValorCapacidad) aObj);
         return;
      }
   }
   
   private void tratarCapacidad(Capacidad capa) throws ExceptionDS{
      writer.addFila();
      writer.addColumna("oid_capa",    capa.getOID());
      writer.addColumna("codigo",      capa.getCodigo());
      writer.addColumna("descripcion", capa.getDescripcion());
      
      Map condi = new HashMap();
      condi.put("Etapa", etapa);
      condi.put("Legajo", legajo.getLegajoEjer());
      condi.put("Capacidad", capa);
      
      IObjectServer evalCap = sesion.getObjectServer(EvalCapacidad.class);
      
      EvalCapacidad evalC = (EvalCapacidad) evalCap.getObjectByCodigo(condi);
      if(evalC != null)
           writer.addColumna("cod_val_cap", evalC.getValor().getCodigo());
      else writer.addColumna("cod_val_cap", codValDef);
      
      factorMaker = new XMLTableMaker("Factores_" + capa.getOID(),this);
      
      capa.getFactores(this);
   }
   
   private void tratarFactor(Factor factor) throws ExceptionDS{
      factorMaker.addFila();
      factorMaker.addColumna("oid_factor",  factor.getOID());
      factorMaker.addColumna("codigo",      factor.getCodigo());
      factorMaker.addColumna("descripcion", factor.getDescripcion());
      
      Map condi = new HashMap();
      condi.put("Etapa", etapa);
      condi.put("Legajo", legajo.getLegajoEjer());
      condi.put("Factor", factor);
      
      IObjectServer evalFac = sesion.getObjectServer(EvalFactor.class);
      EvalFactor evalF = (EvalFactor) evalFac.getObjectByCodigo(condi);
      if(evalF != null)
           factorMaker.addColumna("oid_val_fac", evalF.getValor().getOID());
      else {
      	condi.put("Etapa", etapa);
      	
      	evalF = (EvalFactor) evalFac.getObjectByCodigo(condi);
         if(evalF != null)
              factorMaker.addColumna("oid_val_fac", evalF.getValor().getOID());
         else factorMaker.addColumna("oid_val_fac", 0);
      }
      
   }
   
   private void tratarValorCapacidad(ValorCapacidad valor) throws ExceptionDS{
      vCapMaker.addFila();
      vCapMaker.addColumna("oid_valor",   valor.getOID());
      vCapMaker.addColumna("codigo",      valor.getCodigo());
      vCapMaker.addColumna("descripcion", valor.getDescripcion());
      vCapMaker.addColumna("desc_ext",    valor.getDescExtendida());
      vCapMaker.addColumna("valoracion_global", valor.isValoracionGlobal());
      
      if(evalGlobal != null)
           vCapMaker.addColumna("oid_valor_global", evalGlobal.getValor().getOID());
      else vCapMaker.addColumna("oid_valor_global", 0);
      
      if(codValDef == null)
         codValDef = valor.getCodigo();
   }
}