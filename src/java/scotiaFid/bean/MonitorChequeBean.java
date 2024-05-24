/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "monitorCheque")
@ViewScoped
public class MonitorChequeBean implements Serializable {

    private String referencia;
    private String tipoMovimiento;
    private Double importe;
    private String numerocuenta;
    private Integer banco;
    private String bancoNombre;
    private Integer plaza;
    private String cuenta;
    private String clabe;
    private Integer sucursal;
    private Double saldo;
    private String moneda;
    private Integer adcNumMoneda;

    public MonitorChequeBean(String referencia, String tipoMovimiento, Double importe, String numerocuenta) {
        this.referencia = referencia;
        this.tipoMovimiento = tipoMovimiento;
        this.importe = importe;
        this.numerocuenta = numerocuenta;
    }

    public String getBancoNombre() {
        return bancoNombre;
    }

    public void setBancoNombre(String bancoNombre) {
        this.bancoNombre = bancoNombre;
    }


    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getNumerocuenta() {
        return numerocuenta;
    }

    public void setNumerocuenta(String numerocuenta) {
        this.numerocuenta = numerocuenta;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public Integer getPlaza() {
        return plaza;
    }

    public void setPlaza(Integer plaza) {
        this.plaza = plaza;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
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

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Integer getAdcNumMoneda() {
        return adcNumMoneda;
    }

    public void setAdcNumMoneda(Integer adcNumMoneda) {
        this.adcNumMoneda = adcNumMoneda;
    }
    //Constructor
    public MonitorChequeBean() {
    }

}
