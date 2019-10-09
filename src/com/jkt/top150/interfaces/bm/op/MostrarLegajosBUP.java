package com.jkt.top150.interfaces.bm.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.digester.Digester;

import com.jkt.framework.Aplicacion;
import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.xmlreader.XMLEntity;
import com.jkt.top150.interfaces.bl.Interfaz;
import com.jkt.top150.legajos.bm.Legajo;

public class MostrarLegajosBUP extends Operation {
   
   public Integer execute(MapDS parm1) throws java.lang.Exception {
      writer.addTabla("Legajos");
      
      XMLEntity xml     = this.tomarArchivoConfig();
      Interfaz interfaz = (Interfaz) xml.getHijo("Legajos");
      
      Class clase = Class.forName(interfaz.getJDBC());
      clase.newInstance();
      
      Connection con = Aplicacion.getAplicacion().getConnection();
      PreparedStatement ps = con.prepareStatement(interfaz.getQuery());
      ps.setString(1, parm1.getString("inicial") + "%");
      
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
         writer.addFila();
         
         String legajo = rs.getObject("BUP_LEGAJO").toString();
         writer.addColumna("BUP_LEGAJO", legajo);
         
         IObjectServer server = sesion.getObjectServer(Legajo.class);
         Legajo leg = (Legajo) server.getObjectByCodigo(legajo);
         writer.addColumna("incluido", leg != null);
         
         writer.addColumna("BUP_APELLIDO", rs.getString("BUP_APELLIDO"));
         writer.addColumna("BUP_NOMBRE", rs.getString("BUP_NOMBRE"));
         
         String mail = rs.getString("BUP_EMAIL");
         if(mail == null)
            mail = "";
         
         writer.addColumna("BUP_EMAIL", mail);
         
         String nac = rs.getString("BUP_NACIONALI");
         if(nac == null)
            nac = "ARG";
         
         writer.addColumna("BUP_NACIONALI", nac);
         
         Object date = rs.getObject("BUP_FEC_NAC");
         if(date == null)
            date = new Date();
         
         writer.addColumna("BUP_FEC_NAC", this.formatearDate(date));
      }
      
      rs.close();
      ps.close();
      Aplicacion.getAplicacion().freeConnection(con);
      
      return new Integer(0);
   }
   
   private Object formatearDate(Object aObj) throws ExceptionDS{
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      return df.format((Date)aObj);
   }
   
   protected XMLEntity tomarArchivoConfig() throws Exception{
      Digester digester = new Digester();
      digester.setValidating(false);
      
      digester.addObjectCreate("interfaces", "com.jkt.framework.xmlreader.XMLEntity");
      digester.addSetProperties("interfaces");
      
      digester.addObjectCreate("interfaces/interfaz", Interfaz.class);
      digester.addSetProperties("interfaces/interfaz");
      digester.addSetNext("interfaces/interfaz", "addHijo", Interfaz.class.getName());
      
      digester.addObjectCreate("interfaces/interfaz/modulo", "com.jkt.framework.xmlreader.XMLEntity");
      digester.addSetProperties("interfaces/interfaz/modulo");
      digester.addSetNext("interfaces/interfaz/modulo", "addHijo", "com.jkt.framework.xmlreader.XMLEntity");
      
      String path = Aplicacion.getAplicacion().getContext().getServletContext().getInitParameter("PathProceso");
      return (XMLEntity) digester.parse(Aplicacion.getAplicacion().getContext().getServletContext().getResourceAsStream(path));
   }
}