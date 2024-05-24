/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : ParticipanteBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class ParticipanteBean implements Serializable {
  //Serial
  private static final long serialVersionUID = 1L;
   
  //Members
  private String fideicomiso;
  private String tipoParticipante;
  private String noParticipante;
  private String participante;
  private int porcentaje;
  
  //Getters_Setters
  public String getFideicomiso() {
    return fideicomiso;
  }

  public void setFideicomiso(String fideicomiso) {
    this.fideicomiso = fideicomiso;
  }

  public String getTipoParticipante() {
    return tipoParticipante;
  }

  public void setTipoParticipante(String tipoParticipante) {
    this.tipoParticipante = tipoParticipante;
  }

  public String getNoParticipante() {
    return noParticipante;
  }

  public void setNoParticipante(String noParticipante) {
    this.noParticipante = noParticipante;
  }

  public String getParticipante() {
    return participante;
  }

  public void setParticipante(String participante) {
    this.participante = participante;
  }

  public int getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(int porcentaje) {
    this.porcentaje = porcentaje;
  } 
}
