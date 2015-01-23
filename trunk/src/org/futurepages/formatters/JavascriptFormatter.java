package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.The;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class JavascriptFormatter implements Formatter {
    
	@Override
    public String format(Object value, Locale loc) {
        return The.javascriptText(String.valueOf(value));
    }
}