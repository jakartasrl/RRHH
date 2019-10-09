<html>
<head>
<title>Top 180</title>

<meta http-equiv="expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<link href="<%=request.getContextPath()%>/estilos/estilos.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/estilos/funciones.js"></script>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="form1" action="<%=request.getContextPath()%>/FrontServlet" method="POST">

<script language="Javascript">
function tratarKeyPress(){
  if (window.event && window.event.keyCode == 13)
      enviar();
}
function enviar(){
 if(document.getElementById('LDAP').value == 'true')
    document.forms[0].action = 'j_security_check';
    
 document.forms[0].submit();
}
</script>

<input type=hidden name="op" value="Login">

<div align=center>

<jsp:include 
     page="/jsp/util/tablaError.jsp"
     flush="true"
/>

<br>

<table>
  <tr>
    <th style="font-size: 20; color: #55A">
      Bienvenido al proceso de de fijaci&oacute;n y evaluaci&oacute;n de objetivos
    </th>
  </tr>
  <tr>
    <th style="font-size: 20; color: #55A">
      del Grupo Top 180
    </th>
  </tr>
  <tr>
    <th style="font-size: 20; color: #55A">&nbsp;</th>
  </tr>
  <tr>
    <th style="font-size: 14; color: #55A">
     <b>Para acceder a la herramienta por favor ingrese su usuario y clave de red del dominio 25 de Mayo.</b>
    </th>
  </tr>
  <tr>
    <th style="font-size: 14; color: #55A">
     <b>El sistema lo llevar&aacute; inicialmente a la bandeja de entrada desde donde deber&aacute; seleccionar </b>
    </th>
  </tr>
  <tr>
    <th style="font-size: 14; color: #55A">
     <b>el legajo de la persona sobre el que trabajar&aacute;:</b>
    </th>
  </tr>
</table>

<br>
<br>

<table border=0 cellpadding="0" cellspacing="0" width="310" height="216" align="center">
    <tr>
  <td ><img border=0 src="<%=request.getContextPath()%>/img/login/caja_login_top.gif" width="310" height="23" alt="Ingrese desde aquí"></td>
    </tr>
    <tr>
     <td valign="top" align="center" bgcolor="#D0D7E4">
         <div align=center>
          <%if(request.getParameter("err")!=null){%>
             	<FONT color="red"><b>El usuario o clave ingresados son inválidos o el usuario se encuentra bloqueado</b></FONT>
          <%}%>

            <table border=0 cellpadding="14" cellspacing="0" width="300">
               <tr>
                  <td colspan="2" bgcolor="#D0D7E4" align=center><b>Para comenzar a operar, por favor ingrese:</b>
                  </td>
               </tr>                    
               <tr>
                  <td align=right bgcolor="#D0D7E4">Su usuario</td>
                  <td align=left bgcolor="#D0D7E4">
                     <input type=text name="j_username" size="15" maxlength="20" onkeypress="javascript:tratarKeyPress();">
                  </td>
               </tr>
               <tr>
                  <td align=right bgcolor="#D0D7E4">Su clave</td>
                  <td align=left bgcolor="#D0D7E4">
                     <input type="Password" name="j_password" size="15" maxlength="15" onkeypress="javascript:tratarKeyPress();">
                  </td>
               </tr>
               <tr>
               <% String param = (String) com.jkt.framework.Aplicacion.getAplicacion().getContext().getServletContext().getInitParameter("ActiveDirectory"); 
                  Boolean value = new Boolean(param); %>

                  <td align=center bgcolor="#D0D7E4" colspan="2">
                     <input type="hidden" name="LDAP" id="LDAP" value="<%= value.booleanValue() %>">
                     <input type="button" name="aceptar" value="Aceptar" onClick="javascript:enviar();">
                  </td>
               </tr>
            </table>
         </div>
       </td>
     </tr>
</table>

<br>
<br>

<table align="right" width="700">
  <tr>
    <th style="font-size: 18; color: #55A">
      Muchas Gracias
    </th>
  </tr>
</table>
</div>
</body>
</html>