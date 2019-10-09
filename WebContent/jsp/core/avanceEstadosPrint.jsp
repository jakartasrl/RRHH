<%@ page import="com.jkt.framework.util.Registro"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

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
<link href="<%=request.getContextPath()%>/estilos/estilos2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/estilos/funciones.js"></script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="form1" action="<%=request.getContextPath()%>/FrontServlet" method="POST">

<% List evaluados   = (List) request.getAttribute("TDatos"); 
   List evaluadores = (List) request.getAttribute("Evaluadores");
%>

<script language="JavaScript">
function checkEnter(){
  if (window.event && window.event.keyCode == 13)
     enviar();
}
</script>

<% if(evaluados == null){ %>

<input type=hidden name="op" value="ConsultaAvanceObjetivos">

<div align=center>
  <br>
  
  <table width="1000" border=0 cellspacing="1" cellpadding="0">
    <tr>
      <th colspan="2">Consulta de Estados</th> 
    </tr>
    <tr>
      <td colspan="2">
        <table align="center">
          <tr>
            <td width="15%"><strong>Nombres:</strong></td>
            <td width="35%"><input name="nombres"  type=text style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
            <td width="15%"><strong>Apellido:</strong></td>
            <td width="35%"><input name="apellido" type=text style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
          </tr>
          <tr>
            <td width="15%"><strong>Legajo:&nbsp;&nbsp;&nbsp;</strong></td>
            <td width="35%"><input name="legajo" type=text style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
            <td width="15%"><strong>Evaluador:&nbsp;&nbsp;&nbsp;</strong></td>
            <td width="35%">
               <select name="oid_evaluador" STYLE="width: 200px">
                  <option value="-1">---------------------------------</option>

                  <iterator:iterar coleccion="<%=  evaluadores %>">
                      <option value="<%= registro.getObject("oid_eval")%>"><%= registro.getObject("nombre")%></option>
                  </iterator:iterar>
               </select>
            </td>
          </tr>
          <tr>
            <td width="15%"><strong>Periodo:</strong></td>
            <td width="35%"><input type=text name="ano" size="4" maxlength="4" value="<%if(request.getParameter("ano") != null){%><%= request.getParameter("ano") %><%}else{%><%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %><%}%>" onKeyPress="checkEnter();"></td>
            <td width="15%"><strong>Etapa:</strong></td>
            <td width="35%">
               <select name="opcion" style="text-align:left; width: 200px">
                  <option value="1" <% if("1".equals(request.getParameter("opcion"))){%> selected <%}%> >Carga de Objetivos</option> 
                  <option value="2" <% if("2".equals(request.getParameter("opcion"))){%> selected <%}%> >Cumplimientos</option> 
                  <option value="3" <% if("3".equals(request.getParameter("opcion"))){%> selected <%}%>>Competencias</option> 
               </select>
            </td>
          </tr>
          <tr>
            <td colspan="4" align="center"><div align=center><input type="button" value="Buscar" onclick="javascript:enviar();"/></div></td>
          </tr>
        </table>
      </td>
     </tr>
  </table>
</div>

<%} else { %>  

  <div align=center>
     <table>
       <tr>
          <td align="left">
           <a href="Javascript:history.back();" style="font-size: 16px; color:blue; text-align:left"><b>Volver a la pagina anterior</b></a>
          </td>
       </tr>
     </table>
  
  	 <display:table name="TDatos" id="reg" requestURI="/FrontServlet?op=ConsultaAvanceObjetivos" class="simple nocol" export="true">
	      <display:column title="Legajo"    sortable="true" group="1"><%= ((Registro) reg).getObject("LEGAJO") %></display:column>
	      <display:column title="Apellidos" sortable="true" group="2" align="left"><%= ((Registro) reg).getObject("APELLIDOS") %></display:column>
	      <display:column title="Nombres"   sortable="true" group="3" align="left"><%= ((Registro) reg).getObject("NOMBRES") %></display:column>
	      <display:column title="Evaluado"><%= ((Registro) reg).getObject("evaluado") %></display:column>
	      <display:column title="Evaluador"><%= ((Registro) reg).getObject("evaluador") %></display:column>
	      <display:column title="Planeamiento"><%= ((Registro) reg).getObject("planeamiento") %></display:column>
	      <display:column title="Legajo Evaluador"><%= ((Registro) reg).getObject("legajo_evaluador") %></display:column>
	      <display:column title="Apellidos Evaluador"><%= ((Registro) reg).getObject("apellido_evaluador") %></display:column>
	      <display:column title="Nombres Evaluador"><%= ((Registro) reg).getObject("nombre_evaluador") %></display:column>
     </display:table>
 </div>
<% } %>

</form>
</body>
</html>