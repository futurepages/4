package org.futurepages.formatters.brazil;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.CalendarUtil;

import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author leandro
 */
public class AnniversaryFormatter  extends AbstractFormatter<Calendar> {

    public String format(Calendar value, Locale loc) {
        return CalendarUtil.format(value, "dd/MM");
    }

}
