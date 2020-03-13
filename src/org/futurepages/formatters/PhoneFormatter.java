package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.PhoneUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilPhoneUtil;

import java.util.Locale;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class PhoneFormatter extends AbstractFormatter<String> {
    
    public String format(String value, Locale locale) {
        return BrazilPhoneUtil.normalize(value, PhoneUtil.NumberFormat.INTERNATIONAL);
    }
}