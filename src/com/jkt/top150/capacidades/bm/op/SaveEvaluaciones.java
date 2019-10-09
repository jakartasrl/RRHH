package com.jkt.top150.capacidades.bm.op;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidadGlobal;
import com.jkt.top150.capacidades.bm.EvalFactor;
import com.jkt.top150.capacidades.bm.Factor;
import com.jkt.top150.capacidades.bm.ValorCapacidad;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.LegajoGetter;

public class SaveEvaluaciones extends Operation {
   private Etapa etapa;
   private LegajoEjer legajo;
   
   public Integer execute(MapDS aParams) throws Exception {
      LegajoGetter getter = new LegajoGetter(sesion);
      Legajo leg = getter.execute(aParams);

      legajo = leg.getLegajoEjer();
      
      etapa  = Etapa.getEtapaActual(sesion);
      
      Map condi = new HashMap();
      condi.put("Etapa", etapa);
      condi.put("Legajo", legajo);
      
      Transaccion tran = new Transaccion(this.getConnection());
      
      this.tratarEvalCapacidades(aParams, tran, condi);
      this.tratarEvalFactores(aParams, tran, condi);
      this.tratarCapacidadesGlobales(aParams, tran);
      
      Integer aprobado = aParams.getInteger("aprobado");
      EstadosHandler handler = new EstadosHandler(sesion);
      handler.setLegajoEjer(legajo);
      handler.setCargaActual(EstadosHandler.CARGAEVALUACIONES);

      boolean finalizoCarga = aprobado.intValue() == 1;
      boolean esJunio = !legajo.getEjercicioEtapas().getEjerEtapa().getEtapa().isCargaResumen();
      handler.setFinalizoCarga(finalizoCarga);
      
      handler.actualizar(tran);
      tran.save();
      
      if(legajo.getLegajo().esMismoLegajoSession() || finalizoCarga || esJunio)
         return new Integer(0);
      
      return new Integer(1);
   }
   
   private void tratarEvalFactores(MapDS aParams, Transaccion tran, Map condi) throws ExceptionDS{
      IObjectServer evalFac = sesion.getObjectServer(EvalFactor.class);
      
      Iterator it = ((Tabla) aParams.get("EvalFactor")).getRegitros().iterator();
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Factor factor       = (Factor) next.getObject(sesion, "oid_factor", Factor.class);
         
         try{
            ValorCapacidad valor = (ValorCapacidad) next.getObject(sesion, "oid_valor", ValorCapacidad.class);
            
            condi.put("Factor", factor);
            EvalFactor evalF = (EvalFactor) evalFac.getObjectByCodigo(condi);
            if(evalF == null){
               evalF = (EvalFactor) evalFac.getNewObject();
               evalF.setFactor(factor);
               evalF.setEtapa(etapa);
               evalF.setLegajo(legajo);
               evalF.setUsuario(sesion.getLogin().getUsuario());
            }
            
            evalF.setValor(valor);
            tran.addObject(evalF);
         }
         catch(ExceptionValidacion e){}
         
      }
   }
   
   private void tratarEvalCapacidades(MapDS aParams, Transaccion tran, Map condi) throws ExceptionDS{
      IObjectServer evalCap  = sesion.getObjectServer(EvalCapacidad.class);
      IObjectServer sValor   = sesion.getObjectServer(ValorCapacidad.class); 
      
      Iterator it = ((Tabla) aParams.get("EvalCapacidad")).getRegitros().iterator();
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Capacidad capacidad  = (Capacidad) next.getObject(sesion, "oid_capacidad", Capacidad.class);
         
         try{
            ValorCapacidad valor = (ValorCapacidad) sValor.getObjectByCodigo(next.getString("cod_valor"));
            
            condi.put("Capacidad", capacidad);
            EvalCapacidad evalC = (EvalCapacidad) evalCap.getObjectByCodigo(condi);
            if(evalC == null){
               evalC = (EvalCapacidad) evalCap.getNewObject();
               evalC.setCapacidad(capacidad);
               evalC.setEtapa(etapa);
               evalC.setLegajo(legajo);
               evalC.setUsuario(sesion.getLogin().getUsuario());
            }
            
            evalC.setValor(valor);
            
            tran.addObject(evalC);
         }
         catch(ExceptionValidacion e){}
         
      }
   }
   
   private void tratarCapacidadesGlobales(MapDS aParams, Transaccion tran) throws ExceptionDS{
      try{
         Integer oid = aParams.getInteger("valor_cap_global");
         
         IObjectServer sValor = sesion.getObjectServer(ValorCapacidad.class);
         ValorCapacidad valor = (ValorCapacidad) sValor.getObjectByOID(oid);
         
         EvalCapacidadGlobal eval = EvalCapacidadGlobal.getCapacidadGlobal(legajo, sesion);
         eval.setValor(valor);
         
         tran.addObject(eval);
      }
      catch(ExceptionValidacion e){}
      
   }
}