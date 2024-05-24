/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : MonetariaLiquidacionBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20220301
 * MODIFICADO  : 20220301
 * AUTOR       : EFP 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanMonetariaLiquidacion")
@ViewScoped
public class MonetariaLiquidacionBean implements Serializable{
    
    //Serial
    private static final long serialVersionUID = 1L;
    
  //Atributos privados Monetarias Liq
    private Long                       monetariasLiqInsNumContrato;
    private Integer                    monetariasLiqInsSubContrato;
    private Integer                    monetariasLiqInsNumFolioInst;
    private String                     monetariasLiqInsAdcCuenta;
    private Long                       monetariasLiqInsAdcSucursal;
    private String                     monetariasLiqInsCveDescClave;
    private Integer                    monetariasLiqInsAdcNumMoneda;
    private String                     monetariasLiqInsMonNomMoneda;
    


    /*Getters y Setters Monetarias Liquidacion ---------------------------------------------------------------*/
    //Getters and Setters
     public Long getMonetariasLiqInsNumContrato() {
        return monetariasLiqInsNumContrato;
    }
    public void setMonetariasLiqInsNumContrato(Long monetariasLiqInsNumContrato) {
        this.monetariasLiqInsNumContrato = monetariasLiqInsNumContrato;
    }
    public Integer getMonetariasLiqInsSubContrato() {
        return monetariasLiqInsSubContrato;
    }
    public void setMonetariasLiqInsSubContrato(Integer monetariasLiqInsSubContrato) {
        this.monetariasLiqInsSubContrato = monetariasLiqInsSubContrato;
    }
    public Integer getMonetariasLiqInsNumFolioInst() {
        return monetariasLiqInsNumFolioInst;
    }
    public void setMonetariasLiqInsNumFolioInst(Integer monetariasLiqInsNumFolioInst) {
        this.monetariasLiqInsNumFolioInst = monetariasLiqInsNumFolioInst;
    }
     public String getMonetariasLiqInsAdcCuenta() {
        return monetariasLiqInsAdcCuenta;
    }
    public void setMonetariasLiqInsAdcCuenta(String monetariasLiqInsAdcCuenta) {
        this.monetariasLiqInsAdcCuenta = monetariasLiqInsAdcCuenta;
    }   
   public Long getMonetariasLiqInsAdcSucursal() {
        return monetariasLiqInsAdcSucursal;
    }
    public void setMonetariasLiqInsAdcSucursal(Long monetariasLiqInsAdcSucursal) {
        this.monetariasLiqInsAdcSucursal = monetariasLiqInsAdcSucursal;
    }
    public String getMonetariasLiqInsCveDescClave() {
        return monetariasLiqInsCveDescClave;
    }
    public void setMonetariasLiqInsCveDescClave(String monetariasLiqInsCveDescClave) {
        this.monetariasLiqInsCveDescClave = monetariasLiqInsCveDescClave;
    }
     public Integer getMonetariasLiqInsAdcNumMoneda() {
        return monetariasLiqInsAdcNumMoneda;
    }
    public void setMonetariasLiqInsAdcNumMoneda(Integer monetariasLiqInsAdcNumMoneda) {
        this.monetariasLiqInsAdcNumMoneda = monetariasLiqInsAdcNumMoneda;
    }
    public String getMonetariasLiqInsMonNomMoneda() {
        return monetariasLiqInsMonNomMoneda;
    }
    public void setMonetariasLiqInsMonNomMoneda(String monetariasLiqInsMonNomMoneda) {
        this.monetariasLiqInsMonNomMoneda = monetariasLiqInsMonNomMoneda;
    }
        
    //Constructor
    public MonetariaLiquidacionBean() {
    }








 
    

}