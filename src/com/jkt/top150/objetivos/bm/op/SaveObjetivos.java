package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bl.Cuantificacion;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.LegajoGetter;

public class SaveObjetivos extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      LegajoGetter getter = new LegajoGetter(sesion);
      Legajo legajo = getter.execute(aParams);

      Tabla tabla = (Tabla) aParams.get("Objetivo");
      
      Transaccion tran = new Transaccion(this.getConnection());
      Iterator it = tabla.getRegitros().iterator();
      while(it.hasNext()){
         Registro next = (Registro) it.next();

         Objetivo obj = (Objetivo) next.getObject(sesion, "oid_objetivo", Objetivo.class);
         
         try{
            if(next.containsKey("baja") && next.getBoolean("baja").booleanValue())
               this.tratarBorrado(false, obj);

            obj.setCuantificacion(next.getString("cuantificacion"));
            obj.setDescripcion(next.getString("descripcion"));
            obj.setFuente(next.getString("fuente"));

            obj.setGrupo((GrupoObjetivo) next.getObject(sesion, "oid_grupo", GrupoObjetivo.class));
            
            try{
               obj.setPonderacion(next.getDouble("ponderacion").doubleValue());
            }
            //PORQUE SI EL GRUPO ES RETO LA PONDERACION ES 0
            catch(Exception ignore){}
            
            
            obj.setAspectosCualitativos(next.getString("comentario"));
            obj.setLegajo(legajo.getLegajoEjer());
            
            this.tratarCuantificaciones(obj, next);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(obj);
      }

      Integer aprobado = aParams.getInteger("aprobado");
      
      EstadosHandler handler = new EstadosHandler(sesion);
      handler.setCargaActual(EstadosHandler.CARGAOBJETIVOS);
      handler.setLegajoEjer(legajo.getLegajoEjer());
      
      boolean finalizoCarga = aprobado.intValue() == 1;
      
      handler.setFinalizoCarga(finalizoCarga);
      
      handler.actualizar(tran);
      tran.save();
      
      if(legajo.esMismoLegajoSession() && finalizoCarga){
         writer.addTabla("FinalizoEvaluado");
         return new Integer(1);
      }
         
      
      return new Integer(0);
   }
   
   private void tratarCuantificaciones(Objetivo aObj, Registro reg) throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(Cuantificacion.class);
      
      Iterator it2 = aObj.getGrupo().getCuantificadores().iterator();
      while(it2.hasNext()){
         Cuantificador cuanti = (Cuantificador) it2.next();
         
         Integer oid  = reg.getInteger("pond_oid_"   + cuanti.getOrden());
         String valor = reg.getString("poderacion_" + cuanti.getOrden());
         
         Cuantificacion cuantific = (Cuantificacion) server.getObjectForSave(oid);
         cuantific.setCuantificador(cuanti);
         cuantific.setCuantificacion(valor);
         
         aObj.addCuantificacion(cuantific);
      }
   }
}