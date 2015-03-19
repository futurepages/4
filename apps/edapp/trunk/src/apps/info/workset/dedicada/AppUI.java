package apps.info.workset.dedicada;

import apps.info.workset.dedicada.control.Events;
import apps.info.workset.dedicada.control.Menu;
import apps.info.workset.dedicada.model.entities.Transaction;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import modules.admin.model.entities.User;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.services.UserServices;
import modules.admin.view.components.UserMenuBar;
import org.futurepages.apps.simple.SimpleMenu;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.cookie.Cookies;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.view.ViewItemButton;
import org.futurepages.core.view.ViewItemMenu;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;

import java.util.Collection;

@Title("Workset Dedicada")
@Theme("simple")
public class AppUI extends SimpleUI {

    private static final String LOCAL_USER_KEY = "_luserk"; //abbreviation to local user for cookie key.

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

public final class AppMenu extends SimpleMenu {

	@Override
	protected ViewItemMenu viewItemMenuHome() {
		return Menu.HOME;
	}

	@Override
	protected Component userMenu() {
		return new UserMenuBar(); //if you change for... return super.userMenu();  // returns simple login title with logout button
	}

	@Override
	protected Component customViewItemButton(ViewItemButton viewItemButton) {

		// Add drop target to reports button. You can drop elements from table to this link.
		if (viewItemButton.getView() == Menu.REPORTS) {
			DragAndDropWrapper reports = new DragAndDropWrapper(viewItemButton);
			reports.setDragStartMode(DragAndDropWrapper.DragStartMode.NONE);
			reports.setDropHandler(new DropHandler() {

				@Override
				public void drop(final DragAndDropEvent event) {
					UI.getCurrent().getNavigator().navigateTo(viewItemButton.getView().getViewName());
					Table table = (Table) event.getTransferable().getSourceComponent();
					Eventizer.post(new Events.TransactionReportEvent((Collection<Transaction>) table.getValue()));
				}

				@Override
				public AcceptCriterion getAcceptCriterion() {
					return AbstractSelect.AcceptItem.ALL;
				}
			});
			return reports;
		}
		return viewItemButton;
	}
}

}