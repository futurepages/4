package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;

import java.util.Locale;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class HTMLScriptFormatter extends AbstractFormatter<String> {
    
    public String format(String value, Locale locale) {
        return The.htmlSimpleValue(The.javascriptText(value,true),null);
    }
}