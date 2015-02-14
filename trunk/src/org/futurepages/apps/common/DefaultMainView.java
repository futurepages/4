package org.futurepages.apps.common;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class DefaultMainView extends HorizontalLayout {

    public DefaultMainView(DefaultMenu menu) {
        ComponentContainer componentContainer;
        componentContainer = new CssLayout();
        componentContainer.addStyleName("view-content");
        componentContainer.setSizeFull();
        new DefaultNavigator(menu, componentContainer);

        setSizeFull();
        addStyleName("mainview");
        addComponent(menu);
        addComponent(componentContainer);
        setExpandRatio(componentContainer, 1.0f);
    }
}