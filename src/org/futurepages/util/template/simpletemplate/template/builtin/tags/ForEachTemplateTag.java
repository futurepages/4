package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.parser.Parser;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateWriter;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.ForEachArguments;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.NumericalList;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.ObjectArrayIterator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class ForEachTemplateTag extends TemplateTag {
	// 1..10:1|var
	//                                                           1           2       3           4   5          6   7
	private static final Pattern isBuildArray = Pattern.compile("([^\\.\\.]*)(\\.\\.)([^\\:\\|]*)(\\:([^\\|]*))?(\\|(\\w+))?");
	private static final int built_in_group_begin = 1;
	private static final int built_in_group_length = 3;
	private static final int built_in_group_step = 5;
	private static final int built_in_group_var = 7;

	// lista|var|counter
	//                                                     1    2          34   5      67   8
	private static final Pattern isList = Pattern.compile("(\\w+(\\.\\w+)*)((\\|(\\w+))((\\|(\\w+)))?)?");
	private static final int list_group_id = 1;
	private static final int list_group_var = 5;
	private static final int list_group_counter = 8;

	public ForEachTemplateTag() {
		super("forEach");
	}

	protected int fromBuildingArray(TemplateBlock block, ContextTemplateTag context, ForEachArguments fparams, NumericalList nlist, TemplateWriter sb) {
		int iterations = 0;

		for (int el : nlist) {
			if (fparams.getVar() != null) {
				context.put(fparams.getVar(), el);
			}

			evalBody(block, context, sb);

			iterations++;
		}

		return iterations;
	}

	protected int fromList(TemplateBlock block, ContextTemplateTag context, ForEachArguments fparams, Object result, TemplateWriter sb) {
		if (result != null) {

			Iterator it = null;

			if (result.getClass().isArray()) {
				it = new ObjectArrayIterator(result);
			} else if (result instanceof List) {
				it = ((List)result).iterator();
			}

			if (it != null) {
				int i = 0;
				while (it.hasNext()) {
					Object el = it.next();

					if (fparams.getVar() != null) {
						context.put(fparams.getVar(), el);
					}
					if (fparams.getCounter() != null) {
						context.put(fparams.getCounter(), i);
					}

					evalBody(block, context, sb);

					i += 1;
				}

				return i;
			}
		}

		return 0;
	}

	protected Object builtInItem(String str) throws BadExpression, ExpectedExpression, ExpectedOperator, Unexpected, FunctionDoesNotExists {

		if (str != null && !str.isEmpty()) {
			try {
				return Integer.valueOf(str);
			} catch (NumberFormatException ex) {
				return new Parser(str).parse();
			}
		}

		return 1;
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		Matcher m1 = isBuildArray.matcher(expression);
		Matcher m2 = isList.matcher(expression);

		if (m1.find()) {
			Object begin = builtInItem(m1.group(built_in_group_begin));
			Object length = builtInItem(m1.group(built_in_group_length));
			Object step = builtInItem(m1.group(built_in_group_step));
			String var = m1.group(built_in_group_var);

			NumericalList nl = new NumericalList(begin, length, step);

			return new ForEachArguments(nl, var);
		} else if (m2.find()) {
			String list = m2.group(list_group_id);
			String var = m2.group(list_group_var);
			String counter = m2.group(list_group_counter);

			Exp exp = defaultEvalExpression(list);

			return new ForEachArguments(exp, var, counter);
		} else {
			// @TODO: melhorar esta mensagem
			throw new BadExpression("Invalid tag parameter");
		}
	}

	@Override
	public int doBody(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWriter sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;
		ForEachArguments ps = (ForEachArguments) actualBlock.getParams();

		Object result = ps.eval(context);
		int iterations;

		if (result instanceof NumericalList) {
			iterations = fromBuildingArray(actualBlock, context, ps, (NumericalList)result, sb);
		} else {
			iterations = fromList(actualBlock, context, ps, result, sb);
		}

		return iterations > 0 ? SKIP_BODY : EVAL_ELSE;
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return true;
	}
}
