package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Formata uma data String como entrada "YYYY-MM-DD"  e retorna no formato formato DD/MM/YYYY
 */
 public class DBDateFormatter extends AbstractFormatter<Object> {
 	
    @Override
    public String format(Object value, Locale loc) {
        if(value instanceof Date){
            return DateUtil.getInstance().dbDate((Date) value);
        }else{
            if(value instanceof Calendar){
                return CalendarUtil.dbDate((Calendar) value);
            }
        }
        return "";
    }
}