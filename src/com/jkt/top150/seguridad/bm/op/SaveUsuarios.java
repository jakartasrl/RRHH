package com.jkt.top150.seguridad.bm.op;

import java.util.Iterator;

import com.jkt.framework.Aplicacion;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class SaveUsuarios extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      Transaccion tran = new Transaccion(this.getConnection());
      
      Iterator it = ((Tabla) aParams.get("Usuarios")).getRegitros().iterator();
      
      while(it.hasNext()){
         Registro next = (Registro) it.next();
         
         UsuarioRRHH user = (UsuarioRRHH) next.getObject(sesion, "oid_usu", UsuarioRRHH.class);
         
         String codigo = next.getString("codigo");
         user.setCodigo(codigo);
         user.setNombres(next.getString("descripcion"));
         user.setApellido(next.getString("apellido"));
         user.setPassword(Aplicacion.getAplicacion().encode("codigo"));
         
         if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue()){
            if(!user.isNew()){
               user.setForDelete();
               tran.addObject(user);
            }
            continue;
         }
         
         user.setActivo();
         
         if(!next.containsKey("usuario_rrhh") || !next.getBoolean("usuario_rrhh").booleanValue())
              user.setRRHH(false);
         else user.setRRHH(true);
         
         if(!next.containsKey("planeamiento") || !next.getBoolean("planeamiento").booleanValue())
              user.setPlanificacion(false);
         else user.setPlanificacion(true);
         
         if(!next.containsKey("configurador") || !next.getBoolean("configurador").booleanValue())
              user.setConfigurador(false);
         else user.setConfigurador(true);
         
         tran.addObject(user);
      }
      
      tran.save();
      
      UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
      LoginRRHH login = (LoginRRHH) sesion.getLogin();
      login.setUsuario(user.getCodigo(), user.getPassword(), "false", sesion);
      
      return new Integer(0);
   }
}