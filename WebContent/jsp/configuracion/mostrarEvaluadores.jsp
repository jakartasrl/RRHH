<%@ include file="../util/header.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<% size = 0; %>

<link href="<%= request.getContextPath()%>/estilos/estilos2.css" rel="stylesheet" type="text/css">

<script language="JavaScript">
function grabarOID(a, b, c){
  window.opener.grabarCampos();

  window.opener.document.forms[0].elements[document.forms[0].oid.value].value         = a;
  window.opener.document.forms[0].elements[document.forms[0].codigo.value].value      = b;
  window.opener.document.forms[0].elements[document.forms[0].descripcion.value].value = c;

  self.close();
}

</script>

<input type=hidden name="oid"         value="">
<input type=hidden name="codigo"      value="">
<input type=hidden name="descripcion" value="">

<div align=center>

  <display:table name="Evaluadores" id="registro" class="report" requestURI="/FrontServlet?op=VerEvaluadores" pagesize="20">
      <display:column title="Legajo"   sortable="true"><%= ((Registro) registro).getObject("legajo") %></display:column>
      <display:column title="Nombres"  sortable="true"><%= ((Registro) registro).getObject("nombres") %></display:column>
      <display:column title="Apellido" sortable="true"><%= ((Registro) registro).getObject("apellido") %></display:column>
      <display:column><a href="javascript:grabarOID('<%= ((Registro) registro).getObject("oid_leg_ejer") %>', '<%= ((Registro) registro).getObject("legajo") %>', '<%= ((Registro) registro).getObject("apellido") %>'); ">Aceptar</a> </display:column>
  </display:table>


<%@ include file="../util/footer.jsp" %>