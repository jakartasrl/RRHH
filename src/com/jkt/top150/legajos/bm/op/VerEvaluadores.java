package com.jkt.top150.legajos.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class VerEvaluadores extends Operation implements IObserver{
   
   public Integer execute(MapDS aParams) throws Exception {
      Legajo leg = ((UsuarioRRHH) sesion.getLogin().getUsuario()).getLegajo();
      if(leg == null || leg.getLegajoEjer() == null)
         return new Integer(0);
      
      writer.addTabla("Evaluadores");
      
      IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
      server.getObjects(LegajoEjer.SELECT_EVALUADORES, leg.getLegajoEjer(), this);
      
      return new Integer(0);
   }
   
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      LegajoEjer legE = (LegajoEjer) aObj;
      writer.addFila();
      
      writer.addColumna("oid_leg_ejer", legE.getOID());
      writer.addColumna("legajo",       legE.getLegajo().getLegajo());
      writer.addColumna("nombres",      legE.getLegajo().getNombres());
      writer.addColumna("apellido",     legE.getLegajo().getApellidoPaterno());
   }
}
