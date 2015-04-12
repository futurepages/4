package modules.admin.model.entities;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import modules.admin.model.dao.LogDao;
import modules.admin.model.services.UserServices;
import modules.global.model.entities.brasil.Cidade;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.resource.UploadedResource;
import org.futurepages.core.services.EntityForServices;
import org.futurepages.core.view.annotations.FieldCustom;
import org.futurepages.core.view.annotations.FieldDelete;
import org.futurepages.core.view.annotations.FieldDependency;
import org.futurepages.core.view.annotations.FieldHTML;
import org.futurepages.core.view.annotations.FieldImage;
import org.futurepages.core.view.annotations.FieldPassword;
import org.futurepages.core.view.annotations.FieldStartGroup;
import org.futurepages.core.view.annotations.FieldStartGroupIcon;
import org.futurepages.core.view.annotations.FieldUpdate;
import org.futurepages.core.view.annotations.ForView;
import org.futurepages.core.view.annotations.PreSelectDependency;
import org.futurepages.core.view.types.EntityGenderType;
import org.futurepages.core.view.types.FieldGroupType;
import org.futurepages.util.Is;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.List;

@Entity
@ForView
public class User implements DefaultUser, EntityForServices<UserServices> {

	@FieldStartGroup(label = "Txt:$this.basic_info",type = FieldGroupType.TAB)
	@FieldStartGroupIcon(FontAwesome.USER)
	@FieldUpdate
	@FieldImage(image= "avatarRes", noImage = "defaultAvatarRes")
	@FieldCustom(floatLeft="1:3")
	private String avatarValue;

	@Id
	@NotEmpty
	@FieldUpdate(readOnly = true)
	private String login;

	@NotEmpty
	@FieldUpdate
	@Size(max = 120)
	private String fullName;

	@ManyToOne
	@FieldUpdate
	@FieldDependency(showAttr = "label")
	private Profile profile;

	@NotEmpty
	@Email
	@FieldUpdate
 	private String email;

	@Past
	@Temporal(TemporalType.DATE)
	@FieldUpdate
	private Calendar birthDate;

	@ManyToOne
	@FieldUpdate
	@FieldDependency(showAttr = "nome", orderBy = "nome asc",
			pre = {@PreSelectDependency(groupBy = "estado",showAttr = "nome",orderBy = "nome asc")}
	)
	private Cidade birthCity;

	@FieldDelete(visibleWithRole=true)
	@FieldUpdate
	private boolean status;

	@Lob
	@FieldUpdate
	private String obs;

	@FieldStartGroup(label = "Txt:$this.about",type = FieldGroupType.TAB)
	@FieldStartGroupIcon(FontAwesome.INFO)
	@Lob
	@FieldUpdate
	@FieldHTML
	private String about;

	@Transient
	@FieldUpdate
	@FieldPassword
	@FieldStartGroup(label = "Txt:$this.password",type = FieldGroupType.TAB)
	@FieldStartGroupIcon(FontAwesome.KEY)
	private String oldPassword;

	@Transient
	@FieldUpdate
	@FieldPassword
	private String newPassword;

	@Transient
	@FieldUpdate
	@FieldPassword
	private String newPasswordAgain;

	@Transient
	private String accessKey;

	@Transient
	private String plainPassword;

	private String encriptPassword;

	@Transient
	private List<Module> modules;

	@Transient
	private List<Role> roles;

	@Transient
	private List<Log> lastAccesses;

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

	public Resource getDefaultAvatarRes(){
		return services().getDefaultAvatar(this);
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
		if(profile==null || this.profile == null ||  !services().dao().areEqualsById(profile,this.profile)){
			roles = null;
			modules = null;
		}
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
		return services().getInfo(this);
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

	public Cidade getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(Cidade birthCity) {
		this.birthCity = birthCity;
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
		if(oldAvatarValue==null || !oldAvatarValue.equals(avatarValue)){
			this.oldAvatarValue = this.avatarValue;
		}
		this.avatarValue = avatarValue;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getOldAvatarValue() {
		return oldAvatarValue;
	}
}