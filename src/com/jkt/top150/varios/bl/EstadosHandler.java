package com.jkt.top150.varios.bl;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bl.LegajoEjerEtapa;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class EstadosHandler {
   
   public static final int CARGAOBJETIVOS     = 1;
   public static final int AVANCEOBJETIVOS    = 2;
   public static final int CARGAEVALUACIONES  = 3;
   public static final int VERRESUMENGRAFICO  = 4;
   public static final int INBOX              = -1;
   
   public static final int ESTADO_CARGANDO    = 0;
   public static final int ESTADO_FIN_CARGA   = 1;
   public static final int ESTADO_CERRADO     = -1;
   
   private ISesion sesion;
   private int cargaActual;
   private boolean finalizo;
   private LegajoEjer lEjer;
   
   public EstadosHandler(ISesion aObj){
      sesion = aObj;
   }
   
   public void setLegajoEjer(LegajoEjer aLeg){
      lEjer = aLeg;
   }
   
   public void setCargaActual(int aInt){
      this.cargaActual = aInt;
   }
   
   public void setFinalizoCarga(boolean aBol){
      this.finalizo = aBol;
   }
   
   public void actualizar(Transaccion tran) throws ExceptionDS{
      LegajoEjerEtapa lEjerEtapa = lEjer.getEjercicioEtapas(); 
      
      UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
      
      boolean mismoLegajo = lEjer.getLegajo().esMismoLegajoSession();
      boolean permisosFin = user.tienePermisosFinalizacion();
      
      switch (cargaActual) {
      case CARGAOBJETIVOS:
         if(!finalizo){
            if(!mismoLegajo && permisosFin)
               lEjerEtapa.setEstadoPlaneamientoCargaObj(ESTADO_CARGANDO);
            else {
               if(mismoLegajo)
                    lEjerEtapa.setEstadoEvaluadoCargaObj(ESTADO_CARGANDO);
               else lEjerEtapa.setEstadoEvaluadorCargaObj(ESTADO_CARGANDO);
            }
         }
         else{
            if(!mismoLegajo && permisosFin)
               lEjerEtapa.setEstadoPlaneamientoCargaObj(ESTADO_CERRADO);
            else{
               //YA SEA QUE FINALIZA EL EVALUADO O EL EVALUADOR; EL EVALUADO QUEDA COMO FINALIZADO
               lEjerEtapa.setEstadoEvaluadoCargaObj(ESTADO_FIN_CARGA);
               
               if(!mismoLegajo)
                  lEjerEtapa.setEstadoEvaluadorCargaObj(ESTADO_FIN_CARGA);
            }

            MailSender sender = new MailSender();
            sender.enviarMail(CARGAOBJETIVOS, lEjer, mismoLegajo, permisosFin);
         }
         
         break;
      case AVANCEOBJETIVOS:
         if(!finalizo){
            if(!mismoLegajo && permisosFin)
               lEjerEtapa.setEstadoPlaneamientoCumplimientos(ESTADO_CARGANDO);
            else {
               if(mismoLegajo)
                    lEjerEtapa.setEstadoEvaluadoCumplimientos(ESTADO_CARGANDO);
               else lEjerEtapa.setEstadoEvaluadorCumplimientos(ESTADO_CARGANDO);
            }
         }
         else{
            if(!mismoLegajo && permisosFin)
               lEjerEtapa.setEstadoPlaneamientoCumplimientos(ESTADO_CERRADO);
            else{
               //YA SEA QUE FINALIZA EL EVALUADO O EL EVALUADOR; EL EVALUADO QUEDA COMO FINALIZADO
               lEjerEtapa.setEstadoEvaluadoCumplimientos(ESTADO_FIN_CARGA);
               
               if(!mismoLegajo)
                  lEjerEtapa.setEstadoEvaluadorCumplimientos(ESTADO_FIN_CARGA);

               MailSender sender = new MailSender();
               sender.enviarMail(AVANCEOBJETIVOS, lEjer, mismoLegajo, permisosFin);
            }
         }
         
         break;
      case CARGAEVALUACIONES:
      	EjercicioEtapas ee = EjercicioEtapas.getEjercicioEtapas(sesion, Etapa.getEtapaActual(sesion));
      	lEjerEtapa = lEjer.getEjercicioEtapas(ee);
      	
         //PLANEAMIENTO NO TIENE ACCESO A LA CARGA DE EVALUACIONES
         //EN LA PANTALLA DE CARGA DE EVALUACIONES NO HAY BOTON FINALIZAR
         if(!finalizo){
         	if(mismoLegajo)
         		  lEjerEtapa.setEstadoEvaluadoCapacidades(ESTADO_CARGANDO);
         	else lEjerEtapa.setEstadoEvaluadorCapacidades(ESTADO_CARGANDO);
         }
         else{
            //SI EL EVALUADOR (EL EVALUADO SOLO PUEDE CONSULTAR); EL EVALUADO QUEDA COMO FINALIZADO
            lEjerEtapa.setEstadoEvaluadoCapacidades(ESTADO_FIN_CARGA);
            lEjerEtapa.setEstadoEvaluadorCapacidades(ESTADO_FIN_CARGA);

            MailSender sender = new MailSender();
            sender.enviarMail(CARGAEVALUACIONES, lEjer, mismoLegajo, false);
         }
         
         break;
      case VERRESUMENGRAFICO:
         //PLANEAMIENTO NO TIENE ACCESO AL RESUMEN.
      	//LA PARTE DEL RESUMEN SE MANEJA INDEPENDIENTEMENTE SI FINALIZO O NO.
      	
      	if(mismoLegajo){
      		lEjerEtapa.setEstadoEvaluadoCargaObj(ESTADO_CERRADO);
      		lEjerEtapa.setEstadoEvaluadoCapacidades(ESTADO_CERRADO);
      		lEjerEtapa.setEstadoEvaluadoCumplimientos(ESTADO_CERRADO);
      	}
      	else{
      		lEjerEtapa.setEstadoEvaluadorCargaObj(ESTADO_FIN_CARGA);
      		lEjerEtapa.setEstadoEvaluadorCapacidades(ESTADO_FIN_CARGA);
      		lEjerEtapa.setEstadoEvaluadorCumplimientos(ESTADO_FIN_CARGA);
      	}

      	MailSender sender = new MailSender();
      	sender.enviarMail(AVANCEOBJETIVOS, lEjer, mismoLegajo, user.isPlanificacion());

      	break;
      }
      
      tran.addObject(lEjerEtapa);
   }   
}