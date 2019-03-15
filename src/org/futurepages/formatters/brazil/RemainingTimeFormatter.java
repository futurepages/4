package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;

public class RemainingTimeFormatter extends AbstractFormatter<Calendar> {

	@Override
	public String format(Calendar futureMoment, Locale loc) {
		return formatValue(getNow(), futureMoment,null);
	}
	@Override
    public String format(Calendar futureMoment, Locale locale, String param) {
		return formatValue(getNow(), futureMoment,Integer.valueOf(param));
	}

	public static String formatValue(Calendar agora, Calendar momentoNoFuturo, Integer daysCountdown){
		try {
			if(CalendarUtil.isNeighborDays(momentoNoFuturo, agora)){
					return "amanhã às "+ DateUtil.getInstance().viewDateTime(momentoNoFuturo, "HH:mm");
			}else{
				int[] time = CalendarUtil.getElapsedTime(momentoNoFuturo, agora);
				BrazilCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
				return "hoje às " + DateUtil.getInstance().viewDateTime(momentoNoFuturo, "HH:mm");
			}
		} catch (CalendarUtil.TooBigDateException e) {
			int qtdDias;
			if(daysCountdown!=null && daysCountdown >= (qtdDias=CalendarUtil.getDifferenceInAbsoluteDays(agora,momentoNoFuturo))){
				return The.concat("faltam ",qtdDias," dias");
			}else{
				int mesAtual = agora.get(Calendar.MONTH)+1;
				int anoAtual = agora.get(Calendar.YEAR);
				int diaFuturo = momentoNoFuturo.get(Calendar.DAY_OF_MONTH);
				int mesFuturo = momentoNoFuturo.get(Calendar.MONTH)+1;
				int anoFuturo = momentoNoFuturo.get(Calendar.YEAR);
				String dia = (diaFuturo==1? "1º": String.valueOf(diaFuturo));
				String mes = MonthEnum.get(mesFuturo);
				String ano = null;
				if((anoFuturo>anoAtual) &&
				   ((mesFuturo>=mesAtual) || (mesAtual-mesFuturo<=4) || (anoFuturo-anoAtual > 1))){
						ano = " de "+anoFuturo;
				}else{
					ano = "";
				}
				return The.concat("em ", dia, " de ", mes, ano);
			}
		}
	}

	private Calendar getNow() {
		Calendar now = Calendar.getInstance();
		// ISSO FOI FEITO POR SUSPEITA DE ESTAR SENDO RETORNADO NULL - Relatado em Exception! (ESPERO QUE NUNCA CAIA AÍ NESSE LUGAR)
		// Suspeito também de bug do java no linux. Problema nunca reproduzido no Windows.
		// Card no Asana: https://app.asana.com/0/606870746887765/1114332710382423
		if(now==null){
			try { Thread.sleep(500); } catch (InterruptedException ignored) { }
			now = Calendar.getInstance();
			AppLogger.getInstance().execute(new RuntimeException(""));
		}
		return now;
	}
}