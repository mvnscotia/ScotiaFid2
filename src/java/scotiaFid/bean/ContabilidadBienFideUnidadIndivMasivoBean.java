/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : ContabilidadBienFideUnidadIndivMasivoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210923
 * MODIFICADO  : 20210923
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadBienFideUnidadIndivMasivoBean")
@ViewScoped
public class ContabilidadBienFideUnidadIndivMasivoBean {
  //Atributos privados
    private String                     bienFideUnidadIndivMasivoProceso;
    private Long                       bienFideUnidadIndivMasivoContrato;
    private Integer                    bienFideUnidadIndivMasivoContratoSub;
    private String                     bienFideUnidadIndivMasivoBienTipo;
    private Integer                    bienFideUnidadIndivMasivoBienId;
    private Integer                    bienFideUnidadIndivMasivoBienUniId;
    private Integer                    bienFideUnidadIndivMasivoBienUniIndivId;
    private Double                     bienFideUnidadIndivMasivoBienUniIndivIndiviso;
    private Date                       bienFideUnidadIndivMasivoBienUniIndivFechaSolicitud;
    private Date                       bienFideUnidadIndivMasivoBienUniIndivFechaInstruccion;
    private Date                       bienFideUnidadIndivMasivoBienUniIndivFechaFirma;
    private Date                       bienFideUnidadIndivMasivoBienUniIndivFechaDominio;
    private String                     bienFideUnidadIndivMasivoBienUniIndivEscritura;
    private String                     bienFideUnidadIndivMasivoBienUniIndivNotario;
    private String                     bienFideUnidadIndivMasivoBienUniIndivAdquiriente;
    private String                     bienFideUnidadIndivMasivoBienUniIndivStatus;
    private String                     bienFideUnidadIndivMasivoBienUniIndivStatusLiq;
    private Long                       bienFideUnidadIndivMasivoBienUniIndivFolioOp;
    private Short                      bienFideUnidadIndivMasivoBienUniIndivLineaSec;
    private Integer                    bienFideUnidadIndivMasivoBienUsuarioNum;
    
  //Getters y Setters
    public String getBienFideUnidadIndivMasivoProceso() {
        return bienFideUnidadIndivMasivoProceso;
    }

    public void setBienFideUnidadIndivMasivoProceso(String bienFideUnidadIndivMasivoProceso) {
        this.bienFideUnidadIndivMasivoProceso = bienFideUnidadIndivMasivoProceso;
    }

    public Long getBienFideUnidadIndivMasivoContrato() {
        return bienFideUnidadIndivMasivoContrato;
    }

    public void setBienFideUnidadIndivMasivoContrato(Long bienFideUnidadIndivMasivoContrato) {
        this.bienFideUnidadIndivMasivoContrato = bienFideUnidadIndivMasivoContrato;
    }

    public Integer getBienFideUnidadIndivMasivoContratoSub() {
        return bienFideUnidadIndivMasivoContratoSub;
    }

    public void setBienFideUnidadIndivMasivoContratoSub(Integer bienFideUnidadIndivMasivoContratoSub) {
        this.bienFideUnidadIndivMasivoContratoSub = bienFideUnidadIndivMasivoContratoSub;
    }

    public String getBienFideUnidadIndivMasivoBienTipo() {
        return bienFideUnidadIndivMasivoBienTipo;
    }

    public void setBienFideUnidadIndivMasivoBienTipo(String bienFideUnidadIndivMasivoBienTipo) {
        this.bienFideUnidadIndivMasivoBienTipo = bienFideUnidadIndivMasivoBienTipo;
    }

    public Integer getBienFideUnidadIndivMasivoBienId() {
        return bienFideUnidadIndivMasivoBienId;
    }

    public void setBienFideUnidadIndivMasivoBienId(Integer bienFideUnidadIndivMasivoBienId) {
        this.bienFideUnidadIndivMasivoBienId = bienFideUnidadIndivMasivoBienId;
    }

    public Integer getBienFideUnidadIndivMasivoBienUniId() {
        return bienFideUnidadIndivMasivoBienUniId;
    }

    public void setBienFideUnidadIndivMasivoBienUniId(Integer bienFideUnidadIndivMasivoBienUniId) {
        this.bienFideUnidadIndivMasivoBienUniId = bienFideUnidadIndivMasivoBienUniId;
    }

    public Integer getBienFideUnidadIndivMasivoBienUniIndivId() {
        return bienFideUnidadIndivMasivoBienUniIndivId;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivId(Integer bienFideUnidadIndivMasivoBienUniIndivId) {
        this.bienFideUnidadIndivMasivoBienUniIndivId = bienFideUnidadIndivMasivoBienUniIndivId;
    }

    public Double getBienFideUnidadIndivMasivoBienUniIndivIndiviso() {
        return bienFideUnidadIndivMasivoBienUniIndivIndiviso;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivIndiviso(Double bienFideUnidadIndivMasivoBienUniIndivIndiviso) {
        this.bienFideUnidadIndivMasivoBienUniIndivIndiviso = bienFideUnidadIndivMasivoBienUniIndivIndiviso;
    }

    public Date getBienFideUnidadIndivMasivoBienUniIndivFechaSolicitud() {
        return bienFideUnidadIndivMasivoBienUniIndivFechaSolicitud;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivFechaSolicitud(Date bienFideUnidadIndivMasivoBienUniIndivFechaSolicitud) {
        this.bienFideUnidadIndivMasivoBienUniIndivFechaSolicitud = bienFideUnidadIndivMasivoBienUniIndivFechaSolicitud;
    }

    public Date getBienFideUnidadIndivMasivoBienUniIndivFechaInstruccion() {
        return bienFideUnidadIndivMasivoBienUniIndivFechaInstruccion;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivFechaInstruccion(Date bienFideUnidadIndivMasivoBienUniIndivFechaInstruccion) {
        this.bienFideUnidadIndivMasivoBienUniIndivFechaInstruccion = bienFideUnidadIndivMasivoBienUniIndivFechaInstruccion;
    }

    public Date getBienFideUnidadIndivMasivoBienUniIndivFechaFirma() {
        return bienFideUnidadIndivMasivoBienUniIndivFechaFirma;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivFechaFirma(Date bienFideUnidadIndivMasivoBienUniIndivFechaFirma) {
        this.bienFideUnidadIndivMasivoBienUniIndivFechaFirma = bienFideUnidadIndivMasivoBienUniIndivFechaFirma;
    }

    public Date getBienFideUnidadIndivMasivoBienUniIndivFechaDominio() {
        return bienFideUnidadIndivMasivoBienUniIndivFechaDominio;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivFechaDominio(Date bienFideUnidadIndivMasivoBienUniIndivFechaDominio) {
        this.bienFideUnidadIndivMasivoBienUniIndivFechaDominio = bienFideUnidadIndivMasivoBienUniIndivFechaDominio;
    }

    public String getBienFideUnidadIndivMasivoBienUniIndivEscritura() {
        return bienFideUnidadIndivMasivoBienUniIndivEscritura;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivEscritura(String bienFideUnidadIndivMasivoBienUniIndivEscritura) {
        this.bienFideUnidadIndivMasivoBienUniIndivEscritura = bienFideUnidadIndivMasivoBienUniIndivEscritura;
    }

    public String getBienFideUnidadIndivMasivoBienUniIndivNotario() {
        return bienFideUnidadIndivMasivoBienUniIndivNotario;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivNotario(String bienFideUnidadIndivMasivoBienUniIndivNotario) {
        this.bienFideUnidadIndivMasivoBienUniIndivNotario = bienFideUnidadIndivMasivoBienUniIndivNotario;
    }

    public String getBienFideUnidadIndivMasivoBienUniIndivAdquiriente() {
        return bienFideUnidadIndivMasivoBienUniIndivAdquiriente;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivAdquiriente(String bienFideUnidadIndivMasivoBienUniIndivAdquiriente) {
        this.bienFideUnidadIndivMasivoBienUniIndivAdquiriente = bienFideUnidadIndivMasivoBienUniIndivAdquiriente;
    }

    public String getBienFideUnidadIndivMasivoBienUniIndivStatus() {
        return bienFideUnidadIndivMasivoBienUniIndivStatus;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivStatus(String bienFideUnidadIndivMasivoBienUniIndivStatus) {
        this.bienFideUnidadIndivMasivoBienUniIndivStatus = bienFideUnidadIndivMasivoBienUniIndivStatus;
    }

    public String getBienFideUnidadIndivMasivoBienUniIndivStatusLiq() {
        return bienFideUnidadIndivMasivoBienUniIndivStatusLiq;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivStatusLiq(String bienFideUnidadIndivMasivoBienUniIndivStatusLiq) {
        this.bienFideUnidadIndivMasivoBienUniIndivStatusLiq = bienFideUnidadIndivMasivoBienUniIndivStatusLiq;
    }

    public Long getBienFideUnidadIndivMasivoBienUniIndivFolioOp() {
        return bienFideUnidadIndivMasivoBienUniIndivFolioOp;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivFolioOp(Long bienFideUnidadIndivMasivoBienUniIndivFolioOp) {
        this.bienFideUnidadIndivMasivoBienUniIndivFolioOp = bienFideUnidadIndivMasivoBienUniIndivFolioOp;
    }

    public Short getBienFideUnidadIndivMasivoBienUniIndivLineaSec() {
        return bienFideUnidadIndivMasivoBienUniIndivLineaSec;
    }

    public void setBienFideUnidadIndivMasivoBienUniIndivLineaSec(Short bienFideUnidadIndivMasivoBienUniIndivLineaSec) {
        this.bienFideUnidadIndivMasivoBienUniIndivLineaSec = bienFideUnidadIndivMasivoBienUniIndivLineaSec;
    }

    public Integer getBienFideUnidadIndivMasivoBienUsuarioNum() {
        return bienFideUnidadIndivMasivoBienUsuarioNum;
    }

    public void setBienFideUnidadIndivMasivoBienUsuarioNum(Integer bienFideUnidadIndivMasivoBienUsuarioNum) {
        this.bienFideUnidadIndivMasivoBienUsuarioNum = bienFideUnidadIndivMasivoBienUsuarioNum;
    }
    
  //Constructor
    public ContabilidadBienFideUnidadIndivMasivoBean() {
        
    }
}