package org.futurepages.formatters;

import com.google.gson.Gson;
import org.futurepages.core.formatter.AbstractFormatter;

import java.util.Locale;

public class JSONFormatter extends AbstractFormatter {

	public String format(Object value, Locale loc) {
		return new Gson().toJson(value);
	}
}