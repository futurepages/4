package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttributeOverride;
import org.futurepages.menta.annotations.TagAttributeOverrideArray;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.HttpUtil;

import javax.servlet.jsp.JspException;

/**
 * @author Sergio Oliveira
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
@TagAttributeOverrideArray({@TagAttributeOverride(name = "max") })
public class NoCache extends PrintTag {
    
	@Override
	public String getStringToPrint() throws JspException {
		if(action.hasNoCache()){
			HttpUtil.disableCache(res);
			session.removeAttribute("noCache");
		}
        return null;
    }
}