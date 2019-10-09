<%@ include file="../util/header.jsp" %>

<% size = 520;%>

<script language="JavaScript">
function addRowToTable(tableID){
  var tbl       = document.getElementById(tableID);
  var lastRow   = tbl.rows.length;

  if(checkCodigoDescAnt(lastRow, tableID)){
     alert('Debe ingresar todos los datos antes de agregar elementos');
     return;
  }

  var row = tbl.insertRow(lastRow);
  setClassName(row, lastRow);

  var iteration = lastRow - 1;  
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_codigo', 0, 8);
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_descripcion', 1, 80);

  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid');
  addCampoActivo(row,'T_' + tableID + '_F' + iteration + '_activo', 2);
}

function enviar(){
 document.forms[0].submit();
}
</script>

<input type=hidden name="op"            value="SaveFactores">
<input type=hidden name="oid_capacidad" value="<%= request.getParameter("oid_capacidad") %>">

<div align=center>

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Actores">
    <tr>
      <th colspan="4" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Factores </td>
            <td align="right"><a href="javascript:addRowToTable('Actores');">+ Agregar Factor</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Factores");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
		    <td align=center><input name="T_Actores_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="8" maxlength="8"></td>
		    <td align=center><input name="T_Actores_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="80" maxlength="80"></td>
	    	<input name="T_Actores_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">

            <% if( ((Boolean)next.getObject("activo")).booleanValue()) {%>
               <td align=center><input name="T_Actores_F<%= i%>_activo" type=checkbox checked></td>
            <% } else { %>
               <td align=center><input name="T_Actores_F<%= i%>_activo" type=checkbox></td>
            <% } %>
		 </tr>
    <% } %>

  </table>


<%@ include file="../util/footer.jsp" %>