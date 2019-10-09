package com.jkt.top150.legajos.bm.op;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class SaveJefe extends Operation {

	public Integer execute(MapDS arg0) throws Exception {
		LegajoEjer evaluado = (LegajoEjer) arg0.getObject(sesion, "evaluado", LegajoEjer.class);

		boolean saveEvaluador = true;
		try{
			arg0.getInteger("evaluador");
		}
		catch(Exception e){
			saveEvaluador = false;
		}

		if(saveEvaluador){
			LegajoEjer evaluador= (LegajoEjer) arg0.getObject(sesion, "evaluador", LegajoEjer.class);
			evaluado.setEvaluador(evaluador);
			evaluado.setAdminGerencia(null);
		}
		else{
			LegajoEjer administrador = (LegajoEjer) arg0.getObject(sesion, "administrador", LegajoEjer.class);
			evaluado.setAdminGerencia(administrador);
		}

		Transaccion tran = new Transaccion(this.getConnection());
		tran.addObject(evaluado);
		tran.save();

		writer.addTabla("JustSaved");

		return OK;
	}
}
