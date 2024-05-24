/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : MensajeConfirmaBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210204
 * MODIFICADO  : 20210204
 * AUTOR       : smartBuilder
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanMensajeConfirmacion")
@ViewScoped
public class MensajeConfirmaBean implements Serializable{
  //Atributos privados
    private String                     mensajeConfirmaUsuario;
    private String                     mensajeConfirmaMensaje1;
    private String                     mensajeConfirmaMensaje2;
    private String                     mensajeConfirmaMensaje3;
    private String                     mensajeConfirmacionAccion;
    private String                     mensajeConfirmaOrigen;
    
  //Getters y Setters
    public String getMensajeConfirmaUsuario() {
        return mensajeConfirmaUsuario;
    }

    public void setMensajeConfirmaUsuario(String mensajeConfirmaUsuario) {
        this.mensajeConfirmaUsuario = mensajeConfirmaUsuario;
    }

    public String getMensajeConfirmaMensaje1() {
        return mensajeConfirmaMensaje1;
    }

    public void setMensajeConfirmaMensaje1(String mensajeConfirmaMensaje1) {
        this.mensajeConfirmaMensaje1 = mensajeConfirmaMensaje1;
    }

    public String getMensajeConfirmaMensaje2() {
        return mensajeConfirmaMensaje2;
    }

    public void setMensajeConfirmaMensaje2(String mensajeConfirmaMensaje2) {
        this.mensajeConfirmaMensaje2 = mensajeConfirmaMensaje2;
    }

    public String getMensajeConfirmaMensaje3() {
        return mensajeConfirmaMensaje3;
    }

    public void setMensajeConfirmaMensaje3(String mensajeConfirmaMensaje3) {
        this.mensajeConfirmaMensaje3 = mensajeConfirmaMensaje3;
    }

    public String getMensajeConfirmacionAccion() {
        return mensajeConfirmacionAccion;
    }

    public void setMensajeConfirmacionAccion(String mensajeConfirmacionAccion) {
        this.mensajeConfirmacionAccion = mensajeConfirmacionAccion;
    }

    public String getMensajeConfirmaOrigen() {
        return mensajeConfirmaOrigen;
    }

    public void setMensajeConfirmaOrigen(String mensajeConfirmaOrigen) {
        this.mensajeConfirmaOrigen = mensajeConfirmaOrigen;
    }

  //Constructor
    public MensajeConfirmaBean() {
        
    }
}
