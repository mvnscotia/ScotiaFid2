/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : PlanBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class PlanBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private String clave;
   private String claveAgregar;
   private String descripcion;
   private String descripcionAgregar;
   private String periodo;
   private String periodoAgregar;
   private String control;
   private String controlAgregar;
   private Date   fecha;
   private Date   fechaAgregar;
   private String fechaAplicacion;
   private String horaAplicacion;
   private String hora;
   private String minuto;
   private String horaAgregar;
   private String minutoAgregar;
   private String fechaAltaRegistro;
   private String fechaUltimaModificacion;
   private String estatus;
   private String estatusAgregar;
   
   //Constructor
   public PlanBean() {}
   
   //Getters_Setters
   public String getClave() {
      return clave;
   }
	
   public void setClave(String clave) {
      this.clave = clave;
   }
	
   public String getDescripcion() {
      return descripcion;
   }
	
   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
   
   public String getPeriodo() {
      return periodo;
   }
	
   public void setPeriodo(String periodo) {
      this.periodo = periodo;
   }
	
   public String getControl() {
      return control;
   }
	
   public void setControl(String control) {
      this.control = control;
   }
   
   public Date getFecha() {
      return fecha;
   }

   public void setFecha(Date fecha) {
      this.fecha = fecha;
   }
   
   public String getFechaAplicacion() {
      return fechaAplicacion;
   }
	
   public void setFechaAplicacion(String fechaAplicacion) {
      this.fechaAplicacion = fechaAplicacion;
   }
	
   public String getHoraAplicacion() {
      return horaAplicacion;
   }
	
   public void setHoraAplicacion(String horaAplicacion) {
	  this.horaAplicacion = horaAplicacion;
   }
   
   public String getHora() {
      return hora;
   }

   public void setHora(String hora) {
      this.hora = hora;
   }

   public String getMinuto() {
      return minuto;
   }

   public void setMinuto(String minuto) {
      this.minuto = minuto;
   }

   public String getFechaAltaRegistro() {
       return fechaAltaRegistro;
   }
	
   public void setFechaAltaRegistro(String fechaAltaRegistro) {
      this.fechaAltaRegistro = fechaAltaRegistro;
   }
	
   public String getFechaUltimaModificacion() {
      return fechaUltimaModificacion;
   }
	
   public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
      this.fechaUltimaModificacion = fechaUltimaModificacion;
   }
	
   public String getEstatus() {
      return estatus;
   }
	
   public void setEstatus(String estatus) {
      this.estatus = estatus;
   }

   public String getClaveAgregar() {
      return claveAgregar;
   }

   public void setClaveAgregar(String claveAgregar) {
      this.claveAgregar = claveAgregar;
   }

   public String getDescripcionAgregar() {
      return descripcionAgregar;
   }

   public void setDescripcionAgregar(String descripcionAgregar) {
      this.descripcionAgregar = descripcionAgregar;
   }

   public String getPeriodoAgregar() {
      return periodoAgregar;
   }

   public void setPeriodoAgregar(String periodoAgregar) {
      this.periodoAgregar = periodoAgregar;
   }

   public String getControlAgregar() {
      return controlAgregar;
   }

   public void setControlAgregar(String controlAgregar) {
      this.controlAgregar = controlAgregar;
   }

   public Date getFechaAgregar() {
      return fechaAgregar;
   }

   public void setFechaAgregar(Date fechaAgregar) {
      this.fechaAgregar = fechaAgregar;
   }

   public String getHoraAgregar() {
      return horaAgregar;
   }

   public void setHoraAgregar(String horaAgregar) {
      this.horaAgregar = horaAgregar;
   }

   public String getMinutoAgregar() {
      return minutoAgregar;
   }

   public void setMinutoAgregar(String minutoAgregar) {
      this.minutoAgregar = minutoAgregar;
   }

   public String getEstatusAgregar() {
      return estatusAgregar;
   }

   public void setEstatusAgregar(String estatusAgregar) {
      this.estatusAgregar = estatusAgregar;
   }
}
