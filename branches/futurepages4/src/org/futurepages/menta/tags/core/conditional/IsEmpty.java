package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.ListContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Sergio Oliveira
 */
@org.futurepages.menta.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class IsEmpty extends ConditionalTag {
	
	@TagAttribute
	private String test = null;
	
	public void setTest(String test) {
		this.test = test;
	}
    
    public boolean testCondition() throws JspException {
    	
		Tag parent = findAncestorWithClass(this, ListContext.class);
		
        if (parent != null && test == null) {
        	
			ListContext tag = (ListContext) parent;
            List<Object> list = (List<Object>) pageContext.getAttribute(tag.getVar());
			return list == null || list.size() == 0;
			
		} else {
			
			if (test != null) {
				
				Object value = PrintTag.getValue(test, pageContext, false);
				
				if (value == null) return true; // should do this ???
				
				if (value instanceof Collection) {
					
					Collection col = (Collection) value;
					
					return col.isEmpty();
					
				} else if (value instanceof Map) {
					
					Map map = (Map) value;
					
					return map.isEmpty();
					
				} else if (value instanceof Object[]) {
					
					Object[] array = (Object[]) value;
					
					return array.length == 0;
					
				}
				
				throw new JspException("IsEmpty: Value " + test + " is not a Collection, Array or a Map!");
				
			}
		}

		throw new JspException("IsEmpty: Could not find list context !!!");
	}
}