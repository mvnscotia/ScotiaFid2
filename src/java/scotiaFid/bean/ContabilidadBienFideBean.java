/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210324
 * MODIFICADO  : 20210324
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadBienFide")
@ViewScoped
public class ContabilidadBienFideBean implements Serializable{
  //Atributos privados
    private Long                       bienFideContratoNum;
    private Integer                    bienFideContratoNumSub;
    private String                     bienFideContratoNom;
    private String                     bienFideTipo;
    private String                     bienFideClasificacion;
    private Integer                    bienFideId;
    private Double                     bienFideImporte;
    private Double                     bienFideImporteNvo;
    private Double                     bienFideImporteTC;
    private Double                     bienFideImporteUltVal;
    private String                     bienFidePeriodicidadRev;
    private String                     bienFideMonedaNom;
    private Integer                    bienFideMonedaNum;
    private Date                       bienFideFechaValor;
    private Date                       bienFideFechaValUlt;
    private Date                       bienFideFechaValProx;
    private String                     bienFideDescripcion;
    private String                     bienFideComentario;
    private Date                       bienFideFechaReg;
    private Date                       bienFideFechaMod;
    private String                     bienFideStatus;
    private String                     bienFideBitTipoOperacion;
    private String                     bienFideBitPantalla;
    private String                     bienFideBitTerminal;
    private Integer                    bienFideBitUsuario;
    private String                     bienFideFechaValUltStr;
  //Getetrs y Setters
    private String                     txtBienFideImporte;
    private String                     txtBienFideImporteNvo;
    public Long getBienFideContratoNum() {
        return bienFideContratoNum;
    }

    public void setBienFideContratoNum(Long bienFideContratoNum) {
        this.bienFideContratoNum = bienFideContratoNum;
    }

    public Integer getBienFideContratoNumSub() {
        return bienFideContratoNumSub;
    }

    public void setBienFideContratoNumSub(Integer bienFideContratoNumSub) {
        this.bienFideContratoNumSub = bienFideContratoNumSub;
    }

    public String getBienFideContratoNom() {
        return bienFideContratoNom;
    }

    public void setBienFideContratoNom(String bienFideContratoNom) {
        this.bienFideContratoNom = bienFideContratoNom;
    }

    public String getBienFideTipo() {
        return bienFideTipo;
    }

    public void setBienFideTipo(String bienFideTipo) {
        this.bienFideTipo = bienFideTipo;
    }

    public String getBienFideClasificacion() {
        return bienFideClasificacion;
    }

    public void setBienFideClasificacion(String bienFideClasificacion) {
        this.bienFideClasificacion = bienFideClasificacion;
    }

    public Integer getBienFideId() {
        return bienFideId;
    }

    public void setBienFideId(Integer bienFideId) {
        this.bienFideId = bienFideId;
    }

    public Double getBienFideImporte() {
        return bienFideImporte;
    }

    public void setBienFideImporte(Double bienFideImporte) {
        this.bienFideImporte = bienFideImporte;
    }

    public Double getBienFideImporteNvo() {
        return bienFideImporteNvo;
    }

    public void setBienFideImporteNvo(Double bienFideImporteNvo) {
        this.bienFideImporteNvo = bienFideImporteNvo;
    }

    public Double getBienFideImporteTC() {
        return bienFideImporteTC;
    }

    public void setBienFideImporteTC(Double bienFideImporteTC) {
        this.bienFideImporteTC = bienFideImporteTC;
    }

    public Double getBienFideImporteUltVal() {
        return bienFideImporteUltVal;
    }

    public void setBienFideImporteUltVal(Double bienFideImporteUltVal) {
        this.bienFideImporteUltVal = bienFideImporteUltVal;
    }

    public String getBienFidePeriodicidadRev() {
        return bienFidePeriodicidadRev;
    }

    public void setBienFidePeriodicidadRev(String bienFidePeriodicidadRev) {
        this.bienFidePeriodicidadRev = bienFidePeriodicidadRev;
    }

    public String getBienFideMonedaNom() {
        return bienFideMonedaNom;
    }

    public void setBienFideMonedaNom(String bienFideMonedaNom) {
        this.bienFideMonedaNom = bienFideMonedaNom;
    }

    public Integer getBienFideMonedaNum() {
        return bienFideMonedaNum;
    }

    public void setBienFideMonedaNum(Integer bienFideMonedaNum) {
        this.bienFideMonedaNum = bienFideMonedaNum;
    }

    public Date getBienFideFechaValor() {
        return bienFideFechaValor;
    }

    public void setBienFideFechaValor(Date bienFideFechaValor) {
        this.bienFideFechaValor = bienFideFechaValor;
    }

    public Date getBienFideFechaValUlt() {
        return bienFideFechaValUlt;
    }

    public void setBienFideFechaValUlt(Date bienFideFechaValUlt) {
        this.bienFideFechaValUlt = bienFideFechaValUlt;
    }

    public Date getBienFideFechaValProx() {
        return bienFideFechaValProx;
    }

    public void setBienFideFechaValProx(Date bienFideFechaValProx) {
        this.bienFideFechaValProx = bienFideFechaValProx;
    }

    public String getBienFideDescripcion() {
        return bienFideDescripcion;
    }

    public void setBienFideDescripcion(String bienFideDescripcion) {
        this.bienFideDescripcion = bienFideDescripcion;
    }

    public String getBienFideComentario() {
        return bienFideComentario;
    }

    public void setBienFideComentario(String bienFideComentario) {
        this.bienFideComentario = bienFideComentario;
    }

    public Date getBienFideFechaReg() {
        return bienFideFechaReg;
    }

    public void setBienFideFechaReg(Date bienFideFechaReg) {
        this.bienFideFechaReg = bienFideFechaReg;
    }

    public Date getBienFideFechaMod() {
        return bienFideFechaMod;
    }

    public void setBienFideFechaMod(Date bienFideFechaMod) {
        this.bienFideFechaMod = bienFideFechaMod;
    }

    public String getBienFideStatus() {
        return bienFideStatus;
    }

    public void setBienFideStatus(String bienFideStatus) {
        this.bienFideStatus = bienFideStatus;
    }

    public String getBienFideBitTipoOperacion() {
        return bienFideBitTipoOperacion;
    }

    public void setBienFideBitTipoOperacion(String bienFideBitTipoOperacion) {
        this.bienFideBitTipoOperacion = bienFideBitTipoOperacion;
    }

    public String getBienFideBitPantalla() {
        return bienFideBitPantalla;
    }

    public void setBienFideBitPantalla(String bienFideBitPantalla) {
        this.bienFideBitPantalla = bienFideBitPantalla;
    }

    public String getBienFideBitTerminal() {
        return bienFideBitTerminal;
    }

    public void setBienFideBitTerminal(String bienFideBitTerminal) {
        this.bienFideBitTerminal = bienFideBitTerminal;
    }

    public Integer getBienFideBitUsuario() {
        return bienFideBitUsuario;
    }

    public void setBienFideBitUsuario(Integer bienFideBitUsuario) {
        this.bienFideBitUsuario = bienFideBitUsuario;
    }
    
  //Constructor
    public ContabilidadBienFideBean() {
        
    }

    public String getBienFideFechaValUltStr() {
        return bienFideFechaValUltStr;
    }

    public void setBienFideFechaValUltStr(String bienFideFechaValUltStr) {
        this.bienFideFechaValUltStr = bienFideFechaValUltStr;
    }

    public String getTxtBienFideImporte() {
        return txtBienFideImporte;
    }

    public void setTxtBienFideImporte(String txtBienFideImporte) {
        this.txtBienFideImporte = txtBienFideImporte;
    }

    public String getTxtBienFideImporteNvo() {
        return txtBienFideImporteNvo;
    }

    public void setTxtBienFideImporteNvo(String txtBienFideImporteNvo) {
        this.txtBienFideImporteNvo = txtBienFideImporteNvo;
    }
}