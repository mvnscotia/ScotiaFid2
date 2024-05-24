/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CriterioBusquedaContaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210406
 * MODIFICADO  : 20211007
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20211007.- Se agrega atributo criterioUsuario
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusquedaInstruccionesFideicomiso")
@ViewScoped
public class CriterioBusquedaInstruccionesFideicomisoBean implements Serializable{
    //Atributos privados efp
    private Long                                criterioBusqInstrucFideicomiso;
    private Integer                             criterioBusqInstrucFideSubCto;
    private Integer                             criterioBusqInstrucFideFolioInst;
    private String                              criterioBusqInstrucFideFolioDocto;
    private String                              criterioBusqInstrucFideNomFideicomiso;
    private String                              criterioBusqInstrucFideTipo;
    private Integer                             criterioBusqInstrucFidePlaza;

    private String                              txtCriterioBusqInstrucFideicomiso;
    private String                              txtCriterioBusqInstrucFideSubCto;
    private String                              txtCriterioBusqInstrucFideFolioInst;
    private String                              txtCriterioBusqInstrucFidePlaza;
    
    //Contant
    private String                               criterioBusqInstrucFideInsCveStInstruc;
    
    //Getetrs y Setters
    public Long getCriterioBusqInstrucFideicomiso() {
        return criterioBusqInstrucFideicomiso;
    }
    public void setCriterioBusqInstrucFideicomiso(Long criterioBusqInstrucFideicomiso) {
        this.criterioBusqInstrucFideicomiso = criterioBusqInstrucFideicomiso;
    }    

    public Integer getCriterioBusqInstrucFideSubCto() {
        return criterioBusqInstrucFideSubCto;
    }

    public void setCriterioBusqInstrucFideSubCto(Integer criterioBusqInstrucFideSubCto) {
        this.criterioBusqInstrucFideSubCto = criterioBusqInstrucFideSubCto;
    }

    public Integer getCriterioBusqInstrucFideFolioInst() {
        return criterioBusqInstrucFideFolioInst;
    }

    public void setCriterioBusqInstrucFideFolioInst(Integer criterioBusqInstrucFideFolioInst) {
        this.criterioBusqInstrucFideFolioInst = criterioBusqInstrucFideFolioInst;
    }

    public String getCriterioBusqInstrucFideFolioDocto() {
        return criterioBusqInstrucFideFolioDocto;
    }

    public void setCriterioBusqInstrucFideFolioDocto(String criterioBusqInstrucFideFolioDocto) {
        this.criterioBusqInstrucFideFolioDocto = criterioBusqInstrucFideFolioDocto;
    }

    public String getCriterioBusqInstrucFideNomFideicomiso() {
        return criterioBusqInstrucFideNomFideicomiso;
    }

    public void setCriterioBusqInstrucFideNomFideicomiso(String criterioBusqInstrucFideNomFideicomiso) {
        this.criterioBusqInstrucFideNomFideicomiso = criterioBusqInstrucFideNomFideicomiso;
    }

    public String getCriterioBusqInstrucFideTipo() {
        return criterioBusqInstrucFideTipo;
    }

    public void setCriterioBusqInstrucFideTipo(String criterioBusqInstrucFideTipo) {
        this.criterioBusqInstrucFideTipo = criterioBusqInstrucFideTipo;
    }

    public Integer getCriterioBusqInstrucFidePlaza() {
        return criterioBusqInstrucFidePlaza;
    }

    public void setCriterioBusqInstrucFidePlaza(Integer criterioBusqInstrucFidePlaza) {
        this.criterioBusqInstrucFidePlaza = criterioBusqInstrucFidePlaza;
    }
    public String getCriterioBusqInstrucFideInsCveStInstruc() {
        return criterioBusqInstrucFideInsCveStInstruc;
    }
    public void setCriterioBusqInstrucFideInsCveStInstruc(String criterioBusqInstrucFideInsCveStInstruc) {
        this.criterioBusqInstrucFideInsCveStInstruc = criterioBusqInstrucFideInsCveStInstruc;
    }

    public String getTxtCriterioBusqInstrucFideicomiso() {
        return txtCriterioBusqInstrucFideicomiso;
    }

    public void setTxtCriterioBusqInstrucFideicomiso(String txtCriterioBusqInstrucFideicomiso) {
        this.txtCriterioBusqInstrucFideicomiso = txtCriterioBusqInstrucFideicomiso;
    }

    public String getTxtCriterioBusqInstrucFideSubCto() {
        return txtCriterioBusqInstrucFideSubCto;
    }

    public void setTxtCriterioBusqInstrucFideSubCto(String txtCriterioBusqInstrucFideSubCto) {
        this.txtCriterioBusqInstrucFideSubCto = txtCriterioBusqInstrucFideSubCto;
    }

    public String getTxtCriterioBusqInstrucFideFolioInst() {
        return txtCriterioBusqInstrucFideFolioInst;
    }

    public void setTxtCriterioBusqInstrucFideFolioInst(String txtCriterioBusqInstrucFideFolioInst) {
        this.txtCriterioBusqInstrucFideFolioInst = txtCriterioBusqInstrucFideFolioInst;
    }

    public String getTxtCriterioBusqInstrucFidePlaza() {
        return txtCriterioBusqInstrucFidePlaza;
    }

    public void setTxtCriterioBusqInstrucFidePlaza(String txtCriterioBusqInstrucFidePlaza) {
        this.txtCriterioBusqInstrucFidePlaza = txtCriterioBusqInstrucFidePlaza;
    }

}