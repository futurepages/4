package modules.global.model.enums;


import java.io.Serializable;

import org.futurepages.core.auth.DefaultRole;

/**
 *
 * @author Leandro
 */
public enum GlobalRolesEnum implements DefaultRole, Serializable {


	ADMIN("globalAdmin", "Administração Avançada do Módulo"),
	ORGAO_COMPLETO("globalOrgaoCompleto", "Altera todos os campos do órgão"),
	ORGAO_SIMPLES("globalOrgaoSimples", "Altera apenas alguns campos do órgão")
	;

	private String roleId;
	private String title;

	public static void install(){
		for(GlobalRolesEnum role : GlobalRolesEnum.values()){
//		   RoleDao.save(role, role.getTitle(), "global");
		}
	}

	private GlobalRolesEnum(String roleId, String title) {
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