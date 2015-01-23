package org.futurepages.formatters;

import org.futurepages.util.DateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return DateUtil.literalDateFromDB(value);
 	}
 }