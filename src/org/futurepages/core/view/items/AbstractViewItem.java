package org.futurepages.core.view.items;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

abstract class AbstractViewItem implements ViewItem {

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final ViewItemNotifier notifier;
    private final ViewItemButtonCustomizer buttonCustomizer;

	 public AbstractViewItem(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful, ViewItemNotifier notifier, ViewItemButtonCustomizer buttonCustomizer) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.notifier = notifier;
		 this.buttonCustomizer = buttonCustomizer;
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
		return notifier!=null;
	}

	public ViewItemNotifier getNotifier(){
		return notifier;
	}

	@Override
	public int getCountNotifications(){
		if(isNotifier()){
			return notifier.getCountNotifications();
		}
		return 0;
	}

	public Component buildButton(){
		ViewItemButton viewItemButton = new ViewItemButton(this);
		if(buttonCustomizer!=null){
			return buttonCustomizer.build(viewItemButton);
		}else{
			return viewItemButton;
		}
	}
}