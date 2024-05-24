/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ServicioBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;

public class ServicioBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private int numero;
   private int tarifa;
   private int usaTabla;
   
   //Getters_Setters
   public int getNumero() {
     return numero;
   }

   public void setNumero(int numero) {
     this.numero = numero;
   }

   public int getTarifa() {
     return tarifa;
   }

   public void setTarifa(int tarifa) {
     this.tarifa = tarifa;
   }

   public int getUsaTabla() {
     return usaTabla;
   }

   public void setUsaTabla(int usaTabla) {
     this.usaTabla = usaTabla;
   }
}
