package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.objetivos.bl.Cuantificacion;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class FactoryCuantificacion extends Factory { 
	
	private static final String OID_CUAN = "OID_CUAN";
	private static final String OID_OBJ = "OID_OBJ";
	private static final String OID_CUANTIF = "OID_CUANTIF";
	private static final String CUANTIFICACION = "CUANTIFICACION";
	private static final String OID_USU = "OID_USU";
	//private static final String FEC_PROCESO = "FEC_PROCESO";
	
	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		Cuantificacion persis = (Cuantificacion) server.getObjectForView(db.getInteger(OID_CUAN));
		
		Integer oidobjetivo= db.getInteger(OID_OBJ);
		IObjectServer objetivoServer = sesion.getObjectServer(Objetivo.class);
		Objetivo objetivo = (Objetivo) objetivoServer.getObjectProxy(oidobjetivo);
		persis.setObjetivo(objetivo);
		
		Integer oidcuantificador= db.getInteger(OID_CUANTIF);
		IObjectServer cuantificadorServer = sesion.getObjectServer(Cuantificador.class);
		Cuantificador cuantificador = (Cuantificador) cuantificadorServer.getObjectProxy(oidcuantificador);
		persis.setCuantificador(cuantificador);
		
		persis.setCuantificacion(db.getString(CUANTIFICACION));
		Integer oidusuario= db.getInteger(OID_USU);
		if(oidusuario.intValue() != 0){
			IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
			Usuario usuario = (Usuario) usuarioServer.getObjectProxy(oidusuario);
			persis.setUsuario(usuario);
		}
		
		this.notificar(persis);
	}
}