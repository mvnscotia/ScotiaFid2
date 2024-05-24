/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ResponseMonitorBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.List;

//Class
public class ResponseMonitorBean implements Serializable {
    // Serial

    private static final long serialVersionUID = 1L;

    // Members
    private String contrato;
    private String moneda;
    private String estatus;
    private String monto;
    private String montoFormat;
    private String saldo;
    private String saldoFormat;
    private String tipoTransaccion;
    private String descripcion;
    private String confirmacion;
    private String referencia;
    private String cuenta;
    private String aplicado;
    private String codigo;
    private List<ResponseMonitorBean> lstResponse;
    private Integer plaza;
    private String banco;
    private String clabe;
    private Integer sucursal;
    private Integer adcNumMoneda;
    private Double tipoCambio;
    private String tipoOperacionDesc;
    private String recepcion;
    private Double iva;

    public String getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(String recepcion) {
        this.recepcion = recepcion;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }
    
    public Double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    // Getter_Setters
    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getMontoFormat() {
        return montoFormat;
    }

    public void setMontoFormat(String montoFormat) {
        this.montoFormat = montoFormat;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getSaldoFormat() {
        return saldoFormat;
    }

    public void setSaldoFormat(String saldoFormat) {
        this.saldoFormat = saldoFormat;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getAplicado() {
        return aplicado;
    }

    public void setAplicado(String aplicado) {
        this.aplicado = aplicado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<ResponseMonitorBean> getLstResponse() {
        return lstResponse;
    }

    public void setLstResponse(List<ResponseMonitorBean> lstResponse) {
        this.lstResponse = lstResponse;
    }

    public Integer getPlaza() {
        return plaza;
    }

    public void setPlaza(Integer plaza) {
        this.plaza = plaza;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getClabe() {
        return clabe;
    }

    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal(Integer sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getAdcNumMoneda() {
        return adcNumMoneda;
    }

    public void setAdcNumMoneda(Integer adcNumMoneda) {
        this.adcNumMoneda = adcNumMoneda;
    }

    public String getTipoOperacionDesc() {
        return tipoOperacionDesc;
    }

    public void setTipoOperacionDesc(String tipoOperacionDesc) {
        this.tipoOperacionDesc = tipoOperacionDesc;
    }


}
