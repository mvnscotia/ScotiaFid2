/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadTipoCambBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210531
 * MODIFICADO  : 20210531
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadTC")
@ViewScoped
public class ContabilidadTipoCambBean implements Serializable{
  //Atributos privados
    private Date                       tipoCambioFecha;
    private String                     tipoCambioMonedaNom;
    private Integer                    tipoCambioMonedaNum;
    private Double                     tipoCambioValor;
    private String                     tipoCambioStatus;
    private String                     tipoCambioTipoOperacion;
    
  //Getters y Setters
    public Date getTipoCambioFecha() {
        return tipoCambioFecha;
    }

    public void setTipoCambioFecha(Date tipoCambioFecha) {
        this.tipoCambioFecha = tipoCambioFecha;
    }

    public String getTipoCambioMonedaNom() {
        return tipoCambioMonedaNom;
    }

    public void setTipoCambioMonedaNom(String tipoCambioMonedaNom) {
        this.tipoCambioMonedaNom = tipoCambioMonedaNom;
    }

    public Integer getTipoCambioMonedaNum() {
        return tipoCambioMonedaNum;
    }

    public void setTipoCambioMonedaNum(Integer tipoCambioMonedaNum) {
        this.tipoCambioMonedaNum = tipoCambioMonedaNum;
    }

    public Double getTipoCambioValor() {
        return tipoCambioValor;
    }

    public void setTipoCambioValor(Double tipoCambioValor) {
        this.tipoCambioValor = tipoCambioValor;
    }

    public String getTipoCambioStatus() {
        return tipoCambioStatus;
    }

    public void setTipoCambioStatus(String tipoCambioStatus) {
        this.tipoCambioStatus = tipoCambioStatus;
    }

    public String getTipoCambioTipoOperacion() {
        return tipoCambioTipoOperacion;
    }

    public void setTipoCambioTipoOperacion(String tipoCambioTipoOperacion) {
        this.tipoCambioTipoOperacion = tipoCambioTipoOperacion;
    }
    
  //Constructor
    public ContabilidadTipoCambBean() {
        
    }
}