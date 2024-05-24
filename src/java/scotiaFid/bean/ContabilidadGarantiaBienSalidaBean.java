/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadGarantiaBienSalidaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210927
 * MODIFICADO  : 20210927
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadGarantiaBienSalida")
@ViewScoped
public class ContabilidadGarantiaBienSalidaBean implements Serializable{
  //Atributos privados  
    private Double                     garantiaBienSalidaImporteNvo;
    private Double                     garantiaBienSalidaImporteAnt;
    private Date                       garantiaBienSalidaFechaValNva;
    private Date                       garantiaBienSalidaFechaValAnt;
    
    private String                     txtGarantiaBienSalidaImporteNvo;
  //Getters y Setters
    public Double getGarantiaBienSalidaImporteNvo() {
        return garantiaBienSalidaImporteNvo;
    }

    public void setGarantiaBienSalidaImporteNvo(Double garantiaBienSalidaImporteNvo) {
        this.garantiaBienSalidaImporteNvo = garantiaBienSalidaImporteNvo;
    }

    public Double getGarantiaBienSalidaImporteAnt() {
        return garantiaBienSalidaImporteAnt;
    }

    public void setGarantiaBienSalidaImporteAnt(Double garantiaBienSalidaImporteAnt) {
        this.garantiaBienSalidaImporteAnt = garantiaBienSalidaImporteAnt;
    }

    public Date getGarantiaBienSalidaFechaValNva() {
        return garantiaBienSalidaFechaValNva;
    }

    public void setGarantiaBienSalidaFechaValNva(Date garantiaBienSalidaFechaValNva) {
        this.garantiaBienSalidaFechaValNva = garantiaBienSalidaFechaValNva;
    }

    public Date getGarantiaBienSalidaFechaValAnt() {
        return garantiaBienSalidaFechaValAnt;
    }

    public void setGarantiaBienSalidaFechaValAnt(Date garantiaBienSalidaFechaValAnt) {
        this.garantiaBienSalidaFechaValAnt = garantiaBienSalidaFechaValAnt;
    }
    
  //Constructor
    public ContabilidadGarantiaBienSalidaBean() {
        
    }

    public String getTxtGarantiaBienSalidaImporteNvo() {
        return txtGarantiaBienSalidaImporteNvo;
    }

    public void setTxtGarantiaBienSalidaImporteNvo(String txtGarantiaBienSalidaImporteNvo) {
        this.txtGarantiaBienSalidaImporteNvo = txtGarantiaBienSalidaImporteNvo;
    }
}