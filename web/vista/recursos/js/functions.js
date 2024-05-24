function horaCierreSistema(horaCierre)
{
    horaActual = new Date();
    horaCierreSesion = formatearFecha(horaCierre);
    horaCierreSesion = new Date(horaCierreSesion);
    horaCierreSistemaParsed = new Date();
    horaCierreSistemaParsed.setHours(horaCierreSesion.getHours());
    horaCierreSistemaParsed.setMinutes(horaCierreSesion.getMinutes());
    horaCierreSistemaParsed.setSeconds(horaCierreSesion.getSeconds());
    tiempoRestanteSistema = horaCierreSistemaParsed.getTime() - horaActual.getTime();

    if (tiempoRestanteSistema <= 0) {
        tiempoRestanteSistema = 1000;
    }

    return tiempoRestanteSistema;
}

function cierraSistema() {
    clearActiveItems();
    document.getElementById("frmMenu:btn_TimeOut").click();
}

function formatearFecha(horaCierre) {
    var f = new Date();
    var MM = ((f.getMonth() + 1) < 10 ? '0' : '')
            + (f.getMonth() + 1);
    fecha = f.getFullYear() + "/" + MM + "/" + f.getDate() + " " + horaCierre;

    return fecha;
}