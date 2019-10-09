<%@ include file="../util/header.jsp" %>

<% List etapas = (List) request.getAttribute("Etapas"); 
   Iterator itE = etapas.iterator();

   Object marca = request.getAttribute("JustSaved");
%>

<script language="JavaScript">
   valores = new Array(<%= etapas.size()%>);
<% for(int v = 0; v< etapas.size(); v++){%>
   valores[<%= v %>] = '<%= ((Registro) etapas.get(v)).getObject("oid_etapa")%>';
<% } %>

function enviar(){
 var total = 0;
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'checkbox' && a.name.indexOf('actual') != -1 && a.checked)
       total++;
 }
 
 if(total == 0){
    alert('Debe haber al menos 1 ejercicio habilitado');
    return;
 }

 if(total > 1){
    alert('Debe haber solamente 1 ejercicio habilitado');
    return;
 }
 
 for(i=1;i<= 20; i++){

     var cantIniciados = 0;
     for(a = 0; a< valores.length; a++){
        var oidValor = valores[a];
        var combo = document.form1.elements['T_Ejercicios_F' + i + '_estado_' + oidValor];

        if("undefined" == typeof(combo))
          document.forms[0].submit();

        if(combo.options[combo.selectedIndex].value == 'I'){
           cantIniciados++;
        }
     }

     if(cantIniciados == 0){
        alert('Debe haber al menos una etapa activa');
        return;
     }
     
     if(cantIniciados > 1){
        alert('No puede haber mas de 1 etapa iniciada a la vez');
        return;
     }
 }

 document.forms[0].submit();
}
function addRowToTable(tableID, etapas){
  var tbl       = document.getElementById(tableID);
  var lastRow   = tbl.rows.length;

  var row = tbl.insertRow(lastRow);
  setClassName(row, lastRow);

  var iteration = lastRow - 1;  
  var campo = addCampoText(row,  'T_' + tableID + '_F' + iteration + '_anio', 0, 8);
  campo.style.textAlign = 'right';
  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid');
}

function showEvaluadores(oidEjer){
  var url = '<%=request.getContextPath()%>/FrontServlet?op=VerRolLegajos&oid_ejercicio=' + oidEjer;
  window.open(url,'windowRef','left=150,top=100,width=450,height=530,directories=no,status=no,resize=no,menubar=no,scrollbars=yes');
}

function showDatosCopia(oidEjer, anioTo){
  var url = '<%=request.getContextPath()%>/FrontServlet?op=VerDatosCopia&oid_eje=' + oidEjer + '&anio_to=' + anioTo;
  window.open(url,'windowRef','left=200,top=100,width=400,height=240,directories=no,status=no,resize=no,menubar=no,scrollbars=yes');
}

</script>

<input type=hidden name="op" value="SaveEjercicios">

<div align=center>
<table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" id="Ejercicios">
  <tr>
    <th colspan="<%= 4 + etapas.size() %>" >
      <table width="100%"  border=0 cellspacing="0" cellpadding="0">
        <tr>
          <td align=left>Ejercicios </td>
          <td align="right"><a href="javascript:addRowToTable('Ejercicios', valores);">+ Agregar Ejercicios</a>&nbsp; </td>
        </tr>
      </table>
     </th>
  </tr>
  <tr class="header">
       <td align=center><strong>Ano</strong></td>
       <td align=center><strong>Actual</strong></td>

    <%
       while(itE.hasNext()){
         Registro next = (Registro) itE.next(); 
    %>
         <td align=center><strong><%= next.getObject("descripcion") %></strong></td>
    <% } %>

       <td align=center><strong>Cargar <br> Evaluadores</strong></td>
       <td align=center><strong>Copiar <br> Configuracion</strong></td>
    </tr>

    <% List ejercicios = (List) request.getAttribute("Ejercicios");
       int i = 0;
       Iterator it = ejercicios.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next(); 
          Object oidEjercicio = next.getObject("oid_ejercicio");
          i++;%>
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
		    <td align=center>
  		      <input name="T_Ejercicios_F<%= i %>_anio" type=text value="<%= next.getObject("anio") %>" size="8" style="text-align:right">
	    	  <input name="T_Ejercicios_F<%= i%>_oid" type=hidden value="<%= oidEjercicio %>">
		    </td>

		    <td align=center>
  		      <input name="T_Ejercicios_F<%= i %>_actual" type=checkbox <%if(next.getObject("actual").toString().equals("true")){out.print("checked");} %>>
		    </td>

         <% List etapasEjer = (List) request.getAttribute("EtapasEjercicio_" + oidEjercicio);
            itE = etapasEjer.iterator();
             while(itE.hasNext()){
               Registro etapa  = (Registro) itE.next(); 
               Object oidEtapa = etapa.getObject("oid_etapa");
          %>

           <td align=center margin="0">
              <select NAME="T_Ejercicios_F<%= i %>_estado_<%= oidEtapa %>">
                 <option VALUE="C" <% if(etapa.getObject("cerrada").equals("true")) { %> selected <% } %>> Cerrada
                 <option VALUE="I" <% if(etapa.getObject("iniciada").equals("true")) { %> selected <% } %>>Iniciada
              </select>
           </td>
      <% } %>

         <td align=center><img src="<%= pathImg%>/detail_off.gif" onclick="javascript:showEvaluadores('<%= oidEjercicio %>');"></td>
         <td align=center><img src="<%= pathImg%>/detail_off.gif" onclick="javascript:showDatosCopia('<%= oidEjercicio %>', '<%= next.getObject("anio") %>');"></td>
     </tr>
 <% } %>

</table>

<script language="javascript">
<% if(marca != null) { %>
   parent.frames['encabezado'].document.location.reload();
<% } %>
</script>

<%@ include file="../util/footer.jsp" %>