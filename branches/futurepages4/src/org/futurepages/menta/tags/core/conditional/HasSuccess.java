
package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

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