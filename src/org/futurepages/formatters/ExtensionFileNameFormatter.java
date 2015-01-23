package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.FileUtil;
 
/**
 * Formata a data no formato DD/MM/YYYY
 */
 public class ExtensionFileNameFormatter implements Formatter<String> {
 	
	@Override
 	public String format(String value, Locale loc) {
            return FileUtil.extensionFormat(value);
 	}
 }