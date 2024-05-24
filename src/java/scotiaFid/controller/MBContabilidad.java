/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : MBContabilidad.java
 * TIPO        : Bean Administrado
 * PAQUETE     : scotiafid.controler
 * CREADO      : 20210312
 * MODIFICADO  : 20211013
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20211013.- Se implementa funcionalidad de saldosPromedio
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.primefaces.component.datatable.DataTable;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;

import scotiaFid.bean.AdminContratoBean;
import scotiaFid.bean.AdminContratoSubBean;
import scotiaFid.bean.CriterioBusquedaBean;
import scotiaFid.bean.CriterioBusquedaContaBean;
import scotiaFid.bean.CriterioBusquedaContaAsienBean;
import scotiaFid.bean.CriterioBusquedaContaSaldoBean;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.ContabilidadAsientoBean;
import scotiaFid.bean.ContabilidadBienFideBean;
import scotiaFid.bean.ContabilidadBienFideUnidadBean;
import scotiaFid.bean.ContabilidadBienFideUnidadIndivBean;
import scotiaFid.bean.ContabilidadBienFideUnidadIndivMasivoBean;
import scotiaFid.bean.ContabilidadBienFideUnidadLiqBean;
import scotiaFid.bean.ContabilidadDatoMovBean;
import scotiaFid.bean.ContabilidadDetValorBean;
import scotiaFid.bean.ContabilidadCancelaSaldoBean;
import scotiaFid.bean.ContabilidadGarantiaGralBean;
import scotiaFid.bean.ContabilidadGarantiaBienBean;
import scotiaFid.bean.ContabilidadGarantiaBienSalidaBean;
import scotiaFid.bean.ContabilidadMovtoBean;
import scotiaFid.bean.ContabilidadOperacionBean;
import scotiaFid.bean.ContabilidadPolizaManBean;
import scotiaFid.bean.ContabilidadSaldoBean;
import scotiaFid.bean.ContabilidadSaldoPromBean;
import scotiaFid.bean.ContabilidadTipoCambBean;
import scotiaFid.bean.GenericoColorBean;
import scotiaFid.bean.GenericoConsultaBean;
import scotiaFid.bean.GenericoDesHabilitadoBean;
import scotiaFid.bean.GenericoFechaBean;
import scotiaFid.bean.GenericoTituloBean;
import scotiaFid.bean.GenericoVisibleBean;
import scotiaFid.bean.MensajeConfirmaBean;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CComunes;
import scotiaFid.dao.CContabilidad;
import scotiaFid.dao.CContrato;
import scotiaFid.dao.CContratoSub;
import scotiaFid.dao.CMoneda;
import scotiaFid.dao.CPersonaFid;
import scotiaFid.util.Constantes;
import scotiaFid.util.LogsContext;

@ManagedBean(name = "mbContabilidad")
@ViewScoped
public class MBContabilidad implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBContabilidad.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final Double VALOR_MAX = 999999999999.99;
    private static DecimalFormat formatoDecimal2D;
    private static DecimalFormat formatoDecimal6D;
    private static DecimalFormat formatoImporte2D;
    private Boolean validacion;
    private Boolean valorRetorno;
    private Boolean tcVisible;
    private Integer usuarioNumero;
    private String usuarioNombre;
    private String usuarioTerminal;
    private String usuarioAppCtx;

    private List<String> usuarioFiltro;
    private List<AdminContratoSubBean> listaSubCont;
    private String mensajeError;
    private String nombreObjeto;

    private String[] arrArchivo;
    private Calendar calendario;
    private Map<String, String> parametrosFC;
    private Map<Integer, String> listaContratoSub;
  
    private FacesMessage mensaje;
    private FacesMessage mensajeCons;
    private FacesMessage mensajeCanSaldos;
    private HttpServletRequest peticionURL;

    private DecimalFormat formatoDecimal;
    private static SimpleDateFormat formatoFecha;
    private static SimpleDateFormat formatoFechaHora;
    private static SimpleDateFormat formatoHora;

    private contabilidadBienFide oContabilidadBienFide;
    private contabilidadBienFideIndiv oContabilidadBienFideIndiv;
    private contabilidadCancelaOperacion oContabilidadCancela;
    private contabilidadCancelaSaldos oContabilidadCancelaSld;
    private contabilidadConsAsie oContabilidadConsAsie;
    private contabilidadConsMovto oContabilidadConsMovto;
    private contabilidadConsSaldoProm oContabilidadConsSldPrm;
    private contabilidadGarantia oContabilidadGarantia;
    private contabilidadGrales oContabilidadGrales;
    private contabilidadPolizaMan oContabilidadPolMan;
    private contabilidadSaldo oContabilidadSaldo;

    private CClave oClave;
    private CComunes oComunes;
    private CContabilidad oContabilidad;
    private CContrato oContrato;
    private CContratoSub oContratoSub;

    private CMoneda oMoneda;
    private CPersonaFid oPersFid;

    private byte[] buffer;
    private Short archivoLineaNum;
    private String archivoLinea;
    private String archivoNombre;
    private File archivo;
    private FileInputStream fis;
    private FileOutputStream fos;

    //Cancela_Operaciones
    private boolean cancelaMesesAnt; 

    private List<ContabilidadPolizaManBean> consultaPoliza; //Para la carga masiva

    private List<String> listSubContratosNom = new ArrayList<>();
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @ManagedProperty(value = "#{beanCriterioBusqueda}")
    private volatile CriterioBusquedaBean cb;
    @ManagedProperty(value = "#{beanCriterioBusquedaConta}")
    private volatile CriterioBusquedaContaBean cbc;
    @ManagedProperty(value = "#{beanCriterioBusquedaContaAsien}")
    private volatile CriterioBusquedaContaAsienBean cba;
    @ManagedProperty(value = "#{beanCriterioBusquedaContaSaldo}")
    private volatile CriterioBusquedaContaSaldoBean cbs;
    @ManagedProperty(value = "#{beanContabilidadBienFide}")
    private ContabilidadBienFideBean bienFide;
    @ManagedProperty(value = "#{beanContabilidadBienFideUnidad}")
    private ContabilidadBienFideUnidadBean bienFideUnidad;
    @ManagedProperty(value = "#{beanContabilidadBienFideUnidadIndiv}")
    private ContabilidadBienFideUnidadIndivBean bienFideUnidadIndiv;
    @ManagedProperty(value = "#{beanContabilidadBienFideUnidadIndivMasivoBean}")
    private ContabilidadBienFideUnidadIndivMasivoBean bienFideUnidadIndivMasivo;
    @ManagedProperty(value = "#{beanContabilidadBienFideUnidadLiq}")
    private ContabilidadBienFideUnidadLiqBean bienFideUnidadLiq;
    @ManagedProperty(value = "#{beanContabilidadDetValor}")
    private ContabilidadDetValorBean dv;
    @ManagedProperty(value = "#{beanContabilidadCancelaSaldo}")
    private ContabilidadCancelaSaldoBean canSld;
    @ManagedProperty(value = "#{beanAdminContrato}")
    private AdminContratoBean contrato;
    @ManagedProperty(value = "#{beanContabilidadGarantiaGral}")
    private ContabilidadGarantiaGralBean garantia;
    @ManagedProperty(value = "#{beanContabilidadGarantiaBien}")
    private ContabilidadGarantiaBienBean garantiaBien;
    @ManagedProperty(value = "#{beanContabilidadGarantiaBienSalida}")
    private ContabilidadGarantiaBienSalidaBean garantiaBienSalida;
    @ManagedProperty(value = "#{beanContabilidadOperacion}")
    private ContabilidadOperacionBean operacion;
    @ManagedProperty(value = "#{beanContabilidadPolizaMan}")
    private ContabilidadPolizaManBean polizaMan;
    @ManagedProperty(value = "#{beanContabilidadTC}")
    private ContabilidadTipoCambBean tipoCamb;
    @ManagedProperty(value = "#{beanGenericoColor}")
    private GenericoColorBean geneColor;
    @ManagedProperty(value = "#{beanGenericoConsulta}")
    private GenericoConsultaBean geneCons;
    @ManagedProperty(value = "#{beanGenericoDesHabilitado}")
    private GenericoDesHabilitadoBean geneDes;
    @ManagedProperty(value = "#{beanGenericoFecha}")
    private GenericoFechaBean geneFecha;
    @ManagedProperty(value = "#{beanGenericoTitulo}")
    private GenericoTituloBean geneTitulo;
    @ManagedProperty(value = "#{beanGenericoVisible}")
    private GenericoVisibleBean geneVisible;
    @ManagedProperty(value = "#{beanMensajeConfirmacion}")
    private MensajeConfirmaBean mensajeConfrima;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   E X P U E S T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Double totalCargos;
    private Double totalAbonos;
    private List<ContabilidadAsientoBean> consultaAsiento;
    private List<ContabilidadBienFideBean> consultaBienFide;
    private List<ContabilidadBienFideUnidadBean> consultaBienFideU;
    private List<ContabilidadBienFideUnidadIndivBean> consultaBienFideUI;
    private List<ContabilidadDatoMovBean> consultaDatoMov;
    private List<ContabilidadGarantiaBienBean> consultaGarantiaBien;
    private List<ContabilidadGarantiaGralBean> consultaGarantia;
    private List<ContabilidadMovtoBean> consultaMovtos;
    private List<ContabilidadSaldoBean> consultaSaldo;
    private List<ContabilidadSaldoPromBean> consultaSaldoProm;
    private List<GenericoConsultaBean> consultaGen;
    private List<GenericoConsultaBean> consultaGenBienGar;
    private List<GenericoConsultaBean> consultaGenBienFid;
    private List<GenericoConsultaBean> consultaGenCtaChq;
    private List<GenericoConsultaBean> consultaGenCtoInv;
    private List<GenericoConsultaBean> consultaGenFianzas;
    private List<GenericoConsultaBean> consultaGenHonor;
    private List<GenericoConsultaBean> consultaGenInvPlazo;
    private List<GenericoConsultaBean> consultaGenInvDir;

    private List<GenericoConsultaBean> consultaGenBienGarP;
    private List<GenericoConsultaBean> consultaGenBienFidP;
    private List<GenericoConsultaBean> consultaGenCtaChqP;
    private List<GenericoConsultaBean> consultaGenCtoInvP;
    private List<GenericoConsultaBean> consultaGenFianzasP;
    private List<GenericoConsultaBean> consultaGenHonorP;
    private List<GenericoConsultaBean> consultaGenInvPlazoP;
    private List<GenericoConsultaBean> consultaGenInvDirP;

    private String monedaNomInvPlazo;

    private Double acumActivo, acumPatrim, acumInv, acumTotal;
    private Long numDias;

    private Double sumaInvPlazo;
    private Double sumaInvDir;
    private Double sumaBienGar;
    private Double sumaBienfid;
    private Double sumaCtaChq;
    private Double sumaCtoInv;
    private Double sumaFianzas;
    private Double sumaHonorarios;

    private List<String> listaBienFideTipo;
    private List<String> listaBienFideClas;
    private List<String> listaContrato;
    private List<String> listaErr;
    private List<String> listaFidPers;
    private List<String> listaFidRol;
    private List<String> listaGarStatus;
    private List<String> listaGarClasif;
    private List<String> listaGarBienStatus;
    private List<String> listaGarBienClasif;
    private List<String> listaGenerica;
    private List<String> listaIndivInmblStatus;
    private List<String> listaIndivInmblTipo;
    private List<String> listaIndivInmblTipoUnidad;
    private List<String> listaIndivUnidad;
    private List<String> listaMoneda;
    private List<String> listaMonedaGar;
    private List<String> listaMonedaGarBien;
    private List<String> listaNotarios;
    private Map<String, String> notarios;
    private List<String> listaPeriodicidad;
    private List<String> listaPoliza;
    private List<String> listaPolizaAporPatrim;
    private List<String> listaPolizaErr;
    private List<String> listaStatus;
    private List<String> listaStatusUnidades;
    private List<String> listaStatusUnidadesIndv;

    private ContabilidadBienFideBean seleccionaBienFide;
    private ContabilidadBienFideUnidadBean seleccionaBienFideU;
    private ContabilidadBienFideUnidadIndivBean seleccionaBienFideUI;
    private ContabilidadGarantiaBienBean seleccionaGarantiaBien;
    private ContabilidadGarantiaGralBean seleccionaGarantia;
    private ContabilidadMovtoBean seleccionaMovto;
    private ContabilidadSaldoBean seleccionaSaldo;

    private List<ContabilidadMovtoBean> seleccionaMovtoCancela;

    private LazyDataModel<ContabilidadMovtoBean> consultaMovtosLazy;
    private LazyDataModel<ContabilidadBienFideBean> getTrustProperty;
    private LazyDataModel<ContabilidadMovtoBean> movements;
    private LazyDataModel<ContabilidadAsientoBean> bookEntries;
    private LazyDataModel<ContabilidadSaldoBean> balanceInquiries;
    private LazyDataModel<ContabilidadSaldoPromBean> averageBalanceInquiries;
    private LazyDataModel<ContabilidadGarantiaGralBean> guarantees;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setCb(CriterioBusquedaBean cb) {
        this.cb = cb;
    }

    public void setCbc(CriterioBusquedaContaBean cbc) {
        this.cbc = cbc;
    }

    public void setCba(CriterioBusquedaContaAsienBean cba) {
        this.cba = cba;
    }

    public void setBienFide(ContabilidadBienFideBean bienFide) {
        this.bienFide = bienFide;
    }

    public void setBienFideUnidad(ContabilidadBienFideUnidadBean bienFideUnidad) {
        this.bienFideUnidad = bienFideUnidad;
    }

    public void setBienFideUnidadIndiv(ContabilidadBienFideUnidadIndivBean bienFideUnidadIndiv) {
        this.bienFideUnidadIndiv = bienFideUnidadIndiv;
    }

    public void setBienFideUnidadIndivMasivo(ContabilidadBienFideUnidadIndivMasivoBean bienFideUnidadIndivMasivo) {
        this.bienFideUnidadIndivMasivo = bienFideUnidadIndivMasivo;
    }

    public void setBienFideUnidadLiq(ContabilidadBienFideUnidadLiqBean bienFideUnidadLiq) {
        this.bienFideUnidadLiq = bienFideUnidadLiq;
    }

    public void setDv(ContabilidadDetValorBean dv) {
        this.dv = dv;
    }

    public void setCanSld(ContabilidadCancelaSaldoBean canSld) {
        this.canSld = canSld;
    }

    public void setContrato(AdminContratoBean contrato) {
        this.contrato = contrato;
    }

    public void setGarantia(ContabilidadGarantiaGralBean garantia) {
        this.garantia = garantia;
    }

    public void setGarantiaBien(ContabilidadGarantiaBienBean garantiaBien) {
        this.garantiaBien = garantiaBien;
    }

    public void setGarantiaBienSalida(ContabilidadGarantiaBienSalidaBean garantiaBienSalida) {
        this.garantiaBienSalida = garantiaBienSalida;
    }

    public void setOperacion(ContabilidadOperacionBean operacion) {
        this.operacion = operacion;
    }

    public void setPolizaMan(ContabilidadPolizaManBean polizaMan) {
        this.polizaMan = polizaMan;
    }

    public void setTipoCamb(ContabilidadTipoCambBean tipoCamb) {
        this.tipoCamb = tipoCamb;
    }

    public void setGeneColor(GenericoColorBean geneColor) {
        this.geneColor = geneColor;
    }

    public void setGeneCons(GenericoConsultaBean geneCons) {
        this.geneCons = geneCons;
    }

    public void setGeneDes(GenericoDesHabilitadoBean geneDes) {
        this.geneDes = geneDes;
    }

    public void setGeneFecha(GenericoFechaBean geneFecha) {
        this.geneFecha = geneFecha;
    }

    public void setGeneTitulo(GenericoTituloBean geneTitulo) {
        this.geneTitulo = geneTitulo;
    }

    public void setGeneVisible(GenericoVisibleBean geneVisible) {
        this.geneVisible = geneVisible;
    }

    public void setMensajeConfrima(MensajeConfirmaBean mensajeConfrima) {
        this.mensajeConfrima = mensajeConfrima;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Double getTotalCargos() {
        return totalCargos;
    }

    public Double getTotalAbonos() {
        return totalAbonos;
    }

    public List<ContabilidadAsientoBean> getConsultaAsiento() {
        return consultaAsiento;
    }

    public List<ContabilidadBienFideBean> getConsultaBienFide() {
        return consultaBienFide;
    }

    public LazyDataModel<ContabilidadBienFideBean> getGetTrustProperty() {
        return getTrustProperty;
    }

    public LazyDataModel<ContabilidadMovtoBean> getMovements() {
        return movements;
    }

    public LazyDataModel<ContabilidadAsientoBean> getBookEntries() {
        return bookEntries;
    }

    public LazyDataModel<ContabilidadSaldoBean> getBalanceInquiries() {
        return balanceInquiries;
    }

    public LazyDataModel<ContabilidadSaldoPromBean> getAverageBalanceInquiries() {
        return averageBalanceInquiries;
    }

    public LazyDataModel<ContabilidadGarantiaGralBean> getGuarantees() {
        return guarantees;
    }

    public List<ContabilidadBienFideUnidadBean> getConsultaBienFideU() {
        return consultaBienFideU;
    }

    public List<ContabilidadBienFideUnidadIndivBean> getConsultaBienFideUI() {
        return consultaBienFideUI;
    }

    public List<ContabilidadGarantiaBienBean> getConsultaGarantiaBien() {
        return consultaGarantiaBien;
    }

    public List<ContabilidadGarantiaGralBean> getConsultaGarantia() {
        return consultaGarantia;
    }

    public List<ContabilidadMovtoBean> getConsultaMovtos() {
        return consultaMovtos;
    }

    public List<ContabilidadSaldoBean> getConsultaSaldo() {
        return consultaSaldo;
    }

    public List<ContabilidadSaldoPromBean> getConsultaSaldoProm() {
        return consultaSaldoProm;
    }

    public List<GenericoConsultaBean> getConsultaGen() {
        return consultaGen;
    }

    public List<GenericoConsultaBean> getConsultaGenBienGar() {
        return consultaGenBienGar;
    }

    public List<GenericoConsultaBean> getConsultaGenBienFid() {
        return consultaGenBienFid;
    }

    public List<GenericoConsultaBean> getConsultaGenCtaChq() {
        return consultaGenCtaChq;
    }

    public List<GenericoConsultaBean> getConsultaGenCtoInv() {
        return consultaGenCtoInv;
    }

    public List<GenericoConsultaBean> getConsultaGenFianzas() {
        return consultaGenFianzas;
    }

    public List<GenericoConsultaBean> getConsultaGenHonor() {
        return consultaGenHonor;
    }

    public List<GenericoConsultaBean> getConsultaGenInvPlazo() {
        return consultaGenInvPlazo;
    }

    public List<GenericoConsultaBean> getConsultaGenInvDir() {
        return consultaGenInvDir;
    }

    public List<String> getListaBienFideTipo() {
        return listaBienFideTipo;
    }

    public List<String> getListaBienFideClas() {
        return listaBienFideClas;
    }

    public List<String> getListaContrato() {
        return listaContrato;
    }

    public List<String> getListaErr() {
        return listaErr;
    }

    public List<String> getListaFidPers() {
        return listaFidPers;
    }

    public List<String> getListaFidRol() {
        return listaFidRol;
    }

    public List<String> getListaGarStatus() {
        return listaGarStatus;
    }

    public List<String> getListaGarClasif() {
        return listaGarClasif;
    }

    public List<String> getListaGenerica() {
        return listaGenerica;
    }

    public List<String> getListaIndivInmblStatus() {
        return listaIndivInmblStatus;
    }

    public List<String> getListaIndivInmblTipo() {
        return listaIndivInmblTipo;
    }

    public List<String> getListaIndivInmblTipoUnidad() {
        return listaIndivInmblTipoUnidad;
    }

    public List<String> getListaIndivUnidad() {
        return listaIndivUnidad;
    }

    public List<String> getListaMoneda() {
        return listaMoneda;
    }

    public List<String> getListaNotarios() {
        return listaNotarios;
    }

    public Map<String, String> getNotarios() {
        return notarios;
    }

    public void setNotarios(Map<String, String> notarios) {
        this.notarios = notarios;
    }

    public List<String> getListaPeriodicidad() {
        return listaPeriodicidad;
    }

    public List<String> getListaPoliza() {
        return listaPoliza;
    }

    public List<String> getListaPolizaAporPatrim() {
        return listaPolizaAporPatrim;
    }

    public List<String> getListaPolizaErr() {
        return listaPolizaErr;
    }

    public List<String> getListaStatus() {
        return listaStatus;
    }

    public LazyDataModel<ContabilidadMovtoBean> getConsultaMovtosLazy() {
        return consultaMovtosLazy;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public ContabilidadBienFideBean getSeleccionaBienFide() {
        return seleccionaBienFide;
    }

    public void setSeleccionaBienFide(ContabilidadBienFideBean seleccionaBienFide) {
        this.seleccionaBienFide = seleccionaBienFide;
    }

    public ContabilidadBienFideUnidadBean getSeleccionaBienFideU() {
        return seleccionaBienFideU;
    }

    public void setSeleccionaBienFideU(ContabilidadBienFideUnidadBean seleccionaBienFideU) {
        this.seleccionaBienFideU = seleccionaBienFideU;
    }

    public ContabilidadBienFideUnidadIndivBean getSeleccionaBienFideUI() {
        return seleccionaBienFideUI;
    }

    public void setSeleccionaBienFideUI(ContabilidadBienFideUnidadIndivBean seleccionaBienFideUI) {
        this.seleccionaBienFideUI = seleccionaBienFideUI;
    }

    public ContabilidadGarantiaBienBean getSeleccionaGarantiaBien() {
        return seleccionaGarantiaBien;
    }

    public void setSeleccionaGarantiaBien(ContabilidadGarantiaBienBean seleccionaGarantiaBien) {
        this.seleccionaGarantiaBien = seleccionaGarantiaBien;
    }

    public ContabilidadGarantiaGralBean getSeleccionaGarantia() {
        return seleccionaGarantia;
    }

    public void setSeleccionaGarantia(ContabilidadGarantiaGralBean seleccionaGarantia) {
        this.seleccionaGarantia = seleccionaGarantia;
    }

    public ContabilidadMovtoBean getSeleccionaMovto() {
        return seleccionaMovto;
    }

    public void setSeleccionaMovto(ContabilidadMovtoBean seleccionaMovto) {
        this.seleccionaMovto = seleccionaMovto;
    }

    public ContabilidadSaldoBean getSeleccionaSaldo() {
        return seleccionaSaldo;
    }

    public void setSeleccionaSaldo(ContabilidadSaldoBean seleccionaSaldo) {
        this.seleccionaSaldo = seleccionaSaldo;
    }

    public void setCbs(CriterioBusquedaContaSaldoBean cbs) {
        this.cbs = cbs;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadMovtoBean> getSeleccionaMovtoCancela() {
        return seleccionaMovtoCancela;
    }

    public void setSeleccionaMovtoCancela(List<ContabilidadMovtoBean> seleccionaMovtoCancela) {
        this.seleccionaMovtoCancela = seleccionaMovtoCancela;
    }

    private String name;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + "]";
    }

    public Double getSumaInvPlazo() {
        return sumaInvPlazo;
    }

    public void setSumaInvPlazo(Double sumaInvPlazo) {
        this.sumaInvPlazo = sumaInvPlazo;
    }

    public Double getSumaInvDir() {
        return sumaInvDir;
    }

    public void setSumaInvDir(Double sumaInvDir) {
        this.sumaInvDir = sumaInvDir;
    }

    public Double getSumaBienGar() {
        return sumaBienGar;
    }

    public void setSumaBienGar(Double sumaBienGar) {
        this.sumaBienGar = sumaBienGar;
    }

    public Double getSumaBienfid() {
        return sumaBienfid;
    }

    public void setSumaBienfid(Double sumaBienfid) {
        this.sumaBienfid = sumaBienfid;
    }

    public Double getSumaCtaChq() {
        return sumaCtaChq;
    }

    public void setSumaCtaChq(Double sumaCtaChq) {
        this.sumaCtaChq = sumaCtaChq;
    }

    public Double getSumaCtoInv() {
        return sumaCtoInv;
    }

    public void setSumaCtoInv(Double sumaCtoInv) {
        this.sumaCtoInv = sumaCtoInv;
    }

    public Double getSumaFianzas() {
        return sumaFianzas;
    }

    public void setSumaFianzas(Double sumaFianzas) {
        this.sumaFianzas = sumaFianzas;
    }

    public Double getSumaHonorarios() {
        return sumaHonorarios;
    }

    public void setSumaHonorarios(Double sumaHonorarios) {
        this.sumaHonorarios = sumaHonorarios;
    }

    public String getBienImporteTxt() {
        return BienImporteTxt;
    }

    public void setBienImporteTxt(String BienImporteTxt) {
        this.BienImporteTxt = BienImporteTxt;
    }

    public String getBienTipoCambiTxt() {
        return BienTipoCambiTxt;
    }

    public void setBienTipoCambiTxt(String BienTipoCambiTxt) {
        this.BienTipoCambiTxt = BienTipoCambiTxt;
    }

    public String getBienImporteNvoTxt() {
        return BienImporteNvoTxt;
    }

    public void setBienImporteNvoTxt(String BienImporteNvoTxt) {
        this.BienImporteNvoTxt = BienImporteNvoTxt;
    }

    public String getBienSubFisoTxt() {
        return BienSubFisoTxt;
    }

    public void setBienSubFisoTxt(String BienSubFisoTxt) {
        this.BienSubFisoTxt = BienSubFisoTxt;
    }

    public String getCancelaSaldoFidecomisoTxt() {
        return CancelaSaldoFidecomisoTxt;
    }

    public void setCancelaSaldoFidecomisoTxt(String CancelaSaldoFidecomisoTxt) {
        this.CancelaSaldoFidecomisoTxt = CancelaSaldoFidecomisoTxt;
    }

    public List<String> getListaStatusUnidades() {
        return listaStatusUnidades;
    }

    public void setListaStatusUnidades(List<String> listaStatusUnidades) {
        this.listaStatusUnidades = listaStatusUnidades;
    }

    public List<String> getListaStatusUnidadesIndv() {
        return listaStatusUnidadesIndv;
    }

    public void setListaStatusUnidadesIndv(List<String> listaStatusUnidadesIndv) {
        this.listaStatusUnidadesIndv = listaStatusUnidadesIndv;
    }

    public Boolean getSecHonorariosPV() {
        return secHonorariosPV;
    }

    public void setSecHonorariosPV(Boolean secHonorariosPV) {
        this.secHonorariosPV = secHonorariosPV;
    }

    public Boolean getSecCtasResultadosPV() {
        return secCtasResultadosPV;
    }

    public void setSecCtasResultadosPV(Boolean secCtasResultadosPV) {
        this.secCtasResultadosPV = secCtasResultadosPV;
    }

    public Boolean getSecBienesPV() {
        return secBienesPV;
    }

    public void setSecBienesPV(Boolean secBienesPV) {
        this.secBienesPV = secBienesPV;
    }

    public Boolean getSecInversionesPV() {
        return secInversionesPV;
    }

    public void setSecInversionesPV(Boolean secInversionesPV) {
        this.secInversionesPV = secInversionesPV;
    }

    public Boolean getSecBancosPV() {
        return secBancosPV;
    }

    public void setSecBancosPV(Boolean secBancosPV) {
        this.secBancosPV = secBancosPV;
    }

    public Boolean getSecCtasBalancePV() {
        return secCtasBalancePV;
    }

    public void setSecCtasBalancePV(Boolean secCtasBalancePV) {
        this.secCtasBalancePV = secCtasBalancePV;
    }

    public Boolean getSecCtasOrdenPV() {
        return secCtasOrdenPV;
    }

    public void setSecCtasOrdenPV(Boolean secCtasOrdenPV) {
        this.secCtasOrdenPV = secCtasOrdenPV;
    }

    public Double getAcumActivo() {
        return acumActivo;
    }

    public Double getAcumPatrim() {
        return acumPatrim;
    }

    public Double getAcumInv() {
        return acumInv;
    }

    public Double getAcumTotal() {
        return acumTotal;
    }

    public Long getNumDias() {
        return numDias;
    }

    public void setNumDias(Long numDias) {
        this.numDias = numDias;
    }

    public void setAcumActivo(Double acumActivo) {
        this.acumActivo = acumActivo;
    }

    public void setAcumPatrim(Double acumPatrim) {
        this.acumPatrim = acumPatrim;
    }

    public void setAcumInv(Double acumInv) {
        this.acumInv = acumInv;
    }

    public void setAcumTotal(Double acumTotal) {
        this.acumTotal = acumTotal;
    }

    public List<String> getListaGarBienStatus() {
        return listaGarBienStatus;
    }

    public void setListaGarBienStatus(List<String> listaGarBienStatus) {
        this.listaGarBienStatus = listaGarBienStatus;
    }

    public List<String> getListaGarBienClasif() {
        return listaGarBienClasif;
    }

    public void setListaGarBienClasif(List<String> listaGarBienClasif) {
        this.listaGarBienClasif = listaGarBienClasif;
    }

    public List<String> getListaMonedaGar() {
        return listaMonedaGar;
    }

    public void setListaMonedaGar(List<String> listaMonedaGar) {
        this.listaMonedaGar = listaMonedaGar;
    }

    public List<String> getListaMonedaGarBien() {
        return listaMonedaGarBien;
    }

    public void setListaMonedaGarBien(List<String> listaMonedaGarBien) {
        this.listaMonedaGarBien = listaMonedaGarBien;
    }

    public Boolean getTcVisible() {
        return tcVisible;
    }

    public void setTcVisible(Boolean tcVisible) {
        this.tcVisible = tcVisible;
    }
  
    //Cancelación de Operaciones CAVC
    public boolean isCancelaMesesAnt() {
        return cancelaMesesAnt;
    }

    public void setCancelaMesesAnt(boolean cancelaMesesAnt) {
        this.cancelaMesesAnt = cancelaMesesAnt;
    } 

    public Boolean getPermisoEscrituraPolizaAdm() {
        return permisoEscrituraPolizaAdm;
    }

    public void setPermisoEscrituraPolizaAdm(Boolean permisoEscrituraPolizaAdm) {
        this.permisoEscrituraPolizaAdm = permisoEscrituraPolizaAdm;
    }

    public List<String> getListSubContratosNom() {
        return listSubContratosNom;
    }

    public void setListSubContratosNom(List<String> listSubContratosNom) {
        this.listSubContratosNom = listSubContratosNom;
    }

    public Map<Integer, String> getListaContratoSub() {
        return listaContratoSub;
    }

    public void setListaContratoSub(Map<Integer, String> listaContratoSub) {
        this.listaContratoSub = listaContratoSub;
    }   

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C L A S E S   D E   U S O   I N T E R N O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    class contabilidadGrales {

        public contabilidadGrales() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadGrales.";
            validacion = Boolean.TRUE;
        }

        public void onContabilidadGrales_AplicaConfirmacion() {
            try {
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("bienFide")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("salida")) {
                        bienFide.setBienFideBitTipoOperacion("SALIDA");
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFide_Administra(bienFide).equals(Boolean.TRUE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            RequestContext.getCurrentInstance().execute("dlgBienFide.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        cbc.setCriterioAX1(bienFide.getBienFideContratoNum());
                        cbc.setTxtCriterioAX1(bienFide.getBienFideContratoNum().toString());
//                            consultaBienFide = oContabilidad.onBienFide_Consulta(cbc);
                        usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                        cbc.setCriterioUsuario(usuarioNumero);
                        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmBienFide:dtBienFide");
                        dataTable.setFirst(0);

                        oContabilidad = new CContabilidad();
                        int totalRows = oContabilidad.getTrustPropertyTotalRows(cbc);
                        oContabilidad = null;

                        ContabilidadBienFideBean bean = new ContabilidadBienFideBean();
                        MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName());
                        getTrustProperty = lazyModel.getLazyDataModel(cbc);
                        getTrustProperty.setRowCount(totalRows);
                        oContabilidad = null;
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("Garantia")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("ELIMINAR")) {
                        garantia.setGarantiaBitTipoOper(mensajeConfrima.getMensajeConfirmacionAccion());
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onGarantia_Administra(garantia).equals(Boolean.TRUE)) {
                            cbc.setCriterioAX1(garantia.getGarantiaContratoNumero());
                            cbc.setTxtCriterioAX1(garantia.getGarantiaContratoNumero().toString());
                            seleccionaGarantia = null;
                            consultaGarantia = oContabilidad.onGarantia_Consulta(cbc);
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            RequestContext.getCurrentInstance().execute("dlgGarantia.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("GarantiaBien")) {
                    if ((mensajeConfrima.getMensajeConfirmacionAccion().equals("REVALUACION")) || (mensajeConfrima.getMensajeConfirmacionAccion().equals("SALIDA"))) {
                        if (mensajeConfrima.getMensajeConfirmacionAccion().equals("REVALUACION")) {
                            garantiaBien.setGarantiaBienImporte(garantiaBienSalida.getGarantiaBienSalidaImporteNvo());
                            garantiaBien.setGarantiaBienFechaPrxVal(new java.sql.Date(geneFecha.getGenericoFecha04().getTime()));
                        }
                        garantiaBien.setGarantiaBitTipoOper(mensajeConfrima.getMensajeConfirmacionAccion());
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onGarantia_AdministraBien(garantiaBien).equals(Boolean.TRUE)) {
                            cbc.setCriterioAX1(garantiaBien.getGarantiaContratoNumero());
                            cbc.setTxtCriterioAX1(garantiaBien.getGarantiaContratoNumero().toString());
//                            consultaGarantiaBien = null;

//                            seleccionaGarantia = null;
                            seleccionaGarantiaBien = null;
                            consultaGarantia = oContabilidad.onGarantia_Consulta(cbc);
                            //consultaGarantiaBien   = oContabilidad.onGarantia_ConsultaBien(garantia.getGarantiaContratoNumero(), garantia.getGarantiaContratoNumeroSub());
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            RequestContext.getCurrentInstance().execute("dlgGarBien.hide();");
                            RequestContext.getCurrentInstance().execute("dlgRevaluacion.hide()");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        consultaGarantiaBien = oContabilidad.onGarantia_ConsultaBien(garantiaBien.getGarantiaContratoNumero(), garantiaBien.getGarantiaContratoNumeroSub());
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("bienFideUni")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("ELIMINAR")) {
                        bienFideUnidad.setBienFideBitTipoOperacion(mensajeConfrima.getMensajeConfirmacionAccion());
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_AdministraUnidad(bienFideUnidad).equals(Boolean.TRUE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            consultaBienFideU = oContabilidad.onBienFideIndiv_ConsultaUnidad(bienFideUnidad.getBienFideContratoNum(),
                                    bienFideUnidad.getBienFideContratoNumSub(),
                                    bienFideUnidad.getBienFideTipo(),
                                    bienFideUnidad.getBienFideId());
//                            seleccionaBienFide = null;
                            seleccionaBienFideU = null;
                            RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("bienFideUniIndiv")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("ELIMINAR")) {
                        bienFideUnidadIndiv.setBienFideBitTipoOperacion(mensajeConfrima.getMensajeConfirmacionAccion());
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_AdministraUnidadIndiv(bienFideUnidadIndiv).equals(Boolean.TRUE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidadIndiv.getBienFideContratoNum(),
                                    bienFideUnidadIndiv.getBienFideContratoNumSub(),
                                    bienFideUnidadIndiv.getBienFideTipo(),
                                    bienFideUnidadIndiv.getBienFideId(),
                                    bienFideUnidad.getBienFideUnidadSec());
                            RequestContext.getCurrentInstance().execute("dlgBienFideIndivUni.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        oContabilidad = null;
                    }
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("LIQUIDAR")) {
                        bienFideUnidadLiq.setBienFideBitTipoOperacion(mensajeConfrima.getMensajeConfirmacionAccion());
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_AdministraUnidadLiq(bienFideUnidadLiq).equals(Boolean.TRUE)) {
                            seleccionaBienFideUI = null;
                            consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidadLiq.getBienFideContratoNum(),
                                    bienFideUnidadLiq.getBienFideContratoNumSub(),
                                    bienFideUnidadLiq.getBienFideTipo(),
                                    bienFideUnidadIndiv.getBienFideId(),
                                    bienFideUnidadLiq.getBienFideUnidadSec());

                            consultaBienFideU = oContabilidad.onBienFideIndiv_ConsultaUnidad(bienFideUnidadLiq.getBienFideContratoNum(),
                                    bienFideUnidadLiq.getBienFideContratoNumSub(),
                                    bienFideUnidadLiq.getBienFideTipo(),
                                    bienFideUnidadLiq.getBienFideId());
                            for (int item = 0; item <= consultaBienFideU.size() - 1; item++) {
                                if (consultaBienFideU.get(item).getBienFideUnidadSec().equals(bienFideUnidadLiq.getBienFideUnidadSec())) {
                                    bienFideUnidad.setBienFideUnidadDomicilio(consultaBienFideU.get(item).getBienFideUnidadDomicilio());
                                    bienFideUnidad.setBienFideUnidadEscrituraIni(consultaBienFideU.get(item).getBienFideUnidadEscrituraIni());
                                    bienFideUnidad.setBienFideUnidadFechaCto(consultaBienFideU.get(item).getBienFideUnidadFechaCto());
                                    bienFideUnidad.setBienFideUnidadMonedaNom(consultaBienFideU.get(item).getBienFideUnidadMonedaNom());
                                    bienFideUnidad.setBienFideUnidadMonedaNum(consultaBienFideU.get(item).getBienFideUnidadMonedaNum());
                                    bienFideUnidad.setBienFideUnidadNombre(consultaBienFideU.get(item).getBienFideUnidadNombre());
                                    bienFideUnidad.setBienFideUnidadNotarioLocalidad(consultaBienFideU.get(item).getBienFideUnidadNotarioLocalidad());
                                    bienFideUnidad.setBienFideUnidadNotarioNom(consultaBienFideU.get(item).getBienFideUnidadNotarioNom());
                                    bienFideUnidad.setBienFideUnidadNotarioNum(consultaBienFideU.get(item).getBienFideUnidadNotarioNum());
                                    bienFideUnidad.setBienFideUnidadNotarioNumOFicial(consultaBienFideU.get(item).getBienFideUnidadNotarioNumOFicial());
                                    bienFideUnidad.setBienFideUnidadNotarioLista(consultaBienFideU.get(item).getBienFideUnidadNotarioLista());
                                    bienFideUnidad.setBienFideUnidadObservacion(consultaBienFideU.get(item).getBienFideUnidadObservacion());
                                    bienFideUnidad.setBienFideUnidadSec(consultaBienFideU.get(item).getBienFideUnidadSec());
                                    bienFideUnidad.setBienFideUnidadStatus(consultaBienFideU.get(item).getBienFideUnidadStatus());
                                    bienFideUnidad.setBienFideUnidadSuperficie(consultaBienFideU.get(item).getBienFideUnidadSuperficie());
                                    bienFideUnidad.setTxtBienFideUnidadSuperficie(formatDecimal2D(consultaBienFideU.get(item).getBienFideUnidadSuperficie()));
                                    bienFideUnidad.setBienFideUnidadTipo(consultaBienFideU.get(item).getBienFideUnidadTipo());
                                    bienFideUnidad.setBienFideUnidadValor(consultaBienFideU.get(item).getBienFideUnidadValor());
                                    bienFideUnidad.setTxtBienFideUnidadValor(formatImporte2D(consultaBienFideU.get(item).getBienFideUnidadValor()));
                                    bienFideUnidad.setBienFideUnidadAcumRegContable(consultaBienFideU.get(item).getBienFideUnidadAcumRegContable());

                                    consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidadLiq.getBienFideContratoNum(),
                                            bienFideUnidadLiq.getBienFideContratoNumSub(),
                                            bienFideUnidadLiq.getBienFideTipo(),
                                            bienFideUnidadLiq.getBienFideId(),
                                            bienFideUnidadLiq.getBienFideUnidadSec());
                                    bienFideUnidad.setBienFideUnidadAcumRegContable(oContabilidad.getBienFideUnidadAcumRegContable());

                                    break;
                                }
                            }

                            RequestContext.getCurrentInstance().execute("dlgBienFideIndivUni.hide();");
//                            RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.show();"); 
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("cancelaOperacion")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("ELIMINAR")) {
                        oContabilidad = new CContabilidad();
                        listaGenerica = new ArrayList<>();
   
                        for (int itemPol = 0; itemPol <= seleccionaMovtoCancela.size() - 1; itemPol++) {
                            polizaMan.setPolizaEncaFolio(seleccionaMovtoCancela.get(itemPol).getMovtoFolio());

                            //Cancela_meses_anteriores 
                            if (cancelaMesesAnt == true) {
                                polizaMan.setPoliza00Valor("1");
                            } else {
                                polizaMan.setPoliza00Valor("0");
                            } 
                              
                            if (oContabilidad.onContabilidad_EjecutaCancela(polizaMan)) { 
                                listaGenerica.add("Folio: ".concat(polizaMan.getPolizaEncaFolio().toString()).concat(".- Correcto"));
                            } else {
                                listaGenerica.add("Folio: ".concat(polizaMan.getPolizaEncaFolio().toString()).concat(". ").concat(oContabilidad.getMensajeError()));
                            }  
                        }
                        cbc.setCriterioFolioIni(seleccionaMovtoCancela.get(0).getMovtoFolio());
                        cbc.setTxtCriterioFolioIni(cbc.getCriterioFolioIni().toString());
                        //cbc.setCriterioFolioFin(seleccionaMovtoCancela.get(seleccionaMovtoCancela.size() - 1).getMovtoFolio());
                        cbc.setCriterioCveMovtoCancelado("SI");
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación finalizada. \nVerifique sus resultados");
                        consultaMovtos = oContabilidad.onCancelaOper_ConsultaMovtos(cbc);
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("cancelaSaldos")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("APLICA MOVTOS")) {
                        canSld.setCsContratoNum(cb.getCriterioContratoNumero());
                        canSld.setCsOpcion(geneTitulo.getGenericoTitulo00());
                        canSld.setCsOperacion(mensajeConfrima.getMensajeConfirmacionAccion());
                        canSld.setCsFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                        canSld.setCsBitPantalla("CANCELACIÓN DE SALDOS");
                        canSld.setCsBitTerminal(usuarioTerminal);
                        canSld.setCsBitUsuario(usuarioNumero);
                        oContabilidad = new CContabilidad();
                        if (!consultaGen.isEmpty()) {
                            if (oContabilidad.onCancelaSaldo_Ejecuta(canSld).equals(Boolean.TRUE)) {

                                if (geneTitulo.getGenericoTitulo00().equals("CTAS HONORARIOS")) {
                                    aplHonorarios = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS RESULTADO")) {
                                    aplCtasResultados = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS BIENES")) {
                                    aplBienes = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS INVERSION")) {
                                    aplInversiones = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS BANCOS")) {
                                    aplBancos = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS BALANCE")) {
                                    aplCtasBalance = true;
                                }
                                if (geneTitulo.getGenericoTitulo00().equals("CTAS ORDEN")) {
                                    aplCtasOrden = true;
                                }
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                                consultaGen = oContabilidad.onCancelaSaldo_Consulta_Tipmovto(cb.getCriterioContratoNumero(), "SIMULADO", geneTitulo.getGenericoTitulo00());
//                          consultaGen = oContabilidad.onCancelaSaldo_Consulta(cb.getCriterioContratoNumero(), "SIMULADO");
                            } else {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                            }
                        } else {
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS HONORARIOS")) {
                                aplHonorarios = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS RESULTADO")) {
                                aplCtasResultados = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS BIENES")) {
                                aplBienes = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS INVERSION")) {
                                aplInversiones = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS BANCOS")) {
                                aplBancos = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS BALANCE")) {
                                aplCtasBalance = true;
                            }
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS ORDEN")) {
                                aplCtasOrden = true;
                            }
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");

                        }
                        botonAplica = true;
                        oContabilidad = null;
                    }
                }
                if (mensajeConfrima.getMensajeConfirmaOrigen().equals("polizaMan")) {
                    if (mensajeConfrima.getMensajeConfirmacionAccion().equals("polizaAplica")) {
                        RequestContext.getCurrentInstance().execute("dlgPolizaMtto.hide();");
                    }
                }
                RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
            } catch (SQLException Err) {
                logger.error("onContabilidadGrales_AplicaConfirmacion()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGrales_ObtenContratoNombre() {
            try {
                if (cb.getCriterioContratoNumero() != null) {
                    oContrato = new CContrato();
                    cb.setCriterioContratoNombre(oContrato.onContrato_ObtenNombre(cb.getCriterioContratoNumero()));
                    oContrato = null;
                }
            } catch (SQLException Err) {
                logger.error("onContabilidadGrales_ObtenContratoNombre()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGrales_SeleccionaFicha(String fichaTitulo) {
            try {
                oMoneda = new CMoneda();
                listaMoneda = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                oMoneda = null;
                consultaAsiento = null;
                consultaMovtos = null;
                consultaSaldo = null;
                seleccionaMovto = null;
                seleccionaSaldo = null;

                movements = null;
                consultaMovtos = null;
                seleccionaMovto = null;
                consultaAsiento = null;
                bookEntries = null;
                consultaSaldo = null;
                balanceInquiries = null;
                seleccionaSaldo = null;
                consultaSaldoProm = null;
                averageBalanceInquiries = null;

                if (fichaTitulo.contains("Movimientos")) {
                    cbc.setCriterioAX1(null);
                    cbc.setCriterioAX2(null);
                    cbc.setCriterioFolio(null);
                    cbc.setCriterioTransId(null);
                    cbc.setCriterioTipoFecha("FN");
                    cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                    cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                    cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                    cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
                    cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                    cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());

                    cbc.setCriterioPlaza(null);
                    cbc.setCriterioCveMovtoCancelado("NO");

                    cbc.setTxtCriterioCTAM(null);
                    cbc.setTxtCriterioSC1(null);
                    cbc.setTxtCriterioSC2(null);
                    cbc.setTxtCriterioSC3(null);
                    cbc.setTxtCriterioSC4(null);
                    cbc.setTxtCriterioAX1(null);
                    cbc.setTxtCriterioAX2(null);
                    cbc.setTxtCriterioAX3(null);
                    cbc.setTxtCriterioFolio(null);
                    cbc.setTxtCriterioFolioIni(null);
                    cbc.setTxtCriterioFolioFin(null);
                    cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
                    cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                    cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
                    cbc.setTxtCriterioTransId(null);
                    cbc.setTxtCriterioPlaza(null);
                    cbc.setTxtCriterioUsuario(null);
                }
                if (fichaTitulo.contains("Asientos")) {
                    consultaAsiento = null;
                    cba.setCriterioCTAM(null);
                    cba.setCriterioSC1(null);
                    cba.setCriterioSC2(null);
                    cba.setCriterioSC3(null);
                    cba.setCriterioSC4(null);
                    cba.setCriterioAX1(null);
                    cba.setCriterioAX2(null);
                    cba.setCriterioAX3(null);
                    cba.setCriterioFolio(null);
                    cba.setCriterioMonedaNom(null);
                    cba.setCriterioMonedaNum(null);
                    cba.setCriterioPlaza(null);
                    cba.setCriterioFechaTipo("FN");
                    cba.setCriterioFechaDD(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                    cba.setCriterioFechaMM(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                    cba.setCriterioFechaYY(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                    cba.setCriterioStatus("ACTIVO");
                    cba.setCriterioMuestraTotalCarAbo(Boolean.FALSE);
                    geneVisible.setGenericoVisible00("hidden;");
                    cba.setTxtCriterioAX1(null);
                    cba.setTxtCriterioAX2(null);
                    cba.setTxtCriterioAX3(null);
                    cba.setTxtCriterioCTAM(null);
                    cba.setTxtCriterioSC1(null);
                    cba.setTxtCriterioSC2(null);
                    cba.setTxtCriterioSC3(null);
                    cba.setTxtCriterioSC4(null);
                    cba.setTxtCriterioFolio(null);
                    cba.setTxtCriterioFechaDD(cba.getCriterioFechaDD().toString());
                    cba.setTxtCriterioFechaMM(cba.getCriterioFechaMM().toString());
                    cba.setTxtCriterioFechaYY(cba.getCriterioFechaYY().toString());
                    cba.setTxtCriterioPlaza(null);
                    cba.setTxtCriterioMonedaNum(null);
                    cba.setTxtCriterioUsuario(null);
                    // CAVC Se agrega limpieza a campo Descripción
                    cba.setCriterioDescripcion(null);
                }
                if (fichaTitulo.equals("Consulta de Saldos")) {
                    cbs.setCriterioAX1(null);
                    cbs.setCriterioAX2(null);
                    cbs.setCriterioAX3(null);
                    cbs.setCriterioCTAM(null);
                    cbs.setCriterioSC1(null);
                    cbs.setCriterioSC2(null);
                    cbs.setCriterioSC3(null);
                    cbs.setCriterioSC4(null);
                    cbs.setCriterioAño(null);
                    cbs.setCriterioMes(null);
                    cbs.setCriterioTipo("ACT");
                    cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                    cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));

                    cbs.setCriterioMonedaNom("MONEDA NACIONAL");
                    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);

                    cbs.setTxtCriterioAX1(null);
                    cbs.setTxtCriterioAX2(null);
                    cbs.setTxtCriterioAX3(null);
                    cbs.setTxtCriterioCTAM(null);
                    cbs.setTxtCriterioSC1(null);
                    cbs.setTxtCriterioSC2(null);
                    cbs.setTxtCriterioSC3(null);
                    cbs.setTxtCriterioSC4(null);
                    cbs.setTxtCriterioUsuario(null);
                    cbs.setTxtCriterioMes(cbs.getCriterioMes().toString());
                    cbs.setTxtCriterioAño(cbs.getCriterioAño().toString());
                    cbs.setCriterioNomContrato(null);
                }
                if (fichaTitulo.equals("Consulta de Saldos Promedio")) {
                    cb.setCriterioContratoNumero(null);
                    cb.setCriterioContratoNumeroSub(null);
                    cb.setCriterioFechaDel(null);
                    cb.setCriterioFechaAl(null);
                    cb.setTxtCriterioContratoNumero(null);
                    cb.setTxtCriterioContratoNumeroSub(null);
                    geneFecha.setGenericoFecha00(null);
                    geneFecha.setGenericoFecha01(null);
                    setAcumActivo(null);
                    setAcumInv(null);
                    setAcumPatrim(null);
                    setAcumTotal(null);
                    setNumDias(null);

                }
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadGrales_SeleccionaFicha()");
            } finally {
                onFinalizaObjetos();
            }
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oComunes != null) {
                oComunes = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }

        private Boolean onValidaNumerico(String sEntrada, String campo, String tipo) {
            Boolean bSalida = false;
            try {

                if ("I".equals(tipo)) {
                    Integer.parseInt(sEntrada);
                    bSalida = true;
                }
                if ("L".equals(tipo)) {
                    Long.parseLong(sEntrada);
                    bSalida = true;
                }
                if ("D".equals(tipo)) {
                    validarDouble(sEntrada);
                    if (validarDouble(sEntrada) > VALOR_MAX) {
                       
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El " + campo + " no puede ser mayor a 999,999,999,999.99...");
                    } else {
                        bSalida = true;
                    }
                }
                if ("S".equals(tipo)) {
                    Short.parseShort(sEntrada);
                    bSalida = true;
                }
            } catch (NumberFormatException Err) {
                if (campo.equals("Plaza") || campo.equals("Trans")) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La " + campo + " debe ser un campo numérico...");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El " + campo + " debe ser un campo numérico...");
                }
                
            }

            return bSalida;
        }

    }

    class contabilidadGarantia {

        public void onContabilidadGarantia_RegistroVerificaSubFiso() {
            String[] SubFisoSeleccion = garantia.getTxtGarantiaContratoNumeroSub().split(".-");
            String esSubFiso = SubFisoSeleccion[0];
            garantia.setGarantiaContratoNumeroSub(Integer.parseInt(esSubFiso.trim()));
            garantia.setGarantiaImporteCredito(null);
            garantia.setGarantiaImporteGarantia(0.00);
            garantia.setGarantiaImporteUltVal(0.00);
            garantia.setGarantiaMonedaNombre(null);
            garantia.setGarantiaMonedaNumero(null);
            garantia.setGarantiaRevaluaPeriodo(null);
            garantia.setGarantiaRelacionCredGar(null);
            garantia.setGarantiaTipoInmuebles("0");
            garantia.setGarantiaTipoMuebles("0");
            garantia.setGarantiaTipoNumerario("0");
            garantia.setGarantiaTipoOtros("0");
            garantia.setGarantiaTipoValores("0");
            garantia.setGarantiaTipo(null);
            garantia.setTxtGarantiaImporteCredito(null);
            garantia.setGarantiaFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);
            garantia.setGarantiaRevaluaFechaIni(null);
            garantia.setGarantiaRevaluaFechaFin(null);
            garantia.setGarantiaDesc(null);
            garantia.setGarantiaComentario(null);
            garantia.setGarantiaStatus(null);
            garantia.setGarantiaBitTipoOper("REGISTRO");
            garantia.setGarantiaBitUsuario(usuarioNumero);
            garantia.setGarantiaBitPantalla("contabilidadGarantiaGral");
            garantia.setGarantiaBitIP(usuarioTerminal);
        }

        public contabilidadGarantia() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadGarantia.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadGarantia_Consulta() {
            try {
                consultaGarantiaBien = null;
                consultaGarantia = null;
                seleccionaGarantiaBien = null;
                seleccionaGarantia = null;
                guarantees = null;
                /*if ((cbc.getCriterioAX1()==null)&&(validacion.equals(Boolean.TRUE))){
                    validacion = Boolean.FALSE;
                    mensaje    = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Contrato no puede estar vacío");
                }*/
                consultaGarantiaBien = null;
                //CAVC 22-09-2023
                //Correcciones Validaciones
                validacion = Boolean.TRUE;

                //Validación Fideicomiso
                if (cbc.getTxtCriterioAX1() != null && !"".equals(cbc.getTxtCriterioAX1())) {
                    if (!onGarantia_isLong(cbc.getTxtCriterioAX1())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX1("");
                        cbc.setCriterioAX1(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    }
                } else {
                    cbc.setCriterioAX1(null);
                }
                //Validación SubFiso
                if (cbc.getTxtCriterioAX2() != null && !"".equals(cbc.getTxtCriterioAX2())) {
                    if (!onGarantia_isLong(cbc.getTxtCriterioAX2())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX2("");
                        cbc.setCriterioAX2(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    }
                } else {
                    cbc.setCriterioAX2(null);
                }
                //Validación Plaza
                if (cbc.getTxtCriterioPlaza() != null && !"".equals(cbc.getTxtCriterioPlaza())) {
                    if (!onGarantia_isNumeric(cbc.getTxtCriterioPlaza())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico..."));
                        cbc.setTxtCriterioPlaza("");
                        cbc.setCriterioPlaza(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    }
                } else {
                    cbc.setCriterioPlaza(null);
                }

                if (validacion.equals(Boolean.TRUE)) {
                    cbc.setCriterioUsuario(usuarioNumero);

                    oContabilidad = new CContabilidad();
                    consultaGarantia = oContabilidad.onGarantia_Consulta(cbc);
                    oContabilidad = null;

                    //DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmGarantiaGral:dtGarantiaGral");
                    //dataTable.setFirst(0);
                    //oContabilidad = new CContabilidad();
                    //int totalRows = oContabilidad.getGuaranteesTotalRows(cbc);
                    //oContabilidad = null;
                    //ContabilidadGarantiaGralBean bean = new ContabilidadGarantiaGralBean();
                    //MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName());
                    //guarantees = lazyModel.getLazyDataModel(cbc);
                    //guarantees.setRowCount(totalRows);
                    //if (guarantees.getRowCount() == 0) {
                    //    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados");
                    //}
                    if (consultaGarantia.isEmpty()) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados");
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error("onContabilidadGarantia_Consulta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_Limpia() {
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioStatus(null);
            cbc.setCriterioCheckInm(false);
            cbc.setCriterioCheckMue(false);
            cbc.setCriterioCheckOtr(false);
            cbc.setCriterioPlaza(null);
            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioPlaza(null);
            consultaGarantiaBien = null;
            consultaGarantia = null;
            seleccionaGarantiaBien = null;
            seleccionaGarantia = null;
            guarantees = null;

        }

        public void onContabilidadGarantia_RegistroAplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();

            try {
                if (garantia.getGarantiaContratoNumero() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                }
                if (garantia.getGarantiaContratoNumeroSub() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Sub Fideicomiso no puede estar vacío..."));
                }
                if (garantia.getGarantiaTipo() == null || garantia.getGarantiaTipo().length == 0) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Garantía no puede estar vacío..."));
                }

                if (!garantia.getTxtGarantiaImporteCredito().isEmpty()) {
                    try {
                        garantia.setTxtGarantiaImporteCredito(limpiaImporte2D(garantia.getTxtGarantiaImporteCredito()));
                        garantia.setGarantiaImporteCredito(Double.parseDouble(garantia.getTxtGarantiaImporteCredito()));
                        garantia.setTxtGarantiaImporteCredito(formatImporte2D(garantia.getGarantiaImporteCredito()));
                        if (garantia.getGarantiaImporteCredito() > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe no puede ser mayor a 999,999,999,999.99..."));
                            garantia.setTxtGarantiaImporteCredito(null);
                            garantia.setGarantiaImporteCredito(null);
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe debe ser un campo numérico..."));
                        garantia.setTxtGarantiaImporteCredito(null);
                        garantia.setGarantiaImporteCredito(null);
                    }

                } else {
                    garantia.setTxtGarantiaImporteCredito(null);
                    garantia.setGarantiaImporteCredito(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe Crédito no puede estar vacío..."));
                }

                if (garantia.getGarantiaMonedaNombre() == null || garantia.getGarantiaMonedaNombre().equals(new String())) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
                }
                if (geneFecha.getGenericoFecha00() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Inicio no puede estar vacío..."));
                }
                if (geneFecha.getGenericoFecha01() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Fin no puede estar vacío..."));
                }
                if (geneFecha.getGenericoFecha00() != null && geneFecha.getGenericoFecha01() != null) {
                    if (geneFecha.getGenericoFecha01().before(geneFecha.getGenericoFecha00())) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Fin no puede ser inferior a al campo Fecha Inicio..."));
                    }
                }
                if (garantia.getGarantiaRelacionCredGar() == null || garantia.getGarantiaRelacionCredGar().equals(new String())) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Crédito / Garantía no puede estar vacío..."));
                } else {

                }
                if (garantia.getGarantiaDesc() == null || garantia.getGarantiaDesc().equals(new String())) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Descripción no puede estar vacío..."));
                }
                if (garantia.getGarantiaComentario() == null || garantia.getGarantiaComentario().equals(new String())) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comentario no puede estar vacío..."));
                }

                if (garantia.getGarantiaStatus() == null || garantia.getGarantiaStatus().equals(new String())) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Estatus no puede estar vacío..."));
                }
                if (mensajesDeError.isEmpty()) {
                    oMoneda = new CMoneda();
                    garantia.setGarantiaMonedaNumero(oMoneda.onMoneda_ObtenMonedaId(garantia.getGarantiaMonedaNombre()));
                    oMoneda = null;
                    if ((geneFecha.getGenericoFecha00() != null) && (geneFecha.getGenericoFecha01() != null)) {
                        garantia.setGarantiaRevaluaFechaIni(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                        garantia.setGarantiaRevaluaFechaFin(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()));
                    }
                    garantia.setGarantiaTipoInmuebles("0");
                    garantia.setGarantiaTipoMuebles("0");
                    garantia.setGarantiaTipoOtros("0");
                    for (int itemTipoGar = 0; itemTipoGar <= garantia.getGarantiaTipo().length - 1; itemTipoGar++) {
                        if (garantia.getGarantiaTipo()[itemTipoGar].equals("inm")) {
                            garantia.setGarantiaTipoInmuebles("-1");
                        }
                        if (garantia.getGarantiaTipo()[itemTipoGar].equals("mue")) {
                            garantia.setGarantiaTipoMuebles("-1");
                        }
                        if (garantia.getGarantiaTipo()[itemTipoGar].equals("otr")) {
                            garantia.setGarantiaTipoOtros("-1");
                        }
                    }
                    oContabilidad = new CContabilidad();
                    if (oContabilidad.onGarantia_Administra(garantia).equals(Boolean.TRUE)) {
                        onContabilidadGarantia_Limpia();
                        cbc.setCriterioAX1(garantia.getGarantiaContratoNumero());
                        cbc.setTxtCriterioAX1(garantia.getGarantiaContratoNumero().toString());
                        cbc.setCriterioAX2(null);
                        cbc.setCriterioUsuario(usuarioNumero);
//                        cbc.setCriterioStatus(garantia.getGarantiaStatus());
                        consultaGarantia = oContabilidad.onGarantia_Consulta(cbc);
//                        seleccionaGarantia = null;
                        RequestContext.getCurrentInstance().execute("dlgGarantia.hide();");
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
//                        boolean aplicaGarantia = true;
//                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aplicaGarantia", aplicaGarantia);
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                    }
                    oContabilidad = null;
                }
            } finally {

                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_RegistroElimina() {
            java.util.Date fechaSistema;
            try {
                fechaSistema = new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
                java.util.Date fechaFin = geneFecha.getGenericoFecha01();
                mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                int res = 0;
                if (fechaFin != null) {
                    res = fechaFin.compareTo(fechaSistema);
                }
                if (res > 0) {
                    mensajeConfrima.setMensajeConfirmaMensaje1("No puede borrarse, Fecha Fin Vigente, ¿Desea continuar con la baja?");
                    mensajeConfrima.setMensajeConfirmaMensaje2(null);
                } else {
                    mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la eliminación del registro actual?");
                }
                mensajeConfrima.setMensajeConfirmaOrigen("Garantia");
                mensajeConfrima.setMensajeConfirmacionAccion("ELIMINAR");
                RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
            } catch (AbstractMethodError Err) {
                logger.error("onContabilidadGarantia_RegistroElimina()");
            }
        }

        public void onContabilidadGarantia_RegistroModifica() {
            String[] arrGrantiaTipo = new String[3];
            geneDes.setGenericoGarantiaDesHabilitado00(Boolean.FALSE);
            geneDes.setGenericoGarantiaDesHabilitado01(Boolean.FALSE);
            geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
            geneDes.setGenericoDesHabilitado07(Boolean.TRUE);
            geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
            garantia.setGarantiaBitTipoOper("MODIFICAR");
            garantia.setGarantiaBitUsuario(usuarioNumero);
            garantia.setGarantiaBitPantalla("contabilidadGarantiaGral");
            garantia.setGarantiaBitIP(usuarioTerminal);
            if (seleccionaGarantia.getGarantiaTipoInmuebles().equals("SI")) {
                arrGrantiaTipo[0] = "inm";
                if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                    geneDes.setGenericoDesHabilitado02(Boolean.TRUE);
                }
            }
            if (seleccionaGarantia.getGarantiaTipoMuebles().equals("SI")) {
                arrGrantiaTipo[1] = "mue";
                if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                    geneDes.setGenericoDesHabilitado03(Boolean.TRUE);
                }
            }
            if (seleccionaGarantia.getGarantiaTipoOtros().equals("SI")) {
                arrGrantiaTipo[2] = "otr";
                if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                    geneDes.setGenericoDesHabilitado04(Boolean.TRUE);
                }
            }

            garantia.setGarantiaTipo(arrGrantiaTipo);
            garantia.setGarantiaStatus(seleccionaGarantia.getGarantiaStatus());
            garantia.setGarantiaMonedaNombre(seleccionaGarantia.getGarantiaMonedaNombre());
            garantia.setGarantiaMonedaNumero(seleccionaGarantia.getGarantiaMonedaNumero());

        }

        public void onContabilidadGarantia_RegistroNuevo() {
            try {
                onInicializaGarantia();
                oMoneda = new CMoneda();
                listaMonedaGar = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                listaMonedaGarBien = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                oMoneda = null;
                oClave = new CClave();
                //listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                listaGarStatus = oClave.onClave_ObtenListadoElementos(31);
                listaGarBienStatus = oClave.onClave_ObtenListadoElementos(31);
                oClave = null;
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
//                garantia.setGarantiaTipo(new String[4]);

                geneDes.setGenericoGarantiaDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado07(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado04(Boolean.FALSE);
                geneDes.setGenericoGarantiaDesHabilitado01(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado101(Boolean.FALSE);
                //    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);

                geneDes.setGenericoDesHabilitado02(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado03(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado04(Boolean.FALSE);

                RequestContext.getCurrentInstance().execute("dlgGarantia.show();");
            } catch (SQLException Err) {
                logger.error("onContabilidadGarantia_RegistroNuevo()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_InicializaGarantia() {
            try {
                onInicializaGarantia();
            } catch (AbstractMethodError Err) {
                logger.error("onContabilidadGarantia_InicializaGarantia()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_RegistroSelecciona() {
            String[] arrGrantiaTipo = new String[3];
            try {
                onInicializaGarantia();
                if (permisoEscrituraGarantias.equals(Boolean.TRUE)) {
                    geneDes.setGenericoGarantiaDesHabilitado00(Boolean.FALSE);
                    geneDes.setGenericoGarantiaDesHabilitado01(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado07(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                } else {
                    geneDes.setGenericoGarantiaDesHabilitado00(Boolean.TRUE);
                    geneDes.setGenericoGarantiaDesHabilitado01(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado07(Boolean.TRUE);
                    geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                }
                geneDes.setGenericoDesHabilitado02(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado03(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado04(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado101(Boolean.TRUE);
                oMoneda = new CMoneda();
                listaMonedaGar = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                listaMonedaGarBien = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                oMoneda = null;
                oClave = new CClave();
                listaGarStatus = oClave.onClave_ObtenListadoElementos(31);
                listaGarBienStatus = oClave.onClave_ObtenListadoElementos(31);
                oClave = null;

                garantia.setGarantiaContratoNumero(seleccionaGarantia.getGarantiaContratoNumero());
                garantia.setTxtGarantiaContratoNumero(garantia.getGarantiaContratoNumero().toString());

                garantia.setGarantiaContratoNumeroSub(seleccionaGarantia.getGarantiaContratoNumeroSub());

                oContrato = new CContrato();
                listSubContratosNom = oContrato.onContrato_ListadoSubContratoSrv(garantia.getGarantiaContratoNumero());
                oContrato = null;

                for (String eleSubfioso : listSubContratosNom) {
                    String[] numSubfiso = eleSubfioso.split(".-");
                    if (numSubfiso[0].equals(seleccionaGarantia.getGarantiaContratoNumeroSub().toString())) {
                        garantia.setTxtGarantiaContratoNumeroSub(eleSubfioso);
                        break;
                    }
                }

//                garantia.setTxtGarantiaContratoNumeroSub(garantia.getGarantiaContratoNumeroSub().toString());
                oContrato = new CContrato();
                garantia.setGarantiaContratoNombre(oContrato.onContrato_ObtenNombre(garantia.getGarantiaContratoNumero()));
                oContrato = null;
                //Verificamos el Tipo de Bien que puede ser registrado
                if (seleccionaGarantia.getGarantiaTipoInmuebles().equals("SI")) {
                    arrGrantiaTipo[0] = "inm";
                    if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                        geneDes.setGenericoDesHabilitado02(Boolean.TRUE);
                    }
                }
                if (seleccionaGarantia.getGarantiaTipoMuebles().equals("SI")) {
                    arrGrantiaTipo[1] = "mue";
                    if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                        geneDes.setGenericoDesHabilitado03(Boolean.TRUE);
                    }
                }
                if (seleccionaGarantia.getGarantiaTipoOtros().equals("SI")) {
                    arrGrantiaTipo[2] = "otr";
                    if (seleccionaGarantia.getGarantiaStatus().equals("ACTIVO")) {
                        geneDes.setGenericoDesHabilitado04(Boolean.TRUE);
                    }
                }

                garantia.setGarantiaTipo(arrGrantiaTipo);
                garantia.setGarantiaImporteCredito(seleccionaGarantia.getGarantiaImporteCredito());
                garantia.setTxtGarantiaImporteCredito(formatImporte2D(seleccionaGarantia.getGarantiaImporteCredito()));
                garantia.setGarantiaImporteGarantia(seleccionaGarantia.getGarantiaImporteGarantia());
                garantia.setGarantiaImporteUltVal(seleccionaGarantia.getGarantiaImporteUltVal());
                garantia.setGarantiaMonedaNombre(seleccionaGarantia.getGarantiaMonedaNombre());
                garantia.setGarantiaMonedaNumero(seleccionaGarantia.getGarantiaMonedaNumero());
                if (seleccionaGarantia.getGarantiaRevaluaFechaIni() != null) {
                    geneFecha.setGenericoFecha00(new java.util.Date(seleccionaGarantia.getGarantiaRevaluaFechaIni().getTime()));
                }
                if (seleccionaGarantia.getGarantiaRevaluaFechaFin() != null) {
                    geneFecha.setGenericoFecha01(new java.util.Date(seleccionaGarantia.getGarantiaRevaluaFechaFin().getTime()));
                }
                garantia.setGarantiaRelacionCredGar(seleccionaGarantia.getGarantiaRelacionCredGar());
                garantia.setGarantiaComentario(seleccionaGarantia.getGarantiaComentario());
                garantia.setGarantiaDesc(seleccionaGarantia.getGarantiaDesc());
                garantia.setGarantiaStatus(seleccionaGarantia.getGarantiaStatus());
                //Obtenemos los bienes que tiene la garantía
                oContabilidad = new CContabilidad();
                consultaGarantiaBien = oContabilidad.onGarantia_ConsultaBien(garantia.getGarantiaContratoNumero(), garantia.getGarantiaContratoNumeroSub());
                oContabilidad = null;
                seleccionaGarantiaBien = null;

                RequestContext.getCurrentInstance().execute("dlgGarantia.show();");
            } catch (SQLException Err) {
                logger.error("onContabilidadGarantia_RegistroSelecciona()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_RegistroSeleccionaBien() {
            try {
                onInicializaGarantiaBien();
                oClave = new CClave();
                listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                oMoneda = new CMoneda();
                listaMonedaGarBien = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                oMoneda = null;

                if (seleccionaGarantiaBien.getGarantiaBienTipo().equals("INMUEBLES")) {
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(71);
                    garantiaBien.setGarantiaBienTituloVentana("Mantenimiento de Inmuebles");
                }
                if (seleccionaGarantiaBien.getGarantiaBienTipo().equals("BIENES MUEBLES")) {
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(72);
                    garantiaBien.setGarantiaBienTituloVentana("Mantenimiento de muebles");
                }
                if (seleccionaGarantiaBien.getGarantiaBienTipo().equals("OTROS BIENES")) {
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(73);
                    garantiaBien.setGarantiaBienTituloVentana("Mantenimiento de Otros Bienes");
                }
                oClave = null;
                garantiaBien.setGarantiaContratoNumero(seleccionaGarantia.getGarantiaContratoNumero());
                oContrato = new CContrato();
                garantia.setGarantiaContratoNombre(oContrato.onContrato_ObtenNombre(garantiaBien.getGarantiaContratoNumero()));
                oContrato = null;

                garantiaBien.setGarantiaContratoNumeroSub(seleccionaGarantia.getGarantiaContratoNumeroSub());
                garantiaBien.setGarantiaBienId(seleccionaGarantiaBien.getGarantiaBienId());
                garantiaBien.setGarantiaBienTipo(seleccionaGarantiaBien.getGarantiaBienTipo());
                garantiaBien.setGarantiaBienClasif(seleccionaGarantiaBien.getGarantiaBienClasif());
                garantiaBien.setGarantiaBienImporte(seleccionaGarantiaBien.getGarantiaBienImporte());
                garantiaBien.setTxtGarantiaBienImporte(formatImporte2D(seleccionaGarantiaBien.getGarantiaBienImporte()));
                garantiaBien.setGarantiaBienMonedaNom(seleccionaGarantiaBien.getGarantiaBienMonedaNom());
                oMoneda = new CMoneda();
                garantiaBien.setGarantiaBienMonedaNum(Short.parseShort(oMoneda.onMoneda_ObtenMonedaId(garantiaBien.getGarantiaBienMonedaNom()).toString()));
                oMoneda = null;
                garantiaBien.setGarantiaBienCveRevalua(seleccionaGarantiaBien.getGarantiaBienCveRevalua());
                if (garantiaBien.getGarantiaBienCveRevalua() == -1) {
                    garantiaBien.setGarantiaBienPeriodicidad(seleccionaGarantiaBien.getGarantiaBienPeriodicidad());
                } else {
                    garantiaBien.setGarantiaBienPeriodicidad(new String());
                }
                garantiaBien.setGarantiaBienImporteUltVal(seleccionaGarantiaBien.getGarantiaBienImporteUltVal());
                if (seleccionaGarantiaBien.getGarantiaBienFechaUltVal() != null) {
                    garantiaBien.setGarantiaBienFechaUltVal(seleccionaGarantiaBien.getGarantiaBienFechaUltVal());
                    geneFecha.setGenericoFecha02(new java.util.Date(garantiaBien.getGarantiaBienFechaUltVal().getTime()));
                }
                if (seleccionaGarantiaBien.getGarantiaBienFechaPrxVal() != null) {
                    garantiaBien.setGarantiaBienFechaPrxVal(seleccionaGarantiaBien.getGarantiaBienFechaPrxVal());
                    geneFecha.setGenericoFecha03(new java.util.Date(garantiaBien.getGarantiaBienFechaPrxVal().getTime()));
                }
                garantiaBien.setGarantiaBienDescripcion(seleccionaGarantiaBien.getGarantiaBienDescripcion());
                garantiaBien.setGarantiaBienComentario(seleccionaGarantiaBien.getGarantiaBienComentario());
                garantiaBien.setGarantiaBienStatus(seleccionaGarantiaBien.getGarantiaBienStatus());
                garantiaBien.setGarantiaBitIP(usuarioTerminal);
                garantiaBien.setGarantiaBitPantalla("contabilidadGarantiaGral");
                garantiaBien.setGarantiaBitUsuario(usuarioNumero);
                //Titulo de botón
                geneTitulo.setGenericoTitulo00("Entrada");
                //Campos

                geneDes.setGenericoDesHabilitado00(Boolean.TRUE); //No se pueden editar
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE); //Si se pueden editar
                //Botones Entrada, Selida, Revaluación 
                if (permisoEscrituraGarantias.equals(Boolean.TRUE)) {
                    geneDes.setGenericoDesHabilitado05(Boolean.TRUE); //Entrada
                    geneDes.setGenericoDesHabilitado06(Boolean.TRUE);//Salida, Revaluación 
                } else {
                    geneDes.setGenericoDesHabilitado05(Boolean.TRUE); //Entrada
                    geneDes.setGenericoDesHabilitado06(Boolean.FALSE);//Salida, Revaluación
                }
                setTcVisible(Boolean.FALSE);

//                geneDes.setGenericoDesHabilitado05(Boolean.TRUE); //Entrada
//                geneDes.setGenericoDesHabilitado06(Boolean.FALSE);//Salida, Revaluación
                //Fin  
                RequestContext.getCurrentInstance().execute("dlgGarBien.show();");
            } catch (SQLException | NumberFormatException Err) {
                logger.error("onContabilidadGarantia_RegistroSeleccionaBien()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_VerificaContratoIngresado() {
            try {

                listSubContratosNom = null;
                garantia.setGarantiaContratoNombre(null);
                garantia.setGarantiaContratoNumeroSub(null);
                garantia.setGarantiaImporteCredito(null);
                garantia.setGarantiaImporteGarantia(0.00);
                garantia.setGarantiaImporteUltVal(0.00);
                garantia.setGarantiaMonedaNombre(null);
                garantia.setGarantiaMonedaNumero(null);
                garantia.setGarantiaRevaluaPeriodo(null);
                garantia.setGarantiaRelacionCredGar(null);
                garantia.setGarantiaTipoInmuebles("0");
                garantia.setGarantiaTipoMuebles("0");
                garantia.setGarantiaTipoNumerario("0");
                garantia.setGarantiaTipoOtros("0");
                garantia.setGarantiaTipoValores("0");
                garantia.setGarantiaTipo(null);
                garantia.setTxtGarantiaImporteCredito(null);
                garantia.setTxtGarantiaContratoNumeroSub(null);
                garantia.setGarantiaFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                geneFecha.setGenericoFecha00(null);
                geneFecha.setGenericoFecha01(null);
                garantia.setGarantiaRevaluaFechaIni(null);
                garantia.setGarantiaRevaluaFechaFin(null);
                garantia.setGarantiaDesc(null);
                garantia.setGarantiaComentario(null);
                garantia.setGarantiaStatus(null);
                garantia.setGarantiaBitTipoOper("REGISTRO");
                garantia.setGarantiaBitUsuario(usuarioNumero);
                garantia.setGarantiaBitPantalla("contabilidadGarantiaGral");
                garantia.setGarantiaBitIP(usuarioTerminal);

                oContabilidadGrales = new contabilidadGrales();
                if (oContabilidadGrales.onValidaNumerico(garantia.getTxtGarantiaContratoNumero(), "Fideicomiso", "L")) {
                    garantia.setGarantiaContratoNumero(Long.parseLong(garantia.getTxtGarantiaContratoNumero()));

                } else {
                    garantia.setGarantiaContratoNumero(null);
                    garantia.setTxtGarantiaContratoNumero(null);
                }

                oContabilidadGrales = null;
//                listaSubCont = new ArrayList<>();
                if (garantia.getGarantiaContratoNumero() != null && garantia.getGarantiaContratoNumero() != 0) {
                    oContrato = new CContrato();
                    if (oContrato.onContrato_VerificaExistencia(garantia.getGarantiaContratoNumero()).equals(Boolean.TRUE)) {
                        if (oContrato.onContrato_ObtenProducto(garantia.getGarantiaContratoNumero()).contains("GARANTIA")) {
                            //Aqui verificamos la atención hacia el fideicomiso 
                            usuarioFiltro = new ArrayList<>();
                            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");

                            if (oContrato.onContrato_VerificaAtencion(usuarioFiltro, garantia.getGarantiaContratoNumero()).equals(Boolean.TRUE)) {
                                garantia.setGarantiaContratoNombre(oContrato.onContrato_ObtenNombre(garantia.getGarantiaContratoNumero()));
                                listSubContratosNom = oContrato.onContrato_ListadoSubContratoSrv(garantia.getGarantiaContratoNumero());
                                garantia.setGarantiaContratoNumeroSub(0);
//                                if (oContrato.onContrato_VerificaSiTieneSubFisos(garantia.getGarantiaContratoNumero(), listaSubCont).equals(Boolean.FALSE)) {
//                                    garantia.setTxtGarantiaContratoNumeroSub("0");
//                                    garantia.setGarantiaContratoNumeroSub(0);
//                                    geneDes.setGenericoDesHabilitado101(Boolean.TRUE);
//                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso sin Sub Fideicomisos");
//                                } else {
//                                    geneDes.setGenericoDesHabilitado101(Boolean.FALSE);
//                                }
                            } else {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                                garantia.setGarantiaContratoNumero(null);
                                garantia.setTxtGarantiaContratoNumero(null);
                            }
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso: ".concat(garantia.getGarantiaContratoNumero().toString()).concat(" no es de garantía..."));
                            garantia.setGarantiaContratoNumero(null);
                            garantia.setTxtGarantiaContratoNumero(null);
                        }
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                        garantia.setGarantiaContratoNumero(null);
                        garantia.setTxtGarantiaContratoNumero(null);
                    }
                    oContrato = null;
                } else {
                    if (garantia.getGarantiaContratoNumero() == null && (mensaje == null)) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe ingresar un número de Fideicomiso válido...");
                    }
                }
            } catch (SQLException Err) {
                logger.error("onContabilidadGarantia_VerificaContratoIngresado()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_DespliegaVentana() {
            try {
                onInicializaGarantiaBien();
                setTcVisible(Boolean.TRUE);

                parametrosFC = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                if (parametrosFC.get("garTipo").equals("inm")) {
                    oClave = new CClave();
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(71);
                    listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                    oClave = null;
                    garantiaBien.setGarantiaBienTipo("INMUEBLES");
                    garantiaBien.setGarantiaBienTituloVentana("Registro de Inmuebles");
                    RequestContext.getCurrentInstance().execute("dlgGarBien.show();");
                }
                if (parametrosFC.get("garTipo").equals("mue")) {
                    oClave = new CClave();
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(72);
                    listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                    oClave = null;
                    garantiaBien.setGarantiaBienTipo("BIENES MUEBLES");
                    garantiaBien.setGarantiaBienTituloVentana("Registro de Muebles");
                    RequestContext.getCurrentInstance().execute("dlgGarBien.show();");
                }
                if (parametrosFC.get("garTipo").equals("otr")) {
                    oClave = new CClave();
                    listaGarClasif = oClave.onClave_ObtenListadoElementos(73);
                    listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                    oClave = null;
                    garantiaBien.setGarantiaBienTipo("OTROS BIENES");
                    garantiaBien.setGarantiaBienTituloVentana("Registro de Otros Bienes");
                    RequestContext.getCurrentInstance().execute("dlgGarBien.show();");
                }
                garantiaBien.setGarantiaContratoNumero(garantia.getGarantiaContratoNumero());
                garantiaBien.setGarantiaContratoNumeroSub(garantia.getGarantiaContratoNumeroSub());
                //Botones Entrada, Selida, Revaluación 
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado05(Boolean.FALSE); //Entrada
                geneDes.setGenericoDesHabilitado06(Boolean.TRUE);  //Salida, Revaluación
                //Fin
                geneFecha.setGenericoFecha02(null);
                geneFecha.setGenericoFecha03(null);
                geneTitulo.setGenericoTitulo00("Entrada");
                oContabilidad = new CContabilidad();
                oContabilidad.onGarantia_ObtenMaxBienId(garantiaBien);
                oContabilidad = null;
            } catch (SQLException Err) {
                logger.error("onContabilidadGarantia_DespliegaVentana()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadRevalucionPeriodicidad() {
            if (garantiaBien.getGarantiaBienPeriodicidad() == null) {
                garantiaBien.setGarantiaBienCveRevalua(Short.parseShort("0"));
            } else {
                garantiaBien.setGarantiaBienCveRevalua(Short.parseShort("-1"));
            }

        }

        public void onContabilidadGarantiaBien_Entrada() {
            Boolean bLevantoPopUp = Boolean.FALSE;
            List<FacesMessage> mensajesDeError = new ArrayList<>();

            try {
                if (((garantiaBien.getGarantiaBienClasif() == null) || (garantiaBien.getGarantiaBienClasif().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Clasificación de bien no puede estar vacío..."));
                }
                if (!garantiaBien.getTxtGarantiaBienImporte().isEmpty()) {
                    try {
                        garantiaBien.setTxtGarantiaBienImporte(limpiaImporte2D(garantiaBien.getTxtGarantiaBienImporte()));
                        garantiaBien.setGarantiaBienImporte(Double.parseDouble(garantiaBien.getTxtGarantiaBienImporte()));
                        garantiaBien.setTxtGarantiaBienImporte(formatImporte2D(garantiaBien.getGarantiaBienImporte()));
                        if (garantiaBien.getGarantiaBienImporte() > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe bien no puede ser mayor a 999,999,999,999.99..."));
                            garantiaBien.setTxtGarantiaBienImporte(null);
                            garantiaBien.setGarantiaBienImporte(null);
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe bien debe ser un campo numérico..."));
                        garantiaBien.setTxtGarantiaBienImporte(null);
                        garantiaBien.setGarantiaBienImporte(null);
                    }

                } else {
                    garantiaBien.setTxtGarantiaBienImporte(null);
                    garantiaBien.setGarantiaBienImporte(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe bien no puede estar vacío..."));
                }

                if (((garantiaBien.getGarantiaBienCveRevalua() == null) || (garantiaBien.getGarantiaBienCveRevalua().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Revaluación no puede estar vacío..."));
                }
                if (((garantiaBien.getGarantiaBienMonedaNom() == null) || (garantiaBien.getGarantiaBienMonedaNom().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
                }

                if (((garantiaBien.getGarantiaBienDescripcion() == null) || (garantiaBien.getGarantiaBienDescripcion().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Descripción no puede estar vacío..."));
                } else {
                    garantiaBien.setGarantiaBienDescripcion(garantiaBien.getGarantiaBienDescripcion().replaceAll("\r\n", "\n"));
                }
                if (((garantiaBien.getGarantiaBienComentario() == null) || (garantiaBien.getGarantiaBienComentario().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comentario no puede estar vacío..."));
                } else {
                    garantiaBien.setGarantiaBienComentario(garantiaBien.getGarantiaBienComentario().replaceAll("\r\n", "\n"));

                }
                if ((garantiaBien.getGarantiaBienStatus() == null)) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Status no puede estar vacío..."));
                }

                if (mensajesDeError.isEmpty()) {
                    if ((garantiaBien.getGarantiaBienCveRevalua() == 0) && (bLevantoPopUp.equals(Boolean.FALSE))) {
                        bLevantoPopUp = Boolean.TRUE;
                        mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                        mensajeConfrima.setMensajeConfirmaMensaje1("¿Está de acuerdo en que no se revalue este bien?");
                        mensajeConfrima.setMensajeConfirmaMensaje2("REVALUACION");
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral00.show();");
                    }
                    if ((geneFecha.getGenericoFecha02() == null) && (bLevantoPopUp.equals(Boolean.FALSE))) {
                        bLevantoPopUp = Boolean.TRUE;
                        mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                        mensajeConfrima.setMensajeConfirmaMensaje1("¿Está de acuerdo en que la fecha de última valuación sea ".concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).concat(" ?"));
                        mensajeConfrima.setMensajeConfirmaMensaje2("FECHA");
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral00.show();");
                    }
                    if (bLevantoPopUp.equals(Boolean.FALSE)) {
                        onGrantiaBienEntradaAplica();
                    }

                }
            } catch (AbstractMethodError Err) {
                logger.error("onContabilidadGarantiaBien_Entrada()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantiaBien_EntradaVerifica00() {
            try {
                if (mensajeConfrima.getMensajeConfirmaMensaje2().equals("REVALUACION")) {
                    if (geneFecha.getGenericoFecha02() == null) {
                        mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                        mensajeConfrima.setMensajeConfirmaMensaje1("¿Está de acuerdo en que la fecha de última valuación sea ".concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).concat(" ?"));
                        mensajeConfrima.setMensajeConfirmaMensaje2("FECHA");
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral00.show();");
                    } else {
                        onGrantiaBienEntradaAplica();
                    }
                } else {
                    if (mensajeConfrima.getMensajeConfirmaMensaje2().equals("FECHA")) {
                        onGrantiaBienEntradaAplica();
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral00.hide();");
                    }
                }
                //Obtenemos los bienes que tiene la garantía
                oContabilidad = new CContabilidad();
                consultaGarantiaBien = oContabilidad.onGarantia_ConsultaBien(garantiaBien.getGarantiaContratoNumero(), garantiaBien.getGarantiaContratoNumeroSub());
                seleccionaGarantiaBien = null;

            } catch (SQLException Err) {
                logger.error("onContabilidadGarantiaBien_EntradaVerifica00()");
            } finally {
//                if (mensaje != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
//                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantiaBien_EntradaVerifica01() {
            RequestContext.getCurrentInstance().execute("dlgPopUpGral00.hide();");
        }

        public void onContabilidadGarantia_Cancela() {
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
        }

        public void onContabilidadGarantiaBien_Modifica() {
            geneTitulo.setGenericoTitulo00("Aplicar");
            geneDes.setGenericoDesHabilitado01(Boolean.FALSE); //Si se pueden editar
            //Botones Entrada, Selida, Revaluación 
            geneDes.setGenericoDesHabilitado05(Boolean.FALSE); //Entrada
            geneDes.setGenericoDesHabilitado06(Boolean.TRUE);  //Salida, Revaluación
            garantiaBien.setGarantiaBitTipoOper("MODIFICAR");
        }

        public void onContabilidadGarantiaBien_Salida() {
            mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
            mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma dar salida al bien seleccionado?");
            mensajeConfrima.setMensajeConfirmaOrigen("GarantiaBien");
            mensajeConfrima.setMensajeConfirmacionAccion("SALIDA");
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
            onFinalizaObjetos();
        }

        public void onContabilidadGarantiaBien_RevaluacionConfirma() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            if (!garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().isEmpty()) {
                try {
                    garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(limpiaImporte2D(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo()));
                    garantiaBienSalida.setGarantiaBienSalidaImporteNvo(Double.parseDouble(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo()));
                    garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(formatImporte2D(garantiaBienSalida.getGarantiaBienSalidaImporteNvo()));
                    if (garantiaBienSalida.getGarantiaBienSalidaImporteNvo() > VALOR_MAX) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe nuevo de la garantía  no puede ser mayor a 999,999,999,999.99..."));
                        garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(null);
                        garantiaBienSalida.setGarantiaBienSalidaImporteNvo(null);
                    }
                } catch (NumberFormatException e) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe nuevo de la garantía debe ser un campo numérico..."));
                    garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(null);
                    garantiaBienSalida.setGarantiaBienSalidaImporteNvo(null);
                }
            } else {
                garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(null);
                garantiaBienSalida.setGarantiaBienSalidaImporteNvo(null);
                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe nuevo de la garantía no puede estar vacío..."));
            }
            if (geneFecha.getGenericoFecha04() == null) {
                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha nueva valuación no puede estar vacío..."));
            }
            if (mensajesDeError.isEmpty()) {
                mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                mensajeConfrima.setMensajeConfirmaMensaje1("¿Aplicar revaluación?");
                mensajeConfrima.setMensajeConfirmaOrigen("GarantiaBien");
                mensajeConfrima.setMensajeConfirmacionAccion("REVALUACION");
                RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
            }
            for (FacesMessage mensaje : mensajesDeError) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

        public void onContabilidadGarantiaBien_RevaluacionDespliega() {
            garantiaBienSalida.setGarantiaBienSalidaImporteNvo(null);
            garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(null);
            geneFecha.setGenericoFecha04(null);
            garantiaBienSalida.setGarantiaBienSalidaImporteAnt(garantiaBien.getGarantiaBienImporteUltVal());
            garantiaBienSalida.setGarantiaBienSalidaFechaValAnt(garantiaBien.getGarantiaBienFechaUltVal());
            geneFecha.setGenericoFecha05(new java.util.Date(garantiaBienSalida.getGarantiaBienSalidaFechaValAnt().getTime()));
            RequestContext.getCurrentInstance().execute("dlgRevaluacion.show();");
        }

        public void onContabilidadGarantiaBien_RevaluacionVerifica() {

            if (garantiaBien.getGarantiaBienCveRevalua() == 0) {

                garantiaBien.setGarantiaBienPeriodicidad(null);
            }
        }
        //Funciones privadas

        private void onGrantiaBienEntradaAplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                if (geneFecha.getGenericoFecha02() == null) {
                    geneFecha.setGenericoFecha02(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                }
                if (garantiaBien.getGarantiaBienCveRevalua() == -1) {
                    if (((garantiaBien.getGarantiaBienPeriodicidad() == null) || (garantiaBien.getGarantiaBienPeriodicidad().equals(new String()))) && (validacion.equals(Boolean.TRUE))) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Periodicidad no puede estar vacío..."));
                        validacion = Boolean.FALSE;
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    if (geneFecha.getGenericoFecha02() != null) {
                        garantiaBien.setGarantiaBienFechaUltVal(new java.sql.Date(geneFecha.getGenericoFecha02().getTime()));
                    }
                    if (geneFecha.getGenericoFecha03() != null) {
                        garantiaBien.setGarantiaBienFechaPrxVal(new java.sql.Date(geneFecha.getGenericoFecha03().getTime()));
                    }
                    oMoneda = new CMoneda();
                    garantiaBien.setGarantiaBienMonedaNum(Short.parseShort(oMoneda.onMoneda_ObtenMonedaId(garantiaBien.getGarantiaBienMonedaNom()).toString()));
                    if (!garantiaBien.getGarantiaBienMonedaNom().equals("MONEDA NACIONAL")) {
                        garantiaBien.setGarantiaBienImporteTC(oMoneda.onMonedaTC_ObtenTipoCambio(garantiaBien.getGarantiaBienMonedaNom(),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia"))));
                    }
                    oMoneda = null;
                    oContabilidad = new CContabilidad();
                    if (oContabilidad.onGarantia_AdministraBien(garantiaBien).equals(Boolean.TRUE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
                        RequestContext.getCurrentInstance().execute("dlgGarBien.hide();");
                        //cbc.setCriterioAX1(garantia.getGarantiaContratoNumero());
                        //cbc.setCriterioAX2(null);
                        consultaGarantia = oContabilidad.onGarantia_Consulta(cbc);
                        for (int iGarantia = 0; iGarantia < consultaGarantia.size(); iGarantia++) {
                            if (consultaGarantia.get(iGarantia).getGarantiaContratoNumero().equals(garantia.getGarantiaContratoNumero())) {
                                garantia.setGarantiaImporteGarantia(consultaGarantia.get(iGarantia).getGarantiaImporteGarantia());
                                garantia.setGarantiaImporteUltVal(consultaGarantia.get(iGarantia).getGarantiaImporteUltVal());
                                break;
                            }
                        }
                        consultaGarantiaBien = oContabilidad.onGarantia_ConsultaBien(garantiaBien.getGarantiaContratoNumero(), garantiaBien.getGarantiaContratoNumeroSub());
                        seleccionaGarantiaBien = null;
                        //seleccionaGarantia     = null;
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                        validacion = Boolean.FALSE;
                    }
                    oContabilidad = null;
                }
            } catch (SQLException Err) {
                logger.error("onGrantiaBienEntradaAplica()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);

                }
            }
        }

        private void onInicializaGarantiaBien() {
            garantiaBien.setGarantiaBienClasif(new String());
            garantiaBien.setGarantiaBienComentario(null);
            garantiaBien.setGarantiaBienCveRevalua(Short.parseShort("0"));
            garantiaBien.setGarantiaBienDescripcion(null);
            garantiaBien.setGarantiaBienFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));

            garantiaBien.setGarantiaBienFechaPrxVal(null);
            garantiaBien.setGarantiaBienFechaUltVal(null);
            garantiaBien.setGarantiaBienId(Short.parseShort("0"));
            garantiaBien.setGarantiaBienImporte(null);
            garantiaBien.setGarantiaBienImporteTC(1.0);
            garantiaBien.setGarantiaBienImporteUltVal(0.00);
            garantiaBien.setGarantiaBienMonedaNom(null);
            garantiaBien.setGarantiaBienMonedaNum(null);
            garantiaBien.setGarantiaBienPeriodicidad(new String());
            garantiaBien.setGarantiaBienStatus("ACTIVO");
            garantiaBien.setGarantiaBienTipo(null);
            garantiaBien.setGarantiaBienTituloVentana(null);
            garantiaBien.setGarantiaBitTipoOper("ENTRADA");
            garantiaBien.setGarantiaBitUsuario(usuarioNumero);
            garantiaBien.setGarantiaBitPantalla("contabilidadGarantiaGral");
            garantiaBien.setGarantiaBitIP(usuarioTerminal);
            garantiaBien.setTxtGarantiaBienImporte(null);
            geneFecha.setGenericoFecha02(null);
            geneFecha.setGenericoFecha03(null);
            mensajeConfrima.setMensajeConfirmaMensaje1(null);
            mensajeConfrima.setMensajeConfirmaMensaje2(null);
            mensajeConfrima.setMensajeConfirmaMensaje3(null);
        }

        private void onInicializaGarantia() {
            listSubContratosNom = null;
            garantia.setGarantiaContratoNumero(null);
            garantia.setGarantiaContratoNumeroSub(null);
            garantia.setGarantiaContratoNombre(null);
            garantia.setGarantiaImporteCredito(null);
            garantia.setGarantiaImporteGarantia(0.00);
            garantia.setGarantiaImporteUltVal(0.00);
            garantia.setGarantiaMonedaNombre(null);
            garantia.setGarantiaMonedaNumero(null);
            garantia.setGarantiaRevaluaPeriodo(null);
            garantia.setGarantiaRelacionCredGar(null);
            garantia.setGarantiaTipoInmuebles("0");
            garantia.setGarantiaTipoMuebles("0");
            garantia.setGarantiaTipoNumerario("0");
            garantia.setGarantiaTipoOtros("0");
            garantia.setGarantiaTipoValores("0");
            garantia.setGarantiaTipo(null);
            garantia.setTxtGarantiaImporteCredito(null);
            garantia.setTxtGarantiaContratoNumero(null);
            garantia.setTxtGarantiaContratoNumeroSub(null);
            garantia.setGarantiaFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);
            garantia.setGarantiaRevaluaFechaIni(null);
            garantia.setGarantiaRevaluaFechaFin(null);
            garantia.setGarantiaDesc(null);
            garantia.setGarantiaComentario(null);
            garantia.setGarantiaStatus(null);
            garantia.setGarantiaBitTipoOper("REGISTRO");
            garantia.setGarantiaBitUsuario(usuarioNumero);
            garantia.setGarantiaBitPantalla("contabilidadGarantiaGral");
            garantia.setGarantiaBitIP(usuarioTerminal);
        }

        public void onGarantia_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioAX1":
                    if (!"".equals(cbc.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX1(), "Fideicomiso", "L")) {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } else {
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cbc.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX2(), "SubFiso", "L")) {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } else {
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioPlaza":
                    if (!"".equals(cbc.getTxtCriterioPlaza()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioPlaza(), "Plaza", "I")) {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    } else {
                        cbc.setCriterioPlaza(null);
                        cbc.setTxtCriterioPlaza(null);
                    }
                    break;

                case "txtGarantiaContratoNumeroSub":
                    if (!"".equals(garantiaBien.getTxtGarantiaContratoNumeroSub()) && oContabilidadGrales.onValidaNumerico(garantia.getTxtGarantiaContratoNumeroSub(), "SubFiso", "I")) {
                        garantia.setGarantiaContratoNumeroSub(Integer.parseInt(garantia.getTxtGarantiaContratoNumeroSub()));
                    } else {
                        garantia.setGarantiaContratoNumeroSub(null);
                        garantia.setTxtGarantiaContratoNumeroSub(null);
                    }
                    break;

                case "txtGarantiaImporteCredito":

                    garantia.setTxtGarantiaImporteCredito(garantia.getTxtGarantiaImporteCredito().replaceAll(",", ""));
                    garantia.setTxtGarantiaImporteCredito(garantia.getTxtGarantiaImporteCredito().replace("$", ""));
                    if (!"".equals(garantia.getTxtGarantiaImporteCredito()) && oContabilidadGrales.onValidaNumerico(garantia.getTxtGarantiaImporteCredito(), "Importe de Crédito", "D")) {
                        if (garantia.getTxtGarantiaImporteCredito().contains(".")) {
                            Integer largo = garantia.getTxtGarantiaImporteCredito().length();
                            if (largo >= garantia.getTxtGarantiaImporteCredito().indexOf(".") + 3) {
                                largo = garantia.getTxtGarantiaImporteCredito().indexOf(".") + 3;
                            }
                            garantia.setTxtGarantiaImporteCredito(garantia.getTxtGarantiaImporteCredito().substring(0, largo));
                        }
                        garantia.setGarantiaImporteCredito(Double.parseDouble(garantia.getTxtGarantiaImporteCredito()));
                        garantia.setTxtGarantiaImporteCredito(formatImporte2D(garantia.getGarantiaImporteCredito()));
                    } else {
                        garantia.setGarantiaImporteCredito(null);
                        garantia.setTxtGarantiaImporteCredito(null);
                    }
                    break;

                case "txtGarantiaBienImporte":

                    garantiaBien.setTxtGarantiaBienImporte(garantiaBien.getTxtGarantiaBienImporte().replaceAll(",", ""));
                    garantiaBien.setTxtGarantiaBienImporte(garantiaBien.getTxtGarantiaBienImporte().replace("$", ""));
                    if (!"".equals(garantiaBien.getTxtGarantiaBienImporte()) && oContabilidadGrales.onValidaNumerico(garantiaBien.getTxtGarantiaBienImporte(), "Importe Bien", "D")) {
                        if (garantiaBien.getTxtGarantiaBienImporte().contains(".")) {
                            Integer largo = garantiaBien.getTxtGarantiaBienImporte().length();
                            if (largo >= garantiaBien.getTxtGarantiaBienImporte().indexOf(".") + 3) {
                                largo = garantiaBien.getTxtGarantiaBienImporte().indexOf(".") + 3;
                            }
                            garantiaBien.setTxtGarantiaBienImporte(garantiaBien.getTxtGarantiaBienImporte().substring(0, largo));
                        }
                        garantiaBien.setGarantiaBienImporte(Double.parseDouble(garantiaBien.getTxtGarantiaBienImporte()));
                        garantiaBien.setTxtGarantiaBienImporte(formatImporte2D(garantiaBien.getGarantiaBienImporte()));
                    } else {
                        garantiaBien.setGarantiaBienImporte(null);
                        garantiaBien.setTxtGarantiaBienImporte(null);
                    }
                    break;

                case "txtGarantiaBienSalidaImporteNvo":

                    garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().replaceAll(",", ""));
                    garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().replace("$", ""));
                    if (!"".equals(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo()) && oContabilidadGrales.onValidaNumerico(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo(), "Importe nuevo de la garantía", "D")) {
                        if (garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().contains(".")) {
                            Integer largo = garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().length();
                            if (largo >= garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().indexOf(".") + 3) {
                                largo = garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().indexOf(".") + 3;
                            }
                            garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo().substring(0, largo));
                        }
                        garantiaBienSalida.setGarantiaBienSalidaImporteNvo(Double.parseDouble(garantiaBienSalida.getTxtGarantiaBienSalidaImporteNvo()));
                        garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(formatImporte2D(garantiaBienSalida.getGarantiaBienSalidaImporteNvo()));
                    } else {
                        garantiaBienSalida.setGarantiaBienSalidaImporteNvo(null);
                        garantiaBienSalida.setTxtGarantiaBienSalidaImporteNvo(null);
                    }
                    break;
            }

            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oComunes != null) {
                oComunes = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }

        private boolean onGarantia_isLong(String cadena) {
            try {
                Long.parseLong(cadena);
                return true;
            } catch (NumberFormatException Err) {

                return false;
            }
        }

        private boolean onGarantia_isNumeric(String cadena) {
            try {
                Integer.parseInt(cadena);
                return true;
            } catch (NumberFormatException Err) {

                return false;
            }
        }
    }

    class contabilidadSaldo {

        public contabilidadSaldo() {
            formatoDecimal = new DecimalFormat("###,###,###,###,###.00");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadSaldo.";
            usuarioAppCtx = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadConsSaldo_Consulta() {

            List<FacesMessage> mensajesDeError = new ArrayList<>();

            try {
                consultaSaldo = null;
                balanceInquiries = null;
                seleccionaSaldo = null;
                if (((cbs.getCriterioTipo() == null) || (cbs.getCriterioTipo().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Destino ::. no puede esta vacío..."));
                }
                if ((!cbs.getTxtCriterioCTAM().isEmpty())) {
                    try {
                        cbs.setCriterioCTAM(Short.parseShort(cbs.getTxtCriterioCTAM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La CTAM debe ser un campo numérico..."));
                        cbs.setCriterioCTAM(null);
                        cbs.setTxtCriterioCTAM(null);
                    }
                } else {
                    cbs.setCriterioCTAM(null);
                }
                if ((!cbs.getTxtCriterioSC1().isEmpty())) {
                    try {
                        cbs.setCriterioSC1(Short.parseShort(cbs.getTxtCriterioSC1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC1 debe ser un campo numérico..."));
                        cbs.setCriterioSC1(null);
                        cbs.setTxtCriterioSC1(null);
                    }
                } else {
                    cbs.setCriterioSC1(null);
                }
                if ((!cbs.getTxtCriterioSC2().isEmpty())) {
                    try {
                        cbs.setCriterioSC2(Short.parseShort(cbs.getTxtCriterioSC2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC2 debe ser un campo numérico..."));
                        cbs.setCriterioSC2(null);
                        cbs.setTxtCriterioSC2(null);
                    }
                } else {
                    cbs.setCriterioSC2(null);
                }
                if ((!cbs.getTxtCriterioSC3().isEmpty())) {
                    try {
                        cbs.setCriterioSC3(Short.parseShort(cbs.getTxtCriterioSC3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC3 debe ser un campo numérico..."));
                        cbs.setCriterioSC3(null);
                        cbs.setTxtCriterioSC3(null);
                    }
                } else {
                    cbs.setCriterioSC3(null);
                }
                if ((!cbs.getTxtCriterioSC4().isEmpty())) {
                    try {
                        cbs.setCriterioSC4(Short.parseShort(cbs.getTxtCriterioSC4()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC4 debe ser un campo numérico..."));
                        cbs.setCriterioSC4(null);
                        cbs.setTxtCriterioSC4(null);
                    }
                } else {
                    cbs.setCriterioSC4(null);
                }
                if ((!cbs.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cbs.setCriterioAX1(Long.parseLong(cbs.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbs.setCriterioAX1(null);
                        cbs.setTxtCriterioAX1(null);
                    }
                } else {
                    cbs.setCriterioAX1(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                }
                if ((!cbs.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cbs.setCriterioAX2(Long.parseLong(cbs.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico..."));
                        cbs.setCriterioAX2(null);
                        cbs.setTxtCriterioAX2(null);
                    }
                } else {
                    cbs.setCriterioAX2(null);
                }
                if ((!cbs.getTxtCriterioMes().isEmpty())) {
                    try {
                        cbs.setCriterioMes(Short.parseShort(cbs.getTxtCriterioMes()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  Mes debe ser un campo numérico..."));
                        cbs.setCriterioMes(null);
                        cbs.setTxtCriterioMes(null);
                    }
                } else {
                    cbs.setCriterioMes(null);
                }
                if ((!cbs.getTxtCriterioAño().isEmpty())) {
                    try {
                        cbs.setCriterioAño(Short.parseShort(cbs.getTxtCriterioAño()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Año debe ser un campo numérico..."));
                        cbs.setCriterioAño(null);
                        cbs.setTxtCriterioAño(null);
                    }
                } else {
                    cbs.setCriterioAño(null);
                }
                if (((cbs.getCriterioMonedaNom() == null) || (cbs.getCriterioMonedaNom().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
                }

                if ((cbs.getCriterioAX1() != null)) {
                    if (onVerificaAtencionContrato(cbs.getCriterioAX1()).equals(Boolean.FALSE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                    }
                }
                if (mensajesDeError.isEmpty()) {
                    if (cbs.getCriterioTipo().equals("HST")) {
                        if ((cbs.getCriterioAño() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Año no puede estar vacío..."));
                        }
                        if ((cbs.getCriterioMes() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mes no puede estar vacío..."));
                        }
                    }
                    if (mensajesDeError.isEmpty()) {
                        if (cbs.getCriterioTipo().equals("ACT")) {
                            cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                            cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                        }
                        if (cbs.getCriterioTipo().equals("MSA")) {
                            cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAño")));
                            cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAMes")));
                        }

                        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmContabilidadCons:accordeon:dtSaldos");
                        dataTable.setFirst(0);

                        oContabilidad = new CContabilidad();

                        int totalRows = 0;

                        if ((cbs.getCriterioTipo().equals("ACT")) || (cbs.getCriterioTipo().equals("MSA"))) {
                            totalRows = oContabilidad.getBalanceInquiriesTotalRows(cbs);
                        }
                        if (cbs.getCriterioTipo().equals("HST")) {
                            totalRows = oContabilidad.getBalanceInquiriesHistoricalTotalRows(cbs);
                        }
                        oContabilidad = null;

                        ContabilidadSaldoBean bean = new ContabilidadSaldoBean();
                        MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getBalanceInquiries");
                        balanceInquiries = lazyModel.getLazyDataModel(cbs);
                        balanceInquiries.setRowCount(totalRows);

                        if (balanceInquiries.getRowCount() == 0) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados"));
                        }
                    }
                }
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadSaldo_Consulta()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsSaldo_RegistroBuscaCtoNom() {
            try {
//                listaSubCont = new ArrayList<>();
//                listaContratoSub = new LinkedHashMap<>();
                oContabilidadGrales = new contabilidadGrales();
                if (!"".equals(cbs.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioAX1(), "Fideicomiso", "L")) {

                    cbs.setCriterioAX1(Long.parseLong(cbs.getTxtCriterioAX1()));
                    oContrato = new CContrato();
                    if (oContrato.onContrato_VerificaAtencion(usuarioFiltro, cbs.getCriterioAX1()).equals(Boolean.TRUE)) {
                        cbs.setCriterioNomContrato(oContrato.onContrato_ObtenNombre(cbs.getCriterioAX1()));
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                        cbs.setTxtCriterioAX1(null);
                        cbs.setCriterioNomContrato(null);
                    }
                    oContrato = null;

                } else {
                    cbs.setTxtCriterioAX1(null);
                    cbs.setCriterioNomContrato(null);
                }
                oContabilidadGrales = null;

            } catch (SQLException | NumberFormatException Err) {
                logger.error("onContabilidadConsSaldo_RegistroBuscaCtoNom()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsSaldo_Exporta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();

            try {
                if (((cbs.getCriterioTipo() == null) || (cbs.getCriterioTipo().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Destino ::. no puede esta vacío..."));
                }
                if ((!cbs.getTxtCriterioCTAM().isEmpty())) {
                    try {
                        cbs.setCriterioCTAM(Short.parseShort(cbs.getTxtCriterioCTAM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La CTAM debe ser un campo numérico..."));
                        cbs.setCriterioCTAM(null);
                        cbs.setTxtCriterioCTAM(null);
                    }
                } else {
                    cbs.setCriterioCTAM(null);
                }
                if ((!cbs.getTxtCriterioSC1().isEmpty())) {
                    try {
                        cbs.setCriterioSC1(Short.parseShort(cbs.getTxtCriterioSC1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC1 debe ser un campo numérico..."));
                        cbs.setCriterioSC1(null);
                        cbs.setTxtCriterioSC1(null);
                    }
                } else {
                    cbs.setCriterioSC1(null);
                }
                if ((!cbs.getTxtCriterioSC2().isEmpty())) {
                    try {
                        cbs.setCriterioSC2(Short.parseShort(cbs.getTxtCriterioSC2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC2 debe ser un campo numérico..."));
                        cbs.setCriterioSC2(null);
                    }
                } else {
                    cbs.setCriterioSC2(null);
                }
                if ((!cbs.getTxtCriterioSC3().isEmpty())) {
                    try {
                        cbs.setCriterioSC3(Short.parseShort(cbs.getTxtCriterioSC3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC3 debe ser un campo numérico..."));
                        cbs.setCriterioSC3(null);
                        cbs.setTxtCriterioSC3(null);
                    }
                } else {
                    cbs.setCriterioSC3(null);
                }
                if ((!cbs.getTxtCriterioSC4().isEmpty())) {
                    try {
                        cbs.setCriterioSC4(Short.parseShort(cbs.getTxtCriterioSC4()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC4 debe ser un campo numérico..."));
                        cbs.setCriterioSC4(null);
                        cbs.setTxtCriterioSC4(null);
                    }
                } else {
                    cbs.setCriterioSC4(null);
                }
                if ((!cbs.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cbs.setCriterioAX1(Long.parseLong(cbs.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbs.setCriterioAX1(null);
                        cbs.setTxtCriterioAX1(null);
                    }
                } else {
                    cbs.setCriterioAX1(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                }
                if ((!cbs.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cbs.setCriterioAX2(Long.parseLong(cbs.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico..."));
                        cbs.setCriterioAX2(null);
                        cbs.setTxtCriterioAX2(null);
                    }
                } else {
                    cbs.setCriterioAX2(null);
                }
                if ((!cbs.getTxtCriterioMes().isEmpty())) {
                    try {
                        cbs.setCriterioMes(Short.parseShort(cbs.getTxtCriterioMes()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  Mes debe ser un campo numérico..."));
                        cbs.setCriterioMes(null);
                        cbs.setTxtCriterioMes(null);
                    }
                } else {
                    cbs.setCriterioMes(null);
                }
                if ((!cbs.getTxtCriterioAño().isEmpty())) {
                    try {
                        cbs.setCriterioAño(Short.parseShort(cbs.getTxtCriterioAño()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Año debe ser un campo numérico..."));
                        cbs.setCriterioAño(null);
                        cbs.setTxtCriterioAño(null);
                    }
                } else {
                    cbs.setCriterioAño(null);
                }
                if (((cbs.getCriterioMonedaNom() == null) || (cbs.getCriterioMonedaNom().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
                }

                if ((cbs.getCriterioAX1() != null)) {
                    if (onVerificaAtencionContrato(cbs.getCriterioAX1()).equals(Boolean.FALSE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                    }
                }
                if (mensajesDeError.isEmpty()) {
                    if (cbs.getCriterioTipo().equals("HST")) {
                        if ((cbs.getCriterioAño() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Año no puede estar vacío..."));
                        }
                        if ((cbs.getCriterioMes() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Mes no puede estar vacío..."));
                        }
                    }
                    if (mensajesDeError.isEmpty()) {
                        if (balanceInquiries != null) {
                            oContabilidad = new CContabilidad();

                            if ((cbs.getCriterioTipo().equals("ACT")) || (cbs.getCriterioTipo().equals("MSA"))) {
                                consultaSaldo = oContabilidad.onConsSaldo_Consulta(cbs);
                            }
                            if (cbs.getCriterioTipo().equals("HST")) {
                                consultaSaldo = oContabilidad.onConsSaldo_ConsultaHistorico(cbs);
                            }
                            archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(usuarioNumero.toString()).concat("_").concat("consultaSaldos.txt");
                            archivo = new File(archivoNombre);
                            fos = new FileOutputStream(archivo);
                            archivoLinea = "Consulta de Saldos \n";
                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                            archivoLinea = "CTAM\tS1\tS2\tS3\tS4\tMda\tNombre\tAX1\tAX2\tAX3\tInicial\tCargos\tAbonos\tFinal\tAño\tMes\tUlt. Mod.\n";
                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                            for (int item = 0; item <= consultaSaldo.size() - 1; item++) {
                                archivoLinea = consultaSaldo.get(item).getSaldoCTAM().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoSC1().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoSC2().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoSC3().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoSC4().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoMonedaNombre().concat("\t")
                                        + consultaSaldo.get(item).getSaldoCuentaNombre().concat("\t")
                                        + consultaSaldo.get(item).getSaldoAX1().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoAX2().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoAX3().toString().concat("\t")
                                        + "$".concat(formatDecimal2D(consultaSaldo.get(item).getSaldoInicioMes()).concat("\t"))
                                        + "$".concat(formatDecimal2D(consultaSaldo.get(item).getSaldoCargosMes()).concat("\t"))
                                        + "$".concat(formatDecimal2D(consultaSaldo.get(item).getSaldoAbonosMes()).concat("\t"))
                                        + "$".concat(formatDecimal2D(consultaSaldo.get(item).getSaldoActual()).concat("\t"))
                                        + consultaSaldo.get(item).getSaldoAño().toString().concat("\t")
                                        + consultaSaldo.get(item).getSaldoMes().toString().concat("\t")
                                        + formatFecha(consultaSaldo.get(item).getSaldoFechaUltMod()).concat("\n");

                                buffer = archivoLinea.getBytes();
                                fos.write(buffer);

                            }
                            consultaSaldo = null;
                            fos.close();
                            FacesContext.getCurrentInstance().getExternalContext().redirect(usuarioAppCtx.concat("/SArchivoDescarga?".concat(archivo.getName())));
                        } else {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No es posible exportar una consulta sin registros"));
                        }
                    }
                }
            } catch (IOException Err) {
                logger.error("onContabilidadConsSaldo_Expporta()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsSaldo_Limpia() {
            consultaSaldo = null;
            balanceInquiries = null;
            seleccionaSaldo = null;
            cbs.setCriterioAX1(null);
            cbs.setCriterioAX2(null);
            cbs.setCriterioAX3(null);
            cbs.setCriterioCTAM(null);
            cbs.setCriterioSC1(null);
            cbs.setCriterioSC2(null);
            cbs.setCriterioSC3(null);
            cbs.setCriterioSC4(null);
            cbs.setCriterioAño(null);
            cbs.setCriterioMes(null);
            cbs.setCriterioMonedaNom(null);
            cbs.setCriterioTipo("ACT");
            cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
            cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
            cbs.setCriterioMonedaNom("MONEDA NACIONAL");
            cbs.setCriterioNomContrato(null);

            cbs.setTxtCriterioAX1(null);
            cbs.setTxtCriterioAX2(null);
            cbs.setTxtCriterioAX3(null);
            cbs.setTxtCriterioCTAM(null);
            cbs.setTxtCriterioSC1(null);
            cbs.setTxtCriterioSC2(null);
            cbs.setTxtCriterioSC3(null);
            cbs.setTxtCriterioSC4(null);
            cbs.setTxtCriterioUsuario(null);
            cbs.setTxtCriterioMes(cbs.getCriterioMes().toString());
            cbs.setTxtCriterioAño(cbs.getCriterioAño().toString());

            geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
        }

        public void onContabilidadConsSaldo_SeleccionaDestino() {
            cbs.setCriterioAño(null);
            cbs.setCriterioMes(null);
            geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
            if ((cbs.getCriterioTipo() != null) && (!cbs.getCriterioTipo().equals(new String()))) {
                if (cbs.getCriterioTipo().equals("HST")) {
                    cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaHSTAño")));
                    cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaHSTMes")));
                    cbs.setTxtCriterioAño(cbs.getCriterioAño().toString());
                    cbs.setTxtCriterioMes(cbs.getCriterioMes().toString());
                    geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                } else {
                    if (cbs.getCriterioTipo().equals("MSA")) {
                        cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAño")));
                        cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAMes")));
                        cbs.setTxtCriterioAño(cbs.getCriterioAño().toString());
                        cbs.setTxtCriterioMes(cbs.getCriterioMes().toString());

                    } else {
                        cbs.setCriterioAño(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                        cbs.setCriterioMes(Short.parseShort((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                        cbs.setTxtCriterioAño(cbs.getCriterioAño().toString());
                        cbs.setTxtCriterioMes(cbs.getCriterioMes().toString());

                    }
                }
            }
        }

        public void onContabilidadConsSaldo_SeleccionaCuenta() {
            try {
                String fechaIni = new String();
                String fechaFin = new String();
                oContrato = new CContrato();
                contrato.setContratoNombre(oContrato.onContrato_ObtenNombre(seleccionaSaldo.getSaldoAX1()));
                oContrato = null;
                cba.setCriterioAX1(seleccionaSaldo.getSaldoAX1());
                cba.setCriterioAX2(seleccionaSaldo.getSaldoAX2());
                cba.setCriterioAX3(seleccionaSaldo.getSaldoAX3());
                cba.setCriterioCTAM(seleccionaSaldo.getSaldoCTAM());
                cba.setCriterioSC1(seleccionaSaldo.getSaldoSC1());
                cba.setCriterioSC2(seleccionaSaldo.getSaldoSC2());
                cba.setCriterioSC3(seleccionaSaldo.getSaldoSC3());
                cba.setCriterioSC4(seleccionaSaldo.getSaldoSC4());
                cba.setCriterioMonedaNum(seleccionaSaldo.getSaldoMonedaNumero());
                //cba.setCriterioStatus("ACTIVO");
                if (cbs.getCriterioTipo().equals("ACT")) {
                    fechaIni = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema").toString();
                    fechaIni = fechaIni.replace(fechaIni.substring(0, 2), "01");
                    //System.out.println("Fecha Inicial: ".concat(fechaIni));
                    cba.setCriterioFechaDel(new java.sql.Date(formatFechaParse(fechaIni).getTime()));
                    cba.setCriterioFechaAl(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                }
                if (cbs.getCriterioTipo().equals("MSA")) {
                    fechaIni = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSA").toString();
                    fechaIni = fechaIni.replace(fechaIni.substring(0, 2), "01");
                    //System.out.println("Fecha Inicial: ".concat(fechaIni));
                    cba.setCriterioFechaDel(new java.sql.Date(formatFechaParse(fechaIni).getTime()));
                    cba.setCriterioFechaAl(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSA")).getTime()));
                }
                if (cbs.getCriterioTipo().equals("HST")) {
                    fechaIni = "01/".concat(cbs.getCriterioMes().toString()).concat("/").concat(cbs.getCriterioAño().toString());
                    oComunes = new CComunes();
                    fechaFin = oComunes.onComunes_ObtenUltimoDiaMesDesdeControla(cbs.getCriterioAño(), cbs.getCriterioMes()).toString().concat("/").concat(cbs.getCriterioMes().toString()).concat("/").concat(cbs.getCriterioAño().toString());
                    oComunes = null;
                    cba.setCriterioFechaDel(new java.sql.Date(formatFechaParse(fechaIni).getTime()));
                    cba.setCriterioFechaAl(new java.sql.Date(formatFechaParse(fechaFin).getTime()));
                }
                oContabilidad = new CContabilidad();
                consultaAsiento = oContabilidad.onConsAsiento_ConsultaPorRangoFechas(cba);
                oContabilidad = null;
                RequestContext.getCurrentInstance().execute("dlgSaldoDet.show();");
            } catch (SQLException Err) {
                logger.error("onContabilidadConsSaldo_SeleccionaCuenta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
        }

        public void onConsutaSaldos_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioCTAM":
                    if (!"".equals(cbs.getTxtCriterioCTAM()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioCTAM(), "CTAM", "S")) {
                        cbs.setCriterioCTAM(Short.parseShort(cbs.getTxtCriterioCTAM()));
                    } else {
                        cbs.setCriterioCTAM(null);
                        cbs.setTxtCriterioCTAM(null);
                    }
                    break;
                case "txtCriterioSC1":
                    if (!"".equals(cbs.getTxtCriterioSC1()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioSC1(), "SC1", "S")) {
                        cbs.setCriterioSC1(Short.parseShort(cbs.getTxtCriterioSC1()));
                    } else {
                        cbs.setCriterioSC1(null);
                        cbs.setTxtCriterioSC1(null);
                    }
                    break;
                case "txtCriterioSC2":
                    if (!"".equals(cbs.getTxtCriterioSC2()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioSC2(), "SC2", "S")) {
                        cbs.setCriterioSC2(Short.parseShort(cbs.getTxtCriterioSC2()));
                    } else {
                        cbs.setCriterioSC2(null);
                        cbs.setTxtCriterioSC2(null);
                    }
                    break;
                case "txtCriterioSC3":
                    if (!"".equals(cbs.getTxtCriterioSC3()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioSC3(), "SC3", "S")) {
                        cbs.setCriterioSC3(Short.parseShort(cbs.getTxtCriterioSC3()));
                    } else {
                        cbs.setCriterioSC3(null);
                        cbs.setTxtCriterioSC3(null);
                    }
                    break;
                case "txtCriterioSC4":
                    if (!"".equals(cbs.getTxtCriterioSC4()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioSC4(), "SC4", "S")) {
                        cbs.setCriterioSC4(Short.parseShort(cbs.getTxtCriterioSC4()));
                    } else {
                        cbs.setCriterioSC4(null);
                        cbs.setTxtCriterioSC4(null);
                    }
                    break;
                case "txtCriterioAX1":
                    if (!"".equals(cbs.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioAX1(), "Fideicomiso", "L")) {
                        cbs.setCriterioAX1(Long.parseLong(cbs.getTxtCriterioAX1()));
                    } else {
                        cbs.setCriterioAX1(null);
                        cbs.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cbs.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioAX2(), "SubFiso", "L")) {
                        cbs.setCriterioAX2(Long.parseLong(cbs.getTxtCriterioAX2()));
                    } else {
                        cbs.setCriterioAX2(null);
                        cbs.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioMes":
                    if (!"".equals(cbs.getTxtCriterioMes()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioMes(), "Mes", "S")) {
                        cbs.setCriterioMes(Short.parseShort(cbs.getTxtCriterioMes()));
                    } else {
                        cbs.setCriterioMes(null);
                        cbs.setTxtCriterioMes(null);
                    }
                    break;
                case "txtCriterioAño":
                    if (!"".equals(cbs.getTxtCriterioAño()) && oContabilidadGrales.onValidaNumerico(cbs.getTxtCriterioAño(), "Año", "S")) {
                        cbs.setCriterioAño(Short.parseShort(cbs.getTxtCriterioAño()));
                    } else {
                        cbs.setCriterioAño(null);
                        cbs.setTxtCriterioAño(null);
                    }
                    break;
            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

    }

    class contabilidadBienFide {

        public contabilidadBienFide() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadBienFide.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadBienFide_ConsultaEjecuta() {
            try {
                    getTrustProperty = null;
                    consultaBienFide = null;
                    seleccionaBienFide = null;
                cbc.setCriterioAX1(null);
                cbc.setCriterioAX2(null);
                cbc.setCriterioPlaza(null);
                List<FacesMessage> mensajesDeError = new ArrayList<>();

                cbc.setTxtCriterioAX1(cbc.getTxtCriterioAX1().trim());
                if ((!cbc.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico…"));
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                }
                cbc.setTxtCriterioAX2(cbc.getTxtCriterioAX2().trim());
                if ((!cbc.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico…"));
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                }
                cbc.setTxtCriterioPlaza(cbc.getTxtCriterioPlaza().trim());
                if ((!cbc.getTxtCriterioPlaza().isEmpty())) {
                    try {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico…"));
                        cbc.setCriterioPlaza(null);
                        cbc.setTxtCriterioPlaza(null);
                    }
                }

                if (mensajesDeError.isEmpty()) {

                    getTrustProperty = null;
                    consultaBienFide = null;
                    //listaBienFideClas = null;
                    seleccionaBienFide = null;

                    if (validacion.equals(Boolean.TRUE)) {
                        usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                        cbc.setCriterioUsuario(usuarioNumero);
                        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmBienFide:dtBienFide");
                        dataTable.setFirst(0);

                        oContabilidad = new CContabilidad();
                        int totalRows = oContabilidad.getTrustPropertyTotalRows(cbc);
                        oContabilidad = null;

                        ContabilidadBienFideBean bean = new ContabilidadBienFideBean();
                        MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName());
                        getTrustProperty = lazyModel.getLazyDataModel(cbc);
                        getTrustProperty.setRowCount(totalRows);

                        if (getTrustProperty.getRowCount() == 0) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados...");
                        }
                    }
                } else {

                    for (FacesMessage mensaje : mensajesDeError) {
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }
            } catch (Error Err) {
                logger.error("onContabilidadBienFide_ConsultaEjecuta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_ConsultaLimpia() {
            onInicializaCriterios();
        }

        public void onContabilidadBienFide_CargaClasificacionBien(String bienFideTipo) {
            try {
                listaBienFideClas = null;
                if ((bienFideTipo != null) && (!bienFideTipo.equals(new String()))) {
                    oClave = new CClave();
                    if (bienFideTipo.equals("INMUEBLES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(71);
                    }
                    if (bienFideTipo.equals("MUEBLES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(72);
                    }
                    if (bienFideTipo.equals("OTROS BIENES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(73);
                    }
                    oClave = null;

                    oContabilidad = new CContabilidad();
                    if (bienFide.getBienFideContratoNum() != null && bienFide.getBienFideContratoNumSub() != null) {
                        bienFide.setBienFideId(oContabilidad.onBienFide_ObtenBienId(bienFide.getBienFideContratoNum(), bienFide.getBienFideContratoNumSub(), bienFideTipo));
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Primero debe Ingresar un Fideicomiso y Subfiso");
                    }

                    oContabilidad = null;
                }
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFide_CargaClasificacionBien()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        public void onContabilidadBienFide_CargaClasificacionBien_Criterio(String bienFideTipo) {
            try {
                listaBienFideClas = null;
                if ((bienFideTipo != null) && (!bienFideTipo.equals(new String()))) {
                    oClave = new CClave();
                    if (bienFideTipo.equals("INMUEBLES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(71);
                    }
                    if (bienFideTipo.equals("MUEBLES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(72);
                    }
                    if (bienFideTipo.equals("OTROS BIENES")) {
                        listaBienFideClas = oClave.onClave_ObtenListadoElementos(73);
                    }
                    oClave = null;
              }
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFide_CargaClasificacionBien_Criterio()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_RegistroBuscaTipoCambio() {
            try {
                bienFide.setBienFideImporteTC(1.00);
                setBienTipoCambiTxt(formatDecimal6D(bienFide.getBienFideImporteTC()));

                if ((bienFide.getBienFideMonedaNom() != null) && (!bienFide.getBienFideMonedaNom().equals(new String()))) {
                    if (!bienFide.getBienFideMonedaNom().equals("MONEDA NACIONAL")) {
                        oMoneda = new CMoneda();
                        if (oMoneda.onMonedaTC_VerificaSiHayTipoDeCambio(bienFide.getBienFideMonedaNom(),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia"))).equals(Boolean.FALSE)) {
                            //tipoCamb.setTipoCambioMonedaNum(oMoneda.onMoneda_ObtenMonedaId(bienFide.getBienFideMonedaNom()));
                            //tipoCamb.setTipoCambioFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                            //tipoCamb.setTipoCambioStatus("ACTIVO");
                            //tipoCamb.setTipoCambioTipoOperacion("REGISTRO");
                            //RequestContext.getCurrentInstance().execute("dlgTC.show();");
                            bienFide.setBienFideImporteTC(null);
                            setBienTipoCambiTxt(null);
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "¡No se encuentra tipo de cambio para este día!");
                        } else {
                            bienFide.setBienFideImporteTC(oMoneda.onMonedaTC_ObtenTipoCambio(bienFide.getBienFideMonedaNom(),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia"))));
                            setBienTipoCambiTxt(formatDecimal6D(bienFide.getBienFideImporteTC()));
                        }
                        oMoneda = null;
                    }
                }
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadBienFide_RegistroBuscaTipoCambio()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadGarantia_RegistroBuscaTipoCambio() {
            try {
                garantiaBien.setGarantiaBienImporteTC(1.00);
                setBienTipoCambiTxt(formatDecimal6D(garantiaBien.getGarantiaBienImporteTC()));

                if ((garantiaBien.getGarantiaBienMonedaNom() != null) && (!garantiaBien.getGarantiaBienMonedaNom().equals(new String()))) {
                    if (!garantiaBien.getGarantiaBienMonedaNom().equals("MONEDA NACIONAL")) {
                        oMoneda = new CMoneda();
                        if (oMoneda.onMonedaTC_VerificaSiHayTipoDeCambio(garantiaBien.getGarantiaBienMonedaNom(),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")),
                                Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia"))).equals(Boolean.FALSE)) {
                            //tipoCamb.setTipoCambioMonedaNum(oMoneda.onMoneda_ObtenMonedaId(bienFide.getBienFideMonedaNom()));
                            //tipoCamb.setTipoCambioFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                            //tipoCamb.setTipoCambioStatus("ACTIVO");
                            //tipoCamb.setTipoCambioTipoOperacion("REGISTRO");
                            //RequestContext.getCurrentInstance().execute("dlgTC.show();");
                            garantiaBien.setGarantiaBienMonedaNom("...");
                            garantiaBien.setGarantiaBienMonedaNum(null);
                            garantiaBien.setGarantiaBienImporteTC(null);
                            setBienTipoCambiTxt(null);
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "¡No se encuentra tipo de cambio para este día!");
                        } else {
                            garantiaBien.setGarantiaBienImporteTC(oMoneda.onMonedaTC_ObtenTipoCambio(garantiaBien.getGarantiaBienMonedaNom(),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")),
                                    Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia"))));
                            setBienTipoCambiTxt(formatDecimal6D(garantiaBien.getGarantiaBienImporteTC()));

                        }
                        oMoneda = null;
                    }
                }
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadBienFide_RegistroBuscaTipoCambio()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_RegistroBuscaCtoNom() {
            try {
                bienFide.setBienFideContratoNumSub(null);
                setBienSubFisoTxt(null);
                bienFide.setTxtBienFideImporteNvo(null);
                bienFide.setBienFideTipo(null);
                bienFide.setBienFideClasificacion(null);
                bienFide.setBienFideId(null);
                setBienImporteTxt(null);
                setBienTipoCambiTxt(null);
                setBienImporteNvoTxt(null);
                bienFide.setBienFideBitPantalla("contabilidadBienFide");
                bienFide.setBienFideBitTerminal(usuarioTerminal);
                bienFide.setBienFideBitUsuario(usuarioNumero);
                bienFide.setBienFideClasificacion(null);
                bienFide.setBienFideComentario(null);
                bienFide.setBienFideContratoNom(null);
                bienFide.setBienFideContratoNumSub(null);
                bienFide.setBienFideDescripcion(null);
                bienFide.setBienFideFechaMod(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaValor(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaValProx(null);
                bienFide.setBienFideFechaValUlt(null);
                bienFide.setBienFideId(null);
                bienFide.setBienFideImporte(0.0);
                listaContratoSub = new LinkedHashMap<>();
                bienFide.setTxtBienFideImporte(null);
                bienFide.setTxtBienFideImporteNvo(null);
                bienFide.setBienFideImporteNvo(0.00);
                bienFide.setBienFideImporteTC(1.00);
                bienFide.setBienFideImporteUltVal(0.00);
                bienFide.setBienFideMonedaNom(null);
                bienFide.setBienFideMonedaNum(null);
                bienFide.setBienFidePeriodicidadRev(null);
                bienFide.setBienFideStatus("ACTIVO");
                bienFide.setBienFideTipo(null);
                bienFide.setBienFideBitTipoOperacion("ENTRADA");
                geneFecha.setGenericoFecha00(null);
                geneFecha.setGenericoFecha01(null);
                listaSubCont = new ArrayList<>();
                listaContratoSub = new LinkedHashMap<>();
                oContabilidadGrales = new contabilidadGrales();
                if (!"".equals(BienFideicomisoTxt) && oContabilidadGrales.onValidaNumerico(BienFideicomisoTxt, "Fideicomiso", "L")) {

                    bienFide.setBienFideContratoNum(Long.parseLong(BienFideicomisoTxt));
                    oContrato = new CContrato();
                    if (oContrato.onContrato_VerificaAtencion(usuarioFiltro, bienFide.getBienFideContratoNum()).equals(Boolean.TRUE)) {
                        bienFide.setBienFideContratoNom(oContrato.onContrato_ObtenNombre(bienFide.getBienFideContratoNum()));
                        if (oContrato.onContrato_VerificaSiTieneSubFisos(bienFide.getBienFideContratoNum(), listaSubCont).equals(Boolean.FALSE)) {

                            listaContratoSub.put(0, "0");
                            bienFide.setBienFideContratoNumSub(0);
                            setBienSubFisoTxt("0");
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso sin Sub Fideicomisos");
                            geneDes.setGenericoDesHabilitado101(Boolean.TRUE);
                        } else {
                            oContrato = new CContrato();
                            listaContratoSub = oContrato.onContrato_ObtenMapContratoSub(bienFide.getBienFideContratoNum(), "ACTIVO");
                            oContrato = null;
                            geneDes.setGenericoDesHabilitado101(Boolean.FALSE);
                            bienFide.setBienFideContratoNumSub(0);
                            setBienSubFisoTxt("0");
                        }
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                        bienFide.setBienFideContratoNum(null);
                        BienFideicomisoTxt = null;
                        bienFide.setBienFideContratoNom(null);
                    }
                    oContrato = null;

                } else {
                    BienFideicomisoTxt = null;
                    bienFide.setBienFideContratoNom(null);
                }
                oContabilidadGrales = null;

            } catch (SQLException | NumberFormatException Err) {
                logger.error("onContabilidadBienFide_RegistroBuscaCtoNom()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_RegistroVerificaSubFiso() {
            try {
                oContabilidadGrales = new contabilidadGrales();
                bienFide.setBienFideTipo(null);
                bienFide.setBienFideClasificacion(null);
                bienFide.setBienFideId(null);
                setBienImporteTxt(null);
                setBienTipoCambiTxt(null);
                setBienImporteNvoTxt(null);
                bienFide.setBienFideBitPantalla("contabilidadBienFide");
                bienFide.setBienFideBitTerminal(usuarioTerminal);
                bienFide.setBienFideBitUsuario(usuarioNumero);
                bienFide.setBienFideClasificacion(null);
                bienFide.setBienFideComentario(null);
                bienFide.setBienFideDescripcion(null);
                bienFide.setBienFideFechaMod(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaValor(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                bienFide.setBienFideFechaValProx(null);
                bienFide.setBienFideFechaValUlt(null);
                bienFide.setBienFideId(null);
                bienFide.setBienFideImporte(0.0);
                bienFide.setTxtBienFideImporte(null);
                bienFide.setTxtBienFideImporteNvo(null);
                bienFide.setBienFideImporteNvo(0.00);
                bienFide.setBienFideImporteTC(1.00);
                bienFide.setBienFideImporteUltVal(0.00);
                bienFide.setBienFideMonedaNom(null);
                bienFide.setBienFideMonedaNum(null);
                bienFide.setBienFidePeriodicidadRev(null);
                bienFide.setBienFideStatus("ACTIVO");
                bienFide.setBienFideTipo(null);
                bienFide.setBienFideBitTipoOperacion("ENTRADA");
                geneFecha.setGenericoFecha00(null);
                geneFecha.setGenericoFecha01(null);

                if (!"".equals(getBienSubFisoTxt()) && oContabilidadGrales.onValidaNumerico(getBienSubFisoTxt(), "SubFiso", "L")) {
                    bienFide.setBienFideContratoNumSub(Integer.parseInt(getBienSubFisoTxt()));
                }
                oContabilidadGrales = null;
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadBienFide_RegistroVerificaSubFiso()");
            }
        }

        public void onContabilidadBienFide_RegistroAplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();

            try {
                if (getBienFideicomisoTxt().isEmpty()) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                } else {
                    bienFide.setBienFideContratoNum(Long.parseLong(getBienFideicomisoTxt().trim()));
                }
                if (getBienSubFisoTxt() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Sub Fideicomiso no puede estar vacío..."));
                } else {
                    bienFide.setBienFideContratoNumSub(Integer.parseInt(getBienSubFisoTxt()));
                }
                if ((bienFide.getBienFideTipo() == null) || (bienFide.getBienFideTipo().equals(Boolean.TRUE))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Bien (Tipo) no puede estar vacío..."));
                }
                if ((bienFide.getBienFideClasificacion() == null) || (bienFide.getBienFideClasificacion().equals(Boolean.TRUE))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Bien (Clasif.) no puede estar vacío..."));
                }
                if ((bienFide.getTxtBienFideImporte() == null) || (bienFide.getTxtBienFideImporte().equals(new String()))) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe no puede estar vacío..."));
                }
                if ((!bienFide.getTxtBienFideImporte().isEmpty())) {
                    try {
                        bienFide.setTxtBienFideImporte(bienFide.getTxtBienFideImporte().replaceAll(",", ""));
                        bienFide.setTxtBienFideImporte(bienFide.getTxtBienFideImporte().replace("$", ""));

                        bienFide.setBienFideImporte(Double.parseDouble(bienFide.getTxtBienFideImporte()));
                        bienFide.setTxtBienFideImporte(formatImporte2D(bienFide.getBienFideImporte()));
                        if (bienFide.getBienFideImporte() <= 0) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe debe ser mayor a CER0..."));
                            bienFide.setBienFideImporte(null);
                            bienFide.setTxtBienFideImporte(null);
                        }
                        if (bienFide.getBienFideImporte() > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe no puede ser mayor a 999,999,999,999.99..."));
                            bienFide.setBienFideImporte(null);
                            bienFide.setTxtBienFideImporte(null);
                        }
                    } catch (NumberFormatException e) {

                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe debe ser un campo numérico…"));
                        bienFide.setBienFideImporte(null);
                        bienFide.setTxtBienFideImporte(null);
                    }
                }
                if ((bienFide.getBienFidePeriodicidadRev() == null)) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Periodicidad no puede estar vacío..."));

                }
                if (geneFecha.getGenericoFecha00() == null) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha última val. no puede estar vacío..."));
                }
                if (geneFecha.getGenericoFecha01() == null) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha próxima val. no puede estar vacío..."));
                }
// 2024/01/24 Cambios
//                if (geneFecha.getGenericoFecha00() != null && geneFecha.getGenericoFecha01() != null) {
//                    int validaFecha = geneFecha.getGenericoFecha01().compareTo(geneFecha.getGenericoFecha00());
//                    if (validaFecha <= 0) {
//                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Fecha Fecha última val. debe ser menor a  " + formatFecha(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()))));
//                    }
//                }

                if ((bienFide.getBienFideMonedaNom() == null) || (bienFide.getBienFideMonedaNom().equals(new String()))) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Moneda no puede estar vacío..."));
                }
                if (getBienTipoCambiTxt() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo de Cambio no puede estar vacío..."));
                }
                if ((bienFide.getBienFideDescripcion() == null) || (bienFide.getBienFideDescripcion().equals(new String()))) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Descripción no puede estar vacío..."));
                }
                if ((bienFide.getBienFideComentario() == null) || (bienFide.getBienFideComentario().equals(new String()))) {

                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Comentario no puede estar vacío..."));
                }
                if (bienFide.getBienFideBitTipoOperacion().equals("REVALUACION")) {
                    if ((!bienFide.getTxtBienFideImporteNvo().isEmpty())) {
                        try {
                            bienFide.setTxtBienFideImporteNvo(bienFide.getTxtBienFideImporteNvo().replaceAll(",", ""));
                            bienFide.setTxtBienFideImporteNvo(bienFide.getTxtBienFideImporteNvo().replace("$", ""));

                            bienFide.setBienFideImporte(Double.parseDouble(bienFide.getTxtBienFideImporteNvo()));
                            bienFide.setTxtBienFideImporteNvo(formatImporte2D(bienFide.getBienFideImporteNvo()));

                            if (bienFide.getBienFideImporte() > VALOR_MAX) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe nuevo  no puede ser mayor a 999,999,999,999.99..."));
                                bienFide.setBienFideImporte(null);
                                bienFide.setTxtBienFideImporteNvo(null);
                            }
                        } catch (NumberFormatException e) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Importe nuevo debe ser un campo numérico…"));
                            bienFide.setBienFideImporte(null);
                            bienFide.setTxtBienFideImporteNvo(null);
                        }
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Importe nuevo no puede estar vacío..."));
                        bienFide.setBienFideImporte(null);
                        bienFide.setTxtBienFideImporteNvo(null);
                    }
                }

                if (mensajesDeError.isEmpty()) {
                    if (!bienFide.getBienFideMonedaNom().equals("MONEDA NACIONAL")) {
                        if (bienFide.getBienFideImporteTC() <= 0.00) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor del campo Tipo de Cambio debe ser superior a 0.00");
                        }
                    }
                    if (validacion.equals(Boolean.TRUE)) {
                        //if (bienFide.getBienFideImporteUltVal() == null) bienFide.setBienFideImporteUltVal(0.00);
                        oMoneda = new CMoneda();
                        bienFide.setBienFideMonedaNum(oMoneda.onMoneda_ObtenMonedaId(bienFide.getBienFideMonedaNom()));
                        bienFide.setBienFideFechaValUlt(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                        bienFide.setBienFideFechaValProx(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()));
                        oMoneda = null;

                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFide_Administra(bienFide).equals(Boolean.TRUE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                            onInicializaCriterios();
                            cbc.setCriterioAX1(bienFide.getBienFideContratoNum());
                            cbc.setTxtCriterioAX1(bienFide.getBienFideContratoNum().toString());
//                            consultaBienFide = oContabilidad.onBienFide_Consulta(cbc);
                            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                            cbc.setCriterioUsuario(usuarioNumero);
                            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmBienFide:dtBienFide");
                            dataTable.setFirst(0);

                            oContabilidad = new CContabilidad();
                            int totalRows = oContabilidad.getTrustPropertyTotalRows(cbc);
                            oContabilidad = null;

                            ContabilidadBienFideBean bean = new ContabilidadBienFideBean();
                            MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName());
                            getTrustProperty = lazyModel.getLazyDataModel(cbc);
                            getTrustProperty.setRowCount(totalRows);

                            RequestContext.getCurrentInstance().execute("dlgBienFide.hide();");
                            RequestContext.getCurrentInstance().execute("dlgBienFideRev.hide();");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                        }
                        oContabilidad = null;
                    }
                } else {
                    for (FacesMessage mensaje : mensajesDeError) {
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }
            } finally {
                onFinalizaObjetos();
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onContabilidadBienFide_RegistroSelecciona() {
            try {
                onInicializaBienFide();

                bienFide.setBienFideContratoNum(seleccionaBienFide.getBienFideContratoNum());

                oContrato = new CContrato();
                bienFide.setBienFideContratoNom(oContrato.onContrato_ObtenNombre(bienFide.getBienFideContratoNum()));
                //Consulta SubFiso
                oContrato = null;
                bienFide.setBienFideContratoNumSub(seleccionaBienFide.getBienFideContratoNumSub());
                onContabilidadBienFide_SelectSubFiso();

                oContrato = null;
                bienFide.setBienFideTipo(seleccionaBienFide.getBienFideTipo());
                oClave = new CClave();
                if (bienFide.getBienFideTipo().equals("INMUEBLES")) {
                    listaBienFideClas = oClave.onClave_ObtenListadoElementos(71);
                }
                if (bienFide.getBienFideTipo().equals("MUEBLES")) {
                    listaBienFideClas = oClave.onClave_ObtenListadoElementos(72);
                }
                if (bienFide.getBienFideTipo().equals("OTROS BIENES")) {
                    listaBienFideClas = oClave.onClave_ObtenListadoElementos(73);
                }
                listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                oClave = null;
                bienFide.setBienFideClasificacion(seleccionaBienFide.getBienFideClasificacion());
                bienFide.setBienFideImporte(seleccionaBienFide.getBienFideImporte());
                bienFide.setTxtBienFideImporte(formatImporte2D(seleccionaBienFide.getBienFideImporte()));
                bienFide.setBienFideImporteUltVal(seleccionaBienFide.getBienFideImporteUltVal());
                bienFide.setBienFideMonedaNom(seleccionaBienFide.getBienFideMonedaNom());
                bienFide.setBienFideMonedaNum(seleccionaBienFide.getBienFideMonedaNum());

                oMoneda = new CMoneda();
                listaMoneda = oMoneda.onMoneda_ObtenListadoMonedas();
                if (!bienFide.getBienFideMonedaNom().equals("MONEDA NACIONAL")) {
                    calendario = Calendar.getInstance();
                    calendario.setTime(new java.util.Date(seleccionaBienFide.getBienFideFechaReg().getTime()));
                    bienFide.setBienFideImporteTC(oMoneda.onMonedaTC_ObtenTipoCambio(bienFide.getBienFideMonedaNom(), calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH) + 1, calendario.get(Calendar.DAY_OF_MONTH)));
                }
                oMoneda = null;

                bienFide.setBienFidePeriodicidadRev(seleccionaBienFide.getBienFidePeriodicidadRev());
                bienFide.setBienFideId(seleccionaBienFide.getBienFideId());
                geneFecha.setGenericoFecha00(new java.util.Date(seleccionaBienFide.getBienFideFechaValUlt().getTime()));
                if (seleccionaBienFide.getBienFideFechaValProx() != null) {
                    geneFecha.setGenericoFecha01(new java.util.Date(seleccionaBienFide.getBienFideFechaValProx().getTime()));
                }

                bienFide.setBienFideStatus(seleccionaBienFide.getBienFideStatus());
                //bienFide.setBienFideFechaReg(rs.getDate("FecReg"));
                bienFide.setBienFideComentario(seleccionaBienFide.getBienFideComentario());
                bienFide.setBienFideDescripcion(seleccionaBienFide.getBienFideDescripcion());
                //Proteje Todos los Campos
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado100(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado101(Boolean.TRUE);
                // Desproteje Boton Aplicar Proje botones Modificar y Tipo de Movimiento

                if (permisoEscrituraBienes.equals(Boolean.TRUE)) {
                    geneDes.setGenericoMenuDesHabilitado00(Boolean.FALSE);
                    geneDes.setGenericoMenuDesHabilitado01(Boolean.TRUE);
                } else {
                    geneDes.setGenericoMenuDesHabilitado00(Boolean.TRUE);
                    geneDes.setGenericoMenuDesHabilitado01(Boolean.TRUE);
                }

                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("dlgBienFide.show();");

                setBienFideicomisoTxt(bienFide.getBienFideContratoNum().toString());
                //setBienSubFisoTxt(bienFide.getBienFideContratoNumSub().toString());
                setBienImporteTxt(formatDecimal2D(bienFide.getBienFideImporte()));
                setBienTipoCambiTxt(formatDecimal6D(bienFide.getBienFideImporteTC()));

            } catch (Exception Err) {
                logger.error("onContabilidadBienFide_RegistroSelecciona()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_SelectSubFiso() {

            try {
                // Selecciona_Estado
                oContrato = new CContrato();
                listaContratoSub = new LinkedHashMap<>();
                listaContratoSub = oContrato.onContrato_ObtenMapContratoSub(bienFide.getBienFideContratoNum(), "ACTIVO");

                if (listaContratoSub.isEmpty()) {
                    listaContratoSub.put(0, "0");
                    setBienSubFisoTxt("0");
                    return;
                }

                for (Map.Entry<Integer, String> o : listaContratoSub.entrySet()) {
                    if (bienFide.getBienFideContratoNumSub() == o.getKey()) {
                        setBienSubFisoTxt(o.getKey().toString());
                        listaContratoSub.get(o.getKey());
                        break;
                    }
                }

            } catch (SQLException | ArrayIndexOutOfBoundsException exception) {
                mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " onContabilidadBienFide_SelectSubFiso()";
                logger.error(mensajeError);
            }
        }

        public void onContabilidadBienFide_RegistroModifica() {
            //Proteje Todos los Campos de modificación
            geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
            geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
            geneDes.setGenericoDesHabilitado100(Boolean.TRUE);
            geneDes.setGenericoDesHabilitado101(Boolean.TRUE);

            geneDes.setGenericoMenuDesHabilitado00(Boolean.FALSE);
            geneDes.setGenericoMenuDesHabilitado01(Boolean.FALSE);
            bienFide.setBienFideBitTipoOperacion("MODIFICAR");
        }

        public void onContabilidadBienFide_RegistroEntrada() {
            try {
                onInicializaBienFide();
                oClave = new CClave();
                listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                oClave = null;

                oMoneda = new CMoneda();
                listaMoneda = oMoneda.onMoneda_ObtenListadoMonedas();
                oMoneda = null;
                //Desporteje Todos los Campos
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado100(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado101(Boolean.FALSE);
                // Desproteje Boton Aplicar Proje botones Modificar y Tipo de Movimiento
                geneDes.setGenericoMenuDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoMenuDesHabilitado01(Boolean.FALSE);

                RequestContext.getCurrentInstance().execute("dlgBienFide.show();");
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFide_RegistroEntrada");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_RegistroEntradaForma() {
            try {
//                onInicializaBienFide(); 
                onInicializaBienFideEntradaForma();
                oClave = new CClave();
                listaPeriodicidad = oClave.onClave_ObtenListadoElementos(52);
                oClave = null;

                oMoneda = new CMoneda();
                listaMoneda = oMoneda.onMoneda_ObtenListadoMonedasSinCierre();
                oMoneda = null;

                geneDes.setGenericoMenuDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoMenuDesHabilitado01(Boolean.FALSE);

                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado100(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado101(Boolean.FALSE);
                RequestContext.getCurrentInstance().execute("dlgBienFide.show();");
//                execute_RegistroBuscaCtoNom();
            } catch (Exception Err) {
                logger.error("onContabilidadBienFide_RegistroEntradaForma");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFide_RegistroSalida() {
            mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
            mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la salida del Bien seleccionado?");
            mensajeConfrima.setMensajeConfirmaOrigen("bienFide");
            mensajeConfrima.setMensajeConfirmacionAccion("salida");
            onInicializaCriterios();
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
        }

        public void onContabilidadBienFide_RegistroRevalua() {
            bienFide.setBienFideBitTipoOperacion("REVALUACION");
            bienFide.setBienFideImporteNvo(0.00);
            bienFide.setTxtBienFideImporteNvo(null);
            bienFide.setBienFideImporteUltVal(bienFide.getBienFideImporteUltVal());
            RequestContext.getCurrentInstance().execute("dlgBienFideRev.show();");
        }

        //Funciones privadas
        private void onInicializaCriterios() {
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioBienFideClas(null);
            cbc.setCriterioBienFideTipo(null);
            cbc.setCriterioStatus(null);
            cbc.setCriterioPlaza(null);
            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioPlaza(null);
            getTrustProperty = null;
            consultaBienFide = null;
            listaBienFideClas = null;
            seleccionaBienFide = null;
        }

        private void onInicializaBienFide() {
            setBienFideicomisoTxt(null);
            setBienSubFisoTxt(null);
            setBienImporteTxt(null);
            setBienTipoCambiTxt(null);
            setBienImporteNvoTxt(null);
            bienFide.setBienFideBitPantalla("contabilidadBienFide");
            bienFide.setBienFideBitTerminal(usuarioTerminal);
            bienFide.setBienFideBitUsuario(usuarioNumero);
            bienFide.setBienFideClasificacion(null);
            bienFide.setBienFideComentario(null);
            bienFide.setBienFideContratoNom(null);
            bienFide.setBienFideContratoNum(null);
            bienFide.setBienFideContratoNumSub(null);
            bienFide.setBienFideDescripcion(null);
            bienFide.setBienFideFechaMod(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaValor(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaValProx(null);
            bienFide.setBienFideFechaValUlt(null);
            bienFide.setBienFideId(null);
            bienFide.setBienFideImporte(0.0);
            listaContratoSub = new LinkedHashMap<>();
            bienFide.setTxtBienFideImporte(null);
            bienFide.setTxtBienFideImporteNvo(null);
            bienFide.setBienFideImporteNvo(0.00);
            bienFide.setBienFideImporteTC(1.00);
            bienFide.setBienFideImporteUltVal(0.00);
            bienFide.setBienFideMonedaNom(null);
            bienFide.setBienFideMonedaNum(null);
            bienFide.setBienFidePeriodicidadRev(null);
            bienFide.setBienFideStatus("ACTIVO");
            bienFide.setBienFideTipo(null);
            bienFide.setBienFideBitTipoOperacion("ENTRADA");
            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);
        }

        private void onInicializaBienFideEntradaForma() {
            bienFide.setBienFideBitPantalla("contabilidadBienFide");
            bienFide.setBienFideBitTerminal(usuarioTerminal);
            bienFide.setBienFideBitUsuario(usuarioNumero);

            bienFide.setBienFideContratoNum(null);
            bienFide.setBienFideContratoNumSub(null);
            bienFide.setBienFideContratoNom(null);
            setBienFideicomisoTxt(null);
            setBienSubFisoTxt(null);
            bienFide.setBienFideTipo(null);
            bienFide.setBienFideClasificacion(null);
            bienFide.setBienFideId(null);
            bienFide.setBienFideImporte(0.00);
            listaContratoSub = new LinkedHashMap<>();
            bienFide.setBienFidePeriodicidadRev(null);
            bienFide.setBienFideComentario(null);
            bienFide.setBienFideDescripcion(null);
            bienFide.setTxtBienFideImporte(null);
            bienFide.setTxtBienFideImporteNvo(null);
            bienFide.setBienFideFechaMod(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaValor(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFide.setBienFideFechaValProx(null);
            bienFide.setBienFideFechaValUlt(null);
            bienFide.setBienFideImporteNvo(0.00);
            bienFide.setBienFideImporteTC(1.00);
            bienFide.setBienFideImporteUltVal(0.00);
            setBienImporteTxt(null);
            setBienTipoCambiTxt(null);
            bienFide.setBienFideMonedaNom(null);
            bienFide.setBienFideMonedaNum(null);
            bienFide.setBienFideStatus("ACTIVO");
            bienFide.setBienFideBitTipoOperacion("ENTRADA");
            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }
    }

    class contabilidadBienFideIndiv {

        public contabilidadBienFideIndiv() {
            formatoDecimal = new DecimalFormat("###,###,###,###,###.00");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadBienFideIndiv.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadBienFideIndiv_ConsultaEjecuta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                consultaBienFide = null;
                consultaBienFideU = null;
                seleccionaBienFideU = null;
                seleccionaBienFide = null;
                if (!cbc.getTxtCriterioAX1().isEmpty()) {
                    try {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }

                } else {
                    cbc.setCriterioAX1(null);
                    cbc.setTxtCriterioAX1(null);
                }
                if (!cbc.getTxtCriterioAX2().isEmpty()) {
                    try {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }

                } else {
                    cbc.setCriterioAX2(null);
                    cbc.setTxtCriterioAX2(null);
                }
                if (!cbc.getTxtCriterioPlaza().isEmpty()) {
                    try {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico..."));
                        cbc.setCriterioPlaza(null);
                        cbc.setTxtCriterioPlaza(null);
                    }

                } else {
                    cbc.setCriterioPlaza(null);
                    cbc.setTxtCriterioPlaza(null);
                }

                if (mensajesDeError.isEmpty()) {
                    usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                    cbc.setCriterioUsuario(usuarioNumero);
                    oContabilidad = new CContabilidad();
                    consultaBienFide = oContabilidad.onBienFideIndiv_Consulta(cbc);
                    oContabilidad = null;
                    if (consultaBienFide.isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados"));
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error("onContabilidadBienFideIndiv_Consulta");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_ConsultaLimpia() {
            consultaBienFide = null;
            consultaBienFideU = null;
            seleccionaBienFideU = null;
            seleccionaBienFide = null;
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioPlaza(null);
            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioPlaza(null);
        }

        public void onContabilidadBienFideIndiv_ConsultaSeleccionaBien() {
            try {
                onInicializaBienFideUnidad();
                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                oContabilidad = new CContabilidad();
                consultaBienFideUI = null;
                consultaBienFideU = oContabilidad.onBienFideIndiv_ConsultaUnidad(seleccionaBienFide.getBienFideContratoNum(),
                        seleccionaBienFide.getBienFideContratoNumSub(),
                        seleccionaBienFide.getBienFideTipo(),
                        seleccionaBienFide.getBienFideId());
                oContabilidad = null;
                if (consultaBienFideU.isEmpty()) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Inmueble sin unidades");
                    bienFideUnidad.setBienFideUnidadSec(1);
                    geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                    geneDes.setGenericoDesHabilitado06(Boolean.FALSE);
                    RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.show();");
                }
                bienFideUnidad.setBienFideContratoNum(seleccionaBienFide.getBienFideContratoNum());
                bienFideUnidad.setBienFideContratoNumSub(seleccionaBienFide.getBienFideContratoNumSub());
                bienFideUnidad.setBienFideTipo(seleccionaBienFide.getBienFideTipo());
                bienFideUnidad.setBienFideId(seleccionaBienFide.getBienFideId());
                bienFideUnidad.setBienFideUnidadMonedaNom(seleccionaBienFide.getBienFideMonedaNom());
                oClave = new CClave();
                listaIndivInmblTipoUnidad = oClave.onClave_ObtenListadoElementos(390);
                listaStatusUnidades = oClave.onClave_ObtenListadoElementos(355);
                oClave = null;
                oComunes = new CComunes();
                notarios = oComunes.onComunes_ObtenListadoNotarios("Trim(Ucase(not_nom_notario))||'»'||not_localidad_nota||'»'||not_num_notario||'»'||not_num_ofic_nota", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");
//                listaNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(Ucase(not_nom_notario))", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");

                oComunes = null;
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFideIndiv_ConsultaSeleccionaBien");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadNuevo() {
            try {
                if (seleccionaBienFide != null) {
                    onInicializaBienFideUnidad();
                    consultaBienFideUI = null;
                    oClave = new CClave();
                    listaIndivInmblTipoUnidad = oClave.onClave_ObtenListadoElementos(390);
                    listaStatus = oClave.onClave_ObtenListadoElementos(355);
                    oClave = null;
                    oComunes = new CComunes();
                    listaNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(Ucase(not_nom_notario))||'»'||not_localidad_nota||'»'||not_num_notario", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");
//                    listaNombresNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(Ucase(not_nom_notario))", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");
                    oComunes = null;
                    bienFideUnidad.setBienFideContratoNum(seleccionaBienFide.getBienFideContratoNum());
                    bienFideUnidad.setBienFideContratoNumSub(seleccionaBienFide.getBienFideContratoNumSub());
                    bienFideUnidad.setBienFideTipo(seleccionaBienFide.getBienFideTipo());
                    bienFideUnidad.setBienFideId(seleccionaBienFide.getBienFideId());
                    bienFideUnidad.setBienFideUnidadMonedaNom(seleccionaBienFide.getBienFideMonedaNom());
                    geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                    if (consultaBienFideU.isEmpty()) {
                        geneDes.setGenericoDesHabilitado06(Boolean.FALSE);
                    } else {
                        geneDes.setGenericoDesHabilitado06(Boolean.TRUE);
                    }

                    RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.show();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccione un inmueble");
                }
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFideIndiv_UnidadNuevo");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_ConsultaSeleccionaUnidad() {
            try {
                onInicializaBienFideUnidad();
                oClave = new CClave();
                listaIndivInmblTipoUnidad = oClave.onClave_ObtenListadoElementos(390);
                listaStatus = oClave.onClave_ObtenListadoElementos(355);
                oClave = null;
                oComunes = new CComunes();
                listaNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(Ucase(not_nom_notario))||'»'||not_localidad_nota||'»'||not_num_notario", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");
                oComunes = null;

                //bienFideUnidad.setBienFideUnidadAcumRegContable(seleccionaBienFideU.getBienFideUnidadAcumRegContable());
                bienFideUnidad.setBienFideUnidadDomicilio(seleccionaBienFideU.getBienFideUnidadDomicilio());
                bienFideUnidad.setBienFideUnidadEscrituraIni(seleccionaBienFideU.getBienFideUnidadEscrituraIni());
                bienFideUnidad.setBienFideUnidadFechaCto(seleccionaBienFideU.getBienFideUnidadFechaCto());
                bienFideUnidad.setBienFideUnidadMonedaNom(seleccionaBienFideU.getBienFideUnidadMonedaNom());
                bienFideUnidad.setBienFideUnidadMonedaNum(seleccionaBienFideU.getBienFideUnidadMonedaNum());
                bienFideUnidad.setBienFideUnidadNombre(seleccionaBienFideU.getBienFideUnidadNombre());
                bienFideUnidad.setBienFideUnidadNotarioLocalidad(seleccionaBienFideU.getBienFideUnidadNotarioLocalidad());
                bienFideUnidad.setBienFideUnidadNotarioNom(seleccionaBienFideU.getBienFideUnidadNotarioNom());
                bienFideUnidad.setBienFideUnidadNotarioNum(seleccionaBienFideU.getBienFideUnidadNotarioNum());
                bienFideUnidad.setBienFideUnidadNotarioNumOFicial(seleccionaBienFideU.getBienFideUnidadNotarioNumOFicial());
                bienFideUnidad.setBienFideUnidadNotarioLista(seleccionaBienFideU.getBienFideUnidadNotarioLista());
                bienFideUnidad.setBienFideUnidadObservacion(seleccionaBienFideU.getBienFideUnidadObservacion());
                bienFideUnidad.setBienFideUnidadSec(seleccionaBienFideU.getBienFideUnidadSec());
                bienFideUnidad.setBienFideUnidadStatus(seleccionaBienFideU.getBienFideUnidadStatus());
                bienFideUnidad.setBienFideUnidadSuperficie(seleccionaBienFideU.getBienFideUnidadSuperficie());
                bienFideUnidad.setTxtBienFideUnidadSuperficie(formatDecimal2D(seleccionaBienFideU.getBienFideUnidadSuperficie()));
                bienFideUnidad.setBienFideUnidadTipo(seleccionaBienFideU.getBienFideUnidadTipo());
                bienFideUnidad.setBienFideUnidadValor(seleccionaBienFideU.getBienFideUnidadValor());
                bienFideUnidad.setTxtBienFideUnidadValor(formatImporte2D(seleccionaBienFideU.getBienFideUnidadValor()));
                bienFideUnidad.setBienFideUnidadAcumRegContable(seleccionaBienFideU.getBienFideUnidadAcumRegContable());

                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneDes.setGenericoDesHabilitado06(Boolean.TRUE);
                geneFecha.setGenericoFecha00(new java.util.Date(bienFideUnidad.getBienFideUnidadFechaCto().getTime()));

                oContabilidad = new CContabilidad();
                consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(seleccionaBienFide.getBienFideContratoNum(),
                        seleccionaBienFide.getBienFideContratoNumSub(),
                        seleccionaBienFide.getBienFideTipo(),
                        seleccionaBienFide.getBienFideId(),
                        bienFideUnidad.getBienFideUnidadSec());
                bienFideUnidad.setBienFideUnidadAcumRegContable(oContabilidad.getBienFideUnidadAcumRegContable());
                oContabilidad = null;
                RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.show();");
            } catch (SQLException Err) {
                logger.error("onContabilidadBienFideIndiv_ConsultaSeleccionaUnidad");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadExtraeNotario() {
            try {
                bienFideUnidad.setBienFideUnidadNotarioNom(null);
                bienFideUnidad.setBienFideUnidadNotarioLocalidad(null);
                bienFideUnidad.setBienFideUnidadNotarioNum(null);
                if ((bienFideUnidad.getBienFideUnidadNotarioLista() != null) && (!bienFideUnidad.getBienFideUnidadNotarioLista().equals(new String()))) {
                    String arrNotario[] = bienFideUnidad.getBienFideUnidadNotarioLista().split("»");
                    bienFideUnidad.setBienFideUnidadNotarioNom(arrNotario[0].trim());
                    bienFideUnidad.setBienFideUnidadNotarioLocalidad(arrNotario[1].trim());
                    bienFideUnidad.setBienFideUnidadNotarioNum(Integer.parseInt(arrNotario[2].trim()));
                    bienFideUnidad.setBienFideUnidadNotarioNumOFicial(Integer.parseInt(arrNotario[3].trim()));
                }
            } catch (NumberFormatException Err) {
                logger.error("onContabilidadBienFideIndiv_UnidadExtraeNotario");
            }
        }

        public void onContabilidadBienFideIndiv_UnidadRegistroModifica() {
            geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
            bienFideUnidad.setBienFideBitTipoOperacion("MODIFICAR");
        }

        public void onContabilidadBienFideIndiv_UnidadRegistroAplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                if (((bienFideUnidad.getBienFideUnidadNombre() == null) || (bienFideUnidad.getBienFideUnidadNombre().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Nombre Unidad no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadDomicilio() == null) || (bienFideUnidad.getBienFideUnidadDomicilio().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Domicilio no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadTipo() == null) || (bienFideUnidad.getBienFideUnidadTipo().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadEscrituraIni() == null) || (bienFideUnidad.getBienFideUnidadEscrituraIni().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Escritura Inicial no puede estar vacío..."));
                }
                if ((geneFecha.getGenericoFecha00() == null) && (validacion.equals(Boolean.TRUE))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Contrato no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadNotarioLista() == null) || (bienFideUnidad.getBienFideUnidadNotarioLista().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Notario no puede estar vacío..."));
                }
                if ((!bienFideUnidad.getTxtBienFideUnidadValor().isEmpty())) {
                    try {

                        bienFideUnidad.setTxtBienFideUnidadValor(limpiaImporte2D(bienFideUnidad.getTxtBienFideUnidadValor()));
                        bienFideUnidad.setBienFideUnidadValor(Double.parseDouble(bienFideUnidad.getTxtBienFideUnidadValor()));
                        bienFideUnidad.setTxtBienFideUnidadValor(formatImporte2D(bienFideUnidad.getBienFideUnidadValor()));
                        if (bienFideUnidad.getBienFideUnidadValor() > VALOR_MAX) {
                            bienFideUnidad.setBienFideUnidadValor(null);
                            bienFideUnidad.setTxtBienFideUnidadValor(null);
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Valor Unidad  no puede ser mayor a 999,999,999,999.99..."));
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Valor Unidad debe ser un campo numérico..."));
                        bienFideUnidad.setBienFideUnidadValor(null);
                        bienFideUnidad.setTxtBienFideUnidadValor(null);
                    }

                } else {
                    bienFideUnidad.setBienFideUnidadValor(null);
                    bienFideUnidad.setTxtBienFideUnidadValor(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Valor Unidad no puede estar vacío..."));
                }
                if ((!bienFideUnidad.getTxtBienFideUnidadSuperficie().isEmpty())) {
                    try {
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(limpiaImporte2D(bienFideUnidad.getTxtBienFideUnidadSuperficie()));
                        bienFideUnidad.setBienFideUnidadSuperficie(Double.parseDouble(bienFideUnidad.getTxtBienFideUnidadSuperficie()));
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(formatDecimal2D(bienFideUnidad.getBienFideUnidadSuperficie()));
                        if (bienFideUnidad.getBienFideUnidadSuperficie() > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Superficie m2  no puede ser mayor a 999,999,999,999.99..."));
                            bienFideUnidad.setBienFideUnidadSuperficie(null);
                            bienFideUnidad.setTxtBienFideUnidadSuperficie(null);

                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Superficie m2 debe ser un campo numérico..."));
                        bienFideUnidad.setBienFideUnidadSuperficie(null);
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(null);
                    }

                } else {
                    bienFideUnidad.setBienFideUnidadSuperficie(null);
                    bienFideUnidad.setTxtBienFideUnidadSuperficie(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Superficie m2 no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadObservacion() == null) || (bienFideUnidad.getBienFideUnidadObservacion().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Observaciones no puede estar vacío..."));
                }
                if (((bienFideUnidad.getBienFideUnidadStatus() == null) || (bienFideUnidad.getBienFideUnidadStatus().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Status no puede estar vacío..."));
                }
                if (mensajesDeError.isEmpty()) {
                    oMoneda = new CMoneda();
                    bienFideUnidad.setBienFideUnidadMonedaNum(oMoneda.onMoneda_ObtenMonedaId(bienFideUnidad.getBienFideUnidadMonedaNom()));
                    oMoneda = null;
                    bienFideUnidad.setBienFideUnidadFechaCto(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                    oContabilidad = new CContabilidad();
                    if (bienFideUnidad.getBienFideTipo().equals("FIDEICOMITIDO")) {
                        bienFideUnidad.setBienFideTipo("FIDECOMITIDO");
                    }
                    if (oContabilidad.onBienFideIndiv_AdministraUnidad(bienFideUnidad).equals(Boolean.TRUE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
                        consultaBienFideU = oContabilidad.onBienFideIndiv_ConsultaUnidad(seleccionaBienFide.getBienFideContratoNum(),
                                seleccionaBienFide.getBienFideContratoNumSub(),
                                seleccionaBienFide.getBienFideTipo(),
                                seleccionaBienFide.getBienFideId());
                        //seleccionaBienFide  = null;
                        seleccionaBienFideU = null;
                        RequestContext.getCurrentInstance().execute("dlgBienFideUnidad.hide();");
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                    }
                    oContabilidad = null;
                }
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadRegistroELimina() {
            if (consultaBienFideUI.size() > 0) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No puede dar de baja la unidad mientras tenga individualización");
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            } else {
                mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la eliminación del registro actual?");
                mensajeConfrima.setMensajeConfirmaOrigen("bienFideUni");
                mensajeConfrima.setMensajeConfirmacionAccion("ELIMINAR");
                RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
            }
        }

        public void onContabilidadBienFideIndiv_UnidadRegistroIndividualiza() {
            try {
                onInicializaBienFideUnidadIndiv();
                oClave = new CClave();
                listaIndivInmblTipo = oClave.onClave_ObtenListadoElementos(354);
                listaIndivUnidad = oClave.onClave_ObtenListadoElementos(355);
                setListaStatusUnidadesIndv(oClave.onClave_ObtenListadoElementos(31));
                oClave = null;
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                geneDes.setGenericoDesHabilitado02(Boolean.TRUE); //Pestaña Liquidación}
                if (0 == consultaBienFideUI.size()) {
                    geneDes.setGenericoDesHabilitado05(Boolean.FALSE); // cargaa masiva de Individulizacion de Unidad
                } else {
                    geneDes.setGenericoDesHabilitado05(Boolean.TRUE); // cargaa masiva de Individulizacion de Unidad
                }
                geneVisible.setGenericoVisible00("visible;");
                bienFideUnidadIndiv.setBienFideContratoNum(seleccionaBienFide.getBienFideContratoNum());
                bienFideUnidadIndiv.setBienFideContratoNumSub(seleccionaBienFide.getBienFideContratoNumSub());
                bienFideUnidadIndiv.setBienFideId(seleccionaBienFide.getBienFideId());
                bienFideUnidadIndiv.setBienFideTipo(seleccionaBienFide.getBienFideTipo());
                bienFideUnidadIndiv.setBienFideUnidadSec(bienFideUnidad.getBienFideUnidadSec());
                //Caso raro de scotia
                //if (bienFideUnidadIndiv.getBienFideTipo().equals("FIDEICOMITIDO"))bienFideUnidadIndiv     .setBienFideTipo("FIDECOMITIDO");
                //Fin  
                bienFideUnidadIndiv.setBienFideUnidadIndivSec(consultaBienFideUI.size() + 1);
                bienFideUnidadIndiv.setBienFideUnidadIndivMonedaNom(bienFideUnidad.getBienFideUnidadMonedaNom());
//                oContabilidad = new CContabilidad();
                //        consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidad.getBienFideContratoNum(),
                //        bienFideUnidad.getBienFideContratoNumSub(),
                //        bienFideUnidad.getBienFideTipo(),
                //        bienFideUnidad.getBienFideId(),
                //        bienFideUnidad.getBienFideUnidadSec());

//                consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidadIndiv.getBienFideContratoNum(),
//                        bienFideUnidadIndiv.getBienFideContratoNumSub(),
//                        bienFideUnidadIndiv.getBienFideTipo(),
//                        bienFideUnidadIndiv.getBienFideUnidadSec(),
//                        bienFideUnidad.getBienFideUnidadSec());
//                oContabilidad = null;
//              RequestContext.getCurrentInstance().execute("aPanel.unselect(1);");
                RequestContext.getCurrentInstance().execute("dlgBienFideIndivUni.show();");
                RequestContext.getCurrentInstance().execute("aPanel.unselect(1);");

            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_UnidadRegistroIndividualiza()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadIndivAplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                if (bienFideUnidadIndiv.getBienFideUnidadIndivDeptoNum().isEmpty()) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  Depto/Lote/Local no puede estar vacío..."));
                }
                if (bienFideUnidadIndiv.getBienFideUnidadIndivTipo() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Tipo no puede estar vacío..."));
                }
                if (bienFideUnidadIndiv.getBienFideUnidadIndivUbicacion().isEmpty()) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Ubicación no puede estar vacío..."));
                }

                if (!bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().isEmpty()) {
                    try {
                        bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(limpiaImporte2D(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2()));
                        Double valorM2 = Double.parseDouble(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2());
                        bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(formatDecimal2D(valorM2));
                        if (valorM2 > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Superficie m2  no puede ser mayor a 999,999,999,999.99..."));
                            bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(null);
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Superficie m2 debe ser un campo numérico..."));
                        bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(null);

                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Superficie m2 no puede estar vacío..."));
                }

                if (bienFideUnidadIndiv.getBienFideUnidadIndivMedidas().isEmpty()) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  Medidas no puede estar vacío..."));
                }

                if (!bienFideUnidadIndiv.getBienFideUnidadIndivIndiviso().isEmpty()) {
                    try {
                        Double.parseDouble(bienFideUnidadIndiv.getBienFideUnidadIndivIndiviso());
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Indiviso debe ser un campo numérico..."));
                        bienFideUnidadIndiv.setBienFideUnidadIndivIndiviso(null);

                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Indiviso no puede estar vacío..."));
                }

                if (!bienFideUnidadIndiv.getBienFideUnidadIndivNiveles().isEmpty()) {
                    try {
                        Integer.parseInt(bienFideUnidadIndiv.getBienFideUnidadIndivNiveles());
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Los Niveles debe ser un campo numérico..."));
                        bienFideUnidadIndiv.setBienFideUnidadIndivNiveles(null);

                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Niveles no puede estar vacío..."));
                }

                if (!bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().isEmpty()) {
                    try {
                        bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(limpiaImporte2D(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio()));
                        bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(Double.parseDouble(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio()));
                        bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(formatImporte2D(bienFideUnidadIndiv.getBienFideUnidadIndivPrecio()));
                        if (bienFideUnidadIndiv.getBienFideUnidadIndivPrecio() > VALOR_MAX) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio no puede ser mayor a 999,999,999,999.99..."));
                            bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(null);
                            bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(null);

                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Precio debe ser un campo numérico..."));
                        bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(null);
                        bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(null);
                    }
                } else {
                    bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Precio no puede estar vacío..."));
                }
                if (bienFideUnidadIndiv.getBienFideUnidadIndivStatusInmueble() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Status Inmueble no puede estar vacío..."));
                }
                if (bienFideUnidadIndiv.getBienFideUnidadIndivStatus() == null) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Status Reg. no puede estar vacío..."));
                }
                if (mensajesDeError.isEmpty()) {
                    oContabilidad = new CContabilidad();
                    if (oContabilidad.onBienFideIndiv_AdministraUnidadIndiv(bienFideUnidadIndiv).equals(Boolean.TRUE)) {
                        consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(bienFideUnidadIndiv.getBienFideContratoNum(),
                                bienFideUnidadIndiv.getBienFideContratoNumSub(),
                                bienFideUnidadIndiv.getBienFideTipo(),
                                bienFideUnidadIndiv.getBienFideId(),
                                bienFideUnidad.getBienFideUnidadSec());
                        seleccionaBienFideUI = null;
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
                        RequestContext.getCurrentInstance().execute("dlgBienFideIndivUni.hide();");
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                    }
                    oContabilidad = null;
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_UnidadIndivAplica()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadIndivModifica() {
            geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
            bienFideUnidadIndiv.setBienFideBitTipoOperacion("MODIFICAR");
        }

        public void onContabilidadBienFideIndiv_UnidadIndivElimina() {

            mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
            mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la eliminación del registro actual?");
            mensajeConfrima.setMensajeConfirmaOrigen("bienFideUniIndiv");
            mensajeConfrima.setMensajeConfirmacionAccion("ELIMINAR");
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
        }

        public void onContabilidadBienFideIndiv_UnidadIndivLiquidaSeleccionaNotario() {
            try {
                if ((bienFideUnidadLiq.getBienFideUnidadLiqNotarioNom() != null) && (!bienFideUnidadLiq.getBienFideUnidadLiqNotarioNom().equals(new String()))) {
                    String[] arrNotario = bienFideUnidadLiq.getBienFideUnidadLiqNotarioNom().split("»");
                    bienFideUnidadLiq.setBienFideUnidadLiqNotarioLoc(arrNotario[1].trim());
                    bienFideUnidadLiq.setBienFideUnidadLiqNotarioNum(Integer.parseInt(arrNotario[2].trim()));
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_UnidadIndivLiquidaSeleccionaNotario()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadIndivLiquida() {
            List<FacesMessage> mensajeDeError = new ArrayList<>();
            try {
                Date fechaSis = (new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));

                if (!bienFideUnidadIndiv.getBienFideUnidadIndivStatus().equals("ACTIVO")) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe el Indiviso o su estatus de registro no esta ACTIVO"));
                }
                if (!(bienFideUnidadIndiv.getBienFideUnidadIndivStatusInmueble().contains("LIBRE (SE PUEDE VENDER)") || bienFideUnidadIndiv.getBienFideUnidadIndivStatusInmueble().contains("CON GRAVAMEN"))) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe el Indiviso o su estatus de inmueble es diferente a LIBRE (SE PUEDE VENDER) o CON GRAVAMEN"));
                }
                if ((geneFecha.getGenericoFecha02() == null)) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Solicitud no puede estar vacío..."));
                } else {
                    if (geneFecha.getGenericoFecha02().compareTo(fechaSis) > 0) {
                        validacion = Boolean.FALSE;
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Solicitud no debe ser mayor a la fecha del Sistema"));
                    }
                }
                if ((geneFecha.getGenericoFecha03() == null)) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Instrucción no puede estar vacío..."));
                } else {
                    if (geneFecha.getGenericoFecha03().compareTo(fechaSis) > 0) {
                        validacion = Boolean.FALSE;
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Instrucción no debe ser mayor a la fecha del Sistema"));
                    }
                }
                if ((geneFecha.getGenericoFecha04() == null)) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Firma no puede estar vacío..."));
                } else {
                    if (geneFecha.getGenericoFecha04().compareTo(fechaSis) > 0) {
                        validacion = Boolean.FALSE;
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Firma no debe ser mayor a la fecha del Sistema"));
                    }
                }
                if ((geneFecha.getGenericoFecha05() == null)) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha tras. dom. no puede estar vacío..."));
                } else {
                    if (geneFecha.getGenericoFecha05().compareTo(fechaSis) > 0) {
                        validacion = Boolean.FALSE;
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha tras. dom. no debe ser mayor a la fecha del Sistema"));
                    }
                }
                if (((bienFideUnidadLiq.getBienFideUnidadLiqEscritura() == null) || (bienFideUnidadLiq.getBienFideUnidadLiqEscritura().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Escritura no puede estar vacío..."));
                }
                if (((bienFideUnidadLiq.getBienFideUnidadLiqNotarioNom() == null) || (bienFideUnidadLiq.getBienFideUnidadLiqNotarioNom().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Notario no puede estar vacío..."));
                }
                if (((bienFideUnidadLiq.getBienFideUnidadLiqAquiere() == null) || (bienFideUnidadLiq.getBienFideUnidadLiqAquiere().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajeDeError.add(mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Adquiriente no puede estar vacío..."));
                }
                if (mensajeDeError.isEmpty()) {

                    if (bienFideUnidadIndiv.getBienFideContratoNum() != 0 || bienFideUnidadIndiv.getBienFideContratoNum() != null) {
                        bienFideUnidadLiq.setBienFideContratoNum(bienFideUnidadIndiv.getBienFideContratoNum());
                    }
                    if (bienFideUnidadIndiv.getBienFideContratoNumSub() != 0 || bienFideUnidadIndiv.getBienFideContratoNumSub() != null) {
                        bienFideUnidadLiq.setBienFideContratoNumSub(bienFideUnidadIndiv.getBienFideContratoNumSub());
                    }
                    if (bienFideUnidadIndiv.getBienFideId() != 0 || bienFideUnidadIndiv.getBienFideId() != null) {
                        bienFideUnidadLiq.setBienFideId(bienFideUnidadIndiv.getBienFideId());
                    }
                    if (bienFideUnidadIndiv.getBienFideTipo() != null) {
                        bienFideUnidadLiq.setBienFideTipo(bienFideUnidadIndiv.getBienFideTipo());
                    }
                    if (bienFideUnidadIndiv.getBienFideUnidadSec() != 0 || bienFideUnidadIndiv.getBienFideUnidadSec() != null) {
                        bienFideUnidadLiq.setBienFideUnidadSec(bienFideUnidadIndiv.getBienFideUnidadSec());
                    }
                    if (bienFideUnidadIndiv.getBienFideUnidadIndivSec() != 0 || bienFideUnidadIndiv.getBienFideUnidadIndivSec() != null) {
                        bienFideUnidadLiq.setBienFideUnidadLiqSec(bienFideUnidadIndiv.getBienFideUnidadIndivSec());
                    }
                    if (geneFecha.getGenericoFecha02() != null) {
                        bienFideUnidadLiq.setBienFideUnidadLiqFechaSol(new java.sql.Date(geneFecha.getGenericoFecha02().getTime()));
                    }
                    if (geneFecha.getGenericoFecha03() != null) {
                        bienFideUnidadLiq.setBienFideUnidadLiqFechaInstr(new java.sql.Date(geneFecha.getGenericoFecha03().getTime()));
                    }
                    if (geneFecha.getGenericoFecha04() != null) {
                        bienFideUnidadLiq.setBienFideUnidadLiqFechaFirma(new java.sql.Date(geneFecha.getGenericoFecha04().getTime()));
                    }
                    if (geneFecha.getGenericoFecha05() != null) {
                        bienFideUnidadLiq.setBienFideUnidadLiqFechaTrasDom(new java.sql.Date(geneFecha.getGenericoFecha05().getTime()));
                    }

                    if (usuarioNombre != null && !"".equals(usuarioNombre)) { //CAVC
                        mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                    }

                    mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la liquidación del registro actual?");
                    mensajeConfrima.setMensajeConfirmaOrigen("bienFideUniIndiv");
                    mensajeConfrima.setMensajeConfirmacionAccion("LIQUIDAR");
                    RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_UnidadIndivLiquida()");
            } finally {
                for (FacesMessage mensaje : mensajeDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_UnidadIndivSelecciona() {
            try {
                onInicializaBienFideUnidadIndiv();
                onInicializaBienFideUnidadLiq();
                oClave = new CClave();
                listaIndivInmblTipo = oClave.onClave_ObtenListadoElementos(354);
                listaIndivUnidad = oClave.onClave_ObtenListadoElementos(355);
                listaStatusUnidadesIndv = oClave.onClave_ObtenListadoElementos(31);
                listaStatus = oClave.onClave_ObtenListadoElementos(31);
                oClave = null;

                bienFideUnidadIndiv.setBienFideContratoNum(bienFideUnidad.getBienFideContratoNum());
                bienFideUnidadIndiv.setBienFideContratoNumSub(bienFideUnidad.getBienFideContratoNumSub());
                bienFideUnidadIndiv.setBienFideId(bienFideUnidad.getBienFideId());
                bienFideUnidadIndiv.setBienFideTipo(bienFideUnidad.getBienFideTipo());
                bienFideUnidadIndiv.setBienFideUnidadSec(bienFideUnidad.getBienFideUnidadSec());

                bienFideUnidadIndiv.setBienFideUnidadIndivDeptoNum(seleccionaBienFideUI.getBienFideUnidadIndivDeptoNum());
                bienFideUnidadIndiv.setBienFideUnidadIndivIndiviso(seleccionaBienFideUI.getBienFideUnidadIndivIndiviso());
                bienFideUnidadIndiv.setBienFideUnidadIndivMedidas(seleccionaBienFideUI.getBienFideUnidadIndivMedidas());
                bienFideUnidadIndiv.setBienFideUnidadIndivMonedaNom(seleccionaBienFideUI.getBienFideUnidadIndivMonedaNom());
                bienFideUnidadIndiv.setBienFideUnidadIndivNiveles(seleccionaBienFideUI.getBienFideUnidadIndivNiveles());
                bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(seleccionaBienFideUI.getBienFideUnidadIndivPrecio());
                bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(formatImporte2D(seleccionaBienFideUI.getBienFideUnidadIndivPrecio()));
                bienFideUnidadIndiv.setBienFideUnidadIndivSec(seleccionaBienFideUI.getBienFideUnidadIndivSec());
                bienFideUnidadIndiv.setBienFideUnidadIndivMonedaNom(bienFideUnidad.getBienFideUnidadMonedaNom());
                bienFideUnidadIndiv.setBienFideUnidadIndivStatus(seleccionaBienFideUI.getBienFideUnidadIndivStatus());
                bienFideUnidadIndiv.setBienFideUnidadIndivStatusInmueble(seleccionaBienFideUI.getBienFideUnidadIndivStatusInmueble());
                bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(seleccionaBienFideUI.getBienFideUnidadIndivSuperficieM2());
                bienFideUnidadIndiv.setBienFideUnidadIndivMedidas(seleccionaBienFideUI.getBienFideUnidadIndivMedidas());
                bienFideUnidadIndiv.setBienFideUnidadIndivTipo(seleccionaBienFideUI.getBienFideUnidadIndivTipo());
                bienFideUnidadIndiv.setBienFideUnidadIndivUbicacion(seleccionaBienFideUI.getBienFideUnidadIndivUbicacion());
                bienFideUnidadIndiv.setBienFideUnidadIndivUltAvaluoValor(seleccionaBienFideUI.getBienFideUnidadIndivUltAvaluoValor());
                bienFideUnidadIndiv.setBienFideUnidadindivUlAvaluoFecha(seleccionaBienFideUI.getBienFideUnidadindivUlAvaluoFecha());
                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);  //Campos de individualizacion
                geneDes.setGenericoDesHabilitado02(Boolean.FALSE); //Pestaña y boton de liquidacion
                geneDes.setGenericoDesHabilitado03(Boolean.FALSE); //Campos de Liquidación
                geneFecha.setGenericoFecha01(new java.util.Date(bienFideUnidadIndiv.getBienFideUnidadindivUlAvaluoFecha().getTime()));
                geneVisible.setGenericoVisible00("visible;");
                //Si ya esta CONTABILIZADA la unidad, llenamos el objeto de bienFideUnidadLiq 
                if (seleccionaBienFideUI.getBienFideUnidadIndivCveLiq().equals("SI")) {
                    geneDes.setGenericoDesHabilitado03(Boolean.TRUE);
                    geneVisible.setGenericoVisible00("hidden;");
                    bienFideUnidadLiq.setBienFideContratoNum(bienFideUnidad.getBienFideContratoNum());
                    bienFideUnidadLiq.setBienFideContratoNumSub(bienFideUnidad.getBienFideContratoNumSub());
                    bienFideUnidadLiq.setBienFideId(bienFideUnidad.getBienFideId());
                    bienFideUnidadLiq.setBienFideTipo(bienFideUnidad.getBienFideTipo());
                    bienFideUnidadLiq.setBienFideUnidadSec(bienFideUnidad.getBienFideUnidadSec());
                    bienFideUnidadLiq.setBienFideUnidadLiqSec(bienFideUnidadIndiv.getBienFideUnidadIndivSec());
                    oContabilidad = new CContabilidad();
                    oContabilidad.onBienFideIndiv_ObtenUnidadLiq(bienFideUnidadLiq);
                    oContabilidad = null;
                    if (bienFideUnidadLiq.getBienFideUnidadLiqFechaSol() != null) {
                        geneFecha.setGenericoFecha02(new java.util.Date(bienFideUnidadLiq.getBienFideUnidadLiqFechaSol().getTime()));
                    }
                    if (bienFideUnidadLiq.getBienFideUnidadLiqFechaInstr() != null) {
                        geneFecha.setGenericoFecha03(new java.util.Date(bienFideUnidadLiq.getBienFideUnidadLiqFechaInstr().getTime()));
                    }
                    if (bienFideUnidadLiq.getBienFideUnidadLiqFechaFirma() != null) {
                        geneFecha.setGenericoFecha04(new java.util.Date(bienFideUnidadLiq.getBienFideUnidadLiqFechaFirma().getTime()));
                    }
                    if (bienFideUnidadLiq.getBienFideUnidadLiqFechaTrasDom() != null) {
                        geneFecha.setGenericoFecha05(new java.util.Date(bienFideUnidadLiq.getBienFideUnidadLiqFechaTrasDom().getTime()));
                    }
                }
                RequestContext.getCurrentInstance().execute("dlgBienFideIndivUni.show();");
                RequestContext.getCurrentInstance().execute("aPanel.unselect(1);");
            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_UnidadIndivSelecciona()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadBienFideIndiv_ArchivoDespliegaVentana() {
            try {
                listaErr = new ArrayList<>();
                parametrosFC = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                geneTitulo.setGenericoTitulo00(new String());
                geneTitulo.setGenericoTitulo01(new String());
                geneTitulo.setGenericoTitulo02(new String());
                geneDes.setGenericoDesHabilitado04(Boolean.TRUE);

                if (parametrosFC.get("paramOrigenCarga").equals("00")) {
                    geneTitulo.setGenericoTitulo00(".:: Masivo unidades");
                    geneTitulo.setGenericoTitulo01("Importe máximo esperado ".concat(formatDecimal2D(seleccionaBienFide.getBienFideImporteUltVal())));
                }
                if (parametrosFC.get("paramOrigenCarga").equals("01")) {
                    geneTitulo.setGenericoTitulo00(".:: Masivo cambio de Estatus");
                }
                if (parametrosFC.get("paramOrigenCarga").equals("02")) {
                    geneTitulo.setGenericoTitulo00(".:: Masivo individualización");
                    geneTitulo.setGenericoTitulo01("Valor de la unidad     : ".concat(formatDecimal2D(bienFideUnidad.getBienFideUnidadValor())));
                    geneTitulo.setGenericoTitulo02("Superficie de la unidad: ".concat(formatDecimal2D(bienFideUnidad.getBienFideUnidadSuperficie())));
                }
                if (parametrosFC.get("paramOrigenCarga").equals("03")) {
                    geneTitulo.setGenericoTitulo00(".:: Masivo Liquidación");
                }
                RequestContext.getCurrentInstance().execute("dlgBienFideIndivCargaMasiva.show();");
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_ArchivoDespliegaVentana()");
            }
        }

        public void onContabilidadBienFideIndiv_ArchivoTransfiere(FileUploadEvent event) {
            try {
                arrArchivo = new String[Short.MAX_VALUE]; //32,767 (tamaño del arreglo)
                listaErr = new ArrayList<>();
                boolean validacion = true;
                /*archivoNombre   = System.getProperty("java.io.tmpdir").concat("/").concat(event.getFile().getFileName().replace(" ", "_"));
                archivoLinea    = new String();
                archivo         = new File(archivoNombre);
                fos             = new FileOutputStream(archivo);
                fos.write(event.getFile().getContents());
                fos.close();*/
                //Nos vamos por la piedritas por culpa de IE
                archivoNombre = event.getFile().getFileName().replace(" ", "_");
                archivoLinea = new String();
                archivo = new File(archivoNombre);
                archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(archivo.getName());
                if (parametrosFC.get("paramOrigenCarga").equals("00")
                        || parametrosFC.get("paramOrigenCarga").equals("02")) {

                    try {
                        String nombreA;
                        String xFideicomiso;

                        if (parametrosFC.get("paramOrigenCarga").equals("00")) { //Masivo Unidades
                            nombreA = archivo.getName().substring(0, 8);
                            xFideicomiso = archivo.getName().substring(8, archivo.getName().lastIndexOf("."));
                        } else {
                            nombreA = archivo.getName().substring(0, 10);
                            xFideicomiso = archivo.getName().substring(10, archivo.getName().lastIndexOf("."));
                        }

                        int iFideicomiso = Integer.parseInt(xFideicomiso);
                        if (parametrosFC.get("paramOrigenCarga").equals("00")) { //Masivo Unidades
                            if (!"UNIDADES".equals(nombreA.toUpperCase(Locale.ENGLISH))) {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "**El Archivo debe llamarse UNIDADESnnnnnnnnnn.txt");
                                validacion = false;
                            }
                        } else {
                            if (!"INDIVIDUAL".equals(nombreA.toUpperCase(Locale.ENGLISH))) {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "**El Archivo debe llamarse INDIVIDUALnnnnnnnnnn.txt");
                                validacion = false;
                            }
                        }
                        if (iFideicomiso != seleccionaBienFide.getBienFideContratoNum() && validacion) { //Masivo Unidades
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Archivo no corresponde al Fiso: " + seleccionaBienFide.getBienFideContratoNum());
                            validacion = false;
                        }
                    } catch (NumberFormatException e) {
                        if (parametrosFC.get("paramOrigenCarga").equals("00")) { //Masivo Unidades
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "**El Archivo debe llamarse UNIDADESnnnnnnnnnn.txt");
                            validacion = false;
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "**El Archivo debe llamarse INDIVIDUALnnnnnnnnnn.txt");
                            validacion = false;
                        }
                    }
                }
                if (validacion) {

                    archivo = null;

                    archivo = new File(archivoNombre);
                    fos = new FileOutputStream(archivo);
                    fos.write(event.getFile().getContents());
                    fos.close();

                    //La informaciòn del archivo transferido la subimos a un arreglo unidimensional
                    if (onLlenaInformacionDesdeArchivo(arrArchivo).equals(Boolean.TRUE)) {
                        if (parametrosFC.get("paramOrigenCarga").equals("00")) { //Masivo Unidades
                            if (onCargaUnidadesValida(arrArchivo).equals(Boolean.TRUE)) {
                                geneDes.setGenericoDesHabilitado04(Boolean.FALSE); //Habilitamos el boton de APLICAR
                            }
                        }
                        if (parametrosFC.get("paramOrigenCarga").equals("01")) { //Cambio de Status
                            if (onCargaUnidadesIndivCambioStatusValida(arrArchivo).equals(Boolean.TRUE)) {
                                onCargaUnidadesIndivCambioStatusTransfiere(arrArchivo);
                                geneDes.setGenericoDesHabilitado04(Boolean.FALSE); //Habilitamos el boton de APLICAR
                            }
                        }
                        if (parametrosFC.get("paramOrigenCarga").equals("02")) {//Masivo individualizacion
                            if (onCargaUnidadesIndivValida(arrArchivo, bienFideUnidad.getBienFideUnidadSuperficie(), bienFideUnidad.getBienFideUnidadValor()).equals(Boolean.TRUE)) {
                                geneDes.setGenericoDesHabilitado04(Boolean.FALSE); //Habilitamos el boton de APLICAR
                            }
                        }
                        if (parametrosFC.get("paramOrigenCarga").equals("03")) { //Masivo Liquidacion
                            if (onCargaUnidadesIndivLiquidacionValida(arrArchivo).equals(Boolean.TRUE)) {
                                onCargaUnidadesIndivLiquidacionTransfiere(arrArchivo);
                                geneDes.setGenericoDesHabilitado04(Boolean.FALSE); //Habilitamos el boton de APLICAR       
                            }
                        }
                        if (geneDes.getGenericoDesHabilitado04().equals(Boolean.FALSE)) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Validación correcta, se puede aplicar el archivo");
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El archivo contiene errores, imposible continuar");
                        }
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No fue posible transferir el archivo.");
                    }
                    //Borramos el archivo
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    archivo = null;
                }
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_ArchivoTransfiere()");
            } finally {
                onFinalizaObjetos();
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onContabilidadBienFideIndiv_ArchivoAplica() {
            try {
                if (parametrosFC.get("paramOrigenCarga").equals("00")) {
                    onCargaUnidadesAplica(arrArchivo);
                }
                if (parametrosFC.get("paramOrigenCarga").equals("01")) {
                    oContabilidad = new CContabilidad();
                    oContabilidad.onBienFideIndiv_IndivMasivoAplica(usuarioNumero, new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()),
                            "ESTATUS", listaErr);
                    oContabilidad = null;
                }
                if (parametrosFC.get("paramOrigenCarga").equals("02")) {
                    onCargaUnidadesIndivAplica(arrArchivo);
                }
                if (parametrosFC.get("paramOrigenCarga").equals("03")) {
                    oContabilidad = new CContabilidad();
                    oContabilidad.onBienFideIndiv_IndivMasivoAplica(usuarioNumero, new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()),
                            "LIQUIDACION", listaErr);
                    oContabilidad = null;
                }
                //Actualizamos la consulta de individualizacion
                oContabilidad = new CContabilidad();
                consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(seleccionaBienFide.getBienFideContratoNum(),
                        seleccionaBienFide.getBienFideContratoNumSub(),
                        seleccionaBienFide.getBienFideTipo(),
                        seleccionaBienFide.getBienFideId(),
                        bienFideUnidad.getBienFideUnidadSec());

                consultaBienFideU = oContabilidad.onBienFideIndiv_ConsultaUnidad(seleccionaBienFide.getBienFideContratoNum(),
                        seleccionaBienFide.getBienFideContratoNumSub(),
                        seleccionaBienFide.getBienFideTipo(),
                        seleccionaBienFide.getBienFideId());

                for (int item = 0; item <= consultaBienFideU.size() - 1; item++) {
                    if (consultaBienFideU.get(item).getBienFideUnidadSec().equals(bienFideUnidad.getBienFideUnidadSec())) {
                        bienFideUnidad.setBienFideUnidadDomicilio(consultaBienFideU.get(item).getBienFideUnidadDomicilio());
                        bienFideUnidad.setBienFideUnidadEscrituraIni(consultaBienFideU.get(item).getBienFideUnidadEscrituraIni());
                        bienFideUnidad.setBienFideUnidadFechaCto(consultaBienFideU.get(item).getBienFideUnidadFechaCto());
                        bienFideUnidad.setBienFideUnidadMonedaNom(consultaBienFideU.get(item).getBienFideUnidadMonedaNom());
                        bienFideUnidad.setBienFideUnidadMonedaNum(consultaBienFideU.get(item).getBienFideUnidadMonedaNum());
                        bienFideUnidad.setBienFideUnidadNombre(consultaBienFideU.get(item).getBienFideUnidadNombre());
                        bienFideUnidad.setBienFideUnidadNotarioLocalidad(consultaBienFideU.get(item).getBienFideUnidadNotarioLocalidad());
                        bienFideUnidad.setBienFideUnidadNotarioNom(consultaBienFideU.get(item).getBienFideUnidadNotarioNom());
                        bienFideUnidad.setBienFideUnidadNotarioNum(consultaBienFideU.get(item).getBienFideUnidadNotarioNum());
                        bienFideUnidad.setBienFideUnidadNotarioNumOFicial(consultaBienFideU.get(item).getBienFideUnidadNotarioNumOFicial());
                        bienFideUnidad.setBienFideUnidadNotarioLista(consultaBienFideU.get(item).getBienFideUnidadNotarioLista());
                        bienFideUnidad.setBienFideUnidadObservacion(consultaBienFideU.get(item).getBienFideUnidadObservacion());
                        bienFideUnidad.setBienFideUnidadSec(consultaBienFideU.get(item).getBienFideUnidadSec());
                        bienFideUnidad.setBienFideUnidadStatus(consultaBienFideU.get(item).getBienFideUnidadStatus());
                        bienFideUnidad.setBienFideUnidadSuperficie(consultaBienFideU.get(item).getBienFideUnidadSuperficie());
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(formatDecimal2D(consultaBienFideU.get(item).getBienFideUnidadSuperficie()));
                        bienFideUnidad.setBienFideUnidadTipo(consultaBienFideU.get(item).getBienFideUnidadTipo());
                        bienFideUnidad.setBienFideUnidadValor(consultaBienFideU.get(item).getBienFideUnidadValor());
                        bienFideUnidad.setTxtBienFideUnidadValor(formatImporte2D(consultaBienFideU.get(item).getBienFideUnidadValor()));
                        bienFideUnidad.setBienFideUnidadAcumRegContable(consultaBienFideU.get(item).getBienFideUnidadAcumRegContable());
                        consultaBienFideUI = oContabilidad.onBienFideIndiv_ConsultaUnidadIndiv(seleccionaBienFide.getBienFideContratoNum(),
                                seleccionaBienFide.getBienFideContratoNumSub(),
                                seleccionaBienFide.getBienFideTipo(),
                                seleccionaBienFide.getBienFideId(),
                                bienFideUnidad.getBienFideUnidadSec());
                        bienFideUnidad.setBienFideUnidadAcumRegContable(oContabilidad.getBienFideUnidadAcumRegContable());
                        break;
                    }
                }
                oContabilidad = null;
                //seleccionaBienFide = null;
                //seleccionaBienFideU = null;
                seleccionaBienFideUI = null;
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso finalizado, verifique sus resultados");
                geneDes.setGenericoDesHabilitado04(true);
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadBienFideIndiv_ArchivoAplica()");
            } finally {
                onFinalizaObjetos();
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        //Funciones privadas
        private Boolean onLlenaInformacionDesdeArchivo(String[] arrArchivoInf) {
            Short item = 0;
            try {
                valorRetorno = Boolean.FALSE;
                fis = new FileInputStream(archivoNombre);
                int a = 0;
                while ((a = fis.read()) != -1) {
                    String car = String.valueOf((char) a);
                    if (!car.equals("\n")) {
                        archivoLinea += car;
                    } else {
                        arrArchivoInf[item] = archivoLinea.trim();
                        item++;
                        archivoLinea = new String();
                    }
                }
                if (!"".equals(archivoLinea)) {
                    arrArchivoInf[item] = archivoLinea.trim();
                }
                fis.close();
                valorRetorno = Boolean.TRUE;
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onLlenaInformacionDesdeArchivo()");
            }
            return valorRetorno;
        }

        private void onCargaUnidadesAplica(String[] arrArchivo) {
            ContabilidadBienFideUnidadBean bfu = new ContabilidadBienFideUnidadBean();
            try {
                int unidadSec = bienFideUnidad.getBienFideUnidadSec();
                for (short itemU = 0; itemU <= arrArchivo.length - 1; itemU++) {
                    if (arrArchivo[itemU] != null) {
                        String[] arrArchivoUni = arrArchivo[itemU].split("\t");

                        bfu.setBienFideContratoNum(seleccionaBienFide.getBienFideContratoNum());
                        bfu.setBienFideContratoNumSub(seleccionaBienFide.getBienFideContratoNumSub());
                        bfu.setBienFideTipo(seleccionaBienFide.getBienFideTipo());
                        bfu.setBienFideId(seleccionaBienFide.getBienFideId());

                        bfu.setBienFideUnidadAcumRegContable(0.00);
                        bfu.setBienFideUnidadSec(unidadSec);
                        bfu.setBienFideUnidadNombre(arrArchivoUni[8]);
                        bfu.setBienFideUnidadDomicilio(arrArchivoUni[2]);
                        bfu.setBienFideUnidadTipo(arrArchivoUni[0]);
                        bfu.setBienFideUnidadEscrituraIni("0"); //no viene en el layout
                        bfu.setBienFideUnidadFechaCto(new java.sql.Date(formatFechaParse(arrArchivoUni[1]).getTime()));
                        bfu.setBienFideUnidadNotarioNum(Integer.parseInt(arrArchivoUni[4]));
                        bfu.setBienFideUnidadValor(Double.parseDouble(arrArchivoUni[5]));
                        bfu.setBienFideUnidadSuperficie(Double.parseDouble(arrArchivoUni[6]));
                        bfu.setBienFideUnidadObservacion(arrArchivoUni[9]);
                        bfu.setBienFideUnidadStatus(arrArchivoUni[7]);

                        bfu.setBienFideBitPantalla("Individualizacion");
                        bfu.setBienFideBitUsuario(usuarioNumero);
                        bfu.setBienFideBitTerminal(usuarioTerminal);
                        bfu.setBienFideBitTipoOperacion("REGISTRO");
                        bfu.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));

                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_AdministraUnidad(bfu).equals(Boolean.TRUE)) {
                            listaErr.add("Linea ".concat(String.valueOf(itemU + 1).concat(": ").concat("Registro aplicado correctamente")));
                        } else {
                            listaErr.add("Linea ".concat(String.valueOf(itemU + 1).concat(": ").concat("Error: ").concat(oContabilidad.getMensajeError())));
                        }
                        oContabilidad = null;
                        unidadSec++;
                    } else {
                        break;
                    }
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onLlenaInformacionDesdeArchivo()");
            }
        }

        private Boolean onCargaUnidadesValida(String[] arrArchivo) {
            List<String> lista00 = new ArrayList<>();
            List<String> lista01 = new ArrayList<>();
            List<String> lista02 = new ArrayList<>();
            String regEx00 = "\\d{1,2}/\\d{1,2}/\\d{4}";
            String regEx01 = "^[0-9.]{1,20}$";
            String regEx02 = "^[0-9]{1,10}$";
            try {
                if (arrArchivo.length > 0) {
                    oClave = new CClave();
                    lista00 = oClave.onClave_ObtenListadoElementos(390);
                    lista01 = oClave.onClave_ObtenListadoElementos(355);
                    oClave = null;
                    oComunes = new CComunes();
                    lista02 = oComunes.onComunes_ObtenListadoContenidoCampo("not_num_notario", "NOTARIOS", "WHERE not_cve_st_notario = 'ACTIVO'");
                    oComunes = null;
                    archivoLineaNum = 1;
                    Double sumaImporte = 0.00;
                    for (short itemU = 0; itemU <= arrArchivo.length - 1; itemU++) {
                        if (arrArchivo[itemU] != null) {
                            String[] arrArchivoUni = arrArchivo[itemU].split("\t");
                            if (arrArchivoUni.length == 10) {
                                //Tipo de Unidad
                                if (!lista00.contains(arrArchivoUni[0])) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO DE UNIDAD contiene un valor no registrado en el catálogo (Clave 390)")));
                                }
                                //Fecha de Contrato
                                if (arrArchivoUni[1].length() == 10) {
                                    if (onValidaInformacionCampo(arrArchivoUni[1], regEx00).equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA DE CONTRATO no contiene el formato de fecha requerido dd/mm/yyyy")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA DE CONTRATO no contiene los 10 caracteres requeridos")));
                                }
                                //Domicilio
                                if (arrArchivoUni[2].length() > 250) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo DOMICILIO excede del tamaño permitido (máximo 250)")));
                                }
                                //Fecha de Escrituracion
                                if (arrArchivoUni[3].length() == 10) {
                                    if (onValidaInformacionCampo(arrArchivoUni[3], regEx00).equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA DE ESCRITURACION no contiene el formato de fecha requerido dd/mm/yyyy")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA DE ESCRITURACION no contiene los 10 caractwres requeridos")));
                                }
                                //Numero de Notario
                                if (arrArchivoUni[4].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUni[4], regEx02).equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE NOTARIO contiene caracteres alfabéticos")));
                                    } else {
                                        if (!lista02.contains(arrArchivoUni[4])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE NOTARIO contiene un valor no registrado en el catálogo de notarios")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE NOTARIO excede del tamaño permitido (máximo 10)")));
                                }
                                //Valor de Unidad
                                if (onValidaInformacionCampo(arrArchivoUni[5], regEx01).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo VALOR DE UNIDAD contiene caracteres alfanuméricos")));
                                } else {
                                    sumaImporte = sumaImporte + validarDouble(arrArchivoUni[5]);
                                }
                                //Superficie
                                if (onValidaInformacionCampo(arrArchivoUni[6], regEx01).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUPERFICIE contiene caracteres alfanuméricos")));
                                }
                                //Status
                                if (!lista01.contains(arrArchivoUni[7])) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS contiene un valor no registrado en el catálogo (Clave 355)")));
                                }
                                //Nombre de Unidad
                                if (arrArchivoUni[8].length() > 200) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NOMBRE DE UNIDAD excede del tamaño permitido (máximo 200)")));
                                }
                                //Observaciones
                                if (arrArchivoUni[9].length() > 200) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo OBSERVACIONES excede del tamaño permitido (máximo 200)")));
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el registro no cumple con el requisito de 10 columnas")));
                            }
                        } else {
                            break;
                        }
                        archivoLineaNum++;
                    }
                    double tolerancia = 1e-10;
                    if (!(Math.abs(sumaImporte - seleccionaBienFide.getBienFideImporteUltVal()) < tolerancia)) {
//                    if (!sumaImporte.equals(seleccionaBienFide.getBienFideImporteUltVal())) {
                        listaErr.add("Error, Los Importes no cuadran, Importe de las Unidades = " + formatDecimal2D(sumaImporte)
                                + " , Importe del bien =  " + formatDecimal2D(seleccionaBienFide.getBienFideImporteUltVal()));

                    }
                    if (listaErr.isEmpty()) {
                        valorRetorno = Boolean.TRUE;
                    } else {
                        valorRetorno = Boolean.FALSE;
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se cargó el archivo para su validación");
                }
            } catch (SQLException | NumberFormatException Err) {
                logger.error(Err.getMessage() + "onCargaUnidadesValida()");
            }
            return valorRetorno;
        }

        private Boolean onCargaUnidadesIndivValida(String[] arrArchivo, double indivSuperficieM2, double indivPrecio) {
            List<String> lista00 = new ArrayList<>();
            List<String> lista01 = new ArrayList<>();
            double indivSup = 0.00;
            double indivPre = 0.00;
            try {
                valorRetorno = Boolean.FALSE;
                if (arrArchivo.length > 0) {
                    oClave = new CClave();
                    lista00 = oClave.onClave_ObtenListadoElementos(354);
                    lista01 = oClave.onClave_ObtenListadoElementos(355);
                    oClave = null;
                    archivoLineaNum = 1;
                    for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                        if (arrArchivo[itemUI] != null) {
                            String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");
                            if (arrArchivoUniIndiv.length == 9) {
                                //No. de departamento/lote/local
                                if (arrArchivoUniIndiv[0].length() <= 9) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[0], "^[A-Z0-9Ñ.;:(){}/ ]{1,9}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo No. DEPTO/LOTE/UNIDAD contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo No. DEPTO/LOTE/UNIDAD excede del tamaño permitido (máximo 10)")));
                                }
                                //Ubicacion
                                if (arrArchivoUniIndiv[1].length() <= 50) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[1], "^[A-Z0-9Ñ.;:(){}/ ]{1,50}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo UBICACION contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo UBICACION excede del tamaño permitido (máximo 50)")));
                                }
                                //Superficie m2
                                if (arrArchivoUniIndiv[2].length() <= 15) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[2], "^[0-9.]{1,15}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUPERFICIE M2 contiene caracteres no válidos")));
                                    } else {
                                        indivSup = indivSup + validarDouble(arrArchivoUniIndiv[2]);
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUPERFICIE M2 excede del tamaño permitido (máximo 15)")));
                                }
                                //Indiviso
                                if (arrArchivoUniIndiv[3].length() <= 15) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[3], "^[0-9.]{1,15}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO excede del tamaño permitido (máximo 15)")));
                                }
                                //Tipo
                                if (arrArchivoUniIndiv[4].length() <= 25) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[4], "^[A-Z ]{1,25}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO contiene caracteres no válidos")));
                                    } else {
                                        if (!lista00.contains(arrArchivoUniIndiv[4])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO contiene un valor no registrado en el catálogo (Clave 354)")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO excede del tamaño permitido (máximo 15)")));
                                }
                                //Niveles
                                if (arrArchivoUniIndiv[5].length() <= 3) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[5], "^[0-9]{1,3}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NIVELES contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NIVELES excede del tamaño permitido (máximo 3)")));
                                }
                                //Medidas  
                                if (arrArchivoUniIndiv[6].length() <= 250) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[6], "^[A-Z0-9Ñ.;:(){}/ ]{1,250}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo MEDIDAS contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo MEDIDAS excede del tamaño permitido (máximo 250)")));
                                }
                                //Precio
                                if (arrArchivoUniIndiv[7].length() <= 15) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[7], "^[0-9.]{1,15}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo PRECIO contiene caracteres no válidos")));
                                    } else {
                                        indivPre = indivPre + validarDouble(arrArchivoUniIndiv[7]);
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo PRECIO excede del tamaño permitido (máximo 15)")));
                                }
                                //Status
                                if (arrArchivoUniIndiv[8].length() <= 25) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[8], "^[A-Z() ]{1,25}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS contiene caracteres no válidos")));
                                    } else {
                                        if (!lista01.contains(arrArchivoUniIndiv[8])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS contiene un valor no registrado en el catálogo (Clave 355)")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO excede del tamaño permitido (máximo 15)")));
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el registro no cumple con el requisito de 9 columnas")));
                            }
                        } else {
                            break;
                        }
                        archivoLineaNum++;
                    }
                    double tolerancia = 1e-10;
                    if (!(Math.abs(indivPre - indivPrecio) < tolerancia)) {
//                    if (indivPre != indivPrecio) {
                        listaErr.add("Error, Los importes no cuadran, Importe de las individualizaciones = ".concat(formatDecimal2D(indivPre)).concat(" Importe de la Unidad = ").concat(formatDecimal2D(indivPrecio)));
                    }
                    if (indivSup != indivSuperficieM2) {
                        listaErr.add("Error, Las Superficies no Cuadran, Las individulizaciones = ".concat(formatDecimal2D(indivSup)).concat(" de la Unidad = ").concat(formatDecimal2D(indivSuperficieM2)));
                    }
                    if (listaErr.isEmpty()) {
                        valorRetorno = Boolean.TRUE;
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se cargó el archivo para su validación");
                }
            } catch (SQLException | NumberFormatException Err) {
                logger.error(Err.getMessage() + "onCargaUnidadesIndivValida()");
            }
            return valorRetorno;
        }

        private void onCargaUnidadesIndivAplica(String[] arrArchivo) {
            try {
                ContabilidadBienFideUnidadIndivBean cbfui = new ContabilidadBienFideUnidadIndivBean();
                int unidadIndivSec = bienFideUnidadIndiv.getBienFideUnidadSec();
                for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                    if (arrArchivo[itemUI] != null) {
                        String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");

                        cbfui.setBienFideContratoNum(bienFideUnidadIndiv.getBienFideContratoNum());
                        cbfui.setBienFideContratoNumSub(bienFideUnidadIndiv.getBienFideContratoNumSub());
                        cbfui.setBienFideTipo(bienFideUnidadIndiv.getBienFideTipo());
                        cbfui.setBienFideId(bienFideUnidadIndiv.getBienFideId());
                        cbfui.setBienFideUnidadSec(bienFideUnidadIndiv.getBienFideUnidadSec());

                        cbfui.setBienFideUnidadIndivSec(unidadIndivSec);
                        cbfui.setBienFideUnidadIndivDeptoNum(arrArchivoUniIndiv[0]);
                        cbfui.setBienFideUnidadIndivTipo(arrArchivoUniIndiv[4]);
                        cbfui.setBienFideUnidadIndivUbicacion(arrArchivoUniIndiv[1]);
                        cbfui.setBienFideUnidadIndivSuperficieM2(arrArchivoUniIndiv[2]);
                        cbfui.setBienFideUnidadIndivIndiviso(arrArchivoUniIndiv[3]);
                        cbfui.setBienFideUnidadIndivNiveles(arrArchivoUniIndiv[5]);
                        cbfui.setBienFideUnidadIndivPrecio(Double.parseDouble(arrArchivoUniIndiv[7]));
                        cbfui.setBienFideUnidadIndivMedidas(arrArchivoUniIndiv[6]);
                        cbfui.setBienFideUnidadIndivStatusInmueble(arrArchivoUniIndiv[8]);
                        cbfui.setBienFideUnidadIndivStatus("ACTIVO");
                        cbfui.setBienFideBitTipoOperacion("REGISTRO");
                        cbfui.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                        cbfui.setBienFideBitUsuario(usuarioNumero);
                        cbfui.setBienFideBitTerminal(usuarioTerminal);
                        cbfui.setBienFideBitPantalla("INDIVIDUALIZACION");
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_AdministraUnidadIndiv(cbfui).equals(Boolean.TRUE)) {
                            listaErr.add("Linea ".concat(String.valueOf(itemUI + 1).concat(": ").concat("Registro aplicado correctamente")));
                        } else {
                            listaErr.add("Linea ".concat(String.valueOf(itemUI + 1).concat(": ").concat(oContabilidad.getMensajeError())));
                        }
                        oContabilidad = null;
                        unidadIndivSec++;
                    }
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onCargaUnidadesIndivAplica()");
            } finally {
                onFinalizaObjetos();
            }
        }

        private Boolean onCargaUnidadesIndivCambioStatusValida(String[] arrArchivo) {
            List<String> lista00 = new ArrayList<>();
            try {
                oClave = new CClave();
                lista00 = oClave.onClave_ObtenListadoElementos(355);
                oClave = null;
                if (arrArchivo.length > 0) {
                    archivoLineaNum = 1;
                    for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                        if (arrArchivo[itemUI] != null) {
                            String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");
                            if (arrArchivoUniIndiv.length == 4) {
                                //Id Unidad
                                if (arrArchivoUniIndiv[0].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[0], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID UNIDAD contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFideU.getBienFideUnidadSec().toString().equals(arrArchivoUniIndiv[0])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID UNIDAD no corresponde al número de unidad seleccionado")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID UNIDAD excede del tamaño permitido (máximo 10)")));
                                }
                                //No depto/lote/local  
                                if (arrArchivoUniIndiv[1].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[1], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID INDIVIDUALIZACION contiene caracteres no válidos")));
                                    } else {
                                        if (seleccionaBienFideU.getBienFideUnidadSec().toString().equals(arrArchivoUniIndiv[0])) {
                                            Boolean existeUIndv = Boolean.FALSE;
                                            for (int citemUI = 0; citemUI <= consultaBienFideUI.size() - 1; citemUI++) {
                                                if (consultaBienFideUI.get(citemUI).getBienFideUnidadIndivSec().equals(Integer.parseInt(arrArchivoUniIndiv[1]))) {
                                                    existeUIndv = Boolean.TRUE;
                                                    if (!consultaBienFideUI.get(citemUI).getBienFideUnidadIndivStatus().contains("ACTIVO")) {
                                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, El Status de la INDIVIDUALIZACION debe ser ACTIVO")));

                                                    }
                                                }
                                            }
                                            if (!existeUIndv) {
                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, No existe ID INDIVIDUALIZACION")));
                                            }
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID INDIVIDUALIZACION excede del tamaño permitido (máximo 10)")));
                                }
                                //Indiviso
                                if (arrArchivoUniIndiv[2].length() <= 15) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[2], "^[0-9.]{1,15}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO excede del tamaño permitido (máximo 15)")));
                                }
                                //Status
                                if (arrArchivoUniIndiv[3].length() <= 25) {
                                    if (!lista00.contains(arrArchivoUniIndiv[3])) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS INMUEBLE contiene un valor no registrado en el catálogo (Clave 355)")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS INMUEBLE excede del tamaño permitido (máximo 25)")));
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el registro no cumple con el requisito de 4 columnas")));
                            }
                        } else {
                            break;
                        }
                        archivoLineaNum++;
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se cargó el archivo para su validación");
                }
                valorRetorno = Boolean.FALSE;
                if (listaErr.isEmpty()) {
                    valorRetorno = Boolean.TRUE;
                }
            } catch (SQLException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCargaUnidadesIndivCambioStatusValida()";
                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error: " + Err.getMessage())));
                valorRetorno = Boolean.FALSE;
                logger.error(Err.getMessage() + "onCargaUnidadesIndivCambioStatusValida()");
            }
            return valorRetorno;
        }

        private void onCargaUnidadesIndivCambioStatusTransfiere(String[] arrArchivo) {
            try {
                archivoLineaNum = 1;
                oContabilidad = new CContabilidad();
                oContabilidad.onBienFideIndiv_IndivMasivoLimpiaTablas(usuarioNumero, "ESTATUS");
                oContabilidad = null;
                for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                    if (arrArchivo[itemUI] != null) {
                        onInicializaCargaUnidadesIndivMasivo();
                        String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");
                        //Verificamos que el registro no exista en la tabla SAF.INM_LIQ_MASIVA
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoProceso("ESTATUS");
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContrato(seleccionaBienFide.getBienFideContratoNum());
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContratoSub(seleccionaBienFide.getBienFideContratoNumSub());
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienTipo(seleccionaBienFide.getBienFideTipo());
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienId(seleccionaBienFide.getBienFideId());
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniId(Integer.parseInt(arrArchivoUniIndiv[0]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivId(Integer.parseInt(arrArchivoUniIndiv[1]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivIndiviso(validarDouble(arrArchivoUniIndiv[2]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatus(arrArchivoUniIndiv[3]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatusLiq(arrArchivoUniIndiv[3]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivLineaSec(archivoLineaNum);
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_IndivMasivoValidaDuplicado(bienFideUnidadIndivMasivo).equals(Boolean.FALSE)) {
                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, registro duplicado")));
                        }
                        oContabilidad = null;
                    } else {
                        break;
                    }
                    archivoLineaNum++;
                }
            } catch (NumberFormatException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCargaUnidadesIndivCambioStatusTransfiere()";
                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error: " + Err.getMessage())));
                logger.error(Err.getMessage() + "onCargaUnidadesIndivCambioStatusTransfiere()");
            }
        }

        private Boolean onCargaUnidadesIndivLiquidacionValida(String[] arrArchivo) {
            List<String> lista00 = new ArrayList<>();
            try {
                Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
                oClave = new CClave();
                lista00 = oClave.onClave_ObtenListadoElementos(355);
                oClave = null;
                if (arrArchivo.length > 0) {
                    archivoLineaNum = 1;
                    for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                        if (arrArchivo[itemUI] != null) {
                            String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");
                            if (arrArchivoUniIndiv.length == 16) {
                                //Contrato
                                if (arrArchivoUniIndiv[0].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[0], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FIDEICOMISO contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFide.getBienFideContratoNum().toString().equals(arrArchivoUniIndiv[0])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FIDEICOMISO no corresponde al fideicomiso seleccionado")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FIDEICOMISO excede del tamaño permitido (máximo 10)")));
                                }
                                //Sub contrato  
                                if (arrArchivoUniIndiv[1].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[1], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUB FIDEICOMISO contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFide.getBienFideContratoNumSub().toString().equals(arrArchivoUniIndiv[1])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUBFISO no corresponde al SubFiso seleccionado")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo SUB FIDEICOMISO excede del tamaño permitido (máximo 10)")));
                                }
                                //Tipo Bien  
                                if (arrArchivoUniIndiv[2].length() <= 20) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[2], "^[A-Z]{1,20}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO BIEN contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFide.getBienFideTipo().equals(arrArchivoUniIndiv[2])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO BIEN no corresponde al tipo bien seleccionado")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TIPO BIEN excede del tamaño permitido (máximo 20)")));
                                }
                                //Bien Id
                                if (arrArchivoUniIndiv[3].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[3], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE INMUEBLE contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFide.getBienFideId().toString().equals(arrArchivoUniIndiv[3])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE INMUEBLE no corresponde al número de inmueble seleccionado")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE INMUEBLE excede del tamaño permitido (máximo 10)")));
                                }
                                //Unidad Id
                                if (arrArchivoUniIndiv[4].length() <= 5) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[4], "^[0-9]{1,5}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE UNIDAD contiene caracteres no válidos")));
                                    } else {
                                        if (!seleccionaBienFideU.getBienFideUnidadSec().toString().equals(arrArchivoUniIndiv[4])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE UNIDAD no corresponde al número de unidad seleccionado")));
                                        } else {
                                            if (!(seleccionaBienFideU.getBienFideUnidadStatus().contains("CON GRAVAMEN") || seleccionaBienFideU.getBienFideUnidadStatus().contains("LIBRE (SE PUEDE VENDER"))) {
                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el Status de la UNIDAD debe ser alguno de los siguientes LIBRE (SE PUEDE VENDER) o CON GRAVAMEN")));
                                            }
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NUMERO DE UNIDAD excede del tamaño permitido (máximo 5)")));
                                }

                                //Unidad Indiv Id  
                                if (arrArchivoUniIndiv[5].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[5], "^[0-9]{1,10}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID contiene caracteres no válidos")));
                                    } else {
                                        Boolean existeUIndv = Boolean.FALSE;
                                        for (int citemUI = 0; citemUI <= consultaBienFideUI.size() - 1; citemUI++) {
                                            if (consultaBienFideUI.get(citemUI).getBienFideUnidadIndivSec().equals(Integer.parseInt(arrArchivoUniIndiv[5]))) {
                                                existeUIndv = Boolean.TRUE;
                                                if (!(consultaBienFideUI.get(citemUI).getBienFideUnidadIndivStatusInmueble().contains("CON GRAVAMEN") || consultaBienFideUI.get(citemUI).getBienFideUnidadIndivStatusInmueble().contains("LIBRE (SE PUEDE VENDER)"))) {
                                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, El Status de la INDIVIDUALIZACION debe ser alguno de los siguientes LIBRE (SE PUEDE VENDER) o CON GRAVAMEN")));

                                                }
                                                if (!consultaBienFideUI.get(citemUI).getBienFideUnidadIndivStatus().contains("ACTIVO")) {
                                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, El Status de la INDIVIDUALIZACION debe ser ACTIVO")));

                                                }
                                                if (consultaBienFideUI.get(citemUI).getBienFideUnidadIndivCveLiq().contains("SI")) {
                                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, La INDIVIDUALIZACION ya fue LIQUIDADO")));

                                                }
                                            }
                                        }
                                        if (!existeUIndv) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, No existe ID de INDIVIDUALIZACION")));
                                        }

                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ID excede del tamaño permitido (máximo 10)")));
                                }
                                //Indiviso
                                if (arrArchivoUniIndiv[6].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[6], "^[0-9.]{1,18}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo INDIVISO excede del tamaño permitido (máximo 18)")));
                                }
                                //Fecha Solicitud
                                if (arrArchivoUniIndiv[7].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[7], "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                                            + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                                            + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                                            + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA SOLICITUD contiene caracteres no válidos o es una fecha incorrecta")));
                                    } else {
                                        oComunes = new CComunes();
                                        Date fecha = new Date(formatFechaParse((String) arrArchivoUniIndiv[7]).getTime());
                                        Calendar calendarFecha = Calendar.getInstance();
                                        calendarFecha.setTime(fecha);
                                        if (oComunes.onCommunes_BuscaDiasFeriados(fecha) || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA SOLICITUD es un día no hábil")));
                                        } else {
                                            if (fecha.compareTo(fechSistema) > 0) {
//                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA SOLICITUD es mayor a la fecha de Sistema")));
                                            }
                                        }

                                        oComunes = null;
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA SOLICITUD excede del tamaño permitido (máximo 10)")));
                                }
                                //Fecha Instrucción  
                                if (arrArchivoUniIndiv[8].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[8], "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                                            + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                                            + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                                            + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA INSTRUCCION contiene caracteres no válidos o es una fecha incorrecta")));
                                    } else {
                                        oComunes = new CComunes();
                                        Date fecha = new Date(formatFechaParse((String) arrArchivoUniIndiv[8]).getTime());
                                        Calendar calendarFecha = Calendar.getInstance();
                                        calendarFecha.setTime(fecha);
                                        if (oComunes.onCommunes_BuscaDiasFeriados(fecha) || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA INSTRUCCION es un día no hábil")));
                                        } else {
                                            if (fecha.compareTo(fechSistema) > 0) {
//                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA INSTRUCCION es mayor a la fecha de Sistema")));
                                            }
                                        }
                                        oComunes = null;
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA INSTRUCCION excede del tamaño permitido (máximo 10)")));
                                }
                                //Fecha Firma
                                if (arrArchivoUniIndiv[9].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[9], "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                                            + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                                            + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                                            + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA FIRMA contiene caracteres no válidos o es una fecha incorrecta")));
                                    } else {
                                        oComunes = new CComunes();
                                        Date fecha = new Date(formatFechaParse((String) arrArchivoUniIndiv[9]).getTime());
                                        Calendar calendarFecha = Calendar.getInstance();
                                        calendarFecha.setTime(fecha);
                                        if (oComunes.onCommunes_BuscaDiasFeriados(fecha) || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA FIRMA es un día no hábil")));
                                        } else {
                                            if (fecha.compareTo(fechSistema) > 0) {
//                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA FIRMA es mayor a la fecha de Sistema")));
                                            }
                                        }
                                        oComunes = null;
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA FIRMA excede del tamaño permitido (máximo 10)")));
                                }
                                //Fecha Dominio
                                if (arrArchivoUniIndiv[10].length() <= 10) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[10], "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
                                            + "(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]"
                                            + "|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])"
                                            + "(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA TRANS DOM contiene caracteres no válidos")));
                                    } else {
                                        oComunes = new CComunes();
                                        Date fecha = new Date(formatFechaParse((String) arrArchivoUniIndiv[10]).getTime());
                                        Calendar calendarFecha = Calendar.getInstance();
                                        calendarFecha.setTime(fecha);
                                        if (oComunes.onCommunes_BuscaDiasFeriados(fecha) || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TRANS DOM es un día no hábil")));
                                        } else {
                                            if (fecha.compareTo(fechSistema) > 0) {
//                                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo FECHA TRANS DOM es mayor a la fecha de Sistema")));
                                            }
                                        }
                                        oComunes = null;
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo TRANS DOM excede del tamaño permitido (máximo 10)")));
                                }
                                //Escritura
                                if (arrArchivoUniIndiv[11].length() <= 50) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[11], "^[0-9]{1,50}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ESCRITURA contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ESCRITURA excede del tamaño permitido (máximo 50)")));
                                }
                                //Notario
                                if (arrArchivoUniIndiv[12].length() <= 50) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[12], "^[A-ZÑ. ]{1,50}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NOTARIO contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo NOTARIO excede del tamaño permitido (máximo 50)")));
                                }
                                //Adquieriente  
                                if (arrArchivoUniIndiv[13].length() <= 80) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[13], "^[A-ZÑ. ]{1,80}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ADQUIRIENTE contiene caracteres no válidos")));
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo ADQUIRIENTE excede del tamaño permitido (máximo 80)")));
                                }
                                //Status Indiviso  
                                if (arrArchivoUniIndiv[14].length() <= 25) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[14], "^[A-ZÑ() ]{1,25}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS INDIVISO contiene caracteres no válidos")));
                                    } else {
                                        if (!lista00.contains(arrArchivoUniIndiv[14])) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS INDIVISO contiene un valor no registrado en el catálogo (Clave 355)")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS INDIVISO excede del tamaño permitido (máximo 25)")));
                                }
                                //Status
                                if (arrArchivoUniIndiv[15].length() <= 25) {
                                    if (onValidaInformacionCampo(arrArchivoUniIndiv[15], "^[A-ZÑ ]{1,25}$").equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS LIQUIDACION contiene caracteres no válidos")));
                                    } else {
                                        if (!arrArchivoUniIndiv[15].equals("ACTIVO")) {
                                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS LIQUIDACION debe de ser ACTIVO")));
                                        }
                                    }
                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el campo STATUS LIQUIDACION excede del tamaño permitido (máximo 25)")));
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error, el registro no cumple con el requisito de 16 columnas")));
                            }
                            archivoLineaNum++;
                        } else {
                            break;
                        }
                    }

                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se cargó el archivo para su validación");
                }
                if (listaErr.isEmpty()) {
                    valorRetorno = Boolean.TRUE;
                } else {
                    valorRetorno = Boolean.FALSE;
                }
            } catch (SQLException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCargaUnidadesIndivLiquidacionValida()";
                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error: " + Err.getMessage())));
                valorRetorno = Boolean.FALSE;
                logger.error(Err.getMessage() + "onCargaUnidadesIndivLiquidacionValida()");
            }
            return valorRetorno;
        }

        private void onCargaUnidadesIndivLiquidacionTransfiere(String[] arrArchivo) {
            try {
                archivoLineaNum = 1;
                oContabilidad = new CContabilidad();
                oContabilidad.onBienFideIndiv_IndivMasivoLimpiaTablas(usuarioNumero, "LIQUIDACION");
                oContabilidad = null;
                for (short itemUI = 0; itemUI <= arrArchivo.length - 1; itemUI++) {
                    if (arrArchivo[itemUI] != null) {
                        onInicializaCargaUnidadesIndivMasivo();
                        String[] arrArchivoUniIndiv = arrArchivo[itemUI].split("\t");
                        //Verificamos que el registro no exista en la tabla SAF.INM_LIQ_MASIVA
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoProceso("LIQUIDACION");
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContrato(Long.parseLong(arrArchivoUniIndiv[0]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContratoSub(Integer.parseInt(arrArchivoUniIndiv[1]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienTipo(arrArchivoUniIndiv[2]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienId(Integer.parseInt(arrArchivoUniIndiv[3]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniId(Integer.parseInt(arrArchivoUniIndiv[4]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivId(Integer.parseInt(arrArchivoUniIndiv[5]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivIndiviso(validarDouble(arrArchivoUniIndiv[6]));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaSolicitud(new java.sql.Date(formatFechaParse(arrArchivoUniIndiv[7]).getTime()));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaInstruccion(new java.sql.Date(formatFechaParse(arrArchivoUniIndiv[8]).getTime()));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaFirma(new java.sql.Date(formatFechaParse(arrArchivoUniIndiv[9]).getTime()));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaDominio(new java.sql.Date(formatFechaParse(arrArchivoUniIndiv[10]).getTime()));
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivEscritura(arrArchivoUniIndiv[11]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivNotario(arrArchivoUniIndiv[12]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivAdquiriente(arrArchivoUniIndiv[13]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatus(arrArchivoUniIndiv[14]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatusLiq(arrArchivoUniIndiv[15]);
                        bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivLineaSec(archivoLineaNum);
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onBienFideIndiv_IndivMasivoValidaDuplicado(bienFideUnidadIndivMasivo).equals(Boolean.FALSE)) {
                            listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat(oContabilidad.getMensajeError())));
                        }
                        oContabilidad = null;
                    } else {
                        break;
                    }
                    archivoLineaNum++;
                }
            } catch (NumberFormatException Err) {
                mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCargaUnidadesIndivLiquidacionTransfiere()";
                listaErr.add("Linea ".concat(archivoLineaNum.toString().concat(": ").concat("Error: " + Err.getMessage())));
                logger.error(Err.getMessage() + "onCargaUnidadesIndivLiquidacionTransfiere()");
            }
        }

        private void onInicializaCargaUnidadesIndivMasivo() {
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoProceso(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContrato(Long.parseLong("0"));
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoContratoSub(0);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienTipo(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienId(0);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniId(0);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivId(0);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivIndiviso(0.00);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaSolicitud(null);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaInstruccion(null);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaFirma(null);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFechaDominio(null);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivEscritura(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivNotario(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivAdquiriente(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatus(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivStatusLiq(new String());
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivLineaSec(Short.valueOf("0"));
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUsuarioNum(usuarioNumero);
            bienFideUnidadIndivMasivo.setBienFideUnidadIndivMasivoBienUniIndivFolioOp(Long.parseLong("0"));
        }

        private Boolean onValidaInformacionCampo(String campoValor, String campoPatron) {
            Pattern patron = Pattern.compile(campoPatron);
            Matcher matcher = patron.matcher(campoValor);
            return matcher.matches();
        }

        private void onInicializaBienFideUnidadLiq() {
            bienFideUnidadLiq.setBienFideUnidadLiqAquiere(null);
            bienFideUnidadLiq.setBienFideUnidadLiqDeptoNum(null);
            bienFideUnidadLiq.setBienFideUnidadLiqEscritura(null);
            bienFideUnidadLiq.setBienFideUnidadLiqFechaFirma(null);
            bienFideUnidadLiq.setBienFideUnidadLiqFechaInstr(null);
            bienFideUnidadLiq.setBienFideUnidadLiqFechaSol(null);
            bienFideUnidadLiq.setBienFideUnidadLiqFechaTrasDom(null);
            bienFideUnidadLiq.setBienFideUnidadLiqNotarioLoc(null);
            bienFideUnidadLiq.setBienFideUnidadLiqNotarioNom(null);
            bienFideUnidadLiq.setBienFideUnidadLiqNotarioNum(null);
            bienFideUnidadLiq.setBienFideUnidadLiqSec(null);
            bienFideUnidadLiq.setBienFideUnidadLiqTC(1.00);
            bienFideUnidadLiq.setBienFideUnidadLiqStatus(null);
            bienFideUnidadLiq.setBienFideBitPantalla("Individualizacion");
            bienFideUnidadLiq.setBienFideBitUsuario(usuarioNumero);
            bienFideUnidadLiq.setBienFideBitTerminal(usuarioTerminal);
            bienFideUnidadLiq.setBienFideBitTipoOperacion("LIQUIDAR");
            bienFideUnidadLiq.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            geneFecha.setGenericoFecha02(null);
            geneFecha.setGenericoFecha03(null);
            geneFecha.setGenericoFecha04(null);
            geneFecha.setGenericoFecha05(null);
        }

        private void onInicializaBienFideUnidadIndiv() {
            bienFideUnidadIndiv.setBienFideUnidadIndivDeptoNum(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivSec(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivIndiviso(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivMedidas(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivMonedaNom(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivMonedaNum(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivNiveles(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivStatus(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivStatusInmueble(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivTipo(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivUbicacion(null);
            bienFideUnidadIndiv.setBienFideUnidadIndivUltAvaluoValor(null);
            bienFideUnidadIndiv.setBienFideUnidadindivUlAvaluoFecha(null);
            bienFideUnidadIndiv.setBienFideBitPantalla("Individualizacion");
            bienFideUnidadIndiv.setBienFideBitUsuario(usuarioNumero);
            bienFideUnidadIndiv.setBienFideBitTerminal(usuarioTerminal);
            bienFideUnidadIndiv.setBienFideBitTipoOperacion("REGISTRO");
            bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(null);
            geneFecha.setGenericoFecha01(null);
            geneFecha.setGenericoFecha02(null);
            geneFecha.setGenericoFecha03(null);
            geneFecha.setGenericoFecha04(null);
            geneFecha.setGenericoFecha05(null);
            bienFideUnidadIndiv.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
        }

        private void onInicializaBienFideUnidad() {
            bienFideUnidad.setBienFideUnidadAcumRegContable(0.00);
            bienFideUnidad.setBienFideUnidadDomicilio(null);
            bienFideUnidad.setBienFideUnidadEscrituraIni(null);
            bienFideUnidad.setBienFideUnidadFechaCto(null);
            bienFideUnidad.setBienFideUnidadMonedaNom(null);
            bienFideUnidad.setBienFideUnidadMonedaNum(null);
            bienFideUnidad.setBienFideUnidadNombre(null);
            bienFideUnidad.setBienFideUnidadNotarioLista(null);
            bienFideUnidad.setBienFideUnidadNotarioNom(null);
            bienFideUnidad.setBienFideUnidadNotarioNum(null);
            bienFideUnidad.setBienFideUnidadObservacion(null);
            bienFideUnidad.setBienFideUnidadSec(0);
            bienFideUnidad.setBienFideUnidadStatus(null);
            bienFideUnidad.setBienFideUnidadSuperficie(null);
            bienFideUnidad.setBienFideUnidadTipo(null);
            bienFideUnidad.setBienFideUnidadValor(null);
            bienFideUnidad.setBienFideBitPantalla("Individualizacion");
            bienFideUnidad.setBienFideBitUsuario(usuarioNumero);
            bienFideUnidad.setBienFideBitTerminal(usuarioTerminal);
            bienFideUnidad.setBienFideBitTipoOperacion("REGISTRO");
            bienFideUnidad.setTxtBienFideUnidadSuperficie(null);
            bienFideUnidad.setTxtBienFideUnidadValor(null);
            bienFideUnidad.setBienFideFechaReg(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            bienFideUnidad.setBienFideUnidadNotarioLocalidad(null);
            bienFideUnidad.setBienFideUnidadNotarioNumOFicial(null);
            geneFecha.setGenericoFecha00(null);

        }

        public void onBienIndi_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioAX1":
                    if (!"".equals(cbc.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX1(), "Fideicomiso", "L")) {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } else {
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cbc.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX2(), "SubFiso", "L")) {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } else {
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioPlaza":
                    if (!"".equals(cbc.getTxtCriterioPlaza()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioPlaza(), "Plaza", "I")) {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    } else {
                        cbc.setCriterioPlaza(null);
                        cbc.setTxtCriterioPlaza(null);
                    }
                    break;
                case "txtBienFideUnidadValor":

                    bienFideUnidad.setTxtBienFideUnidadValor(bienFideUnidad.getTxtBienFideUnidadValor().replaceAll(",", ""));
                    bienFideUnidad.setTxtBienFideUnidadValor(bienFideUnidad.getTxtBienFideUnidadValor().replace("$", ""));
                    if (!"".equals(bienFideUnidad.getTxtBienFideUnidadValor()) && oContabilidadGrales.onValidaNumerico(bienFideUnidad.getTxtBienFideUnidadValor(), "Valor Unidad", "D")) {
                        if (bienFideUnidad.getTxtBienFideUnidadValor().contains(".")) {
                            Integer largo = bienFideUnidad.getTxtBienFideUnidadValor().length();
                            if (largo >= bienFideUnidad.getTxtBienFideUnidadValor().indexOf(".") + 3) {
                                largo = bienFideUnidad.getTxtBienFideUnidadValor().indexOf(".") + 3;
                            }
                            bienFideUnidad.setTxtBienFideUnidadValor(bienFideUnidad.getTxtBienFideUnidadValor().substring(0, largo));
                        }
                        bienFideUnidad.setBienFideUnidadValor(Double.parseDouble(bienFideUnidad.getTxtBienFideUnidadValor()));
                        bienFideUnidad.setTxtBienFideUnidadValor(formatImporte2D(bienFideUnidad.getBienFideUnidadValor()));
                    } else {
                        bienFideUnidad.setBienFideUnidadValor(null);
                        bienFideUnidad.setTxtBienFideUnidadValor(null);
                    }
                    break;
                case "txtBienFideUnidadSuperficie":

                    bienFideUnidad.setTxtBienFideUnidadSuperficie(bienFideUnidad.getTxtBienFideUnidadSuperficie().replaceAll(",", ""));
                    if (!"".equals(bienFideUnidad.getTxtBienFideUnidadSuperficie()) && oContabilidadGrales.onValidaNumerico(bienFideUnidad.getTxtBienFideUnidadSuperficie(), "Superficie m2", "D")) {
                        if (bienFideUnidad.getTxtBienFideUnidadSuperficie().contains(".")) {
                            Integer largo = bienFideUnidad.getTxtBienFideUnidadSuperficie().length();
                            if (largo >= bienFideUnidad.getTxtBienFideUnidadSuperficie().indexOf(".") + 3) {
                                largo = bienFideUnidad.getTxtBienFideUnidadSuperficie().indexOf(".") + 3;
                            }
                            bienFideUnidad.setTxtBienFideUnidadSuperficie(bienFideUnidad.getTxtBienFideUnidadSuperficie().substring(0, largo));
                        }
                        bienFideUnidad.setBienFideUnidadSuperficie(Double.parseDouble(bienFideUnidad.getTxtBienFideUnidadSuperficie()));
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(formatDecimal2D(bienFideUnidad.getBienFideUnidadSuperficie()));
                    } else {
                        bienFideUnidad.setBienFideUnidadSuperficie(null);
                        bienFideUnidad.setTxtBienFideUnidadSuperficie(null);
                    }
                    break;
                case "bienFideUnidadIndivSuperficieM2":
                    bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().replaceAll(",", ""));
                    if (!"".equals(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2()) && oContabilidadGrales.onValidaNumerico(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2(), "Superficie m2", "D")) {
                        if (bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().contains(".")) {
                            Integer largo = bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().length();
                            if (largo >= bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().indexOf(".") + 3) {
                                largo = bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().indexOf(".") + 3;
                            }
                            bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2().substring(0, largo));
                        }
                        Double pasoBienFideUnidadSuperficie = validarDouble(bienFideUnidadIndiv.getBienFideUnidadIndivSuperficieM2());
                        bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(formatDecimal2D(pasoBienFideUnidadSuperficie));
                    } else {
                        bienFideUnidadIndiv.setBienFideUnidadIndivSuperficieM2(null);

                    }
                    break;
                case "bienFideUnidadIndivIndiviso":

                    if (!"".equals(bienFideUnidadIndiv.getBienFideUnidadIndivIndiviso()) && oContabilidadGrales.onValidaNumerico(bienFideUnidadIndiv.getBienFideUnidadIndivIndiviso(), "Indiviso", "I")) {
                    } else {
                        bienFideUnidadIndiv.setBienFideUnidadIndivIndiviso(null);
                    }
                    break;
                case "txtBienFideUnidadIndivPrecio":

                    bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().replaceAll(",", ""));
                    bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().replace("$", ""));
                    if (!"".equals(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio()) && oContabilidadGrales.onValidaNumerico(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio(), "Precio", "D")) {
                        if (bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().contains(".")) {
                            Integer largo = bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().length();
                            if (largo >= bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().indexOf(".") + 3) {
                                largo = bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().indexOf(".") + 3;
                            }
                            bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio().substring(0, largo));
                        }
                        bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(Double.parseDouble(bienFideUnidadIndiv.getTxtBienFideUnidadIndivPrecio()));
                        bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(formatImporte2D(bienFideUnidadIndiv.getBienFideUnidadIndivPrecio()));
                    } else {
                        bienFideUnidadIndiv.setBienFideUnidadIndivPrecio(null);
                        bienFideUnidadIndiv.setTxtBienFideUnidadIndivPrecio(null);
                    }
                    break;
                case "bienFideUnidadIndivNiveles":

                    if (!"".equals(bienFideUnidadIndiv.getBienFideUnidadIndivNiveles()) && oContabilidadGrales.onValidaNumerico(bienFideUnidadIndiv.getBienFideUnidadIndivNiveles(), "Niveles", "I")) {
                    } else {
                        bienFideUnidadIndiv.setBienFideUnidadIndivNiveles(null);
                    }
                    break;

            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }
    }

    class contabilidadPolizaMan {

        public contabilidadPolizaMan() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadPolizaMan.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadPolMan_ConsultaEjecuta() {
            try {
                consultaMovtos = null;
                balanceInquiries = null;
                seleccionaMovto = null;
                consultaMovtosLazy = null;

                //CAVC 22-09-2023
                validacion = Boolean.TRUE;
                //CAVC 22-09-2023
                /*if ((cbc.getCriterioAX1()== null)&&(validacion.equals(Boolean.TRUE))){
                    validacion = Boolean.FALSE;
                    mensaje    = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error, el campo .:: Contrato ::. debe de contener un valor");
                }*/
 /*                if ((cbc.getCriterioFolio()== null)&&(validacion.equals(Boolean.TRUE))){
                    if ((cbc.getCriterioFechaMM()== null)&&(validacion.equals(Boolean.TRUE))){
                        validacion = Boolean.FALSE;
                        mensaje    = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error, el campo .:: mm ::. debe de contener un valor");
                    }
                    if ((cbc.getCriterioFechaYYYY()== null)&&(validacion.equals(Boolean.TRUE))){
                        validacion = Boolean.FALSE;
                        mensaje    = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error, el campo .:: aaaa ::. debe de contener un valor");
                    }
                } */

                //CAVC 22/09/2023 
                //Validaciones 
                //Validación para Fideicomiso
                if (cbc.getTxtCriterioAX1() != null && !"".equals(cbc.getTxtCriterioAX1())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioAX1())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX1("");
                        cbc.setCriterioAX1(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    }
                } else {
                    cbc.setCriterioAX1(null);
                }
                //Validación para SubFiso
                if (cbc.getTxtCriterioAX2() != null && !"".equals(cbc.getTxtCriterioAX2())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioAX2())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX2("");
                        cbc.setCriterioAX2(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    }
                } else {
                    cbc.setCriterioAX2(null);
                }
                //Validación para Folio
                if (cbc.getTxtCriterioFolio() != null && !"".equals(cbc.getTxtCriterioFolio())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioFolio())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio debe ser un campo numérico..."));
                        cbc.setTxtCriterioFolio("");
                        cbc.setCriterioFolio(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFolio(Long.parseLong(cbc.getTxtCriterioFolio()));
                    }
                } else {
                    cbc.setCriterioFolio(null);
                }
                //Validación para TransId
                if (cbc.getTxtCriterioTransId() != null && !"".equals(cbc.getTxtCriterioTransId())) {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioTransId())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Trans debe ser un campo numérico..."));
                        cbc.setTxtCriterioTransId("");
                        cbc.setCriterioTransId(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioTransId(Integer.parseInt(cbc.getTxtCriterioTransId()));
                    }
                } else {
                    cbc.setCriterioTransId(null);
                }
                //Validación para DD
                if (cbc.getTxtCriterioFechaDD() != null && !"".equals(cbc.getTxtCriterioFechaDD())) {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaDD())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El dd debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaDD("");
                        cbc.setCriterioFechaDD(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaDD(Integer.parseInt(cbc.getTxtCriterioFechaDD()));
                    }
                } else {
                    cbc.setCriterioFechaDD(null);
                }

                //Validación para MM
                if (cbc.getTxtCriterioFechaMM() == null || "".equals(cbc.getTxtCriterioFechaMM())) {
                    validacion = Boolean.FALSE;
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  campo mm no puede estar vacío..."));
                } else {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaMM())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El mm debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaMM("");
                        cbc.setCriterioFechaMM(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaMM(Integer.parseInt(cbc.getTxtCriterioFechaMM()));
                    }
                }
                //Validación para YYYY
                if (cbc.getTxtCriterioFechaYYYY() == null || "".equals(cbc.getTxtCriterioFechaYYYY())) {
                    validacion = Boolean.FALSE;
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío..."));
                } else {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaYYYY())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaYYYY("");
                        cbc.setCriterioFechaYYYY(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaYYYY(Integer.parseInt(cbc.getTxtCriterioFechaYYYY()));
                    }
                }

                if (validacion.equals(Boolean.TRUE)) {
                    if (cbc.getCriterioFolio() == null) {
                        cbc.setCriterioTipoFecha("FN");
                    } else {
                        cbc.setCriterioTipoFecha(null);
                    }
                    cbc.setCriterioCveMovtoCancelado("NO");
                    cbc.setCriterioUsuario(usuarioNumero);

                    setFirstDataTable("frmPolizaMan:dtPolizaMan");
                    //DataTable dataTable = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("frmPolizaMan:dtPolizaMan");
                    //dataTable.setFirst(0);

                    oContabilidad = new CContabilidad();
                    int totalRows = oContabilidad.accountingPolicyTotalRows(cbc, "getPolicies");
                    oContabilidad = null;

                    ContabilidadMovtoBean bean = new ContabilidadMovtoBean();
                    MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getPolicies");
                    consultaMovtosLazy = lazyModel.getLazyDataModel(cbc);
                    consultaMovtosLazy.setRowCount(totalRows);

                    if (consultaMovtosLazy.getRowCount() == 0) {
                        mensajeCons = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados...");
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ConsultaEjecuta()");
            } finally {
                if (mensajeCons != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensajeCons);
                    mensajeCons = null;
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_ConsultaEjecutaAdm() {
            try {
                consultaMovtos = null;
                balanceInquiries = null;
                seleccionaMovto = null;
                consultaMovtosLazy = null;
                if (cbc.getTxtCriterioAX1() != null && !"".equals(cbc.getTxtCriterioAX1())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioAX1())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX1("");
                        cbc.setCriterioAX1(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    }
                } else {
                    cbc.setCriterioAX1(null);
                }
                //Validación para SubFiso
                if (cbc.getTxtCriterioAX2() != null && !"".equals(cbc.getTxtCriterioAX2())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioAX2())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El SubFiso debe ser un campo numérico..."));
                        cbc.setTxtCriterioAX2("");
                        cbc.setCriterioAX2(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    }
                } else {
                    cbc.setCriterioAX2(null);
                }
                //Validación para Folio
                if (cbc.getTxtCriterioFolio() != null && !"".equals(cbc.getTxtCriterioFolio())) {
                    if (!onPoliza_isLong(cbc.getTxtCriterioFolio())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio debe ser un campo numérico..."));
                        cbc.setTxtCriterioFolio("");
                        cbc.setCriterioFolio(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFolio(Long.parseLong(cbc.getTxtCriterioFolio()));
                    }
                } else {
                    cbc.setCriterioFolio(null);
                }
                //Validación para TransId
                if (cbc.getTxtCriterioTransId() != null && !"".equals(cbc.getTxtCriterioTransId())) {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioTransId())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Trans debe ser un campo numérico..."));
                        cbc.setTxtCriterioTransId("");
                        cbc.setCriterioTransId(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioTransId(Integer.parseInt(cbc.getTxtCriterioTransId()));
                    }
                } else {
                    cbc.setCriterioTransId(null);
                }
                //Validación para DD
                if (cbc.getTxtCriterioFechaDD() != null && !"".equals(cbc.getTxtCriterioFechaDD())) {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaDD())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El dd debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaDD("");
                        cbc.setCriterioFechaDD(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaDD(Integer.parseInt(cbc.getTxtCriterioFechaDD()));
                    }
                } else {
                    cbc.setCriterioFechaDD(null);
                }

                //Validación para MM
                if (cbc.getTxtCriterioFechaMM() == null || "".equals(cbc.getTxtCriterioFechaMM())) {
                    validacion = Boolean.FALSE;
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  campo mm no puede estar vacío..."));
                } else {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaMM())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El mm debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaMM("");
                        cbc.setCriterioFechaMM(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaMM(Integer.parseInt(cbc.getTxtCriterioFechaMM()));
                    }
                }
                //Validación para YYYY
                if (cbc.getTxtCriterioFechaYYYY() == null || "".equals(cbc.getTxtCriterioFechaYYYY())) {
                    validacion = Boolean.FALSE;
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío..."));
                } else {
                    if (!onPoliza_isNumeric(cbc.getTxtCriterioFechaYYYY())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa debe ser un campo numérico..."));
                        cbc.setTxtCriterioFechaYYYY("");
                        cbc.setCriterioFechaYYYY(null);
                        validacion = Boolean.FALSE;
                    } else {
                        cbc.setCriterioFechaYYYY(Integer.parseInt(cbc.getTxtCriterioFechaYYYY()));
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    if (cbc.getCriterioFolio() == null) {
                        cbc.setCriterioTipoFecha("FN");
                    } else {
                        cbc.setCriterioTipoFecha(null);
                    }
                    cbc.setCriterioCveMovtoCancelado("NO");
                    cbc.setCriterioUsuario(usuarioNumero);

                    setFirstDataTable("frmPolizaMan:dtPolizaMan");
                    //DataTable dataTable = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("frmPolizaMan:dtPolizaMan");
                    //dataTable.setFirst(0);

                    oContabilidad = new CContabilidad();
                    int totalRows = oContabilidad.accountingPolicyTotalRowsAdm(cbc, "getPolicies");
                    oContabilidad = null;

                    ContabilidadMovtoBean bean = new ContabilidadMovtoBean();
                    MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName().concat("Adm"), "getPolicies");
                    consultaMovtosLazy = lazyModel.getLazyDataModel(cbc);
                    consultaMovtosLazy.setRowCount(totalRows);

                    if (consultaMovtosLazy.getRowCount() == 0) {
                        mensajeCons = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados...");
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ConsultaEjecutaAdm()");
            } finally {
                if (mensajeCons != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensajeCons);
                    mensajeCons = null;
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_ConsultaSelecciona() {
            ContabilidadOperacionBean o = new ContabilidadOperacionBean();
           
            try {
                onLimpia();
                //Validamos que la poliza se pueda modificar
                Boolean bPuedeModificar = Boolean.FALSE;
                java.util.Date fechaMSA = formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSA"));
                java.util.Date fechaActIni = formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaActIni"));
                String fechaMSAAbierto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto");

                if ((new java.util.Date(seleccionaMovto.getMovtoFecha().getTime()).equals(fechaActIni)) || (new java.util.Date(seleccionaMovto.getMovtoFecha().getTime()).after(fechaActIni))) {
                    bPuedeModificar = Boolean.TRUE;
                } else {
                    if (((new java.util.Date(seleccionaMovto.getMovtoFecha().getTime()).equals(fechaMSA)) || (new java.util.Date(seleccionaMovto.getMovtoFecha().getTime()).before(fechaMSA))) && (fechaMSAAbierto.equals("1"))) {
                        bPuedeModificar = Boolean.TRUE;
                    }
                }
                if (bPuedeModificar.equals(Boolean.TRUE)) {
                    formatoHora = new SimpleDateFormat("hh:mm:ss");
                    o.setOperacionId(seleccionaMovto.getMovtoOperacionId());
                    polizaMan.setPolizaEncaFolio(seleccionaMovto.getMovtoFolio());
                    polizaMan.setPolizaEncaNumero(seleccionaMovto.getMovtoOperacionId());
                    oContabilidad = new CContabilidad();
                    oContabilidad.onPolizaMan_CargaCamposConDatos(polizaMan);
                    polizaMan.setPolizaEncaTipoPol(oContabilidad.onPolizaMan_ObtenTipo(polizaMan.getPolizaEncaNumero()));
                    oContabilidad.onParamOperacion_ObtenInfo(o);
                    oContabilidad = null;
                    polizaMan.setPolizaEncaNombre(o.getOperacionNombre().concat(" » ").concat(o.getOperacionId()));
                    polizaMan.setPolizaEncaRedaccion(seleccionaMovto.getMovtoDesc().substring(o.getOperacionNombre().length()).trim());
                    polizaMan.setPolizaEncaHoraAplica(formatHora(seleccionaMovto.getMovtoFechaReg()));
                    polizaMan.setPolizaEncaBitTipoOper("MODIFICAR");
                    if (polizaMan.getPolizaEncaTipoPol() == null) {
                        geneVisible.setGenericoVisible00("visible;");
                        listaFidRol = new ArrayList<>();
                        listaFidRol.add("");
                        listaFidRol.add("");

                    } else {
                        if (polizaMan.getPolizaEncaTipoPol().equals("LIQUIDACION")) {
                            geneVisible.setGenericoVisible00("visible;");
                            listaFidRol = new ArrayList<>();
                            //listaFidRol.add("BENEFICIARIO");
                            listaFidRol.add("FIDEICOMISARIO");
                            listaFidRol.add("TERCERO");
                        }
                        if (polizaMan.getPolizaEncaTipoPol().equals("DEPOSITO")) {
                            geneVisible.setGenericoVisible00("visible;");
                            listaFidRol = new ArrayList<>();
                            //listaFidRol.add("BENEFICIARIO");
                            listaFidRol.add("FIDEICOMISARIO");
                            listaFidRol.add("FIDEICOMITENTE");
                        }
                    }
                    listaPoliza = new ArrayList<>();
                    listaPoliza.add(o.getOperacionNombre().concat(" » ").concat(o.getOperacionId()));
                    RequestContext.getCurrentInstance().execute("dlgPolizaMtto.show();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La póliza se encuentra registrada en un mes que ya está cerrado. No es posible modificarla...");
                    seleccionaMovto = null;
                }
            } catch (ArithmeticException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ConsultaSelecciona()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onPageChange() {
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
            onFinalizaObjetos();
        }

        public void onContabilidadPolMan_ConsultaLimpia() {
            consultaMovtos = null;
            balanceInquiries = null;
            seleccionaMovto = null;
            consultaMovtosLazy = null;
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioFolio(null);
            cbc.setCriterioTransId(null);
            cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
            cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
            cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));

            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioFolio(null);
            cbc.setTxtCriterioTransId(null);
            cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
            cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
            cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());

            cleanDataTable("frmPolizaMan:dtPolizaMan");
        }

        public void onContabilidadPolMan_DespliegaVentana() {
            try {
                onLimpia();
                oComunes = new CComunes();
                listaPoliza = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(oper_nombre)||' » '||oper_id_operacion", "FDOPERACION", "WHERE oper_id_operacion Between '7000000000' AND '8000000000'");
                oComunes = null;
                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                geneFecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                geneVisible.setGenericoVisible00("hidden;");
                RequestContext.getCurrentInstance().execute("dlgPolizaMtto.show();");
            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_DespliegaVentana()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_DespliegaVentanaAdm() {
            try {
                onLimpia();
                oComunes = new CComunes();
                listaPoliza = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(oper_nombre)||' » '||oper_id_operacion", "FDOPERACION", "WHERE oper_id_operacion Between '7000000000' AND '8000000000' AND (OPER_NOMBRE like 'SUGERENCIA%' or OPER_NOMBRE like '%PAGO%') and OPER_STATUS = 'ACTIVO' ");
                oComunes = null;
                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                geneFecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                geneVisible.setGenericoVisible00("hidden;");
                RequestContext.getCurrentInstance().execute("dlgPolizaMtto.show();");
            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_DespliegaVentanaAdm()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_CargaCampos() {
            try {
                geneVisible.setGenericoVisible00("hidden;");
                if ((polizaMan.getPolizaEncaNombre() != null) && (!polizaMan.getPolizaEncaNombre().equals(new String()))) {
                    polizaMan.setPolizaEncaNumero(polizaMan.getPolizaEncaNombre().substring(polizaMan.getPolizaEncaNombre().indexOf("»") + 1).trim());
                    onInicializaPoliza();
                    oContabilidad = new CContabilidad();
                    oContabilidad.onPolizaMan_CargaCampos(polizaMan);
                    Boolean ConEstructura = oContabilidad.getConEstructura();
                    polizaMan.setPolizaEncaTipoPol(oContabilidad.onPolizaMan_ObtenTipo(polizaMan.getPolizaEncaNumero()));
                    oContabilidad = null;
                    if (polizaMan.getPolizaEncaTipoPol() != null && polizaMan.getPolizaEncaTipoPol().equals("LIQUIDACION")) {
                        geneVisible.setGenericoVisible00("visible;");
                        listaFidRol = new ArrayList<>();
                        //listaFidRol.add("BENEFICIARIO");
                        listaFidRol.add("FIDEICOMISARIO");
                        listaFidRol.add("TERCERO");
                    }
                    if (polizaMan.getPolizaEncaTipoPol() != null && polizaMan.getPolizaEncaTipoPol().equals("DEPOSITO")) {
                        geneVisible.setGenericoVisible00("visible;");
                        listaFidRol = new ArrayList<>();
                        //listaFidRol.add("BENEFICIARIO");
                        listaFidRol.add("FIDEICOMISARIO");
                        listaFidRol.add("FIDEICOMITENTE");
                    }
                    if (!ConEstructura){
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La operacion " + polizaMan.getPolizaEncaNumero() + " no tiene estructura..");
                        onLimpia();

                    }
                }
            } catch (ArithmeticException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_CargaCampos()"); 
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_IngresaCampo00() {
            try {
                polizaMan.setPoliza02Valor("");
                polizaMan.setPolizaEncaFidRol(null);
                polizaMan.setPolizaEncaFidPers(null);
                geneDes.setGenericoDesHabilitado01(Boolean.FALSE);
                oContabilidadGrales = new contabilidadGrales();

                if (!"".equals(polizaMan.getPoliza00Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza00Valor(), "Fideicomiso", "L")) {

                    oContrato = new CContrato();
                    usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
                    if (oContrato.onContrato_VerificaAtencion(usuarioFiltro, Long.parseLong(polizaMan.getPoliza00Valor())).equals(Boolean.TRUE)) {
                        if (oContrato.onContract_CheckHasSubcontract(Long.parseLong(polizaMan.getPoliza00Valor())).equals(Boolean.FALSE)) {
                            if (polizaMan.getPoliza01Etiqueta().equals("SUBCONTRATO")) {
                                polizaMan.setPoliza01Valor(String.valueOf(0));
                                geneDes.setGenericoDesHabilitado01(Boolean.TRUE);
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso sin Sub Fideicomiso...");
                            }
                        } else {
                            polizaMan.setPoliza01Valor(null);
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", polizaMan.getPoliza00Etiqueta().concat(": ").concat(polizaMan.getPoliza00Valor())));
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso " + polizaMan.getPoliza00Valor() + " no existe, no está activo o no le pertenece");
                        polizaMan.setPoliza00Valor(null);
                    }
                    oContrato = null;
                } else {
                    polizaMan.setPoliza00Valor(null);
                }

                oContabilidadGrales = null;
            } catch (NumberFormatException | SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_IngresaCampo00()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_Aplica() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            validacion = Boolean.TRUE;
            try {
                if (((polizaMan.getPolizaEncaNombre() == null) || (polizaMan.getPolizaEncaNombre().equals(new String())))) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Póliza no puede estar vacío..."));
                }
                if ((geneFecha.getGenericoFecha00() == null) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Valor no puede estar vacío..."));
                }
                //Campos de la póliza  
                if (polizaMan.getPoliza00Visible().equals("visible;")) {
                    if (polizaMan.getPoliza00Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza00Etiqueta()).concat(" no puede estar vacío...")));
                    }
                }
                if (polizaMan.getPoliza01Visible().equals("visible;")) {
                    if (polizaMan.getPoliza01Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza01Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza01Valor());
                            if (polizaMan.getPoliza01Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza01Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza01Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza01Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza01Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza02Visible().equals("visible;")) {
                    if (polizaMan.getPoliza02Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza02Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza02Valor());
                            if (polizaMan.getPoliza02Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza02Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza02Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza02Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza02Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza03Visible().equals("visible;")) {
                    if (polizaMan.getPoliza03Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza03Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza03Valor());
                            if (polizaMan.getPoliza03Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza03Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza03Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza03Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza03Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza04Visible().equals("visible;")) {
                    if (polizaMan.getPoliza04Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza04Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza04Valor());
                            if (polizaMan.getPoliza04Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza04Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza04Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza04Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza04Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza05Visible().equals("visible;")) {
                    if (polizaMan.getPoliza05Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza05Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza05Valor());
                            if (polizaMan.getPoliza05Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza05Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza05Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza05Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza05Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza06Visible().equals("visible;")) {
                    if (polizaMan.getPoliza06Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza06Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza06Valor());
                            if (polizaMan.getPoliza06Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza06Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza06Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza06Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza06Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza07Visible().equals("visible;")) {
                    if (polizaMan.getPoliza07Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza07Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza07Valor());
                            if (polizaMan.getPoliza07Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza07Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza07Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza07Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza07Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza08Visible().equals("visible;")) {
                    if (polizaMan.getPoliza08Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza08Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza08Valor());
                            if (polizaMan.getPoliza08Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza08Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza08Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza08Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza08Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza09Visible().equals("visible;")) {
                    if (polizaMan.getPoliza09Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza09Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza09Valor());
                            if (polizaMan.getPoliza09Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza09Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza09Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza09Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza09Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza10Visible().equals("visible;")) {
                    if (polizaMan.getPoliza10Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza10Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza10Valor());
                            if (polizaMan.getPoliza10Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza10Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza10Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza10Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza10Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza11Visible().equals("visible;")) {
                    if (polizaMan.getPoliza11Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza11Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza11Valor());
                            if (polizaMan.getPoliza11Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza11Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza11Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza11Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza11Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza12Visible().equals("visible;")) {
                    if (polizaMan.getPoliza12Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza12Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza12Valor());
                            if (polizaMan.getPoliza12Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza12Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza12Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza12Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza12Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza13Visible().equals("visible;")) {
                    if (polizaMan.getPoliza13Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza13Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza13Valor());
                            if (polizaMan.getPoliza13Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza13Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza13Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza13Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza13Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza14Visible().equals("visible;")) {
                    if (polizaMan.getPoliza14Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza14Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza14Valor());
                            if (polizaMan.getPoliza14Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza14Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza14Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza14Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza14Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza15Visible().equals("visible;")) {
                    if (polizaMan.getPoliza15Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza15Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza15Valor());
                            if (polizaMan.getPoliza15Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza15Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza15Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza15Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza15Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza16Visible().equals("visible;")) {
                    if (polizaMan.getPoliza16Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza16Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza16Valor());
                            if (polizaMan.getPoliza16Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza16Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza16Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza16Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza16Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza17Visible().equals("visible;")) {
                    if (polizaMan.getPoliza17Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza17Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza17Valor());
                            if (polizaMan.getPoliza17Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza17Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza17Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza17Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza17Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza18Visible().equals("visible;")) {
                    if (polizaMan.getPoliza18Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza18Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza18Valor());
                            if (polizaMan.getPoliza18Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza18Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza18Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza18Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza18Valor(null);
                        }
                    }
                }
                if (polizaMan.getPoliza19Visible().equals("visible;")) {
                    if (polizaMan.getPoliza19Valor().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo ".concat(polizaMan.getPoliza19Etiqueta()).concat(" no puede estar vacío...")));
                    } else {
                        try {
                            Double.parseDouble(polizaMan.getPoliza19Valor());
                            if (polizaMan.getPoliza19Valor().length() >= 15) {
                                mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El valor de ".concat(polizaMan.getPoliza19Etiqueta()).concat(" excede el valor permitido...")));
                                polizaMan.setPoliza19Valor(null);
                            }
                        } catch (NumberFormatException err) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El ".concat(polizaMan.getPoliza19Etiqueta()).concat(" debe ser un campo numérico...")));
                            polizaMan.setPoliza19Valor(null);
                        }
                    }
                }
                if (((polizaMan.getPolizaEncaRedaccion() == null) || (polizaMan.getPolizaEncaRedaccion().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Redacción no puede estar vacío..."));
                }
                if (geneVisible.getGenericoVisible00().equals("visible;")) {
                    if (((polizaMan.getPolizaEncaFidRol() == null) || (polizaMan.getPolizaEncaFidRol().equals(new String())))) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Rol Fiduciario no puede estar vacío..."));
                    }
                    if (((polizaMan.getPolizaEncaFidPers() == null) || (polizaMan.getPolizaEncaFidPers().equals(new String())))) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Participante no puede estar vacío..."));
                    }
                }
                if (mensajesDeError.isEmpty()) {
                    if (geneVisible.getGenericoVisible00().equals("visible;")) {
                        String[] arrPolizaPersFid = polizaMan.getPolizaEncaFidPers().split("»");
                        polizaMan.setPolizaEncaFidNum(Integer.parseInt(arrPolizaPersFid[1].trim()));
                    }
                    String[] arrPoliza = polizaMan.getPolizaEncaNombre().split("»");
                    polizaMan.setPolizaEncaNumero(arrPoliza[1].trim());
                    polizaMan.setPolizaEncaFechaVal(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                    polizaMan.setPolizaEncaFechaMovto(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()));
                    polizaMan.setPolizaEncaBitUsuario((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero"));
                    oContabilidad = new CContabilidad();
                    if (oContabilidad.onPolizaMan_Aplica(polizaMan).equals(Boolean.TRUE)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente"));
                        seleccionaMovto = null;
                        onContabilidadPolMan_ConsultaLimpia();
                        calendario = Calendar.getInstance();
                        calendario.setTime(geneFecha.getGenericoFecha00());
                        cbc.setCriterioAX1(Long.parseLong(polizaMan.getPoliza00Valor()));
                        cbc.setTxtCriterioAX1(cbc.getCriterioAX1().toString());
                        cbc.setCriterioFechaYYYY(calendario.get(Calendar.YEAR));
                        cbc.setCriterioFechaMM(calendario.get(Calendar.MONTH) + 1);
                        cbc.setCriterioFechaDD(calendario.get(Calendar.DAY_OF_MONTH));
                        //consultaMovtos = oContabilidad.onPolizaMan_Consulta(cbc);
                        onContabilidadPolMan_ConsultaEjecuta();
                        mensajeConfrima.setMensajeConfirmaOrigen("polizaMan");
                        mensajeConfrima.setMensajeConfirmacionAccion("polizaAplica");
                        mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                        mensajeConfrima.setMensajeConfirmaMensaje1("Se generó la póliza con el folio: " + polizaMan.getPolizaEncaFolioSalida());
                        polizaMan.setPolizaEncaFolio(polizaMan.getPolizaEncaFolioSalida());
                        LocalDateTime locaDate = LocalDateTime.now();
                        int hours = locaDate.getHour();
                        int minutes = locaDate.getMinute();
                        int seconds = locaDate.getSecond();
                        System.out.println("Hora actual : " + hours + ":" + minutes + ":" + seconds);
                        polizaMan.setPolizaEncaHoraAplica(hours + ":" + minutes + ":" + seconds);

                        RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
                    } else {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                    }
                    oContabilidad = null;
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_Aplica()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onPooliza_RetieneFechaValor() {
            geneFecha.setGenericoFecha00(geneFecha.getGenericoFecha01());
            RequestContext.getCurrentInstance().execute("dlgCambioFecha.hide();");
        }

        public void onPooliza_CambiaFechaValor() {
            RequestContext.getCurrentInstance().execute("dlgCambioFecha.hide();");
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Ya se puede continuar con la captura...");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
        }

        public void onPooliza_ValidaFechaValor() {
            try {

                if ((geneFecha.getGenericoFecha00() == null) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Valor no puede estar vacío...");
                }

                Date fechaValor = new java.sql.Date(geneFecha.getGenericoFecha00().getTime());

                oComunes = new CComunes();
                valorRetorno = oComunes.onCommunes_BuscaDiasFeriados(fechaValor);
                if (valorRetorno.equals(Boolean.TRUE) && validacion) {
                    validacion = Boolean.FALSE;
                    geneFecha.setGenericoFecha00(null);
                    geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));

                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Valor no debe ser un día feriado");
                }

                Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
                int fechaValida = fechaValor.compareTo(fechSistema);

                if (fechaValida > 0 && validacion) {
                    validacion = Boolean.FALSE;
                    geneFecha.setGenericoFecha00(null);
                    geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Valor no puede ser mayor a la fecha contable...");
                }
                Calendar calendarFechaDeSistema = Calendar.getInstance();
                calendarFechaDeSistema.setTime(fechSistema);

                Calendar calendarFechaDeValor = Calendar.getInstance();
                calendarFechaDeValor.setTime(fechaValor);

                if (((calendarFechaDeValor.get(Calendar.YEAR) != calendarFechaDeSistema.get(Calendar.YEAR))
                        || (calendarFechaDeValor.get(Calendar.MONTH) != calendarFechaDeSistema.get(Calendar.MONTH)))
                        && validacion) {
                    if (!oComunes.onCommunes_ValidaMesCerrado(fechaValor)) {
                        geneFecha.setGenericoFecha00(null);
                        geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se puede aplicar movimiento con esta fecha");
                    }
                }
                if (validacion) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.update("frmPolizaMan:dlgCambioFecha");
                    context.execute("dlgCambioFecha.show();");
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage());
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadPolMan_Limpia() {
            try {
                onLimpia();
            } catch (ArithmeticException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_Limpia()");
            }
        }

        public void onContabilidadPolMan_CargaPersonas() {
            try {
                if ((polizaMan.getPolizaEncaFidRol() != null) && (!polizaMan.getPolizaEncaFidRol().equals(new String()))) {
                    if ((polizaMan.getPoliza00Valor() != null) && (!polizaMan.getPoliza00Valor().equals(new String()))) {
                        oComunes = new CComunes();

                        if (!oComunes.onComunes_ObtenListadoParticipantePorRolFid(Long.parseLong(polizaMan.getPoliza00Valor()), polizaMan.getPolizaEncaFidRol()).isEmpty()) {
                            listaFidPers = oComunes.onComunes_ObtenListadoParticipantePorRolFid(Long.parseLong(polizaMan.getPoliza00Valor()), polizaMan.getPolizaEncaFidRol());
                        } else {
//                            RequestContext.getCurrentInstance().execute("alert('No se encontraron participantes')");
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se encontraron participantes");
                        }

                        oComunes = null;

                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccione un Fideicomiso");
//                        RequestContext.getCurrentInstance().execute("alert('Seleccione un Fideicomiso')");
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccione un rol fiduciario");
//                    RequestContext.getCurrentInstance().execute("alert('Seleccione un rol fiduciario')");
                }
            } catch (NumberFormatException | SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_CargaPersonas()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onContabilidadPolMan_ArchivoVentanaCierra() {
            try {
                listaErr = null;
                RequestContext.getCurrentInstance().execute("dlgPolizaCarga.hide();");
                /*cbc.setCriterioFechaYYYY(usuarioNumero);
                cbc.setCriterioFechaMM(usuarioNumero);
                cbc.setCriterioUsuario(usuarioNumero);
                oContabilidad  = new CContabilidad();
                consultaMovtos = oContabilidad.onConsMovto_ConsultaPolizaMan(cbc);
                oContabilidad  = null;*/
                FacesContext.getCurrentInstance().getExternalContext().redirect((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto").toString().concat("/vista/contabilidadPolizaMan.sb"));
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ArchivoVentanaCierra()");
            }
        }

        public void onContabilidadPolMan_ArchivoVentanaCierraAdm() {
            try {
                listaErr = null;
                RequestContext.getCurrentInstance().execute("dlgPolizaCarga.hide();");
                /*cbc.setCriterioFechaYYYY(usuarioNumero);
                cbc.setCriterioFechaMM(usuarioNumero);
                cbc.setCriterioUsuario(usuarioNumero);
                oContabilidad  = new CContabilidad();
                consultaMovtos = oContabilidad.onConsMovto_ConsultaPolizaMan(cbc);
                oContabilidad  = null;*/
                FacesContext.getCurrentInstance().getExternalContext().redirect((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto").toString().concat("/vista/contabilidadPolizaManAdm.sb"));
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ArchivoVentanaCierraAdm()");
            }
        }

        public void onContabilidadPolMan_ArchivoVentanaDespliega() {
            try {
                if ((polizaMan.getPolizaEncaNombre() == null) || (polizaMan.getPolizaEncaNombre().equals(new String()))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe de seleccionar una póliza...");
                }
                if ((geneFecha.getGenericoFecha00() == null) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Valor no puede estar vacío...");
                }
                if (((polizaMan.getPolizaEncaRedaccion() == null) || (polizaMan.getPolizaEncaRedaccion().equals(new String()))) && (validacion.equals(Boolean.TRUE))) {
                    validacion = Boolean.FALSE;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Redacción no puede estar vacío...");
                }
                if (validacion.equals(Boolean.TRUE)) {
                    RequestContext.getCurrentInstance().execute("dlgPolizaCarga.show();");
                }
            } catch (ArithmeticException Err) {
                logger.error(Err.getMessage() + "onContabilidadPolMan_ArchivoVentanaDespliega()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onContabilidadPolMan_ArchivoTransfiere(FileUploadEvent event) {
            Boolean bValidaReg = Boolean.FALSE;
            try {
                consultaPoliza = new ArrayList<>();
                listaErr = new ArrayList<>();

                //Internet Explorer, obtiene el nombre del archivo y su ruta  
                archivoNombre = event.getFile().getFileName().replace(" ", "_");
                archivoLinea = new String();
                archivoLineaNum = 1;
                //Nos vamos por las piedritas por culpa de IE  
                archivo = new File(archivoNombre);
                archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(archivo.getName());
                archivo = null;

                archivo = new File(archivoNombre);
                fos = new FileOutputStream(archivo);
                fos.write(event.getFile().getContents());
                fos.close();

                //Validación del archivo
                fis = new FileInputStream(archivoNombre);
                int a = 0;

                while ((a = fis.read()) != -1) {
                    String car = String.valueOf((char) a);
                    if (!car.equals("\n")) {
                        archivoLinea += car;
                    } else {
                        if ((polizaMan.getPolizaEncaTipoPol().equals("LIQUIDACION")) || (polizaMan.getPolizaEncaTipoPol().equals("DEPOSITO"))) {
                            bValidaReg = onPolizaMasivaValidaRegistroConPersonas(polizaMan.getPolizaEncaCamposNum() + 2);
                        } else {
                            try {
                                bValidaReg = onPolizaMasivaValidaRegistroNormal(polizaMan.getPolizaEncaCamposNum());
                            } catch (ArrayIndexOutOfBoundsException e) {
                                logger.error("Error en function");
                            }
                        }
                        if (bValidaReg.equals(Boolean.TRUE)) {
                            ContabilidadPolizaManBean pm = new ContabilidadPolizaManBean();
                            onPolizaMasivaLlenaCampos01(pm);
                            consultaPoliza.add(pm);
                            pm = null;
                        }
                        archivoLineaNum++;
                        archivoLinea = new String();
                    }
                }
                fis.close();

                //Aplicamos cada regisrto del archivo contablemente  
                if (listaErr.isEmpty()) {
                    archivoLineaNum = 1;
                    for (int itemPol = 0; itemPol <= consultaPoliza.size() - 1; itemPol++) {
                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onPolizaMan_Aplica(consultaPoliza.get(itemPol)).equals(Boolean.TRUE)) {
                            listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Registro aplicado correctamente"));
                        } else {
                            listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error: ").concat(oContabilidad.getMensajeError()));
                        }
                        oContabilidad = null;

                        archivoLineaNum++;
                    }
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso finalizado. Verifique sus resultados");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No fue aplicado ningún registro del archivo. Se encontraron errores");
                }
                //Borramos el archivo
                if (archivo.exists()) {
                    archivo.delete();
                }
                archivo = null;
            } catch (IOException FileErr) {
                logger.error(FileErr.getMessage() + "onContabilidadPolMan_ArchivoTransfiere()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
        //Funcione privadas

        private Boolean onPolizaMasivaValidaRegistroNormal(int maxColumna) {
            Boolean bValidaRegistro = Boolean.FALSE;
            try {
                String[] arrLinea = archivoLinea.split("\t");
                if (arrLinea.length == maxColumna) {
                    Boolean bValida00 = Boolean.TRUE;
                    Boolean bValida01 = Boolean.TRUE;
                    Boolean bValida02 = Boolean.TRUE;
                    Boolean bValida03 = Boolean.TRUE;
                    Boolean bValida04 = Boolean.TRUE;
                    String contratoNum = new String();
                    String contratoSub = new String();
                    String contratoInv = new String();
                    for (short itemCol = 0; itemCol <= arrLinea.length - 1; itemCol++) {
                        if (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("CONTRATO")) {
                            contratoNum = arrLinea[itemCol].trim(); //Pongo esta variable para la validacion del subcontrato
                            if (onValidaCampoNumerico(contratoNum, Boolean.FALSE).equals(Boolean.TRUE)) {
                                oContrato = new CContrato();
                                if (oContrato.onContrato_VerificaExistencia(Long.parseLong(contratoNum)).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": No existe el Fideicomiso ".concat(contratoNum)));
                                    bValida00 = Boolean.FALSE;
                                }
                                oContrato = null;
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::Fideicomiso ".concat(contratoNum)));
                                bValida00 = Boolean.FALSE;
                            }
                        }
                        if (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("SUBCONTRATO")) {
                            contratoSub = arrLinea[itemCol].trim();
                            if (onValidaCampoNumerico(contratoSub, Boolean.FALSE).equals(Boolean.TRUE)) {
                                if (!contratoSub.equals("0")) {
                                    oContrato = new CContrato();
                                    if (oContrato.onContrato_VerificaExistenciaSubContrato(Long.decode(contratoNum), Integer.decode(contratoSub), usuarioNumero).equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": El sub Fideicomiso ".concat(contratoSub).concat(" no se encuentra asignado al Fideicomiso ".concat(contratoNum))));
                                        bValida01 = Boolean.FALSE;
                                    }
                                    oContrato = null;
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::SubFideicomiso ".concat(contratoSub)));
                                bValida01 = Boolean.FALSE;
                            }
                        }
                        if (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("CTOINVERSION")) {
                            contratoInv = arrLinea[itemCol].trim();
                            if (onValidaCampoNumerico(contratoInv, Boolean.TRUE).equals(Boolean.FALSE)) {
                                //oContrato = new CContrato();
                                //if (oContrato.onContratoInver_VerificaExistencia(Long.parseLong(contratoNum), Integer.parseInt(contratoSub), Long.parseLong(contratoInv)).equals(Boolean.FALSE)){
                                //    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": El Cto. Inv. ".concat(contratoSub).concat(" no se encuentra asignado al contrato ".concat(contratoNum))));
                                //}
                                //oContrato = null;
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[itemCol]).concat(" ").concat(arrLinea[itemCol].trim())));
                                bValida02 = Boolean.FALSE;
                            }
                        }
                        if ((polizaMan.getPolizaEncaCamposArr()[itemCol].equals("CTACHEQUES")) || (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("TERCEROS"))
                                || (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("EJERCICIO")) || (polizaMan.getPolizaEncaCamposArr()[itemCol].equals("PROVEEDOR"))) {
                            if (onValidaCampoNumerico(arrLinea[itemCol].trim(), Boolean.TRUE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[itemCol]).concat(" ").concat(arrLinea[itemCol].trim())));
                                bValida03 = Boolean.FALSE;
                            }
                        }
                        if ((polizaMan.getPolizaEncaCamposArr()[itemCol].contains("01")) || (polizaMan.getPolizaEncaCamposArr()[itemCol].contains("02")) || (polizaMan.getPolizaEncaCamposArr()[itemCol].contains("46"))) {
                            if (onValidaCampoNumerico(arrLinea[itemCol].trim(), Boolean.TRUE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[itemCol]).concat(" ").concat(arrLinea[itemCol].trim())));
                                bValida04 = Boolean.FALSE;
                            }
                        }
                        onPolizaMasivaLlenaCampos00(itemCol, polizaMan.getPolizaEncaCamposArr()[itemCol], arrLinea[itemCol].trim());
                    }
                    if ((bValida00.equals(Boolean.TRUE)) && (bValida01.equals(Boolean.TRUE)) && (bValida02.equals(Boolean.TRUE)) && (bValida03.equals(Boolean.TRUE)) && (bValida04.equals(Boolean.TRUE))) {
                        bValidaRegistro = Boolean.TRUE;
                    }
                } else {
                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": La estructura del registro, no corresponde con la estructura de la póliza seleccionada."));
                }
            } catch (ArrayIndexOutOfBoundsException Err) {
                logger.error(Err.getMessage() + "onPolizaMasivaValidaRegistroNormal()");
            }
            return bValidaRegistro;
        }

        private Boolean onPolizaMasivaValidaRegistroConPersonas(int maxColumna) {
            Boolean bValidaRegistro = Boolean.FALSE;
            try {
                String[] arrLinea00 = archivoLinea.split("\t");
                if (arrLinea00.length == maxColumna) {
                    Boolean bValida00 = Boolean.TRUE;
                    Boolean bValida01 = Boolean.TRUE;
                    Boolean bValida02 = Boolean.TRUE;
                    Boolean bValida03 = Boolean.TRUE;
                    Boolean bValida04 = Boolean.TRUE;
                    int itemCpto = 0;
                    String ctoNum = new String();
                    String ctoSub = new String();
                    String ctoCta = new String();
                    for (short itemCol = 2; itemCol <= arrLinea00.length - 1; itemCol++) {
                        if (polizaMan.getPolizaEncaCamposArr()[itemCpto].equals("CONTRATO")) {
                            ctoNum = arrLinea00[itemCol].trim();
                            if (onValidaCampoNumerico(ctoNum, Boolean.FALSE).equals(Boolean.TRUE)) {
                                oContrato = new CContrato();
                                if (oContrato.onContrato_VerificaExistencia(Long.parseLong(ctoNum)).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": No existe el Fideicomiso ".concat(ctoNum)));
                                    bValida00 = Boolean.FALSE;
                                }
                                oContrato = null;
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::Contrato ".concat(ctoNum)));
                                bValida00 = Boolean.FALSE;
                            }
                        }
                        if (polizaMan.getPolizaEncaCamposArr()[itemCpto].equals("SUBCONTRATO")) {
                            ctoSub = arrLinea00[itemCol].trim();
                            if (onValidaCampoNumerico(ctoSub, Boolean.FALSE).equals(Boolean.TRUE)) {
                                if (!ctoSub.equals("0")) {
                                    oContrato = new CContrato();
                                    if (oContrato.onContrato_VerificaExistenciaSubContrato(Long.decode(ctoSub), Integer.decode(ctoSub), usuarioNumero).equals(Boolean.FALSE)) {
                                        listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": El sub Fideicomiso ".concat(ctoSub).concat(" no se encuentra asignado al Fideicomiso ".concat(ctoNum))));
                                        bValida01 = Boolean.FALSE;
                                    }
                                    oContrato = null;
                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::SubFideicomiso ".concat(ctoSub)));
                                bValida01 = Boolean.FALSE;
                            }
                        }
                        if (polizaMan.getPolizaEncaCamposArr()[itemCpto].equals("CTACHEQUES")) {
                            ctoCta = arrLinea00[itemCol].trim();
                            if (onValidaCampoNumerico(ctoCta, Boolean.FALSE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::CtaCheques ".concat(ctoCta)));
                                bValida02 = Boolean.FALSE;
                            }
                        }
                        if ((polizaMan.getPolizaEncaCamposArr()[itemCpto].contains("01")) || (polizaMan.getPolizaEncaCamposArr()[itemCpto].contains("02")) || (polizaMan.getPolizaEncaCamposArr()[itemCpto].contains("46"))) {
                            if (onValidaCampoNumerico(arrLinea00[itemCol].trim(), Boolean.TRUE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[itemCol]).concat(" ").concat(arrLinea00[itemCol].trim())));
                                bValida03 = Boolean.FALSE;
                            }
                        }
                        onPolizaMasivaLlenaCampos00(itemCpto, polizaMan.getPolizaEncaCamposArr()[itemCpto], arrLinea00[itemCol].trim());
                        itemCpto++;
                    }
                    //Validamos la información de la persona y de la cuenta de cheques  
                    if ((bValida00.equals(Boolean.TRUE)) && (bValida01.equals(Boolean.TRUE)) && (bValida02.equals(Boolean.TRUE)) && (bValida03.equals(Boolean.TRUE))) {
                        oPersFid = new CPersonaFid();
                        if (ctoNum != null && arrLinea00[0] != null && arrLinea00[1] != null && ctoCta != null) {
                            if (oPersFid.onCuenta_VerificaExistencia(Long.parseLong(ctoNum), arrLinea00[0], Short.parseShort(arrLinea00[1]), ctoCta).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": No existe o no está asignada la cuenta de cheques ".concat(ctoCta)));
                                bValida04 = Boolean.FALSE;
                            } else {
                                polizaMan.setPolizaEncaFidRol(arrLinea00[0]);
                                polizaMan.setPolizaEncaFidNum(Integer.parseInt(arrLinea00[1]));
                            }
                        }
                        oPersFid = null;
                    }
                    if ((bValida00.equals(Boolean.TRUE)) && (bValida01.equals(Boolean.TRUE)) && (bValida02.equals(Boolean.TRUE)) && (bValida03.equals(Boolean.TRUE)) && (bValida04.equals(Boolean.TRUE))) {
                        bValidaRegistro = Boolean.TRUE;
                    }
                } else {
                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": La estructura del registro, no corresponde con la estructura de la póliza seleccionada."));
                }
            } catch (ArithmeticException Err) {
                logger.error(Err.getMessage() + "onPolizaMasivaValidaRegistroConPersonas()");
            } finally {
                onFinalizaObjetos();

            }
            return bValidaRegistro;
        }

        public void onContabilidadPolMan_BulkLoad(FileUploadEvent event) {
            Boolean validacion = true;
            String nombreArchivo;
            try {
                consultaPoliza = new ArrayList<>();
                listaErr = new ArrayList<>();

                //Internet Explorer, obtiene el nombre del archivo y su ruta  
                archivoNombre = event.getFile().getFileName().replace(" ", "_");
                archivoLinea = new String();
                archivoLineaNum = 1;
                //Nos vamos por las piedritas por culpa de IE  
                archivo = new File(archivoNombre);
                archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(archivo.getName());

                oContabilidad = new CContabilidad();
                nombreArchivo = archivo.getName();
                if (nombreArchivo.length() > 50) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Nombre Archivo no puede contener mas de 50 caracteres...");
                    validacion = false;
                }

                if (validacion && oContabilidad.onPolizaMan_Valida_Archivo(nombreArchivo)) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Archivo Existe en el Control de procesados.");
                    validacion = false;

                }

                oContabilidad = null;

                archivo = null;
                if (validacion) {

                    archivo = new File(archivoNombre);
                    fos = new FileOutputStream(archivo);
                    fos.write(event.getFile().getContents());
                    fos.close();

                    /// Read file line by line.
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(archivo);
                        br = new BufferedReader(fr);

                        String line = new String();
                        List<String> fileColumns = new ArrayList<>();
                        List<String> policyColumns = getTurnedColumnsByPolicy();

                        archivoLineaNum = 0;
                        while ((line = br.readLine()) != null) {
                            archivoLineaNum++;
                            String[] lineArr = line.split("\t");

                            fileColumns = getFileColumns(line);
                            if ((lineArr.length % 2 == 0) && findSubSet(fileColumns.toArray(new String[0]), policyColumns.toArray(new String[0]))) {
                                lineValidation(line);

                            } else {
                                listaErr.add("POLIZA MASIVA -> Renglon ".concat(archivoLineaNum.toString()).concat(" La estructura del Renglon no es correcta "));
                                validacion = false;
                            }

                        }
                    } catch (IOException e) {
                        logger.error("Error al procesar archivo: onContabilidadPolMan_BulkLoad()");
                    } finally {
                        if (fr != null) {
                            fr.close();
                        }
                        if (br != null) {
                            br.close();
                        }
                    }
                    ///****
                    //Aplicamos cada regisrto del archivo contablemente
                    Integer archivoLineaErr = 0;
                    if (validacion && listaErr.isEmpty()) {
                        archivoLineaNum = 1;
                        for (int itemPol = 0; itemPol <= consultaPoliza.size() - 1; itemPol++) {
                            oContabilidad = new CContabilidad();
                            if (oContabilidad.onPolizaMan_Aplica(consultaPoliza.get(itemPol)).equals(Boolean.TRUE)) {
                                listaErr.add("POLIZA MASIVA -> Renglon ".concat(archivoLineaNum.toString()).concat(oContabilidad.getMensajePoliza()));
                            } else {
                                listaErr.add("POLIZA MASIVA -> Renglon ".concat(archivoLineaNum.toString()).concat(": Error: ").concat(oContabilidad.getMensajeError()));
                                archivoLineaErr++;
                            }
                            oContabilidad = null;
                            archivoLineaNum++;
                        }
                        archivoLineaNum--;
                        if (archivoLineaErr == 0) {
                            listaErr.add("POLIZA MASIVA -> Se procesaron ".concat(archivoLineaNum.toString()).concat(" movimientos"));
                        } else {
                            listaErr.add("POLIZA MASIVA -> Se procesaron ".concat(archivoLineaNum.toString()).concat(" movimientos, ").concat(archivoLineaErr.toString()).concat(" con error no se aplicaron"));
                        }
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Proceso finalizado. Verifique sus resultados");
                        oContabilidad = new CContabilidad();
                        oContabilidad.onPolizaMan_Registra_Archivo(nombreArchivo, new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                        oContabilidad = null;
                    } else {
//                        if (validacion) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No fue aplicado ningún registro del archivo. Se encontraron errores");
//                        }
                    }

                    //Borramos el archivo
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    archivo = null;
                }
            } catch (IOException FileErr) {
                logger.error(FileErr.getMessage() + "onContabilidadPolMan_BulkLoad()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        private void lineValidation(String line) {
            try {
                Boolean bValidaReg = Boolean.FALSE;
                Map<String, String> lineValues = getFileValues(line);

                if (polizaMan.getPolizaEncaTipoPol() != null && ((polizaMan.getPolizaEncaTipoPol().equals("LIQUIDACION")) || (polizaMan.getPolizaEncaTipoPol().equals("DEPOSITO")))) {
                    bValidaReg = onBulkPolicy_CheckRegistry(lineValues, "CON_PERSONA");
                } else {
                    bValidaReg = onBulkPolicy_CheckRegistry(lineValues, "NORMAL");
                }

                if (bValidaReg.equals(Boolean.TRUE)) {
                    ContabilidadPolizaManBean pm = new ContabilidadPolizaManBean();
                    onPolizaMasivaLlenaCampos01(pm);
                    consultaPoliza.add(pm);
                    pm = null;
                }
                //else {
                //    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": La estructura del registro, no corresponde con la estructura de la póliza seleccionada."));
                //}
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "registerValidation()");
            }
        }

        private Boolean onBulkPolicy_CheckRegistry(Map<String, String> lineValues, String policyType) {
            Boolean bValidaRegistro = Boolean.FALSE;
            try {
                Boolean validContract = Boolean.TRUE;
                Boolean validSubContract = Boolean.TRUE;
                Boolean validCheckAccount = Boolean.TRUE;
                Boolean validInvestmentContract = Boolean.TRUE;
                Boolean validCurrency = Boolean.TRUE;
                Boolean validPersonAccount = Boolean.TRUE;

                String contractNumber = new String();
                String subContract = new String();
                String checkAccount = new String();
                String investmentContract = new String();

                for (int i = 0; i < polizaMan.getPolizaEncaCamposArr().length; i++) {
                    if (polizaMan.getPolizaEncaCamposArr()[i] != null && !polizaMan.getPolizaEncaCamposArr()[i].equals("")) {
                        if (polizaMan.getPolizaEncaCamposArr()[i].equals("CONTRATO")) {
                            contractNumber = lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim();

                            if (onValidaCampoNumerico(contractNumber, Boolean.FALSE).equals(Boolean.TRUE)) {
//                                oContrato = new CContrato();
//                                if (oContrato.onContrato_VerificaExistencia(Long.parseLong(contractNumber)).equals(Boolean.FALSE)) {
//                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": No existe el Fideicomiso ".concat(contractNumber).concat(" o no le pertenece")));
//                                    validContract = Boolean.FALSE;
//                               }
//                                oContrato = null;
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::Fideicomiso ".concat(contractNumber)));
                                validContract = Boolean.FALSE;
                            }
                        }
                        if (polizaMan.getPolizaEncaCamposArr()[i].equals("SUBCONTRATO")) {
                            subContract = lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim();
                            if (onValidaCampoNumerico(subContract, Boolean.FALSE).equals(Boolean.TRUE)) {
//                                if (!subContract.equals("0")) {
//                                    oContrato = new CContrato();
//                                    if (oContrato.onContrato_VerificaExistenciaSubContrato(Long.parseLong(subContract), Integer.parseInt(subContract)).equals(Boolean.FALSE)) {
//                                        listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": El sub Fideicomiso ".concat(subContract).concat(" no se encuentra asignado al Fideicomiso ".concat(contractNumber))));
//                                        validSubContract = Boolean.FALSE;
//                                    }
//                                    oContrato = null;
//                                }
                            } else {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::SubFideicomiso ".concat(subContract)));
                                validSubContract = Boolean.FALSE;
                            }
                        }
                        if (policyType.equals("NORMAL")) {
                            if (polizaMan.getPolizaEncaCamposArr()[i].equals("CTOINVERSION")) {
                                investmentContract = lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim();
                                if (onValidaCampoNumerico(investmentContract, Boolean.TRUE).equals(Boolean.TRUE)) {

                                } else {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[i]).concat(" ").concat(lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim())));
                                    validInvestmentContract = Boolean.FALSE;
                                }
                            }
                            if ((polizaMan.getPolizaEncaCamposArr()[i].equals("CTACHEQUES")) || (polizaMan.getPolizaEncaCamposArr()[i].equals("TERCEROS"))
                                    || (polizaMan.getPolizaEncaCamposArr()[i].equals("EJERCICIO")) || (polizaMan.getPolizaEncaCamposArr()[i].equals("PROVEEDOR"))) {
                                if (onValidaCampoNumerico(lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim(), Boolean.TRUE).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[i]).concat(" ").concat(lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim())));
                                    validCheckAccount = Boolean.FALSE;
                                }
                            }
                        } else {
                            if (polizaMan.getPolizaEncaCamposArr()[i].equals("CTACHEQUES")) {
                                checkAccount = lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim();
                                if (onValidaCampoNumerico(checkAccount, Boolean.FALSE).equals(Boolean.FALSE)) {
                                    listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::CtaCheques ".concat(checkAccount)));
                                    validCheckAccount = Boolean.FALSE;
                                }
                            }
                        }
                        if ((polizaMan.getPolizaEncaCamposArr()[i].contains("01")) || (polizaMan.getPolizaEncaCamposArr()[i].contains("02")) || (polizaMan.getPolizaEncaCamposArr()[i].contains("46"))) {
                            if (onValidaCampoNumerico(lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim(), Boolean.TRUE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .::".concat(polizaMan.getPolizaEncaCamposArr()[i]).concat(" ").concat(lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim())));
                                validCurrency = Boolean.FALSE;
                            }
                        }
                        onPolizaMasivaLlenaCampos00(i, polizaMan.getPolizaEncaCamposArr()[i], lineValues.get(polizaMan.getPolizaEncaCamposArr()[i]).trim());
                    }
                }
                Boolean conPersona = Boolean.TRUE;
                if (policyType.equals("CON_PERSONA")) {
                    if ((validContract.equals(Boolean.TRUE)) && (validSubContract.equals(Boolean.TRUE)) && (validCheckAccount.equals(Boolean.TRUE)) && (validCurrency.equals(Boolean.TRUE))) {
                        oPersFid = new CPersonaFid();
//                        if (oPersFid.onCuenta_VerificaExistencia(Long.parseLong(contractNumber), lineValues.get("TIPO_PERSONA"), Short.parseShort(lineValues.get("NUMERO_PERSONA")), checkAccount).equals(Boolean.FALSE)) {
//                            listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": No existe o no está asignada la cuenta de cheques ".concat(checkAccount)));
//                            validPersonAccount = Boolean.FALSE;
//                        } else {
                        if (lineValues.containsKey("TIPO_PERSONA")) {
                            polizaMan.setPolizaEncaFidRol(lineValues.get("TIPO_PERSONA"));
                        } else {
                            conPersona = Boolean.FALSE;
                        }
                        if (lineValues.containsKey("NUMERO_PERSONA")) {
                            if (onValidaCampoNumerico(lineValues.get("NUMERO_PERSONA").trim(), Boolean.FALSE).equals(Boolean.FALSE)) {
                                listaErr.add("Linea ".concat(archivoLineaNum.toString()).concat(": Error de tipo de dato en el campo .:: ".concat("NUMERO_PERSONA ").concat(lineValues.get("NUMERO_PERSONA").trim())));
                                validCurrency = Boolean.FALSE;
                            } else {
                                polizaMan.setPolizaEncaFidNum(Integer.parseInt(lineValues.get("NUMERO_PERSONA").trim()));
                            }
                        } else {
                            conPersona = Boolean.FALSE;
                        }
                        if (!conPersona) {
                            listaErr.add("POLIZA MASIVA -> Renglon ".concat(archivoLineaNum.toString()).concat(" La estructura del Renglon no es correcta "));
                            validCurrency = false;
                        }
//                        }
                        oPersFid = null;
                    }
                }

                if ((validContract.equals(Boolean.TRUE))
                        && (validSubContract.equals(Boolean.TRUE))
                        && (validInvestmentContract.equals(Boolean.TRUE))
                        && (validCheckAccount.equals(Boolean.TRUE))
                        && (validCurrency.equals(Boolean.TRUE))
                        && (validPersonAccount.equals(Boolean.TRUE))) {
                    bValidaRegistro = Boolean.TRUE;
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onPolizaMasivaValidaRegistroConPersonas()");
            }
            return bValidaRegistro;
        }

        private List<String> getFileColumns(String line) {
            List<String> columns = new ArrayList<>();

            String[] lineArr = line.split("\t");

            for (int i = 0; i < lineArr.length; i++) {
                if (i % 2 == 0) {
                    columns.add(lineArr[i]);
                }
            }

            return columns;
        }

        private Map<String, String> getFileValues(String line) {
            Map<String, String> values = new HashMap();

            String[] lineArr = line.split("\t");

            for (int i = 0; i < lineArr.length; i++) {
                if (i % 2 == 1) {
                    values.put(lineArr[i - 1], lineArr[i]);
                }
            }

            return values;
        }

        private List<String> getTurnedColumnsByPolicy() {
            List<String> columns = new ArrayList<>();
            try {
                for (int i = 0; i < polizaMan.getPolizaEncaCamposArr().length - 1; i++) {
                    if (polizaMan.getPolizaEncaCamposArr()[i] != null && !polizaMan.getPolizaEncaCamposArr()[i].equals("")) {
                        columns.add(polizaMan.getPolizaEncaCamposArr()[i]);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException Err) {
                logger.error(Err.getMessage() + "getTurnedColumnsByPolicy()");
            }
            return columns;
        }

        public boolean findSubSet(String[] first, String[] second) {
            HashSet<String> result = new HashSet<>();

            for (int i = 0; i < first.length; ++i) {
                result.add(first[i]);
            }

            for (int i = 0; i < second.length; ++i) {
                if (result.contains(second[i]) == false) {
                    return false;
                }
            }

            return true;
        }

        private void onPolizaMasivaLlenaCampos01(ContabilidadPolizaManBean cpm) {
            cpm.setPoliza00Etiqueta(polizaMan.getPoliza00Etiqueta());
            cpm.setPoliza00Valor(polizaMan.getPoliza00Valor());
            cpm.setPoliza00Visible(polizaMan.getPoliza00Visible());

            cpm.setPoliza01Etiqueta(polizaMan.getPoliza01Etiqueta());
            cpm.setPoliza01Valor(polizaMan.getPoliza01Valor());
            cpm.setPoliza01Visible(polizaMan.getPoliza01Visible());

            cpm.setPoliza02Etiqueta(polizaMan.getPoliza02Etiqueta());
            cpm.setPoliza02Valor(polizaMan.getPoliza02Valor());
            cpm.setPoliza02Visible(polizaMan.getPoliza02Visible());

            cpm.setPoliza03Etiqueta(polizaMan.getPoliza03Etiqueta());
            cpm.setPoliza03Valor(polizaMan.getPoliza03Valor());
            cpm.setPoliza03Visible(polizaMan.getPoliza03Visible());

            cpm.setPoliza04Etiqueta(polizaMan.getPoliza04Etiqueta());
            cpm.setPoliza04Valor(polizaMan.getPoliza04Valor());
            cpm.setPoliza04Visible(polizaMan.getPoliza04Visible());

            cpm.setPoliza05Etiqueta(polizaMan.getPoliza05Etiqueta());
            cpm.setPoliza05Valor(polizaMan.getPoliza05Valor());
            cpm.setPoliza05Visible(polizaMan.getPoliza05Visible());

            cpm.setPoliza06Etiqueta(polizaMan.getPoliza06Etiqueta());
            cpm.setPoliza06Valor(polizaMan.getPoliza06Valor());
            cpm.setPoliza06Visible(polizaMan.getPoliza06Visible());

            cpm.setPoliza07Etiqueta(polizaMan.getPoliza07Etiqueta());
            cpm.setPoliza07Valor(polizaMan.getPoliza07Valor());
            cpm.setPoliza07Visible(polizaMan.getPoliza07Visible());

            cpm.setPoliza08Etiqueta(polizaMan.getPoliza08Etiqueta());
            cpm.setPoliza08Valor(polizaMan.getPoliza08Valor());
            cpm.setPoliza08Visible(polizaMan.getPoliza08Visible());

            cpm.setPoliza09Etiqueta(polizaMan.getPoliza09Etiqueta());
            cpm.setPoliza09Valor(polizaMan.getPoliza09Valor());
            cpm.setPoliza09Visible(polizaMan.getPoliza09Visible());

            cpm.setPoliza10Etiqueta(polizaMan.getPoliza10Etiqueta());
            cpm.setPoliza10Valor(polizaMan.getPoliza10Valor());
            cpm.setPoliza10Visible(polizaMan.getPoliza10Visible());

            cpm.setPoliza11Etiqueta(polizaMan.getPoliza11Etiqueta());
            cpm.setPoliza11Valor(polizaMan.getPoliza11Valor());
            cpm.setPoliza11Visible(polizaMan.getPoliza11Visible());

            cpm.setPoliza12Etiqueta(polizaMan.getPoliza12Etiqueta());
            cpm.setPoliza12Valor(polizaMan.getPoliza12Valor());
            cpm.setPoliza12Visible(polizaMan.getPoliza12Visible());

            cpm.setPoliza13Etiqueta(polizaMan.getPoliza13Etiqueta());
            cpm.setPoliza13Valor(polizaMan.getPoliza13Valor());
            cpm.setPoliza13Visible(polizaMan.getPoliza13Visible());

            cpm.setPoliza14Etiqueta(polizaMan.getPoliza14Etiqueta());
            cpm.setPoliza14Valor(polizaMan.getPoliza14Valor());
            cpm.setPoliza14Visible(polizaMan.getPoliza14Visible());

            cpm.setPoliza15Etiqueta(polizaMan.getPoliza15Etiqueta());
            cpm.setPoliza15Valor(polizaMan.getPoliza15Valor());
            cpm.setPoliza15Visible(polizaMan.getPoliza15Visible());

            cpm.setPoliza16Etiqueta(polizaMan.getPoliza16Etiqueta());
            cpm.setPoliza16Valor(polizaMan.getPoliza16Valor());
            cpm.setPoliza16Visible(polizaMan.getPoliza16Visible());

            cpm.setPoliza17Etiqueta(polizaMan.getPoliza17Etiqueta());
            cpm.setPoliza17Valor(polizaMan.getPoliza17Valor());
            cpm.setPoliza17Visible(polizaMan.getPoliza17Visible());

            cpm.setPoliza18Etiqueta(polizaMan.getPoliza18Etiqueta());
            cpm.setPoliza18Valor(polizaMan.getPoliza18Valor());
            cpm.setPoliza18Visible(polizaMan.getPoliza18Visible());

            cpm.setPoliza19Etiqueta(polizaMan.getPoliza19Etiqueta());
            cpm.setPoliza19Valor(polizaMan.getPoliza19Valor());
            cpm.setPoliza19Visible(polizaMan.getPoliza19Visible());

            cpm.setPolizaEncaAportPatrim(polizaMan.getPolizaEncaAportPatrim());

            cpm.setPolizaEncaBitPantalla(polizaMan.getPolizaEncaBitPantalla());
            cpm.setPolizaEncaBitTerminal(polizaMan.getPolizaEncaBitTerminal());
            cpm.setPolizaEncaBitTipoOper(polizaMan.getPolizaEncaBitTipoOper());
            cpm.setPolizaEncaBitUsuario(polizaMan.getPolizaEncaBitUsuario());

            cpm.setPolizaEncaFechaVal(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
            cpm.setPolizaEncaFechaMovto(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()));
            cpm.setPolizaEncaFidNum(polizaMan.getPolizaEncaFidNum());
            cpm.setPolizaEncaFidPers(polizaMan.getPolizaEncaFidPers());
            cpm.setPolizaEncaFidRol(polizaMan.getPolizaEncaFidRol());
            cpm.setPolizaEncaFolio(polizaMan.getPolizaEncaFolio());
            cpm.setPolizaEncaHoraAplica(polizaMan.getPolizaEncaHoraAplica());
            cpm.setPolizaEncaLiqCveCpto(polizaMan.getPolizaEncaLiqCveCpto());
            cpm.setPolizaEncaLiqCveMovto(polizaMan.getPolizaEncaLiqCveMovto());
            cpm.setPolizaEncaNombre(polizaMan.getPolizaEncaNombre());
            cpm.setPolizaEncaNumero(polizaMan.getPolizaEncaNumero());
            cpm.setPolizaEncaRedaccion(polizaMan.getPolizaEncaRedaccion());
            cpm.setPolizaEncaSugerCve(polizaMan.getPolizaEncaSugerCve());
            cpm.setPolizaEncaTipoPol(polizaMan.getPolizaEncaTipoPol());
        }

        private void onPolizaMasivaLlenaCampos00(int campoInd, String campoEti, String campoVal) {
            if (campoInd == 0) {
                polizaMan.setPoliza00Etiqueta(campoEti);
                polizaMan.setPoliza00Valor(campoVal);
            }
            if (campoInd == 1) {
                polizaMan.setPoliza01Etiqueta(campoEti);
                polizaMan.setPoliza01Valor(campoVal);
            }
            if (campoInd == 2) {
                polizaMan.setPoliza02Etiqueta(campoEti);
                polizaMan.setPoliza02Valor(campoVal);
            }
            if (campoInd == 3) {
                polizaMan.setPoliza03Etiqueta(campoEti);
                polizaMan.setPoliza03Valor(campoVal);
            }
            if (campoInd == 4) {
                polizaMan.setPoliza04Etiqueta(campoEti);
                polizaMan.setPoliza04Valor(campoVal);
            }
            if (campoInd == 5) {
                polizaMan.setPoliza05Etiqueta(campoEti);
                polizaMan.setPoliza05Valor(campoVal);
            }
            if (campoInd == 6) {
                polizaMan.setPoliza06Etiqueta(campoEti);
                polizaMan.setPoliza06Valor(campoVal);
            }
            if (campoInd == 7) {
                polizaMan.setPoliza07Etiqueta(campoEti);
                polizaMan.setPoliza07Valor(campoVal);
            }
            if (campoInd == 8) {
                polizaMan.setPoliza08Etiqueta(campoEti);
                polizaMan.setPoliza08Valor(campoVal);
            }
            if (campoInd == 9) {
                polizaMan.setPoliza09Etiqueta(campoEti);
                polizaMan.setPoliza09Valor(campoVal);
            }
            if (campoInd == 10) {
                polizaMan.setPoliza10Etiqueta(campoEti);
                polizaMan.setPoliza10Valor(campoVal);
            }
            if (campoInd == 11) {
                polizaMan.setPoliza11Etiqueta(campoEti);
                polizaMan.setPoliza11Valor(campoVal);
            }
            if (campoInd == 12) {
                polizaMan.setPoliza12Etiqueta(campoEti);
                polizaMan.setPoliza12Valor(campoVal);
            }
            if (campoInd == 13) {
                polizaMan.setPoliza13Etiqueta(campoEti);
                polizaMan.setPoliza13Valor(campoVal);
            }
            if (campoInd == 14) {
                polizaMan.setPoliza14Etiqueta(campoEti);
                polizaMan.setPoliza14Valor(campoVal);
            }
            if (campoInd == 15) {
                polizaMan.setPoliza15Etiqueta(campoEti);
                polizaMan.setPoliza15Valor(campoVal);
            }
            if (campoInd == 16) {
                polizaMan.setPoliza16Etiqueta(campoEti);
                polizaMan.setPoliza16Valor(campoVal);
            }
            if (campoInd == 17) {
                polizaMan.setPoliza17Etiqueta(campoEti);
                polizaMan.setPoliza17Valor(campoVal);
            }
            if (campoInd == 18) {
                polizaMan.setPoliza18Etiqueta(campoEti);
                polizaMan.setPoliza18Valor(campoVal);
            }
            if (campoInd == 19) {
                polizaMan.setPoliza19Etiqueta(campoEti);
                polizaMan.setPoliza19Valor(campoVal);
            }
        }

        private Boolean onValidaCampoNumerico(String valorCampo, Boolean bEsValorMonetario) {
            valorRetorno = Boolean.TRUE;
            if (bEsValorMonetario.equals(Boolean.TRUE)) {
                valorCampo = valorCampo.replace(".", new String());
            }
            if (valorCampo.equals(new String())) {
                return Boolean.FALSE;
            }
            for (int itemCar = 0; itemCar <= valorCampo.length() - 1; itemCar++) {
                String car = valorCampo.substring(itemCar, itemCar + 1);
                if ((car.equals("0")) || (car.equals("1")) || (car.equals("2")) || (car.equals("3")) || (car.equals("4")) || (car.equals("5"))
                        || (car.equals("6")) || (car.equals("7")) || (car.equals("8")) || (car.equals("9"))) {

                } else {
                    valorRetorno = Boolean.FALSE;
                    break;
                }
            }
            return valorRetorno;
        }

        private void onLimpia() {
            onInicializaPoliza();
            onInicializaPolizaEnca();
            geneVisible.setGenericoVisible00("hidden;");
            listaFidPers = null;
            listaFidRol = null;
        }

        private void onInicializaPolizaEnca() {
            polizaMan.setPolizaEncaNombre(null);
            polizaMan.setPolizaEncaNumero(null);
            polizaMan.setPolizaEncaFolio(Long.parseLong("0"));
            polizaMan.setPolizaEncaFolioSalida(Long.parseLong("0"));
            polizaMan.setPolizaEncaHoraAplica(null);
            polizaMan.setPolizaEncaRedaccion(null);
            polizaMan.setPolizaEncaLiqCveCpto(0);
            polizaMan.setPolizaEncaLiqCveMovto("0");
            polizaMan.setPolizaEncaSugerCve(0);
            polizaMan.setPolizaEncaFidNum(0);
            polizaMan.setPolizaEncaFidPers(new String());
            polizaMan.setPolizaEncaFidRol(new String());
            polizaMan.setPolizaEncaBitPantalla("Poliza Manual");
            polizaMan.setPolizaEncaBitTerminal(usuarioTerminal);
            polizaMan.setPolizaEncaBitTipoOper("REGISTRO");
            polizaMan.setPolizaEncaBitUsuario(usuarioNumero);
            geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
            geneFecha.setGenericoFecha00(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
            geneFecha.setGenericoFecha01(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")));
        }

        private void onInicializaPoliza() {
            polizaMan.setPolizaEncaCamposArr(new String[20]);
            polizaMan.setPolizaEncaCamposNum(0);
            polizaMan.setPoliza00Etiqueta(new String());
            polizaMan.setPoliza00Valor(new String());
            polizaMan.setPoliza00Visible("hidden;");
            polizaMan.setPoliza01Etiqueta(new String());
            polizaMan.setPoliza01Valor(new String());
            polizaMan.setPoliza01Visible("hidden;");
            polizaMan.setPoliza02Etiqueta(new String());
            polizaMan.setPoliza02Valor(new String());
            polizaMan.setPoliza02Visible("hidden;");
            polizaMan.setPoliza03Etiqueta(new String());
            polizaMan.setPoliza03Valor(new String());
            polizaMan.setPoliza03Visible("hidden;");
            polizaMan.setPoliza04Etiqueta(new String());
            polizaMan.setPoliza04Valor(new String());
            polizaMan.setPoliza04Visible("hidden;");
            polizaMan.setPoliza05Etiqueta(new String());
            polizaMan.setPoliza05Valor(new String());
            polizaMan.setPoliza05Visible("hidden;");
            polizaMan.setPoliza06Etiqueta(new String());
            polizaMan.setPoliza06Valor(new String());
            polizaMan.setPoliza06Visible("hidden;");
            polizaMan.setPoliza07Etiqueta(new String());
            polizaMan.setPoliza07Valor(new String());
            polizaMan.setPoliza07Visible("hidden;");
            polizaMan.setPoliza08Etiqueta(new String());
            polizaMan.setPoliza08Valor(new String());
            polizaMan.setPoliza08Visible("hidden;");
            polizaMan.setPoliza09Etiqueta(new String());
            polizaMan.setPoliza09Valor(new String());
            polizaMan.setPoliza09Visible("hidden;");
            polizaMan.setPoliza10Etiqueta(new String());
            polizaMan.setPoliza10Valor(new String());
            polizaMan.setPoliza10Visible("hidden;");
            polizaMan.setPoliza11Etiqueta(new String());
            polizaMan.setPoliza11Valor(new String());
            polizaMan.setPoliza11Visible("hidden;");
            polizaMan.setPoliza12Etiqueta(new String());
            polizaMan.setPoliza12Valor(new String());
            polizaMan.setPoliza12Visible("hidden;");
            polizaMan.setPoliza13Etiqueta(new String());
            polizaMan.setPoliza13Valor(new String());
            polizaMan.setPoliza13Visible("hidden;");
            polizaMan.setPoliza14Etiqueta(new String());
            polizaMan.setPoliza14Valor(new String());
            polizaMan.setPoliza14Visible("hidden;");
            polizaMan.setPoliza15Etiqueta(new String());
            polizaMan.setPoliza15Valor(new String());
            polizaMan.setPoliza15Visible("hidden;");
            polizaMan.setPoliza16Etiqueta(new String());
            polizaMan.setPoliza16Valor(new String());
            polizaMan.setPoliza16Visible("hidden;");
            polizaMan.setPoliza17Etiqueta(new String());
            polizaMan.setPoliza17Valor(new String());
            polizaMan.setPoliza17Visible("hidden;");
            polizaMan.setPoliza18Etiqueta(new String());
            polizaMan.setPoliza18Valor(new String());
            polizaMan.setPoliza18Visible("hidden;");
            polizaMan.setPoliza19Etiqueta(new String());
            polizaMan.setPoliza19Valor(new String());
            polizaMan.setPoliza19Visible("hidden;");
        }

        public void onPolizaMan_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioAX1":
                    if (!"".equals(cbc.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX1(), "Fideicomiso", "L")) {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } else {
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cbc.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX2(), "SubFiso", "L")) {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } else {
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioFolio":
                    if (!"".equals(cbc.getTxtCriterioFolio()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFolio(), "Folio", "L")) {
                        cbc.setCriterioFolio(Long.parseLong(cbc.getTxtCriterioFolio()));
                    } else {
                        cbc.setCriterioFolio(null);
                        cbc.setTxtCriterioFolio(null);
                    }
                    break;
                case "txtCriterioTransId":
                    if (!"".equals(cbc.getTxtCriterioTransId()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioTransId(), "Trans", "I")) {
                        cbc.setCriterioTransId(Integer.parseInt(cbc.getTxtCriterioTransId()));
                    } else {
                        cbc.setCriterioTransId(null);
                        cbc.setTxtCriterioTransId(null);
                    }
                    break;
                case "txtCriterioFechaDD":
                    if (!"".equals(cbc.getTxtCriterioFechaDD()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaDD(), "dd", "I")) {
                        cbc.setCriterioFechaDD(Integer.parseInt(cbc.getTxtCriterioFechaDD()));
                    } else {
                        cbc.setCriterioFechaDD(null);
                        cbc.setTxtCriterioFechaDD(null);
                    }
                    break;
                case "txtCriterioFechaMM":
                    if (!"".equals(cbc.getTxtCriterioFechaMM()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaMM(), "mm", "I")) {
                        cbc.setCriterioFechaMM(Integer.parseInt(cbc.getTxtCriterioFechaMM()));
                    } else {
                        cbc.setCriterioFechaMM(null);
                        cbc.setTxtCriterioFechaMM(null);
                    }
                    break;
                case "txtCriterioFechaYYYY":
                    if (!"".equals(cbc.getTxtCriterioFechaYYYY()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaYYYY(), "aaaa", "I")) {
                        cbc.setCriterioFechaYYYY(Integer.parseInt(cbc.getTxtCriterioFechaYYYY()));
                    } else {
                        cbc.setCriterioFechaYYYY(null);
                        cbc.setTxtCriterioFechaYYYY(null);
                    }
                    break;

                case "poliza01Valor":
                    if (!"".equals(polizaMan.getPoliza01Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza01Valor(), polizaMan.getPoliza01Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza01Valor(null);
                    }
                    break;
                case "poliza02Valor":
                    if (!"".equals(polizaMan.getPoliza02Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza02Valor(), polizaMan.getPoliza02Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza02Valor(null);
                    }
                    break;
                case "poliza03Valor":
                    if (!"".equals(polizaMan.getPoliza03Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza03Valor(), polizaMan.getPoliza03Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza03Valor(null);
                    }
                    break;
                case "poliza04Valor":
                    if (!"".equals(polizaMan.getPoliza04Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza04Valor(), polizaMan.getPoliza04Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza04Valor(null);
                    }
                    break;
                case "poliza05Valor":
                    if (!"".equals(polizaMan.getPoliza05Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza05Valor(), polizaMan.getPoliza05Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza05Valor(null);
                    }
                    break;
                case "poliza06Valor":
                    if (!"".equals(polizaMan.getPoliza06Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza06Valor(), polizaMan.getPoliza06Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza06Valor(null);
                    }
                    break;
                case "poliza07Valor":
                    if (!"".equals(polizaMan.getPoliza07Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza07Valor(), polizaMan.getPoliza07Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza07Valor(null);
                    }
                    break;
                case "poliza08Valor":
                    if (!"".equals(polizaMan.getPoliza08Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza08Valor(), polizaMan.getPoliza08Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza08Valor(null);
                    }
                    break;
                case "poliza09Valor":
                    if (!"".equals(polizaMan.getPoliza09Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza09Valor(), polizaMan.getPoliza09Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza09Valor(null);
                    }
                    break;
                case "poliza10Valor":
                    if (!"".equals(polizaMan.getPoliza10Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza10Valor(), polizaMan.getPoliza10Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza10Valor(null);
                    }
                    break;
                case "poliza11Valor":
                    if (!"".equals(polizaMan.getPoliza11Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza11Valor(), polizaMan.getPoliza11Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza11Valor(null);
                    }
                    break;
                case "poliza12Valor":
                    if (!"".equals(polizaMan.getPoliza12Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza12Valor(), polizaMan.getPoliza12Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza12Valor(null);
                    }
                    break;
                case "poliza13Valor":
                    if (!"".equals(polizaMan.getPoliza13Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza13Valor(), polizaMan.getPoliza13Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza13Valor(null);
                    }
                    break;
                case "poliza14Valor":
                    if (!"".equals(polizaMan.getPoliza14Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza14Valor(), polizaMan.getPoliza14Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza14Valor(null);
                    }
                    break;
                case "poliza15Valor":
                    if (!"".equals(polizaMan.getPoliza15Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza15Valor(), polizaMan.getPoliza15Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza15Valor(null);
                    }
                    break;
                case "poliza16Valor":
                    if (!"".equals(polizaMan.getPoliza16Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza16Valor(), polizaMan.getPoliza16Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza16Valor(null);
                    }
                    break;
                case "poliza17Valor":
                    if (!"".equals(polizaMan.getPoliza17Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza17Valor(), polizaMan.getPoliza17Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza17Valor(null);
                    }
                    break;
                case "poliza18Valor":
                    if (!"".equals(polizaMan.getPoliza18Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza18Valor(), polizaMan.getPoliza18Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza18Valor(null);
                    }
                    break;
                case "poliza19Valor":
                    if (!"".equals(polizaMan.getPoliza19Valor()) && oContabilidadGrales.onValidaNumerico(polizaMan.getPoliza19Valor(), polizaMan.getPoliza19Etiqueta(), "D")) {

                    } else {
                        polizaMan.setPoliza19Valor(null);
                    }
                    break;
            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

        private boolean onPoliza_isLong(String cadena) {
            try {
                Long.parseLong(cadena);
                return true;
            } catch (NumberFormatException Err) {

                return false;
            }
        }

        private boolean onPoliza_isNumeric(String cadena) {
            try {
                Integer.parseInt(cadena);
                return true;
            } catch (NumberFormatException Err) {

                return false;
            }
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
            if (oPersFid != null) {
                oPersFid = null;
            }
        }
    }

    class contabilidadConsMovto {

        public contabilidadConsMovto() {
            formatoDecimal = new DecimalFormat("###,###,###,###,###.00");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadConsMovto.";
            usuarioAppCtx = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        public Boolean onConsutaMovtos_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            mensaje = null;
            Boolean retorno = Boolean.TRUE;
            switch (Campo) {
                case "txtCriterioAX1":
                    if (!"".equals(cbc.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX1(), "Fideicomiso", "L")) {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioAX1())) {
                            retorno = false;
                        }
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cbc.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioAX2(), "SubFiso", "L")) {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioAX2())) {
                            retorno = false;
                        }
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioFolio":
                    if (!"".equals(cbc.getTxtCriterioFolio()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFolio(), "Folio", "L")) {
                        cbc.setCriterioFolio(Long.parseLong(cbc.getTxtCriterioFolio()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioFolio())) {
                            retorno = false;
                        }
                        cbc.setCriterioFolio(null);
                        cbc.setTxtCriterioFolio(null);
                    }
                    break;
                case "criterioModulo":
                    if (!"".equals(cbc.getCriterioModulo()) && oContabilidadGrales.onValidaNumerico(cbc.getCriterioModulo(), "Módulo", "I")) {
                        // string  solo se valida numerico
                    } else {
                        if (!"".equals(cbc.getCriterioModulo())) {
                            retorno = false;
                        }
                        cbc.setCriterioModulo(null);
                    }
                    break;
                case "txtCriterioTransId":
                    if (!"".equals(cbc.getTxtCriterioTransId()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioTransId(), "Trans", "I")) {
                        cbc.setCriterioTransId(Integer.parseInt(cbc.getTxtCriterioTransId()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioTransId())) {
                            retorno = false;
                        }
                        cbc.setCriterioTransId(null);
                        cbc.setTxtCriterioTransId(null);
                    }
                    break;
                case "txtCriterioFechaDD":
                    if (!"".equals(cbc.getTxtCriterioFechaDD()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaDD(), "dd", "I")) {
                        cbc.setCriterioFechaDD(Integer.parseInt(cbc.getTxtCriterioFechaDD()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioFechaDD())) {
                            retorno = false;
                        }
                        cbc.setCriterioFechaDD(null);
                        cbc.setTxtCriterioFechaDD(null);
                    }
                    break;
                case "txtCriterioFechaMM":
                    if (!"".equals(cbc.getTxtCriterioFechaMM()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaMM(), "mm", "I")) {
                        cbc.setCriterioFechaMM(Integer.parseInt(cbc.getTxtCriterioFechaMM()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioFechaMM())) {
                            retorno = false;
                        }
                        cbc.setCriterioFechaMM(null);
                        cbc.setTxtCriterioFechaMM(null);
                    }
                    break;
                case "txtCriterioFechaYYYY":
                    if (!"".equals(cbc.getTxtCriterioFechaYYYY()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioFechaYYYY(), "aaaa", "I")) {
                        cbc.setCriterioFechaYYYY(Integer.parseInt(cbc.getTxtCriterioFechaYYYY()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioFechaYYYY())) {
                            retorno = false;
                        }
                        cbc.setCriterioFechaYYYY(null);
                        cbc.setTxtCriterioFechaYYYY(null);
                    }
                    break;
                case "txtCriterioPlaza":
                    if (!"".equals(cbc.getTxtCriterioPlaza()) && oContabilidadGrales.onValidaNumerico(cbc.getTxtCriterioPlaza(), "Plaza", "I")) {
                        cbc.setCriterioPlaza(Integer.parseInt(cbc.getTxtCriterioPlaza()));
                    } else {
                        if (!"".equals(cbc.getTxtCriterioPlaza())) {
                            retorno = false;
                        }
                        cbc.setCriterioPlaza(null);
                        cbc.setTxtCriterioPlaza(null);
                    }
                    break;
            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
            return retorno;
        }

        public void onContabilidadConsMovto_Consulta() {
            try {
                Boolean validacionN = Boolean.TRUE;
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioAX1")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioAX2")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFolio")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("criterioModulo")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioTransId")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaDD")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaMM")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaYYYY")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioPlaza")) {
                    validacionN = Boolean.FALSE;
                }
                if (!validacionN) {
                    validacion = Boolean.FALSE;
                }
                mensaje = null;
                movements = null;
                consultaMovtos = null;
                seleccionaMovto = null;

                if (cbc.getCriterioFolio() == null) {
                    if (!cbc.getCriterioCveMovtoCancelado().equals("SI")) {
                        if (cbc.getCriterioPlaza() == null) {
                            if (cbc.getCriterioTransId() == null) {
                                if ((cbc.getCriterioAX1() == null) && cbc.getCriterioFechaYYYY() != null && cbc.getCriterioFechaMM() == null && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  mm  no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioAX1() == null) && cbc.getCriterioFechaYYYY() == null && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioAX1() != null) && (validacion.equals(Boolean.TRUE))) {
                                    if (onVerificaAtencionContrato(cbc.getCriterioAX1()).equals(Boolean.FALSE)) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                }
                                if (cbc.getCriterioTipoFecha() != null) {
                                    if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                        if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                    }
                                }
                            } else {
                                if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if (cbc.getCriterioTipoFecha() != null) {
                                    if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                        if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                    }
                                }

                            }
                        } else {
                            if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                validacion = Boolean.FALSE;
                            }
                            if (cbc.getCriterioTipoFecha() != null) {
                                if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                    if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                }
                            }

                        }
                    } else {
                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                            validacion = Boolean.FALSE;
                        }
                        if (cbc.getCriterioTipoFecha() != null) {
                            if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                            }
                        }
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    cbc.setCriterioUsuario(usuarioNumero);

                    DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmContabilidadCons:accordeon:dtMovtos");
                    dataTable.setFirst(0);
                    oContabilidad = new CContabilidad();
                    int totalRows = 0;
                    if (cbc.getCriterioTipoFecha().equals("FCB")) {
                        totalRows = oContabilidad.accountingPolicyTotalRowsFCB(cbc, "getMovements");
                    } else {
                        totalRows = oContabilidad.accountingPolicyTotalRows(cbc, "getMovements");
                    }
                    oContabilidad = null;

                    ContabilidadMovtoBean bean = new ContabilidadMovtoBean();
                    MBLazyDataTable lazyModel = null;
                    lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getMovements");
                    movements = null;
                    movements = lazyModel.getLazyDataModel(cbc);
                    movements.setRowCount(totalRows);
                    movements.setPageSize(0);

                    if (movements.getRowCount() == 0) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados");
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadConsMovto_Consulta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsMovto_Selecciona() {
            ContabilidadOperacionBean op = new ContabilidadOperacionBean();
            try {

                dv.setDetValContratoNumero(null);
                dv.setDetValContratoNumeroSub(null);
                dv.setDetValContratoNombre(null);
                dv.setDetValContratoNombreSub(null);
                dv.setDetValContratoInver(null);
                dv.setDetValOperacionId(null);
                dv.setDetValOperacionNombre(null);
                dv.setDetValTransaccionId(null);
                dv.setDetValTransaccionNombre(null);
                dv.setDetValFolio(null);
                dv.setDetValSec(null);
                dv.setDetValHoraReg(null);
                dv.setDetValIntermedSec(null);
                dv.setDetValIntermedNom(null);
                dv.setDetValPizarra(null);
                dv.setDetValSerie(null);
                dv.setDetValCupon(null);
                dv.setDetValTituloPrecio(null);
                dv.setDetValTituloNum(null);
                dv.setDetValTituloImp(null);
                dv.setDetValTasaDesc(null);
                dv.setDetValTasaRend(null);
                dv.setDetValImpUtilidad(null);
                dv.setDetValImpPerdida(null);
                dv.setDetValRepIntermedSec(null);
                dv.setDetValRepIntermedNom(null);
                dv.setDetValRepFolio(null);
                dv.setDetValRepInstrume(null);
                dv.setDetValRepPlazo(null);
                dv.setDetValRepTasa(null);
                dv.setDetValRepPremio(null);
                dv.setDetValRepInteres(null);
                dv.setDetValRepISR(null);
                dv.setDetValRepMdaSec(null);
                dv.setDetValRepMdaNom(null);
                dv.setDetValRepImporte(null);
                dv.setDetValRepImporteME(null);
                dv.setDetValRepImporteTC(null);
                dv.setDetValDereTipo(null);
                dv.setDetValDereFactor(null);
                dv.setDetValDerePizarra(null);
                dv.setDetValDereSerieAnt(null);
                dv.setDetValDereCupon(null);
                dv.setMuestraValorDetalle(null);

                oContrato = new CContrato();
                seleccionaMovto.setMovtoContratoNom(oContrato.onContrato_ObtenNombre(seleccionaMovto.getMovtoContratoNum()));
                oContrato = null;
                op.setOperacionId(seleccionaMovto.getMovtoOperacionId());
                oContabilidad = new CContabilidad();
                consultaAsiento = oContabilidad.onConsMovto_ConsultaDetalle(seleccionaMovto.getMovtoFolio(), seleccionaMovto.getMovtoTransaccId());
                oContabilidad.onParamOperacion_ObtenInfo(op);
                consultaDatoMov = oContabilidad.onDatoMov_ObtenInformacion(seleccionaMovto.getMovtoFolio());
                dv.setDetValFolio(seleccionaMovto.getMovtoFolio());
                dv.setDetValTransaccionId(seleccionaMovto.getMovtoTransaccId());
                oContabilidad.onDetVal_ObtenInformacion(dv);
                oContabilidad = null;

                //Si hay detValor hacemos la carga de la informacion
                if ((dv.getDetValContratoNumeroSub() != null) && (dv.getDetValContratoNumeroSub() > 0)) {
                    oContratoSub = new CContratoSub();
                    dv.setDetValContratoNombreSub(oContratoSub.onContratoSub_ObtenNombre(dv.getDetValContratoNumero(), dv.getDetValContratoNumeroSub()));
                    oContratoSub = null;
                }
                //Fin  
                geneTitulo.setGenericoTitulo00(null);
                geneCons.setGenericoCampo00(null);
                geneTitulo.setGenericoTitulo01(null);
                geneCons.setGenericoCampo01(null);
                geneTitulo.setGenericoTitulo02(null);
                geneCons.setGenericoCampo02(null);
                geneTitulo.setGenericoTitulo03(null);
                geneCons.setGenericoCampo03(null);
                geneTitulo.setGenericoTitulo04(null);
                geneCons.setGenericoCampo04(null);
                geneTitulo.setGenericoTitulo05(null);
                geneCons.setGenericoCampo05(null);
                geneTitulo.setGenericoTitulo06(null);
                geneCons.setGenericoCampo06(null);
                geneTitulo.setGenericoTitulo07(null);
                geneCons.setGenericoCampo07(null);
                geneTitulo.setGenericoTitulo08(null);
                geneCons.setGenericoCampo08(null);
                geneTitulo.setGenericoTitulo09(null);
                geneCons.setGenericoCampo09(null);
                geneTitulo.setGenericoTitulo10(null);
                geneCons.setGenericoCampo10(null);
                geneTitulo.setGenericoTitulo11(null);
                geneCons.setGenericoCampo11(null);
                geneTitulo.setGenericoTitulo12(null);
                geneCons.setGenericoCampo12(null);
                geneTitulo.setGenericoTitulo13(null);
                geneCons.setGenericoCampo13(null);
                geneTitulo.setGenericoTitulo14(null);
                geneCons.setGenericoCampo14(null);
                geneTitulo.setGenericoTitulo15(null);
                geneCons.setGenericoCampo15(null);
                geneTitulo.setGenericoTitulo16(null);
                geneCons.setGenericoCampo16(null);
                geneTitulo.setGenericoTitulo17(null);
                geneCons.setGenericoCampo17(null);
                geneTitulo.setGenericoTitulo18(null);
                geneCons.setGenericoCampo18(null);
                geneTitulo.setGenericoTitulo19(null);
                geneCons.setGenericoCampo19(null);
                geneTitulo.setGenericoTitulo20(null);
                geneCons.setGenericoCampo20(null);
                for (int itemDatoMov = 0; itemDatoMov <= consultaDatoMov.size() - 1; itemDatoMov++) {
                    if (itemDatoMov == 0) {
                        geneTitulo.setGenericoTitulo00(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo00(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 1) {
                        geneTitulo.setGenericoTitulo01(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo01(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 2) {
                        geneTitulo.setGenericoTitulo02(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo02(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 3) {
                        geneTitulo.setGenericoTitulo03(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo03(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 4) {
                        geneTitulo.setGenericoTitulo04(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo04(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 5) {
                        geneTitulo.setGenericoTitulo05(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo05(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 6) {
                        geneTitulo.setGenericoTitulo06(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo06(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 7) {
                        geneTitulo.setGenericoTitulo07(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo07(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 8) {
                        geneTitulo.setGenericoTitulo08(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo08(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 9) {
                        geneTitulo.setGenericoTitulo09(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo09(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 10) {
                        geneTitulo.setGenericoTitulo10(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo10(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 11) {
                        geneTitulo.setGenericoTitulo11(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo11(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 12) {
                        geneTitulo.setGenericoTitulo12(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo12(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 13) {
                        geneTitulo.setGenericoTitulo13(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo13(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 14) {
                        geneTitulo.setGenericoTitulo14(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo14(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 15) {
                        geneTitulo.setGenericoTitulo15(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo15(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 16) {
                        geneTitulo.setGenericoTitulo16(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo16(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 17) {
                        geneTitulo.setGenericoTitulo17(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo17(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 18) {
                        geneTitulo.setGenericoTitulo18(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo18(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 19) {
                        geneTitulo.setGenericoTitulo19(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo19(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                    if (itemDatoMov == 20) {
                        geneTitulo.setGenericoTitulo20(consultaDatoMov.get(itemDatoMov).getDatoMovConcepto());
                        geneCons.setGenericoCampo20(consultaDatoMov.get(itemDatoMov).getDatoMovValor());
                    }
                }
                seleccionaMovto.setMovtoOperacionNom(op.getOperacionNombre());
                RequestContext.getCurrentInstance().execute("dlgMovtoDet.show();");
            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadConsMovto_Selecciona()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsMovto_Limpia() {
            onLimpiaCampos();
        }

        public void onContabilidadConsMovto_Exporta() {
            int indice = 0;
            try {
                Boolean validacionN = Boolean.TRUE;
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioAX1")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioAX2")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFolio")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("criterioModulo")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioTransId")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaDD")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaMM")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioFechaYYYY")) {
                    validacionN = Boolean.FALSE;
                }
                if (!onConsutaMovtos_VerificaNumerico("txtCriterioPlaza")) {
                    validacionN = Boolean.FALSE;
                }
                if (!validacionN) {
                    validacion = Boolean.FALSE;
                }
//                mensaje = null;
//                movements = null;
//                consultaMovtos = null;
//                seleccionaMovto = null;

                if (cbc.getCriterioFolio() == null) {
                    if (!cbc.getCriterioCveMovtoCancelado().equals("SI")) {
                        if (cbc.getCriterioPlaza() == null) {
                            if (cbc.getCriterioTransId() == null) {
                                if ((cbc.getCriterioAX1() == null) && cbc.getCriterioFechaYYYY() != null && cbc.getCriterioFechaMM() == null && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo  mm  no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioAX1() == null) && cbc.getCriterioFechaYYYY() == null && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioAX1() != null) && (validacion.equals(Boolean.TRUE))) {
                                    if (onVerificaAtencionContrato(cbc.getCriterioAX1()).equals(Boolean.FALSE)) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                }
                                if (cbc.getCriterioTipoFecha() != null) {
                                    if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                        if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                    }
                                }
                            } else {
                                if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if (cbc.getCriterioTipoFecha() != null) {
                                    if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                        if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                            validacion = Boolean.FALSE;
                                        }
                                    }
                                }

                            }
                        } else {
                            if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                validacion = Boolean.FALSE;
                            }
                            if (cbc.getCriterioTipoFecha() != null) {
                                if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                    if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                    if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                        validacion = Boolean.FALSE;
                                    }
                                }
                            }

                        }
                    } else {
                        if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                            validacion = Boolean.FALSE;
                        }
                        if (cbc.getCriterioTipoFecha() != null) {
                            if (cbc.getCriterioTipoFecha().equals("FV") || cbc.getCriterioTipoFecha().equals("FCB")) {
                                if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioFechaDD() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                                if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío...");
                                    validacion = Boolean.FALSE;
                                }
                            }
                        }
                    }
                }
                if (validacion.equals(Boolean.TRUE)) {
                    if (movements != null) {
                        oContabilidad = new CContabilidad();
                        consultaMovtos = oContabilidad.onConsMovto_Consulta(cbc);
                        archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(usuarioNumero.toString()).concat("_").concat("consultaMovtos.txt");
                        archivo = new File(archivoNombre);
                        fos = new FileOutputStream(archivo);
                        archivoLinea = "Consulta de Movimientos \n\n";
                        buffer = archivoLinea.getBytes();
                        fos.write(buffer);
                        archivoLinea = "Fecha Cont\tFecha Captura\tFideicomiso\tSubFiso\tFolio\tTrans\tDescripción\tImporte\tUsuario\tStatus\tUsu Cancela\tOperación\n";
                        buffer = archivoLinea.getBytes();
                        fos.write(buffer);
                        for (int item = 0; item <= consultaMovtos.size() - 1; item++) {
                            indice = item;
                            archivoLinea = formatFecha(consultaMovtos.get(item).getMovtoFecha()).concat("\t")
                                    + formatFechaHora(consultaMovtos.get(item).getMovtoFechaReg()).concat("\t")
                                    + consultaMovtos.get(item).getMovtoContratoNum().toString().concat("\t")
                                    + consultaMovtos.get(item).getMovtoContratoNumSub().toString().concat("\t")
                                    + consultaMovtos.get(item).getMovtoFolio().toString().concat("\t")
                                    + consultaMovtos.get(item).getMovtoTransaccId().concat("\t");
                            if (consultaMovtos.get(item).getMovtoDesc() != null) {
                                archivoLinea = archivoLinea
                                        + consultaMovtos.get(item).getMovtoDesc().concat("\t");
                            } else {
                                archivoLinea = archivoLinea
                                        + " ".concat("\t");
                            }
                            archivoLinea = archivoLinea
                                    + "$".concat(formatDecimal2D(consultaMovtos.get(item).getMovtoImporte()).concat("\t"))
                                    + consultaMovtos.get(item).getMovtoUsuarioId().toString().concat("\t")
                                    + consultaMovtos.get(item).getMovtoStatus().concat("\t");
                            if (consultaMovtos.get(item).getMovtoCCUsuario() != null) {
                                archivoLinea = archivoLinea
                                        + consultaMovtos.get(item).getMovtoCCUsuario().toString().concat("\t");
                            } else {
                                archivoLinea = archivoLinea
                                        + " ".concat("\t");
                            }
                            archivoLinea = archivoLinea
                                    + consultaMovtos.get(item).getMovtoOperacionId().concat("\n");

                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                        }
                        fos.close();
                        FacesContext.getCurrentInstance().getExternalContext().redirect(usuarioAppCtx.concat("/SArchivoDescarga?".concat(archivo.getName())));
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No es posible exportar una consulta sin registros");
                    }
                }
            } catch (IOException | NullPointerException Err) {
                logger.error(Err.getMessage() + "onContabilidadConsMovto_Exporta()" + indice);
            } finally {
                onFinalizaObjetos();
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
        //Funciones privadas

//        private Boolean onVerificaAtencionContrato(long contratoNumero) {
//            Boolean validacion = Boolean.FALSE;
//            for (int itemFiso = 0; itemFiso <= usuarioFiltro.size() - 1; itemFiso++) {
//                if (Long.parseLong(usuarioFiltro.get(itemFiso)) == contratoNumero) {
//                    validacion = Boolean.TRUE;
//                    break;
//                }
//            }
//            return validacion;
//        }
        private void onLimpiaCampos() {
            movements = null;
            consultaMovtos = null;
            seleccionaMovto = null;
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioFolio(null);
            cbc.setCriterioTransId(null);
            cbc.setCriterioTipoFecha("Fecha Natural");
            cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
            cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
            cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
            cbc.setCriterioPlaza(null);
            cbc.setCriterioCveMovtoCancelado("NO");
            cbc.setCriterioModulo(null);
            cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
            cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
            cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());

            cbc.setTxtCriterioCTAM(null);
            cbc.setTxtCriterioSC1(null);
            cbc.setTxtCriterioSC2(null);
            cbc.setTxtCriterioSC3(null);
            cbc.setTxtCriterioSC4(null);
            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioAX3(null);
            cbc.setTxtCriterioFolio(null);
            cbc.setTxtCriterioFolioIni(null);
            cbc.setTxtCriterioFolioFin(null);
            cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
            cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
            cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
            cbc.setTxtCriterioTransId(null);
            cbc.setTxtCriterioPlaza(null);
            cbc.setTxtCriterioUsuario(null);

        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oContratoSub != null) {
                oContratoSub = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }

    }

    class contabilidadConsAsie {

        public contabilidadConsAsie() {
            formatoDecimal = new DecimalFormat("###,###,###,###,###.00");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadConsAsie.";
            usuarioAppCtx = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadConsAsie_Consulta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                consultaAsiento = null;
                bookEntries = null;

                if ((!cba.getTxtCriterioCTAM().isEmpty())) {
                    try {
                        cba.setCriterioCTAM(Short.parseShort(cba.getTxtCriterioCTAM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La CTAM debe ser un campo numérico..."));
                        cba.setCriterioCTAM(null);
                        cba.setTxtCriterioCTAM(null);
                    }
                } else {
                    cba.setCriterioCTAM(null);
                }
                if ((!cba.getTxtCriterioSC1().isEmpty())) {
                    try {
                        cba.setCriterioSC1(Short.parseShort(cba.getTxtCriterioSC1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC1 debe ser un campo numérico..."));
                        cba.setCriterioSC1(null);
                        cba.setTxtCriterioSC1(null);
                    }
                } else {
                    cba.setCriterioSC1(null);
                }
                if ((!cba.getTxtCriterioSC2().isEmpty())) {
                    try {
                        cba.setCriterioSC2(Short.parseShort(cba.getTxtCriterioSC2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC2 debe ser un campo numérico..."));
                        cba.setCriterioSC2(null);
                        cba.setTxtCriterioSC2(null);
                    }
                } else {
                    cba.setCriterioSC2(null);
                }
                if ((!cba.getTxtCriterioSC3().isEmpty())) {
                    try {
                        cba.setCriterioSC3(Short.parseShort(cba.getTxtCriterioSC3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC3 debe ser un campo numérico..."));
                        cba.setCriterioSC3(null);
                        cba.setTxtCriterioSC3(null);
                    }
                } else {
                    cba.setCriterioSC3(null);
                }
                if ((!cba.getTxtCriterioSC4().isEmpty())) {
                    try {
                        cba.setCriterioSC4(Short.parseShort(cba.getTxtCriterioSC4()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC4 debe ser un campo numérico..."));
                        cba.setCriterioSC4(null);
                        cba.setTxtCriterioSC4(null);
                    }
                } else {
                    cba.setCriterioSC4(null);
                }
                if ((!cba.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cba.setCriterioAX1(Long.parseLong(cba.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El AX1 debe ser un campo numérico..."));
                        cba.setCriterioAX1(null);
                        cba.setTxtCriterioAX1(null);
                    }
                } else {
                    cba.setCriterioAX1(null);
//                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo AX1 no puede estar vacío..."));
                }
                if ((!cba.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cba.setCriterioAX2(Long.parseLong(cba.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  AX2 debe ser un campo numérico..."));
                        cba.setCriterioAX2(null);
                        cba.setTxtCriterioAX2(null);
                    }
                } else {
                    cba.setCriterioAX2(null);
                }
                if ((!cba.getTxtCriterioAX3().isEmpty())) {
                    try {
                        cba.setCriterioAX3(Long.parseLong(cba.getTxtCriterioAX3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  AX3 debe ser un campo numérico..."));
                        cba.setCriterioAX3(null);
                        cba.setTxtCriterioAX3(null);
                    }
                } else {
                    cba.setCriterioAX3(null);
                }
                if ((!cba.getTxtCriterioFolio().isEmpty())) {
                    try {
                        cba.setCriterioFolio(Long.parseLong(cba.getTxtCriterioFolio()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio debe ser un campo numérico..."));
                        cba.setCriterioFolio(null);
                        cba.setTxtCriterioFolio(null);
                    }
                } else {
                    cba.setCriterioFolio(null);
                }
                if ((!cba.getTxtCriterioPlaza().isEmpty())) {
                    try {
                        cba.setCriterioPlaza(Integer.parseInt(cba.getTxtCriterioPlaza()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico..."));
                        cba.setCriterioPlaza(null);
                        cba.setTxtCriterioPlaza(null);
                    }
                } else {
                    cba.setCriterioPlaza(null);
                }
                if ((!cba.getTxtCriterioFechaDD().isEmpty())) {
                    try {
                        cba.setCriterioFechaDD(Short.parseShort(cba.getTxtCriterioFechaDD()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  dd debe ser un campo numérico..."));
                        cba.setCriterioFechaDD(null);
                        cba.setTxtCriterioFechaDD(null);
                    }
                    if (cba.getTxtCriterioFechaMM().isEmpty()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo mm  no puede estar vacío, si requiere consultar un día ..."));
                    }
                } else {
                    cba.setCriterioFechaDD(null);
//                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd  no puede estar vacío..."));
                }
                if ((!cba.getTxtCriterioFechaMM().isEmpty())) {
                    try {
                        cba.setCriterioFechaMM(Short.parseShort(cba.getTxtCriterioFechaMM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  mm debe ser un campo numérico..."));
                        cba.setCriterioFechaMM(null);
                        cba.setTxtCriterioFechaMM(null);
                    }
                } else {
                    cba.setCriterioFechaMM(null);
                    if (null == cba.getCriterioAX1() && null == cba.getCriterioCTAM()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Ingrese al menos uno de los siguientes criterios CTAM, AX1 o mm"));
                    }
                }
                if ((!cba.getTxtCriterioFechaYY().isEmpty())) {
                    try {
                        cba.setCriterioFechaYY(Short.parseShort(cba.getTxtCriterioFechaYY()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa debe ser un campo numérico..."));
                        cba.setCriterioFechaYY(null);
                        cba.setTxtCriterioFechaYY(null);
                    }
                } else {
                    cba.setCriterioFechaYY(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío..."));
                }
                if (((cba.getCriterioFechaTipo() == null) || (cba.getCriterioFechaTipo().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Tipo no puede estar vacío..."));
                }
                if ((cba.getCriterioAX1() != null)) {
                    if (onVerificaAtencionContrato(cba.getCriterioAX1()).equals(Boolean.FALSE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                    }
                }

                if (mensajesDeError.isEmpty()) {
                    usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                    cba.setCriterioUsuario(usuarioNumero);
                    DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmContabilidadCons:accordeon:dtAsientos");
                    dataTable.setFirst(0);

                    oContabilidad = new CContabilidad();
                    int totalRows = oContabilidad.getBookEntriesTotalRows(cba);
                    oContabilidad = null;

                    ContabilidadAsientoBean bean = new ContabilidadAsientoBean();
                    MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getBookEntries");
                    bookEntries = lazyModel.getLazyDataModel(cba);
                    bookEntries.setRowCount(totalRows);

                    if (bookEntries.getRowCount() == 0) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados."));
                    } else {
                        totalAbonos = 0.00;
                        totalCargos = 0.00;
                        if (cba.getCriterioMuestraTotalCarAbo().equals(Boolean.TRUE)) {
                            geneVisible.setGenericoVisible00("visible;");
                            oContabilidad = new CContabilidad();
                            consultaAsiento = oContabilidad.onConsAsiento_Consulta(cba);
                            for (int itemAsi = 0; itemAsi <= totalRows - 1; itemAsi++) {
                                if (consultaAsiento.get(itemAsi).getAsientoCveCarAbo().equals("C")) {
                                    totalCargos = totalCargos + consultaAsiento.get(itemAsi).getAsientoImporte();
                                }
                                if (consultaAsiento.get(itemAsi).getAsientoCveCarAbo().equals("A")) {
                                    totalAbonos = totalAbonos + consultaAsiento.get(itemAsi).getAsientoImporte();
                                }
                            }
                            oContabilidad = null;
                            } else {
                            geneVisible.setGenericoVisible00("hidden;");
                        }
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadConsAsie_Consulta()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsAsie_Limpia() {
            try {
                consultaAsiento = null;
                bookEntries = null;
                cba.setCriterioCTAM(null);
                cba.setCriterioSC1(null);
                cba.setCriterioSC2(null);
                cba.setCriterioSC3(null);
                cba.setCriterioSC4(null);
                cba.setCriterioAX1(null);
                cba.setCriterioAX2(null);
                cba.setCriterioAX3(null);
                cba.setCriterioFolio(null);
                cba.setCriterioMonedaNom(null);
                cba.setCriterioMonedaNum(null);
                cba.setCriterioPlaza(null);
                cba.setCriterioFechaTipo("FN");
                cba.setCriterioFechaDD(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                cba.setCriterioFechaMM(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                cba.setCriterioFechaYY(Short.valueOf((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                cba.setCriterioStatus("ACTIVO");
                cba.setCriterioMuestraTotalCarAbo(Boolean.FALSE);
                geneVisible.setGenericoVisible00("hidden;");
                cba.setCriterioDescripcion(null);
                totalCargos = null;
                totalAbonos = null;
                cba.setTxtCriterioAX1(null);
                cba.setTxtCriterioAX2(null);
                cba.setTxtCriterioAX3(null);
                cba.setTxtCriterioCTAM(null);
                cba.setTxtCriterioSC1(null);
                cba.setTxtCriterioSC2(null);
                cba.setTxtCriterioSC3(null);
                cba.setTxtCriterioSC4(null);
                cba.setTxtCriterioFolio(null);
                cba.setTxtCriterioFechaDD(cba.getCriterioFechaDD().toString());
                cba.setTxtCriterioFechaMM(cba.getCriterioFechaMM().toString());
                cba.setTxtCriterioFechaYY(cba.getCriterioFechaYY().toString());
                cba.setTxtCriterioPlaza(null);
                cba.setTxtCriterioMonedaNum(null);
                cba.setTxtCriterioUsuario(null);

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadConsAsie_Limpia()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsAsie_Exporta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                if ((!cba.getTxtCriterioCTAM().isEmpty())) {
                    try {
                        cba.setCriterioCTAM(Short.parseShort(cba.getTxtCriterioCTAM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La CTAM debe ser un campo numérico..."));
                        cba.setCriterioCTAM(null);
                        cba.setTxtCriterioCTAM(null);
                    }
                } else {
                    cba.setCriterioCTAM(null);
                }
                if ((!cba.getTxtCriterioSC1().isEmpty())) {
                    try {
                        cba.setCriterioSC1(Short.parseShort(cba.getTxtCriterioSC1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC1 debe ser un campo numérico..."));
                        cba.setCriterioSC1(null);
                        cba.setTxtCriterioSC1(null);
                    }
                } else {
                    cba.setCriterioSC1(null);
                }
                if ((!cba.getTxtCriterioSC2().isEmpty())) {
                    try {
                        cba.setCriterioSC2(Short.parseShort(cba.getTxtCriterioSC2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC2 debe ser un campo numérico..."));
                        cba.setCriterioSC2(null);
                        cba.setTxtCriterioSC2(null);
                    }
                } else {
                    cba.setCriterioSC2(null);
                }
                if ((!cba.getTxtCriterioSC3().isEmpty())) {
                    try {
                        cba.setCriterioSC3(Short.parseShort(cba.getTxtCriterioSC3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC3 debe ser un campo numérico..."));
                        cba.setCriterioSC3(null);
                        cba.setTxtCriterioSC3(null);
                    }
                } else {
                    cba.setCriterioSC3(null);
                }
                if ((!cba.getTxtCriterioSC4().isEmpty())) {
                    try {
                        cba.setCriterioSC4(Short.parseShort(cba.getTxtCriterioSC4()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La SC4 debe ser un campo numérico..."));
                        cba.setCriterioSC4(null);
                        cba.setTxtCriterioSC4(null);
                    }
                } else {
                    cba.setCriterioSC4(null);
                }
                if ((!cba.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cba.setCriterioAX1(Long.parseLong(cba.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El AX1 debe ser un campo numérico..."));
                        cba.setCriterioAX1(null);
                        cba.setTxtCriterioAX1(null);
                    }
                } else {
                    cba.setCriterioAX1(null);
//                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo AX1 no puede estar vacío..."));
                }
                if ((!cba.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cba.setCriterioAX2(Long.parseLong(cba.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  AX2 debe ser un campo numérico..."));
                        cba.setCriterioAX2(null);
                        cba.setTxtCriterioAX2(null);
                    }
                } else {
                    cba.setCriterioAX2(null);
                }
                if ((!cba.getTxtCriterioAX3().isEmpty())) {
                    try {
                        cba.setCriterioAX3(Long.parseLong(cba.getTxtCriterioAX3()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  AX3 debe ser un campo numérico..."));
                        cba.setCriterioAX3(null);
                        cba.setTxtCriterioAX3(null);
                    }
                } else {
                    cba.setCriterioAX3(null);
                }
                if ((!cba.getTxtCriterioFolio().isEmpty())) {
                    try {
                        cba.setCriterioFolio(Long.parseLong(cba.getTxtCriterioFolio()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio debe ser un campo numérico..."));
                        cba.setCriterioFolio(null);
                        cba.setTxtCriterioFolio(null);
                    }
                } else {
                    cba.setCriterioFolio(null);
                }
                if ((!cba.getTxtCriterioPlaza().isEmpty())) {
                    try {
                        cba.setCriterioPlaza(Integer.parseInt(cba.getTxtCriterioPlaza()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Plaza debe ser un campo numérico..."));
                        cba.setCriterioPlaza(null);
                        cba.setTxtCriterioPlaza(null);
                    }
                } else {
                    cba.setCriterioPlaza(null);
                }
                if ((!cba.getTxtCriterioFechaDD().isEmpty())) {
                    try {
                        cba.setCriterioFechaDD(Short.parseShort(cba.getTxtCriterioFechaDD()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  dd debe ser un campo numérico..."));
                        cba.setCriterioFechaDD(null);
                        cba.setTxtCriterioFechaDD(null);
                    }
                } else {
                    cba.setCriterioFechaDD(null);
//                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo dd  no puede estar vacío..."));
                }
                if ((!cba.getTxtCriterioFechaMM().isEmpty())) {
                    try {
                        cba.setCriterioFechaMM(Short.parseShort(cba.getTxtCriterioFechaMM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El  mm debe ser un campo numérico..."));
                        cba.setCriterioFechaMM(null);
                        cba.setTxtCriterioFechaMM(null);
                    }
                } else {
                    cba.setCriterioFechaMM(null);
                    if (null == cba.getCriterioAX1() && null == cba.getCriterioCTAM()) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Ingrese al menons uno de los siguientes criterios CTAM, AX1 o mm"));
                    }
                }
                if ((!cba.getTxtCriterioFechaYY().isEmpty())) {
                    try {
                        cba.setCriterioFechaYY(Short.parseShort(cba.getTxtCriterioFechaYY()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa debe ser un campo numérico..."));
                        cba.setCriterioFechaYY(null);
                        cba.setTxtCriterioFechaYY(null);
                    }
                } else {
                    cba.setCriterioFechaYY(null);
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo aaaa no puede estar vacío..."));
                }
                if (((cba.getCriterioFechaTipo() == null) || (cba.getCriterioFechaTipo().equals(new String())))) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Tipo no puede estar vacío..."));
                }
                if ((cba.getCriterioAX1() != null)) {
                    if (onVerificaAtencionContrato(cba.getCriterioAX1()).equals(Boolean.FALSE)) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                    }
                }

                if (mensajesDeError.isEmpty()) {
                    if (bookEntries != null) {
                        oContabilidad = new CContabilidad();
                        consultaAsiento = oContabilidad.onConsAsiento_Consulta(cba);
                        archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(usuarioNumero.toString()).concat("_").concat("consultaAsientos.txt");
                        archivo = new File(archivoNombre);
                        fos = new FileOutputStream(archivo);
                        archivoLinea = "Asientos\n";
                        buffer = archivoLinea.getBytes();
                        fos.write(buffer);
                        archivoLinea = new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()).toString().concat("\n\n");
                        buffer = archivoLinea.getBytes();
                        fos.write(buffer);
                        archivoLinea = "Folio\tSec\tOperacion\tTrans\tFecha Cont.\tFecha Captura\tCTAM\tS1\tS2\tS3\tS4\tMda\tAX1\tAX2\tAX3\tC/A\tImporte\tDescripcion\tStatus\tCan\n";
                        buffer = archivoLinea.getBytes();
                        fos.write(buffer);
                        for (int item = 0; item <= consultaAsiento.size() - 1; item++) {
                            archivoLinea = consultaAsiento.get(item).getAsientoFolio().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoSecuencial().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoOperacion().concat("\t")
                                    + consultaAsiento.get(item).getAsientoTransaccion().concat("\t")
                                    + formatFecha(consultaAsiento.get(item).getAsientoFecMov()).concat("\t")
                                    + formatFechaHora(consultaAsiento.get(item).getAsientoFecReg()).concat("\t")
                                    + consultaAsiento.get(item).getAsientoCTAM().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsiento1SCTA().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsiento2SCTA().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsiento3SCTA().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsiento4SCTA().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoMdaNomCrt().concat("\t")
                                    + consultaAsiento.get(item).getAsientoAX1().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoAX2().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoAX3().toString().concat("\t")
                                    + consultaAsiento.get(item).getAsientoCveCarAbo().concat("\t")
                                    + "$".concat(formatDecimal2D(consultaAsiento.get(item).getAsientoImporte()).concat("\t"))
                                    + consultaAsiento.get(item).getAsientoDescripcion().concat("\t")
                                    + consultaAsiento.get(item).getAsientoStatus().concat("\t")
                                    + consultaAsiento.get(item).getAsientoUsuCancelo().toString().concat("\n");
                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                        }
                        fos.close();
                        oContabilidad = null;
                        FacesContext.getCurrentInstance().getExternalContext().redirect(usuarioAppCtx.concat("/SArchivoDescarga?".concat(archivo.getName())));
                    } else {
                        mensajesDeError.add(mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No es posible exportar una consulta sin registros"));
                    }
                }
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onContabilidadConsAsie_Exporta()");
            } finally {
                onFinalizaObjetos();
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }

        public void onConsutaAsientos_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioCTAM":
                    if (!"".equals(cba.getTxtCriterioCTAM()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioCTAM(), "CTAM", "S")) {
                        cba.setCriterioCTAM(Short.parseShort(cba.getTxtCriterioCTAM()));
                    } else {
                        cba.setCriterioCTAM(null);
                        cba.setTxtCriterioCTAM(null);
                    }
                    break;
                case "txtCriterioSC1":
                    if (!"".equals(cba.getTxtCriterioSC1()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioSC1(), "SC1", "S")) {
                        cba.setCriterioSC1(Short.parseShort(cba.getTxtCriterioSC1()));
                    } else {
                        cba.setCriterioSC1(null);
                        cba.setTxtCriterioSC1(null);
                    }
                    break;
                case "txtCriterioSC2":
                    if (!"".equals(cba.getTxtCriterioSC2()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioSC2(), "SC2", "S")) {
                        cba.setCriterioSC2(Short.parseShort(cba.getTxtCriterioSC2()));
                    } else {
                        cba.setCriterioSC2(null);
                        cba.setTxtCriterioSC2(null);
                    }
                    break;
                case "txtCriterioSC3":
                    if (!"".equals(cba.getTxtCriterioSC3()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioSC3(), "SC3", "S")) {
                        cba.setCriterioSC3(Short.parseShort(cba.getTxtCriterioSC3()));
                    } else {
                        cba.setCriterioSC3(null);
                        cba.setTxtCriterioSC3(null);
                    }
                    break;
                case "txtCriterioSC4":
                    if (!"".equals(cba.getTxtCriterioSC4()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioSC4(), "SC4", "S")) {
                        cba.setCriterioSC4(Short.parseShort(cba.getTxtCriterioSC4()));
                    } else {
                        cba.setCriterioSC4(null);
                        cba.setTxtCriterioSC4(null);
                    }
                    break;
                case "txtCriterioAX1":
                    if (!"".equals(cba.getTxtCriterioAX1()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioAX1(), "AX1", "L")) {
                        cba.setCriterioAX1(Long.parseLong(cba.getTxtCriterioAX1()));
                    } else {
                        cba.setCriterioAX1(null);
                        cba.setTxtCriterioAX1(null);
                    }
                    break;
                case "txtCriterioAX2":
                    if (!"".equals(cba.getTxtCriterioAX2()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioAX2(), "AX2", "L")) {
                        cba.setCriterioAX2(Long.parseLong(cba.getTxtCriterioAX2()));
                    } else {
                        cba.setCriterioAX2(null);
                        cba.setTxtCriterioAX2(null);
                    }
                    break;
                case "txtCriterioAX3":
                    if (!"".equals(cba.getTxtCriterioAX3()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioAX3(), "AX3", "L")) {
                        cba.setCriterioAX3(Long.parseLong(cba.getTxtCriterioAX3()));
                    } else {
                        cba.setCriterioAX3(null);
                        cba.setTxtCriterioAX3(null);
                    }
                    break;
                case "txtCriterioFolio":
                    if (!"".equals(cba.getTxtCriterioFolio()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioFolio(), "Folio", "L")) {
                        cba.setCriterioFolio(Long.parseLong(cba.getTxtCriterioFolio()));
                    } else {
                        cba.setCriterioFolio(null);
                        cba.setTxtCriterioFolio(null);
                    }
                    break;
                case "txtCriterioPlaza":
                    if (!"".equals(cba.getTxtCriterioPlaza()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioPlaza(), "Plaza", "S")) {
                        cba.setCriterioPlaza(Integer.parseInt(cba.getTxtCriterioPlaza()));
                    } else {
                        cba.setCriterioPlaza(null);
                        cba.setTxtCriterioPlaza(null);
                    }
                    break;
                case "txtCriterioFechaDD":
                    if (!"".equals(cba.getTxtCriterioFechaDD()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioFechaDD(), "dd", "S")) {
                        cba.setCriterioFechaDD(Short.parseShort(cba.getTxtCriterioFechaDD()));
                    } else {
                        cba.setCriterioFechaDD(null);
                        cba.setTxtCriterioFechaDD(null);
                    }
                    break;
                case "txtCriterioFechaMM":
                    if (!"".equals(cba.getTxtCriterioFechaMM()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioFechaMM(), "mm", "S")) {
                        cba.setCriterioFechaMM(Short.parseShort(cba.getTxtCriterioFechaMM()));
                    } else {
                        cba.setCriterioFechaMM(null);
                        cba.setTxtCriterioFechaMM(null);
                    }
                    break;
                case "txtCriterioFechaYY":
                    if (!"".equals(cba.getTxtCriterioFechaYY()) && oContabilidadGrales.onValidaNumerico(cba.getTxtCriterioFechaYY(), "aaaa", "S")) {
                        cba.setCriterioFechaYY(Short.parseShort(cba.getTxtCriterioFechaYY()));
                    } else {
                        cba.setCriterioFechaYY(null);
                        cba.setTxtCriterioFechaYY(null);
                    }
                    break;
            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }

    }

    class contabilidadConsSaldoProm {

        public contabilidadConsSaldoProm() {
            formatoDecimal = new DecimalFormat("###,###,###,###,###.00");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadConsSaldoProm.";
            usuarioAppCtx = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appContexto");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadConsSldPrm_Consulta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {
                consultaSaldoProm = null;
                setAcumActivo(null);
                setAcumInv(null);
                setAcumPatrim(null);
                setAcumTotal(null);
                setNumDias(null);

                averageBalanceInquiries = null;
                if ((!cb.getTxtCriterioContratoNumero().isEmpty())) {
                    try {
                        cb.setCriterioContratoNumero(Long.parseLong(cb.getTxtCriterioContratoNumero()));
                        if (onVerificaAtencionContrato(cb.getCriterioContratoNumero()).equals(Boolean.FALSE)) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cb.setCriterioContratoNumero(null);
                        cb.setTxtCriterioContratoNumero(null);
                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                    cb.setCriterioContratoNumero(null);
                }
                if ((!cb.getTxtCriterioContratoNumeroSub().isEmpty())) {
                    try {
                        cb.setCriterioContratoNumeroSub(Integer.parseInt(cb.getTxtCriterioContratoNumeroSub()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico..."));
                        cb.setCriterioContratoNumeroSub(null);
                        cb.setTxtCriterioContratoNumeroSub(null);
                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
                    cb.setTxtCriterioContratoNumeroSub(null);
                    cb.setCriterioContratoNumeroSub(null);
                }
                if ((geneFecha.getGenericoFecha00() == null)) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Inicial no puede estar vacío..."));
                }
                if ((geneFecha.getGenericoFecha01() == null)) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Final no puede estar vacío..."));
                }
                if (mensajesDeError.isEmpty()) {
                    if (geneFecha.getGenericoFecha01().before(geneFecha.getGenericoFecha00())) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Final no puede ser anterior a la  Fecha Inicial"));
                        validacion = Boolean.FALSE;
                    }
                    if (mensajesDeError.isEmpty()) {
                        cb.setCriterioFechaDel(new java.sql.Date(geneFecha.getGenericoFecha00().getTime()));
                        cb.setCriterioFechaAl(new java.sql.Date(geneFecha.getGenericoFecha01().getTime()));
                        usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                        cb.setCriterioUsuario(usuarioNumero);

                        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmContabilidadCons:accordeon:dtSaldosProm");
                        dataTable.setFirst(0);

                        oContabilidad = new CContabilidad();
                        int totalRows = oContabilidad.getAverageBalanceInquiriesTotalRows(cb);
                        oContabilidad = null;

                        ContabilidadSaldoPromBean bean = new ContabilidadSaldoPromBean();
                        MBLazyDataTable lazyModel = new MBLazyDataTable(bean.getClass().getName(), "getAverageBalanceInquiries");
                        averageBalanceInquiries = lazyModel.getLazyDataModel(cb);
                        averageBalanceInquiries.setRowCount(totalRows);

                        if (averageBalanceInquiries.getRowCount() == 0) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados."));
                        } else {
                            long startTime = geneFecha.getGenericoFecha00().getTime();
                            long endTime = geneFecha.getGenericoFecha01().getTime();
                            long diasDesde = (long) Math.floor(startTime / (1000 * 60 * 60 * 24)); // convertimos a dias, para que no afecten cambios de hora 
                            long diasHasta = (long) Math.floor(endTime / (1000 * 60 * 60 * 24)); // convertimos a dias, para que no afecten cambios de hora
                            numDias = (diasHasta - diasDesde) + 1;
                            oContabilidad = new CContabilidad();
                            consultaSaldoProm = oContabilidad.onConsSaldoProm_Consulta(cb);
                            oContabilidad = null;
                            setAcumActivo(0.00);
                            setAcumPatrim(0.00);
                            setAcumInv(0.00);
                            setAcumTotal(0.00);

                            for (short item = 0; item <= consultaSaldoProm.size() - 1; item++) {
                                setAcumActivo((Math.abs(getAcumActivo() + consultaSaldoProm.get(item).getSaldoPromActivo())));
                                setAcumPatrim((Math.abs(getAcumPatrim() + consultaSaldoProm.get(item).getSaldoPromPatrim())));
                                setAcumInv((Math.abs(getAcumInv() + consultaSaldoProm.get(item).getSaldoPromInv())));
                                setAcumTotal((getAcumTotal() + consultaSaldoProm.get(item).getSaldoPromTotal()));
                            }
                            setAcumActivo(Math.abs((getAcumActivo() / numDias)));
                            setAcumPatrim(Math.abs((getAcumPatrim() / numDias)));
                            setAcumInv(Math.abs((getAcumInv() / numDias)));
//                            setAcumTotal((getAcumTotal() / numDias));
                            setAcumTotal(Math.abs(getAcumActivo() + getAcumPatrim() + getAcumInv()));
                        }
                    }
                }
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadConsSldPrm_Consulta()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadConsSldPrm_Limpia() {
            consultaSaldoProm = null;
            averageBalanceInquiries = null;
            cb.setCriterioContratoNumero(null);
            cb.setCriterioContratoNumeroSub(null);
            geneFecha.setGenericoFecha00(null);
            geneFecha.setGenericoFecha01(null);
            cb.setTxtCriterioContratoNumero(null);
            cb.setTxtCriterioContratoNumeroSub(null);
            setAcumActivo(null);
            setAcumInv(null);
            setAcumPatrim(null);
            setAcumTotal(null);
            setNumDias(null);
        }

        public void onContabilidadConsSldPrm_Exporta() {
            List<FacesMessage> mensajesDeError = new ArrayList<>();
            try {

                if ((!cb.getTxtCriterioContratoNumero().isEmpty())) {
                    try {
                        cb.setCriterioContratoNumero(Long.parseLong(cb.getTxtCriterioContratoNumero()));
                        if (onVerificaAtencionContrato(cb.getCriterioContratoNumero()).equals(Boolean.FALSE)) {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                        }
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                        cb.setCriterioContratoNumero(null);
                        cb.setTxtCriterioContratoNumero(null);
                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío..."));
                    cb.setCriterioContratoNumero(null);
                }
                if ((!cb.getTxtCriterioContratoNumeroSub().isEmpty())) {
                    try {
                        cb.setCriterioContratoNumeroSub(Integer.parseInt(cb.getTxtCriterioContratoNumeroSub()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico..."));
                        cb.setCriterioContratoNumeroSub(null);
                        cb.setTxtCriterioContratoNumeroSub(null);
                    }
                } else {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo SubFiso no puede estar vacío..."));
                    cb.setTxtCriterioContratoNumeroSub(null);
                    cb.setCriterioContratoNumeroSub(null);
                }
                if ((geneFecha.getGenericoFecha00() == null)) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Inicial no puede estar vacío..."));
                }
                if ((geneFecha.getGenericoFecha01() == null)) {
                    mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Final no puede estar vacío..."));
                }
                if (mensajesDeError.isEmpty()) {
                    if (geneFecha.getGenericoFecha01().before(geneFecha.getGenericoFecha00())) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fecha Final no puede ser anterior a la  Fecha Inicial"));
                        validacion = Boolean.FALSE;
                    }
                    if (mensajesDeError.isEmpty()) {
                        if (averageBalanceInquiries != null) {
                            oContabilidad = new CContabilidad();
                            consultaSaldoProm = oContabilidad.onConsSaldoProm_Consulta(cb);
                            archivoNombre = Constantes.RUTA_TEMP.concat("/").concat(usuarioNumero.toString()).concat("_").concat("consultaSaldoProm.txt");
                            archivo = new File(archivoNombre);
                            fos = new FileOutputStream(archivo);
                            archivoLinea = "Consulta de Saldos Promedio \n\n";
                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                            archivoLinea = "Fideicomiso\tSubFiso\tFecha\tActivo Fijo\tPatrimonio\tInversion\tTotal\n";
                            buffer = archivoLinea.getBytes();
                            fos.write(buffer);
                            for (int item = 0; item <= consultaSaldoProm.size() - 1; item++) {
                                if ((consultaSaldoProm.get(item).getSaldoPromContratoNom() != null) && (consultaSaldoProm.get(item).getSaldoPromContratoNumSub() != null)) {
                                    archivoLinea = consultaSaldoProm.get(item).getSaldoPromContratoNom() + "\t"
                                            + consultaSaldoProm.get(item).getSaldoPromContratoNumSub() + "\t"
                                            + formatFecha(consultaSaldoProm.get(item).getSaldoPromFecha()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromActivo()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromPatrim()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromInv()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromTotal()) + "\n";
                                }
                                if ((consultaSaldoProm.get(item).getSaldoPromContratoNom() != null) && (consultaSaldoProm.get(item).getSaldoPromContratoNumSub() == null)) {
                                    archivoLinea = consultaSaldoProm.get(item).getSaldoPromContratoNom() + "\t\t\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromActivo()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromPatrim()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromInv()) + "\t"
                                            + formatDecimal2D(consultaSaldoProm.get(item).getSaldoPromTotal()) + "\n";
                                }
                                buffer = archivoLinea.getBytes();
                                fos.write(buffer);
                            }
                            consultaSaldoProm = null;
                            fos.close();
                            FacesContext.getCurrentInstance().getExternalContext().redirect(usuarioAppCtx.concat("/SArchivoDescarga?".concat(archivo.getName())));
                        } else {
                            mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No es posible exportar una consulta sin registros"));
                        }
                    }
                }
            } catch (IOException Err) {
                logger.error(Err.getMessage() + "onContabilidadConsSldPrm_Exporta()");
            } finally {
                for (FacesMessage mensaje : mensajesDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        //Funciones privadas

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }

        public void onConsutaSaldosPrm_VerificaNumerico(String Campo) {
            oContabilidadGrales = new contabilidadGrales();
            switch (Campo) {
                case "txtCriterioContratoNumero":
                    if (!"".equals(cb.getTxtCriterioContratoNumero()) && oContabilidadGrales.onValidaNumerico(cb.getTxtCriterioContratoNumero(), "Fideicomiso", "L")) {
                        cb.setCriterioContratoNumero(Long.parseLong(cb.getTxtCriterioContratoNumero()));
                    } else {
                        cb.setCriterioContratoNumero(null);
                        cb.setTxtCriterioContratoNumero(null);
                    }
                    break;
                case "txtCriterioContratoNumeroSub":
                    if (!"".equals(cb.getTxtCriterioContratoNumeroSub()) && oContabilidadGrales.onValidaNumerico(cb.getTxtCriterioContratoNumeroSub(), "Sub Fiso", "I")) {
                        cb.setCriterioContratoNumeroSub(Integer.parseInt(cb.getTxtCriterioContratoNumeroSub()));
                    } else {
                        cb.setCriterioContratoNumeroSub(null);
                        cb.setTxtCriterioContratoNumeroSub(null);
                    }
                    break;
            }
            oContabilidadGrales = null;
            if (mensaje != null) {
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
            }
        }
    }

    class contabilidadCancelaOperacion {

        public void onContabilidadCancela_Cancela() {
            RequestContext.getCurrentInstance().execute("dlgPopUpGral.hide();");
        }
        
        public contabilidadCancelaOperacion() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadCancelaOperacion.";
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            validacion = Boolean.TRUE; 
        } 
         
        public void onContabilidadCancelaOper_Consulta() {
            try {
                cbc.setCriterioAX1(null);
                cbc.setCriterioAX2(null);
                cbc.setCriterioTransId(null);
                cbc.setCriterioFolioIni(null);
                cbc.setCriterioFolioFin(null);
                cbc.setCriterioFechaDD(null);
                cbc.setCriterioFechaMM(null);
                cbc.setCriterioFechaYYYY(null);
                List<FacesMessage> mensajesDeError = new ArrayList<>();
                
            	// Check de Meses anteriores (checkCancelMeses), analizar funcionamiento en VB
                if (cancelaMesesAnt == true) {

                    cbc.setTxtCriterioAX1(null);
                    cbc.setTxtCriterioAX2(null);
                    cbc.setTxtCriterioFolioFin(null);
                    cbc.setTxtCriterioTransId(null);
                    cbc.setCriterioCveMovtoCancelado(null);
                    cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                    cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                    cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));            
                    cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());                   
                    cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                    cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());    
                    
                   if (cbc.getTxtCriterioFolioIni().isEmpty()) {
                       validacion = Boolean.FALSE;
                       mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Es necesario proporcionar el folio en el campo Folio Ini...");
                   }
                   
                   if ((!cbc.getTxtCriterioFolioIni().isEmpty())) {
                       try {
                           cbc.setCriterioFolioIni(Long.parseLong(cbc.getTxtCriterioFolioIni()));
                       } catch (NumberFormatException e) {
                    	   mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio Ini. debe ser un campo numérico…"); 
                           cbc.setCriterioFolioIni(null);
                           cbc.setTxtCriterioFolioIni(null);
                       }
                   }

                   if(mensaje == null){
                       oContabilidad = new CContabilidad();
                       consultaMovtos = oContabilidad.onContabilidad_CancelaOper_MesesAnt(cbc);

                       if (!consultaMovtos.isEmpty() || oContabilidad.getMensajeError() == null) {
                           if (consultaMovtos.isEmpty()) {
                               mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con el folio de búsqueda especificados...");
                           }
                           return;
                       } else {
                           validacion = Boolean.FALSE;
                           mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError().toUpperCase().replaceAll("ERROR", ""));
                           onContabilidadCancelaOper_Limpia(); //Cancela_Meses_anteriores
                       }
                   }
                   oContabilidad = null;
               }else {
               //CONSULTA NORMAL
                if ((!cbc.getTxtCriterioAX1().isEmpty())) {
                    try {
                        cbc.setCriterioAX1(Long.parseLong(cbc.getTxtCriterioAX1()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico…"));
                        cbc.setCriterioAX1(null);
                        cbc.setTxtCriterioAX1(null);
                    }
                }
                if ((!cbc.getTxtCriterioAX2().isEmpty())) {
                    try {
                        cbc.setCriterioAX2(Long.parseLong(cbc.getTxtCriterioAX2()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Subfiso debe ser un campo numérico…"));
                        cbc.setCriterioAX2(null);
                        cbc.setTxtCriterioAX2(null);
                    }
                }
                if ((!cbc.getTxtCriterioFolioIni().isEmpty())) {
                    try {
                        cbc.setCriterioFolioIni(Long.parseLong(cbc.getTxtCriterioFolioIni()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio Ini. debe ser un campo numérico…"));
                        cbc.setCriterioFolioIni(null);
                        cbc.setTxtCriterioFolioIni(null);
                    }
                }
                if ((!cbc.getTxtCriterioFolioFin().isEmpty())) {
                    try {
                        cbc.setCriterioFolioFin(Long.parseLong(cbc.getTxtCriterioFolioFin()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio Fin debe ser un campo numérico…"));
                        cbc.setCriterioFolioFin(null);
                        cbc.setTxtCriterioFolioFin(null);
                    }
                }
                if ((!cbc.getTxtCriterioTransId().isEmpty())) {
                    try {
                        cbc.setCriterioTransId(Integer.parseInt(cbc.getTxtCriterioTransId()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Trans. debe ser un campo numérico…"));
                        cbc.setCriterioTransId(null);
                        cbc.setTxtCriterioTransId(null);
                    }
                }
                if ((!cbc.getTxtCriterioFechaDD().isEmpty())) {
                    try {
                        cbc.setCriterioFechaDD(Integer.parseInt(cbc.getTxtCriterioFechaDD()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El dd debe ser un campo numérico…"));
                        cbc.setCriterioFechaDD(null);
                        cbc.setTxtCriterioFechaDD(null);
                    }
                }
                if ((!cbc.getTxtCriterioFechaMM().isEmpty())) {
                    try {
                        cbc.setCriterioFechaMM(Integer.parseInt(cbc.getTxtCriterioFechaMM()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El mm debe ser un campo numérico…"));
                        cbc.setCriterioFechaMM(null);
                        cbc.setTxtCriterioFechaMM(null);
                    }
                }
                if ((!cbc.getTxtCriterioFechaYYYY().isEmpty())) {
                    try {
                        cbc.setCriterioFechaYYYY(Integer.parseInt(cbc.getTxtCriterioFechaYYYY()));
                    } catch (NumberFormatException e) {
                        mensajesDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa debe ser un campo numérico…"));
                        cbc.setCriterioFechaYYYY(null);
                        cbc.setTxtCriterioFechaYYYY(null);
                    }
                }
                
                if (mensajesDeError.isEmpty()) { 
                    seleccionaMovtoCancela = null;
                    consultaMovtos = null;
                                       
                    if ((cbc.getCriterioFechaMM() == null) && (validacion.equals(Boolean.TRUE))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El mm no puede estar vacío...");
                    }
                    if ((cbc.getCriterioFechaYYYY() == null) && (validacion.equals(Boolean.TRUE))) {
                        validacion = Boolean.FALSE;
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El aaaa no puede estar vacío...");
                    } 
                    
                    if ((cbc.getCriterioFolioIni() != null) && (cbc.getCriterioFolioFin() != null) && (validacion.equals(Boolean.TRUE))) {
                        if (cbc.getCriterioFolioIni() > cbc.getCriterioFolioFin()) {
                            validacion = Boolean.FALSE;
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Folio Inicial debe ser menor al Folio Final.");
                        }
                    }

                    //VJN Inicio 26/01/2024 - Activa o Desactiva el mes para cancelar operaciones de meses anteriores 
                    if (validacion.equals(Boolean.TRUE)) {
                     if (cbc.getCriterioFechaMM() != Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")) 
                     || cbc.getCriterioFechaYYYY() != Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño"))) { 
                        oContabilidad = new CContabilidad(); 
                        Integer iMesAbierto = oContabilidad.onContabilidad_ValidaMesAbiertoCerrado(cbc);
                        oContabilidad = null;
 
                    	if (iMesAbierto == 0){ 
                    	validacion = Boolean.FALSE; 
                    	consultaMovtos = null;
                        cbc.setTxtCriterioTransId(null);cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                        cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                        cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));            
                        cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                        cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
                        
                    	mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se pueden cancelar movimientos en esta fecha");
                    	}
                      }
                    } 
                    //VJN Fin 
                    
                    if (validacion.equals(Boolean.TRUE)) {
                        oContabilidad = new CContabilidad();
                        consultaMovtos = oContabilidad.onCancelaOper_ConsultaMovtos(cbc);
                        oContabilidad = null;
                        if (consultaMovtos.isEmpty()) {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe información con los criterios de búsqueda especificados...");
                        }
                    }
                } else {
                    for (FacesMessage mensaje : mensajesDeError) {
                        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    }
                }
              }
            } catch (RuntimeException Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaOper_Consulta()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaOper_Limpia() { 
            consultaMovtos = null;
            seleccionaMovto = null;
            cbc.setCriterioAX1(null);
            cbc.setCriterioAX2(null);
            cbc.setCriterioFolio(null);
            cbc.setCriterioFolioIni(null);
            cbc.setCriterioFolioFin(null);
            cbc.setCriterioTransId(null);
            cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
            cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
            cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
            listaGenerica = null;
            cbc.setTxtCriterioAX1(null);
            cbc.setTxtCriterioAX2(null);
            cbc.setTxtCriterioFolio(null);
            cbc.setTxtCriterioFolioIni(null);
            cbc.setTxtCriterioFolioFin(null);
            cbc.setTxtCriterioTransId(null);
            cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
            cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
            cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
            cbc.setCriterioCveMovtoCancelado(null);
            cancelaMesesAnt = false; 
            seleccionaMovtoCancela = null;
        }

        public void onContabilidadCancelaOper_Cancela() {
            try {
                if ((seleccionaMovtoCancela != null) && (seleccionaMovtoCancela.size() > 0)) {
                    onInicializaPolizaEnca();
                    onInicializaPoliza();
                    Boolean movCancelado = false;
                    mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                    for (int itemPol = 0; itemPol <= seleccionaMovtoCancela.size() - 1; itemPol++) {
                        if (seleccionaMovtoCancela.get(itemPol).getMovtoStatus().equals("CANCELADO")) {
                            movCancelado = true;
                        }
                    }

                    if (movCancelado) {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No se puede Cancelar un Folio previamente Cancelado");
                    } else {

                        if (seleccionaMovtoCancela.size() == 1) {
                            mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la cancelación del folio " + seleccionaMovtoCancela.get(0).getMovtoFolio().toString() + "  seleccionado?");
                        }
                        if (seleccionaMovtoCancela.size() > 1) {
                            mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la cancelación de los folios seleccionados?");
                        }
                        mensajeConfrima.setMensajeConfirmaOrigen("cancelaOperacion");
                        mensajeConfrima.setMensajeConfirmacionAccion("ELIMINAR");
                        RequestContext.getCurrentInstance().execute("dlgPopUpGral.show()");
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar como mínimo un registro");
                }
            } catch (Exception Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaOper_Cancela()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }
        
        private void onInicializaPolizaEnca() throws Exception {
            polizaMan.setPolizaEncaNombre(null);
            polizaMan.setPolizaEncaNumero(new String());
            polizaMan.setPolizaEncaFolio(Long.parseLong("0"));
            polizaMan.setPolizaEncaHoraAplica(null);
            polizaMan.setPolizaEncaRedaccion(new String());
            polizaMan.setPolizaEncaLiqCveCpto(0);
            polizaMan.setPolizaEncaLiqCveMovto("0");
            polizaMan.setPolizaEncaSugerCve(0);
            polizaMan.setPolizaEncaFidNum(0);
            polizaMan.setPolizaEncaFidPers(new String());
            polizaMan.setPolizaEncaFidRol(new String());
            polizaMan.setPolizaEncaBitPantalla("Poliza Manual");
            polizaMan.setPolizaEncaBitTerminal(usuarioTerminal);
            polizaMan.setPolizaEncaBitTipoOper("CANCELAR");
            polizaMan.setPolizaEncaBitUsuario(usuarioNumero);
            polizaMan.setPolizaEncaFechaMovto(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            polizaMan.setPolizaEncaFechaVal(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
        }

        private void onInicializaPoliza() {
            polizaMan.setPoliza00Etiqueta(new String());
            polizaMan.setPoliza00Valor(new String());
            polizaMan.setPoliza01Etiqueta(new String());
            polizaMan.setPoliza01Valor(new String());
            polizaMan.setPoliza02Etiqueta(new String());
            polizaMan.setPoliza02Valor(new String());
            polizaMan.setPoliza03Etiqueta(new String());
            polizaMan.setPoliza03Valor(new String());
            polizaMan.setPoliza04Etiqueta(new String());
            polizaMan.setPoliza04Valor(new String());
            polizaMan.setPoliza05Etiqueta(new String());
            polizaMan.setPoliza05Valor(new String());
            polizaMan.setPoliza06Etiqueta(new String());
            polizaMan.setPoliza06Valor(new String());
            polizaMan.setPoliza07Etiqueta(new String());
            polizaMan.setPoliza07Valor(new String());
            polizaMan.setPoliza08Etiqueta(new String());
            polizaMan.setPoliza08Valor(new String());
            polizaMan.setPoliza09Etiqueta(new String());
            polizaMan.setPoliza09Valor(new String());
            polizaMan.setPoliza10Etiqueta(new String());
            polizaMan.setPoliza10Valor(new String());
            polizaMan.setPoliza11Etiqueta(new String());
            polizaMan.setPoliza11Valor(new String());
            polizaMan.setPoliza12Etiqueta(new String());
            polizaMan.setPoliza12Valor(new String());
            polizaMan.setPoliza13Etiqueta(new String());
            polizaMan.setPoliza13Valor(new String());
            polizaMan.setPoliza14Etiqueta(new String());
            polizaMan.setPoliza14Valor(new String());
            polizaMan.setPoliza15Etiqueta(new String());
            polizaMan.setPoliza15Valor(new String());
            polizaMan.setPoliza16Etiqueta(new String());
            polizaMan.setPoliza16Valor(new String());
            polizaMan.setPoliza17Etiqueta(new String());
            polizaMan.setPoliza17Valor(new String());
            polizaMan.setPoliza18Etiqueta(new String());
            polizaMan.setPoliza18Valor(new String());
            polizaMan.setPoliza19Etiqueta(new String());
            polizaMan.setPoliza19Valor(new String());
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }
    }

    class contabilidadCancelaSaldos {

        public contabilidadCancelaSaldos() {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            mensaje = null;
            mensajeError = "Error En Tiempo de Ejecución.\n";
            nombreObjeto = "\nFuente: scotiafid.controller.mbContabilidad.contabilidadCancelaSaldos.";
            usuarioFiltro = new ArrayList<>();
            usuarioFiltro = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioFiltroAtn");
            usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            usuarioNombre = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            usuarioTerminal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal");
            validacion = Boolean.TRUE;
        }

        public void onContabilidadCancelaSaldo_MonedaInvPlazo() {
            try {
                sumaInvPlazo = 0.0;
                if (getMonedaNomInvPlazo() != null && consultaGenInvPlazoP != null) {
                    consultaGenInvPlazo = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenInvPlazoP) {
                        if (monedaNomInvPlazo.equalsIgnoreCase(cons.getGenericoCampo09())) {
                            consultaGenInvPlazo.add(cons);
                            String txtpaso = cons.getGenericoCampo05().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvPlazoTemp = validarDouble(txtpaso);
                            sumaInvPlazo = sumaInvPlazo + sumaInvPlazoTemp;
                        }
                    }
                } else {
                    consultaGenInvPlazo = consultaGenInvPlazoP;
                }
            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaInvPlazo()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaInvDirecto() {
            try {
                sumaInvDir = 0.0;
                if (getMonedaNomInvDirecto() != null && consultaGenInvDirP != null) {
                    consultaGenInvDir = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenInvDirP) {
                        if (monedaNomInvDirecto.equalsIgnoreCase(cons.getGenericoCampo06())) {
                            consultaGenInvDir.add(cons);
                            String txtpaso = cons.getGenericoCampo08().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaInvDir = sumaInvDir + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenInvPlazo = consultaGenInvPlazoP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaInvDirecto()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaBienesGarantia() {
            try {
                sumaBienGar = 0.0;
                if (getMonedaNomBienGaratia() != null && consultaGenBienGarP != null) {
                    consultaGenBienGar = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenBienGarP) {
                        if (monedaNomBienGaratia.equalsIgnoreCase(cons.getGenericoCampo08())) {
                            consultaGenBienGar.add(cons);
                            String txtpaso = cons.getGenericoCampo06().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaBienGar = sumaBienGar + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenBienGar = consultaGenBienGarP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaBienesGarantia()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaBienesFideicomitidos() {
            try {
                sumaBienfid = 0.0;
                if (getMonedaNomBienFideicomitido() != null && consultaGenBienFidP != null) {
                    consultaGenBienFid = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenBienFidP) {
                        if (monedaNomBienFideicomitido.equalsIgnoreCase(cons.getGenericoCampo07())) {
                            consultaGenBienFid.add(cons);
                            String txtpaso = cons.getGenericoCampo06().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaBienfid = sumaBienfid + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenBienFid = consultaGenBienFidP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaBienesFideicomitidos()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaCtasCheques() {
            try {
                sumaCtaChq = 0.0;
                if (getMonedaNomCtasCheques() != null && consultaGenCtaChqP != null) {
                    consultaGenCtaChq = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenCtaChqP) {
                        if (monedaNomCtasCheques.equalsIgnoreCase(cons.getGenericoCampo06())) {
                            consultaGenCtaChq.add(cons);
                            String txtpaso = cons.getGenericoCampo04().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaCtaChq = sumaCtaChq + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenCtaChq = consultaGenCtaChqP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaCtasCheques()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaHonorarios() {
            try {
                sumaHonorarios = 0.0;
                if (getMonedaNomHonorarios() != null && consultaGenHonorP != null) {
                    consultaGenHonor = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenHonorP) {
                        if (monedaNomHonorarios.equalsIgnoreCase(cons.getGenericoCampo11())) {
                            consultaGenHonor.add(cons);
                            String txtpaso = cons.getGenericoCampo08().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaHonorarios = sumaHonorarios + sumaInvDirectoTemp;
                            txtpaso = cons.getGenericoCampo09().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaHonorarios = sumaHonorarios + sumaInvDirectoTemp;
                            txtpaso = cons.getGenericoCampo10().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaHonorarios = sumaHonorarios + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenHonor = consultaGenHonorP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaHonorarios()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_MonedaFianzas() {
            try {
                sumaFianzas = 0.0;
                if (getMonedaNomFianzas() != null && consultaGenFianzasP != null) {
                    consultaGenFianzas = new ArrayList<>();
                    for (GenericoConsultaBean cons : consultaGenFianzasP) {
                        if (monedaNomFianzas.equalsIgnoreCase(cons.getGenericoCampo05())) {
                            consultaGenFianzas.add(cons);
                            String txtpaso = cons.getGenericoCampo04().replace("$", "");
                            txtpaso = txtpaso.replace(",", "");
                            Double sumaInvDirectoTemp = validarDouble(txtpaso);
                            sumaFianzas = sumaFianzas + sumaInvDirectoTemp;
                        }
                    }
                } else {
                    consultaGenFianzas = consultaGenFianzasP;
                }

            } catch (NumberFormatException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_MonedaFianzas()");
            } finally {
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_VerificaFiso() {
            try {
                oContabilidadGrales = new contabilidadGrales();
                cb.setCriterioContratoNombre(null);
                if (null != getCancelaSaldoFidecomisoTxt() && !getCancelaSaldoFidecomisoTxt().isEmpty()) {

                    if (oContabilidadGrales.onValidaNumerico(getCancelaSaldoFidecomisoTxt(), "Fideicomiso", "I")) {
                        cb.setCriterioContratoNumero(Long.parseLong(getCancelaSaldoFidecomisoTxt()));
                    } else {
                        onContabilidadCancelaSaldo_Limpia();
                    }
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo Fideicomiso no puede estar vacío...");
                    onContabilidadCancelaSaldo_Limpia();
                }
                oContabilidadGrales = null;

                geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                if (cb.getCriterioContratoNumero() != null) {
                    oContrato = new CContrato();
                    if (oContrato.onContrato_VerificaExistencia(cb.getCriterioContratoNumero()).equals(Boolean.TRUE)) {
                        if (oContrato.onContrato_VerificaSiEstaActivo(cb.getCriterioContratoNumero()).equals(Boolean.TRUE)) {
                            if (oContrato.onContrato_VerificaAtencion(usuarioFiltro, cb.getCriterioContratoNumero()).equals(Boolean.TRUE)) {
                                cb.setCriterioContratoNombre(oContrato.onContrato_ObtenNombre(cb.getCriterioContratoNumero()));
                                geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso Validado correctamente Continue...");
                            } else {
                                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                                onContabilidadCancelaSaldo_Limpia();
                            }
                        } else {
                            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso está extinto imposible continuar");
                            onContabilidadCancelaSaldo_Limpia();
                        }
                    } else {
                        mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo...");
                        onContabilidadCancelaSaldo_Limpia();
                    }
                    oContrato = null;
                }
            } catch (NumberFormatException | SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadCAncelaSaldo_VerificaFiso()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_GeneraInformacion() {
            try {
                if ((cb.getCriterioContratoNumero() == null) && (validacion.equals(Boolean.TRUE))) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se debe de ingresar un número de Fideicomiso válido");
                    validacion = Boolean.FALSE;
                }
                if (validacion.equals(Boolean.TRUE)) {
                    oContabilidad = new CContabilidad();
                    consultaGenBienFid = oContabilidad.onCancelaSaldo_ConsultaBienesFideicomitidos(cb.getCriterioContratoNumero());
                    consultaGenBienGar = oContabilidad.onCancelaSaldo_ConsultaBienesGarantia(cb.getCriterioContratoNumero());
                    consultaGenCtaChq = oContabilidad.onCancelaSaldo_ConsultaCtaCheques(cb.getCriterioContratoNumero());
                    consultaGenCtoInv = oContabilidad.onCancelaSaldo_ConsultaCtoInv(cb.getCriterioContratoNumero());
                    consultaGenFianzas = oContabilidad.onCancelaSaldo_ConsultaFianzas(cb.getCriterioContratoNumero());
                    consultaGenHonor = oContabilidad.onCancelaSaldo_ConsultaHonorarios(cb.getCriterioContratoNumero());
                    consultaGenInvPlazo = oContabilidad.onCancelaSaldo_ConsultaInversionesPlazo(cb.getCriterioContratoNumero());
                    consultaGenInvDir = oContabilidad.onCancelaSaldo_ConsultaInversionesDirecto(cb.getCriterioContratoNumero());
                    consultaGenInvPlazoP = consultaGenInvPlazo;
                    consultaGenInvDirP = consultaGenInvDir;
                    consultaGenBienGarP = consultaGenBienGar;
                    consultaGenBienFidP = consultaGenBienFid;
                    consultaGenCtaChqP = consultaGenCtaChq;
                    consultaGenCtoInvP = consultaGenCtoInv;
                    consultaGenFianzasP = consultaGenFianzas;
                    consultaGenHonorP = consultaGenHonor;

                    sumaBienGar = null;
                    sumaBienfid = null;
                    sumaCtaChq = null;
                    sumaCtoInv = null;
                    sumaFianzas = null;
                    sumaHonorarios = null;
                    sumaInvDir = null;
                    sumaInvPlazo = null;

                    monedaNomBienFideicomitido = null;
                    monedaNomBienGaratia = null;
                    monedaNomCtasCheques = null;
                    monedaNomFianzas = null;
                    monedaNomHonorarios = null;
                    monedaNomInvDirecto = null;
                    monedaNomInvPlazo = null;

                    oContabilidad = null;
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Verifique sus resultados");
                }
            } catch (SQLException Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaSaldo_GeneraInformacion()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_Limpia() {
            cb.setCriterioContratoNumero(null);
            setCancelaSaldoFidecomisoTxt(null);
            cb.setCriterioContratoNombre(null);
            consultaGenBienFid = null;
            consultaGenBienGar = null;
            consultaGenCtaChq = null;
            consultaGenCtoInv = null;
            consultaGenFianzas = null;
            consultaGenHonor = null;
            consultaGenInvDir = null;
            consultaGenInvPlazo = null;
            CancelaSaldoFidecomisoTxt = null;
        }

        public void onContabilidadCancelaSaldo_Continua() {
            List<FacesMessage> mensajeDeError = new ArrayList<>();

            try {
                if (consultaGenBienFid == null) {
                    mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Primero debe consultar..."));
                } else {
                    if (!consultaGenBienFid.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con bienes fideicomitidos"));
                    }
                    if ((!consultaGenBienGar.isEmpty())) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con bienes en garantía"));
                    }
                    if (!consultaGenCtaChq.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con cuentas de cheques"));
                    }
                    if (!consultaGenCtoInv.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta concontratos de inversión activos"));
                    }
                    if (!consultaGenFianzas.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con fianzas activas"));
                    }
                    if (!consultaGenHonor.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con honorarios pendientes"));
                    }
                    if (!consultaGenInvDir.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con inversiones en directo activas"));
                    }
                    if (!consultaGenInvPlazo.isEmpty()) {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso cuenta con inversiones a plazo activas"));
                    }
                }
                if (mensajeDeError.isEmpty()) {
                    canSld.setCsContratoNum(cb.getCriterioContratoNumero());
                    canSld.setCsOpcion(new String());
                    canSld.setCsOperacion("INICIA CANCELACION");
                    canSld.setCsFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                    canSld.setCsBitUsuario(usuarioNumero);
                    canSld.setCsBitTerminal(usuarioTerminal);
                    canSld.setCsBitPantalla("CANCELACION DE SALDOS");
                    oContabilidad = new CContabilidad();
                    if (oContabilidad.onCancelaSaldo_Ejecuta(canSld).equals(Boolean.TRUE)) {
//                      consultaGen = oContabilidad.onCancelaSaldo_Consulta(cb.getCriterioContratoNumero(), "SIMULADO");
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso bloqueado para simulación"));
                    } else {
                        mensajeDeError.add(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError()));
                    }
                    oContabilidad = null;
                    consultaGen = null;
                    geneTitulo.setGenericoTitulo00(null);
                    secHonorarios = false;
                    secHonorariosPV = true;
                    secCtasResultados = true;
                    secCtasResultadosPV = true;
                    secBienes = true;
                    secBienesPV = true;
                    secInversiones = true;
                    secInversionesPV = true;
                    secBancos = true;
                    secBancosPV = true;
                    secCtasBalance = true;
                    secCtasBalancePV = true;
                    secCtasOrden = true;
                    secCtasOrdenPV = true;
                    botonAplica = true;
                    aplHonorarios = false;
                    aplCtasResultados = false;
                    aplBienes = false;
                    aplInversiones = false;
                    aplBancos = false;
                    aplCtasBalance = false;
                    aplCtasOrden = false;
                    RequestContext.getCurrentInstance().execute("dlgSaldosVar.show();");
                    geneTitulo.setGenericoTitulo00("CTAS HONORARIOS");
                    onContabilidadCancelaSaldo_SeleccionaSaldo();

                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaSaldo_Continua()");
            } finally {
                for (FacesMessage mensaje : mensajeDeError) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_SeleccionaSaldo() {
            boolean ciclo = true;
            try {
                while (ciclo) {
                    if ((geneTitulo.getGenericoTitulo00() != null) && (!geneTitulo.getGenericoTitulo00().equals(new String()))) {
                        consultaGen = null;
                        if (geneTitulo.getGenericoTitulo00() != null) {
                            geneTitulo.setGenericoTitulo01(geneTitulo.getGenericoTitulo00());
                        }
                        canSld.setCsContratoNum(cb.getCriterioContratoNumero());
                        canSld.setCsOpcion(geneTitulo.getGenericoTitulo00());
                        canSld.setCsOperacion("CANCELA SALDOS");
                        canSld.setCsFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                        canSld.setCsBitUsuario(usuarioNumero);
                        canSld.setCsBitTerminal(usuarioTerminal);
                        canSld.setCsBitPantalla("CANCELACION DE SALDOS");

                        oContabilidad = new CContabilidad();
                        if (oContabilidad.onCancelaSaldo_Ejecuta(canSld).equals(Boolean.TRUE)) {
                            if (geneTitulo.getGenericoTitulo00().equals("CTAS RESULTADO")) {
                                canSld.setCsOpcion("TRASPASO 5220");
                                if (oContabilidad.onCancelaSaldo_Ejecuta(canSld).equals(Boolean.FALSE)) {
                                    mensajeCanSaldos = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                                }
                            }
//                        consultaGen = oContabilidad.onCancelaSaldo_Consulta(cb.getCriterioContratoNumero(), "SIMULADO");
                            consultaGen = oContabilidad.onCancelaSaldo_Consulta_Tipmovto(cb.getCriterioContratoNumero(), "SIMULADO", geneTitulo.getGenericoTitulo00());
                            mensajeCanSaldos = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Verifique sus resultados");

                            switch (geneTitulo.getGenericoTitulo00()) {
                                case "CTAS HONORARIOS":
                                    secCtasResultados = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        botonAplica = true;
                                        aplHonorarios = true;
                                        FacesMessage mensaje1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje1);
                                        if (!secHonorarios && secHonorariosPV) {
                                            geneTitulo.setGenericoTitulo00("CTAS RESULTADO");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        botonAplica = false;
                                        ciclo = false;
                                    }
                                    secHonorariosPV = false;
                                    break;
                                case "CTAS RESULTADO":
                                    secBienes = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        aplCtasResultados = true;
                                        botonAplica = true;
                                        FacesMessage mensaje2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje2);
                                        if (!secCtasResultados && secCtasResultadosPV) {
                                            geneTitulo.setGenericoTitulo00("CTAS BIENES");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        if (aplHonorarios) {
                                            botonAplica = false;

                                        } else {
                                            botonAplica = true;
                                        }
                                        ciclo = false;
                                    }
                                    secCtasResultadosPV = false;
                                    break;
                                case "CTAS BIENES":
                                    secInversiones = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        aplBienes = true;
                                        botonAplica = true;
                                        FacesMessage mensaje3 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje3);
                                        if (!secBienes && secBienesPV) {
                                            geneTitulo.setGenericoTitulo00("CTAS INVERSION");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        if (aplHonorarios && aplCtasResultados) {
                                            botonAplica = false;
                                        } else {
                                            botonAplica = true;
                                        }
                                        ciclo = false;
                                    }
                                    secBienesPV = false;
                                    break;
                                case "CTAS INVERSION":
                                    secBancos = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        botonAplica = true;
                                        aplInversiones = true;
                                        FacesMessage mensaje4 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje4);
                                        if (!secInversiones && secInversionesPV) {
                                            geneTitulo.setGenericoTitulo00("CTAS BANCOS");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        if (aplHonorarios && aplCtasResultados && aplBienes) {
                                            botonAplica = false;
                                        } else {
                                            botonAplica = true;
                                        }
                                        ciclo = false;
                                    }
                                    secInversionesPV = false;
                                    break;
                                case "CTAS BANCOS":
                                    secCtasBalance = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        botonAplica = true;
                                        aplBancos = true;
                                        FacesMessage mensaje5 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje5);
                                        if (!secBancos && secBancosPV) {
                                            geneTitulo.setGenericoTitulo00("CTAS BALANCE");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        if (aplHonorarios && aplCtasResultados && aplBienes && aplInversiones) {
                                            botonAplica = false;
                                        } else {
                                            botonAplica = true;
                                        }
                                        ciclo = false;
                                    }
                                    secBancosPV = false;
                                    break;
                                case "CTAS BALANCE":
                                    secCtasOrden = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        botonAplica = true;
                                        aplCtasBalance = true;
                                        FacesMessage mensaje6 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje6);
                                        if (!secCtasBalance && secCtasBalancePV) {
                                            geneTitulo.setGenericoTitulo00("CTAS ORDEN");
                                        } else {
                                            ciclo = false;
                                        }
                                    } else {
                                        if (aplHonorarios && aplCtasResultados && aplBienes && aplInversiones && aplBancos) {
                                            botonAplica = false;
                                        } else {
                                            botonAplica = true;
                                        }
                                        ciclo = false;
                                    }
                                    secCtasBalancePV = false;
                                    break;
                                case "CTAS ORDEN":
                                    ciclo = false;
                                    if (!oContabilidad.getIndSimulados()) {
                                        botonAplica = true;
                                        aplCtasOrden = true;
                                        FacesMessage mensaje7 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existen movimientos de Simulación de ".concat(geneTitulo.getGenericoTitulo00()));
                                        FacesContext.getCurrentInstance().addMessage(null, mensaje7);
                                    } else {
                                        if (aplHonorarios && aplCtasResultados && aplBienes && aplInversiones && aplBancos && aplCtasBalance) {
                                            botonAplica = false;
                                        } else {
                                            botonAplica = true;
                                        }
                                    }
                                    break;
                                default:
                                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccione una opción de la lista desplegable");
                                    ciclo = false;
                            }
                            oContabilidad = null;

                        } else {
                            ciclo = false;
                        }

                    } else {
                        ciclo = false;
                    }
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaSaldo_SeleccionaSaldo()");
            } finally {
                if (mensajeCanSaldos != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensajeCanSaldos);
                }
                onFinalizaObjetos();
            }
        }

        public void onContabilidadCancelaSaldo_Extigue() {
            try {
                if (geneTitulo.getGenericoTitulo00() != null) {
                    mensajeConfrima.setMensajeConfirmaUsuario(usuarioNombre);
                    mensajeConfrima.setMensajeConfirmaMensaje1("¿Confirma la aplicación contable de: ".concat(geneTitulo.getGenericoTitulo00()).concat("?"));
                    mensajeConfrima.setMensajeConfirmaOrigen("cancelaSaldos");
                    mensajeConfrima.setMensajeConfirmacionAccion("APLICA MOVTOS");
                    RequestContext.getCurrentInstance().execute("dlgPopUpGral.show();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Seleccione una opción de la lista desplegable");
                }
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaSaldo_Cancela()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        public void onContabilidadCancelaSaldo_CierraVentana() {
            try {
                canSld.setCsContratoNum(cb.getCriterioContratoNumero());
                canSld.setCsOpcion(geneTitulo.getGenericoTitulo00());
                canSld.setCsOperacion("TERMINA CANCELACION");
                canSld.setCsFecha(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
                canSld.setCsBitPantalla("CANCELACIÓN DE SALDOS");
                canSld.setCsBitTerminal(usuarioTerminal);
                canSld.setCsBitUsuario(usuarioNumero);
                oContabilidad = new CContabilidad();
                if (oContabilidad.onCancelaSaldo_Ejecuta(canSld).equals(Boolean.TRUE)) {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se actualizó el estatus del Fideicomiso a ACTIVO");
                    RequestContext.getCurrentInstance().execute("dlgSaldosVar.hide();");
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", oContabilidad.getMensajeError());
                }
                oContabilidad = null;
            } catch (AbstractMethodError Err) {
                logger.error(Err.getMessage() + "onContabilidadCancelaSaldo_CierraVentana()");
            } finally {
                if (mensaje != null) {
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        }

        private void onFinalizaObjetos() {
            if (oClave != null) {
                oClave = null;
            }
            if (oContabilidad != null) {
                oContabilidad = null;
            }
            if (oContrato != null) {
                oContrato = null;
            }
            if (oMoneda != null) {
                oMoneda = null;
            }
        }
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean permisoEscrituraGarantias;
    private Boolean permisoEscrituraBienes;
    private Boolean permisoEscrituraPoliza;
    private Boolean permisoEscrituraPolizaAdm;
    private Boolean secHonorariosPV;
    private Boolean secHonorarios;
    private Boolean secCtasResultados;
    private Boolean secCtasResultadosPV;
    private Boolean secBienes;
    private Boolean secBienesPV;
    private Boolean secInversiones;
    private Boolean secInversionesPV;
    private Boolean secBancos;
    private Boolean secBancosPV;
    private Boolean secCtasBalance;
    private Boolean secCtasBalancePV;
    private Boolean secCtasOrden;
    private Boolean secCtasOrdenPV;

    private Boolean aplHonorarios;
    private Boolean aplCtasResultados;
    private Boolean aplBienes;
    private Boolean aplInversiones;
    private Boolean aplBancos;
    private Boolean aplCtasBalance;
    private Boolean aplCtasOrden;

    private Boolean botonAplica;

    private String monedaNomInvDirecto;
    private String monedaNomBienGaratia;
    private String monedaNomBienFideicomitido;
    private String monedaNomCtasCheques;
    private String monedaNomHonorarios;
    private String monedaNomFianzas;

    private String BienFideicomisoTxt;
    private String BienSubFisoTxt;
    private String BienImporteTxt;
    private String BienTipoCambiTxt;
    private String BienImporteNvoTxt;
    private String CancelaSaldoFidecomisoTxt;

    public String getBienFideicomisoTxt() {
        return BienFideicomisoTxt;
    }

    public void setBienFideicomisoTxt(String BienFideicomisoTxt) {
        this.BienFideicomisoTxt = BienFideicomisoTxt;
    }

    public String getMonedaNomInvPlazo() {
        return monedaNomInvPlazo;
    }

    public void setMonedaNomInvPlazo(String monedaNomInvPlazo) {
        this.monedaNomInvPlazo = monedaNomInvPlazo;
    }

    public String getMonedaNomInvDirecto() {
        return monedaNomInvDirecto;
    }

    public void setMonedaNomInvDirecto(String monedaNomInvDirecto) {
        this.monedaNomInvDirecto = monedaNomInvDirecto;
    }

    public String getMonedaNomBienGaratia() {
        return monedaNomBienGaratia;
    }

    public void setMonedaNomBienGaratia(String monedaNomBienGaratia) {
        this.monedaNomBienGaratia = monedaNomBienGaratia;
    }

    public String getMonedaNomBienFideicomitido() {
        return monedaNomBienFideicomitido;
    }

    public void setMonedaNomBienFideicomitido(String monedaNomBienFideicomitido) {
        this.monedaNomBienFideicomitido = monedaNomBienFideicomitido;
    }

    public String getMonedaNomCtasCheques() {
        return monedaNomCtasCheques;
    }

    public void setMonedaNomCtasCheques(String monedaNomCtasCheques) {
        this.monedaNomCtasCheques = monedaNomCtasCheques;
    }

    public String getMonedaNomHonorarios() {
        return monedaNomHonorarios;
    }

    public void setMonedaNomHonorarios(String monedaNomHonorarios) {
        this.monedaNomHonorarios = monedaNomHonorarios;
    }

    public String getMonedaNomFianzas() {
        return monedaNomFianzas;
    }

    public void setMonedaNomFianzas(String monedaNomFianzas) {
        this.monedaNomFianzas = monedaNomFianzas;
    }

    public Boolean getBotonAplica() {
        return botonAplica;
    }

    public void setBotonAplica(Boolean botonAplica) {
        this.botonAplica = botonAplica;
    }

    public Boolean getAplHonorarios() {
        return aplHonorarios;
    }

    public void setAplHonorarios(Boolean aplHonorarios) {
        this.aplHonorarios = aplHonorarios;
    }

    public Boolean getAplCtasResultados() {
        return aplCtasResultados;
    }

    public void setAplCtasResultados(Boolean aplCtasResultados) {
        this.aplCtasResultados = aplCtasResultados;
    }

    public Boolean getAplBienes() {
        return aplBienes;
    }

    public void setAplBienes(Boolean aplBienes) {
        this.aplBienes = aplBienes;
    }

    public Boolean getAplInversiones() {
        return aplInversiones;
    }

    public void setAplInversiones(Boolean aplInversiones) {
        this.aplInversiones = aplInversiones;
    }

    public Boolean getAplBancos() {
        return aplBancos;
    }

    public void setAplBancos(Boolean aplBancos) {
        this.aplBancos = aplBancos;
    }

    public Boolean getAplCtasBalance() {
        return aplCtasBalance;
    }

    public void setAplCtasBalance(Boolean aplCtasBalance) {
        this.aplCtasBalance = aplCtasBalance;
    }

    public Boolean getAplCtasOrden() {
        return aplCtasOrden;
    }

    public void setAplCtasOrden(Boolean aplCtasOrden) {
        this.aplCtasOrden = aplCtasOrden;
    }

    public Boolean getSecHonorarios() {
        return secHonorarios;
    }

    public void setSecHonorarios(Boolean secHonorarios) {
        this.secHonorarios = secHonorarios;
    }

    public Boolean getSecCtasResultados() {
        return secCtasResultados;
    }

    public void setSecCtasResultados(Boolean secCtasResultados) {
        this.secCtasResultados = secCtasResultados;
    }

    public Boolean getSecBienes() {
        return secBienes;
    }

    public void setSecBienes(Boolean secBienes) {
        this.secBienes = secBienes;
    }

    public Boolean getSecInversiones() {
        return secInversiones;
    }

    public void setSecInversiones(Boolean secInversiones) {
        this.secInversiones = secInversiones;
    }

    public Boolean getSecBancos() {
        return secBancos;
    }

    public void setSecBancos(Boolean secBancos) {
        this.secBancos = secBancos;
    }

    public Boolean getSecCtasBalance() {
        return secCtasBalance;
    }

    public void setSecCtasBalance(Boolean secCtasBalance) {
        this.secCtasBalance = secCtasBalance;
    }

    public Boolean getSecCtasOrden() {
        return secCtasOrden;
    }

    public void setSecCtasOrden(Boolean secCtasOrden) {
        this.secCtasOrden = secCtasOrden;
    }

    public Boolean getPermisoEscrituraPoliza() {
        return permisoEscrituraPoliza;
    }

    public void setPermisoEscrituraPoliza(Boolean permisoEscrituraPoliza) {
        this.permisoEscrituraPoliza = permisoEscrituraPoliza;
    }

    public Boolean getPermisoEscrituraGarantias() {
        return permisoEscrituraGarantias;
    }

    public void setPermisoEscrituraGarantias(Boolean permisoEscrituraGarantias) {
        this.permisoEscrituraGarantias = permisoEscrituraGarantias;
    }

    public Boolean getPermisoEscrituraBienes() {
        return permisoEscrituraBienes;
    }

    public void setPermisoEscrituraBienes(Boolean permisoEscrituraBienes) {
        this.permisoEscrituraBienes = permisoEscrituraBienes;
    }

    public MBContabilidad() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    @PostConstruct
    public void onPostConstruct() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                //System.out.println("Sesión no valida ..."); 
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                //System.out.println(peticionURL.getRequestURI());
                if (peticionURL.getRequestURI().contains("contabilidadGarantiaGral.sb")) {
                    oClave = new CClave();
                    listaStatus = oClave.onClave_ObtenListadoElementos(31);
                    oClave = null;
                }
                if (peticionURL.getRequestURI().contains("contabilidadBienFide.sb")) {
                    oClave = new CClave();
                    listaBienFideTipo = oClave.onClave_ObtenListadoElementos(70);
                    listaStatus = oClave.onClave_ObtenListadoElementos(31);
                    oClave = null;
                    oComunes = new CComunes();
                    listaNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(UCase(not_nom_notario))||'»'||not_num_notario", "NOTARIOS", new String());
//                    listaNombresNotarios = oComunes.onComunes_ObtenListadoContenidoCampo("Trim(Ucase(not_nom_notario))", "Notarios", "WHERE not_cve_st_notario = 'ACTIVO'");
                    oComunes = null;
                    oMoneda = new CMoneda();
                    listaMoneda = oMoneda.onMoneda_ObtenListadoMonedas();
                    oMoneda = null;
                }
                if (peticionURL.getRequestURI().contains("contabilidadPolizaMan.sb") || peticionURL.getRequestURI().contains("contabilidadPolizaManAdm.sb")) {
                    cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                    cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                    cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));

                    cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
                    cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                    cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
                    //Si el mes anterior esta abierto, habilitamos el campo de citerio mm, aaaa  

                }
                if (peticionURL.getRequestURI().contains("contabilidadCancelaOper.sb")) {
                    cbc.setCriterioFechaDD(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia")));
                    cbc.setCriterioFechaMM(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes")));
                    cbc.setCriterioFechaYYYY(Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño")));
                    cbc.setTxtCriterioFechaDD(cbc.getCriterioFechaDD().toString());
                    cbc.setTxtCriterioFechaMM(cbc.getCriterioFechaMM().toString());
                    cbc.setTxtCriterioFechaYYYY(cbc.getCriterioFechaYYYY().toString());
                    //Si el mes anterior esta abierto, habilitamos el campo de citerio mm, aaaa  
                    String cveMSAAbierto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto");
                    if (cveMSAAbierto.equals("1")) {
                        geneDes.setGenericoDesHabilitado00(Boolean.FALSE);
                        geneColor.setColor00("#ffffff;");
                    } else {
                        geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                        geneColor.setColor00("#ffffcc;");
                    }
                }
                if (peticionURL.getRequestURI().contains("contabilidadCancelaSaldo.sb")) {
                    oMoneda = new CMoneda();
                    geneDes.setGenericoDesHabilitado00(Boolean.TRUE);
                    listaMoneda = oMoneda.onMoneda_ObtenListadoMonedas_Clave();
                    oMoneda = null;

                }
            }
            formatoDecimal2D = new DecimalFormat("###,###.##");
            formatoDecimal2D.setMaximumFractionDigits(2);
            formatoDecimal2D.setMinimumFractionDigits(2);
            formatoDecimal6D = new DecimalFormat("###,###.######");
            formatoDecimal6D.setMaximumFractionDigits(2);
            formatoDecimal6D.setMinimumFractionDigits(6);
            formatoImporte2D = new DecimalFormat("$###,###.##");
            formatoImporte2D.setMaximumFractionDigits(2);
            formatoImporte2D.setMinimumFractionDigits(2);

            asignaPermisos();
        } catch (IOException | NumberFormatException | SQLException Err) {
            logger.error(Err.getMessage() + "onPostConstruct()");
        } finally {
            if (oClave != null) {
                oClave = null;
            }
        }
    }

    public void asignaPermisos() {
        String permisoGarantias = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contabilidadGarantiaGral.sb");
        if (permisoGarantias != null) {
            if (permisoGarantias.equals("NO")) {
                permisoEscrituraGarantias = true;
            } else {
                permisoEscrituraGarantias = false;
            }
        }
        String permisoBienes = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contabilidadBienFide.sb");
        if (permisoBienes != null) {
            if (permisoBienes.equals("NO")) {
                permisoEscrituraBienes = true;
            } else {
                permisoEscrituraBienes = false;
            }
        }
        String permisoPolizaAdm = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contabilidadPolizaManAdm.sb");
        if (permisoPolizaAdm != null) {
            if (permisoPolizaAdm.equals("NO")) {
                permisoEscrituraPolizaAdm = true;
            } else {
                permisoEscrituraPolizaAdm = false;
            }
        }
        String permisoPoliza = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contabilidadPolizaMan.sb");
        if (permisoPoliza != null) {
            if (permisoPoliza.equals("NO")) {
                permisoEscrituraPoliza = true;
            } else {
                permisoEscrituraPoliza = false;
            }
        }

    }

    public void cleanDataTable(String component) {
        DataTable dataTable = null;
        dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(component);
        dataTable.reset();
    }

    public void setFirstDataTable(String component) {
        DataTable dataTable = null;
        dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(component);
        dataTable.setFirst(0);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   G R A L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadGrales_AplicaConfirmacion() {
        oContabilidadGrales = new contabilidadGrales();
        oContabilidadGrales.onContabilidadGrales_AplicaConfirmacion();
        oContabilidadGrales = null;
    }

    public void onContabilidadGrales_ObtenContratoNombre() {
        oContabilidadGrales = new contabilidadGrales();
        oContabilidadGrales.onContabilidadGrales_ObtenContratoNombre();
        oContabilidadGrales = null;
    }

    public void onContabilidadGrales_SeleccionaFicha(TabChangeEvent event) {
        oContabilidadGrales = new contabilidadGrales();
        oContabilidadGrales.onContabilidadGrales_SeleccionaFicha(event.getTab().getTitle());
        oContabilidadGrales = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   G A R A N T I A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadGarantia_InicializaGarantia() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_InicializaGarantia();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_Consulta() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_Consulta();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_Limpia() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_Limpia();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroAplica() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroAplica();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroElimina() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroElimina();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroModifica() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroModifica();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroNuevo() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroNuevo();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroSelecciona() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroSelecciona();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroVerificaSubFiso() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroVerificaSubFiso();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_RegistroSeleccionaBien() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_RegistroSeleccionaBien();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_VerificaContratoIngresado() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_VerificaContratoIngresado();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_DespliegaVentana() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_DespliegaVentana();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_Entrada() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_Entrada();
        oContabilidadGarantia = null;

    }

    public void onContabilidadRevalucionPeriodicidad() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadRevalucionPeriodicidad();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_EntradaVerifica00() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_EntradaVerifica00();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_EntradaVerifica01() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_EntradaVerifica01();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantia_Cancela() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantia_Cancela();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_Modifica() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_Modifica();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_Salida() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_Salida();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_RevaluacionConfirma() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_RevaluacionConfirma();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_RevaluacionDespliega() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_RevaluacionDespliega();
        oContabilidadGarantia = null;
    }

    public void onContabilidadGarantiaBien_RevaluacionVerifica() {
        oContabilidadGarantia = new contabilidadGarantia();
        oContabilidadGarantia.onContabilidadGarantiaBien_RevaluacionVerifica();
        oContabilidadGarantia = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C O N S U L T A   S A L D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadConsSaldo_Consulta() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_Consulta();
        oContabilidadSaldo = null;
    }

    public void onContabilidadConsSaldo_Limpia() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_Limpia();
        oContabilidadSaldo = null;
    }


    public void onContabilidadConsSaldo_RegistroBuscaCtoNom() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_RegistroBuscaCtoNom();
        oContabilidadSaldo = null;
    }

    public void onContabilidadConsSaldo_Exporta() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_Exporta();
        oContabilidadSaldo = null;
    }

    public void onContabilidadConsSaldo_SeleccionaDestino() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_SeleccionaDestino();
        oContabilidadSaldo = null;
    }

    public void onContabilidadConsSaldo_SeleccionaCuenta() {
        oContabilidadSaldo = new contabilidadSaldo();
        oContabilidadSaldo.onContabilidadConsSaldo_SeleccionaCuenta();
        oContabilidadSaldo = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C O N S U L T A   A S I E N T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadConsAsie_Consulta() {
        oContabilidadConsAsie = new contabilidadConsAsie();
        oContabilidadConsAsie.onContabilidadConsAsie_Consulta();
        oContabilidadConsAsie = null;
    }

    public void onContabilidadConsAsie_Limpia() {
        oContabilidadConsAsie = new contabilidadConsAsie();
        oContabilidadConsAsie.onContabilidadConsAsie_Limpia();
        oContabilidadConsAsie = null;
    }

    public void onContabilidadConsAsie_Exporta() {
        oContabilidadConsAsie = new contabilidadConsAsie();
        oContabilidadConsAsie.onContabilidadConsAsie_Exporta();
        oContabilidadConsAsie = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C O N S U L T A   M O V T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadConsMovto_Consulta() {
        oContabilidadConsMovto = new contabilidadConsMovto();
        oContabilidadConsMovto.onContabilidadConsMovto_Consulta();
        oContabilidadConsMovto = null;
    }

    public void onContabilidadConsMovto_Selecciona() {
        oContabilidadConsMovto = new contabilidadConsMovto();
        oContabilidadConsMovto.onContabilidadConsMovto_Selecciona();
        oContabilidadConsMovto = null;
    }

    public void onContabilidadConsMovto_Limpia() {
        oContabilidadConsMovto = new contabilidadConsMovto();
        oContabilidadConsMovto.onContabilidadConsMovto_Limpia();
        oContabilidadConsMovto = null;
    }

    public void onContabilidadConsMovto_Exporta() {
        oContabilidadConsMovto = new contabilidadConsMovto();
        oContabilidadConsMovto.onContabilidadConsMovto_Exporta();
        oContabilidadConsMovto = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C O N S U L T A   S A L D O S   P R O M E D I O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadConsSldPrm_Consulta() {
        oContabilidadConsSldPrm = new contabilidadConsSaldoProm();
        oContabilidadConsSldPrm.onContabilidadConsSldPrm_Consulta();
        oContabilidadConsSldPrm = null;
    }

    public void onContabilidadConsSldPrm_Limpia() {
        oContabilidadConsSldPrm = new contabilidadConsSaldoProm();
        oContabilidadConsSldPrm.onContabilidadConsSldPrm_Limpia();
        oContabilidadConsSldPrm = null;
    }

    public void onContabilidadConsSldPrm_Exporta() {
        oContabilidadConsSldPrm = new contabilidadConsSaldoProm();
        oContabilidadConsSldPrm.onContabilidadConsSldPrm_Exporta();
        oContabilidadConsSldPrm = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   B I E N E S   F I D E I C O M
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadBienFide_ConsultaEjecuta() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_ConsultaEjecuta();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_ConsultaLimpia() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_ConsultaLimpia();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_ConsultaCargaBienClasif() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_CargaClasificacionBien(cbc.getCriterioBienFideTipo());
        oContabilidadBienFide = null;
    }

public void onContabilidadBienFide_CargaClasificacionBien_Criterio() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_CargaClasificacionBien_Criterio(cbc.getCriterioBienFideTipo());
        oContabilidadBienFide = null;
    }
    public void onContabilidadBienFide_RegistroAplica() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroAplica();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroBuscaCtoNom() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroBuscaCtoNom();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroVerificaSubFiso() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroVerificaSubFiso();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroBuscaBienClasif() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_CargaClasificacionBien(bienFide.getBienFideTipo());
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroBuscaTipoCambio() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroBuscaTipoCambio();
        oContabilidadBienFide = null;
    }

    public void onContabilidadGarantia_RegistroBuscaTipoCambio() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadGarantia_RegistroBuscaTipoCambio();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroSelecciona() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroSelecciona();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_SelectSubFiso() {

        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_SelectSubFiso();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroModifica() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroModifica();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroEntrada() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroEntrada();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroEntradaForma() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroEntradaForma();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroSalida() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroSalida();
        oContabilidadBienFide = null;
    }

    public void onContabilidadBienFide_RegistroRevalua() {
        oContabilidadBienFide = new contabilidadBienFide();
        oContabilidadBienFide.onContabilidadBienFide_RegistroRevalua();
        oContabilidadBienFide = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   I N D I V I D U A L I Z A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onGarantias_VerificaNumerico(AjaxBehaviorEvent event) {

        oContabilidadGarantia = new contabilidadGarantia();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtCriterioAX1);
        }
        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtCriterioAX2);
        }

        if (componet.getAttributes().get("txtCriterioPlaza") != null) {
            String txtCriterioPlaza = (String) componet.getAttributes().get("txtCriterioPlaza");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtCriterioPlaza);
        }

        if (componet.getAttributes().get("txtGarantiaContratoNumeroSub") != null) {
            String txtGarantiaContratoNumeroSub = (String) componet.getAttributes().get("txtGarantiaContratoNumeroSub");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtGarantiaContratoNumeroSub);
        }

        if (componet.getAttributes().get("txtGarantiaImporteCredito") != null) {
            String txtGarantiaImporteCredito = (String) componet.getAttributes().get("txtGarantiaImporteCredito");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtGarantiaImporteCredito);
        }

        if (componet.getAttributes().get("txtGarantiaBienImporte") != null) {
            String txtGarantiaBienImporte = (String) componet.getAttributes().get("txtGarantiaBienImporte");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtGarantiaBienImporte);
        }

        if (componet.getAttributes().get("txtGarantiaBienSalidaImporteNvo") != null) {
            String txtGarantiaBienSalidaImporteNvo = (String) componet.getAttributes().get("txtGarantiaBienSalidaImporteNvo");
            oContabilidadGarantia.onGarantia_VerificaNumerico(txtGarantiaBienSalidaImporteNvo);
        }

        oContabilidadGarantia = null;
    }

    public void onConsutaMovtos_VerificaNumerico(AjaxBehaviorEvent event) {
        oContabilidadConsMovto = new contabilidadConsMovto();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioAX1);
        }
        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioAX2);
        }
        if (componet.getAttributes().get("txtCriterioFolio") != null) {
            String txtCriterioFolio = (String) componet.getAttributes().get("txtCriterioFolio");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioFolio);
        }
        if (componet.getAttributes().get("criterioModulo") != null) {
            String criterioModulo = (String) componet.getAttributes().get("criterioModulo");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(criterioModulo);
        }
        if (componet.getAttributes().get("txtCriterioTransId") != null) {
            String txtCriterioTransId = (String) componet.getAttributes().get("txtCriterioTransId");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioTransId);
        }
        if (componet.getAttributes().get("txtCriterioFechaDD") != null) {
            String txtCriterioFechaDD = (String) componet.getAttributes().get("txtCriterioFechaDD");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioFechaDD);
        }
        if (componet.getAttributes().get("txtCriterioFechaMM") != null) {
            String txtCriterioFechaMM = (String) componet.getAttributes().get("txtCriterioFechaMM");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioFechaMM);
        }

        if (componet.getAttributes().get("txtCriterioFechaYYYY") != null) {
            String txtCriterioFechaYYYY = (String) componet.getAttributes().get("txtCriterioFechaYYYY");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioFechaYYYY);
        }

        if (componet.getAttributes().get("txtCriterioPlaza") != null) {
            String txtCriterioPlaza = (String) componet.getAttributes().get("txtCriterioPlaza");
            oContabilidadConsMovto.onConsutaMovtos_VerificaNumerico(txtCriterioPlaza);
        }

        oContabilidadConsMovto = null;
    }

    public void onPolizaMan_VerificaNumerico(AjaxBehaviorEvent event) {
        oContabilidadPolMan = new contabilidadPolizaMan();
        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioAX1);
        }
        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioAX2);
        }
        if (componet.getAttributes().get("txtCriterioFolio") != null) {
            String txtCriterioFolio = (String) componet.getAttributes().get("txtCriterioFolio");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioFolio);
        }
        if (componet.getAttributes().get("txtCriterioTransId") != null) {
            String txtCriterioTransId = (String) componet.getAttributes().get("txtCriterioTransId");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioTransId);
        }
        if (componet.getAttributes().get("txtCriterioFechaDD") != null) {
            String txtCriterioFechaDD = (String) componet.getAttributes().get("txtCriterioFechaDD");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioFechaDD);
        }
        if (componet.getAttributes().get("txtCriterioFechaMM") != null) {
            String txtCriterioFechaMM = (String) componet.getAttributes().get("txtCriterioFechaMM");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioFechaMM);
        }

        if (componet.getAttributes().get("txtCriterioFechaYYYY") != null) {
            String txtCriterioFechaYYYY = (String) componet.getAttributes().get("txtCriterioFechaYYYY");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(txtCriterioFechaYYYY);
        }

        if (componet.getAttributes().get("poliza01Valor") != null) {
            String poliza01Valor = (String) componet.getAttributes().get("poliza01Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza01Valor);
        }

        if (componet.getAttributes().get("poliza02Valor") != null) {
            String poliza02Valor = (String) componet.getAttributes().get("poliza02Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza02Valor);
        }

        if (componet.getAttributes().get("poliza03Valor") != null) {
            String poliza03Valor = (String) componet.getAttributes().get("poliza03Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza03Valor);
        }

        if (componet.getAttributes().get("poliza04Valor") != null) {
            String poliza04Valor = (String) componet.getAttributes().get("poliza04Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza04Valor);
        }

        if (componet.getAttributes().get("poliza05Valor") != null) {
            String poliza05Valor = (String) componet.getAttributes().get("poliza05Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza05Valor);
        }

        if (componet.getAttributes().get("poliza06Valor") != null) {
            String poliza06Valor = (String) componet.getAttributes().get("poliza06Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza06Valor);
        }

        if (componet.getAttributes().get("poliza07Valor") != null) {
            String poliza07Valor = (String) componet.getAttributes().get("poliza07Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza07Valor);
        }

        if (componet.getAttributes().get("poliza08Valor") != null) {
            String poliza08Valor = (String) componet.getAttributes().get("poliza08Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza08Valor);
        }

        if (componet.getAttributes().get("poliza09Valor") != null) {
            String poliza09Valor = (String) componet.getAttributes().get("poliza09Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza09Valor);
        }

        if (componet.getAttributes().get("poliza10Valor") != null) {
            String poliza10Valor = (String) componet.getAttributes().get("poliza10Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza10Valor);
        }

        if (componet.getAttributes().get("poliza11Valor") != null) {
            String poliza11Valor = (String) componet.getAttributes().get("poliza11Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza11Valor);
        }

        if (componet.getAttributes().get("poliza12Valor") != null) {
            String poliza12Valor = (String) componet.getAttributes().get("poliza12Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza12Valor);
        }

        if (componet.getAttributes().get("poliza13Valor") != null) {
            String poliza13Valor = (String) componet.getAttributes().get("poliza13Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza13Valor);
        }

        if (componet.getAttributes().get("poliza14Valor") != null) {
            String poliza14Valor = (String) componet.getAttributes().get("poliza14Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza14Valor);
        }

        if (componet.getAttributes().get("poliza15Valor") != null) {
            String poliza15Valor = (String) componet.getAttributes().get("poliza15Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza15Valor);
        }

        if (componet.getAttributes().get("poliza16Valor") != null) {
            String poliza16Valor = (String) componet.getAttributes().get("poliza16Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza16Valor);
        }

        if (componet.getAttributes().get("poliza17Valor") != null) {
            String poliza17Valor = (String) componet.getAttributes().get("poliza17Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza17Valor);
        }

        if (componet.getAttributes().get("poliza18Valor") != null) {
            String poliza18Valor = (String) componet.getAttributes().get("poliza18Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza18Valor);
        }

        if (componet.getAttributes().get("poliza18Valor") != null) {
            String poliza18Valor = (String) componet.getAttributes().get("poliza18Valor");
            oContabilidadPolMan.onPolizaMan_VerificaNumerico(poliza18Valor);
        }

        oContabilidadPolMan = null;
    }

    public void onBienIndi_VerificaNumerico(AjaxBehaviorEvent event) {

        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtCriterioAX1);
        }
        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtCriterioAX2);
        }

        if (componet.getAttributes().get("txtCriterioPlaza") != null) {
            String txtCriterioPlaza = (String) componet.getAttributes().get("txtCriterioPlaza");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtCriterioPlaza);
        }

        if (componet.getAttributes().get("txtBienFideUnidadValor") != null) {
            String txtBienFideUnidadValor = (String) componet.getAttributes().get("txtBienFideUnidadValor");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtBienFideUnidadValor);
        }

        if (componet.getAttributes().get("txtBienFideUnidadSuperficie") != null) {
            String txtBienFideUnidadSuperficie = (String) componet.getAttributes().get("txtBienFideUnidadSuperficie");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtBienFideUnidadSuperficie);
        }

        if (componet.getAttributes().get("bienFideUnidadIndivSuperficieM2") != null) {
            String bienFideUnidadIndivSuperficieM2 = (String) componet.getAttributes().get("bienFideUnidadIndivSuperficieM2");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(bienFideUnidadIndivSuperficieM2);
        }

        if (componet.getAttributes().get("bienFideUnidadIndivIndiviso") != null) {
            String bienFideUnidadIndivIndiviso = (String) componet.getAttributes().get("bienFideUnidadIndivIndiviso");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(bienFideUnidadIndivIndiviso);
        }

        if (componet.getAttributes().get("txtBienFideUnidadIndivPrecio") != null) {
            String txtBienFideUnidadIndivPrecio = (String) componet.getAttributes().get("txtBienFideUnidadIndivPrecio");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(txtBienFideUnidadIndivPrecio);
        }

        if (componet.getAttributes().get("bienFideUnidadIndivNiveles") != null) {
            String bienFideUnidadIndivNiveles = (String) componet.getAttributes().get("bienFideUnidadIndivNiveles");
            oContabilidadBienFideIndiv.onBienIndi_VerificaNumerico(bienFideUnidadIndivNiveles);
        }

        oContabilidadBienFideIndiv = null;
    }

    public void onConsutaAsientos_VerificaNumerico(AjaxBehaviorEvent event) {
        oContabilidadConsAsie = new contabilidadConsAsie();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioCTAM") != null) {
            String txtCriterioCTAM = (String) componet.getAttributes().get("txtCriterioCTAM");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioCTAM);
        }
        if (componet.getAttributes().get("txtCriterioSC1") != null) {
            String txtCriterioSC1 = (String) componet.getAttributes().get("txtCriterioSC1");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioSC1);
        }
        if (componet.getAttributes().get("txtCriterioSC2") != null) {
            String txtCriterioSC2 = (String) componet.getAttributes().get("txtCriterioSC2");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioSC2);
        }
        if (componet.getAttributes().get("txtCriterioSC3") != null) {
            String txtCriterioSC3 = (String) componet.getAttributes().get("txtCriterioSC3");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioSC3);
        }
        if (componet.getAttributes().get("txtCriterioSC4") != null) {
            String txtCriterioSC4 = (String) componet.getAttributes().get("txtCriterioSC4");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioSC4);
        }
        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioAX1);
        }
        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioAX2);
        }

        if (componet.getAttributes().get("txtCriterioAX3") != null) {
            String txtCriterioAX3 = (String) componet.getAttributes().get("txtCriterioAX3");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioAX3);
        }

        if (componet.getAttributes().get("txtCriterioFolio") != null) {
            String txtCriterioFolio = (String) componet.getAttributes().get("txtCriterioFolio");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioFolio);
        }

        if (componet.getAttributes().get("txtCriterioPlaza") != null) {
            String txtCriterioPlaza = (String) componet.getAttributes().get("txtCriterioPlaza");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioPlaza);
        }

        if (componet.getAttributes().get("txtCriterioFechaDD") != null) {
            String txtCriterioFechaDD = (String) componet.getAttributes().get("txtCriterioFechaDD");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioFechaDD);
        }

        if (componet.getAttributes().get("txtCriterioFechaMM") != null) {
            String txtCriterioFechaMM = (String) componet.getAttributes().get("txtCriterioFechaMM");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioFechaMM);
        }

        if (componet.getAttributes().get("txtCriterioFechaYY") != null) {
            String txtCriterioFechaYY = (String) componet.getAttributes().get("txtCriterioFechaYY");
            oContabilidadConsAsie.onConsutaAsientos_VerificaNumerico(txtCriterioFechaYY);
        }

        oContabilidadConsAsie = null;
    }

    public void onConsutaSaldos_VerificaNumerico(AjaxBehaviorEvent event) {
        oContabilidadSaldo = new contabilidadSaldo();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioCTAM") != null) {
            String txtCriterioCTAM = (String) componet.getAttributes().get("txtCriterioCTAM");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioCTAM);
        }

        if (componet.getAttributes().get("txtCriterioSC1") != null) {
            String txtCriterioSC1 = (String) componet.getAttributes().get("txtCriterioSC1");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioSC1);
        }

        if (componet.getAttributes().get("txtCriterioSC2") != null) {
            String txtCriterioSC2 = (String) componet.getAttributes().get("txtCriterioSC2");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioSC2);
        }

        if (componet.getAttributes().get("txtCriterioSC3") != null) {
            String txtCriterioSC3 = (String) componet.getAttributes().get("txtCriterioSC3");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioSC3);
        }

        if (componet.getAttributes().get("txtCriterioSC4") != null) {
            String txtCriterioSC4 = (String) componet.getAttributes().get("txtCriterioSC4");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioSC4);
        }

        if (componet.getAttributes().get("txtCriterioAX1") != null) {
            String txtCriterioAX1 = (String) componet.getAttributes().get("txtCriterioAX1");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioAX1);
        }

        if (componet.getAttributes().get("txtCriterioAX2") != null) {
            String txtCriterioAX2 = (String) componet.getAttributes().get("txtCriterioAX2");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioAX2);
        }

        if (componet.getAttributes().get("txtCriterioMes") != null) {
            String txtCriterioMes = (String) componet.getAttributes().get("txtCriterioMes");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioMes);
        }

        if (componet.getAttributes().get("txtCriterioAño") != null) {
            String txtCriterioAño = (String) componet.getAttributes().get("txtCriterioAño");
            oContabilidadSaldo.onConsutaSaldos_VerificaNumerico(txtCriterioAño);
        }

        oContabilidadSaldo = null;
    }

    public void onConsutaSaldosProm_VerificaNumerico(AjaxBehaviorEvent event) {

        oContabilidadConsSldPrm = new contabilidadConsSaldoProm();

        UIComponent componet = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (componet.getAttributes().get("txtCriterioContratoNumero") != null) {
            String txtCriterioContratoNumero = (String) componet.getAttributes().get("txtCriterioContratoNumero");
            oContabilidadConsSldPrm.onConsutaSaldosPrm_VerificaNumerico(txtCriterioContratoNumero);
        }

        if (componet.getAttributes().get("txtCriterioContratoNumeroSub") != null) {
            String txtCriterioContratoNumeroSub = (String) componet.getAttributes().get("txtCriterioContratoNumeroSub");
            oContabilidadConsSldPrm.onConsutaSaldosPrm_VerificaNumerico(txtCriterioContratoNumeroSub);
        }

        oContabilidadConsSldPrm = null;
    }

    public void onContabilidadBienFideIndiv_ArchivoDespliegaVentana() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ArchivoDespliegaVentana();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ArchivoTransfiere(FileUploadEvent event) {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ArchivoTransfiere(event);
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ArchivoAplica() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ArchivoAplica();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ConsultaEjecuta() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ConsultaEjecuta();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ConsultaLimpia() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ConsultaLimpia();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ConsultaSeleccionaBien() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ConsultaSeleccionaBien();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadNuevo() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadNuevo();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_ConsultaSeleccionaUnidad() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_ConsultaSeleccionaUnidad();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadExtraeNotario() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadExtraeNotario();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadRegistroModifica() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadRegistroModifica();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadRegistroAplica() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadRegistroAplica();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadRegistroELimina() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadRegistroELimina();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadRegistroIndividualiza() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadRegistroIndividualiza();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivAplica() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivAplica();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivModifica() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivModifica();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivElimina() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivElimina();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivLiquidaSeleccionaNotario() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivLiquidaSeleccionaNotario();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivLiquida() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivLiquida();
        oContabilidadBienFideIndiv = null;
    }

    public void onContabilidadBienFideIndiv_UnidadIndivSelecciona() {
        oContabilidadBienFideIndiv = new contabilidadBienFideIndiv();
        oContabilidadBienFideIndiv.onContabilidadBienFideIndiv_UnidadIndivSelecciona();
        oContabilidadBienFideIndiv = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   P O L I Z A   M A N U A L
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadPolMan_ArchivoVentanaDespliega() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ArchivoVentanaDespliega();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ArchivoVentanaCierra() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ArchivoVentanaCierra();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ArchivoVentanaCierraAdm() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ArchivoVentanaCierraAdm();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ArchivoTransfiere(FileUploadEvent event) {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ArchivoTransfiere(event);
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_BulkLoad(FileUploadEvent event) {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_BulkLoad(event);
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ConsultaEjecuta() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ConsultaEjecuta();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ConsultaEjecutaAdm() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ConsultaEjecutaAdm();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ConsultaSelecciona() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ConsultaSelecciona();
        oContabilidadPolMan = null;
    }

    public void onPageChange() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onPageChange();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_ConsultaLimpia() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_ConsultaLimpia();
        oContabilidadPolMan = null;
    }

    public void onPooliza_RetieneFechaValor() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onPooliza_RetieneFechaValor();
        oContabilidadPolMan = null;
    }

    public void onPooliza_CambiaFechaValor() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onPooliza_CambiaFechaValor();
        oContabilidadPolMan = null;
    }

    public void onPooliza_ValidaFechaValor() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onPooliza_ValidaFechaValor();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_DespliegaVentana() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_DespliegaVentana();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_DespliegaVentanaAdm() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_DespliegaVentanaAdm();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_CargaCampos() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_CargaCampos();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_IngresaCampo00() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_IngresaCampo00();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_Aplica() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_Aplica();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_Limpia() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_Limpia();
        oContabilidadPolMan = null;
    }

    public void onContabilidadPolMan_CargaPersonas() {
        oContabilidadPolMan = new contabilidadPolizaMan();
        oContabilidadPolMan.onContabilidadPolMan_CargaPersonas();
        oContabilidadPolMan = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C A N C E L A   S A L D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadCancelaSaldo_MonedaInvPlazo() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaInvPlazo();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaInvDirecto() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaInvDirecto();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaBienesGarantia() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaBienesGarantia();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaBienesFideicomitidos() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaBienesFideicomitidos();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaCtasCheques() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaCtasCheques();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaHonorarios() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaHonorarios();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_MonedaFianzas() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_MonedaFianzas();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_VerificaFiso() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_VerificaFiso();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_GeneraInformacion() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_GeneraInformacion();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_Limpia() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_Limpia();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_Continua() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_Continua();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_SeleccionaSaldo() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_SeleccionaSaldo();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_Aplica() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_Extigue();
        oContabilidadCancelaSld = null;
    }

    public void onContabilidadCancelaSaldo_CierraVentana() {
        oContabilidadCancelaSld = new contabilidadCancelaSaldos();
        oContabilidadCancelaSld.onContabilidadCancelaSaldo_CierraVentana();
        oContabilidadCancelaSld = null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * M E T O D O S   C A N C E L A   O P E R A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onContabilidadCancela_Cancela() {
        oContabilidadCancela = new contabilidadCancelaOperacion();
        oContabilidadCancela.onContabilidadCancela_Cancela();
        oContabilidadCancela = null;
    } 
      
    public void onContabilidadCancelaOper_Consulta() {
        oContabilidadCancela = new contabilidadCancelaOperacion();
        oContabilidadCancela.onContabilidadCancelaOper_Consulta();
        oContabilidadCancela = null;
    }

    public void onContabilidadCancelaOper_Limpia() {
        oContabilidadCancela = new contabilidadCancelaOperacion();
        oContabilidadCancela.onContabilidadCancelaOper_Limpia();
        oContabilidadCancela = null;
    }

    public void onContabilidadCancelaOper_Cancela() {
        oContabilidadCancela = new contabilidadCancelaOperacion();
        oContabilidadCancela.onContabilidadCancelaOper_Cancela();
        oContabilidadCancela = null;
    }

    public synchronized String formatFecha(Date fecha) {
        return formatoFecha.format(fecha);
    }

    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }

    public synchronized String formatFechaHora(Date fecha) {
        return formatoFechaHora.format(fecha);
    }

    public synchronized String formatHora(Date fecha) {
        return formatoHora.format(fecha);
    }

    public synchronized String formatDecimal2D(Double importe) {
        return formatoDecimal2D.format(importe);
    }

    public synchronized String formatDecimal6D(Double importe) {
        return formatoDecimal6D.format(importe);
    }

    public synchronized String formatImporte2D(Double importe) {
        return formatoImporte2D.format(importe);
    }

    public synchronized double validarDouble(String valor) {
        double resultado = 0.0;
        try {
            resultado = Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            logger.error("El numero proporcionado es invalido");
        }
        return resultado;
    }

    public synchronized String limpiaImporte2D(String valor) {
        String resultado = new String();
        valor = valor.replaceAll(",", "");
        valor = valor.replace("$", "");
        resultado = valor;
//        if (valor.contains(".")) {
//            Integer largo = valor.length();
//            if (largo >= valor.indexOf(".") + 3) {
//                largo = valor.indexOf(".") + 3;
//            }
//            resultado = (valor.substring(0, largo));
//        }
        return resultado;
    }

    private synchronized Boolean onVerificaAtencionContrato(long contratoNumero) {
        Boolean validacion = Boolean.FALSE;
        for (int itemFiso = 0; itemFiso <= usuarioFiltro.size() - 1; itemFiso++) {
            if (Long.parseLong(usuarioFiltro.get(itemFiso)) == contratoNumero) {
                validacion = Boolean.TRUE;
                break;
            }
        }
        return validacion;
    }

}