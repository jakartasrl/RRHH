package com.jkt.top150.capacidades.bm;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jkt.framework.persistence.DBNumero;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.seguridad.bm.IUsuario;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class EvalCapacidad extends Persistente {
   
   private LegajoEjer legajo;
   private Etapa etapa;
   private Capacidad capacidad;
   private ValorCapacidad valor;
   private IUsuario usuario;
   
   public Capacidad getCapacidad() {
      return capacidad;
   }
   
   public void setCapacidad(Capacidad capacidad) throws ExceptionDS{
      this.changePropertyValue(NO_NULL, this.capacidad, capacidad, "Capacidad");
      this.capacidad = capacidad;
   }
   
   public Etapa getEtapa() {
      return etapa;
   }
   
   public void setEtapa(Etapa etapa) throws ExceptionDS{
      this.changePropertyValue(NO_NULL, this.etapa, etapa, "Etapa");
      this.etapa = etapa;
   }
   
   public LegajoEjer getLegajo() {
      return legajo;
   }
   
   public void setLegajo(LegajoEjer legajo) throws ExceptionDS{
      this.changePropertyValue(NO_NULL, this.legajo, legajo, "Legajo");
      this.legajo = legajo;
   }
   
   public IUsuario getUsuario() {
      return usuario;
   }
   
   public void setUsuario(IUsuario usuario) throws ExceptionDS{
      this.changePropertyValue(NO_NULL, this.usuario, usuario, "Usuario");
      this.usuario = usuario;
   }
   
   public ValorCapacidad getValor() {
      return valor;
   }
   
   public void setValor(ValorCapacidad valor) throws ExceptionDS{
      this.changePropertyValue(NO_NULL, this.valor, valor, "Valor Capacidad");
      this.valor = valor;
   }
   
   public Date getFecProceso() {
      return new Date(System.currentTimeMillis());
   }
   
   public void preSave() throws ExceptionDS{
      if(!this.isForUpdate()) 
         return;
      
      try{
         DBNumero db = new DBNumero(sesion);
         int numero =  db.getNumero("DBEVALCAPACIDADHIST");
         
         StringBuffer sb = new StringBuffer();
         sb.append("INSERT INTO " + sesion.getSchema() + "EVALCAPACIDADHIST (oid_eval_cap_hist,oid_eval_cap,oid_leg_eje, oid_etapa, oid_cap, oid_val_cap, oid_usu, fec_proceso)");
         sb.append("SELECT ?, oid_eval_cap,oid_leg_eje, oid_etapa, oid_cap, oid_val_cap, oid_usu, fec_proceso FROM " + sesion.getSchema() + "EVALCAPACIDAD WHERE OID_EVAL_CAP = ?");
         
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