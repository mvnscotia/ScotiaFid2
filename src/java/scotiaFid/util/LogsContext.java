/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import scotiaFid.singleton.DataBaseConexion;

public class LogsContext {

    private static final Logger logger = LogManager.getLogger(LogsContext.class);
    private static HttpServletRequest peticion;
    private static HttpSession sesion;
    private static String GlobalIp;
    private static final String WIN_USER = "WIN_USER";
    
    static final String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
    static SecureRandom secureRnd = new SecureRandom();

    public static void FormatoNormativo() {
        String isHost = null;
        Boolean isHostE = false;
        try {
             peticion = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sesion = peticion.getSession(Boolean.FALSE);
            GlobalIp = getIpAddress();
        } catch (AbstractMethodError | NullPointerException ex) {
//            logger.error("Error al complementar el formato normativo");           
            isHostE = true;
        }

        try {
            isHost = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hostName");
        } catch (AbstractMethodError | NullPointerException e) {
            isHost = null;
            isHostE = true;
        }

        if (!isHostE && isHost == null) {
            try {
                if (!isHostE && isHost ==null) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("hostName", InetAddress.getLocalHost().getCanonicalHostName());
                    isHost = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hostName");
                }
            } catch (UnknownHostException | NullPointerException e) {
                logger.error("Error al consultar Host Name");
            }
        }
        try {

            String accountName = "";
            Object winUserAttribute = sesion.getAttribute(WIN_USER);
            if (winUserAttribute != null) {
                 accountName = limpiarAccountName(winUserAttribute.toString());
                ThreadContext.put("accountName", accountName);
            } 
            String ipAddressServer = "";
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                 ipAddressServer = localHost.getHostAddress();
            } catch (UnknownHostException e) {
              logger.error("Error al obtener la direccion IP del servidor");
            }

            if (isHost != null && getDominios(isHost)) {
                if (!GlobalIp.isEmpty()) {
                    ThreadContext.put("uuid", randomString());
                    ThreadContext.put("accountName", accountName);
                    ThreadContext.put("addressIP", peticion.getRemoteAddr());
                    ThreadContext.put("port", String.valueOf(peticion.getServerPort()));
                    ThreadContext.put("hostName", InetAddress.getLocalHost().getHostName());
                    ThreadContext.put("ipAddressServer", ipAddressServer);
                    ThreadContext.put("sid", sesion.getId());
                }
            } else {
                if (isHost != null) {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    sesion = (HttpSession) ctx.getExternalContext().getSession(Boolean.FALSE);
                    sesion.invalidate();
                    ctx.getExternalContext().redirect("/scotiaFid/vistaExpiro.html");
                } else {
                    if (!GlobalIp.isEmpty()) {
                        ThreadContext.put("uuid", randomString());
                        ThreadContext.put("accountName", accountName);
                        ThreadContext.put("addressIP", peticion.getRemoteAddr());
                        ThreadContext.put("port", String.valueOf(peticion.getServerPort()));
                        ThreadContext.put("hostName", InetAddress.getLocalHost().getHostName());
                        ThreadContext.put("ipAddressServer", ipAddressServer);
                        ThreadContext.put("sid", sesion.getId());
                    }
                }
            }

        } catch (UnknownHostException | NullPointerException ex) {
            logger.error("Error al consultar datos para formato normativo");
        } catch (IOException ex) {
            logger.error("Error al direaccionar Expiro");
        }
    }

    public static Boolean getDominios(String dominioEntrada) {
        Boolean existe = false;
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlDominios;
        try {
            sqlDominios = "select CVE_NUM_SEC_CLAVE,CVE_DESC_CLAVE from SAF.CLAVES \n"
                    + " WHERE CVE_NUM_CLAVE = 761 \n "
                    + "order BY CVE_DESC_CLAVE";
            conexion = DataBaseConexion.getInstance().getConnection();
            pstmt = conexion.prepareStatement(sqlDominios);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String dominio = new String();
                dominio = rs.getString("CVE_DESC_CLAVE");
                if (dominio.toUpperCase(Locale.ENGLISH).equals(dominioEntrada.toUpperCase(Locale.ENGLISH))) {
                    existe = true;
                }
            }
            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException Err) {

            logger.error("Descripción: " + "getDominios()");

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar ResultSet.", e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar PreparetStatement.", e.getMessage());
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    logger.error("Function ::getDominios():: Error al cerrar Connection.", e.getMessage());
                }
            }
        }
        return existe;
    }

    public static String randomString() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(SOURCE.charAt(secureRnd.nextInt(SOURCE.length())));
        }
        return sb.toString();
    }

    public static String getIpAddress() {
        String strIP = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        strIP = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("Error al obtener la IP");
        }
        return strIP;
    }
        private static String limpiarAccountName(String accountName) {
            return accountName.replaceAll("[^a-zA-Z0-9_-]", "");
        }

}
