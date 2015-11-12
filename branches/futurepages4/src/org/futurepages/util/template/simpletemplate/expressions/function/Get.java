package org.futurepages.util.template.simpletemplate.expressions.function;

import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by thiago on 23/05/14.
 */
public class Get extends Function {

	protected Object get(Object collection, Object index) {
		if (collection instanceof List) {
			if (index instanceof Integer) {
				return ((List)collection).get((Integer)index);
			} else if (index instanceof Long) {
				return ((List)collection).get(((Long)index).intValue());
			}
		} else if (collection instanceof Map) {
			return ((Map)collection).get(index);
		} else if (collection.getClass().isArray()) {
			if (index instanceof Integer) {
				return Array.get(collection, (Integer)index);
			} else if (index instanceof Long) {
				return Array.get(collection, ((Long)index).intValue());
			}
		}

		return null;
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		List<Exp> args = getArgs();

		if (args.size() >= 2) {
			Object collection = args.get(0).eval(context);
			Object index = args.get(1).eval(context);

			return get(collection, index);
		}

		return null;
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append("get");
		super.toString(sb);
	}

	@Override
	public String toString() {
		return "get";
	}
}
