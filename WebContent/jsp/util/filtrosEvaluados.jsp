<%@ taglib uri="/WEB-INF/iterator.tld" prefix="iterator"%> 
<%@ page import="java.util.List"%>
<%@ page import="com.jkt.framework.request.ISesion"%>

<% List evaluadores = (List) request.getAttribute("Evaluadores");  
   ISesion sesionJKT = (ISesion) request.getSession().getAttribute("SESION"); %>

<script language="Javascript">
function checkEnter(){
  if (window.event && window.event.keyCode == 13)
     enviar();
}

function showFiltros() {
  var theRow = document.getElementById("tablaFiltros");

 (theRow.style.display == "") ? theRow.style.display = "none" : theRow.style.display = "";
}
function limpiar(){
  document.forms[0].nombres.value = '';
  document.forms[0].apellido.value = '';
  document.forms[0].legajo.value = '';
  document.forms[0].oid_evaluador.selectedIndex = 0;
}

</script>
  <p>

  <table width="<%= pageContext.getAttribute("size")!= null? pageContext.getAttribute("size"):new Integer(760)%>" border=0 cellspacing="1" cellpadding="0" class="contenido">
    <tr>
     <th>
      <table width="100%"  border=0 cellspacing="0" cellpadding="0">
        <tr>
           <td align=left>
             <a href="javascript:showFiltros();">Filtros</a>
           </td>
           <td align=right>
             <a href="javascript:limpiar();">Limpiar</a>-           
             <a href="javascript:enviar();">Buscar</a>
           </td>
        </tr>
      </table>
     </th>
    </tr>
    <% String nombres  = (String) (request.getParameter("nombres") == null ? sesionJKT.getAttribute("nombres") : request.getParameter("nombres"));
	   String apellido = (String) (request.getParameter("apellido")== null ? sesionJKT.getAttribute("apellido"): request.getParameter("apellido"));
       String legajo   = (String) (request.getParameter("legajo")  == null ? sesionJKT.getAttribute("legajo")  : request.getParameter("legajo"));
       Object oidEval  = request.getParameter("oid_evaluador")     == null ? sesionJKT.getAttribute("oid_evaluador") : request.getParameter("oid_evaluador");
       
       if(oidEval != null)
      	  oidEval = new Integer(oidEval.toString());

       if(nombres != null && nombres.trim().length() == 0) 
      	  nombres = null;
       
       if(apellido != null && apellido.trim().length() == 0) 
      	  apellido = null;

       if(legajo != null && legajo.trim().length() == 0) 
      	  legajo = null;
       
       if(oidEval != null && ((Integer) oidEval).intValue() < 0) 
      	  oidEval = null;

       boolean huboEleccion = (nombres != null || apellido != null || legajo != null || oidEval != null);
    %>
    
    <tr <%if(!huboEleccion){ %>style="DISPLAY: none" <%} %> expansible="true" id="tablaFiltros">
      <td colspan="2">
        <table width="760" border=0 cellpadding="0" cellspacing="1">
          <tr>
            <td width="15%"><strong>Nombres:</strong></td>
            <td width="35%"><input name="nombres"  type=text value="<% if(nombres != null){%><%=nombres %><%}%>" style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
            <td width="15%"><strong>Apellido:</strong></td>
            <td width="35%"><input name="apellido" type=text value="<% if(apellido!= null){%><%=apellido %><%}%>" style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
          </tr>
          <tr>
            <td width="15%"><strong>Legajo:&nbsp;&nbsp;&nbsp;</strong></td>
            <td width="35%"><input name="legajo" type=text value="<% if(legajo != null){%><%=legajo %><%}%>" style="text-align:left; width: 200px" onKeyPress="checkEnter();"></td>
            <td width="15%"><strong>Evaluador:&nbsp;&nbsp;&nbsp;</strong></td>
            <td width="35%">
               <select name="oid_evaluador" STYLE="width: 200px">
                  <option value="-1">---------------------------------</option>

                  <iterator:iterar coleccion="<%=  evaluadores %>">
                      <option value="<%= registro.getObject("oid_eval")%>" <% if(registro.getInteger("oid_eval").equals(oidEval)){%>selected <%} %> ><%= registro.getObject("nombre")%></option>
                  </iterator:iterar>
               </select>
            </td>
          </tr>
        </table>
      </td>
     </tr>
  </table>
  
  <p>