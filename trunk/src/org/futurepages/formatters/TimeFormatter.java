package org.futurepages.formatters;

import org.futurepages.util.BrazilianDateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata uma data String cou Date ou Calendar em HH:mm
 */
 public class TimeFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return BrazilianDateUtil.viewDateTime(value, "HH:mm:ss");
 	}
 }