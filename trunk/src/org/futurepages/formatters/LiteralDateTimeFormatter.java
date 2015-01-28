package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Date;
import org.futurepages.util.BrazilianDateUtil;
import java.util.Locale;
import org.futurepages.util.BrazilianCalendarUtil;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateTimeFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
			String time = "";
			if(value instanceof Calendar){
			time = BrazilianCalendarUtil.showHourMin((Calendar) value);

			}
			else{
				if(value instanceof Date){
					time = BrazilianDateUtil.viewHourMin((Date) value);
				}
				else{
					return value.toString();
				}
			}
            return BrazilianDateUtil.literalDateFromDB(value) +" "+time;

	}
 }
