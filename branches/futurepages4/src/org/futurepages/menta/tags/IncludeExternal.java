package org.futurepages.menta.tags;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.FileUtil;

import javax.servlet.jsp.JspException;
import java.net.URL;
import java.net.URLConnection;

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
			AppLogger.getInstance().execute(ex);
            return this.bodyContent.getString();
        }
	}
}