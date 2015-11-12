package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
public class ObjectArrayIterator implements Iterator<Object> {
	
	private final Object array;
	private final int size;
	private int index;
	private boolean started;
	
	public ObjectArrayIterator(Object array) {
		if (array.getClass().isArray()) {
			this.array = array;
			size = Array.getLength(array);
		} else {
			this.array = null;
			size = 0;
		}

		started = false;
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return (size > 0) && (!started || index < (size - 1));
	}

	@Override
	public Object next() {
		if (hasNext()) {
			if (started) {
				index += 1;
			} else {
				started = true;
			}
			
			return Array.get(array, index);
		}

		throw new RuntimeException("Iterating over the limit of array");
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
