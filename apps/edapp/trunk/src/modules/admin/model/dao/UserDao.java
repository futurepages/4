package modules.admin.model.dao;

import modules.admin.model.entities.Module;
import modules.admin.model.entities.Role;
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

	/**
	 * @return the hql string corresponding to: fieldName = role[0] OR field = role[1] OR ... OR field = role[n]
	 */
	public static String whereRolesLikeField(User user, String fieldName) {
		List<Role> roles = user.getRoles();
		if(roles.size()>0){
			StringBuilder sb = new StringBuilder(field(fieldName).equalsTo(roles.get(0).getRoleId()));
			if(roles.size()>1){
				for (int i = 1; i < roles.size(); i++) {
					sb.append(or(field(fieldName).equalsTo(roles.get(i).getRoleId())));
				}
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * @return the hql string corresponding to: field = module[0] OR field = module[1] OR ... OR field = module[n]
	 */
	public static String whereModulesLikeField(User user, String fieldName) {
		List<Module> modules = user.getModules();
		if(modules.size()>0){
			StringBuilder sb = new StringBuilder(field(fieldName).equalsTo(modules.get(0).getModuleId()));
			if(modules.size()>1){
				for (int i = 1; i < modules.size(); i++) {
					sb.append(or(field(fieldName).equalsTo(modules.get(i).getModuleId())));
				}
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	public boolean hasOtherWithEmail(User user) {
		return numRows(hql(count("*"),User.class, ands(field("email").equalsTo(user.getEmail()),field("login").differentFrom(user.getLogin()))))>0;
	}

	public boolean hasOtherWithLogin(User user) {
		return numRows(hql(count("*"),User.class, field("login").equalsTo(user.getEmail())))>0;
	}
}