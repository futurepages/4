package org.futurepages.formatters;

import org.futurepages.util.brazil.DateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formatação de data e hora:
 *  
 * - saída: DD/MM/YYYY HH:MM:SS
 *
 */
 public class DateTimeFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
			return DateUtil.viewDateTime(value);
	}
 }