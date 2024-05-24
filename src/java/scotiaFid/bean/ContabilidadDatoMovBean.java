/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabanck
 * ARCHIVO     : ContabilidadDAtoMovBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210630
 * MODIFICADO  : 20210630
 * AUTOR       : j.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadDatoMov")
@ViewScoped
public class ContabilidadDatoMovBean implements Serializable{
  //Atributos privados
    private Long                       datoMovFolio;
    private Short                      datoMovSec;
    private String                     datoMovConcepto;
    private String                     datoMovValor;
    
  //Getters y Setters
    public Long getDatoMovFolio() {
        return datoMovFolio;
    }

    public void setDatoMovFolio(Long datoMovFolio) {
        this.datoMovFolio = datoMovFolio;
    }

    public Short getDatoMovSec() {
        return datoMovSec;
    }

    public void setDatoMovSec(Short datoMovSec) {
        this.datoMovSec = datoMovSec;
    }

    public String getDatoMovConcepto() {
        return datoMovConcepto;
    }

    public void setDatoMovConcepto(String datoMovConcepto) {
        this.datoMovConcepto = datoMovConcepto;
    }

    public String getDatoMovValor() {
        return datoMovValor;
    }

    public void setDatoMovValor(String datoMovValor) {
        this.datoMovValor = datoMovValor;
    }
    
  //Constructor
    public ContabilidadDatoMovBean() {
        
    }
}