/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scotiaFid.bean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.sql.Date;
/**
 *
 * @author s5851865
 */
@ManagedBean(name = "beanReporteCanSaldo")
@ViewScoped
public class ReporteCanSaldosBean {
    private Long                       saldAx1;
    private String                     tipMovto;
    private Integer                    cconCta;   
    private String                     ctoNomContrato;
    private Integer                    moneIdMoneda;
    private Integer                    secMovto;
    private String                     monNomMoneda;
    private Integer                    cconsCta;
    private Integer                    ccons2Cta;
    private Integer                    ccons3Cta;
    private Integer                    ccons4Cta;
    private Integer                    saldAx2;
    private Integer                    saldAx3;
    private Double                     saldSaldoIniSim;
    private Double                     saldSaldoActual;
    private Long                       detmFolioOp;
    private Integer                    asieSecAsiento;
    private String                     asieCarAbo;
    private Double                     asiImporte;
    private Date                       saldFecAplica;
    private String                     saldEstatus;

    public Long getSaldAx1() {
        return saldAx1;
    }

    public void setSaldAx1(Long saldAx1) {
        this.saldAx1 = saldAx1;
    }

    public String getTipMovto() {
        return tipMovto;
    }

    public void setTipMovto(String tipMovto) {
        this.tipMovto = tipMovto;
    }

    public Integer getCconCta() {
        return cconCta;
    }

    public void setCconCta(Integer cconCta) {
        this.cconCta = cconCta;
    }

    public String getCtoNomContrato() {
        return ctoNomContrato;
    }

    public void setCtoNomContrato(String ctoNomContrato) {
        this.ctoNomContrato = ctoNomContrato;
    }

    public Integer getMoneIdMoneda() {
        return moneIdMoneda;
    }

    public void setMoneIdMoneda(Integer moneIdMoneda) {
        this.moneIdMoneda = moneIdMoneda;
    }

    public Integer getSecMovto() {
        return secMovto;
    }

    public void setSecMovto(Integer secMovto) {
        this.secMovto = secMovto;
    }

    public String getMonNomMoneda() {
        return monNomMoneda;
    }

    public void setMonNomMoneda(String monNomMoneda) {
        this.monNomMoneda = monNomMoneda;
    }

    public Integer getCconsCta() {
        return cconsCta;
    }

    public void setCconsCta(Integer cconsCta) {
        this.cconsCta = cconsCta;
    }

    public Integer getCcons2Cta() {
        return ccons2Cta;
    }

    public void setCcons2Cta(Integer ccons2Cta) {
        this.ccons2Cta = ccons2Cta;
    }

    public Integer getCcons3Cta() {
        return ccons3Cta;
    }

    public void setCcons3Cta(Integer ccons3Cta) {
        this.ccons3Cta = ccons3Cta;
    }

    public Integer getCcons4Cta() {
        return ccons4Cta;
    }

    public void setCcons4Cta(Integer ccons4Cta) {
        this.ccons4Cta = ccons4Cta;
    }

    public Integer getSaldAx2() {
        return saldAx2;
    }

    public void setSaldAx2(Integer saldAx2) {
        this.saldAx2 = saldAx2;
    }

    public Integer getSaldAx3() {
        return saldAx3;
    }

    public void setSaldAx3(Integer saldAx3) {
        this.saldAx3 = saldAx3;
    }

    public Double getSaldSaldoIniSim() {
        return saldSaldoIniSim;
    }

    public void setSaldSaldoIniSim(Double saldSaldoIniSim) {
        this.saldSaldoIniSim = saldSaldoIniSim;
    }

    public Double getSaldSaldoActual() {
        return saldSaldoActual;
    }

    public void setSaldSaldoActual(Double saldSaldoActual) {
        this.saldSaldoActual = saldSaldoActual;
    }

    public Long getDetmFolioOp() {
        return detmFolioOp;
    }

    public void setDetmFolioOp(Long detmFolioOp) {
        this.detmFolioOp = detmFolioOp;
    }

    public Integer getAsieSecAsiento() {
        return asieSecAsiento;
    }

    public void setAsieSecAsiento(Integer asieSecAsiento) {
        this.asieSecAsiento = asieSecAsiento;
    }

    public String getAsieCarAbo() {
        return asieCarAbo;
    }

    public void setAsieCarAbo(String asieCarAbo) {
        this.asieCarAbo = asieCarAbo;
    }

    public Double getAsiImporte() {
        return asiImporte;
    }

    public void setAsiImporte(Double asiImporte) {
        this.asiImporte = asiImporte;
    }

    public Date getSaldFecAplica() {
        return saldFecAplica;
    }

    public void setSaldFecAplica(Date saldFecAplica) {
        this.saldFecAplica = saldFecAplica;
    }

    public String getSaldEstatus() {
        return saldEstatus;
    }

    public void setSaldEstatus(String saldEstatus) {
        this.saldEstatus = saldEstatus;
    }
    
    
}
