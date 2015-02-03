package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.brazil.DateUtil;

/**
 *
 * @author leandro
 */
public class AnniversaryFormatter  implements Formatter<Object> {

    public String format(Object value, Locale loc) {
        Calendar cal = (Calendar) value;
		String retornoFormater = DateUtil.format(cal, "dd/MM");
		return retornoFormater;
    }

}
