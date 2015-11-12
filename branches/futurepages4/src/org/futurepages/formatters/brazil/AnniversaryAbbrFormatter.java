package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.brazil.BrazilDateUtil;

import java.util.Calendar;
import java.util.Locale;

public class AnniversaryAbbrFormatter extends AbstractFormatter<Calendar> {

	@Override
    public String format(Calendar value, Locale loc) {
		return BrazilDateUtil.format(value, "dd")+" "+ CalendarUtil.getMonthAbbr(value);
    }
}