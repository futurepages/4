package modules.admin.model.dao;

import java.util.List;
import modules.admin.model.entities.User;
import modules.admin.model.install.AdminInstaller;
import org.futurepages.core.install.Installer;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.persistence.Dao;
import modules.admin.model.entities.Module;
import modules.admin.model.core.AdminConstants;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;
import org.futurepages.util.ModuleUtil;

public class ModuleDao extends HQLProvider {



	public static List<Module> list(){
		return Dao.getInstance().list(hql(Module.class, null, asc("title")));
	}

    //lista os modulos que o usuario nao possui
    public static List listAllModulesNoUser(User userX) {

        String strModules="";
        String moduleCondition;

        //verificar modulos do usuario
        for (Module currentModule : userX.getModules()) {
            strModules += "'" + currentModule.getModuleId() + "'" + ',';
        }

        if (!Is.empty(strModules)) {
            strModules = strModules.substring(0, (strModules.length() - 1));
            moduleCondition = field("moduleId").notIn(strModules);
        } else {
            moduleCondition = null;
        }

        return Dao.getInstance().list(hql(Module.class, moduleCondition, asc("title")));
    }

    public static List<Module> listAllModulesSelected(String[] id_modulos) {
        return Dao.getInstance().list(hql(Module.class, field("moduleId").in(id_modulos)));
    }

    public static List<Module> listOrderByTitle() {
        return Dao.getInstance().list(hql(Module.class, field("moduleId").differentFrom(AdminConstants.SUPER_ID), asc("title")));
    }

    public static List<Module> listAllOrderByTitle() {
        return Dao.getInstance().list(hql(Module.class, null, asc("title")));
    }

    public static Module getById(String moduleId) {
        return Dao.getInstance().get(Module.class, moduleId);
    }

    public static Module save(String moduleId, String smallTitle, String title) {
        Module moduleAdmin = new Module();
        moduleAdmin.setModuleId(moduleId);
        moduleAdmin.setSmallTitle(smallTitle);
        moduleAdmin.setTitle(title);
        Dao.getInstance().save(moduleAdmin);
        return moduleAdmin;
    }

    public static void installModule(Installer installer) {
        String moduleId = ModuleUtil.moduleId(installer.getClass());
        ModuleDao.save(moduleId, Txt.get(moduleId+".module.smallTitle") , Txt.get(moduleId+".module.title"));
    }
}