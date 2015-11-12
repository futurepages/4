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
public class Str extends Function {

	@Override
	public Object eval(ContextTemplateTag context) {
		List<Exp> args = getArgs();
		StringBuilder sb = new StringBuilder();

		for (Exp arg : args) {
			Object evaluated = arg.eval(context);
			sb.append(evaluated.toString());
		}

		return sb.toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append("str");
		super.toString(sb);
	}
	
	@Override
	public String toString() {
		return "str";
	}
}
