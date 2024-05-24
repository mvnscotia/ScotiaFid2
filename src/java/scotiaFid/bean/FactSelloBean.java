/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : FactSelloBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.sql.Date;

//Class
public class FactSelloBean implements Serializable {

	//Serial
	private static final long serialVersionUID = 1L;
	
    //Members
	private String 	facSistema;
	private Date 	facFecha;
	private int 	facNumContrato;
	private int 	facFolioFact;
	private int 	facNumMoneda;
	private String 	facCvePersFid;
	private int 	facNumPersFid;
	private String 	facCondiPago;
	private int 	facFolioTran;
	private double 	facHonorarios;
	private double 	facMoratorios;
	private double 	facIvaTraladado;
	private String 	facStatus;
	private String 	facStatusCte;
	private String 	facCtaPago;
	private String 	facNacionalidad;
	private String 	facCveTipoPer;
	private String 	facRFC;
	private String 	facRFCGenerico;
	private String 	facNombreRecep;
	private String 	facDomCalle;
	private String 	facDomNumExt;
	private String 	facDomNumInt;
	private String 	facDomColonia;
	private String 	facDomPoblacion;
	private String 	facDomReferencia;
	private String 	facDomEstado;
	private String 	facDomPais;
	private String 	facCodigoPostal;
	private String 	facRecepLocalidad;
	private Date 	facFechaInt;
	private double 	facTasaImpuesto;
	private String 	facMetPago;
	private String 	facMensaje;
	private String 	facUUIDRelacionado;
	private String  dif_regimenfiscal;
	private String  fac_uso_factura; 

	//Adicionales
	private String  Folio_Fac;  
	private String  desc_Regimen_Fiscal;
	
	private FdCanFactBean fdCanFactBean = new FdCanFactBean();
	
	//Constructor
	public FactSelloBean() {}
	
	//Getters_Setters
	public String getFacSistema() {
		return facSistema;
	}

	public void setFacSistema(String facSistema) {
		this.facSistema = facSistema;
	}

	public Date getFacFecha() {
		return facFecha;
	}

	public void setFacFecha(Date facFecha) {
		this.facFecha = facFecha;
	}

	public int getFacNumContrato() {
		return facNumContrato;
	}

	public void setFacNumContrato(int facNumContrato) {
		this.facNumContrato = facNumContrato;
	}

	public int getFacFolioFact() {
		return facFolioFact;
	}

	public void setFacFolioFact(int facFolioFact) {
		this.facFolioFact = facFolioFact;
	}

	public int getFacNumMoneda() {
		return facNumMoneda;
	}

	public void setFacNumMoneda(int facNumMoneda) {
		this.facNumMoneda = facNumMoneda;
	}

	public String getFacCvePersFid() {
		return facCvePersFid;
	}

	public void setFacCvePersFid(String facCvePersFid) {
		this.facCvePersFid = facCvePersFid;
	}

	public int getFacNumPersFid() {
		return facNumPersFid;
	}

	public void setFacNumPersFid(int facNumPersFid) {
		this.facNumPersFid = facNumPersFid;
	}

	public String getFacCondiPago() {
		return facCondiPago;
	}

	public void setFacCondiPago(String facCondiPago) {
		this.facCondiPago = facCondiPago;
	}

	public int getFacFolioTran() {
		return facFolioTran;
	}

	public void setFacFolioTran(int facFolioTran) {
		this.facFolioTran = facFolioTran;
	}

	public double getFacHonorarios() {
		return facHonorarios;
	}

	public void setFacHonorarios(double facHonorarios) {
		this.facHonorarios = facHonorarios;
	}

	public double getFacMoratorios() {
		return facMoratorios;
	}

	public void setFacMoratorios(double facMoratorios) {
		this.facMoratorios = facMoratorios;
	}

	public double getFacIvaTraladado() {
		return facIvaTraladado;
	}

	public void setFacIvaTraladado(double facIvaTraladado) {
		this.facIvaTraladado = facIvaTraladado;
	}

	public String getFacStatus() {
		return facStatus;
	}

	public void setFacStatus(String facStatus) {
		this.facStatus = facStatus;
	}

	public String getFacStatusCte() {
		return facStatusCte;
	}

	public void setFacStatusCte(String facStatusCte) {
		this.facStatusCte = facStatusCte;
	}

	public String getFacCtaPago() {
		return facCtaPago;
	}

	public void setFacCtaPago(String facCtaPago) {
		this.facCtaPago = facCtaPago;
	}

	public String getFacNacionalidad() {
		return facNacionalidad;
	}

	public void setFacNacionalidad(String facNacionalidad) {
		this.facNacionalidad = facNacionalidad;
	}

	public String getFacCveTipoPer() {
		return facCveTipoPer;
	}

	public void setFacCveTipoPer(String facCveTipoPer) {
		this.facCveTipoPer = facCveTipoPer;
	}

	public String getFacRFC() {
		return facRFC;
	}

	public void setFacRFC(String facRFC) {
		this.facRFC = facRFC;
	}

	public String getFacRFCGenerico() {
		return facRFCGenerico;
	}

	public void setFacRFCGenerico(String facRFCGenerico) {
		this.facRFCGenerico = facRFCGenerico;
	}

	public String getFacNombreRecep() {
		return facNombreRecep;
	}

	public void setFacNombreRecep(String facNombreRecep) {
		this.facNombreRecep = facNombreRecep;
	}

	public String getFacDomCalle() {
		return facDomCalle;
	}

	public void setFacDomCalle(String facDomCalle) {
		this.facDomCalle = facDomCalle;
	}

	public String getFacDomNumExt() {
		return facDomNumExt;
	}

	public void setFacDomNumExt(String facDomNumExt) {
		this.facDomNumExt = facDomNumExt;
	}

	public String getFacDomNumInt() {
		return facDomNumInt;
	}

	public void setFacDomNumInt(String facDomNumInt) {
		this.facDomNumInt = facDomNumInt;
	}
	
	public String getFacDomColonia() {
		return facDomColonia;
	}

	public void setFacDomColonia(String facDomColonia) {
		this.facDomColonia = facDomColonia;
	}

	public String getFacDomPoblacion() {
		return facDomPoblacion;
	}

	public void setFacDomPoblacion(String facDomPoblacion) {
		this.facDomPoblacion = facDomPoblacion;
	}

	public String getFacDomReferencia() {
		return facDomReferencia;
	}

	public void setFacDomReferencia(String facDomReferencia) {
		this.facDomReferencia = facDomReferencia;
	}

	public String getFacDomEstado() {
		return facDomEstado;
	}

	public void setFacDomEstado(String facDomEstado) {
		this.facDomEstado = facDomEstado;
	}

	public String getFacDomPais() {
		return facDomPais;
	}

	public void setFacDomPais(String facDomPais) {
		this.facDomPais = facDomPais;
	}

	public String getFacCodigoPostal() {
		return facCodigoPostal;
	}

	public void setFacCodigoPostal(String facCodigoPostal) {
		this.facCodigoPostal = facCodigoPostal;
	}

	public String getFacRecepLocalidad() {
		return facRecepLocalidad;
	}

	public void setFacRecepLocalidad(String facRecepLocalidad) {
		this.facRecepLocalidad = facRecepLocalidad;
	}

	public Date getFacFechaInt() {
		return facFechaInt;
	}

	public void setFacFechaInt(Date facFechaInt) {
		this.facFechaInt = facFechaInt;
	}

	public double getFacTasaImpuesto() {
		return facTasaImpuesto;
	}

	public void setFacTasaImpuesto(double facTasaImpuesto) {
		this.facTasaImpuesto = facTasaImpuesto;
	}

	public String getFacMetPago() {
		return facMetPago;
	}

	public void setFacMetPago(String facMetPago) {
		this.facMetPago = facMetPago;
	}

	public String getFacMensaje() {
		return facMensaje;
	}

	public void setFacMensaje(String facMensaje) {
		this.facMensaje = facMensaje;
	}

	public String getFacUUIDRelacionado() {
		return facUUIDRelacionado;
	}

	public void setFacUUIDRelacionado(String facUUIDRelacionado) {
		this.facUUIDRelacionado = facUUIDRelacionado;
	}

	public FdCanFactBean getFdCanFactBean() {
		return fdCanFactBean;
	}

	public void setFdCanFactBean(FdCanFactBean fdCanFactBean) {
		this.fdCanFactBean = fdCanFactBean;
	}
	
	public String getDif_regimenfiscal() {
		return dif_regimenfiscal;
	}
	
	public void setDif_regimenfiscal(String dif_regimenfiscal) {
		this.dif_regimenfiscal = dif_regimenfiscal;
	}
	
	public String getFac_uso_factura() {
		return fac_uso_factura;
	}
	
	public void setFac_uso_factura(String fac_uso_factura) {
		this.fac_uso_factura = fac_uso_factura;
	}
	
	//Adicionales
	public String getFolio_Fac() {
		return Folio_Fac;
	}
	
	public void setFolio_Fac(String folio_Fac) {
		this.Folio_Fac = folio_Fac;
	}
	
	public String getDesc_Regimen_Fiscal() {
		return desc_Regimen_Fiscal;
	}
	
	public void setDesc_Regimen_Fiscal(String desc_Regimen_Fiscal) {
		this.desc_Regimen_Fiscal = desc_Regimen_Fiscal;
	}

}
