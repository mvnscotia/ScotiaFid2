/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CriterioBusquedaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210314
 * MODIFICADO  : 20210614
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusqueda")
@ViewScoped
public class CriterioBusquedaBean implements Serializable{
  //Atributos privados
    private Date                                 criterioFecha;
    private Date                                 criterioFechaDel;
    private Date                                 criterioFechaAl;
    private Long                                 criterioContratoNumero;
    private Integer                              criterioContratoNumeroSub;
    private String                               criterioContratoNombre;
    private Integer                              criterioContratoN1;
    private Integer                              criterioContratoN2;
    private Integer                              criterioContratoN3;
    private Integer                              criterioContratoN4;
    private Integer                              criterioContratoN5;
    private String                               criterioMonedaNombre;
    private String                               criterioStatus;
    private Integer                              criterioUsuario;
    
    private String                              txtCriterioContratoNumero;
    private String                              txtCriterioContratoNumeroSub;
    private String                              txtCriterioContratoNombre;
    private String                              txtCriterioContratoN1;
    private String                              txtCriterioContratoN2;
    private String                              txtCriterioContratoN3;
    private String                              txtCriterioContratoN4;
    private String                              txtCriterioContratoN5;
    private String                              txtCriterioMonedaNombre;
    private String                              txtCriterioStatus;
    private String                              txtCriterioUsuario;

    //Getters y Setters
    public Date getCriterioFecha() {
        return criterioFecha;
    }

    public void setCriterioFecha(Date criterioFecha) {
        this.criterioFecha = criterioFecha;
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

    public Long getCriterioContratoNumero() {
        return criterioContratoNumero;
    }

    public void setCriterioContratoNumero(Long criterioContratoNumero) {
        this.criterioContratoNumero = criterioContratoNumero;
    }

    public Integer getCriterioContratoNumeroSub() {
          return criterioContratoNumeroSub;
    }

    public void setCriterioContratoNumeroSub(Integer criterioContratoNumeroSub) {
        this.criterioContratoNumeroSub = criterioContratoNumeroSub;
    }

    public String getCriterioContratoNombre() {
        return criterioContratoNombre;
    }

    public void setCriterioContratoNombre(String criterioContratoNombre) {
        this.criterioContratoNombre = criterioContratoNombre;
    }

    public Integer getCriterioContratoN1() {
        return criterioContratoN1;
    }

    public void setCriterioContratoN1(Integer criterioContratoN1) {
        this.criterioContratoN1 = criterioContratoN1;
    }

    public Integer getCriterioContratoN2() {
        return criterioContratoN2;
    }

    public void setCriterioContratoN2(Integer criterioContratoN2) {
        this.criterioContratoN2 = criterioContratoN2;
    }

    public Integer getCriterioContratoN3() {
        return criterioContratoN3;
    }

    public void setCriterioContratoN3(Integer criterioContratoN3) {
        this.criterioContratoN3 = criterioContratoN3;
    }

    public Integer getCriterioContratoN4() {
        return criterioContratoN4;
    }

    public void setCriterioContratoN4(Integer criterioContratoN4) {
        this.criterioContratoN4 = criterioContratoN4;
    }

    public Integer getCriterioContratoN5() {
        return criterioContratoN5;
    }

    public void setCriterioContratoN5(Integer criterioContratoN5) {
        this.criterioContratoN5 = criterioContratoN5;
    }

    public String getCriterioMonedaNombre() {
        return criterioMonedaNombre;
    }

    public void setCriterioMonedaNombre(String criterioMonedaNombre) {
        this.criterioMonedaNombre = criterioMonedaNombre;
    }

    public String getCriterioStatus() {
        return criterioStatus;
    }

    public void setCriterioStatus(String criterioStatus) {
        this.criterioStatus = criterioStatus;
    }
    
  //Constructor
    public CriterioBusquedaBean() {
        
    }

    public Integer getCriterioUsuario() {
        return criterioUsuario;
    }

    public void setCriterioUsuario(Integer criterioUsuario) {
        this.criterioUsuario = criterioUsuario;
    }

    public String getTxtCriterioContratoNumero() {
        return txtCriterioContratoNumero;
    }

    public void setTxtCriterioContratoNumero(String txtCriterioContratoNumero) {
        this.txtCriterioContratoNumero = txtCriterioContratoNumero;
    }

    public String getTxtCriterioContratoNumeroSub() {
        return txtCriterioContratoNumeroSub;
    }

    public void setTxtCriterioContratoNumeroSub(String txtCriterioContratoNumeroSub) {
        this.txtCriterioContratoNumeroSub = txtCriterioContratoNumeroSub;
    }

    public String getTxtCriterioContratoNombre() {
        return txtCriterioContratoNombre;
    }

    public void setTxtCriterioContratoNombre(String txtCriterioContratoNombre) {
        this.txtCriterioContratoNombre = txtCriterioContratoNombre;
    }

    public String getTxtCriterioContratoN1() {
        return txtCriterioContratoN1;
    }

    public void setTxtCriterioContratoN1(String txtCriterioContratoN1) {
        this.txtCriterioContratoN1 = txtCriterioContratoN1;
    }

    public String getTxtCriterioContratoN2() {
        return txtCriterioContratoN2;
    }

    public void setTxtCriterioContratoN2(String txtCriterioContratoN2) {
        this.txtCriterioContratoN2 = txtCriterioContratoN2;
    }

    public String getTxtCriterioContratoN3() {
        return txtCriterioContratoN3;
    }

    public void setTxtCriterioContratoN3(String txtCriterioContratoN3) {
        this.txtCriterioContratoN3 = txtCriterioContratoN3;
    }

    public String getTxtCriterioContratoN4() {
        return txtCriterioContratoN4;
    }

    public void setTxtCriterioContratoN4(String txtCriterioContratoN4) {
        this.txtCriterioContratoN4 = txtCriterioContratoN4;
    }

    public String getTxtCriterioContratoN5() {
        return txtCriterioContratoN5;
    }

    public void setTxtCriterioContratoN5(String txtCriterioContratoN5) {
        this.txtCriterioContratoN5 = txtCriterioContratoN5;
    }

    public String getTxtCriterioMonedaNombre() {
        return txtCriterioMonedaNombre;
    }

    public void setTxtCriterioMonedaNombre(String txtCriterioMonedaNombre) {
        this.txtCriterioMonedaNombre = txtCriterioMonedaNombre;
    }

    public String getTxtCriterioStatus() {
        return txtCriterioStatus;
    }

    public void setTxtCriterioStatus(String txtCriterioStatus) {
        this.txtCriterioStatus = txtCriterioStatus;
    }

    public String getTxtCriterioUsuario() {
        return txtCriterioUsuario;
    }

    public void setTxtCriterioUsuario(String txtCriterioUsuario) {
        this.txtCriterioUsuario = txtCriterioUsuario;
    }
}