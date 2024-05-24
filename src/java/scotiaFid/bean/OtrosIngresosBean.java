/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : OtrosIngresosBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class OtrosIngresosBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	private Date fecha;
	private Date fechaDel;
	private Date fechaAl;
	private String servicio;
	private String fechaValor;
	private String fideicomiso;
	private String nombreServicio;
	private String cantidad;
	private String precioUnitario;
	private String importe;
	private String importeFormat;
	private String tipoCambio;
	private String periodoDel;
	private String periodoAl;
	private String personaCobra;
	private String periodoCobro;
	private String moneda;
	private String nombreMoneda;
	private String iva;
	private String ivaFormat;
	private String ivaFronterizo;
	private String total;
	private String totalFormat;
	private String descripcion;
	private boolean actualizaFecha;

	// Getters_Setters
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaDel() {
		return fechaDel;
	}

	public void setFechaDel(Date fechaDel) {
		this.fechaDel = fechaDel;
	}

	public Date getFechaAl() {
		return fechaAl;
	}

	public void setFechaAl(Date fechaAl) {
		this.fechaAl = fechaAl;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getFideicomiso() {
		return fideicomiso;
	}

	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(String precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getPeriodoDel() {
		return periodoDel;
	}

	public void setPeriodoDel(String periodoDel) {
		this.periodoDel = periodoDel;
	}

	public String getPeriodoAl() {
		return periodoAl;
	}

	public void setPeriodoAl(String periodoAl) {
		this.periodoAl = periodoAl;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNombreMoneda() {
		return nombreMoneda;
	}

	public void setNombreMoneda(String nombreMoneda) {
		this.nombreMoneda = nombreMoneda;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public String getIvaFronterizo() {
		return ivaFronterizo;
	}

	public void setIvaFronterizo(String ivaFronterizo) {
		this.ivaFronterizo = ivaFronterizo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public boolean isActualizaFecha() {
		return actualizaFecha;
	}

	public void setActualizaFecha(boolean actualizaFecha) {
		this.actualizaFecha = actualizaFecha;
	}

	public String getPersonaCobra() {
		return personaCobra;
	}

	public void setPersonaCobra(String personaCobra) {
		this.personaCobra = personaCobra;
	}

	public String getPeriodoCobro() {
		return periodoCobro;
	}

	public void setPeriodoCobro(String periodoCobro) {
		this.periodoCobro = periodoCobro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImporteFormat() {
		return importeFormat;
	}

	public void setImporteFormat(String importeFormat) {
		this.importeFormat = importeFormat;
	}

	public String getIvaFormat() {
		return ivaFormat;
	}

	public void setIvaFormat(String ivaFormat) {
		this.ivaFormat = ivaFormat;
	}

	public String getTotalFormat() {
		return totalFormat;
	}

	public void setTotalFormat(String totalFormat) {
		this.totalFormat = totalFormat;
	}
}
