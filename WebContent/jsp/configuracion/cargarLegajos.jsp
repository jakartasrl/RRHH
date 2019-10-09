<%@ include file="../util/header.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<link href="<%=request.getContextPath()%>/estilos/estilos2.css" rel="stylesheet" type="text/css">

<script language="JavaScript">
function enviar(){
 document.forms[0].submit();
}
function buscar(){
 if(document.form1.inicial.value == ''){
    alert('Debe cargar una inicial');
    return;
 }
 document.form1.op.value= 'MostrarLegajosBUP';
 enviar();
}
</script>

<input type=hidden name="op" value="SaveLegajos">

<div align=center>
  Inicial: <input type=text name="inicial" value=""><input type="button" value="Buscar" onclick="javascript:buscar();"/>
  <br>
  <br>
  <br>
  <% int i = 1;%>

  <display:table name="Legajos" id="registro" class="report" requestURI="/FrontServlet?op=MostrarLegajosBUP" pagesize="25">
      <display:column title="Legajo"   sortable="true"><%= ((Registro) registro).getObject("BUP_LEGAJO") %></display:column>
      <display:column title="Apellido" sortable="true"><%= ((Registro) registro).getObject("BUP_APELLIDO") %></display:column>
      <display:column title="Nombres"  sortable="true"><%= ((Registro) registro).getObject("BUP_NOMBRE") %></display:column>
      <display:column title="Mail" sortable="true"><%= ((Registro) registro).getObject("BUP_EMAIL") %></display:column>
      <display:column title="Nacimiento" sortable="true"><%= ((Registro) registro).getObject("BUP_FEC_NAC") %></display:column>
      <display:column title="Incluir">
          <input type=hidden name="T_Legajo_F<%= i%>_apellido"     value="<%= ((Registro) registro).getObject("BUP_APELLIDO") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_nombre"       value="<%= ((Registro) registro).getObject("BUP_NOMBRE") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_mail"         value="<%= ((Registro) registro).getObject("BUP_EMAIL") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_nacionalidad" value="<%= ((Registro) registro).getObject("BUP_NACIONALI") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_legajo"       value="<%= ((Registro) registro).getObject("BUP_LEGAJO") %>">
          <input type=hidden name="T_Legajo_F<%= i%>_nacimiento"   value="<%= ((Registro) registro).getObject("BUP_FEC_NAC") %>">

          <input type=checkbox name="T_Legajo_F<%= i%>_incluido" <% if(((Registro) registro).getBoolean("incluido").booleanValue()){ %> checked <%}%>>
          <% i++;%>
      </display:column>
  </display:table>

<%@ include file="../util/footer.jsp" %>