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

@ManagedBean(name = "beanCriterioBusquedaConta")
@ViewScoped
public class CriterioBusquedaContaBean implements Serializable{
  //Atributos privados
    private Integer                              criterioCTAM;
    private Integer                              criterioSC1;
    private Integer                              criterioSC2;
    private Integer                              criterioSC3;
    private Integer                              criterioSC4;
    private Long                                 criterioAX1;
    private Long                                 criterioAX2;
    private Long                                 criterioAX3;
    private Long                                 criterioFolio;
    private Long                                 criterioFolioIni;
    private Long                                 criterioFolioFin;
    private Integer                              criterioFechaDD;
    private Integer                              criterioFechaMM;
    private Integer                              criterioFechaYYYY;
    private String                               criterioCveMovtoCancelado;
    private String                               criterioTipoFecha;
    private Integer                              criterioTransId;
    private String                               criterioOperaId;
    private String                               criterioBienFideTipo;
    private String                               criterioBienFideClas;
    private String                               criterioStatus;
    private Integer                              criterioPlaza;
    private Integer                              criterioUsuario;
    private String                               criterioModulo;
    private Boolean                              criterioCheckInm;
    private Boolean                              criterioCheckMue;
    private Boolean                              criterioCheckOtr;
   
    private String                              txtCriterioCTAM;
    private String                              txtCriterioSC1;
    private String                              txtCriterioSC2;
    private String                              txtCriterioSC3;
    private String                              txtCriterioSC4;
    private String                              txtCriterioAX1;
    private String                              txtCriterioAX2;
    private String                              txtCriterioAX3;
    private String                              txtCriterioFolio;
    private String                              txtCriterioFolioIni;
    private String                              txtCriterioFolioFin;
    private String                              txtCriterioFechaDD;
    private String                              txtCriterioFechaMM;
    private String                              txtCriterioFechaYYYY;
    private String                              txtCriterioTransId;
    private String                              txtCriterioPlaza;
    private String                              txtCriterioUsuario;

    
 //Getetrs y Setters
    public Integer getCriterioCTAM() {
        return criterioCTAM;
    }

    public void setCriterioCTAM(Integer criterioCTAM) {
        this.criterioCTAM = criterioCTAM;
    }

    public Integer getCriterioSC1() {
        return criterioSC1;
    }

    public void setCriterioSC1(Integer criterioSC1) {
        this.criterioSC1 = criterioSC1;
    }

    public Integer getCriterioSC2() {
        return criterioSC2;
    }

    public void setCriterioSC2(Integer criterioSC2) {
        this.criterioSC2 = criterioSC2;
    }

    public Integer getCriterioSC3() {
        return criterioSC3;
    }

    public void setCriterioSC3(Integer criterioSC3) {
        this.criterioSC3 = criterioSC3;
    }

    public Integer getCriterioSC4() {
        return criterioSC4;
    }

    public void setCriterioSC4(Integer criterioSC4) {
        this.criterioSC4 = criterioSC4;
    }

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

    public Long getCriterioFolio() {
        return criterioFolio;
    }

    public void setCriterioFolio(Long criterioFolio) {
        this.criterioFolio = criterioFolio;
    }

    public Long getCriterioFolioIni() {
        return criterioFolioIni;
    }

    public void setCriterioFolioIni(Long criterioFolioIni) {
        this.criterioFolioIni = criterioFolioIni;
    }

    public Long getCriterioFolioFin() {
        return criterioFolioFin;
    }

    public void setCriterioFolioFin(Long criterioFolioFin) {
        this.criterioFolioFin = criterioFolioFin;
    }

    public Integer getCriterioFechaDD() {
        return criterioFechaDD;
    }

    public void setCriterioFechaDD(Integer criterioFechaDD) {
        this.criterioFechaDD = criterioFechaDD;
    }

    public Integer getCriterioFechaMM() {
        return criterioFechaMM;
    }

    public void setCriterioFechaMM(Integer criterioFechaMM) {
        this.criterioFechaMM = criterioFechaMM;
    }

    public Integer getCriterioFechaYYYY() {
        return criterioFechaYYYY;
    }

    public void setCriterioFechaYYYY(Integer criterioFechaYYYY) {
        this.criterioFechaYYYY = criterioFechaYYYY;
    }

    public String getCriterioCveMovtoCancelado() {
        return criterioCveMovtoCancelado;
    }

    public void setCriterioCveMovtoCancelado(String criterioCveMovtoCancelado) {
        this.criterioCveMovtoCancelado = criterioCveMovtoCancelado;
    }

    public String getCriterioTipoFecha() {
        return criterioTipoFecha;
    }

    public void setCriterioTipoFecha(String criterioTipoFecha) {
        this.criterioTipoFecha = criterioTipoFecha;
    }

    public Integer getCriterioTransId() {
        return criterioTransId;
    }

    public void setCriterioTransId(Integer criterioTransId) {
        this.criterioTransId = criterioTransId;
    }

    public String getCriterioOperaId() {
        return criterioOperaId;
    }

    public void setCriterioOperaId(String criterioOperaId) {
        this.criterioOperaId = criterioOperaId;
    }

    public String getCriterioBienFideTipo() {
        return criterioBienFideTipo;
    }

    public void setCriterioBienFideTipo(String criterioBienFideTipo) {
        this.criterioBienFideTipo = criterioBienFideTipo;
    }

    public String getCriterioBienFideClas() {
        return criterioBienFideClas;
    }

    public void setCriterioBienFideClas(String criterioBienFideClas) {
        this.criterioBienFideClas = criterioBienFideClas;
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

    public Integer getCriterioUsuario() {
        return criterioUsuario;
    }

    public void setCriterioUsuario(Integer criterioUsuario) {
        this.criterioUsuario = criterioUsuario;
    }

    public String getCriterioModulo() {
        return criterioModulo;
    }

    public void setCriterioModulo(String criterioModulo) {
        this.criterioModulo = criterioModulo;
    }

    public Boolean getCriterioCheckInm() {
        return criterioCheckInm;
    }

    public void setCriterioCheckInm(Boolean criterioCheckInm) {
        this.criterioCheckInm = criterioCheckInm;
    }

    public Boolean getCriterioCheckMue() {
        return criterioCheckMue;
    }

    public void setCriterioCheckMue(Boolean criterioCheckMue) {
        this.criterioCheckMue = criterioCheckMue;
    }

    public Boolean getCriterioCheckOtr() {
        return criterioCheckOtr;
    }

    public void setCriterioCheckOtr(Boolean criterioCheckOtr) {
        this.criterioCheckOtr = criterioCheckOtr;
    }

    
  //Constructor
    public CriterioBusquedaContaBean() {
        
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

    public String getTxtCriterioFolio() {
        return txtCriterioFolio;
    }

    public void setTxtCriterioFolio(String txtCriterioFolio) {
        this.txtCriterioFolio = txtCriterioFolio;
    }

    public String getTxtCriterioFolioIni() {
        return txtCriterioFolioIni;
    }

    public void setTxtCriterioFolioIni(String txtCriterioFolioIni) {
        this.txtCriterioFolioIni = txtCriterioFolioIni;
    }

    public String getTxtCriterioFolioFin() {
        return txtCriterioFolioFin;
    }

    public void setTxtCriterioFolioFin(String txtCriterioFolioFin) {
        this.txtCriterioFolioFin = txtCriterioFolioFin;
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

    public String getTxtCriterioFechaYYYY() {
        return txtCriterioFechaYYYY;
    }

    public void setTxtCriterioFechaYYYY(String txtCriterioFechaYYYY) {
        this.txtCriterioFechaYYYY = txtCriterioFechaYYYY;
    }

    public String getTxtCriterioTransId() {
        return txtCriterioTransId;
    }

    public void setTxtCriterioTransId(String txtCriterioTransId) {
        this.txtCriterioTransId = txtCriterioTransId;
    }
   
    public String getTxtCriterioUsuario() {
        return txtCriterioUsuario;
    }

    public void setTxtCriterioUsuario(String txtCriterioUsuario) {
        this.txtCriterioUsuario = txtCriterioUsuario;
    }

    public String getTxtCriterioPlaza() {
        return txtCriterioPlaza;
    }

    public void setTxtCriterioPlaza(String txtCriterioPlaza) {
        this.txtCriterioPlaza = txtCriterioPlaza;
    }
}