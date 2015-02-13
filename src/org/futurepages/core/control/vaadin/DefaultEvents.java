package org.futurepages.core.control.vaadin;

import com.google.gwt.user.client.Cookies;
import com.vaadin.server.VaadinService;
import elemental.client.Browser;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.util.Security;

import javax.servlet.http.Cookie;

public abstract class DefaultEvents {

    public static final class UserLoginRequestedEvent {
        public final static String REMEMBER_KEY = UserLoginRequestedEvent.class.getSimpleName()+"_remember";
        public final static String LOGIN_KEY = UserLoginRequestedEvent.class.getSimpleName()+"_login";
        private final String login, password;
        private final boolean remember;

        public UserLoginRequestedEvent(final String login, final String password, final boolean remember) {
            this.login = login;
            this.password = password;
            this.remember = remember;
            BrowserCookie.setCookie(REMEMBER_KEY, String.valueOf(remember));
            BrowserCookie.setCookie(LOGIN_KEY, String.valueOf(login));
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public boolean isRemember() {
            return remember;
        }
    }

    public static class BrowserResizeEvent {}

    public static class UserLoggedOutEvent {}

    public static class CloseOpenWindowsEvent {}

}
