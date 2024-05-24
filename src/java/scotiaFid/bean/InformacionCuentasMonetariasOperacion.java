/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

/**
 *
 * @author lenovo
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "informacionCuentasMonetariasOperacion")
@ViewScoped
public class InformacionCuentasMonetariasOperacion {

    private String banco;
    private Integer plaza;
    private String cuenta;
    private String cuentaDes;
    private String clabe;
    private Integer sucursal;
    private Double saldo;
    private String Moneda;
    private Integer adcNumMoneda;
    private Integer adcBanco;

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
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
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        this.Moneda = Moneda;
    }

    public Integer getAdcNumMoneda() {
        return adcNumMoneda;
    }

    public void setAdcNumMoneda(Integer adcNumMoneda) {
        this.adcNumMoneda = adcNumMoneda;
    }

    public Integer getAdcBanco() {
        return adcBanco;
    }

    public void setAdcBanco(Integer adcBanco) {
        this.adcBanco = adcBanco;
    }

    public String getCuentaDes() {
        return cuentaDes;
    }

    public void setCuentaDes(String cuentaDes) {
        this.cuentaDes = cuentaDes;
    }

}
