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
@ManagedBean(name = "viewRecepcionCuentaBanco")
@ViewScoped
public class ViewRecepcionCuentaBancoBean implements Serializable{
    private String cuenta;
    private Integer banco;
    private Integer plaza;
    private Integer sucursal;
    private Integer numMoneda;
    private String descClave;
    private String nomMoneda;

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
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

    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal(Integer sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getNumMoneda() {
        return numMoneda;
    }

    public void setNumMoneda(Integer numMoneda) {
        this.numMoneda = numMoneda;
    }

    public String getDescClave() {
        return descClave;
    }

    public void setDescClave(String descClave) {
        this.descClave = descClave;
    }

    public String getNomMoneda() {
        return nomMoneda;
    }

    public void setNomMoneda(String nomMoneda) {
        this.nomMoneda = nomMoneda;
    }


}
