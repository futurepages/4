package org.futurepages.util.templatizer.template.builtin.tags;

import org.futurepages.util.templatizer.expressions.exceptions.BadExpression;
import org.futurepages.util.templatizer.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.templatizer.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.templatizer.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.templatizer.expressions.exceptions.Unexpected;
import org.futurepages.util.templatizer.expressions.tree.Exp;
import org.futurepages.util.templatizer.template.AbstractTemplateBlock;
import org.futurepages.util.templatizer.template.TemplateBlock;
import org.futurepages.util.templatizer.template.TemplateWriter;
import org.futurepages.util.templatizer.template.builtin.customtagparams.SetTagParams;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class SetTemplateTag extends TemplateTag {
	
	public SetTemplateTag() {
		super("set");
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return false;
	}

	@Override
	public int doBody(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWriter sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;
		SetTagParams params = (SetTagParams)actualBlock.getParams();
		
		Exp attr = params.getAttribute();
		
		String varName = params.getVarName();
		
		if (varName != null && !varName.equals("")) {
			Object result;

			if (attr != null) {
				result = attr.eval(context);
				context.put(varName, result);
			} else if (actualBlock.getNextInner() != null){
				TemplateWriter tw = new TemplateWriter();
				evalBody(block, context, tw);

				if (!tw.isEmpty()) {
					context.put(varName, tw.toString());
				} else {
					context.put(varName, null);
				}
			} else {
				context.put(varName, null);
			}
		}
		
		return SKIP_BODY;
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		String [] attrs = splitParams(expression);
		
		if (attrs != null) {
			if (attrs.length >= 2) {
				return new SetTagParams(attrs[0], defaultEvalExpression(attrs[1]));
			} else {
				return new SetTagParams(attrs[0], null);
			}
		} else {
			return new SetTagParams(null, null);
		}
	}
}
