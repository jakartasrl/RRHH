<%@ include file="header.jsp" %>

<div align=center>
  <table width=350 border=0 cellspacing=1 cellpadding=0 class=contenido>
    <tr>
      <th>Comentario</th>
    </tr>
    <tr>
      <td><textarea name="comentario" cols="50" rows="5" maxlenght="50"></textarea></td>
    </tr>
  </table>
  <table width=300 border=0 cellspacing=0 cellpadding=0 class=botones>
    <tr>
      <td align=center><img src="<%=pathImg%>/botones/b_aceptar.gif" width="60" height=20 onclick="grabarComentario();"></td>
    </tr>
  </table>
  <br>
</div>

<script language="JavaScript">
var campoReal = window.opener.document.forms[0].elements[self.name];

document.forms[0].elements['comentario'].value = campoReal.value;
if(campoReal.disabled)
   document.forms[0].elements['comentario'].disabled = true;

function grabarComentario(){
 var length = document.forms[0].elements['comentario'].value.length;
 if(length > 4000){
    alert('El comentario no puede superar los 4000 caracteres. Actualmente tiene ' + length);
    return -1
 }
  
 window.opener.document.forms[0].elements[self.name].value = document.forms[0].elements['comentario'].value;
 window.close();
}
</script>

</form>
</body>
</html>