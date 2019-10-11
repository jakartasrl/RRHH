package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.objetivos.bm.GrupoObjetivo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class FactoryObjetivo extends Factory { 

	private static final String OID_OBJ = "OID_OBJ";
	private static final String OID_LEG_EJE = "OID_LEG_EJE";
	private static final String OID_GRUPO = "OID_GRUPO";
	private static final String NRO_OBJ = "NRO_OBJ";
	private static final String DESCRIPCION = "DESCRIPCION";
	private static final String CUANTIFICACION = "CUANTIFICACION";
	private static final String PONDERACION = "PONDERACION";
	private static final String FUENTE = "FUENTE";
	private static final String ASPECTOS_CUAL = "ASPECTOS_CUAL";
	private static final String OID_USU = "OID_USU";
	private static final String CUMP_REAL = "CUMP_REAL";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		Objetivo persis = (Objetivo) server.getObjectForView(db.getInteger(OID_OBJ));

		Integer oidlegajo= db.getInteger(OID_LEG_EJE);
		IObjectServer legajoServer = sesion.getObjectServer(LegajoEjer.class);
		LegajoEjer legajo = (LegajoEjer) legajoServer.getObjectProxy(oidlegajo);
		persis.setLegajo(legajo);

		Integer oidgrupo= db.getInteger(OID_GRUPO);
		IObjectServer grupoServer = sesion.getObjectServer(GrupoObjetivo.class);
		GrupoObjetivo grupo = (GrupoObjetivo) grupoServer.getObjectProxy(oidgrupo);
		persis.setGrupo(grupo);

		persis.setNroObjetivo(db.getSimpleInt(NRO_OBJ));
		persis.setDescripcion(db.getString(DESCRIPCION));
		persis.setCuantificacion(db.getString(CUANTIFICACION));
		persis.setPonderacion(db.getSimpleDouble(PONDERACION));
		persis.setFuente(db.getString(FUENTE));
		persis.setAspectosCualitativos(db.getString(ASPECTOS_CUAL));
		persis.setCumplimientoReal(db.getString(CUMP_REAL));

		Integer oidusuario= db.getInteger(OID_USU);
		if(oidusuario.intValue() != 0){
			IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
			Usuario usuario = (Usuario) usuarioServer.getObjectProxy(oidusuario);
			persis.setUsuarioAlta(usuario);
		}

		this.notificar(persis);
	}
}