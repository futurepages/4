package modules.admin.model.services;

import com.vaadin.server.Resource;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.dao.ModuleDao;
import modules.admin.model.dao.ProfileDao;
import modules.admin.model.dao.RoleDao;
import modules.admin.model.dao.UserDao;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.Module;
import modules.admin.model.entities.Profile;
import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.entities.enums.AdminProfilesEnum;
import modules.admin.model.entities.enums.AdminRolesEnum;
import modules.admin.model.entities.enums.LogType;
import modules.admin.model.exceptions.DisabledUserException;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import modules.admin.model.validators.UserValidator;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.resource.UploadedResource;
import org.futurepages.core.resource.UploadedTempResource;
import org.futurepages.core.services.EntityServices;
import org.futurepages.util.Is;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.Security;
import org.futurepages.util.The;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leandro
 */
public class UserServices extends EntityServices<UserDao, User> implements AdminConstants {


	public static UserServices getInstance() {
		return getInstance(UserServices.class, UserValidator.class, UserDao.class);
	}

	/**
	 * Validates and returns the authenticated user in the input.
	 * 
	 * @param inputUser the user with the attributes to be tested.
	 * @return the authenticated user, null if it's not.
	 *
	 * usuario/senha inválido
	 * inativado
	 * 
	 * @throws modules.admin.model.exceptions.InvalidUserOrPasswordException
	 * @throws modules.admin.model.exceptions.DisabledUserException
	 */
	public User authenticatedUser(User inputUser) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		final boolean accessByMail = Is.validMail(inputUser.getAccessKey());
		User dbUser = get(inputUser, accessByMail);
		String login = extractLogin(dbUser, inputUser, accessByMail);
		inputUser.setLogin(login);
		validateAccess(inputUser, dbUser);
		return dbUser;
	}

	private void validateAccess(User inputUser, User dbUser) throws InvalidUserOrPasswordException, DisabledUserException, ExpiredPasswordException {
		if (dbUser == null || !passwordsMatches(inputUser, dbUser)) {
			throw new InvalidUserOrPasswordException();
		} else {
			if (!dbUser.getStatus()) {
				throw new DisabledUserException();
			}
		}
	}

	private boolean passwordsMatches(User inputUser, User dbUser) {
		return inputUser.encryptedPassword().equals(dbUser.getPassword());
	}

	/**
	 * Gets the user from DataBase
	 */
	private User get(User inputUser, final boolean accessByMail) {
		User dbUser = null;
		if (accessByMail) {
			dbUser = dao.getByEmail(inputUser.getAccessKey());
		} else {
			if (!inputUser.getAccessKey().trim().equals("")) {
				dbUser = dao.get(inputUser.getAccessKey());
			}
		}
		return dbUser;
	}

	/**
	 * Gets the correct login from the 'try' user.
	 */
	private String extractLogin(User dbUser, User formUser, boolean accessByMail) {
		String login = "";
		if (accessByMail) {
			if (dbUser != null) {
				login = dbUser.getLogin();
			}
		} else {
			login = formUser.getAccessKey();
		}
		return login;
	}

	public User authenticatedAndDetachedUser(User formUser) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		User tryUser = authenticatedUser(formUser);
		detached(tryUser);
		tryUser.setPlainPassword(formUser.getPlainPassword());
		return tryUser;
	}

	public void logAccess(DefaultUser user, String ipHost) {
		dao.save(new Log(null, LogType.LOGIN, dao.get(user.getLogin()), ipHost));
	}

	public User detached(User user) {
		if (user.hasProfile()) {
			user.getProfile().getRoles().size(); //touch
			user.getProfile().getModules().size(); //touch
			if (user.getProfile().getAllowedProfiles() != null) {
				user.getProfile().getAllowedProfiles().size(); //touch
			}
		}

		if (user.hasProfile()) {
			dao.evict(user.getProfile());
		}
		dao.evict(user);
		return user;
	}

	public User updateEmail(String login, String newEmail) {
		User user = read(login);
		user.setEmail(newEmail);
		dao.update(user);
		return user;
	}
	
	public List<Profile> getReallyAllowedProfiles(User user) {
		if (user.hasRole(SUPER_ID) || ((user.hasRole(AdminRolesEnum.USER_PROFILE) || user.hasRole(AdminRolesEnum.PROFILER)) && user.getProfile().getAllowedProfiles().isEmpty())) {
			return (ProfileDao.listAllOrderByLabel());
		}else{
			return (user.getProfile().getAllowedProfiles());
		}
	}

	public DefaultUser authenticatedAndDetachedUser(String login, String password) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		User user = new User();
		user.setAccessKey(login);
		user.setPassword(password);
		return authenticatedAndDetachedUser(user);
	}

	public User getByIdentifiedHash(String loggedValue) {
		User dbUser = dao.get(loggedValue.split("#")[0]);
		if (dbUser.identifiedHashToStore().equals(loggedValue)) {
			return dbUser;
		}
		return null;
	}

	public String getIdentifiedHashToStore(User user) {
		 return The.concat(user.getLogin(), "#", Security.md5(The.concat(user.getLogin(), "||", user.getPassword())));
	}

	public boolean hasHolesThatMathses(User user, String roleId) {
		if (roleId.startsWith(SUPER_ID)) {
			String sufix = roleId.substring(roleId.indexOf(SUPER_ID) + 1, roleId.length());
			for (Role role : user.getRoles()) {
				if (role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		} else if (roleId.endsWith(SUPER_ID)) {
			String prefixRole = roleId.substring(0, roleId.indexOf(SUPER_ID));
			for (Role role : user.getRoles()) {
				if (role.getRoleId().startsWith(prefixRole)) {
					return true;
				}
			}
		} else { // '*' in the middle
			String prefix = roleId.substring(0, roleId.indexOf(SUPER_ID));
			String sufix = roleId.substring(roleId.indexOf(SUPER_ID) + 1, roleId.length());
			for (Role role : user.getRoles()) {
				if (role.getRoleId().startsWith(prefix) && role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasSuperRole(User user) {
		for (Role role : user.getRoles()) {
			if (role.getRoleId().equals(SUPER_ID)) {
				return true;
			}
		}
		return false;
	}

	public List<Module> getModules(User user) {
		List<Module> modules;
		if (user.getProfile() != null) {
			modules = user.getProfile().getModules();
			//Has module(*) - returns all the modules
			if (user.getProfile().hasModule(SUPER_ID)) {
				modules = ModuleDao.listOrderByTitle();
			}
		} else {
			modules = new ArrayList<Module>();
		}
		return modules;
	}

	public List<Role> getRoles(User user) {
		List<Role> roles;
		if (user.getProfile() != null) {
			roles = user.getProfile().getRoles();
			//Has role(*) - returns all the roles
			if (user.getProfile().hasRole(SUPER_ID)) {
				roles = RoleDao.listOrderByTitle();
			}
		} else {
			roles = new ArrayList<>();
		}
		return roles;
	}

	public boolean hasModule(User user, String moduleId) {
			boolean allowModuleId = true;
			if (!Is.empty(moduleId)) {
			allowModuleId = false;
			if(moduleId.contains(",")){
				String[] modulesIds = moduleId.split(",");
				for(String moduleOne : modulesIds){
					allowModuleId = user.hasTheModule(moduleOne);
					if(allowModuleId){
						break;
					}
				}
			}else{
				allowModuleId = user.hasTheModule(moduleId);
			}
		}
		return allowModuleId;
	}

	public boolean hasRole(User user, String roleId) {
		boolean allowRoleId = true;
		if (!Is.empty(roleId)) {
			allowRoleId = false;
			if(roleId.contains(",")){
				String[] roleIds = roleId.split(",");
				for(String roleOne : roleIds){
					allowRoleId = user.hasTheRole(roleOne);
					if(allowRoleId){
						break;
					}
				}
			}else{
				allowRoleId = user.hasTheRole(roleId);
			}
		}
		return allowRoleId;
	}

	/**
	 * @return true se o usuário é diferente de nulo e não possui módulos.
	 */
	public boolean hasModules(User user) {
		if (user.getModules() != null) {
			if (user.getModules().size() > 0) {
				return true;
			}
		}
		return false;
	}

	public String encriptedPassword(User user, String inputPassword) {
		return Security.md5(The.concat(user.getLogin(), SECURITY_KEY, inputPassword));
	}

	/**
	 * Indica se o usuário está apto a alterar outro
	 *
	 * Só pode alterar se possui o módulo admin (o que já é implícito)
	 * e se for do mesmo perfil ou seu perfil pode alterar aquele perfil ou é SUPER.
	 *
	 * @return true if it's possible.
	 */
	public boolean canUpdate(User userUpdater, User userToUpdate) {
		if(userUpdater.hasModule(ModuleUtil.moduleId(this.getClass()))){

			if (userUpdater.getProfile().getProfileId().equals(AdminProfilesEnum.SYSTEM_SUPER.getProfileId())){
				return true;
			}
			if(userUpdater.hasRole(AdminRolesEnum.USER_CHANGE)
			|| userUpdater.hasRole(AdminRolesEnum.USER_STATUS)
			|| userUpdater.hasRole(AdminRolesEnum.USER_PASSWORD)
			|| userUpdater.hasRole(AdminRolesEnum.USER_PROFILE)
			|| userUpdater.hasRole(AdminRolesEnum.PROFILER)){

				if(userUpdater.getProfile().getAllowedProfiles()==null || userUpdater.getProfile().getAllowedProfiles().isEmpty()){
					return true;
				}
				else if (userUpdater.getProfile().getAllowedProfiles().contains(userToUpdate.getProfile())) {
				         return true;
			    }
			}
		 }
		return false;
	}

	public void treatAvatarFiles(User user) {
		File endFile = (new UploadedResource(user,user.getAvatarValue())).getSourceFile();
		if(!endFile.exists()){
			File tempFile = new UploadedTempResource(user.getAvatarValue()).getSourceFile();
			if(tempFile.exists()){
				boolean renamed = tempFile.renameTo(endFile);
				if (!renamed) {
					throw new RuntimeException(new IOException("Unable to rename file " + tempFile.getAbsolutePath() + " to " + endFile.getAbsolutePath()));
				}
			}
		}
		try{
			UploadedResource oldAvatarRes = user.getOldAvatarRes();
			if (oldAvatarRes != null) {
				boolean deleted = oldAvatarRes.getSourceFile().delete();
				if (!deleted) {
					AppLogger.getInstance().execute(new IOException("Unable to delete " + oldAvatarRes.getSourceFile().getAbsolutePath()));
				}
			}
		}catch(Exception ex){
					AppLogger.getInstance().execute(ex);
		}
	}

	public Resource avatarRes(User user) {
		if(!Is.empty(user.getAvatarValue())){
			return new UploadedResource(user, user.getAvatarValue());
		}
		return AVATAR_DEFAULT_RES;
	}

	/**
	 * You can find using '*'. Only one in the 3 possible cases: - in the
	 * beginning, ex.: "*aaa" - in the middle, ex.: "aaa*bbbb" - or in the end
	 * of the parameter. ex.: "aaaa*"
	 *
	 * WARNING: Get the roles from the profile, but need to be proc
	 *
	 * @return true if user has roleId
	 */
	public boolean hasTheRole(User user, String roleId) {
				if (roleId != null) {
			if (user.getRoles() == null) {
				return false;
			}
			if (!roleId.contains(SUPER_ID)) {
				if (user.containsRole(roleId)) {
					return true;
				}
			} else { //contains '*'
				if (roleId.length() > 1) { //that means: not equals '*' but contains '*'
					if (hasRolesThatMatches(user,roleId)) {
						return true;
					}
				} else {
					if (user.hasSuperRole()) {
						return true;
					}
				}
			}
		} else { // roleId == null
			return true;
		}
		return false;
	}

	/**
	 * @return true if user has moduleId
	 */
	public boolean hasTheModule(User user, String moduleId) {
		if (moduleId != null) {
			if (user.getModules() == null) {
				return false;
			}
			if (!moduleId.contains(SUPER_ID)) {
				if (user.containsModule(moduleId)) {
					return true;
				}
			}
		} else { // moduleId == null
			return true;
		}
		return false;
	}

	private boolean hasRolesThatMatches(User user, String roleId) {
		if (roleId.startsWith(SUPER_ID)) {
			String sufix = roleId.substring(roleId.indexOf(SUPER_ID) + 1, roleId.length());
			for (Role role : user.getRoles()) {
				if (role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		} else if (roleId.endsWith(SUPER_ID)) {
			String prefixRole = roleId.substring(0, roleId.indexOf(SUPER_ID));
			for (Role role : user.getRoles()) {
				if (role.getRoleId().startsWith(prefixRole)) {
					return true;
				}
			}
		} else { // '*' in the middle
			String prefix = roleId.substring(0, roleId.indexOf(SUPER_ID));
			String sufix = roleId.substring(roleId.indexOf(SUPER_ID) + 1, roleId.length());
			for (Role role : user.getRoles()) {
				if (role.getRoleId().startsWith(prefix) && role.getRoleId().endsWith(sufix)) {
					return true;
				}
			}
		}
		return false;
	}
}