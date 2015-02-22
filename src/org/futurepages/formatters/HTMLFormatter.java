package org.futurepages.formatters;

import org.futurepages.util.The;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class HTMLFormatter extends AbstractFormatter<String> {
    
    public String format(String value, Locale loc) {
        return The.htmlSimpleValue(value);
    }
}