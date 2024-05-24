/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBHonorarios.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.controller;


//Imports
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
import org.primefaces.event.SelectEvent;

import scotiaFid.bean.DetCartBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.OtrosIngresosBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.PacaHonBean;
import scotiaFid.bean.ParticipanteBean;
import scotiaFid.bean.ProductoBean;
import scotiaFid.bean.ServicioBean;
import scotiaFid.dao.CHonorarios;
import scotiaFid.util.LogsContext;
import scotiaFid.util.CValidacionesUtil;

//Class
@ManagedBean(name = "mbHonorarios")
@ViewScoped
public class MBHonorarios implements Serializable {
    private static final Logger logger = LogManager.getLogger(MBHonorarios.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R I A L
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * I N Y E C C I O N   D E   B E A N S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private OtrosIngresosBean otrosIngresosBean;
    //-----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean;
    //-----------------------------------------------------------------------------
    private ProductoBean productoBean;
    //-----------------------------------------------------------------------------
    private PacaHonBean pacaHonBean;
    //-----------------------------------------------------------------------------
    private DetCartBean detCartBean = new DetCartBean();
    //-----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    //-----------------------------------------------------------------------------
    private static HttpServletRequest peticionURL;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * O B J E T O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<String> ivas = new ArrayList<>();
    //-----------------------------------------------------------------------------
    private Map<String, String> monedas = new LinkedHashMap<String, String>();
    //-----------------------------------------------------------------------------
    private Map<String, String> servicios = new LinkedHashMap<String, String>();
    //-----------------------------------------------------------------------------
    private List<DetCartBean> pagos = new ArrayList<>();
    //-----------------------------------------------------------------------------
    private List<String> parameters = new ArrayList<>();
    //-----------------------------------------------------------------------------
    private List<ParticipanteBean> participantes = new ArrayList<>();
    //-----------------------------------------------------------------------------
    private Map<String, String> metodosPago = new LinkedHashMap<String, String>();
    //-----------------------------------------------------------------------------
    private List<String> payments = new ArrayList<>();
    //-----------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * F O R M A T O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //-------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    //-------------------------------------------------------------------------
    private static final DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //-------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * A T R I B U T O S   P R I V A D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private int moneda;
    //-------------------------------------------------------------------------
    private int deudaEspecifica;
    //-------------------------------------------------------------------------
    private int habilitaBotonBorrar;
    //-------------------------------------------------------------------------
    private int habilitaBotonAceptar;
    //-------------------------------------------------------------------------
    private int habilitaBotonCancelar;
    //-------------------------------------------------------------------------
    private int habilitaBotonGenerarPago;
    //-------------------------------------------------------------------------
    private int habilitaTextCantidad;
    //-------------------------------------------------------------------------
    private int habilitaTextPrecio;
    //-------------------------------------------------------------------------
    private int habilitaTextImporte;
    //-------------------------------------------------------------------------
    private int habilitaComboServicio;
    //-------------------------------------------------------------------------
    private int visiblePanelMoneda;
    //-------------------------------------------------------------------------
    private int visiblePanelPeriodo;
    //-------------------------------------------------------------------------
    private int visibleCheckBox;
    //-------------------------------------------------------------------------
    private int visibleTipoCambio;
    //-------------------------------------------------------------------------
    private double iva;
    //-------------------------------------------------------------------------
    private double ivaFronterizo;
    //-------------------------------------------------------------------------
    private double totalAdeudo;
    //-------------------------------------------------------------------------
    private double totalAdeudoDolar;
    //-------------------------------------------------------------------------
    private String importeTotal;
    //-------------------------------------------------------------------------
    private double honorario;
    //-------------------------------------------------------------------------
    private double moratorio;
    //-------------------------------------------------------------------------
    private double ivaHonorario;
    //-------------------------------------------------------------------------
    private double ivaMoratorio;
    //-------------------------------------------------------------------------
    private double totalCondonacion;
    //-------------------------------------------------------------------------
    private double totalCartera;
    //-------------------------------------------------------------------------
    private String honorarioFormat;
    //-------------------------------------------------------------------------
    private String moratorioFormat;
    //-------------------------------------------------------------------------
    private String ivaHonorarioFormat;
    //-------------------------------------------------------------------------
    private String ivaMoratorioFormat;
    //-------------------------------------------------------------------------
    private String totalCondonacionFormat;
    //-------------------------------------------------------------------------
    private String totalAdeudoDolarFormat;
    //-------------------------------------------------------------------------
    private String totalAdeudoFormat;
    //-------------------------------------------------------------------------
    private String metodoPago;
    //-------------------------------------------------------------------------
    private boolean especificaFactura;
    //-------------------------------------------------------------------------
    private boolean seCambiaMoneda;
    //-------------------------------------------------------------------------
    private String mensajeError;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R V I C I O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    CHonorarios cHonorarios = new CHonorarios();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * G E T T E R S   Y   S E T T E R S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public OtrosIngresosBean getOtrosIngresosBean() {
        return otrosIngresosBean;
    }

    public void setOtrosIngresosBean(OtrosIngresosBean otrosIngresosBean) {
        this.otrosIngresosBean = otrosIngresosBean;
    }

    public FechaContableBean getFechaContableBean() {
        return fechaContableBean;
    }

    public void setFechaContableBean(FechaContableBean fechaContableBean) {
        this.fechaContableBean = fechaContableBean;
    }

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public PacaHonBean getPacaHonBean() {
        return pacaHonBean;
    }

    public DetCartBean getDetCartBean() {
        return detCartBean;
    }

    public void setDetCartBean(DetCartBean detCartBean) {
        this.detCartBean = detCartBean;
    }

    public void setPacaHonBean(PacaHonBean pacaHonBean) {
        this.pacaHonBean = pacaHonBean;
    }

    public OutParameterBean getOutParameterBean() {
        return outParameterBean;
    }

    public void setOutParameterBean(OutParameterBean outParameterBean) {
        this.outParameterBean = outParameterBean;
    }

    public List<String> getIvas() {
        return ivas;
    }

    public void setIvas(List<String> ivas) {
        this.ivas = ivas;
    }

    public Map<String, String> getMonedas() {
        return monedas;
    }

    public void setMonedas(Map<String, String> monedas) {
        this.monedas = monedas;
    }

    public Map<String, String> getServicios() {
        return servicios;
    }

    public void setServicios(Map<String, String> servicios) {
        this.servicios = servicios;
    }

    public List<DetCartBean> getPagos() {
        return pagos;
    }

    public void setPagos(List<DetCartBean> pagos) {
        this.pagos = pagos;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<ParticipanteBean> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteBean> participantes) {
        this.participantes = participantes;
    }

    public Map<String, String> getMetodosPago() {
        return metodosPago;
    }

    public void setMetodosPago(Map<String, String> metodosPago) {
        this.metodosPago = metodosPago;
    }

    public List<String> getPayments() {
        return payments;
    }

    public void setPayments(List<String> payments) {
        this.payments = payments;
    }

    public int getHabilitaBotonBorrar() {
        return habilitaBotonBorrar;
    }

    public void setHabilitaBotonBorrar(int habilitaBotonBorrar) {
        this.habilitaBotonBorrar = habilitaBotonBorrar;
    }

    public int getHabilitaBotonAceptar() {
        return habilitaBotonAceptar;
    }

    public void setHabilitaBotonAceptar(int habilitaBotonAceptar) {
        this.habilitaBotonAceptar = habilitaBotonAceptar;
    }

    public int getHabilitaBotonCancelar() {
        return habilitaBotonCancelar;
    }

    public void setHabilitaBotonCancelar(int habilitaBotonCancelar) {
        this.habilitaBotonCancelar = habilitaBotonCancelar;
    }

    public int getHabilitaBotonGenerarPago() {
        return habilitaBotonGenerarPago;
    }

    public void setHabilitaBotonGenerarPago(int habilitaBotonGenerarPago) {
        this.habilitaBotonGenerarPago = habilitaBotonGenerarPago;
    }

    public int getHabilitaTextCantidad() {
        return habilitaTextCantidad;
    }

    public void setHabilitaTextCantidad(int habilitaTextCantidad) {
        this.habilitaTextCantidad = habilitaTextCantidad;
    }

    public int getHabilitaTextPrecio() {
        return habilitaTextPrecio;
    }

    public void setHabilitaTextPrecio(int habilitaTextPrecio) {
        this.habilitaTextPrecio = habilitaTextPrecio;
    }

    public int getHabilitaTextImporte() {
        return habilitaTextImporte;
    }

    public void setHabilitaTextImporte(int habilitaTextImporte) {
        this.habilitaTextImporte = habilitaTextImporte;
    }

    public int getHabilitaComboServicio() {
        return habilitaComboServicio;
    }

    public void setHabilitaComboServicio(int habilitaComboServicio) {
        this.habilitaComboServicio = habilitaComboServicio;
    }

    public int getVisiblePanelMoneda() {
        return visiblePanelMoneda;
    }

    public void setVisiblePanelMoneda(int visiblePanelMoneda) {
        this.visiblePanelMoneda = visiblePanelMoneda;
    }

    public int getVisiblePanelPeriodo() {
        return visiblePanelPeriodo;
    }

    public void setVisiblePanelPeriodo(int visiblePanelPeriodo) {
        this.visiblePanelPeriodo = visiblePanelPeriodo;
    }

    public int getVisibleCheckBox() {
        return visibleCheckBox;
    }

    public void setVisibleCheckBox(int visibleCheckBox) {
        this.visibleCheckBox = visibleCheckBox;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getIvaFronterizo() {
        return ivaFronterizo;
    }

    public void setIvaFronterizo(double ivaFronterizo) {
        this.ivaFronterizo = ivaFronterizo;
    }

    public int getVisibleTipoCambio() {
        return visibleTipoCambio;
    }

    public void setVisibleTipoCambio(int visibleTipoCambio) {
        this.visibleTipoCambio = visibleTipoCambio;
    }

    public double getTotalAdeudo() {
        return totalAdeudo;
    }

    public void setTotalAdeudo(double totalAdeudo) {
        this.totalAdeudo = totalAdeudo;
    }

    public String getTotalAdeudoFormat() {
        return totalAdeudoFormat;
    }

    public void setTotalAdeudoFormat(String totalAdeudoFormat) {
        this.totalAdeudoFormat = totalAdeudoFormat;
    }

    public String getTotalAdeudoDolarFormat() {
        return totalAdeudoDolarFormat;
    }

    public void setTotalAdeudoDolarFormat(String totalAdeudoDolarFormat) {
        this.totalAdeudoDolarFormat = totalAdeudoDolarFormat;
    }

    public double getTotalAdeudoDolar() {
        return totalAdeudoDolar;
    }

    public void setTotalAdeudoDolar(double totalAdeudoDolar) {
        this.totalAdeudoDolar = totalAdeudoDolar;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getImporteTotal() {
        return importeTotal;
    }

    public double getHonorario() {
        return honorario;
    }

    public void setHonorario(double honorario) {
        this.honorario = honorario;
    }

    public double getMoratorio() {
        return moratorio;
    }

    public void setMoratorio(double moratorio) {
        this.moratorio = moratorio;
    }

    public double getIvaHonorario() {
        return ivaHonorario;
    }

    public void setIvaHonorario(double ivaHonorario) {
        this.ivaHonorario = ivaHonorario;
    }

    public double getIvaMoratorio() {
        return ivaMoratorio;
    }

    public void setIvaMoratorio(double ivaMoratorio) {
        this.ivaMoratorio = ivaMoratorio;
    }

    public double getTotalCondonacion() {
        return totalCondonacion;
    }

    public void setTotalCondonacion(double totalCondonacion) {
        this.totalCondonacion = totalCondonacion;
    }

    public double getTotalCartera() {
        return totalCartera;
    }

    public void setTotalCartera(double totalCartera) {
        this.totalCartera = totalCartera;
    }

    public void setImporteTotal(String importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getDeudaEspecifica() {
        return deudaEspecifica;
    }

    public void setDeudaEspecifica(int deudaEspecifica) {
        this.deudaEspecifica = deudaEspecifica;
    }

    public boolean isEspecificaFactura() {
        return especificaFactura;
    }

    public void setEspecificaFactura(boolean especificaFactura) {
        this.especificaFactura = especificaFactura;
    }

    public String getIvaHonorarioFormat() {
        return ivaHonorarioFormat;
    }

    public void setIvaHonorarioFormat(String ivaHonorarioFormat) {
        this.ivaHonorarioFormat = ivaHonorarioFormat;
    }

    public String getMoratorioFormat() {
        return moratorioFormat;
    }

    public void setMoratorioFormat(String moratorioFormat) {
        this.moratorioFormat = moratorioFormat;
    }

    public String getIvaMoratorioFormat() {
        return ivaMoratorioFormat;
    }

    public void setIvaMoratorioFormat(String ivaMoratorioFormat) {
        this.ivaMoratorioFormat = ivaMoratorioFormat;
    }

    public String getHonorarioFormat() {
        return honorarioFormat;
    }

    public void setHonorarioFormat(String honorarioFormat) {
        this.honorarioFormat = honorarioFormat;
    }

    public String getTotalCondonacionFormat() {
        return totalCondonacionFormat;
    }

    public void setTotalCondonacionFormat(String totalCondonacionFormat) {
        this.totalCondonacionFormat = totalCondonacionFormat;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
   * C O N S T R U C T O R
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBHonorarios() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * M E T O D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @PostConstruct
    public void init() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                //System.out.println("Sesión no valida ...");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
            //Get_IVA
            ivas = cHonorarios.onHonorarios_GetIVA();

            //Get_Monedas
            monedas = cHonorarios.onHonorarios_GetMonedas();

            //Get_Servicios
            servicios = cHonorarios.onHonorarios_GetServicios();

            //Get_Fecha_Contable
            fechaContableBean = cHonorarios.onHonorarios_GetFechaContable();

            //Get_Metodos_Pago
            metodosPago = cHonorarios.onHonorarios_GetMetodosPago();

            //Format_Fecha_Contable
            fechaContableBean.setFechaContable(formatDate2(fechaContableBean.getFecha()));

            //Write_Console
//            out.println("Fecha Contable: ".concat(fechaContableBean.getFechaContable()));
            //Set_Ivas
            iva = CValidacionesUtil.validarDouble(ivas.get(0));
            ivaFronterizo = CValidacionesUtil.validarDouble(ivas.get(1));

            //Clean_Total_Adeudo
            totalAdeudo = 0;
            totalAdeudoDolar = 0;

            //Set_Disabled_Buttons
            habilitaBotonBorrar = 0;
            habilitaBotonAceptar = 0;
            habilitaBotonCancelar = 0;
            habilitaBotonGenerarPago = 0;

            //Set_Disabled_Textbox
            habilitaTextCantidad = 0;
            habilitaTextPrecio = 0;
            habilitaTextImporte = 0;

            //Set_Disabled_Combo
            habilitaComboServicio = 0;

            //Set_Visible_Panel_Components
            visiblePanelMoneda = 0;
            visiblePanelPeriodo = 0;
            visibleCheckBox = 0;
            visibleTipoCambio = 0;
            payments = new ArrayList<>();
            seCambiaMoneda = false;
            
            //Set_Bean
            this.otrosIngresosBean = new OtrosIngresosBean();
            }

        } catch (IOException | NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        }

    }

    public void resetComboTipoCambio() {
        //Write_Console
//        out.println("Reset Combo Tipo Cambio...");

        //Hide_InputBox_TipoCambio
        visibleTipoCambio = 0;

        //Reset_Combo_Moneda
        if(!seCambiaMoneda){
            otrosIngresosBean.setMoneda(monedas.get("1"));
        }
        
        seCambiaMoneda = false;
    }

    public void cleanOtrosIngresos() {
        //Write_Console
//        out.println("Cleaning OtrosIngresos...");

        //Set_Disabled_Buttons
        habilitaBotonBorrar = 0;
        habilitaBotonAceptar = 0;
        habilitaBotonCancelar = 0;

        //Set_Disabled_Textbox
        habilitaTextCantidad = 0;
        habilitaTextPrecio = 0;
        habilitaTextImporte = 0;

        //Set_Visible_Panel
        visiblePanelMoneda = 0;
        visiblePanelPeriodo = 0;
        visibleCheckBox = 0;
        visibleTipoCambio = 0;

        //Hide_Window_TipoCambio
        RequestContext.getCurrentInstance().execute("dlgTipoCambio.hide()");

        //Load
        init();
    }

    public void cleanPagosHonorarios() {
        //Write_Console
//        out.println("Cleaning...");

        //Clean_Bean
        detCartBean = new DetCartBean();

        //Clean_Importe_Total
        importeTotal = "0.00";

        //Set_Metodo_Pago
        metodoPago = metodosPago.get("A SATISFACCIÓN DEL ACREEDOR");

        //Set_Especifica_Factura
        especificaFactura = false;
    }

    public void checkFideicomisoOtrosIngresos() throws SQLException {
        
        //Check_Is_Numeric
        try {
            if (!isNumeric(otrosIngresosBean.getFideicomiso())) {
                //Disabled_Combo_Servicios
                habilitaComboServicio = 0;

                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_InputText_Precio
                habilitaTextPrecio = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Reset_Combo_Servicios
                otrosIngresosBean.setServicio("");

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Clean_CheckBOx
                otrosIngresosBean.setActualizaFecha(false);

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Hide_Panel_Moneda
                visiblePanelMoneda = 0;

                //Hide_Panel_Periodo
                visiblePanelPeriodo = 0;

                //Hide_Check_Box
                visibleCheckBox = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                return;
            }

            //Check_Fideicomiso
            if ("".equals(otrosIngresosBean.getFideicomiso())) {
                //Disabled_Combo_Servicios
                habilitaComboServicio = 0;

                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_InputText_Precio
                habilitaTextPrecio = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Reset_Combo_Servicios
                otrosIngresosBean.setServicio("");

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Clean_CheckBOx
                otrosIngresosBean.setActualizaFecha(false);

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Hide_Panel_Moneda
                visiblePanelMoneda = 0;

                //Hide_Panel_Periodo
                visiblePanelPeriodo = 0;

                //Hide_Check_Box
                visibleCheckBox = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
                return;
            }

            if (Integer.parseInt(otrosIngresosBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                //Disabled_Combo_Servicios
                habilitaComboServicio = 0;

                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_InputText_Precio
                habilitaTextPrecio = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Reset_Combo_Servicios
                otrosIngresosBean.setServicio("");

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Clean_CheckBOx
                otrosIngresosBean.setActualizaFecha(false);

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Hide_Panel_Moneda
                visiblePanelMoneda = 0;

                //Hide_Panel_Periodo
                visiblePanelPeriodo = 0;

                //Hide_Check_Box
                visibleCheckBox = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es invalido..."));
                return;
            }

            if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso())) == false) {
                //Disabled_Combo_Servicios
                habilitaComboServicio = 0;

                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_InputText_Precio
                habilitaTextPrecio = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Reset_Combo_Servicios
                otrosIngresosBean.setServicio("");

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Clean_CheckBox
                otrosIngresosBean.setActualizaFecha(false);

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Hide_Panel_Moneda
                visiblePanelMoneda = 0;

                //Hide_Panel_Periodo
                visiblePanelPeriodo = 0;

                //Hide_Check_Box
                visibleCheckBox = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                return;
            }
            
            //Reset_Combo_Servicios
            otrosIngresosBean.setServicio("");

            //Clean_Cantidad
            otrosIngresosBean.setCantidad("");

            //Clean_Precio_Unitario
            otrosIngresosBean.setPrecioUnitario("");

            //Clean_Importe
            otrosIngresosBean.setImporte("");

            //Clean_IVA
            otrosIngresosBean.setIva("");

            //Clean_Total
            otrosIngresosBean.setTotal("");

            //Clean_Importe_Format
            otrosIngresosBean.setImporteFormat("");

            //Clean_IVA_Format
            otrosIngresosBean.setIvaFormat("");

            //Clean_Total_Format
            otrosIngresosBean.setTotalFormat("");

            //Clean_Tipo_Cambio
            otrosIngresosBean.setTipoCambio("");

            //Clean_CheckBOx
            otrosIngresosBean.setActualizaFecha(false);

            //Reset_Tipo_Cambio
            resetComboTipoCambio();

            //Hide_Tipo_Cambio
            visibleTipoCambio = 0;

            //Hide_Panel_Moneda
            visiblePanelMoneda = 0;

            //Hide_Panel_Periodo
            visiblePanelPeriodo = 0;

            //Hide_Check_Box
            visibleCheckBox = 0;

            //Disabled_InputText_Cantidad
            habilitaTextCantidad = 0;

            //Disabled_InputText_Precio
            habilitaTextPrecio = 0;

            //Set_Enabled_Buttons
            habilitaBotonBorrar = 1;
            habilitaBotonCancelar = 1;

            //Set_Enabled_Combo_Servicios
            habilitaComboServicio = 1;

            //Get_Moneda
            moneda = cHonorarios.onHonorarios_GetMonedaXFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso()));

            //Write_Console
            //out.println("MONEDA: ".concat(String.valueOf(moneda)));
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            //logger.error(Err.getMessage() + "MBHonorarios_checkFideicomisoOtrosIngresos()");
        } 
    }

    public void selectServicio() throws SQLException {
        //Write_Console
//        out.println("Select Servicio...");
        try {
            //Validate_Servicio
            if (otrosIngresosBean.getServicio() == null) {
                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un Servicio..."));
                return;
            }

            if ("0".equals(otrosIngresosBean.getServicio())) {
                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un Servicio..."));
                return;
            }

            //Check_Fideicomiso
            if ("".equals(otrosIngresosBean.getFideicomiso())) {
                //Disabled_InputText_Cantidad
                habilitaTextCantidad = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Clean_Cantidad
                otrosIngresosBean.setCantidad("");

                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Reset_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe capturar un Fideicomiso..."));
                return;
            }

            //Write_Console
//        out.println("Select No. Servicio: ".concat(otrosIngresosBean.getServicio()));
//        out.println("Select Nombre Servicio: ".concat(servicios.get(otrosIngresosBean.getServicio())));
//        out.println("Select Fideicomiso: ".concat(otrosIngresosBean.getFideicomiso()));
            //Clean_Cantidad
            otrosIngresosBean.setCantidad("");

            //Clean_Precio_Unitario
            otrosIngresosBean.setPrecioUnitario("");

            //Clean_Importe
            otrosIngresosBean.setImporte("");

            //Clean_IVA
            otrosIngresosBean.setIva("");

            //Clean_Total
            otrosIngresosBean.setTotal("");

            //Clean_Importe_Format
            otrosIngresosBean.setImporteFormat("");

            //Clean_IVA_Format
            otrosIngresosBean.setIvaFormat("");

            //Clean_Total_Format
            otrosIngresosBean.setTotalFormat("");

            //Clean_Tipo_Cambio
            otrosIngresosBean.setTipoCambio("");

            //Reset_Tipo_Cambio
            resetComboTipoCambio();

            //Hide_Tipo_Cambio
            visibleTipoCambio = 0;

            //Disabled_Button_Aceptar
            habilitaBotonAceptar = 0;

            if ("HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio()).trim())) {
                //Write_Console
//            out.println("HONORARIOS POR ADMINISTRACION");

                //Set_Visible_Panel
                visiblePanelMoneda = 0;
                visiblePanelPeriodo = 1;
                visibleCheckBox = 1;

                //Get_Fechas
                List<String> fechas = cHonorarios.onHonorarios_GetFechasCalculo(Integer.parseInt(otrosIngresosBean.getFideicomiso()));

                if(fechas.isEmpty()){
                    habilitaTextCantidad = 1;
                }
                //Set Value_Fechas_To_Bean
                otrosIngresosBean.setFechaDel(new SimpleDateFormat("yyyy-MM-dd").parse(fechas.get(0)));
                otrosIngresosBean.setFechaAl(new SimpleDateFormat("yyyy-MM-dd").parse(fechas.get(1)));
            } else {

                if (servicios.get(otrosIngresosBean.getServicio()).indexOf("HONORARIOS POR") == 0) {
                    //Set_Visible_Panel
                    visiblePanelMoneda = 0;
                    visiblePanelPeriodo = 0;
                    visibleCheckBox = 0;
                } else {
                    //Set_Visible_Panel
                    visiblePanelMoneda = 1;
                    visiblePanelPeriodo = 0;
                    visibleCheckBox = 0;
                }

            }

            //Disabled_InputText_Precio
            habilitaTextPrecio = 0;

            //Enabled_InputText_Cantidad
            habilitaTextCantidad = 1;

            //Write_Console
//        out.println("visiblePanelMoneda: ".concat(String.valueOf(visiblePanelMoneda)));
//        out.println("visiblePanelMoneda: ".concat(String.valueOf(visiblePanelMoneda)));
//        out.println("visiblePanelPeriodo: ".concat(String.valueOf(visiblePanelPeriodo)));
//        out.println("visibleTipoCambio: ".concat(String.valueOf(visibleTipoCambio)));
        } catch (ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void checkCantidad() throws SQLException {
        //Write_Console
//        out.println("Check Cantidad...");
        try {
            //Format
            NumberFormat numberFormat = NumberFormat.getNumberInstance();

            //Check_Is_Numeric
            if (!isNumeric(otrosIngresosBean.getCantidad())) {
                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Disabled_Text_Precio
                habilitaTextPrecio = 0;

                //Disabled_Text_Importe
                habilitaTextImporte = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Cantidad. Debe ser numérico o no debe estar vacío..."));

                return;
            }

            if (!"".trim().equals(otrosIngresosBean.getCantidad())) {
//            out.println("Cantidad: ".concat(otrosIngresosBean.getCantidad()));

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) == 0) {
                    //Clean_Precio_Unitario
                    otrosIngresosBean.setPrecioUnitario("");

                    //Clean_Importe
                    otrosIngresosBean.setImporte("");

                    //Clean_IVA
                    otrosIngresosBean.setIva("");

                    //Clean_Total
                    otrosIngresosBean.setTotal("");

                    //Clean_Tipo_Cambio
                    otrosIngresosBean.setTipoCambio("");

                    //Clean_Importe_Format
                    otrosIngresosBean.setImporteFormat("");

                    //Clean_IVA_Format
                    otrosIngresosBean.setIvaFormat("");

                    //Clean_Total_Format
                    otrosIngresosBean.setTotalFormat("");

                    //Hide_Tipo_Cambio
                    visibleTipoCambio = 0;

                    //Reset_Combo_Tipo_Cambio
                    resetComboTipoCambio();

                    //Disabled_Button_Aceptar
                    habilitaBotonAceptar = 0;

                    //Set_Message 
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser mayor a cero..."));
                    return;
                }

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) > 0) {
                    if ("HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio())) || "HONORARIOS POR ACEPTACION".equals(servicios.get(otrosIngresosBean.getServicio()))) {
                        //Enabled_InputText_PrecioUnitario
                        habilitaTextPrecio = 1;

                        if (!"".equals(otrosIngresosBean.getPrecioUnitario())) {
                            //Clean_Precio_Unitario
                            otrosIngresosBean.setPrecioUnitario("");

                            //Clean_Importe
                            otrosIngresosBean.setImporte("");

                            //Clean_IVA
                            otrosIngresosBean.setIva("");

                            //Clean_Total
                            otrosIngresosBean.setTotal("");

                            //Clean_Importe_Format
                            otrosIngresosBean.setImporteFormat("");

                            //Clean_IVA_Format
                            otrosIngresosBean.setIvaFormat("");

                            //Clean_Total_Format
                            otrosIngresosBean.setTotalFormat("");

                            //Clean_Tipo_Cambio
                            otrosIngresosBean.setTipoCambio("");

                            //Disabled_Button_Aceptar
                            habilitaBotonAceptar = 0;
                        }
                    } else {
                        //Enabled_InputText_PrecioUnitario	
                        habilitaTextPrecio = 1;

                        //Get_Servicio
                        ServicioBean servicioBean = cHonorarios.onHonorarios_GetNumeroServicio(otrosIngresosBean.getServicio());

                        if (servicioBean.getTarifa() == 0) {
                            //Clean_Precio_Unitario
                            otrosIngresosBean.setPrecioUnitario("");

                            //Clean_Importe
                            otrosIngresosBean.setImporte("");

                            //Clean_IVA
                            otrosIngresosBean.setIva("");

                            //Clean_Total
                            otrosIngresosBean.setTotal("");

                            //Clean_Importe_Format
                            otrosIngresosBean.setImporteFormat("");

                            //Clean_IVA_Format
                            otrosIngresosBean.setIvaFormat("");

                            //Clean_Total_Format
                            otrosIngresosBean.setTotalFormat("");

                            //Clean_Tipo_Cambio
                            otrosIngresosBean.setTipoCambio("");

                            //Reset_Tipo_Cambio
                            resetComboTipoCambio();

                            //Disabled_Button_Aceptar
                            habilitaBotonAceptar = 0;

                            //Set_Message 
                            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Este servicio no tiene tarifa, favor de proporcionar el valor correspondiente..."));
                            return;
                        } else {
                            //Write_Console
//                        out.println("I.V.A.".concat(String.valueOf(iva)));

                            //Set_Values_To_Bean
                            otrosIngresosBean.setPrecioUnitario(String.valueOf(servicioBean.getTarifa()));
                            otrosIngresosBean.setImporte(String.format("%.2f", Double.parseDouble(otrosIngresosBean.getPrecioUnitario()) * Integer.parseInt(otrosIngresosBean.getCantidad())));
                            otrosIngresosBean.setIva(String.format("%.2f", ((Integer.parseInt(otrosIngresosBean.getCantidad()) * Double.parseDouble(otrosIngresosBean.getPrecioUnitario())) * iva)));
                            otrosIngresosBean.setTotal(String.format("%.2f", CValidacionesUtil.validarDouble(otrosIngresosBean.getImporte()) + Double.parseDouble(otrosIngresosBean.getIva())));

                            //Set_Values_To_Bean_Format
                            otrosIngresosBean.setImporteFormat(numberFormat.format(Double.parseDouble(otrosIngresosBean.getImporte())));
                            otrosIngresosBean.setIvaFormat(numberFormat.format(CValidacionesUtil.validarDouble(otrosIngresosBean.getIva())));
                            otrosIngresosBean.setTotalFormat(numberFormat.format(CValidacionesUtil.validarDouble(otrosIngresosBean.getTotal())));

                            //Disabled_InputText_Importe
                            habilitaTextImporte = 0;
                        }
                    }
                }
            } else {
                //Clean_Precio_Unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Disabled_Text_Precio
                habilitaTextPrecio = 0;

                //Disabled_Text_Importe
                habilitaTextImporte = 0;

                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el campo cantidad..."));
                return;

            }
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 

    }

    public void checkPrecioUnitario() {
        //Write_Console
//        out.println("Check Precio Unitario...");
//        out.println("Precio Unitario:".concat(otrosIngresosBean.getPrecioUnitario()));
        try {
            //Format
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            otrosIngresosBean.setPrecioUnitario(otrosIngresosBean.getPrecioUnitario().replace(",", ""));
            
            //Check_Field_Cantidad
            if ("".trim().equals(otrosIngresosBean.getCantidad())) {
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Precio Unitario. Debe ser numérico o no puede estar vacío..."));

                return;
            }

            //Check_Is_Numeric
            if (!isDoubleNumeric(otrosIngresosBean.getPrecioUnitario().replace(",", ""))) {
                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Precio Unitario. Debe ser numérico o no puede estar vacío..."));

                return;
            }

            //Check_Field_Cantidad
            if ("".trim().equals(otrosIngresosBean.getCantidad())) {
                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el campo cantidad ..."));

                return;
            }

            if (Integer.parseInt(otrosIngresosBean.getCantidad()) <= 0) {
                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");
                

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La cantidad debe ser mayor a cero..."));

                return;
            }

            //Check_Field_Precio_Unitario
            if ("".trim().equals(otrosIngresosBean.getPrecioUnitario().replace(",", ""))) {
                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el campo precio unitario ..."));

                return;
            }

            if (Double.parseDouble(otrosIngresosBean.getPrecioUnitario().replace(",", "")) <= 0) {
                //Clean_Importe
                otrosIngresosBean.setImporte("");

                //Clean_IVA
                otrosIngresosBean.setIva("");

                //Clean_Total
                otrosIngresosBean.setTotal("");

                //Clean_Importe_Format
                otrosIngresosBean.setImporteFormat("");

                //Clean_IVA_Format
                otrosIngresosBean.setIvaFormat("");

                //Clean_Total_Format
                otrosIngresosBean.setTotalFormat("");

                //Clean_Tipo_Cambio
                otrosIngresosBean.setTipoCambio("");
                
                //Clean_precio_unitario
                otrosIngresosBean.setPrecioUnitario("");

                //Hide_Tipo_Cambio
                visibleTipoCambio = 0;

                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El precio unitario debe ser mayor a cero..."));

                return;
            }

            if (!"".equals(otrosIngresosBean.getPrecioUnitario())) {
                //Set_Values_To_Bean
                otrosIngresosBean.setImporte(String.format("%.2f", Double.parseDouble(otrosIngresosBean.getPrecioUnitario()) * Integer.parseInt(otrosIngresosBean.getCantidad())));
                otrosIngresosBean.setIva(String.format("%.2f", ((Integer.parseInt(otrosIngresosBean.getCantidad()) * Double.parseDouble(otrosIngresosBean.getPrecioUnitario())) * iva)));
                otrosIngresosBean.setTotal(String.format("%.2f", CValidacionesUtil.validarDouble(otrosIngresosBean.getImporte()) + Double.parseDouble(otrosIngresosBean.getIva())));

                //Set_Values_To_Bean_Format
                otrosIngresosBean.setImporteFormat(numberFormat.format(Double.parseDouble(otrosIngresosBean.getImporte())));
                otrosIngresosBean.setIvaFormat(numberFormat.format(CValidacionesUtil.validarDouble(otrosIngresosBean.getIva())));
                otrosIngresosBean.setTotalFormat(numberFormat.format(CValidacionesUtil.validarDouble(otrosIngresosBean.getTotal())));

                //Disabled_InputText_Importe
                habilitaTextImporte = 0;

                //Enabled_Button_Aceptar
                habilitaBotonAceptar = 1;
                
                //Reset_Combo_Tipo_Cambio
                resetComboTipoCambio();
            }

            //Set_Format_Number_Precio_Unitario
            otrosIngresosBean.setPrecioUnitario(numberFormat.format(Math.round(Double.parseDouble(otrosIngresosBean.getPrecioUnitario()) * 100.00) / 100.00));

            //Set_Moneda
            otrosIngresosBean.setMoneda(String.valueOf(moneda).trim());

            //Write_Console
//        out.println("MONEDA: ".concat(otrosIngresosBean.getMoneda()));
            if ("HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio()).trim()) || "HONORARIOS POR ACEPTACION".equals(servicios.get(otrosIngresosBean.getServicio()).trim())) {
                //Call_Select_Tipo_Cambio
                selectTipoCambio();
            } else {
                //Set_Moneda
                otrosIngresosBean.setMoneda(String.valueOf(1).trim());
            }
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void checkTipoCambio() {
        //Write_Console
//        out.println("Check Tipo Cambio: ".concat(otrosIngresosBean.getTipoCambio()));
        try {
            //Check_Is_Numeric
            if (!isDoubleNumeric(otrosIngresosBean.getTipoCambio())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Tipo de Cambio. Debe ser numérico..."));
                return;
            }

            //Check_Tipo_Cambio
            if ("".trim().equals(otrosIngresosBean.getTipoCambio())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el campo Tipo Cambio..."));

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                return;
            }

            if (Float.parseFloat(otrosIngresosBean.getTipoCambio()) <= 0.0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El tipo de cambio debe ser mayor a cero..."));

                //Disabled_Button_Aceptar
                habilitaBotonAceptar = 0;

                return;
            }
            
            //Enabled_Button_Aceptar
            habilitaBotonAceptar = 1;

            //Visible_Tipo_Cambio
            visibleTipoCambio = 1;

            //Hide_Window_TipoCambio
            RequestContext.getCurrentInstance().execute("dlgTipoCambio.hide()");

        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void selectTipoCambio() {
        //Write_Console
//        out.println("Select Tipo Cambio...");
        try {
            if (Integer.parseInt(otrosIngresosBean.getMoneda()) != 1) {
                //Get_Parameters
                int anio = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                int mes = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                int dia = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();

                //Get_Tipo_Cambio
                String tipoCambio = cHonorarios.onHonorarios_GetTipoCambio(Integer.parseInt(otrosIngresosBean.getMoneda()), anio, mes, dia);

                //Write_Console
                
//            out.println("Tipo Cambio: ".concat(tipoCambio));
                //Validate_Tipo_Cambio
                if ("".equals(tipoCambio)) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de Cambio para el dia de hoy."));

                    //Hide_InputBox_TipoCambio
                    visibleTipoCambio = 0;
                    
                    seCambiaMoneda = true;

                    //Reset_Tipo_Cambio
                    resetComboTipoCambio();

                    //Disabled_Button_Aceptar
                    habilitaBotonAceptar = 0;

                    return;
                }

                //Set_Tipo_Cambio
                otrosIngresosBean.setTipoCambio(String.format("%.4f", Float.parseFloat(tipoCambio)));
                otrosIngresosBean.setNombreMoneda(monedas.get(otrosIngresosBean.getMoneda()));

                //Set_Fecha_valor
                otrosIngresosBean.setFechaValor(formatDate(fechaContableBean.getFecha()));

                //Enabled_Button_Aceptar
                habilitaBotonAceptar = 1;

                //Show_Window_TipoCambio
                RequestContext.getCurrentInstance().execute("dlgTipoCambio.show();");
            } else {
                //Hide_InputBox_TipoCambio
                visibleTipoCambio = 0;

                //Reset_Combo_Moneda
                resetComboTipoCambio();

                //Set_Tipo_Cambio
                otrosIngresosBean.setTipoCambio(String.format("%.2f", Float.parseFloat("1.0")));

                //Enabled_Button_Aceptar
                habilitaBotonAceptar = 1;
            }
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void otrosIngresos() throws SQLException {
        //Write_Console
//        out.println("Aplica Otros Ingresos...");
        try {
            //Validate_HONORARIOS POR ADMINISTRACION
            if ("HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio()))) {
                if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso())) == false) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                    return;
                }

                if (otrosIngresosBean.getCantidad() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }
                
                if ("".equals(otrosIngresosBean.getCantidad())){
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) != 1) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser igual a 1..."));
                    return;
                }

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getPrecioUnitario() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el precio unitario..."));
                    return;
                }

                if (Double.parseDouble(otrosIngresosBean.getPrecioUnitario().replace(",", "")) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo precio unitario debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getImporte() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el calculo del importe..."));
                    return;
                }

                if (Double.parseDouble(otrosIngresosBean.getImporte()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo importe debe ser mayor a 0..."));
                    return;
                }
                
                if(otrosIngresosBean.getFechaDel() == null || otrosIngresosBean.getFechaDel().equals("")){
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Fecha Del. Debe contener un valor..."));
                    return;
                }

                if(otrosIngresosBean.getFechaAl() == null || otrosIngresosBean.getFechaAl().equals("")){
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Fecha Al. Debe contener un valor..."));
                    return;
                }

                //Set_Moneda
                otrosIngresosBean.setMoneda(String.valueOf(moneda));

                //Chech_Periodo
                LocalDate fechaDel = otrosIngresosBean.getFechaDel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fechaAl = otrosIngresosBean.getFechaAl().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (fechaDel.isAfter(fechaAl)) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo fecha inicio es mayor al campo fecha final..."));
                    return;
                }
            }

            if ("HONORARIOS POR ACEPTACION".equals(servicios.get(otrosIngresosBean.getServicio()))) {
                if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso())) == false) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                    return;
                }

                if (otrosIngresosBean.getCantidad() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }
                
                if ("".equals(otrosIngresosBean.getCantidad())){
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) != 1) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser igual a 1..."));
                    return;
                }

                if (Integer.parseInt(otrosIngresosBean.getCantidad()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getPrecioUnitario() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el precio unitario..."));
                    return;
                }

                if (Double.parseDouble(otrosIngresosBean.getPrecioUnitario().replace(",", "")) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo precio unitario debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getImporte() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el calculo del importe..."));
                    return;
                }

                if (Double.parseDouble(otrosIngresosBean.getImporte()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo importe debe ser mayor a 0..."));
                    return;
                }

                //Check_Importe_Aceptacion
                productoBean = cHonorarios.onHonorarios_GetNumeroProducto(Integer.parseInt(otrosIngresosBean.getFideicomiso()));

                //Set_Moneda
                otrosIngresosBean.setMoneda(String.valueOf(moneda));

                //Write_Console
//            out.println("Importe Aceptacion: ".concat(String.valueOf(productoBean.getImporteAceptacion())));
                if (productoBean.getImporteAceptacionImp() > Double.parseDouble(otrosIngresosBean.getImporte())) {
                    RequestContext.getCurrentInstance().execute("dlgImporte.show();");
                    return;
                }
            }

            if (servicios.get(otrosIngresosBean.getServicio()).indexOf("HONORARIOS POR") != 0) {
                if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso())) == false) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                    return;
                }
                
                if (otrosIngresosBean.getCantidad() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }
                
                if ("".equals(otrosIngresosBean.getCantidad())){
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el campo de cantidad..."));
                    return;
                }
                
                if (Integer.parseInt(otrosIngresosBean.getCantidad()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getMoneda() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo de moneda es un dato obligatorio..."));
                    return;
                }

                if ("".equals(otrosIngresosBean.getMoneda())) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo de moneda es un dato obligatorio..."));
                    return;
                }
                
                if (Integer.parseInt(otrosIngresosBean.getCantidad()) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo cantidad debe ser mayor a 0..."));
                    return;
                }

                if (otrosIngresosBean.getPrecioUnitario() == null) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el precio unitario..."));
                    return;
                }

                if ("".equals(otrosIngresosBean.getPrecioUnitario())){
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta el precio unitario..."));
                    return;
                }
                
                if (Double.parseDouble(otrosIngresosBean.getPrecioUnitario().replace(",", "")) <= 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo precio unitario debe ser mayor a 0..."));
                    return;
                }

            }

            //Write_Console
//        out.println("Servicios: ".concat(servicios.get(otrosIngresosBean.getServicio())));
            if (servicios.get(otrosIngresosBean.getServicio()).indexOf("HONORARIOS POR") == -1 && (Integer.parseInt(otrosIngresosBean.getMoneda()) != 1)) {
                if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(otrosIngresosBean.getFideicomiso())) == false) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));

                    //Disabled_Button_Aceptar
                    habilitaBotonAceptar = 0;

                    return;
                }

                if ("".trim().equals(otrosIngresosBean.getTipoCambio())) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Falta capturar el campo Tipo Cambio..."));

                    //Disabled_Button_Aceptar
                    habilitaBotonAceptar = 0;

                    return;
                }

                if (Float.parseFloat(otrosIngresosBean.getTipoCambio()) <= 0.0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El tipo de cambio debe ser mayor a cero..."));

                    //Disabled_Button_Aceptar
                    habilitaBotonAceptar = 0;

                    return;
                }
            }

            //Get_Parametros_Calculo_Honorarios
            pacaHonBean = cHonorarios.onHonorarios_GetParametrosCalculoHonorarios(Integer.parseInt(otrosIngresosBean.getFideicomiso()));

            //Set_Fecha_valor
            otrosIngresosBean.setFechaValor(formatDate(fechaContableBean.getFecha()));

            //Set_Persona_Cobra
            otrosIngresosBean.setPersonaCobra(pacaHonBean.getPersonaCobra());

            //Set_Periodo_Cobro
            otrosIngresosBean.setPeriodoCobro(pacaHonBean.getPeriodoCobro());

            //Set_Tipo_Honorario
            otrosIngresosBean.setNombreServicio(servicios.get(otrosIngresosBean.getServicio()));

            //Set_Descripcion
            if (servicios.get(otrosIngresosBean.getServicio()).indexOf("HONORARIOS POR") != -1) {
                otrosIngresosBean.setDescripcion(otrosIngresosBean.getNombreServicio());
            } else {
                otrosIngresosBean.setNombreServicio("HONORARIOS POR OTROS SERVICIOS");
                otrosIngresosBean.setPersonaCobra("CARGO AL FONDO");
                otrosIngresosBean.setDescripcion("(".concat(otrosIngresosBean.getCantidad()).concat(") ").concat(servicios.get(otrosIngresosBean.getServicio())));
            }

            //Set_Moneda
            if (otrosIngresosBean.getMoneda() == null) {
                otrosIngresosBean.setMoneda("1");
            }

            //Set_Tipo_Cambio
            if (otrosIngresosBean.getMoneda() != null) {
                if (Integer.parseInt(otrosIngresosBean.getMoneda()) == 1) {
                    otrosIngresosBean.setTipoCambio("1.00");
                }
            } else {
                otrosIngresosBean.setTipoCambio("1.00");
            }

            //Set_Fecha_Periodo
            if (!"HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio()))) {
                otrosIngresosBean.setFechaDel(fechaContableBean.getFecha());
                otrosIngresosBean.setFechaAl(fechaContableBean.getFecha());
            }
            //Write_Console
//        System.out.println(otrosIngresosBean.getNombreServicio().concat(" POR UN IMPORTE DE ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(Double.parseDouble(otrosIngresosBean.getImporte())))));
            //Get_Output_Parameters
            OutParameterBean outParameterBean = cHonorarios.onHonorarios_executeGeneraHonorariosSp(otrosIngresosBean);

            //Write_Console
            
            
//        if(outParameterBean.getPsMsgErrOut() != null)
//           //logger.info("PS_MSGERR_OUT: ".concat(outParameterBean.getPsMsgErrOut()));
            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {

                if (outParameterBean.getiNumFolioContab() > 1) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro Generado con Folio Contable: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Servicio Registrado, el Folio Contable hasta el Pago"));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
            }

            if (otrosIngresosBean.isActualizaFecha()) {
                //Get_Result_Update  
                cHonorarios.onHonorarios_UpdateParametrosCalculoHonorarios(otrosIngresosBean);

                //Write_Console
//            out.println("resultado: ".concat(String.valueOf(resultado)));
            }
            cleanOtrosIngresos();
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 

    }

    public void otrosIngresosXAceptacion() throws SQLException {
        //Write_Console
//        out.println("Aplica Otros Ingresos por Aceptacion...");
        try {
            //Get_Parametros_Calculo_Honorarios
            pacaHonBean = cHonorarios.onHonorarios_GetParametrosCalculoHonorarios(Integer.parseInt(otrosIngresosBean.getFideicomiso()));

            //Set_Fecha_valor
            otrosIngresosBean.setFechaValor(formatDate(fechaContableBean.getFecha()));

            //Set_Persona_Cobra
            otrosIngresosBean.setPersonaCobra(pacaHonBean.getPersonaCobra());

            //Set_Periodo_Cobro
            otrosIngresosBean.setPeriodoCobro(pacaHonBean.getPeriodoCobro());

            //Set_Tipo_Honorario
            otrosIngresosBean.setNombreServicio(servicios.get(otrosIngresosBean.getServicio()));

            //Set_Descripcion
            if (servicios.get(otrosIngresosBean.getServicio()).indexOf("HONORARIOS POR") != -1) {
                otrosIngresosBean.setDescripcion(otrosIngresosBean.getNombreServicio());
            } else {
                otrosIngresosBean.setNombreServicio("HONORARIOS POR OTROS SERVICIOS");
                otrosIngresosBean.setPersonaCobra("CARGO AL FONDO");
                otrosIngresosBean.setDescripcion("(".concat(otrosIngresosBean.getCantidad()).concat(") ").concat(otrosIngresosBean.getNombreServicio()));
            }

            //Set_Moneda
            if (otrosIngresosBean.getMoneda() == null) {
                otrosIngresosBean.setMoneda("1");
            }

            //Set_Tipo_Cambio
            if (otrosIngresosBean.getMoneda() != null) {
                if (Integer.parseInt(otrosIngresosBean.getMoneda()) == 1) {
                    otrosIngresosBean.setTipoCambio("1.00");
                }
            } else {
                otrosIngresosBean.setTipoCambio("1.00");
            }

            //Set_Fecha_Periodo
            if (!"HONORARIOS POR ADMINISTRACION".equals(servicios.get(otrosIngresosBean.getServicio()))) {
                otrosIngresosBean.setFechaDel(fechaContableBean.getFecha());
                otrosIngresosBean.setFechaAl(fechaContableBean.getFecha());
            }
            //Write_Console
//        System.out.println(otrosIngresosBean.getNombreServicio().concat(") POR UN IMPORTE DE ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(Double.parseDouble(otrosIngresosBean.getImporte())))));
            //Get_Output_Parameters
            OutParameterBean outParameterBean = cHonorarios.onHonorarios_executeGeneraHonorariosSp(otrosIngresosBean);

            //Write_Console
//        out.println("bEjecuto: ".concat(String.valueOf(outParameterBean.getbEjecuto())));
//        out.println("iNumFolioContab: ".concat(String.valueOf(outParameterBean.getiNumFolioContab())));
//        out.println("PS_MSGERR_OUT: ".concat(outParameterBean.getPsMsgErrOut()));
            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {

                if (outParameterBean.getiNumFolioContab() > 1) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro Generado con Folio Contable: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Servicio Registrado, el Folio Contable hasta el Pago"));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
            }

            if (otrosIngresosBean.isActualizaFecha()) {
                //Get_Result_Update  
                cHonorarios.onHonorarios_UpdateParametrosCalculoHonorarios(otrosIngresosBean);

                //Write_Console
//            out.println("resultado: ".concat(String.valueOf(resultado)));
            }
            cleanOtrosIngresos();
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void getPagosHonorarios() throws SQLException {
        //Write_Console
//        out.println("Get Pagos Honorarios...");
//        out.println("Get Fideicomiso: ".concat(detCartBean.getFideicomiso()));
        try {
            //Clean_Total_Adeudo
            totalAdeudo = 0;
            totalAdeudoDolar = 0;

            //Check_Fideicomiso
            if ("".equals(detCartBean.getFideicomiso())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCartera(0000000000, fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Dollar
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                return;
            }

            if (!isNumeric(detCartBean.getFideicomiso())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCartera(0000000000, fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Dollar
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                return;
            }

            if (Integer.parseInt(detCartBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es invalido..."));

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCartera(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Dollar
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                return;
            }

            if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(detCartBean.getFideicomiso())) == false) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCartera(000000000, fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Dollar
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                return;
            }
            
            //Get_Pagos
            pagos = cHonorarios.onHonorarios_GetCartera(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

            //Check_Pagos
            if (pagos.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso sin registros de cartera para honorarios diferentes al servicio: HONORARIOS POR ADMINISTRACION."));
                return;
            }

            //Sum_Totals
            for (DetCartBean detCartBean : pagos) {
                if (detCartBean.getNoMoneda() == 1) {
                    totalAdeudo = totalAdeudo + detCartBean.getAdeudoImp();
                } else {
                    totalAdeudoDolar = totalAdeudoDolar + detCartBean.getAdeudoImp();
                }
            }
            
            //Format_Currency
            totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);
            totalAdeudoDolarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudoDolar);

            //Write_Console
//        out.println("Total Adeudo M.N.: ".concat(String.valueOf(totalAdeudoFormat)));
//        out.println("Total Adeudo M.E.: ".concat(String.valueOf(totalAdeudoDolarFormat)));
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void getPagosHonorariosAdmon() throws SQLException {
        //Write_Console
//        out.println("Get Pagos de Honorarios con Prelacion o Deuda Especifica...");
//        out.println("Get Fideicomiso: ".concat(detCartBean.getFideicomiso()));
        try {
            //Clean_Total_Adeudo
            totalAdeudo = 0;

            //Check_Fideicomiso
            if ("".trim().equals(detCartBean.getFideicomiso().trim())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCarteraPagos(1234567890, fechaContableBean.getFechaContable());
                
                payments = new ArrayList<>();

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = "";

                //Disabled_Button
                habilitaBotonGenerarPago = 0;

                return;
            }

            if (!isNumeric(detCartBean.getFideicomiso())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));

                //Init_Method
                init();

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCarteraPagos(0000000000, fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = "";

                //Disabled_Button
                habilitaBotonGenerarPago = 0;

                return;
            }

            if (Integer.parseInt(detCartBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es invalido..."));

                //Init_Method
                init();

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCarteraPagos(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = "";

                //Disabled_Button
                habilitaBotonGenerarPago = 0;

                return;
            }

            if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(detCartBean.getFideicomiso())) == false) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));

                //Init_Method
                init();

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCarteraPagos(000000000, fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = "";

                //Disabled_Button
                habilitaBotonGenerarPago = 0;

                return;
            }
            
            //Get_Pagos
            pagos = cHonorarios.onHonorarios_GetCarteraPagos(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

            //Check_Pagos
            if (pagos.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso sin registros de cartera para el servicio: HONORARIOS POR ADMINISTRACION."));

                //Init_Method
                init();

                //Get_Pagos
                pagos = cHonorarios.onHonorarios_GetCarteraPagos(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

                //Clean_Importe_Total
                totalAdeudo = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

                //Clean_Importe_Total
                totalAdeudoDolar = 0.0;

                //Clean_Importe_Total_Format
                totalAdeudoDolarFormat = "";

                //Disabled_Button
                habilitaBotonGenerarPago = 0;

                return;
            }

            //Sum_Totals
            for (DetCartBean detCartBean : pagos) {
                totalAdeudo = totalAdeudo + detCartBean.getAdeudoImp();
            }

            //Set_Total_Adeudo_Format
            totalAdeudoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAdeudo);

            //Set_Total_Adeudo_Format_Persona
            totalAdeudoDolarFormat = "";
            
            totalAdeudo = round(totalAdeudo, 2);
            totalAdeudoDolar = round(totalAdeudoDolar,2);
            
            //Get_Payments
            payments = cHonorarios.onHonorarios_GetPersonsPay(Integer.parseInt(detCartBean.getFideicomiso()));


            //Write_Console
//        out.println("Total Adeudo:".concat(String.valueOf(totalAdeudoFormat)));
            //Enabled_Buttons
            habilitaBotonGenerarPago = 1;
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void applyPay(DetCartBean detCartBean) throws SQLException {
        //Write_Console
//        out.println("Apply Pago Honorarios...");
//        out.println("Fideicomiso: ".concat(detCartBean.getFideicomiso()));
        try {
            //Get_Parameters
            parameters = cHonorarios.onHonorarios_GetSubFisoAndCtoInver(detCartBean.getFideicomiso());

            //Check_Parameters
            if (parameters.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene asociado un contrato de inversion.")));
                return;
            } else {
                //Set_Parameters
                detCartBean.setSubFiso(parameters.get(0));
                detCartBean.setContratoInversion(parameters.get(1));
            }

            //Check_Liquidez
            if (cHonorarios.onHonorarios_CheckLiquidez(detCartBean.getFideicomiso(), detCartBean.getSubFiso(), detCartBean.getNoMoneda(), detCartBean.getContratoInversion(), detCartBean.getImporteImp())) {

                if (detCartBean.getNoMoneda() == 1) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en MONEDA NACIONAL.")));
                    return;
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en DOLARES.")));
                    return;
                }
            }

            //Write_Console
//        out.println("Calificacion: ".concat(detCartBean.getCalificacion()));
            //Check_Participante
            if (detCartBean.isEspecificaFactura()) {
                //Write_Console
//            out.println("Get Participantes....");

                if (!"LO PAGADO".equals(detCartBean.getCalificacion().trim())) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Solo se puede especificar a quien facturar, cuando las deudas son: LO PAGADO"));
                    return;
                }

                //Get_Participantes
                participantes = cHonorarios.onHonorarios_GetPorcentajeParticipantesPago("CARGO AL FONDO", detCartBean.getFideicomiso());

                //Set_Bean
                this.detCartBean = detCartBean;

                //Show_Window_Participante
                RequestContext.getCurrentInstance().execute("dlgParticipantePago.show();");

                //Return
                return;
            }

            //ApplyPay
            outParameterBean = cHonorarios.onHonorarios_executeApplyPaySp(detCartBean, fechaContableBean);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento aplicado correctamente con Folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                //Pagos_Honorarios
                getPagosHonorarios();
            } else {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
                //Pagos_Honorarios
                getPagosHonorarios();
            }
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        } 
    }

    public void applyPayHonAdmonEspecifica(DetCartBean detCartBean) throws SQLException {
        //Write_Console
//        out.println("Aplica Pago Honorarios con Deuda Especifica...");
        try {
            //Set_Deuda_Especifica
            deudaEspecifica = 1;

            //Get_Parameters
            parameters = cHonorarios.onHonorarios_GetSubFisoAndCtoInver(detCartBean.getFideicomiso());

            //Check_Parameters
            if (parameters.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La chequera registrada en parametros de honorarios no es consistente con las chequeras registradas en el fideicomiso."));
                return;
            } else {
                if (Integer.parseInt(parameters.get(1)) > 0) {
                    //Set_Parameters
                    detCartBean.setSubFiso(parameters.get(0));
                    detCartBean.setContratoInversion(parameters.get(1));
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene asociado un contrato de inversion.")));
                    return;
                }
            }

            //Check_Liquidez
            if (cHonorarios.onHonorarios_CheckLiquidez(detCartBean.getFideicomiso(), detCartBean.getSubFiso(), detCartBean.getNoMoneda(), detCartBean.getContratoInversion(), detCartBean.getImporteImp())) {

                if (detCartBean.getNoMoneda() == 1) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en MONEDA NACIONAL.")));
                    return;
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en DOLARES.")));
                    return;
                }
            }

            //Write_Console
//        out.println("Calificacion: ".concat(detCartBean.getCalificacion()));
            //Check_Participante
            if (detCartBean.isEspecificaFactura()) {
                //Write_Console
//            out.println("Get Participantes....");

                if (!"LO PAGADO".equals(detCartBean.getCalificacion().trim())) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Solo se puede especificar a quien facturar, cuando las deudas son: LO PAGADO"));
                    return;
                }

                //Get_PacaHon
                pacaHonBean = cHonorarios.onHonorarios_GetPacaHon(Integer.parseInt(detCartBean.getFideicomiso()));

                //Write_Console
                //String msjNomContrato = "Nombre Contrato: ".concat(pacaHonBean.getContratoNombre());
                //Get_Participantes
                participantes = cHonorarios.onHonorarios_GetPorcentajeParticipantesPago(pacaHonBean.getPersonaCobra(), detCartBean.getFideicomiso());

                //Write_Console
                //msjNomContrato += "No. Participantes: ".concat(String.valueOf(participantes.size()));
                //Set_Bean
                this.detCartBean = detCartBean;

                //Show_Window_Participante
                RequestContext.getCurrentInstance().execute("dlgParticipantePago.show();");

                //Return
                return;
            }

            //ApplyPay
            outParameterBean = cHonorarios.onHonorarios_executeApplyPayHonorarioSp(detCartBean, fechaContableBean, deudaEspecifica);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento aplicado correctamente."));
                //Pagos_Honorarios_Administracion
                getPagosHonorariosAdmon();
            } else {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
                //Pagos_Honorarios_Administracion
                getPagosHonorariosAdmon();
            }
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_applyPayHonAdmonPrelacion()";
        } 
    }

    public void applyPayHonAdmonPrelacion() throws SQLException {
        //Write_Console
//        out.println("Aplica Pago Honorarios con Prelacion...");
        try {
            //Set_Deuda_Especifica
            deudaEspecifica = 0;

            //Check_Person_Pay
            if (detCartBean.getPersonaPaga() == null) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe la persona que pague el adeudo."));
                return;
            }

            //Set_Parameters
            String[] personPay = detCartBean.getPersonaPaga().split("/");

            //Check_Importe_Pago 
            if (!isDoubleNumeric(String.valueOf(importeTotal))) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Importe de Pago debe ser un campo numérico."));
                return;
            }

            //Check_Importe_Pago
            if (Double.parseDouble(importeTotal) <= 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No puede efectuar pagos con importe cero o menor."));
                return;
            }

            //Check_Adeudo_Total
            if (Double.parseDouble(importeTotal) > totalAdeudo) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se puede pagar mas de lo que se adeuda.."));
                return;
            }

            //Get_Pago
            DetCartBean detCartBean = pagos.get(0);

            //Get_Parameters
            parameters = cHonorarios.onHonorarios_GetSubFisoAndCtoInver(detCartBean.getFideicomiso());

            //Check_Parameters
            if (parameters.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La chequera registrada en parametros de honorarios no es consistente con las chequeras registradas en el fideicomiso."));
                return;
            } else {
                if (Integer.parseInt(parameters.get(1)) > 0) {
                    //Set_Parameters
                    detCartBean.setSubFiso(parameters.get(0));
                    detCartBean.setContratoInversion(parameters.get(1));
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene asociado un contrato de inversion.")));
                    return;
                }
            }

            //Set_Parameters
            detCartBean.setSubFiso(parameters.get(0));
            detCartBean.setContratoInversion(parameters.get(1));
            detCartBean.setMetodoPago(Integer.parseInt(metodoPago));
            detCartBean.setImporteTotalImp(Double.parseDouble(importeTotal));
            detCartBean.setPersona(personPay[1]);
            detCartBean.setNoPersona(Integer.parseInt(personPay[2].trim()));

            //Set_Moneda
            if ("MONEDA NACIONAL".equals(personPay[0].trim())) {
                detCartBean.setNoMoneda(1);
            } else {
                detCartBean.setNoMoneda(2);
            }

            //Check_Contrato_Inversion
            if (Integer.parseInt(parameters.get(1)) <= 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Indicar la Cuenta Bancaria en los parametros de honorarios. El valor actual es: ".concat(parameters.get(1))));
                return;
            }

            //Write_Console
//        out.println("isEspecificaFactura: ".concat(String.valueOf(isEspecificaFactura())));
            //Write_Console
//        out.println("Metodo Pago: ".concat(metodoPago));
            //Write_Console
//        out.println("Total Importe a pagar: ".concat(String.valueOf(detCartBean.getImporteTotalImp())));
            //Check_Participante
            if (isEspecificaFactura()) {
                //Write_Console
//            out.println("Get Participantes....");

                if (!"LO PAGADO".equals(detCartBean.getCalificacion().trim())) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Solo se puede especificar a quien facturar, cuando las deudas son: LO PAGADO"));
                    return;
                }

                //Get_PacaHon
                pacaHonBean = cHonorarios.onHonorarios_GetPacaHon(Integer.parseInt(detCartBean.getFideicomiso()));

                //Write_Console
//            out.println("Nombre Contrato: ".concat(pacaHonBean.getContratoNombre()));
                //Get_Participantes
                participantes = cHonorarios.onHonorarios_GetPorcentajeParticipantesPago(pacaHonBean.getPersonaCobra(), detCartBean.getFideicomiso());

                //Write_Console
//            out.println("No. Participantes: ".concat(String.valueOf(participantes.size())));
                //Show_Window_Participante
                RequestContext.getCurrentInstance().execute("dlgParticipantePago.show();");

                //Return
                return;

            }

            //Check_Liquidez
            if (cHonorarios.onHonorarios_CheckLiquidez(detCartBean.getFideicomiso(), detCartBean.getSubFiso(), detCartBean.getNoMoneda(), detCartBean.getContratoInversion(), detCartBean.getImporteTotalImp())) {

                if (detCartBean.getNoMoneda() == 1) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en MONEDA NACIONAL.")));
                    return;
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(detCartBean.getFideicomiso()).concat(" no tiene la liquidez Bancaria requerida en DOLARES.")));
                    return;
                }
            }

            //ApplyPay
            outParameterBean = cHonorarios.onHonorarios_executeApplyPayHonorarioSp(detCartBean, fechaContableBean, deudaEspecifica);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento aplicado correctamente."));
                //Pagos_Honorarios_Administracion
                getPagosHonorariosAdmon();
                //Hide_Window_Aplicar_Pago
                RequestContext.getCurrentInstance().execute("dlgGenerarPago.hide();");
            } else {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Movimiento sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
                //Pagos_Honorarios_Administracion
                getPagosHonorariosAdmon();
                //Hide_Window_Aplicar_Pago
                RequestContext.getCurrentInstance().execute("dlgGenerarPago.hide();");
            }

        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_applyPayHonAdmonPrelacion()";
        } 
    }

    public void checkPercent() {
        //Write_Console
//        out.println("Get Data Participantes....");

        //Variables
        int total = 0;

        //Write_Console
        for (ParticipanteBean participanteBean : participantes) {
            total += participanteBean.getPorcentaje();
        }

        if (total != 100) {
            //Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El total de los porcentajes debe ser 100; actualmente suma : ".concat(String.valueOf(total))));
            return;
        }

        //Show_Window_Aplicar_Porcentaje
        RequestContext.getCurrentInstance().execute("dlgAplicarPorcentajes.show();");
    }

    public void insertParahope() throws SQLException {
        //Write_Console
//        out.println("Insert Parahope....");
        try {
            //Variables
            int insert = 0;

            //Set_Date
            LocalDate localDate = LocalDate.now();
            String date = localDate.format(dateTimeformatter);

            //Delete_Percents
            cHonorarios.onHonorarios_DeletePorcentajeParticipacion(Integer.parseInt(detCartBean.getFideicomiso()), date);

            //Write_Console
//        System.out.println("Registros Borrados: ".concat(String.valueOf(delete)));
            //Insert_Percent_Particiapantes
            for (ParticipanteBean participanteBean : participantes) {
                insert = cHonorarios.onHonorarios_InsertPorcentajeParticipacion(participanteBean);

                //Validate_Insert
                if (insert >= 0) {
                    //Write_Console 
//                out.println("Registro insertado: ".concat(participanteBean.getParticipante()));
                } else {
                    //Write_Console 
//                out.println("Registro no insertado: ".concat(participanteBean.getParticipante()));
                    //Return
                    return;
                }
            }

            //Apply_Pay
            applyPayParticipacionPorcentaje();
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_insertParahope()";
        } 
    }

    public void applyPayParticipacionPorcentaje() {
        //Write_Console
        try {
            //Get_Parameters
            parameters = cHonorarios.onHonorarios_GetSubFisoAndCtoInver(detCartBean.getFideicomiso());

            //Set_Parameters
            detCartBean.setSubFiso(parameters.get(0));
            detCartBean.setContratoInversion(parameters.get(1));

            //ApplyPay
            outParameterBean = cHonorarios.onHonorarios_executeApplyPaySp(detCartBean, fechaContableBean);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro Generado con Folio Contable: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                //Pagos_Honorarios
                getPagosHonorarios();
                //Hide_Window_Aplicar_Porcentaje
                RequestContext.getCurrentInstance().execute("dlgParticipantePago.hide();");
            } else {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Registro sin Generar con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut()))));
                //Pagos_Honorarios
                getPagosHonorarios();
                //Hide_Window_Aplicar_Porcentaje
                RequestContext.getCurrentInstance().execute("dlgParticipantePago.hide();");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_applyPayParticipacionPorcentaje()";
        } 
    }

    public void getCondonaciones() throws SQLException {
        //Write_Console
//        out.println("Get Condonaciones...");
        try {
            //Check_Fideicomiso 
            if ("".equals(detCartBean.getFideicomiso())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));

                //Init_Method
                init();

                //Get_Condonaciones
                pagos = cHonorarios.onHonorarios_GetCondonaciones(1234567890, fechaContableBean.getFechaContable());

                return;
            }

            //Check_Is_Numeric
            if (!isNumeric(detCartBean.getFideicomiso())) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));

                //Init_Method
                init();

                //Get_Condonaciones
                pagos = cHonorarios.onHonorarios_GetCondonaciones(1234567890, fechaContableBean.getFechaContable());

                return;
            }

            if (Integer.parseInt(detCartBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es invalido..."));

                //Init_Method
                init();

                //Get_Condonaciones
                pagos = cHonorarios.onHonorarios_GetCondonaciones(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

                return;
            }

            if (cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(detCartBean.getFideicomiso())) == false) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));

                //Init_Method
                init();

                //Get_Condonaciones
                pagos = cHonorarios.onHonorarios_GetCondonaciones(000000000, fechaContableBean.getFechaContable());

                return;
            }
            

            //Get_Condonaciones
            pagos = cHonorarios.onHonorarios_GetCondonaciones(Integer.parseInt(detCartBean.getFideicomiso()), fechaContableBean.getFechaContable());

            if (pagos.size() == 0) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Este Fideicomiso no tiene cartera vencida."));
                return;
            }
        } catch (NumberFormatException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_getCondonaciones()";
        } 
    }

    public void onRowSelect(SelectEvent event) throws NumberFormatException, SQLException {
        //Write_Console
//        out.println("Get Data Row...");

        try {
            //Decimal_Format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Clean
            cleanCondonacion();

            //Number_Format
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

            //Get_PacaHon
            pacaHonBean = cHonorarios.onHonorarios_GetPacaHon(Integer.parseInt(detCartBean.getFideicomiso()));

            //Write_Console
//        out.println("Nombre Contrato: ".concat(pacaHonBean.getContratoNombre()));
            //Get_Persona
            this.detCartBean = (DetCartBean) event.getObject();

            //Write_Console
//        out.println("Importe a Condonar: ".concat(detCartBean.getImporte()));
            //Set_Iva_Moratorio
            detCartBean.setIvaMoratoriosImp(detCartBean.getMoratoriosImp() * iva);
            detCartBean.setIvaMoratorios(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(detCartBean.getMoratoriosImp() * iva)));

            //Set_Total
            detCartBean.setTotal(numberFormat.format(detCartBean.getImporteImp() + detCartBean.getIvaImp() + detCartBean.getMoratoriosImp() + detCartBean.getIvaMoratoriosImp()));
            detCartBean.setTotalImp(detCartBean.getImporteImp() + detCartBean.getIvaImp() + detCartBean.getMoratoriosImp() + detCartBean.getIvaMoratoriosImp());
            totalCartera = detCartBean.getImporteImp() + detCartBean.getIvaImp() + detCartBean.getMoratoriosImp() + detCartBean.getIvaMoratoriosImp();

            //Set_Format_Amounts
            honorarioFormat = decimalFormat.format(0);
            moratorioFormat = decimalFormat.format(0);
            ivaHonorarioFormat = decimalFormat.format(0);
            ivaMoratorioFormat = decimalFormat.format(0);
            totalCondonacionFormat = decimalFormat.format(0);

            //Show_Window_Condonacion
            RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.show();");

        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_onRowSelect()";
        } 
    }

    public void cleanCondonacion() {
        //Write_Console
//        out.println("Clean Condonacion...");
        try {
            //Decimal_Format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Clean_Mounts
            honorario = 0;
            moratorio = 0;
            ivaHonorario = 0;
            ivaMoratorio = 0;
            totalCondonacion = 0;

            //Clean_Format_Mounts
            honorarioFormat = decimalFormat.format(honorario);
            moratorioFormat = decimalFormat.format(moratorio);
            ivaHonorarioFormat = decimalFormat.format(ivaHonorario);
            ivaMoratorioFormat = decimalFormat.format(ivaMoratorio);
            totalCondonacionFormat = decimalFormat.format(totalCondonacion);
        } catch (NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();    
        } 
    }

    public void ivaHonorario() {
        //Write_Console
//        out.println("Get Iva HonorivaHonorarioFormatario...");
        try {
            //Decimal_Format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            
            //Set_Honorario
            honorario = Double.parseDouble(honorarioFormat);
            
            //Set_Decimal_Format
            honorario = Double.parseDouble(decimalFormat.format(honorario));

            if (honorario == detCartBean.getImporteImp()) {
                //Get_Iva_Honorario	
                ivaHonorario = detCartBean.getIvaImp();
            } else {
                //Get_Iva_Honorario
                ivaHonorario = round(CValidacionesUtil.validarDouble(decimalFormat.format((honorario * iva))), 2);
            }

            //Set_Format
            ivaHonorarioFormat = decimalFormat.format(ivaHonorario);

            //Write_Console
//        out.println("Iva Honorario...".concat(String.valueOf(ivaHonorario)));
            //Write_Console
//        out.println("Iva...".concat(String.valueOf(iva)));
            //Set_Total_Condonacion
            totalCondonacion = CValidacionesUtil.validarDouble(decimalFormat.format(round(honorario + ivaHonorario + moratorio + ivaMoratorio, 3)));

            //Set_Total_Condonacion_Format
            totalCondonacionFormat = decimalFormat.format(totalCondonacion);
            
            honorarioFormat = decimalFormat.format(honorario);
            
        } catch (NumberFormatException Err) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Clean_Mounts
            honorario = 0;
            moratorio = 0;
            ivaHonorario = 0;
            ivaMoratorio = 0;
            totalCondonacion = 0;

            //Clean_Format_Mounts
            honorarioFormat = decimalFormat.format(honorario);
            moratorioFormat = decimalFormat.format(moratorio);
            ivaHonorarioFormat = decimalFormat.format(ivaHonorario);
            ivaMoratorioFormat = decimalFormat.format(ivaMoratorio);
            totalCondonacionFormat = decimalFormat.format(totalCondonacion);
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe de los Honorarios es incorrecto."));      
        } 
    }

    public void applyCondonacion(String tipoOperacionContable) throws SQLException {
        //Write_Console
//        out.println("Tipo Operacion Contable: ".concat(tipoOperacionContable));
        try {
            //Check_Mount_Honorario > 0
            if (honorario <= 0.00) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe de los Honorarios debe ser mayor a: 0.0"));
                return;
            }

            //Check_Mount_Honorario
            if (honorario > detCartBean.getImporteImp()) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe no puede ser mayor a: ".concat(detCartBean.getImporte())));
                return;
            }

            //Check_Mount_Moratorio
            if (moratorio > detCartBean.getMoratoriosImp()) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe moratorio no puede ser mayor a: ".concat(detCartBean.getMoratorios())));
                return;
            }

            //Check_Mount_Total
            if (totalCartera > detCartBean.getTotalImp()) {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe moratorio no puede ser mayor a: $".concat(String.valueOf(detCartBean.getTotal()))));
                return;
            }

            //Set_Values
            detCartBean.setImporteImp(honorario);
            detCartBean.setIvaImp(ivaHonorario);
            detCartBean.setMoratoriosImp(moratorio);
            detCartBean.setIvaMoratoriosImp(ivaMoratorio);
            detCartBean.setTipoOperacionContable(tipoOperacionContable);

            //Check_Provisiones
            int count = cHonorarios.onHonorarios_CheckProvision(detCartBean);

            if (count > 0) {
                //Show_Window_Provision
                RequestContext.getCurrentInstance().execute("dlgProvision.show();");
                //Return
                return;
            }

            //Apply_Condonacion
            outParameterBean = cHonorarios.onHonorarios_executeApplyPayCondonacionSp(detCartBean, fechaContableBean);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                if ("3".equals(tipoOperacionContable)) {
                    if (outParameterBean.getiNumFolioContab() == 1) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Condonación Aplicada satisfactoriamente."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Condonación Aplicada satisfactoriamente con folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                    }
                } else {
                    if (outParameterBean.getiNumFolioContab() == 1) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Cancelación Aplicada satisfactoriamente."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Cancelación Aplicada satisfactoriamente con folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                    }
                }

                //Get_Condonaciones
                getCondonaciones();

                //Hide_Window_Aplicar_Condonacion
                RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.hide();");
            } else {
                if ("3".equals(tipoOperacionContable)) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No pudo aplicar la Condonación con el siguiente error: ".concat(outParameterBean.getPsMsgErrOut())));
                } else {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No pudo aplicar la Cancelación con el siguiente error: ".concat(outParameterBean.getPsMsgErrOut())));
                }

                //Get_Condonaciones
                getCondonaciones();

                //Hide_Window_Aplicar_Condonacion
                RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.hide();");
            }
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_applyCondonacion()";
        } 
    }

    public void applyCondonacionProvision() throws SQLException {
        //Write_Console
//        out.println("Apply Condonacion Provision...");
        try {
            //Apply_Condonacion
            outParameterBean = cHonorarios.onHonorarios_executeApplyPayCondonacionSp(detCartBean, fechaContableBean);

            //Set_Message
            if ("1".equals(String.valueOf(outParameterBean.getbEjecuto()))) {
                //Set_Message
                if (outParameterBean.getiNumFolioContab() == 1) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Condonación Aplicada satisfactoriamente."));
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Condonación Aplicada satisfactoriamente con folio: ".concat(String.valueOf(outParameterBean.getiNumFolioContab()))));
                }
                //Get_Condonaciones
                getCondonaciones();
                //Hide_Window_Aplicar_Condonacion
                RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.hide();");
            } else {
                //Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No pudo aplicar la Condonación con el siguiente error: ".concat(outParameterBean.getPsMsgErrOut())));
                //Get_Condonaciones
                getCondonaciones();
                //Hide_Window_Aplicar_Condonacion
                RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.hide();");
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + " MBHonorarios_applyCondonacionProvision()";
        } 
    }

    public void onRowSelectTotalPersona(SelectEvent event) {
            //Get_Persona
            this.detCartBean = (DetCartBean) event.getObject();

            //Write_Console
//        out.println("Adeudo a Condonar: ".concat(detCartBean.getAdeudo()));
            //Clean_Importe_Total_Format
            totalAdeudoDolarFormat = detCartBean.getAdeudo();
    }

    public void cleanSelection() {
            //Unselect_DataTable_Condonacion
            RequestContext.getCurrentInstance().execute("dtCondonaciones.unselectAllRows();");

            //Hide_Window_Aplicar_Condonacion
            RequestContext.getCurrentInstance().execute("dlgCondonacionesPago.hide();");
        
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException Err) {
            //logger.error("El valor ingresado es vacío o es contiene carateres no númericos en MBHonorarios_isNumeric()");
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

    public void formatNumber() {
        //Write_Console
//        out.println("Format Number Importe...");
        try {
            //Decimal_Format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Set_Decimal_Format
            importeTotal = decimalFormat.format(Double.parseDouble(importeTotal));
        } catch (NumberFormatException Err) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Set_Decimal_Format
            importeTotal = decimalFormat.format(Double.parseDouble("0"));
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El importe de Pago es incorrecto."));
        }
    }

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
        return bd.doubleValue();
    }
    
    private synchronized String formatDate(Date date) {
        return simpleDateFormat.format(date);
    }
    
    private synchronized String formatDate2(Date date) {
        return simpleDateFormat2.format(date);
    }
   
}
