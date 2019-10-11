
<% if(size != 0) { 
%>
  <table width="<%= size %>" border=0 cellspacing="0" cellpadding="0" class="botones">
    <tr>
      <td>
        <div align=center id="divBotones">
          <% if(usaBotonAceptar) {%><img src="<%=pathImg%>/botones/b_aceptar.gif" width=60 height=20 <%if(readOnly.length() > 5){%> disabled <%}else{%> onclick="javascript:enviar();" <%}%> title="Sirve para seguir modificando datos hasta finalizar"><%}%>
          <% if(usaBotonCancelar) {%><img src="<%=pathImg%>/botones/b_cancelar.gif" width=60 height=20 onclick="javascript:document.location.reload();" title="Cancela la carga y actualiza los datos"><%}%>
          <% if(usaBotonTerminar) {%><img src="<%=pathImg%>/botones/b_finalizar.gif" width=60 height=20 id="b_terminar" <%if(readOnly.length() > 5){%> disabled <%}else{%> onclick="javascript:aprobar();" <%}%> title="Una vez finalizado no se podra volver a editar los datos"><%}%>
          <% if(usaBotonImprimir) {%><img src="<%=pathImg%>/botones/b_imprimir.gif"  id="b_imprimir" onclick="javascript:window.print();" title="Imprime la pagina"><%}%>
          <% if(versionImprimible != null) {%><img src="<%=pathImg%>/botones/b_vimprimible.gif" id="b_vimprimible" onclick="window.location.href='<%= versionImprimible%>'" title="Imprime la pagina actual"><%}%>
        </div>
      </td>
    </tr>
  </table>
  <br>

<% } %>
</div>
</form>
</body>
</html>