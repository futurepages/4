package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

@org.futurepages.menta.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Then extends ConditionalContextTag{

	@Override
	protected boolean contextualCondition(ConditionalTag conditional) {
		return conditional.isCondition();
	}

}