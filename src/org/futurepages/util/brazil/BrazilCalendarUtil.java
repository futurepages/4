package org.futurepages.util.brazil;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.Is;
import org.futurepages.util.brazil.enums.DayOfWeek;
import org.futurepages.util.brazil.enums.MonthEnum;
import org.futurepages.util.brazil.enums.UnitTimeEnum;

import java.util.Calendar;

/**
 * Brazilian CalendarUtil
 */
public class BrazilCalendarUtil extends CalendarUtil {

		/**
	 * retorna um literal expressando o tempo para um
	 * determinado intervalo de calendar que possuem o mesmo dia/mes/ano
	 * caso contrario se não forem do mesmo dia e retornado ""
	 *
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

	public static String literalDayOfWeek(Calendar cal) {
		DayOfWeek day = DayOfWeek.getDayByKey(cal.get(Calendar.DAY_OF_WEEK));
		return day != null ? day.getSmallDescription() : "";
	}

	public static String getMonthAbbr(Calendar cal) {
		return MonthEnum.values()[cal.get(Calendar.MONTH)].getAbbr();
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

				if (isIntervalEqualsMonth && isYearCurrentResult) {
					return diaInicio + " a " + diaFim + " de " + mesInicio;

				} else if (!isIntervalEqualsMonth && isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim;

				} else if (isIntervalEqualsMonth) {
					return diaInicio + " a " + diaFim + " de " + mesFim + " de " + anoFim;

				} else {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim + " de " + anoFim;
				}

			} else { //o intervalo além de estar no mesmo ano também é do mesmo dia

				if (isIntervalEqualsMonth && isYearCurrentResult) {
					return (diaInicio + " de " + mesInicio + " " + faixaDeTempo).trim();

				} else if (isIntervalEqualsMonth) {
					return (diaInicio + " de " + mesInicio + " de " + anoInicio + " " + faixaDeTempo).trim();

				} else if (isYearCurrentResult) {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim;

				} else {
					return diaInicio + " de " + mesInicio + " a " + diaFim + " de " + mesFim + " de " + anoInicio;
				}

			}
		}

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
				assert unit != null;
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

	public static String getElapsedTimeStatement(int[] time, UnitTimeEnum unitLimit, int limitValue) throws TooBigDateException {
		return getElapsedTimeStatement(time, unitLimit, limitValue, true);
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
			exp = BrazilDateUtil.viewDateTime((nowFirst ? end : start));
		}

		if (!Is.empty(prefix)) {
			return prefix + exp;
		}
		return exp;
	}

	public static String literalRangeOfDates(Calendar calIni, Calendar calFim) {
		return literalRangeOfDates(calIni, calFim, CalendarUtil.now());
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, String commonPrefix, String exceptionPrefix) {
		return getElapsedTimeUntilNowStatement(start, null, 0, commonPrefix, exceptionPrefix);
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, UnitTimeEnum unitLimit, int limitValue) {
		return getElapsedTimeUntilNowStatement(start, unitLimit, limitValue, null, null);
	}

	public static String getElapsedTimeUntilNowStatement(Calendar start, UnitTimeEnum unitLimit, int limitValue,
	                                                     String commonPrefix, String exceptionPrefix) {
		return getElapsedTimeUntilNowStatementPriv(start, CalendarUtil.now(), unitLimit, limitValue, commonPrefix, exceptionPrefix);
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
}
