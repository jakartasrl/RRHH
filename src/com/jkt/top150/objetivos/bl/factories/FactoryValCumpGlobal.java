package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;

public class FactoryValCumpGlobal extends Factory { 
   
   private static final String OID_VAL_CUMP = "OID_VAL_CUMP";
   private static final String CODIGO = "CODIGO";
   private static final String DESCRIPCION = "DESCRIPCION";
   private static final String ORDEN = "ORDEN";
   private static final String VALOR_NUMERICO = "VALOR_NUMERICO";
   private static final String PONDERADOR     = "PONDERADOR";
   private static final String DESC_EXTENDIDA = "DESC_EXT";
   private static final String ACTIVO         = "ACTIVO";
   
      public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
      ValCumpGlobal persis = (ValCumpGlobal) server.getObjectForView(db.getInteger(OID_VAL_CUMP));
      
      persis.setCodigo(db.getString(CODIGO));
      persis.setDescripcion(db.getString(DESCRIPCION));
      persis.setDescExtendida(db.getString(DESC_EXTENDIDA));
      persis.setOrden(db.getSimpleInt(ORDEN));
      persis.setValorNumerico(db.getSimpleInt(VALOR_NUMERICO));
      persis.setPonderador(db.getSimpleDouble(PONDERADOR));
      
      if(!db.getSimpleBoolean(ACTIVO))
         persis.setInactivo();
      
      this.notificar(persis);
   }
}