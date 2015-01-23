package org.futurepages.tags;

import org.apache.taglibs.standard.tag.common.core.SetSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.annotations.TagAttributeOverride;
import org.futurepages.annotations.TagAttributeOverrideArray;
import org.futurepages.core.tags.build.ContentTypeEnum;

/**
 * @author Leandro
 * // pageContext.setAttribute(var, value);
 */
@Tag(bodyContent = ContentTypeEnum.JSP)
@TagAttributeOverrideArray({
	@TagAttributeOverride(name = "var", 	tagAttribute = @TagAttribute(name="var", rtexprvalue =false)),
	@TagAttributeOverride(name = "value", 	tagAttribute = @TagAttribute(name  = "value", rtexprvalue=true)),
	@TagAttributeOverride(name = "target",	tagAttribute = @TagAttribute(name  = "target")),
	@TagAttributeOverride(name = "property",tagAttribute = @TagAttribute(name  = "property")),
	@TagAttributeOverride(name = "scope", 	tagAttribute = @TagAttribute(name  = "scope", required = false))
})
public class Set extends SetSupport {

	public void setValue(Object v) {
		this.value = v;
		this.valueSpecified = true;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}