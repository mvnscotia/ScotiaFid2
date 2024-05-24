/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBFacturasSelloDigital.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.controller;


//Imports
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List; 
import java.util.Map;
import java.util.UUID; 

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import scotiaFid.bean.FactSelloBean;  
import scotiaFid.dao.CCFDI;
import scotiaFid.dao.CHonorarios;
import scotiaFid.util.LogsContext; 

//Class  
@Named("mbFacturaSelloDigital") 
@ViewScoped
public class MBFacturasSelloDigital implements Serializable {
	
    private static final Logger logger = LogManager.getLogger(MBFacturasSelloDigital.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R I A L
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * O B J E T O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    //-----------------------------------------------------------------------------
    private FactSelloBean factSelloBean = new FactSelloBean(); 
    // -----------------------------------------------------------------------------
    private Map<String, String> tipo_participante = new LinkedHashMap<String, String>();
    //-----------------------------------------------------------------------------
    private List<FactSelloBean> facturas = new ArrayList<>(); 
    // -----------------------------------------------------------------------------
    private Map<String, String> mMotivo = new LinkedHashMap<String, String>();
    //-----------------------------------------------------------------------------
    private FactSelloBean selectFacturas;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * F O R M A T O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    //-------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //-------------------------------------------------------------------------
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd"); 
    //-------------------------------------------------------------------------
    private static final NumberFormat numberFormat = new DecimalFormat("###################");
    //-------------------------------------------------------------------------
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,##0.00#####");
    //-------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * A T R I B U T O S   P R I V A D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    //-------------------------------------------------------------------------
    private String mensajeError; 
    //-------------------------------------------------------------------------
    private static final String nombreObjeto =  "\nFuente: scotiafid.controller.MBFacturasSelloDigital";
    
    //CRITERIOS DE CONSULTA
    private Date    dFec_Inicial; 
    //-------------------------------------------------------------------------
    private Date    dFec_Final; 
    //-------------------------------------------------------------------------
    private String  sFideicomiso;
    //-------------------------------------------------------------------------
    private String  sNombre;
    //-------------------------------------------------------------------------
    private String  sTipo_Participante;
    //-------------------------------------------------------------------------
    private String  sNo_Part; 
    //-------------------------------------------------------------------------
	private String  sImporte;
    //-------------------------------------------------------------------------
    private String  sEstatus;
    //-------------------------------------------------------------------------
    private String  sUUID; 
    //-------------------------------------------------------------------------
    private boolean bValidaUUID;
    //-------------------------------------------------------------------------
    private String  sValMotivo; 
    //-------------------------------------------------------------------------
    private String  sUUIDCancelacion;
    //-------------------------------------------------------------------------
    private boolean bUUID_Disable;
    //-------------------------------------------------------------------------
	private String  sImporteEmision;
    //-------------------------------------------------------------------------
    private boolean bConsulta_Disable;  
    //-------------------------------------------------------------------------
    private boolean bCorregir_Disable;
    //-------------------------------------------------------------------------
    private boolean bOpciones_Disable; 
    //-------------------------------------------------------------------------
    private boolean bOpera_Disable; 
    //-------------------------------------------------------------------------
    private String archivoNombre;
    // -----------------------------------------------------------------------------
    private String archivoUbicacion; 
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
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R V I C I O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    CCFDI CCFDI = new CCFDI();
    //-------------------------------------------------------------------------
    CHonorarios cHonorarios = new CHonorarios();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * G E T T E R S   Y   S E T T E R S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public FactSelloBean getFactSelloBean() {
		return factSelloBean;
	}

	public void setFactSelloBean(FactSelloBean factSelloBean) {
		this.factSelloBean = factSelloBean;
	} 

	public Map<String, String> getTipo_participante() {
		return tipo_participante;
	}

	public void setTipo_participante(Map<String, String> tipo_participante) {
		this.tipo_participante = tipo_participante;
	}
	
    public List<FactSelloBean> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FactSelloBean> facturas) {
        this.facturas = facturas;
    }

	public Date getdFec_Inicial() {
		return dFec_Inicial;
	}

	public void setdFec_Inicial(Date dFec_Inicial) {
		this.dFec_Inicial = dFec_Inicial;
	}

	public Date getdFec_Final() {
		return dFec_Final;
	}

	public void setdFec_Final(Date dFec_Final) {
		this.dFec_Final = dFec_Final;
	}

	public String getsNombre() {
		return sNombre;
	}

	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}

	public String getsFideicomiso() {
		return sFideicomiso;
	}

	public void setsFideicomiso(String sFideicomiso) {
		this.sFideicomiso = sFideicomiso;
	}

	public String getsTipo_Participante() {
		return sTipo_Participante;
	}

	public void setsTipo_Participante(String sTipo_Participante) {
		this.sTipo_Participante = sTipo_Participante;
	}
	public String getsNo_Part() {
		return sNo_Part;
	}

	public void setsNo_Part(String sNo_Part) {
		this.sNo_Part = sNo_Part;
	}

	public String getsImporte() {
		return sImporte;
	}

	public void setsImporte(String sImporte) {
		this.sImporte = sImporte;
	}
	
	public String getsEstatus() {
		return sEstatus;
	}

	public void setsEstatus(String sEstatus) {
		this.sEstatus = sEstatus;
	}

	public String getsUUID() {
		return sUUID;
	}

	public void setsUUID(String sUUID) {
		this.sUUID = sUUID;
	} 

	public boolean isbValidaUUID() {
		return bValidaUUID;
	}

	public void setbValidaUUID(boolean bValidaUUID) {
		this.bValidaUUID = bValidaUUID;
	}
	
	public Map<String, String> getmMotivo() {
		return mMotivo;
	}

	public void setmMotivo(Map<String, String> mMotivo) {
		this.mMotivo = mMotivo;
	}

	public String getsValMotivo() {
		return sValMotivo;
	}

	public void setsValMotivo(String sValMotivo) {
		this.sValMotivo = sValMotivo;
	}
    
	public String getsUUIDCancelacion() {
		return sUUIDCancelacion;
	}

	public void setsUUIDCancelacion(String sUUIDCancelacion) {
		this.sUUIDCancelacion = sUUIDCancelacion;
	}

    public boolean isbUUID_Disable() {
		return bUUID_Disable;
	}

	public void setbUUID_Disable(boolean bUUID_Disable) {
		this.bUUID_Disable = bUUID_Disable;
	}
	public String getsImporteEmision() {
		return sImporteEmision;
	}

	public void setsImporteEmision(String sImporteEmision) {
		this.sImporteEmision = sImporteEmision;
	}

	public boolean isbConsulta_Disable() {
		return bConsulta_Disable;
	}

	public void setbConsulta_Disable(boolean bConsulta_Disable) {
		this.bConsulta_Disable = bConsulta_Disable;
	}

	public boolean isbCorregir_Disable() {
		return bCorregir_Disable;
	}

	public void setbCorregir_Disable(boolean bCorregir_Disable) {
		this.bCorregir_Disable = bCorregir_Disable;
	}

	public boolean isbOpciones_Disable() {
		return bOpciones_Disable;
	}

	public void setbOpciones_Disable(boolean bOpciones_Disable) {
		this.bOpciones_Disable = bOpciones_Disable;
	}
  
	public String getArchivoNombre() {
		return archivoNombre;
	}

	public void setArchivoNombre(String archivoNombre) {
		this.archivoNombre = archivoNombre;
	}

	public String getArchivoUbicacion() {
		return archivoUbicacion;
	}

	public void setArchivoUbicacion(String archivoUbicacion) {
		this.archivoUbicacion = archivoUbicacion;
	}

	public File getArchivo() {
		return archivo;
	}

	public void setArchivo(File archivo) {
		this.archivo = archivo;
	}

	public FileOutputStream getArchivoSalida() {
		return archivoSalida;
	}

	public void setArchivoSalida(FileOutputStream archivoSalida) {
		this.archivoSalida = archivoSalida;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public String getArchivoLinea() {
		return archivoLinea;
	}

	public void setArchivoLinea(String archivoLinea) {
		this.archivoLinea = archivoLinea;
	}

	public boolean isbOpera_Disable() {
		return bOpera_Disable;
	}

	public void setbOpera_Disable(boolean bOpera_Disable) {
		this.bOpera_Disable = bOpera_Disable;
	}

	public FactSelloBean getSelectFacturas() {
		return selectFacturas;
	}

	public void setSelectFacturas(FactSelloBean selectFacturas) {
		this.selectFacturas = selectFacturas;
	}

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
   * C O N S T R U C T O R
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBFacturasSelloDigital() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();  
    }
                                           
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * M E T O D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @PostConstruct
    public void init() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
            	//Inicializa variables 
                mensajeError = "Error En Tiempo de Ejecución.\n";                  
                cleanFacturas(); 
                
                //Motivo_Cancelacion_Facturas_CFDI_2022
                 mMotivo = CCFDI.onCFDI_getClaves(95);  
            }

        } catch (IOException | NumberFormatException  Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);
        }
    }

    public void Aceptar_click() {
    	try { 
    		if(dFec_Inicial != null || dFec_Final != null || !sFideicomiso.equals("") || !sNombre.trim().equals("")  
    		|| sTipo_Participante != null || !sNo_Part.trim().equals("") || !sImporte.equals("") || sEstatus != null
    		|| (!sUUID.equals("") && bValidaUUID == true)) {

	    		if (dFec_Inicial != null && dFec_Final != null)  
	    			if(dFec_Inicial.after(dFec_Final)) {    			
	    				// Set_Message
	                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fecha Inicial Inválida..."));
	                    dFec_Inicial = null;
	                    return;	    				
	    			}
	    		
	    		if (dFec_Final != null)  
	    			if(dFec_Inicial == null) {    			
	    				// Set_Message
	                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Fecha Inicial no puede estar vacía..."));
	                    return;	    				
	    			}
	    		
       
	    		facturas = CCFDI.onCCFDI_Consulta(dFec_Inicial, dFec_Final, sFideicomiso, sNombre, sTipo_Participante, 
	    					sNo_Part, sImporte, sEstatus, sUUID);
	    		
	            bOpciones_Disable = false; 
            
    		}else
    		{  
    			// Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe proporcionar al menos un criterio de búsqueda..."));
    		}
			
    	
        } catch (NumberFormatException  Err) { 
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " Aceptar_click()"; 
            logger.error(mensajeError);
        } 
    }
    
    public void cleanFacturas() { 
    	 
        //Clean_Bean
        dFec_Final = null;  
    	dFec_Inicial = null; 
        sFideicomiso = "";
        sNombre = "";
        sNo_Part = "";
        sImporte = "";
        sEstatus = null;
        sTipo_Participante  = null;
        
        sUUID = ""; 
        bValidaUUID = true;
        
        sImporteEmision="";
        sValMotivo = "";
         
        //Botones_de_Opciones:_Corregir,Reexpedir,Emision,Cancelar,...
        bOpciones_Disable = false;
        bConsulta_Disable = false;
        
        //UUID de Motivo de Cancelacion
        bUUID_Disable = false;
         
    	bCorregir_Disable = false;
        
    	facturas = new ArrayList<>();
    	factSelloBean = new FactSelloBean(); 
    }
 
	public void onRowSelect_Facturas(SelectEvent event) { 
			// Get_Factura
			bOpciones_Disable = true;
			this.factSelloBean = (FactSelloBean) event.getObject();

			bOpera_Disable = false; 
	}
	
	public void Corregir() {

		// _Validaciones_Para_Corregir
		// Estatus_Incompleto_Activo
		if (factSelloBean.getFacStatus() != null)
			if (factSelloBean.getFacStatus().equals("INCOMPLETO") || factSelloBean.getFacStatus().equals("ACTIVO")) {

				String RespSP = CCFDI.onCFDI_Corrige(factSelloBean.getFacFecha(), factSelloBean.getFacNumContrato(),
				factSelloBean.getFacFolioFact());
				if (!RespSP.equals("")) {
					FacesContext.getCurrentInstance().addMessage("Fiduciario",new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario","Se produjo un error al corregir la factura ".concat(RespSP)));
					return;
				} else {
					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario","Favor de ver el área de mensaje, para conocer el resultado de la corrección.\n Si esta vacío no hay error."));
					Aceptar_click();
				}

			} else {
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El registro ya fue transferido o cancelado, No se puede modificar."));
			}
	}
	
	public void Valida_CancelaFactura() {
		try {
			sValMotivo = null;
       		sUUIDCancelacion ="";
   			bUUID_Disable = false; 
			
			// _Validaciones_Para_Cancelar
			// Estatus_Vigente
			if (factSelloBean.getFacStatus() != null)
				if (!factSelloBean.getFacStatus().equals("VIGENTE")) {
					FacesContext.getCurrentInstance().addMessage("Fiduciario",new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
									"Esta factura no puede ser Cancelada, el estatus tiene que ser Vigente."));
					return;
				}

			 // _Limite_inferior_Para_Cancelar_Año_Anterior 
			 String[] sFechasLimite = new String[5];
			 sFechasLimite = CCFDI.onCFDI_validaLimiteInferior_FecContable(); 

			 String sFecLimInf = sFechasLimite[0]; 
			 
			java.util.Date dfechaLimit = this.dateParse2(sFecLimInf); 
			java.util.Date dfechaFact = new java.util.Date(factSelloBean.getFacFecha().getTime());
			  
			 int iAnioAct = Integer.parseInt(sFechasLimite[2]);
			 int iAnioAnt = Integer.parseInt(sFechasLimite[3]);
			 int iMesAct = Integer.parseInt(sFechasLimite[4]); 
			  
			// Valida_Año_Cancelación //before           
			if (dfechaFact.before(dfechaLimit)) {
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario",
						"La fecha de la factura " + this.dateFormat(factSelloBean.getFacFecha())
						+ ", No puede ser menor al límite inferior(fecha) para la cancelación de CFDI del año anterior, "
						+ this.dateFormat(dfechaLimit))); 
				return;
			}
			
			if (dfechaFact.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() == iAnioAct) {
				// solo cancela si el año de factura es igual al año del sistema,
				RequestContext.getCurrentInstance().execute("dlgCancelaFact.show();");
				
				// o si la factura es del año anterior al del sistema y el mes del sistema es enero
				// si puede cancelar facturas del mismo año
			}  
			else {
				if ((dfechaFact.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() == iAnioAnt)
				  && iMesAct == 1) {
					// si puede cancelar facturas del año anterior, solo en el mes de enero
					RequestContext.getCurrentInstance().execute("dlgCancelaFact.show();");
				} else {
					FacesContext.getCurrentInstance().addMessage("Fiduciario",
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", " La fecha de la factura es: "
							  + this.dateFormat(factSelloBean.getFacFecha())+", la cancelación de los CFDI se podrá efectuar a más tardar  el 31 de enero del año siguiente a su expedición..."));
					return;
				}
			} 
			
			// Se_Elimina_Autentificacion_Supervisor
			// "SOLICITE LA PRESENCIA DE SU SUPERVISOR PARA AUTORIZAR"));

			// -- VALIDACION DE FACTURAS CANCELADAS.
			if (CCFDI.onCFDI_validaFacturaCancelada(factSelloBean.getFacDomPoblacion())) {
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Fiduciario", "La factura solicitada ya está en un proceso de Cancelación..."));
				return;
			}

		} catch (DateTimeException | NumberFormatException | ParseException exception) {
			mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " CancelaFactura()";
			logger.error(mensajeError);
		}
	}
      
    public void select_Motivo() {
 
        try { 
   	    	bOpera_Disable = false; 

            if (sValMotivo == null || sValMotivo.equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe existir algun motivo de cancelación..."));
                return;
            } else { 

                // Selecciona_Emisora
                for (Map.Entry<String, String> o : mMotivo.entrySet()) {

                    if (sValMotivo.equals(o.getValue())){
                    	sValMotivo = o.getValue();  
                       	if(sValMotivo.contains("01")) { 
                	         	bUUID_Disable = true;
                       		}else {	         	
                           		sUUIDCancelacion ="";
                       			bUUID_Disable = false; 
                       	    	bOpera_Disable = true; 
                       		} 
                    } 
                }
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Emisiones()";
            logger.error(mensajeError);
        }
    }
    
    public void CancelaFactura() { 
     	 
    	if (sValMotivo.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe existir un Motivo de Cancelación..."));
            return;
        }
 
       	if(sValMotivo.contains("01")) { 
	      	 if(sUUIDCancelacion == null || sUUIDCancelacion.equals("")) { 
	         	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe existir el folio de Reexpedición..."));
	         	bUUID_Disable = true;
	         	return;
	         } 
		
       	}else  
    	if(factSelloBean.getFacDomPoblacion().equals("") || factSelloBean.getFacNumContrato() <= 0) { 
         	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se puede cancelar si no se cuenta con UUID y Folio..."));
         	return;
        }
    	 
    	String sCancelaStatus = CCFDI.onCFDI_ValidaCancela(factSelloBean.getFacFecha(), factSelloBean.getFacNumContrato(), factSelloBean.getFacFolioFact());
    	if(!sCancelaStatus.equals("")) {
         	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Esta Cancelación ya esta en Proceso ".concat(sCancelaStatus)));
         	return;
    	}
    	
    	try {
			if(CCFDI.onCFDI_CancelaFactura(
					factSelloBean.getFacFecha(), 
					factSelloBean.getFacNumContrato(), 
					factSelloBean.getFacFolioFact(),
					factSelloBean.getFacFolioTran(), 
					sValMotivo, 
					factSelloBean.getFacDomPoblacion(),
					sUUIDCancelacion, 
					new java.sql.Date(this.dateParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime())) > 0) {
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Factura en Proceso de Cancelación, puede consultar su Estatus en el Menu, Consulta de Cancelaciones..."));

				Aceptar_click();
			    RequestContext.getCurrentInstance().execute("dlgCancelaFact.hide();");
			}else
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No pudo grabar el Registro..."));
		} catch (ParseException e) {
            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " CancelaFactura()";
            logger.error(mensajeError);
		}
     	  
	}
	
	public void Valida_Reexpide() {  
		if(factSelloBean.getFacNumContrato() > 0) {
			 
		//_Validaciones
		//_Estatus_Vigente
    	if(factSelloBean.getFacStatus() != null)
		if (!factSelloBean.getFacStatus().equals("VIGENTE")) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe tener un estatus Vigente..."));
				return;
			}
		
		//_UUID_RElacionado
    	if (factSelloBean.getFacDomPoblacion() == null) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe tener UUDID Relacionado..."));
				return;
			}
		
		//Metodo_De_Pago__PUE_y_PPD
    	if (factSelloBean.getFacStatusCte() != null)
		if (!(factSelloBean.getFacStatusCte().equals("PUE") || factSelloBean.getFacStatusCte().equals("PPD"))) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe ser PUE o PPD."));
				return;
			} 
		  
    	//-- VALIDACION DE FACTURAS DE REEXPEDICION.
		if (CCFDI.onCFDI_validaFacturaReexpedicion(factSelloBean.getFacDomPoblacion())) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
           		 "La factura solicitada ya generó una factura de Egreso/Reexpedición/Cancelación..."));
				return;
		} 
		
        RequestContext.getCurrentInstance().execute("dlgReexpide.show();");
		} 
	}
	
	public void Reexpide() {  
		//Reexpedir
		//Fecha_Fiso_FAC_FOLIO_FACT_ImporteHonorarios_TipoFactura 
		try {
			if(CCFDI.onCFDI_Reexpide(factSelloBean.getFacFecha(), 
									 factSelloBean.getFacNumContrato(),
									 factSelloBean.getFacFolioFact(),
									 factSelloBean.getFacHonorarios(),
									 factSelloBean.getFacStatusCte(),
									 new java.sql.Date(this.dateParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()))) {

				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
			   		 "Reexpedición Generada"));

				Aceptar_click();
		        RequestContext.getCurrentInstance().execute("dlgReexpide.hide();");
		        
			}else 
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
			   		 "Se produjo un error al generar la factura"));
		} catch (ParseException e) {
            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " Reexpide()";
            logger.error(mensajeError);
		}
		
	}
	  
	public void ValidaEgresos() { 
	   	 
		//Verifica si la Factura esta vigente
		if (factSelloBean.getFacStatus() != null && !factSelloBean.getFacStatus().equals("")) 
		if (!factSelloBean.getFacStatus().equals("VIGENTE")) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe estar Vigente..."));
				return;
		}
	
		//Verifica si la Factura tiene un UUID relacionado
		if (factSelloBean.getFacDomPoblacion() == null || factSelloBean.getFacDomPoblacion().equals("")) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe tener un Folio Fiscal asociado..."));
				return;
		}
		
		//Metodo_De_Pago__PUE_y_PPD
		if (factSelloBean.getFacStatusCte() != null && !factSelloBean.getFacStatusCte().equals("")) 
		if (!(factSelloBean.getFacStatusCte().equals("PUE") || factSelloBean.getFacStatusCte().equals("PPD"))) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura debe ser PUE o PPD"));
				return;
		} 

   		//-- VALIDACION DE FACTURAS DE EGRESO. 
		if (CCFDI.onCFDI_validaFacturaEgresos(factSelloBean.getFacDomPoblacion())) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
            		 "La factura solicitada ya generó una factura de Egreso/Reexpedición/Cancelación..."));
				return;
		} 
		
        sImporteEmision="";  
        RequestContext.getCurrentInstance().execute("dlgEmisionCFDI.show();");
	}

	public void Emision() {  
	   	 
		if(ValidaImporteEmision()) {
			try {
				if(CCFDI.onCFDI_Emision(factSelloBean.getFacFecha(), factSelloBean.getFacNumContrato(),factSelloBean.getFacFolioFact(),
						sImporteEmision,new java.sql.Date(this.dateParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()))) {

					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
				    		 "Factura Generada."));
					Aceptar_click();
			        RequestContext.getCurrentInstance().execute("dlgEmisionCFDI.hide();");
				}else {
					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
				    		 "No se puede emitir la factura de egreso.")); 
				}
			} catch (NumberFormatException | ParseException  e) {
	            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " Emision()";
	            logger.error(mensajeError);
			}
		} 
	}
	
	public boolean ValidaImporteEmision() { 
	   	 
		if(sImporteEmision.equals("")) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
           		 "Capture el Importe."));
			return false;
		}
		 
		sImporteEmision = sImporteEmision.replace(",", "");
		if(Double.parseDouble(sImporteEmision) <=0) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
           		 "El Importe debe ser mayor a 0."));
			return false;
		}
		
		double dImporteMenos =0, dImporteHono = 0;
    	if(factSelloBean.getFacStatusCte().equals("PUE")) {
    		dImporteMenos = CCFDI.onCFDI_ValidaImporte(factSelloBean.getFacDomPoblacion(), factSelloBean.getFacNumContrato(),"EGRESO");
    		
    		//If Not ((dImpHono - dImpMenos) - dImpEgreso > 0)
    		if(!((factSelloBean.getFacHonorarios() - dImporteMenos) - Double.parseDouble(sImporteEmision) > 0)) {
    			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
               		 "El importe del egreso debe ser menor que los honorarios..."));
    			return false;
    		}
    	}else {
    		dImporteHono = CCFDI.onCFDI_ValidaImporte(factSelloBean.getFacDomPoblacion(), factSelloBean.getFacNumContrato(),"CP");
    		
    		if(dImporteHono == 0) {
    			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
                 		 "No existen Complementos de Pago..."));
    			return false;
    		}
    		
    		dImporteMenos = CCFDI.onCFDI_ValidaImporte(factSelloBean.getFacDomPoblacion(), factSelloBean.getFacNumContrato(),"EGRESO");

	    	//If Not ((dImpHono - dImpMenos) - dImpEgreso >= 0)
	  		if(!((dImporteHono - dImporteMenos) - Double.parseDouble(sImporteEmision) >= 0)) {
	  			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", 
	             		 "El importe del egreso debe ser menor..."));
	  			return false;
	  		}
    	}
		
		return true;
	} 
		
    public void disableConsulta() {   
    	if(dFec_Inicial != null  || dFec_Final != null || !sFideicomiso.equals("") || !sNombre.equals("") || !sNo_Part.equals("") 
    	 || !sImporte.equals("") || sTipo_Participante != null  || sEstatus != null || !sUUID.equals("")) { 

			if(sEstatus != null && sEstatus.equals("VIGENTE") && (dFec_Inicial == null && dFec_Final == null) 
				&& (sFideicomiso == null || sFideicomiso.equals(""))) { 
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "Se debe seleccionar para estatus Vigente un periodo de Fechas o Fideicomiso..."));
				sEstatus = null;
				return;	    	 
			}
			 
	    	if(sTipo_Participante == null && !sNo_Part.equals("")) { 
	    		FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se debe seleccionar el Tipo de participante..."));
	            sNo_Part = "";  
	    	}
	    	
	        bConsulta_Disable = true; 
    	}else
    		bConsulta_Disable = false; 
    }
    
    public void validaConsulta() {  
    		bConsulta_Disable = false; 
    }
    
    public void validateFiso() { 
    	try { 

        	if (sFideicomiso != null && !sFideicomiso.equals("")) {  
	    		sFideicomiso = sFideicomiso.trim();
		    	if(!isNumeric(sFideicomiso)) {
		    		FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
		            sFideicomiso = "";  
		            return;
		    	}
		    	
		    	if(!cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(sFideicomiso))) { 
	                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe, no está activo o no le pertenece..."));
	                sFideicomiso = "";  
		    	}
	    	}
	    	
	    	disableConsulta();
        } catch (NumberFormatException  e) { 
            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " validateFiso()";
            logger.error(mensajeError);
        }
    }
    
    public void validateNoPart() { 

    	if (!sNo_Part.equals(""))
    	if(!isEntero(sNo_Part)) {
    		FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El No. de Participante debe ser numérico..."));
            sNo_Part = "";    
    	}  
    	
    	disableConsulta();
    }
    
    public void validateImporte() {  

    	if (!sImporte.equals(""))
    	if(!isNumeric(sImporte)) {
    		FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe debe ser un campo numérico..."));
    		sImporte = "";    
    	}   
    	disableConsulta();
    }
  
    public void Deshabilita_Btn() { 
    	bOpera_Disable = false; 
    }
    
    public void validateImporteEmision() {   
 
    	if (sImporteEmision.equals(""))
    		return;
    	
    	sImporteEmision =sImporteEmision.replace(",", ""); 
    	if(!isNumeric(sImporteEmision)) {
    		FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe debe ser un campo numérico"));
    		sImporteEmision = ""; 
    		return;
    	} 
    	
    	Double dImporte = round(Double.parseDouble(sImporteEmision),2);
    	sImporteEmision = NumberFormat.getInstance().format(dImporte);

    	bOpera_Disable = true; 
    }
       
    private static boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
   
    private static boolean isEntero(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
        return bd.doubleValue();
    }
    
	public void validaUUID() {

		if (sUUID.length() != 36) {
			sUUID = "";
			bValidaUUID = false;
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El UUID debe ser de 36 caracteres..."));
		}

		try {
			UUID.fromString(sUUID);
			bValidaUUID = true;

		} catch (IllegalArgumentException exception) {
			bValidaUUID = false;
			sUUID = "";
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "UUID no checa con el patrón..."));
		}

		disableConsulta();
	}
    
	public void validaUUID_Cancelacion() {

		if (sUUIDCancelacion.length() != 36) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Folio de Reexpedición, debe tener 36 caracteres..."));
			sUUIDCancelacion = "";
			bValidaUUID = false;
			return;
		}

		try {
			UUID.fromString(sUUIDCancelacion);
			bValidaUUID = true;

		} catch (IllegalArgumentException exception) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario",new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "UUID no checa con el patrón..."));
			sUUIDCancelacion = "";
			bValidaUUID = false;
			return;
		}

		bOpera_Disable = true;
	}
  
	public void Exportar() {
		try {

			archivoNombre = "FacturaSelloDigital_".concat(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema").toString().replace("/", "_")).concat(".txt");
			archivoNombre = archivoNombre.replace("Á", "A");
			archivoNombre = archivoNombre.replace("É", "E");
			archivoNombre = archivoNombre.replace("Í", "I");
			archivoNombre = archivoNombre.replace("Ó", "O");
			archivoNombre = archivoNombre.replace("Ú", "U");
			archivoUbicacion = System.getProperty("java.io.tmpdir") + File.separator;

			if (GeneraArchivo(archivoUbicacion.concat(archivoNombre))) {
				destinoURL = "/scotiaFid/SArchivoDescarga?".concat(archivoNombre);
				FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
				FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "Se generó correctamente el archivo: " + archivoNombre));
			} else {
				FacesContext.getCurrentInstance().addMessage("Fiduciario",new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", mensajeError));
			}
		} catch (IOException Err) {
			FacesContext.getCurrentInstance().addMessage("Fiduciario",new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", Err.getMessage()));
		}
	}

    private boolean GeneraArchivo(String nombreArchivo) {
        boolean res = false;
        //Preparamos el archivo 
        archivo = new File(nombreArchivo);
        try {
            archivoSalida = new FileOutputStream(archivo);
            archivoLinea = new String();
            
            archivoLinea += "FECHA".concat("\t"); 
            archivoLinea += "FECHA_INT".concat("\t");
            archivoLinea += "FIDEICOMISO".concat("\t");
            archivoLinea += "PARTICIPANTE".concat("\t");
            archivoLinea += "NO_PARTICIPANTE".concat("\t");
            archivoLinea += "NOMBRE".concat("\t");
            archivoLinea += "TIPO_PERSONA".concat("\t");
            archivoLinea += "R.F.C".concat("\t");
            archivoLinea += "MONEDA".concat("\t");
            archivoLinea += "HONORARIOS".concat("\t");
            archivoLinea += "MORATORIOS".concat("\t");
            archivoLinea += "I.V.A".concat("\t");
            archivoLinea += "TASA_I.V.A".concat("\t");
            archivoLinea += "CALLE".concat("\t");
            archivoLinea += "NUM_EXT.".concat("\t");
            archivoLinea += "NUM_INT.".concat("\t");
            archivoLinea += "COLONIA".concat("\t");
            archivoLinea += "FOLIO_SAT".concat("\t");
            archivoLinea += "ESTADO".concat("\t");
            archivoLinea += "PAIS".concat("\t");
            archivoLinea += "C.P.".concat("\t");
            archivoLinea += "CONDICIONES_PAGO".concat("\t");
            archivoLinea += "ESTATUS".concat("\t");
            archivoLinea += "MENSAJE".concat("\t");
            archivoLinea += "FOLIO_TRAN.".concat("\t");
            archivoLinea += "FOLIO_FACT.".concat("\t");
            archivoLinea += "FAC_FOLIO_FACT.".concat("\t");
            archivoLinea += "NACIONALIDAD".concat("\t");
            archivoLinea += "FAC_SISTEMA".concat("\t");
            archivoLinea += "TIPO_FACTURA".concat("\t");
            archivoLinea += "METODO_PAGO".concat("\t");
            archivoLinea += "CVE_REGIMEN_FISCAL".concat("\t");
            archivoLinea += "DESC_REGIMEN_FISCAL".concat("\t");
            archivoLinea = archivoLinea + "\n";
            buffer       = archivoLinea.getBytes();
            archivoSalida.write(buffer);

 		   for(FactSelloBean fac_factSelloBean : facturas) {
                archivoLinea = new String(); 

                archivoLinea += this.dateFormat(fac_factSelloBean.getFacFecha()).concat("\t");
                if(fac_factSelloBean.getFacFechaInt() != null) archivoLinea += this.dateFormat(fac_factSelloBean.getFacFechaInt()).concat("\t"); else archivoLinea.concat("\t");                
                archivoLinea += this.integerFormat(fac_factSelloBean.getFacNumContrato()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacCvePersFid()).concat("\t");
                archivoLinea += this.integerFormat(fac_factSelloBean.getFacNumPersFid()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacNombreRecep()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacCveTipoPer()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacRFC()).concat("\t");
                archivoLinea += this.integerFormat(fac_factSelloBean.getFacNumMoneda()).concat("\t");
                archivoLinea += this.decimalFormateo(fac_factSelloBean.getFacHonorarios()).concat("\t");
                archivoLinea += this.decimalFormateo(fac_factSelloBean.getFacMoratorios()).concat("\t");
                archivoLinea += this.decimalFormateo(fac_factSelloBean.getFacIvaTraladado()).concat("\t");
                archivoLinea += this.decimalFormateo(fac_factSelloBean.getFacTasaImpuesto()).concat("\t");
                archivoLinea += fac_factSelloBean.getFacDomCalle().concat("\t");
                archivoLinea += fac_factSelloBean.getFacDomNumExt().concat("\t");
                archivoLinea += fac_factSelloBean.getFacDomNumInt().concat("\t");
                archivoLinea += fac_factSelloBean.getFacDomColonia().concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacDomPoblacion()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacDomEstado()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacDomPais()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacCodigoPostal()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacCondiPago()).concat("\t");
                archivoLinea += String.valueOf( fac_factSelloBean.getFacStatus()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacMensaje()).concat("\t");
                archivoLinea += this.integerFormat(fac_factSelloBean.getFacFolioTran()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFolio_Fac()).concat("\t");
                archivoLinea += this.integerFormat(fac_factSelloBean.getFacFolioFact()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacNacionalidad()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacSistema()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacStatusCte()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getFacMetPago()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getDif_regimenfiscal()).concat("\t");
                archivoLinea += String.valueOf(fac_factSelloBean.getDesc_Regimen_Fiscal()).concat("\t");

                archivoLinea = archivoLinea + "\n";
                buffer       = archivoLinea.getBytes();
                archivoSalida.write(buffer);
            }
            
            archivoSalida.close();
            res = true;
        } catch (FileNotFoundException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        } catch (ParseException | IOException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        } 
        return res;
    }  
    
    private synchronized String dateFormat(Date date) throws ParseException {
        return simpleDateFormat.format(date);
    } 
    
    private synchronized String integerFormat(Integer integer){
        return numberFormat.format(integer);
    }
    
    private synchronized String decimalFormateo(Double decimal){
        return decimalFormat.format(decimal);
    }

    private synchronized Date dateParse(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
    
    private synchronized Date dateParse2(String date) throws ParseException {
        return simpleDateFormat2.parse(date);
    }
}
