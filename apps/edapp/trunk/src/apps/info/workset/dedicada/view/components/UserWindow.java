package apps.info.workset.dedicada.view.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.Module;
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import org.futurepages.apps.simple.SimpleWindow;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.Events;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.view.ViewMaker;
import org.futurepages.formatters.brazil.DateTimeFormatter;

import java.util.List;

public class UserWindow extends SimpleWindow {

    private UserWindow(User user) {

        setDimensionsPercent(50,80);

        (new ViewMaker(this, user)).updateForm((updatedUser) -> Eventizer.post(new Events.LoggedUserChanged((DefaultUser) updatedUser)));

        if(user.hasProfile()){
            addTab(buildProfileTab(user));
        }

        addTab(buildLogAccessesTab(user));
    }

    private Component buildProfileTab(User user) {
        VerticalLayout root = new VerticalLayout();
        root.setCaption(Txt.get("admin.user.profile"));
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
        root.setCaption(Txt.get("admin.user.log_accesses"));
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

    public static void open(final User user, final int tabIdx) {
        (new UserWindow(user)).openTab(tabIdx);
    }
}