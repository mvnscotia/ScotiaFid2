/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : CFDI 4.0
 * ARCHIVO     : MBDireccionesFiscales.java
 * TIPO        : Class
 * MODIFICADO  : 20240315 VJN
 * PAQUETE     : scotiaFid.controller
 * NOTAS       : Separación de Nombre Legal y Fiscal 20240315 VJN
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

//Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.io.IOException;
import java.io.Serializable;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import scotiaFid.bean.DirecciFBean;
import scotiaFid.bean.DirecciFMantenimientoBean;
import scotiaFid.bean.DirecciBean;
import scotiaFid.dao.CCFDI;
import scotiaFid.dao.CHonorarios;
import scotiaFid.util.LogsContext;
import scotiaFid.bean.CargaInterfazBean;
import scotiaFid.bean.OutParameterBean;

//Class  
@Named("mbDireccionesFiscales")
@ViewScoped
public class MBDireccionesFiscales implements Serializable {

    private static final Logger logger = LogManager.getLogger(MBDireccionesFiscales.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R I A L
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * B E A N S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    //-----------------------------------------------------------------------------
    private DirecciFBean direcciFBean = new DirecciFBean();
    //----------------------------------------------------------------------------- 
    private DirecciFMantenimientoBean Backup_direcciF = new DirecciFMantenimientoBean();
    // -----------------------------------------------------------------------------
    private DirecciBean direcciBean = new DirecciBean();
    // -----------------------------------------------------------------------------
    private OutParameterBean outParameterBean = new OutParameterBean();
    // -----------------------------------------------------------------------------
    private CargaInterfazBean cargaInterfazBean = new CargaInterfazBean();
    // -----------------------------------------------------------------------------
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
   * O B J E T O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // ----------------------------------------------------------------------------- 
    private List<DirecciFBean> direcciones = new ArrayList<>();
    // ----------------------------------------------------------------------------- 
    private List<DirecciBean> dirParticipantes = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private Map<Integer, String> mPais = new LinkedHashMap<Integer, String>();
    // -----------------------------------------------------------------------------
    private Map<Integer, String> mEstados = new LinkedHashMap<Integer, String>();
    // -----------------------------------------------------------------------------
    private Map<String, String> mRegFisc = new LinkedHashMap<String, String>();
    // -----------------------------------------------------------------------------
    private Map<Integer, Integer> mRegimen = new LinkedHashMap<Integer, Integer>();
    // -----------------------------------------------------------------------------
    private List<CargaInterfazBean> cargaInterfaz = new ArrayList<>();
    // -----------------------------------------------------------------------------
    private DirecciFBean selectDirecci;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * A T R I B U T O S   P R I V A D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // -----------------------------------------------------------------------------
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private final String nombreObjeto = "\nFuente: scotiafid.controller.MBDireccionesFiscales";

    //CRITERIOS DE CONSULTA 
    // -----------------------------------------------------------------------------
    private String sFideicomiso;
    // -----------------------------------------------------------------------------
    private String sTipo_Participante;
    // -----------------------------------------------------------------------------
    private boolean bConsulta_Disable;
    // -----------------------------------------------------------------------------
    private boolean bOpciones_Disable;
    // -----------------------------------------------------------------------------
    private boolean bConfirmar_Disable;
    // -----------------------------------------------------------------------------
    private boolean bEstatus_Disable;
    // -----------------------------------------------------------------------------
    private boolean bMantenimiento;
    // -----------------------------------------------------------------------------
    private boolean bObligatorios;
    // -----------------------------------------------------------------------------
    private boolean bRegCapital;
    // -----------------------------------------------------------------------------
    private boolean bCargaCSF;
    // -----------------------------------------------------------------------------
    private boolean bBtnManto;
    // -----------------------------------------------------------------------------
    private boolean bBtnRegimen;
    // -----------------------------------------------------------------------------
    private String sTipoMantenimiento;
    // -----------------------------------------------------------------------------
    private int iRegistros = 0;
    // -----------------------------------------------------------------------------
    private double dCorrectos = 0;
    // -----------------------------------------------------------------------------
    private int iIncorrectos = 0;
    // -----------------------------------------------------------------------------
    private String sNomArchivo;
    // -----------------------------------------------------------------------------
    private UploadedFile file;
    // -----------------------------------------------------------------------------
    private boolean bDirecci;
    // -----------------------------------------------------------------------------
    private String sRegCapital;
    // -----------------------------------------------------------------------------
    private String archivoNombre;
    // -----------------------------------------------------------------------------
    private String archivoUbicacion;
    // -----------------------------------------------------------------------------
    private String destinoURL;
    // -----------------------------------------------------------------------------
    private File archivo;
    // -----------------------------------------------------------------------------
    private FileOutputStream archivoSalida;
    // -----------------------------------------------------------------------------
    private byte[] buffer;
    // -----------------------------------------------------------------------------
    private String archivoLinea = new String();
    // -----------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R V I C I O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private CCFDI CCFDI = new CCFDI();
    // -----------------------------------------------------------------------------
    CHonorarios cHonorarios = new CHonorarios();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * G E T T E R S   Y   S E T T E R S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<DirecciFBean> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<DirecciFBean> direcciones) {
        this.direcciones = direcciones;
    }

    public DirecciFBean getDirecciFBean() {
        return direcciFBean;
    }

    public void setDirecciFBean(DirecciFBean direcciFBean) {
        this.direcciFBean = direcciFBean;
    }

    public String getsFideicomiso() {
        return sFideicomiso;
    }

    public void setsFideicomiso(String sFideicomiso) {
        this.sFideicomiso = sFideicomiso;
    }

    public String getsTipo_Participante() {
        return sTipo_Participante;
    }

    public void setsTipo_Participante(String sTipo_Participante) {
        this.sTipo_Participante = sTipo_Participante;
    }

    public List<DirecciBean> getDirParticipantes() {
        return dirParticipantes;
    }

    public void setDirParticipantes(List<DirecciBean> dirParticipantes) {
        this.dirParticipantes = dirParticipantes;
    }

    public boolean isbConsulta_Disable() {
        return bConsulta_Disable;
    }

    public void setbConsulta_Disable(boolean bConsulta_Disable) {
        this.bConsulta_Disable = bConsulta_Disable;
    }

    public boolean isbOpciones_Disable() {
        return bOpciones_Disable;
    }

    public void setbOpciones_Disable(boolean bOpciones_Disable) {
        this.bOpciones_Disable = bOpciones_Disable;
    }

    public boolean isbMantenimiento() {
        return bMantenimiento;
    }

    public void setbMantenimiento(boolean bMantenimiento) {
        this.bMantenimiento = bMantenimiento;
    }

    public DirecciBean getDirecciBean() {
        return direcciBean;
    }

    public void setDirecciBean(DirecciBean direcciBean) {
        this.direcciBean = direcciBean;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    //_Carga_Archivo_Masivo	 
    public int getiRegistros() {
        return iRegistros;
    }

    public void setiRegistros(int iRegistros) {
        this.iRegistros = iRegistros;
    }

    public double getdCorrectos() {
        return dCorrectos;
    }

    public void setdCorrectos(double dCorrectos) {
        this.dCorrectos = dCorrectos;
    }

    public int getiIncorrectos() {
        return iIncorrectos;
    }

    public void setiIncorrectos(int iIncorrectos) {
        this.iIncorrectos = iIncorrectos;
    }

    public String getsNomArchivo() {
        return sNomArchivo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setsNomArchivo(String sNomArchivo) {
        this.sNomArchivo = sNomArchivo;
    }

    public CargaInterfazBean getCargaInterfazBean() {
        return cargaInterfazBean;
    }

    public void setCargaInterfazBean(CargaInterfazBean cargaInterfazBean) {
        this.cargaInterfazBean = cargaInterfazBean;
    }

    public List<CargaInterfazBean> getCargaInterfaz() {
        return cargaInterfaz;
    }

    public void setCargaInterfaz(List<CargaInterfazBean> cargaInterfaz) {
        this.cargaInterfaz = cargaInterfaz;
    }

    public boolean isbObligatorios() {
        return bObligatorios;
    }

    public void setbObligatorios(boolean bObligatorios) {
        this.bObligatorios = bObligatorios;
    }

    public boolean isbRegCapital() {
        return bRegCapital;
    }

    public void setbRegCapital(boolean bRegCapital) {
        this.bRegCapital = bRegCapital;
    }

    public boolean isbConfirmar_Disable() {
        return bConfirmar_Disable;
    }

    public void setbConfirmar_Disable(boolean bConfirmar_Disable) {
        this.bConfirmar_Disable = bConfirmar_Disable;
    }

    public String getsTipoMantenimiento() {
        return sTipoMantenimiento;
    }

    public void setsTipoMantenimiento(String sTipoMantenimiento) {
        this.sTipoMantenimiento = sTipoMantenimiento;
    }

    public Map<String, String> getmRegFisc() {
        return mRegFisc;
    }

    public void setmRegFisc(Map<String, String> mRegFisc) {
        this.mRegFisc = mRegFisc;
    }

    public Map<Integer, Integer> getmRegimen() {
        return mRegimen;
    }

    public void setmRegimen(Map<Integer, Integer> mRegimen) {
        this.mRegimen = mRegimen;
    }

    public Map<Integer, String> getmPais() {
        return mPais;
    }

    public void setmPais(Map<Integer, String> mPais) {
        this.mPais = mPais;
    }

    public Map<Integer, String> getmEstados() {
        return mEstados;
    }

    public void setmEstados(Map<Integer, String> mEstados) {
        this.mEstados = mEstados;
    }

    public String getsRegCapital() {
        return sRegCapital;
    }

    public void setsRegCapital(String sRegCapital) {
        this.sRegCapital = sRegCapital;
    }

    public boolean isbDirecci() {
        return bDirecci;
    }

    public void setbDirecci(boolean bDirecci) {
        this.bDirecci = bDirecci;
    }

    public DirecciFMantenimientoBean getBackup_direcciF() {
        return Backup_direcciF;
    }

    public void setBackup_direcciF(DirecciFMantenimientoBean backup_direcciF) {
        Backup_direcciF = backup_direcciF;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    public String getArchivoUbicacion() {
        return archivoUbicacion;
    }

    public void setArchivoUbicacion(String archivoUbicacion) {
        this.archivoUbicacion = archivoUbicacion;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public FileOutputStream getArchivoSalida() {
        return archivoSalida;
    }

    public void setArchivoSalida(FileOutputStream archivoSalida) {
        this.archivoSalida = archivoSalida;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public String getArchivoLinea() {
        return archivoLinea;
    }

    public void setArchivoLinea(String archivoLinea) {
        this.archivoLinea = archivoLinea;
    }

    public boolean isbEstatus_Disable() {
        return bEstatus_Disable;
    }

    public void setbEstatus_Disable(boolean bEstatus_Disable) {
        this.bEstatus_Disable = bEstatus_Disable;
    }

    public boolean isbCargaCSF() {
        return bCargaCSF;
    }

    public void setbCargaCSF(boolean bCargaCSF) {
        this.bCargaCSF = bCargaCSF;
    }

    public boolean isbBtnManto() {
        return bBtnManto;
    }

    public void setbBtnManto(boolean bBtnManto) {
        this.bBtnManto = bBtnManto;
    }

    public boolean isbBtnRegimen() {
        return bBtnRegimen;
    }

    public void setbBtnRegimen(boolean bBtnRegimen) {
        this.bBtnRegimen = bBtnRegimen;
    }

    public DirecciFBean getSelectDirecci() {
        return selectDirecci;
    }

    public void setSelectDirecci(DirecciFBean selectDirecci) {
        this.selectDirecci = selectDirecci;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
   * C O N S T R U C T O R
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBDireccionesFiscales() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * M E T O D O S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @PostConstruct
    public void init() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {
                //_Inicializa variables
                mensajeError += "Error En Tiempo de Ejecución.\n";

                cleanDirecciones();

                //Catalogo_Regimen_Fiscal
                mRegFisc = CCFDI.onCFDI_getRegimenes(151);

                //_Paises
                mPais = CCFDI.onCFDI_getPaises();
            }

        } catch (IOException | NumberFormatException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
        }
    }

    public void Aceptar_click() {

        bBtnManto = false;

        if ((sFideicomiso != null && !sFideicomiso.equals(""))
                || (sTipo_Participante != null && !sTipo_Participante.equals(""))) {

            if (!sTipoMantenimiento.equals("Alta") && !sTipoMantenimiento.equals("Modificar")) {
                direcciFBean = new DirecciFBean();
            }

            direcciones = CCFDI.onCCFDI_Consulta_DirFis(sFideicomiso, sTipo_Participante);

            if (direcciones.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fiduciario", "No existen direcciones fiscales con los criterios de búsqueda especificados..."));
                RequestContext.getCurrentInstance().execute("dlgAltaDir.show();");
            }
        }
    }

    public void onRowSelect_Direcciones(SelectEvent event) {

        bBtnManto = false;

        this.direcciFBean = (DirecciFBean) event.getObject();

        bBtnManto = true;
        bCargaCSF = false;
        bBtnRegimen = false;
        bOpciones_Disable = true;

        //_Setter_para_respaldar_valores_originales  
        Backup_direcciF = new DirecciFMantenimientoBean();
        Backup_direcciF.setDif_num_contrato(direcciFBean.getDif_num_contrato());
        Backup_direcciF.setDif_cve_pers_fid(direcciFBean.getDif_cve_pers_fid());
        Backup_direcciF.setDif_num_pers_fid(direcciFBean.getDif_num_pers_fid());
        Backup_direcciF.setDif_recep_calle(direcciFBean.getDif_recep_calle());
        Backup_direcciF.setDif_recep_no_int(direcciFBean.getDif_recep_no_int());
        Backup_direcciF.setDif_recep_no_ext(direcciFBean.getDif_recep_no_ext());
        Backup_direcciF.setDif_recep_colonia(direcciFBean.getDif_recep_colonia());
        Backup_direcciF.setDif_nom_legal(direcciFBean.getDif_nom_legal());
        Backup_direcciF.setDif_recep_localidad(direcciFBean.getDif_recep_localidad());
        Backup_direcciF.setDif_num_pais(direcciFBean.getDif_num_pais());
        Backup_direcciF.setDif_num_estado(direcciFBean.getDif_num_estado());
        Backup_direcciF.setDif_nom_estado(direcciFBean.getDif_nom_estado());
        Backup_direcciF.setDif_recep_cp(direcciFBean.getDif_recep_cp());
        Backup_direcciF.setDif_telefono(direcciFBean.getDif_telefono());
        Backup_direcciF.setDif_mail(direcciFBean.getDif_mail());
        Backup_direcciF.setDif_regimen_fiscal(direcciFBean.getDif_regimen_fiscal());
        Backup_direcciF.setDif_recep_referencia(direcciFBean.getDif_recep_referencia());
        //_Carga_Estados
        mEstados = CCFDI.onCFDI_getEstados(direcciFBean.getDif_num_pais());
        sTipoMantenimiento = "";

        Consulta_NombreParticipante();

        if ((direcciFBean.getDif_nom_pers() != null && !"".equals(direcciFBean.getDif_nom_pers()))
                && (direcciFBean.getDif_rfc() != null && !"".equals(direcciFBean.getDif_rfc()))) {
            Backup_direcciF.setDif_nom_pers(direcciFBean.getDif_nom_pers());
            Backup_direcciF.setDif_rfc(direcciFBean.getDif_rfc());
        }
    }

    public void Confirmar_NuevaAlta() {
        String stFiso = sFideicomiso;
        cleanDirecciones();
        direcciFBean.setDif_num_contrato(stFiso);
        Backup_direcciF.setDif_num_contrato(stFiso);
        Manto_Direcci();
        Alta_Direccion();
        sFideicomiso = stFiso;
    }

    public void cleanDirecciones() {

        //Clean_Bean 
        sFideicomiso = "";
        sTipo_Participante = "";
        sTipoMantenimiento = "";

        sRegCapital = "";
        bRegCapital = false;

        mEstados = null;

        //Botones_de_Opciones:_Mantenimiento,_Importar... 
        bBtnManto = false;
        bCargaCSF = false;
        bBtnRegimen = false;
        bEstatus_Disable = false;
        bOpciones_Disable = false;
        bMantenimiento = false;
        bObligatorios = false;
        bDirecci = false;
        bConsulta_Disable = false;

        direcciones = new ArrayList<>();
        direcciFBean = new DirecciFBean();
        Backup_direcciF = new DirecciFMantenimientoBean();
        dirParticipantes = new ArrayList<>();
    }

    public void Manto_Direcci() {
        bBtnManto = false;

        Consulta_NomFideicomiso();
        Consulta_NombreParticipante();

        bCargaCSF = false;
        bObligatorios = false;
        bOpciones_Disable = true;
        bConfirmar_Disable = false;
        RequestContext.getCurrentInstance().execute("dlgDirFiscal.show();");
    }

    public void Cerrar_Manto_Direcci() {
        bDirecci = false;
        bBtnManto = true;
        bOpciones_Disable = true;
        bConfirmar_Disable = false;
        RequestContext.getCurrentInstance().execute("dlgDirFiscal.hide();");
    }

    public void Alta_Direccion() {

        sTipoMantenimiento = "Alta";

        String sFiso = direcciFBean.getDif_num_contrato();
        String sNomFiso = direcciFBean.getDif_nom_cto();
        bObligatorios = true;
        bMantenimiento = true;
        bConfirmar_Disable = true;
        bOpciones_Disable = false;
        direcciFBean = new DirecciFBean();
        direcciFBean.setDif_num_contrato(sFiso);
        direcciFBean.setDif_nom_cto(sNomFiso);
    }

    public void Modificar_Direccion() {
        sTipoMantenimiento = "Modificar";

        if (!CCFDI.onCCFDI_Consulta_Participante(direcciFBean)) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Fiduciario", "El participante no existe o está cancelado..."));
            return;
        }

        bCargaCSF = true;
        bMantenimiento = true;
        bObligatorios = false;
        bConfirmar_Disable = true;
        bOpciones_Disable = false;
    }

    public void Eliminar_Direccion() {
        sTipoMantenimiento = "Baja";

        RequestContext.getCurrentInstance().execute("dlgEliminaDir.show();");
        bMantenimiento = false;
        bObligatorios = false;
        bConfirmar_Disable = false;
    }

    public void refresh() {
        Manto_Direcci();
        Aceptar_click();

        sRegCapital = "";
        bRegCapital = false;

        bDirecci = false;
        bCargaCSF = false;
        bBtnRegimen = false;
        bObligatorios = false;
        bMantenimiento = false;
        dirParticipantes = new ArrayList<>();
    }

    public void CancelaManto() {
        sRegCapital = "";
        bDirecci = false;
        bCargaCSF = false;
        bBtnRegimen = false;
        bRegCapital = false;
        bObligatorios = false;
        bMantenimiento = false;
        bOpciones_Disable = true;
        bConfirmar_Disable = false;
        dirParticipantes = new ArrayList<>();

        if (Backup_direcciF.getDif_num_contrato() != null || !"".equals(Backup_direcciF.getDif_num_contrato())) {
            direcciFBean = new DirecciFBean();
            direcciFBean.setDif_num_contrato(Backup_direcciF.getDif_num_contrato());

            direcciFBean.setDif_nom_cto(Backup_direcciF.getDif_nom_cto());
            direcciFBean.setDif_nom_pers(Backup_direcciF.getDif_nom_pers());
            direcciFBean.setDif_rfc(Backup_direcciF.getDif_rfc());
            direcciFBean.setDif_cve_pers_fid(Backup_direcciF.getDif_cve_pers_fid());
            direcciFBean.setDif_num_pers_fid(Backup_direcciF.getDif_num_pers_fid());
            direcciFBean.setDif_recep_calle(Backup_direcciF.getDif_recep_calle());
            direcciFBean.setDif_recep_no_int(Backup_direcciF.getDif_recep_no_int());
            direcciFBean.setDif_recep_no_ext(Backup_direcciF.getDif_recep_no_ext());
            direcciFBean.setDif_recep_colonia(Backup_direcciF.getDif_recep_colonia());
            direcciFBean.setDif_nom_legal(Backup_direcciF.getDif_nom_legal());
            direcciFBean.setDif_recep_localidad(Backup_direcciF.getDif_recep_localidad());
            direcciFBean.setDif_num_pais(Backup_direcciF.getDif_num_pais());
            direcciFBean.setDif_num_estado(Backup_direcciF.getDif_num_estado());
            direcciFBean.setDif_nom_estado(Backup_direcciF.getDif_nom_estado());
            direcciFBean.setDif_recep_cp(Backup_direcciF.getDif_recep_cp());
            direcciFBean.setDif_telefono(Backup_direcciF.getDif_telefono());
            direcciFBean.setDif_mail(Backup_direcciF.getDif_mail());
            direcciFBean.setDif_regimen_fiscal(Backup_direcciF.getDif_regimen_fiscal());
            direcciFBean.setDif_recep_referencia(Backup_direcciF.getDif_recep_referencia());

        } else {
            if (sTipoMantenimiento.equals("Alta") || sTipoMantenimiento.equals("Modificar")) {
                direcciFBean = new DirecciFBean();
            }
        }

        //_Recupera
        select_Pais();
        select_Estado();

        Aceptar_click();

    }

    public void Mantenimiento_DirFiscal() {
        switch (sTipoMantenimiento) {
            case "Alta":
                //_Valida_los_datos_obligatorios_de_pantalla
                if (!ValidaDatos()) {
                    return;
                }

                if (CCFDI.onCFDI_InsertDirecci(direcciFBean)) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Alta Efectuada."));

                    //_Actualiza_RFC_PArticipante CIF
                    if (direcciFBean.getDif_rfc() != null && !"".equals(direcciFBean.getDif_rfc())) {
                        Actualiza_Participantes();
                    }

                    refresh();
                    Aceptar_click();
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Alta No Efectuada."));
                }

                break;
            case "Baja":
                if (CCFDI.onCFDI_validaEliminaDir(direcciFBean) > 0) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Esta dirección no se puede dar de baja, el participante ya ha facturado..."));
                } else {
                    if (CCFDI.onCFDI_EliminaDirecci(direcciFBean)) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Baja Efectuada."));
                        refresh();

                        //_Limpiar_el respaldo del registro que fue eliminado
                        Backup_direcciF = null;

                        RequestContext.getCurrentInstance().execute("dlgDirFiscal.hide();");
                        Aceptar_click();
                    }
                }
                break;
            case "Modificar":
                if (!CCFDI.onCCFDI_Consulta_Participante(direcciFBean)) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fiduciario", "El participante no existe o está cancelado..."));
                    return;
                }

                //Valida los datos obligatorios de pantalla
                if (!ValidaDatos()) {
                    return;
                }

                if (CCFDI.onCFDI_ModificarDirecci(direcciFBean)) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Modificación Efectuada."));

                    //Actualiza RFC del PArticipante CIF
                    if (direcciFBean.getDif_rfc() != null && !"".equals(direcciFBean.getDif_rfc())) {
                        Actualiza_Participantes();
                    }

                    refresh();
                    //Aceptar_click();
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Modificación No Efectuada."));
                }

                break;
        }
    }

    private void Actualiza_Participantes() {
        if (CCFDI.onCFDI_ActualizaRFC_Part(direcciFBean)) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Actualización de datos del Participante Efectuada."));
            refresh();
            return;
        } else {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Actualización de datos del Participante No Efectuada."));
        }
    }

    private boolean ValidaDatos() {

        boolean bexisteDir = valida_Dir_Existente();

        if (sTipoMantenimiento.equals("Alta") && bexisteDir == true) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Esta dirección ya existe..."));
            return false;
        }

        if ((sTipoMantenimiento.equals("Baja") || sTipoMantenimiento.equals("Modificar")) && bexisteDir == false) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Esta dirección no existe..."));
            return false;
        }

        //Nom. Legal
        if ((direcciFBean.getDif_rfc().length() == 12) && (direcciFBean.getDif_nom_legal() == null || "".equals(direcciFBean.getDif_nom_legal()))) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Nombre Legal no puede estar vacío para personas Morales..."));
            return false;
        }

        if (direcciFBean.getDif_nom_legal() != null || !"".equals(direcciFBean.getDif_nom_legal())) {
            direcciFBean.setDif_nom_legal(direcciFBean.getDif_nom_legal().toUpperCase());
        }

        //Calle
        if (direcciFBean.getDif_recep_calle() == null || "".equals(direcciFBean.getDif_recep_calle())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Calle no puede estar vacía..."));
            return false;
        } else {
            direcciFBean.setDif_recep_calle(direcciFBean.getDif_recep_calle().toUpperCase());
        }

        //CP
        if (direcciFBean.getDif_recep_cp() == null || "".equals(direcciFBean.getDif_recep_cp())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El C.P. no puede estar vacío..."));
            return false;
        }

        //Localidad
        if (direcciFBean.getDif_recep_localidad() == null || "".equals(direcciFBean.getDif_recep_localidad())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La Localidad no puede estar vacía..."));
            return false;
        } else {
            direcciFBean.setDif_recep_localidad(direcciFBean.getDif_recep_localidad().toUpperCase());
        }

        if (direcciFBean.getDif_num_pais() == 0) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un país..."));
            return false;
        }

        //Estado
        if (direcciFBean.getDif_num_estado() == 0) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un estado..."));
            return false;
        }

        if (direcciFBean.getDif_regimen_fiscal() == null || "".equals(direcciFBean.getDif_regimen_fiscal())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar y validar el Régimen Fiscal..."));
            return false;
        } else {
            direcciFBean.setDif_regimen_fiscal(direcciFBean.getDif_regimen_fiscal().toUpperCase());
        }

        //Valida_Regimen   
        //Validaciones_Persona_Fisica,_Moral_VS_Regimen_Fiscal   
        if (!Valida_TipoPers()) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Régimen fiscal, no válido para el tipo de Persona..."));
            direcciFBean.setDif_desc_regimen_fiscal("");
            direcciFBean.setDif_regimen_fiscal("");
            return false;
        }

        //Valida que no sean nulos para pasar a mayusculas.      	
        if (direcciFBean.getDif_recep_no_ext() != null && !"".equals(direcciFBean.getDif_recep_no_ext())) {
            direcciFBean.setDif_recep_no_ext(direcciFBean.getDif_recep_no_ext().toUpperCase());
        }

        if (direcciFBean.getDif_recep_no_int() != null && !"".equals(direcciFBean.getDif_recep_no_int())) {
            direcciFBean.setDif_recep_no_int(direcciFBean.getDif_recep_no_int().toUpperCase());
        }

        if (direcciFBean.getDif_recep_colonia() != null && !"".equals(direcciFBean.getDif_recep_colonia())) {
            direcciFBean.setDif_recep_colonia(direcciFBean.getDif_recep_colonia().toUpperCase());
        }

        if (direcciFBean.getDif_recep_referencia() != null && !"".equals(direcciFBean.getDif_recep_referencia())) {
            direcciFBean.setDif_recep_referencia(direcciFBean.getDif_recep_referencia().toUpperCase());
        }

        if (direcciFBean.getDif_mail() != null && !"".equals(direcciFBean.getDif_mail())) {
            direcciFBean.setDif_mail(direcciFBean.getDif_mail().toUpperCase());
        }
        return true;
    }

    public boolean valida_Dir_Existente() {

        if (direcciFBean.getDif_num_contrato() == null || "".equals(direcciFBean.getDif_num_contrato())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
            return false;
        }

        if (direcciFBean.getDif_cve_pers_fid() == null || "".equals(direcciFBean.getDif_cve_pers_fid())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Tipo de participante no puede estar vacío..."));
            return false;
        }

        if (direcciFBean.getDif_num_pers_fid() == null || "".equals(direcciFBean.getDif_num_pers_fid())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Número de participante no puede estar vacío..."));
            return false;
        }

        // si existe la Dirección
        boolean bexisteDir = CCFDI.onCFDI_validaExistenciaDir(direcciFBean);

        return bexisteDir;
    }

    public void validateFiso() {
        try {

            bConsulta_Disable = false;

            if (sFideicomiso == null || "".equals(sFideicomiso)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
            } else {
                sFideicomiso = sFideicomiso.trim();
                if (!isNumeric(sFideicomiso)) {
                    sFideicomiso = "";
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                }

                if (Integer.parseInt(sFideicomiso) <= 0) {
                    sFideicomiso = "";
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser mayor a cero..."));
                }

                if (!cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(sFideicomiso))) {
                    sFideicomiso = "";
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
                }

                disableConsulta();
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " validateFiso()";
        }
    }

    public void disableConsulta() {
        if (!sFideicomiso.equals("") || sTipo_Participante != null) {
            bConsulta_Disable = true;
        } else {
            bConsulta_Disable = false;
        }
    }

    public void validaConsulta() {
        bConsulta_Disable = false;
    }

    public void validateFiso2() {
        try {

            if (direcciFBean.getDif_num_contrato() == null || "".equals(direcciFBean.getDif_num_contrato())) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
                return;
            }

            if (!isNumeric(direcciFBean.getDif_num_contrato())) {
                direcciFBean.setDif_num_contrato("");
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser un campo numérico..."));
                return;
            }

            if (Integer.parseInt(direcciFBean.getDif_num_contrato()) <= 0) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso debe ser mayor a cero..."));
                return;
            }

            if (!cHonorarios.onHonorarios_checkFideicomiso(Integer.parseInt(direcciFBean.getDif_num_contrato()))) {
                direcciFBean.setDif_num_contrato("");
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no le pertenece, no existe o no está activo..."));
            }

            Consulta_NomFideicomiso();

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " validateFiso2()";
        }
    }

    public void Valida_CP() {

        if (direcciFBean.getDif_recep_cp() != null && !"".equals(direcciFBean.getDif_recep_cp())) {
            if (!isNumeric(direcciFBean.getDif_recep_cp())) {
                direcciFBean.setDif_recep_cp("");
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El C.P. debe ser un campo numérico..."));
                return;
            }

            int iCP = Integer.parseInt(direcciFBean.getDif_recep_cp());

            if (iCP < 1000 || iCP > 99999) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El C.P. no es validó..."));
                direcciFBean.setDif_recep_cp("");;
                return;
            }
        }
    }

    public void Valida_NumPart() {

        try {

            if (direcciFBean.getDif_num_pers_fid() != null && !"".equals(direcciFBean.getDif_num_pers_fid())) {
                if (!isNumeric(direcciFBean.getDif_num_pers_fid())) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El No. de Participante debe ser númerico..."));
                    direcciFBean.setDif_rfc("");
                    direcciFBean.setDif_nom_pers("");
                    direcciFBean.setDif_num_pers_fid("");
                    direcciFBean.setDif_nom_legal("");
                }

                if (Integer.parseInt(direcciFBean.getDif_num_pers_fid()) <= 0) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El No. de Participante debe ser mayor a cero..."));
                    direcciFBean.setDif_rfc("");
                    direcciFBean.setDif_nom_pers("");
                    direcciFBean.setDif_num_pers_fid("");
                    direcciFBean.setDif_nom_legal("");
                }

                if (!Consulta_NombreParticipante()) {
                    direcciFBean.setDif_rfc("");
                    direcciFBean.setDif_nom_pers("");
                    direcciFBean.setDif_num_pers_fid("");
                    direcciFBean.setDif_nom_legal("");
                }

                if (!"".equals(direcciFBean.getDif_nom_pers())) {
                    boolean bexisteDir = valida_Dir_Existente();

                    if (sTipoMantenimiento.equals("Alta") && bexisteDir == true) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Esta dirección ya existe..."));
                        direcciFBean.setDif_rfc("");
                        direcciFBean.setDif_nom_pers("");
                        direcciFBean.setDif_num_pers_fid("");
                        direcciFBean.setDif_nom_legal("");
                        bCargaCSF = false;
                    }
                }
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " Valida_NumPart()";
        }
    }

    private static boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void Valida_Email() {
        if (direcciFBean.getDif_mail() != null && !direcciFBean.getDif_mail().equals("")) {
            String email = direcciFBean.getDif_mail();
            String emailPattern = "^[_A-Z0-9-]+(\\.[_A-Z0-9-]+)*@" + "[A-Z0-9-]+(\\.[A-Z0-9-]+)*(\\.[A-Z]{2,4})$";
            Pattern pattern = Pattern.compile(emailPattern);

            if (email != null) {
                Matcher matcher = pattern.matcher(email);
                if (!matcher.matches()) {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Formato de correo eléctronico incorrecto..."));
                    direcciFBean.setDif_mail("");
                }
            }
        }
    }

    public void CargaArchivo(FileUploadEvent event) throws IOException, FileNotFoundException {

        BufferedReader bufferedReader = null;
        final int MAX_STR_LEN = 10000024;

        try {
            iRegistros = 0;
            dCorrectos = 0;
            iIncorrectos = 0;
            cargaInterfaz = null;
            bEstatus_Disable = false;
            CCFDI.onCFDI_Delete_CargaInterfaz();

            int result = 1, cont = 0;

            //DateTimeFormatter
            DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //Set_Date
            LocalDateTime localDateTime = LocalDateTime.now();
            String dateTime = localDateTime.format(dateTimeformatter);

            CCFDI.onCFDI_existeCargaInterfaz("MASIVO DIRECCIONES FISCALES", java.sql.Date.valueOf(dateTime));

            //Obtiene_el_nombre_del_archivo   
            sNomArchivo = event.getFile().getFileName();
            setFile(event.getFile());

            // Set_Values
            CargaInterfazBean cargaInterBean = new CargaInterfazBean();

            // Open_File             
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputstream(), "UTF-8"), MAX_STR_LEN);
            String line = bufferedReader.readLine();

            while (line != null) {
                cargaInterBean.setSecuencialArchivo(1);
                cargaInterBean.setSecuencial(result++);
                cargaInterBean.setFecha(java.sql.Date.valueOf(dateTime));
                cargaInterBean.setRuta(sNomArchivo);
                cargaInterBean.setNombreArchivo(sNomArchivo);
                cargaInterBean.setArchivoTemporal("N/A");
                cargaInterBean.setCadena(line);
                cargaInterBean.setEstatus("A");
                cargaInterBean.setMensaje("");

                // Inserta_los_registros_aceptados_en_CARGA_INTERFAZ 			
                cont += CCFDI.onCFDI_Insert_CargaInterfaz(cargaInterBean);
                line = bufferedReader.readLine();
            }

            // Close_BufferedReader
            bufferedReader.close();

            iRegistros = cont; //-1_para_no_contar_encabezado 

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se cargaron " + cont + " registros."));

            ConsultaArchivo();

        } catch (FileNotFoundException FileErr) {
            logger.error(mensajeError);
            mensajeError += "Descripción: " + FileErr.getMessage() + nombreObjeto + "CargaArchivo()";

            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El archivo " + sNomArchivo + " no se cargo correctamente."));

        } catch (IOException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "CargaArchivo()";
            logger.error(mensajeError);
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El archivo " + sNomArchivo + " no se cargo correctamente: " + exception));
        }
    }

    public void ValidaArchivo_CargaMasiva() {
        ProcesaArchivo(1);
    }

    public void Invoca_CargaMasiva() {
        ProcesaArchivo(2);
    }

    public void ProcesaArchivo(int sOpcion) {

        try {

            if (sNomArchivo.equals("")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No ha seleccionado el archivo."));
                return;
            }

            //INVOCAR_SPN_VAPLI_MASDONFI
            outParameterBean = CCFDI.onCFDI_Ejecuta_CargaMasiva(sNomArchivo, sNomArchivo, sOpcion);

            bEstatus_Disable = true;

            dCorrectos = outParameterBean.getdImporteCobrado();
            iIncorrectos = outParameterBean.getiNumFolioContab();

            //_Consulta_Resultado_Carga_Interfaz
            ConsultaArchivo();

            int erroresCargaInterfaz = 0;
            for (CargaInterfazBean cargaMasivaDirecciones : cargaInterfaz) {
                if (!"".equals(cargaMasivaDirecciones.getMensaje())) {
                    erroresCargaInterfaz++;
                }
            }

            if (!outParameterBean.getPsMsgErrOut().equals("")) {
                if (sOpcion == 1) {
                    // Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "El archivo no se validó correctamente " + outParameterBean.getPsMsgErrOut()));
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "El archivo no se proceso correctamente " + outParameterBean.getPsMsgErrOut()));
                }
            } else {
                // Set_Message
                if (sOpcion == 1) {
                    if (erroresCargaInterfaz == 0) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo se validó correctamente..."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "El archivo no se validó correctamente..."));
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                            "El archivo se procesó correctamente..."));
                }
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " ProcesaArchivo()";
            logger.error(mensajeError);
        }
    }

    public void ConsultaArchivo() {
        if (sNomArchivo.equals("")) {
            // Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No ha seleccionado el archivo..."));
            return;
        }

        cargaInterfaz = CCFDI.onCFDI_ConsultaInterfaz("MASIVO DIRECCIONES FISCALES", sNomArchivo, sNomArchivo);

    }

    public void Start_CargaMasiva() {
        iRegistros = 0;
        dCorrectos = 0;
        iIncorrectos = 0;
        sNomArchivo = "";
        cargaInterfaz = null;
        bEstatus_Disable = false;
        CCFDI.onCFDI_Delete_CargaInterfaz();
        RequestContext.getCurrentInstance().execute("dgCargaMasivaDir.show();");
    }

    public void Limpia_CargaMasiva() {
        iRegistros = 0;
        dCorrectos = 0;
        iIncorrectos = 0;
        sNomArchivo = "";
        cargaInterfaz = null;
        bEstatus_Disable = false;
        RequestContext.getCurrentInstance().execute("dgCargaMasivaDir.hide();");
    }

    public void select_RegFisc() {

        try {

            if (direcciFBean.getDif_regimen_fiscal().equals("0")) {
                // Set_Message
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar un Régimen fiscal..."));
                direcciFBean.setDif_regimen_fiscal("");
                bBtnRegimen = false;
                return;
            } else {
                // Selecciona_Regimen_Fiscal
                for (Map.Entry<String, String> o : mRegFisc.entrySet()) {

                    if (direcciFBean.getDif_regimen_fiscal().equals(o.getKey())) {
                        bBtnRegimen = true;
                        direcciFBean.setDif_regimen_fiscal(o.getKey());
                        direcciFBean.setDif_desc_regimen_fiscal(o.getValue());
                        return;
                    }
                }
            }
        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_RegFisc()";
            logger.error(mensajeError);
        }
    }

    public void Valida_RegFisc() {
        bBtnRegimen = false;

        if (direcciFBean.getDif_desc_regimen_fiscal() == null || "".equals(direcciFBean.getDif_desc_regimen_fiscal())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Debe seleccionar Régimen fiscal..."));
            return;
        }

        //Validaciones_Persona_Fisica,_Moral_VS_Regimen_Fiscal        
        if ((direcciFBean.getDif_cve_pers_fid() == null || "".equals(direcciFBean.getDif_cve_pers_fid()))
                || (direcciFBean.getDif_num_pers_fid() == null || "".equals(direcciFBean.getDif_num_pers_fid()))) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Faltan datos del participante..."));
        } else {
            if (Valida_TipoPers()) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Régimen fiscal, validado..."));
                bBtnRegimen = true;
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Régimen fiscal, no válido para el tipo de Persona..."));
                direcciFBean.setDif_desc_regimen_fiscal("");
                direcciFBean.setDif_regimen_fiscal("");
            }
        }
    }

    public boolean Valida_TipoPers() {
        String sPerFis = "", sPerMor = "";

        //'Recupera registro del catalogo de regimen fiscal     	
        String TiposPers = CCFDI.onCFDI_ValidaRegimenFisc(direcciFBean.getDif_regimen_fiscal());

        if (TiposPers != null && !TiposPers.equals("")) {
            String[] lRegimen = TiposPers.split("/");
            sPerFis = lRegimen[0];
            sPerMor = lRegimen[1];
        } else {
            FacesContext.getCurrentInstance().addMessage("Fiduciario",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe, el Régimen Fiscal..."));
            return false;
        }

        //'Recupera tipo de persona del participante
        String sTipoPart = CCFDI.onCCFDI_Consulta_TipoParticipante(direcciFBean);

        if ("".equals(sTipoPart)) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe, la clave tipo persona del contrato " + direcciFBean.getDif_num_contrato() + " participante " + direcciFBean.getDif_cve_pers_fid() + " número de persona " + direcciFBean.getDif_num_pers_fid()));
            return false;
        }

        switch (sTipoPart) {

            case "FISICA":
                if (sPerFis.equals("1.00")) {
                    return true;
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Contrato " + direcciFBean.getDif_num_contrato() + " participante " + direcciFBean.getDif_cve_pers_fid() + " número de persona " + direcciFBean.getDif_num_pers_fid() + " es " + sTipoPart + " no le corresponde el régimen fiscal " + direcciFBean.getDif_desc_regimen_fiscal() + " " + direcciFBean.getDif_regimen_fiscal()));
                    return false;
                }
            case "MORAL":
                if (sPerMor.equals("1.00")) {
                    return true;
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Contrato " + direcciFBean.getDif_num_contrato() + " participante " + direcciFBean.getDif_cve_pers_fid() + " número de persona " + direcciFBean.getDif_num_pers_fid() + " es " + sTipoPart + " no le corresponde el régimen fiscal " + direcciFBean.getDif_desc_regimen_fiscal() + " " + direcciFBean.getDif_regimen_fiscal()));
                    return false;
                }
            default:
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Contrato " + direcciFBean.getDif_num_contrato() + " participante " + direcciFBean.getDif_cve_pers_fid() + " número de persona " + direcciFBean.getDif_num_pers_fid() + " es " + sTipoPart + " no le corresponde el régimen fiscal " + direcciFBean.getDif_desc_regimen_fiscal() + " " + direcciFBean.getDif_regimen_fiscal()));
                return false;
        }
    }

    public void select_Pais() {

        try {
            mEstados = null;

            if (direcciFBean.getDif_num_pais() != 0) {
                // Selecciona_Pais
                for (Map.Entry<Integer, String> o : mPais.entrySet()) {

                    if (direcciFBean.getDif_num_pais() == o.getKey()) {
                        direcciFBean.setDif_num_pais(o.getKey());
                        direcciFBean.setDif_nom_pais(o.getValue());

                        mEstados = CCFDI.onCFDI_getEstados(direcciFBean.getDif_num_pais());
                        return;
                    }
                }
            }

        } catch (NumberFormatException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Pais()";
            logger.error(mensajeError);
        }
    }

    public void select_Estado() {

        try {
            // Selecciona_Estado
            if (direcciFBean.getDif_nom_estado() != null && !direcciFBean.getDif_nom_estado().equals("")) {
                for (Map.Entry<Integer, String> o : mEstados.entrySet()) {
                    if (direcciFBean.getDif_nom_estado().contentEquals(o.getValue())) {
                        direcciFBean.setDif_num_estado(o.getKey());
                        direcciFBean.setDif_nom_estado(o.getValue());
                        return;
                    }
                }
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " select_Pais()";
            logger.error(mensajeError);
        }
    }

    public void onRowSelectDirPart(SelectEvent event) {
        try {

            if (dirParticipantes.size() > 0) {

                this.direcciBean = (DirecciBean) event.getObject();

                direcciFBean.setDif_num_contrato(String.valueOf(direcciBean.getDir_num_contrato()));
                direcciFBean.setDif_cve_pers_fid(direcciBean.getDir_cve_pers_fid());
                direcciFBean.setDif_num_pers_fid(String.valueOf(direcciBean.getDir_num_pers_fid()));

                Consulta_NombreParticipante();

                direcciFBean.setDif_recep_calle(direcciBean.getDir_calle_num());
                direcciFBean.setDif_recep_colonia(direcciBean.getDir_nom_colonia());
                direcciFBean.setDif_num_pais(direcciBean.getDir_num_pais());
                direcciFBean.setDif_nom_pais(direcciBean.getDir_nom_pais());
                select_Pais();
                direcciFBean.setDif_num_estado(direcciBean.getDir_num_estado());
                direcciFBean.setDif_nom_estado(direcciBean.getDir_nom_estado());
                select_Estado();
                direcciFBean.setDif_recep_cp(direcciBean.getDir_codigo_postal());
                direcciFBean.setDif_mail(direcciBean.getDir_mail());

                //Vacios
                direcciFBean.setDif_recep_no_ext("");
                direcciFBean.setDif_recep_no_int("");
                direcciFBean.setDif_recep_referencia("");
                direcciFBean.setDif_recep_localidad("");

                //Telefonos
                String sTelCasa = "", sTelOfic = "";
                if (direcciBean.getBen_num_lada_casa() != null && !"".equals(direcciBean.getBen_num_lada_casa())) {
                    sTelCasa = direcciBean.getBen_num_lada_casa().trim();
                }

                if (direcciBean.getBen_num_telef_casa() != null && !"".equals(direcciBean.getBen_num_telef_casa())) {
                    sTelCasa += direcciBean.getBen_num_telef_casa().trim();
                }

                if (direcciBean.getBen_num_lada_ofic() != null && !"".equals(direcciBean.getBen_num_lada_ofic())) {
                    sTelOfic = direcciBean.getBen_num_lada_ofic().trim();
                }

                if (direcciBean.getBen_num_telef_ofic() != null && !"".equals(direcciBean.getBen_num_telef_ofic())) {
                    sTelOfic += direcciBean.getBen_num_telef_ofic().trim();
                }

                if (direcciBean.getBen_num_ext_ofic() != null && !"".equals(direcciBean.getBen_num_ext_ofic())) {
                    sTelOfic += direcciBean.getBen_num_ext_ofic().trim();
                }

                if ("".equals(sTelCasa) && "".equals(sTelOfic)) {
                    direcciFBean.setDif_telefono(sTelCasa.concat(" ").concat(sTelOfic));
                } else {
                    if (!"".equals(sTelOfic)) {
                        direcciFBean.setDif_telefono(sTelOfic);
                    }

                    if (!"".equals(sTelCasa)) {
                        direcciFBean.setDif_telefono(sTelCasa);
                    }
                }
            }

        } catch (StringIndexOutOfBoundsException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " onRowSelectDirParticipantes()";
            logger.error(mensajeError);
        }
    }

    public void Importar() {

        if (direcciFBean.getDif_num_contrato() == null || "".equals(direcciFBean.getDif_num_contrato())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no puede estar vacío..."));
            bDirecci = false;
            return;
        }

        if (direcciFBean.getDif_cve_pers_fid() == null || direcciFBean.getDif_cve_pers_fid().equals("")) {
            dirParticipantes = CCFDI.onCCFDI_Consulta_DirParticipantes(Integer.parseInt(direcciFBean.getDif_num_contrato()));
        } else {
            dirParticipantes = CCFDI.onCCFDI_Consulta_DireccionesPart(Integer.parseInt(direcciFBean.getDif_num_contrato()), direcciFBean.getDif_cve_pers_fid());
        }

        bDirecci = true;
    }

    public void copyFile(String fileName, InputStream inputStream) throws FileNotFoundException {

        int read = 0;
        OutputStream outputStream = null;
        String ruta = System.getProperty("java.io.tmpdir") + File.separator;

        try {
            outputStream = new FileOutputStream(new File(ruta + fileName));

            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + " copyFile()";
            logger.error(mensajeError);
        } finally {
            if (inputStream != null || outputStream != null) {
                safeClose(inputStream, outputStream);
            }
        }
    }

    public static void safeClose(InputStream fileinput, OutputStream outp) {
        if (fileinput != null) {
            try {
                fileinput.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        if (outp != null) {
            try {
                outp.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void CargarCSF(FileUploadEvent event) throws FileNotFoundException {

        String Rfc = "";
        sRegCapital = "";
        bRegCapital = false;
        PdfReader reader = null;
        boolean bValidaConstancia = false;
        String sCP = "", sNombre = "", sNomCalle = "", sLocalidad = "", sLocalidad_2 = "", sColonia = "",
                sNumExt = "", sNumInt = "", sRegimen = "", sApellido = "", sEstado = "",
                sPersona = "", sNomLegal = "";

        try {

            // Copia el archivo 
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());

            reader = new PdfReader(System.getProperty("java.io.tmpdir") + File.separator + event.getFile().getFileName());

            //---------------
            // pageNumber = 1
            //---------------
            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);

            //Valida que el archivo no tenga imagenes que no pueda leer
            if (textFromPage.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Formato no validó..."));
                return;
            }

            String[] lines = textFromPage.split("\n");

            for (int i = 0; i < lines.length; i++) {

                if (lines[i].contains("CÉDULA DE IDENTIFICACIÓN FISCAL")) {
                    bValidaConstancia = true;
                }

                if (lines[i].contains("CEDULA DE IDENTIFICACION FISCAL")) {
                    bValidaConstancia = true;
                }

                if (lines[i].contains("RFC:")) {
                    Rfc = lines[i].substring(lines[i].indexOf(":") + 1).trim();

                    if (Rfc.length() == 0) {
                        Rfc = lines[i - 1].trim();
                    }
                }

                if (lines[i].contains("Código Postal:")) {
                    sCP = lines[i].substring(lines[i].indexOf(":") + 1, lines[i].indexOf("Tipo de Vialidad:")).trim();
                }

                //--Nombre_Calle 
                if (lines[i].contains("Nombre de Vialidad:")) {
                    //Un renglon
                    if (lines[i].contains("Número Exterior:")) {
                        sNomCalle = lines[i].substring(lines[i].indexOf(":") + 1, lines[i].indexOf("Número Exterior:"));
                    } //Dos renglones
                    else {
                        sNomCalle = lines[i].substring(lines[i].indexOf(":") + 1);
                        sNomCalle += " ".concat(lines[i + 2]);
                        sNomCalle = sNomCalle.trim();
                    }
                }

                //--Numero_Exterior
                if (lines[i].contains("Número Exterior:")) {
                    sNumExt = lines[i].substring(lines[i].indexOf("Número Exterior:") + 16).trim();
                }

                //--Numero_Interior
                if (lines[i].contains("Número Interior:")) {
                    sNumInt = lines[i].substring(lines[i].indexOf(":") + 1, lines[i].indexOf("Nombre de la Colonia:"));
                }

                //--Estado
                if (lines[i].contains("Nombre de la Entidad Federativa:")) {
                    sEstado = lines[i].substring(lines[i].indexOf(":") + 1, lines[i].indexOf("Entre Calle:")).trim();
                }

                //--Nombre de la Colonia  
                if (lines[i].contains("Nombre de la Colonia:")) {
                    sColonia = lines[i].substring(lines[i].indexOf("Nombre de la Colonia:") + 21).trim();
                }

                //--Nombre de la Localidad (Municipio o Demarcación Territorial)  
                //Misma pagina
                if (lines[i].contains("Nombre del Municipio o Demarcación Territorial:") && textFromPage.contains("Nombre de la Entidad Federativa:")) {
                    sLocalidad = textFromPage.substring(textFromPage.indexOf("Nombre del Municipio o Demarcación Territorial: ") + 47,
                            textFromPage.indexOf("Nombre de la Entidad Federativa:")).trim();

                } else {
                    //Diferente pagina
                    if (lines[i].contains("Nombre del Municipio o Demarcación Territorial:")) {
                        sLocalidad = textFromPage.substring(textFromPage.indexOf("Nombre del Municipio o Demarcación Territorial: ") + 47,
                                textFromPage.indexOf("Página  [1]")).trim();
                    }
                }

                if (lines[i].contains("Nombre de la Localidad:") && !lines[i].contains(" Nombre del Municipio o Demarcación Territorial:")) {
                    sLocalidad_2 = lines[i];
                }

                if (lines[i].contains("Nombre de la Localidad:") && lines[i].contains(" Nombre del Municipio o Demarcación Territorial:")) {
                    sLocalidad_2 = lines[i];
                }

                //--Nombre Comercial
                // Un renglon 	 
                if (lines[i].contains("Nombre Comercial:")) {
                    sNomLegal = lines[i].substring(lines[i].indexOf(":") + 1).trim();
                }
            }

            if (!bValidaConstancia) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El documento no corresponde a una Cédula de Identificación Fiscal..."));
                return;
            }

            if (Rfc.length() == 12) {
                //--Persona Moral  
                sPersona = "MORAL";

                for (int i = 0; i < lines.length; i++) {
                    // Todo el nombre
                    // Un renglon
                    String sCompara = "RFC: ".concat(Rfc).concat("\nDenominación/Razón Social:");

                    if (lines[i].contains("Denominación/Razón Social:")) {
                        if (textFromPage.contains(sCompara)) {
                            sNombre = lines[i].substring(lines[i].indexOf(":") + 1).trim();
                        } else {
                            //Un renglon sin RFC
                            if ("".equals(sNombre)) {
                                sNombre = lines[i].substring(lines[i].indexOf(":") + 1).trim();
                            }
                            if ("".equals(sNombre)) {
                                // Dos renglones
                                sNombre = textFromPage.substring(textFromPage.indexOf("RFC: ".concat(Rfc)) + (Rfc.length() + 5),
                                        textFromPage.indexOf("Régimen Capital:")).trim();
                                sNombre = sNombre.replace("Denominación/Razón Social:", "");
                                sNombre = sNombre.replace("\n", " ");
                            }
                        }
                    }

                    //--Regimen de Capital para Morales
                    // Un renglon 						
                    if (lines[i].contains("Régimen Capital:")) {
                        sRegCapital = lines[i].substring(lines[i].indexOf(":") + 1).trim();

                        if (sRegCapital.length() == 0) {
                            // Dos renglones
                            sRegCapital = textFromPage.substring(textFromPage.indexOf("Denominación/Razón Social: ".concat(sNombre)) + (sNombre.length() + 27), textFromPage.indexOf("Nombre Comercial:")).trim();
                            sRegCapital = sRegCapital.replace("Régimen Capital:", "");
                            sRegCapital = sRegCapital.replace("\n", " ");
                        }
                        bRegCapital = true;
                    }

                    if (lines[i].contains("Nombre Comercial:")) {
                        if (sNomLegal.isEmpty() && !sRegCapital.isEmpty()) {
                            sNomLegal = textFromPage.substring(textFromPage.indexOf("Régimen Capital:") + 16, textFromPage.indexOf("Nombre Comercial:")).trim();

                            if (sNomLegal.length() > 0) {
                                if (sNomLegal.indexOf("\n") > 0) {
                                    sNomLegal = sNomLegal.substring(sNomLegal.indexOf("\n")).trim();
                                } else {
                                    sNomLegal = "";
                                }
                            }
                        }
                    }
                }
            } else {
                //--Persona Fisica 
                sPersona = "FISICA";

                for (int i = 0; i < lines.length; i++) {

                    if (lines[i].contains("Nombre (s):")) {
                        sNombre = lines[i].substring(lines[i].indexOf(":") + 1).trim();

                        if (sNombre.length() == 0) {
                            sNombre = lines[i - 1].trim();
                        }
                    }

                    if (lines[i].contains("Primer Apellido:")) {
                        sApellido = lines[i].substring(lines[i].indexOf(":") + 1).trim();

                        if (sApellido.length() == 0) {
                            sApellido = lines[i - 1].trim();
                        }

                        sNombre += " " + sApellido;
                    }

                    if (lines[i].contains("Segundo Apellido:")) {
                        sApellido = lines[i].substring(lines[i].indexOf(":") + 1).trim();

                        if (sApellido.length() == 0) {
                            sApellido = lines[i - 1].trim();
                        }

                        sNombre += " " + sApellido;
                    }
                }
            }

            //---------------
            // pageNumber = 2
            //---------------
            String textFromPage2 = PdfTextExtractor.getTextFromPage(reader, 2);
            String[] lines2 = textFromPage2.split("\n");

            for (int i = 0; i < lines2.length; i++) {
                if (lines2[i].contains("Régimen Fecha Inicio Fecha Fin")) {
                    sRegimen = lines2[i + 1];
                    sRegimen = sRegimen.substring(0, sRegimen.indexOf("/") - 2).trim();
                }

                //--Estado
                if (lines2[i].contains("Nombre de la Entidad Federativa:")) {
                    sEstado = lines2[i].substring(lines2[i].indexOf(":") + 1, lines2[i].indexOf("Entre Calle:")).trim();
                }
            }

            reader.close();

            //---------------
            //Valida que se cargue una persona correcta
            //---------------
            if (direcciFBean.getDif_num_contrato() != null && direcciFBean.getDif_cve_pers_fid() != null && direcciFBean.getDif_num_pers_fid() != null) {
                if (!"".equals(direcciFBean.getDif_num_contrato()) && !"".equals(direcciFBean.getDif_cve_pers_fid()) && !"".equals(direcciFBean.getDif_num_pers_fid())) {
                    //'Recupera tipo de persona del participante
                    String sTipoPart = CCFDI.onCCFDI_Consulta_TipoParticipante(direcciFBean);

                    if (!Rfc.equals(direcciFBean.getDif_rfc())) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Los datos del CSF no corresponden con el R.F.C.: " + direcciFBean.getDif_rfc()));
                        sRegCapital = "";
                        bRegCapital = false;
                        if (!sTipoMantenimiento.equals("Modificar")) {
                            direcciFBean.setDif_num_pais(0);
                            direcciFBean.setDif_num_estado(0);
                            direcciFBean.setDif_nom_pais("");
                            direcciFBean.setDif_nom_estado("");
                            direcciFBean.setDif_recep_localidad("");
                            direcciFBean.setDif_regimen_fiscal("");
                            direcciFBean.setDif_recep_cp("");
                            direcciFBean.setDif_recep_no_ext("");
                            direcciFBean.setDif_recep_calle("");
                            direcciFBean.setDif_recep_no_int("");
                            direcciFBean.setDif_recep_colonia("");
                            direcciFBean.setDif_nom_legal("");
                        }

                        return;
                    }

                    /* Se quita validación de tipo de persona
                    if (!sPersona.equals(sTipoPart)) {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Los datos del CSF no corresponden para una Persona " + sTipoPart));
                        sRegCapital = "";
                        bRegCapital = false;
                        if (!sTipoMantenimiento.equals("Modificar")) {
                            direcciFBean.setDif_recep_cp("");
                            direcciFBean.setDif_recep_no_ext("");
                            direcciFBean.setDif_recep_calle("");
                            direcciFBean.setDif_recep_no_int("");
                            direcciFBean.setDif_recep_colonia("");
                            direcciFBean.setDif_nom_legal("");
                        }
                        return;
                    }*/
                }
            }

            //--Estado 
            if (sEstado != null && !sEstado.equals("")) {
                //Default_Mexico
                direcciFBean.setDif_num_pais(1);
                select_Pais();
                //Coincidencia con el cátalogo de Estados
                if (sEstado.equals("MEXICO")) {
                    sEstado = "ESTADO DE MEXICO";
                }
                direcciFBean.setDif_nom_estado(sEstado);
                select_Estado();
            }

            //--Localidad	 
            if (sLocalidad != null && !sLocalidad.equals("")) {
                sLocalidad = sLocalidad.replace(sLocalidad_2, "");
                sLocalidad = sLocalidad.replace("\n", " ");

                direcciFBean.setDif_recep_localidad(sLocalidad);
            }

            //--Regimen Fiscal
            if (!"".equals(sRegimen)) {
                String sCveReg = CCFDI.onCCFDI_BuscaRegimen(sRegimen);
                direcciFBean.setDif_regimen_fiscal(sCveReg);
                select_RegFisc();
            }

            direcciFBean.setDif_recep_cp(sCP);
            direcciFBean.setDif_recep_no_ext(sNumExt);
            direcciFBean.setDif_recep_calle(sNomCalle);
            direcciFBean.setDif_recep_no_int(sNumInt);
            direcciFBean.setDif_recep_colonia(sColonia);
            direcciFBean.setDif_nom_legal(sNomLegal);

            if (!sTipoMantenimiento.equals("Modificar")) {
                direcciFBean.setDif_rfc(Rfc);
                direcciFBean.setDif_nom_pers(sNombre);
            }

            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se cargaron los datos del CSF, validar: Nombre Fiscal, Nombre Legal, R.F.C., Régimen Capital, Calle, No. Int, No. Ext, C.P, Colonia, Localidad, País, Estado y Régimen Fiscal. "));

        } catch (IOException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " LeerPDF()";
            logger.error(mensajeError);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void Exportar() {
        try {

            archivoNombre = "Direcciones_Fiscales_".concat(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema").toString().replace("/", "_")).concat(".txt");
            archivoNombre = archivoNombre.replace("Á", "A");
            archivoNombre = archivoNombre.replace("É", "E");
            archivoNombre = archivoNombre.replace("Í", "I");
            archivoNombre = archivoNombre.replace("Ó", "O");
            archivoNombre = archivoNombre.replace("Ú", "U");
            archivoUbicacion = System.getProperty("java.io.tmpdir") + File.separator;

            if (GeneraArchivo(archivoUbicacion.concat(archivoNombre))) {
                destinoURL = "/scotiaFid/SArchivoDescarga?".concat(archivoNombre);
                FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Se generó correctamente el archivo: " + archivoNombre));
            } else {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", mensajeError));
            }
        } catch (IOException Err) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", Err.getMessage()));
        }
    }

    private boolean GeneraArchivo(String nombreArchivo) {
        boolean res = false;
        //Preparamos el archivo 
        archivo = new File(nombreArchivo);
        try {
            archivoSalida = new FileOutputStream(archivo);
            archivoLinea = new String();

            archivoLinea += "Fideicomiso".concat("\t");
            archivoLinea += "Tipo Participante".concat("\t");
            archivoLinea += "No. Participante".concat("\t");
            archivoLinea += "Calle".concat("\t");
            archivoLinea += "No. Ext.".concat("\t");
            archivoLinea += "No. Int.".concat("\t");
            archivoLinea += "Colonia".concat("\t");
            archivoLinea += "Localidad".concat("\t");
            archivoLinea += "Municipio".concat("\t");
            archivoLinea += "País".concat("\t");
            archivoLinea += "Estado".concat("\t");
            archivoLinea += "C.P.".concat("\t");
            archivoLinea += "Referencia".concat("\t");
            archivoLinea += "Telefonos".concat("\t");
            archivoLinea += "Mail".concat("\t");
            archivoLinea += "Cve. Régimen Fiscal".concat("\t");
            archivoLinea += "Desc. Régimen Fiscal".concat("\t");
            archivoLinea = archivoLinea + "\n";
            buffer = archivoLinea.getBytes();
            archivoSalida.write(buffer);

            for (DirecciFBean direcci_Bean : direcciones) {
                archivoLinea = new String();

                archivoLinea += String.valueOf(direcci_Bean.getDif_num_contrato()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_cve_pers_fid()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_num_pers_fid()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_calle()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_no_ext()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_no_int()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_colonia()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_localidad()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_municipio()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_nom_pais()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_nom_estado()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_cp()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_recep_referencia()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_telefono()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_mail()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_regimen_fiscal()).concat("\t");
                archivoLinea += String.valueOf(direcci_Bean.getDif_desc_regimen_fiscal()).concat("\t");
                archivoLinea = archivoLinea + "\n";
                buffer = archivoLinea.getBytes();
                archivoSalida.write(buffer);
            }

            archivoSalida.close();
            res = true;
        } catch (FileNotFoundException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        } catch (IOException exception) {
            mensajeError = "Error al generar el archivo: " + exception;
        }
        return res;
    }

    public void Consulta_NomFideicomiso() {
        String sNomFiso = "";
        bCargaCSF = false;

        if (direcciFBean.getDif_num_contrato() != null && !"".equals(direcciFBean.getDif_num_contrato())) {

            sNomFiso = CCFDI.onCFDI_getNomFiso(Integer.parseInt(direcciFBean.getDif_num_contrato()));
            direcciFBean.setDif_nom_cto(sNomFiso);

            if (direcciFBean.getDif_nom_cto() == null && "".equals(direcciFBean.getDif_nom_cto())) {
                direcciFBean.setDif_num_contrato("");
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "No existe el Fideicomiso o no le pertenece..."));
                return;
            }

            if (direcciFBean.getDif_nom_cto().equals("EXTINTO")) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso se encuentra Extinto..."));
                direcciFBean.setDif_num_contrato("");
                direcciFBean.setDif_nom_cto("");
                return;
            }

            if (sNomFiso.equals("CANCELADO")) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso no existe o está Cancelado..."));
                direcciFBean.setDif_num_contrato("");
                direcciFBean.setDif_nom_cto("");
                return;
            }

            if ((direcciFBean.getDif_cve_pers_fid() != null && !"".equals(direcciFBean.getDif_cve_pers_fid()))
                    && (direcciFBean.getDif_num_pers_fid() != null && !"".equals(direcciFBean.getDif_num_pers_fid()))) {
                bCargaCSF = true;
            }

            Backup_direcciF.setDif_nom_cto(direcciFBean.getDif_nom_cto());
        }
    }

    public boolean Consulta_NombreParticipante() {
        direcciFBean.setDif_nom_pers("");
        direcciFBean.setDif_rfc("");
        direcciFBean.setDif_nom_legal("");

        bCargaCSF = false;
        List<String> datParticipante = new ArrayList<>();

        if ((direcciFBean.getDif_num_contrato() != null && !"".equals(direcciFBean.getDif_num_contrato()))
                && (direcciFBean.getDif_cve_pers_fid() != null && !"".equals(direcciFBean.getDif_cve_pers_fid()))
                && (direcciFBean.getDif_num_pers_fid() != null && !"".equals(direcciFBean.getDif_num_pers_fid()))) {

            datParticipante = CCFDI.onCCFDI_Consulta_NomParticipantes(direcciFBean);

            if (datParticipante.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El participante no existe"));
                return false;
            } else {
                direcciFBean.setDif_nom_pers(datParticipante.get(0));
                direcciFBean.setDif_rfc(datParticipante.get(1));
                direcciFBean.setDif_nom_legal(datParticipante.get(2));
            }

            if (!CCFDI.onCCFDI_Consulta_Participante(direcciFBean)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El participante está cancelado..."));
                return false;
            }
            bCargaCSF = true;
        }

        return true;
    }
}
