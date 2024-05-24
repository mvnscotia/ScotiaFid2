/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : MonetariaLiquidacionBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20220301
 * MODIFICADO  : 20220301
 * AUTOR       : EFP 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author EFISOFT
 */
@ManagedBean(name = "beanMonLiqInstrucTercerosBean")
@ViewScoped
public class MonLiqInstrucTercerosBean implements Serializable{
    
    //Serial
    private static final long serialVersionUID = 1L;
    
   //Atributos privados Consulta de Instrucciones para Fideicomisos en pantalla Monetarias Liq
    private Long monLiqPtxNumTercero;
    private Double monLiqPtxImpFijoPago;
    private String monLiqPtxCvePerioPago;
    private Integer monLiqPtxDiaIniPagEx;
    private Integer monLiqPtxMesIniPagEx;
    private Integer monLiqPtxAnoIniPagEx;
    private String monLiqTerNomTercero;
    private String monLiqPtxCtaCheques;
    private String monLiqMonNomMoneda;
    private String monLiqPtxCveStParpate;
    private Integer monLiqMlFormaLiq;
    private Long monLiqMlNumCta;
    private Integer monLiqFolioInst;

    public Long getMonLiqPtxNumTercero() {
        return monLiqPtxNumTercero;
    }

    public void setMonLiqPtxNumTercero(Long monLiqPtxNumTercero) {
        this.monLiqPtxNumTercero = monLiqPtxNumTercero;
    }

    public Double getMonLiqPtxImpFijoPago() {
        return monLiqPtxImpFijoPago;
    }

    public void setMonLiqPtxImpFijoPago(Double monLiqPtxImpFijoPago) {
        this.monLiqPtxImpFijoPago = monLiqPtxImpFijoPago;
    }

    public String getMonLiqPtxCvePerioPago() {
        return monLiqPtxCvePerioPago;
    }

    public void setMonLiqPtxCvePerioPago(String monLiqPtxCvePerioPago) {
        this.monLiqPtxCvePerioPago = monLiqPtxCvePerioPago;
    }

    public Integer getMonLiqPtxDiaIniPagEx() {
        return monLiqPtxDiaIniPagEx;
    }

    public void setMonLiqPtxDiaIniPagEx(Integer monLiqPtxDiaIniPagEx) {
        this.monLiqPtxDiaIniPagEx = monLiqPtxDiaIniPagEx;
    }

    public Integer getMonLiqPtxMesIniPagEx() {
        return monLiqPtxMesIniPagEx;
    }

    public void setMonLiqPtxMesIniPagEx(Integer monLiqPtxMesIniPagEx) {
        this.monLiqPtxMesIniPagEx = monLiqPtxMesIniPagEx;
    }

    public Integer getMonLiqPtxAnoIniPagEx() {
        return monLiqPtxAnoIniPagEx;
    }

    public void setMonLiqPtxAnoIniPagEx(Integer monLiqPtxAnoIniPagEx) {
        this.monLiqPtxAnoIniPagEx = monLiqPtxAnoIniPagEx;
    }

    public String getMonLiqTerNomTercero() {
        return monLiqTerNomTercero;
    }

    public void setMonLiqTerNomTercero(String monLiqTerNomTercero) {
        this.monLiqTerNomTercero = monLiqTerNomTercero;
    }

    public String getMonLiqPtxCtaCheques() {
        return monLiqPtxCtaCheques;
    }

    public void setMonLiqPtxCtaCheques(String monLiqPtxCtaCheques) {
        this.monLiqPtxCtaCheques = monLiqPtxCtaCheques;
    }

    public String getMonLiqMonNomMoneda() {
        return monLiqMonNomMoneda;
    }

    public void setMonLiqMonNomMoneda(String monLiqMonNomMoneda) {
        this.monLiqMonNomMoneda = monLiqMonNomMoneda;
    }

    public String getMonLiqPtxCveStParpate() {
        return monLiqPtxCveStParpate;
    }

    public void setMonLiqPtxCveStParpate(String monLiqPtxCveStParpate) {
        this.monLiqPtxCveStParpate = monLiqPtxCveStParpate;
    }

    public Integer getMonLiqMlFormaLiq() {
        return monLiqMlFormaLiq;
    }

    public void setMonLiqMlFormaLiq(Integer monLiqMlFormaLiq) {
        this.monLiqMlFormaLiq = monLiqMlFormaLiq;
    }

    public Long getMonLiqMlNumCta() {
        return monLiqMlNumCta;
    }

    public void setMonLiqMlNumCta(Long monLiqMlNumCta) {
        this.monLiqMlNumCta = monLiqMlNumCta;
    }

    public Integer getMonLiqFolioInst() {
        return monLiqFolioInst;
    }

    public void setMonLiqFolioInst(Integer monLiqFolioInst) {
        this.monLiqFolioInst = monLiqFolioInst;
    }

    
    

    
}
