/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : TPendientesDetBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * CREADO      : 20210609
 * MODIFICADO  : 20210609 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;


//Imports
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanTPendientesDet")
@ViewScoped
public class TPendientesDetBean implements Serializable {
	//Serial
	private static final long serialVersionUID = 1L;
	
	//Members
    private Integer tpdd_Folio;
    private Integer tpdd_Secuencia;
    private String  tpdd_Dato;
    private String  tpdd_Valor;
    private String  tpdd_Op_Desp;
    
    //Constructor
    public TPendientesDetBean() {}

    //Getters y Setters  
	public Integer getTpdd_Folio() {
		return tpdd_Folio;
	}

	public void setTpdd_Folio(Integer tpdd_Folio) {
		this.tpdd_Folio = tpdd_Folio;
	}

	public Integer getTpdd_Secuencia() {
		return tpdd_Secuencia;
	}

	public void setTpdd_Secuencia(Integer tpdd_Secuencia) {
		this.tpdd_Secuencia = tpdd_Secuencia;
	}

	public String getTpdd_Dato() {
		return tpdd_Dato;
	}

	public void setTpdd_Dato(String tpdd_Dato) {
		this.tpdd_Dato = tpdd_Dato;
	}

	public String getTpdd_Valor() {
		return tpdd_Valor;
	}

	public void setTpdd_Valor(String tpdd_Valor) {
		this.tpdd_Valor = tpdd_Valor;
	}

	public String getTpdd_Op_Desp() {
		return tpdd_Op_Desp;
	}

	public void setTpdd_Op_Desp(String tpdd_Op_Desp) {
		this.tpdd_Op_Desp = tpdd_Op_Desp;
	}
}