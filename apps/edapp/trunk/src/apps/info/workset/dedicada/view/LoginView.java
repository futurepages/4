package apps.info.workset.dedicada.view;

import apps.info.workset.dedicada.AppEvents;
import com.google.gwt.user.client.Cookies;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.dao.UserDao;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.control.vaadin.BrowserCookie;
import org.futurepages.core.control.vaadin.DefaultEventBus;
import org.futurepages.core.control.vaadin.DefaultEvents;
import org.futurepages.core.locale.Txt;
import org.futurepages.util.Is;
import org.futurepages.util.Security;

import javax.servlet.http.Cookie;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    final TextField     login     = new TextField     (Txt.get("admin.user.login"));
    final PasswordField password  = new PasswordField (Txt.get("admin.user.password"));
    final Button        signin    = new Button        (Txt.get("admin.login.enter"));
    final CheckBox      remember  = new CheckBox      (Txt.get("admin.login.remember"), true);

    public LoginView() {
        setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        signin.addClickListener(event ->
            DefaultEventBus.post(new AppEvents.UserLoginRequestedEvent(login.getValue(), password.getValue(),remember.getValue()))
        );
  }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(buildRemember());
        return loginPanel;
    }

    private Component buildRemember() {
        String cookieValue = BrowserCookie.getByName(DefaultEvents.UserLoginRequestedEvent.REMEMBER_KEY);
        if (cookieValue!=null) {
            remember.setValue(Boolean.valueOf(cookieValue));
            return remember;
        }
        return remember;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        login.setIcon(FontAwesome.USER);
        login.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        fields.addComponents(login, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        String cookieValue = BrowserCookie.getByName(DefaultEvents.UserLoginRequestedEvent.LOGIN_KEY);
        if (cookieValue!=null) {
            login.setValue(cookieValue);
            password.focus();
        }else{
            login.focus();
        }
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label(Txt.get("admin.needed.authentication"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Workset Dedicada");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

}
