/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CriterioBusquedaContAsienBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210421
 * MODIFICADO  : 20210831
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusquedaContaAsien")
@ViewScoped
public class CriterioBusquedaContaAsienBean implements Serializable{
  //Atributos privados
    private Long                       criterioAX1;
    private Long                       criterioAX2;
    private Long                       criterioAX3;
    private Short                      criterioCTAM;
    private Short                      criterioSC1;
    private Short                      criterioSC2;
    private Short                      criterioSC3;
    private Short                      criterioSC4;
    private Long                       criterioFolio;
    private Date                       criterioFechaDel;
    private Date                       criterioFechaAl;
    private Short                      criterioFechaDD;
    private Short                      criterioFechaMM;
    private Short                      criterioFechaYY;
    private String                     criterioFechaTipo;
    private String                     criterioStatus;
    private Integer                    criterioPlaza;
    private String                     criterioMonedaNom;
    private Short                      criterioMonedaNum;
    private Boolean                    criterioMuestraTotalCarAbo;
    private Integer                    criterioUsuario;    
    private String                     criterioDescripcion;
//validaciones 
    private String                     txtCriterioAX1;
    private String                     txtCriterioAX2;
    private String                     txtCriterioAX3;
    private String                     txtCriterioCTAM;
    private String                     txtCriterioSC1;
    private String                     txtCriterioSC2;
    private String                     txtCriterioSC3;
    private String                     txtCriterioSC4;
    private String                     txtCriterioFolio;
    private String                     txtCriterioFechaDD;
    private String                     txtCriterioFechaMM;
    private String                     txtCriterioFechaYY;
    private String                     txtCriterioPlaza;
    private String                     txtCriterioMonedaNum;
    private String                     txtCriterioUsuario;    

    
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

    public Long getCriterioFolio() {
        return criterioFolio;
    }

    public void setCriterioFolio(Long criterioFolio) {
        this.criterioFolio = criterioFolio;
    }

    public Date getCriterioFechaDel() {
        return criterioFechaDel;
    }

    public void setCriterioFechaDel(Date criterioFechaDel) {
        this.criterioFechaDel = criterioFechaDel;
    }

    public Date getCriterioFechaAl() {
        return criterioFechaAl;
    }

    public void setCriterioFechaAl(Date criterioFechaAl) {
        this.criterioFechaAl = criterioFechaAl;
    }

    public Short getCriterioFechaDD() {
        return criterioFechaDD;
    }

    public void setCriterioFechaDD(Short criterioFechaDD) {
        this.criterioFechaDD = criterioFechaDD;
    }

    public Short getCriterioFechaMM() {
        return criterioFechaMM;
    }

    public void setCriterioFechaMM(Short criterioFechaMM) {
        this.criterioFechaMM = criterioFechaMM;
    }

    public Short getCriterioFechaYY() {
        return criterioFechaYY;
    }

    public void setCriterioFechaYY(Short criterioFechaYY) {
        this.criterioFechaYY = criterioFechaYY;
    }

    public String getCriterioFechaTipo() {
        return criterioFechaTipo;
    }

    public void setCriterioFechaTipo(String criterioFechaTipo) {
        this.criterioFechaTipo = criterioFechaTipo;
    }

    public String getCriterioStatus() {
        return criterioStatus;
    }

    public void setCriterioStatus(String criterioStatus) {
        this.criterioStatus = criterioStatus;
    }

    public Integer getCriterioPlaza() {
        return criterioPlaza;
    }

    public void setCriterioPlaza(Integer criterioPlaza) {
        this.criterioPlaza = criterioPlaza;
    }

    public String getCriterioMonedaNom() {
        return criterioMonedaNom;
    }

    public void setCriterioMonedaNom(String criterioMonedaNom) {
        this.criterioMonedaNom = criterioMonedaNom;
    }

    public Short getCriterioMonedaNum() {
        return criterioMonedaNum;
    }

    public void setCriterioMonedaNum(Short criterioMonedaNum) {
        this.criterioMonedaNum = criterioMonedaNum;
    }

    public Boolean getCriterioMuestraTotalCarAbo() {
        return criterioMuestraTotalCarAbo;
    }

    public void setCriterioMuestraTotalCarAbo(Boolean criterioMuestraTotalCarAbo) {
        this.criterioMuestraTotalCarAbo = criterioMuestraTotalCarAbo;
    }
    
  //Constructor
    public CriterioBusquedaContaAsienBean() {
        
    }

    public Integer getCriterioUsuario() {
        return criterioUsuario;
    }

    public void setCriterioUsuario(Integer criterioUsuario) {
        this.criterioUsuario = criterioUsuario;
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

    public String getTxtCriterioFolio() {
        return txtCriterioFolio;
    }

    public void setTxtCriterioFolio(String txtCriterioFolio) {
        this.txtCriterioFolio = txtCriterioFolio;
    }

    public String getTxtCriterioFechaDD() {
        return txtCriterioFechaDD;
    }

    public void setTxtCriterioFechaDD(String txtCriterioFechaDD) {
        this.txtCriterioFechaDD = txtCriterioFechaDD;
    }

    public String getTxtCriterioFechaMM() {
        return txtCriterioFechaMM;
    }

    public void setTxtCriterioFechaMM(String txtCriterioFechaMM) {
        this.txtCriterioFechaMM = txtCriterioFechaMM;
    }

    public String getTxtCriterioFechaYY() {
        return txtCriterioFechaYY;
    }

    public void setTxtCriterioFechaYY(String txtCriterioFechaYY) {
        this.txtCriterioFechaYY = txtCriterioFechaYY;
    }

    public String getTxtCriterioPlaza() {
        return txtCriterioPlaza;
    }

    public void setTxtCriterioPlaza(String txtCriterioPlaza) {
        this.txtCriterioPlaza = txtCriterioPlaza;
    }

    public String getTxtCriterioMonedaNum() {
        return txtCriterioMonedaNum;
    }

    public void setTxtCriterioMonedaNum(String txtCriterioMonedaNum) {
        this.txtCriterioMonedaNum = txtCriterioMonedaNum;
    }

    public String getTxtCriterioUsuario() {
        return txtCriterioUsuario;
    }

    public void setTxtCriterioUsuario(String txtCriterioUsuario) {
        this.txtCriterioUsuario = txtCriterioUsuario;
    }

    public String getCriterioDescripcion() {
        return criterioDescripcion;
    }

    public void setCriterioDescripcion(String criterioDescripcion) {
        this.criterioDescripcion = criterioDescripcion;
    }
}