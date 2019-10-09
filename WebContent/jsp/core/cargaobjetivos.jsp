<%@ include file="../util/header.jsp" %>
<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 

<% size = 760;
   usaBotonTerminar = true;
   usaBotonImprimir = true;
%>

<script language="JavaScript">
parent.frames['encabezado'].imgOver('cargaobjetivos');

function eliminarTerminar(){
  if (window.event && window.event.keyCode == 13)
       enviar();
  else eliminarBoton(<%= usaBotonTerminar%>);
}
function addRow(idGrupo, cuantificadores, isReto){
  if(<%= readOnly.length() > 5%>) return;
 
  eliminarTerminar();

  var tbl = document.getElementById('TablaGrupo_' + idGrupo);

  var iteration = tbl.rows.length;
  var row = tbl.insertRow(iteration);

  setClassName(row, ((iteration - 3) / 2));

  campo = addCampoText(row, 'T_Objetivo_F' + idGrupo + iteration + '_descripcion',    0, 45);
  campo.setAttribute('maxLength', 255);

  campo = addCampoText(row, 'T_Objetivo_F' + idGrupo + iteration + '_cuantificacion', 1, 5);
  campo.setAttribute('maxLength', 255);
  
  i = 2;

  if(!isReto){
    campo = addCampoText(row, 'T_Objetivo_F' + idGrupo + iteration + '_ponderacion',  i++, 5);
    campo.style.textAlign = 'right';
  }
  
  campo = addCampoHidden(row, 'T_Objetivo_F' + idGrupo + iteration + '_oid_grupo');
  campo.setAttribute('value', idGrupo); 

  addCampoHidden(row, 'T_Objetivo_F' + idGrupo + iteration + '_oid_objetivo');

  for (x=0;x<cuantificadores.length;x++){
     orden = cuantificadores[x];
     addCampoHidden(row, 'T_Objetivo_F' + idGrupo +  iteration + '_pond_oid_'   + orden);
     campo = addCampoText  (row, 'T_Objetivo_F' + idGrupo +  iteration + '_poderacion_' + orden, i + x, 4);
     campo.setAttribute('maxLength', 255);
  }

  //FUENTE DE LA INFO
  row = tbl.insertRow(tbl.rows.length);
  setClassName(row, ((iteration - 3) / 2));

  var cell = row.insertCell(0);  
  cell.setAttribute('align', 'right');

  if(!isReto)
       cell.setAttribute('colSpan',3);
  else cell.setAttribute('colSpan',2);

  cell.innerText = 'Fuente: ';
    
  cell2 = row.insertCell(1);
  cell2.setAttribute('colSpan', cuantificadores.length);
  cell2.setAttribute('align', 'center');
  
  tArea = document.createElement('TEXTAREA');
  tArea.setAttribute('name', 'T_Objetivo_F' + idGrupo + iteration + '_fuente');
  tArea.setAttribute('cols', 55);
  tArea.setAttribute('rows', 2);
  tArea.setAttribute('maxlength', 255);  
  cell2.appendChild(tArea);


  //COMENTARIO  
  row = tbl.insertRow(tbl.rows.length);
  setClassName(row, ((iteration - 3) / 2));

  var cell = row.insertCell(0);  
  cell.setAttribute('align', 'right');

  if(!isReto)
       cell.setAttribute('colSpan',3);
  else cell.setAttribute('colSpan',2);

  cell.innerText = 'Aspectos Cualitativos: ';
    
  cell2 = row.insertCell(1);
  cell2.setAttribute('colSpan', cuantificadores.length);
  cell2.setAttribute('align', 'center');
  
  tArea = document.createElement('TEXTAREA');
  tArea.setAttribute('name', 'T_Objetivo_F' + idGrupo + iteration + '_comentario');
  tArea.setAttribute('cols', 55);
  tArea.setAttribute('rows', 2);
  tArea.setAttribute('maxlength', 255);  
  cell2.appendChild(tArea);
}
function aprobar(){
 try{
   validar();
 }
 catch(e){
   alert(e);
   return;
 }

 var total = new Number(0);
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf('ponderacion') != -1)	   	   
       total += new Number(a.value);
 }

 if(total != 100){
    alert('La suma de las ponderaciones debe ser 100%. Actualmente es ' + total);
    return;
 }
 
 document.form1.elements['aprobado'].value = 1;
 document.forms[0].submit();
}
function validar(){
 validarNoVacio('ponderacion');
 validarNoVacio('descripcion');
 validarNoVacio('cuantificacion');
  
 validarNumeros('ponderacion');
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
</script>

<input type=hidden name="aprobado" value="0">
<input type=hidden name="nextView" value="1">
<input type=hidden name="op" value="SaveObjetivos">
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
       int i = 1;
       Iterator it = grupos.iterator();
       while(it.hasNext()){
          Registro next   = (Registro) it.next();
          Object oidGrupo = next.getObject("oid_grupo");
          boolean esReto  = next.getObject("is_reto").toString().equalsIgnoreCase("true");
          List cuantificadores = (List) request.getAttribute("Cuantificadores_" + oidGrupo);%>

     <script language="JavaScript">
        valores<%= oidGrupo%> = new Array(<%= cuantificadores.size()%>);
    <%    for(int v = 0; v< cuantificadores.size(); v++){%>
        valores<%= oidGrupo%>[<%= v%>] = '<%= ((Registro) cuantificadores.get(v)).getObject("orden")%>';
    <%    } %>
     </script>

  <table width="<%= size%>"  border=0 cellpadding="0" cellspacing="1" class="contenido" id="TablaGrupo_<%= oidGrupo%>">
    <tr>
      <th colspan="<%= esReto ? (3 + cuantificadores.size()) : (4 + cuantificadores.size()) %>">
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td><%= next.getObject("codigo") %>: <%= next.getObject("descripcion") %></td>
            <td align="right"><a href="javascript:addRow('<%= oidGrupo%>', valores<%= oidGrupo%>, <%= esReto%>);">+ Agregar Objetivo</a>&nbsp; </td>
          </tr>
        </table>
      </th>
    </tr>
    <tr class="header">
      <td width="237" rowspan="2">Descripci&oacute;n del Objetivo</td>
      <td width="52" rowspan="2">Cuantifi<br>caci&oacute;n</td>
      <% if(!esReto){%>
         <td width="52" rowspan="2"><p>Ponde<br>raci&oacute;n</p></td>
      <% } %>
      <td colspan="<%= cuantificadores.size()%>">Grilla de Cumplimientos </td>
    </tr>

    <tr class="header">
      <iterator:iterar coleccion="<%=  cuantificadores %>">
	      <td align=center><%= registro.getObject("descripcion")%></td>
      </iterator:iterar>
    </tr>

    <% List objetivos = (List) request.getAttribute("Objetivos_" + next.getObject("oid_grupo")); 
       Iterator it2   = objetivos.iterator();
       while(it2.hasNext()){
          Registro reg2 = (Registro) it2.next();
    %>

	     <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	      <input name="T_Objetivo_F<%= i%>_oid_grupo"    type=hidden value="<%= next.getObject("oid_grupo")%>" <%= readOnly%>>
	      <input name="T_Objetivo_F<%= i%>_oid_objetivo" type=hidden value="<%= reg2.getObject("oid_objetivo")%>" <%= readOnly%>>

	      <td><input name="T_Objetivo_F<%= i%>_descripcion"    type=text value="<%= reg2.getObject("descripcion")%>"    size="45" maxLength="255" onKeyUp="eliminarTerminar();return isMaxLength(this);" <%= readOnly%>></td>
	      <td><input name="T_Objetivo_F<%= i%>_cuantificacion" type=text value="<%= reg2.getObject("cuantificacion")%>" size="5"  maxLength="255" onKeyUp="eliminarTerminar();return isMaxLength(this);" <%= readOnly%>></td>
          <% if(!esReto){%>
  	        <td><input name="T_Objetivo_F<%= i%>_ponderacion"  type=text value="<%= reg2.getObject("ponderacion")%>"    size="5"  maxLength="5"   onKeyUp="eliminarTerminar();" <%= readOnly%> style="text-align:right" <%if(esReto){%>disabled<%}%>></td>
          <% } %>

	      <iterator:iterar coleccion="<%=  cuantificadores %>">
	          <%Object orden = registro.getObject("orden"); %>
	          <td>
		          <input name="T_Objetivo_F<%= i%>_pond_oid_<%= orden%>"   type=hidden value="<%= reg2.getObject("oid_cuanti_" + orden)%>">
		          <input name="T_Objetivo_F<%= i%>_poderacion_<%= orden%>" type=text   value="<%= reg2.getObject("cuanti_" + orden)%>" size="4" maxLength="255" onKeyUp="eliminarTerminar();return isMaxLength(this);" <%= readOnly%>>
	          </td>
	      </iterator:iterar>

	    </tr>

	    <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	      <td align=left></td>
	      <td colspan="<%= esReto ? 1 : 2 %>" align="right">Fuente:</td>
	      <td colspan="<%= cuantificadores.size()%>"><textarea <%= readOnly%> name="T_Objetivo_F<%= i%>_fuente" cols="55" rows="2" maxlength="255" onKeyUp="eliminarBoton(<%= usaBotonTerminar%>); return isMaxLength(this);"><%= reg2.getObject("fuente")%></textarea></td>
	    </tr>

	    <tr class="<%if(i %2 == 0){%>im<%}%>par" align=center>
	      <td align=left>Eliminar: <input name="T_Objetivo_F<%= i%>_baja" type=checkbox <%= readOnly%>></td>
	      <td colspan="<%= esReto ? 1 : 2 %>" align="right">Aspectos Cualitativos:</td>
	      <td colspan="<%= cuantificadores.size()%>"><textarea <%= readOnly%> name="T_Objetivo_F<%= i%>_comentario" cols="55" rows="2" maxlength="255" onKeyUp="eliminarBoton(<%= usaBotonTerminar%>); return isMaxLength(this);"><%= reg2.getObject("comentario")%></textarea></td>
	    </tr>

	      <% i++;%>
      <%}%>
  </table>
  <% } %>

<br>
<br>

<%@ include file="../util/footer.jsp" %>