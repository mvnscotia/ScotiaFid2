/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : DepositTranInq.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.service
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.service;

//Imports
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection; 
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import scotiaFid.bean.RequestMonitorBean;
import scotiaFid.bean.ResponseMonitorBean; 
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.CValidacionesUtil;

//Class
public class DepositTranInq {

    private static final Logger logger = LogManager.getLogger(DepositTranInq.class);

    //Objects    
    private Connection conexion;
    // -----------------------------------------------------------------------------
    private final String nombreObjeto = "\\nFuente: scotiafid.service.DepositTranInq.";
    // -----------------------------------------------------------------------------
    private String mensajeError; 
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	* G E T T E R S   Y   S E T T E R S
	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    //List
    private List<ResponseMonitorBean> responses = new ArrayList<>();
    // -----------------------------------------------------------------------------
    //Methods
    public ResponseMonitorBean requestTransactionXCuenta(RequestMonitorBean requestMonitorBean, String contrato, String moneda) throws Exception {
        //Variables

        String xml = "";
        String responseStatus = "";
        String inputLine = "";
        String urlAcctTrnInquiry = "";
        String xmlAcctTrnInquiry = "";
        String startRecKey = "000000000000000000000000000000";
        boolean pagination = true;
        StringBuilder stringBuilder = new StringBuilder();        

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet2 = null;
        PreparedStatement preparedStatement2 = null;
        
        //Objects 
        LocalDate localDate = LocalDate.now();
        //Format_Date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //Instance_Object_Response
        ResponseMonitorBean responseMonitorBean = new ResponseMonitorBean();
        try {

            //Call_URL_WebService 
            stringBuilder.append("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");

            //Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values 
            preparedStatement.setInt(1, 3);
            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //URL 
                urlAcctTrnInquiry = resultSet.getString("CADENA_XML");
            }

            stringBuilder.setLength(0); 
            stringBuilder.append("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");
                        
            //Call_Operaciones_BD  
            preparedStatement2 = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values  
            preparedStatement2.setInt(1, 4);
            //Execute_Query
            resultSet2 = preparedStatement2.executeQuery();

            if (resultSet2.next()) {
                //XML 
                  xmlAcctTrnInquiry = resultSet2.getString("CADENA_XML");
            } 
            
        } catch (SQLException e) {
            //Write_Log
            logger.info(" Error: ".concat(e.getMessage()));
        } finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar ResultSet." + e.getMessage());
			}

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar PreparetStatement." + e.getMessage());
			}

			try {
				if (resultSet2 != null)
					resultSet2.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar ResultSet." + e.getMessage());
			}

			try {
				if (preparedStatement2 != null)
					preparedStatement2.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar PreparetStatement." + e.getMessage());
			}

			try {
				if (conexion != null)
					conexion.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar conexion." + e.getMessage());
			}
        }

        try {
            //Set_URL
            URL url = new URL(urlAcctTrnInquiry);

            do {
                //Get_XML
                xml = xmlAcctTrnInquiry;

                //Set_HttpURLConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                try {
                    //Set_Properties
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
                    httpURLConnection.setDoOutput(true);

                    //Replace_Values_XML
                    xml = xml.replace("ACCT_ID", requestMonitorBean.getCuenta());
                    xml = xml.replace("STAR_DATE", localDate.format(dateTimeFormatter).concat("T12:00:00"));
                    xml = xml.replace("END_DATE", localDate.format(dateTimeFormatter).concat("T12:00:00"));
                    xml = xml.replace("START_RECKEY", startRecKey);

                    //Log_Request
                    insertFDXmlWebservice(xml, "REQUEST");

                    //Set_DataOutputStream
                    try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
                        try {
                            //Set_Properties
                            dataOutputStream.writeBytes(xml);
                            dataOutputStream.flush();
                        } finally {
                            dataOutputStream.close();
                        }
                    }

                    //Response_Status
                    responseStatus = httpURLConnection.getResponseMessage();
                    
                    if("Error".equals(responseStatus)){
                       mensajeError = "Servicio Error";
                       return responseMonitorBean;
                    }

                    //Response_StringBuffer
                    StringBuffer stringBuffer = new StringBuffer();

                    //Create_BufferedReader 
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                        try {
                            //Write_Response
                            while ((inputLine = bufferedReader.readLine()) != null) {
                                stringBuffer.append(inputLine);
                            }

                        } finally {
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                        }
                    }
                    //Log_Response
                    insertFDXmlWebservice(stringBuffer.toString(), "RESPONSE");

                    //Get_Element_XML_Response
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                    factory.setNamespaceAware(true);

                    DocumentBuilder builder = factory.newDocumentBuilder();  
                    Document document = builder.parse(new InputSource(new StringReader(stringBuffer.toString())));

                    //Get_Node_Status
                    Node node = document.getElementsByTagNameNS("*", "StatusCode").item(0);

                    //Validate_Node_Status
                    if (node == null) {
                        //Throw_Exception
                        throw new Exception("No se puede obtener el nodo: <core:StatusCode>");
                    } else {
                        //Set_Node_StatusCode
                        responseMonitorBean.setCodigo(node.getTextContent());
                    }

                    //Get_Node_LastRecKey
                    node = document.getElementsByTagNameNS("*", "LastRecKey").item(0);

                    //Validate_Node_LastRecKey
                    if (node == null) {
                        //Set_Value_Pagination
                        pagination = false;
                    } else {
                        //Set_Value_LastRecKey
                        if ("LastRecKey".equals(node.getLocalName())) {
                            //Get_Value_LastRecKey	
                            startRecKey = node.getTextContent();
                        }
                    }

                    //Get all ConfirmationId
                    NodeList nodeList = document.getElementsByTagNameNS("*", "AcctTranInfo");

                    //Validate_Node_AcctTranInfo
                    if (nodeList.getLength() > 0) {
                        sig_movimiento:
                        //Get_Element_AcctTranInfo
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            //Get_Node_Item
                            Node nodeItem = nodeList.item(i);

                            //Get_NodeListChild_AcctTranInfo
                            NodeList nodeListChild = nodeItem.getChildNodes();

                            //Instance_Object_Response
                            ResponseMonitorBean responseMonitorMovementBean = new ResponseMonitorBean();

                            //Get_Element_Child
                            for (int y = 0; y < nodeListChild.getLength(); y++) {
                                //Node_Child  
                                Node nodeChild = nodeListChild.item(y);

                                if ("TrnType".equals(nodeChild.getLocalName())) {

                                    if (nodeChild.getTextContent().equals("1")) {
                                        continue sig_movimiento;
                                    }
                                }

                                if ("OUT_ws_s_imp_oper".contains(nodeChild.getLastChild().getTextContent())) {
                                    if ("CurAmt".equals(nodeChild.getFirstChild().getLocalName())) {
                                        //Set_Value_Monto
                                        responseMonitorMovementBean.setMonto(String.valueOf(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100).toString());
                                        responseMonitorMovementBean.setMontoFormat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100));
                                    }
                                }

                                if ("OUT_ws_s_cat_hist_saldo".contains(nodeChild.getLastChild().getTextContent())) {
                                    if ("CurAmt".equals(nodeChild.getFirstChild().getLocalName())) {
                                        //Set_Value_Saldo
                                        responseMonitorMovementBean.setSaldo(String.valueOf(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100).toString());
                                        responseMonitorMovementBean.setSaldoFormat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100));
                                    }
                                }

                                if ("TranDesc".equals(nodeChild.getLocalName())) {
                                    //Set_Value_Descripcion
                                    responseMonitorMovementBean.setDescripcion(nodeChild.getTextContent());
                                }

                                if ("ConfirmationId".equals(nodeChild.getFirstChild().getLocalName()) && !"CR".contains(nodeChild.getFirstChild().getTextContent())) {
                                    //Set_Value_Confirmacion
                                    responseMonitorMovementBean.setConfirmacion(nodeChild.getFirstChild().getTextContent());
                                }

                                if ("OUT-REFERENCIA".contains(nodeChild.getFirstChild().getTextContent())) {
                                    if ("RefId".equals(nodeChild.getLastChild().getLocalName())) {
                                        //Set_Value_RefId
                                        responseMonitorMovementBean.setReferencia(nodeChild.getLastChild().getTextContent().substring(nodeChild.getLastChild().getTextContent().length() - 10));
                                    }
                                }

                                //Set_Cuenta
                                responseMonitorMovementBean.setCuenta(requestMonitorBean.getCuenta());

                                //Set_Contrato
                                responseMonitorMovementBean.setContrato(contrato);

                                //Set_Moneda
                                responseMonitorMovementBean.setMoneda(moneda);
                            }

                            //Add_Response
                            responses.add(responseMonitorMovementBean);
                        }
                    }

                } catch (IOException e) {

                    logger.info(" Exception: ".concat(e.getMessage()));

                    //Response
                    StringBuffer stringBuffer = new StringBuffer();

                    //Create_BufferedReader
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()))) {
                        try {

                            //Write_Response
                            while ((inputLine = bufferedReader.readLine()) != null) {
                                stringBuffer.append(inputLine);
                            }

                        } finally {
                            if (stringBuffer != null) {
                                bufferedReader.close();
                            }
                        }
                    }
                }
                //Set_Element_List
                responseMonitorBean.setLstResponse(responses);

                //Validate_Pagination
            } while (pagination);

        } catch (SQLException e) {
            //Write_Log 
            logger.info(" Error: ".concat(e.getMessage()));
        }

        //Return_method
        return responseMonitorBean;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public int insertFDXmlWebservice(String xml, String tipoPeticion) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();
        PreparedStatement preparedStatement = null;

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Variables
        int result = 0;

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);

        try {
            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.FD_XML_WEBSERVICE (FECHA_HORA, NOM_INTERFACE, TIPO_PETICION, ID_USER, CADENA_XML)");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?) ");

            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(dateTime));
            preparedStatement.setString(2, "SW_MOVIMIENTOS_DIA");
            preparedStatement.setString(3, tipoPeticion);
            preparedStatement.setInt(4, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes());
            InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
            preparedStatement.setClob(5, inputStreamReader);

            //Execute_Update
            result = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();

        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCierreContable_GetReport()"; 
        } finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();				 
				if (conexion != null)
					conexion.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar conexion." + e.getMessage());
			}
        }
        return result;
    }

    public Boolean clearResponses() {
         responses.clear();
         return Boolean.TRUE;
    }
   
}
