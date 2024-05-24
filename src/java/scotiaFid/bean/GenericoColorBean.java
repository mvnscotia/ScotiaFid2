/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : GenericoColorBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210406
 * MODIFICADO  : 20210406
 * AUTOR       : smartBuilder 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanGenericoColor")
@ViewScoped
public class GenericoColorBean implements Serializable{
  //Atributos privados  
    private String                     color00;
    private String                     color01;
    private String                     color02;
    private String                     color03;
    private String                     color04;
    private String                     color05;
    private String                     color06;
    private String                     color07;
    private String                     color08;
    private String                     color09;
    
  //Getters y Setters
    public String getColor00() {
        return color00;
    }

    public void setColor00(String color00) {
        this.color00 = color00;
    }

    public String getColor01() {
        return color01;
    }

    public void setColor01(String color01) {
        this.color01 = color01;
    }

    public String getColor02() {
        return color02;
    }

    public void setColor02(String color02) {
        this.color02 = color02;
    }

    public String getColor03() {
        return color03;
    }

    public void setColor03(String color03) {
        this.color03 = color03;
    }

    public String getColor04() {
        return color04;
    }

    public void setColor04(String color04) {
        this.color04 = color04;
    }

    public String getColor05() {
        return color05;
    }

    public void setColor05(String color05) {
        this.color05 = color05;
    }

    public String getColor06() {
        return color06;
    }

    public void setColor06(String color06) {
        this.color06 = color06;
    }

    public String getColor07() {
        return color07;
    }

    public void setColor07(String color07) {
        this.color07 = color07;
    }

    public String getColor08() {
        return color08;
    }

    public void setColor08(String color08) {
        this.color08 = color08;
    }

    public String getColor09() {
        return color09;
    }

    public void setColor09(String color09) {
        this.color09 = color09;
    }
    
  //Constructor
    public GenericoColorBean() {
        
    }
} 