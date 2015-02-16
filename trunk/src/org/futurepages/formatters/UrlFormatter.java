package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.The;

public class UrlFormatter implements Formatter {

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
	public String format(Object value, Locale loc) {
		String url = (String) value;

		if (url != null && !url.equals("")) {
			boolean contemHttp = contains(url.toLowerCase(), "http://");
			boolean contemHttps = contains(url.toLowerCase(), "https://");

			if (!(contemHttp || contemHttps)) {
				return The.concat("http://", url);
			}

			return url;
		}

		return "";
	}
}
