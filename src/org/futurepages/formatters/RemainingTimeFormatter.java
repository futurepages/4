package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.enums.UnitTimeEnum;
import org.futurepages.util.brazil.CalendarUtil;
import org.futurepages.util.brazil.DateUtil;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.StringUtils;

//TODO - falta contemplar o "amanhã"
public class RemainingTimeFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoFuturo){
		try {
			if(CalendarUtil.isNeighborDays(momentoNoFuturo, agora)){
					return "amanhã às "+ DateUtil.viewDateTime(momentoNoFuturo, "HH:mm");
			}else{
				int[] time = CalendarUtil.getElapsedTime(momentoNoFuturo, agora);
				CalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
				return "hoje às " + DateUtil.viewDateTime(momentoNoFuturo, "HH:mm");
			}
		} catch (CalendarUtil.TooBigDateException e) {
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
			return StringUtils.concat("em ",dia," de ",mes, ano);
		}
	}
}