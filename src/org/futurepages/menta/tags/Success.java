package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.html.HtmlMapChars;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Success extends PrintTag{
    
    public String getStringToPrint() throws JspException {
        if (action == null){
                return HtmlMapChars.htmlValue(req.getParameter(Action.SUCCESS));
        }

        String success = (String) action.getSuccess();

        if(success==null){ //então success vem do input
            success = (String) HtmlMapChars.noHtmlTags((String) action.getInput().getValue(Action.SUCCESS));
        }
        return (success!=null)? success : "";
    }
}
