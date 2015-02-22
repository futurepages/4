package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.CalendarUtil;
import org.futurepages.util.brazil.DateUtil;

public class AnniversaryAbbrFormatter extends AbstractFormatter<Calendar> {

	@Override
    public String format(Calendar value, Locale loc) {
		return DateUtil.format(value, "dd")+" "+ CalendarUtil.getMonthAbbr(value);
    }
}