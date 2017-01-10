package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 *
 * Parametro quando = true... \n --> chega \\n no html. Serve quando estamos passando JSONs por exemplo com
 * conteúdo que possa existir quebra de linha etc.
 * .
 */
public class JavascriptFormatter extends AbstractFormatter<String> {
    
	@Override
    public String format(String value, Locale loc) {
        return The.javascriptText(value,false);
    }

	@Override
    public String format(String value, Locale locale, String param) {
        return The.javascriptText(value,param!=null && param.equals("true"));
	}
}