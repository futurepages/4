package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.futurepages.util.brazil.CalendarUtil;
import org.futurepages.util.brazil.DateUtil;
import org.futurepages.core.formatter.AbstractFormatter;

/**
 * Formata a data retornando o dia da Semana 
 */
public class LiteralDayOfWeekFormatter extends AbstractFormatter {

	public String format(Object date, Locale loc) {

		if (date instanceof Date) {
			return DateUtil.literalDayOfWeek((Date) date);

		}else if (date instanceof Calendar) {
			return CalendarUtil.literalDayOfWeek((Calendar) date);
		}

		return null;
	}
}