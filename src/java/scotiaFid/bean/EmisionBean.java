/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * TIPO        : Class
 * DESCRIPCION : Tabla EMISION
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class EmisionBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	private int 	TipoMercado;
	private int 	NumInstrume;
	private int 	NumSecEmis;
	private int     Cupon;
	private int 	NumMoneda;
	private double 	ValNominal;
	private int 	FondBanco;
	private String 	Status;

	// Getters_Setters
	public int getTipoMercado() {
		return TipoMercado;
	}

	public void setTipoMercado(int tipoMercado) {
		TipoMercado = tipoMercado;
	}

	public int getNumInstrume() {
		return NumInstrume;
	}

	public void setNumInstrume(int numInstrume) {
		NumInstrume = numInstrume;
	}

	public int getNumSecEmis() {
		return NumSecEmis;
	}

	public void setNumSecEmis(int numSecEmis) {
		NumSecEmis = numSecEmis;
	}

	public int getCupon() {
		return Cupon;
	}

	public void setCupon(int cupon) {
		Cupon = cupon;
	}

	public int getNumMoneda() {
		return NumMoneda;
	}

	public void setNumMoneda(int numMoneda) {
		NumMoneda = numMoneda;
	}

	public double getValNominal() {
		return ValNominal;
	}

	public void setValNominal(double valNominal) {
		ValNominal = valNominal;
	}

	public int getFondBanco() {
		return FondBanco;
	}

	public void setFondBanco(int fondBanco) {
		FondBanco = fondBanco;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
