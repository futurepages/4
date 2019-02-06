package org.futurepages.formatters;

import org.apache.commons.lang.WordUtils;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.Is;

import java.util.Locale;

/**
 * Capitalize the first character of each word.
 * .
 */
public class CapitalizeFormatter extends AbstractFormatter<String> {
    
    public String format(String value, Locale loc) {

	    return !Is.empty(value)?WordUtils.capitalize(value):"";

    }
}