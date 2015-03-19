package modules.global.model.install;


import modules.admin.model.dao.ModuleDao;
import modules.global.model.enums.GlobalProfilesEnum;
import modules.global.model.enums.GlobalRolesEnum;
import org.futurepages.core.install.Installer;
import org.futurepages.core.locale.Txt;

/**
 * Inserções Iniciais Padrões do Módulo Admin
 */
public class GlobalInstaller extends Installer {

    public void execute() {
        ModuleDao.installModule(this);

		GlobalRolesEnum.install();
		GlobalProfilesEnum.install();

	    installBrasil();
    }

	private void installBrasil() {

	}

	public static void main(String[] args) {
		System.out.println(Txt.get("$.module.smallTitle"));
	}
}