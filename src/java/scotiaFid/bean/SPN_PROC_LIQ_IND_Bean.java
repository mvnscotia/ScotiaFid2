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
@ManagedBean(name = "spn_proc_liq_ind_bean")
@ViewScoped
public class SPN_PROC_LIQ_IND_Bean {

    private Integer IUSUARIO;
    private Integer INUMCONTRATO;
    private Integer ISUBCONTRATO;
    private String SIDTRANSAC;
    private String VSCVE_PERS_FID;
    private Integer VINUM_PERS_FID;
    private String VSNOMPERSONA;
    private Double VDIMPORTEMOV;
    private Integer VINUM_MONEDA;
    private Double VDTIPOCAMBIO;
    private Integer VICVE_TIPO_LIQ;
    private String VSTIPOLIQUID;
    private Integer IFOLIOINSTR;
    private Integer VINUMPAGOS;
    private Integer VIPAGOSEFECT;
    private String SCTAORIGEN;
    private Integer VICPTOPAGO;
    private String VSCPTOPAGO;
    private String VSCVE_TIPO_CTA;
    private Integer VINUM_BANCO;
    private Integer VINUM_PLAZA;
    private Integer VINUM_SUCURSAL;
    private Double VINUM_CUENTA;
    private Integer VINUM_PAIS;
    private Integer VICTA_BANXICO;
    private String VSDIR_APER_CTA;
    private Integer VINUM_INICIATIVA;
    private Integer VINUM_CTAM;
    private Integer VINUM_SCTA;
    private Integer VINUM_SSCTA;
    private Integer VINUM_SSSCTA;
    private String VSNOM_AREA;
    private String VSCONCEPTO;
    private Integer VICUENTAS_ORDEN;
    private Integer VISECUENINSTR;
    private Date DTFECHAMOV;
    private String STERMINAL;
    private String SIDFORMA;
    private Integer BEJECUTO;
    private Integer IREGISTROS;
    private Integer ICORRECTOS;
    private Integer ICONERROR;
    private String PCH_SQLSTATE_OUT;
    private Integer PI_SQLCODE_OUT;
    private String PS_MSGERR_OUT;

    public Integer getIUSUARIO() {
        return IUSUARIO;
    }

    public void setIUSUARIO(Integer IUSUARIO) {
        this.IUSUARIO = IUSUARIO;
    }

    public Integer getINUMCONTRATO() {
        return INUMCONTRATO;
    }

    public void setINUMCONTRATO(Integer INUMCONTRATO) {
        this.INUMCONTRATO = INUMCONTRATO;
    }

    public Integer getISUBCONTRATO() {
        return ISUBCONTRATO;
    }

    public void setISUBCONTRATO(Integer ISUBCONTRATO) {
        this.ISUBCONTRATO = ISUBCONTRATO;
    }

    public String getSIDTRANSAC() {
		return SIDTRANSAC;
	}

	public void setSIDTRANSAC(String sIDTRANSAC) {
		SIDTRANSAC = sIDTRANSAC;
	}

	public String getVSCVE_PERS_FID() {
        return VSCVE_PERS_FID;
    }

    public void setVSCVE_PERS_FID(String VSCVE_PERS_FID) {
        this.VSCVE_PERS_FID = VSCVE_PERS_FID;
    }

    public Integer getVINUM_PERS_FID() {
        return VINUM_PERS_FID;
    }

    public void setVINUM_PERS_FID(Integer VINUM_PERS_FID) {
        this.VINUM_PERS_FID = VINUM_PERS_FID;
    }

    public String getVSNOMPERSONA() {
        return VSNOMPERSONA;
    }

    public void setVSNOMPERSONA(String VSNOMPERSONA) {
        this.VSNOMPERSONA = VSNOMPERSONA;
    }

    public Double getVDIMPORTEMOV() {
        return VDIMPORTEMOV;
    }

    public void setVDIMPORTEMOV(Double VDIMPORTEMOV) {
        this.VDIMPORTEMOV = VDIMPORTEMOV;
    }

    public Integer getVINUM_MONEDA() {
        return VINUM_MONEDA;
    }

    public void setVINUM_MONEDA(Integer VINUM_MONEDA) {
        this.VINUM_MONEDA = VINUM_MONEDA;
    }

    public Double getVDTIPOCAMBIO() {
        return VDTIPOCAMBIO;
    }

    public void setVDTIPOCAMBIO(Double VDTIPOCAMBIO) {
        this.VDTIPOCAMBIO = VDTIPOCAMBIO;
    }

    public Integer getVICVE_TIPO_LIQ() {
        return VICVE_TIPO_LIQ;
    }

    public void setVICVE_TIPO_LIQ(Integer VICVE_TIPO_LIQ) {
        this.VICVE_TIPO_LIQ = VICVE_TIPO_LIQ;
    }

    public String getVSTIPOLIQUID() {
        return VSTIPOLIQUID;
    }

    public void setVSTIPOLIQUID(String VSTIPOLIQUID) {
        this.VSTIPOLIQUID = VSTIPOLIQUID;
    }

    public Integer getIFOLIOINSTR() {
        return IFOLIOINSTR;
    }

    public void setIFOLIOINSTR(Integer IFOLIOINSTR) {
        this.IFOLIOINSTR = IFOLIOINSTR;
    }

    public Integer getVINUMPAGOS() {
        return VINUMPAGOS;
    }

    public void setVINUMPAGOS(Integer VINUMPAGOS) {
        this.VINUMPAGOS = VINUMPAGOS;
    }

    public Integer getVIPAGOSEFECT() {
        return VIPAGOSEFECT;
    }

    public void setVIPAGOSEFECT(Integer VIPAGOSEFECT) {
        this.VIPAGOSEFECT = VIPAGOSEFECT;
    }

    public String getSCTAORIGEN() {
        return SCTAORIGEN;
    }

    public void setSCTAORIGEN(String SCTAORIGEN) {
        this.SCTAORIGEN = SCTAORIGEN;
    }

    public Integer getVICPTOPAGO() {
        return VICPTOPAGO;
    }

    public void setVICPTOPAGO(Integer VICPTOPAGO) {
        this.VICPTOPAGO = VICPTOPAGO;
    }

    public String getVSCPTOPAGO() {
        return VSCPTOPAGO;
    }

    public void setVSCPTOPAGO(String VSCPTOPAGO) {
        this.VSCPTOPAGO = VSCPTOPAGO;
    }

    public String getVSCVE_TIPO_CTA() {
        return VSCVE_TIPO_CTA;
    }

    public void setVSCVE_TIPO_CTA(String VSCVE_TIPO_CTA) {
        this.VSCVE_TIPO_CTA = VSCVE_TIPO_CTA;
    }

    public Integer getVINUM_BANCO() {
        return VINUM_BANCO;
    }

    public void setVINUM_BANCO(Integer VINUM_BANCO) {
        this.VINUM_BANCO = VINUM_BANCO;
    }

    public Integer getVINUM_PLAZA() {
        return VINUM_PLAZA;
    }

    public void setVINUM_PLAZA(Integer VINUM_PLAZA) {
        this.VINUM_PLAZA = VINUM_PLAZA;
    }

    public Integer getVINUM_SUCURSAL() {
        return VINUM_SUCURSAL;
    }

    public void setVINUM_SUCURSAL(Integer VINUM_SUCURSAL) {
        this.VINUM_SUCURSAL = VINUM_SUCURSAL;
    }

    public Double getVINUM_CUENTA() {
        return VINUM_CUENTA;
    }

    public void setVINUM_CUENTA(Double VINUM_CUENTA) {
        this.VINUM_CUENTA = VINUM_CUENTA;
    }

    public Integer getVINUM_PAIS() {
        return VINUM_PAIS;
    }

    public void setVINUM_PAIS(Integer VINUM_PAIS) {
        this.VINUM_PAIS = VINUM_PAIS;
    }

    public Integer getVICTA_BANXICO() {
        return VICTA_BANXICO;
    }

    public void setVICTA_BANXICO(Integer VICTA_BANXICO) {
        this.VICTA_BANXICO = VICTA_BANXICO;
    }

    public String getVSDIR_APER_CTA() {
        return VSDIR_APER_CTA;
    }

    public void setVSDIR_APER_CTA(String VSDIR_APER_CTA) {
        this.VSDIR_APER_CTA = VSDIR_APER_CTA;
    }

    public Integer getVINUM_INICIATIVA() {
        return VINUM_INICIATIVA;
    }

    public void setVINUM_INICIATIVA(Integer VINUM_INICIATIVA) {
        this.VINUM_INICIATIVA = VINUM_INICIATIVA;
    }

    public Integer getVINUM_CTAM() {
        return VINUM_CTAM;
    }

    public void setVINUM_CTAM(Integer VINUM_CTAM) {
        this.VINUM_CTAM = VINUM_CTAM;
    }

    public Integer getVINUM_SCTA() {
        return VINUM_SCTA;
    }

    public void setVINUM_SCTA(Integer VINUM_SCTA) {
        this.VINUM_SCTA = VINUM_SCTA;
    }

    public Integer getVINUM_SSCTA() {
        return VINUM_SSCTA;
    }

    public void setVINUM_SSCTA(Integer VINUM_SSCTA) {
        this.VINUM_SSCTA = VINUM_SSCTA;
    }

    public Integer getVINUM_SSSCTA() {
        return VINUM_SSSCTA;
    }

    public void setVINUM_SSSCTA(Integer VINUM_SSSCTA) {
        this.VINUM_SSSCTA = VINUM_SSSCTA;
    }

    public String getVSNOM_AREA() {
        return VSNOM_AREA;
    }

    public void setVSNOM_AREA(String VSNOM_AREA) {
        this.VSNOM_AREA = VSNOM_AREA;
    }

    public String getVSCONCEPTO() {
        return VSCONCEPTO;
    }

    public void setVSCONCEPTO(String VSCONCEPTO) {
        this.VSCONCEPTO = VSCONCEPTO;
    }

    public Integer getVICUENTAS_ORDEN() {
        return VICUENTAS_ORDEN;
    }

    public void setVICUENTAS_ORDEN(Integer VICUENTAS_ORDEN) {
        this.VICUENTAS_ORDEN = VICUENTAS_ORDEN;
    }

    public Integer getVISECUENINSTR() {
        return VISECUENINSTR;
    }

    public void setVISECUENINSTR(Integer VISECUENINSTR) {
        this.VISECUENINSTR = VISECUENINSTR;
    }

    public Date getDTFECHAMOV() {
        return DTFECHAMOV;
    }

    public void setDTFECHAMOV(Date DTFECHAMOV) {
        this.DTFECHAMOV = DTFECHAMOV;
    }

    public String getSTERMINAL() {
        return STERMINAL;
    }

    public void setSTERMINAL(String STERMINAL) {
        this.STERMINAL = STERMINAL;
    }

    public String getSIDFORMA() {
        return SIDFORMA;
    }

    public void setSIDFORMA(String SIDFORMA) {
        this.SIDFORMA = SIDFORMA;
    }

    public Integer getBEJECUTO() {
        return BEJECUTO;
    }

    public void setBEJECUTO(Integer BEJECUTO) {
        this.BEJECUTO = BEJECUTO;
    }

    public Integer getIREGISTROS() {
        return IREGISTROS;
    }

    public void setIREGISTROS(Integer IREGISTROS) {
        this.IREGISTROS = IREGISTROS;
    }

    public Integer getICORRECTOS() {
        return ICORRECTOS;
    }

    public void setICORRECTOS(Integer ICORRECTOS) {
        this.ICORRECTOS = ICORRECTOS;
    }

    public Integer getICONERROR() {
        return ICONERROR;
    }

    public void setICONERROR(Integer ICONERROR) {
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
