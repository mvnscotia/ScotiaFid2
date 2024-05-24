/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadSaldoPromBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20211013
 * MODIFICADO  : 20211013
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadSaldoProm")
@ViewScoped
public class ContabilidadSaldoPromBean implements Serializable{
  //Atributos privados
    private Long                       saldoPromContratoNum;
    private Long                       saldoPromContratoNumSub;
    private String                     saldoPromContratoNom;
    private Date                       saldoPromFecha;
    private Double                     saldoPromActivo;
    private Double                     saldoPromPatrim;
    private Double                     saldoPromInv;
    private Double                     saldoPromTotal;
    
  //Getters y Setters
    public Long getSaldoPromContratoNum() {
        return saldoPromContratoNum;
    }

    public void setSaldoPromContratoNum(Long saldoPromContratoNum) {
        this.saldoPromContratoNum = saldoPromContratoNum;
    }

    public Long getSaldoPromContratoNumSub() {
        return saldoPromContratoNumSub;
    }

    public void setSaldoPromContratoNumSub(Long saldoPromContratoNumSub) {
        this.saldoPromContratoNumSub = saldoPromContratoNumSub;
    }

    public String getSaldoPromContratoNom() {
        return saldoPromContratoNom;
    }

    public void setSaldoPromContratoNom(String saldoPromContratoNom) {
        this.saldoPromContratoNom = saldoPromContratoNom;
    }

    public Date getSaldoPromFecha() {
        return saldoPromFecha;
    }

    public void setSaldoPromFecha(Date saldoPromFecha) {
        this.saldoPromFecha = saldoPromFecha;
    }

    public Double getSaldoPromActivo() {
        return saldoPromActivo;
    }

    public void setSaldoPromActivo(Double saldoPromActivo) {
        this.saldoPromActivo = saldoPromActivo;
    }

    public Double getSaldoPromPatrim() {
        return saldoPromPatrim;
    }

    public void setSaldoPromPatrim(Double saldoPromPatrim) {
        this.saldoPromPatrim = saldoPromPatrim;
    }

    public Double getSaldoPromInv() {
        return saldoPromInv;
    }

    public void setSaldoPromInv(Double saldoPromInv) {
        this.saldoPromInv = saldoPromInv;
    }

    public Double getSaldoPromTotal() {
        return saldoPromTotal;
    }

    public void setSaldoPromTotal(Double saldoPromTotal) {
        this.saldoPromTotal = saldoPromTotal;
    }
    
  //Constructor
    public ContabilidadSaldoPromBean() {
        
    }

    public ContabilidadSaldoPromBean(Long saldoPromContratoNum, Long saldoPromContratoNumSub, String saldoPromContratoNom, Date saldoPromFecha, Double saldoPromActivo, Double saldoPromPatrim, Double saldoPromInv, Double saldoPromTotal) {
        this.saldoPromContratoNum    = saldoPromContratoNum;
        this.saldoPromContratoNumSub = saldoPromContratoNumSub;
        this.saldoPromContratoNom    = saldoPromContratoNom;
        this.saldoPromFecha          = saldoPromFecha;
        this.saldoPromActivo         = saldoPromActivo;
        this.saldoPromPatrim         = saldoPromPatrim;
        this.saldoPromInv            = saldoPromInv;
        this.saldoPromTotal          = saldoPromTotal;
    }
}