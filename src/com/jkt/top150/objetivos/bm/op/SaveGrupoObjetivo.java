package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;

public class SaveGrupoObjetivo extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("GrupoObjetivo")).getRegitros().iterator();
      int pos = 1;
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         GrupoObjetivo grupo = (GrupoObjetivo) next.getObject(sesion, "oid", GrupoObjetivo.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, grupo);
            else {
               if(next.getBoolean("activo").booleanValue() && !grupo.isActivo())
                  grupo.setForUpdate();
            }
            
            grupo.setCodigo(next.getString("codigo"));
            grupo.setDescripcion(next.getString("descripcion"));
            grupo.setOrden(pos++);
            
            if(!next.containsKey("es_reto") || !next.getBoolean("es_reto").booleanValue())
                 grupo.setEsReto(false);
            else grupo.setEsReto(true);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(grupo);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}