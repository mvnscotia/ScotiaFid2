/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : PacaHonBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class PacaHonBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private int contrato;
   private int moneda;
   private int importeFijo;
   private int porcentajePactado;
   private int claveExento;
   private int monedaExtranjera;
   private String formaCalculo;
   private String periodoCobro;
   private String personaCobra;
   private String contratoNombre;
   
   //Getters_Setters
   public int getContrato() {
     return contrato;
   }

   public void setContrato(int contrato) {
	 this.contrato = contrato;
   }

   public String getFormaCalculo() {
     return formaCalculo;
   }
   
   public void setFormaCalculo(String formaCalculo) {
     this.formaCalculo = formaCalculo;
   }
   
   public int getMoneda() {
     return moneda;
   }

   public void setMoneda(int moneda) {
     this.moneda = moneda;
   }

   public int getImporteFijo() {
     return importeFijo;
   }

   public void setImporteFijo(int importeFijo) {
     this.importeFijo = importeFijo;
   }

   public int getPorcentajePactado() {
     return porcentajePactado;
   }

   public void setPorcentajePactado(int porcentajePactado) {
     this.porcentajePactado = porcentajePactado;
   }

   public int getClaveExento() {
	 return claveExento;
   }

   public void setClaveExento(int claveExento) {
	 this.claveExento = claveExento;
   }

   public int getMonedaExtranjera() {
	 return monedaExtranjera;
   }

   public void setMonedaExtranjera(int monedaExtranjera) {
	 this.monedaExtranjera = monedaExtranjera;
   }

   public String getPeriodoCobro() {
	 return periodoCobro;
   }

   public void setPeriodoCobro(String periodoCobro) {
	 this.periodoCobro = periodoCobro;
   }

   public String getPersonaCobra() {
	 return personaCobra;
   }
   
   public void setPersonaCobra(String personaCobra) {
	 this.personaCobra = personaCobra;
   }

   public String getContratoNombre() {
	 return contratoNombre;
   }

   public void setContratoNombre(String contratoNombre) {
	 this.contratoNombre = contratoNombre;
   }
}
