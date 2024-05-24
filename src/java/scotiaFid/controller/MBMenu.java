/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : MBMenu.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controler
 * CREADO      : 20210306
 * MODIFICADO  : 20210827
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.component.idlemonitor.IdleMonitor;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;
import scotiaFid.bean.ClaveBean;
import scotiaFid.bean.CriterioBusquedaClaveBean;

import scotiaFid.bean.SeguridadPantallaBean;
import scotiaFid.bean.SeguridadPantallaFidBean;
import scotiaFid.bean.UsuarioLoginBean;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CSeguridad;
import scotiaFid.util.CSingleSignOn;
import scotiaFid.util.LogsContext;
@ViewScoped
@Named("mbMenu")
public class MBMenu implements Serializable {

private static final Logger logger = LogManager.getLogger(MBMenu.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Integer valorTimeOut;
    private final String destinoURL = new String();
    private String mensajeError;
    private String nombreObjeto;

    private FacesMessage mensaje;
    private HttpSession sesion;

    private UsuarioLoginBean ul;
    private CSeguridad oSeguridad;
    private CSingleSignOn oSSO;

    private MenuItem menuItem;
    private Submenu menuSub;

    private menuGrales oMnuGrales;
    private menuMonitorInActividad oMnuMonitor;

    private List<String> listaErr;
    private List<SeguridadPantallaBean> consultaPantallaSSO;
    private List<SeguridadPantallaFidBean> consultaPantallaFid;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

 /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String menuSistemaFecha;
    private String menuSistemaCierra;
    private String menuSistemaHoraCierre;
    private String menuUsuarioNombre;
    private String menuUsuarioPerfil;
    private String menuUsuarioPlaza;
    private String menuUsuarioPuesto;
    private String menuUsuarioTipo;

    private IdleMonitor idleMntr;
    private PanelMenu panelMenu;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMenuSistemaFecha() {
        return menuSistemaFecha;
    }

    public String getMenuSistemaCierra() {
        return menuSistemaCierra;
    }

    public String getMenuSistemaHoraCierre() {
        return menuSistemaHoraCierre;
    }

    public String getMenuUsuarioNombre() {
        return menuUsuarioNombre;
    }

    public String getMenuUsuarioPerfil() {
        return menuUsuarioPerfil;
    }

    public String getMenuUsuarioPlaza() {
        return menuUsuarioPlaza;
    }

    public String getMenuUsuarioPuesto() {
        return menuUsuarioPuesto;
    }

    public String getMenuUsuarioTipo() {
        return menuUsuarioTipo;
    }

    /* * * * * * * * * * * * * * * * * * * * * */
    public IdleMonitor getIdleMntr() {
        return idleMntr;
    }

    public void setIdleMntr(IdleMonitor idleMntr) {
        this.idleMntr = idleMntr;
    }

    public PanelMenu getPanelMenu() {
        return panelMenu;
    }

    public void setPanelMenu(PanelMenu panelMenu) {
        this.panelMenu = panelMenu;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    class menuMonitorInActividad {

        public menuMonitorInActividad() {
            mensajeError = "Error En Tiempo de Ejecución. ";
            nombreObjeto = "Fuente: scotiafid.controller.mbMenu.menuMonitorInActividad.";
            LogsContext.FormatoNormativo();
        }

        public void onMenuMonitor_Activa()  {
                onCierraSesion();
                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Sesion cerrada por inactividad");
        }

        public void onMenuMonitor_Desactiva() {
                RequestContext.getCurrentInstance().execute("onClearBody();");
                onCierraSesion();
                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Sesion cerrada por inactividad");
        }

        public void onMenuMonitor_CierraSesion() {
            onCierraSesion();
        }
        //Funciones privadas

        private void onCierraSesion() {
            try {
                //Cierre de Sesión desde pantalla de TimeOut
                String userScotia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId");
                if (userScotia != null && !"".equals(userScotia)){
                    logger.info( "Proceso de Cierre de Sesion (por TimeOut)");
                    ul = new UsuarioLoginBean();
                    ul.setUsuarioLoginToken((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioToken"));
                    ul.setUsuarioLoginScotiaId((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId"));
                    ul.setUsuarioLoginIP((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
                    oSSO = new CSingleSignOn();
                    if (oSSO.onSSO_SesionCierra(ul, listaErr, "TimeOut").equals(Boolean.TRUE)) {
                        FacesContext ctx = FacesContext.getCurrentInstance();
                        sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                        //sesion.invalidate(); Se remueve cierre de sesión por timeout
                        ctx.getExternalContext().redirect("/scotiaFid/vista/sesionExpiro.html");
                        //FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    } else {
                        FacesContext ctx = FacesContext.getCurrentInstance();
                        sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                        //sesion.invalidate(); Se remueve cierre de sesión por timeout
                        ctx.getExternalContext().redirect("/scotiaFid/vista/sesionExpiro.html");
                        //mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                        //logger.info( "Ocurrio el siguiente error al llamar al servicio SSO: " + oSSO.getMensajeErrorSSO());
                    }
                    oSSO = null;
                } else {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                    sesion.invalidate();
                    ctx.getExternalContext().redirect("/scotiaFid/vista/sesionExpiro.html");
                }
            } catch (IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onMenuMonitor_CierraSesion()";
                logger.error( mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
    }

    class menuGrales {

        public menuGrales() {
            mensajeError = "Error En Tiempo de Ejecución.";
            listaErr = new ArrayList<>();
            nombreObjeto = "Fuente: scotiafid.controller.mbMenu.menuGrales.";
            LogsContext.FormatoNormativo();
        }

        public String onMenu_CierraSesion() {
            try {
                String userScotia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId");
                if (userScotia != null) {
                    //Cierre de Sesión desde Menu Principal  
                    logger.info( "Proceso de Cierre de Sesion (desde menu principal)");
                   
                    ul = new UsuarioLoginBean();
                    ul.setUsuarioLoginToken((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioToken"));
                    ul.setUsuarioLoginScotiaId((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId"));
                    ul.setUsuarioLoginIP((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
                    oSSO = new CSingleSignOn();
                    if (oSSO.onSSO_SesionCierra(ul, listaErr, "Menu Principal").equals(Boolean.TRUE)) {
                        FacesContext ctx = FacesContext.getCurrentInstance();
                        if((HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE) != null){
                            sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                            sesion.invalidate();
                        } else{ 
                            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                        }
                        ctx.getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                        //FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                        logger.info( "Ocurrio el siguiente error al llamar al servicio SSO: " + oSSO.getMensajeErrorSSO());
                        
                    }
                    oSSO = null;
                } else {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                    sesion.invalidate();
                    ctx.getExternalContext().redirect("/scotiaFid/vistaExpiro.html");
                }
            } catch (IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onMenu_CierraSesion()";
                logger.error( mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
            return new String();
        }

        public String onMenu_CierraSesion_TimeOut() {
            try {
                String userScotia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId");
                if (userScotia != null) {
                    //Cierre de Sesión desde Menu Principal  
                    logger.info( "Proceso de Cierre de Sesion (por Horario máximo para operar)");
                    ul = new UsuarioLoginBean();
                    ul.setUsuarioLoginToken((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioToken"));
                    ul.setUsuarioLoginScotiaId((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId"));
                    ul.setUsuarioLoginIP((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
                    oSSO = new CSingleSignOn();
                    if (oSSO.onSSO_SesionCierra(ul, listaErr, "TimeOut").equals(Boolean.TRUE)) {
                        FacesContext ctx = FacesContext.getCurrentInstance();
                        sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                        sesion.invalidate();
                        ctx.getExternalContext().redirect("/scotiaFid/vistaExpiro.html");
                        //FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                        logger.info( "Ocurrio el siguiente error al llamar al servicio SSO: " + oSSO.getMensajeErrorSSO());
                        
                    }
                    oSSO = null;
                } else {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                    sesion.invalidate();
                    ctx.getExternalContext().redirect("/scotiaFid/vistaExpiro.html");
                }
            } catch (IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onMenu_CierraSesion_TimeOut()";
                logger.error( mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
            return new String();
        }
        
        public String onMenu_CierraSesion_CloseTab() {
            try {
                if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId") != null) {
                //Cierre de Sesión desde Menu Principal  
                    ul = new UsuarioLoginBean();
                    ul.setUsuarioLoginToken((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioToken"));
                    ul.setUsuarioLoginScotiaId((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId"));
                    ul.setUsuarioLoginIP((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
                    oSSO = new CSingleSignOn();
                    if (oSSO.onSSO_SesionCierra(ul, listaErr, "Menu Principal").equals(Boolean.TRUE)) {
                        
                        FacesContext ctx = FacesContext.getCurrentInstance();
                        sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                        sesion.invalidate();
                        ctx.getExternalContext().redirect("/scotiaFid/sesionExpiro.html");
                        //FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/inicio.sb?faces-redirect=true");
                        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", oSSO.getMensajeErrorSSO());
                        
                    }
                    oSSO = null;
                }else{
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                    sesion.invalidate();
                    ctx.getExternalContext().redirect("/scotiaFid/vistaExpiro.html");
                }
            } catch (IOException Err) {
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onMenu_CierraSesion_CloseTab()";
               
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
            return new String();
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oSSO != null) {
                oSSO = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBMenu() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    @PostConstruct
    public void onPostConstruct() {
        try {
            String sesionIniciada = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            if (sesionIniciada != null) {

                menuSistemaFecha = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                menuSistemaCierra = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioHoraMax");
                menuSistemaCierra = "El sistema cerrará a las: ".concat(menuSistemaCierra).concat(" son las: ");
                menuSistemaHoraCierre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioHoraMax");
                menuUsuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
                menuUsuarioPerfil = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioRol");
                menuUsuarioPlaza = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioPlaza");
                //menuUsuarioPuesto     = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioPuesto");
                menuUsuarioPuesto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioRol");
                menuUsuarioTipo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTipo");
                valorTimeOut = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTimeOut")) * 60000;  //60,000 = 1 minuto

                consultaPantallaSSO = new ArrayList<>();
                consultaPantallaSSO = (List<SeguridadPantallaBean>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaTrans");

                oSeguridad = new CSeguridad();
                //consultaPantallaFid   = oSeguridad.onSeguridad_ObtenCatalogoPantalla();
                String[] arrMenu = oSeguridad.onSeguridad_ObtenCatalogoMenu().split(",");
                oSeguridad = null;

                CriterioBusquedaClaveBean clave771 = new CriterioBusquedaClaveBean();
                List<ClaveBean> consultaCve;
                CClave oClave;
                clave771.setCriterioCveNumero(771);

                oClave = new CClave();
                consultaCve = oClave.onClave_Consulta(clave771);
                oClave = null;
                clave771 = null;

                //Generamos el Menú
                if (arrMenu.length > 0) {
                    panelMenu = new PanelMenu();
                    for (short itemMnu = 0; itemMnu <= arrMenu.length - 1; itemMnu++) {
                        menuSub = new Submenu();
                        menuSub.setLabel(arrMenu[itemMnu].substring(arrMenu[itemMnu].indexOf("»") + 1).trim());
                        oSeguridad = new CSeguridad();
                        consultaPantallaFid = oSeguridad.onSeguridad_ObtenCatalogoPantallaPorMenu(Short.parseShort(arrMenu[itemMnu].substring(0, arrMenu[itemMnu].indexOf("»"))));
                        oSeguridad = null;
                        for (short itemMnuSub = 0; itemMnuSub <= consultaPantallaFid.size() - 1; itemMnuSub++) {
                            String pantallaFidUrl;
                            String segPantallaFidId = consultaPantallaFid.get(itemMnuSub).getSegPantallaFidId();
                            pantallaFidUrl = consultaPantallaFid.get(itemMnuSub).getSegPantallaFidURL();

                            if (consultaPantallaFid.get(itemMnuSub).getSegPantallaFidSSO().equals("NO")) {
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                                        consultaPantallaFid.get(itemMnuSub).getSegPantallaFidURL(),
                                        consultaPantallaFid.get(itemMnuSub).getSegPantallaFidEscritura());

                                menuItem = new MenuItem();
                                menuItem.setTarget("mainContainer");
                                menuItem.setValue(consultaPantallaFid.get(itemMnuSub).getSegPantallaFidTitulo());
                                menuItem.setUrl(pantallaFidUrl);
                                menuSub.getChildren().add(menuItem);
                            }
                            if (consultaPantallaFid.get(itemMnuSub).getSegPantallaFidSSO().equals("SI")) {
                                for (short itemMnuSubSSO = 0; itemMnuSubSSO <= consultaPantallaSSO.size() - 1; itemMnuSubSSO++) {
                                    if (consultaPantallaFid.get(itemMnuSub).getSegPantallaFidId().equals(consultaPantallaSSO.get(itemMnuSubSSO).getPantallaId())) {
                                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                                                pantallaFidUrl,
                                                consultaPantallaFid.get(itemMnuSub).getSegPantallaFidEscritura());

                                        menuItem = new MenuItem();
                                        menuItem.setValue(consultaPantallaFid.get(itemMnuSub).getSegPantallaFidTitulo());
                                        Boolean Scotia2 = Boolean.FALSE;
                                        for (short itemScotia2 = 0; itemScotia2 <= consultaCve.size() - 1; itemScotia2++) {
                                            if (consultaCve.get(itemScotia2).getClaveDesc().equals(segPantallaFidId)) {
                                                Scotia2 = Boolean.TRUE;
//                                        if ("80841510".equals(segPantallaFidId) || "80871310".equals(segPantallaFidId)) {
                                                HttpServletRequest peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                                                HttpSession sesion = peticion.getSession(Boolean.FALSE);
                                                String winUserContext = sesion.getAttribute("WIN_USER").toString();
                                                String tokenContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLoginToken");
                                                String nombreContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
                                                Integer usuarioContext = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                                                String rolContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioRol");
                                                String plazaContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioPlaza");
                                                String fechaSistemaContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                                                String terminalContext = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
                                                String horaMax = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioHoraMax");
                                                String timeOut = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTimeOut");
                                                String encodedEscritura;
                                                if (consultaPantallaFid.get(itemMnuSub).getSegPantallaFidEscritura().toUpperCase(Locale.ENGLISH).equals("SI")) {
                                                    encodedEscritura = URLEncoder.encode("01", "UTF-8");
                                                } else {
                                                    encodedEscritura = URLEncoder.encode("00", "UTF-8");
                                                }
                                                String encodedToken = URLEncoder.encode(tokenContext, "UTF-8");
                                                String encodedNombre = URLEncoder.encode(nombreContext, "UTF-8");
                                                String encodedUsuario = URLEncoder.encode(Integer.toString(usuarioContext), "UTF-8");
                                                String encodedRol = URLEncoder.encode(rolContext, "UTF-8");
                                                String encodedPlaza = URLEncoder.encode(plazaContext, "UTF-8");
                                                String encodedFechaSistema = URLEncoder.encode(fechaSistemaContext, "UTF-8");
                                                String encodedWinUser = URLEncoder.encode(winUserContext, "UTF-8");
                                                String encodedTerminal = URLEncoder.encode(terminalContext, "UTF-8");
                                                String encodedhoraMax = horaMax.replaceAll(":", "xxx");
 
                                                String encodedtimeOut = URLEncoder.encode(timeOut, "UTF-8");
                                                pantallaFidUrl += "?token=" + encodedToken + "&nombre=" + encodedNombre + "&nousuario=" + encodedUsuario + "&rol=" + encodedRol + "&plaza=" + encodedPlaza + "&fecha=" + encodedFechaSistema + "&winuser="
                                                        + encodedWinUser + "&terminal=" + encodedTerminal + "&horaMax=" + encodedhoraMax + "&timeOut=" + encodedtimeOut + "&escritura=" + encodedEscritura;
                                                menuItem.setTarget("_blank");
                                            }
                                        }
                                        if (!Scotia2) {
                                            menuItem.setTarget("mainContainer");
                                        }
                                        menuItem.setUrl(pantallaFidUrl);

                                        menuSub.getChildren().add(menuItem);
                                        break;
                                    }
                                }
                            }
                        }
                        panelMenu.getChildren().add(menuSub);
                    }

                }
                //Obtenemos el valor del TimeOut  
                idleMntr = new IdleMonitor();
                idleMntr.setId("monitorInactividad");
                idleMntr.setTimeout(valorTimeOut);
            } else {
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            }
        } catch (SQLException | IOException | NumberFormatException Err) {
            mensajeError = "Error: " + Err.getMessage() + "onPostConstruct()";

        } finally {
            if (oSeguridad != null) {
                oSeguridad = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S   M E N U
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String onMenu_CierraSesion() {
        oMnuGrales = new menuGrales();
        oMnuGrales.onMenu_CierraSesion();
        oMnuGrales = null;
        return destinoURL;
    }

    public String onMenu_CierraSesion_TimeOut()  {
        oMnuGrales = new menuGrales();
        oMnuGrales.onMenu_CierraSesion_TimeOut();
        oMnuGrales = null;
        return destinoURL;
    }
    
    public String onMenu_CierraSesion_CloseTab()  {
        oMnuGrales = new menuGrales();
        oMnuGrales.onMenu_CierraSesion_CloseTab();
        oMnuGrales = null;
        return destinoURL;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S   M O N I T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onMenuMonitor_Activa()  {
        oMnuMonitor = new menuMonitorInActividad();
        oMnuMonitor.onMenuMonitor_Activa();
        oMnuMonitor = null;
    }

    public void onMenuMonitor_Desactiva()  {
        oMnuMonitor = new menuMonitorInActividad();
        oMnuMonitor.onMenuMonitor_Desactiva();
        oMnuMonitor = null;
    }

    public String onMenuMonitor_CierraSesion() {
        oMnuMonitor = new menuMonitorInActividad();
        oMnuMonitor.onMenuMonitor_CierraSesion();
        oMnuMonitor = null;
        return new String();
    }
}
