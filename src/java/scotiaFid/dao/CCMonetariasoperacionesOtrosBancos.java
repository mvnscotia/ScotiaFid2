/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import scotiaFid.bean.MonetariasOperacionOtrosBancosBean;
import scotiaFid.bean.ParametrosLiquidacionBean;
import scotiaFid.bean.InformacionCuentasParametrosLiquidacionBean;
import scotiaFid.bean.SPN_PROC_LIQ_IND_Bean;
import scotiaFid.singleton.DataBaseConexion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.util.LogsContext;

/**
 *
 * @author lenovo
 */
public class CCMonetariasoperacionesOtrosBancos {

    private static final Logger logger = LogManager.getLogger(CCMonetariasoperacionesOtrosBancos.class);

    private String sqlComando;
    private String mensajeErrorSP;
    private String nombreObjeto;

    //private CallableStatement cstmt;
    //private PreparedStatement pstmt;
    //private ResultSet rs;
    public CCMonetariasoperacionesOtrosBancos() {
        nombreObjeto = "\nFuente: scotiaFid.dao.CCLiquidacionesOperadasOtrosBancos.";
        LogsContext.FormatoNormativo();
    }

    public Map<String, String> onGuardaSPDeposOtbancos(MonetariasOperacionOtrosBancosBean monetariaOperacion) {
            Connection conexion = null;
        CallableStatement cstmt = null;
        Map<String, String> responseSPProcesa = new HashMap<>();
        try {

            sqlComando = "{call DB2FIDUC.SPN_DEPOS_OTBANCOS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("LNUMCONTRATO", monetariaOperacion.getLNUMCONTRATO());
            cstmt.setInt("ISUBCONTRATO", monetariaOperacion.getISUBCONTRATO());
            cstmt.setDouble("VDIMPORTEMOV", monetariaOperacion.getVDIMPORTEMOV());
            cstmt.setInt("INUMMONEDA", monetariaOperacion.getINUMMONEDA());
            cstmt.setDate("DTFECHAMOV", monetariaOperacion.getDTFECHAMOV());
            cstmt.setString("SCTAORIGEN", monetariaOperacion.getSCTAORIGEN());
            cstmt.setInt("VICPTODEPO", monetariaOperacion.getVICPTODEPO());
            cstmt.setInt("VIFORMADEP", monetariaOperacion.getVIFORMADEP());
            cstmt.setString("SCVE_PERS_FID", monetariaOperacion.getSCVE_PERS_FID());
            cstmt.setInt("INUM_PERS_FID", monetariaOperacion.getINUM_PERS_FID());
            cstmt.setInt("INUMUSUARIO", monetariaOperacion.getINUMUSUARIO());
            cstmt.setString("STERMINAL", monetariaOperacion.getSTERMINAL());

            cstmt.registerOutParameter("BEJECUTO", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("IFOLIOCONTA", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT");
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                responseSPProcesa.put("folioContable", String.valueOf(cstmt.getInt("IFOLIOCONTA")));
            } else {
                responseSPProcesa.put("Error", mensajeErrorSP);
            }

            cstmt.close();
            conexion.close();

        } catch (SQLException Err) {
            logger.error("onGuardaSPDeposOtbancos()");
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::onGuardaSPDeposOtbancos:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");
                    logger.error("onGuardaSPDeposOtbancos()");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Error function: onGuardaSPDeposOtbancos");
                }
            }
        }
        return responseSPProcesa;
    }

    public Map<String, String> onGuarda_SPN_PROC_LIQ_IND(SPN_PROC_LIQ_IND_Bean spnBean) {
        Connection conexion = null;
        CallableStatement cstmt = null;
        Map<String, String> responseSPProcesa = new HashMap<>();
        try {

            sqlComando = "{call DB2FIDUC.SPN_PROC_LIQ_IND(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("IUSUARIO", spnBean.getIUSUARIO());
            cstmt.setInt("INUMCONTRATO", spnBean.getINUMCONTRATO());
            cstmt.setInt("ISUBCONTRATO", spnBean.getISUBCONTRATO());
            cstmt.setString("SIDTRANSAC", spnBean.getSIDTRANSAC());
            cstmt.setString("VSCVE_PERS_FID", spnBean.getVSCVE_PERS_FID());
            cstmt.setInt("VINUM_PERS_FID", spnBean.getVINUM_PERS_FID());
            cstmt.setString("VSNOMPERSONA", spnBean.getVSNOMPERSONA());
            cstmt.setDouble("VDIMPORTEMOV", spnBean.getVDIMPORTEMOV());
            cstmt.setInt("VINUM_MONEDA", spnBean.getVINUM_MONEDA());
            cstmt.setDouble("VDTIPOCAMBIO", spnBean.getVDTIPOCAMBIO());
            cstmt.setInt("VICVE_TIPO_LIQ", spnBean.getVICVE_TIPO_LIQ());
            cstmt.setString("VSTIPOLIQUID", spnBean.getVSTIPOLIQUID());
            cstmt.setInt("IFOLIOINSTR", spnBean.getIFOLIOINSTR());
            cstmt.setInt("VINUMPAGOS", spnBean.getVINUMPAGOS());
            cstmt.setInt("VIPAGOSEFECT", spnBean.getVIPAGOSEFECT());
            cstmt.setString("SCTAORIGEN", spnBean.getSCTAORIGEN());
            cstmt.setInt("VICPTOPAGO", spnBean.getVICPTOPAGO());
            cstmt.setString("VSCPTOPAGO", spnBean.getVSCPTOPAGO());
            cstmt.setString("VSCVE_TIPO_CTA", spnBean.getVSCVE_TIPO_CTA());
            cstmt.setInt("VINUM_BANCO", spnBean.getVINUM_BANCO());
            cstmt.setInt("VINUM_PLAZA", spnBean.getVINUM_PLAZA());
            cstmt.setInt("VINUM_SUCURSAL", spnBean.getVINUM_SUCURSAL());
            cstmt.setDouble("VINUM_CUENTA", spnBean.getVINUM_CUENTA());
            cstmt.setInt("VINUM_PAIS", spnBean.getVINUM_PAIS());
            cstmt.setInt("VICTA_BANXICO", spnBean.getVICTA_BANXICO());
            cstmt.setString("VSDIR_APER_CTA", spnBean.getVSDIR_APER_CTA());
            cstmt.setInt("VINUM_INICIATIVA", spnBean.getVINUM_INICIATIVA());
            cstmt.setInt("VINUM_CTAM", spnBean.getVINUM_CTAM());
            cstmt.setInt("VINUM_SCTA", spnBean.getVINUM_SCTA());
            cstmt.setInt("VINUM_SSCTA", spnBean.getVINUM_SSCTA());
            cstmt.setInt("VINUM_SSSCTA", spnBean.getVINUM_SSSCTA());
            cstmt.setString("VSNOM_AREA", spnBean.getVSNOM_AREA());
            cstmt.setString("VSCONCEPTO", spnBean.getVSCONCEPTO());
            cstmt.setInt("VICUENTAS_ORDEN", spnBean.getVICUENTAS_ORDEN());
            cstmt.setInt("VISECUENINSTR", spnBean.getVISECUENINSTR());
            cstmt.setDate("DTFECHAMOV", spnBean.getDTFECHAMOV());
            cstmt.setString("STERMINAL", spnBean.getSTERMINAL());
            cstmt.setString("SIDFORMA", spnBean.getSIDFORMA());

            cstmt.registerOutParameter("BEJECUTO", java.sql.Types.SMALLINT);
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
            logger.error("onGuarda_SPN_PROC_LIQ_IND()");
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::onGuardaSPDeposOtbancos:: Error al cerrar PreparetStatement."+e.getMessage(), "10", "20", "30");
                    logger.error("onGuarda_SPN_PROC_LIQ_IND()-cstmt.close()");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("onGuarda_SPN_PROC_LIQ_IND()-conexicon.close()");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::onGuardaSPDeposOtbancos:: Error al cerrar Connection."+ e.getMessage(), "10", "20", "30");
                }
            }
        }
        return responseSPProcesa;
    }

    public ParametrosLiquidacionBean obtenParametrosLiquidacion(String fideicomiso, String tipoPersona, int numeroPersona) {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParametrosLiquidacionBean parametroLiquidacion = null;
        try {
                sqlComando = "Select rtrim(ltrim(replace(COALESCE(BEN_NOM_BENEF,TER_NOM_TERCERO),chr(9),' '))) NOMBRE, \n"
                    + "COALESCE(PAL_CVE_TIPO_LIQ,0) PAL_CVE_TIPO_LIQ,COALESCE(PAL_NUM_MONEDA,0) PAL_NUM_MONEDA,COALESCE(PAL_CVE_TIPO_CTA,'') PAL_CVE_TIPO_CTA, \n"
                    + "COALESCE(PAL_NUM_BANCO,0) PAL_NUM_BANCO,COALESCE(PAL_NUM_PLAZA,0) PAL_NUM_PLAZA,COALESCE(PAL_NUM_SUCURSAL,0) PAL_NUM_SUCURSAL, \n"
                    + "COALESCE(PAL_NOM_SUCURSAL,'') PAL_NOM_SUCURSAL,COALESCE(PAL_NUM_CUENTA,0) PAL_NUM_CUENTA,COALESCE(PAL_NUM_PAIS,0) PAL_NUM_PAIS, \n"
                    + "COALESCE(PAL_CTA_BANXICO,0) PAL_CTA_BANXICO,COALESCE(PAL_DIR_APER_CTA,'') PAL_DIR_APER_CTA,COALESCE(PAL_CODIGO_TRANS,'') PAL_CODIGO_TRANS, \n"
                    + "COALESCE(PAL_NUM_INICIATIVA,0) PAL_NUM_INICIATIVA,COALESCE(PAL_NUM_CTAM,0) PAL_NUM_CTAM,COALESCE(PAL_NUM_SCTA,0) PAL_NUM_SCTA, \n"
                    + "COALESCE(PAL_NUM_SSCTA,0) PAL_NUM_SSCTA,COALESCE(PAL_NUM_SSSCTA,0) PAL_NUM_SSSCTA,COALESCE(PAL_NOM_AREA,'') PAL_NOM_AREA, \n"
                    + "COALESCE(PAL_CONCEPTO,'') PAL_CONCEPTO,COALESCE(PAL_CUENTAS_ORDEN,0) PAL_CUENTAS_ORDEN,COALESCE(PAL_ANO_ALTA_REG,0) PAL_ANO_ALTA_REG, \n"
                    + "COALESCE(PAL_MES_ALTA_REG,0) PAL_MES_ALTA_REG,COALESCE(PAL_DIA_ALTA_REG,0) PAL_DIA_ALTA_REG,COALESCE(PAL_ANO_ULT_MOD,0) PAL_ANO_ULT_MOD, \n"
                    + "COALESCE(PAL_MES_ULT_MOD,0) PAL_MES_ULT_MOD,COALESCE(PAL_DIA_ULT_MOD,0) PAL_DIA_ULT_MOD,PAL_CVE_ST_PALIQUI  PAL_CVE_ST_PALIQUI,CVE_DESC_CLAVE,MON_NOM_MONEDA,DESC_BANCO \n"
                    + ",SAF.FUNN_GETCLABE(PAL_NUM_BANCO,PAL_NUM_PLAZA,PAL_NUM_CUENTA) CLABE \n"
                    + "FROM PALIQUID INNER JOIN CLAVES On CVE_NUM_CLAVE=81 and PAL_CVE_TIPO_LIQ=CVE_NUM_SEC_CLAVE \n"
                    + "INNER JOIN MONEDAS On PAL_NUM_MONEDA=MON_NUM_PAIS \n"
                    + "LEFT OUTER JOIN BENEFICI On BEN_NUM_CONTRATO = PAL_NUM_CONTRATO AND PAL_CVE_PERS_FID = 'FIDEICOMISARIO' AND BEN_BENEFICIARIO= PAL_NUM_PERS_FID \n"
                    + "LEFT OUTER JOIN TERCEROS On TER_NUM_CONTRATO = PAL_NUM_CONTRATO AND PAL_CVE_PERS_FID = 'TERCERO' AND TER_NUM_TERCERO =PAL_NUM_PERS_FID \n"
                    + "INNER JOIN (SELECT A.CVE_NUM_SEC_CLAVE CLAVE_BANCO, A.CVE_DESC_CLAVE DESC_BANCO  fROM CLAVES A   WHERE   A.CVE_NUM_CLAVE=27) ON CLAVE_BANCO = PAL_NUM_BANCO \n"
                    + "WHERE PAL_NUM_CONTRATO= ?  \n"
                    + "and PAL_CVE_PERS_FID= ? \n"
                    + "and PAL_NUM_PERS_FID= ? \n"
                    + "and PAL_NUM_PERS_BEN=0 and PAL_CVE_ST_PALIQUI='ACTIVO'";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, Long.parseLong(fideicomiso));
            pstmt.setString(2, tipoPersona);
            pstmt.setInt(3, numeroPersona);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                parametroLiquidacion = new ParametrosLiquidacionBean();
                parametroLiquidacion.setNOMBRE(rs.getString("NOMBRE"));
                parametroLiquidacion.setPAL_CVE_TIPO_LIQ(rs.getInt("PAL_CVE_TIPO_LIQ"));
                parametroLiquidacion.setPAL_NUM_MONEDA(rs.getInt("PAL_NUM_MONEDA"));
                parametroLiquidacion.setPAL_CVE_TIPO_CTA(rs.getString("PAL_CVE_TIPO_CTA"));
                parametroLiquidacion.setPAL_NUM_BANCO(rs.getInt("PAL_NUM_BANCO"));
                parametroLiquidacion.setPAL_NUM_PLAZA(rs.getInt("PAL_NUM_PLAZA"));
                parametroLiquidacion.setPAL_NUM_SUCURSAL(rs.getInt("PAL_NUM_SUCURSAL"));
                parametroLiquidacion.setPAL_NOM_SUCURSAL(rs.getString("PAL_NOM_SUCURSAL"));
                parametroLiquidacion.setPAL_NUM_CUENTA(rs.getDouble("PAL_NUM_CUENTA"));
                parametroLiquidacion.setPAL_NUM_PAIS(rs.getInt("PAL_NUM_PAIS"));
                parametroLiquidacion.setPAL_CTA_BANXICO(rs.getString("CLABE"));
                parametroLiquidacion.setPAL_DIR_APER_CTA(rs.getString("PAL_DIR_APER_CTA"));
                parametroLiquidacion.setPAL_CODIGO_TRANS(rs.getString("PAL_CODIGO_TRANS"));
                parametroLiquidacion.setPAL_NUM_INICIATIVA(rs.getInt("PAL_NUM_INICIATIVA"));
                parametroLiquidacion.setPAL_NUM_CTAM(rs.getInt("PAL_NUM_CTAM"));
                parametroLiquidacion.setPAL_NUM_SCTA(rs.getInt("PAL_NUM_SCTA"));
                parametroLiquidacion.setPAL_NUM_SSCTA(rs.getInt("PAL_NUM_SSCTA"));
                parametroLiquidacion.setPAL_NUM_SSSCTA(rs.getInt("PAL_NUM_SSSCTA"));
                parametroLiquidacion.setPAL_NOM_AREA(rs.getString("PAL_NOM_AREA"));
                parametroLiquidacion.setPAL_CONCEPTO(rs.getString("PAL_CONCEPTO"));
                parametroLiquidacion.setPAL_CUENTAS_ORDEN(rs.getInt("PAL_CUENTAS_ORDEN"));
                parametroLiquidacion.setPAL_ANO_ALTA_REG(rs.getInt("PAL_ANO_ALTA_REG"));
                parametroLiquidacion.setPAL_MES_ALTA_REG(rs.getInt("PAL_MES_ALTA_REG"));
                parametroLiquidacion.setPAL_DIA_ALTA_REG(rs.getInt("PAL_DIA_ALTA_REG"));
                parametroLiquidacion.setPAL_ANO_ULT_MOD(rs.getInt("PAL_ANO_ULT_MOD"));
                parametroLiquidacion.setPAL_MES_ULT_MOD(rs.getInt("PAL_MES_ULT_MOD"));
                parametroLiquidacion.setPAL_DIA_ULT_MOD(rs.getInt("PAL_DIA_ULT_MOD"));
                parametroLiquidacion.setPAL_CVE_ST_PALIQUI(rs.getString("PAL_CVE_ST_PALIQUI"));
                parametroLiquidacion.setCVE_DESC_CLAVE(rs.getString("CVE_DESC_CLAVE"));
                parametroLiquidacion.setMON_NOM_MONEDA(rs.getString("MON_NOM_MONEDA"));
                parametroLiquidacion.setDESC_BANCO(rs.getString("DESC_BANCO"));

            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
            logger.error("obtenParametrosLiquidacion()");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("obtenParametrosLiquidacion()-rs.close()");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenParametrosLiquidacion:: Error al cerrar ResultSet.", "10", "20", "30");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("obtenParametrosLiquidacion()-pstmt.close");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenParametrosLiquidacion:: Error al cerrar PreparetStatement.", "10", "20", "30");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("obtenParametrosLiquidacion()-conexion.close()");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenParametrosLiquidacion:: Error al cerrar Connection.", "10", "20", "30");
                }
            }
        }
        return parametroLiquidacion;
    }

    public List<InformacionCuentasParametrosLiquidacionBean> obtenCuentaParametrosLiquidacion(String fideicomiso, String tipoPersona, int numeroPersona, int claveMoneda) {
        List<InformacionCuentasParametrosLiquidacionBean> listInformacionCuentas = new ArrayList<>();
        InformacionCuentasParametrosLiquidacionBean informacionCuenta = null;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT CBA_CVE_TIPO_CTA,CBA_NUM_PLAZA,CBA_NUM_SEC_CTA, CBA_NUM_BANCO,CBA_NUM_SUCURSAL, CBA_NUM_CUENTA,CVE_DESC_CLAVE, CBA_MONEDA  \n"
                    + ",SAF.FUNN_GETCLABE(CBA_NUM_BANCO,CBA_NUM_PLAZA,CBA_NUM_CUENTA) CLABE \n"
                    + "FROM CUENTAS, CLAVES \n"
                    + "WHERE cba_num_banco = cve_num_sec_clave \n"
                    + "AND cve_num_clave = 27 and CBA_NUM_CONTRATO= ? \n"
                    + "AND CBA_CVE_PERSON_FID = ? \n"
                    + "AND CBA_NUM_PERSON_FID = ? \n"
                    + "AND (CBA_CVE_AUT_ABONOS <> 0 OR CBA_CVE_AUT_AMBOS <> 0)  ";
                  //  + "And CBA_MONEDA = ? ";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, Long.parseLong(fideicomiso)); 
            pstmt.setString(2, tipoPersona);
            pstmt.setInt(3, numeroPersona);
            //pstmt.setInt(4, claveMoneda);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                informacionCuenta = new InformacionCuentasParametrosLiquidacionBean();
                informacionCuenta.setCBA_CVE_TIPO_CTA(rs.getString("CBA_CVE_TIPO_CTA"));
                informacionCuenta.setCBA_NUM_PLAZA(rs.getInt("CBA_NUM_PLAZA"));
                informacionCuenta.setCBA_NUM_SEC_CTA(rs.getInt("CBA_NUM_SEC_CTA"));
                informacionCuenta.setCBA_NUM_BANCO(rs.getInt("CBA_NUM_BANCO"));
                informacionCuenta.setCBA_NUM_SUCURSAL(rs.getInt("CBA_NUM_SUCURSAL"));
                informacionCuenta.setCBA_NUM_CUENTA(rs.getDouble("CBA_NUM_CUENTA"));
                informacionCuenta.setCVE_DESC_CLAVE(rs.getString("CVE_DESC_CLAVE"));
                informacionCuenta.setCBA_MONEDA(rs.getInt("CBA_MONEDA"));
                informacionCuenta.setCBA_CLABE(rs.getString("CLABE"));
                listInformacionCuentas.add(informacionCuenta);

            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
            logger.error("obtenCuentaParametrosLiquidacion");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenCuentaParametrosLiquidacion:: Error al cerrar ResultSet.", "10", "20", "30");
                    logger.error("obtenCuentaParametrosLiquidacion-rs.close()");

                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenCuentaParametrosLiquidacion:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");
                    logger.error("obtenCuentaParametrosLiquidacion-pstmt.close()");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtenCuentaParametrosLiquidacion:: Error al cerrar Connection." + e.getMessage(), "10", "20", "30");
                    logger.error("obtenCuentaParametrosLiquidacion-conexion.close()");
                }
            }
        }
        return listInformacionCuentas;
    }
    public int obtieneSecuenciaDePagoBeneficiario(String fideicomiso, String subFideicomiso, int numeroPersona, String folioInstruccion, String concepto) {
        Integer numeroPago = 0;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT max(PBX_SEC_PAGO) AS PAGO \n"
                    + "FROM parpabex \n"
                    + "WHERE pbx_num_contrato = ? \n"
                    + "ANd pbx_sub_contrato = ? \n"
                    + "AND pbx_beneficiario = ? \n"
                    + "AND pbx_num_folio_inst =  ?\n"
                    + "AND pbx_sub_programa = ? ";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setLong(1, Long.parseLong(fideicomiso));
            pstmt.setInt(2, Integer.parseInt(subFideicomiso));
            pstmt.setInt(3, numeroPersona);
            pstmt.setInt(4, Integer.parseInt(folioInstruccion));
            pstmt.setInt(5, Integer.parseInt(concepto));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                numeroPago = rs.getInt("PAGO");
            }

            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
            logger.error("obtieneSecuenciaDePago");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar ResultSet." + e.getMessage(), "10", "20", "30");
                    logger.error("obtieneSecuenciaDePago-rs.close");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");                    
                    logger.error("obtieneSecuenciaDePago-pstmt.close");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("obtieneSecuenciaDePago-conexion.close");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::oobtieneSecuenciaDePago:: Error al cerrar Connection." + e.getMessage(), "10", "20", "30");
                }
            }

        }
        return numeroPago;
    }

    public String obtieneHorario(int iCveTipoLiq, int iBanco , String sTipoTran, int cveSec) {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String resultado = new String();
        try {
            sqlComando = "SELECT SAF.FUNN_CHECA_HORARIO(?,?,?) as RESULTADO FROM saf.CLAVES \n" +
                         "WHERE CVE_NUM_CLAVE = 690 \n" + 
                          "AND  CVE_NUM_SEC_CLAVE= ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setInt(1, iCveTipoLiq);
            pstmt.setInt(2, iBanco);
            pstmt.setString(3, sTipoTran);
            pstmt.setInt(4, cveSec);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                resultado = rs.getString("RESULTADO");
            }
            
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
            logger.error("obtieneSecuenciaDePago");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar ResultSet." + e.getMessage(), "10", "20", "30");
                    logger.error("obtieneSecuenciaDePago-rs.close");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");                    
                    logger.error("obtieneSecuenciaDePago-pstmt.close");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("obtieneSecuenciaDePago-conexion.close");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::oobtieneSecuenciaDePago:: Error al cerrar Connection." + e.getMessage(), "10", "20", "30");
                }
            }

        }
        return resultado;
    }

    public int obtieneSecuenciaDePagoTercero(String fideicomiso, String subFideicomiso, int numeroPersona, String folioInstruccion, String concepto) {
        Integer numeroPago = 0;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            sqlComando = "select max(ptx_sec_pago) as PAGO\n"
                    + " from parpatex where \n"
                    + " ptx_num_contrato = ? and \n"
                    + " ptx_num_tercero = ? and \n"
                    + " ptx_num_folio_inst = ? and \n"
                    + " ptx_sub_contrato = ? \n"
                    + " and ptx_sub_programa = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setLong(1, Long.parseLong(fideicomiso));
            pstmt.setInt(2, numeroPersona);
            pstmt.setInt(3, Integer.parseInt(folioInstruccion));
            pstmt.setInt(4, Integer.parseInt(subFideicomiso));
            pstmt.setInt(5, Integer.parseInt(concepto));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                numeroPago = rs.getInt("PAGO");
            }

            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", Err.getMessage(), "10", "20", "30");
            logger.error("obtieneSecuenciaDePago");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar ResultSet." + e.getMessage(), "10", "20", "30");
                    logger.error("obtieneSecuenciaDePago-rs.close");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::obtieneSecuenciaDePago:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");                    
                    logger.error("obtieneSecuenciaDePago-pstmt.close");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("obtieneSecuenciaDePago-conexion.close");
                    //oGeneraLog.onGeneraLog(CCMonetariasoperacionesOtrosBancos.class, "0D", "ERROR", "20", "40", "Function ::oobtieneSecuenciaDePago:: Error al cerrar Connection." + e.getMessage(), "10", "20", "30");
                }
            }

        }
        return numeroPago;
    }
}