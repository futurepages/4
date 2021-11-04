package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.core.formatter.AbstractFormatter;
//import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.menta.core.formatter.FormatterManager;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.EmojiUtil;
import org.futurepages.util.Is;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ValueFormatter extends PrintTag {

	private static Pattern PARAM_PATTERN = Pattern.compile("\\[.*?\\]");

	@TagAttribute(required = true)
	private Object object;
	@TagAttribute
	private String formatter;

	@Override
	public String getStringToPrint() throws JspException {
		if (!Is.empty(formatter)) {

			String[] params = extractParams(formatter);
			if (params.length>0) {
				formatter = formatter.substring(0, formatter.indexOf(params[0]) - 1);
			}
			AbstractFormatter f = FormatterManager.getFormatter(formatter);
			if (f == null) {
				throw new JspException("Cannot find formatter: " + formatter);
			}

			if (params.length==1) {
				return ((AbstractFormatter)f).format(object, action.getLocale(), params[0]);
			}else if(params.length>1){
				return ((AbstractFormatter)f).format(object, action.getLocale(), params);
			}

			return f.format(object, action.getLocale());
		} else {
			return (object != null) ? EmojiUtil.decodeAll(object.toString()) : "";
		}
	}

	public String[] extractParams(String nameFormatter) {
		IterableString it = new IterableString(PARAM_PATTERN.pattern(), nameFormatter);
		List<String> tokens = new ArrayList<>();
		for (MatchedToken token : it) {
			tokens.add(token.getMatched().substring(1, token.getMatched().length()-1));
		}
		String[] array = new String[tokens.size()];
		tokens.toArray(array);
		return array ;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}