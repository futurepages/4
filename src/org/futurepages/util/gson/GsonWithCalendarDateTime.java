package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.gson.core.GsonRegister;

import java.util.Calendar;

public class GsonWithCalendarDateTime {

	public static void registerFor(GsonBuilder gb){
		new GsonRegister<Calendar>(Calendar.class, gb){
			@Override
			public Calendar fromJson(String jsonStr) {
				if(jsonStr.length()==10){
					return CalendarUtil.dbDateToCalendar(jsonStr);
				}
				return CalendarUtil.dbDateTimeToCalendar(jsonStr);
			}

			@Override
			public String toJson(Calendar data) {
				return CalendarUtil.dbDateTime(data);
			}
		};
	}
}