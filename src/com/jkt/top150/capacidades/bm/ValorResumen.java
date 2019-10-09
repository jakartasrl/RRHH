package com.jkt.top150.capacidades.bm; 

import com.jkt.common.bm.Descriptible;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class ValorResumen extends Descriptible {

	private static final int SELECT_BY_EVAL_EMPRESA = 10;

	private int orden;
	private double valorNumerico;
	private String descExtendida;

	public int getOrden() throws ExceptionDS{
		this.supportRefresh();
		return orden;
	}

	public void setOrden(int aObj) throws ExceptionDS{
		this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
		orden = aObj;
	}

	public double getValorNumerico() throws ExceptionDS{
		this.supportRefresh();
		return valorNumerico;
	}

	public void setValorNumerico(double aObj) throws ExceptionDS{
		this.changePropertyValue(MAYOR_O_IGUAL_CERO,valorNumerico, aObj, "ValorNumerico");
		valorNumerico = aObj;
	}

	public String getDescExtendida() throws ExceptionDS{
		this.supportRefresh();
		return descExtendida;
	}

	public void setDescExtendida(String aObj) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR,descExtendida, aObj, null);
		descExtendida = aObj;
	}

	public static ValorResumen getResumenGlobalEmpresa(LegajoEjer legajoEjer) throws ExceptionDS {
		IObjectServer server = legajoEjer.getSesion().getObjectServer(ValorResumen.class);
		return (ValorResumen) server.getObjectsForce(SELECT_BY_EVAL_EMPRESA, legajoEjer.getOIDInteger(), new ObjectObserver());
	}
}