package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.UploadException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.resource.UploadedTempResource;
import org.futurepages.exceptions.UserException;
import org.futurepages.formatters.DateTimeFormatter;
import org.futurepages.util.FileUtil;
import org.futurepages.util.ImageUtil;
import org.futurepages.util.Is;
import org.futurepages.util.Security;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class UserWindow extends Window {

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

    @PropertyId("avatarValue")
    private TextField avatarValue;


    private UserWindow(User user, final int tabIdx) {
        user = UserServices.getInstance().read(user.getLogin());
//        addStyleName("profile-window"); //deixo aqui para estudo posterior. Este style aparentemente só dava o width e height e padding.
        setId("profilepreferenceswindow");
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);
        setWidth(50.0f, Unit.PERCENTAGE);
        setHeight(82.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        tabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(tabSheet);
        content.setExpandRatio(tabSheet, 1f);

        tabSheet.addComponent(buildUserTab(user));
        if(user.getProfile()!=null){
            tabSheet.addComponent(buildProfileTab(user));
        }
        tabSheet.addComponent(buildLogAccessesTab(user));


        if(Is.selected(tabIdx)){
            tabSheet.setSelectedTab(tabIdx);
        }


        fieldGroup = new BeanFieldGroup<>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
        content.addComponent(buildFooter());
    }

    private Component buildUserTab(User user) {
        final HorizontalLayout root = new HorizontalLayout();
        root.setCaption(Txt.get("user.basic_info"));
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
//        root.addStyleName("profile-form");

        final VerticalLayout picLayout = new VerticalLayout();
        picLayout.setSizeUndefined();
        picLayout.setSpacing(true);
        final Image profilePic = new Image(null, user.getAvatarRes());
        profilePic.setWidth(118.0f, Unit.PIXELS);
        picLayout.addComponent(profilePic);
        picLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        final UploadReceiver receiver = new UploadReceiver();
        final Upload upload = new Upload(null,receiver);

        final HorizontalLayout uploadingContainer = new HorizontalLayout();
        uploadingContainer.setVisible(false);
        uploadingContainer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        final ProgressBar progressBar = new ProgressBar();
        uploadingContainer.addComponent(progressBar);
        progressBar.setVisible(true);
        progressBar.setValue(0.0f);
        progressBar.setIndeterminate(false);
        picLayout.addComponent(uploadingContainer);
        Button uploadFakeButton = new Button("Trocar Foto");
        uploadFakeButton.setVisible(false);

        uploadFakeButton.setStyleName(ValoTheme.BUTTON_DANGER);
        uploadFakeButton.addFocusListener(event -> {
            uploadFakeButton.setVisible(false);
            upload.setVisible(true);
            upload.setDescription("");
        });
        picLayout.addComponent(uploadFakeButton);
        upload.setImmediate(true);
        upload.setButtonCaption("Trocar Foto");
        upload.addFailedListener(event -> {
            upload.setErrorHandler(new DefaultErrorHandler(){
                @Override
                public void error(com.vaadin.server.ErrorEvent ev) {
                    Throwable originalCause = ev.getThrowable();
                    while(originalCause.getCause()!=null){
                        originalCause = originalCause.getCause();
                    }
                    if(originalCause instanceof UserException){
                        if(!Is.empty(upload.getDescription())){
                            uploadFakeButton.setVisible(true);
                            uploadFakeButton.setDescription(upload.getDescription());
                            AppUI.getCurrent().notifyErrors((UserException) originalCause);
                            uploadFakeButton.focus();
                        }
                    }else{
                        if(!(originalCause instanceof  UploadException)){
                            upload.setVisible(false);
                            uploadFakeButton.setVisible(true);
                            uploadFakeButton.setDescription("");
                            AppUI.getCurrent().notifyFailure(originalCause);
                        }
                    }
			    }
            });
        });
        upload.addSucceededListener(event -> {
            File file = receiver.getNewFileResource().getSourceFile();
            try {
                ImageUtil.resizeCropping(file, 300, 300, file.getAbsolutePath(), false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePic.setSource(receiver.getNewFileResource());
            avatarValue.setValue(receiver.getNewFileName());
        });
            upload.addStartedListener(event -> {
                    if (Is.empty(upload.getDescription())) {
                        if (!event.getMIMEType().equals("image/png") && !event.getMIMEType().equals("image/jpg") && !event.getMIMEType().equals("image/jpeg") && !event.getMIMEType().equals("image/pjpeg")) {
                            upload.setDescription("Somente arquivos de imagem são permitidos para seu avatar. Você tentou enviar um arquivo do tipo " + event.getMIMEType());
                            String errorMsg = upload.getDescription();
                            upload.setVisible(false);
                            throw new UserException(errorMsg);
                        } else if (((event.getContentLength() / 1024f / 1024f) > 10)) {
                            upload.setDescription("Arquivos com mais de 10MB não são aceitos");
                            String errorMsg = upload.getDescription();
                            upload.setVisible(false);
                            throw new UserException(errorMsg);
                        } else {
                            AppUI.getCurrent().setPollInterval(500);
                            uploadingContainer.setVisible(true);
                        }
                    } else {
                        upload.interruptUpload();
                    }
            });
        upload.addProgressListener((readBytes, contentLength) -> {
            progressBar.setValue(readBytes / (float) contentLength);
        });
        upload.addFinishedListener(event -> {
            uploadingContainer.setVisible(false);
            if (AppUI.getCurrent().getPollInterval() > -1) {
                AppUI.getCurrent().setPollInterval(-1);
            }
        });
        picLayout.addComponent(upload);
        root.addComponent(picLayout);
        VerticalLayout verticalLayout = new VerticalLayout();
        root.addComponent(verticalLayout);
        root.setExpandRatio(verticalLayout, 1);


        FormLayout details = new FormLayout();
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

        if(user.getProfile()!=null){
            Label lbProfile = new Label(user.getProfile().getLabel());
            lbProfile.setCaption("Perfil");
            details.addComponent(lbProfile);
        }

        fullNameField = new TextField("Nome Completo");
        details.addComponent(fullNameField);
        emailField = new TextField("Email");
        details.addComponent(emailField);

        Label lbX = new Label("");

        lbX.setStyleName(ValoTheme.LABEL_LARGE);
        details.addComponent(lbX);

        Label labelAlterarSenha = new Label("Definição de Nova Senha");
        labelAlterarSenha.setStyleName(ValoTheme.LABEL_COLORED);
        labelAlterarSenha.setIcon(FontAwesome.LOCK);
        details.addComponent(labelAlterarSenha);

        oldPassword = new PasswordField("Senha Atual");
        details.addComponent(oldPassword);

        newPassword = new PasswordField("Nova Senha");
        details.addComponent(newPassword);
        newPasswordAgain = new PasswordField("Nova Senha (Repetir)");
        details.addComponent(newPasswordAgain);

        avatarValue = new TextField("Avatar Hidden Value");
        avatarValue.setVisible(false);


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
            Label labelAccess = new Label(new DateTimeFormatter().format(access.getDateTime(),getLocale())+ (access.getIpHost()!=null? " (a partir de "+access.getIpHost()+")":""));
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
            } catch (CommitException e) {
                AppUI.getCurrent().notifyFailure(e);
            }
        });
        ok.focus();
        return ok;
    }

    public static void open(final User user, final int tabIdx) {
        Eventizer.post(new Events.CloseOpenWindows());
        Window w = new UserWindow(user, tabIdx);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    private static class UploadReceiver implements Upload.Receiver {

        private String newFileName;
        private FileResource newFileResource;

        /**
         * return an OutputStream that simply counts lineends
         */
        @Override
        public OutputStream receiveUpload(final String fileName, final String MIMEType) {
            String prefix = The.concat(Thread.currentThread().getId(), "_", String.valueOf((new Date()).getTime()));
            this.newFileName = The.concat(prefix, "_", Security.md5(prefix + fileName).substring(0, 10), ".", FileUtil.extensionFormat(fileName));
            this.newFileResource = new UploadedTempResource(newFileName);
            File file =  newFileResource.getSourceFile();
            final FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return fos;
       }

        public String getNewFileName() {
            return newFileName;
        }

        public FileResource getNewFileResource() {
            return newFileResource;
        }
    }
}