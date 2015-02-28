package org.futurepages.formatters;

import org.futurepages.util.DateUtil;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata uma data String cou Date ou Calendar em HH:mm
 */
 public class TimeFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
            return DateUtil.getInstance().viewDateTime(value, "HH:mm:ss");
 	}
 }