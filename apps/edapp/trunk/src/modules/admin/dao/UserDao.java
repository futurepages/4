package modules.admin.model.dao;

import modules.admin.model.entities.User;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.SpecificDao;

import java.util.List;

public class UserDao extends SpecificDao<User> {

	public static final String DEFAULT_ORDER = asc("fullName");


	public static UserDao getInstance(){
		return getInstance(UserDao.class, User.class);
	}

	public static User getRootUser() {
		return UserDao.getByLogin("admin");
	}

	public static User get(String login) {
		return Dao.get(User.class, login);
	}

	public static User getByLogin(String login) {
		String where = ands(
				field("login").equalsTo(login)          //,field("status").isTrue() //não descomentar sem estudar o caso.
		);
		return Dao.uniqueResult(User.class, where);
	}

	public static User getByEmail(String email) {
//		and(field("status").isTrue())
		return Dao.uniqueResult(User.class, field("email").equalsTo(email));
	}

	public static boolean isMailMine(String login, String email) {
		boolean isMine = Dao.uniqueResult(User.class, field("login").equalsTo(login) + and(field("email").equalsTo(email))) != null;

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
		return Dao.paginationSlice(pageNum, pageSize, User.class, where, DEFAULT_ORDER);
	}

	public static List<User> getListByModuleAndRole(String moduleId, String roleId) {
		String where = ands(field("profile.modules.moduleId").equalsTo(moduleId),
				field("profile.roles.roleId").equalsTo(roleId));
		return Dao.list(User.class, where, "login asc");
	}

	public static List<User> getListByProfile(String profileId) {
		String where = field("profile.profileId").equalsTo(profileId);
		return Dao.list(User.class, where, "login asc");
	}

	// falta implementar
//	public static List listAllUsersWithoutProfile() {
//		return null;
//	}
	public static List<User> getListAll() {
		return Dao.list(User.class);
	}

	public static List<User> listAllUsersWithAProfile(String profileId) {
		return Dao.list(User.class,
				ands(field("profile.profileId").equalsTo(profileId), field("status").isTrue()),
				"login asc");
	}
}