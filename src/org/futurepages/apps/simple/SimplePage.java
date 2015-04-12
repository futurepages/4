package org.futurepages.apps.simple;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.view.SimpleView;

@SuppressWarnings("serial")
public abstract class SimplePage extends VerticalLayout implements SimpleView {

    protected final TabSheet tabSheet = new TabSheet();
	protected final VerticalLayout root = this;
	private boolean initialized = false;

	protected SimplePage(){
        root.setSizeFull();
//        root.setMargin(new MarginInfo(true, false, false, false));
        Responsive.makeResponsive(this);

//        Label titleLabel = new Label("User");
//        titleLabel.setSizeUndefined();
//        root.addComponent(titleLabel);


        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//        tabSheet.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
//        tabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        root.addComponent(tabSheet);
        root.setExpandRatio(tabSheet, 1f);
		//addComponent(root);
	}

	private void initIfNecessary() {
		if(!initialized){
            // put what's required here.
			initialized = true;
		}
	}

    @Override
    public Component addFooter(Component component){
		root.addComponent(component);
		return component;
	}

    public void openTab(int tabIdx){
		initIfNecessary();
        tabSheet.setSelectedTab(tabIdx);
    }

    @Override
    public Component addTab(Component tabComponent) {
        tabSheet.addComponent(tabComponent);
        return tabComponent;
    }

    @Override
    public TabSheet getTabSheet(){
        return this.tabSheet;
    }


    public void setDimensionsPercent(int width, int height){
        setWidth (width,  Unit.PERCENTAGE);
        setHeight(height, Unit.PERCENTAGE);
    }

}
