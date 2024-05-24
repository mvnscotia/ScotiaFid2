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
@ManagedBean(name = "monetariasOperacionOtrosBancosBean")
@ViewScoped
public class MonetariasOperacionOtrosBancosBean {

    private Integer LNUMCONTRATO;
    private Integer ISUBCONTRATO;
    private Double VDIMPORTEMOV;
    private Integer INUMMONEDA;
    private Date DTFECHAMOV;
    private String SCTAORIGEN;
    private Integer VICPTODEPO;
    private Integer VIFORMADEP;
    private String SCVE_PERS_FID;
    private Integer INUM_PERS_FID;
    private Integer INUMUSUARIO;
    private String STERMINAL;

    public Integer getLNUMCONTRATO() {
        return LNUMCONTRATO;
    }

    public void setLNUMCONTRATO(Integer LNUMCONTRATO) {
        this.LNUMCONTRATO = LNUMCONTRATO;
    }

    public Integer getISUBCONTRATO() {
        return ISUBCONTRATO;
    }

    public void setISUBCONTRATO(Integer ISUBCONTRATO) {
        this.ISUBCONTRATO = ISUBCONTRATO;
    }

    public Double getVDIMPORTEMOV() {
        return VDIMPORTEMOV;
    }

    public void setVDIMPORTEMOV(Double VDIMPORTEMOV) {
        this.VDIMPORTEMOV = VDIMPORTEMOV;
    }

    public Integer getINUMMONEDA() {
        return INUMMONEDA;
    }

    public void setINUMMONEDA(Integer INUMMONEDA) {
        this.INUMMONEDA = INUMMONEDA;
    }

    public Date getDTFECHAMOV() {
        return DTFECHAMOV;
    }

    public void setDTFECHAMOV(Date DTFECHAMOV) {
        this.DTFECHAMOV = DTFECHAMOV;
    }

    public String getSCTAORIGEN() {
        return SCTAORIGEN;
    }

    public void setSCTAORIGEN(String SCTAORIGEN) {
        this.SCTAORIGEN = SCTAORIGEN;
    }

    public Integer getVICPTODEPO() {
        return VICPTODEPO;
    }

    public void setVICPTODEPO(Integer VICPTODEPO) {
        this.VICPTODEPO = VICPTODEPO;
    }

    public Integer getVIFORMADEP() {
        return VIFORMADEP;
    }

    public void setVIFORMADEP(Integer VIFORMADEP) {
        this.VIFORMADEP = VIFORMADEP;
    }

    public String getSCVE_PERS_FID() {
        return SCVE_PERS_FID;
    }

    public void setSCVE_PERS_FID(String SCVE_PERS_FID) {
        this.SCVE_PERS_FID = SCVE_PERS_FID;
    }

    public Integer getINUM_PERS_FID() {
        return INUM_PERS_FID;
    }

    public void setINUM_PERS_FID(Integer INUM_PERS_FID) {
        this.INUM_PERS_FID = INUM_PERS_FID;
    }

    public Integer getINUMUSUARIO() {
        return INUMUSUARIO;
    }

    public void setINUMUSUARIO(Integer INUMUSUARIO) {
        this.INUMUSUARIO = INUMUSUARIO;
    }

    public String getSTERMINAL() {
        return STERMINAL;
    }

    public void setSTERMINAL(String STERMINAL) {
        this.STERMINAL = STERMINAL;
    }



}
