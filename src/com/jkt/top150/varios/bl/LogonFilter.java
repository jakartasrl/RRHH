package com.jkt.top150.varios.bl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogonFilter implements Filter {

   public void destroy() {
   }
   
   /**
    * @see javax.servlet.Filter#void (javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
    */
   public void doFilter(
         ServletRequest request,
         ServletResponse response,
         FilterChain chain)
   throws ServletException, IOException {
      
      HttpServletRequest hg = (HttpServletRequest) request;
      HttpSession s = hg.getSession(false);
      s.setAttribute("j_username", request.getParameter("j_username"));
      
      chain.doFilter(request, response);
   }
   
   /**
    * Method init.
    * @param config
    * @throws javax.servlet.ServletException
    */
   public void init(FilterConfig config) throws ServletException {
   }
}