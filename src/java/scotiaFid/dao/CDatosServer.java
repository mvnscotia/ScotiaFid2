/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.singleton.DataBaseConexion;

/**
 *
 * @author lenovo
 */
public class CDatosServer {

    private static final Logger logger = LogManager.getLogger(CDatosServer.class);

    public void getDominio() {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sqlDominios = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES \n"
                    + " WHERE CVE_NUM_CLAVE = 761 \n "
                    + "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlDominios);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dominio", rs.getString("CVE_DESC_CLAVE"));
            }
        } catch (SQLException Err) {
            logger.error("Descripción: " + "getDominios()");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar Connection.", e.getMessage());
                }
            }
        }
    }

}
