<%@ include file="../util/header.jsp" %>

<script language="Javascript">
function showComentario(idCampo){
  window.open('<%= request.getContextPath()%>/FrontServlet?op=VerComentarios',idCampo,'left=100, top=100,width=300,height=200');
}
function addRowToTable(tableID){
  var tbl = document.getElementById(tableID);
  var lastRow = tbl.rows.length;
  // if there's no header row in the table, then iteration = lastRow + 1
  var iteration = lastRow;
  var row = tbl.insertRow(lastRow);

  setClassName(row, lastRow);

  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_codigo', 0, 10);
  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_descripcion', 1, 40);
  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_desc_ext');
  addCampoHidden(row, 'T_' + tableID + '_F' + iteration + '_oid');
  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_valorNumerico', 2, 8);
  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_ponderador', 3, 8);
  addCampoActivo  (row, 'T_' + tableID + '_F' + iteration + '_activo', 4);
}
</script>

<input type=hidden name="op" value="SaveValorCumplimiento">

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="ValCumpGlobal">
    <tr>
      <th colspan="7" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Valores De Cumplimiento </td>
            <td align="right"><a href="javascript:addRowToTable('ValCumpGlobal');">+ Agregar Valor Cumplimiento</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Valor</strong></td>
      <td align=center><strong>Ponderador</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("ValCumpGlobal");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
    	    <input name="T_ValCumpGlobal_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">
		    <td align=center><input name="T_ValCumpGlobal_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10"></td>
		    <td align=center>
		      <input name="T_ValCumpGlobal_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40">
		      <input name="T_ValCumpGlobal_F<%= i%>_desc_ext" type=hidden value="<%= next.getObject("desc_ext") %>">

		      <a href="javascript:showComentario('T_ValCumpGlobal_F<%= i%>_desc_ext');">
	             <img src="<%= pathImg%>/detail_off.gif" width="10" height="10" border=0>
		      </a>
		    </td>
		    <td align=center><input name="T_ValCumpGlobal_F<%= i%>_valorNumerico" type=text style="text-align:right" value="<%= next.getObject("valorNumerico") %>" size="8"></td>
		    <td align=center><input name="T_ValCumpGlobal_F<%= i%>_ponderador" type=text style="text-align:right" value="<%= next.getObject("ponderador") %>" size="8"></td>
            <td align=center><input name="T_ValCumpGlobal_F<%= i%>_activo" type=checkbox <% if(((Boolean)next.getObject("activo")).booleanValue()) {%>checked<% } %>></td>
		 </tr>
    <% } %>
  </table>

<%@ include file="../util/footer.jsp" %>