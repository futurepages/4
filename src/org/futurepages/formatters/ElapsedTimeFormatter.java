package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.util.brazil.enums.UnitTimeEnum;
import org.futurepages.util.brazil.CalendarUtil;
import org.futurepages.util.brazil.DateUtil;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.brazil.enums.MonthEnum;
import org.futurepages.util.StringUtils;

public class ElapsedTimeFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		try {
			if(CalendarUtil.isNeighborDays(momentoNoPassado, agora) && CalendarUtil.getDifferenceInDays(momentoNoPassado, agora)>0){
					return "ontem às "+ DateUtil.viewDateTime(momentoNoPassado, "HH:mm");
			}else{
				int[] time = CalendarUtil.getElapsedTime(momentoNoPassado, agora);
				return "há ± " + CalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
			}
		} catch (CalendarUtil.TooBigDateException e) {
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
			return StringUtils.concat("em ",dia," de ",mes, ano);
		}
	}
}