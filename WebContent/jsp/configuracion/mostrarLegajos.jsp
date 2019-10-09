<%@ include file="../util/header.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<% size = 0; %>

<link href="<%= request.getContextPath()%>/estilos/estilos2.css" rel="stylesheet" type="text/css">

<input type=hidden name=op value=SaveJefe>
<input type=hidden name=evaluado       value="">
<input type=hidden name=administrador  value="">
<input type=hidden name=evaluador      value="">

<script language="JavaScript">
function grabarEval(a){
  if(window.name == a){
     alert('No se puede asignar como jefe de un legajo a si mismo. En caso que no tenga jefe, asignar NINGUNO');
     return;
  }

  document.forms[0].evaluado.value = window.name;

  <% if(request.getParameter("administrador") != null) {%>
     document.forms[0].administrador.value = a;
  <% } else { %>
     document.forms[0].evaluador.value = a;
  <% } %>

  document.forms[0].submit();
}

<% if(request.getAttribute("JustSaved") != null) {%>
  window.opener.document.location.reload();
  self.close();
<% } %>

</script>

<div align=center>

  <display:table name="Legajos" id="registro" class="report" requestURI="/FrontServlet?TraerLegajosAnioJefe&ninguno=true" pagesize="20">
      <display:column title="Legajo"   sortable="true"><%= ((Registro) registro).getObject("legajo") %></display:column>
      <display:column title="Nombres"  sortable="true"><%= ((Registro) registro).getObject("nom") %></display:column>
      <display:column title="Apellido" sortable="true"><%= ((Registro) registro).getObject("ape") %></display:column>
      <display:column><a href="javascript:grabarEval('<%= ((Registro) registro).getObject("oid_leg") %>'); ">Aceptar</a> </display:column>
  </display:table>


<%@ include file="../util/footer.jsp" %>