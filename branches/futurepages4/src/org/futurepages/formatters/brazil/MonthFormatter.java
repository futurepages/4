package org.futurepages.formatters.brazil;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class MonthFormatter extends AbstractFormatter<Integer> {
 	
	@Override
 	public String format(Integer value, Locale loc) {
            return MonthEnum.get((int)value);
 	}
 }