package org.futurepages.tags.core;

import org.futurepages.tags.Out;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.AbstractContext;
import org.futurepages.core.tags.cerne.Context;

/**
 * @author Sergio Oliveira
 * @deprecated
 */
@Deprecated
@org.futurepages.annotations.Tag(bodyContent=ContentTypeEnum.JSP)
public class Bean extends AbstractContext {
	
	@TagAttribute(required = true)
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
    
	@Override
    protected String getName() {
        return value;
    }
	
	@Override
	public Object getObject() throws JspException {
		
        Tag parent = findAncestorWithClass(this, Context.class);
        
        if (parent != null) {
        	
            Context ctx = (Context) parent;
            
            Object obj = ctx.getObject();
            
            if (obj != null) {
            	
                Object object = Out.getValue(obj, value, false);
                
                if (object != null) {
                    return object;
                }
            }
        }
        return Out.getValue(value, pageContext, false);
    }
}