<%@include file="../util/header.jsp" %>
<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 

<% boolean esEvaluado= request.getAttribute("evaluado") != null;
   
   List valoresCapacidad = (List) request.getAttribute("TResumen");
   Registro reg = (Registro) valoresCapacidad.get(0);

   List valoresResumen   = (List) request.getAttribute("ValoresResumen");

   usaBotonTerminar = true;
   usaBotonImprimir = true;
%>

<script language="JavaScript">
parent.frames['encabezado'].imgOver('resumen');

function aprobar(){
 document.form1.elements['aprobado'].value = 1;
 enviar();
}
function addRow(id, acceso){
  if(!acceso) return;

  eliminarBoton(true);

  var tbl = document.getElementById(id);
  var lastRow = tbl.rows.length;
  // if there's no header row in the table, then iteration = lastRow + 1
  var iteration = lastRow;
  var row = tbl.insertRow(lastRow);

  setClassName(row, iteration);

  var cell = row.insertCell(0);
  cell.setAttribute('colSpan', 2);
  cell.setAttribute('align', 'center');

  var campoHidden = addCampoHidden(row, 'T_Concepto_F' + id + '_' + iteration + '_oid_leg_varios');
  campoHidden.setAttribute('value', 0);
  
  tArea = document.createElement('TEXTAREA');
  tArea.setAttribute('name', 'T_Concepto_F' + id + '_' + iteration + '_comentario');
  tArea.setAttribute('cols', 140);
  tArea.setAttribute('rows', 10);
  cell.appendChild(tArea);
}
</script>

<% size = 760;

%>

<input type=hidden name="aprobado" value="0">
<input type=hidden name="nextView" value="4">
<input type=hidden name="oid_leg" value="<%= request.getParameter("oid_leg") %>">
<input type=hidden name="op" value="SaveLegajoVarios">

<jsp:include 
     page="/jsp/util/tablaError.jsp"
     flush="true"
/>

<div align=center>

   <jsp:include 
        page="/jsp/util/headerEtapas.jsp"
        flush="true"
   />
   
	 <table width="<%= size %>" border=0 cellspacing="0" cellpadding="0" class="titulo">
	   <tr>
	      <th align="left" colspan="2">Resumen</th>
	   </tr>
	   <tr>
	      <td><strong>Evaluacion de Cumplimiento Global: </strong>&nbsp;&nbsp;<%= reg.getObject("valor_cump_glob") %></td>
	      <td><strong>Evaluacion de Capacidades:</strong>&nbsp;&nbsp;<%= reg.getObject("valor_cap_glob") %></td>
	   </tr>
	   <tr>
	      <td><strong>Resultado Total: </strong>&nbsp;&nbsp;<%= reg.getObject("valor_cumplimiento")  %></td>
	      <td><strong>Valor de Mapeo :</strong>&nbsp;&nbsp;<%= reg.getObject("valor_mapeo") %></td>
	   </tr>
	</table>

    <br>
    <br>
    
  <table width="<%= size%>" border=0 cellspacing=0 cellpadding=0 class="evaluacion">
    <tr>
      <th colspan="5">Valoraci&oacute;n Global de Resumen</th>
    </tr>
    <tr>

    <iterator:iterar coleccion="<%= valoresResumen%>">

      <td align=center valign="top">
       <table width="90%"  border=0 cellspacing=0 cellpadding=0 title="<%= registro.getObject("desc_ext")%>">
        <tr>
          <td>
             <input name="valor_res_global" type="radio" <%if(esEvaluado){%>disabled<%}%> <%= readOnly%> value="<%= registro.getObject("oid_valor")%>" <% if(registro.getObject("oid_valor").toString().equals(registro.getObject("oid_valor_global").toString())){ %> checked <% } %>>
          </td>
        </tr>
        <tr>
          <td><%= registro.getObject("codigo")%> - <%= registro.getObject("descripcion")%></td>
        </tr>
       </table>
      </td>

    </iterator:iterar>

    </tr>
  </table>


<% List conceptos = (List) request.getAttribute("Conceptos");
   Iterator it = conceptos.iterator();
   while(it.hasNext()){
      Registro next      = (Registro) it.next();
      Object oid         = next.getObject("oid_conc");
      boolean tEvaluador = next.getObject("tipo_evaluador").toString().equalsIgnoreCase("true"); 
      boolean acceso     = (!tEvaluador && esEvaluado) || (tEvaluador && !esEvaluado);
%>

  <table width="<%= size%>"  border=0 cellpadding=0 cellspacing=1 class="contenido" id="<%= oid%>">
    <tr>
      <th>
        <table width="100%"  border=0 cellspacing=0 cellpadding=0>
          <tr>
            <td>
                <%= next.getObject("codigo") %>: <%= next.getObject("descripcion") %>
                <input type=hidden name="T_Concepto_F<%= oid%>_oid_conc" value="<%= oid%>">
            </td>
            <td align="right"><a href="javascript:addRow('<%= oid%>', <%= acceso%>);">+ Agregar Elemento</a>&nbsp; </td>
          </tr>
        </table>
      </th>
    </tr>

  <% List comentarios = (List) request.getAttribute("Comentarios_" + oid);
     int i = 1;%>

   <iterator:iterar coleccion="<%=  comentarios %>">
   
     <tr align=center>
        <td colspan="2">
           <input type=hidden name="T_Concepto_F<%= oid%>_<%= i%>_oid_leg_varios" value="<%= registro.getObject("oid_varios")%>">
           <textarea name="T_Concepto_F<%= oid%>_<%= i%>_comentario" cols="140" rows="10" maxlength="1000" onKeyUp="eliminarBoton(true);return isMaxLength(this);" <% if(!acceso){%>disabled<%}%>><%= registro.getObject("comentario") %></textarea>
        </td>
      </tr>
      
      <% i++; %>

   </iterator:iterar>

  </table>

<% } %>

<% if(esEvaluado) readOnly= ""; %>

<%@ include file="../util/footer.jsp" %>