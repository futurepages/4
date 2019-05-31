package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;

public class ElapsedTimeFormatter extends AbstractFormatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado, null,null);
	}

	@Override
	public String format(Calendar momentoNoPassado, Locale loc, String param) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado, Integer.valueOf(param), null);
	}

	@Override
	public String format(Calendar momentoNoPassado, Locale loc, String[] params) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado, Integer.valueOf(params[0]), params[1]);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado, Integer daysCountup, String lang){
		boolean en = !Is.empty(lang) && lang.equals("en");
		try {
			if(BrazilCalendarUtil.isNeighborDays(momentoNoPassado, agora) && BrazilCalendarUtil.getDifferenceInDays(momentoNoPassado, agora)>0){
				if(!en){
					return "ontem às "+ DateUtil.getInstance().viewDateTime(momentoNoPassado, "HH:mm");
				}else{
					return "yesterday at "+ DateUtil.getInstance().viewDateTime(momentoNoPassado, "hh:mm a");
				}
			}else{
			int[] time = BrazilCalendarUtil.getElapsedTime(momentoNoPassado, agora);
				if(!en){
					return "há ± " + BrazilCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
				}else{
					return "± " + BrazilCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false)+ " ago";
				}
			}
		} catch (BrazilCalendarUtil.TooBigDateException e) {
			int qtdDias;
			if(daysCountup!=null && daysCountup >= (qtdDias= CalendarUtil.getDifferenceInAbsoluteDays(agora,momentoNoPassado))){
				if(!en){
					return The.concat("há ",qtdDias," dias");
				}else{
					return The.concat(qtdDias," days ago");
				}
			}else{
				int mesAtual = agora.get(Calendar.MONTH)+1;
				int anoAtual = agora.get(Calendar.YEAR);
				int diaPassado = momentoNoPassado.get(Calendar.DAY_OF_MONTH);
				int mesPassado = momentoNoPassado.get(Calendar.MONTH)+1;
				int anoPassado = momentoNoPassado.get(Calendar.YEAR);
				if(!en){
					String dia = (diaPassado==1? "1º": String.valueOf(diaPassado));
					String mes = MonthEnum.get(mesPassado);
					String ano;
					if((anoPassado==anoAtual) ||
							((anoAtual-anoPassado == 1) && (mesAtual==1) && (mesPassado==12))){
						ano = "";
					}else{
						ano = " de "+anoPassado;
					}
					return The.concat("em ", dia, " de ", mes, ano);
				}else{
					String mes = The.capitalizedWord(MonthEnum.values()[mesPassado - 1].name().toLowerCase());
					String dia = String.valueOf(diaPassado);
					dia = dia + (((diaPassado < 10 || diaPassado > 20) &&  dia.endsWith("1")) ? "st" : ((diaPassado < 10 || diaPassado > 20) && dia.endsWith("2") )? "nd": (((diaPassado < 10 || diaPassado > 20) && dia.endsWith("3"))? "rd": "th"));
					String ano;
					if(anoPassado!=anoAtual){
						ano = ", "+anoPassado;
					}else{
						ano = "";
					}
					return The.concat("on ", mes, " ", dia, ano);
				}
			}
		}
	}
}