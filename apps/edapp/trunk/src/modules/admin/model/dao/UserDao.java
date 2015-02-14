package modules.admin.model.dao;

import modules.admin.model.entities.User;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;

public class UserDao extends HQLProvider {

	public static final String DEFAULT_ORDER = asc("fullName");


	public static User getRootUser() {
		return UserDao.getByLogin("admin");
	}

	public static User get(String login) {
		return Dao.getInstance().get(User.class, login);
	}

	public static User getByLogin(String login) {
		String where = ands(
				field("login").equalsTo(login)          //,field("status").isTrue() //não descomentar sem estudar o caso.
		);
		return Dao.getInstance().uniqueResult(hql(User.class, where));
	}

	public static User getByEmail(String email) {
		return Dao.getInstance().uniqueResult(hql(User.class, field("email").equalsTo(email)));
	}

	public static boolean isMailMine(String login, String email) {
		boolean isMine = Dao.getInstance().uniqueResult(hql(User.class, ands(field("login").equalsTo(login) , field("email").equalsTo(email)))) != null;

		return isMine;
	}

	// buscar por: Perfil, Módulo e Role. Separadamente
	public static PaginationSlice<User> paginateList(int pageNum, int pageSize, String conteudoBusca, String profileId, String moduleId, String roleId) {

		String clauseProfile = field("profile.profileId").equalsTo(profileId);

		if (profileId != null) {
			if (profileId.equals("*")) {
				clauseProfile = field("profile.profileId").isNotNull();
			} else if (profileId.equals("[0]")) {
				clauseProfile = field("profile.profileId").isNull();
			}
		}

		String where = ands(ors(field("login").matches(conteudoBusca),
						field("email").matches(conteudoBusca),
						field("fullName").matches(conteudoBusca)),
				clauseProfile,
				field("profile.roles.roleId").equalsTo(roleId),
				field("profile.modules.moduleId").equalsTo(moduleId));
		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(User.class, where, DEFAULT_ORDER));
	}

	public static List<User> getListByModuleAndRole(String moduleId, String roleId) {
		String where = ands(field("profile.modules.moduleId").equalsTo(moduleId),
				field("profile.roles.roleId").equalsTo(roleId));
		return Dao.getInstance().list(hql(User.class, where, "login asc"));
	}

	public static List<User> getListByProfile(String profileId) {
		String where = field("profile.profileId").equalsTo(profileId);
		return Dao.getInstance().list(hql(User.class, where, "login asc"));
	}

	public static List<User> getListAll() {
		return Dao.getInstance().list(User.class);
	}

	public static List<User> listAllUsersWithAProfile(String profileId) {
		return Dao.getInstance().list(hql(User.class, ands(field("profile.profileId").equalsTo(profileId), field("status").isTrue()),asc("login")));
	}
}