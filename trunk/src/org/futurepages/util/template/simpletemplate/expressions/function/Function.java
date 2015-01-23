package org.futurepages.util.template.simpletemplate.expressions.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;

/**
 * Created by thiago on 27/04/14.
 */
public abstract class Function implements Exp {

	private List<Exp> args = new ArrayList<Exp>();

	public Function() {
	}

	public List<Exp> getArgs() {
		return args;
	}
	
	public void setArgs(List<Exp> args) {
		this.args = args;
	}

	public void setArgs(Exp [] args) {
		this.args = new ArrayList<Exp>(Arrays.asList(args));
	}
	
	public void appendArg(Exp arg) {
		if (args == null) {
			args = new ArrayList<Exp>();
		}
		args.add(arg);
	}
	
	public void trim() {
		try {
			((ArrayList<Exp>)this.args).trimToSize();
		} catch (ClassCastException ex) {
			this.args = new ArrayList<Exp>(this.args);
		}
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append("(");
		String c = "";

		for (Exp arg : args) {
			sb.append(c);
			arg.toString(sb);
			c = ", ";
		}

		sb.append(")");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
