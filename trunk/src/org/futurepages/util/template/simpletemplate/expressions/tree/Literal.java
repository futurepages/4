package org.futurepages.util.template.simpletemplate.expressions.tree;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Literal implements Token {
	Object val;
	
	public Literal() {
	}

	public Literal(Object val) {
		this.val = val;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}
	
	@Override
	public Object eval(ContextTemplateTag context) {
		return val;
	}
	
	@Override
	public String toString() {
		return val.toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(val);
	}
}
