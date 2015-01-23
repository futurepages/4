package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.StringUtils;

/**
 *
 * @author leandro
 */
public class LiteralAnniversaryFormatter implements Formatter {

	@Override
    public String format(Object value, Locale loc) {
    	String retornoFormater = "";
    	if(value!= null){
    		Calendar cal = (Calendar) value;
			int dia = cal.get(Calendar.DAY_OF_MONTH);
    		retornoFormater = StringUtils.concat(dia,(dia==1?"º de ":" de "),MonthEnum.get(cal));
    	}
		return retornoFormater;
    }

}
