/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import scotiaFid.bean.MonetariasOperacionOtrosBancosBean;
import scotiaFid.bean.NombreBean;
import scotiaFid.dao.CCMonetariasoperacionesOtrosBancos;
import scotiaFid.dao.CComunes;
import scotiaFid.util.CValidacionesUtil;
import scotiaFid.util.LogsContext;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "mbMonetariasRecepcionOtrosBancos")
@ViewScoped
public class MBMonetariasRecepcionOtrosBancos implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBMonetariasLiquidacionOtrosBancos.class);
    private static final Double VALOR_MAX = 999999999999.99;
    private static final String CLAVE_RECEPCION_CUENTA = "75";
    private static final String CLAVE_FORMA_RECEPCION = "74";
    private static final String CLAVE_TIPO_PERSONA = "29";

    private String fideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumContrato");
    private String subFideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsSubContrato");
    private String contrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideCtoNomContrato");
    private String folioInstruccion = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumFolioInst");

    Map<String, String> validaciones = null;

    private String destinoRecepcionSelected;
    private String formaRecepcionSelect;
    private String tipoPersonaSelected;
    private String nombrePersonaSelected;
    private Double importe;
    private String importeTxt;
    private DecimalFormat formatoImporte2D;
    private FacesMessage mensaje;
    private String folioContable;
    private String fechaAplicacion;
    private FechaBean fechaRecepcionSistema;
    private List<DestinoRecepcionBean> listDestinoRecepcion;
    private List<DestinoRecepcionBean> listFormaRecepcion;
    private List<InformacionCuentasMonetariasOperacion> listInformacionCuentas;
    private List<NombreBean> listNombre;
    private List<DestinoRecepcionBean> listTipoPersona;

    @ManagedProperty(value = "#{informacionCuentasMonetariasOperacion}")
    private InformacionCuentasMonetariasOperacion informacionCuenta;

    CComunes ccomunes = null;
    CCMonetariasoperacionesOtrosBancos ccMonetariasOperacion = null;

    public MBMonetariasRecepcionOtrosBancos() {
        LogsContext.FormatoNormativo();
        validaciones = new HashMap<>();
        ccomunes = new CComunes();
        ccMonetariasOperacion = new CCMonetariasoperacionesOtrosBancos();
        fechaRecepcionSistema = ccomunes.getFechaRecepcionSistema();
        listTipoPersona = ccomunes.getClavesPorNumClave(CLAVE_TIPO_PERSONA);
        listDestinoRecepcion = ccomunes.getClavesPorNumClave(CLAVE_RECEPCION_CUENTA);
        listFormaRecepcion = ccomunes.getClavesPorNumClave(CLAVE_FORMA_RECEPCION);
        listInformacionCuentas = ccomunes.getInformacionCuentas(fideicomiso, subFideicomiso);
    }

    public void nombreChangeListener() {
        listNombre = ccomunes.obtenParticipantePorRolFid(fideicomiso, tipoPersonaSelected);
        if (listNombre.isEmpty()) {
            reiniciaValidacion(validaciones);
            validaciones.put("Fiduciario", "El Tipo de Persona seleccionado no contiene información");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
    }

    public void reiniciaValidacion(Map<String, String> validaciones) {
        if (!validaciones.isEmpty()) {
            validaciones.clear();
        }
    }

    public void onRowSelect(SelectEvent event) {
        informacionCuenta = (InformacionCuentasMonetariasOperacion) event.getObject();
    }

    public void onMonetariasRecepcionOtrosBancos_Terminar() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instFolioInst", folioInstruccion);
            FacesContext.getCurrentInstance().getExternalContext().redirect("instruccionesFideicomisos.sb?faces-redirect=true");
        } catch (IOException Err) {
            logger.error(Err.getMessage());
        }
    }

    public void guardaRecepcionMonetariaOtrosBancos() {
        reiniciaValidacion(validaciones);
        if (this.informacionCuenta == null) {
            validaciones.put("Fiduciario", "El campo Cuentas de Cheques no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (destinoRecepcionSelected == null) {
            validaciones.put("Fiduciario", "El campo Destino Recepción no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (getImporteTxt().isEmpty()) {
            setImporteTxt(null);
            setImporte(null);
            validaciones.put("Fiduciario", "El campo Importe no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        } else {
//            setImporteTxt(getImporteTxt().replaceAll(",", "")); 
//            setImporteTxt(getImporteTxt().replace("$", "").replace(contrato, importeTxt));
            try {
                setImporte(Double.parseDouble(getImporteTxt().replace("$", "").replaceAll(",", "")));

                if (getImporte() <= 0.0) {
                    setImporteTxt(null);
                    setImporte(null);
                    validaciones.put("Fiduciario", "El campo Importe debe ser mayor a Cero");
                    CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                } else {
                    if (getImporte() > VALOR_MAX) {
                        setImporteTxt(null);
                        setImporte(null);
                        validaciones.put("Fiduciario", "El campo Importe  no puede ser mayor a 999,999,999,999.99...");
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

        if (formaRecepcionSelect == null) {
            validaciones.put("Fiduciario", "El campo Forma de Recepción no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (tipoPersonaSelected == null) {
            validaciones.put("Fiduciario", "El campo Tipo de Persona no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (nombrePersonaSelected == null) {
            validaciones.put("Fiduciario", "El campo Nombre no puede estar vacío...");
            CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }

        if (validaciones.isEmpty()) {
            MonetariasOperacionOtrosBancosBean monetariaOperacionBean = new MonetariasOperacionOtrosBancosBean();
            monetariaOperacionBean.setLNUMCONTRATO(Integer.parseInt(fideicomiso));
            monetariaOperacionBean.setISUBCONTRATO(Integer.parseInt(subFideicomiso));
            monetariaOperacionBean.setVDIMPORTEMOV(importe);
            monetariaOperacionBean.setINUMMONEDA(informacionCuenta.getAdcNumMoneda());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, fechaRecepcionSistema.getFechaActAño());
            cal.set(Calendar.MONTH, fechaRecepcionSistema.getFechaActMes() - 1);
            cal.set(Calendar.DAY_OF_MONTH, fechaRecepcionSistema.getFechaActDia());
            Date fechaOnFuncion = new Date(cal.getTimeInMillis());
            monetariaOperacionBean.setDTFECHAMOV(fechaOnFuncion);
            monetariaOperacionBean.setSCTAORIGEN(informacionCuenta.getCuenta());
            DestinoRecepcionBean destinoRecepcionSeleccionado = listDestinoRecepcion.stream()
                    .filter(destinoRecepcion -> destinoRecepcion.getClaveNumero().equals(Integer.parseInt(destinoRecepcionSelected)))
                    .findAny().orElse(null);
            monetariaOperacionBean.setVICPTODEPO(destinoRecepcionSeleccionado.getClaveNumero());
            DestinoRecepcionBean formaRecepcionSeleccionado = listFormaRecepcion.stream()
                    .filter(formaRecepcion -> formaRecepcion.getClaveNumero().equals(Integer.parseInt(formaRecepcionSelect)))
                    .findAny().orElse(null);
            monetariaOperacionBean.setVIFORMADEP(formaRecepcionSeleccionado.getClaveNumero());
            monetariaOperacionBean.setSCVE_PERS_FID(tipoPersonaSelected);
            monetariaOperacionBean.setINUM_PERS_FID(Integer.parseInt(nombrePersonaSelected));
            monetariaOperacionBean.setINUMUSUARIO((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero"));
            monetariaOperacionBean.setSTERMINAL("SCOTIABANK-PC");
            Map<String, String> responseSPProcesa = new HashMap<>();
            responseSPProcesa = ccMonetariasOperacion.onGuardaSPDeposOtbancos(monetariaOperacionBean);
            if (!responseSPProcesa.isEmpty()) {
                for (Map.Entry<String, String> sp : responseSPProcesa.entrySet()) {
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("Error")) {
                        reiniciaValidacion(validaciones);

                        String[] arrErr = responseSPProcesa.get("Error").split("PS_MSGERR_OUT=");
                        if (arrErr.length > 1) {
                            validaciones.put("Fiduciario", arrErr[1]);
                        } else {
                            validaciones.put("Fiduciario", responseSPProcesa.get("Error"));
                        }
                        CValidacionesUtil.addMessage(FacesMessage.SEVERITY_INFO, validaciones);
                    }
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("folioContable")) {
                        fechaAplicacion = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        folioContable = sp.getValue();
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("frmMonetariasLiquidacionOtrosBancos:dlgSPExito");
                        context.execute("dlgSPExito.show();");
                        //oGeneraLog.onGeneraLog(MBMonetariasRecepcionOtrosBancos.class, "0D", "ERROR", "20", "40", "Folio contable: " + folioContable, "10", "20", "30");
                    }
                }
            } else {
                reiniciaValidacion(validaciones);
                validaciones.put("Fiduciario", "Ocurrio un error en Base de Datos intente más tarde");
                CValidacionesUtil.addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
            }
        }

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

    public List<DestinoRecepcionBean> getListDestinoRecepcion() {
        return listDestinoRecepcion;
    }

    public void setListDestinoRecepcion(List<DestinoRecepcionBean> listDestinoRecepcion) {
        this.listDestinoRecepcion = listDestinoRecepcion;
    }

    public List<DestinoRecepcionBean> getListFormaRecepcion() {
        return listFormaRecepcion;
    }

    public void setListFormaRecepcion(List<DestinoRecepcionBean> listFormaRecepcion) {
        this.listFormaRecepcion = listFormaRecepcion;
    }

    public String getDestinoRecepcionSelected() {
        return destinoRecepcionSelected;
    }

    public void setDestinoRecepcionSelected(String destinoRecepcionSelected) {
        this.destinoRecepcionSelected = destinoRecepcionSelected;
    }

    public String getFormaRecepcionSelect() {
        return formaRecepcionSelect;
    }

    public void setFormaRecepcionSelect(String formaRecepcionSelect) {
        this.formaRecepcionSelect = formaRecepcionSelect;
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

    public FechaBean getFechaRecepcionSistema() {
        return fechaRecepcionSistema;
    }

    public void setFechaRecepcionSistema(FechaBean fechaRecepcionSistema) {
        this.fechaRecepcionSistema = fechaRecepcionSistema;
    }

    public String getTipoPersonaSelected() {
        return tipoPersonaSelected;
    }

    public void setTipoPersonaSelected(String tipoPersonaSelected) {
        this.tipoPersonaSelected = tipoPersonaSelected;
    }

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

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getFolioContable() {
        return folioContable;
    }

    public void setFolioContable(String folioContable) {
        this.folioContable = folioContable;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getImporteTxt() {
        return importeTxt;
    }

    public void setImporteTxt(String importeTxt) {
        this.importeTxt = importeTxt;
    }

    public List<DestinoRecepcionBean> getListTipoPersona() {
        return listTipoPersona;
    }

    public void setListTipoPersona(List<DestinoRecepcionBean> listTipoPersona) {
        this.listTipoPersona = listTipoPersona;
    }

    public String getFolioInstruccion() {
        return folioInstruccion;
    }

    public void setFolioInstruccion(String folioInstruccion) {
        this.folioInstruccion = folioInstruccion;
    }

}
