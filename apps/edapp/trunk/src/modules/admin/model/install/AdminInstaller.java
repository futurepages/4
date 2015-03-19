package modules.admin.model.install;


import modules.admin.model.core.AdminConstants;

import modules.admin.model.dao.ModuleDao;
import modules.admin.model.dao.ProfileDao;
import modules.admin.model.entities.Param;
import modules.admin.model.entities.User;
import modules.admin.model.entities.enums.AdminProfilesEnum;
import modules.admin.model.entities.enums.AdminRolesEnum;
import modules.admin.model.entities.enums.ParamEnum;
import modules.admin.model.entities.enums.ParamValueType;
import org.futurepages.core.config.Apps;
import org.futurepages.core.install.Installer;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.CalendarUtil;

/**
 * Inserções Iniciais Padrões do Módulo Admin
 */
public class AdminInstaller extends Installer implements AdminConstants {

    public void execute() {
        ModuleDao.installModule(this);

	    //installing joker module...
        ModuleDao.save(SUPER_ID, "(*) Todos os Módulos","(*) Todos os Módulos");

		AdminRolesEnum.install();
		AdminProfilesEnum.install();
		Dao.getInstance().flush();

		//Criação do usuario 'super'
        User userAdmin = new User();
        userAdmin.setLogin("admin");
        userAdmin.setFullName("Administrador Padrão do Sistema");
        userAdmin.setEmail("admin@admin.com");
	    userAdmin.setBirthDate(CalendarUtil.buildCalendar(1985, 1, 16));

		if(Apps.get("DEPLOY_MODE").equals("none")){
			userAdmin.setPassword("admin.senha");
		}else{
			userAdmin.setPassword("admin.user#temp.pswd");
		}
        userAdmin.setStatus(true);

		userAdmin.setProfile(ProfileDao.get(AdminProfilesEnum.SYSTEM_SUPER));

        Dao.getInstance().save(userAdmin);

		// Criação do parâmetro que verifica se a senha expirou
		Param param = new Param(ParamEnum.EXPIRED_PASS_IN_DAYS, ParamValueType.INT, "30", "Quantidade de dias para a senha expirar", 365);

		Dao.getInstance().save(param);
    }

	public static void main(String[] args) {
        Dao.getInstance().beginTransaction();
		new AdminInstaller();
        Dao.getInstance().commitTransaction();
	}
}