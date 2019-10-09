package com.jkt.top150.objetivos.bm.op;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.capacidades.bm.Capacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidad;
import com.jkt.top150.capacidades.bm.EvalCapacidadGlobal;
import com.jkt.top150.capacidades.bm.EvalFactor;
import com.jkt.top150.capacidades.bm.Factor;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.LegajoVarios;
import com.jkt.top150.varios.bm.ConcVarios;
import com.jkt.top150.varios.bm.Ejercicio;

public class VistaPrevia extends Operation implements IObserver{
   
   private XMLTableMaker detalleEval  = new XMLTableMaker("mtEvaluacionDet", this);
   private XMLTableMaker detalleComen = new XMLTableMaker("mtComentariosConceptos", this);
   
   private CumplimientoGlobal cump;
   private LegajoEjer legEjer;
   private Etapa etapa;
   
   private int secu  = 1;
   
   public Integer execute(MapDS aParams) throws Exception {
   	IObjectServer server = sesion.getObjectServer(Ejercicio.class);
   	Ejercicio ejer = (Ejercicio) server.getObjectByCodigo(aParams.getInteger("anio"));
   	
   	((UsuarioRRHH) sesion.getLogin().getUsuario()).setEjercicio(ejer);
   	
      server = sesion.getObjectServer(LegajoEjer.class);
      legEjer = (LegajoEjer) server.getObjectByOID(aParams.getInteger("oid_legajo"));
      
      etapa = Etapa.getEtapaActual(sesion);
      
      cump = CumplimientoGlobal.getCumplimientoGlobal(legEjer, sesion);
      
      this.writeCabecera();
      this.writeObjetivos();
      this.writeEvaluacion();
      this.writeCabeceraValoracion();
      this.writeComentarios();
      
      return new Integer(0);
   }
   
   private void writeCabecera() throws ExceptionDS{
      writer.addTabla("mtCabecera");
      writer.addFila();
      writer.addColumna("oid_legajo",          legEjer.getOID());
      writer.addColumna("anio",                legEjer.getEjercicio().getOID());
      writer.addColumna("nombreyape_evaluado", legEjer.getLegajo().getApNom());
      writer.addColumna("nro_emp_evaluado",    legEjer.getLegajo().getLegajo());
      
      LegajoEjer evaluador = legEjer.getEvaluador();
      if(evaluador != null){
         writer.addColumna("nombreyape_evaluador",evaluador.getLegajo().getApNom());
         writer.addColumna("nro_emp_evaluador",   evaluador.getLegajo().getLegajo());
      }
      else{
         writer.addColumna("nombreyape_evaluador","");
         writer.addColumna("nro_emp_evaluador",   "");
      }

      writer.addTabla("mtObjetivos");
      writer.addFila();
      writer.addColumna("oid_objetivo",      1);
      writer.addColumna("oid_legajo",        legEjer.getOID());
      writer.addColumna("descripcion",       "Descripcion");
      writer.addColumna("cuantificacion",    "Cuantificacion");
      writer.addColumna("ponderacion",       "Ponderacion");
      writer.addColumna("grado_consecucion", "Grado Consecucion");
      writer.addColumna("mes_desde",         "Junio");
      writer.addColumna("mes_hasta",         "Diciembre");
      writer.addColumna("resultado",         "Resultado");

      String comentario = cump.getComentario();
      if(comentario == null)
         comentario = "";
      
      writer.addColumna("comentario",        comentario);
   }
   
   private void writeObjetivos() throws ExceptionDS{
      secu = 1;
      
      writer.addTabla("mtObjetivoDet");
      
      legEjer.getObjetivos(this);
   }
   
   private void writeEvaluacion() throws ExceptionDS{
      secu = 1;
      
      writer.addTabla("mtObjetivoEvaluacion");
      writer.addFila();
      writer.addColumna("oid_objetivo_evaluacion", 1);
      writer.addColumna("oid_objetivo",            1);

      IObjectServer server = sesion.getObjectServer(ValCumpGlobal.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
   }
   
   private void writeCabeceraValoracion() throws ExceptionDS{
      secu = 1;
      
      writer.addTabla("mtEvaluacion");
      IObjectServer server = sesion.getObjectServer(Capacidad.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
      
      writer.addTabla("mtEvaGlobal");
      writer.addFila();
      writer.addColumna("oid_eva_global", 1);
      writer.addColumna("oid_legajo",  legEjer.getOID());

      EvalCapacidadGlobal eval = EvalCapacidadGlobal.getCapacidadGlobal(legEjer, sesion);
      if(eval != null && eval.getValor() != null){
         writer.addColumna("cordenada_y",  eval.getValor().getValorNumerico());
         writer.addColumna("campo_selec",  eval.getValor().getCodigo());
      }
      else {
         writer.addColumna("campo_selec",  "");
         writer.addColumna("cordenada_y",  0);
      }
         
      writer.addColumna("cordenada_x",  Cumplimiento.getValorCumpliento(legEjer, etapa));
   }
   
   private void writeDetalleCapacidad(Capacidad capac) throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(EvalFactor.class);
      
      Map condi = new HashMap();
      condi.put("Legajo", legEjer);
      condi.put("Etapa", etapa);

      Iterator it = capac.getFactores().iterator();
      while(it.hasNext()){
         Factor fact = (Factor) it.next();
         
         condi.put("Factor", fact);
         
         detalleEval.addFila();
         detalleEval.addColumna("oid_evaluacion_det", fact.getOID());
         detalleEval.addColumna("oid_evaluacion",     fact.getCapacidad().getOID());
         detalleEval.addColumna("detalle",            fact.getDescripcion());
         
         EvalFactor eval = (EvalFactor) server.getObjectByCodigo(condi);
         
         if(eval != null)
            detalleEval.addColumna("valoracion_" + eval.getValor().getCodigo(), true);
      }
   }
      
   private void writeComentarios() throws ExceptionDS{
      writer.addTabla("mtConceptos");
      
      IObjectServer server = sesion.getObjectServer(ConcVarios.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
   }
      
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      if(aObj instanceof Objetivo){
         this.writeObjetivo((Objetivo) aObj);
         return;
      }
      
      if(aObj instanceof ValCumpGlobal){
         this.writeValCumpGlobal((ValCumpGlobal) aObj);
         return;
      }
      
      if(aObj instanceof Capacidad){
         this.writeCapacidad((Capacidad) aObj);
         return;
      }
      
      if(aObj instanceof ConcVarios){
         this.writeConcVarios((ConcVarios) aObj);
         return;
      }
      
      if(aObj instanceof LegajoVarios){
         this.writeLegajoVarios((LegajoVarios) aObj);
         return;
      }
   }
   
   private void writeObjetivo(Objetivo next) throws ExceptionDS{
      writer.addFila();
      writer.addColumna("oid_objetivo",      1);
      writer.addColumna("oid_objetivo_det",  next.getOID());
      writer.addColumna("objetivo_det",      next.getDescripcion());
      writer.addColumna("aspectos_cualitativos",  next.getAspectosCualitativos());
      writer.addColumna("cuantificacion",    next.getCuantificacion());
      writer.addColumna("ponderacion",       next.getPonderacion());
      writer.addColumna("secu",              secu++);
      
      List cumplimientos = next.getCumplimientos();
      double valor1    = 0;
      double valor2    = 0;
      double resultado = 0;
      try{
         valor1    = ((Cumplimiento) cumplimientos.get(0)).getPorcentaje();
      }
      catch(Exception ignore){}
      
      try{
         valor2    = ((Cumplimiento) cumplimientos.get(1)).getPorcentaje();
         resultado = ((Cumplimiento) cumplimientos.get(1)).getResultado();
      }
      catch(Exception ignore){}
      
      writer.addColumna("mes_desde",         valor1);
      writer.addColumna("mes_hasta",         valor2);
      writer.addColumna("resultado",         resultado);
   }
   
   private void writeValCumpGlobal(ValCumpGlobal next) throws ExceptionDS{
      writer.addColumna("titulo_" + secu, next.getDescripcion());
      writer.addColumna("codigo_" + secu, next.getCodigo());
      writer.addColumna("descripcion_" + secu, next.getDescExtendida());
      writer.addColumna("valor_" + secu,  next.getValorNumerico());
      
      boolean esElMismo = false;
      if((cump != null) && (cump.getValorCumplimiento() != null) && (next.equals(cump.getValorCumplimiento())))
         esElMismo = true;
      
      writer.addColumna("seleccionado_" + secu, esElMismo);
      
      secu++;
   }
   
   private void writeCapacidad(Capacidad capa) throws ExceptionDS{
      writer.addFila();
      writer.addColumna("oid_evaluacion", capa.getOID());
      writer.addColumna("secu",           secu++);
      writer.addColumna("titulo",         capa.getDescripcion());
      
      Map condi = new HashMap();
      condi.put("Etapa", etapa);
      condi.put("Legajo", legEjer);
      condi.put("Capacidad", capa);
      
      IObjectServer evalCap = sesion.getObjectServer(EvalCapacidad.class);
      
      EvalCapacidad evalC = (EvalCapacidad) evalCap.getObjectByCodigo(condi);
      if(evalC != null)
           writer.addColumna("valoracion", evalC.getValor().getDescripcion());
      else writer.addColumna("valoracion", "");

      writer.addColumna("oid_legajo",     legEjer.getOID());
      
      this.writeDetalleCapacidad(capa);
   }
   
   private void writeConcVarios(ConcVarios conc) throws ExceptionDS{
      writer.addFila();
      writer.addColumna("oid_conc",        conc.getOID());
      writer.addColumna("titu_conc",       conc.getDescripcion());
      writer.addColumna("titu_conc",       conc.getDescripcion());
      writer.addColumna("oid_eva_global", 1);
      
      Map condi = new HashMap();
      condi.put("LegajoEjer", legEjer);
      condi.put("ConcVarios", conc);
   
      IObjectServer server = sesion.getObjectServer(LegajoVarios.class);
      server.getObjects(IDB.SELECT_ALL, condi, this);
   }
   
   private void writeLegajoVarios(LegajoVarios varios) throws ExceptionDS{
      detalleComen.addFila();
      detalleComen.addColumna("oid_conc",  varios.getConcVarios().getOID());
      detalleComen.addColumna("oid_comen", varios.getOID());
      detalleComen.addColumna("comen",     varios.getTexto());
   }
}