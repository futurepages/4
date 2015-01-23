package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.Is;

/**
 * @author Sergio Oliveira, Modified by Leandro
 */
@Tag(bodyContent = ContentTypeEnum.JSP)
public class IsEmptyStr extends ConditionalTag {
    
	@TagAttribute
    private String test;
    
    public void setTest(String test) {
        this.test = test;
    }
    
	@Override
    public boolean testCondition() throws JspException {
    	return (Is.empty(findValue(test)));
    }
}