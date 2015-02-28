package org.futurepages.util;

import org.futurepages.core.locale.LocaleManager;
import org.futurepages.util.brazil.enums.DateFormatEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	private static DateUtil INSTANCE;

	private DateFormat defaultViewDateTimeFormat;
	private DateFormat defaultViewDateFormat;
	private DateFormat defaultDBDateFormat;
	private DateFormat defaultDBDateTimeFormat;

	public static DateUtil getInstance(){
		if(INSTANCE==null){
			INSTANCE = new DateUtil();
		}
		return INSTANCE;
	}

	private DateUtil(){
		defaultViewDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, LocaleManager.getDefaultLocale());
		defaultViewDateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, LocaleManager.getDefaultLocale());
		defaultDBDateFormat = new SimpleDateFormat("yyyy-MM-dd", LocaleManager.getDefaultLocale());
		defaultDBDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", LocaleManager.getDefaultLocale());
	}

	public String viewDateTime(Date date) {
		return defaultViewDateTimeFormat.format(date);
	}

	public String viewDate(Date date) {
		if (date == null) {
			return "";
		} else {
			return defaultViewDateFormat.format(date);
		}
	}

	public String viewDateTime(Date date, int dateAndTimeStyle, Locale locale) {
		DateFormat formatter = DateFormat.getDateTimeInstance(dateAndTimeStyle, dateAndTimeStyle, locale);
		return formatter.format(date);
	}
	public String viewDateTime(Date date, int dateAndTimeStyle) {
		return viewDateTime(date, dateAndTimeStyle, LocaleManager.getDefaultLocale());
	}

	public String viewDate(Date date, int dateStyle, Locale locale) {
		DateFormat formatter = DateFormat.getDateInstance(dateStyle, locale);
		return formatter.format(date);
	}

	public String viewDate(Date date, int dateStyle) {
		return viewDate(date, dateStyle, LocaleManager.getDefaultLocale());
	}

	public String dbDate(int year, int month, int day) {
		return The.concat(year,"-",month,"-",day);
	}

	public String dbDate(Date date) {
		return defaultDBDateFormat.format(date);
	}

	/**
	 * Output Date Mask: YYYY-MM-DD HH:mm:ss (database format)
	 */
	public String dbDateTime(Date date) {
		return defaultDBDateTimeFormat.format(date);
	}

	/**
	 *  Input can be Date, Calendar, Long or Number
	 */
	public String viewDateTime(Object in, String mask) {
		if (in instanceof Date) {
			return new SimpleDateFormat(mask, LocaleManager.getDefaultLocale()).format((Date) in);
		} else if (in instanceof Calendar) {
			return CalendarUtil.viewDateTime((Calendar)in,mask);
		} else if (in instanceof Long) {
			return new SimpleDateFormat(mask, LocaleManager.getDefaultLocale()).format(new Date((Long) in));
		} else if (in instanceof Number) {
			return TimeUtil.timeFrom(((Number) in).doubleValue()); //only time, consider signal
		}
		return null;
	}
	/**
	 * output format: yyyy-MM-dd
	 */
	public String dbToday() {
		return defaultDBDateFormat.format(new Date());
	}

	/**
	 * Specify the mask to show today informations.
	 */
	public String viewToday(String mask) {
		return new SimpleDateFormat(mask, LocaleManager.getDefaultLocale()).format(new Date());
	}

	/**
	 * Method to parse a date string in the supplied format to a Date object. If the format is null,
	 * the default "yyyy-MM-dd" is assumed and returns null if the input string does not match the
	 * format.
	 *
	 * @param dateString   The string to be converted to a Date
	 * @param formatString The format of the string
	 * @return The Date object of the string parsed
	 */
	public Date parse(String dateString, DateFormatEnum formatString) {
		if (formatString == null) {
			formatString = DateFormatEnum.DATE;
		}
		return parse(dateString, formatString.getMask());
	}

	public Date parse(String dateString, String formatString) {
		Date data = null;
		if (!Is.empty(dateString)) {
			SimpleDateFormat sdf = new SimpleDateFormat(formatString, LocaleManager.getDefaultLocale());
			sdf.setLenient(false);
			try {
				data = sdf.parse(dateString);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return data;
	}

	public Date parse(String dateString) {
		return parse(dateString, DateFormatEnum.DATE);
	}

	/**
	 * Formats with the givven mask.
	 */
	public String format(Date date, String mask) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(mask, LocaleManager.getDefaultLocale()).format(date);
	}
}