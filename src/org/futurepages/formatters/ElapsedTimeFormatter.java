package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.enums.UnitTimeEnum;
import org.futurepages.util.BrazilianCalendarUtil;
import org.futurepages.util.BrazilianDateUtil;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.StringUtils;

public class ElapsedTimeFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		try {
			if(BrazilianCalendarUtil.isNeighborDays(momentoNoPassado, agora) && BrazilianCalendarUtil.getDifferenceInDays(momentoNoPassado, agora)>0){
					return "ontem às "+ BrazilianDateUtil.viewDateTime(momentoNoPassado, "HH:mm");
			}else{
				int[] time = BrazilianCalendarUtil.getElapsedTime(momentoNoPassado, agora);
				return "há ± " + BrazilianCalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
			}
		} catch (BrazilianCalendarUtil.TooBigDateException e) {
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