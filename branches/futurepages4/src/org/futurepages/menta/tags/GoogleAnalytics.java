package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

/**
 *
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.JSP)
public class GoogleAnalytics extends PrintTag{
   
	@TagAttribute(required = true)
    private String account_id;
    
    public String getStringToPrint() throws JspException {
       
       StringBuffer sb = new StringBuffer();
       sb.append
       (
			   "<!-- Google Analytics -->" +
               "<script src='http://www.google-analytics.com/urchin.js' type='text/javascript'></script>" +
               "<script type='text/javascript'>" +
               "_uacct = '"+account_id+"';" +
               "urchinTracker();" +
               "</script>"
        );

        return sb.toString();
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
}
