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
@ManagedBean(name = "nombreRecepcionCuentas")
@ViewScoped
public class NombreBean implements Serializable {

    private Integer beneficiarioNumero;
    private String beneficiarioNombre;

    /**
     * @return the beneficiarioNumero
     */
    public Integer getBeneficiarioNumero() {
        return beneficiarioNumero;
    }

    /**
     * @param beneficiarioNumero the beneficiarioNumero to set
     */
    public void setBeneficiarioNumero(Integer beneficiarioNumero) {
        this.beneficiarioNumero = beneficiarioNumero;
    }

    /**
     * @return the beneficiarioNombre
     */
    public String getBeneficiarioNombre() {
        return beneficiarioNombre;
    }

    /**
     * @param beneficiarioNombre the beneficiarioNombre to set
     */
    public void setBeneficiarioNombre(String beneficiarioNombre) {
        this.beneficiarioNombre = beneficiarioNombre;
    }

}
