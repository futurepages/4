package org.futurepages.util.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.Is;

import java.util.Calendar;

public class GsonWithCalendarDateTime {


	public static void registerFor(GsonBuilder gb){
		gb.registerTypeAdapter(Calendar.class, (JsonDeserializer<Calendar>) (json, typeOfT, context) -> {
			try {
				String jsonStr = json.getAsString();
				if(!Is.empty(jsonStr)){
					if(jsonStr.length()==10){
						return CalendarUtil.dbDateToCalendar(jsonStr);
					}
					return CalendarUtil.dbDateTimeToCalendar(jsonStr); //Retorna um Date com a data formatada
				}
			}catch (Exception e) {
				AppLogger.getInstance().execute(e);
			}
			return null;
		});
		gb.registerTypeHierarchyAdapter(Calendar.class, (JsonSerializer<Calendar>) (data, typeOft, context) -> {
			try {
				if(data!=null){
					String datajson =  CalendarUtil.dbDateTime(data);
					return new JsonPrimitive(datajson);
				}
			}catch (Exception e) {
				AppLogger.getInstance().execute(e);
			}
			return null;
		});
	}
}
