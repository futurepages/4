package org.futurepages.core.formatter;

import org.futurepages.core.locale.LocaleManager;

import java.util.Locale;


/**
 * This is a simple class to format an object.
 *
 */
public abstract class AbstractFormatter<T extends Object> {

	public abstract String format(T value, Locale loc) ;

    /**
     * Formats an output object from this action.
     *
     * @param value The value to format
     * @param locale The locale to use (if needed)
     * @return The value formatted to a String
     */
	public String format(T value, Locale locale, String param) {
		return format(value,locale);
	}

	public final String format(T value) {
		return format(value, LocaleManager.getDefaultLocale());
	}


	public final String format(T value, String param) {
		return format(value, LocaleManager.getDefaultLocale(), param);
	}
}