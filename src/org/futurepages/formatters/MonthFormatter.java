package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class MonthFormatter implements Formatter<Integer> {
 	
	@Override
 	public String format(Integer value, Locale loc) {
            return MonthEnum.get((int)value);
 	}
 }