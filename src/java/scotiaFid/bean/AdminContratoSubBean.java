/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : AdminContratoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20211108
 * MODIFICADO  : 20211108
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanAdminContratoSub")
@ViewScoped
public class AdminContratoSubBean extends AdminContratoBean{
  //Atributos privados
    private Integer                    contratoSub;
    private String                     contratoSubNombre;
    private String                     contratoSubPersonaTipo;
    private Short                      contratoSubPersonaSec;
    private Date                       contratoSubFechaReg;
    private Date                       contratoSubFechaMod;
    private String                     contratoSubStatus;
    
  //Getter y Setters
    public Integer getContratoSub() {
        return contratoSub;
    }

    public void setContratoSub(Integer contratoSub) {
        this.contratoSub = contratoSub;
    }

    public String getContratoSubNombre() {
        return contratoSubNombre;
    }

    public void setContratoSubNombre(String contratoSubNombre) {
        this.contratoSubNombre = contratoSubNombre;
    }

    public String getContratoSubPersonaTipo() {
        return contratoSubPersonaTipo;
    }

    public void setContratoSubPersonaTipo(String contratoSubPersonaTipo) {
        this.contratoSubPersonaTipo = contratoSubPersonaTipo;
    }

    public Short getContratoSubPersonaSec() {
        return contratoSubPersonaSec;
    }

    public void setContratoSubPersonaSec(Short contratoSubPersonaSec) {
        this.contratoSubPersonaSec = contratoSubPersonaSec;
    }

    public Date getContratoSubFechaReg() {
        return contratoSubFechaReg;
    }

    public void setContratoSubFechaReg(Date contratoSubFechaReg) {
        this.contratoSubFechaReg = contratoSubFechaReg;
    }

    public Date getContratoSubFechaMod() {
        return contratoSubFechaMod;
    }

    public void setContratoSubFechaMod(Date contratoSubFechaMod) {
        this.contratoSubFechaMod = contratoSubFechaMod;
    }

    public String getContratoSubStatus() {
        return contratoSubStatus;
    }

    public void setContratoSubStatus(String contratoSubStatus) {
        this.contratoSubStatus = contratoSubStatus;
    }
    
  //Constructor
    public AdminContratoSubBean() {
        
    }
}