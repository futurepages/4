package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import modules.admin.model.entities.User;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;

public class UserMenuBar extends CustomComponent {

	private final MenuBar settings = new MenuBar();

	public UserMenuBar(){
		Eventizer.register(this);
		setCompositionRoot(build());
	}

	private Component build() {
		settings.addStyleName("user-menu");
		buldContextMenu((User) AppUI.getCurrent().getLoggedUser());
		return settings;
	}

	private void buldContextMenu(User user) {
		settings.removeItems();
		MenuBar.MenuItem settingsItem = settings.addItem("", user.getAvatarRes(), null);

		String[] names = user.getFullName().split("\\s+");
		settingsItem.setText(names.length > 1 ? names[0] + " " + names[names.length - 1] : names[0]);

		settingsItem.addItem(Txt.get("user-menu.basic_info"), selectedItem -> UserWindow.open(user, 0));
		final int logAccessIdx;
		settingsItem.addItem(Txt.get("user-menu.new_password") , selectedItem -> UserWindow.open(user, 1));
		if(user.getProfile()==null){
			logAccessIdx = 2;
		}else{
			settingsItem.addItem(Txt.get("user-menu.profile") , selectedItem -> UserWindow.open(user, 2));
			logAccessIdx = 3;
		}
		settingsItem.addItem(Txt.get("user-menu.log_accesses") , selectedItem -> UserWindow.open(user, logAccessIdx));

		settingsItem.addSeparator();
		settingsItem.addItem(Txt.get("user-menu.sign_out")    , FontAwesome.POWER_OFF,selectedItem -> Eventizer.post(new Events.UserLoggedOut()));
	}

	@Subscribe
	public void updateUserName(final Events.LoggedUserChanged event) {
		buldContextMenu((User) event.getLoggedUser());
	}
}