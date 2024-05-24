/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210324
 * MODIFICADO  : 20210324
 * AUTOR       : smartBuilder 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanInstruccionFideicomiso")
@ViewScoped
public class InstruccionFideicomisoBean implements Serializable{
    
  //Atributos privados
    private String                     instrucFideCtoNomContrato;
    private Long                       instrucFideInsNumContrato;
    private Integer                    instrucFideInsNumFolioInst;
    private String                     instrucFideInsCboSubContrato;
    private Integer                    instrucFideInsSubContrato;
    private String                     instrucFideInsTxtComentario;
    private String                     instrucFideInsCveTipoInstr;
    private String                     instrucFideInsNumMiembro;
    private String                     instrucFideInsNomMiembro;
    private String                     instrucFideInsAnoAltaReg;
    private String                     instrucFideInsMesAltaReg;
    private String                     instrucFideInsDiaAltaReg;
    private String                     instrucFideExpNumExpediente;
    private String                     instrucFideExdIdDocumento;
    private String                     instrucFideExdTextComentar;
    
    private Date                       instrucFideFechaDeInstruccion;
    
    private String                     instrucFideBitTerminal;
    private Integer                    instrucFideBitUsuario;  
    private String                     instrucFideBitPantalla;
    private String                     instrucFideBitTipoOperacion;
    private String                     instrucFideBitDetalleBitacora;
    
    private Integer                     instrucFideExpArchivo;
    private Integer                     instrucFideExpArchivero;
    private Integer                     instrucFideExpNiivel;
    private String                     txtInstrucFideExpArchivo;
    private String                     txtInstrucFideExpArchivero;
    private String                     txtInstrucFideExpNiivel;
    private String                      txtInstrucFideInsNumContrato;
    
    //Getetrs y Setters

    public String getInstrucFideCtoNomContrato() {
        return instrucFideCtoNomContrato;
    }

    public void setInstrucFideCtoNomContrato(String instrucFideCtoNomContrato) {
        this.instrucFideCtoNomContrato = instrucFideCtoNomContrato;
    }

    public Long getInstrucFideInsNumContrato() {
        return instrucFideInsNumContrato;
    }

    public void setInstrucFideInsNumContrato(Long instrucFideInsNumContrato) {
        this.instrucFideInsNumContrato = instrucFideInsNumContrato;
    }

    public Integer getInstrucFideInsNumFolioInst() {
        return instrucFideInsNumFolioInst;
    }

    public void setInstrucFideInsNumFolioInst(Integer instrucFideInsNumFolioInst) {
        this.instrucFideInsNumFolioInst = instrucFideInsNumFolioInst;
    }
    
    public String getInstrucFideInsCboSubContrato() {
        return instrucFideInsCboSubContrato;
    }

    public void setInstrucFideInsCboSubContrato(String instrucFideInsCboSubContrato) {
        this.instrucFideInsCboSubContrato = instrucFideInsCboSubContrato;
    }    

    public Integer getInstrucFideInsSubContrato() {
        return instrucFideInsSubContrato;
    }

    public void setInstrucFideInsSubContrato(Integer instrucFideInsSubContrato) {
        this.instrucFideInsSubContrato = instrucFideInsSubContrato;
    }

    public String getInstrucFideInsTxtComentario() {
        return instrucFideInsTxtComentario;
    }

    public void setInstrucFideInsTxtComentario(String instrucFideInsTxtComentario) {
        this.instrucFideInsTxtComentario = instrucFideInsTxtComentario;
    }

    public String getInstrucFideInsCveTipoInstr() {
        return instrucFideInsCveTipoInstr;
    }

    public void setInstrucFideInsCveTipoInstr(String instrucFideInsCveTipoInstr) {
        this.instrucFideInsCveTipoInstr = instrucFideInsCveTipoInstr;
    }

    public String getInstrucFideInsNumMiembro() {
        return instrucFideInsNumMiembro;
    }

    public void setInstrucFideInsNumMiembro(String instrucFideInsNumMiembro) {
        this.instrucFideInsNumMiembro = instrucFideInsNumMiembro;
    }

    public String getInstrucFideInsNomMiembro() {
        return instrucFideInsNomMiembro;
    }

    public void setInstrucFideInsNomMiembro(String instrucFideInsNomMiembro) {
        this.instrucFideInsNomMiembro = instrucFideInsNomMiembro;
    }

    public String getInstrucFideInsAnoAltaReg() {
        return instrucFideInsAnoAltaReg;
    }

    public void setInstrucFideInsAnoAltaReg(String instrucFideInsAnoAltaReg) {
        this.instrucFideInsAnoAltaReg = instrucFideInsAnoAltaReg;
    }

    public String getInstrucFideInsMesAltaReg() {
        return instrucFideInsMesAltaReg;
    }

    public void setInstrucFideInsMesAltaReg(String instrucFideInsMesAltaReg) {
        this.instrucFideInsMesAltaReg = instrucFideInsMesAltaReg;
    }

    public String getInstrucFideInsDiaAltaReg() {
        return instrucFideInsDiaAltaReg;
    }

    public void setInstrucFideInsDiaAltaReg(String instrucFideInsDiaAltaReg) {
        this.instrucFideInsDiaAltaReg = instrucFideInsDiaAltaReg;
    }

    public String getInstrucFideExpNumExpediente() {
        return instrucFideExpNumExpediente;
    }

    public void setInstrucFideExpNumExpediente(String instrucFideExpNumExpediente) {
        this.instrucFideExpNumExpediente = instrucFideExpNumExpediente;
    }


    public String getInstrucFideExdIdDocumento() {
        return instrucFideExdIdDocumento;
    }

    public void setInstrucFideExdIdDocumento(String instrucFideExdIdDocumento) {
        this.instrucFideExdIdDocumento = instrucFideExdIdDocumento;
    }

    public String getInstrucFideExdTextComentar() {
        return instrucFideExdTextComentar;
    }

    public void setInstrucFideExdTextComentar(String instrucFideExdTextComentar) {
        this.instrucFideExdTextComentar = instrucFideExdTextComentar;
    }

    
    
    /*----------------------------------------------------------------------------------------------*/
    public Date getInstrucFideFechaDeInstruccion() {
        return instrucFideFechaDeInstruccion;
    }
    public void setInstrucFideFechaDeInstruccion(Date instrucFideFechaDeInstruccion) {
        this.instrucFideFechaDeInstruccion = instrucFideFechaDeInstruccion;
    }
    /*----------------------------------------------------------------------------------------------*/

    public String getInstrucFideBitTerminal() {
        return instrucFideBitTerminal;
    }

    public void setInstrucFideBitTerminal(String instrucFideBitTerminal) {
        this.instrucFideBitTerminal = instrucFideBitTerminal;
    }

    public Integer getInstrucFideBitUsuario() {
        return instrucFideBitUsuario;
    }

    public void setInstrucFideBitUsuario(Integer instrucFideBitUsuario) {
        this.instrucFideBitUsuario = instrucFideBitUsuario;
    }

    public String getInstrucFideBitPantalla() {
        return instrucFideBitPantalla;
    }

    public void setInstrucFideBitPantalla(String instrucFideBitPantalla) {
        this.instrucFideBitPantalla = instrucFideBitPantalla;
    }

    public String getInstrucFideBitTipoOperacion() {
        return instrucFideBitTipoOperacion;
    }

    public void setInstrucFideBitTipoOperacion(String instrucFideBitTipoOperacion) {
        this.instrucFideBitTipoOperacion = instrucFideBitTipoOperacion;
    }

    public String getInstrucFideBitDetalleBitacora() {
        return instrucFideBitDetalleBitacora;
    }

    public void setInstrucFideBitDetalleBitacora(String instrucFideBitDetalleBitacora) {
        this.instrucFideBitDetalleBitacora = instrucFideBitDetalleBitacora;
    }

   
    public Integer getInstrucFideExpArchivo() {
        return instrucFideExpArchivo;
    }

    public void setInstrucFideExpArchivo(Integer instrucFideExpArchivo) {
        this.instrucFideExpArchivo = instrucFideExpArchivo;
    }

    public Integer getInstrucFideExpArchivero() {
        return instrucFideExpArchivero;
    }

    public void setInstrucFideExpArchivero(Integer instrucFideExpArchivero) {
        this.instrucFideExpArchivero = instrucFideExpArchivero;
    }

    public Integer getInstrucFideExpNiivel() {
        return instrucFideExpNiivel;
    }

    public void setInstrucFideExpNiivel(Integer instrucFideExpNiivel) {
        this.instrucFideExpNiivel = instrucFideExpNiivel;
    }
        
    
  //Constructor
    public InstruccionFideicomisoBean() {
        
    }

    public String getTxtInstrucFideInsNumContrato() {
        return txtInstrucFideInsNumContrato;
    }

    public void setTxtInstrucFideInsNumContrato(String txtInstrucFideInsNumContrato) {
        this.txtInstrucFideInsNumContrato = txtInstrucFideInsNumContrato;
    }

    public String getTxtInstrucFideExpArchivo() {
        return txtInstrucFideExpArchivo;
    }

    public void setTxtInstrucFideExpArchivo(String txtInstrucFideExpArchivo) {
        this.txtInstrucFideExpArchivo = txtInstrucFideExpArchivo;
    }

    public String getTxtInstrucFideExpArchivero() {
        return txtInstrucFideExpArchivero;
    }

    public void setTxtInstrucFideExpArchivero(String txtInstrucFideExpArchivero) {
        this.txtInstrucFideExpArchivero = txtInstrucFideExpArchivero;
    }

    public String getTxtInstrucFideExpNiivel() {
        return txtInstrucFideExpNiivel;
    }

    public void setTxtInstrucFideExpNiivel(String txtInstrucFideExpNiivel) {
        this.txtInstrucFideExpNiivel = txtInstrucFideExpNiivel;
    }






}