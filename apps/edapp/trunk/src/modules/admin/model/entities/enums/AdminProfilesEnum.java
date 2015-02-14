package modules.admin.model.entities.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modules.admin.model.entities.Module;
import modules.admin.model.entities.Profile;
import modules.admin.model.entities.Role;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.core.DefaultProfile;
import modules.admin.model.dao.ModuleDao;
import modules.admin.model.dao.RoleDao;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jorge Rafael
 */
public enum AdminProfilesEnum implements DefaultProfile, AdminConstants, Serializable {

	BASIC         ("adminBasic" ,
				  "(Admin) Ativador/Inativador de Usuários" ,
				  "Lista e consulta informações básicas de usuários."
				  ),

	ENROLLER     ("adminEnroller" ,
				  "(Admin) Atribuidor de Perfis" ,
				  "Lista todos os usuários, altera/inativa/ativa e atribui os perfis que desejar atribuir.",
				  AdminRolesEnum.PROFILER
				  ),

	AUDITOR		  ("adminLogs"  , 
				   "(Admin) Auditor de Logs",
				   "Lista e consulta informações básicas de usuários e visualiza/auditora Logs do Sistema",
				   AdminRolesEnum.LOGS
				   ),


	ADMIN_SUPER   ("adminSuper" ,
				   "(Admin) Administrador do Módulo",
				   "Cria, atualiza usuários, modifica/atribui perfis, visualiza Logs e altera parâmetros do sistema.",
				   AdminRolesEnum.LOGS, AdminRolesEnum.PARAMS, AdminRolesEnum.PROFILER, AdminRolesEnum.USER_CHANGE,AdminRolesEnum.USER_PASSWORD,AdminRolesEnum.USER_STATUS,AdminRolesEnum.USER_PROFILE
				   ),

				   
	SYSTEM_SUPER  ("super",
				   "Administrador Super do Sistema",
				   "Possui todos as permissões de módulos e papéis gerais possíveis no sistema. Obs.: A exceção são as permissões próprias de tipos específicos de usuários.",
				   AdminRolesEnum.SUPER
				   ),
	;

	private String profileId;
	private String label;
	private String description;
	private List<DefaultRole> roles;

	public static void install(){
		for(AdminProfilesEnum profileEnum : AdminProfilesEnum.values()){
			String moduleId = null;
			if(profileEnum.roles.size()==1 && profileEnum.roles.get(0).getRoleId().equals(SUPER_ID)) {
				moduleId = SUPER_ID;
			}else{
				moduleId = "admin";
			}
			Profile profile = new Profile();
			profile.setProfileId(profileEnum.getProfileId());
			profile.setDescription(profileEnum.getDescription());
			profile.setLabel(profileEnum.getLabel());
			profile.setModules(new ArrayList<Module>());
			profile.setRoles(new ArrayList<Role>());
			profile.getModules().add(ModuleDao.getById(moduleId));
			for(DefaultRole role : profileEnum.roles){
				profile.getRoles().add(RoleDao.get(role));
			}
			Dao.getInstance().save(profile);
		}
	}

	private AdminProfilesEnum(String profileId, String label, String description, DefaultRole... roles) {
		this.profileId = profileId;
		this.label = label;
		this.description = description;

		this.roles = new ArrayList();
		Collections.addAll(this.roles, roles);
	}


	@Override
	public String toString() {
		return profileId;
	}

	public String getProfileId(){
		return this.profileId;
	}

	public String getLabel(){
		return this.label;
	}

	public String getDescription(){
		return this.description;
	}

}