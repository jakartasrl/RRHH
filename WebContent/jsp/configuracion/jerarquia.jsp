<%@ include file="../util/header.jsp" %>

<% usaBotonAceptar = false; %>

<link rel="StyleSheet" href="<%=request.getContextPath()%>/estilos/tree.css" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/estilos/tree.js"></script>

<link rel="StyleSheet" href="<%=request.getContextPath()%>/estilos/contextmenu.css" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/estilos/contextmenu.js"></script>

<script type="text/javascript">
var oid          = -1;
var windowOpened = false;

function dependencia(){
 var myWindow = window.open('<%=request.getContextPath()%>/FrontServlet?op=TraerLegajosAnioJefe&ninguno=true&evaluador=true',oid,'left=100,top=100,width=300,height=500, scrollbars=yes');
}
function administrador(){
 var myWindow = window.open('<%=request.getContextPath()%>/FrontServlet?op=TraerLegajosAnioJefe&ninguno=true&administrador=true',oid,'left=100,top=100,width=300,height=500, scrollbars=yes');
}
function setOID(x){
  if(!windowOpened)
  	  oid = x;
}
function menuClosed(){
  windowOpened = false;
}
function menuOpened(){
  windowOpened = true;
}
</script>

    <ul id="CM1" class="SimpleContextMenu">
        <li><a href="javascript:dependencia();">Modificar dependencia</a></li>
    </ul>

    <ul id="CM2" class="SimpleContextMenu">
        <li><a href="javascript:dependencia();">Modificar dependencia</a></li>
        <li><a href="javascript:administrador();">Asignar administrador de gerencia</a></li>
    </ul>

<div align=center>

  <table width="600"  border=0 cellspacing="1" cellpadding="0" class="contenido" >
    <tr>
      <th >
        <table width="100%"  border=0 cellspacing="0" cellpadding="0">
          <tr>
            <td align=left>Jerarquia de Legajos </td>
          </tr>
        </table>
       </th>
    </tr>
	<tr>
      <td align=left>
         <div class="tree">

         <script language="Javascript">
           SimpleContextMenu.setup({'preventDefault':false, 'preventForms':false});
           SimpleContextMenu.attach('conMenu',  'CM1');
           SimpleContextMenu.attach('conMenuX', 'CM2');

		   var Tree = new Array;
           
       <%  List legajos = (List) request.getAttribute("Legajos");
           int i = 0;
           Iterator it = legajos.iterator();
           while(it.hasNext()){
              Registro next  = (Registro) it.next();
              Object oid     = next.getObject("oid_leg");
              Object oidPadre= next.getObject("oid_eval");
              Object leg     = next.getObject("legajo");
              Object admin   = next.getObject("administrador");
              Object ap      = next.getObject("ape");
              Object nom     = next.getObject("nom");
              String desc    = leg + "-" + ap + ", " + nom + admin; %>
              
           Tree[<%= i++%>]  = "<%= oid %>|<%= oidPadre %>|<%= desc %> |#";
       <%  } %>
       
           createTree(Tree);
         </script>
        </div>
      </td>
    </tr>


  </table>

<%@ include file="../util/footer.jsp" %>