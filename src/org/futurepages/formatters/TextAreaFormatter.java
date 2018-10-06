package org.futurepages.formatters;

import org.futurepages.util.EmojiUtil;
import org.futurepages.util.html.HtmlMapChars;
import java.util.Locale;
import org.futurepages.core.formatter.AbstractFormatter;
 
/**
 * Formata o texto escrito em textaerea em texto com quebras de linhas de html.
 */
 public class TextAreaFormatter extends AbstractFormatter {
 	
	@Override
 	public String format(Object value, Locale loc) {
            return HtmlMapChars.textAreaValue(EmojiUtil.decodeAll((String) value));
 	}
 }