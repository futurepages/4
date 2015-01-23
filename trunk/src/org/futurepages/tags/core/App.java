package org.futurepages.tags.core;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.annotations.TagAttributeOverride;
import org.futurepages.annotations.TagAttributeOverrideArray;
import org.futurepages.core.config.Params;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

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
               return Params.get(param);
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