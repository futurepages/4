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
public class IsEven extends ConditionalTag {
    
    @TagAttribute
	private String test = null;
    
    public void setTest(String test) {
    	
        this.test = test;
        
    }
    
    private int getNumber(String s) throws JspException {
    	
    	try {
    		
    		return Integer.parseInt(s);
    		
    	} catch(NumberFormatException e) {
    		
    		throw new JspException("Cannot convert to number inside isEven tag: " + s);
    	}
    }
    
    public boolean testCondition() throws JspException {
        
		if (test != null) {
			
            Object obj = Out.getValue(test, pageContext, false);
            
            if (obj == null) throw new JspException("Cannot find value for isEven tag: " + test);
            
            return getNumber(obj.toString()) % 2 == 0;
			
		}
		
		Tag parent = findAncestorWithClass(this, Context.class);		
        
        if (parent == null) {
        	
        	// value was null and I cannot find a context for tag!
        	
        	throw new JspException("IsEven: Could not find context!");
        	
        }
            
		Context tag = (Context) parent;
		
		Object obj = tag.getObject();
		
		if (obj == null) throw new JspException("IsEven: context returned null!");
		
		return getNumber(obj.toString()) % 2 == 0;
            
	}
}

    