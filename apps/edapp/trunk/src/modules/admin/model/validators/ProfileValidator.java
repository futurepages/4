package modules.admin.model.validators;

import java.util.List;
import modules.admin.model.entities.Profile;
import modules.admin.model.dao.ProfileDao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

/**
 *
 * @author angelo
 */
public class ProfileValidator extends Validator {

	private void valida(Profile p) {
		List<Profile> allProfiles = ProfileDao.listAllOrderByLabel();
		if (p.getModules() != null) {
			for (Profile profile : allProfiles) {
				if (!profile.getProfileId().equals(p.getProfileId()) && profile.getModules().equals(p.getModules())) { // possui mesmos modules
					if (profile.getRoles().equals(p.getRoles()) && (profile.getAllowedProfiles().equals(p.getAllowedProfiles()))) { // possui mesmas roles
						error("Perfil possui mesma configuração de '" + profile.getLabel()+"'");
					}
				}
			}
		}
	}

	public void create(Profile profile) {
		if (profile.getProfileId().isEmpty() || profile.getLabel().isEmpty()) {
			error("Informe ID e descrição");
		}
		if (ProfileDao.getById(profile.getProfileId()) != null) {
			error("Perfil já cadastrado");
		}
		if (ProfileDao.getByDescription(profile.getLabel()) != null) {
			error("Perfil com mesma descrição já cadastrada");
		}
		if (profile.getModules() == null || profile.getModules().isEmpty()) {
			error("Selecione o(s) módulo(s) do perfil");
		}
		valida(profile);
		validate();
	}

	public void update(Profile profile) {
		Profile perfilComMesmaDesc = ProfileDao.getByDescription(profile.getLabel());
		if( perfilComMesmaDesc!= null && !perfilComMesmaDesc.equals(profile)){
			error("Perfil com mesma descrição já cadastrada");
		}
		if (profile.getProfileId().equals("0")) {
			error("Selecione um perfil para o usuário");
		}else if(Is.empty(profile.getLabel())){
			error("Informe a descrição do Perfil");
		}
		else {
			valida(profile);
		}
		validate();
	}
}
