package org.futurepages.tags.core.paths;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ThemePath extends PrintTag{
    
    public String getStringToPrint() throws JspException {
        return Paths.theme(req);
    }    
}
