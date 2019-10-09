package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;

public class SaveCuantificadores extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Integer oid = aParams.getInteger("oid_dimen");
      
      IObjectServer server = sesion.getObjectServer(GrupoObjetivo.class);
      GrupoObjetivo dimen  = (GrupoObjetivo) server.getObjectByOID(oid);
      
      Iterator it = ((Tabla) aParams.get("Cuantificadores")).getRegitros().iterator();
      int pos = 1;
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         Cuantificador grupo = (Cuantificador) next.getObject(sesion, "oid", Cuantificador.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, grupo);
            else {
               if(next.getBoolean("activo").booleanValue() && !grupo.isActivo())
                  grupo.setForUpdate();
            }
            
            grupo.setCodigo(next.getString("codigo"));
            grupo.setDescripcion(next.getString("descripcion"));
            grupo.setGrupoObjetivo(dimen);
            grupo.setOrden(pos++);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(grupo);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}