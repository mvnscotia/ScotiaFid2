/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CCierreContable.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//Package
package scotiaFid.dao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
//Imports
import java.io.Serializable; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.FidCtasBean;
import scotiaFid.bean.MonitorChequesBean;
import scotiaFid.bean.ResponseMonitorBean;
import scotiaFid.singleton.DataBaseConexion;

public class CCierreContable implements Serializable {
    private static final Logger logger = LogManager.getLogger(CCierreContable.class);
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * S E R I A L 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	private static final long serialVersionUID = 1L;

	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * O B J E T O S 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Connection conexion;
 
    private PreparedStatement pstmt;
    private ResultSet rs;
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	* A T R I B U T O S   P R I V A D O S   V I S I B L E S
	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;
    private String nombreObjeto; 
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	* G E T T E R S   Y   S E T T E R S
	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * M E T O D O S 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public List<FidCtasBean> onMonitorRecepciones_GetCuentasXAtencion(int usuario) throws SQLException {
		//Objects
		List<FidCtasBean> cuentas = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();

		try {
			//SQL 			  
			stringBuilder.append(" SELECT DISTINCT LPAD(d.PLAZA,3,0) || RIGHT(d.CUENTA,8) AS CUENTA, d.MONEDA AS MONEDA, d.MONEDA_NOMBRE AS MONEDA_NOMBRE, d.CONTRATO AS CONTRATO ");
			stringBuilder.append(" FROM (SELECT b.ADC_DESC, b.ADC_NUM_CONTRATO AS CONTRATO, b.ADC_NUM_MONEDA AS MONEDA, c.MON_NOM_MONEDA AS MONEDA_NOMBRE, b.ADC_PLAZA AS PLAZA, b.ADC_CUENTA AS CUENTA "); 
			stringBuilder.append(" FROM (SELECT CTO_NUM_CONTRATO "); 
			stringBuilder.append(" FROM CONTRATO, ATENCION ");
			stringBuilder.append(" WHERE CTO_NUM_CONTRATO = ATE_NUM_CONTRATO ");
			stringBuilder.append(" AND CTO_ES_EJE = 0 "); 
			stringBuilder.append(" AND CTO_CVE_ST_CONTRAT ='ACTIVO' ");
			stringBuilder.append(" AND ATE_EJEC_ATENCION = ?) a, FID_CTAS b, MONEDAS c "); 
			stringBuilder.append(" WHERE a.CTO_NUM_CONTRATO = b.ADC_NUM_CONTRATO ");
			stringBuilder.append(" AND b.ADC_NUM_MONEDA = c.MON_NUM_PAIS ");
			stringBuilder.append(" AND b.ADC_ESTATUS = 'ACTIVO' ");
			stringBuilder.append(" AND b.ADC_BANCO = 44 "); 
			stringBuilder.append(" AND b.ADC_DESC <> 'CCI' ");
			stringBuilder.append(" AND b.ADC_DESC <> 'CCF_MN' ");
			stringBuilder.append(" AND b.ADC_DESC <> 'CCF_DL') d ");
                        stringBuilder.append(" ORDER BY MONEDA");
			//Call_Operaciones_BD
			conexion = DataBaseConexion.getInstance().getConnection();
			pstmt = conexion.prepareStatement(stringBuilder.toString());
			
			//Set_Values
			pstmt.setInt(1, usuario);

			//Execute_Query
			rs = pstmt.executeQuery();

			//Validate
			if(rs != null) {
				//Set_FidCtasBean
				while(rs.next()) {
					//Object
					FidCtasBean fidCtasBean = new FidCtasBean();

					//Set_Values
					fidCtasBean.setCuenta(rs.getString("CUENTA"));
					fidCtasBean.setMoneda(rs.getInt("MONEDA"));
					fidCtasBean.setContrato(rs.getInt("CONTRATO"));
					fidCtasBean.setDescripcion(rs.getString("MONEDA_NOMBRE"));
					 
					//Add_List
					cuentas.add(fidCtasBean);
				}
			} 
			onCierraConexion();
            
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCierreContable_GetCuentasXAtencion()";
        } finally {
        	onCierraConexion();
        }

		return cuentas;
	}

	public HashMap<String, String> onMonitorRecepciones_GetCatalogoErrores() throws SQLException {
		//Objects
		HashMap<String, String> errores = new HashMap<String, String>();
		StringBuilder stringBuilder = new StringBuilder();

		try {
            		//SQL 
			stringBuilder.append("SELECT * FROM CAT_ERR_WEBSERVICE cew ORDER BY ERR_CODIGO");
  
			//Call_Operaciones_BD
			conexion = DataBaseConexion.getInstance().getConnection();
			pstmt = conexion.prepareStatement(stringBuilder.toString());
			
			//Execute_Query
			rs = pstmt.executeQuery();

			//Validate
			if(rs != null) {
				//Set_FidCtasBean
				while(rs.next()) {

					//Set_Values
					errores.put(rs.getString("ERR_CODIGO"), rs.getString("ERR_DESC_PANTALLA").replace("ó", "�"));
				}
			} 
			onCierraConexion();
			
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCierreContable_GetCatalogoErrores()";
		} finally {
			onCierraConexion();
		}

		return errores;
	}
	
	public void onMonitorRecepciones_GetReport(List<ResponseMonitorBean> responses, MonitorChequesBean monitorChequesBean) throws SQLException {
		
		try { 
				   
           String reporte = System.getProperty("java.io.tmpdir") + File.separator +"Movimientos_Recepcion_".concat(monitorChequesBean.getFecha().replace("/", "_")).concat("_").concat(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioNombre").toString()).concat(".txt"); 
		   PrintWriter printWriter = new PrintWriter(reporte);
		   
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("CONSULTA DE RECEPCIONES DEL DIA");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("FECHA: ");
		   printWriter.write(monitorChequesBean.getFecha());
		   printWriter.write("\n");
		   printWriter.write("\n");
		   
		   printWriter.write("CONTRATO");
		   printWriter.write("\t"); printWriter.write("\t");printWriter.write("\t");
		   printWriter.write("CUENTA");
		   printWriter.write("\t"); printWriter.write("\t");printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("MONEDA");
		   printWriter.write("\t"); printWriter.write("\t"); printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("REFERENCIA");
		   printWriter.write("\t"); printWriter.write("\t"); printWriter.write("\t"); 
		   printWriter.write("DESCRIPCION");
		   printWriter.write("\t"); printWriter.write("\t");printWriter.write("\t");
		   printWriter.write("IMPORTE");
		   printWriter.write("\n"); 
		   
		   printWriter.write("===============");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("===============");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("===============");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("===============");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("===============");
		   printWriter.write("\t"); printWriter.write("\t");
		   printWriter.write("===============");
		   printWriter.write("\n");
		            
		   for(ResponseMonitorBean responseMonitorBean : responses) {
			   printWriter.write(responseMonitorBean.getContrato());
			   printWriter.write("\t"); printWriter.write("\t"); printWriter.write("\t");
			   printWriter.write(responseMonitorBean.getCuenta());
			   printWriter.write("\t");printWriter.write("\t"); printWriter.write("\t");
			   printWriter.write(responseMonitorBean.getMoneda());
			   printWriter.write("\t");printWriter.write("\t");
			   printWriter.write(responseMonitorBean.getReferencia());
			   printWriter.write("\t");printWriter.write("\t"); printWriter.write("\t");
			   printWriter.write(responseMonitorBean.getDescripcion());
			   printWriter.write("\t");printWriter.write("\t"); printWriter.write("\t"); printWriter.write("\t");
			   printWriter.write(responseMonitorBean.getMontoFormat());
			   printWriter.write("\n");
		   }
		   
		   printWriter.close();
		     
        } catch (IOException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onCierreContable_GetReport()";
        } 	
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
    }
}