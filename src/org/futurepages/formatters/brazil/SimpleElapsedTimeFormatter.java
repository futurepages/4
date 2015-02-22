package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.futurepages.util.brazil.CalendarUtil;
import org.futurepages.util.brazil.DateUtil;
import org.futurepages.core.formatter.AbstractFormatter;

public class SimpleElapsedTimeFormatter extends AbstractFormatter {

	@Override
	public String format(Object dateOrCalendar, Locale loc) {
		Calendar momentoNoPassado;

		if(dateOrCalendar instanceof Date){
			momentoNoPassado = DateUtil.dateTimeToCalendar((Date) dateOrCalendar);
		}else{
			momentoNoPassado = (Calendar) dateOrCalendar;
		}

		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		if(CalendarUtil.isSameDay(agora, momentoNoPassado)){
			return DateUtil.format(momentoNoPassado, "HH:mm");
		}
		else if(CalendarUtil.isNeighborDays(momentoNoPassado, agora)){
				return "Ontem";
		}else {
			return DateUtil.format(momentoNoPassado, "MMM dd");
		}
	}
}