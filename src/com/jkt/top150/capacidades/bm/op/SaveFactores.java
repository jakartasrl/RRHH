package com.jkt.top150.capacidades.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.Factor;

public class SaveFactores extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Integer oid = aParams.getInteger("oid_capacidad");

      IObjectServer server = sesion.getObjectServer(Capacidad.class);
      Capacidad capa       = (Capacidad) server.getObjectByOID(oid);
      
      Iterator it = ((Tabla) aParams.get("Actores")).getRegitros().iterator();
      int orden   = 1;
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Factor factor = (Factor) next.getObject(sesion, "oid", Factor.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, factor);
            else {
               if(next.getBoolean("activo").booleanValue() && !factor.isActivo())
                  factor.setForUpdate();
            }
            
            factor.setCodigo(next.getString("codigo"));
            factor.setDescripcion(next.getString("descripcion"));
            factor.setOrden(orden++);
            factor.setCapacidad(capa);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(factor);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}