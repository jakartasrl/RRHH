<%@ include file="../util/header.jsp" %>

<script language="JavaScript">
function enviar(){
 document.forms[0].submit();
}
</script>

<input type=hidden name="op" value="SaveEntidades">

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Entidades">
    <tr>
      <th colspan="3" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Entidades </td>
            <td align="right"><a href="javascript:addRowToTable('Entidades');">+ Agregar Entidades</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Entidades");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	      <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
		    <td align=center><input name="T_Entidades_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10"></td>
		    <td align=center><input name="T_Entidades_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40"></td>
	    	<input name="T_Entidades_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">

            <% if( ((Boolean)next.getObject("activo")).booleanValue()) {%>
               <td align=center><input type=checkbox name="T_Entidades_F<%= i%>_activo" checked="checked"></td>
            <% } else { %>
               <td align=center><input type=checkbox name="T_Entidades_F<%= i%>_activo"></td>
            <% } %>
		 </tr>
    <% } %>

  </table>

<%@ include file="../util/footer.jsp" %>