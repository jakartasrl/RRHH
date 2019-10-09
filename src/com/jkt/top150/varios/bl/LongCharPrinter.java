package com.jkt.top150.varios.bl;


public class LongCharPrinter {
   
   public String print(String aStr){
      return this.print(aStr, 50);
   }
   
   public String print(String aStr, int  renglonSize){
      StringBuffer sb = new StringBuffer();
      
      int begin = 0;
      int end   = renglonSize;
      
      String resto = aStr;
      while(resto != null && resto.length() > renglonSize){
         sb.append(aStr.substring(begin, end) + "<br>");
         
         resto = aStr.substring(end);
         
         begin = end;
         end   = end + renglonSize;
      }
      sb.append(resto);
      
      return sb.toString();
   }   
}
