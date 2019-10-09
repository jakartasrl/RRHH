<%@ include file="../util/header.jsp" %>

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
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_codigo', 0, 10);
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_descripcion', 1, 40);

  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid');
  addImg(row, 2, '<%= pathImg%>/detail_off.gif', 40, 20);
  addCampoActivo(row,'T_' + tableID + '_F' + iteration + '_activo', 3);
}

function showItems(oid){
  url = '<%=request.getContextPath()%>/FrontServlet?op=ShowFactores&oid_capacidad=' + oid;
  window.open(url,'windowRef','left=200,top=200,width=540,height=400,directories=no,status=no,resize=no,menubar=no');
}
</script>

<input type=hidden name="op" value="SaveCapacidades">

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Capacidades">
    <tr>
      <th colspan="4" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Capacidades </td>
            <td align="right"><a href="javascript:addRowToTable('Capacidades');">+ Agregar Capacidad</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Detalles</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List capacidades = (List) request.getAttribute("Capacidades");
       int i = 0;
       Iterator it = capacidades.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
 	        <td align=center><input name="T_Capacidades_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10"></td>
		    <td align=center><input name="T_Capacidades_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40"></td>
	    	<input name="T_Capacidades_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">
            <td align=center><img src="<%= pathImg%>/detail_off.gif" onclick="javascript:showItems('<%= next.getObject("oid") %>');"></td>

            <% if( ((Boolean)next.getObject("activo")).booleanValue()) {%>
               <td align=center><input name="T_Capacidades_F<%= i%>_activo" type=checkbox checked></td>
            <% } else { %>
               <td align=center><input name="T_Capacidades_F<%= i%>_activo" type=checkbox></td>
            <% } %>
		 </tr>
    <% } %>
  </table>

<%@ include file="../util/footer.jsp" %>