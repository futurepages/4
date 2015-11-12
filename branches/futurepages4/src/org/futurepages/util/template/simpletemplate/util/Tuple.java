package org.futurepages.util.template.simpletemplate.util;

/**
 *
 * @author thiago
 */
public class Tuple<A, B> {
	
	private A a;
	private B b;

	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Tuple) && (a.equals(((Tuple)other).a) && b.equals(((Tuple)other).b));
	}
}
