package org.futurepages.core.view.items;

import java.util.Collection;

public interface ViewItemMenu {

	public ViewItem getHome();
	public Collection<ViewItem> getViewItems();
	public ViewItem getByName(String viewName);
}