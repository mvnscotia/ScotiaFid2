/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadGarantiaBienBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210508
 * MODIFICADO  : 20210508
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadGarantiaBien")
@ViewScoped
public class ContabilidadGarantiaBienBean extends ContabilidadGarantiaGralBean{
  //Atributos privados
    private Short                      garantiaBienId;
    private String                     garantiaBienTipo;
    private String                     garantiaBienClasif;
    private Double                     garantiaBienImporte;
    private Double                     garantiaBienImporteTC;
    private Double                     garantiaBienImporteUltVal;
    private String                     garantiaBienMonedaNom;
    private Short                      garantiaBienMonedaNum;
    private String                     garantiaBienPeriodicidad;
    private Date                       garantiaBienFecha;
    private Date                       garantiaBienFechaUltVal;
    private Date                       garantiaBienFechaPrxVal;
    private String                     garantiaBienDescripcion;
    private String                     garantiaBienComentario;
    private String                     garantiaBienStatus;
    private String                     garantiaBienTituloVentana;
    private Short                      garantiaBienCveRevalua;

    private String                     txtGarantiaBienImporte;
    
  //Getters y Setters
    public Short getGarantiaBienId() {
        return garantiaBienId;
    }

    public void setGarantiaBienId(Short garantiaBienId) {
        this.garantiaBienId = garantiaBienId;
    }

    public String getGarantiaBienTipo() {
        return garantiaBienTipo;
    }

    public void setGarantiaBienTipo(String garantiaBienTipo) {
        this.garantiaBienTipo = garantiaBienTipo;
    }

    public String getGarantiaBienClasif() {
        return garantiaBienClasif;
    }

    public void setGarantiaBienClasif(String garantiaBienClasif) {
        this.garantiaBienClasif = garantiaBienClasif;
    }

    public Double getGarantiaBienImporte() {
        return garantiaBienImporte;
    }

    public void setGarantiaBienImporte(Double garantiaBienImporte) {
        this.garantiaBienImporte = garantiaBienImporte;
    }

    public Double getGarantiaBienImporteTC() {
        return garantiaBienImporteTC;
    }

    public void setGarantiaBienImporteTC(Double garantiaBienImporteTC) {
        this.garantiaBienImporteTC = garantiaBienImporteTC;
    }

    public Double getGarantiaBienImporteUltVal() {
        return garantiaBienImporteUltVal;
    }

    public void setGarantiaBienImporteUltVal(Double garantiaBienImporteUltVal) {
        this.garantiaBienImporteUltVal = garantiaBienImporteUltVal;
    }

    public String getGarantiaBienMonedaNom() {
        return garantiaBienMonedaNom;
    }

    public void setGarantiaBienMonedaNom(String garantiaBienMonedaNom) {
        this.garantiaBienMonedaNom = garantiaBienMonedaNom;
    }

    public Short getGarantiaBienMonedaNum() {
        return garantiaBienMonedaNum;
    }

    public void setGarantiaBienMonedaNum(Short garantiaBienMonedaNum) {
        this.garantiaBienMonedaNum = garantiaBienMonedaNum;
    }

    public String getGarantiaBienPeriodicidad() {
        return garantiaBienPeriodicidad;
    }

    public void setGarantiaBienPeriodicidad(String garantiaBienPeriodicidad) {
        this.garantiaBienPeriodicidad = garantiaBienPeriodicidad;
    }

    public Date getGarantiaBienFecha() {
        return garantiaBienFecha;
    }

    public void setGarantiaBienFecha(Date garantiaBienFecha) {
        this.garantiaBienFecha = garantiaBienFecha;
    }

    public Date getGarantiaBienFechaUltVal() {
        return garantiaBienFechaUltVal;
    }

    public void setGarantiaBienFechaUltVal(Date garantiaBienFechaUltVal) {
        this.garantiaBienFechaUltVal = garantiaBienFechaUltVal;
    }

    public Date getGarantiaBienFechaPrxVal() {
        return garantiaBienFechaPrxVal;
    }

    public void setGarantiaBienFechaPrxVal(Date garantiaBienFechaPrxVal) {
        this.garantiaBienFechaPrxVal = garantiaBienFechaPrxVal;
    }

    public String getGarantiaBienDescripcion() {
        return garantiaBienDescripcion;
    }

    public void setGarantiaBienDescripcion(String garantiaBienDescripcion) {
        this.garantiaBienDescripcion = garantiaBienDescripcion;
    }

    public String getGarantiaBienComentario() {
        return garantiaBienComentario;
    }

    public void setGarantiaBienComentario(String garantiaBienComentario) {
        this.garantiaBienComentario = garantiaBienComentario;
    }

    public String getGarantiaBienStatus() {
        return garantiaBienStatus;
    }

    public void setGarantiaBienStatus(String garantiaBienStatus) {
        this.garantiaBienStatus = garantiaBienStatus;
    }

    public String getGarantiaBienTituloVentana() {
        return garantiaBienTituloVentana;
    }

    public void setGarantiaBienTituloVentana(String garantiaBienTituloVentana) {
        this.garantiaBienTituloVentana = garantiaBienTituloVentana;
    }

    public Short getGarantiaBienCveRevalua() {
        return garantiaBienCveRevalua;
    }

    public void setGarantiaBienCveRevalua(Short garantiaBienCveRevalua) {
        this.garantiaBienCveRevalua = garantiaBienCveRevalua;
    }
    
  //Constructor
    public ContabilidadGarantiaBienBean() {
        
    }

    public String getTxtGarantiaBienImporte() {
        return txtGarantiaBienImporte;
    }

    public void setTxtGarantiaBienImporte(String txtGarantiaBienImporte) {
        this.txtGarantiaBienImporte = txtGarantiaBienImporte;
    }
}