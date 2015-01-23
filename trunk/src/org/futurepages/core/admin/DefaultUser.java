package org.futurepages.core.admin;

import java.util.Collection;

/**
 * Interface de usuário padrão de uma aplicação futurepages.
 *
 * Os usuários de uma aplicação futurepages devem implementá-la para aproveitarem-se
 * dos recursos que o futurepages provê.
 * 
 * @author leandro
 */
public interface DefaultUser {
	
	public String getLogin();
	
	public String getFullName();

	public String getPassword();
	
	public Collection getRoles();

	public Collection getModules();
	
	public boolean hasModule(String moduleId);

	public boolean hasModules();
	
	public boolean hasRole(String roleId);

	public boolean hasRole(DefaultRole role);
	
	public String getInfo();

	public void setEmail(String email);

	public String getEmail();

}