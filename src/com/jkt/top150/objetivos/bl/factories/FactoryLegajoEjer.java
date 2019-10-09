package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.configuracion.bm.Estructura;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class FactoryLegajoEjer extends Factory { 

	private static final String OID_LEG_EJE = "OID_LEG_EJE";
	private static final String OID_LEG = "OID_LEG";
	private static final String OID_EJE = "OID_EJE";
	private static final String OID_EST = "OID_EST";
	private static final String CATEGORIA = "CATEGORIA";
	private static final String PUESTO = "PUESTO";
	private static final String ES_EVALUADO = "ES_EVALUADO";
	private static final String ES_EVALUADOR = "ES_EVALUADOR";
	private static final String OID_EVALUADOR = "OID_EVALUADOR";
	private static final String ES_ADMINISTRADOR  = "ES_ADMINISTRADOR";
	private static final String OID_ADMINISTRADOR = "OID_ADMINISTRADOR";
	private static final String OID_USU = "OID_USU";

//	private static final String OID_ENT = "OID_ENT";
//	private static final String OID_PAIS = "OID_PAIS";
//	private static final String EDAD_CONYUGE = "EDAD_CONYUGE";  
//	private static final String HIJOS = "HIJOS";
//	private static final String ESTUDIOS_UNIV = "ESTUDIOS_UNIV";
//	private static final String POSGRADOS = "POSGRADOS";
//	private static final String FOTO = "FOTO";
//	private static final String FEC_PROCESO = "FEC_PROCESO";

	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		LegajoEjer persis = (LegajoEjer) server.getObjectForView(db.getInteger(OID_LEG_EJE));

		IObjectServer legajoServer = sesion.getObjectServer(Legajo.class);
		persis.setLegajo((Legajo) legajoServer.getObjectByOID(db.getInteger(OID_LEG)));

		IObjectServer ejercicioServer = sesion.getObjectServer(Ejercicio.class);
		Ejercicio ejercicio = (Ejercicio) ejercicioServer.getObjectProxy(db.getInteger(OID_EJE));
		persis.setEjercicio(ejercicio);

		Integer oidestructura= db.getInteger(OID_EST);
		if(oidestructura.intValue() != 0){
			IObjectServer estructuraServer = sesion.getObjectServer(Estructura.class);
			Estructura estructura = (Estructura) estructuraServer.getObjectProxy(oidestructura);
			persis.setEstructura(estructura);
		}

		persis.setCategoria(db.getString(CATEGORIA));
		persis.setPuesto(db.getString(PUESTO));
		persis.setEvaluado(db.getSimpleBoolean(ES_EVALUADO));
		persis.setEvaluador(db.getSimpleBoolean(ES_EVALUADOR));
		persis.setAdministradorGerencia(db.getSimpleBoolean(ES_ADMINISTRADOR));

		Integer oidevaluador= db.getInteger(OID_EVALUADOR);
		if(oidevaluador.intValue() != 0){
			IObjectServer evaluadorServer = sesion.getObjectServer(LegajoEjer.class);
			LegajoEjer evaluador = (LegajoEjer) evaluadorServer.getObjectProxy(oidevaluador);
			persis.setEvaluador(evaluador);
		}

		Integer oidAdmin= db.getInteger(OID_ADMINISTRADOR);
		if(oidAdmin.intValue() != 0){
			IObjectServer evaluadorServer = sesion.getObjectServer(LegajoEjer.class);
			LegajoEjer admin = (LegajoEjer) evaluadorServer.getObjectProxy(oidAdmin);
			persis.setAdminGerencia(admin);
		}

		IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
		persis.setUsuario((Usuario) usuarioServer.getObjectProxy(db.getInteger(OID_USU)));

		this.notificar(persis);
	}
}