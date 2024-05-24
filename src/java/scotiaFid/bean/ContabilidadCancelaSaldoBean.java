/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideUnidadLiqBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210601
 * MODIFICADO  : 20210601
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadCancelaSaldo")
@ViewScoped
public class ContabilidadCancelaSaldoBean {
  //Atributos privados
    private Long                       csContratoNum;
    private String                     csOpcion;
    private String                     csOperacion;
    private Date                       csFecha;
    private Integer                    csBitUsuario;
    private String                     csBitPantalla;
    private String                     csBitTerminal;
    
  //Getters y Setters
    public Long getCsContratoNum() {
        return csContratoNum;
    }

    public void setCsContratoNum(Long csContratoNum) {
        this.csContratoNum = csContratoNum;
    }

    public String getCsOpcion() {
        return csOpcion;
    }

    public void setCsOpcion(String csOpcion) {
        this.csOpcion = csOpcion;
    }

    public String getCsOperacion() {
        return csOperacion;
    }

    public void setCsOperacion(String csOperacion) {
        this.csOperacion = csOperacion;
    }

    public Date getCsFecha() {
        return csFecha;
    }

    public void setCsFecha(Date csFecha) {
        this.csFecha = csFecha;
    }

    public Integer getCsBitUsuario() {
        return csBitUsuario;
    }

    public void setCsBitUsuario(Integer csBitUsuario) {
        this.csBitUsuario = csBitUsuario;
    }

    public String getCsBitPantalla() {
        return csBitPantalla;
    }

    public void setCsBitPantalla(String csBitPantalla) {
        this.csBitPantalla = csBitPantalla;
    }

    public String getCsBitTerminal() {
        return csBitTerminal;
    }

    public void setCsBitTerminal(String csBitTerminal) {
        this.csBitTerminal = csBitTerminal;
    }
    
  //Constructor
    public ContabilidadCancelaSaldoBean() {
        
    }    
}