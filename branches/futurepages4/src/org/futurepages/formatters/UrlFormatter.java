package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.The;

public class UrlFormatter extends AbstractFormatter<String> {

	private boolean contains(String src, String compare) {
		if (src.length() > compare.length()) {
			for (int i = 0; i < compare.length(); i++) {
				if (src.charAt(i) != compare.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String format(String value, Locale loc) {

		if (value != null && !value.equals("")) {
			boolean contemHttp = contains(value.toLowerCase(), "http://");
			boolean contemHttps = contains(value.toLowerCase(), "https://");

			if (!(contemHttp || contemHttps)) {
				return The.concat("http://", value);
			}

			return value;
		}

		return "";
	}
}
