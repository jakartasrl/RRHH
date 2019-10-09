package com.jkt.top150.legajos.bm.op;

import java.util.Iterator;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class SaveRolLegajos extends Operation {

	public Integer execute(MapDS aParams) throws Exception {
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();

		IObjectServer server = sesion.getObjectServer(Ejercicio.class);
		Ejercicio ejer       = (Ejercicio) server.getObjectByOID(aParams.getInteger("oid_ejercicio"));

		Transaccion tran = new Transaccion(this.getConnection());

		Iterator it = ((Tabla) aParams.get("Legajo")).getRegitros().iterator();

		while(it.hasNext()){
			Registro next = (Registro) it.next();

			LegajoEjer legEje = (LegajoEjer) next.getObject(sesion, "oid_leg_ejer", LegajoEjer.class);

			boolean eraEvaluado = legEje.isEvaluado(); 
			try{
				legEje.setEvaluado(next.getBoolean("es_evaluado").booleanValue());
			}
			catch(ExceptionValidacion e){
				if(eraEvaluado) legEje.setEvaluado(false);
			}

			boolean eraEvaluador = legEje.isEvaluador();
			try{
				legEje.setEvaluador(next.getBoolean("es_evaluador").booleanValue());
			}
			catch(ExceptionValidacion e){
				if(eraEvaluador) legEje.setEvaluador(false);
			}

			boolean eraAdministrador = legEje.isAdministradorGerencia();
			try{
				legEje.setAdministradorGerencia(next.getBoolean("es_administrador").booleanValue());
			}
			catch(ExceptionValidacion e){
				if(eraAdministrador) legEje.setAdministradorGerencia(false);
			}

			Legajo leg = (Legajo) next.getObject(sesion, "oid_legajo", Legajo.class);

			legEje.setLegajo(leg);
			legEje.setEjercicio(ejer);
			legEje.setUsuario(user);

			tran.addObject(legEje);
		}

		tran.save();

		return new Integer(0);
	}
}
