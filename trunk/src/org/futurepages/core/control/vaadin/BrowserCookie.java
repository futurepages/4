package org.futurepages.core.control.vaadin;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import java.util.UUID;

import org.futurepages.core.admin.DefaultUser;
import org.futurepages.util.Security;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.http.Cookie;

/**
 * A helper that provides access to browser cookies.
 *
 * @author Matti Tahvonen
 */
public class BrowserCookie {

    public static void setCookie(String key, String value) {
        setCookie(key, value, "/");
    }

    public static void setCookie(String key, String value, String path) {
        JavaScript.getCurrent().execute(String.format(
                "document.cookie = \"%s=%s; path=%s;expires=\"+(new Date((new Date()).setTime((new Date()).getTime()+(15*86400000)))).toGMTString()+\";\"", key, value, path
        ));
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
