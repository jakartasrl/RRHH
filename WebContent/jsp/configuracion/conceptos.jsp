<%@ include file="../util/header.jsp" %>



<input type=hidden name="op" value="SaveConceptos">

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Conceptos">
    <tr>
      <th colspan="4" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Conceptos </td>
            <td align="right"><a href="javascript:addRowToTable('Conceptos');">+ Agregar Concepto</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Tipo</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Conceptos");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
		    <td align=center><input name="T_Conceptos_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10"></td>
		    <td align=center><input name="T_Conceptos_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40"></td>
	    	<input name="T_Conceptos_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">
            <td align=center>
              <select NAME="T_Conceptos_F<%= i%>_tipo">
                 <option VALUE="R" <% if(next.getObject("tipo").equals("R")) { %> selected <% } %>>EVALUADOR</option>
                 <option VALUE="O" <% if(next.getObject("tipo").equals("O")) { %> selected <% } %>>EVALUADO</option>
              </select>
            </td>
            <td align=center><input type=checkbox name="T_Conceptos_F<%= i%>_activo" <%if(((Boolean)next.getObject("activo")).booleanValue()){%>checked<%}%>></td>
		 </tr>
    <% } %>
  </table>

<%@ include file="../util/footer.jsp" %>