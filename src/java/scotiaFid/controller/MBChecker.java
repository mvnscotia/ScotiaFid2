/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBChecker.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date; 
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext; 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import scotiaFid.bean.DetMasivoBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.ResponseTraspasoBean;
import scotiaFid.bean.TPendientesBean;
import scotiaFid.bean.TPendientesDetBean;
import scotiaFid.bean.TransferChqBean;
import scotiaFid.dao.CContabilidad;
import scotiaFid.dao.CTraspaso;
import scotiaFid.util.LogsContext;
//Class

@Named("mbChecker")
@ViewScoped
public class MBChecker implements Serializable {

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
    private TPendientesBean tPendientesBean = new TPendientesBean();
    // -----------------------------------------------------------------------------
    private TPendientesDetBean tPendientesDetBean = new TPendientesDetBean();
    // -----------------------------------------------------------------------------
    private DetMasivoBean detMasivoBean = new DetMasivoBean();
    // -----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    //-----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    private static final Logger logger = LogManager.getLogger(MBChecker.class);
    // -----------------------------------------------------------------------------
    private List<TPendientesBean> LiChecker = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<Double> LImportes = new ArrayList<>();
    // -----------------------------------------------------------------------------	
    private TPendientesBean folioCheckerBean;
    // -----------------------------------------------------------------------------	
    private List<TPendientesDetBean> detalleCheckerBean = new ArrayList<>();
    // -----------------------------------------------------------------------------	
    Map<String, String> ArregloDespliegueDato = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private List<DetMasivoBean> LiDetMasivo = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<TransferChqBean> fdTrans = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private List<String> URLXML = new ArrayList<>();
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F O R M A T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // -----------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    // -----------------------------------------------------------------------------
    Locale locale = new Locale("es", "MX"); 
    //Number_Format
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
    // -----------------------------------------------------------------------------    
    DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
    // -----------------------------------------------------------------------------    
    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormat().getDecimalFormatSymbols();
    // -----------------------------------------------------------------------------     
    private static final DecimalFormat numerFormat = new DecimalFormat("###############");
    // -----------------------------------------------------------------------------    
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * A T R I B U T O S   P R I V A D O S * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private String nombreObjeto;
    // **************************************************************************** 
    // -----------------------------------------------------------------------------
    //_Habilitar_Botones,_Paneles_y_Listas_Desplegables	
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnSelectTodos;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnLimpia;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnCancela;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnActualiza;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnAplica;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelCriterios;
    // -----------------------------------------------------------------------------
    private boolean HabilitaPanelObs;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnCancel;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnDespliega;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnEditObs;
    // -----------------------------------------------------------------------------
    private boolean HabilitaBtnAcept;
    // -----------------------------------------------------------------------------
    private boolean HabilitaObsMaker;
    // -----------------------------------------------------------------------------
    private boolean HabilitaObsChecker;
    // -----------------------------------------------------------------------------
    //_Campos_Pnatalla	
    // -----------------------------------------------------------------------------
    private String FolioDel;
    // -----------------------------------------------------------------------------
    private String FolioAl;
    // -----------------------------------------------------------------------------
    private String Nombre;
    // -----------------------------------------------------------------------------
    private double Importe;
    // -----------------------------------------------------------------------------
    private String sMsjErr;
    // -----------------------------------------------------------------------------
    private int GF_Usuario;
    // -----------------------------------------------------------------------------
    private String sObsMaker;
    // -----------------------------------------------------------------------------
    private int iNumFisoMas;
    // -----------------------------------------------------------------------------
    private int iFolioMas;
    // -----------------------------------------------------------------------------
    private TPendientesBean selectedTrans;
    // ----------------------------------------------------------------------------- 
    private double dImporteDetalle;
    // -----------------------------------------------------------------------------
    private String sRegistros;
    // -----------------------------------------------------------------------------

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    // -----------------------------------------------------------------------------
    CContabilidad cContabilidad = new CContabilidad();
    // -----------------------------------------------------------------------------
    CTraspaso cTraspaso = new CTraspaso();
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

    public TPendientesBean gettPendientesBean() {
        return tPendientesBean;
    }

    public void settPendientesBean(TPendientesBean tPendientesBean) {
        this.tPendientesBean = tPendientesBean;
    }

    public TPendientesDetBean gettPendientesDetBean() {
        return tPendientesDetBean;
    }

    public void settPendientesDetBean(TPendientesDetBean tPendientesDetBean) {
        this.tPendientesDetBean = tPendientesDetBean;
    }

    public DetMasivoBean getDetMasivoBean() {
        return detMasivoBean;
    }

    public void setDetMasivoBean(DetMasivoBean detMasivoBean) {
        this.detMasivoBean = detMasivoBean;
    }

    public OutParameterBean getOutParameterBean() {
        return outParameterBean;
    }

    public void setOutParameterBean(OutParameterBean outParameterBean) {
        this.outParameterBean = outParameterBean;
    }

    public List<TPendientesBean> getLiChecker() {
        return LiChecker;
    }

    public void setLiChecker(List<TPendientesBean> liChecker) {
        LiChecker = liChecker;
    }

    public List<Double> getLImportes() {
        return LImportes;
    }

    public void setLImportes(List<Double> lImportes) {
        LImportes = lImportes;
    }

    public List<TPendientesDetBean> getDetalleCheckerBean() {
        return detalleCheckerBean;
    }

    public void setDetalleCheckerBean(List<TPendientesDetBean> detalleCheckerBean) {
        this.detalleCheckerBean = detalleCheckerBean;
    }

    public Map<String, String> getArregloDespliegueDato() {
        return ArregloDespliegueDato;
    }

    public void setArregloDespliegueDato(Map<String, String> arregloDespliegueDato) {
        ArregloDespliegueDato = arregloDespliegueDato;
    }

    public List<String> getURLXML() {
        return URLXML;
    }

    public void setURLXML(List<String> uRLXML) {
        URLXML = uRLXML;
    }

    public List<DetMasivoBean> getLiDetMasivo() {
        return LiDetMasivo;
    }

    public void setLiDetMasivo(List<DetMasivoBean> liDetMasivo) {
        LiDetMasivo = liDetMasivo;
    }

    public boolean isHabilitaBtnSelectTodos() {
        return HabilitaBtnSelectTodos;
    }

    public void setHabilitaBtnSelectTodos(boolean habilitaBtnSelectTodos) {
        HabilitaBtnSelectTodos = habilitaBtnSelectTodos;
    }

    public boolean isHabilitaBtnLimpia() {
        return HabilitaBtnLimpia;
    }

    public void setHabilitaBtnLimpia(boolean habilitaBtnLimpia) {
        HabilitaBtnLimpia = habilitaBtnLimpia;
    }

    public boolean isHabilitaBtnCancela() {
        return HabilitaBtnCancela;
    }

    public void setHabilitaBtnCancela(boolean habilitaBtnCancela) {
        HabilitaBtnCancela = habilitaBtnCancela;
    }

    public boolean isHabilitaBtnActualiza() {
        return HabilitaBtnActualiza;
    }

    public void setHabilitaBtnActualiza(boolean habilitaBtnActualiza) {
        HabilitaBtnActualiza = habilitaBtnActualiza;
    }

    public boolean isHabilitaBtnAplica() {
        return HabilitaBtnAplica;
    }

    public void setHabilitaBtnAplica(boolean habilitaBtnAplica) {
        HabilitaBtnAplica = habilitaBtnAplica;
    }

    public boolean isHabilitaPanelCriterios() {
        return HabilitaPanelCriterios;
    }

    public void setHabilitaPanelCriterios(boolean habilitaPanelCriterios) {
        HabilitaPanelCriterios = habilitaPanelCriterios;
    }

    public boolean isHabilitaPanelObs() {
        return HabilitaPanelObs;
    }

    public void setHabilitaPanelObs(boolean habilitaPanelObs) {
        HabilitaPanelObs = habilitaPanelObs;
    }

    public boolean isHabilitaBtnCancel() {
        return HabilitaBtnCancel;
    }

    public void setHabilitaBtnCancel(boolean habilitaBtnCancel) {
        HabilitaBtnCancel = habilitaBtnCancel;
    }

    public boolean isHabilitaBtnDespliega() {
        return HabilitaBtnDespliega;
    }

    public void setHabilitaBtnDespliega(boolean habilitaBtnDespliega) {
        HabilitaBtnDespliega = habilitaBtnDespliega;
    }

    public boolean isHabilitaBtnEditObs() {
        return HabilitaBtnEditObs;
    }

    public void setHabilitaBtnEditObs(boolean habilitaBtnEditObs) {
        HabilitaBtnEditObs = habilitaBtnEditObs;
    }

    public boolean isHabilitaBtnAcept() {
        return HabilitaBtnAcept;
    }

    public void setHabilitaBtnAcept(boolean habilitaBtnAcept) {
        HabilitaBtnAcept = habilitaBtnAcept;
    }

    public boolean isHabilitaObsMaker() {
        return HabilitaObsMaker;
    }

    public void setHabilitaObsMaker(boolean habilitaObsMaker) {
        HabilitaObsMaker = habilitaObsMaker;
    }

    public boolean isHabilitaObsChecker() {
        return HabilitaObsChecker;
    }

    public void setHabilitaObsChecker(boolean habilitaObsChecker) {
        HabilitaObsChecker = habilitaObsChecker;
    }

    public TPendientesBean getFolioCheckerBean() {
        return folioCheckerBean;
    }

    public void setFolioCheckerBean(TPendientesBean folioCheckerBean) {
        this.folioCheckerBean = folioCheckerBean;
    }

    public String getFolioDel() {
        return FolioDel;
    }

    public void setFolioDel(String folioDel) {
        FolioDel = folioDel;
    }

    public String getFolioAl() {
        return FolioAl;
    }

    public void setFolioAl(String folioAl) {
        FolioAl = folioAl;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }

    public String getsMsjErr() {
        return sMsjErr;
    }

    public void setsMsjErr(String sMsjErr) {
        this.sMsjErr = sMsjErr;
    }

    public int getGF_Usuario() {
        return GF_Usuario;
    }

    public void setGF_Usuario(int gF_Usuario) {
        GF_Usuario = gF_Usuario;
    }

    public String getsObsMaker() {
        return sObsMaker;
    }

    public void setsObsMaker(String sObsMaker) {
        this.sObsMaker = sObsMaker;
    }

    public int getiNumFisoMas() {
        return iNumFisoMas;
    }

    public void setiNumFisoMas(int iNumFisoMas) {
        this.iNumFisoMas = iNumFisoMas;
    }

    public int getiFolioMas() {
        return iFolioMas;
    }

    public void setiFolioMas(int iFolioMas) {
        this.iFolioMas = iFolioMas;
    }

    public TPendientesBean getSelectedTrans() {
        return selectedTrans;
    }

    public void setSelectedTrans(TPendientesBean selectedTrans) {
        this.selectedTrans = selectedTrans;
    }

    public double getdImporteDetalle() {
        return dImporteDetalle;
    }

    public void setdImporteDetalle(double dImporteDetalle) {
        this.dImporteDetalle = dImporteDetalle;
    }

    public String getsRegistros() {
        return sRegistros;
    }

    public void setsRegistros(String sRegistros) {
        this.sRegistros = sRegistros;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public MBChecker() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
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

                // Mensajes_de_Error
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.MBChecker";

                Importe = 0;
                Nombre = "";
                FolioAl = "";
                FolioDel = "";

                // Deshabilitar_paneles
                HabilitaPanelCriterios = false;
                HabilitaPanelObs = true;
                HabilitaBtnDespliega = false;

                HabilitaBtnEditObs = true;
                HabilitaBtnCancel = false;
                HabilitaBtnAcept = false;
                HabilitaObsMaker = true;
                HabilitaObsChecker = true;

                // Set_Bean
                this.settPendientesBean(new TPendientesBean());

                // Actualiza_Vista
                ActualizaVista();
            }
        } catch (IOException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Construct()";
            logger.error(mensajeError);
        }
    }

    public void ActualizaVista() {

        HabilitaPanelCriterios = false;
        HabilitaPanelObs = true;
        List<FacesMessage> mensajesDeError = new ArrayList<>();
                
        // Check_Is_Numeric
        if(!FolioDel.isEmpty()) {
            FolioDel = FolioDel.trim();
	        if (!isNumeric(FolioDel)) {
	            // Set_Message
	        	mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Folio Del debe ser un campo numérico..."));
	            FolioDel = "";
	        }
        }
        
        // Check_Is_Numeric
        if(!FolioAl.isEmpty()) {
            FolioAl = FolioAl.trim();
        	if (!isNumeric(FolioAl)) {
            // Set_Message
        	mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Folio Al debe ser un campo numérico..."));
            FolioAl = "";
        	}
        }
        
        if(mensajesDeError.isEmpty() && (!FolioDel.isEmpty() && !FolioAl.isEmpty())) { 
        	if (Double.parseDouble(FolioAl) < Double.parseDouble(FolioDel)) {
            // Set_Message
        	mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Folio Al no puede ser menor al campo Folio Del..."));
            FolioAl = "";
            FolioDel = "";
        	}
        }
        
        if (mensajesDeError.isEmpty()) {
        LiChecker = cContabilidad.onContabilidad_ConsultaChecker(FolioDel, FolioAl, Nombre, Importe); 

	        if (LiChecker.size() > 0) { 
	            LImportes = cContabilidad.onContabilidad_GetImportes();
	        } else {
	            ArregloDespliegueDato = new LinkedHashMap<String, String>();
	        }

        }else { 
	        for (FacesMessage mensaje : mensajesDeError) {
	            FacesContext.getCurrentInstance().addMessage(null, mensaje);
	        }
        }
    }

    public void onRowSelectDetalleCheck(SelectEvent event) {
        // Get_InvRechazosBean
    	ArregloDespliegueDato = new LinkedHashMap<String, String>();
        this.folioCheckerBean = (TPendientesBean) event.getObject();

        if (folioCheckerBean.getTpd_Folio() != null) {

            if (folioCheckerBean.getTpd_Tipo_Mov().equals("Liquidacion Masiva")) {
                HabilitaBtnDespliega = true;
            } else {
                HabilitaBtnDespliega = false;
            }

            iNumFisoMas = folioCheckerBean.getTpd_Num_Fiso();
            iFolioMas = folioCheckerBean.getTpd_Num_Instr();
           
            GF_Usuario = 0;
            sObsMaker = "";

            if (folioCheckerBean.getTpd_Usuario_Opera() != 0) {
                GF_Usuario = folioCheckerBean.getTpd_Usuario_Opera();
            }

            if (folioCheckerBean.getTpd_Obs_Maker() != null || !folioCheckerBean.getTpd_Obs_Maker().equals("")) {
                sObsMaker = folioCheckerBean.getTpd_Obs_Maker();
            }

            detalleCheckerBean = cContabilidad.onContabilidad_DecodificaChecker(folioCheckerBean.getTpd_Folio());

            //Decodifica_Cadena_de_Parametros_Detalles_Checker
            String sCadUp = "";

            for (TPendientesDetBean tpendientesDetBean : detalleCheckerBean) {
                if (tpendientesDetBean.getTpdd_Dato().equals("CADPARAM")) {
                    detalleCheckerBean.indexOf(tpendientesDetBean);
                    sCadUp += tpendientesDetBean.getTpdd_Valor();
                }
            }

            String[] ArregloDetalleCambios = sCadUp.split("\\t");

            //Separa_Valores_que_se_Despliegan_en_pantalla		
            for (String str : ArregloDetalleCambios) {
                if (str.contains("|D")) {
                    ArregloDespliegueDato.put(str.substring(0, str.indexOf("|")), str.substring(str.indexOf("|") + 1).replace("|D", ""));
                }
            }

            //Fecha_que_se_opero
            ArregloDespliegueDato.put("Operado", formatDate2(folioCheckerBean.getTpd_Fec_Opera()));

            //_Faltan
            List<String> ListFaltan = cContabilidad.onContabilidad_FaltanChecker(folioCheckerBean.getTpd_Folio());

            if (ListFaltan.size() > 0) {
                ArregloDespliegueDato.put("Valor Firmas", ListFaltan.get(0) + " de " + ListFaltan.get(2));
            }

            //UsuarioAutorizo
            if (folioCheckerBean.getTpd_Usuario_Au() != 0) {
                ArregloDespliegueDato.put("Autorizó", folioCheckerBean.getTpd_Usuario_Au().toString());
                ArregloDespliegueDato.put("F. Autorizado", formatDate2(folioCheckerBean.getTpd_Fec_Au()));
            }
        }
    }

    public void SelectChecked() {
        for (TPendientesBean checker : LiChecker) {
            checker.setSeleccion(true);
        }
    }

    public void ClearChecked() {

        for (TPendientesBean checker : LiChecker) {
            checker.setSeleccion(false);
        }

        //Limpia Criterios de Consulta
        FolioDel = new String();
        FolioAl = new String();
        Nombre = new String();
        Importe = 0;

        ActualizaVista();
    }

    public void Cancelar() {

        if (LiChecker.size() > 0) {
            sMsjErr = "";
            DeshabilitaBtns();
            RequestContext.getCurrentInstance().execute("dlgCancelarTransacc.show();");
            return;
        }

    }

    public void CancelChecker() {

        for (TPendientesBean checker : LiChecker) {
            if (checker.getSeleccion() == true && checker.getTpd_Status().equals("ACTIVO")) {

                //Aplica_SPN_CANCEL_MK 
                outParameterBean = cContabilidad.onContabilidad_CancelaChecker(checker.getTpd_Folio());

                // Aplicado_correctamente
                if (outParameterBean.getiNumFolioContab() == 1 && outParameterBean.getPsMsgErrOut().equals("")) {
                    // Set_Message
                    checker.setTpd_Status("CANCELADO");
                    sMsjErr += "Movimiento " + checker.getTpd_Folio() + " Cancelado correctamente".concat("\n");
                } // No_Aplicado
                else {
                    // Set_Message						
                    sMsjErr += "Movimiento " + checker.getTpd_Folio() + " No Aplicado con el siguiente error: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))).concat("\n");
                }
            }
        }

        HabilitaBtns();
        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El proceso de cancelación ha concluido..."));
    }

    public void NoCancelar() {
        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El proceso de cancelación ha concluido..."));
        HabilitaBtns();
    }

    public void AplicarTrans() {
        int contador = 0;
        if (LiChecker.size() > 0) {
            contador = LiChecker.stream().filter(pendiente -> (pendiente.getSeleccion() == true)).map(_item -> 1).reduce(contador, Integer::sum);
            if (contador > 0) {
                sMsjErr = "";
                DeshabilitaBtns();
                RequestContext.getCurrentInstance().execute("dlgAplicaTransacc.show();");
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "¡Aún no ha seleccionado ninguna transacción para aplicar!..."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay transacciones para aplicar..."));
        }
    }

    public void DeshabilitaBtns() {

        //Inhabilita_Botones
        HabilitaBtnSelectTodos = true;
        HabilitaBtnLimpia = true;
        HabilitaBtnCancela = true;
        HabilitaBtnActualiza = true;
        HabilitaBtnAplica = true;
    }

    public void HabilitaBtns() {

        //Inhabilita_Botones
        HabilitaBtnSelectTodos = false;
        HabilitaBtnLimpia = false;
        HabilitaBtnCancela = false;
        HabilitaBtnActualiza = false;
        HabilitaBtnAplica = false;
    }

    public void Edit_Obs() {
        HabilitaBtnEditObs = false;
        HabilitaBtnAcept = true;
        HabilitaBtnCancel = true;

        if (GF_Usuario != Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("usuarioNumero").toString())) {
            HabilitaObsMaker = false;
            HabilitaObsChecker = true;
        } else {
            HabilitaObsMaker = true;
            HabilitaObsChecker = false;
        }
    }

    public void Cancel_Obs() {
        HabilitaBtnEditObs = true;
        HabilitaBtnAcept = false;
        HabilitaBtnCancel = false;
        HabilitaObsMaker = true;
        HabilitaObsChecker = true;
    }

    public void BtnCriterios() {
        HabilitaPanelObs = false;
        HabilitaPanelCriterios = true;
    }

    public void BtnDespliegaMas() {
        LiDetMasivo = null;
        List<Double> TotalesDetMasivo = null;
         
        if (sObsMaker.trim().equals("TERCERO")) // Tipo_Persona
        {
            LiDetMasivo = cContabilidad.onContabilidad_DetalleTerceroMas(iNumFisoMas, iFolioMas);
            TotalesDetMasivo = cContabilidad.onContabilidad_Totales_DetTerceroMas(iNumFisoMas, iFolioMas);
        } else {
            LiDetMasivo = cContabilidad.onContabilidad_DetalleBenefici(iNumFisoMas, iFolioMas);
            TotalesDetMasivo = cContabilidad.onContabilidad_Totales_DetBeneficiMas(iNumFisoMas, iFolioMas);
        }

        if (TotalesDetMasivo.size() > 0) {
            dImporteDetalle = TotalesDetMasivo.get(0);
            sRegistros = formatNumerico(TotalesDetMasivo.get(1));
        }

        if (LiDetMasivo.size() == 0) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "No existen información con los criterios de búsqueda especificados..."));
        } else {
            RequestContext.getCurrentInstance().execute("detalleMasivo.show();");
        }

    }

    public void AplicaChecker() {
    	String sMensaje ="";
        try {

            for (TPendientesBean checker : LiChecker) {
                if (checker.getSeleccion() == true && checker.getTpd_Status().equals("ACTIVO")) {

                    GF_Usuario = checker.getTpd_Usuario_Opera();

                    if (GF_Usuario != Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString())) {
                    	boolean bTransfer= true;
                    	
                        // Aplica_SPN_EJECUTA_CHECK
                        // sCveTipoTran _iFolioAut_sRedacc 
                        outParameterBean = cContabilidad.onContabilidad_EjecutaChecker(checker.getTpd_Tipo_Mov(), checker.getTpd_Transacc(), checker.getTpd_Folio(), GF_Usuario);

                        // Aplicado_correctamente
                        if (outParameterBean.getbEjecuto() == 1) {
                            //Servicio_Web
                            //Valida_si_es_ScotiaPro
                            if (outParameterBean.getiNumFolioContab() > 0) {
                                bTransfer = TransferenciaPagos(checker.getTpd_Folio(), outParameterBean.getiNumFolioContab());
                            } 
                            
                            if(bTransfer == true) {
	                            checker.setTpd_Status("APLICADO");
	                            sMensaje = "Movimiento " + checker.getTpd_Folio() + " Aplicado correctamente. ".concat("\n");
                            }else {
	                            checker.setTpd_Status("CANCELADO");
	                        	//Cancela_Folio_Opera
	                        	cContabilidad.onCFDI_ModificaStatusChecker(checker.getTpd_Folio(),"CANCELADO");
                            	sMensaje = "Movimiento " + checker.getTpd_Folio()
                                + " No Aplicado correctamente: Se tuvo error en el Traspaso de Cheques".concat("\n");
                            }
                        } // No_Aplicado
                        else {
                            //Set_Message 
                        	sMensaje = "Movimiento " + checker.getTpd_Folio()
                                    + " No Aplicado: ".concat(String.valueOf(outParameterBean.getPsMsgErrOut().toUpperCase().replaceAll("ERROR", ""))).concat("\n");
                        }
                    } else {
                        // Set_Message
                    	sMensaje = "No puede liberar la transacción ".concat(checker.getTpd_Folio().toString()).concat(" mismo usuario. \n");
                    } 
                    
                    sMsjErr += sMensaje;
                } 
            }

            HabilitaBtns();
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El proceso de autorización ha concluido..."));
            ActualizaVista();

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " AplicaChecker()";
        }
    }

    public boolean TransferenciaPagos(int iFolio, int iRegistros) {

        //'**************************************************************
        //'*             S C O T I A   P R O - W S                      *
        //'**************************************************************
        //' Get_Data_Trans_Origen                                       *
        //'*************************************************************' 
    	double dFolio = 0;
    	boolean bTransfer = true;
        OutParameterBean outParameterBeanCancelaFolio = null;
        ResponseTraspasoBean response = new ResponseTraspasoBean();
        try {

            //Recuperar_valores_de_Transferencia
            fdTrans = cTraspaso.onTraspaso_SelectTrasferChequera(iFolio, iRegistros);

            //Obtiene_URL_y_XML
            URLXML = cTraspaso.onTraspaso_Select_URLXML();

            //for (int i = 0; i < fdTrans.size(); i++) {
            for ( TransferChqBean transferChqBean : fdTrans ) {
            	dFolio = transferChqBean.getFolio_Op();
                response = cTraspaso.onTraspaso_traspasoFondos(transferChqBean, URLXML.get(0), URLXML.get(1));

                if (response.getFolioConfirmacion() != null && response.getMsjError() == null) {
                    // Set_Message
                    sMsjErr += "Se realizó correctamente el traspaso a cuenta de cheques: " + transferChqBean.getCta_Origen()
                            + " a " + transferChqBean.getCta_Destino() + " por la cantidad de: " + transferChqBean.getImporte()
                            + ", con el folio de Confirmación: " + response.getFolioConfirmacion().concat("\n"); 
                } else {

                    // Set_Message
                    sMsjErr += "No se realizó el traspaso a cuenta de cheques: " + transferChqBean.getCta_Origen()
                            + " a " + transferChqBean.getCta_Destino() + " por la cantidad de: " + transferChqBean.getImporte()
                            + ", " + response.getMsjError();

                    bTransfer = false;
                    
                    //Cancelación_y_Reverso_del_Folio_Contable
                    //Cancelar_el_folio_contable_con_el_SP_“SP_CANCELA_FOLIO”
                    outParameterBeanCancelaFolio = cTraspaso.onTraspaso_CancelaFolio(transferChqBean.getFolio_Op());
 
                    if (outParameterBeanCancelaFolio.getbEjecuto() == 1) {
                        //Cambiar_el_status_de_“DETLIQUI”_a_Cancelado
                        if (cTraspaso.onTraspaso_CancelaFolioStatus(transferChqBean.getFolio_Op())) {
                            // Set_Message
                            sMsjErr += ". El Folio " + formatNumerico(transferChqBean.getFolio_Op()) + " fue Cancelado \n";
                        } else {
                            // Set_Message
                            sMsjErr += ". No se realizó la Cancelación del Status con Folio: " + formatNumerico(transferChqBean.getFolio_Op()) + "\n";
                        }
                    } else {
                        // Set_Message
                        sMsjErr += ". No se realizó la Cancelación y Reverso del Folio: " + formatNumerico(transferChqBean.getFolio_Op()) + "\n";
                    }
                }
            }
 
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException  exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " TransferenciaPagos()";
            logger.error(mensajeError);

            bTransfer = false;
            //Cancelación_y_Reverso_del_Folio_Contable
            //Cancelar_el_folio_contable_con_el_SP_“SP_CANCELA_FOLIO”
            outParameterBeanCancelaFolio = cTraspaso.onTraspaso_CancelaFolio(dFolio);

            if (outParameterBeanCancelaFolio.getbEjecuto() == 1) {
                //Cambiar_el_status_de_“DETLIQUI”_a_Cancelado
                if (cTraspaso.onTraspaso_CancelaFolioStatus(dFolio)) {
                    // Set_Message
                    sMsjErr += ". El Folio " + formatNumerico(dFolio) + " fue Cancelado \n";
                } else {
                    // Set_Message
                    sMsjErr += ". No se realizó la Cancelación del Status con Folio: " + formatNumerico(dFolio) + "\n";
                }
            } else {
                // Set_Message
                sMsjErr += ". No se realizó la Cancelación y Reverso del Folio: " + formatNumerico(dFolio) + "\n";
            }
        }
        
		return bTransfer;
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private synchronized String formatDate2(Date date) {
        return simpleDateFormat2.format(date);
    }

    private synchronized String formatNumerico(Double decimal) {
        return numerFormat.format(decimal);
    } 
}