package modules.admin.install;


import modules.admin.beans.Param;
import modules.admin.beans.User;
import modules.admin.core.AdminConstants;
import modules.admin.dao.ModuleDao;
import modules.admin.dao.ProfileDao;
import modules.admin.enums.AdminProfilesEnum;
import modules.admin.enums.AdminRolesEnum;
import modules.admin.enums.ParamEnum;
import modules.admin.enums.ParamValueType;
import org.futurepages.core.config.Params;

import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;

/**
 * Inserções Iniciais Padrões do Módulo Admin
 */
public class AdminInstaller extends Installer implements AdminConstants {

    public void execute() {
        ModuleDao.save("admin", "Administração", "Administração de Usuários/Permissões");
        ModuleDao.save(SUPER_ID, "(*) Todos os Módulos","(*) Todos os Módulos");
        
		AdminRolesEnum.install();
		AdminProfilesEnum.install();
		
		//Criação do usuario 'super'
        User userAdmin = new User();
        userAdmin.setLogin("admin");
        userAdmin.setFullName("Administrador Padrão do Sistema");
        userAdmin.setEmail("admin@admin.com");
		
		if(Params.get("DEPLOY_MODE").equals("none")){
			userAdmin.setPassword("admin.senha");
		}else{
			userAdmin.setPassword("admin.user#temp.pswd");
		}
        userAdmin.setStatus(true);

		userAdmin.setProfile(ProfileDao.get(AdminProfilesEnum.SYSTEM_SUPER));

        Dao.save(userAdmin);
		
		// Criação do parâmetro que verifica se a senha expirou
		Param param = new Param(ParamEnum.EXPIRED_PASS_IN_DAYS, ParamValueType.INT, "30", "Quantidade de dias para a senha expirar", 365);
		
		Dao.save(param);
    }
}