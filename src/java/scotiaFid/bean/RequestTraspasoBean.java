/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : RequestTraspasoBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;


//Imports
import java.io.Serializable;

public class RequestTraspasoBean implements Serializable {
	 
	// Serial
	private static final long serialVersionUID = 1L;

   //Members
   private String cuentaCargo;
   private String cuentaAbono;
   private String importe;
   private String referencia;
   private String usuario;
   private String url;
   private String xml;
   
   //Getters_Setters
   public String getCuentaCargo() {
      return cuentaCargo;
   }
   
   public void setCuentaCargo(String cuentaCargo) {
      this.cuentaCargo = cuentaCargo;
   }
   
   public String getCuentaAbono() {
      return cuentaAbono;
   }
   
   public void setCuentaAbono(String cuentaAbono) {
      this.cuentaAbono = cuentaAbono;
   }
   
   public String getImporte() {
      return importe;
   }

   public void setImporte(String importe) {
      this.importe = importe;
   }
   
   public String getReferencia() {
      return referencia;
   }
   
   public void setReferencia(String referencia) {
      this.referencia = referencia;
   }
   
   public String getUsuario() {
      return usuario;
   }
   
   public void setUsuario(String usuario) {
      this.usuario = usuario;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getXml() {
      return xml;
   }

   public void setXml(String xml) {
      this.xml = xml;
   }
}