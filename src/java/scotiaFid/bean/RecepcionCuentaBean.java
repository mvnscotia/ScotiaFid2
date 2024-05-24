/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "recepcionCuentaBean")
@ViewScoped
public class RecepcionCuentaBean implements Serializable {

    private Integer iUsuario;
    private String sIdTerminal;
    private String sFormaOrigen;
    private Integer lNumContrato;
    private Integer lSubContrato;
    private String sDestinoDep;
    private String sFormaDep;
    private Date dtFechaRecep;
    private String sTipoPersona;
    private String sNombrePers;
    private Integer iNumPersona;
    private String sCtaCheques;
    private String sSucursal;
    private Integer lPlaza;
    private Integer iBanco;
    private String sNomBanco;
    private Integer iMoneda;
    private Double dImporteDep;
    private Integer dtTipoCamb;
    private Integer dtIVA;
    private String sConcepto;

    public Integer getiUsuario() {
        return iUsuario;
    }

    public void setiUsuario(Integer iUsuario) {
        this.iUsuario = iUsuario;
    }

    public String getsIdTerminal() {
        return sIdTerminal;
    }

    public void setsIdTerminal(String sIdTerminal) {
        this.sIdTerminal = sIdTerminal;
    }

    public String getsFormaOrigen() {
        return sFormaOrigen;
    }

    public void setsFormaOrigen(String sFormaOrigen) {
        this.sFormaOrigen = sFormaOrigen;
    }

    public Integer getlNumContrato() {
        return lNumContrato;
    }

    public void setlNumContrato(Integer lNumContrato) {
        this.lNumContrato = lNumContrato;
    }

    public Integer getlSubContrato() {
        return lSubContrato;
    }

    public void setlSubContrato(Integer lSubContrato) {
        this.lSubContrato = lSubContrato;
    }

    public String getsDestinoDep() {
        return sDestinoDep;
    }

    public void setsDestinoDep(String sDestinoDep) {
        this.sDestinoDep = sDestinoDep;
    }

    public String getsFormaDep() {
        return sFormaDep;
    }

    public void setsFormaDep(String sFormaDep) {
        this.sFormaDep = sFormaDep;
    }

    public Date getDtFechaRecep() {
        return dtFechaRecep;
    }

    public void setDtFechaRecep(Date dtFechaRecep) {
        this.dtFechaRecep = dtFechaRecep;
    }

    public String getsTipoPersona() {
        return sTipoPersona;
    }

    public void setsTipoPersona(String sTipoPersona) {
        this.sTipoPersona = sTipoPersona;
    }

    public String getsNombrePers() {
        return sNombrePers;
    }

    public void setsNombrePers(String sNombrePers) {
        this.sNombrePers = sNombrePers;
    }

    public Integer getiNumPersona() {
        return iNumPersona;
    }

    public void setiNumPersona(Integer iNumPersona) {
        this.iNumPersona = iNumPersona;
    }

    public String getsCtaCheques() {
        return sCtaCheques;
    }

    public void setsCtaCheques(String sCtaCheques) {
        this.sCtaCheques = sCtaCheques;
    }

    public String getsSucursal() {
        return sSucursal;
    }

    public void setsSucursal(String sSucursal) {
        this.sSucursal = sSucursal;
    }

    public Integer getlPlaza() {
        return lPlaza;
    }

    public void setlPlaza(Integer lPlaza) {
        this.lPlaza = lPlaza;
    }

    public Integer getiBanco() {
        return iBanco;
    }

    public void setiBanco(Integer iBanco) {
        this.iBanco = iBanco;
    }

    public String getsNomBanco() {
        return sNomBanco;
    }

    public void setsNomBanco(String sNomBanco) {
        this.sNomBanco = sNomBanco;
    }

    public Integer getiMoneda() {
        return iMoneda;
    }

    public void setiMoneda(Integer iMoneda) {
        this.iMoneda = iMoneda;
    }

    public Double getdImporteDep() {
        return dImporteDep;
    }

    public void setdImporteDep(Double dImporteDep) {
        this.dImporteDep = dImporteDep;
    }

    public Integer getDtTipoCamb() {
        return dtTipoCamb;
    }

    public void setDtTipoCamb(Integer dtTipoCamb) {
        this.dtTipoCamb = dtTipoCamb;
    }

    public Integer getDtIVA() {
        return dtIVA;
    }

    public void setDtIVA(Integer dtIVA) {
        this.dtIVA = dtIVA;
    }

    public String getsConcepto() {
        return sConcepto;
    }

    public void setsConcepto(String sConcepto) {
        this.sConcepto = sConcepto;
    }

}
