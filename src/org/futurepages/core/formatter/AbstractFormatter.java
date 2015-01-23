package org.futurepages.core.formatter;

import java.util.Locale;

/**
 *
 * @author diogenes
 */
public abstract class AbstractFormatter<T extends Object> implements Formatter<T>{

	public String format(T value, Locale locale, String param) {
		return (value!=null)?value.toString():null;
	}

	public String format(T value, Locale loc) {
		return (value!=null)?value.toString():null;
	}
}
