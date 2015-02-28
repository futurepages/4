package org.futurepages.formatters.brazil;

import org.futurepages.util.brazil.BrazilDateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formatação de data e hora:
 *  
 * - saída: DD/MM/YYYY HH:MM:SS
 *
 */
 public class DateTimeFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
			return BrazilDateUtil.viewDateTime(value);
	}
 }