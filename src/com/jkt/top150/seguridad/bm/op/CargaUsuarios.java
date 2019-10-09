package com.jkt.top150.seguridad.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class CargaUsuarios extends Operation implements IObserver{
   
   public Integer execute(MapDS aParams) throws Exception {
      writer.addTabla("Usuarios");
      
      IObjectServer server = sesion.getObjectServer(UsuarioRRHH.class);
      server.getObjects(IDB.SELECT_ALL, null, this);
      
      return new Integer(0);
   }
   
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      UsuarioRRHH user = (UsuarioRRHH) aObj;
      
      writer.addFila();
      writer.addColumna("oid_usu", user.getOID());
      writer.addColumna("codigo",  user.getCodigo());
      writer.addColumna("nombres", user.getNombres());
      writer.addColumna("apellido",user.getApellido());
      writer.addColumna("usuario_rrhh",user.isRRHH());
      writer.addColumna("planeamiento",user.isPlanificacion());
      writer.addColumna("configurador",user.isConfigurador());
      writer.addColumna("activo",  user.isActivo());
   }
}