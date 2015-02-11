package modules.admin.model.entities;

import modules.admin.model.core.AdminConstants;
import modules.admin.model.dao.LogDao;
import modules.admin.model.dao.ModuleDao;
import modules.admin.model.dao.RoleDao;
import modules.admin.model.entities.enums.AdminProfilesEnum;
import modules.admin.model.entities.enums.AdminRolesEnum;
import org.futurepages.core.admin.DefaultRole;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.util.Is;
import org.futurepages.util.Security;
import org.futurepages.util.The;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements DefaultUser, Serializable {

    private static final String KEY = "5JkWo73odsp1gCiHjnpKW6HwfqhR5WckqEZES2uIKMPiq8t0hrz92sA";


	@Id
	@Column(length = 30, nullable = false)
	private String login;

	@Column(length = 120, nullable = false)
	private String fullName;

	@Column(length = 255, nullable = false)
	private String encriptPassword;

	@Column(length = 100, unique = true, nullable = false)
	private String email;

	private boolean status;

	@Transient
	private String plainPassword;

	@Transient
	private String accessKey;

	@Transient
	private List<Module> modules;

	@Transient
	private List<Role> roles;

	@ManyToOne
	private Profile profile;

	@Transient
	private List<Log> ultimosAcessos;
	
	
	public List<Log> getLastAccesses() {

//		if (ultimosAcessos == null) {
//			ultimosAcessos = LogDao.topLastAccessesByUser(5, this.login);
//		}

		return ultimosAcessos;
	}

	public User() {
	}

	public User(DefaultUser defaultUser) {
		this.login = defaultUser.getLogin();
	}
	
	public static User newInstance(String fullName, String login, String plainPassword, String email) {
		User user = new User();
		user.setFullName(fullName);
		user.setLogin(login);
		user.setPassword(plainPassword);
		user.setEmail(email);
		user.setStatus(true);
		user.setProfile(null);
		return user;
	}		

	public static User fromDefault(DefaultUser defaultUser) {
		return new User(defaultUser);
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

	/**
	 * 'set' para valor da senha já criptografada.
	 *
	 * @param password
	 */
	@Deprecated
	public void setPersistentPassword(String password) {
		this.encriptPassword = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public List<Module> getModules() {
		if (modules == null) {
			if(this.getProfile()!=null){
				modules = this.getProfile().getModules();
				//Possui Módulo(*) - Retorna Todos os Módulos
				if (this.hasModule(AdminConstants.SUPER_ID)) {
					modules = ModuleDao.listOrderByTitle();
				}
			}else{
				modules = new ArrayList<Module>();
			}
		}
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			if(this.getProfile()!=null){
				roles = this.getProfile().getRoles();
				//Possui Role (*) - Retorna Todos os Roles
				if (this.hasRole(AdminConstants.SUPER_ID)) {
					roles = (RoleDao.listOrderByTitle());
				}
			}else{
				roles = new ArrayList<Role>();
			}
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
		return Security.md5(The.concat(login, KEY, plainPassword));
	}

	public String encryptedPassword() {
		return encryptedPassword(this.plainPassword);
	}

	public boolean hasProfile() {
		if (profile != null) {
			return true;
		}
		return false;
	}

	public boolean hasProfile(String profileId) {
		if (profile != null) {
			if (profile.getProfileId().equals(profileId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param moduleId
	 * @return true if user has moduleId
	 */
	public boolean hasOneModule(String moduleId) {
		if (moduleId != null) {
			if (getModules() == null) {
				return false;
			}
			for (Module module : getModules()) {
				if (module.getModuleId().equals(moduleId)) {
					return true;
				}
			}
		} else { // quando não for especificado nenhum módulo
			return true;
		}
		return false;
	}

	public boolean hasModule(String moduleId){
			boolean allowModuleId = true;
			if (!Is.empty(moduleId)) {
			allowModuleId = false;
			if(moduleId.contains(",")){
				String[] modulesIds = moduleId.split(",");
				for(String moduleOne : modulesIds){
					allowModuleId = allowModuleId || this.hasOneModule(moduleOne);
					if(allowModuleId){
						break;
					}
				}
			}else{
				allowModuleId = this.hasOneModule(moduleId);
			}
		}
		return allowModuleId;
	}

	public boolean hasRole(String roleId){
		boolean allowRoleId = true;
		if (!Is.empty(roleId)) {
			allowRoleId = false;
			if(roleId.contains(",")){
				String[] roleIds = roleId.split(",");
				for(String roleOne : roleIds){
					allowRoleId = allowRoleId || this.hasOneRole(roleOne);
					if(allowRoleId){
						break;
					}
				}
			}else{
				allowRoleId = this.hasOneRole(roleId);
			}
		}
		return allowRoleId;
	}

	/**
	 *
	 * @return true se o usuário é diferente de nulo e não possui módulos.
	 */
	public boolean hasModules() {
		if (this.getModules() != null) {
			if (getModules().size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * You can find using '*'. Only one in the 3 possible cases: - in the
	 * beginning, ex.: "*aaa" - in the middle, ex.: "aaa*bbbb" - or in the end
	 * of the parameter. ex.: "aaaa*"
	 *
	 * WARNING: Get the roles from the profile, but need to be proc
	 *
	 * @param roleId
	 * @return true if user has roleId
	 */
	private boolean hasOneRole(String roleId) {

		if (roleId != null) {
			if (getRoles() == null) {
				return false;
			}
			if (!roleId.contains("*")) {
				if (containsRole(roleId)) {
					return true;
				}
			} else { //contains '*'
				if (roleId.length() > 1) { //that means: not equals '*' but contains '*'
					if (hasRolesThatMatches(roleId)) {
						return true;
					}
				} else {
					if (hasSuperRole()) {
						return true;
					}
				}
			}
		} else { // roleId == null
			return true;
		}
		return false;
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

	/**
	 * @param field
	 * @return the hql string corresponding to: field = role[0] OR field =
	 * role[1] OR ... OR field = role[n]
	 */
	public String rolesLikeField(String field) {
		return User.rolesLikeField(getRoles(), field);
	}

	/**
	 * @param field
	 * @return the hql string corresponding to: field = role[0] OR field =
	 * role[1] OR ... OR field = role[n]
	 */
	public static String rolesLikeField(Collection<Role> roles, String field) {
		Role role;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < roles.size(); i++) {
			role = ((List<Role>) roles).get(i);
			sb.append((i > 0 ? " OR " + field : field)).append(" = '").append(role.getRoleId()).append("'");
		}
		sb.append((roles.size() > 0 ? " OR " + field + " = '*'" : " " + field + " = '*'"));
		return sb.toString();
	}

	/**
	 * @param field
	 * @return the hql string corresponding to: field = module[0] OR field =
	 * module[1] OR ... OR field = module[n]
	 */
	public String modulesLikeField(String field) {
		Module module = this.getModules().get(0);
		StringBuilder sb = new StringBuilder(field + " = '" + module.getModuleId() + "'");
		for (int i = 1; i < getModules().size(); i++) {
			module = getModules().get(i);
			sb.append(" OR ").append(field).append(" = '").append(module.getModuleId()).append("'");
		}
		return sb.toString();
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


	/**
	 * Indica se o usuário está apto a alterar outro
	 *
	 * Só pode alterar se possui o módulo admin (o que já é implícito)
	 * e se for do mesmo perfil ou seu perfil pode alterar aquele perfil ou é SUPER.
	 *
	 * @param user
	 * @return
	 */
	
	
	public boolean canUpdate(User user) {
		if(this.hasModule("admin")){
			
			if (this.getProfile().getProfileId().equals(AdminProfilesEnum.SYSTEM_SUPER.getProfileId())){
				return true;
			}

			if(this.hasRole(AdminRolesEnum.USER_CHANGE)
			|| this.hasRole(AdminRolesEnum.USER_STATUS)
			|| this.hasRole(AdminRolesEnum.USER_PASSWORD)	
			|| this.hasRole(AdminRolesEnum.USER_PROFILE)
			|| this.hasRole(AdminRolesEnum.PROFILER)		){
			    
				if(this.getProfile().getAllowedProfiles()==null || this.getProfile().getAllowedProfiles().isEmpty()){
					return true;
				}
				else if (this.getProfile().getAllowedProfiles().contains(user.getProfile())) {
				         return true;
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
		final User other = (User) obj;
		if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.login != null ? this.login.hashCode() : 0);
		return hash;
	}


	private boolean containsRole(String roleId) {
		for (Role role : getRoles()) {
			if (role.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasRolesThatMatches(String roleId) {
		if (roleId.startsWith("*")) {
			String sufix = roleId.substring(roleId.indexOf("*") + 1, roleId.length());
			for (Role role : getRoles()) {
				if (role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		} else if (roleId.endsWith("*")) {
			String prefixRole = roleId.substring(0, roleId.indexOf("*"));
			for (Role role : getRoles()) {
				if (role.getRoleId().startsWith(prefixRole)) {
					return true;
				}
			}
		} else { // '*' in the middle
			String prefix = roleId.substring(0, roleId.indexOf("*"));
			String sufix = roleId.substring(roleId.indexOf("*") + 1, roleId.length());
			for (Role role : getRoles()) {
				if (role.getRoleId().startsWith(prefix) && role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSuperRole(String roleId) {
		return roleId.equals(AdminConstants.SUPER_ID);
	}

	public boolean hasSuperRole() {
		for (Role role : getRoles()) {
			if (isSuperRole(role.getRoleId())) {
				return true;
			}
		}
		return false;
	}
}
