package org.futurepages.core.control.vaadin;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class DefaultMainView extends HorizontalLayout {

    public DefaultMainView(CustomComponent menu, ComponentContainer content) {
        setSizeFull();
        addStyleName("mainview");
        addComponent(menu);
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }
}