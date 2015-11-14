package org.futurepages.menta.tags.core.webcomponent;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.Is;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class Head extends SimpleTagSupport {


	private WebContainer myContainer;

	@TagAttribute(required = false)
	private String specialHeadTitle;

	@TagAttribute(required = false)
	private String bodyClasses;

	@Override
	public void doTag() throws JspException, IOException {
		getMyContainer();
		if (myContainer != null) {
				StringWriter evalResult = new StringWriter(); //escreve o body da tag
				if(getJspBody()!=null){
					getJspBody().invoke(evalResult);
					myContainer.addHeadContent(evalResult.getBuffer().toString());
				}
				if (!Is.empty(specialHeadTitle)) {
					myContainer.addSpecialHeadContent(specialHeadTitle);
				}
				if (!Is.empty(bodyClasses)) {
					myContainer.addBodyClasses(bodyClasses);
				}
		}
	}

	public String getSpecialHeadTitle() {
		return specialHeadTitle;
	}

	public void setSpecialHeadTitle(String specialHeadTitle) {
		this.specialHeadTitle = specialHeadTitle;
	}

	private WebContainer getMyContainer() {
		if (myContainer == null) {
			myContainer = WebContainer.get();
		}
		return myContainer;
	}

	public void setBodyClasses(String bodyClasses) {
		this.bodyClasses = bodyClasses;
	}
}