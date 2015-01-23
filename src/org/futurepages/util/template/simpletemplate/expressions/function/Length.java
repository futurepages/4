package org.futurepages.util.template.simpletemplate.expressions.function;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Length extends Function {
	
	protected int length(Object sizeable) {
		if (sizeable instanceof Collection) {
			return ((Collection)sizeable).size();
		}
		
		if (sizeable instanceof String) {
			return ((String)sizeable).length();
		}
		
		if (sizeable.getClass().isArray()) {
			return Array.getLength(sizeable);
		}

		return 0;
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		List<Exp> args = getArgs();
		int length = 0;

		for (Exp arg : args) {
			Object evaluated = arg.eval(context);
			length += length(evaluated);
		}

		return length;
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append("length");
		super.toString(sb);
	}
	
	@Override
	public String toString() {
		return "length";
	}
}
