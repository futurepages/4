package org.futurepages.menta.tags.core.paths;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.core.path.Paths;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class TemplatePath extends PrintTag{

	@TagAttribute
	private String module;

	public void setModule(String module) {
		this.module = module;
	}
    
    public String getStringToPrint() throws JspException {
		return Paths.template(req, module);
    }
}