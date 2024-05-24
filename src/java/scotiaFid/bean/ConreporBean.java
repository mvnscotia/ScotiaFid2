/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ConReporBean.java
 * DESCRIPCION : Tablas CONREPOR, almacenamiento de Reportos y Pagares
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

//Class
public class ConreporBean implements Serializable {
    // Serial

    private static final long serialVersionUID = 1L;

    // Members
    // Tabla CONREPOR
    private String fideicomiso;
    private String subFiso;
    private int entidadFinan; 	// Intermediario	
    private double ctoInver;		// Contrato de inversión
    private int FolioReporto;
    private int FolioOpera;
    private String Plazo;
    private int IdMercado;		//Clave Tipo Mercado
    private int IdInstrume;		//Numero de Instrumento
    private String Importe;
    private String Tasa;   		//Tasa Pactada
    private String Premio;
    private double TasaCastigo;
    private String estatus;
    private String custodio;
    private int moneda;
    private double tipoCambio;

    //Datos de Pantalla
    //MBReportos.java
    private String nomPizarra; 	// Pizarra
    private String serieEmis; 		// Serie	
    private String cuponVig; 		// Cupon
    private String diaOpera; 		// Fecha del movimiento
    private String titulos;
    private double precioEmis;

    //Beans para vencimiento de reportos
    private String tipoReporto;    //Tipo
    private int intermediarioReporto; //intermediario
    private String cuentaCheques;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private String nomMoneda;
    private String numeroCuenta;
    private String ctoIntervencion;
    private String negocioReporto;
    private short Chec_FechVal;

    // Getter_Setters
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

    public int getEntidadFinan() {
        return entidadFinan;
    }

    public void setEntidadFinan(int entidadFinan) {
        this.entidadFinan = entidadFinan;
    }

    public double getCtoInver() {
        return ctoInver;
    }

    public void setCtoInver(double ctoInver) {
        this.ctoInver = ctoInver;
    }

    public int getFolioReporto() {
        return FolioReporto;
    }

    public void setFolioReporto(int folioReporto) {
        FolioReporto = folioReporto;
    }

    public int getFolioOpera() {
        return FolioOpera;
    }

    public void setFolioOpera(int folioOpera) {
        FolioOpera = folioOpera;
    }

    public String getPlazo() {
        return Plazo;
    }

    public void setPlazo(String plazo) {
        Plazo = plazo;
    }

    public int getIdMercado() {
        return IdMercado;
    }

    public void setIdMercado(int idMercado) {
        IdMercado = idMercado;
    }

    public int getIdInstrume() {
        return IdInstrume;
    }

    public void setIdInstrume(int idInstrume) {
        IdInstrume = idInstrume;
    }

    public String getImporte() {
        return Importe;
    }

    public void setImporte(String Importe) {
        this.Importe = Importe;
    }

    public String getTasa() {
        return Tasa;
    }

    public void setTasa(String tasa) {
        Tasa = tasa;
    }

    public String getPremio() {
        return Premio;
    }

    public void setPremio(String premio) {
        Premio = premio;
    }

    public double getTasaCastigo() {
        return TasaCastigo;
    }

    public void setTasaCastigo(double tasaCastigo) {
        TasaCastigo = tasaCastigo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCustodio() {
        return custodio;
    }

    public void setCustodio(String custodio) {
        this.custodio = custodio;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getNomPizarra() {
        return nomPizarra;
    }

    public void setNomPizarra(String nomPizarra) {
        this.nomPizarra = nomPizarra;
    }

    public String getSerieEmis() {
        return serieEmis;
    }

    public void setSerieEmis(String serieEmis) {
        this.serieEmis = serieEmis;
    }

    public String getCuponVig() {
        return cuponVig;
    }

    public void setCuponVig(String cuponVig) {
        this.cuponVig = cuponVig;
    }

    public String getDiaOpera() {
        return diaOpera;
    }

    public void setDiaOpera(String diaOpera) {
        this.diaOpera = diaOpera;
    }

    public String getTitulos() {
        return titulos;
    }

    public void setTitulos(String titulos) {
        this.titulos = titulos;
    }

    public double getPrecioEmis() {
        return precioEmis;
    }

    public void setPrecioEmis(double precioEmis) {
        this.precioEmis = precioEmis;
    }

    public String getTipoReporto() {
        return tipoReporto;
    }

    public void setTipoReporto(String tipoReporto) {
        this.tipoReporto = tipoReporto;
    }

    public int getIntermediarioReporto() {
        return intermediarioReporto;
    }

    public void setIntermediarioReporto(int intermediarioReporto) {
        this.intermediarioReporto = intermediarioReporto;
    }

    public String getCuentaCheques() {
        return cuentaCheques;
    }

    public void setCuentaCheques(String cuentaCheques) {
        this.cuentaCheques = cuentaCheques;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNomMoneda() {
        return nomMoneda;
    }

    public void setNomMoneda(String nomMoneda) {
        this.nomMoneda = nomMoneda;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNegocioReporto() {
        return negocioReporto;
    }

    public void setNegocioReporto(String negocioReporto) {
        this.negocioReporto = negocioReporto;
    }

    public String getCtoIntervencion() {
        return ctoIntervencion;
    }

    public void setCtoIntervencion(String ctoIntervencion) {
        this.ctoIntervencion = ctoIntervencion;
    }

    public short getChec_FechVal() {
        return Chec_FechVal;
    }

    public void setChec_FechVal(short Chec_FechVal) {
        this.Chec_FechVal = Chec_FechVal;
    }
}
