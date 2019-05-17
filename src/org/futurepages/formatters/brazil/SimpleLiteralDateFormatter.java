package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.brazil.enums.MonthEnum;

import java.util.Calendar;
import java.util.Locale;

public class SimpleLiteralDateFormatter extends AbstractFormatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado,null);
	}

	@Override
	public String format(Calendar momentoNoPassado,Locale loc, String param) {
		Calendar agora = CalendarUtil.now();
		return formatValue(agora, momentoNoPassado,param);
	}

	public static String formatValue(Calendar agora, Calendar momentoInput, String param){
		int anoAtual = agora.get(Calendar.YEAR);
		int diaInput = momentoInput.get(Calendar.DAY_OF_MONTH);
		int mesInput = momentoInput.get(Calendar.MONTH) + 1;
		int anoInput = momentoInput.get(Calendar.YEAR);

		if(Is.empty(param) || !param.equals("en")){
			String dia = (diaInput == 1 ? "1º" : String.valueOf(diaInput));
			String mes = MonthEnum.get(mesInput);
			String ano = "";
			if ((anoInput != anoAtual)) {
				ano = " de " + anoInput;
			}
			return The.concat(dia, " de ", mes, ano);
		}else{
			String diaStr = String.valueOf(diaInput);
			String dia = diaInput + (((diaInput< 10 || diaInput> 20) &&  diaStr.endsWith("1")) ? "st" : ((diaInput< 10 || diaInput> 20) && diaStr.endsWith("2") )? "nd": (((diaInput< 10 || diaInput> 20) && diaStr.endsWith("3"))? "rd": "th"));

			String ano = "";
			if ((anoInput != anoAtual)) {
				ano = ", " + anoInput;
			}
			return The.capitalizedWord(MonthEnum.values()[mesInput - 1].name().toLowerCase())+" "+dia+ano;
		}
	}
}