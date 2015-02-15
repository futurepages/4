package org.futurepages.core.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.futurepages.core.event.Events;
import org.futurepages.core.event.Eventizer;

public final class ViewItemButton extends Button {

	private static final String STYLE_SELECTED = "selected";

	private final ViewItem view;

	public ViewItemButton(final ViewItem view) {
		Eventizer.register(this);
		this.view = view;
		setPrimaryStyleName("valo-menu-item");
		setIcon(view.getIcon());
		setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
		addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(view.getViewName()));

	}

	public ViewItem getView() {
		return view;
	}

	@Subscribe
	public void postViewChange(final Events.PostViewChange event) {
		removeStyleName(STYLE_SELECTED);
		if (event.getViewItem() == view) {
			addStyleName(STYLE_SELECTED);
		}
	}

}
