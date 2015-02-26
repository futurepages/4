package org.futurepages.core.locale;

public class TxtNotFoundException extends Throwable {
	public TxtNotFoundException(String txtKey, String localeId) {
		super("Txt property '" + txtKey + "' not present for locale " + localeId + ".");
	}
}
