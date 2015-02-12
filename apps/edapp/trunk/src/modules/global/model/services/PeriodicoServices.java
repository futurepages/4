package modules.global.model.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import modules.global.model.core.Periodico;
import org.futurepages.util.Is;
import org.futurepages.util.TimeUtil;

public class PeriodicoServices {

	public static void corrigeDataHoraPeriodico(Periodico periodico, boolean periodicoDeVariosDias, String horarioInicio, String horarioFim) throws Exception  {

		final boolean possuiDataInicio = periodico.getDataHoraInicio() != null;

		corrigirDataHoraInicioPeriodico(periodico, horarioInicio, possuiDataInicio);
		corrigirDataHoraFimPeriodico(periodico, periodicoDeVariosDias, horarioFim, possuiDataInicio);
	}

	public static void corrigirDataHoraInicioPeriodico(Periodico periodico, String horarioInicio, final boolean possuiDataInicio)
			throws Exception {
		if (possuiDataInicio) {
			String defaulHorariotInicio = "00:00:00";
			String horarioInicioCorrigido = corrigirHorario(horarioInicio, defaulHorariotInicio);
			Calendar calendarInicio = setHorarioCalendar(periodico.getDataHoraInicio(), horarioInicioCorrigido);
			periodico.setDataHoraInicio(calendarInicio);
		}
	}

	public static void corrigirDataHoraFimPeriodico(Periodico periodico, boolean periodicoDeVariosDias,
			String horarioFim, final boolean possuiDataInicio)
			throws Exception {

		String defaultHorarioFim = "23:59:59";
		horarioFim = corrigirHorario(horarioFim, defaultHorarioFim);

		//se periodico não é realizado em vários dias, mas sim em um único dia
		//atribui na DataEventoFim do periodico os valores da dataInicio de realização do periodico
		if (periodicoDeVariosDias) {
			//seta o horario de fim de realizacao do periodico
			if (periodico.getDataHoraFim() != null) {
				Calendar calendarFim = setHorarioCalendar(periodico.getDataHoraFim(), horarioFim);
				periodico.setDataHoraFim(calendarFim);
			}
		} else {
			//se existe uma data válida para o inicio do periodico faça
			if (possuiDataInicio) {
				final GregorianCalendar dataFimEventoUnicoDia = new GregorianCalendar(
						periodico.getDataHoraInicio().get(Calendar.YEAR),
						periodico.getDataHoraInicio().get(Calendar.MONTH),
						periodico.getDataHoraInicio().get(Calendar.DAY_OF_MONTH));
				//seta o horario de fim da realizacao do periodico
				Calendar calendarFim = setHorarioCalendar(dataFimEventoUnicoDia, horarioFim);//mudei para horarioFim era horarioInicio
				periodico.setDataHoraFim(calendarFim);
			}
		}
	}

	/**
	 *
	 * Se o horário passado for não vazio e válido, retorna o horário passado + :00 senão retorna o horariodefault
	 *
	 * @param horario no formato 99:99
	 * @param horarioDefault 00:00:00
	 * @return horario no formato 99:99:00 :(horario):00 | horarioDefault
	 * @throws Exception se horário for inválido ex: 25:01
	 */
	public static String corrigirHorario(String horario, String horarioDefault) throws Exception {
		if (TimeUtil.timeIsValid(horario)) {
			horario += ":00";//adiciona os segundos
		} else {
			if (Is.empty(horario)) {
				horario = horarioDefault;
			} else {
				throw new Exception();
			}
		}
		return horario;
	}

	/**
	 *
	 * @param calendar
	 * @param horario (mascara: 99:99:99)
	 * @return calendar com horarios
	 */
	public static Calendar setHorarioCalendar(Calendar calendar, String horario) {
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario.substring(0, 2)));
		calendar.set(Calendar.MINUTE, Integer.parseInt(horario.substring(3, 5)));
		calendar.set(Calendar.SECOND, Integer.parseInt(horario.substring(6, 8)));
		return calendar;
	}
}
