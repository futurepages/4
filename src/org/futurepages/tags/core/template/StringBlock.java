package org.futurepages.tags.core.template;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.template.Page;
import org.futurepages.core.template.TemplateServlet;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class StringBlock extends PrintTag {

	@TagAttribute(required  = true)
	private String id;

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getStringToPrint() throws JspException {
	  Page page = (Page)req.getAttribute(TemplateServlet.PAGE_ATTR);
	  String stringBlock = page.getStringBlock(id);
	  return stringBlock;
	}
}