/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : BitacoraUsiarioBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.sql.Date;

//Class
public class BitacoraUsuarioBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String bit_cve_funcion; 
   private String bit_det_bitacora;
   private Date	  bit_fec_transac;
   private String bit_id_terminal;
   private String bit_nom_pgm;
   private int 	  bit_num_usuario;

   //Constructor
   public BitacoraUsuarioBean() {}
	
   public String getBit_cve_funcion() {
	   return bit_cve_funcion;
   }
   
	public void setBit_cve_funcion(String bit_cve_funcion) {
		this.bit_cve_funcion = bit_cve_funcion;
	}
	
	public String getBit_det_bitacora() {
		return bit_det_bitacora;
	}
	
	public void setBit_det_bitacora(String bit_det_bitacora) {
		this.bit_det_bitacora = bit_det_bitacora;
	}
	
	public String getBit_id_terminal() {
		return bit_id_terminal;
	}
	
	public void setBit_id_terminal(String bit_id_terminal) {
		this.bit_id_terminal = bit_id_terminal;
	}
	
	public String getBit_nom_pgm() {
		return bit_nom_pgm;
	}
	
	public void setBit_nom_pgm(String bit_nom_pgm) {
		this.bit_nom_pgm = bit_nom_pgm;
	}
	
	public int getBit_num_usuario() {
		return bit_num_usuario;
	}
	
	public void setBit_num_usuario(int bit_num_usuario) {
		this.bit_num_usuario = bit_num_usuario;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getBit_fec_transac() {
		return bit_fec_transac;
	}

	public void setBit_fec_transac(Date bit_fec_transac) {
		this.bit_fec_transac = bit_fec_transac;
	} 
}
