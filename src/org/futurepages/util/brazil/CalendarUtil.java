package org.futurepages.util.brazil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.futurepages.util.brazil.enums.DayOfWeek;
import org.futurepages.util.brazil.enums.MonthEnum;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.util.Is;
import org.futurepages.util.iterator.months.MonthYear;

/**
 * Brazilian format dates. Use new jdk8 api for Dates and Time
 */
public class CalendarUtil {

	private static final long millisecondsDayFactor = 86400000L;

	/*
	 * A shortcut to "Calendar.getInstance()".
	 */
	private static Calendar now() {
		return Calendar.getInstance();
	}

	/**
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
	 * @return true se o ano do calendário informado for um ano bisexto, false caso contrário.
	 */
	public static boolean isLeapYear(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		return year % 4 == 0;
	}

	public static boolean isCalendarDateEquals(Calendar calendar1, Calendar calencdar2) {
		return compareCalendarDate(calendar1, calencdar2) == 0;
	}

	/**
	 * Compara dois Calendar utilizando os campos ano, mes e ano
	 *
	 * @param calendar1
	 * @param calencdar2
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
	 */
	//CUIDADO, E UM ARREDONDAMENTO. SE POSSUIR 1,2 dia CONTARÁ COMO 1 e 1,6 dia contará como 2.
	public static int getDifferenceInDays(Calendar start, Calendar end) {
		int milliseconds = 86400000;
		return getDifference(start, end, milliseconds);
	}

	// calcula os dias sem levar em contas as horas
	public static int getDifferenceInAbsoluteDays(Calendar start, Calendar end) {
		int milliseconds = 86400000;
		return getDifference(DateUtil.dateToCalendar(start.getTime()), DateUtil.dateToCalendar(end.getTime()), milliseconds);
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
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.add(field, addValue);
		return dataInicial;
	}

	public static Calendar buildCalendar(int field, int addValue, Calendar dataInicial) {
		Calendar novaData = CalendarUtil.buildCalendar(dataInicial);
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
	 * Compara dois Calendar utilizando os campos hora, minuto , segundo
	 *
	 * @param calendar1
	 * @param calencdar2
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
	 * Compara dois Calendar utilizando o campo dia do mes
	 *
	 * @param calendar1
	 * @param calencdar2
	 * @return true (quando os dois são iguais do mesmo dia do mes)
	 * false caso contrario
	 */
	public static boolean compareCalendarByDayOfMonth(Calendar calendar1, Calendar calencdar2) {
		Integer dia1 = calendar1.get(Calendar.DAY_OF_MONTH);
		Integer dia2 = calencdar2.get(Calendar.DAY_OF_MONTH);
		int comparador = dia1.compareTo(dia2);
		if (comparador == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Compara dois Calendar utilizando o campo mes
	 *
	 * @param calendar1
	 * @param calencdar2
	 * @return true (quando os dois são iguais do mesmo mes)
	 * false caso contrario
	 */
	public static boolean compareCalendarByMonth(Calendar calendar1, Calendar calencdar2) {
		Integer mes1 = calendar1.get(Calendar.MONTH);
		Integer mes2 = calencdar2.get(Calendar.MONTH);
		int comparador = mes1.compareTo(mes2);
		if (comparador == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Compara dois Calendar utilizando o campo ano
	 *
	 * @param calendar1
	 * @param calendar2
	 * @return true (quando os dois são iguais do mesmo ano)
	 * false caso contrario
	 */
	public static boolean compareCalendarByYear(Calendar calendar1, Calendar calendar2) {
		Integer ano1 = calendar1.get(Calendar.YEAR);
		Integer ano2 = calendar2.get(Calendar.YEAR);
		int comparador = ano1.compareTo(ano2);
		if (comparador == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean dateIsOfCurrentYear(Calendar cal1) {
		return dateIsOfCurrentYear(cal1, Calendar.getInstance());
	}

	public static boolean dateIsOfCurrentYear(Calendar cal1, Calendar hoje) {
		return (cal1.get(Calendar.YEAR) == hoje.get(Calendar.YEAR));
	}

	public static String literalRangeOfDates(Calendar calIni, Calendar calFim) {
		return literalRangeOfDates(calIni, calFim, Calendar.getInstance());
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
	 * retorna um literal expressando o tempo para um
	 * determinado intervalo de calendar que possuem o mesmo dia/mes/ano
	 * caso contrario se não forem do mesmo dia e retornado ""
	 *
	 * @param calIni
	 * @param calFim
	 * @return período literal de horários
	 */
	public static String literalRangeOfTimes(Calendar calIni, Calendar calFim) {
		boolean ehMesmaData = (compareCalendarDate(calIni, calFim) == 0);

		if (ehMesmaData) {//se são da mesma data verifica o intervalo de tempo


			if ((hourOfDay(calIni) == 0 && minute(calIni) == 0)
					&& (hourOfDay(calFim) == 0 && minute(calFim) == 0)) {
				return "";
			}

			boolean isTimeInitEqualsTimeFinal = (hourOfDay(calIni) == hourOfDay(calFim) && (minute(calIni) == minute(calFim)));

			//se possuem o mesmo horario de inicio e fim faça
			if (isTimeInitEqualsTimeFinal) {
				return "às " + showLiteralHourMin(calIni);
			}

			if ((hourOfDay(calIni) < 1) && minute(calIni) > 0 && (hourOfDay(calFim) == 1) && minute(calFim) > 0) {
				return "de " + showLiteralHourMin(calIni) + " à " + showLiteralHourMin(calFim);
			}

			//se possuem somente o horario de inicio
			if ((hourOfDay(calFim) == 23 && minute(calFim) == 59) && (hourOfDay(calIni) != 0 || minute(calIni) != 0)) {
				return "a partir de " + showLiteralHourMin(calIni);
			}

			if (hourOfDay(calFim) > 1 && (hourOfDay(calFim) != 23 || minute(calFim) != 59) && (hourOfDay(calIni) != 0 || minute(calIni) != 0)) {
				return "de " + showLiteralHourMin(calIni) + " às " + showLiteralHourMin(calFim);
			}

			if ((hourOfDay(calFim) <= 1) && (hourOfDay(calIni) <= 1)) {
				return "de " + showLiteralHourMin(calIni) + " à " + showLiteralHourMin(calFim);
			}

			if ((hourOfDay(calFim) != 23 && minute(calFim) != 59) && (hourOfDay(calIni) == 0 || minute(calIni) == 0)) {
				return "até " + showLiteralHourMin(calFim);
			}

		}//se nao são da mesma data retorna vazio ""
		return "";
	}

	/**
	 * @return AAhBB onde AA são as horas e BB são os minutos
	 */
	public static String showLiteralHourMin(Calendar cal) {
		String mask = null;
		if (cal.get(Calendar.MINUTE) == 0) {
			mask = "H'h'";
		} else {
			mask = "H'h'mm";
		}
		return DateUtil.format(cal, mask);
	}

	public static String literalRangeOfDates(Calendar calIni, Calendar calFim, Calendar hoje) {

		boolean isYearCurrentResult = dateIsOfCurrentYear(calIni, hoje);//true
		boolean isIntervalEqualsDay = compareCalendarByDayOfMonth(calIni, calFim);//true
		boolean isIntervalEqualsMonth = compareCalendarByMonth(calIni, calFim);//true
		boolean isIntervalEqualsYear = compareCalendarByYear(calIni, calFim);//true

		int diaInicio = calIni.get(Calendar.DAY_OF_MONTH);
		String mesInicio = MonthEnum.get(calIni);
		int anoInicio = calIni.get(Calendar.YEAR);

		int diaFim = calFim.get(Calendar.DAY_OF_MONTH);
		String mesFim = MonthEnum.get(calFim);
		int anoFim = calFim.get(Calendar.YEAR);

		String faixaDeTempo = literalRangeOfTimes(calIni, calFim);

		if ((!isIntervalEqualsYear)) {//o intervalo ocorre em anos diferentes
			return formatLiteralIntervalCalendarDiffByYear(calIni, calFim);

		} else {//todos os intervalos abaixo estão no mesmo ano

			if (!isIntervalEqualsDay) {//apesar de o intervalo ser do mesmo ano ele engloba dias diferentes

				if (isIntervalEqualsYear && isIntervalEqualsMonth && isYearCurrentResult) {
					return diaInicio + " a " + diaFim + " de " + mesInicio;

				} else if (isIntervalEqualsYear && !isIntervalEqualsMonth && isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim;

				} else if (isIntervalEqualsYear && isIntervalEqualsMonth && !isYearCurrentResult) {
					return diaInicio + " a " + diaFim + " de " + mesFim + " de " + anoFim;

				} else if (isIntervalEqualsYear && !isIntervalEqualsMonth && !isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim + " de " + anoFim;
				}

			} else { //o intervalo além de estar no mesmo ano também é do mesmo dia

				if (isIntervalEqualsYear && isIntervalEqualsMonth && isYearCurrentResult) {
					return (diaInicio + " de " + mesInicio + " " + faixaDeTempo).trim();

				} else if (isIntervalEqualsYear && isIntervalEqualsMonth && !isYearCurrentResult) {
					return (diaInicio + " de " + mesInicio + " de " + anoInicio + " " + faixaDeTempo).trim();

				} else if (isIntervalEqualsYear && !isIntervalEqualsMonth && isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim;

				} else if (isIntervalEqualsYear && !isIntervalEqualsMonth && !isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim + " de " + anoInicio;
				}

			}
		}

		return "";
	}

	/**
	 * Retorna um intervalo de calendar no formato por extenso
	 * quando os calendars forem de anos diferentes caso contrario retorna ""
	 */
	private static String formatLiteralIntervalCalendarDiffByYear(Calendar calIni, Calendar calFim) {
		if (!compareCalendarByYear(calIni, calFim)) {
			return calIni.get(Calendar.DAY_OF_MONTH) + " de " + MonthEnum.get(calIni) + " de "
					+ calIni.get(Calendar.YEAR) + " a " + calFim.get(Calendar.DAY_OF_MONTH) + " de "
					+ MonthEnum.get(calFim) + " de " + calFim.get(Calendar.YEAR);
		} else {
			return "";
		}
	}

	private static int getAge(Calendar start, Calendar end) {
		int idade = 0;

		if (start.getTime() == null || start.getTime().compareTo(end.getTime()) > 0) {
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
	 * @param startI
	 * @param endI
	 * @return array int[ano,mes,dia,minuto,segundo]
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

		// Soma a mais 1 pelo fato de Calendar.Month começar com 0
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

	public static String getElapsedTimeUntilNowStatement(Calendar start, String commonPrefix, String exceptionPrefix) {
		return getElapsedTimeUntilNowStatement(start, null, 0, commonPrefix, exceptionPrefix);
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, UnitTimeEnum unitLimit, int limitValue) {
		return getElapsedTimeUntilNowStatement(start, unitLimit, limitValue, null, null);
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, UnitTimeEnum unitLimit, int limitValue,
	                                                     String commonPrefix, String exceptionPrefix) {
		return getElapsedTimeUntilNowStatementPriv(start, Calendar.getInstance(), unitLimit, limitValue, commonPrefix, exceptionPrefix);
	}

	private static String getElapsedTimeUntilNowStatementPriv(Calendar start, Calendar end, UnitTimeEnum unitLimit, int limitValue,
	                                                          String commonPrefix, String exceptionPrefix) {
		return getElapsedTimeUntilNowStatement(start, end, unitLimit, limitValue, commonPrefix, exceptionPrefix, true);
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, Calendar end, UnitTimeEnum unitLimit, int limitValue,
	                                                     String commonPrefix, String exceptionPrefix, boolean mostrarSegundoValor) {
		return getDifferenceToNowStatement(start, end, unitLimit, limitValue, commonPrefix, exceptionPrefix, mostrarSegundoValor, false);
	}

	public static String getRemainingTimeFromNowStatement(Calendar start, Calendar end, UnitTimeEnum unitLimit, int limitValue,
	                                                      String commonPrefix, String exceptionPrefix, boolean mostrarSegundoValor) {
		return getDifferenceToNowStatement(start, end, unitLimit, limitValue, commonPrefix, exceptionPrefix, mostrarSegundoValor, true);
	}

	/**
	 * Hora e minuto no formato HH:mm (hora no formato 24h.)
	 */
	public static String showHourMin(Calendar calendar) {
		return DateUtil.format(calendar, "H:mm");
	}

	public static Calendar buildCalendar(Calendar calendar) {
		Calendar newCalendar = new GregorianCalendar(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
		return newCalendar;
	}

	public static int getDifferenceInMonths(Calendar date1, Calendar date2) {
		int[] elapsed = getElapsedTime(date1, date2);
		return (12 * elapsed[0] + elapsed[1] + ((elapsed[2] > 0 || elapsed[3] > 0 || elapsed[4] > 0) ? 1 : 0));
	}

	public static int getDifferenceInMonths(MonthYear mYearIni, MonthYear mYearFim) {
		Calendar calIni = new GregorianCalendar(mYearIni.getYear(), mYearIni.getMonth() - 1, 1);
		Calendar calFim = new GregorianCalendar(mYearFim.getYear(), mYearFim.getMonth() - 1, 1);
		return CalendarUtil.getDifferenceInMonths(calIni, calFim);

	}

	/**
	 * Se são dias vizinhos, retorna true
	 */
	public static boolean isNeighborDays(Calendar cal1, Calendar cal2) {
		return CalendarUtil.getDifferenceInDays(CalendarUtil.buildDate(cal1), CalendarUtil.buildDate(cal2)) == 1;
	}


	public static String getDifferenceToNowStatement(Calendar start, Calendar end, UnitTimeEnum unitLimit, int limitValue,
	                                                 String commonPrefix, String exceptionPrefix, boolean mostrarSegundoValor, boolean nowFirst) {

		String prefix;
		String exp;
		try {
			int[] time = getElapsedTime(start, end);
			exp = getElapsedTimeStatement(time, unitLimit, limitValue, mostrarSegundoValor);
			prefix = commonPrefix;

		} catch (TooBigDateException e) {
			prefix = exceptionPrefix;
			exp = DateUtil.viewDateTime((nowFirst ? end : start));
		}

		if (!Is.empty(prefix)) {
			return prefix + exp;
		}
		return exp;
	}

	public static Calendar getToday() {
		return buildDate(Calendar.getInstance());
	}

	public static class TooBigDateException extends Exception {
	}

	public static String getElapsedTimeStatement(int[] time, UnitTimeEnum unitLimit, int limitValue) throws TooBigDateException {
		return getElapsedTimeStatement(time, unitLimit, limitValue, true);

	}

	/**
	 * Monta uma Expressão do tipo "1h e 3min" para o valor associado ás duas maiores unidades de tempo entre ano, mes, dia, ano, minuto
	 * presentes no array informado.
	 * Se o valor da unidade (ano, mes, dias...) mais relevante maior que zero for superior ao valor limite é levantada uma {@link TooBigDateException}
	 * <p/>
	 * 0,  1,  2,  3,   4,
	 * ano mes dia hora min
	 *
	 * @param time       array: [ano,mes,dia,hora,minuto]
	 * @param unitLimit: unidade limite a qual deve ser montada a expressão (X unidade atrás),
	 *                   obs: se a maior unidade > 0 for 'mes' e a unidadeLimite for dia, uma {@link TooBigDateException} será lançada.
	 * @param limitValue valor limite para a unidadeLimite
	 *                   obs: se a maior unidade > 0 for 'mes' com valor 2 e o valorLimite for 1, uma {@link TooBigDateException} será lançada.
	 * @return Expressão do tipo "1 ano 2 meses"
	 * @throws TooBigDateException
	 */
	public static String getElapsedTimeStatement(int[] time, UnitTimeEnum unitLimit, int limitValue, boolean withAbrrs) throws TooBigDateException {
		String tempo = withAbrrs ? "menos de 1 minuto" : "1 minuto";

		for (int i = 0; i < time.length; i++) {
			int valor = time[i];
			if (valor > 0) {

				if (unitLimit != null) {

					if (i < unitLimit.getOrder() || (i == unitLimit.getOrder()) && (valor > limitValue)) {
						throw new TooBigDateException();
					}
				}
				UnitTimeEnum unit = UnitTimeEnum.getByOrder(i);
				if (unit != null) {
					String primUnitName = unit.apropriateUnitDescription(valor, !withAbrrs);
					String separador = " ";
					if (unit.getOrder() > 2 && withAbrrs) {
						separador = "";
					}
					tempo = valor + separador + primUnitName;
					if (withAbrrs) {
						tempo = addNextValue(time, tempo, i, withAbrrs);
					}
					break;
				} else {
					break;
				}
			}

			if (valor < 0) {
				break;
			}
		}
		return tempo;
	}

	private static String addNextValue(int[] time, String tempo, int i, boolean withAbbrs) {
		UnitTimeEnum unit;
		if (i < time.length - 1) {
			i++;
			int valor = time[i];
			if (valor > 0) {
				unit = UnitTimeEnum.getByOrder(i);
				String segUnitName = unit.apropriateUnitDescription(valor, !withAbbrs);
				String separador2 = " ";
				if (unit.getOrder() > 2) {
					separador2 = "";
				}
				tempo += " e " + valor + separador2 + segUnitName;
				return tempo;
			}
		}
		return tempo;
	}

	public static String literalDayOfWeek(Calendar cal) {
		DayOfWeek day = DayOfWeek.getDayByKey(cal.get(Calendar.DAY_OF_WEEK));
		return day != null ? day.getSmallDescription() : "";
	}

	public static String getMonthAbbr(Calendar cal) {
		return MonthEnum.values()[cal.get(Calendar.MONTH)].getAbbr();
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
		return cal.after(Calendar.getInstance());
	}

	public static boolean isInPast(Calendar cal) {
		return cal.before(Calendar.getInstance());
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
	 * @param mounth
	 * @return
	 */
	public static int getLastDay(int mounth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mounth - 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns last day from mounth and year.
	 *
	 * @param mounth
	 * @param year
	 * @return Calendar
	 */
	public static Calendar getLastDay(int mounth, int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mounth - 1);
		c.set(Calendar.YEAR, year);
		int i = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, i);
		return c;
	}

	/**
	 * Returns first day from mounth and year.
	 *
	 * @param mounth
	 * @param year
	 * @return Calendar
	 */
	public static Calendar getFirstDay(int mounth, int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mounth - 1);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c;
	}

	/**
	 * Return true if test is between begin and end;
	 *
	 * @param test
	 * @param begin
	 * @param end
	 * @return
	 */

	public static boolean between(Calendar test, Calendar begin, Calendar end) {
		if (areEquals(test, begin) || areEquals(test, end)) return true;
		if (test.after(begin) && test.before(end)) return true;
		return false;
	}
}
