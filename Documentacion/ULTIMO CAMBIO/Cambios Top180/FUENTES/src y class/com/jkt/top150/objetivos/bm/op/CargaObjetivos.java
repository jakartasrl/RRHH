package com.jkt.top150.objetivos.bm.op;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bl.Cuantificacion;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.HeaderWriter;
import com.jkt.top150.varios.bl.LegajoGetter;

public class CargaObjetivos extends Operation implements IObserver{
   protected XMLTableMaker mObjetivos       = null; 
   protected XMLTableMaker mCuantificadores = null;
   protected Legajo     legajo;
   
   public Integer execute(MapDS aParams) throws Exception {
      LegajoGetter getter = new LegajoGetter(sesion);
      legajo = getter.execute(aParams);
         
      if(legajo.getLegajoEjer().getEvaluador() == null && legajo.esMismoLegajoSession())
         throw new ExceptionValidacion("Antes de cargar los objetivos usted debe configurar un evaluador.");

      LegajoEjer legajoEjercicio = legajo.getLegajoEjer();
      
      writer.addTabla("Grupos");
      IObjectServer server = sesion.getObjectServer(GrupoObjetivo.class);
      server.getObjects(IDB.SELECT_ACTIVOS, legajoEjercicio, this);
      
      this.writeHeader();
      
      return new Integer(0);
   }
   
   protected void writeHeader() throws ExceptionDS{
      HeaderWriter hw = new HeaderWriter(sesion);
      hw.write(writer, legajo, EstadosHandler.CARGAOBJETIVOS);
   }
      
   public Object getResult(){
      return null;
   }
   
   public void notify(Object aObj) throws ExceptionDS{
      if(aObj instanceof GrupoObjetivo){
         this.writeGrupo((GrupoObjetivo) aObj);
         return;
      }
      
      if(aObj instanceof Objetivo){
         this.writeObjetivo((Objetivo) aObj);
         return;
      }
   }
   
   private void writeCuantificadores(GrupoObjetivo aGrupo) throws ExceptionDS{
      Iterator it = aGrupo.getCuantificadores().iterator();
      while(it.hasNext()){
         Cuantificador aCuanti = (Cuantificador) it.next(); 
         mCuantificadores.addFila();
         mCuantificadores.addColumna("oid_cuanti",  aCuanti.getOID());
         mCuantificadores.addColumna("orden",       aCuanti.getOrden());
         mCuantificadores.addColumna("descripcion", aCuanti.getDescripcion());
      }
   }
   
   private void writeGrupo(GrupoObjetivo aGrupo) throws ExceptionDS{
      mObjetivos       = new XMLTableMaker("Objetivos_" + aGrupo.getOID(), this);
      mCuantificadores = new XMLTableMaker("Cuantificadores_" + aGrupo.getOID(), this);
      
      writer.addFila();
      writer.addColumna("oid_grupo",   aGrupo.getOID());
      writer.addColumna("codigo",      aGrupo.getCodigo());
      writer.addColumna("descripcion", aGrupo.getDescripcion());
      writer.addColumna("is_reto",     aGrupo.isReto());
      
      writeCuantificadores(aGrupo);
      
      aGrupo.getObjetivos(this, legajo);
   }
   
   protected void writeObjetivo(Objetivo aObj) throws ExceptionDS{
      mObjetivos.addFila();
      mObjetivos.addColumna("oid_objetivo", aObj.getOID());
      mObjetivos.addColumna("cuantificacion", aObj.getCuantificacion());
      mObjetivos.addColumna("descripcion", aObj.getDescripcion());
      mObjetivos.addColumna("fuente", aObj.getFuente());
      mObjetivos.addColumna("ponderacion", aObj.getPonderacion());
      mObjetivos.addColumna("nro_objetivo", aObj.getNroObjetivo());
      mObjetivos.addColumna("comentario",   aObj.getAspectosCualitativos());
      mObjetivos.addColumna("cump_real",    aObj.getCumplimientoReal());
      
      //PARA QUE NO ME BORRE LA LISTA ORIGINAL
      List cuantificadoresAux = new ArrayList(aObj.getGrupo().getCuantificadores()); 
      
      Iterator it = aObj.getCuantificaciones().iterator();
      while(it.hasNext()){
         Cuantificacion cuanti = (Cuantificacion) it.next();
         
         mObjetivos.addColumna("oid_cuanti_" + cuanti.getCuantificador().getOrden(), cuanti.getOID());
         mObjetivos.addColumna("cuanti_"     + cuanti.getCuantificador().getOrden(), cuanti.getCuantificacion());
         
         cuantificadoresAux.remove(cuanti.getCuantificador());
      }
      
      it = cuantificadoresAux.iterator();
      while(it.hasNext()){
         Cuantificador cuanti = (Cuantificador) it.next();
         
         mObjetivos.addColumna("oid_cuanti_" + cuanti.getOrden(), 0);
         mObjetivos.addColumna("cuanti_"     + cuanti.getOrden(), 0);
      }
   }
}