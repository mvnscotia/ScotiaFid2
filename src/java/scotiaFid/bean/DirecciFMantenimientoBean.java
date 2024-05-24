/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : CFDI 4.0
 * ARCHIVO     : DirecciFMantenimientoBean.java 
 * DESCRIPCION : Tabla SAF.DIRECCIF 
 * TIPO        : Class
 * MODIFICADO  : 20240315 VJN
 * PAQUETE     : scotiaFid.bean
 * NOTAS       : Separación de Nombre Legal y Fiscal 20240315 VJN
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
 
@ViewScoped 
//Class
public class DirecciFMantenimientoBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

    // Members
    // Tabla DIRECCIF
	private String		dif_num_contrato;
	private String  	dif_cve_pers_fid;
	private String		dif_num_pers_fid;
	
	
	private String  dif_mail;
	private int		dif_num_estado;
	private int		dif_num_pais;
	private String  dif_recep_calle;
	private String  dif_recep_colonia;
	private String  dif_recep_cp;
	private String  dif_recep_localidad;
	private String  dif_recep_municipio;
	private String  dif_recep_no_ext;
	private String  dif_recep_no_int;
	private String  dif_recep_referencia;
	private String  dif_telefono;
	private Date 	dif_fec_alta;
	private Date 	dif_ultmod;
	private String  dif_regimen_fiscal;
	
	//Adicionales
	private String	dif_nom_cto;
	private String	dif_nom_pers;
	private String	dif_rfc;
	private String 	dif_nom_pais;
	private String	dif_nom_estado;
	private String	dif_desc_regimen_fiscal;
	private String  dif_nom_legal;

	//Constructor
	public DirecciFMantenimientoBean() {}
	
	//Getter Setters
	public String getDif_cve_pers_fid() {
		return dif_cve_pers_fid;
	}
	
	public void setDif_cve_pers_fid(String dif_cve_pers_fid) {
		this.dif_cve_pers_fid = dif_cve_pers_fid;
	}
	
	public Date getDif_fec_alta() {
		return dif_fec_alta;
	}
	
	public void setDif_fec_alta(Date dif_fec_alta) {
		this.dif_fec_alta = dif_fec_alta;
	}
	
	public Date getDif_ultmod() {
		return dif_ultmod;
	}
	
	public void setDif_ultmod(Date dif_ultmod) {
		this.dif_ultmod = dif_ultmod;
	}
	
	public String getDif_mail() {
		return dif_mail;
	}
	
	public void setDif_mail(String dif_mail) {	 
		this.dif_mail = dif_mail;
	}
	
	public String getDif_num_contrato() {
		return dif_num_contrato;
	}
	
	public void setDif_num_contrato(String dif_num_contrato) {
		this.dif_num_contrato = dif_num_contrato;
	}
	
	public int getDif_num_estado() {
		return dif_num_estado;
	}
	
	public void setDif_num_estado(int dif_num_estado) {
		this.dif_num_estado = dif_num_estado;
	}
	
	public int getDif_num_pais() {
		return dif_num_pais;
	}
	
	public void setDif_num_pais(int dif_num_pais) {
		this.dif_num_pais = dif_num_pais;
	} 
	
	public String getDif_recep_calle() {
		return dif_recep_calle;
	}
	
	public void setDif_recep_calle(String dif_recep_calle) {
		this.dif_recep_calle = dif_recep_calle;
	}
	
	public String getDif_recep_colonia() {
		return dif_recep_colonia;
	}
	
	public void setDif_recep_colonia(String dif_recep_colonia) {
		this.dif_recep_colonia = dif_recep_colonia;
	}
	
	public String getDif_recep_cp() {
		return dif_recep_cp;
	}
	
	public void setDif_recep_cp(String dif_recep_cp) {
		this.dif_recep_cp = dif_recep_cp;
	}
	
	public String getDif_recep_localidad() {
		return dif_recep_localidad;
	}
	
	public void setDif_recep_localidad(String dif_recep_localidad) {
		this.dif_recep_localidad = dif_recep_localidad;
	}
	
	public String getDif_recep_municipio() {
		return dif_recep_municipio;
	}
	
	public void setDif_recep_municipio(String dif_recep_municipio) {
		this.dif_recep_municipio = dif_recep_municipio;
	}
	
	public String getDif_recep_no_ext() {
		return dif_recep_no_ext;
	}
	
	public void setDif_recep_no_ext(String dif_recep_no_ext) {
		this.dif_recep_no_ext = dif_recep_no_ext;
	}
	
	public String getDif_recep_no_int() {
		return dif_recep_no_int;
	}
	
	public void setDif_recep_no_int(String dif_recep_no_int) {
		this.dif_recep_no_int = dif_recep_no_int;
	}
	
	public String getDif_recep_referencia() {
		return dif_recep_referencia;
	}
	
	public void setDif_recep_referencia(String dif_recep_referencia) {
		this.dif_recep_referencia = dif_recep_referencia;
	}
	
	public String getDif_regimen_fiscal() {
		return dif_regimen_fiscal;
	}
	
	public void setDif_regimen_fiscal(String dif_regimen_fiscal) {
		  this.dif_regimen_fiscal = dif_regimen_fiscal;
	}
	
	public String getDif_telefono() {
		return dif_telefono;
	}
	
	public void setDif_telefono(String dif_telefono) {
		this.dif_telefono = dif_telefono;
	}

	public String getDif_nom_pais() {
		return dif_nom_pais;
	}

	public void setDif_nom_pais(String dif_nom_pais) {
		this.dif_nom_pais = dif_nom_pais;
	}

	public String getDif_nom_estado() {
		return dif_nom_estado;
	}

	public void setDif_nom_estado(String dif_nom_estado) {
		this.dif_nom_estado = dif_nom_estado;
	}

	public String getDif_desc_regimen_fiscal() {
		return dif_desc_regimen_fiscal;
	}

	public void setDif_desc_regimen_fiscal(String dif_desc_regimen_fiscal) {
		this.dif_desc_regimen_fiscal = dif_desc_regimen_fiscal;
	}

	public String getDif_num_pers_fid() {
		return dif_num_pers_fid;
	}

	public void setDif_num_pers_fid(String dif_num_pers_fid) {
		this.dif_num_pers_fid = dif_num_pers_fid;
	}

	public String getDif_nom_cto() {
		return dif_nom_cto;
	}

	public void setDif_nom_cto(String dif_nom_cto) {
		this.dif_nom_cto = dif_nom_cto;
	}

	public String getDif_nom_pers() {
		return dif_nom_pers;
	}

	public void setDif_nom_pers(String dif_nom_pers) {
		this.dif_nom_pers = dif_nom_pers;
	}

	public String getDif_rfc() {
		return dif_rfc;
	}

	public void setDif_rfc(String dif_rfc) {
		this.dif_rfc = dif_rfc;
	}

	public String getDif_nom_legal() {
		return dif_nom_legal;
	}

	public void setDif_nom_legal(String dif_nom_legal) {
		this.dif_nom_legal = dif_nom_legal;
	}
	
}
