package com.jkt.top150.varios.bm; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.ListObserver;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.framework.xmlreader.digester.QrysReader;
import com.jkt.framework.xmlreader.digester.bl.QueryHolder;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bl.EjercicioEtapas;

public class Ejercicio extends Persistente {

	public final static int SELECT_ACTUAL = 10;

	private int anio;
	private List ejerciciosEtapas;
	private boolean actual;

	private static List ejercicios;

	public boolean isActual() throws ExceptionDS{
		this.supportRefresh();
		return actual;
	}

	public void setActual(boolean aObj) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR,actual, aObj, null);
		actual = aObj;
	}

	public int getAnio() throws ExceptionDS{
		this.supportRefresh();
		return anio;
	}

	public void setAnio(int aObj) throws ExceptionDS{
		this.changePropertyValue(MAYOR_O_IGUAL_CERO,anio, aObj, "Anio");
		anio = aObj;
	}

	public EjercicioEtapas getEjercicioEtapas(Etapa aEtapa) throws ExceptionDS{
		IObjectServer server = sesion.getObjectServer(EjercicioEtapas.class);
		Map condi = new HashMap();
		condi.put("Etapa",     aEtapa);
		condi.put("Ejercicio", this);

		EjercicioEtapas etapa = (EjercicioEtapas) server.getObjectByCodigo(condi);
		if(etapa == null){
			etapa = (EjercicioEtapas) server.getNewObject();
			etapa.setEjercicio(this);
			etapa.setEtapa(aEtapa);
			etapa.setUsuario(sesion.getLogin().getUsuario());
		}

		return etapa;
	}

	public void addEtapa(EjercicioEtapas aObj) throws ExceptionDS{
		if(ejerciciosEtapas == null) ejerciciosEtapas = new ArrayList();

		aObj.setEjercicio(this);
		ejerciciosEtapas.add(aObj);
	}

	public void postSave() throws ExceptionDS{
		if(ejerciciosEtapas == null) return;

		Iterator it = ejerciciosEtapas.iterator();

		while(it.hasNext())
			((Persistente) it.next()).save();

		ejerciciosEtapas.clear();
	}

	public static void clearEjercicios(){
		Ejercicio.ejercicios = null;
	}

	public static List getEjercicios(ISesion sesion) throws ExceptionDS{
		if(ejercicios == null){
			IObjectServer server = sesion.getObjectServer(Ejercicio.class);
			ejercicios = (List) server.getObjects(IDB.SELECT_ALL, null, new ListObserver());
		}
		return ejercicios;
	}

	public static Ejercicio getEjercicioActual(ISesion sesion) throws ExceptionDS{
		IObjectServer server = sesion.getObjectServer(Ejercicio.class);
		Ejercicio ejer = (Ejercicio) server.getObjects(Ejercicio.SELECT_ACTUAL, null, new ObjectObserver());

		if(ejer == null)
			throw new ExceptionValidacion("No hay configurado un ejercicio vigente");
		return ejer;
	}
	
	public boolean existenDatosEnAnio() throws ExceptionDS{
		int count = 0;
		try{
			QrysReader reader = new QrysReader("com.jkt.top150.objetivos.da.DBLegajoEjer");
			QueryHolder holder= reader.execute();
			PreparedStatement ps = holder.getSelect(sesion, LegajoEjer.SELECT_COUNT_PARA_EJERCICIO);
			ps.setInt(1, this.getOID());
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			count = rs.getInt(1);
			rs.close();
		}
		catch(SQLException e){
			throw new ExceptionDS(e, e.toString());
		}
		
		return count > 0;
	}
}