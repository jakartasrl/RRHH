<%@ page import="com.jkt.framework.request.BeanError"%>

<% BeanError bean = (BeanError) request.getAttribute("ERROR");
   if(bean != null) { %>
	<table border=0 cellpadding="0" cellspacing="2" width="760" class="TablaError" align=center>
	  <tr >
    	<td>Error: <%= bean.getException().getMessage()%> </td>
	  </tr>
	</table>
    <p></p>
<% } %>