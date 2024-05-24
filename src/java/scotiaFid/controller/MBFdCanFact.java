/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     :MBFdCanFact.java
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.FactSelloBean;
import scotiaFid.bean.FdCanFactBean;
import scotiaFid.dao.CCFDI;
import scotiaFid.dao.CHonorarios;
import scotiaFid.util.LogsContext; 

@Named("mbFdCanFact")
@ViewScoped
public class MBFdCanFact implements Serializable {
	
    private static final Logger logger = LogManager.getLogger(MBFdCanFact.class);

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	private static final long serialVersionUID = 1L;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * A T R I B U T O S   P R I V A D O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //-------------------------------------------------------------------------
    private String mensajeError;
    //-------------------------------------------------------------------------
	 private Date fechaInicial;
	 //----------------------------------------------------------------------------
	 private Date fechaFinal;
	 //----------------------------------------------------------------------------
	 private String tipoFactura;
	 //----------------------------------------------------------------------------
	 private String fideicomiso;
	 //----------------------------------------------------------------------------
	 private String tipoParticipante;
	 //----------------------------------------------------------------------------
	 private String estatus;
	 //----------------------------------------------------------------------------
	 private String noParticipante;
	 //----------------------------------------------------------------------------
	 private String uuid;
	 //----------------------------------------------------------------------------
	 private boolean bValidaUUID;
	 //----------------------------------------------------------------------------
	 private String archivoNombre;
	 //----------------------------------------------------------------------------
	 private String archivoUbicacion; 
	 //----------------------------------------------------------------------------
	 private String destinoURL;
	 //----------------------------------------------------------------------------
	 private File archivo;
	 //----------------------------------------------------------------------------
	 private FileOutputStream archivoSalida;
	 //----------------------------------------------------------------------------
	 private byte[] buffer;
	 //----------------------------------------------------------------------------
	  private String archivoLinea = new String();
	 // -----------------------------------------------------------------------------
	  private boolean bConsulta_Disable;  
	 //----------------------------------------------------------------------------

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * I N Y E C C I O N   D E   B E A N S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	private FactSelloBean factSelloBean;
	//----------------------------------------------------------------------------
	private FdCanFactBean fdCanFactBean;
	//----------------------------------------------------------------------------

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R V I C E S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
	private CCFDI CCFDI = new CCFDI();
    // -----------------------------------------------------------------------------
    CHonorarios cHonorarios = new CHonorarios();
	//----------------------------------------------------------------------------

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	private FacesMessage mensaje;
	//----------------------------------------------------------------------------
	private List<FactSelloBean> facturas = new ArrayList<>();
	//----------------------------------------------------------------------------
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public FactSelloBean getFactSelloBean() {
		return factSelloBean;
	}

	public void setFactSelloBean(FactSelloBean factSelloBean) {
		this.factSelloBean = factSelloBean;
	}

	public FdCanFactBean getFdCanFactBean() {
		return fdCanFactBean;
	}

	public void setFdCanFactBean(FdCanFactBean fdCanFactBean) {
		this.fdCanFactBean = fdCanFactBean;
	}

	public FacesMessage getMensaje() {
		return mensaje;
	}

	public void setMensaje(FacesMessage mensaje) {
		this.mensaje = mensaje;
	}

	public List<FactSelloBean> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<FactSelloBean> facturas) {
		this.facturas = facturas;
	}
	
	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(String tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getFideicomiso() {
		return fideicomiso;
	}

	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	public String getTipoParticipante() {
		return tipoParticipante;
	}

	public void setTipoParticipante(String tipoParticipante) {
		this.tipoParticipante = tipoParticipante;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getNoParticipante() {
		return noParticipante;
	}

	public void setNoParticipante(String noParticipante) {
		this.noParticipante = noParticipante;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean isbValidaUUID() {
		return bValidaUUID;
	}

	public void setbValidaUUID(boolean bValidaUUID) {
		this.bValidaUUID = bValidaUUID;
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

	public String getDestinoURL() {
		return destinoURL;
	}

	public void setDestinoURL(String destinoURL) {
		this.destinoURL = destinoURL;
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

	public boolean isbConsulta_Disable() {
		return bConsulta_Disable;
	}

	public void setbConsulta_Disable(boolean bConsulta_Disable) {
		this.bConsulta_Disable = bConsulta_Disable;
	}

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * C O N S T R U C T O R
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
	 public MBFdCanFact() { 
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
	 }
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	@PostConstruct
	public void init() throws SQLException { 
		try {
	        if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
	            	FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
	        } else {
			   //Clean_Facturas
			   facturas.clear();

	        } 
        } catch (IOException | NumberFormatException  Err) {
            logger.error("Descripción: " + Err.getMessage());
        }
	}  
	
	public void consultar() throws SQLException, ParseException {
	  //Format_Date
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      
      //Validate_All
      if(this.getFechaInicial() == null && this.getFechaFinal() == null && this.getTipoFactura() == null && "".equals(this.getFideicomiso()) && this.getTipoParticipante() == null && this.getEstatus() == null && "".equals(this.getNoParticipante()) && "".equals(this.getUuid())) {
    	  FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe proporcionar al menos un criterio de búsqueda..."));
          return; 
      } 
       
      
      //Validate_Fecha_Inicial_and_Final
    	  if(this.getFechaInicial() != null || this.getFechaFinal() != null) {
	    	 //Validate_Fecha_Inicial
	         if(this.getFechaInicial() == null) {
	        	 FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Fecha Inicial no puede estar vacío..."));
	    	     return; 
	         }
	         
	    	 //Validate_Fecha_Final
		     if(getFechaFinal() == null) {
		        fechaFinal = fechaInicial; 
		     } 
	    	  
	    	 //Validate_Fecha_Inicial > Fecha_Final
		     if(Long.parseLong(simpleDateFormat.format(this.getFechaInicial()).replace("-", "")) > Long.parseLong(simpleDateFormat.format(this.getFechaFinal()).replace("-", ""))) {
		        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Fecha Inicial no puede ser mayor al campo Fecha Final..."));
		        return; 
		     }
	      }
      
      //Call_Facturas_Canceladas
      if(this.getFechaInicial() == null && this.getFechaFinal() == null) {
    	 facturas = CCFDI.onFactSello_GetFacturasCanceladas("","",this.getTipoFactura(),this.getFideicomiso(),this.getTipoParticipante(),this.getEstatus(),this.getNoParticipante(),this.getUuid());
      } else {	  
    	  facturas = CCFDI.onFactSello_GetFacturasCanceladas(simpleDateFormat.format(this.getFechaInicial()),simpleDateFormat.format(this.getFechaFinal()),this.getTipoFactura(),this.getFideicomiso(),this.getTipoParticipante(),this.getEstatus(),this.getNoParticipante(),this.getUuid());
      }
	}
	
	public void limpiar() throws SQLException { 
       //Clean_Components
	   facturas.clear();
	   this.setFechaInicial(null);
	   this.setFechaFinal(null);
	   this.setTipoFactura("");
	   this.setFideicomiso("");
	   this.setTipoParticipante("");
	   this.setEstatus("");
	   this.setNoParticipante("");
	   this.setUuid("");
	   bValidaUUID = true;
	   bConsulta_Disable = false;   
	}
	 
    public void Exportar() {
        try { 

            archivoNombre = "Facturas_Proceso_Cancelacion_".concat(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema").toString().replace("/", "_")).concat(".txt");
            archivoNombre = archivoNombre.replace("Á", "A");
            archivoNombre = archivoNombre.replace("É", "E");
            archivoNombre = archivoNombre.replace("Í", "I");
            archivoNombre = archivoNombre.replace("Ó", "O");
            archivoNombre = archivoNombre.replace("Ú", "U");
            archivoUbicacion = System.getProperty("java.io.tmpdir")+File.separator;

            if (GeneraArchivo(archivoUbicacion.concat(archivoNombre))) {
                destinoURL = "/scotiaFid/SArchivoDescarga?".concat(archivoNombre);
                FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se generó correctamente el archivo: " + archivoNombre));
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", mensajeError));
            }
        } catch (IOException Err) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", Err.getMessage()));
        }
    }

    private boolean GeneraArchivo(String nombreArchivo) {
        boolean res = false;
        //Preparamos el archivo 
        archivo = new File(nombreArchivo);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        try {

    		// Get_Report
    		//CCFDI.onFactSello_GetReport(facturas);

            archivoSalida = new FileOutputStream(archivo);
            archivoLinea = new String();
            
            archivoLinea += "FAC_SISTEMA".concat("\t");  
            archivoLinea += "FAC_FECHA".concat("\t");
            archivoLinea += "FAC_NUM_CONTRATO".concat("\t");
            archivoLinea += "FAC_FOLIO_FACT".concat("\t");
            archivoLinea += "FAC_NUM_MONEDA".concat("\t");
            archivoLinea += "FAC_CVE_PERS_FID".concat("\t");
            archivoLinea += "FAC_NUM_PERS_FID".concat("\t");
            archivoLinea += "FAC_CONDI_PAGO".concat("\t");
            archivoLinea += "FAC_FOLIO_TRAN".concat("\t");
            archivoLinea += "FAC_HONORARIOS".concat("\t");
            archivoLinea += "FAC_MORATORIOS".concat("\t");
            archivoLinea += "FAC_IVA_TRASLADADO".concat("\t");
            archivoLinea += "FAC_STATUS".concat("\t");
            archivoLinea += "FAC_STATUS_CTE".concat("\t");
            archivoLinea += "FAC_NACIONALIDAD".concat("\t");
            archivoLinea += "FAC_CVE_TIPO_PER".concat("\t");
            archivoLinea += "FAC_RFC".concat("\t");
            archivoLinea += "FAC_RFC_GENERICO".concat("\t");
            archivoLinea += "FAC_NOMBRE_RECEP".concat("\t");
            archivoLinea += "FAC_DOM_CALLE".concat("\t");
            archivoLinea += "FAC_DOM_NUM_EXT".concat("\t");
            archivoLinea += "FAC_DOM_NUM_INT".concat("\t");
            archivoLinea += "FAC_DOM_COLONIA".concat("\t");
            archivoLinea += "FAC_DOM_POBLACION".concat("\t");
            archivoLinea += "FAC_DOM_REFERENCIA".concat("\t");
            archivoLinea += "FAC_DOM_ESTADO".concat("\t");
            archivoLinea += "FAC_DOM_PAIS".concat("\t");
            archivoLinea += "FAC_CODIGO_POSTAL".concat("\t");
            archivoLinea += "FAC_RECEP_LOCALIDAD".concat("\t");
            archivoLinea += "FAC_FECHA_INT".concat("\t");
            archivoLinea += "FAC_TASA_IMPUESTO".concat("\t");
            archivoLinea += "FAC_MET_PAGO".concat("\t");
            archivoLinea += "FAC_MENSAJE".concat("\t");
            archivoLinea += "FAC_UUID_RELACIONADO".concat("\t");
            archivoLinea += "FID_FOLIO_TRAN".concat("\t");
            archivoLinea += "FCR_FOLIO_CANCELA".concat("\t");
            archivoLinea += "FCR_FEC_SOL".concat("\t");
            archivoLinea += "FCR_FEC_CANCELA".concat("\t");
            archivoLinea += "FCR_HORA_CANCELA".concat("\t");
            archivoLinea += "FCR_FOLIO_ACUSE".concat("\t");
            archivoLinea += "FID_FOLIO_FACT".concat("\t");
            archivoLinea += "FCR_MOTIVO_CANCELA".concat("\n");
            buffer       = archivoLinea.getBytes();
            archivoSalida.write(buffer);
     		
            for(FactSelloBean factSelloBean : facturas) { 
                archivoLinea = new String();
                
                archivoLinea += factSelloBean.getFacSistema().concat("\t");
                archivoLinea += simpleDateFormat.format(factSelloBean.getFacFecha()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacNumContrato()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacFolioFact()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacNumMoneda()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacCvePersFid()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacNumPersFid()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacCondiPago()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacFolioTran()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacHonorarios()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacMoratorios()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacIvaTraladado()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacStatus()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacStatusCte()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacNacionalidad()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacCveTipoPer()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacRFC()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacRFCGenerico()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacNombreRecep()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomCalle()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomNumExt()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomNumInt()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomColonia()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomPoblacion()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomReferencia()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomEstado()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacDomPais()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacCodigoPostal()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacRecepLocalidad()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacFechaInt()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacTasaImpuesto()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacMetPago()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacMensaje()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFacUUIDRelacionado()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFidFolioTran()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrFolioCancela()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrFecSol()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrFecCancela()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrHoraCancela()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrFolioAcuse()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFidFolioFact()).concat("\t");
                archivoLinea += String.valueOf(factSelloBean.getFdCanFactBean().getFcrMotivoCancela()).concat("\t");            
                archivoLinea = archivoLinea + "\n";
                buffer       = archivoLinea.getBytes();
                archivoSalida.write(buffer);
            }
            
            archivoSalida.close();
            res = true;
        } catch (FileNotFoundException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        } catch (IOException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        } 
        return res;
    }  
    
    public void validaUUID() {

	     //Validate_UUID 
    	 if(uuid.length() != 36) {
          	uuid = "";
         	bValidaUUID = false;
         	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El UUID debe ser de 36 caracteres..."));
         }
    	 
    	try{
    	    UUID.fromString(uuid);
    	    bValidaUUID = true;
    	    
    	} catch (IllegalArgumentException exception){
         	uuid = "";
	        bValidaUUID = false;
	       	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "UUID no checa con el patrón..."));
    	}  
    	
    	disableConsulta();
    }
    
	public void validaFiso() throws SQLException {
		// Validate_Fideicomiso
		if (this.getFideicomiso() != null) {
				if ("".equals(this.getFideicomiso())) {
					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Fideicomiso no puede estar vacío..."));
				}else{
	
				try {
					Integer.parseInt(this.getFideicomiso());
	
			    	if(!cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(this.getFideicomiso()))) {  
			    		this.setFideicomiso("");
			            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
			    	}
			    	
				} catch (NumberFormatException e) {
					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
					this.setFideicomiso("");
				}
			}
		}
		  
    	disableConsulta();
	}

    public void validaConsulta() {  
    		bConsulta_Disable = false; 
    } 
    
    public void disableConsulta() {   

		bConsulta_Disable = false;   
		
    	if(this.getFechaInicial() != null || this.getFechaFinal() != null || (this.getTipoFactura() != null && !this.getTipoFactura().equals(""))
    	|| (this.getFideicomiso() != null && !this.getFideicomiso().equals("")) || (this.getTipoParticipante() != null  && !this.getTipoParticipante().equals("")) 
    	|| (this.getEstatus() != null  && !this.getEstatus().equals("")) || (this.getNoParticipante() != null && !this.getNoParticipante().equals("")) 
    	|| (this.getUuid()!= null && !this.getUuid().equals("")))  {  
	    		bConsulta_Disable = true; 
	    		
				if(this.getEstatus() != null && this.getEstatus().equals("VIGENTE") && (this.getFechaInicial() == null && this.getFechaFinal() == null) 
			  && (this.getFideicomiso() == null || this.getFideicomiso().equals(""))) { 
					FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,"Fiduciario", "Se debe seleccionar para estatus Vigente un periodo de Fechas o Fideicomiso..."));
					this.setEstatus(null);
					return;	    	 
				}
					 
			    if(this.getTipoParticipante() == null && (this.getNoParticipante() != null && !this.getNoParticipante().equals(""))) { 
			    	FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se debe seleccionar el Tipo de participante..."));
			    	this.setNoParticipante("");  
			    }
	    }
    }
}
