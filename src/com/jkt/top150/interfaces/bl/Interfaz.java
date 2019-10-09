package com.jkt.top150.interfaces.bl;

import com.jkt.framework.xmlreader.XMLEntity;

/**
 * @author Leo
 *
 * Clase hecha por Usuario el dia 27-dic-2004 
 * para el proyecto Puma
 */
public class Interfaz extends XMLEntity {
   private String operacion;
   private String jdbc;
   private String connection;
   private String query;
   private String nomTablaXML;
   
   public String getOperacion() {
      return operacion;
   }
   
   public void setOperacion(String operacion) {
      this.operacion = operacion;
   }
   
   public String getConnection() {
      return connection;
   }
   
   public void setConnection(String connection) {
      this.connection = connection;
   }
   
   public String getJDBC() {
      return jdbc;
   }
   
   public void setJDBC(String jdbc) {
      this.jdbc = jdbc;
   }
   
   public String getQuery() {
      return query;
   }
   
   public void setQuery(String query) {
      this.query = query;
   }
   
   public void setNomTablaXML(String aStr) {
      this.nomTablaXML = aStr;
   }
   
   public String getNomTablaXML() {
      if(nomTablaXML == null)
         return this.getName();
      
      return nomTablaXML;
   }   
}