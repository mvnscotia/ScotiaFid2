/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * TIPO        : Class
 * DESCRIPCION : Tabla INV_RECHAZOS
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class InvRechazosBean implements Serializable {
	
	// Serial
	private static final long serialVersionUID = 1L;
	
	// Members
	//_Componentes_Pantalla
	private String Cto_Inv;
	private int	   Num_Entidad_Financ;
	private String Entidad_Financ;
	private int	   Num_Custodio;
	private String Custodio;
	private int	   No_Existe;
	private int    Num_Regis;
	
	private String Clave;
	private String Servicio;
	
	//_Coumnas_Tabla
	private String Nom_File;
	private int Row_Id;
	private String Origen;
	private Date Fec_Movto;
	private String Cve_Operacion;
	private String Tipo_Valor;
	private String Num_Cto;
	private String Num_Subcto;
	private String Pizarra;
	private String Serie;
	private String Titulos;
	private String Cupon;
	private String Precio;
	private String Importe;
	private String Tasa_Rend;
	private int	   Plazo;
	private String Premio;
	private String Num_Moneda;
	private String Comision;
	private String IVA;
	private String ISR;
	private String Tipo_Cambio;
	private String Fec_Vence;
	private String Num_Banco;
	private String Redaccion;
	private String Fec_Reg;
	private String Fec_Apl;
	private String Mercado;
	private String Orden;
	private String Num_Trans;
	private String Mueve_Tits;
	private String Mueve_Efectivo;
	private String Status;
	private String Desc_Error;
	private Date Fec_Atencion;
	private int User_Atencion;
	private Date Fec_Alta_Reg;

	// Getters_Setters
	public String getNom_File() {
		return Nom_File;
	}

	public void setNom_File(String nom_File) {
		Nom_File = nom_File;
	}

	public int getRow_Id() {
		return Row_Id;
	}

	public void setRow_Id(int row_Id) {
		Row_Id = row_Id;
	}

	public String getOrigen() {
		return Origen;
	}

	public void setOrigen(String origen) {
		Origen = origen;
	}

	public Date getFec_Movto() {
		return Fec_Movto;
	}

	public void setFec_Movto(Date fec_Movto) {
		Fec_Movto = fec_Movto;
	}

	public String getEntidad_Financ() {
		return Entidad_Financ;
	}

	public void setEntidad_Financ(String entidad_Financ) {
		Entidad_Financ = entidad_Financ;
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

	public String getNum_Subcto() {
		return Num_Subcto;
	}

	public void setNum_Subcto(String num_Subcto) {
		Num_Subcto = num_Subcto;
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

	public String getTasa_Rend() {
		return Tasa_Rend;
	}

	public void setTasa_Rend(String tasa_Rend) {
		Tasa_Rend = tasa_Rend;
	}

	public int getPlazo() {
		return Plazo;
	}

	public void setPlazo(int plazo) {
		Plazo = plazo;
	}

	public String getPremio() {
		return Premio;
	}

	public void setPremio(String premio) {
		Premio = premio;
	}

	public String getNum_Moneda() {
		return Num_Moneda;
	}

	public void setNum_Moneda(String num_Moneda) {
		Num_Moneda = num_Moneda;
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

	public String getFec_Reg() {
		return Fec_Reg;
	}

	public void setFec_Reg(String fec_Reg) {
		Fec_Reg = fec_Reg;
	}

	public String getFec_Apl() {
		return Fec_Apl;
	}

	public void setFec_Apl(String fec_Apl) {
		Fec_Apl = fec_Apl;
	}

	public String getMercado() {
		return Mercado;
	}

	public void setMercado(String mercado) {
		Mercado = mercado;
	}

	public String getOrden() {
		return Orden;
	}

	public void setOrden(String orden) {
		Orden = orden;
	}

	public String getNum_Trans() {
		return Num_Trans;
	}

	public void setNum_Trans(String num_Trans) {
		Num_Trans = num_Trans;
	}

	public String getMueve_Tits() {
		return Mueve_Tits;
	}

	public void setMueve_Tits(String mueve_Tits) {
		Mueve_Tits = mueve_Tits;
	}

	public String getMueve_Efectivo() {
		return Mueve_Efectivo;
	}

	public void setMueve_Efectivo(String mueve_Efectivo) {
		Mueve_Efectivo = mueve_Efectivo;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getDesc_Error() {
		return Desc_Error;
	}

	public void setDesc_Error(String desc_Error) {
		Desc_Error = desc_Error;
	}

	public Date getFec_Atencion() {
		return Fec_Atencion;
	}

	public void setFec_Atencion(Date fec_Atencion) {
		Fec_Atencion = fec_Atencion;
	}

	public int getUser_Atencion() {
		return User_Atencion;
	}

	public void setUser_Atencion(int user_Atencion) {
		User_Atencion = user_Atencion;
	}

	public Date getFec_Alta_Reg() {
		return Fec_Alta_Reg;
	}

	public void setFec_Alta_Reg(Date fec_Alta_Reg) {
		Fec_Alta_Reg = fec_Alta_Reg;
	}

	public int getNum_Entidad_Financ() {
		return Num_Entidad_Financ;
	}

	public void setNum_Entidad_Financ(int num_Entidad_Financ) {
		Num_Entidad_Financ = num_Entidad_Financ;
	}

	public String getCustodio() {
		return Custodio;
	}

	public void setCustodio(String custodio) {
		Custodio = custodio;
	}

	public int getNo_Existe() {
		return No_Existe;
	}

	public void setNo_Existe(int no_Existe) {
		No_Existe = no_Existe;
	}

	public int getNum_Regis() {
		return Num_Regis;
	}

	public void setNum_Regis(int num_Regis) {
		Num_Regis = num_Regis;
	}

	public int getNum_Custodio() {
		return Num_Custodio;
	}

	public void setNum_Custodio(int num_Custodio) {
		Num_Custodio = num_Custodio;
	}

	public String getClave() {
		return Clave;
	}

	public void setClave(String clave) {
		Clave = clave;
	}

	public String getServicio() {
		return Servicio;
	}

	public void setServicio(String servicio) {
		Servicio = servicio;
	}
}
