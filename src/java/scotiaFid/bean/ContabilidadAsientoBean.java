/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadAsientoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210506
 * MODIFICADO  : 20210831
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadAsiento")
@ViewScoped
public class ContabilidadAsientoBean implements Serializable{
  //Atributos privados
    private Long                       asientoFolio;
    private String                     asientoOperacion;
    private String                     asientoTransaccion;
    private Integer                    asientoSecuencial;
    private Short                      asientoCTAM;
    private Short                      asiento1SCTA;
    private Short                      asiento2SCTA;
    private Short                      asiento3SCTA;
    private Short                      asiento4SCTA;
    private Short                      asientoMdaId;
    private String                     asientoMdaNomLrg;
    private String                     asientoMdaNomCrt;
    private String                     asientoCuentaNombre;
    private Long                       asientoAX1;
    private Long                       asientoAX2;
    private Long                       asientoAX3;
    private String                     asientoCveCarAbo;
    private Double                     asientoImporte;
    private String                     asientoDescripcion;
    private Date                       asientoFecReg;
    private Date                       asientoFecUltMod;
    private Date                       asientoFecMov;
    private String                     asientoStatus;
    private Integer                    asientoUsuCancelo;
    private Date                       asientoFecAlta;
    
  //Getters y Setters
    public Long getAsientoFolio() {
        return asientoFolio;
    }

    public void setAsientoFolio(Long asientoFolio) {
        this.asientoFolio = asientoFolio;
    }

    public String getAsientoOperacion() {
        return asientoOperacion;
    }

    public void setAsientoOperacion(String asientoOperacion) {
        this.asientoOperacion = asientoOperacion;
    }

    public String getAsientoTransaccion() {
        return asientoTransaccion;
    }

    public void setAsientoTransaccion(String asientoTransaccion) {
        this.asientoTransaccion = asientoTransaccion;
    }

    public Integer getAsientoSecuencial() {
        return asientoSecuencial;
    }

    public void setAsientoSecuencial(Integer asientoSecuencial) {
        this.asientoSecuencial = asientoSecuencial;
    }

    public Short getAsientoCTAM() {
        return asientoCTAM;
    }

    public void setAsientoCTAM(Short asientoCTAM) {
        this.asientoCTAM = asientoCTAM;
    }

    public Short getAsiento1SCTA() {
        return asiento1SCTA;
    }

    public void setAsiento1SCTA(Short asiento1SCTA) {
        this.asiento1SCTA = asiento1SCTA;
    }

    public Short getAsiento2SCTA() {
        return asiento2SCTA;
    }

    public void setAsiento2SCTA(Short asiento2SCTA) {
        this.asiento2SCTA = asiento2SCTA;
    }

    public Short getAsiento3SCTA() {
        return asiento3SCTA;
    }

    public void setAsiento3SCTA(Short asiento3SCTA) {
        this.asiento3SCTA = asiento3SCTA;
    }

    public Short getAsiento4SCTA() {
        return asiento4SCTA;
    }

    public void setAsiento4SCTA(Short asiento4SCTA) {
        this.asiento4SCTA = asiento4SCTA;
    }

    public Short getAsientoMdaId() {
        return asientoMdaId;
    }

    public void setAsientoMdaId(Short asientoMdaId) {
        this.asientoMdaId = asientoMdaId;
    }

    public String getAsientoMdaNomLrg() {
        return asientoMdaNomLrg;
    }

    public void setAsientoMdaNomLrg(String asientoMdaNomLrg) {
        this.asientoMdaNomLrg = asientoMdaNomLrg;
    }

    public String getAsientoMdaNomCrt() {
        return asientoMdaNomCrt;
    }

    public void setAsientoMdaNomCrt(String asientoMdaNomCrt) {
        this.asientoMdaNomCrt = asientoMdaNomCrt;
    }

    public String getAsientoCuentaNombre() {
        return asientoCuentaNombre;
    }

    public void setAsientoCuentaNombre(String asientoCuentaNombre) {
        this.asientoCuentaNombre = asientoCuentaNombre;
    }

    public Long getAsientoAX1() {
        return asientoAX1;
    }

    public void setAsientoAX1(Long asientoAX1) {
        this.asientoAX1 = asientoAX1;
    }

    public Long getAsientoAX2() {
        return asientoAX2;
    }

    public void setAsientoAX2(Long asientoAX2) {
        this.asientoAX2 = asientoAX2;
    }

    public Long getAsientoAX3() {
        return asientoAX3;
    }

    public void setAsientoAX3(Long asientoAX3) {
        this.asientoAX3 = asientoAX3;
    }

    public String getAsientoCveCarAbo() {
        return asientoCveCarAbo;
    }

    public void setAsientoCveCarAbo(String asientoCveCarAbo) {
        this.asientoCveCarAbo = asientoCveCarAbo;
    }

    public Double getAsientoImporte() {
        return asientoImporte;
    }

    public void setAsientoImporte(Double asientoImporte) {
        this.asientoImporte = asientoImporte;
    }

    public String getAsientoDescripcion() {
        return asientoDescripcion;
    }

    public void setAsientoDescripcion(String asientoDescripcion) {
        this.asientoDescripcion = asientoDescripcion;
    }

    public Date getAsientoFecReg() {
        return asientoFecReg;
    }

    public void setAsientoFecReg(Date asientoFecReg) {
        this.asientoFecReg = asientoFecReg;
    }

    public Date getAsientoFecUltMod() {
        return asientoFecUltMod;
    }

    public void setAsientoFecUltMod(Date asientoFecUltMod) {
        this.asientoFecUltMod = asientoFecUltMod;
    }

    public Date getAsientoFecMov() {
        return asientoFecMov;
    }

    public void setAsientoFecMov(Date asientoFecMov) {
        this.asientoFecMov = asientoFecMov;
    }

    public String getAsientoStatus() {
        return asientoStatus;
    }

    public void setAsientoStatus(String asientoStatus) {
        this.asientoStatus = asientoStatus;
    }

    public Integer getAsientoUsuCancelo() {
        return asientoUsuCancelo;
    }

    public void setAsientoUsuCancelo(Integer asientoUsuCancelo) {
        this.asientoUsuCancelo = asientoUsuCancelo;
    }
    
  //Constructor
    public ContabilidadAsientoBean() {
        
    }

    public Date getAsientoFecAlta() {
        return asientoFecAlta;
    }

    public void setAsientoFecAlta(Date asientoFecAlta) {
        this.asientoFecAlta = asientoFecAlta;
    }
}
