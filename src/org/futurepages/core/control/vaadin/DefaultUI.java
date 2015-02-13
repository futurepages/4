package org.futurepages.core.control.vaadin;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.locale.LocaleManager;
import org.futurepages.exceptions.UserException;

public abstract class DefaultUI extends UI {


    private final DefaultEventBus eventBus  = new DefaultEventBus();

    public static DefaultEventBus getEventBus() {
        return ((DefaultUI) getCurrent()).eventBus;
    }

	@Override
	protected void init(VaadinRequest request) {

        setLocale(LocaleManager.getInstance().getDesiredLocale(request.getLocale()));
        DefaultEventBus.register(this);

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        //custom initializations

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                event ->  DefaultEventBus.post(new DefaultEvents.BrowserResizeEvent())
        );
	}

    @Subscribe
    public void closeOpenWindows(final DefaultEvents.CloseOpenWindowsEvent event) {
        getWindows().forEach(com.vaadin.ui.Window::close);
    }

   private void updateContent() {
        DefaultUser user = (DefaultUser) VaadinSession.getCurrent().getAttribute(DefaultUser.class.getName());

        if (user != null) {
            setContent(getMainView());
            removeStyleName("loginview");
        } else {
            setContent(getLoginView());
            addStyleName("loginview");
        }
    }

    protected abstract Layout getLoginView();

    protected abstract Layout getMainView();

    @Subscribe
    public void userLoginRequested(final DefaultEvents.UserLoginRequestedEvent event) {
        try{
            DefaultUser user = authenticate(event.getLogin(), event.getPassword());
            if(user!=null){
                VaadinSession.getCurrent().setAttribute(DefaultUser.class.getName(), user);
            }
            updateContent();
            if(user!=null){
                getNavigator().navigateTo(getNavigator().getState());
            }
        }catch(UserException errEx){
            showAuthenticatingError(errEx);
        }
    }

    protected void showAuthenticatingError(UserException ue) {
        Notification errorNotification = new Notification(ue.getMessage());
        errorNotification.setDelayMsec(2000);
        errorNotification.setStyleName("bar error small");
        errorNotification.setPosition(Position.TOP_CENTER);
        errorNotification.show(Page.getCurrent());
    }

    protected abstract DefaultUser authenticate(String login, String password);

    /**
     * When the user logs out, current VaadinSession gets closed and the
     * page gets reloaded on the login screen. Do notice the this doesn't
     * invalidate the current HttpSession.
     */
    @Subscribe
    public void userLoggedOut(final DefaultEvents.UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }
}