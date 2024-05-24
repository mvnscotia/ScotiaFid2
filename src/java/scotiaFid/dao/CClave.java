/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : CClave.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210320
 * MODIFICADO  : 20210708
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210705.- 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.ClaveBean;
import scotiaFid.bean.CriterioBusquedaClaveBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;
//import scotiaFid.util.CConexion;

public class CClave {

    private static final Logger logger = LogManager.getLogger(CClave.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private Integer paramSec;
    private String sqlComando;
    private String sqlFiltro;
    private String nombreObjeto;


//    private CallableStatement                    cstmt;
//    private Connection                           conexion;
//    private PreparedStatement                    pstmt;
//    private ResultSet                            rs;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CClave() {
        LogsContext.FormatoNormativo();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CClave.";
        valorRetorno = Boolean.FALSE;
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void onClave_ObtenClave(ClaveBean cve) throws SQLException {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT cve_desc_clave, cve_liminf_clave, cve_limsup_clave,\n"
                    + "       cve_forma_emp_cve, cve_cve_st_clave                \n"
                    + "FROM   SAF.Claves            \n"
                    + "WHERE  cve_num_clave     = ? \n"
                    + "AND    cve_num_sec_clave = ?";
            //System.out.println("Antes de obtener conexion a base de datos (obtencion de hora maxima de acceso al sistema)" + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            conexion = DataBaseConexion.getInstance().getConnection();
            //System.out.println("Obtuvo conexion "+ new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, cve.getClaveNumero());
            pstmt.setInt(2, cve.getClaveSec());
            rs = pstmt.executeQuery();
            //System.out.println("Ejecuto consulta "+ new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            if (rs.next()) {
                cve.setClaveDesc(rs.getString("cve_desc_clave"));
                cve.setClaveLimInf(rs.getDouble("cve_liminf_clave"));
                cve.setClaveLimSup(rs.getDouble("cve_limsup_clave"));
                cve.setClaveFormaEmp(rs.getString("cve_forma_emp_cve"));
                cve.setClaveStatus(rs.getString("cve_cve_st_clave"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción : " + Err.getMessage() + nombreObjeto + "onClave_ObtenClave()";
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                mensajeError += "Function ::onCierraConexion:: Error al cerrar Connection." + e.getMessage();
            }
        }
    }

    public List<ClaveBean> onClave_Consulta(CriterioBusquedaClaveBean cb) throws SQLException {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ClaveBean> consulta = new ArrayList<>();
        try {
            paramSec = 0;
            sqlFiltro = "WHERE ";
            sqlComando = "SELECT cve_num_clave, cve_num_sec_clave, cve_desc_clave,     \n"
                    + "       cve_liminf_clave, cve_limsup_clave, cve_forma_emp_cve,\n"
                    + "       cve_cve_st_clave        \n"
                    + "FROM   SAF.Claves              \n";
            if (cb.getCriterioCveNumero() != null) {
                sqlFiltro = sqlFiltro.concat("cve_num_clave     = ? AND \n");
            }
            if (cb.getCriterioCveSec() != null) {
                sqlFiltro = sqlFiltro.concat("cve_num_sec_clave = ? AND \n");
            }
            if ((cb.getCriterioCveFormaEmp() != null) && (!cb.getCriterioCveFormaEmp().equals(new String()))) {
                sqlFiltro = sqlFiltro.concat("cve_forma_emp_cve = ? AND \n");
            }
            if ((cb.getCriterioCveStatus() != null) && (!cb.getCriterioCveStatus().equals(new String()))) {
                sqlFiltro = sqlFiltro.concat("cve_cve_st_clave  = ? AND \n");
            }

            if (sqlFiltro.contains("AND")) {
                sqlFiltro = sqlFiltro.substring(0, sqlFiltro.length() - 5).trim();
                sqlComando = sqlComando.concat(sqlFiltro).concat(" \n");
            }
            sqlComando = sqlComando.concat("ORDER BY 1,2");

            //System.out.println("Antes de obtener conexion (obtencion de parametros para el SSO) " + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            conexion = DataBaseConexion.getInstance().getConnection();
            //System.out.println("Obtuvo conexion " + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            pstmt = conexion.prepareStatement(sqlComando);
            if (cb.getCriterioCveNumero() != null) {
                paramSec++;
                pstmt.setInt(paramSec, cb.getCriterioCveNumero());
            }
            if (cb.getCriterioCveSec() != null) {
                paramSec++;
                pstmt.setInt(paramSec, cb.getCriterioCveSec());
            }
            if ((cb.getCriterioCveFormaEmp() != null) && (!cb.getCriterioCveFormaEmp().equals(new String()))) {
                paramSec++;
                pstmt.setString(paramSec, cb.getCriterioCveFormaEmp());
            }
            if ((cb.getCriterioCveStatus() != null) && (!cb.getCriterioCveStatus().equals(new String()))) {
                paramSec++;
                pstmt.setString(paramSec, cb.getCriterioCveStatus());
            }

            rs = pstmt.executeQuery();
            //System.out.println("Ejecuto consulta " + new java.text.SimpleDateFormat("HH:MM:SS").format(new java.util.Date().getTime()));
            while (rs.next()) {
                ClaveBean cve = new ClaveBean();
                cve.setClaveNumero(rs.getInt("cve_num_clave"));
                cve.setClaveSec(rs.getInt("cve_num_sec_clave"));
                cve.setClaveDesc(rs.getString("cve_desc_clave"));
                cve.setClaveLimInf(rs.getDouble("cve_liminf_clave"));
                cve.setClaveLimSup(rs.getDouble("cve_limsup_clave"));
                cve.setClaveFormaEmp(rs.getString("cve_forma_emp_cve"));
                cve.setClaveStatus(rs.getString("cve_cve_st_clave"));
                consulta.add(cve);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción : " + Err.getMessage() + nombreObjeto + "onClave_Consulta()";
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                mensajeError += "Function ::onCierraConexion:: Error al cerrar Connection." + e.getMessage();
            }
        }
        return consulta;
    }

    public String onClave_ObtenDesc(int claveNumero, int claveSec) throws SQLException {
        String elemento = new String();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT cve_desc_clave        \n"
                    + "FROM   SAF.Claves            \n"
                    + "WHERE  cve_num_clave     = ? \n"
                    + "AND    cve_num_sec_clave = ? \n";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, claveNumero);
            pstmt.setInt(2, claveSec);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                elemento = rs.getString("cve_desc_clave");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException e) {
            mensajeError += "Descripción : " + e.getMessage() + nombreObjeto + "onClave_ObtenDesc()";
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                mensajeError += "Function ::onCierraConexion:: Error al cerrar Connection." + e.getMessage();
            }
        }
        return elemento;
    }

    public List<String> onClave_ObtenListadoElementos(int claveNumero) throws SQLException {
        List<String> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT cve_desc_clave FROM SAF.Claves WHERE cve_num_clave = ? ORDER BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, claveNumero);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("cve_desc_clave"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onClave_ObtenListadoElementos()";
            valorRetorno = Boolean.FALSE;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                mensajeError += "Function ::onCierraConexion:: Error al cerrar Connection." + e.getMessage();
            }
        }
        return lista;
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
 /*   private void onCierraConexion(){
        if (rs      != null) rs       = null;
        if (pstmt   != null) pstmt    = null;
        if (cstmt   != null) cstmt    = null;
        if (conexion!= null) conexion = null;
    }*/
}
