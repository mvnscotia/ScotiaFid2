/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadGarantiaGralBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210310
 * MODIFICADO  : 20210310
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadGarantiaGral")
@ViewScoped
public class ContabilidadGarantiaGralBean implements Serializable{
  //Atributos privados
    private Long                            garantiaContratoNumero;
    private Integer                         garantiaContratoNumeroSub;
    private String                          garantiaContratoNombre;
    private String[]                        garantiaTipo;
    private String                          garantiaTipoInmuebles;
    private String                          garantiaTipoMuebles;
    private String                          garantiaTipoValores;
    private String                          garantiaTipoOtros;
    private String                          garantiaTipoNumerario;
    private String                          garantiaRevaluaCve;
    private String                          garantiaRevaluaPeriodo;
    private Date                            garantiaFecha;
    private Date                            garantiaRevaluaFechaIni;
    private Date                            garantiaRevaluaFechaFin;
    private String                          garantiaRelacionCredGar;
    private Double                          garantiaImporteCredito;
    private Double                          garantiaImporteGarantia;
    private Double                          garantiaImporteUltVal;
    private Double                          garantiaImporteNumerario;
    private String                          garantiaMonedaNombre;
    private Integer                         garantiaMonedaNumero;
    private String                          garantiaDesc;
    private String                          garantiaComentario;
    private String                          garantiaStatus;
    private String                          garantiaBitTipoOper;
    private Integer                         garantiaBitUsuario;
    private String                          garantiaBitPantalla;
    private String                          garantiaBitIP;
    
    private String                          txtGarantiaContratoNumero;
    private String                          txtGarantiaContratoNumeroSub;
    private String                          txtGarantiaImporteCredito;

    
//Getters y Setters

    public Long getGarantiaContratoNumero() {
        return garantiaContratoNumero;
    }

    public void setGarantiaContratoNumero(Long garantiaContratoNumero) {
        this.garantiaContratoNumero = garantiaContratoNumero;
    }

    public Integer getGarantiaContratoNumeroSub() {
        return garantiaContratoNumeroSub;
    }

    public void setGarantiaContratoNumeroSub(Integer garantiaContratoNumeroSub) {
        this.garantiaContratoNumeroSub = garantiaContratoNumeroSub;
    }

    public String getGarantiaContratoNombre() {
        return garantiaContratoNombre;
    }

    public void setGarantiaContratoNombre(String garantiaContratoNombre) {
        this.garantiaContratoNombre = garantiaContratoNombre;
    }

    public String[] getGarantiaTipo() {
        return garantiaTipo;
    }

    public void setGarantiaTipo(String[] garantiaTipo) {
        this.garantiaTipo = garantiaTipo;
    }

    public String getGarantiaTipoInmuebles() {
        return garantiaTipoInmuebles;
    }

    public void setGarantiaTipoInmuebles(String garantiaTipoInmuebles) {
        this.garantiaTipoInmuebles = garantiaTipoInmuebles;
    }

    public String getGarantiaTipoMuebles() {
        return garantiaTipoMuebles;
    }

    public void setGarantiaTipoMuebles(String garantiaTipoMuebles) {
        this.garantiaTipoMuebles = garantiaTipoMuebles;
    }

    public String getGarantiaTipoValores() {
        return garantiaTipoValores;
    }

    public void setGarantiaTipoValores(String garantiaTipoValores) {
        this.garantiaTipoValores = garantiaTipoValores;
    }

    public String getGarantiaTipoOtros() {
        return garantiaTipoOtros;
    }

    public void setGarantiaTipoOtros(String garantiaTipoOtros) {
        this.garantiaTipoOtros = garantiaTipoOtros;
    }

    public String getGarantiaTipoNumerario() {
        return garantiaTipoNumerario;
    }

    public void setGarantiaTipoNumerario(String garantiaTipoNumerario) {
        this.garantiaTipoNumerario = garantiaTipoNumerario;
    }

    public String getGarantiaRevaluaCve() {
        return garantiaRevaluaCve;
    }

    public void setGarantiaRevaluaCve(String garantiaRevaluaCve) {
        this.garantiaRevaluaCve = garantiaRevaluaCve;
    }

    public String getGarantiaRevaluaPeriodo() {
        return garantiaRevaluaPeriodo;
    }

    public void setGarantiaRevaluaPeriodo(String garantiaRevaluaPeriodo) {
        this.garantiaRevaluaPeriodo = garantiaRevaluaPeriodo;
    }

    public Date getGarantiaFecha() {
        return garantiaFecha;
    }

    public void setGarantiaFecha(Date garantiaFecha) {
        this.garantiaFecha = garantiaFecha;
    }

    public Date getGarantiaRevaluaFechaIni() {
        return garantiaRevaluaFechaIni;
    }

    public void setGarantiaRevaluaFechaIni(Date garantiaRevaluaFechaIni) {
        this.garantiaRevaluaFechaIni = garantiaRevaluaFechaIni;
    }

    public Date getGarantiaRevaluaFechaFin() {
        return garantiaRevaluaFechaFin;
    }

    public void setGarantiaRevaluaFechaFin(Date garantiaRevaluaFechaFin) {
        this.garantiaRevaluaFechaFin = garantiaRevaluaFechaFin;
    }

    public String getGarantiaRelacionCredGar() {
        return garantiaRelacionCredGar;
    }

    public void setGarantiaRelacionCredGar(String garantiaRelacionCredGar) {
        this.garantiaRelacionCredGar = garantiaRelacionCredGar;
    }

    public Double getGarantiaImporteCredito() {
        return garantiaImporteCredito;
    }

    public void setGarantiaImporteCredito(Double garantiaImporteCredito) {
        this.garantiaImporteCredito = garantiaImporteCredito;
    }

    public Double getGarantiaImporteGarantia() {
        return garantiaImporteGarantia;
    }

    public void setGarantiaImporteGarantia(Double garantiaImporteGarantia) {
        this.garantiaImporteGarantia = garantiaImporteGarantia;
    }

    public Double getGarantiaImporteUltVal() {
        return garantiaImporteUltVal;
    }

    public void setGarantiaImporteUltVal(Double garantiaImporteUltVal) {
        this.garantiaImporteUltVal = garantiaImporteUltVal;
    }

    public Double getGarantiaImporteNumerario() {
        return garantiaImporteNumerario;
    }

    public void setGarantiaImporteNumerario(Double garantiaImporteNumerario) {
        this.garantiaImporteNumerario = garantiaImporteNumerario;
    }

    public String getGarantiaMonedaNombre() {
        return garantiaMonedaNombre;
    }

    public void setGarantiaMonedaNombre(String garantiaMonedaNombre) {
        this.garantiaMonedaNombre = garantiaMonedaNombre;
    }

    public Integer getGarantiaMonedaNumero() {
        return garantiaMonedaNumero;
    }

    public void setGarantiaMonedaNumero(Integer garantiaMonedaNumero) {
        this.garantiaMonedaNumero = garantiaMonedaNumero;
    }

    public String getGarantiaDesc() {
        return garantiaDesc;
    }

    public void setGarantiaDesc(String garantiaDesc) {
        this.garantiaDesc = garantiaDesc;
    }

    public String getGarantiaComentario() {
        return garantiaComentario;
    }

    public void setGarantiaComentario(String garantiaComentario) {
        this.garantiaComentario = garantiaComentario;
    }

    public String getGarantiaStatus() {
        return garantiaStatus;
    }

    public void setGarantiaStatus(String garantiaStatus) {
        this.garantiaStatus = garantiaStatus;
    }

    public String getGarantiaBitTipoOper() {
        return garantiaBitTipoOper;
    }

    public void setGarantiaBitTipoOper(String garantiaBitTipoOper) {
        this.garantiaBitTipoOper = garantiaBitTipoOper;
    }

    public Integer getGarantiaBitUsuario() {
        return garantiaBitUsuario;
    }

    public void setGarantiaBitUsuario(Integer garantiaBitUsuario) {
        this.garantiaBitUsuario = garantiaBitUsuario;
    }

    public String getGarantiaBitPantalla() {
        return garantiaBitPantalla;
    }

    public void setGarantiaBitPantalla(String garantiaBitPantalla) {
        this.garantiaBitPantalla = garantiaBitPantalla;
    }

    public String getGarantiaBitIP() {
        return garantiaBitIP;
    }

    public void setGarantiaBitIP(String garantiaBitIP) {
        this.garantiaBitIP = garantiaBitIP;
    }
    
  //Constructor
    public ContabilidadGarantiaGralBean() {
        
    }

    public String getTxtGarantiaContratoNumero() {
        return txtGarantiaContratoNumero;
    }

    public void setTxtGarantiaContratoNumero(String txtGarantiaContratoNumero) {
        this.txtGarantiaContratoNumero = txtGarantiaContratoNumero;
    }

    public String getTxtGarantiaContratoNumeroSub() {
        return txtGarantiaContratoNumeroSub;
    }

    public void setTxtGarantiaContratoNumeroSub(String txtGarantiaContratoNumeroSub) {
        this.txtGarantiaContratoNumeroSub = txtGarantiaContratoNumeroSub;
    }

    public String getTxtGarantiaImporteCredito() {
        return txtGarantiaImporteCredito;
    }

    public void setTxtGarantiaImporteCredito(String txtGarantiaImporteCredito) {
        this.txtGarantiaImporteCredito = txtGarantiaImporteCredito;
    }
}