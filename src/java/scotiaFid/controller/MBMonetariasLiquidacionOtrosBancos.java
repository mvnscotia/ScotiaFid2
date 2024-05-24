/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.FechaBean;
import scotiaFid.bean.InformacionCuentasMonetariasOperacion;
import scotiaFid.bean.InformacionCuentasParametrosLiquidacionBean;
import scotiaFid.bean.MonedasBean;
import scotiaFid.bean.NombreBean;
import scotiaFid.bean.ParametrosLiquidacionBean;
import scotiaFid.bean.SPN_PROC_LIQ_IND_Bean;
import scotiaFid.dao.CCMonetariasoperacionesOtrosBancos;
import scotiaFid.dao.CComunes;
import scotiaFid.util.CValidacionesUtil;
import scotiaFid.util.LogsContext;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "mbMonetariasLiquidacionOtrosBancos")
@ViewScoped
public class MBMonetariasLiquidacionOtrosBancos implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBMonetariasLiquidacionOtrosBancos.class);

    private static final String CLAVE_CONCEPTO = "128";
    private static final String CLAVE_TIPO_PAGO = "81";
    private static final Double VALOR_MAX = 999999999999.99;
    Map<String, String> validaciones = null;
    private FacesMessage mensaje;
    private String conceptoSelected;
    private DestinoRecepcionBean conceptoSelectedObj;
    private String tipoPersonaSelected;
    private String nombrePersonaSelected;
    private String tipoPagoSelected;
    private String monedaSelected;
    private String showAndHideOption1 = "none";
    private String showAndHideOption2 = "none";
//    private String showAndHideOption1 = "";
//    private String showAndHideOption2 = "";
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

    @ManagedProperty(value = "#{informacionCuentasMonetariasOperacion}")
    private InformacionCuentasMonetariasOperacion informacionCuenta;
    @ManagedProperty(value = "#{informacionCuentasParametrosLiquidacionBean}")
    private InformacionCuentasParametrosLiquidacionBean informacionCuentaParametroLiquidacion;

    CComunes ccComunes = null;
    CCMonetariasoperacionesOtrosBancos ccMonetariasoperacionesOtrosBancos = null;
    ParametrosLiquidacionBean parametrosLiquidacionBean = null;

    private String fideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumContrato");
    private String subFideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsSubContrato");
    private String contrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideCtoNomContrato");
    private String folioInstruccion = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumFolioInst");
    private Integer usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");

    public MBMonetariasLiquidacionOtrosBancos() throws SQLException {
        LogsContext.FormatoNormativo();
        validaciones = new HashMap<>();
        ccComunes = new CComunes();
        ccMonetariasoperacionesOtrosBancos = new CCMonetariasoperacionesOtrosBancos();
        parametrosLiquidacionBean = new ParametrosLiquidacionBean();
        fechaRecepcionSistema = ccComunes.getFechaRecepcionSistema();
        conceptos = ccComunes.getClavesPorNumClave(CLAVE_CONCEPTO);
        listInformacionCuentas = ccComunes.getInformacionCuentas(fideicomiso, subFideicomiso);
        listTipoPago = ccComunes.getClavesconceptosLiqOtrosBancos(CLAVE_TIPO_PAGO);
        listMonedas = ccComunes.getMonedas();
    }

    public void nombreChangeListener() {
        if (tipoPersonaSelected != null) {
            listNombre = ccComunes.obtenParticipantePorRolFid(fideicomiso, tipoPersonaSelected);
            if (listNombre.isEmpty()) {
                reiniciaValidacion(validaciones);
                validaciones.put("Fiduciario", "El Tipo de Persona seleccionado no contiene información");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
            }
        } else {
            listNombre.clear();
            nombrePersonaSelected = null;
            nombreSeleccionado = null;
        }
    }

    public void nombrePersonaChangeListener() {
//        fideicomiso = "123456"; //Eliminar
        parametrosLiquidacionBean = new ParametrosLiquidacionBean();
    }

    public void onRowSelect(SelectEvent event) {
        informacionCuenta = (InformacionCuentasMonetariasOperacion) event.getObject();
    }

    public void onRowSelectParametrosLiquidacion(SelectEvent event) {
        informacionCuentaParametroLiquidacion = (InformacionCuentasParametrosLiquidacionBean) event.getObject();
    }

    public void reiniciaValidacion(Map<String, String> validaciones) {
        if (!validaciones.isEmpty()) {
            validaciones.clear();
        }
    }

    public void cierraParametrosLiquidacion() {
        RequestContext.getCurrentInstance().execute("dlgFormaLiquidar.hide();");
    }

    public void obtieneNombrePersonaSeleccionado() {
        nombreSeleccionado = listNombre.stream()
                .filter(nombre -> nombre.getBeneficiarioNumero().equals(Integer.parseInt(nombrePersonaSelected)))
                .findAny().orElse(null);

    }

    public void obtenParametrosLiquidacion() {
        reiniciaValidacion(validaciones);
        if (nombrePersonaSelected == null) {
            validaciones.put("Fiduciario", "El campo Nombre no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (informacionCuenta == null) {
            validaciones.put("Fiduciario", "El campo Cuenta no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }

        if (validaciones.isEmpty()) {
            obtieneNombrePersonaSeleccionado();
            parametrosLiquidacionBean = new ParametrosLiquidacionBean();
//            fideicomiso = "11005360";//Eliminar
            try {
                parametrosLiquidacionBean = ccMonetariasoperacionesOtrosBancos.obtenParametrosLiquidacion(fideicomiso, tipoPersonaSelected, nombreSeleccionado.getBeneficiarioNumero());
            } catch (AbstractMethodError e) {
                reiniciaValidacion(validaciones);
                validaciones.put("Server internal error", e.getMessage());
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
            }

            if (parametrosLiquidacionBean != null) {
                if (parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("TRANSFERENCIA ELECTRONICA") || parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("TRANSFERENCIAS INTERNACIONALES")) {
                    reiniciaValidacion(validaciones);
                    validaciones.put("Fiduciario", "El párametro de TRANSFERENCIA ELECTRÓNICA no aplica para pagos de otros bancos");
                    CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    parametrosLiquidacionBean = null;
                } else {
                    tipoPagoSelected = Integer.toString(parametrosLiquidacionBean.getPAL_CVE_TIPO_LIQ());
                    monedaSelected = Integer.toString(parametrosLiquidacionBean.getPAL_NUM_MONEDA());
                    llenaParametrosDeLiquidacion();
                    habilitaDatosCuenta(parametrosLiquidacionBean.getCVE_DESC_CLAVE(), nombreSeleccionado.getBeneficiarioNumero(), parametrosLiquidacionBean.getPAL_NUM_MONEDA());
                    RequestContext.getCurrentInstance().execute("dlgFormaLiquidar.show();");
                }
            } else {
                reiniciaValidacion(validaciones);
                validaciones.put("Fiduciario", "La persona no cuenta con Parámetros de Liquidación");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
            }
        }
    }

    public void habilitaDatosCuenta(String tipoPago, int clavePersona, int claveMoneda) {
//        fideicomiso = "123456";
        if (tipoPago.equals("SPEUA") || tipoPago.equals("TRANSFERENCIA ELECTRONICA") || tipoPago.equals("TRANSFERENCIAS INTERNACIONALES")) {
            try {
                listInformacionCuentaParametroLiquidacion = ccMonetariasoperacionesOtrosBancos.obtenCuentaParametrosLiquidacion(fideicomiso, tipoPersonaSelected, clavePersona, claveMoneda);
            } catch (AbstractMethodError e) {
                reiniciaValidacion(validaciones);
                validaciones.put("Server internal error", e.getMessage());
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
            }
        }
        if (tipoPago.equals("SPEUA")) {
            if (parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("SPEUA")) {
                llenaParametrosDeLiquidacion();
            } else {
                limpiaParametrosDeLiquidacion();
            }
            showAndHideOption1 = "";
            if (!showAndHideOption2.equals("none")) {
                showAndHideOption2 = "none";
            }
        } else if (tipoPago.equals("TRANSFERENCIA ELECTRONICA")) {
            if (parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("TRANSFERENCIA ELECTRONICA")) {
                llenaParametrosDeLiquidacion();
            } else {
                limpiaParametrosDeLiquidacion();
            }
            showAndHideOption1 = "";
            if (!showAndHideOption2.equals("none")) {
                showAndHideOption2 = "none";
            }
        } else if (tipoPago.equals("TRANSFERENCIAS INTERNACIONALES")) {
            if (parametrosLiquidacionBean.getCVE_DESC_CLAVE().equals("TRANSFERENCIAS INTERNACIONALES")) {
                llenaParametrosDeLiquidacion();
            } else {
                limpiaParametrosDeLiquidacion();
            }
            showAndHideOption1 = "";
            showAndHideOption2 = "";
        } else {
            limpiaParametrosDeLiquidacion();
            if (!showAndHideOption1.equals("none")) {
                showAndHideOption1 = "none";
            }
            if (!showAndHideOption2.equals("none")) {
                showAndHideOption2 = "none";
            }
        }
    }

    public void llenaParametrosDeLiquidacion() {
        informacionCuentaParametroLiquidacion.setCVE_DESC_CLAVE(parametrosLiquidacionBean.getDESC_BANCO());
        informacionCuentaParametroLiquidacion.setCBA_MONEDA(parametrosLiquidacionBean.getPAL_NUM_MONEDA());
        informacionCuentaParametroLiquidacion.setCBA_NUM_PLAZA(parametrosLiquidacionBean.getPAL_NUM_PLAZA());
        informacionCuentaParametroLiquidacion.setCBA_NUM_SUCURSAL(parametrosLiquidacionBean.getPAL_NUM_SUCURSAL());
        informacionCuentaParametroLiquidacion.setCBA_NUM_CUENTA(parametrosLiquidacionBean.getPAL_NUM_CUENTA());
        informacionCuentaParametroLiquidacion.setCBA_CVE_TIPO_CTA(parametrosLiquidacionBean.getPAL_CVE_TIPO_CTA());
        informacionCuentaParametroLiquidacion.setCBA_CLABE(parametrosLiquidacionBean.getPAL_CTA_BANXICO());
    }

    public void limpiaParametrosDeLiquidacion() {
        informacionCuentaParametroLiquidacion.setCVE_DESC_CLAVE("");
        informacionCuentaParametroLiquidacion.setCBA_MONEDA(0);
        informacionCuentaParametroLiquidacion.setCBA_NUM_PLAZA(0);
        informacionCuentaParametroLiquidacion.setCBA_NUM_SUCURSAL(0);
        informacionCuentaParametroLiquidacion.setCBA_NUM_CUENTA(0.0);
        informacionCuentaParametroLiquidacion.setCBA_CVE_TIPO_CTA("");
        informacionCuentaParametroLiquidacion.setCBA_NUM_SEC_CTA(0);
        informacionCuentaParametroLiquidacion.setCBA_NUM_BANCO(0);
    }

    public void changeCuentaParametroLiquidacion() {
        obtieneNombrePersonaSeleccionado();
        tipoPagoSeleccionado = listTipoPago.stream()
                .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(tipoPagoSelected)))
                .findAny().orElse(null);
        habilitaDatosCuenta(tipoPagoSeleccionado.getClaveDescripcion(), nombreSeleccionado.getBeneficiarioNumero(), parametrosLiquidacionBean.getPAL_NUM_MONEDA());

    }

    public void onMonetariasLiquidacionOtrosBancos_Terminar() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instFolioInst", folioInstruccion);
            FacesContext.getCurrentInstance().getExternalContext().redirect("instruccionesFideicomisos.sb?faces-redirect=true");
        } catch (IOException Err) {
            logger.error(Err.getMessage());
        }
    }

    public void guardaLiquidacionOperadaOtrosBancos() {
        reiniciaValidacion(validaciones);
        if (informacionCuenta == null) {
            validaciones.put("Fiduciario", "El campo Cuenta de Cheques no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (conceptoSelected == null) {
            validaciones.put("Fiduciario", "El campo Concepto no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (getImporteTxt().isEmpty()) {
            setImporteTxt(null);
            setImporte(null);
            validaciones.put("Fiduciario", "El campo Importe no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        } else {
//            setImporteTxt(getImporteTxt().replaceAll(",", ""));
//            setImporteTxt(getImporteTxt().replace("$", ""));  
            try {
                setImporte(Double.parseDouble(getImporteTxt().replace("$", "").replaceAll(",", "")));
                if (getImporte() <= 0.0) {
                    validaciones.put("Fiduciario", "El campo Importe debe ser mayor a Cero");
                    CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                } else {
                    if (getImporte() > VALOR_MAX) {
                        validaciones.put("Fiduciario", "El campo Importe no puede ser mayor a 999,999,999,999.99...");
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                }
            } catch (NumberFormatException Err) {
                setImporteTxt(null);
                setImporte(null);
                validaciones.put("Fiduciario", "El Importe debe ser un campo numérico...");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
            }
        }
        if (tipoPersonaSelected == null) {
            validaciones.put("Fiduciario", "El campo Tipo de Persona no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (nombrePersonaSelected == null) {
            validaciones.put("Fiduciario", "El campo Nombre no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (parametrosLiquidacionBean != null) {
            if (parametrosLiquidacionBean.getCVE_DESC_CLAVE() == null) {
                validaciones.put("Fiduciario", "Selecciona Parametros de Liquidación");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
            }
        } else {
            validaciones.put("Fiduciario", "Selecciona Parametros de Liquidación");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (validaciones.isEmpty()) {
            SPN_PROC_LIQ_IND_Bean spnBean = new SPN_PROC_LIQ_IND_Bean();
            Map<String, String> responseSPProcesa = new HashMap<>();

            spnBean.setIUSUARIO(usuarioNumero);
            spnBean.setINUMCONTRATO(Integer.parseInt(fideicomiso));
            spnBean.setISUBCONTRATO(Integer.parseInt(subFideicomiso));
            spnBean.setVSCVE_PERS_FID(tipoPersonaSelected);
            spnBean.setVINUM_PERS_FID(nombreSeleccionado.getBeneficiarioNumero());
            spnBean.setVSNOMPERSONA(nombreSeleccionado.getBeneficiarioNombre());
            spnBean.setVDIMPORTEMOV(importe);
            spnBean.setVINUM_MONEDA(informacionCuenta.getAdcNumMoneda());
            spnBean.setVDTIPOCAMBIO(1.00);
//            spnBean.setVICVE_TIPO_LIQ(parametrosLiquidacionBean.getPAL_CVE_TIPO_LIQ());
//            spnBean.setVSTIPOLIQUID(parametrosLiquidacionBean.getCVE_DESC_CLAVE());
            spnBean.setVICVE_TIPO_LIQ(tipoPagoSeleccionado.getClaveNumero());
            spnBean.setVSTIPOLIQUID(tipoPagoSeleccionado.getClaveDescripcion());
            spnBean.setIFOLIOINSTR(Integer.parseInt(folioInstruccion));
            spnBean.setVINUMPAGOS(1);
            spnBean.setVIPAGOSEFECT(0);
            spnBean.setSCTAORIGEN(informacionCuenta.getCuenta());

            obtieneCconcetoSeleccionado();

            spnBean.setVICPTOPAGO(conceptoSelectedObj.getClaveNumero());
            spnBean.setVSCPTOPAGO(conceptoSelectedObj.getClaveDescripcion());
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
            spnBean.setVSCONCEPTO("0");
            spnBean.setVICUENTAS_ORDEN(0);
            int numeroPago = 0;

            if (tipoPersonaSelected.equals("FIDEICOMISARIO")) {
                numeroPago = ccMonetariasoperacionesOtrosBancos.obtieneSecuenciaDePagoBeneficiario(fideicomiso,
                        subFideicomiso,
                        nombreSeleccionado.getBeneficiarioNumero(),
                        folioInstruccion,
                        conceptoSelected);
            } else {
                numeroPago = ccMonetariasoperacionesOtrosBancos.obtieneSecuenciaDePagoTercero(fideicomiso,
                        subFideicomiso,
                        nombreSeleccionado.getBeneficiarioNumero(),
                        folioInstruccion,
                        conceptoSelected);
            }

//            int numeroPago = 0;
//            try {
//                numeroPago = ccMonetariasoperacionesOtrosBancos.obtieneSecuenciaDePagoBeneficiario(fideicomiso, subFideicomiso, nombreSeleccionado.getBeneficiarioNumero(), folioInstruccion,conceptoSelectedObj.getClaveNumero().toString());
//            } catch (Exception e) {
//                logger.error("Erro al obtener secuencia de pago, se realiza la asignacion en 0");
//            }
            spnBean.setVISECUENINSTR(numeroPago + 1);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, fechaRecepcionSistema.getFechaActAño());
            cal.set(Calendar.MONTH, fechaRecepcionSistema.getFechaActMes() - 1);
            cal.set(Calendar.DAY_OF_MONTH, fechaRecepcionSistema.getFechaActDia());
            Date fechaOnFuncion = new Date(cal.getTimeInMillis());
            spnBean.setDTFECHAMOV(fechaOnFuncion);

            spnBean.setSTERMINAL("");
            spnBean.setSIDFORMA("80822010");
            spnBean.setSIDTRANSAC("Liquidacion Registro");

            responseSPProcesa = ccMonetariasoperacionesOtrosBancos.onGuarda_SPN_PROC_LIQ_IND(spnBean);

            if (!responseSPProcesa.isEmpty()) {
                for (Map.Entry<String, String> sp : responseSPProcesa.entrySet()) {
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("Error")) {
                        reiniciaValidacion(validaciones);
                        validaciones.put("Fiduciario", responseSPProcesa.get("Error"));
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("isEjecuto")) {
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("frmMonetariasLiquidacionOtrosBancos:dlgSPExito");
                        context.execute("dlgSPExito.show();");
                    }
                }
            } else {
                reiniciaValidacion(validaciones);
                validaciones.put("Fiduciario", "Ocurrio un error en Base de Datos intente más tarde");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
            }
        }

    }

    public void obtieneCconcetoSeleccionado() {
        conceptoSelectedObj = conceptos.stream()
                .filter(conceptos -> conceptos.getClaveNumero().equals(Integer.parseInt(conceptoSelected)))
                .findAny().orElse(null);

    }

    public void agregarParametrosLiquidacion() {
        tipoPagoSeleccionado = listTipoPago.stream()
                .filter(tipoPago -> tipoPago.getClaveNumero().equals(Integer.parseInt(tipoPagoSelected)))
                .findAny().orElse(null);
        Boolean validacion = Boolean.TRUE;
        if (tipoPagoSeleccionado.getClaveDescripcion().equals("SPEUA") && !monedaSelected.equalsIgnoreCase("1")) {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "SPEUA solo acepta moneda nacional");
            validacion = Boolean.FALSE;
        }
        if (validacion && !monedaSelected.equals(informacionCuenta.getAdcNumMoneda().toString())) {
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

    public FechaBean getFechaRecepcionSistema() {
        return fechaRecepcionSistema;
    }

    public void setFechaRecepcionSistema(FechaBean fechaRecepcionSistema) {
        this.fechaRecepcionSistema = fechaRecepcionSistema;
    }

    public List<DestinoRecepcionBean> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<DestinoRecepcionBean> conceptos) {
        this.conceptos = conceptos;
    }

    public String getFideicomiso() {
        return fideicomiso;
    }

    public void setFideicomiso(String fideicomiso) {
        this.fideicomiso = fideicomiso;
    }

    public String getSubFideicomiso() {
        return subFideicomiso;
    }

    public void setSubFideicomiso(String subFideicomiso) {
        this.subFideicomiso = subFideicomiso;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getFolioInstruccion() {
        return folioInstruccion;
    }

    public void setFolioInstruccion(String folioInstruccion) {
        this.folioInstruccion = folioInstruccion;
    }

    public List<InformacionCuentasMonetariasOperacion> getListInformacionCuentas() {
        return listInformacionCuentas;
    }

    public void setListInformacionCuentas(List<InformacionCuentasMonetariasOperacion> listInformacionCuentas) {
        this.listInformacionCuentas = listInformacionCuentas;
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

    public String getTipoPersonaSelected() {
        return tipoPersonaSelected;
    }

    public void setTipoPersonaSelected(String tipoPersonaSelected) {
        this.tipoPersonaSelected = tipoPersonaSelected;
    }

    public String getNombrePersonaSelected() {
        return nombrePersonaSelected;
    }

    public void setNombrePersonaSelected(String nombrePersonaSelected) {
        this.nombrePersonaSelected = nombrePersonaSelected;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public List<NombreBean> getListNombre() {
        return listNombre;
    }

    public void setListNombre(List<NombreBean> listNombre) {
        this.listNombre = listNombre;
    }

    public List<DestinoRecepcionBean> getListTipoPago() {
        return listTipoPago;
    }

    public void setListTipoPago(List<DestinoRecepcionBean> listTipoPago) {
        this.listTipoPago = listTipoPago;
    }

    public String getTipoPagoSelected() {
        return tipoPagoSelected;
    }

    public void setTipoPagoSelected(String tipoPagoSelected) {
        this.tipoPagoSelected = tipoPagoSelected;
    }

    public List<MonedasBean> getListMonedas() {
        return listMonedas;
    }

    public void setListMonedas(List<MonedasBean> listMonedas) {
        this.listMonedas = listMonedas;
    }

    public String getMonedaSelected() {
        return monedaSelected;
    }

    public void setMonedaSelected(String monedaSelected) {
        this.monedaSelected = monedaSelected;
    }

    public NombreBean getNombreSeleccionado() {
        return nombreSeleccionado;
    }

    public void setNombreSeleccionado(NombreBean nombreSeleccionado) {
        this.nombreSeleccionado = nombreSeleccionado;
    }

    public DestinoRecepcionBean getTipoPagoSeleccionado() {
        return tipoPagoSeleccionado;
    }

    public void setTipoPagoSeleccionado(DestinoRecepcionBean tipoPagoSeleccionado) {
        this.tipoPagoSeleccionado = tipoPagoSeleccionado;
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

    public Boolean getCuentaOrden() {
        return cuentaOrden;
    }

    public void setCuentaOrden(Boolean cuentaOrden) {
        this.cuentaOrden = cuentaOrden;
    }

    public List<InformacionCuentasParametrosLiquidacionBean> getListInformacionCuentaParametroLiquidacion() {
        return listInformacionCuentaParametroLiquidacion;
    }

    public void setListInformacionCuentaParametroLiquidacion(List<InformacionCuentasParametrosLiquidacionBean> listInformacionCuentaParametroLiquidacion) {
        this.listInformacionCuentaParametroLiquidacion = listInformacionCuentaParametroLiquidacion;
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

    public String getImporteTxt() {
        return importeTxt;
    }

    public void setImporteTxt(String importeTxt) {
        this.importeTxt = importeTxt;
    }

}
