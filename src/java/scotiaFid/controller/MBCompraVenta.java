/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBCompraVenta.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

import java.io.IOException;
//Imports
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
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

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
@ManagedBean(name = "mbCompraVenta")
@ViewScoped
public class MBCompraVenta implements Serializable {

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
    private EmisionBean emisionBean = new EmisionBean();
    // -----------------------------------------------------------------------------
    private ContratoBean contratoBean = new ContratoBean();
    // -----------------------------------------------------------------------------
    private PosicionBean posicionBean = new PosicionBean();
    // -----------------------------------------------------------------------------
    private CompraVentaBean compraVentaBean = new CompraVentaBean();
    // -----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    // -----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean = new FechaContableBean();
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    private static final Logger logger = LogManager.getLogger(MBCompraVenta.class);
    // -----------------------------------------------------------------------------
    private List<PosicionBean> posicion = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private Map<String, String> monedas = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> custodio = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> emisiones = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> ctoInversion = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> tipoValores = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private List<String> listaSubContrato = new ArrayList<>();
    // -----------------------------------------------------------------------------
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F O R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatTit = new DecimalFormat("#############");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatImp = new DecimalFormat("############0.00");
    // -----------------------------------------------------------------------------
    //DecimalFormat decimalFormatPrec = new DecimalFormat("####0.00#########");
    private static final DecimalFormat decimalFormatPrec = new DecimalFormat("####0.00");
    // -----------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
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
    // Habilitar_Componentes
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
    private String tipoMovimiento;
    // -----------------------------------------------------------------------------
    private int visiblePanelMoneda;
    // -----------------------------------------------------------------------------
    private int visiblePanelComision;
    // -----------------------------------------------------------------------------
    private int visiblePanelPosicion;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelInv;
    // -----------------------------------------------------------------------------
    // Campos_de_Pantalla
    // -----------------------------------------------------------------------------
    private String Comision;
    // -----------------------------------------------------------------------------
    private Date FecValor;
    // -----------------------------------------------------------------------------
    private int FecValorMSA;
    // -----------------------------------------------------------------------------
    private String CtaCheques;
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
    private String subFiso;
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
	 * G E T T E R S Y S E T T E R S * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public CompraVentaBean getCompraVentaBean() {
        return compraVentaBean;
    }

    public void setCompraVentaBean(CompraVentaBean compraVentaBean) {
        this.compraVentaBean = compraVentaBean;
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

    public List<String> getListaSubContrato() {
        return listaSubContrato;
    }

    public void setListaSubContrato(List<String> listaSubContrato) {
        this.listaSubContrato = listaSubContrato;
    }

    public String getComision() {
        return Comision;
    }

    public void setComision(String Comision) {
        this.Comision = Comision;
    }

    public Date getFecValor() {
        return FecValor;
    }

    public void setFecValor(Date fecValor) {
        FecValor = fecValor;
    }

    public int getFecValorMSA() {
        return FecValorMSA;
    }

    public void setFecValorMSA(int fecValorMSA) {
        FecValorMSA = fecValorMSA;
    }

    public String getCtaCheques() {
        return CtaCheques;
    }

    public void setCtaCheques(String CtaCheques) {
        this.CtaCheques = CtaCheques;
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

    public int getHabilitaPanelComision() {
        return visiblePanelComision;
    }

    public void setHabilitaPanelComision(int visiblePanelComision) {
        this.visiblePanelComision = visiblePanelComision;
    }

    public int getHabilitaPanelPosicion() {
        return visiblePanelPosicion;
    }

    public void setHabilitaPanelPosicion(int visiblePanelPosicion) {
        this.visiblePanelPosicion = visiblePanelPosicion;
    }

    public boolean isHabilitaPanelInv() {
        return HabilitaPanelInv;
    }

    public void setHabilitaPanelInv(boolean habilitaPanelInv) {
        HabilitaPanelInv = habilitaPanelInv;
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

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public MBCompraVenta() {
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
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.MBCompraVenta.";

                // Write_Log
                //logger.debug("Inicia Controller MBCompraVenta");  
                // _Tipo_Movimiento_Default_Compra
                tipoMovimiento = "C";

                // Deshabilitar_paneles
                visiblePanelMoneda = 1;
                visiblePanelComision = 1;
                visiblePanelPosicion = 1;
                HabilitaPanelInv = true;
                habilitaTxtSubFiso = true;

                // Set_Bean
                this.compraVentaBean = new CompraVentaBean();

                // Get_Fecha_Contable
                fechaContableBean = cTesoreria.onTesoreria_GetFechaContable();

                // Format_Fecha_Contable
                fechaContableBean.setFechaContable(formatDate(fechaContableBean.getFecha()));

                // _Agregar_fecha_contable_como_fecha_valor
                FecValor = fechaContableBean.getFecha();
                compraVentaBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));
            }
        } catch (DateTimeException | IOException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Construct()";
        }
    }

    public void select_FecVal() {
        FecValorMSA = 0;

        //logger.debug("Ingresa a select_FecVal()");
        try {
            if (FecValor != null) {

                //Default_fec_contable
                compraVentaBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                //_La_fecha_valor_es_menor_o_igual_a_la_fecha_contable
                if (FecValor.before(parseDate(fechaContableBean.getFechaContable()))
                        || FecValor.equals(parseDate(fechaContableBean.getFechaContable()))) {

                    if (cFecha.onFecha_DiaHabil(FecValor) == 1) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha inválida, debe ser un dia hábil..."));
                        FecValor = fechaContableBean.getFecha();
                    } else {
                        //_Validar_si_permite_aplicar_con_fecha_Valor
                        int iMesVal = 0, iYearVal = 0, iMesCont = 0, iYearCont = 0;

                        iMesVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                        iMesCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                        iYearCont = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                        if (iMesVal != iMesCont || iYearVal != iYearCont) {
                            if (cFecha.onFecha_FecValTesoreria()) {
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
                }

                //_Asigna_el_valor_de_la_fecha_a_la_variable_Bean
                compraVentaBean.setDiaOpera(formatDate(FecValor));

                //Determina_Moneda
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
            compraVentaBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");

            // Determina_Moneda
            DeterminaMoneda();
        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en ConfirmaFecValor()");
        }

    }

    public void NiegaFecValor() {
        //logger.debug("Ingresa a NiegaFecValor()");
        try {
            FecValorMSA = 0;
            FecValor = fechaContableBean.getFecha();
            compraVentaBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
        } catch (ParseException exception) {
            logger.error("Error al formatear fecha valor en NiegaFecValor()");
        }
    }

    public void checkFideicomisoCompraVenta() {
        try {
            // Write_Log
            //logger.debug("Ingresa a checkFideicomisoCompraVenta()" + compraVentaBean.getFideicomiso() );

            // Disabled
            DeshabilitarComponentes();
            contratoBean = new ContratoBean();
            compraVentaBean.setFideicomiso(compraVentaBean.getFideicomiso().trim());

            habilitaBotonCancelar = 1;
            listaSubContrato = null;
            compraVentaBean.setSubFiso("");
            this.subFiso = "";

            // Check_Fideicomiso_vacío
            if (compraVentaBean.getFideicomiso().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(compraVentaBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                compraVentaBean.setFideicomiso(null);
                return;
            }

            if (Integer.parseInt(compraVentaBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es inválido..."));
                compraVentaBean.setFideicomiso(null);
                return;
            }

            //logger.debug("Valida Fideicomiso: ".concat(compraVentaBean.getFideicomiso()));
            //_Valida_si_el_contrato_existe_Activo
            contratoBean = cTesoreria.onTesoreria_checkFideicomiso(Integer.parseInt(compraVentaBean.getFideicomiso()));

            //logger.debug("contratoBean: "+contratoBean.getContrato());
            //_Valida_que_permita_operaciones_de_Inversión_y_no_esta_bloqueado
            if (contratoBean.getContrato() == Integer.parseInt(compraVentaBean.getFideicomiso())
                    && cTesoreria.onTesoreria_CtoBloqueado(contratoBean.getContrato(), "I")) {

                listaSubContrato = cTesoreria.onTesoreria_ListadoSubContratoSrv(Integer.parseInt(compraVentaBean.getFideicomiso()));

                if (contratoBean.getbSubContrato()) {
                    habilitaTxtSubFiso = false;

                    compraVentaBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(compraVentaBean.getFideicomiso(), compraVentaBean.getSubFiso(), Intermed, tipoMovimiento);

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

                    habilitaTxtSubFiso = false;

                    // Establece_SubFiso
                    habilitaBotonCancelar = 1;
                    compraVentaBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(compraVentaBean.getFideicomiso(), compraVentaBean.getSubFiso(), Intermed, tipoMovimiento);

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
                }
            } else {
                // Disabled
                DeshabilitarComponentes();
                habilitaSelCtoInv = 0;

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                compraVentaBean.setFideicomiso("");
                return;
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " checkFideicomisoCompraVenta()";
            //logger.error(mensajeError); 
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void checkSubFiso() {
        try {
            String[] SubFisoSeleccion = subFiso.split(".-");
            String cvSubFiso = SubFisoSeleccion[0];
            // Write_Log
            //logger.debug("Ingresa a checkSubFiso()");
            compraVentaBean.setSubFiso(cvSubFiso.trim());
            // Write_Log
            //logger.debug("Ingresa a checkSubFiso()");
            compraVentaBean.setSubFiso(compraVentaBean.getSubFiso().trim());
            // Write_Log
            //logger.debug("Ingresa a checkSubFiso()");

            //_Check_Fideicomiso_esta_vacío
            if (compraVentaBean.getFideicomiso().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso es un dato necesario..."));
                return;
            }

            // Check_SubFideicomiso_vacío
            if (compraVentaBean.getSubFiso().equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(compraVentaBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                compraVentaBean.setSubFiso("");
                return;
            }

            // Check_SubFiso_vacío
            if (!compraVentaBean.getSubFiso().equals("")) {

                //logger.debug("Valida SubFiso: ".concat(compraVentaBean.getSubFiso()));  
                // Disabled
                SelectTipoMov();

                //_Valida_el_Sub_contrato_existe_Activo
                //_Valida_que_permita_operaciones_de_Inversión_y_no_este_bloqueado
                //_Parametros:_DatosContrato,_SubFiso,_sTipoBloque
                if (cTesoreria.onTesoreria_VerificaSubContratoSrv(contratoBean, Integer.parseInt(compraVentaBean.getSubFiso()), "I")) {

                    habilitaTxtSubFiso = false;
                    habilitaBotonCancelar = 1;

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(compraVentaBean.getFideicomiso(), compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(), tipoMovimiento);

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
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Sub Fiso no existe o no estó activo..."));
                    compraVentaBean.setSubFiso("");
                    return;
                }
            } else {
                compraVentaBean.setSubFiso("0");
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
        try {
            // Write_Log
            //logger.debug("Ingresa a select_CtoInv()" );

            // Validate_Servicio
            if (sCtoInver.equals("0")) {

                // Variables
                posicion = null;
                tipoValor = "";
                CtaCheques = "";
                IdMercado = 0;
                IdInstrumento = 0;
                mercado = "";
                NombreMon = "";
                instrumento = "";
                iSecEmis = 0;
                sCtoInver = "";
                sValEmis = "";

                // Buttons
                habilitaSelTipoValor = 0;
                habilitaSelEmisiones = 0;
                habilitaTxtTitulos = 0;
                habilitaTxtPrecio = 0;
                habilitaSelCustodio = 0;
                visiblePanelMoneda = 1;
                visiblePanelComision = 1;

                // Clean_Campos
                compraVentaBean.setCustodio(0);
                compraVentaBean.setCtoInver(0);
                compraVentaBean.setEntidadFinan(0);
                compraVentaBean.setNomPizarra(null);
                compraVentaBean.setSerieEmis(null);
                compraVentaBean.setCuponVig(null);
                compraVentaBean.setTitulos("0");
                compraVentaBean.setImporte(0);
                compraVentaBean.setPrecioEmis("0");
                compraVentaBean.setMoneda(0);

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
                return;
            } else {

                String[] Id_CtoInv;
                String CtoInv = "", NumInterm = "", sTipoIntermed = "";

                // Clean
                // Variables
                tipoValor = "";
                CtaCheques = "";
                IdMercado = 0;
                IdInstrumento = 0;
                mercado = "";
                NombreMon = "";
                instrumento = "";
                iSecEmis = 0;
                sValEmis = "";

                // Buttons
                habilitaSelTipoValor = 0;
                habilitaSelEmisiones = 0;
                habilitaTxtTitulos = 0;
                habilitaTxtPrecio = 0;
                visiblePanelMoneda = 1;
                visiblePanelComision = 1;

                // Clean_Campos
                compraVentaBean.setNomPizarra(null);
                compraVentaBean.setSerieEmis(null);
                compraVentaBean.setCuponVig(null);
                compraVentaBean.setTitulos("0");
                compraVentaBean.setImporte(0);
                compraVentaBean.setPrecioEmis("0");
                compraVentaBean.setMoneda(0);
                compraVentaBean.setCustodio(0);

                //_Recuperar_los_valores_de_la_lista_de_Contrato_de_Inversión
                //logger.debug("Valor de CtoInver:" + sCtoInver);
                Id_CtoInv = sCtoInver.split("/");

                //_Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                CtoInv = Id_CtoInv[0];
                NumInterm = Id_CtoInv[1]; // Intermediario_o_Entidad_Financiera
                sTipoIntermed = Id_CtoInv[2];

                compraVentaBean.setCtoInver(Double.parseDouble(CtoInv));
                compraVentaBean.setEntidadFinan(Integer.parseInt(NumInterm));
                TipoIntermed = Integer.parseInt(sTipoIntermed);

                // Determina_Custodio
                habilitaSelCustodio = 1;
                //logger.debug("Consulta custodio ");

                //_Agrega_Casa_de_Bolsa_como_Custodio_en_el_Intermediario_Bancario_Scotia
                ArrayList<Integer> sComplementoCust = new ArrayList<Integer>();
                sComplementoCust.add(4);
                sComplementoCust.add(5);

                custodio = cTesoreria.onTesoreria_GetCustodio(386, sComplementoCust);

                //_Determinar_Cuenta_de_Cheques
                CtaCheques = cTesoreria.onTesoreria_GetCtaCheques(compraVentaBean.getFideicomiso(), compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(), compraVentaBean.getCtoInver());
                if (CtaCheques.contains("No existe contrato de Inversión")) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe contrato de Inversión..."));
                    CtaCheques = "";
                }
                //  //logger.debug("Cuenta cheques " + CtaCheques);
                //  //logger.debug("Cuenta cheques " + CtaCheques);

                //_Valida_si_es_Compra_o_Venta
                if (getTipomovimiento().equals("V")) {

                    //_Deshabilita_porque_se_seleccionaran_de_la_tabla_los_valores
                    //logger.debug("Opcion Venta");
                    // Determina_Posicion
                    posicion = cTesoreria.onTesoreria_ObtenPosicion(compraVentaBean.getFideicomiso(),
                            compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(),
                            compraVentaBean.getCtoInver());

                    if (posicion.isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Contrato no tiene Posición..."));
                    }
                } else {
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

                    habilitaSelTipoValor = 1;
                    tipoValores = cTesoreria.onTesoreria_GetTipoValor(sComplemento);

                    //_Deshabilita
                    visiblePanelMoneda = 1;
                }
                // ***********************************************************************
                /*
                //logger.debug("******Datos seleccionando Contrato de Inversión*****");
                //logger.debug("Instrumento: " + IdInstrumento);
                //logger.debug("Mercado: " + IdMercado);
                //logger.debug("Sec. Emisora: " + iSecEmis);
                //logger.debug("Entidad / Intermediario: " + compraVentaBean.getEntidadFinan());
                //logger.debug("Cto. Inversión: " + compraVentaBean.getCtoInver());
                //logger.debug("Tipo Intermediario: " + TipoIntermed);
                 */
                // ***********************************************************************

            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_CtoInv()";
            //logger.error(mensajeError); 
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void select_TipoValor() {
        try {
            // Write_Log
            //logger.debug("Ingresa a select_TipoValor()" );

            // Clean_controles
            // Buttons
            sValEmis = "";
            habilitaSelEmisiones = 0;
            habilitaTxtTitulos = 0;
            habilitaTxtPrecio = 0;
            visiblePanelMoneda = 1;
            visiblePanelComision = 1;
            visiblePanelPosicion = 1;

            // Clean_Campos
            compraVentaBean.setNomPizarra(null);
            compraVentaBean.setSerieEmis(null);
            compraVentaBean.setCuponVig(null);
            compraVentaBean.setTitulos("0");
            compraVentaBean.setImporte(0);
            compraVentaBean.setPrecioEmis("0");
            compraVentaBean.setMoneda(0);

            if (tipoValor.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Valor no puede estar vacío..."));
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
                //logger.debug("Tipo Valor: " + tipoValor);
                for (Map.Entry<String, String> o : tipoValores.entrySet()) {

                    if (tipoValor.equals(o.getKey())) {

                        LabelTipoValor = o.getValue().split("/");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        mercado = LabelTipoValor[1];
                        instrumento = LabelTipoValor[2];

                        IdMercado = Integer.parseInt(o.getKey().substring(0, 1));
                        IdInstrumento = Integer.parseInt(o.getKey().substring(1, 4));

                        /*
						//logger.debug("Valor seleccionado ");
						//logger.debug("Mercado: " + mercado);
						//logger.debug("Instrumento: " + instrumento);
						//logger.debug("Id. Mercado:" + IdMercado);
						//logger.debug("Id. Instrumento:" + IdInstrumento);
                         */
                    }
                }

                if (IdMercado >= 0 && IdInstrumento >= 0) {
                    // Carga_Emisiones
                    habilitaSelEmisiones = 1;
                    emisiones = cTesoreria.onTesoreria_GetEmisiones(getIdMercado(), getIdInstrumento());
                }

                //_Habilitar_Controles_de_Comision
                if (IdMercado == 1) {
                    //_Habilita_Panel_de_Comision
                    visiblePanelComision = 0;
                } else {
                    visiblePanelComision = 1;
                }
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_TipoValor()";
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
        if (compraVentaBean.getCustodio() == 0) {

            compraVentaBean.setCustodio(0);

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
        }
    }

    public void select_Emisiones() {

        //logger.debug("Ingresa a select_Emisiones()" );
        try {

            if (sValEmis.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisiones no puede estar vacío..."));
                compraVentaBean.setMoneda(0);
                compraVentaBean.setNomPizarra(null);
                compraVentaBean.setSerieEmis(null);
                compraVentaBean.setCuponVig(null);
                visiblePanelMoneda = 1;
                compraVentaBean.setTipoCambio(0);
                return;
            } else {
                String[] LabelEmisora;

                // Selecciona_Emisora
                for (Map.Entry<String, String> o : emisiones.entrySet()) {

                    if (sValEmis.equals(o.getKey())) {
                        LabelEmisora = o.getValue().split(" / ");

                        //_Recuperar_etiquetas_de_Mercado_e_Instrumento
                        iSecEmis = Integer.parseInt(sValEmis); // Secuencia_emisora
                        compraVentaBean.setNomPizarra(LabelEmisora[0].trim());
                        compraVentaBean.setSerieEmis(LabelEmisora[1].trim());
                        compraVentaBean.setCuponVig(LabelEmisora[2].trim());

                        // *************************************************************
                        /*
						//logger.debug("Datos en Emisiones ");
						//logger.debug("Pizarra: " + compraVentaBean.getNomPizarra());
						//logger.debug("Serie: " + compraVentaBean.getSerieEmis());
						//logger.debug("Cupón: " + compraVentaBean.getCuponVig());
						//logger.debug("Num. Sec. Emisora: " + iSecEmis);
                         */
                        // *************************************************************
                        //_Habilita_precio,_importe_y_titulos
                        habilitaTxtTitulos = 1;
                        habilitaTxtPrecio = 1;
                    }
                }

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

        String[] LabelMoneda;
        int iDia = 0, iMes = 0, iAnio = 0;

        // Write_Log
        //logger.debug("Ingresa a DeterminaMoneda()");
        compraVentaBean.setTipoCambio(0);

        try {
            if (compraVentaBean.getNomPizarra() != null && IdMercado != 0) {

                FecValor = parseDate(compraVentaBean.getDiaOpera());

                iDia = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                iMes = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                iAnio = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

                monedas = cTesoreria.onTesoreria_GetTipoCambio(iDia, iMes, iAnio, IdMercado, IdInstrumento,
                        compraVentaBean.getNomPizarra(), compraVentaBean.getSerieEmis(), compraVentaBean.getCuponVig());

                for (Map.Entry<String, String> o : monedas.entrySet()) {
                    if (o.getKey() != "1") {
                        LabelMoneda = o.getValue().split("/");

                        // _Recuperar_etiquetas_de_Mercado_e_Instrumento
                        compraVentaBean.setMoneda(Integer.parseInt(o.getKey()));
                        compraVentaBean.setTipoCambio(CValidacionesUtil.validarDouble(LabelMoneda[0]));
                        NombreMon = LabelMoneda[1];

                        //  //logger.debug("Moneda: " + compraVentaBean.getMoneda() + " NombreMon: " + NombreMon);
                        //if (compraVentaBean.getMoneda() != 1) {
                        //logger.debug("Tipo Cambio: " + compraVentaBean.getTipoCambio());
                        visiblePanelMoneda = 0;
                        return;
                        //} 
                    }
                }
            }

        } catch (NumberFormatException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " DeterminaMoneda()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void onRowSelect_Posicion(SelectEvent event) {
        try {
            // Write_Log
            //logger.debug("Ingresa a onRowSelect_Posicion()" );

            // Get_Posicion
            this.posicionBean = (PosicionBean) event.getObject();

            compraVentaBean.setNomPizarra(posicionBean.getPizarra());
            compraVentaBean.setSerieEmis(posicionBean.getSerieEmis());
            compraVentaBean.setCuponVig(String.valueOf(posicionBean.getCuponVig()));
            iSecEmis = posicionBean.getSecEmisora(); // secuencia_emisora
            IdInstrumento = posicionBean.getNumInstrumento();
            IdMercado = posicionBean.getTipoMerca();

            //_Habilita_precio_y_titulos
            compraVentaBean.setTitulos("0");
            compraVentaBean.setPrecioEmis("0");
            compraVentaBean.setImporte(0);
            habilitaTxtTitulos = 1;
            habilitaTxtPrecio = 1;

            if (posicionBean.getTipoMerca() == 1) {
                visiblePanelComision = 0; //_Habilita
                Comision = formatFormatPrec(Double.parseDouble("0.00"));
            } else {
                visiblePanelComision = 1;
            }

            //_Selecciona_el_Custodio_de_la_Posicion
            if (posicionBean.getNomCustodio() != null) {
                EstablecerCustodio();
            }

            /*
			//logger.debug("Pizarra:" + compraVentaBean.getNomPizarra());
			//logger.debug("Serie:" + compraVentaBean.getSerieEmis());
			//logger.debug("Cupón:" + compraVentaBean.getCuponVig());
			//logger.debug("Instrumento:" + IdInstrumento);
			//logger.debug("Mercado:" + IdMercado);
			//logger.debug("Sec. Emisora:" + iSecEmis);
			//logger.debug("Entidad / Intermediario:" + compraVentaBean.getEntidadFinan());
			//logger.debug("Cto. Inversión:" + compraVentaBean.getCtoInver());
             */
            ValidaEmisiones();

            // Recupera_Precio
            compraVentaBean.setPrecioEmis(String.valueOf(cTesoreria.onTesoreria_getPrecio(compraVentaBean.getNomPizarra(),
                    compraVentaBean.getSerieEmis(), compraVentaBean.getCuponVig(), IdMercado, IdInstrumento)));

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " onRowSelect_Posicion()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void ValidaEmisiones() {

        try {
            //logger.debug("Ingresa a ValidaEmisiones()" );

            // Valida_Emisiones
            NombreMon = "";
            emisionBean = cTesoreria.onTesoreria_validaEmisiones(compraVentaBean.getNomPizarra().trim(),
                    compraVentaBean.getSerieEmis().trim(),
                    compraVentaBean.getCuponVig(),
                    IdInstrumento, IdMercado, iSecEmis);

            if (emisionBean.getNumMoneda() > 0) {
                NombreMon = cTesoreria.onTesoreria_NomMoneda(emisionBean.getNumMoneda());
                compraVentaBean.setMoneda(emisionBean.getNumMoneda());

                if (!NombreMon.equals("MONEDA NACIONAL")) {

                    //logger.debug("Num. Moneda: " + emisionBean.getNumMoneda());
                    if (emisionBean.getNumMoneda() != 1 && !NombreMon.equals("MONEDA NACIONAL")) {

                        //_Recupera_el_Tipo_de_Cambio_del_Mismo_Contable
                        String sTipoCambio = cHonorarios.onHonorarios_GetTipoCambio(emisionBean.getNumMoneda(),
                                Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño").toString()),
                                Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes").toString()),
                                Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia").toString()));

                        //_Si_no_hay_fecha_contable_del_dia,_se_debe_proporcionar_una
                        if (String.valueOf(sTipoCambio).equals("")) {
                            //RequestContext.getCurrentInstance().execute("dlgTipoCambio.show();");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de cambio para este dia..."));
                        } else {
                            compraVentaBean.setTipoCambio(CValidacionesUtil.validarDouble(sTipoCambio));
                        }
                        //habilita_Panel_Tipo_Cambio
                        visiblePanelMoneda = 0;

                    } else {
                        visiblePanelMoneda = 0;
                        compraVentaBean.setTipoCambio(Double.parseDouble("1"));
                    }
                } else {
                    compraVentaBean.setTipoCambio(Double.parseDouble("1"));
                    visiblePanelMoneda = 0;
                }
            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe emisión..."));
            }

            return;

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ValidaEmisiones()";
            //logger.error(mensajeError); 
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void SelectTipoMov() {
        //logger.debug("Ingresa a SelectTipoMov()");

        // _Compra
        if (getTipomovimiento().equals("C")) {

            visiblePanelPosicion = 1; // _Deshabilita 
            HabilitaPanelInv = true;
            posicionBean = new PosicionBean();

        }

        // _Venta
        if (getTipomovimiento().equals("V")) {
            posicion = null;
            visiblePanelPosicion = 0; // _Habilita
            HabilitaPanelInv = false;
        }

        // Clean
        // Variables
        tipoValor = "";
        CtaCheques = "";
        IdMercado = 0;
        IdInstrumento = 0;
        mercado = "";
        instrumento = "";
        Comision = formatFormatPrec(Double.parseDouble("0.00"));
        iSecEmis = 0;
        sCtoInver = "";
        sValEmis = "";

        // Buttons
        habilitaSelTipoValor = 0;
        habilitaSelEmisiones = 0;
        habilitaTxtTitulos = 0;
        habilitaTxtPrecio = 0;
        habilitaSelCustodio = 0;
        visiblePanelMoneda = 1;
        visiblePanelComision = 1;

        // Clean_Campos  
        compraVentaBean.setMoneda(0);
        compraVentaBean.setTitulos("0");
        compraVentaBean.setImporte(0);
        compraVentaBean.setCtoInver(0);
        compraVentaBean.setCustodio(0);
        compraVentaBean.setPrecioEmis("0");
        compraVentaBean.setEntidadFinan(0);
        compraVentaBean.setNomPizarra(null);
        compraVentaBean.setSerieEmis(null);
        compraVentaBean.setCuponVig(null);
    }

    public void bCancelar() {
        RequestContext.getCurrentInstance().execute("dlgSuspender.show();");
    }

    public void bConfirmaCancelacion() {
        try {
            //logger.debug("Ingresa a bConfirmaCancelacion");
            tipoMovimiento = "C";
            HabilitaPanelInv = true;
            DeshabilitarComponentes();
            listaSubContrato = null;
            compraVentaBean.setFideicomiso("");
            compraVentaBean.setSubFiso("");
            compraVentaBean.setDiaOpera(fechaContableBean.getFechaContable());
            init();

            FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/tesoreriaCompraVenta.sb");
        } catch (IOException ex) {
            mensajeError = "Error al redireccionar: " + ex.getMessage();
        }

    }

    public void bAceptaCpaVta() {
        //logger.debug("Ingresa a bAceptaCpaVta()" );

        if (ValidacionesPantalla()) {
            ValidacionesNegocio();
        }

    }

    public boolean ValidacionesPantalla() {
        //logger.debug("Ingresa a ValidacionesPantalla()");
        boolean respuesta = true;
        // Check_Fideicomiso_vacío
        if (compraVentaBean.getFideicomiso().equals("") || compraVentaBean.getFideicomiso().equals("0")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
            respuesta = false;
        }

        if (compraVentaBean.getSubFiso() == null || compraVentaBean.getSubFiso().equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Sub Fiso no puede estar vacío..."));
            respuesta = false;
        }

        // Check_Cto_Inversion_vacío
        if (sCtoInver == null || "".equals(sCtoInver) || compraVentaBean.getCtoInver() == 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
            respuesta = false;
        }

        // _Intermediario
        if (compraVentaBean.getEntidadFinan() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //"Fiduciario", "El campo Intermediario no puede estar vacío..."));
            respuesta = false;
        }

        // Check_Emision_vacío
        if (tipoMovimiento.equals("C")) {

            if (tipoValor == null || "".equals(tipoValor) || tipoValor.equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Valor no puede estar vacío..."));
                respuesta = false;
            }

            if (sValEmis == null || sValEmis.equals("0") || "".equals(sValEmis)) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Emisiones no puede estar vacío..."));
                respuesta = false;
            }

            if (compraVentaBean.getNomPizarra() == null || "".equals(compraVentaBean.getNomPizarra())) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Pizarra no puede estar vacío..."));
                respuesta = false;
            }

            if (compraVentaBean.getSerieEmis() == null || "".equals(compraVentaBean.getSerieEmis())) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario",
                //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Serie no puede estar vacío..."));
                respuesta = false;
            }

            if (iSecEmis == 0) {
                // Set_Message
                //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                //"Fiduciario", "El campo Secuencial de Pizarra no puede estar vacío..."));
                respuesta = false;
            }
        }

        if (tipoMovimiento.equals("V") && posicion != null) {
            // _Valida_que_exista_una_Posicion
            if (tipoMovimiento.equals("V") && posicion.size() <= 0) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Contrato no tiene Posición..."));
                return false;
            }

            if (tipoMovimiento.equals("V")) {
                if (compraVentaBean.getNomPizarra() == null && compraVentaBean.getSerieEmis() == null
                        && compraVentaBean.getCuponVig() == null) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Selecciona la venta en el grid..."));
                    respuesta = false;
                }
            }
        }

        if (compraVentaBean.getTitulos() == null || "".equals(compraVentaBean.getTitulos())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos no puede estar vacío..."));
            respuesta = false;
        } else if (!isNumeric(compraVentaBean.getTitulos())) {
            compraVentaBean.setImporte(0);
            compraVentaBean.setTitulos("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El No. Títulos debe ser un campo numérico..."));
            respuesta = false;
        } else if (Integer.parseInt(compraVentaBean.getTitulos()) <= 0) {
            // Clean
            compraVentaBean.setImporte(0);
            compraVentaBean.setTitulos("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo No. Títulos debe ser mayor a 0..."));
            respuesta = false;
        } else {
            int Validacion = 0;
            double iCostoHistMSA = 0, dPosDispMSA = 0;

            // Formateo_Titulos
            compraVentaBean.setTitulos(formatFormatTitInt(Integer.parseInt(compraVentaBean.getTitulos())));

            //_Venta
            if (tipoMovimiento.equals("V")) {
                // Recupera_Posicion
                if (FecValorMSA == 1) { // Posicion_Mes_Anterior
                    posicionBean = cTesoreria.onTesoreria_getPosicionMSA(compraVentaBean.getFideicomiso(),
                            compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(),
                            compraVentaBean.getCtoInver(), IdMercado, IdInstrumento,
                            compraVentaBean.getNomPizarra(), compraVentaBean.getSerieEmis(),
                            compraVentaBean.getCuponVig(), iSecEmis);

                    if (posicionBean.getPizarra() != null) {

                        iCostoHistMSA = posicionBean.getPosCostHist();
                        dPosDispMSA = posicionBean.getPosDisp();

                        if (posicionBean.getPosDisp() < Integer.parseInt(compraVentaBean.getTitulos())) {
                            Validacion = 1;
                            compraVentaBean.setTitulos("0");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                    "El contrato no tiene Posición suficiente en el mes anterior, la Posición es de: " + formatFormatTit(posicionBean.getPosDisp())));
                            respuesta = false;
                        } else {
                            Validacion = 1;
                        }

                    } else {
                        Validacion = 1;
                        compraVentaBean.setTitulos("0");
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe Posición en el mes anterior..."));
                        respuesta = false;
                    }
                }

                if (Validacion == 0) {
                    posicionBean = cTesoreria.onTesoreria_getPosicion(compraVentaBean.getFideicomiso(),
                            compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(),
                            compraVentaBean.getCtoInver(), IdMercado,
                            IdInstrumento, compraVentaBean.getNomPizarra(),
                            compraVentaBean.getSerieEmis(), compraVentaBean.getCuponVig(),
                            iSecEmis);

                    if (posicionBean.getPizarra() != null) {
                        if (posicionBean.getPosDisp() < Integer.parseInt(compraVentaBean.getTitulos())) {
                            Validacion = 1;
                            compraVentaBean.setTitulos("0");
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                    "El contrato no tiene Posición suficiente, la Posición es de: " + formatFormatTit(posicionBean.getPosDisp())));
                            respuesta = false;
                        } else {
                            Validacion = 1;
                        }

                    } else {
                        Validacion = 1;
                        compraVentaBean.setTitulos("0");
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe Posición en el mes..."));
                        respuesta = false;
                    }
                }

                if (FecValorMSA == 1 && Validacion == 0) {
                    if (iCostoHistMSA != posicionBean.getPosCostHist()
                            && dPosDispMSA != posicionBean.getPosDisp()) {

                        //Validacion = 1;
                        compraVentaBean.setTitulos("0");
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "La posición del mes actual es diferente del mes anterior, para aplicar fecha valor debe dejar idénticas las posiciones, si no los costos y las utilidades serian totalmente erroneas"));
                        respuesta = false;
                    }
                }

            }
        }

        if (compraVentaBean.getPrecioEmis() == null || "".equals(compraVentaBean.getPrecioEmis())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(compraVentaBean.getPrecioEmis())) {
            // Clean
            compraVentaBean.setImporte(0);
            compraVentaBean.setPrecioEmis("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Precio debe ser un campo numérico..."));
            respuesta = false;
        } else if (Double.parseDouble(compraVentaBean.getPrecioEmis()) <= 0) {
            // Clean
            compraVentaBean.setImporte(0);
            compraVentaBean.setPrecioEmis("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio debe ser mayor a 0..."));
            respuesta = false;
        } else {
            // Formateo_a_Precio
            compraVentaBean.setPrecioEmis(formatFormatPrec(Double.parseDouble(compraVentaBean.getPrecioEmis())));
            if(compraVentaBean.getTitulos() != null &&  !"".equals(compraVentaBean.getTitulos()) && isNumeric(compraVentaBean.getTitulos())) {
                compraVentaBean.setImporte(Double.parseDouble(formatDecimalImp(Double.parseDouble(compraVentaBean.getPrecioEmis()) * Integer.parseInt(compraVentaBean.getTitulos()))));
            }
        }

        if (IdMercado == 1 && ("".equals(Comision) || Comision == null)) {
            // Clean
            Comision = formatFormatPrec(Double.parseDouble("0.00"));

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comisión no puede estar vacío..."));
            respuesta = false;
        } else {
            if (IdMercado == 1 && !isDoubleNumeric(Comision)) {
                // Clean
                Comision = formatFormatPrec(Double.parseDouble("0.00"));

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Comisión debe ser un campo numérico..."));
                respuesta = false;
            } else if (IdMercado == 1 && Double.parseDouble(Comision) < 0) {
                // Clean
                Comision = formatFormatPrec(Double.parseDouble("0.00"));

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comisión debe ser mayor o igual a 0..."));
                respuesta = false;
            }
        }

        // _Valida_el_tipo_de_Moneda_y_Cambio
        //logger.debug("Tipo de Cambio: " + compraVentaBean.getTipoCambio());
        if (NombreMon != null && !"".equals(NombreMon)) {
            if (compraVentaBean.getMoneda() != 1 && !NombreMon.equals("MONEDA NACIONAL")) {
                if (compraVentaBean.getMoneda() != 1 && !NombreMon.equals("UNIDAD DE INVERSION")) {
                    if (compraVentaBean.getTipoCambio() == 0.0
                            || String.valueOf(compraVentaBean.getTipoCambio()).equals("")) {

                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de cambio para este dia..."));
                        respuesta = false;
                    }
                }
            }
        }

        if (compraVentaBean.getCustodio() == 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
            respuesta = false;
        }

        return respuesta;
    }

    public boolean ValidacionesNegocio() {

        //logger.debug("Ingresa a ValidacionesNegocio()" );
        try {
            FecValor = parseDate(compraVentaBean.getDiaOpera());
            int iDiaVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
            int iMesVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            int iYearVal = FecValor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

            //_Valida_si_es_compra_y_esta_calificado_el_contrato_con_costo_promedio
            //_No_se_puede_efectuar_una_compra_si_existen_ventas_posteriores_a_la_fecha_de
            //_Compra
            if (tipoMovimiento.equals("C") && compraVentaBean.getNomPizarra() != null) {

                //Valida_Si_Existen_Ventas_Posteriores
                if (cTesoreria.onTesoreria_SelectEntidadFin(compraVentaBean.getFideicomiso(),
                        compraVentaBean.getSubFiso(), compraVentaBean.getEntidadFinan(),
                        compraVentaBean.getCtoInver(), compraVentaBean.getNomPizarra(),
                        compraVentaBean.getSerieEmis(), iYearVal, iMesVal, iDiaVal)) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Existen ventas posteriores a la fecha de compra..."));
                    return false;
                }
            }

            //_Valida_si_la_emision_existe			
            if (!cTesoreria.onTesoreria_existeEmision(compraVentaBean.getNomPizarra().trim(),
                    compraVentaBean.getSerieEmis().trim(), compraVentaBean.getCuponVig(), IdInstrumento, IdMercado,
                    iSecEmis, iDiaVal, iMesVal, iYearVal)) {

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "No existe la Emisión " + compraVentaBean.getNomPizarra() + "/" + compraVentaBean.getSerieEmis() + "/" + compraVentaBean.getCuponVig()));
                return false;
            }

            //_Valida_si_el_contrato_tiene_liquidez
            if (tipoMovimiento.equals("V") || (tipoMovimiento.equals("C") && cTesoreria.onTesoreria_VerificaLiquidez(
                    compraVentaBean.getImporte(), compraVentaBean.getFideicomiso(), compraVentaBean.getSubFiso(),
                    compraVentaBean.getEntidadFinan(), compraVentaBean.getCtoInver(), compraVentaBean.getCustodio()))) {

                AplicaOperacion();

            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no tiene Líquidez"));
                compraVentaBean.setFideicomiso("");
                listaSubContrato = null;
                compraVentaBean.setSubFiso("");
                this.subFiso = "";
                DeshabilitarComponentes();
                init();
                return false;
            }

            return true;

        } catch (NumberFormatException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ValidacionesNegocio()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }

        return false;
    }

    public void AplicaOperacion() {
        int iRetieneISR = 0;
        double dValorIVA = 0;
        String sGarantia = "SINGARANTIA";

        //logger.debug("Ingresa a AplicaOperacion()" );
        dValorIVA = Double.parseDouble(Comision) * contratoBean.getImpuestoEspecial(); //logger.error(mensajeError);
        //logger.error("Causa: ".concat(exception.getCause().getMessage()));
        //exception.printStackTrace(new PrintWriter(stringWriter));
        //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        iRetieneISR = cTesoreria.onTesoreria_RecuperaISR(Integer.parseInt(compraVentaBean.getFideicomiso()));

        if (compraVentaBean.getMoneda() == 1) {
            compraVentaBean.setTipoCambio(1);
        } else {
            iRetieneISR = 0;
        }

        // Aplica_Compra
        if (tipoMovimiento.equals("C")) {

            // Aplica_SP_MD_CPA_DIRECTO
            outParameterBean = cTesoreria.onTesoreria_EjecutaCompraDirecto(compraVentaBean,
                    fechaContableBean.getFecha(), // fecha_contable
                    IdMercado,
                    IdInstrumento,
                    iSecEmis,
                    FecValorMSA,
                    Double.parseDouble(Comision),
                    dValorIVA,
                    "PANTALLA",
                    "COMPRAS EN DIRECTO",
                    iRetieneISR,
                    sGarantia);

            //compraVentaBean.getMoneda(),
            //fechaContableBean.getFecha(),  // fecha_contable
            //compraVentaBean.getDiaOpera(), // fecha_valor
            //											Integer.parseInt(compraVentaBean.getFideicomiso()),
            //											Integer.parseInt(compraVentaBean.getSubFiso()), 	compraVentaBean.getEntidadFinan(),
            //											compraVentaBean.getCtoInver(), 						IdMercado,
            //											IdInstrumento, 										iSecEmis,
            //											compraVentaBean.getNomPizarra(), 					compraVentaBean.getSerieEmis(),
            //											Integer.parseInt(compraVentaBean.getCuponVig()), 	FecValorMSA,
            //											compraVentaBean.getTitulos(),
            //											compraVentaBean.getPrecioEmis(), 					Comision,
            //											dValorIVA, 											"PANTALLA",
            //											"COMPRAS EN DIRECTO",								compraVentaBean.getCustodio(),
            //											iRetieneISR, 										sGarantia,
            //											compraVentaBean.getTipoCambio());
        } // COMPRA
        else { 	// VENTA

            // Aplica_SP_MD_VTA_DIRECTO
            outParameterBean = cTesoreria.onTesoreria_EjecutaVentaDirecto(compraVentaBean,
                    fechaContableBean.getFecha(), //_fecha_contable
                    IdMercado,
                    IdInstrumento,
                    iSecEmis,
                    FecValorMSA,
                    Double.parseDouble(Comision),
                    dValorIVA,
                    "PANTALLA",
                    "VENTAS EN DIRECTO",
                    iRetieneISR,
                    sGarantia);

//																compraVentaBean.getMoneda(),
//																fechaContableBean.getFecha(), //_fecha_contable
//																compraVentaBean.getDiaOpera(), //_fecha valor
//																Integer.parseInt(compraVentaBean.getFideicomiso()),
//																Integer.parseInt(compraVentaBean.getSubFiso()), 		compraVentaBean.getEntidadFinan(),
//																compraVentaBean.getCtoInver(), 							IdMercado, 
//																IdInstrumento, 											iSecEmis,
//																compraVentaBean.getNomPizarra(), 						compraVentaBean.getSerieEmis(),
//																Integer.parseInt(compraVentaBean.getCuponVig()), 		FecValorMSA,
//																compraVentaBean.getTitulos(),
//																compraVentaBean.getPrecioEmis(), 						Comision,
//																dValorIVA, 												"PANTALLA",
//																"VENTAS EN DIRECTO", 									compraVentaBean.getCustodio(), 
//																iRetieneISR, 											sGarantia);
        }
        // Aplicado_correctamente
        if (outParameterBean.getiNumFolioContab() > 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "Movimiento Aplicado correctamente con Folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
            this.listaSubContrato = null;
            this.subFiso = "";
            compraVentaBean.setFideicomiso("");
            compraVentaBean.setSubFiso("");

            tipoMovimiento = "C";
            HabilitaPanelInv = true;
            SelectTipoMov();
            DeshabilitarComponentes();

            init();
        } // No_Aplicado
        else {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                    "El Movimiento No se Aplicó: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", "")))));
        }
    }

    public void DeshabilitarComponentes() {
        //logger.debug("Ingresa a DeshabilitarComponentes()");

        // Clean_Bean
        posicion = null;
        emisionBean = new EmisionBean();
        posicionBean = new PosicionBean();
        outParameterBean = new OutParameterBean();

        // Variables
        mercado = "";
        sValEmis = "";
        sCtoInver = "";
        tipoValor = "";
        NombreMon = "";
        CtaCheques = "";
        instrumento = "";
        Comision = formatFormatPrec(Double.parseDouble("0.00"));
        iSecEmis = 0;
        IdMercado = 0;
        IdInstrumento = 0;

        // Buttons
        habilitaSelCtoInv = 0;
        habilitaTxtPrecio = 0;
        habilitaTxtTitulos = 0;
        visiblePanelMoneda = 1;
        habilitaSelCustodio = 0;
        visiblePanelComision = 1;
        habilitaSelTipoValor = 0;
        habilitaSelEmisiones = 0;
        habilitaBotonCancelar = 0;
        habilitaTxtSubFiso = true;

        // Clean_Campos
        compraVentaBean.setMoneda(0);
        compraVentaBean.setTitulos("0");
        compraVentaBean.setImporte(0);
        compraVentaBean.setCtoInver(0);
        compraVentaBean.setCuponVig(null);
        compraVentaBean.setCustodio(0);
        compraVentaBean.setSerieEmis(null);
        compraVentaBean.setPrecioEmis("0");
        compraVentaBean.setNomPizarra(null);
        compraVentaBean.setEntidadFinan(0);
    }

    public void EstablecerCustodio() {
        try {
            //logger.debug("Ingresa a EstablecerCustodio()" );

            for (Map.Entry<String, String> o : custodio.entrySet()) {
                if (o.getKey().equals(posicionBean.getNomCustodio())) {
                    compraVentaBean.setCustodio(Integer.parseInt(o.getKey()));
                }
            }
            return;

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " EstablecerCustodio()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void checkComision() {
        // Write_Log
        //logger.debug("Ingresa a checkComision()");

        if ("".equals(Comision) || Comision == null) {
            // Clean
            Comision = formatFormatPrec(Double.parseDouble("0.00"));

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comisión no puede estar vacío..."));
            return;
        }
        Comision = Comision.trim();
        if (!isDoubleNumeric(Comision)) {
            // Clean
            Comision = formatFormatPrec(Double.parseDouble("0.00"));

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Comisión debe ser un campo numérico..."));
            return;
        }

        if (Double.parseDouble(Comision) < 0) {
            // Clean
            Comision = formatFormatPrec(Double.parseDouble("0.00"));

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comisión debe ser mayor o igual a 0..."));
            return;
        }

        Comision = formatFormatPrec(Double.parseDouble(Comision));
    }

    private static boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
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
