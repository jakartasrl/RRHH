package com.jkt.top150.seguridad.bm.op;

import com.jkt.framework.Aplicacion;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.seguridad.bm.Login;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LoginRRHH extends Login {
   private Usuario usu;

   public LoginRRHH() {
      super();
   }
   
   public void setUsuario(String aCodigo, String aPasswd, String aAutoLogin, ISesion aSesion) throws Exception{
      IObjectServer sUsuRRHH = aSesion.getObjectServer(UsuarioRRHH.class);
      usu = (Usuario) sUsuRRHH.getObjects(UsuarioRRHH.SELECT_BY_CODIGO_UPPER, aCodigo, new ObjectObserver()); 

// Dario 30-11-2005 xq Oracle es case sensitive.
//      IObjectServer usuServer = aSesion.getObjectServer(Usuario.NICKNAME);
//      usu   = (Usuario) usuServer.getObjectByCodigo(aCodigo);
      
      boolean ldap = Aplicacion.getAplicacion().getLdapConfiguration().isLdap();
      try{       
         if (ldap==true) {
            // usuario en el grupo "Puma" de LDAP. 
            boolean enGrupoLdap = Aplicacion.getAplicacion().checkUserLdapInGroup(aCodigo);
            if(enGrupoLdap==false)
               throw new ExceptionValidacion(null, "Usuario Inexistente (LDAP)");
            
            if(aAutoLogin.equalsIgnoreCase("N")==true) { // autologin No.        
               String passwdCtrlLdap = Aplicacion.getAplicacion().decode(aPasswd); // passwd decod.
               boolean passOk = Aplicacion.getAplicacion().checkUserLdap(aCodigo, passwdCtrlLdap);
               if(passOk==false)
                  throw new ExceptionValidacion(null, "Password incorrecta (LDAP)");
            }  
         }
         
         if (usu == null)
            throw new ExceptionValidacion(null, "Usuario Inexistente (Aplicacion)");
         
         /**
          if (ldap==false && !usu.isPassOK(aPasswd))
          throw new ExceptionValidacion(null, "Password incorrecta");
          */
         
         if (!usu.isActivo())
            throw new ExceptionValidacion(null, "El usuario: '" + usu.getApeNom() + "' esta inactivo.");
      }
      catch(Exception e){
         //ESTO ES PORQUE LA APLICACION CREA UNA SESION NUEVA
         //Y NO LA LIBERA
         Long sesionID = aSesion.getSesionID();
         Aplicacion.getAplicacion().freeSesion(sesionID);
         throw e;
      }
   }  
   
   public IUsuario getUsuario(){
      return usu;
   }
}