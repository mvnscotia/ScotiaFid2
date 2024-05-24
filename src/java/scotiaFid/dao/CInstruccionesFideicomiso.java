/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : 
 * ARCHIVO     : CInstruccionesFideicomiso.java
 * TIPO        : Clase
 * PAQUETE     : scotiaFid.dao
 * CREADO      : 20210310
 * MODIFICADO  : 20211013
 * AUTOR       : smartBuilder 
 * NOTAS       : 20211013.- Se imnplementa consulta de saldos promedio
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.CriterioBusquedaInstruccionesFideicomisoBean;
import scotiaFid.bean.DatosFisoBean;
import scotiaFid.bean.InstruccionFideicomisoBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;

public class CInstruccionesFideicomiso {

    private static final Logger logger = LogManager.getLogger(CInstruccionesFideicomiso.class);
    private static final Integer ID_UNO = 1;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private Integer sqlParam;
    private String sqlComando;
    private String sqlFiltro;
    private String nombreObjeto;
    private String mensajeErrorSP;
    private String mensaje = new String();

    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private static SimpleDateFormat formatoFecha;
    private Calendar calendario;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String pMess_Err;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    //efp test
    public String getpMess_Err() {
        return pMess_Err;
    }

    public void setpMess_Err(String pMess_Err) {
        this.pMess_Err = pMess_Err;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CInstruccionesFideicomiso() {
        LogsContext.FormatoNormativo();
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiaFid.dao.CContabilidad.";
        valorRetorno = Boolean.FALSE;
        calendario = Calendar.getInstance();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * M E T O D O S   I N S T R U C C I O N E S   F I D E I C O M I S O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public int getInstructionsTotalRows(CriterioBusquedaInstruccionesFideicomisoBean cbfi) throws SQLException {
        int totalRows = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        Integer sqlParamIns = 0;
        
        String sqlComandoIns = "";
        try {
            sqlParamIns = 0;
        
            //efp
            //"INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = bie_num_contrato AND V.USU_NUM_USUARIO = ? \n"
            sqlComandoIns = " SELECT COUNT(*) as total_rows \n"
                    + " FROM CONTRATO C \n"
                    + " INNER JOIN INSTRUCC I ON I.INS_NUM_CONTRATO = C.CTO_NUM_CONTRATO \n"
                    + " INNER JOIN EXPEDIEN E ON E.EXP_NUM_CONTRATO = C.CTO_NUM_CONTRATO  \n"
                    + " INNER JOIN EXPDOCTS D ON E.EXP_NUM_EXPEDIENTE = D.EXD_NUM_EXPEDIENTE AND D.EXD_NUM_FOLIO_INST = I.INS_NUM_FOLIO_INST \n"
                    + " WHERE C.CTO_NUM_NIVEL4  IN (SELECT DISTINCT (CTO_NUM_NIVEL4)  FROM SAF.VISTA_USUARIO \n"
                    + "                              WHERE  USU_NUM_USUARIO = ? \n)"
                    + "   AND c.cto_num_contrato   >= 0                  \n"
                    + "   AND c.cto_cve_st_contrat = 'ACTIVO'            \n";
            

            if (cbfi.getCriterioBusqInstrucFideicomiso() != null) {
                sqlComandoIns = sqlComandoIns + "and ins_num_contrato = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideSubCto() != null) {
                sqlComandoIns = sqlComandoIns + "and ins_sub_contrato = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideFolioInst() != null) {
                sqlComandoIns = sqlComandoIns + "and ins_num_folio_inst = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideFolioDocto() != null && !cbfi.getCriterioBusqInstrucFideFolioDocto().isEmpty()) {
                sqlComandoIns = sqlComandoIns + "AND UPPER(REPLACE(exd_id_documento,' ','')) = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideNomFideicomiso() != null && !cbfi.getCriterioBusqInstrucFideNomFideicomiso().equals(new String())) {
                sqlComandoIns = sqlComandoIns + "AND UPPER(REPLACE(c.cto_nom_contrato,' ','')) = ? \n";

            }

            if (cbfi.getCriterioBusqInstrucFideTipo() != null && !cbfi.getCriterioBusqInstrucFideTipo().isEmpty()) {
                sqlComandoIns = sqlComandoIns + "AND ins_cve_tipo_instr =  ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideInsCveStInstruc() != null && !cbfi.getCriterioBusqInstrucFideInsCveStInstruc().isEmpty()) {
                sqlComandoIns = sqlComandoIns + "AND ins_cve_st_instruc = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFidePlaza() != null) {
                sqlComandoIns = sqlComandoIns + "AND c.cto_num_nivel4 =  ? \n";
            }

            connection = DataBaseConexion.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sqlComandoIns);
            sqlParamIns++;
            //usuario
            Integer usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            preparedStatement.setObject(sqlParamIns, usuarioNumero, Types.BIGINT);

            if (cbfi.getCriterioBusqInstrucFideicomiso() != null) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideicomiso(), Types.BIGINT);
            }

            if (cbfi.getCriterioBusqInstrucFideSubCto() != null) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideSubCto(), Types.INTEGER);
            }

            if (cbfi.getCriterioBusqInstrucFideFolioInst() != null) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideFolioInst(), Types.INTEGER);

            }

            if (cbfi.getCriterioBusqInstrucFideFolioDocto() != null && !cbfi.getCriterioBusqInstrucFideFolioDocto().isEmpty()) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideFolioDocto().replaceAll("\\s+", "").toUpperCase(Locale.US), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFideNomFideicomiso() != null && !cbfi.getCriterioBusqInstrucFideNomFideicomiso().equals(new String())) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideNomFideicomiso().replaceAll("\\s+", "").toUpperCase(Locale.US), Types.VARCHAR);

            }

            if (cbfi.getCriterioBusqInstrucFideTipo() != null && !cbfi.getCriterioBusqInstrucFideTipo().isEmpty()) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideTipo(), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFideInsCveStInstruc() != null && !cbfi.getCriterioBusqInstrucFideInsCveStInstruc().isEmpty()) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFideInsCveStInstruc(), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFidePlaza() != null) {
                sqlParamIns++;
                preparedStatement.setObject(sqlParamIns, cbfi.getCriterioBusqInstrucFidePlaza(), Types.INTEGER);
            }

            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_rows");
            }
             
            resultSet.close();  
            preparedStatement.close();
            connection.close();
            
        } catch (SQLException Err) {
            logger.error(Err.getMessage() + Err.getCause());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsTotalRows:: Error al cerrar PreparetStatement.");
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsTotalRows:: Error al cerrar ResultSet.");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsTotalRows:: Error al cerrar Connection.");
                }
            }
        }
        return totalRows;
    }

    public synchronized List<InstruccionFideicomisoBean> getInstructionsPaginated(CriterioBusquedaInstruccionesFideicomisoBean cbfi, String offset) throws SQLException {
        String sqlComandoInstructions = "";
        Integer sqlParamInstructions = 0;
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        List<InstruccionFideicomisoBean> consulta = new ArrayList<>();

        try {
            int numericOffset = Integer.parseInt(offset);
            int between = numericOffset + 1;
            int and = numericOffset + 10;

            sqlParamInstructions = 0;
        
            //efp
            sqlComandoInstructions = "SELECT cto_nom_contrato   \n"
                    + " ,ins_num_contrato  \n"
                    + " ,ins_num_folio_inst\n"
                    + " ,ins_sub_contrato  \n"
                    + " ,ins_txt_comentario\n"
                    + " ,ins_cve_tipo_instr\n"
                    + " ,ins_num_miembro   \n"
                    + " ,ins_nom_miembro   \n"
                    + " ,ins_ano_alta_reg  \n"
                    + " ,ins_mes_alta_reg  \n"
                    + " ,ins_dia_alta_reg  \n"
                    + " ,exp_num_expediente\n"
                    + " ,exd_id_documento  \n"
                    + " ,exd_text_comentar \n"
                    + "   FROM ( \n"
                    + "      SELECT ROW_NUMBER() OVER (ORDER BY ins_num_contrato, ins_num_folio_inst , ins_SUB_contrato) AS Ind, \n"
                    + "      cto_nom_contrato   \n"
                    + "	,ins_num_contrato  \n"
                    + "	,ins_num_folio_inst\n"
                    + "	,ins_sub_contrato  \n"
                    + "	,ins_txt_comentario\n"
                    + "	,ins_cve_tipo_instr\n"
                    + "	,ins_num_miembro   \n"
                    + "	,ins_nom_miembro   \n"
                    + "	,ins_ano_alta_reg  \n"
                    + "	,ins_mes_alta_reg  \n"
                    + "	,ins_dia_alta_reg  \n"
                    + "	,exp_num_expediente\n"
                    + "	,exd_id_documento  \n"
                    + "	,exd_text_comentar \n"
                    + " FROM CONTRATO C \n"
                    + " INNER JOIN INSTRUCC I ON I.INS_NUM_CONTRATO = C.CTO_NUM_CONTRATO \n"
                    + " INNER JOIN EXPEDIEN E ON E.EXP_NUM_CONTRATO = C.CTO_NUM_CONTRATO  \n"
                    + " INNER JOIN EXPDOCTS D ON E.EXP_NUM_EXPEDIENTE = D.EXD_NUM_EXPEDIENTE AND D.EXD_NUM_FOLIO_INST = I.INS_NUM_FOLIO_INST \n"
                    + " WHERE C.CTO_NUM_NIVEL4  IN (SELECT DISTINCT (CTO_NUM_NIVEL4)  FROM SAF.VISTA_USUARIO \n"
                    + "                              WHERE  USU_NUM_USUARIO = ? \n)"
                    + "   AND c.cto_num_contrato   >= 0                  \n"
                    + "   AND c.cto_cve_st_contrat = 'ACTIVO'            \n";
            //" ORDER BY ins_num_contrato, ins_num_folio_inst , ins_SUB_contrato LIMIT 10 OFFSET ".concat(offset);

             if (cbfi.getCriterioBusqInstrucFideicomiso() != null) {
                sqlComandoInstructions = sqlComandoInstructions + "and ins_num_contrato = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideSubCto() != null) {
                sqlComandoInstructions = sqlComandoInstructions + "and ins_sub_contrato = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideFolioInst() != null) {
                sqlComandoInstructions = sqlComandoInstructions + "and ins_num_folio_inst = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideFolioDocto() != null && !cbfi.getCriterioBusqInstrucFideFolioDocto().isEmpty()) {
                sqlComandoInstructions = sqlComandoInstructions + "AND UPPER(REPLACE(exd_id_documento,' ','')) = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideNomFideicomiso() != null && !cbfi.getCriterioBusqInstrucFideNomFideicomiso().equals(new String())) {
                sqlComandoInstructions = sqlComandoInstructions + "AND UPPER(REPLACE(c.cto_nom_contrato,' ','')) = ? \n";

            }

            if (cbfi.getCriterioBusqInstrucFideTipo() != null && !cbfi.getCriterioBusqInstrucFideTipo().isEmpty()) {
                sqlComandoInstructions = sqlComandoInstructions + "AND ins_cve_tipo_instr =  ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFideInsCveStInstruc() != null && !cbfi.getCriterioBusqInstrucFideInsCveStInstruc().isEmpty()) {
                sqlComandoInstructions = sqlComandoInstructions + "AND ins_cve_st_instruc = ? \n";
            }

            if (cbfi.getCriterioBusqInstrucFidePlaza() != null) {
                sqlComandoInstructions = sqlComandoInstructions + "AND c.cto_num_nivel4 =  ? \n";
            }

            sqlComandoInstructions += ") AS Consulta WHERE Consulta.Ind \n";
            sqlComandoInstructions += "BETWEEN ".concat(Integer.toString(between).concat(" \n"));
            sqlComandoInstructions += "AND ".concat(Integer.toString(and));
            
            //System.out.println("------> SQL: ".concat(sqlComandoInstructions));
            conn = DataBaseConexion.getInstance().getConnection();
                preparedStatement = conn.prepareStatement(sqlComandoInstructions);

            sqlParamInstructions++;
            //usuario
            Integer usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
            preparedStatement.setObject(sqlParamInstructions, usuarioNumero, Types.BIGINT);

            if (cbfi.getCriterioBusqInstrucFideicomiso() != null) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideicomiso(), Types.BIGINT);
            }

            if (cbfi.getCriterioBusqInstrucFideSubCto() != null) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideSubCto(), Types.INTEGER);
            }

            if (cbfi.getCriterioBusqInstrucFideFolioInst() != null) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideFolioInst(), Types.INTEGER);

            }

            if (cbfi.getCriterioBusqInstrucFideFolioDocto() != null && !cbfi.getCriterioBusqInstrucFideFolioDocto().isEmpty()) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideFolioDocto().replaceAll("\\s+", "").toUpperCase(Locale.US), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFideNomFideicomiso() != null && !cbfi.getCriterioBusqInstrucFideNomFideicomiso().equals(new String())) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideNomFideicomiso().replaceAll("\\s+", "").toUpperCase(Locale.US), Types.VARCHAR);

            }

            if (cbfi.getCriterioBusqInstrucFideTipo() != null && !cbfi.getCriterioBusqInstrucFideTipo().isEmpty()) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideTipo(), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFideInsCveStInstruc() != null && !cbfi.getCriterioBusqInstrucFideInsCveStInstruc().isEmpty()) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFideInsCveStInstruc(), Types.VARCHAR);
            }

            if (cbfi.getCriterioBusqInstrucFidePlaza() != null) {
                sqlParamInstructions++;
                preparedStatement.setObject(sqlParamInstructions, cbfi.getCriterioBusqInstrucFidePlaza(), Types.INTEGER);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InstruccionFideicomisoBean ifb = new InstruccionFideicomisoBean();
                ifb.setInstrucFideCtoNomContrato(resultSet.getString("cto_nom_contrato"));
                ifb.setInstrucFideInsNumContrato(resultSet.getLong("ins_num_contrato"));
                ifb.setInstrucFideInsNumFolioInst(resultSet.getInt("ins_num_folio_inst"));
                ifb.setInstrucFideInsSubContrato(resultSet.getInt("ins_sub_contrato"));
                ifb.setInstrucFideInsTxtComentario(resultSet.getString("ins_txt_comentario"));
                ifb.setInstrucFideInsCveTipoInstr(resultSet.getString("ins_cve_tipo_instr"));
                ifb.setInstrucFideInsNumMiembro(resultSet.getString("ins_num_miembro"));
                ifb.setInstrucFideInsNomMiembro(resultSet.getString("ins_nom_miembro"));
                ifb.setInstrucFideInsAnoAltaReg(resultSet.getString("ins_ano_alta_reg"));
                ifb.setInstrucFideInsMesAltaReg(resultSet.getString("ins_mes_alta_reg"));
                ifb.setInstrucFideInsDiaAltaReg(resultSet.getString("ins_dia_alta_reg"));
                ifb.setInstrucFideExpNumExpediente(resultSet.getString("exp_num_expediente"));
                ifb.setInstrucFideExdIdDocumento(resultSet.getString("exd_id_documento"));
                ifb.setInstrucFideExdTextComentar(resultSet.getString("exd_text_comentar"));
                consulta.add(ifb);
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsPaginated:: Error al cerrar PreparetStatement.");
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsPaginated:: Error al cerrar ResultSet.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Function ::getInstructionsPaginated:: Error al cerrar Connection.");
                }
            }
        }
        return consulta;
    }

    public List<InstruccionFideicomisoBean> onInstruccionesFideicomiso_Consulta(CriterioBusquedaInstruccionesFideicomisoBean cbfi) throws SQLException {
        List<InstruccionFideicomisoBean> consulta = new ArrayList<>();
        try {
            sqlParam = 0;
            sqlFiltro = "WHERE ";
            //efp
            sqlComando = "SELECT cto_nom_contrato   \n"
                    + "	 ,ins_num_contrato  \n"
                    + "	 ,ins_num_folio_inst\n"
                    + "	 ,ins_sub_contrato  \n"
                    + "	 ,ins_txt_comentario\n"
                    + "	 ,ins_cve_tipo_instr\n"
                    + "	 ,ins_num_miembro   \n"
                    + "	 ,ins_nom_miembro   \n"
                    + "	 ,ins_ano_alta_reg  \n"
                    + "	 ,ins_mes_alta_reg  \n"
                    + "	 ,ins_dia_alta_reg  \n"
                    + "	 ,exp_num_expediente\n"
                    + "	 ,exd_id_documento  \n"
                    + "	 ,exd_text_comentar \n"
                    + "  FROM CONTRATO,\n"
                    + "  	 INSTRUCC,\n"
                    + "  	 EXPEDIEN,\n"
                    + "  	 EXPDOCTS \n"
                    + " WHERE exd_num_expediente = eXP_num_expediente  \n"
                    + "   AND ins_num_contrato   = cto_num_contrato    \n"
                    + "   AND exp_num_contrato   = ins_num_contrato    \n"
                    + "   AND exd_num_expediente = exp_num_expediente  \n"
                    + "   AND exd_num_folio_inst = ins_num_folio_inst  \n"
                    + "   AND cto_num_contrato   >= 0                  \n"
                    + "   AND cto_cve_st_contrat = 'ACTIVO'            \n"
                    + "   AND ins_num_contrato                        = (CASE WHEN (CAST(? AS INTEGER    ) IS NULL) THEN ins_num_contrato                        ELSE ? END) \n"
                    + "   AND UPPER(REPLACE(cto_nom_contrato,' ','')) = (CASE WHEN (CAST(? AS VARCHAR(80)) IS NULL) THEN UPPER(REPLACE(cto_nom_contrato,' ','')) ELSE UPPER(REPLACE(CAST(? AS VARCHAR(80)),' ','')) END) \n"
                    + "   AND ins_sub_contrato                        = (CASE WHEN (CAST(? AS INTEGER    ) IS NULL) THEN ins_sub_contrato                        ELSE ? END) \n"
                    + "   AND ins_num_folio_inst                      = (CASE WHEN (CAST(? AS INTEGER    ) IS NULL) THEN ins_num_folio_inst                      ELSE ? END) \n"
                    + "   AND UPPER(REPLACE(exd_id_documento,' ','')) = (CASE WHEN (CAST(? AS VARCHAR(50)) IS NULL) THEN UPPER(REPLACE(exd_id_documento,' ','')) ELSE UPPER(REPLACE(CAST(? AS VARCHAR(50)),' ','')) END) \n"
                    + "   AND ins_cve_tipo_instr                      = (CASE WHEN (CAST(? AS VARCHAR(40)) IS NULL) THEN ins_cve_tipo_instr                      ELSE ? END) \n"
                    + "   AND ins_cve_st_instruc                      = (CASE WHEN (CAST(? AS VARCHAR(25)) IS NULL) THEN ins_cve_st_instruc                      ELSE ? END) \n"
                    + "   AND cto_num_nivel4                          = (CASE WHEN (CAST(? AS INTEGER    ) IS NULL) THEN cto_num_nivel4                          ELSE ? END) \n"
                    + " ORDER BY ins_num_contrato, ins_num_folio_inst , ins_SUB_contrato";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            /*1*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideicomiso(), Types.BIGINT);
            sqlParam++;
            /*2*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideicomiso(), Types.BIGINT);

            if (cbfi.getCriterioBusqInstrucFideNomFideicomiso() != null) {
                cbfi.setCriterioBusqInstrucFideNomFideicomiso(cbfi.getCriterioBusqInstrucFideNomFideicomiso().replaceAll("\\s+", ""));
                if (cbfi.getCriterioBusqInstrucFideNomFideicomiso().isEmpty()) {
                    cbfi.setCriterioBusqInstrucFideNomFideicomiso(null);
                }
            }
            sqlParam++;
            /*3*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideNomFideicomiso(), Types.VARCHAR);
            sqlParam++;
            /*4*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideNomFideicomiso(), Types.VARCHAR);

            sqlParam++;
            /*5*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideSubCto(), Types.INTEGER);
            sqlParam++;
            /*6*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideSubCto(), Types.INTEGER);

            sqlParam++;
            /*7*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideFolioInst(), Types.INTEGER);
            sqlParam++;
            /*8*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideFolioInst(), Types.INTEGER);

            if (cbfi.getCriterioBusqInstrucFideFolioDocto() != null) {
                cbfi.setCriterioBusqInstrucFideFolioDocto(cbfi.getCriterioBusqInstrucFideFolioDocto().replaceAll("\\s+", ""));
                if (cbfi.getCriterioBusqInstrucFideFolioDocto().isEmpty()) {
                    cbfi.setCriterioBusqInstrucFideFolioDocto(null);
                }
            }
            sqlParam++;
            /*9*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideFolioDocto(), Types.VARCHAR);
            sqlParam++;
            /*10*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideFolioDocto(), Types.VARCHAR);

            sqlParam++;
            /*11*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideTipo(), Types.VARCHAR);
            sqlParam++;
            /*12*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideTipo(), Types.VARCHAR);

            sqlParam++;
            /*13*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideInsCveStInstruc(), Types.VARCHAR);
            sqlParam++;
            /*14*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFideInsCveStInstruc(), Types.VARCHAR);

            sqlParam++;
            /*15*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFidePlaza(), Types.INTEGER);
            sqlParam++;
            /*16*/
            pstmt.setObject(sqlParam, cbfi.getCriterioBusqInstrucFidePlaza(), Types.INTEGER);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                InstruccionFideicomisoBean ifb = new InstruccionFideicomisoBean();
                ifb.setInstrucFideCtoNomContrato(rs.getString("cto_nom_contrato"));
                ifb.setInstrucFideInsNumContrato(rs.getLong("ins_num_contrato"));
                ifb.setInstrucFideInsNumFolioInst(rs.getInt("ins_num_folio_inst"));
                ifb.setInstrucFideInsSubContrato(rs.getInt("ins_sub_contrato"));
                ifb.setInstrucFideInsTxtComentario(rs.getString("ins_txt_comentario"));
                ifb.setInstrucFideInsCveTipoInstr(rs.getString("ins_cve_tipo_instr"));
                ifb.setInstrucFideInsNumMiembro(rs.getString("ins_num_miembro"));
                ifb.setInstrucFideInsNomMiembro(rs.getString("ins_nom_miembro"));
                ifb.setInstrucFideInsAnoAltaReg(rs.getString("ins_ano_alta_reg"));
                ifb.setInstrucFideInsMesAltaReg(rs.getString("ins_mes_alta_reg"));
                ifb.setInstrucFideInsDiaAltaReg(rs.getString("ins_dia_alta_reg"));
                ifb.setInstrucFideExpNumExpediente(rs.getString("exp_num_expediente"));
                ifb.setInstrucFideExdIdDocumento(rs.getString("exd_id_documento"));
                ifb.setInstrucFideExdTextComentar(rs.getString("exd_text_comentar"));
                consulta.add(ifb);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_Consulta()");
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_Consulta:: Error al cerrar PreparetStatement.");
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_Consulta:: Error al cerrar ResultSet.");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_Consulta:: Error al cerrar Connection.");
                }
            }
        }
        return consulta;
    }

    public Boolean onInstruccionesFideicomiso_VerificaContratoSrv(Long fideicomiso, DatosFisoBean opFiso) throws SQLException {
        //Objects
        StringBuilder stringBuilder = null;

        try {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT CTO_NUM_CONTRATO, CTO_NOM_CONTRATO, \n");
            stringBuilder.append("CTO_CVE_ST_CONTRAT, CTO_TIPO_ADMON, CTO_CVE_MON_EXT, \n");
            stringBuilder.append("CTO_CVE_SUBCTO, CTO_NUM_NIVEL1, CTO_NUM_NIVEL2, CTO_NUM_NIVEL3, \n");
            stringBuilder.append("CTO_NUM_NIVEL4, CTO_NUM_NIVEL5, A.CVE_NUM_SEC_CLAVE TipoNeg, \n");
            stringBuilder.append("B.CVE_NUM_SEC_CLAVE clasif, CTO_CVE_REQ_SORS AbiertoCerrado, \n");
            stringBuilder.append("CTO_CVE_EXCLU_30 ImpEsp,  CTO_SUB_RAMA Prorrate_SubFiso\n");
            stringBuilder.append(" FROM ((CONTRATO LEFT OUTER JOIN CLAVES A ON A.CVE_NUM_CLAVE = 36 \n");
            stringBuilder.append(" AND CTO_CVE_TIPO_NEG = A.CVE_DESC_CLAVE) LEFT OUTER JOIN CLAVES B ON B.CVE_NUM_CLAVE = 37 AND CTO_CVE_CLAS_PROD = B.CVE_DESC_CLAVE) \n");
            stringBuilder.append(" WHERE CTO_NUM_NIVEL4  IN (SELECT DISTINCT (CTO_NUM_NIVEL4)  FROM SAF.VISTA_USUARIO \n");
            stringBuilder.append("                              WHERE  USU_NUM_USUARIO = ? \n)");
            stringBuilder.append(" AND CTO_NUM_CONTRATO = ? AND CTO_ES_EJE = ? AND CTO_CVE_ST_CONTRAT= ? \n");
            //Write_Console
            //System.out.println("SQL: ".concat(stringBuilder.toString()));
            String queryBuilder = (stringBuilder.toString() != null) ? stringBuilder.toString() : "";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(queryBuilder);

            Integer usuarioNumero = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");

            //Set_Values
            pstmt.setLong(1, usuarioNumero);
            pstmt.setLong(2, fideicomiso);
            pstmt.setInt(3, 0);
            pstmt.setString(4, "ACTIVO"); 

            
            //Execute_Query
            rs = pstmt.executeQuery();

            if (rs != null) {
                if (rs.next() == true) {
                    valorRetorno = true;
                    do {
                        opFiso.setlNumero(rs.getLong("CTO_NUM_CONTRATO"));
                        opFiso.setsNombre(rs.getString("CTO_NOM_CONTRATO"));
                        if (rs.getString("CTO_TIPO_ADMON").equalsIgnoreCase("SI")) {
                            opFiso.setbAdmonPropia(Boolean.TRUE);
                        } else {
                            opFiso.setbAdmonPropia(Boolean.FALSE);
                        }
                        opFiso.setiNivelEstructura1(rs.getInt("CTO_NUM_NIVEL1"));
                        opFiso.setiNivelEstructura2(rs.getInt("CTO_NUM_NIVEL2"));
                        opFiso.setiNivelEstructura3(rs.getInt("CTO_NUM_NIVEL3"));
                        opFiso.setiNivelEstructura4(rs.getInt("CTO_NUM_NIVEL4"));
                        opFiso.setiNivelEstructura5(rs.getInt("CTO_NUM_NIVEL5"));
                        opFiso.setsStatus(rs.getString("CTO_CVE_ST_CONTRAT"));
                        opFiso.setbSubContrato(Boolean.FALSE);
                        if (rs.getInt("CTO_CVE_SUBCTO") == 1) {
                            opFiso.setbSubContrato(Boolean.TRUE);
                        }
                        opFiso.setbMonedaExtranjera(Boolean.FALSE);
                        opFiso.setiAbiertoCerrado(rs.getInt("AbiertoCerrado"));
                        opFiso.setiIVAEspecial(rs.getInt("ImpEsp"));

                        Integer monedaExtranjera = rs.getInt("CTO_CVE_MON_EXT");
                        if (monedaExtranjera != null) {
                            if (rs.getInt("CTO_CVE_MON_EXT") == 1) {
                                opFiso.setbMonedaExtranjera(Boolean.TRUE);
                            }
                        }
                        valorRetorno = Boolean.TRUE;
                        Integer tipoNegocio = rs.getInt("TipoNeg");
                        if (tipoNegocio != null) {
                            opFiso.setiTipoNegocio(rs.getInt("TipoNeg"));
                        } else {
                            valorRetorno = Boolean.FALSE;
                            opFiso.setiTipoNegocio(0);
                        }

                        Integer clasifProducto = rs.getInt("clasif");
                        if (clasifProducto != null) {
                            opFiso.setiClasifProducto(rs.getInt("clasif"));
                        } else {
                            valorRetorno = Boolean.FALSE;
                            opFiso.setiClasifProducto(0);
                        }

                        Integer prorrateaSubFiso = rs.getInt("Prorrate_SubFiso");
                        if (prorrateaSubFiso != null) {
                            opFiso.setiProrrateaSubFiso(rs.getInt("Prorrate_SubFiso"));
                        } else {
                            valorRetorno = Boolean.FALSE;
                            opFiso.setiProrrateaSubFiso(0);
                        }

                    } while (rs.next());
                } else {
                    valorRetorno = false;
                    setpMess_Err("El Fideicomiso no existe o no está activo ");
                }

            } else {
                valorRetorno = false;
                setpMess_Err("El Fideicomiso no existe o no está activo ");
            }

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_VerificaContratoSrv()";
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_VerificaContratoSrv:: Error al cerrar PreparetStatement.");
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_VerificaContratoSrv:: Error al cerrar ResultSet.");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Function ::onInstruccionesFideicomiso_VerificaContratoSrv:: Error al cerrar Connection.");
                }
            }
        }
        return valorRetorno;
    }

    public String onInstruccionesFideicomiso_LeeExpedient(Long instrucFideInsNumContrato) throws SQLException {
        String result = "";
        try {
            sqlParam = 0;
            //efp
            sqlComando = "SELECT * FROM EXPEDIEN WHERE EXP_NUM_CONTRATO = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsNumContrato, Types.BIGINT);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("EXP_NUM_EXPEDIENTE");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_LeeExpedient()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_LeeExpedient()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public Boolean onInstruccionesFideicomiso_LeeExpedientExist(String instrucFideExpNumExpediente) throws SQLException {
        valorRetorno = Boolean.TRUE;
        try {
            sqlParam = 0;
            //efp
            sqlComando = "SELECT Count(*) exp_exist FROM EXPEDIEN WHERE EXP_NUM_EXPEDIENTE = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement((sqlComando != null) ? sqlComando : sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideExpNumExpediente, Types.VARCHAR);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getShort("exp_exist") == 0) {
                    valorRetorno = Boolean.FALSE;
                } else {
                    valorRetorno = Boolean.TRUE;
                }
            }

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_LeeExpedientExist()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_BuscaDiasFeriados(InstruccionFideicomisoBean instrucFide) throws SQLException {

        Integer result = 0;
        valorRetorno = Boolean.FALSE;

        try {

            Calendar calendarFechaDeInstruccion = Calendar.getInstance();
            calendarFechaDeInstruccion.setTime(instrucFide.getInstrucFideFechaDeInstruccion());

            //efp
            sqlParam = 0;
            sqlComando = "SELECT a.fer_fec_dia      \n"
                    + "  FROM feriados a         \n"
                    + " WHERE a.fer_num_pais = ? \n"
                    + "   AND a.fer_fec_mes  = ? \n"
                    + "   AND a.fer_fec_dia  = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setInt(1, ID_UNO);
            pstmt.setInt(2, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
            pstmt.setInt(3, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                valorRetorno = Boolean.TRUE;;
                /*do { CAVC
                    result = rs.getInt("fer_fec_dia");
                } while (rs.next());*/
            } else {
                valorRetorno = Boolean.FALSE;
            }

            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException Err) {

            logger.error(Err.getMessage() + " onInstruccionesFideicomiso_LeeContrato()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Integer onInstruccionesFideicomiso_LeeContrato(Long instrucFideInsNumContrato) throws SQLException {
        Integer result = 0;
        try {
            //efp
            sqlParam = 0;
            sqlComando = "SELECT CTO_CVE_COMITE_TEC FROM CONTRATO WHERE CTO_NUM_CONTRATO =  ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsNumContrato, Types.BIGINT);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                valorRetorno = true;
                do {
                    result = rs.getInt("CTO_CVE_COMITE_TEC");
                } while (rs.next());
            } else {
                valorRetorno = Boolean.TRUE;
                result = 0;
                setpMess_Err("El Fideicomiso no existe o no está activo ");
            }

            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + " onInstruccionesFideicomiso_LeeContrato()";
            logger.error(Err.getMessage() + " onInstruccionesFideicomiso_LeeContrato()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public Boolean onInstruccionesFideicomiso_BuscaEjecutivoAgenda(Long instrucFideInsNumContrato, String tipoEjecutivo, Integer numEjecutivoAgenda, String nomEjecutivoAgenda) throws SQLException {
        try {
            sqlParam = 0;
            sqlComando = "SELECT a.ate_nom_ejecutivo               \n"
                    + "  FROM ATENCION a                        \n"
                    + " WHERE a.ate_num_contrato	  = ? \n"
                    + "   AND a.ate_cve_tipo_func  = ?  \n"
                    + "   AND a.ate_cve_st_atencio = 'AGENDA'   \n"
                    + "   AND a.ate_num_nivel3     != 0";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsNumContrato, Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, tipoEjecutivo, Types.VARCHAR);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                nomEjecutivoAgenda = rs.getString("ate_nom_ejecutivo");
            }
            rs.close();
            pstmt.close();

            if (!nomEjecutivoAgenda.isEmpty()) {
                sqlParam = 0;
                sqlComando = "SELECT a.usu_num_usuario FROM usuarios a WHERE a.usu_nom_usuario = ? ";

                pstmt = conexion.prepareStatement(sqlComando);

                sqlParam++;
                pstmt.setObject(sqlParam, nomEjecutivoAgenda, Types.VARCHAR);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    numEjecutivoAgenda = rs.getInt("usu_num_usuario");
                }
                rs.close();
                pstmt.close();

                /*CAVC
                if (numEjecutivoAgenda == null) {
                    numEjecutivoAgenda = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                    nomEjecutivoAgenda = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
                }
                */
            } 
            /*CAVC
            else {
                numEjecutivoAgenda = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero");
                nomEjecutivoAgenda = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre");
            }*/

            rs.close();
            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;
        } catch (SQLException Err) {
//            mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_LeeExpedient()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_BuscaEjecutivoAgenda()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public List<String> onInstruccionesFideicomiso_CargaComite(Long instrucFideInsNumContrato) throws SQLException {
        List<String> result = new ArrayList<>();
        try {
            //efp
            sqlParam = 0;
            sqlComando = "SELECT COM_NUM_MIEMBRO, COM_NOM_MIEMBRO FROM COMITE WHERE COM_NUM_CONTRATO = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsNumContrato, Types.BIGINT);

            rs = pstmt.executeQuery();

            if (rs != null) {
                if (rs.next() == true) {
                    valorRetorno = true;
                    do {
                        Integer com_num_miembro = rs.getInt("COM_NUM_MIEMBRO");
                        String com_nom_miembro = rs.getString("COM_NOM_MIEMBRO");
                        result.add(com_num_miembro + "/" + com_nom_miembro);
                    } while (rs.next());
                } else {
                    valorRetorno = Boolean.TRUE;

                }

            } else {
                valorRetorno = false;
                setpMess_Err("El Fideicomiso no existe o no está activo ");
            }

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + " onInstruccionesFideicomiso_CargaComite()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public List<String> onInstruccionesFideicomiso_CargaFideicomitente(Long instrucFideInsNumContrato) throws SQLException {
        List<String> result = new ArrayList<>();
        try {
            //efp
            sqlParam = 0;
            sqlComando = "SELECT FID_FIDEICOMITENTE, FID_NOM_FIDEICOM FROM FIDEICOM WHERE FID_NUM_CONTRATO = ? AND FID_CVE_ST_FIDEICO = 'ACTIVO' ORDER BY FID_NOM_FIDEICOM ASC";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement((sqlComando != null) ? sqlComando : sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsNumContrato, Types.BIGINT);

            rs = pstmt.executeQuery();

            if (rs != null) {
                if (rs.next() == true) {
                    valorRetorno = true;
                    do {
                        Integer fid_fideicomitente = rs.getInt("FID_FIDEICOMITENTE");
                        String fid_nom_fideicom = rs.getString("FID_NOM_FIDEICOM");
                        result.add(fid_fideicomitente + "/" + fid_nom_fideicom);
                    } while (rs.next());
                } else {
                    valorRetorno = Boolean.TRUE;
                }

            } else {
                valorRetorno = false;
                setpMess_Err("El Fideicomiso no existe o no está activo ");
            }

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + " onInstruccionesFideicomiso_CargaComite()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public String[] onInstruccionesFideicomiso_CargaAutorizador(InstruccionFideicomisoBean instrucFide) throws SQLException {
        String[] result = new String[2];
        try {
            //efp
            sqlParam = 0;
            sqlComando = "SELECT a.INS_NUM_MIEMBRO,              \n"
                    + "	 a.INS_NOM_MIEMBRO               \n"
                    + "  FROM INSTRUCC a                      \n"
                    + " WHERE a.INS_NUM_CONTRATO 	= ?      \n"
                    + "   AND a.INS_NUM_FOLIO_INST   = ?     \n"
                    + "   AND a.INS_SUB_CONTRATO  	= ? ";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement((sqlComando != null) ? sqlComando : sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumContrato(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumFolioInst(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsSubContrato(), Types.BIGINT);

            rs = pstmt.executeQuery();

            if (rs != null) {
                if (rs.next() == true) {
                    valorRetorno = true;
                    do {
                        Integer ins_num_miembro = rs.getInt("INS_NUM_MIEMBRO");
                        String ins_nom_miembro = rs.getString("INS_NOM_MIEMBRO");
                        result[0] = ins_num_miembro.toString();
                        result[1] = ins_nom_miembro;
                    } while (rs.next());
                } else {
                    valorRetorno = Boolean.TRUE;

                }

            } else {
                valorRetorno = false;
                setpMess_Err("El Fideicomiso no existe o no está activo ");
            }

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + " onInstruccionesFideicomiso_CargaComite()");
        } finally {

            onCierraConexion();
        }
        return result;
    }

    public Boolean onInstruccionesFideicomiso_ValidaExpedocs(String expNumExpediente, InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;
            /**
             * ********************************************************************************************************
             * Consulta expediente
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "SELECT * FROM EXPDOCTS WHERE EXD_NUM_EXPEDIENTE = ? and EXD_NUM_DOCUMENTO = 999 AND EXD_ID_DOCUMENTO = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, expNumExpediente, Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExdIdDocumento(), Types.VARCHAR);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                valorRetorno = Boolean.TRUE;
            } else {
                valorRetorno = Boolean.FALSE;
            }
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException Err) {

            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabaExpedocs()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_GrabaExpedocs(String expNumExpediente, InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;
            /**
             * ********************************************************************************************************
             * Consulta expediente
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "SELECT * FROM EXPDOCTS WHERE EXD_NUM_EXPEDIENTE = ? and EXD_NUM_DOCUMENTO = 999 AND EXD_ID_DOCUMENTO = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, expNumExpediente, Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExdIdDocumento(), Types.VARCHAR);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                valorRetorno = Boolean.TRUE;
            } else {
                valorRetorno = Boolean.FALSE;
            }
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

            if (valorRetorno.equals(Boolean.TRUE)) {
                /**
                 * ********************************************************************************************************
                 * update expediente
                 * *********************************************************************************************************
                 */
//                Integer endLength = instrucFide.getInstrucFideInsTxtComentario().length() > 500 ? 500 : instrucFide.getInstrucFideInsTxtComentario().length();
//                String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().substring(0, endLength);
                String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().replace("\r\n", "\n");

                sqlParam = 0;
                //efp
                sqlComando = " UPDATE expdocts               \n"
                        + "   SET EXD_ID_DOCUMENTO   = ?   \n"
                        + "      ,EXD_TEXT_COMENTAR  = ?   \n"
                        + " WHERE EXD_NUM_EXPEDIENTE = ?   \n"
                        + "   AND EXD_NUM_DOCUMENTO  = 999 \n"
                        + "   AND EXD_ID_DOCUMENTO   = ? ";

                pstmt = conexion.prepareStatement(sqlComando);

                pstmt.setString(1, instrucFide.getInstrucFideExdIdDocumento());

                pstmt.setString(2, instrucFideInsTxtComentario);

                pstmt.setString(3, expNumExpediente);

                pstmt.setString(4, instrucFide.getInstrucFideExdIdDocumento());

                //CAVC int result = pstmt.executeUpdate(); 
                pstmt.executeUpdate();
                
                pstmt.close();
                conexion.close();
                valorRetorno = Boolean.TRUE;
            } else {
                /**
                 * ********************************************************************************************************
                 * Inserta documento relacionado al expediente
                 * *********************************************************************************************************
                 */
                sqlParam = 0;
                //efp
                sqlComando = "INSERT INTO expdocts            \n"
                        + "		  (EXD_NUM_EXPEDIENTE \n"
                        + "		  ,EXD_NUM_DOCUMENTO  \n"
                        + "		  ,EXD_ID_DOCUMENTO   \n"
                        + "                ,EXD_NUM_FOLIO_INST \n"
                        + "                ,EXD_TEXT_COMENTAR  \n"
                        + "                ,EXD_ANO_ALTA_REG   \n"
                        + "                ,EXD_MES_ALTA_REG   \n"
                        + "                ,EXD_DIA_ALTA_REG   \n"
                        + "                ,EXD_ANO_ULT_MOD    \n"
                        + "                ,EXD_MES_ULT_MOD    \n"
                        + "                ,EXD_DIA_ULT_MOD    \n"
                        + "                ,EXD_CVE_ST_DOCUMEN)\n"
                        + "	   VALUES                     \n"
                        + "                (?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,?                  \n"
                        + "                ,'ACTIVO')";

//                conexion = DataBaseConexion.getInstance().getConnection();
                pstmt = conexion.prepareStatement(sqlComando);

                sqlParam++;
                /*1*/
                pstmt.setObject(sqlParam, expNumExpediente, Types.VARCHAR);
                sqlParam++;
                /*2*/
                pstmt.setObject(sqlParam, 999, Types.INTEGER);
                sqlParam++;
                /*3*/
                pstmt.setObject(sqlParam, instrucFide.getInstrucFideExdIdDocumento(), Types.VARCHAR);
                sqlParam++;
                /*4*/
                pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumFolioInst(), Types.INTEGER);
                sqlParam++;
                /*5*/
                Integer endLength = instrucFide.getInstrucFideInsTxtComentario().length() > 500 ? 500 : instrucFide.getInstrucFideInsTxtComentario().length();
                String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().substring(0, endLength);
                pstmt.setObject(sqlParam, instrucFideInsTxtComentario, Types.VARCHAR);
                calendario.setTime(formatDate());
                pstmt.setInt(6, calendario.get(Calendar.YEAR));
                pstmt.setInt(7, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt(8, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setInt(9, calendario.get(Calendar.YEAR));
                pstmt.setInt(10, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt(11, calendario.get(Calendar.DAY_OF_MONTH));

                pstmt.execute();
                pstmt.close();
                conexion.close();
                valorRetorno = Boolean.TRUE;
            }

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabaExpedocs()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public synchronized Date formatDate() {
        Date fechaFormat = null;
        fechaFormat = new Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
        return fechaFormat;
    }

    public Boolean onInstruccionesFideicomiso_GrabaExpedien(InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            valorRetorno = Boolean.FALSE;

            /**
             * ********************************************************************************************************
             * Inserta expediente
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "INSERT INTO EXPEDIEN \n"
                    + "			(EXP_NUM_EXPEDIENTE\n"
                    + "			,EXP_NUM_CONTRATO\n"
                    + "			,EXP_NUM_PROSPECTO\n"
                    + "			,EXP_NUM_BOVEDA\n"
                    + "			,EXP_NUM_ARCHIVERO\n"
                    + "			,EXP_NUM_CAJON\n"
                    + "			,EXP_ANO_ALTA_REG\n"
                    + "			,EXP_MES_ALTA_REG\n"
                    + "			,EXP_DIA_ALTA_REG\n"
                    + "			,EXP_ANO_ULT_MOD\n"
                    + "			,EXP_MES_ULT_MOD\n"
                    + "			,EXP_DIA_ULT_MOD\n"
                    + "			,EXP_CVE_ST_EXPEDIE) \n"
                    + "	VALUES \n"
                    + "			(?              \n"
                    + "			,?              \n"
                    + "			,0              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,?              \n"
                    + "			,'ACTIVO')";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            /*1*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExpNumExpediente(), Types.VARCHAR);
            sqlParam++;
            /*2*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumContrato(), Types.INTEGER);
            sqlParam++;
            /*3*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExpArchivo(), Types.SMALLINT);
            sqlParam++;
            /*4*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExpArchivero(), Types.SMALLINT);
            sqlParam++;
            /*5*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideExpNiivel(), Types.SMALLINT);

            calendario.setTime(formatDate());
            pstmt.setInt(6, calendario.get(Calendar.YEAR));
            pstmt.setInt(7, calendario.get(Calendar.MONTH) + 1);
            pstmt.setInt(8, calendario.get(Calendar.DAY_OF_MONTH));
            pstmt.setInt(9, calendario.get(Calendar.YEAR));
            pstmt.setInt(10, calendario.get(Calendar.MONTH) + 1);
            pstmt.setInt(11, calendario.get(Calendar.DAY_OF_MONTH));

            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_GrabaExpedien()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabaExpedien()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_AltaRegistro(InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            calendario.setTime(formatDate());
            Calendar calendarFechaDeInstruccion = Calendar.getInstance();
            calendarFechaDeInstruccion.setTime(instrucFide.getInstrucFideFechaDeInstruccion());

            /**
             * ********************************************************************************************************
             * Alta de Instruccion de Fideicomiso
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "INSERT INTO INSTRUCC (INS_NUM_CONTRATO     \n"
                    + "                      ,INS_CVE_TIPO_INSTR  \n"
                    + "                      ,INS_NUM_FOLIO_INST  \n"
                    + "                      ,INS_SUB_CONTRATO    \n"
                    + "                      ,INS_TXT_COMENTARIO  \n"
                    + "                      ,INS_NUM_MIEMBRO     \n"
                    + "                      ,INS_NOM_MIEMBRO     \n"
                    + "                      ,INS_ANO_ALTA_REG    \n"
                    + "                      ,INS_MES_ALTA_REG    \n"
                    + "                      ,INS_DIA_ALTA_REG    \n"
                    + "                      ,INS_ANO_ULT_MOD     \n"
                    + "                      ,INS_MES_ULT_MOD     \n"
                    + "                      ,INS_DIA_ULT_MOD     \n"
                    + "                      ,INS_CVE_ST_INSTRUC) \n"
                    + "     VALUES                                \n"
                    + "                      (?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,?                   \n"
                    + "                      ,'PENDIENTE')";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumContrato(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsCveTipoInstr(), Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumFolioInst(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsSubContrato(), Types.INTEGER);

            String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().replace("\r\n", "\n");
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFideInsTxtComentario, Types.VARCHAR);
            if (instrucFide.getInstrucFideInsNomMiembro() != null) {
                String[] datosDeMiembroAutorizador = instrucFide.getInstrucFideInsNomMiembro().split("/");
                sqlParam++;
                pstmt.setObject(sqlParam, new Long(datosDeMiembroAutorizador[0]), Types.BIGINT);
                sqlParam++;
                pstmt.setObject(sqlParam, datosDeMiembroAutorizador[1], Types.VARCHAR);
            } else {
                sqlParam++;
                pstmt.setObject(sqlParam, 0, Types.BIGINT);
                sqlParam++;
                pstmt.setObject(sqlParam, "", Types.VARCHAR);
            }

            pstmt.setInt(8, calendarFechaDeInstruccion.get(Calendar.YEAR));
            pstmt.setInt(9, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
            pstmt.setInt(10, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));
            pstmt.setInt(11, calendario.get(Calendar.YEAR));
            pstmt.setInt(12, calendario.get(Calendar.MONTH) + 1);
            pstmt.setInt(13, calendario.get(Calendar.DAY_OF_MONTH));

            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (NumberFormatException | SQLException Err) {
            valorRetorno = Boolean.FALSE;
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_AltaRegistro()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_ActualizaRegistro(InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            calendario.setTime(formatDate());
            Calendar calendarFechaDeInstruccion = Calendar.getInstance();
            calendarFechaDeInstruccion.setTime(instrucFide.getInstrucFideFechaDeInstruccion());

            /**
             * ********************************************************************************************************
             * Alta de Instruccion de Fideicomiso
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "UPDATE INSTRUCC a\n"
                    + "   SET  a.ins_cve_tipo_instr = ?      \n"
                    + "	 ,a.ins_num_miembro    = ?      \n"
                    + "	 ,a.ins_nom_miembro    = ?      \n"
                    + "	 ,a.ins_txt_comentario = ?      \n"
                    + "	 ,a.ins_ano_ult_mod    = ?      \n"
                    + "	 ,a.ins_mes_ult_mod    = ?      \n"
                    + "	 ,a.ins_dia_ult_mod    = ?      \n"
                    + " WHERE a.ins_num_folio_inst  = ?      \n"
                    + "   AND a.ins_num_contrato    = ?      \n"
                    + "   AND a.ins_sub_contrato    = ? ";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsCveTipoInstr(), Types.VARCHAR);

            String[] datosDeMiembroAutorizador = instrucFide.getInstrucFideInsNomMiembro().split("/");
            sqlParam++;
            pstmt.setObject(sqlParam, new Long(datosDeMiembroAutorizador[0]), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, datosDeMiembroAutorizador[1], Types.VARCHAR);

            String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().replace("\r\n", "\n");
            sqlParam++;
//            pstmt.setObject(sqlParam, instrucFideInsTxtComentario, Types.LONGNVARCHAR); 
            pstmt.setString(sqlParam, instrucFideInsTxtComentario);
            // Integer largo = instrucFideInsTxtComentario.length(); //CAVC
            pstmt.setInt(5, calendario.get(Calendar.YEAR));
            pstmt.setInt(6, calendario.get(Calendar.MONTH) + 1);
            pstmt.setInt(7, calendario.get(Calendar.DAY_OF_MONTH));

            pstmt.setObject(8, instrucFide.getInstrucFideInsNumFolioInst(), Types.BIGINT);
            pstmt.setObject(9, instrucFide.getInstrucFideInsNumContrato(), Types.BIGINT);
            pstmt.setObject(10, instrucFide.getInstrucFideInsSubContrato(), Types.INTEGER);

            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            valorRetorno = Boolean.FALSE;
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_ActualizaRegistro()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_ActualizaRegistro()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_GrabaBitacoraUsuario(InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            /**
             * ********************************************************************************************************
             * Grabar Registro en la Bitacora de Usuario
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "INSERT INTO BIT_USUARIO               \n"
                    + "            (BIT_FEC_TRANSAC          \n"
                    + "            ,BIT_ID_TERMINAL          \n"
                    + "            ,BIT_NUM_USUARIO          \n"
                    + "            ,BIT_NOM_PGM              \n"
                    + "            ,BIT_CVE_FUNCION          \n"
                    + "            ,BIT_DET_BITACORA)        \n"
                    + "     VALUES                           \n"
                    + "             (CURRENT_TIMESTAMP       \n"
                    + "             ,?                       \n"
                    + "             ,?                       \n"
                    + "             ,?                       \n"
                    + "             ,?                       \n"
                    + "             ,?)";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            /*1*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideBitTerminal(), Types.VARCHAR);
            sqlParam++;
            /*2*/
            pstmt.setObject(sqlParam, new Long(instrucFide.getInstrucFideBitUsuario()), Types.BIGINT);
            sqlParam++;
            /*3*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideBitPantalla(), Types.VARCHAR);
            sqlParam++;
            /*4*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideBitTipoOperacion(), Types.VARCHAR);
            sqlParam++;
            /*5*/
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideBitDetalleBitacora(), Types.VARCHAR);

            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            valorRetorno = Boolean.FALSE;
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_GrabarBitacoraUsuario()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabarBitacoraUsuario()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_GrabaAgenda(String expNumExpediente, InstruccionFideicomisoBean instrucFide) throws SQLException {
        try {
            calendario.setTime(formatDate());
            Calendar calendarFechaDeInstruccion = Calendar.getInstance();

            if (instrucFide.getInstrucFideFechaDeInstruccion() == null) {
                calendarFechaDeInstruccion.setTime(new java.sql.Date(formatFechaParse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime()));
            } else {
                calendarFechaDeInstruccion.setTime(instrucFide.getInstrucFideFechaDeInstruccion());
            }
            Integer instruccFolioAgenda = this.onInstruccionesFideicomiso_AsignaFolio(1);

            Integer numEjecutivoAgenda = 0;
            String nomEjecutvoAgenda = "";
            valorRetorno = this.onInstruccionesFideicomiso_BuscaEjecutivoAgenda(instrucFide.getInstrucFideInsNumContrato(), "ABOGADO", numEjecutivoAgenda, nomEjecutvoAgenda);

            /**
             * ********************************************************************************************************
             * Grabar Registro en la Agenda
             * *********************************************************************************************************
             */
            sqlParam = 0;
            //efp
            sqlComando = "INSERT INTO AGENDA                         \n"
                    + "		 (AGE_NUM_FOLIO                 \n"
                    + "		 ,AGE_NUM_CONTRATO              \n"
                    + "		 ,AGE_SUB_CONTRATO              \n"
                    + "		 ,AGE_DES_INSTRUC               \n"
                    + "		 ,AGE_NUM_USUARIO               \n"
                    + "		 ,AGE_TEXT_INST                 \n"
                    + "		 ,AGE_CVE_PERIO_INST            \n"
                    + "		 ,AGE_CVE_INST_SIS              \n"
                    + "		 ,AGE_INSTRUC_ORIGEN            \n"
                    + "		 ,AGE_NUM_EVENTOS               \n"
                    + "		 ,AGE_EVENTOS_TRANS             \n"
                    + "		 ,AGE_ANO_INI_INST              \n"
                    + "		 ,AGE_MES_INI_INST              \n"
                    + "		 ,AGE_DIA_INI_INST              \n"
                    + "		 ,AGE_ANO_FIN_INST              \n"
                    + "		 ,AGE_MES_FIN_INST              \n"
                    + "		 ,AGE_DIA_FIN_INST              \n"
                    + "		 ,AGE_ANO_PROX_APLI             \n"
                    + "		 ,AGE_MES_PROX_APLI             \n"
                    + "		 ,AGE_DIA_PROX_APLI             \n"
                    + "		 ,AGE_CVE_ST_INST               \n"
                    + "		 ,AGE_RESP_ATENCION)            \n"
                    + "	  VALUES                                \n"
                    + "		 (?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,-1                            \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,?                             \n"
                    + "		 ,'PENDIENTE'                   \n"
                    + "		 ,' ')";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            sqlParam++;
            pstmt.setObject(sqlParam, instruccFolioAgenda, Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumContrato(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsSubContrato(), Types.INTEGER);
            sqlParam++;
            pstmt.setObject(sqlParam, "Instruccion Administrativa", Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, numEjecutivoAgenda, Types.BIGINT);
            sqlParam++;
            Integer endLength = instrucFide.getInstrucFideInsTxtComentario().length() > 32700 ? 32700 : instrucFide.getInstrucFideInsTxtComentario().length();
            String instrucFideInsTxtComentario = instrucFide.getInstrucFideInsTxtComentario().substring(0, endLength);
            pstmt.setObject(sqlParam, instrucFideInsTxtComentario, Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, "UNICA VEZ", Types.VARCHAR);
            sqlParam++;
            pstmt.setObject(sqlParam, instrucFide.getInstrucFideInsNumFolioInst(), Types.BIGINT);
            sqlParam++;
            pstmt.setObject(sqlParam, 1, Types.INTEGER);
            sqlParam++;
            pstmt.setObject(sqlParam, 0, Types.INTEGER);
            pstmt.setInt(11, calendarFechaDeInstruccion.get(Calendar.YEAR));
            pstmt.setInt(12, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
            pstmt.setInt(13, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));
            pstmt.setInt(14, calendarFechaDeInstruccion.get(Calendar.YEAR));
            pstmt.setInt(15, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
            pstmt.setInt(16, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));
            pstmt.setInt(17, calendarFechaDeInstruccion.get(Calendar.YEAR));
            pstmt.setInt(18, calendarFechaDeInstruccion.get(Calendar.MONTH) + 1);
            pstmt.setInt(19, calendarFechaDeInstruccion.get(Calendar.DAY_OF_MONTH));

            pstmt.execute();

            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;

        } catch (SQLException Err) {
            valorRetorno = Boolean.FALSE;

            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_GrabaAgenda()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Integer onInstruccionesFideicomiso_AsignaFolio(Integer tipoFolio) throws SQLException {
        Integer numFolio = 0;
        try {
            sqlComando = "{CALL DB2FIDUC.SP_GETFOLIO(?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setInt("PI_TIPOFOLIO", tipoFolio);
            cstmt.registerOutParameter("PI_NUM_FOLIO", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT");

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                numFolio = cstmt.getInt("PI_NUM_FOLIO");
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }

            cstmt.close();
            conexion.close();

        } catch (SQLException Err) {

            logger.error(Err.getMessage() + nombreObjeto + "onCancelaSaldo_Ejecuta()");
        } finally {

            onCierraConexion();
        }
        return numFolio;
    }

    public Boolean onInstruccionesFideicomiso_BorrarInstruccionFideicomiso(Long instrucFideInsNumContrato,
            Integer instrucFideInsSubContrato,
            Long instrucFideInsNumFolioInst,
            String instrucFideExdIdDocumento) throws SQLException {
        valorRetorno = Boolean.FALSE;
        try {
            sqlComando = "DELETE                          \n"
                    + "  FROM INSTRUCC a               \n"
                    + " WHERE a.INS_NUM_CONTRATO   = ? \n"
                    + "   AND a.INS_SUB_CONTRATO   = ? \n"
                    + "   AND a.INS_NUM_FOLIO_INST = ?";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setLong(1, instrucFideInsNumContrato);
            pstmt.setInt(2, instrucFideInsSubContrato);
            pstmt.setLong(3, instrucFideInsNumFolioInst);
            pstmt.execute();
            //int reg1 = CAVC ->
            pstmt.getUpdateCount();
            
            sqlComando = " DELETE                             \n"
                    + "   FROM expdocts a                  \n"
                    + "  WHERE a.EXD_NUM_EXPEDIENTE = ?    \n"
                    + "    AND a.EXD_NUM_DOCUMENTO 	= 999 \n"
                    + "    AND a.EXD_ID_DOCUMENTO 	= ?";

            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "Fid" + instrucFideInsNumContrato);
            pstmt.setString(2, instrucFideExdIdDocumento);
            pstmt.execute();

            valorRetorno = Boolean.TRUE;
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_BorrarInstruccionFideicomiso()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_BorrarInstruccionFideicomiso()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    public Boolean onInstruccionesFideicomiso_BorrarInstruccionMonLiq(Long instrucFideInsNumContrato,
            Integer instrucFideInsSubContrato,
            Long instrucFideInsNumFolioInst,
            String instrucFideExdIdDocumento,
            Integer dia,
            Integer mes,
            Integer ano) throws SQLException {
        valorRetorno = Boolean.FALSE;
        try {
            sqlComando = "DELETE                          \n"
                    + "   FROM DETLIQUI a               \n"
                    + " WHERE a.DEL_FOLIO_OPER_SIS   = ? \n";

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);

            pstmt.setLong(1, instrucFideInsNumFolioInst);
            pstmt.execute();
            int reg1 = pstmt.getUpdateCount();
            if (reg1 > 0) {
                mensaje = "Las liquidaciones ya fueron aplicadas contablemente, solicitar la cancelación de las operaciones de liquidación";
            }

            sqlComando = "DELETE FROM PARPABEX WHERE PBX_NUM_CONTRATO = ? \n"
                    + " and PBX_sub_contrato = ? \n"
                    + " and PBX_NUM_FOLIO_INST = ? \n";

            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, instrucFideInsNumContrato);
            pstmt.setInt(2, instrucFideInsSubContrato);
            pstmt.setLong(3, instrucFideInsNumFolioInst);
            pstmt.execute();

            sqlComando = "DELETE FROM PARPATEX WHERE PTX_NUM_CONTRATO = ? \n"
                    + " and PTX_sub_contrato =  ? \n"
                    + " and PTX_NUM_FOLIO_INST = ? \n";

            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, instrucFideInsNumContrato);
            pstmt.setInt(2, instrucFideInsSubContrato);
            pstmt.setLong(3, instrucFideInsNumFolioInst);
            pstmt.execute();

            sqlComando = "DELETE FROM AGENDA WHERE AGE_INSTRUC_ORIGEN =  ? \n"
                    + " and  AGE_NUM_CONTRATO = ? \n"
                    + " and  AGE_ano_ini_inst = ? \n"
                    + " and  AGE_mes_ini_inst =  ? \n"
                    + " and  AGE_dia_ini_inst  = ? \n";

            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, instrucFideInsNumFolioInst);
            pstmt.setLong(2, instrucFideInsNumContrato);
            pstmt.setInt(3, ano);
            pstmt.setInt(4, mes);
            pstmt.setInt(5, dia);
            pstmt.execute();

            sqlComando = "UPDATE  TPENDIENTES SET TPD_STATUS  = 'CANCELADO' \n"
                    + " where  TPD_NUM_FISO = ? \n"
                    + " and  TPD_SUB_FISO = ? \n"
                    + " and  TPD_NUM_INSTR =  ? \n"
                    + " and  TPD_STATUS = 'ACT' \n";

            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setLong(1, instrucFideInsNumContrato);
            pstmt.setInt(2, instrucFideInsSubContrato);
            pstmt.setLong(3, instrucFideInsNumFolioInst);
            pstmt.execute();

            valorRetorno = Boolean.TRUE;
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            //mensajeError+= "Descripción: " + Err.getMessage() + nombreObjeto + "onInstruccionesFideicomiso_BorrarInstruccionFideicomiso()";
            logger.error(Err.getMessage() + "onInstruccionesFideicomiso_BorrarInstruccionFideicomiso()");
        } finally {

            onCierraConexion();
        }
        return valorRetorno;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onCierraConexion() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar ResultSet.");
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar PreparetStatement.");
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar CallableStatement.");
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar Connection.");
            }
        }
    }

    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
