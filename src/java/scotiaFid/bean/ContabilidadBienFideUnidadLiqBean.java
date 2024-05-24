/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideUnidadLiqBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210514
 * MODIFICADO  : 20210514
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadBienFideUnidadLiq")
@ViewScoped
public class ContabilidadBienFideUnidadLiqBean extends ContabilidadBienFideUnidadBean{
  //Atributos privados
    private Integer                    bienFideUnidadLiqSec;
    private String                     bienFideUnidadLiqDeptoNum;
    private Date                       bienFideUnidadLiqFechaSol;
    private Date                       bienFideUnidadLiqFechaInstr;
    private Date                       bienFideUnidadLiqFechaFirma;
    private Date                       bienFideUnidadLiqFechaTrasDom;
    private String                     bienFideUnidadLiqEscritura;
    private Integer                    bienFideUnidadLiqNotarioNum;
    private String                     bienFideUnidadLiqNotarioLoc;
    private String                     bienFideUnidadLiqNotarioNom;
    private String                     bienFideUnidadLiqAquiere;
    private Double                     bienFideUnidadLiqTC;
    private String                     bienFideUnidadLiqStatus;
    
  //Getters y Setters
    public Integer getBienFideUnidadLiqSec() {
        return bienFideUnidadLiqSec;
    }

    public void setBienFideUnidadLiqSec(Integer bienFideUnidadLiqSec) {
        this.bienFideUnidadLiqSec = bienFideUnidadLiqSec;
    }

    public String getBienFideUnidadLiqDeptoNum() {
        return bienFideUnidadLiqDeptoNum;
    }

    public void setBienFideUnidadLiqDeptoNum(String bienFideUnidadLiqDeptoNum) {
        this.bienFideUnidadLiqDeptoNum = bienFideUnidadLiqDeptoNum;
    }

    public Date getBienFideUnidadLiqFechaSol() {
        return bienFideUnidadLiqFechaSol;
    }

    public void setBienFideUnidadLiqFechaSol(Date bienFideUnidadLiqFechaSol) {
        this.bienFideUnidadLiqFechaSol = bienFideUnidadLiqFechaSol;
    }

    public Date getBienFideUnidadLiqFechaInstr() {
        return bienFideUnidadLiqFechaInstr;
    }

    public void setBienFideUnidadLiqFechaInstr(Date bienFideUnidadLiqFechaInstr) {
        this.bienFideUnidadLiqFechaInstr = bienFideUnidadLiqFechaInstr;
    }

    public Date getBienFideUnidadLiqFechaFirma() {
        return bienFideUnidadLiqFechaFirma;
    }

    public void setBienFideUnidadLiqFechaFirma(Date bienFideUnidadLiqFechaFirma) {
        this.bienFideUnidadLiqFechaFirma = bienFideUnidadLiqFechaFirma;
    }

    public Date getBienFideUnidadLiqFechaTrasDom() {
        return bienFideUnidadLiqFechaTrasDom;
    }

    public void setBienFideUnidadLiqFechaTrasDom(Date bienFideUnidadLiqFechaTrasDom) {
        this.bienFideUnidadLiqFechaTrasDom = bienFideUnidadLiqFechaTrasDom;
    }

    public String getBienFideUnidadLiqEscritura() {
        return bienFideUnidadLiqEscritura;
    }

    public void setBienFideUnidadLiqEscritura(String bienFideUnidadLiqEscritura) {
        this.bienFideUnidadLiqEscritura = bienFideUnidadLiqEscritura;
    }

    public Integer getBienFideUnidadLiqNotarioNum() {
        return bienFideUnidadLiqNotarioNum;
    }

    public void setBienFideUnidadLiqNotarioNum(Integer bienFideUnidadLiqNotarioNum) {
        this.bienFideUnidadLiqNotarioNum = bienFideUnidadLiqNotarioNum;
    }

    public String getBienFideUnidadLiqNotarioLoc() {
        return bienFideUnidadLiqNotarioLoc;
    }

    public void setBienFideUnidadLiqNotarioLoc(String bienFideUnidadLiqNotarioLoc) {
        this.bienFideUnidadLiqNotarioLoc = bienFideUnidadLiqNotarioLoc;
    }

    public String getBienFideUnidadLiqNotarioNom() {
        return bienFideUnidadLiqNotarioNom;
    }

    public void setBienFideUnidadLiqNotarioNom(String bienFideUnidadLiqNotarioNom) {
        this.bienFideUnidadLiqNotarioNom = bienFideUnidadLiqNotarioNom;
    }

    public String getBienFideUnidadLiqAquiere() {
        return bienFideUnidadLiqAquiere;
    }

    public void setBienFideUnidadLiqAquiere(String bienFideUnidadLiqAquiere) {
        this.bienFideUnidadLiqAquiere = bienFideUnidadLiqAquiere;
    }

    public Double getBienFideUnidadLiqTC() {
        return bienFideUnidadLiqTC;
    }

    public void setBienFideUnidadLiqTC(Double bienFideUnidadLiqTC) {
        this.bienFideUnidadLiqTC = bienFideUnidadLiqTC;
    }

    public String getBienFideUnidadLiqStatus() {
        return bienFideUnidadLiqStatus;
    }

    public void setBienFideUnidadLiqStatus(String bienFideUnidadLiqStatus) {
        this.bienFideUnidadLiqStatus = bienFideUnidadLiqStatus;
    }
    
  //Constructor
    public ContabilidadBienFideUnidadLiqBean() {
        
    }
}