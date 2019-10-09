package com.jkt.top150.varios.bm.op;

import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bm.Ejercicio;

public class SaveEjercicios extends Operation {

	public Integer execute(MapDS aParams) throws Exception {
		Transaccion tran = new Transaccion(this.getConnection());

		IObjectServer server = sesion.getObjectServer(Etapa.class);
		List etapas = (List) server.getObjects(IDB.SELECT_ALL, null, new ListObserver());

		Iterator it = ((Tabla) aParams.get("Ejercicios")).getRegitros().iterator();

		while(it.hasNext()){
			Registro next = (Registro) it.next();

			Ejercicio ejer = (Ejercicio) next.getObject(sesion, "oid", Ejercicio.class);
			ejer.setAnio(next.getInteger("anio").intValue());
			ejer.setActual(false);

			if(next.containsKey("actual") && next.getBoolean("actual").booleanValue())
				ejer.setActual(true);

			Iterator it2 = etapas.iterator();
			while(it2.hasNext()){
				Etapa etapa = (Etapa) it2.next();

				EjercicioEtapas ejEta = ejer.getEjercicioEtapas(etapa);

				try{
					ejEta.setEstado(next.getString("estado_" + etapa.getOID()));
				}
				catch(Exception e){
					ejEta.setEstado(EjercicioEtapas.CERRADA);
				}

				ejer.addEtapa(ejEta);
			}

			tran.addObject(ejer);
		}

		tran.save();

		Ejercicio.clearEjercicios();
		writer.addTabla("JustSaved");//MARCA PARA REFRESCAR EL ENCABEZADO

		return new Integer(0);
	}
}