package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;

/**
 *
 * @author leandro
 */
public class AnniversaryAbbrFormatter  implements Formatter {

    public String format(Object value, Locale loc) {
		Calendar cal = (Calendar) value;

		String retornoFormater = DateUtil.format(cal, "dd")+" "+CalendarUtil.getMonthAbbr(cal);

		return retornoFormater;
    }
}