package modules.admin.dao;

import java.util.List;
import modules.admin.beans.User;
import org.futurepages.core.persistence.Dao;
import modules.admin.beans.Module;
import modules.admin.core.AdminConstants;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

public class ModuleDao extends HQLProvider {



	public static List<Module> list(){
		return Dao.list(Module.class, null, asc("title"));
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

        return Dao.list(Module.class, moduleCondition, asc("title"));
    }

    public static List<Module> listAllModulesSelected(String[] id_modulos) {
        return Dao.list(Module.class, field("moduleId").in(id_modulos));
    }

    public static List<Module> listOrderByTitle() {
        return Dao.list(Module.class, field("moduleId").differentFrom(AdminConstants.SUPER_ID), asc("title"));
    }

    public static List<Module> listAllOrderByTitle() {
        return Dao.list(Module.class, null, asc("title"));
    }
	
    public static Module getById(String moduleId) {
        return Dao.get(Module.class, moduleId);
    }	

    public static Module save(String moduleId, String smallTitle, String title) {
        Module moduleAdmin = new Module();
        moduleAdmin.setModuleId(moduleId);
        moduleAdmin.setSmallTitle(smallTitle);
        moduleAdmin.setTitle(title);
        Dao.save(moduleAdmin);
        return moduleAdmin;
    }
}