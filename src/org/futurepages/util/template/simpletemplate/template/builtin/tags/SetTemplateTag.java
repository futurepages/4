package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateWriter;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.SetTagParams;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

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
