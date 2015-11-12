package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.brazil.BrazilDateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Formata a data retornando o dia da Semana 
 */
public class LiteralDayOfWeekFormatter extends AbstractFormatter {

	public String format(Object date, Locale loc) {

		if (date instanceof Date) {
			return BrazilDateUtil.literalDayOfWeek((Date) date);

		}else if (date instanceof Calendar) {
			return CalendarUtil.literalDayOfWeek((Calendar) date);
		}

		return null;
	}
}