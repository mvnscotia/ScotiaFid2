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
@ManagedBean(name = "parametrosLiquidacionBean")
@ViewScoped
public class ParametrosLiquidacionBean implements Serializable {

    private String NOMBRE;
    private Integer PAL_CVE_TIPO_LIQ;
    private Integer PAL_NUM_MONEDA;
    private String PAL_CVE_TIPO_CTA;
    private Integer PAL_NUM_BANCO;
    private Integer PAL_NUM_PLAZA;
    private Integer PAL_NUM_SUCURSAL;
    private String PAL_NOM_SUCURSAL;
    private Double PAL_NUM_CUENTA;
    private Double PAL_CLABE;
    private Integer PAL_NUM_PAIS;
    private String PAL_CTA_BANXICO;
    private String PAL_DIR_APER_CTA;
    private String PAL_CODIGO_TRANS;
    private Integer PAL_NUM_INICIATIVA;
    private Integer PAL_NUM_CTAM;
    private Integer PAL_NUM_SCTA;
    private Integer PAL_NUM_SSCTA;
    private Integer PAL_NUM_SSSCTA;
    private String PAL_NOM_AREA;
    private String PAL_CONCEPTO;
    private Integer PAL_CUENTAS_ORDEN;
    private Integer PAL_ANO_ALTA_REG;
    private Integer PAL_MES_ALTA_REG;
    private Integer PAL_DIA_ALTA_REG;
    private Integer PAL_ANO_ULT_MOD;
    private Integer PAL_MES_ULT_MOD;
    private Integer PAL_DIA_ULT_MOD;
    private String PAL_CVE_ST_PALIQUI;
    private String CVE_DESC_CLAVE;
    private String MON_NOM_MONEDA;
    private String DESC_BANCO;

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public Integer getPAL_CVE_TIPO_LIQ() {
        return PAL_CVE_TIPO_LIQ;
    }

    public void setPAL_CVE_TIPO_LIQ(Integer PAL_CVE_TIPO_LIQ) {
        this.PAL_CVE_TIPO_LIQ = PAL_CVE_TIPO_LIQ;
    }

    public Integer getPAL_NUM_MONEDA() {
        return PAL_NUM_MONEDA;
    }

    public void setPAL_NUM_MONEDA(Integer PAL_NUM_MONEDA) {
        this.PAL_NUM_MONEDA = PAL_NUM_MONEDA;
    }

    public String getPAL_CVE_TIPO_CTA() {
        return PAL_CVE_TIPO_CTA;
    }

    public void setPAL_CVE_TIPO_CTA(String PAL_CVE_TIPO_CTA) {
        this.PAL_CVE_TIPO_CTA = PAL_CVE_TIPO_CTA;
    }

    public Integer getPAL_NUM_BANCO() {
        return PAL_NUM_BANCO;
    }

    public void setPAL_NUM_BANCO(Integer PAL_NUM_BANCO) {
        this.PAL_NUM_BANCO = PAL_NUM_BANCO;
    }

    public Integer getPAL_NUM_PLAZA() {
        return PAL_NUM_PLAZA;
    }

    public void setPAL_NUM_PLAZA(Integer PAL_NUM_PLAZA) {
        this.PAL_NUM_PLAZA = PAL_NUM_PLAZA;
    }

    public Integer getPAL_NUM_SUCURSAL() {
        return PAL_NUM_SUCURSAL;
    }

    public void setPAL_NUM_SUCURSAL(Integer PAL_NUM_SUCURSAL) {
        this.PAL_NUM_SUCURSAL = PAL_NUM_SUCURSAL;
    }

    public String getPAL_NOM_SUCURSAL() {
        return PAL_NOM_SUCURSAL;
    }

    public void setPAL_NOM_SUCURSAL(String PAL_NOM_SUCURSAL) {
        this.PAL_NOM_SUCURSAL = PAL_NOM_SUCURSAL;
    }

    public Double getPAL_NUM_CUENTA() {
        return PAL_NUM_CUENTA;
    }

    public void setPAL_NUM_CUENTA(Double PAL_NUM_CUENTA) {
        this.PAL_NUM_CUENTA = PAL_NUM_CUENTA;
    }

    public Integer getPAL_NUM_PAIS() {
        return PAL_NUM_PAIS;
    }

    public void setPAL_NUM_PAIS(Integer PAL_NUM_PAIS) {
        this.PAL_NUM_PAIS = PAL_NUM_PAIS;
    }

    public String getPAL_DIR_APER_CTA() {
        return PAL_DIR_APER_CTA;
    }

    public void setPAL_DIR_APER_CTA(String PAL_DIR_APER_CTA) {
        this.PAL_DIR_APER_CTA = PAL_DIR_APER_CTA;
    }

    public String getPAL_CODIGO_TRANS() {
        return PAL_CODIGO_TRANS;
    }

    public void setPAL_CODIGO_TRANS(String PAL_CODIGO_TRANS) {
        this.PAL_CODIGO_TRANS = PAL_CODIGO_TRANS;
    }

    public Integer getPAL_NUM_INICIATIVA() {
        return PAL_NUM_INICIATIVA;
    }

    public void setPAL_NUM_INICIATIVA(Integer PAL_NUM_INICIATIVA) {
        this.PAL_NUM_INICIATIVA = PAL_NUM_INICIATIVA;
    }

    public Integer getPAL_NUM_CTAM() {
        return PAL_NUM_CTAM;
    }

    public void setPAL_NUM_CTAM(Integer PAL_NUM_CTAM) {
        this.PAL_NUM_CTAM = PAL_NUM_CTAM;
    }

    public Integer getPAL_NUM_SCTA() {
        return PAL_NUM_SCTA;
    }

    public void setPAL_NUM_SCTA(Integer PAL_NUM_SCTA) {
        this.PAL_NUM_SCTA = PAL_NUM_SCTA;
    }

    public Integer getPAL_NUM_SSCTA() {
        return PAL_NUM_SSCTA;
    }

    public void setPAL_NUM_SSCTA(Integer PAL_NUM_SSCTA) {
        this.PAL_NUM_SSCTA = PAL_NUM_SSCTA;
    }

    public Integer getPAL_NUM_SSSCTA() {
        return PAL_NUM_SSSCTA;
    }

    public void setPAL_NUM_SSSCTA(Integer PAL_NUM_SSSCTA) {
        this.PAL_NUM_SSSCTA = PAL_NUM_SSSCTA;
    }

    public String getPAL_NOM_AREA() {
        return PAL_NOM_AREA;
    }

    public void setPAL_NOM_AREA(String PAL_NOM_AREA) {
        this.PAL_NOM_AREA = PAL_NOM_AREA;
    }

    public String getPAL_CONCEPTO() {
        return PAL_CONCEPTO;
    }

    public void setPAL_CONCEPTO(String PAL_CONCEPTO) {
        this.PAL_CONCEPTO = PAL_CONCEPTO;
    }

    public Integer getPAL_CUENTAS_ORDEN() {
        return PAL_CUENTAS_ORDEN;
    }

    public void setPAL_CUENTAS_ORDEN(Integer PAL_CUENTAS_ORDEN) {
        this.PAL_CUENTAS_ORDEN = PAL_CUENTAS_ORDEN;
    }

    public Integer getPAL_ANO_ALTA_REG() {
        return PAL_ANO_ALTA_REG;
    }

    public void setPAL_ANO_ALTA_REG(Integer PAL_ANO_ALTA_REG) {
        this.PAL_ANO_ALTA_REG = PAL_ANO_ALTA_REG;
    }

    public Integer getPAL_MES_ALTA_REG() {
        return PAL_MES_ALTA_REG;
    }

    public void setPAL_MES_ALTA_REG(Integer PAL_MES_ALTA_REG) {
        this.PAL_MES_ALTA_REG = PAL_MES_ALTA_REG;
    }

    public Integer getPAL_DIA_ALTA_REG() {
        return PAL_DIA_ALTA_REG;
    }

    public void setPAL_DIA_ALTA_REG(Integer PAL_DIA_ALTA_REG) {
        this.PAL_DIA_ALTA_REG = PAL_DIA_ALTA_REG;
    }

    public Integer getPAL_ANO_ULT_MOD() {
        return PAL_ANO_ULT_MOD;
    }

    public void setPAL_ANO_ULT_MOD(Integer PAL_ANO_ULT_MOD) {
        this.PAL_ANO_ULT_MOD = PAL_ANO_ULT_MOD;
    }

    public Integer getPAL_MES_ULT_MOD() {
        return PAL_MES_ULT_MOD;
    }

    public void setPAL_MES_ULT_MOD(Integer PAL_MES_ULT_MOD) {
        this.PAL_MES_ULT_MOD = PAL_MES_ULT_MOD;
    }

    public Integer getPAL_DIA_ULT_MOD() {
        return PAL_DIA_ULT_MOD;
    }

    public void setPAL_DIA_ULT_MOD(Integer PAL_DIA_ULT_MOD) {
        this.PAL_DIA_ULT_MOD = PAL_DIA_ULT_MOD;
    }

    public String getPAL_CVE_ST_PALIQUI() {
        return PAL_CVE_ST_PALIQUI;
    }

    public void setPAL_CVE_ST_PALIQUI(String PAL_CVE_ST_PALIQUI) {
        this.PAL_CVE_ST_PALIQUI = PAL_CVE_ST_PALIQUI;
    }

    public String getCVE_DESC_CLAVE() {
        return CVE_DESC_CLAVE;
    }

    public void setCVE_DESC_CLAVE(String CVE_DESC_CLAVE) {
        this.CVE_DESC_CLAVE = CVE_DESC_CLAVE;
    }

    public String getMON_NOM_MONEDA() {
        return MON_NOM_MONEDA;
    }

    public void setMON_NOM_MONEDA(String MON_NOM_MONEDA) {
        this.MON_NOM_MONEDA = MON_NOM_MONEDA;
    }

    public String getDESC_BANCO() {
        return DESC_BANCO;
    }

    public void setDESC_BANCO(String DESC_BANCO) {
        this.DESC_BANCO = DESC_BANCO;
    }

    public Double getPAL_CLABE() {
        return PAL_CLABE;
    }

    public void setPAL_CLABE(Double PAL_CLABE) {
        this.PAL_CLABE = PAL_CLABE;
    }

    public String getPAL_CTA_BANXICO() {
        return PAL_CTA_BANXICO;
    }

    public void setPAL_CTA_BANXICO(String PAL_CTA_BANXICO) {
        this.PAL_CTA_BANXICO = PAL_CTA_BANXICO;
    }


}
