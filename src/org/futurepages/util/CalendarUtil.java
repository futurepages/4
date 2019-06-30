package org.futurepages.util;

import org.futurepages.core.locale.NewLocaleManager;
import org.futurepages.util.iterator.months.MonthYear;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Brazilian format dates. Use new jdk8 api for Dates and Time
 */
public class CalendarUtil {

	private static final long millisecondsDayFactor = 86400000L;

	/*
	 * A shortcut to "Calendar.getInstance()".
	 */
	public static Calendar now() {
		return Calendar.getInstance();
	}

// ONLY FOR DEBUG:
//	public static Calendar FAKE_NOW = null;
//	public static Calendar now() {
//		return FAKE_NOW!=null? (Calendar) FAKE_NOW.clone() : Calendar.getInstance();
//	}

	/**
	 *  TODO translate:
	 * Retorna o iésimo dia do ano da data informada.
	 * <br>Se o ano for não bisexto(possui um dia a menos no ano),o valor retornado será o iésimo dia +1 para os dias após 01/03.
	 * Desta forma, 1º de março e os dias até o final do ano serão representados pelo mesmo valor em ambos os anos(bisexto e não bisexto).
	 */
	public static int getOrdinalDayOfYear(Calendar cal) {
		int nDia = cal.get(Calendar.DAY_OF_YEAR);
		boolean bisexto = isLeapYear(cal);
		if (!bisexto) {
			int month = cal.get(Calendar.MONTH);
			if (month >= Calendar.MARCH) {
				nDia++;
			}
		}
		return nDia;
	}

	public static boolean areEquals(Calendar cal1, Calendar cal2) {

		return (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
				&& cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
				&& cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND)
		);
	}

	/**
	 * @return true if the year is a leap year.
	 */
	public static boolean isLeapYear(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		return year % 4 == 0;
	}

	public static boolean isCalendarDateEquals(Calendar calendar1, Calendar calencdar2) {
		return compareCalendarDate(calendar1, calencdar2) == 0;
	}

	/**
	 * TODO translate:
	 * Compara dois Calendar utilizando os campos ano, mes e ano

	 * @return -1 (quando a primeira data é menor que a segunda)
	 * 1 (quando a primeira data é maior que a segunda)
	 * 0 (quando os dois são iguais)
	 */
	public static int compareCalendarDate(Calendar calendar1, Calendar calencdar2) {
		Integer ano1 = calendar1.get(Calendar.YEAR);
		Integer ano2 = calencdar2.get(Calendar.YEAR);
		int comparador = ano1.compareTo(ano2);
		if (comparador == 0) {
			Integer mes1 = calendar1.get(Calendar.MONTH);
			Integer mes2 = calencdar2.get(Calendar.MONTH);
			comparador = mes1.compareTo(mes2);
			if (comparador == 0) {
				Integer dia1 = calendar1.get(Calendar.DATE);
				Integer dia2 = calencdar2.get(Calendar.DATE);
				comparador = dia1.compareTo(dia2);
				return comparador;
			}
		}
		return comparador;
	}

	/**
	 * 1 day == 86.400.000 miliseconds; //(24 * 60 * 60 * 1000)
	 *
	 *  TODO translate:
	 *  CUIDADO, E UM ARREDONDAMENTO. SE POSSUIR 1,2 dia CONTARÁ COMO 1 e 1,6 dia contará como 2.
	 */
	public static int getDifferenceInDays(Calendar start, Calendar end) {
		int milliseconds = 86400000;
		return getDifference(start, end, milliseconds);
	}

	// calcula os dias sem levar em contas as horas
	public static int getDifferenceInAbsoluteDays(Calendar start, Calendar end) {
		int milliseconds = 86400000;
		return getDifference(dateToCalendar(start.getTime()), dateToCalendar(end.getTime()), milliseconds);
	}

	/**
	 * 1 minute == 3.600.000 miliseconds //(60 * 60 * 1000)
	 */
	public static int getDifferenceInHours(Calendar start, Calendar end) {
		int milliseconds = 3600000;
		return getDifference(start, end, milliseconds);
	}

	/**
	 * 1 minute == 60.000 miliseconds //(60 * 1000)
	 */
	public static int getDifferenceInMinutes(Calendar start, Calendar end) {
		int milliseconds = 60000;
		return getDifference(start, end, milliseconds);
	}

	/**
	 * 1 second == 1.000 miliseconds
	 */
	public static int getDifferenceInSeconds(Calendar start, Calendar end) {
		int milliseconds = 1000;
		return getDifference(start, end, milliseconds);
	}

	public static int getDifference(Calendar start, Calendar end, double millisecondsFactor) {
		if (start == null || end == null) {
			return 0;
		}
		long m1 = start.getTimeInMillis();
		long m2 = end.getTimeInMillis();
		return getDifference(m1, m2, millisecondsFactor);
	}

	public static int getDifferenceInDays(long m1, long m2) {
		return getDifference(m1, m2, millisecondsDayFactor);
	}

	public static int getDifference(long start, long end, double millisecondsFactor) {
		long diff = (end - start);
		if (diff < 0) {
			diff *= -1;
		}
		return new Long(Math.round(diff / millisecondsFactor)).intValue();
	}

	public static Calendar buildCalendar(int field, int addValue) {
		Calendar dataInicial = CalendarUtil.now();
		dataInicial.add(field, addValue);
		return dataInicial;
	}

	public static Calendar buildCalendar(int field, int addValue, Calendar dataInicial) {
		Calendar novaData = buildCalendar(dataInicial);
		novaData.add(field, addValue);
		return novaData;
	}

	public static Calendar buildDate(Calendar calendar) {
		return new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static Calendar buildCalendar(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day);
	}

	/**
	 * TODO translate:
	 * Compara dois Calendar utilizando os campos hora, minuto , segundo
	 *
	 * @return -1 (quando o segundo tempo é maior que o primeiro)
	 * 1 (quando o primeiro tempo é maior que o segundo)
	 * 0 (quando os dois são iguais do mesmo horario)
	 */
	public static int compareCalendarTime(Calendar calendar1, Calendar calencdar2) {
		Integer hora1 = calendar1.get(Calendar.HOUR_OF_DAY);
		Integer hora2 = calencdar2.get(Calendar.HOUR_OF_DAY);
		int comparador = hora1.compareTo(hora2);
		if (comparador == 0) {
			Integer minute1 = calendar1.get(Calendar.MINUTE);
			Integer minute2 = calencdar2.get(Calendar.MINUTE);
			comparador = minute1.compareTo(minute2);
			if (comparador == 0) {
				Integer segundo1 = calendar1.get(Calendar.SECOND);
				Integer segundo2 = calencdar2.get(Calendar.SECOND);
				comparador = segundo1.compareTo(segundo2);
				return comparador;
			}
		}
		return comparador;
	}

	/**
	 * TODO translate:
	 * Compara dois Calendar utilizando o campo dia do mes
	 *
	 * @return true (quando os dois são iguais do mesmo dia do mes)
	 * false caso contrario
	 */
	public static boolean compareCalendarByDayOfMonth(Calendar calendar1, Calendar calencdar2) {
		Integer dia1 = calendar1.get(Calendar.DAY_OF_MONTH);
		Integer dia2 = calencdar2.get(Calendar.DAY_OF_MONTH);
		int comparador = dia1.compareTo(dia2);
		return comparador == 0;
	}

	/**
	 * Compara dois Calendar utilizando o campo mes
	 *
	 * @return true (quando os dois são iguais do mesmo mes)
	 * false caso contrario
	 */
	public static boolean compareCalendarByMonth(Calendar calendar1, Calendar calencdar2) {
		Integer mes1 = calendar1.get(Calendar.MONTH);
		Integer mes2 = calencdar2.get(Calendar.MONTH);
		int comparador = mes1.compareTo(mes2);
		return comparador == 0;
	}

	/**
	 * Compare two calendars using the 'year' field
	 *
	 * @return true (when they are from the same year)
	 */
	public static boolean compareCalendarByYear(Calendar calendar1, Calendar calendar2) {
		Integer ano1 = calendar1.get(Calendar.YEAR);
		Integer ano2 = calendar2.get(Calendar.YEAR);
		int comparador = ano1.compareTo(ano2);
		return comparador == 0;
	}

	public static boolean dateIsOfCurrentYear(Calendar cal1) {
		return dateIsOfCurrentYear(cal1, CalendarUtil.now());
	}

	public static boolean dateIsOfCurrentYear(Calendar cal1, Calendar hoje) {
		return (cal1.get(Calendar.YEAR) == hoje.get(Calendar.YEAR));
	}

	public static int hourOfDay(Calendar cal) {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int minute(Calendar cal) {
		return cal.get(Calendar.MINUTE);
	}

	public static int second(Calendar cal) {
		return cal.get(Calendar.SECOND);
	}



	/**
	 * @return AAhBB where AA = hours and BB = minutes
	 */
	public static String showLiteralHourMin(Calendar cal) {
		String mask = null;
		if (cal.get(Calendar.MINUTE) == 0) {
			mask = "H'h'";
		} else {
			mask = "H'h'mm";
		}
		return format(cal, mask);
	}

	private static int getAge(Calendar start, Calendar end) {
		int idade = 0;

		if (start.getTime().compareTo(end.getTime()) > 0) {
			return idade;
		}

		boolean mesMaior = end.get(Calendar.MONTH) > start.get(Calendar.MONTH);
		boolean mesIgual = end.get(Calendar.MONTH) == start.get(Calendar.MONTH);
		boolean diaMaior = end.get(Calendar.DATE) >= start.get(Calendar.DATE);

		idade += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) - 1;

		if (mesMaior) {
			idade += 1;
		} else if (mesIgual && diaMaior) {
			idade += 1;
		}
		return idade;
	}

	/**
	 * @return format(calendar.getTime(), mask)
	 */
	public static String format(Calendar calendar, String mask) {
		Date date = null;
		if (calendar != null) {
			date = calendar.getTime();
		}
		return DateUtil.getInstance().format(date, mask);
	}

	/**
	 * @return array int[year,month,day,minute,second]
	 */
	public static int[] getElapsedTime(Calendar startI, Calendar endI) {


		if (startI == null) {
			return new int[]{0, 0, 0, 0, 0};
		}

		Calendar start = (Calendar) startI.clone();
		Calendar end = (Calendar) endI.clone();

		if (start.compareTo(end) > 0) {
			Calendar temp = start;
			start = end;
			end = temp;
		}

		int years = getAge(start, end);

		// plus 1 because Calendar.Month starts with 0
		int months = ((end.get(Calendar.MONTH) + 1) - (start.get(Calendar.MONTH) + 1));

		if (months <= 0) {
			months += 12;
		}

		if (months == 12 && (end.get(Calendar.DATE) >= start.get(Calendar.DATE))) {
			months = 0;
		}

		int days = end.get(Calendar.DATE) - start.get(Calendar.DATE);
		if (days < 0) {
			months--;
			if (end.get(Calendar.MONTH) == Calendar.JANUARY) {
				end.set(Calendar.MONTH, Calendar.DECEMBER);
			} else {
				end.set(Calendar.MONTH, end.get(Calendar.MONTH) - 1);
			}
			days += end.getActualMaximum(Calendar.DATE);
		}
		int horas = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
		if (horas < 0) {
			days--;
			horas += 24;
		}
		int minutos = end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE);
		if (minutos < 0) {
			horas--;
			minutos += 60;
		}
		return new int[]{years, months, days, horas, minutos};
	}

	/**
	 * Hour and minute in 'HH:mm' mask (24h format)
	 */
	public static String showHourMin(Calendar calendar) {
		return format(calendar, "H:mm");
	}

	public static Calendar buildCalendar(Calendar calendar) {
		return new GregorianCalendar(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
	}

	public static int getDifferenceInMonths(Calendar date1, Calendar date2) {
		int[] elapsed = getElapsedTime(date1, date2);
		return (12 * elapsed[0] + elapsed[1] + ((elapsed[2] > 0 || elapsed[3] > 0 || elapsed[4] > 0) ? 1 : 0));
	}

	public static int getDifferenceInMonths(MonthYear mYearIni, MonthYear mYearFim) {
		Calendar calIni = new GregorianCalendar(mYearIni.getYear(), mYearIni.getMonth() - 1, 1);
		Calendar calFim = new GregorianCalendar(mYearFim.getYear(), mYearFim.getMonth() - 1, 1);
		return getDifferenceInMonths(calIni, calFim);

	}

	/**
	 * Se são dias vizinhos, retorna true
	 */
	public static boolean isNeighborDays(Calendar cal1, Calendar cal2) {
		return getDifferenceInDays(buildDate(cal1), buildDate(cal2)) == 1;
	}

	public static Calendar getToday() {
		return buildDate(CalendarUtil.now());
	}

	public static class TooBigDateException extends Exception {
	}

	public static boolean isToday(Calendar cal) {
		return isSameDay(cal, now());
	}

	public static boolean isSameDay(Calendar day1, Calendar day2) {
		boolean isSameYear = day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR);
		boolean isSameMonth = day1.get(Calendar.MONTH) == day2.get(Calendar.MONTH);
		boolean isSameDay = day1.get(Calendar.DAY_OF_MONTH) == day2.get(Calendar.DAY_OF_MONTH);

		return (isSameYear && isSameMonth && isSameDay);
	}

	public static boolean isInFuture(Calendar cal) {
		return cal.after(CalendarUtil.now());
	}

	public static boolean isInPast(Calendar cal) {
		return cal.before(CalendarUtil.now());
	}

	public static boolean isAnniversaryToday(Calendar birthday) {
		Calendar now = now();
		boolean isSameMonth = now.get(Calendar.MONTH) == birthday.get(Calendar.MONTH);
		boolean isSameDay = now.get(Calendar.DAY_OF_MONTH) == birthday.get(Calendar.DAY_OF_MONTH);

		return isSameMonth && isSameDay;
	}

	/**
	 * Returns last day from mounth in current year.
	 *
	 */
	public static int getLastDay(int mounth) {
		Calendar c = CalendarUtil.now();
		c.set(Calendar.MONTH, mounth - 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns last day from mounth and year.
	 *
	 */
	public static Calendar getLastDay(int mounth, int year) {
		Calendar c = CalendarUtil.now();
		c.set(Calendar.MONTH, mounth - 1);
		c.set(Calendar.YEAR, year);
		int i = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, i);
		return c;
	}

	/**
	 * Returns the actual month.
	 */
	public static int getActualMonth() {
		return (now()).get(Calendar.MONTH) + 1;
	}

	/**
	 * Returns the actual year.
	 */
	public static int getActualYear() {
		return (now()).get(Calendar.YEAR);
	}

	/**
	 * Returns the actual day of month.
	 */
	public static int getActualDay() {
		return (now()).get(Calendar.DAY_OF_MONTH);
	}


	/**
	 * Returns first day from mounth and year.
	 *
	 */
	public static Calendar getFirstDay(int mounth, int year) {
		Calendar c = CalendarUtil.now();
		c.set(Calendar.MONTH, mounth - 1);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c;
	}

	/**
	 * Return true if test is between begin and end;
	 */

	public static boolean between(Calendar test, Calendar begin, Calendar end) {
		return areEquals(test, begin) || areEquals(test, end) || test.after(begin) && test.before(end);
	}

	public static Calendar dbDateToCalendar(String in) {
		return new GregorianCalendar(Integer.parseInt(in.substring(0, 4)), Integer.parseInt(in.substring(5, 7)) - 1, Integer.parseInt(in.substring(8)));
	}


	public static Calendar dbDateTimeToCalendar(String in) {
		return new GregorianCalendar(Integer.parseInt(in.substring(0, 4)),
				Integer.parseInt(in.substring(5, 7)) - 1,
				Integer.parseInt(in.substring(8, 10)),
				Integer.parseInt(in.substring(11, 13)),
				Integer.parseInt(in.substring(14, 16)),
				Integer.parseInt(in.substring(17, 19)));
	}


	public static Calendar dateTimeToCalendar(Date date) {
		return dbDateTimeToCalendar(DateUtil.getInstance().dbDateTime(date));
	}

	public static Calendar dateToCalendar(Date date) {
		return dbDateToCalendar(DateUtil.getInstance().dbDate(date));
	}

	/**
	 * Output: YYYY-MM-DD
	 */
	public static String dbDate(Calendar calendar) {
		String bdDate = "";
		if (calendar != null) {
			return DateUtil.getInstance().dbDate(calendar.getTime());
		}
		return bdDate;
	}
	/**
	 * Output: YYYY-MM-DD HH:mm:ss
	 */
	public static String dbDateTime(Calendar calendar) {
		String bdDate = "";
		if (calendar != null) {
			return DateUtil.getInstance().dbDateTime(calendar.getTime());
		}
		return bdDate;
	}

	public static String viewDateTime(Calendar inputCalendar, String mask){
		return new SimpleDateFormat(mask, NewLocaleManager.getDefaultLocale()).format(inputCalendar.getTime());
	}

	/**
	 * String input in MEDIUM DateFormat.
	 */
	public static Calendar viewDateTime(String inputInMediumFormat) {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, NewLocaleManager.getDefaultLocale());
			df.setLenient(false);
			Date date = df.parse(inputInMediumFormat);
			Calendar cal = CalendarUtil.now();
			cal.setTime(date);
			return cal;
		} catch (Exception ex) {
			return null;
		}
	}
}
