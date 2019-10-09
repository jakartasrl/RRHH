package com.jkt.top150.seguridad.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.seguridad.bm.Empresa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class OperLogin extends Operation {
   
   public Integer execute(MapDS parm1) throws Exception {
      LoginRRHH login = new LoginRRHH();
      
      String codigo = null;
      
      try{
         codigo = parm1.getString("j_username");
      }
      catch(Exception ignore){
         
         try{
            codigo = parm1.getString("iniciales");
         }
         catch(Exception ignore2){
            codigo = (String) request.getSession().getAttribute("j_username");
         }
      }

      login.setUsuario(codigo, null, "N", this.sesion);
      sesion.setLogin(login);

      writer.addTabla("Usuario");
      writer.addFila();
      writer.addColumna("oid_usu",    login.getUsuario().getOID());
      writer.addColumna("ApeNom",     login.getUsuario().getApeNom());
      writer.addColumna("iniciales",  login.getUsuario().getID());
      writer.addColumna("sesionID",   sesion.getSesionID());
      
      //TODO REVISAR ESTO
      IObjectServer server = sesion.getObjectServer(Empresa.class);
      Empresa emp = (Empresa) server.getObjects(IDB.SELECT_ALL, null, new ObjectObserver());
      login.setCurrentEmpresa(emp);
      
      if(((UsuarioRRHH)sesion.getLogin().getUsuario()).getLegajo() != null)
         ((UsuarioRRHH)sesion.getLogin().getUsuario()).getLegajo().cambiarLegajoEjer();
      
      return new Integer(0);
   }
}