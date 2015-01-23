package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.action.Action;
import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

/**
 * Verifica se existe erro na tag.
 * 
 * @author Leandro Santana Pereira
 */
@Tag(bodyContent =ContentTypeEnum.JSP)
public class HasError extends ConditionalTag {

	@Override
	public boolean testCondition() throws JspException {
		if(action!=null){
			return action.hasError();
		} else {
			return req.getParameter(Action.ERROR) != null;
		}
	}
}