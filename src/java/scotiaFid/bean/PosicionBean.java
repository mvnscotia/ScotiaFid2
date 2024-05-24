/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : PosicionBean.java
 * TIPO        : Tabla de Posicion y PosicionMSA (mes anterior)
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;


@ViewScoped
//Class
public class PosicionBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	//Tabla POSICION - POSICIONMSA
	private String 	Fideicomiso;
	private String 	SubFiso;	
	private int 	EntidadFinan; 		// Intermediario
	private double 	CtoInver;
	private int 	TipoMerca;
	private int 	NumInstrumento;
	private int 	SecEmisora;
	private String 	Pizarra;
	private String 	SerieEmis;
	private int 	CuponVig;
	private String 	NomCustodio;
	private int 	NumMoneda;
	private int 	ClaveGarantia;
	private double 	PosIniPeriodo; 		// Posición Inicial Periodo
	private double 	VtasIniPeriodo;
	private double 	CpasInicPeriodo;
	private double	PosIniEjer; 		// Posición Inicial Ejercicio
	private double 	VtasPosEjer;
	private double 	CpasPosEjer;
	private double 	PosActual; 			// Posición actual
	private double 	PosComprom; 		// Posición compromiso
	private double 	PosCostHist; 		// Costo Histórico
	private String 	estatus;
	
	private List<String> 	PizarrasFilter;
	
	// Otros campos utilizados para calcular columnas
	private double PosDisp;				// Posicion Disponible

	// Getters_Setters

	public String getFideicomiso() {
		return Fideicomiso;
	}

	public void setFideicomiso(String fideicomiso) {
		Fideicomiso = fideicomiso;
	}

	public String getSubFiso() {
		return SubFiso;
	}

	public void setSubFiso(String subFiso) {
		SubFiso = subFiso;
	}

	public int getEntidadFinan() {
		return EntidadFinan;
	}

	public void setEntidadFinan(int entidadFinan) {
		EntidadFinan = entidadFinan;
	}

	public double getCtoInver() {
		return CtoInver;
	}

	public void setCtoInver(double ctoInver) {
		CtoInver = ctoInver;
	}

	public int getTipoMerca() {
		return TipoMerca;
	}

	public void setTipoMerca(int tipoMerca) {
		TipoMerca = tipoMerca;
	}

	public int getNumInstrumento() {
		return NumInstrumento;
	}

	public void setNumInstrumento(int numInstrumento) {
		NumInstrumento = numInstrumento;
	}

	public int getSecEmisora() {
		return SecEmisora;
	}

	public void setSecEmisora(int secEmisora) {
		SecEmisora = secEmisora;
	}

	public String getPizarra() {
		return Pizarra;
	}

	public void setPizarra(String pizarra) {
		Pizarra = pizarra;
	}

	public String getSerieEmis() {
		return SerieEmis;
	}

	public void setSerieEmis(String serieEmis) {
		SerieEmis = serieEmis;
	}

	public int getCuponVig() {
		return CuponVig;
	}

	public void setCuponVig(int cuponVig) {
		CuponVig = cuponVig;
	}

	public String getNomCustodio() {
		return NomCustodio;
	}

	public void setNomCustodio(String nomCustodio) {
		NomCustodio = nomCustodio;
	}

	public int getNumMoneda() {
		return NumMoneda;
	}

	public void setNumMoneda(int numMoneda) {
		NumMoneda = numMoneda;
	}

	public int getClaveGarantia() {
		return ClaveGarantia;
	}

	public void setClaveGarantia(int claveGarantia) {
		ClaveGarantia = claveGarantia;
	}

	public double getPosIniPeriodo() {
		return PosIniPeriodo;
	}

	public void setPosIniPeriodo(double posIniPeriodo) {
		PosIniPeriodo = posIniPeriodo;
	}

	public double getVtasIniPeriodo() {
		return VtasIniPeriodo;
	}

	public void setVtasIniPeriodo(double vtasIniPeriodo) {
		VtasIniPeriodo = vtasIniPeriodo;
	}

	public double getCpasInicPeriodo() {
		return CpasInicPeriodo;
	}

	public void setCpasInicPeriodo(double cpasInicPeriodo) {
		CpasInicPeriodo = cpasInicPeriodo;
	}

	public double getPosIniEjer() {
		return PosIniEjer;
	}

	public void setPosIniEjer(double posIniEjer) {
		PosIniEjer = posIniEjer;
	}

	public double getVtasPosEjer() {
		return VtasPosEjer;
	}

	public void setVtasPosEjer(double vtasPosEjer) {
		VtasPosEjer = vtasPosEjer;
	}

	public double getCpasPosEjer() {
		return CpasPosEjer;
	}

	public void setCpasPosEjer(double cpasPosEjer) {
		CpasPosEjer = cpasPosEjer;
	}

	public double getPosActual() {
		return PosActual;
	}

	public void setPosActual(double posActual) {
		PosActual = posActual;
	}

	public double getPosComprom() {
		return PosComprom;
	}

	public void setPosComprom(double posComprom) {
		PosComprom = posComprom;
	}

	public double getPosCostHist() {
		return PosCostHist;
	}

	public void setPosCostHist(double posCostHist) {
		PosCostHist = posCostHist;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public double getPosDisp() {
		return PosDisp;
	}

	public void setPosDisp(double posDisp) {
		PosDisp = posDisp;
	}

	public List<String> getPizarrasFilter() {
		return PizarrasFilter;
	}

	public void setPizarrasFilter(List<String> pizarrasFilter) {
		PizarrasFilter = pizarrasFilter;
	}	
}
