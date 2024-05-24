/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CompraVentaBean.java
 * DESCRIPCION : Tablas Ventemis y Compemis
 * 			   : Almacenamiento de Compras, Ventas, Entradas y Salidas F�sicas
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;

//Class
public class CompraVentaBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	//_Comunes_en_las_tablas
	private String 	fideicomiso;
	private String 	subFiso;
	private int 	entidadFinan; 	//_Intermediario
	private double 	ctoInver;		//_Contrato_inversion
	private String 	nomPizarra; 	//_Pizarra
	private String 	serieEmis; 		//_Serie
	private String 	cuponVig; 		//_Cupon
	private String 	diaOpera; 		//_Fecha_del_movimiento
	private int 	secuencia;
	private int 	custodio;
	private int 	moneda;
	private String 	precioEmis;
	private double 	importe;
	private String 	titulos;
	private String  estatus;

	// COMPEMIS
	private double titulosDisp;
	private String folioCancela;

	// VENTEMIS
	private String foliOpera;
	private int precioProm;
	
	//Datos_Pantalla
	//MBCompraVenta.java
	//MBEntradaSalida.java
	private double tipoCambio;

	// Getter_Setters
	public String getFideicomiso() {
		return fideicomiso;
	}

	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	public String getSubFiso() {
		return subFiso;
	}

	public void setSubFiso(String subFiso) {
		this.subFiso = subFiso;
	}

	public int getEntidadFinan() {
		return entidadFinan;
	}

	public void setEntidadFinan(int entidadFinan) {
		this.entidadFinan = entidadFinan;
	}

	public double getCtoInver() {
		return ctoInver;
	}

	public void setCtoInver(double ctoInver) {
		this.ctoInver = ctoInver;
	}

	public String getNomPizarra() {
		return nomPizarra;
	}

	public void setNomPizarra(String nomPizarra) {
		this.nomPizarra = nomPizarra;
	}

	public String getSerieEmis() {
		return serieEmis;
	}

	public void setSerieEmis(String serieEmis) {
		this.serieEmis = serieEmis;
	}

	public String getCuponVig() {
		return cuponVig;
	}

	public void setCuponVig(String cuponVig) {
		this.cuponVig = cuponVig;
	}

	public String getDiaOpera() {
		return diaOpera;
	}

	public void setDiaOpera(String diaOpera) {
		this.diaOpera = diaOpera;
	}

	public int getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}

	public int getCustodio() {
		return custodio;
	}

	public void setCustodio(int custodio) {
		this.custodio = custodio;
	}

	public int getMoneda() {
		return moneda;
	}

	public void setMoneda(int moneda) {
		this.moneda = moneda;
	}

	public String getPrecioEmis() {
		return precioEmis;
	}

	public void setPrecioEmis(String precioEmis) {
		this.precioEmis = precioEmis;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getTitulos() {
		return titulos;
	}

	public void setTitulos(String titulos) {
		this.titulos = titulos;
	}

	//_Venta_
	public String getFoliOpera() {
		return foliOpera;
	}

	public void setFoliOpera(String foliOpera) {
		this.foliOpera = foliOpera;
	}

	public int getPrecioProm() {
		return precioProm;
	}

	public void setPrecioProm(int precioProm) {
		this.precioProm = precioProm;
	}

	//_Compra_
	public double getTitulosDisp() {
		return titulosDisp;
	}

	public void setTitulosDisp(double titulosDisp) {
		this.titulosDisp = titulosDisp;
	}

	public String getFolioCancela() {
		return folioCancela;
	}

	public void setFolioCancela(String folioCancela) {
		this.folioCancela = folioCancela;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
}
