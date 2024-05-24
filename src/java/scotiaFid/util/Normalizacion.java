package scotiaFid.util;

/**
 *
 * @author s2414379
 */
public class Normalizacion {

    public static String parse(final String input) {

        final StringBuilder output = new StringBuilder();
        
        if (input != null) {
            for (int i = 0; i < input.length(); ++i) {
                output.append(Normalizacion.parse(input.charAt(i)));
            }
        }

        return output.toString();
    }

    // Aqui se agrean todas las validaciones que se necesitan de acuerdo al rango de ASCII
    public static char parse(final char input) {
        
        final int digit = (int) input;
        char output = '\0';

        // From 0 to 9 and :
        for (int i = 48; i <= 58; ++i) {
            if (digit == i) {
                output = (char) i;
            }
        }
        // @ and From A to Z
        for (int i = 64; i <= 90; ++i) {
            if (digit == i) {
                output = (char) i;
            }
        }
        // From a to z
        for (int i = 97; i <= 122; ++i) {
            if (digit == i) {
                output = (char) i;
            }
        }
        // - . /
        for (int i = 45; i <= 47; ++i) {
            if (digit == i) {
                output = (char) i;
            }
        }
        // \
        if (digit == 92) {
            output = (char) 92;
        }
        // _
        if (digit == 95) {
            output = (char) 95;
        }
        // space
        if (digit == 32) {
            output = (char) 32;
        }
        return output;
    }

}

