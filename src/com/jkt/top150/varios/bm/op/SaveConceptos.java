package com.jkt.top150.varios.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.varios.bm.ConcVarios;

public class SaveConceptos extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("Conceptos")).getRegitros().iterator();
      int orden   = 1; 
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         ConcVarios conc = (ConcVarios) next.getObject(sesion, "oid", ConcVarios.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, conc);
            else {
               if(next.getBoolean("activo").booleanValue() && !conc.isActivo())
                  conc.setForUpdate();
            }
            
            conc.setCodigo(next.getString("codigo"));
            conc.setDescripcion(next.getString("descripcion"));
            conc.setTipo(next.getString("tipo"));

            conc.setOrden(orden ++);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(conc);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}