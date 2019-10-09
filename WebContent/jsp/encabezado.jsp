<%@ include file="util/header.jsp" %>

<%@ page import="com.jkt.top150.seguridad.bm.UsuarioRRHH"%>
<%@ page import="com.jkt.top150.objetivos.bm.Etapa"%>

<script language="Javascript">
<% ISesion sesionJKT   = (ISesion) request.getSession().getAttribute("SESION");
   Object marcaResumen = request.getAttribute("SinAccesoAResumen");

   UsuarioRRHH user  = (UsuarioRRHH) sesionJKT.getLogin().getUsuario();

   String oidLeg          = request.getParameter("oid_leg");
   if(oidLeg == null){
     if(user.tieneAsigandoUnLegajo())
          oidLeg = "" + user.getLegajo().getOID();
     else oidLeg = "-1";
   }

   Etapa etapa       = Etapa.getEtapaActual(sesionJKT);

   boolean cargaObjetivo     = etapa.isCargaObjetivo();
   boolean cargaCumplimiento = etapa.isCargaCumplimiento();
   boolean evaluaCapacidades = etapa.isEvaluaCapacidades();
   boolean cargaResumen      = etapa.isCargaResumen();

   String nextOper           = (String) sesionJKT.getAttribute("nextOper");

   if(nextOper != null) { %>
     parent.frames['main'].document.location.href = "<%= request.getContextPath() + nextOper%>";
<%   sesionJKT.remove("nextOper");
   }
   else {
        if(sesionJKT.getAttribute("doReaload") != null)
          sesionJKT.remove("doReload");
        else{ %>

        parent.frames['main'].document.location.href = "<%= request.getContextPath() %>/FrontServlet?op=VerInbox";
<%      }
   } %>
function imgOver(name){
  ponerEnBlanco(name);

  document.images[name].src  = '<%= pathImg%>/encabezado/' + name + '_r.PNG';
}
function mostrarError(msg){
  window.status = msg;
  alert(msg);
  window.status = '';
}
function ponerEnBlanco(aName){
  for(i = 0; i<document.images.length; i++){
      if(document.images[i].name == '') continue;
      
      var name = document.images[i].name;
      
      if(name == aName)
           document.images[i].src  = '<%= pathImg%>/encabezado/' + name + '_r.PNG';
      else document.images[i].src  = '<%= pathImg%>/encabezado/' + name + '.PNG';
  }
}
function imgOnclick(name, url, validarLegajoAsignado){
  ponerEnBlanco(name);
  
  if(validarLegajoAsignado){
    var oidLegValue = document.form1.elements['oid_leg'].value;
    if(oidLegValue == -1){
       mostrarError('Usted no tiene un legajo configurado');
       return;
    }
  }

  parent.frames['main'].location.href = '<%= request.getContextPath()%>/FrontServlet?op=' + url + '&oid_leg=' + oidLegValue;
}
function irCargaObjetivos(){
  if(!<%= cargaObjetivo%>){
     mostrarError('En esta etapa del proceso no se encuentra disponible la carga de objetivos');
     return;
  }
  
  imgOnclick('cargaobjetivos', 'CargaObjetivos', true);
}
function irAvanceObjetivos(){
  if(!<%= cargaCumplimiento%>){
     mostrarError('En esta etapa del proceso no se encuentra disponible la carga de cumplimientos');
     return;
  }

  imgOnclick('objetivos', 'AvanceObjetivos', true);
}
function irEvaluaciones(){
  if(!<%= evaluaCapacidades%>){
     mostrarError('En esta etapa del proceso no se encuentra disponible la evaluacion de capacidades');
     return;
  }
  
  if(<%= (marcaResumen != null && etapa.isEvaluaCapacidades())%>){//no se puede acceder en DICIEMBRE
     mostrarError('Usted no tiene acceso a las competencias ni al resumen hasta que su evaluador finalize la carga');
     return;
  }

  imgOnclick('competencias', 'CargarEvaluaciones', true);
}
function irResumen(){
  if(!<%= cargaResumen%>){
     mostrarError('En esta etapa del proceso no se encuentra disponible el resumen de la evaluacion');
     return;
  }

  if(<%= (marcaResumen != null)%>){
     mostrarError('Usted no tiene acceso a las competencias ni al resumen hasta que su evaluador finalize la carga');
     return;
  }

  imgOnclick('resumen', 'ArmarGrafico', true);
}
function irInbox(){
  imgOnclick('inbox', 'VerInbox', false);
}
function logOut(){
  self.location.href = "<%= request.getContextPath()%>/FrontServlet?op=LogOut";
}
</script>

<input type=hidden name="oid_leg" value="<%= oidLeg%>">

<div align=right>
  <a href="javascript:logOut();"><b>Logout</b></a>
</div>

<div align=center>
   <table width="760"  border=0 cellspacing="0" cellpadding="0">
    <tr>
       <td align=left bgcolor="EFEFEF">&nbsp;<img src="<%= pathImg%>/logo.gif" width="172" height="44"></td>
       <td align="right" bgcolor="EFEFEF"><img src="<%= pathImg%>/encab2.jpg" width="328" height="55"></td>
    </tr>
   </table>

   <table width="760"  border=0 cellspacing="0" cellpadding="0">
     <tr>
       <td><img name="portada" src="<%= pathImg%>/encabezado/portada.PNG" onclick="javascript:imgOnclick('portada', 'VerPortada', true);"></td>
       <td><img name="cargaobjetivos" src="<%= pathImg%>/encabezado/cargaobjetivos.PNG" onclick="javascript:irCargaObjetivos();"></td>
       <td><img name="objetivos" src="<%= pathImg%>/encabezado/objetivos.PNG" onclick="javascript:irAvanceObjetivos();"></td>
       <td><img name="competencias" src="<%= pathImg%>/encabezado/competencias.PNG" onclick="javascript:irEvaluaciones();"></td>
       <td><img name="resumen" src="<%= pathImg%>/encabezado/resumen.PNG" onclick="javascript:irResumen();"></td>
       <td><img name="inbox" src="<%= pathImg%>/encabezado/inbox.PNG" onclick="javascript:irInbox();"></td>
     </tr>
   </table>

</div>

</form>
</body>
</html>