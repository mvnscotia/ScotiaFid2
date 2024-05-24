/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : CUsuario.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210306
 * MODIFICADO  : 20210814
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210814.- Se obtiene la estructura en el metodo onUsuario_ObtenInformacion()
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.UsuarioBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

public class CUsuario {
    private static final Logger logger = LogManager.getLogger(CUsuario.class);
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
    private String                               sqlComando;
   // private String                               sqlFiltro;
    private String                               nombreObjeto;
    //private String                               mensajeErrorSP;
    
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
    public CUsuario() {
        LogsContext.FormatoNormativo();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CUsuario.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public UsuarioBean onUsuario_ObtenInformacion(Integer usuarioNumero) {
        UsuarioBean u         = new UsuarioBean();
        List<String> listaAtn = new ArrayList<>();
        u.setUsuarioExiste(Boolean.FALSE);
        try{
            sqlComando = "SELECT usu_num_usuario, usu_nom_usuario, usu_tipo_usuario, usu_nom_puesto, est_desc_nivel,\n" +
                         "       usu_num_nivel1, usu_num_nivel2, usu_num_nivel3, usu_num_nivel4, usu_num_nivel5,    \n" +
                         "       usu_cve_st_usuario              \n" +
                         "FROM   SAF.Usuarios                    \n" +
                         "JOIN   SAF.Estructu                    \n" +
                         "ON     usu_num_nivel1 = est_num_nivel1 \n" +
                         "AND    usu_num_nivel2 = est_num_nivel2 \n" +
                         "AND    usu_num_nivel3 = est_num_nivel3 \n" +
                         "AND    usu_num_nivel4 = est_num_nivel4 \n" +
                         "AND    usu_num_nivel5 = est_num_nivel5 \n" +
                         "WHERE  usu_num_usuario = ? ";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, usuarioNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                u.setUsuarioId(rs.getInt("usu_num_usuario"));
                u.setUsuarioNombre(rs.getString("usu_nom_usuario"));
                u.setUsuarioPuesto(rs.getString("usu_nom_puesto"));
                u.setUsuarioTipo(rs.getString("usu_tipo_usuario"));
                u.setUsuarioNivel01(rs.getInt("usu_num_nivel1"));
                u.setUsuarioNivel02(rs.getInt("usu_num_nivel2"));
                u.setUsuarioNivel03(rs.getInt("usu_num_nivel3"));
                u.setUsuarioNivel04(rs.getInt("usu_num_nivel4"));
                u.setUsuarioNivel05(rs.getInt("usu_num_nivel5"));
                u.setUsuarioPlaza(rs.getString("est_desc_nivel"));
                u.setUsuarioStatus(rs.getString("usu_cve_st_usuario"));
                u.setUsuarioExiste(Boolean.TRUE);
            }
            rs.close();
            pstmt.close();
            
          //Obtenemos los fideicomisos a los cuales el usuario tiene acceso
            sqlComando = "SELECT CTO_NUM_CONTRATO                \n" +
                         "FROM saf.vista_usuario                 \n" +
                         "WHERE USU_NUM_USUARIO = ?              \n" +
                         "AND CTO_CVE_ST_CONTRAT = ?             \n" +
                         "ORDER  BY 1";
//            sqlComando = "SELECT cto_num_contrato                \n" +
//                         "FROM   SAF.Contrato                    \n" +
//                         "JOIN   SAF.Usuarios                    \n" + 
//                         "ON     cto_num_nivel1 = usu_num_nivel1 \n" +
//                         "AND    cto_num_nivel2 = usu_num_nivel2 \n" +
//                         "AND    cto_num_nivel3 = usu_num_nivel3 \n" +
//                         "AND    cto_num_nivel4 = usu_num_nivel4 \n" +
//                         "WHERE  cto_cve_st_contrat = ?          \n" +
//                         "AND    usu_num_usuario    = ?          \n" +
//                         "ORDER  BY 1";
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt   (1, u.getUsuarioId());
            pstmt.setString(2, "ACTIVO");
            rs         = pstmt.executeQuery();
            
            while (rs.next()){
                listaAtn.add(rs.getString("cto_num_contrato"));
            }
            u.setUsuarioFiltroAtn(listaAtn);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onUsuario_ObtenInformacion()";
        }finally{
            onCierraConexion();
        }return u;
    }
    
    public boolean onUsuario_ExisteUsuario(Integer usuarioNumero) {
        UsuarioBean u         = new UsuarioBean();
        u.setUsuarioExiste(Boolean.FALSE);
        boolean respuesta = false;
        try{
            sqlComando = "SELECT usu_num_usuario, usu_nom_usuario, usu_tipo_usuario, usu_nom_puesto, est_desc_nivel,\n" +
                         "       usu_num_nivel1, usu_num_nivel2, usu_num_nivel3, usu_num_nivel4, usu_num_nivel5,    \n" +
                         "       usu_cve_st_usuario              \n" +
                         "FROM   SAF.Usuarios                    \n" +
                         "JOIN   SAF.Estructu                    \n" +
                         "ON     usu_num_nivel1 = est_num_nivel1 \n" +
                         "AND    usu_num_nivel2 = est_num_nivel2 \n" +
                         "AND    usu_num_nivel3 = est_num_nivel3 \n" +
                         "AND    usu_num_nivel4 = est_num_nivel4 \n" +
                         "AND    usu_num_nivel5 = est_num_nivel5 \n" +
                         "WHERE  usu_num_usuario = ? ";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, usuarioNumero);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                u.setUsuarioId(rs.getInt("usu_num_usuario"));
                u.setUsuarioNombre(rs.getString("usu_nom_usuario"));
                u.setUsuarioPuesto(rs.getString("usu_nom_puesto"));
                u.setUsuarioTipo(rs.getString("usu_tipo_usuario"));
                u.setUsuarioNivel01(rs.getInt("usu_num_nivel1"));
                u.setUsuarioNivel02(rs.getInt("usu_num_nivel2"));
                u.setUsuarioNivel03(rs.getInt("usu_num_nivel3"));
                u.setUsuarioNivel04(rs.getInt("usu_num_nivel4"));
                u.setUsuarioNivel05(rs.getInt("usu_num_nivel5"));
                u.setUsuarioPlaza(rs.getString("est_desc_nivel"));
                u.setUsuarioStatus(rs.getString("usu_cve_st_usuario"));
                u.setUsuarioExiste(Boolean.TRUE);
                respuesta = true;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onUsuario_ObtenInformacion()";
        }finally{
            onCierraConexion();
        }return respuesta;
    }
    
    public boolean onUsuario_UsuarioActivo(Integer usuarioNumero) {
        UsuarioBean u         = new UsuarioBean();
        u.setUsuarioExiste(Boolean.FALSE);
        boolean respuesta = false;
        try{
            sqlComando = "SELECT usu_num_usuario, usu_nom_usuario, usu_tipo_usuario, usu_nom_puesto, est_desc_nivel,\n" +
                         "       usu_num_nivel1, usu_num_nivel2, usu_num_nivel3, usu_num_nivel4, usu_num_nivel5,    \n" +
                         "       usu_cve_st_usuario              \n" +
                         "FROM   SAF.Usuarios                    \n" +
                         "JOIN   SAF.Estructu                    \n" +
                         "ON     usu_num_nivel1 = est_num_nivel1 \n" +
                         "AND    usu_num_nivel2 = est_num_nivel2 \n" +
                         "AND    usu_num_nivel3 = est_num_nivel3 \n" +
                         "AND    usu_num_nivel4 = est_num_nivel4 \n" +
                         "AND    usu_num_nivel5 = est_num_nivel5 \n" +
                         "WHERE  usu_num_usuario = ? AND usu_cve_st_usuario = ? ";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, usuarioNumero);
            pstmt.setString(2, "ACTIVO");
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                u.setUsuarioId(rs.getInt("usu_num_usuario"));
                u.setUsuarioNombre(rs.getString("usu_nom_usuario"));
                u.setUsuarioPuesto(rs.getString("usu_nom_puesto"));
                u.setUsuarioTipo(rs.getString("usu_tipo_usuario"));
                u.setUsuarioNivel01(rs.getInt("usu_num_nivel1"));
                u.setUsuarioNivel02(rs.getInt("usu_num_nivel2"));
                u.setUsuarioNivel03(rs.getInt("usu_num_nivel3"));
                u.setUsuarioNivel04(rs.getInt("usu_num_nivel4"));
                u.setUsuarioNivel05(rs.getInt("usu_num_nivel5"));
                u.setUsuarioPlaza(rs.getString("est_desc_nivel"));
                u.setUsuarioStatus(rs.getString("usu_cve_st_usuario"));
                u.setUsuarioExiste(Boolean.TRUE);
                respuesta = true;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onUsuario_ObtenInformacion()";
        }finally{
            onCierraConexion();
        }return respuesta;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    private void onCierraConexion(){
        if (rs      != null) try{rs.close();} catch(SQLException e){mensajeError+= "Function :: Error al cerrar ResultSet."+ e.getMessage();}
        if (pstmt   != null) try{pstmt.close();} catch(SQLException e){mensajeError+= "Function :: Error al cerrar PreparetStatement." + e.getMessage();}
        if (cstmt   != null) try{cstmt.close();} catch(SQLException e){mensajeError+= "Function :: Error al cerrar CallableStatement." + e.getMessage();}
        if (conexion!= null) try{conexion.close();} catch(SQLException e){mensajeError+= "Function :: Error al cerrar Connection."+ e.getMessage();}
    }
}