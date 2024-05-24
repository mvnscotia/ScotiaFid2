/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Soctiabank
 * ARCHIVO     : PantallaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210712
 * MODIFICADO  : 20210712
 * AUTOR       : j.delatorre
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanPantalla")
@ViewScoped
public class SeguridadPantallaBean implements Serializable{
  //Atributos privados
    private String                     pantallaId;
    private String                     pantallaTitulo;
    
  //Getters y Setters
    public String getPantallaId() {
        return pantallaId;
    }

    public void setPantallaId(String pantallaId) {
        this.pantallaId = pantallaId;
    }

    public String getPantallaTitulo() {
        return pantallaTitulo;
    }

    public void setPantallaTitulo(String pantallaTitulo) {
        this.pantallaTitulo = pantallaTitulo;
    }
    
  //Constructor
    public SeguridadPantallaBean() {
        
    }
  //Constructor con parametros
    public SeguridadPantallaBean(String pantallaId, String pantallaTitulo) {
        this.pantallaId     = pantallaId;
        this.pantallaTitulo = pantallaTitulo;
    }
}