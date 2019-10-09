<%@ include file="../util/header.jsp" %>
<script language="Javascript">
function enviar(){
 document.forms[0].submit();
}

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
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_es_reto', 2);
  addImg(row, 3, '<%= pathImg%>/detail_off.gif', 40, 20);
  addCampoActivo(row,'T_' + tableID + '_F' + iteration + '_activo', 4);
}
function showItems(oid){
  url = '<%=request.getContextPath()%>/FrontServlet?op=ShowDetallesDimension&oid_dimen=' + oid;
  window.open(url,'windowRef','left=200,top=200,width=400,height=400,directories=no,status=no,resize=no,menubar=no');
}
</script>

<input type=hidden name="op" value="SaveGrupoObjetivo">

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="GrupoObjetivo">
    <tr>
      <th colspan="5" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Dimensiones </td>
            <td align="right"><a href="javascript:addRowToTable('GrupoObjetivo');">+ Agregar Dimensiones</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Es Reto</strong></td>
      <td align=center><strong>Detalles</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("GrupoObjetivo");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	    	<input name="T_GrupoObjetivo_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">
		    <td align=center><input name="T_GrupoObjetivo_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10"></td>
		    <td align=center><input name="T_GrupoObjetivo_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="40"></td>
            <% if( ((Boolean)next.getObject("es_reto")).booleanValue()) {%>
               <td align=center><input name="T_GrupoObjetivo_F<%= i%>_es_reto" type=checkbox checked></td>
            <% } else { %>
               <td align=center><input name="T_GrupoObjetivo_F<%= i%>_es_reto" type=checkbox></td>
            <% } %>

            <td align=center><img src="<%= pathImg%>/detail_off.gif" onclick="javascript:showItems('<%= next.getObject("oid") %>');"></td>

            <% if( ((Boolean)next.getObject("activo")).booleanValue()) {%>
               <td align=center><input name="T_GrupoObjetivo_F<%= i%>_activo" type=checkbox checked></td>
            <% } else { %>
               <td align=center><input name="T_GrupoObjetivo_F<%= i%>_activo" type=checkbox></td>
            <% } %>
		 </tr>
    <% } %>

  </table>

<%@ include file="../util/footer.jsp" %>