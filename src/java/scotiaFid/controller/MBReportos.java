/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBReportos.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

import java.io.IOException;
//Imports
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import scotiaFid.bean.ConreporBean;
import scotiaFid.bean.ContratoBean;
import scotiaFid.bean.EmisionBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.dao.CFecha;
import scotiaFid.dao.CTesoreria;
import scotiaFid.util.LogsContext;
import scotiaFid.util.CValidacionesUtil;

//Class
@ViewScoped
@Named("mbReportos")
public class MBReportos implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBReportos.class);
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private static final long serialVersionUID = 1L;

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * B E A N S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // ----------------------------------------------------------------------------- 
    private EmisionBean emisionBean = new EmisionBean();
    // -----------------------------------------------------------------------------
    private ConreporBean reportosBean = new ConreporBean();
    // ----------------------------------------------------------------------------- 
    private ContratoBean contratoBean = new ContratoBean();
    // -----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean = new FechaContableBean();
    //-----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    //-----------------------------------------------------------------------------
    private List<ConreporBean> reportos = new ArrayList<>();

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    private Map<String, String> monedas = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> custodio = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> emisiones = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> tipoValores = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> ctoInversion = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private List<String> listaSubContrato = new ArrayList<>();
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F O R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    DecimalFormat decimalFormatTit = new DecimalFormat("###############");
    // -----------------------------------------------------------------------------
    DecimalFormat decimalFormatImp = new DecimalFormat("############0.00");
    // -----------------------------------------------------------------------------
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    DecimalFormat decimalFormatPrec = new DecimalFormat("###########0.00####");
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * A T R I B U T O S   P R I V A D O S * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private String nombreObjeto;
    // -----------------------------------------------------------------------------
    //_Habilitar_Botones,_Paneles_y_Listas_Desplegables	
    // -----------------------------------------------------------------------------
    private int habilitaSelCustodio;
    // -----------------------------------------------------------------------------
    private int habilitaSelCtoInv;
    // -----------------------------------------------------------------------------
    private int habilitaSelEmisiones;
    // -----------------------------------------------------------------------------
    private int habilitaBotonCancelar;
    // -----------------------------------------------------------------------------
    private int habilitaTxtTitulos;
    // -----------------------------------------------------------------------------
    private int habilitaTxtContenedor;
    // -----------------------------------------------------------------------------
    private int visiblePanelMoneda;
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtSubFiso;
    // -----------------------------------------------------------------------------
    private boolean visiblePanelPizarra;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelInv;
    // -----------------------------------------------------------------------------
    private int habilitaTxtTasa;
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtMoneda;
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtCambio;
    // -----------------------------------------------------------------------------
    //Otros_campos_de_pantalla		
    // -----------------------------------------------------------------------------
    private String tipoMovimiento;
    // -----------------------------------------------------------------------------
    private String CtaCheques;
    // -----------------------------------------------------------------------------
    private Date FecValor;
    // -----------------------------------------------------------------------------
    private int FecValorMSA;
    // -----------------------------------------------------------------------------
    private String tipoValor;
    // -----------------------------------------------------------------------------
    private String mercado;
    // -----------------------------------------------------------------------------
    private String instrumento;
    // -----------------------------------------------------------------------------
    private int iSecEmis;
    // -----------------------------------------------------------------------------
    private String NombreMon;
    // -----------------------------------------------------------------------------
    private int Intermed;
    // -----------------------------------------------------------------------------
    private String sCtoInver;
    // -----------------------------------------------------------------------------
    private String subFiso;
    // -----------------------------------------------------------------------------
    private String sValEmis;
    // -----------------------------------------------------------------------------
    private String FecMovto;
    // -----------------------------------------------------------------------------
    private double dImpMovto;
    // -----------------------------------------------------------------------------
    private int folioReporto;
    //----------------------------------------------------------------------------
    private int habilitaBotonAmortizar;
    //----------------------------------------------------------------------------
    private int habilitaBotonAmortizarAll;
    //----------------------------------------------------------------------------
    private String usuarioNombre;
    //----------------------------------------------------------------------------                
    private String mensajeConfirmaUsuario;
    //----------------------------------------------------------------------------
    private String mensajeConfirmaMensaje1;
    //----------------------------------------------------------------------------
    private String mensajeConfirmaMensaje2;
    //----------------------------------------------------------------------------
    private String mensajeConfirmaOrigen;
    //----------------------------------------------------------------------------
    private String mensajeConfirmaAccion;
    //----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    CFecha cFecha = new CFecha();
    // -----------------------------------------------------------------------------
    CTesoreria cTesoreria = new CTesoreria();

    //--------------------------------------------------------------------------
    private static HttpServletRequest peticionURL;
    //--------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S * * * * * * * * * * * * * * * * * * * * *   
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public ConreporBean getReportosBean() {
        return reportosBean;
    }

    public void setReportosBean(ConreporBean reportosBean) {
        this.reportosBean = reportosBean;
    }

    public FechaContableBean getFechaContableBean() {
        return fechaContableBean;
    }

    public void setFechaContableBean(FechaContableBean fechaContableBean) {
        this.fechaContableBean = fechaContableBean;
    }

    public ContratoBean getContratoBean() {
        return contratoBean;
    }

    public void setContratoBean(ContratoBean contratoBean) {
        this.contratoBean = contratoBean;
    }

    public EmisionBean getEmisionBean() {
        return emisionBean;
    }

    public void setEmisionBean(EmisionBean emisionBean) {
        this.emisionBean = emisionBean;
    }

    public OutParameterBean getOutParameterBean() {
        return outParameterBean;
    }

    public void setOutParameterBean(OutParameterBean outParameterBean) {
        this.outParameterBean = outParameterBean;
    }

    public String getTipomovimiento() {
        return tipoMovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipoMovimiento = tipomovimiento;
    }

    public String getSubFiso() {
        return subFiso;
    }

    public void setSubFiso(String subFiso) {
        this.subFiso = subFiso;
    }

    public Map<String, String> getCtoInversion() {
        return ctoInversion;
    }

    public void setCtoInversion(Map<String, String> ctoInversion) {
        this.ctoInversion = ctoInversion;
    }

    public Map<String, String> getTipoValores() {
        return tipoValores;
    }

    public void setTipoValores(Map<String, String> tipoValores) {
        this.tipoValores = tipoValores;
    }

    public Map<String, String> getCustodio() {
        return custodio;
    }

    public void setCustodio(Map<String, String> custodio) {
        this.custodio = custodio;
    }

    public List<String> getListaSubContrato() {
        return listaSubContrato;
    }

    public void setListaSubContrato(List<String> listaSubContrato) {
        this.listaSubContrato = listaSubContrato;
    }

    public Map<String, String> getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(Map<String, String> emisiones) {
        this.emisiones = emisiones;
    }

    public Map<String, String> getMonedas() {
        return monedas;
    }

    public void setMonedas(Map<String, String> monedas) {
        this.monedas = monedas;
    }

    //Otros_campos_de_pantalla		
    public String getCtaCheques() {
        return CtaCheques;
    }

    public void setCtaCheques(String ctaCheques) {
        CtaCheques = ctaCheques;
    }

    public void setFecValor(Date fecValor) {
        FecValor = fecValor;
    }

    public Date getFecValor() {
        return FecValor;
    }

    public int getFecValorMSA() {
        return FecValorMSA;
    }

    public void setFecValorMSA(int fecValorMSA) {
        FecValorMSA = fecValorMSA;
    }

    public String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getMercado() {
        return mercado;
    }

    public void setMercado(String mercado) {
        this.mercado = mercado;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public int getiSecEmis() {
        return iSecEmis;
    }

    public void setiSecEmis(int iSecEmis) {
        this.iSecEmis = iSecEmis;
    }

    public String getNombreMon() {
        return NombreMon;
    }

    public void setNombreMon(String nombreMon) {
        NombreMon = nombreMon;
    }

    public int getIntermed() {
        return Intermed;
    }

    public void setIntermed(int intermed) {
        this.Intermed = intermed;
    }

    public String getsCtoInver() {
        return sCtoInver;
    }

    public void setsCtoInver(String sCtoInver) {
        this.sCtoInver = sCtoInver;
    }

    public String getsValEmis() {
        return sValEmis;
    }

    public void setsValEmis(String sValEmis) {
        this.sValEmis = sValEmis;
    }

    public String getFecMovto() {
        return FecMovto;
    }

    public void setFecMovto(String FecMovto) {
        this.FecMovto = FecMovto;
    }

    public double getdImpMovto() {
        return dImpMovto;
    }

    public void setdImpMovto(double dImpMovto) {
        this.dImpMovto = dImpMovto;
    }

    public int getHabilitaSelCustodio() {
        return habilitaSelCustodio;
    }

    public void setHabilitaSelCustodio(int habilitaSelCustodio) {
        this.habilitaSelCustodio = habilitaSelCustodio;
    }

    public int getVisiblePanelMoneda() {
        return visiblePanelMoneda;
    }

    public void setVisiblePanelMoneda(int visiblePanelMoneda) {
        this.visiblePanelMoneda = visiblePanelMoneda;
    }

    public boolean isHabilitaTxtSubFiso() {
        return habilitaTxtSubFiso;
    }

    public void setHabilitaTxtSubFiso(boolean habilitaTxtSubFiso) {
        this.habilitaTxtSubFiso = habilitaTxtSubFiso;
    }

    public int getHabilitaSelCtoInv() {
        return habilitaSelCtoInv;
    }

    public void setHabilitaSelCtoInv(int habilitaSelCtoInv) {
        this.habilitaSelCtoInv = habilitaSelCtoInv;
    }

    public int getHabilitaSelEmisiones() {
        return habilitaSelEmisiones;
    }

    public void setHabilitaSelEmisiones(int habilitaSelEmisiones) {
        this.habilitaSelEmisiones = habilitaSelEmisiones;
    }

    public int getHabilitaTxtTitulos() {
        return habilitaTxtTitulos;
    }

    public void setHabilitaTxtTitulos(int habilitaTxtTitulos) {
        this.habilitaTxtTitulos = habilitaTxtTitulos;
    }

    public int getHabilitaTxtContenedor() {
        return habilitaTxtContenedor;
    }

    public void setHabilitaTxtContenedor(int habilitaTxtContenedor) {
        this.habilitaTxtContenedor = habilitaTxtContenedor;
    }

    public int getHabilitaBotonCancelar() {
        return habilitaBotonCancelar;
    }

    public void setHabilitaBotonCancelar(int habilitaBotonCancelar) {
        this.habilitaBotonCancelar = habilitaBotonCancelar;
    }

    public void setHabilitaPanelInv(boolean habilitaPanelInv) {
        HabilitaPanelInv = habilitaPanelInv;
    }

    public boolean getHabilitaPanelInv() {
        return HabilitaPanelInv;
    }

    public int getHabilitaTxtTasa() {
        return habilitaTxtTasa;
    }

    public void setHabilitaTxtTasa(int habilitaTxtTasa) {
        this.habilitaTxtTasa = habilitaTxtTasa;
    }

    public boolean isVisiblePanelPizarra() {
        return visiblePanelPizarra;
    }

    public void setVisiblePanelPizarra(boolean visiblePanelPizarra) {
        this.visiblePanelPizarra = visiblePanelPizarra;
    }

    public boolean isHabilitaTxtMoneda() {
        return habilitaTxtMoneda;
    }

    public void setHabilitaTxtMoneda(boolean habilitaTxtMoneda) {
        this.habilitaTxtMoneda = habilitaTxtMoneda;
    }

    public boolean isHabilitaTxtCambio() {
        return habilitaTxtCambio;
    }

    public void setHabilitaTxtCambio(boolean habilitaTxtCambio) {
        this.habilitaTxtCambio = habilitaTxtCambio;
    }

    public List<ConreporBean> getReportos() {
        return reportos;
    }

    public void setReportos(List<ConreporBean> reportos) {
        this.reportos = reportos;
    }

    public int getFolioReporto() {
        return folioReporto;
    }

    public void setFolioReporto(int folioReporto) {
        this.folioReporto = folioReporto;
    }

    public int getHabilitaBotonAmortizar() {
        return habilitaBotonAmortizar;
    }

    public void setHabilitaBotonAmortizar(int habilitaBotonAmortizar) {
        this.habilitaBotonAmortizar = habilitaBotonAmortizar;
    }

    public int getHabilitaBotonAmortizarAll() {
        return habilitaBotonAmortizarAll;
    }

    public void setHabilitaBotonAmortizarAll(int habilitaBotonAmortizarAll) {
        this.habilitaBotonAmortizarAll = habilitaBotonAmortizarAll;
    }

    public String getMensajeConfirmaUsuario() {
        return mensajeConfirmaUsuario;
    }

    public void setMensajeConfirmaUsuario(String mensajeConfirmaUsuario) {
        this.mensajeConfirmaUsuario = mensajeConfirmaUsuario;
    }

    public String getMensajeConfirmaMensaje1() {
        return mensajeConfirmaMensaje1;
    }

    public void setMensajeConfirmaMensaje1(String mensajeConfirmaMensaje1) {
        this.mensajeConfirmaMensaje1 = mensajeConfirmaMensaje1;
    }

    public String getMensajeConfirmaMensaje2() {
        return mensajeConfirmaMensaje2;
    }

    public void setMensajeConfirmaMensaje2(String mensajeConfirmaMensaje2) {
        this.mensajeConfirmaMensaje2 = mensajeConfirmaMensaje2;
    }

    public String getMensajeConfirmaOrigen() {
        return mensajeConfirmaOrigen;
    }

    public void setMensajeConfirmaOrigen(String mensajeConfirmaOrigen) {
        this.mensajeConfirmaOrigen = mensajeConfirmaOrigen;
    }

    public String getMensajeConfirmaAccion() {
        return mensajeConfirmaAccion;
    }

    public void setMensajeConfirmaAccion(String mensajeConfirmaAccion) {
        this.mensajeConfirmaAccion = mensajeConfirmaAccion;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public MBReportos() {
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
                //logger.debug("Inicia Controller MBReportos");

                // Deshabilitar_paneles
                visiblePanelMoneda = 1;
                HabilitaPanelInv = true;
                visiblePanelPizarra = true;
                habilitaTxtSubFiso = true;

                // Mensajes_de_Error
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.MBReportos";

                // Tipo_Movimiento
                tipoMovimiento = "CR";

                // _Determinar_Custodio
                // Agrega_Casa_de_Bolsa_como_Custodio_en_el_Intermediario_Bancario_Scotia
                ArrayList<Integer> sComplementoCust = new ArrayList<Integer>();
                sComplementoCust.add(4);
                sComplementoCust.add(5);

                custodio = cTesoreria.onTesoreria_GetCustodio(386, sComplementoCust);

                // Carga_Tipo_Valor
                ArrayList<Integer> iTipoValor = new ArrayList<Integer>();
                iTipoValor.add(3);
                iTipoValor.add(4);
                tipoValores = cTesoreria.onTesoreria_GetTipoValorReporto(iTipoValor);

                // Set_Bean
                if (this.reportosBean == null) {
                    this.reportosBean = new ConreporBean();
                }

                // Get_Fecha_Contable
                fechaContableBean = cTesoreria.onTesoreria_GetFechaContable();

                // Format_Fecha_Contable
                fechaContableBean.setFechaContable(formatDate(fechaContableBean.getFecha()));

                // Agregar_fecha_contable_como_fecha_valor
                FecValorMSA = 0;
                FecValor = fechaContableBean.getFecha();
                reportosBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                // Establece_Tipo_de_Cambio
                reportosBean.setMoneda(1);
                reportosBean.setTipoCambio(1);

                //Vencimiento de reportos
                usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");

                initVencReportos();

            }
        } catch (DateTimeException | IOException | ParseException exception) {
            mensajeError += "Descripción : " + exception.getMessage() + nombreObjeto + " Construct()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    private void initVencReportos() {
        // Set_Bean

        this.setReportos(cTesoreria.onTesoreria_GetReportos(FecValor));
        habilitaBotonAmortizarAll = 0;
        habilitaBotonAmortizar = 0;
        folioReporto = 0;

        int sizeReportos = this.getReportos().size();

        if (sizeReportos > 0) {
            habilitaBotonAmortizarAll = 1;
        }

    }

    public void select_FecVal() {
        FecValorMSA = 0;

        //logger.debug("Ingresa a select_FecVal()");
        try {
            if (FecValor != null) {
                formatDate(FecValor);

                //Default_fec_contable
                reportosBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                //La_fecha_valor_es_menor_o_igual_a_la_fecha_contable
                if (FecValor.before(parseDate(fechaContableBean.getFechaContable()))
                        || FecValor.equals(parseDate(fechaContableBean.getFechaContable()))) {

                    if (cFecha.onFecha_DiaHabil(FecValor) == 1) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha inválida, debe ser un dia hábil..."));
                        FecValor = fechaContableBean.getFecha();
                        return;
                    } else {
                        //Validar_si_permite_aplicar_con_fecha_Valor
                        int iMesVal = 0, iYearVal = 0, iMesCont = 0, iYearCont = 0;

                        iMesVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                        iMesCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                        if (iMesVal != iMesCont || iYearVal != iYearCont) {
                            if (cFecha.onFecha_FecValTesoreria()) {
//							iFecValor_Param = cFecha.onFecha_FecValTesoreria(); 
//							//Mes_Abierto_y_Permite_Tesoreria
//							if(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 1 && iFecValor_Param == 1)
//							{
                                if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAMes").toString()) != iMesVal
                                        || Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAño").toString()) != iYearVal) {

                                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No puede aplicar movimiento con esa fecha..."));
                                    FecValor = fechaContableBean.getFecha();
                                    return;
                                } else {
                                    FecValorMSA = 1;
                                }
                                RequestContext.getCurrentInstance().execute("dlgFecVal.show();");
                                return;

                            } else {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No puedo aplicar movimiento con esa fecha..."));
                                FecValor = fechaContableBean.getFecha();
                                return;
                            }
                        } else {
                            if (FecValor.before(parseDate(fechaContableBean.getFechaContable()))) {
                                RequestContext.getCurrentInstance().execute("dlgFecVal.show();");
                            }
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha inválida, no puede ser mayor a la fecha contable..."));
                    FecValor = fechaContableBean.getFecha();
                    return;
                }

                //Asigna_el_valor_de_la_fecha_a_la_variable_Bean
                reportosBean.setDiaOpera(formatDate(FecValor));

                DeterminaMoneda();
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La fecha es incorrecta..."));
                FecValor = fechaContableBean.getFecha();
                return;
            }

        } catch (DateTimeException | NumberFormatException | ParseException | SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_FecVal()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void ConfirmaFecValor() {
        try {
            //logger.debug("Ingresa a ConfirmaFecValor()");
            reportosBean.setDiaOpera(formatDate(FecValor));
            listaSubContrato = null;
            reportosBean.setFideicomiso("");
            reportosBean.setSubFiso("");

            DeshabilitarComponentes();
            LimpiarTipoValores();
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
            visiblePanelMoneda = 1;
            DeterminaMoneda();

        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en ConfirmaFecValor()");
        }
    }

    public void NiegaFecValor() {
        try {
            //logger.debug("Ingresa a NiegaFecValor()");
            FecValorMSA = 0;
            FecValor = fechaContableBean.getFecha();
            reportosBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en ConfirmaFecValor()");
        }
    }

    public void checkFideicomiso() {
        try {
            // Write_Log
            //logger.debug("Ingresa a checkFideicomiso()");
            //logger.debug("Fideicomiso:".concat(reportosBean.getFideicomiso()));
            DeshabilitarComponentes();
            contratoBean = new ContratoBean();

            reportosBean.setFideicomiso(reportosBean.getFideicomiso().trim());
            listaSubContrato = null;
            reportosBean.setSubFiso("");
            this.subFiso = "";
            habilitaBotonCancelar = 1;

            // Check_Fideicomiso_vacío
            if ("".equals(reportosBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(reportosBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));

                reportosBean.setFideicomiso("");
                return;
            }

            if (Integer.parseInt(reportosBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es inválido..."));
                reportosBean.setFideicomiso("");
                return;
            }

            //_Valida_si_el_contrato_existe_Activo
            contratoBean = cTesoreria.onTesoreria_checkFideicomiso(Integer.parseInt(reportosBean.getFideicomiso()));

            //_Valida_que_permita_operaciones_de_Inversión_y_no_esta_bloqueado
            if (contratoBean.getContrato() == Integer.parseInt(reportosBean.getFideicomiso())
                    && cTesoreria.onTesoreria_CtoBloqueado(contratoBean.getContrato(), "I")) {

                listaSubContrato = cTesoreria.onTesoreria_ListadoSubContratoSrv(Integer.parseInt(reportosBean.getFideicomiso()));

                if (contratoBean.getbSubContrato() && contratoBean.getProrrateo() == 0) {

                    habilitaTxtSubFiso = false;
                    //logger.debug("SubContrato: " + contratoBean.getSubcontrato());
                    habilitaTxtTasa = 1;
                    habilitaTxtTitulos = 1;
                    habilitaBotonCancelar = 1;
                    habilitaTxtContenedor = 1;
                    reportosBean.setSubFiso(String.valueOf(0)); // SubFiso

                    //Habilita_Custodio
                    habilitaSelCustodio = 1;

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(reportosBean.getFideicomiso(), reportosBean.getSubFiso(), Intermed, tipoMovimiento);

                    //_Validar_si_trae_resultados
                    if (ctoInversion.size() > 1) {
                        // Habilita_control
                        habilitaSelCtoInv = 1;
                    } else {
                        // Deshabilita_control
                        habilitaSelCtoInv = 0;

                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontró Contrato de Inversión..."));
                    }
                } else {
                    // Establece_SubFiso
                    habilitaTxtTasa = 1;
                    habilitaTxtTitulos = 1;
                    habilitaBotonCancelar = 1;
                    habilitaTxtContenedor = 1;
                    habilitaTxtSubFiso = false;

                    reportosBean.setSubFiso(String.valueOf(0)); // SubFiso

                    //Habilita_Custodio
                    habilitaSelCustodio = 1;

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(reportosBean.getFideicomiso(), reportosBean.getSubFiso(), Intermed, tipoMovimiento);

                    //_Validar_si_trae_resultados
                    if (ctoInversion.size() > 1) {
                        // Habilita_control
                        habilitaSelCtoInv = 1;
                    } else {
                        // Deshabilita_control
                        habilitaSelCtoInv = 0;

                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontró Contrato de Inversión..."));
                    }
                    return;
                }

            } else {
                // Disabled
                DeshabilitarComponentes();
                habilitaTxtTasa = 0;
                habilitaSelCtoInv = 0;
                habilitaTxtContenedor = 0;
                habilitaTxtTitulos = 0;
                habilitaSelCustodio = 0;

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                reportosBean.setFideicomiso("");
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " checkFideicomiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void checkSubFiso() {
        try {
            String[] SubFisoSeleccion = subFiso.split(".-");
            String reportSubFiso = SubFisoSeleccion[0];
            // Write_Log
            //logger.debug("Ingresa a checkSubFiso()");
            reportosBean.setSubFiso(reportSubFiso.trim());

            // Check_Fideicomiso_vacío
            if ("".equals(reportosBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_SubFideicomiso_vacío
            if ("".equals(reportosBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(reportosBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                reportosBean.setSubFiso("0");
                subFiso = "0";
                return;
            }

            // Check_SubFiso_vacío
            if (!reportosBean.getSubFiso().equals("")) {

                //logger.debug("SubFiso:".concat(reportosBean.getSubFiso()));
                //_Valida_el_Sub_contrato_existe_Activo
                //_Valida_que_permita_operaciones_de_Inversión_y_no_este_bloqueado
                //_Parametros:_DatosContrato,_SubFiso,_sTipoBloque
                if (cTesoreria.onTesoreria_VerificaSubContratoSrv(contratoBean, Integer.parseInt(reportosBean.getSubFiso()), "I")) {

                    //_Habilita 
                    habilitaTxtTasa = 1;
                    habilitaTxtTitulos = 1;
                    habilitaSelCustodio = 1;
                    habilitaBotonCancelar = 1;
                    habilitaTxtContenedor = 1;
                    habilitaTxtSubFiso = false;

                    sCtoInver = "";
                    CtaCheques = "";
                    reportosBean.setCtoInver(0);
                    reportosBean.setEntidadFinan(0);

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(reportosBean.getFideicomiso(), reportosBean.getSubFiso(), Intermed, tipoMovimiento);

                    //_Validar_si_trae_resultados
                    if (ctoInversion.size() > 1) {
                        // Habilita_control
                        habilitaSelCtoInv = 1;
                    } else {
                        //_Deshabilita 
                        habilitaSelCtoInv = 0;

                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontró Contrato de Inversión..."));
                    }
                    return;
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso no existe o no está activo..."));
                    reportosBean.setSubFiso("");
                    return;
                }
            } else {
                reportosBean.setSubFiso("0");
                subFiso = "0";
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " checkSubFiso()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void select_CtoInv() {

        // Write_Log
        //logger.debug("Ingresa a select_CtoInv()");
        try {

            // Validate_Servicio
            if (sCtoInver.equals("0")) {

                CtaCheques = "";
                reportosBean.setCtoInver(0);
                reportosBean.setEntidadFinan(0);

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
                return;
            } else {

                String[] Id_CtoInv;
                String CtoInv = "", NumInterm = "";

                //_Recuperar_los_valores_de_la_lista_de_Contrato_de_Inversión
                Id_CtoInv = sCtoInver.split("/");

                //_Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                CtoInv = Id_CtoInv[0];
                NumInterm = Id_CtoInv[1]; // Intermediario_o_Entidad_Financiera

                reportosBean.setCtoInver(Double.parseDouble(CtoInv));
                reportosBean.setEntidadFinan(Integer.parseInt(NumInterm));

                //_Determinar_Cuenta_de_Cheques
                CtaCheques = cTesoreria.onTesoreria_GetCtaCheques(reportosBean.getFideicomiso(), reportosBean.getSubFiso(), reportosBean.getEntidadFinan(), reportosBean.getCtoInver());
                if (CtaCheques.contains("No existe contrato de Inversión")) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe contrato de Inversión..."));
                    CtaCheques = "";
                }

                // ***********************************************************************
                /*
				//logger.debug("******Datos seleccionando Contrato de Inversión*****");
				//logger.debug("Instrumento: " +reportosBean.getIdInstrume());
				//logger.debug("Mercado: " + reportosBean.getIdMercado());				
				//logger.debug("Entidad / Intermediario: " + reportosBean.getEntidadFinan());				
				//logger.debug("Cto. Inversión: " + reportosBean.getCtoInver());
				//logger.debug("Cuenta cheques " + CtaCheques);
                 */
                // ***********************************************************************
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_CtoInv()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void select_Custodio() {
        // Write_Log
        //logger.debug("Ingresa a select_Custodio()");

        // Validate_Servicio
        if (reportosBean.getCustodio().equals("0")) {
            reportosBean.setCustodio("");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
            return;
        }
    }

    public void select_TipoValor() {
        try {
            // Write_Log
            //logger.debug("Ingresa a select_TipoValor()");
            sValEmis = "0";
            habilitaBotonCancelar = 1;

            if (tipoValor.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Valor no puede estar vacío..."));

                //Clean
                LimpiarTipoValores();
                reportosBean.setNomPizarra(null);
                reportosBean.setSerieEmis(null);
                reportosBean.setCuponVig(null);
                reportosBean.setPrecioEmis(0);
                reportosBean.setTitulos("");

                return;

            } else {
                reportosBean.setIdMercado(0);
                reportosBean.setIdInstrume(0);
                String[] LabelTipoValor;

                // Seleccionar_Tipo_Valor
                //logger.debug("Tipo Valor: " + getTipoValor());
                for (Map.Entry<String, String> o : tipoValores.entrySet()) {

                    if (getTipoValor().equals(o.getKey())) {

                        LabelTipoValor = o.getValue().split("/");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        mercado = LabelTipoValor[1];
                        instrumento = LabelTipoValor[2];

                        reportosBean.setIdMercado(Integer.parseInt(o.getKey().substring(0, 1)));
                        reportosBean.setIdInstrume(Integer.parseInt(o.getKey().substring(1, 4)));

                        /*
						//logger.debug("Valor seleccionado: " + o.getKey() + " - " + o.getValue());
						//logger.debug("Mercado: " + mercado);
						//logger.debug("Instrumento: " + instrumento);
						//logger.debug("Id. Mercado:" + reportosBean.getIdMercado());
						//logger.debug("Id. Instrumento:" + reportosBean.getIdInstrume());
                         */
                    }
                }

                // Carga_Emisiones
                habilitaSelEmisiones = 1;
                reportosBean.setNomPizarra(null);
                reportosBean.setSerieEmis(null);
                reportosBean.setCuponVig(null);
                reportosBean.setPrecioEmis(0);
                reportosBean.setTitulos("");
                habilitaBotonCancelar = 1;

                emisiones = cTesoreria.onTesoreria_GetEmisionesEntradaSalida(reportosBean.getIdMercado(), reportosBean.getIdInstrume());

                if (reportosBean.getIdMercado() != 5) {
                    reportosBean.setMoneda(1);
                    reportosBean.setTipoCambio(1);
                    NombreMon = "MONEDA NACIONAL";
                    visiblePanelMoneda = 0;
                } else {
                    visiblePanelMoneda = 0;
                }
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_TipoValor()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void select_Emisiones() {

        //logger.debug("Ingresa a select_Emisiones()");
        try {
            iSecEmis = 0;
            reportosBean.setNomPizarra(null);
            reportosBean.setSerieEmis(null);
            reportosBean.setCuponVig(null);
            //reportosBean.setPrecioEmis(0);
            //reportosBean.setTitulos("");

            if (sValEmis.equals("0")) {

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisión no puede estar vacío..."));
                return;
            } else {
                String[] LabelEmisora;
                String[] LabelMoneda;

                // Seleccionar_Emisora
                for (Map.Entry<String, String> o : emisiones.entrySet()) {

                    if (sValEmis.equals(o.getKey())) {
                        LabelMoneda = o.getKey().split("/");
                        LabelEmisora = o.getValue().split(" / ");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        reportosBean.setMoneda(Integer.parseInt(LabelMoneda[0].trim())); 	//_Moneda	
                        iSecEmis = Integer.parseInt(LabelMoneda[1].trim());					//_Secuencia_Emisora
                        reportosBean.setNomPizarra(LabelEmisora[0].trim());
                        reportosBean.setSerieEmis(LabelEmisora[1].trim());
                        reportosBean.setCuponVig(LabelEmisora[2].trim());

                        //*************************************************************
                        /*
						//logger.debug("Datos en Emisiones ");
						//logger.debug("Pizarra: " + reportosBean.getNomPizarra());
						//logger.debug("Serie: " + reportosBean.getSerieEmis());
						//logger.debug("Cupón: " + reportosBean.getCuponVig());
						//logger.debug("Moneda: " + reportosBean.getMoneda());
						//logger.debug("Secuencia Emisora: " + iSecEmis);
                         */
                        //*************************************************************
                    }
                }

                //Habilita_Precio
                habilitaTxtTitulos = 1;

                // Determina_Moneda
                DeterminaMoneda();
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Emisiones()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void DeterminaMoneda() {
        int iDia = 0, iMes = 0, iAnio = 0;

        //logger.debug("Ingresa a DeterminaMoneda()");
        try {
            if (reportosBean.getNomPizarra() != null && reportosBean.getSerieEmis() != null) {

                if (reportosBean.getMoneda() != 1) {// Diferente_a_Moneda_Nacional

                    visiblePanelMoneda = 0;

                    iDia = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                    iMes = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                    iAnio = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                    monedas = cTesoreria.onTesoreria_GetTipoCambioEntradaSalida(iDia, iMes, iAnio,
                            reportosBean.getMoneda());

                    if (reportosBean.getMoneda() != 1) {

                        for (Map.Entry<String, String> o : monedas.entrySet()) {
                            // _Recuperar_valor_de_Moneda_y_Tipo_de_Cambio
                            reportosBean.setMoneda(Integer.parseInt(o.getKey()));

                            String[] LabelTipoCamb = o.getValue().split("/");
                            reportosBean.setTipoCambio(CValidacionesUtil.validarDouble(LabelTipoCamb[0]));
                            NombreMon = LabelTipoCamb[1];

                            habilitaTxtMoneda = false;
                        }

                        if (reportosBean.getTipoCambio() == 0) {
                            reportosBean.setTipoCambio(1);
                        }

                        /*
						//logger.debug("Tipo Cambio: " + reportosBean.getTipoCambio());
						//logger.debug("Moneda: " + reportosBean.getMoneda() + " Nombre Mon: " + NombreMon);
                         */
                    }

                }
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " DeterminaMoneda()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void CalculaPremio() {
        try {
            //logger.debug("Ingresa a CalculaPremio()");
            if (!"".equals(reportosBean.getImporte()) && reportosBean.getImporte() != null) {
                if (Integer.parseInt(reportosBean.getPlazo()) > 0 && Double.parseDouble(reportosBean.getImporte()) > 0 && Double.parseDouble(reportosBean.getTasa()) > 0) {
                    reportosBean.setPremio(formatDecimalImp(Double.parseDouble(formatDecimalImp(Double.parseDouble(reportosBean.getImporte()) * Integer.parseInt(reportosBean.getPlazo()) * Double.parseDouble(reportosBean.getTasa()) / 36000))));
                } else {
                    dImpMovto = 0;
                    reportosBean.setPremio("0");
                }
            } else {
                dImpMovto = 0;
                reportosBean.setPremio("0");
            }

        } catch (NumberFormatException | ArithmeticException exception) {
            reportosBean.setPremio("0.00");
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " CalculaPremio()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void bCancelar() {
        RequestContext.getCurrentInstance().execute("dlgSuspender.show();");
    }

    public void bConfirmaCancelacion() {
        try {
            //logger.debug("Ingresa a bConfirmaCancelacion()");

            listaSubContrato = null;
            reportosBean.setFideicomiso("");
            reportosBean.setSubFiso("");
            DeshabilitarComponentes();
            LimpiarTipoValores();
            reportosBean.setDiaOpera(fechaContableBean.getFechaContable());
            init();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/tesoreriaReportos.sb");
        } catch (IOException ex) {
            mensajeError = "Error al redireccionar: " + ex.getMessage();
        }
    }

    public void bAceptaReportos() {

        //logger.debug("Ingresa a bAceptaReportos()");
        if (ValidacionesPantalla()) {
            ValidacionesNegocio();
        }

    }

    public boolean ValidacionesPantalla() {
        //logger.debug("Ingresa a ValidacionesPantalla()");
        boolean respuesta = true;
        if (tipoValor.equals("0")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Valor no puede estar vacío..."));
            respuesta = false;
        }

        // Check_Fideicomiso_vacío
        if ("".equals(reportosBean.getFideicomiso())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getSubFiso() == null || "".equals(reportosBean.getSubFiso())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
            respuesta = false;
        }

        // Check_Cto Inversion_vacío
        if (sCtoInver == null || "".equals(sCtoInver) || reportosBean.getCtoInver() == 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getEntidadFinan() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //      "Fiduciario", "El campo Intermediario no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getIdMercado() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //      new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mercado no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getIdInstrume() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            // "Fiduciario", "El campo Instrumento no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getCustodio() == null || reportosBean.getCustodio().equals("") || reportosBean.getCustodio().equals("0")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getPlazo() == null || "".equals(reportosBean.getPlazo())) {
            // Clean
            dImpMovto = 0;
            FecMovto = null;
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo no puede estar vacío..."));
            respuesta = false;
        } else if (!isNumeric(reportosBean.getPlazo())) {
            // Clean
            dImpMovto = 0;
            FecMovto = null;
            reportosBean.setPlazo("0");
            reportosBean.setPremio("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Plazo debe ser un campo numérico..."));
            respuesta = false;
        } else if (Integer.parseInt(reportosBean.getPlazo()) > 10000 || Integer.parseInt(reportosBean.getPlazo()) < 1) {

            dImpMovto = 0;
            FecMovto = null;
            reportosBean.setPlazo("0");
            reportosBean.setPremio("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo debe ser mayor que cero y no exceder a 10,000..."));
            respuesta = false;
        } else {

            try {
                reportosBean.setDiaOpera(formatDate(FecValor));
                FecMovto = formatDate(cFecha.Proyecta_Tiempo(parseDate(reportosBean.getDiaOpera()), Integer.parseInt(reportosBean.getPlazo()), "Dias"));

                if (FecMovto != null) {
                    if (cFecha.onFecha_DiaHabil(parseDate(FecMovto)) == 1) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Fecha Proyectada no es hábil " + FecMovto));

                        //Clean
                        FecMovto = null;
                        reportosBean.setPlazo("0");
                        respuesta = false;
                    }
                } else {
                    FecMovto = null;
                    reportosBean.setPlazo("0");

                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo es erróneo..."));
                    respuesta = false;
                }
            } catch (NumberFormatException | ParseException | SQLException ex) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo es erróneo..."));
                respuesta = false;
            }
        }

        if ("".equals(reportosBean.getTasa()) || reportosBean.getTasa() == null) {
            // Clean
            dImpMovto = 0;
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tasa no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(reportosBean.getTasa())) {
            // Clean
            dImpMovto = 0;
            reportosBean.setTasa("0");
            reportosBean.setPremio("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Tasa debe ser un campo numérico..."));
            respuesta = false;
        } else if (Double.parseDouble(reportosBean.getTasa()) <= 0) {
            dImpMovto = 0;
            reportosBean.setTasa("0");
            reportosBean.setPremio("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tasa debe ser mayor que cero..."));
            respuesta = false;
        } else {
            reportosBean.setTasa(formatDecimalImp(Double.parseDouble(reportosBean.getTasa())));
        }

        if (reportosBean.getImporte() == null || "".equals(reportosBean.getImporte())) {

            //Clean
            dImpMovto = 0;

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe del reporto no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(reportosBean.getImporte())) {
            // Clean
            dImpMovto = 0;
            reportosBean.setPremio("0");
            reportosBean.setImporte("0");
            reportosBean.setPrecioEmis(0);
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe del reporto debe ser un campo numérico..."));
            respuesta = false;
        } else if (Double.parseDouble(reportosBean.getImporte()) <= 0) {

            //Clean
            dImpMovto = 0;
            reportosBean.setPremio("0");
            reportosBean.setImporte("0");
            reportosBean.setPrecioEmis(0);

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe del reporto debe ser mayor a cero..."));
            respuesta = false;
        } else {
            reportosBean.setImporte(formatDecimalImp(Double.parseDouble(reportosBean.getImporte())));

            //Calcular_precio_y_establecer_titulos_con_formato
            if (reportosBean.getTitulos() != null && !"".equals(reportosBean.getTitulos())) {
                if (isNumeric(reportosBean.getTitulos())) {
                    if (Integer.parseInt(reportosBean.getTitulos()) != 0) {
                        reportosBean.setPrecioEmis(Double.parseDouble(formatDecimalImp(Double.parseDouble(reportosBean.getImporte()) / Integer.parseInt(reportosBean.getTitulos()))));
                    }
                }
            }
        }
        
        if (sValEmis == null || "".equals(sValEmis) || sValEmis.equals("0")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisión no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getTitulos() == null || "".equals(reportosBean.getTitulos())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos no puede estar vacío..."));
            respuesta = false;
        } else if (!isNumeric(reportosBean.getTitulos())) {
            // Clean
            reportosBean.setTitulos("0");
            reportosBean.setPrecioEmis(0);

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El No Títulos debe ser un campo numérico..."));
            respuesta = false;
        } else if (Integer.parseInt(reportosBean.getTitulos()) <= 0) {

            //Clean
            reportosBean.setTitulos("0");
            reportosBean.setPrecioEmis(0);

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos debe ser mayor que Cero..."));
            respuesta = false;
        } else {

            //Calcular_Precio 
            if (reportosBean.getImporte() != null && !"".equals(reportosBean.getImporte())) {
                if (isDoubleNumeric(reportosBean.getImporte())) {
                    if (Double.parseDouble(reportosBean.getImporte()) != 0) {
                        reportosBean.setPrecioEmis(Double.parseDouble(formatDecimalImp(Double.parseDouble(reportosBean.getImporte()) / Integer.parseInt(reportosBean.getTitulos()))));
                    }
                }
            }

            reportosBean.setTitulos(formatFormatTitInt(Integer.parseInt(reportosBean.getTitulos())));
        }

        if (reportosBean.getPlazo() != null && !reportosBean.getPlazo().equals("")
                && reportosBean.getTasa() != null && !reportosBean.getTasa().equals("")
                && reportosBean.getImporte() != null && !reportosBean.getImporte().equals("")) {
            if (isNumeric(reportosBean.getPlazo()) && isDoubleNumeric(reportosBean.getTasa()) && isDoubleNumeric(reportosBean.getImporte())) {
                CalculaPremio();
                if (Double.parseDouble(reportosBean.getPremio()) >= 0 && Integer.parseInt(reportosBean.getPlazo()) > 0) {
                    dImpMovto = Double.parseDouble(formatDecimalImp(Double.parseDouble(reportosBean.getImporte()) + Double.parseDouble(reportosBean.getPremio())));
                }
            }
        }

        if (reportosBean.getPremio() == null || reportosBean.getPremio().equals("")) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Premio no puede estar vacío"));
            respuesta = false;
        } else if (Double.parseDouble(reportosBean.getPremio()) < 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Premio debe ser mayor o igual a Cero..."));
            respuesta = false;
        }


        if (iSecEmis == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //"Fiduciario", "El campo Secuencial de la Emisión no puede estar vacío..."));
            respuesta = false;
        }

        if (reportosBean.getNomPizarra() == null) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //      new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proporcione Pizarra"));
            respuesta = false;
        }

        if (reportosBean.getSerieEmis() == null) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //      new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proporcione Serie"));
            respuesta = false;
        }

        return respuesta;
    }

    public boolean ValidacionesNegocio() {

        //logger.debug("Ingresa a ValidacionesNegocio()");
        try {

            //_Valida_si_el_contrato_tiene_liquidez			
            if (cTesoreria.onTesoreria_VerificaLiquidez(Double.parseDouble(reportosBean.getImporte()), reportosBean.getFideicomiso(), reportosBean.getSubFiso(),
                    reportosBean.getEntidadFinan(), reportosBean.getCtoInver(), Integer.parseInt(reportosBean.getCustodio()))) {

                if (cTesoreria.onTesoreria_Valida_Instrumento(reportosBean.getIdMercado(), reportosBean.getIdInstrume())) {

                    emisionBean = cTesoreria.onTesoreria_ValidaEmision_Reporto(reportosBean.getIdMercado(), reportosBean.getIdInstrume(), iSecEmis);

                    if (!emisionBean.getStatus().equals("")) {

                        if (emisionBean.getStatus().equals("ACTIVO")) {
                            reportosBean.setCuponVig(String.valueOf(emisionBean.getCupon()));
                            reportosBean.setMoneda(emisionBean.getNumMoneda());

                            AplicaOperacion();
                            RequestContext.getCurrentInstance().update(":formReportos:txtFecMovto");
                        } else {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Emisión no se encuentra Activa"));
                            return false;
                        }
                    } else {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Emisión no existe"));
                        return false;
                    }
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Instrumento no existe"));
                    return false;
                }
            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no tiene Líquidez"));
                reportosBean.setFideicomiso("");
                listaSubContrato = null;
                reportosBean.setSubFiso("");
                this.subFiso = "";
                DeshabilitarComponentes();
                RequestContext.getCurrentInstance().update(":formReportos:txtFecMovto");
                LimpiarTipoValores();
                init();
                return false;
            }

            return true;

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ValidacionesNegocio()";
        }
        return false;
    }

    public void AplicaOperacion() {

        //logger.debug("Ingresa a AplicaOperacion()");
        try {
            String sManejoSubFiso = "";

            if (contratoBean.getTipoAdmon().equals("True")) {
                contratoBean.setTipoAdmon("SI");
            } else {
                contratoBean.setTipoAdmon("NO");
            }

            if (contratoBean.getbSubContrato() == true) {
                sManejoSubFiso = "TRUE";
            } else {
                sManejoSubFiso = "FALSE";
            }

            //Aplica_SP_MD_CPA_PAGREP
            outParameterBean = cTesoreria.onTesoreria_Ejecuta_ReportoPagare(reportosBean.getDiaOpera(), //fecha_valor	
                    FecValorMSA, "LINEA",
                    Integer.parseInt(reportosBean.getFideicomiso()), Integer.parseInt(reportosBean.getSubFiso()),
                    tipoMovimiento, "", //_Redaccion
                    String.valueOf(reportosBean.getTasa()), Integer.parseInt(reportosBean.getPlazo()),
                    reportosBean.getNomPizarra(), reportosBean.getSerieEmis(),
                    Integer.parseInt(reportosBean.getCuponVig()), Integer.parseInt(reportosBean.getTitulos()),
                    reportosBean.getPrecioEmis(), reportosBean.getEntidadFinan(),
                    reportosBean.getIdMercado(), reportosBean.getIdInstrume(),
                    iSecEmis, Integer.parseInt(reportosBean.getCustodio()),
                    reportosBean.getMoneda(), reportosBean.getCtoInver(),
                    Double.parseDouble(reportosBean.getImporte()), FecMovto,
                    String.valueOf(reportosBean.getPremio()),
                    "0", //_Comisión
                    "0", //_Iva_Comisión
                    "0", //_ISR
                    "FALSE", //_Retiene_ISR
                    reportosBean.getTipoCambio(),
                    0, //_Sub_Operación
                    0, //_Ref._CICS
                    contratoBean.getTipoNeg(), contratoBean.getClasificacion(),
                    contratoBean.getAbiertoCerrado(), contratoBean.getTipoAdmon(),
                    contratoBean.getProrrateo(), sManejoSubFiso);

            //_Aplicado
            if (outParameterBean.getiNumFolioContab() > 0) {
                // Set_Message
                int folio = outParameterBean.getbEjecuto();
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento Aplicado correctamente con Folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                this.listaSubContrato = null;
                this.subFiso = "";
                reportosBean.setFideicomiso("");
                reportosBean.setSubFiso("");
                DeshabilitarComponentes();
                LimpiarTipoValores();
                reportosBean.setFolioReporto(folio);
                init();
            } //No_Aplicado
            else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Movimiento No se Aplicó: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " AplicaOperacion()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void DeshabilitarComponentes() {
        //logger.debug("Ingresa a DeshabilitarComponentes()");

        // Clean_Bean
        outParameterBean = new OutParameterBean();
        emisionBean = new EmisionBean();

        // Variable
        FecMovto = null;
        tipoValor = "";
        sCtoInver = "";
        dImpMovto = 0;
        sValEmis = "";
        CtaCheques = "";
        //FecValorMSA = 0;
        iSecEmis = 0;
        NombreMon = "";

        // Buttons
        habilitaSelCtoInv = 0;
        habilitaTxtTasa = 0;
        habilitaTxtTitulos = 0;
        visiblePanelMoneda = 0;
        habilitaSelCustodio = 0;
        habilitaTxtSubFiso = true;
        habilitaTxtContenedor = 0;
        HabilitaPanelInv = true;
        habilitaBotonCancelar = 0;

        // Clean_Campos
        reportosBean.setNomPizarra(null);
        reportosBean.setSerieEmis(null);
        reportosBean.setCuponVig(null);
        reportosBean.setPrecioEmis(0);
        reportosBean.setFolioReporto(0);
        reportosBean.setEntidadFinan(0);
        reportosBean.setPlazo("");
        reportosBean.setImporte("");
        reportosBean.setTasa("");
        reportosBean.setPremio("");
        reportosBean.setCustodio("");
        reportosBean.setTitulos("");
        reportosBean.setCtoInver(0);
        reportosBean.setTipoCambio(1);
        reportosBean.setMoneda(1);
        NombreMon = "MONEDA NACIONAL";
        RequestContext.getCurrentInstance().update(":formReportos:txtFecMovto");

    }

    public void LimpiarTipoValores() {

        //logger.debug("Ingresa a LimpiarTipoValores()");
        mercado = "";
        instrumento = "";
        habilitaSelEmisiones = 0;
        reportosBean.setIdMercado(0);
        reportosBean.setIdInstrume(0);
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static boolean isDoubleNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException Err) {
            //logger.error(Err.getMessage() + "MBHonorarios_isDoubleNumeric()");
            return false;
        }
    }

    // Vencimiento de reportos
    public void onRowSelect_ReportoVenc(SelectEvent event) {
        this.reportosBean = (ConreporBean) event.getObject();
        this.setFolioReporto(reportosBean.getFolioReporto());
        habilitaBotonAmortizar = 1;
    }

    public void amortizarReportos() {
        try {
            if (this.getMensajeConfirmaAccion().equals("Amortizar")) {
                OutParameterBean outParamBean = cTesoreria.onTesoreria_NewValAmortizaReporto(reportosBean, "VENCIMIENTO DE REPORTO");
                if ("1".equals(String.valueOf(outParamBean.getbEjecuto()))) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso terminado con éxito"));
                } else {
                    if (outParamBean.getPsMsgErrOut() != null) {
                        //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "Proceso terminado"));
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso terminado: \n Ocurrio un error al vencer el reporto " + String.valueOf(reportosBean.getFolioReporto()) + " " + String.valueOf(outParamBean.getPsMsgErrOut())));
                    } else {
                        //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "Proceso terminado"));   
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso terminado: \n Ocurrio un error al vencer el reporto ".concat(String.valueOf(reportosBean.getFolioReporto()))));
                    }

                }
            }

            if (this.getMensajeConfirmaAccion().equals("AmortizarAll")) {
                if (this.reportos.size() > 0) {
                    int iResultados = 0;

                    for (ConreporBean reporto : reportos) {
                        OutParameterBean outParamBean = cTesoreria.onTesoreria_NewValAmortizaReporto(reporto, "VENCIMIENTO DE REPORTO");
                        if ("1".equals(String.valueOf(outParamBean.getbEjecuto()))) {
                            iResultados++;
                        }
                    }

                    if (iResultados == reportos.size()) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso terminado: Todos los reportos se han amortizado correctamente"));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso terminado: Se amortizaron " + iResultados + " de " + reportos.size() + " reportos..."));
                    }
                }
            }
        } finally {
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
            this.reportosBean = new ConreporBean();
            initVencReportos();
        }

    }

    public void confirmarAmortizarReporto() {
        resetarMensajesVencReportos();
        this.setMensajeConfirmaUsuario(usuarioNombre);
        this.setMensajeConfirmaMensaje1("¿Confirmar vencimiento del reporto: " + reportosBean.getFolioReporto() + "?");
        this.setMensajeConfirmaOrigen("REPORTOS");
        this.setMensajeConfirmaAccion("Amortizar");
        if (cTesoreria.onTesoreria_FunnChecFechVal(FecValor, reportosBean.getFechaVencimiento()) == 1) {
            this.setMensajeConfirmaMensaje2("¿Está seguro de querer aplicar contablemente con fecha valor?");
        }
        RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
    }

    public void confirmarAmortizarAllReportos() {
        resetarMensajesVencReportos();
        this.setMensajeConfirmaUsuario(usuarioNombre);
        this.setMensajeConfirmaMensaje1("¿Confirma la amortización de todos los Reportos y Pagarés?");
        this.setMensajeConfirmaOrigen("REPORTOS");
        this.setMensajeConfirmaAccion("AmortizarAll");
        this.setMensajeConfirmaMensaje2(null);
        RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
    }

    private void resetarMensajesVencReportos() {
        this.setMensajeConfirmaUsuario(null);
        this.setMensajeConfirmaMensaje1(null);
        this.setMensajeConfirmaOrigen(null);
        this.setMensajeConfirmaAccion(null);
        this.setMensajeConfirmaMensaje2(null);
    }

    private synchronized String formatDate(Date date) throws ParseException {
        return simpleDateFormat2.format(date);
    }

    private synchronized Date parseDate(String date) throws ParseException {
        return simpleDateFormat2.parse(date);
    }

    private synchronized String formatFormatTitInt(Integer entero) {
        return decimalFormatTit.format(entero);
    }

    private synchronized String formatDecimalImp(Double decimal) {
        return decimalFormatImp.format(decimal);
    }

}