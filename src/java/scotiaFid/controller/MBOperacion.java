/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : MBOperacion.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.controller
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.controller;

//Imports Statics
import java.io.IOException;

//Imports
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.datatable.DataTable;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import scotiaFid.bean.CalProdBean;
import scotiaFid.bean.PlanBean;
import scotiaFid.bean.RutinaBean;
import scotiaFid.dao.COperacion;
import scotiaFid.util.LogsContext;

//Class
@ManagedBean(name = "mbOperacion")
@ViewScoped
public class MBOperacion implements Serializable {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * S E R I A L
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String plan;
    //----------------------------------------------------------------------------
    private String rutina;
    //----------------------------------------------------------------------------
    private String planCalProd;
    //----------------------------------------------------------------------------
    private String rutinaCalProd;
    //----------------------------------------------------------------------------
    private String estatus;
    //----------------------------------------------------------------------------
    private String calprod;
    //----------------------------------------------------------------------------
    private String secuencia;
    //----------------------------------------------------------------------------
    private int habilitaBotonAsignar;
    //----------------------------------------------------------------------------
    private int habilitaBotonDesasignar;
    //----------------------------------------------------------------------------
    private static HttpServletRequest peticionURL;
    // -----------------------------------------------------------------------------
    private String mensajeError;
    // -----------------------------------------------------------------------------
    private final String nombreObjeto;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * I N Y E C C I O N   D E   B E A N S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private PlanBean planBean;
    //----------------------------------------------------------------------------
    private RutinaBean rutinaBean;
    //----------------------------------------------------------------------------
    private CalProdBean calProdBean;
    //----------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * S E R V I C E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private COperacion cOperacion = new COperacion();
    //----------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * O B J E T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private FacesMessage mensaje;
    //----------------------------------------------------------------------------
    private List<PlanBean> planes = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<String> horas = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<String> minutos = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<RutinaBean> rutinas = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<String> areas = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<String> planesCalProd = new ArrayList<>();
    //----------------------------------------------------------------------------
    private List<String> rutinasCalProd = new ArrayList<>();
    ;
   //----------------------------------------------------------------------------
   private List<CalProdBean> rutinasInCalprod = new ArrayList<>();
    //----------------------------------------------------------------------------

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<PlanBean> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanBean> planes) {
        this.planes = planes;
    }

    public PlanBean getPlanBean() {
        return planBean;
    }

    public void setPlanBean(PlanBean planBean) {
        this.planBean = planBean;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public List<String> getHoras() {
        return horas;
    }

    public void setHoras(List<String> horas) {
        this.horas = horas;
    }

    public List<String> getMinutos() {
        return minutos;
    }

    public void setMinutos(List<String> minutos) {
        this.minutos = minutos;
    }

    public RutinaBean getRutinaBean() {
        return rutinaBean;
    }

    public void setRutinaBean(RutinaBean rutinaBean) {
        this.rutinaBean = rutinaBean;
    }

    public List<RutinaBean> getRutinas() {
        return rutinas;
    }

    public void setRutinas(List<RutinaBean> rutinas) {
        this.rutinas = rutinas;
    }

    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public CalProdBean getCalProdBean() {
        return calProdBean;
    }

    public void setCalProdBean(CalProdBean calProdBean) {
        this.calProdBean = calProdBean;
    }

    public List<String> getPlanesCalProd() {
        return planesCalProd;
    }

    public void setPlanesCalProd(List<String> planesCalProd) {
        this.planesCalProd = planesCalProd;
    }

    public List<String> getRutinasCalProd() {
        return rutinasCalProd;
    }

    public void setRutinasCalProd(List<String> rutinasCalProd) {
        this.rutinasCalProd = rutinasCalProd;
    }

    public List<CalProdBean> getRutinasInCalprod() {
        return rutinasInCalprod;
    }

    public void setRutinasInCalprod(List<CalProdBean> rutinasInCalprod) {
        this.rutinasInCalprod = rutinasInCalprod;
    }

    public String getPlanCalProd() {
        return planCalProd;
    }

    public void setPlanCalProd(String planCalProd) {
        this.planCalProd = planCalProd;
    }

    public String getRutinaCalProd() {
        return rutinaCalProd;
    }

    public void setRutinaCalProd(String rutinaCalProd) {
        this.rutinaCalProd = rutinaCalProd;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCalprod() {
        return calprod;
    }

    public void setCalprod(String calprod) {
        this.calprod = calprod;
    }

    public int getHabilitaBotonAsignar() {
        return habilitaBotonAsignar;
    }

    public void setHabilitaBotonAsignar(int habilitaBotonAsignar) {
        this.habilitaBotonAsignar = habilitaBotonAsignar;
    }

    public int getHabilitaBotonDesasignar() {
        return habilitaBotonDesasignar;
    }

    public void setHabilitaBotonDesasignar(int habilitaBotonDesasignar) {
        this.habilitaBotonDesasignar = habilitaBotonDesasignar;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public MBOperacion() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        peticionURL = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.controller.MBOperacion.";
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @PostConstruct
    public void init() {
        try {
            if ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema") == null) {
                //System.out.println("Sesión no valida ...");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
            } else {

                //Write_Console
                //out.print("Init Controller...");
                //out.print("List Planes...");
                //out.print("List Rutinas...");
                //out.print("List Areas Responsables...");
                //out.print("List Combo Planes In CalProd...");
                //out.print("List DataTable Rutinas...");
                //out.print("List DataTable Rutinas In CalProd...");
                //Set_List_Planes
                this.setPlanes(cOperacion.onOperacion_GetPlanes());

                //Set_List_Rutina
                this.setRutinas(cOperacion.onOperacion_GetRutinas());

                //Set_Areas_Responsables
                this.setAreas(cOperacion.onOperacion_GetClaveAreaResponsable());

                //Set_Planes_Combo
                this.setPlanesCalProd(cOperacion.onOperacion_GetPlanesInCalProd());

                //Get_First_Plan
                planCalProd = this.getPlanesCalProd().get(0);

                //Disabled_CommandButton_Asignar
                habilitaBotonAsignar = 0;

                //Disabled_CommandButton_Desasignar
                habilitaBotonDesasignar = 0;

                //Set_Rutinas_Not_In_CalProd
                this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

                //Set_Rutinas_In_CalProd
                this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

                //Set_Bean
                this.planBean = new PlanBean();

                //Set_Bean
                this.rutinaBean = new RutinaBean();

                //Set_Bean
                this.calProdBean = new CalProdBean();

            }
        } catch (IOException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto + "onPostConstruct()";
            //oGeneraLog.onGeneraLog(MBOperacion.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");
        }
    }

    public void initPlan() {

        //Set_List_Planes
        this.setPlanes(cOperacion.onOperacion_GetPlanes());

        this.setPlanesCalProd(cOperacion.onOperacion_GetPlanesInCalProd());

        planCalProd = this.getPlanesCalProd().get(0);

        //Set_Bean
        this.planBean = new PlanBean();
    }

    public void initRutina() throws SQLException {
        //Set_List_Rutina
        this.setRutinas(cOperacion.onOperacion_GetRutinas());

        //Set_Areas_Responsables
        this.setAreas(cOperacion.onOperacion_GetClaveAreaResponsable());

        this.setPlanesCalProd(cOperacion.onOperacion_GetPlanesInCalProd());

        planCalProd = this.getPlanesCalProd().get(0);

        //Set_Bean
        this.rutinaBean = new RutinaBean();
    }

    public void initCalProd() {
        //Set_Planes_Combo
        this.setPlanesCalProd(cOperacion.onOperacion_GetPlanesInCalProd());

        //Get_First_Plan
        planCalProd = this.getPlanesCalProd().get(0);

        //Disabled_CommandButton_Asignar
        habilitaBotonAsignar = 0;

        //Disabled_CommandButton_Desasignar
        habilitaBotonDesasignar = 0;

        //Set_Rutinas_Not_In_CalProd
        this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

        //Set_Rutinas_In_CalProd
        this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

        //Set_Bean
        this.calProdBean = new CalProdBean();
    }

    public void cleanPlan() {

        planBean = new PlanBean();

    }

    public void cleanRutina() {

        rutinaBean = new RutinaBean();
    }

    public void loadPlan(PlanBean planBean) {
        //Set_Plan
        plan = planBean.getClave();

        //Write_Console
        //out.print("Plan seleccionado...".concat(plan));
        //Get_Plan
        this.planBean = cOperacion.onOperacion_GetPlan(planBean.getClave());

        //Set_Value_Hour_To_Bean
        this.planBean.setHora(planBean.getHoraAplicacion().substring(0, planBean.getHoraAplicacion().indexOf(":")));

        //Set_Value_Minute_To_Bean
        this.planBean.setMinuto(planBean.getHoraAplicacion().substring(planBean.getHoraAplicacion().indexOf(":") + 1, planBean.getHoraAplicacion().lastIndexOf(":")));

    }

    public void loadRutina(RutinaBean rutinaBean) {
        //Write_Console
        //out.print("Load Rutina...");

        //Set_Rutina
        rutina = rutinaBean.getRutina();

        //Write_Console
        //out.print("Rutina seleccionada...".concat(rutina));
        //Get_Rutina_Bean
        this.rutinaBean = cOperacion.onOperacion_GetRutina(rutina);
    }

    public void loadCalProd(CalProdBean calProdBean) throws SQLException {

        //Get_CalProd
        this.calProdBean = calProdBean;

        //Get_Secuencia
        this.secuencia = this.calProdBean.getSecuencia();

        //Get_Estatus
        this.estatus = this.calProdBean.getEstatus();

        //Show_Window_Agregar
        RequestContext.getCurrentInstance().execute("dlgActualizarCalProd.show();");
    }

    public void closeCalProd() {
        //Show_Window_Agregar
        this.calProdBean.setEstatus(estatus);
        RequestContext.getCurrentInstance().execute("dlgActualizarCalProd.hide();");
    }

    public void insertPlan() throws SQLException {
        //Write_Console
        //out.print("Insert Plan...");
        
        //Format_Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Validate_Bean
        if ("".equals(planBean.getClaveAgregar())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Nombre Plan debe contener un valor."));
            return;
        }
        if ("".equals(planBean.getDescripcionAgregar())) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Descripción debe contener un valor."));
            return;
        }
        if (planBean.getFechaAgregar() == null) {
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Fecha Inicio Operación debe contener un valor."));
            return;
        }
        //Write_Console_Values
        //out.print("PLAN: ".concat(planBean.getClaveAgregar()));
        //out.print("DESCRIPCION: ".concat(planBean.getDescripcionAgregar()));
        //out.print("PERIODO: ".concat(planBean.getPeriodoAgregar()));
        //out.print("FECHA: ".concat(planBean.getFechaAgregar().toString()));
        //out.print("HORA: ".concat(planBean.getHoraAgregar()));
        //out.print("MINUTO: ".concat(planBean.getMinutoAgregar()));
        //out.print("ESTATUS: ".concat(planBean.getEstatusAgregar()));
        //Set_Values_To_Bean
        planBean.setClave(planBean.getClaveAgregar());
        planBean.setDescripcion(planBean.getDescripcionAgregar());
        planBean.setPeriodo(planBean.getPeriodoAgregar());
        planBean.setFechaAplicacion(simpleDateFormat.format(planBean.getFechaAgregar()));
        planBean.setHoraAplicacion(planBean.getHoraAgregar().concat(":").concat(planBean.getMinutoAgregar().concat(":00")));
        planBean.setEstatus(planBean.getEstatusAgregar());
        //Insert_Plan
        int insert = cOperacion.onOperacion_InsertPlan(planBean);
        //Count_Insert
        //out.print("Registros insertados: ".concat(String.valueOf(insert)));
        //Validate_Insert
        if (insert >= 0) {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" guardado exitosamente...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            
            //Hide_Window_Agregar
            RequestContext.getCurrentInstance().execute("dlgAgregarPlan.hide();");
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan ".concat(planBean.getClave().toUpperCase().concat(" guardado exitosamente...")), "00", "00", "00");
            
            //Init_Plan
            this.initPlan();
            
        } else {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" no se pudo guardar...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            
            //Hide_Window_Agregar
            RequestContext.getCurrentInstance().execute("dlgAgregarPlan.hide();");
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan ".concat(planBean.getClave().toUpperCase().concat(" no se pudo guardar...")), "00", "00", "00");
            
            //Init_Plan
            this.initPlan();
            
        }
    }

    public void insertRutina() throws SQLException {
        try {
            //Write_Console
            //out.print("Insert Rutina...");

            //Validate_Bean
            if ("".equals(rutinaBean.getRutinaAgregar())) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Identificación debe contener un valor."));
                return;
            }

            if ("".equals(rutinaBean.getNombreAgregar())) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Nombre debe contener un valor."));
                return;
            }

            if ("".equals(rutinaBean.getDescripcionAgregar())) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Descripción debe contener un valor."));
                return;
            }

            //Write_Console_Values
            //out.print("ID: ".concat(rutinaBean.getRutinaAgregar()));
            //out.print("NOMBRE: ".concat(rutinaBean.getNombreAgregar()));
            //out.print("DESCRIPCION: ".concat(rutinaBean.getDescripcionAgregar()));
            //out.print("AREA RESPONSABLE: ".concat(rutinaBean.getAreaResponsableAgregar()));
            //out.print("TIPO PROCESO: ".concat(rutinaBean.getProcesoAgregar()));
            //out.print("PERIODICIDAD: ".concat(rutinaBean.getPeriodoAgregar()));
            //out.print("ESTATUS: ".concat(rutinaBean.getEstatusAgregar()));
            //Set_Values_To_Bean
            rutinaBean.setRutina(rutinaBean.getRutinaAgregar());
            rutinaBean.setNombre(rutinaBean.getNombreAgregar());
            rutinaBean.setDescripcion(rutinaBean.getDescripcionAgregar());
            rutinaBean.setAreaResponsable(rutinaBean.getAreaResponsableAgregar());
            rutinaBean.setProceso(rutinaBean.getProcesoAgregar());
            rutinaBean.setPeriodo(rutinaBean.getPeriodoAgregar());
            rutinaBean.setEstatus(rutinaBean.getEstatusAgregar());

            //Insert_Rutina
            int insert = cOperacion.onOperacion_InsertRutina(rutinaBean);

            //Count_Insert
            //out.print("Registros insertados: ".concat(String.valueOf(insert)));
            //Validate_Insert
            if (insert >= 0) {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina: ".concat(rutinaBean.getRutina().toUpperCase().concat(" guardado exitosamente...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Hide_Window_Agregar
                RequestContext.getCurrentInstance().execute("dlgAgregarRutina.hide();");
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(rutinaBean.getRutina().toUpperCase().concat(" guardado exitosamente...")), "00", "00", "00");

                //Init_Rutina
                this.initRutina();

            } else {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina: ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo guardar...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Hide_Window_Agregar
                RequestContext.getCurrentInstance().execute("dlgAgregarRutina.hide();");
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo guardar...")), "00", "00", "00");

                //Init_Rutina
                this.initRutina();

            }
        } catch (SQLException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto;
        }
    }

    public void deletePlan() throws SQLException {
        //Write_Console
        //out.print("Delete Plan...");
        //out.print("Plan a eliminar:".concat(plan));
        
        //Get_Delete
        int delete = cOperacion.onOperacion_DeletePlan(plan);
        //Write_Console
        //out.print("Registros eliminados: ".concat(String.valueOf(delete)));
        //Validate_Foreign_Key
        if (delete == -532) {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El plan: ".concat(planBean.getClave().toUpperCase()).concat(" no se puede eliminar porque tiene asociada una(s) rutina(s)..."));
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan: ".concat(planBean.getClave().toUpperCase()).concat(" no se puede eliminar porque tiene asociada una(s) rutina(s)..."), "00", "00", "00");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            
            //Init_Plan
            this.initPlan();
            
            return;
        }
        //Validate_Delete
        if (delete >= 0) {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" eliminado exitosamente...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan: ".concat(planBean.getClave().toUpperCase()).concat(" eliminado exitosamente..."), "00", "00", "00");
            
            //Init_Plan
            this.initPlan();
        } else {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" no se pudo eliminar...")));
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan: ".concat(planBean.getClave().toUpperCase()).concat(" no se puede eliminar..."), "00", "00", "00");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
        }
    }

    public void deleteRutina() throws SQLException {
        try {
            //Write_Console
            //out.print("Delete Rutina...");
            //out.print("Rutina a eliminar:".concat(rutina));

            //Get_Delete
            int delete = cOperacion.onOperacion_DeleteRutina(rutina);

            //Validate_Foreign_Key
            if (delete == -532) {
                //Set_Message
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina: ".concat(rutina).concat(" no se puede eliminar porque esta asociada a un plan..."));
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(rutina).concat(" no se puede eliminar porque esta asociada a un plan..."), "00", "00", "00");

                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Init_Rutina
                this.initRutina();
                return;
            }

            //Write_Console
            //out.print("Registros eliminados: ".concat(String.valueOf(delete)));
            //Validate_Delete
            if (delete == 1) {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina ".concat(rutina).concat(" eliminado exitosamente..."));
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(rutina).concat(" eliminado exitosamente..."), "00", "00", "00");
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Init_Rutina
                this.initRutina();
            } else {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina ".concat(rutina).concat(" no se pudo eliminar..."));
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(rutina).concat(" no se pudo eliminar..."), "00", "00", "00");
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Init_Rutina
                this.initRutina();
            }
        } catch (SQLException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto;
        }
    }

    public void updatePlan() throws SQLException {
        //Write_Console
        //out.print("Update Plan...");
        
        //Format_Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Validate_Bean
        if ("".equals(planBean.getClave())) {
            //Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Nombre Plan debe contener un valor."));
            return;
        }
        if ("".equals(planBean.getDescripcion())) {
            //Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Descripción debe contener un valor."));
            return;
        }
        if (planBean.getFecha() == null) {
            //Set_Message
            FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Fecha Inicio Operación debe contener un valor."));
            return;
        }
        //Write_Console_Values
        //out.print("PLAN: ".concat(planBean.getClave()));
        //out.print("DESCRIPCION: ".concat(planBean.getDescripcion()));
        //out.print("PERIODO: ".concat(planBean.getPeriodo()));
        //out.print("FECHA: ".concat(planBean.getFecha().toString()));
        //out.print("HORA: ".concat(planBean.getHora()));
        //out.print("MINUTO: ".concat(planBean.getMinuto()));
        //out.print("ESTATUS: ".concat(planBean.getEstatus()));
        //Convert_Date
        planBean.setFechaAplicacion(simpleDateFormat.format(planBean.getFecha()));
        //Convert_Time
        planBean.setHoraAplicacion(planBean.getHora().concat(":").concat(planBean.getMinuto().concat(":00")));
        //Update_Rutina
        int update = cOperacion.onOperacion_UpdatePlan(planBean);
        //Write_Console
        //out.print("Registros actualizados: ".concat(String.valueOf(update)));
        //Validate_Update
        if (update >= 0) {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" actualizado exitosamente...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            RequestContext.getCurrentInstance().execute("dlgActualizarPlan.hide();");
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan ".concat(planBean.getClave().toUpperCase().concat(" actualizado exitosamente...")), "00", "00", "00");
            
            this.initPlan();
            
        } else {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "El plan ".concat(planBean.getClave().toUpperCase().concat(" no se pudo actualizar...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            
            //Hide_Window_Actualizar
            RequestContext.getCurrentInstance().execute("dlgActualizarPlan.hide();");
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El plan ".concat(planBean.getClave().toUpperCase().concat(" no se pudo actualizar...")), "00", "00", "00");
            
            //Init_Plan
            this.initPlan();
            
        }
    }

    public void updateRutina() throws SQLException {
        try {
            //Write_Console
            //out.print("Update Rutina...");

            //Validate_Bean
            if ("".equals(rutinaBean.getRutina())) {
                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Identificación debe contener un valor."));
                return;
            }

            if ("".equals(rutinaBean.getNombre())) {
                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Nombre debe contener un valor."));
                return;
            }

            if ("".equals(rutinaBean.getDescripcion())) {
                //Set_Message 
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: Descripción debe contener un valor."));
                return;
            }

            //Write_Console_Values
            //out.print("ID: ".concat(rutinaBean.getRutina()));
            //out.print("NOMBRE: ".concat(rutinaBean.getNombre()));
            //out.print("DESCRIPCION: ".concat(rutinaBean.getDescripcion()));
            //out.print("AREA RESPONSABLE: ".concat(rutinaBean.getAreaResponsable()));
            //out.print("TIPO PROCESO: ".concat(rutinaBean.getProceso()));
            //out.print("PERIODICIDAD: ".concat(rutinaBean.getPeriodo()));
            //out.print("ESTATUS: ".concat(rutinaBean.getEstatus()));
            //Update_Rutina
            int update = cOperacion.onOperacion_UpdateRutina(rutinaBean);

            //Write_Console
            //out.print("Registros actualizados: ".concat(String.valueOf(update)));
            //Validate_Update
            if (update >= 0) {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" actualizado exitosamente...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Hide_Window_Actualizar
                RequestContext.getCurrentInstance().execute("dlgActualizarRutina.hide();");
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" actualizado exitosamente...")), "00", "00", "00");

                //Init_Rutina
                this.initRutina();

            } else {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo actualizar...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Hide_Window_Actualizar
                RequestContext.getCurrentInstance().execute("dlgActualizarRutina.hide();");
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo actualizar...")), "00", "00", "00");

                //Init_Rutina
                this.initRutina();

            }
        } catch (SQLException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto;
        }
    }

    public void updateCalProd() throws SQLException {
        try {
            //Write_Console
            //out.print("Update CalProd...");

            //Validate_Bean
            if ("".trim().equals(this.secuencia)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: secuencia debe contener un valor."));
                return;
            }

            if (!isNumeric(this.secuencia)) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: secuencia debe contener un valor numérico."));
                return;
            }

            if (Integer.parseInt(this.secuencia) == 0) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: secuencia debe contener un valor diferente de cero o mayor."));
                return;
            }

            //Write_Console_Values
            //out.print("PLAN: ".concat(calProdBean.getPlan()));
            //out.print("RUTINA: ".concat(calProdBean.getRutina()));
            //out.print("SECUENCIA: ".concat(String.valueOf(calProdBean.getSecuencia())));
            //out.print("SECUENCIA CAPTURADA: ".concat(String.valueOf(this.secuencia)));
            //out.print("ESTATUS: ".concat(calProdBean.getEstatus()));
            if (!String.valueOf(calProdBean.getSecuencia()).equals(this.secuencia)) {
                //Set_Secuencia
                calProdBean.setSecuencia(this.secuencia);

                //Validate_Secuencia
                int exist = cOperacion.onOperacion_ExistSecuencia(calProdBean);

                //Write_Console_Values
                //out.print("EXISTE SECUENCIA: ".concat(String.valueOf(exist)));
                //Exists 
                if (exist > 0) {
                    //Set_Message
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El campo: secuencia ya se encuentra asociado en otra rutina."));
                    //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","El campo: secuencia ya se encuentra asociado en otra rutina.", "00", "00", "00");

                    //Set_Rutinas_In_CalProd
                    this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

                    //Hide_Window_Actualizar
                    RequestContext.getCurrentInstance().execute("dlgActualizarCalProd.hide();");

                    return;
                }
            }

            //Set_Secuencia
            calProdBean.setSecuencia(this.secuencia);

            //Update_CalProd
            int update = cOperacion.onOperacion_UpdateCalProd(calProdBean);

            if (update == -1) {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina: ".concat(calProdBean.getRutina().toUpperCase().concat(" ya tiene asignada esta secuencia: ".concat(String.valueOf(calProdBean.getSecuencia())))));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Disabled_Button
                habilitaBotonAsignar = 0;
                habilitaBotonDesasignar = 0;
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina: ".concat(calProdBean.getRutina().toUpperCase().concat(" ya tiene asignada esta secuencia: ".concat(String.valueOf(calProdBean.getSecuencia())))), "00", "00", "00");

                //Set_Rutinas_Not_In_CalProd
                this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

                //Set_Rutinas_In_CalProd
                this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

            }

            //Write_Console
            //out.print("Registros actualizados: ".concat(String.valueOf(update)));
            //Validate_Update
            if (update >= 0) {
                //Set_Message 
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina: ".concat(calProdBean.getRutina().toUpperCase().concat(" actualizado exitosamente...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Disabled_Button
                habilitaBotonAsignar = 0;
                habilitaBotonDesasignar = 0;
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" actualizado exitosamente...")), "00", "00", "00");

                //Set_Rutinas_Not_In_CalProd
                this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

                //Set_Rutinas_In_CalProd
                this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

                //Set_Bean
                this.calProdBean = new CalProdBean();

                //Hide_Window_Actualizar
                RequestContext.getCurrentInstance().execute("dlgActualizarCalProd.hide();");

            } else {
                //Set_Message
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina: ".concat(calProdBean.getRutina().toUpperCase().concat(" no se pudo actualizar...")));
                FacesContext.getCurrentInstance().addMessage(null, mensaje);

                //Disabled_Button
                habilitaBotonAsignar = 0;
                habilitaBotonDesasignar = 0;
                //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo actualizar...")), "00", "00", "00");

                //Set_Rutinas_Not_In_CalProd
                this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

                //Set_Rutinas_In_CalProd
                this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));
                this.calProdBean = new CalProdBean();

                //Hide_Window_Actualizar
                RequestContext.getCurrentInstance().execute("dlgActualizarCalProd.hide();");

            }
        } catch (NumberFormatException Err) {
            mensajeError += "Descripcion: " + Err.getMessage() + nombreObjeto;
        }
    }

    public void selectPlan() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formOperacion:panelOperacion:dtRutinasCalProd");
        dataTable.reset();
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.clearSelection()");
        RequestContext.getCurrentInstance().execute("dtCalProd.clearSelection()");

        //Get_Value
        planCalProd = calProdBean.getPlan();

        //Write_Console
        //out.print("PLAN SELECCIONADO: ".concat(planCalProd));
        //Disabled_CommandButton_Asignar
        habilitaBotonAsignar = 0;

        //Disabled_CommandButton_Desasignar
        habilitaBotonDesasignar = 0;

        //Set_Rutinas_Not_In_CalProd
        this.setRutinasCalProd(null);
        this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

        //Set_Rutinas_In_CalProd
        this.setRutinasInCalprod(null);
        this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

    }

    public void onRowSelect(SelectEvent event) {
        //Get_Rutina
        rutinaCalProd = (String) event.getObject();

        //Write_Console
        //out.print("RUTINA SELECCIONADA: ".concat(rutinaCalProd));
        //Enabled_CommandButton_Asignar
        habilitaBotonAsignar = 1;
        habilitaBotonDesasignar = 0;
        RequestContext.getCurrentInstance().execute("dtCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtCalProd.clearSelection()");
    }

    public void onRowSelectCalProd(SelectEvent event) {
        //Get_Rutina
        calProdBean = (CalProdBean) event.getObject();

        //Write_Console
        //out.print("RUTINA SELECCIONADA CALPROD: ".concat(calProdBean.getRutina()));
        //Enabled_CommandButton_Desasignar
        habilitaBotonDesasignar = 1;
        habilitaBotonAsignar = 0;
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.clearSelection()");
    }

    public void add() {
        //Write_Console
        //out.print("Add CalProd...");

        //Write_Console_Values
        //out.print("PLAN: ".concat(planCalProd));
        //out.print("RUTINA: ".concat(rutinaCalProd));
        //Set_Values_To_Bean
        calProdBean.setPlan(planCalProd);
        calProdBean.setRutina(rutinaCalProd);
        calProdBean.setEstatus(estatus);

        //Add_CalProd
        int add = cOperacion.onOperacion_InsertCalProd(planCalProd, rutinaCalProd);

        //Count_Add
        //out.print("Registros agregados: ".concat(String.valueOf(add)));
        //Validate_Add
        if (add >= 0) {
            //Set_Message 
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina: ".concat(rutinaCalProd.toUpperCase().concat(" se agrego exitosamente...")));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);

            //Disable_Buttons
            habilitaBotonAsignar = 0;
            habilitaBotonDesasignar = 0;

            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" se agrego exitosamente...")), "00", "00", "00");
            //Set_Rutinas_Not_In_CalProd
            this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

            //Set_Rutinas_In_CalProd
            this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

            //Set_Bean
            this.calProdBean = new CalProdBean();

        } else {
            //Set_Message 
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina: ".concat(rutinaCalProd.toUpperCase().concat(" no se pudo agregar...")));

            FacesContext.getCurrentInstance().addMessage(null, mensaje);

            //Disabled_Buttons
            habilitaBotonAsignar = 0;
            habilitaBotonDesasignar = 0;

            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo agregar...")), "00", "00", "00");
            //Set_Rutinas_Not_In_CalProd
            this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(planCalProd));

            //Set_Rutinas_In_CalProd
            this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(planCalProd));

            //Set_Bean
            this.calProdBean = new CalProdBean();

        }
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.clearSelection()");
        RequestContext.getCurrentInstance().execute("dtCalProd.clearSelection()");
    }

    public void minus() {
        //Write_Console
        //out.print("Minus Rutina...");
        //out.print("CalProd a eliminar:".concat(calProdBean.getPlan()).concat(" -> ").concat(calProdBean.getRutina()));

        //Get_Minus
        int minus = cOperacion.onOperacion_DeleteCalProd(calProdBean.getPlan(), calProdBean.getRutina());

        //Write_Console
        //out.print("Registros desasignados: ".concat(String.valueOf(minus)));
        //Validate_Minus
        if (minus >= 0) {
            //Set_Message 
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "La rutina ".concat(calProdBean.getRutina()).concat(" se desasigno exitosamente..."));
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" se desasigno exitosamente...")), "00", "00", "00");
            //Disabled_Buttons
            habilitaBotonDesasignar = 0;
            habilitaBotonAsignar = 0;

            //Set_Rutinas_Not_In_CalProd
            this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(calProdBean.getPlan()));

            //Set_Rutinas_In_CalProd
            this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(calProdBean.getPlan()));

            //Set_Bean
            this.calProdBean = new CalProdBean();
        } else {
            //Set_Message
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fiduciario", "La rutina ".concat(calProdBean.getRutina()).concat(" no se pudo desasignar..."));

            FacesContext.getCurrentInstance().addMessage(null, mensaje);

            //Disabled_Buttons
            habilitaBotonDesasignar = 0;
            habilitaBotonAsignar = 0;
            //oGenerLog.onGeneraLog(MBOperacion.class, "0E", "INFO", "00", "00","La rutina ".concat(rutinaBean.getRutina().toUpperCase().concat(" no se pudo desasignar...")), "00", "00", "00");
            //Set_Rutinas_Not_In_CalProd
            this.setRutinasCalProd(cOperacion.onOperacion_GetRutinasNotInCalProd(calProdBean.getPlan()));

            //Set_Rutinas_In_CalProd
            this.setRutinasInCalprod(cOperacion.onOperacion_GetRutinasInCalProd(calProdBean.getPlan()));

            //Set_Bean
            this.calProdBean = new CalProdBean();

        }
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtCalProd.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("dtRutinasCalProd.clearSelection()");
        RequestContext.getCurrentInstance().execute("dtCalProd.clearSelection()");
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {

            return false;
        }
    }

}
