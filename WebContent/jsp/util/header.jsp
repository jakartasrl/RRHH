<%@ page import="com.jkt.framework.util.Registro"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.jkt.framework.request.BeanError"%>
<%@ page import="com.jkt.framework.request.ISesion"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
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

<% boolean usaBotonAceptar  = true;
   boolean usaBotonCancelar = true;
   boolean usaBotonTerminar = false;
   boolean usaBotonImprimir = false;
   String  versionImprimible= null;

   int size                 = 600;
   
   String pathImg           = request.getContextPath() + "/img";

   String readOnly = "";
   if(request.getAttribute("ReadOnly") != null)
      readOnly = "disabled";
      
   Object doReload = request.getAttribute("ReloadEncabezado");
%>

<script language="Javascript">
<% if(doReload != null) { 
    ISesion sesionJKT   = (ISesion) request.getSession().getAttribute("SESION");
    sesionJKT.putAttribute("doReaload", new Integer(1));
%>
    parent.frames['encabezado'].document.location.reload();
<% } %>
</script>