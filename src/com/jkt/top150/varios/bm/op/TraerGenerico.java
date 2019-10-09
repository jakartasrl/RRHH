package com.jkt.top150.varios.bm.op;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.xmlreader.XMLCampo;
import com.jkt.framework.xmlreader.XMLEntity;

public class TraerGenerico extends Operation implements IObserver{
   private Collection hijos;
   private HashSet tablas = new HashSet();
   
   public Integer execute(MapDS arg0) throws Exception {
      Iterator it = config.values().iterator();
      
      while(it.hasNext()){
         XMLEntity next = (XMLEntity) it.next();
         
         IObjectServer server = null;
         
         try{
            server = sesion.getObjectServer(next.getName());
         }
         catch(Exception e){
            //PORQUE PUEDE TOMAR COMO HIJO UN RESULT EN VEZ DE UN DATASET
            continue;
         }
         
         hijos = next.getHijos();
         
         String tabla = next.getTabla();
         if(tabla == null) tabla = next.getName();
         
         if(!tablas.contains(tabla))
            writer.addTabla(tabla);
         
         tablas.add(tabla);
         
         server.getObjects(new Integer(next.getValue()).intValue(), arg0, this);
      }
      
      return new Integer(0);
   }
   
   public Object getResult() {
      return null;
   }
   
   public void notify(Object arg0) throws ExceptionDS {
      writer.addFila();
      
      Iterator it = hijos.iterator();
      while(it.hasNext()){
         XMLCampo next = (XMLCampo) it.next();
         writer.addColumna(next.getCampo(), resolveMethodInvocation(next.getValue(), arg0));
      }
   }
   
   private Object resolveMethodInvocation(String aName, Object aPersistente) throws ExceptionDS{
      Object aObj = aPersistente;
      
      try{
         int indice = aName.indexOf('.');
         if(indice == -1)
            return aObj.getClass().getMethod(armarMetodo(aName), null).invoke(aObj, null);
         
         Method mt = null;
         StringTokenizer a = new StringTokenizer(aName, ".");
         while(a.hasMoreTokens()){
            String parcial = armarMetodo((String) a.nextElement());
            
            mt = aObj.getClass().getMethod(parcial, null);
            
            aObj = mt.invoke(aObj, null);
         }
      }
      catch(Exception e){
         if(e instanceof ExceptionDS)
            throw (ExceptionDS) e;
         
         throw new ExceptionDS(e, e.toString());
      }
      
      return aObj;
   }
   
   private String armarMetodo(String aName){
      if(aName.startsWith("is")) return aName;
      
      String priLetra = "" + aName.charAt(0);
      String metodo = "get" + priLetra.toUpperCase() +  aName.substring(1, aName.length());
      
      return metodo;
   }
   
}