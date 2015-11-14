package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.Context;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Sergio Oliveira
 */
@org.futurepages.menta.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
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
			
            Object obj = PrintTag.getValue(test, pageContext, false);
            
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

    