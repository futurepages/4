package org.futurepages.util.templatizer.expressions.operators.numerical;

import org.futurepages.util.templatizer.expressions.operators.core.UnaryOperator;
import org.futurepages.util.templatizer.expressions.primitivehandle.Const;
import org.futurepages.util.templatizer.expressions.primitivehandle.Infinity;
import org.futurepages.util.templatizer.expressions.primitivehandle.NumHandle;
import org.futurepages.util.templatizer.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Positive extends UnaryOperator {

	public static Object execute(Object param) {
		if (isNum(param)) {
			return !NumHandle.isInfinity(param)
				? positive(param)
				: (Infinity)param;
		} else {
			return Const.NAN;
		}
	}

	protected static Number positive(Object param) {
		Number n = NumHandle.toLongOrDouble(param);
		
		if (NumHandle.isDouble(n)) {
			return +((Double)n);
		} else { // Long
			return +((Long)n);
		}
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object param = getParam().eval(context);
		
		return execute(param);
	}
	
	@Override
	public String toString() {
		return "+";
	}
}
