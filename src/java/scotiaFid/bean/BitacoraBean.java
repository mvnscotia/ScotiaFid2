/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : BitacoraBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class BitacoraBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String fecha;
   private String terminal;
   private int usuario;
   private String nombre;
   private String funcion;
   private String detalle;
   
   public String getFecha() {
	 return fecha;
   }
   public void setFecha(String fecha) {
	 this.fecha = fecha;
   }
   
   public String getTerminal() {
	 return terminal;
   }

   public void setTerminal(String terminal) {
	 this.terminal = terminal;
   }

   public int getUsuario() {
	 return usuario;
   }

   public void setUsuario(int usuario) {
	 this.usuario = usuario;
   }

   public String getNombre() {
	 return nombre;
   }

   public void setNombre(String nombre) {
	 this.nombre = nombre;
   }

   public String getFuncion() {
	 return funcion;
   }

   public void setFuncion(String funcion) {
	 this.funcion = funcion;
   }

   public String getDetalle() {
	 return detalle;
   }

   public void setDetalle(String detalle) {
	 this.detalle = detalle;
   }
}
