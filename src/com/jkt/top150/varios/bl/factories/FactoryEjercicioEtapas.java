package com.jkt.top150.varios.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bm.Ejercicio;

public class FactoryEjercicioEtapas extends Factory { 
	
	private static final String OID_EJE_ETAPA = "OID_EJE_ETAPA";
	private static final String OID_EJE = "OID_EJE";
	private static final String OID_ETAPA = "OID_ETAPA";
	private static final String ESTADO = "ESTADO";
//	private static final String FEC_PROCESO = "FEC_PROCESO";
	private static final String OID_USU = "OID_USU";
	
	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		EjercicioEtapas persis = (EjercicioEtapas) server.getObjectForView(db.getInteger(OID_EJE_ETAPA));
		
		IObjectServer ejercicioServer = sesion.getObjectServer(Ejercicio.class);
		Ejercicio ejercicio = (Ejercicio) ejercicioServer.getObjectProxy(db.getInteger(OID_EJE));
		persis.setEjercicio(ejercicio);
		
		IObjectServer etapaServer = sesion.getObjectServer(Etapa.class);
		Etapa etapa = (Etapa) etapaServer.getObjectByOID(db.getInteger(OID_ETAPA));
		persis.setEtapa(etapa);
		
		persis.setEstado(db.getString(ESTADO));
		
		IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
		Usuario usuario = (Usuario) usuarioServer.getObjectProxy(db.getInteger(OID_USU));
		persis.setUsuario(usuario);
		
		this.notificar(persis);
	}
}