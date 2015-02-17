package org.futurepages.apps.simple;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Events;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.locale.LocaleManager;
import org.futurepages.core.locale.Txt;
import org.futurepages.exceptions.UserException;

import java.util.Map;

public abstract class SimpleUI extends UI {

    private final Eventizer eventizer = new Eventizer();

    // BEGIN methods user need to implements or can override:
    protected abstract DefaultUser loadUserLocally();
    protected abstract SimpleMenu appMenu();
    protected abstract void removeUserLocally();
    protected abstract void storeUserLocally(DefaultUser user);
    protected abstract DefaultUser authenticate(String login, String password);

    protected String loggedUserKey() { return "loggedUser"; }
    protected SessionInitListener sessionInitListener() { return new SimpleSessionInitListener(); }
    protected Component loginView() { return new SimpleLoginView(); }

    // END methods user need to implements or can override:


    @Override
	protected void init(VaadinRequest request) {
        VaadinService.getCurrent().addSessionInitListener(sessionInitListener());
        setLocale(LocaleManager.getInstance().getDesiredLocale(request.getLocale()));
        Eventizer.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        renderContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(event ->  Eventizer.post(new Events.BrowserResize()));
	}

   private void renderContent() {
        DefaultUser user = (DefaultUser) VaadinSession.getCurrent().getAttribute(loggedUserKey());
        if(user==null){
            user = loadUserLocally();
            if(user!=null){
                VaadinSession.getCurrent().setAttribute(loggedUserKey(), user);
            }
        }

        if (user != null) {
            SimpleMenu APP_MENU = appMenu();
            setContent(new SimpleMainView(APP_MENU));
            removeStyleName("loginview");
        } else {
            setContent(loginView());
            addStyleName("loginview");
        }
    }


    @Subscribe
    public void login(final Events.UserLoginRequested event) {
        try{
            DefaultUser user = authenticate(event.getLogin(), event.getPassword());
            if(user!=null){
                VaadinSession.getCurrent().setAttribute(loggedUserKey(), user);
                if(event.isRemember()){
                    storeUserLocally(user);
                }
            }
            renderContent();
            if(user!=null){
                getNavigator().navigateTo(getNavigator().getState());
            }
        }catch(UserException errEx){
            notifyError(errEx.getMessage());
        }
    }

    public void notifySuccess(String msg){
            Notification success = new Notification(msg);
            success.setDelayMsec(2000);
            success.setStyleName("bar success small");
            success.setPosition(Position.TOP_CENTER);
            success.show(Page.getCurrent());
    }

    public void notifyError(String msg){
        Notification errorNotification = new Notification(msg);
        errorNotification.setDelayMsec(5000);
        errorNotification.setStyleName("bar failure small");
        errorNotification.setPosition(Position.TOP_CENTER);
        errorNotification.show(Page.getCurrent());
    }

    public void notifyErrors(UserException e) {
        Map<String,String> map = e.getValidationMap();
        String msg;
        if(map.size()>1){
            StringBuilder sb = new StringBuilder(Txt.get("some_errors_found")+":<ul>");

            int i = 0;
            for(String errorMsg : map.values()){
                i++;
                sb.append("<li>").append(errorMsg).append(i<map.values().size()?";":".").append("</li>");
            }
            sb.append("</ul>");
            msg = sb.toString();
        }else{
            msg = e.getMessage();
        }
        Notification errorNotification = new Notification(msg);
        errorNotification.setDelayMsec(2000 + (400 * map.size()));
        errorNotification.setHtmlContentAllowed(true);
        errorNotification.setStyleName("bar failure small");
        errorNotification.setPosition(Position.TOP_CENTER);
        errorNotification.show(Page.getCurrent());
    }

    public void notifyFailure(String msg){
        Notification.show(msg, Notification.Type.ERROR_MESSAGE); //it's another way to notify.
    }

    /**
     * When the user logs out, current VaadinSession gets closed and the
     * page gets reloaded on the login screen. Do notice the this doesn't
     * invalidate the current HttpSession.
     */
    @Subscribe
    public void logout(final Events.UserLoggedOut event) {
        removeUserLocally();
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final Events.CloseOpenWindows event) {
        getWindows().forEach(com.vaadin.ui.Window::close);
    }

	@Subscribe
	public void updateLoggedUser(final Events.LoggedUserChanged event) {
        VaadinSession.getCurrent().setAttribute(loggedUserKey(),event.getLoggedUser());
    }

    //BEGIN GETs AND UTILs METHODs
    public DefaultUser getLoggedUser() {
		return (DefaultUser) VaadinSession.getCurrent().getAttribute(loggedUserKey());
    }

    public static SimpleUI getCurrent() {
        return (SimpleUI) UI.getCurrent();
    }

    public static Eventizer getEventizer() {
        return getCurrent().eventizer;
    }
    //END GETs AND UTILs METHODs
}