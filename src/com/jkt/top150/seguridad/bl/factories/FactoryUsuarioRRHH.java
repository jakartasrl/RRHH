package com.jkt.top150.seguridad.bl.factories;

import com.jkt.framework.util.ExceptionDS;
import com.jkt.seguridad.bl.factories.FactoryUsuario;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class FactoryUsuarioRRHH extends FactoryUsuario {
	
	private static final String ES_RRHH         = "USUARIO_RRHH";
	private static final String ES_PLANEAMIENTO = "USUARIO_PLANEAMIENTO";
	private static final String ES_CONFIGURADOR = "USUARIO_CONFIG";
	
	private boolean isRRHH;
	private boolean isPlaneamiento;
	private boolean isConfigurador;
	
	/**
	 * PARA SALIR DEL PASO SE RESCRIBIO DE ESTA FORMA
	 */
	
	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		isRRHH         = db.getSimpleBoolean(ES_RRHH);
		isPlaneamiento = db.getSimpleBoolean(ES_PLANEAMIENTO);
		isConfigurador = db.getSimpleBoolean(ES_CONFIGURADOR);
		
		super.newRecordNotify(db);
	}
	
	public void notificar(Object aObj) throws ExceptionDS {
		((UsuarioRRHH) aObj).setRRHH(isRRHH);
		((UsuarioRRHH) aObj).setPlanificacion(isPlaneamiento);
		((UsuarioRRHH) aObj).setConfigurador(isConfigurador);
		
		super.notificar(aObj);
	}
}