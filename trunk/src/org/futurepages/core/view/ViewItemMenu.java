package org.futurepages.core.view;

public interface ViewItemMenu extends ViewItem {

	public ViewItemMenu getHome();
	public ViewItem[] getViewItems();
	public ViewItem getByName(String viewName);
}