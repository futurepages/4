package modules.admin.model.entities.enums;

import java.io.Serializable;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.dao.RoleDao;
import org.futurepages.core.auth.DefaultRole;

/**
 *
 * @author Jorge Rafael
 */
public enum AdminRolesEnum implements DefaultRole, Serializable, AdminConstants {

	SUPER		 (SUPER_ID		      , "(*) Todos os Papéis"),
	PROFILER	 ("adminProfiler"     , "Atribui, retira e altera qualquer perfil"),
	PARAMS	  	 ("adminParams"	      , "Atualiza Parâmetros do sistema"),
	LOGS		 ("adminLogs"	      , "Visualiza logs de ações e acessos ao sistema"),
	USER_CHANGE  ("adminUserChange"	  , "Cria usuário e altera informações de nome e email (este quando possível) de usuários do sistema."),
	USER_STATUS  ("adminUserStatus"	  , "Habilita/desabilita usuários."),
	USER_PROFILE ("adminUserProfile"  , "Atribui e retira perfis dos usuários (todos ou somente os restringidos)."),
	USER_PASSWORD("adminUserPassword" , "Altera e cancela a expiração de senha de usuários vinculados ao PJPI. "),
	;

	private String roleId;
	private String title;

	public static void install(){
		for(AdminRolesEnum role : AdminRolesEnum.values()){
			if(role.getRoleId().equals(SUPER_ID)){
				RoleDao.save(role, role.getTitle(), SUPER_ID);
			}else{
				RoleDao.save(role, role.getTitle(), "admin");
			}
		}
	}

	private AdminRolesEnum(String roleId, String title) {
		this.roleId = roleId;
		this.title = title;
	}

	@Override
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return roleId;
	}
}