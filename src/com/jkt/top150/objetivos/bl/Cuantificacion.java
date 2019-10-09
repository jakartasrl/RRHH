package com.jkt.top150.objetivos.bl; 

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.jkt.framework.persistence.DBNumero;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bm.Cuantificador;
import com.jkt.top150.objetivos.bm.Objetivo;

public class Cuantificacion extends Persistente { 
   
   private Objetivo objetivo;
   private Cuantificador cuantificador;
   private String cuantificacion;
   private IUsuario usuario;
   private Date fecProceso;
   
   public Objetivo getObjetivo() throws ExceptionDS{
      this.supportRefresh();
      return objetivo;
   }
   
   public void setObjetivo(Objetivo aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,objetivo, aObj, "Objetivo");
      objetivo = aObj;
   }
   
   public Cuantificador getCuantificador() throws ExceptionDS{
      this.supportRefresh();
      return cuantificador;
   }
   
   public void setCuantificador(Cuantificador aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,cuantificador, aObj, "Cuantificador");
      cuantificador = aObj;
   }
   
   public String getCuantificacion() throws ExceptionDS{
      this.supportRefresh();
      return cuantificacion;
   }
   
   public void setCuantificacion(String aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,cuantificacion, aObj, null);
      cuantificacion = aObj;
   }
   
   public IUsuario getUsuario() throws ExceptionDS{
      this.supportRefresh();
      return usuario;
   }
   
   public void setUsuario(IUsuario aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,usuario, aObj, "Usuario");
      usuario = aObj;
   }
   
   public Date getFecProceso() throws ExceptionDS{
      return new java.sql.Date(System.currentTimeMillis());
   }
   
   public void preSave() throws ExceptionDS{
      if(this.isNew()){
         this.setUsuario(objetivo.getUsuario());
         return;
      }
      
      if(!this.isForUpdate()) 
         return;
      
      try{
         DBNumero db = new DBNumero(sesion);
         int numero =  db.getNumero("DBCUANTIFICACIONHIST");

         StringBuffer sb = new StringBuffer();
         sb.append("INSERT INTO " + sesion.getSchema() + "CUANTIFICACIONHIST (oid_cuan_hist,oid_cuan,oid_obj,oid_cuantif,cuantificacion,oid_usu,fec_proceso, orden)");
         sb.append("SELECT ?, oid_cuan,oid_obj,oid_cuantif,cuantificacion,oid_usu,fec_proceso, orden FROM " + sesion.getSchema() + "CUANTIFICACION WHERE OID_CUAN = ?");
         
         DBPool pool = new DBPool();

         PreparedStatement ps = pool.getPreparedStatement(sesion.getConnection(), sb.toString());
         ps.setInt(1, numero);
         ps.setInt(2, this.getOID());
         ps.executeUpdate();
      }
      catch(SQLException e){
         throw new ExceptionDS(e.toString());
      }
   }
}