package org.futurepages.core.event;

import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.cookie.Cookies;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.GenericDao;
import org.futurepages.core.view.items.ViewItem;

public abstract class NativeEvents {

    public static final class UserLoginRequested {
        public final static String REMEMBER_KEY = UserLoginRequested.class.getSimpleName()+"_remember";
        public final static String LOGIN_KEY = UserLoginRequested.class.getSimpleName()+"_login";
        private final String login, password;
        private final boolean remember;

        public UserLoginRequested(final String login, final String password, final boolean remember) {
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

    public static class BrowserResize {}

    public static class UserLoggedOut {}

    public static class CloseOpenWindows {}

    public static final class PostViewChange {
        private final ViewItem itemView;

        public PostViewChange(final ViewItem itemView) {
            this.itemView = itemView;
        }

        public ViewItem getViewItem() {
            return itemView;
        }
    }

    public static final class LoggedUserChanged {
        private GenericDao dao;
        private DefaultUser loggedUser;

        public LoggedUserChanged(DefaultUser loggedUser) {
            this(loggedUser, Dao.getInstance());
        }

        public LoggedUserChanged(DefaultUser loggedUser, GenericDao theDao) {
            this.dao = theDao;
            this.loggedUser = dao.detached(loggedUser);
        }

        public DefaultUser getLoggedUser() {
            return loggedUser;
        }
    }

    public static final class NotifyViewItem {

        private ViewItem viewItem;
        private int count;

        public NotifyViewItem(ViewItem viewItem, int count) {
            this.viewItem = viewItem;
            this.count = count;
        }

        public NotifyViewItem(ViewItem viewItem) {
            this.viewItem = viewItem;
            this.count = viewItem.getCountNotifications();
        }

        public ViewItem getViewItem() {
            return viewItem;
        }

        public int getCount() {
            return count;
        }
    }
}
