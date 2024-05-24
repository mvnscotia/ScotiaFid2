/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : MBAdministracion.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controller
 * CREADO      : 20210611
 * MODIFICADO  : 20210611
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap; 
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.AdminControlesBean;
import scotiaFid.bean.GenericoAlineacionBean;
import scotiaFid.bean.GenericoConsultaBean;
import scotiaFid.bean.GenericoDesHabilitadoBean;
import scotiaFid.bean.GenericoTituloBean;
import scotiaFid.bean.GenericoVisibleBean;
import scotiaFid.bean.MensajeConfirmaBean;
import scotiaFid.dao.CControles;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CMoneda;

import org.primefaces.context.RequestContext;
import scotiaFid.bean.GeneralList;
import scotiaFid.bean.KeyValueBean;
import scotiaFid.util.Constantes;
import scotiaFid.util.LogsContext;
import scotiaFid.util.Normalizacion;

@ManagedBean(name = "mbAdmin")
@ViewScoped
public class MBAdministracion implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBAdministracion.class);
    private static final String URL_DESCARGA = "/scotiaFid/SArchivoDescarga?";
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean validacion;
    private String nombreObjeto;
    private String archivoNombre;
    private String archivoUbicacion;  
    private Integer usuarioNumero;
    private String usuarioNombre; 
    private Integer indx3;
    private String mensajeError;
    private Boolean controlVacio = true;
    private List< Map<String, KeyValueBean>> tableData;
    private List<KeyValueBean> tableHeaderNames;

    private String[][] arrTMP03 = new String[15][3];

    private String[] arropcional = new String[15];
    private String[] arrTipo = new String[15];
    String[] arrSQLSeccion = new String[6];

    private Calendar calendario;
    private FacesMessage mensaje;
    private HttpServletRequest peticionURL;

    private CControles oCtrls;
    private CComunes oComunes;
    private CMoneda oMoneda;

    private controles oControles;
    private generales oGrales;
    

    private SimpleDateFormat formatoFecha;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @ManagedProperty(value = "#{beanAdminControles}")
    private AdminControlesBean control;
    @ManagedProperty(value = "#{beanGenericoAlineacion}")
    private GenericoAlineacionBean geneAlinea;
    @ManagedProperty(value = "#{beanGenericoConsulta}")
    private GenericoConsultaBean geneCons;
    @ManagedProperty(value = "#{beanGenericoDesHabilitado}")
    private GenericoDesHabilitadoBean geneDes;
    @ManagedProperty(value = "#{beanGenericoTitulo}")
    private GenericoTituloBean geneTitulo;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean geneVisible;
    @ManagedProperty(value = "#{beanMensajeConfirmacion}")
    private MensajeConfirmaBean mensajeConfirma;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<GeneralList> listaGen00;
    private List<GeneralList> listaGen01;
    private List<GeneralList> listaGen02;
    private List<GeneralList> listaGen03;
    private List<GeneralList>listaGen04;
    private List<GeneralList> listaGen05;
    private List<GeneralList> listaGen06;
    private List<GeneralList> listaGen07;
    private List<GeneralList> listaGen08;
    private List<GeneralList> listaGen09;
    private List<GeneralList> listaGen10;
    private List<GeneralList> listaGen11;
    private List<GeneralList> listaGen12;
    private List<GeneralList> listaGen13;
    private List<GeneralList> listaGen14;
    private List<FacesMessage> mensajesDeError;    

    private Boolean coboInput00;
    private Boolean coboInput01;
    private Boolean coboInput02;
    private Boolean coboInput03;
    private Boolean coboInput04;
    private Boolean coboInput05;
    private Boolean coboInput06;
    private Boolean coboInput07;
    private Boolean coboInput08;
    private Boolean coboInput09;
    private Boolean coboInput10;
    private Boolean coboInput11;
    private Boolean coboInput12;
    private Boolean coboInput13;
    private Boolean coboInput14;
    
    private List<String> listaCtrls;
    private List<String> listaEjecutiv;
    private List<String> listaMoneda;
    private List<String> listaPlaza;
    private List<String> listaReporte;
    private List<AdminControlesBean> consultaCtrls;
    private List<AdminControlesBean> consultaCtrlsFiltro;
    private List<GenericoConsultaBean> consultaCtrlsRes;

    private AdminControlesBean seleccionaCtrl;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setControl(AdminControlesBean control) {
        this.control = control;
    }

    public void setGeneAlinea(GenericoAlineacionBean geneAlinea) {
        this.geneAlinea = geneAlinea;
    }

    public void setGeneCons(GenericoConsultaBean geneCons) {
        this.geneCons = geneCons;
    }

    public void setGeneDes(GenericoDesHabilitadoBean geneDes) {
        this.geneDes = geneDes;
    }

    public void setGeneTitulo(GenericoTituloBean geneTitulo) {
        this.geneTitulo = geneTitulo;
    }

    public void setGeneVisible(GenericoVisibleBean geneVisible) {
        this.geneVisible = geneVisible;
    }

    public void setMensajeConfirma(MensajeConfirmaBean mensajeConfirma) {
        this.mensajeConfirma = mensajeConfirma;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<String> getListaCtrls() {
        return listaCtrls;
    }

    public List<String> getListaEjecutiv() {
        return listaEjecutiv;
    }

    public List<String> getListaMoneda() {
        return listaMoneda;
    }

    public List<String> getListaPlaza() {
        return listaPlaza;
    }

    public List<String> getListaReporte() {
        return listaReporte;
    }

    public List<AdminControlesBean> getConsultaCtrls() {
        return consultaCtrls;
    }

    public List<AdminControlesBean> getConsultaCtrlsFiltro() {
        return consultaCtrlsFiltro;
    }

    public void setConsultaCtrlsFiltro(List<AdminControlesBean> consultaCtrlsFiltro) {
        this.consultaCtrlsFiltro = consultaCtrlsFiltro;
    }

    public List<GenericoConsultaBean> getConsultaCtrlsRes() {
        return consultaCtrlsRes;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public AdminControlesBean getSeleccionaCtrl() {
        return seleccionaCtrl;
    }

    public void setSeleccionaCtrl(AdminControlesBean seleccionaCtrl) {
        this.seleccionaCtrl = seleccionaCtrl;
    }

    public List< Map<String, KeyValueBean>> getTableData() {
        return tableData;
    }

    public void setTableData(List< Map<String, KeyValueBean>> tableData) {
        this.tableData = tableData;
    }

    public List<KeyValueBean> getTableHeaderNames() {
        return tableHeaderNames;
    }

    public void setTableHeaderNames(List<KeyValueBean> tableHeaderNames) {
        this.tableHeaderNames = tableHeaderNames;
    }


    public Boolean getCoboInput00() {
        return coboInput00;
    }

    public void setCoboInput00(Boolean coboInput00) {
        this.coboInput00 = coboInput00;
    }

    public Boolean getCoboInput01() {
        return coboInput01;
    }

    public void setCoboInput01(Boolean coboInput01) {
        this.coboInput01 = coboInput01;
    }

    public Boolean getCoboInput02() {
        return coboInput02;
    }

    public void setCoboInput02(Boolean coboInput02) {
        this.coboInput02 = coboInput02;
    }

    public Boolean getCoboInput03() {
        return coboInput03;
    }

    public void setCoboInput03(Boolean coboInput03) {
        this.coboInput03 = coboInput03;
    }

    public Boolean getCoboInput04() {
        return coboInput04;
    }

    public void setCoboInput04(Boolean coboInput04) {
        this.coboInput04 = coboInput04;
    }

    public Boolean getCoboInput05() {
        return coboInput05;
    }

    public void setCoboInput05(Boolean coboInput05) {
        this.coboInput05 = coboInput05;
    }

    public Boolean getCoboInput06() {
        return coboInput06;
    }

    public void setCoboInput06(Boolean coboInput06) {
        this.coboInput06 = coboInput06;
    }

    public Boolean getCoboInput07() {
        return coboInput07;
    }

    public void setCoboInput07(Boolean coboInput07) {
        this.coboInput07 = coboInput07;
    }

    public Boolean getCoboInput08() {
        return coboInput08;
    }

    public void setCoboInput08(Boolean coboInput08) {
        this.coboInput08 = coboInput08;
    }

    public Boolean getCoboInput09() {
        return coboInput09;
    }

    public void setCoboInput09(Boolean coboInput09) {
        this.coboInput09 = coboInput09;
    }

    public Boolean getCoboInput10() {
        return coboInput10;
    }

    public void setCoboInput10(Boolean coboInput10) {
        this.coboInput10 = coboInput10;
    }

    public Boolean getCoboInput11() {
        return coboInput11;
    }

    public void setCoboInput11(Boolean coboInput11) {
        this.coboInput11 = coboInput11;
    }

    public Boolean getCoboInput12() {
        return coboInput12;
    }

    public void setCoboInput12(Boolean coboInput12) {
        this.coboInput12 = coboInput12;
    }

    public Boolean getCoboInput13() {
        return coboInput13;
    }

    public void setCoboInput13(Boolean coboInput13) {
        this.coboInput13 = coboInput13;
    }

    public Boolean getCoboInput14() {
        return coboInput14;
    }

    public void setCoboInput14(Boolean coboInput14) {
        this.coboInput14 = coboInput14;
    }

    public List<GeneralList> getListaGen00() {
        return listaGen00;
    }

    public void setListaGen00(List<GeneralList> listaGen00) {
        this.listaGen00 = listaGen00;
    }

    public List<GeneralList> getListaGen01() {
        return listaGen01;
    }

    public void setListaGen01(List<GeneralList> listaGen01) {
        this.listaGen01 = listaGen01;
    }

    public List<GeneralList> getListaGen02() {
        return listaGen02;
    }

    public void setListaGen02(List<GeneralList> listaGen02) {
        this.listaGen02 = listaGen02;
    }

    public List<GeneralList> getListaGen03() {
        return listaGen03;
    }

    public void setListaGen03(List<GeneralList> listaGen03) {
        this.listaGen03 = listaGen03;
    }

    public List<GeneralList> getListaGen04() {
        return listaGen04;
    }

    public void setListaGen04(List<GeneralList> listaGen04) {
        this.listaGen04 = listaGen04;
    }

    public List<GeneralList> getListaGen05() {
        return listaGen05;
    }

    public void setListaGen05(List<GeneralList> listaGen05) {
        this.listaGen05 = listaGen05;
    }

    public List<GeneralList> getListaGen06() {
        return listaGen06;
    }

    public void setListaGen06(List<GeneralList> listaGen06) {
        this.listaGen06 = listaGen06;
    }

    public List<GeneralList> getListaGen07() {
        return listaGen07;
    }

    public void setListaGen07(List<GeneralList> listaGen07) {
        this.listaGen07 = listaGen07;
    }

    public List<GeneralList> getListaGen08() {
        return listaGen08;
    }

    public void setListaGen08(List<GeneralList> listaGen08) {
        this.listaGen08 = listaGen08;
    }

    public List<GeneralList> getListaGen09() {
        return listaGen09;
    }

    public void setListaGen09(List<GeneralList> listaGen09) {
        this.listaGen09 = listaGen09;
    }

    public List<GeneralList> getListaGen10() {
        return listaGen10;
    }

    public void setListaGen10(List<GeneralList> listaGen10) {
        this.listaGen10 = listaGen10;
    }

    public List<GeneralList> getListaGen11() {
        return listaGen11;
    }

    public void setListaGen11(List<GeneralList> listaGen11) {
        this.listaGen11 = listaGen11;
    }

    public List<GeneralList> getListaGen13() {
        return listaGen13;
    }

    public void setListaGen13(List<GeneralList> listaGen13) {
        this.listaGen13 = listaGen13;
    }

    public List<GeneralList> getListaGen14() {
        return listaGen14;
    }

    public void setListaGen14(List<GeneralList> listaGen14) {
        this.listaGen14 = listaGen14;
    }

    public List<GeneralList> getListaGen12() {
        return listaGen12;
    }

    public void setListaGen12(List<GeneralList> listaGen12) {
        this.listaGen12 = listaGen12;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    class generales {

        public generales() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbAdministracion.generales.";
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            validacion = Boolean.TRUE;
        }

        public void onGenerales_AplicaCancelacion() {
            //          RequestContext.getCurrentInstance().execute("dlgCtrlMtto.hide();");
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
        }

        public void onGenerales_AplicaConfirmacion() {
            try {
                if (mensajeConfirma.getMensajeConfirmaOrigen().equals("CONTROLES")) {
                    if (mensajeConfirma.getMensajeConfirmacionAccion().equals("Eliminar")) {
                        control.setControlOperacionTipo("ELIMINAR");
                        oCtrls = new CControles();
                        if (oCtrls.onControles_Adminsitra(control).equals(Boolean.TRUE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            consultaCtrls = oCtrls.onControles_Consulta();
                            seleccionaCtrl = null;
                            RequestContext.getCurrentInstance().execute("dtControlwidgetVar.filter();");
                            RequestContext.getCurrentInstance().execute("dlgCtrlMtto.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oCtrls.getMensajeError());
                        }
                        oCtrls = null;
                    }
                }
                RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
            } catch (SQLException Err) {
                logger.error( nombreObjeto + "onGenerales_AplicaConfirmacion()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oCtrls != null) {
                oCtrls = null;
            }
        }
    }

    class controles {

        public controles() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbAdministracion.controles.";
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            validacion = Boolean.TRUE;
        }
        public void onControles_LimpiaVentana(){
            geneCons.setGenericoCampo00(null);
            geneCons.setGenericoCampo01(null);
            geneCons.setGenericoCampo02(null);
            geneCons.setGenericoCampo03(null);
            geneCons.setGenericoCampo04(null);
            geneCons.setGenericoCampo05(null);
            geneCons.setGenericoCampo06(null);
            geneCons.setGenericoCampo07(null);
            geneCons.setGenericoCampo08(null);
            geneCons.setGenericoCampo09(null);
            geneCons.setGenericoCampo10(null);
            geneCons.setGenericoCampo11(null);
            geneCons.setGenericoCampo12(null);
            geneCons.setGenericoCampo13(null);
            geneCons.setGenericoCampo14(null);
            setTableData(null);
            geneTitulo.setGenericoTitulo15(null);
            tableHeaderNames = null;
            controlVacio = true;

        }

        public void onControles_CtrlEjecutaDespliegaVentana() {
            String[] arrTMP01 = new String[5];
            String[] arrTMP02 = new String[10];
            String[] arrT = new String[10];
            Boolean bAsignoTitulo = Boolean.FALSE;
            String cadenaTMP00 = new String();
            setTableHeaderNames(null);
            setTableData(null);

            try {
                arrTMP03 = new String[15][3];

                geneDes.setGenericoDesHabilitado15(Boolean.TRUE);
                geneVisible.setGenericoVisible15("visible;");
                if (seleccionaCtrl != null) {
                    onInicializaEjecucion();
                    oCtrls = new CControles();
                    cadenaTMP00 = oCtrls.onControles_ObtenControl(seleccionaCtrl.getControlSec());
                    oCtrls = null;
                    arrSQLSeccion = cadenaTMP00.split(";");
                    //05 Titulos del filtro
                    if (arrSQLSeccion[5] != null && !arrSQLSeccion[5].trim().equals(new String())) {
                        arrTMP01 = arrSQLSeccion[5].trim().split(",");
                        for (int itemTitulo = 0; itemTitulo <= arrTMP01.length - 1; itemTitulo++) {
                            bAsignoTitulo = Boolean.FALSE;
                            arrT = arrTMP01[itemTitulo].split(" ");
                            arrTMP02[0] = arrTMP01[itemTitulo].split(" ")[0];
                            arrTMP02[1] = arrTMP01[itemTitulo].split(" ")[1];

                            if (arrTMP01[itemTitulo].split(" ").length > 2) {
                                arrTMP02[2] = arrTMP01[itemTitulo].split(" ")[2];
                            } else {
                                arrTMP02[2] = "O";
                            }

                            if (arrTMP02[1].equals("A?O")) {
                                arrTMP02[1] = arrTMP02[1] = "AÑO";
                            }

                            if (arrTMP02[1].equals("NUM_ANIO")) {
                                arrTMP02[1] = arrTMP02[1] = "NUM_AÑO";
                            }
                            if ((geneVisible.getGenericoVisible00().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible00("visible;");
                                geneTitulo.setGenericoTitulo00(arrTMP02[1].trim());
                                arropcional[0] = arrTMP02[2];
                                arrTipo[0] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen00(carga_Catalogos(geneTitulo.getGenericoTitulo00()));
                                if(getListaGen00().isEmpty()){
                                    coboInput00 = Boolean.FALSE;
                                }else{
                                    coboInput00 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible01().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible01("visible;");
                                geneTitulo.setGenericoTitulo01(arrTMP02[1].trim());
                                arropcional[1] = arrTMP02[2];
                                arrTipo[1] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen01(carga_Catalogos(geneTitulo.getGenericoTitulo01()));
                                if(getListaGen01().isEmpty()){
                                    coboInput01 = Boolean.FALSE;
                                }else{
                                    coboInput01 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible02().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible02("visible;");
                                geneTitulo.setGenericoTitulo02(arrTMP02[1].trim());
                                arropcional[2] = arrTMP02[2];
                                arrTipo[2] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen02(carga_Catalogos(geneTitulo.getGenericoTitulo02()));
                                if(getListaGen02().isEmpty()){
                                    coboInput02 = Boolean.FALSE;
                                }else{
                                    coboInput02 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible03().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible03("visible;");
                                geneTitulo.setGenericoTitulo03(arrTMP02[1].trim());
                                arropcional[3] = arrTMP02[2];
                                arrTipo[3] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen03(carga_Catalogos(geneTitulo.getGenericoTitulo03()));
                                if(getListaGen03().isEmpty()){
                                    coboInput03 = Boolean.FALSE;
                                }else{
                                    coboInput03 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible04().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible04("visible;");
                                geneTitulo.setGenericoTitulo04(arrTMP02[1].trim());
                                arropcional[4] = arrTMP02[2];
                                arrTipo[4] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen03(carga_Catalogos(geneTitulo.getGenericoTitulo03()));
                                if(getListaGen03().isEmpty()){
                                    coboInput03 = Boolean.FALSE;
                                }else{
                                    coboInput03 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible05().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible05("visible;");
                                geneTitulo.setGenericoTitulo05(arrTMP02[1].trim());
                                arropcional[5] = arrTMP02[2];
                                arrTipo[5] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen05(carga_Catalogos(geneTitulo.getGenericoTitulo05()));
                                if(getListaGen05().isEmpty()){
                                    coboInput05 = Boolean.FALSE;
                                }else{
                                    coboInput05 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible06().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible06("visible;");
                                geneTitulo.setGenericoTitulo06(arrTMP02[1].trim());
                                arropcional[6] = arrTMP02[2];
                                arrTipo[6] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen06(carga_Catalogos(geneTitulo.getGenericoTitulo06()));
                                if(getListaGen06().isEmpty()){
                                    coboInput06 = Boolean.FALSE;
                                }else{
                                    coboInput06 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible07().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible07("visible;");
                                geneTitulo.setGenericoTitulo07(arrTMP02[1].trim());
                                arropcional[7] = arrTMP02[2];
                                arrTipo[7] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen07(carga_Catalogos(geneTitulo.getGenericoTitulo07()));
                                if(getListaGen07().isEmpty()){
                                    coboInput07 = Boolean.FALSE;
                                }else{
                                    coboInput07 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible08().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible08("visible;");
                                geneTitulo.setGenericoTitulo08(arrTMP02[1].trim());
                                arropcional[8] = arrTMP02[2];
                                arrTipo[8] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen08(carga_Catalogos(geneTitulo.getGenericoTitulo08()));
                                if(getListaGen08().isEmpty()){
                                    coboInput08 = Boolean.FALSE;
                                }else{
                                    coboInput08 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible09().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible09("visible;");
                                geneTitulo.setGenericoTitulo09(arrTMP02[1].trim());
                                arropcional[9] = arrTMP02[2];
                                arrTipo[9] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen09(carga_Catalogos(geneTitulo.getGenericoTitulo09()));
                                if(getListaGen09().isEmpty()){
                                    coboInput09 = Boolean.FALSE;
                                }else{
                                    coboInput09 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible10().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible10("visible;");
                                geneTitulo.setGenericoTitulo10(arrTMP02[1].trim());
                                arropcional[10] = arrTMP02[2];
                                arrTipo[10] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen10(carga_Catalogos(geneTitulo.getGenericoTitulo10()));
                                if(getListaGen10().isEmpty()){
                                    coboInput10 = Boolean.FALSE;
                                }else{
                                    coboInput10 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible11().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible11("visible;");
                                geneTitulo.setGenericoTitulo11(arrTMP02[1].trim());
                                arropcional[11] = arrTMP02[2];
                                arrTipo[11] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen11(carga_Catalogos(geneTitulo.getGenericoTitulo11()));
                                if(getListaGen11().isEmpty()){
                                    coboInput11 = Boolean.FALSE;
                                }else{
                                    coboInput11 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible12().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible12("visible;");
                                geneTitulo.setGenericoTitulo12(arrTMP02[1].trim());
                                arropcional[12] = arrTMP02[2];
                                arrTipo[12] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen12(carga_Catalogos(geneTitulo.getGenericoTitulo12()));
                                if(getListaGen12().isEmpty()){
                                    coboInput12 = Boolean.FALSE;
                                }else{
                                    coboInput12 = Boolean.TRUE;
                                }
                            } 
                            if ((geneVisible.getGenericoVisible13().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible13("visible;");
                                geneTitulo.setGenericoTitulo13(arrTMP02[1].trim());
                                arropcional[13] = arrTMP02[2];
                                arrTipo[13] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                setListaGen13(carga_Catalogos(geneTitulo.getGenericoTitulo13()));
                                if(getListaGen13().isEmpty()){
                                    coboInput13 = Boolean.FALSE;
                                }else{
                                    coboInput13 = Boolean.TRUE;
                                }
                            }
                            if ((geneVisible.getGenericoVisible14().equals("hidden;")) && (bAsignoTitulo.equals(Boolean.FALSE))) {
                                geneVisible.setGenericoVisible14("visible;");
                                geneTitulo.setGenericoTitulo14(arrTMP02[1].trim());
                                arropcional[14] = arrTMP02[2];
                                arrTipo[14] = arrTMP02[0];
                                bAsignoTitulo = Boolean.TRUE;
                                 setListaGen14(carga_Catalogos(geneTitulo.getGenericoTitulo14()));
                                if(getListaGen14().isEmpty()){
                                    coboInput14 = Boolean.FALSE;
                                }else{
                                    coboInput14 = Boolean.TRUE;
                                }
                           }
                        }
                    }
                    geneDes.setGenericoDesHabilitado15(Boolean.FALSE);
                    geneVisible.setGenericoVisible15("hidden;");
                    RequestContext.getCurrentInstance().execute("dlgCtrlEjecuta.show();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe de seleccionar un control");
                }
            } catch (SQLException Err) {
                logger.error(nombreObjeto + "onControles_CtrlEjecutaDespliegaVentana()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        public List<GeneralList> carga_Catalogos(String tituloCampo) {
            List<GeneralList> listSalida = new ArrayList<>();
            List<String> lista;
            GeneralList cat = new GeneralList();
            try {
                switch (tituloCampo.toUpperCase(Locale.ENGLISH)) {
                    case "MONEDA":
                        oComunes = new CComunes();
                        lista = oComunes.onComunes_ObtenListadoContenidoCampo("mon_nom_moneda", "Monedas", "WHERE mon_cve_st_moneda = 'ACTIVO'");
                        for(String mon:lista){
                           cat = new GeneralList();
                           cat.setClave(mon);
                           cat.setDescripcion(mon);
                           listSalida.add(cat);
                        }
                        oComunes = null;
                        break; 
                    case "MES":
                        cat = new GeneralList();
                        cat.setClave("01"); cat.setDescripcion("Enero");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("02"); cat.setDescripcion("Febrero");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("03"); cat.setDescripcion("Marzo");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("04"); cat.setDescripcion("Abril");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("05"); cat.setDescripcion("Mayo");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("06"); cat.setDescripcion("Junio");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("07"); cat.setDescripcion("Julio");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("08"); cat.setDescripcion("Agosto");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("09"); cat.setDescripcion("Septiembre");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("10"); cat.setDescripcion("Octubre");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("11"); cat.setDescripcion("Noviembre");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("12"); cat.setDescripcion("Diciembre");
                        listSalida.add(cat);
                        break;
                    case "ESTATUS":
                        cat = new GeneralList();
                        cat.setClave("ACTIVO"); cat.setDescripcion("ACTIVO");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("CANCELADO"); cat.setDescripcion("CANCELADO");
                        listSalida.add(cat);
                        break;
                    case "STATUS":
                        cat = new GeneralList();
                        cat.setClave("PENDIENTE"); cat.setDescripcion("PENDIENTE");
                        listSalida.add(cat);
                        cat = new GeneralList();
                        cat.setClave("ATENDIDO"); cat.setDescripcion("ATENDIDO");
                        listSalida.add(cat);
                        break;
                    case "PLAZA":
                        oComunes = new CComunes();
                        listSalida = oComunes.onComunes_Plazas();
                        oComunes = null;
                        break; 
                }
            } catch (SQLException e) {
                logger.error(nombreObjeto + "onControles_CtrlEjecuta()");
            }
            return listSalida;
        }
    
        public void onControles_CtrlEjecuta() {
            try {
                setTableData(null);
                tableHeaderNames = null;
                onControles_ValidaEntrada(); 
                if (mensajesDeError.isEmpty()) {
                    oCtrls = new CControles();
                    controlVacio = true;
                    if (oCtrls.onControles_Ejecuta(arrTMP03, seleccionaCtrl.getControlSec(), usuarioNumero).equals(Boolean.TRUE) && oCtrls.getControlVacio().equals(Boolean.FALSE)) {
                        controlVacio = oCtrls.getControlVacio();
                        if (oCtrls.getControlDesbordado().equals(Boolean.TRUE)) {
                            geneTitulo.setGenericoTitulo15("Aviso, El control debe ser Exportado sobrepasa capacidad de WEB");
                            geneVisible.setGenericoVisible15("visible;");

                        } else {
                            setTableHeaderNames(oCtrls.getTableHeaderNames());
                            setTableData(oCtrls.getTableData());
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            geneVisible.setGenericoVisible15("hidden;");
                        }
                    } else {
                        if (!oCtrls.getMensajeError().equals(new String())) {
                            geneTitulo.setGenericoTitulo15("Error, el control no esta definido de manera correcta");
                            geneVisible.setGenericoVisible15("visible;");
                        } else {
                            if ((oCtrls.getControlVacio().equals(Boolean.TRUE))) {
                                geneTitulo.setGenericoTitulo15("No existe información para el control seleccionado");
                                geneVisible.setGenericoVisible15("visible;");
                            }
                        }
                    }
                    
                    oCtrls = null;
                }
            } catch (SQLException | IOException Err) { 
                logger.error(nombreObjeto + "onControles_CtrlEjecuta()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        public void onControles_Cancelar() {
            tableHeaderNames = null;
            tableData = null;
            RequestContext.getCurrentInstance().execute("dlgCtrlEjecuta.hide();");

        }
        public void onControles_Descarga() {
            try {
                if (tableHeaderNames == null && controlVacio){
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El control no ha sido Ejecutado");
                }   
                if (validacion.equals(Boolean.TRUE)) {
                    calendario = Calendar.getInstance();
                    calendario.setTime(formatoFecha.parse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                    formatoFecha = new SimpleDateFormat("yyyyMMdd");
                    archivoNombre = formatoFecha.format(calendario.getTime()).concat("_").concat(usuarioNumero.toString()).concat("_").
                            concat(seleccionaCtrl.getControlNombre().substring(seleccionaCtrl.getControlNombre().indexOf(" ")).
                            trim().replace(",", new String()).trim().replace(".", new String()).trim().replace(";", new String()).
                            trim().replace("-", new String()).trim().replace("(", new String()).trim().replace(")", new String()).trim().replace(" ", "_")).trim().concat(".txt");
                    archivoNombre = archivoNombre.replace("Á", "A");
                    archivoNombre = archivoNombre.replace("É", "E");
                    archivoNombre = archivoNombre.replace("Í", "I");
                    archivoNombre = archivoNombre.replace("Ó", "O");
                    archivoNombre = archivoNombre.replace("Ú", "U");
                    archivoUbicacion = Constantes.RUTA_TEMP; 
                    if (archivoUbicacion != null) {
                        archivoUbicacion = archivoUbicacion.concat("/");
                        oCtrls = new CControles();
                        if (oCtrls.onControles_Descarga(arrTMP03, archivoUbicacion.concat(archivoNombre), seleccionaCtrl.getControlSec(), usuarioNumero).equals(Boolean.TRUE) && oCtrls.getControlVacio().equals(Boolean.FALSE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            geneVisible.setGenericoVisible15("hidden;");
                            StringBuilder destinoURLDescarga = new StringBuilder(URL_DESCARGA);
                            destinoURLDescarga.append(Normalizacion.parse(archivoNombre));
                            if(destinoURLDescarga.length() > 0){
                                String URLFinal = destinoURLDescarga.toString();
                                FacesContext.getCurrentInstance().getExternalContext().redirect(URLFinal);
                            }
    //                        RequestContext.getCurrentInstance().execute("dlgCtrlEjecuta.hide();");
                        } else {
                            if ((oCtrls.getControlVacio().equals(Boolean.TRUE)) && (!oCtrls.getMensajeError().contains("onControles_Ejecuta()"))) {
                                geneTitulo.setGenericoTitulo15("No existe información para el control seleccionado");
                                geneVisible.setGenericoVisible15("visible;");
                            } else {
                                geneTitulo.setGenericoTitulo15("Error, el control no esta definido de manera correcta");
                                geneVisible.setGenericoVisible15("visible;");
                            }
                        }
                    } 
                    oCtrls = null;
                }
            } catch (IOException Err) {
                logger.error("IOException onControles_CtrlEjecuta()");
            } catch (SQLException Err ) {
                logger.error("SQLException onControles_CtrlEjecuta()");
            } catch (ParseException Err) {
                logger.error("ParseException onControles_CtrlEjecuta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        private void onControles_ValidaEntrada() {
            try {
                mensajesDeError = new ArrayList<>();
                indx3 = 0;
                if (geneVisible.getGenericoVisible00().equals("visible;")) {
                    if ((geneCons.getGenericoCampo00() == null || geneCons.getGenericoCampo00().equals(new String())) && !arropcional[0].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo00()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo00(), arrTipo[0], geneCons.getGenericoCampo00());
                    }
                }
                if (geneVisible.getGenericoVisible01().equals("visible;")) {
                    if ((geneCons.getGenericoCampo01() == null || geneCons.getGenericoCampo01().equals(new String())) && !arropcional[1].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo01()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo01(), arrTipo[1], geneCons.getGenericoCampo01());
                    }
                }
                if (geneVisible.getGenericoVisible02().equals("visible;")) {
                    if ((geneCons.getGenericoCampo02() == null || geneCons.getGenericoCampo02().equals(new String())) && !arropcional[2].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo02()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo02(), arrTipo[2], geneCons.getGenericoCampo02());
                    }
                }
                if (geneVisible.getGenericoVisible03().equals("visible;")) {
                    if ((geneCons.getGenericoCampo03() == null || geneCons.getGenericoCampo03().equals(new String())) && !arropcional[3].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo03()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo03(), arrTipo[3], geneCons.getGenericoCampo03());
                    }
                }
                if (geneVisible.getGenericoVisible04().equals("visible;")) {
                    if ((geneCons.getGenericoCampo04() == null || geneCons.getGenericoCampo04().equals(new String())) && !arropcional[4].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo04()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo04(), arrTipo[4], geneCons.getGenericoCampo04());
                    }
                }
                if (geneVisible.getGenericoVisible05().equals("visible;")) {
                    if ((geneCons.getGenericoCampo05() == null || geneCons.getGenericoCampo05().equals(new String())) && !arropcional[5].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo05()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo05(), arrTipo[5], geneCons.getGenericoCampo05());
                    }
                }
                if (geneVisible.getGenericoVisible06().equals("visible;")) {
                    if ((geneCons.getGenericoCampo06() == null || geneCons.getGenericoCampo06().equals(new String())) && !arropcional[6].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo06()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo06(), arrTipo[6], geneCons.getGenericoCampo06());
                    }
                }
                if (geneVisible.getGenericoVisible07().equals("visible;")) {
                    if ((geneCons.getGenericoCampo07() == null || geneCons.getGenericoCampo07().equals(new String())) && !arropcional[7].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo07()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo07(), arrTipo[7], geneCons.getGenericoCampo07());
                    }
                }
                if (geneVisible.getGenericoVisible08().equals("visible;")) {
                    if ((geneCons.getGenericoCampo08() == null || geneCons.getGenericoCampo08().equals(new String())) && !arropcional[8].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo08()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo08(), arrTipo[8], geneCons.getGenericoCampo08());
                    }
                }
                if (geneVisible.getGenericoVisible09().equals("visible;")) {
                    if ((geneCons.getGenericoCampo09() == null || geneCons.getGenericoCampo09().equals(new String())) && !arropcional[9].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo09()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo09(), arrTipo[9], geneCons.getGenericoCampo09());
                    }
                }
                if (geneVisible.getGenericoVisible10().equals("visible;")) {
                    if ((geneCons.getGenericoCampo10() == null || geneCons.getGenericoCampo10().equals(new String())) && !arropcional[10].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo10()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo10(), arrTipo[10], geneCons.getGenericoCampo10());
                    }
                }
                if (geneVisible.getGenericoVisible11().equals("visible;")) {
                    if ((geneCons.getGenericoCampo11() == null || geneCons.getGenericoCampo11().equals(new String())) && !arropcional[11].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo11()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo11(), arrTipo[11], geneCons.getGenericoCampo11());
                    }
                }
                if (geneVisible.getGenericoVisible12().equals("visible;")) {
                    if ((geneCons.getGenericoCampo12() == null || geneCons.getGenericoCampo12().equals(new String())) && !arropcional[12].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo12()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo12(), arrTipo[12], geneCons.getGenericoCampo12());
                    }
                }
                if (geneVisible.getGenericoVisible13().equals("visible;")) {
                    if ((geneCons.getGenericoCampo13() == null || geneCons.getGenericoCampo13().equals(new String())) && !arropcional[13].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo13()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo13(), arrTipo[13], geneCons.getGenericoCampo13());
                    }
                }
                if (geneVisible.getGenericoVisible14().equals("visible;")) {
                    if ((geneCons.getGenericoCampo14() == null || geneCons.getGenericoCampo14().equals(new String())) && !arropcional[15].equals("O")) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo14()).concat("  no puede estar vacío...")));
                    } else {
                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo14(), arrTipo[14], geneCons.getGenericoCampo14());
                    }
                }
//                if (geneVisible.getGenericoVisible15().equals("visible;")) {
//                    if ((geneCons.getGenericoCampo15() == null || geneCons.getGenericoCampo15().equals(new String())) && !arropcional[15].equals("O")) {
//                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(geneTitulo.getGenericoTitulo14()).concat("  no puede estar vacío...")));
//                    } else {
//                        onAsignaValorFiltro(geneTitulo.getGenericoTitulo15(), arrTipo[15], geneCons.getGenericoCampo15());
//                    }
//                }
            } catch (SQLException Err) {
                logger.error(nombreObjeto + "onControles_ValidaEntrada()");
            } 
        }

        private void onAsignaValorFiltro(String valorTitulo, String tipoCampo, String valorCampo) throws SQLException {
            Boolean formatoCorrecto = Boolean.TRUE;
            if ((valorTitulo.toUpperCase(Locale.ENGLISH).equals("MONEDA"))) {
                oMoneda = new CMoneda();
                valorCampo = String.valueOf(oMoneda.onMoneda_ObtenMonedaId(valorCampo));
                oMoneda = null;
            }
            if (valorTitulo.toUpperCase().contains("FECHA")) {
                if ((valorCampo != null && !valorCampo.equals(new String()))) {
                    if (onValidaInformacionCampo(valorCampo, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                            + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                            + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                            + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
                    ).equals(Boolean.FALSE)) {
                        formatoCorrecto = Boolean.FALSE;
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(valorTitulo).concat("  debe ser un campo con fecha correcta")));
                    } else {
                        oComunes = new CComunes();
                        Date fecha = new Date(formatFechaParse(valorCampo).getTime());
                        Calendar calendarFecha = Calendar.getInstance();
                        calendarFecha.setTime(fecha);
                        if (oComunes.onCommunes_BuscaDiasFeriados(fecha) || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            formatoCorrecto = Boolean.FALSE;
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "** Fecha invalida **"));
                        }
                        oComunes = null;

                    }
                }
            } else {

                switch (tipoCampo.substring(0, 1)) {
                    case "A":
                        if ((valorCampo != null && !valorCampo.equals(new String())) && onValidaInformacionCampo(valorCampo, "^[a-zA-Z0-9Ññ ]{1,}$").equals(Boolean.FALSE)) {
                            formatoCorrecto = Boolean.FALSE;
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  ".concat(valorTitulo).concat("  contiene caracteres no validos...")));
                        }
                        break;
                    case "N":
                        if ((valorCampo != null && !valorCampo.equals(new String())) && onValidaInformacionCampo(valorCampo, "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                            formatoCorrecto = Boolean.FALSE;
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(valorTitulo).concat("  debe ser un campo numérico...")));
                        }
                        break;
                case "S":
                    if ((valorCampo != null && !valorCampo.equals(new String()))) {
                        if (onValidaInformacionCampo(valorCampo, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                                + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                                + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                                + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
                        ).equals(Boolean.FALSE)) {
                            formatoCorrecto = Boolean.FALSE;
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(valorTitulo).concat("  debe ser un campo con fecha correcta")));
                        } else {
                            oComunes = new CComunes();
                            Date fecha = new Date(formatFechaParse(valorCampo).getTime());
                            Calendar calendarFecha = Calendar.getInstance();
                                        calendarFecha.setTime(fecha);
                            if (oComunes.onCommunes_BuscaDiasFeriados(fecha)|| calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                formatoCorrecto = Boolean.FALSE;
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "** Fecha invalida **"));
                            }
                            oComunes = null;

                        }
                    }
                    break;
                }
            }
            if (formatoCorrecto) {
                arrTMP03[indx3][0] = valorTitulo;
                arrTMP03[indx3][1] = tipoCampo;
                if (valorCampo != null && !valorCampo.equals(new String())) {
                    arrTMP03[indx3][2] = valorCampo;
                } else {
                    arrTMP03[indx3][2] = new String();
                }
            }
            indx3++;
        }

        private Boolean onValidaInformacionCampo(String campoValor, String campoPatron) {
            Pattern patron = Pattern.compile(campoPatron);
            Matcher matcher = patron.matcher(campoValor);
            return matcher.matches();
        }

        public void onControles_RegistroNuevo() {
            try {
                onInicializaCtrl();
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                RequestContext.getCurrentInstance().execute("dlgCtrlMtto.show();");
            } catch (ParseException Err) {
                logger.error(nombreObjeto + "onControles_RegistroNuevo()");
            }
        }

        public void onControles_RegistroModifica() {
            geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
            geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
            control.setControlOperacionTipo("MODIFICAR");
        }

        public void onControles_RegistroAplica() {
            mensajesDeError = new ArrayList<>();
            try {
                if (((control.getControlStatus() == null) || (control.getControlStatus().equals(new String()))) ) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Status no puede estar vacío..."));
                }
                if (((control.getControlNombre() == null) || (control.getControlNombre().equals(new String()))) ) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Nombre no puede estar vacío..."));
                }
                if (((control.getControlTexto() == null) || (control.getControlTexto().equals(new String()))) ) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Control no puede estar vacío..."));
                }
                if (validacion.equals(Boolean.TRUE)) {
                    control.setControlTexto(control.getControlNombre().trim().concat(";").concat(control.getControlTexto().trim()));
                    oCtrls = new CControles();
                    if (oCtrls.onControles_Adminsitra(control).equals(Boolean.TRUE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
                        consultaCtrls = oCtrls.onControles_Consulta();
                        seleccionaCtrl = null; 
//                        consultaCtrlsFiltro = null;
                        RequestContext.getCurrentInstance().execute("dlgCtrlMtto.hide();");
                        RequestContext.getCurrentInstance().execute("dtControlwidgetVar.filter();");
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oCtrls.getMensajeError()));
                    }
                    oCtrls = null;
                }
            } catch (SQLException Err) {
                logger.error(nombreObjeto + "onControles_RegistroAplica()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onControles_RegistroElimina() {
            mensajeConfirma.setMensajeConfirmaUsuario(usuarioNombre);
            mensajeConfirma.setMensajeConfirmaMensaje1("¿Confirma la eliminación del registro actual?");
            mensajeConfirma.setMensajeConfirmaOrigen("CONTROLES");
            mensajeConfirma.setMensajeConfirmacionAccion("Eliminar");
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
        }

        public void onControles_RegistroSelecciona() {
            try {
                if (seleccionaCtrl != null) {
                    onInicializaCtrl();
                    control.setControlSec(seleccionaCtrl.getControlSec());
                    control.setControlNombre(seleccionaCtrl.getControlNombre());
                    control.setControlTexto(seleccionaCtrl.getControlTexto());
                    control.setControlStatus(seleccionaCtrl.getControlStatus());
                    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                    RequestContext.getCurrentInstance().execute("dlgCtrlMtto.show();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe de seleccionar un control");
                }
            } catch (ParseException Err) {
                logger.error(nombreObjeto + "onControles_RegistroSelecciona()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
        //Funciones privadas

        private void onInicializaCtrl() throws ParseException {
            control.setControlTipo("CONTROL");
            control.setControlNombre(null);
            control.setControlTexto(null);
            control.setControlSec(null);
            control.setControlFechaReg(new java.sql.Date(formatoFecha.parse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            control.setControlFechaMod(new java.sql.Date(formatoFecha.parse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            control.setControlStatus("ACTIVO");
            control.setControlOperacionTipo("REGISTRO");
        }

        private void onInicializaEjecucion() {
            tableHeaderNames = null;
            controlVacio = true;
            //Etiquetas  
            geneTitulo.setGenericoTitulo00(null);
            geneTitulo.setGenericoTitulo01(null);
            geneTitulo.setGenericoTitulo02(null);
            geneTitulo.setGenericoTitulo03(null);
            geneTitulo.setGenericoTitulo04(null);
            geneTitulo.setGenericoTitulo05(null);
            geneTitulo.setGenericoTitulo06(null);
            geneTitulo.setGenericoTitulo07(null);
            geneTitulo.setGenericoTitulo08(null);
            geneTitulo.setGenericoTitulo09(null);
            geneTitulo.setGenericoTitulo10(null);
            geneTitulo.setGenericoTitulo11(null);
            geneTitulo.setGenericoTitulo12(null);
            geneTitulo.setGenericoTitulo13(null);
            geneTitulo.setGenericoTitulo14(null);
            //Controles
            geneVisible.setGenericoVisible00("hidden;");
            geneVisible.setGenericoVisible01("hidden;");
            geneVisible.setGenericoVisible02("hidden;");
            geneVisible.setGenericoVisible03("hidden;");
            geneVisible.setGenericoVisible04("hidden;");
            geneVisible.setGenericoVisible05("hidden;");
            geneVisible.setGenericoVisible06("hidden;");
            geneVisible.setGenericoVisible07("hidden;");
            geneVisible.setGenericoVisible08("hidden;");
            geneVisible.setGenericoVisible09("hidden;");
            geneVisible.setGenericoVisible10("hidden;");
            geneVisible.setGenericoVisible11("hidden;");
            geneVisible.setGenericoVisible12("hidden;");
            geneVisible.setGenericoVisible13("hidden;");
            geneVisible.setGenericoVisible14("hidden;");
            //Valor de controles
            geneCons.setGenericoCampo00(null); 
            geneCons.setGenericoCampo01(null);
            geneCons.setGenericoCampo02(null);
            geneCons.setGenericoCampo03(null);
            geneCons.setGenericoCampo04(null);
            geneCons.setGenericoCampo05(null);
            geneCons.setGenericoCampo06(null);
            geneCons.setGenericoCampo07(null);
            geneCons.setGenericoCampo08(null);
            geneCons.setGenericoCampo09(null);
            geneCons.setGenericoCampo10(null);
            geneCons.setGenericoCampo11(null);
            geneCons.setGenericoCampo12(null);
            geneCons.setGenericoCampo13(null);
            geneCons.setGenericoCampo14(null);
        }

        private void onFinalizaObjetos() {
            if (oCtrls != null) {
                oCtrls = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBAdministracion() {
        LogsContext.FormatoNormativo();
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
    }
 
    @PostConstruct
    public void onPostConstruct() {
        try {
            peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if (peticionURL.getRequestURI().contains("adminControles.sb")) {
                oCtrls = new CControles();
                //listaCtrls    = oCtrls.onControles_CargaCtrls();
                consultaCtrls = oCtrls.onControles_Consulta();
                oCtrls = null;

                oComunes = new CComunes();
                listaMoneda = oComunes.onComunes_ObtenListadoContenidoCampo("mon_nom_moneda", "Monedas", "WHERE mon_cve_st_moneda = 'ACTIVO'");
                oComunes = null;
            }
        } catch (SQLException Err) { 
            logger.error(nombreObjeto + "onPostConstruct()");
        } finally {
            if (oCtrls != null) {
                oCtrls = null;
            }
            if (oComunes != null) {
                oComunes = null;
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   G E N E R A L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onGenerales_AplicaConfirmacion() {
        oGrales = new generales();
        oGrales.onGenerales_AplicaConfirmacion();
        oGrales = null;
    }

    public void onGenerales_AplicaCancelacion() {
        oGrales = new generales();
        oGrales.onGenerales_AplicaCancelacion();
        oGrales = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C O N T R O L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onControles_LimpiaVentana() {
        oControles = new controles();
        oControles.onControles_LimpiaVentana();
        oControles = null;
    }
    public void onControles_CtrlEjecutaDespliegaVentana() {
        oControles = new controles();
        oControles.onControles_CtrlEjecutaDespliegaVentana();
        oControles = null;
    }

    public void onControles_Descarga() {
        oControles = new controles();
        oControles.onControles_Descarga();
        oControles = null;
    }
    public void onControles_Cancelar() {
        oControles = new controles();
        oControles.onControles_Cancelar();
        oControles = null;
    }

    public void onControles_CtrlEjecuta() {
        oControles = new controles();
        oControles.onControles_CtrlEjecuta();
        oControles = null;
    }

    public void onControles_RegistroNuevo() {
        oControles = new controles();
        oControles.onControles_RegistroNuevo();
        oControles = null;
    }

    public void onControles_RegistroModifica() {
        oControles = new controles();
        oControles.onControles_RegistroModifica();
        oControles = null;
    }

    public void onControles_RegistroAplica() {
        oControles = new controles();
        oControles.onControles_RegistroAplica();
        oControles = null;
    }

    public void onControles_RegistroElimina() {
        oControles = new controles();
        oControles.onControles_RegistroElimina();
        oControles = null;
    }

    public void onControles_RegistroSelecciona() {
        oControles = new controles();
        oControles.onControles_RegistroSelecciona();
        oControles = null;
    }
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
