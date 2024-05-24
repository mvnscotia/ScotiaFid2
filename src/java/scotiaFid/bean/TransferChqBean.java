/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : FDTransfer_Chq.java
 * DESCRIPCION : Tabla FDTRANSFER, almacenamiento para Transferencia de Chequera
 *             : Almacena datos para que se realice la transferencia de pagos, despues de la aplicacion de Checker
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;
import java.util.Date;

//Class
public class TransferChqBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	// Tabla FDTRANSFER
	private int 	Folio;
	private double 	Folio_Op;
	private Date 	Fecha_Mov;
	private String 	Cta_Origen;
	private String 	Cta_Destino;
	private int 	Moneda;
	private double 	Importe;
	private String 	Referencia;		//Id.-de_Referencia_de_envió
	private String 	Confirmacion;	//Id._que_confirma_SW
	private Date 	Fecha_Hora;     //Fecha_en_que_se_Aplico_SW
	//Msg_Err       //Se almacena en ResponseTraspasoBean
	 
	//Getters y Setters
	public int getFolio() {
		return Folio;
	}
	public void setFolio(int folio) {
		Folio = folio;
	}
	
	public double getFolio_Op() {
		return Folio_Op;
	}
	public void setFolio_Op(double folio_Op) {
		Folio_Op = folio_Op;
	}
	
	public Date getFecha_Mov() {
		return Fecha_Mov;
	}
	public void setFecha_Mov(Date fecha_Mov) {
		Fecha_Mov = fecha_Mov;
	}
	
	public String getCta_Origen() {
		return Cta_Origen;
	}
	public void setCta_Origen(String cta_Origen) {
		Cta_Origen = cta_Origen;
	}
	
	public String getCta_Destino() {
		return Cta_Destino;
	}
	public void setCta_Destino(String cta_Destino) {
		Cta_Destino = cta_Destino;
	}
	
	public int getMoneda() {
		return Moneda;
	}
	public void setMoneda(int moneda) {
		Moneda = moneda;
	}
	
	public double getImporte() {
		return Importe;
	}
	
	public void setImporte(double importe) {
		Importe = importe;
	}
	
	public String getReferencia() {
		return Referencia;
	}
	
	public void setReferencia(String referencia) {
		Referencia = referencia;
	}
	
	public String getConfirmacion() {
		return Confirmacion;
	}
	
	public void setConfirmacion(String confirmacion) {
		Confirmacion = confirmacion;
	}
	
	public Date getFecha_Hora() {
		return Fecha_Hora;
	}
	
	public void setFecha_Hora(Date fecha_Hora) {
		Fecha_Hora = fecha_Hora;
	} 
}
