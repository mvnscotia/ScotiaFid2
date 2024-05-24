/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank.
 * ARCHIVO     : CFeriado.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210320
 * MODIFICADO  : 20210320
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.singleton.DataBaseConexion;

public class CFeriado {
    private static final Logger logger = LogManager.getLogger(CFeriado.class);
  /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
    private String                               sqlComando;
    private String                               nombreObjeto;
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
    public CFeriado() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CFecha.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */   
    public Boolean onFeriado_VerificaSiEsDiaFeriado(int dia, int mes, int paisId) throws SQLException{
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total \n" +
                         "FROM   SAF.Feriados          \n" +
                         "WHERE  fer_num_pais = ?      \n" +
                         "AND    fer_fec_mes  = ?      \n" +
                         "AND    fer_fec_dia  = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, paisId);
            pstmt.setInt(2, mes);
            pstmt.setInt(3, dia);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")>0) valorRetorno = Boolean.TRUE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onFeriado_VerificaSiEsDiaFeriado()";
            logger.error( mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error( "Function ::onFeriado_VerificaSiEsDiaFeriado:: Error al cerrar ResultSet."+ e.getMessage(),"10", "20", "30");
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                   logger.error( "Function ::onFeriado_VerificaSiEsDiaFeriado:: Error al cerrar PreparetStatement."+ e.getMessage(),"10", "20", "30");
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                   logger.error( "Function ::onFeriado_VerificaSiEsDiaFeriado:: Error al cerrar Connection."+ e.getMessage(),"10", "20", "30");
                }
            }		
	}
    return valorRetorno;
    }

}
