package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SimpleElapsedTimeFormatter extends AbstractFormatter {

	@Override
	public String format(Object dateOrCalendar, Locale loc) {
		Calendar momentoNoPassado;

		if(dateOrCalendar instanceof Date){
			momentoNoPassado = CalendarUtil.dateTimeToCalendar((Date) dateOrCalendar);
		}else{
			momentoNoPassado = (Calendar) dateOrCalendar;
		}

		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		if(CalendarUtil.isSameDay(agora, momentoNoPassado)){
			return CalendarUtil.format(momentoNoPassado, "HH:mm");
		}
		else if(CalendarUtil.isNeighborDays(momentoNoPassado, agora)){
				return "Ontem";
		}else {
			return CalendarUtil.format(momentoNoPassado, "MMM dd");
		}
	}
}