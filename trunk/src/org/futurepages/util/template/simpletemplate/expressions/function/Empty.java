package org.futurepages.util.template.simpletemplate.expressions.function;

import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Const;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by thiago on 27/04/14.
 */
public class Empty extends Function {

	public Empty() {
	}

	protected boolean isempty(Object obj) {
		if (obj != null) {
			if (obj.getClass().isArray()) {
				return Array.getLength(obj) == 0;
			} else if (obj instanceof Collection) {
				return ((Collection)obj).isEmpty();
			} else if (obj instanceof Map) {
				((Map)obj).isEmpty();
			} else if (obj instanceof String) {
				return ((String)obj).isEmpty();
			} else if (obj == Const.NULL) {
				return true;
			}
		}

		return true;
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		List<Exp> args = getArgs();
		boolean isEmpty = true;

		for (Exp arg : args) {
			Object evaluated = arg.eval(context);
			isEmpty &= isempty(evaluated);
		}

		return isEmpty;
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append("empty");
		super.toString(sb);
	}
	
	@Override
	public String toString() {
		return "empty";
	}
}
