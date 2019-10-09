package com.jkt.top150.seguridad.bm.op;

import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.LegajoGetter;

public class VerEncabezado extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      UsuarioRRHH usuario = (UsuarioRRHH) sesion.getLogin().getUsuario(); 
      
      LegajoGetter getter = new LegajoGetter(sesion);
      
      boolean vienePorLogin = !aParams.containsKey("oid_leg");
      
      Legajo legReq = null;
      
      if(vienePorLogin)
           legReq = usuario.getLegajo();
      else legReq = getter.execute(aParams);
      
      if(usuario.tieneAccesoAlResumen() && legReq != null && !legReq.esMismoLegajoSession()){
         //EL EVALUADOR TIENE ACCESO AL RESUMEN
      }
      else{      	
         if(legReq != null){
         	boolean finEvaluador    = legReq.getLegajoEjer().finCargaCapacEvaluador();
         	boolean finPlaneamiento = legReq.getLegajoEjer().finCargaCumplPlanificacion();
            //NO ACCEDE AL RESUMEN HASTA QUE TERMINE PLANEAMIENTO Y CAPACIDADES
         	if(!finEvaluador || !finPlaneamiento)
         		writer.addTabla("SinAccesoAResumen");
         }
      }
      
      if(vienePorLogin)
         return new Integer(0);
      
      String nextOper = "";
      Etapa etapa = Etapa.getEtapaActual(sesion);
      
      if(etapa.isCargaObjetivo())
         nextOper = "/FrontServlet?op=CargaObjetivos&oid_leg=" + aParams.getInteger("oid_leg");
      else{
         if(etapa.isCargaCumplimiento())
            nextOper = "/FrontServlet?op=AvanceObjetivos&oid_leg=" + aParams.getInteger("oid_leg");
         else{
            if(etapa.isEvaluaCapacidades())
               nextOper = "/FrontServlet?op=CargarEvaluaciones&oid_leg=" + aParams.getInteger("oid_leg");
            else throw new ExceptionValidacion("Etapas mal configuradas, no se puede conseguir la proxima pagina");
         }
      }
      
      sesion.putAttribute("nextOper", nextOper);
      
      return new Integer(0);
   }
}