package com.jkt.top150.objetivos.bm; 

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.DBNumero;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.IPersistente;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ListObserver;
import com.jkt.top150.objetivos.bl.Cuantificacion;

public class Objetivo extends Persistente {
   
   public static final int SELECT_BY_LEG_EJER = 20;
   
   private LegajoEjer legajo;
   private GrupoObjetivo grupo;
   private int nroObjetivo;
   private double ponderacion;
   private IUsuario usuarioAlta;
   private List cuantificaciones;
   
   private String fuente               = " ";
   private String descripcion          = " ";
   private String cuantificacion       = " ";
   private String aspectosCualitativos = " ";
   private String cumplimientoReal     = " "; 
   
   public LegajoEjer getLegajo() throws ExceptionDS{
      this.supportRefresh();
      return legajo;
   }
   
   public void setLegajo(LegajoEjer aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,legajo, aObj, "Legajo");
      legajo = aObj;
   }
   
   public GrupoObjetivo getGrupo() throws ExceptionDS{
      this.supportRefresh();
      return grupo;
   }
   
   public void setGrupo(GrupoObjetivo aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,grupo, aObj, "Grupo");
      grupo = aObj;
   }
   
   public int getNroObjetivo() throws ExceptionDS{
      this.supportRefresh();
      return nroObjetivo;
   }
   
   public void setNroObjetivo(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,nroObjetivo, aObj, "NroObjetivo");
      nroObjetivo = aObj;
   }
   
   public String getDescripcion() throws ExceptionDS{
      this.supportRefresh();
      return descripcion;
   }
   
   public void setDescripcion(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,descripcion, aObj, "Descripcion");
      descripcion = aObj;
   }
   
   public String getCuantificacion() throws ExceptionDS{
      this.supportRefresh();
      return cuantificacion;
   }
   
   public void setCuantificacion(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,cuantificacion, aObj, "Cuantificacion");
      cuantificacion = aObj;
   }
   
   public double getPonderacion() throws ExceptionDS{
      this.supportRefresh();
      return ponderacion;
   }
   
   public void setPonderacion(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,ponderacion, aObj, "Ponderacion");
      ponderacion = aObj;
   }
   
   public String getFuente() throws ExceptionDS{
      this.supportRefresh();
      return fuente;
   }
   
   public void setFuente(String aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,fuente, aObj, null);
      fuente = aObj;
   }
   
   public String getCumplimientoReal() throws ExceptionDS{
		this.supportRefresh();
		return cumplimientoReal;
	}

	public void setCumplimientoReal(String cumplimientoReal) throws ExceptionDS{
		this.changePropertyValue(NO_VALIDAR, this.cumplimientoReal, cumplimientoReal,null);
		this.cumplimientoReal = cumplimientoReal;
	}
   
   public String getAspectosCualitativos() throws ExceptionDS{
      this.supportRefresh();
      return aspectosCualitativos;
   }
   
   public void setAspectosCualitativos(String aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,aspectosCualitativos, aObj, null);
      aspectosCualitativos = aObj;
   }
   
   public IUsuario getUsuario() throws ExceptionDS{
      return sesion.getLogin().getUsuario();
   }
   
   public Date getFecProceso() throws ExceptionDS{
      return new java.sql.Date(System.currentTimeMillis());
   }
   
   public IUsuario getUsuarioAlta() throws ExceptionDS{
      return usuarioAlta;
   }
   
   public void setUsuarioAlta(IUsuario aUser) {
      this.usuarioAlta = aUser;
   }
   
   public List getCuantificaciones() throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(Cuantificacion.class);
      return (List) server.getObjects(IDB.SELECT_ALL, this, new ListObserver());
   }
   
   public List getCumplimientos() throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(Cumplimiento.class);
      return (List) server.getObjects(IDB.SELECT_ALL, this, new ListObserver());
   }
   
   public Cuantificacion getCuantificacion(Cuantificador aCuanti) throws ExceptionDS{
      IObjectServer server = sesion.getObjectServer(Cuantificacion.class);
      Map condi = new HashMap();
      condi.put("Objetivo", this);
      condi.put("Cuantificador", aCuanti);
      
      return (Cuantificacion) server.getObjectByCodigo(condi);
   }
   
   public void addCuantificacion(Cuantificacion aCuanti) throws ExceptionDS{
      if(cuantificaciones == null) cuantificaciones = new ArrayList();
      
      aCuanti.setObjetivo(this);
      cuantificaciones.add(aCuanti);
   }
   
   public void preSave() throws ExceptionDS{
      this.setUsuarioAlta(sesion.getLogin().getUsuario());
      
      try{
         if(this.isNew()){
            DBNumero db = new DBNumero(sesion);
            this.setNroObjetivo(db.getNumero("NUMERADOR_OBJ"));
         }
         
         DBPool pool = new DBPool();
         
         if(this.isForDelete()){
            StringBuffer sb = new StringBuffer();
            //HISTORICO OBJETIVO
            sb.append("delete from " + sesion.getSchema() + "OBJETIVOHIST where oid_obj = ? ");

            PreparedStatement ps = pool.getPreparedStatement(sesion.getConnection(), sb.toString());
            ps.setInt(1, this.getOID());
            ps.executeUpdate();
            
            //HISTORICO CUANTIFICACION
            sb = new StringBuffer();
            sb.append("delete from " + sesion.getSchema() + "CUANTIFICACIONHIST where oid_cuan in ");
            sb.append("(select oid_cuan from " + sesion.getSchema() + "CUANTIFICACION where oid_obj = ? )");
            
            ps = pool.getPreparedStatement(sesion.getConnection(), sb.toString());
            ps.setInt(1, this.getOID());
            ps.executeUpdate();
            
            //HISTORICO CUMPLIMIENTO
            sb = new StringBuffer();
            sb.append("delete from " + sesion.getSchema() + "CUMPLIMIENTOHIST where oid_cump in ");
            sb.append("(select oid_cump from " + sesion.getSchema() + "CUMPLIMIENTO where oid_obj = ? )");

            ps.setInt(1, this.getOID());
            ps.executeUpdate();

            //CUANTIFICACION
            sb = new StringBuffer();
            sb.append("delete from " + sesion.getSchema() + "CUANTIFICACION where oid_obj = ? ");
            
            ps = pool.getPreparedStatement(sesion.getConnection(), sb.toString());
            ps.setInt(1, this.getOID());
            ps.executeUpdate();
            
            //CUMPLIMIENTO
            sb = new StringBuffer();
            sb.append("delete from " + sesion.getSchema() + "CUMPLIMIENTO where oid_obj = ? ");

            ps.setInt(1, this.getOID());
            ps.executeUpdate();
         }
         
         if(!this.isForUpdate()) 
            return;
         
         DBNumero db = new DBNumero(sesion);
         int numero =  db.getNumero("DBCUANTIFICACIONHIST");
         
         StringBuffer sb = new StringBuffer();
         sb.append("INSERT INTO " + sesion.getSchema() + "OBJETIVOHIST (oid_obj_hist, oid_obj,oid_leg_eje,oid_grupo,nro_obj,descripcion,cuantificacion,ponderacion,fuente,aspectos_cual,oid_usu,fec_proceso)");
         sb.append("SELECT ?, oid_obj,oid_leg_eje,oid_grupo,nro_obj,descripcion,cuantificacion,ponderacion,fuente,aspectos_cual,oid_usu,fec_proceso FROM " + sesion.getSchema() + "OBJETIVO WHERE OID_OBJ = ?");
         
         PreparedStatement ps = pool.getPreparedStatement(sesion.getConnection(), sb.toString());
         ps.setInt(1, numero);
         ps.setInt(2, this.getOID());
         ps.executeUpdate();
      }
      catch(SQLException e){
         throw new ExceptionDS(e.toString());
      }
   }
   
   public void postSave() throws ExceptionDS{
      if(cuantificaciones == null)
         return;
      
      Iterator it = cuantificaciones.iterator();
      while(it.hasNext()){
         IPersistente pers = (IPersistente) it.next();
         pers.save();
      }
      
      cuantificaciones.clear();
   }
}