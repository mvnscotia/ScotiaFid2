/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : RolBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * CREADO      : 20210306
 * MODIFICADO  : 20210606 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Packages
package scotiaFid.bean;

//Imports
import java.io.Serializable;

public class RolBean implements Serializable {
    //Serial
	private static final long serialVersionUID = 1L;

	//Members
	private String claveRol;
	private String descripcionRol;
	
	//Getters_Setters
	public String getClaveRol() {
		return claveRol;
	}
	
	public void setClaveRol(String claveRol) {
		this.claveRol = claveRol;
	}
	
	public String getDescripcionRol() {
		return descripcionRol;
	}
	
	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}
}
