package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class UpperCaseFormatter extends AbstractFormatter<String> {
 	
 	public String format(String value, Locale loc) {
            return value.toUpperCase();
 	}
 }
