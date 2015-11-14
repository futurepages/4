package org.futurepages.util.templatizer.expressions.primitivehandle;

/**
 *
 * @author thiago
 */
public class Null implements Const {
	
	static {
		instance = new Null();
	}
	
	private static final Null instance;
	
	protected static Null instance() {
		return instance;
	}
	
	private Null() {
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	public Object value() {
		return null;
	}

	public boolean equals(Object other) {
		return other == this;
	}
}
