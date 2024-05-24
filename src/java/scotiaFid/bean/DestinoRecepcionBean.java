/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lenovo
 */

@ManagedBean(name = "destinoRecepcion")
@ViewScoped
public class DestinoRecepcionBean implements Serializable{
    private Integer claveNumero;
    private String claveDescripcion;

    /**
     * @return the claveNumero
     */
    public Integer getClaveNumero() {
        return claveNumero;
    }

    /**
     * @param claveNumero the claveNumero to set
     */
    public void setClaveNumero(Integer claveNumero) {
        this.claveNumero = claveNumero;
    }

    /**
     * @return the claveDescripcion
     */
    public String getClaveDescripcion() {
        return claveDescripcion;
    }

    /**
     * @param claveDescripcion the claveDescripcion to set
     */
    public void setClaveDescripcion(String claveDescripcion) {
        this.claveDescripcion = claveDescripcion;
    }

    


}
