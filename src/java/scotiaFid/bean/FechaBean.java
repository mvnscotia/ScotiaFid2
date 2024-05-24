/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : FechaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210307
 * MODIFICADO  : 20210607
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanFecha")
@ViewScoped
public class FechaBean implements Serializable{
  //Atributos privados
    private Boolean                              fechaCveExisteControla;
    private Integer                              fechaActAño;
    private Integer                              fechaActMes;
    private Integer                              fechaActDia;
    private Integer                              fechaMSAAño;
    private Integer                              fechaMSAMes;
    private Integer                              fechaMSADia;
    private Integer                              fechaMSAAbierto;
    private Date                                 fechaAct;
    private Date                                 fechaActFin;
    private Date                                 fechaActIni;
    private Date                                 fechaMSA;
    private Integer                              fechaHSTMes;
    private Integer                              fechaHSTAño;
    
  //Getters y Setters  
    public Boolean getFechaCveExisteControla() {
        return fechaCveExisteControla;
    }

    public void setFechaCveExisteControla(Boolean fechaCveExisteControla) {
        this.fechaCveExisteControla = fechaCveExisteControla;
    }

    public Integer getFechaActAño() {
        return fechaActAño;
    }

    public void setFechaActAño(Integer fechaActAño) {
        this.fechaActAño = fechaActAño;
    }

    public Integer getFechaActMes() {
        return fechaActMes;
    }

    public void setFechaActMes(Integer fechaActMes) {
        this.fechaActMes = fechaActMes;
    }

    public Integer getFechaActDia() {
        return fechaActDia;
    }

    public void setFechaActDia(Integer fechaActDia) {
        this.fechaActDia = fechaActDia;
    }

    public Integer getFechaMSAAño() {
        return fechaMSAAño;
    }

    public void setFechaMSAAño(Integer fechaMSAAño) {
        this.fechaMSAAño = fechaMSAAño;
    }

    public Integer getFechaMSAMes() {
        return fechaMSAMes;
    }

    public void setFechaMSAMes(Integer fechaMSAMes) {
        this.fechaMSAMes = fechaMSAMes;
    }

    public Integer getFechaMSADia() {
        return fechaMSADia;
    }

    public void setFechaMSADia(Integer fechaMSADia) {
        this.fechaMSADia = fechaMSADia;
    }

    public Integer getFechaMSAAbierto() {
        return fechaMSAAbierto;
    }

    public void setFechaMSAAbierto(Integer fechaMSAAbierto) {
        this.fechaMSAAbierto = fechaMSAAbierto;
    }

    public Date getFechaAct() {
        return fechaAct;
    }

    public void setFechaAct(Date fechaAct) {
        this.fechaAct = fechaAct;
    }

    public Date getFechaActFin() {
        return fechaActFin;
    }

    public void setFechaActFin(Date fechaActFin) {
        this.fechaActFin = fechaActFin;
    }

    public Date getFechaActIni() {
        return fechaActIni;
    }

    public void setFechaActIni(Date fechaActIni) {
        this.fechaActIni = fechaActIni;
    }

    public Date getFechaMSA() {
        return fechaMSA;
    }

    public void setFechaMSA(Date fechaMSA) {
        this.fechaMSA = fechaMSA;
    }

    public Integer getFechaHSTMes() {
        return fechaHSTMes;
    }

    public void setFechaHSTMes(Integer fechaHSTMes) {
        this.fechaHSTMes = fechaHSTMes;
    }

    public Integer getFechaHSTAño() {
        return fechaHSTAño;
    }

    public void setFechaHSTAño(Integer fechaHSTAño) {
        this.fechaHSTAño = fechaHSTAño;
    }
    
  //Constructor
    public FechaBean() {
        
    }
}