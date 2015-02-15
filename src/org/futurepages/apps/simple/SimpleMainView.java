package org.futurepages.apps.simple;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class SimpleMainView extends HorizontalLayout {

    public SimpleMainView(SimpleMenu menu) {
        ComponentContainer componentContainer;
        componentContainer = new CssLayout();
        componentContainer.addStyleName("view-content");
        componentContainer.setSizeFull();
        new SimpleNavigator(menu, componentContainer);

        setSizeFull();
        addStyleName("mainview");
        addComponent(menu);
        addComponent(componentContainer);
        setExpandRatio(componentContainer, 1.0f);
    }
}