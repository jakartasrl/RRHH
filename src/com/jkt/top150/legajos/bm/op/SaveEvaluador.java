package com.jkt.top150.legajos.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class SaveEvaluador extends Operation {
	
	public Integer execute(MapDS aParams) throws Exception {
		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
		
		LegajoEjer legEjer = null;
		
		try{
			legEjer = user.getLegajo().getLegajoEjer();
		}
		catch(ExceptionValidacion e){
			legEjer = (LegajoEjer) server.getNewObject();
		}
		
		legEjer.setLegajo(user.getLegajo());
		legEjer.setEjercicio(user.getEjercicio());
		legEjer.setUsuario(user);
		legEjer.setEvaluador((LegajoEjer) server.getObjectByOID(aParams.getInteger("oidEvaluador")));
		
		Transaccion tran = new Transaccion(this.getConnection());
		tran.addObject(legEjer);
		tran.save();
		
		writer.addTabla("ReloadEncabezado");
		
		return new Integer(0);
	}
}
