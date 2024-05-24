/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : OutParameterBean.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;

//Class
public class OutParameterBean implements Serializable {
   //Serial
   private static final long serialVersionUID = 1L;
   
   //Members
   private int bEjecuto;
   private int iNumFolioContab;
   private double dImporteCobrado;
   private String psMsgErrOut;
   
   //Getters_Setters
   public int getbEjecuto() {
	 return bEjecuto;
   }

   public void setbEjecuto(int bEjecuto) {
	 this.bEjecuto = bEjecuto;
   }

   public int getiNumFolioContab() {
	 return iNumFolioContab;
   }
   
   public void setiNumFolioContab(int iNumFolioContab) {
	 this.iNumFolioContab = iNumFolioContab;
   }
   
   public double getdImporteCobrado() {
	 return dImporteCobrado;
   }

   public void setdImporteCobrado(double dImporteCobrado) {
	 this.dImporteCobrado = dImporteCobrado;
   }

   public String getPsMsgErrOut() {
	 return psMsgErrOut;
   }
   
   public void setPsMsgErrOut(String psMsgErrOut) {
	 this.psMsgErrOut = psMsgErrOut;
   }
}
