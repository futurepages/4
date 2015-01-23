package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.util.The;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.HTMLTag;

/**
 * @author Sergio Oliveira (modificado por Leandro Santana Pereira)
 *
 * @deprecated utilize o inputText
 */
@Deprecated
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Input extends HTMLTag {

	@TagAttribute(required = true)
    private String name;
	
	@TagAttribute
    private String id = null;
	
	@TagAttribute
    private String klass = null;
	
	@TagAttribute
    private String style = null;
	
	@TagAttribute
    private String onblur = null;
    
    @TagAttribute
    private int size = -1;
    
    @TagAttribute
    private int maxlength = -1;
    
    @TagAttribute
    private String type = "text"; // default
    
    @TagAttribute
    private String value = null;

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected StringBuffer buildTag() {

        Object localValue = null;

        if (type.equalsIgnoreCase("checkbox") || type.equalsIgnoreCase("radio")) {

            localValue = findObject(name, true); // try boolean as well...

        } else {

            localValue = findObject(name, false); // no need to try boolean...
        }

        StringBuffer sb = new StringBuffer(200);
        sb.append("<input type=\"").append(type).append("\" name=\"").append(name).append("\"");
        if (size > 0) {
            sb.append(" size=\"").append(size).append("\"");
        }
        if (maxlength > 0) {
            sb.append(" maxlength=\"").append(maxlength).append("\"");
        }
        if (klass != null) {
            sb.append(" class=\"").append(klass).append("\"");
        }
        if (style != null) {
            sb.append(" style=\"").append(style).append("\"");
        }
        if (onblur != null) {
            sb.append(" onblur=\"").append(onblur).append("\"");
        }
        if (id != null) {
            sb.append(" id=\"").append(id).append("\"");
        }
        if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password") || type.equalsIgnoreCase("hidden")) {
            if (localValue != null) {
                //@ByLeandro
                String theHtmlValue = The.htmlSimpleValue(localValue.toString());
                sb.append(" value=\"").append(theHtmlValue).append("\"");
            } else if (this.value != null) {
                sb.append(" value=\"").append(this.value).append("\"");
            }
        } else if (type.equalsIgnoreCase("checkbox") || type.equalsIgnoreCase("radio")) {

            Object obj = null;

            if (this.value != null) {

                obj = findObject(this.value);

                sb.append(" value=\"");

                if (obj != null) {
                    
                    sb.append(obj);

                } else {

                    sb.append(this.value);

                }
                sb.append("\"");
            }

            if (obj != null && localValue != null && obj.toString().equals(localValue.toString())) {

                sb.append(" checked=\"true\"");

            } else if (this.value != null && localValue != null && this.value.toString().equals(localValue.toString())) {

                sb.append(" checked=\"true\"");

            } else if (localValue instanceof Boolean) {

                Boolean b = (Boolean) localValue;

                if (b) {
                    sb.append(" checked=\"true\"");
                }
            }

        } else {

            throw new IllegalArgumentException("Input tag with bad type: " + type);
        }

        String extra = getExtraAttributes();

        sb.append(extra);

        return sb;
    }

    public String getStringToPrint() throws JspException {

        StringBuffer sb = new StringBuffer(200);
        sb.append(buildTag().toString());
        sb.append(" />");

        return sb.toString();
    }
}
            
