package org.futurepages.apps.simple;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.view.ViewItem;
import org.futurepages.core.view.ViewItemButton;
import org.futurepages.core.view.ViewItemMenu;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleMenu extends CustomComponent {

  private final ViewItemMenu HOME_ITEM_MENU;

  private static final String STYLE_VISIBLE = "valo-menu-visible";

  private Map<ViewItem, Label> badges = new HashMap<>();

	public SimpleMenu() {
        HOME_ITEM_MENU = viewItemMenuHome();

		addStyleName("valo-menu");
		setId("app-menu");
		setSizeUndefined();

		//If There's only one AppMenu per UI so this doesn't need to be unregistered from the UI-scoped Eventizer.
		Eventizer.register(this);

		setCompositionRoot(buildContent());
    }

    public ViewItemMenu getHome(){
        return HOME_ITEM_MENU;
    }

    protected abstract ViewItemMenu viewItemMenuHome();

	protected Component buildTitle() {
		Label logo = new Label(Txt.get("menu.app_title"), ContentMode.HTML);
		logo.setSizeUndefined();
		HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		logoWrapper.addStyleName("valo-menu-title");
		return logoWrapper;
	}

	protected Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
		CssLayout dashboardWrapper = new CssLayout(menuItemButton);
		dashboardWrapper.addStyleName("badgewrapper");
		dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
		dashboardWrapper.setWidth(100.0f, Unit.PERCENTAGE);
		badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
		badgeLabel.setWidthUndefined();
		badgeLabel.setVisible(false);
		dashboardWrapper.addComponent(badgeLabel);
		return dashboardWrapper;
	}

	protected Component buildContent() {
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");

		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildToggleButton());

        Component userMenu = userMenu();
        if(userMenu!=null){
            menuContent.addComponent(userMenu);
        }

		menuContent.addComponent(buildMenuItems());

		return menuContent;
	}

    //builds a simple user menu with userLogin and logout button.
    protected Component userMenu(){
		DefaultUser user = SimpleUI.getCurrent().getLoggedUser();
        if(user!=null){
            final MenuBar settings = new MenuBar();
            settings.setAutoOpen(true);
            settings.setSizeFull();
            MenuBar.MenuItem settingsItem = settings.addItem("", FontAwesome.USER, null);
            settingsItem.setText(user.getLogin());
            settings.addStyleName("user-menu");
            settingsItem.addItem(Txt.get("menu.sign_out"), FontAwesome.POWER_OFF, selectedItem -> Eventizer.post(new Events.UserLoggedOut()));
            return settings;
        }
        return null;
    }

	protected Component buildMenuItems() {
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);

		for (final ViewItem viewItem  : getHome().getViewItems()) {
			ViewItemButton viewItemButton = new ViewItemButton(viewItem);

            Component resultButton = customViewItemButton(viewItemButton);
            if(viewItem.isNotifier()){
                Label badge = new Label();
                badge.setId("app-menu-"+viewItem.getViewName()+"-badge");
                badges.put(viewItem, badge);
                resultButton = buildBadgeWrapper(resultButton, badge);
            }
            menuItemsLayout.addComponent(resultButton);
		}

		return menuItemsLayout;
	}

    protected abstract Component customViewItemButton(ViewItemButton viewItemButton);


    private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", event -> {
			if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
				getCompositionRoot().removeStyleName(STYLE_VISIBLE);
			} else {
				getCompositionRoot().addStyleName(STYLE_VISIBLE);
			}
		});
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName("valo-menu-toggle");
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}


    @Subscribe // After a successful view change the menu can be hidden in mobile view.
	public void postViewChange(final Events.PostViewChange event) {
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}

	@Override
	public void attach() {
		super.attach();
		updateNotificationsCount(null);
	}

	@Subscribe
	public void updateNotificationsCount(final Events.NotifyViewItem event) {
		if(badges!=null){
			ViewItem viewItem;
			if(event!=null){
				viewItem = event.getViewItem();
			}else{
				viewItem = getHome();
			}
			Label component = badges.get(viewItem);
			if(component!=null){
				int count;
				if(event!=null){
					count = event.getCount();
				}else{
					count = viewItem.getCountNotifications();
				}
				component.setValue(String.valueOf(count));
				component.setVisible(count > 0);
			}
		}
	}
}