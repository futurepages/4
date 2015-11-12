package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class JavascriptFormatter extends AbstractFormatter<String> {
    
	@Override
    public String format(String value, Locale loc) {
        return The.javascriptText(value);
    }
}