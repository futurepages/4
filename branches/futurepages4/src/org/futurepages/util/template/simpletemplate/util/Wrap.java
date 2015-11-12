package org.futurepages.util.template.simpletemplate.util;

/**
 *
 * @author thiago
 */
public class Wrap<T> {
	
	private T value;
	
	public Wrap(T i) {
		this.value = i;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T i) {
		this.value = i;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
