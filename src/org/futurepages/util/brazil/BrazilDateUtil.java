package org.futurepages.util.brazil;

import org.futurepages.core.locale.LocaleManager;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.brazil.enums.DateFormatEnum;
import org.futurepages.util.brazil.enums.MonthEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Brazilian format Dates, Use new jdk8 api for Dates and Time
 */
public class BrazilDateUtil {

	/**
	 * Converte datas no formato DD/MM/AAAA para o formato por extenso em língua portuguesa.
	 * Obs.: converte somente datas entre os anos de  1500 a 2099
	 *
	 * @param date - a data de entrada no formato DD/MM/AAAA
	 * @return - a data por extenso em caracteres todos mnúsculos.
	 * @throws java.lang.Exception
	 */
	public static String literal(String date) throws Exception {
		return NumberUtil.dezenaPorExtenso(date.substring(0, 2))
				+ " de "
				+ MonthEnum.get(Integer.parseInt(date.substring(3, 5)))
				+ " de "
				+ NumberUtil.milharPorExtenso(date.substring(6, 10));
	}

	/**
	 * Converte datas no formato DD/MM/AAAA para o formato por extenso em língua portuguesa.
	 * Obs.: converte somente datas entre os anos de  1500 a 2099
	 *
	 * @param date - a data de entrada no formato DD/MM/AAAA
	 * @return - a data por extenso em caracteres todos mínusculos.
	 */
	public static String literal(Date date) {
		try {
			return literal(viewDate(date));
		} catch (Exception ex) {
			return null;
		}
	}

	public static String literalDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return BrazilCalendarUtil.literalDayOfWeek(cal);
	}

	public static String literalDateFromDB(Object in) {
		String value = "";
		if (in instanceof Date) {
			value = DateUtil.getInstance().dbDate((Date) in);

		} else if (in instanceof Calendar) {
			value = CalendarUtil.dbDate((Calendar) in);

		} else if (in instanceof String) {
			value = (String) in;
		}

		try {
			int dia = Integer.parseInt(value.substring(8, 10));
			return dia + ((dia == 1) ? "º" : "") + " de " + MonthEnum.get(Integer.parseInt(value.substring(5, 7))) + " de " + value.substring(0, 4);
		} catch (Exception e) {
			return "<Erro de Conversão>"; //TODO: fazer exception
		}
	}

	public static String literalDateFromView(String in) {
		try {
			return in.substring(0, 2) + " de " + MonthEnum.get(Integer.parseInt(in.substring(3, 5))) + " de " + in.substring(6, 10);
		} catch (Exception e) {
			return "<Erro na conversão da data>";
		}
	}

	public static String literalDate() {
		try {
			return CalendarUtil.getActualDay() + " de " + MonthEnum.get(CalendarUtil.getActualMonth()) + " de " + CalendarUtil.getActualYear();
		} catch (Exception e) {
			return "<Erro na conversão da data>";
		}
	}

	public static String literalDate(Date date) {
		return literalDateFromView(viewDate(date));
	}

	/**
	 * Formato da entrada: DD/MM/YYYY
	 * Formato da saída: YYYY-MM-DD
	 */
	public static String dbDate(String in) {
		String dia, mes, ano, data;
		dia = in.substring(0, 2);
		mes = in.substring(3, 5);
		ano = in.substring(6, 10);
		data = ano + "-" + mes + "-" + dia;
		return data;
	}

	/**
	 * Formato da entrada: YYYY-MM-DD
	 * Formato da saída: DD/MM/YYYY
	 */
	public static String viewDate(Object in) {
		try {
			if (in instanceof String) {
				String strIn = (String) in;
				String dia, mes, ano, data;
				dia = strIn.substring(8, 10);
				mes = strIn.substring(5, 7);
				ano = strIn.substring(0, 4);
				data = dia + "/" + mes + "/" + ano;
				return data;
			} else if (in instanceof Date) {
				return new SimpleDateFormat("dd/MM/yyyy", LocaleManager.getDefaultLocale()).format((Date) in);
			}
			if (in instanceof GregorianCalendar) {
				GregorianCalendar date = (GregorianCalendar) in;
				return new SimpleDateFormat("dd/MM/yyyy", LocaleManager.getDefaultLocale()).format((Date) date.getTime());
			}
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Formato da entrada: YYYY-MM-DD
	 * Formato da saída: DD/MM/YYYY
	 */
	public static String viewDateTime(Object in) {
		return DateUtil.getInstance().viewDateTime(in, "dd/MM/yyyy - HH:mm");
	}

	public static String viewToday() {
		return DateUtil.getInstance().viewToday("dd/MM/yyyy");
	}

	/**
	 * Recebe como entrada a data no formato DD/MM/YYYY e retorna
	 * o valor da tada do tipo long no formato YYYYMMDD
	 */
	public static long longDate(String in) {
		String dia, mes, ano, data;
		dia = in.substring(0, 2);
		mes = in.substring(3, 5);
		ano = in.substring(6, 10);
		data = ano + mes + dia;
		return Long.parseLong(data);
	}

	/**
	 * A data de entrada do formato AAAA-MM-DD é retornada no seguinte formato: AAMMDD
	 */
	public static String rawDateIn6(String in) {
		String dia, mes, ano, data;
		ano = in.substring(2, 4);
		mes = in.substring(5, 7);
		dia = in.substring(8, 10);
		data = ano + mes + dia;
		return data;
	}

	public static String rawDateIn6(Date dataIn) {
		String in = new SimpleDateFormat("dd/MM/yyyy", LocaleManager.getDefaultLocale()).format(dataIn);
		return rawDateIn6(in);
	}

	public static Date date(String in) throws ParseException {
		Date correctDate;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", LocaleManager.getDefaultLocale());
		df.setLenient(false);
		correctDate = df.parse(in);
		return correctDate;
	}

	/**
	 * Valida datas no formato DD/MM/YYYY
	 */
	public static boolean isValidDate(String data) {

		try {
			if (data.length() != 10) {
				return false;
			}
			String dia, mes, ano;
			dia = data.substring(0, 2);
			mes = data.substring(3, 5);
			ano = data.substring(6, 10);

			Integer.parseInt(dia);
			Integer.parseInt(mes);
			Integer.parseInt(ano);
			BrazilDateUtil.date(dia + "/" + mes + "/" + ano);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static Date parseView(String dateString) {
		if (dateString.length() == 10) {
			if (dateString.charAt(2) != '/' || dateString.charAt(5) != '/') {
				throw new RuntimeException("Invalid format of date: " + dateString);
			}
		}
		return DateUtil.getInstance().parse(dateString, DateFormatEnum.VIEW_DATE_PT_BR);
	}

	/**
	 * @return format(calendar.getTime())
	 */
	public static String format(Calendar calendar) {
		return format(calendar.getTime());
	}

	/**
	 * @return format(calendar, mask.getMask())
	 */
	public static String format(Calendar calendar, DateFormatEnum mask) {
		return CalendarUtil.format(calendar, mask.getMask());
	}

	/**
	 * Mask: "yyyy-MM-dd"
	 */
	public static String format(Date date) {
		return format(date, DateFormatEnum.DATE);
	}

	public static String format(Date date, DateFormatEnum mask) {
		return DateUtil.getInstance().format(date, mask.getMask());
	}

	public static String viewHourMin(Date date) {
		return (new SimpleDateFormat("HH:mm", LocaleManager.getDefaultLocale()).format(date));
	}
}