package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.futurepages.util.BrazilianCalendarUtil;
import org.futurepages.util.BrazilianDateUtil;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata a data retornando o dia da Semana 
 */
public class LiteralDayOfWeekFormatter implements Formatter {

	public String format(Object date, Locale loc) {

		if (date instanceof Date) {
			return BrazilianDateUtil.literalDayOfWeek((Date) date);

		}else if (date instanceof Calendar) {
			return BrazilianCalendarUtil.literalDayOfWeek((Calendar) date);
		}

		return null;
	}
}