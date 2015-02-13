package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.data.DataProvider;
import apps.info.workset.dedicada.model.data.dummy.DummyDataProvider;
import apps.info.workset.dedicada.view.LoginView;
import apps.info.workset.dedicada.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.ui.Layout;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.services.UserServices;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.control.vaadin.DefaultUI;
import org.futurepages.exceptions.UserException;

@Title("Workset Dedicada")
@Theme("dashboard")
public class AppUI extends DefaultUI {

    //TODO tempo while learning, delete it soon...
    private final DataProvider dataProvider = new DummyDataProvider();
    public static DataProvider getDataProvider() {
        return ((AppUI) getCurrent()).dataProvider;
    }

    @Override
    protected Layout getLoginView() {
        return new LoginView();
    }

    @Override
    protected Layout getMainView() {
        return new MainView();
    }

    @Override
    protected DefaultUser authenticate(String login, String password) {
        try {
            return UserServices.authenticatedAndDetachedUser(login, password);
        } catch (InvalidUserOrPasswordException | ExpiredPasswordException e) {
            throw new UserException(e);
        }
    }
}