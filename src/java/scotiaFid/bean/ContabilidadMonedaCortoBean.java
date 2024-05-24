/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean(name = "beanContabilidadMonedaCorto")
@ViewScoped

/**
 *
 * @author lenovo
 */
public class ContabilidadMonedaCortoBean implements Serializable {
    private Short                         monedaCortoID;
    private String                          monedaCortoNombre;

    public Short getMonedaCortoID() {
        return monedaCortoID;
    }

    public void setMonedaCortoID(Short monedaCortoID) {
        this.monedaCortoID = monedaCortoID;
    }

    public String getMonedaCortoNombre() {
        return monedaCortoNombre;
    }

    public void setMonedaCortoNombre(String monedaCortoNombre) {
        this.monedaCortoNombre = monedaCortoNombre;
    }
 
}
