/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : mbInterfazInversion.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

import scotiaFid.bean.ArchivoOtrosBean;
import scotiaFid.bean.ContratoBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.InvRechazoCtoInvBean;
import scotiaFid.bean.InvRechazosBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.dao.CTesoreria;
import scotiaFid.util.LogsContext;

//Class
@ViewScoped
@Named("mbInterfazInversion")
public class MBInterfazInversion implements Serializable {

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * S
	 * E R I A L * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private static final long serialVersionUID = 1L;
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * B E
	 * A N S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    private InvRechazoCtoInvBean porAsignarBean = new InvRechazoCtoInvBean();
    // -----------------------------------------------------------------------------
    private InvRechazoCtoInvBean noPerteneceBean = new InvRechazoCtoInvBean();
    // -----------------------------------------------------------------------------
    private InvRechazosBean rechazosBeanNoIdentif = new InvRechazosBean();
    // -----------------------------------------------------------------------------
    private InvRechazosBean clavesBeanSinServicio = new InvRechazosBean();
    // -----------------------------------------------------------------------------
    private InvRechazosBean otrosRechazosBean = new InvRechazosBean();
    // -----------------------------------------------------------------------------
    private InvRechazosBean detalleRechazosBean = new InvRechazosBean();
    // -----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    // -----------------------------------------------------------------------------
    private ArchivoOtrosBean archivoOtrosBean = new ArchivoOtrosBean();
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * O B
	 * J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    List<InvRechazosBean> rechazosRedaccion = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> NoIdentificados = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> filteredNoIdentif;
    // -----------------------------------------------------------------------------
    private List<Double> TotalesNoIdentificados = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazoCtoInvBean> porAsignarFiso = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazoCtoInvBean> filteredPorAsignar;
    // -----------------------------------------------------------------------------
    private List<InvRechazoCtoInvBean> noPerteneceFiducia = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazoCtoInvBean> filteredNoPertenece;
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> clavesNoIdentificadas = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> filteredClavesNoIdentificadas;
    // -----------------------------------------------------------------------------
    private Map<String, String> FormaManejo = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private List<String> Servicios = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<Double> ClavesTotalesNoIdentif = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> otrosRechazos = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<Double> OtrosTotalesRechazados = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<InvRechazosBean> DetalleRechazados = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private InvRechazosBean selectedDetalleRechazado;
    // -----------------------------------------------------------------------------
    private List<Double> TotalesDetRechazados = new ArrayList<>();
    // -----------------------------------------------------------------------------
    List<ArchivoOtrosBean> otrosIntermediarios = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<ArchivoOtrosBean> resumenOtrosInterm = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<ArchivoOtrosBean> detalleOtrosInterm = new ArrayList<>();
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * F O
	 * R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * *
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // -----------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatInver = new DecimalFormat("###############0");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatPje = new DecimalFormat("##0.0###");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatImp = new DecimalFormat("#,###,###,###,##0.0#");
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * A
	 * T R I B U T O S P R I V A D O S * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private String nombreObjeto;
    // -----------------------------------------------------------------------------
    private String PDF_REPORT_TYPE = "PDF";
    // -----------------------------------------------------------------------------
    private String TXT_REPORT_TYPE = "TXT";
    // -----------------------------------------------------------------------------
    private Calendar calendario;
    // -----------------------------------------------------------------------------
    private String archivoNombre;
    // -----------------------------------------------------------------------------
    private String archivoUbicacion;
    // -----------------------------------------------------------------------------
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    private String destinoURL;
    // -----------------------------------------------------------------------------
    private File archivo;
    // -----------------------------------------------------------------------------
    private FileOutputStream archivoSalida;
    // -----------------------------------------------------------------------------
    private byte[] buffer;
    // -----------------------------------------------------------------------------
    private String archivoLinea = new String();

    // _Habilitar_Componentes
    // -----------------------------------------------------------------------------
    private boolean habilitaCtoAsign;
    // -----------------------------------------------------------------------------
    private boolean habilitaBtnAsigna;
    // -----------------------------------------------------------------------------
    private boolean habilitaEliminaporAsignar;
    // -----------------------------------------------------------------------------
    private boolean habilitaEliminaNoPertenece;
    // -----------------------------------------------------------------------------
    private boolean habilitaTableNoAsign;
    // -----------------------------------------------------------------------------
    private boolean habilitaBtnOtrosInterm;
    // -----------------------------------------------------------------------------
    private boolean habilitaBtnAplicarInv;
    // -----------------------------------------------------------------------------
    private boolean habilitaFiltrosNoIdent;
    // -----------------------------------------------------------------------------
    private boolean habilitaServicios;
    // -----------------------------------------------------------------------------
    // _Otros_campos_de_pantalla
    // -----------------------------------------------------------------------------
    private Date FecValor;
    // -----------------------------------------------------------------------------
    private Date FechaContable;
    // -----------------------------------------------------------------------------
    private String CtoInvNoIdentificadosTotalImp;
    // -----------------------------------------------------------------------------
    private String CtoInvNoIdentificadosTotalPje;
    // -----------------------------------------------------------------------------
    private InvRechazosBean clavesNoIdentif;
    // -----------------------------------------------------------------------------
    private String ClaveNoIdentifTotalImp;
    // -----------------------------------------------------------------------------
    private String ClaveNoIdentifTotalPje;
    // -----------------------------------------------------------------------------
    private String otrosRechazosTotalImp;
    // -----------------------------------------------------------------------------
    private String otrosRechazosTotalPje;
    // -----------------------------------------------------------------------------
    private boolean checkEstructuraContable;
    // -----------------------------------------------------------------------------
    private String RechazosDet;
    // -----------------------------------------------------------------------------
    private String detalleRechazosTotalImp;
    // -----------------------------------------------------------------------------
    private String detalleRechazosTotalPje;
    // -----------------------------------------------------------------------------
    private String sCtoInv;
    // -----------------------------------------------------------------------------
    private int iIntermediario;
    // -----------------------------------------------------------------------------
    private String sEstatus;
    // -----------------------------------------------------------------------------
    private String sFisoLista;
    // -----------------------------------------------------------------------------
    private String sEstatusLista;
    // -----------------------------------------------------------------------------
    private String sNumFiso;
    // -----------------------------------------------------------------------------
    private String sNomFiso;
    // -----------------------------------------------------------------------------
    private String sPlaza;
    // -----------------------------------------------------------------------------
    private String sPromotor;
    // -----------------------------------------------------------------------------
    private String sFormaManejo;
    // -----------------------------------------------------------------------------
    private String cbServicio;
    // -----------------------------------------------------------------------------
    private String sCtaCheques;
    // -----------------------------------------------------------------------------
    private String sNumFisoOtros;
    // -----------------------------------------------------------------------------
    private String sCtoInvOtros;
    // -----------------------------------------------------------------------------
    private String sClaveOtros;
    // -----------------------------------------------------------------------------
    private String sFisoporAsignar;
    // -----------------------------------------------------------------------------
    private String sCtoInvporAsignar;
    // -----------------------------------------------------------------------------
    private String sNomArchivo;
    // -----------------------------------------------------------------------------
    private UploadedFile file;
    // -----------------------------------------------------------------------------
    private String OtrosIntermDet;
    // -----------------------------------------------------------------------------
    // //
    // -----------------------------------------------------------------------------
    private String sCtoInvNoIdent;

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * S
	 * E R V I C I O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    CTesoreria cTesoreria = new CTesoreria();
    //-----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean = new FechaContableBean();
    //-----------------------------------------------------------------------------
    //Funciones privadas estaticas
    //--------------------------------------------------------------------------
    private static HttpServletRequest peticionURL;
    //--------------------------------------------------------------------------
    private ArrayList<ContratoBean> listadoBusquedaFideicomisoPorAsignar = new ArrayList<>();

    private String sBusquedaFideicomisoPorAsignar;

    private String sBusquedaNombreContratoPorAsignar;

    private ContratoBean contratoBusqueda = new ContratoBean();

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * G
	 * E T T E R S Y S E T T E R S * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public List<InvRechazosBean> getRechazosRedaccion() {
        return rechazosRedaccion;
    }

    public void setRechazosRedaccion(List<InvRechazosBean> rechazosRedaccion) {
        this.rechazosRedaccion = rechazosRedaccion;
    }

    public InvRechazoCtoInvBean getPorAsignarBean() {
        return porAsignarBean;
    }

    public void setPorAsignarBean(InvRechazoCtoInvBean porAsignarBean) {
        this.porAsignarBean = porAsignarBean;
    }

    public InvRechazoCtoInvBean getNoPerteneceBean() {
        return noPerteneceBean;
    }

    public void setNoPerteneceBean(InvRechazoCtoInvBean noPerteneceBean) {
        this.noPerteneceBean = noPerteneceBean;
    }

    public InvRechazosBean getRechazosBeanNoIdentif() {
        return rechazosBeanNoIdentif;
    }

    public void setRechazosBeanNoIdentif(InvRechazosBean rechazosBeanNoIdentif) {
        this.rechazosBeanNoIdentif = rechazosBeanNoIdentif;
    }

    public InvRechazosBean getClavesBeanSinServicio() {
        return clavesBeanSinServicio;
    }

    public void setClavesBeanSinServicio(InvRechazosBean clavesBeanSinServicio) {
        this.clavesBeanSinServicio = clavesBeanSinServicio;
    }

    public InvRechazosBean getDetalleRechazosBean() {
        return detalleRechazosBean;
    }

    public void setDetalleRechazosBean(InvRechazosBean detalleRechazosBean) {
        this.detalleRechazosBean = detalleRechazosBean;
    }

    public OutParameterBean getOutParameterBean() {
        return outParameterBean;
    }

    public void setOutParameterBean(OutParameterBean outParameterBean) {
        this.outParameterBean = outParameterBean;
    }

    public ArchivoOtrosBean getArchivoOtrosBean() {
        return archivoOtrosBean;
    }

    public void setArchivoOtrosBean(ArchivoOtrosBean archivoOtrosBean) {
        this.archivoOtrosBean = archivoOtrosBean;
    }

    public List<InvRechazosBean> getNoIdentificados() {
        return NoIdentificados;
    }

    public void setNoIdentificados(List<InvRechazosBean> noIdentificados) {
        NoIdentificados = noIdentificados;
    }

    public List<InvRechazosBean> getFilteredNoIdentif() {
        return filteredNoIdentif;
    }

    public void setFilteredNoIdentif(List<InvRechazosBean> filteredNoIdentif) {
        this.filteredNoIdentif = filteredNoIdentif;
    }

    public List<InvRechazoCtoInvBean> getPorAsignarFiso() {
        return porAsignarFiso;
    }

    public void setPorAsignarFiso(List<InvRechazoCtoInvBean> porAsignarFiso) {
        this.porAsignarFiso = porAsignarFiso;
    }

    public List<InvRechazoCtoInvBean> getFilteredPorAsignar() {
        return filteredPorAsignar;
    }

    public void setFilteredPorAsignar(List<InvRechazoCtoInvBean> filteredPorAsignar) {
        this.filteredPorAsignar = filteredPorAsignar;
    }

    public List<InvRechazoCtoInvBean> getNoPerteneceFiducia() {
        return noPerteneceFiducia;
    }

    public void setNoPerteneceFiducia(List<InvRechazoCtoInvBean> noPerteneceFiducia) {
        this.noPerteneceFiducia = noPerteneceFiducia;
    }

    public List<InvRechazoCtoInvBean> getFilteredNoPertenece() {
        return filteredNoPertenece;
    }

    public void setFilteredNoPertenece(List<InvRechazoCtoInvBean> filteredNoPertenece) {
        this.filteredNoPertenece = filteredNoPertenece;
    }

    public List<InvRechazosBean> getClavesNoIdentificadas() {
        return clavesNoIdentificadas;
    }

    public void setClavesNoIdentificadas(List<InvRechazosBean> clavesNoIdentificadas) {
        this.clavesNoIdentificadas = clavesNoIdentificadas;
    }

    public List<InvRechazosBean> getFilteredClavesNoIdentificadas() {
        return filteredClavesNoIdentificadas;
    }

    public void setFilteredClavesNoIdentificadas(List<InvRechazosBean> filteredClavesNoIdentificadas) {
        this.filteredClavesNoIdentificadas = filteredClavesNoIdentificadas;
    }

    public InvRechazosBean getClavesNoIdentif() {
        return clavesNoIdentif;
    }

    public void setClavesNoIdentif(InvRechazosBean clavesNoIdentificadas) {
        this.clavesNoIdentif = clavesNoIdentificadas;
    }

    // _Habilita
    public boolean getHabilitaCtoAsign() {
        return habilitaCtoAsign;
    }

    public void setHabilitaCtoAsign(boolean habilitaCtoAsign) {
        this.habilitaCtoAsign = habilitaCtoAsign;
    }

    public boolean isHabilitaTableNoAsign() {
        return habilitaTableNoAsign;
    }

    public void setHabilitaTableNoAsign(boolean habilitaTableNoAsign) {
        this.habilitaTableNoAsign = habilitaTableNoAsign;
    }

    public boolean isHabilitaBtnAsigna() {
        return habilitaBtnAsigna;
    }

    public void setHabilitaBtnAsigna(boolean habilitaBtnAsigna) {
        this.habilitaBtnAsigna = habilitaBtnAsigna;
    }

    public boolean isHabilitaEliminaporAsignar() {
        return habilitaEliminaporAsignar;
    }

    public void setHabilitaEliminaporAsignar(boolean habilitaEliminaporAsignar) {
        this.habilitaEliminaporAsignar = habilitaEliminaporAsignar;
    }

    public boolean isHabilitaEliminaNoPertenece() {
        return habilitaEliminaNoPertenece;
    }

    public void setHabilitaEliminaNoPertenece(boolean habilitaEliminaNoPertenece) {
        this.habilitaEliminaNoPertenece = habilitaEliminaNoPertenece;
    }

    public boolean isHabilitaBtnOtrosInterm() {
        return habilitaBtnOtrosInterm;
    }

    public void setHabilitaBtnOtrosInterm(boolean habilitaBtnOtrosInterm) {
        this.habilitaBtnOtrosInterm = habilitaBtnOtrosInterm;
    }

    public boolean isHabilitaBtnAplicarInv() {
        return habilitaBtnAplicarInv;
    }

    public void setHabilitaBtnAplicarInv(boolean habilitaBtnAplicarInv) {
        this.habilitaBtnAplicarInv = habilitaBtnAplicarInv;
    }

    public boolean isHabilitaFiltrosNoIdent() {
        return habilitaFiltrosNoIdent;
    }

    public void setHabilitaFiltrosNoIdent(boolean habilitaFiltrosNoIdent) {
        this.habilitaFiltrosNoIdent = habilitaFiltrosNoIdent;
    }

    public boolean isHabilitaServicios() {
        return habilitaServicios;
    }

    public void setHabilitaServicios(boolean habilitaServicios) {
        this.habilitaServicios = habilitaServicios;
    }

    public Date getFecValor() {
        return FecValor;
    }

    public void setFecValor(Date fecValor) {
        FecValor = fecValor;
    }

    public String getCtoInvNoIdentificadosTotalImp() {
        return CtoInvNoIdentificadosTotalImp;
    }

    public void setCtoInvNoIdentificadosTotalImp(String ctoInvNoIdentificadosTotalImp) {
        CtoInvNoIdentificadosTotalImp = ctoInvNoIdentificadosTotalImp;
    }

    public String getCtoInvNoIdentificadosTotalPje() {
        return CtoInvNoIdentificadosTotalPje;
    }

    public void setCtoInvNoIdentificadosTotalPje(String ctoInvNoIdentificadosTotalPje) {
        CtoInvNoIdentificadosTotalPje = ctoInvNoIdentificadosTotalPje;
    }

    public String getClaveNoIdentifTotalImp() {
        return ClaveNoIdentifTotalImp;
    }

    public void setClaveNoIdentifTotalImp(String claveNoIdentifTotalImp) {
        ClaveNoIdentifTotalImp = claveNoIdentifTotalImp;
    }

    public String getClaveNoIdentifTotalPje() {
        return ClaveNoIdentifTotalPje;
    }

    public void setClaveNoIdentifTotalPje(String claveNoIdentifTotalPje) {
        ClaveNoIdentifTotalPje = claveNoIdentifTotalPje;
    }

    public String getOtrosRechazosTotalImp() {
        return otrosRechazosTotalImp;
    }

    public void setOtrosRechazosTotalImp(String otrosRechazosTotalImp) {
        this.otrosRechazosTotalImp = otrosRechazosTotalImp;
    }

    public String getOtrosRechazosTotalPje() {
        return otrosRechazosTotalPje;
    }

    public void setOtrosRechazosTotalPje(String otrosRechazosTotalPje) {
        this.otrosRechazosTotalPje = otrosRechazosTotalPje;
    }

    public Map<String, String> getFormaManejo() {
        return FormaManejo;
    }

    public void setFormaManejo(Map<String, String> formaManejo) {
        FormaManejo = formaManejo;
    }

    public List<String> getServicios() {
        return Servicios;
    }

    public void setServicios(List<String> servicios) {
        Servicios = servicios;
    }

    public List<Double> getClavesTotalesNoIdentif() {
        return ClavesTotalesNoIdentif;
    }

    public void setClavesTotalesNoIdentif(List<Double> clavesTotalesNoIdentif) {
        ClavesTotalesNoIdentif = clavesTotalesNoIdentif;
    }

    public List<Double> getOtrosTotalesRechazados() {
        return OtrosTotalesRechazados;
    }

    public void setOtrosTotalesRechazados(List<Double> otrosTotalesRechazados) {
        OtrosTotalesRechazados = otrosTotalesRechazados;
    }

    public List<InvRechazosBean> getOtrosRechazos() {
        return otrosRechazos;
    }

    public void setOtrosRechazos(List<InvRechazosBean> otrosRechazos) {
        this.otrosRechazos = otrosRechazos;
    }

    public List<InvRechazosBean> getDetalleRechazados() {
        return DetalleRechazados;
    }

    public void setDetalleRechazados(List<InvRechazosBean> detalleRechazados) {
        DetalleRechazados = detalleRechazados;
    }

    public List<Double> getTotalesDetRechazados() {
        return TotalesDetRechazados;
    }

    public void setTotalesDetRechazados(List<Double> totalesDetRechazados) {
        TotalesDetRechazados = totalesDetRechazados;
    }

    public List<ArchivoOtrosBean> getResumenOtrosInterm() {
        return resumenOtrosInterm;
    }

    public void setResumenOtrosInterm(List<ArchivoOtrosBean> resumenOtrosInterm) {
        this.resumenOtrosInterm = resumenOtrosInterm;
    }

    public List<ArchivoOtrosBean> getDetalleOtrosInterm() {
        return detalleOtrosInterm;
    }

    public void setDetalleOtrosInterm(List<ArchivoOtrosBean> detalleOtrosInterm) {
        this.detalleOtrosInterm = detalleOtrosInterm;
    }

    public boolean isCheckEstructuraContable() {
        return checkEstructuraContable;
    }

    public void setCheckEstructuraContable(boolean checkEstructuraContable) {
        this.checkEstructuraContable = checkEstructuraContable;
    }

    public String getRechazosDet() {
        return RechazosDet;
    }

    public void setRechazosDet(String rechazosDet) {
        RechazosDet = rechazosDet;
    }

    public String getDetalleRechazosTotalImp() {
        return detalleRechazosTotalImp;
    }

    public void setDetalleRechazosTotalImp(String detalleRechazosTotalImp) {
        this.detalleRechazosTotalImp = detalleRechazosTotalImp;
    }

    public String getDetalleRechazosTotalPje() {
        return detalleRechazosTotalPje;
    }

    public void setDetalleRechazosTotalPje(String detalleRechazosTotalPje) {
        this.detalleRechazosTotalPje = detalleRechazosTotalPje;
    }

    public InvRechazosBean getSelectedDetalleRechazado() {
        return selectedDetalleRechazado;
    }

    public void setSelectedDetalleRechazado(InvRechazosBean invRechazosBean) {
        this.selectedDetalleRechazado = invRechazosBean;

        //logger.debug("setSelectedDetalleRechazado" + invRechazosBean.getOrigen());
    }

    public int getiIntermediario() {
        return iIntermediario;
    }

    public void setiIntermediario(int iIntermediario) {
        this.iIntermediario = iIntermediario;
    }

    public String getsCtoInv() {
        return sCtoInv;
    }

    public void setsCtoInv(String sCtoInv) {
        this.sCtoInv = sCtoInv;
    }

    public String getsEstatus() {
        return sEstatus;
    }

    public void setsEstatus(String sEstatus) {
        this.sEstatus = sEstatus;
    }

    public String getsFisoLista() {
        return sFisoLista;
    }

    public void setsFisoLista(String sFisoLista) {
        this.sFisoLista = sFisoLista;
    }

    public String getsEstatusLista() {
        return sEstatusLista;
    }

    public void setsEstatusLista(String sEstatusLista) {
        this.sEstatusLista = sEstatusLista;
    }

    public String getsNumFiso() {
        return sNumFiso;
    }

    public void setsNumFiso(String sNumFiso) {
        this.sNumFiso = sNumFiso;
    }

    public String getsNomFiso() {
        return sNomFiso;
    }

    public void setsNomFiso(String sNomFiso) {
        this.sNomFiso = sNomFiso;
    }

    public String getsPlaza() {
        return sPlaza;
    }

    public void setsPlaza(String sPlaza) {
        this.sPlaza = sPlaza;
    }

    public String getsPromotor() {
        return sPromotor;
    }

    public void setsPromotor(String sPromotor) {
        this.sPromotor = sPromotor;
    }

    public String getsFormaManejo() {
        return sFormaManejo;
    }

    public void setsFormaManejo(String sFormaManejo) {
        this.sFormaManejo = sFormaManejo;
    }

    public String getCbServicio() {
        return cbServicio;
    }

    public void setCbServicio(String cbServicio) {
        this.cbServicio = cbServicio;
    }

    public String getsCtaCheques() {
        return sCtaCheques;
    }

    public void setsCtaCheques(String sCtaCheques) {
        this.sCtaCheques = sCtaCheques;
    }

    public String getsNumFisoOtros() {
        return sNumFisoOtros;
    }

    public void setsNumFisoOtros(String sNumFisoOtros) {
        this.sNumFisoOtros = sNumFisoOtros;
    }

    public String getsCtoInvOtros() {
        return sCtoInvOtros;
    }

    public void setsCtoInvOtros(String sCtoInvOtros) {
        this.sCtoInvOtros = sCtoInvOtros;
    }

    public String getsClaveOtros() {
        return sClaveOtros;
    }

    public void setsClaveOtros(String sClaveOtros) {
        this.sClaveOtros = sClaveOtros;
    }

    public String getsFisoporAsignar() {
        return sFisoporAsignar;
    }

    public String getsCtoInvporAsignar() {
        return sCtoInvporAsignar;
    }

    public void setsCtoInvporAsignar(String sCtoInvporAsignar) {
        this.sCtoInvporAsignar = sCtoInvporAsignar;
    }

    public void setsFisoporAsignar(String sFisoporAsignar) {
        this.sFisoporAsignar = sFisoporAsignar;
    }

    public String getsNomArchivo() {
        return sNomArchivo;
    }

    public void setsNomArchivo(String sNomArchivo) {
        this.sNomArchivo = sNomArchivo;
    }

    public String getOtrosIntermDet() {
        return OtrosIntermDet;
    }

    public void setOtrosIntermDet(String otrosIntermDet) {
        OtrosIntermDet = otrosIntermDet;
    }

    public String getsCtoInvNoIdent() {
        return sCtoInvNoIdent;
    }

    public void setsCtoInvNoIdent(String sCtoInvNoIdent) {
        this.sCtoInvNoIdent = sCtoInvNoIdent;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ArrayList<ContratoBean> getListadoBusquedaFideicomisoPorAsignar() {
        return listadoBusquedaFideicomisoPorAsignar;
    }

    public void setListadoBusquedaFideicomisoPorAsignar(ArrayList<ContratoBean> listadoBusquedaFideicomisoPorAsignar) {
        this.listadoBusquedaFideicomisoPorAsignar = listadoBusquedaFideicomisoPorAsignar;
    }

    public String getsBusquedaFideicomisoPorAsignar() {
        return sBusquedaFideicomisoPorAsignar;
    }

    public void setsBusquedaFideicomisoPorAsignar(String sBusquedaFideicomisoPorAsignar) {
        this.sBusquedaFideicomisoPorAsignar = sBusquedaFideicomisoPorAsignar;
    }

    public String getsBusquedaNombreContratoPorAsignar() {
        return sBusquedaNombreContratoPorAsignar;
    }

    public void setsBusquedaNombreContratoPorAsignar(String sBusquedaNombreContratoPorAsignar) {
        this.sBusquedaNombreContratoPorAsignar = sBusquedaNombreContratoPorAsignar;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * C O
	 * N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public MBInterfazInversion() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        LogsContext.FormatoNormativo();
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    @PostConstruct
    public void init() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                //logger.info("Sesión no valida ...");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                // Write_Log
                //logger.debug("Inicia Controller MBInterfazInversion");

                // Mensajes_Error
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.mbInterfazInversion.";

                // _mes_Abierto
                int iAnDesde = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .get("fechaAño").toString());
                int iMesDesde = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .get("fechaMes").toString());

                // _mes_Cerrado
                if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .get("fechaMSAAbierto").toString()) == 1) {
                    if (iMesDesde == 1) {
                        iAnDesde = iAnDesde - 1;
                        iMesDesde = 12;
                    } else {
                        iMesDesde = iMesDesde - 1;
                    }
                }

                if (String.valueOf(iMesDesde).length() == 1) {
                    FecValor = parseDateSimple(iAnDesde + "-0" + iMesDesde + "-01");
                } else {
                    FecValor = parseDateSimple(iAnDesde + "-" + iMesDesde + "-01");
                }

                //Se corrige fecha valor
                fechaContableBean = cTesoreria.onTesoreria_GetFechaContable();

                fechaContableBean.setFechaContable(formatDate(fechaContableBean.getFecha()));

                // _Agregar_fecha_contable_como_fecha_valor
                FechaContable = fechaContableBean.getFecha();

                fechaContableBean = null;

                rechazosRedaccion = cTesoreria.onTesoreria_ConsultaDescErr(formatDateSimple(FecValor));

                if (rechazosRedaccion.size() > 0) {
                    cTesoreria.onTesoreria_AlineaDescErr(rechazosRedaccion);
                }

                // Deshabilita_panel
                habilitaCtoAsign = false;
                habilitaBtnAsigna = false;
                habilitaEliminaporAsignar = false;
                habilitaEliminaNoPertenece = false;
                habilitaBtnOtrosInterm = true;
                habilitaBtnAplicarInv = true;
                habilitaFiltrosNoIdent = true;
                habilitaServicios = false;
                sCtoInvNoIdent = "";

                // Llena_Servicios
                Servicios = cTesoreria.onTesoreria_GetServicios(131);

                // Forma_de_Manejo
                FormaManejo = cTesoreria.onTesoreria_GetFormaManejo(24);

                ActualizaDatos();

            }
        } catch (ParseException | IOException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Construct()";
        }
    }

    public void ActualizaDatos() {
        InicializarDatos();

        // CONTRATOS_DE_INVERSION_NO_IDENTIFICADOS
        CtoNoIdentificados();

        // CONTRATOS_DE_INVERSION_POR_ASIGNAR_A_FIDEICOMISO
        CtoPorAsignarFiso();

        // CTOS_DE_INVERSION_QUE_NO_PERTENECE_A_FIDUCIARIO
        CtoNoPerteneceFiducia();

        // CLAVES_NO_IDENTIFICADAS
        ClavesNoIdentificadas();

        // OTROS_RECHAZOS
        OtrosRechazos();
    }

    public void InicializarDatos() {

        // Write_Log
        //logger.debug("Ingresa a InicializarDatos()");
        // Inicializar_valores_Detalle_Rechazados
        // Limpiar_Totales_Detalle_Rechazos
        detalleRechazosTotalImp = "";
        detalleRechazosTotalPje = "";
        sCtoInv = "";
        RechazosDet = "";
        sFisoporAsignar = "";
        sCtoInvporAsignar = "";
        sNumFisoOtros = "";
        sCtoInvOtros = "";

        // Limpia_Otros
        LimpiaOtrosRechazos();

        // Panel_Cto_Invers_No_Identif
        habilitaBtnAsigna = false;
        habilitaCtoAsign = false;
        habilitaBtnAplicarInv = true;
        habilitaBtnOtrosInterm = true;

        //Panel de búsq.
        habilitaFiltrosNoIdent = true;
        habilitaCtoAsign = false;
        habilitaBtnOtrosInterm = true;
        habilitaBtnAplicarInv = true;

        habilitaEliminaporAsignar = false;
        habilitaEliminaNoPertenece = false;
        pintaPantallaAsignaCtoInvFiso();

        // Claves_sin_Servicios
        cbServicio = null;

        // Inicializar_valores_Tablas
        NoIdentificados = new ArrayList<>();
        filteredNoIdentif = null;
        TotalesNoIdentificados = new ArrayList<>();
        porAsignarFiso = new ArrayList<>();
        filteredPorAsignar = null;
        noPerteneceFiducia = new ArrayList<>();
        filteredNoPertenece = null;
        clavesNoIdentificadas = new ArrayList<>();
        filteredClavesNoIdentificadas = null;
        ClavesTotalesNoIdentif = new ArrayList<>();
        otrosRechazos = new ArrayList<>();
        OtrosTotalesRechazados = new ArrayList<>();
        DetalleRechazados = new ArrayList<>();
        selectedDetalleRechazado = null;
        TotalesDetRechazados = new ArrayList<>();

        // Limpiar_Filtros
        clearAllFiltersNoIdentif();
        clearAllFiltersPorAsignar();
        clearAllFiltersClaves();
    }

    public void CtoNoIdentificados() {

        sCtoInvNoIdent = sCtoInvNoIdent.trim();

        if ("".equals(sCtoInvNoIdent) || sCtoInvNoIdent == null) {
            sCtoInvNoIdent = "";
        } else {
            if (!isNumeric(sCtoInvNoIdent)) {
                sCtoInvNoIdent = "";
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El Contrato de Inversión debe ser un campo numérico..."));
                return;
            }
        }

        // Write_Log
        //logger.debug("Ingresa a CtoNoIdentificados()");
        // Contratos_No_Identificados
        try {
            NoIdentificados = cTesoreria.onTesoreria_CtoNoIdentificados(formatDateSimple(FecValor), sCtoInvNoIdent);

            // Actualiza_Totales(Importe_Total_y_Pje_de_Ctos_No_Identificados)
            CtoInvNoIdentificadosTotalImp = "";
            CtoInvNoIdentificadosTotalPje = "";

            if (NoIdentificados.size() > 0) {

                TotalesNoIdentificados = cTesoreria
                        .onTesoreria_Totales_CtoNoIdentificados(formatDateSimple(FecValor));

                if (TotalesNoIdentificados.size() > 0) {
                    CtoInvNoIdentificadosTotalImp = formatDecimalImp(TotalesNoIdentificados.get(0));
                    CtoInvNoIdentificadosTotalPje = formatFormatPje(TotalesNoIdentificados.get(1));
                }
            }
        } catch (ParseException pe) {
            mensajeError = "Error al parsear fecha valor";
        }
    }

    public void CtoPorAsignarFiso() {

        // Write_Log
        //logger.debug("Ingresa a CtoPorAsignarFiso()");
        // Contratos_de_Inversion_por_Asignar_a_Fideicomiso
        porAsignarFiso = cTesoreria.onTesoreria_CtoPorAsignarFiso();

    }

    public void CtoNoPerteneceFiducia() {

        // Write_Log
        //logger.debug("Ingresa a CtoNoPerteneceFiducia()");
        // Contratos_de_Inversion_que_no_Pertenece_a_Fiduciario
        noPerteneceFiducia = cTesoreria.onTesoreria_NoPerteneceFiducia();
    }

    public void ClavesNoIdentificadas() {

        try {
            // Write_Log
            //logger.debug("Ingresa a ClavesNoIdentificadas()");
            // Claves_No_Identificadas
            clavesNoIdentificadas = cTesoreria.onTesoreria_ClavesNoIdentificadas(formatDateSimple(FecValor));
            
            ClaveNoIdentifTotalImp = "";
            ClaveNoIdentifTotalPje = "";
            
            // Actualiza_Totales(Importe_Total_y_Pje_de_los_servicios_No_Identificados)
            if (clavesNoIdentificadas.size() > 0) {
                
                ClavesTotalesNoIdentif = cTesoreria
                        .onTesoreria_Claves_Totales_CtoNoIdentif(formatDateSimple(FecValor));
                
                if (ClavesTotalesNoIdentif.size() > 0) {
                    ClaveNoIdentifTotalImp = formatDecimalImp(ClavesTotalesNoIdentif.get(0));
                    ClaveNoIdentifTotalPje = formatFormatPje(ClavesTotalesNoIdentif.get(1));
                }
            }
        } catch (ParseException ex) {
           mensajeError = "Error al parsear fecha valor";
        }
    }

    public void OtrosRechazos() {

        try {
            boolean respuesta = true;
            // Valida_nulo
            if (sNumFisoOtros.equals("")) {
                sNumFisoOtros = "";
            } else {
                if (!isNumeric(sNumFisoOtros)) {
                    // Clean
                    sNumFisoOtros = "";
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                    respuesta = false;
                }
            }
            
            sCtoInvOtros = sCtoInvOtros.trim();
            
            if (sCtoInvOtros == null || sCtoInvOtros.equals("")) {
                sCtoInvOtros = "";
            } else {
                if (!isNumeric(sCtoInvOtros)) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fiduciario", " El Cto. de Inversión debe ser un campo numérico..."));
                    sCtoInvOtros = "";
                    respuesta = false;
                }
            }
            
            if (sClaveOtros != null && !sClaveOtros.equals("")) {
                sClaveOtros = sClaveOtros.toUpperCase(Locale.ENGLISH).trim();
            }
            
            if (!respuesta) {
                return;
            }
            //String sNomFisoOtros = cTesoreria.onTesoreria_NombreContrato(sNumFisoOtros);
            
            // Write_Log
            //logger.debug("Ingresa a OtrosRechazos()");
            otrosRechazos = null;
            
            otrosRechazos = cTesoreria.onTesoreria_OtrosRechazos(formatDateSimple(FecValor), sNumFisoOtros,
                    sCtoInvOtros, sClaveOtros, checkEstructuraContable);
            
            otrosRechazosTotalImp = "";
            otrosRechazosTotalPje = "";
            
            // Actualiza_Totales(Importe_Total_y_Pje_de_Otros_Servicios)
            if (otrosRechazos.size() > 0) {
                
                OtrosTotalesRechazados = cTesoreria.onTesoreria_Totales_OtrosRechazos(formatDateSimple(FecValor),
                        sNumFisoOtros, sCtoInvOtros, sClaveOtros, checkEstructuraContable);
                
                if (OtrosTotalesRechazados.size() > 0) {
                    otrosRechazosTotalImp = formatDecimalImp(OtrosTotalesRechazados.get(0));
                    otrosRechazosTotalPje = formatFormatPje(OtrosTotalesRechazados.get(1));
                }
            }
        } catch (ParseException ex) {
            mensajeError = "Error al parsear fecha valor";
        }

    }

    public void onRowSelect_CtoInvNoIdentif(SelectEvent event) {

        // Write_Log
        //logger.debug("Ingresa a onRowSelect_CtoInvNoIdentif()");
        if (NoIdentificados.size() > 0) {
            try {
                // Get_InvRechazosBean
                this.rechazosBeanNoIdentif = (InvRechazosBean) event.getObject();
                
                DetalleRechazados = cTesoreria.onTesoreria_Detalle_Rechazados_NoIdent(formatDateSimple(FecValor),
                        rechazosBeanNoIdentif);
                
                sCtoInv = "";
                detalleRechazosTotalImp = "";
                detalleRechazosTotalPje = "";
                
                // Actualiza_Totales(Importe_Total_y_Pje
                if (DetalleRechazados.size() > 0) {
                    RechazosDet = ":: Contratos de Inversión No Identificados";
                    
                    TotalesDetRechazados = cTesoreria.onTesoreria_Totales_DetNoIdentificados(
                            formatDateSimple(FecValor), rechazosBeanNoIdentif);
                    
                    if (TotalesDetRechazados.size() > 0) {
                        detalleRechazosTotalImp = formatDecimalImp(TotalesDetRechazados.get(0));
                        detalleRechazosTotalPje = formatFormatPje(TotalesDetRechazados.get(1));
                    }
                    
                    sCtoInv = rechazosBeanNoIdentif.getCto_Inv();
                    
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron datos"));
                }
            } catch (ParseException ex) {
                mensajeError = "Error al parsear fecha valor";
            }
        }

        habilitaBtnAsigna = true;

        if (habilitaCtoAsign == true) {
            habilitaCtoAsign = false;
            pintaPantallaAsignaCtoInvFiso();
        }
    }

    public void onRowSelect_porAsignar(SelectEvent event) {
        // Write_Log
        //logger.debug("Ingresa a onRowSelect_porAsignar()");

        if (porAsignarFiso.size() > 0) {
            try {
                // Get_invRechazoCtoInvBean
                this.porAsignarBean = (InvRechazoCtoInvBean) event.getObject();
                
                habilitaEliminaporAsignar = true;
                String sCtoInver = formatFormatInver(porAsignarBean.getCto_Inv());
                
                DetalleRechazados = cTesoreria.onTesoreria_Detalle_Rechazados_Asig(formatDateSimple(FecValor),
                        sCtoInver);
                
                sCtoInv = "";
                detalleRechazosTotalImp = "";
                detalleRechazosTotalPje = "";
                
                // Actualiza_Totales(Importe_Total_y_Pje
                if (DetalleRechazados.size() > 0) {
                    RechazosDet = ":: Contratos de Inversión por Asignar a Fideicomiso";
                    
                    TotalesDetRechazados = cTesoreria.onTesoreria_Totales_DetPorAsignar(formatDateSimple(FecValor),
                            sCtoInver);
                    
                    if (TotalesDetRechazados.size() > 0) {
                        detalleRechazosTotalImp = formatDecimalImp(TotalesDetRechazados.get(0));
                        detalleRechazosTotalPje = formatFormatPje(TotalesDetRechazados.get(1));
                    }
                    
                    sCtoInv = sCtoInver;
                    
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron datos"));
                }
            } catch (ParseException ex) {
                mensajeError = "Error al parsear fecha valor";
            }
        }
    }

    public void onRowSelect_noPertenece(SelectEvent event) {
        // Write_Log
        //logger.debug("Ingresa a onRowSelect_noPertenece()");

        if (noPerteneceFiducia.size() > 0) {
            try {
                // Get_invRechazoCtoInvBean
                this.noPerteneceBean = (InvRechazoCtoInvBean) event.getObject();
                
                habilitaEliminaNoPertenece = true;
                String sCtoInver = formatFormatInver(noPerteneceBean.getCto_Inv());
                
                DetalleRechazados = cTesoreria.onTesoreria_Detalle_Rechazados_NoPert(formatDateSimple(FecValor),
                        sCtoInver);
                
                sCtoInv = "";
                detalleRechazosTotalImp = "";
                detalleRechazosTotalPje = "";
                
                if (DetalleRechazados.size() > 0) {
                    RechazosDet = ":: Contratos de Inversión No Pertenece a Fiduciario";
                    
                    TotalesDetRechazados = cTesoreria.onTesoreria_Totales_DetNoPertenece(formatDateSimple(FecValor),
                            sCtoInver);
                    
                    if (TotalesDetRechazados.size() > 0) {
                        detalleRechazosTotalImp = formatDecimalImp(TotalesDetRechazados.get(0));
                        detalleRechazosTotalPje = formatFormatPje(TotalesDetRechazados.get(1));
                    }
                    
                    sCtoInv = sCtoInver;
                    
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron datos"));
                }
            } catch (ParseException ex) {
                 mensajeError = "Error al parsear fecha valor";
            }
        }
    }

    public void onRowSelect_claveSinServ(SelectEvent event) {
        // Write_Log
        //logger.debug("Ingresa a onRowSelect_claveSinServ()");

        if (clavesNoIdentificadas.size() > 0) {
            try {
                // Get_InvRechazosBean
                this.clavesBeanSinServicio = (InvRechazosBean) event.getObject();
                
                DetalleRechazados = cTesoreria.onTesoreria_Detalle_Rechazados_Claves(formatDateSimple(FecValor),
                        clavesBeanSinServicio);
                
                sCtoInv = "";
                detalleRechazosTotalImp = "";
                detalleRechazosTotalPje = "";
                
                if (DetalleRechazados.size() > 0) {
                    RechazosDet = ":: Claves sin Servicio o Estructura Contable";
                    
                    TotalesDetRechazados = cTesoreria.onTesoreria_Totales_DetSinClave(formatDateSimple(FecValor),
                            clavesBeanSinServicio);
                    
                    if (TotalesDetRechazados.size() > 0) {
                        detalleRechazosTotalImp = formatDecimalImp(TotalesDetRechazados.get(0));
                        detalleRechazosTotalPje = formatFormatPje(TotalesDetRechazados.get(1));
                    }
                    
                    sCtoInv = clavesBeanSinServicio.getCto_Inv();
                    // sClave = clavesBeanSinServicio.getClave();
                    // sServicios = clavesBeanSinServicio.getServicio();
                    
                    // Habilita_Servicios
                    habilitaServicios = true;
                    
                } else {
                    
                    habilitaServicios = false;
                    
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron datos"));
                }
            } catch (ParseException ex) {
                 mensajeError = "Error al parsear fecha valor";
            }
        }
    }

    public void onRowSelect_OtrosRechazos(SelectEvent event) {

        String sFiltro = "";

        // Write_Log
        //logger.debug("Ingresa a onRowSelect_OtrosRechazos()");
        if (otrosRechazos.size() > 0) {
            try {
                // Get_InvRechazosBean
                this.otrosRechazosBean = (InvRechazosBean) event.getObject();
                
                if (checkEstructuraContable == true) {
                    sFiltro = "true";
                }
                
                DetalleRechazados = cTesoreria.onTesoreria_Detalle_Rechazados_Otros(formatDateSimple(FecValor),
                        otrosRechazosBean, sFiltro);
                
                sCtoInv = "";
                detalleRechazosTotalImp = "";
                detalleRechazosTotalPje = "";
                
                // Actualiza_Totales(Importe_Total_y_Pje
                if (DetalleRechazados.size() > 0) {
                    RechazosDet = ":: Otros Rechazos";
                    
                    TotalesDetRechazados = cTesoreria.onTesoreria_Totales_DetOtrosRech(formatDateSimple(FecValor),
                            otrosRechazosBean, sFiltro);
                    
                    if (TotalesDetRechazados.size() > 0) {
                        detalleRechazosTotalImp = formatDecimalImp(TotalesDetRechazados.get(0));
                        detalleRechazosTotalPje = formatFormatPje(TotalesDetRechazados.get(1));
                    }
                    
                    sCtoInv = otrosRechazosBean.getCto_Inv();
                    
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron datos"));
                }
            } catch (ParseException ex) {
                 mensajeError = "Error al parsear fecha valor";
            }
        }
    }

    public void CtoInvAsignarFiso() {
        try {
            // Write_Log
            //logger.debug("Ingresa a CtoInvAsignarFiso()");

            sEstatus = "";
            iIntermediario = 0;
            sFisoLista = "";
            sEstatusLista = "";

            // SI_YA_EXISTE_EL_CTO_DE_INVERSION,_DEBE_VERIFICAR_EL_DETALLE_DEL_ERROR
            if (rechazosBeanNoIdentif.getNo_Existe() == 0) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "Ya existe el Cto. de Inversión en Fiduciario, debe verificar el detalle del error..."));
                return;
            }

            iIntermediario = 7;
            sEstatus = "NO FIDUCIA";

            if (Double.parseDouble(rechazosBeanNoIdentif.getCto_Inv()) > 0) {

                // VERIFICA_SI_YA_EXISTE_EN_ALGUNA_DE_LAS_LISTAS
                List<String> exiteRechazos = new ArrayList<>();
                exiteRechazos = cTesoreria.onTesoreria_existeEnRechazoCtoInv(rechazosBeanNoIdentif.getCto_Inv(),
                        iIntermediario);

                if (exiteRechazos.size() > 0) {
                    sFisoLista = exiteRechazos.get(0);
                    sEstatusLista = exiteRechazos.get(1);

                    if (sEstatusLista.equals("NO FIDUCIA")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Cto. Inversión " + rechazosBeanNoIdentif.getCto_Inv()
                                        + " ya esta en la lista de los que No Pertenecen a Fiduciario"));
                    } else {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Cto. Inversión " + rechazosBeanNoIdentif.getCto_Inv()
                                        + " ya esta en la lista para ser Asignados a un Fideicomiso"));
                    }
                    return;
                }

                // PINTA_PANTALLA_ASIGNAR_CTO_INVERSION_FISO
                habilitaBtnOtrosInterm = false;
                habilitaBtnAplicarInv = false;

                habilitaTableNoAsign = false;
                habilitaFiltrosNoIdent = false;

                // Presentar_pantalla_de_Asigna_Cto_Inv_Fideicomiso
                habilitaCtoAsign = true;

                pintaPantallaAsignaCtoInvFiso();
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " CtoInvAsignarFiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void pintaPantallaAsignaCtoInvFiso() {
        // Write_Log
        //logger.debug("Ingresa a pintaPantallaAsignaCtoInvFiso()");

        sNumFiso = "";
        sNomFiso = "";
        sPlaza = "";
        sPromotor = "";
        sFormaManejo = "";
        sCtaCheques = "";

        sCtoInvNoIdent = "";
    }

    public void SalirAsignaFiso() {
        // Write_Log
        //logger.debug("Ingresa a SalirAsignaFiso()");

        pintaPantallaAsignaCtoInvFiso();

        habilitaCtoAsign = false;
        habilitaBtnOtrosInterm = true;
        habilitaBtnAplicarInv = true;
        habilitaFiltrosNoIdent = true;

        ActualizaDatos();
    }

    public void LimpiaOtrosRechazos() {
        // Write_Log
        //logger.debug("Ingresa a LimpiaOtrosRechazos()");

        sNumFisoOtros = "";
        sCtoInvOtros = "";
        sClaveOtros = "";

        // Limpia_Check_Operacion_Guia_Contable
        checkEstructuraContable = false;
    }

    public void ActualizarOtrosRechazos() {
        // Write_Log
        //logger.debug("Ingresa a ActualizarOtrosRechazos()");

        LimpiaOtrosRechazos();
        OtrosRechazos();
    }

    public void ActualizaNoIdent() {
        // Write_Log
        //logger.debug("Ingresa a ActualizaNoIdent()");

        sCtoInvNoIdent = "";
        CtoNoIdentificados();

    }

    public void onRowSelectDesAsignaPertenece() {
        // Write_Log
        //logger.debug("Ingresa a onRowSelectDesAsignaPertenece()");

        habilitaBtnAsigna = false;
    }

    public void onRowSelectDetalleRech(SelectEvent event) {
        // Write_Log
        //logger.debug("Ingresa a onRowSelectDetalleRech()");

        // Get_InvRechazosBean
        this.detalleRechazosBean = (InvRechazosBean) event.getObject();

    }

    public void onRowSelectBusquedaFideicomiso(SelectEvent event) {

        // Get_InvRechazosBean
        this.contratoBusqueda = (ContratoBean) event.getObject();
        sNumFiso = String.valueOf(contratoBusqueda.getContrato());
        sNomFiso = contratoBusqueda.getNombre();
        RequestContext.getCurrentInstance().execute("dlgBuscarFideicomisos.hide();");

    }

    public void loadDetalleRech(InvRechazosBean invRechazos) {

        //Get_CalProd
        this.detalleRechazosBean = invRechazos;

        //Show_Window_Agregar
        RequestContext.getCurrentInstance().execute("detalleDialog.show();");
    }

    public void ObtieneNomContrato() {
        try {
            // Write_Log
            //logger.debug("Ingresa a ObtieneNomContrato()");

            // Valida_nulo
            if (sNumFiso.equals("")) {
                sNomFiso = "";
                return;
            }

            if (!isNumeric(sNumFiso)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El campo Fideicomiso debe ser un campo numérico..."));
                sNumFiso = "";
                sNomFiso = "";
                return;
            }

            sNomFiso = cTesoreria.onTesoreria_NombreContrato(sNumFiso);

            if (sNomFiso.equals("")) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "No existe el Fideicomiso " + sNumFiso + "..."));
                sNumFiso = "";
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ObtieneNomContrato()";
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El Fideicomiso ingresado no es correcto..."));
            sNumFiso = "";
            sNomFiso = "";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void AbrirBusquedaContratoPorAsignar() {
        sBusquedaFideicomisoPorAsignar = "";
        sBusquedaNombreContratoPorAsignar = "";
        listadoBusquedaFideicomisoPorAsignar = cTesoreria.onTesoreria_ObtenerListadoContratosPorAsignar(sBusquedaFideicomisoPorAsignar, sBusquedaNombreContratoPorAsignar);
        RequestContext.getCurrentInstance().execute("dlgBuscarFideicomisos.show();");
    }

    public void ValidaNumContratoOtros() {
        // Valida_nulo
        if (sNumFisoOtros.equals("")) {
            sNumFisoOtros = "";
            return;
        }

        if (!isNumeric(sNumFisoOtros)) {
            // Clean
            sNumFisoOtros = "";
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
            return;
        }

    }

    private static boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void AsignaFiso() {
        try {
            // Write_Log
            //logger.debug("Ingresa a Aceptar_AsignaFiso()");
            if (validaDatos()) {
                if (Double.parseDouble(rechazosBeanNoIdentif.getCto_Inv()) > 0) {
                    RequestContext.getCurrentInstance().execute("dlgAsignaCto.show();");
                }
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto
                    + " Aceptar_AsignaFiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public boolean validaDatos() {
        // Write_Log
        //logger.debug("Ingresa a validaDatos()");
        boolean respuesta = true;
        if (sNumFiso == null || sNumFiso.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
            sNumFiso = "";
            respuesta = false;
        }

        if (sPlaza == null || sPlaza.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plaza no puede estar vacío..."));
            sPlaza = "";
            respuesta = false;
        }

        if (sPromotor == null || sPromotor.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Promotor no puede estar vacío..."));
            sPromotor = "";
            respuesta = false;
        }

        if (sFormaManejo == null || sFormaManejo.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Forma de Manejo no puede estar vacío..."));
            sFormaManejo = "";
            respuesta = false;
        }
        
        if (sCtaCheques == null || sCtaCheques.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Cuenta de Cheques no puede estar vacío..."));
            sCtaCheques = "";
            respuesta = false;
        } else {
            if(!isNumeric(sCtaCheques)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "La Cuenta de Cheques debe ser un campo numérico..."));
                sCtaCheques = "";
                respuesta = false;
            }
        }

        return respuesta;
    }

    public void Confirma_AsignaFiso() {
        try {

            // Write_Log
            //logger.debug("Ingresa a Confirma_AsignaFiso()");
            // SP PARA EL MANEJO DE RECHAZOS: SP_INV_RECHAZO_CTO
            outParameterBean = Ejecuta_ProcesoRechazos(0, // lSec
                    "ASIGNAR A FIDEICOMISO", Double.parseDouble(rechazosBeanNoIdentif.getCto_Inv()),
                    rechazosBeanNoIdentif.getNum_Entidad_Financ(), Double.parseDouble(sNumFiso), sPlaza, sPromotor,
                    sFormaManejo, sCtaCheques);

            // _Aplicado
            if (outParameterBean.getPsMsgErrOut().equals("")) {
                SalirAsignaFiso();
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto
                    + " Confirma_AsignaFiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void Confirma_NoAsignaFiso() {
        try {

            // Write_Log
            //logger.debug("Ingresa a Confirma_NoAsignaFiso()");
            outParameterBean = Ejecuta_ProcesoRechazos(0, // lSec
                    "NO PERTENECE A FIDUCIARIO", Double.parseDouble(rechazosBeanNoIdentif.getCto_Inv()),
                    rechazosBeanNoIdentif.getNum_Entidad_Financ(), 0.0, "", "",
                    "", "");

            // _Aplicado
            if (outParameterBean.getPsMsgErrOut().equals("")) {
                ActualizaDatos();
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción : " + exception.getMessage() + nombreObjeto
                    + " Confirma_NoAsignaFiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void CtoInvNoFiducia() {
        try {
            // Write_Log
            //logger.debug("Ingresa a CtoInvNoFiducia()");

            // SI_YA_EXISTE_EL_CTO_DE_INVERSION,_DEBE_VERIFICAR_EL_DETALLE_DEL_ERROR
            if (rechazosBeanNoIdentif.getNo_Existe() == 0) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "Ya existe el Cto. de Inversión en Fiduciario, debe verificar el detalle del error..."));
                return;
            }

            if (Double.parseDouble(rechazosBeanNoIdentif.getCto_Inv()) > 0) {

                // VERIFICA_SI_YA_EXISTE_EN_ALGUNA_DE_LAS_LISTAS
                List<String> exiteRechazos = new ArrayList<>();
                exiteRechazos = cTesoreria.onTesoreria_existeEnRechazoCtoInv(rechazosBeanNoIdentif.getCto_Inv(),
                        rechazosBeanNoIdentif.getNum_Entidad_Financ());

                if (exiteRechazos.size() > 0) {
                    sFisoLista = exiteRechazos.get(0);
                    sEstatusLista = exiteRechazos.get(1);

                    if (sEstatusLista.equals("NO FIDUCIA")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Cto. Inversión " + rechazosBeanNoIdentif.getCto_Inv()
                                        + " ya esta en la lista de los que No Pertenecen a Fiduciario..."));
                    } else {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Cto. Inversión " + rechazosBeanNoIdentif.getCto_Inv()
                                        + " ya esta en la lista para ser Asignados a un Fideicomiso..."));
                    }
                    return;
                }

                RequestContext.getCurrentInstance().execute("dlgNoFiducia.show();");
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto
                    + " CtoInvNoFiducia()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void clearAllFiltersNoIdentif() {

        // Write_Log
        //logger.debug("Ingresa a clearAllFiltersNoIdentif()");
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(":formInterfazInversion:ViewCtoInv:dtNoIdentif");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":formInterfazInversion:ViewCtoInv:dtNoIdentif");
        }
    }

    public void clearAllFiltersPorAsignar() {

        // Write_Log
        //logger.debug("Ingresa a clearAllFiltersPorAsignar()");
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(":formInterfazInversion:ViewCtoInv:dtPorAsignarCto");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":formInterfazInversion:ViewCtoInv:dtPorAsignarCto");
        }
    }

    public void clearAllFiltersClaves() {

        // Write_Log
        //logger.debug("Ingresa a clearAllFiltersClaves()");
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(":formInterfazInversion:ViewCtoInv:dtClavesNoIdentif");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":formInterfazInversion:ViewCtoInv:dtClavesNoIdentif");
        }
    }

    public void BorrarCtoInvFiso() {
        // Write_Log
        //logger.debug("Ingresa a BorrarCtoInvFiso()");

        if (porAsignarFiso.size() > 0) {

            if (porAsignarBean.getCto_Inv() > 0 && !porAsignarBean.getStatus().equals("ASIGNADO")) {

                // Dar_formato_para_el_usuario
                sFisoporAsignar = new DecimalFormat("################").format(porAsignarBean.getNum_Cto());
                sCtoInvporAsignar = new DecimalFormat("################").format(porAsignarBean.getCto_Inv());

                RequestContext.getCurrentInstance().execute("dlgBorraAsignarFiso.show();");
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "No se puede eliminar este registro debido a que ya se encuentra ASIGNADO..."));
            }
        }
    }

    public void Confirma_EliminarFiso() {

        // Write_Log
        //logger.debug("Ingresa a Confirma_EliminarFiso()");
        outParameterBean = Ejecuta_ProcesoRechazos(porAsignarBean.getInv_Sec(), // lSec
                "ELIMINAR", porAsignarBean.getCto_Inv(), porAsignarBean.getNum_Entidad_Financ(),
                porAsignarBean.getNum_Cto(), "", "", "", "");

        // _Aplicado
        if (outParameterBean.getPsMsgErrOut().equals("")) {
            ActualizaDatos();
        }
    }

    public void BorrarNoPerteneceFiducia() {
        // Write_Log
        //logger.debug("Ingresa a BorrarNoPerteneceFiducia()");

        if (noPerteneceFiducia.size() > 0) {

            if (noPerteneceBean.getCto_Inv() > 0) {

                // Dar_formato_para_el_usuario
                sCtoInvporAsignar = new DecimalFormat("################").format(noPerteneceBean.getCto_Inv());

                RequestContext.getCurrentInstance().execute("dlgBorraNoPertenece.show();");
                return;
            }
        }
    }

    public void Confirma_EliminarNoPerteneceFiducia() {
        // Write_Log
        //logger.debug("Ingresa a Confirma_EliminarNoPerteneceFiducia()");

        outParameterBean = Ejecuta_ProcesoRechazos(noPerteneceBean.getInv_Sec(), // lSec
                "ELIMINAR", noPerteneceBean.getCto_Inv(), noPerteneceBean.getNum_Entidad_Financ(), 0, "", "", "", "");

        // _Aplicado
        if (outParameterBean.getPsMsgErrOut().equals("")) {
            ActualizaDatos();
        }
    }

    public OutParameterBean Ejecuta_ProcesoRechazos(int iSec, String sOperacion, double dCtoInv, int iEntFin,
            double dFiso, String sPlaza, String sPromotor, String sManejo, String sCtaCheq) {
        // Write_Log
        //logger.debug("Ingresa a Ejecuta_ProcesoRechazos()");

        // SP PARA EL MANEJO DE RECHAZOS: SP_INV_RECHAZO_CTO
        outParameterBean = cTesoreria.onTesoreria_Ejecuta_AdministraCtoInv(iSec, sOperacion, dCtoInv, iEntFin, dFiso, sPlaza,
                sPromotor, sManejo, sCtaCheq);
        // _Aplicado
        if (outParameterBean.getPsMsgErrOut().equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "Operación Realizada Correctamente"));
        } // No_aplicado
        else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "Operación No Realizada: "
                                    .concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", "")))));
        }
        return outParameterBean;
    }

    public void AplicarInv() {
        try {
            // Write_Log
            //logger.debug("Ingresa a AplicarInv()");

            if (!sCtoInv.equals("") && Double.parseDouble(sCtoInv) > 0) {
                RequestContext.getCurrentInstance().execute("dlgAplicar.show();");
                return;
            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "Seleccione un Contrato de Inversión a procesar..."));
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " AplicarInv()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void Confirma_Aplicar() {
        try {
            outParameterBean = new OutParameterBean();

            // Write_Log
            //logger.debug("Ingresa a Confirma_Aplicar()");
            // Guarda_el-Cto_Inver_en_la_Clave_132_para_que_el_Proceso_Solo_tome_este_Cto
            //logger.debug("Guarda/Actualiza Clave 132");
            if (cTesoreria.onTesoreria_Clave132(sCtoInv)) {

                //logger.debug("PROCESA ARCHIVO OTROS");
                outParameterBean = cTesoreria.onTesoreria_AplicaInversion("PROCESA ARCHIVO OTROS");

                // _Aplicado
                if (outParameterBean.getPsMsgErrOut().equals("")) {

                    //logger.debug("ACTUALIZA ARCHIVO OTROS");
                    outParameterBean = new OutParameterBean();
                    outParameterBean = cTesoreria.onTesoreria_AplicaInversion("ACTUALIZA ARCHIVO OTROS");

                    // No_Aplicado
                    if (!outParameterBean.getPsMsgErrOut().equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                        "No Actualizó Archivo Otros: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
                    }
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                    "No Procesó Archivo Otros: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));

                }

                //logger.debug("PROCESA ARCHIVO MEDI");
                outParameterBean = new OutParameterBean();
                outParameterBean = cTesoreria.onTesoreria_AplicaInversion("PROCESA ARCHIVO MEDI");

                // _Aplicado
                if (outParameterBean.getPsMsgErrOut().equals("")) {
                    //logger.debug("ACTUALIZA ARCHIVO MEDI");

                    outParameterBean = new OutParameterBean();
                    outParameterBean = cTesoreria.onTesoreria_AplicaInversion("ACTUALIZA ARCHIVO MEDI");

                    // No_Aplicado
                    if (!outParameterBean.getPsMsgErrOut().equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                        "No Actualizó Archivo MEDI: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
                    }
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                    "No Procesó Archivo MEDI: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));

                }

                //logger.debug("APLICA OPERTRAN3");
                outParameterBean = new OutParameterBean();
                outParameterBean = cTesoreria.onTesoreria_AplicaInversion("APLICA OPERTRAN3");

                // No_Aplicado
                if (!outParameterBean.getPsMsgErrOut().equals("")) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                    "No Aplicó OPERTRAN3: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
                }

                //logger.debug("ACTUALIZA OPERTRAN3");
                outParameterBean = new OutParameterBean();
                outParameterBean = cTesoreria.onTesoreria_AplicaInversion("ACTUALIZA OPERTRAN3");

                // No_Aplicado
                if (!outParameterBean.getPsMsgErrOut().equals("")) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                    "No Actualizó OPERTRAN3: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
                }

                //logger.debug("Proceso Terminado");
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso Terminado"));

                ActualizaDatos();
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto
                    + " Confirma_Aplicar()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void CargaArchivoInversiones(FileUploadEvent event) {
        // Obtiene_el_nombre_del_archivo
        sNomArchivo = event.getFile().getFileName();
        file = event.getFile();

        //logger.debug("Nombre: ".concat(sNomArchivo));
        //logger.debug("Tamaño: ".concat(String.valueOf(event.getFile().getSize())));
        // Verifica_Existencia_Archivo_Procesado
        boolean bExisteArchivoProcesado = cTesoreria.onTesoreria_ExisteArchivoInversiones(sNomArchivo);

        if (bExisteArchivoProcesado) {
            // Verifica_Si_Se_Puede_Sustituir_Archivo_Procesado
            boolean bSePuedeSustituir = cTesoreria.onTesoreria_ReprocesarArchivoInversiones(sNomArchivo);

            if (!bSePuedeSustituir) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "Ya existe un archivo Cargado con este nombre " + sNomArchivo
                                + "y existen Instrucciones Aplicadas"));
                return;
            } else {
                RequestContext.getCurrentInstance().execute("dlgReprocesarArchivo.show();");
                return;
            }
        }

        OtrosIntermediarios();
    }

    public void OtrosIntermediarios() {

        if (!ValidaArchivoInversiones()) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "Error al cargar el Archivo " + sNomArchivo));
            return;
        }

        if (cTesoreria.onTesoreria_GrabaArchivoInversiones(sNomArchivo, otrosIntermediarios)) {
            ActualizaOtrosIntermediarios();
        } else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "Error al Grabar el Archivo " + sNomArchivo + " en la tabla ARCHIVO_OTROS "));
            return;
        }
    }

    public boolean ValidaArchivoInversiones() {
        //logger.debug(" Ingresa a ValidaArchivoInversiones()");

        try {

            // Variable
            int iNumLinea = 1;

            // Open_File
            // Leer_el_archivo
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputstream(), "UTF-8"),
                    10000024);
            String line = bufferedReader.readLine();

            while (line != null) {

                // Set_Values
                archivoOtrosBean = new ArchivoOtrosBean();

                /*
				 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
				 * VALIDAR ENCABEZADO * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
				 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                 */
                String[] ArregloColumnas = line.split("\\|");

                if (iNumLinea == 1) {

                    // Contenga_dos_columnas
                    if (ArregloColumnas.length != 2) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". En la primera línea del archivo"
                                        + " debe indicarse: FECHA MOVTO      TOTAL REGISTROS"));
                        return false;
                    }

                    // Fecha_Movimiento
                    if (!validarFecha(ArregloColumnas[0])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". La Fecha del Movimiento " + ArregloColumnas[1]
                                        + " debe ser tipo Fecha y Formato (DD/MM/YYYY)."));
                        return false;
                    }

                    // Total_Registros
                    if (!isNumeric(ArregloColumnas[1])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo + ". el Total de Registros "
                                        + ArregloColumnas[1] + " debe ser numérico."));
                        return false;
                    }
                } else {

                    /*
					 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
					 * VALIDAR DETALLE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
					 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                     */
                    // No_sea_linea_vacia
                    if (ArregloColumnas.length < 1) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo + ". "
                                + "En el detalle del archivo debe indicarse: FECHA_MOVTO	NUM_INTERMEDIARIO	CTO_INV	TIPO_VALOR	PIZARRA	SERIE	CUPON"
                                + "	PLAZO	TASA	NUM_CUSTODIO	IMP_INV	TITULOS	NUM_MDA	PRECIO	PREMIO 	FECHA_VENCE	ID_BANCO	TIPO_MOVTO	"
                                + "TIPO_CAMBIO	REDACCION	COMISION	IVA	ISR	"));
                        return false;
                    }

                    // FECHA_MOVTO
                    if (!validarFecha(ArregloColumnas[0])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". La Fecha de Movimiento (" + ArregloColumnas[0]
                                        + ") debe ser tipo Fecha y Formato (DD/MM/YYYY)."));
                        return false;
                    }
                    archivoOtrosBean.setFec_Movto(ArregloColumnas[0]);

                    // NUM_INTERMEDIARIO
                    if (!isNumeric(ArregloColumnas[1])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Intermediario (" + ArregloColumnas[1]
                                        + ") debe ser numérico."));
                        return false;
                    }

                    if (Integer.parseInt(ArregloColumnas[1]) <= 0) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Intermediario (" + ArregloColumnas[1]
                                        + ") es incorrecto."));
                        return false;
                    }
                    archivoOtrosBean.setNum_Entidad_Financ(ArregloColumnas[1]);

                    // CTO_INV
                    if (!isNumeric(ArregloColumnas[2])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Contrato de Inversión (" + ArregloColumnas[2]
                                        + ") debe ser numérico."));
                        return false;
                    }

                    if (Double.parseDouble(ArregloColumnas[2]) <= 0) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Contrato de Inversión (" + ArregloColumnas[2]
                                        + ") es incorrecto."));
                        return false;
                    }
                    archivoOtrosBean.setCto_Inv(ArregloColumnas[2]);

                    // TIPO_VALOR (INSTRUMENTO)
                    if (ArregloColumnas[3].trim().equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Tipo Valor (Instrumento) (" + ArregloColumnas[3]
                                        + ") es obligatorio."));
                        return false;
                    }
                    archivoOtrosBean.setTipo_Valor(ArregloColumnas[3]);

                    // TIPO_MOVTO
                    // Para_Obtener_Tipo_Servicio
                    /*
					 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
					 * TIPO DE SERVICIO (V_FORMA_NEG): * * * * * * * * * * * * * * * * * * * * * * 1
					 * = CD CompraDirecto 2 = VD VentaDirecto 3 = EF EntradaFisica 4 = SF
					 * SalidaFisica 5 = CR CompraReporto 6 = VR VentaReporto 7 = CP CompraPagare 8 =
					 * VP VentaPagare 9 = CO Comisión * * * * * * * * * * * * * * * * * * * * * * *
					 * * * * * * * * * * * * * * * *
                     */
                    if (ArregloColumnas[17].trim().equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Tipo de Movimieto (Clave de Operacion) (" + ArregloColumnas[17]
                                        + ") es obligatorio."));
                        return false;
                    }
                    archivoOtrosBean.setCve_Operacion(ArregloColumnas[17]);

                    // Obtiene_Tipo_Servicio
                    String sTipoServicio = "";
                    sTipoServicio = cTesoreria.onTesoreria_getTipoServicio(archivoOtrosBean.getCve_Operacion());
                    if (sTipoServicio.equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Fiduciario",
                                " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                + ". El Tipo de Movimieto (Clave de Operacion) ("
                                + archivoOtrosBean.getCve_Operacion() + ") No Existe" + " en la Clave 131"));
                        return false;
                    }

                    // PIZARRA SERIE CUPON
                    if (sTipoServicio.equals("CD") || sTipoServicio.equals("VD") || sTipoServicio.equals("EF")
                            || sTipoServicio.equals("SF") || sTipoServicio.equals("CR") || sTipoServicio.equals("VR")) {

                        // PIZARRA
                        if (ArregloColumnas[4].trim().equals("")) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". La Pizarra es un dato obligatorio."));
                            return false;
                        }
                        archivoOtrosBean.setPizarra(ArregloColumnas[4]);

                        // SERIE
                        if (ArregloColumnas[5].trim().equals("")) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". La Serie es un dato obligatorio."));
                            return false;
                        }
                        archivoOtrosBean.setSerie(ArregloColumnas[5]);

                        // CUPON
                        if (!isNumeric(ArregloColumnas[6])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Cupón (" + ArregloColumnas[6]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Integer.parseInt(ArregloColumnas[6]) < 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Cupón (" + ArregloColumnas[6]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setCupon(ArregloColumnas[6]);
                    }

                    // PLAZO TASA
                    if (sTipoServicio.equals("CR") || sTipoServicio.equals("VR") || sTipoServicio.equals("CP")
                            || sTipoServicio.equals("VP")) {
                        // PLAZO
                        if (!isNumeric(ArregloColumnas[7])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Plazo (" + ArregloColumnas[7]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[7]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Plazo (" + ArregloColumnas[7]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setPlazo(ArregloColumnas[7]);

                        // TASA
                        if (!isNumeric(ArregloColumnas[8])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". La Tasa (" + ArregloColumnas[8]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[8]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". La Tasa (" + ArregloColumnas[8]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setTasa(ArregloColumnas[8]);
                    }

                    // NUM_CUSTODIO
                    if (!isNumeric(ArregloColumnas[9])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Custodio (" + ArregloColumnas[9]
                                        + ") debe ser numérico."));
                        return false;
                    }

                    if (Integer.parseInt(ArregloColumnas[9]) <= 0) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Custodio (" + ArregloColumnas[9] + ") es incorrecto."));
                        return false;
                    }
                    archivoOtrosBean.setCustodio(ArregloColumnas[9]);

                    // IMP_INV TITULOS
                    if (sTipoServicio.equals("CD") || sTipoServicio.equals("VD") || sTipoServicio.equals("EF")
                            || sTipoServicio.equals("SF") || sTipoServicio.equals("CR") || sTipoServicio.equals("VR")) {

                        // IMP_INV
                        if (!isNumeric(ArregloColumnas[10])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Importe (" + ArregloColumnas[10]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[10]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Importe (" + ArregloColumnas[10]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setImporte(ArregloColumnas[10]);

                        // TITULOS
                        if (!isNumeric(ArregloColumnas[11])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Importe (" + ArregloColumnas[11]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[11]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Importe (" + ArregloColumnas[11]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setTitulos(ArregloColumnas[11]);
                    }

                    // NUM_MDA
                    if (!isNumeric(ArregloColumnas[12])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Moneda (" + ArregloColumnas[12]
                                        + ") debe ser numérico."));
                        return false;
                    }

                    if (Integer.parseInt(ArregloColumnas[12]) <= 0) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El No. de Moneda (" + ArregloColumnas[12] + ") es incorrecto."));
                        return false;
                    }
                    archivoOtrosBean.setMoneda(ArregloColumnas[12]);

                    if (sTipoServicio.equals("CD") || sTipoServicio.equals("VD") || sTipoServicio.equals("EF")
                            || sTipoServicio.equals("SF") || sTipoServicio.equals("CR") || sTipoServicio.equals("VR")) {

                        // PRECIO
                        if (!isNumeric(ArregloColumnas[13])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Precio (" + ArregloColumnas[13]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[13]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Precio (" + ArregloColumnas[13]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setPrecio(ArregloColumnas[13]);
                    }

                    if (sTipoServicio.equals("CR") || sTipoServicio.equals("VR") || sTipoServicio.equals("CP")
                            || sTipoServicio.equals("VP")) {

                        // PREMIO
                        if (!isNumeric(ArregloColumnas[14])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Premio (" + ArregloColumnas[14]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[14]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El Premio (" + ArregloColumnas[14]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setPremio(ArregloColumnas[14]);
                    }

                    if (sTipoServicio.equals("CP") || sTipoServicio.equals("VP")) {

                        // FECHA_VENCE
                        if (!validarFecha(ArregloColumnas[15])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            "ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                            + ". La Fecha del Movimiento " + ArregloColumnas[15]
                                            + " debe ser tipo Fecha y Formato (DD/MM/YYYY)."));
                            return false;
                        }
                        archivoOtrosBean.setFec_Vence(ArregloColumnas[15]);

                        // ID_BANCO
                        if (!isNumeric(ArregloColumnas[16])) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El No. de Banco (" + ArregloColumnas[16]
                                            + ") debe ser numérico."));
                            return false;
                        }

                        if (Double.parseDouble(ArregloColumnas[16]) <= 0) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                            " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO "
                                            + sNomArchivo + ". El No. de Banco (" + ArregloColumnas[16]
                                            + ") es incorrecto."));
                            return false;
                        }
                        archivoOtrosBean.setNum_Banco(ArregloColumnas[16]);

                    }

                    // TIPO_CAMBIO
                    if (!isNumeric(ArregloColumnas[18])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Tipo de Cambio (" + ArregloColumnas[18]
                                        + ") debe ser numérico."));
                        return false;
                    }

                    if (Double.parseDouble(ArregloColumnas[18]) <= 0) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El Tipo de Cambio (" + ArregloColumnas[18] + ") es incorrecto."));
                        return false;
                    }
                    archivoOtrosBean.setTipo_Cambio(ArregloColumnas[18]);

                    // REDACCION
                    if (ArregloColumnas[19].trim().equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". La Redacción es un dato obligatorio."));
                        return false;
                    }
                    archivoOtrosBean.setRedaccion(ArregloColumnas[19]);

                    // COMISION
                    if (!isNumeric(ArregloColumnas[20])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". La Comisión (" + ArregloColumnas[20] + ") debe ser numérico."));
                        return false;
                    }
                    archivoOtrosBean.setComision(ArregloColumnas[20]);

                    // IVA
                    if (!isNumeric(ArregloColumnas[21])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El IVA (" + ArregloColumnas[21] + ") debe ser numérico."));
                        return false;
                    }
                    archivoOtrosBean.setIVA(ArregloColumnas[21]);

                    // ISR
                    if (!isNumeric(ArregloColumnas[22])) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        " LINEA (" + iNumLinea + ") ERROR EN LA ESTRUCTURA DEL ARCHIVO " + sNomArchivo
                                        + ". El ISR (" + ArregloColumnas[22] + ") debe ser numérico."));
                        return false;
                    }
                    archivoOtrosBean.setISR(ArregloColumnas[22]);
                }

                iNumLinea = iNumLinea + 1;

                // Add_List
                otrosIntermediarios.add(archivoOtrosBean);
                line = bufferedReader.readLine();
            }

            // Close_BufferedReader
            bufferedReader.close();

            return true;

        } catch (FileNotFoundException FileErr) {
            mensajeError += "Descripción: " + FileErr.getMessage() + nombreObjeto + "ValidaArchivoInversiones()";
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El archivo " + sNomArchivo + " no se cargo correctamente"));
            return false;

        } catch (IOException | NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "ValidaArchivoInversiones()";
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El archivo " + sNomArchivo + " no se cargo correctamente"));
            return false;
        }
    }

    public boolean validarFecha(String fecha) {

        try {
            // Formato_fecha_(día/mes/año)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.parse(fecha);
            return true;

        } catch (ParseException e) {
            return false;
        }
    }

    public void ActualizaOtrosIntermediarios() {

        //logger.debug(" Ingresa a ActualizaOtrosIntermediarios()");
        sNomArchivo = "";
        OtrosIntermDet = "";
        detalleOtrosInterm = null;
        resumenOtrosInterm = cTesoreria.onTesoreria_ActualizaOtrosResumen();
    }

    public void onRowSelect_resumenOtros(SelectEvent event) {
        //logger.debug("Ingresa a onRowSelect_resumenOtros()");

        if (resumenOtrosInterm.size() > 0) {

            // Get_invRechazoCtoInvBean
            this.archivoOtrosBean = (ArchivoOtrosBean) event.getObject();

            detalleOtrosInterm = cTesoreria.onTesoreria_DetalleOtrosIntermediarios(archivoOtrosBean.getNom_file(),
                    archivoOtrosBean.getNum_Entidad_Financ(), archivoOtrosBean.getNum_Custodio(),
                    archivoOtrosBean.getId_Moneda());
            if (detalleOtrosInterm.size() > 0) {
                OtrosIntermDet = ": Intermediario: " + archivoOtrosBean.getNum_Entidad_Financ() + "-"
                        + archivoOtrosBean.getIntermediario() + " Custodio: " + archivoOtrosBean.getNum_Custodio()
                        + "-" + archivoOtrosBean.getCustodio() + " Moneda: " + archivoOtrosBean.getMoneda();
            }
        }

    }

    public void AplicarOtrosIntermediarios() {
        outParameterBean = new OutParameterBean();
        //logger.debug("PROCESA ARCHIVO OTROS");
        outParameterBean = cTesoreria.onTesoreria_AplicaInversion("PROCESA ARCHIVO OTROS");

        // _Aplicado
        if (outParameterBean.getPsMsgErrOut().equals("")) {

            //logger.debug("ACTUALIZA ARCHIVO OTROS");
            outParameterBean = new OutParameterBean();
            outParameterBean = cTesoreria.onTesoreria_AplicaInversion("ACTUALIZA ARCHIVO OTROS");

            // No_Aplicado
            if (!outParameterBean.getPsMsgErrOut().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                "No Actualizó Archivo Otros: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
            }

            //logger.debug("APLICA OPERTRAN3");
            outParameterBean = new OutParameterBean();
            outParameterBean = cTesoreria.onTesoreria_AplicaInversion("APLICA OPERTRAN3");

            // No_Aplicado
            if (!outParameterBean.getPsMsgErrOut().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                "No Aplicó OPERTRAN3: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
            }

            //logger.debug("ACTUALIZA OPERTRAN3");
            outParameterBean = new OutParameterBean();
            outParameterBean = cTesoreria.onTesoreria_AplicaInversion("ACTUALIZA OPERTRAN3");

            // No_Aplicado
            if (!outParameterBean.getPsMsgErrOut().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario",
                                "No Actualizó OPERTRAN3: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
            }
        } else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fiduciario", "No Procesó Archivo Otros: ".concat(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))));
        }

        //logger.debug("Proceso Terminado");
        // Set_Message
        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso Terminado"));

        ActualizaOtrosIntermediarios();

    }

    public void AltaClaveNoIdent() {
        // _Mantenimiento_Clave_No_Identificada
        //logger.debug("Ingresa a AltaClaveNoIdent()");

        if (clavesNoIdentificadas.size() <= 0) {
            return;
        }

        if (clavesBeanSinServicio.getServicio() != null) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "La Clave " + clavesBeanSinServicio.getClave() + " tiene asignado el Servicio: "
                            + clavesBeanSinServicio.getServicio() + ". De click en el boton Aplicar"));
            return;
        }

        // _Servicio
        if (cbServicio == null) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Servicio no puede estar vacío..."));
            return;
        }

        if (clavesBeanSinServicio.getClave() != null && cbServicio != null) {
            RequestContext.getCurrentInstance().execute("dlgAltaClave.show();");
        }
    }

    public void Administra_Clave() {
        // _Mantenimiento_Clave_No_Identificada

        outParameterBean = new OutParameterBean();
        // SP_INV_RECHAZO_CVE
        outParameterBean = cTesoreria.onTesoreria_AdministraClave(0, clavesBeanSinServicio.getClave(), cbServicio,
                "REGISTRO");

        if (outParameterBean.getPsMsgErrOut().equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "Operación Realizada Correctamente"));
            init();
        } else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Operación No Realizada"));
        }
    }

    public void Valida_CtoInv() {
        // Write_Log
        //logger.debug("Ingresa a Valida_CtoInv()");
        sCtoInvNoIdent = sCtoInvNoIdent.trim();

        if ("".equals(sCtoInvNoIdent) || sCtoInvNoIdent == null) {
            sCtoInvNoIdent = "";
            return;
        }

        if (!isNumeric(sCtoInvNoIdent)) {
            sCtoInvNoIdent = "";
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El Contrato de Inversión debe ser un campo numérico."));
        }
    }

    public void Valida_CtoBusquedaPorAsignar() {
        // Write_Log
        //logger.debug("Ingresa a Valida_CtoInv()");
        sBusquedaFideicomisoPorAsignar = sBusquedaFideicomisoPorAsignar.trim();

        if ("".equals(sBusquedaFideicomisoPorAsignar) || sBusquedaFideicomisoPorAsignar == null) {
            sBusquedaFideicomisoPorAsignar = "";
            return;
        }

        if (!isNumeric(sBusquedaFideicomisoPorAsignar)) {
            sBusquedaFideicomisoPorAsignar = "";
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
        }
    }

    public void ValidaCtoInv() {
        // Write_Log
        //logger.debug("Ingresa a ValidaCtoInv()");
        sCtoInvOtros = sCtoInvOtros.trim();

        if (sCtoInvOtros == null || sCtoInvOtros.equals("")) {
            return;
        }

        // Check_Is_Numeric
        if (!isNumeric(sCtoInvOtros)) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", " El Cto. de Inversión debe ser un campo numérico..."));
            sCtoInvOtros = "";
        }
    }

    public void ValidaCtaCheques() {
        // Write_Log
        //logger.debug("Ingresa a ValidaCtaCheques()");
        sCtaCheques = sCtaCheques.trim();

        if ("".equals(sCtaCheques) || sCtaCheques == null) {
            sCtaCheques = "";
            return;
        }

        // Check_Is_Numeric
        if (!isNumeric(sCtaCheques)) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "La Cuenta de Cheques debe ser un campo numérico..."));
            sCtaCheques = "";
        }
    }

    public void ExportaCtoPorAsignar() {
        try {
            calendario = Calendar.getInstance();
            calendario.setTime(parseDate((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
            formatoFecha = new SimpleDateFormat("yyyyMMdd");

            archivoNombre = "rechazos".concat(".txt");
            archivoNombre = archivoNombre.replace("Á", "A");
            archivoNombre = archivoNombre.replace("É", "E");
            archivoNombre = archivoNombre.replace("Í", "I");
            archivoNombre = archivoNombre.replace("Ó", "O");
            archivoNombre = archivoNombre.replace("Ú", "U");
            archivoUbicacion = System.getProperty("java.io.tmpdir");
            if(archivoUbicacion != null) {

 archivoUbicacion = archivoUbicacion.concat("/");

            if (GeneraArchivoTxtCtoPorAsignar(archivoUbicacion.concat(archivoNombre))) {
                destinoURL = "/scotiaFid/SArchivoDescarga?".concat(archivoNombre);
                FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se generó correctamente el archivo: " + archivoNombre));
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", mensajeError));
            }
}
        } catch (IOException | ParseException Err) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", Err.getMessage()));
        }
    }

    private boolean GeneraArchivoTxtCtoPorAsignar(String nombreArchivo) {
        boolean res = false;
        //Preparamos el archivo 
        archivo = new File(nombreArchivo);
        try {
            archivoSalida = new FileOutputStream(archivo);
            archivoLinea = new String();
            archivoLinea += "\t\t\t".concat("CONTRATOS DE INVERSIÓN PENDIENTES DE ASIGNAR AL FIDEICOMISO.".toUpperCase(Locale.US));
            archivoLinea += "\t".concat("  FECHA: ").concat(String.valueOf(formatDate(FechaContable)));
            archivoLinea = archivoLinea + "\n\n";
            buffer = archivoLinea.getBytes();
            archivoSalida.write(buffer);

            for (InvRechazoCtoInvBean porAsignarF : porAsignarFiso) {
                archivoLinea = new String();
                archivoLinea += "Nombre: Scotiabank Inverlat SA FID ".concat(String.format("%.0f", porAsignarF.getNum_Cto())).concat(" ").concat(String.valueOf(porAsignarF.getNom_Cto())).concat("\n");
                archivoLinea += "No. Contrato: ".concat(String.format("%.0f", porAsignarF.getCto_Inv())).concat("\n");
                archivoLinea += "Plaza: ".concat(String.valueOf(porAsignarF.getPlaza())).concat("\n");
                archivoLinea += "Promotor / área: ".concat(String.valueOf(porAsignarF.getPromotor())).concat("\n");
                archivoLinea += "Forma de Manejo: ".concat(String.valueOf(porAsignarF.getManejo())).concat("\n");
                archivoLinea += "Cuenta Cheques: ".concat(String.valueOf(porAsignarF.getCta_Cheques())).concat("\n");
                archivoLinea += "Estatus del Contrato: ".concat("Activo").concat("\n");
                archivoLinea = archivoLinea + "\n \n";
                buffer = archivoLinea.getBytes();
                archivoSalida.write(buffer);
            }

            archivoSalida.close();
            res = true;
        } catch (FileNotFoundException exception) {
            mensajeError = "Error al generar el archivo: " + exception.getMessage();
        } catch (IOException exception) {
            mensajeError = "Error al generar el archivo: " + exception.getMessage();
        } catch (ParseException exception) {
            mensajeError = "Error al formatear fecha valor en GeneraArchivoTxtCtoPorAsignar()";
        }
        return res;
    }

    public void limpiarCamposBusquedaPorAsignar() {
        sBusquedaFideicomisoPorAsignar = "";
        sBusquedaNombreContratoPorAsignar = "";
        listadoBusquedaFideicomisoPorAsignar = cTesoreria.onTesoreria_ObtenerListadoContratosPorAsignar(sBusquedaFideicomisoPorAsignar, sBusquedaNombreContratoPorAsignar);
    }

    public void onTabChange(TabChangeEvent event) {
        InicializarDatos();

        if (event.getTab().getId().equals("tabCtosInv")) {
            // CONTRATOS_DE_INVERSION_NO_IDENTIFICADOS
            CtoNoIdentificados();
        }

        if (event.getTab().getId().equals("tabCtosInvAsignar")) {
            // CONTRATOS_DE_INVERSION_POR_ASIGNAR_A_FIDEICOMISO
            CtoPorAsignarFiso();
        }

        if (event.getTab().getId().equals("tabCtosInvNoPerten")) {
            // CTOS_DE_INVERSION_QUE_NO_PERTENECE_A_FIDUCIARIO
            CtoNoPerteneceFiducia();
        }

        if (event.getTab().getId().equals("tabClaves")) {
            // CLAVES_NO_IDENTIFICADAS
            ClavesNoIdentificadas();
        }

        if (event.getTab().getId().equals("tabOtrosRechazados")) {
            // OTROS_RECHAZOS
            OtrosRechazos();
        }
    }

    public void buscarFideicomisosPorAsignar() {
        sBusquedaFideicomisoPorAsignar = sBusquedaFideicomisoPorAsignar.trim();

        if ("".equals(sBusquedaFideicomisoPorAsignar) || sBusquedaFideicomisoPorAsignar == null) {
            sBusquedaFideicomisoPorAsignar = "";
        } else {
            if (!isNumeric(sBusquedaFideicomisoPorAsignar)) {
                sBusquedaFideicomisoPorAsignar = "";
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                return;
            }
        }

        listadoBusquedaFideicomisoPorAsignar = cTesoreria.onTesoreria_ObtenerListadoContratosPorAsignar(sBusquedaFideicomisoPorAsignar, sBusquedaNombreContratoPorAsignar);
    }

    private synchronized String formatDateSimple(Date date) throws ParseException {
        return simpleDateFormat.format(date);
    }

    private synchronized Date parseDateSimple(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }

    private synchronized String formatDate(Date date) throws ParseException {
        return simpleDateFormat2.format(date);
    }
    
    private synchronized Date parseDate(String date) throws ParseException {
        return simpleDateFormat2.parse(date);
    }

    private synchronized String formatFormatInver(Double decimal) {
        return decimalFormatInver.format(decimal);
    }

    private synchronized String formatFormatPje(Double decimal) {
        return decimalFormatPje.format(decimal);
    }

    private synchronized String formatDecimalImp(Double decimal) {
        return decimalFormatImp.format(decimal);
    }

}
