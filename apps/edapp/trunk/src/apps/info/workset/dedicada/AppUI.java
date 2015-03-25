package apps.info.workset.dedicada;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.ui.Component;
import modules.admin.model.entities.User;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.services.UserServices;
import modules.admin.view.components.UserMenuBar;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.exceptions.UserException;

@Title("Workset Dedicada")
@Theme("simple")
public class AppUI extends SimpleUI {

    @Override
    protected Component userMenu() {
        return new UserMenuBar();
    }

    @Override
    protected DefaultUser authenticate(String login, String password) {
        try {
            UserServices services = UserServices.getInstance();
            User user = services.authenticatedAndDetachedUser(login, password);
            services.logAccess(user, getIpsFromClient());
            return user;
        } catch (InvalidUserOrPasswordException | ExpiredPasswordException e) {
            throw new UserException(e);
        }
    }

    @Override
    protected String getLocalUserId(DefaultUser user){
        return  ((User) user).identifiedHashToStore();
    }

    @Override
    protected DefaultUser getLocalUserById(String userLocalId){
        UserServices services = UserServices.getInstance();
        User userFromDB = services.getByIdentifiedHash(userLocalId);
        if (userFromDB != null) {
            services.logAccess(userFromDB, getIpsFromClient());
            return services.dao().detached(userFromDB);
        }
        return null;
    }
}