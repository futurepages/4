package org.futurepages.formatters.brazil;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.brazil.BrazilDateUtil;

/**
 *
 * @author leandro
 */
public class AnniversaryFormatter  extends AbstractFormatter<Calendar> {

    public String format(Calendar value, Locale loc) {
        return BrazilDateUtil.format(value, "dd/MM");
    }

}
