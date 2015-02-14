package org.futurepages.core.control.vaadin;

import org.futurepages.core.view.ViewItem;

public abstract class Events {

    public static final class UserLoginRequestedEvent {
        public final static String REMEMBER_KEY = UserLoginRequestedEvent.class.getSimpleName()+"_remember";
        public final static String LOGIN_KEY = UserLoginRequestedEvent.class.getSimpleName()+"_login";
        private final String login, password;
        private final boolean remember;

        public UserLoginRequestedEvent(final String login, final String password, final boolean remember) {
            this.login = login;
            this.password = password;
            this.remember = remember;
            Cookies.set(REMEMBER_KEY, String.valueOf(remember));
            Cookies.set(LOGIN_KEY, String.valueOf(login));
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
        private final ViewItem itemView;

        public PostViewChangeEvent(final ViewItem itemView) {
            this.itemView = itemView;
        }

        public ViewItem getViewItem() {
            return itemView;
        }
    }

}
