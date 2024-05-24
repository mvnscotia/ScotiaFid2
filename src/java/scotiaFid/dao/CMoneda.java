/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank.
 * ARCHIVO     : CMoneda.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210320
 * MODIFICADO  : 20210320
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.ContabilidadTipoCambBean;
import scotiaFid.singleton.DataBaseConexion;

public class CMoneda {
 private static final Logger logger = LogManager.getLogger(CMoneda.class);
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;
    private String                               sqlComando;
//    private String                               sqlFiltro;
    private String                               nombreObjeto;
//    private String                               mensajeErrorSP;
    
    private CallableStatement                    cstmt;
    private Connection                           conexion;
    private PreparedStatement                    pstmt;
    private ResultSet                            rs;
    
    private Calendar                             calendario;
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
    public CMoneda() {
        calendario   = Calendar.getInstance();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CMoneda.";
        valorRetorno = Boolean.FALSE;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<String> onMoneda_ObtenListadoMonedas() {
        List<String> lista = new ArrayList<>();
        try{
            sqlComando = "SELECT mon_nom_moneda         \n" + 
                         "FROM   SAF.Monedas            \n" +
                         "WHERE  mon_cve_st_moneda  = ? \n" +
                         "ORDER  BY 1";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            rs         = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(rs.getString("mon_nom_moneda"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMoneda_ObtenListadoMonedas()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        }finally{
            
            onCierraConexion();		
        }
return lista;
    }
    public List<String> onMoneda_ObtenListadoMonedas_Clave() {
        List<String> lista = new ArrayList<>();
        try{
            sqlComando = "SELECT CVE_DESC_CLAVE DESC_MONEDA FROM SAF.CLAVES WHERE CVE_NUM_CLAVE=2";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);

            rs         = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(rs.getString("DESC_MONEDA"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMoneda_ObtenListadoMonedas_clave()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        }finally{
            
            onCierraConexion();		
        }
return lista;
    }

    public List<String> onMoneda_ObtenListadoMonedasSinCierre() {
        List<String> lista = new ArrayList<>();
        try{
            sqlComando = "SELECT mon_nom_moneda                     \n" + 
                         "FROM   SAF.Monedas                        \n" +
                         "WHERE  mon_cve_st_moneda  = ?             \n" +
                         "AND    mon_nom_moneda NOT Like '%CIERRE%' \n" +
                         "ORDER  BY 1";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            rs         = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(rs.getString("mon_nom_moneda"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMoneda_ObtenListadoMonedasSinCierre()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        }finally{
            
            onCierraConexion();		
        }
return lista;
    }
    public Integer onMoneda_ObtenMonedaId(String monedaNombre) {
        int monedaId = 0;
        try{
            sqlComando = "SELECT mon_num_pais       \n" +
                         "FROM   SAF.Monedas        \n" +
                         "WHERE  mon_nom_moneda = ? \n";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, monedaNombre);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                monedaId = rs.getInt("mon_num_pais");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMoneda_ObtenMonedaId()";
            logger.error(mensajeError);
        }finally{
            
            onCierraConexion();
        }
return monedaId;
    }
    public Double onMonedaTC_ObtenTipoCambio(String monedaNombre, int añoTC, int mesTC, int diaTC) {
        Double importeTC = 1.00;
        try{
            sqlComando = "SELECT tic_imp_tipo_camb    \n" +
                         "FROM   SAF.TipoCamb         \n" +
                         "JOIN   SAF.Monedas          \n" +
                         "ON     tic_num_pais = mon_num_pais \n" +
                         "WHERE  mon_nom_moneda   = ? \n" +
                         "AND    tic_ano_alta_reg = ? \n" +
                         "AND    tic_mes_alta_reg = ? \n" +
                         "AND    tic_dia_alta_reg = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, monedaNombre);
            pstmt.setInt   (2, añoTC);
            pstmt.setInt   (3, mesTC);
            pstmt.setInt   (4, diaTC);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                importeTC = rs.getDouble("tic_imp_tipo_camb");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMonedaTC_ObtenTipoCambio()";
            logger.error(mensajeError);
        }finally{
            
            onCierraConexion();
        }
return importeTC;
    }
    public Boolean onMonedaTC_VerificaSiHayTipoDeCambio(String monedaNombre, int añoTC, int mesTC, int diaTC) {
        try{
            sqlComando = "SELECT Nvl(Count(*),0) Total\n" +
                         "FROM   SAF.TipoCamb         \n" +
                         "JOIN   SAF.Monedas          \n" +
                         "ON     tic_num_pais     = mon_num_pais \n" +
                         "WHERE  mon_nom_moneda   = ? \n" +
                         "AND    tic_ano_alta_reg = ? \n" +
                         "AND    tic_mes_alta_reg = ? \n" +
                         "AND    tic_dia_alta_reg = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, monedaNombre);
            pstmt.setInt   (2, añoTC);
            pstmt.setInt   (3, mesTC);
            pstmt.setInt   (4, diaTC);
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                if (rs.getInt("Total")==1) valorRetorno = Boolean.TRUE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción:  Error de SQLException en onMonedaTC_VerificaSiHayTipoDeCambio()";
            logger.error(mensajeError);
        }finally{
            
            onCierraConexion();
        }
        return valorRetorno;
    }
    public Boolean onMonedaTC_AdministraTC(ContabilidadTipoCambBean tc) {
        try{
            calendario.setTime(new java.util.Date(tc.getTipoCambioFecha().getTime()));
            conexion = DataBaseConexion.getInstance().getConnection();
            if (tc.getTipoCambioTipoOperacion().equals("REGISTRO")){
                sqlComando = "INSERT INTO SAF.TipoCamb VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                pstmt      = conexion.prepareStatement(sqlComando);
                pstmt.setInt   ( 1, tc.getTipoCambioMonedaNum());
                pstmt.setInt   ( 2, calendario.get(Calendar.YEAR));
                pstmt.setInt   ( 3, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt   ( 4, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setInt   ( 5, calendario.get(Calendar.HOUR_OF_DAY));
                pstmt.setInt   ( 6, calendario.get(Calendar.MINUTE));
                pstmt.setDouble( 7, tc.getTipoCambioValor());
                pstmt.setInt   ( 8, calendario.get(Calendar.YEAR));
                pstmt.setInt   ( 9, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt   (10, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setString(11, tc.getTipoCambioStatus());
            }
            pstmt.execute();
            conexion.close();
            valorRetorno = Boolean.TRUE;
        }catch(SQLException Err){
            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onMonedaTC_AdministraTC()";
            logger.error(mensajeError);
        }finally{
            
            onCierraConexion();
        }
return valorRetorno;
    }
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
   private void onCierraConexion(){
        if (rs      != null) try{rs.close();} catch(SQLException e){logger.error("Function :: Error al cerrar ResultSet.", e.getMessage());}
        if (pstmt   != null) try{pstmt.close();} catch(SQLException e){logger.error("Function :: Error al cerrar PreparetStatement.", e.getMessage());}
        if (cstmt   != null) try{cstmt.close();} catch(SQLException e){logger.error("Function :: Error al cerrar CallableStatement.", e.getMessage());}
        if (conexion!= null) try{conexion.close();} catch(SQLException e){logger.error("Function :: Error al cerrar Connection.", e.getMessage());}
    }
}