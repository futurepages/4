package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.futurepages.apps.simple.SimpleWindow;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.upload.UploadField;
import org.futurepages.exceptions.UserException;
import org.futurepages.formatters.brazil.DateTimeFormatter;
import org.futurepages.util.ImageUtil;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserWindow extends SimpleWindow {

    private final BeanFieldGroup<User> fieldGroup;

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

    @PropertyId("birthDate")
    private DateField birthDateField;

    @PropertyId("avatarValue")
    private TextField avatarValue;


    private UserWindow(User user) {
        user = UserServices.getInstance().read(user.getLogin());
//        addStyleName("profile-window"); //deixo aqui para estudo posterior. Este style aparentemente só dava o width e height e padding.
//        setId("profilepreferenceswindow");

        setWidth (50.0f, Unit.PERCENTAGE);
        setHeight(90.0f, Unit.PERCENTAGE);

        fieldGroup = new BeanFieldGroup<>(User.class);

        addTab(buildUserTab(user));

        if(user.hasProfile()){
            addTab(buildProfileTab(user));
        }
        addTab(buildLogAccessesTab(user));

        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
        addFooter(buildFooter());
    }

    private Component buildUserTab(User user) {
        final HorizontalLayout root = new HorizontalLayout();
        final VerticalLayout picLayout = new VerticalLayout();
        final Image profilePic = new Image(null, user.getAvatarRes());
        final FormLayout details = new FormLayout();

        root.setCaption(Txt.get("user.basic_info"));
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        // root.addStyleName("profile-form");

        picLayout.setSizeUndefined();
        picLayout.setSpacing(true);
        profilePic.setWidth(118.0f, Unit.PIXELS);
        picLayout.addComponent(profilePic);
        picLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        final UploadField uploadField = new UploadField("Trocar Foto", AdminConstants.AVATAR_MAX_SIZE, UploadField.AllowedTypes.IMAGES,
        event -> {
            File file = event.getReceiver().getNewFileResource().getSourceFile();
                try {
                    ImageUtil.resizeCropping(file, 300, 300, file.getAbsolutePath(), false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                profilePic.setSource(event.getReceiver().getNewFileResource());
                avatarValue.setValue(event.getReceiver().getNewFileName());
        });
        picLayout.addComponent(uploadField);

        final Button removerAvatar = new Button("");
        removerAvatar.setDescription("Remover Avatar");
        removerAvatar.setIcon(FontAwesome.TIMES);
        removerAvatar.setStyleName(ValoTheme.BUTTON_TINY);

         if(Is.empty(user.getAvatarValue())){
           removerAvatar.setVisible(false);
        }

        removerAvatar.addClickListener(event -> {
            profilePic.setSource(AdminConstants.AVATAR_DEFAULT_RES);
            avatarValue.setValue("");
            removerAvatar.setVisible(false);
        });
        uploadField.setStartListener(()  ->  removerAvatar.setVisible(false));
        uploadField.setFinishListener(() ->{  if(!Is.empty(avatarValue.getValue())) removerAvatar.setVisible(true); });
        picLayout.addComponent(removerAvatar);
        removerAvatar.setStyleName("user-no-avatar-button " + ValoTheme.BUTTON_TINY + " " + ValoTheme.BUTTON_ICON_ONLY);


        root.addComponent(picLayout);
        VerticalLayout verticalLayout = new VerticalLayout();
        root.addComponent(verticalLayout);
        root.setExpandRatio(verticalLayout, 1);


        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.setMargin(false);
        verticalLayout.addComponent(details);
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Label labelPrincipal = new Label("Informações Principais");
        labelPrincipal.setStyleName(ValoTheme.LABEL_COLORED);
        labelPrincipal.setIcon(FontAwesome.USER);
        details.addComponent(labelPrincipal);


        Label lbLogin = new Label(user.getLogin());
        lbLogin.setCaption("Login");
        details.addComponent(lbLogin);

        if(user.hasProfile()){
            Label lbProfile = new Label(user.getProfile().getLabel());
            lbProfile.setCaption("Perfil");
            details.addComponent(lbProfile);
        }

        fullNameField = new TextField("Nome Completo");
        fullNameField.setImmediate(true);
        details.addComponent(fullNameField);
        emailField = new TextField("Email");
        details.addComponent(emailField);
        fullNameField.addBlurListener(event -> {
            Collection<Validator> validators = fullNameField.getValidators();
            if (validators == null || validators.isEmpty()) {
                fullNameField.addValidator(new BeanValidator(User.class, "fullName"));
            }
        });
        emailField.addBlurListener(event -> {
            Collection<Validator> validators = emailField.getValidators();
            if (validators == null || validators.isEmpty()) {
                emailField.addValidator(new BeanValidator(User.class, "email"));
                emailField.setNullRepresentation("");
            }
        });

        Property calendarProperty = new Property<Calendar>() {

            private Calendar value;

            @Override
            public Calendar getValue() {
                return value;
            }

            @Override
            public void setValue(Calendar newValue) throws ReadOnlyException {
                value = newValue;
            }

            @Override
            public Class getType() {
                return Calendar.class;
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public void setReadOnly(boolean newStatus) {

            }
        };
        birthDateField = new DateField("Data de Nascimento");
        Temporal tempA = ReflectionUtil.getObjectField("birthDate",User.class).getDeclaredAnnotation(Temporal.class);
        if(tempA.value()==TemporalType.DATE){
            birthDateField.setResolution(Resolution.DAY);
            birthDateField.setDateFormat("dd/MM/yyyy");
        } else {
            birthDateField.setResolution(Resolution.MINUTE);
            birthDateField.setDateFormat("dd/MM/yyyy HH:mm");
        }
        birthDateField.setConverter(new Converter<Date, Calendar>() {
            @Override
            public Calendar convertToModel(Date value, Class<? extends Calendar> targetType, Locale locale) throws ConversionException {
                if (value == null) {
                    return null;
                }
                Calendar newCal = Calendar.getInstance(locale);
                newCal.setTime(value);
                return newCal;
            }

            @Override
            public Date convertToPresentation(Calendar value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
                return (value == null) ? null : value.getTime();
            }

            @Override
            public Class<Calendar> getModelType() {
                return Calendar.class;
            }

            @Override
            public Class<Date> getPresentationType() {
                return Date.class;
            }
        });
        birthDateField.setPropertyDataSource(calendarProperty);
        birthDateField.addBlurListener(event -> {
            Collection<Validator> validators = birthDateField.getValidators();
            if (validators == null || validators.isEmpty()) {
                birthDateField.addValidator(new BeanValidator(User.class, "birthDate"));
            }
        });
        details.addComponent(birthDateField);

        Label lbX = new Label("");

        lbX.setStyleName(ValoTheme.LABEL_LARGE);
        details.addComponent(lbX);

        Label labelAlterarSenha = new Label("Definição de Nova Senha");
        labelAlterarSenha.setStyleName(ValoTheme.LABEL_COLORED);
        labelAlterarSenha.setIcon(FontAwesome.LOCK);
        details.addComponent(labelAlterarSenha);

        oldPassword = new PasswordField("Senha Atual");
        oldPassword.setNullRepresentation("");
        details.addComponent(oldPassword);

        newPassword = new PasswordField("Nova Senha");
        newPassword.setNullRepresentation("");
        details.addComponent(newPassword);

        newPasswordAgain = new PasswordField("Nova Senha (Repetir)");
        newPasswordAgain.setNullRepresentation("");
        details.addComponent(newPasswordAgain);

        avatarValue = new TextField("Avatar Hidden Value");
        avatarValue.setVisible(false);
        avatarValue.setNullRepresentation("");
        details.addComponent(avatarValue);

        return root;
    }

    private Component buildProfileTab(User user) {
        VerticalLayout root = new VerticalLayout();
        root.setCaption(Txt.get("user.profile"));
        root.setIcon(FontAwesome.UNLOCK);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();
        root.setHeightUndefined();

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);


        Label perfilLabel = new Label(user.getProfile().getLabel());
        perfilLabel.setCaption("Perfil");
        perfilLabel.setSizeUndefined();
        perfilLabel.addStyleName(ValoTheme.LABEL_LIGHT);
        details.addComponent(perfilLabel);

        TextArea profileDescription = new TextArea("Descrição", user.getProfile().getDescription());
        profileDescription.setEnabled(false);
        profileDescription.setReadOnly(true);
        details.addComponent(profileDescription);
        details.setComponentAlignment(perfilLabel, Alignment.MIDDLE_CENTER);
        Label lbPapeis = new Label();
        lbPapeis.setCaption("Permissões ("+user.getRoles().size()+")");
        lbPapeis.addStyleName(ValoTheme.LABEL_BOLD);
        lbPapeis.setEnabled(true);
        details.addComponent(lbPapeis);
        for(Role role : user.getRoles()){
            Label labelRole = new Label(role.getTitle(), ContentMode.TEXT);
            labelRole.setIcon(FontAwesome.CHECK_CIRCLE);
            details.addComponent(labelRole);
        }
        return root;
    }

    private Component buildLogAccessesTab(User user) {
        VerticalLayout root = new VerticalLayout();
        root.setCaption(Txt.get("user.log_accesses"));
        root.setIcon(FontAwesome.TH_LIST);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();
        root.setHeightUndefined();

        FormLayout details = new FormLayout();
        Label label = new Label("Últimos Acessos Realizados:");
        label.setStyleName(ValoTheme.LABEL_COLORED);
        label.setIcon(FontAwesome.TH);
        details.addComponent(label);
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        List<Log> accesses = user.getLastAccesses(20);
        for(Log access : accesses){
            Label labelAccess = new Label(new DateTimeFormatter().format(access.getDateTime())+ (access.getIpHost()!=null? " (a partir de "+access.getIpHost()+")":""));
            labelAccess.setIcon(FontAwesome.CLOCK_O);
            details.addComponent(labelAccess);
        }
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = buildOkButton();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    private Button buildOkButton() {
        Button ok = new Button(Txt.get("save"));
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            try {
                User user = fieldGroup.getItemDataSource().getBean();
                fieldGroup.commit();
                UserServices.getInstance().update(user);
                AppUI.getCurrent().notifySuccess(Txt.get("user.profile_successfully_updated"));
                Eventizer.post(new Events.LoggedUserChanged(user));
                close();
            } catch (UserException e) {
                AppUI.getCurrent().notifyErrors(e);
            } catch (CommitException e1) {
                AppUI.getCurrent().notifyErrors(new UserException(e1));
//                throw new RuntimeException(e);
            }

        });
        ok.focus();
        return ok;
    }

    public static void open(final User user, final int tabIdx) {
        (new UserWindow(user)).openTab(tabIdx);
    }
}