/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ProductoBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

public class ProductoBean implements Serializable {
   //Serial	
   private static final long serialVersionUID = 1L;
   
   //Members
   private int numero;
   private String nombre;
   private String importeAceptacion;
   private double importeAceptacionImp;
   private double importeManejo;
   
   //Getters_Setters
   public int getNumero() {
      return numero;
   }
   
   public void setNumero(int numero) {
      this.numero = numero;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public String getImporteAceptacion() {
	  return importeAceptacion;
   }

   public void setImporteAceptacion(String importeAceptacion) {
	  this.importeAceptacion = importeAceptacion;
   }

   public double getImporteAceptacionImp() {
	  return importeAceptacionImp;
   }

   public void setImporteAceptacionImp(double importeAceptacionImp) {
	  this.importeAceptacionImp = importeAceptacionImp;
   }

   public double getImporteManejo() {
      return importeManejo;
   }

   public void setImporteManejo(double importeManejo) {
      this.importeManejo = importeManejo;
   }
}
