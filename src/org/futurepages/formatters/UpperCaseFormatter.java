package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class UpperCaseFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return ((String)value).toUpperCase();
 	}
 }
