/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * TIPO        : Class
 * DESCRIPCION : Tabla ARCHIVO_OTROS
 * 				 Archivo de Interfaz Inversiones Otros Intermediarios
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class ArchivoOtrosBean implements Serializable {

	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	//_Coumnas_Tabla
	private String 	Nom_file;
	private int 	Id;
	private String 	Fec_Movto;
	private String 	Num_Entidad_Financ;
	private String 	Custodio;
	private String 	Cve_Operacion;
	private String 	Tipo_Valor;
	private String 	Cto_Inv;
	private String 	Num_Cto;
	private String 	Num_SubCto;
	private String 	Pizarra;
	private String 	Serie;
	private String 	Titulos;
	private String 	Cupon;
	private String 	Precio;
	private String 	Importe;
	private String 	Tasa;
	private String 	Plazo;
	private String 	Premio;
	private String 	Moneda;
	private String 	Comision;
	private String 	IVA;
	private String 	ISR;
	private String 	Tipo_Cambio;
	private String 	Fec_Vence;
	private String 	Num_Banco;
	private String 	Redaccion;
	private String 	Estatus;
	private String 	Desc_Error;
	private String 	Fec_Alta;
	
	//Datos_Resumen_Otros_Intermediario
	private String  Intermediario;
	private int     Num_Custodio;
	private int     Id_Moneda;	
	private int     Total_Reg;
	private Double	Total_Imp;
	private int		Total_Reg_Err;
	private Double	Total_Imp_Err;
	
	// Getters_Setters_
	
	public String getNom_file() {
		return Nom_file;
	}

	public void setNom_file(String nom_file) {
		Nom_file = nom_file;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFec_Movto() {
		return Fec_Movto;
	}

	public void setFec_Movto(String fec_Movto) {
		Fec_Movto = fec_Movto;
	}

	public String getNum_Entidad_Financ() {
		return Num_Entidad_Financ;
	}

	public void setNum_Entidad_Financ(String num_Entidad_Financ) {
		Num_Entidad_Financ = num_Entidad_Financ;
	}

	public String getCustodio() {
		return Custodio;
	}

	public void setCustodio(String custodio) {
		Custodio = custodio;
	}

	public String getCve_Operacion() {
		return Cve_Operacion;
	}

	public void setCve_Operacion(String cve_Operacion) {
		Cve_Operacion = cve_Operacion;
	}

	public String getTipo_Valor() {
		return Tipo_Valor;
	}

	public void setTipo_Valor(String tipo_Valor) {
		Tipo_Valor = tipo_Valor;
	}

	public String getCto_Inv() {
		return Cto_Inv;
	}

	public void setCto_Inv(String cto_Inv) {
		Cto_Inv = cto_Inv;
	}

	public String getNum_Cto() {
		return Num_Cto;
	}

	public void setNum_Cto(String num_Cto) {
		Num_Cto = num_Cto;
	}

	public String getNum_SubCto() {
		return Num_SubCto;
	}

	public void setNum_SubCto(String num_SubCto) {
		Num_SubCto = num_SubCto;
	}

	public String getPizarra() {
		return Pizarra;
	}

	public void setPizarra(String pizarra) {
		Pizarra = pizarra;
	}

	public String getSerie() {
		return Serie;
	}

	public void setSerie(String serie) {
		Serie = serie;
	}

	public String getTitulos() {
		return Titulos;
	}

	public void setTitulos(String titulos) {
		Titulos = titulos;
	}

	public String getCupon() {
		return Cupon;
	}

	public void setCupon(String cupon) {
		Cupon = cupon;
	}

	public String getPrecio() {
		return Precio;
	}

	public void setPrecio(String precio) {
		Precio = precio;
	}

	public String getImporte() {
		return Importe;
	}

	public void setImporte(String importe) {
		Importe = importe;
	}

	public String getTasa() {
		return Tasa;
	}

	public void setTasa(String tasa) {
		Tasa = tasa;
	}

	public String getPlazo() {
		return Plazo;
	}

	public void setPlazo(String plazo) {
		Plazo = plazo;
	}

	public String getPremio() {
		return Premio;
	}

	public void setPremio(String premio) {
		Premio = premio;
	}

	public String getMoneda() {
		return Moneda;
	}

	public void setMoneda(String moneda) {
		Moneda = moneda;
	}

	public String getComision() {
		return Comision;
	}

	public void setComision(String comision) {
		Comision = comision;
	}

	public String getIVA() {
		return IVA;
	}

	public void setIVA(String iVA) {
		IVA = iVA;
	}

	public String getISR() {
		return ISR;
	}

	public void setISR(String iSR) {
		ISR = iSR;
	}

	public String getTipo_Cambio() {
		return Tipo_Cambio;
	}

	public void setTipo_Cambio(String tipo_Cambio) {
		Tipo_Cambio = tipo_Cambio;
	}

	public String getFec_Vence() {
		return Fec_Vence;
	}

	public void setFec_Vence(String fec_Vence) {
		Fec_Vence = fec_Vence;
	}

	public String getNum_Banco() {
		return Num_Banco;
	}

	public void setNum_Banco(String num_Banco) {
		Num_Banco = num_Banco;
	}

	public String getRedaccion() {
		return Redaccion;
	}

	public void setRedaccion(String redaccion) {
		Redaccion = redaccion;
	}

	public String getEstatus() {
		return Estatus;
	}

	public void setEstatus(String estatus) {
		Estatus = estatus;
	}

	public String getDesc_Error() {
		return Desc_Error;
	}

	public void setDesc_Error(String desc_Error) {
		Desc_Error = desc_Error;
	}

	public String getFec_Alta() {
		return Fec_Alta;
	}

	public void setFec_Alta(String fec_Alta) {
		Fec_Alta = fec_Alta;
	}

	public String getIntermediario() {
		return Intermediario;
	}

	public void setIntermediario(String intermediario) {
		Intermediario = intermediario;
	}

	public int getNum_Custodio() {
		return Num_Custodio;
	}

	public void setNum_Custodio(int num_Custodio) {
		Num_Custodio = num_Custodio;
	}

	public int getId_Moneda() {
		return Id_Moneda;
	}

	public void setId_Moneda(int id_Moneda) {
		Id_Moneda = id_Moneda;
	}

	public int getTotal_Reg() {
		return Total_Reg;
	}

	public void setTotal_Reg(int total_Reg) {
		Total_Reg = total_Reg;
	}

	public Double getTotal_Imp() {
		return Total_Imp;
	}

	public void setTotal_Imp(Double total_Imp) {
		Total_Imp = total_Imp;
	}

	public int getTotal_Reg_Err() {
		return Total_Reg_Err;
	}

	public void setTotal_Reg_Err(int total_Reg_Err) {
		Total_Reg_Err = total_Reg_Err;
	}

	public Double getTotal_Imp_Err() {
		return Total_Imp_Err;
	}

	public void setTotal_Imp_Err(Double total_Imp_Err) {
		Total_Imp_Err = total_Imp_Err;
	}
}
