/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadMovtoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210402
 * MODIFICADO  : 20210402
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadMovto")
@ViewScoped
public class ContabilidadMovtoBean implements Serializable{
  //Atributos privados
    private Long                       movtoFolio;
    private String                     movtoOperacionId;
    private String                     movtoOperacionNom;
    private String                     movtoTransaccId;
    private Date                       movtoFecha;
    private Long                       movtoContratoNum;
    private String                     movtoContratoNom;
    private Integer                    movtoContratoNumSub;
    private Long                       movtoUsuarioId;
    private String                     movtoUsuarioNom;
    private String                     movtoDesc;
    private Double                     movtoImporte;
    private Date                       movtoFechaReg;
    private String                     movtoStatus;
    private Integer                    movtoCCUsuario;
    
  //Getters y Setters
    public Long getMovtoFolio() {
        return movtoFolio;
    }

    public void setMovtoFolio(Long movtoFolio) {
        this.movtoFolio = movtoFolio;
    }

    public String getMovtoOperacionId() {
        return movtoOperacionId;
    }

    public void setMovtoOperacionId(String movtoOperacionId) {
        this.movtoOperacionId = movtoOperacionId;
    }

    public String getMovtoOperacionNom() {
        return movtoOperacionNom;
    }

    public void setMovtoOperacionNom(String movtoOperacionNom) {
        this.movtoOperacionNom = movtoOperacionNom;
    }

    public String getMovtoTransaccId() {
        return movtoTransaccId;
    }

    public void setMovtoTransaccId(String movtoTransaccId) {
        this.movtoTransaccId = movtoTransaccId;
    }

    public Date getMovtoFecha() {
        return movtoFecha;
    }

    public void setMovtoFecha(Date movtoFecha) {
        this.movtoFecha = movtoFecha;
    }

    public Long getMovtoContratoNum() {
        return movtoContratoNum;
    }

    public void setMovtoContratoNum(Long movtoContratoNum) {
        this.movtoContratoNum = movtoContratoNum;
    }

    public String getMovtoContratoNom() {
        return movtoContratoNom;
    }

    public void setMovtoContratoNom(String movtoContratoNom) {
        this.movtoContratoNom = movtoContratoNom;
    }

    public Integer getMovtoContratoNumSub() {
        return movtoContratoNumSub;
    }

    public void setMovtoContratoNumSub(Integer movtoContratoNumSub) {
        this.movtoContratoNumSub = movtoContratoNumSub;
    }

    public Long getMovtoUsuarioId() {
        return movtoUsuarioId;
    }

    public void setMovtoUsuarioId(Long movtoUsuarioId) {
        this.movtoUsuarioId = movtoUsuarioId;
    }

    public String getMovtoUsuarioNom() {
        return movtoUsuarioNom;
    }

    public void setMovtoUsuarioNom(String movtoUsuarioNom) {
        this.movtoUsuarioNom = movtoUsuarioNom;
    }

    public String getMovtoDesc() {
        return movtoDesc;
    }

    public void setMovtoDesc(String movtoDesc) {
        this.movtoDesc = movtoDesc;
    }

    public Double getMovtoImporte() {
        return movtoImporte;
    }

    public void setMovtoImporte(Double movtoImporte) {
        this.movtoImporte = movtoImporte;
    }

    public Date getMovtoFechaReg() {
        return movtoFechaReg;
    }

    public void setMovtoFechaReg(Date movtoFechaReg) {
        this.movtoFechaReg = movtoFechaReg;
    }

    public String getMovtoStatus() {
        return movtoStatus;
    }

    public void setMovtoStatus(String movtoStatus) {
        this.movtoStatus = movtoStatus;
    }
  //Constructor
    public ContabilidadMovtoBean() {
        
    }

    public Integer getMovtoCCUsuario() {
        return movtoCCUsuario;
    }

    public void setMovtoCCUsuario(Integer movtoCCUsuario) {
        this.movtoCCUsuario = movtoCCUsuario;
    }
}