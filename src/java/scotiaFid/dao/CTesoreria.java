/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CTesoreria.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.dao;

//Imports
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.ArchivoOtrosBean;
import scotiaFid.bean.BitacoraBean;
import scotiaFid.bean.CargaInterfazBean;
import scotiaFid.bean.CompraVentaBean;
import scotiaFid.bean.ConreporBean;
import scotiaFid.bean.ContratoBean;
import scotiaFid.bean.EmisionBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.InvRechazoCtoInvBean;
import scotiaFid.bean.InvRechazosBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.PosicionBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

//Class
public class CTesoreria implements Serializable {

    private static final Logger logger = LogManager.getLogger(CTesoreria.class);
    // Serial
    private static final long serialVersionUID = 1L;

    private static HttpServletRequest peticion;

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * A T R I B U T O S  P R I V A D O S  V I S I B L E S * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * *
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DecimalFormat decimalFormatTit = new DecimalFormat("#,###,###,###,###,##0");
    private static final DecimalFormat decimalFormatImp = new DecimalFormat("#,###,###,###,###,##0.0#");
    private static final DecimalFormat decimalFormatTasa = new DecimalFormat("#,###,###,###,###,##0.0#######");
    private static final DecimalFormat decimalFormatTP = new DecimalFormat("###,###,###,###,###,##0.0#######");
    private static final DecimalFormat decimalFormatPre = new DecimalFormat("###,###,###,###,###,###,##0.0########");

    private String mensajeError;
    // **************************************************************************** 
    private String nombreObjeto;
    // **************************************************************************** 
    private Connection conexion;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private CallableStatement callableStatement;
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * B E A N S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    BitacoraBean bitacoraBean = new BitacoraBean();
    // ****************************************************************************

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
     * F O R M A T O S
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // -----------------------------------------------------------------------------
    private static DecimalFormat formatoDecimal2D = new DecimalFormat("###,###.##");

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * M E T O D O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * C O N S T R U C T O R * * * * * * * * * * * * * * * * * * * * * * * * * *

	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    public CTesoreria() {
        mensajeError = "Error En Tiempo de Ejecución: ";
        nombreObjeto = " Fuente: scotiafid.dao.CTesoreria.";
        formatoDecimal2D.setMaximumFractionDigits(2);
        formatoDecimal2D.setMinimumFractionDigits(2);
        peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        LogsContext.FormatoNormativo();
    }

    public List<CargaInterfazBean> onTesoreria_ConsultaInterfaz(String sRutina, Date dFecha, String sPath,
            String sNomArch) {

        StringBuilder stringBuilder = new StringBuilder();
        List<CargaInterfazBean> cargaInterfaz = new ArrayList<>();

        java.sql.Date dateFec = new java.sql.Date(dFecha.getTime());

        try {
            // SQL
            stringBuilder.append("SELECT CARINT_SEC_ARCH, CARINT_SECUENCIAL, CARINT_CADENA, CARINT_ESTATUS,CARINT_MENSAJE,CARINT_NOM_ARCH");
            stringBuilder.append(" FROM SAF.CARGA_INTERFAZ");
            stringBuilder.append(" WHERE RUT_ID_RUTINA = ? ");
            stringBuilder.append(" AND CARINT_FECHA = ? ");
            stringBuilder.append(" AND CARINT_NOM_PATH = ?");
            stringBuilder.append(" AND CARINT_NOM_ARCH = ?");
            stringBuilder.append(" AND CARINT_NUM_USUARIO = ?");
            stringBuilder.append(" ORDER BY CARINT_SEC_ARCH, CARINT_SECUENCIAL ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sRutina);
            preparedStatement.setDate(2, dateFec);
            preparedStatement.setString(3, sPath);
            preparedStatement.setString(4, sNomArch);
            //08092023 Se agrega filtro de usuario para carga interfaz
            preparedStatement.setInt(5, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {
                    CargaInterfazBean cargaBean = new CargaInterfazBean();

                    // Set_Values
                    cargaBean.setSecuencialArchivo(resultSet.getInt("CARINT_SEC_ARCH"));
                    cargaBean.setSecuencial(resultSet.getInt("CARINT_SECUENCIAL"));
                    cargaBean.setCadena(resultSet.getString("CARINT_CADENA"));
                    cargaBean.setEstatus(resultSet.getString("CARINT_ESTATUS"));
                    cargaBean.setMensaje(resultSet.getString("CARINT_MENSAJE"));
                    cargaBean.setNombreArchivo(resultSet.getString("CARINT_NOM_ARCH"));

                    // Add_List
                    cargaInterfaz.add(cargaBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return cargaInterfaz;
    }

    public int onTesoreria_existeCargaInterfaz(String sRutina, Date dFecha) {

        int secArc = 0;
        StringBuilder stringBuilder = new StringBuilder();

        java.sql.Date dateFec = new java.sql.Date(dFecha.getTime());

        try {
            // SQL
            stringBuilder.append("SELECT MAX(CARINT_SEC_ARCH) as CONT ");
            stringBuilder.append(" FROM SAF.CARGA_INTERFAZ");
            stringBuilder.append(" WHERE RUT_ID_RUTINA = ? ");
            stringBuilder.append(" AND CARINT_FECHA = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sRutina);
            preparedStatement.setDate(2, dateFec);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {
                secArc = resultSet.getInt("CONT");
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return secArc;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * C O M P R A S  Y  V E N T A S * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * *
     */
    public Map<String, String> onTesoreria_GetCtoInv(String fideicomiso, String subFiso, int Intermed, String sOpcion) {

        StringBuilder stringBuilder = new StringBuilder();
        String Nom_Interm = "", Num_CtoInterm = "", Interm = "", TipoIntermed = "";
        Map<String, String> ctoInv = new LinkedHashMap<String, String>();

        try {
            // SQL
            if (sOpcion.equals("C") || sOpcion.equals("V") || sOpcion.equals("E") || sOpcion.equals("S")) {
                stringBuilder.append(" SELECT INT_INTERMEDIARIO, CPR_CONTRATO_INTER,  CPR_ENTIDAD_FIN,  INT_TIPO_INTERMED ");
                stringBuilder.append("FROM SAF.CONTINTE, SAF.INTERMED ");
                stringBuilder.append("WHERE CPR_ENTIDAD_FIN = INT_ENTIDAD_FIN ");
                stringBuilder.append("AND CPR_NUM_CONTRATO = ? ");
                stringBuilder.append("AND CPR_SUB_CONTRATO= ? ");
                stringBuilder.append("AND CPR_CVE_ST_CONTINT ='ACTIVO' ");
                stringBuilder.append("ORDER BY INT_INTERMEDIARIO,CPR_CONTRATO_INTER ");
            }

            if (sOpcion.equals("CR")) {
                stringBuilder.append(" SELECT INT_INTERMEDIARIO, CPR_CONTRATO_INTER, CPR_ENTIDAD_FIN, INT_TIPO_INTERMED ");
                stringBuilder.append(" FROM SAF.CONTINTE, SAF.INTERMED ");
                stringBuilder.append(" WHERE CPR_ENTIDAD_FIN = INT_ENTIDAD_FIN ");
                stringBuilder.append(" AND CPR_NUM_CONTRATO = ? ");
                stringBuilder.append(" AND CPR_SUB_CONTRATO = ? ");
                stringBuilder.append(" AND CPR_CVE_ST_CONTINT ='ACTIVO' ");
                stringBuilder.append(" AND INT_TIPO_INTERMED = 0 ");
                stringBuilder.append(" ORDER BY INT_INTERMEDIARIO,CPR_CONTRATO_INTER");
            }

            if (sOpcion.equals("CP")) {
                stringBuilder.append(" SELECT INT_INTERMEDIARIO, CPR_CONTRATO_INTER, CPR_ENTIDAD_FIN, INT_TIPO_INTERMED ");
                stringBuilder.append(" FROM SAF.CONTINTE, SAF.INTERMED ");
                stringBuilder.append(" WHERE CPR_ENTIDAD_FIN = INT_ENTIDAD_FIN ");
                stringBuilder.append("AND CPR_NUM_CONTRATO = ? ");
                stringBuilder.append("AND CPR_SUB_CONTRATO = ? ");
                stringBuilder.append("AND CPR_CVE_ST_CONTINT ='ACTIVO' ");
                stringBuilder.append("AND INT_TIPO_INTERMED = 1 ");
                stringBuilder.append("ORDER BY INT_INTERMEDIARIO,CPR_CONTRATO_INTER");
            }

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(fideicomiso));
            preparedStatement.setInt(2, Integer.parseInt(subFiso));

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                // Vacio_el_primero
                ctoInv.put(String.valueOf(0), "");

                while (resultSet.next()) {
                    // Add_List
                    Nom_Interm = resultSet.getString("INT_INTERMEDIARIO");
                    Num_CtoInterm = resultSet.getString("CPR_CONTRATO_INTER");
                    Interm = resultSet.getString("CPR_ENTIDAD_FIN");
                    TipoIntermed = resultSet.getString("INT_TIPO_INTERMED");
                    ctoInv.put(Num_CtoInterm + "/" + Interm + "/" + TipoIntermed,
                            "CTO. INV / " + Num_CtoInterm + " / " + Nom_Interm + " / " + Interm);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return ctoInv;
    }

    public String onTesoreria_GetCtaCheques(String fideicomiso, String subFiso, int Interm, double CtoInv) {
        StringBuilder stringBuilder = new StringBuilder();
        String CtaCheque = "No existe contrato de Inversión";;

        try {
            // SQL
            stringBuilder.append("SELECT DB2FIDUC.FUN_GET_CTACHEQUES(?,?,?,?) CHEQUERA FROM FECCONT");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(fideicomiso));
            preparedStatement.setInt(2, Integer.parseInt(subFiso));
            preparedStatement.setInt(3, Interm);
            preparedStatement.setDouble(4, CtoInv);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    CtaCheque = resultSet.getString("CHEQUERA");
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return CtaCheque;
    }

    public Map<String, String> onTesoreria_GetTipoValor(ArrayList<Integer> sClaves) {
        int NumInstrume = 0;
        String[] Div_Instrume;
        StringBuilder QuerySql = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        Map<String, String> tipoValor = new LinkedHashMap<String, String>();

        try {
            // SQL
            QuerySql.append("SELECT INS_MNEMO_INSTRUME, INS_CVE_TIPO_MERCA, INS_NUM_INSTRUME, CVE_DESC_CLAVE, INS_NOM_INSTRUME ");
            QuerySql.append("FROM SAF.INSTRUME ");
            QuerySql.append("INNER JOIN SAF.CLAVES ");
            QuerySql.append("ON CVE_NUM_CLAVE = 45 ");
            QuerySql.append("AND CVE_NUM_SEC_CLAVE = INS_CVE_TIPO_MERCA ");
            QuerySql.append("WHERE INS_CVE_ST_INSTRUM ='ACTIVO' ");

            // _Validar_si_existe_una_condicion_adicional_para_el_Tipo_de_Valor
            if (sClaves.size() > 0) {

                for (int i = 0; i < sClaves.size(); i++) {
                    //Cantidad_de_?_parametros
                    builder.append("?,");
                }

                QuerySql.append(" AND INS_MNEMO_INSTRUME IN (" + builder.deleteCharAt(builder.length() - 1).toString() + ") ");
            }

            QuerySql.append(" ORDER BY 1");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            if (sClaves.size() > 0) {
                for (int i = 0; i < sClaves.size(); i++) {
                    //_Parametros
                    preparedStatement.setInt((i + 1), sClaves.get(i));
                }
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                tipoValor.put(String.valueOf(0), "");

                while (resultSet.next()) {
                    @SuppressWarnings("unused")
                    String ValInstrume2 = ""; //ValInstrume1 = "",
                    // _Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                    if (resultSet.getString("INS_NOM_INSTRUME").contains("/")) {
                        Div_Instrume = resultSet.getString("INS_NOM_INSTRUME").split("/");
                        //ValInstrume1 = Div_Instrume[0];
                        ValInstrume2 = Div_Instrume[1];
                    } else {
                        ValInstrume2 = resultSet.getString("INS_NOM_INSTRUME");
                    }

                    NumInstrume = Integer.parseInt(resultSet.getString("INS_CVE_TIPO_MERCA")) * 1000
                            + Integer.parseInt(resultSet.getString("INS_NUM_INSTRUME"));

                    // Add_List
                    tipoValor.put(String.valueOf(NumInstrume), resultSet.getString("INS_MNEMO_INSTRUME") + " / "
                            + resultSet.getString("CVE_DESC_CLAVE") + " / " + ValInstrume2);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return tipoValor;
    }

    public Map<String, String> onTesoreria_GetEmisiones(int sMercado, int sInstrume) {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> Emisiones = new LinkedHashMap<String, String>();

        try {
            // SQL
            stringBuilder.append("SELECT EMI_NOM_PIZARRA, ");
            stringBuilder.append("EMI_NUM_SER_EMIS, ");
            stringBuilder.append("EMI_NUM_CUPON_VIG, ");
            stringBuilder.append("EMI_NUM_MONEDA, ");
            stringBuilder.append("EMI_NUM_SEC_EMIS ");
            stringBuilder.append("FROM SAF.EMISION ");
            stringBuilder.append("WHERE EMI_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND EMI_NUM_INSTRUME = ? ");
            stringBuilder.append("AND EMI_CVE_ST_EMISION = 'ACTIVO' ");
            stringBuilder.append("ORDER BY 1 ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, sMercado);
            preparedStatement.setInt(2, sInstrume);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Vacio_primero
                Emisiones.put(String.valueOf(0), "");

                while (resultSet.next()) {

                    // Add_List
                    Emisiones.put(String.valueOf(resultSet.getString("EMI_NUM_SEC_EMIS")),
                            resultSet.getString("EMI_NOM_PIZARRA") + " / " + resultSet.getString("EMI_NUM_SER_EMIS")
                            + " / " + resultSet.getString("EMI_NUM_CUPON_VIG"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Emisiones;
    }

    public List<PosicionBean> onTesoreria_ObtenPosicion(String contrato, String subfiso, int EntFinanc,
            double CtoInver) {

        StringBuilder stringBuilder = new StringBuilder();
        List<PosicionBean> posiciones = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT POS_NUM_CONTRATO,   POS_SUB_CONTRATO, ");
            stringBuilder.append("POS_NUM_ENTID_FIN,  		POS_CONTRATO_INTER, ");
            stringBuilder.append("POS_CVE_TIPO_MERCA, 		POS_NUM_INSTRUME, ");
            stringBuilder.append("POS_NUM_SEC_EMIS,   		POS_NOM_PIZARRA, ");
            stringBuilder.append("POS_NUM_SER_EMIS,   		POS_NUM_CUPON_VIG, ");
            stringBuilder.append("POS_NOM_CUSTODIO,   		POS_NUM_MONEDA, ");
            stringBuilder.append("POS_CVE_GARANTIA ,  		POS_POSIC_INI_PER, ");
            stringBuilder.append("POS_VTAS_POSIC_PER, 		POS_CPAS_POSIC_PER, ");
            stringBuilder.append("POS_POSIC_INI_EJER, 		POS_VTAS_POS_EJER, ");
            stringBuilder.append("POS_CPAS_POS_EJER,  		POS_POSIC_ACTUAL, ");
            stringBuilder.append("POS_POSIC_COMPROM,  		POS_COSTO_HISTORIC, ");
            stringBuilder.append("POS_ANO_ULT_MOVTO,  		POS_MES_ULT_MOVTO, ");
            stringBuilder.append("POS_DIA_ULT_MOVTO,  		POS_ANO_ALTA_REG, ");
            stringBuilder.append("POS_MES_ALTA_REG,   		POS_DIA_ALTA_REG, ");
            stringBuilder.append("POS_ANO_ULT_MOD,    		POS_MES_ULT_MOD, ");
            stringBuilder.append("POS_DIA_ULT_MOD,    		POS_CVE_ST_POSICIO,	");
            stringBuilder.append("POS_POSIC_ACTUAL-POS_POSIC_COMPROM AS POS_DISPO ");
            stringBuilder.append("FROM SAF.POSICION ");
            stringBuilder.append("WHERE POS_NUM_CONTRATO = ? ");
            stringBuilder.append("AND POS_SUB_CONTRATO = ? ");
            stringBuilder.append("AND POS_NUM_ENTID_FIN = ? ");
            stringBuilder.append("AND POS_CONTRATO_INTER = ? ");
            stringBuilder.append("AND POS_CVE_TIPO_MERCA <> 8 ");
            stringBuilder.append("AND POS_POSIC_ACTUAL <> 0 ");
            stringBuilder.append("ORDER BY POS_NOM_PIZARRA ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, contrato);
            preparedStatement.setString(2, subfiso);
            preparedStatement.setInt(3, EntFinanc);
            preparedStatement.setDouble(4, CtoInver);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {
                    PosicionBean posicionBean = new PosicionBean();

                    // Set_Values
                    posicionBean.setPizarra(resultSet.getString("POS_NOM_PIZARRA"));
                    posicionBean.setSerieEmis(resultSet.getString("POS_NUM_SER_EMIS"));
                    posicionBean.setCuponVig(resultSet.getInt("POS_NUM_CUPON_VIG"));
                    posicionBean.setVtasIniPeriodo(resultSet.getDouble("POS_VTAS_POSIC_PER"));
                    posicionBean.setCpasInicPeriodo(resultSet.getDouble("POS_CPAS_POSIC_PER"));
                    posicionBean.setPosActual(resultSet.getDouble("POS_POSIC_ACTUAL"));
                    posicionBean.setPosComprom(resultSet.getDouble("POS_POSIC_COMPROM"));
                    posicionBean.setPosDisp(resultSet.getDouble("POS_DISPO"));
                    posicionBean.setPosCostHist(resultSet.getDouble("POS_COSTO_HISTORIC"));
                    // -------------------------------------------------------------------
                    posicionBean.setSecEmisora(resultSet.getInt("POS_NUM_SEC_EMIS"));
                    posicionBean.setNumInstrumento(resultSet.getInt("POS_NUM_INSTRUME"));
                    posicionBean.setTipoMerca(resultSet.getInt("POS_CVE_TIPO_MERCA"));
                    posicionBean.setNomCustodio(resultSet.getString("POS_NOM_CUSTODIO"));

                    // Add_List
                    posiciones.add(posicionBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return posiciones;
    }

    public PosicionBean onTesoreria_getPosicionMSA(String contrato, String subfiso, int EntFinanc, double CtoInver,
            int IdMercado, int IdInstrumento, String sNomPizarra, String SerieEmis, String Cupon, int secEmisora) {

        StringBuilder stringBuilder = new StringBuilder();
        PosicionBean posicionBean = new PosicionBean();

        try {
            // SQL
            stringBuilder.append("SELECT POS_NUM_CONTRATO,   POS_SUB_CONTRATO, ");
            stringBuilder.append("POS_NUM_ENTID_FIN,  POS_CONTRATO_INTER, ");
            stringBuilder.append("POS_CVE_TIPO_MERCA, POS_NUM_INSTRUME, ");
            stringBuilder.append("POS_NUM_SEC_EMIS,   POS_NOM_PIZARRA, ");
            stringBuilder.append("POS_NUM_SER_EMIS,   POS_NUM_CUPON_VIG, ");
            stringBuilder.append("POS_NOM_CUSTODIO,   POS_NUM_MONEDA, ");
            stringBuilder.append("POS_CVE_GARANTIA ,  POS_POSIC_INI_PER, ");
            stringBuilder.append("POS_VTAS_POSIC_PER, POS_CPAS_POSIC_PER, ");
            stringBuilder.append("POS_POSIC_INI_EJER, POS_VTAS_POS_EJER, ");
            stringBuilder.append("POS_CPAS_POS_EJER,  POS_POSIC_ACTUAL, ");
            stringBuilder.append("POS_POSIC_COMPROM,  POS_COSTO_HISTORIC, ");
            stringBuilder.append("POS_ANO_ULT_MOVTO,  POS_MES_ULT_MOVTO, ");
            stringBuilder.append("POS_DIA_ULT_MOVTO,  POS_ANO_ALTA_REG, ");
            stringBuilder.append("POS_MES_ALTA_REG,   POS_DIA_ALTA_REG, ");
            stringBuilder.append("POS_ANO_ULT_MOD,    POS_MES_ULT_MOD, ");
            stringBuilder.append("POS_DIA_ULT_MOD,    POS_CVE_ST_POSICIO, ");
            stringBuilder.append("POS_POSIC_ACTUAL-POS_POSIC_COMPROM AS POS_DISPO ");
            stringBuilder.append("FROM SAF.POSICIONMSA ");
            stringBuilder.append("WHERE POS_NUM_CONTRATO = ? ");
            stringBuilder.append("AND POS_SUB_CONTRATO = ? ");
            stringBuilder.append("AND POS_NUM_ENTID_FIN = ? ");
            stringBuilder.append("AND POS_CONTRATO_INTER = ? ");
            stringBuilder.append("AND POS_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND POS_NUM_INSTRUME = ? ");
            stringBuilder.append("AND POS_NOM_PIZARRA = ? ");
            stringBuilder.append("AND POS_NUM_SER_EMIS = ? ");
            stringBuilder.append("AND POS_NUM_CUPON_VIG = ?  ");
            stringBuilder.append("AND POS_NUM_SEC_EMIS  = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(contrato));
            preparedStatement.setInt(2, Integer.parseInt(subfiso));
            preparedStatement.setInt(3, EntFinanc);
            preparedStatement.setDouble(4, CtoInver);
            preparedStatement.setInt(5, IdMercado);
            preparedStatement.setInt(6, IdInstrumento);
            preparedStatement.setString(7, sNomPizarra);
            preparedStatement.setString(8, SerieEmis);
            preparedStatement.setInt(9, Integer.parseInt(Cupon));
            preparedStatement.setInt(10, secEmisora);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {

                    // Set_Values
                    posicionBean.setPizarra(resultSet.getString("POS_NOM_PIZARRA"));
                    posicionBean.setSerieEmis(resultSet.getString("POS_NUM_SER_EMIS"));
                    posicionBean.setCuponVig(resultSet.getInt("POS_NUM_CUPON_VIG"));
                    posicionBean.setVtasIniPeriodo(resultSet.getDouble("POS_VTAS_POSIC_PER"));
                    posicionBean.setCpasInicPeriodo(resultSet.getDouble("POS_CPAS_POSIC_PER"));
                    posicionBean.setPosActual(resultSet.getDouble("POS_POSIC_ACTUAL"));
                    posicionBean.setPosComprom(resultSet.getDouble("POS_POSIC_COMPROM"));
                    posicionBean.setPosDisp(resultSet.getDouble("POS_DISPO"));
                    posicionBean.setPosCostHist(resultSet.getDouble("POS_COSTO_HISTORIC"));
                    // -------------------------------------------------------------------
                    posicionBean.setSecEmisora(resultSet.getInt("POS_NUM_SEC_EMIS"));
                    posicionBean.setNumInstrumento(resultSet.getInt("POS_NUM_INSTRUME"));
                    posicionBean.setTipoMerca(resultSet.getInt("POS_CVE_TIPO_MERCA"));
                    posicionBean.setNomCustodio(resultSet.getString("POS_NOM_CUSTODIO"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return posicionBean;
    }

    public PosicionBean onTesoreria_getPosicion(String contrato, String subfiso, int EntFinanc, double CtoInver,
            int IdMercado, int IdInstrumento, String sNomPizarra, String SerieEmis, String Cupon, int secEmisora) {

        StringBuilder stringBuilder = new StringBuilder();
        PosicionBean posicionBean = new PosicionBean();

        try {
            // SQL
            stringBuilder.append("SELECT POS_NUM_CONTRATO,   POS_SUB_CONTRATO, ");
            stringBuilder.append("POS_NUM_ENTID_FIN,  POS_CONTRATO_INTER, ");
            stringBuilder.append("POS_CVE_TIPO_MERCA, POS_NUM_INSTRUME, ");
            stringBuilder.append("POS_NUM_SEC_EMIS,   POS_NOM_PIZARRA, ");
            stringBuilder.append("POS_NUM_SER_EMIS,   POS_NUM_CUPON_VIG, ");
            stringBuilder.append("POS_NOM_CUSTODIO,   POS_NUM_MONEDA, ");
            stringBuilder.append("POS_CVE_GARANTIA ,  POS_POSIC_INI_PER, ");
            stringBuilder.append("POS_VTAS_POSIC_PER, POS_CPAS_POSIC_PER, ");
            stringBuilder.append("POS_POSIC_INI_EJER, POS_VTAS_POS_EJER, ");
            stringBuilder.append("POS_CPAS_POS_EJER,  POS_POSIC_ACTUAL, ");
            stringBuilder.append("POS_POSIC_COMPROM,  POS_COSTO_HISTORIC, ");
            stringBuilder.append("POS_ANO_ULT_MOVTO,  POS_MES_ULT_MOVTO, ");
            stringBuilder.append("POS_DIA_ULT_MOVTO,  POS_ANO_ALTA_REG, ");
            stringBuilder.append("POS_MES_ALTA_REG,   POS_DIA_ALTA_REG, ");
            stringBuilder.append("POS_ANO_ULT_MOD,    POS_MES_ULT_MOD, ");
            stringBuilder.append("POS_DIA_ULT_MOD,    POS_CVE_ST_POSICIO, ");
            stringBuilder.append("POS_POSIC_ACTUAL-POS_POSIC_COMPROM AS POS_DISPO ");
            stringBuilder.append("FROM SAF.POSICION ");
            stringBuilder.append("WHERE POS_NUM_CONTRATO = ? ");
            stringBuilder.append("AND POS_SUB_CONTRATO = ? ");
            stringBuilder.append("AND POS_NUM_ENTID_FIN = ? ");
            stringBuilder.append("AND POS_CONTRATO_INTER = ? ");
            stringBuilder.append("AND POS_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND POS_NUM_INSTRUME = ? ");
            stringBuilder.append("AND POS_NOM_PIZARRA = ? ");
            stringBuilder.append("AND POS_NUM_SER_EMIS = ? ");
            stringBuilder.append("AND POS_NUM_CUPON_VIG = ?  ");
            stringBuilder.append("AND POS_NUM_SEC_EMIS  = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(contrato));
            preparedStatement.setInt(2, Integer.parseInt(subfiso));
            preparedStatement.setInt(3, EntFinanc);
            preparedStatement.setDouble(4, CtoInver);
            preparedStatement.setInt(5, IdMercado);
            preparedStatement.setInt(6, IdInstrumento);
            preparedStatement.setString(7, sNomPizarra);
            preparedStatement.setString(8, SerieEmis);
            preparedStatement.setInt(9, Integer.parseInt(Cupon));
            preparedStatement.setInt(10, secEmisora);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {

                    // Set_Values
                    posicionBean.setPizarra(resultSet.getString("POS_NOM_PIZARRA"));
                    posicionBean.setSerieEmis(resultSet.getString("POS_NUM_SER_EMIS"));
                    posicionBean.setCuponVig(resultSet.getInt("POS_NUM_CUPON_VIG"));
                    posicionBean.setVtasIniPeriodo(resultSet.getDouble("POS_VTAS_POSIC_PER"));
                    posicionBean.setCpasInicPeriodo(resultSet.getDouble("POS_CPAS_POSIC_PER"));
                    posicionBean.setPosActual(resultSet.getDouble("POS_POSIC_ACTUAL"));
                    posicionBean.setPosComprom(resultSet.getDouble("POS_POSIC_COMPROM"));
                    posicionBean.setPosDisp(resultSet.getDouble("POS_DISPO"));
                    posicionBean.setPosCostHist(resultSet.getDouble("POS_COSTO_HISTORIC"));
                    // -------------------------------------------------------------------
                    posicionBean.setSecEmisora(resultSet.getInt("POS_NUM_SEC_EMIS"));
                    posicionBean.setNumInstrumento(resultSet.getInt("POS_NUM_INSTRUME"));
                    posicionBean.setTipoMerca(resultSet.getInt("POS_CVE_TIPO_MERCA"));
                    posicionBean.setNomCustodio(resultSet.getString("POS_NOM_CUSTODIO"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return posicionBean;
    }

    public EmisionBean onTesoreria_validaEmisiones(String sPizarra, String sSerie, String sCupon, int iInstrume,
            int iMercado, int sSecEmis) {

        StringBuilder stringBuilder = new StringBuilder();
        EmisionBean emisionBean = new EmisionBean();

        try {
            // SQL
            stringBuilder.append("SELECT EMI_CVE_TIPO_MERCA, EMI_NUM_INSTRUME, EMI_NUM_MONEDA, EMI_NUM_SEC_EMIS, EMI_VALOR_NOMINAL ");
            stringBuilder.append("FROM SAF.EMISION ");
            stringBuilder.append("WHERE EMI_NOM_PIZARRA = ? ");
            stringBuilder.append("AND EMI_NUM_SER_EMIS = ? ");
            stringBuilder.append("AND EMI_NUM_CUPON_VIG = ? ");
            stringBuilder.append(" AND EMI_NUM_INSTRUME = ? ");
            stringBuilder.append(" AND EMI_CVE_TIPO_MERCA = ? ");

            if (sSecEmis != 0) {
                stringBuilder.append(" AND EMI_NUM_SEC_EMIS = ? ");
            }

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sPizarra);
            preparedStatement.setString(2, sSerie);
            preparedStatement.setInt(3, Integer.parseInt(sCupon));
            preparedStatement.setInt(4, iInstrume);
            preparedStatement.setInt(5, iMercado);

            if (sSecEmis != 0) {
                preparedStatement.setInt(6, sSecEmis);
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {

                    // Set_Values
                    emisionBean.setTipoMercado(resultSet.getInt("EMI_CVE_TIPO_MERCA"));
                    emisionBean.setNumInstrume(resultSet.getInt("EMI_NUM_INSTRUME"));
                    emisionBean.setNumSecEmis(resultSet.getInt("EMI_NUM_SEC_EMIS"));
                    emisionBean.setValNominal(resultSet.getDouble("EMI_VALOR_NOMINAL"));
                    emisionBean.setNumMoneda(resultSet.getInt("EMI_NUM_MONEDA"));
                }
            }
            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return emisionBean;
    }

    public boolean onTesoreria_existeEmision(String sPizarra, String sSerie, String sCupon, int iInstrume, int iMercado,
            int sSecEmis, int iDia, int iMes, int iAnio) {

        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        // Write_("Ingresa a Método onTesoreria_existeEmision()");
        try {
            // SQL
            stringBuilder.append("SELECT EMI_CVE_TIPO_MERCA, ");
            stringBuilder.append("EMI_NUM_INSTRUME, ");
            stringBuilder.append("EMI_NUM_SEC_EMIS, ");
            stringBuilder.append(" COALESCE(EMB_ANO_INI_BLOQUE * 10000 + EMB_MES_INI_BLOQUE * 100 + EMB_DIA_INI_BLOQUE,0) FEC_INICIO, ");
            stringBuilder.append(" COALESCE(EMB_ANO_FIN_BLOQUE * 10000 + EMB_MES_FIN_BLOQUE * 100 + EMB_DIA_FIN_BLOQUE,0) FEC_FIN ");
            stringBuilder.append(" FROM SAF.EMISION ");
            stringBuilder.append(" LEFT OUTER JOIN SAF.EMIBLOQU ");
            stringBuilder.append(" ON EMB_NOM_PIZARRA = EMI_NOM_PIZARRA ");
            stringBuilder.append(" AND EMB_NUM_SER_EMIS = EMI_NUM_SER_EMIS ");
            stringBuilder.append(" AND EMB_NUM_CUPON_VIG = EMI_NUM_CUPON_VIG ");
            stringBuilder.append(" AND EMB_NUM_INSTRUME = EMI_NUM_INSTRUME ");
            stringBuilder.append(" WHERE EMI_NOM_PIZARRA = ? ");
            stringBuilder.append(" AND EMI_NUM_SER_EMIS = ? ");
            stringBuilder.append(" AND EMI_NUM_CUPON_VIG = ? ");
            stringBuilder.append(" AND EMI_NUM_INSTRUME = ? ");
            stringBuilder.append(" AND EMI_CVE_TIPO_MERCA = ? ");
            stringBuilder.append(" AND EMI_NUM_SEC_EMIS = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sPizarra);
            preparedStatement.setString(2, sSerie);
            preparedStatement.setInt(3, Integer.parseInt(sCupon));
            preparedStatement.setInt(4, iInstrume);
            preparedStatement.setInt(5, iMercado);
            preparedStatement.setInt(6, sSecEmis);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    int iFecIni = resultSet.getInt("FEC_INICIO");
                    int iFecFin = resultSet.getInt("FEC_FIN");
                    int FecMov = iAnio * 10000 + iMes * 100 + iDia;

                    if (iFecIni <= FecMov && FecMov <= iFecFin) {
                        // Set_Message
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                "La emisión se encuentra bloqueda para Compras/Ventas"));
                        valorRetorno = false;
                    } else {
                        valorRetorno = true;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_ValidaOperacion(String NumOperac) {

        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {
            // SQL
            stringBuilder.append("SELECT TRAN_ID_TRAN ,OPER_ID_OPERACION ");
            stringBuilder.append("FROM SAF.FDDETOPERA ");
            stringBuilder.append("WHERE OPER_ID_OPERACION = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, NumOperac);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    valorRetorno = true;
                }
            }

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;

    }

    public double onTesoreria_getPrecio(String sPizarra, String sSerie, String sCupon, int iMercado, int iInstrume) {

        double iPrecio = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT PRE_PRECIO_CIERRE ");
            stringBuilder.append(" FROM SAF.PRECIOSM ");
            stringBuilder.append(" WHERE PRE_NOM_PIZARRA = ? ");
            stringBuilder.append(" AND PRE_SER_EMIS = ? ");
            stringBuilder.append(" AND PRE_NUM_CUPON_VIG = ? ");
            stringBuilder.append(" AND PRE_ANO_PREC_EMIS =? ");
            stringBuilder.append(" AND PRE_MES_PREC_EMIS = ? ");
            stringBuilder.append(" AND PRE_DIA_PREC_EMIS = ? ");
            stringBuilder.append(" AND PRE_CVE_ST_PRECIO ='ACTIVO' ");

            if (iMercado > 0) {
                stringBuilder.append(" AND PRE_CVE_TIPO_MERCA = ? ");
            }

            if (iInstrume > 0) {
                stringBuilder.append(" AND PRE_NUM_INSTRUME = ? ");
            }

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setString(1, sPizarra);
            preparedStatement.setString(2, sSerie);
            preparedStatement.setInt(3, Integer.parseInt(sCupon));
            preparedStatement.setInt(4, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño").toString()));
            preparedStatement.setInt(5, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes").toString()));
            preparedStatement.setInt(6, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia").toString()));

            if (iMercado > 0) {
                preparedStatement.setInt(7, iMercado);
            }

            if (iInstrume > 0) {
                if (iMercado > 0) {
                    preparedStatement.setInt(8, iInstrume);
                } else {
                    preparedStatement.setInt(7, iInstrume);
                }
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("PRE_PRECIO_CIERRE") != null || !resultSet.getString("PRE_PRECIO_CIERRE").equals("")) {

                        iPrecio = resultSet.getInt("PRE_PRECIO_CIERRE");
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return iPrecio;
    }

    public String onTesoreria_NomMoneda(int nMoneda) {

        String NomMoneda = "";
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT MON_NOM_MONEDA ");
            stringBuilder.append("FROM SAF.MONEDAS ");
            stringBuilder.append("WHERE MON_NUM_PAIS = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, nMoneda);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {

                    // Add_List
                    NomMoneda = resultSet.getString("MON_NOM_MONEDA");
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return NomMoneda;
    }

    public boolean onTesoreria_SelectEntidadFin(String fideicomiso, String SubFiso, int EntidadFinan, double CtoInver,
            String NomPizarra, String SerieEmis, int iYearVal, int iMesVal, int iDiaVal) {

        int iReg = 0;
        boolean valorRetorno = false;
        StringBuilder stringBuilder = new StringBuilder();
        Connection connect = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // SQL
            stringBuilder.append("SELECT COUNT(*) as INT_TIPO_VALUA ");
            stringBuilder.append(" FROM SAF.INTERMED ");
            stringBuilder.append(" WHERE INT_ENTIDAD_FIN =  ? ");
            stringBuilder.append(" AND INT_TIPO_VALUA = 'COSTO PROMEDIO'");

            // Call_Operaciones_BD
            connect = DataBaseConexion.getInstance().getConnection();
            pstmt = connect.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setInt(1, EntidadFinan);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            while (rs.next()) {
                iReg = rs.getInt("INT_TIPO_VALUA");
                if (iReg > 0) {
                    if (onTesoreria_VentasPosteriores(fideicomiso, SubFiso, EntidadFinan, CtoInver,
                            NomPizarra, SerieEmis, iYearVal, iMesVal, iDiaVal)) {
                        valorRetorno = true;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connect != null) {
                    connect.close();
                }

            } catch (SQLException sqlException) {
                mensajeError += "Error al cerrar la conexión: ".concat(sqlException.getMessage());
            }
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_VentasPosteriores(String fideicomiso, String SubFiso, int EntidadFinan, double CtoInver,
            String NomPizarra, String SerieEmis, int iYearVal, int iMesVal, int iDiaVal) {

        String FecVal = "";
        FecVal = iYearVal + "-" + iMesVal + "-" + iDiaVal;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecVal);

            // SQL
            stringBuilder.append("SELECT COUNT(*) as CONT ");
            stringBuilder.append(" FROM SAF.VENTEMIS ");
            stringBuilder.append(" WHERE VTE_NUM_CONTRATO =  ? ");
            stringBuilder.append(" AND VTE_SUB_CONTRATO  = ? ");
            stringBuilder.append(" AND VTE_CONTRATO_INTER = ? ");
            stringBuilder.append(" AND VTE_NUM_PIZARRA =  ? ");
            stringBuilder.append(" AND VTE_NUM_SER_EMIS = ? ");
            stringBuilder.append(" AND DATE(RTRIM(CHAR(VTE_ANO_ALTA_REG))||'-'||RTRIM(CHAR(VTE_MES_ALTA_REG))||'-'||RTRIM(CHAR(VTE_DIA_ALTA_REG))) > ? ");
            stringBuilder.append(" AND VTE_CVE_ST_VENTEM ='ACTIVO'");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, fideicomiso);
            preparedStatement.setString(2, SubFiso);
            preparedStatement.setDouble(3, CtoInver);
            preparedStatement.setString(4, NomPizarra);
            preparedStatement.setString(5, SerieEmis);
            preparedStatement.setDate(6, FechaSql);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("CONT") != null && !resultSet.getString("CONT").equals("0")) {
                        //Existen ventas posteriores a la fecha de compra
                        valorRetorno = true;
                    }
                }
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_VerificaLiquidez(double Importe, String fideicomiso, String SubFiso, int iEntidadFinan,
            double dCtoInver, int iCustodio) {

        boolean valorRetorno = false;
        StringBuilder stringBuilder = new StringBuilder();

        try {

            // SQL
            stringBuilder.append("SELECT SALD_SALDO_ACTUAL, CPR_NUM_CUENTA ");
            stringBuilder.append("FROM SAF.CONTINTE ");
            stringBuilder.append("INNER JOIN SAF.FDSALDOS ON SALD_AX1 = CPR_NUM_CONTRATO ");
            stringBuilder.append("AND SALD_AX2 = CPR_SUB_CONTRATO ");
            stringBuilder.append("AND CCON_CTA = 1103 ");
            stringBuilder.append("AND CCON_SCTA = 1 ");

            if (peticion.getRequestURI().contains("CompraVenta")) {
                stringBuilder.append("AND CCON_2SCTA = 4 AND SALD_AX3 = CPR_CONTRATO_INTER ");
            } else if (peticion.getRequestURI().contains("Reportos")) {
                stringBuilder.append("AND CCON_2SCTA = 4 AND SALD_AX3 = CPR_CONTRATO_INTER ");
            } else if (peticion.getRequestURI().contains("Pagares")) {
                stringBuilder.append("AND CCON_2SCTA = 2 AND SALD_AX3 = CPR_NUM_CUENTA ");
            } else {
                // _Valida_liquidez,_para_los_intermediarios_Bursátiles_se_checa_en_la_cuenta
                // _1103-1-4-1_con_Aux_3_el_contrato_de_Inversión
                if (iCustodio == 4) {
                    stringBuilder.append("AND CCON_2SCTA = 4 AND SALD_AX3 = CPR_CONTRATO_INTER ");
                }
                // _Valida_liquidez,_para_los_intermediarios_Bancarios_se_checa_en_la_cuenta
                // _1103-1-2-1_con_Aux_3_la_cuenta_de_cheques_del_contrato_de_Inversión
                if (iCustodio == 5) {
                    stringBuilder.append("AND CCON_2SCTA = 2 AND SALD_AX3 = CPR_NUM_CUENTA ");
                }
            }

            stringBuilder.append("AND CCON_3SCTA = 1 ");
            stringBuilder.append("AND MONE_ID_MONEDA = CPR_MONEDA_CTA ");
            stringBuilder.append("WHERE CPR_NUM_CONTRATO = ? ");
            stringBuilder.append("AND CPR_SUB_CONTRATO = ? ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN = ? ");
            stringBuilder.append("AND CPR_CONTRATO_INTER = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, fideicomiso);
            preparedStatement.setString(2, SubFiso);
            preparedStatement.setInt(3, iEntidadFinan);
            preparedStatement.setDouble(4, dCtoInver);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    if (Importe <= resultSet.getDouble("SALD_SALDO_ACTUAL")) {
                        valorRetorno = true;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public int onTesoreria_RecuperaISR(int fideicomiso) {

        int iRetieneISR = 2;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT CVA_CVE_ISR_EXENTO ");
            stringBuilder.append("FROM SAF.CTOPAINV ");
            stringBuilder.append("WHERE CVA_NUM_CONTRATO = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, fideicomiso);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt("CVA_CVE_ISR_EXENTO") == 1) {
                        //No_retiene
                        iRetieneISR = 0;
                    } else {
                        //Si_retiene
                        iRetieneISR = 1;
                    }
                } else {
                    iRetieneISR = 1;
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        // _Solo_juega_el_contrato_en_el_ISR
        return iRetieneISR;
    }

    public OutParameterBean onTesoreria_EjecutaCompraDirecto(CompraVentaBean compraVentaBean, Date FechaCont, int iMercado, int iInstrumento, int iSecEmis,
            int iFecValorMSA, double Comision, double dValorIVA, String sOrigen, String sTipoMov, int iRetieneISR, String sGarantia
    ) {

        OutParameterBean outParameterBean = new OutParameterBean();

        try {
            DecimalFormat decimalFormat2 = new DecimalFormat("#0.00");

            // cambiar_fecha
            Date diaOpera = this.dateParse(compraVentaBean.getDiaOpera());

            // SQL
            String sql = "{CALL DB2FIDUC.SP_MD_CPA_DIRECTO (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            callableStatement.setInt(2, compraVentaBean.getMoneda());
            callableStatement.setDate(3, java.sql.Date.valueOf(this.dateFormat(FechaCont)));
            callableStatement.setDate(4, java.sql.Date.valueOf(this.dateFormat(diaOpera)));
            callableStatement.setInt(5, Integer.parseInt(compraVentaBean.getFideicomiso()));
            callableStatement.setInt(6, Integer.parseInt(compraVentaBean.getSubFiso()));
            callableStatement.setInt(7, compraVentaBean.getEntidadFinan());
            callableStatement.setDouble(8, compraVentaBean.getCtoInver());
            callableStatement.setInt(9, iMercado);
            callableStatement.setInt(10, iInstrumento);
            callableStatement.setInt(11, iSecEmis);
            callableStatement.setString(12, compraVentaBean.getNomPizarra());
            callableStatement.setString(13, compraVentaBean.getSerieEmis());
            callableStatement.setInt(14, Integer.parseInt(compraVentaBean.getCuponVig()));
            callableStatement.setInt(15, iFecValorMSA);
            callableStatement.setDouble(16, Double.parseDouble(compraVentaBean.getTitulos()));
            callableStatement.setString(17, compraVentaBean.getPrecioEmis());
            callableStatement.setString(18, decimalFormat2.format(Comision));
            callableStatement.setString(19, decimalFormat2.format(dValorIVA));
            callableStatement.setString(20, sOrigen);
            callableStatement.setString(21, sTipoMov);
            callableStatement.setInt(22, compraVentaBean.getCustodio());
            callableStatement.setInt(23, 0);
            callableStatement.setInt(24, iRetieneISR);
            callableStatement.setString(25, sGarantia);
            callableStatement.setInt(26, 0); // GF_Referencia_CICS
            callableStatement.setDouble(27, compraVentaBean.getTipoCambio());

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PI_FOLIO", Types.SMALLINT);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iNumFolio = callableStatement.getInt("PI_FOLIO");
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (iNumFolio > 0 && sMsErr.equals("")) {
                outParameterBean.setiNumFolioContab(iNumFolio);
                bitacoraBean.setDetalle("Operación con el folio: " + iNumFolio);
            } else {
                outParameterBean.setPsMsgErrOut(sMsErr);
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            }

            // SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoMov.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_COMPRA_ENTRADA".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException | ParseException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public OutParameterBean onTesoreria_EjecutaVentaDirecto(CompraVentaBean entradaSalidaBean, Date FechaCont,
            int iMercado, int iInstrumento, int iSecEmis,
            int iFecValorMSA, double Comision, double dValorIVA, String sOrigen, String sTipoMov,
            int iRetieneISR, String sGarantia) {

        OutParameterBean outParameterBean = new OutParameterBean();
        DecimalFormat decimalFormat2 = new DecimalFormat("#0.00");

        try {
            // cambiar_fecha
            Date diaOpera = this.dateParse(entradaSalidaBean.getDiaOpera());

            // SQL
            String sql = "{CALL DB2FIDUC.SP_MD_VTA_DIRECTO (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            callableStatement.setInt(2, entradaSalidaBean.getMoneda());
            callableStatement.setDate(3, java.sql.Date.valueOf(this.dateFormat(FechaCont)));
            callableStatement.setDate(4, java.sql.Date.valueOf(this.dateFormat(diaOpera)));
            callableStatement.setInt(5, Integer.parseInt(entradaSalidaBean.getFideicomiso()));
            callableStatement.setInt(6, Integer.parseInt(entradaSalidaBean.getSubFiso()));
            callableStatement.setInt(7, entradaSalidaBean.getEntidadFinan());
            callableStatement.setDouble(8, entradaSalidaBean.getCtoInver());
            callableStatement.setInt(9, iMercado);
            callableStatement.setInt(10, iInstrumento);
            callableStatement.setInt(11, iSecEmis);
            callableStatement.setString(12, entradaSalidaBean.getNomPizarra());
            callableStatement.setString(13, entradaSalidaBean.getSerieEmis());
            callableStatement.setInt(14, Integer.parseInt(entradaSalidaBean.getCuponVig()));
            callableStatement.setInt(15, iFecValorMSA);
            callableStatement.setDouble(16, Double.parseDouble(entradaSalidaBean.getTitulos()));
            callableStatement.setString(17, entradaSalidaBean.getPrecioEmis());
            callableStatement.setString(18, decimalFormat2.format(Comision));
            callableStatement.setString(19, decimalFormat2.format(dValorIVA));
            callableStatement.setString(20, sOrigen);
            callableStatement.setString(21, sTipoMov);
            callableStatement.setInt(22, entradaSalidaBean.getCustodio());
            callableStatement.setInt(23, 0);
            callableStatement.setInt(24, iRetieneISR);
            callableStatement.setString(25, sGarantia);
            callableStatement.setInt(26, 0); // GF_Referencia_CICS
            callableStatement.setString(27, "0.00");

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PI_FOLIO", Types.SMALLINT);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iNumFolio = callableStatement.getInt("PI_FOLIO");
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (iNumFolio > 0 && sMsErr.equals("")) {
                outParameterBean.setiNumFolioContab(iNumFolio);
                bitacoraBean.setDetalle("Operación aplicada con el folio: " + iNumFolio);
            } else {
                outParameterBean.setPsMsgErrOut(sMsErr);
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            }

            // SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoMov.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_VENTA_SALIDA".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException | ParseException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * E N T R A D A S  Y  S A L I D A S * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * *
     */
    public Map<String, String> onTesoreria_GetEmisionesEntradaSalida(int sMercado, int sInstrume) {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> Emisiones = new LinkedHashMap<String, String>();

        try {
            // SQL
            stringBuilder.append("SELECT EMI_NUM_SEC_EMIS,  EMI_NOM_PIZARRA, ");
            stringBuilder.append("EMI_NUM_SER_EMIS,  EMI_NUM_CUPON_VIG, ");
            stringBuilder.append("EMI_NUM_MONEDA ");
            stringBuilder.append("FROM SAF.EMISION ");
            stringBuilder.append("WHERE EMI_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND EMI_NUM_INSTRUME = ? ");
            stringBuilder.append("AND EMI_CVE_ST_EMISION = 'ACTIVO' ");
            stringBuilder.append("ORDER BY 2 ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, sMercado);
            preparedStatement.setInt(2, sInstrume);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                Emisiones.put(String.valueOf(0), "");

                while (resultSet.next()) {

                    // Add_List
                    Emisiones.put(
                            String.valueOf(resultSet.getString("EMI_NUM_MONEDA") + "/"
                                    + resultSet.getString("EMI_NUM_SEC_EMIS")),
                            resultSet.getString("EMI_NOM_PIZARRA") + " / "
                            + resultSet.getString("EMI_NUM_SER_EMIS")
                            + " / " + resultSet.getString("EMI_NUM_CUPON_VIG"));
                }
            }
            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Emisiones;
    }

    public Map<String, String> onTesoreria_GetTipoCambioEntradaSalida(int iDia, int iMes, int iAnio, int iMoneda) {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> Moneda = new LinkedHashMap<String, String>();

        try {
            // SQL
            // validar_Tipo_Cambio_fecha
            stringBuilder.append("SELECT MON_NUM_PAIS, ");
            stringBuilder.append("MON_NOM_MONEDA, ");
            stringBuilder.append("COALESCE (TIC_IMP_TIPO_CAMB ,0) TIPO_CAMB ");
            stringBuilder.append("FROM SAF.MONEDAS  ");
            stringBuilder.append("LEFT OUTER JOIN SAF.TIPOCAMB  ");
            stringBuilder.append("ON TIC_ANO_ALTA_REG  = ? ");
            stringBuilder.append("AND TIC_MES_ALTA_REG = ? ");
            stringBuilder.append("AND TIC_DIA_ALTA_REG = ? ");
            stringBuilder.append("WHERE MON_NUM_PAIS = ? ");
            stringBuilder.append("ORDER BY TIC_ANO_ULT_MOD DESC, TIC_MES_ULT_MOD DESC, TIC_DIA_ULT_MOD DESC, TIC_HORA_ALTA DESC, TIC_MINUTO_ALTA DESC ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, iAnio);
            preparedStatement.setInt(2, iMes);
            preparedStatement.setInt(3, iDia);
            preparedStatement.setInt(4, iMoneda);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("MON_NUM_PAIS") != null) {
                        // Add_Map
                        Moneda.put(String.valueOf(resultSet.getInt("MON_NUM_PAIS")),
                                resultSet.getDouble("TIPO_CAMB") + "/" + resultSet.getString("MON_NOM_MONEDA"));

                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Moneda;
    }

    public List<PosicionBean> onTesoreria_PosicionSalidas(String contrato, String subfiso, int EntFinanc, double CtoInver) {

        List<PosicionBean> posiciones = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT POS_NOM_PIZARRA, ");
            stringBuilder.append("POS_NUM_SER_EMIS, ");
            stringBuilder.append("POS_NUM_CUPON_VIG, ");
            stringBuilder.append("POS_VTAS_POSIC_PER, ");
            stringBuilder.append("POS_CPAS_POSIC_PER, ");
            stringBuilder.append("POS_POSIC_ACTUAL, ");
            stringBuilder.append("POS_NOM_CUSTODIO, ");
            stringBuilder.append("CVE_DESC_CLAVE CUSTODIO, ");
            stringBuilder.append("POS_CVE_TIPO_MERCA, ");
            stringBuilder.append("POS_NUM_INSTRUME, ");
            stringBuilder.append("POS_NUM_MONEDA, ");
            stringBuilder.append("POS_COSTO_HISTORIC / POS_POSIC_ACTUAL PRECIO ");
            stringBuilder.append("FROM SAF.POSICION ");
            stringBuilder.append("INNER JOIN SAF.CLAVES ON CVE_NUM_CLAVE =386 ");
            stringBuilder.append("AND CVE_NUM_SEC_CLAVE  = INT (CASE WHEN POS_NOM_CUSTODIO ='' THEN '0' ELSE COALESCE(POS_NOM_CUSTODIO,'0') END) ");
            stringBuilder.append("WHERE POS_NUM_CONTRATO = ? ");
            stringBuilder.append("AND POS_SUB_CONTRATO = ? ");
            stringBuilder.append("AND POS_NUM_ENTID_FIN = ? ");
            stringBuilder.append("AND POS_CONTRATO_INTER = ? ");
            stringBuilder.append("AND POS_POSIC_ACTUAL <> 0 ");
            stringBuilder.append("ORDER BY POS_NOM_PIZARRA ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, contrato);
            preparedStatement.setString(2, subfiso);
            preparedStatement.setInt(3, EntFinanc);
            preparedStatement.setDouble(4, CtoInver);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                while (resultSet.next()) {
                    PosicionBean posicionBean = new PosicionBean();

                    // Set_Values
                    posicionBean.setPizarra(resultSet.getString("POS_NOM_PIZARRA"));
                    posicionBean.setSerieEmis(resultSet.getString("POS_NUM_SER_EMIS"));
                    posicionBean.setCuponVig(resultSet.getInt("POS_NUM_CUPON_VIG"));
                    posicionBean.setVtasIniPeriodo(resultSet.getDouble("POS_VTAS_POSIC_PER"));
                    posicionBean.setCpasInicPeriodo(resultSet.getDouble("POS_CPAS_POSIC_PER"));
                    posicionBean.setPosActual(resultSet.getDouble("POS_POSIC_ACTUAL"));
                    posicionBean.setNomCustodio(resultSet.getString("CUSTODIO"));
                    posicionBean.setNumInstrumento(resultSet.getInt("POS_NUM_INSTRUME"));
                    posicionBean.setTipoMerca(resultSet.getInt("POS_CVE_TIPO_MERCA"));
                    posicionBean.setNumMoneda(resultSet.getInt("POS_NUM_MONEDA"));
                    // _Almacenan_temporalmente_valores_de_Precio_y_Custodio,_no_pertenecen_a_la_Posicion
                    posicionBean.setPosDisp(resultSet.getDouble("PRECIO"));
                    posicionBean.setClaveGarantia(resultSet.getInt("POS_NOM_CUSTODIO"));

                    // Add_List
                    posiciones.add(posicionBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return posiciones;
    }

    public PosicionBean onTesoreria_ValidaSalida(String contrato, String subfiso, int EntFinanc, double CtoInver,
            String sNomPizarra, String SerieEmis, String Cupon, int IdMercado, int IdInstrumento) {

        PosicionBean posicionBean = new PosicionBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT POS_NOM_PIZARRA, ");
            stringBuilder.append("POS_NUM_SER_EMIS, ");
            stringBuilder.append("POS_NUM_CUPON_VIG, ");
            stringBuilder.append("POS_VTAS_POSIC_PER, ");
            stringBuilder.append("POS_CPAS_POSIC_PER, ");
            stringBuilder.append("POS_POSIC_ACTUAL, ");
            stringBuilder.append("POS_NOM_CUSTODIO, ");
            stringBuilder.append("POS_CVE_TIPO_MERCA, ");
            stringBuilder.append("POS_NUM_INSTRUME, ");
            stringBuilder.append("POS_POSIC_COMPROM, ");
            stringBuilder.append("POS_COSTO_HISTORIC, ");
            stringBuilder.append("POS_POSIC_ACTUAL-POS_POSIC_COMPROM AS POS_DISPO  ");
            stringBuilder.append("FROM SAF.POSICION ");
            stringBuilder.append("WHERE POS_NUM_CONTRATO = ? ");
            stringBuilder.append("AND POS_SUB_CONTRATO = ? ");
            stringBuilder.append("AND POS_NUM_ENTID_FIN = ? ");
            stringBuilder.append("AND POS_CONTRATO_INTER = ? ");
            stringBuilder.append("AND POS_NOM_PIZARRA = ? ");
            stringBuilder.append("AND POS_NUM_SER_EMIS = ? ");
            stringBuilder.append("AND POS_NUM_CUPON_VIG = ? ");
            stringBuilder.append("AND POS_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND POS_NUM_INSTRUME = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(contrato));
            preparedStatement.setInt(2, Integer.parseInt(subfiso));
            preparedStatement.setInt(3, EntFinanc);
            preparedStatement.setDouble(4, CtoInver);
            preparedStatement.setString(5, sNomPizarra);
            preparedStatement.setString(6, SerieEmis);
            preparedStatement.setInt(7, Integer.parseInt(Cupon));
            preparedStatement.setInt(8, IdMercado);
            preparedStatement.setInt(9, IdInstrumento);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    // Set_Values
                    posicionBean.setPizarra(resultSet.getString("POS_NOM_PIZARRA"));
                    posicionBean.setSerieEmis(resultSet.getString("POS_NUM_SER_EMIS"));
                    posicionBean.setCuponVig(resultSet.getInt("POS_NUM_CUPON_VIG"));
                    posicionBean.setVtasIniPeriodo(resultSet.getDouble("POS_VTAS_POSIC_PER"));
                    posicionBean.setCpasInicPeriodo(resultSet.getDouble("POS_CPAS_POSIC_PER"));
                    posicionBean.setPosActual(resultSet.getDouble("POS_POSIC_ACTUAL"));
                    posicionBean.setNomCustodio(resultSet.getString("POS_NOM_CUSTODIO"));
                    posicionBean.setNumInstrumento(resultSet.getInt("POS_NUM_INSTRUME"));
                    posicionBean.setTipoMerca(resultSet.getInt("POS_CVE_TIPO_MERCA"));
                    posicionBean.setPosDisp(resultSet.getDouble("POS_DISPO"));
                    posicionBean.setPosComprom(resultSet.getDouble("POS_POSIC_COMPROM"));
                    posicionBean.setPosCostHist(resultSet.getDouble("POS_COSTO_HISTORIC"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return posicionBean;
    }

    public EmisionBean onTesoreria_existeEmisionSalida(String sPizarra, String sSerie, int iInstrume, int iMercado) {

        EmisionBean emisionBean = new EmisionBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT EMI_NUM_CUPON_VIG, ");
            stringBuilder.append(" EMI_NUM_MONEDA,  ");
            stringBuilder.append(" EMI_NUM_SEC_EMIS,  ");
            stringBuilder.append(" EMI_CVE_ST_EMISION,  ");
            stringBuilder.append(" COALESCE(EMB_ANO_INI_BLOQUE * 10000 + EMB_MES_INI_BLOQUE * 100 + EMB_DIA_INI_BLOQUE,0) FEC_INICIO, ");
            stringBuilder.append(" COALESCE(EMB_ANO_FIN_BLOQUE * 10000 + EMB_MES_FIN_BLOQUE * 100 + EMB_DIA_FIN_BLOQUE,0) FEC_FIN, ");
            stringBuilder.append(" EMB_CVE_COMPRAS, ");
            stringBuilder.append(" EMB_CVE_VENTAS ");
            stringBuilder.append(" FROM SAF.EMISION ");
            stringBuilder.append(" LEFT OUTER JOIN SAF.EMIBLOQU ");
            stringBuilder.append(" ON EMB_NOM_PIZARRA = EMI_NOM_PIZARRA ");
            stringBuilder.append(" AND EMB_NUM_SER_EMIS = EMI_NUM_SER_EMIS ");
            stringBuilder.append(" AND EMB_NUM_CUPON_VIG = EMI_NUM_CUPON_VIG ");
            stringBuilder.append(" AND EMB_NUM_INSTRUME = EMI_NUM_INSTRUME ");
            stringBuilder.append(" WHERE EMI_CVE_TIPO_MERCA = ? ");
            stringBuilder.append(" AND EMI_NUM_INSTRUME =  ? ");
            stringBuilder.append(" AND EMI_NOM_PIZARRA =  ? ");
            stringBuilder.append(" AND EMI_NUM_SER_EMIS =  ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, iMercado);
            preparedStatement.setInt(2, iInstrume);
            preparedStatement.setString(3, sPizarra);
            preparedStatement.setString(4, sSerie);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            emisionBean.setTipoMercado(0);
            emisionBean.setNumInstrume(0);

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("EMI_CVE_ST_EMISION").equals("ACTIVO")) {

                        emisionBean.setStatus(resultSet.getString("EMI_CVE_ST_EMISION"));
                        emisionBean.setCupon(resultSet.getInt("EMI_NUM_CUPON_VIG"));
                        emisionBean.setNumMoneda(resultSet.getInt("EMI_NUM_MONEDA"));
                        emisionBean.setNumSecEmis(resultSet.getInt("EMI_NUM_SEC_EMIS"));

                        // _Se_guardan_temporalemente_para_validar_con_la_fecha_del_movimiento
                        if (resultSet.getInt("FEC_INICIO") != 0 && resultSet.getInt("FEC_FIN") != 0) {
                            emisionBean.setTipoMercado(resultSet.getInt("FEC_INICIO"));
                            emisionBean.setNumInstrume(resultSet.getInt("FEC_FIN"));
                        }
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return emisionBean;
    }

    public int onTesoreria_Insert_CargaInterfaz(CargaInterfazBean cargaInterfazBean) {

        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        int result = 0;

        java.sql.Date dateFec = new java.sql.Date(cargaInterfazBean.getFecha().getTime());

        try {
            // Set_SQL
            stringBuilder.append("INSERT INTO SAF.CARGA_INTERFAZ (CARINT_NUM_USUARIO, RUT_ID_RUTINA, CARINT_FECHA, CARINT_SEC_ARCH, CARINT_SECUENCIAL, CARINT_NOM_PATH, CARINT_NOM_ARCH, CARINT_ARCH_TMP, CARINT_CADENA, CARINT_ESTATUS, CARINT_MENSAJE)");
            stringBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            preparedStatement.setString(2, "MASIVO ENTRADAS-SALIDAS");
            preparedStatement.setDate(3, dateFec);
            preparedStatement.setInt(4, cargaInterfazBean.getSecuencialArchivo());
            preparedStatement.setInt(5, cargaInterfazBean.getSecuencial());
            preparedStatement.setString(6, cargaInterfazBean.getRuta());
            preparedStatement.setString(7, cargaInterfazBean.getNombreArchivo());
            preparedStatement.setString(8, cargaInterfazBean.getArchivoTemporal());
            preparedStatement.setString(9, cargaInterfazBean.getCadena());
            preparedStatement.setString(10, cargaInterfazBean.getEstatus());
            preparedStatement.setString(11, cargaInterfazBean.getMensaje());

            // Execute_Update
            result = preparedStatement.executeUpdate();
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return result;
    }

    public String onTesoreria_ConsultaTipoValua(int numInterm) {

        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        String result = "";

        try {
            // Set_SQL
            stringBuilder.append("SELECT INT_TIPO_VALUA FROM SAF.INTERMED WHERE INT_ENTIDAD_FIN = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, numInterm);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Set_Values
                    result = resultSet.getString("INT_TIPO_VALUA");
                }
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return result;
    }

    public double onTesoreria_ConsultaValorNominal(int numInstrume, int iMercado) {

        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        double result = 0;

        try {
            // Set_SQL
            stringBuilder.append("SELECT INS_CVE_BAJO_PAR, INS_VALOR_NOMINAL FROM SAF.INSTRUME where INS_NUM_INSTRUME = ? And INS_CVE_TIPO_MERCA = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, numInstrume);
            preparedStatement.setInt(2, iMercado);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getInt("INS_CVE_BAJO_PAR") != 0) {
                        result = resultSet.getDouble("INS_VALOR_NOMINAL");
                    } else {
                        result = 0;
                    }
                }
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return result;
    }

    //Validación Bajo Par
    public double onTesoreria_ConsultaBajoPar(int numInstrume, int iMercado) {

        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        int result = 0;

        try {
            // Set_SQL
            stringBuilder.append("SELECT INS_CVE_BAJO_PAR, INS_VALOR_NOMINAL FROM SAF.INSTRUME where INS_NUM_INSTRUME = ? And INS_CVE_TIPO_MERCA = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, numInstrume);
            preparedStatement.setInt(2, iMercado);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    result = resultSet.getInt("INS_CVE_BAJO_PAR");
                }
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return result;
    }

    public void onTesoreria_Delete_CargaInterfaz() {

        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        //int result = 0;
        try {
            // Set_SQL
            stringBuilder.append("DELETE FROM SAF.CARGA_INTERFAZ WHERE RUT_ID_RUTINA = ? AND CARINT_NUM_USUARIO = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, "MASIVO ENTRADAS-SALIDAS");
            preparedStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // Execute_Update
            preparedStatement.executeUpdate();

            onCierraConexion();
        } catch (SQLException sqlException) {
            mensajeError += "Ocurrio el siguiente error al eliminar Carga Interfaz: " + sqlException.getMessage();
        } finally {
            onCierraConexion();
        }

    }

    public OutParameterBean onTesoreria_Ejecuta_CargaMasiva(Date FecVal, String sPath, String nomArch, int iOpcion) {

        OutParameterBean outParameterBean = new OutParameterBean();

        java.sql.Date dateFec = new java.sql.Date(FecVal.getTime());

        // SQL
        String sql = "{CALL DB2FIDUC.SPN_VAL_ENTSAL_MAS (?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setDate(1, dateFec);
            callableStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            callableStatement.setString(3, sPath);
            callableStatement.setString(4, nomArch);
            callableStatement.setInt(5, iOpcion);

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("iResultado", Types.INTEGER);
            callableStatement.registerOutParameter("iREGISTROS", Types.INTEGER);
            callableStatement.registerOutParameter("iCORRECTOS", Types.INTEGER);
            callableStatement.registerOutParameter("iCONERROR", Types.INTEGER);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iNumResult = callableStatement.getInt("iResultado");
            int iNumRegistros = callableStatement.getInt("iREGISTROS");
            int iNumCorrectos = callableStatement.getInt("iCORRECTOS");
            int iNumiCorrectos = callableStatement.getInt("iCONERROR");
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT") != null ? callableStatement.getString("PS_MSGERR_OUT") : new String();
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            outParameterBean.setdImporteCobrado(iNumCorrectos);
            outParameterBean.setiNumFolioContab(iNumiCorrectos);
            outParameterBean.setPsMsgErrOut(sMsErr);

            if (iNumiCorrectos == 0 && sMsErr.equals("")) {
                bitacoraBean.setDetalle("Resultado: " + iNumResult + " Registros: " + iNumRegistros + " Correctos: " + iNumCorrectos + " Incorrectos: " + iNumiCorrectos);
            } else {
                bitacoraBean.setDetalle("Error en Carga Masiva: " + sMsErr + ", SqlCode: " + sCode
                        + " SqlState: " + sEstado + " Resultado: " + iNumResult + " Registros: " + iNumRegistros + " Correctos: " + iNumCorrectos + " Incorrectos: " + iNumiCorrectos);
            }

            // SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre("Carga Masiva Entradas/Salidas Físicas".toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_ENTRADA_SALIDA".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public OutParameterBean onTesoreria_EjecutaPEPS(int iFideicomiso, int iSubFiso, Date dFecha,
            int EntidadFinan, double CtoInver, String sNomPizarra,
            String sSerieEmis, int iCuponVig, int iTitulos) {

        OutParameterBean outParameterBean = new OutParameterBean();

        // Format 
        java.sql.Date dateFec = new java.sql.Date(dFecha.getTime());

        try {

            // SQL
            String sql = "{CALL DB2FIDUC.SPN_DATOS_PEPS (?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, iFideicomiso);
            callableStatement.setInt(2, iSubFiso);
            callableStatement.setDate(3, dateFec);
            callableStatement.setInt(4, EntidadFinan);
            callableStatement.setDouble(5, CtoInver);
            callableStatement.setString(6, sNomPizarra);
            callableStatement.setString(7, sSerieEmis);
            callableStatement.setInt(8, iCuponVig);
            callableStatement.setInt(9, iTitulos);

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("vdmporte", Types.DOUBLE);
            callableStatement.registerOutParameter("vlTitAsignados", Types.INTEGER);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER           
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (sMsErr.equals("")) {
                outParameterBean.setbEjecuto(1);

                outParameterBean.setdImporteCobrado(callableStatement.getDouble("vdmporte"));
                outParameterBean.setiNumFolioContab(callableStatement.getInt("vlTitAsignados"));
            } else {
                outParameterBean.setPsMsgErrOut(sMsErr.replace("ERROR", ""));
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);

                // SET_VALUES_BITACORA
                bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
                bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .get("usuarioTerminal").toString());
                bitacoraBean.setFuncion("TESORERIA_SALIDA_PEPS".toUpperCase());

                // INSERT_BITACORA
                onTesoreria_InsertBitacora(bitacoraBean);
            }
            onCierraConexion();

        } catch (NumberFormatException | SQLException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * R E P O R T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    public Map<String, String> onTesoreria_GetTipoValorReporto(ArrayList<Integer> sClaves) {

        int NumInstrume = 0;
        String[] Div_Instrume;
        StringBuilder builder = new StringBuilder();
        StringBuilder QuerySql = new StringBuilder();
        Map<String, String> tipoValor = new LinkedHashMap<String, String>();

        try {
            // SQL
            QuerySql.append("SELECT INS_MNEMO_INSTRUME, INS_CVE_TIPO_MERCA, INS_NUM_INSTRUME, CVE_DESC_CLAVE, INS_NOM_INSTRUME ");
            QuerySql.append("FROM SAF.INSTRUME ");
            QuerySql.append("INNER JOIN SAF.CLAVES ");
            QuerySql.append("ON CVE_NUM_CLAVE = 45 ");
            QuerySql.append("AND CVE_NUM_SEC_CLAVE = INS_CVE_TIPO_MERCA ");
            QuerySql.append("WHERE INS_CVE_ST_INSTRUM ='ACTIVO' ");

            // _Validar_si_existe_una_condicion_adicional_para_el_Tipo_de_Valor
            if (sClaves.size() > 0) {

                for (int i = 0; i < sClaves.size(); i++) {
                    // Cantidad_de_?_parametros
                    builder.append("?,");
                }

                QuerySql.append(" AND CVE_NUM_SEC_CLAVE IN (" + builder.deleteCharAt(builder.length() - 1).toString() + ") ");
            }

            QuerySql.append(" ORDER BY 1");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Parameters			
            if (sClaves.size() > 0) {

                for (int i = 0; i < sClaves.size(); i++) {
                    //_Parametros
                    preparedStatement.setInt((i + 1), sClaves.get(i));
                }
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                tipoValor.put(String.valueOf(0), "");

                while (resultSet.next()) {
                    @SuppressWarnings("unused")
                    String ValInstrume2 = ""; //ValInstrume1 = "", 
                    // _Recuperar_Valor_Cto._de_Inversion_e_Intermediario_(Entidad_Financiera)
                    if (resultSet.getString("INS_NOM_INSTRUME").contains("/")) {
                        Div_Instrume = resultSet.getString("INS_NOM_INSTRUME").split("/");
                        //ValInstrume1 = Div_Instrume[0];
                        ValInstrume2 = Div_Instrume[1];
                    } else {
                        ValInstrume2 = resultSet.getString("INS_NOM_INSTRUME");
                    }

                    NumInstrume = Integer.parseInt(resultSet.getString("INS_CVE_TIPO_MERCA")) * 1000
                            + Integer.parseInt(resultSet.getString("INS_NUM_INSTRUME"));

                    // Add_List
                    tipoValor.put(String.valueOf(NumInstrume), resultSet.getString("INS_MNEMO_INSTRUME") + " / "
                            + resultSet.getString("CVE_DESC_CLAVE") + " / " + ValInstrume2);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return tipoValor;
    }

    public Map<String, String> onTesoreria_CargaMonedas() {

        StringBuilder QuerySql = new StringBuilder();
        Map<String, String> Monedas = new LinkedHashMap<String, String>();

        try {
            // SQL
            // validar_fecha);
            QuerySql.append("SELECT MON_NUM_PAIS, MON_NOM_MONEDA ");
            QuerySql.append("FROM SAF.MONEDAS ");
            QuerySql.append("ORDER BY MON_NUM_PAIS ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                Monedas.put(String.valueOf(0), "");

                while (resultSet.next()) {
                    // Add_Map
                    Monedas.put(resultSet.getString("MON_NUM_PAIS"), resultSet.getString("MON_NUM_PAIS") + " - " + resultSet.getString("MON_NOM_MONEDA"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Monedas;
    }

    public boolean onTesoreria_Valida_Instrumento(int iMercado, int iInstrumento) {

        StringBuilder QuerySql = new StringBuilder();
        boolean valorRetorno = false;
        try {
            // SQL);
            QuerySql.append("SELECT INS_CVE_ST_INSTRUM ");
            QuerySql.append("FROM SAF.INSTRUME ");
            QuerySql.append("WHERE INS_CVE_TIPO_MERCA = ? ");
            QuerySql.append("AND INS_NUM_INSTRUME = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Set_Values
            preparedStatement.setInt(1, iMercado);
            preparedStatement.setInt(2, iInstrumento);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (!resultSet.getString("INS_CVE_ST_INSTRUM").equals("")) {
                        valorRetorno = true;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public EmisionBean onTesoreria_ValidaEmision_Reporto(int iMercado, int iInstrume, int iSecEmis) {

        StringBuilder QuerySql = new StringBuilder();
        EmisionBean emisionBean = new EmisionBean();

        try {
            // SQL
            QuerySql.append("SELECT EMI_NUM_CUPON_VIG, EMI_NUM_MONEDA, EMI_CVE_ST_EMISION ");
            QuerySql.append("FROM SAF.EMISION ");
            QuerySql.append("WHERE EMI_CVE_TIPO_MERCA = ? ");
            QuerySql.append("AND EMI_NUM_INSTRUME = ? ");
            QuerySql.append("AND EMI_NUM_SEC_EMIS = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Set_Values
            preparedStatement.setInt(1, iMercado);
            preparedStatement.setInt(2, iInstrume);
            preparedStatement.setInt(3, iSecEmis);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    // Set_Values
                    emisionBean.setStatus(resultSet.getString("EMI_CVE_ST_EMISION"));
                    emisionBean.setCupon(resultSet.getInt("EMI_NUM_CUPON_VIG"));
                    emisionBean.setNumMoneda(resultSet.getInt("EMI_NUM_MONEDA"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return emisionBean;
    }

    public OutParameterBean onTesoreria_Ejecuta_ReportoPagare(String FecVal, int iFecValorMSA, String sOrigen, int Fideicomiso,
            int SubFiso, String sTipoMov, String sRedacc, String dTasa, int dPlazo, String NomPizarra, String SerieEmis,
            int CuponVig, double Titulos, double PrecioEmis, int EntidadFinan, int iMercado, int iInstrumento,
            int iSecEmis, int iCustodio, int iMoneda, double dCtoInver, double dImporte, String fecMovto,
            String sPremio, String sComision, String sIVAComis, String sISR, String sRetieneISR, double tipoCambio,
            int iSubOper, int iRefCICS, int iTipoNeg, int iClasif, int iAbierCerrado, String sAdmonProp, int iProrrateo,
            String sManSubFis) {

        OutParameterBean outParameterBean = new OutParameterBean();

        // cambiar_fecha
        try {
            Date diaOpera = this.dateParse(FecVal);
            Date FecProyecta = this.dateParse(fecMovto);

            // SQL
            String sql = "{CALL DB2FIDUC.SP_MD_CPA_PAGREP (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setDate(1, java.sql.Date.valueOf(this.dateFormat(diaOpera)));
            callableStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            callableStatement.setInt(3, iFecValorMSA);
            callableStatement.setString(4, sOrigen);
            callableStatement.setInt(5, Fideicomiso);
            callableStatement.setInt(6, SubFiso);
            callableStatement.setString(7, sTipoMov);
            callableStatement.setString(8, sRedacc);
            callableStatement.setString(9, dTasa);
            callableStatement.setInt(10, dPlazo);
            callableStatement.setString(11, NomPizarra);
            callableStatement.setString(12, SerieEmis);
            callableStatement.setInt(13, CuponVig);
            callableStatement.setDouble(14, Titulos);
            callableStatement.setDouble(15, PrecioEmis);
            callableStatement.setInt(16, EntidadFinan);
            callableStatement.setInt(17, iMercado);
            callableStatement.setInt(18, iInstrumento);
            callableStatement.setInt(19, iSecEmis);
            callableStatement.setInt(20, iCustodio);
            callableStatement.setInt(21, iMoneda);
            callableStatement.setDouble(22, dCtoInver);
            callableStatement.setDouble(23, dImporte);
            callableStatement.setDate(24, java.sql.Date.valueOf(this.dateFormat(FecProyecta)));
            callableStatement.setString(25, sPremio);
            callableStatement.setString(26, sComision);
            callableStatement.setString(27, sIVAComis);
            callableStatement.setString(28, sISR);
            callableStatement.setString(29, sRetieneISR);
            callableStatement.setDouble(30, tipoCambio);
            callableStatement.setInt(31, iSubOper);
            callableStatement.setInt(32, iRefCICS);
            callableStatement.setInt(33, iTipoNeg);
            callableStatement.setInt(34, iClasif);
            callableStatement.setInt(35, iAbierCerrado);
            callableStatement.setString(36, sAdmonProp);
            callableStatement.setInt(37, iProrrateo);
            callableStatement.setString(38, sManSubFis);

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PI_FOL_CONTA", Types.INTEGER);
            callableStatement.registerOutParameter("PI_FOL_REPORTO", Types.INTEGER);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iNumFolio = callableStatement.getInt("PI_FOL_CONTA");
            int iNumFolioRepor = callableStatement.getInt("PI_FOL_REPORTO");
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (iNumFolio > 0 && sMsErr.equals("")) {
                outParameterBean.setiNumFolioContab(iNumFolio);
                outParameterBean.setbEjecuto(iNumFolioRepor);
                bitacoraBean.setDetalle("Operación Aplicada con el folio: " + iNumFolio);
            } else {
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            }

            // SET_VALUES_BITACORA
            outParameterBean.setPsMsgErrOut(sMsErr);
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoMov.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_REPORTO_PAGARE".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException | ParseException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }
        return outParameterBean;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * P A G A R E S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public Map<String, String> onTesoreria_Carga_Intermediarios() {

        StringBuilder QuerySql = new StringBuilder();
        Map<String, String> Intermediarios = new LinkedHashMap<String, String>();

        try {
            // SQL
            QuerySql.append("SELECT INT_ENTIDAD_FIN, INT_INTERMEDIARIO ");
            QuerySql.append("FROM SAF.INTERMED ");
            QuerySql.append("ORDER BY INT_INTERMEDIARIO ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Vacio_el_primero
                Intermediarios.put(String.valueOf(0), "");

                while (resultSet.next()) {

                    // Add_List
                    Intermediarios.put(String.valueOf(resultSet.getString("INT_ENTIDAD_FIN")),
                            resultSet.getString("INT_INTERMEDIARIO"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Intermediarios;
    }

    public int onTesoreria_Busca_Sec_Clave(int nClave, String sAdicional) {

        int iNumSec = 0;
        StringBuilder QuerySql = new StringBuilder();

        try {
            // SQL
            QuerySql.append("SELECT CVE_NUM_SEC_CLAVE  ");
            QuerySql.append("FROM SAF.CLAVES ");
            QuerySql.append("WHERE CVE_NUM_CLAVE = ? ");
            QuerySql.append("AND CVE_DESC_CLAVE = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Parameters
            preparedStatement.setInt(1, nClave);
            preparedStatement.setString(2, sAdicional);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Add_List
                    iNumSec = resultSet.getInt("CVE_NUM_SEC_CLAVE");
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return iNumSec;
    }

    public double onTesoreria_Get_TipoCambio(int iMoneda) {

        double tipoCambio = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            // _Validar_lo_de_la_fecha
            stringBuilder.append("SELECT TIC_IMP_TIPO_CAMB ");
            stringBuilder.append("FROM SAF.TIPOCAMB  ");
            stringBuilder.append("WHERE TIC_NUM_PAIS = ? ");
            stringBuilder.append("AND TIC_ANO_ALTA_REG  = ? ");
            stringBuilder.append("AND TIC_MES_ALTA_REG = ? ");
            stringBuilder.append("AND TIC_DIA_ALTA_REG = ? ");
            stringBuilder.append("ORDER BY TIC_ANO_ULT_MOD DESC, TIC_MES_ULT_MOD DESC, TIC_DIA_ULT_MOD DESC, TIC_HORA_ALTA DESC, TIC_MINUTO_ALTA DESC ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, iMoneda);
            preparedStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño").toString()));
            preparedStatement.setInt(3, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes").toString()));
            preparedStatement.setInt(4, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaDia").toString()));

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("TIC_IMP_TIPO_CAMB") != null) {
                        tipoCambio = resultSet.getDouble("TIC_IMP_TIPO_CAMB");
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return tipoCambio;
    }

    public String onTesoreria_VerificaMoneda_Pagare(ConreporBean pagare) {

        StringBuilder stringBuilder = new StringBuilder();
        String mensajeErrorConsulta = new String();

        try {
            stringBuilder.append("SELECT CPR_MONEDA_CTA, MON_NOM_MONEDA FROM SAF.CONTINTE ");
            stringBuilder.append("INNER JOIN MONEDAS ON MON_NUM_PAIS = CPR_MONEDA_CTA ");
            stringBuilder.append("WHERE CPR_NUM_CONTRATO = ? AND ");
            stringBuilder.append("CPR_CONTRATO_INTER = ? AND ");
            stringBuilder.append("CPR_ENTIDAD_FIN = ? AND CPR_NUM_CUENTA = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Integer.parseInt(pagare.getFideicomiso()));
            preparedStatement.setDouble(2, pagare.getCtoInver());
            preparedStatement.setInt(3, pagare.getEntidadFinan());
            preparedStatement.setString(4, pagare.getCuentaCheques());
            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt("CPR_MONEDA_CTA") == pagare.getMoneda()) {
                        mensajeErrorConsulta = new String();
                    } else {
                        mensajeErrorConsulta = "El contrato de inversión solo opera en: " + resultSet.getString("MON_NOM_MONEDA") + ", por favor seleccione otra";
                    }
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return mensajeErrorConsulta;
    }

    public boolean onTesoreria_VerificaLiquidez_Pagare(double Importe, String fideicomiso, String SubFiso, int iEntidadFinan,
            double dCtoInver, int iCustodio) {

        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {

            // SQL
            // _Valida_liquidez
            // _1103-1-2-1_con_Aux_3_la_cuenta_de_cheques_del_contrato_de_Inversión
            stringBuilder.append("SELECT SALD_SALDO_ACTUAL, CPR_NUM_CUENTA ");
            stringBuilder.append("FROM SAF.CONTINTE ");
            stringBuilder.append("INNER JOIN SAF.FDSALDOS ON SALD_AX1 = CPR_NUM_CONTRATO ");
            stringBuilder.append("AND SALD_AX2 = CPR_SUB_CONTRATO ");
            stringBuilder.append("AND SALD_AX3 = CPR_NUM_CUENTA ");
            stringBuilder.append("AND CCON_CTA = 1103 ");
            stringBuilder.append("AND CCON_SCTA = 1 ");
            stringBuilder.append("AND CCON_2SCTA = 2 ");
            stringBuilder.append("AND CCON_3SCTA = 1 ");
            stringBuilder.append("AND MONE_ID_MONEDA = CPR_MONEDA_CTA ");
            stringBuilder.append("WHERE CPR_NUM_CONTRATO = ? ");
            stringBuilder.append("AND CPR_SUB_CONTRATO = ? ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN = ? ");
            stringBuilder.append("AND CPR_CONTRATO_INTER = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, fideicomiso);
            preparedStatement.setString(2, SubFiso);
            preparedStatement.setInt(3, iEntidadFinan);
            preparedStatement.setDouble(4, dCtoInver);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    if (Importe <= resultSet.getDouble("SALD_SALDO_ACTUAL")) {
                        valorRetorno = true;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O T R A S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public void onTesoreria_InsertBitacora(BitacoraBean bitacoraBean) {

        LogsContext.FormatoNormativo();
        StringBuilder stringBuilder = new StringBuilder();

        // DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);

        try {
            // Set_SQL
            stringBuilder.append("INSERT INTO SAF.BITACORA (BIT_FEC_TRANSAC, BIT_ID_TERMINAL, BIT_NUM_USUARIO, BIT_NOM_PGM, BIT_CVE_FUNCION, BIT_DET_BITACORA)");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?) ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(dateTime));
            preparedStatement.setString(2, bitacoraBean.getTerminal());
            preparedStatement.setInt(3, bitacoraBean.getUsuario());
            preparedStatement.setString(4, bitacoraBean.getNombre().substring(0, Math.min(bitacoraBean.getNombre().length(), 49)));
            preparedStatement.setString(5, bitacoraBean.getFuncion().substring(0, Math.min(bitacoraBean.getFuncion().length(), 24)));
            preparedStatement.setString(6, bitacoraBean.getDetalle().substring(0, Math.min(bitacoraBean.getDetalle().length(), 199)));

            //Execute_Update
            preparedStatement.executeUpdate();
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }
    }

    public ContratoBean onTesoreria_checkFideicomiso(int fideicomiso) {

        ContratoBean contratoBean = new ContratoBean();
        String sQuery = "";
        StringBuilder stringBuilder = new StringBuilder();

        try {

            // SQL
            sQuery = "SELECT C.CTO_NUM_CONTRATO, C.CTO_CVE_SUBCTO, ";
            sQuery += "C.CTO_NOM_CONTRATO, C.CTO_CVE_ST_CONTRAT, ";
            sQuery += "C.CTO_TIPO_ADMON, C.CTO_CVE_MON_EXT, ";
            sQuery += "C.CTO_NUM_NIVEL1, C.CTO_NUM_NIVEL2, ";
            sQuery += "C.CTO_NUM_NIVEL3, C.CTO_NUM_NIVEL4, ";
            sQuery += "C.CTO_NUM_NIVEL5, A.CVE_NUM_SEC_CLAVE TipoNeg, ";
            sQuery += "B.CVE_NUM_SEC_CLAVE clasif, C.CTO_CVE_REQ_SORS AbiertoCerrado, ";
            sQuery += "C.CTO_CVE_EXCLU_30 ImpEsp, C.CTO_SUB_RAMA Prorrate_SubFiso ";
            sQuery += "FROM ((((SAF.CONTRATO C ";
            sQuery += " LEFT OUTER JOIN SAF.CLAVES A ON A.CVE_NUM_CLAVE = 36 AND C.CTO_CVE_TIPO_NEG = A.CVE_DESC_CLAVE) ";
            sQuery += " LEFT OUTER JOIN SAF.CLAVES B ON B.CVE_NUM_CLAVE = 37 AND C.CTO_CVE_CLAS_PROD = B.CVE_DESC_CLAVE) ";
            //_Regionalización 
            sQuery += " INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = C.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? )";
            sQuery += " INNER JOIN SAF.USUARIOS U ON U.USU_NUM_USUARIO = V.USU_NUM_USUARIO ) ";
            // Recupera_Plaza_del_usuario 
            //sQuery += " LEFT OUTER JOIN SAF.CLAVES c1 ON c1.CVE_NUM_CLAVE = 6890 AND c1.CVE_NUM_SEC_CLAVE = ? ) "; 
            //sQuery += " LEFT OUTER JOIN SAF.CLAVES c2 ON c2.CVE_NUM_CLAVE = c1.CVE_LIMINF_CLAVE AND c2.CVE_NUM_SEC_CLAVE = C.CTO_NUM_NIVEL4 )";
            sQuery += " WHERE C.CTO_NUM_CONTRATO =  ? ";
            sQuery += " AND C.CTO_ES_EJE = ? ";
            sQuery += " AND C.CTO_CVE_ST_CONTRAT= 'ACTIVO' ";

            stringBuilder.append(sQuery);

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            // Recupera_Plaza_del_usuario 
            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            preparedStatement.setInt(2, fideicomiso);
            preparedStatement.setInt(3, 0);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                if (resultSet.next()) {

                    // Write_Log
                    contratoBean.setContrato(resultSet.getInt("CTO_NUM_CONTRATO"));
                    contratoBean.setNombre(resultSet.getString("CTO_NOM_CONTRATO"));

                    if (!resultSet.getString("CTO_TIPO_ADMON").equals("") && resultSet.getString("CTO_TIPO_ADMON").equals("SI")) {
                        contratoBean.setTipoAdmon("True");
                    } else {
                        contratoBean.setTipoAdmon("False");
                    }

                    contratoBean.setNivel1(resultSet.getInt("CTO_NUM_NIVEL1"));
                    contratoBean.setNivel2(resultSet.getInt("CTO_NUM_NIVEL2"));
                    contratoBean.setNivel3(resultSet.getInt("CTO_NUM_NIVEL3"));
                    contratoBean.setNivel4(resultSet.getInt("CTO_NUM_NIVEL4"));
                    contratoBean.setNivel5(resultSet.getInt("CTO_NUM_NIVEL5"));
                    contratoBean.setEstatus(resultSet.getString("CTO_CVE_ST_CONTRAT"));
                    contratoBean.setbSubContrato(false);

                    if (!resultSet.getString("CTO_CVE_SUBCTO").equals("")) {
                        if (Integer.parseInt(resultSet.getString("CTO_CVE_SUBCTO")) == 1) {
                            contratoBean.setbSubContrato(true);
                        }
                    }
                    if (!resultSet.getString("CTO_CVE_MON_EXT").equals("")
                            && Integer.parseInt(resultSet.getString("CTO_CVE_MON_EXT")) == 1) {
                        contratoBean.setMonedaExtranjera(resultSet.getInt("CTO_CVE_MON_EXT"));
                    }

                    contratoBean.setAbiertoCerrado(resultSet.getInt("AbiertoCerrado"));
                    contratoBean.setImpuestoEspecial(resultSet.getInt("ImpEsp"));

                    if (!resultSet.getString("TipoNeg").equals("")) {
                        contratoBean.setTipoNeg(resultSet.getInt("TipoNeg"));
                    } else {
                        contratoBean.setTipoNeg(0);
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Fideicomiso no tiene Tipo de Negocio, notifique al Administrador."));
                    }

                    if (!resultSet.getString("clasif").equals("")) {
                        contratoBean.setClasificacion(resultSet.getInt("clasif"));
                    } else {
                        contratoBean.setClasificacion(0);
                        FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                        "El Fideicomiso no tiene Clasificación, notifique al Administrador."));
                    }

                    if (!resultSet.getString("Prorrate_SubFiso").equals("")) {
                        contratoBean.setProrrateo(resultSet.getInt("Prorrate_SubFiso"));
                    } else {
                        contratoBean.setProrrateo(0);
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return contratoBean;
    }

    public boolean onTesoreria_CtoBloqueado(int fideicomiso, String sTipoBloque) {

        boolean VerificaContratoSrv = true;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // SQL
            stringBuilder.append("SELECT CTB_CONTRATO, CTB_SUB_CONTRATO, CTB_CVE_ST_CTOBLOQ ");
            stringBuilder.append(" CTB_ANO_INI_BLOQUE, ");
            stringBuilder.append(" CTB_MES_INI_BLOQUE,");
            stringBuilder.append(" CTB_DIA_INI_BLOQUE,");
            stringBuilder.append(" CTB_ANO_FIN_BLOQUE,");
            stringBuilder.append(" CTB_MES_FIN_BLOQUE,");
            stringBuilder.append(" CTB_DIA_FIN_BLOQUE,");
            stringBuilder.append(" CTB_CVE_ENTRADAS,");
            stringBuilder.append(" CTB_CVE_SALIDAS,");
            stringBuilder.append(" CTB_CVE_INVERSION,");
            stringBuilder.append(" CURRENT_DATE HOY,");
            stringBuilder.append(" DATE(RTRIM(CHAR(CTB_ANO_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_MES_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_DIA_FIN_BLOQUE))) ");
            stringBuilder.append(" FROM SAF.CTOBLOQU ");
            stringBuilder.append(" WHERE CTB_CONTRATO = ? ");
            stringBuilder.append(" AND CTB_SUB_CONTRATO = 0 ");
            stringBuilder.append(" AND CTB_CVE_ST_CTOBLOQ = 'ACTIVO' ");
            stringBuilder.append(" AND DATE(RTRIM(CHAR(CTB_ANO_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_MES_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_DIA_FIN_BLOQUE))) >= CURRENT_DATE ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, fideicomiso);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                if (resultSet.next()) {
                    //El contrato esta en la tabla de Bloqueados 
                    switch (sTipoBloque) {
                        case "E":
                            if (resultSet.getInt("CTB_CVE_ENTRADAS") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                        FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso Bloqueado para Entradas."));
                                VerificaContratoSrv = false;
                            }
                            break;

                        case "S":
                            if (resultSet.getInt("CTB_CVE_SALIDAS") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                        FacesMessage.SEVERITY_INFO, "Fiduciario", "Fideicomiso Bloqueado para Salidas."));
                                VerificaContratoSrv = false;
                            }
                            break;

                        case "I":
                            if (resultSet.getInt("CTB_CVE_INVERSION") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario",
                                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                                                "Fideicomiso Bloqueado para Inversiones."));
                                VerificaContratoSrv = false;
                            }
                            break;
                    }
                }
                //else_El contrato no esta en la tabla de Bloqueados
            }

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return VerificaContratoSrv;
    }

    public List<String> onTesoreria_ListadoSubContratoSrv(int fideicomiso) {

        List<String> listSubContratos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {

            // SQL
            stringBuilder.append("SELECT SCT_SUB_CONTRATO, SCT_NOM_SUB_CTO  ");
            stringBuilder.append("FROM SAF.SUBCONT ");
            stringBuilder.append("WHERE SCT_NUM_CONTRATO = ? ");
            stringBuilder.append("AND SCT_CVE_ST_SUBCONT = 'ACTIVO' ");
            //Ordernar por número de SubSifo
            stringBuilder.append("ORDER BY SCT_SUB_CONTRATO");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, fideicomiso);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Set_Rutinas
                listSubContratos.add("0");

                while (resultSet.next()) {

                    // Add_List
                    listSubContratos.add(String.valueOf(resultSet.getString("SCT_SUB_CONTRATO") + ".-" + resultSet.getString("SCT_NOM_SUB_CTO")));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return listSubContratos;
    }

    public boolean onTesoreria_VerificaSubContratoSrv(ContratoBean contratoBean, int iSubFiso, String sTipo) {

        boolean VerificaSubContratoSrv = false;
        StringBuilder stringBuilder = new StringBuilder();
        Connection connect = null;
        PreparedStatement pstmt = null;
        ResultSet results = null;

        try {

            if (contratoBean.getContrato() == 0) {
                return false;
            }

            if (iSubFiso == 0) {
                return true;
            }

            if (contratoBean.getbSubContrato() == false) {
                if (iSubFiso == 0) {
                    VerificaSubContratoSrv = true;
                } else {
                    FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Fiduciario", "Este Fideicomiso no puede tener SubFisos"));
                    VerificaSubContratoSrv = false;
                }
                onCierraConexion();
                return VerificaSubContratoSrv;
            }

            // SQL
            stringBuilder.append("SELECT SCT_SUB_CONTRATO, SCT_NOM_SUB_CTO  ");
            stringBuilder.append("FROM SAF.SUBCONT ");
            stringBuilder.append("WHERE SCT_NUM_CONTRATO = ? ");
            stringBuilder.append("AND SCT_SUB_CONTRATO = ? ");
            stringBuilder.append("AND SCT_CVE_ST_SUBCONT  = 'ACTIVO'");

            // Call_Operaciones_BD
            connect = DataBaseConexion.getInstance().getConnection();
            pstmt = connect.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setInt(1, contratoBean.getContrato());
            pstmt.setInt(2, iSubFiso);

            // Execute_Query
            results = pstmt.executeQuery();

            // Get_Data
            if (results != null) {
                // Set_Rutinas
                while (results.next()) {

                    int subContrato = results.getInt("SCT_SUB_CONTRATO");
                    //VerificaSubContratoSrv = true;

                    // _Validar_Bloqueo_de_SubContrato
                    VerificaSubContratoSrv = onTesoreria_Valida_SubContrato_Bloqueo(contratoBean.getContrato(), subContrato, sTipo);
                }
            }

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException sqlException) {
                mensajeError += "Error al cerrar la conexión: ".concat(sqlException.getMessage());
            }
            onCierraConexion();
        }

        return VerificaSubContratoSrv;
    }

    public boolean onTesoreria_Valida_SubContrato_Bloqueo(int Fiso, int iSubFiso, String sTipoBloque) {

        boolean VerificaContratoSrv = true;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT CTB_CONTRATO, CTB_SUB_CONTRATO, CTB_CVE_ST_CTOBLOQ ");
            stringBuilder.append(" CTB_ANO_INI_BLOQUE, ");
            stringBuilder.append(" CTB_MES_INI_BLOQUE, ");
            stringBuilder.append(" CTB_DIA_INI_BLOQUE, ");
            stringBuilder.append(" CTB_ANO_FIN_BLOQUE, ");
            stringBuilder.append(" CTB_MES_FIN_BLOQUE, ");
            stringBuilder.append(" CTB_DIA_FIN_BLOQUE, ");
            stringBuilder.append(" CTB_CVE_ENTRADAS, ");
            stringBuilder.append(" CTB_CVE_SALIDAS, ");
            stringBuilder.append(" CTB_CVE_INVERSION, ");
            stringBuilder.append(" CURRENT_DATE HOY, ");
            stringBuilder.append(" DATE(RTRIM(CHAR(CTB_ANO_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_MES_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_DIA_FIN_BLOQUE))) ");
            stringBuilder.append("FROM SAF.CTOBLOQU ");
            stringBuilder.append("WHERE CTB_CONTRATO = ? ");
            stringBuilder.append("AND CTB_SUB_CONTRATO = ? ");
            stringBuilder.append("AND CTB_CVE_ST_CTOBLOQ = 'ACTIVO' ");
            stringBuilder.append("AND DATE(RTRIM(CHAR(CTB_ANO_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_MES_FIN_BLOQUE))||'-'||RTRIM(CHAR(CTB_DIA_FIN_BLOQUE))) >= CURRENT_DATE ");

            // Call_Operaciones_BD 
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, Fiso);
            preparedStatement.setInt(2, iSubFiso);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                if (resultSet.next()) {
                    //El contrato esta en la Tabla de Bloqueados
                    switch (sTipoBloque) {
                        case "E":
                            if (resultSet.getInt("CTB_CVE_ENTRADAS") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                        FacesMessage.SEVERITY_INFO, "Fiduciario", "Subfiso Bloqueado para Entradas"));
                                VerificaContratoSrv = false;
                            }
                            break;

                        case "S":
                            if (resultSet.getInt("CTB_CVE_SALIDAS") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                        FacesMessage.SEVERITY_INFO, "Fiduciario", "Subfiso Bloqueado para Salidas."));
                                VerificaContratoSrv = false;
                            }
                            break;

                        case "I":
                            if (resultSet.getInt("CTB_CVE_INVERSION") != 0) {
                                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(
                                        FacesMessage.SEVERITY_INFO, "Fiduciario", "Subfiso bloqueado para Inversiones."));
                                VerificaContratoSrv = false;
                            }
                            break;
                    }
                }
                //else_El contrato no esta en la Tabla de Bloqueados
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return VerificaContratoSrv;
    }

    public FechaContableBean onTesoreria_GetFechaContable() {

        FechaContableBean fechaContableBean = new FechaContableBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT FCO_DIA_DIA, FCO_MES_DIA, FCO_ANO_DIA ");
            stringBuilder.append("FROM SAF.FECCONT ");
            stringBuilder.append("WHERE FCO_CVE_TIPO_FECHA = ? ");
            stringBuilder.append("AND FCO_CVE_ST_FECCONT  = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, "CONTABLE");
            preparedStatement.setString(2, "ACTIVO");

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Set_Fecha_Contable
                    fechaContableBean.setFechaContable(String.format("%02d", resultSet.getInt("FCO_DIA_DIA"))
                            .concat("/").concat(String.format("%02d", resultSet.getInt("FCO_MES_DIA")).concat("/")
                            .concat(String.format("%04d", resultSet.getInt("FCO_ANO_DIA")))));
                    fechaContableBean.setFecha(this.dateParse(fechaContableBean.getFechaContable()));
                }
            }
            onCierraConexion();
        } catch (SQLException | ParseException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return fechaContableBean;
    }

    public Map<String, String> onTesoreria_GetCustodio(int nClave, ArrayList<Integer> sSecuencial) {

        StringBuilder QuerySql = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        Map<String, String> claves = new LinkedHashMap<String, String>();

        try {
            // SQL
            QuerySql.append("SELECT CVE_NUM_SEC_CLAVE, CVE_DESC_CLAVE ");
            QuerySql.append("FROM SAF.CLAVES ");
            QuerySql.append("WHERE CVE_NUM_CLAVE = ? ");

            if (sSecuencial.size() > 0) {

                sSecuencial.forEach(_item -> {
                    //Cantidad_de_?_parametros
                    builder.append("?,");
                });

                QuerySql.append(" AND CVE_NUM_SEC_CLAVE IN (").append(builder.deleteCharAt(builder.length() - 1).toString()).append(") ");
            }

            QuerySql.append(" ORDER BY CVE_DESC_CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Parameters
            preparedStatement.setInt(1, nClave);

            if (sSecuencial.size() > 0) {

                for (int i = 0; i < sSecuencial.size(); i++) {
                    //_Parametros
                    preparedStatement.setInt((i + 2), sSecuencial.get(i));
                }
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Vacio_el_primero
                claves.put(String.valueOf(0), "");

                while (resultSet.next()) {

                    // Add_List
                    claves.put(String.valueOf(resultSet.getString("CVE_NUM_SEC_CLAVE")),
                            resultSet.getString("CVE_DESC_CLAVE"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return claves;
    }

    public Map<String, String> onTesoreria_GetTipoCambio(int iDia, int iMes, int iAnio, int iMercado, int iInstrume,
            String sPizarra, String sSerie, String sCupon) {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> Moneda = new LinkedHashMap<String, String>();

        try {
            // SQL
            stringBuilder.append("SELECT EMI_NUM_MONEDA, MON_NOM_MONEDA, COALESCE (TIC_IMP_TIPO_CAMB,1) AS TIPO_CAMB ");
            stringBuilder.append("FROM SAF.EMISION ");
            stringBuilder.append("INNER JOIN SAF.MONEDAS ");
            stringBuilder.append("ON MON_NUM_PAIS = EMI_NUM_MONEDA ");
            stringBuilder.append("LEFT OUTER JOIN SAF.TIPOCAMB  ");
            stringBuilder.append("ON TIC_NUM_PAIS = MON_NUM_PAIS ");
            stringBuilder.append("AND TIC_ANO_ALTA_REG  = ? ");
            stringBuilder.append("AND TIC_MES_ALTA_REG = ? ");
            stringBuilder.append("AND TIC_DIA_ALTA_REG = ? ");
            stringBuilder.append("WHERE EMI_CVE_TIPO_MERCA = ? ");
            stringBuilder.append("AND EMI_NUM_INSTRUME = ? ");
            stringBuilder.append("AND EMI_NOM_PIZARRA = ? ");
            stringBuilder.append("AND EMI_NUM_SER_EMIS = ? ");
            stringBuilder.append("AND EMI_NUM_CUPON_VIG= ? ");;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, iAnio);
            preparedStatement.setInt(2, iMes);
            preparedStatement.setInt(3, iDia);
            preparedStatement.setInt(4, iMercado);
            preparedStatement.setInt(5, iInstrume);
            preparedStatement.setString(6, sPizarra);
            preparedStatement.setString(7, sSerie);
            preparedStatement.setString(8, sCupon);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {

                    if (resultSet.getString("EMI_NUM_MONEDA") != null) {
                        // Add_Map
                        Moneda.put(String.valueOf(resultSet.getString("EMI_NUM_MONEDA")),
                                resultSet.getDouble("TIPO_CAMB") + " / " + resultSet.getString("MON_NOM_MONEDA"));

                    } else {
                        //Esta vacio tipo de Cambio
                        return Moneda;
                    }
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Moneda;
    }

    // End Class

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * R E C H A Z A D O S * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    public Map<String, String> onTesoreria_GetFormaManejo(int nClave) {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> claves = new LinkedHashMap<String, String>();

        try {
            // SQL
            stringBuilder.append("SELECT CVE_NUM_SEC_CLAVE, CVE_DESC_CLAVE ");
            stringBuilder.append(" FROM SAF.CLAVES ");
            stringBuilder.append(" WHERE CVE_NUM_CLAVE = ? ");
            stringBuilder.append(" AND CVE_CVE_ST_CLAVE='ACTIVO' ");
            stringBuilder.append(" ORDER BY CVE_DESC_CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, nClave);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                // Vacio_el_primero
                claves.put(String.valueOf(0), "");

                while (resultSet.next()) {

                    // Add_List
                    claves.put(String.valueOf(resultSet.getString("CVE_NUM_SEC_CLAVE")),
                            resultSet.getString("CVE_DESC_CLAVE"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return claves;
    }

    public List<String> onTesoreria_GetServicios(int nClave) {

        StringBuilder stringBuilder = new StringBuilder();
        List<String> servicios = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT MAX(CVE_NUM_SEC_CLAVE) NUM , CVE_FORMA_EMP_CVE ");
            stringBuilder.append(" FROM SAF.CLAVES ");
            stringBuilder.append(" WHERE CVE_NUM_CLAVE = ? ");
            stringBuilder.append(" AND CVE_CVE_ST_CLAVE='ACTIVO' ");
            stringBuilder.append(" AND RTRIM(CVE_FORMA_EMP_CVE) <> '' ");
            stringBuilder.append(" GROUP BY  CVE_FORMA_EMP_CVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, nClave);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    // Add_List
                    servicios.add(resultSet.getString("CVE_FORMA_EMP_CVE"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return servicios;
    }

    public List<InvRechazosBean> onTesoreria_ConsultaDescErr(String FecValor) {

        List<InvRechazosBean> redaccionErr = new ArrayList<>();

        StringBuilder QuerySql = new StringBuilder();
        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql.append("SELECT INV_NOM_FILE, INV_ROW_ID, INV_FEC_MOVTO, INV_CVE_OPERACION, INV_DESC_ERROR, INV_CTOINV, FISO ");
            QuerySql.append("FROM ( SELECT INV_NOM_FILE, INV_ROW_ID, INV_FEC_MOVTO, INV_CVE_OPERACION, INV_DESC_ERROR, INV_CTOINV, ");
            QuerySql.append("(SELECT DISTINCT CPR_NUM_CONTRATO ");
            QuerySql.append("FROM SAF.CONTINTE ");
            QuerySql.append("WHERE CPR_CONTRATO_INTER = CAST(DECIMAL(INV_CTOINV,11,0)  AS BIGINT) ");
            QuerySql.append("AND CPR_ENTIDAD_FIN =7) FISO ");
            QuerySql.append("FROM SAF.INV_RECHAZOS ");
            QuerySql.append("INNER JOIN SAF.CLAVES c131 On c131.cve_num_clave = 131 ");
            QuerySql.append("AND  c131.CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            QuerySql.append("WHERE (SUBSTR(COALESCE(INV_DESC_ERROR,''),1,3)= '7.-') ");
            QuerySql.append("AND (INV_DESC_ERROR LIKE '%NO EXISTE%') ");
            QuerySql.append("AND  INV_FEC_MOVTO >= ? ");
            QuerySql.append("AND INV_CVE_OPERACION != 'EFECTIVO' AND INV_ORIGEN ='ARCHIVO_MEDI' ) AJST  WHERE NOT FISO IS NULL");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazosBean invRechazosBean = new InvRechazosBean();

                    // Set_Values
                    invRechazosBean.setNom_File(resultSet.getString("INV_NOM_FILE"));
                    invRechazosBean.setRow_Id(resultSet.getInt("INV_ROW_ID"));
                    invRechazosBean.setFec_Movto(resultSet.getDate("INV_FEC_MOVTO"));
                    invRechazosBean.setCve_Operacion(resultSet.getString("INV_CVE_OPERACION"));
                    invRechazosBean.setDesc_Error(resultSet.getString("INV_DESC_ERROR"));
                    invRechazosBean.setCto_Inv(resultSet.getString("INV_CTOINV"));

                    // Add_List
                    redaccionErr.add(invRechazosBean);
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return redaccionErr;
    }

    public boolean onTesoreria_AlineaDescErr(List<InvRechazosBean> redaccionErr) {
        boolean valorRetorno = false;
        try {

            int resultSet = 0;

            for (int i = 0; i < redaccionErr.size(); i++) {

                StringBuilder stringBuilder = new StringBuilder();

                // SQL
                stringBuilder.append("UPDATE SAF.INV_RECHAZOS set INV_DESC_ERROR = replace(INV_DESC_ERROR,'NO EXISTE','') ");
                stringBuilder.append(" WHERE INV_NOM_FILE = ? ");
                stringBuilder.append(" AND INV_ROW_ID = ? ");
                stringBuilder.append(" AND INV_FEC_MOVTO = ? ");
                stringBuilder.append(" AND INV_CVE_OPERACION = ? ");
                stringBuilder.append(" AND INV_DESC_ERROR = ? ");
                stringBuilder.append(" AND INV_CTOINV = ? ");

                // Call_Operaciones_BD
                conexion = DataBaseConexion.getInstance().getConnection();
                preparedStatement = conexion.prepareStatement(stringBuilder.toString());

                // Set_Values
                preparedStatement.setString(1, redaccionErr.get(i).getNom_File());
                preparedStatement.setInt(2, redaccionErr.get(i).getRow_Id());
                preparedStatement.setDate(3, java.sql.Date.valueOf(redaccionErr.get(i).getFec_Movto().toString()));
                preparedStatement.setString(4, redaccionErr.get(i).getCve_Operacion());
                preparedStatement.setString(5, redaccionErr.get(i).getDesc_Error());
                preparedStatement.setString(6, redaccionErr.get(i).getCto_Inv());

                // Execute_Update
                resultSet = preparedStatement.executeUpdate();
                conexion.close();
                preparedStatement.close();
            }

            if (resultSet > 0) {
                valorRetorno = true;
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public List<InvRechazosBean> onTesoreria_CtoNoIdentificados(String FecValor, String sCtoInv) {

        StringBuilder stringBuilder = new StringBuilder();
        List<InvRechazosBean> noIdentificados = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT INV_CTOINV CTO_INV, INV_ENTIDAD_FIN NUM_INTERMEDIARIO,");
            stringBuilder.append(" COALESCE(INT_INTERMEDIARIO, RTRIM(CHAR(INV_ENTIDAD_FIN))) INTERMEDIARIO,");
            stringBuilder.append(" INV_NUM_CUSTODIO NUM_CUSTODIO,");
            stringBuilder.append(" COALESCE(NOM_CUSTODIO,RTRIM(CHAR(INV_NUM_CUSTODIO))) CUSTODIO,");
            stringBuilder.append(" CASE SUM(LOCATE('NO EXISTE',INV_DESC_ERROR)) WHEN 0 THEN 0 ELSE 1 END NO_EXISTE, COUNT(*) NUM_REGS ");
            stringBuilder.append("FROM (SELECT DECIMAL(INV_CTOINV,11,0) INV_CTOINV, SMALLINT(INV_ENTIDAD_FIN) INV_ENTIDAD_FIN, SMALLINT(INV_NUM_CUSTODIO) INV_NUM_CUSTODIO, INV_DESC_ERROR, ");
            stringBuilder.append(" (select count(*) ");
            stringBuilder.append("FROM SAF.CONTINTE ");
            stringBuilder.append("WHERE CPR_CONTRATO_INTER = CAST(DECIMAL(INV_CTOINV,11,0)  AS BIGINT) ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN =7) CTOS ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND INV_CVE_OPERACION !='EFECTIVO' ");
            stringBuilder.append("AND INV_DESC_ERROR not like '%LIGADO%' ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_FEC_MOVTO >=  ? ");
            stringBuilder.append("AND INV_NUM_CTO = '0' )INV ");
            stringBuilder.append("LEFT JOIN SAF.INTERMED ON INT_ENTIDAD_FIN = INV_ENTIDAD_FIN ");
            stringBuilder.append("LEFT JOIN (SELECT CVE_NUM_SEC_CLAVE NUM_CUSTODIO, CVE_DESC_CLAVE NOM_CUSTODIO ");
            stringBuilder.append(" FROM SAF.CLAVES ");
            stringBuilder.append(" WHERE CVE_NUM_CLAVE=386) CUSTODIO ON NUM_CUSTODIO = INV_NUM_CUSTODIO ");
            stringBuilder.append(" WHERE INV.CTOS = 0 ");

            if (!sCtoInv.equals("")) {
                stringBuilder.append(" AND INV_CTOINV = ? ");
            }
            stringBuilder.append(" GROUP BY INV_CTOINV, INV_ENTIDAD_FIN, INT_INTERMEDIARIO, INV_NUM_CUSTODIO, NOM_CUSTODIO ");
            stringBuilder.append(" ORDER BY NUM_REGS DESC,CTO_INV");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);

            if (!sCtoInv.equals("")) {
                preparedStatement.setDouble(2, Double.parseDouble(sCtoInv));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazosBean invRechazosBean = new InvRechazosBean();

                    // Set_Values
                    invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                    invRechazosBean.setNum_Entidad_Financ(resultSet.getInt("NUM_INTERMEDIARIO"));
                    invRechazosBean.setEntidad_Financ(resultSet.getString("INTERMEDIARIO"));
                    invRechazosBean.setNum_Custodio(resultSet.getInt("NUM_CUSTODIO"));
                    invRechazosBean.setCustodio(resultSet.getString("CUSTODIO"));
                    invRechazosBean.setNo_Existe(resultSet.getInt("NO_EXISTE"));
                    invRechazosBean.setNum_Regis(resultSet.getInt("NUM_REGS"));

                    // Add_List
                    noIdentificados.add(invRechazosBean);
                }
            }
            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return noIdentificados;
    }

    public java.sql.Date ConvertirFechayyyyMMdd(String FecValor) {
        java.sql.Date FechaSql = null;

        Date FechaConsulta;
        try {

            FechaConsulta = formatDate(FecValor);
            FechaSql = new java.sql.Date(FechaConsulta.getTime());

        } catch (ParseException e) {
            mensajeError += "Descripción: " + e.getMessage() + nombreObjeto + "ConvertirFechayyyyMMdd()";
        }
        return FechaSql;
    }

    public List<Double> onTesoreria_Totales_CtoNoIdentificados(String FecValor) {

        StringBuilder stringBuilder = new StringBuilder();
        List<Double> Totales_noIdentificados = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, ");
            stringBuilder.append(" ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR, ");
            stringBuilder.append(" ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR ");
            stringBuilder.append("FROM (  SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("and INV_FEC_MOVTO >=  ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append("UNION ALL ");
            stringBuilder.append("SELECT 0 TOTAL_IMP, COALESCE(SUM(DOUBLE(TOTAL_IMP)),0) TOTAL_IMP_ERR ");
            stringBuilder.append("FROM ( SELECT DOUBLE(INV_IMPORTE) TOTAL_IMP, (select count(*) ");
            stringBuilder.append("	FROM SAF.CONTINTE ");
            stringBuilder.append(" WHERE CPR_CONTRATO_INTER = CAST(DECIMAL(INV_CTOINV,11,0)  AS BIGINT) ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN =7) CTOS ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND INV_CVE_OPERACION !='EFECTIVO' ");
            stringBuilder.append("AND INV_DESC_ERROR not like '%LIGADO%' ");
            stringBuilder.append("AND INV_FEC_MOVTO >= ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_NUM_CTO = '0' ) INV WHERE INV.CTOS = 0 ) A");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_noIdentificados.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_noIdentificados.add(resultSet.getDouble("PJE_ERR"));
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_noIdentificados;
    }

    public List<InvRechazoCtoInvBean> onTesoreria_CtoPorAsignarFiso() {

        StringBuilder stringBuilder = new StringBuilder();
        List<InvRechazoCtoInvBean> porAsignarFiso = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT INV_CTOINV, INV_ENTIDAD_FIN, COALESCE(INT_INTERMEDIARIO,RTRIM(CHAR(INV_ENTIDAD_FIN)))INTERMEDIARIO, ");
            stringBuilder.append("INV_NUM_CTO, CTO_NOM_CONTRATO NOM_CTO, INV_PLAZA, ");
            stringBuilder.append("INV_PROMOTOR, INV_CTA_CHEQUES,  INV_MANEJO,");
            stringBuilder.append("INV_ESTATUS, INV_FEC_ALTA, INV_FEC_ULTMOD,  	INV_SEC ");
            stringBuilder.append("FROM SAF.INV_RECHAZO_CTOINV ");
            stringBuilder.append("LEFT JOIN SAF.INTERMED ON INT_ENTIDAD_FIN = INV_ENTIDAD_FIN ");
            stringBuilder.append("LEFT JOIN SAF.CONTRATO ON CTO_NUM_CONTRATO = INV_NUM_CTO ");
            stringBuilder.append("WHERE INV_ESTATUS <> 'NO FIDUCIA' ");
            stringBuilder.append("AND INV_ESTATUS <> 'PROCESADA' ");
            stringBuilder.append("ORDER BY INV_CTOINV");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazoCtoInvBean invRechazosCtoInvBean = new InvRechazoCtoInvBean();

                    // Set_Values
                    invRechazosCtoInvBean.setCto_Inv(resultSet.getDouble("INV_CTOINV"));
                    invRechazosCtoInvBean.setNum_Entidad_Financ(resultSet.getInt("INV_ENTIDAD_FIN"));
                    invRechazosCtoInvBean.setEntidad_Financ(resultSet.getString("INTERMEDIARIO"));
                    invRechazosCtoInvBean.setNum_Cto(resultSet.getDouble("INV_NUM_CTO"));
                    invRechazosCtoInvBean.setNom_Cto(resultSet.getString("NOM_CTO"));
                    invRechazosCtoInvBean.setPlaza(resultSet.getString("INV_PLAZA"));
                    invRechazosCtoInvBean.setPromotor(resultSet.getString("INV_PROMOTOR"));
                    invRechazosCtoInvBean.setCta_Cheques(resultSet.getString("INV_CTA_CHEQUES"));
                    invRechazosCtoInvBean.setManejo(resultSet.getString("INV_MANEJO"));
                    invRechazosCtoInvBean.setStatus(resultSet.getString("INV_ESTATUS"));
                    invRechazosCtoInvBean.setFec_Alta(resultSet.getDate("INV_FEC_ALTA"));
                    invRechazosCtoInvBean.setFec_Ult_Mov(resultSet.getDate("INV_FEC_ULTMOD"));
                    invRechazosCtoInvBean.setInv_Sec(resultSet.getInt("INV_SEC"));

                    // Add_List
                    porAsignarFiso.add(invRechazosCtoInvBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return porAsignarFiso;
    }

    public List<InvRechazoCtoInvBean> onTesoreria_NoPerteneceFiducia() {

        StringBuilder stringBuilder = new StringBuilder();
        List<InvRechazoCtoInvBean> noPertenece = new ArrayList<>();

        try {
            // SQL7
            stringBuilder.append("SELECT INV_CTOINV,  INV_ENTIDAD_FIN, 	COALESCE(INT_INTERMEDIARIO,RTRIM(CHAR(INV_ENTIDAD_FIN)))INTERMEDIARIO,");
            stringBuilder.append("INV_NUM_CTO, 		INV_PLAZA, 			INV_PROMOTOR,");
            stringBuilder.append("INV_CTA_CHEQUES, 	INV_MANEJO, 		INV_ESTATUS,");
            stringBuilder.append("INV_FEC_ALTA, 		INV_FEC_ULTMOD,		INV_SEC ");
            stringBuilder.append("FROM SAF.INV_RECHAZO_CTOINV ");
            stringBuilder.append("LEFT JOIN SAF.INTERMED ON INT_ENTIDAD_FIN = INV_ENTIDAD_FIN ");
            stringBuilder.append("WHERE INV_ESTATUS ='NO FIDUCIA' ORDER BY INV_CTOINV");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazoCtoInvBean invRechazosCtoInvBean = new InvRechazoCtoInvBean();

                    // Set_Values
                    invRechazosCtoInvBean.setCto_Inv(resultSet.getDouble("INV_CTOINV"));
                    invRechazosCtoInvBean.setNum_Entidad_Financ(resultSet.getInt("INV_ENTIDAD_FIN"));
                    invRechazosCtoInvBean.setEntidad_Financ(resultSet.getString("INTERMEDIARIO"));
                    invRechazosCtoInvBean.setNum_Cto(resultSet.getDouble("INV_NUM_CTO"));
                    invRechazosCtoInvBean.setPlaza(resultSet.getString("INV_PLAZA"));
                    invRechazosCtoInvBean.setPromotor(resultSet.getString("INV_PROMOTOR"));
                    invRechazosCtoInvBean.setCta_Cheques(resultSet.getString("INV_CTA_CHEQUES"));
                    invRechazosCtoInvBean.setManejo(resultSet.getString("INV_MANEJO"));
                    invRechazosCtoInvBean.setStatus(resultSet.getString("INV_ESTATUS"));
                    invRechazosCtoInvBean.setFec_Alta(resultSet.getDate("INV_FEC_ALTA"));
                    invRechazosCtoInvBean.setFec_Ult_Mov(resultSet.getDate("INV_FEC_ULTMOD"));
                    invRechazosCtoInvBean.setInv_Sec(resultSet.getInt("INV_SEC"));

                    // Add_List
                    noPertenece.add(invRechazosCtoInvBean);
                }
            }
            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return noPertenece;
    }

    public List<InvRechazosBean> onTesoreria_ClavesNoIdentificadas(String FecValor) {

        StringBuilder stringBuilder = new StringBuilder();
        List<InvRechazosBean> ClavesNoIdentificadas = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT DISTINCT INV_CVE_OPERACION CLAVE,");
            stringBuilder.append(" COALESCE(INT_INTERMEDIARIO,RTRIM(CHAR(INV_ENTIDAD_FIN))) INTERMEDIARIO, ");
            stringBuilder.append(" (SELECT MAX(CVE_FORMA_EMP_CVE) FROM SAF.CLAVES WHERE CVE_NUM_CLAVE = 131 AND CVE_DESC_CLAVE = INV_CVE_OPERACION ) SERVICIO,");
            stringBuilder.append(" COUNT(*) NUM_REGS ");
            stringBuilder.append("FROM (SELECT DECIMAL(INV_CTOINV,11,0) INV_CTOINV,");
            stringBuilder.append("	SMALLINT(INV_ENTIDAD_FIN) INV_ENTIDAD_FIN,");
            stringBuilder.append(" SMALLINT(INV_NUM_CUSTODIO) INV_NUM_CUSTODIO, ");
            stringBuilder.append(" INV_CVE_OPERACION, INV_DESC_ERROR,");
            stringBuilder.append(" COALESCE(CVE_NUM_SEC_CLAVE,-99) CVE ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("LEFT OUTER JOIN SAF.CLAVES c131 on CVE_NUM_CLAVE = 131 AND CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA') ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO'  AND INV_FEC_MOVTO >= ? ");
            stringBuilder.append("UNION ");
            stringBuilder.append("SELECT DECIMAL(INV_CTOINV,11,0) INV_CTOINV,");
            stringBuilder.append(" SMALLINT(INV_ENTIDAD_FIN) INV_ENTIDAD_FIN, ");
            stringBuilder.append(" SMALLINT(INV_NUM_CUSTODIO) INV_NUM_CUSTODIO, ");
            stringBuilder.append(" INV_CVE_OPERACION, INV_DESC_ERROR, ");
            stringBuilder.append(" COALESCE(CVE_NUM_SEC_CLAVE,-99) CVE  ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS LEFT OUTER JOIN SAF.CLAVES c131 on CVE_NUM_CLAVE = 131 ");
            stringBuilder.append("AND CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA') ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_DESC_ERROR LIKE 'PRINCIPAL CON ERROR%' ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append("AND INV_FEC_MOVTO >=  ? ) INV ");
            stringBuilder.append("LEFT JOIN SAF.INTERMED ON INT_ENTIDAD_FIN = INV_ENTIDAD_FIN ");
            stringBuilder.append("WHERE CVE=-99 ");
            stringBuilder.append("GROUP BY INV_CVE_OPERACION, INV_ENTIDAD_FIN, INT_INTERMEDIARIO, CVE ORDER BY CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazosBean invRechazosBean = new InvRechazosBean();

                    // Set_Values
                    invRechazosBean.setClave(resultSet.getString("CLAVE"));
                    invRechazosBean.setEntidad_Financ(resultSet.getString("INTERMEDIARIO"));
                    invRechazosBean.setServicio(resultSet.getString("SERVICIO"));
                    invRechazosBean.setNum_Regis(resultSet.getInt("NUM_REGS"));

                    // Add_List
                    ClavesNoIdentificadas.add(invRechazosBean);
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return ClavesNoIdentificadas;
    }

    public List<Double> onTesoreria_Claves_Totales_CtoNoIdentif(String FecValor) {

        List<Double> Totales_claves = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP,ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR FROM ( ");
            stringBuilder.append("SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR  ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND INV_FEC_MOVTO >=  ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append("	UNION ");
            stringBuilder.append("SELECT 0 TOTAL_IMP,SUM(TOTAL_IMP_ERR0) TOTAL_IMP_ERR FROM ( ");
            stringBuilder.append("SELECT DOUBLE(INV_IMPORTE) TOTAL_IMP_ERR0,COALESCE(CVE_NUM_SEC_CLAVE,-99) CVE,INV_CVE_OPERACION ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS LEFT OUTER JOIN SAF.CLAVES c131 on CVE_NUM_CLAVE = 131 AND CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA') ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' and INV_FEC_MOVTO >= ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append(" UNION ");
            stringBuilder.append("SELECT DOUBLE(INV_IMPORTE) TOTAL_IMP_ERR0,COALESCE(CVE_NUM_SEC_CLAVE,-99) CVE,INV_CVE_OPERACION ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS LEFT OUTER JOIN SAF.CLAVES c131 on CVE_NUM_CLAVE = 131 AND CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA') ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_DESC_ERROR LIKE 'PRINCIPAL CON ERROR%' ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append("AND INV_FEC_MOVTO >=  ? ) A where CVE = -99 ) XX");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);
            preparedStatement.setDate(3, FechaSql);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_claves.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_claves.add(resultSet.getDouble("PJE_ERR"));
                }
            }
            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_claves;
    }

    public List<InvRechazosBean> onTesoreria_OtrosRechazos(String FecValor, String sFiso, String sCtoInv, String sClave, boolean bEstructuraContable) {

        int index = 1;
        StringBuilder stringBuilder = new StringBuilder();
        List<InvRechazosBean> otrosRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT CTO_INV, CLAVE, INTERMEDIARIO, CUSTODIO, NUM_REGS, FISO ");
            stringBuilder.append("FROM (SELECT CAST(DECIMAL(INV_CTOINV,12,0) AS BIGINT) CTO_INV,  INV_CVE_OPERACION CLAVE, ");
            stringBuilder.append(" COALESCE(INT_INTERMEDIARIO, RTRIM(CHAR(INV_ENTIDAD_FIN)))INTERMEDIARIO, COALESCE(B.CVE_DESC_CLAVE, ");
            stringBuilder.append(" RTRIM(CHAR(INV_NUM_CUSTODIO))) CUSTODIO,   COUNT(*) NUM_REGS, ");
            stringBuilder.append("(SELECT DISTINCT CPR_NUM_CONTRATO ");
            stringBuilder.append("FROM SAF.CONTINTE ");
            stringBuilder.append("WHERE CPR_CONTRATO_INTER = CAST(DECIMAL(INV_CTOINV,11,0)  AS BIGINT) ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN =7) FISO ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS r ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c131 ON c131.cve_num_clave = 131 ");
            stringBuilder.append("AND  c131.CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("JOIN SAF.intermed ON smallint(INV_ENTIDAD_FIN) =  INT_ENTIDAD_FIN ");
            stringBuilder.append("JOIN SAF.CLAVES B ON B.CVE_NUM_SEC_CLAVE = INT(INV_NUM_CUSTODIO) ");
            stringBuilder.append("AND B.CVE_NUM_CLAVE = 386 ");
            stringBuilder.append("WHERE  (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_ESTATUS IN('ERROR', 'NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND NOT (  (SUBSTR(COALESCE(INV_DESC_ERROR,''),1,3)= '7.-') ");
            stringBuilder.append("AND (INV_DESC_ERROR LIKE '%NO EXISTE%')) ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");

            //Claves_No_Identificadas
            if (bEstructuraContable == true) {
                stringBuilder.append(" AND INV_DESC_ERROR LIKE '%NO.OPERACION%'");
            }

            stringBuilder.append("AND INV_FEC_MOVTO >= ? ");
            stringBuilder.append("GROUP BY INV_CTOINV, INV_CVE_OPERACION, INV_ENTIDAD_FIN, INT_INTERMEDIARIO, INV_NUM_CUSTODIO,  B.CVE_DESC_CLAVE) INV ");
            stringBuilder.append("WHERE NOT FISO IS NULL ");

            if (!sFiso.equals("")) {
                stringBuilder.append(" AND FISO = ? ");
            }

            if (!sCtoInv.equals("")) {
                stringBuilder.append(" AND CTO_INV = ? ");
            }

            if (!sClave.equals("")) {
                stringBuilder.append(" AND CLAVE = ? ");
            }

            stringBuilder.append(" ORDER BY NUM_REGS DESC, CTO_INV, CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);

            if (!sFiso.equals("")) {
                index = index + 1;
                preparedStatement.setString(index, sFiso.trim());
            }

            if (!sCtoInv.equals("")) {
                index = index + 1;
                preparedStatement.setString((index), sCtoInv.trim());
            }

            if (!sClave.equals("")) {
                index = index + 1;
                preparedStatement.setString((index), sClave.trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    InvRechazosBean invRechazosBean = new InvRechazosBean();

                    // Set_Values
                    invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                    invRechazosBean.setClave(resultSet.getString("CLAVE"));
                    invRechazosBean.setEntidad_Financ(resultSet.getString("INTERMEDIARIO"));
                    invRechazosBean.setCustodio(resultSet.getString("CUSTODIO"));
                    invRechazosBean.setNum_Regis(resultSet.getInt("NUM_REGS"));
                    invRechazosBean.setNum_Cto(resultSet.getString("FISO"));

                    // Add_List
                    otrosRechazos.add(invRechazosBean);
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return otrosRechazos;
    }

    public List<Double> onTesoreria_Totales_OtrosRechazos(String FecValor, String sFiso, String sCtoInv, String sClave, boolean bEstructuraContable) {

        int index = 2;
        StringBuilder stringBuilder = new StringBuilder();
        List<Double> Totales_otrosRechazos = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            stringBuilder.append("SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, ");
            stringBuilder.append(" ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR, ");
            stringBuilder.append(" ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR ");
            stringBuilder.append("FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND INV_FEC_MOVTO >=  ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");
            stringBuilder.append("UNION ALL ");
            stringBuilder.append("SELECT 0 TOTAL_IMP, COALESCE(SUM(IMP),0) TOTAL_IMP_ERR FROM (  SELECT DOUBLE(INV_IMPORTE) IMP, (select DISTINCT CPR_NUM_CONTRATO ");
            stringBuilder.append("FROM SAF.CONTINTE ");
            stringBuilder.append("WHERE CPR_CONTRATO_INTER = CAST(DECIMAL(INV_CTOINV,11,0)  AS BIGINT) ");
            stringBuilder.append("AND CPR_ENTIDAD_FIN =7) FISO ");
            stringBuilder.append("FROM SAF.INV_RECHAZOS ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c131 ON CVE_NUM_CLAVE = 131 ");
            stringBuilder.append("AND CVE_DESC_CLAVE = INV_CVE_OPERACION ");
            stringBuilder.append("WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') ");
            stringBuilder.append("AND NOT ((SUBSTR(COALESCE(INV_DESC_ERROR,''),1,3)= '7.-') ");
            stringBuilder.append("AND (INV_DESC_ERROR LIKE '%NO EXISTE%')) ");
            stringBuilder.append("AND INV_FEC_MOVTO >= ? ");
            stringBuilder.append("AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') ");
            stringBuilder.append("AND INV_CVE_OPERACION != 'EFECTIVO' ");

            if (!sFiso.equals("")) {
                stringBuilder.append(" AND (DECIMAL(INV_CTOINV,11,0),SMALLINT(INV_ENTIDAD_FIN)) IN(SELECT CPR_CONTRATO_INTER,CPR_ENTIDAD_FIN FROM SAF.CONTINTE WHERE CPR_NUM_CONTRATO = ? )");
            }

            if (!sCtoInv.equals("")) {
                stringBuilder.append(" AND DECIMAL(INV_CTOINV,11,0) = ? ");
            }

            if (!sClave.equals("")) {
                stringBuilder.append(" AND INV_CVE_OPERACION = ? ");
            }

            if (bEstructuraContable == true) {
                stringBuilder.append(" AND INV_DESC_ERROR LIKE '%NO.OPERACION%' ");
            }

            stringBuilder.append(" ) A WHERE NOT FISO  IS NULL) INV");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!sFiso.equals("")) {
                index = index + 1;
                preparedStatement.setDouble((index), Double.parseDouble(sFiso.trim()));
            }

            if (!sCtoInv.equals("")) {
                index = index + 1;
                preparedStatement.setDouble((index), Double.parseDouble(sCtoInv.trim()));
            }

            if (!sClave.equals("")) {
                index = index + 1;
                preparedStatement.setString((index), sClave.trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_otrosRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_otrosRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_otrosRechazos;
    }

    public List<InvRechazosBean> onTesoreria_Detalle_Rechazados_NoIdent(String FecValor, InvRechazosBean RechazosBean) {

        String QuerySql = "", sQueryOrden = "", sFiltro = "", sQueryDet = "";
        List<InvRechazosBean> detalleRechazos = new ArrayList<>();

        // -----------------------------------------------------------------------------
        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            QuerySql = "SELECT INV_ROW_ID ID,				INV_CVE_OPERACION CLAVE,	"
                    + "	INV_TIPO_VALOR TIPO_VALOR,			INT(INV_NUM_CTO) NUM_CTO, "
                    + " SMALLINT(INV_NUM_SUBCTO) NUM_SUBCTO,INV_PIZARRA PIZARRA,"
                    + " INV_SERIE SERIE,					SMALLINT(INV_CUPON) CUPON, "
                    + " DECIMAL(INV_TITULOS,16,0) TITULOS,	DECIMAL(INV_PRECIO,21,9) PRECIO,"
                    + " DECIMAL(INV_IMPORTE,16,2) IMPORTE,  DECIMAL(INV_TASA_REND,16,8) TASA,"
                    + " SMALLINT(INV_PLAZO) PLAZO,			DECIMAL(INV_PREMIO,16,2) PREMIO,"
                    + " INV_FEC_MOVTO FECHA_REG,  			INV_FEC_ATENCION FECHA_APL,"
                    + " DECIMAL(INV_COMISION,16,2) COMISION,DECIMAL(INV_IVA,16,2) IVA,"
                    + " DECIMAL(INV_ISR,16,2) ISR,  		DECIMAL(INV_TIPO_CAMBIO,18,8) TIPO_CAMBIO,"
                    + " COALESCE(SUBSTR(C45.CVE_FORMA_EMP_CVE,1,4),INV_MERCADO) MERCADO,  INV_ORDEN ORDEN, "
                    + " INV_NUM_TRANS NUM_TRANS,			DECIMAL(INV_CTOINV,11,0) CTO_INV, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMINF_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_TITS WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_TIT, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMSUP_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_EFEC WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_EFE, "
                    + " 0 NUM_TRANS,						SMALLINT(INV_NUM_MONEDA) MDA,"
                    + " INV_ESTATUS ESTATUS,				INV_DESC_ERROR ERROR,"
                    + " INV_NOM_FILE NOM_FILE,				INV_NUM_BANCO NUM_BANCO, "
                    + " INV_FEC_VENCE FEC_VENCE,			INV_ORIGEN ORIGEN "
                    + " FROM SAF.INV_RECHAZOS "
                    + " LEFT OUTER JOIN SAF.OPERTRAN3 ON TRA_FECHA_MOV =INV_FEC_MOVTO AND TRA_AGRUPA2 = INV_ROW_ID AND TRA_NOM_FILE =INV_NOM_FILE"
                    + " LEFT OUTER JOIN SAF.CLAVES c45 ON C45.CVE_NUM_CLAVE=45 AND C45.CVE_CVE_ST_CLAVE = 'ACTIVO' AND C45.CVE_NUM_SEC_CLAVE = TRA_CVE_TIPO_MERCA"
                    + " LEFT OUTER JOIN SAF.CLAVES c131 ON c131.CVE_NUM_CLAVE = 131 AND c131.CVE_DESC_CLAVE = INV_CVE_OPERACION"
                    + " LEFT OUTER JOIN SAF.CLAVES c662 ON c662.CVE_NUM_CLAVE = 662 AND c662.CVE_DESC_CLAVE = SUBSTR(c131.CVE_FORMA_EMP_CVE,1,2)"
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') "
                    + " AND INV_DESC_ERROR NOT LIKE '%LIGADO%' "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!RechazosBean.getCto_Inv().equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            sQueryOrden = " ORDER BY INV_FEC_MOVTO,INV_ROW_ID ";

            sQueryDet = QuerySql + sFiltro + sQueryOrden;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sQueryDet);

            preparedStatement.setDate(1, FechaSql);

            if (!RechazosBean.getCto_Inv().equals("")) {
                preparedStatement.setDouble(2, Double.parseDouble(RechazosBean.getCto_Inv().trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                InvRechazosBean invRechazosBean = new InvRechazosBean();

                // Set_Values
                invRechazosBean.setOrigen(resultSet.getString("ORIGEN"));
                invRechazosBean.setRow_Id(resultSet.getInt("ID"));
                invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                invRechazosBean.setNum_Moneda(resultSet.getString("MDA"));
                invRechazosBean.setClave(resultSet.getString("CLAVE"));
                invRechazosBean.setTipo_Valor(resultSet.getString("TIPO_VALOR"));
                invRechazosBean.setNum_Cto(resultSet.getString("NUM_CTO"));
                invRechazosBean.setNum_Subcto(resultSet.getString("NUM_SUBCTO"));
                invRechazosBean.setStatus(resultSet.getString("ESTATUS"));
                invRechazosBean.setDesc_Error(resultSet.getString("ERROR"));
                invRechazosBean.setPizarra(resultSet.getString("PIZARRA"));
                invRechazosBean.setSerie(resultSet.getString("SERIE"));
                invRechazosBean.setCupon(resultSet.getString("CUPON"));

                if (resultSet.getString("TITULOS") != null) {
                    invRechazosBean.setTitulos(formatFormatTit(resultSet.getDouble("TITULOS")));
                } else {
                    invRechazosBean.setTitulos(resultSet.getString("TITULOS"));
                }

                if (resultSet.getString("PRECIO") != null) {
                    invRechazosBean.setPrecio(formatDecimalPre(resultSet.getDouble("PRECIO")));
                } else {
                    invRechazosBean.setPrecio(resultSet.getString("PRECIO"));
                }

                if (resultSet.getString("IMPORTE") != null) {
                    invRechazosBean.setImporte(formatDecimalImp(resultSet.getDouble("IMPORTE")));
                } else {
                    invRechazosBean.setImporte(resultSet.getString("IMPORTE"));
                }

                if (resultSet.getString("TASA") != null) {
                    invRechazosBean.setTasa_Rend(formatDecimalTasa(resultSet.getDouble("TASA")));
                } else {
                    invRechazosBean.setTasa_Rend(resultSet.getString("TASA"));
                }

                invRechazosBean.setPlazo(resultSet.getInt("PLAZO"));

                if (resultSet.getString("PREMIO") != null) {
                    invRechazosBean.setPremio(formatDecimalImp(resultSet.getDouble("PREMIO")));
                } else {
                    invRechazosBean.setPremio(resultSet.getString("PREMIO"));
                }

                invRechazosBean.setFec_Apl(resultSet.getString("FECHA_APL"));

                if (resultSet.getString("COMISION") != null) {
                    invRechazosBean.setComision(formatDecimalImp(resultSet.getDouble("COMISION")));
                } else {
                    invRechazosBean.setComision(resultSet.getString("COMISION"));
                }

                if (resultSet.getString("IVA") != null) {
                    invRechazosBean.setIVA(formatDecimalImp(resultSet.getDouble("IVA")));
                } else {
                    invRechazosBean.setIVA(resultSet.getString("IVA"));
                }

                if (resultSet.getString("ISR") != null) {
                    invRechazosBean.setISR(formatDecimalImp(resultSet.getDouble("ISR")));
                } else {
                    invRechazosBean.setISR(resultSet.getString("ISR"));
                }

                if (resultSet.getString("TIPO_CAMBIO") != null) {
                    invRechazosBean.setTipo_Cambio(formatDecimalTP(resultSet.getDouble("TIPO_CAMBIO")));
                } else {
                    invRechazosBean.setTipo_Cambio(resultSet.getString("TIPO_CAMBIO"));
                }

                invRechazosBean.setMercado(resultSet.getString("MERCADO"));
                invRechazosBean.setOrden(resultSet.getString("ORDEN"));
                invRechazosBean.setNum_Trans(resultSet.getString("NUM_TRANS"));
                invRechazosBean.setNum_Banco(resultSet.getString("NUM_BANCO"));
                invRechazosBean.setFec_Vence(resultSet.getString("FEC_VENCE"));
                invRechazosBean.setMueve_Tits(resultSet.getString("MUEVE_TIT"));
                invRechazosBean.setMueve_Efectivo(resultSet.getString("MUEVE_EFE"));
                invRechazosBean.setNom_File(resultSet.getString("NOM_FILE"));

                // Add_List
                detalleRechazos.add(invRechazosBean);
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return detalleRechazos;
    }

    public List<InvRechazosBean> onTesoreria_Detalle_Rechazados_Asig(String FecValor, String sCondicion) {

        // Objects
        String QuerySql = "", sQueryOrden = "", sFiltro = "", sQueryDet = "";
        List<InvRechazosBean> detalleRechazos = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            QuerySql = "SELECT INV_ROW_ID ID,				INV_CVE_OPERACION CLAVE,	"
                    + "	INV_TIPO_VALOR TIPO_VALOR,			INT(INV_NUM_CTO) NUM_CTO, "
                    + " SMALLINT(INV_NUM_SUBCTO) NUM_SUBCTO,INV_PIZARRA PIZARRA,"
                    + " INV_SERIE SERIE,					SMALLINT(INV_CUPON) CUPON, "
                    + " DECIMAL(INV_TITULOS,16,0) TITULOS,	DECIMAL(INV_PRECIO,21,9) PRECIO,"
                    + " DECIMAL(INV_IMPORTE,16,2) IMPORTE,  DECIMAL(INV_TASA_REND,16,8) TASA,"
                    + " SMALLINT(INV_PLAZO) PLAZO,			DECIMAL(INV_PREMIO,16,2) PREMIO,"
                    + " INV_FEC_MOVTO FECHA_REG,  			INV_FEC_ATENCION FECHA_APL,"
                    + " DECIMAL(INV_COMISION,16,2) COMISION,DECIMAL(INV_IVA,16,2) IVA,"
                    + " DECIMAL(INV_ISR,16,2) ISR,  		DECIMAL(INV_TIPO_CAMBIO,18,8) TIPO_CAMBIO,"
                    + " COALESCE(SUBSTR(C45.CVE_FORMA_EMP_CVE,1,4),INV_MERCADO) MERCADO,  INV_ORDEN ORDEN, "
                    + " INV_NUM_TRANS NUM_TRANS,			DECIMAL(INV_CTOINV,11,0) CTO_INV, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMINF_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_TITS WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_TIT, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMSUP_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_EFEC WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_EFE, "
                    + " 0 NUM_TRANS,						SMALLINT(INV_NUM_MONEDA) MDA,"
                    + " INV_ESTATUS ESTATUS,				INV_DESC_ERROR ERROR,"
                    + " INV_NOM_FILE NOM_FILE,				INV_NUM_BANCO NUM_BANCO, "
                    + " INV_FEC_VENCE FEC_VENCE,			INV_ORIGEN ORIGEN "
                    + " FROM SAF.INV_RECHAZOS "
                    + " LEFT OUTER JOIN SAF.OPERTRAN3 ON TRA_FECHA_MOV =INV_FEC_MOVTO AND TRA_AGRUPA2 = INV_ROW_ID AND TRA_NOM_FILE = INV_NOM_FILE"
                    + " LEFT OUTER JOIN SAF.CLAVES c45 ON C45.CVE_NUM_CLAVE=45 AND C45.CVE_CVE_ST_CLAVE = 'ACTIVO' AND C45.CVE_NUM_SEC_CLAVE = TRA_CVE_TIPO_MERCA"
                    + " LEFT OUTER JOIN SAF.CLAVES c131 ON c131.CVE_NUM_CLAVE = 131 AND c131.CVE_DESC_CLAVE = INV_CVE_OPERACION"
                    + " LEFT OUTER JOIN SAF.CLAVES c662 ON c662.CVE_NUM_CLAVE = 662 AND c662.CVE_DESC_CLAVE = SUBSTR(c131.CVE_FORMA_EMP_CVE,1,2)"
                    + " WHERE INV_ESTATUS IN('POR ASIGNAR') "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!sCondicion.equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            sQueryOrden = " ORDER BY INV_FEC_MOVTO,INV_ROW_ID ";

            sQueryDet = QuerySql + sFiltro + sQueryOrden;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sQueryDet);

            preparedStatement.setDate(1, FechaSql);

            if (!sCondicion.equals("")) {
                preparedStatement.setDouble(2, Double.parseDouble(sCondicion.trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                InvRechazosBean invRechazosBean = new InvRechazosBean();

                // Set_Values
                invRechazosBean.setOrigen(resultSet.getString("ORIGEN"));
                invRechazosBean.setRow_Id(resultSet.getInt("ID"));
                invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                invRechazosBean.setNum_Moneda(resultSet.getString("MDA"));
                invRechazosBean.setClave(resultSet.getString("CLAVE"));
                invRechazosBean.setTipo_Valor(resultSet.getString("TIPO_VALOR"));
                invRechazosBean.setNum_Cto(resultSet.getString("NUM_CTO"));
                invRechazosBean.setNum_Subcto(resultSet.getString("NUM_SUBCTO"));
                invRechazosBean.setStatus(resultSet.getString("ESTATUS"));
                invRechazosBean.setDesc_Error(resultSet.getString("ERROR"));
                invRechazosBean.setPizarra(resultSet.getString("PIZARRA"));
                invRechazosBean.setSerie(resultSet.getString("SERIE"));
                invRechazosBean.setCupon(resultSet.getString("CUPON"));

                if (resultSet.getString("TITULOS") != null) {
                    invRechazosBean.setTitulos(formatFormatTit(resultSet.getDouble("TITULOS")));
                } else {
                    invRechazosBean.setTitulos(resultSet.getString("TITULOS"));
                }

                if (resultSet.getString("PRECIO") != null) {
                    invRechazosBean.setPrecio(formatDecimalPre(resultSet.getDouble("PRECIO")));
                } else {
                    invRechazosBean.setPrecio(resultSet.getString("PRECIO"));
                }

                if (resultSet.getString("IMPORTE") != null) {
                    invRechazosBean.setImporte(formatDecimalImp(resultSet.getDouble("IMPORTE")));
                } else {
                    invRechazosBean.setImporte(resultSet.getString("IMPORTE"));
                }

                if (resultSet.getString("TASA") != null) {
                    invRechazosBean.setTasa_Rend(formatDecimalTasa(resultSet.getDouble("TASA")));
                } else {
                    invRechazosBean.setTasa_Rend(resultSet.getString("TASA"));
                }

                invRechazosBean.setPlazo(resultSet.getInt("PLAZO"));

                if (resultSet.getString("PREMIO") != null) {
                    invRechazosBean.setPremio(formatDecimalImp(resultSet.getDouble("PREMIO")));
                } else {
                    invRechazosBean.setPremio(resultSet.getString("PREMIO"));
                }

                invRechazosBean.setFec_Apl(resultSet.getString("FECHA_APL"));

                if (resultSet.getString("COMISION") != null) {
                    invRechazosBean.setComision(formatDecimalImp(resultSet.getDouble("COMISION")));
                } else {
                    invRechazosBean.setComision(resultSet.getString("COMISION"));
                }

                if (resultSet.getString("IVA") != null) {
                    invRechazosBean.setIVA(formatDecimalImp(resultSet.getDouble("IVA")));
                } else {
                    invRechazosBean.setIVA(resultSet.getString("IVA"));
                }

                if (resultSet.getString("ISR") != null) {
                    invRechazosBean.setISR(formatDecimalImp(resultSet.getDouble("ISR")));
                } else {
                    invRechazosBean.setISR(resultSet.getString("ISR"));
                }

                if (resultSet.getString("TIPO_CAMBIO") != null) {
                    invRechazosBean.setTipo_Cambio(formatDecimalTP(resultSet.getDouble("TIPO_CAMBIO")));
                } else {
                    invRechazosBean.setTipo_Cambio(resultSet.getString("TIPO_CAMBIO"));
                }

                invRechazosBean.setMercado(resultSet.getString("MERCADO"));
                invRechazosBean.setOrden(resultSet.getString("ORDEN"));
                invRechazosBean.setNum_Trans(resultSet.getString("NUM_TRANS"));
                invRechazosBean.setNum_Banco(resultSet.getString("NUM_BANCO"));
                invRechazosBean.setFec_Vence(resultSet.getString("FEC_VENCE"));
                invRechazosBean.setMueve_Tits(resultSet.getString("MUEVE_TIT"));
                invRechazosBean.setMueve_Efectivo(resultSet.getString("MUEVE_EFE"));
                invRechazosBean.setNom_File(resultSet.getString("NOM_FILE"));

                // Add_List
                detalleRechazos.add(invRechazosBean);
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return detalleRechazos;
    }

    public List<InvRechazosBean> onTesoreria_Detalle_Rechazados_NoPert(String FecValor, String sCondicion) {

        String QuerySql = "", sQueryOrden = "", sFiltro = "", sQueryDet = "";
        List<InvRechazosBean> detalleRechazos = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            QuerySql = "SELECT INV_ROW_ID ID,				INV_CVE_OPERACION CLAVE, "
                    + "	INV_TIPO_VALOR TIPO_VALOR,			INT(INV_NUM_CTO) NUM_CTO, "
                    + "	SMALLINT(INV_NUM_SUBCTO) NUM_SUBCTO,INV_PIZARRA PIZARRA, "
                    + "	INV_SERIE SERIE,					SMALLINT(INV_CUPON) CUPON,  "
                    + "	DECIMAL(INV_TITULOS,16,0) TITULOS,	DECIMAL(INV_PRECIO,21,9) PRECIO, "
                    + "	DECIMAL(INV_IMPORTE,16,2) IMPORTE,  DECIMAL(INV_TASA_REND,16,8) TASA, "
                    + "	SMALLINT(INV_PLAZO) PLAZO,			DECIMAL(INV_PREMIO,16,2) PREMIO, "
                    + "	INV_FEC_MOVTO FECHA_REG,  			INV_FEC_ATENCION FECHA_APL, "
                    + "	DECIMAL(INV_COMISION,16,2) COMISION,DECIMAL(INV_IVA,16,2) IVA, "
                    + "	DECIMAL(INV_ISR,16,2) ISR,  		DECIMAL(INV_TIPO_CAMBIO,18,8) TIPO_CAMBIO, "
                    + "	COALESCE(SUBSTR(C45.CVE_FORMA_EMP_CVE,1,4),INV_MERCADO) MERCADO,  INV_ORDEN ORDEN,  "
                    + "	INV_NUM_TRANS NUM_TRANS,			DECIMAL(INV_CTOINV,11,0) CTO_INV,  "
                    + "	CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMINF_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_TITS WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_TIT, "
                    + "	CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMSUP_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_EFEC WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_EFE, "
                    + "	0 NUM_TRANS,						SMALLINT(INV_NUM_MONEDA) MDA, "
                    + "	INV_ESTATUS ESTATUS,				INV_DESC_ERROR ERROR, "
                    + "	INV_NOM_FILE NOM_FILE,				INV_NUM_BANCO NUM_BANCO, "
                    + "	INV_FEC_VENCE FEC_VENCE,			INV_ORIGEN ORIGEN "
                    + " FROM SAF.INV_RECHAZOS "
                    + " LEFT OUTER JOIN SAF.OPERTRAN3 On TRA_FECHA_MOV =INV_FEC_MOVTO AND TRA_AGRUPA2 = INV_ROW_ID AND TRA_NOM_FILE =INV_NOM_FILE "
                    + " LEFT OUTER JOIN SAF.CLAVES c45 On C45.CVE_NUM_CLAVE=45 AND C45.CVE_CVE_ST_CLAVE = 'ACTIVO' AND C45.CVE_NUM_SEC_CLAVE = TRA_CVE_TIPO_MERCA "
                    + " LEFT OUTER JOIN SAF.CLAVES c131 On c131.cve_num_clave = 131 AND  c131.CVE_DESC_CLAVE = INV_CVE_OPERACION "
                    + " LEFT OUTER JOIN SAF.CLAVES c662 On c662.cve_num_clave = 662 AND  c662.CVE_DESC_CLAVE = SUBSTR(c131.CVE_FORMA_EMP_CVE,1,2) "
                    + " WHERE INV_ESTATUS ='NO FIDUCIA'  "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!sCondicion.equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            sQueryOrden = " ORDER BY INV_FEC_MOVTO,INV_ROW_ID ";

            sQueryDet = QuerySql + sFiltro + sQueryOrden;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sQueryDet);

            preparedStatement.setDate(1, FechaSql);

            if (!sCondicion.equals("")) {
                preparedStatement.setDouble(2, Double.parseDouble(sCondicion.trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                InvRechazosBean invRechazosBean = new InvRechazosBean();

                // Set_Values
                invRechazosBean.setOrigen(resultSet.getString("ORIGEN"));
                invRechazosBean.setRow_Id(resultSet.getInt("ID"));
                invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                invRechazosBean.setNum_Moneda(resultSet.getString("MDA"));
                invRechazosBean.setClave(resultSet.getString("CLAVE"));
                invRechazosBean.setTipo_Valor(resultSet.getString("TIPO_VALOR"));
                invRechazosBean.setNum_Cto(resultSet.getString("NUM_CTO"));
                invRechazosBean.setNum_Subcto(resultSet.getString("NUM_SUBCTO"));
                invRechazosBean.setStatus(resultSet.getString("ESTATUS"));
                invRechazosBean.setDesc_Error(resultSet.getString("ERROR"));
                invRechazosBean.setPizarra(resultSet.getString("PIZARRA"));
                invRechazosBean.setSerie(resultSet.getString("SERIE"));
                invRechazosBean.setCupon(resultSet.getString("CUPON"));

                if (resultSet.getString("TITULOS") != null) {
                    invRechazosBean.setTitulos(formatFormatTit(resultSet.getDouble("TITULOS")));
                } else {
                    invRechazosBean.setTitulos(resultSet.getString("TITULOS"));
                }

                if (resultSet.getString("PRECIO") != null) {
                    invRechazosBean.setPrecio(formatDecimalPre(resultSet.getDouble("PRECIO")));
                } else {
                    invRechazosBean.setPrecio(resultSet.getString("PRECIO"));
                }

                if (resultSet.getString("IMPORTE") != null) {
                    invRechazosBean.setImporte(formatDecimalImp(resultSet.getDouble("IMPORTE")));
                } else {
                    invRechazosBean.setImporte(resultSet.getString("IMPORTE"));
                }

                if (resultSet.getString("TASA") != null) {
                    invRechazosBean.setTasa_Rend(formatDecimalTasa(resultSet.getDouble("TASA")));
                } else {
                    invRechazosBean.setTasa_Rend(resultSet.getString("TASA"));
                }

                invRechazosBean.setPlazo(resultSet.getInt("PLAZO"));

                if (resultSet.getString("PREMIO") != null) {
                    invRechazosBean.setPremio(formatDecimalImp(resultSet.getDouble("PREMIO")));
                } else {
                    invRechazosBean.setPremio(resultSet.getString("PREMIO"));
                }

                invRechazosBean.setFec_Apl(resultSet.getString("FECHA_APL"));

                if (resultSet.getString("COMISION") != null) {
                    invRechazosBean.setComision(formatDecimalImp(resultSet.getDouble("COMISION")));
                } else {
                    invRechazosBean.setComision(resultSet.getString("COMISION"));
                }

                if (resultSet.getString("IVA") != null) {
                    invRechazosBean.setIVA(formatDecimalImp(resultSet.getDouble("IVA")));
                } else {
                    invRechazosBean.setIVA(resultSet.getString("IVA"));
                }

                if (resultSet.getString("ISR") != null) {
                    invRechazosBean.setISR(formatDecimalImp(resultSet.getDouble("ISR")));
                } else {
                    invRechazosBean.setISR(resultSet.getString("ISR"));
                }

                if (resultSet.getString("TIPO_CAMBIO") != null) {
                    invRechazosBean.setTipo_Cambio(formatDecimalTP(resultSet.getDouble("TIPO_CAMBIO")));
                } else {
                    invRechazosBean.setTipo_Cambio(resultSet.getString("TIPO_CAMBIO"));
                }

                invRechazosBean.setMercado(resultSet.getString("MERCADO"));
                invRechazosBean.setOrden(resultSet.getString("ORDEN"));
                invRechazosBean.setNum_Trans(resultSet.getString("NUM_TRANS"));
                invRechazosBean.setNum_Banco(resultSet.getString("NUM_BANCO"));
                invRechazosBean.setFec_Vence(resultSet.getString("FEC_VENCE"));
                invRechazosBean.setMueve_Tits(resultSet.getString("MUEVE_TIT"));
                invRechazosBean.setMueve_Efectivo(resultSet.getString("MUEVE_EFE"));
                invRechazosBean.setNom_File(resultSet.getString("NOM_FILE"));

                // Add_List
                detalleRechazos.add(invRechazosBean);
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return detalleRechazos;
    }

    public List<InvRechazosBean> onTesoreria_Detalle_Rechazados_Claves(String FecValor, InvRechazosBean RechazosBean) {

        String QuerySql = "", sQueryOrden = "", sFromQuery = "", sFiltro = "", sQueryDet = "";
        List<InvRechazosBean> detalleRechazos = new ArrayList<>();

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            QuerySql = "SELECT INV_ROW_ID ID,INV_CVE_OPERACION CLAVE,INV_TIPO_VALOR TIPO_VALOR,INT(INV_NUM_CTO) NUM_CTO,"
                    + " SMALLINT(INV_NUM_SUBCTO) NUM_SUBCTO,INV_PIZARRA PIZARRA,INV_SERIE SERIE,SMALLINT(INV_CUPON) CUPON,"
                    + " DECIMAL(INV_TITULOS,16,0) TITULOS,DECIMAL(INV_PRECIO,21,9) PRECIO,DECIMAL(INV_IMPORTE,16,2) IMPORTE,"
                    + " DECIMAL(INV_TASA_REND,16,8) TASA,SMALLINT(INV_PLAZO) PLAZO,DECIMAL(INV_PREMIO,16,2) PREMIO,INV_FEC_MOVTO FECHA_REG,"
                    + " INV_FEC_ATENCION FECHA_APL,DECIMAL(INV_COMISION,16,2) COMISION,DECIMAL(INV_IVA,16,2) IVA,DECIMAL(INV_ISR,16,2) ISR,"
                    + " DECIMAL(INV_TIPO_CAMBIO,18,8) TIPO_CAMBIO,COALESCE(SUBSTR(C45.CVE_FORMA_EMP_CVE,1,4),INV_MERCADO) MERCADO,INV_ORDEN ORDEN,"
                    + " INV_NUM_TRANS NUM_TRANS,DECIMAL(INV_CTOINV,11,0) CTO_INV,"
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (CASE c662.CVE_LIMINF_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_TITS WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_TIT,"
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (CASE c662.CVE_LIMSUP_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_EFEC WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_EFE,"
                    + " 0 NUM_TRANS,SMALLINT(INV_NUM_MONEDA) MDA,INV_ESTATUS ESTATUS,"
                    + " CASE INV_DESC_ERROR WHEN 'PRINCIPAL CON ERROR' THEN '4.- LA CLAVE DE OPERACION (TIPO_MOVIMIENTO)'||INV_CVE_OPERACION||' NO EXISTE EN CLAVES 131.' ELSE INV_DESC_ERROR END ERROR,"
                    + " INV_NOM_FILE NOM_FILE,INV_NUM_BANCO NUM_BANCO,"
                    + " INV_FEC_VENCE FEC_VENCE,INV_ORIGEN ORIGEN "
                    + " FROM SAF.INV_RECHAZOS "
                    + " LEFT OUTER JOIN SAF.OPERTRAN3 ON TRA_FECHA_MOV =INV_FEC_MOVTO AND TRA_AGRUPA2 = INV_ROW_ID and TRA_NOM_FILE =INV_NOM_FILE"
                    + " LEFT OUTER JOIN SAF.CLAVES c45 ON C45.CVE_NUM_CLAVE=45 AND C45.CVE_CVE_ST_CLAVE = 'ACTIVO' AND C45.CVE_NUM_SEC_CLAVE = TRA_CVE_TIPO_MERCA"
                    + " LEFT OUTER JOIN SAF.CLAVES c131 ON c131.CVE_NUM_CLAVE = 131 AND  c131.CVE_DESC_CLAVE = INV_CVE_OPERACION"
                    + " LEFT OUTER JOIN SAF.CLAVES c662 ON c662.CVE_NUM_CLAVE = 662 AND  c662.CVE_DESC_CLAVE = SUBSTR(c131.CVE_FORMA_EMP_CVE,1,2)"
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA') "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!RechazosBean.getClave().equals("")) {
                sFiltro += " AND INV_CVE_OPERACION = ? ";
            }

            sQueryOrden = " ORDER BY INV_FEC_MOVTO,INV_ROW_ID ";

            sQueryDet = QuerySql + sFromQuery + sFiltro + sQueryOrden;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sQueryDet);

            preparedStatement.setDate(1, FechaSql);

            if (!RechazosBean.getClave().equals("")) {
                preparedStatement.setString(2, RechazosBean.getClave().trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                InvRechazosBean invRechazosBean = new InvRechazosBean();

                // Set_Values
                invRechazosBean.setOrigen(resultSet.getString("ORIGEN"));
                invRechazosBean.setRow_Id(resultSet.getInt("ID"));
                invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                invRechazosBean.setNum_Moneda(resultSet.getString("MDA"));
                invRechazosBean.setClave(resultSet.getString("CLAVE"));
                invRechazosBean.setTipo_Valor(resultSet.getString("TIPO_VALOR"));
                invRechazosBean.setNum_Cto(resultSet.getString("NUM_CTO"));
                invRechazosBean.setNum_Subcto(resultSet.getString("NUM_SUBCTO"));
                invRechazosBean.setStatus(resultSet.getString("ESTATUS"));
                invRechazosBean.setDesc_Error(resultSet.getString("ERROR"));
                invRechazosBean.setPizarra(resultSet.getString("PIZARRA"));
                invRechazosBean.setSerie(resultSet.getString("SERIE"));
                invRechazosBean.setCupon(resultSet.getString("CUPON"));

                if (resultSet.getString("TITULOS") != null) {
                    invRechazosBean.setTitulos(formatFormatTit(resultSet.getDouble("TITULOS")));
                } else {
                    invRechazosBean.setTitulos(resultSet.getString("TITULOS"));
                }

                if (resultSet.getString("PRECIO") != null) {
                    invRechazosBean.setPrecio(formatDecimalPre(resultSet.getDouble("PRECIO")));
                } else {
                    invRechazosBean.setPrecio(resultSet.getString("PRECIO"));
                }

                if (resultSet.getString("IMPORTE") != null) {
                    invRechazosBean.setImporte(formatDecimalImp(resultSet.getDouble("IMPORTE")));
                } else {
                    invRechazosBean.setImporte(resultSet.getString("IMPORTE"));
                }

                if (resultSet.getString("TASA") != null) {
                    invRechazosBean.setTasa_Rend(formatDecimalTasa(resultSet.getDouble("TASA")));
                } else {
                    invRechazosBean.setTasa_Rend(resultSet.getString("TASA"));
                }

                invRechazosBean.setPlazo(resultSet.getInt("PLAZO"));

                if (resultSet.getString("PREMIO") != null) {
                    invRechazosBean.setPremio(formatDecimalImp(resultSet.getDouble("PREMIO")));
                } else {
                    invRechazosBean.setPremio(resultSet.getString("PREMIO"));
                }

                invRechazosBean.setFec_Apl(resultSet.getString("FECHA_APL"));

                if (resultSet.getString("COMISION") != null) {
                    invRechazosBean.setComision(formatDecimalImp(resultSet.getDouble("COMISION")));
                } else {
                    invRechazosBean.setComision(resultSet.getString("COMISION"));
                }

                if (resultSet.getString("IVA") != null) {
                    invRechazosBean.setIVA(formatDecimalImp(resultSet.getDouble("IVA")));
                } else {
                    invRechazosBean.setIVA(resultSet.getString("IVA"));
                }

                if (resultSet.getString("ISR") != null) {
                    invRechazosBean.setISR(formatDecimalImp(resultSet.getDouble("ISR")));
                } else {
                    invRechazosBean.setISR(resultSet.getString("ISR"));
                }

                if (resultSet.getString("TIPO_CAMBIO") != null) {
                    invRechazosBean.setTipo_Cambio(formatDecimalTP(resultSet.getDouble("TIPO_CAMBIO")));
                } else {
                    invRechazosBean.setTipo_Cambio(resultSet.getString("TIPO_CAMBIO"));
                }

                invRechazosBean.setMercado(resultSet.getString("MERCADO"));
                invRechazosBean.setOrden(resultSet.getString("ORDEN"));
                invRechazosBean.setNum_Trans(resultSet.getString("NUM_TRANS"));
                invRechazosBean.setNum_Banco(resultSet.getString("NUM_BANCO"));
                invRechazosBean.setFec_Vence(resultSet.getString("FEC_VENCE"));
                invRechazosBean.setMueve_Tits(resultSet.getString("MUEVE_TIT"));
                invRechazosBean.setMueve_Efectivo(resultSet.getString("MUEVE_EFE"));
                invRechazosBean.setNom_File(resultSet.getString("NOM_FILE"));

                // Add_List
                detalleRechazos.add(invRechazosBean);
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return detalleRechazos;
    }

    public List<InvRechazosBean> onTesoreria_Detalle_Rechazados_Otros(String FecValor, InvRechazosBean RechazosBean, String sCondicion) {

        int index = 1;
        List<InvRechazosBean> detalleRechazos = new ArrayList<>();
        String QuerySql = "", sQueryOrden = "", sFiltro = "", sQueryDet = "";

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            QuerySql = "SELECT INV_ROW_ID ID,				INV_CVE_OPERACION CLAVE,	"
                    + "	INV_TIPO_VALOR TIPO_VALOR,			INT(INV_NUM_CTO) NUM_CTO, "
                    + " SMALLINT(INV_NUM_SUBCTO) NUM_SUBCTO,INV_PIZARRA PIZARRA,"
                    + " INV_SERIE SERIE,					SMALLINT(INV_CUPON) CUPON, "
                    + " DECIMAL(INV_TITULOS,16,0) TITULOS,	DECIMAL(INV_PRECIO,21,9) PRECIO,"
                    + " DECIMAL(INV_IMPORTE,16,2) IMPORTE,  DECIMAL(INV_TASA_REND,16,8) TASA,"
                    + " SMALLINT(INV_PLAZO) PLAZO,			DECIMAL(INV_PREMIO,16,2) PREMIO,"
                    + " INV_FEC_MOVTO FECHA_REG,  			INV_FEC_ATENCION FECHA_APL,"
                    + " DECIMAL(INV_COMISION,16,2) COMISION,DECIMAL(INV_IVA,16,2) IVA,"
                    + " DECIMAL(INV_ISR,16,2) ISR,  		DECIMAL(INV_TIPO_CAMBIO,18,8) TIPO_CAMBIO,"
                    + " COALESCE(SUBSTR(C45.CVE_FORMA_EMP_CVE,1,4),INV_MERCADO) MERCADO,  INV_ORDEN ORDEN, "
                    + " INV_NUM_TRANS NUM_TRANS,			DECIMAL(INV_CTOINV,11,0) CTO_INV, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMINF_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_TITS WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_TIT, "
                    + " CASE INV_ORIGEN WHEN 'OPERTRAN3' THEN (case c662.CVE_LIMSUP_CLAVE WHEN 1 THEN 'SI' ELSE 'NO' END) ELSE (CASE INV_MUEVE_EFEC WHEN '0' THEN 'NO' ELSE 'SI' END) END MUEVE_EFE, "
                    + " 0 NUM_TRANS,						SMALLINT(INV_NUM_MONEDA) MDA,"
                    + " INV_ESTATUS ESTATUS,				INV_DESC_ERROR ERROR,"
                    + " INV_NOM_FILE NOM_FILE,				INV_NUM_BANCO NUM_BANCO, "
                    + " INV_FEC_VENCE FEC_VENCE,			INV_ORIGEN ORIGEN "
                    + " FROM SAF.INV_RECHAZOS "
                    + " LEFT OUTER JOIN SAF.OPERTRAN3 ON TRA_FECHA_MOV =INV_FEC_MOVTO and TRA_AGRUPA2 = INV_ROW_ID and TRA_NOM_FILE =INV_NOM_FILE "
                    + " LEFT OUTER JOIN SAF.CLAVES c45 ON C45.CVE_NUM_CLAVE=45 AND C45.CVE_CVE_ST_CLAVE = 'ACTIVO' AND C45.CVE_NUM_SEC_CLAVE = TRA_CVE_TIPO_MERCA "
                    + " LEFT OUTER JOIN SAF.claves c131 ON c131.cve_num_clave = 131 and  c131.CVE_DESC_CLAVE = INV_CVE_OPERACION "
                    + " LEFT OUTER JOIN SAF.claves c662 ON c662.cve_num_clave = 662 and  c662.CVE_DESC_CLAVE = SUBSTR(c131.CVE_FORMA_EMP_CVE,1,2) "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') "
                    + " AND NOT ( (SUBSTR(COALESCE(INV_DESC_ERROR,''),1,3)= '7.-') AND (INV_DESC_ERROR LIKE '%NO EXISTE%')) "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!RechazosBean.getCto_Inv().equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            if (!RechazosBean.getClave().equals("")) {
                sFiltro += " AND INV_CVE_OPERACION = ? ";
            }

            if (!sCondicion.equals("")) {
                sFiltro += " AND INV_DESC_ERROR LIKE '%NO.OPERACION%' ";
            }

            sQueryOrden = " ORDER BY INV_FEC_MOVTO,INV_ROW_ID ";

            sQueryDet = QuerySql + sFiltro + sQueryOrden;

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sQueryDet);

            preparedStatement.setDate(1, FechaSql);

            if (!RechazosBean.getCto_Inv().equals("")) {
                index = index + 1;
                preparedStatement.setDouble((index), Integer.parseInt(RechazosBean.getCto_Inv().trim()));
            }

            if (!RechazosBean.getClave().equals("")) {
                index = index + 1;
                preparedStatement.setString((index), RechazosBean.getClave().trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                InvRechazosBean invRechazosBean = new InvRechazosBean();

                // Set_Values
                invRechazosBean.setOrigen(resultSet.getString("ORIGEN"));
                invRechazosBean.setRow_Id(resultSet.getInt("ID"));
                invRechazosBean.setCto_Inv(resultSet.getString("CTO_INV"));
                invRechazosBean.setNum_Moneda(resultSet.getString("MDA"));
                invRechazosBean.setClave(resultSet.getString("CLAVE"));
                invRechazosBean.setTipo_Valor(resultSet.getString("TIPO_VALOR"));
                invRechazosBean.setNum_Cto(resultSet.getString("NUM_CTO"));
                invRechazosBean.setNum_Subcto(resultSet.getString("NUM_SUBCTO"));
                invRechazosBean.setStatus(resultSet.getString("ESTATUS"));
                invRechazosBean.setDesc_Error(resultSet.getString("ERROR"));
                invRechazosBean.setPizarra(resultSet.getString("PIZARRA"));
                invRechazosBean.setSerie(resultSet.getString("SERIE"));
                invRechazosBean.setCupon(resultSet.getString("CUPON"));

                if (resultSet.getString("TITULOS") != null) {
                    invRechazosBean.setTitulos(formatFormatTit(resultSet.getDouble("TITULOS")));
                } else {
                    invRechazosBean.setTitulos(resultSet.getString("TITULOS"));
                }

                if (resultSet.getString("PRECIO") != null) {
                    invRechazosBean.setPrecio(formatDecimalPre(resultSet.getDouble("PRECIO")));
                } else {
                    invRechazosBean.setPrecio(resultSet.getString("PRECIO"));
                }

                if (resultSet.getString("IMPORTE") != null) {
                    invRechazosBean.setImporte(formatDecimalImp(resultSet.getDouble("IMPORTE")));
                } else {
                    invRechazosBean.setImporte(resultSet.getString("IMPORTE"));
                }

                if (resultSet.getString("TASA") != null) {
                    invRechazosBean.setTasa_Rend(formatDecimalTasa(resultSet.getDouble("TASA")));
                } else {
                    invRechazosBean.setTasa_Rend(resultSet.getString("TASA"));
                }

                invRechazosBean.setPlazo(resultSet.getInt("PLAZO"));

                if (resultSet.getString("PREMIO") != null) {
                    invRechazosBean.setPremio(formatDecimalImp(resultSet.getDouble("PREMIO")));
                } else {
                    invRechazosBean.setPremio(resultSet.getString("PREMIO"));
                }

                invRechazosBean.setFec_Apl(resultSet.getString("FECHA_APL"));

                if (resultSet.getString("COMISION") != null) {
                    invRechazosBean.setComision(formatDecimalImp(resultSet.getDouble("COMISION")));
                } else {
                    invRechazosBean.setComision(resultSet.getString("COMISION"));
                }

                if (resultSet.getString("IVA") != null) {
                    invRechazosBean.setIVA(formatDecimalImp(resultSet.getDouble("IVA")));
                } else {
                    invRechazosBean.setIVA(resultSet.getString("IVA"));
                }

                if (resultSet.getString("ISR") != null) {
                    invRechazosBean.setISR(formatDecimalImp(resultSet.getDouble("ISR")));
                } else {
                    invRechazosBean.setISR(resultSet.getString("ISR"));
                }

                if (resultSet.getString("TIPO_CAMBIO") != null) {
                    invRechazosBean.setTipo_Cambio(formatDecimalTP(resultSet.getDouble("TIPO_CAMBIO")));
                } else {
                    invRechazosBean.setTipo_Cambio(resultSet.getString("TIPO_CAMBIO"));
                }

                invRechazosBean.setMercado(resultSet.getString("MERCADO"));
                invRechazosBean.setOrden(resultSet.getString("ORDEN"));
                invRechazosBean.setNum_Trans(resultSet.getString("NUM_TRANS"));
                invRechazosBean.setNum_Banco(resultSet.getString("NUM_BANCO"));
                invRechazosBean.setFec_Vence(resultSet.getString("FEC_VENCE"));
                invRechazosBean.setMueve_Tits(resultSet.getString("MUEVE_TIT"));
                invRechazosBean.setMueve_Efectivo(resultSet.getString("MUEVE_EFE"));
                invRechazosBean.setNom_File(resultSet.getString("NOM_FILE"));

                // Add_List
                detalleRechazos.add(invRechazosBean);
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return detalleRechazos;
    }

    public List<Double> onTesoreria_Totales_DetNoIdentificados(String FecValor, InvRechazosBean RechazosBean) {

        String QuerySql = "", sFiltro = "";
        List<Double> Totales_detalleRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql = "SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, "
                    + " ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,"
                    + "ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR "
                    + " FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR','NO FIDUCIA') "
                    + " and INV_FEC_MOVTO >= ? "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " UNION ALL "
                    + " SELECT 0 TOTAL_IMP,COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') "
                    + " AND INV_DESC_ERROR NOT LIKE '%LIGADO%' "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND INV_FEC_MOVTO >= ? ";

            if (!RechazosBean.getCto_Inv().equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            QuerySql += sFiltro + ") A";

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql);

            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!RechazosBean.getCto_Inv().equals("")) {
                preparedStatement.setDouble(3, Double.parseDouble(RechazosBean.getCto_Inv().trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detalleRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_detalleRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_detalleRechazos;
    }

    public List<Double> onTesoreria_Totales_DetPorAsignar(String FecValor, String sCondicion) {

        String QuerySql = "", sFiltro = "";
        List<Double> Totales_detalleRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql = "SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, "
                    + " ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,"
                    + " ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR "
                    + " FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR','NO FIDUCIA') "
                    + " and INV_FEC_MOVTO >= ? "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " UNION ALL "
                    + " SELECT 0 TOTAL_IMP,COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('POR ASIGNAR') "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND  INV_FEC_MOVTO >= ? ";

            if (!sCondicion.equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            QuerySql += sFiltro + ") A";

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql);

            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!sCondicion.equals("")) {
                preparedStatement.setDouble(3, Double.parseDouble(sCondicion.trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detalleRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_detalleRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_detalleRechazos;
    }

    public List<Double> onTesoreria_Totales_DetNoPertenece(String FecValor, String sCondicion) {

        String QuerySql = "", sFiltro = "";
        List<Double> Totales_detalleRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql = "SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, "
                    + " ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,"
                    + "ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR "
                    + " FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR','NO FIDUCIA') "
                    + " and INV_FEC_MOVTO >= ? "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " UNION ALL "
                    + " SELECT 0 TOTAL_IMP,COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS"
                    + " WHERE INV_ESTATUS ='NO FIDUCIA' "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND INV_FEC_MOVTO >= ? ";

            if (!sCondicion.equals("")) {
                sFiltro = " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            QuerySql += sFiltro + ") A";

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql);

            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!sCondicion.equals("")) {
                preparedStatement.setDouble(3, Double.parseDouble(sCondicion.trim()));
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detalleRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_detalleRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_detalleRechazos;
    }

    public List<Double> onTesoreria_Totales_DetSinClave(String FecValor, InvRechazosBean RechazosBean) {

        String QuerySql = "", sFiltro = "";
        List<Double> Totales_detalleRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql = "SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, "
                    + " ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,"
                    + "ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR "
                    + " FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR','NO FIDUCIA') "
                    + " and INV_FEC_MOVTO >= ? "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " UNION ALL "
                    + " SELECT 0 TOTAL_IMP,COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA') "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND INV_FEC_MOVTO >= ? ";

            if (!RechazosBean.getClave().equals("")) {
                sFiltro += " AND INV_CVE_OPERACION = ?";
            }

            QuerySql += sFiltro + ") A";

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql);

            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!RechazosBean.getClave().equals("")) {
                preparedStatement.setString(3, RechazosBean.getClave().trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detalleRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_detalleRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_detalleRechazos;
    }

    public List<Double> onTesoreria_Totales_DetOtrosRech(String FecValor, InvRechazosBean RechazosBean, String sCondicion) {

        int index = 2;
        String QuerySql = "", sFiltro = "";
        List<Double> Totales_detalleRechazos = new ArrayList<>();

        try {
            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

            // SQL
            QuerySql = "SELECT ROUND(SUM(TOTAL_IMP),2) TOTAL_IMP, "
                    + " ROUND(SUM(TOTAL_IMP_ERR),2) TOTAL_IMP_ERR,"
                    + "ROUND((SUM(TOTAL_IMP_ERR)*100)/SUM(TOTAL_IMP),4) PJE_ERR "
                    + " FROM ( SELECT COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP, 0 TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR','NO FIDUCIA') "
                    + " and INV_FEC_MOVTO >= ? "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " UNION ALL "
                    + " SELECT 0 TOTAL_IMP,COALESCE(SUM(DOUBLE(INV_IMPORTE)),0) TOTAL_IMP_ERR "
                    + " FROM SAF.INV_RECHAZOS "
                    + " WHERE INV_ESTATUS IN('ERROR','NOAPLICA','POR ASIGNAR') "
                    + " AND NOT (  (SUBSTR(COALESCE(INV_DESC_ERROR,''),1,3)= '7.-') AND (INV_DESC_ERROR LIKE '%NO EXISTE%')) "
                    + " AND INV_CVE_OPERACION != 'EFECTIVO' "
                    + " AND (UPPER(INV_NOM_FILE) LIKE 'MI%' OR UPPER(INV_NOM_FILE) LIKE 'MB%') "
                    + " AND INV_FEC_MOVTO >=  ? ";

            if (!RechazosBean.getNum_Cto().equals("")) {
                sFiltro += " AND (DECIMAL(INV_CTOINV,11,0),SMALLINT(INV_ENTIDAD_FIN)) IN(SELECT CPR_CONTRATO_INTER,CPR_ENTIDAD_FIN FROM SAF.CONTINTE WHERE CPR_NUM_CONTRATO = ? )";
            }

            if (!RechazosBean.getCto_Inv().equals("")) {
                sFiltro += " AND DECIMAL(INV_CTOINV,11,0) = ? ";
            }

            if (!RechazosBean.getClave().equals("")) {
                sFiltro += " AND INV_CVE_OPERACION = ? ";
            }

            if (!sCondicion.equals("")) {
                sFiltro += " AND INV_DESC_ERROR LIKE '%NO.OPERACION%' ";
            }

            QuerySql += sFiltro + ") A";

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(QuerySql);

            preparedStatement.setDate(1, FechaSql);
            preparedStatement.setDate(2, FechaSql);

            if (!RechazosBean.getCto_Inv().equals("")) {
                index = index + 1;
                preparedStatement.setInt((index), Integer.parseInt(RechazosBean.getNum_Cto().trim()));
            }

            if (!RechazosBean.getCto_Inv().equals("")) {
                index = index + 1;
                preparedStatement.setDouble((index), Double.parseDouble(RechazosBean.getCto_Inv().trim()));
            }

            if (!RechazosBean.getClave().equals("")) {
                index = index + 1;
                preparedStatement.setString((index), RechazosBean.getClave().trim());
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detalleRechazos.add(resultSet.getDouble("TOTAL_IMP_ERR"));
                    Totales_detalleRechazos.add(resultSet.getDouble("PJE_ERR"));
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return Totales_detalleRechazos;
    }

    public List<String> onTesoreria_existeEnRechazoCtoInv(String sCtoInv, int iIntermediario) {

        List<String> existeRechazos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT INV_NUM_CTO, INV_ESTATUS ");
            stringBuilder.append(" FROM SAF.INV_RECHAZO_CTOINV ");
            stringBuilder.append(" WHERE INV_CTOINV = ? ");
            stringBuilder.append(" AND INV_ENTIDAD_FIN = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setString(1, sCtoInv);
            preparedStatement.setInt(2, iIntermediario);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    existeRechazos.add(resultSet.getString("INV_NUM_CTO"));
                    existeRechazos.add(resultSet.getString("INV_ESTATUS"));
                }
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return existeRechazos;
    }

    public String onTesoreria_NombreContrato(String sCto) {

        String sNombre = "";
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT CTO_NOM_CONTRATO NOMBRE, CTO_CVE_ST_CONTRAT ESTATUS ");
            stringBuilder.append("FROM SAF.CONTRATO ");
            stringBuilder.append(" WHERE CTO_NUM_CONTRATO = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, Integer.parseInt(sCto));

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString("ESTATUS").equals("ACTIVO")) {
                        sNombre = resultSet.getString("NOMBRE");
                    } else {
                        FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "El Fideicomiso " + sCto + " se encuentra con estatus " + resultSet.getString("ESTATUS")));
                    }
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return sNombre;
    }

    public ArrayList<ContratoBean> onTesoreria_ObtenerListadoContratosPorAsignar(String sCto, String nomContrato) {

        StringBuilder stringBuilder = new StringBuilder();
        int iPosicion = 0;
        ArrayList<ContratoBean> contratos = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT CTO_NUM_CONTRATO CONTRATO, CTO_NOM_CONTRATO NOMBRE, CTO_CVE_ST_CONTRAT ESTATUS ");
            stringBuilder.append("FROM SAF.CONTRATO ");
            stringBuilder.append(" WHERE ");

            if (!"".equals(sCto)) {
                stringBuilder.append(" CTO_NUM_CONTRATO = ? ");
                iPosicion++;
            }

            if (!"".equals(sCto) && iPosicion == 1) {
                stringBuilder.append(" AND ");
            }

            if (!"".equals(nomContrato)) {
                stringBuilder.append(" CTO_NOM_CONTRATO LIKE UPPER(?) ");
                iPosicion++;
            }

            if (!"".equals(nomContrato) && iPosicion > 0) {
                stringBuilder.append(" AND ");
            }

            stringBuilder.append(" CTO_CVE_ST_CONTRAT = ? ");
            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());
            iPosicion = 0;
            // Parameters
            if (!"".equals(sCto)) {
                iPosicion++;
                preparedStatement.setInt(iPosicion, Integer.parseInt(sCto));
            }

            if (!"".equals(nomContrato)) {
                iPosicion++;
                preparedStatement.setString(iPosicion, "%" + nomContrato + "%");
            }

            iPosicion++;
            preparedStatement.setString(iPosicion, "ACTIVO");

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    ContratoBean contrato = new ContratoBean();
                    contrato.setContrato(resultSet.getInt("CONTRATO"));
                    contrato.setNombre(resultSet.getString("NOMBRE"));
                    contratos.add(contrato);
                }
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return contratos;
    }

    public OutParameterBean onTesoreria_Ejecuta_AdministraCtoInv(int lSec, String sTipoOper, double sCtoInv, int sInterm, double sNumFiso,
            String sPlaza, String sPromotor, String sFormaManejo, String sCtaCheques) {

        OutParameterBean outParameterBean = new OutParameterBean();

        // SQL
        String sql = "{CALL DB2FIDUC.SP_INV_RECHAZO_CTO (?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, lSec);
            callableStatement.setDouble(2, sCtoInv);
            callableStatement.setInt(3, sInterm);
            callableStatement.setDouble(4, sNumFiso);
            callableStatement.setString(5, sPlaza);
            callableStatement.setString(6, sPromotor);
            callableStatement.setString(7, sFormaManejo);
            callableStatement.setString(8, sCtaCheques);
            callableStatement.setString(9, sTipoOper);
            callableStatement.setInt(10, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (sMsErr.equals("")) {
                bitacoraBean.setDetalle("Operación aplicada correctamente ");
            } else {
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            }

            // SET_VALUES_BITACORA
            outParameterBean.setPsMsgErrOut(sMsErr);
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoOper.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_RECHAZO_INVER".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public boolean onTesoreria_Clave132(String sCtoInv) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        Connection conectar = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //Verifica_Si_Existe_la_clave_132
            // SQL
            stringBuilder.append("SELECT COUNT(*) NUM_REGS FROM SAF.CLAVES WHERE CVE_NUM_CLAVE = 132 AND CVE_NUM_SEC_CLAVE = 1 ");

            // Call_Operaciones_BD
            conectar = DataBaseConexion.getInstance().getConnection();
            pstmt = conectar.prepareStatement(stringBuilder.toString());

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            while (rs.next()) {
                iReg = rs.getInt("NUM_REGS");
                if (iReg == 0) {

                    if (onTesoreria_InsertClave132(sCtoInv)) {
                        valorRetorno = true;
                    }

                } else {

                    if (onTesoreria_UpdateClave132(sCtoInv)) {
                        valorRetorno = true;
                    }
                }
            }

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conectar != null) {
                    conectar.close();
                }
            } catch (SQLException sqlException) {
                mensajeError += "Error al cerrar la conexión: ".concat(sqlException.getMessage());
            }
        }

        return valorRetorno;
    }

    public boolean onTesoreria_InsertClave132(String sCtoInv) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("INSERT INTO SAF.CLAVES (CVE_NUM_CLAVE, CVE_NUM_SEC_CLAVE, CVE_DESC_CLAVE, CVE_LIMINF_CLAVE, CVE_LIMSUP_CLAVE, CVE_FORMA_EMP_CVE, CVE_ANO_ALTA_REG, CVE_MES_ALTA_REG, CVE_DIA_ALTA_REG, CVE_ANO_ULT_MOD, CVE_MES_ULT_MOD, CVE_DIA_ULT_MOD, CVE_CVE_ST_CLAVE )");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setInt(1, 132);
            preparedStatement.setInt(2, 1);
            preparedStatement.setString(3, sCtoInv.trim());
            preparedStatement.setDouble(4, 0);
            preparedStatement.setDouble(5, 0);
            preparedStatement.setString(6, "CONTRATO DE INVERSION A REPROCESAR");
            preparedStatement.setInt(7, LocalDate.now().getYear());
            preparedStatement.setInt(8, LocalDate.now().getMonthValue());
            preparedStatement.setInt(9, LocalDate.now().getDayOfMonth());
            preparedStatement.setInt(10, LocalDate.now().getYear());
            preparedStatement.setInt(11, LocalDate.now().getMonthValue());
            preparedStatement.setInt(12, LocalDate.now().getDayOfMonth());
            preparedStatement.setString(13, "ACTIVO");

            // Execute_Update
            iReg = preparedStatement.executeUpdate();

            if (iReg > 0) {
                return true;
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return false;
    }

    public boolean onTesoreria_UpdateClave132(String sCtoInv) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean iRespuesta = false;
        try {

            // Set_SQL
            stringBuilder.append("UPDATE SAF.CLAVES SET CVE_DESC_CLAVE = ? WHERE CVE_NUM_CLAVE = ? AND CVE_NUM_SEC_CLAVE = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sCtoInv.trim());
            preparedStatement.setInt(2, 132);
            preparedStatement.setInt(3, 1);

            // Execute_Update
            iReg = preparedStatement.executeUpdate();

            if (iReg > 0) {
                iRespuesta = true;
            }
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return iRespuesta;
    }

    public boolean onTesoreria_ExisteArchivoInversiones(String sNomFile) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {
            // SQL
            stringBuilder.append("SELECT COUNT(*) NUM_REGS FROM SAF.ARCHIVO_OTROS ");
            stringBuilder.append("WHERE RTRIM(UPPER(OTR_NOM_FILE)) = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sNomFile.trim().toUpperCase());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {
                iReg = resultSet.getInt("NUM_REGS");

                if (iReg > 0) {
                    valorRetorno = true;
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_ReprocesarArchivoInversiones(String sNomFile) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {
            // SQL
            //Verifica_si_lo_puede_sustituir
            stringBuilder.append("SELECT COUNT(*) NUM_REGS FROM SAF.ARCHIVO_OTROS ");
            stringBuilder.append(" WHERE RTRIM(UPPER(OTR_NOM_FILE)) = ? ");
            stringBuilder.append(" AND NOT OTR_ESTATUS IN ('FALSE','ERROR') ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sNomFile.trim().toUpperCase());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {
                iReg = resultSet.getInt("NUM_REGS");

                if (iReg == 0) {
                    valorRetorno = true;
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_GrabaArchivoInversiones(String sNomArchivo, List<ArchivoOtrosBean> archivoOtrosBean) {

        // DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Set_Date
        LocalDate localDate = LocalDate.now();// For reference
        String date = localDate.format(dateTimeformatter);

        boolean valorRetorno = false;

        try {

            java.sql.Date FechaSql = ConvertirFechayyyyMMdd(date);

            /* * * * * * * * * * * * * * * * * * * *
			//Elimina_Archivo_Con_el_Mismo_Nombre
			/* * * * * * * * * * * * * * * * * * * */
            onTesoreria_EliminaArchivoInversiones(sNomArchivo);

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
			//CARGA_ARCHIVO_OTROS_INTERMEDIARIOS EN LA TABLA ARCHIVO OTROS
			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            //SIN_ENCABEZADO
            for (int i = 1; i < archivoOtrosBean.size(); i++) {

                StringBuilder stringBuilder = new StringBuilder();

                // Set_SQL
                stringBuilder.append("INSERT INTO SAF.ARCHIVO_OTROS VALUES (");
                // Set_Values
                stringBuilder.append(" ?, ").append(i).append(", ?, ?, ?, ?, ?, ?, '0', '0', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
                stringBuilder.append("'FALSE',");  //OTR_ESTATUS
                stringBuilder.append("NULL,");	   //OTR_DESC_ERROR
                stringBuilder.append("?)");

                // Call_Operaciones_BD
                conexion = DataBaseConexion.getInstance().getConnection();
                preparedStatement = conexion.prepareStatement(stringBuilder.toString());

                //Set_Values
                preparedStatement.setString(1, sNomArchivo);
                preparedStatement.setString(2, archivoOtrosBean.get(i).getFec_Movto());
                preparedStatement.setString(3, archivoOtrosBean.get(i).getNum_Entidad_Financ());
                preparedStatement.setString(4, archivoOtrosBean.get(i).getCustodio());
                preparedStatement.setString(5, archivoOtrosBean.get(i).getCve_Operacion());
                preparedStatement.setString(6, archivoOtrosBean.get(i).getTipo_Valor());
                preparedStatement.setString(7, archivoOtrosBean.get(i).getCto_Inv());
                preparedStatement.setString(8, archivoOtrosBean.get(i).getPizarra());
                preparedStatement.setString(9, archivoOtrosBean.get(i).getSerie());
                preparedStatement.setString(10, archivoOtrosBean.get(i).getTitulos());
                preparedStatement.setString(11, archivoOtrosBean.get(i).getCupon());
                preparedStatement.setString(12, archivoOtrosBean.get(i).getPrecio());
                preparedStatement.setString(13, archivoOtrosBean.get(i).getImporte());
                preparedStatement.setString(14, archivoOtrosBean.get(i).getTasa());
                preparedStatement.setString(15, archivoOtrosBean.get(i).getPlazo());
                preparedStatement.setString(16, archivoOtrosBean.get(i).getPremio());
                preparedStatement.setString(17, archivoOtrosBean.get(i).getMoneda());
                preparedStatement.setString(18, archivoOtrosBean.get(i).getComision());
                preparedStatement.setString(19, archivoOtrosBean.get(i).getIVA());
                preparedStatement.setString(20, archivoOtrosBean.get(i).getISR());
                preparedStatement.setString(21, archivoOtrosBean.get(i).getTipo_Cambio());
                preparedStatement.setString(22, archivoOtrosBean.get(i).getFec_Vence());
                preparedStatement.setString(23, archivoOtrosBean.get(i).getNum_Banco());
                preparedStatement.setString(24, archivoOtrosBean.get(i).getRedaccion());
                preparedStatement.setDate(25, FechaSql);

                // Execute_Update
                preparedStatement.executeUpdate();

                conexion.close();
                preparedStatement.close();
            }

            valorRetorno = true;
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public boolean onTesoreria_EliminaArchivoInversiones(String sNomArchivo) {

        // Object 
        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        try {

            /* * * * * * * * * * * * * * * * * * * *
			//Elimina_Archivo_Con_el_Mismo_Nombre
			/* * * * * * * * * * * * * * * * * * * */
            // SQL
            stringBuilder.append("DELETE FROM ARCHIVO_OTROS WHERE OTR_NOM_FILE = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sNomArchivo.trim());

            // Execute_Query
            iReg = preparedStatement.executeUpdate();

            if (iReg > 0) {
                valorRetorno = true;
            }

            onCierraConexion();

        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }

    public String onTesoreria_getTipoServicio(String sCveOpera) {

        String sTipoServ = "";
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            //Verifica_si_existe_el_contrato
            stringBuilder.append("SELECT SUBSTR(CVE_FORMA_EMP_CVE,1,2) FORMA_NEG ");
            stringBuilder.append(" FROM SAF.CLAVES WHERE CVE_NUM_CLAVE = 131 ");
            stringBuilder.append(" AND CVE_CVE_ST_CLAVE = 'ACTIVO' ");
            stringBuilder.append(" AND CVE_DESC_CLAVE = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setString(1, sCveOpera.trim());

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {
                sTipoServ = resultSet.getString("FORMA_NEG");
            }

            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return sTipoServ;
    }

    public OutParameterBean onTesoreria_AplicaInversion(String sTipoOper) {

        OutParameterBean outParameterBean = new OutParameterBean();

        // SQL
        String sql = "{CALL DB2FIDUC.SP_INV_RECHAZO_APL (?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setString(1, sTipoOper);
            callableStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("usuarioNumero").toString()));

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (sCode != 0 || sMsErr.trim().length() > 0) {
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + " SqlCode: " + sCode + " SqlState: " + sEstado);
            } else {
                bitacoraBean.setDetalle("Operación aplicada correctamente ");
            }

            // SET_VALUES_BITACORA
            outParameterBean.setPsMsgErrOut(sMsErr);
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoOper.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_RECHAZO_INVER".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (SQLException | NumberFormatException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public List<ArchivoOtrosBean> onTesoreria_ActualizaOtrosResumen() {

        String FecValor = "";
        StringBuilder stringBuilder = new StringBuilder();
        List<ArchivoOtrosBean> resumenOtros = new ArrayList<>();

        try {
            // SQL
            /*'-------------------------------------------------------------------------------------------------
		    -- OTROS INTERMEDIARIOS RESUMEN
		    -------------------------------------------------------------------------------------------------*/

            stringBuilder.append("SELECT OTR_FEC_ALTA_REG, OTR_NOM_FILE, OTR_ENTIDAD_FIN, COALESCE(INT_INTERMEDIARIO,RTRIM(CHAR(OTR_ENTIDAD_FIN))) INTERMEDIARIO,");
            stringBuilder.append("OTR_NUM_CUSTODIO, COALESCE(NOM_CUSTODIO,RTRIM(CHAR(OTR_NUM_CUSTODIO))) CUSTODIO,");
            stringBuilder.append("OTR_NUM_MONEDA NUM_MDA,(SELECT MON_NOM_MONEDA FROM MONEDAS WHERE MON_NUM_PAIS = DOUBLE(OTR_NUM_MONEDA)) NOM_MDA,");
            stringBuilder.append("SUM(CASE OTR_ESTATUS WHEN 'ERROR' THEN 0 ELSE 1 END) TOTAL_REGS, SUM(CASE OTR_ESTATUS WHEN 'ERROR' THEN 0 ELSE DOUBLE(OTR_IMPORTE) END) TOTAL_IMP,");
            stringBuilder.append("SUM(CASE OTR_ESTATUS WHEN 'ERROR' THEN 1 ELSE 0 END) TOTAL_REGS_ERR, SUM(CASE OTR_ESTATUS WHEN 'ERROR' THEN DOUBLE(OTR_IMPORTE) ELSE 0 END) TOTAL_IMP_ERR ");
            stringBuilder.append("FROM SAF.ARCHIVO_OTROS LEFT JOIN SAF.INTERMED ON INT_ENTIDAD_FIN=DOUBLE(OTR_ENTIDAD_FIN) ");
            stringBuilder.append(" LEFT JOIN (SELECT CVE_NUM_SEC_CLAVE NUM_CUSTODIO,CVE_DESC_CLAVE NOM_CUSTODIO FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=386) CUSTODIO ON NUM_CUSTODIO=DOUBLE(OTR_NUM_CUSTODIO) ");
            stringBuilder.append(" FULL JOIN SAF.OPERTRAN3 ON OTR_NOM_FILE= TRA_NOM_FILE AND TRA_AGRUPA2=OTR_ROW_ID AND TRA_CONTRATO_INTER= OTR_CTOINV AND TRA_NOM_PIZARRA= OTR_PIZARRA AND TRA_NUM_SERIE = OTR_SERIE AND OTR_ENTIDAD_FIN = TRA_ENTIDAD_FIN ");
            stringBuilder.append(" WHERE  OTR_NOM_FILE <> '' ");

            if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 0) {
                FecValor = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño").toString()) + "-" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes").toString() + "-01";

                stringBuilder.append(" and DATE(substr(OTR_FEC_MOVTO,7,4) || '-' ||  substr(OTR_FEC_MOVTO,4,2) || '-' ||  substr(OTR_FEC_MOVTO,1,2)) >= ? ");
            }

            stringBuilder.append(" GROUP BY OTR_FEC_ALTA_REG,OTR_NOM_FILE,OTR_ENTIDAD_FIN,INT_INTERMEDIARIO,OTR_NUM_CUSTODIO,NOM_CUSTODIO,OTR_NUM_MONEDA ");
            stringBuilder.append(" ORDER BY OTR_FEC_ALTA_REG DESC,OTR_NOM_FILE DESC,OTR_ENTIDAD_FIN,INT_INTERMEDIARIO,OTR_NUM_CUSTODIO,NOM_CUSTODIO,OTR_NUM_MONEDA ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 0) {
                java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);
                preparedStatement.setDate(1, FechaSql);
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    ArchivoOtrosBean archivoBean = new ArchivoOtrosBean();

                    archivoBean.setFec_Alta(resultSet.getString("OTR_FEC_ALTA_REG"));
                    archivoBean.setNom_file(resultSet.getString("OTR_NOM_FILE"));
                    archivoBean.setNum_Entidad_Financ(resultSet.getString("OTR_ENTIDAD_FIN"));
                    archivoBean.setIntermediario(resultSet.getString("INTERMEDIARIO"));
                    archivoBean.setCustodio(resultSet.getString("CUSTODIO"));
                    archivoBean.setNum_Custodio(resultSet.getInt("OTR_NUM_CUSTODIO"));
                    archivoBean.setMoneda(resultSet.getString("NOM_MDA"));
                    archivoBean.setId_Moneda(resultSet.getInt("NUM_MDA"));
                    archivoBean.setTotal_Reg(resultSet.getInt("TOTAL_REGS"));
                    archivoBean.setTotal_Imp(resultSet.getDouble("TOTAL_IMP"));
                    archivoBean.setTotal_Reg_Err(resultSet.getInt("TOTAL_REGS_ERR"));
                    archivoBean.setTotal_Imp_Err(resultSet.getDouble("TOTAL_IMP_ERR"));

                    // Add_List
                    resumenOtros.add(archivoBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return resumenOtros;
    }

    public List<ArchivoOtrosBean> onTesoreria_DetalleOtrosIntermediarios(String Nom_file, String Num_Entidad_Financ,
            int Num_Custodio, int Id_Moneda) {

        String FecValor = "";
        StringBuilder stringBuilder = new StringBuilder();
        List<ArchivoOtrosBean> otrosIntermediarios = new ArrayList<>();

        try {
            // SQL
            /*'-------------------------------------------------------------------------------------------------
		    -- OTROS INTERMEDIARIOS RESUMEN
		    -------------------------------------------------------------------------------------------------*/
            stringBuilder.append("SELECT OTR_NOM_FILE, OTR_ROW_ID, OTR_FEC_MOVTO, OTR_ENTIDAD_FIN, OTR_NUM_CUSTODIO, OTR_CVE_OPERACION, ");
            stringBuilder.append("OTR_TIPO_VALOR, OTR_CTOINV, OTR_NUM_CTO, OTR_NUM_SUBCTO, OTR_PIZARRA, OTR_SERIE, OTR_TITULOS, OTR_CUPON, ");
            stringBuilder.append("OTR_PRECIO, OTR_IMPORTE, OTR_TASA_REND, OTR_PLAZO, OTR_PREMIO, OTR_NUM_MONEDA, OTR_COMISION, OTR_IVA, ");
            stringBuilder.append("OTR_ISR, OTR_TIPO_CAMBIO, OTR_FEC_VENCE, OTR_NUM_BANCO, OTR_REDACCION, ");
            stringBuilder.append("CASE WHEN OTR_ESTATUS = 'TRANSFERIDO' THEN TRA_ERR_APLICA ELSE  OTR_DESC_ERROR END AS OTR_DESC_ERROR, ");
            stringBuilder.append("CASE WHEN OTR_ESTATUS = 'TRANSFERIDO' THEN TRA_CVE_ST_TRANSFE ELSE OTR_ESTATUS END AS OTR_ESTATUS, OTR_FEC_ALTA_REG ");
            stringBuilder.append(" FROM SAF.ARCHIVO_OTROS LEFT JOIN SAF.OPERTRAN3 ON OTR_NOM_FILE= TRA_NOM_FILE AND TRA_AGRUPA2 = OTR_ROW_ID ");
            stringBuilder.append(" AND TRA_CONTRATO_INTER= OTR_CTOINV AND TRA_NOM_PIZARRA= OTR_PIZARRA AND TRA_NUM_SERIE = OTR_SERIE ");
            stringBuilder.append(" AND OTR_ENTIDAD_FIN = TRA_ENTIDAD_FIN WHERE OTR_NOM_FILE = ? ");
            stringBuilder.append(" AND OTR_ENTIDAD_FIN  = ? ");
            stringBuilder.append(" AND OTR_NUM_CUSTODIO = ? ");
            stringBuilder.append(" AND OTR_NUM_MONEDA = ? ");

            if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 0) {
                FecValor = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaAño").toString()) + "-" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMes").toString() + "-01";

                stringBuilder.append(" AND DATE(substr(OTR_FEC_MOVTO,7,4) || '-' ||  substr(OTR_FEC_MOVTO,4,2) || '-' ||  substr(OTR_FEC_MOVTO,1,2)) >= ? ");
            }
            stringBuilder.append(" ORDER BY OTR_ROW_ID ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setString(1, Nom_file);
            preparedStatement.setInt(2, Integer.parseInt(Num_Entidad_Financ));
            preparedStatement.setInt(3, Num_Custodio);
            preparedStatement.setInt(4, Id_Moneda);

            if (Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAbierto").toString()) == 0) {
                java.sql.Date FechaSql = ConvertirFechayyyyMMdd(FecValor);

                preparedStatement.setDate(5, FechaSql);
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    ArchivoOtrosBean detalleArchivoBean = new ArchivoOtrosBean();

                    detalleArchivoBean.setId(resultSet.getInt("OTR_ROW_ID"));
                    detalleArchivoBean.setFec_Movto(resultSet.getString("OTR_FEC_MOVTO"));
                    detalleArchivoBean.setId_Moneda(resultSet.getInt("OTR_NUM_MONEDA"));
                    detalleArchivoBean.setCve_Operacion(resultSet.getString("OTR_CVE_OPERACION"));
                    detalleArchivoBean.setTipo_Valor(resultSet.getString("OTR_TIPO_VALOR"));
                    detalleArchivoBean.setEstatus(resultSet.getString("OTR_ESTATUS"));
                    detalleArchivoBean.setDesc_Error(resultSet.getString("OTR_DESC_ERROR"));
                    detalleArchivoBean.setPizarra(resultSet.getString("OTR_PIZARRA"));
                    detalleArchivoBean.setSerie(resultSet.getString("OTR_SERIE"));
                    detalleArchivoBean.setCupon(resultSet.getString("OTR_CUPON"));
                    if (resultSet.getString("OTR_TITULOS") != null) {
                        detalleArchivoBean.setTitulos(formatFormatTit(resultSet.getDouble("OTR_TITULOS")));
                    } else {
                        detalleArchivoBean.setTitulos(resultSet.getString("OTR_TITULOS"));
                    }

                    if (resultSet.getString("OTR_PRECIO") != null) {
                        detalleArchivoBean.setPrecio(formatDecimalPre(resultSet.getDouble("OTR_PRECIO")));
                    } else {
                        detalleArchivoBean.setPrecio(resultSet.getString("OTR_PRECIO"));
                    }

                    if (resultSet.getString("OTR_IMPORTE") != null) {
                        detalleArchivoBean.setImporte(formatDecimalImp(resultSet.getDouble("OTR_IMPORTE")));
                    } else {
                        detalleArchivoBean.setImporte(resultSet.getString("OTR_IMPORTE"));
                    }

                    if (resultSet.getString("OTR_TASA_REND") != null) {
                        detalleArchivoBean.setTasa(formatDecimalTasa(resultSet.getDouble("OTR_TASA_REND")));
                    } else {
                        detalleArchivoBean.setTasa(resultSet.getString("OTR_TASA_REND"));
                    }

                    if (resultSet.getString("OTR_PLAZO") != null) {
                        detalleArchivoBean.setPlazo(formatFormatTit(resultSet.getDouble("OTR_PLAZO")));
                    } else {
                        detalleArchivoBean.setPlazo(resultSet.getString("OTR_PLAZO"));
                    }

                    if (resultSet.getString("OTR_PREMIO") != null) {
                        detalleArchivoBean.setPremio(formatDecimalPre(resultSet.getDouble("OTR_PREMIO")));
                    } else {
                        detalleArchivoBean.setPremio(resultSet.getString("OTR_PREMIO"));
                    }

                    if (resultSet.getString("OTR_COMISION") != null) {
                        detalleArchivoBean.setComision(formatDecimalPre(resultSet.getDouble("OTR_COMISION")));
                    } else {
                        detalleArchivoBean.setComision(resultSet.getString("OTR_COMISION"));
                    }

                    if (resultSet.getString("OTR_IVA") != null) {
                        detalleArchivoBean.setIVA(formatDecimalPre(resultSet.getDouble("OTR_IVA")));
                    } else {
                        detalleArchivoBean.setIVA(resultSet.getString("OTR_IVA"));
                    }

                    if (resultSet.getString("OTR_ISR") != null) {
                        detalleArchivoBean.setISR(formatDecimalPre(resultSet.getDouble("OTR_ISR")));
                    } else {
                        detalleArchivoBean.setISR(resultSet.getString("OTR_ISR"));
                    }

                    if (resultSet.getString("OTR_TIPO_CAMBIO") != null) {
                        detalleArchivoBean.setTipo_Cambio(formatDecimalPre(resultSet.getDouble("OTR_TIPO_CAMBIO")));
                    } else {
                        detalleArchivoBean.setTipo_Cambio(resultSet.getString("OTR_TIPO_CAMBIO"));
                    }

                    detalleArchivoBean.setFec_Vence(resultSet.getString("OTR_FEC_VENCE"));
                    detalleArchivoBean.setNum_Banco(resultSet.getString("OTR_NUM_BANCO"));
                    detalleArchivoBean.setRedaccion(resultSet.getString("OTR_REDACCION"));

                    // Add_List
                    otrosIntermediarios.add(detalleArchivoBean);
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return otrosIntermediarios;
    }

    public OutParameterBean onTesoreria_AdministraClave(int lSec, String sClave, String sServicio, String sTipoOper) {

        OutParameterBean outParameterBean = new OutParameterBean();

        // SQL
        String sql = "{CALL DB2FIDUC.SP_INV_RECHAZO_CVE (?,?,?,?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, lSec);
            callableStatement.setString(2, sClave);
            callableStatement.setString(3, sServicio);
            callableStatement.setString(4, sTipoOper);
            callableStatement.setInt(5, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            // GET_OUT_PARAMETER
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            if (sCode != 0 || sMsErr.trim().length() > 0) {
                bitacoraBean.setDetalle("Error al Aplicar: " + sMsErr + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            } else {
                bitacoraBean.setDetalle("Operación aplicada correctamente ");
            }

            // SET_VALUES_BITACORA
            outParameterBean.setPsMsgErrOut(sMsErr);
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoOper.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_RECHAZOS_CLAVE".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    //Vencimiento de reportos
    public List<ConreporBean> onTesoreria_GetReportos(Date FechaCont) {
        //Objects
        List<ConreporBean> reportos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT CASE WHEN ERE_NUM_FOLIO_REPO=CRE_FOLIO_REPORTO THEN 'REPORTO' ELSE 'PAGARE' END TIPO, ");
            stringBuilder.append("CRE_NUM_CONTRATO, CRE_SUB_CONTRATO, CRE_ENTIDAD_FIN, CRE_CONTRATO_INTER, CRE_FOLIO_REPORTO, ");
            stringBuilder.append("CRE_FOLIO_OPERA, CRE_NUM_PLAZO, CRE_PJE_TASA_PACTA, CRE_IMP_REPORTO, CRE_PREMIO_REPORTO,");
            stringBuilder.append("date(rtrim(char(CRE_ANO_VENCIM))||'-'||rtrim(char(CRE_MES_VENCIM))||'-'||rtrim(char(CRE_DIA_VENCIM))) FechVto,");
            stringBuilder.append("date(rtrim(char(CRE_ANO_ALTA_REG))||'-'||rtrim(char(CRE_MES_ALTA_REG))||'-'||rtrim(char(CRE_DIA_ALTA_REG))) FechALt,");
            stringBuilder.append("cto_nom_contrato, MON_NOM_MONEDA, CPR_NUM_CUENTA ");
            stringBuilder.append("FROM SAF.CONREPOR ");
            stringBuilder.append("inner join SAF.VISTA_USUARIO on CRE_NUM_CONTRATO=cto_num_contrato AND USU_NUM_USUARIO = ? ");
            stringBuilder.append("inner join SAF.CONTINTE on CPR_NUM_CONTRATO=CRE_NUM_CONTRATO and CPR_SUB_CONTRATO=CRE_SUB_CONTRATO and CPR_ENTIDAD_FIN=CRE_ENTIDAD_FIN and ");
            stringBuilder.append("CPR_CONTRATO_INTER=CRE_CONTRATO_INTER ");
            stringBuilder.append("inner join SAF.MONEDAS on CRE_NUM_MONEDA=MON_NUM_PAIS ");
            stringBuilder.append("left outer join SAF.EMIREPO on ERE_NUM_FOLIO_REPO=CRE_FOLIO_REPORTO ");

            stringBuilder.append("where  date(rtrim(char(CRE_ANO_VENCIM))||'-'||rtrim(char(CRE_MES_VENCIM))||'-'||rtrim(char(CRE_DIA_VENCIM)))<= ? and ");
            stringBuilder.append("CRE_CVE_ST_CONREPO = ? ");
            stringBuilder.append("order by CRE_NUM_CONTRATO");

            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();;
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(this.dateFormat(FechaCont)));
            preparedStatement.setString(3, "ACTIVO");

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //Object
                    ConreporBean reportoBean = new ConreporBean();

                    //Set_Values
                    reportoBean.setFideicomiso(resultSet.getString("CRE_NUM_CONTRATO"));
                    reportoBean.setSubFiso(resultSet.getString("CRE_SUB_CONTRATO"));
                    reportoBean.setTipoReporto(resultSet.getString("TIPO"));
                    reportoBean.setNegocioReporto(resultSet.getString("cto_nom_contrato"));
                    reportoBean.setIntermediarioReporto(resultSet.getInt("CRE_ENTIDAD_FIN"));
                    reportoBean.setCtoIntervencion(resultSet.getString("CRE_CONTRATO_INTER"));
                    reportoBean.setCuentaCheques(resultSet.getString("CRE_FOLIO_OPERA"));
                    reportoBean.setFechaInicio(resultSet.getDate("FechALt"));
                    reportoBean.setFechaVencimiento(resultSet.getDate("FechVto"));
                    reportoBean.setTasa(String.valueOf(resultSet.getInt("CRE_PJE_TASA_PACTA")));
                    reportoBean.setPlazo(String.valueOf(resultSet.getInt("CRE_NUM_PLAZO")));
                    reportoBean.setImporte(String.valueOf("$".concat(formatDecimal2D(resultSet.getDouble("CRE_IMP_REPORTO")))));
                    reportoBean.setPremio(String.valueOf("$".concat(formatDecimal2D(resultSet.getDouble("CRE_PREMIO_REPORTO")))));
                    reportoBean.setNomMoneda(resultSet.getString("MON_NOM_MONEDA"));
                    reportoBean.setNumeroCuenta(resultSet.getString("CPR_NUM_CUENTA"));
                    reportoBean.setFolioReporto(resultSet.getInt("CRE_FOLIO_REPORTO"));

                    //Add_List
                    reportos.add(reportoBean);
                }
            }
            conexion.close();
            //resultSet.close();
            preparedStatement.close();
        } catch (SQLException | ParseException Err) {
            logger.error("Ocurrio un error al parsear un valor obtenido en la tabla de CONREPORT");
        } finally {
            onCierraConexion();
        }

        return reportos;
    }

    public OutParameterBean onTesoreria_NewValAmortizaReporto(ConreporBean reporto, String sTipoOper) {

        //Objects
        OutParameterBean outParameterBean = new OutParameterBean();

        // SQL
        String sql = "{CALL DB2FIDUC.SPN_AMORTIZA_REPOR(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setDate(1, java.sql.Date.valueOf(this.dateFormat(reporto.getFechaVencimiento())));
            callableStatement.setInt(2, reporto.getFolioReporto());
            callableStatement.setInt(3, Integer.parseInt(reporto.getFideicomiso()));
            callableStatement.setInt(4, Integer.parseInt(reporto.getSubFiso()));
            callableStatement.setInt(5, reporto.getIntermediarioReporto());
            callableStatement.setString(6, reporto.getCtoIntervencion());
            callableStatement.setInt(7, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            callableStatement.setString(8, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("bEjecuto", Types.SMALLINT);
            callableStatement.registerOutParameter("iFolio_Conta", Types.INTEGER);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            callableStatement.execute();

            //GET_OUT_PARAMETER
            int iNumFolio = callableStatement.getInt("iFolio_Conta");
            int bEjecuto = callableStatement.getInt("bEjecuto");
            String psMsgErrOut = callableStatement.getString("PS_MSGERR_OUT");
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sEstado = callableStatement.getString("PCH_SQLSTATE_OUT");

            //Set_Values
            outParameterBean.setiNumFolioContab(iNumFolio);
            outParameterBean.setbEjecuto(bEjecuto);
            outParameterBean.setiNumFolioContab(iNumFolio);
            outParameterBean.setPsMsgErrOut(psMsgErrOut);

            //CLOSE_OBJECT_CALLABLESTATEMENT
            callableStatement.close();

            if (iNumFolio > 0 && psMsgErrOut.equals("")) {
                outParameterBean.setiNumFolioContab(iNumFolio);
                bitacoraBean.setDetalle("Operación con el folio: " + iNumFolio);
            } else {
                outParameterBean.setPsMsgErrOut(psMsgErrOut);
                bitacoraBean.setDetalle("Error al Aplicar: " + psMsgErrOut + ", SqlCode: " + sCode + " SqlState: " + sEstado);
            }

            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre(sTipoOper.toUpperCase());
            bitacoraBean.setFuncion("TESORERIA_VENCIMIENTO_REPORTOS".toUpperCase());

            onCierraConexion();

            // INSERT_BITACORA
            onTesoreria_InsertBitacora(bitacoraBean);

        } catch (SQLException | ParseException exception) {
            logger.error(exception.getMessage() + " en " + bitacoraBean.getFuncion());
        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public int onTesoreria_FunnChecFechVal(Date FechaContable, Date FechaMov) {

        // Objects 
        int iResultado = 0;

        // SQL
        String sql = "SELECT SAF.FUNN_CHEC_FECHVAL(?,?) AS FECVALOR FROM SAF.FECCONT";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(sql);

            // REGISTER_IN_PARAMETER
            preparedStatement.setDate(1, java.sql.Date.valueOf(this.dateFormat(FechaContable)));
            preparedStatement.setDate(2, java.sql.Date.valueOf(this.dateFormat(FechaMov)));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_PlanBean
                while (resultSet.next()) {
                    //GET_OUT_PARAMETER
                    iResultado = resultSet.getInt("FECVALOR");
                }
            }
            onCierraConexion();
        } catch (SQLException | ParseException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return iResultado;
    }

    public void onCierraConexion() {
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
        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar CallableStatement");
            }
        }
    }

    private synchronized Date formatDate(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }

    private synchronized String dateFormat(Date date) throws ParseException {
        return simpleDateFormat.format(date);
    }

    private synchronized Date dateParse(String date) throws ParseException {
        return parseFormat.parse(date);
    }

    private synchronized String formatFormatTit(Double decimal) {
        return decimalFormatTit.format(decimal);
    }

    private synchronized String formatDecimalImp(Double decimal) {
        return decimalFormatImp.format(decimal);
    }

    private synchronized String formatDecimalTasa(Double decimal) {
        return decimalFormatTasa.format(decimal);
    }

    private synchronized String formatDecimalTP(Double decimal) {
        return decimalFormatTP.format(decimal);
    }

    private synchronized String formatDecimalPre(Double decimal) {
        return decimalFormatPre.format(decimal);
    }

    private synchronized String formatDecimal2D(Double decimal) {
        return formatoDecimal2D.format(decimal);
    }

}
