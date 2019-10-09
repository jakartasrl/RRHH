package com.jkt.top150.capacidades.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.capacidades.bm.Capacidad;

public class SaveCapacidades extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("Capacidades")).getRegitros().iterator();
      int orden   = 1;
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Capacidad capa = (Capacidad) next.getObject(sesion, "oid", Capacidad.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, capa);
            else {
               if(next.getBoolean("activo").booleanValue() && !capa.isActivo())
                  capa.setForUpdate();
            }
            
            capa.setCodigo(next.getString("codigo"));
            capa.setDescripcion(next.getString("descripcion"));
            capa.setOrden(orden++);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(capa);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}