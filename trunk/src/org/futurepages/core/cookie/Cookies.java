package org.futurepages.core.cookie;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.JavaScript;

import javax.servlet.http.Cookie;

public class Cookies {

    public static void set(String key, String value) {
        set(key, value, 15); //default is 15 days.
    }

    public static void set(String key, String value, int nDays) {
        set(key, value, VaadinService.getCurrentRequest().getContextPath() + "/", nDays);
    }

    public static void remove(String key) {
        set(key, "", VaadinService.getCurrentRequest().getContextPath() + "/", -1);
    }

    public static void set(String key, String value, String path, int nDays) {
        String expiresInDaysCommand = (nDays==-1? "": "expires=\"+(new Date((new Date()).setTime((new Date()).getTime()+("+nDays+"*86400000)))).toGMTString()+\";");
        JavaScript.getCurrent().execute(String.format("document.cookie = \"%s=%s; path=%s;"+expiresInDaysCommand+"\"", key, value, path));
    }

    public static String get(String keyName) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (keyName.equals(cookie.getName())) {
                    return cookie.getValue();
            }
        }
        return null;
    }
}
