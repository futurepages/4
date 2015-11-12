package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class ValueFormatterParams implements TagParams {
	
	private Exp object;
	private Exp formatter;
	private Exp max;
	
	public ValueFormatterParams(Exp object, Exp formatter, Exp max) {
		this.object = object;
		this.formatter = formatter;
		this.max = max;
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		return this;
	}

	@Override
	public void toString(StringBuilder sb) {
		// @TODO: vai dar null pointer se um dos Exp for null;
		object.toString(sb);
		sb.append(" | ");
		formatter.toString(sb);
		sb.append(" | ");
		max.toString(sb);
	}

	public Exp getObject() {
		return object;
	}

	public Exp getFormatter() {
		return formatter;
	}

	public Exp getMax() {
		return max;
	}
}
