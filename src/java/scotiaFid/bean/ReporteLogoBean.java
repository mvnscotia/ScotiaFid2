/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : ReporteLogoBean.java
 * TIPO        : Clase
 * PAQUETE     : acotiaFid.bean
 * CREADO      : 20211019
 * MODIFICADO  : 20211019
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Blob;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanReporteLogo")
@ViewScoped
public class ReporteLogoBean implements Serializable{
  //Atributos privados
    private Short            reporteLogoId;
    private String           reporteLogoBanco;
    private FileInputStream  reporteLogoImagenArchivo;
    private Blob             reporteLogoImagen;
    private String           reporteLogoImagenRuta;
    private Integer          reporteLogoImagenTamaño;
    private String           reporteLogoDireccion;
    private String           reporteLogoBitTipoOperacion;
    
  //Getters y Setters
    public Short getReporteLogoId() {
        return reporteLogoId;
    }

    public void setReporteLogoId(Short reporteLogoId) {
        this.reporteLogoId = reporteLogoId;
    }

    public String getReporteLogoBanco() {
        return reporteLogoBanco;
    }

    public void setReporteLogoBanco(String reporteLogoBanco) {
        this.reporteLogoBanco = reporteLogoBanco;
    }

    public FileInputStream getReporteLogoImagenArchivo() {
        return reporteLogoImagenArchivo;
    }

    public void setReporteLogoImagenArchivo(FileInputStream reporteLogoImagenArchivo) {
        this.reporteLogoImagenArchivo = reporteLogoImagenArchivo;
    }

    public Blob getReporteLogoImagen() {
        return reporteLogoImagen;
    }

    public void setReporteLogoImagen(Blob reporteLogoImagen) {
        this.reporteLogoImagen = reporteLogoImagen;
    }

    public String getReporteLogoImagenRuta() {
        return reporteLogoImagenRuta;
    }

    public void setReporteLogoImagenRuta(String reporteLogoImagenRuta) {
        this.reporteLogoImagenRuta = reporteLogoImagenRuta;
    }

    public Integer getReporteLogoImagenTamaño() {
        return reporteLogoImagenTamaño;
    }

    public void setReporteLogoImagenTamaño(Integer reporteLogoImagenTamaño) {
        this.reporteLogoImagenTamaño = reporteLogoImagenTamaño;
    }

    public String getReporteLogoDireccion() {
        return reporteLogoDireccion;
    }

    public void setReporteLogoDireccion(String reporteLogoDireccion) {
        this.reporteLogoDireccion = reporteLogoDireccion;
    }

    public String getReporteLogoBitTipoOperacion() {
        return reporteLogoBitTipoOperacion;
    }

    public void setReporteLogoBitTipoOperacion(String reporteLogoBitTipoOperacion) {
        this.reporteLogoBitTipoOperacion = reporteLogoBitTipoOperacion;
    }
    
  //Constructor
    public ReporteLogoBean() {
        
    }
}