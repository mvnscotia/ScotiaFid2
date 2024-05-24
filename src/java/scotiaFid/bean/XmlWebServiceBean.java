/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : XmlWebServiceBean.java
 * DESCRIPCION : Tabla FD_XML_WEBSERVICE, almacenamiento para log de Servicios Web 
 * TIPO        : Bean
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;
import java.util.Date;

//Class
public class XmlWebServiceBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	// Tabla FD_XML_WEBSERVICE
	private Date 	Fecha_Hora;     //Fecha_en_que_se_Aplico_SW
	private String 	Nom_Interface;	
	private String 	Tipo_Peticion;	
	private String 	User;	
	private String 	XML;	 
	 
	//Getters y Setters 
	public Date getFecha_Hora() {
		return Fecha_Hora;
	}
	
	public void setFecha_Hora(Date fecha_Hora) {
		Fecha_Hora = fecha_Hora;
	}

	public String getNom_Interface() {
		return Nom_Interface;
	}

	public void setNom_Interface(String nom_Interface) {
		Nom_Interface = nom_Interface;
	}

	public String getTipo_Peticion() {
		return Tipo_Peticion;
	}

	public void setTipo_Peticion(String tipo_Peticion) {
		Tipo_Peticion = tipo_Peticion;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getXML() {
		return XML;
	}

	public void setXML(String xML) {
		XML = xML;
	} 
}
