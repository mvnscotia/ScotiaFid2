/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : TPendientesBean.java
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
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanTPendientes")
@ViewScoped
public class TPendientesBean implements Serializable {
	//Serial
	private static final long serialVersionUID = 1L;
	
	//Members
    private boolean seleccion;   			//Dato_de_pantalla
    private Integer tpd_Folio;
    private Integer tpd_Num_Fiso;
    private Integer tpd_Sub_Fiso;
    private String  tpd_Tipo_Mov;
    private Integer tpd_Num_Instr;
    private Integer tpd_Tipo;
    private String  tpd_Transacc;
    private Integer tpd_Folio_Opera;
    private Date 	tpd_Fec_Mov;
    private Integer tpd_Usuario_Opera;
    private String  tpd_Nom_Usuario_Opera;   //Dato_de_pantalla
    private Date 	tpd_Fec_Opera;
    private Integer tpd_Usuario_Au;
    private Date 	tpd_Fec_Au;
    private String 	tpd_Importe;
    private Integer tpd_Moneda;
    private String  tpd_Nom_Moneda; 		//Dato_de_pantalla
    private String  tpd_Obs_Maker;
    private String  tpd_Obs_Checker;
    private String  tpd_Status;
    
    //Constructor
    public TPendientesBean() {}

	//Getters y Setters 
    public boolean getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	
	public Integer getTpd_Folio() {
		return tpd_Folio;
	}

	public void setTpd_Folio(Integer tpd_Folio) {
		this.tpd_Folio = tpd_Folio;
	}

	public Integer getTpd_Num_Fiso() {
		return tpd_Num_Fiso;
	}

	public void setTpd_Num_Fiso(Integer tpd_Num_Fiso) {
		this.tpd_Num_Fiso = tpd_Num_Fiso;
	}

	public Integer getTpd_Sub_Fiso() {
		return tpd_Sub_Fiso;
	}

	public void setTpd_Sub_Fiso(Integer tpd_Sub_Fiso) {
		this.tpd_Sub_Fiso = tpd_Sub_Fiso;
	}

	public String getTpd_Tipo_Mov() {
		return tpd_Tipo_Mov;
	}

	public void setTpd_Tipo_Mov(String tpd_Tipo_Mov) {
		this.tpd_Tipo_Mov = tpd_Tipo_Mov;
	}

	public Integer getTpd_Num_Instr() {
		return tpd_Num_Instr;
	}

	public void setTpd_Num_Instr(Integer tpd_Num_Instr) {
		this.tpd_Num_Instr = tpd_Num_Instr;
	}

	public Integer getTpd_Tipo() {
		return tpd_Tipo;
	}

	public void setTpd_Tipo(Integer tpd_Tipo) {
		this.tpd_Tipo = tpd_Tipo;
	}

	public String getTpd_Transacc() {
		return tpd_Transacc;
	}

	public void setTpd_Transacc(String tpd_Transacc) {
		this.tpd_Transacc = tpd_Transacc;
	}

	public Integer getTpd_Folio_Opera() {
		return tpd_Folio_Opera;
	}

	public void setTpd_Folio_Opera(Integer tpd_Folio_Opera) {
		this.tpd_Folio_Opera = tpd_Folio_Opera;
	}

	public Date getTpd_Fec_Mov() {
		return tpd_Fec_Mov;
	}

	public void setTpd_Fec_Mov(Date tpd_Fec_Mov) {
		this.tpd_Fec_Mov = tpd_Fec_Mov;
	}

	public Integer getTpd_Usuario_Opera() {
		return tpd_Usuario_Opera;
	}

	public void setTpd_Usuario_Opera(Integer tpd_Usuario_Opera) {
		this.tpd_Usuario_Opera = tpd_Usuario_Opera;
	}

	public String getTpd_Nom_Usuario_Opera() {
		return tpd_Nom_Usuario_Opera;
	}

	public void setTpd_Nom_Usuario_Opera(String tpd_Nom_Usuario_Opera) {
		this.tpd_Nom_Usuario_Opera = tpd_Nom_Usuario_Opera;
	}

	public Date getTpd_Fec_Opera() {
		return tpd_Fec_Opera;
	}

	public void setTpd_Fec_Opera(Date tpd_Fec_Opera) {
		this.tpd_Fec_Opera = tpd_Fec_Opera;
	}

	public Integer getTpd_Usuario_Au() {
		return tpd_Usuario_Au;
	}

	public void setTpd_Usuario_Au(Integer tpd_Usuario_Au) {
		this.tpd_Usuario_Au = tpd_Usuario_Au;
	}

	public Date getTpd_Fec_Au() {
		return tpd_Fec_Au;
	}

	public void setTpd_Fec_Au(Date tpd_Fec_Au) {
		this.tpd_Fec_Au = tpd_Fec_Au;
	}

	public String getTpd_Importe() {
		return tpd_Importe;
	}

	public void setTpd_Importe(String tpd_Importe) {
		this.tpd_Importe = tpd_Importe;
	}

	public Integer getTpd_Moneda() {
		return tpd_Moneda;
	}

	public void setTpd_Moneda(Integer tpd_Moneda) {
		this.tpd_Moneda = tpd_Moneda;
	}

	public String getTpd_Nom_Moneda() {
		return tpd_Nom_Moneda;
	}

	public void setTpd_Nom_Moneda(String tpd_Nom_Moneda) {
		this.tpd_Nom_Moneda = tpd_Nom_Moneda;
	}

	public String getTpd_Obs_Maker() {
		return tpd_Obs_Maker;
	}

	public void setTpd_Obs_Maker(String tpd_Obs_Maker) {
		this.tpd_Obs_Maker = tpd_Obs_Maker;
	}

	public String getTpd_Obs_Checker() {
		return tpd_Obs_Checker;
	}

	public void setTpd_Obs_Checker(String tpd_Obs_Checker) {
		this.tpd_Obs_Checker = tpd_Obs_Checker;
	}

	public String getTpd_Status() {
		return tpd_Status;
	}

	public void setTpd_Status(String tpd_Status) {
		this.tpd_Status = tpd_Status;
	}
}