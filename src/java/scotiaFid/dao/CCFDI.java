/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : CFDI 4.0 
 * TIPO        : Clase
 * PAQUETE     : scotiafid.dao
 * CREADO      : 20210306 VJN
 * MODIFICADO  : 20240315 VJN
 * NOTAS       : Separación de Nombre Legal y Fiscal VJN 20240315
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.dao;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.BitacoraBean;
import scotiaFid.bean.BitacoraUsuarioBean;
import scotiaFid.bean.CargaInterfazBean; 
import scotiaFid.bean.DirecciBean;
import scotiaFid.bean.DirecciFBean;
import scotiaFid.bean.FactSelloBean; 
import scotiaFid.bean.OutParameterBean; 
import scotiaFid.singleton.DataBaseConexion;
import scotiaFid.util.LogsContext;
import scotiaFid.bean.FdCanFactBean;


public class CCFDI {
	private static final Logger logger = LogManager.getLogger(CCFDI.class);
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * A T R I B U T O S   P R I V A D O S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean                              valorRetorno;   
	private String                               nombreObjeto;
    private String                               mensajeErrorSP;
    
    private CallableStatement                    cstmt;
    private Connection                           conexion;
    private PreparedStatement                    pstmt;
    private ResultSet                            rs;
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * B E A N S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * *
     */
    BitacoraBean bitacoraBean = new BitacoraBean();
    // -----------------------------------------------------------------------------
    BitacoraUsuarioBean bitacoraUsuarioBean = new BitacoraUsuarioBean();
    // -----------------------------------------------------------------------------
    CTesoreria cTesoreria = new CTesoreria();
    // -----------------------------------------------------------------------------
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    private String                               mensajeError;


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
     * F O R M A T O S
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    // -----------------------------------------------------------------------------
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    //-------------------------------------------------------------------------
    DecimalFormat decimalFormatImp = new DecimalFormat("$#,###,###,###,###,##0.00####");
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * G E T T E R S   Y   S E T T E R S
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 

    public Boolean getValorRetorno() {
		return valorRetorno;
	}
	public void setValorRetorno(Boolean valorRetorno) {
		this.valorRetorno = valorRetorno;
	}
	public String getMensajeErrorSP() {
		return mensajeErrorSP;
	}
	public void setMensajeErrorSP(String mensajeErrorSP) {
		this.mensajeErrorSP = mensajeErrorSP;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
    * C O N S T R U C T O R
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */  
    public CCFDI() {
        LogsContext.FormatoNormativo();
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CCFDI.";
        valorRetorno = Boolean.FALSE;
    }
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     * M E T O D O S
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * *  F A C T U R A S   C O N   S E L L O   D I G I T A L  * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * *
     */
    public List<FactSelloBean> onCCFDI_Consulta(java.util.Date dFec_Inicial, java.util.Date dFec_Final, String sFideicomiso, 
            String sNombre, String sTipo_Participante, String sNo_Participante, String sImporte,
            String sEstatus, String sUUID) {

		rs = null;
		pstmt = null;
		conexion = null;
		int iCont = 0;
		String sqlComando = "", sqlWhere= "", sqlOrder= "";  
		List<FactSelloBean> consulta = new ArrayList<>(); 
		
		try {

sqlComando = "SELECT FAC_FECHA Fecha, \n"
		+ "      FAC_FECHA_INT Fecha_Int, \n"
		+ "      FAC_NUM_CONTRATO Fiso, \n"
		+ "      FAC_CVE_PERS_FID Participante, \n"
		+ "      FAC_NUM_PERS_FID No_Participante, \n"
		+ "      FAC_NOMBRE_RECEP Nombre, \n"
		+ "      FAC_CVE_TIPO_PER Tipo_Persona, \n"
		+ "      FAC_RFC RFC, \n"
		+ "      FAC_NUM_MONEDA Moneda, \n"
		+ "      FAC_HONORARIOS Honorarios,  \n"
		+ "      FAC_MORATORIOS Moratorios, \n"
		+ "      FAC_IVA_TRASLADADO IVA, \n"
		+ "      FAC_TASA_IMPUESTO Tasa_IVA, \n"
		+ "      COALESCE(FAC_DOM_CALLE,'') Calle,  \n"
		+ "      COALESCE(FAC_DOM_NUM_EXT,'') NUM_EXT, \n"
		+ "      COALESCE(FAC_DOM_NUM_INT,'') NUM_INT, \n"
		+ "      COALESCE(FAC_DOM_COLONIA,'') Colonia,  \n"
		+ "      COALESCE(FAC_RECEP_LOCALIDAD,'') Localidad, \n"
		+ "      COALESCE(FAC_DOM_POBLACION,'') Folio_SAT, \n"
		+ "      FAC_DOM_ESTADO Estado, \n"
		+ "      FAC_DOM_PAIS Pais, \n"
		+ "      FAC_CODIGO_POSTAL CP, \n"
		+ "      FAC_CONDI_PAGO Condiciones_Pago, \n"
		+ "      CASE WHEN FCR_STATUS IN ('TRANSFERIDO', 'PENDIENTE') THEN 'EN PROCESO' ELSE FAC_STATUS END Status, \n"
		+ "      COALESCE(REPLACE(FAC_MENSAJE, CHR(13) || CHR(10), ''),'') Mensaje, \n"
		+ "      FAC_FOLIO_TRAN FOLIO_TRAN, \n"
		+ "      RIGHT(REPLACE(CHAR(FAC_FECHA,ISO),'-',''),6)||RTRIM(CHAR(FAC_NUM_CONTRATO))||FUN_RELLENA(RTRIM(CHAR(FAC_FOLIO_FACT)),'0',CASE WHEN LENGTH (RTRIM(CHAR(FAC_FOLIO_FACT))) < 3 THEN 2 ELSE LENGTH(RTRIM(CHAR(FAC_FOLIO_FACT))) END ,'I') FOLIO_FACT, \n"
		+ "      FAC_FOLIO_FACT,  \n"
		+ "      FAC_NACIONALIDAD NAL, \n"
		+ "      FAC_SISTEMA, \n"
		+ "      FAC_STATUS_CTE Tipo_Factura, \n"
		+ "      COALESCE(LPAD(CHAR(CMP_CVE_METPAG),2,'0'),''), \n"
		+ "      UPPER(CMP_DESC_METPAG) AS Metodo_Pago, \n"
		+ "      COALESCE(DIF_REGIMENFISCAL,'') AS CVE_REGIMEN_FISCAL, \n"
		+ "      COALESCE(UPPER(CVE_DESC_CLAVE),'') AS DESC_REGIMEN_FISCAL \n"
		+ " FROM SAF.FACTSELLO LEFT JOIN SAF.CATSATMETPAGO \n"
		+ "   ON (CMP_CVE_METPAG = FAC_MET_PAGO \n"
		+ "    OR LPAD(CHAR(CMP_CVE_METPAG),2,'0') = LPAD(FAC_MET_PAGO,2,'0')) \n"
		+ "  LEFT JOIN SAF.FDCANFACT \n"
		+ "   ON FAC_FECHA = FID_FECHA \n"
		+ "  AND FID_NUM_CONTRATO = FAC_NUM_CONTRATO \n"
		+ "  AND FAC_FOLIO_FACT = FID_FOLIO_FACT \n"
		+ "  LEFT JOIN SAF.CLAVES    \n"
		+ "   ON DIF_REGIMENFISCAL = CVE_FORMA_EMP_CVE    \n"
		+ "  AND CVE_NUM_CLAVE = 151 \n"
		+ "WHERE  ";
 
		if (dFec_Inicial != null && dFec_Final != null) {   
			sqlWhere += "AND FAC_FECHA between ? AND ? ";
		} else {
			 
			if (dFec_Inicial != null) {
				sqlWhere += "AND FAC_FECHA = ? ";
			}
		
			if (dFec_Final != null) {
				sqlWhere += "AND FAC_FECHA = ? "; 
			}  
		}
		
		if (!sFideicomiso.equals("")) {
			sqlWhere += "AND FAC_NUM_CONTRATO = ? ";
		}
		
		if (!sNombre.trim().equals("")) {
			sqlWhere += "AND FAC_NOMBRE_RECEP like ? ";
		}
		
		if (sTipo_Participante != null) {
			if (sTipo_Participante.contains("CARGO AL FONDO"))
				sqlWhere += "AND FAC_CVE_PERS_FID = 'CARGO AL FONDO' ";
			else
				sqlWhere += "AND FAC_CVE_PERS_FID = ? ";
		}

		if (sEstatus != null) {
			if (sEstatus.equals("REVERSADA"))
				sqlWhere += "AND FAC_HONORARIOS + FAC_MORATORIOS = 0 ";
			else
				sqlWhere += "AND FAC_HONORARIOS + FAC_MORATORIOS > 0 ";
		
			if (sEstatus.equals("REEXPEDIDA")) {
				sqlWhere += "AND (FAC_FECHA,FAC_NUM_CONTRATO,FAC_FOLIO_FACT) IN ";
				sqlWhere += " (select FID_FECHA, FID_NUM_CONTRATO, FID_FOLIO_FACT ";
				sqlWhere += " From SAF.FDCANFACT ";
				sqlWhere += " WHERE FCR_STATUS = 'REEXPEDIDA' ) ";
			} else {
				sqlWhere += "AND FAC_STATUS = ? ";
			}
		}
		
		if (!sNo_Participante.trim().equals("")) {
			sqlWhere += "AND FAC_NUM_PERS_FID = ? ";
		}
		
		if (!sUUID.trim().equals("")) {
			sqlWhere += "AND FAC_DOM_POBLACION = ? ";
		}
		
		if (!sImporte.equals("")) {
			sqlWhere += "AND FAC_HONORARIOS = ? ";
		}

	sqlOrder = " ORDER BY FAC_FECHA_INT,FAC_NUM_CONTRATO";

	sqlComando = sqlComando + sqlWhere.substring(4) + sqlOrder; 
	
	conexion = DataBaseConexion.getInstance().getConnection();
	pstmt = conexion.prepareStatement(sqlComando); 
  	
	if (dFec_Inicial != null && dFec_Final != null) {
		iCont += 1;
		pstmt.setDate(iCont, java.sql.Date.valueOf(this.dateFormat2(dFec_Inicial)));
		iCont += 1;
		pstmt.setDate(iCont, java.sql.Date.valueOf(this.dateFormat2(dFec_Final)));
	} else {
		if (dFec_Inicial != null) {
			iCont += 1;
			pstmt.setDate(iCont, java.sql.Date.valueOf(this.dateFormat2(dFec_Inicial)));
		}

		if (dFec_Final != null) {
			iCont += 1;
			pstmt.setDate(iCont, java.sql.Date.valueOf(this.dateFormat2(dFec_Final)));
		}
	}

	if (!sFideicomiso.equals("")) {
		iCont += 1;
		pstmt.setInt(iCont, Integer.parseInt(sFideicomiso));
	}

	if (!sNombre.trim().equals("")) {
		iCont += 1;
		pstmt.setString(iCont, "%" + sNombre.toUpperCase().replace("'", "") + "%");
		;
	}

	if (sTipo_Participante != null && !sTipo_Participante.contains("CARGO AL FONDO")) {
		iCont += 1;
		pstmt.setString(iCont, sTipo_Participante);
	}

	if (sEstatus != null) {
		if (!sEstatus.equals("REVERSADA")) {
			if (!sEstatus.equals("REEXPEDIDA")) {
				iCont += 1;
				pstmt.setString(iCont, sEstatus.trim());
			}
		}
	} 

	if (!sNo_Participante.trim().equals("")) {
		iCont += 1;
		pstmt.setInt(iCont, Integer.parseInt(sNo_Participante));
		;
	}

	if (!sUUID.trim().equals("")) {
		iCont += 1;
		pstmt.setString(iCont, sUUID);
	}

	if (!sImporte.equals("")) {
		iCont += 1;
		pstmt.setDouble(iCont, Double.parseDouble(sImporte));
	}

	rs = pstmt.executeQuery();

		while (rs.next()) { 
	
			FactSelloBean facturasSelloDig = new FactSelloBean();
	
			facturasSelloDig.setFacFecha(java.sql.Date.valueOf(rs.getString("Fecha")));
			facturasSelloDig.setFacStatus(rs.getString("Status"));
			facturasSelloDig.setFacStatusCte(rs.getString("Tipo_Factura"));
			facturasSelloDig.setFacNumContrato(rs.getInt("Fiso"));
			facturasSelloDig.setFacCvePersFid(rs.getString("Participante"));
			facturasSelloDig.setFacNumPersFid(rs.getInt("No_Participante"));
			facturasSelloDig.setFacNacionalidad(rs.getString("NAL"));
			facturasSelloDig.setFacNombreRecep(rs.getString("Nombre"));
			facturasSelloDig.setFacCveTipoPer(rs.getString("Tipo_Persona"));
			facturasSelloDig.setFacRFC(rs.getString("RFC"));
			facturasSelloDig.setFacNumMoneda(rs.getInt("Moneda"));
			facturasSelloDig.setFacHonorarios(rs.getDouble("Honorarios"));
			facturasSelloDig.setFacMoratorios(rs.getDouble("Moratorios"));
			facturasSelloDig.setFacIvaTraladado(rs.getDouble("IVA"));
			facturasSelloDig.setFacTasaImpuesto(rs.getDouble("Tasa_IVA"));
			facturasSelloDig.setFacDomCalle(rs.getString("Calle"));
			facturasSelloDig.setFacDomNumExt(rs.getString("NUM_EXT"));
			facturasSelloDig.setFacDomNumInt(rs.getString("NUM_INT"));
			facturasSelloDig.setFacDomColonia(rs.getString("Colonia"));
			facturasSelloDig.setFacRecepLocalidad(rs.getString("Localidad"));
			facturasSelloDig.setFacDomEstado(rs.getString("Estado"));
			facturasSelloDig.setFacDomPais(rs.getString("Pais"));
			facturasSelloDig.setFacCodigoPostal(rs.getString("CP"));
			facturasSelloDig.setFacCondiPago(rs.getString("Condiciones_Pago"));
	
			if (rs.getString("Fecha_Int") != null) 
				facturasSelloDig.setFacFechaInt(java.sql.Date.valueOf(rs.getString("Fecha_Int"))); 
			
			facturasSelloDig.setFolio_Fac(rs.getString("FOLIO_FACT"));
			facturasSelloDig.setFacFolioTran(rs.getInt("FOLIO_TRAN"));
			facturasSelloDig.setFacMensaje(rs.getString("Mensaje"));
			facturasSelloDig.setFacFolioFact(rs.getInt("FAC_FOLIO_FACT"));
			facturasSelloDig.setFacSistema(rs.getString("FAC_SISTEMA"));
			facturasSelloDig.setFacDomPoblacion(rs.getString("Folio_SAT"));
			facturasSelloDig.setFacMetPago(rs.getString("Metodo_Pago"));
			facturasSelloDig.setDif_regimenfiscal(rs.getString("CVE_REGIMEN_FISCAL"));
			facturasSelloDig.setDesc_Regimen_Fiscal(rs.getString("DESC_REGIMEN_FISCAL"));
	
			consulta.add(facturasSelloDig);
		}
		onCierraConexion();
	
	} catch (SQLException | ParseException Err) {
		mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCCFDI_Consulta()";
		logger.error(mensajeError);
	
	} finally {
		onCierraConexion();
	}
	
	return consulta;
}
    
    public Map<String, String> onCFDI_getClaves(int iClave) {

        StringBuilder QuerySql = new StringBuilder(); 
        Map<String, String> claves = new LinkedHashMap<String, String>();
 
        try {
            // SQL
            QuerySql.append("SELECT CVE_NUM_SEC_CLAVE, CVE_DESC_CLAVE ");
            QuerySql.append("FROM SAF.CLAVES ");
            QuerySql.append("WHERE CVE_NUM_CLAVE = ? ");
            QuerySql.append(" ORDER BY CVE_NUM_SEC_CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(QuerySql.toString());
  
            // Parameters
            pstmt.setInt(1, iClave);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) {
                // Vacio_el_primero
                claves.put(String.valueOf(0), "");

                while (rs.next()) {

                    // Add_List
                    claves.put(String.valueOf(
                    		rs.getString("CVE_NUM_SEC_CLAVE")),
                            rs.getString("CVE_DESC_CLAVE").replace("Ã³", "ó"));
                }
            }

        	onCierraConexion();
            
        }  catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_getClaves()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return claves;
    }
    
    public Map<String, String> onCFDI_getRegimenes(int iClave) {

        StringBuilder QuerySql = new StringBuilder(); 
        Map<String, String> claves = new LinkedHashMap<String, String>(); 
    	
        try {
            // SQL
            QuerySql.append("SELECT CVE_FORMA_EMP_CVE, CVE_DESC_CLAVE ");
            QuerySql.append("FROM SAF.CLAVES ");
            QuerySql.append("WHERE CVE_NUM_CLAVE = ? ");
            QuerySql.append(" ORDER BY CVE_NUM_SEC_CLAVE");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(QuerySql.toString()); 
        	
            // Parameters
            pstmt.setInt(1, iClave);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) {
                // Vacio_el_primero
                claves.put(String.valueOf(0), "");

                while (rs.next()) {

                    // Add_List
                    claves.put(String.valueOf(
                    		rs.getString("CVE_FORMA_EMP_CVE")),
                            rs.getString("CVE_DESC_CLAVE").replace("Ã³", "ó"));
                }
            }

        	onCierraConexion();
            
        }  catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_getRegimenes()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return claves;
    }
      
    public Boolean onCFDI_Reexpide(Date dFecha, int iFiso, int iFolio, Double dImpHono, String sTipoFactura, java.sql.Date fechaSistema) {
        try { 
        	
            String sqlComando = "{CALL DB2FIDUC.SP_REEXPEDICION(?,?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);
              
            cstmt.setString("PV_FECHA", this.dateFormat2(dFecha));  
            cstmt.setString("PV_FISO",String.valueOf(iFiso));
            cstmt.setString("PV_FOLIO", String.valueOf(iFolio));
            cstmt.setDouble("PD_IMPORTE", dImpHono); 
        	cstmt.setString("PV_FECHA_SIS",this.dateFormat2(fechaSistema));  
            cstmt.setString("PV_TIPOFAC", sTipoFactura); 
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");

        	onCierraConexion();
 
            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;                 
                bitacoraUsuarioBean.setBit_det_bitacora("Reexpedición de la Factura, Fiso:" + iFiso + ", Folio " + iFolio + ", Tipo " +sTipoFactura + ", Importe " + dImpHono);
 
            } else 
                bitacoraUsuarioBean.setBit_det_bitacora("Error al Reexpedir la Factura, Fiso: " + iFiso + mensajeErrorSP);   

            // SET_VALUES_BITACORA
            bitacoraUsuarioBean.setBit_cve_funcion("REEXPEDICION DE FACTURA");
            bitacoraUsuarioBean.setBit_nom_pgm("honorariosFacturaSelloDigital");
            // INSERT_BITACORA
        	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
        	
        } catch (SQLException | ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_Reexpide()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }
        return valorRetorno;
    }

    public String onCFDI_ValidaCancela(Date sFecha, int iFiso, int iFolioFact) {
 
    	String sValor="";
        StringBuilder stringBuilder = new StringBuilder();  

        try {
            // SQL 
                stringBuilder.append("select FCR_STATUS from SAF.FDCANFACT WHERE FID_FECHA = ? AND FID_NUM_CONTRATO =  ? AND FID_FOLIO_FACT = ? "); 

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

        	pstmt.setString(1, this.dateFormat2(sFecha));   
        	pstmt.setInt(2, iFiso);   
        	pstmt.setInt(3, iFolioFact);   
        	
            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) { 
                while (rs.next()) {
                	sValor = rs.getString("FCR_STATUS");
                }
            }

        	onCierraConexion();
        } catch (SQLException | ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_ValidaCancela()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        } 

        return sValor;
    }
    
    public int onCFDI_CancelaFactura(Date dFecha, int iFiso, int iFolioFact, int iFolioTrans, String sMotivo, String sFolioCancela, String sUUIDCancelacion,java.sql.Date fechaSistema) {
 
        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        int result = 0;
 
        try {
            // Set_SQL
            stringBuilder.append("insert into SAF.FDCANFACT (FID_FECHA,FID_NUM_CONTRATO, FID_FOLIO_FACT, FID_FOLIO_TRAN, FCR_STATUS, FCR_MOTIVO_CANCELA, FCR_FOLIO_CANCELA,FCR_FEC_SOL,FCR_FOLIO_ACUSE) ");
            stringBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values  
        	pstmt.setString(1, this.dateFormat2(dFecha));  
            pstmt.setInt(2, iFiso); 
            pstmt.setInt(3, iFolioFact); 
            pstmt.setInt(4, iFolioTrans); 
        	pstmt.setString(5, "PENDIENTE");  
        	pstmt.setString(6, sMotivo);   
        	pstmt.setString(7, sFolioCancela); 
        	pstmt.setString(8, this.dateFormat2(fechaSistema));
        	pstmt.setString(9, sUUIDCancelacion); 
             
            // Execute_Update
            result = pstmt.executeUpdate(); 

        	onCierraConexion();
        } catch (SQLException | ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_CancelaFactura()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        } 

        return result;
    }
    
    public double onCFDI_ValidaImporte(String sUUIDRel, int iFiso, String sTipo) { 
    	
    	double dImporte= 0;
        StringBuilder stringBuilder = new StringBuilder();  

        try {
            // SQL 
                stringBuilder.append("select COALESCE(sum( FAC_HONORARIOS ),0) Importe from SAF.FACTSELLO "); 
                stringBuilder.append("where FAC_UUID_RELACIONADO = ? ");
                stringBuilder.append("and FAC_NUM_CONTRATO =  ? ");
                stringBuilder.append("and FAC_STATUS_CTE = ? ");
                stringBuilder.append("AND FAC_STATUS not in ('CANCELADA','REVERSADA')");
                                
            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

        	pstmt.setString(1, sUUIDRel);   
        	pstmt.setString(2, String.valueOf(iFiso));  
        	pstmt.setString(3, sTipo);     
        	
            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) { 
                while (rs.next()) {
                	dImporte = rs.getDouble("Importe");
                }
            }

        	onCierraConexion();
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_ValidaImporte()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        } 
   
        return dImporte;
    }


    public Boolean onCFDI_Emision(Date dFecha, int iFiso, int iFolio, String sImpHono, java.sql.Date fechaSistema) {
        try { 
  
            String sqlComando = "{CALL DB2FIDUC.SP_FAC_EGRESO(?,?,?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);

            cstmt.setString("PV_FECHA",this.dateFormat2(dFecha));
            cstmt.setString("PV_FISO",String.valueOf(iFiso));
            cstmt.setString("PV_FOLIO", String.valueOf(iFolio));
            cstmt.setDouble("PD_IMPORTE", Double.parseDouble(sImpHono));
            cstmt.setString("PV_FECHA_SIS",this.dateFormat2(fechaSistema));
   
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");
            
        	onCierraConexion();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
                valorRetorno = Boolean.TRUE;
                bitacoraUsuarioBean.setBit_det_bitacora("Egreso de la Factura, Fiso:" + iFiso + ", Folio " + iFolio + ", Importe " + sImpHono);
            } else 
            	bitacoraUsuarioBean.setBit_det_bitacora("Error en el Egreso de la Factura, Fiso:" + iFiso + ", " + mensajeErrorSP);
            
            // SET_VALUES_BITACORA
            bitacoraUsuarioBean.setBit_cve_funcion("EGRESO DE FACTURA");
            bitacoraUsuarioBean.setBit_nom_pgm("honorariosFacturaSelloDigital");
            // INSERT_BITACORA
        	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
            
        } catch (SQLException | ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_Emision()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }
        return valorRetorno;
    }
    
    public String onCFDI_Corrige(Date dFecha, int iFiso, int iFolio) {
    	String Result="";
    	
        try {
        	
            String sqlComando = "{CALL DB2FIDUC.SP_FAC_ACTUALIZA(?,?,?,?,?,?)}";
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sqlComando);
            
            //dFecha.toString()
            cstmt.setString("PV_FECHA", this.dateFormat2(dFecha));         	 
            cstmt.setString("PV_CONTRATO",String.valueOf(iFiso));
            cstmt.setString("PV_CONTRATO", String.valueOf(iFiso));            
            cstmt.setString("PV_FOLIO",String.valueOf(iFolio));  
        	             
            cstmt.registerOutParameter("PI_SQLCODE_OUT", java.sql.Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", java.sql.Types.VARCHAR);
            cstmt.registerOutParameter("PS_MSGERR_OUT", java.sql.Types.VARCHAR);

            cstmt.execute();
            mensajeErrorSP = cstmt.getString("PS_MSGERR_OUT").replaceAll("ERROR", "");

        	onCierraConexion();

            if ((mensajeErrorSP == null) || (mensajeErrorSP.equals(new String()))) {
            	Result = "";

                // SET_VALUES_BITACORA
                bitacoraUsuarioBean.setBit_cve_funcion("CORRECION DE FACTURA");
                bitacoraUsuarioBean.setBit_nom_pgm("honorariosFacturaSelloDigital");
                bitacoraUsuarioBean.setBit_det_bitacora("Corrección de la Factura, Fiso:" + iFiso + ", Folio " + iFolio);
 
                // INSERT_BITACORA
            	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
            	
            } else {
            	Result = mensajeErrorSP;
                logger.error("Descripción: " + mensajeError + nombreObjeto + " onCFDI_Corrige()");
            }
        	
        } catch (SQLException | ParseException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_Corrige()";
            valorRetorno = Boolean.FALSE;
            logger.error(mensajeError);
        } finally { 
        	onCierraConexion();
        }
        return Result;
    } 
    
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * *          D I R E C C I O N E S   F I S C A L E S      * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * *
     */
	public List<DirecciFBean> onCCFDI_Consulta_DirFis(String sFideicomiso, String sTipo_Participante) {

		rs = null;
		pstmt = null;
		conexion = null;
		
		int iCont=0;
        StringBuilder stringBuilder = new StringBuilder();  
		List<DirecciFBean> direcciones = new ArrayList<>();

		try {
			 stringBuilder.append(" SELECT  DIF_NUM_CONTRATO    FISO, ");
			 stringBuilder.append(" DIF_CVE_PERS_FID      		TIPO_PARTICIPANTE, ");
			 stringBuilder.append(" DIF_NUM_PERS_FID      		NO_PARTICIPANTE, ");
			 stringBuilder.append(" DIF_RECEP_CALLE       		CALLE, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_NO_EXT,'')      		NO_EXT, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_NO_INT,'')    		NO_INT, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_COLONIA,'')     		COLONIA, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_LOCALIDAD,'')   		LOCALIDAD, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_MUNICIPIO,'')   		NOM_MUNICIPIO, ");
			 stringBuilder.append(" PAI_NOM_PAIS          					PAIS, ");
			 stringBuilder.append(" coalesce(EDO_NOM_ESTADO,'') 			ESTADO, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_CP,'')          		CP, ");
			 stringBuilder.append(" coalesce(DIF_RECEP_REFERECIA,'')   		REFERENCIA, ");
			 stringBuilder.append(" coalesce(DIF_TELEFONO,'')         		TELEFONOS, ");
			 stringBuilder.append(" coalesce(DIF_MAIL,'')  					MAIL,  ");
			 stringBuilder.append(" DIF_NUM_ESTADO 							NUMESTADO,  ");
			 stringBuilder.append(" DIF_NUM_PAIS 							NUMPAIS, ");
			 stringBuilder.append(" coalesce(DIF_REGIMENFISCAL,'')     		CVE_REGIMEN_FISCAL, "); 
			 stringBuilder.append(" coalesce(CVE_DESC_CLAVE,'')        		DESC_REGIMEN_FISCAL, "); 
			 stringBuilder.append(" DIF_NOM_LEGAL                           NOM_LEGAL"); 
			 stringBuilder.append(" FROM SAF.DIRECCIF ");
			 stringBuilder.append(" inner join SAF.CONTRATO on  ");
			 stringBuilder.append(" DIF_NUM_CONTRATO = CTO_NUM_CONTRATO ");
			 stringBuilder.append(" AND CTO_CVE_ST_CONTRAT ='ACTIVO' ");
			 stringBuilder.append(" left outer join SAF.PAISES on ");
			 stringBuilder.append(" PAI_NUM_PAIS = DIF_NUM_PAIS ");
			 stringBuilder.append(" left outer join ESTADOS on ");
			 stringBuilder.append(" EDO_NUM_PAIS = DIF_NUM_PAIS ");
			 stringBuilder.append(" and  EDO_NUM_ESTADO = DIF_NUM_ESTADO ");
			 stringBuilder.append(" left outer join SAF.CLAVES on  ");
			 stringBuilder.append(" DIF_REGIMENFISCAL = CVE_FORMA_EMP_CVE And CVE_NUM_CLAVE = 151 WHERE ");
 
			if (sFideicomiso != null && !sFideicomiso.equals("")) {
				 stringBuilder.append(" DIF_NUM_CONTRATO = ? ");
			}
	
			if (sTipo_Participante != null &&  !sTipo_Participante.equals("")) {
				if (sFideicomiso != null && !sFideicomiso.equals(""))
					stringBuilder.append(" AND DIF_CVE_PERS_FID = ? "); 
				else
					stringBuilder.append(" DIF_CVE_PERS_FID = ? "); 
			}

			stringBuilder.append(" ORDER BY DIF_NUM_CONTRATO, DIF_CVE_PERS_FID, DIF_NUM_PERS_FID");
			 
			conexion = DataBaseConexion.getInstance().getConnection();
			pstmt = conexion.prepareStatement(stringBuilder.toString());

			if (sFideicomiso != null && !sFideicomiso.equals("")) {
				iCont += 1;
				pstmt.setInt(iCont, Integer.parseInt(sFideicomiso));
			}

			if (sTipo_Participante != null &&  !sTipo_Participante.equals("")) {
				iCont += 1;
				pstmt.setString(iCont, sTipo_Participante);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) { 

				DirecciFBean direcci_fiscales = new DirecciFBean();  
				
				direcci_fiscales.setDif_num_contrato(rs.getString("FISO"));
				direcci_fiscales.setDif_cve_pers_fid(rs.getString("TIPO_PARTICIPANTE"));
				direcci_fiscales.setDif_num_pers_fid(rs.getString("NO_PARTICIPANTE"));
				direcci_fiscales.setDif_recep_calle(rs.getString("CALLE"));
				direcci_fiscales.setDif_recep_no_ext(rs.getString("NO_EXT"));
				direcci_fiscales.setDif_recep_no_int(rs.getString("NO_INT"));
				direcci_fiscales.setDif_recep_colonia(rs.getString("COLONIA"));
				direcci_fiscales.setDif_recep_localidad(rs.getString("LOCALIDAD"));
				direcci_fiscales.setDif_recep_municipio(rs.getString("NOM_MUNICIPIO"));
				direcci_fiscales.setDif_nom_pais(rs.getString("PAIS"));
				direcci_fiscales.setDif_num_pais(rs.getInt("NUMPAIS"));
				direcci_fiscales.setDif_nom_estado(rs.getString("ESTADO"));
				direcci_fiscales.setDif_num_estado(rs.getInt("NUMESTADO"));
				direcci_fiscales.setDif_recep_cp(rs.getString("CP"));
				direcci_fiscales.setDif_recep_referencia(rs.getString("REFERENCIA"));
				direcci_fiscales.setDif_telefono(rs.getString("TELEFONOS")); 
				direcci_fiscales.setDif_mail(rs.getString("MAIL"));
				direcci_fiscales.setDif_regimen_fiscal(rs.getString("CVE_REGIMEN_FISCAL"));
				direcci_fiscales.setDif_desc_regimen_fiscal(rs.getString("DESC_REGIMEN_FISCAL"));
				direcci_fiscales.setDif_nom_legal(rs.getString("NOM_LEGAL"));
				
				direcciones.add(direcci_fiscales);
			}

        	onCierraConexion();

		} catch (NumberFormatException | SQLException Err) {
			mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCCFDI_Consulta_DirFis()";
			logger.error(mensajeError); 
		} finally {
        	onCierraConexion();
		}
		return direcciones;
	}
	  
    public List<String> onCCFDI_Consulta_NomParticipantes(DirecciFBean dParticipante) {     	 
        StringBuilder stringBuilder = new StringBuilder();
        List<String> datParticipantes = new ArrayList<>(); 

        try {
            // SQL 
        	switch (dParticipante.getDif_cve_pers_fid()) {
            case "FIDEICOMITENTE": 
                stringBuilder.append("select replace(FID_NOM_FIDEICOM,chr(9), ' ') NOM_PARTICIPA, FID_RFC RFC, DIF_NOM_LEGAL ");
                stringBuilder.append("FROM SAF.fideicom ");  
                stringBuilder.append("LEFT JOIN SAF.DIRECCIF ON DIF_NUM_CONTRATO = FID_NUM_CONTRATO AND DIF_NUM_PERS_FID = FID_FIDEICOMITENTE AND DIF_CVE_PERS_FID = ? "); 
                stringBuilder.append("where fid_num_contrato = ? AND FID_FIDEICOMITENTE = ?"); 
                
                break; 
            case "FIDEICOMISARIO":
                stringBuilder.append("select replace(BEN_NOM_BENEF,chr(9), ' ') NOM_PARTICIPA, BEN_RFC RFC, DIF_NOM_LEGAL ");
                stringBuilder.append("from SAF.BENEFICI ");
                stringBuilder.append("LEFT JOIN SAF.DIRECCIF ON DIF_NUM_CONTRATO = BEN_num_contrato AND DIF_NUM_PERS_FID = BEN_BENEFICIARIO AND DIF_CVE_PERS_FID = ? ");
                stringBuilder.append("where BEN_num_contrato = ? AND BEN_BENEFICIARIO = ?");
                break;
            case "TERCERO":
                stringBuilder.append("select replace(TER_NOM_TERCERO,chr(9), ' ') NOM_PARTICIPA, TER_RFC RFC, DIF_NOM_LEGAL ");
                stringBuilder.append("from SAF.terceros ");
                stringBuilder.append("LEFT JOIN SAF.DIRECCIF ON DIF_NUM_CONTRATO = ter_num_contrato AND DIF_NUM_PERS_FID = TER_NUM_TERCERO AND DIF_CVE_PERS_FID = ? ");
                stringBuilder.append("where ter_num_contrato = ? AND TER_NUM_TERCERO = ?");
                break;
        } 

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

        	pstmt.setString(1, dParticipante.getDif_cve_pers_fid());
        	pstmt.setInt(2, Integer.parseInt(dParticipante.getDif_num_contrato()));   
        	pstmt.setInt(3, Integer.parseInt(dParticipante.getDif_num_pers_fid()));   
        	 
            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data 
             while (rs.next()) {
               if( rs.getString("NOM_PARTICIPA") != null)
            	datParticipantes.add(rs.getString("NOM_PARTICIPA"));
        	   	datParticipantes.add(rs.getString("RFC"));  
        	   	datParticipantes.add(rs.getString("DIF_NOM_LEGAL"));  
            } 

        	onCierraConexion();
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCCFDI_Consulta_NomParticipantes()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        } 

        return datParticipantes;
    }
    
	  
    public boolean onCCFDI_Consulta_Participante(DirecciFBean dParticipante) {  
        StringBuilder stringBuilder = new StringBuilder();  

        try {
            // SQL 
        	switch (dParticipante.getDif_cve_pers_fid()) {
            case "FIDEICOMITENTE": 
                stringBuilder.append("select replace(FID_NOM_FIDEICOM,chr(9), ' ') NOM_PARTICIPA from SAF.fideicom where fid_num_contrato = ? AND FID_FIDEICOMITENTE = ? AND FID_CVE_ST_FIDEICO = 'ACTIVO'"); 
                break; 
            case "FIDEICOMISARIO":
                stringBuilder.append("select replace(BEN_NOM_BENEF,chr(9), ' ') NOM_PARTICIPA from SAF.BENEFICI where BEN_num_contrato = ? AND BEN_BENEFICIARIO = ? AND BEN_CVE_ST_BENEFIC = 'ACTIVO'");
                break;
            case "TERCERO":
                stringBuilder.append("select replace(TER_NOM_TERCERO,chr(9), ' ') NOM_PARTICIPA from SAF.terceros where ter_num_contrato = ? AND TER_NUM_TERCERO = ? AND TER_CVE_ST_TERCERO = 'ACTIVO'");
                break;
        } 

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

        	pstmt.setInt(1, Integer.parseInt(dParticipante.getDif_num_contrato()));   
        	pstmt.setInt(2, Integer.parseInt(dParticipante.getDif_num_pers_fid()));   
        	
            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data 
             while (rs.next()) {
               if( rs.getString("NOM_PARTICIPA") != null)
            	   return true;
               else
            	   return false;
            } 

        	onCierraConexion();
        } catch (NumberFormatException | SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCCFDI_Consulta_Participante()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }  
 	   return false;
    }
 
    public void onCFDI_Delete_CargaInterfaz() { 
        
        StringBuilder stringBuilder = new StringBuilder();
  
        try {
            // Set_SQL
            stringBuilder.append("DELETE FROM SAF.CARGA_INTERFAZ WHERE RUT_ID_RUTINA = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setString(1, "MASIVO DIRECCIONES FISCALES");

            // Execute_Update
            pstmt.executeUpdate();

        	onCierraConexion();
			
        } catch (SQLException sqlException) { 
            mensajeError += "Ocurrio el siguiente error al eliminar Carga Interfaz: " + sqlException.getMessage();
	        logger.error(mensajeError); 
        }finally {
        	onCierraConexion();
		}

    }

    public OutParameterBean onCFDI_Ejecuta_CargaMasiva(String sPath, String nomArch, int iOpcion) {

        OutParameterBean outParameterBean = new OutParameterBean();

        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);
        
        // Write_Log
        //"Ejecutar : SPN_VAPLI_MASDONFI";
        // SQL
        String sql = "{CALL DB2FIDUC.SPN_VAPLI_MASDONFI (?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            // Call_Stored_Procedured
            conexion = DataBaseConexion.getInstance().getConnection();
            cstmt = conexion.prepareCall(sql);

            // REGISTER_IN_PARAMETER
            cstmt.setDate(1, java.sql.Date.valueOf(dateTime));
            cstmt.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            cstmt.setString(3, sPath);
            cstmt.setString(4, nomArch);
            cstmt.setInt(5, iOpcion);

            // REGISTER_OUT_PARAMETER
            cstmt.registerOutParameter("iResultado", Types.INTEGER);
            cstmt.registerOutParameter("iREGISTROS", Types.INTEGER);
            cstmt.registerOutParameter("iCORRECTOS", Types.INTEGER);
            cstmt.registerOutParameter("iCONERROR", Types.INTEGER);
            cstmt.registerOutParameter("PCH_SQLSTATE_OUT", Types.CHAR);
            cstmt.registerOutParameter("PI_SQLCODE_OUT", Types.INTEGER);
            cstmt.registerOutParameter("PS_MSGERR_OUT", Types.VARCHAR);

            // EXECUTE_callableStatement
            cstmt.execute();

            // GET_OUT_PARAMETER
            int iNumResul = cstmt.getInt("iResultado");
            int iNumRegistro = cstmt.getInt("iREGISTROS");
            int iNumCorrecto = cstmt.getInt("iCORRECTOS");
            int iNumiCorrecto = cstmt.getInt("iCONERROR");
            String sMErr = cstmt.getString("PS_MSGERR_OUT");
            int sCodigo = cstmt.getInt("PI_SQLCODE_OUT");
            String sState = cstmt.getString("PCH_SQLSTATE_OUT");

            outParameterBean.setdImporteCobrado(iNumCorrecto);
            outParameterBean.setiNumFolioContab(iNumiCorrecto);
            outParameterBean.setPsMsgErrOut(sMErr);

            if (iNumiCorrecto == 0 && sMErr.equals("")) {
                bitacoraBean.setDetalle("Resultado: " + iNumResul + " Registros: " + iNumRegistro + " Correctos: " + iNumCorrecto + " Incorrectos: " + iNumiCorrecto);
            } else 
                bitacoraBean.setDetalle("Error en Carga Masiva: " + sMErr + ", SqlCode: " + sCodigo
                        + " SqlState: " + sState + " Resultado: " + iNumResul + " Registros: " + iNumRegistro + " Correctos: " + iNumCorrecto + " Incorrectos: " + iNumiCorrecto);
            
            // SET_VALUES_BITACORA
            bitacoraBean.setUsuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraBean.setTerminal((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
            bitacoraBean.setNombre("MASIVO DIRECCIONES FISCALES".toUpperCase());
            bitacoraBean.setFuncion("ADMINISTRACION_DIRECCIONES".toUpperCase());

        	onCierraConexion();
        	
            // INSERT_BITACORA
            cTesoreria.onTesoreria_InsertBitacora(bitacoraBean);

        } catch (NumberFormatException | SQLException exception) {
	           mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " onCFDI_Ejecuta_CargaMasiva()";
	           logger.error(mensajeError); 
        } finally {
        	onCierraConexion();
		}
        return outParameterBean;
    }
    

    public List<CargaInterfazBean> onCFDI_ConsultaInterfaz(String sRutina, String sPath, String sNomArch) {
  
        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);
        
        StringBuilder stringBuilder = new StringBuilder();
        List<CargaInterfazBean> cargaInterfaz = new ArrayList<>(); 

        try {
            // SQL
            stringBuilder.append("SELECT CARINT_SEC_ARCH, CARINT_SECUENCIAL, CARINT_CADENA, CARINT_ESTATUS,CARINT_MENSAJE,CARINT_NOM_ARCH");
            stringBuilder.append(" FROM SAF.CARGA_INTERFAZ");
            stringBuilder.append(" WHERE RUT_ID_RUTINA = ? ");
            stringBuilder.append(" AND CARINT_FECHA = ? ");
            stringBuilder.append(" AND CARINT_NOM_PATH = ?");
            stringBuilder.append(" AND CARINT_NOM_ARCH = ?");
            stringBuilder.append(" ORDER BY CARINT_SEC_ARCH, CARINT_SECUENCIAL ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setString(1, sRutina);
            pstmt.setDate(2,  java.sql.Date.valueOf(dateTime));
            pstmt.setString(3, sPath);
            pstmt.setString(4, sNomArch);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            if (rs != null) {
                // Set_Rutinas
                while (rs.next()) {
                    CargaInterfazBean cargaBean = new CargaInterfazBean();

                    // Set_Values
                    cargaBean.setSecuencialArchivo(rs.getInt("CARINT_SEC_ARCH"));
                    cargaBean.setSecuencial(rs.getInt("CARINT_SECUENCIAL"));
                    cargaBean.setCadena(rs.getString("CARINT_CADENA"));
                    
                    if(rs.getString("CARINT_ESTATUS") != null) {
	                    if(rs.getString("CARINT_ESTATUS").equals("A"))  cargaBean.setEstatus("Cargado");
	                    if(rs.getString("CARINT_ESTATUS").equals("V"))  cargaBean.setEstatus("Correcto");
	                    if(rs.getString("CARINT_ESTATUS").equals("P"))  cargaBean.setEstatus("Procesado");
	                    if(rs.getString("CARINT_ESTATUS").equals("E"))  cargaBean.setEstatus("Incorrecto");
                    }
                    cargaBean.setMensaje(rs.getString("CARINT_MENSAJE"));
                    cargaBean.setNombreArchivo(rs.getString("CARINT_NOM_ARCH"));

                    // Add_List
                    cargaInterfaz.add(cargaBean);
                }
            }
            
        	onCierraConexion();
        	
        } catch (SQLException Err){
	           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_ConsultaInterfaz()";
	           logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return cargaInterfaz;
    }

    public int onCFDI_existeCargaInterfaz(String sRutina, java.sql.Date fechaSistema) {

        int secArc = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // SQL
            stringBuilder.append("SELECT MAX(CARINT_SEC_ARCH) as CONT ");
            stringBuilder.append(" FROM SAF.CARGA_INTERFAZ");
            stringBuilder.append(" WHERE RUT_ID_RUTINA = ? ");
            stringBuilder.append(" AND CARINT_FECHA = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString()); 
            
            pstmt.setString(1, sRutina);
            pstmt.setDate(2, fechaSistema);

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            while (rs.next()) {
                secArc = rs.getInt("CONT");
            }
             
        	onCierraConexion();
            
        } catch (SQLException  Err){
	        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_existeCargaInterfaz()";
	        logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return secArc;
    }

    public int onCFDI_Insert_CargaInterfaz(CargaInterfazBean cargaInterfazBean) {
        
        StringBuilder stringBuilder = new StringBuilder();

        // Variables
        int result = 0;

        java.sql.Date dateFec = new java.sql.Date(cargaInterfazBean.getFecha().getTime());

        try {
            // Set_SQL
            stringBuilder.append("INSERT INTO SAF.CARGA_INTERFAZ (CARINT_NUM_USUARIO, RUT_ID_RUTINA, CARINT_FECHA, CARINT_SEC_ARCH, CARINT_SECUENCIAL, CARINT_NOM_PATH, CARINT_NOM_ARCH, CARINT_ARCH_TMP, CARINT_CADENA, CARINT_ESTATUS, CARINT_MENSAJE)");
            stringBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            pstmt.setString(2, "MASIVO DIRECCIONES FISCALES");
            pstmt.setDate(3, dateFec);
            pstmt.setInt(4, cargaInterfazBean.getSecuencialArchivo());
            pstmt.setInt(5, cargaInterfazBean.getSecuencial());
            pstmt.setString(6, cargaInterfazBean.getRuta());
            pstmt.setString(7, cargaInterfazBean.getNombreArchivo());
            pstmt.setString(8, cargaInterfazBean.getArchivoTemporal());
            pstmt.setString(9, cargaInterfazBean.getCadena().toUpperCase());
            pstmt.setString(10, cargaInterfazBean.getEstatus());
            pstmt.setString(11, cargaInterfazBean.getMensaje());

            // Execute_Update
            result = pstmt.executeUpdate();
            onCierraConexion();
            
        } catch (SQLException Err){
	        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_Insert_CargaInterfaz()";
	        logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }

        return result;
    }
     
    public int onCFDI_validaEliminaDir(DirecciFBean dParticipante) {

        int iFact = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try { 
            // SQL
            stringBuilder.append("SELECT COALESCE(COUNT(*),0) CONT FROM SAF.FACTSELLO");
            stringBuilder.append(" WHERE FAC_NUM_CONTRATO = ? ");
            stringBuilder.append(" AND  FAC_CVE_PERS_FID = ? ");
            stringBuilder.append(" AND  FAC_NUM_PERS_FID = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());
             
            // Set_Values            
            pstmt.setInt(1, Integer.parseInt(dParticipante.getDif_num_contrato()));
            pstmt.setString(2, dParticipante.getDif_cve_pers_fid());
            pstmt.setInt(3, Integer.parseInt(dParticipante.getDif_num_pers_fid()));

            // Execute_Query
            rs = pstmt.executeQuery();

            // Get_Data
            while (rs.next()) {
            	iFact = rs.getInt("CONT");
            }
             
        	onCierraConexion();
            
        } catch (SQLException  Err){
	        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaEliminaDir()";
	        logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }

        return iFact;
    }
     
    public boolean onCFDI_EliminaDirecci(DirecciFBean dParticipante) {
  /*(DirecciFBean dParticipante, String sRfC, String sNom) {

	        int iReg = 0;
	        StringBuilder stringBuilder = new StringBuilder(); */
        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();
        boolean valorRetorno = false;
        
        try {

            /* * * * * * * * * * * * * * * * * * * *
			//Elimina_Direccion_Fiscal
			/* * * * * * * * * * * * * * * * * * * */
            // SQL 
            stringBuilder.append("DELETE FROM SAF.DIRECCIF ");
            stringBuilder.append(" Where DIF_NUM_CONTRATO = ?");
            stringBuilder.append(" AND DIF_CVE_PERS_FID = ? ");
            stringBuilder.append(" AND DIF_NUM_PERS_FID =? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setInt(1, Integer.parseInt(dParticipante.getDif_num_contrato()));
            pstmt.setString(2, dParticipante.getDif_cve_pers_fid());
            pstmt.setInt(3, Integer.parseInt(dParticipante.getDif_num_pers_fid())); 
            
            // Execute_Query
            iReg = pstmt.executeUpdate();
            onCierraConexion();

            if (iReg > 0) {
                valorRetorno = true;
                 
                // SET_VALUES_BITACORA
                bitacoraUsuarioBean.setBit_cve_funcion("BAJA");
                bitacoraUsuarioBean.setBit_nom_pgm("administracionDireccionesFiscales");
                bitacoraUsuarioBean.setBit_det_bitacora("Dirección Fiscal, Fiso:" + dParticipante.getDif_num_contrato() + ", Participante " + dParticipante.getDif_cve_pers_fid() + ", No. Participante " +dParticipante.getDif_num_pers_fid());
 
                // INSERT_BITACORA
            	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
            } 

        } catch (SQLException Err){
	           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_EliminaDirecci()";
	           logger.error(mensajeError);
        } finally {
            onCierraConexion();
        }

        return valorRetorno;
    }
    
    public boolean onCFDI_InsertDirecci(DirecciFBean direcciBean) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();


        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);
        
        try {
            // SQL
            stringBuilder.append("INSERT INTO SAF.DIRECCIF (DIF_NUM_CONTRATO, DIF_CVE_PERS_FID, DIF_NUM_PERS_FID, DIF_RECEP_CALLE, DIF_RECEP_NO_EXT, DIF_RECEP_NO_INT, DIF_RECEP_COLONIA, DIF_RECEP_LOCALIDAD, DIF_RECEP_MUNICIPIO, DIF_NUM_PAIS, DIF_NUM_ESTADO, DIF_RECEP_CP, DIF_RECEP_REFERECIA, DIF_TELEFONO, DIF_MAIL, DIF_FEC_ALTA, DIF_FEC_ULTMOD, DIF_REGIMENFISCAL, DIF_NOM_LEGAL)");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setInt(1, Integer.parseInt(direcciBean.getDif_num_contrato()));
            pstmt.setString(2, direcciBean.getDif_cve_pers_fid());
            pstmt.setInt(3, Integer.parseInt(direcciBean.getDif_num_pers_fid()));
            pstmt.setString(4, direcciBean.getDif_recep_calle());
            pstmt.setString(5, direcciBean.getDif_recep_no_ext());
            pstmt.setString(6, direcciBean.getDif_recep_no_int());
            pstmt.setString(7, direcciBean.getDif_recep_colonia());
            pstmt.setString(8, direcciBean.getDif_recep_localidad());
            pstmt.setString(9, null);
            pstmt.setInt(10, direcciBean.getDif_num_pais());
            pstmt.setInt(11, direcciBean.getDif_num_estado());
            pstmt.setString(12, direcciBean.getDif_recep_cp());
            pstmt.setString(13, direcciBean.getDif_recep_referencia());
            pstmt.setString(14, direcciBean.getDif_telefono());
            pstmt.setString(15, direcciBean.getDif_mail());             
            pstmt.setDate(16, java.sql.Date.valueOf(dateTime));
            pstmt.setDate(17, java.sql.Date.valueOf(dateTime));
            pstmt.setString(18, direcciBean.getDif_regimen_fiscal()); 
            pstmt.setString(19, direcciBean.getDif_nom_legal());        

            // Execute_Update
            iReg = pstmt.executeUpdate();
            onCierraConexion();

            if (iReg > 0) {

                // SET_VALUES_BITACORA
                bitacoraUsuarioBean.setBit_cve_funcion("ALTA");
                bitacoraUsuarioBean.setBit_nom_pgm("administracionDireccionesFiscales");
                bitacoraUsuarioBean.setBit_det_bitacora("Dirección Fiscal, Fiso:" + direcciBean.getDif_num_contrato() + ", Participante " +  direcciBean.getDif_cve_pers_fid() + ", No. Participante " +direcciBean.getDif_num_pers_fid());
 
                // INSERT_BITACORA
            	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean); 
                return true;
            }

        }  catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_InsertDirecci()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }
        return false;
    }
    
    public boolean onCFDI_ModificarDirecci(DirecciFBean direcciBean) {

        int iReg = 0;
        StringBuilder stringBuilder = new StringBuilder();


        //DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter); 
        
        try {
            // SQL
            stringBuilder.append("UPDATE SAF.DIRECCIF SET DIF_RECEP_CALLE = ? ");
            stringBuilder.append(", DIF_RECEP_NO_EXT = ? ");
            stringBuilder.append(", DIF_RECEP_NO_INT = ? ");
            stringBuilder.append(", DIF_RECEP_COLONIA = ? ");
            stringBuilder.append(", DIF_RECEP_LOCALIDAD = ? ");
            stringBuilder.append(", DIF_RECEP_MUNICIPIO = ? ");
            stringBuilder.append(", DIF_NUM_PAIS = ? ");
            stringBuilder.append(", DIF_NUM_ESTADO = ? ");
            stringBuilder.append(", DIF_RECEP_CP = ? ");
            stringBuilder.append(", DIF_RECEP_REFERECIA = ? ");
            stringBuilder.append(", DIF_TELEFONO = ? ");
            stringBuilder.append(", DIF_MAIL = ? ");
            stringBuilder.append(", DIF_FEC_ULTMOD = ? ");
            stringBuilder.append(", DIF_REGIMENFISCAL = ? ");
            stringBuilder.append(", DIF_NOM_LEGAL = ? ");
            stringBuilder.append(" Where DIF_NUM_CONTRATO = ?");
            stringBuilder.append(" AND DIF_CVE_PERS_FID = ?");
            stringBuilder.append(" AND DIF_NUM_PERS_FID = ?"); 
            
            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            pstmt.setString(1, direcciBean.getDif_recep_calle());
            pstmt.setString(2, direcciBean.getDif_recep_no_ext());
            pstmt.setString(3, direcciBean.getDif_recep_no_int());
            pstmt.setString(4, direcciBean.getDif_recep_colonia());
            pstmt.setString(5, direcciBean.getDif_recep_localidad());
            pstmt.setString(6, null);
            pstmt.setInt(7, direcciBean.getDif_num_pais());
            pstmt.setInt(8, direcciBean.getDif_num_estado());
            pstmt.setString(9, direcciBean.getDif_recep_cp());
            pstmt.setString(10, direcciBean.getDif_recep_referencia());
            pstmt.setString(11, direcciBean.getDif_telefono());
            pstmt.setString(12, direcciBean.getDif_mail());             
            pstmt.setDate(13,  java.sql.Date.valueOf(dateTime)); 
            pstmt.setString(14, direcciBean.getDif_regimen_fiscal());  
            pstmt.setString(15, direcciBean.getDif_nom_legal());         
            pstmt.setInt(16, Integer.parseInt(direcciBean.getDif_num_contrato()));
            pstmt.setString(17, direcciBean.getDif_cve_pers_fid());
            pstmt.setInt(18, Integer.parseInt(direcciBean.getDif_num_pers_fid()));

            // Execute_Update
            iReg = pstmt.executeUpdate();
            onCierraConexion();

            if (iReg > 0) {

                // SET_VALUES_BITACORA
                bitacoraUsuarioBean.setBit_cve_funcion("MODIFICACION");
                bitacoraUsuarioBean.setBit_nom_pgm("administracionDireccionesFiscales");
                bitacoraUsuarioBean.setBit_det_bitacora("Dirección Fiscal, Fiso:" + direcciBean.getDif_num_contrato() + ", Participante " +  direcciBean.getDif_cve_pers_fid() + ", No. Participante " +direcciBean.getDif_num_pers_fid());
 
                // INSERT_BITACORA
            	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
            	
                return true;
            }

        }  catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_ModificarDirecci()";
            logger.error(mensajeError);
        } finally {
        	onCierraConexion();
        }
        return false;
    }
    
   public Map<Integer, String> onCFDI_getPaises() {

       StringBuilder QuerySql = new StringBuilder(); 
       Map<Integer, String> paises = new LinkedHashMap<Integer, String>();

       try {
           // SQL
           QuerySql.append("select PAI_NUM_PAIS, PAI_NOM_PAIS from SAF.paises where PAI_NUM_PAIS > 0 order by PAI_NUM_PAIS");

           // Call_Operaciones_BD
           conexion = DataBaseConexion.getInstance().getConnection();
           pstmt = conexion.prepareStatement(QuerySql.toString());

           // Execute_Query
           rs = pstmt.executeQuery();

           // Get_Data
           if (rs != null) {  
               // Vacio_el_primero
        	   paises.put(0, "");
        	   
               while (rs.next()) {

                   // Add_List
            	   paises.put(rs.getInt("PAI_NUM_PAIS"),
                        rs.getString("PAI_NOM_PAIS"));
               }
           }

       	onCierraConexion();
           
       }  catch (NumberFormatException | SQLException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_getPaises()";
           logger.error(mensajeError);
       } finally {
       	onCierraConexion();
       }

       return paises;
   }

   public Map<Integer, String> onCFDI_getEstados(int iPais) {

       StringBuilder QuerySql = new StringBuilder(); 
       Map<Integer, String> estados = new LinkedHashMap<Integer, String>();
   	
       try {
           // SQL
           QuerySql.append("select edo_num_estado, edo_nom_estado from SAF.estados where edo_num_pais = ? order by edo_nom_estado ");

           // Call_Operaciones_BD
           conexion = DataBaseConexion.getInstance().getConnection();
           pstmt = conexion.prepareStatement(QuerySql.toString());

           pstmt.setInt(1, iPais);       	

           // Execute_Query
           rs = pstmt.executeQuery();

           // Get_Data
           if (rs != null) {
               // Vacio_el_primero
        	   estados.put(0, "");

               while (rs.next()) {

                   // Add_List
            	   estados.put(rs.getInt("edo_num_estado"),
                        rs.getString("edo_nom_estado"));
               }
           }

       	onCierraConexion();
           
       }  catch (NumberFormatException | SQLException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_getEstados()";
           logger.error(mensajeError);
       } finally {
       	onCierraConexion();
       }

       return estados;
   }
    
   public String onCFDI_getNomFiso(int iFiso) {

       StringBuilder QuerySql = new StringBuilder(); 
       String fideicomiso = "";
   	
       try {
           // SQL
           QuerySql.append("SELECT CTO_CVE_ST_CONTRAT, CTO_NOM_CONTRATO, CTO_CVE_SUBCTO FROM SAF.CONTRATO WHERE CTO_NUM_CONTRATO = ? ");

           // Call_Operaciones_BD
           conexion = DataBaseConexion.getInstance().getConnection();
           pstmt = conexion.prepareStatement(QuerySql.toString());

           pstmt.setInt(1, iFiso);
       	 
           // Execute_Query
           rs = pstmt.executeQuery();

           // Get_Data
           if (rs != null) {  
               while (rs.next()) { 
            	   if(rs.getString("CTO_CVE_ST_CONTRAT").equals("EXTINTO") || rs.getString("CTO_CVE_ST_CONTRAT").equals("CANCELADO")) 
                	   fideicomiso = rs.getString("CTO_CVE_ST_CONTRAT");
            	   else 
            		   fideicomiso = rs.getString("CTO_NOM_CONTRATO");
               }
           }

       	onCierraConexion();
           
       }  catch (NumberFormatException | SQLException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_getNomFiso()";
           logger.error(mensajeError);
       } finally {
       	onCierraConexion();
       }

       return fideicomiso;
   }
    
   public String onCFDI_ValidaRegimenFisc(String iClaveReg) {

       StringBuilder QuerySql = new StringBuilder(); 
       String fiscales = "";
   	
       try {
           // SQL
           QuerySql.append("select CVE_FORMA_EMP_CVE, CVE_LIMINF_CLAVE, CVE_LIMSUP_CLAVE ");
           QuerySql.append(" from SAF.claves where cve_num_clave =151 ");
           QuerySql.append(" and CVE_FORMA_EMP_CVE = ?");
           
           // Call_Operaciones_BD
           conexion = DataBaseConexion.getInstance().getConnection();
           pstmt = conexion.prepareStatement(QuerySql.toString());

           pstmt.setString(1, iClaveReg);       	

           // Execute_Query
           rs = pstmt.executeQuery();

           // Get_Data
           if (rs != null) {  
               while (rs.next()) {
                  	// Add_List 
                  	fiscales = rs.getString("CVE_LIMINF_CLAVE") + "/" + rs.getString("CVE_LIMSUP_CLAVE");
               }
           }

       	onCierraConexion();
           
       }  catch (NumberFormatException | SQLException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_ValidaRegimenFisc()";
           logger.error(mensajeError);
       } finally {
       	onCierraConexion();
       }

       return fiscales;
   }
       
   public String onCCFDI_Consulta_TipoParticipante(DirecciFBean dParticipante) {

   	String sValor="";
       StringBuilder stringBuilder = new StringBuilder();  

       try {
           // SQL 
       	switch (dParticipante.getDif_cve_pers_fid()) {
           case "FIDEICOMITENTE": 
               stringBuilder.append("SELECT FID_CVE_TIPO_PER NOM_PARTICIPA FROM SAF.FIDEICOM WHERE FID_NUM_CONTRATO = ? AND FID_FIDEICOMITENTE = ?"); 
               break;

           case "FIDEICOMISARIO":
               stringBuilder.append("SELECT BEN_CVE_TIPO_PER NOM_PARTICIPA FROM SAF.BENEFICI WHERE BEN_NUM_CONTRATO = ? AND BEN_BENEFICIARIO = ?");
               break;
           case "TERCERO":
               stringBuilder.append("SELECT TER_CVE_TIPO_PERS NOM_PARTICIPA FROM SAF.TERCEROS WHERE TER_NUM_CONTRATO = ? AND TER_NUM_TERCERO = ?");
               break;
       }
       	 
           // Call_Operaciones_BD
           conexion = DataBaseConexion.getInstance().getConnection();
           pstmt = conexion.prepareStatement(stringBuilder.toString());

       	pstmt.setInt(1, Integer.parseInt(dParticipante.getDif_num_contrato()));   
       	pstmt.setInt(2, Integer.parseInt(dParticipante.getDif_num_pers_fid()));   
       	
           // Execute_Query
           rs = pstmt.executeQuery();

           // Get_Data
           if (rs != null) { 
               while (rs.next()) {
               	sValor = rs.getString("NOM_PARTICIPA");
               }
           }

       	onCierraConexion();
       	
       } catch (NumberFormatException | SQLException Err) {
           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCCFDI_Consulta_DirParticipantes()";
           logger.error(mensajeError);
       } finally {
       	onCierraConexion();
       } 

       return sValor;
   }
   
	public List<DirecciBean> onCCFDI_Consulta_DirParticipantes(int iFideicomiso) {

		rs = null;
		pstmt = null;
		conexion = null;
		 
        StringBuilder stringBuilder = new StringBuilder();  
		List<DirecciBean> direcciones = new ArrayList<>();

		try {
			 stringBuilder.append(" SELECT DIR_NUM_CONTRATO FISO,");
					stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
					stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
					stringBuilder.append(" REPLACE(FID_NOM_FIDEICOM,CHR(9), ' ') NOMBRE,");
					stringBuilder.append(" FID_NUM_LADA_CASA LADA_CASA,");
					stringBuilder.append(" FID_NUM_TELEF_CASA TEL_CASA,");
					stringBuilder.append(" FID_NUM_LADA_OFIC lada_OFIC,");
					stringBuilder.append(" FID_NUM_TELEF_OFIC TEL_oFIC,");
					stringBuilder.append(" FID_NUM_EXT_OFIC EXT_ofic,");
					stringBuilder.append(" ' ' MAIL,");
					stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
					stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
					stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
					stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
					stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
					stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
					stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
					stringBuilder.append(" DIR_CALLE_NUM CALLE_NO");
					stringBuilder.append(" From SAF.DIRECCI ");
					stringBuilder.append(" INNER JOIN SAF.FIDEICOM ON ");
					stringBuilder.append(" FID_NUM_CONTRATO = DIR_NUM_CONTRATO ");
					stringBuilder.append(" AND FID_FIDEICOMITENTE = DIR_NUM_PERS_FID ");
					stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? ");
					stringBuilder.append(" AND FID_CVE_ST_FIDEICO = 'ACTIVO' ");
					stringBuilder.append(" and DIR_CVE_PERS_FID = 'FIDEICOMITENTE' ");
					stringBuilder.append(" union all ");
					stringBuilder.append("  SELECT  ");
					stringBuilder.append(" DIR_NUM_CONTRATO FISO,");
					stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
					stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
					stringBuilder.append(" REPLACE(BEN_NOM_BENEF,CHR(9), ' ') NOMBRE,");
					stringBuilder.append(" BEN_NUM_LADA_CASA LADA_CASA,");
					stringBuilder.append(" BEN_NUM_TELEF_CASA TEL_CASA,");
					stringBuilder.append(" BEN_NUM_LADA_OFIC lada_OFIC,");
					stringBuilder.append(" BEN_NUM_TELEF_OFIC TEL_oFIC,");
					stringBuilder.append(" BEN_NUM_EXT_OFIC EXT_ofic,");
					stringBuilder.append(" ' ' MAIL,");
					stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
					stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
					stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
					stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
					stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
					stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
					stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
					stringBuilder.append(" DIR_CALLE_NUM CALLE_NO ");
					stringBuilder.append(" From SAF.DIRECCI ");
					stringBuilder.append(" INNER JOIN SAF.BENEFICI ON ");
					stringBuilder.append(" BEN_NUM_CONTRATO = DIR_NUM_CONTRATO ");
					stringBuilder.append(" AND BEN_BENEFICIARIO = DIR_NUM_PERS_FID ");
					stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? ");
					stringBuilder.append(" AND BEN_CVE_ST_BENEFIC = 'ACTIVO' ");
					stringBuilder.append(" and DIR_CVE_PERS_FID = 'FIDEICOMISARIO' ");
					stringBuilder.append(" union all ");
					stringBuilder.append("  SELECT ");
					stringBuilder.append(" DIR_NUM_CONTRATO FISO,");
					stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
					stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
					stringBuilder.append(" REPLACE(TER_NOM_TERCERO,CHR(9), ' ') NOMBRE,");
					stringBuilder.append(" TER_NUM_LADA_CASA LADA_CASA,");
					stringBuilder.append(" TER_NUM_TELEF_CASA TEL_CASA,");
					stringBuilder.append(" TER_NUM_LADA_OFIC lada_OFIC,");
					stringBuilder.append(" TER_NUM_TELEF_OFIC TEL_oFIC,");
					stringBuilder.append(" TER_NUM_EXT_OFIC EXT_ofic,");
					stringBuilder.append(" ' ' MAIL,");
					stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
					stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
					stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
					stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
					stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
					stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
					stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
					stringBuilder.append(" DIR_CALLE_NUM CALLE_NO ");
					stringBuilder.append(" From SAF.DIRECCI ");
					stringBuilder.append(" INNER JOIN SAF.TERCEROS ON ");
					stringBuilder.append(" TER_NUM_CONTRATO = DIR_NUM_CONTRATO ");
					stringBuilder.append(" AND TER_NUM_TERCERO    = DIR_NUM_PERS_FID ");
					stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? ");
					stringBuilder.append(" AND TER_CVE_ST_TERCERO = 'ACTIVO' ");
					stringBuilder.append(" and DIR_CVE_PERS_FID = 'TERCERO' ");

			conexion = DataBaseConexion.getInstance().getConnection();
			pstmt = conexion.prepareStatement(stringBuilder.toString());

			pstmt.setInt(1, iFideicomiso);
			pstmt.setInt(2, iFideicomiso);
			pstmt.setInt(3, iFideicomiso);
			
			rs = pstmt.executeQuery();

			while (rs.next()) { 

				DirecciBean direcci_fiscales = new DirecciBean();  
				
				direcci_fiscales.setDir_num_contrato(rs.getInt("FISO"));
				direcci_fiscales.setDir_cve_pers_fid(rs.getString("TIPO_PARTICIPANTE"));
				direcci_fiscales.setDir_num_pers_fid(rs.getInt("NO_PARTICIPANTE"));
				direcci_fiscales.setDir_nombre(rs.getString("NOMBRE"));
				direcci_fiscales.setBen_num_lada_casa(rs.getString("LADA_CASA"));
				direcci_fiscales.setBen_num_telef_casa(rs.getString("TEL_CASA")); 
				direcci_fiscales.setBen_num_lada_ofic(rs.getString("lada_OFIC"));
				direcci_fiscales.setBen_num_telef_ofic(rs.getString("TEL_oFIC")); 
				direcci_fiscales.setBen_num_ext_ofic(rs.getString("EXT_ofic"));
				direcci_fiscales.setDir_mail(rs.getString("MAIL"));
				direcci_fiscales.setDir_nom_colonia(rs.getString("COLONIA"));
				direcci_fiscales.setDir_nom_poblacion(rs.getString("POBLACION"));
				direcci_fiscales.setDir_codigo_postal(rs.getString("CP"));
				direcci_fiscales.setDir_num_estado(rs.getInt("NUM_ESTADO"));
				direcci_fiscales.setDir_nom_estado(rs.getString("ESTADO"));
				direcci_fiscales.setDir_num_pais(rs.getInt("NUM_PAIS"));
				direcci_fiscales.setDir_nom_pais(rs.getString("PAIS"));
				direcci_fiscales.setDir_calle_num(rs.getString("CALLE_NO"));  
				direcciones.add(direcci_fiscales);
			}

        	onCierraConexion();

		} catch (NumberFormatException | SQLException Err) {
			mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCCFDI_Consulta_DirParticipantes()";
			logger.error(mensajeError);
		} finally {
        	onCierraConexion();
		}
		return direcciones;
	}
	
	public List<DirecciBean> onCCFDI_Consulta_DireccionesPart(int iFideicomiso, String sTipoPart) {

		rs = null;
		pstmt = null;
		conexion = null;
		 
        StringBuilder stringBuilder = new StringBuilder();  
		List<DirecciBean> direcciones = new ArrayList<>();

		try {
			
			switch(sTipoPart) {
			case "FIDEICOMITENTE":
				
				stringBuilder.append(" SELECT DIR_NUM_CONTRATO FISO,");
				stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
				stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
				stringBuilder.append(" REPLACE(FID_NOM_FIDEICOM,CHR(9), ' ') NOMBRE,");
				stringBuilder.append(" FID_NUM_LADA_CASA LADA_CASA,");
				stringBuilder.append(" FID_NUM_TELEF_CASA TEL_CASA,");
				stringBuilder.append(" FID_NUM_LADA_OFIC lada_OFIC,");
				stringBuilder.append(" FID_NUM_TELEF_OFIC TEL_oFIC,");
				stringBuilder.append(" FID_NUM_EXT_OFIC EXT_ofic,");
				stringBuilder.append(" ' ' MAIL,");
				stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
				stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
				stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
				stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
				stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
				stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
				stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
				stringBuilder.append(" DIR_CALLE_NUM CALLE_NO");
				stringBuilder.append(" From SAF.DIRECCI");
				stringBuilder.append(" INNER JOIN SAF.FIDEICOM ON");
				stringBuilder.append(" FID_NUM_CONTRATO = DIR_NUM_CONTRATO");
				stringBuilder.append(" AND FID_FIDEICOMITENTE = DIR_NUM_PERS_FID");
				stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? ");
				stringBuilder.append(" AND FID_CVE_ST_FIDEICO = 'ACTIVO'");
				stringBuilder.append(" and DIR_CVE_PERS_FID = 'FIDEICOMITENTE'"); 
				break;

			case "FIDEICOMISARIO":
				stringBuilder.append("SELECT DIR_NUM_CONTRATO FISO,");
				stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
				stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
				stringBuilder.append(" REPLACE(BEN_NOM_BENEF,CHR(9), ' ') NOMBRE,");
				stringBuilder.append(" BEN_NUM_LADA_CASA LADA_CASA,");
				stringBuilder.append(" BEN_NUM_TELEF_CASA TEL_CASA,");
				stringBuilder.append(" BEN_NUM_LADA_OFIC lada_OFIC,");
				stringBuilder.append(" BEN_NUM_TELEF_OFIC TEL_oFIC,");
				stringBuilder.append(" BEN_NUM_EXT_OFIC EXT_ofic,");
				stringBuilder.append(" ' ' MAIL,");
				stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
				stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
				stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
				stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
				stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
				stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
				stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
				stringBuilder.append(" DIR_CALLE_NUM CALLE_NO");
				stringBuilder.append(" From SAF.DIRECCI");
				stringBuilder.append(" INNER JOIN SAF.BENEFICI ON");
				stringBuilder.append(" BEN_NUM_CONTRATO = DIR_NUM_CONTRATO");
				stringBuilder.append(" AND BEN_BENEFICIARIO = DIR_NUM_PERS_FID");
				stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? "); 
				stringBuilder.append(" AND BEN_CVE_ST_BENEFIC = 'ACTIVO'");
				stringBuilder.append(" and DIR_CVE_PERS_FID = 'FIDEICOMISARIO'");
				break;

			case "TERCERO":
				stringBuilder.append(" SELECT DIR_NUM_CONTRATO FISO,");
				stringBuilder.append(" DIR_CVE_PERS_FID TIPO_PARTICIPANTE,");
				stringBuilder.append(" DIR_NUM_PERS_FID NO_PARTICIPANTE,");
				stringBuilder.append(" REPLACE(TER_NOM_TERCERO,CHR(9), ' ') NOMBRE,");
				stringBuilder.append(" TER_NUM_LADA_CASA LADA_CASA,");
				stringBuilder.append(" TER_NUM_TELEF_CASA TEL_CASA,");
				stringBuilder.append(" TER_NUM_LADA_OFIC lada_OFIC,");
				stringBuilder.append(" TER_NUM_TELEF_OFIC TEL_oFIC,");
				stringBuilder.append(" TER_NUM_EXT_OFIC EXT_ofic,");
				stringBuilder.append(" ' ' MAIL,");
				stringBuilder.append(" DIR_NOM_COLONIA    COLONIA,");
				stringBuilder.append(" DIR_NOM_POBLACION  POBLACION,");
				stringBuilder.append(" DIR_CODIGO_POSTAL  CP,");
				stringBuilder.append(" DIR_NUM_ESTADO     NUM_ESTADO,");
				stringBuilder.append(" DIR_NOM_ESTADO     ESTADO,");
				stringBuilder.append(" DIR_NUM_PAIS   NUM_PAIS,");
				stringBuilder.append(" DIR_NOM_PAIS   PAIS,");
				stringBuilder.append(" DIR_CALLE_NUM CALLE_NO");
				stringBuilder.append(" From SAF.DIRECCI");
				stringBuilder.append(" INNER JOIN SAF.TERCEROS ON");
				stringBuilder.append(" TER_NUM_CONTRATO = DIR_NUM_CONTRATO");
				stringBuilder.append(" AND TER_NUM_TERCERO    = DIR_NUM_PERS_FID");
				stringBuilder.append(" WHERE  DIR_NUM_CONTRATO = ? ");
				stringBuilder.append(" AND TER_CVE_ST_TERCERO = 'ACTIVO'");
				stringBuilder.append(" and DIR_CVE_PERS_FID = 'TERCERO'");
				break; 
			} 

			conexion = DataBaseConexion.getInstance().getConnection();
			pstmt = conexion.prepareStatement(stringBuilder.toString());

			pstmt.setInt(1, iFideicomiso); 
			
			rs = pstmt.executeQuery();

			while (rs.next()) { 

				DirecciBean direcci_fiscales = new DirecciBean();  
				
				direcci_fiscales.setDir_num_contrato(rs.getInt("FISO"));
				direcci_fiscales.setDir_cve_pers_fid(rs.getString("TIPO_PARTICIPANTE"));
				direcci_fiscales.setDir_num_pers_fid(rs.getInt("NO_PARTICIPANTE"));
				direcci_fiscales.setDir_nom_atencion(rs.getString("NOMBRE")); 
				direcci_fiscales.setBen_num_lada_casa(rs.getString("LADA_CASA"));
				direcci_fiscales.setBen_num_telef_casa(rs.getString("TEL_CASA")); 
				direcci_fiscales.setBen_num_lada_ofic(rs.getString("lada_OFIC"));
				direcci_fiscales.setBen_num_telef_ofic(rs.getString("TEL_oFIC")); 
				direcci_fiscales.setBen_num_ext_ofic(rs.getString("EXT_ofic"));
				direcci_fiscales.setDir_mail(rs.getString("MAIL"));
				direcci_fiscales.setDir_nom_colonia(rs.getString("COLONIA"));
				direcci_fiscales.setDir_nom_poblacion(rs.getString("POBLACION"));
				direcci_fiscales.setDir_codigo_postal(rs.getString("CP"));
				direcci_fiscales.setDir_num_estado(rs.getInt("NUM_ESTADO"));
				direcci_fiscales.setDir_nom_estado(rs.getString("ESTADO"));
				direcci_fiscales.setDir_num_pais(rs.getInt("NUM_PAIS"));
				direcci_fiscales.setDir_nom_pais(rs.getString("PAIS"));
				direcci_fiscales.setDir_calle_num(rs.getString("CALLE_NO"));  
				direcciones.add(direcci_fiscales);
			}

        	onCierraConexion();

		} catch (NumberFormatException | SQLException Err) {
			mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCCFDI_Consulta_DireccionesPart()";
			logger.error(mensajeError);
		} finally {
        	onCierraConexion();
		}
		return direcciones;
	}

	   public String onCCFDI_BuscaRegimen(String sNomReg) {

	   		String sValor="", sNombreFiltro=""; 
	   		StringBuilder stringBuilder = new StringBuilder();  

	       try {
	           // SQL  
	           stringBuilder.append("SELECT CVE_FORMA_EMP_CVE, CVE_DESC_CLAVE FROM SAF.CLAVES WHERE CVE_NUM_CLAVE =151");
	           stringBuilder.append(" AND CVE_DESC_CLAVE LIKE ? ");
	            
	           // Call_Operaciones_BD
	           conexion = DataBaseConexion.getInstance().getConnection();
	           pstmt = conexion.prepareStatement(stringBuilder.toString());

	           sNomReg = sNomReg.replace("Régimen de", "").trim();
	           sNombreFiltro = sNomReg.replace("Régimen ", "").replace(" PM", "").trim();
	            	           
	           pstmt.setString(1, "%" + sNombreFiltro + "%"); 
	           
	           // Execute_Query
	           rs = pstmt.executeQuery();

	           // Get_Data
	           if (rs != null) { 
	               while (rs.next()) { 
	               	sValor = rs.getString("CVE_FORMA_EMP_CVE");
	               }
	           }

	       	onCierraConexion();
	       	
	       } catch (NumberFormatException | SQLException Err) {
	           mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCCFDI_BuscaRegimen()";
	           logger.error(mensajeError);
	       } finally {
	       	onCierraConexion();
	       } 

	       return sValor;
	   }
 
	    public boolean onCFDI_ActualizaRFC_Part(DirecciFBean dParticipante) {

	        int iReg = 0;
	        StringBuilder stringBuilder = new StringBuilder(); 
	        try {
	        	
	        	switch (dParticipante.getDif_cve_pers_fid()) {
	                case "FIDEICOMITENTE": 
	                    stringBuilder.append("UPDATE SAF.FIDEICOM SET FID_RFC = ?, ");  
	                    stringBuilder.append(" FID_NOM_FIDEICOM = ? ");  
	                    stringBuilder.append(" WHERE FID_NUM_CONTRATO = ? ");
	                    stringBuilder.append(" AND FID_FIDEICOMITENTE = ? "); 
	                    stringBuilder.append(" AND FID_CVE_ST_FIDEICO = 'ACTIVO' "); 
	                    break; 
	                case "FIDEICOMISARIO": 
	                    stringBuilder.append("UPDATE SAF.BENEFICI SET BEN_RFC = ?, "); 
	                    stringBuilder.append(" BEN_NOM_BENEF = ? ");   
	                    stringBuilder.append(" WHERE BEN_NUM_CONTRATO = ? ");
	                    stringBuilder.append(" AND BEN_BENEFICIARIO = ? "); 
	                    stringBuilder.append(" AND BEN_CVE_ST_BENEFIC = 'ACTIVO' ");  
	                    break;
	                case "TERCERO": 
	                    stringBuilder.append("UPDATE SAF.TERCEROS SET TER_RFC = ?,");  
	                    stringBuilder.append(" TER_NOM_TERCERO = ? ");   
	                    stringBuilder.append(" WHERE TER_NUM_CONTRATO = ? ");
	                    stringBuilder.append(" AND TER_NUM_TERCERO = ? "); 
	                    stringBuilder.append(" AND TER_CVE_ST_TERCERO = 'ACTIVO' ");  
	                    break;
	            } 
	             
	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());

	            // Set_Values
	            pstmt.setString(1, dParticipante.getDif_rfc()); 
	            pstmt.setString(2, dParticipante.getDif_nom_pers().substring(0, Math.min(dParticipante.getDif_nom_pers().length(), 144))); 
	            pstmt.setInt(3, Integer.parseInt(dParticipante.getDif_num_contrato())); 
	            pstmt.setInt(4, Integer.parseInt(dParticipante.getDif_num_pers_fid()));

	            // Execute_Update
	            iReg = pstmt.executeUpdate();
	            onCierraConexion();

	            if (iReg > 0) {

	                // SET_VALUES_BITACORA
	                bitacoraUsuarioBean.setBit_cve_funcion("MODIFICACION");
	                bitacoraUsuarioBean.setBit_nom_pgm("administracionDireccionesFiscales");
	                bitacoraUsuarioBean.setBit_det_bitacora("Actualiza datos, Fiso:" + dParticipante.getDif_num_contrato() + ", Participante " + dParticipante.getDif_cve_pers_fid() + ", No. Participante " + dParticipante.getDif_num_pers_fid() + ", RFC " + dParticipante.getDif_rfc());
	 
	                // INSERT_BITACORA
	            	onCFDI_InsertBitacoraUsuario(bitacoraUsuarioBean);
	            	
	                return true;
	            }

	        }  catch (SQLException Err) {
	            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCFDI_ModificarDirecci()";
	            logger.error(mensajeError);
	        } finally {
	        	onCierraConexion();
	        }
	        return false;
	    }
	     
	   public boolean onCFDI_validaExistenciaDir(DirecciFBean dParticipante) {
	   
	        boolean valorRetorno = false;
	        StringBuilder stringBuilder = new StringBuilder();  
	        
	        try {

	            /* * * * * * * * * * * * * * * * * * * *
				//Elimina_Direccion_Fiscal
				/* * * * * * * * * * * * * * * * * * * */
	            // SQL 
	            stringBuilder.append("SELECT DIF_NUM_CONTRATO FROM SAF.DIRECCIF ");
	            stringBuilder.append(" WHERE DIF_NUM_CONTRATO = ? ");
	            stringBuilder.append(" AND DIF_CVE_PERS_FID = ? ");
	            stringBuilder.append(" AND DIF_NUM_PERS_FID = ? ");

	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());

	            // Set_Values
	            pstmt.setInt(1, Integer.parseInt(dParticipante.getDif_num_contrato()));
	            pstmt.setString(2, dParticipante.getDif_cve_pers_fid());
	            pstmt.setInt(3, Integer.parseInt(dParticipante.getDif_num_pers_fid()));
 
	            // Execute_Query
	            rs = pstmt.executeQuery();

	            // Get_Data
	            while (rs.next()) { 
	                if (rs.getInt("DIF_NUM_CONTRATO") > 0) {
	                    valorRetorno = true;
	                }
	            }
	            onCierraConexion();

	        } catch (SQLException Err){
		        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaExistenciaDir()";
		        logger.error(mensajeError);
	        } finally {
	            onCierraConexion();
	        }

	        return valorRetorno;
	    }
	   
	   public boolean onCFDI_validaFacturaEgresos(String sUUID) {
	        boolean valorRetorno = false;
	        StringBuilder stringBuilder = new StringBuilder();
	        
	        try {

	            /* * * * * * * * * * * * * * * * * * * *
		   		//-- VALIDACION DE FACTURAS DE EGRESO. 
				/* * * * * * * * * * * * * * * * * * * */
	            // SQL 
	            stringBuilder.append("SELECT COUNT(*) CONTADOR FROM SAF.FACTSELLO WHERE FAC_STATUS_CTE IN('EGRESO','PUE','PPD','CP') AND FAC_UUID_RELACIONADO = ? ");

	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());

	            // Set_Values 
	            pstmt.setString(1, sUUID); 
 
	            // Execute_Query
	            rs = pstmt.executeQuery();

	            // Get_Data
	            while (rs.next()) { 
	                if (rs.getInt("CONTADOR") > 0) {
	                    valorRetorno = true;
	                }
	            }
	            onCierraConexion();

	        } catch (SQLException Err){
		        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaFacturaEgresos()";
		        logger.error(mensajeError);
	        } finally {
	            onCierraConexion();
	        }

	        return valorRetorno;
	    }

	   public boolean onCFDI_validaFacturaReexpedicion(String sUUID) {
	        boolean valorRetorno = false;
	        StringBuilder stringBuilder = new StringBuilder();
	        
	        try {

	            /* * * * * * * * * * * * * * * * * * * *
				-- VALIDACION DE FACTURAS DE REEXPEDICION.
				/* * * * * * * * * * * * * * * * * * * */
	            // SQL 
	            stringBuilder.append("SELECT COUNT(*) CONTADOR FROM SAF.FACTSELLO f WHERE FAC_STATUS IN('ACTIVO','VIGENTE','TRANSFERIDO','CANCELADA','ALTA') AND FAC_UUID_RELACIONADO =  ? ");

	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());

	            // Set_Values 
	            pstmt.setString(1, sUUID); 
 
	            // Execute_Query
	            rs = pstmt.executeQuery();

	            // Get_Data
	            while (rs.next()) { 
	                if (rs.getInt("CONTADOR") > 0) {
	                    valorRetorno = true;
	                }
	            }
	            onCierraConexion();

	        } catch (SQLException Err){
		        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaFacturaReexpedicion()";
		        logger.error(mensajeError);
	        } finally {
	            onCierraConexion();
	        }

	        return valorRetorno;
	    } 

	   public boolean onCFDI_validaFacturaCancelada(String sUUID) {
	        boolean valorRetorno = false;
	        StringBuilder stringBuilder = new StringBuilder();
	        
	        try {

	            /* * * * * * * * * * * * * * * * * * * *
				-- VALIDACION DE FACTURAS CANCELADAS.
				/* * * * * * * * * * * * * * * * * * * */
	            // SQL 
	            stringBuilder.append("SELECT COUNT(*) CONTADOR FROM SAF.FDCANFACT WHERE FCR_FOLIO_CANCELA = ? AND FCR_STATUS IN ('PENDIENTE','TRANSFERIDO','CANCELADO') ");

	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());

	            // Set_Values 
	            pstmt.setString(1, sUUID); 
 
	            // Execute_Query
	            rs = pstmt.executeQuery();

	            // Get_Data
	            while (rs.next()) { 
	                if (rs.getInt("CONTADOR") > 0) {
	                    valorRetorno = true;
	                }
	            }
	            onCierraConexion();

	        } catch (SQLException Err){
		        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaFacturaCancelada()";
		        logger.error(mensajeError);
	        } finally {
	            onCierraConexion();
	        }

	        return valorRetorno;
	    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *  F A C T U R A S   C A N C E L A D A S* * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
		public List<FactSelloBean> onFactSello_GetFacturasCanceladas(String fechaInicio, String fechaFin, String tipoFactura, String fideicomiso, String tipoParticipante, String estatus, String noParticipante, String uuid) throws SQLException {
		      //Objects
			  List<FactSelloBean> facturas = new ArrayList<>();
			  StringBuilder stringBuilder = new StringBuilder();
			        
			  try {
				 //SQL 
			     stringBuilder.append("SELECT FAC_SISTEMA, FAC_FECHA, FAC_NUM_CONTRATO, FAC_FOLIO_FACT, FAC_NUM_MONEDA, FAC_CVE_PERS_FID, FAC_NUM_PERS_FID, ");
			     stringBuilder.append(" FAC_CONDI_PAGO, FAC_FOLIO_TRAN, FAC_HONORARIOS, FAC_MORATORIOS, FAC_IVA_TRASLADADO, FAC_STATUS, FAC_STATUS_CTE, ");
			     stringBuilder.append(" FAC_CTA_PAGO, FAC_NACIONALIDAD, FAC_CVE_TIPO_PER, FAC_RFC, FAC_RFC_GENERICO, FAC_NOMBRE_RECEP, FAC_DOM_CALLE, ");
			     stringBuilder.append(" FAC_DOM_NUM_EXT, FAC_DOM_NUM_INT, FAC_DOM_COLONIA, FAC_DOM_POBLACION, FAC_DOM_REFERENCIA, FAC_DOM_ESTADO, FAC_DOM_PAIS, ");
			     stringBuilder.append(" FAC_CODIGO_POSTAL, FAC_RECEP_LOCALIDAD, FAC_FECHA_INT, FAC_TASA_IMPUESTO, FAC_MET_PAGO, COALESCE(REPLACE(FAC_MENSAJE, CHR(13) || CHR(10), ''),'') FAC_MENSAJE,  ");
			     stringBuilder.append(" FAC_UUID_RELACIONADO, FID_FOLIO_TRAN, FCR_STATUS, COALESCE(REPLACE(FCR_MOTIVO_CANCELA, CHR(13) || CHR(10), ''),'') FCR_MOTIVO_CANCELA, FCR_FOLIO_CANCELA, TO_CHAR(FCR_FEC_SOL,'DD/MM/YYYY') AS FCR_FEC_SOL, TO_CHAR(FCR_FEC_CANCELA,'DD/MM/YYYY') AS FCR_FEC_CANCELA, FCR_HORA_CANCELA, ");
			     stringBuilder.append(" FCR_FOLIO_ACUSE, FCR_ERRORES, ");
			     stringBuilder.append(" RIGHT(REPLACE(char(FAC_FECHA,ISO),'-',''),6)||RTRIM(char(FAC_NUM_CONTRATO))||FUN_RELLENA(RTRIM(char(FAC_FOLIO_FACT)),'0',CASE WHEN LENGTH (RTRIM(char(FAC_FOLIO_FACT))) < 3 THEN 2 ELSE LENGTH(RTRIM(char(FAC_FOLIO_FACT))) END ,'I') FID_FOLIO_FACT ");
			     stringBuilder.append(" FROM SAF.FACTSELLO, SAF.FDCANFACT ");
			     stringBuilder.append(" WHERE FID_FECHA = FAC_FECHA ");
			     stringBuilder.append(" AND FID_NUM_CONTRATO = FAC_NUM_CONTRATO ");
			     stringBuilder.append(" AND FID_FOLIO_FACT = FAC_FOLIO_FACT ");
			     
			     if (!"".equals(fechaInicio)) {
			    	 stringBuilder.append(" AND FCR_FEC_SOL BETWEEN '".concat(fechaInicio).concat("' AND '").concat(fechaFin).concat("'")); 
			     } 
			     
			     if (tipoFactura != null) {
			    	 stringBuilder.append(" AND FAC_STATUS_CTE = '".concat(tipoFactura).concat("'")); 
			     }
			     
			     if (!"".equals(fideicomiso)) {
			    	 stringBuilder.append(" AND FAC_NUM_CONTRATO = ".concat(fideicomiso));	 
			     }
			     
			     
			     if (tipoParticipante != null) {
			    	 stringBuilder.append(" AND FAC_CVE_PERS_FID = '".concat(tipoParticipante).concat("'"));	 
			     }
			     
			     if (estatus != null) {
			    	 if("REEXPEDIDA".equals(estatus)) { 
			    		stringBuilder.append(" AND (FAC_FECHA,FAC_NUM_CONTRATO,FAC_FOLIO_FACT) IN ");
			    		stringBuilder.append(" (SELECT FID_FECHA, FID_NUM_CONTRATO, FID_FOLIO_FACT ");
			    		stringBuilder.append(" FROM SAF.FDCANFACT ");	    		            
			    		stringBuilder.append(" WHERE FCR_STATUS = 'REEXPEDIDA') ");
			    	 } else {	 
			    	    stringBuilder.append(" AND FCR_STATUS = '".concat(estatus).concat("'"));
			    	 }
			     }
			     
			     if (!"".equals(noParticipante)) {
			    	 stringBuilder.append(" AND FAC_NUM_PERS_FID = ".concat(noParticipante));	 
			     }
			     
			     if (!"".equals(uuid)) {
			    	 stringBuilder.append(" AND FCR_FOLIO_CANCELA = '".concat(uuid).concat("'"));
			     } 
			     
			     //Call_Operaciones_BD
		         conexion = DataBaseConexion.getInstance().getConnection();
		         pstmt = conexion.prepareStatement(stringBuilder.toString());
				 
				 //Execute_Query
			     rs = pstmt.executeQuery();
			     
			     //Validate
			     if(rs != null) {
			        //Set_FactSelloBean
			        while(rs.next()) {
			          //Object
			          FactSelloBean factSelloBean = new FactSelloBean();
			          FdCanFactBean fdCanFactBean = new FdCanFactBean();
			    	  
			    	  //Set_Values
			          factSelloBean.setFacSistema(rs.getString("FAC_SISTEMA"));
			          factSelloBean.setFacFecha(java.sql.Date.valueOf(rs.getString("FAC_FECHA")));
			          factSelloBean.setFacNumContrato(rs.getInt("FAC_NUM_CONTRATO")); 
			          factSelloBean.setFacFolioFact(rs.getInt("FAC_FOLIO_FACT"));
			          factSelloBean.setFacNumMoneda(rs.getInt("FAC_NUM_MONEDA"));
			          factSelloBean.setFacCvePersFid(rs.getString("FAC_CVE_PERS_FID"));
			          factSelloBean.setFacNumPersFid(rs.getInt("FAC_NUM_PERS_FID"));
			          factSelloBean.setFacCondiPago(rs.getString("FAC_CONDI_PAGO"));
			          factSelloBean.setFacFolioTran(rs.getInt("FAC_FOLIO_TRAN"));
			          factSelloBean.setFacHonorarios(rs.getDouble("FAC_HONORARIOS")); 
			          factSelloBean.setFacMoratorios(rs.getDouble("FAC_MORATORIOS"));
			          factSelloBean.setFacIvaTraladado(rs.getDouble("FAC_IVA_TRASLADADO")); 
			          factSelloBean.setFacStatus(rs.getString("FAC_STATUS")); 
			          factSelloBean.setFacStatusCte(rs.getString("FAC_STATUS_CTE"));
			          factSelloBean.setFacCtaPago(rs.getString("FAC_CTA_PAGO"));
			          factSelloBean.setFacNacionalidad(rs.getString("FAC_NACIONALIDAD"));
			          factSelloBean.setFacCveTipoPer(rs.getString("FAC_CVE_TIPO_PER"));
			          factSelloBean.setFacRFC(rs.getString("FAC_RFC"));
			          factSelloBean.setFacRFCGenerico(rs.getString("FAC_RFC_GENERICO"));
			          factSelloBean.setFacNombreRecep(rs.getString("FAC_NOMBRE_RECEP"));
			          factSelloBean.setFacDomCalle(rs.getString("FAC_DOM_CALLE"));
			          factSelloBean.setFacDomNumExt(rs.getString("FAC_DOM_NUM_EXT")); 
			          factSelloBean.setFacDomNumInt(rs.getString("FAC_DOM_NUM_INT")); 
			          factSelloBean.setFacDomColonia(rs.getString("FAC_DOM_COLONIA"));  
			          factSelloBean.setFacDomPoblacion(rs.getString("FAC_DOM_POBLACION"));
			          factSelloBean.setFacDomReferencia(rs.getString("FAC_DOM_REFERENCIA")); 
			          factSelloBean.setFacDomEstado(rs.getString("FAC_DOM_ESTADO"));
			          factSelloBean.setFacDomPais(rs.getString("FAC_DOM_PAIS"));
			          factSelloBean.setFacCodigoPostal(rs.getString("FAC_CODIGO_POSTAL"));
			          factSelloBean.setFacRecepLocalidad(rs.getString("FAC_RECEP_LOCALIDAD")); 
			          factSelloBean.setFacFechaInt(java.sql.Date.valueOf(rs.getString("FAC_FECHA_INT")));
			          factSelloBean.setFacTasaImpuesto(rs.getDouble("FAC_TASA_IMPUESTO"));
			          factSelloBean.setFacMetPago(rs.getString("FAC_MET_PAGO"));
			          factSelloBean.setFacMensaje(rs.getString("FAC_MENSAJE"));
			          factSelloBean.setFacUUIDRelacionado(rs.getString("FAC_UUID_RELACIONADO")); 
			          fdCanFactBean.setFidFolioTran(rs.getInt("FID_FOLIO_TRAN"));
			          fdCanFactBean.setFcrStatus(rs.getString("FCR_STATUS"));
			          fdCanFactBean.setFcrMotivoCancela(rs.getString("FCR_MOTIVO_CANCELA")); 
			          fdCanFactBean.setFcrFolioCancela(rs.getString("FCR_FOLIO_CANCELA")); 
			          fdCanFactBean.setFcrFecSol(rs.getString("FCR_FEC_SOL")); 
			          fdCanFactBean.setFcrFecCancela(rs.getString("FCR_FEC_CANCELA"));
			          fdCanFactBean.setFcrHoraCancela(rs.getString("FCR_HORA_CANCELA"));
			          fdCanFactBean.setFcrFolioAcuse(rs.getString("FCR_FOLIO_ACUSE"));
			          fdCanFactBean.setFcrErrores(rs.getString("FCR_ERRORES"));
			          fdCanFactBean.setFidFolioFact(rs.getString("FID_FOLIO_FACT")); 	          
			    	  
			    	  //Add_List
			          factSelloBean.setFdCanFactBean(fdCanFactBean);
			          facturas.add(factSelloBean);
			       }
			     } 
			     onCierraConexion();
		            
			  } catch(SQLException exception) {
		           mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + " onFactSello_GetFacturasCanceladas()";
		           logger.error(mensajeError); 
			  } finally {
		            onCierraConexion();
		      }
			  
			  return facturas;
		   } 
			
    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O T R A S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public void onCFDI_InsertBitacoraUsuario(BitacoraUsuarioBean bitacoraUsuario) {

        StringBuilder stringBuilder = new StringBuilder();

        // DateTimeFormatter
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Set_Date
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(dateTimeformatter);

        try {
            // Set_SQL
            stringBuilder.append("INSERT INTO SAF.BIT_USUARIO (BIT_CVE_FUNCION, BIT_DET_BITACORA, BIT_FEC_TRANSAC, BIT_ID_TERMINAL, BIT_NOM_PGM, BIT_NUM_USUARIO)");
            stringBuilder.append(" VALUES(?, ?, ?, ?, ?, ?) ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(stringBuilder.toString());

            // Set_Values
            bitacoraUsuarioBean.setBit_num_usuario(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNumero").toString()));
            bitacoraUsuarioBean.setBit_id_terminal((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTerminal"));
            
            pstmt.setString(1, bitacoraUsuario.getBit_cve_funcion().substring(0, Math.min(bitacoraUsuario.getBit_cve_funcion().length(), 24)));
            pstmt.setString(2, bitacoraUsuario.getBit_det_bitacora().substring(0, Math.min(bitacoraUsuario.getBit_det_bitacora().length(), 199)));
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(dateTime)); 
            pstmt.setString(4, bitacoraUsuario.getBit_id_terminal());
            pstmt.setString(5, bitacoraUsuario.getBit_nom_pgm());
            pstmt.setInt(6, bitacoraUsuario.getBit_num_usuario());

            //Execute_Update
            pstmt.executeUpdate();
            onCierraConexion();
            
        } catch (SQLException Err){
	        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_InsertBitacoraUsuario()";
	        logger.error(mensajeError); 
        } finally {
            onCierraConexion();
        }
    }
    
	   public String[] onCFDI_validaLimiteInferior_FecContable() {
		    String[] sFechas = new String[5];
	        StringBuilder stringBuilder = new StringBuilder();
	        
	        try {

	            /* * * * * * * * * * * * * * * * * * * *
		   		//--LA FECHA DEL LIMITE INFERIOR 
				/* * * * * * * * * * * * * * * * * * * */
	            // SQL 
	            stringBuilder.append("SELECT DATE(EXTRACT(YEAR FROM DATE(DATE(FCO_ANO_DIA || '-01-01') - 1 DAY))|| '-01-01') AS FECHA_LIMITE, ");
	            stringBuilder.append(" DATE(FCO_ANO_DIA || '-' || FCO_MES_DIA || '-' || FCO_DIA_DIA) AS FECHA, ");
	            stringBuilder.append(" EXTRACT(YEAR FROM DATE(LAST_DAY(DATE(FCO_ANO_DIA || '-12-' || FCO_DIA_DIA)) + 1 DAY)) AS ANIO_SIGUIENTE, EXTRACT(YEAR FROM DATE(FCO_ANO_DIA || '-' || FCO_MES_DIA || '-' || FCO_DIA_DIA)) AS ANIO_ACTUAL, ");
	            stringBuilder.append(" EXTRACT(YEAR FROM DATE(DATE(FCO_ANO_DIA || '-01-01') - 1 DAY)) AS ANIO_ANTERIOR, EXTRACT(MONTH FROM DATE(FCO_ANO_DIA || '-' || FCO_MES_DIA || '-' || FCO_DIA_DIA)) AS MES_ACTUAL ");
	            stringBuilder.append(" FROM SAF.FECCONT");

	            // Call_Operaciones_BD
	            conexion = DataBaseConexion.getInstance().getConnection();
	            pstmt = conexion.prepareStatement(stringBuilder.toString());
   
	            // Execute_Query
	            rs = pstmt.executeQuery();

			     //Validate
			     if(rs != null) {
			        //Set_FactSelloBean
			        while(rs.next()) {
		            	sFechas[0] = rs.getString("FECHA_LIMITE");
		            	sFechas[1] = rs.getString("FECHA");
		            	sFechas[2] = rs.getString("ANIO_ACTUAL");
		            	sFechas[3] = rs.getString("ANIO_ANTERIOR");
		            	sFechas[4] = rs.getString("MES_ACTUAL");  
			        }
			    }
	            onCierraConexion();

	        } catch (SQLException Err){
		        mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + " onCFDI_validaFacturaEgresos()";
		        logger.error(mensajeError);
	        } finally {
	            onCierraConexion();
	        }

	        return sFechas;
	}

    public void onCierraConexion() {
        if (rs != null) {
            try {
            	rs.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar ResultSet");
            }
        }
        if (pstmt != null) {
            try {
            	pstmt.close();
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
        if (cstmt != null) {
            try {
            	cstmt.close();
            } catch (SQLException sqlExc) {
                logger.error("Error al cerrar CallableStatement");
            }
        }
    }
      
    private synchronized String dateFormat2(java.util.Date dFec_Inicial) throws ParseException {
        return simpleDateFormat2.format(dFec_Inicial);
    } 
}