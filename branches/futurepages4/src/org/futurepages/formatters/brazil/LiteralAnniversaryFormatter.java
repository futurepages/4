package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;
import org.futurepages.util.brazil.enums.MonthEnum;

/**
 *
 * @author leandro
 */
public class LiteralAnniversaryFormatter extends AbstractFormatter {

	@Override
    public String format(Object value, Locale loc) {
    	String retornoFormater = "";
    	if(value!= null){
    		Calendar cal = (Calendar) value;
			int dia = cal.get(Calendar.DAY_OF_MONTH);
    		retornoFormater = The.concat(dia, (dia == 1 ? "º de " : " de "), MonthEnum.get(cal));
    	}
		return retornoFormater;
    }

}
