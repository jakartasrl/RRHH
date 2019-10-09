<%@ include file="../util/header.jsp" %>
<%@ page import="com.jkt.top150.varios.bl.LongCharPrinter"%>
<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 

<% size             = 760;
   usaBotonAceptar  = false;
   usaBotonCancelar = false;
   usaBotonImprimir = true;
%>

<script language="JavaScript">
parent.frames['encabezado'].imgOver('objetivos');

function sumarResultado(){
 document.form1.elements['total'].value = 0;
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'hidden'){
      if(a.name.indexOf('_resultado') != -1){
         var valor = a.value * 1;
         document.form1.elements['total'].value = (document.form1.elements['total'].value * 1) + valor;
       }
   }
 }
}
</SCRIPT>

<input type=hidden name="nextView" value="2">
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
          boolean esReto  = next.getObject("is_reto").toString().equalsIgnoreCase("true");
          List cuantificadores = (List) request.getAttribute("Cuantificadores_" + next.getObject("oid_grupo"));
          List objetivos       = (List) request.getAttribute("Objetivos_" + next.getObject("oid_grupo"));

          if(objetivos.isEmpty()) continue; %>

  <table width="<%= size%>" border=0 cellspacing=1 cellpadding=0 class="contenido">
    <tr>
      <th colspan="<%= esReto ? 3 : 4 %>"><%= next.getObject("codigo") %>: <%= next.getObject("descripcion") %></th>
    </tr>

    <% Iterator it2   = objetivos.iterator();
       while(it2.hasNext()){
          Registro reg2 = (Registro) it2.next();
          Object oidObjetivo   = reg2.getObject("oid_objetivo");
    %>

       	 <tr class="header">
            <td align="center">Descripci&oacute;n del Objetivo</td>
            <td align="center">Cuantificaci&oacute;n</td>
        <% if(!esReto){%><td align="center"><p>Ponderaci&oacute;n</p></td><% } %>
            <td align="center">Resultado</td>
         </tr>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	      <td width="345" align=left heigth="25"><%= reg2.getObject("descripcion") %> </td>
	      <td heigth="25"><%= reg2.getObject("cuantificacion")%></td>
          <% if(!esReto){%><td heigth="25"><%= reg2.getObject("ponderacion")%></td><% } %>
          <td heigth="25"><%= reg2.getObject("resultado")%> <input type="hidden" name="<%= oidObjetivo%>_resultado" value="<%= reg2.getObject("resultado")%>"> </td>
         </tr>
    
    <tr class="<%if(i %2 == 0){%>im<%}%>par" id="tr_<%= oidObjetivo%>">
      <td colspan="<%= esReto ? (3 + etapas.size()) : (4 + etapas.size()) %>" align=center>
      <br>

      <table width="725" border=0 cellpadding=0 cellspacing=1 bgcolor="#CCCCCC">
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
          <td rowspan=2 align=center heigth="20"><strong>Fuente de la info</strong></td>
          <td colspan="<%= cuantificadores.size()%>" align=center><strong>Grilla de Cumplimiento </strong></td>
          <td rowspan=2 align=center heigth="20" width="300"><strong>Comentario</strong></td>
        </tr>

        <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
        
        <iterator:iterar coleccion="<%=  cuantificadores %>">
            <td width="50" align=center><strong><%= registro.getObject("descripcion")%></strong></td>
        </iterator:iterar>

        </tr>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
          <td width="100"><%= reg2.getObject("fuente")%></td>

          <iterator:iterar coleccion="<%=  cuantificadores %>">
             <td width="60"><strong><%= reg2.getObject("cuanti_" + registro.getObject("orden"))%></strong></td>
          </iterator:iterar>

		  <td width="350"><%= reg2.getObject("comentario") %></td>
        </tr>
      </table>
      <br>
     </td>
    </tr> 

    <tr class="<%if(i %2 == 0){%>im<%}%>par" id="tr_<%= oidObjetivo%>">
     <td colspan="<%= esReto ? 3 : 4  %>" align=center>
      <br>
      <table width="725" border=0 cellpadding=0 cellspacing=1 bgcolor="#CCCCCC">
	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
          <td align=center heigth="20"><strong>Etapa</strong></td>
          <td align=center heigth="20"><strong>Porcentaje</strong></td>
          <td align=center heigth="20" width="360"><strong>Comentario</strong></td>
        </tr>

     <% List cumplimientos = (List) request.getAttribute("Cumplimiento_" + oidObjetivo); %>
        <iterator:iterar coleccion="<%=  cumplimientos %>">
           <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
             <td align=left><%=registro.getObject("desc_etapa")%></td>
             <td align=center><%=registro.getObject("porcentaje")%></td>
             <td align=left><%=registro.getObject("comentario")%></td>
           </tr>
        </iterator:iterar>
        
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
         <b>Total: </b><input name="total" type=text size="5" style="text-align:right" readonly>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
  </table>

  <% Registro first= (Registro) (((List) request.getAttribute("CumpGlobal")).get(0));
     Object oid         = first.getObject("oid_val_cumpl");
     Object comentario  = first.getObject("comentario");
     List valores  = (List) request.getAttribute("ValCumpGlobal");
  %> 

  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="comentario">
    <tr>
      <th>Comentarios:</th>
    </tr>
    <tr>
      <td><%= comentario %></td>
    </tr>
  </table>

  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="evaluacion">
    <tr>
      <th colspan=<%= valores.size()%>>Marcar la casilla que corresponda</th>
    </tr>
    <tr>
    
    <iterator:iterar coleccion="<%=  valores %>">
       <% Object oidVal= registro.getObject("oid_val"); %>
       
      <td align=center valign=top>
	      <table width="85%" border=0 cellspacing=0 cellpadding=0 class="evaluacionFinal" title="<%= registro.getObject("desc_ext")%>">
	        <tr>
	          <td>
                 <input name="valor_global" type="radio" <% if(oidVal.equals(oid)) { %>checked<%}%> value="<%= oidVal%>" <%= readOnly%>>
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