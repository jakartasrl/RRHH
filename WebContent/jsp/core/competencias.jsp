<%@ include file="../util/header.jsp" %>

<% size = 760;
   usaBotonImprimir = true;
   usaBotonTerminar = true;

   List valores     = (List) request.getAttribute("ValoresCapacidad");
%>

<script language="JavaScript">
parent.frames['encabezado'].imgOver('competencias');

function eliminarTerminar(){
  eliminarBoton(<%= usaBotonTerminar%>);
}
function aprobar(){
  document.form1.elements['aprobado'].value = 1;

<% if(request.getAttribute("EvaluadoSoloPuedeFinalizar") != null) { %>
    document.form1.submit();
<% } else {%>
    enviar();
<% } %>
}
function enviar(){
 if(<%= readOnly.length() > 5%>) return;

 if(validarRadios() == -1)
   return;

 var cantChecked = 0;
 for(j=0; j< document.form1.elements.length;j++){
   var a = document.form1.elements[j];
   
   if(a.type == 'radio' && a.name == 'valor_cap_global' && a.checked)
      cantChecked++;
 }
 
 if(cantChecked == 0){
    alert('Debe marcar una casilla de capacidad de gestion');
    return;
 }

 document.forms[0].submit();
}
function validarRadios(){
 var cantChecked = 0;
 var name;
 var nameAnt;
 var primeravez = true;

 for(j=0; j< document.form1.elements.length;j++){
   var a = document.form1.elements[j];
   
   if(a.type == 'radio' && a.name != 'valor_cap_global'){
      name    = a.name;
      
      if(primeravez){
         nameAnt    = name;
         primeravez = false;
      }
      
      if(nameAnt != name){
         if(cantChecked == 0) {
           alert('Faltan ingresar campos de valoracion de capacidades');
           return -1;
         }
         cantChecked = 0;
         nameAnt     = name;
      }
      
      if(a.checked) 
        cantChecked ++;
   }
 }
 
 if(cantChecked == 0) {
    alert('Faltan ingresar campos de valoracion de capacidades');
    return -1;
 }
}
</script>

<input type=hidden name="nextView" value="3">
<input type=hidden name="aprobado" value=0>
<input type=hidden name="op" value="SaveEvaluaciones">
<input type=hidden name="oid_leg" value="<%= request.getParameter("oid_leg") %>">

<jsp:include 
     page="/jsp/util/tablaError.jsp"
     flush="true"
/>

<div align=center>
   <jsp:include 
        page="/jsp/util/headerEtapas.jsp"
        flush="true"
   />

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding=0 class="contenido">
    <tr>
      <th width="350" colspan="<%= 2 + valores.size()%>">Valoraci&oacute;n de Capacidades de Gesti&oacute;n</th>
    </tr>

    <% List capacidades = (List) request.getAttribute("Capacidades");
       int i = 0;
       Iterator it = capacidades.iterator();
       while(it.hasNext()){
          Registro next   = (Registro) it.next(); 
          Object oidCapa  = next.getObject("oid_capa");
          Object codValCap= next.getObject("cod_val_cap");
          List factores   = (List) request.getAttribute("Factores_" + oidCapa);

          Iterator it2   = factores.iterator();
    %>

    <tr class="header">
      <td align=left><strong><%= next.getObject("codigo") %> - <%= next.getObject("descripcion") %></strong></td>

    <% Iterator itVal = valores.iterator();
       while(itVal.hasNext()){
          Registro rVal = (Registro) itVal.next(); %>

	      <td align=center title="<%= rVal.getObject("descripcion")%>"><%= rVal.getObject("codigo")%></td>
    <% } %>
          <td width=66 align=center>Valoracion</td>
    </tr>

    <% while(it2.hasNext()){
          Registro rFact  = (Registro) it2.next(); 
          Object oidFactor= rFact.getObject("oid_factor");
          Object oidValEle= rFact.getObject("oid_val_fac");

          ++i;
   %>
     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=left>
      <td width="65%"> <%= rFact.getObject("descripcion") %>
        <input name="T_EvalFactor_F<%= oidFactor%>_oid_factor" type=hidden value="<%= oidFactor %>">
      </td>

    <% itVal = valores.iterator();
       while(itVal.hasNext()){
          Registro rVal = (Registro) itVal.next();
          Object oidVal = rVal.getObject("oid_valor"); %>

      <td align=center>
          <input name="T_EvalFactor_F<%= oidFactor%>_oid_valor" type="radio" value="<%= oidVal %>" <% if(oidVal.toString().equals(oidValEle.toString())){%> checked <% } %> <%= readOnly%>>
      </td>
    <% } %>

      <td align=center>&nbsp;</td>
    </tr>

    <% } %> 
    <tr class="total">
      <td colspan="<%= valores.size() + 1%>">&nbsp;</td>
      <td align=center>
          <input name="T_EvalCapacidad_F<%= oidCapa %>_oid_capacidad" type=hidden value="<%= oidCapa%>">
          
          <select name="T_EvalCapacidad_F<%= oidCapa %>_cod_valor" <%= readOnly%>> 
         <% itVal = valores.iterator();
            while(itVal.hasNext()){
              Registro rVal = (Registro) itVal.next();
              Object codVal = rVal.getObject("codigo"); %>

             <option value="<%= codVal%>" <% if(codVal.toString().equals(codValCap)){ %> SELECTED <% } %>><%= codVal%></option> 
         <% } %>
          </select> 
      </td>
    </tr>
    <% } %>
  </table>

  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="evaluacion">
    <tr>
      <th colspan="5">Valoraci&oacute;n Global Capacidades de Gesti&oacute;n</th>
    </tr>
    <tr>
    <% Iterator itVal = valores.iterator();
       while(itVal.hasNext()){
          Registro next = (Registro) itVal.next(); 
          if(!next.getString("valoracion_global").equalsIgnoreCase("true"))
         	 continue;
    %>

      <td align=center valign="top">
       <table width="90%"  border=0 cellspacing=0 cellpadding=0 title="<%= next.getObject("desc_ext")%>">
        <tr>
          <td>
             <input name="valor_cap_global" type="radio" value="<%= next.getObject("oid_valor")%>" <% if(next.getObject("oid_valor").toString().equals(next.getObject("oid_valor_global").toString())){ %> checked <% } %> <%= readOnly%>>
          </td>
        </tr>
        <tr>
          <td><%= next.getObject("codigo")%> - <%= next.getObject("descripcion")%></td>
        </tr>
       </table>
      </td>
    <% } %>
    </tr>
  </table>

<%@ include file="../util/footer.jsp" %>