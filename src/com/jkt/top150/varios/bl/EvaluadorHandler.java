package com.jkt.top150.varios.bl;

import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.patterns.writers.IWriter;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ListObserver;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;

public class EvaluadorHandler {

	public void writeEvaluadores(IWriter writer, UsuarioRRHH user) throws ExceptionDS{
		writer.addTabla("Evaluadores");

		if(user.isPlanificacion() || user.isRRHH()){
			IObjectServer server = user.getSesion().getObjectServer(LegajoEjer.class);
			List evaluadores     = (List) server.getObjects(LegajoEjer.SELECT_EVALUADORES_POR_ANIO, user.getEjercicio(), new ListObserver());
			Iterator it          = evaluadores.iterator();
			while(it.hasNext()){
				LegajoEjer ejer = (LegajoEjer) it.next();
				this.addEvaluador(writer, ejer);
			}
		}
		else{
			try{
				if(user.getLegajo().getLegajoEjer().isEvaluador())
					this.addEvaluador(writer, user.getLegajo().getLegajoEjer());
			}
			catch(Exception e){}      	
		}
	}

	private void addEvaluador(IWriter writer, LegajoEjer ejer) throws ExceptionDS{
		writer.addFila();
		writer.addColumna("oid_eval", ejer.getOID());
		writer.addColumna("nombre",   ejer.getLegajo().getApNom());
	}
}