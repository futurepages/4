package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.context.Context;
import org.futurepages.core.persistence.Dao;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.HTMLTag;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;

import javax.persistence.Entity;
import javax.servlet.jsp.JspException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Tag(bodyContent = ContentTypeEnum.EMPTY,name="Select")
public class Select extends HTMLTag {

	@TagAttribute
    private Object list;
    
    @TagAttribute
    private String selected = null;

    @TagAttribute
    private String id = "";
    
    @TagAttribute
    private String klass = "";
    
    @TagAttribute(required = true)
    private String name = "";
    
    @TagAttribute
    private String style = "";
    
    @TagAttribute
    private String onblur = "";
    
    @TagAttribute
    private String onchange = "";
    
    @TagAttribute
    private String idName = null;
    
    @TagAttribute
    private String showAttr = null;
    
    @TagAttribute
    private String defaultText = null;
    
    @TagAttribute
    private String defaultValue = "0";

	@TagAttribute
	private boolean disabled = false;

	@Override
    public String getStringToPrint() throws JspException {
        String[] values = findValues(name);
        StringBuilder sb = new StringBuilder();
		sb.append("<select ").append(disabled ? "disabled " : "").append(klass).append(" ").append(id).append(" name=\"").append(name).append("\" ").append(onchange).append(" ").append(style).append(" ").append(onblur).append(" ").append(getExtraAttributes()).append(">");

        if (list != null) {

            javax.servlet.jsp.tagext.Tag parent = findAncestorWithClass(this, Context.class);

			Object tempVal = null;
            if(list instanceof String){
                tempVal = PrintTag.getValue(parent, (String) list, pageContext, true);
            }else{
                tempVal = list;
            }
			List actionList;
			
			if (tempVal instanceof List) {
				actionList = (List) tempVal;
			} else if (tempVal instanceof Object[]){
				actionList = Arrays.asList((Object[]) tempVal);
			} else {
				actionList = new ArrayList<Object>();
				for (int i = 0; i < Array.getLength(tempVal); i++) {
					actionList.add(Array.get(tempVal, i));
				}
			}

			String value_id;
            if (defaultText != null) {
                sb.append("<option value=\"").append(defaultValue).append("\">").append(defaultText).append("</option>");
            }

            if (actionList.size() > 0) {
				Class objectClass = actionList.get(0).getClass();
				boolean entityClass = (objectClass.isAnnotationPresent(Entity.class));
				String idNameTry = idName;
				if (entityClass && idNameTry == null) {
					idNameTry = Dao.getInstance().getIdName(objectClass);
				}
                for (int i = 0; i < actionList.size(); i++) {
					if(idNameTry!=null){
						value_id = ReflectionUtil.getField(actionList.get(i), idNameTry).toString();
					} else {
                        if(!objectClass.isEnum()){
                            value_id = actionList.get(i).toString();
                        }else{
                            value_id = ((Enum)actionList.get(i)).name();
                        }
					}

                    sb.append("<option value=\"").append(value_id).append("\"");

                    if ((values == null || values.length == 0) && selected != null && selected.equals(value_id)) {
                        sb.append(" selected=\"selected\" ");
                    } else if (contains(values, value_id)) {
                        sb.append(" selected=\"selected\" ");
                    }
                    sb.append(">");
                    if (showAttr == null) {
                        sb.append(actionList.get(i));
                    } else {
                        sb.append(ReflectionUtil.getField(actionList.get(i), showAttr).toString());
                    }
                    sb.append("</option>");
                }
            }
        }
        sb.append("</select>");
        idName = null; //Nao entendi por que, mas coloquei aqui pra corrigir um bug!
		
        return sb.toString();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setList(Object list) {
        this.list = list;
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

    public void setKlass(String klass) {
        this.klass = "class=\"" + klass + "\"";
    }

    @Override
    public void setId(String id) {
        this.id = "id=\"" + id + "\"";
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setOnchange(String onchange) {
		if(!Is.empty(this.onchange)){
	        this.onchange = "onchange=\"" + onchange + "\"";
		}else{
	        this.onchange = "";
		}
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public void setShowAttr(String showAttr) {
        this.showAttr = showAttr;
    }

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}