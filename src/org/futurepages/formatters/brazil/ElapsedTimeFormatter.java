package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.The;
import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.util.brazil.BrazilCalendarUtil;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;

public class ElapsedTimeFormatter extends AbstractFormatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado, null);
	}

	@Override
	public String format(Calendar momentoNoPassado, Locale loc, String param) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado, Integer.valueOf(param));
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado, Integer daysCountup){
		 //TODO parametro do tempo como no RemainingTImeFormatter
		try {
			if(BrazilCalendarUtil.isNeighborDays(momentoNoPassado, agora) && BrazilCalendarUtil.getDifferenceInDays(momentoNoPassado, agora)>0){
					return "ontem às "+ DateUtil.getInstance().viewDateTime(momentoNoPassado, "HH:mm");
			}else{
				int[] time = BrazilCalendarUtil.getElapsedTime(momentoNoPassado, agora);
				return "há ± " + BrazilCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
			}
		} catch (BrazilCalendarUtil.TooBigDateException e) {
			int qtdDias;
			if(daysCountup!=null && daysCountup >= (qtdDias= CalendarUtil.getDifferenceInAbsoluteDays(agora,momentoNoPassado))){
				return The.concat("há ",qtdDias," dias");
			}else{
				int mesAtual = agora.get(Calendar.MONTH)+1;
				int anoAtual = agora.get(Calendar.YEAR);
				int diaPassado = momentoNoPassado.get(Calendar.DAY_OF_MONTH);
				int mesPassado = momentoNoPassado.get(Calendar.MONTH)+1;
				int anoPassado = momentoNoPassado.get(Calendar.YEAR);
				String dia = (diaPassado==1? "1º": String.valueOf(diaPassado));
				String mes = MonthEnum.get(mesPassado);
				String ano = null;
				if((anoPassado==anoAtual) ||
				   ((anoAtual-anoPassado == 1) && (mesAtual==1) && (mesPassado==12))){
						ano = "";
				}else{
						ano = " de "+anoPassado;
				}
				return The.concat("em ", dia, " de ", mes, ano);
			}
		}
	}
}