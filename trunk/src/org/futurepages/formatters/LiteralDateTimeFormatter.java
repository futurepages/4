package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Date;
import org.futurepages.util.DateUtil;
import java.util.Locale;
import org.futurepages.util.CalendarUtil;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateTimeFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
			String time = "";
			if(value instanceof Calendar){
			time = CalendarUtil.showHourMin((Calendar)value);

			}
			else{
				if(value instanceof Date){
					time = DateUtil.viewHourMin((Date) value);
				}
				else{
					return value.toString();
				}
			}
            return DateUtil.literalDateFromDB(value) +" "+time;

	}
 }
