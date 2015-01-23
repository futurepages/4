package org.futurepages.tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.util.Is;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ValueFormatter extends PrintTag {

	private static Pattern PARAM_PATTERN = Pattern.compile("\\[(.*?)\\]");

	@TagAttribute(required = true)
	private Object object;
	@TagAttribute
	private String formatter;

	@Override
	public String getStringToPrint() throws JspException {
		if (formatter != null) {

			String param = extractParam(formatter);
			if (!Is.empty(param)) {
				formatter = formatter.substring(0, formatter.indexOf(param) - 1);
			}
			Formatter f = FormatterManager.getFormatter(formatter);
			if (f == null) {
				throw new JspException("Cannot find formatter: " + formatter);
			}

			if (!Is.empty(param)) {
				return ((AbstractFormatter)f).format(object, action.getLocale(), param);
			}

			return f.format(object, action.getLocale());
		} else {
			return (object != null) ? object.toString() : "";
		}
	}

	public String extractParam(String nameFormatter) {
		Matcher matcher = PARAM_PATTERN.matcher(nameFormatter);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}