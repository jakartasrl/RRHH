package com.jkt.top150.seguridad.bm;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bm.Ejercicio;

public class UsuarioRRHH extends Usuario {

	public static final int SELECT_BY_CODIGO_UPPER = 20;

	private Legajo legajo;
	private Ejercicio ejercicio;
	private boolean rrhh;
	private boolean planificacion;
	private boolean configurador;

	public boolean tieneAsigandoUnLegajo() throws ExceptionDS {
		return this.getLegajo() != null;
	}

	public Legajo getLegajo() throws ExceptionDS{
		if(legajo == null){
			IObjectServer server = sesion.getObjectServer(Legajo.class);
			legajo = (Legajo) server.getObjects(Legajo.SELECT_BY_USUARIO, this, new ObjectObserver());
		}
		return legajo;
	}

	public void setEjercicio(Ejercicio ejer) throws ExceptionDS{
		ejercicio = ejer;
	}

	public Ejercicio getEjercicio() throws ExceptionDS{
		if(ejercicio == null)
			ejercicio = Ejercicio.getEjercicioActual(sesion);

		return ejercicio;
	}

	public boolean isPlanificacion() throws ExceptionDS{
		this.supportRefresh();
		return planificacion;
	}

	public void setPlanificacion(boolean aObj) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR, this.planificacion, aObj, null);
		this.planificacion = aObj;
	}

	public boolean isRRHH() throws ExceptionDS{
		this.supportRefresh();
		return rrhh;
	}

	public void setRRHH(boolean aObj) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR, this.rrhh, aObj, null);
		this.rrhh = aObj;
	}

	public boolean isConfigurador() throws ExceptionDS{
		this.supportRefresh();
		return configurador;
	}

	public void setConfigurador(boolean aObj) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR, this.configurador, aObj, null);
		this.configurador = aObj;
	}

	public void preSave(){
		this.setUsuAlta(sesion.getLogin().getUsuario());
	}

	public void getEvaluados(IObserver aObs, MapDS aParams) throws ExceptionDS{
		if(this.tienePermisosFinalizacion()){
			aParams.put("Ejercicio", this.getEjercicio());

			IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
			server.getObjects(LegajoEjer.SELECT_EVALUADOS_POR_ANIO, aParams, aObs);
		}
		else{
			LegajoEjer legEjer = this.getLegajo().getLegajoEjer();
			if(legEjer.isEvaluado())
				aObs.notify(legEjer);

			legEjer.getEvaluadosDirectos(aObs, aParams);
			legEjer.getEvaluadosInDirectos(aObs, aParams);
		}
	}

	public boolean tieneAccesoAlResumen() throws ExceptionDS{
		boolean accesoEvaluador = this.tieneAsigandoUnLegajo() && legajo.getLegajoEjer().tienePermisosComoEvaluador();

		return accesoEvaluador || this.tienePermisosFinalizacion(); 
	}
	
	public boolean tienePermisosFinalizacion() throws ExceptionDS{
		return this.isRRHH() || this.isPlanificacion();
	}

	public String getFuncion(Legajo legajo) throws ExceptionDS{
		LegajoEjer lEjer = null;
		UsuarioRRHH user = null;

		if(legajo != null){
			lEjer = legajo.getLegajoEjer();
			user  = (UsuarioRRHH) legajo.getUsuario();
		}
		else {
			if(this.getLegajo() != null)
				lEjer = this.getLegajo().getLegajoEjer();

			user  = this;
		}

		String funcion = "";

		if(lEjer != null && lEjer.isEvaluado())
			funcion = "Evaluado";

		if(lEjer != null && lEjer.isEvaluador())
			funcion += (funcion.length() == 0) ? "Evaluador" : "/Evaluador";

		if(lEjer != null && lEjer.isAdministradorGerencia())
			funcion += (funcion.length() == 0) ? "Admin. Gerencia" : "/Admin. Gerencia";

		if(user.isPlanificacion())
			funcion += (funcion.length() == 0) ? "Planeamiento Estrategico" : "/Planeamiento Estrategico";

		if(user.isRRHH())
			funcion += (funcion.length() == 0) ? "RRHH" : "/RRHH";

		return funcion;
	}
}