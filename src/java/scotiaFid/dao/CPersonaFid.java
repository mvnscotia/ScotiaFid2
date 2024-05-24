/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : CPersonaFid.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210928
 * MODIFICADO  : 20210928
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.singleton.DataBaseConexion;

public class CPersonaFid {
    
    private static final Logger logger = LogManager.getLogger(CPersonaFid.class);
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
    private String                               sqlComando;
    private String                               nombreObjeto;
    
    private CallableStatement                    cstmt;
    private Connection                           conexion;
    private PreparedStatement                    pstmt;
    private ResultSet                            rs;
    
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    private String                               mensajeError;
    
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    public String getMensajeError() {
        return mensajeError;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    public CPersonaFid() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CPersonaFid.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean onCuenta_VerificaExistencia(long contratoNumero, String personaRolFid, short personaRolFidSec, String personaCuenta){
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total  \n" +
                         "FROM   SAF.Cuentas            \n" +
                         "WHERE  cba_num_contrato   = ? \n" +
                         "AND    cba_cve_person_fid = ? \n" +
                         "AND    cba_num_person_fid = ? \n" +
                         "AND    cba_num_cuenta     = ? \n";
            
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong  (1, contratoNumero);
            pstmt.setString(2, personaRolFid);
            pstmt.setShort (3, personaRolFidSec);
            pstmt.setString(4, personaCuenta);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")>0) valorRetorno = Boolean.TRUE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onCuenta_VerificaExistencia()";
            logger.error( mensajeError);
            valorRetorno = Boolean.FALSE;
        }finally{
            
            onCierraConexion();		
        }return valorRetorno;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    private void onCierraConexion() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar ResultSet."+ e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
            logger.error("Function :: Error al cerrar PreparetStatement."+ e.getMessage());
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar CallableStatement."+ e.getMessage());
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar Connection."+ e.getMessage());
            }
        }
    }
}