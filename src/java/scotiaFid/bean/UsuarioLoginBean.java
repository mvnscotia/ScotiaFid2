/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : UsuarioLoginBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210306
 * MODIFICADO  : 20210719
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210713.- Se agrega el atributo usuarioLoginPlaza
 *               20210713.- Se agrega el atributo usuarioLoginSucursal
 *               20210716.- Se agrega el atributo usuarioLoginIP 
 *               20210719.- Se agrega el atributo usuarioLoginHoraMax;
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanUsuarioLogin")
@ViewScoped
public class UsuarioLoginBean implements Serializable{
  //Atributos privados
    private String                               usuarioLoginScotiaId;
    private String                               usuarioLoginNombre;
    private String                               usuarioLoginFirmado;
    private Integer                              usuarioLoginNumero;
    private String                               usuarioLoginToken;
    private String                               usuarioLoginRol;
    private String                               usuarioLoginRolId;
    private String                               usuarioLoginPlaza;
    private String                               usuarioLoginSucursal;
    private String                               usuarioLoginIP;
    private String                               usuarioLoginHoraMax;
    
  //Getters y Setters
    public String getUsuarioLoginScotiaId() {
        return usuarioLoginScotiaId;
    }

    public void setUsuarioLoginScotiaId(String usuarioLoginScotiaId) {
        this.usuarioLoginScotiaId = usuarioLoginScotiaId;
    }

    public String getUsuarioLoginNombre() {
        return usuarioLoginNombre;
    }

    public void setUsuarioLoginNombre(String usuarioLoginNombre) {
        this.usuarioLoginNombre = usuarioLoginNombre;
    }

    public String getUsuarioLoginFirmado() {
        return usuarioLoginFirmado;
    }

    public void setUsuarioLoginFirmado(String usuarioLoginFirmado) {
        this.usuarioLoginFirmado = usuarioLoginFirmado;
    }

    public Integer getUsuarioLoginNumero() {
        return usuarioLoginNumero;
    }

    public void setUsuarioLoginNumero(Integer usuarioLoginNumero) {
        this.usuarioLoginNumero = usuarioLoginNumero;
    }

    public String getUsuarioLoginToken() {
        return usuarioLoginToken;
    }

    public void setUsuarioLoginToken(String usuarioLoginToken) {
        this.usuarioLoginToken = usuarioLoginToken;
    }

    public String getUsuarioLoginRol() {
        return usuarioLoginRol;
    }

    public void setUsuarioLoginRol(String usuarioLoginRol) {
        this.usuarioLoginRol = usuarioLoginRol;
    }

    public String getUsuarioLoginRolId() {
        return usuarioLoginRolId;
    }

    public void setUsuarioLoginRolId(String usuarioLoginRolId) {
        this.usuarioLoginRolId = usuarioLoginRolId;
    }

    public String getUsuarioLoginPlaza() {
        return usuarioLoginPlaza;
    }

    public void setUsuarioLoginPlaza(String usuarioLoginPlaza) {
        this.usuarioLoginPlaza = usuarioLoginPlaza;
    }

    public String getUsuarioLoginSucursal() {
        return usuarioLoginSucursal;
    }

    public void setUsuarioLoginSucursal(String usuarioLoginSucursal) {
        this.usuarioLoginSucursal = usuarioLoginSucursal;
    }

    public String getUsuarioLoginIP() {
        return usuarioLoginIP;
    }

    public void setUsuarioLoginIP(String usuarioLoginIP) {
        this.usuarioLoginIP = usuarioLoginIP;
    }

    public String getUsuarioLoginHoraMax() {
        return usuarioLoginHoraMax;
    }

    public void setUsuarioLoginHoraMax(String usuarioLoginHoraMax) {
        this.usuarioLoginHoraMax = usuarioLoginHoraMax;
    }
    
  //Constructor
    public UsuarioLoginBean() {
        
    }
}