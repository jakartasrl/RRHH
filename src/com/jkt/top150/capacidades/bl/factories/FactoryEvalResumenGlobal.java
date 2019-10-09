package com.jkt.top150.capacidades.bl.factories;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.top150.capacidades.bm.EvalResumenGlobal;
import com.jkt.top150.capacidades.bm.ValorResumen;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class FactoryEvalResumenGlobal extends Factory {

	private static final String OID           = "OID_EVAL_RES";
	private static final String OID_VALOR     = "OID_VAL_RES";
	private static final String OID_ETAPA     = "OID_ETAPA";
	private static final String OID_LEG_EJER  = "OID_LEG_EJE";
//	private static final String OID_USUARIO   = "OID_USU";
//	private static final String FEC_PROCESO   = "FEC_PROCESO";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		EvalResumenGlobal persis = (EvalResumenGlobal) server.getObjectForView(db.getInteger(OID));

		IObjectServer sValor = sesion.getObjectServer(ValorResumen.class);
		persis.setValor((ValorResumen) sValor.getObjectProxy(db.getInteger(OID_VALOR)));

		IObjectServer sEtapa = sesion.getObjectServer(Etapa.class);
		persis.setEtapa((Etapa) sEtapa.getObjectProxy(db.getInteger(OID_ETAPA)));

		IObjectServer sLeg = sesion.getObjectServer(LegajoEjer.class);
		persis.setLegajo((LegajoEjer) sLeg.getObjectProxy(db.getInteger(OID_LEG_EJER)));

		this.notificar(persis);
	}
}