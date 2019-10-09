package com.jkt.top150.capacidades.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.ValorCapacidad;

public class FactoryValorCapacidad extends Factory { 

	private static final String OID_VAL_CAP       = "OID_VAL_CAP";
	private static final String CODIGO            = "CODIGO";
	private static final String DESCRIPCION       = "DESCRIPCION";
	private static final String DESC_EXTENDIDA    = "DESC_EXT";
	private static final String ORDEN             = "ORDEN";
	private static final String VALOR_NUMERICO    = "VALOR_NUMERICO";
	private static final String VALORACION_GLOBAL = "VALORACION_GLOBAL";
	private static final String ACTIVO            = "ACTIVO";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		ValorCapacidad persis = (ValorCapacidad) server.getObjectForView(db.getInteger(OID_VAL_CAP));

		persis.setCodigo(db.getString(CODIGO));
		persis.setDescripcion(db.getString(DESCRIPCION));
		persis.setDescExtendida(db.getString(DESC_EXTENDIDA));
		persis.setOrden(db.getSimpleInt(ORDEN));
		persis.setValorNumerico(db.getSimpleInt(VALOR_NUMERICO));
		persis.setValoracionGlobal(db.getSimpleBoolean(VALORACION_GLOBAL));

		if(!db.getSimpleBoolean(ACTIVO))
			persis.setInactivo();

		this.notificar(persis);
	}
}