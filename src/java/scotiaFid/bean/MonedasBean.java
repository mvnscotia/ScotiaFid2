/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "monedas")
@ViewScoped
public class MonedasBean {

    private Integer monedaNumero;
    private String monedaNombre;

    /**
     * @return the monedaNumero
     */
    public Integer getMonedaNumero() {
        return monedaNumero;
    }

    /**
     * @param monedaNumero the monedaNumero to set
     */
    public void setMonedaNumero(Integer monedaNumero) {
        this.monedaNumero = monedaNumero;
    }

    /**
     * @return the monedaNombre
     */
    public String getMonedaNombre() {
        return monedaNombre;
    }

    /**
     * @param monedaNombre the monedaNombre to set
     */
    public void setMonedaNombre(String monedaNombre) {
        this.monedaNombre = monedaNombre;
    }

}
