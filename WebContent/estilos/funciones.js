function isMaxLength(obj){
   var mlength= parseInt(obj.getAttribute("maxlength"));
   if (obj.value.length>mlength)
      obj.value=obj.value.substring(0,mlength)
}
function addRowToTable(tableID){
  var tbl       = document.getElementById(tableID);
  var lastRow   = tbl.rows.length;

  if(checkCodigoDescAnt(lastRow, tableID)){
     alert('Debe ingresar todos los datos antes de agregar elementos');
     return;
  }

  var row = tbl.insertRow(lastRow);
  setClassName(row, lastRow);

  var iteration = lastRow - 1;  
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_codigo', 0, 10);
  addCampoText(row,  'T_' + tableID + '_F' + iteration + '_descripcion', 1, 40);

  addCampoHidden(row,'T_' + tableID + '_F' + iteration + '_oid');
  addCampoActivo(row,'T_' + tableID + '_F' + iteration + '_activo', 2);
}

function checkCodigoDescAnt (lastRow, tableID){
  if(lastRow > 2){
    var posAnt = lastRow - 2;
              
    return (document.forms[0].elements['T_' + tableID + '_F' + posAnt + '_codigo'].value      == '' ||
            document.forms[0].elements['T_' + tableID + '_F' + posAnt + '_descripcion'].value == '');
  }
}

function setClassName(row, index){
  if((index%2) == 0)
	   row.className = "par";
  else row.className = "impar";
}

function addCampoActivo(row, name, pos){
  var checkBox = addCampoCheckBox(row, name, pos);
  checkBox.checked = true;
}

function addCampoCheckBox(row, name, pos){
  var cell = row.insertCell(pos);
  cell.setAttribute('align','center');
  var checkBox = document.createElement('input');
  checkBox.setAttribute('type', 'checkbox');
  checkBox.setAttribute('name', name);
  checkBox.setAttribute('id',   name);
  cell.appendChild(checkBox);
  checkBox.checked = false;
  
  return checkBox;
}
function addCampoText(row, name, pos, size){
  var cell = row.insertCell(pos);
  cell.setAttribute('align','center');
  var text = document.createElement('input');
  text.setAttribute('type', 'text');
  text.setAttribute('name', name);
  text.setAttribute('id',   name);
  text.setAttribute('maxLength', size);
  text.setAttribute('size', size);
  cell.appendChild(text);
  
  return text;
}
function addCampoHidden(row, name){
  var cell = row.cells[0];
  var text = document.createElement('input');
  text.setAttribute('type', 'hidden');
  text.setAttribute('name',  name);
  text.setAttribute('id',    name);
  text.setAttribute('value', '0');
  cell.appendChild(text);
  
  return text;
}

function addImg(row, pos, src){
  var cell = row.insertCell(pos);
  cell.setAttribute('align','center');
  var img = document.createElement('img');
  img.setAttribute('src', src);
  cell.appendChild(img);
  
  return img;
}
function enviar(){
 document.forms[0].submit();
}
function eliminarBoton(value){
  if(!value) return;
  
   try{
     var div   = document.getElementById('divBotones');
     var boton = document.getElementById('b_terminar');
     div.removeChild(boton);
   }
   catch(e){}
}
function trim(s) {
  // Remove leading spaces and carriage returns
  while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r')) 
    s = s.substring(1,s.length); 

  // Remove trailing spaces and carriage returns
  while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r')) 
    s = s.substring(0,s.length-1); 

  return s;
}
function validarNoVacio(nombreCampo){
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf(nombreCampo) != -1){
	   var valor = trim(a.value);

	   if(valor.length == 0)
	      throw 'Campo ' + nombreCampo + ' vacio.';
   }
 }
}
function validarMaxLength(nombreCampo, maxLength){
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if((a.type == 'text' || a.type == 'textarea') && a.name.indexOf(nombreCampo) != -1){
	   var valor = trim(a.value);
	   
	   if(valor.length > maxLength)
	      throw 'El valor del campo ' + nombreCampo + ' es mayor a ' + maxLength + ' caracteres. Valor actual:' + valor.length;
   }
 }
}
function validarNoNumero(valor, nombreCampo){
   if(valor.toUpperCase() == 'NA')
      return;

   var GoodChars = '0123456789.';
   for (i =0; i <= valor.length -1; i++) {
 	  if (GoodChars.indexOf(valor.charAt(i)) == -1) 
		  throw "El numero '" + valor + "' no es valido para el campo " + nombreCampo;
   }
}
function validarNumeros(nombreCampo){
 for(j=0; j< document.forms[0].elements.length;j++){
   var a = document.forms[0].elements[j];
   
   if(a.type == 'text' && a.name.indexOf(nombreCampo) != -1){
	   var valor = a.value;
	   if(valor.toUpperCase() == 'NA')
	      continue;
       
       validarNoNumero(valor, nombreCampo);
   }
 }
}