/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : AdminControlesBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210611
 * MODIFICADO  : 20210611
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanAdminControles")
@ViewScoped
public class AdminControlesBean implements Serializable{
  //Atributos privados
    private String                     controlTipo;
    private Short                      controlSec;
    private String                     controlNombre;
    private String                     controlTexto;
    private Date                       controlFechaReg;
    private Date                       controlFechaMod;
    private String                     controlStatus;
    private String                     controlOperacionTipo;
    
  //Getters y Setters
    public String getControlTipo() {
        return controlTipo;
    }

    public void setControlTipo(String controlTipo) {
        this.controlTipo = controlTipo;
    }

    public Short getControlSec() {
        return controlSec;
    }

    public void setControlSec(Short controlSec) {
        this.controlSec = controlSec;
    }

    public String getControlNombre() {
        return controlNombre;
    }

    public void setControlNombre(String controlNombre) {
        this.controlNombre = controlNombre;
    }

    public String getControlTexto() {
        return controlTexto;
    }

    public void setControlTexto(String controlTexto) {
        this.controlTexto = controlTexto;
    }

    public Date getControlFechaReg() {
        return controlFechaReg;
    }

    public void setControlFechaReg(Date controlFechaReg) {
        this.controlFechaReg = controlFechaReg;
    }

    public Date getControlFechaMod() {
        return controlFechaMod;
    }

    public void setControlFechaMod(Date controlFechaMod) {
        this.controlFechaMod = controlFechaMod;
    }

    public String getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getControlOperacionTipo() {
        return controlOperacionTipo;
    }

    public void setControlOperacionTipo(String controlOperacionTipo) {
        this.controlOperacionTipo = controlOperacionTipo;
    }
    
  //Constructor
    public AdminControlesBean() {
        
    }
}