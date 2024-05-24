/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBPagares.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import scotiaFid.bean.ConreporBean;
import scotiaFid.bean.ContratoBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.dao.CFecha;
import scotiaFid.dao.CTesoreria;
import scotiaFid.util.LogsContext;

//Class
@ManagedBean(name = "mbPagares")
@ViewScoped
public class MBPagares implements Serializable {

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
    private ConreporBean pagaresBean = new ConreporBean();
    // ----------------------------------------------------------------------------- 
    private ContratoBean contratoBean = new ContratoBean();
    //-----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    // -----------------------------------------------------------------------------
    private FechaContableBean fechaContableBean = new FechaContableBean();
    //-----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // ----------------------------------------------------------------------------- 
    private static final Logger logger = LogManager.getLogger(MBPagares.class);
    // -----------------------------------------------------------------------------
    private Map<String, String> custodio = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> monedas = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> ctoInversion = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F O R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatTit = new DecimalFormat("###############");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatImp = new DecimalFormat("############0.00");
    // -----------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    private static final DecimalFormat decimalFormatPrec = new DecimalFormat("###########0.00####");
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * A T R I B U T O S   P R I V A D O S * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private String mensajeErrorConsulta;
    // -----------------------------------------------------------------------------
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private String nombreObjeto;
    // -----------------------------------------------------------------------------
    private StringWriter stringWriter = new StringWriter();
    // -----------------------------------------------------------------------------
    //_Habilitar_Botones,_Paneles_y_Listas_Desplegables	
    // -----------------------------------------------------------------------------
    private int habilitaSelCustodio;
    // -----------------------------------------------------------------------------
    private int habilitaSelCtoInv;
    // -----------------------------------------------------------------------------
    private int habilitaBotonCancelar;
    // -----------------------------------------------------------------------------
    private int habilitaTxtContenedor;
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtSubFiso;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelInv;
    // -----------------------------------------------------------------------------
    private boolean habilitaTxtMoneda;
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
    private int iSecEmis;
    // -----------------------------------------------------------------------------
    private String sCtoInver;
    // -----------------------------------------------------------------------------
    private String FecMovto;
    // -----------------------------------------------------------------------------
    private double dImpMovto;
    // -----------------------------------------------------------------------------
    private int iSubClave;
    // -----------------------------------------------------------------------------
    private String subFiso;
    // -----------------------------------------------------------------------------
    private List<String> listaSubContrato = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private boolean isHabilitadoTC;
    private String colorHabilitadoTC;
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    CFecha cFecha = new CFecha();
    // -----------------------------------------------------------------------------
    CTesoreria cTesoreria = new CTesoreria();
    //Funciones privadas estaticas
    //--------------------------------------------------------------------------
    private static HttpServletRequest peticionURL;
    //--------------------------------------------------------------------------

    /*
        
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S * * * * * * * * * * * * * * * * * * * * *   
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public String getMensajeErrorConsulta() {
        return mensajeErrorConsulta;
    }

    public void setMensajeErrorConsulta(String mensajeErrorConsulta) {
        this.mensajeErrorConsulta = mensajeErrorConsulta;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public ConreporBean getPagaresBean() {
        return pagaresBean;
    }

    public void setPagaresBean(ConreporBean pagaresBean) {
        this.pagaresBean = pagaresBean;
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

    public Map<String, String> getCustodio() {
        return custodio;
    }

    public void setCustodio(Map<String, String> custodio) {
        this.custodio = custodio;
    }

    public Map<String, String> getMonedas() {
        return monedas;
    }

    public void setMonedas(Map<String, String> monedas) {
        this.monedas = monedas;
    }

    public String getSubFiso() {
        return subFiso;
    }

    public void setSubFiso(String subFiso) {
        this.subFiso = subFiso;
    }

    public List<String> getListaSubContrato() {
        return listaSubContrato;
    }

    public void setListaSubContrato(List<String> listaSubContrato) {
        this.listaSubContrato = listaSubContrato;
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

    public int getiSecEmis() {
        return iSecEmis;
    }

    public void setiSecEmis(int iSecEmis) {
        this.iSecEmis = iSecEmis;
    }

    public String getsCtoInver() {
        return sCtoInver;
    }

    public void setsCtoInver(String sCtoInver) {
        this.sCtoInver = sCtoInver;
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

    public int getiSubClave() {
        return iSubClave;
    }

    public void setiSubClave(int iSubClave) {
        this.iSubClave = iSubClave;
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

    public boolean isHabilitaTxtMoneda() {
        return habilitaTxtMoneda;
    }

    public void setHabilitaTxtMoneda(boolean habilitaTxtMoneda) {
        this.habilitaTxtMoneda = habilitaTxtMoneda;
    }

    public boolean isIsHabilitadoTC() {
        return isHabilitadoTC;
    }

    public void setIsHabilitadoTC(boolean isHabilitadoTC) {
        this.isHabilitadoTC = isHabilitadoTC;
    }

    public String getColorHabilitadoTC() {
        return colorHabilitadoTC;
    }

    public void setColorHabilitadoTC(String colorHabilitadoTC) {
        this.colorHabilitadoTC = colorHabilitadoTC;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public MBPagares() {
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
                // Deshabilitar_paneles
                HabilitaPanelInv = true;
                habilitaTxtSubFiso = true;
                habilitaTxtMoneda = true;
                isHabilitadoTC = true;
                colorHabilitadoTC = "inputNumber2";

                // Mensajes_de_Error
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.MBPagares";

                // Tipo_Movimiento
                tipoMovimiento = "CP";

                // Set_Bean
                if (this.pagaresBean == null) {
                    this.pagaresBean = new ConreporBean();
                }

                // Get_Fecha_Contable
                fechaContableBean = cTesoreria.onTesoreria_GetFechaContable();

                // Format_Fecha_Contable
                fechaContableBean.setFechaContable(formatDate(fechaContableBean.getFecha()));

                // _Agregar_fecha_contable_como_fecha_valor
                FecValorMSA = 0;
                FecValor = fechaContableBean.getFecha();
                pagaresBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

                // Carga_moneda
                monedas = cTesoreria.onTesoreria_CargaMonedas();

            }
        } catch (DateTimeException | IOException | ParseException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Construct()";
        }
    }

    public void select_Moneda() {
        try {
            if (pagaresBean.getMoneda() == 0) {

                pagaresBean.setTipoCambio(0);
                return;
            } else {
                if (pagaresBean.getMoneda() != 1) {

                    pagaresBean.setTipoCambio(Double.parseDouble(formatFormatPrec(cTesoreria.onTesoreria_Get_TipoCambio(pagaresBean.getMoneda()))));

                    if (pagaresBean.getTipoCambio() == 0) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de cambio para este día, por favor proporcione uno..."));
                        isHabilitadoTC = false;
                        colorHabilitadoTC = "inputForm";
                    } else {
                        isHabilitadoTC = true;
                        colorHabilitadoTC = "inputNumber2";
                    }
                } else {
                    pagaresBean.setTipoCambio(1);
                    isHabilitadoTC = true;
                    colorHabilitadoTC = "inputNumber2";
                }
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Moneda()";
            //logger.error(mensajeError);
            //logger.error("Causa: ".concat(exception.getCause().getMessage()));
            //exception.printStackTrace(new PrintWriter(stringWriter));
            //logger.error("Seguimiento: ".concat(stringWriter.toString()));
        }
    }

    public void select_FecVal() {
        FecValorMSA = 0;

        //logger.debug("Ingresa a select_FecVal()");
        try {
            if (FecValor != null) {
                formatDate(FecValor);

                //Default_fec_contable
                pagaresBean.setDiaOpera(formatDate(fechaContableBean.getFecha()));

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
                pagaresBean.setDiaOpera(formatDate(FecValor));
                select_Moneda();
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La fecha es incorrecta..."));
                FecValor = fechaContableBean.getFecha();
                return;
            }

        } catch (NumberFormatException | DateTimeException | ParseException | SQLException exception) {
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
            pagaresBean.setDiaOpera(formatDate(FecValor));
            listaSubContrato = null;
            pagaresBean.setFideicomiso("");
            pagaresBean.setSubFiso("");

            DeshabilitarComponentes();
            //logger.debug("getDiaOpera" + pagaresBean.getDiaOpera());
            select_Moneda();

            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
        } catch (ParseException ex) {
            logger.error("Error al formatear fecha valor en ConfirmaFecValor()");
        }
    }

    public void NiegaFecValor() {
        //logger.debug("Ingresa a NiegaFecValor()");
        try {
            FecValorMSA = 0;
            FecValor = fechaContableBean.getFecha();
            pagaresBean.setDiaOpera(formatDate(FecValor));
            RequestContext.getCurrentInstance().execute("dlgFecVal.hide();");
        } catch (ParseException exception) {
            logger.error("Error al formatear fecha en NiegaFecValor()");
        }

    }

    public void checkFideicomiso() {
        try {
            // Write_Log
            //logger.debug("Ingresa a checkFideicomiso()");

            //logger.debug("Fideicomiso: ".concat(pagaresBean.getFideicomiso()));
            DeshabilitarComponentes();
            contratoBean = new ContratoBean();

            pagaresBean.setFideicomiso(pagaresBean.getFideicomiso().trim());
            listaSubContrato = null;
            pagaresBean.setSubFiso("");
            this.subFiso = "";

            // Check_Fideicomiso_vacío
            if ("".equals(pagaresBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(pagaresBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "El campo Fideicomiso debe ser un campo numérico..."));
                pagaresBean.setFideicomiso("");
                return;
            }

            if (Integer.parseInt(pagaresBean.getFideicomiso()) >= Integer.parseInt("2147483647")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El número de Fideicomiso es inválido..."));
                pagaresBean.setFideicomiso("");
                return;
            }

            //_Valida_si_el_contrato_existe_Activo
            contratoBean = cTesoreria.onTesoreria_checkFideicomiso(Integer.parseInt(pagaresBean.getFideicomiso()));

            //_Valida_que_permita_operaciones_de_Inversión_y_no_esta_bloqueado
            if (contratoBean.getContrato() == Integer.parseInt(pagaresBean.getFideicomiso())
                    && cTesoreria.onTesoreria_CtoBloqueado(contratoBean.getContrato(), "I")) {

                listaSubContrato = cTesoreria.onTesoreria_ListadoSubContratoSrv(Integer.parseInt(pagaresBean.getFideicomiso()));

                if (contratoBean.getbSubContrato() && contratoBean.getProrrateo() == 0) {

                    habilitaBotonCancelar = 1;
                    habilitaTxtSubFiso = false;
                    //logger.debug("SubContrato: " + contratoBean.getSubcontrato());

                    pagaresBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(pagaresBean.getFideicomiso(), pagaresBean.getSubFiso(), pagaresBean.getEntidadFinan(), tipoMovimiento);

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
                    habilitaBotonCancelar = 1;
                    habilitaTxtSubFiso = false;

                    pagaresBean.setSubFiso(String.valueOf(0)); // SubFiso

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(pagaresBean.getFideicomiso(), pagaresBean.getSubFiso(), pagaresBean.getEntidadFinan(), tipoMovimiento);

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
                habilitaSelCtoInv = 0;
                habilitaTxtContenedor = 0;
                habilitaSelCustodio = 0;

                pagaresBean.setCtoInver(0);
                pagaresBean.setCustodio("");
                CtaCheques = "";

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
                pagaresBean.setFideicomiso("");
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
            // Write_Log
            //logger.debug("Ingresa a checkSubFiso()");
            String[] SubFisoSeleccion = subFiso.split(".-");
            String reportSubFiso = SubFisoSeleccion[0];
            pagaresBean.setSubFiso(reportSubFiso.trim());

            //_Check_Fideicomiso_esta_vacío
            if ("".equals(pagaresBean.getFideicomiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                return;
            }

            // Check_SubFideicomiso_vacío
            if ("".equals(pagaresBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
                return;
            }

            // Check_Is_Numeric
            if (!isNumeric(pagaresBean.getSubFiso())) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso debe ser un campo numérico..."));
                pagaresBean.setSubFiso("");
                return;
            }

            // Check_SubFiso_vacío
            if (!pagaresBean.getSubFiso().equals("")) {
                // Disabled
                DeshabilitarComponentes();

                //_Valida_el_Sub_contrato_existe_Activo
                //_Valida_que_permita_operaciones_de_Inversión_y_no_este_bloqueado
                //_Parametros:_DatosContrato,_SubFiso,_sTipoBloque
                if (cTesoreria.onTesoreria_VerificaSubContratoSrv(contratoBean, Integer.parseInt(pagaresBean.getSubFiso()), "I")) {

                    habilitaTxtSubFiso = false;
                    habilitaBotonCancelar = 1;

                    // Get_Contrato_Inversion				
                    ctoInversion = cTesoreria.onTesoreria_GetCtoInv(pagaresBean.getFideicomiso(), pagaresBean.getSubFiso(), pagaresBean.getEntidadFinan(), tipoMovimiento);

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
                    pagaresBean.setSubFiso("");
                    return;
                }
            } else {
                pagaresBean.setSubFiso("0");
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
        custodio = null;
        pagaresBean.setCustodio("");

        try {

            // Validate_Servicio
            if (sCtoInver.equals("0")) {
                CtaCheques = "";
                pagaresBean.setCtoInver(0);
                pagaresBean.setEntidadFinan(0);
                habilitaSelCustodio = 0;

                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
                return;
            } else {

                String[] Id_CtoInv;
                String CtoInv = "", NumInterm = "";

                //_Recuperar_los_valores_de_la_lista_de_Contrato_de_Inversión
                //logger.debug("Valor de CtoInver:" + sCtoInver);
                Id_CtoInv = sCtoInver.split("/");

                //_Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                CtoInv = Id_CtoInv[0];
                NumInterm = Id_CtoInv[1]; //_Intermediario_o_Entidad_Financiera

                pagaresBean.setCtoInver(Double.parseDouble(CtoInv));
                pagaresBean.setEntidadFinan(Integer.parseInt(NumInterm));

                //_Determinar_Cuenta_de_Cheques
                CtaCheques = cTesoreria.onTesoreria_GetCtaCheques(pagaresBean.getFideicomiso(), pagaresBean.getSubFiso(), pagaresBean.getEntidadFinan(), pagaresBean.getCtoInver());
                if (CtaCheques.contains("No existe contrato de Inversión")) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe contrato de Inversión..."));
                    CtaCheques = "";
                }

                // Determinar_Custodio
                //logger.debug("Consulta custodio ");
                //Habilita_Custodio
                habilitaSelCustodio = 1;
                ArrayList<Integer> sComplementoCust = new ArrayList<Integer>();

                if (NumInterm.equals("1")) {
                    sComplementoCust.add(1);
                } else {
                    sComplementoCust.add(2);
                }

                custodio = cTesoreria.onTesoreria_GetCustodio(386, sComplementoCust);

                // ****************************************************************
                /*
				//logger.debug("******Datos seleccionando Contrato de Inversión*****");
				//logger.debug("Entidad / Intermediario: " + pagaresBean.getEntidadFinan());				
				//logger.debug("Cto. Inversión: " + pagaresBean.getCtoInver());	
				//logger.debug("Sub Clave: " + iSubClave);	
				//logger.debug("Cuenta cheques " + CtaCheques);	
                 */
                // ***********************************************************************
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

    public void select_Custodio() {
        // Write_Log
        //logger.debug("Ingresa a select_Custodio()");

        // Validate_Servicio
        if (pagaresBean.getCustodio().equals("0")) {

            pagaresBean.setCustodio("");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
            return;
        } else {
            //logger.debug("Custodio:" + pagaresBean.getCustodio());
            habilitaTxtContenedor = 1;

            // Establece_Tipo_de_Cambio
            pagaresBean.setMoneda(1);
            pagaresBean.setTipoCambio(1);
            habilitaTxtMoneda = false;
        }
    }

    public void checkTipoCambio() {
        try {

            if ("".equals(pagaresBean.getTipoCambio())) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de cambio no puede estar vacío..."));
                return;
            }

            if (!isDoubleNumeric(String.valueOf(pagaresBean.getTipoCambio()))) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Tipo de cambio debe ser un campo numérico..."));
                return;
            }

            if (pagaresBean.getTipoCambio() <= 0) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de cambio debe ser mayor a Cero..."));
                return;
            }

            pagaresBean.setTipoCambio(Double.parseDouble(formatFormatPrec(pagaresBean.getTipoCambio())));

        } catch (ArithmeticException | NumberFormatException exception) {
            pagaresBean.setTipoCambio(0.00);
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Tipo de cambio debe ser un campo numérico..."));
        }

    }

    public void CalculaIntereses() {
        try {
            // Write_Log
            //logger.debug("Ingresa a CalculaIntereses()");
            if (Integer.parseInt(pagaresBean.getPlazo()) > 0 && Double.parseDouble(pagaresBean.getImporte()) > 0 && Double.parseDouble(pagaresBean.getTasa()) > 0) {

                pagaresBean.setPremio(formatDecimalImp(Double.parseDouble(formatDecimalImp(Double.parseDouble(pagaresBean.getImporte()) * Integer.parseInt(pagaresBean.getPlazo()) * Double.parseDouble(pagaresBean.getTasa()) / 36000))));
            } else {
                pagaresBean.setPremio("0.00");
                dImpMovto = 0.00;
            }

        } catch (ArithmeticException | NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " CalculaIntereses()";
            pagaresBean.setPremio("0.00");
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
        // Write_Log
        //logger.debug("Ingresa a bConfirmaCancelacion()");
        try {
            listaSubContrato = null;
            pagaresBean.setFideicomiso("");
            pagaresBean.setSubFiso("");
            pagaresBean.setDiaOpera(fechaContableBean.getFechaContable());
            DeshabilitarComponentes();
            init();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/tesoreriaPagares.sb");
        } catch (IOException ex) {
            mensajeError = "Error al redireccionar: " + ex.getMessage();
        }
    }

    public void bAceptaPagares() {

        // Write_Log
        //logger.debug("Ingresa a bAceptaPagares()");
        // Establecer_valores_por_default_para_Pagares
        iSecEmis = 0;
        pagaresBean.setIdMercado(3);
        pagaresBean.setIdInstrume(16);
        pagaresBean.setNomPizarra("PAGARE");
        pagaresBean.setSerieEmis("0");
        pagaresBean.setCuponVig("0");
        pagaresBean.setTitulos("1");
        pagaresBean.setPrecioEmis(1);

        if (ValidacionesPantalla()) {
            ValidacionesNegocio();
        }
    }

    public boolean ValidacionesPantalla() {
        // Write_Log
        //logger.debug("Ingresa a ValidacionesPantalla()");
        boolean respuesta = true;
        // Check_Fideicomiso_vacío
        if ("".equals(pagaresBean.getFideicomiso())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
            respuesta = false;
        }

        if (pagaresBean.getSubFiso() == null || "".equals(pagaresBean.getSubFiso())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
            respuesta = false;
        }

        // Check_Cto Inversion_vacío
        if (sCtoInver == null || "".equals(sCtoInver) || pagaresBean.getCtoInver() == 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El campo Contrato de Inversión no puede estar vacío..."));
            respuesta = false;
        }

        if (pagaresBean.getEntidadFinan() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //"Fiduciario", "El campo Intermediario no puede estar vacío..."));
            respuesta = false;
        }

        if (pagaresBean.getIdMercado() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mercado no puede estar vacío..."));
            respuesta = false;
        }

        if (pagaresBean.getIdInstrume() == 0) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
            //"Fiduciario", "El campo Instrumento no puede estar vacío..."));
            respuesta = false;
        }

        if (pagaresBean.getCustodio() == null || pagaresBean.getCustodio().equals("") || pagaresBean.getCustodio().equals("0")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Custodio no puede estar vacío..."));
            respuesta = false;
        }

        //Validaciones para Tipo de cambio
        if (pagaresBean.getMoneda() != 0) {
            if ("".equals(pagaresBean.getTipoCambio())) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de cambio no puede estar vacío..."));
                respuesta = false;
            } else if (!isDoubleNumeric(String.valueOf(pagaresBean.getTipoCambio()))) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Tipo de cambio debe ser un campo numérico..."));
                respuesta = false;
            } else if (pagaresBean.getMoneda() != 1 && pagaresBean.getTipoCambio() == Double.parseDouble("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de cambio para este dia, por favor proporcione uno..."));
                respuesta = false;
            } else if (pagaresBean.getTipoCambio() <= 0) {
                pagaresBean.setTipoCambio(0.00);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de cambio debe ser mayor a Cero..."));
                respuesta = false;
            } else {
                pagaresBean.setTipoCambio(Double.parseDouble(formatFormatPrec(pagaresBean.getTipoCambio())));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
            respuesta = false;
        }

        //Validaciones para Plazo
        if (pagaresBean.getPlazo() == null || "".equals(pagaresBean.getPlazo())) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo no puede estar vacío..."));
            respuesta = false;
        } else if (!isNumeric(pagaresBean.getPlazo())) {
            // Clean
            dImpMovto = 0;
            FecMovto = null;
            pagaresBean.setPlazo("0");
            pagaresBean.setPremio("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Plazo debe ser un campo numérico..."));
            respuesta = false;
        } else if (Integer.parseInt(pagaresBean.getPlazo()) > 10000 || Integer.parseInt(pagaresBean.getPlazo()) < 1) {

            dImpMovto = 0;
            FecMovto = null;
            pagaresBean.setPlazo("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Plazo debe ser mayor que Cero y no exceder a 10,000..."));
            respuesta = false;
        } else {

            try {
                FecMovto = formatDate(cFecha.Proyecta_Tiempo(
                        parseDate(pagaresBean.getDiaOpera()), Integer.parseInt(pagaresBean.getPlazo()), "Dias"));

                if (FecMovto != null) {
                    if (cFecha.onFecha_DiaHabil(parseDate(FecMovto)) == 1) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Fiduciario", "La fecha Proyectada no es hábil " + FecMovto));

                        // Clean
                        FecMovto = null;
                        pagaresBean.setPlazo("0");
                        RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
                        respuesta = false;
                    }
                } else {
                    FecMovto = null;
                    pagaresBean.setPlazo("0");

                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Plazo erróneo..."));
                    RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
                    respuesta = false;
                }
            } catch (ParseException | SQLException ex) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Plazo erróneo"));
                RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
                respuesta = false;
            }
        }

        if ("".equals(pagaresBean.getTasa()) || pagaresBean.getTasa() == null) {
            // Clean
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tasa no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(pagaresBean.getTasa())) {
            // Clean
            dImpMovto = 0;
            pagaresBean.setTasa("0");
            pagaresBean.setPremio("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Tasa debe ser un campo numérico..."));
            respuesta = false;
        } else if (Double.parseDouble(pagaresBean.getTasa()) <= 0) {
            dImpMovto = 0;
            pagaresBean.setTasa("0");
            pagaresBean.setPremio("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Tasa debe ser mayor que cero..."));
            respuesta = false;
        } else {
            pagaresBean.setTasa(formatDecimalImp(Double.parseDouble(pagaresBean.getTasa())));
        }

        if ("".equals(pagaresBean.getImporte()) || pagaresBean.getImporte() == null) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe no puede estar vacío..."));
            respuesta = false;
        } else if (!isDoubleNumeric(pagaresBean.getImporte())) {
            // Clean
            dImpMovto = 0;
            pagaresBean.setImporte("0");
            pagaresBean.setPremio("0");
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe debe ser un campo numérico..."));
            respuesta = false;
        } else if (Double.parseDouble(pagaresBean.getImporte()) <= 0) {

            //Clean
            dImpMovto = 0;
            pagaresBean.setImporte("0");
            pagaresBean.setPremio("0");

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe debe ser mayor a cero..."));
            respuesta = false;
        } else {
            pagaresBean.setImporte(formatDecimalImp(Double.parseDouble(pagaresBean.getImporte())));
        }
        
        if (pagaresBean.getPlazo() != null && !pagaresBean.getPlazo().equals("")
                && pagaresBean.getTasa() != null && !pagaresBean.getTasa().equals("")
                && pagaresBean.getImporte() != null && !pagaresBean.getImporte().equals("")) {
            if (isNumeric(pagaresBean.getPlazo()) && isDoubleNumeric(pagaresBean.getTasa()) && isDoubleNumeric(pagaresBean.getImporte())) {
                CalculaIntereses();
                if (Double.parseDouble(pagaresBean.getPremio()) >= 0) {
                    dImpMovto = Double.parseDouble(formatDecimalImp(Double.parseDouble(pagaresBean.getImporte()) + Double.parseDouble(pagaresBean.getPremio())));
                }
            }
        }

        if (pagaresBean.getPremio() == null || pagaresBean.getPremio().equals("")) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Intereses no puede estar vacío"));
            respuesta = false;
        } else if (Double.parseDouble(pagaresBean.getPremio()) < 0) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Intereses debe ser mayor o igual a Cero..."));
            respuesta = false;
        }

        if (FecMovto == null) {
            // Set_Message
            //FacesContext.getCurrentInstance().addMessage("Fiduciario",
            //new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proporcione la Fecha de Movimiento"));
            respuesta = false;
        }

        return respuesta;
    }

    public boolean ValidacionesNegocio() {

        // Write_Log
        //logger.debug("Ingresa a ValidacionesNegocio()");
        try {
            pagaresBean.setCuentaCheques(CtaCheques);
            this.setMensajeErrorConsulta(cTesoreria.onTesoreria_VerificaMoneda_Pagare(pagaresBean));

            if (!"".equals(this.getMensajeErrorConsulta())) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", this.getMensajeErrorConsulta()));
                return false;
            }

            //_Valida_si_el_contrato_tiene_liquidez			
            if (cTesoreria.onTesoreria_VerificaLiquidez_Pagare(Double.parseDouble(pagaresBean.getImporte()), pagaresBean.getFideicomiso(), pagaresBean.getSubFiso(),
                    pagaresBean.getEntidadFinan(), pagaresBean.getCtoInver(), Integer.parseInt(pagaresBean.getCustodio()))) {

                AplicaOperacion();
                RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
            } else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no tiene Líquidez"));
                pagaresBean.setFideicomiso("");
                listaSubContrato = null;
                pagaresBean.setSubFiso("");
                this.subFiso = "";
                DeshabilitarComponentes();
                init();
                RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
                return false;
            }

            return true;

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ValidacionesNegocio()";
        }

        return false;
    }

    public void AplicaOperacion() {
        try {
            // Write_Log
            //logger.debug("Ingresa a AplicaOperacion()");

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
            outParameterBean = cTesoreria.onTesoreria_Ejecuta_ReportoPagare(pagaresBean.getDiaOpera(), //fecha_valor	
                    FecValorMSA, "LINEA",
                    Integer.parseInt(pagaresBean.getFideicomiso()), Integer.parseInt(pagaresBean.getSubFiso()),
                    tipoMovimiento, "", //_Redaccion
                    String.valueOf(pagaresBean.getTasa()), Integer.parseInt(pagaresBean.getPlazo()),
                    pagaresBean.getNomPizarra(), pagaresBean.getSerieEmis(),
                    Integer.parseInt(pagaresBean.getCuponVig()), Integer.parseInt(pagaresBean.getTitulos()),
                    pagaresBean.getPrecioEmis(), pagaresBean.getEntidadFinan(),
                    pagaresBean.getIdMercado(), pagaresBean.getIdInstrume(),
                    iSecEmis, Integer.parseInt(pagaresBean.getCustodio()),
                    pagaresBean.getMoneda(), pagaresBean.getCtoInver(),
                    Double.parseDouble(pagaresBean.getImporte()), FecMovto,
                    String.valueOf(pagaresBean.getPremio()),
                    "0", //_Comisión
                    "0", //_Iva_Comisión
                    "0", //ISR
                    "FALSE", //_Retiene ISR
                    pagaresBean.getTipoCambio(),
                    0, //_Sub_Operacion
                    0, //_Ref. CICS
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
                pagaresBean.setFideicomiso("");
                pagaresBean.setSubFiso("");
                DeshabilitarComponentes();
                pagaresBean.setFolioReporto(folio);
                init();
            } //No_Aplicado
            else {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Movimiento No se Aplicó: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", "")))));
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
        // Write_Log
        //logger.debug("Ingresa a DeshabilitarComponentes()");

        // Clean_Bean
        outParameterBean = new OutParameterBean();

        // Variable
        FecMovto = null;
        sCtoInver = "";
        dImpMovto = 0;
        CtaCheques = "";
        //FecValorMSA = 0;
        iSecEmis = 0;

        habilitaSelCtoInv = 0;
        habilitaSelCustodio = 0;
        habilitaTxtSubFiso = true;
        habilitaTxtContenedor = 0;
        habilitaTxtMoneda = true;
        HabilitaPanelInv = true;
        habilitaBotonCancelar = 0;

        isHabilitadoTC = true;
        colorHabilitadoTC = "inputNumber2";

        // Clean_Campos
        pagaresBean.setNomPizarra("");
        pagaresBean.setSerieEmis("");
        pagaresBean.setCuponVig("");
        pagaresBean.setPrecioEmis(0);
        pagaresBean.setFolioReporto(0);
        pagaresBean.setEntidadFinan(0);
        pagaresBean.setIdMercado(0);
        pagaresBean.setIdInstrume(0);
        pagaresBean.setPlazo("");
        pagaresBean.setImporte("");
        pagaresBean.setTasa("");
        pagaresBean.setPremio("");
        pagaresBean.setCustodio("");
        pagaresBean.setTitulos("");
        pagaresBean.setCtoInver(0);
        pagaresBean.setMoneda(0);
        pagaresBean.setTipoCambio(0);
        RequestContext.getCurrentInstance().update(":formPagares:txtFecMovto");
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

    private synchronized String formatDate(Date date) throws ParseException {
        return simpleDateFormat2.format(date);
    }

    private synchronized Date parseDate(String date) throws ParseException {
        return simpleDateFormat2.parse(date);
    }

    private synchronized String formatDecimalImp(Double decimal) {
        return decimalFormatImp.format(decimal);
    }

    private synchronized String formatFormatPrec(Double decimal) {
        return decimalFormatPrec.format(decimal);
    }

}