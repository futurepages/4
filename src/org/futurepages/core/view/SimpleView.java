package org.futurepages.core.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public interface SimpleView extends View {

	public TabSheet getTabSheet();
	public Component addTab(Component tabComponent);
	public Component addFooter(Component component);
	public void addComponent(Component component);

}