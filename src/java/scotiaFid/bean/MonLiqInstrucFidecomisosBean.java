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
@ManagedBean(name = "beanMonLiqInstrucFidecomisosBean")
@ViewScoped
public class MonLiqInstrucFidecomisosBean implements Serializable{
    
    //Serial
    private static final long serialVersionUID = 1L;
    
   //Atributos privados Consulta de Instrucciones para Fideicomisos en pantalla Monetarias Liq
    private Long monLiqPbxBeneficiario;
    private String monLiqBenNomBenef;
    private Double monLiqPbxImpFijoPago;
    private String monLiqPbxCvePerPago;
    private Integer monLiqPbxDiaIniPago;
    private Integer monLiqPbxMesIniPago;
    private Integer monLiqPbxAnoIniPago;
    private Integer monLiqMlNumMoneda;
    private Integer monLiqMlFormaLiq;
    private String monLiqMlNumCta;
    private String monLiqPbxCtaCheques;
    private String monLiqMonNomMoneda;
    private String monLiqPbxCveStParpabe;

    public Long getMonLiqPbxBeneficiario() {
        return monLiqPbxBeneficiario;
    }

    public void setMonLiqPbxBeneficiario(Long monLiqPbxBeneficiario) {
        this.monLiqPbxBeneficiario = monLiqPbxBeneficiario;
    }

    public String getMonLiqBenNomBenef() {
        return monLiqBenNomBenef;
    }

    public void setMonLiqBenNomBenef(String monLiqBenNomBenef) {
        this.monLiqBenNomBenef = monLiqBenNomBenef;
    }

    public Double getMonLiqPbxImpFijoPago() {
        return monLiqPbxImpFijoPago;
    }

    public void setMonLiqPbxImpFijoPago(Double monLiqPbxImpFijoPago) {
        this.monLiqPbxImpFijoPago = monLiqPbxImpFijoPago;
    }

    public String getMonLiqPbxCvePerPago() {
        return monLiqPbxCvePerPago;
    }

    public void setMonLiqPbxCvePerPago(String monLiqPbxCvePerPago) {
        this.monLiqPbxCvePerPago = monLiqPbxCvePerPago;
    }

    public Integer getMonLiqPbxDiaIniPago() {
        return monLiqPbxDiaIniPago;
    }

    public void setMonLiqPbxDiaIniPago(Integer monLiqPbxDiaIniPago) {
        this.monLiqPbxDiaIniPago = monLiqPbxDiaIniPago;
    }

    public Integer getMonLiqPbxMesIniPago() {
        return monLiqPbxMesIniPago;
    }

    public void setMonLiqPbxMesIniPago(Integer monLiqPbxMesIniPago) {
        this.monLiqPbxMesIniPago = monLiqPbxMesIniPago;
    }

    public Integer getMonLiqPbxAnoIniPago() {
        return monLiqPbxAnoIniPago;
    }

    public void setMonLiqPbxAnoIniPago(Integer monLiqPbxAnoIniPago) {
        this.monLiqPbxAnoIniPago = monLiqPbxAnoIniPago;
    }
    
    public Integer getMonLiqMlNumMoneda() {
        return monLiqMlNumMoneda;
    }

    public void setMonLiqMlNumMoneda(Integer monLiqMlNumMoneda) {
        this.monLiqMlNumMoneda = monLiqMlNumMoneda;
    }    

    public Integer getMonLiqMlFormaLiq() {
        return monLiqMlFormaLiq;
    }

    public void setMonLiqMlFormaLiq(Integer monLiqMlFormaLiq) {
        this.monLiqMlFormaLiq = monLiqMlFormaLiq;
    }

    public String getMonLiqMlNumCta() {
        return monLiqMlNumCta;
    }

    public void setMonLiqMlNumCta(String monLiqMlNumCta) {
        this.monLiqMlNumCta = monLiqMlNumCta;
    }

    public String getMonLiqPbxCtaCheques() {
        return monLiqPbxCtaCheques;
    }

    public void setMonLiqPbxCtaCheques(String monLiqPbxCtaCheques) {
        this.monLiqPbxCtaCheques = monLiqPbxCtaCheques;
    }

    public String getMonLiqMonNomMoneda() {
        return monLiqMonNomMoneda;
    }

    public void setMonLiqMonNomMoneda(String monLiqMonNomMoneda) {
        this.monLiqMonNomMoneda = monLiqMonNomMoneda;
    }

    public String getMonLiqPbxCveStParpabe() {
        return monLiqPbxCveStParpabe;
    }

    public void setMonLiqPbxCveStParpabe(String monLiqPbxCveStParpabe) {
        this.monLiqPbxCveStParpabe = monLiqPbxCveStParpabe;
    }


    
}
