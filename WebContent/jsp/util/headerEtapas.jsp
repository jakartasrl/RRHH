<%@ page import="com.jkt.framework.util.Registro"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.jkt.framework.request.ISesion"%>
<%@ page import="com.jkt.top150.varios.bm.Ejercicio"%>

<style type="text/css">
.titulo {
	text-align: left;
	padding: 6px;
	border: 1px solid #999999;
	margin-top: 10px;
}

.titulo th{
	background-image: url(/RRHH/img/fondo.jpg);
	color: #666666;
}

.titulo input{
	font-size:10px;
	font-family: Verdana;
}
</style> 
<script language="javascript" src="<%=request.getContextPath()%>/estilos/showComent.js"></script>
<script language="JavaScript">
function cambiarEjercicio(oidEjer, oidLegEjer){
  nextView  = document.form1.elements['nextView'].value;
  paramsAux = 'FrontServlet?op=CambiarEjercicio&oid_ejer=' + oidEjer + '&nextView=' + nextView + '&oid_leg=' + oidLegEjer;
  window.location = '<%= request.getContextPath()%>/' + paramsAux;
}
</script>

<% List header = (List) request.getAttribute("Header");
   Object evaluado  = "";
   Object evaluador = "";
   Integer ejercicio= new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
   Object etapa     = "";
   Object funcion   = "";
   Object oidLeg    = "0";

   if(header != null && !header.isEmpty()) {
      Registro reg = (Registro) header.get(0);
      evaluado = reg.getObject("evaluado");
      evaluador= reg.getObject("evaluador");
      ejercicio= new Integer(reg.getString("ejercicio"));
      oidLeg   = reg.getString("oid_leg");
      etapa    = reg.getObject("etapa");
      funcion  = reg.getObject("funcion");
   }

   ISesion sesionJKT = (ISesion) request.getSession().getAttribute("SESION");
%>

 <table width="760" border=0 cellspacing="0" cellpadding="0" class="titulo">
   <tr>
      <th><strong onmouseover="showComent(this, 'cabecera', 150, true);">Fijacion de Objetivo</strong></th>
      <th align="right">
      <% Iterator it = Ejercicio.getEjercicios(sesionJKT).iterator();
         while(it.hasNext()){
           Ejercicio next = (Ejercicio) it.next();
      %>
           <strong>
               <a href="javascript:cambiarEjercicio('<%= next.getOID()%>', '<%= oidLeg%>');" 
               <% if(next.getAnio() == ejercicio.intValue()) {%>
                  style="color:#FF0000;border: 1px solid #FF0000">
               <% } else { %>
                  style="color:#000000;">
               <% } %>
               <%= next.getAnio()%> </a>
           </strong>
           &nbsp;
      <% } %>
      </th>
   </tr>
   <tr>
      <td width="300"><strong>Etapa: </strong>&nbsp;&nbsp;<%= etapa %></td>
      <td width="300"><strong>Funcion:</strong>&nbsp;&nbsp;<%= funcion %></td>
   </tr>
   <tr>
      <td width="300"><strong>Usuario: </strong>&nbsp;&nbsp;<%= evaluado  %></td>
      <td width="300"><strong>Evaluador:</strong>&nbsp;&nbsp;<%= evaluador %></td>
   </tr>
</table>

<div id="cabecera" style="visibility: hidden">
  <span>Informa la etapa en la que se encuentra el proceso y que periodo. Esto le permite haciendo "click" en el ano visualizar evaluaciones de otros periodos.</span>
</div> 