package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.data.DataProvider;
import apps.info.workset.dedicada.model.data.dummy.DummyDataProvider;
import apps.info.workset.dedicada.view.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import modules.admin.model.entities.User;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.services.UserServices;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.control.vaadin.BrowserCookie;
import org.futurepages.core.control.vaadin.DefaultNavigator;
import org.futurepages.core.control.vaadin.DefaultUI;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;

@Title("Workset Dedicada")
@Theme("dashboard")
public class AppUI extends DefaultUI {

    //TODO tempo while learning, delete it soon...
    private final DataProvider dataProvider = new DummyDataProvider();
    public static DataProvider getDataProvider() {
        return ((AppUI) getCurrent()).dataProvider;
    }


    @Override
    protected DefaultNavigator naviagator() {
        return new AppNavigator(getComponentContainer());
    }

    @Override
    protected Layout loginView() {
        return new LoginView();
    }

    @Override
    protected CustomComponent appMenu() {
        return new AppMenu();
    }

    @Override
    protected DefaultUser authenticate(String login, String password) {
        try {
            return UserServices.authenticatedAndDetachedUser(login, password);
        } catch (InvalidUserOrPasswordException | ExpiredPasswordException e) {
            throw new UserException(e);
        }
    }


    @Override
    protected void storeUserLocally(DefaultUser user) {
        BrowserCookie.setCookie(DefaultUser.class.getSimpleName(), ((User)user).identifiedHashToStore());
    }

    @Override
    protected DefaultUser loadUserLocally() {
       String loggedValue = BrowserCookie.getByName(DefaultUser.class.getSimpleName());
        if (!Is.empty(loggedValue)) {
            User dbUser = UserServices.getByIdentiedHash(loggedValue);
            if(dbUser!=null){
                BrowserCookie.setCookie(DefaultUser.class.getSimpleName(), loggedValue);
                UserServices.turnDetached(dbUser);
                return dbUser;
            }
        }
        return null;
    }

    @Override
    protected void removeUserLocally() {
        BrowserCookie.setCookie(DefaultUser.class.getSimpleName(),""); //TODO create a remove method.
    }
}