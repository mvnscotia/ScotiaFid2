/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ResponseTraspasoBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;


//Imports
import java.io.Serializable;

//Class
public class ResponseTraspasoBean implements Serializable {
	 
	// Serial
	private static final long serialVersionUID = 1L;

   //Members
   private String estatus;
   private String folioConfirmacion;
   private String MsjError;
      
   //Getters_Setters
   public String getEstatus() {
      return estatus;
   }
   
   public void setEstatus(String estatus) {
      this.estatus = estatus;
   }

   public String getFolioConfirmacion() {
      return folioConfirmacion;
   }

   public void setFolioConfirmacion(String folioConfirmacion) {
      this.folioConfirmacion = folioConfirmacion;
   }

	public String getMsjError() {
		return MsjError;
	}
	
	public void setMsjError(String msjError) {
		MsjError = msjError;
	}
}