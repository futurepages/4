package org.futurepages.formatters;

import java.text.DecimalFormat;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;

/**
 *
 * @author leandro
 */
public class FloatFormatter extends AbstractFormatter<String> {

	@Override
    public String format(String value, Locale locale) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }
	
	@Override
    public String format(String value, Locale locale, String param) {
        DecimalFormat df = new DecimalFormat(param);
        return df.format(value);		
	}
}