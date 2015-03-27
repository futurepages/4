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
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.auth.DefaultModule;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.modules.Menus;
import org.futurepages.core.modules.ModuleMenu;
import org.futurepages.core.view.items.ViewItem;
import org.futurepages.core.view.items.ViewItemMenu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings({ "serial", "unchecked" })
public class SimpleMenu extends CustomComponent {

  private ViewItemMenu HOME_ITEM_MENU = null;

  private static final String STYLE_VISIBLE = "valo-menu-visible";

  private Map<ViewItem, Label> badges = new HashMap<>();

	public SimpleMenu(SimpleUI ui) {
		ViewItemMenu menu = Menus.get(ui);
		if(menu!=null){
	        HOME_ITEM_MENU = menu;
		}
		addStyleName("valo-menu");
		setId("app-menu");
		setSizeUndefined();

		//If There's only one AppMenu per UI so this doesn't need to be unregistered from the UI-scoped Eventizer.
		Eventizer.register(this);

		setCompositionRoot(buildContent());
    }

    public ViewItemMenu getItemMenu(){
        return HOME_ITEM_MENU;
    }

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

        Component userMenu = SimpleUI.getCurrent().userMenu();
        if(userMenu!=null){
            menuContent.addComponent(userMenu);
        }

		APP_MENU = buildMenuItems();
		menuContent.addComponent(APP_MENU);
		return menuContent;
	}

	private CssLayout APP_MENU = null;

	protected CssLayout buildMenuItems() {
		CssLayout appMenu = (CssLayout) buildAppMenu();
		Component modulesMenu = buildModulesMenu(appMenu);
		if(modulesMenu != null) {
			appMenu.addComponent(modulesMenu);
		}
		return appMenu;
	}

	private Component buildAppMenu() {
		CssLayout appMenu = new CssLayout();
		appMenu.addStyleName("valo-menuitems");
		appMenu.setHeight(100.0f, Unit.PERCENTAGE);
		addItemsMenu(getItemMenu(), appMenu);
		return appMenu;
	}

	private void addItemsMenu(ViewItemMenu menu, CssLayout appMenu) {
		if(menu!=null){
			for (final ViewItem viewItem  : menu.getViewItems()) {
	            Component resultButton = viewItem.buildButton();
	            if(viewItem.isNotifier()){
	                Label badge = new Label();
	                badge.setId("app-menu-"+viewItem.getViewName()+"-badge");
	                badges.put(viewItem, badge);
	                resultButton = buildBadgeWrapper(resultButton, badge);
	            }
	            appMenu.addComponent(resultButton);
			}
		}
	}

	Map<String, MenuBar.MenuItem> moduleMenuItems = new HashMap<>();

	private Component buildModulesMenu(CssLayout appMenu) {
		final MenuBar settings;
		Iterator<? extends DefaultModule> it = SimpleUI.getCurrent().getLoggedUser().getModules().iterator();
		if (it.hasNext()) {
			settings = new MenuBar();
			settings.setAutoOpen(true);
			settings.setSizeUndefined();
			settings.addStyleName("modules-menu");
			MenuBar.MenuItem settingsItem = settings.addItem(" "+Txt.get("modules"), FontAwesome.FOLDER, null);
			final MenuBar.MenuItem closeItem;

			settingsItem.setStyleName("valo-menu-item");
			settings.setAutoOpen(false);
			while (it.hasNext()) {
				DefaultModule module = it.next();
				ModuleMenu menu = Menus.get(module.getModuleId());
				if(menu!=null){
					MenuBar.MenuItem menuItem = settingsItem.addItem(module.getSmallTitle(), selectedItem -> {
						if (menu.hasHome()) {
							UI.getCurrent().getNavigator().navigateTo(menu.getHome().getViewName());
						}else{
							selectModuleMenu(menu, selectedItem, appMenu);
						}
					});
					for(ViewItem viewItem : menu.getViewItems()){
						moduleMenuItems.put(viewItem.getViewName(), menuItem);
					}
				}
			}
			MenuBar.MenuItem separator = settingsItem.addSeparator();
			separator.setVisible(false);
			closeItem = settingsItem.addItem(Txt.get("menu.close_module"),FontAwesome.TIMES , selectedCloseItem -> {
				selectedCloseItem.setVisible(false);
				settingsItem.getChildren().get(settingsItem.getChildren().size()-2).setVisible(false);
				settingsItem.setText(" " + Txt.get("modules"));
				settingsItem.setIcon(FontAwesome.FOLDER);
			});
			closeItem.setVisible(false);
			return settings;
		}
		return null;
	}

	private void selectModuleMenu(ModuleMenu menu, MenuBar.MenuItem selectedItem, CssLayout appMenu) {
		selectedItem.getParent().setText(" " + selectedItem.getText());
		MenuBar.MenuItem parent = selectedItem.getParent();
		parent.setIcon(FontAwesome.FOLDER_OPEN);
		parent.getChildren().get(parent.getChildren().size() - 1).setVisible(true);
		parent.getChildren().get(parent.getChildren().size() - 2).setVisible(true);
		addItemsMenu(menu, APP_MENU); //TODO evitar que ele seja criado ao navegar para um item. Ou seja criar flag que verifica se está aberto.
	}

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
	public void postViewChange(final NativeEvents.PostViewChange event) {
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}

	@Override
	public void attach() {
		super.attach();
		updateNotificationsCount(null);
	}

	@Subscribe
	public void updateNotificationsCount(final NativeEvents.NotifyViewItem event) {
		if(badges!=null){
			ViewItem viewItem;
			if(event!=null){
				viewItem = event.getViewItem();
			}else{
				viewItem = getItemMenu().getHome();
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

	public static Component defaultUserMenu(DefaultUser user) {
		final MenuBar settings = new MenuBar();
            settings.setAutoOpen(true);
            settings.setSizeFull();
            MenuBar.MenuItem settingsItem = settings.addItem("", FontAwesome.USER, null);
            settingsItem.setText(user.getLogin());
            settings.addStyleName("user-menu");
            settingsItem.addItem(Txt.get("menu.sign_out"), FontAwesome.POWER_OFF, selectedItem -> Eventizer.post(new NativeEvents.UserLoggedOut()));
            return settings;
	}

	public void navigateTo(String state) {
		if(state.contains("/")){
			if(moduleMenuItems.get(state) != null) {
				ModuleMenu menu =  Menus.get(state.split("/")[0]);
				selectModuleMenu(menu,moduleMenuItems.get(state),APP_MENU);
			}
        }
	}
}

