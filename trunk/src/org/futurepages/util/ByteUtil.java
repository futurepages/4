package org.futurepages.util;

/**
 *
 * @author diogenes
 */
public class ByteUtil {

    public static String bytesInWords(Long bytes) {

        float aux;

        if ((bytes == null) || (bytes < 0)) {

            return "";

        } else if (bytes < 1024) {

            return bytes + (bytes == 1 ? " byte" : " bytes");

        } else if (bytes < 1048576) {

            aux = ((float) bytes / 1024);
            return aux + (aux == 1 ? " KByte" : " KBytes");

        } else if (bytes < 1073741824) {

            aux = ((float) bytes / 1048576);
            return aux + (aux == 1 ? " MByte" : " MBytes");
            
        } else if (bytes < 1099511627776L) {

            aux = ((float) bytes / 1073741824);
            return aux + (aux == 1 ? " GByte" : " GBytes");
        }
        return "";
    }
}
