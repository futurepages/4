package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class SetTagParams implements TagParams {

	private Exp attribute;
	private String varName;
	
	public SetTagParams(String varName, Exp attribute) {
		this.attribute = attribute;
		this.varName = varName;
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		return this;
	}

	@Override
	public void toString(StringBuilder sb) {
		attribute.toString(sb);
		sb.append(" | ").append(varName);
	}

	public Exp getAttribute() {
		return attribute;
	}

	public String getVarName() {
		return varName;
	}
}
