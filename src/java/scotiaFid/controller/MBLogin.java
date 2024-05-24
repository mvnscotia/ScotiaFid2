/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : MBLogin.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controler
 * CREADO      : 20210306
 * MODIFICADO  : 20220512 
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210716.- Se implementa de manera correcta el SSO
 *               20210820.- Se implementa mensaje timeOut mediante el uso de idleMonitor
 *               20210904.- Se elimina el usi de idleMonitor como auxiliar al TimeOut
 *               20220512.- Se agrega el servicio RolValidate
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
//import java.util.UUID;
import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.context.RequestContext;

import scotiaFid.bean.ClaveBean;
import scotiaFid.bean.FechaBean;
import scotiaFid.bean.SeguridadPantallaBean;
//import scotiaFid.bean.UserSessionBean;
import scotiaFid.bean.UsuarioBean;
import scotiaFid.bean.UsuarioLoginBean;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CFecha;
import scotiaFid.dao.CUsuario;
import scotiaFid.util.CSingleSignOn;
import scotiaFid.dao.CSeguridad;
import scotiaFid.util.LogsContext;

@ManagedBean(name = "mbLogin")
@ViewScoped
public class MBLogin implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBLogin.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean validacion;
    private String destinoURL;
    private String mensajeError;
    private String nombreObjeto;
    private String nombre;
    private String firmado;
    private String rol;

    private FacesMessage mensaje;
    private Class<?> classLogin;

    private FechaBean fecha;
    private UsuarioBean usuario;

    private CClave oClave;
    private CFecha oFecha;
    private CUsuario oUsuario;
    private CSeguridad oSeguridad;

    private CSingleSignOn oSSO;

    private login oLogin;
    private loginMonitorInActividad oLoginMon;

    private Calendar cal;
    private static SimpleDateFormat formatoFecha;
    private static SimpleDateFormat formatoHora;
    private static SimpleDateFormat formatoHoraCompleta;
    private HttpServletRequest peticion;
    private HttpSession sesion;

    private List<String> listaErrores;
    private List<SeguridadPantallaBean> listaTrans;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    *   LISTAS BLANCAS
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    enum Features {
        LogsTraza,
        FID_LOG_TRAZA,
        LogsTrazaErr,
        FID_LOG_ERR,
        Logs_Service_Web,
        Fid_Traspaso_Chequera,
        Logs_Login,
        Fid_Login,
        log;
    }
    private static final String PUNTO = ".";

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @ManagedProperty(value = "#{beanUsuarioLogin}")
    private UsuarioLoginBean usuarioLogin;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<String> listaRol;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public String getFirmado() {
        return firmado;
    }

    public List<String> getListaRol() {
        return listaRol;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public void setUsuarioLogin(UsuarioLoginBean usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    class loginMonitorInActividad {

        public loginMonitorInActividad() {
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbLogin.loginMonitorInActividad.";
        }

        public void onLoginMonitor_Activa() {
            //logger.info("Se activo a las: " + LocalDateTime.now());
            //System.out.println("Valor del timeOut: " + String.valueOf(idleMntr.getFacetCount()));
        }

        public void onLoginMonitor_Desactiva() {
            try {
                //logger.info("Se llegó al TimeOut");
                //logger.info("Se desactivo a las: " + LocalDateTime.now());
                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "La sesión ha expirado, se recarga la página");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                RequestContext.getCurrentInstance().execute("alert('No se detectó actividad. Presione F5 para recargar página');");
            } catch (IOException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onLoginMonitor_Desactiva()";
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }

        }
    }

    class login {

        public login() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoHora = new SimpleDateFormat("hh:mm");
            formatoHoraCompleta = new SimpleDateFormat("hh:mm:ss");
            listaErrores = new ArrayList<>();
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.";
            nombreObjeto = "Fuente: scotiafid.controller.mbLogin.login.";
            validacion = Boolean.TRUE;
            classLogin = MBLogin.class;
            LogsContext.FormatoNormativo();
        }

        public void onLogin_SesionInicia()  {
            ClaveBean clave690 = new ClaveBean(690, 100, new String(), 0.00, 0.00, new String(), null, null, new String());
            try {

                oClave = new CClave();
                oClave.onClave_ObtenClave(clave690);
                oClave = null;
                
                boolean inDaylightTime = TimeZone.getDefault().inDaylightTime(new Date());

                cal = Calendar.getInstance();
                
                int hh = cal.get(Calendar.HOUR_OF_DAY);
                int mm = cal.get(Calendar.MINUTE);
                int ss = cal.get(Calendar.SECOND);
                
                if(inDaylightTime) {
                    hh = hh - 1;
                }
                Date horaLim = formatHoraParse(clave690.getClaveDesc());
                Date horaAct = formatoHoraCompletaParse(String.valueOf(hh).concat(":").concat(String.valueOf(mm)).concat(":").concat(String.valueOf(ss)));
//                GeneraPathHomeLogs();
                if (!horaAct.after(horaLim)) {
                    peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    sesion = peticion.getSession(Boolean.FALSE);

                    logger.info(sesion.getServletContext().getContextPath() + ": Proceso de inicio de sesión");
                    usuarioLogin.setUsuarioLoginScotiaId(sesion.getAttribute("WIN_USER").toString());

                    usuarioLogin.setUsuarioLoginIP(peticion.getRemoteAddr());
                    usuarioLogin.setUsuarioLoginHoraMax(clave690.getClaveDesc());
                    
                    
                    if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginToken") != null) {
                        listaRol = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listadoRoles");
                        usuarioLogin.setUsuarioLoginNumero(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginNumero").toString()));
                        usuarioLogin.setUsuarioLoginToken(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginToken").toString());
                        usuarioLogin.setUsuarioLoginFirmado(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginFirmado").toString());
                        usuarioLogin.setUsuarioLoginNombre(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginNombre").toString());
                        usuarioLogin.setUsuarioLoginPlaza(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginPlaza").toString());
                        RequestContext.getCurrentInstance().execute("dlgUsuarioRol.show();");
                    } else {
                        oSSO = new CSingleSignOn();
                        listaRol = new ArrayList<>();
                        if (oSSO.onSSO_SesionInicia(usuarioLogin, listaRol, listaErrores).equals(Boolean.TRUE)) {
                            RequestContext.getCurrentInstance().execute("dlgUsuarioRol.show();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                            if (oSSO.getMensajeErrorSSO().contains("104086") || oSSO.getMensajeErrorSSO().contains("104088")) {
                                RequestContext.getCurrentInstance().execute("dlgCierre.show();");
                            }
                        }
                        oSSO = null;
                    }

                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Esta fuera de horario");
                    logger.info("Se intento acceder al sistema en fuera de horario");
                }

            } catch (Exception Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onLogin_SesionInicia()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onLogin_SesionIniciaSinSSO() {

            peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sesion = peticion.getSession(Boolean.FALSE);

            listaRol = new ArrayList<>();
            listaRol.add("347 GENERAL FIDUCIARIO");
//                listaRol.add("348 CONSULTA FIDUCIARIO");
//                listaRol.add("349 ADM SIST FIDUCIARIO");
//                listaRol.add("350 JURIDICO FIDUCIARIO");
//                listaRol.add("351 DIRECCION FIDUCIARIO");
//                listaRol.add("352 PROMOCION FIDUCIARIO");
//                listaRol.add("353 ADMON FIDUCIARIO");
//                listaRol.add("354 ADM CONT FIDUCIARIO");
//                listaRol.add("355 ADM INV FIDUCIARIO");
//                listaRol.add("356 ADM HON FIDUCIARIO");
//                listaRol.add("357 CONTAB FIDUCIARIO");
//                listaRol.add("358 OP CONT FIDUCIARIO");
//                listaRol.add("359 FISCAL FIDUCIARIO");
//                listaRol.add("360 ATN INV FIDUCIARIO");
//                listaRol.add("361 ATN BIENES FIDUCIARIO");
//                listaRol.add("389 ADMON S");
//                listaRol.add("1680 CONCILIACIONES DE CHEQUES");
//                listaRol.add("1689 CONSULTA POSICIONES");

            usuarioLogin.setUsuarioLoginScotiaId("mexeo\\s6367397");
            usuarioLogin.setUsuarioLoginIP("127.0.0.1");
            usuarioLogin.setUsuarioLoginHoraMax("23:59");
            // HABILITAR PARA PRUEBAS DEV
            usuarioLogin.setUsuarioLoginNombre("RUBEN USCANGA");
            usuarioLogin.setUsuarioLoginNumero(8932506);
//            usuarioLogin.setUsuarioLoginNombre("OMAR PRUEBAS");
//            usuarioLogin.setUsuarioLoginNumero(9003649);
            // HABILIDAR PARA PRUEBAS EN LOCAL
//                            usuarioLogin.setUsuarioLoginNombre("SERGIO GONZALEZ DIAZ");
//                 usuarioLogin.setUsuarioLoginNumero(1024027);

            RequestContext.getCurrentInstance().execute("dlgUsuarioRol.show();");

        }

        public void onLogin_SesionSeleccionaRol()  {
            ClaveBean cveTimeOut = new ClaveBean(690, 260, new String(), 0.00, 0.00, new String(), null, null, new String());
            try {
                oUsuario = new CUsuario();
                if (oUsuario.onUsuario_ExisteUsuario(usuarioLogin.getUsuarioLoginNumero())) {
                    if (oUsuario.onUsuario_UsuarioActivo(usuarioLogin.getUsuarioLoginNumero())) {
                        ClaveBean clave690 = new ClaveBean(690, 100, new String(), 0.00, 0.00, new String(), null, null, new String());
                        oClave = new CClave();
                        oClave.onClave_ObtenClave(clave690);
                        oClave = null;
                        boolean inDaylightTime = TimeZone.getDefault().inDaylightTime(new Date());

                        cal = Calendar.getInstance();

                        int hh = cal.get(Calendar.HOUR_OF_DAY);
                        int mm = cal.get(Calendar.MINUTE);
                        int ss = cal.get(Calendar.SECOND);

                        if (inDaylightTime) {
                            hh = hh - 1;
                        }
                        Date horaLim = formatHoraParse(clave690.getClaveDesc());
                        Date horaAct = formatoHoraCompletaParse(String.valueOf(hh).concat(":").concat(String.valueOf(mm)).concat(":").concat(String.valueOf(ss)));

                        if (!horaAct.after(horaLim)) {
                            if ((usuarioLogin.getUsuarioLoginRol() != null) && (!usuarioLogin.getUsuarioLoginRol().equals(new String()))) {
                                //logger.info("===============================================================================");
                                //logger.info("Proceso de seleccion de rol");
                                //logger.info("===============================================================================");
                                oSSO = new CSingleSignOn();
                                listaTrans = new ArrayList<>();
                                usuarioLogin.setUsuarioLoginRolId(usuarioLogin.getUsuarioLoginRol().substring(0, usuarioLogin.getUsuarioLoginRol().indexOf(" ")).trim());
                                if (oSSO.onSSO_SesionSeleccionaPerfil(usuarioLogin, listaErrores, listaTrans).equals(Boolean.TRUE)) {
                                    String msjSeleccionaRol = "Rol seleccionado : " + usuarioLogin.getUsuarioLoginRol();
                                    logger.info(msjSeleccionaRol);
                                    //logger.info("usuario Numero (SSO): " + usuarioLogin.getUsuarioLoginNumero());
                                    //logger.info("Se firma con : " + usuarioLogin.getUsuarioLoginNombre());
                                    //logger.info("Rol seleccionado : " + usuarioLogin.getUsuarioLoginRol());

//      PARA AGREGAR PANTALLAS CON OPCION DE SOLO LECTURA
                                    CComunes ccomunes = new CComunes();
                                    int cont = listaTrans.size();
                                    boolean enListaTrans;
                                    String panatallaRelacionada;
                                    for (int i = 0; i < cont; i++) {
                                        List<String> tranSinLectura = ccomunes.getPantallasPorNumClaveByID(listaTrans.get(i).getPantallaId());
                                        if (tranSinLectura.size() > 0) {
                                            for (String pantallaSinLectura : tranSinLectura) {
                                                enListaTrans = false;
                                                for (int x = 0; x < cont; x++) {
                                                    panatallaRelacionada = pantallaSinLectura.trim().split(",")[1];
                                                    if (listaTrans.get(x).getPantallaId().equals(panatallaRelacionada)) {
                                                        enListaTrans = true;
                                                    }
                                                }
                                                if (!enListaTrans) {
                                                    listaTrans.add(new SeguridadPantallaBean(pantallaSinLectura.split(",")[0], ""));
                                                }
                                            }
                                        }
                                    }
//      PARA AGREGAR PANTALLAS CON OPCION DE SOLO LECTURA

                                    oUsuario = new CUsuario();
                                    usuario = oUsuario.onUsuario_ObtenInformacion(usuarioLogin.getUsuarioLoginNumero());
                                    oUsuario = null;

                                    if ((usuario.getUsuarioExiste().equals(Boolean.TRUE)) && (usuario.getUsuarioStatus().equals("ACTIVO"))) {
                                        oClave = new CClave();
                                        oClave.onClave_ObtenClave(cveTimeOut);
                                        oClave = null;
                                        oFecha = new CFecha();
                                        fecha = oFecha.onFecha_ObtenFechaSistema();
                                        oFecha = null;
                                        if (fecha.getFechaCveExisteControla().equals(Boolean.TRUE)) {
                                            peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                                            //Subimos las variables a facesContext  
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNumero", usuario.getUsuarioId());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNombre", usuario.getUsuarioNombre());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPuesto", usuario.getUsuarioPuesto());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTipo", usuario.getUsuarioTipo());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel01", usuario.getUsuarioNivel01());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel02", usuario.getUsuarioNivel02());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel03", usuario.getUsuarioNivel03());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel04", usuario.getUsuarioNivel04());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel05", usuario.getUsuarioNivel05());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza", usuario.getUsuarioPlaza());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTimeOut", cveTimeOut.getClaveDesc());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioFiltroAtn", usuario.getUsuarioFiltroAtn());

                                            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza"    , usuarioLogin.getUsuarioLoginPlaza());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioToken", usuarioLogin.getUsuarioLoginToken());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioScotiaId", usuarioLogin.getUsuarioLoginScotiaId());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioRol", usuarioLogin.getUsuarioLoginRol());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioHoraMax", usuarioLogin.getUsuarioLoginHoraMax());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTerminal", usuarioLogin.getUsuarioLoginIP());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aplicaGarantia", false);

                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaSistema", formatFecha(fecha.getFechaAct()));
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaActIni", formatFecha(fecha.getFechaActIni()));
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSA", formatFecha(fecha.getFechaMSA()));
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAbierto", fecha.getFechaMSAAbierto().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaAño", fecha.getFechaActAño().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMes", fecha.getFechaActMes().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaDia", fecha.getFechaActDia().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAño", fecha.getFechaMSAAño().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAMes", fecha.getFechaMSAMes().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSADia", fecha.getFechaMSADia().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTAño", fecha.getFechaHSTAño().toString());
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTMes", fecha.getFechaHSTMes().toString());

                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaTrans", listaTrans);
                                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appContexto", sesion.getServletContext().getContextPath());
                                            // Se agrega Servicio Rol Validate (INICIO)
                                            // CAVC - 28/04/2022          
                                            if (oSSO.onSSO_RolValidate(usuarioLogin, listaErrores).equals(Boolean.TRUE)) {
                                                destinoURL = sesion.getServletContext().getContextPath().concat("/vista/menuPrincipal.sb");
                                            } else {
                                                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                                            }
                                            // Se agrega Servicio Rol Validate (FIN)
                                            // destinoURL = sesion.getServletContext().getContextPath().concat("/vista/menuPrincipal.sb");                               
                                        } else {
                                            oSeguridad = new CSeguridad();
                                            if (oSeguridad.onSeguridad_ObtenAccesoDiaInhabil(listaTrans).equals(Boolean.TRUE)) {
                                                peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                                                //Subimos las variables a facesContext  
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNumero", usuario.getUsuarioId());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNombre", usuario.getUsuarioNombre());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPuesto", usuario.getUsuarioPuesto());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTipo", usuario.getUsuarioTipo());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel01", usuario.getUsuarioNivel01());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel02", usuario.getUsuarioNivel02());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel03", usuario.getUsuarioNivel03());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel04", usuario.getUsuarioNivel04());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel05", usuario.getUsuarioNivel05());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza", usuario.getUsuarioPlaza());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTimeOut", cveTimeOut.getClaveDesc());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioFiltroAtn", usuario.getUsuarioFiltroAtn());

                                                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza"    , usuarioLogin.getUsuarioLoginPlaza());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioToken", usuarioLogin.getUsuarioLoginToken());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioScotiaId", usuarioLogin.getUsuarioLoginScotiaId());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioRol", usuarioLogin.getUsuarioLoginRol());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioHoraMax", usuarioLogin.getUsuarioLoginHoraMax());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTerminal", usuarioLogin.getUsuarioLoginIP());

                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaSistema", formatFecha(fecha.getFechaAct()));
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaActIni", formatFecha(fecha.getFechaActIni()));
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSA", formatFecha(fecha.getFechaMSA()));
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAbierto", fecha.getFechaMSAAbierto().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaAño", fecha.getFechaActAño().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMes", fecha.getFechaActMes().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaDia", fecha.getFechaActDia().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAño", fecha.getFechaMSAAño().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAMes", fecha.getFechaMSAMes().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSADia", fecha.getFechaMSADia().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTAño", fecha.getFechaHSTAño().toString());
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTMes", fecha.getFechaHSTMes().toString());

                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaTrans", listaTrans);
                                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appContexto", sesion.getServletContext().getContextPath());
                                                //logger.info("ScotiaId que ingresa : " + usuarioLogin.getUsuarioLoginScotiaId());
                                                //logger.info("Horario máximo para laborar : " + usuarioLogin.getUsuarioLoginHoraMax());
                                                //logger.info("ScotiaId que ingresa : " + usuarioLogin.getUsuarioLoginScotiaId());
                                                //logger.info("Horario máximo para laborar : " + usuarioLogin.getUsuarioLoginHoraMax());
                                                //logger.info("Dirección IP del usuario : " + usuarioLogin.getUsuarioLoginIP());
                                                //InetAddress addr = InetAddress.getByName(usuarioLogin.getUsuarioLoginIP());
                                                //logger.info("Hostname del usuario : " + addr.getHostName());
                                                //logger.info("Horario en que accede : " + LocalDateTime.now());
                                                //logger.info("Hostname del servidor: " + InetAddress.getLocalHost().getHostName());
                                                //logger.info("Dirección IP del servidor : " + onObtenerIPUsuario());
                                                destinoURL = sesion.getServletContext().getContextPath().concat("/vista/menuPrincipal.sb");
                                            } else {
                                                destinoURL = "/scotiaFid/vista/fueraHorario.sb";
                                                logger.info("ScotiaFid fuera del horario laboral");
                                            }
                                        }
                                        FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                                    } else {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Usuario no registrado o usuario no activo en scotiaFid");
                                        logger.error("Usuario no registrado o usuario no activo en scotiaFid");
                                    }
                                } else {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                                    logger.error("Se produjo el siguiente mensaje en el servicio SSO: " + oSSO.getMensajeErrorSSO().replace("\n", "") + " onLogin_SesionSeleccionaRol()");
                                }
                                oSSO = null;
                            } else {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Seleccione un rol");
                            }
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "El sistema está fuera de servicio");
                        }
                        //El usuario no está activo
                    } else {
                        //logger.info("El usuario no existe o no se encuentra activo");
                        oSSO = new CSingleSignOn();
                        if (oSSO.onSSO_SesionCierra(usuarioLogin, listaErrores, "Pantalla de Inicio").equals(Boolean.TRUE)) {
                            logger.info("Proceso de Cierre de Sesion Correcto (desde pantalla de bienvenida)");
                            //logger.info("usuario Numero (SSO): " + usuarioLogin.getUsuarioLoginNumero());
                            //logger.info("ScotiaId: " + sesion.getAttribute("WIN_USER").toString());
                            //logger.info("===============================================================================");
                            //logger.info("Proceso de Cierre de Sesion (desde pantalla de bienvenida)");
                            //logger.info("===============================================================================");
                            //logger.info("Horario en que termina su sesión : " + LocalDateTime.now());

                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                        }
                        oSSO = null;

                        RequestContext.getCurrentInstance().execute("dlgUsuarioRol.hide();");
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "El Usuario no se encuentra activo en Fiduciario... Verifique.");
                        logger.error("El Usuario no se encuentra activo en Fiduciario");
                    }
                    //El usuario no existe
                } else {
                    //logger.info("El usuario no existe o no se encuentra activo");
                    oSSO = new CSingleSignOn();
                    if (oSSO.onSSO_SesionCierra(usuarioLogin, listaErrores, "Pantalla de Inicio").equals(Boolean.TRUE)) {
                        logger.info("Proceso de Cierre de Sesion Correcto (desde pantalla de bienvenida)");
                        //logger.info("usuario Numero (SSO): " + usuarioLogin.getUsuarioLoginNumero());
                        //logger.info("ScotiaId: " + sesion.getAttribute("WIN_USER").toString());
                        //logger.info("===============================================================================");
                        //logger.info("Proceso de Cierre de Sesion (desde pantalla de bienvenida)");
                        //logger.info("===============================================================================");
                        //logger.info("Horario en que termina su sesión : " + LocalDateTime.now());

                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                    }
                    oSSO = null;
                    oUsuario = null;

                    RequestContext.getCurrentInstance().execute("dlgUsuarioRol.hide();");
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "El Usuario no se encuentra registrado en Fiduciario... Verifique.");
                    logger.error("El Usuario no se encuentra registrado en Fiduciario");
                }
            } catch (SQLException | IOException  Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onLogin_SesionSeleccionaRol()";
                logger.error(mensajeError);

            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onLogin_SesionSeleccionaRolSinSSO() {
            //ClaveBean cveTimeOut= new ClaveBean(690, 260, new String(), 0.00, 0.00, new String(), null, null, new String());
            ClaveBean cveTimeOut = new ClaveBean(690, 260, "2", 0.00, 0.00, new String(), null, null, new String());
            try {
                if ((usuarioLogin.getUsuarioLoginRol() != null) && (!usuarioLogin.getUsuarioLoginRol().equals(new String()))) {
                    listaTrans = new ArrayList<>();
                    listaTrans.add(new SeguridadPantallaBean("80885310", "CONSULTA DE SALDOS"));

                    listaTrans.add(new SeguridadPantallaBean("80822610", ""));
                    listaTrans.add(new SeguridadPantallaBean("80823010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80823210", ""));
                    listaTrans.add(new SeguridadPantallaBean("80824010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80828810", ""));
                    listaTrans.add(new SeguridadPantallaBean("80891510", ""));
                    listaTrans.add(new SeguridadPantallaBean("80890010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80890010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80841210", ""));
                    listaTrans.add(new SeguridadPantallaBean("80842110", ""));
                    listaTrans.add(new SeguridadPantallaBean("80842010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80842210", ""));
                    listaTrans.add(new SeguridadPantallaBean("80842310", ""));
                    listaTrans.add(new SeguridadPantallaBean("80821410", ""));
                    listaTrans.add(new SeguridadPantallaBean("88888888", ""));
                    listaTrans.add(new SeguridadPantallaBean("80814110", ""));
                    listaTrans.add(new SeguridadPantallaBean("80822210", "FINALIDADES - GARANTIAS"));
                    listaTrans.add(new SeguridadPantallaBean("80822810", "PÓLIZA"));
                    listaTrans.add(new SeguridadPantallaBean("80822410", "BIENES"));
                    listaTrans.add(new SeguridadPantallaBean("80822110", "INSTRUCCIONES"));
                    listaTrans.add(new SeguridadPantallaBean("80823510", "CONSULTAS"));
                    listaTrans.add(new SeguridadPantallaBean("80820010", "APROBACION"));    
                    listaTrans.add(new SeguridadPantallaBean("80825010", ""));
                    listaTrans.add(new SeguridadPantallaBean("80822010", "MENU"));
                    listaTrans.add(new SeguridadPantallaBean("80812510", "Poliza Adm"));
                    listaTrans.add(new SeguridadPantallaBean("80841510", "Parámetro de Honorarios"));
                    listaTrans.add(new SeguridadPantallaBean("80871310", "Parámetro de Honorarios"));
//                    listaTrans.add(new SeguridadPantallaBean("80812511", "Poliza Adm Cons"));
                   
//      PARA AGREGAR PANTALLAS CON OPCION DE SOLO LECTURA
                    CComunes ccomunes = new CComunes();
                    int cont = listaTrans.size();
                    boolean enListaTrans;
                    String panatallaRelacionada;
                    for (int i = 0; i < cont; i++) {
                        List<String> tranSinLectura = ccomunes.getPantallasPorNumClaveByID(listaTrans.get(i).getPantallaId());
                        if (tranSinLectura.size() > 0) {
                            for (String pantallaSinLectura : tranSinLectura) {
                                enListaTrans = false;
                                for (int x = 0; x < cont; x++) {
                                    panatallaRelacionada = pantallaSinLectura.trim().split(",")[1];
                                    if (listaTrans.get(x).getPantallaId().equals(panatallaRelacionada)) {
                                        enListaTrans = true;
                                    }
                                }
                                if (!enListaTrans) {
                                    listaTrans.add(new SeguridadPantallaBean(pantallaSinLectura.split(",")[0], ""));
                                }
                            }
                        }
                    }
//      PARA AGREGAR PANTALLAS CON OPCION DE SOLO LECTURA
                    usuarioLogin.setUsuarioLoginRolId(usuarioLogin.getUsuarioLoginRol().substring(0, usuarioLogin.getUsuarioLoginRol().indexOf(" ")).trim());
                    oUsuario = new CUsuario();
                    usuario = oUsuario.onUsuario_ObtenInformacion(usuarioLogin.getUsuarioLoginNumero());
                    oUsuario = null;
                    if ((usuario.getUsuarioExiste().equals(Boolean.TRUE)) && (usuario.getUsuarioStatus().equals("ACTIVO"))) {
                        oClave = new CClave();
                        oClave.onClave_ObtenClave(cveTimeOut);
                        oClave = null;
                        oFecha = new CFecha();
                        fecha = oFecha.onFecha_ObtenFechaSistema();
                        oFecha = null;
                        if (fecha.getFechaCveExisteControla().equals(Boolean.TRUE)) {
                            peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                            //Subimos las variables a facesContext  
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginToken", "eo2ZRcYnUsfeJzQrqrvpFRH5UGgAFSMo8i3x4wTeq1tHfmIzGemwKhKPYfjbK+YVzoLiCwZ8U4tUvIhoaAsinF1oLqVSOPU2qzP8Mqpfz9EoDEkJkRtH+LSIPg3O7B3N");
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNumero", usuario.getUsuarioId());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNombre", usuario.getUsuarioNombre());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPuesto", usuario.getUsuarioPuesto());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTipo", usuario.getUsuarioTipo());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel01", usuario.getUsuarioNivel01());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel02", usuario.getUsuarioNivel02());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel03", usuario.getUsuarioNivel03());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel04", usuario.getUsuarioNivel04());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel05", usuario.getUsuarioNivel05());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza", usuario.getUsuarioPlaza());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTimeOut", cveTimeOut.getClaveDesc());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioFiltroAtn", usuario.getUsuarioFiltroAtn());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aplicaGarantia", false);

                            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza"    , usuarioLogin.getUsuarioLoginPlaza());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioToken", usuarioLogin.getUsuarioLoginToken());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioScotiaId", usuarioLogin.getUsuarioLoginScotiaId());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioRol", usuarioLogin.getUsuarioLoginRol());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioHoraMax", usuarioLogin.getUsuarioLoginHoraMax());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTerminal", usuarioLogin.getUsuarioLoginIP());

                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaSistema", formatFecha(fecha.getFechaAct()));
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaActIni", formatFecha(fecha.getFechaActIni()));
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSA", formatFecha(fecha.getFechaMSA()));
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAbierto", fecha.getFechaMSAAbierto().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaAño", fecha.getFechaActAño().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMes", fecha.getFechaActMes().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaDia", fecha.getFechaActDia().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAño", fecha.getFechaMSAAño().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAMes", fecha.getFechaMSAMes().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSADia", fecha.getFechaMSADia().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTAño", fecha.getFechaHSTAño().toString());
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTMes", fecha.getFechaHSTMes().toString());

                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaTrans", listaTrans);
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appContexto", sesion.getServletContext().getContextPath());
                           
                            destinoURL = sesion.getServletContext().getContextPath().concat("/vista/menuPrincipal.sb");
                        } else {
                            if (usuarioLogin.getUsuarioLoginRolId().equals("347") || usuarioLogin.getUsuarioLoginRolId().equals("349")) {
                                peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                                //Subimos las variables a facesContext  
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNumero", usuario.getUsuarioId());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNombre", usuario.getUsuarioNombre());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPuesto", usuario.getUsuarioPuesto());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTipo", usuario.getUsuarioTipo());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel01", usuario.getUsuarioNivel01());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel02", usuario.getUsuarioNivel02());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel03", usuario.getUsuarioNivel03());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel04", usuario.getUsuarioNivel04());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioNivel05", usuario.getUsuarioNivel05());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza", usuario.getUsuarioPlaza());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTimeOut", cveTimeOut.getClaveDesc());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioFiltroAtn", usuario.getUsuarioFiltroAtn());

                                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPlaza"    , usuarioLogin.getUsuarioLoginPlaza());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioToken", usuarioLogin.getUsuarioLoginToken());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioScotiaId", usuarioLogin.getUsuarioLoginScotiaId());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioRol", usuarioLogin.getUsuarioLoginRol());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioHoraMax", usuarioLogin.getUsuarioLoginHoraMax());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioTerminal", usuarioLogin.getUsuarioLoginIP());

                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaSistema", formatFecha(fecha.getFechaAct()));
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaActIni", formatFecha(fecha.getFechaActIni()));
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSA", formatFecha(fecha.getFechaMSA()));
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAbierto", fecha.getFechaMSAAbierto().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaAño", fecha.getFechaActAño().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMes", fecha.getFechaActMes().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaDia", fecha.getFechaActDia().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAAño", fecha.getFechaMSAAño().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSAMes", fecha.getFechaMSAMes().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaMSADia", fecha.getFechaMSADia().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTAño", fecha.getFechaHSTAño().toString());
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fechaHSTMes", fecha.getFechaHSTMes().toString());

                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaTrans", listaTrans);
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appContexto", sesion.getServletContext().getContextPath());
                                destinoURL = sesion.getServletContext().getContextPath().concat("/vista/menuPrincipal.sb");
                            } else {
                                destinoURL = "/scotiaFid/vista/fueraHorario.sb";
                                //logger.info("ScotiaFid fuera del horario laboral");
                            }
                        }
                        FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Usuario no registrado o usuario no activo en scotiaFid");
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Seleccione un rol");
                }
            } catch (SQLException | IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onLogin_SesionSeleccionaRolSinSSO()";
                //logger.info(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onLogin_SesionCierra()  {
            try {
                //logger.info("===============================================================================");
                //logger.info("Proceso de Cierre de Sesion (desde pantalla de bienvenida)");
                //logger.info("===============================================================================");
                //logger.info("Horario en que termina su sesión : " + LocalDateTime.now());

                oSSO = new CSingleSignOn();
                if (oSSO.onSSO_SesionCierra(usuarioLogin, listaErrores, "Pantalla de Inicio").equals(Boolean.TRUE)) {
                    logger.info("Proceso de Cierre de Sesion Correcto (desde pantalla de bienvenida)");
                    destinoURL = "/scotiaFid/inicio.sb?faces-redirect=true";
                    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                    logger.error(oSSO.getMensajeErrorSSO().replace("\n", ""));
                }
                oSSO = null;
            } catch (IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onLogin_SesionCierra()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        private void onFinalizaObjetos() {
            if (oSSO != null) {
                oSSO = null;
            }
            if (oUsuario != null) {
                oUsuario = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBLogin() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    @PostConstruct
    public void onPostConstruct() {

    }
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onLogin_SesionInicia() throws Exception {
        oLogin = new login();
        oLogin.onLogin_SesionInicia();
        oLogin = null;
    }

    public void onLogin_SesionIniciaSinSSO() {
        oLogin = new login();
        oLogin.onLogin_SesionIniciaSinSSO();
        oLogin = null;
    }

    public void onLogin_SesionSeleccionaRol()  {
        oLogin = new login();
        oLogin.onLogin_SesionSeleccionaRol();
        oLogin = null;
    }

    public void onLogin_SesionSeleccionaRolSinSSO() {
        oLogin = new login();
        oLogin.onLogin_SesionSeleccionaRolSinSSO();
        oLogin = null;
    }

    public void onLogin_SesionCierra() {
        oLogin = new login();
        oLogin.onLogin_SesionCierra();
        oLogin = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S   M O N I T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onLoginMonitor_Activa() {
        oLoginMon = new loginMonitorInActividad();
        oLoginMon.onLoginMonitor_Activa();
        oLoginMon = null;
    }

    public void onLoginMonitor_Desactiva() {
        oLoginMon = new loginMonitorInActividad();
        oLoginMon.onLoginMonitor_Desactiva();
        oLoginMon = null;
    }
    
    public synchronized String formatFecha(java.sql.Date fecha) {
        return formatoFecha.format(fecha);
    }
    public synchronized java.util.Date formatHoraParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoHora.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("formatHoraParse()");
        }
        return fechaSal;
    }
    public synchronized java.util.Date formatoHoraCompletaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoHoraCompleta.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("formatoHoraCompletaParse()");
        }
        return fechaSal;
    }

}
