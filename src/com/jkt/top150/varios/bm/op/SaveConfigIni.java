package com.jkt.top150.varios.bm.op;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IPersistente;
import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.objetivos.bm.Objetivo;
import com.jkt.top150.varios.bm.Ejercicio;

public class SaveConfigIni extends Operation implements IObserver{

	private Ejercicio to;
	private Ejercicio from;
	private List objetos = new ArrayList(100);
	
	private boolean objetivos;
	
	protected void tomarParams(MapDS arg0) throws ExceptionDS {
		super.tomarParams(arg0);
		
		to   = (Ejercicio) arg0.getObject(sesion, "anio_to", Ejercicio.class);
		from = (Ejercicio) arg0.getObject(sesion, "anio_from", Ejercicio.class);

		if(to.existenDatosEnAnio())
			throw new ExceptionValidacion("El ejercicio " + to.getAnio() + " ya tiene asignado evaluados y evaluadores");

		objetivos   = arg0.containsKey("objetivos") && arg0.getSimpleBoolean("objetivos");
	}

	public Integer execute(MapDS arg0) throws Exception {
		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
		server.getObjects(LegajoEjer.SELECT_LEGAJOS_JERARQUICOS, from, this);
						
		Transaccion tran = new Transaccion(this.getConnection());
		tran.addAll(objetos);
		tran.save();
		
		writer.addTabla("FinProc");

		return OK;
	}

	public Object getResult() {
		return null;
	}

	public void notify(Object arg0) throws ExceptionDS {
		if(arg0 instanceof LegajoEjer){
			LegajoEjer lEje = (LegajoEjer) arg0;
			setNewAndAdd(lEje);

			if(objetivos)
				lEje.getObjetivos(this);

			lEje.setEjercicio(to);

			Iterator it = lEje.getEvaluados().iterator();
			while(it.hasNext())
				this.notify(it.next());
		}
		else{
			Objetivo obj = (Objetivo) arg0;
			setNewAndAdd(obj);
		}
	}
	
	private void setNewAndAdd(IPersistente pers) {
		pers.setNew();
		
		if(!objetos.contains(pers))
			objetos.add(pers);
	}
}