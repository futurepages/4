package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
 public class NoSpecialsFormatter extends AbstractFormatter {
 	
 	public String format(Object value, Locale loc) {
            return ((String)value).replace(".", "_");
 	}
 }