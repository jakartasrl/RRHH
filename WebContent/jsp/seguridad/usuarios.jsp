<%@ include file="../util/header.jsp" %>
<% size = 760; %>

<script language="JavaScript">
function invertirRRHH(pos){
  if(document.form1.elements['T_Usuarios_F' + pos + '_usuario_rrhh'].checked)
     document.form1.elements['T_Usuarios_F' + pos + '_planeamiento'].checked = false;
}
function invertirPlan(pos){
  if(document.form1.elements['T_Usuarios_F' + pos + '_planeamiento'].checked)
     document.form1.elements['T_Usuarios_F' + pos + '_usuario_rrhh'].checked = false;
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
  var campo = addCampoText(row,  'T_' + tableID + '_F' + iteration + '_codigo', 0, 10);
  campo.setAttribute('maxLength', 20);

  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_descripcion', 1, 30);
  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid_usu');

  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_apellido', 2, 30);

  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_usuario_rrhh', 3);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_planeamiento', 4);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_configurador', 5);

  addCampoActivo(row,'T_' + tableID + '_F' + iteration + '_activo', 6);
}
</script>

<input type=hidden name="op" value="SaveUsuarios">

<div align=center>

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Usuarios">
    <tr>
      <th colspan="7" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Usuarios </td>
            <td align="right"><a href="javascript:addRowToTable('Usuarios');">+ Agregar Usuario</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Nombre</strong></td>
      <td align=center><strong>Apellido</strong></td>
      <td align=center><strong>RRHH</strong></td>
      <td align=center><strong>Planeamiento</strong></td>
      <td align=center><strong>Configurador</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Usuarios");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	        <input name="T_Usuarios_F<%= i%>_oid_usu" type=hidden value="<%= next.getObject("oid_usu") %>">
		    <td align=center><input name="T_Usuarios_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="10" maxlength="20"></td>
		    <td align=center><input name="T_Usuarios_F<%= i%>_descripcion" type=text value="<%= next.getObject("nombres") %>" size="30" maxlength="30"></td>
		    <td align=center><input name="T_Usuarios_F<%= i%>_apellido" type=text value="<%= next.getObject("apellido") %>" size="30" maxlength="30"></td>
            <% String checked = "";
               if(next.getObject("usuario_rrhh").equals("true")) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Usuarios_F<%= i%>_usuario_rrhh" type=checkbox <%= checked %> onclick="javascript:invertirRRHH(<%= i%>);"></td>

            <% checked = "";
               if(next.getObject("planeamiento").equals("true")) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Usuarios_F<%= i%>_planeamiento" type=checkbox <%= checked %> onclick="javascript:invertirPlan(<%= i%>);"></td>

            <% checked = "";
               if(next.getObject("configurador").equals("true")) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Usuarios_F<%= i%>_configurador" type=checkbox <%= checked %>></td>

            <% checked = "";
               if(next.getObject("activo").equals("true")) 
                  checked = "checked"; 
            %>

               <td align=center><input name="T_Usuarios_F<%= i%>_activo" type=checkbox <%= checked %>></td>
		  </tr>
    <% } %>

  </table>

<%@ include file="../util/footer.jsp" %>