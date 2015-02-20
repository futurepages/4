package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import modules.admin.model.entities.User;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;

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
		settingsItem = settings.addItem("", user.getAvatarRes(), null);
		updateUserName(user);
		settingsItem.addItem(Txt.get("user-menu.basic_info"), selectedItem -> UserWindow.open(user, 0));
		final int logAccessIdx;
		if(user.getProfile()==null){
			logAccessIdx = 1;
		}else{
			settingsItem.addItem(Txt.get("user-menu.profile") , selectedItem -> UserWindow.open(user, 1));
			logAccessIdx = 2;
		}
		settingsItem.addItem(Txt.get("user-menu.log_accesses") , selectedItem -> UserWindow.open(user, logAccessIdx));

		settingsItem.addSeparator();
		settingsItem.addItem(Txt.get("user-menu.sign_out")    , FontAwesome.POWER_OFF,selectedItem -> Eventizer.post(new Events.UserLoggedOut()));
		return settings;
	}

	private void updateUserName(DefaultUser user){
		String[] names = user.getFullName().split("\\s+");
		settingsItem.setText(names.length>1? names[0]+" "+names[names.length-1]: names[0]);
	}

	@Subscribe
	public void updateUserName(final Events.LoggedUserChanged event) {
		updateUserName(event.getLoggedUser());
		settingsItem.setIcon(((User)event.getLoggedUser()).getAvatarRes());
	}
}