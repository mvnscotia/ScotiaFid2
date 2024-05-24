/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CriterioBusquedaContaSaldoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210421
 * MODIFICADO  : 20210901
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210901.- Se ajusta tipo de dato, pasamos de Integer a Short
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusquedaContaSaldo")
@ViewScoped
public class CriterioBusquedaContaSaldoBean implements Serializable{
  //Atributos privados
    private Long                       criterioAX1;
    private Long                       criterioAX2;
    private Long                       criterioAX3;
    private Short                      criterioCTAM;
    private Short                      criterioSC1;
    private Short                      criterioSC2;
    private Short                      criterioSC3;
    private Short                      criterioSC4;
    private Short                      criterioMes;
    private Short                      criterioAño;
    private String                     criterioTipo;
    private String                     criterioMonedaNom;
    private String                     criterioMonedaNum;
    private Integer                    criterioUsuario;
    private String                     criterioNomContrato;

    private String                     txtCriterioAX1;
    private String                     txtCriterioAX2;
    private String                     txtCriterioAX3;
    private String                     txtCriterioCTAM;
    private String                     txtCriterioSC1;
    private String                     txtCriterioSC2;
    private String                     txtCriterioSC3;
    private String                     txtCriterioSC4;
    private String                     txtCriterioMes;
    private String                     txtCriterioAño;
    private String                     txtCriterioUsuario;


    public Integer getCriterioUsuario() {
        return criterioUsuario;
    }

    public void setCriterioUsuario(Integer criterioUsuario) {
        this.criterioUsuario = criterioUsuario;
    }
    
  //Getters y Setters
    public Long getCriterioAX1() {
        return criterioAX1;
    }

    public void setCriterioAX1(Long criterioAX1) {
        this.criterioAX1 = criterioAX1;
    }

    public Long getCriterioAX2() {
        return criterioAX2;
    }

    public void setCriterioAX2(Long criterioAX2) {
        this.criterioAX2 = criterioAX2;
    }

    public Long getCriterioAX3() {
        return criterioAX3;
    }

    public void setCriterioAX3(Long criterioAX3) {
        this.criterioAX3 = criterioAX3;
    }

    public Short getCriterioCTAM() {
        return criterioCTAM;
    }

    public void setCriterioCTAM(Short criterioCTAM) {
        this.criterioCTAM = criterioCTAM;
    }

    public Short getCriterioSC1() {
        return criterioSC1;
    }

    public void setCriterioSC1(Short criterioSC1) {
        this.criterioSC1 = criterioSC1;
    }

    public Short getCriterioSC2() {
        return criterioSC2;
    }

    public void setCriterioSC2(Short criterioSC2) {
        this.criterioSC2 = criterioSC2;
    }

    public Short getCriterioSC3() {
        return criterioSC3;
    }

    public void setCriterioSC3(Short criterioSC3) {
        this.criterioSC3 = criterioSC3;
    }

    public Short getCriterioSC4() {
        return criterioSC4;
    }

    public void setCriterioSC4(Short criterioSC4) {
        this.criterioSC4 = criterioSC4;
    }

    public Short getCriterioMes() {
        return criterioMes;
    }

    public void setCriterioMes(Short criterioMes) {
        this.criterioMes = criterioMes;
    }

    public Short getCriterioAño() {
        return criterioAño;
    }

    public void setCriterioAño(Short criterioAño) {
        this.criterioAño = criterioAño;
    }

    public String getCriterioTipo() {
        return criterioTipo;
    }

    public void setCriterioTipo(String criterioTipo) {
        this.criterioTipo = criterioTipo;
    }

    public String getCriterioMonedaNom() {
        return criterioMonedaNom;
    }

    public void setCriterioMonedaNom(String criterioMonedaNom) {
        this.criterioMonedaNom = criterioMonedaNom;
    }

    public String getCriterioMonedaNum() {
        return criterioMonedaNum;
    }

    public void setCriterioMonedaNum(String criterioMonedaNum) {
        this.criterioMonedaNum = criterioMonedaNum;
    }
    
  //Constructor
    public CriterioBusquedaContaSaldoBean() {
        
    }

    public String getTxtCriterioAX1() {
        return txtCriterioAX1;
    }

    public void setTxtCriterioAX1(String txtCriterioAX1) {
        this.txtCriterioAX1 = txtCriterioAX1;
    }

    public String getTxtCriterioAX2() {
        return txtCriterioAX2;
    }

    public void setTxtCriterioAX2(String txtCriterioAX2) {
        this.txtCriterioAX2 = txtCriterioAX2;
    }

    public String getTxtCriterioAX3() {
        return txtCriterioAX3;
    }

    public void setTxtCriterioAX3(String txtCriterioAX3) {
        this.txtCriterioAX3 = txtCriterioAX3;
    }

    public String getTxtCriterioCTAM() {
        return txtCriterioCTAM;
    }

    public void setTxtCriterioCTAM(String txtCriterioCTAM) {
        this.txtCriterioCTAM = txtCriterioCTAM;
    }

    public String getTxtCriterioSC1() {
        return txtCriterioSC1;
    }

    public void setTxtCriterioSC1(String txtCriterioSC1) {
        this.txtCriterioSC1 = txtCriterioSC1;
    }

    public String getTxtCriterioSC2() {
        return txtCriterioSC2;
    }

    public void setTxtCriterioSC2(String txtCriterioSC2) {
        this.txtCriterioSC2 = txtCriterioSC2;
    }

    public String getTxtCriterioSC3() {
        return txtCriterioSC3;
    }

    public void setTxtCriterioSC3(String txtCriterioSC3) {
        this.txtCriterioSC3 = txtCriterioSC3;
    }

    public String getTxtCriterioSC4() {
        return txtCriterioSC4;
    }

    public void setTxtCriterioSC4(String txtCriterioSC4) {
        this.txtCriterioSC4 = txtCriterioSC4;
    }

    public String getTxtCriterioMes() {
        return txtCriterioMes;
    }

    public void setTxtCriterioMes(String txtCriterioMes) {
        this.txtCriterioMes = txtCriterioMes;
    }

    public String getTxtCriterioAño() {
        return txtCriterioAño;
    }

    public void setTxtCriterioAño(String txtCriterioAño) {
        this.txtCriterioAño = txtCriterioAño;
    }

    public String getTxtCriterioUsuario() {
        return txtCriterioUsuario;
    }

    public void setTxtCriterioUsuario(String txtCriterioUsuario) {
        this.txtCriterioUsuario = txtCriterioUsuario;
    }

    public String getCriterioNomContrato() {
        return criterioNomContrato;
    }

    public void setCriterioNomContrato(String criterioNomContrato) {
        this.criterioNomContrato = criterioNomContrato;
    }

}