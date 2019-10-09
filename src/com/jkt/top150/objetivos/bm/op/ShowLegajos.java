package com.jkt.top150.objetivos.bm.op;

import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bm.Ejercicio;

public class ShowLegajos extends Operation implements IObserver{
   
   public Integer execute(MapDS aParams) throws Exception {
      IObjectServer sEjer = sesion.getObjectServer(Ejercicio.class);
      Ejercicio ejer = (Ejercicio) sEjer.getObjectByCodigo(aParams.getInteger("anio"));
      
      if(ejer == null)
         throw new ExceptionValidacion("Ejercicio inexistente: " + aParams.getInteger("anio"));
      
      writer.addTabla("mtLegajos");
      
      Map cond = new HashMap();
      cond.put("Ejercicio", ejer);
      
      IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
      server.getObjects(LegajoEjer.SELECT_EVALUADOS_POR_ANIO, ejer, this);
      
      return new Integer(0);
   }
   
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS {
      LegajoEjer ejer = (LegajoEjer) aObj;
      
      writer.addFila();
      writer.addColumna("oid_legajo", ejer.getOID());
      writer.addColumna("descripcion", ejer.getDescripcion());
   }
}
