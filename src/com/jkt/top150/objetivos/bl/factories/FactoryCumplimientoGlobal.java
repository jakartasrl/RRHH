package com.jkt.top150.objetivos.bl.factories; 

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Factory;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class FactoryCumplimientoGlobal extends Factory { 
	
	private static final String OID_CUMP_GLOB = "OID_CUMP_GLOB";
	private static final String OID_LEG_EJE = "OID_LEG_EJE";
	private static final String OID_ETAPA = "OID_ETAPA";
	private static final String OID_VAL_CUMP = "OID_VAL_CUMP";
	private static final String VALOR       = "VALOR";
	private static final String OID_USU     = "OID_USU";
	private static final String COMENTARIO  = "COMENTARIO";
//	private static final String FEC_PROCESO = "FEC_PROCESO";
	
	public void newRecordNotify(IRecord db) throws com.jkt.framework.util.ExceptionDS{
		CumplimientoGlobal persis = (CumplimientoGlobal) server.getObjectForView(db.getInteger(OID_CUMP_GLOB));
		
		Integer oidlegajo= db.getInteger(OID_LEG_EJE);
		IObjectServer legajoServer = sesion.getObjectServer(LegajoEjer.class);
		LegajoEjer legajo = (LegajoEjer) legajoServer.getObjectProxy(oidlegajo);
		persis.setLegajo(legajo);
		
		Integer oidetapa= db.getInteger(OID_ETAPA);
		IObjectServer etapaServer = sesion.getObjectServer(Etapa.class);
		Etapa etapa = (Etapa) etapaServer.getObjectProxy(oidetapa);
		persis.setEtapa(etapa);
		
		Integer oidvalorCumplimiento= db.getInteger(OID_VAL_CUMP);
		if(oidvalorCumplimiento.intValue() != 0){
			IObjectServer valorCumplimientoServer = sesion.getObjectServer(ValCumpGlobal.class);
			ValCumpGlobal valorCumplimiento = (ValCumpGlobal) valorCumplimientoServer.getObjectProxy(oidvalorCumplimiento);
			persis.setValorCumplimiento(valorCumplimiento);
		}
		
		Integer oidusuario= db.getInteger(OID_USU);
		if(oidusuario.intValue() != 0){
			IObjectServer usuarioServer = sesion.getObjectServer(UsuarioRRHH.class);
			Usuario usuario = (Usuario) usuarioServer.getObjectProxy(oidusuario);
			persis.setUsuario(usuario);
		}
		
		persis.setComentario(db.getString(COMENTARIO));
		
		this.notificar(persis);
	}
}