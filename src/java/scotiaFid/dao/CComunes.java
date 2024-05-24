/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank.
 * ARCHIVO     : CComunes.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210314
 * MODIFICADO  : 20210920
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 20210920.- Se agrega metodo onComunes_ObtenUltimoDiaMesDesdeControla();
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
//import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.DestinoRecepcionBean;
import scotiaFid.bean.FechaBean;
import scotiaFid.bean.InformacionCuentasMonetariasOperacion;
import scotiaFid.bean.MonedasBean;
import scotiaFid.bean.NombreBean;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import scotiaFid.bean.GeneralList;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

//import scotiaFid.util.CConexion;

public class CComunes {
    private static final Logger logger = LogManager.getLogger(CComunes.class);
    private static final Integer ID_UNO = 1;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private byte[] buffer;
    private Boolean valorRetorno;
    private String archivoLinea;
    private String sqlComando;
//    private String sqlFiltro;
    private String nombreObjeto;
//    private String mensajeErrorSP;

    private File archivo;
    private FileOutputStream archivoSalida;

//    private CallableStatement cstmt;
//    private Connection conexion;
//    private PreparedStatement pstmt;
//    private ResultSet rs;
    private Integer sqlParam;
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
    public CComunes() {
        LogsContext.FormatoNormativo();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CComunes.";
        valorRetorno = Boolean.FALSE;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean onComunes_ExportaInformacionDesdeDataTable(javax.faces.model.ListDataModel ldm, String archivoNombre, String nombreClase, String arrTitulos[]) throws IOException {
        //CAVC
        //Class cls = null;
        //Field[] lst = null;
        //Object obj = null;
        try {
            ldm.getWrappedData();
            //CAVC
            //cls = Class.forName(nombreClase);
            //lst = cls.getDeclaredFields();
            
            //Preparamos el archivo  
            archivo = new File(archivoNombre);
            archivoSalida = new FileOutputStream(archivo);
            //Generamos y escribimos los titluos de las columnas
            archivoLinea = new String();
            for (int itemTitulo = 0; itemTitulo <= arrTitulos.length - 1; itemTitulo++) {
                if (!arrTitulos[itemTitulo].equals(new String())) {
                    archivoLinea += arrTitulos[itemTitulo].substring(0, arrTitulos[itemTitulo].indexOf("»")).trim().concat("\t");
                }
            }
            archivoLinea = archivoLinea.concat("\n");
            buffer = archivoLinea.getBytes();
            archivoSalida.write(buffer);
            
            //CAVC
            /*for (Iterator it = ldm.iterator(); it.hasNext();) {
                obj = it.next();
             //   System.out.println("Hola objeto");
            }*/
            
            archivoSalida.close();
            valorRetorno = Boolean.TRUE;
        } catch (IOException | SecurityException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ExportaInformacionDesdeDataTable()";
            logger.error(mensajeError);
        } finally {
            if(archivoSalida != null) {
                archivoSalida.close();
            }
        //    onCierraConexion();
        }
        return valorRetorno;
    }

    public Map<String, String> onComunes_ObtenListadoNotarios(String nombreCampo, String nombreTabla, String filtro) throws SQLException {
        Map<String, String> lista = new  LinkedHashMap<String, String>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT ".concat(nombreCampo).concat(" AS Campo FROM SAF.").concat(nombreTabla).concat(" ").concat(filtro).concat(" ORDER BY 1");
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String consultaNoratio = rs.getString("Campo");
                String[] extraeNotario = consultaNoratio.split("»");
                lista.put(extraeNotario[0], consultaNoratio);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ObtenListadoContenidoCampo()";
            logger.error(mensajeError);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar Connection.", e.getMessage());
                }
            }
        }
        return lista;
    }

    public List<String> onComunes_ObtenListadoContenidoCampo(String nombreCampo, String nombreTabla, String filtro) throws SQLException {
        List<String> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
      try {
            sqlComando = "SELECT ".concat(nombreCampo).concat(" AS Campo FROM SAF.").concat(nombreTabla).concat(" ").concat(filtro).concat(" ORDER BY 1");
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("Campo"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ObtenListadoContenidoCampo()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return lista;
    }
    public List<GeneralList> onComunes_Plazas() throws SQLException {
        List<GeneralList> listSalida = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
      try { 
            sqlComando = "SELECT EST_NUM_NIVEL4, EST_DESC_NIVEL FROM Estructu WHERE EST_NUM_NIVEL4 > 0 AND EST_NUM_NIVEL5 = 0 ORDER BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GeneralList cat = new GeneralList();
                cat.setClave(rs.getString("EST_DESC_NIVEL")); 
                cat.setDescripcion(rs.getString("EST_DESC_NIVEL"));
                listSalida.add(cat);
            } 
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_Plazas()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoContenidoCampo:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listSalida;
    }

    public List<String> onComunes_ObtenListadoParticipantePorRolFid(Long contratoNumero, String rolFiduciario) throws SQLException {
        List<String> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if ((rolFiduciario.equals("BENEFICIARIO")) || (rolFiduciario.equals("FIDEICOMISARIO"))) {
                sqlComando = "SELECT ben_nom_benef||' » '||ben_beneficiario Campo FROM SAF.Benefici WHERE ben_num_contrato = ?  AND ben_cve_st_benefic = ? ORDER BY 1";
            }
            if (rolFiduciario.equals("FIDEICOMITENTE")) {
                sqlComando = "SELECT fid_nom_fideicom||' » '||fid_fideicomitente Campo FROM SAF.Fideicom WHERE fid_num_contrato = ? AND fid_cve_st_fideico = ? ORDER BY 1";
            }
            if (rolFiduciario.equals("TERCERO")) {
                sqlComando = "SELECT ter_nom_tercero||' » '||ter_num_tercero Campo FROM SAF.Terceros WHERE ter_num_contrato = ? AND  ter_cve_st_tercero = ? ORDER BY 1";
            }
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, contratoNumero);
            pstmt.setString(2, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("Campo"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ObtenListadoParticipantePorRolFid()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoParticipantePorRolFid:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoParticipantePorRolFid:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenListadoParticipantePorRolFid:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return lista;
    }

    public Short onComunes_ObtenUltimoDiaMesDesdeControla(Short paramAño, Short paramMes) throws SQLException {
        short dia = 0;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT ctl_dia_control FROM SAF.Controla WHERE ctl_ano_control = ? AND ctl_mes_control = ? ORDER BY 1 DESC";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setShort(1, paramAño);
            pstmt.setShort(2, paramMes);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dia = rs.getShort("ctl_dia_control");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ObtenUltimoDiaMesDesdeControla()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenUltimoDiaMesDesdeControla:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenUltimoDiaMesDesdeControla:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenUltimoDiaMesDesdeControla:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return dia;
    }

    public FechaBean getFechaRecepcionSistema() {
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FechaBean fechaBean = null;
        try {
            sqlComando = "SELECT FCO_DIA_DIA,FCO_MES_DIA,FCO_ANO_DIA from SAF.FECCONT";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                fechaBean = new FechaBean();
                fechaBean.setFechaActDia(rs.getInt("FCO_DIA_DIA"));
                fechaBean.setFechaActMes(rs.getInt("FCO_MES_DIA"));
                fechaBean.setFechaActAño(rs.getInt("FCO_ANO_DIA"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getFechaRecepcionSistema:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getFechaRecepcionSistema:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getFechaRecepcionSistema:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return fechaBean;
    }

    public List<DestinoRecepcionBean> getClavesPorNumClave(String numClave) {
        List<DestinoRecepcionBean> listDestinoRecepcionBeans = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES WHERE CVE_NUM_CLAVE = ? \n " +
                         "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando); 
            pstmt.setString(1, numClave);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DestinoRecepcionBean destinoRecepcionBean = new DestinoRecepcionBean();
                destinoRecepcionBean.setClaveNumero(rs.getInt("CVE_NUM_SEC_CLAVE"));
                destinoRecepcionBean.setClaveDescripcion(rs.getString("CVE_DESC_CLAVE"));
                listDestinoRecepcionBeans.add(destinoRecepcionBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listDestinoRecepcionBeans;
    }

        public List<DestinoRecepcionBean> getClavesConceptos(String numClave) {
        List<DestinoRecepcionBean> listDestinoRecepcionBeans = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES WHERE CVE_NUM_CLAVE = ?  AND\n" +
                         "(cve_desc_clave NOT LIKE '%C/FACTURA%' and cve_desc_clave NOT LIKE '%HONORARIOS%')  \n " +
                         "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando); 
            pstmt.setString(1, numClave);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DestinoRecepcionBean destinoRecepcionBean = new DestinoRecepcionBean();
                destinoRecepcionBean.setClaveNumero(rs.getInt("CVE_NUM_SEC_CLAVE"));
                destinoRecepcionBean.setClaveDescripcion(rs.getString("CVE_DESC_CLAVE"));
                listDestinoRecepcionBeans.add(destinoRecepcionBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesConceptos:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesConceptos:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesConceptos:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listDestinoRecepcionBeans;
    }
    public List<DestinoRecepcionBean> getClavesconceptosLiqOtrosBancos(String numClave) {
        List<DestinoRecepcionBean> listDestinoRecepcionBeans = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES WHERE CVE_NUM_CLAVE = ? \n " +
                         "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando); 
            pstmt.setString(1, numClave);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt("CVE_NUM_SEC_CLAVE") != 4 && rs.getInt("CVE_NUM_SEC_CLAVE") != 6) {
                    DestinoRecepcionBean destinoRecepcionBean = new DestinoRecepcionBean();
                    destinoRecepcionBean.setClaveNumero(rs.getInt("CVE_NUM_SEC_CLAVE"));
                    destinoRecepcionBean.setClaveDescripcion(rs.getString("CVE_DESC_CLAVE"));
                    listDestinoRecepcionBeans.add(destinoRecepcionBean);
                }
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listDestinoRecepcionBeans;
    }
    public List<DestinoRecepcionBean> getClavesconceptosLiq(String numClave) {
        List<DestinoRecepcionBean> listDestinoRecepcionBeans = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES WHERE CVE_NUM_CLAVE = ? \n " +
                         "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando); 
            pstmt.setString(1, numClave);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt("CVE_NUM_SEC_CLAVE") != 6) {
                    DestinoRecepcionBean destinoRecepcionBean = new DestinoRecepcionBean();
                    destinoRecepcionBean.setClaveNumero(rs.getInt("CVE_NUM_SEC_CLAVE"));
                    destinoRecepcionBean.setClaveDescripcion(rs.getString("CVE_DESC_CLAVE"));
                    listDestinoRecepcionBeans.add(destinoRecepcionBean);
                }
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClave:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listDestinoRecepcionBeans;
    }

    public double onComunes_ObtenIva(String fideicomiso) {
        double iva = 0;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "select IND_VALOR_INDICE from SAF.INDICES inner join SAF.CONTRATO on CTO_NUM_CONTRATO = ? AND IND_CVE_INDICE  = case when CTO_CVE_EXCLU_30 = 0 then 'IVA' ELSE 'IVA FRONTERIZO' END";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, Long.parseLong(fideicomiso));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                iva = rs.getDouble("IND_VALOR_INDICE");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onComunes_ObtenIva()";
            logger.error(mensajeError);
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenIva:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenIva:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onComunes_ObtenIva:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return iva;
    }

    public List<InformacionCuentasMonetariasOperacion> getInformacionCuentas(String fideicomiso, String Subfideicomiso) {
        List<InformacionCuentasMonetariasOperacion> listInformacionCuentas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "Select CVE_DESC_CLAVE BANCO,ADC_PLAZA PLAZA,ADC_CUENTA CUENTA,ADC_CLABE CLABE,ADC_SUCURSAL SUCURSAL,COALESCE(SALD_SALDO_ACTUAL,0) SALDO,MON_NOM_MONEDA MONEDA,ADC_NUM_MONEDA "
                    + "From FID_CTAS INNER JOIN MONEDAS On MON_NUM_PAIS = ADC_NUM_MONEDA "
                    + "INNER JOIN CLAVES On  CVE_NUM_CLAVE = 27 AND CVE_NUM_SEC_CLAVE= ADC_BANCO "
                    + "LEFT OUTER JOIN FDSALDOS On  SALD_AX1 = ADC_NUM_CONTRATO AND SALD_AX2 = ADC_SUB_CONTRATO AND SALD_AX3 = decimal(ADC_CUENTA) AND  CCON_CTA = 1103 And CCON_SCTA = 1 And CCON_2SCTA = 2 And CCON_3SCTA = 1 "
                    + "WHERE ADC_NUM_CONTRATO= ? AND ADC_SUB_CONTRATO = ? AND ADC_ESTATUS='ACTIVO' AND ADC_BANCO !=44";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, Integer.parseInt(fideicomiso));
            pstmt.setInt(2, Integer.parseInt(Subfideicomiso));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                InformacionCuentasMonetariasOperacion informacionCuenta = new InformacionCuentasMonetariasOperacion();
                informacionCuenta.setBanco(rs.getString("BANCO"));
                informacionCuenta.setPlaza(rs.getInt("PLAZA"));
                informacionCuenta.setCuenta(rs.getString("CUENTA"));
                informacionCuenta.setClabe(rs.getString("CLABE"));
                informacionCuenta.setSucursal(rs.getInt("SUCURSAL"));
                informacionCuenta.setSaldo(rs.getDouble("SALDO"));
                informacionCuenta.setMoneda(rs.getString("MONEDA"));
                informacionCuenta.setAdcNumMoneda(rs.getInt("ADC_NUM_MONEDA"));
                listInformacionCuentas.add(informacionCuenta);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getInformacionCuentas:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getInformacionCuentas:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getInformacionCuentas:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listInformacionCuentas;
    }
    
    public List<InformacionCuentasMonetariasOperacion> getDetalleDeCuentasScotaibank(String fideicomiso, String Subfideicomiso) {
        List<InformacionCuentasMonetariasOperacion> listInformacionCuentas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlParam   = 0;
            sqlComando = "SELECT ADC_CUENTA AS CUENTA       \n" +
                         "	,ADC_BANCO                  \n" +
                         "	,ADC_PLAZA AS PLAZA         \n" +
                         "	,ADC_SUCURSAL AS SUCURSAL   \n" +
                         "	,ADC_NUM_MONEDA             \n" +
                         "	,CVE_DESC_CLAVE AS BANCO    \n" +
                         "	,MON_NOM_MONEDA AS MONEDA   \n" +
                         "	,ADC_SECUENCIAL             \n" +
                         "  FROM FID_CTAS                   \n" +
                         " INNER JOIN CLAVES ON CVE_NUM_CLAVE=27 AND CVE_NUM_SEC_CLAVE= ADC_BANCO \n" +
                         " INNER JOIN MONEDAS ON MON_NUM_PAIS=ADC_NUM_MONEDA \n" +
                         " WHERE ADC_NUM_CONTRATO= ?        \n" +
                         "   AND ADC_SUB_CONTRATO= ?        \n" +
                         "   AND ADC_ESTATUS= 'ACTIVO'      \n" +
                         "   AND ADC_BANCO=44";
            
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            
            sqlParam++;
            pstmt.setObject(sqlParam, fideicomiso   , Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, Subfideicomiso, Types.VARCHAR);            

            rs = pstmt.executeQuery();

            while (rs.next()) {
                InformacionCuentasMonetariasOperacion informacionCuenta = new InformacionCuentasMonetariasOperacion();
                informacionCuenta.setCuenta(rs.getString("CUENTA"));
                informacionCuenta.setCuentaDes(rs.getString("ADC_SECUENCIAL").concat(".-").concat(rs.getString("CUENTA").concat("/").concat(rs.getString("BANCO"))));
                informacionCuenta.setAdcBanco(rs.getInt("ADC_BANCO"));
                informacionCuenta.setPlaza(rs.getInt("PLAZA"));
                informacionCuenta.setSucursal(rs.getInt("SUCURSAL"));
                informacionCuenta.setAdcNumMoneda(rs.getInt("ADC_NUM_MONEDA"));
                informacionCuenta.setBanco(rs.getString("BANCO"));
                informacionCuenta.setMoneda(rs.getString("MONEDA"));
             
                listInformacionCuentas.add(informacionCuenta);
             
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getDetalleDeCuentasScotaibank:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getDetalleDeCuentasScotaibank:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getDetalleDeCuentasScotaibank:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listInformacionCuentas;
    }    

    public List<NombreBean> obtenParticipantePorRolFid(String fideicomiso, String tipoPersona) {
        List<NombreBean> listNombreBean = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (tipoPersona.equals("FIDEICOMISARIO")) {
                sqlComando = "SELECT rtrim(ltrim(replace(BEN_NOM_BENEF,chr(9),' '))) as BEN_NOM_BENEF , BEN_BENEFICIARIO FROM BENEFICI WHERE BEN_NUM_CONTRATO = ? AND BEN_CVE_ST_BENEFIC = 'ACTIVO'";
            }
            if (tipoPersona.equals("TERCERO")) {
                sqlComando = "SELECT rtrim(ltrim(replace(TER_NOM_TERCERO,chr(9),' '))) as TER_NOM_TERCERO, TER_NUM_TERCERO FROM TERCEROS WHERE TER_NUM_CONTRATO = ? And TER_CVE_ST_TERCERO = 'ACTIVO'";
            }
            if (tipoPersona.equals("FIDEICOMITENTE")) {
                sqlComando = "SELECT rtrim(ltrim(replace(FID_NOM_FIDEICOM,chr(9),' '))) as FID_NOM_FIDEICOM, FID_FIDEICOMITENTE FROM FIDEICOM WHERE FID_NUM_CONTRATO = ? And FID_CVE_ST_FIDEICO  = 'ACTIVO'";
            }
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, Long.parseLong(fideicomiso));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                NombreBean nombreBean = new NombreBean();
                if (tipoPersona.equals("FIDEICOMISARIO")) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("BEN_BENEFICIARIO"));
                    nombreBean.setBeneficiarioNombre(rs.getString("BEN_BENEFICIARIO").concat(".-").concat(rs.getString("BEN_NOM_BENEF")));
                }
                if (tipoPersona.equals("TERCERO")) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("TER_NUM_TERCERO"));
                    nombreBean.setBeneficiarioNombre(rs.getString("TER_NUM_TERCERO").concat(".-").concat(rs.getString("TER_NOM_TERCERO")));
                }

                if (tipoPersona.equals("FIDEICOMITENTE")) {
                    nombreBean.setBeneficiarioNumero(rs.getInt("FID_FIDEICOMITENTE"));
                    nombreBean.setBeneficiarioNombre(rs.getString("FID_FIDEICOMITENTE").concat(".-").concat(rs.getString("FID_NOM_FIDEICOM")));
                                                                  
                }

                listNombreBean.add(nombreBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::obtenParticipantePorRolFid:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::obtenParticipantePorRolFid:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::obtenParticipantePorRolFid:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listNombreBean;
    }
    public List<String> getPantallasPorNumClaveByID(String numClave) {
        List<String> listPantallas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT CVE_FORMA_EMP_CVE FROM SAF.CLAVES WHERE CVE_NUM_CLAVE = 3001 AND CVE_LIMINF_CLAVE = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, Integer.parseInt(numClave));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listPantallas.add(rs.getString("CVE_FORMA_EMP_CVE"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getPantallasPorNumClaveByID:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getPantallasPorNumClaveByID:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getPantallasPorNumClaveByID:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listPantallas;
    }
    public List<DestinoRecepcionBean> getClavesPorNumClaveByID(String numClave) {
        List<DestinoRecepcionBean> listDestinoRecepcionBeans = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT  CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from CLAVES WHERE  CVE_NUM_CLAVE = ? order by 2";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, Integer.parseInt(numClave));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DestinoRecepcionBean destinoRecepcionBean = new DestinoRecepcionBean();
                destinoRecepcionBean.setClaveNumero(rs.getInt("CVE_NUM_SEC_CLAVE"));
                destinoRecepcionBean.setClaveDescripcion(rs.getString("CVE_DESC_CLAVE"));
                listDestinoRecepcionBeans.add(destinoRecepcionBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClaveByID:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClaveByID:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getClavesPorNumClaveByID:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listDestinoRecepcionBeans;
    }

    public List<MonedasBean> getMonedas() throws SQLException {
        List<MonedasBean> listMonedas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sqlComando = "SELECT MON_NUM_PAIS,MON_NOM_MONEDA from SAF.MONEDAS";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MonedasBean monedaBean = new MonedasBean();
                monedaBean.setMonedaNumero(rs.getInt("MON_NUM_PAIS"));
                monedaBean.setMonedaNombre(rs.getString("MON_NOM_MONEDA"));
                listMonedas.add(monedaBean);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage();
            logger.error(mensajeError);

	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::getMonedas:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::getMonedas:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::getMonedas:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
        return listMonedas;
    }
    public Boolean onCommunes_BuscaDiasFeriados(Date parFecha)throws SQLException{
        
        //Integer result = 0;CAVC
        valorRetorno = Boolean.FALSE;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            
            Calendar calendarFechaDeInstruccion  = Calendar.getInstance();
            calendarFechaDeInstruccion.setTime(parFecha);    
            
            //efp
            sqlParam   = 0;
            sqlComando = "SELECT a.fer_fec_dia      \n" +
                         "  FROM feriados a         \n" +
                         " WHERE a.fer_num_pais = ? \n" +
                         "   AND a.fer_fec_mes  = ? \n" +
                         "   AND a.fer_fec_dia  = ?";
            
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            
            pstmt.setInt(1, ID_UNO);
            pstmt.setInt(2, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1   );
            pstmt.setInt(3, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));
                        
            rs = pstmt.executeQuery();
            
            if(rs != null) {
                if(rs.next() == true) { 
                   valorRetorno = Boolean.TRUE;;
                    /*do{CAVC
                        result = rs.getInt("fer_fec_dia");
                    } while(rs.next());*/
                } else {
                   valorRetorno = Boolean.FALSE;
                } 

            } else {
                valorRetorno = Boolean.FALSE;
            }            
            
//            if(rs != null) rs.close();
//            if(pstmt != null) pstmt.close();
//            if (conexion != null) conexion.close();
        }catch(SQLException Err){
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + " onInstruccionesFideicomiso_LeeContrato()";
            logger.error(Err.getMessage() +  " onInstruccionesFideicomiso_LeeContrato()");
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_BuscaDiasFeriados:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_BuscaDiasFeriados:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_BuscaDiasFeriados:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
     return valorRetorno;
    }    

    public Boolean onCommunes_ValidaMesCerrado(Date parFecha)throws SQLException{
        
        Integer result = 0;
        valorRetorno = Boolean.FALSE;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            
            Calendar calendarFechaDeInstruccion  = Calendar.getInstance();
            calendarFechaDeInstruccion.setTime(parFecha);    
            
            //efp
            sqlParam   = 0;
            sqlComando = "SELECT ccc_mes_abierto      \n" +
                            "  FROM saf.ctl_mesc        \n" +
                         " WHERE  ccc_ano = ?        \n" +
                         "   AND  ccc_mes = ?        \n" ;            
            conexion   = DataBaseConexion.getInstance().getConnection();
            pstmt      = conexion.prepareStatement(sqlComando);
            
            pstmt.setInt(1, calendarFechaDeInstruccion.get(Calendar.YEAR));
            pstmt.setInt(2, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
                        
            rs = pstmt.executeQuery();
            valorRetorno = Boolean.FALSE;
                if(rs.next() == true) { 
                    do{
                        result = rs.getInt("ccc_mes_abierto");
                    } while(rs.next());
                if (result ==1) valorRetorno = Boolean.TRUE;
                } 
            
//            if(rs != null) rs.close();
//            if(pstmt != null) pstmt.close();
//            if (conexion != null) conexion.close();
        }catch(SQLException Err){
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + " onInstruccionesFideicomiso_LeeContrato()";
            logger.error(Err.getMessage() +  " onInstruccionesFideicomiso_LeeContrato()");
	}finally{
            if (rs != null) {
                try {
                     rs.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_ValidaMesCerrado:: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                     pstmt.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_ValidaMesCerrado:: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
               try {
                    conexion.close();
                }catch (SQLException e) {
                    logger.error("Function ::onCommunes_ValidaMesCerrado:: Error al cerrar Connection.", e.getMessage());
                }
            }		
	}
    return valorRetorno;
    }    

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*    private void onCierraConexion() {
        if (rs != null) {
            rs = null;
        }
        if (pstmt != null) {
            pstmt = null;
        }
        if (cstmt != null) {
            cstmt = null;
        }
        if (conexion != null) {
            conexion = null;
        }
    }*/
}
