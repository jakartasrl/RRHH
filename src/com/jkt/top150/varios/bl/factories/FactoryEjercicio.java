package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.persistence.Factory;
import com.jkt.top150.varios.bm.Ejercicio;

public class FactoryEjercicio extends Factory { 

	private static final String OID_EJE = "OID_EJE";
	private static final String ANIO    = "ANIO";
	private static final String ACTUAL  = "ACTUAL";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		Ejercicio persis = (Ejercicio) server.getObjectForView(db.getInteger(OID_EJE));

		persis.setAnio(db.getSimpleInt(ANIO));
		persis.setActual(db.getSimpleBoolean(ACTUAL));

		this.notificar(persis);
	}
}