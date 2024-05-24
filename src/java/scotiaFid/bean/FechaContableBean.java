/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : FechaContableBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

public class FechaContableBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String tipoFecha;
   private String fechaContable;
   private Date fecha;
   private int anio;
   private int mes;
   private int dia;
   
   //Getters_Setters
   public String getTipoFecha() {
      return tipoFecha;
   }

   public void setTipoFecha(String tipoFecha) {
      this.tipoFecha = tipoFecha;
   }

   public String getFechaContable() {
      return fechaContable;
   }

   public void setFechaContable(String fechaContable) {
      this.fechaContable = fechaContable;
   }
   
   public Date getFecha() {
      return fecha;
   }

   public void setFecha(Date fecha) {
      this.fecha = fecha;
   }

   public int getAnio() {
      return anio;
   }

   public void setAnio(int anio) {
      this.anio = anio;
   }

   public int getMes() {
      return mes;
   }

   public void setMes(int mes) {
      this.mes = mes;
   }
   
   public int getDia() {
      return dia;
   }

   public void setDia(int dia) {
      this.dia = dia;
   }
}
