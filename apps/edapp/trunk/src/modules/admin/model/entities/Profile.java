package modules.admin.model.entities;

import modules.admin.model.core.AdminConstants;
import modules.admin.model.core.DefaultProfile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge Rafael
 */
@Entity
public class Profile implements DefaultProfile, Serializable {

	@Id
	@Column(length = 30, nullable = false)
	private String profileId;

	@Column(length = 100, nullable = false)
	private String label;

	@Lob
	private String description;

	private boolean status;

	@OrderBy("smallTitle asc")
	@ManyToMany
	private List<Module> modules;

	@OrderBy("title asc")
	@ManyToMany
	private List<Role> roles;

	@OrderBy("label asc")
	@ManyToMany
	private List<Profile> allowedProfiles;

	public Profile() {
	}
	
	public String getId(){
		return profileId;
	}

	public Profile(String profileId, String label, String description) {
		this.profileId = profileId;
		this.label = label;
		this.description = description;
	}

	public Profile(String profileId, String label, boolean status) {
		this.profileId = profileId;
		this.label = label;
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Profile> getAllowedProfiles() {
		if (allowedProfiles == null) {
			allowedProfiles = new ArrayList<Profile>();
		}
		return allowedProfiles;
	}

	public void setAllowedProfiles(List<Profile> allowedProfiles) {
		this.allowedProfiles = allowedProfiles;
	}

	@Override
	public String toString() {
		return label;
	}

	public boolean hasRole(String roleId) {
		if(this.getRoles()!=null && !this.getRoles().isEmpty()){
		  if (this.getRoles().get(0).getRoleId().equals(AdminConstants.SUPER_ID)) {
			return true;
		  } else {
			for (Role role : this.getRoles()) {
				if (role.getRoleId().equals(roleId)) {
					return true;
				}
			}
		  }	
		}		
		return false;
	}

	public boolean hasModule(String moduleId) {
		if(this.getModules()!=null && !this.getModules().isEmpty()){
		  if (this.getModules().get(0).getModuleId().equals(AdminConstants.SUPER_ID)) {
			return true;
		  } else {
			for (Module module : this.getModules()) {
				if (module.getModuleId().equals(moduleId)) {
					return true;
				}
			}
		  }
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Profile other = (Profile) obj;
		if ((this.profileId == null) ? (other.profileId != null) : !this.profileId.equals(other.profileId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.profileId != null ? this.profileId.hashCode() : 0);
		return hash;
	}
}
