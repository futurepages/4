package modules.admin.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import modules.admin.model.entities.Role;
import modules.admin.model.entities.User;
import modules.admin.model.core.AdminConstants;

import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

public class RoleDao extends HQLProvider {


	public static List<Role> list(){
		return Dao.getInstance().list(hql(Role.class, null, asc("title")));
	}

	//lista as roles que o usuario não possui
	public static List<Role> listAllRolesInUser(User userX) {

		String roleCondition;
		ArrayList<String> stringRoles = new ArrayList<String>();
		for (Role r : userX.getRoles()) {
			if (!r.getRoleId().equals(AdminConstants.SUPER_ID)) {
				stringRoles.add(r.getRoleId());
			}
		}

		if (stringRoles.size() > 0) {
			roleCondition = field("roleId").notIn(stringRoles);
		} else {
			roleCondition = null;
		}

		return Dao.getInstance().list(hql(Role.class, roleCondition, asc("title")));
	}

	public static List<Role> listAllRolesSelected(String[] id_roles) {
		return Dao.getInstance().list(hql(Role.class, field("roleId").in(id_roles)));
	}

	public static List<Role> listOrderByTitle() {
		return Dao.getInstance().list(hql(Role.class, field("roleId").differentFrom(AdminConstants.SUPER_ID), asc("title")));
	}

	public static List<Role> listAllOrderByTitle() {
		return Dao.getInstance().list(hql(Role.class, null, "title"));
	}

	public static Role get(DefaultRole role) {
		return Dao.getInstance().get(Role.class, role.getRoleId());
	}

	public static Role getById(String roleId) {
		return Dao.getInstance().get(Role.class, roleId);
	}

	public static Role save(String roleId, String title) {
		Role role = new Role();
		role.setRoleId(roleId);
		role.setTitle(title);
		Dao.getInstance().save(role);
		return role;
	}

	public static Role save(DefaultRole role, String title) {
		return save(role.getRoleId(), title);
	}

	public static Role save(DefaultRole roleDef, String title, String moduleId) {
		Role role = new Role();
		role.setRoleId(roleDef.getRoleId());
		role.setTitle(title);
		role.setModule(ModuleDao.getById(moduleId));
		return Dao.getInstance().save(role);
	}

	public static List<Role> listByModule(String moduleId) {
		return Dao.getInstance().list(hql(Role.class, field("module.moduleId").equalsTo(moduleId), asc("title")));
	}

	public static Map<String, String> mapByModule(String moduleId){
		String where = ands(field("module.moduleId").equalsTo(moduleId));
		return Dao.getInstance().map(hql("roleId, title", Role.class, where, asc("title")));
	}

	public static Map<String, String> mapAll(){
		return Dao.getInstance().map(hql("roleId,title",Role.class, null, asc("title")));
	}

	public static String whereBasic(List<Role> roles, String field) {
		if (!roles.get(0).getRoleId().equals(AdminConstants.SUPER_ID)) {
			List<String> roleIds = new ArrayList<String>();
			for (Role role : roles) {
				roleIds.add(role.getRoleId());
			}
			return field(field).inList(roleIds);
		}
		return null;
	}
}
