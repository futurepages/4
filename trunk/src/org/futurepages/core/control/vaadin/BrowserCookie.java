package org.futurepages.core.control.vaadin;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.JavaScript;

import javax.servlet.http.Cookie;

public class BrowserCookie {

    public static void setCookie(String key, String value) {
        setCookie(key, value, 15); //default is 15 days.
    }

    public static void setCookie(String key, String value, int nDays) {
        setCookie(key, value, VaadinService.getCurrentRequest().getContextPath()+"/", nDays);
    }

    public static void removeCookie(String key) {
        setCookie(key, "", VaadinService.getCurrentRequest().getContextPath()+"/",-1);
    }

    public static void setCookie(String key, String value, String path, int nDays) {
        String expiresInDaysCommand = (nDays==-1? "": "expires=\"+(new Date((new Date()).setTime((new Date()).getTime()+("+nDays+"*86400000)))).toGMTString()+\";");
        JavaScript.getCurrent().execute(String.format("document.cookie = \"%s=%s; path=%s;"+expiresInDaysCommand+"\"", key, value, path));
    }

    public static String getByName(String keyName) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (keyName.equals(cookie.getName())) {
                    return cookie.getValue();
            }
        }
        return null;
    }
}
