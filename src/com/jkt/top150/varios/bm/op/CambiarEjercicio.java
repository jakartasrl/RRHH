package com.jkt.top150.varios.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class CambiarEjercicio extends Operation {
	
	public Integer execute(MapDS aParams) throws Exception {
		IObjectServer server = sesion.getObjectServer(Ejercicio.class);
		
		Ejercicio ejer = (Ejercicio) server.getObjectByOID(aParams.getInteger("oid_ejer"));
		
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
		
		try{
			if(!user.isPlanificacion()){
				user.setEjercicio(ejer);
				user.getLegajo().cambiarLegajoEjer();
			}
		}
		catch(ExceptionDS e){
			user.setEjercicio(Ejercicio.getEjercicioActual(sesion));
			user.getLegajo().cambiarLegajoEjer();
		}
		
		writer.addTabla("ReloadEncabezado");
		
		return aParams.getInteger("nextView");
	}
}
