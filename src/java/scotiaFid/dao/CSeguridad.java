/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : CReportes.java
 * TIPO        : Clase
 * PAQUETE     : scotiaFid.dao
 * CREADO      : 20210712
 * MODIFICADO  : 20210828
 * AUTOR       : j.ranfery.delatorre
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import scotiaFid.bean.SeguridadPantallaBean;

import scotiaFid.bean.SeguridadPantallaFidBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

public class CSeguridad {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private String sqlComando;
    //private String                               sqlFiltro;
    private String nombreObjeto;
    //private String                               mensajeErrorSP;

    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private Calendar calendario;
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
    public CSeguridad() {
        calendario = Calendar.getInstance();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CSeguridad.";
        valorRetorno = Boolean.FALSE;
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String onSeguridad_ObtenCatalogoMenu() {
        String cadenaMenu = new String();
        try {
            sqlComando = "SELECT menuId, menuNombre FROM SAF.FuncioneMenu ORDER BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cadenaMenu += rs.getString("menuId").concat("»").concat(rs.getString("menuNombre")).concat(",");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción:" + Err.getMessage() + nombreObjeto + "onSeguridad_ObtenCatalogoMenu()";
        } finally {
            onCierraConexion();
        }
        return cadenaMenu.substring(0, cadenaMenu.length() - 1).trim();
    }

    public List<SeguridadPantallaFidBean> onSeguridad_ObtenCatalogoPantallaPorMenu(Short menuId) throws SQLException {
        List<SeguridadPantallaFidBean> consulta = new ArrayList<>();
        try {
            sqlComando = "SELECT menuNombre, pantallaId, pantallaTitulo, pantallaURL, pantallaSSO, PANTALLAESCRITURA \n"
                    + "FROM   SAF.FuncioneWeb  fw   \n"
                    + "JOIN   SAF.FuncioneMenu fm   \n"
                    + "ON     fw.menuId = fm.menuId \n"
                    + "WHERE  pantallaStatus = ?    \n"
                    + "AND    fw.menuId      = ?    \n"
                    + "ORDER  BY 3";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            pstmt.setShort(2, menuId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                SeguridadPantallaFidBean spf = new SeguridadPantallaFidBean();
                spf.setSegPantallaFidMenu(rs.getString("menuNombre"));
                spf.setSegPantallaFidId(rs.getString("pantallaId"));
                spf.setSegPantallaFidTitulo(rs.getString("pantallaTitulo"));
                spf.setSegPantallaFidURL(rs.getString("pantallaURL"));
                spf.setSegPantallaFidSSO(rs.getString("pantallaSSO"));
                spf.setSegPantallaFidEscritura(rs.getString("PANTALLAESCRITURA"));

                consulta.add(spf);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción:" + Err.getMessage() + nombreObjeto + "onSeguridad_ObtenCatalogoPantallaPorMenu()";
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public List<SeguridadPantallaFidBean> onSeguridad_ObtenCatalogoPantalla() {
        List<SeguridadPantallaFidBean> consulta = new ArrayList<>();
        try {
            sqlComando = "SELECT menuNombre, pantallaId, pantallaTitulo, pantallaURL, pantallaSSO \n"
                    + "FROM   SAF.FuncioneWeb  fw   \n"
                    + "JOIN   SAF.FuncioneMenu fm   \n"
                    + "ON     fw.menuId = fm.menuId \n"
                    + "WHERE  pantallaStatus = ?    \n"
                    + "ORDER  BY 3";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                SeguridadPantallaFidBean spf = new SeguridadPantallaFidBean();
                spf.setSegPantallaFidMenu(rs.getString("menuNombre"));
                spf.setSegPantallaFidId(rs.getString("pantallaId"));
                spf.setSegPantallaFidTitulo(rs.getString("pantallaTitulo"));
                spf.setSegPantallaFidURL(rs.getString("pantallaURL"));
                spf.setSegPantallaFidSSO(rs.getString("pantallaSSO"));

                consulta.add(spf);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción:" + Err.getMessage() + nombreObjeto + "onSeguridad_ObtenCatalogoPantalla()";
        } finally {

            onCierraConexion();
        }
        return consulta;
    }

    public Boolean onSeguridad_ObtenAccesoDiaInhabil(List<SeguridadPantallaBean> listaTrans) {
        try {
            String cadena = new String();
            final String noTransa;
            noTransa = "80890010";
            for (int x = 0; x < listaTrans.size(); x++) {
                cadena = listaTrans.get(x).getPantallaId();
                if (cadena.equals(noTransa)) {
                    valorRetorno = Boolean.TRUE;
                }
            }

        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onCierraConexion() {
        try{
            if(rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción:" + Err.getMessage() + nombreObjeto + "onSeguridad_onCierraConexion()";
        }
    }
}
