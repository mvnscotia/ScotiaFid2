/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBCierreContable.java
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped; 
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

import scotiaFid.bean.CatErrWebServiceBean;
import scotiaFid.bean.FidCtasBean;
import scotiaFid.bean.MonitorChequesBean;
import scotiaFid.bean.RequestMonitorBean;
import scotiaFid.bean.ResponseMonitorBean;
import scotiaFid.dao.CCierreContable;
import scotiaFid.service.DepositTranInq;
import scotiaFid.util.LogsContext;

//Class
@Named("mbCierreContable")
@ViewScoped
public class MBCierreContable implements Serializable {
	
    private static final Logger logger = LogManager.getLogger(MBCierreContable.class);
	
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 private static final long serialVersionUID = 1L;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * I N Y E C C I O N   D E   B E A N S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 private MonitorChequesBean monitorChequesBean;
	//--------------------------------------------------------------------------
	 private RequestMonitorBean requestMonitorBean;
	//--------------------------------------------------------------------------
	 
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * O B J E T O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 private List<FidCtasBean> cuentas = new ArrayList<>();
	//--------------------------------------------------------------------------
	 private List<ResponseMonitorBean> responses = new ArrayList<>();
	//--------------------------------------------------------------------------
	 private HashMap<String, String> errores = new HashMap<String, String>();
	//--------------------------------------------------------------------------
	 private List<CatErrWebServiceBean> responsesWebError = new ArrayList<>();
	//--------------------------------------------------------------------------
	 private String mensajeError;
	// -----------------------------------------------------------------------------
	private String nombreObjeto;

        private String archivoNombre;
        // -----------------------------------------------------------------------------
        private String archivoUbicacion;
        // -----------------------------------------------------------------------------
        private String destinoURL;
        // -----------------------------------------------------------------------------
        private File archivo;
        // -----------------------------------------------------------------------------
        private byte[] buffer;
        // -----------------------------------------------------------------------------
        private String archivoLinea = new String();
        // -----------------------------------------------------------------------------
        private FileOutputStream archivoSalida;
        // -----------------------------------------------------------------------------
	/* * * * * * * * * * * * * * * * * * * * ** * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 CCierreContable cCierreContable = new CCierreContable();
	 DepositTranInq depositTranInq = new DepositTranInq();
	 
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 public MonitorChequesBean getMonitorChequesBean() {
		 return monitorChequesBean;
	 }

	 public void setMonitorChequesBean(MonitorChequesBean monitorChequesBean) {
		 this.monitorChequesBean = monitorChequesBean;
	 }

	 public RequestMonitorBean getRequestMonitorBean() {
		 return requestMonitorBean;
	 }

	 public void setRequestMonitorBean(RequestMonitorBean requestMonitorBean) {
		 this.requestMonitorBean = requestMonitorBean;
	 }

	 public List<FidCtasBean> getCuentas() {
		 return cuentas;
	 }

	 public void setCuentas(List<FidCtasBean> cuentas) {
		 this.cuentas = cuentas;
	 }

	 public List<ResponseMonitorBean> getResponses() {
		 return responses;
	 }

	 public void setResponses(List<ResponseMonitorBean> responses) {
		 this.responses = responses;
	 }

	 public HashMap<String, String> getErrores() {
		 return errores;
	 }

	 public void setErrores(HashMap<String, String> errores) {
		 this.errores = errores;
	 }

	 public List<CatErrWebServiceBean> getResponsesWebError() {
		 return responsesWebError;
	 }

	 public void setResponsesWebError(List<CatErrWebServiceBean> responsesWebError) {
		 this.responsesWebError = responsesWebError;
	 }

		public String getMensajeError() {
			return mensajeError;
		}

		public void setMensajeError(String mensajeError) {
			this.mensajeError = mensajeError;
		}
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * C O N S T R U C T O R
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
	 public MBCierreContable() { 
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
                mensajeError = "Error En Tiempo de Ejecución.\n";
                nombreObjeto = "\nFuente: scotiafid.controller.mbCierreContable.";

		      //Set_Bean
	          this.monitorChequesBean = new MonitorChequesBean();
		         
		      //Date_Format
		      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		            
		      //LocalDate
		      LocalDate localDate = LocalDate.now();
		      
		      //Set_Parameters
	          monitorChequesBean.setFecha(localDate.format(dateTimeFormatter));
           }
	    } catch (IOException e) { 
	    	mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " onPostConstruct()";
	    	logger.error(e.getMessage() + "onPostConstruct()"); 
	    } 
    }
	 
	 public void getMonitorMovementsXAtencion() throws Exception {

		 try {
		 //Objects
		 requestMonitorBean = new RequestMonitorBean();

		 //Get_Catalogo_Errores
		 errores = cCierreContable.onMonitorRecepciones_GetCatalogoErrores();

		 //Get_Cuentas
		 cuentas = cCierreContable.onMonitorRecepciones_GetCuentasXAtencion(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

			// Get_Movements
			responsesWebError.clear();
			depositTranInq.clearResponses();
			for (FidCtasBean fidCtasBean : cuentas) {

			 //Set_Cuenta
			 requestMonitorBean.setCuenta(fidCtasBean.getCuenta());
			  
				 //Instance_Bean_Cat_Err_WebService
				 CatErrWebServiceBean catErrWebServiceBean = new CatErrWebServiceBean();

				 //Get_Movements
				 ResponseMonitorBean responseMonitorBean = depositTranInq.requestTransactionXCuenta(requestMonitorBean, String.valueOf(fidCtasBean.getContrato()), fidCtasBean.getDescripcion());
                                 
                     if (("Servicio Error".equals(depositTranInq.getMensajeError())))
                     {
                         FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error en Servicio de movimientos del Día Intente Mas Tarde y/o Reporte a Soporte Técnico."));
                         return;
                     }
				 //Get_Responses
				 
                                 responses = responseMonitorBean.getLstResponse();
				  
				 //Validate_Code 
				 if(!"0".equals(responseMonitorBean.getCodigo())) {
					 //Set_Descripcion_Error
					 catErrWebServiceBean.setDescripcion("EL NÚMERO DE CUENTA : ".concat(fidCtasBean.getCuenta()).concat(" : ").concat(errores.get(responseMonitorBean.getCodigo()).toUpperCase().replace("Ã³", "Ó")));

					 //Set_List
					 responsesWebError.add(catErrWebServiceBean);
				 }       
		 }
		 
		 }catch (NumberFormatException | SQLException  e) { 
			 mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " getMonitorMovementsXAtencion()";
		 }
		 //Set_Message
		 FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Consulta Terminada."));
	 }
	 
	 public void cleanMonitorMovements() {

		 if(!responses.isEmpty()) { 		  
 		     //Clean_List_Responses
 		     responses.clear();

 		     //Clean_List_Responses_Error
 		     responsesWebError.clear();
          }
     }
	    public void downloadValue() {
	        try { 

	            archivoNombre = "Movimientos_Recepcion_".concat(monitorChequesBean.getFecha().replace("/", "_")).concat(".txt");
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
	        try {
	            archivoSalida = new FileOutputStream(archivo);
              archivoLinea = "                                          RECEPCIONES PENDIENTES DE APLICAR                                  FECHA: ".concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).concat("\n").concat("\n");
                    
	            buffer       = archivoLinea.getBytes();
	            archivoSalida.write(buffer);
                    
	            archivoLinea = new String();	           
	            archivoLinea += "  FIDEICOMISO    ";
	            archivoLinea += "   CUENTA    ";
	            archivoLinea += "      MONEDA    ";
	            archivoLinea += "          REFERENCIA           ";
	            archivoLinea += "          DESCRIPCION          ";
	            archivoLinea += "      IMPORTE       "; 
	            archivoLinea = archivoLinea + "\n";
	            buffer       = archivoLinea.getBytes();
	            archivoSalida.write(buffer);

	            archivoLinea = new String();
	            archivoLinea += "=============== ";
	            archivoLinea += "============ ";
	            archivoLinea += "=============== ";
	            archivoLinea += "============================== ";
	            archivoLinea += "============================== ";
	            archivoLinea += "=================== "; 
	            archivoLinea += "\n";
	            buffer       = archivoLinea.getBytes();
	            archivoSalida.write(buffer);

                    for(ResponseMonitorBean responseMonitorBean : responses) { 
	                archivoLinea = new String(); 
					archivoLinea += String.format("%-16s", String.format("%011d", Integer.parseInt(responseMonitorBean.getContrato()))); 
					archivoLinea += String.format("%-13s",responseMonitorBean.getCuenta()); 
					archivoLinea += String.format("%-16s",responseMonitorBean.getMoneda()); 
					archivoLinea += String.format("%-31s",String.format("%019d", Integer.parseInt(responseMonitorBean.getReferencia()))); 
					archivoLinea += String.format("%-29s",responseMonitorBean.getDescripcion()); 
					archivoLinea += String.format("%20s",responseMonitorBean.getMontoFormat()); 

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
}