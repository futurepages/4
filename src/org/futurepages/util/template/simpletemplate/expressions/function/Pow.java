package org.futurepages.util.template.simpletemplate.expressions.function;

import java.util.List;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Power;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Pow extends Function {

	@Override
	public Object eval(ContextTemplateTag context) {
		List<Exp> args = getArgs();
		Exp a = args.get(0), b = args.get(1);
		
		return Power.execute(a.eval(context), b.eval(context));
	}
}
