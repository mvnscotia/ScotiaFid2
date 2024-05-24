/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : CContabilidad.java
 * TIPO        : Clase
 * PAQUETE     : scotiaFid.dao
 * CREADO      : 20210310
 * MODIFICADO  : 20211013
 * AUTOR       : smartBuilder 
 * NOTAS       : 20211013.- Se imnplementa consulta de saldos promedio
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.MonLiqInstrucFidecomisosBean;
import scotiaFid.bean.MonLiqInstrucTercerosBean;
import scotiaFid.bean.MonetariaLiquidacionBean;
import scotiaFid.bean.SPN_PROC_LOTE_LIQ_Bean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

public class CMonetariasLiquidacion {

    private static final Logger logger = LogManager.getLogger(CMonetariasLiquidacion.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private Integer sqlParam;
    private String sqlComando;
    private String nombreObjeto;
    private String mensajeErrorSP;

    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private DecimalFormat formatoDecimal2D;
    private SimpleDateFormat formatoFecha;
    private Calendar calendario;

    private Short archivoLineaNum;
    private String archivoLinea;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String pMess_Err;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    //efp test
    public String getpMess_Err() {
        return pMess_Err;
    }

    public void setpMess_Err(String pMess_Err) {
        this.pMess_Err = pMess_Err;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CMonetariasLiquidacion() {
        LogsContext.FormatoNormativo();
        formatoDecimal2D = new DecimalFormat("###,###.##");
        formatoDecimal2D.setMaximumFractionDigits(2);
        formatoDecimal2D.setMinimumFractionDigits(2);
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CMonetariasLiquidacion.";
        valorRetorno = Boolean.FALSE;
        calendario = Calendar.getInstance();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   M O N E T A R I A S   L I Q U I D A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<String> getListError(Integer usuarioNumero) {
        List<String> listError = new ArrayList<>();
        try {
            sqlComando = "select CARINT_MENSAJE FROM  CARGA_INTERFAZ     \n"
                        + "where  CARINT_NUM_USUARIO = ?\n"
                        + "and    RUT_ID_RUTINA = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setInt(1, usuarioNumero);
            pstmt.setString(2, "PAGO MASIVO"); 
            rs = pstmt.executeQuery();  

            while (rs.next()) {
                String msj = rs.getString("CARINT_MENSAJE").replace("|", ";");
                String lista [] = msj.split(";");
                for(int ind = 0;ind< lista.length;ind++){
                    listError.add(lista[ind]);                
                }
                //listError.add(rs.getString("CARINT_MENSAJE"));
            }
            rs.close();

            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "getListError()";
            logger.error(Err.getMessage() + "getListError()");
        } finally {

            onCierraConexion();
        }
        return listError;
    }
    public List<MonLiqInstrucFidecomisosBean> getListMonLiqInstrucFideicomisos(MonetariaLiquidacionBean formMonetariasLiq) throws SQLException {
        List<MonLiqInstrucFidecomisosBean> listMonLiqInstrucFidecomisos = new ArrayList<>();
        try {
            sqlComando = "SELECT PBX_BENEFICIARIO   \n"
                    + "	 ,RTRIM(LTRIM(REPLACE(BEN_NOM_BENEF,CHR(9),' '))) AS BEN_NOM_BENEF\n"
                    + "	 ,PBX_IMP_FIJO_PAGO \n"
                    + "	 ,PBX_CVE_PER_PAGO  \n"
                    + "	 ,PBX_DIA_INI_PAGO  \n"
                    + "	 ,PBX_MES_INI_PAGO  \n"
                    + "	 ,PBX_ANO_INI_PAGO  \n"
                    + "	 ,ML_NUM_MONEDA     \n"
                    + "	 ,ML_FORMA_LIQ      \n"
                    + "	 ,ML_NUM_CTA        \n"
                    + "	 ,PBX_CTA_CHEQUES   \n"
                    + "	 ,MON_NOM_MONEDA    \n"
                    + "	 ,PBX_CVE_ST_PARPABE\n"
                    + "   FROM BENEFICI,PARPABEX\n"
                    + "  INNER JOIN FID_CTAS ON ADC_NUM_CONTRATO=PBX_NUM_CONTRATO AND ADC_SUB_CONTRATO=PBX_SUB_CONTRATO AND ADC_BANCO = 44 AND ADC_CUENTA=PBX_CTA_CHEQUES\n"
                    + "  INNER JOIN CLAVES 	ON CVE_NUM_CLAVE = 27  AND CVE_NUM_SEC_CLAVE=ADC_BANCO\n"
                    + "  INNER JOIN MONEDAS 	ON MON_NUM_PAIS=ADC_NUM_MONEDA \n"
                    + "   LEFT OUTER JOIN FDMASIVOLIQ ON PBX_NUM_FOLIO_INST = ML_NUM_FOLIO_INST AND PBX_SUB_CONTRATO   = ML_SUB_CONTRATO AND \n"
                    + "                   PBX_SUB_PROGRAMA = ML_SUB_PROGRAMA AND PBX_NUM_CONTRATO = ML_NUM_CONTRATO AND PBX_BENEFICIARIO = ML_NUM_PERSONA AND\n"
                    + "                   PBX_SEC_PAGO     = ML_SEC_PAGO 	AND ML_CVE_PERSONA   = 'FIDEICOMISARIO' \n"
                    + "  WHERE PBX_NUM_CONTRATO = ? AND PBX_NUM_FOLIO_INST = ? AND PBX_SUB_CONTRATO = ? AND    \n"
                    + "        PBX_CVE_ST_PARPABE IN ('POR AUTORIZAR','PENDIENTE') 	AND BEN_NUM_CONTRATO > 0 AND PBX_NUM_CONTRATO = BEN_NUM_CONTRATO AND\n"
                    + "        PBX_BENEFICIARIO = BEN_BENEFICIARIO\n"
                    + "  ORDER BY PBX_ANO_INI_PAGO,PBX_MES_INI_PAGO,PBX_DIA_INI_PAGO, PBX_BENEFICIARIO";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, formMonetariasLiq.getMonetariasLiqInsNumContrato());
            pstmt.setInt(2, formMonetariasLiq.getMonetariasLiqInsNumFolioInst());
            pstmt.setInt(3, formMonetariasLiq.getMonetariasLiqInsSubContrato());
            rs = pstmt.executeQuery();

            while (rs.next()) {

                MonLiqInstrucFidecomisosBean monLiqInstrucFide = new MonLiqInstrucFidecomisosBean();

                monLiqInstrucFide.setMonLiqPbxBeneficiario(rs.getLong("PBX_BENEFICIARIO"));
                monLiqInstrucFide.setMonLiqBenNomBenef(rs.getString("BEN_NOM_BENEF"));
                monLiqInstrucFide.setMonLiqPbxImpFijoPago(rs.getDouble("PBX_IMP_FIJO_PAGO"));
                monLiqInstrucFide.setMonLiqPbxCvePerPago(rs.getString("PBX_CVE_PER_PAGO"));
                monLiqInstrucFide.setMonLiqPbxDiaIniPago(rs.getInt("PBX_DIA_INI_PAGO"));
                monLiqInstrucFide.setMonLiqPbxMesIniPago(rs.getInt("PBX_MES_INI_PAGO"));
                monLiqInstrucFide.setMonLiqPbxAnoIniPago(rs.getInt("PBX_ANO_INI_PAGO"));
                monLiqInstrucFide.setMonLiqMlNumMoneda(rs.getInt("ML_NUM_MONEDA"));
                monLiqInstrucFide.setMonLiqMlFormaLiq(rs.getInt("ML_FORMA_LIQ"));
//                monLiqInstrucFide.setMonLiqMlNumCta(formatDecimal2D(rs.getDouble("ML_NUM_CTA")));
                monLiqInstrucFide.setMonLiqMlNumCta(rs.getString("ML_NUM_CTA"));
                monLiqInstrucFide.setMonLiqPbxCtaCheques(rs.getString("PBX_CTA_CHEQUES"));
                monLiqInstrucFide.setMonLiqMonNomMoneda(rs.getString("MON_NOM_MONEDA"));
                monLiqInstrucFide.setMonLiqPbxCveStParpabe(rs.getString("PBX_CVE_ST_PARPABE"));

                listMonLiqInstrucFidecomisos.add(monLiqInstrucFide);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "getListMonLiqInstrucFideicomisos()";
            logger.error(Err.getMessage() + "getListMonLiqInstrucFideicomisos()");
        } finally {

            onCierraConexion();
        }
        return listMonLiqInstrucFidecomisos;
    }

    public List<MonLiqInstrucTercerosBean> getListMonLiqInstrucTerceros(MonetariaLiquidacionBean formMonetariasLiq) throws SQLException {
        List<MonLiqInstrucTercerosBean> listMonLiqInstrucTerceros = new ArrayList<>();
        try {

            sqlComando = "SELECT PTX_NUM_TERCERO,PTX_IMP_FIJO_PAGO,PTX_CVE_PERIO_PAGO,PTX_DIA_INI_PAG_EX,\n"
                    + "PTX_MES_INI_PAG_EX,PTX_ANO_INI_PAG_EX,\n"
                    + "rtrim(ltrim(replace(TER_NOM_TERCERO,chr(9),' '))) as TER_NOM_TERCERO,PTX_CTA_CHEQUES,\n"
                    + "MON_NOM_MONEDA,PTX_CVE_ST_PARPATE,ML_FORMA_LIQ,ML_NUM_CTA ,PTX_NUM_FOLIO_INST FOLIO_INST \n"
                    + "FROM terceros,parpatex\n"
                    + " inner join FID_CTAS On ADC_NUM_CONTRATO=ptx_num_contrato and ADC_SUB_CONTRATO=ptx_sub_contrato and \n"
                    + "                        ADC_BANCO =44 and ADC_CUENTA=PTX_CTA_CHEQUES\n"
                    + "  INNER JOIN CLAVES On CVE_NUM_CLAVE = 27 and CVE_NUM_SEC_CLAVE=ADC_BANCO\n"
                    + " inner join MONEDAS ON MON_NUM_PAIS=ADC_NUM_MONEDA\n"
                    + " LEFT OUTER JOIN FDMASIVOLIQ ON ptx_num_folio_inst = ML_NUM_FOLIO_INST and\n"
                    + "                                ptx_sub_contrato=ML_SUB_CONTRATO AND ptx_sub_programa= ML_SUB_PROGRAMA AND\n"
                    + " ptx_num_contrato= ML_NUM_CONTRATO AND PTX_NUM_TERCERO= ML_NUM_PERSONA AND PTX_SEC_PAGO= ML_SEC_PAGO AND\n"
                    + " ML_CVE_PERSONA = 'TERCERO' \n"
                    + "WHERE ter_num_contrato>0 AND ptx_num_contrato = ? and ptx_num_folio_inst = ? and\n"
                    + "      ptx_sub_contrato = ? and ptx_cve_st_parpate in ('POR AUTORIZAR','PENDIENTE')  and\n"
                    + "      ptx_num_contrato=ter_num_contrato  and PTX_NUM_TERCERO=ter_num_tercero\n"
                    + "ORDER BY ptx_ano_ini_pag_ex,ptx_mes_ini_pag_ex,ptx_dia_ini_pag_ex, PTX_NUM_TERCERO";
            
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, formMonetariasLiq.getMonetariasLiqInsNumContrato());
            pstmt.setInt(2, formMonetariasLiq.getMonetariasLiqInsNumFolioInst());
            pstmt.setInt(3, formMonetariasLiq.getMonetariasLiqInsSubContrato());
            rs = pstmt.executeQuery();

            while (rs.next()) { 

                MonLiqInstrucTercerosBean monLiqInstrucTerceros = new MonLiqInstrucTercerosBean();
                monLiqInstrucTerceros.setMonLiqPtxNumTercero(rs.getLong("PTX_NUM_TERCERO"));
                monLiqInstrucTerceros.setMonLiqPtxImpFijoPago(rs.getDouble("PTX_IMP_FIJO_PAGO"));
                monLiqInstrucTerceros.setMonLiqPtxCvePerioPago(rs.getString("PTX_CVE_PERIO_PAGO"));
                monLiqInstrucTerceros.setMonLiqPtxDiaIniPagEx(rs.getInt("PTX_DIA_INI_PAG_EX"));
                monLiqInstrucTerceros.setMonLiqPtxMesIniPagEx(rs.getInt("PTX_MES_INI_PAG_EX"));
                monLiqInstrucTerceros.setMonLiqPtxAnoIniPagEx(rs.getInt("PTX_ANO_INI_PAG_EX"));
                monLiqInstrucTerceros.setMonLiqTerNomTercero(rs.getString("TER_NOM_TERCERO"));
                monLiqInstrucTerceros.setMonLiqPtxCtaCheques(rs.getString("PTX_CTA_CHEQUES"));
                monLiqInstrucTerceros.setMonLiqMonNomMoneda(rs.getString("MON_NOM_MONEDA"));
                monLiqInstrucTerceros.setMonLiqPtxCveStParpate(rs.getString("PTX_CVE_ST_PARPATE"));
                monLiqInstrucTerceros.setMonLiqMlFormaLiq(rs.getInt("ML_FORMA_LIQ"));
                monLiqInstrucTerceros.setMonLiqMlNumCta(rs.getLong("ML_NUM_CTA"));
                monLiqInstrucTerceros.setMonLiqFolioInst(rs.getInt("FOLIO_INST"));

                listMonLiqInstrucTerceros.add(monLiqInstrucTerceros);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            logger.error(Err.getMessage() + "getListMonLiqInstrucFideicomisos()");
        } finally {

            onCierraConexion();
        }
        return listMonLiqInstrucTerceros;
    }

    public Double getSaldoActualCuentaOrigen(MonetariaLiquidacionBean formMonetariasLiq) throws SQLException {
        Double result = 0.00;
        try {
            sqlComando = "SELECT SALD_SALDO_ACTUAL  \n"
                    + "  FROM FDSALDOS           \n"
                    + " WHERE CCON_CTA   = 1103  \n"
                    + "   AND CCON_SCTA  = 1     \n"
                    + "   AND CCON_2SCTA = 2     \n"
                    + "   AND CCON_3SCTA = 1     \n"
                    + "   AND SALD_AX1       = ? \n"
                    + "   AND SALD_AX2       = ? \n"
                    + "   AND MONE_ID_MONEDA = ? \n"
                    + "   AND SALD_AX3       = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, formMonetariasLiq.getMonetariasLiqInsNumContrato());
            pstmt.setInt(2, formMonetariasLiq.getMonetariasLiqInsSubContrato());
            pstmt.setInt(3, formMonetariasLiq.getMonetariasLiqInsAdcNumMoneda());
            Long monetariasLiqInsAdcCuenta = new Long(formMonetariasLiq.getMonetariasLiqInsAdcCuenta());
            pstmt.setLong(4, monetariasLiqInsAdcCuenta);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                result = rs.getDouble("SALD_SALDO_ACTUAL");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "getSaldoActualCuentaOrigen()";
            logger.error(Err.getMessage() + "getSaldoActualCuentaOrigen()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public Map<String, String> onMonetariasLiquidacion_ValidarAchivoCargado(String archivoNombre) throws SQLException {
        Map<String, String> result = new HashMap<>();
        try {
            sqlComando = "SELECT ARL_USUARIO, ARL_FECHA  FROM SAF.ARCHLIQ WHERE ARL_NOM_ARCH = UPPER(?)";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, archivoNombre);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                result.put("ARL_USUARIO", rs.getString("ARL_USUARIO"));
                String ARL_FECHA = formatFecha(rs.getDate("ARL_FECHA"));
                result.put("ARL_FECHA", ARL_FECHA);
            }

            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_ValidarAchivoCargado()";
            logger.error(Err.getMessage() + "onMonetariasLiquidacion_ValidarAchivoCargado()");
            valorRetorno = Boolean.FALSE;
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public Boolean onMonetariasLiquidacion_RegistrarAchivoCargado(String archivoNombre, Integer usuarioNumero) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;
            /**
             * ********************************************************************************************************
             * Inserta documento relacionado al expediente
            **********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "INSERT INTO ARCHLIQ(ARL_NOM_ARCH, ARL_USUARIO, ARL_FECHA ) VALUES (UPPER(?), ?, CURRENT_TIMESTAMP)";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setString(1, archivoNombre);
            pstmt.setInt(2, usuarioNumero);
            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_GrabaExpedocs()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabaExpedocs()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }
    public Boolean onMonetariasLiquidacion_BorrarCargaAnterior(Integer usuarioNumero) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;
            /**
             * ********************************************************************************************************
             * Inserta documento relacionado al expediente
            **********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "DELETE FROM  CARGA_INTERFAZ     \n"
                        + "where  CARINT_NUM_USUARIO = ?\n"
                        + "and    RUT_ID_RUTINA = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setInt(1, usuarioNumero);
            pstmt.setString(2, "PAGO MASIVO");
            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_GrabaExpedocs()";
            logger.error(Err.getMessage() + "onMonetariasLiquidacion_BorrarCargaAnterior()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onMonetariasLiquidacion_CargarTablaTemporalCargaInterfaz(String[] arrArchivo, Integer usuarioNumero, String archivoNombre) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;
            /**
             * ********************************************************************************************************
             * Inserta documento relacionado al expediente
            **********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = this.onMonetariasLiquidacion_Insert_Para_Carga_Interfaz(arrArchivo, usuarioNumero, archivoNombre);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (ParseException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMonetariasLiquidacion_CargarTablaTemporalCargaInterfaz()";
            logger.error(Err.getMessage() + "onMonetariasLiquidacion_CargarTablaTemporalCargaInterfaz()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    private String onMonetariasLiquidacion_Insert_Para_Carga_Interfaz(String[] arrArchivo, Integer usuarioNumero, String archivoNombre) throws ParseException {
        /// List<String> lista00 = new ArrayList<>();
        //List<String> lista01 = new ArrayList<>();
        //List<String> lista02 = new ArrayList<>();
        //String       regEx00 = "\\d{1,2}/\\d{1,2}/\\d{4}";
        //String       regEx01 = "^[0-9.]{1,20}$";
        //String       regEx02 = "^[0-9]{1,10}$"; 

        StringBuilder stringBuilder = new StringBuilder();
        Date fechSistema = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());

        try {
            if (arrArchivo.length > 0) {
                archivoLineaNum = 1;

                stringBuilder.append("INSERT INTO CARGA_INTERFAZ     \n"
                        + "        (CARINT_NUM_USUARIO,\n"
                        + /*USUARIO DE LA SESION*/ "         RUT_ID_RUTINA,     \n"
                        + /*CONSTANTE 'PAGO MASIVO'*/ "         CARINT_FECHA,      \n"
                        + /*FECHA CONTABLE*/ "         CARINT_SEC_ARCH,   \n"
                        + /*CONSTANTE 1*/ "         CARINT_SECUENCIAL, \n"
                        + /*NUMERO DE REGISTRO O FILA DEL ARCHIVO*/ "         CARINT_NOM_PATH,   \n"
                        + /*constante ''*/ "         CARINT_NOM_ARCH,   \n"
                        + /*NOMBRE DEL ARCHIVO*/ "         CARINT_ARCH_TMP,   \n"
                        + /*CONSTANTE N/A*/ "         CARINT_CADENA,     \n"
                        + /*fila del archivo*/ "         CARINT_ESTATUS,    \n"
                        + /*'C' si es correcto 'E' si es error*/ "         CARINT_MENSAJE)    \n"
                        + /*si estatus  = 'E'  MensajeVal-- el menasje que se arma en la rutina de validación*/ " VALUES \n");
//                    String secArch = archivoNombre.substring(12, 15);
                for (short itemU = 0; itemU <= arrArchivo.length; itemU++) {
                    if (arrArchivo[itemU] != null) {
                        archivoLinea = arrArchivo[itemU];
                        String rowQuery = "(" + usuarioNumero + ",'PAGO MASIVO','" + fechSistema + "',1,'" + archivoLineaNum + "',' ','" + archivoNombre + "','N/A','" + archivoLinea + "','C',' ') \n";
                        if (itemU > 0) {
                            rowQuery = ",".concat(rowQuery);
                        }
                        /*
                            if(itemU == arrArchivo.length - 1) {
                                rowQuery = "(" + usuarioNumero + ",'PAGO MASIVO','" + fechSistema + "',1,'" + archivoLineaNum + "',' ','" + archivoNombre + "','N/A','" + archivoLinea + "','C',' ')";
                            }
                         */
                        stringBuilder.append(rowQuery);

                    } else {
                        break;
                    }
                    archivoLineaNum++;
                }
            } else {
                mensajeError = "Error, no se cargó el archivo para su validación";
            }
        } catch (NullPointerException Err) {
            logger.error("NullPointerException CargaUnidadesValida()");
        }
        return stringBuilder.toString();
    }

    public Map<String, String> onEjecutarLiqMasiva_SPN_PROC_LOTE_LIQ(SPN_PROC_LOTE_LIQ_Bean spnProcLoteLiqBean) {
        Map<String, String> responseSPProcesa = new HashMap<>();
        try {

            sqlComando = "{call DB2FIDUC.SPN_PROC_LOTE_LIQ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("IUSUARIO", spnProcLoteLiqBean.getIUSUARIO());
            cstmt.setLong("INUMCONTRATO", spnProcLoteLiqBean.getINUMCONTRATO());
            cstmt.setInt("ISUBCONTRATO", spnProcLoteLiqBean.getISUBCONTRATO());
            cstmt.setInt("ICPTOPAGO", spnProcLoteLiqBean.getICPTOPAGO());
            cstmt.setString("SNOMPROCESO", spnProcLoteLiqBean.getSNOMPROCESO());
            cstmt.setDate("DTFECHA", spnProcLoteLiqBean.getDTFECHA());
            cstmt.setString("SNOMARCHIVO", spnProcLoteLiqBean.getSNOMARCHIVO());
            cstmt.setString("STIPOPERSONA", spnProcLoteLiqBean.getSTIPOPERSONA());
            cstmt.setLong("IFOLIOINSTR", spnProcLoteLiqBean.getIFOLIOINSTR());
            cstmt.setDouble("DVALORLOTE", spnProcLoteLiqBean.getDVALORLOTE());
            cstmt.setInt("IMONEDA", spnProcLoteLiqBean.getIMONEDA());
            cstmt.setString("SCTAORIGEN", spnProcLoteLiqBean.getSCTAORIGEN());
            cstmt.setString("SIDFORMA", spnProcLoteLiqBean.getSIDFORMA());
            cstmt.setString("STERMINAL", spnProcLoteLiqBean.getSTERMINAL());

            cstmt.registerOutParameter("BEJECUTO", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("SCADENATOT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("IREGISTROS", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("ICORRECTOS", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("ICONERROR", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT");
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                responseSPProcesa.put("isEjecuto", String.valueOf(cstmt.getInt("BEJECUTO")));
            } else {
                responseSPProcesa.put("Error", mensajeErrorSP);
            }
            cstmt.close();
            conexion.close();

        } catch (SQLException Err) {
            //mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onEjecutarLiqMasiva_SPN_PROC_LOTE_LIQ()";
            logger.error(Err.getMessage() + "onEjecutarLiqMasiva_SPN_PROC_LOTE_LIQ()");
        } finally {
            onCierraConexion();
        }
        return responseSPProcesa;
    }

    private void onCierraConexion() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar ResultSet.");
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar PreparetStatement.");
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar CallableStatement.");
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar Connection.");
            }
        }
    }

    public synchronized String formatDecimal2D(double importe) {
        return formatoDecimal2D.format(importe);
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

}
