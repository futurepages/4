package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

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
