/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ContratoBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

public class ContratoBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	private int contrato;
	private String nombre;
	private String estatus;
	private String tipoAdmon;
	private int monedaExtranjera;
	private int subcontrato;
	private int nivel1;
	private int nivel2;
	private int nivel3;
	private int nivel4;
	private int nivel5; // VJN - 07/02/22 - INICIO - AGREGA NIVEL 5
	private int tipoNeg;
	private int clasificacion;
	private int abiertoCerrado;
	private int impuestoEspecial;
	private int prorrateo;
	private boolean bSubContrato; // VJN - INICIO - Boolean Sub_Fiso

	// Getters_Setters
	public int getContrato() {
		return contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getTipoAdmon() {
		return tipoAdmon;
	}

	public void setTipoAdmon(String tipoAdmon) {
		this.tipoAdmon = tipoAdmon;
	}

	public int getMonedaExtranjera() {
		return monedaExtranjera;
	}

	public void setMonedaExtranjera(int monedaExtranjera) {
		this.monedaExtranjera = monedaExtranjera;
	}

	public int getSubcontrato() {
		return subcontrato;
	}

	public void setSubcontrato(int subcontrato) {
		this.subcontrato = subcontrato;
	}

	public int getNivel1() {
		return nivel1;
	}

	public void setNivel1(int nivel1) {
		this.nivel1 = nivel1;
	}

	public int getNivel2() {
		return nivel2;
	}

	public void setNivel2(int nivel2) {
		this.nivel2 = nivel2;
	}

	public int getNivel3() {
		return nivel3;
	}

	public void setNivel3(int nivel3) {
		this.nivel3 = nivel3;
	}

	public int getNivel4() {
		return nivel4;
	}

	public void setNivel4(int nivel4) {
		this.nivel4 = nivel4;
	}

//VJN - 07/02/22 - INICIO 
	public int getNivel5() {
		return nivel5;
	}

	public void setNivel5(int nivel5) {
		this.nivel5 = nivel5;
	}
//VJN - 07/02/22 - FIN 

	public int getTipoNeg() {
		return tipoNeg;
	}

	public void setTipoNeg(int tipoNeg) {
		this.tipoNeg = tipoNeg;
	}

	public int getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(int clasificacion) {
		this.clasificacion = clasificacion;
	}

	public int getAbiertoCerrado() {
		return abiertoCerrado;
	}

	public void setAbiertoCerrado(int abiertoCerrado) {
		this.abiertoCerrado = abiertoCerrado;
	}

	public int getImpuestoEspecial() {
		return impuestoEspecial;
	}

	public void setImpuestoEspecial(int impuestoEspecial) {
		this.impuestoEspecial = impuestoEspecial;
	}

	public int getProrrateo() {
		return prorrateo;
	}

	public void setProrrateo(int prorrateo) {
		this.prorrateo = prorrateo;
	}

	public boolean getbSubContrato() {
		return bSubContrato;
	}

	public void setbSubContrato(boolean bSubContrato) {
		this.bSubContrato = bSubContrato;
	}
}
