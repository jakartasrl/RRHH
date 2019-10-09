package com.jkt.top150.varios.bl;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.legajos.bm.Legajo;

public class LegajoGetter {
   
   private ISesion sesion;
   
   public LegajoGetter(ISesion ases){
      sesion = ases;
   }
   
   public Legajo execute(MapDS aParams) throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(Legajo.class);
      Legajo legajo = (Legajo) server.getObjectByOID(aParams.getInteger("oid_leg"));
      legajo.cambiarLegajoEjer();
      
      return legajo;
   }
}