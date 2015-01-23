package org.futurepages.util.template.simpletemplate.expressions.primitivehandle;

/**
 *
 * @author thiago
 */
public class NaN extends Number implements Const {
	
	static {
		nan = new NaN();
	}
	
	protected static final NaN nan;
	
	private NaN() {
	}

	@Override
	public int intValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long longValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float floatValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double doubleValue() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public boolean equals(Object other) {
		return false;
	}
	
	@Override
	public String toString() {
		return "NaN";
	}
}
