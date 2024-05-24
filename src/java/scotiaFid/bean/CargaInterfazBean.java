/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CargaInterfazBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class CargaInterfazBean implements Serializable {
	//Serial
	private static final long serialVersionUID = 1L;
	   
	//Members
	private int usuario;
	private String rutina;
	private Date fecha;
	private int secuencialArchivo;
	private int secuencial;
	private String ruta;
	private String nombreArchivo;
	private String archivoTemporal;
	private String cadena;
	private String estatus;
	private String mensaje;
	
	//Getters_Setters	
	public int getUsuario() {
		return usuario;
	}
	
	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
	
	public String getRutina() {
		return rutina;
	}
	
	public void setRutina(String rutina) {
		this.rutina = rutina;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public int getSecuencialArchivo() {
		return secuencialArchivo;
	}
	
	public void setSecuencialArchivo(int secuencialArchivo) {
		this.secuencialArchivo = secuencialArchivo;
	}
	
	public int getSecuencial() {
		return secuencial;
	}
	
	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}
	
	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	public String getArchivoTemporal() {
		return archivoTemporal;
	}

	public void setArchivoTemporal(String archivoTemporal) {
		this.archivoTemporal = archivoTemporal;
	}

	public String getCadena() {
		return cadena;
	}
	
	public void setCadena(String cadena) {
		this.cadena = cadena;
	}
	
	public String getEstatus() {
		return estatus;
	}
	
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
