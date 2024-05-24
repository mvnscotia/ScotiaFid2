/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : CSingleSignOn.java
 * TIPO        : Clase Estática
 * PAQUETE     : scotiafid.util
 * CREADO      : 20210716
 * MODIFICADO  : 20220512
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20220512.-Se agrega el servicio del RolValidate
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import scotiaFid.bean.ClaveBean;
import scotiaFid.bean.CriterioBusquedaClaveBean;
import scotiaFid.bean.SSOParamBean;
import scotiaFid.bean.SeguridadPantallaBean;
import scotiaFid.bean.UsuarioLoginBean;
import scotiaFid.dao.CClave;

import com.bns.soa.client.SecurityLogin001SOAPClient;
import com.bns.soa.client.SecurityLogOff001SOAPClient;
import com.bns.soa.client.SecurityRolValidate001SOAPClient;
import com.bns.soa.client.SecurityTransactionInq001SOAPClient;
import com.bns.soa.client.SecurityUserAuthentication001SOAPClient;
import com.bns.soa.client.utils.exception.KeyLoadException;
import com.bns.soa.client.utils.exception.SecurityUserInfoLoadException;
import com.bns.xsd.types.security.ErrorType;
import com.scotia.sso.util.ConfiguracionUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CSingleSignOn {

    private static final Logger logger = LogManager.getLogger(CSingleSignOn.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private String mensajeError;
    private String nombreObjeto;

    private URL urlAuth;
    private URL urlSec;

    private List<ClaveBean> consultaCve;
    private List<ClaveBean> consultaClave;
    private CriterioBusquedaClaveBean cbc;
    private CriterioBusquedaClaveBean cbcm;
    private SSOParamBean ssoParam;

    private CClave oClave;

    private SecurityLogin001SOAPClient sLogIn;
    private SecurityLogOff001SOAPClient sLogOff;
    private SecurityTransactionInq001SOAPClient sti001;
    private SecurityUserAuthentication001SOAPClient sua001;

    private HttpServletRequest request;
    private HttpSession sesion;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeErrorSSO;
    private int iClaveListaRolInit;
    private int iClaveListaRolFin;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeErrorSSO() {
        return mensajeErrorSSO;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CSingleSignOn() {
        cbc = new CriterioBusquedaClaveBean();
        cbcm = new CriterioBusquedaClaveBean();
        mensajeError = "Error En Tiempo de Ejecución\n";
        mensajeErrorSSO = new String();
        nombreObjeto = "\nFuente: scotiaFid.util.";
        ssoParam = new SSOParamBean();
        valorRetorno = Boolean.FALSE;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        sesion = request.getSession(Boolean.FALSE);
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean onSSO_SesionSeleccionaPerfil(UsuarioLoginBean ul, List<String> listaErrores, List<SeguridadPantallaBean> listaPantalla)  {
        try {
            onObtenSSOParam();

            urlAuth = new URL(ConfiguracionUtil.getPropiedad("sso.services.securitytransactioninq001"));
            sti001 = new SecurityTransactionInq001SOAPClient(urlAuth, ssoParam.getParamSSOCipherAlgorithm(), ssoParam.getParamSSOKeyPath(), ssoParam.getParamSSOWSUser());
            sti001.setRqParameters(ul.getUsuarioLoginRolId());
            //logger.debug("Antes de llamar al sericio SSO " + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            sti001.invoke();
            //logger.debug("Sericio llamado " + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            if (!sti001.getErrores().isEmpty()) {
                for (int itemErr = 0; itemErr <= sti001.getErrores().size() - 1; itemErr++) {
                    listaErrores.add(sti001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError()));
                    logger.error("Error al obtener transacciones: ".concat(sti001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError())));
                    mensajeErrorSSO = sti001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError());
                }
            } else {
                if (!sti001.getTransacciones().isEmpty()) {
                    //logger.debug(".:: Total transacciones desde SSO: " + sti001.getTransacciones().size());
                    for (int itemTrans = 0; itemTrans <= sti001.getTransacciones().size() - 1; itemTrans++) {
                        SeguridadPantallaBean sp = new SeguridadPantallaBean();
                        sp.setPantallaId(sti001.getTransacciones().get(itemTrans).getClaveTransaccion());
                        sp.setPantallaTitulo(sti001.getTransacciones().get(itemTrans).getDescripcionTransaccion());
                        listaPantalla.add(sp);
                        //logger.debug("Transacción Id: " + sti001.getTransacciones().get(itemTrans).getClaveTransaccion() + " Nombre: " +sti001.getTransacciones().get(itemTrans).getDescripcionTransaccion());
                    }
                    valorRetorno = Boolean.TRUE;
                } else {
                    mensajeErrorSSO = "110025 .- El rol seleccionado no tiene transacciones asignadas";
                    logger.info(mensajeErrorSSO);
                }
            }
        } catch (Exception Err) {
            mensajeErrorSSO = "Motivo: " + Err.getMessage() + nombreObjeto + "onSSO_SesionSeleccionaPerfil()";
        } finally {
            onFinalizaObjetos();
        }
        return valorRetorno;
    }

    public Boolean onSSO_SesionCierra(UsuarioLoginBean ul, List<String> listaErrores, String origenCierre) {
        try {

            onObtenSSOParam();
            ssoParam.setParamSSOLogOnUser(ul.getUsuarioLoginScotiaId());
            urlAuth = new URL(ConfiguracionUtil.getPropiedad("sso.services.securitylogoff001"));
            sLogOff = new SecurityLogOff001SOAPClient(urlAuth, ssoParam.getParamSSOCipherAlgorithm(), ssoParam.getParamSSOKeyPath(), ssoParam.getParamSSOWSUser());
            sLogOff.setRqParameters(ul.getUsuarioLoginToken(), ssoParam.getParamSSOAppId(), ssoParam.getParamSSOLogOnUser(), ul.getUsuarioLoginIP(), ul.getUsuarioLoginIP());
            sLogOff.invoke();

            if (sLogOff.getErrores().isEmpty()) {
                valorRetorno = Boolean.TRUE;
            } else {
                listaErrores = new ArrayList<>();
                for (ErrorType err : sLogOff.getErrores()) {
                    listaErrores.add(err.getNumError().concat(".- ").concat(err.getDescError()));
                    mensajeErrorSSO = err.getNumError().concat(".- ").concat(err.getDescError());
                }
            }
        } catch (Exception Err) {
            mensajeErrorSSO = "Motivo: " + Err.getMessage() + nombreObjeto + "onSSO_SesionCierra()";
        } finally {
            onFinalizaObjetos();
        }
        return valorRetorno;
    }

    public Boolean onSSO_SesionInicia(UsuarioLoginBean ul, List<String> listaRol, List<String> listaErrores) throws Exception {
        try {

            try {
                onObtenSSOParam();
                onObtenSSOClaveRoles();
                
                ssoParam.setParamSSOLogOnUser(ul.getUsuarioLoginScotiaId());
                urlAuth = new URL(ConfiguracionUtil.getPropiedad("sso.services.securityuserauthentication001"));
                
                sua001 = new SecurityUserAuthentication001SOAPClient(urlAuth, ssoParam.getParamSSOCipherAlgorithm(), ssoParam.getParamSSOKeyPath(), ssoParam.getParamSSOWSUser());

                sua001.setRqParameters(ssoParam.getParamSSOLogOnUser());
                sua001.invoke();
                
                if (sua001.getErrores().isEmpty()) {
                    if ((sua001.getAuthenticationToken() == null) || (sua001.getAuthenticationToken().equals(new String()))) {
                        mensajeErrorSSO = "300029.- Error al iniciar sesión, El funcionario no existe en seguridad institucional: “AuthenticationToken” inválido.";
                    } else {
                        ul.setUsuarioLoginToken(sua001.getAuthenticationToken());
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioAuhenticationToken", sua001.getAuthenticationToken());
                        
                        urlSec = new URL(ConfiguracionUtil.getPropiedad("sso.services.securitylogin001"));
                        sLogIn = new SecurityLogin001SOAPClient(urlSec, ssoParam.getParamSSOCipherAlgorithm(), ssoParam.getParamSSOKeyPath(), ssoParam.getParamSSOWSUser());
                        sLogIn.setRqParameters(sua001.getAuthenticationToken(), ssoParam.getParamSSOAppId(), ssoParam.getParamSSOLogOnUser(), ul.getUsuarioLoginIP(), ul.getUsuarioLoginIP(), Boolean.TRUE, Boolean.TRUE);
                        sLogIn.invoke();
                        if (sLogIn.getErrores().isEmpty()) {
                            if (!sLogIn.getRoles().isEmpty()) {
                                for (int itemRol = 0; itemRol <= sLogIn.getRoles().size() - 1; itemRol++) {
                                    int idRol = Integer.parseInt(sLogIn.getRoles().get(itemRol).getClaveRol());
                                    if (idRol >= iClaveListaRolInit && idRol <= iClaveListaRolFin) {
                                        listaRol.add(sLogIn.getRoles().get(itemRol).getClaveRol().concat(" ").concat(sLogIn.getRoles().get(itemRol).getDescripcionRol()));

                                    }
                                }
                                //Establecemos valores para ul (sera regresado a mbLogin)
                                if (!listaRol.isEmpty()) {
                                    ul.setUsuarioLoginNumero(Integer.parseInt(sua001.getUserCode()));
                                    ul.setUsuarioLoginToken(sua001.getAuthenticationToken());
                                    ul.setUsuarioLoginFirmado(sLogIn.getFirmado());
                                    ul.setUsuarioLoginNombre(sLogIn.getNombre());
                                    ul.setUsuarioLoginPlaza(sLogIn.getPlaza());
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listadoRoles", listaRol);
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginNumero", Integer.parseInt(sua001.getUserCode()));
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginToken", sua001.getAuthenticationToken());
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginFirmado", sLogIn.getFirmado());
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginNombre", sLogIn.getNombre());
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLoginPlaza", sLogIn.getPlaza());
                                    valorRetorno = Boolean.TRUE;
                                } else {
                                    mensajeErrorSSO = "110018.- El Funcionario no tiene roles para ejecutar esa aplicación";
                                    //Aparecerá la ventana emergente que indicara que se debe de cerrar sesión.
                                    //Por esta razón regresamos el valor del token, ya que se requiere para el cierre de sesión
                                    ul.setUsuarioLoginToken(sua001.getAuthenticationToken());
                                }

                            } else {
                                mensajeErrorSSO = "110018.- El Funcionario no tiene roles para ejecutar esa aplicación";
                                //Aparecerá la ventana emergente que indicara que se debe de cerrar sesión.
                                //Por esta razón regresamos el valor del token, ya que se requiere para el cierre de sesión
                                ul.setUsuarioLoginToken(sua001.getAuthenticationToken());
                            }
                        } else {
                            listaErrores = new ArrayList<>();
                            for (int itemErr = 0; itemErr <= sLogIn.getErrores().size() - 1; itemErr++) {
                                listaErrores.add(sLogIn.getErrores().get(itemErr).getNumError().concat(".- ").concat(sLogIn.getErrores().get(itemErr).getDescError()));
                                mensajeErrorSSO = sLogIn.getErrores().get(itemErr).getNumError().concat(".- ").concat(sLogIn.getErrores().get(itemErr).getDescError());
                                logger.error(mensajeErrorSSO);
                            }
                        }
                    }
                } else {
                    listaErrores = new ArrayList<>();
                    for (int itemErr = 0; itemErr <= sua001.getErrores().size() - 1; itemErr++) {
                        listaErrores.add(sua001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sua001.getErrores().get(itemErr).getDescError()));
                        mensajeErrorSSO = sua001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sua001.getErrores().get(itemErr).getDescError());
                        logger.error(mensajeErrorSSO);
                    }
                }
                
            } catch (MalformedURLException mue) {
                mensajeErrorSSO += "Motivo: Se ha producido una URL mal formada, la URL no se pudo analizar " + mue.getMessage() + " " + nombreObjeto + "onSSO_SesionInicia()";
            } catch (UnknownHostException uhe) {
                mensajeErrorSSO += "Motivo: No se ha podido acceder a la url debido a que el hostname no es correcto o no se encuentra registrado en el archivo host: " + uhe.getMessage() + nombreObjeto + "onSSO_SesionInicia()";
            } catch (IOException ioe) {
                mensajeErrorSSO += "Motivo: Se ha producido un error en la entrada/salida de la información: " + ioe.getMessage() + nombreObjeto + "onSSO_SesionInicia()";
            } catch (javax.xml.ws.WebServiceException wbException) {
                mensajeErrorSSO = "No fue posible conectar a los servicios SSO debido a que el certificado de confianza no se encuentra instalado";
                mensajeError = "No fue posible conectar a los servicios web SSO debido a que el certificado de confianza no se encuentra instalado en la KeyStore o no se encuentra registrados los parametros del sistema .- " + wbException.getMessage() + nombreObjeto + "onSSO_SesionInicia()";
                logger.error(mensajeError.replace("\n", ""));
                onComprobarParametrosKeyStore();
            }
        } catch (SecurityUserInfoLoadException | KeyLoadException Err) {
            mensajeError = "No fue posible conectar con los servicios web SSO debido a que la URL no ha respondido a la petición, por favor compruebe que los párametros del archivo configSSO.properties sean correctos: " + Err.getMessage();
            mensajeErrorSSO = "Error al momento de interactuar con el servicio de SSO.\nVerifique que el servicio está activo y que los parámetros de conectividad del archivo configSSO.properties sean los correctos";
            logger.error(mensajeError);
        } finally {
            onFinalizaObjetos();
        }
        return valorRetorno;
    }

    public Boolean onSSO_RolValidate(UsuarioLoginBean ul, List<String> listaErrores) {
        try {
            onObtenSSOParam();

            urlAuth = new URL(ConfiguracionUtil.getPropiedad("sso.services.securityrolvalidate001"));

            String keyPath = ssoParam.getParamSSOKeyPath();
            String wsPath = ssoParam.getParamSSOWSUser();
            String appId = ssoParam.getParamSSOAppId();
            String rol = ul.getUsuarioLoginRolId();
            String userCode = ul.getUsuarioLoginNumero().toString();

            SecurityRolValidate001SOAPClient client = new SecurityRolValidate001SOAPClient(urlAuth, ssoParam.getParamSSOCipherAlgorithm(), keyPath, wsPath);
            client.setRqParameters(appId, rol, userCode);

            client.invoke();

            if (!client.getErrores().isEmpty()) {
                mensajeErrorSSO = "Error al momento de interactuar con el servicio de SSO.\nVerifique que el servicio esta activo y que los parÃ¡metros de conectividad sean los correctos";
                for (int itemErr = 0; itemErr <= client.getErrores().size() - 1; itemErr++) {
                    listaErrores.add(client.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError()));
                    logger.error("Error al obtener Rol: ".concat(sti001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError())));
                    mensajeErrorSSO = sti001.getErrores().get(itemErr).getNumError().concat(".- ").concat(sti001.getErrores().get(itemErr).getDescError());
                }
            } else {
                valorRetorno = Boolean.TRUE;
            }
        } catch (Exception Err) {
            mensajeErrorSSO = "No fue posible conectar con los servicios web SSO debido a que la URL no ha respondido a la petición, por favor compruebe que los párametros del archivo configSSO.properties sean correctos";
            logger.error(mensajeErrorSSO);
        } finally {
            onFinalizaObjetos();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onObtenSSOParam() {
        try {
            cbc.setCriterioCveNumero(690);
            oClave = new CClave();
            consultaCve = oClave.onClave_Consulta(cbc);
            oClave = null;
            
            InputStream is = null;
            try {
                is = request.getServletContext().getResourceAsStream("/WEB-INF/configSSO.properties");
                ConfiguracionUtil.inicializarPropiedades(is);
                
            } catch (AbstractMethodError ex) {
                logger.error("Ocurrio el sig. error al inicializar el archivo de propiedades SSO: " + ex.getMessage());
            } finally {
                if(is != null){
                    try{
                       is.close();
                    }catch(IOException ioe){
                        logger.error("Error al cerrar el archivo de propiedades: " + ioe.getMessage());
                    }
                }
            }
            
            /***** Se quita parámetrización de la Clave 690 **********/
            ssoParam.setParamSSOCipherAlgorithm(ConfiguracionUtil.getPropiedad("CIPHER.ALGORITHM"));
            ssoParam.setParamSSOKeyPath(ConfiguracionUtil.getPropiedad("KEY.PATH"));
            ssoParam.setParamSSOWSUser(ConfiguracionUtil.getPropiedad("WS.USER"));
            ssoParam.setParamSSOAppId(ConfiguracionUtil.getPropiedad("APP.ID"));
            
        } catch (SQLException Err) {
            mensajeError += "Motivo: " + Err.getMessage() + nombreObjeto + "onSSO_onObtenSSOParam()";
            logger.error(mensajeError);
        } finally {
            onFinalizaObjetos();
        }
    }

    private void onObtenSSOClaveRoles() {

        try {
            cbcm.setCriterioCveNumero(694);
            cbcm.setCriterioCveSec(992);
            oClave = new CClave();
            consultaClave = oClave.onClave_Consulta(cbcm);
            oClave = null;

            if (consultaClave == null || consultaClave.isEmpty()) {
                logger.info("No existe la clave 694(Sec. 992) que contiene el rango de roles ScotiaFid Web");
                mensajeErrorSSO = "No existe la clave 694(Sec. 992) que contiene el rango de roles ScotiaFid Web";
                return;
            }

            if(consultaClave.get(0).getClaveLimInf() != null && consultaClave.get(0).getClaveLimSup() != null) {
                iClaveListaRolInit = (int) Math.round(consultaClave.get(0).getClaveLimInf());
                iClaveListaRolFin = (int) Math.round(consultaClave.get(0).getClaveLimSup());
            }

        } catch (SQLException Err) {
            mensajeError += "Motivo: " + Err.getMessage() + nombreObjeto + "onSSO_onObtenSSOClaveRoles()";
            logger.error(mensajeError);
        } finally {
            onFinalizaObjetos();
        }
    }

    private void onComprobarParametrosKeyStore() {
        if (System.getProperty("javax.net.ssl.trustStore") == null) {
            logger.error("javax.net.ssl.trustStore: No se encuentra registrado en los párametros del sistema");
        } else {
            logger.info("javax.net.ssl.trustStore: " + System.getProperty("javax.net.ssl.trustStore"));
        }

        if (System.getProperty("javax.net.ssl.keyStore") == null) {
            logger.error("javax.net.ssl.keyStore: No se encuentra registrado en los párametros del sistema");
        } else {
            logger.info("javax.net.ssl.keyStore: " + System.getProperty("javax.net.ssl.keyStore"));
        }

        if (System.getProperty("javax.net.ssl.keyStorePassword") == null) {
            logger.error("javax.net.ssl.keyStorePassword: No se encuentra registrado en los párametros del sistema");
        } else {
            logger.info("javax.net.ssl.keyStorePassword: " + System.getProperty("javax.net.ssl.keyStorePassword"));
        }

        if (System.getProperty("javax.net.ssl.trustStorePassword") == null) {
            logger.error("javax.net.ssl.trustStorePassword: No se encuentra registrado en los párametros del sistema");
        } else {
            logger.info("javax.net.ssl.trustStorePassword: " + System.getProperty("javax.net.ssl.trustStorePassword"));
        }
    }

    private void onFinalizaObjetos() {
        if (oClave != null) {
            oClave = null;
        }
    }
}
