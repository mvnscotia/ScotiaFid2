/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.sql.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "bean_spn_proc_liq_ind")
@ViewScoped
public class SPN_PROC_LOTE_LIQ_Bean {
    
    //Input
    private Long IUSUARIO;
    private Long INUMCONTRATO;
    private Integer ISUBCONTRATO;
    private Integer ICPTOPAGO;
    private String SNOMPROCESO;
    private Date DTFECHA;
    private String SNOMARCHIVO;
    private String STIPOPERSONA;
    private Long IFOLIOINSTR;
    private Double DVALORLOTE;
    private Integer IMONEDA;
    private String SCTAORIGEN;
    private String SIDFORMA;
    private String STERMINAL;
    
    //Output
    private Integer BEJECUTO;
    private String SCADENATOT;
    private Long IREGISTROS;
    private Long ICORRECTOS;
    private Long ICONERROR;
    private String PCH_SQLSTATE_OUT;
    private Integer PI_SQLCODE_OUT;
    private String PS_MSGERR_OUT;
    
    
    public Long getIUSUARIO() {
        return IUSUARIO;
    }

    public void setIUSUARIO(Long IUSUARIO) {
        this.IUSUARIO = IUSUARIO;
    }
    
    public Long getINUMCONTRATO() {
        return INUMCONTRATO;
    }

    public void setINUMCONTRATO(Long INUMCONTRATO) {
        this.INUMCONTRATO = INUMCONTRATO;
    }

    public Integer getISUBCONTRATO() {
        return ISUBCONTRATO;
    }

    public void setISUBCONTRATO(Integer ISUBCONTRATO) {
        this.ISUBCONTRATO = ISUBCONTRATO;
    }

    public Integer getICPTOPAGO() {
        return ICPTOPAGO;
    }

    public void setICPTOPAGO(Integer ICPTOPAGO) {
        this.ICPTOPAGO = ICPTOPAGO;
    }

    public String getSNOMPROCESO() {
        return SNOMPROCESO;
    }

    public void setSNOMPROCESO(String SNOMPROCESO) {
        this.SNOMPROCESO = SNOMPROCESO;
    }

    public Date getDTFECHA() {
        return DTFECHA;
    }

    public void setDTFECHA(Date DTFECHA) {
        this.DTFECHA = DTFECHA;
    }

    public String getSNOMARCHIVO() {
        return SNOMARCHIVO;
    }

    public void setSNOMARCHIVO(String SNOMARCHIVO) {
        this.SNOMARCHIVO = SNOMARCHIVO;
    }

    public String getSTIPOPERSONA() {
        return STIPOPERSONA;
    }

    public void setSTIPOPERSONA(String STIPOPERSONA) {
        this.STIPOPERSONA = STIPOPERSONA;
    }

    public Long getIFOLIOINSTR() {
        return IFOLIOINSTR;
    }

    public void setIFOLIOINSTR(Long IFOLIOINSTR) {
        this.IFOLIOINSTR = IFOLIOINSTR;
    }

    public Double getDVALORLOTE() {
        return DVALORLOTE;
    }

    public void setDVALORLOTE(Double DVALORLOTE) {
        this.DVALORLOTE = DVALORLOTE;
    }

    public Integer getIMONEDA() {
        return IMONEDA;
    }

    public void setIMONEDA(Integer IMONEDA) {
        this.IMONEDA = IMONEDA;
    }

    public String getSCTAORIGEN() {
        return SCTAORIGEN;
    }

    public void setSCTAORIGEN(String SCTAORIGEN) {
        this.SCTAORIGEN = SCTAORIGEN;
    }

    public String getSIDFORMA() {
        return SIDFORMA;
    }

    public void setSIDFORMA(String SIDFORMA) {
        this.SIDFORMA = SIDFORMA;
    }

    public String getSTERMINAL() {
        return STERMINAL;
    }

    public void setSTERMINAL(String STERMINAL) {
        this.STERMINAL = STERMINAL;
    }

    public Integer getBEJECUTO() {
        return BEJECUTO;
    }

    public void setBEJECUTO(Integer BEJECUTO) {
        this.BEJECUTO = BEJECUTO;
    }

    public String getSCADENATOT() {
        return SCADENATOT;
    }

    public void setSCADENATOT(String SCADENATOT) {
        this.SCADENATOT = SCADENATOT;
    }

    public Long getIREGISTROS() {
        return IREGISTROS;
    }

    public void setIREGISTROS(Long IREGISTROS) {
        this.IREGISTROS = IREGISTROS;
    }

    public Long getICORRECTOS() {
        return ICORRECTOS;
    }

    public void setICORRECTOS(Long ICORRECTOS) {
        this.ICORRECTOS = ICORRECTOS;
    }

    public Long getICONERROR() {
        return ICONERROR;
    }

    public void setICONERROR(Long ICONERROR) {
        this.ICONERROR = ICONERROR;
    }

    public String getPCH_SQLSTATE_OUT() {
        return PCH_SQLSTATE_OUT;
    }

    public void setPCH_SQLSTATE_OUT(String PCH_SQLSTATE_OUT) {
        this.PCH_SQLSTATE_OUT = PCH_SQLSTATE_OUT;
    }

    public Integer getPI_SQLCODE_OUT() {
        return PI_SQLCODE_OUT;
    }

    public void setPI_SQLCODE_OUT(Integer PI_SQLCODE_OUT) {
        this.PI_SQLCODE_OUT = PI_SQLCODE_OUT;
    }

    public String getPS_MSGERR_OUT() {
        return PS_MSGERR_OUT;
    }

    public void setPS_MSGERR_OUT(String PS_MSGERR_OUT) {
        this.PS_MSGERR_OUT = PS_MSGERR_OUT;
    }


    

}
