/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : FdCanFactBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class FdCanFactBean implements Serializable {
	//Serial
	private static final long serialVersionUID = 1L;
    
	//Members
	private Date fidFecha;
	private int fidNumContrato;
	private String fidFolioFact;
	private int fidFolioTran;
	private String fcrStatus;
	private String fcrMotivoCancela;
	private String fcrFolioCancela;
	private String fcrFecSol;
	private String fcrFecCancela;
	private String fcrHoraCancela;
	private String fcrFolioAcuse;
	private String fcrErrores;
	
	//Constructor
	public FdCanFactBean() {}
	
	//Getters_Setters
	public Date getFidFecha() {
		return fidFecha;
	}

	public void setFidFecha(Date fidFecha) {
		this.fidFecha = fidFecha;
	}

	public int getFidNumContrato() {
		return fidNumContrato;
	}

	public void setFidNumContrato(int fidNumContrato) {
		this.fidNumContrato = fidNumContrato;
	}

	public String getFidFolioFact() {
		return fidFolioFact;
	}

	public void setFidFolioFact(String fidFolioFact) {
		this.fidFolioFact = fidFolioFact;
	}

	public int getFidFolioTran() {
		return fidFolioTran;
	}

	public void setFidFolioTran(int fidFolioTran) {
		this.fidFolioTran = fidFolioTran;
	}

	public String getFcrStatus() {
		return fcrStatus;
	}

	public void setFcrStatus(String fcrStatus) {
		this.fcrStatus = fcrStatus;
	}

	public String getFcrMotivoCancela() {
		return fcrMotivoCancela;
	}

	public void setFcrMotivoCancela(String fcrMotivoCancela) {
		this.fcrMotivoCancela = fcrMotivoCancela;
	}

	public String getFcrFolioCancela() {
		return fcrFolioCancela;
	}

	public void setFcrFolioCancela(String fcrFolioCancela) {
		this.fcrFolioCancela = fcrFolioCancela;
	}

	public String getFcrFecSol() {
		return fcrFecSol;
	}

	public void setFcrFecSol(String fcrFecSol) {
		this.fcrFecSol = fcrFecSol;
	}

	public String getFcrFecCancela() {
		return fcrFecCancela;
	}

	public void setFcrFecCancela(String fcrFecCancela) {
		this.fcrFecCancela = fcrFecCancela;
	}

	public String getFcrHoraCancela() {
		return fcrHoraCancela;
	}

	public void setFcrHoraCancela(String fcrHoraCancela) {
		this.fcrHoraCancela = fcrHoraCancela;
	}

	public String getFcrFolioAcuse() {
		return fcrFolioAcuse;
	}

	public void setFcrFolioAcuse(String fcrFolioAcuse) {
		this.fcrFolioAcuse = fcrFolioAcuse;
	}

	public String getFcrErrores() {
		return fcrErrores;
	}

	public void setFcrErrores(String fcrErrores) {
		this.fcrErrores = fcrErrores;
	}
}
