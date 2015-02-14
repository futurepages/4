package org.futurepages.core.control.vaadin;

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

    public static final class PostViewChangeEvent {
        private final DefaultViewItem itemView;

        public PostViewChangeEvent(final DefaultViewItem itemView) {
            this.itemView = itemView;
        }

        public DefaultViewItem getViewItem() {
            return itemView;
        }
    }

}
