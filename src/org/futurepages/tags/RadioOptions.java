package org.futurepages.tags;

import java.util.List;
import javax.servlet.jsp.JspException;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.core.tags.cerne.HTMLTag;
import org.futurepages.util.The;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class RadioOptions extends HTMLTag {

    @TagAttribute
    private List list;

	@TagAttribute
    private String selected = null;

	@TagAttribute
    private String id = "";

	@TagAttribute
    private String name = "";
    
    @TagAttribute
    private String idName = null;

	@TagAttribute
    private String showAttr = null;

	@TagAttribute
    private String value = null;

	@TagAttribute(required=false)
    private String itemOuterTag = null;

	@Override
    public String getStringToPrint() throws JspException {

        String[] values = findValues(name);
        StringBuffer sb = new StringBuffer();

        if (list != null) {

            String value_id = "";
            String value_input = "";

            if (list.size() > 0) {

                if (idName == null) {
                    idName = Dao.getIdName(list.get(0).getClass());
                }

                for (int i = 0; i < list.size(); i++) {

                    value_id = ReflectionUtil.getField(list.get(i), idName).toString();

                    if (value != null) {
                        value_input = ReflectionUtil.getField(list.get(i), value).toString();
                    } else {
                        value_input = value_id;
                    }
					if (itemOuterTag!=null){
						sb.append("<").append(itemOuterTag).append(">");
					}
                    sb.append("<input id=\"" + name + value_id + "\" type=\"radio\" value=\"" + value_input + "\" name=\"" + name + "\"");

                    if ((values == null || values.length == 0) && selected != null && selected.equals(value_id)) {
                        sb.append(" checked=\"checked\" ");
                    } else if (contains(values, value_id)) {
                        sb.append(" checked=\"checked\" ");
                    }

                    sb.append("");
                    if (showAttr == null) {
                        sb.append("/>&nbsp;").append(list.get(i));
                    } else {
                        sb.append("/>&nbsp;").append(ReflectionUtil.getField(list.get(i), showAttr).toString());
                    }
					if(itemOuterTag==null){
					sb.append("<br/>");
					}else{
						sb.append("</").append(The.firstTokenOf(itemOuterTag," ")).append(">");
					}
                }
            }
        }

        idName = null; //Nao entendi por que, mas coloquei aqui pra corrigir um bug!
        return sb.toString();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = "id=\"" + id + "\"";
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setShowAttr(String showAttr) {
        this.showAttr = showAttr;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public void setItemOuterTag(String itemOuterTag) {
		this.itemOuterTag = itemOuterTag;
	}
}