/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : DetMasivoBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * CREADO      : 20210620
 * MODIFICADO  : 20210620 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;


//Imports
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanDetMasivo")
@ViewScoped
public class DetMasivoBean implements Serializable {
	//Serial
	private static final long serialVersionUID = 1L;
	
	//Members
    private String 	Nom_Tercero;   			 
    private Double 	Imp_Fijo_Pago;
    private String 	Nom_Moneda;   			 
    private String 	Banco;   
    private Double  Num_Cta;			 
    private String 	Concepto;   		 
    private String 	Forma_Pago;  	 		 
         
    //Constructor
    public DetMasivoBean() {}

	//Getters y Setters 
	public String getNom_Tercero() {
		return Nom_Tercero;
	}
 
	public void setNom_Tercero(String nom_Tercero) {
		Nom_Tercero = nom_Tercero;
	}

	public Double getImp_Fijo_Pago() {
		return Imp_Fijo_Pago;
	}

	public void setImp_Fijo_Pago(Double imp_Fijo_Pago) {
		Imp_Fijo_Pago = imp_Fijo_Pago;
	}

	public String getNom_Moneda() {
		return Nom_Moneda;
	}

	public void setNom_Moneda(String nom_Moneda) {
		Nom_Moneda = nom_Moneda;
	}

	public String getBanco() {
		return Banco;
	}

	public void setBanco(String banco) {
		Banco = banco;
	}

	public Double getNum_Cta() {
		return Num_Cta;
	}

	public void setNum_Cta(Double num_Cta) {
		Num_Cta = num_Cta;
	}

	public String getConcepto() {
		return Concepto;
	}

	public void setConcepto(String concepto) {
		Concepto = concepto;
	}

	public String getForma_Pago() {
		return Forma_Pago;
	}

	public void setForma_Pago(String forma_Pago) {
		Forma_Pago = forma_Pago;
	}
}