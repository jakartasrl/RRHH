<%@ include file="../util/header.jsp" %>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="com.jkt.top150.seguridad.bm.UsuarioRRHH"%>
<%@ page import="com.jkt.top150.objetivos.bm.Etapa"%>

<% size = 760;
   ISesion sesionJKT = (ISesion) request.getSession().getAttribute("SESION");
   UsuarioRRHH user  = (UsuarioRRHH) sesionJKT.getLogin().getUsuario();

   List evaluados = (List) request.getAttribute("Evaluados");    
   Etapa etapa    = Etapa.getEtapaActual(sesionJKT);
%>

<script type="text/javascript">
function showEstados(){
}
function save(){
  document.forms[0].op.value='SaveEstadosEvaluados';
  enviar();
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
  <input type=hidden name="op" value="ShowEstadosEvaluados">

  <p>
  <jsp:include 
       page="/jsp/util/tablaError.jsp"
       flush="true"
  />

  <jsp:include 
       page="/jsp/util/filtrosEvaluados.jsp"
       flush="true"
  />

    <%if(!evaluados.isEmpty()) { %> 
          <div align=center>
        	 <display:table name="Evaluados" id="reg" class="contenido" border="0" cellpadding="0" cellspacing="1" requestURI="/FrontServlet?op=VerInbox" pagesize="20" width="760">
		      <display:column title="Legajo"    align="center" sortable="true" class="cabecera"><%= ((Registro) reg).getObject("legajo") %></display:column>
		      <display:column title="Nombre"    align="center" sortable="true" class="cabecera"><%= ((Registro) reg).getObject("nombre") %></display:column>
		      <display:column title="Evaluador" align="center" sortable="true" class="cabecera"><%= ((Registro) reg).getObject("evaluador") %></display:column>

		      <display:column title="Estados" class="top" align="center">
                   <% String oidLegEje = ((Registro) reg).getObject("oid_leg_eje").toString();
                      String nameDet   = "Estados_" + ((Registro) reg).getObject("oid_leg");%>

               	   <display:table name="<%= nameDet%>" id="child" class="detalle">
               	     <% String tarea     = ((Registro)child).getObject("titulo").toString(); 
               	        boolean disabled = false;   
               	     
               	        if(tarea.equalsIgnoreCase("Objetivos") && !etapa.isCargaObjetivo())
               	      	   disabled = true;
               	        
               	        if(tarea.equalsIgnoreCase("Cumplimientos") && !etapa.isCargaCumplimiento())
              	      	   disabled = true;

               	        if(tarea.equalsIgnoreCase("Competencias") && !etapa.isEvaluaCapacidades())
               	      	   disabled = true;
               	     %>

                     <display:column title="Tarea"   class="detalle"><b><%= tarea %></b></display:column>
                   	 <display:column title="Estado"  class="detalle"><%= ((Registro)child).getObject("valor") %></display:column>
                   	 <display:column title="Proximo" class="detalle">

                        <input type=hidden name="T_Estados_F<%= oidLegEje%>_oid_leg_ejer" value="<%= oidLegEje %>">
                        <div align="right">
                          <select name="T_Estados_F<%= oidLegEje %>_<%= ((Registro)child).getObject("titulo")%>" STYLE="width: 120px" <%if(disabled){%> disabled <%} %>>
                            <option value="0">------------------</option>
                            <option value="1">ACTIVO</option>
                            <option value="2">FIN EVALUADO</option>
                            <option value="3">FIN EVALUADOR</option>
                            <option value="4">FIN PLANEAMIENTO</option>
                            <option value="5">CERRADO</option>
                         </select>
                        </div>

                   	 </display:column>
                   </display:table>
                        
              </display:column>

     	     </display:table>
          </div>
    <% } %>

<br>

  <table width="<%= size %>" border=0 cellspacing="0" cellpadding="0" class="botones">
    <tr>
      <td>
        <div align=center id="divBotones">
          <img src="<%=pathImg%>/botones/b_aceptar.gif" width=60 height=20 onclick="javascript:save();">
          <img src="<%=pathImg%>/botones/b_cancelar.gif" width=60 height=20 onclick="javascript:document.location.reload();" title="Cancela la carga y actualiza los datos">
        </div>
      </td>
    </tr>
  </table>
  <br>

</div>
</form>
</body>
</html>