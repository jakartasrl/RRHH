<%@ include file="../util/header.jsp" %>

<% size = 760; 

   Object marca = request.getAttribute("JustSaved");
%>

<script language="JavaScript">
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
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_codigo', 0, 6);
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_descripcion', 1, 30);
  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid');

  addCampoText(row, 'T_' + tableID + '_F' + iteration + '_periodo', 2, 20);

  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_carga_obj', 3);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_carga_cumpl', 4);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_evalua_capa', 5);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_carga_resu', 6);
  addCampoCheckBox(row, 'T_' + tableID + '_F' + iteration + '_calcula_res', 7);

  addCampoActivo(row, 'T_' + tableID + '_F' + iteration + '_activo', 8);
}
function validarCargaObj(pos){
  if(document.form1.elements['T_Etapa_F' + pos + '_carga_obj'].checked){
     document.form1.elements['T_Etapa_F' + pos + '_carga_cumpl'].checked = false;
     document.form1.elements['T_Etapa_F' + pos + '_evalua_capa'].checked = false;
     document.form1.elements['T_Etapa_F' + pos + '_carga_resu'].checked  = false;
     document.form1.elements['T_Etapa_F' + pos + '_calcula_res'].checked = false;
  }
}
function validarCargaCumpl(pos){
  if(document.form1.elements['T_Etapa_F' + pos + '_carga_cumpl'].checked)
     document.form1.elements['T_Etapa_F' + pos + '_carga_obj'].checked   = false;
}
function validarCalculaRes(pos){
  if(document.form1.elements['T_Etapa_F' + pos + '_calcula_res'].checked)
     document.form1.elements['T_Etapa_F' + pos + '_carga_obj'].checked   = false;
}
function validarCargaResumen(pos){
  if(document.form1.elements['T_Etapa_F' + pos + '_carga_resu'].checked)
     document.form1.elements['T_Etapa_F' + pos + '_carga_obj'].checked   = false;
}

function validarCargaEvaluaCapa(pos){
  if(document.form1.elements['T_Etapa_F' + pos + '_evalua_capa'].checked)
     document.form1.elements['T_Etapa_F' + pos + '_carga_obj'].checked   = false;
}
</script>

<input type=hidden name="op" value="SaveEtapa">

<div align=center>

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Etapa">
    <tr>
      <th colspan="9" >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Etapas </td>
            <td align="right"><a href="javascript:addRowToTable('Etapa');">+ Agregar Etapa</a>&nbsp; </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr class="header">
      <td align=center><strong>Codigo</strong></td>
      <td align=center><strong>Descripcion</strong></td>
      <td align=center><strong>Periodo</strong></td>
      <td align=center><strong>Carga Objetivo</strong></td>
      <td align=center><strong>Carga Cumplimientos</strong></td>
      <td align=center><strong>Evalua Capacidades</strong></td>
      <td align=center><strong>Carga Resumen</strong></td>
      <td align=center><strong>Calcula Resultado</strong></td>
      <td align=center><strong>Activo</strong></td>
    </tr>

    <% List paises = (List) request.getAttribute("Etapas");
       int i = 0;
       Iterator it = paises.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          i++;%>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	    	<input name="T_Etapa_F<%= i%>_oid" type=hidden value="<%= next.getObject("oid") %>">
		    <td align=center><input name="T_Etapa_F<%= i%>_codigo" type=text value="<%= next.getObject("codigo") %>" size="6" maxlength="6"></td>
		    <td align=center><input name="T_Etapa_F<%= i%>_descripcion" type=text value="<%= next.getObject("descripcion") %>" size="30" maxlength="30"></td>
		    <td align=center><input name="T_Etapa_F<%= i%>_periodo" type=text value="<%= next.getObject("periodo") %>" size="20" maxlength="20"></td>
            <% String checked = "";
               if( ((Boolean)next.getObject("carga_obj")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_carga_obj" type=checkbox <%= checked %> onclick="validarCargaObj(<%= i%>);"></td>

            <% checked = "";
               if( ((Boolean)next.getObject("carga_cumpl")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_carga_cumpl" type=checkbox <%= checked %> onclick="validarCargaCumpl(<%= i%>);"></td>

            <% checked = "";
               if( ((Boolean)next.getObject("evalua_capa")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_evalua_capa" type=checkbox <%= checked %> onclick="validarCargaEvaluaCapa(<%= i%>);"></td>

            <% checked = "";
               if( ((Boolean)next.getObject("carga_resu")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_carga_resu" type=checkbox <%= checked %> onclick="validarCargaResumen(<%= i%>);"></td>

            <% checked = "";
               if( ((Boolean)next.getObject("calcula_res")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_calcula_res" type=checkbox <%= checked %> onclick="validarCalculaRes(<%= i%>);"></td>

            <% checked = "";
               if( ((Boolean)next.getObject("activo")).booleanValue()) 
                  checked = "checked"; 
            %>
               <td align=center><input name="T_Etapa_F<%= i%>_activo" type=checkbox <%= checked %>></td>
		  </tr>
    <% } %>

  </table>

<script language="javascript">
<% if(marca != null) { %>
   parent.frames['encabezado'].document.location.reload();
<% } %>
</script>

<%@ include file="../util/footer.jsp" %>