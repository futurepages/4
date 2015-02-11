
package modules.admin.model.dao;

import java.util.List;
import modules.admin.model.entities.Profile;
import modules.admin.model.core.DefaultProfile;
import modules.admin.model.entities.enums.AdminProfilesEnum;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Jorge Rafael
 */
public class ProfileDao extends Dao {


    public static List<Profile> list() {
        return Dao.getInstance().list(hql(Profile.class, null, asc("profileId")));
    }

	public static List<Profile> listByLabel(String inputText) {
		return Dao.getInstance().list(hql(Profile.class, field("label").contains(inputText), asc("label")));
	}

    public static List<Profile> listOrderByLabel() {
        return Dao.getInstance().list(hql(Profile.class, field("profileId").differentFrom(AdminProfilesEnum.SYSTEM_SUPER), asc("label")));
    }

    public static List<Profile> listAllOrderByLabel() {
        return Dao.getInstance().list(hql(Profile.class, null, asc("label")));
    }

	public static List<Profile> listAllProfilesSelected(String[] idsProfile) {
        return Dao.getInstance().list(hql(Profile.class, field("profileId").in(idsProfile)));
    }

    public static Profile get(DefaultProfile profile) {
        return Dao.getInstance().get(Profile.class, profile.getProfileId());
    }

    public static Profile getById(String profileId) {
		// TODO result unique e trazer apenas os ativos
        return Dao.getInstance().get(Profile.class, profileId);
    }

    public static Profile save(String profileId, String lablel, boolean status) {
        Profile profileAdmin = new Profile(profileId, lablel, status);
        Dao.getInstance().save(profileAdmin);
        return profileAdmin;
    }

	public static Profile getByDescription(String desc) {
         return Dao.getInstance().uniqueResult(hql(Profile.class, field("label").equalsTo(desc)));
    }
}