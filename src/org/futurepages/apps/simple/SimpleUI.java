package org.futurepages.apps.simple;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.DefaultErrorHandler;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.cookie.Cookies;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.locale.LocaleManager;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.modules.Menus;
import org.futurepages.core.persistence.Dao;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import java.util.Map;

public abstract class SimpleUI extends UI {

    private static final int NOTIFICATIONS_TIMEOUT_MS = 2000;
    private static final String DEFAULT_LOGGED_USER_KEY = "loggedUser";
    private static final String LOCAL_USER_KEY = "_luserk"; //abbreviation to local user for cookie key.


    private final Eventizer eventizer = new Eventizer();

    protected abstract String getLocalUserId(DefaultUser user);
    protected abstract DefaultUser getLocalUserById(String userLocalId);
    protected abstract DefaultUser authenticate(String login, String password);


    protected SimpleMenu buildMenu(){
        return new SimpleMenu(this);
    }

    protected void storeUserLocally(DefaultUser user) {
        Cookies.set(LOCAL_USER_KEY, getLocalUserId(user));
    }

    protected void removeUserLocally() {
        Cookies.remove(LOCAL_USER_KEY);
    }

    protected DefaultUser loadUserLocally() {
       String loggedValue = Cookies.get(LOCAL_USER_KEY);
        if (!Is.empty(loggedValue)) {
            DefaultUser localUser = getLocalUserById(loggedValue);
            if(localUser!=null){
                Cookies.set(LOCAL_USER_KEY, loggedValue);
                return localUser;
            }
        }
        return null;
    }


    protected String loggedUserKey() { return DEFAULT_LOGGED_USER_KEY; }
    protected SessionInitListener sessionInitListener() { return new SimpleSessionInitListener(); }
    protected Component loginView() { return new SimpleLoginView(); }

    private Menus menus;

    public Menus getMenus(){
        return this.menus;
    }

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
        initErrorHandler();
        Page.getCurrent().addBrowserWindowResizeListener(event ->  Eventizer.post(new NativeEvents.BrowserResize()));
	}

    private void initErrorHandler() {
        	UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (Dao.getInstance().isTransactionActive()) {
					Dao.getInstance().rollBackTransaction();
				}

				Throwable originalCause = event.getThrowable();
				while(originalCause.getCause()!=null){
					originalCause = originalCause.getCause();
				}
                if(originalCause instanceof UserException){
                        UserException ue = (UserException) originalCause;
                        getCurrent().notifyErrors(ue);
                }else{
                      getCurrent().notifyFailure(originalCause);
                    //TODO it's here to  know how is the default handle for component.
//                    AbstractComponent component = findAbstractComponent(event);
                     //Default way of handling component failures. Putting an exclamation point in it.
//                    if (component != null) {
                        // Shows the error in AbstractComponent
//                        ErrorMessage errorMessage = new UserError(Txt.get("ui.internal_failure")+" "+errorNumber);
//                        component.setComponentError(errorMessage);
//                    }
                }
			}
		});
    }

    private SimpleMenu APP_MENU = null;

    private void renderContent() {
        DefaultUser user = (DefaultUser) VaadinSession.getCurrent().getAttribute(loggedUserKey());
        if(user==null){
            user = loadUserLocally();
            if(user!=null){
                VaadinSession.getCurrent().setAttribute(loggedUserKey(), user);
            }
        }

       menus = new Menus(getLoggedUser());

        if (user != null) {
            APP_MENU = buildMenu();
            setContent(new SimpleMainView(APP_MENU));
            removeStyleName("loginview");
        } else {
            setContent(loginView());
            addStyleName("loginview");
        }
    }

    public SimpleMenu getMenu(){
        return APP_MENU;
    }

    //builds a simple user menu with userLogin and logout button.
    protected Component userMenu(){
		DefaultUser user = getLoggedUser();
        if(user!=null){
            SimpleMenu.defaultUserMenu(user);
        }
        return null;
    }


    public void notifySuccess(String msg){
            Notification success = new Notification((!msg.endsWith(".") && !msg.endsWith("!"))? (msg+"."):msg);
            success.setDelayMsec(NOTIFICATIONS_TIMEOUT_MS);
            success.setStyleName("bar success small");
            success.setPosition(Position.TOP_CENTER);
            success.show(Page.getCurrent());
    }

    public void notifyError(String msg){
        Notification errorNotification = new Notification(msg);
        errorNotification.setDelayMsec(NOTIFICATIONS_TIMEOUT_MS);
        errorNotification.setHtmlContentAllowed(true);
        errorNotification.setStyleName("bar failure small");
        errorNotification.setPosition(Position.TOP_CENTER);
        errorNotification.show(Page.getCurrent());
    }

    public void notifyErrors(UserException e) {
        Map<String,String> map = e.getValidationMap();
        String msg;
        if(map.size()>1){
            StringBuilder sb = new StringBuilder(Txt.get("ui.some_errors_found")+":<ul>");

            int i = 0;
            for(String errorMsg : map.values()){
                i++;
                sb.append("<li>").append(errorMsg).append(i<map.values().size()?";":".").append("</li>");
            }
            sb.append("</ul>");
            msg = sb.toString();
        }else{
            msg = !e.getMessage().endsWith(".")? e.getMessage()+".":e.getMessage();
        }
        notifyError(msg);
    }

    public void notifyFailure(String msg){
        Notification.show(msg, Notification.Type.ERROR_MESSAGE); //it's another way to notify.
    }

    public void notifyFailure(Throwable originalCause) {
        String errorNumber = AppLogger.getInstance().execute(originalCause, VaadinService.getCurrentRequest());
        getCurrent().notifyFailure(The.concat(Txt.get("ui.internal_failure"), " ", errorNumber, "  (", Txt.get("ui.press_esc_to_exit"),")"));
    }



    @Subscribe
    public void login(final NativeEvents.UserLoginRequested event) {
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
                restoreState();
            }
        }catch(UserException errEx){
            notifyError(errEx.getMessage());
        }
    }
    /**
     * When the user logs out, current VaadinSession gets closed and the
     * page gets reloaded on the login screen. Do notice the this doesn't
     * invalidate the current HttpSession.
     */
    @Subscribe
    public void logout(final NativeEvents.UserLoggedOut event) {
        removeUserLocally();
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final NativeEvents.CloseOpenWindows event) {
        for(Window window : getWindows()){
           window.close();
        }

        //TODO consider it...
        // getWindows().forEach(com.vaadin.ui.Window::close); //could i change the code above for <-- this??
    }

	@Subscribe
	public void updateLoggedUser(final NativeEvents.LoggedUserChanged event) {
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

    public String getIpsFromClient(){
        VaadinRequest req =VaadinService.getCurrentRequest();
        String ipRealClient = req.getHeader("x-forwarded-for");
		String ipResult;
		if (ipRealClient == null) {
			ipResult = req.getRemoteAddr();
		} else {
			ipResult = ipRealClient;
		}
		return ipResult;
    }

    public void navigateTo(String viewName) {
        getNavigator().navigateTo(viewName);
    }

    public void restoreState(){
        navigateTo(getNavigator().getState());
    }
    //END GETs AND UTILs METHODs
}