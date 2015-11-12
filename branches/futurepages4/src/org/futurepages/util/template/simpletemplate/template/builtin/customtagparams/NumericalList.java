package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.util.Iterator;
import org.futurepages.util.The;
import org.futurepages.util.template.simpletemplate.expressions.function.Function;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.expressions.tree.Token;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class NumericalList implements Iterable<Integer>, Exp {

	private Object start;
	private Object length;
	private Object step;

	private int i_start;
	private int i_length;
	private int i_step;
	private int i_size;

	public NumericalList(Object start, Object length, Object step) {
		this.start = start;
		this.length = length;
		this.step = step;
	}

	public NumericalList(int start, int length) {
		this(start, length, 1);
	}

	public int getStart() {
		return i_start;
	}

	public int getLength() {
		return i_length;
	}

	public int getStep() {
		return i_step;
	}
	
	public int size() {
		return i_size;
	}
	
	@Override
	public Object eval(ContextTemplateTag context) {
		i_start = (start instanceof Token || start instanceof Function)
			? (Integer)((Exp)start).eval(context)
			: ((Integer)start);

		i_length = (length instanceof Token || length instanceof Function)
			? (Integer)((Exp)length).eval(context)
			: ((Integer)length);

		i_step = (step instanceof Token || step instanceof Function)
			? (Integer)((Exp)step).eval(context)
			: ((Integer)step);

		i_size = Math.abs(i_length - i_start);

		i_step = i_length >= i_start ? Math.abs(i_step) : -Math.abs(i_step);
		
		return this;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new NumericalListIterator(this);
	}
	
	@Override
	public String toString() {
		return The.concat(" ", start, "..", length, "|", step, " ");
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(toString());
	}
}
