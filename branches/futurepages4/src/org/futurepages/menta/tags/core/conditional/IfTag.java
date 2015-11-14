package org.futurepages.menta.tags.core.conditional;

import org.futurepages.menta.annotations.SuperTag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.ConditionalTag;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.core.tags.cerne.Context;
import org.futurepages.util.Is;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Sergio Oliveira, Modified by Leandro
 */
@SuperTag
@org.futurepages.menta.annotations.Tag(bodyContent = ContentTypeEnum.JSP,name="if")
public class IfTag extends ConditionalTag {

	@Deprecated
	@TagAttribute
	private String test = null;

	@TagAttribute
	private Object value = null;

	@TagAttribute
	private String dynValue = null;

	public void setTest(String test) {
		this.test = test;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setDynValue(String dynValue) {
		this.dynValue = dynValue;
	}

	@Override
	public int doStartTag() throws JspException {
		init();
		eval();
		if (context) {
			return EVAL_BODY_BUFFERED;
		} else {
			if (!print) {
				if (this.testCondition()) {
					return EVAL_BODY_BUFFERED;
				}
			}
		}
		return SKIP_BODY;
	}

	protected boolean isTestValueSet(){
		return value != null || test != null;
	}

	protected void eval() throws JspException {
		boolean cond =  (!negate) ? evaluateExpression() :! evaluateExpression() ;
		setCondition(cond);
	}

	@Override
	public boolean testCondition() throws JspException {
		return this.isCondition();
	}

	private boolean evaluateExpression() throws JspException {
		if (Is.empty(test)) {
			if(value == null){
				return false;
			}else {
				if (value instanceof Boolean) {
					return (Boolean) value;
				} else {
					return Boolean.valueOf(value.toString());
				}
			}
		} else { //legado: atributo "test" não costuma mais ser utilizado.
			String value = (String) this.value;
			if (dynValue != null && value != null) {
				throw new JspException("Invalid IfTag: cannot have value and dynValue at the same time!");
			}

			Tag parent = findAncestorWithClass(this, Context.class);
			Object obj = null;

			try {
				obj = PrintTag.getValue(test, pageContext, true);
				if (obj == null) {
					obj = action.getInput().getValue(test);
				}
			} catch (Exception ex) {
				//by Leandro
				if(action!=null){
					obj = action.getInput().getValue(test);
				}
			}
			Object dynObj = null;

			if (dynValue != null) {

				dynObj = PrintTag.getValue(parent, dynValue, pageContext, true);
			}

			if (obj == null) {
				if (value != null && value.equals("null")) {
					return true;
				}

				if (dynValue != null && dynObj == null) {
					return true;
				}

				//throw new JspException("NullPointerException on IfTag: test expression " + test + " evaluated to null!");

				return false; // no need to throw nasty exception here...

			}

			if (obj instanceof Boolean && dynValue == null && value == null) {

				Boolean b = (Boolean) obj;

				return b.booleanValue();
			}

			if (dynValue != null) {

				if (dynObj == null) {
					return false;
				}

				return obj.equals(dynObj);

			} else {
				if (obj instanceof Boolean) {
					Boolean b = (Boolean) obj;
					if (value != null) {
						if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true")) {
							throw new JspException("Invalid IfTag: value must be a boolean: " + test + " / " + value);
						}
						boolean flag = value.equalsIgnoreCase("true");
						return b.booleanValue() == flag;
					}
					return b.booleanValue();
				} else if (obj instanceof Integer) {
					Integer i = (Integer) obj;
					if (value == null) {
						throw new JspException("Invalid IfTag: value must be present for integer: " + test);
					}
					try {
						return i.intValue() == Integer.parseInt(value);
					} catch (NumberFormatException e) {
						throw new JspException("Invalid IfTag: value must be an integer: " + test + " / " + value);
					}
				} else if (obj instanceof Character) {

					Character c = (Character) obj;

					if (value == null) {
						throw new JspException("Invalid IfTag: value must be present for character: " + test);
					} else if (value.length() != 1) {
						throw new JspException("Invalid IfTag: value is not a char: " + value);
					}

					return c.charValue() == value.charAt(0);
				} else {
					if (value == null) {
						if(obj instanceof String){
							return Boolean.parseBoolean((String) obj);
						} else {
							throw new JspException("Invalid IfTag: value must be present: " + test);
						}
					}
					return value.equals(obj);
				}
			}
		}
	}
}