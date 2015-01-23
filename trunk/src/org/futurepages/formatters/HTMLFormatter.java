package org.futurepages.formatters;

import org.futurepages.util.The;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class HTMLFormatter implements Formatter {
    
    public String format(Object value, Locale loc) {
        return The.htmlSimpleValue((String) value);
    }
}