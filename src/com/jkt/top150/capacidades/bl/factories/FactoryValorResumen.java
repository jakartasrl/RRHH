package com.jkt.top150.capacidades.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.ValorResumen;

public class FactoryValorResumen extends Factory { 

	private static final String OID_VAL_RES       = "OID_VAL_RES";
	private static final String CODIGO            = "CODIGO";
	private static final String DESCRIPCION       = "DESCRIPCION";
	private static final String DESC_EXTENDIDA    = "DESC_EXT";
	private static final String ORDEN             = "ORDEN";
	private static final String VALOR_NUMERICO    = "VALOR_NUMERICO";
	private static final String ACTIVO            = "ACTIVO";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		ValorResumen persis = (ValorResumen) server.getObjectForView(db.getInteger(OID_VAL_RES));

		persis.setCodigo(db.getString(CODIGO));
		persis.setDescripcion(db.getString(DESCRIPCION));
		persis.setDescExtendida(db.getString(DESC_EXTENDIDA));
		persis.setOrden(db.getSimpleInt(ORDEN));
		persis.setValorNumerico(db.getSimpleInt(VALOR_NUMERICO));

		if(!db.getSimpleBoolean(ACTIVO))
			persis.setInactivo();

		this.notificar(persis);
	}
}