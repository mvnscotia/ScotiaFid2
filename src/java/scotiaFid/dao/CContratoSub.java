/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : CContratoSub.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20211027
 * MODIFICADO  : 20211027
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import scotiaFid.singleton.DataBaseConexion;


public class CContratoSub {
   
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
   // private String                               sqlComando;
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
    public CContratoSub() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CContratoSub.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String onContratoSub_ObtenNombre(Long contratoNumero, int contratoNumeroSub) throws SQLException{
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String contratoSubNombre = new String();
        try{
            String sqlComando = "SELECT sct_nom_sub_cto FROM SAF.SubCont WHERE sct_num_contrato = ? AND sct_sub_contrato = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setInt (2, contratoNumeroSub);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                contratoSubNombre = rs.getString("sct_nom_sub_cto");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContratoSub_ObtenNombre()";
           //oGeneraLog.onGeneraLog(CContratoSub.class, "0D", "ERROR", "20", "40", mensajeError, "10", "20", "30");
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    mensajeError+= "Descripción: " + e.getMessage() + nombreObjeto + "onContratoSub_ObtenNombre()";
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    mensajeError+= "Descripción: " + e.getMessage() + nombreObjeto + "onContratoSub_ObtenNombre()";
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    mensajeError+= "Descripción: " + e.getMessage() + nombreObjeto + "onContratoSub_ObtenNombre()";
                }
            }		
	}
    return contratoSubNombre;
    }
  
}
