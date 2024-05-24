/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : ScotiaBank
 * ARCHIVO     : SArchivoDescarga.java
 * TIPO        : Servlet
 * PAQUETE     : scotiaFid.servlet.archivo
 * CREADO      : 20200624
 * MODIFICADO  : 20200624
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.util.Constantes;
import scotiaFid.util.Normalizacion;

@WebServlet(name = "SArchivoDescarga", urlPatterns = {"/SArchivoDescarga"})
public class SArchivoDescarga extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SArchivoDescarga.class);
    //Atributos privados
    private static final String APLICATIONS = "APPLICATION/OCTET-STREAM;text/html;charset=ISO-8859-1";
    private static final String COTENTDISP = "Content-Disposition";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        onSarchivo_descarga_process(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (Throwable e) {
            logger.error("Error en la servlet:", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Ocurrió un error en la servlet.");
            } catch (IOException io) {
                logger.error("Error en la servlet:", io);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (Throwable e) {
            logger.error("Error en la servlet:", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Ocurrió un error en la servlet.");
            } catch (IOException io) {
                logger.error("Error en la servlet:", io);
            }
        }
    }

    private synchronized void onSarchivo_descarga_process(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Integer archivoTamaño;
        String archivoNombre;
        String archivoUbicacion;
        File archivo;
        FileInputStream fis = null;
        try {
            
            response.setContentType(APLICATIONS);
            out = response.getWriter();
            archivoTamaño = 0;
            archivoUbicacion = Constantes.RUTA_TEMP;
            
            if (archivoUbicacion != null) {
                archivoUbicacion = archivoUbicacion.concat("/");

                archivoNombre = Normalizacion.parse(request.getQueryString());
                
                if (onValidaInformacionArchivo(archivoNombre, "^[a-zA-Z0-9_]{1,}.txt{1,}$") || onValidaInformacionArchivo(archivoNombre, "^[a-zA-Z0-9_]{1,}.pdf{1,}$")) {
                    
                    String rutaNormalizada = Normalizacion.parse(archivoUbicacion.concat(archivoNombre));
                    
                    archivo = new File(rutaNormalizada);
                    response.setHeader(COTENTDISP, "attachment; filename=\"".concat(archivoNombre).concat("\""));

                    fis = new FileInputStream(archivo);
                    while ((archivoTamaño = fis.read()) != -1) {
                        out.write(archivoTamaño);
                    }
                }
            }
        } catch (IOException IOErr) {
            logger.error(IOErr.getMessage() + "onSarchivo_descarga_process()");
        } finally {
            if (out != null) {
                out.close();
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException IOErr) {
                    logger.error("Error al cerrar componete File Input String");
                }
            }

        }
    }

    private Boolean onValidaInformacionArchivo(String campoValor, String campoPatron) {
        Pattern patron = Pattern.compile(campoPatron);
        Matcher matcher = patron.matcher(campoValor);
        return matcher.matches();
    }

}
