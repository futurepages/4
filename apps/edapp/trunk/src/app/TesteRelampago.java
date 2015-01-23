package app;

import modules.admin.dao.UserDao;

public class TesteRelampago {

	public static void main(String[] args) {
		System.out.println(UserDao.getInstance().getIdName());
	}
}
