package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.util.SEOUtil;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formatação de texto para SEO URL.
 *
 * Exemplo: Maria Mãe de José --> maria-mae-de-jose.html
 *  
 * - saída: DD/MM/YYYY HH:MM:SS
 *
 */
 public class SEOURLFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return SEOUtil.urlFormat(((String) value).trim());
 	}
 }