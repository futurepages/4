package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.FileUtil;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class ExtensionFileNameFormatter extends AbstractFormatter<String> {
 	
	@Override
 	public String format(String value, Locale loc) {
            return FileUtil.extensionFormat(value);
 	}
 }