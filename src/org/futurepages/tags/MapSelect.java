package org.futurepages.tags;

import java.util.Map;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.HTMLTag;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class MapSelect extends HTMLTag {

	@TagAttribute
    private Map map;
    
    @TagAttribute
    private String selected = null;

    @TagAttribute
    private String id = "";
    
    @TagAttribute
    private String cssClass = "";
    
    @TagAttribute(required = true)
    private String name = "";
    
    @TagAttribute
    private String style = "";
    
    @TagAttribute
    private String onblur = "";
    
    @TagAttribute
    private String onchange = "";
    
    @TagAttribute
    private String defaultText = null;
    
    @TagAttribute
    private String defaultValue = "0";

	@Override
    public String getStringToPrint() throws JspException {
        StringBuilder sb = new StringBuilder();
        sb.append("<select ").append(cssClass).append(" ").append(id).append(" name=\"").append(name).append("\" ").append(onchange).append(" ").append(style).append(" ").append(onblur).append(" ").append(getExtraAttributes()).append(">");

        if (map != null) {
			
            if (defaultText != null) {
                sb.append("<option value=\"").append(defaultValue).append("\">").append(defaultText).append("</option>");
            }

                for (Object object : map.keySet()) {

                    sb.append("<option value=\"").append(object.toString()).append("\"");

                    if (selected.equals(object.toString())) {
                        sb.append(" selected=\"selected\" ");
                    }
                    sb.append(">");
					sb.append(map.get(object).toString());
                    sb.append("</option>");
                }
        }
        sb.append("</select>");
		
        return sb.toString();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = "style=\"" + style + "\"";
    }

    public void setOnblur(String onblur) {
        this.onblur = "onblur=\"" + onblur + "\"";
    }

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setMap(Map map) {
		this.map = map;
	}

    @Override
    public void setId(String id) {
        this.id = "id=\"" + id + "\"";
    }


    public void setOnchange(String onchange) {
        this.onchange = "onchange=\"" + onchange + "\"";
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

}