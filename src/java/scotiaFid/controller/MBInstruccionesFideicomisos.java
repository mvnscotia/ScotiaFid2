/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : MBInstruccionesFideicomisos.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controler
 * CREADO      : 20210312
 * MODIFICADO  : 20211013
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20211013.- Se implementa funcionalidad de saldosPromedio
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import scotiaFid.bean.CriterioBusquedaInstruccionesFideicomisoBean;
import scotiaFid.bean.DatosFisoBean;
import scotiaFid.bean.GenericoDesHabilitadoBean;
import scotiaFid.bean.GenericoFechaBean;
import scotiaFid.bean.GenericoTituloBean;
import scotiaFid.bean.GenericoVisibleBean;
import scotiaFid.bean.InformacionCuentasMonetariasOperacion;
import scotiaFid.bean.InstruccionFideicomisoBean;
import scotiaFid.bean.MensajeConfirmaBean;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CContrato;
import scotiaFid.dao.CInstruccionesFideicomiso;
import scotiaFid.util.LogsContext;

@ManagedBean(name = "mbInstruccionesFideicomisos")
@ViewScoped
public class MBInstruccionesFideicomisos implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBInstruccionesFideicomisos.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean validacion;
    private Boolean valorRetorno;
    private Integer usuarioNumero;
    private String usuarioNombre;
    private String usuarioTerminal;

    private List<String> usuarioFiltro;
    private Map<Integer, String> listaContratoSub;
    private List<InformacionCuentasMonetariasOperacion> listInformacionCuentas;
    private String mensajeError;
    private String nombreObjeto;

    private Map<String, String> parametrosFC;

    private FacesMessage mensaje;

    private HttpServletRequest peticionURL;

    private static SimpleDateFormat formatoFecha;

    private DatosFisoBean oFiso;
    private instruccionesFideicomisos oInstruccionesFideicomisos;
    private CInstruccionesFideicomiso oInstruccionesFideicomiso;
    private CContrato oContrato;
    private String mensajeLiq;
    //end - efp

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin - efp
    @ManagedProperty(value = "#{beanCriterioBusquedaInstruccionesFideicomiso}")
    private CriterioBusquedaInstruccionesFideicomisoBean cbif;
    @ManagedProperty(value = "#{beanInstruccionFideicomiso}")
    private InstruccionFideicomisoBean instrucFide;
    //end - efp
    @ManagedProperty(value = "#{beanMensajeConfirmacion}")
    private MensajeConfirmaBean mensajeConfirma;

    @ManagedProperty(value = "#{beanGenericoDesHabilitado}")
    private GenericoDesHabilitadoBean geneDes;
    @ManagedProperty(value = "#{beanGenericoFecha}")
    private GenericoFechaBean geneFecha;
    @ManagedProperty(value = "#{beanGenericoTitulo}")
    private GenericoTituloBean geneTitulo;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean geneVisible;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin - efp
    //private List<InstruccionFideicomisoBean>               consultaInstruccionesFideicomisos;
    private LazyDataModel<InstruccionFideicomisoBean> instructions;
    //end - efp

    //begin - efp
    private List<String> listaInstruccFideAltaAutoriza;
    //begin - end

    //begin - efp
    private InstruccionFideicomisoBean seleccionaInstrucFideicomiso;
    //end - efp

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin - efp
    public void setCbif(CriterioBusquedaInstruccionesFideicomisoBean cbif) {
        this.cbif = cbif;
    }

    public void setInstrucFide(InstruccionFideicomisoBean instrucFide) {
        this.instrucFide = instrucFide;
    }
    //end -efp

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

    //begin - efp
//    public List<InstruccionFideicomisoBean> getConsultaInstruccionesFideicomisos() {
//        return consultaInstruccionesFideicomisos;
//    }
    public LazyDataModel<InstruccionFideicomisoBean> getInstructions() {
        return instructions;
    }

    public List<String> getListaInstruccFideAltaAutoriza() {
        return listaInstruccFideAltaAutoriza;
    }

    public InstruccionFideicomisoBean getSeleccionaInstrucFideicomiso() {
        return seleccionaInstrucFideicomiso;
    }

    public void setSeleccionaInstrucFideicomiso(InstruccionFideicomisoBean seleccionaInstrucFideicomiso) {
        this.seleccionaInstrucFideicomiso = seleccionaInstrucFideicomiso;
    }

    public Map<Integer, String> getListaContratoSub() {
        return listaContratoSub;
    }

    public void setListaContratoSub(Map<Integer, String> listaContratoSub) {
        this.listaContratoSub = listaContratoSub;
    }

    //efp End
    public void setMensajeConfirma(MensajeConfirmaBean mensajeConfirma) {
        this.mensajeConfirma = mensajeConfirma;
    }

    public String getMensajeLiq() {
        return mensajeLiq;
    }

    public void setMensajeLiq(String mensajeLiq) {
        this.mensajeLiq = mensajeLiq;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin -efp
    class instruccionesFideicomisos {

        public instruccionesFideicomisos() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbInstruccionesFideicomisos.instruccionesFideicomisos.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        //begin -efp
        public void onInstruccionesFideicomisos_ConsultaEjecuta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {

                if ((!cbif.getTxtCriterioBusqInstrucFideicomiso().isEmpty())) {
                    try {
                        cbif.setCriterioBusqInstrucFideicomiso(Long.parseLong(cbif.getTxtCriterioBusqInstrucFideicomiso()));
//                        if (cbif.getCriterioBusqInstrucFideicomiso() != null && !cbif.getCriterioBusqInstrucFideicomiso().equals(new String())) {
//                            if (!onVerificaAtencionContrato(cbif.getCriterioBusqInstrucFideicomiso())) {
//                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece"));
//                            }
//                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbif.setTxtCriterioBusqInstrucFideicomiso(null);
                        cbif.setCriterioBusqInstrucFideicomiso(null);
                    }
                } else {
                    cbif.setTxtCriterioBusqInstrucFideicomiso(null);
                    cbif.setCriterioBusqInstrucFideicomiso(null);
                }
                if ((!cbif.getTxtCriterioBusqInstrucFideSubCto().isEmpty())) {
                    try {
                        cbif.setCriterioBusqInstrucFideSubCto(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFideSubCto()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                        cbif.setCriterioBusqInstrucFideSubCto(null);
                        cbif.setTxtCriterioBusqInstrucFideSubCto(null);
                    }
                } else {
                    cbif.setCriterioBusqInstrucFideSubCto(null);
                    cbif.setTxtCriterioBusqInstrucFideSubCto(null); 
                }
                if ((!cbif.getTxtCriterioBusqInstrucFideFolioInst().isEmpty())) {
                    try {
                        cbif.setCriterioBusqInstrucFideFolioInst(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFideFolioInst()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio Inst. debe ser un campo numérico..."));
                        cbif.setCriterioBusqInstrucFideFolioInst(null);
                        cbif.setTxtCriterioBusqInstrucFideFolioInst(null);
                    }
                } else {
                    cbif.setCriterioBusqInstrucFideFolioInst(null);
                    cbif.setTxtCriterioBusqInstrucFideFolioInst(null);
                }
                if ((!cbif.getTxtCriterioBusqInstrucFidePlaza().isEmpty())) {
                    try {
                        cbif.setCriterioBusqInstrucFidePlaza(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFidePlaza()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico..."));
                        cbif.setCriterioBusqInstrucFidePlaza(null);
                        cbif.setTxtCriterioBusqInstrucFidePlaza(null);
                    }
                } else {
                    cbif.setCriterioBusqInstrucFidePlaza(null);
                    cbif.setTxtCriterioBusqInstrucFidePlaza(null);
                }
                if (mensajesDeError.isEmpty()) {
                    seleccionaInstrucFideicomiso = null;
                    oInstruccionesFideicomiso = new CInstruccionesFideicomiso();

                    DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmInstrucFide:dtInstrucFide");
                    dataTable.setFirst(0);

                    int totalRows = oInstruccionesFideicomiso.getInstructionsTotalRows(cbif);
                    InstruccionFideicomisoBean bean = new InstruccionFideicomisoBean();
                    MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getinstructions");
                    instructions = lazyModel.getLazyDataModel(cbif);
                    instructions.setRowCount(totalRows);

                    if (instructions.getRowCount() == 0) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados");
                    }

                    oInstruccionesFideicomiso = null;
                }
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //end -efp

        //begin -efp
        public void onInstruccionesFideicomisos_RedirectMonetarias() {
            try {
                String instrucFideInsNumContrato = "";
                String instrucFideInsSubContrato = "";
                String instrucFideInsNumFolioInst = "";
                String instrucFideCtoNomContrato = "";
                String instrucFideInsCveTipoInstr = "";

                parametrosFC = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

                if (parametrosFC.get("instrucFideInsNumContrato") != null) {
                    instrucFideInsNumContrato = parametrosFC.get("instrucFideInsNumContrato");
                }
                if (parametrosFC.get("instrucFideInsSubContrato") != null) {
                    instrucFideInsSubContrato = parametrosFC.get("instrucFideInsSubContrato");
                }
                if (parametrosFC.get("instrucFideInsNumFolioInst") != null) {
                    instrucFideInsNumFolioInst = parametrosFC.get("instrucFideInsNumFolioInst");
                }
                if (parametrosFC.get("instrucFideCtoNomContrato") != null) {
                    instrucFideCtoNomContrato = parametrosFC.get("instrucFideCtoNomContrato");
                }
                if (parametrosFC.get("instrucFideInsCveTipoInstr") != null) {
                    instrucFideInsCveTipoInstr = parametrosFC.get("instrucFideInsCveTipoInstr");
                }

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instrucFideInsNumContrato", instrucFideInsNumContrato);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instrucFideInsSubContrato", instrucFideInsSubContrato);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instrucFideInsNumFolioInst", instrucFideInsNumFolioInst);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instrucFideCtoNomContrato", instrucFideCtoNomContrato);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instrucFideInsCveTipoInstr", instrucFideInsCveTipoInstr);
                CComunes ccomunes = new CComunes();

                listInformacionCuentas = ccomunes.getInformacionCuentas(instrucFideInsNumContrato, instrucFideInsSubContrato);
                switch (instrucFideInsCveTipoInstr) {
                    //case "ADMINISTRATIVAS": FacesContext.getCurrentInstance().getExternalContext().redirect("administrativas.sb?faces-redirect=true"); break;
                    case "MONETARIAS LIQUIDACION":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("monetariasLiquidacion.sb?faces-redirect=true");
                        Thread.sleep(5000);
                        break;
                    case "MONETARIAS RECEPCION":
                        FacesContext.getCurrentInstance().getExternalContext().redirect("recepcionCuentasBanco.sb?faces-redirect=true");
                        break;
                    case "MONETARIAS LIQ. OTRAS INST. BANCARIAS":
                        if (!listInformacionCuentas.isEmpty()) {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("monetariasLiquidacionOtrosBancos.sb?faces-redirect=true");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Este contrato no tiene cuentas de cheques de otros intermediarios...");
                        }
                        break;
                    case "MONETARIAS REC. OTRAS INST. BANCARIAS":
                        if (!listInformacionCuentas.isEmpty()) {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("monetariasRecepcionOtrosBancos.sb?faces-redirect=true");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Este contrato no tiene cuentas de cheques de otros intermediarios...");
                        }
                        break;
                    default:
                }

            } catch (InterruptedException |  IOException Err) {
                logger.error(Err.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //end -efp

        public void onInstruccionesFideicomisos_ConsultaLimpia() {
            onInicializaCriterios();
        }

        //begin -efp
        public void onInstruccionesFideicomisos_RegistroValidaContrato() {
            try {
                listaContratoSub = new  LinkedHashMap<>();
                oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                oFiso = new DatosFisoBean();
                if (!instrucFide.getTxtInstrucFideInsNumContrato().equals("")) {
                    try{
                    instrucFide.setInstrucFideInsNumContrato(Long.parseLong(instrucFide.getTxtInstrucFideInsNumContrato()));
                    }catch(NumberFormatException Err){
                        instrucFide.setInstrucFideInsNumContrato(null);
                        instrucFide.setTxtInstrucFideInsNumContrato(null);
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico...");   
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");   
                    instrucFide.setInstrucFideInsNumContrato(null);
                    instrucFide.setTxtInstrucFideInsNumContrato(null);
                }

                if (instrucFide.getInstrucFideInsNumContrato() != null) {

                    if (validacion.equals(Boolean.TRUE)) {
                        validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_VerificaContratoSrv(instrucFide.getInstrucFideInsNumContrato(), oFiso);
                        if (validacion.equals(Boolean.TRUE)) {
//                            if (oFiso.getbSubContrato()) {
                                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                                oContrato = new CContrato();
                                listaContratoSub = oContrato.onContrato_ObtenMapContratoSub(instrucFide.getInstrucFideInsNumContrato(), "ACTIVO");
                                oContrato = null;
//                            } else {
//                                listaContratoSub = new LinkedHashMap<>();
//                                listaContratoSub.put(0, "");
                                instrucFide.setInstrucFideInsSubContrato(0);
//                                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
//                            }

                            Integer cto_cve_comite_tect = oInstruccionesFideicomiso.onInstruccionesFideicomiso_LeeContrato(instrucFide.getInstrucFideInsNumContrato());
                            if (cto_cve_comite_tect == 1) {
                                listaInstruccFideAltaAutoriza = oInstruccionesFideicomiso.onInstruccionesFideicomiso_CargaComite(instrucFide.getInstrucFideInsNumContrato());
                                if (listaInstruccFideAltaAutoriza == null) {
                                    validacion = Boolean.FALSE;
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso está calificado con Comité y no se han dado de alta miembros del comité, No se puede aceptar la Instrucción...");
                                }
                            } else {
                                listaInstruccFideAltaAutoriza = oInstruccionesFideicomiso.onInstruccionesFideicomiso_CargaFideicomitente(instrucFide.getInstrucFideInsNumContrato());
                                if (listaInstruccFideAltaAutoriza == null) {
                                    validacion = Boolean.FALSE;
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen fideicomitentes, No se acepta la Instrucción...");
                                }
                            }

                            if (validacion.equals(Boolean.TRUE)) {
                                Integer instruccFolio = oInstruccionesFideicomiso.onInstruccionesFideicomiso_AsignaFolio(4);
                                instrucFide.setInstrucFideInsNumFolioInst(instruccFolio);
                            } else {
                                instrucFide.setInstrucFideInsNumContrato(null);
                                instrucFide.setTxtInstrucFideInsNumContrato(null);
                            }
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                            instrucFide.setInstrucFideInsNumContrato(null);
                            instrucFide.setTxtInstrucFideInsNumContrato(null);
                        }
                    }
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onInstruccionesFideicomisos_RegistroValidaFechaCorrecta() {
            try {
                if ((geneFecha.getGenericoFecha00() == null) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha de Instrucción no puede estar vacío...");
                }

                instrucFide.setInstrucFideFechaDeInstruccion(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                valorRetorno = oInstruccionesFideicomiso.onInstruccionesFideicomiso_BuscaDiasFeriados(instrucFide);
                if (valorRetorno.equals(Boolean.TRUE)) {
                    validacion = Boolean.FALSE;
                    geneFecha.setGenericoFecha00(null);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha de Instrucción no debe ser un día feriado...");
                }

                Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
                int fechaValida = instrucFide.getInstrucFideFechaDeInstruccion().compareTo(fechSistema);

                if (fechaValida > 0) {
                    validacion = Boolean.FALSE;
                    geneFecha.setGenericoFecha00(null);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha de Instrucción no puede ser mayor a la fecha contable...");
                }

                oInstruccionesFideicomiso = null;

            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //end -efp       

        public void onInstruccionesFideicomisos_RegistroGuardar() {
            try {

                if ((instrucFide.getInstrucFideInsNumContrato() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsSubContrato() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsCveTipoInstr() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Instrucción no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((geneFecha.getGenericoFecha00() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha de Instrucción no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsTxtComentario() == null || instrucFide.getInstrucFideInsTxtComentario().equalsIgnoreCase(new String()))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Texto no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsNomMiembro() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Autoriza no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideExdIdDocumento() == null || instrucFide.getInstrucFideExdIdDocumento().equalsIgnoreCase(new String()))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Folio del Documento no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                } else {
                    if (!onValidaInformacionCampo(instrucFide.getInstrucFideExdIdDocumento(), "^[a-zA-Z0-9-_Ññ\\/]+$")) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Folio del Documento no debe contener caracteres especiales...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }

                if (validacion.equals(Boolean.TRUE)) {

                    oInstruccionesFideicomiso = new CInstruccionesFideicomiso();

                    instrucFide.setInstrucFideFechaDeInstruccion(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                    String expNumExpediente = oInstruccionesFideicomiso.onInstruccionesFideicomiso_LeeExpedient(instrucFide.getInstrucFideInsNumContrato());
                    if (null == expNumExpediente || expNumExpediente.equalsIgnoreCase(new String())) {
                        instrucFide.setInstrucFideExpNumExpediente("Fid" + instrucFide.getInstrucFideInsNumContrato());
                        RequestContext.getCurrentInstance().execute("dlgInstrucFideAltaExp.show();");

                    } else {
                        if (oInstruccionesFideicomiso.onInstruccionesFideicomiso_ValidaExpedocs(expNumExpediente, instrucFide)) {
                            validacion = false;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Folio del Documento ".concat(instrucFide.getInstrucFideExdIdDocumento()).concat(" ya existe en el expediente..."));
                        } else {
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaExpedocs(expNumExpediente, instrucFide);

                        }
                        if (validacion.equals(Boolean.TRUE)) {
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_AltaRegistro(instrucFide);
                        }

                        if (validacion.equals(Boolean.TRUE)) {
                            instrucFide.setInstrucFideBitTerminal(usuarioTerminal);
                            instrucFide.setInstrucFideBitUsuario(usuarioNumero);
                            instrucFide.setInstrucFideBitPantalla("INSTRUCCIONES_FIDEICOMISOS");
                            instrucFide.setInstrucFideBitTipoOperacion("ALTA");
                            instrucFide.setInstrucFideBitDetalleBitacora("ALTA DE INSTRUCCION (" + instrucFide.getInstrucFideInsNumFolioInst() + ") DE CONTRATO " + instrucFide.getInstrucFideInsNumContrato());
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaBitacoraUsuario(instrucFide);
                        }

                        if (validacion.equals(Boolean.TRUE)) {
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaAgenda(expNumExpediente, instrucFide);
                        }

                        if (validacion.equals(Boolean.TRUE)) {

                            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmInstrucFide:dtInstrucFide");
                            dataTable.setFirst(0);
                            onInicializaCriterios();
                            cbif.setCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst());
                            cbif.setTxtCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst().toString());
                            int totalRows = oInstruccionesFideicomiso.getInstructionsTotalRows(cbif);
                            InstruccionFideicomisoBean bean = new InstruccionFideicomisoBean();
                            MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getinstructions");
                            instructions = lazyModel.getLazyDataModel(cbif);
                            instructions.setRowCount(totalRows);
//                            consultaInstruccionesFideicomisos = oInstruccionesFideicomiso.onInstruccionesFideicomiso_Consulta(cbif);
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            RequestContext.getCurrentInstance().execute("dlgInstrucFide.hide();");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    }
                    oInstruccionesFideicomiso = null;
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
            }
        }
        private Boolean onValidaInformacionCampo(String campoValor, String campoPatron) {
            Pattern patron = Pattern.compile(campoPatron);
            Matcher matcher = patron.matcher(campoValor);
            return matcher.matches();
        }

        public void onInstruccionesFideicomisos_RegistroModificar() {
            try {

                if ((instrucFide.getInstrucFideInsNumContrato() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Contrato no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsSubContrato() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Sub Cto. no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsCveTipoInstr() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Instrucción no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((geneFecha.getGenericoFecha00() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha de Instrucción no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsTxtComentario() == null || instrucFide.getInstrucFideInsTxtComentario().equalsIgnoreCase(new String()))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Texto no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideInsNomMiembro() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Autoriza no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if ((instrucFide.getInstrucFideExdIdDocumento() == null || instrucFide.getInstrucFideExdIdDocumento().equalsIgnoreCase(new String()))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Folio del Documento no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }

                if (validacion.equals(Boolean.TRUE)) {

                    oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                    instrucFide.setInstrucFideFechaDeInstruccion(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                    String expNumExpediente = oInstruccionesFideicomiso.onInstruccionesFideicomiso_LeeExpedient(instrucFide.getInstrucFideInsNumContrato());
                    if (expNumExpediente != null) {
                        validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaExpedocs(expNumExpediente, instrucFide);
                        if (validacion.equals(Boolean.TRUE)) {
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_ActualizaRegistro(instrucFide);
                        }

                        if (validacion.equals(Boolean.TRUE)) {
                            instrucFide.setInstrucFideBitTerminal(usuarioTerminal);
                            instrucFide.setInstrucFideBitUsuario(usuarioNumero);
                            instrucFide.setInstrucFideBitPantalla("INSTRUCCIONES_FIDEICOMISOS");
                            instrucFide.setInstrucFideBitTipoOperacion("MODIFICACIÓN");
                            instrucFide.setInstrucFideBitDetalleBitacora("MODIFICACIÓN DE INSTRUCCION (" + instrucFide.getInstrucFideInsNumFolioInst() + ") DE CONTRATO " + instrucFide.getInstrucFideInsNumContrato());
                            validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaBitacoraUsuario(instrucFide);
                        }

                        if (validacion.equals(Boolean.TRUE)) {
                            onInicializaCriterios();
                            cbif.setCriterioBusqInstrucFideicomiso(instrucFide.getInstrucFideInsNumContrato());
                            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmInstrucFide:dtInstrucFide");
                            dataTable.setFirst(0);
                            cbif.setCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst());
                            cbif.setTxtCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst().toString());
                            int totalRows = oInstruccionesFideicomiso.getInstructionsTotalRows(cbif);
                            InstruccionFideicomisoBean bean = new InstruccionFideicomisoBean();
                            MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getinstructions");
                            instructions = lazyModel.getLazyDataModel(cbif);
                            instructions.setRowCount(totalRows);

//                            consultaInstruccionesFideicomisos = oInstruccionesFideicomiso.onInstruccionesFideicomiso_Consulta(cbif);
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                            RequestContext.getCurrentInstance().execute("dlgInstrucFide.hide();");
                        }
                    }
                    oInstruccionesFideicomiso = null;
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
            }
        }

        public void onInstruccionesFideicomisos_RegistroEliminar() {
            parametrosFC = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            mensajeConfirma.setMensajeConfirmaUsuario(usuarioNombre);
            mensajeConfirma.setMensajeConfirmaMensaje1("¿Confirma la eliminación del registro actual?");
            mensajeConfirma.setMensajeConfirmaOrigen("INSTRUCCIONES_FIDEICOMISOS");
            mensajeConfirma.setMensajeConfirmacionAccion("Eliminar");
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
        }

        public void onInstruccionesFideicomisos_AplicarRegistroEliminar() {
            try {

                String instrucFideInsNumContrato = "";
                String instrucFideInsSubContrato = "";
                String instrucFideInsNumFolioInst = "";
                String instrucFideExdIdDocumento = "";
                String instrucFideInsCveTipoInstr = "";
                String instrucFideInsDiaAltaReg = "";
                String instrucFideInsMesAltaReg = "";
                String instrucFideInsAnoAltaReg = "";
                setMensajeLiq(new String());
                if (parametrosFC.get("instrucFideInsNumContrato") != null) {
                    instrucFideInsNumContrato = parametrosFC.get("instrucFideInsNumContrato");
                }
                if (parametrosFC.get("instrucFideInsSubContrato") != null) {
                    instrucFideInsSubContrato = parametrosFC.get("instrucFideInsSubContrato");
                }
                if (parametrosFC.get("instrucFideInsNumFolioInst") != null) {
                    instrucFideInsNumFolioInst = parametrosFC.get("instrucFideInsNumFolioInst");
                }
                if (parametrosFC.get("instrucFideExdIdDocumento") != null) {
                    instrucFideExdIdDocumento = parametrosFC.get("instrucFideExdIdDocumento");
                }
                if (parametrosFC.get("instrucFideExdIdDocumento") != null) {
                    instrucFideExdIdDocumento = parametrosFC.get("instrucFideExdIdDocumento");
                }
                if (parametrosFC.get("instrucFideInsCveTipoInstr") != null) {
                    instrucFideInsCveTipoInstr = parametrosFC.get("instrucFideInsCveTipoInstr");
                }
                if (parametrosFC.get("instrucFideInsDiaAltaReg") != null) {
                    instrucFideInsDiaAltaReg = parametrosFC.get("instrucFideInsDiaAltaReg");
                }
                if (parametrosFC.get("instrucFideInsMesAltaReg") != null) {
                    instrucFideInsMesAltaReg = parametrosFC.get("instrucFideInsMesAltaReg");
                }
                if (parametrosFC.get("instrucFideInsAnoAltaReg") != null) {
                    instrucFideInsAnoAltaReg = parametrosFC.get("instrucFideInsAnoAltaReg");
                }
                oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                if (validacion.equals(Boolean.TRUE)) {
                    validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_BorrarInstruccionFideicomiso(new Long(instrucFideInsNumContrato),
                            new Integer(instrucFideInsSubContrato),
                            new Long(instrucFideInsNumFolioInst),
                            instrucFideExdIdDocumento);
                }

                if (("MONETARIAS LIQUIDACION".equals(instrucFideInsCveTipoInstr) || "MONETARIAS LIQ. OTRAS INST. BANCARIAS".equals(instrucFideInsCveTipoInstr)) && validacion) {

                    validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_BorrarInstruccionMonLiq(new Long(instrucFideInsNumContrato),
                            new Integer(instrucFideInsSubContrato),
                            new Long(instrucFideInsNumFolioInst),
                            instrucFideExdIdDocumento,
                            new Integer(instrucFideInsDiaAltaReg),
                            new Integer(instrucFideInsMesAltaReg),
                            new Integer(instrucFideInsAnoAltaReg));
                    setMensajeLiq(oInstruccionesFideicomiso.getMensaje());

                }

                if (validacion.equals(Boolean.TRUE)) {
                    instrucFide.setInstrucFideBitTerminal(usuarioTerminal);
                    instrucFide.setInstrucFideBitUsuario(usuarioNumero);
                    instrucFide.setInstrucFideBitPantalla("INSTRUCCIONES_FIDEICOMISOS");
                    instrucFide.setInstrucFideBitTipoOperacion("BAJA");
                    instrucFide.setInstrucFideBitDetalleBitacora("BAJA DE INSTRUCCION (" + instrucFideInsNumFolioInst + ") DE CONTRATO " + instrucFideInsNumFolioInst);
                    validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaBitacoraUsuario(instrucFide);
                    if (validacion.equals(Boolean.TRUE)) {
//                        onInicializaCriterios();
//   
//
//                        cbif.setCriterioBusqInstrucFideicomiso(instrucFide.getInstrucFideInsNumContrato());
//                        cbif.setCriterioBusqInstrucFideicomiso(instrucFide.getInstrucFideInsNumContrato());
                        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmInstrucFide:dtInstrucFide");
                        dataTable.setFirst(0);
//                        cbif.setCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst());
//                        cbif.setTxtCriterioBusqInstrucFideFolioInst(instrucFide.getInstrucFideInsNumFolioInst().toString());
                        int totalRows = oInstruccionesFideicomiso.getInstructionsTotalRows(cbif);
                        InstruccionFideicomisoBean bean = new InstruccionFideicomisoBean();
                        MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getinstructions");
                        instructions = lazyModel.getLazyDataModel(cbif);
                        instructions.setRowCount(totalRows);

//                        consultaInstruccionesFideicomisos = oInstruccionesFideicomiso.onInstruccionesFideicomiso_Consulta(cbif);
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
                        if (!getMensajeLiq().equals(new String())) {
                            RequestContext.getCurrentInstance().execute("dlgPopUpLiq.show();");

                        }
                    }
                }
                if (!validacion) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "Operación no pudo ser realizada");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
                }
                oInstruccionesFideicomiso = null;

            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
            }
        }

        public void onInstruccionesFideicomisos_RegistroSelecciona() {
            try {
                //onInicializaInstruccFide();                

                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado02(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado03(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado04(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado05(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado06(Boolean.TRUE);

                oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                instrucFide.setInstrucFideInsNumContrato(seleccionaInstrucFideicomiso.getInstrucFideInsNumContrato());
                instrucFide.setTxtInstrucFideInsNumContrato(seleccionaInstrucFideicomiso.getInstrucFideInsNumContrato().toString());
                oContrato = new CContrato();
                listaContratoSub = oContrato.onContrato_ObtenMapContratoSub(instrucFide.getInstrucFideInsNumContrato(), "ACTIVO");
                if (listaContratoSub.isEmpty()) {
                    listaContratoSub = new LinkedHashMap<>();
                    listaContratoSub.put(0, "");
                }
                oContrato = null;
                instrucFide.setInstrucFideInsSubContrato(seleccionaInstrucFideicomiso.getInstrucFideInsSubContrato());
                instrucFide.setInstrucFideInsNumFolioInst(seleccionaInstrucFideicomiso.getInstrucFideInsNumFolioInst());
                String fechaDeInstruccion = seleccionaInstrucFideicomiso.getInstrucFideInsDiaAltaReg() + "/" + seleccionaInstrucFideicomiso.getInstrucFideInsMesAltaReg() + "/" + seleccionaInstrucFideicomiso.getInstrucFideInsAnoAltaReg();
                geneFecha.setGenericoFecha00(new java.sql.Date(formatFechaParse(fechaDeInstruccion).getTime()));
                instrucFide.setInstrucFideInsCveTipoInstr(seleccionaInstrucFideicomiso.getInstrucFideInsCveTipoInstr());
                instrucFide.setInstrucFideInsTxtComentario(seleccionaInstrucFideicomiso.getInstrucFideInsTxtComentario());
                instrucFide.setInstrucFideInsNomMiembro(seleccionaInstrucFideicomiso.getInstrucFideInsNomMiembro());
                instrucFide.setInstrucFideExdIdDocumento(seleccionaInstrucFideicomiso.getInstrucFideExdIdDocumento());
                String[] autorizadorList = oInstruccionesFideicomiso.onInstruccionesFideicomiso_CargaAutorizador(instrucFide);
                instrucFide.setInstrucFideInsNomMiembro(autorizadorList[0] + "/" + autorizadorList[1]);

                Integer cto_cve_comite_tect = oInstruccionesFideicomiso.onInstruccionesFideicomiso_LeeContrato(instrucFide.getInstrucFideInsNumContrato());
                if (cto_cve_comite_tect == 1) {
                    listaInstruccFideAltaAutoriza = oInstruccionesFideicomiso.onInstruccionesFideicomiso_CargaComite(instrucFide.getInstrucFideInsNumContrato());
                    if (listaInstruccFideAltaAutoriza == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso está calificado con Comité y no se han dado de alta miembros del comité, No se puede aceptar la Instrucción...");
                    }
                } else {
                    listaInstruccFideAltaAutoriza = oInstruccionesFideicomiso.onInstruccionesFideicomiso_CargaFideicomitente(instrucFide.getInstrucFideInsNumContrato());
                    if (listaInstruccFideAltaAutoriza == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen fideicomitentes, No se acepta la Instrucción...");
                    }
                }

                RequestContext.getCurrentInstance().execute("dlgInstrucFide.show();");

            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onInstruccionesFideicomisos_RegistroEntrada() {
            try {
                onInicializaInstruccFide();
                listaContratoSub = new LinkedHashMap<>();
                listaInstruccFideAltaAutoriza = null;
                instrucFide.setInstrucFideInsSubContrato(null);
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado02(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado03(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado06(Boolean.FALSE);

                RequestContext.getCurrentInstance().execute("dlgInstrucFide.show();");
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onInstruccionesFideicomisos_RegistroParametrosDeLiquidacion() {
            try {
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                RequestContext.getCurrentInstance().execute("dlgMonetariasLiq.show();");
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage());
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onInstruccionesFideicomisos_ExpedienteGuardar() {
            try {

                if ((instrucFide.getInstrucFideExpNumExpediente() == null)) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No Expediente no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }

                if (!instrucFide.getTxtInstrucFideExpArchivo().isEmpty()) {
                    try {
                        instrucFide.setInstrucFideExpArchivo(Integer.parseInt(instrucFide.getTxtInstrucFideExpArchivo()));
                    } catch (NumberFormatException err) {
                        validacion = Boolean.FALSE;
                        instrucFide.setInstrucFideExpArchivo(null);
                        instrucFide.setTxtInstrucFideExpArchivo(null);
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Archivo debe ser un campo numérico...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                } else {
                    validacion = Boolean.FALSE;
                    instrucFide.setInstrucFideExpArchivo(null);
                    instrucFide.setTxtInstrucFideExpArchivo(null);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Archivo no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                if (!instrucFide.getTxtInstrucFideExpArchivero().isEmpty()) {
                    try {
                        instrucFide.setInstrucFideExpArchivero(Integer.parseInt(instrucFide.getTxtInstrucFideExpArchivero()));
                    } catch (NumberFormatException err) {
                        validacion = Boolean.FALSE;
                        instrucFide.setInstrucFideExpArchivero(null);
                        instrucFide.setTxtInstrucFideExpArchivero(null);
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Archivero debe ser un campo numérico...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }else{
                    validacion = Boolean.FALSE;
                    instrucFide.setInstrucFideExpArchivero(null);
                    instrucFide.setTxtInstrucFideExpArchivero(null);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Archivero no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }

                if (!instrucFide.getTxtInstrucFideExpNiivel().isEmpty()) {
                    try {
                        instrucFide.setInstrucFideExpNiivel(Integer.parseInt(instrucFide.getTxtInstrucFideExpNiivel()));
                    } catch (NumberFormatException err) {
                        validacion = Boolean.FALSE;
                        instrucFide.setInstrucFideExpNiivel(null);
                        instrucFide.setTxtInstrucFideExpNiivel(null);
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Nivel debe ser un campo numérico...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                
                }else{
                    validacion = Boolean.FALSE;
                    instrucFide.setInstrucFideExpNiivel(null);
                    instrucFide.setTxtInstrucFideExpNiivel(null);
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Nivel no puede estar vacío...");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }

                if (validacion.equals(Boolean.TRUE)) {

                    oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
                    instrucFide.setInstrucFideFechaDeInstruccion(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                    Boolean numExpExist = oInstruccionesFideicomiso.onInstruccionesFideicomiso_LeeExpedientExist(instrucFide.getInstrucFideExpNumExpediente());
                    if (!numExpExist) {
                        validacion = oInstruccionesFideicomiso.onInstruccionesFideicomiso_GrabaExpedien(instrucFide);
                        if (validacion.equals(Boolean.TRUE)) {
                            this.onInstruccionesFideicomisos_RegistroGuardar();
                            mensaje = null;
                            RequestContext.getCurrentInstance().execute("dlgInstrucFideAltaExp.hide();");
                        }
                    } else {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Ya existe el No de Expediente para otro Fideicomiso...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                    oInstruccionesFideicomiso = null;
                }
            } catch (SQLException Err) {
                cbif.setCriterioBusqInstrucFideicomiso(null);
                cbif.setCriterioBusqInstrucFideSubCto(null);
                cbif.setCriterioBusqInstrucFideFolioInst(null);
                cbif.setCriterioBusqInstrucFideFolioDocto(null);
                cbif.setCriterioBusqInstrucFideNomFideicomiso(null);
                cbif.setCriterioBusqInstrucFideTipo(null);
                cbif.setCriterioBusqInstrucFidePlaza(null);
                cbif.setTxtCriterioBusqInstrucFideicomiso(null);
                cbif.setTxtCriterioBusqInstrucFideSubCto(null);
                cbif.setTxtCriterioBusqInstrucFideFolioInst(null);
                cbif.setTxtCriterioBusqInstrucFidePlaza(null);
                logger.error("Al guardar Expediente");
            } finally {
                onFinalizaObjetos();
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
            }
        }

        private void onInicializaCriterios() {
            cbif.setCriterioBusqInstrucFideicomiso(null);
            cbif.setCriterioBusqInstrucFideSubCto(null);
            cbif.setCriterioBusqInstrucFideFolioInst(null);
            cbif.setCriterioBusqInstrucFideFolioDocto(null);
            cbif.setCriterioBusqInstrucFideNomFideicomiso(null);
            cbif.setCriterioBusqInstrucFideTipo(null);
            cbif.setCriterioBusqInstrucFidePlaza(null);

            cbif.setTxtCriterioBusqInstrucFideicomiso(null);
            cbif.setTxtCriterioBusqInstrucFideSubCto(null);
            cbif.setTxtCriterioBusqInstrucFideFolioInst(null);
            cbif.setTxtCriterioBusqInstrucFidePlaza(null);

            //consultaInstruccionesFideicomisos = null;
//            listaInstruccFideAltaAutoriza = null; 
            seleccionaInstrucFideicomiso = null;
            instructions = null;

        }

        private void onInicializaInstruccFide() {
            instrucFide.setInstrucFideInsNumContrato(null);
            instrucFide.setInstrucFideInsSubContrato(null);

            instrucFide.setInstrucFideInsCboSubContrato(null);
            instrucFide.setInstrucFideInsNumFolioInst(null);
            geneFecha.setGenericoFecha00(null);
            instrucFide.setInstrucFideInsCveTipoInstr(null);
            instrucFide.setInstrucFideInsTxtComentario(null);
            instrucFide.setInstrucFideInsNomMiembro(null);
            //listaInstruccFideAltaAutoriza = null;
            instrucFide.setInstrucFideExdIdDocumento(null);

            instrucFide.setInstrucFideBitPantalla("INSTRUCCIONES_FIDEICOMISOS");
            instrucFide.setInstrucFideBitTerminal(usuarioTerminal);
            instrucFide.setInstrucFideBitUsuario(usuarioNumero);

            instrucFide.setTxtInstrucFideInsNumContrato(null);

            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);

            instrucFide.setInstrucFideExpNumExpediente(null);
            instrucFide.setInstrucFideExpArchivo(null);
            instrucFide.setInstrucFideExpArchivero(null);
            instrucFide.setInstrucFideExpNiivel(null);

            instrucFide.setTxtInstrucFideExpArchivo(null);
            instrucFide.setTxtInstrucFideExpArchivero(null);
            instrucFide.setTxtInstrucFideExpNiivel(null);

        }

//        public void onInstrucciones_VerificaNumerico(String Campo) {
//            try {
//                switch (Campo) {
//                    case "txtCriterioBusqInstrucFideicomiso":
//                        if (!"".equals(cbif.getTxtCriterioBusqInstrucFideicomiso()) && onValidaNumerico(cbif.getTxtCriterioBusqInstrucFideicomiso(), "Fideicomiso", "L")) {
//                            cbif.setCriterioBusqInstrucFideicomiso(Long.parseLong(cbif.getTxtCriterioBusqInstrucFideicomiso()));
//                        } else {
//                            cbif.setCriterioBusqInstrucFideicomiso(null);
//                            cbif.setTxtCriterioBusqInstrucFideicomiso(null);
//                        }
//                        break;
//                    case "txtCriterioBusqInstrucFideSubCto":
//                        if (!"".equals(cbif.getTxtCriterioBusqInstrucFideSubCto()) && onValidaNumerico(cbif.getTxtCriterioBusqInstrucFideSubCto(), "SubFiso", "I")) {
//                            cbif.setCriterioBusqInstrucFideSubCto(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFideSubCto()));
//                        } else {
//                            cbif.setCriterioBusqInstrucFideSubCto(null);
//                            cbif.setTxtCriterioBusqInstrucFideSubCto(null);
//                        }
//                        break;
//                    case "txtCriterioBusqInstrucFideFolioInst":
//                        if (!"".equals(cbif.getTxtCriterioBusqInstrucFideFolioInst()) && onValidaNumerico(cbif.getTxtCriterioBusqInstrucFideFolioInst(), "Folio Inst", "I")) {
//                            cbif.setCriterioBusqInstrucFideFolioInst(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFideFolioInst()));
//                        } else {
//                            cbif.setCriterioBusqInstrucFideFolioInst(null);
//                            cbif.setTxtCriterioBusqInstrucFideFolioInst(null);
//                        }
//                        break;
//                    case "txtCriterioBusqInstrucFidePlaza":
//                        if (!"".equals(cbif.getTxtCriterioBusqInstrucFidePlaza()) && onValidaNumerico(cbif.getTxtCriterioBusqInstrucFidePlaza(), "Plaza", "I")) {
//                            cbif.setCriterioBusqInstrucFidePlaza(Integer.parseInt(cbif.getTxtCriterioBusqInstrucFidePlaza()));
//                        } else {
//                            cbif.setCriterioBusqInstrucFidePlaza(null);
//                            cbif.setTxtCriterioBusqInstrucFidePlaza(null);
//                        }
//                        break;
//                    case "txtInstrucFideExpArchivo":
//                        if (!"".equals(instrucFide.getTxtInstrucFideExpArchivo()) && onValidaNumerico(instrucFide.getTxtInstrucFideExpArchivo(), "Archivo", "S")) {
//                            instrucFide.setInstrucFideExpArchivo(Integer.parseInt(instrucFide.getTxtInstrucFideExpArchivo()));
//                        } else {
//                            instrucFide.setInstrucFideExpArchivo(null);
//                            instrucFide.setTxtInstrucFideExpArchivo(null);
//                        }
//                        break;
//                    case "txtInstrucFideExpArchivero":
//                        if (!"".equals(instrucFide.getTxtInstrucFideExpArchivero()) && onValidaNumerico(instrucFide.getTxtInstrucFideExpArchivero(), "Archivero", "S")) {
//                            instrucFide.setInstrucFideExpArchivero(Integer.parseInt(instrucFide.getTxtInstrucFideExpArchivero()));
//                        } else {
//                            instrucFide.setInstrucFideExpArchivero(null);
//                            instrucFide.setTxtInstrucFideExpArchivero(null);
//                        }
//                        break;
//                    case "txtInstrucFideExpNiivel":
//                        if (!"".equals(instrucFide.getTxtInstrucFideExpNiivel()) && onValidaNumerico(instrucFide.getTxtInstrucFideExpNiivel(), "Nivel", "S")) {
//                            instrucFide.setInstrucFideExpNiivel(Integer.parseInt(instrucFide.getTxtInstrucFideExpNiivel()));
//                        } else {
//                            instrucFide.setInstrucFideExpNiivel(null);
//                            instrucFide.setTxtInstrucFideExpNiivel(null);
//                        }
//                        break;
//                }
//
//            } catch (NumberFormatException Err) {
//                logger.error(Err.getMessage());
//            } finally {
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
//            }
//
//        }

//        private Boolean onValidaNumerico(String sEntrada, String campo, String tipo) {
//            Boolean bSalida = false;
//            try {
//
//                if ("I".equals(tipo)) {
//                    Integer.parseInt(sEntrada);
//                    bSalida = true;
//                }
//                if ("L".equals(tipo)) {
//                    Long.parseLong(sEntrada);
//                    bSalida = true;
//                }
//                if ("D".equals(tipo)) {
//                    Double.parseDouble(sEntrada);
//                    bSalida = true;
//                }
//                if ("S".equals(tipo)) {
//                    Short.parseShort(sEntrada);
//                    bSalida = true;
//                }
//            } catch (NumberFormatException Err) {
//                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo " + campo + "  no es numérico...");
//            }
//
//            return bSalida;
//        }

        private void onFinalizaObjetos() {
            if (oInstruccionesFideicomiso != null) {
                oInstruccionesFideicomiso = null;
            }
        }

        private Boolean onVerificaAtencionContrato(long contratoNumero) {
            Boolean validacion = Boolean.FALSE;
            for (int itemFiso = 0; itemFiso <= usuarioFiltro.size() - 1; itemFiso++) {
                if (Long.parseLong(usuarioFiltro.get(itemFiso)) == contratoNumero) {
                    validacion = Boolean.TRUE;
                    break;
                }
            }
            return validacion;
        }

    }
    //end -efp

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBInstruccionesFideicomisos() {
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
                //System.out.println(peticionURL.getRequestURI());
            }
            instructions = null;
            String parinstFolioInst = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instFolioInst");
            if (parinstFolioInst != null && !parinstFolioInst.equals(0)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instFolioInst", null);

                seleccionaInstrucFideicomiso = null;
                oInstruccionesFideicomiso = new CInstruccionesFideicomiso();

                DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmInstrucFide:dtInstrucFide");
                dataTable.setFirst(0);
                cbif.setCriterioBusqInstrucFideFolioInst(Integer.parseInt(parinstFolioInst));
                cbif.setTxtCriterioBusqInstrucFideFolioInst(parinstFolioInst);
                int totalRows = oInstruccionesFideicomiso.getInstructionsTotalRows(cbif);
                InstruccionFideicomisoBean bean = new InstruccionFideicomisoBean();
                MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getinstructions");
                instructions = lazyModel.getLazyDataModel(cbif);
                instructions.setRowCount(totalRows);

                oInstruccionesFideicomiso = null;
            }
        } catch (IOException | SQLException Err) {
            logger.error(Err.getMessage());
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   B I E N E S   F I D E I C O M
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //begin -efp
    public void onInstruccionesFideicomisos_ConsultaEjecuta() {
        seleccionaInstrucFideicomiso = null;
        instructions = null;

        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_ConsultaEjecuta();
        oInstruccionesFideicomisos = null;
    }
    //end -efp

    //begin -efp
    public void onInstruccionesFideicomisos_RedirectMonetarias() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RedirectMonetarias();
        oInstruccionesFideicomisos = null;
    }
    //end -efp

    public void onInstruccionesFideicomisos_ConsultaLimpia() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_ConsultaLimpia();
        oInstruccionesFideicomisos = null;
    }

    //begin -efp
    public void onInstruccionesFideicomisos_RegistroGuardar() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroGuardar();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_RegistroModificar() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroModificar();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_RegistroEliminar() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroEliminar();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_AplicarRegistroEliminar() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_AplicarRegistroEliminar();
        oInstruccionesFideicomisos = null;
    }
    //end -efp

    //begin -efp
    public void onInstruccionesFideicomisos_RegistroValidaContrato() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroValidaContrato();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_RegistroValidaFechaCorrecta() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroValidaFechaCorrecta();
        oInstruccionesFideicomisos = null;
    }
    //begin -efp

    public void onInstruccionesFideicomisos_RegistroSelecciona() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroSelecciona();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_RegistroEntrada() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroEntrada();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_RegistroParametrosDeLiquidacion() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_RegistroParametrosDeLiquidacion();
        oInstruccionesFideicomisos = null;
    }

    public void onInstruccionesFideicomisos_ExpedienteGuardar() {
        oInstruccionesFideicomisos = new instruccionesFideicomisos();
        oInstruccionesFideicomisos.onInstruccionesFideicomisos_ExpedienteGuardar();
        oInstruccionesFideicomisos = null;
    }

//    public void onInstrucciones_VerificaNumerico(AjaxBehaviorEvent event) {
//        oInstruccionesFideicomisos = new instruccionesFideicomisos();
//
//        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
//        if (componet.getAttributes().get("txtCriterioBusqInstrucFideicomiso") != null) {
//            String txtCriterioBusqInstrucFideicomiso = (String) componet.getAttributes().get("txtCriterioBusqInstrucFideicomiso");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtCriterioBusqInstrucFideicomiso);
//        }
//        if (componet.getAttributes().get("txtCriterioBusqInstrucFideSubCto") != null) {
//            String txtCriterioBusqInstrucFideSubCto = (String) componet.getAttributes().get("txtCriterioBusqInstrucFideSubCto");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtCriterioBusqInstrucFideSubCto);
//        }
//
//        if (componet.getAttributes().get("txtCriterioBusqInstrucFideFolioInst") != null) {
//            String txtCriterioBusqInstrucFideFolioInst = (String) componet.getAttributes().get("txtCriterioBusqInstrucFideFolioInst");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtCriterioBusqInstrucFideFolioInst);
//        }
//
//        if (componet.getAttributes().get("txtCriterioBusqInstrucFidePlaza") != null) {
//            String txtCriterioBusqInstrucFidePlaza = (String) componet.getAttributes().get("txtCriterioBusqInstrucFidePlaza");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtCriterioBusqInstrucFidePlaza);
//        }
//        if (componet.getAttributes().get("txtInstrucFideExpArchivo") != null) {
//            String txtInstrucFideExpArchivo = (String) componet.getAttributes().get("txtInstrucFideExpArchivo");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtInstrucFideExpArchivo);
//        }
//        if (componet.getAttributes().get("txtInstrucFideExpArchivero") != null) {
//            String txtInstrucFideExpArchivero = (String) componet.getAttributes().get("txtInstrucFideExpArchivero");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtInstrucFideExpArchivero);
//        }
//        if (componet.getAttributes().get("txtInstrucFideExpNiivel") != null) {
//            String txtInstrucFideExpNiivel = (String) componet.getAttributes().get("txtInstrucFideExpNiivel");
//            oInstruccionesFideicomisos.onInstrucciones_VerificaNumerico(txtInstrucFideExpNiivel);
//        }
//
//        oInstruccionesFideicomisos = null;
//    }

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

}
