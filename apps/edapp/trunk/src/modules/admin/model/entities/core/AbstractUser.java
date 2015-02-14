package modules.admin.model.entities.core;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import modules.admin.model.entities.User;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.auth.DefaultUser;

/**
 *
 * @author leandro
 */
@MappedSuperclass
public abstract class AbstractUser implements DefaultUser, Serializable {

	@OneToOne
	private User user;

	@Override
	public String getLogin() {
		return this.user.getLogin();
	}

	@Override
	public String getFullName() {
		return this.user.getFullName();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public Collection getRoles() {
		return this.user.getRoles();
	}

	@Override
	public Collection getModules() {
		return this.user.getModules();
	}

	@Override
	public boolean hasModule(String moduleId) {
		return this.user.hasModule(moduleId);
	}

	@Override
	public boolean hasModules() {
		return this.user.hasModules();
	}

	@Override
	public boolean hasRole(String roleId) {
		return this.user.hasRole(roleId);
	}

	@Override
	public boolean hasRole(DefaultRole role) {
		return this.user.hasRole(role);
	}

	@Override
	public void setEmail(String email) {
		this.user.setEmail(email);
	}

	@Override
	public String getEmail() {
		return this.user.getEmail();
	}

	@Override
	public String getInfo() {
		return this.toString()+" - "+this.user.toString();
	}

	public void setUser(User user){
		this.user = user;
	}
}