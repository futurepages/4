package org.futurepages.core.tags;

import org.futurepages.core.tags.cerne.AbstractListTag;

public abstract class ListTag<T> extends AbstractListTag<T> {

	@Override
	protected String getName() {
		return this.getClass().getSimpleName();
	}
}