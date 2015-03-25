package org.futurepages.core.view.items;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public class SimpleViewItem extends AbstractViewItem {

	public SimpleViewItem(String viewName, Class<? extends View> viewClass, Resource icon, boolean stateful, ViewItemNotifier notifier, ViewItemButtonCustomizer buttonCustomizer) {
		super(viewName, viewClass, icon, stateful, notifier, buttonCustomizer);
	}
}