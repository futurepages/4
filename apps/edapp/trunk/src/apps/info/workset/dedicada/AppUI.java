package apps.info.workset.dedicada;

import apps.info.workset.dedicada.control.events.EDEvent;
import apps.info.workset.dedicada.control.events.EDEventBus;
import apps.info.workset.dedicada.model.data.DataProvider;
import apps.info.workset.dedicada.model.data.dummy.DummyDataProvider;
import apps.info.workset.dedicada.model.entities.User;
import apps.info.workset.dedicada.view.LoginView;
import apps.info.workset.dedicada.view.MainView;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.locale.LocaleManager;

@Title("Workset Dedicada")
@Theme("dashboard")
public class AppUI extends UI {

    private final DataProvider dataProvider = new DummyDataProvider();
    private final EDEventBus   eventbus     = new EDEventBus();
	
	@Override
	protected void init(VaadinRequest request) {

        setLocale(LocaleManager.getInstance().getDesiredLocale(request.getLocale()));
        EDEventBus.register(this);

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        
        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                event -> {
                    EDEventBus.post(new EDEvent.BrowserResizeEvent());
                });
	}
	
	   /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
//            getNavigator().navigateTo(getNavigator().getState()); //comentado pq já é feito pelo framework.
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final EDEvent.UserLoginRequestedEvent event) {
        User user = getDataProvider().authenticate(event.getUserName(),  event.getPassword());
        if(user!=null){
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        }
        updateContent();
        if(user!=null){
            getNavigator().navigateTo(getNavigator().getState()); //foi retirado do updateContent() pq o navegador já chamava.
        }
    }

    @Subscribe
    public void userLoggedOut(final EDEvent.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final EDEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }	
    
    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((AppUI) getCurrent()).dataProvider;
    }

    public static EDEventBus getEventbus() {
        return ((AppUI) getCurrent()).eventbus;
    }
    

}