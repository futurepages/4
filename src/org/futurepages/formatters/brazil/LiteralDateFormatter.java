package org.futurepages.formatters.brazil;

import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.brazil.BrazilDateUtil;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.enums.MonthEnum;

/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateFormatter extends AbstractFormatter<Calendar> {
 	
 	public String format(Calendar value, Locale loc) {
            return BrazilDateUtil.literalDateFromDB(value);
 	}

	@Override
	public String format(Calendar value, Locale locale, String param) {
		if(Is.empty(param) || !param.equals("en")){
			return format(value, locale);
		}


		int diaInput = value.get(Calendar.DAY_OF_MONTH);
		int mesInput = value.get(Calendar.MONTH) + 1;
		int anoInput = value.get(Calendar.YEAR);


		String diaStr = String.valueOf(diaInput);
		String dia = diaInput + (((diaInput< 10 || diaInput> 20) &&  diaStr.endsWith("1")) ? "st" : ((diaInput< 10 || diaInput> 20) && diaStr.endsWith("2") )? "nd": (((diaInput< 10 || diaInput> 20) && diaStr.endsWith("3"))? "rd": "th"));

		String ano = ", " + anoInput;
		return The.capitalizedWord(MonthEnum.values()[mesInput - 1].name().toLowerCase())+" "+dia+ano;
	}
}