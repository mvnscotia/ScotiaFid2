/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotia Bank
 * ARCHIVO     : CControles.java
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210611
 * MODIFICADO  : 20210611
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       :
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import scotiaFid.bean.AdminControlesBean;
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.bean.KeyValueBean;
import scotiaFid.util.LogsContext;
import scotiaFid.util.Normalizacion;

public class CControles {

    private static final Logger logger = LogManager.getLogger(CControles.class); 
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private byte[] buffer;
    private Boolean valorRetorno;
    private String archivoLinea;
    private String sqlComando;
    private String nombreObjeto;
    private String mensajeErrorSP;
    private String[][] arrTMP03 = new String[15][3];
    private File archivo;
    private FileOutputStream archivoSalida;
    private SimpleDateFormat formatoFecha;
    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private ResultSetMetaData rsmd;

    private Calendar calendario;
    private List< Map<String , KeyValueBean>> tableData;
    private List<KeyValueBean> tableHeaderNames;

    public List<Map<String, KeyValueBean>> getTableData() {
        return tableData;
    }

    public List<KeyValueBean> getTableHeaderNames() {
        return tableHeaderNames;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private Boolean controlVacio;
    private Boolean controlDesbordado;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    public Boolean getControlVacio() {
        return controlVacio;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CControles() {
        LogsContext.FormatoNormativo();
        calendario = Calendar.getInstance();
        mensajeError = new String();
        nombreObjeto = "\nFuente: scotiaFid.dao.CControles.";
        valorRetorno = Boolean.FALSE;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * M E T O D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Boolean onControles_Adminsitra(AdminControlesBean ctrl) throws SQLException {
        try {
            conexion = DataBaseConexion.getInstance().getConnection();
            calendario.setTime(ctrl.getControlFechaMod());
            if (ctrl.getControlOperacionTipo().equals("REGISTRO")) {
                sqlComando = "SELECT Max(red_num_redaccion) + 1 MaxSec FROM SAF.Redacc";
                pstmt = conexion.prepareStatement(sqlComando);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    ctrl.setControlSec(rs.getShort("MaxSec"));
                }
                rs.close();
                pstmt.close();

                sqlComando = "INSERT INTO SAF.Redacc VALUES (?,?,?,?,?,?,?,?,?,?)";
                pstmt = conexion.prepareStatement(sqlComando);
                pstmt.setString(1, ctrl.getControlTipo());
                pstmt.setShort(2, ctrl.getControlSec());
                pstmt.setString(3, ctrl.getControlTexto());
                pstmt.setInt(4, calendario.get(Calendar.YEAR));
                pstmt.setInt(5, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt(6, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setInt(7, calendario.get(Calendar.YEAR));
                pstmt.setInt(8, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt(9, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setString(10, ctrl.getControlStatus());
            }
            if (ctrl.getControlOperacionTipo().equals("MODIFICAR")) {
                sqlComando = "UPDATE SAF.Redacc             \n"
                        + "SET    red_txt_redac     = ?, \n"
                        + "       red_ano_ult_mod   = ?, \n"
                        + "       red_mes_ult_mod   = ?, \n"
                        + "       red_dia_ult_mod   = ?, \n"
                        + "       red_cve_st_redacc = ?  \n"
                        + "WHERE  red_num_redaccion = ?"; 
                pstmt = conexion.prepareStatement(sqlComando);
                pstmt.setString(1, ctrl.getControlTexto());
                pstmt.setInt(2, calendario.get(Calendar.YEAR));
                pstmt.setInt(3, calendario.get(Calendar.MONTH) + 1);
                pstmt.setInt(4, calendario.get(Calendar.DAY_OF_MONTH));
                pstmt.setString(5, ctrl.getControlStatus());
                pstmt.setShort(6, ctrl.getControlSec());
            }
            if (ctrl.getControlOperacionTipo().equals("ELIMINAR")) {
                sqlComando = "DELETE FROM SAF.Redacc WHERE red_num_redaccion = ?";
                pstmt = conexion.prepareStatement(sqlComando);
                pstmt.setShort(1, ctrl.getControlSec());
            }
            pstmt.execute();
            pstmt.close();
            conexion.close();
            valorRetorno = Boolean.TRUE;
        } catch (SQLException Err) {
            mensajeError = "onControles_Adminsitra()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }

    public List<AdminControlesBean> onControles_Consulta() throws SQLException {
        List<AdminControlesBean> consulta = new ArrayList<>();
        try {
            sqlComando = "SELECT red_cve_tipo_docto, red_num_redaccion, Replace(red_txt_redac, 'A?O','AÑO')red_txt_redac,  \n"
                    + "       DATE(RTRIM(CHAR(RED_ANO_ALTA_REG))||'-'||RTRIM(CHAR(RED_MES_ALTA_REG))||'-'||RTRIM(CHAR(RED_DIA_ALTA_REG))) Regis,\n"
                    + "       DATE(RTRIM(CHAR(RED_ANO_ULT_MOD))||'-'||RTRIM(CHAR(RED_MES_ULT_MOD))||'-'||RTRIM(CHAR(RED_DIA_ULT_MOD))) Modif,\n"
                    + "       red_cve_st_redacc      \n"
                    + "FROM   SAF.Redacc             \n"
                    + "WHERE  red_cve_tipo_docto = ? \n"
                    + "AND RED_TXT_REDAC LIKE 'RC%'  \n"
                    + "AND red_cve_st_redacc = 'ACTIVO' \n"
                    + "ORDER  BY 2";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "CONTROL");  
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AdminControlesBean ctrl = new AdminControlesBean();
                ctrl.setControlTipo(rs.getString("red_cve_tipo_docto"));
                ctrl.setControlSec(rs.getShort("red_num_redaccion"));
                ctrl.setControlNombre(rs.getString("red_txt_redac").substring(0, rs.getString("red_txt_redac").indexOf(";")));
                if (ctrl.getControlNombre().contains("APENDICE")) {
                    ctrl.setControlNombre(ctrl.getControlNombre().replace("APENDICE", "APÉNDICE"));
                }
                ctrl.setControlTexto(rs.getString("red_txt_redac").substring(rs.getString("red_txt_redac").indexOf(";") + 1));
                ctrl.setControlFechaReg(rs.getDate("Regis"));
                ctrl.setControlFechaMod(rs.getDate("Modif"));
                ctrl.setControlStatus(rs.getString("red_cve_st_redacc"));

                consulta.add(ctrl);
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError = "onControles_Consulta()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return consulta;
    }

    public Boolean onControles_Ejecuta(String[][] arrParams, int controlNum, int usuarioNumero) throws SQLException, IOException {
        archivoLinea = new String();
        controlVacio = Boolean.TRUE;
        int totalReg = 0;
        String sqlControl;

        try {
            valorRetorno = OnControles_ResuelveControl(arrParams, controlNum, usuarioNumero);
                if (valorRetorno.equals(Boolean.TRUE)) {
                Integer maxNumReg =  onControles_NumRegistros();
                sqlControl = "SELECT DATOSDESPLIEGUE FROM SAF.DATOSCONTROL \n"
                        + "WHERE USUARIONOMINAID = ? \n"
                        + "  AND DATOSCONTRATO = ? \n"
                        + "ORDER BY DATOSCONTROLID";
                conexion = DataBaseConexion.getInstance().getConnection();
                pstmt = conexion.prepareStatement(sqlControl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                pstmt.setInt(1, usuarioNumero);
                pstmt.setInt(2, controlNum);
                rs = pstmt.executeQuery();
                rs.last();
                totalReg = rs.getRow();
                rs.beforeFirst();
                controlVacio = Boolean.TRUE;
                controlDesbordado = Boolean.FALSE;
                if (totalReg > 1 && totalReg <= maxNumReg ) {
                    tableHeaderNames = new ArrayList<KeyValueBean>();
                    tableData = new ArrayList< Map<String, KeyValueBean>>();
                    rsmd = rs.getMetaData();
                    controlVacio = Boolean.FALSE;
                    int cols = 0;
                    Boolean primero = Boolean.TRUE;
                    while (rs.next()) {
                        if (primero) {
                            String header = rs.getString("DATOSDESPLIEGUE").replace("|", "%");
                            String[] headers = header.split("%");
                            cols = headers.length;
                            for (int j = 0; j < cols; j++) {
                                tableHeaderNames.add(new KeyValueBean(headers[j], headers[j]));
                            }
                            primero = Boolean.FALSE;
                        } else {

                            String dato = rs.getString("DATOSDESPLIEGUE");
                            if (dato != null) {
                                dato = dato.replace("|", "%");
                                String[] datos = dato.split("%");
                                Map<String, KeyValueBean> playlist = new HashMap< String, KeyValueBean>();
                                for (int j = 0; j < cols; j++) {
                                    playlist.put(tableHeaderNames.get(j).getKey(), new KeyValueBean(tableHeaderNames.get(j).getKey(), datos[j]));
                                }
                                tableData.add(playlist);
                            }
                        }
                    }
                }else{
                   if(totalReg > 1){ 
                   controlDesbordado = Boolean.TRUE;
                   controlVacio = Boolean.FALSE;
                   }
                }
                    
                valorRetorno = Boolean.TRUE;
                rs.close();
                pstmt.close();  
                conexion.close();
            }
        } catch (SQLException Err) {
            mensajeError = "onControles_Ejecuta()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
        }
        return valorRetorno;
    }
    public Integer onControles_NumRegistros() {
        String sqlControl;
        Integer numReg = 0;
        try {
            sqlControl = "SELECT CVE_LIMSUP_CLAVE FROM CLAVES \n"
                    + "WHERE CVE_NUM_CLAVE = ? \n";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlControl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setInt(1, 3005);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                numReg = rs.getInt("CVE_LIMSUP_CLAVE");
                valorRetorno = Boolean.TRUE;
            }
            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException Err) {
            mensajeError = "onControles_NumRegistros";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
        }
        return numReg;
    }

    public Boolean onControles_Descarga(String[][] arrParams, String controlNombre, int controlNum, int usuarioNumero) throws SQLException, IOException {
        archivoLinea = new String();
        controlVacio = Boolean.TRUE;
        int totalReg;
        String sqlControl;

        try {
            sqlControl = "SELECT DATOSDESPLIEGUE FROM SAF.DATOSCONTROL \n"
                    + "WHERE USUARIONOMINAID = ? \n"
                    + "  AND DATOSCONTRATO = ? \n"
                    + "ORDER BY DATOSCONTROLID";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlControl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setInt(1, usuarioNumero);
            pstmt.setInt(2, controlNum);
            rs = pstmt.executeQuery();  
            rs.last();
            totalReg = rs.getRow();
            rs.beforeFirst();
            controlVacio = Boolean.TRUE;
            if (totalReg > 1) {
                String controlNombreNormalizado = Normalizacion.parse(controlNombre);
                controlVacio = Boolean.FALSE;
                //Preparamos el archivo 
                archivo = new File(controlNombreNormalizado);
                formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a" ); 
                archivoSalida = new FileOutputStream(archivo);
                archivoLinea = "Fecha de Sistema: ".concat((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).
                        concat(" Fecha: ").concat(formatoFecha.format(new Date()).toString()).concat("\n");
                buffer = archivoLinea.getBytes();
                archivoSalida.write(buffer);
                while (rs.next()) {
                    archivoLinea = rs.getString("DATOSDESPLIEGUE");
                    if (archivoLinea != null) {
                        archivoLinea = rs.getString("DATOSDESPLIEGUE").replace("|", "\t").trim();
                        archivoLinea = archivoLinea + "\n";
                        buffer = archivoLinea.getBytes();
                        archivoSalida.write(buffer);
                    } 
                }
                archivoSalida.close();
            }
            valorRetorno = Boolean.TRUE;
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (IOException | SQLException Err) {
            mensajeError = "onControles_Ejecuta()";
            logger.error(mensajeError);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
            try {
                if(archivoSalida !=null){
                    archivoSalida.close();
                }
            } catch (IOException Err) {  
              logger.error("Error en funcion: onControles_Descarga");
            }
        }
        return valorRetorno;
    }

    public Boolean OnControles_ResuelveControl(String[][] arrParamsEntrada, int controlSec, Integer usuarioNumero) {
        String sqlControl = null;
        String cadenaTMP00 = new String();
        String[] arrTMP00 = new String[10];
        String[] arrTMP01 = new String[5]; 
        String[] arrT = new String[10]; 
        Boolean bGeneroFROM = Boolean.FALSE;  
        String cadenaSELECT = "SELECT ";
        String cadenaFROM = " FROM ";
        String cadenaWHERE = new String();
        String cadenaORDER = new String();
        String cadenaGROUP = new String();
        Integer intemAux = 0;
        Integer ItemCal = 0;
        String[] arrSQLSeccion = new String[6];
        try {
            cadenaTMP00 = onControles_ObtenControl(controlSec);
            arrSQLSeccion = cadenaTMP00.split(";");
//MNV-INI   
//01 Agrega campos Selecionados
            cadenaTMP00 = arrSQLSeccion[1];
            arrTMP00 = cadenaTMP00.split("&");
            for (int itemSELECT = 0; itemSELECT <= arrTMP00.length - 1; itemSELECT++) {//Recorre los campos del select 
                arrTMP01 = arrTMP00[itemSELECT].trim().split("~"); //Descompone el campo seleccionado (campo, formato, tamaño, alineacion)
                for (int itemFIELD = 0; itemFIELD <= arrTMP01.length - 1; itemFIELD++) {
                    if (itemFIELD == 0) {
                        cadenaSELECT = cadenaSELECT.concat(arrTMP01[itemFIELD]).concat(" ").concat(arrTMP01[1]).concat(", ");
                    }
                }
            }
            cadenaSELECT = cadenaSELECT.trim();
            cadenaSELECT = cadenaSELECT.substring(0, cadenaSELECT.length() - 1).concat("\n");
//02 Origen de Información FROM
            if ((arrSQLSeccion[2].contains(",")) && (!arrSQLSeccion[2].contains("SAF")) && (bGeneroFROM.equals(Boolean.FALSE))) {//Viene de varias tablas separadas por comas (join a la antigua)
                arrTMP01 = arrSQLSeccion[2].split(",");
                for (int itemTabla = 0; itemTabla <= arrTMP01.length - 1; itemTabla++) {
                    cadenaFROM = cadenaFROM.concat(arrTMP01[itemTabla].trim().concat(", "));
                }
                cadenaFROM = cadenaFROM.trim();
                cadenaFROM = cadenaFROM.substring(0, cadenaFROM.length() - 1);
                bGeneroFROM = Boolean.TRUE;
            } 
            if ((!arrSQLSeccion[2].trim().contains(" ") || !arrSQLSeccion[2].trim().equals("")) && (bGeneroFROM.equals(Boolean.FALSE))) {
                cadenaFROM = cadenaFROM.concat(arrSQLSeccion[2]);
            }
            cadenaFROM = cadenaFROM.concat("\n");

//03 Filtro simple
            if (!arrSQLSeccion[3].trim().equals(new String())) {
                cadenaWHERE = "WHERE ".concat(arrSQLSeccion[3]);
            }
//04 Filtro con valores que el usuario ingresa
            if (!arrSQLSeccion[4].trim().equals(new String())) {

                ArrayList<String> userParamsArr = new ArrayList<String>();
                String[] userParamsByCommaArr = arrSQLSeccion[5].trim().split(",");
                //*Inicio-mnv
                String[] ArruserParamsArr = arrSQLSeccion[4].trim().split("/");
                String ValidaTitulo = new String();
                String Calificador = new String();
                String OpcNec = new String();
                String Cal1 = new String();
                String userParams = arrSQLSeccion[4].trim().replace("/", " ");
                intemAux = userParamsByCommaArr.length;

                for (int item = 0; item < ArruserParamsArr.length; item++) {
                    Cal1 = new String();
                    if (ArruserParamsArr[item].contains("&")) {
                        Cal1 = (ArruserParamsArr[item].trim().split("&")[1]);
                    }

                    ItemCal = null;
                    if (!Cal1.equals(new String())) {
                        for (int item2 = 0; item2 < userParamsByCommaArr.length; item2++) {
                            if (userParamsByCommaArr[item2].trim().split(" ")[0].equals(Cal1)) {
                                ItemCal = item2;
                            }

                        }
                    }
                    if (ItemCal != null) {
                        ValidaTitulo = (userParamsByCommaArr[ItemCal].trim().split(" ")[1]);
                        arrT = (userParamsByCommaArr[ItemCal].trim().split(" "));
                        if (arrT.length > 2) {
                            OpcNec = (userParamsByCommaArr[ItemCal].trim().split(" ")[2]);
                        } else {
                            OpcNec = "O";
                        }
                        if (ValidaTitulo.equals("A?O")) {
                            ValidaTitulo = "AÑO";
                        }
                        if (ValidaTitulo.equals("NUM_ANIO")) {
                            ValidaTitulo = "NUM_AÑO";
                        }

                        for (int indPE = 0; indPE < arrParamsEntrada.length; indPE++) {
                            if (arrParamsEntrada[indPE][0] != null && arrParamsEntrada[indPE][0].toLowerCase(Locale.ENGLISH).equals(ValidaTitulo.toLowerCase(Locale.ENGLISH))) {
                                if (arrParamsEntrada[indPE][2].equals(new String()) && (OpcNec.equals("O"))) {
                                    if (ArruserParamsArr[item].contains(Calificador)) {
                                        ArruserParamsArr[item] = new String();
                                        intemAux = intemAux - 1;
                                        //        if ((item == 0) || (ArruserParamsArr[item - 1].equals(new String()))) {
                                        if (ArruserParamsArr.length > 1) {
                                            if (item == 0 || ArruserParamsArr[item - 1].equals(new String())) {
                                                if(ArruserParamsArr.length > (item + 1)){
                                                ArruserParamsArr[item + 1] = new String();
                                                }
                                            } else {
                                                ArruserParamsArr[item - 1] = new String();
                                            }
                                        }
                                    }
                                }
                            }
                        }                     
                    }
                }
                if (intemAux > 0) {
                    if (!cadenaWHERE.contains("WHERE")) {
                        cadenaWHERE = "WHERE ";
                    } else {
                        cadenaWHERE = cadenaWHERE.concat("\nAND ");
                    }
                }
                userParams = "";

                for (int item = 0; item < ArruserParamsArr.length; item++) {
                    if (!ArruserParamsArr[item].equals("")) {
                        userParams = userParams.concat((ArruserParamsArr[item].concat(" ")));

                    }
                }

                arrTMP03 = new String[intemAux][3];
                intemAux = 0;

                for (int item = 0; item < userParamsByCommaArr.length; item++) {
                    if (userParams.contains(userParamsByCommaArr[item].trim().split(" ")[0])) {

                        arrTMP03[intemAux][0] = userParamsByCommaArr[item].trim().split(" ")[1];  //Título de filtro

                        arrT = (userParamsByCommaArr[item].trim().split(" "));
                        if (arrT.length >= 2) {
                            arrTMP03[intemAux][2] = new String();
                            arrTMP03[intemAux][1] = userParamsByCommaArr[item].trim().split(" ")[0];  //Tipo de dato del filtro
                        } else {
                            arrTMP03[intemAux][1] = new String();
                        }
//                        //Valor del filtro
                        if (arrTMP03[intemAux][0].equals("A?O")) {
                            arrTMP03[intemAux][0] = "AÑO";
                        }

                        if (arrTMP03[intemAux][0].equals("NUM_ANIO")) {
                            arrTMP03[intemAux][0] = "NUM_AÑO";
                        }
                        intemAux = intemAux + 1;
                        userParamsArr.add(userParamsByCommaArr[item].trim().split(" ")[0]);
                    }
                }
                for (int rr3 = 0; rr3 < arrTMP03.length; rr3++) {
                    for (int item = 0; item < userParamsByCommaArr.length; item++) {
                        if (arrTMP03[rr3][0] != null && arrParamsEntrada[item][0].toLowerCase(Locale.ENGLISH).equals(arrTMP03[rr3][0].toLowerCase(Locale.ENGLISH))) {
                            arrTMP03[rr3][2] = arrParamsEntrada[item][2];
                        }

                    }
                }
                for (int param = 0; param < userParamsArr.size(); param++) {
                    String valorParametro = new String();
                    if (userParams.contains(userParamsArr.get(param))) {
                        if (arrSQLSeccion[0].trim().equals("RC: INVENTARIO_GOBIERNO")) {
                            for (int rr3 = 0; rr3 < arrTMP03.length; rr3++) {
                                if (arrTMP03[rr3][1] != null && userParamsArr.get(param).contains(arrTMP03[rr3][1])) {
                                    if (userParamsArr.get(param).contains("N")) {
                                        valorParametro = arrTMP03[rr3][2];
                                    }
                                }
                            }

                            userParams = userParams.replace("&".concat(userParamsArr.get(param)), valorParametro);

                        } else {

                            for (int rr3 = 0; rr3 < arrTMP03.length; rr3++) {
                                if (arrTMP03[rr3][1] != null && userParamsArr.get(param).contains(arrTMP03[rr3][1])) {
                                    if (userParamsArr.get(param).contains("N")) {
                                        valorParametro = " ".concat(arrTMP03[rr3][2].concat(" \n"));
                                    } else {
                                        try{
                                        String fecha = dateFormato(arrTMP03[rr3][2]);
                                        valorParametro = " '".concat(fecha.concat("' \n"));
                                        }catch(ParseException Err){
                                          valorParametro = " '".concat(arrTMP03[rr3][2].concat("' \n"));
                                        }
                                        
                                    }
                                }
                            }

                            userParams = userParams.replace("&".concat(userParamsArr.get(param)), valorParametro);
                        }
                    }
                }
                cadenaWHERE = cadenaWHERE.concat(userParams);
            }
//06 Agrupación
            if (arrSQLSeccion.length >= 7) {
                if (!arrSQLSeccion[6].trim().equals(new String())) {
                    if (arrSQLSeccion[6].trim().contains("FE&")) {
//                        cadenaGROUP = arrSQLSeccion[6].trim().replace("FE&", " AND CONTRATO.CTO_NUM_CONTRATO IN (SELECT CTO_NUM_CONTRATO FROM SAF.VISTA_USUARIO WHERE USU_NUM_USUARIO = ".concat(usuarioNumero.toString()).concat(") "));
                        cadenaGROUP = arrSQLSeccion[6].trim().replace("FE&", " AND  CONTRATO.CTO_NUM_NIVEL4  IN (SELECT DISTINCT (CTO_NUM_NIVEL4)  FROM SAF.VISTA_USUARIO WHERE  USU_NUM_USUARIO = ".concat(usuarioNumero.toString()).concat(") ")); 
                    } else {
                        cadenaGROUP = arrSQLSeccion[6].trim().concat("\n");
                    }
                }
            }

//07 Ordenacion
            if (arrSQLSeccion.length >= 8) {
                if (!arrSQLSeccion[7].trim().equals(new String())) {
                    cadenaORDER = arrSQLSeccion[7].trim().concat("\n");
                }
            }

            //Generamos la instrucción SQL que será ejecutada
            sqlControl = cadenaSELECT.concat(" ").concat(cadenaFROM).concat(" ");
            if (!cadenaWHERE.equals(new String())) {
                sqlControl = sqlControl.concat(" ").concat(cadenaWHERE);
            };
            if (!cadenaGROUP.equals(new String())) {
                sqlControl = sqlControl.concat(" ").concat(cadenaGROUP);
            }
            if (!cadenaORDER.equals(new String())) {
                sqlControl = sqlControl.concat(" ").concat(cadenaORDER);
            }
//***************** Ejecuta control 
            sqlComando = "{call DB2FIDUC.SPN_EJECUT_CONTROL(?,?,?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);
            cstmt.setInt("IOPERADOR", usuarioNumero);
            cstmt.setInt("ICONTROL", controlSec);
            cstmt.setInt("ICONTRATO", controlSec);
            cstmt.setString("SCONTROL1", cadenaSELECT);
            cstmt.setString("SCONTROL2", cadenaFROM);
            cstmt.setString("SCONTROL3", cadenaWHERE);
            cstmt.setString("SCONTROL4", cadenaGROUP);
            cstmt.setString("SCONTROL5", cadenaORDER);

            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
                mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
             cstmt.close();
            conexion.close();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
            } else {
                mensajeError = mensajeErrorSP;
            }

        } catch (SQLException  Err) {
            mensajeError = "OnControles_ResuelveControl()";
            logger.error(mensajeError + Err);
            valorRetorno = Boolean.FALSE;
        } finally {
            onCierraConexion();
        }
        return valorRetorno;

    }

    public List<String> onControles_CargaCtrls() throws SQLException {
        List<String> lista = new ArrayList<>();
        try {
            sqlComando = "SELECT subString(red_txt_redac,1,(InStr(red_txt_redac,';')-1))||'»'||red_num_redaccion Control FROM SAF.Redacc WHERE red_cve_st_redacc = ? ORDER BY 1";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setString(1, "ACTIVO");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("Control"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError = "onControles_CargaCtrls()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return lista;
    }

    public String onControles_ObtenControl(int controlSec) throws SQLException {
        String controlTexto = new String();
        try {
            sqlComando = "SELECT red_txt_redac FROM SAF.Redacc WHERE red_num_redaccion = ?";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, controlSec);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                controlTexto = rs.getString("red_txt_redac");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {
            mensajeError = "onControles_ObtenControl()";
            logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }
        return controlTexto;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * F U N C I O N E S   P R I V A D A S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onCierraConexion() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar ResultSet." + e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar PreparetStatement." + e.getMessage());
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar CallableStatement." + e.getMessage());
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                logger.error("Function :: Error al cerrar Connection." + e.getMessage());
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String dateFormato(String str) throws ParseException {
        DateFormat dfInput = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfOutput = new SimpleDateFormat("MM/dd/yyyy");
        Date date = dfInput.parse(str);
        String stringDate = dfOutput.format(date);

        return stringDate;
    }

    public static java.sql.Date stringToDate(String str) throws ParseException {
        DateFormat dfInput = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dfInput.parse(str);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;
    }

    //convierte de string a int
    public static int dateStringToInt(String str) throws ParseException {
        String[] arrDate = str.split("/");
        int dateConcated = Integer.parseInt(arrDate[2].concat(arrDate[1]).concat(arrDate[0]));

        return dateConcated;
    }

    public Boolean getControlDesbordado() {
        return controlDesbordado;
    }

    public void setControlDesbordado(Boolean controlDesbordado) {
        this.controlDesbordado = controlDesbordado;
    }

}
