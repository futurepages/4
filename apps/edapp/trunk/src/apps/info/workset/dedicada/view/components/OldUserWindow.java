package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.dao.ProfileDao;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.Module;
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import modules.global.model.dao.CidadeDao;
import modules.global.model.entities.brasil.Estado;
import org.futurepages.apps.simple.SimpleWindow;
import org.futurepages.components.CalendarDateField;
import org.futurepages.components.ImageUploadField;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLQuery;
import org.futurepages.exceptions.UserException;
import org.futurepages.formatters.brazil.DateTimeFormatter;
import org.futurepages.util.ReflectionUtil;
import org.vaadin.maddon.ListContainer;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Collection;
import java.util.List;

public class OldUserWindow extends SimpleWindow {

    private final BeanFieldGroup<User> fieldGroup;

    @PropertyId("fullName")
    private TextField fullNameField;

    @PropertyId("email")
    private TextField emailField;

    @PropertyId("profile")
    private ComboBox profileField;

    @PropertyId("birthCity")
    private ComboBox birthCityField;

    @PropertyId("oldPassword")
    private PasswordField oldPassword;

    @PropertyId("newPassword")
    private PasswordField newPassword;

    @PropertyId("newPasswordAgain")
    private PasswordField newPasswordAgain;

    @PropertyId("birthDate")
    private CalendarDateField birthDateField;

    @PropertyId("avatarValue")
    private TextField avatarValueField;


    private OldUserWindow(User user) {
        user = UserServices.getInstance().read(user.getLogin());

        setWidth (50.0f, Unit.PERCENTAGE);
        setHeight(80.0f, Unit.PERCENTAGE);

        fieldGroup = new BeanFieldGroup<>(User.class);

        addTab(buildUserTab(user));

        addTab(buildPasswordTab(user));

        if(user.hasProfile()){
            addTab(buildProfileTab(user));
        }
        addTab(buildLogAccessesTab(user));

        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
        addFooter(buildFooter());
    }

    private Component buildPasswordTab(User user) {
        final HorizontalLayout root = new HorizontalLayout();
        root.setCaption(Txt.get("user.new_password"));
        root.setIcon(FontAwesome.KEY);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        final FormLayout details = new FormLayout();

        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.setMargin(false);
        root.addComponent(details);
        root.setDefaultComponentAlignment(Alignment.TOP_CENTER);

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
        return root;
    }

    private Component buildUserTab(User user) {
        final HorizontalLayout root = new HorizontalLayout();
        final FormLayout details = new FormLayout();

        root.setCaption(Txt.get("user.basic_info"));
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);

        avatarValueField = new TextField("Avatar Hidden Value");
        avatarValueField.setVisible(false);
        avatarValueField.setNullRepresentation("");
        avatarValueField.setValue(user.getAvatarValue()); //necessary here to avatarValueField

        ImageUploadField imageUploadField = new ImageUploadField(avatarValueField, AdminConstants.AVATAR_DEFAULT_RES ,user.getAvatarRes());

        root.addComponent(imageUploadField);


        VerticalLayout verticalLayout = new VerticalLayout();
        root.addComponent(verticalLayout);
        root.setExpandRatio(imageUploadField, 1);
        root.setExpandRatio(verticalLayout, 3);

        details.addComponent(avatarValueField);
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

        if(AppUI.getCurrent().getLoggedUser().hasModule("admin")){
            profileField = new ComboBox("Perfil");
            profileField.setContainerDataSource(new ListContainer(ProfileDao.listAllOrderByLabel()));
            details.addComponent(profileField);
        }else{
            if(user.hasProfile()){
                Label lbProfile = new Label(user.getProfile().getLabel());
                lbProfile.setCaption("Perfil");
                details.addComponent(lbProfile);
            }
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

        birthDateField = new CalendarDateField("Data de Nascimento");
        //a bind stuff. MIGRATE TO AUTOMATED SCREEN
        Temporal tempA = ReflectionUtil.getObjectField("birthDate",User.class).getDeclaredAnnotation(Temporal.class);
        if(tempA.value()==TemporalType.DATE){
            birthDateField.setResolution(Resolution.DAY);
        } else {
            birthDateField.setResolution(Resolution.MINUTE);
        }
        birthDateField.addBlurListener(event -> {
            Collection<Validator> validators = birthDateField.getValidators();
            if (validators == null || validators.isEmpty()) {
                birthDateField.addValidator(new BeanValidator(User.class, "birthDate"));
            }
        });
        details.addComponent(birthDateField);
        final ComboBox birthStateField = new ComboBox("Estado de Nascimento");
        birthCityField = new ComboBox("Cidade de Nascimento");
        birthCityField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        birthCityField.setItemCaptionPropertyId("nome");
        birthStateField.addValueChangeListener(event -> {
            if (birthStateField.getValue() != null) {
                birthCityField.setValue(null);
                birthCityField.setContainerDataSource(new ListContainer<>(CidadeDao.listByUF(((Estado) birthStateField.getValue()).getSigla())));
                birthCityField.setEnabled(true);
                birthCityField.setReadOnly(false);
                birthCityField.setTextInputAllowed(true);
                birthCityField.setInputPrompt("Escolha a Cidade...");
            } else {
                birthCityField.setValue(null);
                birthCityField.setEnabled(false);
                birthCityField.setReadOnly(true);
                birthCityField.setTextInputAllowed(false);
                birthCityField.setDescription("Necessário escolher estado.");
            }
        });

        if(birthStateField.getValue()==null){
            birthCityField.setValue(null);
            birthCityField.setEnabled(false);
            birthCityField.setReadOnly(true);
            birthCityField.setTextInputAllowed(false);
            birthCityField.setInputPrompt("Necessário escolher estado.");
        }else{
            birthCityField.setInputPrompt("Escolha a cidade...");
        }

        birthStateField.setInputPrompt("Escolha o Estado...");
        birthStateField.setContainerDataSource(new ListContainer(Dao.getInstance().list(new HQLQuery<Estado>(Estado.class, null, "nome asc"))));
        birthStateField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        birthStateField.setItemCaptionPropertyId("nome");
        birthStateField.setTextInputAllowed(false);
        if(user.getBirthCity()!=null){
            birthStateField.setValue(user.getBirthCity().getEstado());
            birthCityField.setContainerDataSource(new ListContainer(CidadeDao.listByUF(user.getBirthCity().getEstado().getSigla())));
            birthCityField.setEnabled(true);
            birthCityField.setReadOnly(false);
        }else{
            birthCityField.setEnabled(false);
            birthCityField.setReadOnly(true);
        }
        details.addComponent(birthStateField);
        details.addComponent(birthCityField);

        Label lbX = new Label("");

        lbX.setStyleName(ValoTheme.LABEL_LARGE);
        details.addComponent(lbX);

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
        perfilLabel.setStyleName(ValoTheme.LABEL_COLORED);
        perfilLabel.setSizeUndefined();
        details.addComponent(perfilLabel);

        TextArea profileDescription = new TextArea("Descrição", user.getProfile().getDescription());
        profileDescription.setEnabled(false);
        profileDescription.setReadOnly(true);
        details.addComponent(profileDescription);
        details.setComponentAlignment(perfilLabel, Alignment.MIDDLE_CENTER);
        Label lbPapeis = new Label();
        lbPapeis.addStyleName(ValoTheme.LABEL_BOLD);
        lbPapeis.setEnabled(true);
        details.addComponent(lbPapeis);
        int countPermissions = 0;
        for(Module module : user.getModules()){
            Label moduleLabel = new Label(module.getTitle());
            moduleLabel.setStyleName(ValoTheme.LABEL_COLORED);
            details.addComponent(moduleLabel);
            moduleLabel.setIcon(FontAwesome.FOLDER_O);
            List<Role> moduleRoles = module.getRoles(user);
            if(moduleRoles.size()==0){
                Label roleLabel = new Label("Permissão total do módulo (*)");
                roleLabel.setStyleName(ValoTheme.LABEL_LIGHT);
                details.addComponent(roleLabel);
                countPermissions++;
            }else{
                for(Role role : moduleRoles){
                    Label roleLabel = new Label(role.getTitle());
                    details.addComponent(roleLabel);
                    countPermissions++;
                }
            }
        }
        lbPapeis.setCaption("Permissões (" + countPermissions + ")");
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
                UserServices services = UserServices.getInstance();
                user = services.update(user);
                services.dao().flush(); //flush is here because of the deached below
                AppUI.getCurrent().notifySuccess(Txt.get("user.profile_successfully_updated"));
                Eventizer.post(new NativeEvents.LoggedUserChanged(user));
                close();
            } catch (UserException e) {
                AppUI.getCurrent().notifyErrors(e);
            } catch (CommitException e1) {
                AppUI.getCurrent().notifyErrors(new UserException("O formulário não foi preenchido devidamente. Revise os alertas"));
                //throw new RuntimeException(e);
            }

        });
        ok.focus();
        return ok;
    }

    public static void open(final User user, final int tabIdx) {
        (new OldUserWindow(user)).openTab(tabIdx);
    }
}