package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;

public class RemainingTimeFormatter extends AbstractFormatter<Calendar> {

	@Override
	public String format(Calendar futureMoment, Locale loc) {
		return formatValue(CalendarUtil.now(), futureMoment,null,null);
	}

	@Override
	public String format(Calendar futureMoment, Locale locale, String param) {
		return formatValue(CalendarUtil.now(), futureMoment, Integer.valueOf(param),null);
	}

	@Override
    public String format(Calendar futureMoment, Locale locale, String[] params) {
		return formatValue(CalendarUtil.now(), futureMoment, Integer.valueOf(params[0]), params[1]);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoFuturo, Integer daysCountdown, String lang){
		boolean en = !Is.empty(lang) && lang.equals("en");
		try {
			if(CalendarUtil.isNeighborDays(momentoNoFuturo, agora)){
					if(!en){
						return "amanhã às "+ DateUtil.getInstance().viewDateTime(momentoNoFuturo, "HH:mm");
					}else{
						return "tomorrow at "+ DateUtil.getInstance().viewDateTime(momentoNoFuturo, "hh:mm a");
					}
			}else{
				int[] time = CalendarUtil.getElapsedTime(momentoNoFuturo, agora);
				BrazilCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
				if(!en){
					return "hoje às " + DateUtil.getInstance().viewDateTime(momentoNoFuturo, "HH:mm");
				}else{
					return "today at " + DateUtil.getInstance().viewDateTime(momentoNoFuturo, "hh:mm a");
				}
			}
		} catch (CalendarUtil.TooBigDateException e) {
			int qtdDias;
			if(daysCountdown!=null && daysCountdown >= (qtdDias=CalendarUtil.getDifferenceInAbsoluteDays(agora,momentoNoFuturo))){
				if(!en){
					return The.concat("faltam ",qtdDias," dias");
				}else{
					return The.concat("",qtdDias," days left");
				}
			}else{
				int mesAtual = agora.get(Calendar.MONTH)+1;
				int anoAtual = agora.get(Calendar.YEAR);
				int diaFuturo = momentoNoFuturo.get(Calendar.DAY_OF_MONTH);
				int mesFuturo = momentoNoFuturo.get(Calendar.MONTH)+1;
				int anoFuturo = momentoNoFuturo.get(Calendar.YEAR);
				if(!en){
					String dia = (diaFuturo==1? "1º": String.valueOf(diaFuturo));
					String mes = MonthEnum.get(mesFuturo);
					String ano;
					if((anoFuturo>anoAtual) &&
							((mesFuturo>=mesAtual) || (mesAtual-mesFuturo<=4) || (anoFuturo-anoAtual > 1))){
						ano = " de "+anoFuturo;
					}else{
						ano = "";
					}
					return The.concat("em ", dia, " de ", mes, ano);
				}else{
					String mes = The.capitalizedWord(MonthEnum.values()[mesFuturo - 1].name().toLowerCase());
					String dia = String.valueOf(diaFuturo);
					String ano;
					if((anoFuturo>anoAtual) &&
							((mesFuturo>=mesAtual) || (mesAtual-mesFuturo<=4) || (anoFuturo-anoAtual > 1))){
						ano = ", "+anoFuturo;
					}else{
						ano = "";
						dia = dia + (((diaFuturo < 10 || diaFuturo > 20) &&  dia.endsWith("1")) ? "st" : ((diaFuturo < 10 || diaFuturo > 20) && dia.endsWith("2") )? "nd": (((diaFuturo < 10 || diaFuturo > 20) && dia.endsWith("3"))? "rd": "th"));
					}
					return The.concat("on ", mes, " ", dia, ano);
				}

			}
		}
	}
}