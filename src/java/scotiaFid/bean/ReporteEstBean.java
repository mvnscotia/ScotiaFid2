/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : ReporteEstBean.java
 * TIPO        : Clase
 * PAQUETE     : acotiaFid.bean
 * CREADO      : 20210702
 * MODIFICADO  : 20210702
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanReporteEst")
@ViewScoped
public class ReporteEstBean implements Serializable{
    private Integer                    reporteEstUsuarioNumero;
    private Integer                    reporteEstReporteNumero;
    private Integer                    reporteEstContratoNumero; 
    private Integer                    reporteEstContratoSubNumero;
    private Integer                    reporteEstMonedaNumero;
    private Integer                    reporteEstOrden;
    private String                     reporteEstCol01;
    private String                     reporteEstTipoCol01;
    private String                     reporteEstCol02;
    private String                     reporteEstTipoCol02;
    private String                     reporteEstCol03;
    private String                     reporteEstTipoCol03;
    private String                     reporteEstCol04;
    private String                     reporteEstTipoCol04;
    private String                     reporteEstCol05;
    private String                     reporteEstTipoCol05;
    private String                     reporteEstCol06;
    private String                     reporteEstTipoCol06;
    private String                     reporteEstCol07;
    private String                     reporteEstTipoCol07;
    private String                     reporteEstCol08;
    private String                     reporteEstTipoCol08;
    private String                     reporteEstCol09;
    private String                     reporteEstTipoCol09;
    private String                     reporteEstCol10;
    private String                     reporteEstTipoCol10;
    private Date                       reporteEstFechaIni;
    private Date                       reporteEstFechaFin;
    private String                     reporteEstFormato;
    
  //Getters y Setters
    public Integer getReporteEstUsuarioNumero() {
        return reporteEstUsuarioNumero;
    }

    public void setReporteEstUsuarioNumero(Integer reporteEstUsuarioNumero) {
        this.reporteEstUsuarioNumero = reporteEstUsuarioNumero;
    }

    public Integer getReporteEstReporteNumero() {
        return reporteEstReporteNumero;
    }

    public void setReporteEstReporteNumero(Integer reporteEstReporteNumero) {
        this.reporteEstReporteNumero = reporteEstReporteNumero;
    }

    public Integer getReporteEstContratoNumero() {
        return reporteEstContratoNumero;
    }

    public void setReporteEstContratoNumero(Integer reporteEstContratoNumero) {
        this.reporteEstContratoNumero = reporteEstContratoNumero;
    }

    public Integer getReporteEstContratoSubNumero() {
        return reporteEstContratoSubNumero;
    }

    public void setReporteEstContratoSubNumero(Integer reporteEstContratoSubNumero) {
        this.reporteEstContratoSubNumero = reporteEstContratoSubNumero;
    }

    public Integer getReporteEstMonedaNumero() {
        return reporteEstMonedaNumero;
    }

    public void setReporteEstMonedaNumero(Integer reporteEstMonedaNumero) {
        this.reporteEstMonedaNumero = reporteEstMonedaNumero;
    }

    public Integer getReporteEstOrden() {
        return reporteEstOrden;
    }

    public void setReporteEstOrden(Integer reporteEstOrden) {
        this.reporteEstOrden = reporteEstOrden;
    }

    public String getReporteEstCol01() {
        return reporteEstCol01;
    }

    public void setReporteEstCol01(String reporteEstCol01) {
        this.reporteEstCol01 = reporteEstCol01;
    }

    public String getReporteEstTipoCol01() {
        return reporteEstTipoCol01;
    }

    public void setReporteEstTipoCol01(String reporteEstTipoCol01) {
        this.reporteEstTipoCol01 = reporteEstTipoCol01;
    }

    public String getReporteEstCol02() {
        return reporteEstCol02;
    }

    public void setReporteEstCol02(String reporteEstCol02) {
        this.reporteEstCol02 = reporteEstCol02;
    }

    public String getReporteEstTipoCol02() {
        return reporteEstTipoCol02;
    }

    public void setReporteEstTipoCol02(String reporteEstTipoCol02) {
        this.reporteEstTipoCol02 = reporteEstTipoCol02;
    }

    public String getReporteEstCol03() {
        return reporteEstCol03;
    }

    public void setReporteEstCol03(String reporteEstCol03) {
        this.reporteEstCol03 = reporteEstCol03;
    }

    public String getReporteEstTipoCol03() {
        return reporteEstTipoCol03;
    }

    public void setReporteEstTipoCol03(String reporteEstTipoCol03) {
        this.reporteEstTipoCol03 = reporteEstTipoCol03;
    }

    public String getReporteEstCol04() {
        return reporteEstCol04;
    }

    public void setReporteEstCol04(String reporteEstCol04) {
        this.reporteEstCol04 = reporteEstCol04;
    }

    public String getReporteEstTipoCol04() {
        return reporteEstTipoCol04;
    }

    public void setReporteEstTipoCol04(String reporteEstTipoCol04) {
        this.reporteEstTipoCol04 = reporteEstTipoCol04;
    }

    public String getReporteEstCol05() {
        return reporteEstCol05;
    }

    public void setReporteEstCol05(String reporteEstCol05) {
        this.reporteEstCol05 = reporteEstCol05;
    }

    public String getReporteEstTipoCol05() {
        return reporteEstTipoCol05;
    }

    public void setReporteEstTipoCol05(String reporteEstTipoCol05) {
        this.reporteEstTipoCol05 = reporteEstTipoCol05;
    }

    public String getReporteEstCol06() {
        return reporteEstCol06;
    }

    public void setReporteEstCol06(String reporteEstCol06) {
        this.reporteEstCol06 = reporteEstCol06;
    }

    public String getReporteEstTipoCol06() {
        return reporteEstTipoCol06;
    }

    public void setReporteEstTipoCol06(String reporteEstTipoCol06) {
        this.reporteEstTipoCol06 = reporteEstTipoCol06;
    }

    public String getReporteEstCol07() {
        return reporteEstCol07;
    }

    public void setReporteEstCol07(String reporteEstCol07) {
        this.reporteEstCol07 = reporteEstCol07;
    }

    public String getReporteEstTipoCol07() {
        return reporteEstTipoCol07;
    }

    public void setReporteEstTipoCol07(String reporteEstTipoCol07) {
        this.reporteEstTipoCol07 = reporteEstTipoCol07;
    }

    public String getReporteEstCol08() {
        return reporteEstCol08;
    }

    public void setReporteEstCol08(String reporteEstCol08) {
        this.reporteEstCol08 = reporteEstCol08;
    }

    public String getReporteEstTipoCol08() {
        return reporteEstTipoCol08;
    }

    public void setReporteEstTipoCol08(String reporteEstTipoCol08) {
        this.reporteEstTipoCol08 = reporteEstTipoCol08;
    }

    public String getReporteEstCol09() {
        return reporteEstCol09;
    }

    public void setReporteEstCol09(String reporteEstCol09) {
        this.reporteEstCol09 = reporteEstCol09;
    }

    public String getReporteEstTipoCol09() {
        return reporteEstTipoCol09;
    }

    public void setReporteEstTipoCol09(String reporteEstTipoCol09) {
        this.reporteEstTipoCol09 = reporteEstTipoCol09;
    }

    public String getReporteEstCol10() {
        return reporteEstCol10;
    }

    public void setReporteEstCol10(String reporteEstCol10) {
        this.reporteEstCol10 = reporteEstCol10;
    }

    public String getReporteEstTipoCol10() {
        return reporteEstTipoCol10;
    }

    public void setReporteEstTipoCol10(String reporteEstTipoCol10) {
        this.reporteEstTipoCol10 = reporteEstTipoCol10;
    }

    public Date getReporteEstFechaIni() {
        return reporteEstFechaIni;
    }

    public void setReporteEstFechaIni(Date reporteEstFechaIni) {
        this.reporteEstFechaIni = reporteEstFechaIni;
    }

    public Date getReporteEstFechaFin() {
        return reporteEstFechaFin;
    }

    public void setReporteEstFechaFin(Date reporteEstFechaFin) {
        this.reporteEstFechaFin = reporteEstFechaFin;
    }

    public String getReporteEstFormato() {
        return reporteEstFormato;
    }

    public void setReporteEstFormato(String reporteEstFormato) {
        this.reporteEstFormato = reporteEstFormato;
    }
    
  //Constructor
    public ReporteEstBean() {
        
    }
}