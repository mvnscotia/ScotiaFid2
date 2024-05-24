/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CriterioBusquedaClaveBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210708
 * MODIFICADO  : 20210708
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanCriterioBusquedaClave")
@ViewScoped
public class CriterioBusquedaClaveBean implements Serializable{
  //Atributos privados
    private Integer                    criterioCveNumero;
    private Integer                    criterioCveSec;
    private String                     criterioCveFormaEmp;
    private String                     criterioCveStatus;
    
  //Getters y Setters
    public Integer getCriterioCveNumero() {
        return criterioCveNumero;
    }

    public void setCriterioCveNumero(Integer criterioCveNumero) {
        this.criterioCveNumero = criterioCveNumero;
    }

    public Integer getCriterioCveSec() {
        return criterioCveSec;
    }

    public void setCriterioCveSec(Integer criterioCveSec) {
        this.criterioCveSec = criterioCveSec;
    }

    public String getCriterioCveFormaEmp() {
        return criterioCveFormaEmp;
    }

    public void setCriterioCveFormaEmp(String criterioCveFormaEmp) {
        this.criterioCveFormaEmp = criterioCveFormaEmp;
    }

    public String getCriterioCveStatus() {
        return criterioCveStatus;
    }

    public void setCriterioCveStatus(String criterioCveStatus) {
        this.criterioCveStatus = criterioCveStatus;
    }
    
  //Constructor
    public CriterioBusquedaClaveBean() {
        
    }
}