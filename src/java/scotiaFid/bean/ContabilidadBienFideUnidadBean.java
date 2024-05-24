/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideUnidadBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210420
 * MODIFICADO  : 20210420
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadBienFideUnidad")
@ViewScoped
public class ContabilidadBienFideUnidadBean extends ContabilidadBienFideBean{
  //Atributos privados
    private Integer                    bienFideUnidadSec;
    private String                     bienFideUnidadNombre;
    private String                     bienFideUnidadDomicilio;
    private String                     bienFideUnidadTipo;
    private String                     bienFideUnidadEscrituraIni;
    private Date                       bienFideUnidadFechaCto;
    private String                     bienFideUnidadNotarioLista;
    private String                     bienFideUnidadNotarioLocalidad;
    private String                     bienFideUnidadNotarioNom;
    private Integer                    bienFideUnidadNotarioNum;
    private Integer                    bienFideUnidadNotarioNumOFicial;
    private Double                     bienFideUnidadValor;
    private String                     bienFideUnidadMonedaNom;
    private Integer                    bienFideUnidadMonedaNum;
    private Double                     bienFideUnidadSuperficie;
    private String                     bienFideUnidadObservacion;
    private String                     bienFideUnidadStatus;
    private Double                     bienFideUnidadAcumRegContable;
    
    private String                     txtBienFideUnidadValor;
    private String                     txtBienFideUnidadSuperficie;
  //Getters y Setters
    public Integer getBienFideUnidadSec() {
        return bienFideUnidadSec;
    }

    public void setBienFideUnidadSec(Integer bienFideUnidadSec) {
        this.bienFideUnidadSec = bienFideUnidadSec;
    }

    public String getBienFideUnidadNombre() {
        return bienFideUnidadNombre;
    }

    public void setBienFideUnidadNombre(String bienFideUnidadNombre) {
        this.bienFideUnidadNombre = bienFideUnidadNombre;
    }

    public String getBienFideUnidadDomicilio() {
        return bienFideUnidadDomicilio;
    }

    public void setBienFideUnidadDomicilio(String bienFideUnidadDomicilio) {
        this.bienFideUnidadDomicilio = bienFideUnidadDomicilio;
    }

    public String getBienFideUnidadTipo() {
        return bienFideUnidadTipo;
    }

    public void setBienFideUnidadTipo(String bienFideUnidadTipo) {
        this.bienFideUnidadTipo = bienFideUnidadTipo;
    }

    public String getBienFideUnidadEscrituraIni() {
        return bienFideUnidadEscrituraIni;
    }

    public void setBienFideUnidadEscrituraIni(String bienFideUnidadEscrituraIni) {
        this.bienFideUnidadEscrituraIni = bienFideUnidadEscrituraIni;
    }

    public Date getBienFideUnidadFechaCto() {
        return bienFideUnidadFechaCto;
    }

    public void setBienFideUnidadFechaCto(Date bienFideUnidadFechaCto) {
        this.bienFideUnidadFechaCto = bienFideUnidadFechaCto;
    }

    public String getBienFideUnidadNotarioLista() {
        return bienFideUnidadNotarioLista;
    }

    public void setBienFideUnidadNotarioLista(String bienFideUnidadNotarioLista) {
        this.bienFideUnidadNotarioLista = bienFideUnidadNotarioLista;
    }

    public String getBienFideUnidadNotarioLocalidad() {
        return bienFideUnidadNotarioLocalidad;
    }

    public void setBienFideUnidadNotarioLocalidad(String bienFideUnidadNotarioLocalidad) {
        this.bienFideUnidadNotarioLocalidad = bienFideUnidadNotarioLocalidad;
    }

    public String getBienFideUnidadNotarioNom() {
        return bienFideUnidadNotarioNom;
    }

    public void setBienFideUnidadNotarioNom(String bienFideUnidadNotarioNom) {
        this.bienFideUnidadNotarioNom = bienFideUnidadNotarioNom;
    }

    public Integer getBienFideUnidadNotarioNum() {
        return bienFideUnidadNotarioNum;
    }

    public void setBienFideUnidadNotarioNum(Integer bienFideUnidadNotarioNum) {
        this.bienFideUnidadNotarioNum = bienFideUnidadNotarioNum;
    }

    public Double getBienFideUnidadValor() {
        return bienFideUnidadValor;
    }

    public void setBienFideUnidadValor(Double bienFideUnidadValor) {
        this.bienFideUnidadValor = bienFideUnidadValor;
    }

    public String getBienFideUnidadMonedaNom() {
        return bienFideUnidadMonedaNom;
    }

    public void setBienFideUnidadMonedaNom(String bienFideUnidadMonedaNom) {
        this.bienFideUnidadMonedaNom = bienFideUnidadMonedaNom;
    }

    public Integer getBienFideUnidadMonedaNum() {
        return bienFideUnidadMonedaNum;
    }

    public void setBienFideUnidadMonedaNum(Integer bienFideUnidadMonedaNum) {
        this.bienFideUnidadMonedaNum = bienFideUnidadMonedaNum;
    }

    public Double getBienFideUnidadSuperficie() {
        return bienFideUnidadSuperficie;
    }

    public void setBienFideUnidadSuperficie(Double bienFideUnidadSuperficie) {
        this.bienFideUnidadSuperficie = bienFideUnidadSuperficie;
    }

    public String getBienFideUnidadObservacion() {
        return bienFideUnidadObservacion;
    }

    public void setBienFideUnidadObservacion(String bienFideUnidadObservacion) {
        this.bienFideUnidadObservacion = bienFideUnidadObservacion;
    }

    public String getBienFideUnidadStatus() {
        return bienFideUnidadStatus;
    }

    public void setBienFideUnidadStatus(String bienFideUnidadStatus) {
        this.bienFideUnidadStatus = bienFideUnidadStatus;
    }

    public Double getBienFideUnidadAcumRegContable() {
        return bienFideUnidadAcumRegContable;
    }

    public void setBienFideUnidadAcumRegContable(Double bienFideUnidadAcumRegContable) {
        this.bienFideUnidadAcumRegContable = bienFideUnidadAcumRegContable;
    }
    
  //Constructor
    public ContabilidadBienFideUnidadBean() {
        
    }

    public Integer getBienFideUnidadNotarioNumOFicial() {
        return bienFideUnidadNotarioNumOFicial;
    }

    public void setBienFideUnidadNotarioNumOFicial(Integer bienFideUnidadNotarioNumOFicial) {
        this.bienFideUnidadNotarioNumOFicial = bienFideUnidadNotarioNumOFicial;
    }

    public String getTxtBienFideUnidadValor() {
        return txtBienFideUnidadValor;
    }

    public void setTxtBienFideUnidadValor(String txtBienFideUnidadValor) {
        this.txtBienFideUnidadValor = txtBienFideUnidadValor;
    }

    public String getTxtBienFideUnidadSuperficie() {
        return txtBienFideUnidadSuperficie;
    }

    public void setTxtBienFideUnidadSuperficie(String txtBienFideUnidadSuperficie) {
        this.txtBienFideUnidadSuperficie = txtBienFideUnidadSuperficie;
    }
}