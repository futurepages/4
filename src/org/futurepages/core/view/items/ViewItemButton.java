package org.futurepages.core.view.items;

import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.locale.Txt;
import org.futurepages.util.The;

public final class ViewItemButton extends Button {

	private static final String STYLE_SELECTED = "selected";

	private final ViewItem view;

	public ViewItemButton(final ViewItem view) {
		Eventizer.register(this);
		this.view = view;
		setPrimaryStyleName("valo-menu-item");
		setIcon(view.getIcon());
		String[] txtParts = view.getViewName().split("/");
		String txtCaption;
		if(txtParts.length==2){
			txtCaption = The.concat(txtParts[0],".menu.",txtParts[1]);
		} else {
			txtCaption = "menu."+view.getViewName();
		}
 		setCaption(Txt.get(txtCaption));
		addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(view.getViewName()));

	}

	public ViewItem getView() {
		return view;
	}

	@Subscribe
	public void postViewChange(final NativeEvents.PostViewChange event) {
		removeStyleName(STYLE_SELECTED);
		if (event.getViewItem() == view) {
			addStyleName(STYLE_SELECTED);
		}
	}

}
