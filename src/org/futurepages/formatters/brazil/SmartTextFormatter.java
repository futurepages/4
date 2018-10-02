package org.futurepages.formatters.brazil;

import org.futurepages.util.EmojiUtil;
import org.futurepages.util.The;
import org.futurepages.util.html.HtmlMapChars;
import java.util.Locale;
import org.futurepages.core.config.Apps;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.html.HtmlRegex;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

/**
 * Formata o texto escrito em textaerea em texto com quebras de linhas de html.
 */
public class SmartTextFormatter extends AbstractFormatter {

	private static final int MAX_CHARS = 28;

	@Override
	public String format(Object value, Locale loc) {
		String result =  HtmlMapChars.plainTextValue(EmojiUtil.decodeAll((String) value));


		IterableString iter = new IterableString(HtmlRegex.getCompiledUrlPattern(), result);
		StringBuilder sb     = new StringBuilder();
		String end = result;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			String url = token.getMatched();
			sb.append("<a href=\"")
					.append(url.startsWith("www.")?"http://"+url:url)
					.append(url.startsWith(Apps.get("APP_HOST"))?"\"":"\" target=\"_blank\"")
					.append(" title=\"")
					.append(url).append("\">")
					.append(shortUrl(url))
					.append("</a>");
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	public static String shortUrl(String url){
		if(url.startsWith("http://")){
			url = url.substring(7);
		}else if(url.startsWith("https://")){
			url = url.substring(8);
		}
		if(url.length()>MAX_CHARS){
			if(url.length()<=MAX_CHARS+8){
				return url;
			}else{
				String sufix = url.substring(url.length()-8,url.length());
				return The.concat(url.substring(0, MAX_CHARS), "...", sufix);
			}
		}else{
			return url;
		}
	}
}