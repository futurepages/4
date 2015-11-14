package org.futurepages.menta.filters;

import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.auth.Authentication;
import org.futurepages.menta.core.action.AuthenticationFree;
import org.futurepages.core.auth.DefaultRole;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;

/**
 * Usado individualmente por action, para proteger uma action
 * de acordo com a role e/ou o tipo do usuário logado.
 * 
 * Verifica se o suário logado é do tipo e/ou se tem o role passados como parâmetros.
 * 
 * @author leandro
 */
public class PermissionFilter implements Filter {

	private String[] roles;
	private Class userType;
	private Class actionType;

	public PermissionFilter() {
	}

	public <T extends DefaultUser> PermissionFilter(Class<T> userType, String... roles) {
		this.roles = roles;
		this.userType = userType;
	}

	public <T extends DefaultUser> PermissionFilter(Class<T> userType) {
		this.userType = userType;
	}

	public <T extends DefaultUser> PermissionFilter(Class<T> userType, Class actionType) {
		this.userType = userType;
		this.actionType = actionType;
	}

	public PermissionFilter(String... roles) {
		this.roles = roles;
	}

	public PermissionFilter(DefaultRole... roles) {
		this.roles = new String[roles.length];
		for (int i = 0; i < roles.length; i++) {
			this.roles[i] = roles[i].getRoleId();
		}
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {

		AbstractAction action = (AbstractAction) chain.getAction();

		if (action.loggedUser() != null) {

			if (AuthenticationFree.class.isAssignableFrom(action.getClass())) {
				if (((AuthenticationFree) action).bypassAuthentication(chain.getInnerAction())) {
					return chain.invoke();
				}
			}

			if (actionType == null && userType != null) {
				if (!userType.isAssignableFrom(action.loggedUser().getClass())) {
					return Authentication.accessDenied(chain);
				}
			} else if (actionType != null) {
				if (actionType.isAssignableFrom(action.getClass())
						&& !userType.isAssignableFrom(action.loggedUser().getClass())) {
					return Authentication.accessDenied(chain);
				}
			}

			if (roles != null) {
				boolean hasPermission = false;
				for (String roleId : roles) {
					if (action.loggedUser().hasRole(roleId)) {
						hasPermission = true;
						break;
					}
				}
				if(!hasPermission){
					return Authentication.accessDenied(chain);
				}
			}
		}

		return chain.invoke();
	}

	@Override
	public void destroy() {
	}
}
