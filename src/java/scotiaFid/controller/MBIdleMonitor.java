
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.idlemonitor.IdleMonitor;
import org.primefaces.context.RequestContext;
import scotiaFid.bean.UsuarioLoginBean;
import scotiaFid.util.CSingleSignOn;
import scotiaFid.util.LogsContext;

@ViewScoped
@Named("mbIdleMonitor")
public class MBIdleMonitor implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBIdleMonitor.class);
    private static final long serialVersionUID = 1L;

    private UsuarioLoginBean ul;
    private CSingleSignOn oSSO;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Integer valorTimeOut;
    private String mensajeError;
    private final String nombreObjeto;

    private FacesMessage mensaje;
    private HttpSession sesion;

    private IdleMonitor idleMntr;
    private List<String> listaErr;
    private MonitorInActividad oMntrInActividad;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    public IdleMonitor getIdleMntr() {
        return idleMntr;
    }

    public void setIdleMntr(IdleMonitor idleMntr) {
        this.idleMntr = idleMntr;
    }

    public Integer getValorTimeOut() {
        return valorTimeOut;
    }

    public void setValorTimeOut(Integer valorTimeOut) {
        this.valorTimeOut = valorTimeOut;
    }

    public MBIdleMonitor() {
        mensajeError = "Error En Tiempo de Ejecución. ";
        nombreObjeto = "Fuente: scotiafid.controller.MBIdleMonitor.";
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }
    
    class MonitorInActividad {
        
        public void onIdleMonitor_Active() {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Se desactiva monitor de Inactividad");
        }

        public void onIdleMonitor_Idle() throws Exception {
            RequestContext.getCurrentInstance().execute("onClearBody();");
            onCierraSesion();
        }

        private void onCierraSesion() throws Exception {
            LogsContext.FormatoNormativo();
            try {
                //Cierre de Sesión desde pantalla de TimeOut
                String userScotia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioScotiaId");
                if (userScotia != null && !"".equals(userScotia)) {
                    logger.info("Proceso de Cierre de Sesion (por TimeOut)");
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
                mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "CierraSesion()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
    }

    @PostConstruct
    public void onPostConstruct() {
        LogsContext.FormatoNormativo();
        try {
            String sesionIniciada = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            if (sesionIniciada != null) {

                valorTimeOut = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTimeOut")) * 60000;  //60,000 = 1 minuto

                //Obtenemos el valor del TimeOut  
                idleMntr = new IdleMonitor();
                idleMntr.setId("monitorInactividad");
                idleMntr.setTimeout(valorTimeOut);
            } else {
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            }
        } catch (IOException | NumberFormatException Err) {
            mensajeError = "Error: " + Err.getMessage() + "onPostConstruct()";
            logger.error(mensajeError);
        }
    }
    
    public void idleMonitor_onActive() {
        oMntrInActividad = new MonitorInActividad();
        oMntrInActividad.onIdleMonitor_Active();
        oMntrInActividad = null;
    }
    
    public void idleMonitor_onIdle() throws Exception {
        oMntrInActividad = new MonitorInActividad();
        oMntrInActividad.onIdleMonitor_Idle();
        oMntrInActividad = null;
    }
}
