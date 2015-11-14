package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.html.HtmlMapChars;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Error extends PrintTag {

	public String getStringToPrint() throws JspException {

        if (action == null){
            return HtmlMapChars.htmlValue(req.getParameter(Action.ERROR));
        } else {
			String error = action.getError();
			if(error == null){ //então error vem do input
				error = HtmlMapChars.noHtmlTags((String) action.getInput().getValue(Action.ERROR));
			}
        return (error!=null)? error : "";
		}
	}
}