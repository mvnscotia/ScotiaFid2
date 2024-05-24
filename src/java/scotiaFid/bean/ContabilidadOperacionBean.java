/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadMOperacionBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210506
 * MODIFICADO  : 20210506
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadOperacion")
@ViewScoped
public class ContabilidadOperacionBean implements Serializable{
  //Atributos privados
    private String                     operacionId;
    private String                     operacionNombre;
    private String                     operacionTarifa;
    private Date                       operacionFechaReg;
    private Date                       operacionFechaMod;
    private String                     operacionStatus;
    private Integer                    operacionBitUsuarioNum;
    private String                     operacionBitTipoOperacion;
    
  //Getters y Setters
    public String getOperacionId() {
        return operacionId;
    }

    public void setOperacionId(String operacionId) {
        this.operacionId = operacionId;
    }

    public String getOperacionNombre() {
        return operacionNombre;
    }

    public void setOperacionNombre(String operacionNombre) {
        this.operacionNombre = operacionNombre;
    }

    public String getOperacionTarifa() {
        return operacionTarifa;
    }

    public void setOperacionTarifa(String operacionTarifa) {
        this.operacionTarifa = operacionTarifa;
    }

    public Date getOperacionFechaReg() {
        return operacionFechaReg;
    }

    public void setOperacionFechaReg(Date operacionFechaReg) {
        this.operacionFechaReg = operacionFechaReg;
    }

    public Date getOperacionFechaMod() {
        return operacionFechaMod;
    }

    public void setOperacionFechaMod(Date operacionFechaMod) {
        this.operacionFechaMod = operacionFechaMod;
    }

    public String getOperacionStatus() {
        return operacionStatus;
    }

    public void setOperacionStatus(String operacionStatus) {
        this.operacionStatus = operacionStatus;
    }

    public Integer getOperacionBitUsuarioNum() {
        return operacionBitUsuarioNum;
    }

    public void setOperacionBitUsuarioNum(Integer operacionBitUsuarioNum) {
        this.operacionBitUsuarioNum = operacionBitUsuarioNum;
    }

    public String getOperacionBitTipoOperacion() {
        return operacionBitTipoOperacion;
    }

    public void setOperacionBitTipoOperacion(String operacionBitTipoOperacion) {
        this.operacionBitTipoOperacion = operacionBitTipoOperacion;
    }
    
  //Constructor
    public ContabilidadOperacionBean() {
        
    }
}
