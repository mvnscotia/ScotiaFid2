/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : UsuarioBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210306
 * MODIFICADO  : 20210830
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210712.- Se agrega atributo usuarioExiste
 *               20210830.- Se agrega atributo usuarioFiltroAtn
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanUsuario")
@ViewScoped
public class UsuarioBean implements Serializable{
    
  //Atributos privados
    private Integer                              usuarioId;
    private String                               usuarioNombre;
    private String                               usuarioTipo;
    private Integer                              usuarioTipoId;
    private String                               usuarioPlaza;
    private String                               usuarioPlazaEst;
    private String                               usuarioPuesto;
    private Integer                              usuarioPuestoId;
    private Integer                              usuarioNivel01;
    private Integer                              usuarioNivel02;
    private Integer                              usuarioNivel03;
    private Integer                              usuarioNivel04;
    private Integer                              usuarioNivel05;
    private Integer                              usuarioNivelFirma;
    private Date                                 usuarioFechaReg;
    private Date                                 usuarioFechaMod;
    private Boolean                              usuarioExiste;
    private String                               usuarioStatus; 
    private List<String>                         usuarioFiltroAtn;
    
  //Getters y Setters
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioTipo() {
        return usuarioTipo;
    }

    public void setUsuarioTipo(String usuarioTipo) {
        this.usuarioTipo = usuarioTipo;
    }

    public Integer getUsuarioTipoId() {
        return usuarioTipoId;
    }

    public void setUsuarioTipoId(Integer usuarioTipoId) {
        this.usuarioTipoId = usuarioTipoId;
    }

    public String getUsuarioPlaza() {
        return usuarioPlaza;
    }

    public void setUsuarioPlaza(String usuarioPlaza) {
        this.usuarioPlaza = usuarioPlaza;
    }

    public String getUsuarioPlazaEst() {
        return usuarioPlazaEst;
    }

    public void setUsuarioPlazaEst(String usuarioPlazaEst) {
        this.usuarioPlazaEst = usuarioPlazaEst;
    }

    public String getUsuarioPuesto() {
        return usuarioPuesto;
    }

    public void setUsuarioPuesto(String usuarioPuesto) {
        this.usuarioPuesto = usuarioPuesto;
    }

    public Integer getUsuarioPuestoId() {
        return usuarioPuestoId;
    }

    public void setUsuarioPuestoId(Integer usuarioPuestoId) {
        this.usuarioPuestoId = usuarioPuestoId;
    }

    public Integer getUsuarioNivel01() {
        return usuarioNivel01;
    }

    public void setUsuarioNivel01(Integer usuarioNivel01) {
        this.usuarioNivel01 = usuarioNivel01;
    }

    public Integer getUsuarioNivel02() {
        return usuarioNivel02;
    }

    public void setUsuarioNivel02(Integer usuarioNivel02) {
        this.usuarioNivel02 = usuarioNivel02;
    }

    public Integer getUsuarioNivel03() {
        return usuarioNivel03;
    }

    public void setUsuarioNivel03(Integer usuarioNivel03) {
        this.usuarioNivel03 = usuarioNivel03;
    }

    public Integer getUsuarioNivel04() {
        return usuarioNivel04;
    }

    public void setUsuarioNivel04(Integer usuarioNivel04) {
        this.usuarioNivel04 = usuarioNivel04;
    }

    public Integer getUsuarioNivel05() {
        return usuarioNivel05;
    }

    public void setUsuarioNivel05(Integer usuarioNivel05) {
        this.usuarioNivel05 = usuarioNivel05;
    }

    public Integer getUsuarioNivelFirma() {
        return usuarioNivelFirma;
    }

    public void setUsuarioNivelFirma(Integer usuarioNivelFirma) {
        this.usuarioNivelFirma = usuarioNivelFirma;
    }

    public Date getUsuarioFechaReg() {
        return usuarioFechaReg;
    }

    public void setUsuarioFechaReg(Date usuarioFechaReg) {
        this.usuarioFechaReg = usuarioFechaReg;
    }

    public Date getUsuarioFechaMod() {
        return usuarioFechaMod;
    }

    public void setUsuarioFechaMod(Date usuarioFechaMod) {
        this.usuarioFechaMod = usuarioFechaMod;
    }

    public Boolean getUsuarioExiste() {
        return usuarioExiste;
    }

    public void setUsuarioExiste(Boolean usuarioExiste) {
        this.usuarioExiste = usuarioExiste;
    }

    public String getUsuarioStatus() {
        return usuarioStatus;
    }

    public void setUsuarioStatus(String usuarioStatus) {
        this.usuarioStatus = usuarioStatus;
    }

    public List<String> getUsuarioFiltroAtn() {
        return usuarioFiltroAtn;
    }

    public void setUsuarioFiltroAtn(List<String> usuarioFiltroAtn) {
        this.usuarioFiltroAtn = usuarioFiltroAtn;
    }
    
  //Constructor
    public UsuarioBean() {
        
    }
}