/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : AdminContratoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210629
 * MODIFICADO  : 20210629
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanAdminContrato")
@ViewScoped
public class AdminContratoBean implements Serializable{
  //Atributos privados
    private Long                       contratoNumero;
    private String                     contratoNombre;
    private String                     contratoTipoNegocio;
    private String                     contratoTipoProd;
    private Date                       contratoFechaApertura;
    private Date                       contratoFechaExtingue;
    private String                     contratoStatus;
    
  //Getters y Setters
    public Long getContratoNumero() {
        return contratoNumero;
    }

    public void setContratoNumero(Long contratoNumero) {
        this.contratoNumero = contratoNumero;
    }

    public String getContratoNombre() {
        return contratoNombre;
    }

    public void setContratoNombre(String contratoNombre) {
        this.contratoNombre = contratoNombre;
    }

    public String getContratoTipoNegocio() {
        return contratoTipoNegocio;
    }

    public void setContratoTipoNegocio(String contratoTipoNegocio) {
        this.contratoTipoNegocio = contratoTipoNegocio;
    }

    public String getContratoTipoProd() {
        return contratoTipoProd;
    }

    public void setContratoTipoProd(String contratoTipoProd) {
        this.contratoTipoProd = contratoTipoProd;
    }

    public Date getContratoFechaApertura() {
        return contratoFechaApertura;
    }

    public void setContratoFechaApertura(Date contratoFechaApertura) {
        this.contratoFechaApertura = contratoFechaApertura;
    }

    public Date getContratoFechaExtingue() {
        return contratoFechaExtingue;
    }

    public void setContratoFechaExtingue(Date contratoFechaExtingue) {
        this.contratoFechaExtingue = contratoFechaExtingue;
    }

    public String getContratoStatus() {
        return contratoStatus;
    }

    public void setContratoStatus(String contratoStatus) {
        this.contratoStatus = contratoStatus;
    }
    
  //Constructor
    public AdminContratoBean() {
        
    }
}