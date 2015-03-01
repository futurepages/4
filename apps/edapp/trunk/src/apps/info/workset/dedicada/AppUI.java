package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.data.DataProvider;
import apps.info.workset.dedicada.model.data.dummy.DummyDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import modules.admin.model.entities.User;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.services.UserServices;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.cookie.Cookies;
import org.futurepages.apps.simple.SimpleMenu;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;

import java.lang.reflect.Field;

@Title("Workset Dedicada")
@Theme("simple")
public class AppUI extends SimpleUI {

    private static final String LOCAL_USER_KEY = "_luserk"; //abbreviation to local user key (proposital as it is a local key.

    @Override
    protected SimpleMenu appMenu() {
        return new AppMenu();
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
    protected void storeUserLocally(DefaultUser user) {
        Cookies.set(LOCAL_USER_KEY, ((User) user).identifiedHashToStore());
    }

    @Override
    protected DefaultUser loadUserLocally() {
       String loggedValue = Cookies.get(LOCAL_USER_KEY);
        if (!Is.empty(loggedValue)) {
            UserServices services = UserServices.getInstance();
            User userFromDb = services.getByIdentifiedHash(loggedValue);
            if(userFromDb!=null){
                Cookies.set(LOCAL_USER_KEY, loggedValue);
                services.logAccess(userFromDb, getIpsFromClient());
                return services.dao().detached(userFromDb);
            }
        }
        return null;
    }

    @Override
    protected void removeUserLocally() {
        Cookies.remove(LOCAL_USER_KEY);
    }



    //TODO TEMP-DELETE-SOON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO TEMP-DELETE-SOON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO TEMP-DELETE-SOON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO TEMP-DELETE-SOON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO TEMP-DELETE-SOON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private final DataProvider dataProvider = new DummyDataProvider();
    public static DataProvider getDataProvider() {
        return ((AppUI) getCurrent()).dataProvider;
    }

    public static void main(String[] args) {
        for(Field f : User.class.getDeclaredFields()){
            System.out.println(f.getName());
        }
    }
}