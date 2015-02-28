package org.futurepages.formatters.brazil;

import org.futurepages.util.brazil.BrazilDateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata uma data String como entrada "YYYY-MM-DD"  e retorna no formato formato DD/MM/YYYY
 */
 public class DateFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
            return BrazilDateUtil.viewDate(value);
 	}
 }