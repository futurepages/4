package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

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