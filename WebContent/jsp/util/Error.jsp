<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/JavaScript">
function MM_preloadimg() { //v3.0
  var d=document; if(d.img){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadimg.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
</script>

<% BeanError bean      = (BeanError) request.getAttribute("ERROR");
   Exception exception = bean.getException();
   size = 0;
%>

<table width="500" border=0 align=center cellpadding="0" cellspacing="0" class="TablaComun">
    <tr>
  	  <td>&nbsp;</td>
    </tr>
	<tr align=center>
	  <td height="68"  id="BordesH" ><img src="<%=pathImg%>/logo.gif"></td>
	</tr>
	<tr>
	  <td height="5"></td>
	</tr>
	<tr>
	  <td height="10"></td>
	</tr>
	<tr>
	  <td height="25"></td>
	</tr>
	<tr>
	  <td>
	  <table width="100%" class="TablaForm"><tr><td>
	  <table width="100%" class="TablaError">
	  	  <tr>
	  	  	<Th align=left>Excepcion:</th>
	  	  	<td align=left><%=exception.getClass().getName()%></td>
	  	  </tr>
          <tr>
            <th><br></th>
            <td><br></tr>
          </tr>
	  	  <tr>
	  	  	<Th align=left>Mensage:</th>
	  	  	<td align=left><%=exception.getMessage()%></td>
	  	  </tr>
          <tr>
            <th><br></th>
            <td><br></tr>
          </tr>
	  	  <tr>
	  	  	<Th align=left>URL Requerida:</th>
	  	  	<td align=left><%=request.getAttribute("javax.servlet.forward.request_uri") + "?" + request.getAttribute("javax.servlet.forward.query_string")%></td>
	  	  </tr>
	  </table></td></tr></table>
	  </td>
	</tr>
	<tr>
	  <td height="5"></td>
	</tr>
	<tr>
	  <td height="5"></td>
	</tr>
	<tr >
	  <td height="25"></td>
	</tr>
	<tr  >
	  <td height="30" id="BordeB">&nbsp;</td>
	</tr>
	<tr >
	  <td height="5"></td>
	</tr>
</table>

<%@ include file="footer.jsp" %>