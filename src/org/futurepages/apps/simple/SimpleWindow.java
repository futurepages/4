package org.futurepages.apps.simple;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.view.SimpleView;

@SuppressWarnings("serial")
public abstract class SimpleWindow extends Window implements SimpleView {

    protected final TabSheet tabSheet = new TabSheet();
	protected final VerticalLayout root = new VerticalLayout();
	private boolean initialized = false;

	protected SimpleWindow(){
        root.setSizeFull();
        root.setMargin(new MarginInfo(true, false, false, false));
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setResizable(false);
        setClosable(true);
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        tabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        root.addComponent(tabSheet);
        root.setExpandRatio(tabSheet, 1f);
		setContent(root);
	}

	public void open(){
		Eventizer.post(new Events.CloseOpenWindows());
        UI.getCurrent().addWindow(this);
        this.focus();
	}

	private void initIfNecessary() {
		if(!initialized){
			open();
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

    @Override
    public Component addComponent(Component component){
        root.addComponent(component);
        return component;
    }

}
