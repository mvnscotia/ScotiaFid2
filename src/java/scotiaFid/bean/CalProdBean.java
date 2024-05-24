/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CalProdBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class CalProdBean implements Serializable {
   //Serial   
   private static final long serialVersionUID = 1L;

   //Members
   private String plan;
   private String rutina;
   private String secuencia;
   private String estatus;
   
   //Getters_Setters   
   public String getPlan() {
      return plan;
   }

   public void setPlan(String plan) {
      this.plan = plan;
   }

   public String getRutina() {
      return rutina;
   }

   public void setRutina(String rutina) {
      this.rutina = rutina;
   }

   public String getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(String secuencia) {
      this.secuencia = secuencia;
   }

   public String getEstatus() {
      return estatus;
   }

   public void setEstatus(String estatus) {
      this.estatus = estatus;
   }
}
