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
@ManagedBean(name = "informacionCuentasParametrosLiquidacionBean")
@ViewScoped
public class InformacionCuentasParametrosLiquidacionBean implements Serializable {

    private String CBA_CVE_TIPO_CTA;
    private Integer CBA_NUM_PLAZA;
    private Integer CBA_NUM_SEC_CTA;
    private Integer CBA_NUM_BANCO;
    private Integer CBA_NUM_SUCURSAL;
    private Double CBA_NUM_CUENTA;
    private String CBA_CLABE;
    private String CVE_DESC_CLAVE;
    private Integer CBA_MONEDA;

    public String getCBA_CVE_TIPO_CTA() {
        return CBA_CVE_TIPO_CTA;
    }

    public void setCBA_CVE_TIPO_CTA(String CBA_CVE_TIPO_CTA) {
        this.CBA_CVE_TIPO_CTA = CBA_CVE_TIPO_CTA;
    }

    public Integer getCBA_NUM_PLAZA() {
        return CBA_NUM_PLAZA;
    }

    public void setCBA_NUM_PLAZA(Integer CBA_NUM_PLAZA) {
        this.CBA_NUM_PLAZA = CBA_NUM_PLAZA;
    }

    public Integer getCBA_NUM_SEC_CTA() {
        return CBA_NUM_SEC_CTA;
    }

    public void setCBA_NUM_SEC_CTA(Integer CBA_NUM_SEC_CTA) {
        this.CBA_NUM_SEC_CTA = CBA_NUM_SEC_CTA;
    }

    public Integer getCBA_NUM_BANCO() {
        return CBA_NUM_BANCO;
    }

    public void setCBA_NUM_BANCO(Integer CBA_NUM_BANCO) {
        this.CBA_NUM_BANCO = CBA_NUM_BANCO;
    }

    public Integer getCBA_NUM_SUCURSAL() {
        return CBA_NUM_SUCURSAL;
    }

    public void setCBA_NUM_SUCURSAL(Integer CBA_NUM_SUCURSAL) {
        this.CBA_NUM_SUCURSAL = CBA_NUM_SUCURSAL;
    }

    public Double getCBA_NUM_CUENTA() {
        return CBA_NUM_CUENTA;
    }

    public void setCBA_NUM_CUENTA(Double CBA_NUM_CUENTA) {
        this.CBA_NUM_CUENTA = CBA_NUM_CUENTA;
    }

    public String getCVE_DESC_CLAVE() {
        return CVE_DESC_CLAVE;
    }

    public void setCVE_DESC_CLAVE(String CVE_DESC_CLAVE) {
        this.CVE_DESC_CLAVE = CVE_DESC_CLAVE;
    }

    public Integer getCBA_MONEDA() {
        return CBA_MONEDA;
    }

    public void setCBA_MONEDA(Integer CBA_MONEDA) {
        this.CBA_MONEDA = CBA_MONEDA;
    }

    public String getCBA_CLABE() {
        return CBA_CLABE;
    }

    public void setCBA_CLABE(String CBA_CLABE) {
        this.CBA_CLABE = CBA_CLABE;
    }

}
