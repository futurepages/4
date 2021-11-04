package org.futurepages.menta.filters;

import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.The;

import java.util.Calendar;

public class CalendarInjectionFilter implements Filter {

	private String keyToInject;
	private boolean dateTime;

	public CalendarInjectionFilter(String keyToInject) {
		this(keyToInject,true);
	}
	public CalendarInjectionFilter(String keyToInject,boolean dateTime) {
		this.keyToInject = keyToInject;
		this.dateTime = dateTime;
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
			int yearInt = Integer.parseInt(year);
			int hourInt = dateTime? Integer.parseInt(hour):0;
			int minuteInt = dateTime? Integer.parseInt(minute):0;

			Calendar cal = CalendarUtil.now();
			cal.set(Calendar.YEAR, yearInt);
			boolean anoBissexto = CalendarUtil.isLeapYear(cal);
			if(dayInt>31 || monthInt > 12 || yearInt > 2999 || hourInt > 23 || minuteInt > 59
				||
			   dayInt<0 || monthInt<0 || yearInt<0 || hourInt<0 || minuteInt<0){
				input.setValue(keyToInject, null);
			}else
			if(dayInt>30 && (monthInt==2 || monthInt==4 || monthInt == 6 || monthInt == 9 || monthInt == 11 )){
				input.setValue(keyToInject, null);
			}else
			if(dayInt>29 && monthInt==2){
				input.setValue(keyToInject, null);
			}else if(dayInt>28 && monthInt == 2 && !anoBissexto ){
				input.setValue(keyToInject, null);
			}else{
				if(dateTime){
					input.setValue(keyToInject, CalendarUtil.dbDateTimeToCalendar(The.concat(year , "-",The.strWithLeftZeros(month,2),"-", The.strWithLeftZeros(day,2), " ", The.strWithLeftZeros(hour,2),":",The.strWithLeftZeros(minute,2),":00")));
				}else{
					input.setValue(keyToInject, CalendarUtil.dbDateToCalendar(The.concat(year , "-",The.strWithLeftZeros(month,2),"-", The.strWithLeftZeros(day,2))));
				}
			}

		} catch (Exception ex) {
			input.setValue(keyToInject, null);
		}
		return chain.invoke();
	}

	@Override
	public void destroy() {
	}
}