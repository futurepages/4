
package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.action.Action;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.JSP)
public class HasSuccess extends ConditionalTag {

	public boolean testCondition() throws JspException {
		if(action!=null){
			return action.hasSuccess();
		} else {
			return req.getParameter(Action.SUCCESS) != null;
		}
	}
}