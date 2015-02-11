package modules.admin.model.services;

import modules.admin.model.entities.Profile;
import modules.admin.model.entities.Role;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author TJPI
 */
public class ProfileServices {

	private static final Role ADMIN_PROFILER = new Role("adminProfiler");
	private static final Role SUPER_PROFILE = new Role("*");

	/**
	 * Sincroniza com a base de dados um perfil preenchido por um usuário em um formulário
	 * 
	 * @param profileForm é o que se supõe ser os novos valores para o profile na base dados.
	 * @return profile salvo no banco de dados (não destacado)
	 * @throws Exception 
	 */
//	public static Profile sync(Profile profileForm) throws Exception {
//		Profile profileExistente = Dao.get(Profile.class, profileForm.getProfileId());
//		Dao.evict(profileExistente);
//		if (profileExistente == null) {
//			//cria pela primeira Vez
//			return Dao.save(profileForm);
//		} else {
//			//já existia antes o profile:
//			return Dao.update(profileForm);
//		}
//	}

}
