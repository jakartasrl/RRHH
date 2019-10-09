package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;

public class FactoryCuantificador extends Factory { 
   
   private static final String OID_CUANTIF = "OID_CUANTIF";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String OID_GRUPO = "OID_GRUPO";
   private static final String ORDEN = "ORDEN";
   private static final String ACTIVO = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      Cuantificador persis = (Cuantificador) server.getObjectForView(db.getInteger(OID_CUANTIF));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      
      IObjectServer grupoObjetivoServer = sesion.getObjectServer(GrupoObjetivo.class);
      GrupoObjetivo grupoObjetivo = (GrupoObjetivo) grupoObjetivoServer.getObjectProxy(db.getInteger(OID_GRUPO));
      persis.setGrupoObjetivo(grupoObjetivo);
      
      persis.setOrden(db.getSimpleInt(ORDEN));
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}