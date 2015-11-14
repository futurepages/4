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
public class Block extends PrintTag {

	@TagAttribute(required = true, name="id")
	private String idX; //@TODO - Excluir após contemplar herança de atributos
	
	@Override
	public String getStringToPrint() throws JspException {
		Page page = (Page) req.getAttribute(TemplateServlet.PAGE_ATTR);
		String view = null;
		if(page.isWithRule() && id.equals("body")) {
				view = ((String)req.getAttribute(TemplateServlet.CURRENT_PATH)) + ".jsp";
		} else {
			view = page.getBlock(id).getView();
		}

		try {
			pageContext.include("/" + view, false);
		} catch (Exception ex) {
			throw new JspException(ex);
		}
		return "";
	}
}