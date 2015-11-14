package org.futurepages.menta.tags.core.template;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.template.Page;
import org.futurepages.menta.core.template.TemplateServlet;

import javax.servlet.jsp.JspException;

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