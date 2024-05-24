/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : ContabilidadSaldoBean.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.bean
 * CREADO      : 20210320
 * MODIFICADO  : 20210901
 * AUTOR       : j.delatorre 
 * NOTAS       : 20210901.- Se ajusta tipo de dato, pasamos de Integer a Short
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "beanContabilidadSaldo")
@ViewScoped
public class ContabilidadSaldoBean implements Serializable{
  //Atributos privados
    private Short                      saldoCTAM;
    private Short                      saldoSC1;
    private Short                      saldoSC2;
    private Short                      saldoSC3;
    private Short                      saldoSC4;
    private String                     saldoCuentaNombre;
    private String                     saldoMonedaNombre;
    private Short                      saldoMonedaNumero;
    private Long                       saldoAX1;
    private Long                       saldoAX2;
    private Long                       saldoAX3;
    private Double                     saldoInicioMes;
    private Double                     saldoCargosMes;
    private Double                     saldoAbonosMes;
    private Double                     saldoInicioEjer;
    private Double                     saldoCargosEjer;
    private Double                     saldoAbonosEjer;
    private Double                     saldoActual;
    private Short                      saldoMes;
    private Short                      saldoAño;
    private Date                       saldoFechaUltMod;
    private Integer                    saldoNcargosMes;
    private Integer                    saldoNAbonosMes;
    private Integer                    saldoNcargosEjer;
    private Integer                    saldoNAbonosEjer;
  //Getters y Setters
    public Short getSaldoCTAM() {
        return saldoCTAM;
    }

    public void setSaldoCTAM(Short saldoCTAM) {
        this.saldoCTAM = saldoCTAM;
    }

    public Short getSaldoSC1() {
        return saldoSC1;
    }

    public void setSaldoSC1(Short saldoSC1) {
        this.saldoSC1 = saldoSC1;
    }

    public Short getSaldoSC2() {
        return saldoSC2;
    }

    public void setSaldoSC2(Short saldoSC2) {
        this.saldoSC2 = saldoSC2;
    }

    public Short getSaldoSC3() {
        return saldoSC3;
    }

    public void setSaldoSC3(Short saldoSC3) {
        this.saldoSC3 = saldoSC3;
    }

    public Short getSaldoSC4() {
        return saldoSC4;
    }

    public void setSaldoSC4(Short saldoSC4) {
        this.saldoSC4 = saldoSC4;
    }

    public String getSaldoCuentaNombre() {
        return saldoCuentaNombre;
    }

    public void setSaldoCuentaNombre(String saldoCuentaNombre) {
        this.saldoCuentaNombre = saldoCuentaNombre;
    }

    public String getSaldoMonedaNombre() {
        return saldoMonedaNombre;
    }

    public void setSaldoMonedaNombre(String saldoMonedaNombre) {
        this.saldoMonedaNombre = saldoMonedaNombre;
    }

    public Short getSaldoMonedaNumero() {
        return saldoMonedaNumero;
    }

    public void setSaldoMonedaNumero(Short saldoMonedaNumero) {
        this.saldoMonedaNumero = saldoMonedaNumero;
    }

    public Long getSaldoAX1() {
        return saldoAX1;
    }

    public void setSaldoAX1(Long saldoAX1) {
        this.saldoAX1 = saldoAX1;
    }

    public Long getSaldoAX2() {
        return saldoAX2;
    }

    public void setSaldoAX2(Long saldoAX2) {
        this.saldoAX2 = saldoAX2;
    }

    public Long getSaldoAX3() {
        return saldoAX3;
    }

    public void setSaldoAX3(Long saldoAX3) {
        this.saldoAX3 = saldoAX3;
    }

    public Double getSaldoInicioMes() {
        return saldoInicioMes;
    }

    public void setSaldoInicioMes(Double saldoInicioMes) {
        this.saldoInicioMes = saldoInicioMes;
    }

    public Double getSaldoCargosMes() {
        return saldoCargosMes;
    }

    public void setSaldoCargosMes(Double saldoCargosMes) {
        this.saldoCargosMes = saldoCargosMes;
    }

    public Double getSaldoAbonosMes() {
        return saldoAbonosMes;
    }

    public void setSaldoAbonosMes(Double saldoAbonosMes) {
        this.saldoAbonosMes = saldoAbonosMes;
    }

    public Double getSaldoInicioEjer() {
        return saldoInicioEjer;
    }

    public void setSaldoInicioEjer(Double saldoInicioEjer) {
        this.saldoInicioEjer = saldoInicioEjer;
    }

    public Double getSaldoCargosEjer() {
        return saldoCargosEjer;
    }

    public void setSaldoCargosEjer(Double saldoCargosEjer) {
        this.saldoCargosEjer = saldoCargosEjer;
    }

    public Double getSaldoAbonosEjer() {
        return saldoAbonosEjer;
    }

    public void setSaldoAbonosEjer(Double saldoAbonosEjer) {
        this.saldoAbonosEjer = saldoAbonosEjer;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Short getSaldoMes() {
        return saldoMes;
    }

    public void setSaldoMes(Short saldoMes) {
        this.saldoMes = saldoMes;
    }

    public Short getSaldoAño() {
        return saldoAño;
    }

    public void setSaldoAño(Short saldoAño) {
        this.saldoAño = saldoAño;
    }

    public Date getSaldoFechaUltMod() {
        return saldoFechaUltMod;
    }

    public void setSaldoFechaUltMod(Date saldoFechaUltMod) {
        this.saldoFechaUltMod = saldoFechaUltMod;
    }
    
  //Constructor
    public ContabilidadSaldoBean() {
        
    }

    public Integer getSaldoNcargosMes() {
        return saldoNcargosMes;
    }

    public void setSaldoNcargosMes(Integer saldoNcargosMes) {
        this.saldoNcargosMes = saldoNcargosMes;
    }

    public Integer getSaldoNAbonosMes() {
        return saldoNAbonosMes;
    }

    public void setSaldoNAbonosMes(Integer saldoNAbonosMes) {
        this.saldoNAbonosMes = saldoNAbonosMes;
    }

    public Integer getSaldoNcargosEjer() {
        return saldoNcargosEjer;
    }

    public void setSaldoNcargosEjer(Integer saldoNcargosEjer) {
        this.saldoNcargosEjer = saldoNcargosEjer;
    }

    public Integer getSaldoNAbonosEjer() {
        return saldoNAbonosEjer;
    }

    public void setSaldoNAbonosEjer(Integer saldoNAbonosEjer) {
        this.saldoNAbonosEjer = saldoNAbonosEjer;
    }
}