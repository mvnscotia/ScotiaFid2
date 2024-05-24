/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.singleton;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lenovo
 */
public class DataBaseConexion {

    private static final Logger logger = LogManager.getLogger(DataBaseConexion.class);

    private static DataBaseConexion instance;
    private static Connection connection;
    private static final String URL_CONEXION = "java:/FIDUCI";

    private DataBaseConexion() {
        InitialContext ic;
        DataSource ds;
        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup(URL_CONEXION);
            this.connection = ds.getConnection();
        } catch (NamingException e) {
            logger.error("Conexión[NamingException] " + e.getMessage(), e.getCause());
        } catch (SQLException e) {
            logger.error("Conexión[SQLException] " + e.getMessage(), e.getCause());
        }
    }

    public synchronized Connection getConnection() {
        return connection;
    }

    public synchronized static DataBaseConexion getInstance() throws SQLException {
        instance = new DataBaseConexion();
        return instance;
    }
}
