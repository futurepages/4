package apps;

import modules.admin.model.dao.UserDao;
import org.futurepages.core.config.Apps;

public class FlashTests {

	public static void main(String[] args) {
		if(Apps.devMode()){
			System.out.println(UserDao.getInstance().list().get(0).getFullName());
		}
	}
}
