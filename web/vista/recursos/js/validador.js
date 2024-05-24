/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : validador.js
 * TIPO        : Archivo Java Script
 * PAQUETE     : 
 * CREADO      : 20210904
 * MODIFICADO  : 20210904
 * AUTOR       : j.delatorre
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
window.onload = function () {
    var controls = document.getElementsByTagName("*");
    var regEx = new RegExp("(^| )disable( |$)");
    for (var i = 0; i < controls.length; i++) {
        if (regEx.test(controls[i].className)) {
            AttachEvent(controls[i], "copy");
            AttachEvent(controls[i], "paste");
            AttachEvent(controls[i], "cut");
        }
    }
};

function onValidaEntradaNumerica(campo, campoNombre){
    const campoConFormato = campo.value;
    const campoSinFormato = campoConFormato.replaceAll(',', '');
    if (isNaN(campoSinFormato)){
        var msj = "Error el campo .:: " + campoNombre + " ::. no puede contener caracteres alfabéticos";
        alert (msj);
        campo.value = 0;
        campo.focus();
    }
}
function AttachEvent(control, eventName) {
    if (control.addEventListener) {
        control.addEventListener(eventName, function (e) { e.preventDefault(); }, false);
    } else if (control.attachEvent) {
        control.attachEvent('on' + eventName, function () { return false; });
    }
}

function onValidarNumeros(e) {
   var key = window.Event ? e.which : e.keyCode;
   return ((key >= 48 && key <= 57) ||(key===8))
 }
 
 function onReemplazarCeroAlPrincipio(e){
    var valor = e.value.replace(/^0*/, '');
    e.value = valor;
 }