package org.futurepages.menta.tags.core.webcomponent;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class Footer extends SimpleTagSupport {


	private WebContainer myContainer;

	@Override
	public void doTag() throws JspException, IOException {
		StringWriter evalResult = new StringWriter(); //escreve o body da tag
		if (getJspBody() != null) {
			getJspBody().invoke(evalResult);
			getMyContainer();
			if (myContainer != null) {
				myContainer.addFooterContent(evalResult.getBuffer().toString());
			}else{
				getJspContext().getOut().print(evalResult.getBuffer().toString());
			}
		}
	}

	private WebContainer getMyContainer() {
		if (myContainer == null) {
			myContainer = WebContainer.get();
		}
		return myContainer;
	}
}