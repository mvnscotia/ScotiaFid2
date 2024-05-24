/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : MBReportes.java
 * TIPO        : Bean administrado
 * PAQUETE     : scotiaFid.controller
 * CREADO      : 20210702
 * MODIFICADO  : 20211019
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.primefaces.context.RequestContext;

import scotiaFid.bean.CriterioBusquedaReporte;
import scotiaFid.bean.GenericoDesHabilitadoBean;
import scotiaFid.bean.GenericoVisibleBean;
import scotiaFid.bean.GenericoFechaBean;
import scotiaFid.bean.MensajeConfirmaBean;

import scotiaFid.dao.CContrato;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CMoneda;
import scotiaFid.dao.CReportes;

import org.primefaces.model.UploadedFile;
import scotiaFid.util.Constantes;
import scotiaFid.util.LogsContext;

@ManagedBean(name = "mbReportes")
@ViewScoped
public class MBReportes implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBReportes.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean validacion;
    private Integer usuarioNumero;
    private List<String> usuarioFiltro;
    private String destinoURL;
//    private String                               usuarioNombre;
    private String usuarioTerminal;
    private String usuarioTipo;
    private String nombreObjeto;
    private String mensajeError;

    private FacesMessage mensaje;
    private static SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private reporteGeneracion oRepGen;
//    private reporteGenerales                     oRepGrales;

    private CComunes oComunes;
    private CContrato oContrato;
    private CMoneda oMoneda;
    private CReportes oReportes;

    private HttpServletRequest peticion;
    private String PDF_REPORT_TYPE = "PDF";
    private String TXT_REPORT_TYPE = "TXT";
    private Calendar calendario;
    private String archivoNombre;
    private String archivoUbicacion;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @ManagedProperty(value = "#{beanCriterioBusquedaRep}")
    private CriterioBusquedaReporte criterioRep;
    @ManagedProperty(value = "#{beanGenericoDesHabilitado}")
    private GenericoDesHabilitadoBean desHabilitado;
    @ManagedProperty(value = "#{beanGenericoFecha}")
    private GenericoFechaBean fecha;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean visible;
    @ManagedProperty(value = "#{beanMensajeConfirmacion}")
    private MensajeConfirmaBean mensajeConfirma;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean geneVisible;

    private List<String> listaContrato;
    private List<String> listaContratoSub;
    private List<String> listaMoneda;
    private List<String> listaReporte;
    private List<String> listaUsuGral;
    private List<String> listaUsuEdoFin;

    //private List<ReporteFirmaEdoFinBean>         consultaFirma;
    //private ReporteFirmaEdoFinBean               seleccionaFirma;
    private UploadedFile archivoFirma;
    private String visibleAnoMes;
    private Boolean visiblePDF;
    private Boolean visibleTXT;
    private String fideicomisoTxt;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
 /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * G E T T E R S   Y   S E T T E R S
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean getVisiblePDF() {
        return visiblePDF;
    }

    public void setVisiblePDF(Boolean visiblePDF) {
        this.visiblePDF = visiblePDF;
    }

    public Boolean getVisibleTXT() {
        return visibleTXT;
    }

    public void setVisibleTXT(Boolean visibleTXT) {
        this.visibleTXT = visibleTXT;
    }

    public String getVisibleAnoMes() {
        return visibleAnoMes;
    }

    public void setVisibleAnoMes(String visibleAnoMes) {
        this.visibleAnoMes = visibleAnoMes;
    }

    public void setCriterioRep(CriterioBusquedaReporte criterioRep) {
        this.criterioRep = criterioRep;
    }

    public void setDesHabilitado(GenericoDesHabilitadoBean desHabilitado) {
        this.desHabilitado = desHabilitado;
    }

    public void setFecha(GenericoFechaBean fecha) {
        this.fecha = fecha;
    }

    public void setVisible(GenericoVisibleBean visible) {
        this.visible = visible;
    }

    public void setMensajeConfirma(MensajeConfirmaBean mensajeConfirma) {
        this.mensajeConfirma = mensajeConfirma;
    }
    //--

    public List<String> getListaContrato() {
        return listaContrato;
    }

    public List<String> getListaContratoSub() {
        return listaContratoSub;
    }

    public List<String> getListaMoneda() {
        return listaMoneda;
    }

    public List<String> getListaReporte() {
        return listaReporte;
    }

    public List<String> getListaUsuGral() {
        return listaUsuGral;
    }

    public List<String> getListaUsuEdoFin() {
        return listaUsuEdoFin;
    }

    public UploadedFile getArchivoFirma() {
        return archivoFirma;
    }

    public void setArchivoFirma(UploadedFile archivoFirma) {
        this.archivoFirma = archivoFirma;
    }

    public void setGeneVisible(GenericoVisibleBean geneVisible) {
        this.geneVisible = geneVisible;
    }

    public String getFideicomisoTxt() {
        return fideicomisoTxt;
    }

    public void setFideicomisoTxt(String fideicomisoTxt) {
        this.fideicomisoTxt = fideicomisoTxt;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O 
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    class reporteGenerales {

        public reporteGenerales() {
            nombreObjeto = "\nFuente: scotiaFid.mbReportes.reporteGeneracion.";
            mensajeError = "Error En Tiempo de Ejecución. \n";
            mensaje = null;
            validacion = Boolean.TRUE;
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");

            usuarioTipo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTipo");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");

        }
        //Funciones privadas
        //  private void onFinalizaObjetos(){
        //      if (oReportes!= null) oReportes = null;
        //  }
    }

    class reporteGeneracion {

        public reporteGeneracion() {
            nombreObjeto = "\nFuente: scotiaFid.mbReportes.reporteGeneracion.";
            mensajeError = "Error En Tiempo de Ejecución. \n";
            mensaje = null;
            validacion = Boolean.TRUE;
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
        }

        public Boolean onReporteGenera_VerificaNumerico(String Campo) {
            Boolean validacion = Boolean.TRUE;
            mensaje = null;
            switch (Campo) {
                case "Fideicomiso":
                    if (!criterioRep.getTxtcriterioRepContratoNum().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepContratoNum(), "Fideicomiso", "I")) {
                        criterioRep.setCriterioRepContratoNum(Integer.parseInt(criterioRep.getTxtcriterioRepContratoNum()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepContratoNum().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepContratoNum(null);
                        criterioRep.setTxtcriterioRepContratoNum(new String());
                    }
                    break;
                case "SubFiso":
                    if (!criterioRep.getTxtcriterioRepContratoNumSub().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepContratoNumSub(), "SubFiso(A2):", "I")) {
                        criterioRep.setCriterioRepContratoNumSub(Integer.parseInt(criterioRep.getTxtcriterioRepContratoNumSub()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepContratoNumSub().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepContratoNumSub(null);
                        criterioRep.setTxtcriterioRepContratoNumSub(new String());
                    }
                    break;

                case "Anio":
                    if (!criterioRep.getTxtcriterioRepAño().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepAño(), "Año", "I")) {
                        criterioRep.setCriterioRepAño(Integer.parseInt(criterioRep.getTxtcriterioRepAño()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepAño().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepAño(null);
                        criterioRep.setTxtcriterioRepAño(new String());
                    }
                    break;
                case "Mes":
                    if (!criterioRep.getTxtcriterioRepMes().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepMes(), "Mes", "I")) {
                        criterioRep.setCriterioRepMes(Integer.parseInt(criterioRep.getTxtcriterioRepMes()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepMes().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepMes(null);
                        criterioRep.setTxtcriterioRepMes(new String());
                    }
                    break;
                case "CuentaMayor":
                    if (!criterioRep.getTxtcriterioRepCTAM().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepCTAM(), "CTAM", "I")) {
                        criterioRep.setCriterioRepCTAM(Integer.parseInt(criterioRep.getTxtcriterioRepCTAM()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepCTAM().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepCTAM(0);
                        criterioRep.setTxtcriterioRepCTAM("0");
                    }
                    break;
                case "SC1":
                    if (!criterioRep.getTxtcriterioRepSC1().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepSC1(), "SC1", "I")) {
                        criterioRep.setCriterioRepSC1(Integer.parseInt(criterioRep.getTxtcriterioRepSC1()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepSC1().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepSC1(0);
                        criterioRep.setTxtcriterioRepSC1("0");
                    }
                    break;
                case "SC2":
                    if (!criterioRep.getTxtcriterioRepSC2().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepSC2(), "SC2", "I")) {
                        criterioRep.setCriterioRepSC2(Integer.parseInt(criterioRep.getTxtcriterioRepSC2()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepSC2().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepSC2(0);
                        criterioRep.setTxtcriterioRepSC2("0");
                    }
                    break;
                case "SC3":
                    if (!criterioRep.getTxtcriterioRepSC3().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepSC3(), "SC3", "I")) {
                        criterioRep.setCriterioRepSC3(Integer.parseInt(criterioRep.getTxtcriterioRepSC3()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepSC3().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepSC3(0);
                        criterioRep.setTxtcriterioRepSC3("0");
                    }
                    break;
                case "SC4":
                    if (!criterioRep.getTxtcriterioRepSC4().equals(new String()) && onValidaNumerico(criterioRep.getTxtcriterioRepSC4(), "SC4", "I")) {
                        criterioRep.setCriterioRepSC4(Integer.parseInt(criterioRep.getTxtcriterioRepSC4()));
                    } else {
                        if (!criterioRep.getTxtcriterioRepSC4().equals(new String())) {
                            validacion = Boolean.FALSE;
                        }
                        criterioRep.setCriterioRepSC4(0);
                        criterioRep.setTxtcriterioRepSC4("0");
                    }
                    break;
                case "AX3":
                    if (criterioRep.getTxtcriterioRepCtoInv().equals(new String())) {
                        criterioRep.setCriterioRepCtoInv(0L);
                        criterioRep.setTxtcriterioRepCtoInv(null);

                    } else {
                        if (!criterioRep.getTxtcriterioRepCtoInv().equals(new String())) {
                            if (onValidaNumerico(criterioRep.getTxtcriterioRepCtoInv(), "Auxiliar(A3)", "L")) {
                                criterioRep.setCriterioRepCtoInv(Long.parseLong(criterioRep.getTxtcriterioRepCtoInv()));
                            } else {
                                if (!criterioRep.getTxtcriterioRepCtoInv().equals(new String())) {
                                    validacion = Boolean.FALSE;
                                }
                                criterioRep.setCriterioRepCtoInv(0L);
                                criterioRep.setTxtcriterioRepCtoInv("0");
                            }
                        }
                    }
                    break;
            }
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
            return validacion;
        }

        public void onReporteGenera_VerificaFormatoReporte() {
            String filtroRep = new String();
            try {
                if (criterioRep.getCriterioRepReporteSel() == null) {
                    onPost();
                    onReporteGenera_Inicializa();
                    return;
                }
                //Reiniciar variables
                fideicomisoTxt = null;

                oComunes = new CComunes();
                criterioRep.setCriterioRepFmto(oComunes.onComunes_ObtenListadoContenidoCampo("edoFinCatFormato", "SOFI_EdoFinCat", "WHERE edoFinCatNumRep= ".concat(criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf(".")))).get(0));

                //20210209
                //j.delatorre
                //Son variables que se pasan al SP, pero no pueden estar con valor nulo
                criterioRep.setCriterioRepContratoNum(0);
                criterioRep.setCriterioRepContratoNumSub(0);
                criterioRep.setCriterioRepMonedaNum(0);
                criterioRep.setCriterioRepMonedaNom(null);
                criterioRep.setCriterioRepCTAM(0);
                criterioRep.setCriterioRepSC1(0);
                criterioRep.setCriterioRepSC2(0);
                criterioRep.setCriterioRepSC3(0);
                criterioRep.setCriterioRepSC4(0);
                criterioRep.setCriterioRepCtoInv(Long.parseLong("0"));
                fecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                fecha.setGenericoFecha02(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));

                /// Fecha del sistema en campos Fecha, Año y Mes.
                //  String subtractedDate = getSubtractedDate((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema"), 1);
                String subtractedDate = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                String[] subtractedDateArray = subtractedDate.split("/");
                fecha.setGenericoFecha00(formatFechaParse(subtractedDate));
                criterioRep.setCriterioRepMes(Integer.parseInt(subtractedDateArray[1]));
                criterioRep.setCriterioRepAño(Integer.parseInt(subtractedDateArray[2]));
                //---
                criterioRep.setTxtcriterioRepReporteNum(null);
                criterioRep.setTxtcriterioRepContratoNum(null);
                criterioRep.setTxtcriterioRepContratoNumSub(null);
                criterioRep.setTxtcriterioRepMonedaNum(null);
                criterioRep.setTxtcriterioRepAño(criterioRep.getCriterioRepAño().toString());
                criterioRep.setTxtcriterioRepMes(criterioRep.getCriterioRepMes().toString());
                criterioRep.setTxtcriterioRepCTAM("0");
                criterioRep.setTxtcriterioRepSC1("0");
                criterioRep.setTxtcriterioRepSC2("0");
                criterioRep.setTxtcriterioRepSC3("0");
                criterioRep.setTxtcriterioRepSC4("0");
                criterioRep.setTxtcriterioRepCtoInv("0");
                criterioRep.setTxtcriterioRepFolioCont(null);
                listaContratoSub = null;

                criterioRep.setCriterioBitPantalla("Generación de Reportes");
                criterioRep.setCriterioBitTerminal(usuarioTerminal);
                //Fin  
                filtroRep = oComunes.onComunes_ObtenListadoContenidoCampo("edoFinCatFirma2", "SOFI_EdoFinCat", "WHERE edoFinCatNumRep= ".concat(criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf(".")))).get(0);
                oComunes = null;
                onInHabilitaFiltros();
                if (filtroRep.contains(";")) {
                    if (filtroRep.contains("F00")) {
                        desHabilitado.setGenericoDesHabilitado00(Boolean.FALSE);//Contrato
                    }
                    if (filtroRep.contains("F01")) {
                        desHabilitado.setGenericoDesHabilitado01(Boolean.FALSE);//Moneda
                    }
                    if (filtroRep.contains("F02")) {
                        desHabilitado.setGenericoDesHabilitado02(Boolean.FALSE);//Fecha
                    }
                    if (filtroRep.contains("F03")) {
                        desHabilitado.setGenericoDesHabilitado03(Boolean.FALSE);//Del - Al
                    }
                    if (filtroRep.contains("F04")) {
                        desHabilitado.setGenericoDesHabilitado04(Boolean.FALSE);//Año
                    }
                    if (filtroRep.contains("F05")) {
                        desHabilitado.setGenericoDesHabilitado05(Boolean.FALSE);//Mes
                    }
                    if (filtroRep.contains("F06")) {
                        desHabilitado.setGenericoDesHabilitado06(Boolean.FALSE);//Cuenta Contable
                    }
                    if (filtroRep.contains("F07")) {
                        desHabilitado.setGenericoDesHabilitado07(Boolean.FALSE);//Folio Contable
                    }
                    if (filtroRep.contains("F08")) {
                        desHabilitado.setGenericoDesHabilitado08(Boolean.FALSE);//Cto. Inv.
                    }
                    if (filtroRep.contains("F09")) {
                        desHabilitado.setGenericoDesHabilitado09(Boolean.FALSE);//Rol Fid.
                    }
                }
                if (criterioRep.getCriterioRepFmto().equals("H")) {
                    criterioRep.setCriterioRepFmto("HORIZONTAL");
                }
                if (criterioRep.getCriterioRepFmto().equals("V")) {
                    criterioRep.setCriterioRepFmto("VERTICAL");
                }
                String txtPaso = criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf("."));
                if (!txtPaso.equals("4")) {
                    setVisibleAnoMes("");
                    visiblePDF = false;
                    visibleTXT = true;

                    desHabilitado.setGenericoDesHabilitado01(Boolean.TRUE);
                    //                if (txtPaso.equals("2")) desHabilitado.setGenericoDesHabilitado01(Boolean.FALSE);
                } else {
                    //setVisiblePDF(Boolean.FALSE);
                    //setVisibleTXT(Boolean.TRUE);
                    visiblePDF = true;
                    visibleTXT = false;
                    setVisibleAnoMes("none");
                    desHabilitado.setGenericoDesHabilitado01(Boolean.FALSE);
                }
            } catch (NumberFormatException | SQLException e) {
                logger.error(e.getMessage() + "onReporteGenera_VerificaFormatoReporte()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onReporteGenera_CargaListaSubCont() {
            try {
                oContrato = new CContrato();
                if (onValidaNumerico(criterioRep.getTxtcriterioRepContratoNum(), "Fideicomiso", "I")) {
//                oContrato = new CContrato();
                    criterioRep.setCriterioRepContratoNum(Integer.parseInt(criterioRep.getTxtcriterioRepContratoNum()));
                    if (oContrato.onContrato_VerificaExistencia(criterioRep.getCriterioRepContratoNum()).equals(Boolean.TRUE)) {
                        listaContratoSub = oContrato.onContrato_ObtenListadoContratoSub(criterioRep.getCriterioRepContratoNum(), "ACTIVO");
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece...");
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece...");
                    }
                } else {
                    criterioRep.setTxtcriterioRepContratoNum("0");
                    criterioRep.setCriterioRepContratoNum(0);
                    listaContratoSub = null;
                }
                oContrato = null;

            } catch (NumberFormatException | SQLException e) {
                logger.error(e.getMessage() + "onReporteGenera_CargaListaSubCont()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onReporteGenera_ActualizaFechas() {
            try {
                if (fecha.getGenericoFecha00() != null) {
                    fecha.setGenericoFecha01(fecha.getGenericoFecha00());
                    fecha.setGenericoFecha02(fecha.getGenericoFecha00());
                }
            } catch (AbstractMethodError e) {
                logger.error(e.getMessage() + "onReporteGenera_ActualizaFechas()");
            }
        }

        public void onReporteGenera_UpdateToCurrentDate() {
            try {
                String systemDate = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                String[] systemDateArr = systemDate.split("/");

                fecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                criterioRep.setCriterioRepMes(Integer.parseInt(systemDateArr[1]));
                criterioRep.setCriterioRepAño(Integer.parseInt(systemDateArr[2]));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage() + "onReporteGenera_UpdateToCurrentDate()");
            }
        }

        public void onReporteGenera_Genera(String type) {
            Boolean valorRetorno = Boolean.FALSE;
            try {
                mensaje = null;
                String txtPaso = criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf("."));

//                if (((criterioRep.getCriterioRepReporteSel() == null) || (criterioRep.getCriterioRepReporteSel().equals(new String()))) && (validacion.equals(Boolean.TRUE))) {
//                    validacion = Boolean.FALSE;
//                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error, seleccione un reporte a ser generado");
//                }
//* Fideicomiso
                Boolean validaFiso = Boolean.TRUE;
                oContrato = new CContrato();
                if (onReporteGenera_VerificaNumerico("Fideicomiso")) {
                    if (criterioRep.getCriterioRepContratoNum() == null || criterioRep.getCriterioRepContratoNum() == 0) {
                        criterioRep.setCriterioRepContratoNum(null);
                    }
                    if (criterioRep.getCriterioRepContratoNum() == null) {
                        if (!txtPaso.equals("4")) {
                            validacion = Boolean.FALSE;
                            validaFiso = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        usuarioFiltro = new ArrayList<>();
                        usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");

                        if (!oContrato.onContrato_VerificaAtencion(usuarioFiltro, criterioRep.getCriterioRepContratoNum()).equals(Boolean.TRUE)) {
                            validacion = Boolean.FALSE;
                            validaFiso = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    }
                } else {
                    validacion = false;
                    validaFiso = Boolean.FALSE;
                }
//*SubFiso
                if (onReporteGenera_VerificaNumerico("SubFiso")) {
                    if (criterioRep.getCriterioRepContratoNumSub() != null) {
                        if (validaFiso && criterioRep.getCriterioRepContratoNum() != null && validaFiso) {
                            if (criterioRep.getCriterioRepContratoNumSub() != 0) {
                                validacion = oContrato.onContrato_VerificaExistenciaSubContrato(criterioRep.getCriterioRepContratoNum(), criterioRep.getCriterioRepContratoNumSub(), usuarioNumero);
                                if (!validacion) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  SubFiso " + criterioRep.getTxtcriterioRepContratoNumSub() + " no existe...");
                                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                                }
                            }
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Primero debe ingresar un Fideicomiso correcto...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                            validacion = false;
                        }
                    }
                } else {
                    validacion = false;
                }
                oContrato = null;
//Moneda
                if ((desHabilitado.getGenericoDesHabilitado01().equals(Boolean.FALSE))) {//Moneda
                    if ((criterioRep.getCriterioRepMonedaNom() == null) || (criterioRep.getCriterioRepMonedaNom().equals(new String()))) {

                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }
//Fechas del-Al                
                oComunes = new CComunes();
                if ((desHabilitado.getGenericoDesHabilitado02().equals(Boolean.FALSE))) {//Fecha

                    java.sql.Date fechaDel = new java.sql.Date(fecha.getGenericoFecha01().getTime());
                    valorRetorno = oComunes.onCommunes_BuscaDiasFeriados(fechaDel);
                    if (valorRetorno.equals(Boolean.TRUE)) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Del no debe ser un dia feriado...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }

                    java.sql.Date fechaAl = new java.sql.Date(fecha.getGenericoFecha02().getTime());
                    valorRetorno = oComunes.onCommunes_BuscaDiasFeriados(fechaAl);
                    if (valorRetorno.equals(Boolean.TRUE)) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Al no debe ser un dia feriado...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }

                    Integer dias = fechaDel.compareTo(fechaAl);
                    if (dias > 0) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Del mayor al campo Fecha Al...");
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    } else {
                        Calendar calendarioDel = Calendar.getInstance();
                        Calendar calendarioAl = Calendar.getInstance();
                        calendarioDel.setTime(fechaDel);
                        calendarioAl.setTime(fechaAl);
                        int diff = calendarioAl.get(Calendar.YEAR) - calendarioDel.get(Calendar.YEAR);
                        if (diff != 0 && calendarioAl.get(Calendar.DAY_OF_YEAR) > calendarioDel.get(Calendar.DAY_OF_YEAR)) {
                            diff++;
                        }
                        if (diff > 1 && criterioRep.getCriterioRepContratoNum() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El rango de fechas de los campos Fecha Del y Fecha Al no puede ser mayor a un año...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }

                    }
                }

                oComunes = null;

                if ((desHabilitado.getGenericoDesHabilitado04().equals(Boolean.FALSE))) {//Año
                    if (onReporteGenera_VerificaNumerico("Anio")) {
                        if (criterioRep.getCriterioRepAño() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Año no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                        if (criterioRep.getCriterioRepAño() != null && criterioRep.getCriterioRepAño() < 1900) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Año debe de contener un valor superior a 1900...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado05().equals(Boolean.FALSE))) {//Mes
                    if (onReporteGenera_VerificaNumerico("Mes")) {
                        if (criterioRep.getCriterioRepMes() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mes no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                        if (criterioRep.getCriterioRepMes() != null && (criterioRep.getCriterioRepMes() <= 0 || criterioRep.getCriterioRepMes() > 13)) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mes debe de contener un valor entre 1 y 13...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado06().equals(Boolean.FALSE))) {//Cuenta contable
                    if (onReporteGenera_VerificaNumerico("CuentaMayor")) {
                        if (criterioRep.getCriterioRepCTAM() == null || criterioRep.getCriterioRepCTAM() == 0) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo CTAM no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }

                    if (onReporteGenera_VerificaNumerico("SC1")) {
                        if (criterioRep.getCriterioRepSC1() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC1 no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }

                    if (onReporteGenera_VerificaNumerico("SC2")) {
                        if (criterioRep.getCriterioRepSC2() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC2 no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }

                    if (onReporteGenera_VerificaNumerico("SC3")) {
                        if (criterioRep.getCriterioRepSC3() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC3 no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }

                    if (onReporteGenera_VerificaNumerico("SC4")) {
                        if (criterioRep.getCriterioRepSC4() == null) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC4 no puede estar vacío...");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        }
                    } else {
                        validacion = false;
                    }

                    if (!onReporteGenera_VerificaNumerico("AX3")) {
                        validacion = false;
                    } else {
                        if (criterioRep.getCriterioRepCtoInv() < 0) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Auxiliar(A3) debe ser mayor igual a Cero");
                            FacesContext.getCurrentInstance().addMessage(null, mensaje);
                            validacion = false;
                        }
                    }
                }
//                if ((desHabilitado.getGenericoDesHabilitado07().equals(Boolean.FALSE))) {//Folio contable
//                    if (criterioRep.getCriterioRepFolioCont() == null) {
//                        validacion = Boolean.FALSE;
//                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error, el campo « Folio Contable » debe de contener un valor");
//                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                    }
//                }

                mensaje = null;

                if (validacion.equals(Boolean.TRUE)) {
                    criterioRep.setCriterioBitUsuarioNum(usuarioNumero);
                    criterioRep.setCriterioRepReporteNum(Integer.parseInt(criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf("."))));
                    if ((criterioRep.getCriterioRepReporteNum() == 2) || (criterioRep.getCriterioRepReporteNum() == 16)) {
                        criterioRep.setCriterioRepMonedaNum(1);
                    }
                    if (desHabilitado.getGenericoDesHabilitado01().equals(Boolean.FALSE)) {
                        oMoneda = new CMoneda();
                        criterioRep.setCriterioRepMonedaNum(oMoneda.onMoneda_ObtenMonedaId(criterioRep.getCriterioRepMonedaNom()));
                        oMoneda = null;
                    }
                    if (desHabilitado.getGenericoDesHabilitado02().equals(Boolean.FALSE)) {
                        criterioRep.setCriterioRepFecha(new java.sql.Date(fecha.getGenericoFecha00().getTime()));
                    }
//                    if (criterioRep.getCriterioRepContratoNomSub() != null) {
//                        criterioRep.setCriterioRepContratoNumSub(Integer.parseInt(criterioRep.getCriterioRepContratoNomSub().substring(0, criterioRep.getCriterioRepContratoNomSub().indexOf("."))));
//                    } else {
//                        criterioRep.setCriterioRepContratoNumSub(null);
//                    }
//                    // ***
                    //  la modeda siempre es cero si no es auxiliar por cuenta
                    if (!txtPaso.equals("4")) {
                        criterioRep.setCriterioRepMonedaNum(0);
                        // siempre se mueve cero al subcontrato para que sp funcione correctamente
                        // sin importar lo que seleccione el usuario
                        criterioRep.setCriterioRepContratoNumSub(0);
                        // ***
                    } else {
                        criterioRep.setCriterioRepMes(0);
                        criterioRep.setCriterioRepAño(0);
                    }
                    criterioRep.setCriterioRepFechaDel(new java.sql.Date(fecha.getGenericoFecha01().getTime()));
                    criterioRep.setCriterioRepFechaAl(new java.sql.Date(fecha.getGenericoFecha02().getTime()));
                    oReportes = new CReportes();

                    if (oReportes.onReporte_GeneraInformacion(criterioRep).equals(Boolean.TRUE)
                            || "SP_GEN_EDOFINREP:  No existen datos que cumplan con los criterios de consulta".equals(oReportes.getMensajeError())
                            && "2".equals(txtPaso)) {
                        if (type.equals(PDF_REPORT_TYPE)) {
                            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                            if (criterioRep.getCriterioRepReporteSel().contains("BALANZA") || criterioRep.getCriterioRepReporteSel().contains("RESULTADOS")) {
//                            if (criterioRep.getCriterioRepReporteSel().contains("BALANZA")) {
                                destinoURL = "/scotiaFid/PDFReportServlet?";
                            } else {
                                destinoURL = "/scotiaFid/SGeneraReporteGenerico?";
                            }
                            if (destinoURL != null) {
                                destinoURL = destinoURL.concat(criterioRep.getCriterioRepReporteNum().toString());
                                destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepContratoNum().toString());
                                destinoURL = destinoURL.concat("&").concat(String.valueOf((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero")));
                                destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepReporteSel());
                                destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepFmto());
                                destinoURL = destinoURL.concat("&").concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema"));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepAño()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepMes()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepCTAM()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepSC1()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepSC2()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepSC3()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepSC4()));
                                destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepCtoInv()));
                                destinoURL = destinoURL.concat("&").concat(formatFecha(fecha.getGenericoFecha00()));//Fecha

                                FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                            }
                        } else {
                            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                            calendario = Calendar.getInstance();
                            calendario.setTime(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                            formatoFecha = new SimpleDateFormat("yyyyMMdd");

                            archivoNombre = formatFecha(calendario.getTime()).concat("_").concat(usuarioNumero.toString()).concat("_").concat("REPORTE_").concat(criterioRep.getCriterioRepReporteSel().replace(".-", new String()).trim().replace(" ", "_")).concat(".txt");
                            archivoNombre = archivoNombre.replace("Á", "A");
                            archivoNombre = archivoNombre.replace("É", "E");
                            archivoNombre = archivoNombre.replace("Í", "I");
                            archivoNombre = archivoNombre.replace("Ó", "O");
                            archivoNombre = archivoNombre.replace("Ú", "U");
                            archivoUbicacion = Constantes.RUTA_TEMP;
                            if (archivoUbicacion != null) {
                                archivoUbicacion = archivoUbicacion.concat("/");

                                if (oReportes.getReportByColumns(criterioRep, archivoUbicacion.concat(archivoNombre)).equals(Boolean.TRUE)) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                                    destinoURL = "/scotiaFid/SArchivoDescarga?".concat(archivoNombre);
                                    geneVisible.setGenericoVisible15("hidden;");
                                    if(destinoURL != null){
                                        FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                                    }
                                    RequestContext.getCurrentInstance().execute("dlgCtrlEjecuta.hide();");
                                } else {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oReportes.getMensajeError());
                                }
                                /*List<String> reportList = oReportes.getReportByColumns(criterioRep, archivoUbicacion.concat(archivoNombre));

                            for (int i = 0; i < reportList.size(); i++) {
                                System.out.println(reportList.get(i));
                            }*/
                            }
                        }
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oReportes.getMensajeError());
                    }
                    oReportes = null;
                }
            } catch (IOException | NumberFormatException | ParseException | SQLException Err) {
                logger.error(Err.getMessage() + "onReporteGenera_Genera");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onReporteGenera_GeneraMasivo() {
            try {
                if (((criterioRep.getCriterioRepReporteSel() == null) || (criterioRep.getCriterioRepReporteSel().equals(new String()))) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "seleccione un reporte a ser generado...");
                }
                if ((desHabilitado.getGenericoDesHabilitado01().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Moneda
                    if ((criterioRep.getCriterioRepMonedaNom() == null) || (criterioRep.getCriterioRepMonedaNom().equals(new String()))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado02().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Fecha
                    if (fecha.getGenericoFecha00() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado03().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Del - Al
                    if ((fecha.getGenericoFecha01() == null) || (fecha.getGenericoFecha02() == null)) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Los campos Del/Al no pueden estar vacíos...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado04().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Año
                    if (criterioRep.getCriterioRepAño() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Año no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado05().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Mes
                    if (criterioRep.getCriterioRepMes() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mes no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado06().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Cuenta contable
                    if (criterioRep.getCriterioRepCTAM() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo CTAM no puede estar vacío...");
                    }
                    if (criterioRep.getCriterioRepSC1() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC1 no puede estar vacío...");
                    }
                    if (criterioRep.getCriterioRepSC2() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC2 no puede estar vacío...");
                    }
                    if (criterioRep.getCriterioRepSC3() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC3 no puede estar vacío...");
                    }
                    if (criterioRep.getCriterioRepSC4() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SC4 no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado07().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Folio contable
                    if (criterioRep.getCriterioRepFolioCont() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Folio Contable no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado08().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Cto. Inversión
                    if (criterioRep.getCriterioRepCtoInv() == null) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Cto. Inversión no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado10().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Firmas Edo Fin
                    if ((criterioRep.getCriterioRepFirma01() == null) || (criterioRep.getCriterioRepFirma01().equals(new String()))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Firma 1 no puede estar vacío...");
                    }
                }
                if ((desHabilitado.getGenericoDesHabilitado11().equals(Boolean.FALSE)) && (validacion.equals(Boolean.TRUE))) {//Firmas Edo Fin
                    if ((criterioRep.getCriterioRepFirma02() == null) || (criterioRep.getCriterioRepFirma02().equals(new String()))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Firma 2 no puede estar vacío...");
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    criterioRep.setCriterioRepReporteNum(Integer.parseInt(criterioRep.getCriterioRepReporteSel().substring(0, criterioRep.getCriterioRepReporteSel().indexOf("."))));
                    if (desHabilitado.getGenericoDesHabilitado01().equals(Boolean.FALSE)) {
                        oMoneda = new CMoneda();
                        criterioRep.setCriterioRepMonedaNum(oMoneda.onMoneda_ObtenMonedaId(criterioRep.getCriterioRepMonedaNom()));
                        oMoneda = null;
                    }
                    destinoURL = "/scotiaFid/SGeneraReporteGenericoMasivo?";
                    destinoURL = destinoURL.concat(criterioRep.getCriterioRepReporteNum().toString());
                    destinoURL = destinoURL.concat("&").concat("0");
                    destinoURL = destinoURL.concat("&").concat(String.valueOf((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero")));
                    destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepReporteSel());
                    destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepFmto());
                    destinoURL = destinoURL.concat("&").concat(formatFecha(fecha.getGenericoFecha00()));
                    destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepAño()));
                    destinoURL = destinoURL.concat("&").concat(String.valueOf(criterioRep.getCriterioRepMes()));
                    if ((criterioRep.getCriterioRepFirma01() != null) && (!criterioRep.getCriterioRepFirma01().equals(new String()))) {
                        destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepFirma01().substring(0, criterioRep.getCriterioRepFirma01().indexOf(".")));
                    }
                    if ((criterioRep.getCriterioRepFirma02() != null) && (!criterioRep.getCriterioRepFirma02().equals(new String()))) {
                        destinoURL = destinoURL.concat("&").concat(criterioRep.getCriterioRepFirma02().substring(0, criterioRep.getCriterioRepFirma02().indexOf(".")));
                    }
                    FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                }
            } catch (IOException | NumberFormatException Err) {
                logger.error(Err.getMessage() + "onReporteGenera_GeneraMasivo");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onReporteGenera_Inicializa() {
            onInHabilitaFiltros();
            onInicializaCriterio();
        }
        //Funciones privadas

        private void onInHabilitaFiltros() {
            desHabilitado.setGenericoDesHabilitado00(Boolean.TRUE);//Contrato
            desHabilitado.setGenericoDesHabilitado01(Boolean.TRUE);//Moneda
            desHabilitado.setGenericoDesHabilitado02(Boolean.TRUE);//Fecha
            desHabilitado.setGenericoDesHabilitado03(Boolean.TRUE);//Del - Al
            desHabilitado.setGenericoDesHabilitado04(Boolean.TRUE);//Año
            desHabilitado.setGenericoDesHabilitado05(Boolean.TRUE);//Mes
            desHabilitado.setGenericoDesHabilitado06(Boolean.TRUE);//Cuenta Contable
            desHabilitado.setGenericoDesHabilitado07(Boolean.TRUE);//Folio Contable
            desHabilitado.setGenericoDesHabilitado08(Boolean.TRUE);//Cto. Inv.
            desHabilitado.setGenericoDesHabilitado09(Boolean.TRUE);//Rol Fid.
            desHabilitado.setGenericoDesHabilitado10(Boolean.TRUE);//Firma 1
            desHabilitado.setGenericoDesHabilitado11(Boolean.TRUE);//Firma 2
        }

        private void onInicializaCriterio() {
            try {
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                criterioRep.setCriterioRepFmto(null);
                criterioRep.setCriterioRepReporteSel(null);
                criterioRep.setCriterioRepContratoNom(null);
                criterioRep.setCriterioRepContratoNum(0);
                fideicomisoTxt = null;
                criterioRep.setCriterioRepContratoNomSub(null);
                criterioRep.setCriterioRepContratoNumSub(0);
                fecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                fecha.setGenericoFecha02(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                criterioRep.setCriterioRepMonedaNom(null);
                criterioRep.setCriterioRepMonedaNum(1);
                criterioRep.setCriterioRepMonedaNom(null);
                onReporteGenera_UpdateToCurrentDate();

                criterioRep.setCriterioRepReporteNum(null);
                criterioRep.setCriterioRepReporteSel(null);
                criterioRep.setCriterioRepCTAM(0);
                criterioRep.setCriterioRepSC1(0);
                criterioRep.setCriterioRepSC2(0);
                criterioRep.setCriterioRepSC3(0);
                criterioRep.setCriterioRepSC4(0);
                criterioRep.setCriterioRepCtoInv(Long.parseLong("0"));
                criterioRep.setCriterioBitUsuarioNum(usuarioNumero);
                criterioRep.setCriterioBitPantalla("Generación de Reportes");
                criterioRep.setCriterioBitTerminal(usuarioTerminal);
                criterioRep.setCriterioRepFmto(null);
                criterioRep.setCriterioRepFirma01(null);
                criterioRep.setCriterioRepFirma02(null);
                visiblePDF = true;
                visibleTXT = true;
                /// Fecha del sistema en campos Fecha, Año y Mes.
                //  String subtractedDate = getSubtractedDate((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema"), 1);
                String subtractedDate = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                String[] subtractedDateArray = subtractedDate.split("/");
                fecha.setGenericoFecha00(formatFechaParse(subtractedDate));
                criterioRep.setCriterioRepMes(Integer.parseInt(subtractedDateArray[1]));
                criterioRep.setCriterioRepAño(Integer.parseInt(subtractedDateArray[2]));
                //---

                criterioRep.setTxtcriterioRepReporteNum(null);
                criterioRep.setTxtcriterioRepContratoNum(null);
                criterioRep.setTxtcriterioRepContratoNumSub(null);
                criterioRep.setTxtcriterioRepMonedaNum(null);
                criterioRep.setTxtcriterioRepAño(criterioRep.getCriterioRepAño().toString());
                criterioRep.setTxtcriterioRepMes(criterioRep.getCriterioRepMes().toString());
                criterioRep.setTxtcriterioRepCTAM("0");
                criterioRep.setTxtcriterioRepSC1("0");
                criterioRep.setTxtcriterioRepSC2("0");
                criterioRep.setTxtcriterioRepSC3("0");
                criterioRep.setTxtcriterioRepSC4("0");
                criterioRep.setTxtcriterioRepCtoInv("0");
                criterioRep.setTxtcriterioRepFolioCont(null);
                listaContratoSub = null;
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onInicializaCriterio");
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
                    bSalida = true;
                }
            } catch (NumberFormatException Err) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El " + campo + "  debe ser un campo numérico...");
            }

            return bSalida;
        }

        private void onFinalizaObjetos() {
            if (oComunes != null) {
                oComunes = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oReportes != null) {
                oReportes = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBReportes() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    @PostConstruct
    public void onPost() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                if (peticion.getRequestURI().contains("reporteGeneracion")) {
                    formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    oReportes = new CReportes();
                    listaReporte = oReportes.onReporte_CargaListaReportes();
                    oReportes = null;

                    /*oContrato     = new CContrato();
                listaContrato = oContrato.onContrato_ObtenListadoContratoNumeroNombreActivo((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero"));
                oContrato     = null;*/
                    oMoneda = new CMoneda();
                    listaMoneda = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                    oMoneda = null;

                    /// Fecha del sistema en campos Fecha, Año y Mes.
                    //  String subtractedDate = getSubtractedDate((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema"), 1);
                    String subtractedDate = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema");
                    String[] subtractedDateArray = subtractedDate.split("/");
                    fecha.setGenericoFecha00(formatFechaParse(subtractedDate));
                    criterioRep.setCriterioRepMes(Integer.parseInt(subtractedDateArray[1]));
                    criterioRep.setCriterioRepAño(Integer.parseInt(subtractedDateArray[2]));
                    //---

                    fecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                    fecha.setGenericoFecha02(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                    setVisibleAnoMes("none");
                    //setVisiblePDF(Boolean.TRUE);
                    //setVisibleTXT(Boolean.TRUE);
                    visiblePDF = true;
                    visibleTXT = true;

                    desHabilitado.setGenericoDesHabilitado00(Boolean.TRUE);//Contrato
                    desHabilitado.setGenericoDesHabilitado01(Boolean.TRUE);//Moneda
                    desHabilitado.setGenericoDesHabilitado02(Boolean.TRUE);//Fecha
                    desHabilitado.setGenericoDesHabilitado03(Boolean.TRUE);//Del - Al
                    desHabilitado.setGenericoDesHabilitado04(Boolean.TRUE);//Año
                    desHabilitado.setGenericoDesHabilitado05(Boolean.TRUE);//Mes
                    desHabilitado.setGenericoDesHabilitado06(Boolean.TRUE);//Cuenta Contable
                    desHabilitado.setGenericoDesHabilitado07(Boolean.TRUE);//Folio Contable
                    desHabilitado.setGenericoDesHabilitado08(Boolean.TRUE);//Cto. Inv.
                    desHabilitado.setGenericoDesHabilitado09(Boolean.TRUE);//Rol Fid.
                    desHabilitado.setGenericoDesHabilitado10(Boolean.TRUE);//Firma 1
                    desHabilitado.setGenericoDesHabilitado11(Boolean.TRUE);//Firma 2
                    onReporteGenera_Inicializa();
                }
            }
        } catch (IOException | NumberFormatException | SQLException Err) {
            logger.error(Err.getMessage() + "onPost()");
        } finally {
            if (oContrato != null) {
                oContrato = null;
            }
            if (oComunes != null) {
                oComunes = null;
            }
            if (oReportes != null) {
                oReportes = null;
            }
        }
    }
//    private String getSubtractedDate(String date, Integer month) throws ParseException {
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd/MM/yyyy");
//        Date referenceDate = simpleFormatDate.parse(date);
//
//        Calendar calendar = Calendar.getInstance(); 
//        calendar.setTime(referenceDate); 
//        calendar.add(Calendar.MONTH, -month);
//
//        LocalDateTime monthSubtracted = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        return dateFormat.format(monthSubtracted);
//    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S 
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onReporteGenera_VerificaNumerico(AjaxBehaviorEvent event) {
        oRepGen = new reporteGeneracion();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("Anio") != null) {
            String anio = (String) componet.getAttributes().get("Anio");
            oRepGen.onReporteGenera_VerificaNumerico(anio);
        }

        if (componet.getAttributes().get("Mes") != null) {
            String mes = (String) componet.getAttributes().get("Mes");
            oRepGen.onReporteGenera_VerificaNumerico(mes);
        }

        if (componet.getAttributes().get("CuentaMayor") != null) {
            String cuentaMayor = (String) componet.getAttributes().get("CuentaMayor");
            oRepGen.onReporteGenera_VerificaNumerico(cuentaMayor);
        }

        if (componet.getAttributes().get("SC1") != null) {
            String sc1 = (String) componet.getAttributes().get("SC1");
            oRepGen.onReporteGenera_VerificaNumerico(sc1);
        }

        if (componet.getAttributes().get("SC2") != null) {
            String sc2 = (String) componet.getAttributes().get("SC2");
            oRepGen.onReporteGenera_VerificaNumerico(sc2);
        }

        if (componet.getAttributes().get("SC3") != null) {
            String sc3 = (String) componet.getAttributes().get("SC3");
            oRepGen.onReporteGenera_VerificaNumerico(sc3);
        }

        if (componet.getAttributes().get("SC4") != null) {
            String sc4 = (String) componet.getAttributes().get("SC4");
            oRepGen.onReporteGenera_VerificaNumerico(sc4);
        }

        if (componet.getAttributes().get("AX3") != null) {
            String ax3 = (String) componet.getAttributes().get("AX3");
            oRepGen.onReporteGenera_VerificaNumerico(ax3);
        }
        oRepGen = null;

    }

    public void onReporteGenera_VerificaFormatoReporte() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_VerificaFormatoReporte();
        oRepGen = null;
    }

    public void onReporteGenera_CargaListaSubCont() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_CargaListaSubCont();
        oRepGen = null;
    }

    public void onReporteGenera_ActualizaFechas() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_ActualizaFechas();
        oRepGen = null;
    }

    public void onReporteGenera_UpdateToCurrentDate() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_UpdateToCurrentDate();
        oRepGen = null;
    }

    public void onReporteGenera_Genera(String type) {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_Genera(type);
        oRepGen = null;
    }

    public void onReporteGenera_GeneraMasivo() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_GeneraMasivo();
        oRepGen = null;
    }

    public void onReporteGenera_Inicializa() {
        oRepGen = new reporteGeneracion();
        oRepGen.onReporteGenera_Inicializa();
        oRepGen = null;
    }

    public synchronized String formatFecha(Date fecha) {
        return formatoFecha.format(fecha);
    }

    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
        }
        return fechaSal;
    }

}
