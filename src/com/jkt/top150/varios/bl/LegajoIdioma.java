package com.jkt.top150.varios.bl; 

import com.jkt.framework.persistence.Persistente;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.top150.objetivos.bm.LegajoEjer;
import com.jkt.top150.varios.bm.NivelIdioma;

public class LegajoIdioma extends Persistente { 
   
   private LegajoEjer legajoEjer;
   private int orden;
   private String idioma;
   private NivelIdioma nivelIdioma;
   
   public LegajoEjer getLegajoEjer() throws ExceptionDS{
      this.supportRefresh();
      return legajoEjer;
   }
   
   public void setLegajoEjer(LegajoEjer aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,legajoEjer, aObj, "LegajoEjer");
      legajoEjer = aObj;
   }
   
   public int getOrden() throws ExceptionDS{
      this.supportRefresh();
      return orden;
   }
   
   public void setOrden(int aObj) throws ExceptionDS{
      this.changePropertyValue(MAYOR_O_IGUAL_CERO,orden, aObj, "Orden");
      orden = aObj;
   }
   
   public String getIdioma() throws ExceptionDS{
      this.supportRefresh();
      return idioma;
   }
   
   public void setIdioma(String aObj) throws ExceptionDS{
      this.changePropertyValue(EMPTY_STRING,idioma, aObj, "Idioma");
      idioma = aObj;
   }
   
   public NivelIdioma getNivelIdioma() throws ExceptionDS{
      this.supportRefresh();
      return nivelIdioma;
   }
   
   public void setNivelIdioma(NivelIdioma aObj) throws ExceptionDS{
      this.changePropertyValue(NO_NULL,nivelIdioma, aObj, "NivelIdioma");
      nivelIdioma = aObj;
   }
}