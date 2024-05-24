/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : PlanBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class DatosFisoBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private Boolean bAdmonPropia;
   private Boolean bBloqueado;
   private Boolean bMonedaExtranjera;
   private Boolean bSubContrato;
   private Integer iAbiertoCerrado;
   private Integer iClasifProducto;
   private Integer iIVAEspecial;
   private Integer iNivelEstructura1;
   private Integer iNivelEstructura2;
   private Integer iNivelEstructura3;
   private Integer iNivelEstructura4;
   private Integer iNivelEstructura5;
   private Integer iProrrateaSubFiso;
   private Integer iTipoNegocio;
   private Long    lNivelEstructura;
   private Long    lNumero;
   private String  sNombre;
   private String  sStatus;
   
   
   //Constructor
   public DatosFisoBean() {}
   
   //Getters_Setters

    public Boolean getbAdmonPropia() {
        return bAdmonPropia;
    }

    public void setbAdmonPropia(Boolean bAdmonPropia) {
        this.bAdmonPropia = bAdmonPropia;
    }

    public Boolean getbBloqueado() {
        return bBloqueado;
    }

    public void setbBloqueado(Boolean bBloqueado) {
        this.bBloqueado = bBloqueado;
    }

    public Boolean getbMonedaExtranjera() {
        return bMonedaExtranjera;
    }

    public void setbMonedaExtranjera(Boolean bMonedaExtranjera) {
        this.bMonedaExtranjera = bMonedaExtranjera;
    }

    public Boolean getbSubContrato() {
        return bSubContrato;
    }

    public void setbSubContrato(Boolean bSubContrato) {
        this.bSubContrato = bSubContrato;
    }

    public Integer getiAbiertoCerrado() {
        return iAbiertoCerrado;
    }

    public void setiAbiertoCerrado(Integer iAbiertoCerrado) {
        this.iAbiertoCerrado = iAbiertoCerrado;
    }

    public Integer getiClasifProducto() {
        return iClasifProducto;
    }

    public void setiClasifProducto(Integer iClasifProducto) {
        this.iClasifProducto = iClasifProducto;
    }

    public Integer getiIVAEspecial() {
        return iIVAEspecial;
    }

    public void setiIVAEspecial(Integer iIVAEspecial) {
        this.iIVAEspecial = iIVAEspecial;
    }

    public Integer getiNivelEstructura1() {
        return iNivelEstructura1;
    }

    public void setiNivelEstructura1(Integer iNivelEstructura1) {
        this.iNivelEstructura1 = iNivelEstructura1;
    }

    public Integer getiNivelEstructura2() {
        return iNivelEstructura2;
    }

    public void setiNivelEstructura2(Integer iNivelEstructura2) {
        this.iNivelEstructura2 = iNivelEstructura2;
    }

    public Integer getiNivelEstructura3() {
        return iNivelEstructura3;
    }

    public void setiNivelEstructura3(Integer iNivelEstructura3) {
        this.iNivelEstructura3 = iNivelEstructura3;
    }

    public Integer getiNivelEstructura4() {
        return iNivelEstructura4;
    }

    public void setiNivelEstructura4(Integer iNivelEstructura4) {
        this.iNivelEstructura4 = iNivelEstructura4;
    }

    public Integer getiNivelEstructura5() {
        return iNivelEstructura5;
    }

    public void setiNivelEstructura5(Integer iNivelEstructura5) {
        this.iNivelEstructura5 = iNivelEstructura5;
    }

    public Integer getiProrrateaSubFiso() {
        return iProrrateaSubFiso;
    }

    public void setiProrrateaSubFiso(Integer iProrrateaSubFiso) {
        this.iProrrateaSubFiso = iProrrateaSubFiso;
    }

    public Integer getiTipoNegocio() {
        return iTipoNegocio;
    }

    public void setiTipoNegocio(Integer iTipoNegocio) {
        this.iTipoNegocio = iTipoNegocio;
    }

    public Long getlNivelEstructura() {
        return lNivelEstructura;
    }

    public void setlNivelEstructura(Long lNivelEstructura) {
        this.lNivelEstructura = lNivelEstructura;
    }

    public Long getlNumero() {
        return lNumero;
    }

    public void setlNumero(Long lNumero) {
        this.lNumero = lNumero;
    }

    public String getsNombre() {
        return sNombre;
    }

    public void setsNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }
   
}
