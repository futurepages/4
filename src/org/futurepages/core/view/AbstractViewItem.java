package org.futurepages.core.view;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

abstract class AbstractViewItem implements ViewItem {

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final boolean notifier;

	 public AbstractViewItem(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful, final boolean notifier) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.notifier = notifier;
    }

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public boolean isStateful() {
		return stateful;
	}

	@Override
	public boolean isNotifier() {
		return notifier;
	}

	@Override
	public abstract int getCountNotifications();
}
