package org.futurepages.util.templatizer.template.builtin.tags;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.menta.core.formatter.FormatterManager;
import org.futurepages.util.templatizer.expressions.exceptions.BadExpression;
import org.futurepages.util.templatizer.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.templatizer.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.templatizer.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.templatizer.expressions.exceptions.Unexpected;
import org.futurepages.util.templatizer.expressions.tree.Exp;
import org.futurepages.util.templatizer.template.AbstractTemplateBlock;
import org.futurepages.util.templatizer.util.ContextTemplateTag;
import org.futurepages.util.templatizer.template.TemplateBlock;
import org.futurepages.util.templatizer.template.TemplateWriter;
import org.futurepages.util.templatizer.template.builtin.customtagparams.ValueFormatterParams;

/**
 *
 * @author thiago
 */
public class ValueFormatterTemplateTag extends TemplateTag {
	
	public ValueFormatterTemplateTag() {
		super("valueFormatter");
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
		ValueFormatterParams params = (ValueFormatterParams) actualBlock.getParams();
		
		Exp obj = params.getObject();
		Object object;
		
		if (obj != null && (object = obj.eval(context)) != null) {
			Exp frmttr = params.getFormatter();
			Object formatter;
			
			Exp mx = params.getMax();
			Object max;
			
			if (frmttr != null && (formatter = frmttr.eval(context)) != null) {
				AbstractFormatter f = FormatterManager.getFormatter((String)formatter);

				String result = f.format(object);

				if (mx != null && (max = mx.eval(context)) != null) {
					result = result.length() > (Integer)max ? result.substring(0, ((Integer)max) - 3) + "..." : result;
					sb.append(result);
				} else {
					sb.append(result);
				}
			} else if (mx != null && (max = mx.eval(context)) != null) {
				String result = object.toString();
				result = result.length() > (Integer)max ? result.substring(0, ((Integer)max) - 3) + "..." : result;
				sb.append(result);
			} else {
				sb.append(object.toString());
			}
		} else if (actualBlock.getNextInner() != null) {
			TemplateWriter tw = new TemplateWriter();
			evalBody(block, context, tw);
			
			sb.append(tw.toString());
		}

		return SKIP_BODY;
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		String [] attrs = splitParams(expression);
		
		if (attrs != null) {
			if (attrs.length >= 3) {
				return new ValueFormatterParams(defaultEvalExpression(attrs[0]), defaultEvalExpression(attrs[1]), defaultEvalExpression(attrs[2]));
			} else if (attrs.length == 2) {
				return new ValueFormatterParams(defaultEvalExpression(attrs[0]), defaultEvalExpression(attrs[1]), null);
			} else if (attrs.length == 1) {
				return new ValueFormatterParams(defaultEvalExpression(attrs[0]), null, null);
			} else {
				return new ValueFormatterParams(null, null, null);
			}
		} else {
			return new ValueFormatterParams(null, null, null);
		}
	}
}
