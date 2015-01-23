package org.futurepages.tags;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.HTMLTag;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

import javax.servlet.jsp.JspException;
import java.util.List;

/**
 * @author diogenes
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class CheckOptions extends HTMLTag {

	@TagAttribute(required = true)
	private String name;

	@TagAttribute(required = true)
	private List fullList;

	@TagAttribute(required = true)
	private List list;

	@TagAttribute(required = false)
	private String attribute = null;

	@TagAttribute(required = true)
	private String showAttr = null;

	@TagAttribute
	private String cssClass = "";

	@TagAttribute(required = false)
	private String itemOuterTag = null;

	@TagAttribute(required = false)
	private boolean disabled = false;

	@Override
	public String getStringToPrint() throws JspException {
		StringBuilder sb = new StringBuilder();
		String[] attributePath;
		String value1, value2;
		if (fullList != null && fullList.size() > 0) {
			if (attribute == null) {
				attributePath = new String[1];
				attributePath[0] = Dao.getIdName(fullList.get(0).getClass());
				attribute = attributePath[0];
			} else {
				attributePath = attribute.split("\\.");
			}

			if (list != null) {
				for (int i = 0; i < fullList.size(); i++) {
					value1 = isEnum(attributePath) ? fullList.get(i).toString() : ReflectionUtil.getField(fullList.get(i), attributePath[attributePath.length - 1]).toString();
					if (itemOuterTag != null) {
						sb.append("<").append(itemOuterTag).append(">");
					}
					sb.append("<input value=\"").append(value1).append("\" id=\"")
							.append(name).append("_").append(i).append("\" type=\"checkbox\" name=\"").append(name)
							.append("\" ").append(disabled ? " disabled " : "").append(cssClass);
					for (int j = 0; j < list.size(); j++) {
						value2 = getAttributeValue(j, attributePath);
						if (value1.equals(value2)) {
							sb.append("checked=\"checked\"");
						}
					}
					sb.append("/>&nbsp;").append(ReflectionUtil.getField(fullList.get(i), showAttr).toString());
					if (itemOuterTag == null) {
						sb.append("<br/>");
					} else {
						sb.append("</").append(The.firstTokenOf(itemOuterTag, " ")).append(">");
					}
				}
			} else {
				for (int i = 0; i < fullList.size(); i++) {
					value1 = isEnum(attributePath) ? fullList.get(i).toString() : ReflectionUtil.getField(fullList.get(i), attribute).toString();
					if (itemOuterTag != null) {
						sb.append("<").append(itemOuterTag).append(">");
					}
					sb.append("<input value=\"").append(value1).append("\" id=\"").append(name)
							.append("_").append(i).append("\" type=\"checkbox\" name=\"").append(name)
							.append("\" ").append(disabled ? " disabled " : "").append(cssClass).append("/>&nbsp;")
							.append(ReflectionUtil.getField(fullList.get(i), showAttr).toString());
					if (itemOuterTag == null) {
						sb.append("<br/>");
					} else {
						sb.append("</").append(The.firstTokenOf(itemOuterTag, " ")).append(">");
					}
				}

			}
		}

		return sb.toString();
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;

	}

	public void setShowAttr(String showAttr) {
		this.showAttr = showAttr;
	}

	public void setList(List list) {
		this.list = list;
	}

	public void setFullList(List fullList) {
		this.fullList = fullList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = "class=\"" + cssClass + "\"";
	}

	private String getAttributeValue(int index, String[] attributePath) {
		Object targetObject;
		targetObject = isEnum(attributePath) ? list.get(index) : ReflectionUtil.getField(list.get(index), attributePath[0]);
		for (int i = 1; i < attributePath.length; i++) {
			targetObject = ReflectionUtil.getField(targetObject, attributePath[i]);
		}
		return targetObject.toString();
	}

	public void setItemOuterTag(String itemOuterTag) {
		this.itemOuterTag = itemOuterTag;
	}

	private boolean isEnum(String[] attributePath) {
		if (attributePath[0] == null) { // lista de enum
			return true;
		}
		return false;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
