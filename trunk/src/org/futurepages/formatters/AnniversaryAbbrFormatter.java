package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.BrazilianCalendarUtil;
import org.futurepages.util.BrazilianDateUtil;

/**
 *
 * @author leandro
 */
public class AnniversaryAbbrFormatter  implements Formatter {

    public String format(Object value, Locale loc) {
		Calendar cal = (Calendar) value;

		String retornoFormater = BrazilianDateUtil.format(cal, "dd")+" "+ BrazilianCalendarUtil.getMonthAbbr(cal);

		return retornoFormater;
    }
}