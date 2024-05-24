/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBEntradaSalida.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

//Imports_Statics
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
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
//Imports
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import scotiaFid.bean.CargaInterfazBean;
import scotiaFid.bean.CompraVentaBean;
import scotiaFid.bean.ContratoBean;
import scotiaFid.bean.EmisionBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.PosicionBean;
import scotiaFid.dao.CFecha;
import scotiaFid.dao.CHonorarios;
import scotiaFid.dao.CTesoreria;
import scotiaFid.util.LogsContext;
import scotiaFid.util.CValidacionesUtil;

//Class
@ManagedBean(name = "mbEntradaSalida")
@ViewScoped
public class MBEntradaSalida implements Serializable {

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
    private CompraVentaBean entradaSalidaBean = new CompraVentaBean();
    // -----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean = new FechaContableBean();
    // -----------------------------------------------------------------------------
    private PosicionBean posicionBean = new PosicionBean();
    // -----------------------------------------------------------------------------
    private EmisionBean emisionBean = new EmisionBean();
    // -----------------------------------------------------------------------------
    private ContratoBean contratoBean = new ContratoBean();
    // -----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    // -----------------------------------------------------------------------------
    private CargaInterfazBean cargaInterfazBean = new CargaInterfazBean();
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    private static final Logger logger = LogManager.getLogger(MBEntradaSalida.class);
    // -----------------------------------------------------------------------------
    private Map<String, String> ctoInversion = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> tipoValores = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> custodio = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> emisiones = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> monedas = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private List<PosicionBean> posicion = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<CargaInterfazBean> CargaInterfaz = new ArrayList<>();
    // --------------------------------------------------------------------------
    private List<String> listaSubContrato = new ArrayList<>();
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F O R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    //DecimalFormat decimalFormatPrec = new DecimalFormat("####0.00#########");

    private static final DecimalFormat decimalFormatPrec = new DecimalFormat("####0.00");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatTit = new DecimalFormat("#############");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatImp = new DecimalFormat("############0.00");
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
    //_Habilitar_Componentes
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtSubFiso;
    // -----------------------------------------------------------------------------
    private int habilitaSelCtoInv;
    // -----------------------------------------------------------------------------
    private int habilitaSelTipoValor;
    // -----------------------------------------------------------------------------
    private int habilitaSelEmisiones;
    // -----------------------------------------------------------------------------
    private int habilitaSelCustodio;
    // -----------------------------------------------------------------------------
    private int habilitaBotonCancelar;
    // -----------------------------------------------------------------------------
    private int habilitaTxtTitulos;
    // -----------------------------------------------------------------------------
    private int habilitaTxtPrecio;
    // -----------------------------------------------------------------------------
    private int visiblePanelMoneda;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelInv;
    // -----------------------------------------------------------------------------
    private int visiblePanelPosicion;
    // -----------------------------------------------------------------------------
    //_Otros_campos_de_pantalla
    // -----------------------------------------------------------------------------
    private String tipoMovimiento;
    // -----------------------------------------------------------------------------
    private Date FecValor;
    // -----------------------------------------------------------------------------
    private int FecValorMSA;
    // -----------------------------------------------------------------------------
    private String tipoValor;
    // -----------------------------------------------------------------------------
    private int IdMercado;
    // -----------------------------------------------------------------------------
    private String mercado;
    // -----------------------------------------------------------------------------
    private int IdInstrumento;
    // -----------------------------------------------------------------------------
    private String instrumento;
    // -----------------------------------------------------------------------------
    private int iSecEmis;
    // -----------------------------------------------------------------------------
    private String NombreMon;
    // -----------------------------------------------------------------------------
    private int Intermed;
    // -----------------------------------------------------------------------------
    private int TipoIntermed;
    // -----------------------------------------------------------------------------
    private String sCtoInver;
    // -----------------------------------------------------------------------------
    private String sValEmis;
    // -----------------------------------------------------------------------------
    private String sRedaccion;
    // -----------------------------------------------------------------------------
    private int iDia = 0;
    // -----------------------------------------------------------------------------
    private int iMes = 0;
    // -----------------------------------------------------------------------------
    private int iAnio = 0;
    // -----------------------------------------------------------------------------
    private int iRegistros = 0;
    // -----------------------------------------------------------------------------
    private double dCorrectos = 0;
    // -----------------------------------------------------------------------------
    private int iIncorrectos = 0;
    // -----------------------------------------------------------------------------
    private String sNomArchivo;
    // -----------------------------------------------------------------------------
    private UploadedFile file;
    // -----------------------------------------------------------------------------
    private String subFiso;
    // -----------------------------------------------------------------------------
    private String sTipoValua;
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    CFecha cFecha = new CFecha();
    // -----------------------------------------------------------------------------
    CTesoreria cTesoreria = new CTesoreria();
    // -----------------------------------------------------------------------------
    CHonorarios cHonorarios = new CHonorarios();

    //Funciones privadas estaticas
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

    public CompraVentaBean getEntradaSalidaBean() {
        return entradaSalidaBean;
    }

    public void setEntradaSalidaBean(CompraVentaBean entradaSalidaBean) {
        this.entradaSalidaBean = entradaSalidaBean;
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

    public OutParameterBean getOutParameterBean() {
        return outParameterBean;
    }

    public void setOutParameterBean(OutParameterBean outParameterBean) {
        this.outParameterBean = outParameterBean;
    }

    public CargaInterfazBean getCargaInterfazBean() {
        return cargaInterfazBean;
    }

    public void setCargaInterfazBean(CargaInterfazBean cargaInterfazBean) {
        this.cargaInterfazBean = cargaInterfazBean;
    }

    public String getTipomovimiento() {
        return tipoMovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipoMovimiento = tipomovimiento;
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

    public Map<String, String> getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(Map<String, String> emisiones) {
        this.emisiones = emisiones;
    }

    public Map<String, String> getMoneda() {
        return monedas;
    }

    public void setMoneda(Map<String, String> monedas) {
        this.monedas = monedas;
    }

    public List<PosicionBean> getPosicion() {
        return posicion;
    }

    public void setPosicion(List<PosicionBean> posicion) {
        this.posicion = posicion;
    }

    public List<CargaInterfazBean> getCargaInterfaz() {
        return CargaInterfaz;
    }

    public void setCargaInterfaz(List<CargaInterfazBean> cargaInterfaz) {
        CargaInterfaz = cargaInterfaz;
    }

    public List<String> getListaSubContrato() {
        return listaSubContrato;
    }

    public void setListaSubContrato(List<String> listaSubContrato) {
        this.listaSubContrato = listaSubContrato;
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

    public int getIdMercado() {
        return IdMercado;
    }

    public void setIdMercado(int idMercado) {
        this.IdMercado = idMercado;
    }

    public String getMercado() {
        return mercado;
    }

    public void setMercado(String mercado) {
        this.mercado = mercado;
    }

    public int getIdInstrumento() {
        return IdInstrumento;
    }

    public void setIdInstrumento(int idInstrumento) {
        this.IdInstrumento = idInstrumento;
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

    public int getTipoIntermed() {
        return TipoIntermed;
    }

    public void setTipoIntermed(int tipoIntermed) {
        this.TipoIntermed = tipoIntermed;
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

    public String getsRedaccion() {
        return sRedaccion;
    }

    public void setsRedaccion(String sRedaccion) {
        this.sRedaccion = sRedaccion;
    }

    public int getiDia() {
        return iDia;
    }

    public void setiDia(int iDia) {
        this.iDia = iDia;
    }

    public int getiMes() {
        return iMes;
    }

    public void setiMes(int iMes) {
        this.iMes = iMes;
    }

    public int getiAnio() {
        return iAnio;
    }

    public void setiAnio(int iAnio) {
        this.iAnio = iAnio;
    }

    public String getSubFiso() {
        return subFiso;
    }

    public void setSubFiso(String subFiso) {
        this.subFiso = subFiso;
    }

    //_Habilita
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

    public int getHabilitaSelTipoValor() {
        return habilitaSelTipoValor;
    }

    public void setHabilitaSelTipoValor(int habilitaSelTipoValor) {
        this.habilitaSelTipoValor = habilitaSelTipoValor;
    }

    public int getHabilitaSelEmisiones() {
        return habilitaSelEmisiones;
    }

    public void setHabilitaSelEmisiones(int habilitaSelEmisiones) {
        this.habilitaSelEmisiones = habilitaSelEmisiones;
    }

    public int getHabilitaSelCustodio() {
        return habilitaSelCustodio;
    }

    public void setHabilitaSelCustodio(int habilitaSelCustodio) {
        this.habilitaSelCustodio = habilitaSelCustodio;
    }

    public int getHabilitaTxtTitulos() {
        return habilitaTxtTitulos;
    }

    public void setHabilitaTxtTitulos(int habilitaTxtTitulos) {
        this.habilitaTxtTitulos = habilitaTxtTitulos;
    }

    public int getHabilitaTxtPrecio() {
        return habilitaTxtPrecio;
    }

    public void setHabilitaTxtPrecio(int habilitaTxtPrecio) {
        this.habilitaTxtPrecio = habilitaTxtPrecio;
    }

    public int getHabilitaBotonCancelar() {
        return habilitaBotonCancelar;
    }

    public void setHabilitaBotonCancelar(int habilitaBotonCancelar) {
        this.habilitaBotonCancelar = habilitaBotonCancelar;
    }

    public int getHabilitaPanelMoneda() {
        return visiblePanelMoneda;
    }

    public void setHabilitaPanelMoneda(int visiblePanelMoneda) {
        this.visiblePanelMoneda = visiblePanelMoneda;
    }

    public int getHabilitaPanelPosicion() {
        return visiblePanelPosicion;
    }

    public void setHabilitaPanelPosicion(int visiblePanelPosicion) {
        this.visiblePanelPosicion = visiblePanelPosicion;
    }

    public void setHabilitaPanelInv(boolean habilitaPanelInv) {
        HabilitaPanelInv = habilitaPanelInv;
    }

    public boolean getHabilitaPanelInv() {
        return HabilitaPanelInv;
    }

    public PosicionBean getPosicionBean() {
        return posicionBean;
    }

    public void setPosicionBean(PosicionBean posicionBean) {
        this.posicionBean = posicionBean;
    }

    public EmisionBean getEmisionBean() {
        return emisionBean;
    }

    public void setEmisionBean(EmisionBean emisionBean) {
        this.emisionBean = emisionBean;
    }

    //_Carga_Archivo_Masivo	
    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public int getiRegistros() {
        return iRegistros;
    }

    public void setiRegistros(int iRegistros) {
        this.iRegistros = iRegistros;
    }

    public double getdCorrectos() {
        return dCorrectos;
    }

    public void setdCorrectos(double dCorrectos) {
        this.dCorrectos = dCorrectos;
    }

    public int getiIncorrectos() {
        return iIncorrectos;
    }

    public void setiIncorrectos(int iIncorrectos) {
        this.iIncorrectos = iIncorrectos;
    }

    public String getsNomArchivo() {
        return sNomArchivo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setsNomArchivo(String sNomArchivo) {
        this.sNomArchivo = sNomArchivo;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public MBEntradaSalida() {
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
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                // Mensajes_Error
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.MBEntradaSalida.";

                // Tipo_Movimiento_Default_Entrada
                tipoMovimiento = "E";

                // Deshabilita_paneles
                visiblePanelMoneda = 1;
                visiblePanelPosicion = 1;
                HabilitaPanelInv = true;
                habilitaTxtSubFiso = true;

                // Set_Bean
                this.entradaSalidaBean = new CompraVentaBean();

                // Get_Fecha_Contable
                fechaContableBean = cTesoreria.onTesoreria_GetFechaContable();

                // Format_Fecha_Contable
                fechaContableBean.setFechaContable(formatDate(fechaContableBean.getFecha()));

                // Agregar_fecha_contable_como_fecha_valor
                FecValor = fechaContableBean.getFecha();
                entradaSalidaBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                //oGeneraLog.onGeneraLog(MBEntradaSalida.class, "0E", "INFO", "00", "00", "Se accedio a la pantalla de: ".concat(peticionURL.getRequestURI().replace(".sb", "")), "00", "20", "00");
            }
        } catch (DateTimeException | IOException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Construct()";
        }
    }

    public void select_FecVal() {
        FecValorMSA = 0;

        try {
            if (FecValor != null) {

                //Default_fec_contable
                entradaSalidaBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                // La_fecha_valor_es_menor_o_igual_a_la_fecha_contable
                if (FecValor.before(parseDate(fechaContableBean.getFechaContable()))
                        || FecValor.equals(parseDate(fechaContableBean.getFechaContable()))) {

                    if (cFecha.onFecha_DiaHabil(FecValor) == 1) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha inválida, debe ser un dia hábil..."));
                        FecValor = fechaContableBean.getFecha();
                        return;
                    } else {
                        // Validar_si_permite_aplicar_con_fecha_Valor
                        int iMesVal = 0, iYearVal = 0, iMesCont = 0, iYearCont = 0;

                        iMesVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                        iMesCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                        if (iMesVal != iMesCont || iYearVal != iYearCont) {
                            if (cFecha.onFecha_FecValEntradaSalidaTesoreria()) {
//							iFecValor_Param = cFecha.onFecha_FecValTesoreria();
//							// Valida_Mes_Abierto_y_Permite_Tesoreria
//							if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 1 
//								&& iFecValor_Param == 1) {

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
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No puede aplicar movimiento con esa fecha..."));
                                FecValor = fechaContableBean.getFecha();
                                return;
                            }
                        } else {
                            if (FecValor.before(parseDate(fechaContableBean.getFechaContable()))) {
                                RequestContext.getCurrentInstance().execute("dlgFecVal.show();");
                                return;
                            }
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha inválida, no puede ser mayor a la fecha contable..."));
                    FecValor = fechaContableBean.getFecha();
                    return;
                }

                // Asigna_el_valor_de_la_fecha_a_la_variable_Bean
                entradaSalidaBean.setDiaOpera(formatDate(FecValor));

                // Determina_Moneda
                DeterminaMoneda();

            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La fecha es incorrecta..."));
                entradaSalidaBean.setDiaOpera("");
                return;
            }

        } catch (DateTimeException | NumberFormatException | ParseException | SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_FecVal()";
        }
    }

    public void ConfirmaFecValor() {
        try {
            entradaSalidaBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");

            // Determina_Moneda
            DeterminaMoneda();
        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en ConfirmaFecValor()");
        }
    }

    public void NiegaFecValor() {
        try {

            FecValorMSA = 0;
            FecValor = fechaContableBean.getFecha();
            entradaSalidaBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en NiegaFecValor()");
        }
    }

    public void checkFideicomisoCompraVenta() {
        try {

            // Disabled
            DeshabilitarComponentes();
            contratoBean = new ContratoBean();
            entradaSalidaBean.setFideicomiso(entradaSalidaBean.getFideicomiso().trim());
            listaSubContrato = null;
            entradaSalidaBean.setSubFiso("");
            this.subFiso = "";

            // Check_Fideicomiso_esta_vacío
            if ("".equals(entradaSalidaBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(entradaSalidaBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                entradaSalidaBean.setFideicomiso("");
                return;
            }

            if (Integer.parseInt(entradaSalidaBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es inválido..."));
                entradaSalidaBean.setFideicomiso("");
                return;
            }

            // Valida_contrato_existente_y_Activo
            contratoBean = cTesoreria.onTesoreria_checkFideicomiso(Integer.parseInt(entradaSalidaBean.getFideicomiso()));

            // Valida_permitir_operaciones_Inversion_y_no_este_bloqueado
            if (contratoBean.getContrato() == Integer.parseInt(entradaSalidaBean.getFideicomiso())
                    && cTesoreria.onTesoreria_CtoBloqueado(contratoBean.getContrato(), "I")) {
                habilitaBotonCancelar = 1;
                listaSubContrato = cTesoreria.onTesoreria_ListadoSubContratoSrv(Integer.parseInt(entradaSalidaBean.getFideicomiso()));

                if (contratoBean.getbSubContrato()) {

                    habilitaTxtSubFiso = false;
                    entradaSalidaBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(entradaSalidaBean.getFideicomiso(),
                            entradaSalidaBean.getSubFiso(), Intermed, tipoMovimiento);

                    // Validar_si_trae_resultados
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

                    habilitaTxtSubFiso = false;

                    // Establece_SubFiso
                    habilitaBotonCancelar = 1;
                    entradaSalidaBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(entradaSalidaBean.getFideicomiso(),
                            entradaSalidaBean.getSubFiso(), Intermed, tipoMovimiento);

                    // Validar_si_trae_resultados
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
                habilitaSelCtoInv = 0;

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                entradaSalidaBean.setFideicomiso("");
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " checkFideicomisoCompraVenta()";
        }
    }

    public void checkSubFiso() {
        try {
            String[] SubFisoSeleccion = subFiso.split(".-");
            String esSubFiso = SubFisoSeleccion[0];
            entradaSalidaBean.setSubFiso(esSubFiso.trim());
            entradaSalidaBean.setSubFiso(entradaSalidaBean.getSubFiso().trim());

            //_Check_Fideicomiso_esta_vacío
            if ("".equals(entradaSalidaBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso es un dato necesario..."));
                return;
            }

            // Check_SubFideicomiso_vacío
            if ("".equals(entradaSalidaBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(entradaSalidaBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                entradaSalidaBean.setSubFiso("");
                return;
            }

            // Check_SubFiso_vacío
            if (!entradaSalidaBean.getSubFiso().equals("")) {
                // Disabled
                DeshabilitarComponentes();

                //_Valida_el_Sub_contrato_existe_Activo
                //_Valida_que_permita_operaciones_de_Inversión_y_no_este_bloqueado
                //_Parametros:_DatosContrato,_SubFiso,_sTipoBloque
                if (cTesoreria.onTesoreria_VerificaSubContratoSrv(contratoBean, Integer.parseInt(entradaSalidaBean.getSubFiso()), "I")) {

                    habilitaTxtSubFiso = false;
                    habilitaBotonCancelar = 1;

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(entradaSalidaBean.getFideicomiso(), entradaSalidaBean.getSubFiso(), entradaSalidaBean.getEntidadFinan(), tipoMovimiento);

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
                } else {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso no existe o no está activo..."));
                    entradaSalidaBean.setSubFiso("");
                    return;
                }
            } else {
                entradaSalidaBean.setSubFiso("0");
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " checkSubFiso()";
        }
    }

    public void select_CtoInv() {
        try {
            if (sCtoInver == null) {
                return;
            }

            if (sCtoInver.equals("0") || sCtoInver.equals("")) {

                // Limpia_controles
                // Variables
                posicion = null;
                tipoValor = "";
                IdMercado = 0;
                IdInstrumento = 0;
                NombreMon = "";
                mercado = "";
                instrumento = "";
                iSecEmis = 0;
                sCtoInver = "";
                sValEmis = "";

                //_Botones
                habilitaSelTipoValor = 0;
                habilitaSelEmisiones = 0;
                habilitaTxtTitulos = 0;
                habilitaTxtPrecio = 0;
                habilitaSelCustodio = 0;
                visiblePanelMoneda = 1;

                // Clean_Campos
                entradaSalidaBean.setCustodio(0);
                entradaSalidaBean.setCtoInver(0);
                entradaSalidaBean.setEntidadFinan(0);
                entradaSalidaBean.setNomPizarra("");
                entradaSalidaBean.setSerieEmis("");
                entradaSalidaBean.setCuponVig("");
                entradaSalidaBean.setTitulos("0");
                entradaSalidaBean.setImporte(0);
                entradaSalidaBean.setPrecioEmis("0");
                entradaSalidaBean.setMoneda(0);

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "Debe seleccionar un Contrato de Inversión..."));
                return;
            } else {

                String[] Id_CtoInv;
                String CtoInv = "", NumInterm = "", sTipoIntermed = "";

                //_Limpia
                // Variables
                tipoValor = "";
                IdMercado = 0;
                IdInstrumento = 0;
                NombreMon = "";
                mercado = "";
                instrumento = "";
                iSecEmis = 0;
                sValEmis = "";

                //_Botones
                habilitaSelTipoValor = 0;
                habilitaSelEmisiones = 0;
                habilitaTxtTitulos = 0;
                habilitaTxtPrecio = 0;
                visiblePanelMoneda = 1;

                // Clean_Campos
                entradaSalidaBean.setNomPizarra("");
                entradaSalidaBean.setSerieEmis("");
                entradaSalidaBean.setCuponVig("");
                entradaSalidaBean.setTitulos("0");
                entradaSalidaBean.setImporte(0);
                entradaSalidaBean.setPrecioEmis("0");
                entradaSalidaBean.setMoneda(0);
                entradaSalidaBean.setCustodio(0);

                // Recupera_los_valores_de_la_lista_de_Contrato_de_Inversión
                Id_CtoInv = sCtoInver.split("/");

                // Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                CtoInv = Id_CtoInv[0];
                NumInterm = Id_CtoInv[1]; // Intermediario_o_Entidad_Financiera
                sTipoIntermed = Id_CtoInv[2];

                entradaSalidaBean.setCtoInver(Double.parseDouble(CtoInv));
                entradaSalidaBean.setEntidadFinan(Integer.parseInt(NumInterm));
                TipoIntermed = Integer.parseInt(sTipoIntermed);

                //_/Valida_Salida
                //VALIDA_INTERMEDIARIO_EN_SALIDAS
                if (tipoMovimiento.equals("S")) {
                    sTipoValua = cTesoreria.onTesoreria_ConsultaTipoValua(entradaSalidaBean.getEntidadFinan());

                    if (sTipoValua.equals("")) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Fiduciario", "El Intermediario es Incorrecto: " + entradaSalidaBean.getEntidadFinan() + "..."));
                        return;
                    } else {
                        if (FecValorMSA == 1 && sTipoValua.equals("COSTO PROMEDIO")) {
                            // Set_Message
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Fiduciario", "No se permite Fecha Valor para Intermediarios con Costo Promedio..."));
                            return;
                        }
                    }
                }
                //_/Valida_Salida

                //_Condicion_para_seleccionar_el_Tipo_de_Valor
                //_Si_es_Bancario_(1)_o_Bursatil_(0)
                ArrayList<Integer> sComplemento = new ArrayList<Integer>();

                if (TipoIntermed == 1) {
                    sComplemento.add(51);
                    sComplemento.add(52);
                    sComplemento.add(53);
                    sComplemento.add(54);
                    sComplemento.add(56);
                }

                // Agrega_Casa_de_Bolsa_como_Custodio_en_el_Intermediario_Bancario_Scotia
                ArrayList<Integer> sComplementoCust = new ArrayList<Integer>();
                sComplementoCust.add(4);
                sComplementoCust.add(5);

                habilitaSelTipoValor = 1;
                tipoValores = cTesoreria.onTesoreria_GetTipoValor(sComplemento);

                // Determinar_Custodio
                habilitaSelCustodio = 1;
                custodio = cTesoreria.onTesoreria_GetCustodio(386, sComplementoCust);

                //_Falta_Valida_si_es_Salida
                if (getTipomovimiento().equals("S")) {
                    posicion = null;
                    //_Determinar_Posicion
                    posicion = cTesoreria.onTesoreria_PosicionSalidas(entradaSalidaBean.getFideicomiso(), entradaSalidaBean.getSubFiso(),
                            entradaSalidaBean.getEntidadFinan(), entradaSalidaBean.getCtoInver());
                    habilitaSelCustodio = 0;
                }
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_CtoInv()";
        }
    }

    public void select_TipoValor() {
        try {
            // Limpia_controles
            //_Botones
            sValEmis = "";
            habilitaSelEmisiones = 0;
            habilitaTxtTitulos = 0;
            habilitaTxtPrecio = 0;
            visiblePanelMoneda = 1;
            visiblePanelPosicion = 1;

            // Clean_Campos
            entradaSalidaBean.setNomPizarra("");
            entradaSalidaBean.setSerieEmis("");
            entradaSalidaBean.setCuponVig("");
            entradaSalidaBean.setTitulos("0");
            entradaSalidaBean.setImporte(0);
            entradaSalidaBean.setPrecioEmis("0");
            entradaSalidaBean.setMoneda(0);

            if (tipoValor.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un Tipo de Valor..."));
                mercado = "";
                IdMercado = 0;
                instrumento = "";
                IdInstrumento = 0;
                return;
            } else {

                String[] LabelTipoValor;
                IdMercado = 0;
                IdInstrumento = 0;

                // Seleccionar_Tipo_Valor
                for (Map.Entry<String, String> o : tipoValores.entrySet()) {

                    if (tipoValor.equals(o.getKey())) {

                        LabelTipoValor = o.getValue().split("/");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        mercado = LabelTipoValor[1];
                        instrumento = LabelTipoValor[2];

                        IdMercado = Integer.parseInt(o.getKey().substring(0, 1));
                        IdInstrumento = Integer.parseInt(o.getKey().substring(1, 4));
                    }
                }

                if (IdMercado >= 0 && IdInstrumento >= 0) {
                    // Carga_Emisiones
                    habilitaSelEmisiones = 1;
                    emisiones = cTesoreria.onTesoreria_GetEmisionesEntradaSalida(IdMercado, IdInstrumento);
                }

                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_TipoValor()";
        }
    }

    public void select_Emisiones() {

        try {
            if (sValEmis.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisiones no puede estar vacío..."));
                entradaSalidaBean.setMoneda(0);
                entradaSalidaBean.setNomPizarra("");
                entradaSalidaBean.setSerieEmis("");
                entradaSalidaBean.setCuponVig("");
                return;
            } else {
                String[] LabelEmisora;
                String[] LabelMoneda;

                // Seleccionar_Emisora
                for (Map.Entry<String, String> o : emisiones.entrySet()) {

                    if (sValEmis.equals(o.getKey())) {
                        LabelEmisora = o.getValue().split(" / ");
                        LabelMoneda = o.getKey().split("/");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        entradaSalidaBean.setMoneda(Integer.parseInt(LabelMoneda[0].trim()));
                        entradaSalidaBean.setNomPizarra(LabelEmisora[0].trim());
                        entradaSalidaBean.setSerieEmis(LabelEmisora[1].trim());
                        entradaSalidaBean.setCuponVig(LabelEmisora[2].trim());

                        //_Habilita_precio,_importe_y_titulos
                        habilitaTxtTitulos = 1;
                        habilitaTxtPrecio = 1;
                    }
                }

                // Determina_Moneda
                DeterminaMoneda();

                return;
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Emisiones()";
        }
    }

    public void DeterminaMoneda() {
        // Determina_Moneda 
        entradaSalidaBean.setTipoCambio(0);

        try {

            FecValor = parseDate(entradaSalidaBean.getDiaOpera());
            iDia = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
            iMes = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            iAnio = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

            monedas = cTesoreria.onTesoreria_GetTipoCambioEntradaSalida(iDia, iMes, iAnio,
                    entradaSalidaBean.getMoneda());

            if (entradaSalidaBean.getMoneda() > 1) {

                for (Map.Entry<String, String> o : monedas.entrySet()) {
                    // _Recuperar_valor_de_Moneda_y_Tipo_de_Cambio
                    entradaSalidaBean.setMoneda(Integer.parseInt(o.getKey()));

                    String[] LabelTipoCamb = o.getValue().split("/");
                    entradaSalidaBean.setTipoCambio(CValidacionesUtil.validarDouble(LabelTipoCamb[0]));
                    NombreMon = LabelTipoCamb[1];
                }

                visiblePanelMoneda = 0;
            } else {
                entradaSalidaBean.setMoneda(Integer.parseInt("1"));
                entradaSalidaBean.setTipoCambio(Double.parseDouble("1"));
                NombreMon = "MONEDA NACIONAL";
                visiblePanelMoneda = 0;
            }

        } catch (NumberFormatException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " DeterminaMoneda()";
        }
    }

    public void onRowSelect_Posicion(SelectEvent event) {

        // Get_Posicion
        this.posicionBean = (PosicionBean) event.getObject();

        entradaSalidaBean.setNomPizarra(posicionBean.getPizarra());
        entradaSalidaBean.setSerieEmis(posicionBean.getSerieEmis());
        entradaSalidaBean.setCuponVig(String.valueOf(posicionBean.getCuponVig()));
        IdMercado = posicionBean.getTipoMerca();
        IdInstrumento = posicionBean.getNumInstrumento();
        entradaSalidaBean.setPrecioEmis(String.valueOf(posicionBean.getPosDisp())); //_Precio

        iSecEmis = posicionBean.getSecEmisora(); // secuencia_emisora
        entradaSalidaBean.setMoneda(posicionBean.getNumMoneda()); //_moneda 

        //_Selecciona_el_Custodio_de_la_Posición
        habilitaSelCustodio = 0;
        if (!posicionBean.getNomCustodio().equals("")) {

            //_Recuperar_valor_de_Clave_de_Custodio_y_Nombre
            entradaSalidaBean.setCustodio(posicionBean.getClaveGarantia()); //Temporal_almacena_Nomb_Custodio
            custodio.put(String.valueOf(entradaSalidaBean.getCustodio()), posicionBean.getNomCustodio());

        } else {
            entradaSalidaBean.setCustodio(0);
        }

        //_Habilita_precio_y_títulos
        habilitaTxtTitulos = 1;

        //habilitaTxtPrecio = 1; Deshabilitamos precio del titulo
        return;
    }

    public void select_Custodio() {

        // Validate_Servicio
        if (entradaSalidaBean.getCustodio() == 0) {

            entradaSalidaBean.setCustodio(0);

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un Custodio..."));
            return;
        }
    }

    public void checkTipoCambio() {

        // Check_Is_Numeric
        if (entradaSalidaBean.getTipoCambio() <= 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de cambio para este dia..."));
            return;
        }
    }

    public void DeshabilitaTipoCambio() {

        NombreMon = "";
        habilitaTxtPrecio = 0;
        habilitaTxtTitulos = 0;
        visiblePanelMoneda = 1; // DesHabilita
    }

    public void SelectTipoMov() {

        // _Botones
        habilitaSelTipoValor = 0;
        habilitaSelEmisiones = 0;
        habilitaTxtTitulos = 0;
        habilitaTxtPrecio = 0;
        habilitaSelCustodio = 0;
        visiblePanelMoneda = 1;

        // Clean_Campos
        entradaSalidaBean.setTitulos("0");
        entradaSalidaBean.setImporte(0);
        entradaSalidaBean.setMoneda(0);
        entradaSalidaBean.setCustodio(0);
        entradaSalidaBean.setNomPizarra("");
        entradaSalidaBean.setSerieEmis("");
        entradaSalidaBean.setCuponVig("");
        entradaSalidaBean.setPrecioEmis("0");
        entradaSalidaBean.setEntidadFinan(0);
        sRedaccion = new String();

        // _Compra
        if (getTipomovimiento().equals("E")) {
            visiblePanelPosicion = 1; // _Deshabilita
            HabilitaPanelInv = true;
            posicionBean = new PosicionBean();
            if (entradaSalidaBean.getFideicomiso() != null && !"".equals(entradaSalidaBean.getFideicomiso())) {
                select_CtoInv();
            }
        }

        // _Venta
        if (getTipomovimiento().equals("S")) {
            visiblePanelPosicion = 0; // _Habilita
            HabilitaPanelInv = false;

            if (entradaSalidaBean.getFideicomiso() != null && !"".equals(entradaSalidaBean.getFideicomiso())) {
                select_CtoInv();
            }

            if (posicionBean.getFideicomiso() != null) {
                RequestContext.getCurrentInstance().execute("dtPosicion.unselectAllRows();");
                RequestContext.getCurrentInstance().execute("dtPosicion.clearSelection();");
            }
        }

        // _Limpia
        // _Variables
        tipoValor = "";
        tipoValor = "";
        IdMercado = 0;
        IdInstrumento = 0;
        mercado = "";
        instrumento = "";
        iSecEmis = 0;
        sValEmis = "";
    }

    public void bCancelar() {
        RequestContext.getCurrentInstance().execute("dlgSuspender.show();");
    }

    public void bConfirmaCancelacion() {
        try {
            tipoMovimiento = "E";
            HabilitaPanelInv = true;
            DeshabilitarComponentes();
            entradaSalidaBean.setFideicomiso("");
            listaSubContrato = null;
            entradaSalidaBean.setSubFiso("");
            entradaSalidaBean.setDiaOpera(fechaContableBean.getFechaContable());
            init();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/tesoreriaEntradaSalida.sb");
        } catch (IOException ex) {
            mensajeError = "Error al redireccionar: " + ex.getMessage();
        }
    }

    public void bAceptaEntSal() {

        if (ValidacionesPantalla()) {
            ValidacionesNegocio();
        }
    }

    public boolean ValidacionesPantalla() {

        boolean respuesta = true;
        // _Check_Fideicomiso_esta_vacío
        if ("".equals(entradaSalidaBean.getFideicomiso())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
            respuesta = false;
        }

        // _Check_Fideicomiso_esta_vacío
        if ("".equals(entradaSalidaBean.getSubFiso()) || entradaSalidaBean.getSubFiso() == null) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Sub Fiso. no puede estar vacío..."));
            respuesta = false;
        }

        // _Check_Cto_Inversion_esta_vacío
        if (sCtoInver == null || "".equals(sCtoInver) || entradaSalidaBean.getCtoInver() == 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
            respuesta = false;
        }

        // _Intermediario
        if (entradaSalidaBean.getEntidadFinan() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //      "Fiduciario", "El campo Intermediario no puede estar vacío..."));
            respuesta = false;
        }

        if (tipoMovimiento.equals("S")) {

            if (posicion.isEmpty()) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proporcione Custodio..."));
                return false;
            } else if ("".equals(entradaSalidaBean.getNomPizarra()) && "".equals(entradaSalidaBean.getSerieEmis())
                    && "".equals(entradaSalidaBean.getCuponVig())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proporcione Custodio..."));
                respuesta = false;
            }

        } else {
            if (tipoValor == null || tipoValor.equals("0") || "".equals(tipoValor)) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Valor no puede estar vacío..."));
                respuesta = false;
            }

            if (IdMercado == 0) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mercado no puede estar vacío..."));
                respuesta = false;
            }

            if (IdInstrumento == 0) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                //"Fiduciario", "El campo Instrumento no puede estar vacío..."));
                respuesta = false;
            }

            if (sValEmis == null || sValEmis.equals("0") || "".equals(sValEmis)) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisiones no puede estar vacío..."));
                respuesta = false;
            }

            if (entradaSalidaBean.getNomPizarra() == null || "".equals(entradaSalidaBean.getNomPizarra())) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Pizarra no puede estar vacío..."));
                respuesta = false;
            }

            if (entradaSalidaBean.getSerieEmis() == null || "".equals(entradaSalidaBean.getSerieEmis())) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //      new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Serie no puede estar vacío..."));
                respuesta = false;
            }

            if (entradaSalidaBean.getCuponVig() == null || "".equals(entradaSalidaBean.getCuponVig())) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Cupón no puede estar vacío..."));
                respuesta = false;
            }
        }

        // Write_Log
        if (entradaSalidaBean.getTitulos() == null || "".equals(entradaSalidaBean.getTitulos())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos no puede estar vacío..."));
            respuesta = false;
        } else {
            if (!isNumeric(entradaSalidaBean.getTitulos())) {
                // Clean
                entradaSalidaBean.setImporte(0);
                entradaSalidaBean.setTitulos("0");
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El No. Títulos debe ser un campo numérico..."));
                respuesta = false;
            } else if (Integer.parseInt(entradaSalidaBean.getTitulos()) <= 0) {
                // Clean_Importe
                entradaSalidaBean.setImporte(0);
                entradaSalidaBean.setTitulos("0");

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos debe ser mayor a 0..."));
                respuesta = false;
            } else {
                // Formateo_Titulos
                entradaSalidaBean.setTitulos(formatFormatTitInt(Integer.parseInt(entradaSalidaBean.getTitulos())));

                int Validacion = 0;
                double dCostoHistMSA = 0, dPosDispMSA = 0;

                //_/Valida_Salida
                //_Salida
                //VALIDA_POSICION_MSA_Y_MA
                if (tipoMovimiento.equals("S")) {
                    int iTitulos = Integer.parseInt(entradaSalidaBean.getTitulos());

                    // Recupera_Posicion
                    if (FecValorMSA == 1) {
                        //_Valida_Posicion_de_Posicion_Mes_Anterior
                        posicionBean = cTesoreria.onTesoreria_getPosicionMSA(entradaSalidaBean.getFideicomiso(),
                                entradaSalidaBean.getSubFiso(), entradaSalidaBean.getEntidadFinan(),
                                entradaSalidaBean.getCtoInver(), IdMercado, IdInstrumento,
                                entradaSalidaBean.getNomPizarra(), entradaSalidaBean.getSerieEmis(),
                                entradaSalidaBean.getCuponVig(), iSecEmis);

                        if (posicionBean.getPizarra() != null) {

                            dCostoHistMSA = posicionBean.getPosCostHist();
                            dPosDispMSA = posicionBean.getPosDisp();

                            if (posicionBean.getPosDisp() < iTitulos) {
                                Validacion = 1;
                                entradaSalidaBean.setTitulos("0");
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El contrato no tiene Posición suficiente en el mes anterior, la Posición es de: " + formatFormatTit(posicionBean.getPosDisp()) + "..."));
                            } else {
                                Validacion = 1;
                            }

                        } else {
                            Validacion = 1;
                            entradaSalidaBean.setTitulos("0");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe Posición en el mes anterior..."));
                        }
                    }
                    if (Validacion == 0) {
                        //_Valida_Posicion_de_Salida_Actual
                        posicionBean = cTesoreria.onTesoreria_ValidaSalida(entradaSalidaBean.getFideicomiso(),
                                entradaSalidaBean.getSubFiso(), entradaSalidaBean.getEntidadFinan(),
                                entradaSalidaBean.getCtoInver(), entradaSalidaBean.getNomPizarra(),
                                entradaSalidaBean.getSerieEmis(), entradaSalidaBean.getCuponVig(),
                                IdMercado, IdInstrumento);

                        if (posicionBean.getPizarra() != null) {
                            if (posicionBean.getPosDisp() < iTitulos) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "Fiduciario", "No existe Posición suficiente para la Operación, la Posición Actual es de: " + formatFormatTit(posicionBean.getPosDisp()) + "..."));
                                entradaSalidaBean.setTitulos("0");
                                respuesta = false;
                            } else {
                                Validacion = 1;
                            }

                        } else {
                            Validacion = 1;
                            entradaSalidaBean.setTitulos("0");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                    FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe Posición en el mes..."));
                            respuesta = false;
                        }
                    }

                    if (FecValorMSA == 1 && Validacion == 0) {
                        if (dCostoHistMSA != posicionBean.getPosCostHist()
                                && dPosDispMSA != posicionBean.getPosDisp()) {

                            //Validacion = 1;
                            entradaSalidaBean.setTitulos("0");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                    "La posición del mes actual es diferente del mes anterior, para aplicar fecha valor debe dejar idénticas las posiciones, si no los costos y las utilidades serian totalmente erróneas..."));
                            respuesta = false;
                        }
                    }

                    //RECUPERA_PRECIO:_COSTO_PROMEDIO_O_PEPS
                    double dPrecio = 0, dImporte = 0;
                    if (CValidacionesUtil.validarDouble(entradaSalidaBean.getPrecioEmis()) == 0) {
                        if (sTipoValua.equals("COSTO PROMEDIO")) {
                            if (posicionBean.getPosActual() > 0) {
                                dPrecio = round(posicionBean.getPosCostHist() / posicionBean.getPosActual(), 8);
                                // Formateo_Precio
                                entradaSalidaBean.setPrecioEmis(formatFormatPrec(dPrecio));
                            } else {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Costo en la Posicion es Cero..."));
                                respuesta = false;
                            }
                        } else {
                            //PEPS 
                            Date diaOpera = null;
                            try {
                                diaOpera = new SimpleDateFormat("dd/MM/yyyy").parse(entradaSalidaBean.getDiaOpera());
                            } catch (ParseException ex) {
                                logger.error("Error al parsear la fecha de dia opera");
                            }
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            outParameterBean = cTesoreria.onTesoreria_EjecutaPEPS(
                                    Integer.parseInt(entradaSalidaBean.getFideicomiso()),
                                    Integer.parseInt(entradaSalidaBean.getSubFiso()),
                                    java.sql.Date.valueOf(simpleDateFormat.format(diaOpera)),
                                    entradaSalidaBean.getEntidadFinan(),
                                    entradaSalidaBean.getCtoInver(),
                                    entradaSalidaBean.getNomPizarra(),
                                    entradaSalidaBean.getSerieEmis(),
                                    Integer.parseInt(entradaSalidaBean.getCuponVig()),
                                    iTitulos);

                            //Aplicado
                            //vlTitAsignados
                            if (outParameterBean.getiNumFolioContab() == 0 && outParameterBean.getbEjecuto() == 1) {
                                dImporte = outParameterBean.getdImporteCobrado();
                                dPrecio = round(dImporte / iTitulos, 8);
                                // Formateo_Precio
                                entradaSalidaBean.setPrecioEmis(formatFormatPrec(dPrecio));
                            } else {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "No existen Compras con Títulos disponibles para esta Salida ".concat(outParameterBean.getPsMsgErrOut()).concat("...")));
                                return false;
                            }
                        }
                    }

                    //_VALIDA_VALOR_NOMINAL
                    if (IdMercado == 3 || IdMercado == 2) {
                        //Validacion Bajo Par
                        if (cTesoreria.onTesoreria_ConsultaBajoPar(IdInstrumento, IdMercado) != 0) {
                            double dValorNominal = cTesoreria.onTesoreria_ConsultaValorNominal(IdInstrumento, IdMercado);

                            if (dValorNominal < CValidacionesUtil.validarDouble(entradaSalidaBean.getPrecioEmis())) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Precio ".concat(entradaSalidaBean.getPrecioEmis()).concat(" no debe ser mayor que el valor nominal ").concat(String.valueOf(dValorNominal)).concat("...")));
                                respuesta = false;
                            }
                        }
                    }
                }
            }

        }

        if (entradaSalidaBean.getPrecioEmis() == null || "".equals(entradaSalidaBean.getPrecioEmis())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio por Título no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(entradaSalidaBean.getPrecioEmis())) {
            // Clean
            entradaSalidaBean.setImporte(0.0);
            entradaSalidaBean.setPrecioEmis("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Precio por Título debe ser un campo numérico..."));
            respuesta = false;
        } else if (CValidacionesUtil.validarDouble(entradaSalidaBean.getPrecioEmis()) <= 0) {
            // Clean
            entradaSalidaBean.setImporte(0.0);
            entradaSalidaBean.setPrecioEmis("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio por Título debe ser mayor a 0..."));
            respuesta = false;
        } else {
            // Formateo_Precio
            entradaSalidaBean.setPrecioEmis(formatFormatPrec(CValidacionesUtil.validarDouble(entradaSalidaBean.getPrecioEmis())));

            if (entradaSalidaBean.getTitulos() != null && !"".equals(entradaSalidaBean.getTitulos()) && isNumeric(entradaSalidaBean.getTitulos()) && Integer.parseInt(entradaSalidaBean.getTitulos()) > 0) {
                // Set_Values_To_Bean
                entradaSalidaBean.setImporte(CValidacionesUtil.validarDouble(formatDecimalImp(Double.parseDouble(entradaSalidaBean.getPrecioEmis()) * Integer.parseInt(entradaSalidaBean.getTitulos()))));

            } else {
                // Clean_Importe
                entradaSalidaBean.setImporte(0);

                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar los títulos."));
                //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el Precio."));
                respuesta = false;
            }
        }

        // _Custodio
        if (tipoMovimiento.equals("E")) {
            if (entradaSalidaBean.getCustodio() == 0) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El campo Custodio no puede estar vacío..."));
                respuesta = false;
            }
        }

        // _Redaccion
        if (sRedaccion == null || sRedaccion.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Redacción no puede estar vacío..."));
            respuesta = false;
        }

        return respuesta;
    }

    public boolean ValidacionesNegocio() {
        // Valida_si_la_emision_existe
        emisionBean = cTesoreria.onTesoreria_existeEmisionSalida(entradaSalidaBean.getNomPizarra().trim(),
                entradaSalidaBean.getSerieEmis().trim(), IdInstrumento, IdMercado);

        if (!emisionBean.getStatus().equals("")) {

            if (emisionBean.getStatus().equals("ACTIVO")) {

                iSecEmis = emisionBean.getNumSecEmis();
                entradaSalidaBean.setCuponVig(String.valueOf(emisionBean.getCupon()));

                //_Validar_si_la_emision_está_Bloqueada_para_Entradas_y_Salidas
                //_Se_guardan_temporalemente_para_validar_con_la_fecha_del_movimiento
                //_TipoMercado_=_Fecha_Inicio
                //_NumInstrume_=_Fecha_Fin
                if (emisionBean.getTipoMercado() >= 0 && emisionBean.getNumInstrume() >= 0) {
                    int iFechaIni = emisionBean.getTipoMercado();
                    int iFechaFin = emisionBean.getNumInstrume();
                    int iFechaMov = iAnio * 10000 + iMes * 100 + iDia;

                    if (iFechaIni <= iFechaMov && iFechaMov <= iFechaFin && (iFechaIni != 0 && iFechaFin != 0)) {
                        // Set_Message
                        if (tipoMovimiento.equals("E")) {
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                    "La Emisión se encuentra Bloqueda para Entradas/Compras..."));
                        }

                        if (tipoMovimiento.equals("S")) {
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                    "La Emisión se encuentra Bloqueda para Salidas/Ventas..."));
                        }

                        return false;
                    }
                }
            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Emisión " + entradaSalidaBean.getNomPizarra() + "/"
                                + entradaSalidaBean.getSerieEmis() + "no está activa..."));
                return false;
            }
        } else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "No existe la Emisión " + entradaSalidaBean.getNomPizarra() + "/" + entradaSalidaBean.getSerieEmis() + "..."));
            return false;
        }

        // Manda_Aplicar_Operación
        AplicaOperacion();
        return true;

    }

    public void AplicaOperacion() {
        int Comision = 0;
        int iRetieneISR = 0;
        double dValorIVA = 0;
        String sGarantia = "SINGARANTIA";

        try {

            // Aplica_Compra
            if (tipoMovimiento.equals("E")) {

                // Aplica_SP: SP_MD_CPA_DIRECTO
                outParameterBean = cTesoreria.onTesoreria_EjecutaCompraDirecto(entradaSalidaBean,
                        fechaContableBean.getFecha(), // fecha_contable
                        IdMercado,
                        IdInstrumento,
                        iSecEmis,
                        FecValorMSA,
                        Comision,
                        dValorIVA,
                        "PANTALLA",
                        "ENTRADAS FISICAS",
                        iRetieneISR,
                        sGarantia);
            } // ENTRADA
            else { // SALIDA

                // Aplica_SP_MD_VTA_DIRECTO
                outParameterBean = cTesoreria.onTesoreria_EjecutaVentaDirecto(entradaSalidaBean,
                        fechaContableBean.getFecha(), // fecha_contable
                        IdMercado,
                        IdInstrumento,
                        iSecEmis,
                        FecValorMSA,
                        Comision,
                        dValorIVA,
                        "PANTALLA",
                        "SALIDAS FISICAS",
                        iRetieneISR,
                        sGarantia);
            }

            //_Aplicado
            if (outParameterBean.getiNumFolioContab() > 0) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "Movimiento Aplicado correctamente con Folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                this.listaSubContrato = null;
                this.subFiso = "";
                entradaSalidaBean.setFideicomiso("");
                entradaSalidaBean.setSubFiso("");
                tipoMovimiento = "E";
                HabilitaPanelInv = true;
                SelectTipoMov();
                DeshabilitarComponentes();

                init();
            } // No_aplicado
            else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "El Movimiento No se Aplicó: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", "")))));
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " AplicaOperacion()";
        }
    }

    public void DeshabilitarComponentes() {

        // Clean_Bean
        posicion = null;
        emisionBean = new EmisionBean();
        posicionBean = new PosicionBean();
        outParameterBean = new OutParameterBean();

        // Variables
        mercado = "";
        sValEmis = "";
        tipoValor = "";
        sCtoInver = "";
        NombreMon = "";
        instrumento = "";
        iDia = 0;
        iMes = 0;
        iAnio = 0;
        iSecEmis = 0;
        IdMercado = 0;
        IdInstrumento = 0;
        sRedaccion = "";

        // _Botones
        habilitaSelCtoInv = 0;
        habilitaTxtPrecio = 0;
        habilitaTxtTitulos = 0;
        visiblePanelMoneda = 1;
        habilitaSelCustodio = 0;
        habilitaSelTipoValor = 0;
        habilitaSelEmisiones = 0;
        habilitaBotonCancelar = 0;
        habilitaTxtSubFiso = true;

        // Clean_Campos
        entradaSalidaBean.setMoneda(0);
        entradaSalidaBean.setTitulos("0");
        entradaSalidaBean.setImporte(0);
        entradaSalidaBean.setCtoInver(0);
        entradaSalidaBean.setCuponVig("");
        entradaSalidaBean.setCustodio(0);
        entradaSalidaBean.setSerieEmis("");
        entradaSalidaBean.setPrecioEmis("0");
        entradaSalidaBean.setNomPizarra("");
        entradaSalidaBean.setEntidadFinan(0);

    }

    public void CargaArchivo(FileUploadEvent event) {

        try {
            iRegistros = 0;
            dCorrectos = 0;
            iIncorrectos = 0;
            CargaInterfaz = null;
            cTesoreria.onTesoreria_Delete_CargaInterfaz();

            int result = 1, cont = 0;
            //int secuencialArchivo = 0; //Default

            //Fecha_Valor_o_Contable
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date diaOpera = new SimpleDateFormat("dd/MM/yyyy").parse(entradaSalidaBean.getDiaOpera());

            //secuencialArchivo = (cTesoreria.onTesoreria_existeCargaInterfaz("MASIVO ENTRADAS-SALIDAS",
            //        java.sql.Date.valueOf(simpleDateFormat.format(diaOpera)))) + 1;
            //Obtiene_el_nombre_del_archivo   
            sNomArchivo = event.getFile().getFileName();
            setFile(event.getFile());

            // Set_Values
            CargaInterfazBean cargaInterBean = new CargaInterfazBean();

            // Open_File
            //Leer_el_archivo       
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputstream(), "UTF-8"), 10000024);
            String line = bufferedReader.readLine();

            while (line != null) {
                cargaInterBean.setSecuencialArchivo(1);
                cargaInterBean.setSecuencial(result++);
                cargaInterBean.setFecha(java.sql.Date.valueOf(simpleDateFormat.format(diaOpera)));
                cargaInterBean.setRuta(sNomArchivo);
                cargaInterBean.setNombreArchivo(sNomArchivo);
                cargaInterBean.setArchivoTemporal("N/A");
                cargaInterBean.setCadena(line);
                cargaInterBean.setEstatus("A");
                cargaInterBean.setMensaje("");

                // Inserta_los_registros_aceptados_en_CARGA_INTERFAZ 			
                cont += cTesoreria.onTesoreria_Insert_CargaInterfaz(cargaInterBean);
                line = bufferedReader.readLine();
            }

            // Close_BufferedReader
            bufferedReader.close();

            iRegistros = cont; //-1_para_no_contar_encabezado

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "Se cargaron " + cont + " registros"));

        } catch (FileNotFoundException FileErr) {
            mensajeError += "Descripción: " + FileErr.getMessage() + nombreObjeto + "CargaArchivo()";

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "El archivo " + sNomArchivo + " no se cargo correctamente"));

        } catch (IOException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "CargaArchivo()";
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "El archivo " + sNomArchivo + " no se cargo correctamente: " + exception));
        }
    }

    public void ValidaArchivo_CargaMasiva() {
        ProcesaArchivo(1);
    }

    public void Invoca_CargaMasiva() {
        ProcesaArchivo(2);
    }

    public void ProcesaArchivo(int sOpcion) {

        try {

            //Fecha_Valor_o_Contable
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date diaOpera = new SimpleDateFormat("dd/MM/yyyy").parse(entradaSalidaBean.getDiaOpera());

            if (sNomArchivo.equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "No ha seleccionado el archivo..."));
                return;
            }

            //INVOCAR_SPN_VAL_ENTSAL_MAS
            outParameterBean = cTesoreria.onTesoreria_Ejecuta_CargaMasiva(java.sql.Date.valueOf(simpleDateFormat.format(diaOpera)), // fecha_valor
                    sNomArchivo,
                    sNomArchivo,
                    sOpcion);

            dCorrectos = outParameterBean.getdImporteCobrado();
            iIncorrectos = outParameterBean.getiNumFolioContab();

            //_Consulta_Resultado_Carga_Interfaz
            ConsultaArchivo();

            int erroresCargaInterfaz = 0;
            for (CargaInterfazBean cargaMasivaES : CargaInterfaz) {
                if (!"".equals(cargaMasivaES.getMensaje())) {
                    erroresCargaInterfaz++;
                }
            }

            if (!outParameterBean.getPsMsgErrOut().equals("")) {
                if (sOpcion == 1) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "El archivo no se validó correctamente, " + outParameterBean.getPsMsgErrOut()));
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "El archivo no se proceso correctamente, " + outParameterBean.getPsMsgErrOut()));
                }
            } else {
                // Set_Message
                if (sOpcion == 1) {
                    if (erroresCargaInterfaz == 0) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo se validó correctamente..."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo no se validó correctamente, verifique los errores..."));
                    }
                } else {
                    if (erroresCargaInterfaz == 0) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo se procesó correctamente..."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo ha sido procesado, verifique sus resultados..."));
                    }
                }
            }

        } catch (ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ProcesaArchivo()";
        }
    }

    public void ConsultaArchivo() {

        try {

            //Fecha_Valor_o_Contable
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date diaOpera = new SimpleDateFormat("dd/MM/yyyy").parse(entradaSalidaBean.getDiaOpera());

            if (sNomArchivo.equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "No ha seleccionado el archivo..."));
                return;
            }

            CargaInterfaz = cTesoreria.onTesoreria_ConsultaInterfaz("MASIVO ENTRADAS-SALIDAS",
                    java.sql.Date.valueOf(simpleDateFormat.format(diaOpera)), // fecha_valor
                    sNomArchivo,
                    sNomArchivo);

        } catch (ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ConsultaArchivo()";
        }
    }

    public void Start_CargaMasiva() {
        sNomArchivo = "";
        iRegistros = 0;
        dCorrectos = 0;
        iIncorrectos = 0;
        CargaInterfaz = null;
        cTesoreria.onTesoreria_Delete_CargaInterfaz();
        RequestContext.getCurrentInstance().execute("dialogCargaMasiva.show();");
    }

    public void Limpia_CargaMasiva() {
        sNomArchivo = "";
        iRegistros = 0;
        dCorrectos = 0;
        iIncorrectos = 0;
        CargaInterfaz = null;
        RequestContext.getCurrentInstance().execute("dialogCargaMasiva.hide();");
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
            return false;
        }
    }

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
        return bd.doubleValue();
    }

    private synchronized String formatDate(Date date) throws ParseException {
        return simpleDateFormat2.format(date);
    }

    private synchronized Date parseDate(String date) throws ParseException {
        return simpleDateFormat2.parse(date);
    }

    private synchronized String formatFormatTit(Double decimal) {
        return decimalFormatTit.format(decimal);
    }

    private synchronized String formatFormatTitInt(Integer entero) {
        return decimalFormatTit.format(entero);
    }

    private synchronized String formatDecimalImp(Double decimal) {
        return decimalFormatImp.format(decimal);
    }

    private synchronized String formatFormatPrec(Double decimal) {
        return decimalFormatPrec.format(decimal);
    }

}
