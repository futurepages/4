package org.futurepages.formatters;

import org.futurepages.util.BrazilianDateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata uma data String como entrada "YYYY-MM-DD"  e retorna no formato formato DD/MM/YYYY
 */
 public class DateFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return BrazilianDateUtil.viewDate(value);
 	}
 }