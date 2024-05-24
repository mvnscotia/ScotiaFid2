/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * TIPO        : Class
 * DESCRIPCION : Tabla INV_RECHAZO_CTOINV
 * PAQUETE     : scotiaFid.bean
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.bean;

//Imports
import java.io.Serializable;
import java.util.Date;

//Class
public class InvRechazoCtoInvBean implements Serializable {

	// Serial
	private static final long serialVersionUID = 1L;

	// Members
	//_Coumnas_Tabla
	private int 	Inv_Sec;
	private double 	Cto_Inv;
	private int 	Num_Entidad_Financ;
	private String 	Entidad_Financ;
	private double 	Num_Cto;
	private String 	Nom_Cto;
	private String 	Plaza;
	private String 	Promotor;
	private String	Manejo;
	private String 	Cta_Cheques;
	private String 	Status;
	private Date 	Fec_Alta;
	private Date 	Fec_Ult_Mov;

	// Getters_Setters
	public int getInv_Sec() {
		return Inv_Sec;
	}

	public void setInv_Sec(int inv_Sec) {
		Inv_Sec = inv_Sec;
	}

	public double getCto_Inv() {
		return Cto_Inv;
	}

	public void setCto_Inv(double cto_Inv) {
		Cto_Inv = cto_Inv;
	}

	public int getNum_Entidad_Financ() {
		return Num_Entidad_Financ;
	}

	public void setNum_Entidad_Financ(int num_Entidad_Financ) {
		Num_Entidad_Financ = num_Entidad_Financ;
	}

	public String getEntidad_Financ() {
		return Entidad_Financ;
	}

	public void setEntidad_Financ(String entidad_Financ) {
		Entidad_Financ = entidad_Financ;
	}

	public double getNum_Cto() {
		return Num_Cto;
	}

	public void setNum_Cto(double num_Cto) {
		Num_Cto = num_Cto;
	}

	public String getNom_Cto() {
		return Nom_Cto;
	}

	public void setNom_Cto(String nom_Cto) {
		Nom_Cto = nom_Cto;
	}

	public String getPlaza() {
		return Plaza;
	}

	public void setPlaza(String plaza) {
		Plaza = plaza;
	}

	public String getPromotor() {
		return Promotor;
	}

	public void setPromotor(String promotor) {
		Promotor = promotor;
	}

	public String getManejo() {
		return Manejo;
	}

	public void setManejo(String manejo) {
		Manejo = manejo;
	}

	public String getCta_Cheques() {
		return Cta_Cheques;
	}

	public void setCta_Cheques(String cta_Cheques) {
		Cta_Cheques = cta_Cheques;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Date getFec_Alta() {
		return Fec_Alta;
	}

	public void setFec_Alta(Date fec_Alta) {
		Fec_Alta = fec_Alta;
	}

	public Date getFec_Ult_Mov() {
		return Fec_Ult_Mov;
	}

	public void setFec_Ult_Mov(Date fec_Ult_Mov) {
		Fec_Ult_Mov = fec_Ult_Mov;
	}

}
