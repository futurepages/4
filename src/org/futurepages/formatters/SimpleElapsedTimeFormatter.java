package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.futurepages.util.BrazilianCalendarUtil;
import org.futurepages.util.BrazilianDateUtil;
import org.futurepages.core.formatter.Formatter;

public class SimpleElapsedTimeFormatter implements Formatter {

	@Override
	public String format(Object dateOrCalendar, Locale loc) {
		Calendar momentoNoPassado;

		if(dateOrCalendar instanceof Date){
			momentoNoPassado = BrazilianDateUtil.dateTimeToCalendar((Date) dateOrCalendar);
		}else{
			momentoNoPassado = (Calendar) dateOrCalendar;
		}

		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		if(BrazilianCalendarUtil.isSameDay(agora, momentoNoPassado)){
			return BrazilianDateUtil.format(momentoNoPassado, "HH:mm");
		}
		else if(BrazilianCalendarUtil.isNeighborDays(momentoNoPassado, agora)){
				return "Ontem";
		}else {
			return BrazilianDateUtil.format(momentoNoPassado, "MMM dd");
		}
	}
}