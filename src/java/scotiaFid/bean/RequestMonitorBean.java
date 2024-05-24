/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : RequestMonitorBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class RequestMonitorBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	private String cuenta;

	// Getter_Setters
	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
}
