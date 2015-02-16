package modules.admin.model.dao;

import modules.admin.model.entities.User;
import org.futurepages.core.persistence.EntityDao;
import org.futurepages.core.persistence.PaginationSlice;

import java.util.List;

public class UserDao extends EntityDao<User> {

	public final String DEFAULT_ORDER = asc("fullName");

	public User getRootUser() {
		return get("admin");
	}

	public static UserDao getInstance(){
		return getInstance(UserDao.class);
	}

	public User getByEmail(String email) {
		return uniqueResult(hql(User.class, field("email").equalsTo(email)));
	}

	public boolean isMailMine(String login, String email) {
		return uniqueResult(hql(User.class, ands(field("login").equalsTo(login) , field("email").equalsTo(email)))) != null;
	}

	// buscar por: Perfil, Módulo e Role. Separadamente
	public PaginationSlice<User> paginationSlice(int pageNum, int pageSize, String conteudoBusca, String profileId, String moduleId, String roleId) {

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
		return paginationSlice(pageNum, pageSize, hql(User.class, where, DEFAULT_ORDER));
	}

	public List<User> getListByModuleAndRole(String moduleId, String roleId) {
		String where = ands(field("profile.modules.moduleId").equalsTo(moduleId),
				field("profile.roles.roleId").equalsTo(roleId));
		return list(hql(User.class, where, "login asc"));
	}

	public List<User> getListByProfile(String profileId) {
		String where = field("profile.profileId").equalsTo(profileId);
		return list(hql(User.class, where, "login asc"));
	}

	public List<User> getListAll() {
		return list(User.class);
	}

	public List<User> listAllUsersWithAProfile(String profileId) {
		return list(hql(User.class, ands(field("profile.profileId").equalsTo(profileId), field("status").isTrue()),asc("login")));
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}