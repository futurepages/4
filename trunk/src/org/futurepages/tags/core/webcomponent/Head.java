package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.Is;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class Head extends SimpleTagSupport {


	private WebContainer myContainer;

	@TagAttribute(required = false)
	private String specialHeadTitle;

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
}