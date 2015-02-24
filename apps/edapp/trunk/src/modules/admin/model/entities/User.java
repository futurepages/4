package modules.admin.model.entities;

import com.vaadin.server.Resource;
import modules.admin.model.dao.LogDao;
import modules.admin.model.services.UserServices;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.resource.UploadedResource;
import org.futurepages.core.services.EntityForServices;
import org.futurepages.util.Is;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity
public class User implements DefaultUser, Serializable, EntityForServices<UserServices> {

	@Id
	@Size(max=30)
	private String login;

	@NotEmpty
	@Size(max=120)
	private String fullName;

	@NotEmpty
	private String encriptPassword;

	@NotEmpty
	@Email
 	private String email;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar birthDate;

	private boolean status;

	@Transient
	private String plainPassword;

	@Transient
	private String oldPassword;

	@Transient
	private String newPassword;

	@Transient
	private String newPasswordAgain = "";

	@Transient
	private String accessKey;

	@Transient
	private List<Module> modules;

	@Transient
	private List<Role> roles;

	@ManyToOne
	private Profile profile;

	@Transient
	private List<Log> lastAccesses;

	private String avatarValue;

	@Transient
	private String oldAvatarValue;


	public List<Log> getLastAccesses(int size) {
		if (lastAccesses == null) {
			lastAccesses = LogDao.topLastAccessesByUser(size, this.login);
		}
		return lastAccesses;
	}

	public User() {
	}

	public User(DefaultUser defaultUser) {
		this.login = defaultUser.getLogin();
	}
	
	public String getAccessKey() {
		return this.accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPlainPassword() {
		return this.plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getEncriptPassword() {
		return encriptPassword;
	}

	public void setEncriptPassword(String encriptPassword) {
		this.encriptPassword = encriptPassword;
	}

	@Override
	public String getPassword() {
		return getEncriptPassword();
	}

	public void setPassword(String plainPassword) {
		this.setPlainPassword(plainPassword);
		this.setEncriptPassword(this.encryptedPassword(plainPassword));
	}

	public void generatePassword() {
		setPassword(plainPassword);
	}

	public Calendar getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public List<Module> getModules() {
		if (modules == null) {
			modules = services().getModules(this);
		}
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			roles = services().getRoles(this);
		}
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return this.login;
	}

	public String encryptedPassword(String plainPassword) {
		return services().encriptedPassword(this, plainPassword);
	}

	public String encryptedPassword() {
		return encryptedPassword(this.plainPassword);
	}

	public boolean hasProfile() {
		return profile != null;
	}

	public boolean hasTheModule(String moduleId) {
		return services().hasTheModule(this,moduleId);
	}

	public boolean hasTheRole(String roleId) {
		return services().hasTheRole(this,roleId);
	}

	public boolean hasModule(String moduleId){
		return services().hasModule(this, moduleId);
	}

	public boolean hasRole(String roleId){
		return services().hasRole(this, roleId);
	}

	public boolean hasModules() {
		return services().hasModules(this);
	}

	public boolean profileHasRole(String roleId) {
		if (profile != null) {
			if (!profile.getRoles().isEmpty()) {
				return profile.hasRole(roleId);
			} else {
				return false;
			}
		}
		return false;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordAgain() {
		return newPasswordAgain;
	}

	public void setNewPasswordAgain(String newPasswordAgain) {
		this.newPasswordAgain = newPasswordAgain;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getInfo() {
		if (this.hasModules()) {
			return "Usuário Administrativo";
		} else {
			return "Acesso Limitado";
		}
	}

	public boolean hasRole(DefaultRole role) {
		return this.hasRole(role.getRoleId());
	}

	public boolean canUpdate(User user) {
		return services().canUpdate(this, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		return !((this.login == null) ? (other.login != null) : !this.login.equals(other.login));
	}

	public boolean containsRole(String roleId) {
		for (Role role : getRoles()) {
			if (role.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsModule(String moduleId) {
		for (Module module : getModules()) {
			if (module.getModuleId().equals(moduleId)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasSuperRole() {
		return services().hasSuperRole(this);
	}

	public String identifiedHashToStore() {
		return services().getIdentifiedHashToStore(this);
	}

	private void treatNewPassword() {
		if(!Is.empty(this.getNewPassword())){
			this.setPassword(this.getNewPassword());
        }
	}

	@Override
	public void prepareToCreate() {
	}

	@Override
	public void prepareToUpdate() {
		treatNewPassword();
		treatAvatarFiles();
	}

	private void treatAvatarFiles() {
		services().treatAvatarFiles(this);
	}

	@Override
	public void prepareToRead() {
	}

	@Override
	public void prepareToDelete() {
	}

	@Override
	public UserServices services() {
		return UserServices.getInstance();
	}

	public Resource getAvatarRes() {
		return services().avatarRes(this);
	}

	public UploadedResource getOldAvatarRes() {
		if (!Is.empty(oldAvatarValue)) {
			return new UploadedResource(this, this.getOldAvatarValue());
		}
		return null;
	}

	public String getAvatarValue() {
		return avatarValue;
	}

	public void setAvatarValue(String avatarValue) {
		if(oldAvatarValue!=null && !oldAvatarValue.equals(avatarValue)){
			this.oldAvatarValue = this.avatarValue;
		}
		this.avatarValue = avatarValue;
	}

	public String getOldAvatarValue() {
		return oldAvatarValue;
	}
}