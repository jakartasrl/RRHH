<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
    <title>Top 180</title>
	<meta http-equiv="Expires" content="-1" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
	<link href="<%=request.getContextPath()%>/estilos/estilos.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=request.getContextPath()%>/estilos/funciones.js"></script>

</head>

<frameset rows="100,*" frameborder="NO" border=0 framespacing="0">
  <frame src="<%= request.getContextPath()%>/FrontServlet?op=VerEncabezado" name="encabezado" scrolling="NO" noresize>
  <frame name="main">
</frameset>
<noframes><body>
</body></noframes>
</html>
