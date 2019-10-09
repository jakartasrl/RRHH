<%@ include file="../util/header.jsp" %>

<%@ page import="com.jkt.top150.seguridad.bm.UsuarioRRHH"%>
<%@ page import="com.jkt.top150.legajos.bm.Legajo"%>
<%@ page import="com.jkt.framework.request.ISesion"%>
<%@ page import="com.jkt.top150.objetivos.bm.Etapa"%>

<% size = 760;
   ISesion sesion = (ISesion) request.getSession().getAttribute("SESION");

   UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario();
   Legajo leg   = user.getLegajo();

   Etapa etapa = Etapa.getEtapaActual(sesion);
%>

<script language="JavaScript">
var myWindow;
function windowOpen() {
  if(<%= !etapa.isCargaObjetivo() %> ) return;

  url = '<%=request.getContextPath()%>/FrontServlet?op=VerEvaluadores';
  myWindow = window.open(url,'VerEvaluadores','left=100,top=100,width=300,height=500, scrollbars=yes');
}
function grabarCampos(){
 myWindow.document.form1.oid.value        = "oidEvaluador";
 myWindow.document.form1.codigo.value     = "codEvaluador";
 myWindow.document.form1.descripcion.value= "descEvaluador";
}
</script>

<form name="form1" action="<%=request.getContextPath()%>/FrontServlet">

<input type=hidden name="op" value="SaveEvaluador">
<input type=hidden name="oid" value="">
<input type=hidden name="codigo" value="">
<input type=hidden name="descripcion" value="">

<jsp:include 
     page="/jsp/util/tablaError.jsp"
     flush="true"
/>

<div align=center>
  <table width="760"  border=0 cellspacing="1" cellpadding="0" class="contenido">
    <tr>
      <th width="169" colspan="5">Evaluado</th>
      <th colspan="2">Evaluador</th>
    </tr>
    <tr>
      <td width="169"><strong>Apellido:</strong></td>
      <td colspan="4"><%= leg.getApellidoPaterno()%> </td>
      <td width="150"><a href="javascript:windowOpen();"><strong>Evaluador:</strong></a></td>
      <td width="300">
       <% if(user.getLegajo().tieneAsignadoUnEvaluador())  {%>
            <input type=hidden name="oidEvaluador" value="<%= user.getLegajo().getLegajoEjer().getEvaluador().getOID()%>">
            <input type=text name="codEvaluador"  READONLY size=10 value="<%= user.getLegajo().getLegajoEjer().getEvaluador().getLegajo().getLegajo()%>">
            <input type=text name="descEvaluador" READONLY size=40 value="<%= user.getLegajo().getLegajoEjer().getEvaluador().getLegajo().getApNom() %>">
       <% } else { %>

            <input type=hidden name="oidEvaluador">
            <input type=text name="codEvaluador" READONLY size=10>
            <input type=text name="descEvaluador" READONLY size=40>
       <% } %>
      </td>
    </tr>
    <tr>
      <td height="15"><strong>Nombre: </strong></td>
      <td colspan="4"><%= leg.getNombres()%></td>
      <td height="15"><strong>Nro Empleado:</strong></td>
      <td><%= leg.getLegajo()%></td>
    </tr>
  </table>

<%@ include file="../util/footer.jsp" %>