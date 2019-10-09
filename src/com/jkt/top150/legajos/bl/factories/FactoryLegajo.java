package com.jkt.top150.legajos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class FactoryLegajo extends Factory { 
	
	private static final String OID_LEG = "OID_LEG";
	private static final String OID_USU_LEGAJO = "OID_USU_LEGAJO";
	private static final String LEGAJO = "LEGAJO";
	private static final String APELLIDO_PAT = "APELLIDO_PAT";
	private static final String APELLIDO_MAT = "APELLIDO_MAT";
	private static final String NOMBRES = "NOMBRES";
	private static final String FEC_NAC = "FEC_NAC";
	private static final String NACIONALIDAD = "NACIONALIDAD";
	private static final String SEXO = "SEXO";
	private static final String MAIL = "MAIL";
	private static final String ACTIVO = "ACTIVO";
//	private static final String OID_USU = "OID_USU";
//	private static final String FEC_PROCESO = "FEC_PROCESO";
	
	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		Legajo persis = (Legajo) server.getObjectForView(db.getInteger(OID_LEG));
		
		persis.setLegajo(db.getString(LEGAJO));
		persis.setApellidoPaterno(db.getString(APELLIDO_PAT));
		persis.setApellidoMaterno(db.getString(APELLIDO_MAT));
		persis.setNombres(db.getString(NOMBRES));
		persis.setNacimiento(db.getDate(FEC_NAC));
		persis.setNacionalidad(db.getString(NACIONALIDAD));
		persis.setSexo(db.getString(SEXO));
		persis.setMail(db.getString(MAIL));
		
		IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
		Usuario usuario = (Usuario) usuarioServer.getObjectByOID(db.getInteger(OID_USU_LEGAJO));
		persis.setUsuario(usuario);
		
		if(!db.getSimpleBoolean(ACTIVO))
			persis.setInactivo();
		
		this.notificar(persis);
	}
}