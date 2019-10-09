package com.jkt.top150.objetivos.bm; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.jkt.framework.persistence.DBNumero;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;

public class Cumplimiento extends Persistente { 
   
   private Objetivo objetivo;
   private Etapa etapa;
   private double porcentaje;
   private double resultado;
   private String comentario;
   
   public Objetivo getObjetivo() throws ExceptionDS{
      this.supportRefresh();
      return objetivo;
   }
   
   public void setObjetivo(Objetivo aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,objetivo, aObj, "Objetivo");
      objetivo = aObj;
   }
   
   public Etapa getEtapa() throws ExceptionDS{
      this.supportRefresh();
      return etapa;
   }
   
   public void setEtapa(Etapa aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,etapa, aObj, "Etapa");
      etapa = aObj;
   }
   
   public double getPorcentaje() throws ExceptionDS{
      this.supportRefresh();
      return porcentaje;
   }
   
   public void setPorcentajeSinValidar(double aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,porcentaje, aObj, null);
      porcentaje = aObj;
   }

   public void setPorcentaje(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,porcentaje, aObj, "Porcentaje");
      porcentaje = aObj;
   }
   
   public String getComentario() throws ExceptionDS{
      this.supportRefresh();
      return comentario;
   }
   
   public void setComentario(String aObj) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR,comentario, aObj.trim(), null);
      comentario = aObj;
   }
   
	public double getResultado() throws ExceptionDS{
      this.supportRefresh();
      return resultado;
   }
   
   public void setResultado(double aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,resultado, aObj, "Resultado");
      resultado = aObj;
   }
   
   public IUsuario getUsuario() throws ExceptionDS{
      return sesion.getLogin().getUsuario();
   }
   
   public void preSave() throws ExceptionDS{
      if(!this.isForUpdate()) 
         return;
      
      try{
         DBNumero db = new DBNumero(sesion);
         int numero =  db.getNumero("DBCUMPLIMIENTOHIST");

         StringBuffer sb = new StringBuffer();
         sb.append("INSERT INTO " + sesion.getSchema() + "CUMPLIMIENTOHIST (oid_cump_hist, oid_cump,oid_obj,oid_etapa,porc,comen,resultado,oid_usu,fec_proceso)");
         sb.append("SELECT ?, oid_cump,oid_obj,oid_etapa,porc,comen,resultado,oid_usu,fec_proceso FROM " + sesion.getSchema() + "CUMPLIMIENTO WHERE OID_CUMP = ?");
         
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

   public Date getFecProceso() throws ExceptionDS{
      return new java.sql.Date(System.currentTimeMillis());
   }
   
   public static double getValorCumpliento(LegajoEjer ejer, Etapa etapa) throws ExceptionDS{
      double resultado = 0;
      StringBuffer sb = new StringBuffer();
      sb.append("select sum(resultado) from " + ejer.getSesion().getSchema() + "cumplimiento ");
      sb.append(" where oid_etapa = ? ");
      sb.append("   and oid_obj in (select oid_obj from " + ejer.getSesion().getSchema() + "objetivo where oid_leg_eje = ?) ");

      try{
         DBPool db = new DBPool();
         PreparedStatement ps = db.getPreparedStatement(ejer.getSesion().getConnection(), sb.toString());
         ps.setInt(1, etapa.getOID());
         ps.setInt(2, ejer.getOID());
         
         ResultSet rs = ps.executeQuery();
         
         if(rs.next())
            resultado = rs.getInt(1);
            
         rs.close();
      }
      catch(SQLException e){
         throw new ExceptionDS(e.toString());
      }

      return resultado;
   }
}