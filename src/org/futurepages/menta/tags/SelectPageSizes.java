package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.pagination.Pageable;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.HTMLTag;
import org.futurepages.util.The;

import javax.servlet.jsp.JspException;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class SelectPageSizes extends HTMLTag implements Pageable {
    
    @TagAttribute
    private String id = "";
    
    @TagAttribute
    private String klass = "";
    
    @TagAttribute
    private String style = "";
    
    @TagAttribute    
    private String onblur = "";
    
    @TagAttribute
    private String onchange = "";
    
    @TagAttribute(required = true)
    private String sizes = "";

    public String getStringToPrint() throws JspException {
        StringBuilder sb = new StringBuilder();
        sb.append("<select ").append(klass).append(" ").append(id).append(" name=\"").append(_PAGE_SIZE).append("\"").append(onchange).append(" ").append(style).append(" ").append(onblur).append(" ").append(extra).append(">");

        String[] pageSizes = The.explodedToArray(sizes, ",");
        for (String pageSize : pageSizes) {
            boolean selected = false;
            if (action.getOutput().getValue(_PAGE_SIZE) != null) {
                int pSize = (Integer) action.getOutput().getValue(_PAGE_SIZE);
                if (pSize == Integer.parseInt(pageSize)){
                    selected = true;
                }
            }
            sb.append("<option ").append(selected ? "selected=\"selected\"" : "").append(" value=\"").append(pageSize).append("\">").append(pageSize).append("</option>");
        }

        sb.append("</select>");
        return sb.toString();
    }

    public void setStyle(String style) {
        this.style = "style=\"" + style + "\"";
    }

    public void setOnblur(String onblur) {
        this.onblur = "onblur=\"" + onblur + "\"";
    }

    public void setKlass(String klass) {
        this.klass = "class=\"" + klass + "\"";
    }

    @Override
    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public void setId(String id) {
        this.id = "id=\"" + id + "\"";
    }

    public void setOnchange(String onchange) {
        this.onchange = "onchange=\"" + onchange + "\"";
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }
}