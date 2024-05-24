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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.MonedasBean;
import scotiaFid.bean.NombreBean;
import scotiaFid.bean.RecepcionCuentaBean;
import scotiaFid.bean.ComplementoMonitorCheques;
import scotiaFid.bean.ViewRecepcionCuentaBancoBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

/**
 *
 * @author lenovo
 */
public class CCRecepcionCuentasBanco {
//    private String sqlComando;
    private String mensajeError;
    private String mensajeErrorSP;
    private String nombreObjeto;

private static final Logger logger = LogManager.getLogger(CCRecepcionCuentasBanco.class);
//    private Connection conexion;
    public CCRecepcionCuentasBanco() {
        LogsContext.FormatoNormativo();
        nombreObjeto = "\nFuente: scotiaFid.dao.CCRecepcionCuentasBanco.";
    }

    public List<NombreBean> getNombresRecepcionCuentas(String fideicomiso, int tipoPersona) throws SQLException {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlComando = new String();
        List<NombreBean> listNombreBean = new ArrayList<>();
        try {
            switch (tipoPersona) {
                case 1:
                    sqlComando = " SELECT FID_FIDEICOMITENTE, FID_NOM_FIDEICOM FROM SAF.FIDEICOM WHERE FID_NUM_CONTRATO = ? And FID_CVE_ST_FIDEICO = 'ACTIVO'";
                    break;
                case 3:
                    sqlComando = "SELECT rtrim(ltrim(replace(TER_NOM_TERCERO,chr(9),' '))) as TER_NOM_TERCERO, TER_NUM_TERCERO FROM TERCEROS WHERE TER_NUM_CONTRATO = ? And TER_CVE_ST_TERCERO = 'ACTIVO'";
                    break;
                case 4:
                    sqlComando = "SELECT rtrim(ltrim(replace(BEN_NOM_BENEF,chr(9),' '))) as BEN_NOM_BENEF , BEN_BENEFICIARIO FROM SAF.BENEFICI WHERE BEN_NUM_CONTRATO = ? AND BEN_CVE_ST_BENEFIC = 'ACTIVO'";
                    break;
                default:
                    break;
            }
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, Long.parseLong(fideicomiso));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                NombreBean nombreBean = new NombreBean();
                if (tipoPersona == 1) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("FID_FIDEICOMITENTE"));
                    nombreBean.setBeneficiarioNombre(rs.getString("FID_FIDEICOMITENTE").concat(".-").concat(rs.getString("FID_NOM_FIDEICOM")));
                } else if (tipoPersona == 3) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("TER_NUM_TERCERO"));
                    nombreBean.setBeneficiarioNombre(rs.getString("TER_NUM_TERCERO").concat(".-").concat(rs.getString("TER_NOM_TERCERO")));
                } else if (tipoPersona == 4) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("BEN_BENEFICIARIO"));
                    nombreBean.setBeneficiarioNombre(rs.getString("BEN_BENEFICIARIO").concat(".-").concat(rs.getString("BEN_NOM_BENEF")));
                }

                listNombreBean.add(nombreBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   logger.error("Error funcion: getNombresRecepcionCuentas");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                  logger.error("Error funcion: getNombresRecepcionCuentas");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Error funcion: getNombresRecepcionCuentas");
                }
            }
        }
        return listNombreBean;
    }

    public List<MonedasBean> getMonedas() throws SQLException {
        List<MonedasBean> listMonedas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlComando = new String();
        try {
            sqlComando = "SELECT MON_NUM_PAIS,MON_NOM_MONEDA from SAF.MONEDAS";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MonedasBean monedaBean = new MonedasBean();
                monedaBean.setMonedaNumero(rs.getInt("MON_NUM_PAIS"));
                monedaBean.setMonedaNombre(rs.getString("MON_NOM_MONEDA"));
                listMonedas.add(monedaBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                logger.error("Error en funcion: getMonedas");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion: getMonedas");    
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion: getMonedas");
                }
            }
        }
        return listMonedas;
    }

    public List<ViewRecepcionCuentaBancoBean> getCuentaRecepcion(String contrato, String subcontrato) throws SQLException {
        List<ViewRecepcionCuentaBancoBean> listCuentaBanco = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlComando = new String();
        try {
            sqlComando = "SELECT ADC_CUENTA,ADC_BANCO,ADC_PLAZA,ADC_SUCURSAL,ADC_NUM_MONEDA,CVE_DESC_CLAVE,MON_NOM_MONEDA from SAF.FID_CTAS Inner Join SAF.CLAVES On CVE_NUM_CLAVE=27 AND CVE_NUM_SEC_CLAVE= ADC_BANCO Inner Join SAF.MONEDAS On MON_NUM_PAIS=ADC_NUM_MONEDA where ADC_NUM_CONTRATO = " + contrato + " and ADC_SUB_CONTRATO = " + subcontrato + " AND ADC_ESTATUS= 'ACTIVO'";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ViewRecepcionCuentaBancoBean recepcionCuentaBancoBean = new ViewRecepcionCuentaBancoBean();
                recepcionCuentaBancoBean.setCuenta(rs.getString("ADC_CUENTA"));
                recepcionCuentaBancoBean.setBanco(rs.getInt("ADC_BANCO"));
                recepcionCuentaBancoBean.setPlaza(rs.getInt("ADC_PLAZA"));
                recepcionCuentaBancoBean.setSucursal(rs.getInt("ADC_SUCURSAL"));
                recepcionCuentaBancoBean.setNumMoneda(rs.getInt("ADC_NUM_MONEDA"));
                recepcionCuentaBancoBean.setDescClave(rs.getString("CVE_DESC_CLAVE"));
                recepcionCuentaBancoBean.setNomMoneda(rs.getString("MON_NOM_MONEDA"));
                listCuentaBanco.add(recepcionCuentaBancoBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion: getCuentaRecepcion");    
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion: getCuentaRecepcion"); 
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion: getCuentaRecepcion"); 
                }
            }
        }
        return listCuentaBanco;
    }

    public ComplementoMonitorCheques getComplementoMonitorCheques(String contrato, String subcontrato, String cuenta) {
        ComplementoMonitorCheques complementoMonitorCheque = new ComplementoMonitorCheques();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlComando = new String();
        try {
            sqlComando = "SELECT CVE_DESC_CLAVE BANCO,ADC_PLAZA PLAZA,ADC_CUENTA CUENTA, \n"
                    + "          ADC_CLABE CLABE,ADC_SUCURSAL SUCURSAL,COALESCE(SALD_SALDO_ACTUAL,0) \n"
                    + "          SALDO,MON_NOM_MONEDA MONEDA,ADC_NUM_MONEDA \n"
                    + "FROM FID_CTAS INNER JOIN MONEDAS On MON_NUM_PAIS = ADC_NUM_MONEDA \n"
                    + "INNER JOIN CLAVES On  CVE_NUM_CLAVE = 27 AND CVE_NUM_SEC_CLAVE= ADC_BANCO \n"
                    + "LEFT OUTER JOIN FDSALDOS On  SALD_AX1 = ADC_NUM_CONTRATO \n"
                    + "            AND SALD_AX2 = ADC_SUB_CONTRATO \n"
                    + "            AND SALD_AX3 = decimal(ADC_CUENTA) \n"
                    + "            AND  CCON_CTA = 1103 And CCON_SCTA = 1 \n"
                    + "            And CCON_2SCTA = 2 And CCON_3SCTA = 1  \n"
                    + "WHERE ADC_NUM_CONTRATO= " + contrato + " \n"
                    + "  AND ADC_SUB_CONTRATO = " + subcontrato + " \n"
                    + "  AND ADC_ESTATUS='ACTIVO' \n"
                    + "  AND ADC_BANCO = 44 \n "
                    + "  AND LPAD(ADC_PLAZA,3,0) || RIGHT(ADC_CUENTA,8)  = '" + cuenta + "' \n";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                complementoMonitorCheque.setBanco(rs.getString("BANCO"));
                complementoMonitorCheque.setPlaza(rs.getInt("PLAZA"));
                complementoMonitorCheque.setCuenta(rs.getString("CUENTA"));
                complementoMonitorCheque.setClabe(rs.getString("CLABE"));
                complementoMonitorCheque.setSucursal(rs.getInt("SUCURSAL"));
                complementoMonitorCheque.setSaldo(rs.getDouble("SALDO"));
                complementoMonitorCheque.setMoneda(rs.getString("MONEDA"));
                complementoMonitorCheque.setAdcNumMoneda(rs.getInt("ADC_NUM_MONEDA"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   logger.error("Error funcion: getComplementoMonitorCheques"); //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", "Function ::getComplementoMonitorCheques:: Error al cerrar ResultSet." + e.getMessage(), "10", "20", "30");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                      logger.error("Error funcion: getComplementoMonitorCheques"); //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", "Function ::getComplementoMonitorCheques:: Error al cerrar PreparetStatement." + e.getMessage(), "10", "20", "30");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                      logger.error("Error funcion: getComplementoMonitorCheques"); //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", "Function ::getComplementoMonitorCheques:: Error al cerrar Connection." + e.getMessage(), "10", "20", "30");
                }
            }
        }
        return complementoMonitorCheque;
    }

    public Map<String, String> onGuardaSPProcesaDepo(RecepcionCuentaBean recepcionCuenta) {
        Map<String, String> responseSPProcesa = new HashMap<>();
        Connection conexion = null;
        CallableStatement cstmt = null;
        String sqlComando = new String();
        try {
            //sqlComando = "{call DB2FIDUC.SPN_PROCESA_DEPO(:IUSUARIO,:SIDTERMINAL,:SFORMAORIGEN,:LNUMCONTRATO,:LSUBCONTRATO,:SDESTINODEP,:SFORMADEP,:DTFECHARECEP,:STIPOPERSONA,:SNOMBREPERS,:INUMPERSONA,:SCTACHEQUES,:SSUCURSAL,:LPLAZA,:IBANCO,:SNOMBANCO,:IMONEDA,:DIMPORTEDEP,:DTTIPOCAMB,:DTIVA,:SCONCEPTO,?,?,?,?,?);}";
            sqlComando = "{call DB2FIDUC.SPN_PROCESA_DEPO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("IUSUARIO", recepcionCuenta.getiUsuario());
            cstmt.setString("SIDTERMINAL", recepcionCuenta.getsIdTerminal());
            cstmt.setString("SFORMAORIGEN", recepcionCuenta.getsFormaOrigen());
            cstmt.setInt("LNUMCONTRATO", recepcionCuenta.getlNumContrato());
            cstmt.setInt("LSUBCONTRATO", recepcionCuenta.getlSubContrato());
            cstmt.setString("SDESTINODEP", recepcionCuenta.getsDestinoDep());
            cstmt.setString("SFORMADEP", recepcionCuenta.getsFormaDep());
            cstmt.setDate("DTFECHARECEP", recepcionCuenta.getDtFechaRecep());
            cstmt.setString("STIPOPERSONA", recepcionCuenta.getsTipoPersona());
            cstmt.setString("SNOMBREPERS", recepcionCuenta.getsNombrePers());
            cstmt.setInt("INUMPERSONA", recepcionCuenta.getiNumPersona());
            cstmt.setString("SCTACHEQUES", recepcionCuenta.getsCtaCheques());
            cstmt.setString("SSUCURSAL", recepcionCuenta.getsSucursal());
            cstmt.setInt("LPLAZA", recepcionCuenta.getlPlaza());
            cstmt.setInt("IBANCO", recepcionCuenta.getiBanco());
            cstmt.setString("SNOMBANCO", recepcionCuenta.getsNomBanco());
            cstmt.setInt("IMONEDA", recepcionCuenta.getiMoneda());
            cstmt.setDouble("DIMPORTEDEP", recepcionCuenta.getdImporteDep());
            cstmt.setInt("DTTIPOCAMB", recepcionCuenta.getDtTipoCamb());
            cstmt.setInt("DTIVA", recepcionCuenta.getDtIVA());
            cstmt.setString("SCONCEPTO", recepcionCuenta.getsConcepto());

            cstmt.registerOutParameter("bEjecuto", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("iNumFolioContab", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT");
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                responseSPProcesa.put("folioContable", String.valueOf(cstmt.getInt("iNumFolioContab")));
            } else {
                responseSPProcesa.put("Error", mensajeErrorSP);
            }

            cstmt.close();
            conexion.close();

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCancelaSaldo_Ejecuta()";
            //oGeneraLog.onGeneraLog(CCRecepcionCuentasBanco.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();  
                } catch (SQLException e) {
                    logger.error("Error en funcion onGuardaSPProcesaDepo");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Error en funcion onGuardaSPProcesaDepo");
                }
            }
        }
        return responseSPProcesa;
    }
}
