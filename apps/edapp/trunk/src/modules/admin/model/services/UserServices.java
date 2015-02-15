package modules.admin.model.services;

import modules.admin.model.entities.Log;
import modules.admin.model.entities.Profile;
import modules.admin.model.entities.User;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.dao.ProfileDao;
import modules.admin.model.dao.UserDao;
import modules.admin.model.entities.enums.AdminRolesEnum;
import modules.admin.model.entities.enums.LogType;
import modules.admin.model.exceptions.ExpiredPasswordException;
import modules.admin.model.exceptions.DisabledUserException;
import modules.admin.model.exceptions.InvalidUserOrPasswordException;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.Is;

import java.util.List;

/**
 *
 * @author leandro
 */
public class UserServices {

	/**
	 * Validates and returns the authenticated user in the input.
	 * 
	 * @param formUser the user with the attributes to be tested.
	 * @return the authenticated user, null if it's not.
	 *
	 * usuario/senha inválido
	 * inativado
	 * 
	 * @throws modules.admin.model.exceptions.InvalidUserOrPasswordException
	 * @throws modules.admin.model.exceptions.DisabledUserException
	 */
	public static User authenticatedUser(User formUser) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		final boolean accessByMail = Is.validMail(formUser.getAccessKey());
		User dbUser = getUserFromBase(formUser, accessByMail);
		String login = extractLogin(dbUser, formUser, accessByMail);
		formUser.setLogin(login);
		validateAccess(formUser, dbUser);
		return dbUser;
	}

	private static void validateAccess(User formUser, User dbUser) throws InvalidUserOrPasswordException, DisabledUserException, ExpiredPasswordException {
		if (dbUser == null || !passwordsMatches(formUser, dbUser)) {
			throw new InvalidUserOrPasswordException();
		} else {
			if (!dbUser.getStatus()) {
				throw new DisabledUserException();
			}
		}
	}

	private static boolean passwordsMatches(User formUser, User dbUser) {
		return formUser.encryptedPassword().equals(dbUser.getPassword());
	}

	/**
	 * Gets the user from DataBase
	 * @param formUser
	 * @param accessByMail
	 * @return
	 */
	private static User getUserFromBase(User formUser, final boolean accessByMail) {
		User dbUser = null;
		if (accessByMail) {
			dbUser = UserDao.getByEmail(formUser.getAccessKey());
		} else {
			if (!formUser.getAccessKey().trim().equals("")) {
				dbUser = UserDao.getByLogin(formUser.getAccessKey());
			}
		}
		return dbUser;
	}

	/**
	 * Gets the correctt login from the 'try' user.
	 * @param dbUser
	 * @param formUser
	 * @param accessByMail
	 * @return
	 */
	private static String extractLogin(User dbUser, User formUser, boolean accessByMail) {
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

	public static User authenticatedAndDetachedUser(User formUser) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		User tryUser = authenticatedUser(formUser);
		detached(tryUser);
		tryUser.setPlainPassword(formUser.getPlainPassword());
		return tryUser;
	}

	public static void logAccess(User user, String ipHost) {
		Dao.getInstance().save(new Log(null, LogType.LOGIN, user, ipHost));
	}

	public static User detached(User user) {
		if (user.getProfile() != null) {
			user.getProfile().getRoles().size(); //touch
			user.getProfile().getModules().size(); //touch
			if (user.getProfile().getAllowedProfiles() != null) {
				user.getProfile().getAllowedProfiles().size(); //touch
			}
		}
		Dao.getInstance().evict(user);

		if (user.getProfile() != null) {
			Dao.getInstance().evict(user.getProfile());
		}
		return user;
	}

	public static User updateEmailUser(String login, String newEmail) {
		User user = UserDao.getByLogin(login);
		user.setEmail(newEmail);
		Dao.getInstance().update(user);
		return user;
	}
	
	public static List<Profile> getReallyAllowedProfiles(User user) {
		if (user.hasRole(AdminConstants.SUPER_ID) || ((user.hasRole(AdminRolesEnum.USER_PROFILE) || user.hasRole(AdminRolesEnum.PROFILER)) && user.getProfile().getAllowedProfiles().isEmpty())) {
			return (ProfileDao.listAllOrderByLabel());
		}else{
			return (user.getProfile().getAllowedProfiles());
		}
	}

	public static DefaultUser authenticatedAndDetachedUser(String login, String password) throws InvalidUserOrPasswordException, ExpiredPasswordException {
		User user = new User();
		user.setAccessKey(login);
		user.setPassword(password);
		return authenticatedAndDetachedUser(user);
	}

	public static User getByIdentiedHash(String loggedValue) {
		User dbUser = UserDao.getByLogin(loggedValue.split("#")[0]);
		if (dbUser.identifiedHashToStore().equals(loggedValue)) {
			return dbUser;
		}
		return null;
	}
}