package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.gson.core.GsonRegister;

import java.lang.reflect.Type;
import java.util.Calendar;

public class GsonWithCalendarDateTime {

	public static void registerFor(GsonBuilder gb){
		new GsonRegister<Calendar>(Calendar.class, gb, true){

			@Override
			public Calendar fromJson(String jsonStr) {
				if(jsonStr.length()==10){
					return CalendarUtil.dbDateToCalendar(jsonStr);
				}
				return CalendarUtil.dbDateTimeToCalendar(jsonStr);
			}

			@Override
			public JsonPrimitive toJson(Calendar data, Type typeOft, JsonSerializationContext context) {
				return new JsonPrimitive(CalendarUtil.dbDateTime(data));
			}
		};
	}
}