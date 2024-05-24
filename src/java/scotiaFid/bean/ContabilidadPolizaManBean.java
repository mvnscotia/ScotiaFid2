/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : ContabilidadBienFideBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210401
 * MODIFICADO  : 20211126
 * AUTOR       : smartBuilder 
 * NOTAS       : 20211126.- Se agrega atributo polizaEncaFolioSalida 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadPolizaMan")
@ViewScoped
public class ContabilidadPolizaManBean implements Serializable{
  //Atributos privados
    private String                     polizaEncaNombre;
    private String                     polizaEncaNumero;
    private Date                       polizaEncaFechaVal;
    private Date                       polizaEncaFechaMovto;
    private String                     polizaEncaAportPatrim;
    private Long                       polizaEncaFolio;
    private Long                       polizaEncaFolioSalida;
    private String                     polizaEncaHoraAplica;
    private String                     polizaEncaRedaccion;
    private String                     polizaEncaLiqCveMovto;
    private Integer                    polizaEncaLiqCveCpto;
    private Integer                    polizaEncaSugerCve;
    private String                     polizaEncaFidRol;
    private String                     polizaEncaFidPers;
    private Integer                    polizaEncaFidNum;
    private String                     polizaEncaTipoPol;
    private String[]                   polizaEncaCamposArr;
    private Integer                    polizaEncaCamposNum;
    private String                     polizaEncaBitTipoOper;
    private String                     polizaEncaBitPantalla;
    private String                     polizaEncaBitTerminal;
    private Integer                    polizaEncaBitUsuario;
    private String                     poliza00Etiqueta;
    private String                     poliza00Valor;
    private String                     poliza00Visible;
    private String                     poliza01Etiqueta;
    private String                     poliza01Valor;
    private String                     poliza01Visible;
    private String                     poliza02Etiqueta;
    private String                     poliza02Valor;
    private String                     poliza02Visible;
    private String                     poliza03Etiqueta;
    private String                     poliza03Valor;
    private String                     poliza03Visible;
    private String                     poliza04Etiqueta;
    private String                     poliza04Valor;
    private String                     poliza04Visible;
    private String                     poliza05Etiqueta;
    private String                     poliza05Valor;
    private String                     poliza05Visible;
    private String                     poliza06Etiqueta;
    private String                     poliza06Valor;
    private String                     poliza06Visible;
    private String                     poliza07Etiqueta;
    private String                     poliza07Valor;
    private String                     poliza07Visible;
    private String                     poliza08Etiqueta;
    private String                     poliza08Valor;
    private String                     poliza08Visible;
    private String                     poliza09Etiqueta;
    private String                     poliza09Valor;
    private String                     poliza09Visible;
    private String                     poliza10Etiqueta;
    private String                     poliza10Valor;
    private String                     poliza10Visible;
    private String                     poliza11Etiqueta;
    private String                     poliza11Valor;
    private String                     poliza11Visible;
    private String                     poliza12Etiqueta;
    private String                     poliza12Valor;
    private String                     poliza12Visible;
    private String                     poliza13Etiqueta;
    private String                     poliza13Valor;
    private String                     poliza13Visible;
    private String                     poliza14Etiqueta;
    private String                     poliza14Valor;
    private String                     poliza14Visible;
    private String                     poliza15Etiqueta;
    private String                     poliza15Valor;
    private String                     poliza15Visible;
    private String                     poliza16Etiqueta;
    private String                     poliza16Valor;
    private String                     poliza16Visible;
    private String                     poliza17Etiqueta;
    private String                     poliza17Valor;
    private String                     poliza17Visible;
    private String                     poliza18Etiqueta;
    private String                     poliza18Valor;
    private String                     poliza18Visible;
    private String                     poliza19Etiqueta;
    private String                     poliza19Valor;
    private String                     poliza19Visible;
    
  //Gettes y Setters
    public String getPolizaEncaNombre() {
        return polizaEncaNombre;
    }

    public void setPolizaEncaNombre(String polizaEncaNombre) {
        this.polizaEncaNombre = polizaEncaNombre;
    }

    public String getPolizaEncaNumero() {
        return polizaEncaNumero;
    }

    public void setPolizaEncaNumero(String polizaEncaNumero) {
        this.polizaEncaNumero = polizaEncaNumero;
    }

    public Date getPolizaEncaFechaVal() {
        return polizaEncaFechaVal;
    }

    public void setPolizaEncaFechaVal(Date polizaEncaFechaVal) {
        this.polizaEncaFechaVal = polizaEncaFechaVal;
    }

    public Date getPolizaEncaFechaMovto() {
        return polizaEncaFechaMovto;
    }

    public void setPolizaEncaFechaMovto(Date polizaEncaFechaMovto) {
        this.polizaEncaFechaMovto = polizaEncaFechaMovto;
    }

    public String getPolizaEncaAportPatrim() {
        return polizaEncaAportPatrim;
    }

    public void setPolizaEncaAportPatrim(String polizaEncaAportPatrim) {
        this.polizaEncaAportPatrim = polizaEncaAportPatrim;
    }

    public Long getPolizaEncaFolio() {
        return polizaEncaFolio;
    }

    public void setPolizaEncaFolio(Long polizaEncaFolio) {
        this.polizaEncaFolio = polizaEncaFolio;
    }

    public Long getPolizaEncaFolioSalida() {
        return polizaEncaFolioSalida;
    }

    public void setPolizaEncaFolioSalida(Long polizaEncaFolioSalida) {
        this.polizaEncaFolioSalida = polizaEncaFolioSalida;
    }

    public String getPolizaEncaHoraAplica() {
        return polizaEncaHoraAplica;
    }

    public void setPolizaEncaHoraAplica(String polizaEncaHoraAplica) {
        this.polizaEncaHoraAplica = polizaEncaHoraAplica;
    }

    public String getPolizaEncaRedaccion() {
        return polizaEncaRedaccion;
    }

    public void setPolizaEncaRedaccion(String polizaEncaRedaccion) {
        this.polizaEncaRedaccion = polizaEncaRedaccion;
    }

    public String getPolizaEncaLiqCveMovto() {
        return polizaEncaLiqCveMovto;
    }

    public void setPolizaEncaLiqCveMovto(String polizaEncaLiqCveMovto) {
        this.polizaEncaLiqCveMovto = polizaEncaLiqCveMovto;
    }

    public Integer getPolizaEncaLiqCveCpto() {
        return polizaEncaLiqCveCpto;
    }

    public void setPolizaEncaLiqCveCpto(Integer polizaEncaLiqCveCpto) {
        this.polizaEncaLiqCveCpto = polizaEncaLiqCveCpto;
    }

    public Integer getPolizaEncaSugerCve() {
        return polizaEncaSugerCve;
    }

    public void setPolizaEncaSugerCve(Integer polizaEncaSugerCve) {
        this.polizaEncaSugerCve = polizaEncaSugerCve;
    }

    public String getPolizaEncaFidRol() {
        return polizaEncaFidRol;
    }

    public void setPolizaEncaFidRol(String polizaEncaFidRol) {
        this.polizaEncaFidRol = polizaEncaFidRol;
    }

    public String getPolizaEncaFidPers() {
        return polizaEncaFidPers;
    }

    public void setPolizaEncaFidPers(String polizaEncaFidPers) {
        this.polizaEncaFidPers = polizaEncaFidPers;
    }

    public Integer getPolizaEncaFidNum() {
        return polizaEncaFidNum;
    }

    public void setPolizaEncaFidNum(Integer polizaEncaFidNum) {
        this.polizaEncaFidNum = polizaEncaFidNum;
    }

    public String getPolizaEncaTipoPol() {
        return polizaEncaTipoPol;
    }

    public void setPolizaEncaTipoPol(String polizaEncaTipoPol) {
        this.polizaEncaTipoPol = polizaEncaTipoPol;
    }

    public String[] getPolizaEncaCamposArr() {
        return polizaEncaCamposArr;
    }

    public void setPolizaEncaCamposArr(String[] polizaEncaCamposArr) {
        this.polizaEncaCamposArr = polizaEncaCamposArr;
    }

    public Integer getPolizaEncaCamposNum() {
        return polizaEncaCamposNum;
    }

    public void setPolizaEncaCamposNum(Integer polizaEncaCamposNum) {
        this.polizaEncaCamposNum = polizaEncaCamposNum;
    }

    public String getPolizaEncaBitTipoOper() {
        return polizaEncaBitTipoOper;
    }

    public void setPolizaEncaBitTipoOper(String polizaEncaBitTipoOper) {
        this.polizaEncaBitTipoOper = polizaEncaBitTipoOper;
    }

    public String getPolizaEncaBitPantalla() {
        return polizaEncaBitPantalla;
    }

    public void setPolizaEncaBitPantalla(String polizaEncaBitPantalla) {
        this.polizaEncaBitPantalla = polizaEncaBitPantalla;
    }

    public String getPolizaEncaBitTerminal() {
        return polizaEncaBitTerminal;
    }

    public void setPolizaEncaBitTerminal(String polizaEncaBitTerminal) {
        this.polizaEncaBitTerminal = polizaEncaBitTerminal;
    }

    public Integer getPolizaEncaBitUsuario() {
        return polizaEncaBitUsuario;
    }

    public void setPolizaEncaBitUsuario(Integer polizaEncaBitUsuario) {
        this.polizaEncaBitUsuario = polizaEncaBitUsuario;
    }

    public String getPoliza00Etiqueta() {
        return poliza00Etiqueta;
    }

    public void setPoliza00Etiqueta(String poliza00Etiqueta) {
        this.poliza00Etiqueta = poliza00Etiqueta;
    }

    public String getPoliza00Valor() {
        return poliza00Valor;
    }

    public void setPoliza00Valor(String poliza00Valor) {
        this.poliza00Valor = poliza00Valor;
    }

    public String getPoliza00Visible() {
        return poliza00Visible;
    }

    public void setPoliza00Visible(String poliza00Visible) {
        this.poliza00Visible = poliza00Visible;
    }

    public String getPoliza01Etiqueta() {
        return poliza01Etiqueta;
    }

    public void setPoliza01Etiqueta(String poliza01Etiqueta) {
        this.poliza01Etiqueta = poliza01Etiqueta;
    }

    public String getPoliza01Valor() {
        return poliza01Valor;
    }

    public void setPoliza01Valor(String poliza01Valor) {
        this.poliza01Valor = poliza01Valor;
    }

    public String getPoliza01Visible() {
        return poliza01Visible;
    }

    public void setPoliza01Visible(String poliza01Visible) {
        this.poliza01Visible = poliza01Visible;
    }

    public String getPoliza02Etiqueta() {
        return poliza02Etiqueta;
    }

    public void setPoliza02Etiqueta(String poliza02Etiqueta) {
        this.poliza02Etiqueta = poliza02Etiqueta;
    }

    public String getPoliza02Valor() {
        return poliza02Valor;
    }

    public void setPoliza02Valor(String poliza02Valor) {
        this.poliza02Valor = poliza02Valor;
    }

    public String getPoliza02Visible() {
        return poliza02Visible;
    }

    public void setPoliza02Visible(String poliza02Visible) {
        this.poliza02Visible = poliza02Visible;
    }

    public String getPoliza03Etiqueta() {
        return poliza03Etiqueta;
    }

    public void setPoliza03Etiqueta(String poliza03Etiqueta) {
        this.poliza03Etiqueta = poliza03Etiqueta;
    }

    public String getPoliza03Valor() {
        return poliza03Valor;
    }

    public void setPoliza03Valor(String poliza03Valor) {
        this.poliza03Valor = poliza03Valor;
    }

    public String getPoliza03Visible() {
        return poliza03Visible;
    }

    public void setPoliza03Visible(String poliza03Visible) {
        this.poliza03Visible = poliza03Visible;
    }

    public String getPoliza04Etiqueta() {
        return poliza04Etiqueta;
    }

    public void setPoliza04Etiqueta(String poliza04Etiqueta) {
        this.poliza04Etiqueta = poliza04Etiqueta;
    }

    public String getPoliza04Valor() {
        return poliza04Valor;
    }

    public void setPoliza04Valor(String poliza04Valor) {
        this.poliza04Valor = poliza04Valor;
    }

    public String getPoliza04Visible() {
        return poliza04Visible;
    }

    public void setPoliza04Visible(String poliza04Visible) {
        this.poliza04Visible = poliza04Visible;
    }

    public String getPoliza05Etiqueta() {
        return poliza05Etiqueta;
    }

    public void setPoliza05Etiqueta(String poliza05Etiqueta) {
        this.poliza05Etiqueta = poliza05Etiqueta;
    }

    public String getPoliza05Valor() {
        return poliza05Valor;
    }

    public void setPoliza05Valor(String poliza05Valor) {
        this.poliza05Valor = poliza05Valor;
    }

    public String getPoliza05Visible() {
        return poliza05Visible;
    }

    public void setPoliza05Visible(String poliza05Visible) {
        this.poliza05Visible = poliza05Visible;
    }

    public String getPoliza06Etiqueta() {
        return poliza06Etiqueta;
    }

    public void setPoliza06Etiqueta(String poliza06Etiqueta) {
        this.poliza06Etiqueta = poliza06Etiqueta;
    }

    public String getPoliza06Valor() {
        return poliza06Valor;
    }

    public void setPoliza06Valor(String poliza06Valor) {
        this.poliza06Valor = poliza06Valor;
    }

    public String getPoliza06Visible() {
        return poliza06Visible;
    }

    public void setPoliza06Visible(String poliza06Visible) {
        this.poliza06Visible = poliza06Visible;
    }

    public String getPoliza07Etiqueta() {
        return poliza07Etiqueta;
    }

    public void setPoliza07Etiqueta(String poliza07Etiqueta) {
        this.poliza07Etiqueta = poliza07Etiqueta;
    }

    public String getPoliza07Valor() {
        return poliza07Valor;
    }

    public void setPoliza07Valor(String poliza07Valor) {
        this.poliza07Valor = poliza07Valor;
    }

    public String getPoliza07Visible() {
        return poliza07Visible;
    }

    public void setPoliza07Visible(String poliza07Visible) {
        this.poliza07Visible = poliza07Visible;
    }

    public String getPoliza08Etiqueta() {
        return poliza08Etiqueta;
    }

    public void setPoliza08Etiqueta(String poliza08Etiqueta) {
        this.poliza08Etiqueta = poliza08Etiqueta;
    }

    public String getPoliza08Valor() {
        return poliza08Valor;
    }

    public void setPoliza08Valor(String poliza08Valor) {
        this.poliza08Valor = poliza08Valor;
    }

    public String getPoliza08Visible() {
        return poliza08Visible;
    }

    public void setPoliza08Visible(String poliza08Visible) {
        this.poliza08Visible = poliza08Visible;
    }

    public String getPoliza09Etiqueta() {
        return poliza09Etiqueta;
    }

    public void setPoliza09Etiqueta(String poliza09Etiqueta) {
        this.poliza09Etiqueta = poliza09Etiqueta;
    }

    public String getPoliza09Valor() {
        return poliza09Valor;
    }

    public void setPoliza09Valor(String poliza09Valor) {
        this.poliza09Valor = poliza09Valor;
    }

    public String getPoliza09Visible() {
        return poliza09Visible;
    }

    public void setPoliza09Visible(String poliza09Visible) {
        this.poliza09Visible = poliza09Visible;
    }

    public String getPoliza10Etiqueta() {
        return poliza10Etiqueta;
    }

    public void setPoliza10Etiqueta(String poliza10Etiqueta) {
        this.poliza10Etiqueta = poliza10Etiqueta;
    }

    public String getPoliza10Valor() {
        return poliza10Valor;
    }

    public void setPoliza10Valor(String poliza10Valor) {
        this.poliza10Valor = poliza10Valor;
    }

    public String getPoliza10Visible() {
        return poliza10Visible;
    }

    public void setPoliza10Visible(String poliza10Visible) {
        this.poliza10Visible = poliza10Visible;
    }

    public String getPoliza11Etiqueta() {
        return poliza11Etiqueta;
    }

    public void setPoliza11Etiqueta(String poliza11Etiqueta) {
        this.poliza11Etiqueta = poliza11Etiqueta;
    }

    public String getPoliza11Valor() {
        return poliza11Valor;
    }

    public void setPoliza11Valor(String poliza11Valor) {
        this.poliza11Valor = poliza11Valor;
    }

    public String getPoliza11Visible() {
        return poliza11Visible;
    }

    public void setPoliza11Visible(String poliza11Visible) {
        this.poliza11Visible = poliza11Visible;
    }

    public String getPoliza12Etiqueta() {
        return poliza12Etiqueta;
    }

    public void setPoliza12Etiqueta(String poliza12Etiqueta) {
        this.poliza12Etiqueta = poliza12Etiqueta;
    }

    public String getPoliza12Valor() {
        return poliza12Valor;
    }

    public void setPoliza12Valor(String poliza12Valor) {
        this.poliza12Valor = poliza12Valor;
    }

    public String getPoliza12Visible() {
        return poliza12Visible;
    }

    public void setPoliza12Visible(String poliza12Visible) {
        this.poliza12Visible = poliza12Visible;
    }

    public String getPoliza13Etiqueta() {
        return poliza13Etiqueta;
    }

    public void setPoliza13Etiqueta(String poliza13Etiqueta) {
        this.poliza13Etiqueta = poliza13Etiqueta;
    }

    public String getPoliza13Valor() {
        return poliza13Valor;
    }

    public void setPoliza13Valor(String poliza13Valor) {
        this.poliza13Valor = poliza13Valor;
    }

    public String getPoliza13Visible() {
        return poliza13Visible;
    }

    public void setPoliza13Visible(String poliza13Visible) {
        this.poliza13Visible = poliza13Visible;
    }

    public String getPoliza14Etiqueta() {
        return poliza14Etiqueta;
    }

    public void setPoliza14Etiqueta(String poliza14Etiqueta) {
        this.poliza14Etiqueta = poliza14Etiqueta;
    }

    public String getPoliza14Valor() {
        return poliza14Valor;
    }

    public void setPoliza14Valor(String poliza14Valor) {
        this.poliza14Valor = poliza14Valor;
    }

    public String getPoliza14Visible() {
        return poliza14Visible;
    }

    public void setPoliza14Visible(String poliza14Visible) {
        this.poliza14Visible = poliza14Visible;
    }

    public String getPoliza15Etiqueta() {
        return poliza15Etiqueta;
    }

    public void setPoliza15Etiqueta(String poliza15Etiqueta) {
        this.poliza15Etiqueta = poliza15Etiqueta;
    }

    public String getPoliza15Valor() {
        return poliza15Valor;
    }

    public void setPoliza15Valor(String poliza15Valor) {
        this.poliza15Valor = poliza15Valor;
    }

    public String getPoliza15Visible() {
        return poliza15Visible;
    }

    public void setPoliza15Visible(String poliza15Visible) {
        this.poliza15Visible = poliza15Visible;
    }

    public String getPoliza16Etiqueta() {
        return poliza16Etiqueta;
    }

    public void setPoliza16Etiqueta(String poliza16Etiqueta) {
        this.poliza16Etiqueta = poliza16Etiqueta;
    }

    public String getPoliza16Valor() {
        return poliza16Valor;
    }

    public void setPoliza16Valor(String poliza16Valor) {
        this.poliza16Valor = poliza16Valor;
    }

    public String getPoliza16Visible() {
        return poliza16Visible;
    }

    public void setPoliza16Visible(String poliza16Visible) {
        this.poliza16Visible = poliza16Visible;
    }

    public String getPoliza17Etiqueta() {
        return poliza17Etiqueta;
    }

    public void setPoliza17Etiqueta(String poliza17Etiqueta) {
        this.poliza17Etiqueta = poliza17Etiqueta;
    }

    public String getPoliza17Valor() {
        return poliza17Valor;
    }

    public void setPoliza17Valor(String poliza17Valor) {
        this.poliza17Valor = poliza17Valor;
    }

    public String getPoliza17Visible() {
        return poliza17Visible;
    }

    public void setPoliza17Visible(String poliza17Visible) {
        this.poliza17Visible = poliza17Visible;
    }

    public String getPoliza18Etiqueta() {
        return poliza18Etiqueta;
    }

    public void setPoliza18Etiqueta(String poliza18Etiqueta) {
        this.poliza18Etiqueta = poliza18Etiqueta;
    }

    public String getPoliza18Valor() {
        return poliza18Valor;
    }

    public void setPoliza18Valor(String poliza18Valor) {
        this.poliza18Valor = poliza18Valor;
    }

    public String getPoliza18Visible() {
        return poliza18Visible;
    }

    public void setPoliza18Visible(String poliza18Visible) {
        this.poliza18Visible = poliza18Visible;
    }

    public String getPoliza19Etiqueta() {
        return poliza19Etiqueta;
    }

    public void setPoliza19Etiqueta(String poliza19Etiqueta) {
        this.poliza19Etiqueta = poliza19Etiqueta;
    }

    public String getPoliza19Valor() {
        return poliza19Valor;
    }

    public void setPoliza19Valor(String poliza19Valor) {
        this.poliza19Valor = poliza19Valor;
    }

    public String getPoliza19Visible() {
        return poliza19Visible;
    }

    public void setPoliza19Visible(String poliza19Visible) {
        this.poliza19Visible = poliza19Visible;
    }
    
  //Constructor
    public ContabilidadPolizaManBean() {
        
    }
}