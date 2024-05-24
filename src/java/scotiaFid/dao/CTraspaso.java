/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CTraspaso.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package 
package scotiaFid.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.BitacoraBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.RequestTraspasoBean;
import scotiaFid.bean.ResponseTraspasoBean;
import scotiaFid.bean.TransferChqBean;
import scotiaFid.service.SvtTransferTraspaso;
import scotiaFid.singleton.DataBaseConexion;

//Class
public class CTraspaso implements Serializable {

    private static final Logger logger = LogManager.getLogger(CTraspaso.class);
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    private static final long serialVersionUID = 1L;

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    private RequestTraspasoBean requestTraspasoBean = new RequestTraspasoBean();
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    private ResponseTraspasoBean responseTraspasoBean = new ResponseTraspasoBean();
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    private SvtTransferTraspaso svtTransferTraspaso = new SvtTransferTraspaso();

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * V A R I A B L E S * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    CTesoreria cTesoreria = new CTesoreria();
    // -----------------------------------------------------------------------------
    private String mensajeError;
    // **************************************************************************** 
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * C O N S T A N T E S * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    private static final String OK = "0";

    private static final String nombreObjeto = "Fuente: scotiafid.dao.CTraspaso.";

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    @PostConstruct
    public void init() {
        mensajeError = "Error En Tiempo de Ejecución: ";
    }

    public ResponseTraspasoBean onTraspaso_traspasoFondos(TransferChqBean fDTransOrigen, String URL, String XML) {

        //Establece la dirección URL y XML del servicio
        requestTraspasoBean.setUrl(URL);
        requestTraspasoBean.setXml(XML);

        // Execute_Transfer.
        onTraspaso_ejecutaTraspaso(fDTransOrigen);

        return responseTraspasoBean;
    }

    private boolean onTraspaso_ejecutaTraspaso(TransferChqBean fdTransCheq) {

        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        boolean bTranferencia = true;
        StringBuilder stringBuilder = new StringBuilder();

        //Obtiene_la_Cuenta_Origen_a_Traspasar.
        stringBuilder.append("SELECT ADC_CUENTA, ADC_PLAZA FROM SAF.FID_CTAS WHERE ADC_CUENTA = ? AND ADC_NUM_CONTRATO = ? ");

        try {
            // Call_Operaciones_BD 
            conexion = DataBaseConexion.getInstance().getConnection();;
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values 
            preparedStatement.setString(1, String.format("%011d", Integer.parseInt(fdTransCheq.getCta_Origen())));
            preparedStatement.setInt(2, Integer.parseInt(fdTransCheq.getReferencia()));

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Get_Info_Request
            if (resultSet.next()) {
                //1.-_Cuenta_Cargo
                requestTraspasoBean.setCuentaCargo(String.format("%03d", resultSet.getInt("ADC_PLAZA")).concat(String.format("%08d", Integer.parseInt(resultSet.getString("ADC_CUENTA")))));
            } else {
                bTranferencia = false;
            }

            //2.-_Cuenta_Abono
            requestTraspasoBean.setCuentaAbono(String.format("%03d", resultSet.getInt("ADC_PLAZA")).concat(String.format("%08d", Integer.parseInt(fdTransCheq.getCta_Destino()))));

            //3.-_Importe
            requestTraspasoBean.setImporte(String.valueOf(fdTransCheq.getImporte()));

            //4.-_Referencia
            requestTraspasoBean.setReferencia(fdTransCheq.getReferencia());

            //5.-_Usuario
            requestTraspasoBean.setUsuario(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString());

            //Call_Transfer
            if (bTranferencia) {
                responseTraspasoBean = svtTransferTraspaso.transfer(requestTraspasoBean);
            } else {
                bTranferencia = false;
            }

            if("Error".equals(responseTraspasoBean.getMsjError())) {
            	responseTraspasoBean.setMsjError("Falló la comunicación con el servicio de Traspaso de Cheques.");
            	responseTraspasoBean.setEstatus("-1");
                onTraspaso_UpdateFallaSW(fdTransCheq); 
                bTranferencia = false;
            }
            
            if (OK.equals(responseTraspasoBean.getEstatus()) && bTranferencia == true) {

                onTraspaso_UpdateConfirmaSW(fdTransCheq);
                //Return_Method
                bTranferencia = true;
            } else {
                onTraspaso_UpdateFallaSW(fdTransCheq);
                bTranferencia = false;
            }

        } catch (SQLException | IOException Err) {
            logger.error(Err.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar ResultSet");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }

        //Return_Method
        return bTranferencia;
    }

    public List<String> onTraspaso_Select_URLXML() {
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        List<String> dirSW = new ArrayList<>();

        try {

            // Call_Operaciones_BD 
            conexion = DataBaseConexion.getInstance().getConnection();

            //Call_URL_WebService
            preparedStatement = conexion.prepareStatement("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID IN (?, ?)  ORDER BY XML_ID");

            //Set_Values  
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, 2);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    // 6.-_URL
                    // 7.-_XML 
                    dirSW.add(resultSet.getString("CADENA_XML"));
                }
            } else {
                responseTraspasoBean.setMsjError("La URL/XML del Servicio XferOwnAdd es nula");
            }

        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar ResultSet");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }

        return dirSW;
    }

    public List<TransferChqBean> onTraspaso_SelectTrasferChequera(int iFolio, int iRegistros) {

        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        int cont = 0;

        // Objects 
        StringBuilder stringBuilder = new StringBuilder();
        List<TransferChqBean> datosChequera = new ArrayList<>();

        try {
            // SQL 
            stringBuilder.append("SELECT TPD_FOLIO, DETM_FOLIO_OP, 	TRAN_FECHA_MOV,  TRAN_CTA_ORIGEN,");
            stringBuilder.append("TRAN_CTA_DESTINO,	TRAN_MONEDA,  	TRAN_IMPORTE,    TRAN_REFERENCIA ");
            stringBuilder.append("FROM SAF.FDTRANSFER ");
            stringBuilder.append("WHERE TPD_FOLIO = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();;
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, iFolio);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    TransferChqBean detalleTransferChqBean = new TransferChqBean();

                    cont++;
                    detalleTransferChqBean.setFolio(resultSet.getInt("TPD_FOLIO"));
                    detalleTransferChqBean.setFolio_Op(resultSet.getInt("DETM_FOLIO_OP"));
                    detalleTransferChqBean.setFecha_Mov(resultSet.getDate("TRAN_FECHA_MOV"));
                    detalleTransferChqBean.setCta_Origen(String.format("%08d", resultSet.getInt("TRAN_CTA_ORIGEN")));
                    detalleTransferChqBean.setCta_Destino(String.format("%08d", resultSet.getInt("TRAN_CTA_DESTINO")));
                    detalleTransferChqBean.setMoneda(resultSet.getInt("TRAN_MONEDA"));
                    detalleTransferChqBean.setImporte(resultSet.getDouble("TRAN_IMPORTE"));
                    detalleTransferChqBean.setReferencia(resultSet.getString(("TRAN_REFERENCIA")));

                    datosChequera.add(detalleTransferChqBean);
                }
            }

            if (cont != iRegistros) {
                FacesContext.getCurrentInstance().addMessage("Fiduciario", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario",
                        "La cantidad de Registros a Transferir no conciden, se transferiran " + cont + " de " + iRegistros));
            }

        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar ResultSet");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }

        return datosChequera;
    }

    public OutParameterBean onTraspaso_CancelaFolio(double iFolio) {
        // Objects
        Connection conexion = null;
        CallableStatement callableStatement = null;

        OutParameterBean outParameterBean = new OutParameterBean();
        BitacoraBean bitacoraBean = new BitacoraBean();

        try {
            // SQL
            String sql = "{CALL DB2FIDUC.SP_CANCELA_FOLIO (?,?,?,?,?)}";

            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();;
            callableStatement = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            callableStatement.setDouble(1, iFolio);
            callableStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));

            // REGISTER_OUT_PARAMETER  
            callableStatement.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            callableStatement.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            callableStatement.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_CALLABLESTATEMENT
            callableStatement.execute();

            // GET_OUT_PARAMETER
            int sCode = callableStatement.getInt("PI_SQLCODE_OUT");
            String sState = callableStatement.getString("PCH_SQLSTATE_OUT");
            String sMsErr = callableStatement.getString("PS_MSGERR_OUT");

            if (sMsErr.trim().length() > 0) {
                outParameterBean.setbEjecuto(0);
                outParameterBean.setPsMsgErrOut("Error al Cancelar el folio: ".concat(String.valueOf(iFolio)));
            } else {
                outParameterBean.setbEjecuto(1);
                outParameterBean.setPsMsgErrOut("");
            }

            // SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal").toString());
            bitacoraBean.setNombre("CANCELA_FOLIO".toUpperCase());
            bitacoraBean.setFuncion("CANCELA_FOLIO".toUpperCase());
            bitacoraBean.setDetalle("Finalizo incorrectamente la Cancelación del Folio: " + iFolio + ", con el error: " + sMsErr + " sCode: " + sCode + " sState: " + sState);

            // INSERT_BITACORA
            cTesoreria.onTesoreria_InsertBitacora(bitacoraBean);

        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {

            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar CallableStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }

        return outParameterBean;
    }

    public boolean onTraspaso_CancelaFolioStatus(double iFolio) {
        // Objects 
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        boolean iResultado = true;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            //AGREGA CONFIRMACION
            stringBuilder.append("UPDATE SAF.DETLIQUI SET DEL_CVE_ST_DETLIQU = 'CANCELADO' WHERE DEL_FOLIO_OPERA = ? ");

            // Call_Operaciones_BD 
            conexion = DataBaseConexion.getInstance().getConnection();;
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            preparedStatement.setDouble(1, iFolio);

            // Execute_Update
            preparedStatement.executeUpdate();
 
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
            iResultado = false;
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }

        return iResultado;
    }

    public void onTraspaso_UpdateConfirmaSW(TransferChqBean fdTransCheq) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilderTransferSource = new StringBuilder();

        try {
            //AGREGA CONFIRMACION
            stringBuilderTransferSource.append("UPDATE SAF.FDTRANSFER SET TRAN_CONFIRMACION = ?, TRAN_FECHA_HORA = CURRENT TIMESTAMP WHERE TPD_FOLIO = ? AND DETM_FOLIO_OP = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilderTransferSource.toString());

            // Set_Values
            preparedStatement.setString(1, responseTraspasoBean.getFolioConfirmacion());
            preparedStatement.setInt(2, fdTransCheq.getFolio());
            preparedStatement.setDouble(3, fdTransCheq.getFolio_Op());

            // Execute_Update
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            mensajeError += "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onTraspaso_UpdateConfirmaSW()");
            logger.error(mensajeError);
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }
    }

    public void onTraspaso_UpdateFallaSW(TransferChqBean fdTransCheq) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        StringBuilder stringBuilderTransferSource = new StringBuilder();

        try {
        	//Recupera el mensaje de Error del catálogo de WebService 
        	if(!"-1".equals(responseTraspasoBean.getEstatus())) { 
        		responseTraspasoBean.setMsjError(Consulta_CatErrWebService(Integer.parseInt(responseTraspasoBean.getEstatus()))); 
        	}
        	
            //Imprime_Error
            stringBuilderTransferSource.append("UPDATE SAF.FDTRANSFER SET TRAN_MSG_ERR = ?, TRAN_FECHA_HORA = CURRENT TIMESTAMP,  TRAN_CONFIRMACION = null WHERE TPD_FOLIO = ? AND DETM_FOLIO_OP = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilderTransferSource.toString());

            // Set_Values			             
            preparedStatement.setString(1,responseTraspasoBean.getMsjError());
            preparedStatement.setInt(2, fdTransCheq.getFolio());
            preparedStatement.setDouble(3, fdTransCheq.getFolio_Op());

            // Execute_Update
            preparedStatement.executeUpdate();

        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        }
    }
    
    private String Consulta_CatErrWebService(int IdErr) {
    	String sMensajeWeb="";
        Connection conexion = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null; 

        // Objects 
        StringBuilder stringBuilder = new StringBuilder(); 

        try {
            // SQL 
            stringBuilder.append("SELECT ERR_DESC_PANTALLA FROM SAF.CAT_ERR_WEBSERVICE WHERE ERR_CODIGO = ? AND ERR_STATUS = 'ACTIVO' ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();;
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Parameters
            preparedStatement.setInt(1, IdErr);

            // Execute_Query
            resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                	sMensajeWeb = resultSet.getString(("ERR_DESC_PANTALLA"));
                }
            } 

        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar ResultSet");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar PreparedStatement");
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException sqlExc) {
                    logger.error("Error al cerrar la Conexion");
                }
            }
        } 
        
        return sMensajeWeb;
    } 
}
