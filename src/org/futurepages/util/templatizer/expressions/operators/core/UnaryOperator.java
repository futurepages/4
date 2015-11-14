package org.futurepages.util.templatizer.expressions.operators.core;

import org.futurepages.util.templatizer.expressions.tree.Exp;

/**
 *
 * @author thiago
 */
public abstract class UnaryOperator extends Operator {
	private Exp param;

	public UnaryOperator() {
		super();
	}

	public Exp getParam() {
		return param;
	}

	public void setParam(Exp param) {
		this.param = param;
	}
	
	@Override
	public void toString(StringBuilder sb) {
		Exp p = param;
		
		sb.append(this.toString());
		sb.append("(");
		p.toString(sb);
		sb.append(")");
	}
}
