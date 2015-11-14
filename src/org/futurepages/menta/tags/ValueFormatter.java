package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.Is;

import javax.servlet.jsp.JspException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		if (!Is.empty(formatter)) {

			String param = extractParam(formatter);
			if (!Is.empty(param)) {
				formatter = formatter.substring(0, formatter.indexOf(param) - 1);
			}
			AbstractFormatter f = FormatterManager.getFormatter(formatter);
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