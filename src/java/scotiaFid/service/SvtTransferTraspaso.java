/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : SvtTransferTraspaso.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.service
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.service;

//Imports
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
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

import scotiaFid.bean.RequestTraspasoBean;
import scotiaFid.bean.ResponseTraspasoBean;
import scotiaFid.bean.XmlWebServiceBean;
import scotiaFid.singleton.DataBaseConexion;

//Class 
public class SvtTransferTraspaso implements Serializable {

    private static final Logger logger = LogManager.getLogger(SvtTransferTraspaso.class);
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
    //private ResponseTraspasoBean responseTraspasoBean = new ResponseTraspasoBean();
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    private XmlWebServiceBean xmlWebServiceBean = new XmlWebServiceBean();
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * V A R I A B L E S * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    private Connection conexion;
    // -----------------------------------------------------------------------------
    private PreparedStatement preparedStatement;
    // -----------------------------------------------------------------------------
    private static final String nombreObjeto = "Fuente: scotiafid.service.SvtTransferTraspaso.";
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     */
    public XmlWebServiceBean getXmlWebServiceBean() {
        return xmlWebServiceBean;
    }

    public void setXmlWebServiceBean(XmlWebServiceBean xmlWebServiceBean) {
        this.xmlWebServiceBean = xmlWebServiceBean;
    }

    public ResponseTraspasoBean transfer(RequestTraspasoBean fdRequestTrans) throws FileNotFoundException, IOException {
        //Variables
        String Cadenaxml = "";
        String respEstatus = "";
        String inputLine;
        String mensajeError = "Error En Tiempo de Ejecución.";
        XmlWebServiceBean xmlWebServiceBean = new XmlWebServiceBean();
        ResponseTraspasoBean responseTraspasoBean = new ResponseTraspasoBean();

        try { 
            //Set_URL
            URL url = new URL(fdRequestTrans.getUrl());

            //Set_HttpURLConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            try {
                //Set_Properties
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
                httpURLConnection.setDoOutput(true);

                //Set_XML
                Cadenaxml = fdRequestTrans.getXml();

                //Set_Values_XML
                Cadenaxml = Cadenaxml.replace("CUENTA_CARGO", fdRequestTrans.getCuentaCargo()).replace("CUENTA_ABONO", fdRequestTrans.getCuentaAbono()).replace("IMPORTE", fdRequestTrans.getImporte()).replace("REFERENCIA", fdRequestTrans.getReferencia());

                //Write_XML_WEBSERVICE
                xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                xmlWebServiceBean.setTipo_Peticion("REQUEST");
                xmlWebServiceBean.setXML(Cadenaxml);
                Insert_XML_WEBSERVICE(xmlWebServiceBean);

                try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
                    try {
                        dataOutputStream.writeBytes(Cadenaxml);
                        dataOutputStream.flush();
                    } finally {
                        dataOutputStream.close();
                    }
                }

                //Response_Status
                respEstatus = httpURLConnection.getResponseMessage();
 
                //Validate_Node_Status
                if ("Error".equals(respEstatus)) {
 
                    responseTraspasoBean.setMsjError(respEstatus);
                      
                    //Log
                    xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                    xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                    xmlWebServiceBean.setXML("Se puede obtuvó la siguiente contestación del Servicio: ".concat(respEstatus));
                    Insert_XML_WEBSERVICE(xmlWebServiceBean);
  
                    logger.error("Se obtuvo error en el Servicio");
                    return responseTraspasoBean;
                } 
                
                
                //Validate_Response_Status
                if (respEstatus == null) {
                    xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                    xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                    xmlWebServiceBean.setXML("No se puede obtener el Response Status");
                    Insert_XML_WEBSERVICE(xmlWebServiceBean); 
                }

                StringBuilder sBufer = new StringBuilder();

                try (BufferedReader bRead = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                    try {
                        //Response
                        inputLine = bRead.readLine();

                        //Write_Response
                        while (inputLine != null) {
                            sBufer.append(inputLine);
                            inputLine = bRead.readLine();
                        }

                        //Print_Console_Response 
                        xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                        xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                        xmlWebServiceBean.setXML(sBufer.toString());
                    } finally {
                        if (bRead != null) {
                            bRead.close();
                        }
                    }
                }
                Insert_XML_WEBSERVICE(xmlWebServiceBean);

                //Get_Element_XML_Response
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setNamespaceAware(true);

                DocumentBuilder builder = factory.newDocumentBuilder();                
                Document document = builder.parse(new InputSource(new StringReader(sBufer.toString())));
                 
                //Get_Node_Status
                Node node = document.getElementsByTagNameNS("*", "StatusCode").item(0);

                //Validate_Node_Status
                if (node == null) {
                    //Log
                    xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                    xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                    xmlWebServiceBean.setXML("No se puede obtener el nodo: <core:StatusCode>");
                    Insert_XML_WEBSERVICE(xmlWebServiceBean);
 
                    responseTraspasoBean.setMsjError("No se puede obtener el nodo: <core:StatusCode>");
                    logger.error("No se puede obtener el nodo: <core:StatusCode>");
                } else {
                    //Set_Node_Status 
                    responseTraspasoBean.setEstatus(node.getTextContent());
                }

                //Get all ConfirmationId
                NodeList nodeList = document.getElementsByTagNameNS("*", "Confirmation");

                //Validate_Node_Confirmation
                if (nodeList.getLength() > 0) {
                    //Get_Nodes
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        //Get_Node_Item
                        Node nodeItem = nodeList.item(i);
                        //Get_NodeListChild_Confirmation
                        NodeList nodeListChild = nodeItem.getChildNodes();

                        for (int y = 0; y < nodeListChild.getLength(); y++) {
                            //Get_Node_Item_Child
                            Node nodeChild = nodeListChild.item(y);

                            if ("ConfirmationId".equals(nodeChild.getLocalName())) {

                                //Set_Value_ConfirmationId
                                responseTraspasoBean.setFolioConfirmacion(nodeChild.getFirstChild().getTextContent());
                            }
                        }
                    }
                }

            } catch (IOException | ParserConfigurationException | SAXException e) {
                mensajeError = "Descripción: ".concat(e.getMessage()).concat(nombreObjeto).concat("transfer()");
                logger.error(mensajeError);

                xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                xmlWebServiceBean.setXML(mensajeError.concat(" ").concat(respEstatus));
                Insert_XML_WEBSERVICE(xmlWebServiceBean);

                try (BufferedReader bReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()))) {
                    try {
                        //Response
                        StringBuilder sBuilder = new StringBuilder();
                        inputLine = bReader.readLine();

                        //Write_Response
                        while (inputLine != null) {
                            sBuilder.append(inputLine);
                            inputLine = bReader.readLine();
                        }

                        //Print_Console_Response
                        xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
                        xmlWebServiceBean.setTipo_Peticion("RESPONSE");
                        xmlWebServiceBean.setXML(sBuilder.toString());
                        Insert_XML_WEBSERVICE(xmlWebServiceBean);
                    } finally {
                        if (bReader != null) {
                            bReader.close();
                        }
                    }
                }
            } 

        } catch (MalformedURLException e) {
            mensajeError += "Descripción : ".concat(e.getMessage()).concat(nombreObjeto).concat(" transfer()");

            xmlWebServiceBean.setNom_Interface("SW_CHEQUERA");
            xmlWebServiceBean.setTipo_Peticion("RESPONSE");
            xmlWebServiceBean.setXML(mensajeError.concat(" ").concat(respEstatus));
            Insert_XML_WEBSERVICE(xmlWebServiceBean);

            logger.error(mensajeError);
        }

        return responseTraspasoBean;
    }

    private void Insert_XML_WEBSERVICE(XmlWebServiceBean xmlWebServiceBean) {
 
        // Objects 
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Set_SQL
            stringBuilder.append("INSERT INTO SAF.FD_XML_WEBSERVICE (FECHA_HORA, NOM_INTERFACE, TIPO_PETICION, ID_USER, CADENA_XML)");
            stringBuilder.append(" VALUES (CURRENT TIMESTAMP, ?, ?, ?, ?)");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values   
            preparedStatement.setString(1, xmlWebServiceBean.getNom_Interface());
            preparedStatement.setString(2, xmlWebServiceBean.getTipo_Peticion());
            preparedStatement.setInt(3, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
   		 	
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlWebServiceBean.getXML().getBytes());
   		 	InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
   		 	preparedStatement.setClob(4, inputStreamReader); 
   		 	
            // Execute_Update
            preparedStatement.executeUpdate();

        } catch (SQLException | NumberFormatException sqlException) {
            String mensajeError = "Descripcion SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat(" Insert_XML_WEBSERVICE()");
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
}
