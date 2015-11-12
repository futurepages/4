package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.UnaryOperator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Not extends UnaryOperator {
	
	public static Object case1(Object bool) {
		return !(Boolean)bool;
	}

	public static Object case2(Object object) {
		return !(object != null);
	}
	
	public static Object execute(Object param) {
		return isBool(param) ? case1(param) : case2(param);
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object param = getParam().eval(context);

		return execute(param);
	}
	
	@Override
	public String toString() {
		return "!";
	}
}
