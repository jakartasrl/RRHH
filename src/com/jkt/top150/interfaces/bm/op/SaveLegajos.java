package com.jkt.top150.interfaces.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.legajos.bm.Legajo;

public class SaveLegajos extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      IObjectServer server = sesion.getObjectServer(Legajo.class);
      
      Iterator it = ((Tabla) aParams.get("Legajo")).getRegitros().iterator();
      
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         if(!next.containsKey("incluido") || !next.getBoolean("incluido").booleanValue())
            continue;
         
         String numero = next.getString("legajo");
         Legajo leg    = (Legajo) server.getObjectByCodigo(numero);
         if(leg == null)
            leg = (Legajo) server.getNewObject();
         
         leg.setLegajo(numero);
         leg.setNombres(next.getString("nombre"));
         leg.setApellidoPaterno(next.getString("apellido"));
         leg.setMail(next.getString("mail"));
         leg.setNacionalidad(next.getString("nacionalidad"));
         leg.setNacimiento(next.getDate("nacimiento"));
         
         tran.addObject(leg);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}