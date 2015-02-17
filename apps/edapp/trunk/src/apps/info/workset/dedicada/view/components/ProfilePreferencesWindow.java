package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.dao.ProfileDao;
import modules.admin.model.entities.Profile;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.resource.UploadedResource;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;

@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window {

    public static final String ID = "profilepreferenceswindow";

    private final BeanFieldGroup<User> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("fullName")
    private TextField fullNameField;

    @PropertyId("email")
    private TextField emailField;

    @PropertyId("oldPassword")
    private PasswordField oldPassword;

    @PropertyId("newPassword")
    private PasswordField newPassword;

    @PropertyId("newPasswordAgain")
    private PasswordField newPasswordAgain;
//
//    @PropertyId("profile")
//    private OptionGroup profileField;
//
//  @PropertyId("title")
//    private ComboBox titleField;
//    @PropertyId("male")
//    private OptionGroup sexField;
//    @PropertyId("location")
//    private TextField locationField;
//    @PropertyId("phone")
//    private TextField phoneField;
//    @PropertyId("newsletterSubscription")
//    private OptionalSelect<Integer> newsletterField;
//    @PropertyId("website")
//    private TextField websiteField;
//    @PropertyId("bio")
//    private TextArea bioField;

    private ProfilePreferencesWindow(User user,final boolean preferencesTabOpen) {
        user = UserServices.getInstance().read(user.getLogin());
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab(user));
        detailsWrapper.addComponent(buildPreferencesTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());
        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);


    }

    private Component buildPreferencesTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Preferences");
        root.setIcon(FontAwesome.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        Label message = new Label("Not implemented in this demo");
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_LIGHT);
        root.addComponent(message);
        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        return root;
    }

    private Component buildProfileTab(User user) {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Profile");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new UploadedResource("avatar.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Change…", event -> Notification.show("Not implemented in this demo"));
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        Label lbLogin = new Label(user.getLogin());
        lbLogin.setCaption("Login");
        details.addComponent(lbLogin);

        fullNameField = new TextField("Nome Completo");
        details.addComponent(fullNameField);
        emailField = new TextField("Email");
        details.addComponent(emailField);

        oldPassword = new PasswordField("Senha Atual");
        details.addComponent(oldPassword);

        newPassword = new PasswordField("Nova Senha");
        details.addComponent(newPassword);
        newPasswordAgain = new PasswordField("Nova Senha (Repetir)");
        details.addComponent(newPasswordAgain);

//        profileField = new OptionGroup("Perfil");
//        profileField.setInputPrompt("Defina o Perfil"); //se combobox
//        profileField.setReadOnly(true); // TODO Descobrir pq não pegou.
//        profileField.setEnabled(false);
//        profileField.setContainerDataSource(new BeanItemContainer(Profile.class, ProfileDao.list()));
//        profileField.setTextInputAllowed(false); //se combobox
//        details.addComponent(profileField);
        Label lbProfile = new Label(user.getProfile().getLabel());
        lbProfile.setCaption("Perfil");
        details.addComponent(lbProfile);

//        sexField = new OptionGroup("Sex");
//        sexField.addItem(Boolean.FALSE);
//        sexField.setItemCaption(Boolean.FALSE, "Female");
//        sexField.addItem(Boolean.TRUE);
//        sexField.setItemCaption(Boolean.TRUE, "Male");
//        sexField.addStyleName("horizontal");
//        details.addComponent(sexField);
//
//        Label section = new Label("Contact Info");
//        section.addStyleName(ValoTheme.LABEL_H4);
//        section.addStyleName(ValoTheme.LABEL_COLORED);
//        details.addComponent(section);
//
//        emailField = new TextField("Email");
//        emailField.setWidth("100%");
//        emailField.setRequired(true);
//        emailField.setNullRepresentation("");
//        details.addComponent(emailField);
//
//        locationField = new TextField("Location");
//        locationField.setWidth("100%");
//        locationField.setNullRepresentation("");
//        locationField.setComponentError(new UserError(
//                "This address doesn't exist"));
//        details.addComponent(locationField);
//
//        phoneField = new TextField("Phone");
//        phoneField.setWidth("100%");
//        phoneField.setNullRepresentation("");
//        details.addComponent(phoneField);
//
//        newsletterField = new OptionalSelect<Integer>();
//        newsletterField.addOption(0, "Daily");
//        newsletterField.addOption(1, "Weekly");
//        newsletterField.addOption(2, "Monthly");
//        details.addComponent(newsletterField);
//
//        section = new Label("Additional Info");
//        section.addStyleName(ValoTheme.LABEL_H4);
//        section.addStyleName(ValoTheme.LABEL_COLORED);
//        details.addComponent(section);
//
//        websiteField = new TextField("Website");
//        websiteField.setInputPrompt("http://");
//        websiteField.setWidth("100%");
//        websiteField.setNullRepresentation("");
//        details.addComponent(websiteField);
//
//        bioField = new TextArea("Bio");
//        bioField.setWidth("100%");
//        bioField.setRows(4);
//        bioField.setNullRepresentation("");
//        details.addComponent(bioField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        UserServices.getInstance().dao().session().clear();
        ok.addClickListener(event -> {
            try {
                User user = fieldGroup.getItemDataSource().getBean();
                fieldGroup.commit();
                UserServices.getInstance().update(user);
                if(!Is.empty(user.getNewPassword())){
                    UserServices.getInstance().applyNewPassword(user);
                }
                AppUI.getCurrent().notifySuccess(Txt.get("profile_successfully_updated"));
                Eventizer.post(new Events.LoggedUserChanged(user));
                close();
            } catch (UserException e) {
                AppUI.getCurrent().notifyErrors(e);
            } catch (CommitException e) {
                AppUI.getCurrent().notifyFailure(e.getMessage());
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(final User user, final boolean preferencesTabActive) {
        Eventizer.post(new Events.CloseOpenWindows());
        Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}