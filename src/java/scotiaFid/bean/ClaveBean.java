/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ClaveBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210708
 * MODIFICADO  : 20210708
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanClave")
@ViewScoped

public class ClaveBean implements Serializable{
  //Atributos privados
    private Integer                    claveNumero;
    private Integer                    claveSec;
    private String                     claveDesc;
    private Double                     claveLimInf;
    private Double                     claveLimSup;
    private String                     claveFormaEmp;
    private Date                       claveFechaReg;
    private Date                       claveFechaMod;
    private String                     claveStatus;
    
  //Getters y Setters
    public Integer getClaveNumero() {
        return claveNumero;
    }

    public void setClaveNumero(Integer claveNumero) {
        this.claveNumero = claveNumero;
    }

    public Integer getClaveSec() {
        return claveSec;
    }

    public void setClaveSec(Integer claveSec) {
        this.claveSec = claveSec;
    }

    public String getClaveDesc() {
        return claveDesc;
    }

    public void setClaveDesc(String claveDesc) {
        this.claveDesc = claveDesc;
    }

    public Double getClaveLimInf() {
        return claveLimInf;
    }

    public void setClaveLimInf(Double claveLimInf) {
        this.claveLimInf = claveLimInf;
    }

    public Double getClaveLimSup() {
        return claveLimSup;
    }

    public void setClaveLimSup(Double claveLimSup) {
        this.claveLimSup = claveLimSup;
    }

    public String getClaveFormaEmp() {
        return claveFormaEmp;
    }

    public void setClaveFormaEmp(String claveFormaEmp) {
        this.claveFormaEmp = claveFormaEmp;
    }

    public Date getClaveFechaReg() {
        return claveFechaReg;
    }

    public void setClaveFechaReg(Date claveFechaReg) {
        this.claveFechaReg = claveFechaReg;
    }

    public Date getClaveFechaMod() {
        return claveFechaMod;
    }

    public void setClaveFechaMod(Date claveFechaMod) {
        this.claveFechaMod = claveFechaMod;
    }

    public String getClaveStatus() {
        return claveStatus;
    }

    public void setClaveStatus(String claveStatus) {
        this.claveStatus = claveStatus;
    }
    
  //Constructor
    public ClaveBean() {
        
    }

    public ClaveBean(Integer claveNumero, Integer claveSec, String claveDesc, Double claveLimInf, Double claveLimSup, String claveFormaEmp, Date claveFechaReg, Date claveFechaMod, String claveStatus) {
        this.claveNumero   = claveNumero;
        this.claveSec      = claveSec;
        this.claveDesc     = claveDesc;
        this.claveLimInf   = claveLimInf;
        this.claveLimSup   = claveLimSup;
        this.claveFormaEmp = claveFormaEmp;
        this.claveFechaReg = claveFechaReg;
        this.claveFechaMod = claveFechaMod;
        this.claveStatus   = claveStatus;
    }
}