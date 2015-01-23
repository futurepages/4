package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.action.Action;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.html.HtmlMapChars;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Error extends PrintTag {

	public String getStringToPrint() throws JspException {

        if (action == null){
            return HtmlMapChars.htmlValue(req.getParameter(Action.ERROR));
        } else {
			String error = action.getError();
			if(error == null){ //então error vem do input
				error = HtmlMapChars.noHtmlTags((String)action.getInput().getValue(Action.ERROR));
			}
        return (error!=null)? error : "";
		}
	}
}