/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CatErrWebService.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

import java.io.Serializable;

//Class
public class CatErrWebServiceBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
	   
   //Members
   private int codigo;
   private String descripcion;
   
   //Getters_Setters
   public int getCodigo() {
	   return codigo;
   }
	
   public void setCodigo(int codigo) {
	   this.codigo = codigo;
   }
	
   public String getDescripcion() {
	   return descripcion;
   }
   
   public void setDescripcion(String descripcion) {
	   this.descripcion = descripcion;
   }
}