package org.futurepages.tags;

import java.net.URL;
import java.net.URLConnection;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.FileUtil;

/**
 * Inclui o corpo html retornado por uma requisição http a uma url.
 * @author leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class IncludeExternal extends PrintTag {

	@TagAttribute(required=true)
	private String url;

	public void setUrl(String url){
		this.url=url;
	}

	@Override
	public String getStringToPrint() throws JspException {
        try {
            URL protectedURL = new URL(url);
            URLConnection connection = protectedURL.openConnection();
            connection.setDoInput(true);
			return FileUtil.convertStreamToString(connection.getInputStream());
        } catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex);
            return this.bodyContent.getString();
        }
	}
}