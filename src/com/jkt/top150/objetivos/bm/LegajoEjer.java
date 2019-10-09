package com.jkt.top150.objetivos.bm; 

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.patterns.writers.IWriter;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.ListObserver;
import com.jkt.top150.configuracion.bm.Estructura;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bl.LegajoEjerEtapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EjercicioEtapas;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bm.Ejercicio;

public class LegajoEjer extends Persistente {
   
   public static final int SELECT_EVALUADORES             = 10;
   public static final int SELECT_EVALUADORES_POR_ANIO    = 11;
   public static final int SELECT_EVALUADOS_POR_ANIO      = 20;
   public static final int SELECT_EVALUADOS_POR_EVALUADOR = 21;
   public static final int SELECT_COUNT_PARA_EJERCICIO    = 22;
   public static final int SELECT_LEGAJOS_JERARQUICOS     = 23;
   public static final int SELECT_ALL_BY_ANIO             = 24;
   
   public static final String FIN_PLANEAMIENTO = "<span style='color: red'   onmouseover='showEstados();'>FIN PLANEAMIENTO</span>";
   public static final String FIN_EVALUADOR    = "<span style='color: red'   onmouseover='showEstados();'>FIN EVALUADOR</span>";
   public static final String FIN_EVALUADO     = "<span style='color: red'   onmouseover='showEstados();'>FIN EVALUADO</span>";
   public static final String CERRADO          = "<span style='color: black' onmouseover='showEstados();'>CERRADO</span>";
   public static final String ACTIVO           = "<span style='color: green' onmouseover='showEstados();'>ACTIVO</span>";
   public static final String INACTIVO         = "<span style='color: blue'  onmouseover='showEstados();'>INACTIVO</span>";
   
   private Legajo legajo;
   private Ejercicio ejercicio;
   private Estructura estructura;
   private String categoria;
   private String puesto;
   private IUsuario usuario;
   private boolean isEvaluado;
   private boolean isEvaluador;
   private boolean isAdministrador;
   private LegajoEjer adminGerencia;
   private LegajoEjer evaluador;
   
   public Legajo getLegajo() throws ExceptionDS{
      this.supportRefresh();
      return legajo;
   }
   
   public void setLegajo(Legajo aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,legajo, aObj, "Legajo");
      legajo = aObj;
   }
   
   public Ejercicio getEjercicio() throws ExceptionDS{
      this.supportRefresh();
      return ejercicio;
   }
   
   public void setEjercicio(Ejercicio aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,ejercicio, aObj, "Ejercicio");
      ejercicio = aObj;
   }
      
   public Estructura getEstructura() throws ExceptionDS{
      this.supportRefresh();
      return estructura;
   }
   
   public void setEstructura(Estructura aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,estructura, aObj, "Estructura");
      estructura = aObj;
   }
      
   public String getCategoria() throws ExceptionDS{
      this.supportRefresh();
      return categoria;
   }
   
   public void setCategoria(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,categoria, aObj, "Categoria");
      categoria = aObj;
   }
   
   public String getPuesto() throws ExceptionDS{
      this.supportRefresh();
      return puesto;
   }
   
   public void setPuesto(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,puesto, aObj, "Puesto");
      puesto = aObj;
   }
   
   public boolean isEvaluado() throws ExceptionDS{
      this.supportRefresh();
      return isEvaluado;
   }
   
   public void setEvaluado(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,isEvaluado, aObj, null);
      isEvaluado = aObj;
   }
   
   public boolean isEvaluador() throws ExceptionDS{
      this.supportRefresh();
      return isEvaluador;
   }
   
   public void setEvaluador(boolean aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,isEvaluador, aObj, null);
      isEvaluador = aObj;
   }
   
   public boolean isAdministradorGerencia() throws ExceptionDS{
      this.supportRefresh();
      return isAdministrador;
   }
      
   public void setAdministradorGerencia(boolean aValue) throws ExceptionDS{
   	this.changePropertyValue(NO_VALIDAR,isAdministrador, aValue, null);
      isAdministrador = aValue;
   }
   
   public LegajoEjer getEvaluador() throws ExceptionDS{
      this.supportRefresh();
      return evaluador;
   }
   
   public void setEvaluador(LegajoEjer aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,evaluador, aObj, "Evaluador");
      evaluador = aObj;
   }
   
   public LegajoEjer getAdminGerencia() throws ExceptionDS{
		this.supportRefresh();
		return adminGerencia;
	}

	public void setAdminGerencia(LegajoEjer adminGerencia) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR, this.adminGerencia, adminGerencia, null);
		this.adminGerencia = adminGerencia;
	}

	public IUsuario getUsuario() throws ExceptionDS{
      this.supportRefresh();
      return usuario;
   }
   
   public void setUsuario(IUsuario aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,usuario, aObj, "Usuario");
      usuario = aObj;
   }
   
   public boolean tienePermisosComoEvaluador() throws ExceptionDS{
   	return this.isEvaluador() || this.isAdministradorGerencia();
   }
   
   public Date getFecProceso(){
      return new Date(System.currentTimeMillis());
   }
   
   public boolean finCargaCapacEvaluado() throws ExceptionDS{
   	//SE BUSCA EL ESTADO EN DICIEMBRE
   	EjercicioEtapas ee = EjercicioEtapas.getEjercicioEtapas(sesion, Etapa.getEtapaDiciembre(sesion));
   	int estado = this.getEjercicioEtapas(ee).getEstadoEvaluadoCapacidades();
      
      return estado == EstadosHandler.ESTADO_CERRADO && this.finCargaCapacEvaluador();
   }
   
   //****************** OBJETIVOS **************** //
   
   private boolean finCargaObjEvaluado() throws ExceptionDS{
      return this.getEjercicioEtapas().getEstadoEvaluadoCargaObj() == EstadosHandler.ESTADO_FIN_CARGA;
   }

   private boolean finCargaObjEvaluador() throws ExceptionDS{
      return this.getEjercicioEtapas().getEstadoEvaluadorCargaObj() == EstadosHandler.ESTADO_FIN_CARGA;
   }
   
   private boolean finCargaObjPlanificacion() throws ExceptionDS{
   	int estado = this.getEjercicioEtapas().getEstadoPlaneamientoCargaObj();
      return estado == EstadosHandler.ESTADO_CERRADO || estado == EstadosHandler.ESTADO_FIN_CARGA;
   }
   
   //****************** FIN OBJETIVOS **************** //
   
   
   
   //****************** CUMPLIMIENTOS **************** //
   
   private boolean finCargaCumplEvaluador() throws ExceptionDS{
   	int estado = this.getEjercicioEtapas().getEstadoEvaluadorCumplimientos();
      return estado == EstadosHandler.ESTADO_CERRADO || estado == EstadosHandler.ESTADO_FIN_CARGA;
   }
   
   private boolean finCargaCumplEvaluado() throws ExceptionDS{
   	int estado = this.getEjercicioEtapas().getEstadoEvaluadoCumplimientos();
      return estado == EstadosHandler.ESTADO_FIN_CARGA || estado == EstadosHandler.ESTADO_CERRADO;
   }
   
   public boolean finCargaCumplPlanificacion() throws ExceptionDS{
   	int estado = this.getEjercicioEtapas().getEstadoPlaneamientoCumplimientos();
      return estado == EstadosHandler.ESTADO_CERRADO || estado == EstadosHandler.ESTADO_FIN_CARGA;
   }
   
   //****************** FIN CUMPLIMIENTOS **************** //
   
   public boolean finCargaCapacEvaluador() throws ExceptionDS{
   	int estado = this.getEjercicioEtapas().getEstadoEvaluadorCapacidades();
      return estado == EstadosHandler.ESTADO_FIN_CARGA;
   }
         
   public boolean finCargaObjetivos() throws ExceptionDS{
      if(!this.getLegajo().esMismoLegajoSession() && this.getUsuarioSession().tienePermisosFinalizacion())
         return !(this.finCargaObjEvaluado() && this.finCargaObjEvaluador()) || this.finCargaObjPlanificacion();
      
      if(esMiEvaluador())
         return this.finCargaObjEvaluador();
      
      return this.finCargaObjEvaluado();
   }
      
   private UsuarioRRHH getUsuarioSession() throws ExceptionDS{
   	return (UsuarioRRHH) sesion.getLogin().getUsuario();
   }
   
   private boolean esMiEvaluador() {   	
   	try{
   		//NO SOY YO Y (ES ADMIN GERENCIA O ES MI EVALUADOR)
   		return !this.getLegajo().esMismoLegajoSession() &&
   		       (this.getUsuarioSession().getLegajo().getLegajoEjer().isAdministradorGerencia() ||
   		        this.getUsuarioSession().getLegajo().getLegajoEjer().getOID() == this.getEvaluador().getOID());
   	}
   	catch(Exception e){
   		return false;
   	}
   }
   
   public boolean finCargaCumplimientos() throws ExceptionDS{
      if(!this.getLegajo().esMismoLegajoSession() && this.getUsuarioSession().tienePermisosFinalizacion())
         return !(this.finCargaCumplEvaluado() && this.finCargaCumplEvaluador()) || this.finCargaCumplPlanificacion();
      
      if(esMiEvaluador())
      	return this.finCargaCumplEvaluador();
      
      return this.finCargaCumplEvaluado();
   }
   
   public boolean finCargaCapacidades(IWriter writer) throws ExceptionDS{
   	  //PLANEAMIENTO NO TIENE ACCESO A LAS CAPACIDADES

   	boolean result = this.finCargaCapacEvaluador();

   	if(result)
   		writer.addTabla("EvaluadoSoloPuedeFinalizar");
   	      
      return result;
   }
   
   public boolean finCargaResumen(IWriter writer) throws ExceptionDS{
   	boolean result = this.finCargaCapacEvaluado(); 
   	
   	if(result)
   		writer.addTabla("EvaluadoSoloPuedeFinalizar");
   	      
      return result;
   }
         
   public String getEstadoCargaObjStr() throws ExceptionDS{
  	   if(this.finCargaCapacEvaluado())
         return CERRADO;
         
      if(this.finCargaObjPlanificacion())
         return FIN_PLANEAMIENTO;
         
      if(this.finCargaObjEvaluador())
         return FIN_EVALUADOR;
         
      if(this.finCargaObjEvaluado())
         return FIN_EVALUADO;

      return ACTIVO;
   }
   
   public String getEstadoCumplimientosStr() throws ExceptionDS{
   	  if(this.finCargaCapacEvaluado())
         return CERRADO;

      if(this.finCargaCumplPlanificacion())
         return FIN_PLANEAMIENTO;
         
      if(this.finCargaCumplEvaluador())
         return FIN_EVALUADOR;
         
      if(this.finCargaCumplEvaluado())
         return FIN_EVALUADO;

      return ACTIVO;
   }

   public String getEstadoCapacidadesStr() throws ExceptionDS{
   	if(this.finCargaCapacEvaluado())
   		return CERRADO;

      if(this.finCargaCapacEvaluador())
         return FIN_EVALUADOR;
         
      if(this.finCargaCapacEvaluado())
         return FIN_EVALUADO;

      return ACTIVO;
   }
         
   public boolean tieneAsignadoUnEvaluador() throws ExceptionDS{
      return (!this.isNew() && this.getEvaluador() != null);      
   }
   
   public LegajoEjerEtapa getEjercicioEtapas() throws ExceptionDS{
   	return this.getEjercicioEtapas(EjercicioEtapas.getEjercicioEtapasActual(sesion));
   }
   
   public LegajoEjerEtapa getEjercicioEtapas(EjercicioEtapas aEE) throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(LegajoEjerEtapa.class);
      
      Map condi = new HashMap();
      condi.put("EjercicioEtapas", aEE);
      condi.put("LegajoEjer", this);
      
      return (LegajoEjerEtapa) server.getObjectByCodigo(condi);
   }

   public String getDescripcion() throws ExceptionDS{
      return this.getLegajo().getLegajo() + "-" + this.getLegajo().getApNom();
   }
   
   public List getObjetivos(IObserver aObs) throws ExceptionDS{
   	IObjectServer server = sesion.getObjectServer(Objetivo.class);
   	return (List) server.getObjectsForce(Objetivo.SELECT_BY_LEG_EJER, this, aObs);
   }

   public List getEvaluados() throws ExceptionDS{
  		return (List) this.getEvaluadosDirectos(new ListObserver(), new HashMap());
   }

   public List getEvaluados(Map preCond) throws ExceptionDS{
  		return (List) this.getEvaluadosDirectos(new ListObserver(), preCond);
   }
   
   public Object getEvaluadosDirectos(IObserver aObs, Map preCond) throws ExceptionDS{
   	if(!this.isEvaluador())
   		return aObs.getResult();

   	//COPIA DE MAPAS, NO BORRAR
   	Map cond = new HashMap();
   	cond.putAll(preCond);
   	cond.put("LegajoEjer", this);

   	IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
   	return server.getObjects(SELECT_EVALUADOS_POR_EVALUADOR, cond, aObs);
   }
   
   public void getEvaluadosInDirectos(IObserver aObs, Map preCond) throws ExceptionDS{
   	if(!this.isAdministradorGerencia())
   		return;
   	
   	//COPIA DE MAPAS, NO BORRAR
   	Map cond = new HashMap();
   	cond.putAll(preCond);
   	cond.put("Administrador", this);
   	
		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);
   	List list = (List) server.getObjects(SELECT_EVALUADOS_POR_EVALUADOR, cond, new ListObserver());
   	
   	Set aux = new HashSet();
   	
   	this.recorrerListYAdd(list, aux, preCond);
   	
   	Iterator it = aux.iterator();
   	while(it.hasNext())
   		aObs.notify(it.next());
   }
   
   private void recorrerListYAdd(List list, Set aux, Map preCond) throws ExceptionDS{
   	Iterator it = list.iterator();
   	while(it.hasNext()){
   		LegajoEjer next = (LegajoEjer) it.next();
   		aux.add(next);
   		
   		List evaluados = next.getEvaluados(preCond);
   		
   		this.recorrerListYAdd(evaluados, aux, preCond);
   	}
   }

   protected void postSave() throws ExceptionDS{
      if(this.isNew()){
         IObjectServer server = sesion.getObjectServer(EjercicioEtapas.class);
         List etapasEnEjer = (List) server.getObjects(IDB.SELECT_ALL, this.getEjercicio(), new ListObserver());
         
         server = sesion.getObjectServer(LegajoEjerEtapa.class);
         
         Iterator it = etapasEnEjer.iterator();
         while(it.hasNext()){
            EjercicioEtapas next = (EjercicioEtapas) it.next();
            
            LegajoEjerEtapa lee = (LegajoEjerEtapa) server.getNewObject();
            lee.setLegajoEjer(this);
            lee.setEjerEtapa(next);
            
            lee.save();
         }
      }
   }   
}