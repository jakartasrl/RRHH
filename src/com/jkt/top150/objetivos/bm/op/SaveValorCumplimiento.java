package com.jkt.top150.objetivos.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;

public class SaveValorCumplimiento extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("ValCumpGlobal")).getRegitros().iterator();
      int pos = 1;
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         ValCumpGlobal valor = (ValCumpGlobal) next.getObject(sesion, "oid", ValCumpGlobal.class);
         try{
            if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
               this.tratarBorrado(false, valor);
            else {
               if(next.getBoolean("activo").booleanValue() && !valor.isActivo())
                  valor.setForUpdate();
            }
            
            valor.setCodigo(next.getString("codigo"));
            valor.setDescripcion(next.getString("descripcion"));
            valor.setDescExtendida(next.getString("desc_ext"));
            valor.setValorNumerico(next.getDouble("valorNumerico").doubleValue());
            valor.setPonderador(next.getDouble("ponderador").doubleValue());
            
            valor.setOrden(pos++);
         }
         catch(ExceptionDelete e){}
         
         tran.addObject(valor);
      }
      
      tran.save();
      
      return new Integer(0);
   }
}