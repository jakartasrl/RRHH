package com.jkt.top150.objetivos.bm; 

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.DBNumero;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.request.ISesion;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ObjectObserver;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;

public class CumplimientoGlobal extends Persistente { 
   
   private LegajoEjer legajo;
   private Etapa etapa;
   private ValCumpGlobal valorCumplimiento;
   private IUsuario usuario;
   private String comentario;
   
   public LegajoEjer getLegajo() throws ExceptionDS{
      this.supportRefresh();
      return legajo;
   }
   
   public void setLegajo(LegajoEjer aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,legajo, aObj, "Legajo");
      legajo = aObj;
   }
   
   public Etapa getEtapa() throws ExceptionDS{
      this.supportRefresh();
      return etapa;
   }
   
   public void setEtapa(Etapa aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,etapa, aObj, "Etapa");
      etapa = aObj;
   }
   
   public ValCumpGlobal getValorCumplimiento() throws ExceptionDS{
      this.supportRefresh();
      return valorCumplimiento;
   }
   
   public void setValorCumplimiento(ValCumpGlobal aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,valorCumplimiento, aObj, "ValorCumplimiento");
      valorCumplimiento = aObj;
   }
      
   public IUsuario getUsuario() throws ExceptionDS{
      this.supportRefresh();
      return usuario;
   }
   
   public void setUsuario(IUsuario aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,usuario, aObj, "Usuario");
      usuario = aObj;
   }
   
   public Date getFecProceso() {
      return new java.sql.Date(System.currentTimeMillis());
   }
   
   public String getComentario() {
      return comentario;
   }
   
   public void setComentario(String aComent) throws ExceptionDS{
      this.changePropertyValue(NO_VALIDAR, comentario, aComent.trim(), null);
      comentario = aComent;
   }
   
   public void preSave() throws ExceptionDS{
      if(!this.isForUpdate()) 
         return;
      
      try{
         DBNumero db = new DBNumero(sesion);
         int numero =  db.getNumero("DBCUMPLIMIENTOGLOBALHIST");

         StringBuffer sb = new StringBuffer();
         sb.append("INSERT INTO " + sesion.getSchema() + "CUMPGLOBALHIST (oid_cump_glob_hist, oid_cump_glob,oid_leg_eje,oid_etapa,oid_val_cump,valor,oid_usu,fec_proceso)");
         sb.append("SELECT ?, oid_cump_glob,oid_leg_eje,oid_etapa,oid_val_cump,valor,oid_usu,fec_proceso FROM " + sesion.getSchema() + "CUMPGLOBAL WHERE OID_CUMP_GLOB = ?");
         
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
   
   public static CumplimientoGlobal getCumplimientoGlobal(LegajoEjer legajo, ISesion sesion) throws ExceptionDS{
      Etapa etapa = Etapa.getEtapaActual(sesion);
      
      IObjectServer server = sesion.getObjectServer(CumplimientoGlobal.class);
      Map condi = new HashMap();
      condi.put("LegajoEjer", legajo);
      condi.put("Etapa",  etapa);
      
      CumplimientoGlobal cumplimiento = (CumplimientoGlobal) server.getObjectsForce(IDB.SELECT_BY_COD, condi, new ObjectObserver());
      if(cumplimiento == null){
         cumplimiento = (CumplimientoGlobal) server.getNewObject();
         cumplimiento.setLegajo(legajo);
         cumplimiento.setEtapa(etapa);
         cumplimiento.setUsuario(sesion.getLogin().getUsuario());
      }
      
      return cumplimiento;
   }
}