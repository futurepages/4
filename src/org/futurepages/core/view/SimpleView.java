package org.futurepages.core.view;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public interface SimpleView {

	public TabSheet getTabSheet();
	public Component addTab(Component tabComponent);
	public Component addFooter(Component component);
	public Component addComponent(Component component);
}