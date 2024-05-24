/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : DepositTranInqByAcctId.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.service
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import scotiaFid.bean.CtasSaldos;
import scotiaFid.util.CValidacionesUtil;

import scotiaFid.bean.RequestMonitorBean;
import scotiaFid.bean.ResponseMonitorBean;
import scotiaFid.singleton.DataBaseConexion;

public class DepositTranInqByAcctId {

    private static final Logger logger = LogManager.getLogger(DepositTranInqByAcctId.class);
    private static final String XML_XXE = "http://apache.org/xml/features/disallow-doctype-decl";
    private String mensajeError;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	* O B J E T O S
	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Connection conexion;

    public ResponseMonitorBean requestTransaction(RequestMonitorBean requestMonitorBean) throws ParserConfigurationException, SAXException, SQLException, IOException {
        //Variables
        List<ResponseMonitorBean> responses = new ArrayList<>();
        ResponseMonitorBean responseMonitor1Bean = new ResponseMonitorBean();
        String xml = "";
        String responseStatus = "";
        String inputLine = "";
        String urlAcctTrnInquiry = "";
        String xmlAcctTrnInquiry = "";
        String startRecKey = "000000000000000000000000000000";
        boolean pagination = true;

        LocalDate localDate = LocalDate.now();
        StringBuilder stringBuilder = new StringBuilder();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;

        stringBuilder.append("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");

        conexion = DataBaseConexion.getInstance().getConnection();
        preparedStatement = conexion.prepareStatement(stringBuilder.toString());

        preparedStatement.setInt(1, 3);

        //Execute_Query
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            //URL
            urlAcctTrnInquiry = resultSet.getString("CADENA_XML");
        }

        preparedStatement2 = conexion.prepareStatement("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");

        preparedStatement2.setInt(1, 4);

        resultSet2 = preparedStatement2.executeQuery();

        if (resultSet2.next()) {
            xmlAcctTrnInquiry = resultSet2.getString("CADENA_XML");
        }

        try {
            URL url = new URL(urlAcctTrnInquiry);

            do {
                xml = xmlAcctTrnInquiry;

                //Set_HttpURLConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader_1 = null;
                BufferedReader bufferedReader = null;
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

                    //Set_DataOutputStream
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                    //Set_Properties
                    dataOutputStream.writeBytes(xml);
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    //Response_Status
                    responseStatus = httpURLConnection.getResponseMessage();

                    if ("Error".equals(responseStatus)) {
                        mensajeError = "Servicio Error";
                        return responseMonitor1Bean;
                    }

                    //Create_BufferedReader
                    bufferedReader_1 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    //Response_StringBuffer
                    StringBuffer stringBuffer = new StringBuffer();

                    //Write_Response
                    while ((inputLine = bufferedReader_1.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }

                    //Close_BufferedReader
                    bufferedReader_1.close();

                    //Get_Element_XML_Response
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setFeature(XML_XXE, true);
                    factory.setNamespaceAware(true);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    builder.isValidating();

                    builder.setEntityResolver(new EntityResolver() {
                        //_Dummi resolver - alvays do nothing            
                        public InputSource resolveEntity(String publicId, String systemId)
                                throws SAXException, IOException {
                            return new InputSource(new StringReader(""));
                        }

                    });
                    Document document = builder.parse(new InputSource(new StringReader(stringBuffer.toString())));

                    //Get_Node_Status
                    Node node = document.getElementsByTagNameNS("*", "StatusCode").item(0);

                    //Validate_Node_Status
                    if (node == null) {
                        //Print_Console
                        logger.info("No se puede obtener el nodo: <core:StatusCode>");
                        //Throw_Exception
                        throw new SAXException("No se puede obtener el nodo: <core:StatusCode>");
                    } else {
                        //Set_Node_Status
                        responseMonitor1Bean.setEstatus(node.getTextContent());
                    }

                    //Get_Node_LastRecKey
                    node = document.getElementsByTagNameNS("*", "LastRecKey").item(0);

                    //Validate_Node_LastRecKey
                    if (node == null) {
                        //Print_Console
                        logger.info("No se puede obtener el nodo: <core:LastRecKey>");
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
                        //Get_Element_AcctTranInfo
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            //Get_Node_Item
                            Node nodeItem = nodeList.item(i);
                            //Instance_Object_Reponse
                            ResponseMonitorBean responseMonitorBean = new ResponseMonitorBean();
                            //Get_NodeListChild_AcctTranInfo
                            NodeList nodeListChild = nodeItem.getChildNodes();

                            //Get_Element_Child
                            for (int y = 0; y < nodeListChild.getLength(); y++) {
                                Node nodeChild = nodeListChild.item(y);

                                if ("OUT_ws_s_imp_oper".contains(nodeChild.getLastChild().getTextContent())) {
                                    if ("CurAmt".equals(nodeChild.getFirstChild().getLocalName())) {
                                        //Print_Console 
                                        logger.info("<core:Amt>" + CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100 + "</core:Amt>");
                                        //Set_Value_Monto
                                        responseMonitorBean.setMonto(String.valueOf(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100).toString());
                                        responseMonitorBean.setMontoFormat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100));
                                    }
                                }

                                if ("OUT_ws_s_cat_hist_saldo".contains(nodeChild.getLastChild().getTextContent())) {
                                    if ("CurAmt".equals(nodeChild.getFirstChild().getLocalName())) {
                                        //Print_Console
                                        logger.info("<core:Amt:Hist>" + CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100 + "</core:Amt:Hist>");
                                        //Set_Value_Saldo
                                        responseMonitorBean.setSaldo(String.valueOf(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100).toString());
                                        responseMonitorBean.setSaldoFormat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(nodeChild.getFirstChild().getTextContent()) / 100));
                                    }
                                }

                                if ("TrnType".equals(nodeChild.getLocalName())) {
                                    //Set_Value_Tipo_Transaccion
                                    responseMonitorBean.setTipoTransaccion(nodeChild.getTextContent());
                                }

                                if ("TranDesc".equals(nodeChild.getLocalName())) {
                                    //Set_Value_Descripcion
                                    responseMonitorBean.setDescripcion(nodeChild.getTextContent());
                                }

                                if ("ConfirmationId".equals(nodeChild.getFirstChild().getLocalName()) && !"CR".contains(nodeChild.getFirstChild().getTextContent())) {
                                    //Set_Value_Confirmacion
                                    responseMonitorBean.setConfirmacion(nodeChild.getFirstChild().getTextContent());
                                }

                                if ("OUT-REFERENCIA".contains(nodeChild.getFirstChild().getTextContent())) {
                                    if ("RefId".equals(nodeChild.getLastChild().getLocalName())) {
                                        //Set_Value_RefId
                                        responseMonitorBean.setReferencia(nodeChild.getLastChild().getTextContent().substring(nodeChild.getLastChild().getTextContent().length() - 10));
                                    }
                                }

                                //Set_Value_Estatus
                                responseMonitorBean.setEstatus("/vista/recursos/img/png/red_circle.png");

                                //Set_Value_Aplicado
                                responseMonitorBean.setAplicado("NO");

                                //Set_Cuenta
                                responseMonitorBean.setCuenta(requestMonitorBean.getCuenta());
                            }

                            //Add_Response
                            responses.add(responseMonitorBean);
                        }
                    }
                } catch (IOException e) {
                    //Create_BufferedReader
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));

                    //Response
                    StringBuffer stringBuffer = new StringBuffer();

                    //Write_Response
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }

                    //Close_BufferedReader
                    bufferedReader.close();

                    //Print_Console_Response
                    logger.info("Response: ".concat(stringBuffer.toString()));
                } finally {
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    if (bufferedReader_1 != null) {
                        try {
                            bufferedReader_1.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }

                //Set_Element_List
                responseMonitor1Bean.setLstResponse(responses);

                //Validate_Pagination
            } while (pagination);

        } catch (MalformedURLException e) {
            //Write_Log
            logger.info(" Error: ".concat(e.getMessage()));
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }

            if (resultSet2 != null) {
                resultSet2.close();
            }

            if (preparedStatement2 != null) {
                preparedStatement2.close();
            }

            if (conexion != null) {
                conexion.close();
            }
        }

        //Print_Console
        logger.info("STAR_DATE" + localDate.format(dateTimeFormatter).concat("T12:00:00"));
        logger.info("END_DATE" + localDate.format(dateTimeFormatter).concat("T12:00:00"));

        //Return_method
        return responseMonitor1Bean;
    }

    public CtasSaldos requestAcounts(RequestMonitorBean requestMonitorBean) throws SQLException, IOException, ParserConfigurationException, SAXException {
        //Variables
        CtasSaldos ctasSaldos = new CtasSaldos();
        String xml = "";
        String inputLine = "";
        String urlAcctTrnInquiry = "";
        String xmlAcctTrnInquiry = "";

        StringBuilder stringBuilder = new StringBuilder();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
            stringBuilder.append("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");

            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, 7);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //URL
                urlAcctTrnInquiry = resultSet.getString("CADENA_XML");
            }

            preparedStatement2 = conexion.prepareStatement("SELECT CADENA_XML FROM SAF.FD_XML_WS_CHEQUES WHERE XML_ID = ? ");

            preparedStatement2.setInt(1, 8);

            resultSet2 = preparedStatement2.executeQuery();

            if (resultSet2.next()) {
                xmlAcctTrnInquiry = resultSet2.getString("CADENA_XML");
            }

            try {
                URL url = new URL(urlAcctTrnInquiry);

                xml = xmlAcctTrnInquiry;

                //Set_HttpURLConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader_1 = null;
                BufferedReader bufferedReader = null;
                try {
                    //Set_Properties
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
                    httpURLConnection.setDoOutput(true);

                    //Replace_Values_XML
                    xml = xml.replace("ACCT_ID", requestMonitorBean.getCuenta());

                    //Set_DataOutputStream
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                    //Set_Properties
                    dataOutputStream.writeBytes(xml);
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    //Create_BufferedReader
                    bufferedReader_1 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    //Response_StringBuffer
                    StringBuffer stringBuffer = new StringBuffer();

                    //Write_Response
                    while ((inputLine = bufferedReader_1.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }

                    //Close_BufferedReader
                    bufferedReader_1.close();

                    //Get_Element_XML_Response
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setFeature(XML_XXE, true);
                    factory.setNamespaceAware(true);

                    DocumentBuilder builder = factory.newDocumentBuilder();
                    builder.isValidating();

                    builder.setEntityResolver(new EntityResolver() {
                        // Dummi resolver - alvays do nothing            
                        public InputSource resolveEntity(String publicId, String systemId)
                                throws SAXException, IOException {
                            return new InputSource(new StringReader(""));
                        }

                    });
                    Document document = builder.parse(new InputSource(new StringReader(stringBuffer.toString())));

                    //Get_Node_Status
                    Node node = document.getElementsByTagNameNS("*", "StatusCode").item(0);

                    //Validate_Node_Status
                    if (node == null) {
                        //Print_Console
                        logger.info("No se puede obtener el nodo: <core:StatusCode>");
                        //Throw_Exception
                        throw new SAXException("No se puede obtener el nodo: <core:StatusCode>");
                    }

                    //CurAmt
                    node = document.getElementsByTagNameNS("*", "CurAmt").item(0);

                    //Validate_Node_LastRecKey
                    if (node == null) {
                        //Print_Console
                        logger.info("No se puede obtener el nodo: <core:AcctBal>");
                        ctasSaldos.setCtaNumero(null);
                        ctasSaldos.setCtaSaldo(null);

                    } else {
                        //Set_Value_LastRecKey
                        if ("CurAmt".equals(node.getLocalName())) {
                            //Get_Value_LastRecKey	
                            ctasSaldos.setCtaNumero(requestMonitorBean.getCuenta());
                            ctasSaldos.setCtaSaldo(CValidacionesUtil.validarDouble(node.getTextContent()));
                            if (!ctasSaldos.getCtaSaldo().equals(0)) {
                                ctasSaldos.setCtaSaldo(ctasSaldos.getCtaSaldo() / 100);
                            }
                        }
                    }

                    //Get all ConfirmationId
                } catch (IOException e) {

                    //Create_BufferedReader
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));

                    //Response
                    StringBuffer stringBuffer = new StringBuffer();

                    //Write_Response
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }

                    //Close_BufferedReader
                    bufferedReader.close();

                    //Print_Console_Response
                    logger.info("Response: ".concat(stringBuffer.toString()));
                } finally {
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    if (bufferedReader_1 != null) {
                        try {
                            bufferedReader_1.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }

            

        } catch (MalformedURLException  e) {
            //Write_Log
            logger.info(" Error: ".concat(e.getMessage()));
        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement2 != null) {
                preparedStatement2.close();
            }
            if (resultSet2 != null) {
                resultSet2.close();
            }

            if (conexion != null) {
                conexion.close();
            }
        }
        return ctasSaldos;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}