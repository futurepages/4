package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 *
 * @author leandro
 */
public class FloatFormatter extends AbstractFormatter{

	@Override
    public String format(Object value, Locale locale) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }

	@Override
    public String format(Object value, Locale locale, String param) {
        DecimalFormat df = new DecimalFormat(param);
        return df.format(value);
	}
}