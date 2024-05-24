/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : DetCartBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class DetCartBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String fideicomiso;
   private String subFiso;
   private String formaPago;
   private String fechaCalculo;
   private String honorario;
   private String personaNombre;
   private String persona;
   private String moneda;
   private String adeudo;
   private String importe;
   private String importeTotal;
   private String iva;
   private String moratorios;
   private String ivaMoratorios;
   private String impPagos;
   private String calificacion;
   private String concepto;
   private String fechaDel;
   private String fechaAl;
   private String totalAdeudo;
   private String contratoInversion;
   private String periodoCobro;
   private String personaPaga;
   private String total;
   private String tipoOperacionContable;
   private int metodoPago;
   private int noPersona;
   private int noMoneda;
   private int secuencial;
   private int noPagos;
   private int impPagosImp;
   private int diasAtraso;
   private int intermediario;
   private int ctoInversion;
   private int servicio;
   private Date fecha;
   private double adeudoImp;
   private double importeImp;
   private double importeTotalImp;
   private double ivaImp;
   private double ivaMoratoriosImp;
   private double moratoriosImp;
   private double totalImp;
   private boolean especificaFactura;
    
   //Getters_Setters
   public String getFideicomiso() {
      return fideicomiso;
   }

   public void setFideicomiso(String fideicomiso) {
      this.fideicomiso = fideicomiso;
   }
   
   public String getSubFiso() {
	  return subFiso;
   }

   public void setSubFiso(String subFiso) {
	  this.subFiso = subFiso;
   }
   
   public String getFormaPago() {
	  return formaPago;
   }

   public void setFormaPago(String formaPago) {
	  this.formaPago = formaPago;
   }
   
   public int getMetodoPago() {
	  return metodoPago;
   }

   public void setMetodoPago(int metodoPago) {
	  this.metodoPago = metodoPago;
   }

   public String getFechaCalculo() {
	  return fechaCalculo;
   }

   public void setFechaCalculo(String fechaCalculo) {
	 this.fechaCalculo = fechaCalculo;
   }

   public Date getFecha() {
	 return fecha;
   }

   public void setFecha(Date fecha) {
	 this.fecha = fecha;
   }

   public String getHonorario() {
	 return honorario;
   }

   public void setHonorario(String honorario) {
	 this.honorario = honorario;
   }
   
   public String getPersonaNombre() {
	 return personaNombre;
   }

   public void setPersonaNombre(String personaNombre) {
	 this.personaNombre = personaNombre;
   }

   public String getPersona() {
	 return persona;
   }

   public void setPersona(String persona) {
	 this.persona = persona;
   }

   public int getNoPersona() {
	 return noPersona;
   }

   public void setNoPersona(int noPersona) {
	 this.noPersona = noPersona;
   }

   public String getMoneda() {
	 return moneda;
   }

   public void setMoneda(String moneda) {
	 this.moneda = moneda;
   }

   public int getNoMoneda() {
	 return noMoneda;
   }

   public void setNoMoneda(int noMoneda) {
	 this.noMoneda = noMoneda;
   }

   public int getSecuencial() {
	 return secuencial;
   }

   public void setSecuencial(int secuencial) {
	 this.secuencial = secuencial;
   }

   public String getAdeudo() {
	 return adeudo;
   }

   public void setAdeudo(String adeudo) {
	 this.adeudo = adeudo;
   }
   
   public double getImporteImp() {
	 return importeImp;
   }

   public void setImporteImp(double importeImp) {
	 this.importeImp = importeImp;
   }

   public String getImporte() {
	 return importe;
   }

   public void setImporte(String importe) {
	 this.importe = importe;
   }
   
   public double getImporteTotalImp() {
	 return importeTotalImp;
   }

   public void setImporteTotalImp(double importeTotalImp) {
	 this.importeTotalImp = importeTotalImp;
   }

   public String getImporteTotal() {
	 return importeTotal;
   }

   public void setImporteTotal(String importeTotal) {
	 this.importeTotal = importeTotal;
   }

   public double getIvaImp() {
	 return ivaImp;
   }

   public void setIvaImp(double ivaImp) {
	 this.ivaImp = ivaImp;
   }

   public String getIva() {
	 return iva;
   }

   public void setIva(String iva) {
	 this.iva = iva;
   }

   public String getMoratorios() {
	 return moratorios;
   }

   public void setMoratorios(String moratorios) {
	 this.moratorios = moratorios;
   }
   
   public double getMoratoriosImp() {
	 return moratoriosImp;
   }

   public void setMoratoriosImp(double moratoriosImp) {
	 this.moratoriosImp = moratoriosImp;
   }
   
   public String getIvaMoratorios() {
	 return ivaMoratorios;
   }

   public void setIvaMoratorios(String ivaMoratorios) {
	 this.ivaMoratorios = ivaMoratorios;
   }

   public double getIvaMoratoriosImp() {
	 return ivaMoratoriosImp;
   }

   public void setIvaMoratoriosImp(double ivaMoratoriosImp) {
	 this.ivaMoratoriosImp = ivaMoratoriosImp;
   }

   public int getNoPagos() {
	 return noPagos;
   }

   public void setNoPagos(int noPagos) {
	 this.noPagos = noPagos;
   }

   public String getImpPagos() {
	 return impPagos;
   }

   public void setImpPagos(String impPagos) {
	 this.impPagos = impPagos;
   }
   
   public int getImpPagosImp() {
	 return impPagosImp;
   }

   public void setImpPagosImp(int impPagosImp) {
	 this.impPagosImp = impPagosImp;
   }

   public String getCalificacion() {
	 return calificacion;
   }

   public void setCalificacion(String calificacion) {
	 this.calificacion = calificacion;
   }

   public String getConcepto() {
	 return concepto;
   }

   public void setConcepto(String concepto) {
	 this.concepto = concepto;
   }

   public String getFechaDel() {
	 return fechaDel;
   }

   public void setFechaDel(String fechaDel) {
	 this.fechaDel = fechaDel;
   }

   public String getFechaAl() {
	 return fechaAl;
   }

   public void setFechaAl(String fechaAl) {
	 this.fechaAl = fechaAl;
   }

   public int getDiasAtraso() {
	 return diasAtraso;
   }

   public void setDiasAtraso(int diasAtraso) {
	 this.diasAtraso = diasAtraso;
   }

   public String getTotalAdeudo() {
	 return totalAdeudo;
   }

   public void setTotalAdeudo(String totalAdeudo) {
	 this.totalAdeudo = totalAdeudo;
   }

   public double getAdeudoImp() {
	 return adeudoImp;
   }

   public void setAdeudoImp(double adeudoImp) {
	 this.adeudoImp = adeudoImp;
   }

   public String getContratoInversion() {
	 return contratoInversion;
   }

   public void setContratoInversion(String contratoInversion) {
	 this.contratoInversion = contratoInversion;
   }
   
   public int getIntermediario() {
	 return intermediario;
   }

   public void setIntermediario(int intermediario) {
	 this.intermediario = intermediario;
   }

   public int getCtoInversion() {
	 return ctoInversion;
   }

   public void setCtoInversion(int ctoInversion) {
	 this.ctoInversion = ctoInversion;
   }
   
   public int getServicio() {
	 return servicio;
   }

   public void setServicio(int servicio) {
	 this.servicio = servicio;
   }

   public String getPeriodoCobro() {
	 return periodoCobro;
   }

   public void setPeriodoCobro(String periodoCobro) {
	 this.periodoCobro = periodoCobro;
   }
   
   public String getPersonaPaga() {
	 return personaPaga;
   }

   public void setPersonaPaga(String personaPaga) {
	 this.personaPaga = personaPaga;
   }
   
   public String getTotal() {
	 return total;
   }

   public void setTotal(String total) {
	 this.total = total;
   }

   public double getTotalImp() {
	 return totalImp;
   }

   public void setTotalImp(double totalImp) {
	 this.totalImp = totalImp;
   }
   
   public String getTipoOperacionContable() {
	 return tipoOperacionContable;
   }

   public void setTipoOperacionContable(String tipoOperacionContable) {
	  this.tipoOperacionContable = tipoOperacionContable;
   }

   public boolean isEspecificaFactura() {
	 return especificaFactura;
   }

   public void setEspecificaFactura(boolean especificaFactura) {
	 this.especificaFactura = especificaFactura;
   }
}
