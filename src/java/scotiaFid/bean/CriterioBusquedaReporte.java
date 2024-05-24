/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : CriterioBusquedaReporte.java
 * TIPO        : Clase
 * PAQUETE     : scotiaFid.bean
 * CREADO      : 20210702
 * MODIFICADO  : 20210702
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusquedaRep")
@ViewScoped
public class CriterioBusquedaReporte implements Serializable{
  //Atributos privados
    private Integer                    criterioRepReporteNum;
    private String                     criterioRepReporteSel;
    private Integer                    criterioRepContratoNum;
    private Integer                    criterioRepContratoNumSub;
    private String                     criterioRepContratoNom;
    private String                     criterioRepContratoNomSub;
    private Integer                    criterioRepMonedaNum;
    private String                     criterioRepMonedaNom;
    private Integer                    criterioRepAño;
    private Integer                    criterioRepMes;
    private Date                       criterioRepFecha;
    private Date                       criterioRepFechaDel;
    private Date                       criterioRepFechaAl;
    private Integer                    criterioRepCTAM;
    private Integer                    criterioRepSC1;
    private Integer                    criterioRepSC2;
    private Integer                    criterioRepSC3;
    private Integer                    criterioRepSC4;
    private Long                       criterioRepCtoInv; 
    private Long                       criterioRepFolioCont;
    private String                     criterioRepRolFid;
    private String                     criterioRepRolFidPersona;
    private String                     criterioRepFmto;
    private String                     criterioRepFirma01;
    private String                     criterioRepFirma02;
    private Integer                    criterioBitUsuarioNum;
    private String                     criterioBitTerminal;
    private String                     criterioBitPantalla;
// *Strings para valiaciones en pantalla
    private String                    txtcriterioRepReporteNum;
    private String                    txtcriterioRepContratoNum;
    private String                    txtcriterioRepContratoNumSub;
    private String                    txtcriterioRepMonedaNum;
    private String                    txtcriterioRepAño;
    private String                    txtcriterioRepMes;
    private String                    txtcriterioRepCTAM;
    private String                    txtcriterioRepSC1;
    private String                    txtcriterioRepSC2;
    private String                    txtcriterioRepSC3;
    private String                    txtcriterioRepSC4;
    private String                    txtcriterioRepCtoInv; 
    private String                    txtcriterioRepFolioCont;

    
  //Getters y Setters
    public Integer getCriterioRepReporteNum() {
        return criterioRepReporteNum;
    }

    public void setCriterioRepReporteNum(Integer criterioRepReporteNum) {
        this.criterioRepReporteNum = criterioRepReporteNum;
    }

    public String getCriterioRepReporteSel() {
        return criterioRepReporteSel;
    }

    public void setCriterioRepReporteSel(String criterioRepReporteSel) {
        this.criterioRepReporteSel = criterioRepReporteSel;
    }

    public Integer getCriterioRepContratoNum() {
        return criterioRepContratoNum;
    }

    public void setCriterioRepContratoNum(Integer criterioRepContratoNum) {
        this.criterioRepContratoNum = criterioRepContratoNum;
    }

    public Integer getCriterioRepContratoNumSub() {
        return criterioRepContratoNumSub;
    }

    public void setCriterioRepContratoNumSub(Integer criterioRepContratoNumSub) {
        this.criterioRepContratoNumSub = criterioRepContratoNumSub;
    }

    public String getCriterioRepContratoNom() {
        return criterioRepContratoNom;
    }

    public void setCriterioRepContratoNom(String criterioRepContratoNom) {
        this.criterioRepContratoNom = criterioRepContratoNom;
    }

    public String getCriterioRepContratoNomSub() {
        return criterioRepContratoNomSub;
    }

    public void setCriterioRepContratoNomSub(String criterioRepContratoNomSub) {
        this.criterioRepContratoNomSub = criterioRepContratoNomSub;
    }

    public Integer getCriterioRepMonedaNum() {
        return criterioRepMonedaNum;
    }

    public void setCriterioRepMonedaNum(Integer criterioRepMonedaNum) {
        this.criterioRepMonedaNum = criterioRepMonedaNum;
    }

    public String getCriterioRepMonedaNom() {
        return criterioRepMonedaNom;
    }

    public void setCriterioRepMonedaNom(String criterioRepMonedaNom) {
        this.criterioRepMonedaNom = criterioRepMonedaNom;
    }

    public Integer getCriterioRepAño() {
        return criterioRepAño;
    }

    public void setCriterioRepAño(Integer criterioRepAño) {
        this.criterioRepAño = criterioRepAño;
    }

    public Integer getCriterioRepMes() {
        return criterioRepMes;
    }

    public void setCriterioRepMes(Integer criterioRepMes) {
        this.criterioRepMes = criterioRepMes;
    }

    public Date getCriterioRepFecha() {
        return criterioRepFecha;
    }

    public void setCriterioRepFecha(Date criterioRepFecha) {
        this.criterioRepFecha = criterioRepFecha;
    }

    public Date getCriterioRepFechaDel() {
        return criterioRepFechaDel;
    }

    public void setCriterioRepFechaDel(Date criterioRepFechaDel) {
        this.criterioRepFechaDel = criterioRepFechaDel;
    }

    public Date getCriterioRepFechaAl() {
        return criterioRepFechaAl;
    }

    public void setCriterioRepFechaAl(Date criterioRepFechaAl) {
        this.criterioRepFechaAl = criterioRepFechaAl;
    }

    public Integer getCriterioRepCTAM() {
        return criterioRepCTAM;
    }

    public void setCriterioRepCTAM(Integer criterioRepCTAM) {
        this.criterioRepCTAM = criterioRepCTAM;
    }

    public Integer getCriterioRepSC1() {
        return criterioRepSC1;
    }

    public void setCriterioRepSC1(Integer criterioRepSC1) {
        this.criterioRepSC1 = criterioRepSC1;
    }

    public Integer getCriterioRepSC2() {
        return criterioRepSC2;
    }

    public void setCriterioRepSC2(Integer criterioRepSC2) {
        this.criterioRepSC2 = criterioRepSC2;
    }

    public Integer getCriterioRepSC3() {
        return criterioRepSC3;
    }

    public void setCriterioRepSC3(Integer criterioRepSC3) {
        this.criterioRepSC3 = criterioRepSC3;
    }

    public Integer getCriterioRepSC4() {
        return criterioRepSC4;
    }

    public void setCriterioRepSC4(Integer criterioRepSC4) {
        this.criterioRepSC4 = criterioRepSC4;
    }

    public Long getCriterioRepCtoInv() {
        return criterioRepCtoInv;
    }

    public void setCriterioRepCtoInv(Long criterioRepCtoInv) {
        this.criterioRepCtoInv = criterioRepCtoInv;
    }

    public Long getCriterioRepFolioCont() {
        return criterioRepFolioCont;
    }

    public void setCriterioRepFolioCont(Long criterioRepFolioCont) {
        this.criterioRepFolioCont = criterioRepFolioCont;
    }

    public String getCriterioRepRolFid() {
        return criterioRepRolFid;
    }

    public void setCriterioRepRolFid(String criterioRepRolFid) {
        this.criterioRepRolFid = criterioRepRolFid;
    }

    public String getCriterioRepRolFidPersona() {
        return criterioRepRolFidPersona;
    }

    public void setCriterioRepRolFidPersona(String criterioRepRolFidPersona) {
        this.criterioRepRolFidPersona = criterioRepRolFidPersona;
    }

    public String getCriterioRepFmto() {
        return criterioRepFmto;
    }

    public void setCriterioRepFmto(String criterioRepFmto) {
        this.criterioRepFmto = criterioRepFmto;
    }

    public String getCriterioRepFirma01() {
        return criterioRepFirma01;
    }

    public void setCriterioRepFirma01(String criterioRepFirma01) {
        this.criterioRepFirma01 = criterioRepFirma01;
    }

    public String getCriterioRepFirma02() {
        return criterioRepFirma02;
    }

    public void setCriterioRepFirma02(String criterioRepFirma02) {
        this.criterioRepFirma02 = criterioRepFirma02;
    }

    public Integer getCriterioBitUsuarioNum() {
        return criterioBitUsuarioNum;
    }

    public void setCriterioBitUsuarioNum(Integer criterioBitUsuarioNum) {
        this.criterioBitUsuarioNum = criterioBitUsuarioNum;
    }

    public String getCriterioBitTerminal() {
        return criterioBitTerminal;
    }

    public void setCriterioBitTerminal(String criterioBitTerminal) {
        this.criterioBitTerminal = criterioBitTerminal;
    }

    public String getCriterioBitPantalla() {
        return criterioBitPantalla;
    }

    public void setCriterioBitPantalla(String criterioBitPantalla) {
        this.criterioBitPantalla = criterioBitPantalla;
    }
    
  //Constructor
    public CriterioBusquedaReporte() {
        
    }

    public String getTxtcriterioRepReporteNum() {
        return txtcriterioRepReporteNum;
    }

    public void setTxtcriterioRepReporteNum(String txtcriterioRepReporteNum) {
        this.txtcriterioRepReporteNum = txtcriterioRepReporteNum;
    }

    public String getTxtcriterioRepContratoNum() {
        return txtcriterioRepContratoNum;
    }

    public void setTxtcriterioRepContratoNum(String txtcriterioRepContratoNum) {
        this.txtcriterioRepContratoNum = txtcriterioRepContratoNum;
    }

    public String getTxtcriterioRepContratoNumSub() {
        return txtcriterioRepContratoNumSub;
    }

    public void setTxtcriterioRepContratoNumSub(String txtcriterioRepContratoNumSub) {
        this.txtcriterioRepContratoNumSub = txtcriterioRepContratoNumSub;
    }

    public String getTxtcriterioRepMonedaNum() {
        return txtcriterioRepMonedaNum;
    }

    public void setTxtcriterioRepMonedaNum(String txtcriterioRepMonedaNum) {
        this.txtcriterioRepMonedaNum = txtcriterioRepMonedaNum;
    }

    public String getTxtcriterioRepAño() {
        return txtcriterioRepAño;
    }

    public void setTxtcriterioRepAño(String txtcriterioRepAño) {
        this.txtcriterioRepAño = txtcriterioRepAño;
    }

    public String getTxtcriterioRepMes() {
        return txtcriterioRepMes;
    }

    public void setTxtcriterioRepMes(String txtcriterioRepMes) {
        this.txtcriterioRepMes = txtcriterioRepMes;
    }

    public String getTxtcriterioRepCTAM() {
        return txtcriterioRepCTAM;
    }

    public void setTxtcriterioRepCTAM(String txtcriterioRepCTAM) {
        this.txtcriterioRepCTAM = txtcriterioRepCTAM;
    }

    public String getTxtcriterioRepSC1() {
        return txtcriterioRepSC1;
    }

    public void setTxtcriterioRepSC1(String txtcriterioRepSC1) {
        this.txtcriterioRepSC1 = txtcriterioRepSC1;
    }

    public String getTxtcriterioRepSC2() {
        return txtcriterioRepSC2;
    }

    public void setTxtcriterioRepSC2(String txtcriterioRepSC2) {
        this.txtcriterioRepSC2 = txtcriterioRepSC2;
    }

    public String getTxtcriterioRepSC3() {
        return txtcriterioRepSC3;
    }

    public void setTxtcriterioRepSC3(String txtcriterioRepSC3) {
        this.txtcriterioRepSC3 = txtcriterioRepSC3;
    }

    public String getTxtcriterioRepSC4() {
        return txtcriterioRepSC4;
    }

    public void setTxtcriterioRepSC4(String txtcriterioRepSC4) {
        this.txtcriterioRepSC4 = txtcriterioRepSC4;
    }

    public String getTxtcriterioRepCtoInv() {
        return txtcriterioRepCtoInv;
    }

    public void setTxtcriterioRepCtoInv(String txtcriterioRepCtoInv) {
        this.txtcriterioRepCtoInv = txtcriterioRepCtoInv;
    }

    public String getTxtcriterioRepFolioCont() {
        return txtcriterioRepFolioCont;
    }

    public void setTxtcriterioRepFolioCont(String txtcriterioRepFolioCont) {
        this.txtcriterioRepFolioCont = txtcriterioRepFolioCont;
    }
}