package com.jkt.top150.varios.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class IterateTag extends TagSupport {

	private Collection coleccion;
	private Iterator iterator;

	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}

	public int doStartTag() throws JspTagException { 
		if (coleccion == null)	 
			coleccion = new ArrayList(); 

		iterator = coleccion.iterator(); 
		if (iterator.hasNext())	{ 
			pageContext.setAttribute("registro", iterator.next()); 
			return EVAL_BODY_INCLUDE; 
		} 

		return SKIP_BODY; 
	} 

	public int doAfterBody() {
		if (iterator.hasNext())	{ 
			pageContext.setAttribute("registro", iterator.next()); 
			return EVAL_BODY_AGAIN; 
		} 

		return SKIP_BODY; 
	}
}