package org.futurepages.formatters.brazil;

import org.futurepages.util.brazil.BrazilDateUtil;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class LiteralDateFormatter extends AbstractFormatter<Calendar> {
 	
 	public String format(Calendar value, Locale loc) {
            return BrazilDateUtil.literalDateFromDB(value);
 	}
 }