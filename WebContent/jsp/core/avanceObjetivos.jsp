<%@ include file="../util/header.jsp" %>
<%@ page import="com.jkt.top150.varios.bl.LongCharPrinter"%>
<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 

<% size             = 760;
   usaBotonTerminar = true;
   versionImprimible= request.getContextPath() + "/FrontServlet?op=AvanceObjetivos&imprimir=1&oid_leg=" + request.getParameter("oid_leg");

   LongCharPrinter lcp = new LongCharPrinter();
   boolean usuarioNoDebeCargarCumplimientosTodavia = request.getAttribute("EvitarValidacionCumpGlobal") != null;
   List valores  = (List) request.getAttribute("ValCumpGlobal");
   
   double maxValorPermitido = 0;
   Iterator itV = valores.iterator();
   while(itV.hasNext()){
   	   Registro next = (Registro) itV.next();
   	   double parcial    = next.getDouble("valor").doubleValue();
   	   if(parcial > maxValorPermitido)
   	   	  maxValorPermitido = parcial;
   }
%>

<script language="JavaScript">
parent.frames['encabezado'].imgOver('objetivos');

function sumarResultado(){
 document.form1.elements['total'].value = 0;
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf('_resultado') != -1 && !a.disabled)
      document.form1.elements['total'].value = parseFloat(document.form1.elements['total'].value) + parseFloat(a.value);
 }
}
function enviar(){
 try{
   validar();
 }
 catch(e){
   alert(e);
   return;
 }

 document.forms[0].submit();
}
function validar() {
 if(!<%= usuarioNoDebeCargarCumplimientosTodavia%> && controlarChecks() == 0)
    throw 'Debe marcar una casilla de cumplimiento';
    
  validarNumeros('porcentaje');
  validarMaxLength('cumplimiento_global', 4000);
}

function aprobar() {
 try{
   validar();
 }
 catch(e){
   alert(e);
   return;
 }

 document.form1.elements['aprobado'].value = 1;
 document.forms[0].submit();
}
function controlarChecks(){
 var cantChecked = 0;
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'radio'){
       if(a.checked)
          cantChecked++;
   }
 }
 return cantChecked;
}
function showRow(rowID) {
	var theRow = document.getElementById("tr_" + rowID);
	var theImage = document.getElementById("img_" + rowID);

	(theRow.style.display == "") ? theRow.style.display = "none" : theRow.style.display = "";
	(theRow.style.display == "") ? theImage.src = "<%= pathImg%>/menos.gif" : theImage.src = "<%= pathImg%>/mas.gif";
}
function showComentario(idCampo){
  window.open('<%= request.getContextPath()%>/FrontServlet?op=VerComentarios',idCampo,'left=100, top=100,width=300,height=200');
}
function cambiarResultado(reCalcula, campo){
 try{
   validarNoNumero(campo.value, 'porcentaje');
 }
 catch(e){
   alert(e);
   campo.select();
   campo.focus();
   return;
 }
 
 var esNoAplica = campo.value.toUpperCase() == 'NA';
 if(!esNoAplica && campo.value > <%= maxValorPermitido %>){
   alert('El valor ingresado "' + campo.value + '" supera el maximo: "' +  <%= maxValorPermitido %> + '"');
   campo.select();
   campo.focus();
   return;
 }
 
 if(!reCalcula)
    return;

 eliminarBoton(<%= usaBotonTerminar%>);

 reCalcular();
 sumarResultado(); 
}
function reCalcular(){
 var totalPonderacion = sumarTotalPonderacion();
 for(j=0; j< document.forms[0].elements.length;j++){
   var a     = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf('_porcentaje') != -1 && !a.disabled){
     if(a.value.toUpperCase() != 'NA' && totalPonderacion != 0){
        var valor = (a.value * parseFloat(a.getAttribute("ponderacion"))) / totalPonderacion;
        document.form1.elements[a.getAttribute("campoResultado")].value = valor;
     }
     else document.form1.elements[a.getAttribute("campoResultado")].value = 0;
   }
 }
}
function sumarTotalPonderacion() {
 var result = 0;
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf('_porcentaje') != -1 && !a.disabled && a.value.toUpperCase() != 'NA')
      result = result + parseFloat(a.getAttribute("ponderacion"));
 }
 return result;
}
</SCRIPT>

<input type=hidden name="nextView" value="2">
<input type=hidden name="aprobado" value=0>
<input type=hidden name="op" value="SaveAvanceObjetivos">
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

    <% List grupos = (List) request.getAttribute("Grupos");
       List etapas = (List) request.getAttribute("Etapas");

       int i = 1;
       Iterator it = grupos.iterator();
       while(it.hasNext()){
          Registro next = (Registro) it.next();
          boolean esReto  = next.getObject("is_reto").toString().equalsIgnoreCase("true");%>

  <table width="<%= size%>" border=0 cellspacing=1 cellpadding=0 class="contenido">
    <tr>
      <th colspan="<%= esReto ? (5 + etapas.size()) : (6 + etapas.size()) %>"><%= next.getObject("codigo") %>: <%= next.getObject("descripcion") %></th>
    </tr>
	<tr class="header">
      <td colspan="2">Descripci&oacute;n del Objetivo</td>
      <td>Cuantificaci&oacute;n</td>
      <% if(!esReto){%>
         <td>Ponderaci&oacute;n</td>
      <% } %>
      <td>Cump. Real</td>
            
      <iterator:iterar coleccion="<%=  etapas %>">
           <td><%= registro.getObject("descripcion") %></td>
      </iterator:iterar>

      <td width="40">Resultado</td>
    </tr>

    <% List cuantificadores = (List) request.getAttribute("Cuantificadores_" + next.getObject("oid_grupo"));
       List objetivos       = (List) request.getAttribute("Objetivos_" + next.getObject("oid_grupo")); 
       Iterator it2   = objetivos.iterator();
       while(it2.hasNext()){
          Registro reg2      = (Registro) it2.next();
          Object oidObjetivo = reg2.getObject("oid_objetivo");
    %>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	      <td width="21" align=center>
    	    <a href="javascript:showRow('<%= oidObjetivo%>');">
		    <img src="<%= pathImg%>/mas.gif" id="img_<%= oidObjetivo%>" width="16" height="16" border=0>
		    </a>
	      </td>
	      <td width="320" align=left><%= lcp.print(reg2.getObject("descripcion").toString(), 45)%> </td>
	      <td><%= reg2.getObject("cuantificacion")%></td>
          <% if(!esReto){%>
   	         <td><%= reg2.getObject("ponderacion")%></td>
          <% } %>
          
           <td width="60" align=center>		      
              <input name="T_Consecucion_F<%= oidObjetivo%>_cump_real" type=text size=10 value="<%=reg2.getObject("cump_real")%>" <%= readOnly%>>
           </td>

      <% List cumplimientos = (List) request.getAttribute("Cumplimiento_" + reg2.getObject("oid_objetivo")); %>
      <iterator:iterar coleccion="<%=  cumplimientos %>">
       <%
            Object oidEtapa = registro.getObject("oid_etapa");
            Object oidCumpl = registro.getObject("oid_cumpl");
            
            boolean calculaResultado = registro.getObject("calculaResultado").toString().equalsIgnoreCase("true");
            boolean actual           = registro.getObject("actual").toString().equalsIgnoreCase("true");
      %>
           <td align=center>
              <input name=T_Consecucion_F<%= oidObjetivo%>_oid_cumpl_<%=  oidEtapa%> type=hidden value="<%=registro.getObject("oid_cumpl")%>">
              <input name=T_Consecucion_F<%= oidObjetivo%>_comentario_<%= oidEtapa%> type=hidden value="<%=registro.getObject("comentario")%>" <%= readOnly%> <%if(!actual){%>disabled<%}%>>
              <input name="T_Consecucion_F<%= oidObjetivo%>_porcentaje_<%= oidEtapa%>" type=text size=4 value="<%=registro.getObject("porcentaje")%>" style="text-align:right" <% if(!actual){%>disabled<%}else{%> onblur="cambiarResultado(<%= calculaResultado%>, this);" ponderacion="<%= reg2.getObject("ponderacion")%>" campoResultado="T_Consecucion_F<%= oidObjetivo%>_resultado" <%}%> <%= readOnly%>>
              
		      <a href="javascript:showComentario('T_Consecucion_F<%= oidObjetivo%>_comentario_<%= oidEtapa%>');">
	             <img src="<%= pathImg%>/detail_off.gif" width=10 height=10 border=0 <%if(registro.getObject("comentario").toString().trim().length() > 0){%>style="color:#FF0000;border: 1px solid #FF0000"<%} %> >
		      </a>
           </td>
      </iterator:iterar>
      
      <td>
         <input name="T_Consecucion_F<%= oidObjetivo%>_oid_objetivo" type=hidden value="<%= oidObjetivo%>">
         <input name="T_Consecucion_F<%= oidObjetivo%>_resultado"  type=text size="5" value="<%= reg2.getObject("resultado")%>" style="text-align:right" readonly>
      </td>

    </tr>
    <tr class="impar" id="tr_<%= oidObjetivo%>" style="DISPLAY: none" expansible="true">
      <td align=center>&nbsp;</td>
      <td colspan="<%= esReto ? (4 + etapas.size()) : (5 + etapas.size()) %>" align=center>
      <br>

      <table width="700"  border=0 cellpadding=0 cellspacing=1 bgcolor="#CCCCCC">

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
          <td rowspan=2 align=center heigth="20"><strong>Fuente de la info</strong></td>
          <td colspan="<%= cuantificadores.size()%>" align=center><strong>Grilla de Cumplimiento </strong></td>
          <td rowspan=2 align=center heigth="20" width="300"><strong>Comentario</strong></td>
        </tr>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>

         <iterator:iterar coleccion="<%=  cuantificadores %>">
	         <td width=50 align=center><strong><%= registro.getObject("descripcion") %></strong></td>
         </iterator:iterar>

        </tr>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
          <td width="100"><%= reg2.getObject("fuente")%></td>

        <iterator:iterar coleccion="<%=  cuantificadores %>">
           <td width="60"><strong><%= reg2.getObject("cuanti_" + registro.getObject("orden"))%></strong></td>
        </iterator:iterar>

	    <td width="350"><%= lcp.print(reg2.getObject("comentario").toString())%></td>
        </tr>
      </table>
     <br>
     </td>
    </tr>

    <%  i ++;
      }%>
  </table>

 <% } %>

  <table width="<%= size %>" border=0 cellspacing=0 cellpadding=0 class="contenido">
    <tr>
      <td align="right">
         <b>Total: &nbsp;</b><input name="total" type=text size="5" style="text-align:right" readonly>&nbsp;&nbsp;
      </td>
    </tr>
  </table>

  <% Registro first= (Registro) (((List) request.getAttribute("CumpGlobal")).get(0));
     Object oid         = first.getObject("oid_val_cumpl");
     Object comentario  = first.getObject("comentario");
  %> 

  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="comentario">
    <tr>
      <th>Comentarios:</th>
    </tr>
    <tr>
      <td><textarea name="comentario_global" cols="95" rows="5" maxlength="4000" <%= readOnly%> onKeyUp="eliminarBoton(<%= usaBotonTerminar%>);return isMaxLength(this);"><%= comentario %></textarea></td>
    </tr>
  </table>

  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="evaluacion">
    <tr>
      <th colspan=<%= valores.size()%>>Marcar la casilla que corresponda</th>
    </tr>
    <tr>
    <% String disabled = "";
       if(usuarioNoDebeCargarCumplimientosTodavia) disabled = "disabled"; %>
    
    <iterator:iterar coleccion="<%=  valores %>">
       <% Object oidVal= registro.getObject("oid_val"); %>
       
      <td align=center valign=top>
	      <table width="85%" border=0 cellspacing=0 cellpadding=0 class="evaluacionFinal" title="<%= registro.getObject("desc_ext")%>">
	        <tr>
	          <td>
                 <input name="valor_global" type="radio" <% if(oidVal.equals(oid)) { %>checked<%}%> value="<%= oidVal%>" <%= readOnly%> <%= disabled%>>
	          </td>
	        </tr>
	        <tr>
	          <td><%= registro.getObject("codigo")%> - <%= registro.getObject("descripcion")%></td>
	        </tr>
	      </table>      
      </td>

    </iterator:iterar>

  </table>

<script language="javascript">
sumarResultado();
</script>

<%@ include file="../util/footer.jsp" %>