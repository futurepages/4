package modules.global.model.install;

import modules.admin.model.dao.ModuleDao;
import modules.global.model.enums.GlobalProfilesEnum;
import modules.global.model.enums.GlobalRolesEnum;
import org.futurepages.core.install.Installer;

/**
 *
 * @author Daiane
 */
public class ModuleAndRoles extends Installer{

    @Override
    public void execute() throws Exception {
//		ModuleDao.save("global","Global","Entidades Globais");

		GlobalRolesEnum.install();
		GlobalProfilesEnum.install();
    }

}