/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabanck
 * ARCHIVO     : ContabilidadDetValorBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20211027
 * MODIFICADO  : 20211027
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadDetValor")
@ViewScoped
public class ContabilidadDetValorBean implements Serializable{
  //Atributos privados
    private Long                       detValContratoNumero;
    private Integer                    detValContratoNumeroSub;
    private String                     detValContratoNombre;
    private String                     detValContratoNombreSub;
    private String                     detValContratoInver;
    private String                     detValOperacionId;
    private String                     detValOperacionNombre;
    private String                     detValTransaccionId;
    private String                     detValTransaccionNombre;
    private Long                       detValFolio;
    private Short                      detValSec;
    private String                     detValHoraReg;
    private Short                      detValIntermedSec;
    private String                     detValIntermedNom;
    private String                     detValPizarra;
    private String                     detValSerie;
    private String                     detValCupon;
    private Double                     detValTituloPrecio;
    private Double                     detValTituloNum;
    private Double                     detValTituloImp;
    private Double                     detValTasaDesc;
    private Double                     detValTasaRend;
    private Double                     detValImpUtilidad;
    private Double                     detValImpPerdida;
    private Short                      detValRepIntermedSec;
    private String                     detValRepIntermedNom;
    private Long                       detValRepFolio;
    private String                     detValRepInstrume;
    private Short                      detValRepPlazo;
    private Double                     detValRepTasa;
    private Double                     detValRepPremio;
    private Double                     detValRepInteres;
    private Double                     detValRepISR;
    private Short                      detValRepMdaSec;
    private String                     detValRepMdaNom;
    private Double                     detValRepImporte;
    private Double                     detValRepImporteME;
    private Double                     detValRepImporteTC;
    private String                     detValDereTipo;
    private String                     detValDereFactor;
    private String                     detValDerePizarra;
    private String                     detValDereSerieAnt;
    private String                     detValDereCupon;
    private String                     muestraValorDetalle;

    public String getMuestraValorDetalle() {
        return muestraValorDetalle;
    }

    public void setMuestraValorDetalle(String muestraValorDetalle) {
        this.muestraValorDetalle = muestraValorDetalle;
    }


    
  //Getters y Setters
    public Long getDetValContratoNumero() {
        return detValContratoNumero;
    }

    public void setDetValContratoNumero(Long detValContratoNumero) {
        this.detValContratoNumero = detValContratoNumero;
    }

    public Integer getDetValContratoNumeroSub() {
        return detValContratoNumeroSub;
    }

    public void setDetValContratoNumeroSub(Integer detValContratoNumeroSub) {
        this.detValContratoNumeroSub = detValContratoNumeroSub;
    }

    public String getDetValContratoNombre() {
        return detValContratoNombre;
    }

    public void setDetValContratoNombre(String detValContratoNombre) {
        this.detValContratoNombre = detValContratoNombre;
    }

    public String getDetValContratoNombreSub() {
        return detValContratoNombreSub;
    }

    public void setDetValContratoNombreSub(String detValContratoNombreSub) {
        this.detValContratoNombreSub = detValContratoNombreSub;
    }

    public String getDetValContratoInver() {
        return detValContratoInver;
    }

    public void setDetValContratoInver(String detValContratoInver) {
        this.detValContratoInver = detValContratoInver;
    }

    public String getDetValOperacionId() {
        return detValOperacionId;
    }

    public void setDetValOperacionId(String detValOperacionId) {
        this.detValOperacionId = detValOperacionId;
    }

    public String getDetValOperacionNombre() {
        return detValOperacionNombre;
    }

    public void setDetValOperacionNombre(String detValOperacionNombre) {
        this.detValOperacionNombre = detValOperacionNombre;
    }

    public String getDetValTransaccionId() {
        return detValTransaccionId;
    }

    public void setDetValTransaccionId(String detValTransaccionId) {
        this.detValTransaccionId = detValTransaccionId;
    }

    public String getDetValTransaccionNombre() {
        return detValTransaccionNombre;
    }

    public void setDetValTransaccionNombre(String detValTransaccionNombre) {
        this.detValTransaccionNombre = detValTransaccionNombre;
    }

    public Long getDetValFolio() {
        return detValFolio;
    }

    public void setDetValFolio(Long detValFolio) {
        this.detValFolio = detValFolio;
    }

    public Short getDetValSec() {
        return detValSec;
    }

    public void setDetValSec(Short detValSec) {
        this.detValSec = detValSec;
    }

    public String getDetValHoraReg() {
        return detValHoraReg;
    }

    public void setDetValHoraReg(String detValHoraReg) {
        this.detValHoraReg = detValHoraReg;
    }

    public Short getDetValIntermedSec() {
        return detValIntermedSec;
    }

    public void setDetValIntermedSec(Short detValIntermedSec) {
        this.detValIntermedSec = detValIntermedSec;
    }

    public String getDetValIntermedNom() {
        return detValIntermedNom;
    }

    public void setDetValIntermedNom(String detValIntermedNom) {
        this.detValIntermedNom = detValIntermedNom;
    }

    public String getDetValPizarra() {
        return detValPizarra;
    }

    public void setDetValPizarra(String detValPizarra) {
        this.detValPizarra = detValPizarra;
    }

    public String getDetValSerie() {
        return detValSerie;
    }

    public void setDetValSerie(String detValSerie) {
        this.detValSerie = detValSerie;
    }

    public String getDetValCupon() {
        return detValCupon;
    }

    public void setDetValCupon(String detValCupon) {
        this.detValCupon = detValCupon;
    }

    public Double getDetValTituloPrecio() {
        return detValTituloPrecio;
    }

    public void setDetValTituloPrecio(Double detValTituloPrecio) {
        this.detValTituloPrecio = detValTituloPrecio;
    }

    public Double getDetValTituloNum() {
        return detValTituloNum;
    }

    public void setDetValTituloNum(Double detValTituloNum) {
        this.detValTituloNum = detValTituloNum;
    }

   

    public Double getDetValTituloImp() {
        return detValTituloImp;
    }

    public void setDetValTituloImp(Double detValTituloImp) {
        this.detValTituloImp = detValTituloImp;
    }

    public Double getDetValTasaDesc() {
        return detValTasaDesc;
    }

    public void setDetValTasaDesc(Double detValTasaDesc) {
        this.detValTasaDesc = detValTasaDesc;
    }

    public Double getDetValTasaRend() {
        return detValTasaRend;
    }

    public void setDetValTasaRend(Double detValTasaRend) {
        this.detValTasaRend = detValTasaRend;
    }

    public Double getDetValImpUtilidad() {
        return detValImpUtilidad;
    }

    public void setDetValImpUtilidad(Double detValImpUtilidad) {
        this.detValImpUtilidad = detValImpUtilidad;
    }

    public Double getDetValImpPerdida() {
        return detValImpPerdida;
    }

    public void setDetValImpPerdida(Double detValImpPerdida) {
        this.detValImpPerdida = detValImpPerdida;
    }

    public Short getDetValRepIntermedSec() {
        return detValRepIntermedSec;
    }

    public void setDetValRepIntermedSec(Short detValRepIntermedSec) {
        this.detValRepIntermedSec = detValRepIntermedSec;
    }

    public String getDetValRepIntermedNom() {
        return detValRepIntermedNom;
    }

    public void setDetValRepIntermedNom(String detValRepIntermedNom) {
        this.detValRepIntermedNom = detValRepIntermedNom;
    }

    public Long getDetValRepFolio() {
        return detValRepFolio;
    }

    public void setDetValRepFolio(Long detValRepFolio) {
        this.detValRepFolio = detValRepFolio;
    }

    public String getDetValRepInstrume() {
        return detValRepInstrume;
    }

    public void setDetValRepInstrume(String detValRepInstrume) {
        this.detValRepInstrume = detValRepInstrume;
    }

    public Short getDetValRepPlazo() {
        return detValRepPlazo;
    }

    public void setDetValRepPlazo(Short detValRepPlazo) {
        this.detValRepPlazo = detValRepPlazo;
    }

    public Double getDetValRepTasa() {
        return detValRepTasa;
    }

    public void setDetValRepTasa(Double detValRepTasa) {
        this.detValRepTasa = detValRepTasa;
    }

    public Double getDetValRepPremio() {
        return detValRepPremio;
    }

    public void setDetValRepPremio(Double detValRepPremio) {
        this.detValRepPremio = detValRepPremio;
    }

    public Double getDetValRepInteres() {
        return detValRepInteres;
    }

    public void setDetValRepInteres(Double detValRepInteres) {
        this.detValRepInteres = detValRepInteres;
    }

    public Double getDetValRepISR() {
        return detValRepISR;
    }

    public void setDetValRepISR(Double detValRepISR) {
        this.detValRepISR = detValRepISR;
    }

    public Short getDetValRepMdaSec() {
        return detValRepMdaSec;
    }

    public void setDetValRepMdaSec(Short detValRepMdaSec) {
        this.detValRepMdaSec = detValRepMdaSec;
    }

    public String getDetValRepMdaNom() {
        return detValRepMdaNom;
    }

    public void setDetValRepMdaNom(String detValRepMdaNom) {
        this.detValRepMdaNom = detValRepMdaNom;
    }

    public Double getDetValRepImporte() {
        return detValRepImporte;
    }

    public void setDetValRepImporte(Double detValRepImporte) {
        this.detValRepImporte = detValRepImporte;
    }

    public Double getDetValRepImporteME() {
        return detValRepImporteME;
    }

    public void setDetValRepImporteME(Double detValRepImporteME) {
        this.detValRepImporteME = detValRepImporteME;
    }

    public Double getDetValRepImporteTC() {
        return detValRepImporteTC;
    }

    public void setDetValRepImporteTC(Double detValRepImporteTC) {
        this.detValRepImporteTC = detValRepImporteTC;
    }

    public String getDetValDereTipo() {
        return detValDereTipo;
    }

    public void setDetValDereTipo(String detValDereTipo) {
        this.detValDereTipo = detValDereTipo;
    }

    public String getDetValDereFactor() {
        return detValDereFactor;
    }

    public void setDetValDereFactor(String detValDereFactor) {
        this.detValDereFactor = detValDereFactor;
    }

    public String getDetValDerePizarra() {
        return detValDerePizarra;
    }

    public void setDetValDerePizarra(String detValDerePizarra) {
        this.detValDerePizarra = detValDerePizarra;
    }

    public String getDetValDereSerieAnt() {
        return detValDereSerieAnt;
    }

    public void setDetValDereSerieAnt(String detValDereSerieAnt) {
        this.detValDereSerieAnt = detValDereSerieAnt;
    }

    public String getDetValDereCupon() {
        return detValDereCupon;
    }

    public void setDetValDereCupon(String detValDereCupon) {
        this.detValDereCupon = detValDereCupon;
    }
    
  //Constructor
    public ContabilidadDetValorBean() {
        
    }
}