/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideUnidadIndivBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210513
 * MODIFICADO  : 20210513
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadBienFideUnidadIndiv")
@ViewScoped
public class ContabilidadBienFideUnidadIndivBean extends ContabilidadBienFideUnidadBean{
  //Atributos privados
    private Integer                    bienFideUnidadIndivSec;
    private String                     bienFideUnidadIndivDeptoNum;
    private String                     bienFideUnidadIndivTipo;
    private String                     bienFideUnidadIndivUbicacion;
    private String                     bienFideUnidadIndivSuperficieM2;
    private String                     bienFideUnidadIndivIndiviso;
    private String                     bienFideUnidadIndivNiveles;
    private Double                     bienFideUnidadIndivPrecio;
    private String                     bienFideUnidadIndivMedidas;
    private Double                     bienFideUnidadIndivUltAvaluoValor;
    private Date                       bienFideUnidadindivUlAvaluoFecha;
    private String                     bienFideUnidadIndivMonedaNom;
    private Short                      bienFideUnidadIndivMonedaNum;
    private String                     bienFideUnidadIndivStatusInmueble;
    private String                     bienFideUnidadIndivStatus;
    private String                     bienFideUnidadIndivCveLiq;

    
    private String                     txtBienFideUnidadIndivPrecio;

    //Getters y Setters
    public Integer getBienFideUnidadIndivSec() {
        return bienFideUnidadIndivSec;
    }

    public void setBienFideUnidadIndivSec(Integer bienFideUnidadIndivSec) {
        this.bienFideUnidadIndivSec = bienFideUnidadIndivSec;
    }

    public String getBienFideUnidadIndivDeptoNum() {
        return bienFideUnidadIndivDeptoNum;
    }

    public void setBienFideUnidadIndivDeptoNum(String bienFideUnidadIndivDeptoNum) {
        this.bienFideUnidadIndivDeptoNum = bienFideUnidadIndivDeptoNum;
    }

    public String getBienFideUnidadIndivTipo() {
        return bienFideUnidadIndivTipo;
    }

    public void setBienFideUnidadIndivTipo(String bienFideUnidadIndivTipo) {
        this.bienFideUnidadIndivTipo = bienFideUnidadIndivTipo;
    }

    public String getBienFideUnidadIndivUbicacion() {
        return bienFideUnidadIndivUbicacion;
    }

    public void setBienFideUnidadIndivUbicacion(String bienFideUnidadIndivUbicacion) {
        this.bienFideUnidadIndivUbicacion = bienFideUnidadIndivUbicacion;
    }

    public String getBienFideUnidadIndivSuperficieM2() {
        return bienFideUnidadIndivSuperficieM2;
    }

    public void setBienFideUnidadIndivSuperficieM2(String bienFideUnidadIndivSuperficieM2) {
        this.bienFideUnidadIndivSuperficieM2 = bienFideUnidadIndivSuperficieM2;
    }

    public String getBienFideUnidadIndivIndiviso() {
        return bienFideUnidadIndivIndiviso;
    }

    public void setBienFideUnidadIndivIndiviso(String bienFideUnidadIndivIndiviso) {
        this.bienFideUnidadIndivIndiviso = bienFideUnidadIndivIndiviso;
    }

    public String getBienFideUnidadIndivNiveles() {
        return bienFideUnidadIndivNiveles;
    }

    public void setBienFideUnidadIndivNiveles(String bienFideUnidadIndivNiveles) {
        this.bienFideUnidadIndivNiveles = bienFideUnidadIndivNiveles;
    }

    public Double getBienFideUnidadIndivPrecio() {
        return bienFideUnidadIndivPrecio;
    }

    public void setBienFideUnidadIndivPrecio(Double bienFideUnidadIndivPrecio) {
        this.bienFideUnidadIndivPrecio = bienFideUnidadIndivPrecio;
    }

    public String getBienFideUnidadIndivMedidas() {
        return bienFideUnidadIndivMedidas;
    }

    public void setBienFideUnidadIndivMedidas(String bienFideUnidadIndivMedidas) {
        this.bienFideUnidadIndivMedidas = bienFideUnidadIndivMedidas;
    }

    public Double getBienFideUnidadIndivUltAvaluoValor() {
        return bienFideUnidadIndivUltAvaluoValor;
    }

    public void setBienFideUnidadIndivUltAvaluoValor(Double bienFideUnidadIndivUltAvaluoValor) {
        this.bienFideUnidadIndivUltAvaluoValor = bienFideUnidadIndivUltAvaluoValor;
    }

    public Date getBienFideUnidadindivUlAvaluoFecha() {
        return bienFideUnidadindivUlAvaluoFecha;
    }

    public void setBienFideUnidadindivUlAvaluoFecha(Date bienFideUnidadindivUlAvaluoFecha) {
        this.bienFideUnidadindivUlAvaluoFecha = bienFideUnidadindivUlAvaluoFecha;
    }

    public String getBienFideUnidadIndivMonedaNom() {
        return bienFideUnidadIndivMonedaNom;
    }

    public void setBienFideUnidadIndivMonedaNom(String bienFideUnidadIndivMonedaNom) {
        this.bienFideUnidadIndivMonedaNom = bienFideUnidadIndivMonedaNom;
    }

    public Short getBienFideUnidadIndivMonedaNum() {
        return bienFideUnidadIndivMonedaNum;
    }

    public void setBienFideUnidadIndivMonedaNum(Short bienFideUnidadIndivMonedaNum) {
        this.bienFideUnidadIndivMonedaNum = bienFideUnidadIndivMonedaNum;
    }

    public String getBienFideUnidadIndivStatusInmueble() {
        return bienFideUnidadIndivStatusInmueble;
    }

    public void setBienFideUnidadIndivStatusInmueble(String bienFideUnidadIndivStatusInmueble) {
        this.bienFideUnidadIndivStatusInmueble = bienFideUnidadIndivStatusInmueble;
    }

    public String getBienFideUnidadIndivStatus() {
        return bienFideUnidadIndivStatus;
    }

    public void setBienFideUnidadIndivStatus(String bienFideUnidadIndivStatus) {
        this.bienFideUnidadIndivStatus = bienFideUnidadIndivStatus;
    }

    public String getBienFideUnidadIndivCveLiq() {
        return bienFideUnidadIndivCveLiq;
    }

    public void setBienFideUnidadIndivCveLiq(String bienFideUnidadIndivCveLiq) {
        this.bienFideUnidadIndivCveLiq = bienFideUnidadIndivCveLiq;
    }
    
  //Constructor
    public ContabilidadBienFideUnidadIndivBean() {
        
    }

    public String getTxtBienFideUnidadIndivPrecio() {
        return txtBienFideUnidadIndivPrecio;
    }

    public void setTxtBienFideUnidadIndivPrecio(String txtBienFideUnidadIndivPrecio) {
        this.txtBienFideUnidadIndivPrecio = txtBienFideUnidadIndivPrecio;
    }
}