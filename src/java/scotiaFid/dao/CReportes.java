/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : CReportes.java
 * TIPO        : Clase
 * PAQUETE     : scotiaFid.dao
 * CREADO      : 20210702
 * MODIFICADO  : 20211019
 * AUTOR       : j.ranfery.delatorre
 * NOTAS       : 20211019.- Se implementa la administración del logo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.ReporteEstBean;
import scotiaFid.bean.ReporteCanSaldosBean;
import scotiaFid.bean.ReporteLogoBean;
import scotiaFid.bean.CriterioBusquedaReporte;
import scotiaFid.singleton.DataBaseConexion;
public class CReportes {
    private static final Logger logger = LogManager.getLogger(CReportes.class);
  //Atributos privados  
    private Boolean                    valorRetorno;
    private Boolean                    continua;
    private String                     nombreObjeto;
    private String                     sqlComando;
    private String                     mensajeErrorSP;
//    private String                     archivoUbicacion;
    private String                     archivoLinea;
    private Boolean                    controlVacio;
    
    private Connection                 conexion;
    private CallableStatement          cstmt;
    private PreparedStatement          pstmt;
    private ResultSet                  rs;
    private ResultSetMetaData          rsmd;
    
    private SimpleDateFormat           formatoFecha;
    private File                       archivo;
    private FileOutputStream           archivoSalida;
    private byte[]                     buffer;
    
  //Atributos privados visibles al exterior
    private String                     archivoNombre;
    private String                     mensajeError;
    
  //Getters y Setters
    public String getArchivoNombre() {
        return archivoNombre;
    }
    public String getMensajeError() {
        return mensajeError;
    }
    public Boolean getControlVacio() {
        return controlVacio;
    }
  //Constructor  
    public CReportes() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: mx.com.bmxt.sofi.dao.CReportes.";
        valorRetorno = Boolean.FALSE;
        continua     = Boolean.TRUE;
        formatoFecha =  new SimpleDateFormat("dd/MM/yyyy");
    }
    public List<ReporteEstBean> onReporte_ConsultaReporte(int contratoNumero, int reporteNumero, int usuarioNumero)throws SQLException{
        List<ReporteEstBean> consulta = new ArrayList<ReporteEstBean>();
        try{
            sqlComando = "SELECT Replace(Replace(Replace(Nvl(edoFinRepCol1,' '),'Ã³','ó'),'Ã­','í'),'Ã©','é') edoFinRepCol1,                        \n" +
                         "                                              Decode(edoFinRepTipoCol1,  Null,' ', edoFinRepTipoCol1) edoFinRepTipoCol1,  \n" +
                         "       Nvl(edoFinRepCol2,' ') edoFinRepCol2 , Decode(edoFinRepTipoCol2,  Null,' ', edoFinRepTipoCol2) edoFinRepTipoCol2,  \n" +
                         "       Nvl(edoFinRepCol3,' ') edoFinRepCol3 , Decode(edoFinRepTipoCol3,  Null,' ', edoFinRepTipoCol3) edoFinRepTipoCol3,  \n" +
                         "       Nvl(edoFinRepCol4,' ') edoFinRepCol4 , Decode(edoFinRepTipoCol4,  Null,' ', edoFinRepTipoCol4) edoFinRepTipoCol4,  \n" +
                         "       Nvl(edoFinRepCol5,' ') edoFinRepCol5 , Decode(edoFinRepTipoCol5,  Null,' ', edoFinRepTipoCol5) edoFinRepTipoCol5,  \n" +
                         "       Nvl(edoFinRepCol6,' ') edoFinRepCol6 , Decode(edoFinRepTipoCol6,  Null,' ', edoFinRepTipoCol6) edoFinRepTipoCol6,  \n" +
                         "       Nvl(edoFinRepCol7,' ') edoFinRepCol7 , Decode(edoFinRepTipoCol7,  Null,' ', edoFinRepTipoCol7) edoFinRepTipoCol7,  \n" +
                         "       Nvl(edoFinRepCol8,' ') edoFinRepCol8 , Decode(edoFinRepTipoCol8,  Null,' ', edoFinRepTipoCol8) edoFinRepTipoCol8,  \n" +
                         "       Nvl(edoFinRepCol9,' ') edoFinRepCol9 , Decode(edoFinRepTipoCol9,  Null,' ', edoFinRepTipoCol9) edoFinRepTipoCol9,  \n" +
                         "       Nvl(edoFinRepCol10,' ')edoFinRepCol10, Decode(edoFinRepTipoCol10, Null,' ', edoFinRepTipoCol10) edoFinRepTipoCol10,\n" +
                         "       edoFinRepFormato    \n" +
                         "FROM   SAF.SOFI_EdoFinRep  \n" +
                         "WHERE  edoFinRepNumUsu = ? \n" +
                         "AND    edoFinRepNumRep = ? \n" +
                         "AND    edoFinRepNumCto = ? \n" + 
                         "ORDER  BY edoFinRepOrden";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, usuarioNumero);
            pstmt.setInt(2, reporteNumero);
            pstmt.setInt(3, contratoNumero);
            
            rs     = pstmt.executeQuery();
            
            while (rs.next()){
                ReporteEstBean elemento = new ReporteEstBean();
                elemento.setReporteEstCol01(rs.getString("edoFinRepCol1").replace(",", "."));
                elemento.setReporteEstTipoCol01(rs.getString("edoFinRepTipoCol1"));
                elemento.setReporteEstCol02(rs.getString("edoFinRepCol2").replace(",", "."));
                elemento.setReporteEstTipoCol02(rs.getString("edoFinRepTipoCol2"));
                elemento.setReporteEstCol03(rs.getString("edoFinRepCol3").replace(",", "."));
                elemento.setReporteEstTipoCol03(rs.getString("edoFinRepTipoCol3"));
                elemento.setReporteEstCol04(rs.getString("edoFinRepCol4").replace(",", "."));
                elemento.setReporteEstTipoCol04(rs.getString("edoFinRepTipoCol4"));
                elemento.setReporteEstCol05(rs.getString("edoFinRepCol5").replace(",", "."));
                elemento.setReporteEstTipoCol05(rs.getString("edoFinRepTipoCol5"));
                elemento.setReporteEstCol06(rs.getString("edoFinRepCol6").replace(",", "."));
                elemento.setReporteEstTipoCol06(rs.getString("edoFinRepTipoCol6"));
                elemento.setReporteEstCol07(rs.getString("edoFinRepCol7").replace(",", "."));
                elemento.setReporteEstTipoCol07(rs.getString("edoFinRepTipoCol7"));
                elemento.setReporteEstCol08(rs.getString("edoFinRepCol8").replace(",", "."));
                elemento.setReporteEstTipoCol08(rs.getString("edoFinRepTipoCol8"));
                elemento.setReporteEstCol09(rs.getString("edoFinRepCol9").replace(",", "."));
                elemento.setReporteEstTipoCol09(rs.getString("edoFinRepTipoCol9"));
                elemento.setReporteEstCol10(rs.getString("edoFinRepCol10").replace(",", "."));
                elemento.setReporteEstTipoCol10(rs.getString("edoFinRepTipoCol10"));
                elemento.setReporteEstFormato(rs.getString("edoFinRepFormato"));
                
                consulta.add(elemento);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException e){
            //mensajeError += "Motivo      : Error genérico. \n" +
            //                "Descripción : " + e.getMessage() + nombreObjeto + "onEdoFinanciero_ConsultaReporte()";
            logger.error(e.getMessage() + "onEdoFinanciero_ConsultaReporte()");
        }finally{
            
            onCierraConexion();
        }
return consulta;
    }
    public List<ReporteCanSaldosBean> onReporte_ConsultaReporteCancelaSaldo(Long contratoNumero) throws SQLException {
        List<ReporteCanSaldosBean> consulta = new ArrayList<ReporteCanSaldosBean>();
        try {
            sqlComando = "SELECT CAN.SALD_AX1, CAN.TIP_MOVTO, CAN.CCON_CTA, CON.CTO_NOM_CONTRATO, CAN.MONE_ID_MONEDA, CAN.SEC_MOVTO, \n"
                       + "       MON.MON_NOM_MONEDA, CAN.CCON_SCTA,CAN.CCON_2SCTA, CAN.CCON_3SCTA, CAN.CCON_4SCTA, CAN.SALD_AX2, \n"
                       + "       CAN.SALD_AX3, CAN.SALD_SALDO_INISIM, CAN.SALD_SALDO_ACTUAL, CAN.DETM_FOLIO_OP,CAN.ASIE_SEC_ASIENTO, \n"
                       + "       CASE WHEN CAN.ASIE_CAR_ABO IS NULL THEN ' ' ELSE CAN.ASIE_CAR_ABO  END ASIE_CAR_ABO, \n"
                       + "       CAN.ASIE_IMPORTE, CAN.SALD_FEC_APLIC, CAN.SALD_ESTATUS   \n"
                       + "FROM CANCELA_SALDH CAN INNER JOIN MONEDAS MON ON CAN.MONE_ID_MONEDA = MON.MON_NUM_PAIS   \n"
                       + "INNER JOIN CONTRATO CON ON CAN.SALD_AX1=CON.CTO_NUM_CONTRATO   \n"
                       + "WHERE CAN.SALD_AX1= ? AND CAN.SALD_ESTATUS='APLICADO'   \n"
                       // + "AND DETM_FOLIO_OP > 0 \n"
                       + "ORDER BY CAN.MONE_ID_MONEDA, CAN.CCON_CTA, CAN.CCON_SCTA, CAN.CCON_2SCTA, CAN.CCON_3SCTA, \n"
                       + "CAN.CCON_4SCTA, CAN.SALD_AX2,  CAN.SALD_AX3";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ReporteCanSaldosBean elemento = new ReporteCanSaldosBean();
                elemento.setSaldAx1(rs.getLong("SALD_AX1"));
                elemento.setTipMovto(rs.getString("TIP_MOVTO"));
                elemento.setCconCta(rs.getInt("CCON_CTA"));
                elemento.setCtoNomContrato(rs.getString("CTO_NOM_CONTRATO"));
                elemento.setMoneIdMoneda(rs.getInt("MONE_ID_MONEDA"));
                elemento.setSecMovto(rs.getInt("SEC_MOVTO"));
                elemento.setCconsCta(rs.getInt("CCON_SCTA"));
                elemento.setCcons2Cta(rs.getInt("CCON_2SCTA"));
                elemento.setCcons3Cta(rs.getInt("CCON_3SCTA"));
                elemento.setCcons4Cta(rs.getInt("CCON_4SCTA"));
                elemento.setSaldAx2(rs.getInt("SALD_AX2"));
                elemento.setSaldAx3(rs.getInt("SALD_AX3"));
                elemento.setSaldSaldoIniSim(rs.getDouble("SALD_SALDO_INISIM"));
                elemento.setSaldSaldoActual(rs.getDouble("SALD_SALDO_ACTUAL"));
                elemento.setDetmFolioOp(rs.getLong("DETM_FOLIO_OP"));
                elemento.setAsieSecAsiento(rs.getInt("ASIE_SEC_ASIENTO"));
                elemento.setAsieCarAbo(rs.getString("ASIE_CAR_ABO"));
                if (elemento.getAsieCarAbo() == null) {
                    elemento.setAsieCarAbo("");
                } 
                elemento.setAsiImporte(rs.getDouble("ASIE_IMPORTE"));
                elemento.setSaldFecAplica(rs.getDate("SALD_FEC_APLIC"));
                elemento.setSaldEstatus(rs.getString("SALD_ESTATUS"));
                elemento.setMonNomMoneda(rs.getString("MON_NOM_MONEDA"));
                
                consulta.add(elemento);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException e) {
            //mensajeError += "Motivo      : Error genérico. \n" +
            //                "Descripción : " + e.getMessage() + nombreObjeto + "onEdoFinanciero_ConsultaReporte()";
            logger.error(e.getMessage() + "onReporte_ConsultaReporteCancelaSaldo()");
        } finally {

            onCierraConexion();
        }
        return consulta;
    }
    public List<String> onReporte_CargaListaReportes()throws SQLException{
        List<String> lista = new ArrayList<String>();
        try{
                sqlComando = "SELECT edoFinCatNumRep||'.- '||edoFinCatNomRep Reporte FROM SAF.SOFI_EdoFinCat ORDER BY edoFinCatNumRep";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            rs         = pstmt.executeQuery();
            
            while (rs.next()){
                lista.add(rs.getString("Reporte"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException e){
            //mensajeError += "Motivo      : Error genérico. \n" +
            //                "Descripción : " + e.getMessage() + nombreObjeto + "onReporte_CargaListaReportes()";
            logger.error(e.getMessage() +  "onReporte_CargaListaReportes()");
        }finally{
            
            onCierraConexion();
        }
return lista;
    }
    public String onReporte_ObtenEstructuraColumnas(int reporteNumero)throws SQLException{
        String matrizAnchoCol = new String();
        try{
            sqlComando = "SELECT edoFinCatFirma1 FROM SAF.SOFI_EdoFinCat WHERE edoFinCatNumRep = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, reporteNumero);
            rs     = pstmt.executeQuery();
            
            if (rs.next()){
                matrizAnchoCol = rs.getString("edoFinCatFirma1");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException e){
            //mensajeError += "Motivo      : Error genérico. \n" +
            //                "Descripción : " + e.getMessage() + nombreObjeto + "onReporte_ObtenEstructuraColumnas()";
            logger.error(mensajeError + "onReporte_ObtenEstructuraColumnas()");
            
        }finally{
            
            onCierraConexion();
        }
return matrizAnchoCol;
    }
    public Boolean onReporte_GeneraInformacion(CriterioBusquedaReporte reporte) throws SQLException{
        try{
            Integer paso = -1;
            sqlComando = "{CALL DB2FIDUC.SP_GEN_EDOFINREP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            conexion   = DataBaseConexion.getInstance().getConnection();
            cstmt      = conexion.prepareCall(sqlComando); 
            cstmt.setInt   ("PI_USUARIO"      , reporte.getCriterioBitUsuarioNum());
            cstmt.setInt   ("PI_NUM_REPORTE"  , reporte.getCriterioRepReporteNum());
            if (reporte.getCriterioRepContratoNum() == null) {
                cstmt.setInt("PI_NUM_CTO",paso);
            } else {
                cstmt.setInt("PI_NUM_CTO", reporte.getCriterioRepContratoNum());
            }
            if (reporte.getCriterioRepContratoNumSub() == null) {
                cstmt.setInt("PI_NUM_SUBCTO", paso);
            } else {
                cstmt.setInt("PI_NUM_SUBCTO", reporte.getCriterioRepContratoNumSub());
            }
            cstmt.setInt   ("PI_MDA"          , reporte.getCriterioRepMonedaNum());
            cstmt.setInt   ("PI_ANYO"         , reporte.getCriterioRepAño());
            cstmt.setInt   ("PI_MES"          , reporte.getCriterioRepMes());
            cstmt.setDate  ("PDT_FECINI"      , reporte.getCriterioRepFechaDel());
            cstmt.setDate  ("PDT_FECFIN"      , reporte.getCriterioRepFechaAl()); 
            cstmt.setInt   ("PI_CTAM"         , reporte.getCriterioRepCTAM());
            cstmt.setInt   ("PI_SCTA1"        , reporte.getCriterioRepSC1());
            cstmt.setInt   ("PI_SCTA2"        , reporte.getCriterioRepSC2());
            cstmt.setInt   ("PI_SCTA3"        , reporte.getCriterioRepSC3());
            cstmt.setInt   ("PI_SCTA4"        , reporte.getCriterioRepSC4());
            if (reporte.getCriterioRepCtoInv() == null) {
                
                cstmt.setInt("PI_AUX3", paso);
            } else {
                cstmt.setLong("PI_AUX3", reporte.getCriterioRepCtoInv());
            }
            
            cstmt.setString("PS_TERMINAL"     , reporte.getCriterioBitTerminal());
            cstmt.setString("PS_PANTALLA"     , reporte.getCriterioBitPantalla());
            
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT"  , java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT"   , java.sql.Types.VARCHAR);
            
            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            Integer Sqlstate_out = cstmt.getInt("PCH_SQLSTATE_OUT");
            cstmt.close();
            conexion.close();
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                if (Sqlstate_out == 40001) {
                    mensajeError = "Reporte en proceso por otro usuario, por favor intente más tarde";
                } else {
                    mensajeError = mensajeErrorSP;
                }
                valorRetorno = Boolean.FALSE;
            }
            
        }catch(SQLException Err){
            //mensajeError+= "Descripción:" + Err.getMessage() + nombreObjeto + "onReporte_GeneraInformacion()";
            logger.error(Err.getMessage() + "onReporte_GeneraInformacion()");
        }finally{
            
            onCierraConexion();
        }
return valorRetorno;
    }
//    public Boolean onReporte_LogoAdministra(ReporteLogoBean rptLogo, int archivoTamaño)throws SQLException{
//        try{
//            conexion = DataBaseConexion.getInstance().getConnection();
//            if (rptLogo.getReporteLogoBitTipoOperacion().equals("REGISTRO")){
//                sqlComando = "SELECT Nvl(Max(log_id_banco),0) + 1 MaxLogo FROM SAF.Logo";
//                pstmt      = conexion.prepareStatement(sqlComando);
//                rs         = pstmt.executeQuery();
//                if (rs.next()){
//                    rptLogo.setReporteLogoId(rs.getShort("MaxLogo"));
//                }
//                rs.close();
//                pstmt.close();
//                
//                sqlComando = "INSERT INTO SAF.Logo VALUES (?,?,?,?)";
//                pstmt      = conexion.prepareStatement(sqlComando);
//                pstmt.setShort       (1, rptLogo.getReporteLogoId());
//                pstmt.setString      (2, rptLogo.getReporteLogoBanco());
//                pstmt.setBinaryStream(3, rptLogo.getReporteLogoImagenArchivo(), archivoTamaño);
//                pstmt.setString      (4, rptLogo.getReporteLogoDireccion());
//                
//                pstmt.execute();
//            }
//            if (rptLogo.getReporteLogoBitTipoOperacion().equals("ACTUALIZAR")){
//                
//            }
//            pstmt.close();
//            conexion.close();
//            valorRetorno = Boolean.TRUE;
//        }catch(SQLException Err){
//            //mensajeError+= "Descripción:" + Err.getMessage() + nombreObjeto + "onReporte_LogoAdministra()";
//            logger.error(Err.getMessage() + "onReporte_LogoAdministra()");
//        }finally{
//            
//            onCierraConexion();
//        }
//return valorRetorno;
//    }
    public void onReporte_LogoObten(ReporteLogoBean rptLogo)throws SQLException{
        try{
            sqlComando = "SELECT log_logo1, log_direccion FROM SAF.Logo WHERE log_id_banco = ?";
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            pstmt.setShort(1, rptLogo.getReporteLogoId());
            rs         = pstmt.executeQuery();
            
            if (rs.next()){
                rptLogo.setReporteLogoImagen(rs.getBlob("LOG_LOGO1"));
                //System.out.println("Tamaño desde el recordset " + rs.getBlob("LOG_LOGO1").length());
                //System.out.println("Tamaño desde el objeto    " + rptLogo.getReporteLogoImagen().length());
                rptLogo.setReporteLogoImagenTamaño((int)rptLogo.getReporteLogoImagen().length());
                rptLogo.setReporteLogoDireccion(rs.getString("log_direccion"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }catch(SQLException Err){
            mensajeError+= "Descripción:" + Err.getMessage() + nombreObjeto + "onReporte_LogoObten()";
            logger.error(Err.getMessage() + "onReporte_LogoObten()");
        } finally {
            onCierraConexion();
        }
    }
        public Boolean getReportByColumns(CriterioBusquedaReporte report, String controlNombre) throws SQLException, FileNotFoundException, IOException, ParseException {
        controlVacio = Boolean.TRUE;
        archivoLinea = new String();
        controlVacio = Boolean.TRUE;
        int totalReg = 0;

        try {
            sqlComando = "SELECT EDOFINREPCOL1, EDOFINREPCOL2, EDOFINREPCOL3, EDOFINREPCOL4, EDOFINREPCOL5, \n" +
                         "EDOFINREPCOL6, EDOFINREPCOL7, EDOFINREPCOL8, EDOFINREPCOL9, EDOFINREPCOL10 \n" +
                         "FROM SAF.SOFI_EdoFinRep2 \n" +
                         "WHERE EDOFINREPNUMUSU = ? \n" +
                         "AND EDOFINREPNUMREP = ? \n" ;
            if (report.getCriterioRepContratoNum() != null) {        
            sqlComando = sqlComando + "AND EDOFINREPNUMCTO = ? \n";
                                 }                     
            sqlComando = sqlComando + "ORDER  BY edoFinRepOrden";
            conexion = DataBaseConexion.getInstance().getConnection();

            pstmt = conexion.prepareStatement(sqlComando, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setInt(1, report.getCriterioBitUsuarioNum());
            pstmt.setInt(2, report.getCriterioRepReporteNum());
            if (report.getCriterioRepContratoNum() != null) {
                pstmt.setInt(3, report.getCriterioRepContratoNum());
            }
            rs = pstmt.executeQuery();
            rs.last();
            totalReg = rs.getRow();
            rs.beforeFirst();

            if (totalReg>0) {
                rsmd = rs.getMetaData();
                controlVacio  = Boolean.FALSE;

                //Preparamos el archivo 
                archivo = new File(controlNombre);
                archivoSalida = new FileOutputStream(archivo);
                int dateColum = -1;
                int amountColum = -1;

                //Generamos los titulos de las columnas 
/*                for (int itemTitulo=1; itemTitulo<= rsmd.getColumnCount(); itemTitulo++) {
                    if (rsmd.getColumnLabel(itemTitulo).trim().toLowerCase(Locale.ENGLISH).equals("fecha")) {
                        dateColum = itemTitulo;
                    }
                    if (rsmd.getColumnLabel(itemTitulo).trim().toLowerCase(Locale.ENGLISH).equals("importe")) {
                        amountColum = itemTitulo;
                    }
                    archivoLinea += rsmd.getColumnLabel(itemTitulo).concat("\t");
                }*/
                //archivoLinea = archivoLinea.concat("\n\n");
                buffer = archivoLinea.getBytes();
                archivoSalida.write(buffer);

                while (rs.next()){
                    archivoLinea = new String();
                    for (int itemCol=1; itemCol<= rsmd.getColumnCount(); itemCol++){
                        if (itemCol==dateColum) {
                            archivoLinea += dateFormat(rs.getString(itemCol)) + "\t";
                        } else if (itemCol==amountColum) {
                            archivoLinea += "$ ".concat(rs.getString(itemCol)).concat("\t");
                        } else {
                            archivoLinea += rs.getString(itemCol) + "\t";
                        }
                    }
                    archivoLinea = archivoLinea + "\n";
                    buffer       = archivoLinea.getBytes();
                    archivoSalida.write(buffer);
                }
                archivoSalida.close();
                valorRetorno = Boolean.TRUE;
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch(SQLException Err) {
            //mensajeError+= "Descripción:" + Err.getMessage() + nombreObjeto + "getReportByColumns()";
            logger.error(Err.getMessage() + "getReportByColumns()");
        } finally {
            
            onCierraConexion();
        }
        return valorRetorno;
    } 
  //Funciones privadas
    private void onCierraConexion(){
        if (rs      != null) try{rs.close();} catch(SQLException e){logger.error("Function :: Error al cerrar ResultSet.", e.getMessage());}
        if (pstmt   != null) try{pstmt.close();} catch(SQLException e){logger.error("Function :: Error al cerrar PreparetStatement.", e.getMessage());}
        if (cstmt   != null) try{cstmt.close();} catch(SQLException e){logger.error("Function :: Error al cerrar CallableStatement.", e.getMessage());}
        if (conexion!= null) try{conexion.close();} catch(SQLException e){logger.error("Function :: Error al cerrar Connection.", e.getMessage());}
    }

    public static String dateFormat(String str) throws ParseException{ 
        DateFormat dfInput = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfOutput = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dfInput.parse(str);
        String stringDate = dfOutput.format(date);
  
        return stringDate;
    }
}