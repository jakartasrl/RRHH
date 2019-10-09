package com.jkt.top150.objetivos.bm.op;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.HeaderWriter;

public class AvanceObjetivos extends CargaObjetivos {
   private XMLTableMaker mEtapa = new XMLTableMaker("Etapas", this);
   private XMLTableMaker mValCu = new XMLTableMaker("ValCumpGlobal", this);
   private XMLTableMaker mCumpl;
   private Etapa etapaActual;
   
   private List etapas;
   
   public Integer execute(MapDS aParams) throws Exception{
      this.writeEtapas();
      
      super.execute(aParams);
      
      IObjectServer server = sesion.getObjectServer(ValCumpGlobal.class);
      server.getObjects(IDB.SELECT_ACTIVOS, null, this);
      
      this.writeCumplimientoGlobal();
      
      Integer result = new Integer(0);
      if(aParams.containsKey("imprimir"))
         result = new Integer(10);
      
      return result;
   }
   
   protected void writeHeader() throws ExceptionDS{
      HeaderWriter hw = new HeaderWriter(sesion);
      hw.write(writer, legajo, EstadosHandler.AVANCEOBJETIVOS);
      
      if(legajo.esMismoLegajoSession()) //ES MISMO USUARIO
         writer.addTabla("EvitarValidacionCumpGlobal");
   }
   
   private void writeCumplimientoGlobal() throws ExceptionDS{
      XMLTableMaker maker = new XMLTableMaker("CumpGlobal", this);
      CumplimientoGlobal cumpl = CumplimientoGlobal.getCumplimientoGlobal(legajo.getLegajoEjer(), sesion);
      maker.addFila();
      
      if(cumpl.getValorCumplimiento() == null)
           maker.addColumna("oid_val_cumpl", 0);
      else maker.addColumna("oid_val_cumpl", cumpl.getValorCumplimiento().getOID());
      
      String comentario = "";
      if(cumpl.getComentario() != null)
         comentario = cumpl.getComentario();
      
      maker.addColumna("comentario", comentario);
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      super.notify(aObj);
      
      if(aObj instanceof ValCumpGlobal){
         this.writeValor((ValCumpGlobal) aObj);
         return;
      }
      
      if(aObj instanceof Objetivo){
         this.writeCumplimientos((Objetivo) aObj);
         return;
      }
   }
   
   private void writeCumplimientos(Objetivo aObj) throws ExceptionDS{
      mCumpl = new XMLTableMaker("Cumplimiento_" + aObj.getOID(), this);
      
      List etapasAux = new ArrayList(etapas);

      double resultado = 0;
      Iterator it = aObj.getCumplimientos().iterator();
      while(it.hasNext()){
         Cumplimiento next = (Cumplimiento) it.next();
         mCumpl.addFila();
         mCumpl.addColumna("oid_cumpl",        next.getOID());
         
         if(next.getPorcentaje() < 0)
         	  mCumpl.addColumna("porcentaje",  "NA");
         else mCumpl.addColumna("porcentaje",  next.getPorcentaje());

         mCumpl.addColumna("comentario",       next.getComentario());
         
         Etapa etapa = next.getEtapa();
         
         mCumpl.addColumna("desc_etapa",       etapa.getDescripcion());
         mCumpl.addColumna("oid_etapa",        etapa.getOID());
         mCumpl.addColumna("calculaResultado", etapa.isCalculaResultado());
         mCumpl.addColumna("actual",           etapa.equals(etapaActual));
         
         if(etapa.isCalculaResultado() && etapa.equals(etapaActual))
         	resultado = next.getResultado();
         
         etapasAux.remove(next.getEtapa());
      }
      
      it = etapasAux.iterator();
      while(it.hasNext()){
         Etapa next = (Etapa) it.next();
         mCumpl.addFila();
         mCumpl.addColumna("oid_cumpl",  0);
         mCumpl.addColumna("porcentaje", 0);
         mCumpl.addColumna("cump_real",  " ");
         mCumpl.addColumna("comentario", " ");
         mCumpl.addColumna("oid_etapa",        next.getOID());
         mCumpl.addColumna("desc_etapa",       next.getDescripcion());
         mCumpl.addColumna("calculaResultado", next.isCalculaResultado());
         mCumpl.addColumna("actual",           next.equals(etapaActual));
      }
      
      mObjetivos.addColumna("resultado", resultado);
   }
   
   private void writeValor(ValCumpGlobal valCump) throws ExceptionDS{
      mValCu.addFila();
      mValCu.addColumna("oid_val",     valCump.getOID());
      mValCu.addColumna("codigo",      valCump.getCodigo());
      mValCu.addColumna("descripcion", valCump.getDescripcion());
      mValCu.addColumna("desc_ext",    valCump.getDescExtendida());
      mValCu.addColumna("valor",       valCump.getValorNumerico());
   }
   
   private void writeEtapas() throws ExceptionDS{
      etapaActual = Etapa.getEtapaActual(sesion);
      etapas      = Etapa.getEtapasEvaluadoras(sesion);
      
      Iterator it = etapas.iterator();
      while(it.hasNext()){
         Etapa aEtapa = (Etapa) it.next();
         mEtapa.addFila();
         mEtapa.addColumna("oid_etapa",   aEtapa.getOID());
         mEtapa.addColumna("descripcion", aEtapa.getDescripcion());
      }
   }
}