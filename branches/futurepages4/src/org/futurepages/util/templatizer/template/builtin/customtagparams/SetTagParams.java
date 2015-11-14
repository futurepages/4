package org.futurepages.util.templatizer.template.builtin.customtagparams;

import org.futurepages.util.templatizer.expressions.tree.Exp;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

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
