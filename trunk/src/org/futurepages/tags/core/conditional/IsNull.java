package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.Context;
import org.futurepages.tags.Out;

/**
 * @author Sergio Oliveira
 */
@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class IsNull extends ConditionalTag {
    
	@TagAttribute
    private String test = null;
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public boolean testCondition() throws JspException {
        
		if (test != null) {
			
            Object value = Out.getValue(test, pageContext, false);
            
            if (value == null) return true;
            
            return false;
			
		}
		
		Tag parent = findAncestorWithClass(this, Context.class);		
        
        if (parent == null) {
        	
        	// test was null and I cannot find a context for tag!
        	
        	throw new JspException("IsNull: Could not find context!");
        	
        }
            
		Context tag = (Context) parent;
		
		return tag.getObject() == null;
            
	}
}

    