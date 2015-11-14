package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.annotations.TagAttributeOverride;
import org.futurepages.menta.annotations.TagAttributeOverrideArray;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
@TagAttributeOverrideArray({@TagAttributeOverride(name = "max")})
public class Cookie extends PrintTag{

	@TagAttribute(required = true)
	private String key;

	@TagAttribute(required = false)
	private boolean user = false;

	@TagAttribute(required = false)
	private String defaultValue;

	@Override
	public String getStringToPrint() throws JspException {
		String cookieName = key;
		if(user){
			DefaultUser loggedUser = this.action.loggedUser();
			if(loggedUser != null){
				cookieName = key + "_"+ loggedUser.getLogin();
			}
		}
		Object result = this.action.getCookies().getAttribute(cookieName);

		if(result != null){
			return result.toString();
		}
		return defaultValue == null ? "" : defaultValue;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}