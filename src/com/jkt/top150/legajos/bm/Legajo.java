package com.jkt.top150.legajos.bm; 

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.Aplicacion;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.seguridad.bm.Usuario;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bm.Ejercicio;

public class Legajo extends Persistente {

	public final static int SELECT_PLANEAMIENTO = 10;
	public final static int SELECT_BY_USUARIO   = 20;

	private String legajo;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombres;
	private Date nacimiento;
	private String nacionalidad;
	private String sexo;
	private String mail;
	private boolean activo = true;
	private IUsuario usuarioAlta;
	private IUsuario usuario;
	private LegajoEjer legajoEjer;

	public String getLegajo() throws ExceptionDS{
		this.supportRefresh();
		return legajo;
	}

	public LegajoEjer getLegajoEjer(Ejercicio aEjer) throws ExceptionDS{
		return this.searchByEjer(aEjer, false);
	}

	public LegajoEjer getLegajoEjer() throws ExceptionDS{
		if(legajoEjer == null)
			this.cambiarLegajoEjer();

		return legajoEjer;
	}

	public void cambiarLegajoEjer() throws ExceptionDS{
		Ejercicio ejer = ((UsuarioRRHH) sesion.getLogin().getUsuario()).getEjercicio();

		legajoEjer = this.searchByEjer(ejer, true);
	}

	private LegajoEjer searchByEjer(Ejercicio ejer, boolean validar) throws ExceptionDS{
		Map condi = new HashMap();
		condi.put("Legajo", this);
		condi.put("Ejercicio", ejer);

		IObjectServer sLegEjer = sesion.getObjectServer(LegajoEjer.class);
		LegajoEjer legE = (LegajoEjer) sLegEjer.getObjectByCodigo(condi);

		if(legE == null){
			if(validar)
				throw new ExceptionValidacion("El legajo: " + this.getApNom() + " no esta configurado para: " + ejer.getAnio());
			else{
				legE = (LegajoEjer) sLegEjer.getNewObject();
				legE.setEjercicio(ejer);
				legE.setLegajo(this);
			}
		}

		return legE;
	}

	public void setLegajo(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,legajo, aObj, "Legajo");
		legajo = aObj;
	}

	public String getApellidoPaterno() throws ExceptionDS{
		this.supportRefresh();
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,apellidoPaterno, aObj, "ApellidoPaterno");
		apellidoPaterno = aObj;
	}

	public String getApNom() throws ExceptionDS{
		return this.getApellidos() + ", " + this.getNombres();
	}

	public String getApellidos() throws ExceptionDS{
		return this.getApellidoPaterno() + " " + this.getApellidoMaterno(); 
	}

	public String getApellidoMaterno() throws ExceptionDS{
		this.supportRefresh();
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,apellidoMaterno, aObj, "ApellidoMaterno");
		apellidoMaterno = aObj;
	}

	public String getNombres() throws ExceptionDS{
		this.supportRefresh();
		return nombres;
	}

	public void setNombres(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,nombres, aObj, "Nombres");
		nombres = aObj;
	}

	public Date getNacimiento() throws ExceptionDS{
		this.supportRefresh();
		return nacimiento;
	}

	public void setNacimiento(Date aObj) throws ExceptionDS{
		nacimiento = aObj;
	}

	public String getNacionalidad() throws ExceptionDS{
		this.supportRefresh();
		return nacionalidad;
	}

	public void setNacionalidad(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,nacionalidad, aObj, "Nacionalidad");
		nacionalidad = aObj;
	}

	public String getSexo() throws ExceptionDS{
		this.supportRefresh();
		return sexo;
	}

	public void setSexo(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,sexo, aObj, "Sexo");
		sexo = aObj;
	}

	public String getMail() throws ExceptionDS{
		this.supportRefresh();
		return mail;
	}

	public void setMail(String aObj) throws ExceptionDS{
		this.changePropertyValue(EMPTY_STRING,mail, aObj, "Mail");
		mail = aObj;
	}

	public boolean isActivo() throws ExceptionDS{
		this.supportRefresh();
		return activo;
	}

	public void setInactivo() throws ExceptionDS{
		activo = false;
	}

	public IUsuario getUsuario() throws ExceptionDS{
		this.supportRefresh();
		return usuario;
	}

	public IUsuario getUsuarioAlta() throws ExceptionDS{
		this.supportRefresh();
		return usuarioAlta;
	}

	public void setUsuario(Usuario aObj) throws ExceptionDS{
		this.changePropertyValue(NO_NULL,usuario, aObj, "Usuario");
		usuario = aObj;
	}

	public Date getFecProceso() throws ExceptionDS{
		return new java.sql.Date(System.currentTimeMillis());
	}

	protected void preSave() throws ExceptionDS{
		this.usuarioAlta = sesion.getLogin().getUsuario();

		if(this.isNew()){
			IObjectServer server = sesion.getObjectServer(UsuarioRRHH.class);
			Usuario usu = (Usuario) server.getNewObject();
			usu.setCodigo("a0" + this.getLegajo().trim());
			usu.setApellido(this.getApellidoPaterno());
			usu.setNombres(this.getNombres());
			usu.setPassword(Aplicacion.getAplicacion().encode(this.getLegajo().trim()));
			usu.setUsuAlta(sesion.getLogin().getUsuario());
			usu.save();

			this.setUsuario(usu);
		}
	}

	public boolean tieneAsignadoUnEvaluador() throws ExceptionDS{
		this.supportRefresh();

		Ejercicio ejer = ((UsuarioRRHH)sesion.getLogin().getUsuario()).getEjercicio();
		LegajoEjer leg = this.getLegajoEjer(ejer);

		return leg.tieneAsignadoUnEvaluador();
	}
	
	public boolean esMismoLegajoSession() throws ExceptionDS{
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
		if(user.getLegajo() == null)
			return false;
				
		return(user.getLegajo() != null && user.getLegajo().getOID() == this.getOID());
	}
}