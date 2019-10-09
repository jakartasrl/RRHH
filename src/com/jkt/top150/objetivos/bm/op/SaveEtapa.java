package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bm.Etapa;

public class SaveEtapa extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("Etapa")).getRegitros().iterator();
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Etapa etapa = (Etapa) next.getObject(sesion, "oid", Etapa.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, etapa);
            else {
               if(next.getBoolean("activo").booleanValue() && !etapa.isActivo())
                  etapa.setForUpdate();
            }
            
            etapa.setCodigo(next.getString("codigo"));
            etapa.setDescripcion(next.getString("descripcion"));
            etapa.setPeriodo(next.getString("periodo"));
            
            if(!next.containsKey("carga_obj") || !next.getBoolean("carga_obj").booleanValue())
                 etapa.setCargaObjetivo(false);
            else etapa.setCargaObjetivo(true);
            
            if(!next.containsKey("calcula_res") || !next.getBoolean("calcula_res").booleanValue())
                 etapa.setCalculaResultado(false);
            else etapa.setCalculaResultado(true);

            if(!next.containsKey("evalua_capa") || !next.getBoolean("evalua_capa").booleanValue())
                 etapa.setEvaluaCapacidades(false);
            else etapa.setEvaluaCapacidades(true);
            
            if(!next.containsKey("carga_cumpl") || !next.getBoolean("carga_cumpl").booleanValue())
                 etapa.setCargaCumplimiento(false);
            else etapa.setCargaCumplimiento(true);
            
            if(!next.containsKey("carga_resu") || !next.getBoolean("carga_resu").booleanValue())
                 etapa.setCargaResumen(false);
            else etapa.setCargaResumen(true);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(etapa);
      }
      
      tran.save();
      
      writer.addTabla("JustSaved");//MARCA PARA REFRESCAR EL ENCABEZADO
      
      return new Integer(0);
   }
}