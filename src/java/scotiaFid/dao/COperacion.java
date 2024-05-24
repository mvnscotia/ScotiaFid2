/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : COperacion.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.dao;

//Imports
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.CalProdBean;
import scotiaFid.bean.PlanBean;
import scotiaFid.bean.RutinaBean;

import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

public class COperacion implements Serializable {

    private static final Logger logger = LogManager.getLogger(COperacion.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * S E R I A L
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * O B J E T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Connection conexion;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String nombreObjeto;
    private Boolean valorRetorno;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public COperacion() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CHonorarios.";
        valorRetorno = Boolean.FALSE;
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<PlanBean> onOperacion_GetPlanes() {
        //Objects
        List<PlanBean> planes = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PLA_CVE_FASE_PROD, PLA_DESC_FASE_PROD, PLA_CVE_PER_APLICA, TO_CHAR(PLA_FEC_APLICACION,'DD/MM/YYYY') AS FECHA, PLA_HORA_APLICA, PLA_CVE_ST_PLAN ");
            stringBuilder.append("FROM SAF.PLANES ORDER BY PLA_CVE_FASE_PROD ");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //Object
                    PlanBean planBean = new PlanBean();

                    //Set_Values
                    planBean.setClave(resultSet.getString("PLA_CVE_FASE_PROD"));
                    planBean.setDescripcion(resultSet.getString("PLA_DESC_FASE_PROD"));
                    planBean.setPeriodo(resultSet.getString("PLA_CVE_PER_APLICA"));
                    planBean.setFechaAplicacion(resultSet.getString("FECHA"));
                    planBean.setHoraAplicacion(resultSet.getString("PLA_HORA_APLICA"));
                    planBean.setEstatus(resultSet.getString("PLA_CVE_ST_PLAN"));

                    //Add_List
                    planes.add(planBean);
                }
            }
        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return planes;
    }

    public PlanBean onOperacion_GetPlan(String plan) {
        //Object
        PlanBean planBean = new PlanBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PLA_CVE_FASE_PROD, PLA_DESC_FASE_PROD, PLA_CVE_PER_APLICA, PLA_FEC_APLICACION, PLA_HORA_APLICA, PLA_CVE_ST_PLAN ");
            stringBuilder.append("FROM SAF.PLANES WHERE PLA_CVE_FASE_PROD = ? ");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());
            //Set_Values
            preparedStatement.setString(1, plan);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //Set_Values_Bean
                    planBean.setClave(resultSet.getString("PLA_CVE_FASE_PROD"));
                    planBean.setDescripcion(resultSet.getString("PLA_DESC_FASE_PROD"));
                    planBean.setPeriodo(resultSet.getString("PLA_CVE_PER_APLICA"));
                    planBean.setFecha(java.sql.Date.valueOf(resultSet.getString("PLA_FEC_APLICACION")));
                    planBean.setFechaAplicacion(resultSet.getString("PLA_FEC_APLICACION"));
                    planBean.setHoraAplicacion(resultSet.getString("PLA_HORA_APLICA"));
                    planBean.setEstatus(resultSet.getString("PLA_CVE_ST_PLAN"));
                }
            }
        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return planBean;
    }

    public int onOperacion_InsertPlan(PlanBean planBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Variables
        int result = 0;

        //Set_Date
        LocalDate localDate = LocalDate.now();//For reference
        String date = localDate.format(dateTimeformatter);

        try {
            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.PLANES (PLA_CVE_FASE_PROD, PLA_DESC_FASE_PROD, PLA_CVE_PER_APLICA, PLA_CVE_CH_CONTROL, PLA_FEC_APLICACION, PLA_HORA_APLICA, PLA_FEC_ALTA_REG, PLA_FEC_ULT_MOD, PLA_CVE_ST_PLAN)");
            stringBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, planBean.getClave().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, planBean.getDescripcion().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(3, planBean.getPeriodo());
            preparedStatement.setInt(4, 0);
            preparedStatement.setDate(5, java.sql.Date.valueOf(planBean.getFechaAplicacion()));
            preparedStatement.setTime(6, java.sql.Time.valueOf(planBean.getHoraAplicacion()));
            preparedStatement.setDate(7, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            preparedStatement.setDate(8, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            preparedStatement.setString(9, planBean.getEstatus());

            //Execute_Update
            result = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            try {
                conexion.close();
                preparedStatement.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();

            }
        }

        return result;
    }

    public int onOperacion_DeletePlan(String plan) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("DELETE FROM SAF.PLANES WHERE PLA_CVE_FASE_PROD = ?");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public int onOperacion_UpdatePlan(PlanBean planBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("UPDATE SAF.PLANES SET PLA_DESC_FASE_PROD = ?, PLA_CVE_PER_APLICA = ?, PLA_FEC_APLICACION = ?, PLA_HORA_APLICA = ?, PLA_CVE_ST_PLAN = ? WHERE PLA_CVE_FASE_PROD = ?");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, planBean.getDescripcion().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, planBean.getPeriodo().toUpperCase(Locale.ENGLISH));
            preparedStatement.setDate(3, java.sql.Date.valueOf(planBean.getFechaAplicacion()));
            preparedStatement.setTime(4, java.sql.Time.valueOf(planBean.getHoraAplicacion()));
            preparedStatement.setString(5, planBean.getEstatus().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(6, planBean.getClave().toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();

            }
            //	   onCierraConexion();
        }

        return result;
    }

    public List<RutinaBean> onOperacion_GetRutinas() {
        //Objects
        List<RutinaBean> rutinas = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT RUT_ID_RUTINA, RUT_NOM_RUTINA, RUT_DESC_RUTINA, RUT_CVE_AREA_RESP, RUT_CVE_PROCESO, RUT_CVE_PERIOD, RUT_CVE_ST_RUTINAS ");
            stringBuilder.append("FROM SAF.RUTINAS ORDER BY RUT_ID_RUTINA ");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //Object
                    RutinaBean rutinaBean = new RutinaBean();

                    //Set_Values
                    rutinaBean.setRutina(resultSet.getString("RUT_ID_RUTINA"));
                    rutinaBean.setNombre(resultSet.getString("RUT_NOM_RUTINA"));
                    rutinaBean.setDescripcion(resultSet.getString("RUT_DESC_RUTINA"));
                    rutinaBean.setAreaResponsable(resultSet.getString("RUT_CVE_AREA_RESP"));
                    rutinaBean.setProceso(resultSet.getString("RUT_CVE_PROCESO"));
                    rutinaBean.setPeriodo(resultSet.getString("RUT_CVE_PERIOD"));
                    rutinaBean.setEstatus(resultSet.getString("RUT_CVE_ST_RUTINAS"));

                    //Add_List
                    rutinas.add(rutinaBean);
                }
            }
        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return rutinas;
    }

    public RutinaBean onOperacion_GetRutina(String plan) {
        //Object
        RutinaBean rutinaBean = new RutinaBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT RUT_ID_RUTINA, RUT_NOM_RUTINA, RUT_DESC_RUTINA, RUT_CVE_AREA_RESP, RUT_CVE_PROCESO, RUT_CVE_PERIOD, RUT_CVE_ST_RUTINAS ");
            stringBuilder.append("FROM SAF.RUTINAS WHERE RUT_ID_RUTINA = ? ");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase());

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //Set_Values_Bean
                    rutinaBean.setRutina(resultSet.getString("RUT_ID_RUTINA"));
                    rutinaBean.setNombre(resultSet.getString("RUT_NOM_RUTINA"));
                    rutinaBean.setDescripcion(resultSet.getString("RUT_DESC_RUTINA"));
                    rutinaBean.setAreaResponsable(resultSet.getString("RUT_CVE_AREA_RESP"));
                    rutinaBean.setProceso(resultSet.getString("RUT_CVE_PROCESO"));
                    rutinaBean.setPeriodo(resultSet.getString("RUT_CVE_PERIOD"));
                    rutinaBean.setEstatus(resultSet.getString("RUT_CVE_ST_RUTINAS"));
                }
            }
            preparedStatement.close();
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();

            }
        }

        return rutinaBean;
    }

    public int onOperacion_InsertRutina(RutinaBean rutinaBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        //Set_Date
        LocalDate localDate = LocalDate.now();

        try {
            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.RUTINAS (RUT_ID_RUTINA, RUT_NOM_RUTINA, RUT_DESC_RUTINA, RUT_CVE_AREA_RESP, RUT_CVE_PROCESO, RUT_CVE_PERIOD, RUT_CVE_IMPRESION, RUT_CVE_PAPELERIA, RUT_CVE_RESP_DATOS, RUT_NUM_USUARIO, RUT_CVE_BLOQUEO, RUT_TEXTO_BLOQUEO, RUT_FEC_INIC_BLOQ, RUT_FEC_FIN_BLOQ, RUT_ANO_ALTA_REG, RUT_MES_ALTA_REG, RUT_DIA_ALTA_REG, RUT_ANO_ULT_MOD, RUT_MES_ULT_MOD, RUT_DIA_ULT_MOD, RUT_CVE_ST_RUTINAS )");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, rutinaBean.getRutina().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, rutinaBean.getNombre().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(3, rutinaBean.getDescripcion().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(4, rutinaBean.getAreaResponsable());
            preparedStatement.setString(5, rutinaBean.getProceso());
            preparedStatement.setString(6, rutinaBean.getPeriodo());
            preparedStatement.setInt(7, 0);
            preparedStatement.setString(8, "0");
            preparedStatement.setInt(9, 0);
            preparedStatement.setInt(10, 0);
            preparedStatement.setInt(11, 0);
            preparedStatement.setString(12, "0");
            preparedStatement.setString(13, "0");
            preparedStatement.setString(14, "0");
            preparedStatement.setInt(15, localDate.getYear());
            preparedStatement.setInt(16, localDate.getMonthValue());
            preparedStatement.setInt(17, localDate.getDayOfMonth());
            preparedStatement.setInt(18, localDate.getYear());
            preparedStatement.setInt(19, localDate.getMonthValue());
            preparedStatement.setInt(20, localDate.getDayOfMonth());
            preparedStatement.setString(21, rutinaBean.getEstatus());

            //Execute_Update
            result = preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();

            }
        }

        return result;
    }

    public int onOperacion_DeleteRutina(String rutina) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("DELETE FROM SAF.RUTINAS WHERE RUT_ID_RUTINA = ?");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, rutina.toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public int onOperacion_UpdateRutina(RutinaBean rutinaBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Set_Date
        LocalDate localDate = LocalDate.now();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("UPDATE SAF.RUTINAS SET RUT_NOM_RUTINA = ?, RUT_DESC_RUTINA = ?, RUT_CVE_AREA_RESP = ?, RUT_CVE_PROCESO = ?, RUT_CVE_PERIOD = ?, RUT_ANO_ULT_MOD = ?, RUT_MES_ULT_MOD = ?, RUT_DIA_ULT_MOD = ?, RUT_CVE_ST_RUTINAS = ? WHERE RUT_ID_RUTINA = ? ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, rutinaBean.getNombre().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, rutinaBean.getDescripcion().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(3, rutinaBean.getAreaResponsable().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(4, rutinaBean.getProceso().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(5, rutinaBean.getPeriodo().toUpperCase(Locale.ENGLISH));
            preparedStatement.setInt(6, localDate.getYear());
            preparedStatement.setInt(7, localDate.getMonthValue());
            preparedStatement.setInt(8, localDate.getDayOfMonth());
            preparedStatement.setString(9, rutinaBean.getEstatus());
            preparedStatement.setString(10, rutinaBean.getRutina().toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();
            }
        }

        return result;
    }

    public List<String> onOperacion_GetClaveAreaResponsable() {
        //Objects
        List<String> areas = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT CVE_DESC_CLAVE FROM CLAVES WHERE CVE_NUM_CLAVE = ? ORDER BY CVE_DESC_CLAVE");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setInt(1, 43);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet != null) {
                //Set_Area_Responsable
                while (resultSet.next()) {
                    //Set_Values
                    String areaResponsable = resultSet.getString("CVE_DESC_CLAVE");

                    //Add_List
                    areas.add(areaResponsable);
                }
            }

        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return areas;
    }

    public List<String> onOperacion_GetPlanesInCalProd() {
        //Objects
        List<String> planes = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PLA_CVE_FASE_PROD FROM SAF.PLANES WHERE PLA_CVE_ST_PLAN IN (?,?) ORDER BY PLA_CVE_FASE_PROD");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, "ACTIVO");
            preparedStatement.setString(2, "CONTROLM");

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet != null) {
                //Set_Planes
                while (resultSet.next()) {
                    //Set_Plan
                    String plan = resultSet.getString("PLA_CVE_FASE_PROD");

                    //Add_List
                    planes.add(plan);
                }
            }

        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return planes;
    }

    public List<String> onOperacion_GetRutinasNotInCalProd(String plan) {
        //Objects
        List<String> rutinas = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT RUT_ID_RUTINA FROM RUTINAS WHERE RUT_CVE_ST_RUTINAS = ? AND RUT_ID_RUTINA NOT IN (");
            stringBuilder.append("SELECT CAL_ID_RUTINA FROM CALPROD WHERE CAL_CVE_FASE_PROD IN (?)) ORDER BY RUT_ID_RUTINA");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, "ACTIVO");
            preparedStatement.setString(2, plan.toUpperCase(Locale.ENGLISH));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet != null) {
                //Set_Rutinas
                while (resultSet.next()) {
                    //Set_Rutina
                    String rutina = resultSet.getString("RUT_ID_RUTINA");

                    //Add_List
                    rutinas.add(rutina);
                }
            }

        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return rutinas;
    }

    public List<CalProdBean> onOperacion_GetRutinasInCalProd(String plan) {
        //Objects
        List<CalProdBean> rutinas = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT CAL_CVE_FASE_PROD, CAL_ID_RUTINA, CAL_NUM_SECUENCIA, CAL_CVE_ST_CALPROD FROM CALPROD WHERE CAL_CVE_FASE_PROD IN (?) ORDER BY CAL_NUM_SECUENCIA");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase(Locale.ENGLISH));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet != null) {
                //Set_Rutinas
                while (resultSet.next()) {
                    //Bean
                    CalProdBean calProdBean = new CalProdBean();

                    //Set_Bean
                    calProdBean.setPlan(resultSet.getString("CAL_CVE_FASE_PROD"));
                    calProdBean.setRutina(resultSet.getString("CAL_ID_RUTINA"));
                    calProdBean.setSecuencia(resultSet.getString("CAL_NUM_SECUENCIA"));
                    calProdBean.setEstatus(resultSet.getString("CAL_CVE_ST_CALPROD"));

                    //Add_List
                    rutinas.add(calProdBean);
                }
            }

        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return rutinas;
    }

    public int onOperacion_InsertCalProd(String plan, String rutina) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Variables
        int result = 0;
        int secuencia = 0;

        //Set_Date
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(dateTimeformatter);

        try {
            //Get_Secuencia
            secuencia = this.onOperacion_GetSecuenciaCalProd(plan);

            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.CALPROD (CAL_CVE_FASE_PROD, CAL_ID_RUTINA, CAL_NUM_SECUENCIA, CAL_CVE_PROCESADO, CAL_FEC_ALTA_REG, CAL_FEC_ULT_MOD, CAL_CVE_ST_CALPROD )");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?, ?) ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, rutina.toUpperCase(Locale.ENGLISH));
            preparedStatement.setInt(3, secuencia);
            preparedStatement.setInt(4, 0);
            preparedStatement.setDate(5, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            preparedStatement.setDate(6, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            preparedStatement.setString(7, "ACTIVO");

            //Execute_Update
            result = preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public int onOperacion_DeleteCalProd(String plan, String rutina) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("DELETE FROM SAF.CALPROD WHERE CAL_CVE_FASE_PROD = ? AND CAL_ID_RUTINA = ? ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(2, rutina.toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public int onOperacion_UpdateCalProd(CalProdBean calProdBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_Date
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(dateTimeformatter);

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("UPDATE SAF.CALPROD SET CAL_NUM_SECUENCIA = ?, CAL_CVE_ST_CALPROD = ?, CAL_FEC_ULT_MOD = ? WHERE CAL_CVE_FASE_PROD = ? AND CAL_ID_RUTINA = ? ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, calProdBean.getSecuencia());
            preparedStatement.setString(2, calProdBean.getEstatus().toUpperCase(Locale.ENGLISH));
            preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            preparedStatement.setString(4, calProdBean.getPlan().toUpperCase(Locale.ENGLISH));
            preparedStatement.setString(5, calProdBean.getRutina().toUpperCase(Locale.ENGLISH));

            //Execute_Update
            result = preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public int onOperacion_ExistSecuencia(CalProdBean calProdBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("SELECT COUNT(*) AS EXISTE_SECUENCIA FROM SAF.CALPROD WHERE CAL_CVE_FASE_PROD = ? AND CAL_NUM_SECUENCIA = ?");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, calProdBean.getPlan().toUpperCase(Locale.ENGLISH).trim());
            preparedStatement.setInt(2, Integer.parseInt(calProdBean.getSecuencia()));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet.next()) {
                //Validate_Secuencia
                result = resultSet.getInt("EXISTE_SECUENCIA");
            }
            preparedStatement.close();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onOperacion_GetSecuenciaCalProd()");

        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                mensajeError += "Error al cerrar la conexión: " + e.getMessage();

            }
        }

        return result;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
   * F U N C I O N E S   P R I V A D A S
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private int onOperacion_GetSecuenciaCalProd(String plan) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int secuencia = 0;

        try {
            //SQL 
            stringBuilder.append("SELECT NVL(MAX(CAL_NUM_SECUENCIA),0) + 1 AS SECUENCIA FROM CALPROD WHERE CAL_CVE_FASE_PROD = ?");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setString(1, plan.toUpperCase(Locale.ENGLISH));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Data
            if (resultSet != null) {
                //Set_No_Secuencia
                if (resultSet.next()) {
                    //Set_Count
                    secuencia = resultSet.getInt("SECUENCIA");
                }
            }

        } catch (SQLException Err){
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return secuencia;
    }

    private void onCierraConexion() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar ResultSet");
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar PreparedStatement");
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar la Conexion");
            }
        }
    }
}
