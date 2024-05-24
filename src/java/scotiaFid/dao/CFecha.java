package scotiaFid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import javax.faces.context.FacesContext;
import scotiaFid.bean.FechaBean;
import scotiaFid.singleton.DataBaseConexion;

public class CFecha {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	    * A T R I B U T O S   P R I V A D O S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Boolean valorRetorno;
    private String sqlComando;
    private String nombreObjeto;
   
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	    * A T R I B U T O S   P R I V A D O S   V I S I B L E S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private String mensajeError;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	    * G E T T E R S   Y   S E T T E R S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public String getMensajeError() {
        return mensajeError;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	    * C O N S T R U C T O R
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public CFecha() {
        mensajeError = "Error En Tiempo de Ejecución.\n";
        nombreObjeto = "\nFuente: scotiafid.dao.CFecha.";
        valorRetorno = Boolean.FALSE;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	    * M E T O D O S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public FechaBean onFecha_ObtenFechaSistema() throws SQLException {
        FechaBean f = new FechaBean();
        try {
            //Obtenemos la fecha del sistema  
            sqlComando = "SELECT fco_ano_dia, fco_mes_dia, fco_dia_dia, \n"
                    + "       To_Date(To_Char(fco_dia_dia)||'/'||To_Char(fco_mes_dia)||'/'||To_Char(fco_ano_dia),'DD/MM/YYYY') fechaSistema \n"
                    + "FROM   SAF.FecCont";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                f.setFechaActAño(rs.getInt("fco_ano_dia"));
                f.setFechaActMes(rs.getInt("fco_mes_dia"));
                f.setFechaActDia(rs.getInt("fco_dia_dia"));
                f.setFechaAct(rs.getDate("fechaSistema"));

                //Calculamos a�o anterior, mes anterior
                if (f.getFechaActMes() > 1) {
                    f.setFechaMSAAño(f.getFechaActAño());
                    f.setFechaMSAMes(f.getFechaActMes() - 1);
                } else {
                    f.setFechaMSAAño(f.getFechaActAño() - 1);
                    f.setFechaMSAMes(12);
                }
            }
            conexion.close();
            rs.close();
            pstmt.close();

            //Obtenemos el primer dia del mes actual
            sqlComando = "SELECT Date(To_Char(ctl_ano_control)||'-'||To_Char(ctl_mes_control)||'-'||To_Char(ctl_dia_control)) FechaIniMes \n"
                    + "FROM   SAF.Controla        \n"
                    + "WHERE  ctl_ano_control = ? \n"
                    + "AND    ctl_mes_control = ? \n"
                    + "ORDER  BY 1 ASC";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, f.getFechaActAño());
            pstmt.setInt(2, f.getFechaActMes());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                f.setFechaActIni(rs.getDate("FechaIniMes"));
            }
            
            conexion.close();
            rs.close();
            pstmt.close();

            //Verificamos que este el registro en controla
            sqlComando = "SELECT Nvl(Count(*),0) Total \n"
                    + "FROM   SAF.Controla          \n"
                    + "WHERE  ctl_Ano_control = ?   \n"
                    + "AND    ctl_mes_control = ?   \n"
                    + "AND    ctl_dia_Control = ? ";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, f.getFechaActAño());
            pstmt.setInt(2, f.getFechaActMes());
            pstmt.setInt(3, f.getFechaActDia());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("Total") > 0) {
                    f.setFechaCveExisteControla(Boolean.TRUE);
                } else {
                    f.setFechaCveExisteControla(Boolean.FALSE);
                }
            }
            
            conexion.close();
            rs.close();
            pstmt.close();

            //Obtenemos la fecha del mes anterior
            sqlComando = "SELECT To_Date(To_Char(ctl_dia_control)||'/'||To_Char(ctl_mes_control)||'/'||To_Char(ctl_ano_control),'DD/MM/YYYY') fechaMSA,\n"
                    + "       ctl_dia_control     \n"
                    + "FROM   SAF.Controla        \n"
                    + "WHERE  ctl_mes_control = ? \n"
                    + "AND    ctl_ano_control = ? \n"
                    + "ORDER  BY ctl_dia_control DESC";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, f.getFechaMSAMes());
            pstmt.setInt(2, f.getFechaMSAAño());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                f.setFechaMSADia(rs.getInt("ctl_dia_control"));
                f.setFechaMSA(rs.getDate("fechaMSA"));
            }
            
            conexion.close();
            rs.close();
            pstmt.close();

            //Verificamos si el mes anterior est� abierto
            sqlComando = "SELECT ccc_mes_abierto  \n"
                    + "FROM   SAF.Ctl_Mesc     \n"
                    + "WHERE  ccc_ano = ?      \n"
                    + "AND    ccc_mes = ? ";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, f.getFechaMSAAño());
            pstmt.setInt(2, f.getFechaMSAMes());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                f.setFechaMSAAbierto(rs.getInt("ccc_mes_abierto"));
            }
            
            conexion.close();
            rs.close();
            pstmt.close();

            //Obtenemos la fecha del ultimo mes cerrado
            sqlComando = "SELECT ccc_ano, ccc_mes FROM SAF.Ctl_Mesc WHERE ccc_mes_abierto = ? ORDER BY 1 DESC, 2 DESC";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlComando);
            pstmt.setInt(1, 0);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                f.setFechaHSTAño(rs.getInt("ccc_ano"));
                f.setFechaHSTMes(rs.getInt("ccc_mes"));
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "onFecha_ObtenFechaSistema()";
        } finally {
            onCierraConexion();
        }
        return f;
    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //VJN - INICIO - Metodos de Fechas
    //Valida si la fecha es un  dia Habil 
    // Valida que no sea fin de semana
    // Valida que no sea un dia feriado
    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    public int onFecha_DiaHabil(Date FecVal) throws SQLException {

        // Objects
        String QuerySql = "";
        int iDia = 0;
        int iResultado = 0;
        
        try {
            //Valida que no sea fin de semana
            if (FecVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek() != DayOfWeek.SATURDAY
                    && FecVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
                //Valida que no sea dia Feriado
                // SQL
                QuerySql = "SELECT FER_FEC_DIA FROM FERIADOS  "
                        + "WHERE FER_NUM_PAIS = 1 " //Pais Nacional (MEX)
                        + "AND FER_FEC_DIA= ?  "
                        + "AND FER_FEC_MES= ? ";

                // Write_Console
                //System.out.println("SQL: ".concat(QuerySql));
                // Call_Operaciones_BD
                conexion = DataBaseConexion.getInstance().getConnection();
                pstmt = conexion.prepareStatement(QuerySql);

                // Parameters
                pstmt.setInt(1, FecVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth());
                pstmt.setInt(2, FecVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue());

                // Execute_Query
                rs = pstmt.executeQuery();

                // Get_Data
                if (rs != null) {
                    while (rs.next()) {
                        // Add_List
                        iDia = Integer.parseInt(rs.getString("FER_FEC_DIA"));

                        if (iDia != 0) {
                            //Dia feriado
                            //System.out.println("Dia feriado: " + resultSet.getString("FER_FEC_DIA"));
                            iResultado = 1;
                        }
                    }
                }
            } else {
                //System.out.println("Dia en fin de semana");
                iResultado = 1;
            }
        } catch (SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "onFecha_DiaHabil()";
            iResultado = 1;
        } finally {
            onCierraConexion();
        }
        return iResultado;
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Valida que el modulo de Tesoreria, permita fecha Valor
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    public boolean onFecha_FecValTesoreria() throws SQLException {

        int iFecTes = 0, iCtlMes = 0;
        boolean bValidacion = false;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Valida que permita Fecha Valor Tesoreria
            // SQL
            stringBuilder.append("SELECT ccc_mes_abierto, FVL_VALOR FROM SAF.FECHAVALOR, SAF.Ctl_Mesc WHERE FVL_ELEMENTO ='TESORERIA' ");
            stringBuilder.append("AND ccc_mes = ? AND ccc_ano = ? ");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            PreparedStatement preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAMes").toString()));
            preparedStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAño").toString()));

            // Execute_Query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Add_List
                    iFecTes = Integer.parseInt(resultSet.getString("FVL_VALOR"));
                    iCtlMes = Integer.parseInt(resultSet.getString("ccc_mes_abierto"));

                    if (iFecTes == 1 && iCtlMes == 1) {
                        bValidacion = true;
                    }
                }
            }

        } catch (SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "onFecha_FecValTesoreria()";
        } finally {
            onCierraConexion();
        }

        return bValidacion;
    }
    
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Valida que el modulo de Tesoreria, no permita fecha Valor Entradas y Salidas
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    public boolean onFecha_FecValEntradaSalidaTesoreria() throws SQLException {

        int iFecTes = 0, iCtlMes = 0;
        boolean bValidacion = false;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Valida que permita Fecha Valor Tesoreria
            // SQL
            stringBuilder.append("SELECT ccc_mes_abierto, FVL_VALOR FROM SAF.FECHAVALOR, SAF.Ctl_Mesc WHERE FVL_ELEMENTO ='TESORERIA' ");
            stringBuilder.append("AND ccc_mes = ? AND ccc_ano = ? AND cc_mes_abierto = ?");

            // Call_Operaciones_BD
            conexion = DataBaseConexion.getInstance().getConnection();
            PreparedStatement preparedStatement = conexion.prepareStatement(stringBuilder.toString());

            preparedStatement.setInt(1, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAMes").toString()));
            preparedStatement.setInt(2, Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaMSAAño").toString()));
            preparedStatement.setInt(3, 2);
            // Execute_Query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Get_Data
            if (resultSet != null) {
                while (resultSet.next()) {
                    // Add_List
                    iFecTes = Integer.parseInt(resultSet.getString("FVL_VALOR"));
                    iCtlMes = Integer.parseInt(resultSet.getString("ccc_mes_abierto"));

                    if (iFecTes == 1 && iCtlMes == 1) {
                        bValidacion = true;
                    }
                }
            }

        } catch (SQLException exception) {
            mensajeError += "Descripción: " + exception.getMessage() + nombreObjeto + "onFecha_FecValTesoreria()";
        } finally {
            onCierraConexion();
        }

        return bValidacion;
    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Aumenta dias, meses o anios a la fecha que se ingresa
    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    public Date Proyecta_Tiempo(Date iFecha, int iTiempo, String sTipo) {

        //logger.debug("Ingresa a Proyecta_Tiempo()");
        Date fechaProyectada = null;
        Calendar calendar = Calendar.getInstance();

        if (iFecha != null) {
            calendar.setTime(iFecha);

            switch (sTipo) {
                case "Dias":
                    calendar.add(Calendar.DAY_OF_YEAR, iTiempo);
                    break;
                case "Mes":
                    calendar.add(Calendar.MONTH, iTiempo);
                    break;
                case "Anio":
                    calendar.add(Calendar.YEAR, iTiempo);
                    break;
            }

            fechaProyectada = calendar.getTime();

        }
        return fechaProyectada;
    }
    //VJN - FIN - Metodos_de_Fechas

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	    * F U N C I O N E S   P R I V A D A S
	    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onCierraConexion() {
        try{
            if(rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException Err) {
            mensajeError += "Descripción:" + Err.getMessage() + nombreObjeto + "onSeguridad_onCierraConexion()";
        }
    }
}
