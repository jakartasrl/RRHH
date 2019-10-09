<%@ include file="../util/header.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<link href="<%=request.getContextPath()%>/estilos/estilos2.css" rel="stylesheet" type="text/css">

<input type=hidden name="op" value="SaveRolLegajos">
<input type=hidden name="oid_ejercicio" value="<%= request.getParameter("oid_ejercicio")%>">

<div align=center>
  <% int i = 1;%>

  <display:table name="Roles" id="registro" class="report" requestURI="/FrontServlet?op=VerRolLegajos" pagesize="20">
      <display:column title="Legajo"    sortable="true"><%= ((Registro) registro).getObject("legajo") %></display:column>
      <display:column title="Nombres"   sortable="true"><%= ((Registro) registro).getObject("nombres") %></display:column>
      <display:column title="Apellidos" sortable="true"><%= ((Registro) registro).getObject("apellido") %></display:column>
      <display:column title="Evaluado">
          <input type=checkbox name="T_Legajo_F<%= i%>_es_evaluado" <% if(((Registro) registro).getBoolean("es_evaluado").booleanValue()){ %> checked <%}%>>
      </display:column>
      <display:column title="Evaluador">
          <input type=checkbox name="T_Legajo_F<%= i%>_es_evaluador" <% if(((Registro) registro).getBoolean("es_evaluador").booleanValue()){ %> checked <%}%>>
      </display:column>
      <display:column title="Administrador">
          <input type=hidden name="T_Legajo_F<%= i%>_oid_legajo"    value="<%= ((Registro) registro).getObject("oid_legajo") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_oid_leg_ejer"  value="<%= ((Registro) registro).getObject("oid_leg_ejer") %>">

          <input type=checkbox name="T_Legajo_F<%= i%>_es_administrador" <% if(((Registro) registro).getBoolean("es_administrador").booleanValue()){ %> checked <%}%>>
      </display:column>

      <% i++;%>
  </display:table>

<%@ include file="../util/footer.jsp" %>