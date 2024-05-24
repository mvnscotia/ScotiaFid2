/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : The_Bank_of_Nova_Scotia
 * ARCHIVO     : CMonitorCheques.java
 * TIPO        : Class
 * PAQUETE     : scotiaFid.dao
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//Package
package scotiaFid.dao;

//Imports
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.DepositBean;
import scotiaFid.bean.FidCtasBean;
import scotiaFid.singleton.DataBaseConexion;


//Class
public class CMonitorMovimientos implements Serializable {
    private static final Logger logger = LogManager.getLogger(CMonitorMovimientos.class);
   
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    * S E R I A L
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final long serialVersionUID = 1L;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	* O B J E T O S
	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Connection conexion;
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * C O N S T R U C T O R
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CMonitorMovimientos() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CMonitorMovimientos."; 
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 * M E T O D O S
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public List<FidCtasBean> onMonitorMovimientos_GetCuentasXContrato(int contrato, int moneda) throws SQLException {
        //Objects
        List<FidCtasBean> movements = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //SQL 
                stringBuilder.append("SELECT RIGHT(ADC_CUENTA,8) AS ADC_CUENTA, ADC_CLABE, ADC_NUM_MONEDA, ADC_PLAZA FROM FID_CTAS WHERE ADC_NUM_CONTRATO = ? AND ADC_NUM_MONEDA = ? AND ADC_DESC NOT IN('CCI') AND ADC_ESTATUS = 'ACTIVO' AND ADC_BANCO = 44 ORDER BY ADC_SECUENCIAL ASC");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setInt(1, contrato);
            preparedStatement.setInt(2, moneda);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_FidCtasBean
                while (resultSet.next()) {
                    //Object
                    FidCtasBean fidCtasBean = new FidCtasBean();

                    //Set_Values
                    fidCtasBean.setCuenta(resultSet.getString("ADC_CUENTA"));
                    fidCtasBean.setClabe(resultSet.getString("ADC_CLABE"));
                    fidCtasBean.setMoneda(resultSet.getInt("ADC_NUM_MONEDA"));
                    fidCtasBean.setPlaza(resultSet.getInt("ADC_PLAZA"));

                    //Add_List
                    movements.add(fidCtasBean);
                }
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción: " + Err.getMessage() + nombreObjeto + "onMonitorMovimientos_GetCuentasXContrato()";
        } finally {
            if(preparedStatement != null)  {
               preparedStatement.close();
            }
             if(resultSet != null)  {
               resultSet.close();
            }
            if(conexion != null) {
               conexion.close();
            }
        }

        return movements;
    }

    public List<DepositBean> onMonitorMovimientos_GetDepositos(int contrato, int moneda, Date fecsistema) throws SQLException {
        //Objects
        List<DepositBean> deposits = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(); 
        PreparedStatement preparedStatement = null;
         ResultSet resultSet = null; 
        Calendar calendario;

        try {
            //SQL 
            stringBuilder.append("SELECT COUNT(DPO_IMP_DEPOSITO) AS OCURRENCIAS, DPO_NUM_CONTRATO AS CONTRATO, \n"
                              + " DPO_IMP_DEPOSITO AS IMPORTE, DPO_CONCEPTO AS PLAZADERECEPCION \n"
                              + "FROM DEPOSIT  \n "
                              + "WHERE DPO_NUM_CONTRATO = ?  \n"
                              + "AND DPO_ANO_RCP = ?  \n "
                              + "AND  DPO_MES_RCP = ? \n"
                              + "AND  DPO_DIA_RCP = ?  \n"
                              + "AND (DPO_CVE_ST_DEPOSI = 'CONTABILIZADO' OR DPO_CVE_ST_DEPOSI = 'AUTOMATICO') \n"
                              + " AND  DPO_NUM_MONEDA = ? AND DPO_CVE_TIPO_DEP = ? \n"
                              + "GROUP BY DPO_IMP_DEPOSITO, DPO_CONCEPTO, DPO_NUM_CONTRATO ");

            //Call_Conexion
            conexion = DataBaseConexion.getInstance().getConnection();

                    calendario = Calendar.getInstance();
                    calendario.setTime(new java.util.Date(fecsistema.getTime()));
                    Integer dia = calendario.get((Calendar.DAY_OF_MONTH));
                    Integer mes = calendario.get((Calendar.MONTH)) + 1 ;
                    Integer anio = calendario.get((Calendar.YEAR));
            preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            //Set_Values
            preparedStatement.setInt(1, contrato);
            preparedStatement.setInt(2, anio);
            preparedStatement.setInt(3, mes);            
            preparedStatement.setInt(4, dia);
            preparedStatement.setInt(5, moneda);
            preparedStatement.setInt(6, 1);

            //Execute_Query
            resultSet = preparedStatement.executeQuery();

            //Validate
            if (resultSet != null) {
                //Set_FidCtasBean
                while (resultSet.next()) {
                    //Object
                    DepositBean depositBean = new DepositBean();

                    //Set_Values
                    depositBean.setOcurrencias(resultSet.getInt("OCURRENCIAS"));
                    depositBean.setContrato(resultSet.getInt("CONTRATO"));
                    depositBean.setImporte(resultSet.getDouble("IMPORTE"));
                    depositBean.setConcepto(resultSet.getString("PLAZADERECEPCION"));

                    //Add_List
                    deposits.add(depositBean);
                }
            }
        } catch (SQLException Err) {
            logger.error(Err.getMessage());
        } finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (resultSet != null)
					resultSet.close(); 
				if (conexion != null)
					conexion.close();
			} catch (SQLException e) {
				logger.error("Function :: Error al cerrar conexion." + e.getMessage());
			}
        }

        return deposits;
    }
}
