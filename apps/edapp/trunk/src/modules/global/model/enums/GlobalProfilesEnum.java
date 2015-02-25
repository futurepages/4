package modules.global.model.enums;

import modules.admin.model.core.AdminConstants;
import modules.admin.model.core.DefaultProfile;
import modules.admin.model.dao.ModuleDao;
import modules.admin.model.dao.RoleDao;
import modules.admin.model.entities.Module;
import modules.admin.model.entities.Profile;
import modules.admin.model.entities.Role;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.persistence.Dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jorge Rafael
 */
public enum GlobalProfilesEnum implements DefaultProfile, AdminConstants, Serializable {


	SUPER ("globalSuper" ,
			"Administrador do Entidades Globais",
			"Cadastra, altera, valida, (in)defere cadastros de vinculados ao PJPI.",
			GlobalRolesEnum.ADMIN
	)


	;

	private String profileId;
	private String label;
	private String description;
	private List<DefaultRole> roles;

	public static void install(){
		for(GlobalProfilesEnum profileEnum : GlobalProfilesEnum.values()){
			Profile profile = new Profile();
			profile.setProfileId(profileEnum.getProfileId());
			profile.setDescription(profileEnum.getDescription());
			profile.setLabel(profileEnum.getLabel());
			profile.setModules(new ArrayList<>());
			profile.setRoles(new ArrayList<>());
			profile.getModules().add(ModuleDao.getById("global"));

			for(DefaultRole role : profileEnum.roles){
				profile.getRoles().add(RoleDao.get(role));
			}

			Dao.getInstance().save(profile);
		}
	}

	private GlobalProfilesEnum(String profileId, String label, String description, DefaultRole... roles) {
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