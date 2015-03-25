package org.futurepages.core.view.items;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

public interface ViewItem {

	public String getViewName();

	public Class<? extends View> getViewClass();

	//try to use font icon: FontAwesome.SOMETHING.
	//Choose what to use here:-->  http://fortawesome.github.io/Font-Awesome/icons/
	public Resource getIcon();

	public boolean isStateful(); //if true, the view will be cached by the navigator.
	public boolean isNotifier(); //if true, the view will be cached by the navigator.

	public int getCountNotifications();

	public Component getButton();
}
