package org.futurepages.apps.simple;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
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
import org.futurepages.core.cookie.Cookies;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.locale.Txt;

@SuppressWarnings("serial")
public class SimpleLoginView extends VerticalLayout {

    final TextField      accesskey = new TextField     (Txt.get("login.user.accesskey"));
    final PasswordField  password  = new PasswordField (Txt.get("login.user.password"));
    final Button         signin    = new Button        (Txt.get("login.enter"));
    final CheckBox       remember  = new CheckBox      (Txt.get("login.remember"), true);

    public SimpleLoginView() {
        setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        signin.addClickListener(event ->
            Eventizer.post(new NativeEvents.UserLoginRequested(accesskey.getValue(), password.getValue(), remember.getValue()))
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

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label(Txt.get("login.authentication_required"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(Txt.get("login.caption"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        accesskey.setIcon(FontAwesome.USER);
        accesskey.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        String cookieValue = Cookies.get(NativeEvents.UserLoginRequested.LOGIN_KEY);
        if (cookieValue!=null) {
            accesskey.setValue(cookieValue);
            password.focus();
        }else{
            accesskey.focus();
        }

        fields.addComponents(accesskey, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        return fields;
    }

    private Component buildRemember() {
        String cookieValue = Cookies.get(NativeEvents.UserLoginRequested.REMEMBER_KEY);
        if (cookieValue!=null) {
            remember.setValue(Boolean.valueOf(cookieValue));
            return remember;
        }
        return remember;
    }
}
