package com.jkt.top150.varios.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.da.ObjectNotFoundException;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.capacidades.bm.EvalResumenGlobal;
import com.jkt.top150.capacidades.bm.ValorResumen;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.LegajoGetter;
import com.jkt.top150.varios.bl.LegajoVarios;
import com.jkt.top150.varios.bm.ConcVarios;

public class SaveLegajoVarios extends Operation {
   private Legajo legajo;
   
   public Integer execute(MapDS aParams) throws Exception {
      LegajoGetter getter = new LegajoGetter(sesion);
      legajo = getter.execute(aParams);
      
      Integer aprobado = aParams.getInteger("aprobado");
      boolean finalizoCarga = aprobado.intValue() == 1;
      
      Transaccion tran = new Transaccion(this.getConnection());
      
      Tabla tabla = (Tabla) aParams.get("Concepto");

      this.saveComentarios(tabla, tran);
      this.saveValorResumen(tran, aParams);
      
      EstadosHandler handler = new EstadosHandler(sesion);
      handler.setCargaActual(EstadosHandler.VERRESUMENGRAFICO);
      handler.setLegajoEjer(legajo.getLegajoEjer());
      
      handler.setFinalizoCarga(finalizoCarga);
      
      handler.actualizar(tran);
      tran.save();
      
      return new Integer(0);
   }
   
   private void saveValorResumen(Transaccion tran, MapDS aParams) throws ExceptionDS{
   	EvalResumenGlobal eval = EvalResumenGlobal.getResumenGlobal(legajo.getLegajoEjer());

      try{
         IObjectServer server = sesion.getObjectServer(ValorResumen.class);
         ValorResumen valor = (ValorResumen) server.getObjectByOID(aParams.getInteger("valor_res_global"));
         eval.setValor(valor);
      }
      catch(ExceptionValidacion e){}
      catch(ObjectNotFoundException e){}
      
      tran.addObject(eval);
   }
      
   private void saveComentarios(Tabla aTabla, Transaccion tran) throws ExceptionDS{
      if(aTabla == null)
         return;
      
      Iterator it= aTabla.getRegitros().iterator();
      while(it.hasNext()){
         Registro reg = (Registro) it.next();
         
         ConcVarios conc = (ConcVarios) reg.getObject(sesion, "oid_conc", ConcVarios.class); 
         
         for(int i = 1; ;i++){
            try{
               String comen     = reg.getString(i + "_comentario");
               
               if(comen.trim().length() == 0)
                  throw new ExceptionValidacion("");
               
               LegajoVarios varios = (LegajoVarios) reg.getObject(sesion, i + "_oid_leg_varios", LegajoVarios.class);
               varios.setConcVarios(conc);
               varios.setLegajoEjer(legajo.getLegajoEjer());
               varios.setSecuencia(i);
               varios.setTexto(comen);
               
               tran.addObject(varios);
            }
            catch(ExceptionValidacion e){
               break;
            }
         }
      }
   }
}