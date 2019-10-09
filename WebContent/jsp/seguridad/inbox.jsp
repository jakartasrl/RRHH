<%@ include file="../util/header.jsp" %>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="com.jkt.top150.seguridad.bm.UsuarioRRHH"%>

<% size = 760;
   ISesion sesionJKT = (ISesion) request.getSession().getAttribute("SESION");
   UsuarioRRHH user  = (UsuarioRRHH) sesionJKT.getLogin().getUsuario();

   List evaluados = (List) request.getAttribute("Evaluados"); 
   String order   = (String)  sesionJKT.getAttribute("order");
   if(order == null)
   	  order = "ascending";
   
   Integer col    = (Integer) sesionJKT.getAttribute("colNumber");
   if(col == null)
   	  col = new Integer(3);
   
%>
<script language="Javascript">
parent.frames['encabezado'].imgOver('inbox');

function verDatosLegajoElegido(oidLeg){
  parent.frames['encabezado'].location.href = '<%= request.getContextPath()%>/FrontServlet?op=VerEncabezado&oid_leg=' + oidLeg;
}
function showEstados(){
 showComent(this, 'estados', 150, true);
}
function showTipos(){
 showComent(this, 'tipos', 150, true);
}
function col(aValue){
  if(aValue != <%= col.intValue() %>){
    window.location.href = "<%= request.getContextPath()%>/FrontServlet?op=VerInbox&col=" + aValue;
    return;
  }

  var orden = 'ascending';
  if("<%= order %>" == orden)
     orden  = 'descending';

  window.location.href = "<%= request.getContextPath()%>/FrontServlet?op=VerInbox&col=" + aValue + "&order=" + orden;
}
</script>
<style type="text/css">
table.borders{
  font-family: Verdana;
  border: 1px solid #ccc;
  border-collapse: collapse;
  font-size: 10px;
  width: 98%;
}
td.cabecera{
  font-family: Verdana;
  border: 1px solid #ccc;
  text-align: center;
}
td.top{
  font-family: Verdana;
  vertical-align: top;
}
table.detalle {
  font-family: Verdana;
  align:  center;
  text-align: center;
  border: 1px solid #ccc;
  width: 98%;
}
td.detalle{
  font-family: Verdana;
  font-weight: bold;
  height: 15px;
  font-size: 10px;
  text-align: center;
}
</style>

<div align=center>
  <input type=hidden name="op" value="VerInbox">
  <input type=hidden name="nextView" value="5">

  <jsp:include 
       page="/jsp/util/headerEtapas.jsp"
       flush="true"
  />
  <p>
  <jsp:include 
       page="/jsp/util/tablaError.jsp"
       flush="true"
  />

  <jsp:include 
       page="/jsp/util/filtrosEvaluados.jsp"
       flush="true"
  />

  <% if(request.getAttribute("FinalizoEvaluado") != null){%>
  <table border=0 cellpadding="0" cellspacing="2" width="<%= size%>" class="TablaError" align=center>
     <tr >
       <td>Usted termino la autoevaluacion. El formulario fue enviado a su evaluador para finalizar el proceso</td>
	 </tr>
  </table>
  <p>
  <% } %>

  <table width="<%= size%>"  border=0 cellpadding="0" cellspacing="1" class="contenido" id="Inbox">
    <tr>
      <th colspan="2">Inbox</th> 
    </tr>
    <%if(!evaluados.isEmpty()) { %> 
      <tr>
        <td colspan="2">
          <div align=center>
        	 <display:table name="Evaluados" id="reg" class="borders" requestURI="/FrontServlet?op=VerInbox" pagesize="20" defaultsort="<%= col.intValue() %>" defaultorder="<%= order %>">
		      <display:column title="<a href='javascript:col(1);'>Periodo</a>"   sortable="true" class="cabecera"><%= ((Registro) reg).getObject("periodo") %></display:column>
		      <display:column title="<a href='javascript:col(2);'>Legajo</a>"    sortable="true" class="cabecera"><a href="javascript:verDatosLegajoElegido(<%= ((Registro) reg).getObject("oid_leg")%>);"><%= ((Registro) reg).getObject("legajo") %></a></display:column>
		      <display:column title="<a href='javascript:col(3);'>Nombre</a>"    sortable="true" class="cabecera"><%= ((Registro) reg).getObject("nombre") %></display:column>
		      <display:column title="<a href='javascript:col(4);'>Evaluador</a>" sortable="true" class="cabecera"><%= ((Registro) reg).getObject("evaluador") %></display:column>

		      <display:column title="Estados" class="top">
                   <% String nameDet = "Estados_" + ((Registro) reg).getObject("oid_leg");%>

               	   <display:table name="<%= nameDet%>" id="child" class="detalle">
                     <display:column title="Tarea"  class="detalle"><%= ((Registro)child).getObject("titulo") %></display:column>
                   	 <display:column title="Estado" class="detalle"><%= ((Registro)child).getObject("valor") %></display:column>
                   </display:table>
                        
              </display:column>

     	     </display:table>
          </div>
        </td>
     </tr>
    <% } %>
  </table>
  
  <br>
  <br>

  <% if(user.isConfigurador()) {%>

  <table width="<%= size%>"  border=0 cellspacing="1" cellpadding="0" class="contenido">
    <tr>
      <th>Tareas</th>
    </tr>
    <tr class="header" id="izq">
      <td><strong>Administracion</strong></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarEtapas">Carga de Etapas</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=ShowEtapasEjercicio">Carga de Ejercicios</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=ShowEstadosEvaluados&nombres=&apellido=&legajo=&oid_evaluador=-1">Modificacion de Estados</a></td>
    </tr>
    <tr><td>&nbsp;</td></tr>

    <%if(user.isPlanificacion() || user.isRRHH()){ %>
       <tr class="header" id="izq">
          <td><strong>Consultas</strong></td>
       </tr>
      <tr>
          <td><a href="<%= request.getContextPath()%>/FrontServlet?op=InicioConsultaObjetivos">Consulta de Objetivos</a></td>
	  </tr>
	  <tr>
	      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=InicioConsultaAvanceObjetivos">Consulta de Avance Objetivos</a></td>
	  </tr>
	  <tr>
	      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=InicioComparacionResumen">Comparacion de Resultados</a></td>
	  </tr>
	  <tr>
	      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=TraerLegajosAnio">Jerarquia de Legajos</a></td>
	  </tr>
      <tr><td>&nbsp;</td></tr>
    <%}%>

    <tr class="header" id="izq">
      <td><strong>Configuracion</strong></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarGrupoObjetivo">Carga de Dimensiones</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarValCumpl">Carga de Valores de Cumplimiento</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarValoresCapacidad">Carga de Valores de Capacidad</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarValoresResumen">Carga de Valores del Resumen</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarCapacidades">Carga de Capacidades</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarLegajos">Carga de legajos</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarPaises">Carga de Paises</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarEntidades">Carga de Entidades</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargarConceptosVarios">Carga de Conceptos Varios</a></td>
    </tr>
    <tr>
      <td><a href="<%= request.getContextPath()%>/FrontServlet?op=CargaUsuarios">Carga Usuarios</a></td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
  <% } %>

<% size = 0; %>

</div>

<div id="estados" style="visibility: hidden">
  <span>Informa el estado en que se encuentra el proceso en cada etapa.<br/>
        <b>Activo:</b> la evaluacion se encuentra para iniciar el proceso.<br />
        <b>Fin evaluado:</b> El evaluado a finalizado su etapa y se encuentra disponible para que el evaluador pueda validar/modificar/evaluar <br />
        <b>Fin evaluador:</b> El evaluado a finalizado su etapa y se encuentra disponible para que planeamiento valide los objetivos <br />
        <b>Fin planeamiento:</b> Planeamiento a validado los objetivos y sus resultados. Se encuentra disponible para que el evaluador puedo concluir el proceso. <br />
        <b>Cerrado:</b> Una vez que termina el ejercicio y todas las etapas fueron finalizadas por cada rol. <br />
  </span>
</div> 

<div id="tipos" style="visibility: hidden">
  <span><b>Carga de objetivos:</b> Informa sobre el estado del proceso en la etapa de Fijacion de Objetivos.<br />
        <b>Cumplimientos:</b> Informa sobre el estado del proceso de revision y evaluacion de objetivos <br />
        <b>Competencias:</b> Informa sobre el etado del proceso de evaluacion de competencias <br />
  </span>
</div> 

<%@ include file="../util/footer.jsp" %>