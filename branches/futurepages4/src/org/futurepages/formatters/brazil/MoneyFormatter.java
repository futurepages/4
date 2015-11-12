package org.futurepages.formatters.brazil;

import java.math.BigDecimal;
import org.futurepages.util.brazil.MoneyUtil;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;

/**
 * Formata o double em formato de dinheiro local.
 */
public class MoneyFormatter extends AbstractFormatter {

	@Override
	public String format(Object value, Locale loc) {
		if (value instanceof Float) {
			return MoneyUtil.moneyFormat(((Float)value).doubleValue());
		} else if(value instanceof Double) {
			return MoneyUtil.moneyFormat((Double) value);
		} else {
			return MoneyUtil.moneyFormat((BigDecimal) value);
		}
	}
}
