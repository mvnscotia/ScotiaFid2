/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CHonorarios.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.dao;

//Import
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.BitacoraBean;
import scotiaFid.bean.DetCartBean;
import scotiaFid.bean.FechaContableBean;
import scotiaFid.bean.OtrosIngresosBean;
import scotiaFid.bean.OutParameterBean;
import scotiaFid.bean.PacaHonBean;
import scotiaFid.bean.ParticipanteBean;
import scotiaFid.bean.ProductoBean;
import scotiaFid.bean.ServicioBean;
import scotiaFid.singleton.DataBaseConexion;
import static scotiaFid.util.CValidacionesUtil.realScapeString;
import scotiaFid.util.LogsContext;
import scotiaFid.util.CValidacionesUtil;

//Class
public class CHonorarios implements Serializable {
    private static final Logger logger = LogManager.getLogger(CHonorarios.class);
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

 /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * B E A N S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    BitacoraBean bitacoraBean = new BitacoraBean();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * A T R I B U T O S   P R I V A D O S   V I S I B L E S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String nombreObjeto;
    private Boolean valorRetorno;

    private CallableStatement cstmt;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * G E T T E R S   Y   S E T T E R S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * C O N S T R U C T O R
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CHonorarios() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CHonorarios.";
        valorRetorno = Boolean.FALSE;
        LogsContext.FormatoNormativo();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * M E T O D O S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<String> onHonorarios_GetIVA() {
        //Objects
        List<String> ivas = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT IND_VALOR_INDICE, IND_CVE_INDICE FROM INDICES WHERE IND_CVE_INDICE = ? ");
            stringBuilder.append(" UNION SELECT IND_VALOR_INDICE, IND_CVE_INDICE FROM INDICES WHERE IND_CVE_INDICE = ? ORDER BY 2");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, "IVA");
            pstmt.setString(2, "IVA FRONTERIZO");

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_I.V.A.
                while (rs.next()) {
                    //Set_IVA
                    String iva = rs.getString("IND_VALOR_INDICE");

                    //Add_List
                    ivas.add(iva);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetIVA()");

        } finally {
            onCierraConexion();
        }

        return ivas;
    }

    public Map<String, String> onHonorarios_GetMonedas() {
        //Objects 
        Map<String, String> monedas = new LinkedHashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT MON_NOM_MONEDA, MON_NUM_PAIS FROM MONEDAS ORDER BY MON_NUM_PAIS");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_Monedas.
                while (rs.next()) {
                    //Add_List
                    monedas.put(String.valueOf(rs.getInt("MON_NUM_PAIS")), rs.getString("MON_NOM_MONEDA"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetMonedas()");

        } finally {
            onCierraConexion();
        }

        return monedas;
    }

    public int onHonorarios_GetMonedaXFideicomiso(int fideicomiso) {
        //Objects
        int moneda = 1;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PAC_NUM_MONEDA FROM PACAHON WHERE PAC_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_Monedas.
                while (rs.next()) {
                    //Add_Moneda
                    moneda = rs.getInt("PAC_NUM_MONEDA");
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetMonedaXFideicomiso()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetMonedaXFideicomiso()");

        } finally {
            onCierraConexion();
        }

        return moneda;
    }

    public Map<String, String> onHonorarios_GetServicios() {
        //Objects 
        Map<String, String> servicios = new LinkedHashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT SER_NUM_SERVICIO, SER_NOM_SERVICIO FROM SERVICIO WHERE SER_CVE_ST_SERVICI = ? ORDER BY SER_NOM_SERVICIO");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, "ACTIVO");

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Add_One_Element_Empty
                servicios.put(String.valueOf(0), "");

                while (rs.next()) {
                    //Add_List
                    servicios.put(String.valueOf(rs.getInt("SER_NUM_SERVICIO")), rs.getString("SER_NOM_SERVICIO"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetMonedaXFideicomiso()");

        } finally {
            onCierraConexion();
        }

        return servicios;
    }

    public FechaContableBean onHonorarios_GetFechaContable() {
        //Objects
        FechaContableBean fechaContableBean = new FechaContableBean();
        StringBuilder stringBuilder = new StringBuilder();

        //Date_Format 
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            //SQL 
            stringBuilder.append("SELECT FCO_DIA_DIA, FCO_MES_DIA, FCO_ANO_DIA FROM FECCONT WHERE FCO_CVE_TIPO_FECHA = ? AND FCO_CVE_ST_FECCONT = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, "CONTABLE");
            pstmt.setString(2, "ACTIVO");

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                while (rs.next()) {
                    //Set_Fecha_Contable
                    fechaContableBean.setFechaContable(String.format("%04d", rs.getInt("FCO_ANO_DIA")).concat("-").concat(String.format("%02d", rs.getInt("FCO_MES_DIA")).concat("-").concat(String.format("%02d", rs.getInt("FCO_DIA_DIA")))));
                    fechaContableBean.setFecha(dateFormat.parse(fechaContableBean.getFechaContable()));
                }
            }
        } catch (SQLException | ParseException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetFechaContable()");

        } finally {
            onCierraConexion();
        }

        return fechaContableBean;
    }

    public Map<String, String> onHonorarios_GetMetodosPago() {
        //Objects
        Map<String, String> metodosPago = new LinkedHashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT CMP_CVE_METPAG, UPPER(CMP_DESC_METPAG) AS CMP_DESC_METPAG FROM CATSATMETPAGO WHERE CMP_CVE_METPAG > 0 ORDER BY CMP_DESC_METPAG");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_Monedas.
                while (rs.next()) {
                    //Add_List
                    metodosPago.put(String.valueOf(rs.getInt("CMP_CVE_METPAG")), rs.getString("CMP_DESC_METPAG"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetMetodosPago()");

        } finally {
            onCierraConexion();
        }

        return metodosPago;
    }

    public boolean onHonorarios_checkFideicomiso(int fideicomiso) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        boolean exist = false;

        try {
            //SQL 
            stringBuilder.append("SELECT C.CTO_NUM_CONTRATO, C.CTO_NOM_CONTRATO, ");
            stringBuilder.append("C.CTO_CVE_ST_CONTRAT, C.CTO_TIPO_ADMON, C.CTO_CVE_MON_EXT,");
            stringBuilder.append("C.CTO_CVE_SUBCTO, C.CTO_NUM_NIVEL1, C.CTO_NUM_NIVEL2, C.CTO_NUM_NIVEL3, ");
            stringBuilder.append("C.CTO_NUM_NIVEL4, C.CTO_NUM_NIVEL5, A.CVE_NUM_SEC_CLAVE TipoNeg, ");
            stringBuilder.append("B.CVE_NUM_SEC_CLAVE clasif, CTO_CVE_REQ_SORS AbiertoCerrado, ");
            stringBuilder.append("C.CTO_CVE_EXCLU_30 ImpEsp,  C.CTO_SUB_RAMA Prorrate_SubFiso ");
            stringBuilder.append("FROM ((((CONTRATO C LEFT OUTER JOIN CLAVES A ON A.CVE_NUM_CLAVE = 36 ");
            stringBuilder.append("AND C.CTO_CVE_TIPO_NEG = A.CVE_DESC_CLAVE) LEFT OUTER JOIN CLAVES B ON B.CVE_NUM_CLAVE = 37 AND C.CTO_CVE_CLAS_PROD = B.CVE_DESC_CLAVE) ");
            //Validacion de Regionalización
            stringBuilder.append("INNER JOIN SAF.VISTA_USUARIO V ON V.CTO_NUM_CONTRATO = C.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? ) ");
            stringBuilder.append("INNER JOIN SAF.USUARIOS U ON U.USU_NUM_USUARIO = V.USU_NUM_USUARIO ) ");

            stringBuilder.append(" WHERE C.CTO_NUM_CONTRATO = ? AND C.CTO_ES_EJE = ? AND C.CTO_CVE_ST_CONTRAT= ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(2, fideicomiso);
            pstmt.setInt(3, 0);
            pstmt.setString(4, "ACTIVO");

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                if (rs.next()) {
                    exist = true;
                } else {
                    exist = false;
                }
            } else {
                exist = false;
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_checkFideicomiso()");

        } finally {
            onCierraConexion();
        }

        return exist;
    }

    public List<String> onHonorarios_GetFechasCalculo(int fideicomiso) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();
        List<String> fechas = new ArrayList<>();
        LocalDate localDateProyecta;

        //Date_Time_Formatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Variables
        int posicion = 0;
        String periodo = "";
        String tiempo = "";
        String fechaUltimoCalculo = "";
        String fechaCalculo = "";

        try {
            //SQL 
            stringBuilder.append("SELECT PAC_FEC_ULT_CALC, PAC_ANO_CALC_HONO, PAC_MES_CALC_HONO, ");
            stringBuilder.append(" PAC_DIA_CALC_HONO, PAC_CVE_PERIOD_COB, PAC_DIA_CALC_CLTE, PAC_CVE_FORMA_CALC ");
            stringBuilder.append(" FROM SAF.PACAHON WHERE PAC_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                while (rs.next()) {
                    //Get_Posicion
                    posicion = rs.getString("PAC_CVE_PERIOD_COB").indexOf(" ");
 
                    if (posicion != -1) {
                        //Get_Periodo
                        periodo = rs.getString("PAC_CVE_PERIOD_COB");
                        periodo = periodo.substring(0, periodo.indexOf(" "));
 
                        //Get_Tiempo
                        tiempo = rs.getString("PAC_CVE_PERIOD_COB");
                        tiempo = tiempo.substring(tiempo.indexOf(" "), tiempo.length());
  
                    } else {
                        periodo = rs.getString("PAC_CVE_PERIOD_COB");
                        tiempo = "ADELANTADA";
                    }

                    if (!"".equals(rs.getString("PAC_FEC_ULT_CALC"))) {
                        //Obten_Fecha_Calculo 
                        fechaUltimoCalculo = rs.getString("PAC_FEC_ULT_CALC");
                        fechaCalculo = String.format("%02d", rs.getInt("PAC_ANO_CALC_HONO")).concat("-").concat(String.format("%02d", rs.getInt("PAC_MES_CALC_HONO")).concat("-").concat(String.format("%02d", rs.getInt("PAC_DIA_CALC_HONO"))));
                    }

                    if (!rs.getString("PAC_CVE_FORMA_CALC").equals("EXENTO")) {
                        if (onHonorarios_isFechaValida(fechaUltimoCalculo) && onHonorarios_isFechaValida(fechaCalculo)) {
                            if ("VENCIDA".equals(tiempo.trim())) {
                                //Set_Date
                                LocalDate localDate = LocalDate.parse(fechaUltimoCalculo, dateTimeFormatter);
                                localDateProyecta = onHonorarios_proyectaFecha(localDate, periodo);

                                //Plus_One_day
                                LocalDate localDateStart = LocalDate.parse(fechaUltimoCalculo, dateTimeFormatter).plusDays(1);

                                //Proyecta_Fecha_Ultimo_Calculo
                                LocalDate localDateEnd = localDateProyecta.withDayOfMonth(localDateProyecta.lengthOfMonth());
 
                                //Set_List
                                fechas.add(localDateStart.format(dateTimeFormatter));
                                fechas.add(localDateEnd.format(dateTimeFormatter));

                            } else {
                                //Set_Date
                                LocalDate localDate = LocalDate.parse(fechaCalculo, dateTimeFormatter);
                                localDateProyecta = onHonorarios_proyectaFecha(localDate, periodo);

                                //Plus_One_day
                                LocalDate localDateStart = LocalDate.parse(fechaCalculo, dateTimeFormatter).plusDays(1);

                                //Proyecta_Fecha_Calculo
                                LocalDate localDateEnd = localDateProyecta.withDayOfMonth(localDateProyecta.lengthOfMonth());
 
                                //Set_List
                                fechas.add(localDateStart.format(dateTimeFormatter));
                                fechas.add(localDateEnd.format(dateTimeFormatter));
                            }
                        }
                    } else {
                        //Set_Date_Now
                        LocalDate localDateStart = LocalDate.now();
                        LocalDate localDateEnd = LocalDate.now();
 
                        //Set_List
                        fechas.add(localDateStart.format(dateTimeFormatter));
                        fechas.add(localDateEnd.format(dateTimeFormatter));
                    }
                }
            }
            //rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetFechasCalculo()");

        } finally {
            onCierraConexion();
        }
        return fechas;
    }

    public ServicioBean onHonorarios_GetNumeroServicio(String servicio) {
        //Objects
        ServicioBean servicioBean = new ServicioBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT SER_NUM_SERVICIO, SER_TARIFA_SERV, SER_CVE_USA_TABLA FROM SERVICIO WHERE SER_NUM_SERVICIO = ? AND  SER_CVE_ST_SERVICI = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, servicio);
            pstmt.setString(2, "ACTIVO");

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {

                if (rs.next()) {
                    //Set_Servicio
                    servicioBean.setNumero(rs.getInt("SER_NUM_SERVICIO"));
                    servicioBean.setTarifa(rs.getInt("SER_TARIFA_SERV"));
                    servicioBean.setUsaTabla(rs.getInt("SER_CVE_USA_TABLA"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetNumeroServicio()");

        } finally {
            onCierraConexion();
        }

        return servicioBean;
    }

    public ProductoBean onHonorarios_GetNumeroProducto(int fideicomiso) {
        //Objects
        ProductoBean productoBean = new ProductoBean();
        StringBuilder stringBuilder = new StringBuilder();

        //Variable
        int producto = 0;

        try {
            //SQL 
            stringBuilder.append("SELECT CTO_NUM_PRODUCTO FROM CONTRATO WHERE CTO_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                if (rs.next()) {
                    //Set_Producto_X_Fiso
                    producto = rs.getInt("CTO_NUM_PRODUCTO");
                }
            }

            onCierraConexion();

            //Clean_StringBuilder
            stringBuilder.setLength(0);

            //Get_Producto 
            stringBuilder.append("SELECT PRL_IMP_HON_ACEP, PRL_IMP_HON_MANEJO FROM PRODUCTO  WHERE PRL_NUM_PRODUCTO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, producto);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                if (rs.next()) {
                    //Set_Producto_X_Fiso
                    Double PRL_IMP_HON_ACEP = CValidacionesUtil.validarDouble(rs.getString("PRL_IMP_HON_ACEP")); productoBean.setImporteAceptacion(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(PRL_IMP_HON_ACEP));
                    productoBean.setImporteAceptacionImp(rs.getDouble("PRL_IMP_HON_ACEP"));
                    productoBean.setImporteManejo(rs.getDouble("PRL_IMP_HON_MANEJO"));
                }
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetNumeroProducto()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetNumeroProducto()");

        } finally {
            onCierraConexion();
        }

        return productoBean;
    }

    public String onHonorarios_GetTipoCambio(int moneda, int anio, int mes, int dia) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        String tipoCambio = "";

        try {
            //SQL 
            stringBuilder.append("SELECT TIC_IMP_TIPO_CAMB FROM TIPOCAMB WHERE TIC_NUM_PAIS = ? AND TIC_ANO_ALTA_REG = ? AND TIC_MES_ALTA_REG = ? AND TIC_DIA_ALTA_REG = ? ORDER BY TIC_ANO_ULT_MOD DESC,TIC_MES_ULT_MOD DESC ,TIC_DIA_ULT_MOD DESC, TIC_HORA_ALTA DESC,TIC_MINUTO_ALTA DESC");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, moneda);
            pstmt.setInt(2, anio);
            pstmt.setInt(3, mes);
            pstmt.setInt(4, dia);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                if (rs.next()) {
                    //Set_Values
                    tipoCambio = rs.getString("TIC_IMP_TIPO_CAMB");
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetTipoCambio()");

        } finally {
            onCierraConexion();
        }

        return tipoCambio;
    }

    public PacaHonBean onHonorarios_GetParametrosCalculoHonorarios(int fideicomiso) {
        //Objects
        PacaHonBean pacaHonBean = new PacaHonBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PAC_NUM_CONTRATO, PAC_NUM_MONEDA, PAC_CVE_PERS_COB, PAC_CVE_PERIOD_COB, PAC_CVE_FORMA_CALC FROM PACAHON WHERE PAC_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {

                if (rs.next()) {
                    //Set_Values
                    pacaHonBean.setContrato(rs.getInt("PAC_NUM_CONTRATO"));
                    pacaHonBean.setMoneda(rs.getInt("PAC_NUM_MONEDA"));
                    pacaHonBean.setPersonaCobra(rs.getString("PAC_CVE_PERS_COB"));
                    pacaHonBean.setPeriodoCobro(rs.getString("PAC_CVE_PERIOD_COB"));
                    pacaHonBean.setFormaCalculo(rs.getString("PAC_CVE_FORMA_CALC"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetParametrosCalculoHonorarios()");

        } finally {
            onCierraConexion();
        }

        return pacaHonBean;
    }

    public OutParameterBean onHonorarios_executeGeneraHonorariosSp(OtrosIngresosBean otrosIngresosBean) {
        //Objects
        OutParameterBean outParameterBean = new OutParameterBean();

        //Format_Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

        //SQL
        String sql = "{CALL DB2FIDUC.SPN_GENERA_HON_MAN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        //GET_CALLABLESTATEMENT
        try {
            //Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sql);

            //REGISTER_IN_PARAMETER
            cstmt.setInt(1, Integer.parseInt(otrosIngresosBean.getFideicomiso()));
            cstmt.setString(2, otrosIngresosBean.getPersonaCobra());
            cstmt.setInt(3, 1);
            cstmt.setString(4, otrosIngresosBean.getNombreServicio());
            cstmt.setInt(5, Integer.parseInt(otrosIngresosBean.getMoneda()));
            cstmt.setDouble(6, Double.parseDouble(otrosIngresosBean.getImporte()));
            Double gIVA = CValidacionesUtil.validarDouble(otrosIngresosBean.getIva()); cstmt.setDouble(7, gIVA);
            Double gTipoCambio = CValidacionesUtil.validarDouble(otrosIngresosBean.getTipoCambio()); cstmt.setDouble(8, gTipoCambio);
            cstmt.setDate(9, java.sql.Date.valueOf(otrosIngresosBean.getFechaValor()));
            cstmt.setDate(10, java.sql.Date.valueOf(simpleDateFormat.format(otrosIngresosBean.getFechaDel())));
            cstmt.setDate(11, java.sql.Date.valueOf(simpleDateFormat.format(otrosIngresosBean.getFechaAl())));
            cstmt.setInt(12, Integer.parseInt(otrosIngresosBean.getServicio()));
            cstmt.setInt(13, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(14, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            cstmt.setString(15, "");
            cstmt.setString(16, otrosIngresosBean.getDescripcion());

            //REGISTER_OUT_PARAMETER
            cstmt.registerOutParameter("bEjecuto", Types.SMALLINT);
            cstmt.registerOutParameter("iNumFolioContab", Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            //EXECUTE_CALLABLESTATEMENT
            cstmt.execute();

            //GET_OUT_PARAMETER
            int bEjecuto = cstmt.getInt("bEjecuto");
            int iNumFolioContab = cstmt.getInt("iNumFolioContab");
            String psMsgErrOut = cstmt.getString("PS_MSGERR_OUT");

            //Set_Values
            outParameterBean.setbEjecuto(bEjecuto);
            outParameterBean.setiNumFolioContab(iNumFolioContab);
            outParameterBean.setPsMsgErrOut(psMsgErrOut);

            onCierraConexion();

            //SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            bitacoraBean.setNombre("Generacion Otros Ingresos".toUpperCase());
            bitacoraBean.setFuncion("honorariosGenOtrosIng".toUpperCase());
            Double gTotal = CValidacionesUtil.validarDouble(otrosIngresosBean.getTotal()); bitacoraBean.setDetalle("SERVICIO DE (".concat(otrosIngresosBean.getNombreServicio().concat(") POR UN TOTAL DE ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(gTotal)))));

            //INSERT_BITACORA
            onHonorarios_InsertBitacora(bitacoraBean);
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeGeneraHonorariosSp()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeGeneraHonorariosSp()");

        } finally {
            onCierraConexion();

        }

        return outParameterBean;
    }

    public int onHonorarios_UpdateParametrosCalculoHonorarios(OtrosIngresosBean otrosIngresosBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //LocalDate
        LocalDate localDateFechaCalculo = LocalDate.now();
        LocalDate localDateFechaSiguiente = LocalDate.now();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Variables
        int result = 0;
        double provisionMes = 0;
        String fechaCalculo = "";
        String fechaSiguiente = "";

        //Get_Periodo
        String periodo = otrosIngresosBean.getPeriodoCobro().substring(0, otrosIngresosBean.getPeriodoCobro().indexOf(" "));

        //Get_Tiempo
        String tiempo = otrosIngresosBean.getPeriodoCobro().substring(otrosIngresosBean.getPeriodoCobro().indexOf(" "), otrosIngresosBean.getPeriodoCobro().length());
        Double gTotal = CValidacionesUtil.validarDouble(otrosIngresosBean.getTotal());
        if ("ADELANTADA".equals(tiempo.trim())) {
            switch (periodo) {
                case "MENSUAL":
                    provisionMes = 0;
                    break;
                case "BIMESTRAL":
                    provisionMes = gTotal / 2;
                    break;
                case "TRIMESTRAL":
                    provisionMes = gTotal / 3;
                    break;
                case "CUATRIMESTRAL":
                    provisionMes = gTotal / 4;
                    break;
                case "SEMESTRAL":
                    provisionMes = gTotal / 6;
                    break;
                case "ANUAL":
                    provisionMes = gTotal / 12;
                    break;
            }
        }

        //Set_Local_Date
        LocalDate localDateProyectada = onHonorarios_proyectaFecha(otrosIngresosBean.getFechaAl().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), periodo);
        LocalDate localDate = LocalDate.now();
 
        //Get_Fecha_Calculo
        switch (tiempo.trim()) {
            case "ADELANTADA":
                localDateFechaCalculo = otrosIngresosBean.getFechaDel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(-1);
                fechaCalculo = localDateFechaCalculo.format(dateTimeformatter);
                localDateFechaSiguiente = otrosIngresosBean.getFechaAl().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaSiguiente = localDateFechaSiguiente.format(dateTimeformatter);
                break;
            case "VENCIDA":
                localDateFechaCalculo = otrosIngresosBean.getFechaAl().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaCalculo = localDateFechaCalculo.format(dateTimeformatter);
                localDateFechaSiguiente = localDateProyectada;
                fechaSiguiente = localDateFechaSiguiente.format(dateTimeformatter);
                break;
        }
 
        try {
            //Set_SQL
            stringBuilder.append("UPDATE SAF.PACAHON SET PAC_FEC_ULT_CALC = ?, PAC_ANO_CALC_HONO = ?, PAC_MES_CALC_HONO = ?, PAC_DIA_CALC_HONO = ?, PAC_IMP_ADIC_HONO = ?, PAC_ANO_ULT_MOD = ?, PAC_MES_ULT_MOD = ?, PAC_DIA_ULT_MOD = ?, PAC_DIAS_CALC_HONO = ? WHERE  PAC_NUM_CONTRATO = ?");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Period
            Period period = Period.between(otrosIngresosBean.getFechaAl().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), otrosIngresosBean.getFechaDel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1));

            //Set_Values
            pstmt.setDate(1, java.sql.Date.valueOf(fechaCalculo));
            pstmt.setInt(2, LocalDate.parse(fechaSiguiente, dateTimeformatter).getYear());
            pstmt.setInt(3, LocalDate.parse(fechaSiguiente, dateTimeformatter).getMonthValue());
            pstmt.setInt(4, LocalDate.parse(fechaSiguiente, dateTimeformatter).getDayOfMonth());
            pstmt.setDouble(5, provisionMes);
            pstmt.setInt(6, localDate.getYear());
            pstmt.setInt(7, localDate.getMonthValue());
            pstmt.setInt(8, localDate.getDayOfMonth());
            pstmt.setInt(9, period.getDays());
            pstmt.setInt(10, Integer.parseInt(otrosIngresosBean.getFideicomiso()));

            //Execute_Update
            result = pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_UpdateParametrosCalculoHonorarios()");

            //
        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_UpdateParametrosCalculoHonorarios()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public List<DetCartBean> onHonorarios_GetCartera(int fideicomiso, String fecha) {
        //Objects
        List<DetCartBean> pagos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        //Format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //Date_Format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_LocalDate
        LocalDate localDate = LocalDate.parse(fecha, dateTimeFormatter);
 
        try {
            //SQL 
            stringBuilder.append("SELECT DEC_NUM_CONTRATO, ");
            stringBuilder.append("       DEC_FEC_CALC_HONO, ");
            stringBuilder.append("       DEC_CVE_TIPO_HONO, ");
            stringBuilder.append("       DEC_CVE_PERS_FID, ");
            stringBuilder.append("       DEC_NUM_PERS_FID, ");
            stringBuilder.append("       DEC_NUM_MONEDA, ");
            stringBuilder.append("       MON_NOM_MONEDA, ");
            stringBuilder.append("       DEC_NUM_SECUENCIAL, ");
            stringBuilder.append("       (DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP) AS ADEUDO, ");
            stringBuilder.append("       DEC_IMP_REM_HONOR, ");
            stringBuilder.append("       DEC_REM_IVA_HONOR, ");
            stringBuilder.append("       DEC_REM_EXTEMP, ");
            stringBuilder.append("       DEC_NUM_PAGOS_EFE, ");
            stringBuilder.append("       DEC_IMP_PAGOS_EFE, ");
            stringBuilder.append("       DEC_CVE_CALIF_HONO, ");
            stringBuilder.append("       DEC_CONCEPTO_HONO, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_DEL))||'-'||RTRIM(CHAR(DEC_MES_PER_DEL))||'-'||RTRIM(CHAR(DEC_DIA_PER_DEL))) DEL, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_AL))||'-'||RTRIM(CHAR(DEC_MES_PER_AL))||'-'||RTRIM(CHAR(DEC_DIA_PER_AL))) AL, ");
            stringBuilder.append("       CASE LEFT(SUBSTR(CASE PAC_CVE_PERIOD_COB||'' WHEN '' THEN 'MENSUAL ADELANTADA' ELSE PAC_CVE_PERIOD_COB END ,LENGTH(CASE PAC_CVE_PERIOD_COB||'' WHEN '' THEN 'MENSUAL ADELANTADA' ELSE PAC_CVE_PERIOD_COB END)-9),10) WHEN 'ADELANTADA' THEN (DAYS(CAST(? AS DATE)) - DAYS(DATE(RTRIM(CHAR(DEC_ANO_PER_DEL)) ||'-'||RTRIM(CHAR(DEC_MES_PER_DEL))||'-'||RTRIM(CHAR(DEC_DIA_PER_DEL))))) ELSE (DAYS(CAST(? AS DATE)) - DAYS(DATE(RTRIM(CHAR(DEC_ANO_PER_AL))||'-'||RTRIM(CHAR(DEC_MES_PER_AL))||'-'||RTRIM(CHAR(DEC_DIA_PER_AL))))) END AS DIAS_ATRASO, ");
            stringBuilder.append("       PAC_CVE_PERIOD_COB, ");
            stringBuilder.append("       PAC_INTERMED, ");
            stringBuilder.append("       PAC_CTO_INVER, ");
            stringBuilder.append("       DEC_NUM_TRAMITE,  ");
            stringBuilder.append("       FID_NOM_FIDEICOM, ");
            stringBuilder.append("       BEN_NOM_BENEF ");
            stringBuilder.append(" FROM (DETCART LEFT OUTER JOIN MONEDAS  ON DEC_NUM_MONEDA=MON_NUM_PAIS ");
            stringBuilder.append("               LEFT OUTER JOIN PACAHON ON DEC_NUM_CONTRATO = PAC_NUM_CONTRATO ");
            stringBuilder.append("               LEFT OUTER JOIN BENEFICI ON DEC_NUM_CONTRATO = BEN_NUM_CONTRATO AND DEC_NUM_PERS_FID = BEN_BENEFICIARIO AND DEC_CVE_PERS_FID='FIDEICOMISARIO' ");
            stringBuilder.append("               LEFT OUTER JOIN FIDEICOM ON DEC_NUM_CONTRATO=FID_NUM_CONTRATO   AND DEC_NUM_PERS_FID=FID_FIDEICOMITENTE AND DEC_CVE_PERS_FID='FIDEICOMITENTE') ");
            stringBuilder.append(" WHERE DEC_NUM_CONTRATO = ? ");
            stringBuilder.append("   AND DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP > 0 ");
            stringBuilder.append("   AND DEC_CVE_TIPO_HONO NOT IN ('HONORARIOS POR ADMINISTRACION','GASTOS DE COBRANZA') ");
            stringBuilder.append("ORDER BY 1, 2, 3, 4, 5, 6, 8 ");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, localDate.format(dateTimeFormatter2));
            pstmt.setString(2, localDate.format(dateTimeFormatter2));
            pstmt.setInt(3, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) {

                while (rs.next()) {
                    //Object
                    DetCartBean detCartBean = new DetCartBean();

                    //Set_Values
                    detCartBean.setFideicomiso(rs.getString("DEC_NUM_CONTRATO"));
                    detCartBean.setSubFiso("0");
                    detCartBean.setFormaPago("DESCONTAR DEL FONDO");
                    detCartBean.setFecha(rs.getDate("DEC_FEC_CALC_HONO"));
                    detCartBean.setFechaCalculo(simpleDateFormat.format(detCartBean.getFecha()));
                    detCartBean.setHonorario(rs.getString("DEC_CVE_TIPO_HONO"));
                    detCartBean.setPersona(rs.getString("DEC_CVE_PERS_FID"));
                    detCartBean.setNoPersona(rs.getInt("DEC_NUM_PERS_FID"));

                    if ("CARGO AL FONDO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre("CARGO AL FONDO");
                    } else if ("FIDEICOMISARIO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre(rs.getString("BEN_NOM_BENEF"));
                    } else {
                        detCartBean.setPersonaNombre(rs.getString("FID_NOM_FIDEICOM"));
                    }

                    detCartBean.setNoMoneda(rs.getInt("DEC_NUM_MONEDA"));
                    detCartBean.setMoneda(rs.getString("MON_NOM_MONEDA"));
                    detCartBean.setSecuencial(rs.getInt("DEC_NUM_SECUENCIAL"));
                    detCartBean.setAdeudoImp(rs.getDouble("ADEUDO"));
                    detCartBean.setAdeudo(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("ADEUDO")))));
                    detCartBean.setImporteImp(rs.getDouble("DEC_IMP_REM_HONOR"));
                    detCartBean.setImporte(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_IMP_REM_HONOR")))));
                    detCartBean.setIvaImp(rs.getDouble("DEC_REM_IVA_HONOR"));
                    detCartBean.setIva(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_IVA_HONOR")))).replace("($", "$-").replace(")", ""));
                    detCartBean.setMoratoriosImp(rs.getDouble("DEC_REM_EXTEMP"));
                    detCartBean.setMoratorios(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_EXTEMP")))));
                    detCartBean.setNoPagos(rs.getInt("DEC_NUM_PAGOS_EFE"));
                    detCartBean.setImpPagosImp(rs.getInt("DEC_IMP_PAGOS_EFE"));
                    detCartBean.setImpPagos(String.valueOf(NumberFormat.getInstance(new Locale("en", "US")).format(rs.getInt("DEC_IMP_PAGOS_EFE"))));
                    detCartBean.setCalificacion(rs.getString("DEC_CVE_CALIF_HONO"));
                    detCartBean.setConcepto(rs.getString("DEC_CONCEPTO_HONO"));
                    detCartBean.setFechaDel(simpleDateFormat.format(rs.getDate("DEL")));
                    detCartBean.setFechaAl(simpleDateFormat.format(rs.getDate("AL")));
                    detCartBean.setDiasAtraso(rs.getInt("DIAS_ATRASO"));
                    detCartBean.setPeriodoCobro(rs.getString("PAC_CVE_PERIOD_COB"));
                    detCartBean.setIntermediario(rs.getInt("PAC_INTERMED"));
                    detCartBean.setCtoInversion(rs.getInt("PAC_CTO_INVER"));

                    //Add_List
                    pagos.add(detCartBean);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCartera()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCartera()");

        } finally {
            onCierraConexion();
        }

        return pagos;
    }

    public List<String> onHonorarios_GetSubFisoAndCtoInver(String fideicomiso) {
        //Objects
        List<String> parameters = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT ADC_SUB_CONTRATO, PAC_CTO_INVER FROM FID_CTAS C JOIN PACAHON P ON P.PAC_NUM_CONTRATO = C.ADC_NUM_CONTRATO AND DECIMAL(ADC_CUENTA) = PAC_CTO_INVER WHERE PAC_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, Integer.parseInt(fideicomiso));

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_Parameters
                if (rs.next()) {
                    //Set_SubCto
                    String subCto = rs.getString("ADC_SUB_CONTRATO");
                    //Set_CtoInver
                    String ctoInver = rs.getString("PAC_CTO_INVER");

                    //Add_List
                    parameters.add(subCto);
                    parameters.add(ctoInver);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat(" onOperacion_GetSecuenciaCalProd()");

        } finally {
            onCierraConexion();
        }

        return parameters;
    }

    public boolean onHonorarios_CheckLiquidez(String fideicomiso, String subCto, int moneda, String ctoInver, double importe) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();
        fideicomiso = realScapeString(fideicomiso);
        subCto = realScapeString(subCto);
        ctoInver = realScapeString(ctoInver);

        //Variables
        boolean checkLiquidez = false;
 
        try {
            //SQL 
            stringBuilder.append("SELECT SALD_SALDO_ACTUAL FROM FDSALDOS WHERE CCON_CTA = ? AND CCON_SCTA = ? AND CCON_2SCTA = ? AND CCON_3SCTA = ? AND SALD_AX1 = ? AND  SALD_AX2 = ? AND MONE_ID_MONEDA = ? AND SALD_AX3 = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, 1103);
            pstmt.setInt(2, 1);
            pstmt.setInt(3, 2);
            pstmt.setInt(4, 1);
            pstmt.setInt(5, Integer.parseInt(fideicomiso));
            pstmt.setInt(6, Integer.parseInt(subCto));
            pstmt.setInt(7, moneda);
            pstmt.setInt(8, Integer.parseInt(ctoInver));

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {

                if (rs.next()) {
                    //Set_Liquidez 
                    double saldo = rs.getDouble("SALD_SALDO_ACTUAL");

                    //Check_Liquidez
                    if (importe > saldo) {
                        checkLiquidez = true;
                    }
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_CheckLiquidez()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_CheckLiquidez()");

        } finally {
            onCierraConexion();
        }

        return checkLiquidez;
    }

    public PacaHonBean onHonorarios_GetPacaHon(int fideicomiso) {
        //Objects
        PacaHonBean pacaHonBean = new PacaHonBean();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            //SQL 
            stringBuilder.append("SELECT PAC_CVE_PERS_COB, CTO_NOM_CONTRATO FROM PACAHON, CONTRATO WHERE PAC_NUM_CONTRATO = CTO_NUM_CONTRATO AND PAC_NUM_CONTRATO = ?");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values 
            pstmt.setInt(1, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) {

                if (rs.next()) {
                    //Set_Values
                    pacaHonBean.setPersonaCobra(rs.getString("PAC_CVE_PERS_COB"));
                    pacaHonBean.setContratoNombre(rs.getString("CTO_NOM_CONTRATO"));
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetPacaHon()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetPacaHon()");

        } finally {
            onCierraConexion();
        }

        return pacaHonBean;
    }

    public List<ParticipanteBean> onHonorarios_GetPorcentajeParticipantesPago(String participante, String fideicomiso) {
        //Objects
        List<ParticipanteBean> participantes = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            switch (participante) {
                case "FIDEICOMITENTE":
                    //SQL 
                    stringBuilder.append("SELECT FID_NUM_CONTRATO FISO, 'FIDEICOMITENTE' TIPO_PARTICIPANTE, FID_FIDEICOMITENTE NO_PARTICIPANTE, REPLACE(FID_NOM_FIDEICOM,chr(9),' ') NOM_PARTICIPANTE, 0 PORCENTAJE FROM FIDEICOM WHERE FID_CVE_ST_FIDEICO = ? AND FID_NUM_CONTRATO = ?");
                    break;
                case "FIDEICOMISARIO":
                    //SQL 
                    stringBuilder.append("SELECT BEN_NUM_CONTRATO FISO, 'FIDEICOMISARIO' TIPO_PARTICIPANTE, BEN_BENEFICIARIO NO_PARTICIPANTE, REPLACE(BEN_NOM_BENEF,chr(9),' ') NOM_PARTICIPANTE, 0 PORCENTAJE FROM BENEFICI WHERE BEN_CVE_ST_BENEFIC = ? AND BEN_NUM_CONTRATO = ?");
                    break;
                default:
                    stringBuilder.append("SELECT FID_NUM_CONTRATO FISO, 'FIDEICOMITENTE' TIPO_PARTICIPANTE, FID_FIDEICOMITENTE NO_PARTICIPANTE, REPLACE(FID_NOM_FIDEICOM,chr(9),' ') NOM_PARTICIPANTE, 0 PORCENTAJE FROM FIDEICOM WHERE FID_CVE_ST_FIDEICO = ? AND FID_NUM_CONTRATO = ? ");
                    stringBuilder.append(" UNION ALL ");
                    stringBuilder.append("SELECT BEN_NUM_CONTRATO FISO, 'FIDEICOMISARIO' TIPO_PARTICIPANTE, BEN_BENEFICIARIO NO_PARTICIPANTE, REPLACE(BEN_NOM_BENEF,chr(9),' ') NOM_PARTICIPANTE, 0 PORCENTAJE FROM BENEFICI WHERE BEN_CVE_ST_BENEFIC = ? AND BEN_NUM_CONTRATO = ? ");
                    stringBuilder.append(" UNION ALL ");
                    stringBuilder.append("SELECT TER_NUM_CONTRATO FISO, 'TERCERO' TIPO_PARTICIPANTE, TER_NUM_TERCERO NO_PARTICIPANTE, REPLACE(TER_NOM_TERCERO,chr(9),' ') NOM_PARTICIPANTE, 0 PORCENTAJE FROM TERCEROS WHERE TER_CVE_ST_TERCERO = ? AND TER_NUM_CONTRATO = ? ");
            }
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Switch_Participante
            switch (participante) {
                case "FIDEICOMITENTE":
                    pstmt.setString(1, "ACTIVO");
                    pstmt.setString(2, realScapeString(fideicomiso));
                    break;
                case "FIDEICOMISARIO":
                    pstmt.setString(1, "ACTIVO");
                    pstmt.setString(2, realScapeString(fideicomiso));
                    break;
                default:
                    pstmt.setString(1, "ACTIVO");
                    pstmt.setString(2, realScapeString(fideicomiso));
                    pstmt.setString(3, "ACTIVO");
                    pstmt.setString(4, realScapeString(fideicomiso));
                    pstmt.setString(5, "ACTIVO");
                    pstmt.setString(6, realScapeString(fideicomiso));
            }

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) {

                while (rs.next()) {
                    //Object
                    ParticipanteBean participanteBean = new ParticipanteBean();

                    //Set_Values
                    participanteBean.setFideicomiso(rs.getString("FISO"));
                    participanteBean.setTipoParticipante(rs.getString("TIPO_PARTICIPANTE"));
                    participanteBean.setNoParticipante(rs.getString("NO_PARTICIPANTE"));
                    participanteBean.setParticipante(rs.getString("NOM_PARTICIPANTE"));
                    participanteBean.setPorcentaje(rs.getInt("PORCENTAJE"));

                    //Add_List
                    participantes.add(participanteBean);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetPorcentajeParticipantesPago()");

        } finally {
            onCierraConexion();
        }

        return participantes;
    }

    public int onHonorarios_InsertPorcentajeParticipacion(ParticipanteBean participanteBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Variables
        int result = 0;

        //Set_Date
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(dateTimeformatter);

        try {
            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.PARAPAHOPE (HON_FOLIO_CONTA, HON_NUM_CONTRATO, HON_CVE_PERS_FID, HON_NUM_PERS_FID, HON_PJE_FRAG_COMIS, HON_FEC_ALTA, HON_NUM_USUARIO, HON_TIPO_OPE)");
            stringBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(2, Integer.parseInt(realScapeString(participanteBean.getFideicomiso())));
            pstmt.setString(3, participanteBean.getNoParticipante());
            pstmt.setInt(4, Integer.parseInt(participanteBean.getNoParticipante()));
            pstmt.setInt(5, participanteBean.getPorcentaje());
            pstmt.setDate(6, java.sql.Date.valueOf(LocalDate.parse(date, dateTimeformatter)));
            pstmt.setInt(7, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setString(8, realScapeString(participanteBean.getTipoParticipante()));

            //Execute_Update
            result = pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_InsertPorcentajeParticipacion()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_InsertPorcentajeParticipacion()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public OutParameterBean onHonorarios_executeApplyPaySp(DetCartBean detCartBean, FechaContableBean fechaContableBean) {
        //Objects
        OutParameterBean outParameterBean = new OutParameterBean();

        //Format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
        //SQL
        String sql = "{ CALL DB2FIDUC.SPN_APLICA_PAGOHON (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        //GET_CALLABLESTATEMENT
        try {
            //Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sql);

            //REGISTER_IN_PARAMETER
            cstmt.setInt(1, Integer.parseInt(realScapeString(detCartBean.getFideicomiso())));
            cstmt.setInt(2, detCartBean.getIntermediario());
            cstmt.setInt(3, detCartBean.getCtoInversion());
            cstmt.setDouble(4, detCartBean.getImporteImp());
            cstmt.setDouble(5, detCartBean.getIvaImp());
            cstmt.setDouble(6, 0);
            cstmt.setDouble(7, 0);
            cstmt.setDouble(8, detCartBean.getMoratoriosImp());
            cstmt.setInt(9, detCartBean.getNoMoneda());
            cstmt.setString(10, detCartBean.getHonorario());
            cstmt.setString(11, detCartBean.getFormaPago()); 
            cstmt.setDate(12, java.sql.Date.valueOf(simpleDateFormat.format(fechaContableBean.getFecha())));
            cstmt.setDate(13, java.sql.Date.valueOf(simpleDateFormat.format(detCartBean.getFecha())));
            cstmt.setDouble(14, 0);

            //Check_Factura
            if (detCartBean.isEspecificaFactura()) {
                cstmt.setInt(15, 1);
            } else {
                cstmt.setInt(15, 0);
            }

            cstmt.setString(16, detCartBean.getPersona());
            cstmt.setInt(17, detCartBean.getNoPersona());
            cstmt.setInt(18, detCartBean.getSecuencial());
            cstmt.setInt(19, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(20, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            cstmt.setString(21, "");
            cstmt.setString(22, String.valueOf(detCartBean.getMetodoPago()));

            //REGISTER_OUT_PARAMETER
            cstmt.registerOutParameter("bEjecuto", Types.SMALLINT);
            cstmt.registerOutParameter("iNumFolioContab", Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            //EXECUTE_CALLABLESTATEMENT
            cstmt.execute();

            //GET_OUT_PARAMETER
            int bEjecuto = cstmt.getInt("bEjecuto");
            int iNumFolioContab = cstmt.getInt("iNumFolioContab");
            String psMsgErrOut = cstmt.getString("PS_MSGERR_OUT");

            //Set_Values
            outParameterBean.setbEjecuto(bEjecuto);
            outParameterBean.setiNumFolioContab(iNumFolioContab);
            outParameterBean.setPsMsgErrOut(psMsgErrOut);

            onCierraConexion();

            //SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            bitacoraBean.setNombre("Pago Honorarios".toUpperCase());
            bitacoraBean.setFuncion("honorariosPago".toUpperCase());
            bitacoraBean.setDetalle("PAGO CONTROLADO CON EL FIDEICOMISO ".concat(detCartBean.getFideicomiso().concat(". POR UN IMPORTE DE: ".concat(detCartBean.getAdeudo()).concat(" EN ".concat(detCartBean.getMoneda())))));

            //INSERT_BITACORA
            onHonorarios_InsertBitacora(bitacoraBean);
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPaySp()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPaySp()");

        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public int onHonorarios_DeletePorcentajeParticipacion(int fideicomiso, String fecha) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int result = 0;

        try {
            //Set_SQL
            stringBuilder.append("DELETE FROM SAF.PARAPAHOPE WHERE HON_FOLIO_CONTA = ? AND HON_NUM_CONTRATO = ? AND HON_FEC_ALTA = ?");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(2, fideicomiso);
            pstmt.setDate(3, java.sql.Date.valueOf(fecha));

            //Execute_Update
            result = pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            result = sqlException.getErrorCode();
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_DeletePorcentajeParticipacion()");

            //
        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_DeletePorcentajeParticipacion()");

        } finally {
            onCierraConexion();
        }

        return result;
    }

    public List<DetCartBean> onHonorarios_GetCarteraPagos(int fideicomiso, String fecha) {
        //Objects
        List<DetCartBean> pagos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        //Format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //Date_Format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_LocalDate
        LocalDate localDate = LocalDate.parse(fecha, dateTimeFormatter);
 
        try {
            //SQL 
            stringBuilder.append("SELECT DEC_NUM_CONTRATO, ");
            stringBuilder.append("       DEC_FEC_CALC_HONO, ");
            stringBuilder.append("       DEC_CVE_TIPO_HONO, ");
            stringBuilder.append("       DEC_CVE_PERS_FID, ");
            stringBuilder.append("       DEC_NUM_PERS_FID, ");
            stringBuilder.append("       DEC_NUM_MONEDA, ");
            stringBuilder.append("       MON_NOM_MONEDA, ");
            stringBuilder.append("       DEC_NUM_SECUENCIAL, ");
            stringBuilder.append("       (DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP) AS ADEUDO, ");
            stringBuilder.append("       DEC_IMP_REM_HONOR, ");
            stringBuilder.append("       DEC_REM_IVA_HONOR, ");
            stringBuilder.append("       DEC_REM_EXTEMP, ");
            stringBuilder.append("       DEC_NUM_PAGOS_EFE, ");
            stringBuilder.append("       DEC_IMP_PAGOS_EFE, ");
            stringBuilder.append("       DEC_CVE_CALIF_HONO, ");
            stringBuilder.append("       DEC_CONCEPTO_HONO, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_DEL))||'-'||RTRIM(CHAR(DEC_MES_PER_DEL))||'-'||RTRIM(CHAR(DEC_DIA_PER_DEL))) DEL, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_AL))||'-'||RTRIM(CHAR(DEC_MES_PER_AL))||'-'||RTRIM(CHAR(DEC_DIA_PER_AL))) AL, ");
            stringBuilder.append("       CASE LEFT(SUBSTR(CASE PAC_CVE_PERIOD_COB||'' WHEN '' THEN 'MENSUAL ADELANTADA' ELSE PAC_CVE_PERIOD_COB END ,LENGTH(CASE PAC_CVE_PERIOD_COB||'' WHEN '' THEN 'MENSUAL ADELANTADA' ELSE PAC_CVE_PERIOD_COB END)-9),10) WHEN 'ADELANTADA' THEN (DAYS(CAST(? AS DATE)) - DAYS(DATE(RTRIM(CHAR(DEC_ANO_PER_DEL)) ||'-'||RTRIM(CHAR(DEC_MES_PER_DEL))||'-'||RTRIM(CHAR(DEC_DIA_PER_DEL))))) ELSE (DAYS(CAST(? AS DATE)) - DAYS(DATE(RTRIM(CHAR(DEC_ANO_PER_AL))||'-'||RTRIM(CHAR(DEC_MES_PER_AL))||'-'||RTRIM(CHAR(DEC_DIA_PER_AL))))) END AS DIAS_ATRASO, ");
            stringBuilder.append("       PAC_CVE_PERIOD_COB, ");
            stringBuilder.append("       PAC_INTERMED, ");
            stringBuilder.append("       PAC_CTO_INVER, ");
            stringBuilder.append("       DEC_NUM_SERVICIO, ");
            stringBuilder.append("       DEC_NUM_TRAMITE,  ");
            stringBuilder.append("       FID_NOM_FIDEICOM, ");
            stringBuilder.append("       BEN_NOM_BENEF ");
            stringBuilder.append(" FROM (DETCART LEFT OUTER JOIN MONEDAS  ON DEC_NUM_MONEDA=MON_NUM_PAIS ");
            stringBuilder.append("               LEFT OUTER JOIN PACAHON ON DEC_NUM_CONTRATO = PAC_NUM_CONTRATO ");
            stringBuilder.append("               LEFT OUTER JOIN BENEFICI ON DEC_NUM_CONTRATO = BEN_NUM_CONTRATO AND DEC_NUM_PERS_FID = BEN_BENEFICIARIO AND DEC_CVE_PERS_FID='FIDEICOMISARIO' ");
            stringBuilder.append("               LEFT OUTER JOIN FIDEICOM ON DEC_NUM_CONTRATO = FID_NUM_CONTRATO   AND DEC_NUM_PERS_FID=FID_FIDEICOMITENTE AND DEC_CVE_PERS_FID='FIDEICOMITENTE') ");
            stringBuilder.append(" WHERE DEC_NUM_CONTRATO = ? ");
            stringBuilder.append("   AND DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP > 0 ");
            stringBuilder.append("   AND (DEC_CVE_TIPO_HONO IN ('HONORARIOS POR ADMINISTRACION') ");
            stringBuilder.append("    OR DEC_CONCEPTO_HONO like 'GASTOS DE COBRANZA%' ");
            stringBuilder.append("    OR DEC_CONCEPTO_HONO like '%HONORARIOS POR ADMINISTRACION%') ");
            stringBuilder.append(" ORDER BY DEC_CONCEPTO_HONO DESC, DEL,DEC_FEC_CALC_HONO, DEC_CVE_PERS_FID, DEC_NUM_PERS_FID, DEC_NUM_MONEDA,  DEC_NUM_SECUENCIAL ");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setString(1, localDate.format(dateTimeFormatter2));
            pstmt.setString(2, localDate.format(dateTimeFormatter2));
            pstmt.setInt(3, fideicomiso);

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) {

                while (rs.next()) {
                    //Object
                    DetCartBean detCartBean = new DetCartBean();

                    //Set_Values
                    detCartBean.setFideicomiso(rs.getString("DEC_NUM_CONTRATO"));
                    detCartBean.setSubFiso("0");
                    detCartBean.setFormaPago("DESCONTAR DEL FONDO");
                    detCartBean.setFecha(rs.getDate("DEC_FEC_CALC_HONO"));
                    detCartBean.setFechaCalculo(simpleDateFormat.format(detCartBean.getFecha()));
                    detCartBean.setHonorario(rs.getString("DEC_CVE_TIPO_HONO"));
                    detCartBean.setPersona(rs.getString("DEC_CVE_PERS_FID"));
                    detCartBean.setNoPersona(rs.getInt("DEC_NUM_PERS_FID"));

                    if ("CARGO AL FONDO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre("CARGO AL FONDO");
                    } else if ("FIDEICOMISARIO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre(rs.getString("BEN_NOM_BENEF"));
                    } else {
                        detCartBean.setPersonaNombre(rs.getString("FID_NOM_FIDEICOM"));
                    }

                    detCartBean.setNoMoneda(rs.getInt("DEC_NUM_MONEDA"));
                    detCartBean.setMoneda(rs.getString("MON_NOM_MONEDA"));
                    detCartBean.setSecuencial(rs.getInt("DEC_NUM_SECUENCIAL"));
                    detCartBean.setAdeudoImp(rs.getDouble("ADEUDO"));
                    detCartBean.setAdeudo(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("ADEUDO")))));
                    detCartBean.setImporteImp(rs.getDouble("DEC_IMP_REM_HONOR"));
                    detCartBean.setImporte(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_IMP_REM_HONOR")))));
                    detCartBean.setIvaImp(rs.getDouble("DEC_REM_IVA_HONOR"));
                    detCartBean.setIva(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_IVA_HONOR")))).replace("($", "$-").replace(")", ""));
                    detCartBean.setMoratoriosImp(rs.getDouble("DEC_REM_EXTEMP"));
                    detCartBean.setMoratorios(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_EXTEMP")))));
                    detCartBean.setNoPagos(rs.getInt("DEC_NUM_PAGOS_EFE"));
                    detCartBean.setImpPagosImp(rs.getInt("DEC_IMP_PAGOS_EFE"));
                    detCartBean.setImpPagos(String.valueOf(NumberFormat.getInstance(new Locale("en", "US")).format(rs.getInt("DEC_IMP_PAGOS_EFE"))));
                    detCartBean.setCalificacion(rs.getString("DEC_CVE_CALIF_HONO"));
                    detCartBean.setConcepto(rs.getString("DEC_CONCEPTO_HONO"));
                    detCartBean.setFechaDel(simpleDateFormat.format(rs.getDate("DEL")));
                    detCartBean.setFechaAl(simpleDateFormat.format(rs.getDate("AL")));
                    detCartBean.setDiasAtraso(rs.getInt("DIAS_ATRASO"));
                    detCartBean.setPeriodoCobro(rs.getString("PAC_CVE_PERIOD_COB"));
                    detCartBean.setIntermediario(rs.getInt("PAC_INTERMED"));
                    detCartBean.setServicio(rs.getInt("DEC_NUM_SERVICIO"));
                    detCartBean.setCtoInversion(rs.getInt("PAC_CTO_INVER"));

                    //Add_List
                    pagos.add(detCartBean);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCarteraPagos()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCarteraPagos()");

        } finally {
            onCierraConexion();
        }

        return pagos;
    }

    public OutParameterBean onHonorarios_executeApplyPayHonorarioSp(DetCartBean detCartBean, FechaContableBean fechaContableBean, int deudaEspecifica) {
        //Objects
        OutParameterBean outParameterBean = new OutParameterBean();

        //Format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
        //SQL
        String sql = "{CALL DB2FIDUC.SPN_HONAPLICAPAGO (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        //GET_CALLABLESTATEMENT
        try {
            //Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sql);

            //REGISTER_IN_PARAMETER
            cstmt.setInt(1, Integer.parseInt(realScapeString(detCartBean.getFideicomiso())));
            cstmt.setString(2, detCartBean.getPersona());
            cstmt.setInt(3, detCartBean.getNoPersona());
            cstmt.setInt(4, detCartBean.getSecuencial());
            cstmt.setDate(5, java.sql.Date.valueOf(simpleDateFormat.format(detCartBean.getFecha())));
            cstmt.setString(6, realScapeString(detCartBean.getHonorario()));
            cstmt.setInt(7, detCartBean.getNoMoneda());

            //Set_Importe
            if (deudaEspecifica == 1) {
                cstmt.setDouble(8, detCartBean.getAdeudoImp());
            } else {
                cstmt.setDouble(8, detCartBean.getImporteTotalImp());
            }

            cstmt.setString(9,detCartBean.getFormaPago());
            cstmt.setString(10,String.format("%02d",detCartBean.getMetodoPago())); 

            if (detCartBean.isEspecificaFactura()) {
                cstmt.setString(11, "SI");
            } else {
                cstmt.setString(11, "NO");
            }

            cstmt.setInt(12, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(13, "");
            cstmt.setInt(14, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(15, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            cstmt.setInt(16, deudaEspecifica);

            //REGISTER_OUT_PARAMETER
            cstmt.registerOutParameter("bEjecuto", Types.SMALLINT);
            cstmt.registerOutParameter("dImporteCobrado", Types.DECIMAL);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            //EXECUTE_CALLABLESTATEMENT
            cstmt.execute();

            //GET_OUT_PARAMETER
            int bEjecuto = cstmt.getInt("bEjecuto");
            double dImporteCobrado = cstmt.getDouble("dImporteCobrado");
            String psMsgErrOut = cstmt.getString("PS_MSGERR_OUT");

            //Set_Values
            outParameterBean.setbEjecuto(bEjecuto);
            outParameterBean.setdImporteCobrado(dImporteCobrado);
            outParameterBean.setPsMsgErrOut(psMsgErrOut);

            onCierraConexion();

            //SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            bitacoraBean.setNombre("PAGO HONORARIO".toUpperCase());

            if (deudaEspecifica == 1) {
                bitacoraBean.setFuncion("honorariosPagoEsp".toUpperCase());
                bitacoraBean.setDetalle("Pago especifico aplicado por un importe de: ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(detCartBean.getAdeudoImp())).toUpperCase());
            } else {
                bitacoraBean.setFuncion("honorariosPagoPrelacion".toUpperCase());
                bitacoraBean.setDetalle("Pago con prelación aplicado por un importe de: ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(detCartBean.getImporteTotalImp())).toUpperCase());
            }

            //INSERT_BITACORA
            onHonorarios_InsertBitacora(bitacoraBean);
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPayHonorarioSp()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPayHonorarioSp()");

        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    public List<String> onHonorarios_GetPersonsPay(int fideicomiso) {
        //Objects
        List<String> personsPay = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Connection conect = null;
        PreparedStatement prepareStmt = null;
        ResultSet resSet = null;
        try {
            //SQL

            stringBuilder.append("SELECT DEC_CVE_PERS_FID,");
            stringBuilder.append(" DEC_NUM_PERS_FID, ");
            stringBuilder.append(" MON_NOM_MONEDA, ");
            stringBuilder.append(" SUM(COALESCE(DEC_IMP_REM_HONOR,0) + COALESCE(DEC_REM_IVA_HONOR,0) + COALESCE(DEC_REM_EXTEMP,0)) ADEUDA_PERSONA ");
            stringBuilder.append(" FROM DETCART, MONEDAS, PACAHON ");
            stringBuilder.append(" WHERE DEC_NUM_MONEDA = MON_NUM_PAIS ");
            stringBuilder.append(" AND DEC_NUM_CONTRATO = PAC_NUM_CONTRATO ");
            stringBuilder.append(" AND DEC_NUM_CONTRATO = ?");
            stringBuilder.append(" AND DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP > 0 ");
            stringBuilder.append(" AND DEC_CVE_TIPO_HONO IN ('HONORARIOS POR ADMINISTRACION','GASTOS DE COBRANZA') ");
            stringBuilder.append(" AND (DEC_CVE_TIPO_HONO IN ('HONORARIOS POR ADMINISTRACION','GASTOS DE COBRANZA') ");
            stringBuilder.append("      OR DEC_CONCEPTO_HONO LIKE '%GASTOS DE COBRANZA%' ");
            stringBuilder.append("      OR DEC_CONCEPTO_HONO LIKE '%HONORARIOS POR ADMINISTRACION%') ");
            stringBuilder.append(" GROUP BY MON_NOM_MONEDA, DEC_CVE_PERS_FID, DEC_NUM_PERS_FID ");
            stringBuilder.append(" ORDER BY 1, 2, 3 ");
 
            //Call_Conexion
            conect = DataBaseConexion.getInstance().getConnection();
            prepareStmt = conect.prepareStatement(stringBuilder.toString());

            //Set_Values
            prepareStmt.setInt(1, fideicomiso);

            //Execute_Query
            resSet = prepareStmt.executeQuery();

            if (resSet != null) {
                //Get_Data
                while (resSet.next()) {
                    //Set_Person
                    String person = resSet.getString("MON_NOM_MONEDA").concat(" / ").concat(resSet.getString("DEC_CVE_PERS_FID")).concat(" / ").concat(String.valueOf(resSet.getInt("DEC_NUM_PERS_FID"))).concat(" / ").concat(onHonorarios_getPersonName(fideicomiso, resSet.getString("DEC_CVE_PERS_FID"), resSet.getInt("DEC_NUM_PERS_FID")));

                    //Add_List
                    personsPay.add(person);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetPersonsPay()");

        } finally {
            if (resSet != null) {
                try {
                    resSet.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
            if (prepareStmt != null) {
                try {
                    prepareStmt.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
            if (conect != null) {
                try {
                    conect.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
        }

        return personsPay;
    }

    public List<DetCartBean> onHonorarios_GetCondonaciones(int fideicomiso, String fecha) {
        //Objects
        List<DetCartBean> pagos = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        //Format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_LocalDate
        LocalDate localDate = LocalDate.parse(fecha, dateTimeFormatter);
 
        try {
            //SQL 
            stringBuilder.append("SELECT CTO_CVE_EXCLU_30, ");
            stringBuilder.append("       DETCART.*, ");
            stringBuilder.append("       DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR + DEC_REM_EXTEMP ADEUDO, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_DEL))||'-'||RTRIM(CHAR(DEC_MES_PER_DEL))||'-'||RTRIM(CHAR(DEC_DIA_PER_DEL))) DEL, ");
            stringBuilder.append("       DATE(RTRIM(CHAR(DEC_ANO_PER_AL)) ||'-'||RTRIM(CHAR(DEC_MES_PER_AL)) ||'-'||RTRIM(CHAR(DEC_DIA_PER_AL))) AL, ");
            stringBuilder.append("       CTO_NOM_CONTRATO, ");
            stringBuilder.append("       FID_NOM_FIDEICOM, ");
            stringBuilder.append("       BEN_NOM_BENEF ");
            stringBuilder.append("  FROM (((DETCART LEFT OUTER JOIN BENEFICI  ");
            stringBuilder.append("    ON DEC_NUM_CONTRATO = BEN_NUM_CONTRATO ");
            stringBuilder.append("   AND DEC_NUM_PERS_FID = BEN_BENEFICIARIO ");
            stringBuilder.append("   AND DEC_CVE_PERS_FID='FIDEICOMISARIO') LEFT OUTER JOIN FIDEICOM ");
            stringBuilder.append("    ON DEC_NUM_CONTRATO=FID_NUM_CONTRATO ");
            stringBuilder.append("   AND DEC_NUM_PERS_FID=FID_FIDEICOMITENTE ");
            stringBuilder.append("   AND DEC_CVE_PERS_FID='FIDEICOMITENTE') INNER JOIN CONTRATO ");
            stringBuilder.append("    ON DEC_NUM_CONTRATO=CTO_NUM_CONTRATO) ");
            stringBuilder.append(" WHERE DEC_NUM_CONTRATO = ? ");
            stringBuilder.append("   AND DEC_CVE_ST_DETCART <> 'CANCELADO'  ");
            stringBuilder.append("   AND DEC_FEC_CALC_HONO <= DATE(CAST(? AS DATE)) ");
            stringBuilder.append("   AND DEC_IMP_REM_HONOR + DEC_REM_IVA_HONOR+DEC_REM_EXTEMP > 0 ");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, fideicomiso);
            pstmt.setString(2, localDate.format(dateTimeFormatter2));

            //Execute_Query
            rs = pstmt.executeQuery();

            //Validate
            if (rs != null) {

                while (rs.next()) {
                    //Object
                    DetCartBean detCartBean = new DetCartBean();

                    //Set_Values
                    detCartBean.setFideicomiso(rs.getString("CTO_NOM_CONTRATO"));
                    detCartBean.setNoMoneda(rs.getInt("DEC_NUM_MONEDA"));
                    detCartBean.setPersona(rs.getString("DEC_CVE_PERS_FID"));

                    if ("CARGO AL FONDO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre("CARGO AL FONDO");
                    } else if ("FIDEICOMISARIO".equals(rs.getString("DEC_CVE_PERS_FID"))) {
                        detCartBean.setPersonaNombre(rs.getString("BEN_NOM_BENEF"));
                    } else {
                        detCartBean.setPersonaNombre(rs.getString("FID_NOM_FIDEICOM"));
                    }

                    detCartBean.setNoPersona(rs.getInt("DEC_NUM_PERS_FID"));
                    detCartBean.setSecuencial(rs.getInt("DEC_NUM_SECUENCIAL"));
                    detCartBean.setHonorario(rs.getString("DEC_CVE_TIPO_HONO"));
                    detCartBean.setFechaCalculo(rs.getString("DEC_FEC_CALC_HONO"));
                    detCartBean.setFechaDel(dateTimeFormatter.format((LocalDate.parse(rs.getString("DEL")))));
                    detCartBean.setFechaAl(dateTimeFormatter.format((LocalDate.parse(rs.getString("AL")))));
                    detCartBean.setAdeudoImp(rs.getDouble("ADEUDO"));
                    detCartBean.setAdeudo(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("ADEUDO")))));
                    detCartBean.setImporteImp(rs.getDouble("DEC_IMP_REM_HONOR"));
                    detCartBean.setImporte(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_IMP_REM_HONOR")))));
                    detCartBean.setIvaImp(rs.getDouble("DEC_REM_IVA_HONOR"));
                    detCartBean.setIva(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_IVA_HONOR")))).replace("($", "$-").replace(")", ""));
                    detCartBean.setMoratoriosImp(rs.getDouble("DEC_REM_EXTEMP"));
                    detCartBean.setMoratorios(String.valueOf(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(CValidacionesUtil.validarDouble(rs.getString("DEC_REM_EXTEMP")))));

                    //Add_List
                    pagos.add(detCartBean);
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCondonaciones()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_GetCondonaciones()");

        } finally {
            onCierraConexion();
        }

        return pagos;
    }

    public int onHonorarios_CheckProvision(DetCartBean detCartBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //Variables
        int count = 0;

        try {
            //SQL 
            stringBuilder.append("SELECT COUNT(*) AS COUNT FROM PROVHONORARIOS ");
            stringBuilder.append(" WHERE PRV_NUM_CONTRATO = ? ");
            stringBuilder.append("   AND DEC_FEC_CALC_HONO  = ? ");
            stringBuilder.append("   AND DEC_NUM_SECUENCIAL  = ? ");
            stringBuilder.append("   AND DEC_NUM_MONEDA = ? ");
            stringBuilder.append("   AND PRV_SUBFISO = 0 ");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setInt(1, Integer.parseInt(realScapeString(detCartBean.getFideicomiso())));
            pstmt.setString(2, detCartBean.getFechaCalculo());
            pstmt.setInt(3, detCartBean.getSecuencial());
            pstmt.setInt(4, detCartBean.getNoMoneda());

            //Execute_Query
            rs = pstmt.executeQuery();

            //Get_Data
            if (rs != null) {
                //Set_Planes
                if (rs.next()) {
                    //Set_Count
                    count = rs.getInt("COUNT");
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_CheckProvision()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_CheckProvision()");

        } finally {
            onCierraConexion();
        }

        return count;
    }

    public OutParameterBean onHonorarios_executeApplyPayCondonacionSp(DetCartBean detCartBean, FechaContableBean fechaContableBean) {
        //Objects
        OutParameterBean outParameterBean = new OutParameterBean();
 
        //SQL
        String sql = "{CALL DB2FIDUC.SPN_CAN_CON_HONO (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        //GET_CALLABLESTATEMENT
        try {
            //Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sql);

            //REGISTER_IN_PARAMETER
            cstmt.setInt(1, Integer.parseInt(realScapeString(detCartBean.getFideicomiso())));
            cstmt.setString(2, detCartBean.getPersona());
            cstmt.setInt(3, detCartBean.getNoPersona());
            cstmt.setString(4, detCartBean.getHonorario());
            cstmt.setInt(5, detCartBean.getNoMoneda());
            cstmt.setDate(6, java.sql.Date.valueOf(detCartBean.getFechaCalculo()));
            cstmt.setInt(7, detCartBean.getSecuencial());
            cstmt.setString(8, detCartBean.getTipoOperacionContable());
            cstmt.setDouble(9, detCartBean.getImporteImp());
            cstmt.setDouble(10, detCartBean.getIvaImp());
            cstmt.setDouble(11, 0);
            cstmt.setInt(12, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(13, "");
            cstmt.setString(14, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());

            //REGISTER_OUT_PARAMETER
            cstmt.registerOutParameter("bEjecuto", Types.SMALLINT);
            cstmt.registerOutParameter("lNumFolio", Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            //EXECUTE_CALLABLESTATEMENT
            cstmt.execute();

            //GET_OUT_PARAMETER
            int bEjecuto = cstmt.getInt("bEjecuto");
            int lNumFolio = cstmt.getInt("lNumFolio");
            String psMsgErrOut = cstmt.getString("PS_MSGERR_OUT");

            //Set_Values
            outParameterBean.setbEjecuto(bEjecuto);
            outParameterBean.setiNumFolioContab(lNumFolio);
            outParameterBean.setPsMsgErrOut(psMsgErrOut);

            onCierraConexion();

            //SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString());
            bitacoraBean.setNombre("Condonacion".toUpperCase());
            bitacoraBean.setFuncion("honorariosCondonacion".toUpperCase());

            //SET_DETALLE_BITACORA
            if ("1".equals(bEjecuto)) {
                bitacoraBean.setDetalle("CONDONACION DE HONORARIOS DEL CONTRATO (".concat(detCartBean.getFideicomiso()).concat(" POR LA CANTIDAD: ".concat(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(detCartBean.getImporteImp() + detCartBean.getIvaImp()))));
            } else {
                bitacoraBean.setDetalle("CONDONACION DE HONORARIOS DEL CONTRATO (".concat(detCartBean.getFideicomiso()).concat(" ) ".concat(psMsgErrOut)));
            }

            //INSERT_BITACORA
            onHonorarios_InsertBitacora(bitacoraBean);
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPayCondonacionSp()");

        } catch (NumberFormatException numberFormatException) {
            mensajeError += "Descripción numberFormatException: ".concat(numberFormatException.getMessage()).concat(nombreObjeto).concat("onHonorarios_executeApplyPayCondonacionSp()");

        } finally {
            onCierraConexion();
        }

        return outParameterBean;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * F U N C I O N E S   P R I V A D A S
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private boolean onHonorarios_isFechaValida(String fecha) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(fecha);
        } catch (ParseException e) { 
            return false;
        }
 
        return true;
    }

    private LocalDate onHonorarios_proyectaFecha(LocalDate fecha, String periodo) {
        //Object
        LocalDate localDate = LocalDate.now();
        //Date_Time_Formatter
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
        switch (periodo) {
            case "DIARIO":
                localDate = fecha.plusDays(1);
                break;
            case "SEMANAL":
                localDate = fecha.plusDays(7);
                break;
            case "QUINCENAL":
                localDate = fecha.plusDays(14);
                break;
            case "MENSUAL":
                localDate = fecha.plusMonths(1);
                break;
            case "BIMESTRAL":
                localDate = fecha.plusMonths(2);
                break;
            case "TRIMESTRAL":
                localDate = fecha.plusMonths(3);
                break;
            case "CUATRIMESTRAL":
                localDate = fecha.plusMonths(4);
                break;
            case "SEMESTRAL":
                localDate = fecha.plusMonths(6);
                break;
            case "ANUAL":
                localDate = fecha.plusYears(1);
                break;
        }
 
        return localDate;
    }

    private void onHonorarios_InsertBitacora(BitacoraBean bitacoraBean) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);
 
        try {
            //Set_SQL
            stringBuilder.append("INSERT INTO SAF.BITACORA (BIT_FEC_TRANSAC, BIT_ID_TERMINAL, BIT_NUM_USUARIO, BIT_NOM_PGM, BIT_CVE_FUNCION, BIT_DET_BITACORA)");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?) ");
 
            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(dateTime));
            pstmt.setString(2, bitacoraBean.getTerminal());
            pstmt.setInt(3, bitacoraBean.getUsuario());
            pstmt.setString(4, bitacoraBean.getNombre());
            pstmt.setString(5, bitacoraBean.getFuncion());
            pstmt.setString(6, bitacoraBean.getDetalle());

            //Execute_Update
            pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_InsertBitacora()");

        } finally {
            onCierraConexion();
            //oGeneraLog.onGeneraLog(CHonorarios.class, "0E", "INFO", "00", "00", bitacoraBean.getDetalle(), "00", "20", "00");
        }
    }

    private String onHonorarios_getPersonName(int fideicomiso, String tipoPersona, int noPersona) {
        //Objects
        StringBuilder stringBuilder = new StringBuilder();
        Connection conect = null;
        PreparedStatement prepareStmt = null;
        ResultSet resSet = null;
        //Variables
        String person = "";

        try {
            switch (tipoPersona) {
                case "FIDEICOMITENTE":
                    //SQL 
                    stringBuilder.append("SELECT FID_NOM_FIDEICOM AS NAME FROM FIDEICOM WHERE FID_NUM_CONTRATO = ? AND FID_FIDEICOMITENTE = ?");
                    break;
                case "FIDEICOMISARIO":
                    //SQL 
                    stringBuilder.append("SELECT BEN_NOM_BENEF AS NAME FROM BENEFICI WHERE BEN_NUM_CONTRATO = ? AND BEN_BENEFICIARIO = ?");
                    break;
                case "TERCERO":
                    //SQL 	
                    stringBuilder.append("SELECT TER_NOM_TERCERO AS NAME FROM TERCEROS WHERE TER_NUM_CONTRATO = ? AND TER_NUM_TERCERO = ?");
                    break;
                default:
                    person = "CARGO AL FONDO";
                    break;
            }

            //Validate_CARGO_AL_FONDO
            if ("CARGO AL FONDO".equals(person)) {
                return person;
            }

            //Call_Conexion
            conect = DataBaseConexion.getInstance().getConnection();
            prepareStmt = conect.prepareStatement(stringBuilder.toString());

            //Set_Values
            prepareStmt.setInt(1, fideicomiso);
            prepareStmt.setInt(2, noPersona);

            //Execute_Query
            resSet = prepareStmt.executeQuery();

            //Check
            if (resSet != null) {

                if (resSet.next()) {
                    //Get_Name_Person
                    person = resSet.getString("NAME");
                }
            }
        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");

        } finally {
            if (resSet != null) {
                try {
                    resSet.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
            if (prepareStmt != null) {
                try {
                    prepareStmt.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
            if (conect != null) {
                try {
                    conect.close();
                } catch (SQLException e) {
                    mensajeError += "Descripción SQLException: ".concat(e.getMessage()).concat(nombreObjeto).concat("onHonorarios_getPersonName()");
                }
            }
        }

        return person;
    }

    public int onHonorario_AccountingPolicy(int Fideicomiso) {
        int totalRows = 0;

        try {
            StringBuilder sqlComando = new StringBuilder();
            sqlComando.append("SELECT COUNT(*) AS total_rows ");
            sqlComando.append("FROM ( ");
            sqlComando.append("SELECT ROW_NUMBER() OVER (ORDER BY detm_folio_op DESC) AS Ind, ");
            sqlComando.append("movi_fecha_mov, movi_alta, fiso_id_fiso, sfis_id_sfiso, ");
            sqlComando.append("detm_folio_op, tran_id_tran, movi_descripcion, movi_importe, ");
            sqlComando.append("movi_usuario, movi_status, oper_id_operacion, usu_nom_usuario ");
            sqlComando.append("FROM   SAF.FDMovimiento m ");
            sqlComando.append("INNER JOIN SAF.Usuarios U ON u.USU_NUM_USUARIO = m.MOVI_USUARIO AND U.USU_NUM_USUARIO = ? ");
            sqlComando.append("INNER JOIN SAF.VISTA_USUARIO V ON m.fiso_id_fiso = V.CTO_NUM_CONTRATO AND V.USU_NUM_USUARIO = ? ");
            sqlComando.append("LEFT OUTER JOIN SAF.CancelaCTRL ON detm_folio_op = cc_folio ");
            sqlComando.append("WHERE fiso_id_fiso  = ? ");
            sqlComando.append(") AS Consulta");

            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando.toString());

            //Set_Valuespstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setInt(3, Fideicomiso);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalRows = rs.getInt("total_rows");
            }

        } catch (SQLException sqlException) {
            mensajeError += "Descripción SQLException: ".concat(sqlException.getMessage()).concat(nombreObjeto).concat(" onHonorario_AccountingPolicy()");

        } finally {
            onCierraConexion();
        }

        return totalRows;
    }

    private void onCierraConexion() {
        if (rs != null) {
            try{
                rs.close();
            }catch (SQLException sqlExc){
               logger.error("Error al cerrar ResultSet");
            }
        }
        if (pstmt != null) {
            try{
                pstmt.close();
            }catch (SQLException sqlExc){
               logger.error("Error al cerrar PrepareStatement");
            }
        }
        if (cstmt != null) {
            try{
                cstmt.close();
            }catch (SQLException sqlExc){
               logger.error("Error al cerrar CallableStatement");
            }
        }
        if (conexion != null) {
            try{
                conexion.close();
            }catch (SQLException sqlExc){
               logger.error("Error al cerrar la Conexión");
            }
        }
    }
}