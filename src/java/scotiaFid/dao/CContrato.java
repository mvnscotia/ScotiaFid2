/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : CContrato.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210318
 * MODIFICADO  : 20210903
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210903.- Se implementa metodo onContrato_VerificaAtencion()
 *                          Se implementa metodo onContrato_VerificaExistenciaSubFiso()  
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.AdminContratoSubBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;
//import scotiaFid.util.CConexion;

public class CContrato {
    private static final Logger logger = LogManager.getLogger(CContrato.class);
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
    private String                               sqlComando;
//    private String                               sqlFiltro;
    private String                               nombreObjeto;
    private String                               mensajeErrorSP;
    
//    private CallableStatement                    cstmt;
//    private Connection                           conexion;
//    private PreparedStatement                    pstmt;
//    private ResultSet                            rs;
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
    public CContrato() {
        LogsContext.FormatoNormativo();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CContrato.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    public String onContrato_ObtenNegocio(long contratoNumero)throws SQLException{
        String contratoNegocio = new String();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT cto_cve_tipo_neg FROM SAF.Contrato WHERE cto_num_contrato = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                contratoNegocio = rs.getString("cto_cve_tipo_neg");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenNegocio()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNegocio:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNegocio:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNegocio:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
return contratoNegocio;
    }
    public String onContrato_ObtenProducto(long contratoNumero)throws SQLException{
        String contratoProducto = new String();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT cto_cve_clas_prod FROM SAF.Contrato WHERE cto_num_contrato = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                contratoProducto = rs.getString("cto_cve_clas_prod");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenProducto()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenProducto:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenProducto:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenProducto:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
return contratoProducto;
    }
    public String onContrato_ObtenNombre(long contratoNumero)throws SQLException{
        String contratoNombre = new String();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT cto_nom_contrato FROM SAF.Contrato WHERE cto_num_contrato = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                contratoNombre = rs.getString("cto_nom_contrato");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenNombre()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNombre:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNombre:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenNombre:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return contratoNombre;
    }
public Boolean onContrato_VerificaAtencion(List<String> listaContratoFiltro, long contratoNumeroSolicitado){
        try{
            valorRetorno = Boolean.FALSE;
            for (int itemFiso=0; itemFiso<= listaContratoFiltro.size()-1; itemFiso++){
                if (Long.parseLong(listaContratoFiltro.get(itemFiso))== contratoNumeroSolicitado){
                    valorRetorno = Boolean.TRUE;
                    break;
                }
            }
        }catch(NumberFormatException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_VerificaAtencion()";
            logger.error(mensajeError);
        }return valorRetorno;
    }
    public Boolean onContrato_VerificaExistencia(long contratoNumero) {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total \n" +
                         "FROM   SAF.Contrato          \n" +
                         "WHERE  CTO_NUM_CONTRATO = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")>0)
                    valorRetorno = Boolean.TRUE;
                else
                    valorRetorno = Boolean.FALSE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_VerificaExistencia()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistencia:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistencia:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistencia:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return valorRetorno;
    }
    public Boolean onContrato_VerificaExistenciaSubContrato(long contratoNumero, int contratoSub,  int usuario) {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total \n" +
                         "FROM   SAF.SubCont,           \n" +
                         "SAF.USUARIOS U \n" +
                         "WHERE  sct_num_contrato = ?  \n" +
                         "AND    sct_sub_contrato = ?  \n" +
                         "AND u.usu_num_usuario = ?";
            conexion   = DataBaseConexion.getInstance().getConnection(); 
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setInt (2, contratoSub);
            pstmt.setInt (3, usuario);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")>0)
                    valorRetorno = Boolean.TRUE;
                else
                    valorRetorno = Boolean.FALSE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_VerificaExistenciaSubContrato()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistenciaSubContrato:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistenciaSubContrato:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaExistenciaSubContrato:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return valorRetorno;
    }
    public Boolean onContrato_VerificaSiEstaActivo(long contratoNumero) throws SQLException{
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
         try{
            sqlComando = "SELECT cto_cve_st_contrat FROM SAF.Contrato WHERE cto_num_contrato = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (!rs.getString("cto_cve_st_contrat").equals("EXTINTO")) //Cuidado con la cancelacion de SALDOS
                    valorRetorno = Boolean.TRUE;
                else
                    valorRetorno = Boolean.FALSE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_VerificaSiEstaActivo()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiEstaActivo:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiEstaActivo:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiEstaActivo:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return valorRetorno;
    }
    public List<String> onContrato_ListadoSubContratoSrv(long fideicomiso) {

        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<String> listSubContratos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {

            // SQL
            stringBuilder.append("SELECT SCT_SUB_CONTRATO, SCT_NOM_SUB_CTO  ");
            stringBuilder.append("FROM SAF.SUBCONT ");
            stringBuilder.append("WHERE SCT_NUM_CONTRATO = ? ");
            stringBuilder.append("AND SCT_CVE_ST_SUBCONT = 'ACTIVO' ");
            //Ordernar por número de SubSifo
            stringBuilder.append("ORDER BY SCT_SUB_CONTRATO");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setLong(1, fideicomiso);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) {
                // Set_Rutinas
                listSubContratos.add("0");

                while (rs.next()) {

                    // Add_List
                    listSubContratos.add(String.valueOf(rs.getString("SCT_SUB_CONTRATO") + ".-" + rs.getString("SCT_NOM_SUB_CTO")));
                }
            }
        } catch (SQLException Err) {
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ListadoSubContratoSrv()";
            logger.error(mensajeError);
        } finally {
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar Connection.", e.getMessage());
                }
            }		
        }

        return listSubContratos;
    }


    public Boolean onContrato_VerificaSiTieneSubFisos(long contratoNumero, List<AdminContratoSubBean> listaSubFisos) throws SQLException{
        Boolean resultado = Boolean.FALSE;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
         try{    
            sqlComando = "SELECT sct_sub_contrato, sct_nom_sub_cto FROM SAF.SubCont WHERE sct_num_contrato = ? AND sct_cve_st_subcont = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong  (1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            rs         = pstmt.executeQuery();
            
            while (rs.next()){
                AdminContratoSubBean sf = new AdminContratoSubBean();
                sf.setContratoSub(rs.getInt("sct_sub_contrato"));
                sf.setContratoSubNombre(rs.getString("sct_nom_sub_cto"));
                sf.setContratoSubStatus("ACTIVO");
                listaSubFisos.add(sf);
            }
            rs.close();
            pstmt.close();
            conexion.close();
            
            if (listaSubFisos.size()>0){
                resultado = Boolean.TRUE;
            }
            
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_VerificaSiTieneSubFisos()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_VerificaSiTieneSubFisos:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return resultado;
    }
    public List<String> onContrato_ObtenListadoContratoNumeroNombreActivo(int usuarioNumero)throws SQLException{
        List<String> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT cto_num_contrato||'.- '||cto_nom_contrato Nombre \n" +
                         "FROM   SAF.Contrato                 \n" +
                         "WHERE  cto_cve_st_contrat = ?       \n" +
                         "AND    cto_num_contrato             \n" +
                         "IN    (SELECT ate_num_contrato      \n" +
                         "       FROM   SAF.Atencion          \n" +
                         "       WHERE  ate_ejec_atencion = ?)\n" +
                         "ORDER  BY 1";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            pstmt.setInt   (2, usuarioNumero);
            rs         = pstmt.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getString("Nombre"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenListadoContratoNumeroNombreActivo()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoNumeroNombreActivo:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoNumeroNombreActivo:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoNumeroNombreActivo:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return lista;
    }
    public List<String> onContrato_ObtenListadoContratoSub(long contratoNumero, String contratoSubStatus) throws SQLException{
        List<String> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT sct_sub_contrato||'.- '||sct_nom_sub_cto nomSubCto \n" +
                         "FROM   SAF.subCont             \n" +
                         "WHERE  sct_num_contrato   = ?  \n" +
                         "AND    sct_cve_st_subcont = ?  \n" +
                         "ORDER  BY sct_sub_contrato";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong  (1, contratoNumero);
            pstmt.setString(2, contratoSubStatus);
            rs         = pstmt.executeQuery();
            
            while (rs.next()){
                lista.add(rs.getString("nomSubCto"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenListadoContratoSub()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return lista;
    }
    public Map <Integer,String> onContrato_ObtenMapContratoSub(long contratoNumero, String contratoSubStatus) throws SQLException{
        Map <Integer,String> lista = new  LinkedHashMap<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT sct_sub_contrato, sct_sub_contrato||'.- '||sct_nom_sub_cto nomSubCto \n" +
                         "FROM   SAF.subCont             \n" +
                         "WHERE  sct_num_contrato   = ?  \n" +
                         "AND    sct_cve_st_subcont = ?  \n" +
                         "ORDER  BY sct_sub_contrato";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong  (1, contratoNumero);
            pstmt.setString(2, contratoSubStatus);
            rs         = pstmt.executeQuery();
            lista.put(0,"0");
            
            while (rs.next()){
                lista.put(rs.getInt("sct_sub_contrato"),rs.getString("nomSubCto"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContrato_ObtenListadoContratoSub()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContrato_ObtenListadoContratoSub:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return lista;
    }
    public Boolean onContratoInver_VerificaExistencia(long contratoNum, int contratoNumSub, long contratoInver)throws SQLException{
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total  \n" +
                         "FROM   SAF.Continte           \n" +
                         "WHERE  cpr_num_contrato   = ? \n" +
                         "AND    cpr_sub_contrato   = ? \n" +
                         "AND    cpr_contrato_inter = ? \n" +
                         "AND    cpr_cve_st_contint = ? ";
            
            
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setLong  (1, contratoNum);
            pstmt.setInt   (2, contratoNumSub);
            pstmt.setLong  (3, contratoInver);
            pstmt.setString(4, "ACTIVO");
            
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")>0) valorRetorno= Boolean.TRUE; else valorRetorno= Boolean.FALSE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContratoInver_VerificaExistencia()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContratoInver_VerificaExistencia:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContratoInver_VerificaExistencia:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContratoInver_VerificaExistencia:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
return valorRetorno;
    }
    public Boolean onContract_CheckHasSubcontract(long contractNumber) throws SQLException{
        Boolean resultado = Boolean.FALSE;  
        Connection conexion = null;
        CallableStatement cstmt = null;
        try{
            sqlComando = "{CALL DB2FIDUC.SP_VERIFICA_FISO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion   = DataBaseConexion.getInstance().getConnection();
            cstmt      = conexion.prepareCall(sqlComando);

            cstmt.setLong("PI_FISO", contractNumber);
            cstmt.setLong("PI_ACTIVO", 1);

            cstmt.registerOutParameter("PS_NOM_FISO", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_ADMON_PROPIA", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_NIVEL_1", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PI_NIVEL_2", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PI_NIVEL_3", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PI_NIVEL_4", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PI_NIVEL_5", java.sql.Types.NUMERIC);
            cstmt.registerOutParameter("PS_STATUS", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_MANEJO_SUBCTO", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_MON_EXTRANJERA", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PSM_OPEN_CLOSE", java.sql.Types.SMALLINT);
            cstmt.registerOutParameter("PI_IVA_ESPECIAL", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PI_TIPO_NEGOCIO", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PI_CLAS_PRODUCTO", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PI_PRORRATEA_SFISO", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT");
            String subContract = cstmt.getString("PS_MANEJO_SUBCTO");
            cstmt.close();

            if ((mensajeErrorSP== null)||(mensajeErrorSP.equals(new String()))) {
                if ("TRUE".equals(subContract)) {
                    resultado = Boolean.TRUE;
                } else {
                    resultado = Boolean.FALSE;
                }
            } else {
                mensajeError = mensajeErrorSP;
            }
            conexion.close();
        }catch(SQLException Err){
//            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onContract_CheckHasSubcontract()";
            logger.error(Err.getMessage());
	}finally{
            if (cstmt != null) {
                try {
                     cstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContract_CheckHasSubcontract:: Error al cerrar CallableStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onContract_CheckHasSubcontract:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return resultado;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 

/*    private void onCierraConexion(){
        if (rs      != null) rs       = null;
        if (pstmt   != null) pstmt    = null;
        if (cstmt   != null) cstmt    = null;
        if (conexion!= null) conexion = null;
    }*/
}