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
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.ContabilidadMonedaCortoBean;
import scotiaFid.bean.ContabilidadAsientoBean;
import scotiaFid.bean.ContabilidadBienFideBean;
import scotiaFid.bean.ContabilidadBienFideUnidadBean;
import scotiaFid.bean.ContabilidadBienFideUnidadIndivBean;
import scotiaFid.bean.ContabilidadBienFideUnidadIndivMasivoBean;
import scotiaFid.bean.ContabilidadBienFideUnidadLiqBean;
import scotiaFid.bean.ContabilidadCancelaSaldoBean;
import scotiaFid.bean.ContabilidadDetValorBean;
import scotiaFid.bean.ContabilidadDatoMovBean;
import scotiaFid.bean.ContabilidadGarantiaGralBean;
import scotiaFid.bean.ContabilidadGarantiaBienBean;
import scotiaFid.bean.ContabilidadOperacionBean;
import scotiaFid.bean.ContabilidadPolizaManBean;
import scotiaFid.bean.ContabilidadMovtoBean;
import scotiaFid.bean.ContabilidadSaldoBean;
import scotiaFid.bean.ContabilidadSaldoPromBean;
import scotiaFid.bean.CriterioBusquedaBean;
import scotiaFid.bean.CriterioBusquedaContaBean;
import scotiaFid.bean.CriterioBusquedaContaAsienBean;
import scotiaFid.bean.CriterioBusquedaContaSaldoBean;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.DetMasivoBean;
import scotiaFid.bean.GenericoConsultaBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.TPendientesBean;
import scotiaFid.bean.TPendientesDetBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;
import scotiaFid.util.CValidacionesUtil;
import scotiaFid.bean.BitacoraBean;

public class CContabilidad {
private static final Logger logger = LogManager.getLogger(CContabilidad.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean conEstructura;
    private Boolean valorRetorno;
    private Boolean indSimulados;
    private Integer sqlParam;
    //private String sqlComando;
    //private String sqlFiltro;
    private String nombreObjeto;
    private String mensajeErrorSP;
    private Double bienFideUnidadAcumRegContable;

    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private static DecimalFormat formatoDecimal0D;
    private static DecimalFormat formatoDecimal2D;
    private static SimpleDateFormat formatoFecha;

    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String mensajePoliza;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CContabilidad() {
        LogsContext.FormatoNormativo();
        formatoDecimal0D = new DecimalFormat("###,###");
        formatoDecimal0D.setMaximumFractionDigits(0);
        formatoDecimal0D.setMinimumFractionDigits(0);
        formatoDecimal2D = new DecimalFormat("###,###.##");
        formatoDecimal2D.setMaximumFractionDigits(2);
        formatoDecimal2D.setMinimumFractionDigits(2);
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CContabilidad.";
        valorRetorno = Boolean.FALSE;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C A N C E L A   S A L D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaBienesFideicomitidos(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT BIE_NUM_CONTRATO, BIE_SUB_CONTRATO, BIE_CVE_TIPO_BIEN, BIE_ID_BIEN, BIE_CVE_BIEN,\n"
                    + "       BIE_IMP_BIEN, BIE_IMP_ULT_VALUA, DESC_MONEDA, BIE_DESC_BIEN, BIE_CVE_ST_BIENFID  \n"
                    + "FROM   SAF.BIENFIDE                            \n"
                    + "JOIN  (SELECT CVE_NUM_SEC_CLAVE NUM_MONEDA,CVE_DESC_CLAVE DESC_MONEDA FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=2) MONEDAS \n"
                    + "ON     BIE_NUM_MONEDA     = MONEDAS.NUM_MONEDA \n"
                    + "WHERE  BIE_NUM_CONTRATO   = ?                  \n"
                    + "AND    BIE_CVE_ST_BIENFID = ? ";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("BIE_NUM_CONTRATO"));
                gc.setGenericoCampo01(rs.getString("BIE_SUB_CONTRATO"));
                gc.setGenericoCampo02(rs.getString("BIE_CVE_TIPO_BIEN"));
                gc.setGenericoCampo03(rs.getString("BIE_ID_BIEN"));
                gc.setGenericoCampo04(rs.getString("BIE_CVE_BIEN"));
                gc.setGenericoCampo05("$".concat(formatDecimal2D(rs.getDouble("BIE_IMP_BIEN"))));
                gc.setGenericoCampo06("$".concat(formatDecimal2D(rs.getDouble("BIE_IMP_ULT_VALUA"))));
                gc.setGenericoCampo07(rs.getString("DESC_MONEDA"));
                gc.setGenericoCampo08(rs.getString("BIE_DESC_BIEN"));
                gc.setGenericoCampo09(rs.getString("BIE_CVE_ST_BIENFID"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaBienesFideicomitidos()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaBienesGarantia(long contratoNumero)  {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT fideicomiso, subfiso, bien, tipo_bien, sec_bien, importe, imp_ult_valuacion, descripcion, desc_moneda moneda, estatus \n"
                    + "FROM  (SELECT 'MUEBLE' bien, grm_num_contrato fideicomiso, grm_sub_contrato subfiso, grm_cve_tipo_mueb tipo_bien, grm_num_bie_mueble sec_bien, grm_imp_valor importe, grm_imp_ult_valua imp_ult_valuacion, char(grm_texto_descrip) descripcion, grm_cve_st_mueble estatus \n"
                    + "       FROM   SAF.GARMUEBL           \n"
                    + "       WHERE  grm_num_contrato   = ? \n"
                    + "       AND    grm_cve_st_mueble  = ? \n"
                    + "       UNION                         \n"
                    + "       SELECT 'INMUEBLE' bien, gri_num_contrato fideicomiso, gri_sub_contrato subfiso, gri_cve_tipo_inmu tipo_bien, gri_num_inmueble sec_bien, gri_imp_inmueble importe, gri_imp_ult_valua imp_ult_valuacion, char(gri_texto_descrip) descripcion, gri_cve_status estatus \n"
                    + "       FROM   SAF.GRAINMU            \n"
                    + "       WHERE  gri_num_contrato   = ? \n"
                    + "       AND    gri_cve_status     = ? \n"
                    + "       UNION                         \n"
                    + "       SELECT 'OTROS BIENES' bien, grn_num_contrato fideicomiso, grn_sub_contrato subfiso, grn_cve_tipo_garan tipo_bien, grn_num_nobursatil sec_bien, grn_imp_documento importe, grn_imp_ult_valua imp_ult_valuacion, char(grn_texto_descrip) descripcion, grn_cve_st_nobursa estatus \n"
                    + "       FROM   SAF.GARNBURS           \n"
                    + "       WHERE  grn_num_contrato   = ? \n"
                    + "       AND    grn_cve_st_nobursa = ? \n"
                    + "       UNION                         \n"
                    + "       SELECT 'VALORES' bien, grv_num_contrato fideicomiso, grv_sub_contrato subfiso, grv_cve_tipo_garan tipo_bien, grv_num_valores sec_bien, grv_imp_libros importe, grv_imp_mercado imp_ult_valuacion, char(grv_texto_descrip) descripcion, grv_cve_st_valor estatus \n"
                    + "       FROM   SAF.GARVALOR           \n"
                    + "       WHERE  grv_num_contrato   = ? \n"
                    + "       AND    grv_cve_st_valor   = ?)bienes \n"
                    + "JOIN   SAF.GARA_TIPO_MONEDA            \n"
                    + "ON     fideicomiso = grn_num_contrato  \n"
                    + "AND    subfiso     = grn_sub_contrato  \n"
                    + "AND    bien        = gra_tipo_garantia \n"
                    + "AND    sec_bien    = gra_num_garantia  \n"
                    + "JOIN  (SELECT cve_num_sec_clave num_moneda, cve_desc_clave desc_moneda \n"
                    + "       FROM   SAF.Claves               \n"
                    + "       WHERE  cve_num_clave=?) monedas \n"
                    + "ON     gra_num_moneda = monedas.num_moneda";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            pstmt.setLong(3, contratoNumero);
            pstmt.setString(4, "ACTIVO");
            pstmt.setLong(5, contratoNumero);
            pstmt.setString(6, "ACTIVO");
            pstmt.setLong(7, contratoNumero);
            pstmt.setString(8, "ACTIVO");
            pstmt.setInt(9, 2);
            rs = pstmt.executeQuery();
            //System.out.println(sqlComando);
            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("fideicomiso"));
                gc.setGenericoCampo01(rs.getString("subfiso"));
                gc.setGenericoCampo02(rs.getString("bien"));
                gc.setGenericoCampo03(rs.getString("tipo_bien"));
                gc.setGenericoCampo04(rs.getString("sec_bien"));
                gc.setGenericoCampo05("$".concat(formatDecimal2D(rs.getDouble("importe"))));
                gc.setGenericoCampo06("$".concat(formatDecimal2D(rs.getDouble("imp_ult_valuacion"))));
                gc.setGenericoCampo07(rs.getString("descripcion"));
                gc.setGenericoCampo08(rs.getString("moneda"));
                gc.setGenericoCampo09(rs.getString("estatus"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaBienesGarantia()";
           logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaCtaCheques(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT adc_num_contrato, adc_sub_contrato, adc_tipocta, adc_cuenta,    \n"
                    + "       adc_imp_total, banco.nom_corto bco, monedas.desc_moneda mda,adc_estatus \n"
                    + "FROM   SAF.Fid_Ctas                          \n "
                    + "JOIN  (SELECT cve_num_sec_clave num_banco,   \n"
                    + "              cve_desc_clave desc_banco,     \n"
                    + "              cve_forma_emp_cve nom_corto    \n"
                    + "       FROM   SAF.Claves                     \n"
                    + "       WHERE  cve_num_clave = 27) banco      \n"
                    + "ON     adc_banco = banco.num_banco           \n"
                    + "JOIN  (SELECT cve_num_sec_clave num_moneda,  \n"
                    + "              cve_desc_clave desc_moneda     \n"
                    + "       FROM   SAF.Claves                     \n"
                    + "       WHERE  cve_num_clave = 2) monedas     \n"
                    + "ON     adc_num_moneda   = monedas.num_moneda \n"
                    + "WHERE  adc_num_contrato = ?                  \n"
                    + "AND    adc_estatus      = ?                  \n"
                    + "AND    adc_imp_total    <>?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            pstmt.setInt(3, 0);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("adc_num_contrato"));
                gc.setGenericoCampo01(rs.getString("adc_sub_contrato"));
                gc.setGenericoCampo02(rs.getString("adc_tipocta"));
                gc.setGenericoCampo03(rs.getString("adc_cuenta"));
                gc.setGenericoCampo04("$".concat(formatDecimal2D(rs.getDouble("adc_imp_total"))));
                gc.setGenericoCampo05(rs.getString("bco"));
                gc.setGenericoCampo06(rs.getString("mda"));
                gc.setGenericoCampo07(rs.getString("adc_estatus"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaCtaCheques()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaCtoInv(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        Date dPaso;
        try {
            String sqlComando = "SELECT CPR_NUM_CONTRATO, CPR_SUB_CONTRATO, CPR_CONTRATO_INTER, CPR_NOM_INTERMED, DESC_MONEDA MONEDA, CPR_CVE_ST_CONTINT,     \n"
                    + "       DATE(RTRIM(CHAR(CPR_ANO_APERTURA))||'-'||RTRIM(CHAR(CPR_MES_APERTURA))||'-'||RTRIM(CHAR(CPR_DIA_APERTURA))) APERTURA, \n"
                    + "       DATE(RTRIM(CHAR(CPR_ANO_VENCIM))||'-'||RTRIM(CHAR(CPR_MES_VENCIM))||'-'||RTRIM(CHAR(CPR_DIA_VENCIM))) VENCIMIENTO \n"
                    + "FROM   SAF.CONTINTE   \n" 
                    + "JOIN  (SELECT CVE_NUM_SEC_CLAVE NUM_MONEDA,CVE_DESC_CLAVE DESC_MONEDA FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=2) MONEDAS \n"
                    + "ON     CPR_MONEDA_CTA  = MONEDAS.NUM_MONEDA \n"
                    + "WHERE  CPR_NUM_CONTRATO   = ? \n"
                    + "AND    CPR_CVE_ST_CONTINT = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("CPR_NUM_CONTRATO"));
                gc.setGenericoCampo01(rs.getString("CPR_SUB_CONTRATO"));
                gc.setGenericoCampo02(rs.getString("CPR_CONTRATO_INTER"));
                gc.setGenericoCampo03(rs.getString("CPR_NOM_INTERMED"));
                gc.setGenericoCampo04(rs.getString("MONEDA"));
                gc.setGenericoCampo05(formatFecha(rs.getDate("APERTURA")));
                gc.setGenericoCampo06(rs.getString("CPR_CVE_ST_CONTINT"));
                if (rs.getDate("VENCIMIENTO") != null){
                
                gc.setGenericoCampo07(formatFecha(rs.getDate("VENCIMIENTO")));}
                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaCtoInv()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaFianzas(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT cto_num_contrato, fia_num_fianza, fia_num_folio, fia_titular_fianza, fia_imp_fianza,\n"
                    + "       desc_moneda, fia_afianzadora, fia_nom_agente, fia_status        \n"
                    + "FROM   SAF.Fianzas                          \n"
                    + "JOIN  (SELECT cve_num_sec_clave num_moneda, cve_desc_clave desc_moneda \n"
                    + "       FROM   SAF.Claves                    \n"
                    + "       WHERE  cve_num_clave=2) Monedas      \n"
                    + "ON     mone_id_moneda  = monedas.num_moneda \n"
                    + "WHERE  cto_num_contrato= ?                  \n"
                    + "AND    fia_status      = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("cto_num_contrato"));
                gc.setGenericoCampo01(rs.getString("fia_num_fianza"));
                gc.setGenericoCampo02(rs.getString("fia_num_folio"));
                gc.setGenericoCampo03(rs.getString("fia_titular_fianza"));
                gc.setGenericoCampo04("$".concat(formatDecimal2D(rs.getDouble("fia_imp_fianza"))));
                gc.setGenericoCampo05(rs.getString("desc_moneda"));
                gc.setGenericoCampo06(rs.getString("fia_afianzadora"));
                gc.setGenericoCampo07(rs.getString("fia_nom_agente"));
                gc.setGenericoCampo08(rs.getString("fia_status"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaFianzas()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaHonorarios(long contratoNumero) throws SQLException {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT dec_num_contrato, dec_cve_pers_fid, dec_cve_tipo_hono, dec_fec_calc_hono,\n"
                    + "       Date(RTRim(Char(dec_ano_per_del))||'-'||RTrim(Char(dec_mes_per_del))||'-'||RTrim(Char(dec_dia_per_del))) Del,\n"
                    + "       Date(RTRim(Char(dec_ano_per_al))||'-'||RTRim(Char(dec_mes_per_al))||'-'||RTrim(Char(dec_dia_per_al))) Al,    \n"
                    + "       CASE WHEN dec_concepto_hono Like '%gastos de cobranza%' THEN 0 ELSE dec_imp_orig_honor END importe,\n"
                    + "       dec_orig_iva_honor, CASE WHEN dec_concepto_hono Like '%gastos de cobranza%' THEN dec_imp_orig_honor ELSE dec_orig_extemp END Moratorios,\n"
                    + "       CASE WHEN dec_concepto_hono Like '%gastos de cobranza%' THEN 0 ELSE dec_imp_rem_honor END imp_rem, \n"
                    + "       dec_rem_iva_honor, CASE WHEN dec_concepto_hono Like '%gastos de cobranza%' THEN dec_imp_rem_honor ELSE dec_rem_extemp END mora_rem, \n"
                    + "       desc_moneda, dec_cve_calif_hono                               \n"
                    + "FROM   SAF.DetCart                                                   \n"
                    + "JOIN  (SELECT cve_num_sec_clave num_moneda,cve_desc_clave desc_moneda\n"
                    + "       FROM   SAF.Claves                                             \n"
                    + "       WHERE  cve_num_clave=?) monedas                               \n"
                    + "ON     dec_num_moneda      = monedas.num_moneda                      \n"
                    + "WHERE  dec_num_contrato    = ?                                       \n"
                    + "AND   (dec_imp_rem_honor   <>?                                       \n"
                    + "OR     dec_rem_extemp      <>?                                       \n"
                    + "OR     dec_rem_iva_honor   <>?)                                      \n"
                    + "AND    dec_cve_st_detcart = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, 2);
            pstmt.setLong(2, contratoNumero);
            pstmt.setInt(3, 0);
            pstmt.setInt(4, 0);
            pstmt.setInt(5, 0);
            pstmt.setString(6, "ACTIVO");
            rs = pstmt.executeQuery();
            //System.out.println(sqlComando);
            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("dec_num_contrato"));
                gc.setGenericoCampo01(rs.getString("dec_cve_pers_fid"));
                gc.setGenericoCampo02(rs.getString("dec_cve_tipo_hono"));
                gc.setGenericoCampo03(rs.getString("dec_fec_calc_hono"));
                gc.setGenericoCampo04(formatFecha(rs.getDate("Del")).concat(" - ").concat(formatFecha(rs.getDate("Al"))));
                gc.setGenericoCampo05("$".concat(formatDecimal2D(rs.getDouble("importe"))));
                gc.setGenericoCampo06("$".concat(formatDecimal2D(rs.getDouble("dec_orig_iva_honor"))));
                gc.setGenericoCampo07("$".concat(formatDecimal2D(rs.getDouble("Moratorios"))));
                gc.setGenericoCampo08("$".concat(formatDecimal2D(rs.getDouble("imp_rem"))));
                gc.setGenericoCampo09("$".concat(formatDecimal2D(rs.getDouble("dec_rem_iva_honor"))));
                gc.setGenericoCampo10("$".concat(formatDecimal2D(rs.getDouble("mora_rem"))));
                gc.setGenericoCampo11(rs.getString("desc_moneda"));
                gc.setGenericoCampo12(rs.getString("dec_cve_calif_hono"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaHonorarios()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaInversionesDirecto(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT  CPE_NUM_CONTRATO, CPE_SUB_CONTRATO, CPE_CONTRATO_INTER, CPE_NOM_PIZARRA, CPE_NUM_SERIE_EMIS, CPE_NUM_CUPON_VIG,\n"
                    + "        DESC_MONEDA, CPE_NUM_TIT_COMPRA, CPE_TIT_DISP_COMP, CPE_PRECIO_EMISION, CPE_IMP_COMPRA, CPE_CVE_ST_COMPEMI,    \n"
                    + "        CPE_DIA_COMPRA, int_intermediario       \n"
                    + "FROM    SAF.COMPEMIS                            \n"
                    + "JOIN   (SELECT CVE_NUM_SEC_CLAVE NUM_MONEDA,CVE_DESC_CLAVE DESC_MONEDA FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=2) MONEDAS  \n"
                    + "ON      CPE_NUM_MONEDA     = MONEDAS.NUM_MONEDA \n"
                    + "JOIN    SAF.INTERMED                            \n"
                    + "ON      CPE_ENTIDAD_FIN    = INT_ENTIDAD_FIN    \n"
                    + "WHERE   CPE_NUM_CONTRATO   = ?                  \n"
                    + "AND     CPE_CVE_ST_COMPEMI = ?                  \n"
                    + "AND     CPE_TIT_DISP_COMP  > ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            pstmt.setInt(3, 0);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("CPE_NUM_CONTRATO"));
                gc.setGenericoCampo01(rs.getString("CPE_SUB_CONTRATO"));
                gc.setGenericoCampo02(rs.getString("CPE_CONTRATO_INTER"));
                gc.setGenericoCampo03(rs.getString("CPE_NOM_PIZARRA"));
                gc.setGenericoCampo04(rs.getString("CPE_NUM_SERIE_EMIS"));
                gc.setGenericoCampo05(rs.getString("CPE_NUM_CUPON_VIG"));
                gc.setGenericoCampo06(rs.getString("DESC_MONEDA"));
                gc.setGenericoCampo07(formatDecimal0D(rs.getInt("CPE_NUM_TIT_COMPRA")));
                gc.setGenericoCampo08(formatDecimal0D(rs.getInt("CPE_TIT_DISP_COMP")));
                gc.setGenericoCampo09("$".concat(formatDecimal2D(rs.getDouble("CPE_PRECIO_EMISION"))));
                gc.setGenericoCampo10("$".concat(formatDecimal2D(rs.getDouble("CPE_IMP_COMPRA"))));
                gc.setGenericoCampo11(rs.getString("CPE_CVE_ST_COMPEMI"));
                gc.setGenericoCampo12(formatFecha(rs.getDate("CPE_DIA_COMPRA")));
                gc.setGenericoCampo13(rs.getString("int_intermediario"));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaInversionesDirecto()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_ConsultaInversionesPlazo(long contratoNumero) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT CRE_NUM_CONTRATO, CRE_SUB_CONTRATO, CRE_FOLIO_REPORTO, CRE_CONTRATO_INTER, NOM_CUSTODIO,                     \n"
                    + "       CRE_IMP_REPORTO, CRE_NUM_PLAZO, CRE_PJE_TASA_PACTA, CRE_PREMIO_REPORTO, DESC_MONEDA, CRE_CVE_ST_CONREPO,     \n"
                    + "       DATE(RTRIM(CHAR(CRE_ANO_VENCIM))||'-'||RTRIM(CHAR(CRE_MES_VENCIM))||'-'||RTRIM(CHAR(CRE_DIA_VENCIM))) Vencim \n"
                    + "FROM   SAF.CONREPOR \n"
                    + "JOIN  (SELECT CVE_NUM_SEC_CLAVE NUM_CUSTODIO, CVE_DESC_CLAVE NOM_CUSTODIO FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=386) CUSTODIO \n"
                    + "ON     INTEGER(CRE_NOM_CUSTODIO) = CUSTODIO.NUM_CUSTODIO   \n"
                    + "JOIN  (SELECT CVE_NUM_SEC_CLAVE NUM_MONEDA,CVE_DESC_CLAVE DESC_MONEDA FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=2) MONEDAS \n"
                    + "ON     CRE_NUM_MONEDA     = MONEDAS.NUM_MONEDA             \n"
                    + "WHERE  CRE_NUM_CONTRATO   = ? \n"
                    + "AND    CRE_CVE_ST_CONREPO = ? \n"
                    + "AND    CRE_IMP_REPORTO    > ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            pstmt.setInt(3, 0);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("CRE_NUM_CONTRATO"));
                gc.setGenericoCampo01(rs.getString("CRE_SUB_CONTRATO"));
                gc.setGenericoCampo02(rs.getString("CRE_FOLIO_REPORTO"));
                gc.setGenericoCampo03(rs.getString("CRE_CONTRATO_INTER"));
                gc.setGenericoCampo04(rs.getString("NOM_CUSTODIO"));
                Double CRE_IMP_REPORTO = CValidacionesUtil.validarDouble(rs.getString("CRE_IMP_REPORTO"));
                gc.setGenericoCampo05("$".concat(formatDecimal2D(CRE_IMP_REPORTO)));
                gc.setGenericoCampo06(rs.getString("CRE_NUM_PLAZO"));
                Double CRE_PJE_TASA_PACTA = CValidacionesUtil.validarDouble(rs.getString("CRE_PJE_TASA_PACTA"));
                gc.setGenericoCampo07("$".concat(formatDecimal2D(CRE_PJE_TASA_PACTA)));
                Double CRE_PREMIO_REPORTO = CValidacionesUtil.validarDouble(rs.getString("CRE_PREMIO_REPORTO"));
                gc.setGenericoCampo08("$".concat(formatDecimal2D(CRE_PREMIO_REPORTO)));
                gc.setGenericoCampo09(rs.getString("DESC_MONEDA"));
                gc.setGenericoCampo10(rs.getString("CRE_CVE_ST_CONREPO"));
                gc.setGenericoCampo11(formatFecha(rs.getDate("Vencim")));

                consulta.add(gc);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_ConsultaInversionesPlazo()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }


    public List<GenericoConsultaBean> onCancelaSaldo_Consulta(long contratoNumero, String filtro) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta, mone_id_moneda, sald_ax1, sald_ax2, sald_ax3, \n"
                    + "       sald_saldo_inisim, detm_folio_op, asie_sec_asiento, asie_car_abo, asie_importe, asie_descripcion,      \n"
                    + "       sald_saldo_actual, sald_fec_sim, sald_fec_aplic, sald_estatus, sec_movto, tip_movto                    \n"
                    + "FROM   SAF.CANCELA_SALDH  \n"
                    + "WHERE  sald_ax1     = ?   \n"
                    + "AND    sald_estatus = ?   \n"
                    + "ORDER  BY 1,2,3,4,5,6";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, filtro);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("asie_sec_asiento"));
                gc.setGenericoCampo01(rs.getString("ccon_cta"));
                gc.setGenericoCampo02(rs.getString("ccon_scta"));
                gc.setGenericoCampo03(rs.getString("ccon_2scta"));
                gc.setGenericoCampo04(rs.getString("ccon_3scta"));
                gc.setGenericoCampo05(rs.getString("ccon_4scta"));
                gc.setGenericoCampo06(rs.getString("mone_id_moneda"));
                gc.setGenericoCampo07(rs.getString("sald_ax1"));
                gc.setGenericoCampo08(rs.getString("sald_ax2"));
                gc.setGenericoCampo09(rs.getString("sald_ax3"));
                Double sald_saldo_inisim = CValidacionesUtil.validarDouble(rs.getString("sald_saldo_inisim"));
                gc.setGenericoCampo10("$".concat(formatDecimal2D(sald_saldo_inisim)));
                gc.setGenericoCampo11(rs.getString("detm_folio_op"));
                gc.setGenericoCampo12(rs.getString("asie_sec_asiento"));
                gc.setGenericoCampo13(rs.getString("asie_car_abo"));
                Double asie_importe =  CValidacionesUtil.validarDouble(rs.getString("asie_importe"));
                gc.setGenericoCampo14("$".concat(formatDecimal2D(asie_importe)));
                Double sald_saldo_actual =  CValidacionesUtil.validarDouble(rs.getString("sald_saldo_actual"));
                gc.setGenericoCampo15("$".concat(formatDecimal2D(sald_saldo_actual)));
                gc.setGenericoCampo16(rs.getString("sald_estatus"));
                gc.setGenericoCampo17(rs.getString("asie_descripcion"));

                consulta.add(gc);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_Consulta()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<GenericoConsultaBean> onCancelaSaldo_Consulta_Tipmovto(long contratoNumero, String filtro, String tipMovto) {
        List<GenericoConsultaBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta, mone_id_moneda, sald_ax1, sald_ax2, sald_ax3, \n"
                    + "       sald_saldo_inisim, detm_folio_op, asie_sec_asiento, asie_car_abo, asie_importe, asie_descripcion,      \n"
                    + "       sald_saldo_actual, sald_fec_sim, sald_fec_aplic, sald_estatus, sec_movto, tip_movto          \n"
                    + "FROM   SAF.CANCELA_SALDH  \n"
                    + "WHERE  sald_ax1     = ?   \n"
                //    + "AND    sald_estatus = ?   \n"
                    + "AND    tip_movto    = ?   \n"
                    + "ORDER  BY sec_movto, ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta, mone_id_moneda";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            //pstmt.setString(2, filtro);
            pstmt.setString(2, tipMovto);
            rs = pstmt.executeQuery();
            indSimulados = false;
            while (rs.next()) {
                GenericoConsultaBean gc = new GenericoConsultaBean();
                gc.setGenericoCampo00(rs.getString("sec_movto"));
                gc.setGenericoCampo01(rs.getString("ccon_cta"));
                gc.setGenericoCampo02(rs.getString("ccon_scta"));
                gc.setGenericoCampo03(rs.getString("ccon_2scta"));
                gc.setGenericoCampo04(rs.getString("ccon_3scta"));
                gc.setGenericoCampo05(rs.getString("ccon_4scta"));
                gc.setGenericoCampo06(rs.getString("mone_id_moneda"));
                gc.setGenericoCampo07(rs.getString("sald_ax1"));
                gc.setGenericoCampo08(rs.getString("sald_ax2"));
                gc.setGenericoCampo09(rs.getString("sald_ax3"));
                Double sald_saldo_inisim = CValidacionesUtil.validarDouble(rs.getString("sald_saldo_inisim"));
                gc.setGenericoCampo10("$".concat(formatDecimal2D(sald_saldo_inisim)));
                gc.setGenericoCampo11(rs.getString("detm_folio_op"));
                gc.setGenericoCampo12(rs.getString("asie_sec_asiento"));
                gc.setGenericoCampo13(rs.getString("asie_car_abo"));
                Double  asie_importe = CValidacionesUtil.validarDouble(rs.getString("asie_importe"));
                gc.setGenericoCampo14("$".concat(formatDecimal2D(asie_importe)));
                Double sald_saldo_actual = CValidacionesUtil.validarDouble(rs.getString("sald_saldo_actual"));
                gc.setGenericoCampo15("$".concat(formatDecimal2D(sald_saldo_actual)));
                gc.setGenericoCampo16(rs.getString("sald_estatus"));
                gc.setGenericoCampo17(rs.getString("asie_descripcion"));
                gc.setGenericoCampo18(formatFecha(rs.getDate("sald_fec_sim")));
                gc.setGenericoCampo19(formatFecha(rs.getDate("sald_fec_aplic")));
                if (gc.getGenericoCampo16().equals("SIMULADO")) indSimulados = true;
                
                        consulta.add(gc);
            }
        } catch (NumberFormatException | SQLException Err) {
            logger.error(Err.getMessage() + "onCancelaSaldo_Consulta()");
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public Boolean onCancelaSaldo_Ejecuta(ContabilidadCancelaSaldoBean cs) {
        try {
            String sqlComando = "{CALL DB2FIDUC.SP_EXTINCION_SALDO(?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("PI_FISO", cs.getCsContratoNum());
            cstmt.setString("PS_OPC_CTAS", cs.getCsOpcion());
            cstmt.setString("PS_TIPO_OPERACION", cs.getCsOperacion());
            cstmt.setDate("PDT_FECHA", cs.getCsFecha());
            cstmt.setInt("PI_USUARIO", cs.getCsBitUsuario());
            cstmt.setString("PS_TERMINAL", cs.getCsBitTerminal());
            cstmt.setString("PS_PANTALLA", cs.getCsBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   I N D I V I D U A L I Z A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean onBienFideIndiv_AdministraUnidadIndiv(ContabilidadBienFideUnidadIndivBean bfui) {
        try {
            String sqlComando = "{CALL DB2FIDUC.SP_INMUEBLE_DEPTO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("PN_ID_DEPTO", bfui.getBienFideUnidadIndivSec());
            cstmt.setLong("PI_FISO", bfui.getBienFideContratoNum());
            cstmt.setInt("PSM_SUBFISO", bfui.getBienFideContratoNumSub());
            cstmt.setString("PS_TIPO_BIEN", bfui.getBienFideTipo());
            cstmt.setInt("PSM_ID_BIEN", bfui.getBienFideId());
            cstmt.setInt("PN_ID_UNIDAD", bfui.getBienFideUnidadSec());
            cstmt.setString("PS_NUM_DEPTO", bfui.getBienFideUnidadIndivDeptoNum());
            cstmt.setString("PS_TIPO", bfui.getBienFideUnidadIndivTipo());
            cstmt.setString("PS_UBICACION", bfui.getBienFideUnidadIndivUbicacion());
            cstmt.setString("PN_SUPERFICIEM2", bfui.getBienFideUnidadIndivSuperficieM2().replaceAll(",", ""));
            cstmt.setString("PN_INDIVISO", bfui.getBienFideUnidadIndivIndiviso());
            Double UnidaNiveles = CValidacionesUtil.validarDouble(bfui.getBienFideUnidadIndivNiveles());
            cstmt.setDouble("PN_NIVELES", UnidaNiveles);    
            cstmt.setDouble("PN_IMPORTE", bfui.getBienFideUnidadIndivPrecio());
            cstmt.setString("PS_MEDIDAS", bfui.getBienFideUnidadIndivMedidas());
            cstmt.setString("PS_ESTATUS_INM", bfui.getBienFideUnidadIndivStatusInmueble());
            cstmt.setString("PS_ESTATUS", bfui.getBienFideUnidadIndivStatus());
            cstmt.setString("PS_TIPO_OPERACION", bfui.getBienFideBitTipoOperacion());
            cstmt.setDate("PDT_FECHA", bfui.getBienFideFechaReg());
            cstmt.setInt("PI_USUARIO", bfui.getBienFideBitUsuario());
            cstmt.setString("PS_TERMINAL", bfui.getBienFideBitTerminal());
            cstmt.setString("PS_PANTALLA", bfui.getBienFideBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_AdministraUnidadIndiv()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onBienFideIndiv_AdministraUnidadLiq(ContabilidadBienFideUnidadLiqBean bful) {
        try {
            String sqlComando = "{CALL DB2FIDUC.SP_INMUEBLE_LIQUID(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("PI_FISO", bful.getBienFideContratoNum());
            cstmt.setInt("PSM_SUBFISO", bful.getBienFideContratoNumSub());
            cstmt.setString("PS_TIPO_BIEN", bful.getBienFideTipo());
            cstmt.setInt("PSM_ID_BIEN", bful.getBienFideId());
            cstmt.setInt("PN_ID_UNIDAD", bful.getBienFideUnidadSec());
            cstmt.setInt("PN_ID_DEPTO", bful.getBienFideUnidadLiqSec());
            cstmt.setDate("PDT_FEC_SOLICIT", bful.getBienFideUnidadLiqFechaSol());
            cstmt.setDate("PDT_FEC_INSTR", bful.getBienFideUnidadLiqFechaInstr());
            cstmt.setDate("PDT_FEC_FIRMA", bful.getBienFideUnidadLiqFechaFirma());
            cstmt.setDate("PDT_FEC_TRASDOM", bful.getBienFideUnidadLiqFechaTrasDom());
            cstmt.setString("PS_ESCRITURA", bful.getBienFideUnidadLiqEscritura());
            cstmt.setInt("PN_NUM_NOTARIO", bful.getBienFideUnidadLiqNotarioNum());
            cstmt.setString("PS_ADQUIERE", bful.getBienFideUnidadLiqAquiere());
            cstmt.setDouble("PN_TC", bful.getBienFideUnidadLiqTC());
            cstmt.setString("PS_TIPO_OPERACION", bful.getBienFideBitTipoOperacion());
            cstmt.setDate("PDT_FECHA", bful.getBienFideFechaReg());
            cstmt.setInt("PI_USUARIO", bful.getBienFideBitUsuario());
            cstmt.setString("PS_TERMINAL", bful.getBienFideBitTerminal());
            cstmt.setString("PS_PANTALLA", bful.getBienFideBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_AdministraUnidadLiq()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onBienFideIndiv_AdministraUnidad(ContabilidadBienFideUnidadBean bfu) {
        try {
            String sqlComando = "{CALL DB2FIDUC.SP_INMUEBLE_UNIDAD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("PN_ID_UNIDAD", bfu.getBienFideUnidadSec());
            cstmt.setLong("PI_FISO", bfu.getBienFideContratoNum());
            cstmt.setInt("PSM_SUBFISO", bfu.getBienFideContratoNumSub());
            cstmt.setString("PS_TIPO_BIEN", bfu.getBienFideTipo());
            cstmt.setInt("PSM_ID_BIEN", bfu.getBienFideId());
            cstmt.setString("PS_NOM_UNIDAD", bfu.getBienFideUnidadNombre());
            cstmt.setString("PS_TIPO_UNIDAD", bfu.getBienFideUnidadTipo());
            cstmt.setDate("PDT_FEC_CTO", bfu.getBienFideUnidadFechaCto());
            cstmt.setString("PS_DOMICILIO", bfu.getBienFideUnidadDomicilio());
            cstmt.setString("PS_ESCRIT_INICI", bfu.getBienFideUnidadEscrituraIni());
            cstmt.setInt("PN_NUM_NOTARIO", bfu.getBienFideUnidadNotarioNum());
            cstmt.setDouble("PN_IMPORTE", bfu.getBienFideUnidadValor());
            cstmt.setDouble("PN_SUPERFICIEM2", bfu.getBienFideUnidadSuperficie());
            cstmt.setString("PS_OBSERVACION", bfu.getBienFideUnidadObservacion());
            cstmt.setString("PS_ESTATUS", bfu.getBienFideUnidadStatus());
            cstmt.setString("PS_TIPO_OPERACION", bfu.getBienFideBitTipoOperacion());
            cstmt.setDate("PDT_FECHA", bfu.getBienFideFechaReg());
            cstmt.setInt("PI_USUARIO", bfu.getBienFideBitUsuario());
            cstmt.setString("PS_TERMINAL", bfu.getBienFideBitTerminal());
            cstmt.setString("PS_PANTALLA", bfu.getBienFideBitPantalla());
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_AdministraUnidad()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public void onBienFideIndiv_ObtenUnidadLiq(ContabilidadBienFideUnidadLiqBean bful) {
        try {
            String sqlComando = "SELECT dep_fecha_solicit, dep_fecha_instr, dep_fecha_firma, dep_fec_trasdom, dep_status1, not_localidad_nota, not_num_notario,\n"
                    + "       dep_num_escritura, dep_adquiere, Trim(Ucase(not_nom_notario))||'»'||not_localidad_nota||'»'||not_num_notario Notario   \n"
                    + "FROM   SAF.GRI_DEPT                       \n"
                    + "JOIN   SAF.Notarios                       \n"
                    + "ON     dep_notario      = not_num_notario \n"
                    + "WHERE  CTO_NUM_CONTRATO = ?               \n"
                    + "AND    INM_SUB_CONTRATO = ?               \n"
                    + "AND    INM_ORIGEN       = ?               \n"
                    + "AND    BIE_ID_BIEN      = ?               \n"
                    + "AND    UNI_NUM_UNIDAD   = ?               \n"
                    + "AND    DEP_NUM_ID       = ? ";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, bful.getBienFideContratoNum());
            pstmt.setInt(2, bful.getBienFideContratoNumSub());
            pstmt.setString(3, bful.getBienFideTipo());
            pstmt.setInt(4, bful.getBienFideId());
            pstmt.setInt(5, bful.getBienFideUnidadSec());
            pstmt.setInt(6, bful.getBienFideUnidadLiqSec());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bful.setBienFideUnidadLiqFechaSol(rs.getDate("dep_fecha_solicit"));
                bful.setBienFideUnidadLiqFechaInstr(rs.getDate("dep_fecha_instr"));
                bful.setBienFideUnidadLiqFechaFirma(rs.getDate("dep_fecha_firma"));
                bful.setBienFideUnidadLiqFechaTrasDom(rs.getDate("dep_fec_trasdom"));
                bful.setBienFideUnidadLiqEscritura(rs.getString("dep_num_escritura"));
                bful.setBienFideUnidadLiqAquiere(rs.getString("dep_adquiere"));
                bful.setBienFideUnidadLiqNotarioNom(rs.getString("Notario"));
                bful.setBienFideUnidadLiqNotarioNum(rs.getInt("not_num_notario"));
                bful.setBienFideUnidadLiqNotarioLoc(rs.getString("not_localidad_nota"));
                bful.setBienFideUnidadLiqStatus(rs.getString("dep_status1"));
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_ObtenUnidadLiq()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public List<ContabilidadBienFideUnidadIndivBean> onBienFideIndiv_ConsultaUnidadIndiv(long contratoNumero, int contratoNumeroSub, String bienFideTipo, int getBienFideId, int bienFideUnidadSec) {
        List<ContabilidadBienFideUnidadIndivBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT uni_num_unidad, dep_num_id, dep_num_depto, dep_ubicacion, dep_m2superfi, dep_indiviso, \n"
                    + "       dep_tipo, dep_niveles, dep_medidas, dep_precio, dep_imp_ultaval, dep_fecha_ultaval,    \n"
                    + "       cve_desc_clave, dep_status0, Decode(dep_status1, 'CONTABILIZADO','SI','NO') cveLiq, \n"
                    + "       COALESCE (DEP_VALOR_EXTIN,0) DEP_VALOR_EXTIN, DEP_MONEDA \n"
                    + "FROM   SAF.GRI_DEPT                   \n"
                    + "JOIN   SAF.CLAVES                     \n"
                    + "ON     dep_status = cve_num_sec_clave \n"
                    + "WHERE  cto_num_contrato = ?           \n"
                    + "AND    inm_sub_contrato = ?           \n"
                    + "AND    inm_origen       = ?           \n"
                    + "AND    bie_id_bien      = ?           \n"
                    + "AND    uni_num_unidad    = ?           \n"
                    + "AND    cve_num_clave    = ?           \n"
                    + "ORDER  BY dep_num_id";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setInt(2, contratoNumeroSub);
            pstmt.setString(3, bienFideTipo);
            pstmt.setInt(4, getBienFideId);
            pstmt.setInt(5, bienFideUnidadSec);
            pstmt.setInt(6, 355); 
                rs = pstmt.executeQuery();
            setBienFideUnidadAcumRegContable(0.00); 
            while (rs.next()) {
                ContabilidadBienFideUnidadIndivBean bfui00 = new ContabilidadBienFideUnidadIndivBean();
                bfui00.setBienFideUnidadIndivSec(rs.getInt("dep_num_id"));
                bfui00.setBienFideUnidadIndivDeptoNum(rs.getString("dep_num_depto"));
                bfui00.setBienFideUnidadIndivTipo(rs.getString("dep_tipo"));
                bfui00.setBienFideUnidadIndivUbicacion(rs.getString("dep_ubicacion"));
                bfui00.setBienFideUnidadIndivSuperficieM2(rs.getString("dep_m2superfi"));
                bfui00.setBienFideUnidadIndivMedidas(rs.getString("dep_medidas"));
                bfui00.setBienFideUnidadIndivIndiviso(rs.getString("dep_indiviso"));
                bfui00.setBienFideUnidadIndivNiveles(rs.getString("dep_niveles"));
                bfui00.setBienFideUnidadIndivPrecio(rs.getDouble("dep_precio"));
                bfui00.setBienFideUnidadIndivUltAvaluoValor(rs.getDouble("dep_imp_ultaval"));
                bfui00.setBienFideUnidadindivUlAvaluoFecha(rs.getDate("dep_fecha_ultaval"));
                bfui00.setBienFideUnidadIndivStatusInmueble(rs.getString("cve_desc_clave"));
                bfui00.setBienFideUnidadIndivStatus(rs.getString("dep_status0"));
                bfui00.setBienFideUnidadIndivCveLiq(rs.getString("cveLiq"));
                bfui00.setBienFideUnidadIndivMonedaNum(rs.getShort("dep_moneda"));
                bienFideUnidadAcumRegContable = bienFideUnidadAcumRegContable + (rs.getDouble("DEP_VALOR_EXTIN"));
                consulta.add(bfui00);
                
            }
        } catch (SQLException Err) {
            logger.error("Descripción: " + Err.getMessage() + "onBienFideIndiv_ConsultaUnidadIndiv()");
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadBienFideUnidadBean> onBienFideIndiv_ConsultaUnidad(long contratoNum, long contratoSubNum, String bienTipo, int bienId) {
        List<ContabilidadBienFideUnidadBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT cto_num_contrato, sct_sub_contrato, inm_origen, bie_num_bien, \n"
                    + "       uni_num_unidad, uni_nom_unidad, uni_tipo, uni_fecha_contrato, uni_domicilio, uni_escrit_inici,  \n"
                    + "       n.not_num_notario, uni_valor_uni, uni_num_moneda, uni_superficiem2, uni_observaciones, uni_alta,\n"
                    + "       uni_ultmov, uni_status, mon_nom_moneda, Trim(Ucase(not_nom_notario)) nom, not_localidad_nota, not_num_ofic_nota  \n"
                    + "FROM   SAF.INMU_UNIDADES  iu                  \n"
                    + "JOIN   SAF.Monedas                            \n"
                    + "ON     uni_num_moneda = mon_num_pais          \n"
                    + "JOIN   SAF.Notarios       n                   \n"
                    + "ON     iu.not_num_notario = n.not_num_notario \n"
                    + "WHERE  cto_num_contrato = ?                   \n"
                    + "AND    sct_sub_contrato = ?                   \n"
                    + "AND    inm_origen       = ?                   \n"
                    + "AND    bie_num_bien     = ?                   \n"
                    + "ORDER  BY bie_num_bien";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNum);
            pstmt.setLong(2, contratoSubNum);
            pstmt.setString(3, bienTipo);
            pstmt.setInt(4, bienId);
            rs = pstmt.executeQuery();

                while (rs.next()) {
                ContabilidadBienFideUnidadBean bfu = new ContabilidadBienFideUnidadBean();
                bfu.setBienFideId(rs.getInt("bie_num_bien"));
                bfu.setBienFideUnidadSec(rs.getInt("uni_num_unidad"));
                bfu.setBienFideUnidadDomicilio(rs.getString("uni_domicilio"));
                bfu.setBienFideUnidadEscrituraIni(rs.getString("uni_escrit_inici"));
                bfu.setBienFideUnidadFechaCto(rs.getDate("uni_fecha_contrato"));
                bfu.setBienFideUnidadMonedaNum(rs.getInt("uni_num_moneda"));
                bfu.setBienFideUnidadMonedaNom(rs.getString("mon_nom_moneda"));
                bfu.setBienFideUnidadNombre(rs.getString("uni_nom_unidad"));
                bfu.setBienFideUnidadNotarioNum(rs.getInt("not_num_notario"));
                bfu.setBienFideUnidadNotarioNumOFicial(rs.getInt("not_num_ofic_nota"));
                bfu.setBienFideUnidadNotarioNom(rs.getString("nom"));
                bfu.setBienFideUnidadNotarioLocalidad(rs.getString("not_localidad_nota"));
                bfu.setBienFideUnidadNotarioLista(rs.getString("nom").concat("»").concat(rs.getString("not_localidad_nota")).concat("»").concat(String.valueOf(rs.getInt("not_num_notario")).concat("»").concat(String.valueOf(rs.getInt("not_num_ofic_nota")))));
                bfu.setBienFideUnidadObservacion(rs.getString("uni_observaciones"));
                bfu.setBienFideUnidadSec(rs.getInt("uni_num_unidad"));
                bfu.setBienFideUnidadStatus(rs.getString("uni_status"));
                bfu.setBienFideUnidadSuperficie(rs.getDouble("uni_superficiem2"));
                bfu.setBienFideUnidadTipo(rs.getString("uni_tipo"));
                bfu.setBienFideUnidadValor(rs.getDouble("uni_valor_uni"));
              
                consulta.add(bfu);
            }
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadBienFideBean> onBienFideIndiv_Consulta(CriterioBusquedaContaBean cb) {
        List<ContabilidadBienFideBean> consulta = new ArrayList<>();
        try {
            conexion = DataBaseConexion.getInstance().getConnection();
            String sqlComando = "SELECT bie_num_contrato   AS fiso,           \n"
                    + "       bie_sub_contrato   AS sub_fiso,       \n"
                    + "      'FIDECOMITIDO'      AS clase_bien,     \n"
                    + "       bie_id_bien        AS num_bien,       \n"
                    + "       bie_cve_bien       AS desc_c,         \n"
                    + "       bie_imp_bien       AS importe,        \n"
                    + "       bie_imp_ult_valua  AS valua,          \n"
                    + "       bie_fec_ult_valua  AS fec_valua,      \n"
                    + "       bie_cve_st_bienfid AS estatus,        \n"
                    + "       bie_num_moneda     AS moneda,         \n"
                    + "       mon_nom_moneda     AS monedaNom,      \n"
                    + "      'BIENFIDE'          AS nomtabla        \n"
                    + "FROM   SAF.BienFide                          \n"
                    + "JOIN   SAF.Contrato C ON bie_num_contrato   = C.cto_num_contrato \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = bie_num_contrato AND V.USU_NUM_USUARIO = ? \n"
                    + "JOIN   SAF.Monedas ON bie_num_moneda     = mon_num_pais \n"
                    + "WHERE  C.cto_cve_st_contrat = ?                \n"
                    + "AND    bie_cve_tipo_bien  = ?                \n";

            if (cb.getCriterioAX1() != null) {
                sqlComando += "AND bie_num_contrato   = ? \n";
            }
            if (cb.getCriterioAX2() != null) {
                sqlComando += "AND bie_sub_contrato = ? \n";
            }
            if (cb.getCriterioPlaza() != null) {
                sqlComando += "AND    bie_num_contrato IN (SELECT cto_num_contrato FROM SAF.Contrato WHERE cto_num_nivel4= ?) \n";
            }

            sqlComando += "UNION                                        \n"
                    + "SELECT gri_num_contrato   AS fiso,           \n"
                    + "       gri_sub_contrato   AS sub_fiso,       \n"
                    + "      'GARANTIA'          AS clase_bien,     \n"
                    + "       gri_num_inmueble   AS num_bien,       \n"
                    + "       gri_cve_tipo_inmu  AS desc_c,         \n"
                    + "       gri_imp_inmueble   AS importe,        \n"
                    + "       gri_imp_ult_valua  AS valua,          \n"
                    + "       gri_fec_ult_valua  AS fec_valua,      \n"
                    + "       gri_cve_status     AS estatus,        \n"
                    + "       gra_num_moneda     AS moneda,         \n"
                    + "       mon_nom_moneda     AS monedaNom,      \n"
                    + "      'GRAINMU'           AS nomtabla        \n"
                    + "FROM   SAF.Contrato C                        \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = C.cto_num_contrato AND V.USU_NUM_USUARIO = ? \n"
                    + "INNER JOIN SAF.GraInmu ON C.cto_num_contrato = GRI_NUM_CONTRATO \n"
                    + "INNER JOIN SAF.Gara_Tipo_Moneda ON C.cto_num_contrato = GRN_NUM_CONTRATO AND grn_sub_contrato   = gri_sub_contrato \n"
                    + "INNER JOIN SAF.Monedas ON gra_num_moneda = mon_num_pais \n"
                    + "WHERE  C.cto_cve_st_contrat = ? \n"
                    + "AND    gra_tipo_garantia  = ? \n"
                    + "AND    gra_num_garantia   = gri_num_inmueble \n";

            if (cb.getCriterioAX1() != null) {
                sqlComando += "AND gri_num_contrato   = ? \n";
            }
            if (cb.getCriterioAX2() != null) {
                sqlComando += "AND gri_sub_contrato = ? \n";
            }
            if (cb.getCriterioPlaza() != null) {
                sqlComando += "AND    gri_num_contrato IN (SELECT cto_num_contrato FROM SAF.Contrato WHERE cto_num_nivel4= ?) \n";
            }

            sqlComando += "ORDER  BY fiso, sub_fiso, clase_bien, num_bien";
            sqlParam = 0;
                pstmt = conexion.prepareStatement(sqlComando);
            sqlParam++;
            pstmt.setInt(sqlParam, cb.getCriterioUsuario());
            sqlParam++;
            pstmt.setString(sqlParam, "ACTIVO");
            sqlParam++;
            pstmt.setString(sqlParam, "INMUEBLES");

            if (cb.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cb.getCriterioAX1());
            }
            if (cb.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cb.getCriterioAX2());
            }
            if (cb.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cb.getCriterioPlaza());
            }

            sqlParam++;
            pstmt.setInt(sqlParam, cb.getCriterioUsuario());
            sqlParam++;
            pstmt.setString(sqlParam, "ACTIVO");
            sqlParam++;
            pstmt.setString(sqlParam, "INMUEBLE");

            if (cb.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cb.getCriterioAX1());
            }
            if (cb.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cb.getCriterioAX2());
            }
            if (cb.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cb.getCriterioPlaza());
            }
            /*
                pstmt.setLong  (4, cb.getCriterioAX1());
                if (cb.getCriterioAX2()!= null){
                    pstmt.setInt(5, cb.getCriterioUsuario());
                    pstmt.setLong  (6, cb.getCriterioAX2());
                    pstmt.setString(7, "ACTIVO");
                    pstmt.setString(8, "INMUEBLE");
                    pstmt.setLong  (9, cb.getCriterioAX1());
                    pstmt.setLong  (10, cb.getCriterioAX2());
                }else{
                    pstmt.setInt(5, cb.getCriterioUsuario());
                    pstmt.setString(6, "ACTIVO");
                    pstmt.setString(7, "INMUEBLE");
                    pstmt.setLong  (8, cb.getCriterioAX1());
                }*/
//            }
            rs = pstmt.executeQuery();
             
            while (rs.next()) {
                ContabilidadBienFideBean bf = new ContabilidadBienFideBean();
                bf.setBienFideContratoNum(rs.getLong("fiso"));
                bf.setBienFideContratoNumSub(rs.getInt("sub_fiso"));
                bf.setBienFideTipo(rs.getString("clase_bien"));
                bf.setBienFideImporte(rs.getDouble("importe"));
                bf.setBienFideImporteUltVal(rs.getDouble("valua"));
                bf.setBienFideMonedaNom(rs.getString("monedaNom"));
                bf.setBienFideId(rs.getInt("num_bien"));
               
//                String fecha = rs.getString("fec_valua");                
//                if (fecha.contains("-")) fecha = fecha.substring(0, 4).concat("/") + fecha.substring(5, 2).concat("/")+ fecha.substring(8, 2);
//                bf.setBienFideFechaValUlt(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                
                bf.setBienFideFechaValUltStr(rs.getString("fec_valua"));
                bf.setBienFideStatus(rs.getString("estatus"));
                bf.setBienFideDescripcion(rs.getString("desc_c"));
                consulta.add(bf);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_Consulta()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public void onBienFideIndiv_IndivMasivoAplica(int paramUsuarioNumero, java.sql.Date fechaSistema, String paramTipoProceso, List<String> listaProceso) {
        String[] arr = new String[Short.MAX_VALUE];
        int idx = 0, regCor = 0, regErr = 0, regTot = 0;
        try {
                conexion = DataBaseConexion.getInstance().getConnection();
            if (paramTipoProceso.equals("ESTATUS")) {
                String sqlComando = "SELECT fiso, sub_fiso, tipo_bien, num_inmueble, num_unidad, \n"
                        + "       id, RTrim(Char(cve_num_sec_clave)) AS ST_Indiv       \n"
                        + "FROM   SAF.INM_LIQ_MASIVA \n"
                        + "JOIN   SAF.Claves         \n"
                        + "ON     cve_num_clave  = ? \n"
                        + "AND    cve_desc_clave = estatus_indiv \n"
                        + "WHERE  proceso        = ? \n"
                        + "AND    liquida_reg    = ? \n"
                        + "AND    usuario        = ? WITH UR";
                pstmt = conexion.prepareStatement(sqlComando);
                pstmt.setInt(1, 355);
                pstmt.setString(2, paramTipoProceso);
                pstmt.setInt(3, 0);
                pstmt.setInt(4, paramUsuarioNumero);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    arr[idx] = rs.getString("fiso").concat("\t")
                            + //0
                            rs.getString("sub_fiso").concat("\t")
                            + //1
                            rs.getString("tipo_bien").concat("\t")
                            + //2
                            rs.getString("num_inmueble").concat("\t")
                            + //3
                            rs.getString("num_unidad").concat("\t")
                            + //4
                            rs.getString("id").concat("\t")
                            + //5
                            rs.getString("ST_Indiv");                   //6
                    idx++;
                }
                rs.close();
                pstmt.close();

                for (int item = 0; item <= arr.length - 1; item++) {
                    if (arr[item] != null) {
                        String[] arrItem = arr[item].split("\t");
                        try {
                            sqlComando = "UPDATE SAF.GRI_DEPT        \n"
                                    + "SET    DEP_STATUS      = ?,\n"
                                    + "       DEP_ULT_MOD     = ? \n"
                                    + "WHERE  CTO_NUM_CONTRATO= ? \n"
                                    + "AND    INM_SUB_CONTRATO= ? \n"
                                    + "AND    INM_ORIGEN      = ? \n"
                                    + "AND    BIE_ID_BIEN     = ? \n"
                                    + "AND    UNI_NUM_UNIDAD  = ? \n"
                                    + "AND    DEP_NUM_ID      = ? \n"
                                    + "WITH UR";
                            pstmt = conexion.prepareStatement(sqlComando);
                            pstmt.setString(1, arrItem[6].trim());
                            pstmt.setDate(2, fechaSistema);
                            pstmt.setLong(3, Long.parseLong(arrItem[0]));
                            pstmt.setInt(4, Integer.parseInt(arrItem[1]));
                            pstmt.setString(5, arrItem[2].trim());
                            pstmt.setInt(6, Integer.parseInt(arrItem[3]));
                            pstmt.setInt(7, Integer.parseInt(arrItem[4]));
                            pstmt.setInt(8, Integer.parseInt(arrItem[5]));

                            pstmt.execute();
                            listaProceso.add("Linea ".concat(String.valueOf(item + 1)).concat(": ").concat("Registro actualizado correctamente"));

                        } catch (NumberFormatException | SQLException SQLErr) {
                            listaProceso.add("Linea ".concat(String.valueOf(item + 1)).concat(": ").concat("Error de actualización.").concat(SQLErr.getMessage()));
                        }
                        pstmt.close();
                    } else {
                        //System.out.println("Finalizó ciclo de actualización de Status masivo");
                        break;
                    }
                }
            }
            if (paramTipoProceso.equals("LIQUIDACION")) {
                String sqlComando = "{CALL DB2FIDUC.SP_EJEC_LIQMASIVA(?,?,?,?,?,?,?,?)}";
                cstmt = conexion.prepareCall(sqlComando);
                cstmt.setDate("PDT_CONTA", fechaSistema);
                cstmt.setInt("PI_USUARIO", paramUsuarioNumero);
                cstmt.registerOutParameter("PI_REG_COR", java.sql.Types.INTEGER);
                cstmt.registerOutParameter("PI_REG_ERR", java.sql.Types.INTEGER);
                cstmt.registerOutParameter("PI_REG_TOT", java.sql.Types.INTEGER);
                cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
                cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
                cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

                cstmt.execute();

                mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
                regCor = cstmt.getInt("PI_REG_COR");
                regErr = cstmt.getInt("PI_REG_ERR");
                regTot = cstmt.getInt("PI_REG_TOT");
                cstmt.close();
                listaProceso.add("Correctos: " + String.valueOf(regCor));
                listaProceso.add("Erroneos : " + String.valueOf(regErr));
                listaProceso.add("Totales  : " + String.valueOf(regTot));
                listaProceso.add(mensajeErrorSP);
            }
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_IndivMasivoAplica()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public void onBienFideIndiv_IndivMasivoLimpiaTablas(int paramUsuarioNumero, String paramProcesoTipo) {
        try {
            conexion = DataBaseConexion.getInstance().getConnection();
            String sqlComando = "DELETE FROM SAF.INM_LIQ_MASIVA WHERE proceso = ? AND usuario = ? WITH UR";
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, paramProcesoTipo);
            pstmt.setLong(2, paramUsuarioNumero);
            pstmt.execute();
            pstmt.close();

            sqlComando = "DELETE FROM SAF.INM_MSG_MASIVA WHERE proceso = ? AND usuario = ? WITH UR";
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, paramProcesoTipo);
            pstmt.setLong(2, paramUsuarioNumero);
            pstmt.execute();
            pstmt.close();

            conexion.close();

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_IndivMasivoLimpiaTablas()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public Boolean onBienFideIndiv_IndivMasivoValidaDuplicado(ContabilidadBienFideUnidadIndivMasivoBean cbfuim)  {
        Boolean bExiste = Boolean.TRUE;
        try {
            String sqlComando = "SELECT Count(*) Total     \n"
                    + "FROM   SAF.INM_LIQ_MASIVA \n"
                    + "WHERE  proceso      = ?   \n"
                    + "AND    fiso         = ?   \n"
                    + "AND    sub_fiso     = ?   \n"
                    + "AND    tipo_bien    = ?   \n"
                    + "AND    num_inmueble = ?   \n"
                    + "AND    num_unidad   = ?   \n"
                    + "AND    id           = ?   \n"
                    + "AND    usuario      = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, cbfuim.getBienFideUnidadIndivMasivoProceso());
            pstmt.setLong(2, cbfuim.getBienFideUnidadIndivMasivoContrato());
            pstmt.setInt(3, cbfuim.getBienFideUnidadIndivMasivoContratoSub());
            pstmt.setString(4, cbfuim.getBienFideUnidadIndivMasivoBienTipo());
            pstmt.setInt(5, cbfuim.getBienFideUnidadIndivMasivoBienId());
            pstmt.setInt(6, cbfuim.getBienFideUnidadIndivMasivoBienUniId());
            pstmt.setInt(7, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivId());
            pstmt.setInt(8, cbfuim.getBienFideUnidadIndivMasivoBienUsuarioNum());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getShort("Total") == 0) {
                    bExiste = Boolean.FALSE;
                } else {
                    mensajeError = "Error, el registro ya se encuentra generado en SAF.INM_LIQ_MASIVA";
                }
            }
            rs.close();
            pstmt.close();

            if (bExiste.equals(Boolean.FALSE)) {
                sqlComando = "INSERT INTO SAF.INM_LIQ_MASIVA VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = conexion.prepareStatement(sqlComando);
                pstmt.setString(1, cbfuim.getBienFideUnidadIndivMasivoProceso());
                pstmt.setLong(2, cbfuim.getBienFideUnidadIndivMasivoContrato());
                pstmt.setInt(3, cbfuim.getBienFideUnidadIndivMasivoContratoSub());
                pstmt.setString(4, cbfuim.getBienFideUnidadIndivMasivoBienTipo());
                pstmt.setInt(5, cbfuim.getBienFideUnidadIndivMasivoBienId());
                pstmt.setInt(6, cbfuim.getBienFideUnidadIndivMasivoBienUniId());
                pstmt.setInt(7, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivId());
                pstmt.setDouble(8, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivIndiviso());
                pstmt.setDate(9, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivFechaSolicitud());
                pstmt.setDate(10, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivFechaInstruccion());
                pstmt.setDate(11, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivFechaFirma());
                pstmt.setDate(12, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivFechaDominio());
                pstmt.setString(13, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivEscritura());
                pstmt.setString(14, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivNotario());
                pstmt.setString(15, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivAdquiriente());
                pstmt.setString(16, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivStatus());
                pstmt.setString(17, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivStatusLiq());
                pstmt.setLong(18, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivFolioOp());
                pstmt.setShort(19, cbfuim.getBienFideUnidadIndivMasivoBienUniIndivLineaSec());
                pstmt.setInt(20, 0);
                pstmt.setInt(21, cbfuim.getBienFideUnidadIndivMasivoBienUsuarioNum());

                pstmt.execute();
                pstmt.close();

                valorRetorno = Boolean.TRUE;
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFideIndiv_IndivMasivoValidaDuplicado()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   B I E N E S   F I D
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadBienFideBean> onBienFide_Consulta(CriterioBusquedaContaBean cbc)  {
        List<ContabilidadBienFideBean> consulta = new ArrayList<>();
        try {
            sqlParam = 0;
            String sqlFiltro = "WHERE ";
            String sqlComando = "SELECT bie_num_contrato, bie_sub_contrato, bie_cve_tipo_bien, bie_cve_bien, bie_id_bien,  \n"
                    + "       bie_imp_bien, bie_imp_ult_valua, bie_cve_per_val, bie_desc_bien, bie_fec_ult_valua,\n"
                    + "       bie_fec_ano_inicio, bie_fec_mes_inicio, bie_fec_dia_inicio, mon_nom_moneda,        \n"
                    + "       bie_fec_ano_fin, bie_fec_mes_fin, bie_fec_dia_fin,                                 \n"
                    + "       Date(bie_ano_alta_reg||'-'||bie_mes_alta_reg||'-'||bie_dia_alta_reg)FecReg,        \n"
                    + "       subStr(bie_tex_comentario, 1, 50) bie_tex_comentario, bie_cve_st_bienfid,          \n"
                    + "       bie_num_moneda, bie_fec_sig_valua \n"
                    + "FROM   SAF.BienFide                      \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     bie_num_moneda = mon_num_pais     \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "bie_num_contrato = ? AND \n";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "bie_sub_contrato = ? AND \n";
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlFiltro += "bie_cve_tipo_bien = ? AND \n";
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlFiltro += "bie_cve_bien      = ? AND \n";
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlFiltro += "bie_cve_st_bienfid= ? AND \n";
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(new String()))) {
                sqlFiltro += "bie_num_contrato IN (SELECT cto_num_contrato FROM SAF.Contrato WHERE cto_num_nivel4= ?) AND \n";
            }

            if (sqlFiltro.contains("AND")) {
                sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
                sqlComando = sqlComando.concat(sqlFiltro);
            }
            sqlComando += "ORDER BY bie_num_contrato,bie_sub_contrato, bie_id_bien";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioBienFideTipo());
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioBienFideClas());
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioStatus());
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(0))) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadBienFideBean cbf = new ContabilidadBienFideBean();
                cbf.setBienFideContratoNum(rs.getLong("bie_num_contrato"));
                cbf.setBienFideContratoNumSub(rs.getInt("bie_sub_contrato"));
                cbf.setBienFideTipo(rs.getString("bie_cve_tipo_bien"));
                cbf.setBienFideClasificacion(rs.getString("bie_cve_bien"));

                cbf.setBienFideImporte(rs.getDouble("bie_imp_bien"));
                //cbf.setBienFideImporteTC(Double.MIN_NORMAL);
                cbf.setBienFideImporteUltVal(rs.getDouble("bie_imp_ult_valua"));
                cbf.setBienFideMonedaNom(rs.getString("mon_nom_moneda"));
                cbf.setBienFideMonedaNum(rs.getInt("bie_num_moneda"));
                cbf.setBienFidePeriodicidadRev(rs.getString("bie_cve_per_val"));
                cbf.setBienFideId(rs.getInt("bie_id_bien"));
                cbf.setBienFideFechaValUlt(rs.getDate("bie_fec_ult_valua"));
                cbf.setBienFideFechaValProx(rs.getDate("bie_fec_sig_valua"));
                cbf.setBienFideStatus(rs.getString("bie_cve_st_bienfid"));
                cbf.setBienFideFechaReg(rs.getDate("FecReg"));
                cbf.setBienFideComentario(rs.getString("bie_tex_comentario"));
                cbf.setBienFideDescripcion(rs.getString("bie_desc_bien"));
                consulta.add(cbf);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFide_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadBienFideBean> getTrustPropertyPaginated(CriterioBusquedaContaBean cbc, String offset) {
        String sqlComandoGetTrust = "";
        Integer sqlParamGetTrust = 0;
        String sqlFiltroGetTrust = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadBienFideBean> consulta = new ArrayList<>();
        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlFiltroGetTrust = "WHERE ";
            sqlComandoGetTrust = "SELECT bie_num_contrato, bie_sub_contrato, bie_cve_tipo_bien, bie_cve_bien, bie_id_bien, \n"
                    + "bie_imp_bien, bie_imp_ult_valua, bie_cve_per_val, bie_desc_bien, bie_fec_ult_valua, \n"
                    + "bie_fec_ano_inicio, bie_fec_mes_inicio, bie_fec_dia_inicio, mon_nom_moneda, \n"
                    + "bie_fec_ano_fin, bie_fec_mes_fin, bie_fec_dia_fin, FecReg, \n"
                    + "trim(bie_tex_comentario) bie_tex_comentario, bie_cve_st_bienfid, \n"
                    + "bie_num_moneda, bie_fec_sig_valua \n"
                    + "FROM ( \n"
                    + "SELECT ROW_NUMBER() OVER (ORDER BY bie_num_contrato,bie_sub_contrato, bie_id_bien) AS Ind, \n"
                    + "       bie_num_contrato, bie_sub_contrato, bie_cve_tipo_bien, bie_cve_bien, bie_id_bien,  \n"
                    + "       bie_imp_bien, bie_imp_ult_valua, bie_cve_per_val, bie_desc_bien, bie_fec_ult_valua,\n"
                    + "       bie_fec_ano_inicio, bie_fec_mes_inicio, bie_fec_dia_inicio, mon_nom_moneda,        \n"
                    + "       bie_fec_ano_fin, bie_fec_mes_fin, bie_fec_dia_fin,                                 \n"
                    + "       Date(bie_ano_alta_reg||'-'||bie_mes_alta_reg||'-'||bie_dia_alta_reg)FecReg,        \n"
                    + "       subStr(bie_tex_comentario, 1, 255) bie_tex_comentario, bie_cve_st_bienfid,          \n"
                    + "       bie_num_moneda, bie_fec_sig_valua \n"
                    + "FROM   SAF.BienFide                      \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     bie_num_moneda = mon_num_pais     \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = bie_num_contrato AND V.USU_NUM_USUARIO = ? \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltroGetTrust += "bie_num_contrato = ? AND \n";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltroGetTrust += "bie_sub_contrato = ? AND \n";
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlFiltroGetTrust += "bie_cve_tipo_bien = ? AND \n";
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlFiltroGetTrust += "bie_cve_bien      = ? AND \n";
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlFiltroGetTrust += "bie_cve_st_bienfid= ? AND \n";
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(new String()))) {
                sqlFiltroGetTrust += "bie_num_contrato IN (SELECT cto_num_contrato FROM SAF.Contrato WHERE cto_num_nivel4= ?) AND \n";
            }

            if (sqlFiltroGetTrust.contains("AND")) {
                sqlFiltroGetTrust = sqlFiltroGetTrust.substring(0, sqlFiltroGetTrust.length() - 5);
                sqlComandoGetTrust = sqlComandoGetTrust.concat(sqlFiltroGetTrust);
            }
            //sqlComandoGetTrust+= "ORDER BY bie_num_contrato,bie_sub_contrato, bie_id_bien LIMIT 10 OFFSET ".concat(offset);

            sqlComandoGetTrust += ") AS Consulta WHERE Consulta.Ind \n";
            sqlComandoGetTrust += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoGetTrust += "AND ".concat(Integer.toString(and));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGetTrust);

            sqlParamGetTrust++;
            preparedStatement.setInt(sqlParamGetTrust, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParamGetTrust++;
                preparedStatement.setLong(sqlParamGetTrust, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParamGetTrust++;
                preparedStatement.setLong(sqlParamGetTrust, cbc.getCriterioAX2());
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlParamGetTrust++;
                preparedStatement.setString(sqlParamGetTrust, cbc.getCriterioBienFideTipo());
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlParamGetTrust++;
                preparedStatement.setString(sqlParamGetTrust, cbc.getCriterioBienFideClas());
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlParamGetTrust++;
                preparedStatement.setString(sqlParamGetTrust, cbc.getCriterioStatus());
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(0))) {
                sqlParamGetTrust++;
                preparedStatement.setInt(sqlParamGetTrust, cbc.getCriterioPlaza());
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadBienFideBean cbf = new ContabilidadBienFideBean();
                cbf.setBienFideContratoNum(resultSet.getLong("bie_num_contrato"));
                cbf.setBienFideContratoNumSub(resultSet.getInt("bie_sub_contrato"));
                cbf.setBienFideTipo(resultSet.getString("bie_cve_tipo_bien"));
                cbf.setBienFideClasificacion(resultSet.getString("bie_cve_bien"));

                cbf.setBienFideImporte(resultSet.getDouble("bie_imp_bien"));
                //cbf.setBienFideImporteTC(Double.MIN_NORMAL);
                cbf.setBienFideImporteUltVal(resultSet.getDouble("bie_imp_ult_valua"));
                cbf.setBienFideMonedaNom(resultSet.getString("mon_nom_moneda"));
                cbf.setBienFideMonedaNum(resultSet.getInt("bie_num_moneda"));
                cbf.setBienFidePeriodicidadRev(resultSet.getString("bie_cve_per_val"));
                cbf.setBienFideId(resultSet.getInt("bie_id_bien"));
                cbf.setBienFideFechaValUlt(new Date (formatFechaParse(resultSet.getString("bie_fec_ult_valua")).getTime()));
                cbf.setBienFideFechaValProx(resultSet.getDate("bie_fec_sig_valua"));
                cbf.setBienFideStatus(resultSet.getString("bie_cve_st_bienfid"));
                cbf.setBienFideFechaReg(resultSet.getDate("FecReg"));
                cbf.setBienFideComentario(resultSet.getString("bie_tex_comentario"));
                cbf.setBienFideDescripcion(resultSet.getString("bie_desc_bien"));
                consulta.add(cbf);
            }
        } catch (SQLException e) {

            logger.error(mensajeError);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getTrustPropertyPaginated:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getTrustPropertyPaginated:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                   logger.error("Function ::getTrustPropertyPaginated:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        return consulta;
    }

    public int getTrustPropertyTotalRows(CriterioBusquedaContaBean cbc) {
        int totalRows = 0;

        try {
            sqlParam = 0;
            String sqlFiltro = "WHERE ";
            String sqlComando = "SELECT COUNT(*) AS total_rows            \n"
                    + "FROM   SAF.BienFide                      \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     bie_num_moneda = mon_num_pais     \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = bie_num_contrato AND V.USU_NUM_USUARIO = ? \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "bie_num_contrato = ? AND \n";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "bie_sub_contrato = ? AND \n";
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlFiltro += "bie_cve_tipo_bien = ? AND \n";
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlFiltro += "bie_cve_bien      = ? AND \n";
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlFiltro += "bie_cve_st_bienfid= ? AND \n";
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(new String()))) {
                sqlFiltro += "bie_num_contrato IN (SELECT cto_num_contrato FROM SAF.Contrato WHERE cto_num_nivel4= ?) AND \n";
            }

            if (sqlFiltro.contains("AND")) {
                sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
                sqlComando = sqlComando.concat(sqlFiltro);
            }

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setInt(sqlParam, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if ((cbc.getCriterioBienFideTipo() != null) && (!cbc.getCriterioBienFideTipo().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioBienFideTipo());
            }
            if ((cbc.getCriterioBienFideClas() != null) && (!cbc.getCriterioBienFideClas().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioBienFideClas());
            }
            if ((cbc.getCriterioStatus() != null) && (!cbc.getCriterioStatus().equals(new String()))) {
                sqlParam++;
                pstmt.setString(sqlParam, cbc.getCriterioStatus());
            }
            if ((cbc.getCriterioPlaza() != null) && (!cbc.getCriterioPlaza().equals(0))) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFide_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }

    public Integer onBienFide_ObtenBienId(long contratoNumero, int contratoNumeroSub, String bienFideTipo) throws SQLException {
        int bienFideId = 0;
        try {
            String sqlComando = "SELECT Nvl(Max(bie_id_bien),0) maxId \n"
                    + "FROM   SAF.BienFide                  \n"
                    + "WHERE  bie_num_contrato  = ?         \n"
                    + "AND    bie_sub_contrato  = ?         \n"
                    + "AND    bie_cve_tipo_bien = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setInt(2, contratoNumeroSub);
            pstmt.setString(3, bienFideTipo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bienFideId = rs.getInt("maxId") + 1;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFide_ObtenBienId()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return bienFideId;
    }

    public Boolean onBienFide_Administra(ContabilidadBienFideBean bf) {
        try {
            bf.setBienFideDescripcion(bf.getBienFideDescripcion().replace("\r\n", "\n"));
            bf.setBienFideComentario(bf.getBienFideComentario().replace("\r\n", "\n"));

            String sqlComando = "{CALL db2Fiduc.SP_GEN_BIENFIDE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("PSM_ID_BIEN", bf.getBienFideId());
            cstmt.setLong("PI_FISO", bf.getBienFideContratoNum());
            cstmt.setInt("PSM_SUBFISO", bf.getBienFideContratoNumSub());
            cstmt.setString("PS_TIPO_BIEN", bf.getBienFideTipo());
            cstmt.setString("PS_CLASIF_BIEN", bf.getBienFideClasificacion());
            cstmt.setDouble("PN_IMPORTE", bf.getBienFideImporte());
            cstmt.setInt("PSM_MDA", bf.getBienFideMonedaNum());
            cstmt.setDouble("PN_TC", bf.getBienFideImporteTC());
            cstmt.setString("PS_PERIODICIDAD", bf.getBienFidePeriodicidadRev());
            cstmt.setDate("PDT_FEC_ULT_VAL", bf.getBienFideFechaValUlt());
            cstmt.setDate("PDT_FEC_PROX_VAL", bf.getBienFideFechaValProx());
            cstmt.setDouble("PN_IMP_ULT_VAL", bf.getBienFideImporteUltVal());
            cstmt.setString("PS_DESCRIPCION", bf.getBienFideDescripcion().toUpperCase(Locale.ENGLISH));
            cstmt.setString("PS_COMENTARIO", bf.getBienFideComentario().toUpperCase(Locale.ENGLISH));
            cstmt.setString("PS_TIPO_OPERACION", bf.getBienFideBitTipoOperacion());
            cstmt.setDate("PDT_FECHA", bf.getBienFideFechaValor());
            cstmt.setInt("PI_USUARIO", bf.getBienFideBitUsuario());
            cstmt.setString("PS_TERMINAL", bf.getBienFideBitTerminal());
            cstmt.setString("PS_PANTALLA", bf.getBienFideBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onBienFide_Administra()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   G A R A N T I A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onGarantia_ObtenMaxBienId(ContabilidadGarantiaBienBean cgb)  {
        try {
            conexion = DataBaseConexion.getInstance().getConnection();
            if (cgb.getGarantiaBienTipo().equals("INMUEBLES")) {
                String sqlComando = "SELECT COALESCE(MAX(GRI_NUM_INMUEBLE),0)+1 MaxId  \n"
                        + "FROM   SAF.GRAINMU \n"
                        + "WHERE  GRI_NUM_CONTRATO = ? \n"
                        + "AND    GRI_SUB_CONTRATO = ?";
                pstmt = conexion.prepareStatement(sqlComando);
            }
            if (cgb.getGarantiaBienTipo().equals("BIENES MUEBLES")) {
                String sqlComando = "SELECT COALESCE(MAX(GRM_NUM_BIE_MUEBLE),0)+1 MaxId \n"
                        + "FROM   SAF.GARMUEBL         \n"
                        + "WHERE  GRM_NUM_CONTRATO = ? \n"
                        + "AND    GRM_SUB_CONTRATO = ?";
                pstmt = conexion.prepareStatement(sqlComando);
            }
            if (cgb.getGarantiaBienTipo().equals("OTROS BIENES")) {
                String sqlComando = "SELECT COALESCE(MAX(GRN_NUM_NOBURSATIL),0)+1 MaxId \n"
                        + "FROM   SAF.GARNBURS         \n"
                        + "WHERE  GRN_NUM_CONTRATO = ? \n"
                        + "AND    GRN_SUB_CONTRATO = ?";
                pstmt = conexion.prepareStatement(sqlComando);
            }
            pstmt.setLong(1, cgb.getGarantiaContratoNumero());
            pstmt.setInt(2, cgb.getGarantiaContratoNumeroSub());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cgb.setGarantiaBienId(rs.getShort("MaxId"));
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onGarantia_ObtenMaxBienId()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public Boolean onGarantia_AdministraBien(ContabilidadGarantiaBienBean garBien)  {
        try {
            String sqlComando = "{CALL db2Fiduc.SP_GEN_GTIA_BIENES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setShort("PSM_ID_BIEN", garBien.getGarantiaBienId());
            cstmt.setLong("PI_FISO", garBien.getGarantiaContratoNumero());
            cstmt.setInt("PSM_SUBFISO", garBien.getGarantiaContratoNumeroSub());
            cstmt.setString("PS_TIPO_BIEN", garBien.getGarantiaBienTipo());
            cstmt.setString("PS_CLASIF_BIEN", garBien.getGarantiaBienClasif());
            cstmt.setDouble("PN_IMPORTE", garBien.getGarantiaBienImporte());
            cstmt.setShort("PSM_MDA", garBien.getGarantiaBienMonedaNum());
            cstmt.setDouble("PN_TC", garBien.getGarantiaBienImporteTC());
            cstmt.setShort("PSM_REVALUA", garBien.getGarantiaBienCveRevalua());
            cstmt.setString("PS_PERIODICIDAD", garBien.getGarantiaBienPeriodicidad());
            cstmt.setDate("PDT_FEC_ULT_VAL", garBien.getGarantiaBienFechaUltVal());
//            cstmt.setDate  ("PDT_FEC_ULT_VAL"  , garBien.getGarantiaBienFecha());
            cstmt.setDate("PDT_FEC_PROX_VAL", garBien.getGarantiaBienFechaPrxVal());
            cstmt.setDouble("PN_IMP_ULT_VAL", garBien.getGarantiaBienImporteUltVal());
            cstmt.setString("PS_DESCRIPCION", garBien.getGarantiaBienDescripcion().toUpperCase(Locale.ENGLISH).replaceAll("\r\n", "\n"));
            cstmt.setString("PS_COMENTARIO", garBien.getGarantiaBienComentario().toUpperCase(Locale.ENGLISH).replaceAll("\r\n", "\n"));
            cstmt.setString("PS_ESTATUS", garBien.getGarantiaBienStatus());
            cstmt.setString("PS_TIPO_OPERACION", garBien.getGarantiaBitTipoOper());
            cstmt.setDate("PDT_FECHA", garBien.getGarantiaBienFecha());
            cstmt.setInt("PI_USUARIO", garBien.getGarantiaBitUsuario());
            cstmt.setString("PS_TERMINAL", garBien.getGarantiaBitIP());
            cstmt.setString("PS_PANTALLA", garBien.getGarantiaBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onGarantia_AdministraBien()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onGarantia_Administra(ContabilidadGarantiaGralBean gar) {
        try {
            gar.setGarantiaDesc(gar.getGarantiaDesc().replace("\r\n", "\n"));
            gar.setGarantiaComentario(gar.getGarantiaComentario().replace("\r\n", "\n"));
            String sqlComando = "{CALL db2Fiduc.SP_GEN_GARANTIA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("PI_FISO", gar.getGarantiaContratoNumero());
            cstmt.setInt("PSM_SUBFISO", gar.getGarantiaContratoNumeroSub());
            cstmt.setInt("PSM_INMUEBLES", Integer.parseInt(gar.getGarantiaTipoInmuebles()));
            cstmt.setInt("PSM_MUEBLES", Integer.parseInt(gar.getGarantiaTipoMuebles()));
            cstmt.setInt("PSM_VALORES", Integer.parseInt(gar.getGarantiaTipoValores()));
            cstmt.setInt("PSM_OTROS", Integer.parseInt(gar.getGarantiaTipoOtros()));
            cstmt.setInt("PSM_NUMERARIO", Integer.parseInt(gar.getGarantiaTipoNumerario()));
            cstmt.setDouble("PN_IMP_CREDITO", gar.getGarantiaImporteCredito());
            cstmt.setInt("PSM_MDA", gar.getGarantiaMonedaNumero());
            cstmt.setDate("PDT_FEC_INI", gar.getGarantiaRevaluaFechaIni());
            cstmt.setDate("PDT_FEC_FIN", gar.getGarantiaRevaluaFechaFin());
            cstmt.setString("PS_RELACION", gar.getGarantiaRelacionCredGar());
            cstmt.setString("PS_DESCRIPCION", gar.getGarantiaDesc().toUpperCase(Locale.ENGLISH));
            cstmt.setString("PS_COMENTARIO", gar.getGarantiaComentario().toUpperCase(Locale.ENGLISH));
            cstmt.setString("PS_ESTATUS", gar.getGarantiaStatus());
            cstmt.setString("PS_TIPO_OPERACION", gar.getGarantiaBitTipoOper());
            cstmt.setDate("PDT_FECHA", gar.getGarantiaFecha());
            cstmt.setInt("PI_USUARIO", gar.getGarantiaBitUsuario());
            cstmt.setString("PS_TERMINAL", gar.getGarantiaBitIP());
            cstmt.setString("PS_PANTALLA", gar.getGarantiaBitPantalla());

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
                    cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                valorRetorno = Boolean.FALSE;
                mensajeError = mensajeErrorSP;
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onGarantia_Administra()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public List<ContabilidadGarantiaBienBean> onGarantia_ConsultaBien(long contratoNumero, int contratoNumeroSub) throws SQLException {
        List<ContabilidadGarantiaBienBean> consulta = new ArrayList<>();
        try {
            //BIENES MUEBLES  
            String sqlComando = "SELECT Tipo, Sec, Clasif, Moneda, Importe, ImpUltVal, Comentario, Descripcion, FecPrxVal, FecUltVal, Status, CveRevalua, Per \n"
                    + "FROM  (SELECT 'BIENES MUEBLES' Tipo, grm_num_bie_mueble Sec, grm_cve_tipo_mueb Clasif,\n"
                    + "              mon_nom_moneda Moneda, grm_imp_valor Importe, grm_imp_ult_valua ImpUltVal,\n"
                    + "              grm_tex_comentario Comentario, grm_texto_descrip Descripcion, grm_fec_sig_valua FecPrxVal, \n"
                    + "              grm_fec_ult_valua FecUltVal, grm_cve_st_mueble Status, grm_cve_revalua CveRevalua,\n"
                    + "              Case When grm_cve_per_valua= '' or grm_cve_per_valua Is Null Then 'NINGUNO' Else grm_cve_per_valua End Per \n"
                    + "       FROM   SAF.GarMuebl                          \n"
                    + "       JOIN   SAF.Gara_Tipo_Moneda                  \n"
                    + "       ON     grm_num_contrato   = grn_num_contrato \n"
                    + "       AND    grm_sub_contrato   = grn_sub_contrato \n"
                    + "       AND    grm_num_bie_mueble = gra_num_garantia \n"
                    + "       JOIN   SAF.Monedas                           \n"
                    + "       ON     gra_num_moneda     = mon_num_pais     \n"
                    + "       WHERE  gra_tipo_garantia  = ?                \n"
                    + "       AND    grm_num_contrato   = ?                \n"
                    + "       AND    grm_sub_contrato   = ?)               \n"
                    + "ORDER  BY 1,2";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "MUEBLE");
            pstmt.setLong(2, contratoNumero);
            pstmt.setInt(3, contratoNumeroSub);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadGarantiaBienBean gb = new ContabilidadGarantiaBienBean();
                gb.setGarantiaBienId(rs.getShort("Sec"));
                gb.setGarantiaBienTipo(rs.getString("Tipo"));
                gb.setGarantiaBienClasif(rs.getString("Clasif"));
                gb.setGarantiaBienImporte(rs.getDouble("Importe"));
                gb.setGarantiaBienCveRevalua(rs.getShort("CveRevalua"));
                gb.setGarantiaBienMonedaNom(rs.getString("Moneda"));
                gb.setGarantiaBienPeriodicidad(rs.getString("Per"));
                gb.setGarantiaBienImporteUltVal(rs.getDouble("ImpUltVal"));
                gb.setGarantiaBienFechaUltVal(new java.sql.Date(formatFechaParse(rs.getString("FecUltVal")).getTime()));
                gb.setGarantiaBienFechaPrxVal(rs.getDate("FecPrxVal"));
                gb.setGarantiaBienDescripcion(rs.getString("Descripcion"));
                gb.setGarantiaBienComentario(rs.getString("Comentario"));
                gb.setGarantiaBienStatus(rs.getString("Status"));
                consulta.add(gb);
            }
            rs.close();
            pstmt.close(); 

            //INMUEBLES
            sqlComando = "SELECT Tipo, Sec, Clasif, Moneda, Importe, ImpUltVal, Comentario, Desc, FecPrxVal, FecUltVal, Status, CveRevalua, Per \n"
                    + "FROM  (SELECT 'INMUEBLES' Tipo, gri_num_inmueble Sec, gri_cve_tipo_inmu Clasif,                   \n"
                    + "              mon_nom_moneda Moneda, gri_imp_inmueble Importe, gri_imp_ult_valua ImpUltVal,       \n"
                    + "              gri_tex_comentario Comentario, gri_texto_descrip Desc, gri_fec_sig_valua FecPrxVal, \n"
                    + "              gri_fec_ult_valua FecUltVal, gri_cve_status Status,  gri_cve_revalua CveRevalua,    \n"
                    + "              Case When gri_cve_per_valua= '' or gri_cve_per_valua Is Null Then 'NINGUNO' Else gri_cve_per_valua End Per \n"
                    + "       FROM   SAF.GraInmu                          \n"
                    + "       JOIN   SAF.Gara_Tipo_Moneda                 \n"
                    + "       ON     gri_num_contrato  = grn_num_contrato \n"
                    + "       AND    gri_sub_contrato  = grn_sub_contrato \n"
                    + "       AND    gri_num_inmueble  = gra_num_garantia \n"
                    + "       JOIN   SAF.Monedas                          \n"
                    + "       ON     gra_num_moneda    = mon_num_pais     \n"
                    + "       WHERE  gra_tipo_garantia = ?                \n"
                    + "       AND    gri_num_contrato  = ?                \n"
                    + "       AND    gri_sub_contrato  = ?)               \n"
                    + "ORDER  BY 1,2";
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "INMUEBLE");
            pstmt.setLong(2, contratoNumero);
            pstmt.setInt(3, contratoNumeroSub);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadGarantiaBienBean gb = new ContabilidadGarantiaBienBean();
                gb.setGarantiaBienId(rs.getShort("Sec"));
                gb.setGarantiaBienTipo(rs.getString("Tipo"));
                gb.setGarantiaBienClasif(rs.getString("Clasif"));
                gb.setGarantiaBienImporte(rs.getDouble("Importe"));
                gb.setGarantiaBienCveRevalua(rs.getShort("CveRevalua"));
                gb.setGarantiaBienMonedaNom(rs.getString("Moneda"));
                gb.setGarantiaBienPeriodicidad(rs.getString("Per"));
                gb.setGarantiaBienImporteUltVal(rs.getDouble("ImpUltVal"));
                gb.setGarantiaBienFechaUltVal(new java.sql.Date(formatFechaParse(rs.getString("FecUltVal")).getTime()));
                gb.setGarantiaBienFechaPrxVal(rs.getDate("FecPrxVal"));
                gb.setGarantiaBienDescripcion(rs.getString("Desc"));
                gb.setGarantiaBienComentario(rs.getString("Comentario"));
                gb.setGarantiaBienStatus(rs.getString("Status"));
                consulta.add(gb);
            }
            rs.close();
            pstmt.close();

            //OTROS BIENES  
            sqlComando = "SELECT Tipo, Sec, Clasif, Moneda, Importe, ImpUltVal, Comentario, Descripcion, FecPrxVal, FecUltVal, Status, CveRevalua, Per \n"
                    + "FROM  (SELECT 'OTROS BIENES' Tipo, grn_num_nobursatil Sec, grn_cve_tipo_garan Clasif,\n"
                    + "              mon_nom_moneda Moneda, grn_imp_documento Importe, grn_imp_ult_valua ImpUltVal,\n"
                    + "              grn_tex_comentario Comentario, grn_texto_descrip Descripcion, grn_fec_sig_valua FecPrxVal, \n"
                    + "              grn_fec_ult_valua FecUltVal, grn_cve_st_nobursa Status, grn_cve_revalua CveRevalua,\n"
                    + "              Case When grn_cve_per_valua= '' or grn_cve_per_valua Is Null Then 'NINGUNO' Else grn_cve_per_valua End Per \n"
                    + "       FROM   SAF.GarnBurs         gb                 \n"
                    + "       JOIN   SAF.Gara_Tipo_Moneda gtm                \n"
                    + "       ON     gb.grn_num_contrato   = gtm.grn_num_contrato \n"
                    + "       AND    gb.grn_sub_contrato   = gtm.grn_sub_contrato \n"
                    + "       AND    grn_num_nobursatil = gra_num_garantia   \n"
                    + "       JOIN   SAF.Monedas                             \n"
                    + "       ON     gra_num_moneda      = mon_num_pais      \n"
                    + "       WHERE  gra_tipo_garantia   = ?                 \n"
                    + "       AND    gb.grn_num_contrato = ?                 \n"
                    + "       AND    gb.grn_sub_contrato = ?)                \n"
                    + "ORDER  BY 1,2";
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "OTROS BIENES");
            pstmt.setLong(2, contratoNumero);
            pstmt.setInt(3, contratoNumeroSub);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadGarantiaBienBean gb = new ContabilidadGarantiaBienBean();
                gb.setGarantiaBienId(rs.getShort("Sec"));
                gb.setGarantiaBienTipo(rs.getString("Tipo"));
                gb.setGarantiaBienClasif(rs.getString("Clasif"));
                gb.setGarantiaBienImporte(rs.getDouble("Importe"));
                gb.setGarantiaBienCveRevalua(rs.getShort("CveRevalua"));
                gb.setGarantiaBienMonedaNom(rs.getString("Moneda"));
                gb.setGarantiaBienPeriodicidad(rs.getString("Per"));
                gb.setGarantiaBienImporteUltVal(rs.getDouble("ImpUltVal"));
                gb.setGarantiaBienFechaUltVal(new java.sql.Date(formatFechaParse(rs.getString("FecUltVal")).getTime()));
                gb.setGarantiaBienFechaPrxVal(rs.getDate("FecPrxVal"));
                gb.setGarantiaBienDescripcion(rs.getString("Descripcion"));
                gb.setGarantiaBienComentario(rs.getString("Comentario"));
                gb.setGarantiaBienStatus(rs.getString("Status"));
                consulta.add(gb);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public int getGuaranteesTotalRows(CriterioBusquedaContaBean cb) {
        int totalRows = 0;

        try {
            sqlParam = 0;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.Garantia           g   \n"
                    + "JOIN   SAF.GARA_TIPO_MONEDA   gtm \n"
                    + "ON     g.grn_num_contrato = gtm.grn_num_contrato \n"
                    + "AND    g.grn_sub_contrato = gtm.grn_sub_contrato \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     gtm.gra_num_moneda = mon_num_pais \n"
                    + "JOIN   SAF.VISTA_USUARIO V ON g.GRN_NUM_CONTRATO = CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "WHERE  gra_tipo_garantia  = ?            \n";
            if (cb.getCriterioPlaza() != null) {
                sqlComando += " AND    V.CTO_NUM_NIVEL4 = " + cb.getCriterioPlaza() + "\n";
            }
            if (cb.getCriterioAX1() != null) {
                sqlComando += "AND g.grn_num_contrato = " + cb.getCriterioAX1() + " \n";
            }
            if (cb.getCriterioAX2() != null) {
                sqlComando += "AND g.grn_sub_contrato = " + cb.getCriterioAX2() + " \n";
            }
            if (cb.getCriterioCheckInm().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_inmuebles <> 0 \n";
            }
            if (cb.getCriterioCheckMue().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_muebles <> 0 \n";
            }
            if (cb.getCriterioCheckOtr().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_otros <> 0 \n";
            }
            if (cb.getCriterioStatus() != null) {
                sqlComando += "AND grn_cve_status = '" + cb.getCriterioStatus() + "' \n";
            }

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, cb.getCriterioUsuario());
            pstmt.setString(2, "GARANTIA");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onGarantia_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }

        return totalRows;
    }

    public List<ContabilidadGarantiaGralBean> getGuaranteesPaginated(CriterioBusquedaContaBean cb, String offset) {
        String sqlComandoGuarantees = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadGarantiaGralBean> consulta = new ArrayList<>();
        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlComandoGuarantees = "SELECT cto, subCto,grn_cve_status, grn_relacion, mon_nom_moneda, gra_num_moneda, \n"
                    + "       cveVal,cveOtr,cveInm,cveMue,cveNum,cveRev,                \n"
                    + "       grn_imp_garanti, grn_imp_ult_valua, grn_imp_garantizad,      \n"
                    + "       grn_fec_dia_inicio, grn_fec_mes_inicio, grn_fec_ano_inicio,  \n"
                    + "       grn_fec_dia_fin, grn_fec_mes_fin, grn_fec_ano_fin,           \n"
                    + "       grn_fec_ult_valua, grn_tex_garantia, grn_tex_comentario \n"
                    + "FROM ( \n"
                    + "  SELECT ROW_NUMBER() OVER (ORDER BY 1,2) AS Ind, \n"
                    + "       g.grn_num_contrato cto, g.grn_sub_contrato subCto,           \n"
                    + "       grn_cve_status, grn_relacion, mon_nom_moneda, gra_num_moneda,\n"
                    + "       Decode(grn_cve_valores,0,'NO','SI')   cveVal,                \n"
                    + "       Decode(grn_cve_otros,0,'NO','SI')     cveOtr,                \n"
                    + "       Decode(grn_cve_inmuebles,0,'NO','SI') cveInm,                \n"
                    + "       Decode(grn_cve_muebles,0,'NO','SI')   cveMue,                \n"
                    + "       Decode(grn_cve_numerario,0,'NO','SI') cveNum,                \n"
                    + "       Decode(grn_cve_revalua,0,'NO','SI')   cveRev,                \n"
                    + "       grn_imp_garanti, grn_imp_ult_valua, grn_imp_garantizad,      \n"
                    + "       grn_fec_dia_inicio, grn_fec_mes_inicio, grn_fec_ano_inicio,  \n"
                    + "       grn_fec_dia_fin, grn_fec_mes_fin, grn_fec_ano_fin,           \n"
                    + "       grn_fec_ult_valua, grn_tex_garantia, grn_tex_comentario      \n"
                    + "FROM   SAF.Garantia           g   \n"
                    + "JOIN   SAF.GARA_TIPO_MONEDA   gtm \n"
                    + "ON     g.grn_num_contrato = gtm.grn_num_contrato \n"
                    + "AND    g.grn_sub_contrato = gtm.grn_sub_contrato \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     gtm.gra_num_moneda = mon_num_pais \n"
                    + "JOIN   SAF.VISTA_USUARIO V ON g.GRN_NUM_CONTRATO = CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "WHERE  gra_tipo_garantia  = ?            \n";
            if (cb.getCriterioPlaza() != null) {
                sqlComandoGuarantees += " AND    V.CTO_NUM_NIVEL4 = " + cb.getCriterioPlaza() + "\n";
            }
            if (cb.getCriterioAX1() != null) {
                sqlComandoGuarantees += "AND g.grn_num_contrato = " + cb.getCriterioAX1() + " \n";
            }
            if (cb.getCriterioAX2() != null) {
                sqlComandoGuarantees += "AND g.grn_sub_contrato = " + cb.getCriterioAX2() + " \n";
            }
            if (cb.getCriterioCheckInm().equals(Boolean.TRUE)) {
                sqlComandoGuarantees += "AND grn_cve_inmuebles <> 0 \n";
            }
            if (cb.getCriterioCheckMue().equals(Boolean.TRUE)) {
                sqlComandoGuarantees += "AND grn_cve_muebles <> 0 \n";
            }
            if (cb.getCriterioCheckOtr().equals(Boolean.TRUE)) {
                sqlComandoGuarantees += "AND grn_cve_otros <> 0 \n";
            }
            if (cb.getCriterioStatus() != null) {
                sqlComandoGuarantees += "AND grn_cve_status = '" + cb.getCriterioStatus() + "' \n";
            }

            sqlComandoGuarantees += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoGuarantees += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoGuarantees += "AND ".concat(Integer.toString(and));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGuarantees);
            preparedStatement.setInt(1, cb.getCriterioUsuario());
            preparedStatement.setString(2, "GARANTIA");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadGarantiaGralBean gar = new ContabilidadGarantiaGralBean();
                gar.setGarantiaContratoNumero(resultSet.getLong("cto"));
                gar.setGarantiaContratoNumeroSub(resultSet.getInt("subCto"));
                gar.setGarantiaTipoValores(resultSet.getString("cveVal"));
                gar.setGarantiaTipoOtros(resultSet.getString("cveOtr"));
                gar.setGarantiaTipoInmuebles(resultSet.getString("cveInm"));
                gar.setGarantiaTipoMuebles(resultSet.getString("cveMue"));
                gar.setGarantiaTipoNumerario(resultSet.getString("cveNum"));
                gar.setGarantiaMonedaNombre(resultSet.getString("mon_nom_moneda"));
                gar.setGarantiaMonedaNumero(resultSet.getInt("gra_num_moneda"));
                gar.setGarantiaRelacionCredGar(resultSet.getString("grn_relacion"));
                //gar.setGarantiaRevaluaCve(resultSet.getString("cveRev"));
                gar.setGarantiaImporteCredito(resultSet.getDouble("grn_imp_garanti"));
                gar.setGarantiaImporteGarantia(resultSet.getDouble("grn_imp_garantizad"));
                gar.setGarantiaImporteUltVal(resultSet.getDouble("grn_imp_ult_valua"));
                gar.setGarantiaStatus(resultSet.getString("grn_cve_status"));
                gar.setGarantiaComentario(resultSet.getString("grn_tex_comentario"));
                gar.setGarantiaDesc(resultSet.getString("grn_tex_garantia"));
                if ((resultSet.getInt("grn_fec_dia_inicio")) > 0 && (resultSet.getInt("grn_fec_mes_inicio")) > 0 && (resultSet.getInt("grn_fec_ano_inicio")) > 0) {
                    String fechaIni = String.valueOf(resultSet.getInt("grn_fec_dia_inicio")).concat("/")
                            + String.valueOf(resultSet.getInt("grn_fec_mes_inicio")).concat("/")
                            + String.valueOf(resultSet.getInt("grn_fec_ano_inicio"));
                    gar.setGarantiaRevaluaFechaIni(new java.sql.Date(formatFechaParse(fechaIni).getTime()));
                }
                if ((resultSet.getInt("grn_fec_dia_fin")) > 0 && (resultSet.getInt("grn_fec_mes_fin")) > 0 && (resultSet.getInt("grn_fec_ano_fin")) > 0) {
                    String fechaFin = String.valueOf(resultSet.getInt("grn_fec_dia_fin")).concat("/")
                            + String.valueOf(resultSet.getInt("grn_fec_mes_fin")).concat("/")
                            + String.valueOf(resultSet.getInt("grn_fec_ano_fin"));
                    gar.setGarantiaRevaluaFechaFin(new java.sql.Date(formatFechaParse(fechaFin).getTime()));
                }
                consulta.add(gar);
            }
        } catch (NumberFormatException | SQLException e) {
            logger.error(e.getMessage() + e.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getGuaranteesPaginated:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getGuaranteesPaginated:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getGuaranteesPaginated:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }

        return consulta;
    }

    public List<ContabilidadGarantiaGralBean> onGarantia_Consulta(CriterioBusquedaContaBean cb)  {
        List<ContabilidadGarantiaGralBean> consulta = new ArrayList<>();
        try {
            sqlParam = 0;
            String sqlComando = "SELECT g.grn_num_contrato cto, g.grn_sub_contrato subCto,           \n"
                    + "       grn_cve_status, grn_relacion, mon_nom_moneda, gra_num_moneda,\n"
                    + "       Decode(grn_cve_valores,0,'NO','SI')   cveVal,                \n"
                    + "       Decode(grn_cve_otros,0,'NO','SI')     cveOtr,                \n"
                    + "       Decode(grn_cve_inmuebles,0,'NO','SI') cveInm,                \n"
                    + "       Decode(grn_cve_muebles,0,'NO','SI')   cveMue,                \n"
                    + "       Decode(grn_cve_numerario,0,'NO','SI') cveNum,                \n"
                    + "       Decode(grn_cve_revalua,0,'NO','SI')   cveRev,                \n"
                    + "       grn_imp_garanti, grn_imp_ult_valua, grn_imp_garantizad,      \n"
                    + "       grn_fec_dia_inicio, grn_fec_mes_inicio, grn_fec_ano_inicio,  \n"
                    + "       grn_fec_dia_fin, grn_fec_mes_fin, grn_fec_ano_fin,           \n"
                    + "       grn_fec_ult_valua, grn_tex_garantia, grn_tex_comentario      \n"
                    + "FROM   SAF.Garantia           g   \n"
                    + "JOIN   SAF.GARA_TIPO_MONEDA   gtm \n"
                    + "ON     g.grn_num_contrato = gtm.grn_num_contrato \n"
                    + "AND    g.grn_sub_contrato = gtm.grn_sub_contrato \n"
                    + "JOIN   SAF.Monedas                       \n"
                    + "ON     gtm.gra_num_moneda = mon_num_pais \n"
                    + "JOIN   SAF.VISTA_USUARIO V ON g.GRN_NUM_CONTRATO = CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "WHERE  gra_tipo_garantia  = ?            \n";
            if (cb.getCriterioPlaza() != null) {
                sqlComando += " AND    V.CTO_NUM_NIVEL4 = " + cb.getCriterioPlaza() + "\n";
            }
            if (cb.getCriterioAX1() != null) {
                sqlComando += "AND g.grn_num_contrato = " + cb.getCriterioAX1() + " \n";
            }
            if (cb.getCriterioAX2() != null) {
                sqlComando += "AND g.grn_sub_contrato = " + cb.getCriterioAX2() + " \n";
            }
            if (cb.getCriterioCheckInm().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_inmuebles <> 0 \n";
            }
            if (cb.getCriterioCheckMue().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_muebles <> 0 \n";
            }
            if (cb.getCriterioCheckOtr().equals(Boolean.TRUE)) {
                sqlComando += "AND grn_cve_otros <> 0 \n";
            }
            if (cb.getCriterioStatus() != null) {
                sqlComando += "AND grn_cve_status = '" + cb.getCriterioStatus() + "' \n";
            }else{
                sqlComando += "AND grn_cve_status = 'ACTIVO' \n";
            }
            sqlComando += "ORDER  BY 1,2";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, cb.getCriterioUsuario());
            pstmt.setString(2, "GARANTIA");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadGarantiaGralBean gar = new ContabilidadGarantiaGralBean();
                gar.setGarantiaContratoNumero(rs.getLong("cto"));
                gar.setGarantiaContratoNumeroSub(rs.getInt("subCto"));
                gar.setGarantiaTipoValores(rs.getString("cveVal"));
                gar.setGarantiaTipoOtros(rs.getString("cveOtr"));
                gar.setGarantiaTipoInmuebles(rs.getString("cveInm"));
                gar.setGarantiaTipoMuebles(rs.getString("cveMue"));
                gar.setGarantiaTipoNumerario(rs.getString("cveNum"));
                gar.setGarantiaMonedaNombre(rs.getString("mon_nom_moneda"));
                gar.setGarantiaMonedaNumero(rs.getInt("gra_num_moneda"));
                gar.setGarantiaRelacionCredGar(rs.getString("grn_relacion"));
                //gar.setGarantiaRevaluaCve(rs.getString("cveRev"));
                gar.setGarantiaImporteCredito(rs.getDouble("grn_imp_garanti"));
                gar.setGarantiaImporteGarantia(rs.getDouble("grn_imp_garantizad"));
                gar.setGarantiaImporteUltVal(rs.getDouble("grn_imp_ult_valua"));
                gar.setGarantiaStatus(rs.getString("grn_cve_status"));
                gar.setGarantiaComentario(rs.getString("grn_tex_comentario"));
                gar.setGarantiaDesc(rs.getString("grn_tex_garantia"));
                if ((rs.getInt("grn_fec_dia_inicio")) > 0 && (rs.getInt("grn_fec_mes_inicio")) > 0 && (rs.getInt("grn_fec_ano_inicio")) > 0) {
                    String fechaIni = String.valueOf(rs.getInt("grn_fec_dia_inicio")).concat("/")
                            + String.valueOf(rs.getInt("grn_fec_mes_inicio")).concat("/")
                            + String.valueOf(rs.getInt("grn_fec_ano_inicio"));
                    gar.setGarantiaRevaluaFechaIni(new java.sql.Date(formatFechaParse(fechaIni).getTime()));
                }
                if ((rs.getInt("grn_fec_dia_fin")) > 0 && (rs.getInt("grn_fec_mes_fin")) > 0 && (rs.getInt("grn_fec_ano_fin")) > 0) {
                    String fechaFin = String.valueOf(rs.getInt("grn_fec_dia_fin")).concat("/")
                            + String.valueOf(rs.getInt("grn_fec_mes_fin")).concat("/")
                            + String.valueOf(rs.getInt("grn_fec_ano_fin"));
                    gar.setGarantiaRevaluaFechaFin(new java.sql.Date(formatFechaParse(fechaFin).getTime()));
                }
                consulta.add(gar);
            }
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   P O L I Z A   M A N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadMovtoBean> onPolizaMan_Consulta(CriterioBusquedaContaBean cbc) {
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso,       \n"
                    + "       detm_folio_op, tran_id_tran, movi_descripcion, movi_importe,  \n"
                    + "       movi_usuario, movi_status, usu_nom_usuario, oper_id_operacion \n"
                    + "FROM   SAF.FDMovimiento                       \n"
                    + "JOIN   SAF.Usuarios                           \n"
                    + "ON     movi_usuario = usu_num_usuario         \n"
                    + "WHERE  subStr(oper_id_operacion, 1, 1)    = ? \n"
                    + "AND    movi_status                        = ? \n"
                    + "AND    fiso_id_fiso                       = ? \n"
                    + "AND    Extract(Year  FROM movi_fecha_mov) = ? \n"
                    + "AND    Extract(Month FROM movi_fecha_mov) = ? \n"
                    + "AND    Extract(Day   FROM movi_fecha_mov) = ? \n"
                    + "ORDER  BY detm_folio_op DESC\n";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "7");
            pstmt.setString(2, "ACTIVO");
            pstmt.setLong(3, cbc.getCriterioAX1());
            pstmt.setInt(4, cbc.getCriterioFechaYYYY());
            pstmt.setInt(5, cbc.getCriterioFechaMM());
            pstmt.setInt(6, cbc.getCriterioFechaDD());

            //System.out.println(sqlComando);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(rs.getDate("movi_fecha_mov"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoContratoNum(rs.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(rs.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoFolio(rs.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(rs.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(rs.getString("tran_id_tran"));
                movto.setMovtoDesc(rs.getString("movi_descripcion"));
                movto.setMovtoImporte(rs.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(rs.getLong("movi_usuario"));
                movto.setMovtoUsuarioNom(rs.getString("usu_nom_usuario"));
                movto.setMovtoStatus(rs.getString("movi_status"));
                consulta.add(movto);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public String onPolizaMan_ObtenTipo(String operacionId) {
        String polizaManTipo = new String();
        try {
            String sqlComando = "SELECT tip_tipo_operacion FROM SAF.Polizas_Tipo \n"
                    + "WHERE tip_id_operacion = ?"
                    + "  and tip_status = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, operacionId);
            pstmt.setString(2, "ACTIVO");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                polizaManTipo = rs.getString("tip_tipo_operacion");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_ObtenTipo()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return polizaManTipo;
    }
    public Boolean onPolizaMan_Valida_Archivo(String nomArchivo)  {
        Boolean arhivoExiste = false;
        try {
            String sqlComando = "SELECT * from saf.POLIZAS_CTRL_FILES \n" +
                         "WHERE POL_NOM_FILE = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, nomArchivo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                arhivoExiste  = true;
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_Valida_Archivo()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return arhivoExiste;
    }
    public void onPolizaMan_Registra_Archivo(String nomArchivo, Date fecha) {
        try {
            String sqlComando = "insert into saf.POLIZAS_CTRL_FILES \n" +
                         "(POL_NOM_FILE, POL_FECHA) \n" +
                         "VALUES \n" +
                         "(?,?)"; 
                  
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, nomArchivo);
            pstmt.setDate(2, fecha);
            pstmt.execute();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_Registra_Archivo()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public Boolean onPolizaMan_Aplica(ContabilidadPolizaManBean pm)  {
        try {
            //System.out.println("Fecha valor: ".concat(pm.getPolizaEncaFechaVal().toString()));
            //System.out.println("Fecha movto: ".concat(pm.getPolizaEncaFechaMovto().toString()));
            String sqlComando = "{CALL db2Fiduc.SP_POLIZA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("PN_FOLIO_CONT", pm.getPolizaEncaFolio());
            cstmt.setString("PS_NUM_OPERACION", pm.getPolizaEncaNumero());
            cstmt.setDate("PDT_FEC_VAL", pm.getPolizaEncaFechaVal());
            cstmt.setDate("PDT_FEC_MOVTO", pm.getPolizaEncaFechaMovto());
            cstmt.setString("PV_CONCEPTO1", pm.getPoliza00Etiqueta());
            cstmt.setString("PV_VALOR1", pm.getPoliza00Valor());
            cstmt.setString("PV_CONCEPTO2", pm.getPoliza01Etiqueta());
            cstmt.setString("PV_VALOR2", pm.getPoliza01Valor());
            cstmt.setString("PV_CONCEPTO3", pm.getPoliza02Etiqueta());
            cstmt.setString("PV_VALOR3", pm.getPoliza02Valor());
            cstmt.setString("PV_CONCEPTO4", pm.getPoliza03Etiqueta());
            cstmt.setString("PV_VALOR4", pm.getPoliza03Valor());
            cstmt.setString("PV_CONCEPTO5", pm.getPoliza04Etiqueta());
            cstmt.setString("PV_VALOR5", pm.getPoliza04Valor());
            cstmt.setString("PV_CONCEPTO6", pm.getPoliza05Etiqueta());
            cstmt.setString("PV_VALOR6", pm.getPoliza05Valor());
            cstmt.setString("PV_CONCEPTO7", pm.getPoliza06Etiqueta());
            cstmt.setString("PV_VALOR7", pm.getPoliza06Valor());
            cstmt.setString("PV_CONCEPTO8", pm.getPoliza07Etiqueta());
            cstmt.setString("PV_VALOR8", pm.getPoliza07Valor());
            cstmt.setString("PV_CONCEPTO9", pm.getPoliza08Etiqueta());
            cstmt.setString("PV_VALOR9", pm.getPoliza08Valor());
            cstmt.setString("PV_CONCEPTO10", pm.getPoliza09Etiqueta());
            cstmt.setString("PV_VALOR10", pm.getPoliza09Valor());
            cstmt.setString("PV_CONCEPTO11", pm.getPoliza10Etiqueta());
            cstmt.setString("PV_VALOR11", pm.getPoliza10Valor());
            cstmt.setString("PV_CONCEPTO12", pm.getPoliza11Etiqueta());
            cstmt.setString("PV_VALOR12", pm.getPoliza11Valor());
            cstmt.setString("PV_CONCEPTO13", pm.getPoliza12Etiqueta());
            cstmt.setString("PV_VALOR13", pm.getPoliza12Valor());
            cstmt.setString("PV_CONCEPTO14", pm.getPoliza13Etiqueta());
            cstmt.setString("PV_VALOR14", pm.getPoliza13Valor());
            cstmt.setString("PV_CONCEPTO15", pm.getPoliza14Etiqueta());
            cstmt.setString("PV_VALOR15", pm.getPoliza14Valor());
            cstmt.setString("PV_CONCEPTO16", pm.getPoliza15Etiqueta());
            cstmt.setString("PV_VALOR16", pm.getPoliza15Valor());
            cstmt.setString("PV_CONCEPTO17", pm.getPoliza16Etiqueta());
            cstmt.setString("PV_VALOR17", pm.getPoliza16Valor());
            cstmt.setString("PV_CONCEPTO18", pm.getPoliza17Etiqueta());
            cstmt.setString("PV_VALOR18", pm.getPoliza17Valor());
            cstmt.setString("PV_CONCEPTO19", pm.getPoliza18Etiqueta());
            cstmt.setString("PV_VALOR19", pm.getPoliza18Valor());
            cstmt.setString("PV_CONCEPTO20", pm.getPoliza19Etiqueta());
            cstmt.setString("PV_VALOR20", pm.getPoliza19Valor());
            cstmt.setString("PS_REDACCION", pm.getPolizaEncaRedaccion().toUpperCase(Locale.ENGLISH));
            cstmt.setInt("PI_MOVTO_CON_LIQ", Integer.parseInt(pm.getPolizaEncaLiqCveMovto()));
            cstmt.setInt("PI_CONCEPTO_LIQ", pm.getPolizaEncaLiqCveCpto());
            cstmt.setInt("PI_MOVTO_CON_SUG", pm.getPolizaEncaSugerCve());
            cstmt.setString("PS_CVE_PERS_FID", pm.getPolizaEncaFidRol());
            cstmt.setInt("PI_NUM_PERS_FID", pm.getPolizaEncaFidNum());
            cstmt.setString("PS_TIPO_OPERACION", pm.getPolizaEncaBitTipoOper());
            cstmt.setDate("PDT_FECHA", pm.getPolizaEncaFechaMovto());
            cstmt.setInt("PI_USUARIO", pm.getPolizaEncaBitUsuario());
            cstmt.setString("PS_TERMINAL", pm.getPolizaEncaBitTerminal());
            cstmt.setString("PS_PANTALLA", pm.getPolizaEncaBitPantalla());

            cstmt.registerOutParameter("PN_FOLIO_OUT", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            pm.setPolizaEncaFolioSalida(cstmt.getLong("PN_FOLIO_OUT"));
            cstmt.close();
            conexion.close();
            
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
                if(pm.getPolizaEncaFolioSalida() != null && pm.getPoliza00Valor() != null &&  pm.getPolizaEncaNombre() != null){
                setMensajePoliza(" Poliza con folio: ".concat(pm.getPolizaEncaFolioSalida().toString().concat( " Fiso ").concat(pm.getPoliza00Valor()).concat(" ").concat(pm.getPolizaEncaNombre())));
                }
            } else {
                
                mensajeError = mensajeErrorSP;
            }
        } catch (NumberFormatException | SQLException |  NullPointerException Err) {
            mensajeError += "Descripción: " + Err.getMessage()  + "onPolizaMan_Aplica()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public void onPolizaMan_CargaCampos(ContabilidadPolizaManBean pm)  {
        int index = 0;
        String nombre = new String();
        String[] arrCampos = new String[20];
        try {
String sqlComando = "SELECT DISTINCT MONE_ID_MONEDA moneda, GUID_VALOR Clave FROM SAF.FDTRANS T, SAF.FDGUIADET G WHERE T.TRAN_ID_TRAN=G.TRAN_ID_TRAN AND T.TRAN_ID_TRAN IN \n"
                    + "(select TRAN_ID_TRAN from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?) \n"
                    + "union \n"
                    + "select distinct coalesce(C664.CVE_LIMINF_CLAVE,0) moneda, C22.CVE_DESC_CLAVE Clave \n"
                    + "from SAF.FDCATCONTABLE INNER JOIN SAF.claves C22 on c22.CVE_NUM_CLAVE=22 and  (c22.CVE_NUM_SEC_CLAVE=CCON_AX1 or c22.CVE_NUM_SEC_CLAVE=CCON_AX2 or c22.CVE_NUM_SEC_CLAVE=CCON_AX3) \n"
                    + "left outer JOIN SAF.CLAVES c664 On c664.CVE_NUM_CLAVE=664 and c664.CVE_DESC_CLAVE=c22.CVE_DESC_CLAVE \n"
                    + "where (CCON_CTA,CCON_SCTA,CCON_2SCTA,CCON_3SCTA,CCON_4SCTA) \n"
                    + "IN (  SELECT CCON_CTA,CCON_SCTA,CCON_2SCTA,CCON_3SCTA,CCON_4SCTA  FROM SAF.FDGUIADET WHERE TRAN_ID_TRAN \n"
                    + "IN (select TRAN_ID_TRAN  from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?)) \n"
                    + "union \n"
                    + "select distinct 0 moneda, 'PROVEEDOR' Clave from fdtrans where   tran_id_tran in (select TRAN_ID_TRAN \n"
                    + "from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?) and tran_tipo_tran = 3 \n"
                    + "Union select distinct 0 moneda, 'CTOINVERSION' Clave from SAF.fdtrans where tran_id_tran  \n"
                    + "in (select TRAN_ID_TRAN from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?) and tran_tipo_tran = 3   \n"
                    + "union \n"
                    + "select distinct 0 moneda, 'CONTRATO' Clave from fdtrans where   tran_id_tran in (select TRAN_ID_TRAN from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?) and tran_tipo_tran = 3   \n"
                    + "Union \n"
                    + "select distinct 0 moneda, 'SUBCONTRATO' Clave from fdtrans where tran_id_tran in (select TRAN_ID_TRAN from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ?) and tran_tipo_tran = 3   \n"
                    + "union \n"
                    + "select distinct MONE_ID_MONEDA moneda, 'IMPORTE' Clave from fdtrans where   tran_id_tran in (select TRAN_ID_TRAN from SAF.fdDETOPERA WHERE OPER_ID_OPERACION = ? ) and tran_tipo_tran = 3 \n"
                    + "order by 1 ";

            conEstructura = Boolean.FALSE;
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, pm.getPolizaEncaNumero());
            pstmt.setString(2, pm.getPolizaEncaNumero());
            pstmt.setString(3, pm.getPolizaEncaNumero());
            pstmt.setString(4, pm.getPolizaEncaNumero());
            pstmt.setString(5, pm.getPolizaEncaNumero());
            pstmt.setString(6, pm.getPolizaEncaNumero());
            pstmt.setString(7, pm.getPolizaEncaNumero());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                conEstructura = Boolean.TRUE;
                if (rs.getInt("Moneda") <= 0) {
                    nombre = rs.getString("Clave");
                } else {
                    if (String.valueOf(rs.getInt("Moneda")).length() == 1) {
                        nombre = "0".concat(String.valueOf(rs.getInt("Moneda"))).concat(rs.getString("Clave"));
                    }
                    if (String.valueOf(rs.getInt("Moneda")).length() == 2) {
                        nombre = String.valueOf(rs.getInt("Moneda")).concat(rs.getString("Clave"));
                    }
                }
                if (index == 0) {
                    pm.setPoliza00Etiqueta(nombre);
                    pm.setPoliza00Visible("visible;");
                }
                if (index == 1) {
                    pm.setPoliza01Etiqueta(nombre);
                        pm.setPoliza01Visible("visible;");
                }
                if (index == 2) {
                    pm.setPoliza02Etiqueta(nombre);
                    pm.setPoliza02Visible("visible;");
                }
                if (index == 3) {
                    pm.setPoliza03Etiqueta(nombre);
                    pm.setPoliza03Visible("visible;");
                }
                if (index == 4) {
                    pm.setPoliza04Etiqueta(nombre);
                    pm.setPoliza04Visible("visible;");
                }
                if (index == 5) {
                    pm.setPoliza05Etiqueta(nombre);
                    pm.setPoliza05Visible("visible;");
                }
                if (index == 6) {
                    pm.setPoliza06Etiqueta(nombre);
                    pm.setPoliza06Visible("visible;");
                }
                if (index == 7) {
                    pm.setPoliza07Etiqueta(nombre);
                    pm.setPoliza07Visible("visible;");
                }
                if (index == 8) {
                    pm.setPoliza08Etiqueta(nombre);
                    pm.setPoliza08Visible("visible;");
                }
                if (index == 9) {
                    pm.setPoliza09Etiqueta(nombre);
                    pm.setPoliza09Visible("visible;");
                }
                if (index == 10) {
                    pm.setPoliza10Etiqueta(nombre);
                    pm.setPoliza10Visible("visible;");
                }
                if (index == 11) {
                    pm.setPoliza11Etiqueta(nombre);
                    pm.setPoliza11Visible("visible;");
                }
                if (index == 12) {
                    pm.setPoliza12Etiqueta(nombre);
                    pm.setPoliza12Visible("visible;");
                }
                if (index == 13) {
                    pm.setPoliza13Etiqueta(nombre);
                    pm.setPoliza13Visible("visible;");
                }
                if (index == 14) {
                    pm.setPoliza14Etiqueta(nombre);
                    pm.setPoliza14Visible("visible;");
                }
                if (index == 15) {
                    pm.setPoliza15Etiqueta(nombre);
                    pm.setPoliza15Visible("visible;");
                }
                if (index == 16) {
                    pm.setPoliza16Etiqueta(nombre);
                    pm.setPoliza16Visible("visible;");
                }
                if (index == 17) {
                    pm.setPoliza17Etiqueta(nombre);
                    pm.setPoliza17Visible("visible;");
                }
                if (index == 18) {
                    pm.setPoliza18Etiqueta(nombre);
                    pm.setPoliza18Visible("visible;");
                }
                if (index == 19) {
                    pm.setPoliza19Etiqueta(nombre);
                    pm.setPoliza19Visible("visible;");
                }
                arrCampos[index] = nombre.trim();
                index++;
            }
            pm.setPolizaEncaCamposNum(index);
            pm.setPolizaEncaCamposArr(arrCampos);

            onCierraConexion();
            
            if (pm.getPolizaEncaNumero().toString().substring(0, 1).equals("7")) {
                onPolizaMan_CargaCampos2(pm, index, arrCampos);
            }
 
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_CargaCampos()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public void onPolizaMan_CargaCampos2(ContabilidadPolizaManBean polizaM, int index,String[] arrCampos)  {
        String nombre = new String();
        //String[] arrCampos = new String[20];
        try {
            String sqlComando = "SELECT distinct MONE_ID_MONEDA moneda, 'IMPORTE' Clave \n"
                    + "from SAF.FDTrans \n"
                    + "where \n"
                    + "tran_id_tran in (select TRAN_ID_TRAN from fdDETOPERA \n"
                    + "                  WHERE OPER_ID_OPERACION=?) \n";
            if (polizaM.getPolizaEncaNumero().equals("7000007101")
                    || polizaM.getPolizaEncaNumero().equals("7000007106")
                    || polizaM.getPolizaEncaNumero().equals("7000037000")
                    || polizaM.getPolizaEncaNumero().equals("7000039000")
                    || polizaM.getPolizaEncaNumero().equals("7000067000")
                    || polizaM.getPolizaEncaNumero().equals("7000077096")
                    || polizaM.getPolizaEncaNumero().equals("7000580000")) {

                sqlComando = sqlComando
                        + "union \n"
                        + "select distinct MONE_ID_MONEDA moneda, 'BASE RETENCION ISR' Clave from fdtrans where \n"
                        + "tran_id_tran in (select TRAN_ID_TRAN from fdDETOPERA WHERE \n"
                        + "OPER_ID_OPERACION=?) \n";
            }
            if (polizaM.getPolizaEncaNumero().equals("7000036000")
                    || polizaM.getPolizaEncaNumero().equals("7000038000")
                    || polizaM.getPolizaEncaNumero().equals("7000039000")
                    || polizaM.getPolizaEncaNumero().equals("7000580000")) {

                sqlComando = sqlComando
                        + "union \n"
                        + "select distinct MONE_ID_MONEDA moneda, 'BASE RETENCION IVA' Clave from fdtrans where \n"
                        + " tran_id_tran in (select TRAN_ID_TRAN from fdDETOPERA WHERE \n"
                        + "OPER_ID_OPERACION=?)\n";
            }
            //'ENTERO RET. ISR POR PENSIONES Y/O PRIMAS DE ANT                    
            if (polizaM.getPolizaEncaNumero().equals("7000007101")) {
                sqlComando = sqlComando
                        + "union \n"
                        + "select distinct MONE_ID_MONEDA moneda, 'MONTO_GRAVADO' Clave from fdtrans where \n"
                        + "tran_id_tran in (select TRAN_ID_TRAN from fdDETOPERA WHERE \n"
                        + "OPER_ID_OPERACION=7000007101) \n"
                        + "union \n"
                        + "select distinct MONE_ID_MONEDA moneda, 'MONTO_EXENTO' Clave from fdtrans where \n"
                        + "tran_id_tran in (select TRAN_ID_TRAN from fdDETOPERA WHERE \n"
                        + "OPER_ID_OPERACION=7000007101) \n";
            }
            sqlComando = sqlComando
                    + "order by 2 DESC";
            int param = 1;
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setString(param, polizaM.getPolizaEncaNumero());

            if (polizaM.getPolizaEncaNumero().equals("7000007101")
                    || polizaM.getPolizaEncaNumero().equals("7000007106")
                    || polizaM.getPolizaEncaNumero().equals("7000037000")
                    || polizaM.getPolizaEncaNumero().equals("7000039000")
                    || polizaM.getPolizaEncaNumero().equals("7000067000")
                    || polizaM.getPolizaEncaNumero().equals("7000077096")
                    || polizaM.getPolizaEncaNumero().equals("7000580000")) {
                param++;
                pstmt.setString(param, polizaM.getPolizaEncaNumero());
            }
            if (polizaM.getPolizaEncaNumero().equals("7000036000")
                    || polizaM.getPolizaEncaNumero().equals("7000038000")
                    || polizaM.getPolizaEncaNumero().equals("7000039000")
                    || polizaM.getPolizaEncaNumero().equals("7000580000")) {
                param++;
                pstmt.setString(param, polizaM.getPolizaEncaNumero());
            }

            rs = pstmt.executeQuery();
            boolean existe;
            while (rs.next()) {
                if (rs.getInt("Moneda") == 0) {
                    nombre = rs.getString("Clave");
                } else {
                    if (String.valueOf(rs.getInt("Moneda")).length() == 1) {
                        nombre = "0".concat(String.valueOf(rs.getInt("Moneda"))).concat(rs.getString("Clave"));
                    }
                    if (String.valueOf(rs.getInt("Moneda")).length() == 2) {
                        nombre = String.valueOf(rs.getInt("Moneda")).concat(rs.getString("Clave"));
                    }
                }
                existe = false;
                for (int x = 0; x < index; x++) {
                    if (arrCampos[x].equals(nombre.trim())){ 
                        existe = true;
                    }
                    
                }
                if (!existe){
                if (index == 0) {
                    polizaM.setPoliza00Etiqueta(nombre);
                    polizaM.setPoliza00Visible("visible;");
                }
                if (index == 1) {
                    polizaM.setPoliza01Etiqueta(nombre);
                    polizaM.setPoliza01Visible("visible;");
                }
                if (index == 2) {
                    polizaM.setPoliza02Etiqueta(nombre);
                    polizaM.setPoliza02Visible("visible;");
                }
                if (index == 3) {
                    polizaM.setPoliza03Etiqueta(nombre);
                    polizaM.setPoliza03Visible("visible;");
                }
                if (index == 4) {
                    polizaM.setPoliza04Etiqueta(nombre);
                    polizaM.setPoliza04Visible("visible;");
                }
                if (index == 5) {
                    polizaM.setPoliza05Etiqueta(nombre);
                    polizaM.setPoliza05Visible("visible;");
                }
                if (index == 6) {
                    polizaM.setPoliza06Etiqueta(nombre);
                    polizaM.setPoliza06Visible("visible;");
                }
                if (index == 7) {
                    polizaM.setPoliza07Etiqueta(nombre);
                    polizaM.setPoliza07Visible("visible;");
                }
                if (index == 8) {
                    polizaM.setPoliza08Etiqueta(nombre);
                    polizaM.setPoliza08Visible("visible;");
                }
                if (index == 9) {
                    polizaM.setPoliza09Etiqueta(nombre);
                    polizaM.setPoliza09Visible("visible;");
                }
                if (index == 10) {
                    polizaM.setPoliza10Etiqueta(nombre);
                    polizaM.setPoliza10Visible("visible;");
                }
                if (index == 11) {
                    polizaM.setPoliza11Etiqueta(nombre);
                    polizaM.setPoliza11Visible("visible;");
                }
                if (index == 12) {
                    polizaM.setPoliza12Etiqueta(nombre);
                    polizaM.setPoliza12Visible("visible;");
                }
                if (index == 13) {
                    polizaM.setPoliza13Etiqueta(nombre);
                    polizaM.setPoliza13Visible("visible;");
                }
                if (index == 14) {
                    polizaM.setPoliza14Etiqueta(nombre);
                    polizaM.setPoliza14Visible("visible;");
                }
                if (index == 15) {
                    polizaM.setPoliza15Etiqueta(nombre);
                    polizaM.setPoliza15Visible("visible;");
                }
                if (index == 16) {
                    polizaM.setPoliza16Etiqueta(nombre);
                    polizaM.setPoliza16Visible("visible;");
                }
                if (index == 17) {
                    polizaM.setPoliza17Etiqueta(nombre);
                    polizaM.setPoliza17Visible("visible;");
                }
                if (index == 18) {
                    polizaM.setPoliza18Etiqueta(nombre);
                    polizaM.setPoliza18Visible("visible;");
                }
                if (index == 19) {
                    polizaM.setPoliza19Etiqueta(nombre);
                    polizaM.setPoliza19Visible("visible;");
                }
                arrCampos[index] = nombre.trim();
                index++;
                }
            }
            polizaM.setPolizaEncaCamposNum(index);
            polizaM.setPolizaEncaCamposArr(arrCampos);
            
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_CargaCampos2()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    public void onPolizaMan_CargaCamposConDatos(ContabilidadPolizaManBean pm)  {
        try {
            String sqlComando = "SELECT detm_folio_op, datm_secuencia, \n"
                    + "       datm_concepto, datm_valor      \n"
                    + "FROM   SAF.FDDatosMov                 \n"
                    + "WHERE  detm_folio_op         = ?      \n"
                    + "AND    Length(datm_concepto) > ?      \n"
                    + "ORDER  BY 2";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, pm.getPolizaEncaFolio());
            pstmt.setInt(2, 0);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("datm_secuencia").equals("1") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza00Visible("visible;");
                    pm.setPoliza00Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza00Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("2") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza01Visible("visible;");
                    pm.setPoliza01Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza01Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("3") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza02Visible("visible;");
                    pm.setPoliza02Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza02Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("4") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza03Visible("visible;");
                    pm.setPoliza03Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza03Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("5") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza04Visible("visible;");
                    pm.setPoliza04Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza04Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("6") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza05Visible("visible;");
                    pm.setPoliza05Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza05Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("7") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza06Visible("visible;");
                    pm.setPoliza06Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza06Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("8") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza07Visible("visible;");
                    pm.setPoliza07Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza07Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("9") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza08Visible("visible;");
                    pm.setPoliza08Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza08Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("10") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza09Visible("visible;");
                    pm.setPoliza09Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza09Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("11") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza10Visible("visible;");
                    pm.setPoliza10Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza10Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("12") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza11Visible("visible;");
                    pm.setPoliza11Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza11Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("13") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza12Visible("visible;");
                    pm.setPoliza12Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza12Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("14") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza13Visible("visible;");
                    pm.setPoliza13Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza13Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("15") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza14Visible("visible;");
                    pm.setPoliza14Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza14Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("16") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza15Visible("visible;");
                    pm.setPoliza15Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza15Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("17") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza16Visible("visible;");
                    pm.setPoliza16Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza16Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("18") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza17Visible("visible;");
                    pm.setPoliza17Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza17Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("19") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza18Visible("visible;");
                    pm.setPoliza18Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza18Valor(rs.getString("datm_valor"));
                }
                if (rs.getString("datm_secuencia").equals("20") && !rs.getString("datm_concepto").equals("0")) {
                    pm.setPoliza19Visible("visible;");
                    pm.setPoliza19Etiqueta(rs.getString("datm_concepto"));
                    pm.setPoliza19Valor(rs.getString("datm_valor"));
                }
            }
           
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onPolizaMan_CargaCamposConDatos()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C O N S U L T A   A S I E N T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadAsientoBean> onConsAsiento_ConsultaPorRangoFechas(CriterioBusquedaContaAsienBean cba)  {
        List<ContabilidadAsientoBean> consulta = new ArrayList<>();
        try {
            String sqlFiltro = "";
            String sqlComando = "SELECT A.detm_folio_op, A.asie_sec_asiento, A.ccon_cta, A.ccon_scta, A.ccon_2scta, A.ccon_3scta, A.ccon_4scta,  \n"
                    + "       A.asie_ax1, A.asie_ax2, A.asie_ax3, A.asie_car_abo, A.asie_importe, A.asie_descripcion, A.movi_fecha_mov,\n"
                    + "       A.asi_status, A.mone_id_moneda, mon_nom_moneda, A.oper_id_operacion, A.tran_id_tran, A.asie_alta  \n"
                    + "  from (SAF.FDASIENTOS A inner join SAF.FDCATCONTABLE C on A.CCON_cta=C.CCON_cta \n"
                    + "  AND A.CCON_SCTA=C.CCON_SCTA \n"
                    + "  AND a.CCON_2SCTA=C.CCON_2SCTA \n"
                    + "  AND a.CCON_3SCTA=C.CCON_3SCTA\n"
                    + "  AND a.CCON_4SCTA=C.CCON_4SCTA) \n"
                    + "  JOIN  SAF.Monedas ON     mone_id_moneda = mon_num_pais  \n"
                    + "  where A.CCON_cta > 0 \n";
            if (cba.getCriterioCTAM() != null) {
                sqlFiltro += "AND A.ccon_cta       = " + cba.getCriterioCTAM() + " \n AND ";
            }
            if ((cba.getCriterioSC1() != null) && (cba.getCriterioSC1() != 0)) {
                sqlFiltro += "A.ccon_scta      = " + cba.getCriterioSC1() + " \n AND ";
            }
            if ((cba.getCriterioSC2() != null) && (cba.getCriterioSC2() != 0)) {
                sqlFiltro += "A.ccon_2scta     = " + cba.getCriterioSC2() + " \n AND ";
            }
            if ((cba.getCriterioSC3() != null) && (cba.getCriterioSC3() != 0)) {
                sqlFiltro += "A.ccon_3scta     = " + cba.getCriterioSC3() + " \n AND ";
            }
            if (cba.getCriterioAX1() != null) {
                sqlFiltro += "A.asie_ax1       = " + cba.getCriterioAX1() + " \n AND ";
            }
//            if ((cba.getCriterioAX2() != null) && (cba.getCriterioAX2() != 0))     sqlFiltro+= "A.asie_ax2       = "+cba.getCriterioAX2()+" \n AND ";
//            if ((cba.getCriterioAX3() != null) && (cba.getCriterioAX3() != 0))     sqlFiltro+= "A.asie_ax3       = "+cba.getCriterioAX3()+" \n AND ";
            if ((cba.getCriterioAX2() != null)) {
                sqlFiltro += "A.asie_ax2       = " + cba.getCriterioAX2() + " \n AND ";
            }
            if ((cba.getCriterioAX3() != null)) {
                sqlFiltro += "A.asie_ax3       = " + cba.getCriterioAX3() + " \n AND ";
            }
            if (cba.getCriterioMonedaNum() != null) {
                sqlFiltro += "mone_id_moneda = " + cba.getCriterioMonedaNum() + " \n AND ";
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 4);
            sqlComando = sqlComando.concat(sqlFiltro).concat("ORDER BY DETM_FOLIO_OP DESC");

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadAsientoBean asi = new ContabilidadAsientoBean();
                asi.setAsientoFolio(rs.getLong("detm_folio_op"));
                asi.setAsientoSecuencial(rs.getInt("asie_sec_asiento"));
                asi.setAsientoOperacion(rs.getString("oper_id_operacion"));
                asi.setAsientoTransaccion(rs.getString("tran_id_tran"));
                asi.setAsientoFecMov(rs.getDate("movi_fecha_mov"));
                asi.setAsientoCTAM(rs.getShort("ccon_cta"));
                asi.setAsiento1SCTA(rs.getShort("ccon_scta"));
                asi.setAsiento2SCTA(rs.getShort("ccon_2scta"));
                asi.setAsiento3SCTA(rs.getShort("ccon_3scta"));
                asi.setAsiento4SCTA(rs.getShort("ccon_4scta"));
                asi.setAsientoMdaNomCrt(rs.getString("mon_nom_moneda"));
                asi.setAsientoAX1(rs.getLong("asie_ax1"));
                asi.setAsientoAX2(rs.getLong("asie_ax2"));
                asi.setAsientoAX3(rs.getLong("asie_ax3"));
                asi.setAsientoCveCarAbo(rs.getString("asie_car_abo"));
                asi.setAsientoImporte(rs.getDouble("asie_importe"));
                asi.setAsientoDescripcion(rs.getString("asie_descripcion"));
                asi.setAsientoStatus(rs.getString("asi_status"));
                asi.setAsientoMdaId(rs.getShort("mone_id_moneda"));
                asi.setAsientoFecAlta(rs.getDate("asie_alta"));
                consulta.add(asi);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsAsiento_ConsultaPorRango()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadAsientoBean> onConsAsiento_Consulta(CriterioBusquedaContaAsienBean cba)  {
        String sqlComandoGetBook = "";
        Integer sqlParamGetBook = 0;
        String sqlFiltroGetBook = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadAsientoBean> consulta = new ArrayList<>();
        try {
            List<ContabilidadMonedaCortoBean> listMonedasCorto = getDivisaCorto();
            List<ContabilidadMonedaCortoBean> listMonedasLargo = getDivisaLargo();

            sqlFiltroGetBook = "WHERE ";
            sqlComandoGetBook = "SELECT \n"
                    + "       detm_folio_op, asie_sec_asiento, ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta,   \n"
                    + "       asie_ax1, asie_ax2, asie_ax3, asie_car_abo, asie_importe, asie_descripcion, movi_fecha_mov, asie_alta,\n";
            if (cba.getCriterioStatus().equals("CANCELADO")) {
                sqlComandoGetBook += "       asi_status, mone_id_moneda,  oper_id_operacion, tran_id_tran, cc_num_usuario     \n";
            } else {
                sqlComandoGetBook += "       asi_status, mone_id_moneda,  oper_id_operacion, tran_id_tran, null as cc_num_usuario     \n";
            }
            sqlComandoGetBook
                    += "FROM   SAF.FDAsientos                   \n";
            if (cba.getCriterioStatus().equals("CANCELADO")) {
                    sqlComandoGetBook
                    += "LEFT   \n"
                    + "OUTER  \n"
                    + "JOIN   SAF.CancelaCtrl                  \n"
                    + "ON     detm_folio_op = cc_folio         \n";
            }   
//                    + "JOIN   SAF.Monedas       m              \n"
//                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
//                    + "JOIN   SAF.Monedas_Corto mc             \n"
//                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
//                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = asie_ax1 AND V.USU_NUM_USUARIO = ? \n";

            sqlFiltroGetBook += "asie_ax1 IN (SELECT V.CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO V WHERE V.USU_NUM_USUARIO = ?) AND \n";

            if (cba.getCriterioCTAM() != null) {
                sqlFiltroGetBook += "ccon_cta       = ? AND \n";
            }
            if (cba.getCriterioSC1() != null) {
                sqlFiltroGetBook += "ccon_scta      = ? AND \n";
            }
            if (cba.getCriterioSC2() != null) {
                sqlFiltroGetBook += "ccon_2scta     = ? AND \n";
            }
            if (cba.getCriterioSC3() != null) {
                sqlFiltroGetBook += "ccon_3scta     = ? AND \n";
            }
            if (cba.getCriterioSC4() != null) {
                sqlFiltroGetBook += "ccon_4scta     = ? AND \n";
            }
            if (cba.getCriterioAX1() != null) {
                sqlFiltroGetBook += "asie_ax1       = ? AND \n";
            }
            if (cba.getCriterioAX2() != null) {
                sqlFiltroGetBook += "asie_ax2       = ? AND \n";
            }
            if (cba.getCriterioAX3() != null) {
                sqlFiltroGetBook += "asie_ax3       = ? AND \n";
            }
            if (cba.getCriterioFolio() != null) {
                sqlFiltroGetBook += "detm_folio_op  = ? AND \n";
            }
            if (cba.getCriterioMonedaNom() != null) {
                sqlFiltroGetBook += "mone_id_moneda = ? AND \n";
            }
            if (cba.getCriterioPlaza() != null) {
                sqlComandoGetBook += "JOIN SAF.Contrato C ON C.cto_num_contrato = asie_ax1 \n";
                sqlFiltroGetBook += "C.cto_num_nivel4 = ? \n AND ";
            }
            if (cba.getCriterioFechaTipo() != null) {
                if (cba.getCriterioFechaTipo().equals("FN")) {
                    sqlFiltroGetBook += "movi_fecha_mov BETWEEN ? and  ? AND \n"; 
                }
                if (cba.getCriterioFechaTipo().equals("FV")) {
                    sqlFiltroGetBook += "asie_fec_cont_int BETWEEN ? and ?  AND \n";
                    sqlFiltroGetBook += "movi_fecha_mov < asie_fec_cont_int AND \n";
                }
                if (cba.getCriterioFechaTipo().equals("FCB")) {

                }
            }
            if (cba.getCriterioDescripcion() != null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlFiltroGetBook += "asie_descripcion like ? AND \n";
            }
            if (cba.getCriterioStatus() != null) {
                sqlFiltroGetBook += "asi_status = ? AND \n";
            }

            sqlFiltroGetBook = sqlFiltroGetBook.substring(0, sqlFiltroGetBook.length() - 5);
            sqlComandoGetBook = sqlComandoGetBook.concat(sqlFiltroGetBook);

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGetBook);

            sqlParamGetBook++;
            preparedStatement.setInt(sqlParamGetBook, cba.getCriterioUsuario());
            if (cba.getCriterioCTAM() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioCTAM());
            }
            if (cba.getCriterioSC1() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC1());
            }
            if (cba.getCriterioSC2() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC2());
            }
            if (cba.getCriterioSC3() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC3());
            }
            if (cba.getCriterioSC4() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC4());
            }
            if (cba.getCriterioAX1() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX1());
            }
            if (cba.getCriterioAX2() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX2());
            }
            if (cba.getCriterioAX3() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX3());
            }
            if (cba.getCriterioFolio() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioFolio());
            }
            if (cba.getCriterioMonedaNom() != null) {
                ContabilidadMonedaCortoBean monedaLargo  = listMonedasLargo.stream()
                    .filter(monedaCortomonedaCortoNombre ->   monedaCortomonedaCortoNombre.getMonedaCortoNombre().equals(cba.getCriterioMonedaNom()))
                    .findAny().orElse(null);
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, monedaLargo.getMonedaCortoID());
            }
            if (cba.getCriterioPlaza() != null) {
                sqlParamGetBook++;
                preparedStatement.setInt(sqlParamGetBook, cba.getCriterioPlaza());
            }
            java.util.Date fechaInicio = new java.util.Date();
            java.util.Date fechaFin= new java.util.Date();
            Calendar calendario;
            if (cba.getCriterioFechaTipo() != null) {
                if ((cba.getCriterioFechaTipo().equals("FN")) || (cba.getCriterioFechaTipo().equals("FV"))) {

                if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() == null && cba.getCriterioFechaDD() == null) {
                    fechaInicio = formatFechaParse("01/01/".concat(cba.getCriterioFechaYY().toString()));
                    fechaFin = formatFechaParse("31/12/".concat(cba.getCriterioFechaYY().toString()));
                } else {
                    if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() == null) {
                        fechaInicio = formatFechaParse("01/".concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        calendario = Calendar.getInstance();
                        calendario.setTime(fechaInicio);
                        fechaFin = formatFechaParse(calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (calendario.get(Calendar.MONTH) + 1) + "/" + calendario.get(Calendar.YEAR));
                    }else{
                     if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() != null) {
                        fechaInicio = formatFechaParse(cba.getCriterioFechaDD().toString().concat("/").concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        fechaFin = fechaInicio;
                    }
}
                }

                    sqlParamGetBook++;
                    preparedStatement.setDate(sqlParamGetBook,  new java.sql.Date(fechaInicio.getTime()));
                    sqlParamGetBook++;
                    preparedStatement.setDate(sqlParamGetBook, new java.sql.Date(fechaFin.getTime()));
                } 
            }
            if (cba.getCriterioDescripcion() != null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlParamGetBook++;
                String parDescripcion = "%".concat(cba.getCriterioDescripcion().toUpperCase(Locale.ENGLISH).concat("%"));
                preparedStatement.setString(sqlParamGetBook, parDescripcion);
            }
            if (cba.getCriterioStatus() != null) {
                sqlParamGetBook++;
                preparedStatement.setString(sqlParamGetBook, cba.getCriterioStatus());
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadAsientoBean asi = new ContabilidadAsientoBean();
                asi.setAsientoFolio(resultSet.getLong("detm_folio_op"));
                asi.setAsientoSecuencial(resultSet.getInt("asie_sec_asiento"));
                asi.setAsientoOperacion(resultSet.getString("oper_id_operacion"));
                asi.setAsientoTransaccion(resultSet.getString("tran_id_tran"));
                asi.setAsientoFecMov(resultSet.getDate("movi_fecha_mov"));
                asi.setAsientoFecReg(resultSet.getDate("asie_alta"));
                asi.setAsientoCTAM(resultSet.getShort("ccon_cta"));
                asi.setAsiento1SCTA(resultSet.getShort("ccon_scta"));
                asi.setAsiento2SCTA(resultSet.getShort("ccon_2scta"));
                asi.setAsiento3SCTA(resultSet.getShort("ccon_3scta"));
                asi.setAsiento4SCTA(resultSet.getShort("ccon_4scta"));
                asi.setAsientoMdaId(resultSet.getShort("mone_id_moneda"));
                // Moneda corto
                //asi.setAsientoMdaNomCrt(resultSet.getString("mon_nom_moneda_corto"));mone_id_moneda
                ContabilidadMonedaCortoBean monedaCorto  = listMonedasCorto.stream()
                    .filter(monedaCortoID ->   monedaCortoID.getMonedaCortoID().equals(asi.getAsientoMdaId()))
                    .findAny().orElse(null);
                asi.setAsientoMdaNomCrt(monedaCorto.getMonedaCortoNombre());

                asi.setAsientoAX1(resultSet.getLong("asie_ax1"));
                asi.setAsientoAX2(resultSet.getLong("asie_ax2"));
                asi.setAsientoAX3(resultSet.getLong("asie_ax3"));
                asi.setAsientoCveCarAbo(resultSet.getString("asie_car_abo"));
                asi.setAsientoImporte(resultSet.getDouble("asie_importe"));
                asi.setAsientoDescripcion(resultSet.getString("asie_descripcion"));
                asi.setAsientoStatus(resultSet.getString("asi_status"));
                asi.setAsientoUsuCancelo(resultSet.getInt("cc_num_usuario"));

                consulta.add(asi);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage() + e.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar PreparetStatement." + e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar ResultSet." + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar Connection." + e.getMessage());
                }
            }
        }
        return consulta;
    }
    public List<ContabilidadMonedaCortoBean> getDivisaCorto() {
       String sqlComandoGetBook = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadMonedaCortoBean> consulta = new ArrayList<>();
        try {

            sqlComandoGetBook = "SELECT MON_NUM_PAIS,  MON_NOM_MONEDA_CORTO  \n "
                              + "FROM SAF.MONEDAS_CORTO ";

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGetBook);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadMonedaCortoBean monedaCorto = new ContabilidadMonedaCortoBean();
                monedaCorto.setMonedaCortoID(resultSet.getShort("MON_NUM_PAIS"));
                monedaCorto.setMonedaCortoNombre(resultSet.getString("MON_NOM_MONEDA_CORTO"));                

                consulta.add(monedaCorto);
            }

        } catch (SQLException e) {
                    logger.error("Function ::getDivisaCorto:: Consultar monedas corto." + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaCorto:: Error al cerrar PreparetStatement." + e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaCorto:: Error al cerrar ResultSet." + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaCorto:: Error al cerrar Connection." + e.getMessage());
                }
            }
        }
        return consulta;

    }
    public List<ContabilidadMonedaCortoBean> getDivisaLargo() {
       String sqlComandoGetBook = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadMonedaCortoBean> consulta = new ArrayList<>();
        try {

            sqlComandoGetBook = "SELECT MON_NUM_PAIS,  MON_NOM_MONEDA  \n "
                              + "FROM SAF.MONEDAS ";

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGetBook);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadMonedaCortoBean monedaCorto = new ContabilidadMonedaCortoBean();
                monedaCorto.setMonedaCortoID(resultSet.getShort("MON_NUM_PAIS"));
                monedaCorto.setMonedaCortoNombre(resultSet.getString("MON_NOM_MONEDA"));                

                consulta.add(monedaCorto);
            }

        } catch (SQLException e) {
                    logger.error("Function ::getDivisaLargo:: Consultar monedas corto." + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaLargo:: Error al cerrar PreparetStatement." + e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaLargo:: Error al cerrar ResultSet." + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDivisaLargo:: Error al cerrar Connection." + e.getMessage());
                }
            }
        }
        return consulta;

    }
    public List<ContabilidadAsientoBean> getBookEntriesPaginated(CriterioBusquedaContaAsienBean cba, String offset) {
        String sqlComandoGetBook = "";
        Integer sqlParamGetBook = 0;
        String sqlFiltroGetBook = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadAsientoBean> consulta = new ArrayList<>();
        try {
            List<ContabilidadMonedaCortoBean> listMonedasCorto = getDivisaCorto();
            List<ContabilidadMonedaCortoBean> listMonedasLargo = getDivisaLargo();
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlFiltroGetBook = "WHERE ";
            sqlComandoGetBook = "SELECT detm_folio_op, asie_sec_asiento, ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta, \n"
                    + "asie_ax1, asie_ax2, asie_ax3, asie_car_abo, asie_importe, asie_descripcion, movi_fecha_mov, asie_alta, \n"
                    + "asi_status, mone_id_moneda, oper_id_operacion, tran_id_tran, cc_num_usuario \n"
                    + "FROM ( \n"
                    + "  SELECT ROW_NUMBER() OVER (ORDER BY detm_folio_op DESC, asie_sec_asiento ASC) AS Ind, \n"
                    + "       detm_folio_op, asie_sec_asiento, ccon_cta, ccon_scta, ccon_2scta, ccon_3scta, ccon_4scta,   \n"
                    + "       asie_ax1, asie_ax2, asie_ax3, asie_car_abo, asie_importe, asie_descripcion, movi_fecha_mov, asie_alta,\n";
            if (cba.getCriterioStatus().equals("CANCELADO")) {
                sqlComandoGetBook += "       asi_status, mone_id_moneda,  oper_id_operacion, tran_id_tran, cc_num_usuario     \n";
            } else {
                sqlComandoGetBook += "       asi_status, mone_id_moneda,  oper_id_operacion, tran_id_tran, null as cc_num_usuario     \n";
            }
            sqlComandoGetBook
                    += "FROM   SAF.FDAsientos                   \n";
            if (cba.getCriterioStatus().equals("CANCELADO")) {
                    sqlComandoGetBook
                    += "LEFT   \n"
                    + "OUTER  \n"
                    + "JOIN   SAF.CancelaCtrl                  \n"
                    + "ON     detm_folio_op = cc_folio         \n";
            }   
//                    + "JOIN   SAF.Monedas       m              \n"
//                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
//                    + "JOIN   SAF.Monedas_Corto mc             \n"
//                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
//                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = asie_ax1 AND V.USU_NUM_USUARIO = ? \n";

            sqlFiltroGetBook += "asie_ax1 IN (SELECT V.CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO V WHERE V.USU_NUM_USUARIO = ?) AND \n";

            if (cba.getCriterioCTAM() != null) {
                sqlFiltroGetBook += "ccon_cta       = ? AND \n";
            }
            if (cba.getCriterioSC1() != null) {
                sqlFiltroGetBook += "ccon_scta      = ? AND \n";
            }
            if (cba.getCriterioSC2() != null) {
                sqlFiltroGetBook += "ccon_2scta     = ? AND \n";
            }
            if (cba.getCriterioSC3() != null) {
                sqlFiltroGetBook += "ccon_3scta     = ? AND \n";
            }
            if (cba.getCriterioSC4() != null) {
                sqlFiltroGetBook += "ccon_4scta     = ? AND \n";
            }
            if (cba.getCriterioAX1() != null) {
                sqlFiltroGetBook += "asie_ax1       = ? AND \n";
            }
            if (cba.getCriterioAX2() != null) {
                sqlFiltroGetBook += "asie_ax2       = ? AND \n";
            }
            if (cba.getCriterioAX3() != null) {
                sqlFiltroGetBook += "asie_ax3       = ? AND \n";
            }
            if (cba.getCriterioFolio() != null) {
                sqlFiltroGetBook += "detm_folio_op  = ? AND \n";
            }
            if (cba.getCriterioMonedaNom() != null) {
                sqlFiltroGetBook += "mone_id_moneda = ? AND \n";
            }
            if (cba.getCriterioPlaza() != null) {
                sqlComandoGetBook += "JOIN SAF.Contrato C ON C.cto_num_contrato = asie_ax1 \n";
                sqlFiltroGetBook += "C.cto_num_nivel4 = ? \n AND ";
            }
            if (cba.getCriterioFechaTipo() != null) {
                if (cba.getCriterioFechaTipo().equals("FN")) {
                    sqlFiltroGetBook += "movi_fecha_mov BETWEEN ? and  ? AND \n"; 
                }
                if (cba.getCriterioFechaTipo().equals("FV")) {
                    sqlFiltroGetBook += "asie_fec_cont_int BETWEEN ? and ?  AND \n";
                    sqlFiltroGetBook += "movi_fecha_mov < asie_fec_cont_int AND \n";
                }
                if (cba.getCriterioFechaTipo().equals("FCB")) {

                }
            }
            if (cba.getCriterioDescripcion() != null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlFiltroGetBook += "asie_descripcion like ? AND \n";
            }
            if (cba.getCriterioStatus() != null) {
                sqlFiltroGetBook += "asi_status = ? AND \n";
            }

            sqlFiltroGetBook = sqlFiltroGetBook.substring(0, sqlFiltroGetBook.length() - 5);
            sqlComandoGetBook = sqlComandoGetBook.concat(sqlFiltroGetBook);
            sqlComandoGetBook += ") AS Consulta WHERE Consulta.Ind \n";
            sqlComandoGetBook += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoGetBook += "AND ".concat(Integer.toString(and));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoGetBook);

            sqlParamGetBook++;
            preparedStatement.setInt(sqlParamGetBook, cba.getCriterioUsuario());
            if (cba.getCriterioCTAM() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioCTAM());
            }
            if (cba.getCriterioSC1() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC1());
            }
            if (cba.getCriterioSC2() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC2());
            }
            if (cba.getCriterioSC3() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC3());
            }
            if (cba.getCriterioSC4() != null) {
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, cba.getCriterioSC4());
            }
            if (cba.getCriterioAX1() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX1());
            }
            if (cba.getCriterioAX2() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX2());
            }
            if (cba.getCriterioAX3() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioAX3());
            }
            if (cba.getCriterioFolio() != null) {
                sqlParamGetBook++;
                preparedStatement.setLong(sqlParamGetBook, cba.getCriterioFolio());
            }
            if (cba.getCriterioMonedaNom() != null) {
                ContabilidadMonedaCortoBean monedaLargo  = listMonedasLargo.stream()
                    .filter(monedaCortomonedaCortoNombre ->   monedaCortomonedaCortoNombre.getMonedaCortoNombre().equals(cba.getCriterioMonedaNom()))
                    .findAny().orElse(null);
                sqlParamGetBook++;
                preparedStatement.setShort(sqlParamGetBook, monedaLargo.getMonedaCortoID());
            }
            if (cba.getCriterioPlaza() != null) {
                sqlParamGetBook++;
                preparedStatement.setInt(sqlParamGetBook, cba.getCriterioPlaza());
            }
            java.util.Date fechaInicio = new java.util.Date();
            java.util.Date fechaFin= new java.util.Date();
            Calendar calendario;
            if (cba.getCriterioFechaTipo() != null) {
                if ((cba.getCriterioFechaTipo().equals("FN")) || (cba.getCriterioFechaTipo().equals("FV"))) {

                if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() == null && cba.getCriterioFechaDD() == null) {
                    fechaInicio = formatFechaParse("01/01/".concat(cba.getCriterioFechaYY().toString()));
                    fechaFin = formatFechaParse("31/12/".concat(cba.getCriterioFechaYY().toString()));
                } else {
                    if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() == null) {
                        fechaInicio = formatFechaParse("01/".concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        calendario = Calendar.getInstance();
                        calendario.setTime(fechaInicio);
                        fechaFin = formatFechaParse(calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (calendario.get(Calendar.MONTH) + 1) + "/" + calendario.get(Calendar.YEAR));
                    }else{
                     if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() != null) {
                        fechaInicio = formatFechaParse(cba.getCriterioFechaDD().toString().concat("/").concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        fechaFin = fechaInicio;
                    }
}
                }

                    sqlParamGetBook++;
                    preparedStatement.setDate(sqlParamGetBook,  new java.sql.Date(fechaInicio.getTime()));
                    sqlParamGetBook++;
                    preparedStatement.setDate(sqlParamGetBook, new java.sql.Date(fechaFin.getTime()));
                } 
            }
            if (cba.getCriterioDescripcion() != null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlParamGetBook++;
                String parDescripcion = "%".concat(cba.getCriterioDescripcion().toUpperCase(Locale.ENGLISH).concat("%"));
                preparedStatement.setString(sqlParamGetBook, parDescripcion);
            }
            if (cba.getCriterioStatus() != null) {
                sqlParamGetBook++;
                preparedStatement.setString(sqlParamGetBook, cba.getCriterioStatus());
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadAsientoBean asi = new ContabilidadAsientoBean();
                asi.setAsientoFolio(resultSet.getLong("detm_folio_op"));
                asi.setAsientoSecuencial(resultSet.getInt("asie_sec_asiento"));
                asi.setAsientoOperacion(resultSet.getString("oper_id_operacion"));
                asi.setAsientoTransaccion(resultSet.getString("tran_id_tran"));
                asi.setAsientoFecMov(resultSet.getDate("movi_fecha_mov"));
                asi.setAsientoFecReg(resultSet.getDate("asie_alta"));
                asi.setAsientoCTAM(resultSet.getShort("ccon_cta"));
                asi.setAsiento1SCTA(resultSet.getShort("ccon_scta"));
                asi.setAsiento2SCTA(resultSet.getShort("ccon_2scta"));
                asi.setAsiento3SCTA(resultSet.getShort("ccon_3scta"));
                asi.setAsiento4SCTA(resultSet.getShort("ccon_4scta"));
                asi.setAsientoMdaId(resultSet.getShort("mone_id_moneda"));
                // Moneda corto
                //asi.setAsientoMdaNomCrt(resultSet.getString("mon_nom_moneda_corto"));mone_id_moneda
                ContabilidadMonedaCortoBean monedaCorto  = listMonedasCorto.stream()
                    .filter(monedaCortoID ->   monedaCortoID.getMonedaCortoID().equals(asi.getAsientoMdaId()))
                    .findAny().orElse(null);
                asi.setAsientoMdaNomCrt(monedaCorto.getMonedaCortoNombre());

                asi.setAsientoAX1(resultSet.getLong("asie_ax1"));
                asi.setAsientoAX2(resultSet.getLong("asie_ax2"));
                asi.setAsientoAX3(resultSet.getLong("asie_ax3"));
                asi.setAsientoCveCarAbo(resultSet.getString("asie_car_abo"));
                asi.setAsientoImporte(resultSet.getDouble("asie_importe"));
                asi.setAsientoDescripcion(resultSet.getString("asie_descripcion"));
                asi.setAsientoStatus(resultSet.getString("asi_status"));
                asi.setAsientoUsuCancelo(resultSet.getInt("cc_num_usuario"));

                consulta.add(asi);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage() + e.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar PreparetStatement." + e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar ResultSet." + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBookEntriesPaginated:: Error al cerrar Connection." + e.getMessage());
                }
            }
        }
        return consulta;
    }

    public int getBookEntriesTotalRows(CriterioBusquedaContaAsienBean cba)  {
        int totalRows = 0;

        try {
            List<ContabilidadMonedaCortoBean> listMonedasLargo = getDivisaLargo();
            sqlParam = 0;
            String sqlFiltro = "WHERE ";
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.FDAsientos \n";
            if (cba.getCriterioStatus() == "CANCELADO") {
                sqlComando
                        += "LEFT   \n"
                        + "OUTER  \n"
                        + "JOIN   SAF.CancelaCtrl                  \n"
                        + "ON     detm_folio_op = cc_folio         \n";
            }
//                    + "JOIN   SAF.Monedas       m              \n"
//                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
//                    + "JOIN   SAF.Monedas_Corto mc             \n"
//                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
//                    + "INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = asie_ax1 AND V.USU_NUM_USUARIO = ? \n";
             sqlFiltro += "asie_ax1 IN (SELECT V.CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO V WHERE V.USU_NUM_USUARIO = ?) AND \n";
            if (cba.getCriterioCTAM() != null) {
                sqlFiltro += "ccon_cta       = ? AND \n";
            }
            if (cba.getCriterioSC1() != null) {
                sqlFiltro += "ccon_scta      = ? AND \n";
            }
            if (cba.getCriterioSC2() != null) {
                sqlFiltro += "ccon_2scta     = ? AND \n";
            }
            if (cba.getCriterioSC3() != null) {
                sqlFiltro += "ccon_3scta     = ? AND \n";
            }
            if (cba.getCriterioSC4() != null) {
                sqlFiltro += "ccon_4scta     = ? AND \n";
            }
            if (cba.getCriterioAX1() != null) {
                sqlFiltro += "asie_ax1       = ? AND \n";
            }
            if (cba.getCriterioAX2() != null) {
                sqlFiltro += "asie_ax2       = ? AND \n";
            }
            if (cba.getCriterioAX3() != null) {
                sqlFiltro += "asie_ax3       = ? AND \n";
            }
            if (cba.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op  = ? AND \n";
            }
            if (cba.getCriterioMonedaNom() != null) {
                sqlFiltro += "mone_id_moneda = ? AND \n";
            }
            if (cba.getCriterioPlaza() != null) {
                sqlComando += "JOIN SAF.Contrato C ON c.cto_num_contrato = asie_ax1 \n";
                sqlFiltro += "c.cto_num_nivel4 = ? \n AND ";
            }
            if (cba.getCriterioFechaTipo() != null) {
                if (cba.getCriterioFechaTipo().equals("FN")) {
                    sqlFiltro+= "movi_fecha_mov BETWEEN ? and  ? AND \n"; 
                }
                if (cba.getCriterioFechaTipo().equals("FV")) {
                    sqlFiltro+= "asie_fec_cont_int BETWEEN ? and ?  AND \n";
                    sqlFiltro+= "movi_fecha_mov < asie_fec_cont_int AND \n";
                }
                if (cba.getCriterioFechaTipo().equals("FCB")) {

                }
                if (cba.getCriterioFechaTipo().equals("FCB")) {

                }
            }
            if (cba.getCriterioDescripcion()!= null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlFiltro += "asie_descripcion like ? AND \n";
            }
            if (cba.getCriterioStatus() != null) {
                sqlFiltro += "asi_status = ? AND \n";
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
            sqlComando = sqlComando.concat(sqlFiltro);
            //System.out.println(sqlComando);

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setInt(sqlParam, cba.getCriterioUsuario());

            if (cba.getCriterioCTAM() != null) {
                sqlParam++;
                pstmt.setShort(sqlParam, cba.getCriterioCTAM());
            }
            if (cba.getCriterioSC1() != null) {
                sqlParam++;
                pstmt.setShort(sqlParam, cba.getCriterioSC1());
            }
            if (cba.getCriterioSC2() != null) {
                sqlParam++;
                pstmt.setShort(sqlParam, cba.getCriterioSC2());
            }
            if (cba.getCriterioSC3() != null) {
                sqlParam++;
                pstmt.setShort(sqlParam, cba.getCriterioSC3());
            }
            if (cba.getCriterioSC4() != null) {
                sqlParam++;
                pstmt.setShort(sqlParam, cba.getCriterioSC4());
            }
            if (cba.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cba.getCriterioAX1());
            }
            if (cba.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cba.getCriterioAX2());
            }
            if (cba.getCriterioAX3() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cba.getCriterioAX3());
            }
            if (cba.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cba.getCriterioFolio());
            }
            if (cba.getCriterioMonedaNom() != null) {
                ContabilidadMonedaCortoBean monedaLargo  = listMonedasLargo.stream()
                    .filter(monedaCortomonedaCortoNombre ->   monedaCortomonedaCortoNombre.getMonedaCortoNombre().equals(cba.getCriterioMonedaNom()))
                    .findAny().orElse(null);
                sqlParam++;
                pstmt.setShort(sqlParam, monedaLargo.getMonedaCortoID());
            }
            if (cba.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cba.getCriterioPlaza());
            }
            if (cba.getCriterioFechaTipo() != null) {
            java.util.Date fechaInicio = new java.util.Date();
            java.util.Date fechaFin= new java.util.Date();
            Calendar calendario;
            if (cba.getCriterioFechaTipo() != null) {
                if ((cba.getCriterioFechaTipo().equals("FN")) || (cba.getCriterioFechaTipo().equals("FV"))) {

                if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() == null && cba.getCriterioFechaDD() == null) {
                    fechaInicio = formatFechaParse("01/01/".concat(cba.getCriterioFechaYY().toString()));
                    fechaFin = formatFechaParse("31/12/".concat(cba.getCriterioFechaYY().toString()));
                } else {
                    if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() == null) {
                        fechaInicio = formatFechaParse("01/".concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        calendario = Calendar.getInstance();
                        calendario.setTime(fechaInicio);
                        fechaFin = formatFechaParse(calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (calendario.get(Calendar.MONTH) + 1) + "/" + calendario.get(Calendar.YEAR));
                    }else{
                     if (cba.getCriterioFechaYY() != null && cba.getCriterioFechaMM() != null && cba.getCriterioFechaDD() != null) {
                        fechaInicio = formatFechaParse(cba.getCriterioFechaDD().toString().concat("/").concat(cba.getCriterioFechaMM().toString()).concat("/").concat(cba.getCriterioFechaYY().toString()));
                        fechaFin = fechaInicio;
                    }
}
                }

                    sqlParam++;
                    pstmt.setDate(sqlParam,  new java.sql.Date(fechaInicio.getTime()));
                    sqlParam++;
                    pstmt.setDate(sqlParam, new java.sql.Date(fechaFin.getTime()));
                }
            }

            }
            if (cba.getCriterioDescripcion()!= null && !cba.getCriterioDescripcion().isEmpty()) {
                sqlParam++;
                String parDescripcion = "%".concat(cba.getCriterioDescripcion().toUpperCase(Locale.ENGLISH).concat("%"));
                pstmt.setString(sqlParam, parDescripcion );
            }
            if (cba.getCriterioStatus() != null) {
                sqlParam++;
                pstmt.setString(sqlParam, cba.getCriterioStatus());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsAsiento_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C O N S U L T A   M O V T O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadAsientoBean> onConsMovto_ConsultaDetalle(long asientoFolio, String transacionId)  {
        List<ContabilidadAsientoBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT asie_sec_asiento, mon_nom_moneda, asi.ccon_cta, asi.ccon_scta, asi.ccon_2scta, asi.ccon_3scta, asi.ccon_4scta,\n"
                    + "       asie_ax1, asie_ax2, asie_ax3, asie_car_abo, asie_importe, asie_descripcion, ccon_nombre, mone_id_moneda       \n"
                    + "FROM   SAF.FDAsientos asi             \n"
                    + "JOIN   SAF.Monedas                    \n"
                    + "ON     mone_id_moneda = mon_num_pais  \n"
                    + "JOIN   SAF.FDCatContable  cc          \n"
                    + "ON     asi.ccon_cta   = cc.ccon_cta   \n"
                    + "AND    asi.ccon_scta  = cc.ccon_scta  \n"
                    + "AND    asi.ccon_2scta = cc.ccon_2scta \n"
                    + "AND    asi.ccon_3scta = cc.ccon_3scta \n"
                    + "AND    asi.ccon_4scta = cc.ccon_4scta \n"
                    + "WHERE  detm_folio_op = ?              \n"
                    + "AND    tran_id_tran  = ?              \n"
                    + "ORDER  BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, asientoFolio);
            pstmt.setString(2, transacionId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadAsientoBean asi = new ContabilidadAsientoBean();
                asi.setAsientoSecuencial(rs.getInt("asie_sec_asiento"));
                asi.setAsientoMdaNomLrg(rs.getString("mon_nom_moneda"));
                asi.setAsientoCTAM(rs.getShort("ccon_cta"));
                asi.setAsiento1SCTA(rs.getShort("ccon_scta"));
                asi.setAsiento2SCTA(rs.getShort("ccon_2scta"));
                asi.setAsiento3SCTA(rs.getShort("ccon_3scta"));
                asi.setAsiento4SCTA(rs.getShort("ccon_4scta"));
                asi.setAsientoAX1(rs.getLong("asie_ax1"));
                asi.setAsientoAX2(rs.getLong("asie_ax2"));
                asi.setAsientoAX3(rs.getLong("asie_ax3"));
                asi.setAsientoCveCarAbo(rs.getString("asie_car_abo"));
                asi.setAsientoImporte(rs.getDouble("asie_importe"));
                asi.setAsientoDescripcion(rs.getString("asie_descripcion"));
                asi.setAsientoCuentaNombre(rs.getString("ccon_nombre"));
                asi.setAsientoMdaId(rs.getShort("mone_id_moneda"));
                consulta.add(asi);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_ConsultaDetalle()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadMovtoBean> onConsMovto_ConsultaPolizaMan(CriterioBusquedaContaBean cbc) {
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            sqlParam = 2;
            String sqlFiltro = "";
            String sqlComando = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso,       \n"
                    + "       detm_folio_op, tran_id_tran, movi_descripcion, movi_importe,  \n"
                    + "       movi_usuario, movi_status, oper_id_operacion, usu_nom_usuario \n"
                    + "FROM   SAF.FDMovimiento                    \n"
                    + "JOIN   SAF.Usuarios                        \n"
                    + "ON     movi_usuario  = usu_num_usuario     \n"
                    + "WHERE  subStr(oper_id_operacion, 1, 1) = ? \n"
                    + "AND    movi_status                     = ? \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "fiso_id_fiso    = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "sfis_id_sfiso   = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op   = ? \n AND ";
            }
            if (cbc.getCriterioUsuario() != null) {
                sqlFiltro += "usu_num_usuario = ? \n AND ";
            }

            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    sqlFiltro += "Extract(Year  FROM movi_fecha_mov) = ? \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fecha_mov) = ? \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fecha_mov) = ? \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltro += "Extract(Year  FROM movi_fec_cont_int) = ? \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fec_cont_int) = ? \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fec_cont_int) = ? \n AND ";
                    }
                    sqlFiltro += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
            }
            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
            sqlComando = sqlComando.concat(sqlFiltro).concat("ORDER BY detm_folio_op DESC");
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "7");
            pstmt.setString(2, "ACTIVO");

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioUsuario() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if ((cbc.getCriterioTipoFecha().equals("FN")) || (cbc.getCriterioTipoFecha().equals("FV"))) {
                    sqlParam++;
                    pstmt.setInt(sqlParam, cbc.getCriterioFechaYYYY());
                    sqlParam++;
                    pstmt.setInt(sqlParam, cbc.getCriterioFechaMM());
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlParam++;
                        pstmt.setInt(sqlParam, cbc.getCriterioFechaDD());
                    }
                }
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(rs.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(rs.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(rs.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoFolio(rs.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(rs.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(rs.getString("tran_id_tran"));
                movto.setMovtoDesc(rs.getString("movi_descripcion"));
                movto.setMovtoImporte(rs.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(rs.getLong("movi_usuario"));
                movto.setMovtoUsuarioNom(rs.getString("usu_nom_usuario"));
                movto.setMovtoStatus(rs.getString("movi_status"));
                consulta.add(movto);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadMovtoBean> onConsMovto_Consulta(CriterioBusquedaContaBean cbc)  {
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            String sqlFiltro = "WHERE ";
            sqlParam = 0;
            String sqlComando = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso,       \n"
                    + "       detm_folio_op, tran_id_tran, movi_descripcion, movi_importe,  \n"
                    + "       movi_usuario, movi_status, oper_id_operacion, cc_num_usuario \n"
                    + "FROM   SAF.FDMovimiento \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";

            if (!cbc.getCriterioTipoFecha().equals("FCB")) {
            sqlFiltro += "fiso_id_fiso IN  (SELECT CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO WHERE USU_NUM_USUARIO = ? ) \n AND ";
            }

            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltro += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                        sqlFiltro += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltro += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltro += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltro += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltro += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }
            if (cbc.getCriterioPlaza() != null) {
                //sqlComando += "JOIN SAF.Contrato ON cto_num_contrato = fiso_id_fiso \n";
                sqlFiltro += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltro += "movi_status = ? \n AND ";
                }
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
//            if (!cbc.getCriterioTipoFecha().equals("FCB")) {
//            sqlComando += "JOIN SAF.VISTA_USUARIO V ON fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = " + cbc.getCriterioUsuario() + " \n";
//            }
            sqlComando = sqlComando.concat(sqlFiltro).concat("ORDER BY detm_folio_op DESC");
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            if (!cbc.getCriterioTipoFecha().equals("FCB")) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
            }

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioTransId());
            }
            /* if (cbc.getCriterioTipoFecha()!= null){
                if ((cbc.getCriterioTipoFecha().equals("FN"))||(cbc.getCriterioTipoFecha().equals("FV"))){
                    sqlParam++;
                    pstmt.setInt(sqlParam, cbc.getCriterioFechaYYYY());
                    sqlParam++;
                    pstmt.setInt(sqlParam, cbc.getCriterioFechaMM());
                    if (cbc.getCriterioFechaDD()!= null){
                        sqlParam++;
                        pstmt.setInt(sqlParam, cbc.getCriterioFechaDD());
                    }
                }
            }*/
            if (cbc.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParam++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    pstmt.setString(sqlParam, "ACTIVO");
                } else {
                    pstmt.setString(sqlParam, "CANCELADO");
                }
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(rs.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(rs.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(rs.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoFolio(rs.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(rs.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(rs.getString("tran_id_tran"));
                movto.setMovtoDesc(rs.getString("movi_descripcion"));
                movto.setMovtoImporte(rs.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(rs.getLong("movi_usuario"));
                //movto.setMovtoUsuarioNom(rs.getString("usu_nom_usuario"));
                movto.setMovtoStatus(rs.getString("movi_status"));
                movto.setMovtoCCUsuario(rs.getInt("cc_num_usuario"));
                if (movto.getMovtoCCUsuario().equals(0)) {
                    movto.setMovtoCCUsuario(null);
                }
                consulta.add(movto);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadMovtoBean> accountingPolicyPaginated(CriterioBusquedaContaBean cbc, String offset, String type) {
        long timeStart = System.currentTimeMillis();
        String sqlComandoAccountingPolicy = "";
        Integer sqlParamAccountingPolicy = 0;
        String sqlFiltroAccountingPolicy = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSetAccountingPolicy = null;
        Connection connAccountingPolicy = null;
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlFiltroAccountingPolicy = "WHERE ";

            sqlComandoAccountingPolicy = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "movi_usuario, movi_status, oper_id_operacion,  cc_num_usuario \n"
                    + "FROM ( \n"
                    + "   SELECT ROW_NUMBER() OVER (ORDER BY detm_folio_op DESC) AS Ind,"
                    + "   movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "   detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "   movi_usuario, movi_status, oper_id_operacion, cc_num_usuario \n"
                    + "FROM   SAF.FDMovimiento m \n"
//                    + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
//                    + "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";
                        sqlFiltroAccountingPolicy += "fiso_id_fiso IN  (SELECT CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO WHERE USU_NUM_USUARIO = ? ) \n AND ";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltroAccountingPolicy += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltroAccountingPolicy += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltroAccountingPolicy += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltroAccountingPolicy += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                            sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltroAccountingPolicy += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltroAccountingPolicy += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }
            if (cbc.getCriterioPlaza() != null) {
//                sqlComandoAccountingPolicy += "JOIN SAF.Contrato C ON C.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltroAccountingPolicy += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltroAccountingPolicy += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltroAccountingPolicy += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltroAccountingPolicy = sqlFiltroAccountingPolicy.substring(0, sqlFiltroAccountingPolicy.length() - 5);
            sqlComandoAccountingPolicy = sqlComandoAccountingPolicy.concat(sqlFiltroAccountingPolicy);
            //.concat("ORDER BY detm_folio_op DESC LIMIT 10 OFFSET ".concat(offset));
            sqlComandoAccountingPolicy += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoAccountingPolicy += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoAccountingPolicy += "AND ".concat(Integer.toString(and));
                    connAccountingPolicy = DataBaseConexion.getInstance().getConnection();
            preparedStatement = connAccountingPolicy.prepareStatement(sqlComandoAccountingPolicy);

//            sqlParamAccountingPolicy++;
//            preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());
            sqlParamAccountingPolicy++;
            preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParamAccountingPolicy++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    preparedStatement.setString(sqlParamAccountingPolicy, "ACTIVO");
                } else {
                    preparedStatement.setString(sqlParamAccountingPolicy, "CANCELADO");
                }
            }

            resultSetAccountingPolicy = preparedStatement.executeQuery();

            while (resultSetAccountingPolicy.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(resultSetAccountingPolicy.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(resultSetAccountingPolicy.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(resultSetAccountingPolicy.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(resultSetAccountingPolicy.getDate("movi_alta"));
                movto.setMovtoFolio(resultSetAccountingPolicy.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(resultSetAccountingPolicy.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(resultSetAccountingPolicy.getString("tran_id_tran"));
                movto.setMovtoDesc(resultSetAccountingPolicy.getString("movi_descripcion"));
                movto.setMovtoImporte(resultSetAccountingPolicy.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(resultSetAccountingPolicy.getLong("movi_usuario"));
//                movto.setMovtoUsuarioNom(resultSetAccountingPolicy.getString("usu_nom_usuario"));
                movto.setMovtoStatus(resultSetAccountingPolicy.getString("movi_status"));
                movto.setMovtoCCUsuario(resultSetAccountingPolicy.getInt("cc_num_usuario"));
                if (movto.getMovtoCCUsuario().equals(0)) {
                    movto.setMovtoCCUsuario(null);
                }
                consulta.add(movto);
            }
            preparedStatement.close();
            resultSetAccountingPolicy.close();
            connAccountingPolicy.close();
        } catch (SQLException e) {
            logger.error(e.getMessage() + e.getCause(), "10", "20", "30");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSetAccountingPolicy != null) {
                try {
                    resultSetAccountingPolicy.close();
                } catch (SQLException e) {
                   logger.error("Function ::accountingPolicyPaginated:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (connAccountingPolicy != null) {
                try {
                    connAccountingPolicy.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        long timeEnd = System.currentTimeMillis() - timeStart;
       logger.info("Tiempo de respuesta :: accountingPolicyPaginated :: Movimientos::  " + timeEnd + " milliseconds");

        return consulta;
    }
    public List<ContabilidadMovtoBean> accountingPolicyPaginatedAdm(CriterioBusquedaContaBean cbc, String offset, String type) {
        long timeStart = System.currentTimeMillis();
        String sqlComandoAccountingPolicy = "";
        Integer sqlParamAccountingPolicy = 0;
        String sqlFiltroAccountingPolicy = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSetAccountingPolicy = null;
        Connection connAccountingPolicy = null;
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlFiltroAccountingPolicy = "WHERE (OPER_ID_OPERACION  BETWEEN '7000000000' and '8000000000'  and (MOVI_DESCRIPCION  like 'SUGERENCIA%' or MOVI_DESCRIPCION like '%PAGO%' ))  \n AND ";

            sqlComandoAccountingPolicy = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "movi_usuario, movi_status, oper_id_operacion,  cc_num_usuario \n"
                    + "FROM ( \n"
                    + "   SELECT ROW_NUMBER() OVER (ORDER BY detm_folio_op DESC) AS Ind,"
                    + "   movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "   detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "   movi_usuario, movi_status, oper_id_operacion, cc_num_usuario \n"
                    + "FROM   SAF.FDMovimiento m \n"
//                    + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltroAccountingPolicy += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltroAccountingPolicy += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltroAccountingPolicy += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltroAccountingPolicy += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                            sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltroAccountingPolicy += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltroAccountingPolicy += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }
            if (cbc.getCriterioPlaza() != null) {
//                sqlComandoAccountingPolicy += "JOIN SAF.Contrato C ON C.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltroAccountingPolicy += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltroAccountingPolicy += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltroAccountingPolicy += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltroAccountingPolicy = sqlFiltroAccountingPolicy.substring(0, sqlFiltroAccountingPolicy.length() - 5);
            sqlComandoAccountingPolicy = sqlComandoAccountingPolicy.concat(sqlFiltroAccountingPolicy);
            //.concat("ORDER BY detm_folio_op DESC LIMIT 10 OFFSET ".concat(offset));
            sqlComandoAccountingPolicy += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoAccountingPolicy += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoAccountingPolicy += "AND ".concat(Integer.toString(and));
                    connAccountingPolicy = DataBaseConexion.getInstance().getConnection();
            preparedStatement = connAccountingPolicy.prepareStatement(sqlComandoAccountingPolicy);

//            sqlParamAccountingPolicy++;
//            preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());
            sqlParamAccountingPolicy++;
            preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParamAccountingPolicy++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    preparedStatement.setString(sqlParamAccountingPolicy, "ACTIVO");
                } else {
                    preparedStatement.setString(sqlParamAccountingPolicy, "CANCELADO");
                }
            }

            resultSetAccountingPolicy = preparedStatement.executeQuery();

            while (resultSetAccountingPolicy.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(resultSetAccountingPolicy.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(resultSetAccountingPolicy.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(resultSetAccountingPolicy.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(resultSetAccountingPolicy.getDate("movi_alta"));
                movto.setMovtoFolio(resultSetAccountingPolicy.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(resultSetAccountingPolicy.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(resultSetAccountingPolicy.getString("tran_id_tran"));
                movto.setMovtoDesc(resultSetAccountingPolicy.getString("movi_descripcion"));
                movto.setMovtoImporte(resultSetAccountingPolicy.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(resultSetAccountingPolicy.getLong("movi_usuario"));
//                movto.setMovtoUsuarioNom(resultSetAccountingPolicy.getString("usu_nom_usuario"));
                movto.setMovtoStatus(resultSetAccountingPolicy.getString("movi_status"));
                movto.setMovtoCCUsuario(resultSetAccountingPolicy.getInt("cc_num_usuario"));
                if (movto.getMovtoCCUsuario().equals(0)) {
                    movto.setMovtoCCUsuario(null);
                }
                consulta.add(movto);
            }
            preparedStatement.close();
            resultSetAccountingPolicy.close();
            connAccountingPolicy.close();
        } catch (SQLException e) {
            logger.error(e.getMessage() + e.getCause(), "10", "20", "30");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated ADM :: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSetAccountingPolicy != null) {
                try {
                    resultSetAccountingPolicy.close();
                } catch (SQLException e) {
                   logger.error("Function ::accountingPolicyPaginated ADM :: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (connAccountingPolicy != null) {
                try {
                    connAccountingPolicy.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated ADM :: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        long timeEnd = System.currentTimeMillis() - timeStart;
       logger.info("Tiempo de respuesta :: accountingPolicyPaginated  :: ADM::  " + timeEnd + " milliseconds");

        return consulta;
    }

    public List<ContabilidadMovtoBean> accountingPolicyPaginatedFCB(CriterioBusquedaContaBean cbc, String offset, String type) {
        String sqlComandoAccountingPolicy = "";
        Integer sqlParamAccountingPolicy = 0;
        String sqlFiltroAccountingPolicy = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSetAccountingPolicy = null;
        Connection connAccountingPolicy = null;
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlFiltroAccountingPolicy = "WHERE ";

            sqlComandoAccountingPolicy = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "movi_usuario, movi_status, oper_id_operacion,  cc_num_usuario \n"
                    + "FROM ( \n"
                    + "   SELECT ROW_NUMBER() OVER (ORDER BY detm_folio_op DESC) AS Ind,"
                    + "   movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "   detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "   movi_usuario, movi_status, oper_id_operacion,  cc_num_usuario \n"
                    + "FROM   SAF.FDMovimiento m \n"
                    // + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
                    //                    + "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";

            if (cbc.getCriterioAX1() != null) {
                sqlFiltroAccountingPolicy += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltroAccountingPolicy += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltroAccountingPolicy += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltroAccountingPolicy += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltroAccountingPolicy += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltroAccountingPolicy += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltroAccountingPolicy += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltroAccountingPolicy += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltroAccountingPolicy += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlComandoAccountingPolicy += "JOIN SAF.Contrato C ON C.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltroAccountingPolicy += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltroAccountingPolicy += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltroAccountingPolicy += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltroAccountingPolicy = sqlFiltroAccountingPolicy.substring(0, sqlFiltroAccountingPolicy.length() - 5);
            sqlComandoAccountingPolicy = sqlComandoAccountingPolicy.concat(sqlFiltroAccountingPolicy);
            //.concat("ORDER BY detm_folio_op DESC LIMIT 10 OFFSET ".concat(offset));
            sqlComandoAccountingPolicy += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoAccountingPolicy += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoAccountingPolicy += "AND ".concat(Integer.toString(and));
            connAccountingPolicy = DataBaseConexion.getInstance().getConnection();
            preparedStatement = connAccountingPolicy.prepareStatement(sqlComandoAccountingPolicy);

            //           sqlParamAccountingPolicy++;
            //           preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());
            //           sqlParamAccountingPolicy++;
            //           preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioUsuario());
            if (cbc.getCriterioAX1() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setLong(sqlParamAccountingPolicy, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParamAccountingPolicy++;
                preparedStatement.setInt(sqlParamAccountingPolicy, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParamAccountingPolicy++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    preparedStatement.setString(sqlParamAccountingPolicy, "ACTIVO");
                } else {
                    preparedStatement.setString(sqlParamAccountingPolicy, "CANCELADO");
                }
            }

            resultSetAccountingPolicy = preparedStatement.executeQuery();

            while (resultSetAccountingPolicy.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(resultSetAccountingPolicy.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(resultSetAccountingPolicy.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(resultSetAccountingPolicy.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(resultSetAccountingPolicy.getDate("movi_alta"));
                movto.setMovtoFolio(resultSetAccountingPolicy.getLong("detm_folio_op"));
                movto.setMovtoOperacionId(resultSetAccountingPolicy.getString("oper_id_operacion"));
                movto.setMovtoTransaccId(resultSetAccountingPolicy.getString("tran_id_tran"));
                movto.setMovtoDesc(resultSetAccountingPolicy.getString("movi_descripcion"));
                movto.setMovtoImporte(resultSetAccountingPolicy.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(resultSetAccountingPolicy.getLong("movi_usuario"));
         //       movto.setMovtoUsuarioNom(resultSetAccountingPolicy.getString("usu_nom_usuario"));
                movto.setMovtoStatus(resultSetAccountingPolicy.getString("movi_status"));
                movto.setMovtoCCUsuario(resultSetAccountingPolicy.getInt("cc_num_usuario")); 
                consulta.add(movto);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSetAccountingPolicy != null) {
                try {
                    resultSetAccountingPolicy.close();
                } catch (SQLException e) {
                    logger.error("Function ::accountingPolicyPaginated:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (connAccountingPolicy != null) {
                try {
                    connAccountingPolicy.close();
                } catch (SQLException e) {
                   logger.error("Function ::accountingPolicyPaginated:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        return consulta;
    }

    public int accountingPolicyTotalRows(CriterioBusquedaContaBean cbc, String type)  {
//        logger.info("ERROR MESSAGE DAO");   
//        logger.error("ERROR MESSAGE DAO");      
//        long timeStart = System.currentTimeMillis();
        int totalRows = 0;

        try {
            String sqlFiltro = "WHERE ";
            sqlParam = 0;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.FDMovimiento m \n"
            //        + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
            //        + "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";
            sqlFiltro += "fiso_id_fiso IN  (SELECT CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO WHERE USU_NUM_USUARIO = ? ) \n AND ";
            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltro += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                        sqlFiltro += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltro += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltro += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltro += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltro += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }

            if (cbc.getCriterioPlaza() != null) {
//                sqlComando += "JOIN SAF.Contrato C ON C.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltro += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltro += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltro += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
            sqlComando = sqlComando.concat(sqlFiltro);
            //System.out.println("-------> SQL_QUERY: ".concat(sqlComando));
            conexion = DataBaseConexion.getInstance().getConnection();
                pstmt = conexion.prepareStatement(sqlComando);

//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
            sqlParam++;
            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParam++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    pstmt.setString(sqlParam, "ACTIVO");
                } else {
                    pstmt.setString(sqlParam, "CANCELADO");
                }
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
            
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_Consulta()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
//        long timeEnd = System.currentTimeMillis() - timeStart;
//        logger.info("Timepo de respuesta:: accountingPolicyTotalRows::  " + timeEnd + "  milliseconds");
        return totalRows;
    }
    public int accountingPolicyTotalRowsAdm(CriterioBusquedaContaBean cbc, String type)  {
//        logger.info("ERROR MESSAGE DAO");   
//        logger.error("ERROR MESSAGE DAO");      
//        long timeStart = System.currentTimeMillis();
        int totalRows = 0;

        try {
            String sqlFiltro = "WHERE (OPER_ID_OPERACION  BETWEEN '7000000000' and '8000000000'  and (MOVI_DESCRIPCION  like 'SUGERENCIA%' or MOVI_DESCRIPCION like '%PAGO%' ))  \n AND ";
            sqlParam = 0;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.FDMovimiento m \n"
            //        + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
                    + "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n"
                    + "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";
            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltro += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                        sqlFiltro += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltro += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltro += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltro += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltro += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }

            if (cbc.getCriterioPlaza() != null) {
//                sqlComando += "JOIN SAF.Contrato C ON C.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltro += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltro += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltro += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
            sqlComando = sqlComando.concat(sqlFiltro);
            //System.out.println("-------> SQL_QUERY: ".concat(sqlComando));
            conexion = DataBaseConexion.getInstance().getConnection();
                pstmt = conexion.prepareStatement(sqlComando);

//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
            sqlParam++;
            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());

            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParam++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    pstmt.setString(sqlParam, "ACTIVO");
                } else {
                    pstmt.setString(sqlParam, "CANCELADO");
                }
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
            
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_ConsultaAdm()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
//        long timeEnd = System.currentTimeMillis() - timeStart;
//        logger.info("Timepo de respuesta:: accountingPolicyTotalRows::  " + timeEnd + "  milliseconds");
        return totalRows;
    }

    public int accountingPolicyTotalRowsFCB(CriterioBusquedaContaBean cbc, String type)  {
   // logger.error("ERROR MESSAGE DAO");        
        int totalRows = 0;

        try {
            String sqlFiltro = "WHERE ";
            sqlParam = 0;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.FDMovimiento m \n"
                    // + "INNER JOIN SAF.Usuarios U ON U.USU_NUM_USUARIO = MOVI_USUARIO \n"
                    + //                        "INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? \n" +
                    "LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio \n";
            if (cbc.getCriterioAX1() != null) {
                sqlFiltro += "fiso_id_fiso  = ? \n AND ";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlFiltro += "sfis_id_sfiso = ? \n AND ";
            }
            if (cbc.getCriterioFolio() != null) {
                sqlFiltro += "detm_folio_op = ? \n AND ";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlFiltro += "tran_id_tran  = ? \n AND ";
            }
            if (cbc.getCriterioTipoFecha() != null) {
                if (cbc.getCriterioTipoFecha().equals("FN")) {
                    if (cbc.getCriterioFechaYYYY() != null) {
                        sqlFiltro += "Extract(Year  FROM movi_fecha_mov) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaMM() != null) {
                        sqlFiltro += "Extract(Month FROM movi_fecha_mov) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    }
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fecha_mov) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                }
                if (cbc.getCriterioTipoFecha().equals("FV")) {
                    sqlFiltro += "Extract(Year  FROM movi_fec_cont_int) = " + cbc.getCriterioFechaYYYY() + " \n AND ";
                    sqlFiltro += "Extract(Month FROM movi_fec_cont_int) = " + cbc.getCriterioFechaMM() + " \n AND ";
                    if (cbc.getCriterioFechaDD() != null) {
                        sqlFiltro += "Extract(Day FROM movi_fec_cont_int) = " + cbc.getCriterioFechaDD() + " \n AND ";
                    }
                    sqlFiltro += "movi_fecha_mov < movi_fec_cont_int \n AND ";
                }
                if (cbc.getCriterioTipoFecha().equals("FCB")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, cbc.getCriterioFechaYYYY());
                    cal.set(Calendar.MONTH, cbc.getCriterioFechaMM() - 1);
                    cal.set(Calendar.DAY_OF_MONTH, cbc.getCriterioFechaDD());
                    Date fechaOnFuncion = new Date(cal.getTimeInMillis());
                    sqlFiltro += "((MOVI_FEC_CONT_INT = DATE('" + fechaOnFuncion + "') AND MOVI_FECHA_MOV <= MOVI_FEC_CONT_INT AND movi_status='ACTIVO') OR \n"
                            + "(movi_status='CANCELADO' AND CC_FEC_ACTIVO = DATE('" + fechaOnFuncion + "') AND DETM_FOLIO_OP=CC_FOLIO AND CC_FEC_ACTIVO <> CC_FEC_CANCELADO)) AND \n";
                }
            }

            if (cbc.getCriterioPlaza() != null) {
                sqlComando += "JOIN SAF.Contrato C ON c.cto_num_contrato = fiso_id_fiso \n";
                sqlFiltro += "cto_num_nivel4 = ? \n AND ";
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlFiltro += "movi_status = ? \n AND ";
                }
            }

            if (type.equals("getPolicies")) {
                sqlFiltro += " 7 = SUBSTR(oper_id_operacion, 1, 1) \n AND ";
            }

            sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5);
            sqlComando = sqlComando.concat(sqlFiltro);
            //System.out.println("-------> SQL_QUERY: ".concat(sqlComando));
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
//            sqlParam++;
//            pstmt.setLong(sqlParam, cbc.getCriterioUsuario());
            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioFolio() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolio());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioPlaza() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioPlaza());
            }
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParam++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    pstmt.setString(sqlParam, "ACTIVO");
                } else {
                    pstmt.setString(sqlParam, "CANCELADO");
                }
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsMovto_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C O N S U L T A   S A L D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadSaldoBean> onConsSaldo_ConsultaHistorico(CriterioBusquedaContaSaldoBean cbs)  {
        List<ContabilidadSaldoBean> consulta = new ArrayList<>();
        try {
            sqlParam = 4;
            String sqlComando = "SELECT sld.ccon_cta CTAM, sld.ccon_scta SC1, sld.ccon_2scta SC2, sld.ccon_3scta SC3, sld.ccon_4scta SC4,\n"
                    + "       sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes,  \n"
                    + "       sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda \n"
                    + "FROM   SAF.FDSaldosH sld                \n"
                    + "JOIN   SAF.Monedas   m                  \n"
                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
                    + "JOIN   SAF.Monedas_Corto  mc            \n"
                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
                    + "JOIN   SAF.FDCatContable  cc            \n"
                    + "ON     sld.ccon_cta   = cc.ccon_cta     \n"
                    + "AND    sld.ccon_scta  = cc.ccon_scta    \n"
                    + "AND    sld.ccon_2scta = cc.ccon_2scta   \n"
                    + "AND    sld.ccon_3scta = cc.ccon_3scta   \n"
                    + "AND    sld.ccon_4scta = cc.ccon_4scta   \n"
                    + "WHERE  sald_ax1       = ?               \n"
                    + "AND    mon_nom_moneda = ?               \n"
                    + "AND    sald_ano       = ?               \n"
                    + "AND    sald_mes       = ?               \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComando += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComando += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComando += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComando += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComando += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComando += "AND sld.sald_ax2   = ? \n";
            }
            sqlComando += "ORDER BY 1,2,3,4,5,6,7,8";
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, cbs.getCriterioAX1());
            pstmt.setString(2, cbs.getCriterioMonedaNom());
            pstmt.setShort(3, cbs.getCriterioAño());
            pstmt.setShort(4, cbs.getCriterioMes());

            if (cbs.getCriterioCTAM() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbs.getCriterioAX2());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadSaldoBean sld = new ContabilidadSaldoBean();
                sld.setSaldoCTAM(rs.getShort("CTAM"));
                sld.setSaldoSC1(rs.getShort("SC1"));
                sld.setSaldoSC2(rs.getShort("SC2"));
                sld.setSaldoSC3(rs.getShort("SC3"));
                sld.setSaldoSC4(rs.getShort("SC4"));
                sld.setSaldoCuentaNombre(rs.getString("ccon_nombre"));
                sld.setSaldoMonedaNombre(rs.getString("mon_nom_moneda_corto"));
                sld.setSaldoAX1(rs.getLong("sald_ax1"));
                sld.setSaldoAX2(rs.getLong("sald_ax2"));
                sld.setSaldoAX3(rs.getLong("sald_ax3"));
                sld.setSaldoInicioMes(rs.getDouble("sald_inicio_mes"));
                sld.setSaldoCargosMes(rs.getDouble("sald_cargos_mes"));
                sld.setSaldoAbonosMes(rs.getDouble("sald_abonos_mes"));
                sld.setSaldoActual(rs.getDouble("sald_saldo_actual"));
                sld.setSaldoFechaUltMod(rs.getDate("sald_ultmod"));
                sld.setSaldoAño(cbs.getCriterioAño());
                sld.setSaldoMes(cbs.getCriterioMes());

                consulta.add(sld);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldo_ConsultaHistorico()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadSaldoBean> onConsSaldo_Consulta(CriterioBusquedaContaSaldoBean cbs)  {
        List<ContabilidadSaldoBean> consulta = new ArrayList<>();
        String sTablaSaldos = "FDSaldos";
        try {
            if (cbs.getCriterioTipo().equals("MSA")) {
                sTablaSaldos = "FDSaldosMSA";
            }
            sqlParam = 2;
            String sqlComando = "SELECT sld.ccon_cta CTAM, sld.ccon_scta SC1, sld.ccon_2scta SC2, sld.ccon_3scta SC3, sld.ccon_4scta SC4,\n"
                    + "       sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes,  \n"
                    + "       sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda,\n"
                    + "       sald_inicio_ejer, sald_cargos_ejer, sald_abonos_ejer                              \n"
                    + "FROM   SAF.".concat(sTablaSaldos).concat(" sld\n")
                    + "JOIN   SAF.Monedas        m                   \n"
                    + "ON     mone_id_moneda = m.mon_num_pais        \n"
                    + "JOIN   SAF.Monedas_Corto  mc                  \n"
                    + "ON     mone_id_moneda = mc.mon_num_pais       \n"
                    + "JOIN   SAF.FDCatContable  cc                  \n"
                    + "ON     sld.ccon_cta   = cc.ccon_cta           \n"
                    + "AND    sld.ccon_scta  = cc.ccon_scta          \n"
                    + "AND    sld.ccon_2scta = cc.ccon_2scta         \n"
                    + "AND    sld.ccon_3scta = cc.ccon_3scta         \n"
                    + "AND    sld.ccon_4scta = cc.ccon_4scta         \n"
                    + "WHERE  sald_ax1       = ?                     \n"
                    + "AND    mon_nom_moneda = ?                     \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComando += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComando += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComando += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComando += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComando += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComando += "AND sld.sald_ax2   = ? \n";
            }
            sqlComando += "ORDER BY 1,2,3,4,5,6,7,8";
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, cbs.getCriterioAX1());
            pstmt.setString(2, cbs.getCriterioMonedaNom());
            if (cbs.getCriterioCTAM() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbs.getCriterioAX2());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadSaldoBean sld = new ContabilidadSaldoBean();
                sld.setSaldoCTAM(rs.getShort("CTAM"));
                sld.setSaldoSC1(rs.getShort("SC1"));
                sld.setSaldoSC2(rs.getShort("SC2"));
                sld.setSaldoSC3(rs.getShort("SC3"));
                sld.setSaldoSC4(rs.getShort("SC4"));
                sld.setSaldoCuentaNombre(rs.getString("ccon_nombre"));
                sld.setSaldoMonedaNombre(rs.getString("mon_nom_moneda_corto"));
                sld.setSaldoAX1(rs.getLong("sald_ax1"));
                sld.setSaldoAX2(rs.getLong("sald_ax2"));
                sld.setSaldoAX3(rs.getLong("sald_ax3"));
                sld.setSaldoInicioMes(rs.getDouble("sald_inicio_mes"));
                sld.setSaldoCargosMes(rs.getDouble("sald_cargos_mes"));
                sld.setSaldoAbonosMes(rs.getDouble("sald_abonos_mes"));
                sld.setSaldoActual(rs.getDouble("sald_saldo_actual"));
                sld.setSaldoFechaUltMod(rs.getDate("sald_ultmod"));
                sld.setSaldoAño(cbs.getCriterioAño());
                sld.setSaldoMes(cbs.getCriterioMes());

                sld.setSaldoInicioEjer(rs.getDouble("sald_inicio_ejer"));
                sld.setSaldoAbonosEjer(rs.getDouble("sald_cargos_ejer"));
                sld.setSaldoCargosEjer(rs.getDouble("sald_abonos_ejer"));

                consulta.add(sld);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldo_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadSaldoBean> getBalanceInquiriesHistorical(CriterioBusquedaContaSaldoBean cbs, String offset) {
        String sqlComandoBalanceInquiries = "";
        Integer sqlParamBalanceInquiries = 4;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;

        List<ContabilidadSaldoBean> consulta = new ArrayList<>();
        int numericOffset = Integer.parseInt(offset);
        int between = numericOffset + 1;
        int and = numericOffset + 10;
        try {

            sqlComandoBalanceInquiries = "SELECT CTAM, SC1, SC2, SC3, SC4,\n"
                    + "       sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes,  \n"
                    + "       sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda, mone_id_moneda, \n"
                    + "       sald_ncargos_mes, sald_nabonos_mes \n"
                    + "FROM( \n"
                    + "SELECT ROW_NUMBER() OVER (ORDER BY sld.ccon_cta,sld.ccon_scta,sld.ccon_2scta,sld.ccon_3scta,sld.ccon_4scta ,sald_ax1, sald_ax2, sald_ax3) AS Ind, \n"
                    + "       sld.ccon_cta CTAM, sld.ccon_scta SC1, sld.ccon_2scta SC2, sld.ccon_3scta SC3, sld.ccon_4scta SC4,\n"
                    + "       sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes,  \n"
                    + "       sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda, mone_id_moneda,\n"
                    + "       sald_ncargos_mes, sald_nabonos_mes \n"
                    + "FROM   SAF.FDSaldosH sld                \n"
                    + "JOIN   SAF.Monedas   m                  \n"
                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
                    + "JOIN   SAF.Monedas_Corto  mc            \n"
                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
                    + "JOIN   SAF.FDCatContable  cc            \n"
                    + "ON     sld.ccon_cta   = cc.ccon_cta     \n"
                    + "AND    sld.ccon_scta  = cc.ccon_scta    \n"
                    + "AND    sld.ccon_2scta = cc.ccon_2scta   \n"
                    + "AND    sld.ccon_3scta = cc.ccon_3scta   \n"
                    + "AND    sld.ccon_4scta = cc.ccon_4scta   \n"
                    + "WHERE  sald_ax1       = ?               \n"
                    + "AND    mon_nom_moneda = ?               \n"
                    + "AND    sald_ano       = ?               \n"
                    + "AND    sald_mes       = ?               \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComandoBalanceInquiries += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComandoBalanceInquiries += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComandoBalanceInquiries += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComandoBalanceInquiries += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComandoBalanceInquiries += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComandoBalanceInquiries += "AND sld.sald_ax2   = ? \n";
            }
            //sqlComandoBalanceInquiries+= "ORDER BY 1,2,3,4,5,6 LIMIT 10 OFFSET ".concat(offset);
            sqlComandoBalanceInquiries += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoBalanceInquiries += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoBalanceInquiries += "AND ".concat(Integer.toString(and));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoBalanceInquiries);
            preparedStatement.setLong(1, cbs.getCriterioAX1());
            preparedStatement.setString(2, cbs.getCriterioMonedaNom());
            preparedStatement.setShort(3, cbs.getCriterioAño());
            preparedStatement.setShort(4, cbs.getCriterioMes());

            if (cbs.getCriterioCTAM() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setInt(sqlParamBalanceInquiries, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setInt(sqlParamBalanceInquiries, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setInt(sqlParamBalanceInquiries, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setInt(sqlParamBalanceInquiries, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setInt(sqlParamBalanceInquiries, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParamBalanceInquiries++;
                preparedStatement.setLong(sqlParamBalanceInquiries, cbs.getCriterioAX2());
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadSaldoBean sld = new ContabilidadSaldoBean();
                sld.setSaldoCTAM(resultSet.getShort("CTAM"));
                sld.setSaldoSC1(resultSet.getShort("SC1"));
                sld.setSaldoSC2(resultSet.getShort("SC2"));
                sld.setSaldoSC3(resultSet.getShort("SC3"));
                sld.setSaldoSC4(resultSet.getShort("SC4"));
                sld.setSaldoCuentaNombre(resultSet.getString("ccon_nombre"));
                sld.setSaldoMonedaNombre(resultSet.getString("mon_nom_moneda_corto"));
                sld.setSaldoAX1(resultSet.getLong("sald_ax1"));
                sld.setSaldoAX2(resultSet.getLong("sald_ax2"));
                sld.setSaldoAX3(resultSet.getLong("sald_ax3"));
                sld.setSaldoInicioMes(resultSet.getDouble("sald_inicio_mes"));
                sld.setSaldoCargosMes(resultSet.getDouble("sald_cargos_mes"));
                sld.setSaldoAbonosMes(resultSet.getDouble("sald_abonos_mes"));
                sld.setSaldoActual(resultSet.getDouble("sald_saldo_actual"));
                sld.setSaldoFechaUltMod(resultSet.getDate("sald_ultmod"));
                sld.setSaldoAño(cbs.getCriterioAño());
                sld.setSaldoMes(cbs.getCriterioMes());
                sld.setSaldoMonedaNumero(resultSet.getShort("mone_id_moneda"));
                sld.setSaldoNAbonosMes(resultSet.getInt("sald_nabonos_mes"));
                sld.setSaldoNcargosMes(resultSet.getInt("sald_ncargos_mes"));

                consulta.add(sld);
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBalanceInquiriesHistorical:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                   logger.error("Function ::getBalanceInquiriesHistorical:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBalanceInquiriesHistorical:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        return consulta;
    }

    public List<ContabilidadSaldoBean> getBalanceInquiries(CriterioBusquedaContaSaldoBean cbs, String offset) {
        String sqlComandoBalance = "";
        Integer sqlParamBalance = 2;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<ContabilidadSaldoBean> consulta = new ArrayList<>();
        String sTablaSaldos = "FDSaldos";

        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            if (cbs.getCriterioTipo().equals("MSA")) {
                    sTablaSaldos = "FDSaldosMSA";
            }

            sqlComandoBalance = "SELECT CTAM, SC1, SC2, SC3, SC4, \n"
                    + "sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes, \n"
                    + "sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda, \n"
                    + "sald_inicio_ejer, sald_cargos_ejer, sald_abonos_ejer, mone_id_moneda,\n"
                    + "sald_ncargos_mes, sald_nabonos_mes,sald_ncargos_ejer, sald_nabonos_ejer \n"
                    + "FROM ( \n"
                    + "  SELECT ROW_NUMBER() OVER (ORDER BY sld.ccon_cta,sld.ccon_scta,sld.ccon_2scta,sld.ccon_3scta,sld.ccon_4scta ,sald_ax1, sald_ax2, sald_ax3) AS Ind, \n"
                    + "       sld.ccon_cta CTAM, sld.ccon_scta SC1, sld.ccon_2scta SC2, sld.ccon_3scta SC3, sld.ccon_4scta SC4,\n"
                    + "       sald_ax1, sald_ax2, sald_ax3, sald_inicio_mes, sald_cargos_mes, sald_abonos_mes,  \n"
                    + "       sald_saldo_actual, sald_ultmod, ccon_nombre, mon_nom_moneda_corto, mon_nom_moneda,\n"
                    + "       sald_inicio_ejer, sald_cargos_ejer, sald_abonos_ejer, mone_id_moneda, sald_ncargos_mes, sald_nabonos_mes,\n"
                    + "       sald_ncargos_ejer, sald_nabonos_ejer\n"
                    + "  FROM   SAF.".concat(sTablaSaldos).concat(" sld\n")
                    + "  JOIN   SAF.Monedas        m                   \n"
                    + "  ON     mone_id_moneda = m.mon_num_pais        \n"
                    + "  JOIN   SAF.Monedas_Corto  mc                  \n"
                    + "  ON     mone_id_moneda = mc.mon_num_pais       \n"
                    + "  JOIN   SAF.FDCatContable  cc                  \n"
                    + "  ON     sld.ccon_cta   = cc.ccon_cta           \n"
                    + "  AND    sld.ccon_scta  = cc.ccon_scta          \n"
                    + "  AND    sld.ccon_2scta = cc.ccon_2scta         \n"
                    + "  AND    sld.ccon_3scta = cc.ccon_3scta         \n"
                    + "  AND    sld.ccon_4scta = cc.ccon_4scta         \n"
                    + "  WHERE  sald_ax1       = ?                     \n"
                    + "  AND    mon_nom_moneda = ?                     \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComandoBalance += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComandoBalance += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComandoBalance += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComandoBalance += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComandoBalance += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComandoBalance += "AND sld.sald_ax2   = ? \n";
            }

            sqlComandoBalance += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoBalance += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoBalance += "AND ".concat(Integer.toString(and));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoBalance);
            preparedStatement.setLong(1, cbs.getCriterioAX1());
            preparedStatement.setString(2, cbs.getCriterioMonedaNom());
            if (cbs.getCriterioCTAM() != null) {
                sqlParamBalance++;
                preparedStatement.setInt(sqlParamBalance, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParamBalance++;
                preparedStatement.setInt(sqlParamBalance, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParamBalance++;
                preparedStatement.setInt(sqlParamBalance, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParamBalance++;
                preparedStatement.setInt(sqlParamBalance, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParamBalance++;
                preparedStatement.setInt(sqlParamBalance, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParamBalance++;
                preparedStatement.setLong(sqlParamBalance, cbs.getCriterioAX2());
            }
                resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadSaldoBean sld = new ContabilidadSaldoBean();
                sld.setSaldoCTAM(resultSet.getShort("CTAM"));
                sld.setSaldoSC1(resultSet.getShort("SC1"));
                sld.setSaldoSC2(resultSet.getShort("SC2"));
                sld.setSaldoSC3(resultSet.getShort("SC3"));
                sld.setSaldoSC4(resultSet.getShort("SC4"));
                sld.setSaldoCuentaNombre(resultSet.getString("ccon_nombre"));
                sld.setSaldoMonedaNombre(resultSet.getString("mon_nom_moneda_corto"));
                sld.setSaldoAX1(resultSet.getLong("sald_ax1"));
                sld.setSaldoAX2(resultSet.getLong("sald_ax2"));
                sld.setSaldoAX3(resultSet.getLong("sald_ax3"));
                sld.setSaldoInicioMes(resultSet.getDouble("sald_inicio_mes"));
                sld.setSaldoCargosMes(resultSet.getDouble("sald_cargos_mes"));
                sld.setSaldoAbonosMes(resultSet.getDouble("sald_abonos_mes"));
                sld.setSaldoActual(resultSet.getDouble("sald_saldo_actual"));
                sld.setSaldoFechaUltMod(resultSet.getDate("sald_ultmod"));
                sld.setSaldoAño(cbs.getCriterioAño());
                sld.setSaldoMes(cbs.getCriterioMes());

                sld.setSaldoInicioEjer(resultSet.getDouble("sald_inicio_ejer"));
                sld.setSaldoAbonosEjer(resultSet.getDouble("sald_abonos_ejer"));
                sld.setSaldoCargosEjer(resultSet.getDouble("sald_cargos_ejer"));
                sld.setSaldoMonedaNumero(resultSet.getShort("mone_id_moneda"));
                sld.setSaldoNAbonosMes(resultSet.getInt("sald_nabonos_mes"));
                sld.setSaldoNcargosMes(resultSet.getInt("sald_ncargos_mes"));
                sld.setSaldoNAbonosEjer(resultSet.getInt("sald_nabonos_ejer"));
                sld.setSaldoNcargosEjer(resultSet.getInt("sald_ncargos_ejer"));

                consulta.add(sld);
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage() + e.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBalanceInquiries:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getBalanceInquiries:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                   logger.error("Function ::getBalanceInquiries:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        return consulta;
    }

    public int getBalanceInquiriesHistoricalTotalRows(CriterioBusquedaContaSaldoBean cbs)  {
        int totalRows = 0;

        try {
            sqlParam = 4;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.FDSaldosH sld                \n"
                    + "JOIN   SAF.Monedas   m                  \n"
                    + "ON     mone_id_moneda = m.mon_num_pais  \n"
                    + "JOIN   SAF.Monedas_Corto  mc            \n"
                    + "ON     mone_id_moneda = mc.mon_num_pais \n"
                    + "JOIN   SAF.FDCatContable  cc            \n"
                    + "ON     sld.ccon_cta   = cc.ccon_cta     \n"
                    + "AND    sld.ccon_scta  = cc.ccon_scta    \n"
                    + "AND    sld.ccon_2scta = cc.ccon_2scta   \n"
                    + "AND    sld.ccon_3scta = cc.ccon_3scta   \n"
                    + "AND    sld.ccon_4scta = cc.ccon_4scta   \n"
                    + "WHERE  sald_ax1       = ?               \n"
                    + "AND    mon_nom_moneda = ?               \n"
                    + "AND    sald_ano       = ?               \n"
                    + "AND    sald_mes       = ?               \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComando += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComando += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComando += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComando += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComando += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComando += "AND sld.sald_ax2   = ? \n";
            }
            //sqlComando+= "ORDER BY 1,2,3,4,5,6";
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, cbs.getCriterioAX1());
            pstmt.setString(2, cbs.getCriterioMonedaNom());
            pstmt.setShort(3, cbs.getCriterioAño());
            pstmt.setShort(4, cbs.getCriterioMes());

            if (cbs.getCriterioCTAM() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbs.getCriterioAX2());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getShort("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldo_ConsultaHistorico()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }   

    public int getBalanceInquiriesTotalRows(CriterioBusquedaContaSaldoBean cbs)  {
        int totalRows = 0;
        String sTablaSaldos = "FDSaldos";

        try {
            if (cbs.getCriterioTipo().equals("MSA")) {
                sTablaSaldos = "FDSaldosMSA";
            }
            sqlParam = 2;
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "FROM   SAF.".concat(sTablaSaldos).concat(" sld\n")
                    + "JOIN   SAF.Monedas        m                   \n"
                    + "ON     mone_id_moneda = m.mon_num_pais        \n"
                    + "JOIN   SAF.Monedas_Corto  mc                  \n"
                    + "ON     mone_id_moneda = mc.mon_num_pais       \n"
                    + "JOIN   SAF.FDCatContable  cc                  \n"
                    + "ON     sld.ccon_cta   = cc.ccon_cta           \n"
                    + "AND    sld.ccon_scta  = cc.ccon_scta          \n"
                    + "AND    sld.ccon_2scta = cc.ccon_2scta         \n"
                    + "AND    sld.ccon_3scta = cc.ccon_3scta         \n"
                    + "AND    sld.ccon_4scta = cc.ccon_4scta         \n"
                    + "WHERE  sald_ax1       = ?                     \n"
                    + "AND    mon_nom_moneda = ?                     \n";

            if (cbs.getCriterioCTAM() != null) {
                sqlComando += "AND sld.ccon_cta   = ? \n";
            }
            if (cbs.getCriterioSC1() != null) {
                sqlComando += "AND sld.ccon_scta  = ? \n";
            }
            if (cbs.getCriterioSC2() != null) {
                sqlComando += "AND sld.ccon_2scta = ? \n";
            }
            if (cbs.getCriterioSC3() != null) {
                sqlComando += "AND sld.ccon_3scta = ? \n";
            }
            if (cbs.getCriterioSC4() != null) {
                sqlComando += "AND sld.ccon_4scta = ? \n";
            }
            if (cbs.getCriterioAX2() != null) {
                sqlComando += "AND sld.sald_ax2   = ? \n";
            }
            //sqlComando+= "ORDER BY 1,2,3,4,5,6";
            //System.out.println(sqlComando);
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, cbs.getCriterioAX1());
            pstmt.setString(2, cbs.getCriterioMonedaNom());
            if (cbs.getCriterioCTAM() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioCTAM());
            }
            if (cbs.getCriterioSC1() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC1());
            }
            if (cbs.getCriterioSC2() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC2());
            }
            if (cbs.getCriterioSC3() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC3());
            }
            if (cbs.getCriterioSC4() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbs.getCriterioSC4());
            }
            if (cbs.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbs.getCriterioAX2());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");

            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldo_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C O N S U L T A   S A L D O S   P R O M E D I O
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadSaldoPromBean> onConsSaldoProm_Consulta(CriterioBusquedaBean cb)  {
        List<ContabilidadSaldoPromBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT (RTrim(Char(spr_num_contrato))||'.- '||cto_nom_contrato) Contrato, \n"
                    + "        spr_num_contrato, spr_sub_contrato,        \n"
                    + "        Date(RTrim(char(spr_ano_alta_reg))||'-' || \n"
                    + "             RTrim(char(spr_mes_alta_reg))||'-' || \n"
                    + "             RTrim(char(spr_dia_alta_reg))) Fecha, \n"
                    + "        spr_imp_saldo_acum/spr_num_dias_acum AS spr_imp_saldo_acum,\n"
                    + "        spr_imp_patrim/spr_num_dias_acum     AS spr_imp_patrim,    \n"
                    + "        spr_imp_inv/spr_num_dias_acum        AS spr_imp_inv,       \n"
                    + "       (spr_imp_saldo_acum+ spr_imp_patrim + spr_imp_inv)/spr_num_dias_acum AS Total \n"
                    + "FROM    SAF.Saldopro, SAF.Contrato                                 \n"
                    + "WHERE   cto_num_contrato = spr_num_contrato                        \n"
                    + "AND     cto_num_contrato = ?                                       \n"
                    + "AND     Date(RTrim(char(spr_ano_alta_reg))||'-'||                  \n"
                    + "             RTrim(char(spr_mes_alta_reg))||'-'||                  \n"
                    + "             RTrim(char(spr_dia_alta_reg))) Between ? AND ?";
            if (cb.getCriterioContratoNumeroSub() != null) {
                sqlComando += "AND spr_sub_contrato = ? \n";
            }
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, cb.getCriterioContratoNumero());
            pstmt.setDate(2, cb.getCriterioFechaDel());
            pstmt.setDate(3, cb.getCriterioFechaAl());
            if (cb.getCriterioContratoNumeroSub() != null) {
                pstmt.setInt(4, cb.getCriterioContratoNumeroSub());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadSaldoPromBean csp = new ContabilidadSaldoPromBean();
                csp.setSaldoPromContratoNum(rs.getLong("spr_num_contrato"));
                csp.setSaldoPromContratoNumSub(rs.getLong("spr_sub_contrato"));
                csp.setSaldoPromContratoNom(rs.getString("Contrato"));
                csp.setSaldoPromFecha(rs.getDate("Fecha"));

                csp.setSaldoPromActivo(rs.getDouble("spr_imp_saldo_acum"));
                csp.setSaldoPromPatrim(rs.getDouble("spr_imp_patrim"));
                csp.setSaldoPromInv(rs.getDouble("spr_imp_inv"));
                csp.setSaldoPromTotal(rs.getDouble("Total"));

                consulta.add(csp);
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldoProm_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public List<ContabilidadSaldoPromBean> getAverageBalanceInquiriesPaginated(CriterioBusquedaBean cb, String offset) {
        String sqlComandoAverageBalance = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;

        List<ContabilidadSaldoPromBean> consulta = new ArrayList<>();

        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlComandoAverageBalance = "SELECT Contrato, spr_num_contrato, spr_sub_contrato, Fecha, \n"
                    + "spr_imp_saldo_acum, spr_imp_patrim, spr_imp_inv, Total \n"
                    + "FROM ( \n"
                    + "  SELECT ROW_NUMBER() OVER (ORDER BY C.cto_nom_contrato ASC) AS Ind, \n"
                    + "      (RTrim(Char(spr_num_contrato))||'.- '|| C.cto_nom_contrato) Contrato, \n"
                    + "      spr_num_contrato, spr_sub_contrato,        \n"
                    + "      Date(RTrim(char(spr_ano_alta_reg))||'-' || \n"
                    + "             RTrim(char(spr_mes_alta_reg))||'-' || \n"
                    + "             RTrim(char(spr_dia_alta_reg))) Fecha, \n"
                    + "      spr_imp_saldo_acum/spr_num_dias_acum AS spr_imp_saldo_acum,\n"
                    + "      spr_imp_patrim/spr_num_dias_acum     AS spr_imp_patrim,    \n"
                    + "      spr_imp_inv/spr_num_dias_acum        AS spr_imp_inv,       \n"
                    + "      (spr_imp_saldo_acum+ spr_imp_patrim + spr_imp_inv)/spr_num_dias_acum AS Total \n"
                    + "  FROM  SAF.Saldopro S \n"
                    + "  INNER JOIN SAF.Contrato C ON S.spr_num_contrato = C.cto_num_contrato \n"
                    + "  INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = S.spr_num_contrato AND V.USU_NUM_USUARIO = ? \n"
                    + "  WHERE S.spr_num_contrato = ? \n"
                    + "  AND     Date(RTrim(char(spr_ano_alta_reg))||'-'|| \n"
                    + "               RTrim(char(spr_mes_alta_reg))||'-'|| \n"
                    + "               RTrim(char(spr_dia_alta_reg))) Between ? AND ? \n";
            if (cb.getCriterioContratoNumeroSub() != null) {
                sqlComandoAverageBalance += "AND spr_sub_contrato = ? ";
            }

            //sqlComandoAverageBalance = sqlComandoAverageBalance.concat(" LIMIT 10 OFFSET ".concat(offset));
            sqlComandoAverageBalance += ") AS Consulta \n"
                    + "WHERE Consulta.Ind \n";
            sqlComandoAverageBalance += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoAverageBalance += "AND ".concat(Integer.toString(and));
            //System.out.println("-------> SQL: ".concat(sqlComandoAverageBalance));

            conn = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(sqlComandoAverageBalance);
            preparedStatement.setInt(1, cb.getCriterioUsuario());
            preparedStatement.setLong(2, cb.getCriterioContratoNumero());
            preparedStatement.setDate(3, cb.getCriterioFechaDel());
            preparedStatement.setDate(4, cb.getCriterioFechaAl());
            if (cb.getCriterioContratoNumeroSub() != null) {
                preparedStatement.setInt(5, cb.getCriterioContratoNumeroSub());
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ContabilidadSaldoPromBean csp = new ContabilidadSaldoPromBean();
                csp.setSaldoPromContratoNum(resultSet.getLong("spr_num_contrato"));
                csp.setSaldoPromContratoNumSub(resultSet.getLong("spr_sub_contrato"));
                csp.setSaldoPromContratoNom(resultSet.getString("Contrato"));
                csp.setSaldoPromFecha(resultSet.getDate("Fecha"));

                csp.setSaldoPromActivo(resultSet.getDouble("spr_imp_saldo_acum"));
                csp.setSaldoPromPatrim(resultSet.getDouble("spr_imp_patrim"));
                csp.setSaldoPromInv(resultSet.getDouble("spr_imp_inv"));
                csp.setSaldoPromTotal(resultSet.getDouble("Total"));

                consulta.add(csp);
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
           logger.error( e.getMessage() + e.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getAverageBalanceInquiriesPaginated:: Error al cerrar PreparetStatement."+ e.getMessage());
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getAverageBalanceInquiriesPaginated:: Error al cerrar ResultSet."+ e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getAverageBalanceInquiriesPaginated:: Error al cerrar Connection."+ e.getMessage());
                }
            }
        }
        return consulta;
    }

    public int getAverageBalanceInquiriesTotalRows(CriterioBusquedaBean cb)  {
        int totalRows = 0;

        try {
            String sqlComando = "SELECT COUNT(*) AS total_rows \n"
                    + "  FROM  SAF.Saldopro S \n"
                    + "  INNER JOIN SAF.Contrato C ON S.spr_num_contrato = C.cto_num_contrato \n"
                    + "  INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = S.spr_num_contrato AND V.USU_NUM_USUARIO = ? \n"
                    + "  WHERE S.spr_num_contrato = ? \n"
                    + "  AND     Date(RTrim(char(spr_ano_alta_reg))||'-'|| \n"
                    + "               RTrim(char(spr_mes_alta_reg))||'-'|| \n"
                    + "               RTrim(char(spr_dia_alta_reg))) Between ? AND ? \n";
            if (cb.getCriterioContratoNumeroSub() != null) {
                sqlComando += "AND spr_sub_contrato = ? ";
            }
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, cb.getCriterioUsuario());
            pstmt.setLong(2, cb.getCriterioContratoNumero());
            pstmt.setDate(3, cb.getCriterioFechaDel());
            pstmt.setDate(4, cb.getCriterioFechaAl());
            if (cb.getCriterioContratoNumeroSub() != null) {
                pstmt.setInt(5, cb.getCriterioContratoNumeroSub());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onConsSaldoProm_Consulta()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return totalRows;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   C A N C E L A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadMovtoBean> onCancelaOper_ConsultaMovtos(CriterioBusquedaContaBean cbc)  {
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        try {
            sqlParam = 3;
            String sqlComando = "SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, \n"
                    + "       detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, \n"
                    + "       movi_usuario, movi_status    \n"
                    + "FROM   SAF.FDMovimiento             \n"
                    + "INNER JOIN SAF.VISTA_USUARIO ON CTO_NUM_CONTRATO = FISO_ID_FISO AND USU_NUM_USUARIO = ?  \n"
                    + "WHERE " // quitar para Conta 2
                    + "Extract(Month FROM movi_fecha_mov) = ? \n"
                    + "AND    Extract(Year  FROM movi_fecha_mov) = ? \n";

            if (cbc.getCriterioCveMovtoCancelado() != null) {
                if ((cbc.getCriterioCveMovtoCancelado().equals("NO")) || (cbc.getCriterioCveMovtoCancelado().equals("SI"))) {
                    sqlComando += "AND movi_status = ? \n";
                }
            }
            //"AND    movi_status                        = ? \n";
            if (cbc.getCriterioAX1() != null) {
                sqlComando += "AND fiso_id_fiso  = ? \n";
            }
            if (cbc.getCriterioAX2() != null) {
                sqlComando += "AND sfis_id_sfiso = ? \n";
            }
            if (cbc.getCriterioTransId() != null) {
                sqlComando += "AND tran_id_tran  = ? \n";
            }
//            if ((cbc.getCriterioFolioIni()!=null)&&(cbc.getCriterioFolioFin()!= null)) sqlComando+= "AND detm_folio_op Between ? AND ? \n";
            if (cbc.getCriterioFolioIni() != null) {
                if (cbc.getCriterioFolioFin() == null) {

                    sqlComando += "AND detm_folio_op = ? \n";

                } else {
                    sqlComando += "AND detm_folio_op >= ? \n";
                }
            }
            if (cbc.getCriterioFolioFin() != null) {
                if (cbc.getCriterioFolioIni() == null) {
                    sqlComando += "AND detm_folio_op = ? \n";
                } else {
                    sqlComando += "AND detm_folio_op <= ? \n";
                }
            }
            if (cbc.getCriterioFechaDD() != null) {
                sqlComando += "AND Extract(Day  FROM movi_fecha_mov) = ? \n";
            }
            sqlComando += "ORDER BY detm_folio_op DESC";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(2, cbc.getCriterioFechaMM());
            pstmt.setInt(3, cbc.getCriterioFechaYYYY());
            //pstmt.setString(3, "ACTIVO");
            if (cbc.getCriterioCveMovtoCancelado() != null) {
                sqlParam++;
                if (cbc.getCriterioCveMovtoCancelado().equals("NO")) {
                    pstmt.setString(sqlParam, "ACTIVO");
                } else {
                    pstmt.setString(sqlParam, "CANCELADO");
                }
            }
            if (cbc.getCriterioAX1() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX1());
            }
            if (cbc.getCriterioAX2() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioAX2());
            }
            if (cbc.getCriterioTransId() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioTransId());
            }
            if (cbc.getCriterioFolioIni() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolioIni());
            }
            if (cbc.getCriterioFolioFin() != null) {
                sqlParam++;
                pstmt.setLong(sqlParam, cbc.getCriterioFolioFin());
            }
            if (cbc.getCriterioFechaDD() != null) {
                sqlParam++;
                pstmt.setInt(sqlParam, cbc.getCriterioFechaDD());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(rs.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(rs.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(rs.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoFolio(rs.getLong("detm_folio_op"));
                movto.setMovtoTransaccId(rs.getString("tran_id_tran"));
                movto.setMovtoDesc(rs.getString("movi_descripcion"));
                movto.setMovtoImporte(rs.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(rs.getLong("movi_usuario"));
                movto.setMovtoStatus(rs.getString("movi_status"));
                consulta.add(movto);
            }
        } catch (SQLException Err) {
            logger.error("Descripción: " + Err.getMessage()  + "onCancelaOper_ConsultaMovtos()");
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   O P E R A C I O N
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onParamOperacion_ObtenInfo(ContabilidadOperacionBean oper)  {
        try {
            String sqlComando = "SELECT oper_id_operacion, oper_nombre, oper_tarifa,\n"
                    + "       oper_alta, oper_ultmod, oper_status         \n"
                    + "FROM   SAF.FDOperacion                             \n"
                    + "WHERE  oper_id_operacion = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, oper.getOperacionId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                oper.setOperacionNombre(rs.getString("oper_nombre"));
                oper.setOperacionFechaReg(rs.getDate("oper_alta"));
                oper.setOperacionFechaMod(rs.getDate("oper_ultmod"));
                oper.setOperacionStatus(rs.getString("oper_status"));
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaOper_ConsultaMovtos()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   D E T A L L E   D E   V A L O R ( D E T V A L )
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onDetVal_ObtenInformacion(ContabilidadDetValorBean detVal)  {
        try {
            String sqlComando = "SELECT cto_num_contrato, cto_nom_contrato, dev_sub_contrato, dev_contrato_inter, dev_num_sec_opera, \n"
                    + "       dev_num_operacion, oper_nombre, dev_entidad_fin, int_intermediario, dev_cve_tipo_opera,      \n"
                    + "       Nvl(dev_nom_pizarra, ' ')Piz, Nvl(dev_serie_emision, ' ')Ser, Nvl(dev_num_cupon_vig, ' ')Cup,\n"
                    + "       dev_imp_precio_tit, dev_num_titulos, dev_num_imp_tit, dev_pje_tasa_desc, dev_tasa_rendimien, \n"
                    + "       dev_imp_utilidad, dev_imp_perdida, dev_imp_intereses, dev_imp_isr, mon_nom_moneda, dev_imp_moneda ImpMe,\n"
                    + "       dev_num_plazo, dev_folio_oper_pla, dev_num_imp_premio, dev_imp_tip_cambio, Nvl(dev_cve_tipo_dere, ' ') DereTipo, \n"
                    + "       Nvl(dev_cve_factor, ' ') DereCveFact, Nvl(dev_nom_pizarra_an, ' ')DerePiz, Nvl(dev_serie_emis_ant, ' ')DereSerie,\n"
                    + "       Nvl(dev_cupon_vig_ant, ' ') DereCupon, dev_num_noneda, Nvl(ins_nom_instrume, ' ') NomIns  \n"
                    + "FROM   SAF.DetValor                          \n"
                    + "JOIN   SAF.Contrato                          \n"
                    + "ON     dev_num_contrato  = cto_num_contrato  \n"
                    + "JOIN   SAF.FDOperacion                       \n"
                    + "ON     dev_num_operacion = oper_id_operacion \n"
                    + "JOIN   SAF.INTERMED                          \n"
                    + "ON     dev_entidad_fin   = int_entidad_fin   \n"
                    + "JOIN   SAF.Monedas                           \n"
                    + "ON     dev_num_noneda    = mon_num_pais      \n"
                    + "LEFT OUTER JOIN  INSTRUME ON INS_CVE_TIPO_MERCA = dev_cve_tipo_merca AND INS_NUM_INSTRUME = dev_num_instrume\n"
                    + "WHERE  dev_folio_opera   = ? ";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, detVal.getDetValFolio());
//            pstmt.setLong(1, 455);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                detVal.setDetValContratoNumero(rs.getLong("cto_num_contrato"));
                detVal.setDetValContratoNumeroSub(rs.getInt("dev_sub_contrato"));
                detVal.setDetValContratoNombre(rs.getString("cto_nom_contrato"));
                detVal.setDetValContratoInver(rs.getString("dev_contrato_inter"));
                detVal.setDetValOperacionId(rs.getString("dev_num_operacion"));
                detVal.setDetValOperacionNombre(rs.getString("oper_nombre"));

                if (rs.getLong("dev_folio_oper_pla") != 0) {

                    detVal.setDetValRepFolio(rs.getLong("dev_folio_oper_pla"));
                    detVal.setDetValRepPremio(rs.getDouble("dev_num_imp_premio"));
                    detVal.setDetValRepPlazo(rs.getShort("dev_num_plazo"));
                    detVal.setDetValRepTasa(rs.getDouble("dev_tasa_rendimien"));
                    detVal.setDetValRepInstrume(rs.getString("NomIns"));
                    detVal.setDetValRepImporte(rs.getDouble("dev_num_imp_tit"));
                } else {
                    detVal.setDetValPizarra(rs.getString("Piz"));
                    detVal.setDetValSerie(rs.getString("Ser"));
                    detVal.setDetValCupon(rs.getString("Cup"));

                    detVal.setDetValTituloNum(rs.getDouble("dev_num_titulos"));
                    detVal.setDetValTituloPrecio(rs.getDouble("dev_imp_precio_tit"));
                    detVal.setDetValTasaRend(rs.getDouble("dev_tasa_rendimien"));
                    detVal.setDetValTituloImp(rs.getDouble("dev_num_imp_tit"));
                    detVal.setDetValImpPerdida(rs.getDouble("dev_imp_perdida"));
                }
                detVal.setDetValRepInteres(rs.getDouble("dev_imp_intereses"));
                detVal.setDetValRepISR(rs.getDouble("dev_imp_isr"));
                detVal.setDetValDereTipo(rs.getString("DereTipo"));
                detVal.setDetValDereFactor(rs.getString("DereCveFact"));
                detVal.setDetValDerePizarra(rs.getString("DerePiz"));
                detVal.setDetValDereSerieAnt(rs.getString("DereSerie"));
                detVal.setDetValDereCupon(rs.getString("DereCupon"));

                detVal.setDetValIntermedSec(rs.getShort("dev_entidad_fin"));
                detVal.setDetValIntermedNom(rs.getString("int_intermediario"));
                detVal.setDetValImpUtilidad(rs.getDouble("dev_imp_utilidad"));
                detVal.setDetValRepIntermedSec(rs.getShort("dev_entidad_fin"));
//                    detVal.setDetValRepIntermedNom(rs.getString("int_intermediario"));

                if (rs.getInt("dev_num_noneda") != 1) {
                    detVal.setDetValRepMdaNom(rs.getString("mon_nom_moneda"));
                    detVal.setDetValRepImporteTC(rs.getDouble("dev_imp_tip_cambio"));
                    detVal.setDetValRepImporteME(rs.getDouble("ImpMe"));
                }
                detVal.setMuestraValorDetalle("");
            } else {
                detVal.setMuestraValorDetalle("none");
            }
           
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onDetVal_ObtenInformacion()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   D A T O S M O V ( D A T O V A L )
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<ContabilidadDatoMovBean> onDatoMov_ObtenInformacion(Long folioContable)  {
        List<ContabilidadDatoMovBean> consulta = new ArrayList<>();
        try {
            String sqlComando = "SELECT datm_secuencia, datm_concepto, datm_valor FROM SAF.FDDatosMov WHERE detm_folio_op = ? ORDER BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, folioContable);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ContabilidadDatoMovBean dm = new ContabilidadDatoMovBean();
                if (!rs.getString("datm_concepto").trim().equals(new String())) {
                    dm.setDatoMovFolio(folioContable);
                    dm.setDatoMovSec(rs.getShort("datm_secuencia"));
                    dm.setDatoMovConcepto(rs.getString("datm_concepto"));
                    dm.setDatoMovValor(rs.getString("datm_valor"));
                }
                consulta.add(dm);
            }
           
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onDatoMov_ObtenInformacion()";
            logger.error(mensajeError);
        } finally {

            onCierraConexion();
        }
        return consulta;
    }
    
    
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C H E C K E R    M O N E T A R I O  * * * * * * * * * * * * * * * * * * * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public List<TPendientesBean> onContabilidad_ConsultaChecker(String FolioDel, String FolioAl, String Nombre, Double dImporte) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_ConsultaChecker()");

        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        int index = 2;
        int folioDel = 0, folioAl;
        StringBuilder stringBuilder = new StringBuilder();
        List<TPendientesBean> tPendientes = new ArrayList<>();

        try {

            //================================================================================
            // MUESTRA LAS INSTRUCCIONES PENDIENTES DE AUTORIZACION A FECHA CONTABLE
            //================================================================================
            stringBuilder.append("SELECT TPD_FOLIO,			TPD_TIPO_MOV,	TPD_TRANSACC,	TPD_FOLIO_OPERA, TPD_FECHA_MOV,");
            stringBuilder.append(" 		 USU_NOM_USUARIO,	TPD_IMPORTE,	MON_NOM_MONEDA, TPD_MONEDA, 	 TPD_STATUS, ");
            stringBuilder.append(" 		 TPD_NUM_FISO, 		TPD_USUARIO_OP, TPD_OBS_CHECKER,TPD_OBS_MAKER,	 TPD_TIPO_MOV, ");
            stringBuilder.append(" 		 TPD_FECHA_OP,		TPD_USUARIO_AU,	TPD_FECHA_AU, 	TPD_SUB_FISO,	 TPD_NUM_INSTR  ");
            stringBuilder.append(" FROM SAF.TPENDIENTES t Inner Join SAF.VISTA_USUARIO w On CTO_NUM_CONTRATO = TPD_NUM_FISO ");
            stringBuilder.append(" AND w.USU_NUM_USUARIO = ? "); //USUARIO
            stringBuilder.append(" INNER JOIN SAF.MONEDAS On MON_NUM_PAIS = t.TPD_MONEDA ");
            stringBuilder.append(" INNER JOIN SAF.USUARIOS U On TPD_USUARIO_OP = U.USU_NUM_USUARIO ");
            stringBuilder.append(" INNER JOIN SAF.PUESTOS P On P.PUE_NUM_PUESTO = U.USU_NUM_PUESTO ");
            stringBuilder.append(" WHERE  t.TPD_TIPO=1 AND t.TPD_STATUS = 'ACTIVO' ");
            stringBuilder.append(" AND t.TPD_FECHA_MOV = ? ");
            stringBuilder.append(" AND (t.TPD_NUM_FISO=0 OR t.TPD_NUM_FISO  >= 0 ) ");
    
            if ((FolioDel != null && !FolioDel.equals("")) && (FolioAl != null && !FolioAl.equals(""))) {
                stringBuilder.append(" AND TPD_FOLIO BETWEEN ? AND ?");
            }

            if ((FolioDel != null && !FolioDel.equals("")) && (FolioAl == null || FolioAl.equals(""))) {
                stringBuilder.append(" AND TPD_FOLIO = ? ");
            }

            if ((FolioDel == null || FolioDel.equals("")) && (FolioAl != null && !FolioAl.equals(""))) {
                return tPendientes;
            }

            if (Nombre != null && !Nombre.equals("")) {
                stringBuilder.append(" AND USU_NOM_USUARIO like ? ");
            }

            if (dImporte > 0) {
                stringBuilder.append(" AND TPD_IMPORTE = ? ");
            }
            
            stringBuilder.append(" ORDER BY TPD_FOLIO ASC");
            
            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            preparedStatement.setDate(2, new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));

            if ((FolioDel != null && !FolioDel.equals("")) && (FolioAl != null && !FolioAl.equals(""))) {
                index = index + 1;
                folioDel = Integer.parseInt(FolioDel.trim());
                preparedStatement.setInt(index, folioDel);

                index = index + 1;
                folioAl = Integer.parseInt(FolioAl.trim());
                preparedStatement.setInt(index, folioAl);
            }

            if ((FolioDel != null && !FolioDel.equals("")) && (FolioAl == null || FolioAl.equals(""))) {
                index = index + 1;
                folioDel = Integer.parseInt(FolioDel.trim());
                    preparedStatement.setInt(index, folioDel);
                }

                if (Nombre != null && !Nombre.equals("")) {
                index = index + 1;
                preparedStatement.setString(index, "%" + Nombre + "%");
            }

            if (dImporte > 0) {
                index = index + 1;
                preparedStatement.setDouble(index, dImporte);
            }

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                TPendientesBean tPendientesBean = new TPendientesBean();

                // Set_Values
                tPendientesBean.setTpd_Folio(resultSet.getInt("TPD_FOLIO"));
                tPendientesBean.setTpd_Tipo_Mov(resultSet.getString("TPD_TIPO_MOV"));
                tPendientesBean.setTpd_Transacc(resultSet.getString("TPD_TRANSACC"));
                tPendientesBean.setTpd_Folio_Opera(resultSet.getInt("TPD_FOLIO_OPERA"));
                tPendientesBean.setTpd_Fec_Mov(resultSet.getDate("TPD_FECHA_MOV"));
                tPendientesBean.setTpd_Nom_Usuario_Opera(resultSet.getString("USU_NOM_USUARIO"));
                tPendientesBean.setTpd_Usuario_Opera(resultSet.getInt("TPD_USUARIO_OP")); 
                tPendientesBean.setTpd_Importe(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(resultSet.getDouble("TPD_IMPORTE")));
                tPendientesBean.setTpd_Moneda(resultSet.getInt("TPD_MONEDA"));
                tPendientesBean.setTpd_Nom_Moneda(resultSet.getString("MON_NOM_MONEDA"));
                tPendientesBean.setTpd_Status(resultSet.getString("TPD_STATUS"));
                tPendientesBean.setTpd_Num_Fiso(resultSet.getInt("TPD_NUM_FISO"));
                //_Detalle
                tPendientesBean.setTpd_Obs_Checker(resultSet.getString("TPD_OBS_CHECKER"));
                tPendientesBean.setTpd_Obs_Maker(resultSet.getString("TPD_OBS_MAKER"));
                tPendientesBean.setTpd_Tipo_Mov(resultSet.getString("TPD_TIPO_MOV"));
                tPendientesBean.setTpd_Fec_Opera(resultSet.getDate("TPD_FECHA_MOV"));  //TPD_FECHA_OP
                tPendientesBean.setTpd_Usuario_Au(resultSet.getInt("TPD_USUARIO_AU"));
                tPendientesBean.setTpd_Fec_Au(resultSet.getDate("TPD_FECHA_AU"));
                tPendientesBean.setTpd_Sub_Fiso(resultSet.getInt("TPD_SUB_FISO"));
                tPendientesBean.setTpd_Num_Instr(resultSet.getInt("TPD_NUM_INSTR"));

                // Add_List
                tPendientes.add(tPendientesBean);
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_ConsultaChecker()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return tPendientes;
    }

    public List<Double> onContabilidad_GetImportes() {
 
        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        // -----------------------------------------------------------------------------
        List<Double> Importes = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT DISTINCT(TPD_IMPORTE) ");
            stringBuilder.append("FROM SAF.TPENDIENTES t ");
            stringBuilder.append("Inner Join SAF.VISTA_USUARIO w On CTO_NUM_CONTRATO = TPD_NUM_FISO "); 
            stringBuilder.append("AND w.USU_NUM_USUARIO = ? ");
            stringBuilder.append("INNER JOIN SAF.MONEDAS On MON_NUM_PAIS = t.TPD_MONEDA "); 
            stringBuilder.append("INNER JOIN SAF.USUARIOS U On TPD_USUARIO_OP = U.USU_NUM_USUARIO ");
            stringBuilder.append("INNER JOIN SAF.PUESTOS P On P.PUE_NUM_PUESTO = U.USU_NUM_PUESTO "); 
            stringBuilder.append("WHERE  t.TPD_TIPO=1 AND t.TPD_STATUS = 'ACTIVO' ");
            stringBuilder.append("AND (t.TPD_NUM_FISO=0 OR t.TPD_NUM_FISO  >= 0 ) ");
            stringBuilder.append("AND t.TPD_IMPORTE > 0  ");
            stringBuilder.append("AND t.TPD_FECHA_MOV = ? ");
            stringBuilder.append("ORDER BY t.TPD_IMPORTE ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());
            
            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            preparedStatement.setDate(2, new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {

                    // Add_List
                    Importes.add(resultSet.getDouble("TPD_IMPORTE"));
                }
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_GetImportes()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }
        return Importes;
    }

    public List<TPendientesDetBean> onContabilidad_DecodificaChecker(int Folio) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_DecodificaChecker()");

        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<TPendientesDetBean> tPendientesDet = new ArrayList<>();

        try {

            //================================================================================
            // MUESTRA LAS INSTRUCCIONES PENDIENTES DE AUTORIZACION A FECHA CONTABLE
            //================================================================================
            stringBuilder.append("Select TPDD_DATO, TPDD_VALOR,	1 PUE_NIV_JERAR ");
            stringBuilder.append(" FROM SAF.TPENDIENTES T,USUARIOS U,PUESTOS P,TPENDIENTESDET D ");
            stringBuilder.append(" WHERE TPD_USUARIO_OP = USU_NUM_USUARIO AND ");
            stringBuilder.append(" U.USU_NUM_PUESTO = P.PUE_NUM_PUESTO AND ");
            stringBuilder.append(" T.TPD_FOLIO = D.TPD_FOLIO AND T.TPD_FOLIO = ? ");
            stringBuilder.append(" ORDER BY TPDD_SECUENCIA");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                TPendientesDetBean tPendientesDetBean = new TPendientesDetBean();

                // Set_Values
                tPendientesDetBean.setTpdd_Dato(resultSet.getString("TPDD_DATO"));
                tPendientesDetBean.setTpdd_Valor(resultSet.getString("TPDD_VALOR"));

                // Add_List
                tPendientesDet.add(tPendientesDetBean);
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_DecodificaChecker()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return tPendientesDet;
    }

    public List<String> onContabilidad_FaltanChecker(int Folio) {

        //logger.info("Ingresa a Método onContabilidad_FaltanChecker()");

        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<String> faltanCheker = new ArrayList<>();

        try {

            //================================================================================
            // MUESTRA LAS INSTRUCCIONES PENDIENTES DE AUTORIZACION A FECHA CONTABLE
            //================================================================================
            stringBuilder.append("SELECT Coalesce(sum(au.TPD_PESO_AUT),0) Acum, count(*) Num, ");
            stringBuilder.append("Coalesce((SELECT MAT_PESO_AUT ");
            stringBuilder.append(" FROM SAF.TPENDIENTES INNER JOIN SAF.MATRIZAUT ON MAT_TRANSACCION = TPD_TIPO_MOV AND MONE_ID_MONEDA = TPD_MONEDA ");
            stringBuilder.append(" AND TPD_IMPORTE BETWEEN MAT_RANGO_INI AND MAT_RANGO_SUP AND MAT_STATUS='ACTIVO' ");
            stringBuilder.append(" WHERE TPD_FOLIO = ? AND TPD_STATUS='ACTIVO'),0) Faltan ");
            stringBuilder.append(" FROM TPENDIENTESAUT au WHERE  au.TPD_FOLIO = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Folio);
            preparedStatement.setInt(2, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                // Set_Values
                faltanCheker.add(resultSet.getString("Acum"));
                faltanCheker.add(resultSet.getString("Num"));
                faltanCheker.add(resultSet.getString("Faltan"));
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_FaltanChecker()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());

            }
        }

        return faltanCheker;
    }

    public OutParameterBean onContabilidad_EjecutaChecker(String sTipoTrans, String sForma, int iFolio, int iUsuario) {

        //logger.info("Ingresa a Método onContabilidad_EjecutaChecker()");

        // Objects 
        Connection conexion = null;
        CallableStatement callableStatement = null;
        OutParameterBean outParameterBean = new OutParameterBean();

        try {

          
            // SQL
            String sql = "{CALL DB2FIDUC.SPN_EJECUTA_CHECK (?,?,?,?,?,?,?,?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setString(1, sTipoTrans);
            callableStatement.setString(2, sForma);
            callableStatement.setInt(3, iFolio);
            callableStatement.setString(4, (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal")); 
//            callableStatement.setString(4, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            callableStatement.setInt(5, iUsuario);
            callableStatement.setInt(6, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("bEjecuto", Types.INTEGER);
            callableStatement.registerOutParameter("iNumTransfer", Types.INTEGER);
            callableStatement.registerOutParameter("dImpTransfer", Types.DOUBLE);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_cstmt
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iEjecuto = callableStatement.getInt("bEjecuto");
            String sMsgErrOut = callableStatement.getString("PS_MSGERR_OUT");
            //String sState = callableStatement.getString("PCH_SQLSTATE_OUT");CAVC
            //int sCode = callableStatement.getInt("PI_SQLCODE_OUT");CAVC

            if (iEjecuto == 1) {
                outParameterBean.setPsMsgErrOut("");
                outParameterBean.setbEjecuto(iEjecuto);
                outParameterBean.setiNumFolioContab(callableStatement.getInt("iNumTransfer"));
                outParameterBean.setdImporteCobrado(callableStatement.getDouble("dImpTransfer"));
                
            } else {
                outParameterBean.setbEjecuto(0);
                outParameterBean.setPsMsgErrOut(sMsgErrOut);
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_EjecutaChecker()");
            logger.error(mensajeError);
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+e.getMessage());
            }
        }

        return outParameterBean;
    }

    public int onContabilidad_ValidaAutorización(int Folio) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_ValidaAutorización()");

        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        int iPuntos = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //================================================================================
            // MUESTRA LAS INSTRUCCIONES PENDIENTES DE AUTORIZACION A FECHA CONTABLE
            //================================================================================

            stringBuilder.append("SELECT SUM(TPD_PESO_AUT) Puntos FROM SAF.TPENDIENTESAUT WHERE TPD_FOLIO = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {
                iPuntos = resultSet.getInt("Puntos");
                return iPuntos;
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_ValidaAutorización()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return 0;
    }

    public List<DetMasivoBean> onContabilidad_DetalleTerceroMas(int iFiso, int Folio) {
   
        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<DetMasivoBean> detMasivo = new ArrayList<>();

        try {
            stringBuilder.append("SELECT TER_NOM_TERCERO BENEFICIARIO,	PTX_IMP_FIJO_PAGO IMPORTE,		MON_NOM_MONEDA MONEDA,			c27.CVE_DESC_CLAVE BANCO,");
            stringBuilder.append("		ML_NUM_CTA CUENTA,				c128.CVE_DESC_CLAVE CONCEPTO,	c81.CVE_DESC_CLAVE FORMA_DE_PAGO ");
            stringBuilder.append("FROM SAF.PARPATEX ");
            stringBuilder.append("INNER JOIN SAF.TERCEROS ");
            stringBuilder.append("ON TER_NUM_CONTRATO = PTX_NUM_CONTRATO ");
            stringBuilder.append("AND TER_NUM_TERCERO = PTX_NUM_TERCERO ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c128 ");
            stringBuilder.append("ON C128.CVE_NUM_CLAVE = 128 ");
            stringBuilder.append("AND C128.CVE_NUM_SEC_CLAVE = PTX_SUB_PROGRAMA ");
            stringBuilder.append("INNER JOIN SAF.FDMASIVOLIQ ");
            stringBuilder.append("ON ML_NUM_FOLIO_INST= PTX_NUM_FOLIO_INST ");
            stringBuilder.append("AND ML_NUM_CONTRATO = PTX_NUM_CONTRATO ");
            stringBuilder.append("AND ML_SUB_CONTRATO = PTX_SUB_CONTRATO ");
            stringBuilder.append("AND ML_CVE_PERSONA = 'TERCERO' ");
            stringBuilder.append("AND ML_NUM_PERSONA = PTX_NUM_TERCERO ");
            stringBuilder.append("AND ML_SUB_PROGRAMA = PTX_SUB_PROGRAMA ");
            stringBuilder.append("AND ML_SEC_PAGO = PTX_SEC_PAGO ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c81 ");
            stringBuilder.append("ON C81.CVE_NUM_CLAVE=81 ");
            stringBuilder.append("AND C81.CVE_NUM_SEC_CLAVE= ML_FORMA_LIQ ");
            stringBuilder.append("INNER JOIN SAF.MONEDAS ");
            stringBuilder.append("ON MON_NUM_PAIS=ML_NUM_MONEDA ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c27 ");
            stringBuilder.append("ON C27.CVE_NUM_CLAVE=27 ");
            stringBuilder.append("AND C27.CVE_NUM_SEC_CLAVE= ML_NUM_BANCO ");
            stringBuilder.append("WHERE PTX_NUM_CONTRATO = ? ");
            stringBuilder.append("AND PTX_NUM_FOLIO_INST = ? ");
            stringBuilder.append("AND PTX_CVE_ST_PARPATE ='POR AUTORIZAR'");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, iFiso);
            preparedStatement.setInt(2, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                DetMasivoBean detalleBean = new DetMasivoBean();

                // Set_Values
                detalleBean.setNom_Tercero(resultSet.getString("BENEFICIARIO"));
                detalleBean.setImp_Fijo_Pago(resultSet.getDouble("IMPORTE"));
                detalleBean.setNom_Moneda(resultSet.getString("MONEDA"));
                detalleBean.setBanco(resultSet.getString("BANCO"));
                detalleBean.setNum_Cta(resultSet.getDouble("CUENTA"));
                detalleBean.setConcepto(resultSet.getString("CONCEPTO"));
                detalleBean.setForma_Pago(resultSet.getString("FORMA_DE_PAGO"));

                // Add_List
                detMasivo.add(detalleBean);
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_DetalleTerceroMas()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
               logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return detMasivo;
    }

    public List<Double> onContabilidad_Totales_DetTerceroMas(int iFiso, int Folio) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_Totales_DetTerceroMas()");

        // Objects
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<Double> Totales_detTercero = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("SELECT SUM(PTX_IMP_FIJO_PAGO) SUMIMPORTE, COUNT(*) NUM_REGS ");
            stringBuilder.append("FROM SAF.PARPATEX  ");
            stringBuilder.append("INNER JOIN SAF.FDMASIVOLIQ ");
            stringBuilder.append("ON ML_NUM_FOLIO_INST= PTX_NUM_FOLIO_INST ");
            stringBuilder.append("AND ML_NUM_CONTRATO = PTX_NUM_CONTRATO ");
            stringBuilder.append("AND ML_SUB_CONTRATO=PTX_SUB_CONTRATO ");
            stringBuilder.append("AND ML_CVE_PERSONA = 'TERCERO' ");
            stringBuilder.append("AND ML_NUM_PERSONA = PTX_NUM_TERCERO ");
            stringBuilder.append("AND ML_SUB_PROGRAMA=PTX_SUB_PROGRAMA ");
            stringBuilder.append("AND ML_SEC_PAGO = PTX_SEC_PAGO ");
            stringBuilder.append("WHERE PTX_NUM_CONTRATO = ? ");
            stringBuilder.append("AND PTX_NUM_FOLIO_INST = ? ");
            stringBuilder.append("AND PTX_CVE_ST_PARPATE ='POR AUTORIZAR'");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, iFiso);
            preparedStatement.setInt(2, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detTercero.add(resultSet.getDouble("SUMIMPORTE"));
                    Totales_detTercero.add(resultSet.getDouble("NUM_REGS"));
                }
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_Totales_DetTerceroMas()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return Totales_detTercero;
    }

    public List<DetMasivoBean> onContabilidad_DetalleBenefici(int iFiso, int Folio) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_DetalleBenefici()");

        // Objects 
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<DetMasivoBean> detMasivo = new ArrayList<>();

        try {
            stringBuilder.append("select BEN_NOM_BENEF BENEFICIARIO,PBX_IMP_FIJO_PAGO IMPORTE,MON_NOM_MONEDA MONEDA,c27.CVE_DESC_CLAVE BANCO, ");
            stringBuilder.append("ML_NUM_CTA CUENTA,c128.CVE_DESC_CLAVE CONCEPTO,c81.CVE_DESC_CLAVE FORMA_DE_PAGO ");
            stringBuilder.append("From SAF.PARPABEX ");
            stringBuilder.append("INNER JOIN SAF.BENEFICI ");
            stringBuilder.append("ON BEN_NUM_CONTRATO = PBX_NUM_CONTRATO ");
            stringBuilder.append("AND BEN_BENEFICIARIO = PBX_BENEFICIARIO ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c128 ");
            stringBuilder.append("ON C128.CVE_NUM_CLAVE=128 ");
            stringBuilder.append("AND C128.CVE_NUM_SEC_CLAVE= PBX_SUB_PROGRAMA ");
            stringBuilder.append("INNER JOIN SAF.FDMASIVOLIQ ");
            stringBuilder.append("ON ML_NUM_FOLIO_INST= PBX_NUM_FOLIO_INST ");
            stringBuilder.append("AND ML_NUM_CONTRATO = PBX_NUM_CONTRATO ");
            stringBuilder.append("AND ML_SUB_CONTRATO=PBX_SUB_CONTRATO ");
            stringBuilder.append("AND ML_CVE_PERSONA = 'FIDEICOMISARIO' ");
            stringBuilder.append("AND ML_NUM_PERSONA = PBX_BENEFICIARIO ");
            stringBuilder.append("AND ML_SUB_PROGRAMA=PBX_SUB_PROGRAMA ");
            stringBuilder.append("AND ML_SEC_PAGO = PBX_SEC_PAGO ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c81 ");
            stringBuilder.append("ON C81.CVE_NUM_CLAVE=81 ");
            stringBuilder.append("AND C81.CVE_NUM_SEC_CLAVE= ML_FORMA_LIQ ");
            stringBuilder.append("INNER JOIN SAF.MONEDAS ");
            stringBuilder.append("On MON_NUM_PAIS = ML_NUM_MONEDA ");
            stringBuilder.append("INNER JOIN SAF.CLAVES c27 oN C27.CVE_NUM_CLAVE=27 ");
            stringBuilder.append("AND C27.CVE_NUM_SEC_CLAVE= ML_NUM_BANCO ");
            stringBuilder.append("Where  PBX_NUM_CONTRATO = ? ");
            stringBuilder.append("AND  PBX_NUM_FOLIO_INST =  ? ");
            stringBuilder.append("AND  PBX_CVE_ST_PARPABE ='POR AUTORIZAR'");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, iFiso);
            preparedStatement.setInt(2, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            while (resultSet.next()) {

                DetMasivoBean detalleBean = new DetMasivoBean();

                // Set_Values
                detalleBean.setNom_Tercero(resultSet.getString("BENEFICIARIO"));
                detalleBean.setImp_Fijo_Pago(resultSet.getDouble("IMPORTE"));
                detalleBean.setNom_Moneda(resultSet.getString("MONEDA"));
                detalleBean.setBanco(resultSet.getString("BANCO"));
                detalleBean.setNum_Cta(resultSet.getDouble("CUENTA"));
                detalleBean.setConcepto(resultSet.getString("CONCEPTO"));
                detalleBean.setForma_Pago(resultSet.getString("FORMA_DE_PAGO"));

                // Add_List
                detMasivo.add(detalleBean);
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_DetalleBenefici()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+ e.getMessage());
            }
        }

        return detMasivo;
    }

    public List<Double> onContabilidad_Totales_DetBeneficiMas(int iFiso, int Folio) {

        // Write_Console
        //logger.info("Ingresa a Método onContabilidad_Totales_DetBeneficiMas()");

        // Objects
        // Objects 
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilder = new StringBuilder();
        List<Double> Totales_detTercero = new ArrayList<>();

        try {
            // SQL
            stringBuilder.append("select sum(PBX_IMP_FIJO_PAGO) SUMIMPORTE,COUNT(*) NUM_REGS ");
            stringBuilder.append("From SAF.PARPABEX ");
            stringBuilder.append("INNER JOIN SAF.FDMASIVOLIQ ");
            stringBuilder.append("On ML_NUM_FOLIO_INST= PBX_NUM_FOLIO_INST ");
            stringBuilder.append("AND ML_NUM_CONTRATO = PBX_NUM_CONTRATO ");
            stringBuilder.append("AND ML_SUB_CONTRATO=PBX_SUB_CONTRATO ");
            stringBuilder.append("AND ML_CVE_PERSONA = 'FIDEICOMISARIO' ");
            stringBuilder.append("AND ML_NUM_PERSONA = PBX_BENEFICIARIO ");
            stringBuilder.append("AND ML_SUB_PROGRAMA=PBX_SUB_PROGRAMA ");
            stringBuilder.append("AND ML_SEC_PAGO = PBX_SEC_PAGO ");
            stringBuilder.append("Where  PBX_NUM_CONTRATO = ? ");
            stringBuilder.append("AND  PBX_NUM_FOLIO_INST =  ? ");
            stringBuilder.append("AND  PBX_CVE_ST_PARPABE ='POR AUTORIZAR'");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, iFiso);
            preparedStatement.setInt(2, Folio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {

                while (resultSet.next()) {
                    // Add_List
                    Totales_detTercero.add(resultSet.getDouble("SUMIMPORTE"));
                    Totales_detTercero.add(resultSet.getDouble("NUM_REGS"));
                }
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_Totales_DetBeneficiMas()");
            logger.error(mensajeError);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+e.getMessage());
            }
        }

        return Totales_detTercero;
    }

    public OutParameterBean onContabilidad_CancelaChecker(int iFolio) {

        //logger.info("Ingresa a Método onContabilidad_CancelaChecker()");

      
        // Objects
        Connection conexion = null;
        CallableStatement callableStatement = null;

        OutParameterBean outParameterBean = new OutParameterBean();

        try {

         
            // SQL
            String sql = "{CALL DB2FIDUC.SPN_CANCEL_MK(?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setInt(1, iFolio);

            // REGISTER_OUT_PARAMETER
            callableStatement.registerOutParameter("bEjecuto", Types.INTEGER);
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_cstmt
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int iEjecuto = callableStatement.getInt("bEjecuto");
            String sMsgErrOut = callableStatement.getString("PS_MSGERR_OUT").replaceAll("ERROR", ""); 
            //logger.debug("iEjecuto " + iEjecuto);
            //logger.debug("sMsgErrOut " + sMsgErrOut);

            if (iEjecuto == 1) {
                outParameterBean.setPsMsgErrOut(sMsgErrOut);
                outParameterBean.setiNumFolioContab(iEjecuto);
            } else {
                outParameterBean.setiNumFolioContab(0);
                outParameterBean.setPsMsgErrOut(sMsgErrOut);
                
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_CancelaChecker()");
            logger.error(mensajeError);
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
               logger.error("Function ::onCierraConexion:: Error al cerrar Connection."+e.getMessage());
            }
        }

        return outParameterBean;
    }

    
    public boolean onCFDI_ModificaStatusChecker(int iFolio, String sStatus) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
 
        try {
            // SQL
            stringBuilder.append(" UPDATE SAF.TPENDIENTES SET TPD_STATUS = ? WHERE TPD_FOLIO = ?"); 
            
            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setString(1, sStatus); 
            pstmt.setInt(2, iFolio); 

            // Execute_Update
            iReg = pstmt.executeUpdate();
            onCierraConexion();

            if (iReg > 0) { 
                return true;
            }

        }  catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_ModificarDirecci()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }
        return false;
    }
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	    * C A N C E L A C I O N    D E    O P E R A C I O N E S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    public List<ContabilidadMovtoBean> onContabilidad_CancelaOper_MesesAnt(CriterioBusquedaContaBean cbc) {
 
        List<ContabilidadMovtoBean> consulta = new ArrayList<>();

        try { 
            int iResultado = 0;

            String sqlComando = "{CALL DB2FIDUC.SPN_VALIDA_PROVHON (?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("iFolioConta", cbc.getCriterioFolioIni());

            cstmt.registerOutParameter("iResultado", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("iMeses", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("dImporte", java.sql.Types.DECIMAL);
            cstmt.registerOutParameter("sComplemento", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", ""); 
            iResultado = cstmt.getInt("iResultado");
            onCierraConexion(); 
            
            //logger.debug("mensajeErrorSP " + mensajeErrorSP);
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                mensajeError = null;
                //Envia a Consultar 
                if (iResultado == 1) {
                    consulta = onContabilidad_selectPagosMesCerrado(cbc.getCriterioFolioIni());
                }
            } else {
                mensajeError = mensajeErrorSP;
            }
            
            
        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_CancelaOper_MesesAnt()");
            logger.error(mensajeError);
        }  finally {
            onCierraConexion();
        }

        return consulta;
    }
    
    //VJN Inicio - 29/01/2024 - Valida mes abierto o cerrado
    public int onContabilidad_ValidaMesAbiertoCerrado(CriterioBusquedaContaBean cbc) {

        // Objects        
        int iResultado = 0; 
        StringBuilder stringBuilder = new StringBuilder();

        // SQL
        stringBuilder.append("SELECT CCC_MES_ABIERTO from SAF.CTL_MESC WHERE CCC_MES = ? AND CCC_ANO = ? ");
        
        try { 
        	conexion = DataBaseConexion.getInstance().getConnection();
        	pstmt = conexion.prepareStatement(stringBuilder.toString()); 
 
            // REGISTER_IN_PARAMETER
        	pstmt.setInt(1, cbc.getCriterioFechaMM()); 
        	pstmt.setInt(2, cbc.getCriterioFechaYYYY()); 

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) { 
                while (rs.next()) {
                    //GET_OUT_PARAMETER
                    iResultado = rs.getInt("CCC_MES_ABIERTO");
                }
            }
            onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += Err.getMessage();
        } finally {
            onCierraConexion();
        }

        return iResultado;
    }
    //VJN fin
    
        public List<ContabilidadMovtoBean> onContabilidad_selectPagosMesCerrado(Long folio) {

        List<ContabilidadMovtoBean> consulta = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append("SELECT movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso,");
            stringBuilder.append("  detm_folio_op, tran_id_tran, movi_descripcion, movi_importe,");
            stringBuilder.append("  movi_usuario, movi_status ");
            stringBuilder.append(" FROM SAF.FDMOVIMIENTO");
            stringBuilder.append(" WHERE DETM_FOLIO_OP = ? ");
            stringBuilder.append("AND FISO_ID_FISO  >= 0 ");
            stringBuilder.append("AND movi_status <> 'CANCELADO' ");

            conexion = DataBaseConexion.getInstance().getConnection();

            pstmt = conexion.prepareStatement(stringBuilder.toString());
            pstmt.setLong(1, folio);

            rs = pstmt.executeQuery();
            while (rs.next()) {

                ContabilidadMovtoBean movto = new ContabilidadMovtoBean();
                movto.setMovtoFecha(rs.getDate("movi_fecha_mov"));
                movto.setMovtoContratoNum(rs.getLong("fiso_id_fiso"));
                movto.setMovtoContratoNumSub(rs.getInt("sfis_id_sfiso"));
                movto.setMovtoFechaReg(rs.getDate("movi_alta"));
                movto.setMovtoFolio(rs.getLong("detm_folio_op"));
                movto.setMovtoTransaccId(rs.getString("tran_id_tran"));
                movto.setMovtoDesc(rs.getString("movi_descripcion"));
                movto.setMovtoImporte(rs.getDouble("movi_importe"));
                movto.setMovtoUsuarioId(rs.getLong("movi_usuario"));
                movto.setMovtoStatus(rs.getString("movi_status"));
                consulta.add(movto);
            }
            onCierraConexion();
            
        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onContabilidad_selectPagosMesCerrado()");
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return consulta;
    }

    public Boolean onContabilidad_EjecutaCancela(ContabilidadPolizaManBean pm) {

        try {
            
            String sqlComando = "{CALL db2Fiduc.SPN_EJECUT_CANCELA (?,?,?,?,?,?,?,?,?,?)}";

            conexion = DataBaseConexion.getInstance().getConnection(); 
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setLong("iFolioConta", pm.getPolizaEncaFolio());
            cstmt.setString("sNumOpera", pm.getPolizaEncaNumero());
            cstmt.setDate("dtFechaMov", pm.getPolizaEncaFechaMovto());
            cstmt.setLong("iChkMesCerrado", Long.parseLong(pm.getPoliza00Valor()));
            cstmt.setInt("iNumUsuario", pm.getPolizaEncaBitUsuario());

            cstmt.registerOutParameter("iResultado", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("iDesbqChk", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            pm.setPolizaEncaFolioSalida(cstmt.getLong("iResultado"));            
            onCierraConexion();
            
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE; 
            } else {
                mensajeError = mensajeErrorSP;
                valorRetorno = Boolean.FALSE;
            }

        } catch (SQLException sqlException) {
            logger.error(sqlException.getMessage());
        } finally {
        	onCierraConexion();
        }
        return valorRetorno;
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

    public synchronized String formatDecimal0D(Integer importe) {
        return formatoDecimal0D.format(importe);
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onCierraConexion() {   
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar ResultSet." + e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar PreparetStatement." + e.getMessage());
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar CallableStatement." + e.getMessage());
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar Connection." + e.getMessage());
            }
        }
    }
      
    public Double getBienFideUnidadAcumRegContable() {
        return bienFideUnidadAcumRegContable;
    }

    public void setBienFideUnidadAcumRegContable(Double bienFideUnidadAcumRegContable) {
        this.bienFideUnidadAcumRegContable = bienFideUnidadAcumRegContable;
    }

    public String getMensajePoliza() {
        return mensajePoliza;
    }

    public void setMensajePoliza(String mensajePoliza) {
        this.mensajePoliza = mensajePoliza;
    }

    public Boolean getIndSimulados() {
        return indSimulados;
    }

    public void setIndSimulados(Boolean indSimulados) {
        this.indSimulados = indSimulados;
    }

    public Boolean getConEstructura() {
        return conEstructura;
    }

    public void setConEstructura(Boolean conEstructura) {
        this.conEstructura = conEstructura;
    }
}