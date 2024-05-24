/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author s6596367
 */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean(name = "CtasSaldos")
@ViewScoped

public class CtasSaldos implements Serializable{
private String                     ctaNumero;
private Double                     ctaSaldo;

    public String getCtaNumero() {
        return ctaNumero;
    }

    public void setCtaNumero(String ctaNumero) {
        this.ctaNumero = ctaNumero;
    }

    public Double getCtaSaldo() {
        return ctaSaldo;
    }

    public void setCtaSaldo(Double ctaSaldo) {
        this.ctaSaldo = ctaSaldo;
    }
}
