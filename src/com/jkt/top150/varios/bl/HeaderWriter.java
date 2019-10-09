package com.jkt.top150.varios.bl;

import com.jkt.framework.patterns.writers.IWriter;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class HeaderWriter {
   
   private ISesion sesion;
   
   public HeaderWriter(ISesion aSes){
      sesion = aSes;
   }
   
   public void write(IWriter writer, Legajo legajo, int aPagina) throws ExceptionDS{
      UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
      Etapa etapa      = Etapa.getEtapaActual(sesion);
      Ejercicio ejer   = user.getEjercicio();
      
      writer.addTabla("Header");
      writer.addFila();
      writer.addColumna("ejercicio",  ejer.getAnio());
      writer.addColumna("etapa",      etapa.getDescripcion());
      writer.addColumna("funcion", user.getFuncion(legajo));
      
      if(legajo != null){
         writer.addColumna("evaluado", legajo.getApNom());
         writer.addColumna("oid_leg",  legajo.getOID());
         
         LegajoEjer evaluador = legajo.getLegajoEjer().getEvaluador();
         if(evaluador != null)
              writer.addColumna("evaluador", evaluador.getLegajo().getApNom());
         else writer.addColumna("evaluador", "");
      }
      else{
         writer.addColumna("evaluado", user.getApeNom());
         writer.addColumna("oid_leg",  -1);
         writer.addColumna("evaluador", "");
      }
      
      Legajo legajoSesion = user.getLegajo();
      if(!user.isRRHH() && (legajo == null || legajoSesion == null || !etapa.isIniciada())){
         writer.addTabla("ReadOnly");
         return;
      }
      
      if(aPagina == EstadosHandler.CARGAOBJETIVOS){
         if(legajo.getLegajoEjer().finCargaObjetivos())
            writer.addTabla("ReadOnly");
      }
      
      if(aPagina == EstadosHandler.AVANCEOBJETIVOS){
         if(legajo.getLegajoEjer().finCargaCumplimientos())
            writer.addTabla("ReadOnly");
      }
      
      if(aPagina == EstadosHandler.CARGAEVALUACIONES){
         if(legajo.getLegajoEjer().finCargaCapacidades(writer))
            writer.addTabla("ReadOnly");
      }
      
      //LAS REGLAS DE NEGOCIO DE ESTA PAGINA SON IGUALES A LA ANTERIOR
      if(aPagina == EstadosHandler.VERRESUMENGRAFICO){
         if(legajo.getLegajoEjer().finCargaResumen(writer))
            writer.addTabla("ReadOnly");
      }
   }
}