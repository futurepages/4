package org.futurepages.menta.core.tags;

import org.futurepages.menta.core.tags.cerne.AbstractListTag;

public abstract class ListTag<T> extends AbstractListTag<T> {

	@Override
	protected String getName() {
		return this.getClass().getSimpleName();
	}
}