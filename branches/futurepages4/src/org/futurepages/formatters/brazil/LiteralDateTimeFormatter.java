package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Date;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.brazil.BrazilDateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateTimeFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
			String time = "";
			if(value instanceof Calendar){
			time = CalendarUtil.showHourMin((Calendar) value);

			}
			else{
				if(value instanceof Date){
					time = BrazilDateUtil.viewHourMin((Date) value);
				}
				else{
					return value.toString();
				}
			}
            return BrazilDateUtil.literalDateFromDB(value) +" "+time;

	}
 }
