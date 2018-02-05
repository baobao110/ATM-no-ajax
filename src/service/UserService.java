package service;

import org.apache.ibatis.session.SqlSession;

import base.DataBase;
import inter.UserDAO;
import user.User;

public class UserService {

	public User register(String username,String password) {
		SqlSession session=DataBase.open(true);
		UserDAO dao=session.getMapper(UserDAO.class);
		User user=new User();
		user.setUsername(username);
		user.setPassword(password);
		if(dao.getUser(username)!=null) {
			return null;
		}
		int i=dao.addUser(user);
		if(i!=1) {
			System.out.println("ע��ʧ��");
			return null;
		}
		return user;
	}
	
	public User login(String username,String password) {
		SqlSession session=DataBase.open(true);
		UserDAO dao=session.getMapper(UserDAO.class);
		User user=dao.getUser(username);
		if(null==user) {
			System.out.println("�û���¼ʧ��");
			return null;
		}
		if(!password.equals(user.getPassword()))
		{
			System.out.println("�û���¼ʧ��");
			return null;
		}
		return user;
	}

}
