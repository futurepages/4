package org.futurepages.menta.tags.core;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.annotations.TagAttributeOverride;
import org.futurepages.menta.annotations.TagAttributeOverrideArray;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
@TagAttributeOverrideArray({@TagAttributeOverride (name = "max")})
public class App extends PrintTag{
	
	@TagAttribute
    private String attribute;
	
	@TagAttribute
    private String param;
    
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setParam(String param) {
        this.param = param;
    }
    
    public String getStringToPrint() throws JspException {
        if(param!=null){
            try {
               return Apps.get(param);
            } catch (Exception ex) {
                System.out.println("erro: "+ex);
            }
        }
        else if(attribute!=null){
           return (String) application.getAttribute(attribute);
        }
        return "";
    }
}