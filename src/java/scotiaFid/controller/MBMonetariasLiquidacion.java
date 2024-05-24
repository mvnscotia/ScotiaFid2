/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : MBContabilidad.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controler
 * CREADO      : 20210312
 * MODIFICADO  : 20211013
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20211013.- Se implementa funcionalidad de saldosPromedio
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.FechaBean;
import scotiaFid.bean.GenericoDesHabilitadoBean;
import scotiaFid.bean.GenericoFechaBean;
import scotiaFid.bean.GenericoTituloBean;
import scotiaFid.bean.GenericoVisibleBean;
import scotiaFid.bean.InformacionCuentasMonetariasOperacion;
import scotiaFid.bean.InformacionCuentasParametrosLiquidacionBean;
import scotiaFid.bean.MensajeConfirmaBean;
import scotiaFid.bean.MonLiqInstrucFidecomisosBean;
import scotiaFid.bean.MonLiqInstrucTercerosBean;
import scotiaFid.bean.MonedasBean;
import scotiaFid.bean.MonetariaLiquidacionBean;
import scotiaFid.bean.NombreBean;
import scotiaFid.bean.ParametrosLiquidacionBean;
import scotiaFid.bean.SPN_PROC_LIQ_IND_Bean;
import scotiaFid.bean.SPN_PROC_LOTE_LIQ_Bean;
import scotiaFid.dao.CCMonetariasoperacionesOtrosBancos;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CContabilidad;
import scotiaFid.dao.CMonetariasLiquidacion;
import scotiaFid.util.CValidacionesUtil;
import scotiaFid.util.LogsContext;

@ManagedBean(name = "mbMonetariasLiquidacion")
@ViewScoped
public class MBMonetariasLiquidacion implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBMonetariasLiquidacion.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin - efp
    private static final String CLAVE_CONCEPTO = "128";
    private static final String CLAVE_TIPO_PAGO = "81";
    //end - efp

    private Boolean validacion;
    private Boolean valorRetorno;
    private Integer usuarioNumero;
    private String usuarioNombre;
    private String usuarioTerminal;
    private int numeroPago = 0;

    private List<String> usuarioFiltro;
    private String mensajeError;
    private String nombreObjeto;

    private String[] arrArchivo;
    private List<String> listaErr;
    private String archivoLinea;
    private String archivoNombre;
    private File archivo;
    private FileInputStream fis;
    private FileOutputStream fos;
    private DecimalFormat formatoImporte2D;

    private FacesMessage mensaje;

    private HttpServletRequest peticionURL;

    private SimpleDateFormat formatoFecha;

    //begin - efp
    private monetariasLiquidacion oMonetariasLiquidacion;
    //end - efp

    private CClave oClave;
    private CContabilidad oContabilidad;
    //begin - efp
    private CMonetariasLiquidacion ccMonetariasLiquidacion;

    //end - efp
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @ManagedProperty(value = "#{beanMonetariaLiquidacion}")
    private MonetariaLiquidacionBean formMonetariasLiq;

    @ManagedProperty(value = "#{beanGenericoDesHabilitado}")
    private GenericoDesHabilitadoBean geneDes;
    @ManagedProperty(value = "#{beanGenericoFecha}")
    private GenericoFechaBean geneFecha;
    @ManagedProperty(value = "#{beanGenericoTitulo}")
    private GenericoTituloBean geneTitulo;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean geneVisible;
    @ManagedProperty(value = "#{beanMensajeConfirmacion}")
    private MensajeConfirmaBean mensajeConfrima;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin - efp
    private String cuentaSelected;
    private InformacionCuentasMonetariasOperacion cuentaSeleccionada;
    private List<MonLiqInstrucFidecomisosBean> listMonLiqInstrucFidecomisos;
    private List<MonLiqInstrucTercerosBean> listMonLiqInstrucTerceros;
    //end - efp

    //begin -Integracion de Parametros de Liquidacion efp
    private String conceptoSelected;
    private String tipoPersonaSelected;
    private String nombrePersonaSelected;
    private String tipoPagoSelected;
    private String monedaSelected;
    private String showAndHideOption1 = "none";
    private String showAndHideOption2 = "none";

    private NombreBean nombreSeleccionado;
    private DestinoRecepcionBean tipoPagoSeleccionado;
    private Double importe;
    private String importeTxt;
    private Boolean cuentaOrden;
    private String direccionParametroLiquidacion;
    private String codigoSwift;
    private FechaBean fechaRecepcionSistema;
    private List<DestinoRecepcionBean> conceptos;
    private List<DestinoRecepcionBean> listTipoPago;
    private List<NombreBean> listNombre;
    private List<MonedasBean> listMonedas;
    private List<InformacionCuentasMonetariasOperacion> listInformacionCuentas;
    private List<InformacionCuentasParametrosLiquidacionBean> listInformacionCuentaParametroLiquidacion;
    private Boolean botonTerminar = Boolean.FALSE;

    @ManagedProperty(value = "#{informacionCuentasMonetariasOperacion}")
    private InformacionCuentasMonetariasOperacion informacionCuenta;
    @ManagedProperty(value = "#{informacionCuentasParametrosLiquidacionBean}")
    private InformacionCuentasParametrosLiquidacionBean informacionCuentaParametroLiquidacion;

    CComunes ccComunes = null;
    CCMonetariasoperacionesOtrosBancos ccMonetariasoperacionesOtrosBancos = null;
    ParametrosLiquidacionBean parametrosLiquidacionBean = null;

    Map<String, String> validaciones = null;

    //end- Integracion de Parametros de Liquidacion efp
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setGeneDes(GenericoDesHabilitadoBean geneDes) {
        this.geneDes = geneDes;
    }

    public void setGeneFecha(GenericoFechaBean geneFecha) {
        this.geneFecha = geneFecha;
    }

    public void setGeneTitulo(GenericoTituloBean geneTitulo) {
        this.geneTitulo = geneTitulo;
    }

    public void setGeneVisible(GenericoVisibleBean geneVisible) {
        this.geneVisible = geneVisible;
    }

    public void setMensajeConfrima(MensajeConfirmaBean mensajeConfrima) {
        this.mensajeConfrima = mensajeConfrima;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MonetariaLiquidacionBean getFormMonetariasLiq() {
        return formMonetariasLiq;
    }

    public void setFormMonetariasLiq(MonetariaLiquidacionBean formMonetariasLiq) {
        this.formMonetariasLiq = formMonetariasLiq;
    }

    public String getTipoPersonaSelected() {
        return tipoPersonaSelected;
    }

    public void setTipoPersonaSelected(String tipoPersonaSelected) {
        this.tipoPersonaSelected = tipoPersonaSelected;
    }

    //efp
    public List<NombreBean> getListNombre() {
        return listNombre;
    }

    public void setListNombre(List<NombreBean> listNombre) {
        this.listNombre = listNombre;
    }

    public String getNombrePersonaSelected() {
        return nombrePersonaSelected;
    }

    public void setNombrePersonaSelected(String nombrePersonaSelected) {
        this.nombrePersonaSelected = nombrePersonaSelected;
    }

    public List<InformacionCuentasMonetariasOperacion> getListInformacionCuentas() {
        return listInformacionCuentas;
    }

    public void setListInformacionCuentas(List<InformacionCuentasMonetariasOperacion> listInformacionCuentas) {
        this.listInformacionCuentas = listInformacionCuentas;
    }

    public String getCuentaSelected() {
        return cuentaSelected;
    }

    public void setCuentaSelected(String cuentaSelected) {
        this.cuentaSelected = cuentaSelected;
    }

    public InformacionCuentasMonetariasOperacion getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(InformacionCuentasMonetariasOperacion cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public String getMonedaSelected() {
        return monedaSelected;
    }

    public void setMonedaSelected(String monedaSelected) {
        this.monedaSelected = monedaSelected;
    }

    public String getTipoPagoSelected() {
        return tipoPagoSelected;
    }

    public void setTipoPagoSelected(String tipoPagoSelected) {
        this.tipoPagoSelected = tipoPagoSelected;
    }

    public InformacionCuentasParametrosLiquidacionBean getInformacionCuentaParametroLiquidacion() {
        if (informacionCuentaParametroLiquidacion == null) {
            informacionCuentaParametroLiquidacion = new InformacionCuentasParametrosLiquidacionBean();
        }
        return informacionCuentaParametroLiquidacion;
    }

    public void setInformacionCuentaParametroLiquidacion(InformacionCuentasParametrosLiquidacionBean informacionCuentaParametroLiquidacion) {
        if (informacionCuentaParametroLiquidacion == null) {
            informacionCuentaParametroLiquidacion = new InformacionCuentasParametrosLiquidacionBean();
        }
        this.informacionCuentaParametroLiquidacion = informacionCuentaParametroLiquidacion;
    }

    public List<InformacionCuentasParametrosLiquidacionBean> getListInformacionCuentaParametroLiquidacion() {
        return listInformacionCuentaParametroLiquidacion;
    }

    public void setListInformacionCuentaParametroLiquidacion(List<InformacionCuentasParametrosLiquidacionBean> listInformacionCuentaParametroLiquidacion) {
        this.listInformacionCuentaParametroLiquidacion = listInformacionCuentaParametroLiquidacion;
    }

    public String getShowAndHideOption1() {
        return showAndHideOption1;
    }

    public void setShowAndHideOption1(String showAndHideOption1) {
        this.showAndHideOption1 = showAndHideOption1;
    }

    public String getShowAndHideOption2() {
        return showAndHideOption2;
    }

    public void setShowAndHideOption2(String showAndHideOption2) {
        this.showAndHideOption2 = showAndHideOption2;
    }

    public NombreBean getNombreSeleccionado() {
        return nombreSeleccionado;
    }

    public void setNombreSeleccionado(NombreBean nombreSeleccionado) {
        this.nombreSeleccionado = nombreSeleccionado;
    }

    public List<DestinoRecepcionBean> getListTipoPago() {
        return listTipoPago;
    }

    public void setListTipoPago(List<DestinoRecepcionBean> listTipoPago) {
        this.listTipoPago = listTipoPago;
    }

    public List<MonedasBean> getListMonedas() {
        return listMonedas;
    }

    public void setListMonedas(List<MonedasBean> listMonedas) {
        this.listMonedas = listMonedas;
    }

    public Boolean getCuentaOrden() {
        return cuentaOrden;
    }

    public void setCuentaOrden(Boolean cuentaOrden) {
        this.cuentaOrden = cuentaOrden;
    }

    public String getDireccionParametroLiquidacion() {
        return direccionParametroLiquidacion;
    }

    public void setDireccionParametroLiquidacion(String direccionParametroLiquidacion) {
        this.direccionParametroLiquidacion = direccionParametroLiquidacion;
    }

    public String getCodigoSwift() {
        return codigoSwift;
    }

    public void setCodigoSwift(String codigoSwift) {
        this.codigoSwift = codigoSwift;
    }

    public InformacionCuentasMonetariasOperacion getInformacionCuenta() {
        return informacionCuenta;
    }

    public void setInformacionCuenta(InformacionCuentasMonetariasOperacion informacionCuenta) {
        this.informacionCuenta = informacionCuenta;
    }

    public String getConceptoSelected() {
        return conceptoSelected;
    }

    public void setConceptoSelected(String conceptoSelected) {
        this.conceptoSelected = conceptoSelected;
    }

    public List<DestinoRecepcionBean> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<DestinoRecepcionBean> conceptos) {
        this.conceptos = conceptos;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public List<MonLiqInstrucFidecomisosBean> getListMonLiqInstrucFidecomisos() {
        return listMonLiqInstrucFidecomisos;
    }

    public void setListMonLiqInstrucFidecomisos(List<MonLiqInstrucFidecomisosBean> listMonLiqInstrucFidecomisos) {
        this.listMonLiqInstrucFidecomisos = listMonLiqInstrucFidecomisos;
    }

    //efp
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<MonLiqInstrucTercerosBean> getListMonLiqInstrucTerceros() {
        return listMonLiqInstrucTerceros;
    }

    public void setListMonLiqInstrucTerceros(List<MonLiqInstrucTercerosBean> listMonLiqInstrucTerceros) {
        this.listMonLiqInstrucTerceros = listMonLiqInstrucTerceros;
    }

    public String getImporteTxt() {
        return importeTxt;
    }

    public void setImporteTxt(String importeTxt) {
        this.importeTxt = importeTxt;
    }

    public List<String> getListaErr() {
        return listaErr;
    }

    public void setListaErr(List<String> listaErr) {
        this.listaErr = listaErr;
    }

    public Boolean getBotonTerminar() {
        return botonTerminar;
    }

    public void setBotonTerminar(Boolean botonTerminar) {
        this.botonTerminar = botonTerminar;
    }

    //begin -efp
    class monetariasLiquidacion {

        public monetariasLiquidacion() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbMonetariasLiquidacion.monetariasLiquidacion.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }
        //begin -efp

        public void onMonetariasLiquidacion_Cargar_Beneficiario_O_Tercero() {
            try {
                nombrePersonaSelected = null;
                cuentaSelected = null;
                formMonetariasLiq.setMonetariasLiqInsAdcSucursal(null);
                formMonetariasLiq.setMonetariasLiqInsCveDescClave(null);
                formMonetariasLiq.setMonetariasLiqInsMonNomMoneda(null);

                ccComunes = new CComunes();
                listNombre = ccComunes.obtenParticipantePorRolFid(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(), tipoPersonaSelected);
                if (getListNombre().isEmpty()) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Tipo de Persona " + tipoPersonaSelected + " no cuenta con Fideicomisarios o Terceros...");
                }
            } catch (AbstractMethodError Err) {
                mensajeError += "onMonetariasLiquidacion_Cargar_Beneficiario_O_Tercero()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void obtieneCuentaSeleccionada() {
            cuentaSeleccionada = listInformacionCuentas.stream()
                    .filter(cuenta -> cuenta.getCuenta().equals(cuentaSelected))
                    .findAny().orElse(null);

        }

        public void obtieneNombrePersonaSeleccionado() {
            setNombreSeleccionado(listNombre.stream()
                    .filter(nombre -> nombre.getBeneficiarioNumero().equals(Integer.parseInt(nombrePersonaSelected)))
                    .findAny().orElse(null));

        }

        public void onMonetariasLiquidacion_CargarCuentas() {
            try {
                cuentaSelected = null;
                formMonetariasLiq.setMonetariasLiqInsAdcSucursal(null);
                formMonetariasLiq.setMonetariasLiqInsCveDescClave(null);
                formMonetariasLiq.setMonetariasLiqInsMonNomMoneda(null);
                ccComunes = new CComunes();
                listInformacionCuentas = ccComunes.getDetalleDeCuentasScotaibank(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(), formMonetariasLiq.getMonetariasLiqInsSubContrato().toString());
                if (getListInformacionCuentas().isEmpty()) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La persona no tiene parámetro de liquidación...");
                }
            } catch (AbstractMethodError Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_CargarCuentas()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onMonetariasLiquidacion_CargarDetalleDeCuentas() {
            try {
                this.obtieneCuentaSeleccionada();
                if (cuentaSeleccionada != null) {
                    formMonetariasLiq.setMonetariasLiqInsAdcCuenta(cuentaSeleccionada.getCuenta());
                    formMonetariasLiq.setMonetariasLiqInsAdcSucursal(new Long(cuentaSeleccionada.getSucursal()));
                    formMonetariasLiq.setMonetariasLiqInsCveDescClave(cuentaSeleccionada.getBanco());
                    formMonetariasLiq.setMonetariasLiqInsAdcNumMoneda(cuentaSeleccionada.getAdcNumMoneda());
                    formMonetariasLiq.setMonetariasLiqInsMonNomMoneda(cuentaSeleccionada.getMoneda());
                } else {
                    formMonetariasLiq.setMonetariasLiqInsAdcCuenta(null);
                    formMonetariasLiq.setMonetariasLiqInsAdcSucursal(null);
                    formMonetariasLiq.setMonetariasLiqInsCveDescClave(null);
                    formMonetariasLiq.setMonetariasLiqInsAdcNumMoneda(null);
                    formMonetariasLiq.setMonetariasLiqInsMonNomMoneda(null);
                }
                if (getListInformacionCuentas().isEmpty()) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error al cargar detalle de cuentas...");
                }
            } catch (AbstractMethodError Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_CargarDetalleDeCuentas()";
                logger.error(mensajeError);
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onMonetariasLiquidacion_CargarParametrosLiquidacion() {
            List<FacesMessage> mensajeDeError = new ArrayList<>();
            try {
                if (tipoPersonaSelected == null ) {
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Persona no puede estar vacío..."));
                }
                if (conceptoSelected == null ) {
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Concepto no puede estar vacío..."));
                }
                if (nombrePersonaSelected == null ){
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Nombre persona no puede estar vacío..."));
                }
                if (cuentaSelected == null ) {
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cuenta Cuenta no puede estar vacío..."));
                }
                if (mensajeDeError.isEmpty()) {
                    obtieneNombrePersonaSeleccionado();
                   
                    ccMonetariasoperacionesOtrosBancos = new CCMonetariasoperacionesOtrosBancos();
                    parametrosLiquidacionBean = ccMonetariasoperacionesOtrosBancos.obtenParametrosLiquidacion(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(), tipoPersonaSelected, getNombreSeleccionado().getBeneficiarioNumero());
                    if (parametrosLiquidacionBean != null) {
                        tipoPagoSelected = Integer.toString(parametrosLiquidacionBean.getPAL_CVE_TIPO_LIQ());
                        monedaSelected = Integer.toString(parametrosLiquidacionBean.getPAL_NUM_MONEDA());
                        informacionCuentaParametroLiquidacion.setCVE_DESC_CLAVE(parametrosLiquidacionBean.getDESC_BANCO());
                        informacionCuentaParametroLiquidacion.setCBA_MONEDA(parametrosLiquidacionBean.getPAL_NUM_MONEDA());
                        informacionCuentaParametroLiquidacion.setCBA_NUM_PLAZA(parametrosLiquidacionBean.getPAL_NUM_PLAZA());
                        informacionCuentaParametroLiquidacion.setCBA_NUM_SUCURSAL(parametrosLiquidacionBean.getPAL_NUM_SUCURSAL());
                        informacionCuentaParametroLiquidacion.setCBA_NUM_CUENTA(parametrosLiquidacionBean.getPAL_NUM_CUENTA());
                        informacionCuentaParametroLiquidacion.setCBA_CLABE(parametrosLiquidacionBean.getPAL_CTA_BANXICO());
                        informacionCuentaParametroLiquidacion.setCBA_CVE_TIPO_CTA(parametrosLiquidacionBean.getPAL_CVE_TIPO_CTA());
                        llenaParametrosDeLiquidacion();
                        habilitaDatosCuenta(parametrosLiquidacionBean.getCVE_DESC_CLAVE(), getNombreSeleccionado().getBeneficiarioNumero(), parametrosLiquidacionBean.getPAL_NUM_MONEDA());
                        tipoPagoSeleccionado = listTipoPago.stream()
                                .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(tipoPagoSelected)))
                                .findAny().orElse(null);
                        RequestContext.getCurrentInstance().execute("dlgFormaLiquidar.show();");
                    } else {
                        mensajeDeError.add(mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La persona no cuenta con parámetros de liquidación"));
                    }
                }
            } catch (AbstractMethodError e) {
                logger.error("onMonetariasLiquidacion_CargarParametrosLiquidacion()");
            } finally {
                for (FacesMessage mensaje : mensajeDeError){
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void llenaParametrosDeLiquidacion() {
            informacionCuentaParametroLiquidacion.setCVE_DESC_CLAVE(parametrosLiquidacionBean.getDESC_BANCO());
            informacionCuentaParametroLiquidacion.setCBA_MONEDA(parametrosLiquidacionBean.getPAL_NUM_MONEDA());
            informacionCuentaParametroLiquidacion.setCBA_NUM_PLAZA(parametrosLiquidacionBean.getPAL_NUM_PLAZA());
            informacionCuentaParametroLiquidacion.setCBA_NUM_SUCURSAL(parametrosLiquidacionBean.getPAL_NUM_SUCURSAL());
            informacionCuentaParametroLiquidacion.setCBA_NUM_CUENTA(parametrosLiquidacionBean.getPAL_NUM_CUENTA());
            informacionCuentaParametroLiquidacion.setCBA_CVE_TIPO_CTA(parametrosLiquidacionBean.getPAL_CVE_TIPO_CTA());
        }

        public void habilitaDatosCuenta(String tipoPago, int clavePersona, int claveMoneda) {
            //        fideicomiso = "123456";
            if (tipoPago.equals("SPEUA") || tipoPago.equals("TRANSFERENCIA ELECTRONICA") || tipoPago.equals("TRANSFERENCIAS INTERNACIONALES")) {
                try {
                    setListInformacionCuentaParametroLiquidacion(ccMonetariasoperacionesOtrosBancos.obtenCuentaParametrosLiquidacion(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(), tipoPersonaSelected, clavePersona, claveMoneda));
                } catch (AbstractMethodError e) {
                    mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + "onMonetariasLiquidacion_habilitaDatosCuenta()";
                    logger.error(mensajeError);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "Server internal error");
                }
            }
            if (tipoPago.equals("SPEUA")) {
                setShowAndHideOption1("");
                if (!showAndHideOption2.equals("none")) {
                    setShowAndHideOption2("none");
                }
            } else if (tipoPago.equals("TRANSFERENCIA ELECTRONICA")) {
                setShowAndHideOption1("");
                if (!showAndHideOption2.equals("none")) {
                    setShowAndHideOption2("none");
                }
            } else if (tipoPago.equals("TRANSFERENCIAS INTERNACIONALES")) {
                setShowAndHideOption1("");
                setShowAndHideOption2("");
            } else {
                if (!showAndHideOption1.equals("none")) {
                    setShowAndHideOption1("none");
                }
                if (!showAndHideOption2.equals("none")) {
                    setShowAndHideOption2("none");
                }

            }
        }

        public void changeimport() {
            setImporteTxt(getImporteTxt().replaceAll(",", ""));
            setImporteTxt(getImporteTxt().replace("$", ""));
            if (!"".equals(getImporteTxt()) && onValidaNumerico(getImporteTxt(), "Importe", "D")) {
                if (getImporteTxt().contains(".")) {
                    Integer largo = getImporteTxt().length();
                    if (largo >= getImporteTxt().indexOf(".") + 3) {
                        largo = getImporteTxt().indexOf(".") + 3;
                    }
                    setImporteTxt(getImporteTxt().substring(0, largo));
                }
                setImporte(Double.parseDouble(getImporteTxt()));
                setImporteTxt(formatoImporte2D.format(getImporte()));
            } else {
                setImporte(null);
                setImporteTxt(null);
            }
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }

        }

        private Boolean onValidaNumerico(String sEntrada, String campo, String tipo) {
            Boolean bSalida = false;
            try {

                if ("I".equals(tipo)) {
                    Integer.parseInt(sEntrada);
                    bSalida = true;
                }
                if ("L".equals(tipo)) {
                    Long.parseLong(sEntrada);
                    bSalida = true;
                }
                if ("D".equals(tipo)) {
                    Double.parseDouble(sEntrada);
                    if (Double.parseDouble(sEntrada) > 99999999999999.99) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo " + campo + "  Supera el valor permitido");
                    } else {
                        bSalida = true;
                    }
                }
                if ("S".equals(tipo)) {
                    Short.parseShort(sEntrada);
                    bSalida = true;
                }
            } catch (NumberFormatException Err) {
                if(!"Secuencia".equals(campo)){
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El " + campo + "  debe ser un campo numérico...");
                }else{
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Secuencia del Nombre de Archivo no es Numérico ");
                
                }
            }
            return bSalida;
        }

        public void changeCuentaParametroLiquidacion() {
            obtieneNombrePersonaSeleccionado();
            tipoPagoSeleccionado = listTipoPago.stream()
                    .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(tipoPagoSelected)))
                    .findAny().orElse(null);
            habilitaDatosCuenta(tipoPagoSeleccionado.getClaveDescripcion(), nombreSeleccionado.getBeneficiarioNumero(), parametrosLiquidacionBean.getPAL_NUM_MONEDA());

        }

        public void guardaLiquidacionOperadaOtrosBancos() {
            try {
                if (tipoPersonaSelected == null) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Persona no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    validacion = Boolean.FALSE;
                }
                if (conceptoSelected == null) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Concepto no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    validacion = Boolean.FALSE;
                }
                if (nombrePersonaSelected == null) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Nombre persona no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    validacion = Boolean.FALSE;
                }
                if (cuentaSelected == null) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Cuenta no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    validacion = Boolean.FALSE;
                }

                if (getImporteTxt().isEmpty()) {
                    setImporteTxt(null);
                    setImporte(null);
                    validaciones.put("Fiduciario", "El campo Importe no puede estar vacío...");
                    CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    validacion = Boolean.FALSE;
                } else {
                    setImporteTxt(getImporteTxt().replaceAll(",", ""));
                    setImporteTxt(getImporteTxt().replace("$", ""));
                    try {
                        setImporte(Double.parseDouble(getImporteTxt()));
                        if (getImporte() <= 0.0) {
                            validaciones.put("Fiduciario", "El campo Importe debe ser mayor a Cero...");
                            validacion = Boolean.FALSE;
                            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                        }
                    } catch (NumberFormatException Err) {
                        setImporteTxt(null);
                        setImporte(null);
                            validacion = Boolean.FALSE;
                         validaciones.put("Fiduciario", "El Importe debe ser un campo numérico...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                }

                if (parametrosLiquidacionBean == null || parametrosLiquidacionBean.getCVE_DESC_CLAVE() == null) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccionar Parámetros de Liquidación");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    validacion = Boolean.FALSE;
                } else {
                    if ((tipoPagoSeleccionado.getClaveDescripcion().equals("SPEUA") && informacionCuentaParametroLiquidacion.getCBA_NUM_BANCO().equals(44))
                            || (tipoPagoSeleccionado.getClaveDescripcion().equals("TRANSFERENCIA ELECTRONICA") && !informacionCuentaParametroLiquidacion.getCBA_NUM_BANCO().equals(44))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Solo se permite  TRANSFERENCIA ELECTRONICA A SCOTIABANK"
                                + " y SPEUA a otros bancos");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                    if (formMonetariasLiq.getMonetariasLiqInsAdcCuenta() != null) {
                        ccMonetariasLiquidacion = new CMonetariasLiquidacion();
                        Double saldoActual = ccMonetariasLiquidacion.getSaldoActualCuentaOrigen(formMonetariasLiq);
                        //double tolerancia = 1e-10;
//                    if ((Math.abs(saldoActual - importe) < 0tolerancia)){

                        if (importe != null && importe > saldoActual) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no tiene liquidez " + parametrosLiquidacionBean.getMON_NOM_MONEDA());
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    }

                }

//                if ((parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("SPEUA") && parametrosLiquidacionBean.getPAL_NUM_BANCO().equals(44))
//                        || (parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("TRANSFERENCIA ELECTRONICA") && !parametrosLiquidacionBean.getPAL_NUM_BANCO().equals(44))) {
//                    validacion = Boolean.FALSE;
//                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Solo se permite  TRANSFERENCIA ELECTRONICA A SCOTIABANK"
//                            + "y SPEUA a otros bancos");
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
                ccMonetariasLiquidacion = null;

                if (validacion.equals(Boolean.TRUE)) {
                    String Horario = ccMonetariasoperacionesOtrosBancos.obtieneHorario(parametrosLiquidacionBean.getPAL_CVE_TIPO_LIQ(),
                            informacionCuentaParametroLiquidacion.getCBA_NUM_BANCO(), "I", 111);
                    if (!Horario.equals(new String())) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", Horario);
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);

                    }
                }
                if (validacion.equals(Boolean.TRUE)) {

                    if (tipoPersonaSelected.equals("FIDEICOMISARIO")) {
                        numeroPago = ccMonetariasoperacionesOtrosBancos.obtieneSecuenciaDePagoBeneficiario(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(),
                                formMonetariasLiq.getMonetariasLiqInsSubContrato().toString(),
                                nombreSeleccionado.getBeneficiarioNumero(),
                                formMonetariasLiq.getMonetariasLiqInsNumFolioInst().toString(),
                                conceptoSelected);
                    } else {
                        numeroPago = ccMonetariasoperacionesOtrosBancos.obtieneSecuenciaDePagoTercero(formMonetariasLiq.getMonetariasLiqInsNumContrato().toString(),
                                formMonetariasLiq.getMonetariasLiqInsSubContrato().toString(),
                                nombreSeleccionado.getBeneficiarioNumero(),
                                formMonetariasLiq.getMonetariasLiqInsNumFolioInst().toString(),
                                conceptoSelected);
                    }
                    if (numeroPago == 0) {

                        onMonetariasLiquidacion_Aplica_Operacion();
                    } else {
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("frmMonetariasLiq:dlgPopUpConfirma");
                        context.execute("dlgPopUpConfirma.show();");
                    }
                }
            } catch (SQLException err) {
                mensajeError += "Descripción: " + err.getMessage() + nombreObjeto + "guardaLiquidacionOperadaOtrosBancos()";
                logger.error(mensajeError);
            }
        }

        public void onMonetariasLiquidacion_Aplica_Operacion() {
            try {
                SPN_PROC_LIQ_IND_Bean spnBean = new SPN_PROC_LIQ_IND_Bean();
                Map<String, String> responseSPProcesa = new HashMap<>();

                spnBean.setIUSUARIO(usuarioNumero);
                spnBean.setINUMCONTRATO(formMonetariasLiq.getMonetariasLiqInsNumContrato().intValue());
                spnBean.setISUBCONTRATO(formMonetariasLiq.getMonetariasLiqInsSubContrato());
                spnBean.setVSCVE_PERS_FID(tipoPersonaSelected);
                spnBean.setVINUM_PERS_FID(nombreSeleccionado.getBeneficiarioNumero());
                spnBean.setVSNOMPERSONA(nombreSeleccionado.getBeneficiarioNombre());
                spnBean.setVDIMPORTEMOV(getImporte());
                spnBean.setVINUM_MONEDA(cuentaSeleccionada.getAdcNumMoneda());
                spnBean.setVDTIPOCAMBIO(1.00);
                spnBean.setVICVE_TIPO_LIQ(tipoPagoSeleccionado.getClaveNumero());
                spnBean.setVSTIPOLIQUID(tipoPagoSeleccionado.getClaveDescripcion());
                spnBean.setIFOLIOINSTR(formMonetariasLiq.getMonetariasLiqInsNumFolioInst());
                spnBean.setVINUMPAGOS(1);
                spnBean.setVIPAGOSEFECT(0);
                spnBean.setSCTAORIGEN(cuentaSeleccionada.getCuenta());
                spnBean.setVICPTOPAGO(Integer.parseInt(conceptoSelected));
                spnBean.setVSCPTOPAGO("");
                spnBean.setVSCVE_TIPO_CTA((informacionCuentaParametroLiquidacion.getCBA_CVE_TIPO_CTA() == null) ? "" : informacionCuentaParametroLiquidacion.getCBA_CVE_TIPO_CTA());
                spnBean.setVINUM_BANCO((informacionCuentaParametroLiquidacion.getCBA_NUM_BANCO() == null) ? 0 : informacionCuentaParametroLiquidacion.getCBA_NUM_BANCO());
                spnBean.setVINUM_PLAZA((informacionCuentaParametroLiquidacion.getCBA_NUM_PLAZA() == null) ? 0 : informacionCuentaParametroLiquidacion.getCBA_NUM_PLAZA());
                spnBean.setVINUM_SUCURSAL((informacionCuentaParametroLiquidacion.getCBA_NUM_SUCURSAL() == null) ? 0 : informacionCuentaParametroLiquidacion.getCBA_NUM_SUCURSAL());
                spnBean.setVINUM_CUENTA((informacionCuentaParametroLiquidacion.getCBA_NUM_CUENTA() == null) ? 0 : informacionCuentaParametroLiquidacion.getCBA_NUM_CUENTA());
                spnBean.setVINUM_PAIS(parametrosLiquidacionBean.getPAL_NUM_PAIS());
                spnBean.setVICTA_BANXICO(0);
                spnBean.setVSDIR_APER_CTA(monedaSelected);
                spnBean.setVINUM_INICIATIVA(0);
                spnBean.setVINUM_CTAM(0);
                spnBean.setVINUM_SCTA(0);
                spnBean.setVINUM_SSCTA(0);
                spnBean.setVINUM_SSSCTA(0);
                spnBean.setVSNOM_AREA("");
                
                DestinoRecepcionBean selConcepto  = conceptos.stream()
                    .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(conceptoSelected)))
                    .findAny().orElse(null);
                spnBean.setVSCONCEPTO(selConcepto.getClaveDescripcion());
                
                spnBean.setVICUENTAS_ORDEN(0);
                spnBean.setVISECUENINSTR(numeroPago + 1);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, fechaRecepcionSistema.getFechaActAño());
                cal.set(Calendar.MONTH, fechaRecepcionSistema.getFechaActMes() - 1);
                cal.set(Calendar.DAY_OF_MONTH, fechaRecepcionSistema.getFechaActDia());
                Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                spnBean.setDTFECHAMOV(fechaOnFuncion);

                spnBean.setSTERMINAL("");
                spnBean.setSIDFORMA("80822010");
                spnBean.setSIDTRANSAC("Liquidacion");

                responseSPProcesa = ccMonetariasoperacionesOtrosBancos.onGuarda_SPN_PROC_LIQ_IND(spnBean);

                if (!responseSPProcesa.isEmpty()) {
                    for (Map.Entry<String, String> sp : responseSPProcesa.entrySet()) {
                        if (!sp.getKey().isEmpty() && sp.getKey().equals("Error")) {
                            reiniciaValidacion(validaciones);
                            validaciones.put("ERROR INTERNO", responseSPProcesa.get("Error"));
                            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
                        }
                        if (!sp.getKey().isEmpty() && sp.getKey().equals("isEjecuto")) {
                            ccMonetariasLiquidacion = new CMonetariasLiquidacion();
                            listMonLiqInstrucFidecomisos = ccMonetariasLiquidacion.getListMonLiqInstrucFideicomisos(formMonetariasLiq);
                            listMonLiqInstrucTerceros = ccMonetariasLiquidacion.getListMonLiqInstrucTerceros(formMonetariasLiq);
                            ccMonetariasLiquidacion = null;
                            RequestContext context = RequestContext.getCurrentInstance();
                            context.update("frmMonetariasLiq:dlgPopUpConfirma");
                            context.execute("dlgPopUpConfirma.hide();");
                            context.update("frmMonetariasLiq:dlgSPExito");
                            context.execute("dlgSPExito.show();");
                        }
                    }
                } else {
                    validaciones.put("ERROR INTERNO", "Ocurrio un error en Base de Datos intente más tarde");
                    CValidacionesUtil.addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
                }
            } catch (SQLException e) {
                mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + "guardaLiquidacionOperadaOtrosBancos()";
                logger.error(mensajeError);
            }

        }

        public void onMonetariasLiquidacion_Cierra_dlgPopUpListaError() {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frmMonetariasLiq:dlgPopUpListaError");
            context.execute("dlgPopUpListaError.hide();");
        }

        public void onMonetariasLiquidacion_Terminar() {
            try {
               geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                setBotonTerminar(Boolean.TRUE); 
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instFolioInst", formMonetariasLiq.getMonetariasLiqInsNumFolioInst().toString());
                                   
                                    RequestContext context = RequestContext.getCurrentInstance();
                                    context.update(":frmMonetariasLiq:cmdInstrucFideAltaCancelar");
                                    context.update(":frmMonetariasLiq:cmdInstrucFideAltaGuardar");

                FacesContext.getCurrentInstance().getExternalContext().redirect("instruccionesFideicomisos.sb?faces-redirect=true");
            } catch (IOException Err) {
                logger.error(Err.getMessage());
            }
        }

        public void onMonetariasLiquidacion_CargarArchivo() {
            try {
                reiniciaValidacion(validaciones);
                validacion = Boolean.TRUE;
                ccMonetariasoperacionesOtrosBancos = new CCMonetariasoperacionesOtrosBancos();
                String Horario = ccMonetariasoperacionesOtrosBancos.obtieneHorario(1, 1, "M", 113);
                if (!Horario.equals(new String())) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", Horario);
                }
                ccMonetariasoperacionesOtrosBancos = null;

                if (validacion.equals(Boolean.TRUE)) {
                    if (null == tipoPersonaSelected) {
                        validaciones.put("Fiduciario", "El campo Tipo persona no puede estar vacío...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                    if (null == conceptoSelected) {
                        validaciones.put("Fiduciario", "El campo Concepto no puede estar vacío...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                    if (null == cuentaSeleccionada) {
                        validaciones.put("Fiduciario", "El campo Cuenta no puede estar vacío...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }

                    if (getImporteTxt().isEmpty()) {
                        setImporteTxt(null);
                        setImporte(null);
                        validaciones.put("Fiduciario", "El campo Importe no puede estar vacío...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    } else {
                        setImporteTxt(getImporteTxt().replaceAll(",", ""));
                        setImporteTxt(getImporteTxt().replace("$", ""));
                        try {
                            setImporte(Double.parseDouble(getImporteTxt()));
                            if (getImporte() <= 0.0) {
                                validaciones.put("Fiduciario", "El campo Importe debe ser mayor a Cero");
                                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                            }
                        } catch (NumberFormatException Err) {
                            setImporteTxt(null);
                            setImporte(null);
                            validaciones.put("Fiduciario", "El Importe debe ser un campo numérico...");
                            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                        }
                    }
                    if (!validaciones.isEmpty()) {
                        validacion = Boolean.FALSE;
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    ccMonetariasLiquidacion = new CMonetariasLiquidacion();
                    Double saldoActual = ccMonetariasLiquidacion.getSaldoActualCuentaOrigen(formMonetariasLiq);
                    if (importe > saldoActual) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Importe", "El importe ingresado es mayor al saldo actual de la cuenta seleccionada");
                    }

                    ccMonetariasLiquidacion = null;
                    if (validacion.equals(Boolean.TRUE)) {
                        RequestContext.getCurrentInstance().execute("dlgMonLiqCargaMasiva.show();");
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }

        }

        public void onMonetariasLiquidacion_ArchivoTransfiere(FileUploadEvent event) {
            try {
                arrArchivo = new String[Short.MAX_VALUE]; //32,767 (tamaño del arreglo)

                //Nos vamos por la piedritas por culpa de IE
                archivoNombre = event.getFile().getFileName().replace(" ", "_");

                if (!validaArchivoNombre(archivoNombre)) {
                   return;
                }
                archivoLinea = new String();
                archivo = new File(archivoNombre);
                archivoNombre = System.getProperty("java.io.tmpdir").concat("/").concat(archivo.getName());
                archivo = null;

                archivo = new File(archivoNombre);
                fos = new FileOutputStream(archivo);
                fos.write(event.getFile().getContents());
                fos.close();

                //La informaciòn del archivo transferido la subimos a un arreglo unidimensional
                ccMonetariasLiquidacion = new CMonetariasLiquidacion();
                Map<String, String> responseSPProcesa = new HashMap<>();
                if (onMonetariasLiquidacion_LlenaInformacionDesdeArchivo().equals(Boolean.TRUE)) {

                    Map<String, String> existeArchivoCargadoPreviamente = ccMonetariasLiquidacion.onMonetariasLiquidacion_ValidarAchivoCargado(archivo.getName());
                    if (existeArchivoCargadoPreviamente.isEmpty()) {
//                        valorRetorno = ccMonetariasLiquidacion.onMonetariasLiquidacion_RegistrarAchivoCargado(archivo.getName(), usuarioNumero);
                        ccMonetariasLiquidacion.onMonetariasLiquidacion_BorrarCargaAnterior(usuarioNumero);
                    } else {
                        valorRetorno = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "EL ARCHIVO: " + archivo.getName() + " YA FUE APLICADO EL : " + existeArchivoCargadoPreviamente.get("ARL_FECHA") + " POR EL USUARIO: " + existeArchivoCargadoPreviamente.get("ARL_USUARIO"));
                    }

                    if (valorRetorno.equals(Boolean.TRUE)) {
                        valorRetorno = ccMonetariasLiquidacion.onMonetariasLiquidacion_CargarTablaTemporalCargaInterfaz(arrArchivo, usuarioNumero, archivo.getName());
                    }

                    if (valorRetorno.equals(Boolean.TRUE)) {
                        SPN_PROC_LOTE_LIQ_Bean spnProcLoteLiqBean = new SPN_PROC_LOTE_LIQ_Bean();
                        spnProcLoteLiqBean.setIUSUARIO(new Long(usuarioNumero));
                        spnProcLoteLiqBean.setINUMCONTRATO(formMonetariasLiq.getMonetariasLiqInsNumContrato());
                        spnProcLoteLiqBean.setISUBCONTRATO(formMonetariasLiq.getMonetariasLiqInsSubContrato());
                        spnProcLoteLiqBean.setICPTOPAGO(Integer.parseInt(conceptoSelected));
                        spnProcLoteLiqBean.setSNOMPROCESO("PAGO MASIVO");
                        spnProcLoteLiqBean.setDTFECHA(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                        spnProcLoteLiqBean.setSNOMARCHIVO(archivo.getName());
                        spnProcLoteLiqBean.setSTIPOPERSONA(tipoPersonaSelected);
                        spnProcLoteLiqBean.setIFOLIOINSTR(formMonetariasLiq.getMonetariasLiqInsNumFolioInst().longValue());
                        spnProcLoteLiqBean.setDVALORLOTE(importe);
                        spnProcLoteLiqBean.setIMONEDA(cuentaSeleccionada.getAdcNumMoneda());
                        spnProcLoteLiqBean.setSCTAORIGEN(formMonetariasLiq.getMonetariasLiqInsAdcCuenta());
                        spnProcLoteLiqBean.setSIDFORMA("LIQUIDACION");
                        spnProcLoteLiqBean.setSTERMINAL(usuarioTerminal);
                        responseSPProcesa = ccMonetariasLiquidacion.onEjecutarLiqMasiva_SPN_PROC_LOTE_LIQ(spnProcLoteLiqBean);

                        if (!responseSPProcesa.isEmpty()) {
                            for (Map.Entry<String, String> sp : responseSPProcesa.entrySet()) {
                                if (!sp.getKey().isEmpty() && sp.getKey().equals("Error")) {
                                    listaErr = new ArrayList<>(); 
                                    listaErr.add(responseSPProcesa.get("Error"));
                                    List<String> listaErr1 = ccMonetariasLiquidacion.getListError(usuarioNumero);
                                    for (int ind = 0; ind < listaErr1.size(); ind++) {
                                        listaErr.add(listaErr1.get(ind));
                                    }

                                    RequestContext context = RequestContext.getCurrentInstance();
                                    context.execute("dlgMonLiqCargaMasiva.hide();");
                                    context.update("frmMonetariasLiq:dlgPopUpListaError");
                                    context.execute("dlgPopUpListaError.show();");
                                }
                                if (!sp.getKey().isEmpty() && sp.getKey().equals("isEjecuto")) {
                                    valorRetorno = ccMonetariasLiquidacion.onMonetariasLiquidacion_RegistrarAchivoCargado(archivo.getName(), usuarioNumero);
                                    RequestContext context = RequestContext.getCurrentInstance();
                                    context.execute("dlgMonLiqCargaMasiva.hide();");
                                    context.update("frmMonetariasLiq:dlgSPExito2");
                                    context.execute("dlgSPExito2.show();");
                                }
                            }
                        } else {
                            reiniciaValidacion(validaciones);
                            validaciones.put("ERROR INTERNO", "Ocurrio un error en Base de Datos intente más tarde");
                            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                        }
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No fue posible transferir el archivo.");
                }
                //Borramos el archivo
                if (archivo.exists()) {
                    archivo.delete();
                }
                archivo = null;
            } catch (IOException | SQLException | NumberFormatException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_ArchivoTransfiere()";
                logger.error(mensajeError);
            } finally {
                onFinalizaObjetos();
                if (mensaje != null) {
                    RequestContext.getCurrentInstance().execute("dlgMonLiqCargaMasiva.hide();");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
        private Boolean validaArchivoNombre(String nomArchivo) {
            Boolean validacion = Boolean.TRUE;
            //int lenArchivo = nomArchivo.length(); CAVC
            if (validacion && nomArchivo.length() != 18) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Longitud del Nombre de Archivo inválida");
                validacion = Boolean.FALSE;
            }

            if (validacion && !"LIQ".equals(nomArchivo.substring(0, 3).toUpperCase(Locale.ENGLISH))) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "El prefijo del Nombre de Archivo debe ser LIQ");
                validacion = Boolean.FALSE;
            }
            if (validacion) {
                try {
                    java.util.Date fechaArchivo = dateFormatoddMMyyyy(nomArchivo.substring(3, 11));
                    int diferencia = fechaArchivo.compareTo(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                    if (diferencia != 0) {

                        mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "La fecha del Nombre de Archivo debe ser ".concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                        validacion = Boolean.FALSE;
                    }

                } catch (ParseException err) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "La fecha del Nombre de Archivo debe tener formato ddmmaaaa");
                    validacion = Boolean.FALSE;
                }
            } 
            if (validacion) {
                if (!onValidaNumerico(nomArchivo.substring(12, 14), "Secuencia", "I")) {
                    validacion = Boolean.FALSE;
                }
            }

            return validacion;
        }

        private Boolean onMonetariasLiquidacion_LlenaInformacionDesdeArchivo() {
            Short item = 0;
            try {
                valorRetorno = Boolean.FALSE;
                fis = new FileInputStream(archivoNombre);
                int a = 0;
                while ((a = fis.read()) != -1) {
                    String car = String.valueOf((char) a);
                    if (!car.equals("\n")) {
                        archivoLinea += car;
                    } else {
                        arrArchivo[item] = archivoLinea.trim();
                        item++;
                        archivoLinea = new String();
                    }
                }
                if (!archivoLinea.equals(new String())) {
                    arrArchivo[item] = archivoLinea.trim();
                }
                fis.close();
                valorRetorno = Boolean.TRUE;
            } catch (IOException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_LlenaInformacionDesdeArchivo()";
                logger.error(mensajeError);
            } finally {
                try {
                    fis.close();
                } catch (IOException Err) { logger.error("onMonetariasLiquidacion_LlenaInformacionDesdeArchivo");
                }
            }
            return valorRetorno;
        }

        public void reiniciaValidacion(Map<String, String> validaciones) {
            if (!validaciones.isEmpty()) {
                validaciones.clear();
            }
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
        }
        public void agregarParametrosLiquidacion() {
            tipoPagoSeleccionado = listTipoPago.stream()
                    .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(tipoPagoSelected)))
                    .findAny().orElse(null);
            validacion = Boolean.TRUE;
            if (tipoPagoSeleccionado.getClaveDescripcion().equals("SPEUA") && !monedaSelected.equalsIgnoreCase("1")) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "SPEUA solo acepta moneda nacional...");
                validacion = Boolean.FALSE;
            }  
            if (validacion && !monedaSelected.equals(formMonetariasLiq.getMonetariasLiqInsAdcNumMoneda().toString())) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La moneda de la cuenta no corresponde con la moneda elegida");
                validacion = Boolean.FALSE;
            }
            if (validacion) {
                RequestContext.getCurrentInstance().execute("dlgFormaLiquidar.hide();");
            } else {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }

            }

        }

    }


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBMonetariasLiquidacion() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    @PostConstruct
    public void onPostConstruct() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                //System.out.println("Sesión no valida ...");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                //efp
                String instrucFideInsNumContrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumContrato");
                String instrucFideInsSubContrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsSubContrato");
                String instrucFideInsNumFolioInst = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumFolioInst");
                //String instrucFideCtoNomContrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideCtoNomContrato"); CAVC
                //String instrucFideInsCveTipoInstr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsCveTipoInstr"); CAVC

                formMonetariasLiq.setMonetariasLiqInsNumContrato(new Long(instrucFideInsNumContrato));
                formMonetariasLiq.setMonetariasLiqInsSubContrato(new Integer(instrucFideInsSubContrato));
                formMonetariasLiq.setMonetariasLiqInsNumFolioInst(new Integer(instrucFideInsNumFolioInst));

                validaciones = new HashMap<>();
                ccComunes = new CComunes();
                ccMonetariasLiquidacion = new CMonetariasLiquidacion();
                ccMonetariasoperacionesOtrosBancos = new CCMonetariasoperacionesOtrosBancos();
                parametrosLiquidacionBean = new ParametrosLiquidacionBean();
                fechaRecepcionSistema = ccComunes.getFechaRecepcionSistema();
                conceptos = ccComunes.getClavesConceptos(CLAVE_CONCEPTO);
                listInformacionCuentas = ccComunes.getDetalleDeCuentasScotaibank(instrucFideInsNumContrato, instrucFideInsSubContrato);
                listTipoPago = ccComunes.getClavesconceptosLiq(CLAVE_TIPO_PAGO);
                listMonedas = ccComunes.getMonedas();
                listMonLiqInstrucFidecomisos = ccMonetariasLiquidacion.getListMonLiqInstrucFideicomisos(formMonetariasLiq);
                listMonLiqInstrucTerceros = ccMonetariasLiquidacion.getListMonLiqInstrucTerceros(formMonetariasLiq);

                formatoImporte2D = new DecimalFormat("$###,###.##");
                formatoImporte2D.setMaximumFractionDigits(2);
                formatoImporte2D.setMinimumFractionDigits(2);
                geneFecha.setGenericoFecha00(new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            }
        } catch (IOException | NumberFormatException | SQLException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onPostConstruct()";
            logger.error(mensajeError);
        } finally {
            if (oClave != null) {
                oClave = null;
            }
        }
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   B I E N E S   F I D E I C O M
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin -efp
    public void onMonetariasLiquidacion_Terminar() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_Terminar();
        oMonetariasLiquidacion = null;
    }

    public void onMonetariasLiquidacion_Cargar_Beneficiario_O_Tercero() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_Cargar_Beneficiario_O_Tercero();
        oMonetariasLiquidacion = null;
    }

    public void onMonetariasLiquidacion_Aplica_Operacion() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_Aplica_Operacion();
        oMonetariasLiquidacion = null;
    }
    //end -efp   

    //begin -efp
    public void onMonetariasLiquidacion_CargarCuentas() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_CargarCuentas();
        oMonetariasLiquidacion = null;
    }
    //end -efp     

    //begin -efp
    public void onMonetariasLiquidacion_CargarDetalleDeCuentas() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_CargarDetalleDeCuentas();
        oMonetariasLiquidacion = null;
    }
    //end -efp

    //begin -efp
    public void onMonetariasLiquidacion_Cierra_dlgPopUpListaError() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_Cierra_dlgPopUpListaError();
        oMonetariasLiquidacion = null;

    }

    public void onMonetariasLiquidacion_CargarParametrosLiquidacion() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_CargarParametrosLiquidacion();
        oMonetariasLiquidacion = null;
    }
    //end -efp       

    //begin -efp
    public void onMonetariasLiquidacion_GuardarLiquidacion() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.guardaLiquidacionOperadaOtrosBancos();
        oMonetariasLiquidacion = null;
    }
    //end -efp       

    //Begin - Integracion de Parametros de Liquidacion
    public void onRowSelectParametrosLiquidacion(SelectEvent event) {
        informacionCuentaParametroLiquidacion = (InformacionCuentasParametrosLiquidacionBean) event.getObject();
    }

    public void changeCuentaParametroLiquidacion() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.changeCuentaParametroLiquidacion();
        oMonetariasLiquidacion = null;
    }
    public void agregarParametrosLiquidacion() {
      
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.agregarParametrosLiquidacion();
        oMonetariasLiquidacion = null;

    }

    public void cierraParametrosLiquidacion() {
        parametrosLiquidacionBean = null;
        RequestContext.getCurrentInstance().execute("dlgFormaLiquidar.hide();");
    }

    public void onMonetariasLiquidacion_CargarArchivo() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_CargarArchivo();
        oMonetariasLiquidacion = null;
    }

    public void onMonetariasLiquidacion_ArchivoTransfiere(FileUploadEvent event) {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.onMonetariasLiquidacion_ArchivoTransfiere(event);
        oMonetariasLiquidacion = null;
    }

    //End -efp 
    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }

    public void changeimport() {
        oMonetariasLiquidacion = new monetariasLiquidacion();
        oMonetariasLiquidacion.changeimport();
        oMonetariasLiquidacion = null;
    }

    public static java.util.Date dateFormatoddMMyyyy(String str) throws ParseException {
        DateFormat dfInput = new SimpleDateFormat("ddMMyyyy");
        java.util.Date date = dfInput.parse(str);
        return date;
    }

}