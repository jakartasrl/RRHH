package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.Objetivo;

public class FactoryCumplimiento extends Factory { 

	private static final String OID_CUMP = "OID_CUMP";
	private static final String OID_OBJ = "OID_OBJ";
	private static final String OID_ETAPA = "OID_ETAPA";
	private static final String PORC = "PORC";
	private static final String COMEN = "COMEN";
	private static final String RESULTADO = "RESULTADO";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		Cumplimiento persis = (Cumplimiento) server.getObjectForView(db.getInteger(OID_CUMP));

		Objetivo objetivo = (Objetivo) sesion.getObjectServer(Objetivo.class).getObjectProxy(db.getInteger(OID_OBJ));
		persis.setObjetivo(objetivo);

		Etapa etapa = (Etapa) sesion.getObjectServer(Etapa.class).getObjectProxy(db.getInteger(OID_ETAPA));
		persis.setEtapa(etapa);

		persis.setPorcentaje(db.getSimpleInt(PORC));
		persis.setComentario(db.getString(COMEN));
		persis.setResultado(db.getSimpleDouble(RESULTADO));

		this.notificar(persis);
	}
}