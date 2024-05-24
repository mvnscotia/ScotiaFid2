/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.xml.sax.SAXException;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.FechaBean;
import scotiaFid.bean.MonedasBean;
import scotiaFid.bean.MonitorChequeBean;
import scotiaFid.bean.NombreBean;
import scotiaFid.bean.RecepcionCuentaBean;
import scotiaFid.bean.ViewRecepcionCuentaBancoBean;
import scotiaFid.dao.CCRecepcionCuentasBanco;
import scotiaFid.dao.CComunes;
import scotiaFid.bean.ComplementoMonitorCheques;
import scotiaFid.bean.CtasSaldos;
import scotiaFid.bean.DepositBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.FidCtasBean;
import scotiaFid.bean.MonitorChequesBean;
import scotiaFid.bean.RequestMonitorBean;
import scotiaFid.bean.ResponseMonitorBean;
import scotiaFid.dao.CHonorarios;
import scotiaFid.dao.CMonitorMovimientos;
import scotiaFid.service.DepositTranInqByAcctId;
import scotiaFid.util.LogsContext;

/**
 *
 * @author lenovo
 */
@ManagedBean(name = "mbRecepcionCuentasBanco")
@ViewScoped
public class MBRecepcionCuentasBanco implements Serializable {

    private FacesMessage mensaje;
    private static SimpleDateFormat formatoFecha;
    private static final Logger logger = LogManager.getLogger(MBRecepcionCuentasBanco.class);

    private static final String CLAVE_RECEPCION_CUENTA = "75";
    private static final String CLAVE_FORMA_RECEPCION = "74";
    private static final String CLAVE_TIPO_PERSONA = "29";
    private static final String SP_FORMA_ORIGEN = "FrmR221";
    private static final Integer SP_ID_BANCO = 44;
    private static final String SP_NOMBRE_BANCO = "SCOTIABANK INVERLAT";
    private static DecimalFormat formatoDecimal2D;
    private FechaContableBean fechaContableBean;
    CHonorarios cHonorarios = new CHonorarios();

    Map<String, String> validaciones = null;

    private String fideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumContrato");
    private String subFideicomiso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsSubContrato");
    private String contrato = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideCtoNomContrato");
    private String folioInst = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instrucFideInsNumFolioInst");
    private Double importeRecepcion;
    private Double importeRecepcionIVA;
    private Double iva;
    private Double importeRecepcionTmp;
    private Double importeRecepcionIVATmp;
    private Double ivaTmp;
    private Boolean checkIva;
    private String Referencia;
    private String ReferenciaTmp;
    private String tcVisible;
    private int destinoRecepcionSelected;
    private int monedaSelected;
    private int formaRecepcionSelected;
    private int tipoPersonaSelected;
    private int bancoSelected;
    private int nombreSelected;
    private String folioContable;
    private String fechaAplicacion;
    private String tipoCambio;
    private FechaBean fechaContabilizacionRecepcion;
    private ComplementoMonitorCheques complementoMonitorCheque;
    private List<DestinoRecepcionBean> listDestinoRecepcion;
    private List<MonedasBean> listMoneda;
    private MonedasBean monSelecionada;
    private List<DestinoRecepcionBean> listFormaRecepcion;
    private List<DestinoRecepcionBean> listTipoPersona;
    private List<NombreBean> listNombre;
    //private MonitorChequeBean monitorCheque;
    private List<ViewRecepcionCuentaBancoBean> listViewCuentaBanco;
    private ViewRecepcionCuentaBancoBean viewCuentaBanco;
    private List<MonitorChequeBean> listMonitorCheque;
    CCRecepcionCuentasBanco ccRecepcionCuentaBanco = null;
    private Boolean scotiabankButton;

    /**
     * MONITOR DE CHEQUES
     */
    private MonitorChequesBean monitorChequesBean;
    private RequestMonitorBean requestMonitorBean;
    private ResponseMonitorBean tempResponseMonitorBean;
    private ResponseMonitorBean responseMonitorBean;
    private CtasSaldos ctasSaldosBean;
    private DepositBean depositBean;
    private List<FidCtasBean> cuentas = new ArrayList<>();
    private List<DepositBean> depositos = new ArrayList<>();
    private List<ResponseMonitorBean> responses = new ArrayList<>();
    private List<CtasSaldos> ctasSaldos = new ArrayList<>();
    CMonitorMovimientos cMonitorMovimientos = new CMonitorMovimientos();
    DepositTranInqByAcctId depositTranInqByAcctId = new DepositTranInqByAcctId();

    public MBRecepcionCuentasBanco() throws SQLException {
        LogsContext.FormatoNormativo();
        validaciones = new HashMap<>();
        CComunes ccomunes = new CComunes();
        ccRecepcionCuentaBanco = new CCRecepcionCuentasBanco();
        fechaContabilizacionRecepcion = ccomunes.getFechaRecepcionSistema();
        listDestinoRecepcion = ccomunes.getClavesPorNumClave(CLAVE_RECEPCION_CUENTA);
        listFormaRecepcion = ccomunes.getClavesPorNumClave(CLAVE_FORMA_RECEPCION);
        listTipoPersona = ccomunes.getClavesPorNumClave(CLAVE_TIPO_PERSONA);
        listMoneda = ccRecepcionCuentaBanco.getMonedas();
        //monitorCheque = new MonitorChequeBean();
        scotiabankButton = Boolean.FALSE;
        listViewCuentaBanco = ccRecepcionCuentaBanco.getCuentaRecepcion(fideicomiso, subFideicomiso);
        tcVisible = "hidden;";
        /**
         * MONITOR DE MOVIMIENTOS
         */
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);

    }

    @PostConstruct
    public void init() {
        try {
            this.monitorChequesBean = new MonitorChequesBean();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.now();

//            fideicomiso = "11026531";
            monitorChequesBean.setContrato(fideicomiso);
            monitorChequesBean.setMoneda(1);
            monedaSelected = 1;
            monitorChequesBean.setFecha(localDate.format(dateTimeFormatter));
            obtieneMonedaSeleccionada();
//            if (1 == monitorChequesBean.getMoneda()) {
//                monitorChequesBean.setNombreMoneda("Moneda Nacional");
//            } else {
//                monitorChequesBean.setNombreMoneda("Dólar Americano");
//            }

        } catch (AbstractMethodError Err) {
            logger.error(Err.getMessage());
        }
    }

    public void obtieneMonedaSeleccionada() {

        monSelecionada = listMoneda.stream()
                .filter(cuenta -> cuenta.getMonedaNumero().equals(monedaSelected))
                .findAny().orElse(null);
        monitorChequesBean.setMoneda(monedaSelected);
        monitorChequesBean.setNombreMoneda(monSelecionada.getMonedaNombre());
        selectTipoCambio();
    }

    public void selectTipoCambio() {
        //Write_Console
//        out.println("Select Tipo Cambio...");
        try {
            if (monSelecionada.getMonedaNumero() != 1) {
                //Get_Parameters
                fechaContableBean = cHonorarios.onHonorarios_GetFechaContable();
                int anio = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                int mes = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                int dia = fechaContableBean.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();

                //Get_Tipo_Cambio
                tipoCambio = cHonorarios.onHonorarios_GetTipoCambio(monSelecionada.getMonedaNumero(), anio, mes, dia);

                scotiabankButton = Boolean.FALSE;

                //Validate_Tipo_Cambio
                if ("".equals(tipoCambio)) {
                    //Set_Message
                    tcVisible = "hidden;";
                    scotiabankButton = Boolean.TRUE;
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No hay tipo de Cambio para el día de hoy."));
                    return;
                }

                //Show_Window_TipoCambio
                tcVisible = "visible;";
                RequestContext.getCurrentInstance().execute("dlgTipoCambio.show();");
            } else {
                tcVisible = "hidden;";
                scotiabankButton = Boolean.FALSE;
            }

        } catch (NumberFormatException Err) {
            logger.error(Err.getMessage());
        }
    }

    public void cierra_dlgTipoCambio() {
        RequestContext.getCurrentInstance().execute("dlgTipoCambio.hide();");
    }

    public void onMonetariasLiquidacion_Terminar() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instFolioInst", folioInst);
            FacesContext.getCurrentInstance().getExternalContext().redirect("instruccionesFideicomisos.sb?faces-redirect=true");
        } catch (IOException Err) {
            logger.error(Err.getMessage());
        }
    }

    public void getMonitorMovements() throws NumberFormatException, SQLException {
//        logger.info("Get Monitor Movements...");
        responses.clear();
        ctasSaldos.clear();
        requestMonitorBean = new RequestMonitorBean();
        cuentas = cMonitorMovimientos.onMonitorMovimientos_GetCuentasXContrato(Integer.parseInt(monitorChequesBean.getContrato()), monitorChequesBean.getMoneda());
        Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
        depositos = cMonitorMovimientos.onMonitorMovimientos_GetDepositos(Integer.parseInt(monitorChequesBean.getContrato()), monitorChequesBean.getMoneda(), fechSistema);
//        logger.info("No Count cuentas: ".concat(String.valueOf(cuentas.size())));

        for (FidCtasBean fidCtasBean : cuentas) {
//            logger.info("Plaza: ".concat(String.valueOf(String.format("%03d", fidCtasBean.getPlaza()))));
//            logger.info("Cuenta: ".concat(fidCtasBean.getCuenta()));
            mensaje = null;
            requestMonitorBean.setCuenta(String.format("%03d", fidCtasBean.getPlaza()).concat(fidCtasBean.getCuenta()));

            try {
                //Get_Movements

                ctasSaldosBean = new CtasSaldos();
                ctasSaldosBean = depositTranInqByAcctId.requestAcounts(requestMonitorBean);
                ctasSaldos.add(ctasSaldosBean);

                tempResponseMonitorBean = new ResponseMonitorBean();
                tempResponseMonitorBean = depositTranInqByAcctId.requestTransaction(requestMonitorBean);

                if (("Servicio Error".equals(depositTranInqByAcctId.getMensajeError()))) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Error en Servicio de movimientos del Día Intente Mas Tarde y/o Reporte a Soporte Técnico."));
                    return;
                }

                //Get_Responses
                List<ResponseMonitorBean> responsesPaso = tempResponseMonitorBean.getLstResponse();

                if (responsesPaso.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Sin Movimientos para el Fideicomiso ".concat(fideicomiso).concat(" número de Cuenta: ").concat(String.format("%03d", fidCtasBean.getPlaza()).concat(fidCtasBean.getCuenta()))));
                }
                if ("0".equals(tempResponseMonitorBean.getEstatus())) {
                    for (ResponseMonitorBean response : responsesPaso) {

                        for (DepositBean depositBean : depositos) {
//                            logger.info("depositBean.getConcepto() : " + depositBean.getConcepto());

                            if (response.getConfirmacion().equals(depositBean.getConcepto())) {
                                response.setEstatus("/vista/recursos/img/png/green_circle.png");
                                response.setAplicado("SI");
                            }
                        }
                        responses.add(response);
                    }
                }
            } catch (AbstractMethodError | IOException | ParserConfigurationException | SAXException e) {
                logger.error(e.getMessage());
            }
        }
        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Consulta Terminada."));
    }

    public void onInicializa_movto() {
        if (responseMonitorBean == null) {
            importeRecepcion = null;
            importeRecepcionIVA = null;
            iva = null;
        }
        responses.clear();
        ctasSaldos.clear();
        RequestContext.getCurrentInstance().execute("dlgMonitorMovimientos.show();");
    }

    public void onRowSelect(SelectEvent event) throws NumberFormatException, SQLException {
        tempResponseMonitorBean = new ResponseMonitorBean();

        this.tempResponseMonitorBean = (ResponseMonitorBean) event.getObject();

        complementoMonitorCheque = ccRecepcionCuentaBanco.getComplementoMonitorCheques(fideicomiso, subFideicomiso, tempResponseMonitorBean.getCuenta());

//        monitorCheque.setBancoNombre(complementoMonitorCheque.getBanco());
//        monitorCheque.setPlaza(complementoMonitorCheque.getPlaza());
//        monitorCheque.setMoneda(complementoMonitorCheque.getMoneda());
//        monitorCheque.setAdcNumMoneda(complementoMonitorCheque.getAdcNumMoneda());
//        monitorCheque.setCuenta(contrato);
        tempResponseMonitorBean.setBanco(complementoMonitorCheque.getBanco());
        tempResponseMonitorBean.setPlaza(complementoMonitorCheque.getPlaza());
        tempResponseMonitorBean.setAdcNumMoneda(complementoMonitorCheque.getAdcNumMoneda());
        tempResponseMonitorBean.setMoneda(complementoMonitorCheque.getMoneda());
        tempResponseMonitorBean.setTipoCambio(1.00);
        formaRecepcionSelected = 1;
        DestinoRecepcionBean formaRecepcionSeleccionado = listFormaRecepcion.stream()
                .filter(formaRecepcion -> formaRecepcion.getClaveNumero().equals(formaRecepcionSelected))
                .findAny().orElse(null);
        tempResponseMonitorBean.setTipoOperacionDesc(formaRecepcionSeleccionado.getClaveDescripcion());
        tempResponseMonitorBean.setRecepcion(fechaContabilizacionRecepcion.getFechaActDia().toString().concat("/").concat(fechaContabilizacionRecepcion.getFechaActMes().toString().concat("/").concat(fechaContabilizacionRecepcion.getFechaActAño().toString())));

        //responseMonitorBean.set(complementoMonitorCheque.getMoneda());
        if ("SI".equals(tempResponseMonitorBean.getAplicado())) //Show_Window_Aplicado
        {
            /* //Se quita limpieza de responses para que no marque NullPointerException
            responses.clear();
            responses.add(responseMonitorBean);
             */ //CAVC
            importeRecepcionTmp = Double.parseDouble(tempResponseMonitorBean.getMonto());
            importeRecepcionIVATmp = Double.parseDouble(tempResponseMonitorBean.getMonto());
            ReferenciaTmp = tempResponseMonitorBean.getReferencia();

            RequestContext.getCurrentInstance().execute("dlgAplicado.show();");
        } else {
            /* //Se quita limpieza de responses para que no marque NullPointerException
            responses.clear();
            responses.add(responseMonitorBean);
             */ //CAVC
            importeRecepcionTmp = Double.parseDouble(tempResponseMonitorBean.getMonto());
            importeRecepcionIVATmp = Double.parseDouble(tempResponseMonitorBean.getMonto());
            ReferenciaTmp = tempResponseMonitorBean.getReferencia();
            RequestContext.getCurrentInstance().execute("dlgCopiar.show();");
        }
    }

    public void SiSelectCopiarMovimiento() {
        responseMonitorBean = new ResponseMonitorBean();
        responseMonitorBean = tempResponseMonitorBean;
        importeRecepcion = Double.parseDouble(responseMonitorBean.getMonto());
        importeRecepcionIVA = Double.parseDouble(responseMonitorBean.getMonto());
        Referencia = responseMonitorBean.getReferencia();
        calculaIva();
        responses.clear();
        tempResponseMonitorBean = null;
    }

    public void cleanMonitorMovements() {
        responses.clear();
    }

    public void monedaChangeListener() {
        importeRecepcion = null;
        importeRecepcionIVA = null;
        iva = null;
        formaRecepcionSelected = 0;
        responses.clear();

        obtieneMonedaSeleccionada();
    }

    public void nombreChangeListener() throws SQLException {
//        fideicomiso = "123456"; //Eliminar
        listNombre = ccRecepcionCuentaBanco.getNombresRecepcionCuentas(fideicomiso, tipoPersonaSelected);
        if (listNombre.isEmpty()) {
            reiniciaValidacion(validaciones);
            validaciones.put("Fiduciario", "El tipo de persona seleccionado no contiene información");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
    }

    public void bancoChangeListener() {

        if (bancoSelected != 0) {
            viewCuentaBanco = new ViewRecepcionCuentaBancoBean();
            listViewCuentaBanco.stream().map(rowView -> {
                viewCuentaBanco.setSucursal(rowView.getSucursal());
                return rowView;
            }).map(rowView -> {
                viewCuentaBanco.setDescClave(rowView.getDescClave());
                return rowView;
            }).forEachOrdered(rowView -> {
                viewCuentaBanco.setNomMoneda(rowView.getNomMoneda());
            });
        } else {
            viewCuentaBanco.setSucursal(null);
            viewCuentaBanco.setDescClave(null);
            viewCuentaBanco.setNomMoneda(null);
        }

    }

//    public void cargaMonitorCheque() {
//        complementoMonitorCheque = ccRecepcionCuentaBanco.getComplementoMonitorCheques(fideicomiso, subFideicomiso, monitorCheque.getNumerocuenta());
//
//        listMonitorCheque = new ArrayList<>();
//        importeRecepcion = monitorCheque.getImporte();
//        importeRecepcionIVA = monitorCheque.getImporte();
//        Referencia = monitorCheque.getReferencia();
//        monitorCheque.setBancoNombre(complementoMonitorCheque.getBanco());
//        monitorCheque.setPlaza(complementoMonitorCheque.getPlaza());
//        monitorCheque.setMoneda(complementoMonitorCheque.getMoneda());
//        monitorCheque.setAdcNumMoneda(complementoMonitorCheque.getAdcNumMoneda());
//        listMonitorCheque.add(monitorCheque);
//    }
    public void calculaIva() {
        if (importeRecepcion != null) {
            CComunes ccomunes = new CComunes();
            if (checkIva == true) {
                iva = importeRecepcion - (importeRecepcion / (1 + ccomunes.onComunes_ObtenIva(fideicomiso)));
                importeRecepcionIVA = importeRecepcion - iva;
                responseMonitorBean.setIva(iva);
                responseMonitorBean.setMontoFormat(formatImportel2D(importeRecepcionIVA));
            } else if (checkIva == false) {
                iva = null;
                importeRecepcionIVA = importeRecepcion;
                responseMonitorBean.setMontoFormat(formatImportel2D(importeRecepcion));

                responseMonitorBean.setIva(null);
            }
        } else {
            checkIva = false;
            reiniciaValidacion(validaciones);
            validaciones.put("Fiduciario", "No hay importe de recepción desde el monitor de cheques");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }

    }

    public void addMessage(FacesMessage.Severity severity, Map<String, String> validaciones) {
        validaciones.entrySet().forEach(validacion -> {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, validacion.getKey(), validacion.getValue()));
        });
    }

    public void reiniciaValidacion(Map<String, String> validaciones) {
        if (!validaciones.isEmpty()) {
            validaciones.clear();
        }
    }

    public void guardaRecepcionCuenta() throws ParseException {
        reiniciaValidacion(validaciones);
        if (destinoRecepcionSelected <= 0) {
            validaciones.put("Fiduciario", "El campo Destino de la Recepción no puede estar vacío...");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (importeRecepcion == null) {
            validaciones.put("Fiduciario", "El campo Importe Recepción no puede estar vacío...");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (tipoPersonaSelected == 0) {
            validaciones.put("Fiduciario", "El campo Tipo de Persona no puede estar vacío...");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (nombreSelected == 0) {
            validaciones.put("Fiduciario", "El campo Nombre no puede estar vacío...");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (bancoSelected == 0) {
            validaciones.put("Fiduciario", "El campo Banco no puede estar vacío...");
            addMessage(FacesMessage.SEVERITY_INFO, validaciones);
        }
        if (validaciones.isEmpty()) {

            RecepcionCuentaBean recepcionBean = new RecepcionCuentaBean();
            recepcionBean.setiUsuario((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero"));
            recepcionBean.setsIdTerminal("ROB-PC");
            recepcionBean.setsFormaOrigen(SP_FORMA_ORIGEN);
            recepcionBean.setlNumContrato(Integer.parseInt(fideicomiso));
            recepcionBean.setlSubContrato(Integer.parseInt(subFideicomiso));

            DestinoRecepcionBean destinoRecepcionSeleccionado = listDestinoRecepcion.stream()
                    .filter(destinoRecepcion -> destinoRecepcion.getClaveNumero().equals(destinoRecepcionSelected))
                    .findAny().orElse(null);
            recepcionBean.setsDestinoDep(destinoRecepcionSeleccionado.getClaveDescripcion());

            DestinoRecepcionBean formaRecepcionSeleccionado = listFormaRecepcion.stream()
                    .filter(formaRecepcion -> formaRecepcion.getClaveNumero().equals(formaRecepcionSelected))
                    .findAny().orElse(null);
            recepcionBean.setsFormaDep(formaRecepcionSeleccionado.getClaveDescripcion());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, fechaContabilizacionRecepcion.getFechaActAño());
            cal.set(Calendar.MONTH, fechaContabilizacionRecepcion.getFechaActMes() - 1);
            cal.set(Calendar.DAY_OF_MONTH, fechaContabilizacionRecepcion.getFechaActDia());
            Date fechaOnFuncion = new Date(cal.getTimeInMillis());
            recepcionBean.setDtFechaRecep(fechaOnFuncion);
            DestinoRecepcionBean tipoPersonaSeleccionado = listTipoPersona.stream()
                    .filter(tipoPersona -> tipoPersona.getClaveNumero().equals(tipoPersonaSelected))
                    .findAny().orElse(null);
            recepcionBean.setsTipoPersona(tipoPersonaSeleccionado.getClaveDescripcion());
            NombreBean nombreSeleccionado = listNombre.stream()
                    .filter(nombre -> nombre.getBeneficiarioNumero().equals(nombreSelected))
                    .findAny().orElse(null);
            recepcionBean.setsNombrePers(nombreSeleccionado.getBeneficiarioNombre());
            recepcionBean.setiNumPersona(nombreSeleccionado.getBeneficiarioNumero());
            recepcionBean.setsCtaCheques(responseMonitorBean.getCuenta());
            recepcionBean.setsSucursal(String.valueOf(complementoMonitorCheque.getSucursal()));
            recepcionBean.setlPlaza(complementoMonitorCheque.getPlaza());
            recepcionBean.setiBanco(SP_ID_BANCO);
            recepcionBean.setsNomBanco(SP_NOMBRE_BANCO);
            recepcionBean.setiMoneda(complementoMonitorCheque.getAdcNumMoneda());
            recepcionBean.setdImporteDep(Double.parseDouble(responseMonitorBean.getMonto()));
            recepcionBean.setDtTipoCamb(1);
            recepcionBean.setDtIVA((checkIva) ? 1 : 0);
            recepcionBean.setsConcepto(responseMonitorBean.getConfirmacion());
            Map<String, String> responseSPProcesa = new HashMap<String, String>();
            responseSPProcesa = ccRecepcionCuentaBanco.onGuardaSPProcesaDepo(recepcionBean);
            if (!responseSPProcesa.isEmpty()) {
                for (Map.Entry<String, String> sp : responseSPProcesa.entrySet()) {
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("Error")) {
                        reiniciaValidacion(validaciones);
                        validaciones.put("ERROR INTERNO", responseSPProcesa.get("Error"));
                        addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
                    }
                    if (!sp.getKey().isEmpty() && sp.getKey().equals("folioContable")) {
                        fechaAplicacion = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        folioContable = sp.getValue();
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("frmRecepcionCuentasBanco:dlgSPExito");
                        context.execute("dlgSPExito.show();");
                        logger.error("Folio contable: " + folioContable);
                    }
                }
            } else {
                reiniciaValidacion(validaciones);
                validaciones.put("Fiduciario", "Ocurrio un error en Base de Datos intente mas tarde");
                addMessage(FacesMessage.SEVERITY_ERROR, validaciones);
            }

        }

    }

    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;

        try {
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }

    public synchronized String formatImportel2D(double importe) {
        formatoDecimal2D = new DecimalFormat("$###,###.##");
        formatoDecimal2D.setMaximumFractionDigits(2);
        formatoDecimal2D.setMinimumFractionDigits(2);

        return formatoDecimal2D.format(importe);
    }

    public void NoSelectCopiarMovimiento() {
        if (responseMonitorBean == null) {
            importeRecepcion = null;
            importeRecepcionIVA = null;
            iva = null;
        }
    }

    public int getTipoPersonaSelected() {
        return tipoPersonaSelected;
    }

    public void setTipoPersonaSelected(int tipoPersonaSelected) {
        this.tipoPersonaSelected = tipoPersonaSelected;
    }

    public String getFideicomiso() {
        return fideicomiso;
    }

    public void setFideicomiso(String fideicomiso) {
        this.fideicomiso = fideicomiso;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public Double getImporteRecepcion() {
        return importeRecepcion;
    }

    public void setImporteRecepcion(Double importeRecepcion) {
        this.importeRecepcion = importeRecepcion;
    }

    public FechaBean getFechaContabilizacionRecepcion() {
        return fechaContabilizacionRecepcion;
    }

    public void setFechaContabilizacionRecepcion(FechaBean fechaContabilizacionRecepcion) {
        this.fechaContabilizacionRecepcion = fechaContabilizacionRecepcion;
    }

    public List<DestinoRecepcionBean> getListDestinoRecepcion() {
        return listDestinoRecepcion;
    }

    public void setListDestinoRecepcion(List<DestinoRecepcionBean> listDestinoRecepcion) {
        this.listDestinoRecepcion = listDestinoRecepcion;
    }

    public List<MonedasBean> getListMoneda() {
        return listMoneda;
    }

    public void setListMoneda(List<MonedasBean> listMoneda) {
        this.listMoneda = listMoneda;
    }

    public List<DestinoRecepcionBean> getListFormaRecepcion() {
        return listFormaRecepcion;
    }

    public void setListFormaRecepcion(List<DestinoRecepcionBean> listFormaRecepcion) {
        this.listFormaRecepcion = listFormaRecepcion;
    }

    public List<DestinoRecepcionBean> getListTipoPersona() {
        return listTipoPersona;
    }

    public void setListTipoPersona(List<DestinoRecepcionBean> listTipoPersona) {
        this.listTipoPersona = listTipoPersona;
    }

    public List<NombreBean> getListNombre() {
        return listNombre;
    }

    public void setListNombre(List<NombreBean> listNombre) {
        this.listNombre = listNombre;
    }

//    public MonitorChequeBean getMonitorCheque() {
//        return monitorCheque;
//    }
//
//    public void setMonitorCheque(MonitorChequeBean monitorCheque) {
//        this.monitorCheque = monitorCheque;
//    }
    public List<ViewRecepcionCuentaBancoBean> getListViewCuentaBanco() {
        return listViewCuentaBanco;
    }

    public void setListViewCuentaBanco(List<ViewRecepcionCuentaBancoBean> listViewCuentaBanco) {
        this.listViewCuentaBanco = listViewCuentaBanco;
    }

    public List<MonitorChequeBean> getListMonitorCheque() {
        return listMonitorCheque;
    }

    public void setListMonitorCheque(List<MonitorChequeBean> listMonitorCheque) {
        this.listMonitorCheque = listMonitorCheque;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public String getSubFideicomiso() {
        return subFideicomiso;
    }

    public void setSubFideicomiso(String subFideicomiso) {
        this.subFideicomiso = subFideicomiso;
    }

    public Double getImporteRecepcionIVA() {
        return importeRecepcionIVA;
    }

    public void setImporteRecepcionIVA(Double importeRecepcionIVA) {
        this.importeRecepcionIVA = importeRecepcionIVA;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Boolean getCheckIva() {
        return checkIva;
    }

    public void setCheckIva(Boolean checkIva) {
        this.checkIva = checkIva;
    }

    public int getDestinoRecepcionSelected() {
        return destinoRecepcionSelected;
    }

    public void setDestinoRecepcionSelected(int destinoRecepcionSelected) {
        this.destinoRecepcionSelected = destinoRecepcionSelected;
    }

    public int getMonedaSelected() {
        return monedaSelected;
    }

    public void setMonedaSelected(int monedaSelected) {
        this.monedaSelected = monedaSelected;
    }

    public int getFormaRecepcionSelected() {
        return formaRecepcionSelected;
    }

    public void setFormaRecepcionSelected(int formaRecepcionSelected) {
        this.formaRecepcionSelected = formaRecepcionSelected;
    }

    public int getBancoSelected() {
        return bancoSelected;
    }

    public void setBancoSelected(int bancoSelected) {
        this.bancoSelected = bancoSelected;
    }

    public ViewRecepcionCuentaBancoBean getViewCuentaBanco() {
        return viewCuentaBanco;
    }

    public void setViewCuentaBanco(ViewRecepcionCuentaBancoBean viewCuentaBanco) {
        this.viewCuentaBanco = viewCuentaBanco;
    }

    public int getNombreSelected() {
        return nombreSelected;
    }

    public void setNombreSelected(int nombreSelected) {
        this.nombreSelected = nombreSelected;
    }

    public String getFolioContable() {
        return folioContable;
    }

    public void setFolioContable(String folioContable) {
        this.folioContable = folioContable;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public ComplementoMonitorCheques getComplementoMonitorCheque() {
        return complementoMonitorCheque;
    }

    public void setComplementoMonitorCheque(ComplementoMonitorCheques complementoMonitorCheque) {
        this.complementoMonitorCheque = complementoMonitorCheque;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * G E T T E R S   Y   S E T T E R S MONITOR DE MOVIMIENTOS
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MonitorChequesBean getMonitorChequesBean() {
        return monitorChequesBean;
    }

    public void setMonitorChequesBean(MonitorChequesBean monitorChequesBean) {
        this.monitorChequesBean = monitorChequesBean;
    }

    public List<FidCtasBean> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<FidCtasBean> cuentas) {
        this.cuentas = cuentas;
    }

    public List<DepositBean> getDepositos() {
        return depositos;
    }

    public void setDepositos(List<DepositBean> depositos) {
        this.depositos = depositos;
    }

    public RequestMonitorBean getRequestMonitorBean() {
        return requestMonitorBean;
    }

    public void setRequestMonitorBean(RequestMonitorBean requestMonitorBean) {
        this.requestMonitorBean = requestMonitorBean;
    }

    public ResponseMonitorBean getResponseMonitorBean() {
        return responseMonitorBean;
    }

    public void setResponseMonitorBean(ResponseMonitorBean responseMonitorBean) {
        this.responseMonitorBean = responseMonitorBean;
    }

    public List<ResponseMonitorBean> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseMonitorBean> responses) {
        this.responses = responses;
    }

    public DepositBean getDepositBean() {
        return depositBean;
    }

    public void setDepositBean(DepositBean depositBean) {
        this.depositBean = depositBean;
    }

    public String getFolioInst() {
        return folioInst;
    }

    public void setFolioInst(String folioInst) {
        this.folioInst = folioInst;
    }

    public MonedasBean getMonSelecionada() {
        return monSelecionada;
    }

    public void setMonSelecionada(MonedasBean monSelecionada) {
        this.monSelecionada = monSelecionada;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Boolean getScotiabankButton() {
        return scotiabankButton;
    }

    public void setScotiabankButton(Boolean scotiabankButton) {
        this.scotiabankButton = scotiabankButton;
    }

    public String getTcVisible() {
        return tcVisible;
    }

    public void setTcVisible(String tcVisible) {
        this.tcVisible = tcVisible;
    }

    public List<CtasSaldos> getCtasSaldos() {
        return ctasSaldos;
    }

    public void setCtasSaldos(List<CtasSaldos> ctasSaldos) {
        this.ctasSaldos = ctasSaldos;
    }

    public Double getImporteRecepcionTmp() {
        return importeRecepcionTmp;
    }

    public void setImporteRecepcionTmp(Double importeRecepcionTmp) {
        this.importeRecepcionTmp = importeRecepcionTmp;
    }

    public Double getImporteRecepcionIVATmp() {
        return importeRecepcionIVATmp;
    }

    public void setImporteRecepcionIVATmp(Double importeRecepcionIVATmp) {
        this.importeRecepcionIVATmp = importeRecepcionIVATmp;
    }

    public Double getIvaTmp() {
        return ivaTmp;
    }

    public void setIvaTmp(Double ivaTmp) {
        this.ivaTmp = ivaTmp;
    }

    public String getReferenciaTmp() {
        return ReferenciaTmp;
    }

    public void setReferenciaTmp(String ReferenciaTmp) {
        this.ReferenciaTmp = ReferenciaTmp;
    }
}
