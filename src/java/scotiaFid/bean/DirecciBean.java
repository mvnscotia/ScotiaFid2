/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : CFDI
 * ARCHIVO     : DirecciBean.java 
 * DESCRIPCION : Tabla SAF.DIRECCI 
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import javax.inject.Named;
import java.io.Serializable;
import javax.faces.view.ViewScoped;

@Named("DirecciBean") 
@ViewScoped 
//Class
public class DirecciBean implements Serializable {
	// Serial
	private static final long serialVersionUID = 1L;

    // Members
    // Tabla DIRECCI
	private int 	dir_ano_alta_reg;
	private int 	dir_ano_ult_mod;
	private String  dir_calle_num;
	private String  dir_codigo_postal;	
	private String  dir_cve_pers_fid;
	private String  dir_cve_st_direcc;
	private String  dir_cve_tipo_domic;
	private int 	dir_dia_alta_reg;
	private int 	dir_dia_ult_mod;
	private int 	dir_mes_alta_reg;
	private int 	dir_mes_ult_mod;
	private String  dir_nom_atencion;
	private String  dir_nom_colonia;
	private String	dir_nom_estado;
	private String	dir_nom_pais;
	private String  dir_nom_poblacion;
	private int		dir_num_contrato;
	private int		dir_num_estado;
	private int		dir_num_pais;
	private int		dir_num_pers_fid; 
	private int		dir_num_sec_direcc; 
	
	//Adicionales
	private String  ben_tipo_per;
	private String  ben_rfc;
	private String  ben_nacionalidad;
	private String  ben_num_lada_casa;
	private String  ben_num_telef_casa;
	private String  ben_num_lada_ofic;
	private String  ben_num_telef_ofic;
	private String  ben_num_ext_ofic;
	private String  dir_mail;
	private String  dir_no_ejecutivo;
	private String  dir_nom_ejecutivo;
	private String  dir_nombre;
	private String  dir_localidad;

	//Constructor
    public DirecciBean() {}
    
	//Getter Setters
	public int getDir_ano_alta_reg() {
		return dir_ano_alta_reg;
	}
	public void setDir_ano_alta_reg(int dir_ano_alta_reg) {
		this.dir_ano_alta_reg = dir_ano_alta_reg;
	}
	public int getDir_ano_ult_mod() {
		return dir_ano_ult_mod;
	}
	public void setDir_ano_ult_mod(int dir_ano_ult_mod) {
		this.dir_ano_ult_mod = dir_ano_ult_mod;
	}
	public String getDir_calle_num() {
		return dir_calle_num;
	}
	public void setDir_calle_num(String dir_calle_num) {
		this.dir_calle_num = dir_calle_num;
	}
	public String getDir_codigo_postal() {
		return dir_codigo_postal;
	}
	public void setDir_codigo_postal(String dir_codigo_postal) {
		this.dir_codigo_postal = dir_codigo_postal;
	}
	public String getDir_cve_pers_fid() {
		return dir_cve_pers_fid;
	}
	public void setDir_cve_pers_fid(String dir_cve_pers_fid) {
		this.dir_cve_pers_fid = dir_cve_pers_fid;
	}
	public String getDir_cve_st_direcc() {
		return dir_cve_st_direcc;
	}
	public void setDir_cve_st_direcc(String dir_cve_st_direcc) {
		this.dir_cve_st_direcc = dir_cve_st_direcc;
	}
	public String getDir_cve_tipo_domic() {
		return dir_cve_tipo_domic;
	}
	public void setDir_cve_tipo_domic(String dir_cve_tipo_domic) {
		this.dir_cve_tipo_domic = dir_cve_tipo_domic;
	}
	public int getDir_dia_alta_reg() {
		return dir_dia_alta_reg;
	}
	public void setDir_dia_alta_reg(int dir_dia_alta_reg) {
		this.dir_dia_alta_reg = dir_dia_alta_reg;
	}
	public int getDir_dia_ult_mod() {
		return dir_dia_ult_mod;
	}
	public void setDir_dia_ult_mod(int dir_dia_ult_mod) {
		this.dir_dia_ult_mod = dir_dia_ult_mod;
	}
	public int getDir_mes_alta_reg() {
		return dir_mes_alta_reg;
	}
	public void setDir_mes_alta_reg(int dir_mes_alta_reg) {
		this.dir_mes_alta_reg = dir_mes_alta_reg;
	}
	public int getDir_mes_ult_mod() {
		return dir_mes_ult_mod;
	}
	public void setDir_mes_ult_mod(int dir_mes_ult_mod) {
		this.dir_mes_ult_mod = dir_mes_ult_mod;
	}
	public String getDir_nom_atencion() {
		return dir_nom_atencion;
	}
	public void setDir_nom_atencion(String dir_nom_atencion) {
		this.dir_nom_atencion = dir_nom_atencion;
	}
	public String getDir_nom_colonia() {
		return dir_nom_colonia;
	}
	public void setDir_nom_colonia(String dir_nom_colonia) {
		this.dir_nom_colonia = dir_nom_colonia;
	}
	public String getDir_nom_estado() {
		return dir_nom_estado;
	}
	public void setDir_nom_estado(String dir_nom_estado) {
		this.dir_nom_estado = dir_nom_estado;
	}
	public String getDir_nom_pais() {
		return dir_nom_pais;
	}
	public void setDir_nom_pais(String dir_nom_pais) {
		this.dir_nom_pais = dir_nom_pais;
	}
	public String getDir_nom_poblacion() {
		return dir_nom_poblacion;
	}
	public void setDir_nom_poblacion(String dir_nom_poblacion) {
		this.dir_nom_poblacion = dir_nom_poblacion;
	}
	public int getDir_num_contrato() {
		return dir_num_contrato;
	}
	public void setDir_num_contrato(int dir_num_contrato) {
		this.dir_num_contrato = dir_num_contrato;
	}
	public int getDir_num_estado() {
		return dir_num_estado;
	}
	public void setDir_num_estado(int dir_num_estado) {
		this.dir_num_estado = dir_num_estado;
	}
	public int getDir_num_pais() {
		return dir_num_pais;
	}
	public void setDir_num_pais(int dir_num_pais) {
		this.dir_num_pais = dir_num_pais;
	}
	public int getDir_num_pers_fid() {
		return dir_num_pers_fid;
	}
	public void setDir_num_pers_fid(int dir_num_pers_fid) {
		this.dir_num_pers_fid = dir_num_pers_fid;
	}
	public int getDir_num_sec_direcc() {
		return dir_num_sec_direcc;
	}
	public void setDir_num_sec_direcc(int dir_num_sec_direcc) {
		this.dir_num_sec_direcc = dir_num_sec_direcc;
	}
	public String getBen_tipo_per() {
		return ben_tipo_per;
	}
	public void setBen_tipo_per(String ben_tipo_per) {
		this.ben_tipo_per = ben_tipo_per;
	}
	public String getBen_rfc() {
		return ben_rfc;
	}
	public void setBen_rfc(String ben_rfc) {
		this.ben_rfc = ben_rfc;
	}
	public String getBen_nacionalidad() {
		return ben_nacionalidad;
	}
	public void setBen_nacionalidad(String ben_nacionalidad) {
		this.ben_nacionalidad = ben_nacionalidad;
	}
	public String getBen_num_lada_casa() {
		return ben_num_lada_casa;
	}
	public void setBen_num_lada_casa(String ben_num_lada_casa) {
		this.ben_num_lada_casa = ben_num_lada_casa;
	}
	public String getBen_num_telef_casa() {
		return ben_num_telef_casa;
	}
	public void setBen_num_telef_casa(String ben_num_telef_casa) {
		this.ben_num_telef_casa = ben_num_telef_casa;
	}
	public String getBen_num_lada_ofic() {
		return ben_num_lada_ofic;
	}
	public void setBen_num_lada_ofic(String ben_num_lada_ofic) {
		this.ben_num_lada_ofic = ben_num_lada_ofic;
	}
	public String getBen_num_telef_ofic() {
		return ben_num_telef_ofic;
	}
	public void setBen_num_telef_ofic(String ben_num_telef_ofic) {
		this.ben_num_telef_ofic = ben_num_telef_ofic;
	}
	public String getBen_num_ext_ofic() {
		return ben_num_ext_ofic;
	}
	public void setBen_num_ext_ofic(String ben_num_ext_ofic) {
		this.ben_num_ext_ofic = ben_num_ext_ofic;
	}
	public String getDir_mail() {
		return dir_mail;
	}
	public void setDir_mail(String dir_mail) {
		this.dir_mail = dir_mail;
	}
	public String getDir_no_ejecutivo() {
		return dir_no_ejecutivo;
	}
	public void setDir_no_ejecutivo(String dir_no_ejecutivo) {
		this.dir_no_ejecutivo = dir_no_ejecutivo;
	}
	public String getDir_nom_ejecutivo() {
		return dir_nom_ejecutivo;
	}
	public void setDir_nom_ejecutivo(String dir_nom_ejecutivo) {
		this.dir_nom_ejecutivo = dir_nom_ejecutivo;
	}
	public String getDir_nombre() {
		return dir_nombre;
	}
	public void setDir_nombre(String dir_nombre) {
		this.dir_nombre = dir_nombre;
	}
	public String getDir_localidad() {
		return dir_localidad;
	}
	public void setDir_localidad(String dir_localidad) {
		this.dir_localidad = dir_localidad;
	}   
}
