package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import modules.admin.model.entities.User;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.resource.UploadedResource;

public class UserMenuBar extends CustomComponent {

	private MenuBar.MenuItem settingsItem;

	public UserMenuBar(){
		Eventizer.register(this);
		setCompositionRoot(build());
	}

	private Component build() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		final User user = (User) AppUI.getCurrent().getLoggedUser();
//		settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
		settingsItem = settings.addItem("", new UploadedResource("avatar.jpg"), null);
		updateUserName(user);
		settingsItem.addItem(Txt.get("menu.edit_profile"), selectedItem -> ProfilePreferencesWindow.open(user, false));
		settingsItem.addItem(Txt.get("menu.preferences") , selectedItem -> ProfilePreferencesWindow.open(user, true));
		settingsItem.addSeparator();
		settingsItem.addItem(Txt.get("menu.sign_out")    , FontAwesome.POWER_OFF,selectedItem -> Eventizer.post(new Events.UserLoggedOut()));
		return settings;
	}

	private void updateUserName(DefaultUser user){
		String[] names = user.getFullName().split("\\s+");
		settingsItem.setText(names.length>1? names[0]+" "+names[names.length-1]: names[0]);
	}

	@Subscribe
	public void updateUserName(final Events.LoggedUserChanged event) {
		updateUserName(event.getLoggedUser());
	}

}