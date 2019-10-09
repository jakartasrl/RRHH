package com.jkt.top150.configuracion.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.configuracion.bm.Pais;

public class SavePaises extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("Paises")).getRegitros().iterator();
      
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Pais pais = (Pais) next.getObject(sesion, "oid", Pais.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, pais);
            else {
               if(next.getBoolean("activo").booleanValue() && !pais.isActivo())
                  pais.setForUpdate();
            }

            pais.setCodigo(next.getString("codigo"));
            pais.setDescripcion(next.getString("descripcion"));
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(pais);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}