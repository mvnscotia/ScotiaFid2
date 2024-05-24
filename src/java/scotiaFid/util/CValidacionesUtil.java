/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.util;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lenovo
 */
public class CValidacionesUtil {

    private static final Logger logger = LogManager.getLogger(CValidacionesUtil.class);

    public static void addMessage(FacesMessage.Severity severity, Map<String, String> validaciones) {
        for (Map.Entry<String, String> validacion : validaciones.entrySet()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, validacion.getKey(), validacion.getValue()));
        }
    }

    public static String realScapeString(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\'");
        text = text.replaceAll("\\n", "\\\\n'");
        text = text.replaceAll("\\r", "\\\\r'");
        text = text.replaceAll("\\t", "\\\\t'");
        text = text.replaceAll("\\n", "\\\\n'");
        text = text.replaceAll("'", "\\\\'");
        return text;
    }

    public static double validarDouble(String valor) {
        double resultado = 0.0;
        try {
            if (esDoubleValido(valor)) {
                resultado = Double.parseDouble(valor);
            }
        } catch (NumberFormatException e) {
            logger.error("El numero proporcionado es invalido");
        }
        return resultado;
    }

    public static boolean esDoubleValido(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
