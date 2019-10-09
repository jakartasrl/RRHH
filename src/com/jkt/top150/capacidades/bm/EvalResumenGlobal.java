package com.jkt.top150.capacidades.bm;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class EvalResumenGlobal extends Persistente {

	private LegajoEjer legajo;
	private Etapa etapa;
	private ValorResumen valor;
	private IUsuario usuario;

	public LegajoEjer getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEjer legajo) throws ExceptionDS{
		this.changePropertyValue(NO_NULL, this.legajo, legajo, "Legajo");
		this.legajo = legajo;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) throws ExceptionDS{
		this.changePropertyValue(NO_NULL, this.etapa, etapa, "Etapa");
		this.etapa = etapa;
	}

	public ValorResumen getValor() {
		return valor;
	}

	public void setValor(ValorResumen valor) throws ExceptionDS{
		this.changePropertyValue(NO_NULL, this.valor, valor, "Valor Resumen");
		this.valor = valor;
	}

	public IUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(IUsuario usuario) throws ExceptionDS{
		this.changePropertyValue(NO_NULL, this.usuario, usuario, "Usuario");
		this.usuario = usuario;
	}

	public Date getFecProceso() {
		return new Date(System.currentTimeMillis());
	}

	public static EvalResumenGlobal getResumenGlobal(LegajoEjer legajoEjer) throws ExceptionDS {
		Etapa etapa = Etapa.getEtapaActual(legajoEjer.getSesion());
		IObjectServer server = legajoEjer.getSesion().getObjectServer(EvalResumenGlobal.class);
		Map condi = new HashMap();
		condi.put("Legajo", legajoEjer);
		condi.put("Etapa",  etapa);

		EvalResumenGlobal capacidad = (EvalResumenGlobal) server.getObjectsForce(IDB.SELECT_BY_COD, condi, new ObjectObserver());
		if(capacidad == null){
			capacidad = (EvalResumenGlobal) server.getNewObject();
			capacidad.setLegajo(legajoEjer);
			capacidad.setEtapa(etapa);
			capacidad.setUsuario(legajoEjer.getSesion().getLogin().getUsuario());
		}

		return capacidad;
	}
}