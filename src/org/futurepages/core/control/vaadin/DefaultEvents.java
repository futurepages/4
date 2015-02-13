package org.futurepages.core.control.vaadin;

public abstract class DefaultEvents {

    public static final class UserLoginRequestedEvent {
        private final String login, password;

        public UserLoginRequestedEvent(final String login, final String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {}

    public static class UserLoggedOutEvent {}

    public static class CloseOpenWindowsEvent {}

}
