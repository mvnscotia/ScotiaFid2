/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBMonitorCheques.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.controller;


//Imports
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.xml.sax.SAXException;

import scotiaFid.bean.DepositBean;
import scotiaFid.bean.FidCtasBean;
import scotiaFid.bean.MonitorChequesBean;
import scotiaFid.bean.RequestMonitorBean;
import scotiaFid.bean.ResponseMonitorBean;
import scotiaFid.dao.CMonitorMovimientos;
import scotiaFid.service.DepositTranInqByAcctId;
import scotiaFid.util.LogsContext;


//Class
@ManagedBean(name = "mbMonitorMovimientos")
@ViewScoped
public class MBMonitorMovimientos  implements Serializable {
        private static final Logger logger = LogManager.getLogger(MBContabilidad.class);
        private static SimpleDateFormat formatoFecha;  
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
	 private ResponseMonitorBean responseMonitorBean;
	//--------------------------------------------------------------------------
	 private DepositBean depositBean;
	//--------------------------------------------------------------------------
	 
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * O B J E T O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
	 private List<FidCtasBean> cuentas = new ArrayList<>();
	//--------------------------------------------------------------------------
	 private List<DepositBean> depositos = new ArrayList<>();
	//--------------------------------------------------------------------------
	 private List<ResponseMonitorBean> responses = new ArrayList<>();
	//-------------------------------------------------------------------------- 
	 
	/* * * * * * * * * * * * * * * * * * * * ** * * * * * * * * * * * * * * * * 
	 * S E R V I C I O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 CMonitorMovimientos cMonitorMovimientos = new CMonitorMovimientos();
	 DepositTranInqByAcctId depositTranInqByAcctId = new DepositTranInqByAcctId();
	 
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 public MonitorChequesBean getMonitorChequesBean() {
		return monitorChequesBean;
	 }

	 public void setMonitorChequesBean(MonitorChequesBean monitorChequesBean) {
		this.monitorChequesBean = monitorChequesBean;
	 }
	 
	 public List<FidCtasBean> getCuentas() {
		return cuentas;
	 }

	 public void setCuentas(List<FidCtasBean> cuentas) {
		this.cuentas = cuentas;
	 }
	 
	 public List<DepositBean> getDepositos() {
		return depositos;
	 }

	 public void setDepositos(List<DepositBean> depositos) {
		this.depositos = depositos;
	 }

	 public RequestMonitorBean getRequestMonitorBean() {
		return requestMonitorBean;
	 }

	 public void setRequestMonitorBean(RequestMonitorBean requestMonitorBean) {
		this.requestMonitorBean = requestMonitorBean;
	 }
	 
	 public ResponseMonitorBean getResponseMonitorBean() {
		return responseMonitorBean;
	 }

	 public void setResponseMonitorBean(ResponseMonitorBean responseMonitorBean) {
		this.responseMonitorBean = responseMonitorBean;
	 }
	 
	 public List<ResponseMonitorBean> getResponses() {
		return responses;
	 }

	 public void setResponses(List<ResponseMonitorBean> responses) {
		this.responses = responses;
	 }
	 
	 public DepositBean getDepositBean() {
		return depositBean;
	 }

	 public void setDepositBean(DepositBean depositBean) {
		this.depositBean = depositBean;
	 }

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * C O N S T R U C T O R
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
	 public MBMonitorMovimientos() { 
	    FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
             LogsContext.FormatoNormativo();
	 }

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	 @PostConstruct
	 public void init() {
        try {
            //Write_Console
//            out.println("Init Controller...");
            
            //Set_Bean
            this.monitorChequesBean = new MonitorChequesBean();
            
            //Date_Format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            //LocalDate
            LocalDate localDate = LocalDate.now();
            
            //Set_Parameters
            monitorChequesBean.setContrato("11026531");
            monitorChequesBean.setMoneda(1);
            monitorChequesBean.setFecha(localDate.format(dateTimeFormatter));
            
            if(1 == monitorChequesBean.getMoneda()) 
               monitorChequesBean.setNombreMoneda("Moneda Nacional");
            else
               monitorChequesBean.setNombreMoneda("D�lar Americano");
            
        } catch (AbstractMethodError Err) {
            logger.error("Error en function init");//oGeneraLog.onGeneraLog(MBMonitorMovimientos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
        }
    }
	public void getMonitorMovements() throws NumberFormatException, SQLException {
		//Write_Console
//        out.println("Get Monitor Movements...");
        
        //Objects
        requestMonitorBean = new RequestMonitorBean();
        
        //Get_Cuentas
        cuentas = cMonitorMovimientos.onMonitorMovimientos_GetCuentasXContrato(Integer.parseInt(monitorChequesBean.getContrato()), monitorChequesBean.getMoneda());
        
        //Get_Depositos
        Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()); 
        depositos = cMonitorMovimientos.onMonitorMovimientos_GetDepositos(Integer.parseInt(monitorChequesBean.getContrato()), monitorChequesBean.getMoneda(),fechSistema);
        
        //Write_Console
//        out.println("No Count cuentas: ".concat(String.valueOf(cuentas.size())));
        
        //Get_Movements
        for(FidCtasBean fidCtasBean : cuentas) {
        	//Write_Console
//            out.println("Plaza: ".concat(String.valueOf(String.format("%03d", fidCtasBean.getPlaza()))));
            
            //Write_Console
//            out.println("Cuenta: ".concat(fidCtasBean.getCuenta()));
        	
        	//Set_Cuenta
        	requestMonitorBean.setCuenta(String.format("%03d", fidCtasBean.getPlaza()).concat(fidCtasBean.getCuenta()));
        	
        	try {
        		//Get_Movements
				responseMonitorBean = depositTranInqByAcctId.requestTransaction(requestMonitorBean);
				
				//Get_Responses
				responses = responseMonitorBean.getLstResponse();
				
				//Validate_Responses
				if(responses.size() == 0) {
				   //Set_Message
		           FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Sin Movimientos para el nímero de cuenta: ".concat(String.format("%03d", fidCtasBean.getPlaza()).concat(fidCtasBean.getCuenta()))));
		           return;
				}
				
				//Write_Console
				//System.out.println("response.getEstatus() : " + responseMonitorBean.getEstatus());
				   
				//Response
				if("0".equals(responseMonitorBean.getEstatus())) {
				      for(ResponseMonitorBean response : responses) {
					      //System.out.println("response.getMonto() : " + response.getMonto());
					      //System.out.println("response.getSaldo() : " + response.getSaldo());
					      //System.out.println("response.getTipoTransaccion() : " + response.getTipoTransaccion());
					      //System.out.println("response.getDescripcion() : " + response.getDescripcion());
					      //System.out.println("response.getConfirmacion() : " + response.getConfirmacion());
					      //System.out.println("response.getReferencia() : " + response.getReferencia());
					      
					      //Write_Console
				          //out.println("NO DEPOSITOS: ".concat(String.valueOf(depositos.size())));
					      
					      for(DepositBean depositBean : depositos) {
					    	//  System.out.println("depositBean.getConcepto() : " + depositBean.getConcepto());
					    	  
					    	  if(response.getConfirmacion().equals(depositBean.getConcepto())) { 
					    		 response.setEstatus("/vista/recursos/img/png/green_circle.png");
					    		 response.setAplicado("SI");
					    	  }
					      }
				      }
				   } else {
					  //Print_Console
				 	  //System.out.println(" INTERFASE CONSULTA DE CHEQUES : ".concat(requestMonitorBean.getCuenta()).concat(" : "));
				   }
			} catch (ParserConfigurationException | SAXException | SQLException | IOException e) {
                          logger.error("Error en funcion getMonitorMovements");  //oGeneraLog.onGeneraLog(MBMonitorMovimientos.class, "0D", "ERROR", "20", "40", e.getMessage(), "10", "20", "30");
			}
        	
        	//Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Consulta Terminada."));
            return;
        } 
	}
    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {  
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }
	
	public void onRowSelect(SelectEvent event) throws NumberFormatException, SQLException {
      //Write_Console
//      out.println("On Row Select...");
      
      //Get_Movimiento
      this.responseMonitorBean = (ResponseMonitorBean) event.getObject();
      
      //Validate_Aplicado
      if("SI".equals(responseMonitorBean.getAplicado()))
    	 //Show_Window_Aplicado
         RequestContext.getCurrentInstance().execute("dlgAplicado.show();"); 
      else  
         //Show_Window_Copiar
         RequestContext.getCurrentInstance().execute("dlgCopiar.show();");
    }
	
    public void cleanMonitorMovements() {
    	//Write_Console
//        out.println("Cleaning...");

        //Clean_List_Responses
        responses.clear();
    }
}
