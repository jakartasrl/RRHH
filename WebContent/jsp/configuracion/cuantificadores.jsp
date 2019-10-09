<%@ include file="../util/header.jsp" %>
<% size = 380; %>

<input type=hidden name="op"            value="SaveCuantificadores">
<input type=hidden name="oid_dimen" value="<%= request.getParameter("oid_dimen") %>">

<div align=center>

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Cuantificadores">
    <tr>
      <th colspan="4" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Cuantificadores </td>
            <td align="right"><a href="javascript:addRowToTable('Cuantificadores');">+ Agregar Cuantificador</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Cuantificadores");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
		    <td align=center><input name="T_Cuantificadores_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10" maxlength="8"></td>
		    <td align=center><input name="T_Cuantificadores_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40" maxlength="40"></td>
	    	<input name="T_Cuantificadores_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">

            <% if( ((Boolean)next.getObject("activo")).booleanValue()) {%>
               <td align=center><input name="T_Cuantificadores_F<%= i%>_activo" type=checkbox checked></td>
            <% } else { %>
               <td align=center><input name="T_Cuantificadores_F<%= i%>_activo" type=checkbox></td>
            <% } %>
		  </tr>
    <% } %>

  </table>

<%@ include file="../util/footer.jsp" %>