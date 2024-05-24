/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : SeguridadPantallaFidBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210827
 * MODIFICADO  : 20210827
 * AUTOR       : j.delatorre
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanSegPantallaFid")
@ViewScoped
public class SeguridadPantallaFidBean implements Serializable{
  //Atributos privados
    private String                     segPantallaFidId;
    private String                     segPantallaFidTitulo;
    private String                     segPantallaFidURL;
    private String                     segPantallaFidSSO;
    private String                     segPantallaFidMenu;
    private String                    segPantallaFidEscritura;
    
  //Getters y Setters
    public String getSegPantallaFidId() {
        return segPantallaFidId;
    }

    public void setSegPantallaFidId(String segPantallaFidId) {
        this.segPantallaFidId = segPantallaFidId;
    }

    public String getSegPantallaFidTitulo() {
        return segPantallaFidTitulo;
    }

    public void setSegPantallaFidTitulo(String segPantallaFidTitulo) {
        this.segPantallaFidTitulo = segPantallaFidTitulo;
    }

    public String getSegPantallaFidURL() {
        return segPantallaFidURL;
    }

    public void setSegPantallaFidURL(String segPantallaFidURL) {
        this.segPantallaFidURL = segPantallaFidURL;
    }

    public String getSegPantallaFidSSO() {
        return segPantallaFidSSO;
    }

    public void setSegPantallaFidSSO(String segPantallaFidSSO) {
        this.segPantallaFidSSO = segPantallaFidSSO;
    }

    public String getSegPantallaFidMenu() {
        return segPantallaFidMenu;
    }

    public void setSegPantallaFidMenu(String segPantallaFidMenu) {
        this.segPantallaFidMenu = segPantallaFidMenu;
    }

    public String getSegPantallaFidEscritura() {
        return segPantallaFidEscritura;
    }

    public void setSegPantallaFidEscritura(String segPantallaFidEscritura) {
        this.segPantallaFidEscritura = segPantallaFidEscritura;
    }

    

    
  //Constructor
    public SeguridadPantallaFidBean() {
        
    }
}