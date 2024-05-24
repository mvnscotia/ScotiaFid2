/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : RutinaBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

import java.io.Serializable;

//Class
public class RutinaBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String rutina;
   private String rutinaAgregar;
   private String nombre;
   private String nombreAgregar;
   private String descripcion;
   private String descripcionAgregar;
   private String areaResponsable;
   private String areaResponsableAgregar;
   private String proceso;
   private String procesoAgregar;
   private String periodo;
   private String periodoAgregar;
   private String anioAltaRegistro;
   private String mesAltaRegistro;
   private String diaAltaRegistro;
   private String anioUltimaModificacion;
   private String mesUltimaModificacion;
   private String diaUltimaModificacion;
   private String estatus;
   private String estatusAgregar;
   
   //Getter_Setters
    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public String getRutinaAgregar() {
        return rutinaAgregar;
    }

    public void setRutinaAgregar(String rutinaAgregar) {
        this.rutinaAgregar = rutinaAgregar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreAgregar() {
        return nombreAgregar;
    }

    public void setNombreAgregar(String nombreAgregar) {
        this.nombreAgregar = nombreAgregar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionAgregar() {
        return descripcionAgregar;
    }

    public void setDescripcionAgregar(String descripcionAgregar) {
        this.descripcionAgregar = descripcionAgregar;
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public String getAreaResponsableAgregar() {
        return areaResponsableAgregar;
    }

    public void setAreaResponsableAgregar(String areaResponsableAgregar) {
        this.areaResponsableAgregar = areaResponsableAgregar;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getProcesoAgregar() {
        return procesoAgregar;
    }

    public void setProcesoAgregar(String procesoAgregar) {
        this.procesoAgregar = procesoAgregar;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getPeriodoAgregar() {
        return periodoAgregar;
    }

    public void setPeriodoAgregar(String periodoAgregar) {
        this.periodoAgregar = periodoAgregar;
    }

    public String getAnioAltaRegistro() {
        return anioAltaRegistro;
    }

    public void setAnioAltaRegistro(String anioAltaRegistro) {
        this.anioAltaRegistro = anioAltaRegistro;
    }

    public String getMesAltaRegistro() {
        return mesAltaRegistro;
    }

    public void setMesAltaRegistro(String mesAltaRegistro) {
        this.mesAltaRegistro = mesAltaRegistro;
    }

    public String getDiaAltaRegistro() {
        return diaAltaRegistro;
    }

    public void setDiaAltaRegistro(String diaAltaRegistro) {
        this.diaAltaRegistro = diaAltaRegistro;
    }

    public String getAnioUltimaModificacion() {
        return anioUltimaModificacion;
    }

    public void setAnioUltimaModificacion(String anioUltimaModificacion) {
        this.anioUltimaModificacion = anioUltimaModificacion;
    }

    public String getMesUltimaModificacion() {
        return mesUltimaModificacion;
    }

    public void setMesUltimaModificacion(String mesUltimaModificacion) {
        this.mesUltimaModificacion = mesUltimaModificacion;
    }

    public String getDiaUltimaModificacion() {
        return diaUltimaModificacion;
    }

    public void setDiaUltimaModificacion(String diaUltimaModificacion) {
        this.diaUltimaModificacion = diaUltimaModificacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatusAgregar() {
        return estatusAgregar;
    }

    public void setEstatusAgregar(String estatusAgregar) {
        this.estatusAgregar = estatusAgregar;
    }

}
