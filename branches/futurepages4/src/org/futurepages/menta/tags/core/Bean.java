package org.futurepages.menta.tags.core;

import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.AbstractContext;
import org.futurepages.menta.core.tags.cerne.Context;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Sergio Oliveira
 * @deprecated
 */
@Deprecated
@org.futurepages.menta.annotations.Tag(bodyContent=ContentTypeEnum.JSP)
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
            	
                Object object = PrintTag.getValue(obj, value, false);
                
                if (object != null) {
                    return object;
                }
            }
        }
        return PrintTag.getValue(value, pageContext, false);
    }
}