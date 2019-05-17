package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.util.brazil.BrazilDateUtil;
import org.futurepages.util.brazil.enums.DayOfWeek;

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
			return BrazilCalendarUtil.literalDayOfWeek((Calendar) date);
		}

		return null;
	}

	@Override
	public String format(Object date, Locale locale, String param) {
		if(Is.empty(param) || !param.equals("en")){
			return format(date,locale);
		}
		// in english...
		Calendar cal;
		if (date instanceof Date) {
			cal = CalendarUtil.now();
			cal.setTime((Date) date);
		}else{
			cal = (Calendar) date;
		}
		return The.capitalizedWord(DayOfWeek.getDayByKey(cal.get(Calendar.DAY_OF_WEEK)).name().toLowerCase());
	}

}