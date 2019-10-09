package com.jkt.top150.objetivos.bm.op;

import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EvaluadorHandler;

public class EvaluadorWriter extends Operation {

	public Integer execute(MapDS arg0) throws Exception {
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();

		EvaluadorHandler handler = new EvaluadorHandler();
		handler.writeEvaluadores(writer, user);

		return OK;
	}
}
