<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Top 180</title>

<meta http-equiv="expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<link href="<%=request.getContextPath()%>/estilos/estilos.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/estilos/funciones.js"></script>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.jkt.top150.varios.bm.Ejercicio"%>
<%@ page import="com.jkt.framework.request.ISesion"%>

<% String pathImg      = request.getContextPath() + "/img"; 
   ISesion sesionJKT   = (ISesion) request.getSession().getAttribute("SESION");
   boolean fin         = request.getAttribute("FinProc") != null;
%>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="form1" action="<%=request.getContextPath()%>/FrontServlet" method="POST">
<input type=hidden name=op value=SaveConfigIni>

<jsp:include 
     page="/jsp/util/tablaError.jsp"
     flush="true"
/>

<script type="text/javascript">
if(<%= fin %>){
   alert('Proceso Finalizado');
}
</script>

<div align=center>
  <table width="300"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Ejercicios">
    <tr>
      <th align="center" colspan="2">Copiar configuracion del periodo</th>
    </tr>
    <tr>
      <td>Periodo destino:</td>
      <td align="center">
          <select name="anio_to">
             <option value="<%= request.getParameter("oid_eje") %>"><%= request.getParameter("anio_to") %></option>
          </select>
      </td>
    </tr>
    <tr>
      <td>Periodo base:</td>
      <td align="center">
      
          <select name="anio_from">
          
       <% Iterator it = Ejercicio.getEjercicios(sesionJKT).iterator();
          while(it.hasNext()){
            Ejercicio next = (Ejercicio) it.next(); %>  

             <option value="<%= next.getOID() %>"><%= next.getAnio() %></option>
      <%  } %>
          </select>
          
      </td>
    </tr>
    <tr>
      <td>Incluye evaluados:</td>
      <td align="center"><input type=checkbox name=evaluados checked   disabled> </td>
    </tr>
    <tr>
      <td>Incluye evaluadores:</td>
      <td align="center"><input type=checkbox name=evaluadores checked disabled> </td>
    </tr>
    <tr>
      <td>Incluye objetivos:</td>
      <td align="center"><input type=checkbox name=objetivos checked> </td>
    </tr>
  </table>

  <table width=300 border=0 cellspacing=0 cellpadding=0 class=botones>
    <tr>
      <td>
      <div align="center">
         <img src="<%=pathImg%>/botones/b_aceptar.gif" width="60" height=20 onclick="javascript:document.forms[0].submit();">
         <img src="<%=pathImg%>/botones/b_cancelar.gif" width="60" height=20 onclick="javascript:window.close();">
      </div>
      </td>
    </tr>
  </table>

</div>

</form>
</body>
</html>