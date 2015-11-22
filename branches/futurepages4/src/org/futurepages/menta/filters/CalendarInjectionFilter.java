package org.futurepages.menta.filters;

import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;
import org.futurepages.util.brazil.enums.DateFormatEnum;

import java.util.Calendar;
import java.util.Date;

public class CalendarInjectionFilter implements Filter {

	private String keyToInject;

	public CalendarInjectionFilter(String keyToInject) {
		this.keyToInject = keyToInject;
	}
//	public CalendarInjectionFilter(String keyToInject) {
//		this(keyToInject);
//		this.html5Input = html5Input;
//	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		try {
			String day    = input.getStringValue(keyToInject + "_day");
			String month  = input.getStringValue(keyToInject + "_month");
			String year   = input.getStringValue(keyToInject + "_year");
			String hour   = input.getStringValue(keyToInject + "_hour");
			String minute = input.getStringValue(keyToInject + "_minute");

			int dayInt = Integer.parseInt(day);
			int monthInt = Integer.parseInt(month);
			int yearInt = Integer.parseInt(month);
			int hourInt = Integer.parseInt(hour);
			int minuteInt = Integer.parseInt(minute);
			if(dayInt>31 || monthInt > 12 || yearInt > 2999 || hourInt > 23 || minuteInt > 59
				||
			   dayInt<0 || monthInt<0 || yearInt<0 || hourInt<0 || minuteInt<0){
				input.setValue(keyToInject, null);
			}

			input.setValue(keyToInject, CalendarUtil.dbDateTimeToCalendar(The.concat(year , "-",The.strWithLeftZeros(month,2),"-", The.strWithLeftZeros(day,2), " ", The.strWithLeftZeros(hour,2),":",The.strWithLeftZeros(minute,2),":00")));
		} catch (Exception ex) {
			input.setValue(keyToInject, null);
		}
		return chain.invoke();
	}

	@Override
	public void destroy() {
	}
}