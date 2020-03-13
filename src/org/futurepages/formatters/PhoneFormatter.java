package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;

import java.util.Locale;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class PhoneFormatter extends AbstractFormatter<String> {
    
    public String format(String value, Locale locale) {
        // +5511957050704  --> +55 (11) 9 5705-0704
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < value.length();i++){
            if(i==3){
               sb.append(" (");
            }
            sb.append(value.charAt(i));
            if(i==4){
                sb.append(") ");
            }
            if(i==5){
                sb.append(" ");
            }
            if(i==9){
                sb.append("-");
            }
        }
        return sb.toString();
    }
}