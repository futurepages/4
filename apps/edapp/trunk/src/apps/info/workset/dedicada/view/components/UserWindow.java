package apps.info.workset.dedicada.view.components;

import apps.info.workset.dedicada.AppUI;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
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
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.apache.xpath.SourceTree;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.resource.UploadedTempResource;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.JPEGUtil2;
import org.futurepages.util.Security;
import org.futurepages.util.The;

import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

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


    private UserWindow(User user, final boolean preferencesTabOpen) {
        user = UserServices.getInstance().read(user.getLogin());
        addStyleName("profile-window");
        setId("profilepreferenceswindow");
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);
        setHeight(90.0f, Unit.PERCENTAGE);

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

        if (preferencesTabOpen) {
            tabSheet.setSelectedTab(1);
        }

        fieldGroup = new BeanFieldGroup<>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
        content.addComponent(buildFooter());
    }

    private final Component buildUserTab(User user) {
        final HorizontalLayout root = new HorizontalLayout();
        root.setCaption(Txt.get("user.basic_info"));
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        final VerticalLayout picLayout = new VerticalLayout();
        picLayout.setSizeUndefined();
        picLayout.setSpacing(true);
        final Image profilePic = new Image(null, user.getAvatarRes());
        profilePic.setWidth(118.0f, Unit.PIXELS);
        picLayout.addComponent(profilePic);
        picLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        final UploadReceiver receiver = new UploadReceiver();
        final Upload uploadButton = new Upload(null,receiver);

        final HorizontalLayout uploadingContainer = new HorizontalLayout();
        uploadingContainer.setVisible(false);
        uploadingContainer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        final ProgressBar progressBar = new ProgressBar();
//        final Button cancelButton = new Button("");
//        cancelButton.setIcon(FontAwesome.TIMES);
//        cancelButton.setStyleName(ValoTheme.BUTTON_TINY + " " + ValoTheme.BUTTON_BORDERLESS);
        uploadingContainer.addComponent(progressBar);
//        uploadingContainer.addComponent(cancelButton);
        progressBar.setVisible(true);
        progressBar.setValue(0.0f);
        progressBar.setIndeterminate(false);
        picLayout.addComponent(uploadingContainer);
//        uploadingContainer.setExpandRatio(progressBar, 5);
//        uploadingContainer.setExpandRatio(cancelButton, 1);

        uploadButton.setImmediate(true);
        uploadButton.setButtonCaption("Trocar Foto");
        uploadButton.addFailedListener(event -> {
            System.out.print("FAILED");
            System.out.println(Thread.currentThread());
            System.out.println(event.getReason());
            System.out.println(event.getReason().getCause()!=null?event.getReason().getCause():"---");
        });
        uploadButton.addSucceededListener(event -> {
            System.out.println("SUCCESS");
            File file = receiver.getNewFileResource().getSourceFile();
            try {
                JPEGUtil2.resizeImage(file, 300, 300, 100, file.getAbsolutePath());
//                JPEGUtil2.resizeImageByOneDimension(Color.WHITE,false, file, 300, 100, file.getAbsolutePath(),false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePic.setSource(receiver.getNewFileResource());
            avatarValue.setValue(receiver.getNewFileName());
        });
            uploadButton.addStartedListener(event -> {
//             cancelButton.addClickListener(eventX -> {
//                System.out.print("cancel Button");
//                System.out.println(Thread.currentThread());
//                uploadButton.interruptUpload();
//            });
            System.out.print("START ");
            System.out.println(Thread.currentThread());

//             //testing long files
//             if(true){
//                System.out.println("começou a enviar");
//                AppUI.getCurrent().setPollInterval(10);
//                event.getUpload().setVisible(false);
//                uploadingContainer.setVisible(true);
//                 return;
//             }
            if(Is.empty(uploadButton.getDescription())){
                System.out.println("START-ERROR-ENTER");
                Thread turnAround = new Thread(()-> {
                        try {
                            Thread.sleep(1000);
                            uploadButton.setDescription("");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    );
                if (!event.getMIMEType().equals("image/png") && !event.getMIMEType().equals("image/jpg") && !event.getMIMEType().equals("image/jpeg")) {
                    uploadButton.setDescription("Somente arquivos de imagem são permitidos para seu avatar. Você tentou enviar um arquivo do tipo "+event.getMIMEType());
                    turnAround.start();
                    String errorMsg = uploadButton.getDescription();
                    AppUI.getCurrent().accessSynchronously(turnAround);
                    throw new UserException(errorMsg);
                } else if (((event.getContentLength() / 1024f / 1024f) > 10)) {
                    uploadButton.setDescription("Arquivos com mais de 10MB não são aceitos");
                    String errorMsg = uploadButton.getDescription();
                    turnAround.start();
                    AppUI.getCurrent().accessSynchronously(turnAround);
                    throw new UserException(errorMsg);
                } else {
                    System.out.println("começou a enviar");
                    AppUI.getCurrent().setPollInterval(5);
                    event.getUpload().setVisible(false);
                    uploadingContainer.setVisible(true);
                }
            }else{
                uploadButton.interruptUpload();
            }
        });
        uploadButton.addProgressListener((readBytes, contentLength) -> {
            System.out.print("PROGRESS ");
            System.out.println(Thread.currentThread());
            progressBar.setValue(readBytes / (float) contentLength);
        });
        uploadButton.addFinishedListener(event -> {
            System.out.println("FINISHED");
            uploadingContainer.setVisible(false);
            uploadButton.setVisible(true);
            if (AppUI.getCurrent().getPollInterval() > -1) {
                AppUI.getCurrent().setPollInterval(-1);
            }
        });
        picLayout.addComponent(uploadButton);
        root.addComponent(picLayout);
        VerticalLayout verticalLayout = new VerticalLayout();
        root.addComponent(verticalLayout);
        root.setExpandRatio(verticalLayout, 1);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.setMargin(false);
        verticalLayout.addComponent(details);
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

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

//        Label label = new Label("Alterar Senha");
//        label.setStyleName(ValoTheme.LABEL_COLORED);
//        verticalLayout.addComponent(label);

        FormLayout detailsPsswd = new FormLayout();
        detailsPsswd.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        verticalLayout.addComponent(detailsPsswd);
        oldPassword = new PasswordField("Senha Atual");
        detailsPsswd.addComponent(oldPassword);

        newPassword = new PasswordField("Nova Senha");
        detailsPsswd.addComponent(newPassword);
        newPasswordAgain = new PasswordField("Nova Senha (Repetir)");
        detailsPsswd.addComponent(newPasswordAgain);

        avatarValue = new TextField("Avatar Value");
        avatarValue.setVisible(false);


        details.addComponent(avatarValue);

        return root;
    }

    private Component buildProfileTab(User user) {
        VerticalLayout root = new VerticalLayout();
        root.setCaption(Txt.get("user.profile_roles"));
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
        lbPapeis.setCaption("Permissões");
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
        Button ok = new Button("Salvar");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        UserServices.getInstance().dao().session().clear();
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
                AppUI.getCurrent().notifyFailure(e.getMessage());
            } catch (Exception exx) {
                throw new RuntimeException(exx);
            }
        });
        ok.focus();
        return ok;
    }

    public static void open(final User user, final boolean preferencesTabActive) {
        Eventizer.post(new Events.CloseOpenWindows());
        Window w = new UserWindow(user, preferencesTabActive);
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