package org.futurepages.tags.core.conditional;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Else extends ConditionalContextTag {
	
	@Override
	protected boolean contextualCondition(ConditionalTag conditional) {
		return  !conditional.isCondition();
	}
}
